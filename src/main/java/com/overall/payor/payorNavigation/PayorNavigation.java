package com.overall.payor.payorNavigation;

import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class PayorNavigation
{
	private static final Logger LOG = Logger.getLogger(PayorNavigation.class);
	
	private final RemoteWebDriver driver;	
	private final Configuration config;
	
	public PayorNavigation(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.config = config;
	}
	
	public WebElement contentFrame()
	{
		return driver.findElement(By.id("content"));		
	}
	
	public WebElement groupDemographicsLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Group Demographics)'))[0].click();"); 
	}
	
	public WebElement pyrDemographicsLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#menu').contents().find('#vMenu')).find('[href*=\"pyrDemgrphc.jsp\"]'))[0].click();"); 
	}
	
	public WebElement pyrContactManagerLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#menu').contents().find('#vMenu')).find('[href*=\"pyrCntctMgr.jsp\"]'))[0].click();"); 
	}
	public WebElement pyrContractConfigLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Contract Config.)'))[0].click();"); 
	}
	
	public void navigateToGroupDemographicsLink() throws MalformedURLException
	{
		groupDemographicsLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/payor/pyrGrpDemgrphc.jsp"));
	}
	
	public void navigateToPricingConfigLink() throws MalformedURLException
	{
		groupDemographicsLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/payor/pyrPrcngConfig.jsp"));
	}
	
	public void navigateToContractConfigLink() throws MalformedURLException
	{
		pyrContractConfigLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/payor/pyrContrctConfig.jsp"));
	}
	
	public void navigateToPyrDemoLink() throws MalformedURLException
	{
		pyrDemographicsLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/payor/pyrDemgrphc.jsp"));
	}	
	
	public void navigateToDialysisFreqControlLink() throws MalformedURLException
	{
		pyrDemographicsLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/payor/pyrDialFrqCtrl.jsp"));
	}	
	
	public void navigateToPyrContactManager() throws MalformedURLException
	{
		pyrContactManagerLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/payor/pyrCntctMgr.jsp"));
	}

	private void navigateToUrl(URL url)
	{
		LOG.info("Navigating to URL using JS, orgAlias="+config.getProperty(PropertyMap.ORGALIAS)+", url="+url);
		driver.executeScript("$(window.open('"+url+"'))");
	}
}
