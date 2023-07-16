package com.overall.accession.accessionProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class AccessionDiagnosis {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public AccessionDiagnosis(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	public WebElement accnIdInput(String accnId) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#accnId')).val(\"" + accnId + "\")).trigger('onblur')[0]"); 
	}
	
	public WebElement accnIdInput() throws Exception {
		return driver.findElement(By.id("accnId"));
	}
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement epiInput() throws Exception {
		return driver.findElement(By.id("ptId"));
	}
	
	public WebElement ptNameInput() throws Exception {
		return driver.findElement(By.id("ptNm"));
	}
	
	public WebElement accnStatusTypeInput() throws Exception {
		return driver.findElement(By.id("accStatTyp"));
	}
	
	public WebElement dxCodeInput() throws Exception {
		return driver.findElement(By.id("diags"));
	}
	
	public WebElement commentInput() throws Exception {
		return driver.findElement(By.id("comment"));
	}
	
	public WebElement docRecvdDropdown() throws Exception {
		return driver.findElement(By.id("docRecvd"));
	}
	
	public WebElement clientContactInput() throws Exception {
		return driver.findElement(By.id("clnCntct"));
	}
	
	public WebElement dateInput() throws Exception {
		return driver.findElement(By.id("clnCntctDt"));
	}
	
	public WebElement diagSearchBtn() throws Exception {
		return driver.findElement(By.id("diagSearchBtn"));
	}
		
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}

	public WebElement appendFileText() {
		return driver.findElementByXPath("//*[@id='mainSections']/div/div[3]/section/header/div[1]/span/span");
	}

	public WebElement appendFileBtn() {
		return driver.findElement(By.id("appendFileBtn"));
	}
	
	
	public void searchAccnId(String accnId) throws Exception{
		accnIdInput(accnId);
		logger.info("Entered AccnID: " + accnId);
	}
	
	public void navigateToDiagnosisCode() throws Exception{
		diagSearchBtn().click();
		logger.info("Clicked Diagnosis Search button");
	}
	
	public void resetDiagnosis() throws Exception{
		resetBtn().click();
		logger.info("Clicked Reset button");
	}
	
	public void setDxCode(String dxCode) throws Exception{
		dxCodeInput().sendKeys(dxCode);
		logger.info("Entered: " + dxCode);
	}
	
	public void setDiagnosisInfo(SeleniumBaseTest b) throws Exception{
		commentInput().sendKeys("comments in the comments field");
		b.selectItem(docRecvdDropdown(), "STDPTERR");
		clientContactInput().sendKeys("qatester");
		submitBtn().click();
		logger.info("Submitted Diagnosis Info");
	}
}
