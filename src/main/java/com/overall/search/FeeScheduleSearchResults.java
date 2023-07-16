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

public class FeeScheduleSearchResults {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private DaoManagerXifinRpm daoManagerXifinRpm;
	private SeleniumBaseTest b;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;

	public FeeScheduleSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	public WebElement searchResultTitle() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='gview_feeschedulesearchTable']/div[1]/span"));
	}
	public WebElement keepSearchOpenCheckBox() throws Exception{
		return driver.findElement(By.id("keepSearchOpen"));
	}
	public WebElement newSearchBtn() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='feeScheduleSearch']/div[3]/button[1]"));
	}
	public WebElement closeSearchBtn() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='feeScheduleSearch']/div[3]/button[2]"));
	}
	
	public WebElement feeScheduleTblFSID(int row, int col) throws Exception{
		return driver.findElement(By.xpath("//*[@id='feeschedulesearchTable']/tbody/tr["+ row +"]/td["+ col +"]/a"));
	}
	
	public WebElement feeScheduleTblFSCell(int row, int col) throws Exception{
		return driver.findElement(By.xpath("//*[@id='feeschedulesearchTable']/tbody/tr["+ row +"]/td["+ col +"]"));
	}
	
	public WebElement feeScheduleTblRightResult()throws Exception{
		return driver.findElement(By.xpath(".//*[@id='pager_right']/div"));
	}
	
	public void clickCloseSearchBtn() throws Exception{
		closeSearchBtn().click();
		logger.info("        Click on Close Search button");
	}
	public WebElement firstPage() throws Exception{
		return driver.findElement(By.id("first_pager"));
	}
	public WebElement prevPage() throws Exception{
		return driver.findElement(By.id("prev_pager"));
	}
	public WebElement nextPage() throws Exception{
		return driver.findElement(By.id("next_pager"));
	}
	public WebElement lastPage() throws Exception{
		return driver.findElement(By.id("last_pager"));
	}
}
