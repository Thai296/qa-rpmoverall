package com.overall.headerNavigation;

import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class HeaderNavigation
{
	private static final Logger LOG = Logger.getLogger(HeaderNavigation.class);
	
	private final RemoteWebDriver driver;	
	private final Configuration config;
	
	public HeaderNavigation(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.config = config;
	}	
	
	public WebElement marsHeaderFrame()
	{
		return driver.findElement(By.id("marsheader"));		
	}
	
	public WebElement marsUsernameText()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('#header_credentials > li.login')).get(0)");
	}
	
	public WebElement marsCustomerText()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('#header_credentials > li.cust')).get(0)");
	}
	
	public WebElement marsHelpLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#marsheader').contents().find('#header_credentials')).find('li.hlp > a'))[0].click();");
	}
	
	public WebElement accessionTab()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('#marsheader').contents().find('#accn > a'))[0].click();");
	}
	
	public WebElement fileMaintenanceTab() 
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('#marsheader').contents().find('#fm > a'))[0].click();");
	}
	
	public WebElement payorTab()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('#marsheader').contents().find('#pyr > a'))[0].click();");
	}
	
	public WebElement paymentTab()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('#marsheader').contents().find('#payment > a'))[0].click();");
	}
	
	public WebElement clientTab()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('#marsheader').contents().find('#cln > a'))[0].click();");
	}
	
	public WebElement ssoServiceWidget()
	{
		return driver.findElement(By.id("xf_sso_widget"));
	}
	
	public WebElement ssoServicesPopupText()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"xf_sso_captionText fixBox\"]')[0]");
	}

	public WebElement ssoServiceWidgetCloseBtn()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[title=\"Close\"]')[0]");
	}
	
	public WebElement marsContentFrame() 
	{
		return driver.findElement(By.id("content"));		
	}
	
	public WebElement msgBoardText()
	{				
		String str = "//*[@id=\"oClientCaps\"]/table/tbody/tr/td[1]/p";			
		return driver.findElement(By.xpath(str));		
	}
	
	public WebElement platformiFrame()
	{
		return driver.findElement(By.id("platformiframe"));
	}
	
	public WebElement financialManagementTab()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('#marsheader').contents().find('#misc > a'))[0].click();");
	}
	
	public void navigateToClientTab() throws MalformedURLException
	{
		clientTab();
		navigateToUrlUsingJs(new URL(config.getProperty(PropertyMap.RPM_URL)+"/client/clDemogrphcs.jsp"));
	}
	
	public void navigateToPaymentTab() throws MalformedURLException
	{
		paymentTab();
		navigateToUrlUsingJs(new URL(config.getProperty(PropertyMap.RPM_URL)+"/paymnt/pymtSrch.jsp"));
	}	
	
	public void navigateToAccessionTab() throws MalformedURLException
	{
		accessionTab();
		navigateToUrlUsingJs(new URL(config.getProperty(PropertyMap.RPM_URL)+"/accession/acnDemogrphc.jsp"));
	}
	
	public void navigateToFileMaintenanceTab() throws MalformedURLException
	{
		fileMaintenanceTab();
		navigateToUrlUsingJs(new URL(config.getProperty(PropertyMap.RPM_URL)+"/fileMaint/fmTestCodeFM.jsp"));
	}
	
	public void navigateToPayorTab() throws MalformedURLException
	{
		payorTab();
		navigateToUrlUsingJs(new URL(config.getProperty(PropertyMap.RPM_URL)+"/payor/pyrPrcngConfig.jsp"));
	}
	
	public void navigateToFinancialManagementTab()
	{
		financialManagementTab();
	}

	private void navigateToUrlUsingJs(URL url)
	{
		LOG.info("Navigating to URL using JS, orgAlias="+config.getProperty(PropertyMap.ORGALIAS)+", url="+url);
		driver.executeScript("$(window.open('"+url+"'))");
	}
}
