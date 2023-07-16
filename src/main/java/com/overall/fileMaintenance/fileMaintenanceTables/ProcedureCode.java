package com.overall.fileMaintenance.fileMaintenanceTables;

import java.util.List;

import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;


public class ProcedureCode {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private SeleniumBaseTest b;
	
	public ProcedureCode(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}		

	
}
