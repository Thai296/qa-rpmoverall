package com.overall.eppws;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.RandomCharacter;
import com.overall.utils.TestCodeUtils;

public class EppWSIndexPage {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private DaoManagerXifinRpm daoManagerXifinRpm;
	private SeleniumBaseTest b;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;

	public EppWSIndexPage(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
		
	
	public WebElement eppWSLink() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='webservicesList']/tbody/tr[2]/td/a"));
	}	
	
	public void clickEPPWSLink() throws Exception{
		eppWSLink().click();
		logger.info("        Click on Electronic Payment Posting WSDL link.");
	}
	
	public WebElement eppWSDLContents() throws Exception{		
		//return driver.findElement(By.cssSelector("#collapsible295 > div.expanded > div:nth-child(1) > span.html-tag > span > span.html-attribute-value")); //works locally on my pc with the newer chrome
		return driver.findElement(By.cssSelector("#collapsible295 > div.expanded > div:nth-child(1) > span.webkit-html-tag > span > span.webkit-html-attribute-value"));  //works in QA VMs with older chrome
	}	

}
