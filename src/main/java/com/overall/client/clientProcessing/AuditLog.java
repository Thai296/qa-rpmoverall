package com.overall.client.clientProcessing;

import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.mars.dao.IGenericDaoXifinRpm;
import com.xifin.qa.config.Configuration;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AuditLog
{
	private static final Logger LOG = Logger.getLogger(AuditLog.class);

	private final RemoteWebDriver driver;
	private final Configuration config;
	private IGenericDaoXifinRpm daoManagerXifinRpm;
	WebDriverWait wait;
	
	public AuditLog(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 10);
		this.config = config;
		this.daoManagerXifinRpm = new DaoManagerXifinRpm(config.getRpmDatabase());
	}

	public WebElement clnAuditLogTitle() {
		return driver.findElementByClassName("platormPageTitle");
	}
	
	public WebElement clnIdInput() throws Exception {
		return driver.findElement(By.id("lookupClientAbbrev"));
	}
	
	public WebElement nameInput() throws Exception {
		return driver.findElement(By.id("clnNm"));	
	}

	public WebElement loadClientTblTitle() {
		return driver.findElementByXPath("/html/body/section/div/div/div[1]/div[2]/section/header/div/span/span");
	}

	public WebElement auditDetailTblTitle() throws Exception {
		return driver.findElementByXPath("//*[@id='mainSections']/section/header/div[1]/span/span");
	}

	public WebElement acctTypInput() throws Exception {
		return driver.findElement(By.id("acctTyp"));	
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("Reset"));
	}
	
	public WebElement toolTipText(String toolTipStr) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[title=\"" + toolTipStr + "\"]')[0]"); 
	}

	public void clickResetBtn() throws Exception {
		wait.until(ExpectedConditions.elementToBeClickable(resetBtn()));
		resetBtn().click();
	}

	//-------------------------------------------------------------------------------------
	public void setClnID(String str) throws Exception{		
		clnIdInput().sendKeys(str);
		clnIdInput().sendKeys(Keys.TAB);
		Thread.sleep(5000);
		LOG.info("        Entered Client ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void updateClnLtrType(String currClnLtrTypId, String clnAbbrev, String testDb) throws Exception{	
		String newClnLtrTypId = "";
		
		if (currClnLtrTypId.equals("3"))
		{
			Assert.assertEquals(daoManagerXifinRpm.setClnLtrTypFromCLNByClnAbbrev("2", clnAbbrev, testDb), 1);
			newClnLtrTypId = "2";
			
		}else{
			Assert.assertEquals(daoManagerXifinRpm.setClnLtrTypFromCLNByClnAbbrev("3", clnAbbrev, testDb), 1);
			newClnLtrTypId = "3";
		}
		
		LOG.info("        Updated CLN.FK_CLN_LTR_TYP_ID to a new value: " + newClnLtrTypId + " for Client " + clnAbbrev);
	}
	
}
