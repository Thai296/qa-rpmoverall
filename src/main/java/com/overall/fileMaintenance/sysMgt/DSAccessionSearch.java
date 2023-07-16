package com.overall.fileMaintenance.sysMgt;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DSAccessionSearch
{

	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public DSAccessionSearch(RemoteWebDriver driver)
	{
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement closeButton()
	{
		return driver.findElement(By.name("Close"));
	}
	
	public WebElement accnIdInput()
	{
		return driver.findElement(By.id("accnId"));
	}
	
	public WebElement searchButton()
	{
		return driver.findElement(By.name("Submit"));
	}
	
	public WebElement searchGrid(int row, int col)
	{
		return driver.findElement(By.xpath(".//*[@id='accessionsearchTable']/tbody/tr["+row+"]/td["+col+"]/a"));
	}

	public void inputAccnID(String text)
	{
		accnIdInput().clear();
		accnIdInput().sendKeys(text);
	}
}
