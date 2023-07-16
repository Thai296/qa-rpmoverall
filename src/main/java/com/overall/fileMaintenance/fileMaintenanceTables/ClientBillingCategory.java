package com.overall.fileMaintenance.fileMaintenanceTables;

import java.util.List;

import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;


public class ClientBillingCategory {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private SeleniumBaseTest b;
	
	public ClientBillingCategory(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}	
	
	public WebElement addCategoryBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $(':button[value=\"Add Category\"]')[0]"); 
	}
	
	public WebElement newIDText() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"rvuRow newRow\"] [name=\"tAbbrv\"]')[0]"); 
	}
	
	public WebElement newDescrText() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"rvuRow newRow\"] [name=\"tDescr\"]')[0]"); 
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	public WebElement cbcIDText(String abbrev) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $(':input[name=\"tAbbrv\"][value=\"" + abbrev + "\"]')[0]"); 
	}
	
	public WebElement cbcCategoryTable() throws Exception {
		return driver.findElement(By.id("categoryTable"));
	}
	
	public String cbcIDList(int row) throws Exception {		
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[name=\"tAbbrv\"]')[" + row + "]).val()");
	}
	
	public String serviceCdList(int row) throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $(':input[name=\"tSvcCd\"]')[" + row + "].getAttribute(\"value\")"); 
	}
	
	public String descrOnClnStmtList(int row) throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $(':input[name=\"tDescClnStmt\"]')[" + row + "].getAttribute(\"value\")"); 
	}
	
	//Add/Edit Client Billing Category popup window
	public WebElement popUpPageTitleText() throws Exception {
		return driver.findElement(By.id("ui-dialog-title-editor"));
	}	
	
	public WebElement idInput() throws Exception {
		return driver.findElement(By.id("pAbbrv"));
	}
	
	public WebElement descriptionInput() throws Exception {
		return driver.findElement(By.id("pDescr"));
	}
	
	public WebElement okBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('.ui-button-text')[1]"); 
	}
	
	public WebElement serviceCdOnClnStmtInput() throws Exception {
		return driver.findElement(By.id("pSvcCd"));
	}
	
	public WebElement descrOnClnStmtInput() throws Exception {
		return driver.findElement(By.id("pDescClnStmt"));
	}
	
	public WebElement errText() throws Exception {
		return driver.findElement(By.id("errorText"));
	}
	
	//-------------------------------------------------------------------------------------
	public void setID(String str) throws Exception{		
		idInput().sendKeys(str);
		idInput().sendKeys(Keys.TAB);
		logger.info("        Entered ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setDescription(String str) throws Exception{		
		descriptionInput().sendKeys(str);
		descriptionInput().sendKeys(Keys.TAB);
		logger.info("        Entered Description: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setServiceCdOnClnStmt(String str) throws Exception{		
		serviceCdOnClnStmtInput().sendKeys(str);
		serviceCdOnClnStmtInput().sendKeys(Keys.TAB);
		logger.info("        Entered Service Code on Client Statement: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setDescriptionOnClnStmt(String str) throws Exception{		
		descrOnClnStmtInput().sendKeys(str);
		descrOnClnStmtInput().sendKeys(Keys.TAB);
		logger.info("        Entered Description on Client Statement: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method returns the row number when found a match in a webtable for the passing ONE column value
	public int getRowNumberInWebTable(WebElement tableElement, String colVal) throws Exception {
		int rowNum = 0;
		b = new SeleniumBaseTest();
		int totalRowCount = b.getTableTotalRowSize(tableElement);
		//System.out.println("total Row Count---" + totalRowCount + "---");	
				
		for (int i=0; i<totalRowCount; i++){	
			//System.out.println("Row Num---" + i + "---" + cbcIDList(i).trim());					
			if(cbcIDList(i).trim().equals(colVal)) {				
				logger.info("        Match found in " + tableElement.getAttribute("id") +  " table at row " + i);
				rowNum = i;
				break;
			}			
		}
		return rowNum;
	}
	
}
