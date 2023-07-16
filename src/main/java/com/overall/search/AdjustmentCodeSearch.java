package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdjustmentCodeSearch {
	protected Logger logger;
	private WebDriverWait wait;
	
	public AdjustmentCodeSearch(RemoteWebDriver driver){
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		wait = new WebDriverWait(driver, 10);
	}
	
		/*Adjustment Code Search*/
	public WebElement adjCodeSearchTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='adjustmentCodeSearch']/header/span")));
	}
	
	public WebElement adjCodeSearchAjdCategoryDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='adjType']")));
	}
	
	public WebElement adjCodeSearchName() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='descr']")));
	}
	
	public WebElement adjCodeSearchIncludedDeleletedCheckbox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='includeDeletedRecords1']")));
	}
	
	public WebElement adjCodeSearchSortByDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sortBy']")));
	}
	
	public WebElement adjCodeSearchHelpLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pageHelpLink']")));
	}
	
	public WebElement adjCodeSearchCloseButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='adjustmentCodeSearch']/div[2]/button[3]")));
	}
	
	public WebElement adjCodeSearchResetButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='adjustmentCodeSearch']/div[2]/button[2]")));
	}
	
	public WebElement adjCodeSearchSearchButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='adjustmentCodeSearch']/div[2]/button[1]")));
	}


	//methods
	public void clickAdjCodeSearchCloseButton() throws Exception{
		adjCodeSearchCloseButton().click();
		logger.info("        Click on Adj Code search Close button.");
	}
	
	public void clickAdjCodeSearchResetButton() throws Exception{
		adjCodeSearchResetButton().click();
		logger.info("        Click on Adj Code search Reset button.");
	}
	
	public void clickAdjCodeSearchSearchButton() throws Exception{
		adjCodeSearchSearchButton().click();
		Thread.sleep(2000);
		logger.info("        Click on Adj Code search Search button.");
	}
	
	public void clickAdjCodeSearchHelpLink() throws Exception{
		adjCodeSearchHelpLink().click();
		logger.info("        Click on Adj Code search Help Link.");
	}
	
	public void enterToAdjCodeSearchNameInput(String value) throws Exception{
		adjCodeSearchName().sendKeys(value);
		adjCodeSearchName().sendKeys(Keys.TAB);
		logger.info("        Entered Name: " + value);
	}										
		
	public void selectAdjCodeSearchAjdCategoryDropdown(String value) throws Exception{
		Select dropdown = new Select(adjCodeSearchAjdCategoryDropDown());
		dropdown.selectByValue(value);
		logger.info("        Selected "+value+" in Adj Code Search Ajd Category Dropdown list.");	
	}
	
	public void selectAdjCodeSearchSortByDropdown(String value) throws Exception{
		Select dropdown = new Select(adjCodeSearchSortByDropDown());
		dropdown.selectByValue(value);
		logger.info("        Selected "+value+" in Adj Code Search Sort By Dropdown list.");	
	}
	
	public void clickAdjCodeSearchIncludedDeleletedCheckbox() throws Exception{
		adjCodeSearchIncludedDeleletedCheckbox().click();
		logger.info("        Click on Adj Code Search Included Deleleted Checkbox.");
	}
}
