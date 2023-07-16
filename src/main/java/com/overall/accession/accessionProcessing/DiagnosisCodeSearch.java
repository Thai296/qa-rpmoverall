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

public class DiagnosisCodeSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public DiagnosisCodeSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	public WebElement closeBtn() throws Exception {
		return driver.findElement(By.id("cancel"));
	}
	
	public WebElement DiagTypeDropdown() throws Exception {
		return driver.findElement(By.id("DiagTyp"));
	}
	
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.id("search"));
	}
	
	public void closePopup() throws Exception{
		closeBtn().click();
		logger.info("Clicked Close button");
	}
	
	//-------------------------------------------------------------------------------------
	public void setDiagnosisDxType(SeleniumBaseTest b, String type) throws Exception{
		b.selectItemByVal(DiagTypeDropdown(), type);
		searchBtn().click();
		logger.info("        Search Diagnosis Code");
	}

}
