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

public class PatientDemographicSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public PatientDemographicSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement patientIdInput() throws Exception {
		return driver.findElement(By.id("ptId"));
	}
	
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.id("search"));
	}
	
	//-------------------------------------------------------------------------------------
	public void setPatientId(String str) throws Exception{		
		patientIdInput().sendKeys(str);
		patientIdInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient ID: " + str);
	}

}
