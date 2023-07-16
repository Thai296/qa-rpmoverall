package com.overall.payment.paymentPayments;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PaymentSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;

	public PaymentSearch(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		this.wait=wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.id("search"));
	}
	public WebElement payorSearchhBtn() throws Exception {
		return driver.findElement(By.id("pyrSrchBtn"));
	}
	
	public WebElement checkNumInput() throws Exception {
		return driver.findElement(By.id("chkNum"));
	}
	public WebElement paymntSrchHeaderTitleTxt() {
		return wait.until(ExpectedConditions.elementToBeClickable(By.className("platormPageTitle")));
	}

	public WebElement PaymentTypeDropDown(){
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_paymentType")));
	}
	
	//-------------------------------------------------------------------------------------
	public void setCheckNum(String str) throws Exception{		
		checkNumInput().sendKeys(str);
		checkNumInput().sendKeys(Keys.TAB);
		logger.info("        Entered Check #: " + str);
	}

}
