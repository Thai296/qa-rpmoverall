package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdjustmentCodeSearchResult {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;
	
	public AdjustmentCodeSearchResult(RemoteWebDriver driver){
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
		/*Adjustment Code Search Result*/
	public WebElement ajdCodeSearchResultTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='adjustmentCodeSearchTable']")));
	}
	
	public WebElement ajdCodeSearchResultTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_adjustmentCodeSearchTable']/div[1]/span")));
	}

	public WebElement ajdCodeSearchResultCloseButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='adjustmentCodeSearch']/div[3]/button[2]")));
	}
	
	public WebElement ajdCodeSearchResultNewSearchButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='adjustmentCodeSearch']/div[3]/button[1]")));
	}
	
	public WebElement ajdCodeSearchResultHelpLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pageHelpLink']")));
	}
	
	public WebElement ajdCodeSearchResultCell(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='adjustmentCodeSearchTable']/tbody/tr["+ row +"]/td["+ col +"]")));
	}
	
	public WebElement ajdCodeSearchResultCellLink(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='adjustmentCodeSearchTable']/tbody/tr["+ row +"]/td["+ col +"]/a")));
	}
	
	public WebElement ajdCodeSearchResultTblTotalRecord() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	
	//methods
	public void clickAjdCodeSearchResultCloseButton() throws Exception{
		ajdCodeSearchResultCloseButton().click();
		logger.info("        Click on Ajd Code Search Result Close Button.");
	}
	
	public void clickAjdCodeSearchResultNewSearchButton() throws Exception{
		ajdCodeSearchResultNewSearchButton().click();
		Thread.sleep(2000);
		logger.info("        Click on Ajd Code Search Result New Search button");
	}
	
	public void clickAjdCodeSearchResultHelpLink() throws Exception{
		ajdCodeSearchResultHelpLink().click();
		logger.info("        Click on Ajd Code Search Result Help Link");
	}
	
	public WebElement nexPager() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}
}
