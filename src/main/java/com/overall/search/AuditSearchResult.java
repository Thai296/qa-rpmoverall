package com.overall.search;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AuditSearchResult {
	private WebDriverWait wait;
	private RemoteWebDriver driver;	
	protected Logger logger;

	public AuditSearchResult(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement autditDetailTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_auditlogwait")));
	}
	
	public WebElement autditDetailTblDateColTxt(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr["+row+"]/td[@aria-describedby='tbl_auditlogwait_timeStamp']")));	
	}
	
	public WebElement autditDetailTblActionColTxt(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr["+row+"]/td[@aria-describedby='tbl_auditlogwait_action']")));	
	}
	
	public WebElement autditDetailTblTableColTxt(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr["+row+"]/td[@aria-describedby='tbl_auditlogwait_tableName']")));	
	}
	
	public WebElement autditDetailTblFieldColTxt(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr["+row+"]/td[@aria-describedby='tbl_auditlogwait_columnName']")));	
	}
	
	public WebElement autditDetailTblOldValueColTxt(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr["+row+"]/td[@aria-describedby='tbl_auditlogwait_oldValue']")));	
	}
	
	public WebElement autditDetailTblNewValueColTxt(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr["+row+"]/td[@aria-describedby='tbl_auditlogwait_newValue']")));	
	}
	
	public WebElement autditDetailTblUserNameColTxt(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr["+row+"]/td[@aria-describedby='tbl_auditlogwait_userName']")));	
	}
	
	public List<WebElement> autditDetailTblRow() { 
		return driver.findElements(By.xpath("//*[@id='tbl_auditlogwait']/tbody/tr"));	
	}
	
	public WebElement totalResultsLbl() { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pager_right")));	
	}
	
	public List<WebElement> autditDetailTblTableNameColTxt() { 
		return driver.findElements(By.xpath(".//*[@id='tbl_auditlogwait']/tbody//td[@aria-describedby='tbl_auditlogwait_tableName']"));	
	}
	
	public WebElement autditDetailTblNextPageIco() { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_pager")));	
	}

	
}



