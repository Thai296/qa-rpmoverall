package com.overall.accession.accessionNavigation;

import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class AccessionNavigation
{
	private static final Logger LOG = Logger.getLogger(AccessionNavigation.class);

	private final RemoteWebDriver driver;
	private final Configuration config;

	public AccessionNavigation(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.config = config;
	}

	public By contentFrameLocator()
	{
		return By.id("content");
	}

	public WebElement contentFrame()
	{
		return driver.findElement(contentFrameLocator());
	}
	
	public WebElement platformiFrame()
	{
		return driver.findElement(By.id("platformiframe"));
	}
	
	public WebElement demographicsLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Demographics)'))[0].click();");
	}
	
	public WebElement detailLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Detail)'))[0].click();"); 		
	}
	
	public WebElement diagnosisLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Diagnosis)'))[0].click();"); 
	}
	
	public WebElement superSearchLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Super Search)'))[0].click();"); 
	}
	public WebElement superSearchLinkPF()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Super Search)'))[1].click();");
	}
	
	public WebElement transactionDetailLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Transaction Detail)'))[0].click();"); 
	}

	public WebElement epDunningLetterLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(EP Dunning Letter)'))[0].click();");
	}
	
	public WebElement epSearchLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(EP Search)'))[0].click();");
	}
	public WebElement epSearchLinkPF() throws Exception {
		//return driver.findElementByLinkText("EP Search");
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(EP Search)'))[1].click();");
	}
	public WebElement epSummaryLink() throws Exception {
		return driver.findElementByLinkText("EP Summary");
	}
	
	//Order Processing
	public WebElement accnTestUpdateLink()  {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Accn Test Update)'))[0].click();"); 
	}

	public WebElement orderEntryLinkPF()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Order Entry)'))[0].click();");
	}
	
	public WebElement patientDemographicsLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Patient Demographics)'))[0].click();");
	}
	
	public WebElement standingOrderLink()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Standing Order)'))[0].click();");
	}
	//Patient Service Center - Patient Estimation
	public WebElement patientEstimationLinkPF() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Patient Estimation)'))[0].click();");
	}

	//Patient Service Center - Prepayment
	public WebElement pscPrepayLinkPF() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Pre-Payment)'))[0].click();");
	}
	public WebElement accnSingleStatementLinkPF() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($($('#menu').contents().find('#vMenu')).find('li.indent > a:contains(Single Statement (New))'))[0].click();");
	}
	//------------------------------------------------------------------------------
	public void navigateToEPDunningLetter() throws MalformedURLException
	{
		epDunningLetterLink();
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/ep/epDunLtr.jsp");
		navigateToUrlUsingJs(url);
	}
	
	public void navigateToSuperSearch() throws MalformedURLException
	{
		superSearchLink();
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/accession/acnSupSrch.jsp");
		navigateToUrlUsingJs(url);
	}
	
	public void navigateToDetail() throws MalformedURLException
	{
		detailLink();
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accnprocessing.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrlUsingJs(url);
	}
	
	public void navigateToDiagnosis() throws MalformedURLException
	{
		diagnosisLink();
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/accession/acnDiag.jsp");
		navigateToUrlUsingJs(url);
	}

	public void navigateToTransactionDetailLink() throws MalformedURLException
	{
		transactionDetailLink();
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/accession/acnTransDetail.jsp");
		navigateToUrlUsingJs(url);
	}
	
	public void navigateToAccnTestUpdateLinkLink() throws MalformedURLException
	{
		accnTestUpdateLink();
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/accession/acnLabComponent.jsp");
		navigateToUrlUsingJs(url);
	}
	
	public void navigateToEPSearchLink() throws MalformedURLException
	{
		epSearchLink();
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/ep/epSrch.jsp");
		navigateToUrlUsingJs(url);
	}
	
	public void navigateToDemographicsLink() throws MalformedURLException
	{
		demographicsLink();
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/accession/acnDemogrphc.jsp");
		navigateToUrlUsingJs(url);
	}
	
	public void navigateToPatientDemographicsLink() throws MalformedURLException
	{
		patientDemographicsLink();
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/accession/acnPatDemogrphcContent.jsp");
		navigateToUrlUsingJs(url);
	}
	
	public void navigateToOriginalPatientDemographicsLink() throws MalformedURLException
	{
		patientDemographicsLink();
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/accession/acnPatDemogrphc.jsp");
		navigateToUrlUsingJs(url);
	}
	
	public void navigateToStandingOrderLink() throws MalformedURLException
	{
		standingOrderLink();
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/accession/acnStndngOrder.jsp");
		navigateToUrlUsingJs(url);
	}

	private void navigateToUrlUsingJs(URL url)
	{
		LOG.info("Navigating to URL using JS, orgAlias="+config.getProperty(PropertyMap.ORGALIAS)+", url="+url);
		driver.executeScript("$(window.open('"+url+"'))");
	}
}
