package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TaxonomyCodeSearch {

	private WebDriverWait wait;
	protected Logger logger;
	
	public TaxonomyCodeSearch(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*** Taxonomy Code Search ***/
	public WebElement taxCdSearchPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='taxonomySearchForm']/header/span")));
	}
	
	public WebElement taxCdSearchTaxIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='taxonomyCd']")));
	}
	
	public WebElement providerTypeDescSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("providerTypeDesc")));
	}
	
	public WebElement classificationSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("classification")));
	}
	
	public WebElement specializationSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("specialization")));
	}
	
	public WebElement definitionSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("definition")));
	}
	
	public WebElement notesSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("notes")));
	}
	
	public WebElement excludedPayorGroupSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("excludedPayorGroup")));
	}
	
	public WebElement excludedPayorIDSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("excludedPayorAbbrev")));
	}
	
	public WebElement excludedPayorIdSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='taxonomyCodeSearch']//a[@title='Payor Search'][@data-help-id='payor_id_search']")));
	}
	
	public WebElement allExcludedRecordsCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("allExcludedRecords")));
	}
	
	public WebElement allPayorGroupExcludedRecordsCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("allPayorGroupExcludedRecords")));
	}
	
	public WebElement allPayorExcludedRecordsCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("allPayorExcludedRecords")));
	}
	
	public WebElement deactivatedRecordsCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deactivatedRecords")));
	}
	
	public WebElement taxCdSearchSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@type='button'][@value='Submit']")));
	}
	
	public WebElement taxonomyCodeSearchResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@type='button'][@value='Reset']")));
	}
	
	public WebElement taxonomyCodeSearchCloseBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@type='button'][@value='Close']")));
	}
	
	public WebElement taxonomyCodeSearchHelpLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
}
