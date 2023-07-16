package com.overall.fileMaintenance.pricing;

//this is only used in testRPM_784 by Production Deployment Smoke Tests

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.JavascriptExecutor;

import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class TestCodeSearchNew {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private SeleniumBaseTest b;
	
	public TestCodeSearchNew(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	
	public WebElement testCodeIdInput(){
		return driver.findElement(By.id("testAbbrev"));
	}
	
	public WebElement searchBtn() throws Exception{
		return driver.findElement(By.name("Submit"));
	}
	
	public void setTestCodeId(String value){
		testCodeIdInput().sendKeys(value);
		testCodeIdInput().sendKeys(Keys.TAB);
		logger.info("       Enter to Test Code: " + value);
	}
	
	
	public void clickSearchBtn() throws Exception{
		searchBtn().click();
		logger.info("        Clicked Search icon button.");
	}
	
	
	
}
