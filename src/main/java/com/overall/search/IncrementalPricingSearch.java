package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class IncrementalPricingSearch {
	private final RemoteWebDriver driver;
	protected final Logger logger;

	public IncrementalPricingSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*Incremental Pricing Search*/
	public WebElement inPriSearchTitle() {
		return driver.findElement(By.xpath(".//*[@id='incrPricingSearch']/header/span"));
	}
	
	public WebElement inPriSearchHelpIconBtn() {
		return driver.findElement(By.xpath(".//*[@id='pageHelpLink']"));
	}
	
	public WebElement inPriSearchPriID(){
		return driver.findElement(By.xpath(".//*[@id='incrPricingId']"));
	}
	
	public WebElement inPriSearchPriName() {
		return driver.findElement(By.xpath(".//*[@id='incrPricingAbbrev']"));
	}
	
	public WebElement inPriSearchCloseBtn() {
		return driver.findElement(By.xpath(".//*[@id='incrPricingSearch']/div[2]/button[3]"));
	}
	
	public WebElement inPriSearchResetBtn() {
		return driver.findElement(By.xpath(".//*[@id='incrPricingSearch']/div[2]/button[2]"));
	}
	
	public WebElement inPriSearchSearchBtn() {
		return driver.findElement(By.xpath(".//*[@id='incrPricingSearch']/div[2]/button[1]"));
	}
	
	public WebElement inPriSearchValidErrMessContent() {
		return driver.findElement(By.xpath(".//*[@id='messagefor_message0']/div[2]"));
	}
	
	public WebElement inPriSearchValidErrMessCloseIconBtn() {
		return driver.findElement(By.xpath(".//*[@id='messagefor_message0']/div[1]/a"));
	}
	
	public void clickInPriSearchHelpIconBtn() throws Exception{
		inPriSearchHelpIconBtn().click();
		Thread.sleep(2000);
		logger.info("        Click on Incremental Pricing Search Help Icon button");
	}
	
	public void inputInPriSearchPriID(String value) throws Exception{
		inPriSearchPriID().sendKeys(value);
		logger.info("        Input "+value+" to Incremental Pricing Search Pricing ID field");
	}
	
	public void inputInPriSearchPriName(String value) throws Exception{
		inPriSearchPriName().sendKeys(value);
		logger.info("        Input "+value+" to Incremental Pricing Search Pricing Name field");
	}
	
	public void clickInPriSearchCloseBtn() throws Exception{
		inPriSearchCloseBtn().click();
		Thread.sleep(2000);
		logger.info("        Click on Incremental Pricing Search Close button");
	}
	
	public void clickInPriSearchResetBtn() throws Exception{
		inPriSearchResetBtn().click();
		Thread.sleep(2000);
		logger.info("        Click on Incremental Pricing Search Reset button");
	}
	
	public void clickInPriSearchSearchBtn() throws Exception{
		inPriSearchSearchBtn().click();
		Thread.sleep(2000);
		logger.info("        Click on Incremental Pricing Search Search button");
	}
}
