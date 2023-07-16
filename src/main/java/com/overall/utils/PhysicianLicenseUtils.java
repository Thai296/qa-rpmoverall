package com.overall.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.overall.filemaint.physicianLicense.PhysicianLicense;
import com.xifin.utils.SeleniumBaseTest;

public class PhysicianLicenseUtils extends SeleniumBaseTest {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private PhysicianLicense physicianLicense;
	
	public PhysicianLicenseUtils(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		physicianLicense = null;
	}

	public void setPhysicianLicense(PhysicianLicense physicianLicense){
		this.physicianLicense = physicianLicense;
	}

	
	public boolean clickDoubleOnSaveAndClearBtn(SeleniumBaseTest b) throws Exception {
		if(physicianLicense == null){
			physicianLicense= new PhysicianLicense(driver);
		}
		boolean result = false;

		if (physicianLicense.npiIdLookupInput().isDisplayed()) {
			result = true;
		} else if (physicianLicense.headerNpiIdInput().isDisplayed()) {
			b.clickHiddenPageObject(physicianLicense.saveAndClearBtn(), 0);
			result = true;
		}
		return result;
	}
	
	
	
	
}
