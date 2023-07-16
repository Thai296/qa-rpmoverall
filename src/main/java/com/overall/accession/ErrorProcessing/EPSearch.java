package com.overall.accession.ErrorProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EPSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;

	public EPSearch(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		this.wait=wait;
	}

	public WebElement filter1Dropdown()  {
		return driver.findElement(By.id("filter1"));
	}
	
	public WebElement filter1Input()  {
		return driver.findElement(By.id("val1"));
	}
		
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	public WebElement select2DropInput()
	{
		return driver.findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
	}

	public WebElement epSearchSearchIdInput(){
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lookupSearchId")));
	}
	//------------------------------------------------------------------------------------------------------------------------------

	public void setFilter1(String str, String value) throws Exception{
		Select select = new Select(filter1Dropdown());
		select.selectByValue(str);
		filter1Input().sendKeys(value);
		logger.info("Selected Filter1: " + str);
		logger.info("Entered value in Filter1 : " + value);
	}

}
