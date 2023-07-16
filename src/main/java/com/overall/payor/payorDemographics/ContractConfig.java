package com.overall.payor.payorDemographics;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class ContractConfig {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public ContractConfig(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	//contract config
	public WebElement auditBtn() throws Exception {
		return driver.findElement(By.id("auditbtn"));
	}
	
	public WebElement contractSearchBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $(':button[data-help-id=searches/contract_search]')[0]"); 
	}
	
	public WebElement contractIdInput(String contractId) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#contrctId')).val(\"" + contractId + "\")).trigger('onblur')[0]"); 
	}
	
	public WebElement contractIdInput() throws Exception {
		return driver.findElement(By.id("contrctId"));
	}
	
	public WebElement contractNameInput() throws Exception {
		return driver.findElement(By.id("contrctNm"));
	}
	
	public WebElement contractCoverageDropdown() throws Exception {
		return driver.findElement(By.id("contrctCovrgId"));
	}
	
	public WebElement effDtInput() throws Exception {
		return driver.findElement(By.id("startDt"));
	}
	
	public WebElement endDtInput() throws Exception {
		return driver.findElement(By.id("endDt"));
	}
	
	public WebElement reviewDtInput() throws Exception {
		return driver.findElement(By.id("reviewDt"));
	}
	
	public WebElement priceExpContractCheckbox() throws Exception {
		return driver.findElement(By.id("bExpired"));
	}
	
	public WebElement reportFormDropdown() throws Exception {
		return driver.findElement(By.id("rptFrmId"));
	}
	
	public WebElement adminNameInput() throws Exception {
		return driver.findElement(By.id("adminNm"));
	}
	
	public WebElement adminCountryInput() throws Exception {
		return driver.findElement(By.id("adminCntry"));
	}
	
	public WebElement adminAddr1Input() throws Exception {
		return driver.findElement(By.id("adminAddr1"));
	}
	
	public WebElement adminAddr2Input() throws Exception {
		return driver.findElement(By.id("adminAddr2"));
	}
	
	public WebElement adminZipInput() throws Exception {
		return driver.findElement(By.id("adminZip"));
	}
	
	public WebElement adminCityInput() throws Exception {
		return driver.findElement(By.id("adminCity"));
	}
	
	public WebElement adminStDropdown() throws Exception {
		return driver.findElement(By.id("adminStId"));
	}
	
	public WebElement adminContactInput() throws Exception {
		return driver.findElement(By.id("adminContct"));
	}
	
	public WebElement adminPhoneInput() throws Exception {
		return driver.findElement(By.id("adminPhn"));
	}
	
	public WebElement adminFaxInput() throws Exception {
		return driver.findElement(By.id("adminFax"));
	}
	
	public WebElement adminEmailInput() throws Exception {
		return driver.findElement(By.id("adminEmail"));
	}
	
	public WebElement adminContactMethodDropdown() throws Exception {
		return driver.findElement(By.id("adminCntctMthdId"));
	}
	
	//Payor Assignment
	public WebElement payorAssigEffDtInput() throws Exception {
		return driver.findElement(By.id("pyrEffDt"));
	}
	
	public WebElement payorAssigPayorIdInput() throws Exception {
		return driver.findElement(By.id("pyrAbbrev"));
	}
	
	public WebElement payorAssigPayorNameInput() throws Exception {
		return driver.findElement(By.id("pyrNm"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}
	
	
	//Contact Details
	public WebElement userIdInput() throws Exception {
		return driver.findElement(By.id("userId"));
	}
	
	public WebElement contactDtInput() throws Exception {
		return driver.findElement(By.id("cntctDt"));
	}
	
	public WebElement contactInfoInput() throws Exception {
		return driver.findElement(By.id("cntctInfo"));
	}
	
	public WebElement followUpDtInput() throws Exception {
		return driver.findElement(By.id("flwUpDt"));
	}
	
	public WebElement followUpPersonDropdown() throws Exception {
		return driver.findElement(By.id("pcdUsrSel"));
	}
	
	
	public void searchContract() throws Exception{
		contractSearchBtn().click();
		logger.info("Clicked Contract Search button");
	}
	
	public void resetPayor() throws Exception{
		resetBtn().click();
		logger.info("Reset Payor");
	}
	
	public void runAudit() throws Exception{
		auditBtn().click();
		logger.info("Run Audit");
	}
	
	public void setContractId(String contractId) throws Exception{
		contractIdInput(contractId);
		logger.info("Entered PayorId: " + contractId);
	}
	
	public boolean reqCheck(String val) throws Exception{
		boolean flag = false;
		if (!val.equals("")) {
			if(val.equals("1")){
				logger.info("Checkbox required checked");
				return true;
			} 
		} else {
			logger.error("Must enter valid value, " + val + " is not valid");
		}
		return flag;
	}
}
