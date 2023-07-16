package com.overall.client.clientInquiry.clientBatchSingleDemandStatement;

//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
//import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;

//import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

//import static org.testng.Assert.*;

public class ClientBatchSingleDemandStatement {

	private RemoteWebDriver driver;
	protected Logger logger;
	private WebDriverWait wait;
	private SeleniumBaseTest b;

	public ClientBatchSingleDemandStatement(RemoteWebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" +driver );
		this.wait=wait;
	}

	/* GENERAL */
	public WebElement pageTitle() throws Exception{
		return driver.findElement(By.xpath("//*[@id=\"batchSingleDemandStmtForm\"]/div[1]/div/div[2]/span"));
	}//*[@id="batchSingleDemandStmtForm"]/div[1]/div/div[2]/span
	
	public WebElement addBtn() throws Exception{
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("add_tbl_batchClients")));
	}	
	
	public WebElement generateStmBtn() throws Exception{
		return driver.findElement(By.id("btnSaveAndClear"));
	}
	
	public WebElement saveInProgressInfoText() throws Exception {
		return driver.findElement(By.id("messagefor_message0")); 
	}
	
	public WebElement errMsgTxt() throws Exception{
		return driver.findElement(By.xpath("//*[@id='sectionServerMessages']/div/div/ul/li/span"));
	}
	
	public WebElement helpBtn() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('a[data-help-id=\"p_batch_single_demand_statement\"]')[0]");
	}
	
	/* Batch Clients Table */
	public WebElement batchClientsTable() throws Exception{
		return driver.findElement(By.id("tbl_batchClients"));
	}
	
	public WebElement deleteCheckbox(int col) throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_batchClients']/tbody/tr["+col+"]/td[6]/input"));
	}	
	
	// can use for Include 'B' Clients and Delete checkbox in table
	public WebElement batchClientTblCheckbox(int row, int col) throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_batchClients']/tbody/tr["+row+"]/td["+col+"]/input"));
	}
	
	public WebElement batchClnTblCell(int row, int col) throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_batchClients']/tbody/tr["+row+"]/td["+col+"]"));
	}
	
	public WebElement editBtn() throws Exception{
		return driver.findElement(By.id("edit_tbl_batchClients"));
	}
	
	/* Add Record Pop up*/
	public WebElement clientIdInput() throws Exception{
		return driver.findElement(By.id("clnID"));
	}
	
	public WebElement clientNameText() throws Exception{
		return driver.findElement(By.id("clnName"));
	}
	
	public WebElement includeBClnChkBox() throws Exception{
		return driver.findElement(By.id("includeBClns"));
	}
	
	public WebElement okBtn() throws Exception{
		return driver.findElement(By.id("sData"));
	}	
	
	public WebElement popupErrMsgTxt() {
		return driver.findElement(By.xpath(".//*[@id='FormError']/td"));
	}
	
	public WebElement clnSearchBtn() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('a[title=\"Client ID Search\"]')[0]");
	}
	
	/* Help page */	
	public WebElement helpPageTitle() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('body > h1')[0]");
	}
	
	/* Actions*/		
	public void clickHelpBtn() throws Exception{
		helpBtn().click();
		logger.info("       Clicked Help button.");
	}
	
	public void clickClnSearchBtn() throws Exception{
		clnSearchBtn().click();
		logger.info("       Clicked Client Search button.");
	}
	
	public void clickEditBtn() throws Exception{
		editBtn().click();
		logger.info("       Clicked on Edit button.");
	}
	
	public void clickBatchClientTblCell(int row, int col) throws Exception{
		batchClnTblCell(row, col).click();
		logger.info("       Clicked on a row in Batch Clients table.");
	}
	
	public void clickDeleteCheckboxOnTable(int row) throws Exception{
		deleteCheckbox(row).click();
		Thread.sleep(2000);
		logger.info("       Clicked on Delete checkbox.");
	}
	
	public void clickAddBtn() throws Exception{
		addBtn().click();
		logger.info("       Clicked Add button.");
	}
	
	public void setClientId(String value) throws Exception{
		clientIdInput().clear();
		clientIdInput().sendKeys(value);
		clientIdInput().sendKeys(Keys.TAB);
		logger.info("       Entered Client ID " + value);
	}
	
	public void clickOkBtn() throws Exception{
		okBtn().click();
		logger.info("       Clicked OK button.");
	}
	
	public void clickGenerateStmBtn() throws Exception{
		generateStmBtn().click();
		Thread.sleep(5000);
		logger.info("       Clicked Generate Statement button.");
	}
	
	public void checkIncludeBCheckbox() throws Exception{
		includeBClnChkBox().click();
		logger.info("       Clicked on Include B Checkbox.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method checks if the Save is done 
	public boolean isSaveInProgressNotPresent(int time) throws Exception{
		boolean flag = false;
		int i = 0;
		
		//Override the timeout in SeleniumBaseTest
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);		
		
		while (i < time) {
			try{
				if(saveInProgressInfoText().isDisplayed() || saveInProgressInfoText().isEnabled() || !(saveInProgressInfoText().getAttribute("style").contains("none"))){					
					logger.info("        Save in progress is visible.");					
				}
			}catch (Exception e){		
				flag = true;
				logger.info("        Save in progress is not visible.");	
				break;
			}
			Thread.sleep(1000);
			i++;		
		}
		return flag;	
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method checks if the accn was done saving 
	public boolean isSaveDone() throws Exception{
		boolean flag = false;	
		b = new SeleniumBaseTest();
		
		if(isSaveInProgressNotPresent(40)){		
				flag = true;
				Thread.sleep(5000);
				logger.info("        The changes were saved.");
			}
		else{
			flag = false;
			Thread.sleep(5000);
			logger.info("        The changes were taking more than 40 seconds.");
		}
		return flag;		
	}
	
}

