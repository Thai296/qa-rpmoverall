package com.overall.fileMaintenance.fileMaintenanceNavigation;

import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class FileMaintenanceNavigation
{
	private static final Logger LOG = Logger.getLogger(FileMaintenanceNavigation.class);

	private final RemoteWebDriver driver;
	private final Configuration config;
	
	public FileMaintenanceNavigation(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.config = config;
	}

	public WebElement contentFrame() {
		return driver.findElement(By.id("content"));		
	}

	
	public WebElement clientBillingCategoryLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Client Billing Category)'))[0].click();");
	}

	public WebElement eligibilityTranslationLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Eligibility Translation)'))[0].click();");	
	}

	public WebElement physicianLicenseLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Physician License)'))[0].click();");
	}

	public WebElement procedureCodeLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Procedure Code)'))[0].click();");
	}
	
	public WebElement reasonCodeLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Reason Code)'))[0].click();");
	}

	public WebElement patternDefinitionLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Pattern Definition)'))[0].click();");
	}
	
	public WebElement testCodeLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Test Code(New))'))[0].click();");	
	}
	
	public WebElement testCodeNewLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Test Code(New))'))[0].click();");	
	}
	
	public WebElement systemDataCacheLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(System Data Cache)'))[0].click();"); 
	}
	
	public WebElement taskSchedulerLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Task Scheduler)'))[0].click();");
	}
	
	public WebElement taskStatusLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Task Status)'))[0].click();");
	}
	
	public void navigateToSystemDataCacheLink() throws MalformedURLException
	{
		systemDataCacheLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/util/cacheConfig.jsp"));
	}
	
	public void navigateToTaskSchedulerLink() throws MalformedURLException
	{
		taskSchedulerLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/fileMaint/fmSchedConfg.jsp"));
	}
	
	public void navigateToTaskStatusLink() throws MalformedURLException
	{
		taskStatusLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/fileMaint/fmSchedStat.jsp"));
	}
	
	public void navigateToPatternDefinitionLink() throws MalformedURLException
	{
		patternDefinitionLink();
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)+"/fileMaint/fmOEvalPatMaint.jsp"));
	}	

	public void navigateToTestCodeNewLink()
	{
		testCodeNewLink();
	}

	private void navigateToUrl(URL url)
	{
		LOG.info("Navigating to URL using JS, orgAlias="+config.getProperty(PropertyMap.ORGALIAS)+", url="+url);
		driver.executeScript("$(window.open('"+url+"'))");
	}
}
