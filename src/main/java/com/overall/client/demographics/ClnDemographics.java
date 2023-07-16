package com.overall.client.demographics;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.log4j.Logger;

public class ClnDemographics {
	private WebDriverWait wait;
	protected Logger logger;
	
	public ClnDemographics(RemoteWebDriver driver){
		this.wait= new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement clnDemographicsTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody//td[contains(text(),'Client - Demographics')]")));
	}
	
	public WebElement clnIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientId")));
	}
	
	public WebElement nameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientName")));
	}
	
	public WebElement accountTypeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_accountType")));
	}
	
	public WebElement startDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("startDate")));
	}
	
	public WebElement primaryFacilityDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_primaryFac")));
	}
	
	public WebElement accountTypeDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("accntTypId")));
	}
	
	//Account Demographics
	public WebElement addXrefBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_crossReferences_iladd")));
	}
	
	public WebElement effDtInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("1_effectiveDate")));
	}
	
	public WebElement expDtInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("1_expirationDate")));
	}
	
	public WebElement xrefTypeDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_1_xrefTypeId")));
	}
	
	public WebElement memberAbbrevTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_1_xrefAbbrev")));
	}
	
	public WebElement xrefSearchDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_1_xrefAbbrev")));
	}
	
	//Billing Address and Contact Information
	public WebElement addr1Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrAddr1")));
	}
	
	public WebElement addr2Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrAddr2")));
	}
	
	public WebElement zipInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrPostCode"))); 
	}
	
	public WebElement phone1Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrPhone1")));
	}
	
	public WebElement fax1Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrFax1")));
	}
	
	public WebElement copyBillAddressToStreetAddressChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrCopy2Street")));
	}
	
	public WebElement copyBillAddrToCorresAddressChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrCopy2Correspondence")));
	}
	
	public WebElement copyBillAddrToStreetAddressChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrCopy2Street")));
	}
	
	public WebElement copyBillAddrToShippingAddressChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrCopy2Shipping")));
	}
	
	public WebElement submitBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
}
