package com.overall.fileMaintenance.reasonCode;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReasonCodeTable {
	private WebDriverWait wait;
	protected Logger logger;
		
	public ReasonCodeTable(RemoteWebDriver driver) {
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*** header***/
	public WebElement reasonCodeTablePageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='reasonCodeTblForm']//span[@class='platormPageTitle']")));
	}
	
	public WebElement reasonCodeTableHeaderHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='reasonCodeTblForm']//a[@data-help-id='p_reason_code_table_header']")));
	}
	
	public WebElement reasonCodeTableRunAuditBtn ()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditbtn")));
	}
	
	/*** reason code table ***/
	public WebElement reasonCodeTableSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_reason_code_table']")));
	}
	
	public WebElement reasonCodeTableReasonIdInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupReasonCdTblId")));
	}
	
	public WebElement reasonCodeTableDescriptionInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("denialTblDesc")));
	}
	
	public WebElement totalRecordInAuditDetail () {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
    }
	
	public WebElement colRowInAuditDetail(int row, int col) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr["+row+"]/td["+col+"]")));
	}
	
	public WebElement nextPagerIconInAuditDetail () {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
    }
	
	/*** footer ***/
	public WebElement reasonCodeTableResetBtn () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement reasonCodeTableSaveAndClearBtn () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	public WebElement reasonCodeTableFooterMenuHelpIco () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
}
