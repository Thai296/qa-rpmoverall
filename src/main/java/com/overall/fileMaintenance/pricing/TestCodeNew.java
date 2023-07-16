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

public class TestCodeNew {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private SeleniumBaseTest b;
	
	public TestCodeNew(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement testIdText(){
		return driver.findElement(By.id("testAbbrv"));
	}	
	
	public WebElement testCodeSearchIconBtn() throws Exception{
		return driver.findElement(By.xpath("//*[@id='test_code_search_btn']/span"));
	}
	
	public WebElement resetBtn() throws Exception{
		return driver.findElement(By.id("Reset"));
	}
	
	public void clickTestCodeSearchIconBtn() throws Exception{
		testCodeSearchIconBtn().click();
		logger.info("        Clicked Test Code Search icon button.");
	}
	
	public void clickResetBtn() throws Exception{
		resetBtn().click();	
		logger.info("        Clicked on Reset button.");
	}
	
	
}
