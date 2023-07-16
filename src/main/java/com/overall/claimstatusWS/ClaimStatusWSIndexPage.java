package com.overall.claimstatusWS;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;


public class ClaimStatusWSIndexPage {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private SeleniumBaseTest b;
	

	public ClaimStatusWSIndexPage(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
		
	
	public WebElement claimStatusWSLink() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='webservicesList']/tbody/tr[2]/td/a"));
	}	
	
	public void clickClaimStatusWSLink() throws Exception{
		claimStatusWSLink().click();
		logger.info("        Click on Claim Status WSDL link.");
	}
	
	
	
}
