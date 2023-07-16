package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.support.ui.Select;


public class EligibilitySubServiceSearchResults {
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public EligibilitySubServiceSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement eligibilitySubSvcSearchTbl() throws Exception{
		return driver.findElement(By.id("eligibilitySubSvcSearch"));
	}
	
	public WebElement eligibilitySubSvcSearchCelData(int row, int col) throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_eligibilitySubSvcSearch']/tbody/tr["+row+"]/td["+col+"]/a"));
	}
	
	public WebElement eligibilitySubSvcSearchTblTitle() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='gview_tbl_eligibilitySubSvcSearch']/div[1]/span"));
	}

}
