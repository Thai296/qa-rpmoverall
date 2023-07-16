package com.overall.payment.paymentNavigation;

import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class PaymentNavigation
{
	private static final Logger LOG = Logger.getLogger(PaymentNavigation.class);

	private final RemoteWebDriver driver;
	private final Configuration config;
	
	public PaymentNavigation(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.config = config;
	}	

	public WebElement contentFrame() {
		return driver.findElement(By.id("content"));		
	}
	
	public WebElement paymentSearchLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Payment Search)'))[0].click();"); 
	}

	public void navigateToDepositsContentLink() throws MalformedURLException
	{
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/paymnt/pymtDepositsContent.jsp"));
	}
	
	public void navigateToPaymentSearchLink() throws MalformedURLException
	{
		paymentSearchLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/paymnt/pymtSrch.jsp"));
	}

	private void navigateToUrl(URL url)
	{
		LOG.info("Navigating to URL using JS, orgAlias="+config.getProperty(PropertyMap.ORGALIAS)+", url="+url);
		driver.executeScript("$(window.open('"+url+"'))");
	}
}
