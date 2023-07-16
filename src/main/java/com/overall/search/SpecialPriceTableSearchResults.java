package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SpecialPriceTableSearchResults {
	private RemoteWebDriver driver;	
	private WebDriverWait wait;
	protected Logger logger;
	
	public SpecialPriceTableSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		this.wait= new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement helpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement searchResultTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_feeschedulesearchTable']//span[text()='Special Price Table Search Result']")));
	}
	public WebElement keepSearchOpenChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("keepSearchOpen")));
	}
	public WebElement newSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='feeScheduleSearch']/div[3]/button[1]")));
	}
	public WebElement closeSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='feeScheduleSearch']/div[3]/button[2]")));
	}
	
	public WebElement feeScheduleTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("feeschedulesearchTable")));
	}
		
	public WebElement feeScheduleTblFeeScheduleIdColLnk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='feeschedulesearchTable']//tr["+ row +"]/td[@aria-describedby='feeschedulesearchTable_abbrv']/a")));
	}
	
	public WebElement feeScheduleTblNameColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='feeschedulesearchTable']//tr["+ row +"]/td[@aria-describedby='feeschedulesearchTable_descr']")));
	}
	
	public WebElement feeScheduleTblTypeColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='feeschedulesearchTable']//tr["+ row +"]/td[@aria-describedby='feeschedulesearchTable_strFeeSchedule']")));
	}
	
	public WebElement feeScheduleTblRetailColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='feeschedulesearchTable']//tr["+ row +"]/td[@aria-describedby='feeschedulesearchTable_strRetail']")));
	}
	
	public WebElement feeScheduleTblTestProcBasedColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='feeschedulesearchTable']//tr["+ row +"]/td[@aria-describedby='feeschedulesearchTable_strTestBased']")));
	}
	
	public WebElement feeScheduleTblClientPayorBasedColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='feeschedulesearchTable']//tr["+ row +"]/td[@aria-describedby='feeschedulesearchTable_strClientBased']")));
	}
				
	public WebElement feeScheduleTblFirstIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_pager']/span")));
	}
	public WebElement feeScheduleTblPrevIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_pager']/span")));
	}
	
	public WebElement feeScheduleTblPageNumberInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_center']//input")));
	}
	
	public WebElement feeScheduleTblNextIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}
	public WebElement feeScheduleTblLastIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_pager']/span")));
	}
	
	public WebElement feeScheduleTblTotalRecordTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}		
}
