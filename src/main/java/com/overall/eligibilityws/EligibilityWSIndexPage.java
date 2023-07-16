package com.overall.eligibilityws;

import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;


public class EligibilityWSIndexPage {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private SeleniumBaseTest b;
	

	public EligibilityWSIndexPage(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
		
	
	public WebElement eligibilityWSLink() {
		return driver.findElement(By.xpath(".//*[@id='webservicesList']/tbody/tr[2]/td/a"));
	}	
	
	public void clickEligibilityWSLink(){
		eligibilityWSLink().click();
		logger.info("        Click on Eligibility WSDL link.");
	}
	public WebElement eligibilityWSHeading() {
		return driver.findElement(By.xpath("//*[@id=\"webservicesList\"]/tbody/tr[1]/td"));
	}

}
