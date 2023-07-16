package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PhysicianSearchResults {
	private WebDriverWait wait;
	protected Logger logger;
	public PhysicianSearchResults(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public WebElement physSearchResultTitle(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_physiciansearchTable']//span[contains(@class,'title')]")));
	}
	
	public WebElement helpIcon(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement physSrchResultsTable(int row, int col){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='physiciansearchTable']/tbody/tr["+row+"]/td["+col+"]/a")));
	}
	
	public WebElement refreshIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='refresh_physiciansearchTable']//span")));
	}
	
	public WebElement prevIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_pager")));
	}
	
	public WebElement nextIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_pager")));
	}
	
	public WebElement pyrTblTotalRecord() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	
	public WebElement keepSearchOpenCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("keepSearchOpen")));
	}
	
	public WebElement closeBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Close")));
	}
	
	public WebElement newSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Submit")));
	}
	
	public void selectPhysSrchResults(int row, int col)  {
		physSrchResultsTable(row, col).click();
		logger.info("        selected a Physician.");
	}	

	public WebElement physicianSearchResultsTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("physiciansearchTable")));
	}
	
	public WebElement physSrchResultsTblNpiColLnk(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='physiciansearchTable']//tr["+row+"]/td[@aria-describedby='physiciansearchTable_npi']/a")));
	}
	
	public WebElement physSrchResultsTblUpinColLnk(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='physiciansearchTable']//tr["+row+"]/td[@aria-describedby='physiciansearchTable_upin']/a")));
	}
}
