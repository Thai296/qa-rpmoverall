package com.overall.fileMaintenance.sysMgt;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class DSClientSearch {

	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public DSClientSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement searchBtn() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[name=\"search\"]')[0]");
	}
	
	public WebElement searchBtnNew() throws Exception {		
		return driver.findElement(By.xpath(".//*[@id='clientSearch']/div/button[1]"));
	}
	
	public WebElement clientID() throws Exception {
		return driver.findElement(By.id("clientId"));
	}
	
	public WebElement clientSearchGrid(int row, int col) throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_clientsearchresults']/tbody/tr["+row+"]/td["+col+"]/a"));
	}
	
	public void inputClientID(String text) throws Exception{
		clientID().clear();
		clientID().sendKeys(text);
		logger.info("        Input "+text+" to ClientID");
	}
	
}
