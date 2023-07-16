package com.overall.financialManagement.endOfMonth;

//import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FinancialManagement
{
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;

	public FinancialManagement(RemoteWebDriver driver, WebDriverWait wait){
		this.driver=driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		this.wait = wait;
	}
	
	public WebElement menuFrame() {
		return driver.findElement(By.id("menu"));		
	}
	
	public WebElement contentFrame() {
		return driver.findElement(By.id("content"));		
	}

	public WebElement rpmiFrame(){
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rpmiframe")));
	}
	
	public WebElement platformIFrame() throws Exception {
		return driver.findElement(By.id("platformiframe"));
	}
	
	public WebElement closingPackageLink() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Closing Package)'))[0].click();");	
	}
	
	public WebElement closingPackage() throws Exception {
//		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Closing Package)'))[0];");	
		return driver.findElement(By.xpath("//*[@id='vMenu']/tbody/tr[1]/td/ul[1]/li[4]/a"));
	}
	
	public WebElement accountingPeriodDropdown() throws Exception {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accountingDateDroplist")));
	}
	
	public WebElement facilityDropdown() throws Exception {
		return driver.findElement(By.id("facilityDroplist"));	
	}
	
	public WebElement loadPackageBtn() throws Exception {
		return driver.findElement(By.id("btnLoadPackage"));	
	}
	
	public WebElement facilitytable() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='eomForm']//section[@id='eomsection']"));	
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	public void navigateToClosingPackageLink() throws Exception{
		closingPackageLink();
		logger.info("        Clicked Closing Package link.");
	}
	
	public void clickAccountingPeriodDropdown() throws Exception{
		Select objSel = new Select(accountingPeriodDropdown());
		objSel.selectByIndex(1);
		logger.info("        Clicked Accounting Period dropdown list.");
	}
	
	public void clickFacilityDropdown() throws Exception{
		Select objSel = new Select(facilityDropdown());
		objSel.selectByIndex(1);
		logger.info("        Clicked Facility dropdown list.");
	}
	
	public void clickLoadPackageBtn() throws Exception{
		loadPackageBtn().click();
		logger.info("        Clicked Load Package button.");
	}

}
