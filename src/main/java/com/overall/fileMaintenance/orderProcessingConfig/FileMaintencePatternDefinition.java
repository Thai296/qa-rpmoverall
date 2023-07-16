package com.overall.fileMaintenance.orderProcessingConfig;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class FileMaintencePatternDefinition {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public FileMaintencePatternDefinition(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	public WebElement addNewPatternBtn() throws Exception {
		return driver.findElement(By.id("btnAddRw1"));
	}
	
	public WebElement patternDefinitionCountText() throws Exception {
		return driver.findElement(By.id("oRwCnt"));
	}
	
	public WebElement patternDefinitionTable() throws Exception {
		return driver.findElement(By.id("oTable"));
	}
	
	public WebElement patternDefinitionDescrInput(int row, String descr) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#oTable > tbody > tr:nth-child(" + row + ") > td:nth-child(3) > :textarea[name=text]')).val(\"" + descr + "\")).trigger('onblur=cellChanged(this)')[0]"); 
	}
	
	public WebElement patternDefinitionRegexInput(int row, String regex) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#oTable > tbody > tr:nth-child(" + row + ") > td:nth-child(4) > :input[name=pttrn][class=nbrdr]')).val(\"" + regex + "\")).trigger('onblur=cellChanged(this)')[0]"); 
	}
	
	public WebElement patternDefinitionDescrText(int row) throws Exception {
		return driver.findElement(By.xpath("//*[@id='oTable']/tbody/tr[" + row + "]/td[3]/textarea[@id='text']")); 
	}
	
	public WebElement patternDefinitionRegexText(int row) throws Exception {
		return driver.findElement(By.xpath("//*[@id='oTable']/tbody/tr[" + row + "]/td[4]/input[1]")); 
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}
	

	
	public void setPatternDefinition(String descr, String regex) throws Exception{
		int count = Integer.parseInt(patternDefinitionCountText().getText());
		addNewPatternBtn().click();
		patternDefinitionDescrInput(count+1, descr);
		patternDefinitionRegexInput(count+1, regex);
		submitBtn().click();
		resetBtn().click();
		logger.info("Submitted Pattern Definition");
	}
	
	public void setPatternDefinitionNoSubmit(String descr, String regex) throws Exception{
		int count = Integer.parseInt(patternDefinitionCountText().getText());
		addNewPatternBtn().click();
		patternDefinitionDescrInput(count+1, descr);
		patternDefinitionRegexInput(count+1, regex);
		logger.info("Entered Pattern Definition");
	}
	
	public String getPatternDescr(int i) throws Exception{
		int count = Integer.parseInt(patternDefinitionCountText().getText());
		count = count - i;
		return patternDefinitionDescrText(count).getText();
	}
	
	public String getPatternRegex(int i) throws Exception{
		int count = Integer.parseInt(patternDefinitionCountText().getText());
		count = count - i;
		return patternDefinitionRegexText(count).getAttribute("value");
	}
	
	public void resetPatternDefinition() throws Exception{
		resetBtn().click();
		logger.info("Reset Pattern Definition");
	}
		

}
