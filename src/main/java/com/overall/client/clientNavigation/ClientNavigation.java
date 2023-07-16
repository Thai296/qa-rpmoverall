package com.overall.client.clientNavigation;

import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class ClientNavigation
{
	private static final Logger LOG = Logger.getLogger(ClientNavigation.class);	
	private final RemoteWebDriver driver;	
	private final Configuration config;
	
	public ClientNavigation(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.config = config;
	}	
		
	public WebElement pyrExclusionLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Payor Exclusions)'))[0].click();"); 
	}
	
	public WebElement auditLogLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Audit Log)'))[0].click();"); 
	}
	
	public WebElement physAssignmentLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Physician Assignment)'))[0].click();"); 
	}
	
	public WebElement eligCensusConfigLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Eligibility Census Configuration)'))[0].click();"); 
	}
	
	public WebElement clientDeomgraphicsLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Demographics)'))[0].click();"); 
	}
	
	public WebElement contactManagerLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Contact Manager)'))[0].click();"); 
	}

	public WebElement priceInquiryLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Price Inquiry)'))[0].click();"); 
	}	

	public void navigateToPriceInquiry() throws MalformedURLException
	{
		priceInquiryLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/client/clPrcInqry.jsp"));
	}		

	public void navigateToEligCensusConfig() throws MalformedURLException
	{
		eligCensusConfigLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/client/eligRsterConfig.jsp"));
	}		
	
	public void navigateToPhysAssignment() throws MalformedURLException
	{
		physAssignmentLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/client/clnPhysAssgn.jsp"));
	}	

	
	public void navigateToAuditLogLink() throws MalformedURLException
	{
		auditLogLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/client/clAuditLg.jsp"));
	}
	
	public void navigateToContactManagerLink() throws MalformedURLException
	{
		contactManagerLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/client/clNotesPrmsPmts.jsp"));
	}

	private void navigateToUrl(URL url)
	{
		LOG.info("Navigating to URL using JS, orgAlias="+config.getProperty(PropertyMap.ORGALIAS)+", url="+url);
		driver.executeScript("$(window.open('"+url+"'))");
	}
}
