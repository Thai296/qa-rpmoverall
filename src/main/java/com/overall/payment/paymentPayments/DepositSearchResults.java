package com.overall.payment.paymentPayments;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class DepositSearchResults {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public DepositSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement depositLedgerTable() throws Exception {
		return driver.findElement(By.id("ledgerTable"));
	}
	
	public WebElement newResearchBtn() throws Exception {
		return driver.findElement(By.id("btnReselect"));
	}

}
