package com.overall.fileMaintenance.sysMgt;

import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

public class DataCacheConfiguration {
	
	private RemoteWebDriver driver;	
	protected Logger logger;

	public DataCacheConfiguration(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public By contentFrameLocator()
	{
		return By.id("content");
	}

	public WebElement contentFrame()
	{
		return driver.findElement(contentFrameLocator());
	}
	
	public WebElement reloadAllBtn() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($(':input[type=button][class=btn]')).click(checkAll())[0]"); 
	}
	
	public WebElement userFacAccessByUserId() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$.find('body > form > div > table > tbody > tr:nth-child(143) > td:nth-child(2) > input[type=checkbox]')[0]"); 
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement reloadAllBtnText() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $(':input[value=Reload All]')[0]"); 
	}
	
	public WebElement reloadNoneBtnText() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $(':input[value=Reload None]')[0]"); 
	}	
	
	public WebElement reloadNowText(int row) throws Exception {		
		return driver.findElement(By.cssSelector("body > form > div > table > tbody > tr:nth-child(" + row + ") > td:nth-child(2)> input[type=checkbox]"));
	}
	
	public WebElement dataCacheConfigTable() throws Exception {		
		return driver.findElement(By.cssSelector("body > form > div > table"));
	}	
	
	public WebElement dataCacheConfigText(int row) throws Exception {		
		return driver.findElement(By.cssSelector("body > form > div > table > tbody > tr:nth-child(" + row + ") > td:nth-child(1) > input"));
	}
	
	public WebElement reloadNoneBtn() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($(':input[value=Reload None]')).click(clearAll())[0]");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method returns the row number when found a match in a webtable for the passing ONE column value
	public int getRowNumberInDataCacheTable(WebElement tableElement, String colVal) throws Exception {
		int rowNum = 0;
		int totalRowCount = getTotalRowSize(tableElement);
		
		for (int i=1; i<totalRowCount + 1; i++){
			//System.out.println("Row Num---" + i + "---");					
			if(dataCacheConfigText(i).getAttribute("value").trim().equals(colVal)) {				
				logger.info("        Match found in Data Cache Configuration table at row " + i);
				rowNum = i;
				break;
			}			
		}
		return rowNum;
	}	
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	//Check if all the reload checkboxes are checked (true) or not checked (false)
	public boolean isReloadChecked(WebElement tableElement, boolean validateAllChecked) throws Exception {
		boolean flag = true;
		int totalRowCount = getTotalRowSize(tableElement);
		
		for (int i=1; i<totalRowCount + 1; i++){
			
		   if(validateAllChecked){				
			   //System.out.println("Row Num---" + i + "---" + reloadNowText(i).isSelected());	
			   //If clicked the Reload All button
			   if(!(reloadNowText(i).isSelected())) {				
				   logger.info("        The Reload checkbox is not checked at row " + i);
				   flag = false;
				   break;
			   }
		    }else {
		    	//If clicked the Reload None or Reset button
		    	if(reloadNowText(i).isSelected()) {				
		    		logger.info("        The Reload checkbox is checked at row " + i);
		    		flag = false;
		    		break;
		    	}	
		     }			
		}
		return flag;
	}	
	
	//----------------------------------------------------------------------------------------------------------------------------------------
	//Returns total row counts for a webtable
	public int getTotalRowSize(WebElement tableElement)  throws Exception {		
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));		
		int total_rows = rows.size();
		logger.info("        Total Row Size " + total_rows);
		return total_rows;
	}
	


	public void setClearCacheAll() throws Exception
	{
		reloadAllBtn();
		submitBtn().click();
	}
}
