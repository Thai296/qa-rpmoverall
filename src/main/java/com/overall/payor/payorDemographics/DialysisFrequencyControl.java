package com.overall.payor.payorDemographics;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class DialysisFrequencyControl {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public DialysisFrequencyControl(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}		
	
	public WebElement dialysisFreqControlIDInput() throws Exception {
		return driver.findElement(By.id("displayDialFrqCtrlID"));
	}
	
	public WebElement dialysisFreqControlDescInput() throws Exception {
		return driver.findElement(By.id("DialFrqCtrlDesc"));
	}
	
	public WebElement addPyrGroupBtn() throws Exception {
		return driver.findElement(By.id("btnAddPyrGrp"));
	}
	
	public WebElement addPyrBtn() throws Exception {
		return driver.findElement(By.id("btnAddPyr"));
	}
	
	public boolean isPyrGrpDeleteColVisible(int row1, int row2) throws Exception {			
		return (boolean) ((JavascriptExecutor) driver).executeScript("return $($('#pyrGrpTable > tbody > tr:nth-child(" + row1 + ") > td:nth-child(" + row2 + ") > input.nbrdr')[0]).is(\":visible\")"); 
			
	}
	
	public boolean isPyrDeleteColVisible(int row1, int row2) throws Exception {			
		return (boolean) ((JavascriptExecutor) driver).executeScript("return $($('#pyrTable > tbody > tr:nth-child(" + row1 + ") > td:nth-child(" + row2 + ") > input.nbrdr')[0]).is(\":visible\")"); 
			
	}
	
	//-------------------------------------------------------------------------------------
	public void setDialysisFreqControlID(String str) throws Exception{		
		dialysisFreqControlIDInput().sendKeys(str);
		dialysisFreqControlIDInput().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("        Entered Dialysis Frequency Control ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void clickAddPyrGrp() throws Exception{		
		addPyrGroupBtn().click();
		logger.info("        Clicked Add Payor Group button.");
	}
	
	//-------------------------------------------------------------------------------------
	public void clickAddPyr() throws Exception{		
		addPyrBtn().click();
		logger.info("        Clicked Add Payor button.");
	}
	


}
