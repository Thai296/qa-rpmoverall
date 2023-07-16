package com.overall.fileMaintenance.sysMgt;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class DSPayorSearchResult {

	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public DSPayorSearchResult(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	public WebElement payorSearchResultTbl() throws Exception{
		return driver.findElement(By.id("gbox_payorsearchTable"));
	}
	
	public WebElement payorSearchResultGrid(int row, int col) throws Exception{
		return driver.findElement(By.xpath("//*[@id='payorsearchTable']/tbody/tr[" + row + "]/td[" + col + "]/a"));
	}
}
