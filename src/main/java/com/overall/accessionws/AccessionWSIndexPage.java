package com.overall.accessionws;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;


public class AccessionWSIndexPage {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private SeleniumBaseTest b;
	

	public AccessionWSIndexPage(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
		
	
	public WebElement accessionWSLink() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='webservicesList']/tbody/tr[7]/td/a"));
	}	
	
	public void clickAccessionWSLink() throws Exception{
		accessionWSLink().click();
		logger.info("        Click on Accession WSDL link.");
	}
	
	
	
}
