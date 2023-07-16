package com.overall.fileMaintenance.sysMgt;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class DSPayorSearch {

	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public DSPayorSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement payorIdSearchIcon() throws Exception {
		return driver.findElement(By.xpath("html/body/div[2]/div[2]/div[2]/div[5]/div[2]/div[2]/a[3]/span"));
	}	
	
	public WebElement payorIdTextbox() throws Exception {
		return driver.findElement(By.id("payorAbbrev"));
	}	
	
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.xpath("html/body/section/div/div/div/button[1]"));
	}	
	
	public void clickPayorIdSearchIcon() throws Exception {
		payorIdSearchIcon().click();
		logger.info("        Click to Payor Id Search Icon ");
	}	
	
	public void enterPayorId(String value) throws Exception {
		payorIdTextbox().sendKeys(value);
		logger.info("        Enter to Payor Id textbox : "+value);
	}	
}
