package com.overall.fileMaintenance.fileMaintenanceTables;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;


public class PhysicianLicense {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public PhysicianLicense(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	//Physician License
	public WebElement physNpiIdInput() throws Exception {
		return driver.findElement(By.id("npiId"));
	}
	
	public WebElement physNpiIdInput(String npiId) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#npiId')).val(\"" + npiId + "\")).trigger('onblur')[0]"); 
	}
	
	public WebElement physUpinIdInput() throws Exception {
		return driver.findElement(By.id("upinId"));
	}
	
	public WebElement physLastNameInput() throws Exception {
		return driver.findElement(By.id("lNm"));
	}
	
	public WebElement physFirstNameInput() throws Exception {
		return driver.findElement(By.id("fNm"));
	}
	
	public WebElement physAddr1Input() throws Exception {
		return driver.findElement(By.id("addr1"));
	}
	
	public WebElement physZipInput() throws Exception {
		return driver.findElement(By.id("zip"));
	}
	
	public WebElement physCredInput() throws Exception {
		return driver.findElement(By.id("cred"));
	}
	
	//Taxonomy
	public WebElement taxonomyCodeInput() throws Exception {
		return driver.findElement(By.id("taxonomyCode"));
	}
	
	public WebElement taxonomyProviderTypeText() throws Exception {
		return driver.findElement(By.id("taxonomyProviderType"));
	}
	
	public WebElement taxonomyClassificationText() throws Exception {
		return driver.findElement(By.id("taxonomyClassification"));
	}
	
	public WebElement taxonomySpecializationText() throws Exception {
		return driver.findElement(By.id("taxonomySpecialization"));
	}
	
	public WebElement goToTaxonomyBtn() throws Exception {
		return driver.findElement(By.id("btnGoToTaxonomy"));
	}
	
	
	// State Licenses
	public WebElement addStateLicenseBtn() throws Exception {
		return driver.findElement(By.id("btnAddStLic"));
	}
	
	public WebElement physLicenseTable() throws Exception {
		return driver.findElement(By.xpath("//*[@id='fmPhyLicConf']/table[5]/tbody/tr/td[1]/table[1]"));
	}	
	
	//Affiliated Licenses
	public WebElement addAffiliatedLicenseBtn() throws Exception {
		return driver.findElement(By.id("btnAddAffLic"));
	}
	
	public WebElement rowCountAffiliatedLicenseText() throws Exception {
		return driver.findElement(By.id("qRwCnt"));
	}
	
	public WebElement indexAffiliatedLicenseText(int row) throws Exception {	
		return driver.findElement(By.cssSelector("#qTable > tbody > tr:nth-child(" + row + ") > td.tbbdy1a"));	
	}
	
	//Client Specific Affiliated Licenses
	public WebElement addPyrClnAffiliatedLicenseBtn() throws Exception {
		return driver.findElement(By.id("btnAddPyrClnLic"));
	}
	
	public WebElement indexPyrClnAffiliatedLicenseText(int row) throws Exception {	
		return driver.findElement(By.cssSelector("#pclTable > tbody > tr:nth-child(" + row + ") > td.tbbdy1a"));	
	}
	
	public WebElement rowCountPyrClnAffiliatedLicenseText() throws Exception {
		return driver.findElement(By.id("pclRwCnt"));
	}
	
	//OIG Exclusion
	public WebElement addOIGExclusionBtn() throws Exception {
		return driver.findElement(By.id("btnAddOigExclsn"));
	}
	
	public WebElement indexOIGExclusionText(int row) throws Exception {	
		return driver.findElement(By.cssSelector("#oigTable > tbody > tr:nth-child(" + row + ") > td.tbbdy1a"));	
	}
	
	public WebElement rowCountOIGExclusionText() throws Exception {
		return driver.findElement(By.id("oigRwCnt"));
	}
	
	//Payor Group Exclusion
	public WebElement addGrpExclusionBtn() throws Exception {
		return driver.findElement(By.id("btnAddExclsn2"));
	}
	
	public WebElement indexGrpExclusionText(int row) throws Exception {	
		return driver.findElement(By.cssSelector("#oTable > tbody > tr:nth-child(" + row + ") > td.tbbdy1a"));	
	}
	
	public WebElement rowCountGrpExclusionText() throws Exception {
		return driver.findElement(By.id("oRwCnt"));
	}
	
	//Payor Exclusion
	public WebElement addPyrExclusionBtn() throws Exception {
		return driver.findElement(By.id("btnAddExclsn"));
	}
	
	public WebElement indexPyrExclusionText(int row) throws Exception {	
		return driver.findElement(By.cssSelector("#pTable > tbody > tr:nth-child(" + row + ") > td.tbbdy1a"));	
	}
	
	public WebElement rowCountPyrExclusionText() throws Exception {
		return driver.findElement(By.id("pRwCnt"));
	}
	
	//Physician Cross References
	public WebElement addPhysicianXrefBtn() throws Exception {
		return driver.findElement(By.id("btnAddRwX"));
	}
	
	public WebElement indexPhysicianXrefText(int row) throws Exception {	
		return driver.findElement(By.cssSelector("#xTable > tbody > tr:nth-child(" + row + ") > td.tbbdy1a"));	
	}
	
	public WebElement rowCountPhysicianXrefText() throws Exception {
		return driver.findElement(By.id("xRwCnt"));
	}	
	
	public void setPhysNpiIdInput(String npiId) throws Exception {
		physNpiIdInput(npiId);
		Thread.sleep(5000);
		logger.info("        Loaded npi Id: " + npiId);
	}
	
	public void addStateLicense() throws Exception {
		addStateLicenseBtn();
		logger.info("        State License Record Added");
	}
	
	

}
