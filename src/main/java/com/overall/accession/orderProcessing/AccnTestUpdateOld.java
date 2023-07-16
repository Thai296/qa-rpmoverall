package com.overall.accession.orderProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class AccnTestUpdateOld {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public AccnTestUpdateOld(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement accnIdInput(String accnId) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#accnId')).val(\"" + accnId + "\")).trigger('onblur')[0]"); 
	}
	
	//Errors
	public WebElement accessionErrorsText() throws Exception {
		return driver.findElement(By.cssSelector("#acnLabComponent > table:nth-child(1) > tbody > tr:nth-child(2) > td"));
	}
	
	//Overload
	public WebElement accnIdInput() throws Exception {
		return driver.findElement(By.id("accnId"));
	}
	
	public WebElement ptNameInput(String pName) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#acnLabComponent > table tbody > tr > td > input[value=\"" + pName + "\"]')[0]"); 
	}
	
	//Overload
	public WebElement ptNameInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#acnLabComponent > table:nth-child(3) > tbody > tr:nth-child(3) > td:nth-child(4) > input')[0]");
	}
	
	public WebElement ptAddr1Input() throws Exception {
		return driver.findElement(By.id("ptAddr1"));
	}
	
	public WebElement finalRptDtInput(String dt) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#finalRptDt')).val(\"" + dt + "\")).trigger('onchange')[0]");
	}
	
	public WebElement finalRptDtInput() throws Exception {
		return  driver.findElement(By.id("finalRptDt"));
	}
	
	public WebElement forceToCorrespCheckbox() throws Exception {
		return driver.findElement(By.id("forceToCorresp"));
	}
	
	public WebElement reasonCodeText() throws Exception {
		return driver.findElement(By.id("errCd"));
	}
	
	//Ordered Test Details
	public WebElement orderTestDetailsMod1Dropdown() throws Exception {
		return driver.findElement(By.id("mod1"));
	}
	
	public WebElement orderTestDetailsMod1Text() throws Exception {
		return driver.findElement(By.id("mod1"));
	}
	
	//Patient Demographics
	public WebElement createNewEpiBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($(':button[title=\"Create New EPI\"]')).get(0)"); 
	}
	
	public WebElement epiInput() throws Exception {
		return driver.findElement(By.id("ptId"));
	}
	
		//Create New Patient Popup
		public WebElement createNewPatientPopupTitleText() throws Exception {
			return driver.findElement(By.id("ui-dialog-title-genEPI"));
		}
		
		public WebElement createNewPatientPopupOkBtn() throws Exception {
			return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('div:nth-child(12) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(2) > span')).get(0)"); 
		}
	
		
	//Insurance Info
	public WebElement payorIdInput() throws Exception {
		return driver.findElement(By.id("pyrAbbrv"));
	}	
		
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	
	
	public void searchAccnId(String accnId) throws Exception{
		accnIdInput(accnId);
		logger.info("Entered AccnID: " + accnId);
	}
	
	public void setforceToCorresp() throws Exception{
		forceToCorrespCheckbox().click();;
		submitBtn().click();
		logger.info("Submitted Force to Corresp");
	}
	
	public void naviateToCreateNewPatient() throws Exception{
		createNewEpiBtn().click();
		logger.info("Clicked Create New EPI button");
	}
	
	public void addEpi() throws Exception{
		createNewPatientPopupOkBtn().click();
		submitBtn().click();
		logger.info("Submitted EPI");
	}
	
	public void setPayorId(String payorId) throws Exception{
		payorIdInput().clear();
		payorIdInput().sendKeys(payorId);
		submitBtn().click();
		logger.info("Submitted PayorId");
	}
	
	public void selectOrderTestDetailMod(SeleniumBaseTest b, String value) throws Exception{
		orderTestDetailsMod1Dropdown().click();
		b.selectItem(orderTestDetailsMod1Dropdown(), value);
		submitBtn().click();
		logger.info("Submitted Modifier: " + value);
	}
	
	public void setFinalRptDt(String dt) throws Exception{
		finalRptDtInput().clear();
		finalRptDtInput(dt);
		logger.info("Submitted Final Report Date: " + dt);
	}
	
	public void submitAccn() throws Exception{
		submitBtn().click();
		logger.info("Submitted Accn");
	}
	

}
