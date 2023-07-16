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

public class DiagnosisCodeSearchResults {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public DiagnosisCodeSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement diagonsisCodeSearchResultText(int row, int col) throws Exception {							
		return driver.findElement(By.cssSelector("#diagTable > tbody > tr:nth-child(" + row + ") > td:nth-child(" + col + ") > input"));		
	}
	
	/*public WebElement selectDxCode(int row, int col) throws Exception {
		System.out.println("$($('#diagTable > tbody > tr:nth-child(" + row + ") > td:nth-child(" + col + ") > input')).trigger('click')");
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#diagTable > tbody > tr:nth-child(" + row + ") > td:nth-child(" + col + " > input')).trigger('click')"); 

	}*/
	
	public WebElement diagonsisCodeSearchResultTable() throws Exception {
		return driver.findElement(By.id("diagTable")); 
	}
	
	public void selectDxCode(int col, int row, String dxCode) throws Exception{
		driver.executeScript("$('#diagTable :input[value=" + dxCode + "]').click()");
	}

}
