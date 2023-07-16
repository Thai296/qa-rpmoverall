package com.overall.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

import com.eviware.soapui.support.StringUtils;
import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.RandomCharacter;
import com.overall.utils.TestCodeUtils;

public class AccessionSearchResults {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private DaoManagerXifinRpm daoManagerXifinRpm;
	private SeleniumBaseTest b;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;

	public AccessionSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement accnSearchResultTable() throws Exception {
		return driver.findElement(By.id("accessionsearchTable"));
	}
	
	public WebElement accnSrchResultsTable(int row, int col){
		return driver.findElement(By.xpath(".//*[@id='accessionsearchTable']/tbody/tr["+row+"]/td["+col+"]/a"));  //*[@id="accessionsearchTable"]/tbody/tr[2]/td[2]/a
	}
	
	public void selectAccnSrchResults(int row, int col) throws Exception{
		accnSrchResultsTable(row, col).click();
		logger.info("        selected an Accession.");
	}
	
	/*
	public WebElement physSrchResultsTable(int row, int col){
		return driver.findElement(By.xpath(".//*[@id='physiciansearchTable']/tbody/tr["+row+"]/td["+col+"]/a"));  //*[@id="accessionsearchTable"]/tbody/tr[2]/td[2]/a
	}
	
	public void selectPhysSrchResults(int row, int col) throws Exception{
		physSrchResultsTable(row, col).click();
		logger.info("        selected a Physician.");
	}	
	*/

}
