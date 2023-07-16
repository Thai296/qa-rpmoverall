package com.overall.client.clientProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EligCensusConfig {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;

	public EligCensusConfig(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		this.wait=wait;
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
	
	public WebElement acctTypInput() throws Exception {
		return driver.findElement(By.id("acctTyp"));	
	}
	
	public WebElement rosterValidThroughDateInput() throws Exception {
		return driver.findElement(By.id("valDt"));	
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));	
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));	
	}
	
	public WebElement helpBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#contentarea').contents()).contents().find('#helpbtn')[0]).click()"); 
	}
	
	public String errMsgText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#errorCodeBlockTbl > tbody > tr:nth-child(3) > td:nth-child(2)').text()"); 
	}
	
	public String ptIdErrMsgText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#xf_msgFor_ptId').text()"); 
	}
	
	public String ptSSNErrMsgText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#xf_msgFor_ptSsn').text()"); 
	}
	
	//Patient Update
	public WebElement epiInput() throws Exception {
		return driver.findElement(By.id("ptId"));	
	}	
	
	public WebElement lNameInput() throws Exception {
		return driver.findElement(By.id("srchPtLName"));	
	}
	
	public WebElement fNameInput() throws Exception {
		return driver.findElement(By.id("srchPtFName"));	
	}
	
	public WebElement dobInput() throws Exception {
		return driver.findElement(By.id("ptDob"));	
	}
	
	public WebElement ssnInput() throws Exception {
		return driver.findElement(By.id("ptSsn"));	
	}
	
	public WebElement clnPtIdInput() throws Exception {
		return driver.findElement(By.id("clnPtId"));	
	}
	
	public WebElement clnFacPtIdInput() throws Exception {
		return driver.findElement(By.id("facPtId"));	
	}
	
	public WebElement createNewEPIBtn() throws Exception {
		return driver.findElement(By.id("btnEpiGen"));	
	}
	
	public WebElement effDateInput() throws Exception {
		return driver.findElement(By.id("effDt"));	
	}
	
	public WebElement expDateInput() throws Exception {
		return driver.findElement(By.id("expDt"));	
	}
	
	public WebElement daysInput() throws Exception {
		return driver.findElement(By.id("days"));	
	}
	
	public WebElement rptDateInput() throws Exception {
		return driver.findElement(By.id("eligRptDt"));	
	}
	
	public WebElement commentsInput() throws Exception {
		return driver.findElement(By.id("comments"));	
	}
	
	public WebElement deleteCheckBox() throws Exception {
		return driver.findElement(By.id("delete"));	
	}
	
	public WebElement newEffDateInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"tbbdy1\"] [name=\"effDt\"]')[0]"); 
	}
	
	public WebElement addRowBtn() throws Exception {
		return driver.findElement(By.id("btnAdd"));	
	}
	
	public WebElement ptDetailsBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"btnPtDtl\"]')[0]");	
	}
	
	//Create New Patient popup window	
	public String createNewPtPageTitleText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#ui-dialog-title-genEPI').text()"); 
	}
	
	public WebElement sysGenPtLNameInput() throws Exception {
		return driver.findElement(By.id("sysGen_lname"));	
	}
	
	public WebElement okBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[aria-labelledby*=\"genEPI\"] [type=\"button\"]')[1]"); 
	}
	
	//Patient Permanent Record Update popup window
	public WebElement ptRecordUpdatePopup() throws Exception {
		return driver.findElement(By.id("ui-dialog-title-genCPSI"));	
	}
	
	public WebElement okBtnInPtUpdatePopup() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"ui-dialog-buttonpane\"] [type=\"button\"]')[5]"); 
	}
	
	//Associated Patient IDs popup window
	public WebElement asscPtIdPopup() throws Exception {
		return driver.findElement(By.id("ui-dialog-title-miniPtDiv"));	
	}
	
	public WebElement asscPtIdTable() throws Exception {
		return driver.findElement(By.id("miniPtTable"));	
	}
	
	public WebElement closeBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"ui-icon-closethick\"]')[3]"); 
	}

	public WebElement eligibilityCensusConfigLoadClientIdInput()throws Exception{
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lookupClientId")));
	}
	//-------------------------------------------------------------------------------------
	public void setRosterValidThroughDt(String str) throws Exception{		
		rosterValidThroughDateInput().clear();
		Thread.sleep(2000);
		rosterValidThroughDateInput().sendKeys(str);
		rosterValidThroughDateInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Date Roster is Valid Through: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setNewEffDt(String str) throws Exception{		
		newEffDateInput().clear();
		Thread.sleep(2000);
		newEffDateInput().sendKeys(str);
		newEffDateInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Eff Date: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setlName(String str) throws Exception{		
		lNameInput().clear();
		Thread.sleep(2000);
		lNameInput().sendKeys(str);
		lNameInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Last Nm: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setfName(String str) throws Exception{		
		fNameInput().clear();
		Thread.sleep(2000);
		fNameInput().sendKeys(str);
		fNameInput().sendKeys(Keys.TAB);	
		logger.info("        Entered First Nm: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setDOB(String str) throws Exception{	
		dobInput().clear();
		Thread.sleep(2000);
		dobInput().sendKeys(str);
		dobInput().sendKeys(Keys.TAB);	
		logger.info("        Entered DOB: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setSSN(String str) throws Exception{	
		ssnInput().clear();
		Thread.sleep(2000);
		ssnInput().sendKeys(str);
		ssnInput().sendKeys(Keys.TAB);	
		logger.info("        Entered SSN: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setRptDate(String str) throws Exception{		
		rptDateInput().clear();
		Thread.sleep(2000);
		rptDateInput().sendKeys(str);
		rptDateInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Rpt Date: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setExpDate(String str) throws Exception{	
		expDateInput().clear();
		expDateInput().sendKeys(str);
		expDateInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Exp Date: " + str);
	}

	//-------------------------------------------------------------------------------------
	public void setClnPtId(String str) throws Exception{		
		clnPtIdInput().clear();
		Thread.sleep(2000);
		clnPtIdInput().sendKeys(str);
		clnPtIdInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Client Pt ID: " + str);
	}	

	//-------------------------------------------------------------------------------------
	public void setClnFacPtId(String str) throws Exception{		
		clnFacPtIdInput().clear();
		Thread.sleep(2000);
		clnFacPtIdInput().sendKeys(str);
		clnFacPtIdInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Client's Primary Facility Pt ID: " + str);
	}		
	
	//-------------------------------------------------------------------------------------
	public void setComments(String str) throws Exception{	
		commentsInput().clear();
		Thread.sleep(2000);
		commentsInput().sendKeys(str);
		commentsInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Comments: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setEPI(String str) throws Exception{		
		epiInput().sendKeys(str);
		epiInput().sendKeys(Keys.TAB);	
		logger.info("        Entered EPI: " + str);
	}

	//-------------------------------------------------------------------------------------
	public void setEffDate(String str) throws Exception{		
		effDateInput().sendKeys(str);
		effDateInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Eff Date: " + str);
	}		
	
	//-------------------------------------------------------------------------------------
	public void setSysGenPtLName(String str) throws Exception{		
		sysGenPtLNameInput().sendKeys(str);
		sysGenPtLNameInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Patient Last Name: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setClnID(String str) throws Exception{		
		clnIdInput().sendKeys(str);
		clnIdInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Client ID: " + str);
	}		

}
