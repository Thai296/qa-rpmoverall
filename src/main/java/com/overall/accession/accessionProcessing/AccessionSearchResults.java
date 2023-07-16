package com.overall.accession.accessionProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccessionSearchResults {

	private final RemoteWebDriver driver;
	protected final Logger logger;

	public AccessionSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public WebElement accnTable() {
		return driver.findElement(By.id("accnTable"));
	}

	public WebElement accnSearchResultTable() {
		return driver.findElement(By.id("accessionsearchTable"));
	}

	public WebElement accnIdLink(int row, int col){
		return driver.findElement(By.xpath(".//*[@id='accnTable']/tbody/tr["+row+"]/td["+col+"]/input"));
	}
	public WebElement accnInSearchResults() {
		return driver.findElement(By.xpath(".//*[@id='oTable']/tbody/tr[2]/td[5]"));
	}
	public WebElement accessionSearchResultsTitle(WebDriverWait wait){
		return wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".ui-jqgrid-title")));
	}


}
