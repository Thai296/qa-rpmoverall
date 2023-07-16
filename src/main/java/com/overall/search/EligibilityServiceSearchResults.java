package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.support.ui.Select;


public class EligibilityServiceSearchResults {
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public EligibilityServiceSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*** Eligibility Service Search results***/
    public WebElement serviceSearchResultTitlePage () throws Exception {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_eligibilitySearch']/div[1]/span"));
    }
    
    public WebElement eligibilitySearchTbl() throws Exception {
        return driver.findElement(By.id("tbl_eligibilitySearch"));
    }
    
    public WebElement serviceSearchResultHelpIcon () throws Exception {
        return driver.findElement(By.id("pageHelpLink"));
    }
    
    public WebElement colRowInServiceSearchResult(int row, int col) throws Exception{
        return driver.findElement(By.xpath(".//*[@id='tbl_eligibilitySearch']/tbody/tr["+row+"]/td["+col+"]"));
    }
    
    public WebElement colRowInServiceSearchResultHyberLink(int row, int col) throws Exception{
      //  return driver.findElement(By.xpath(".//*[@id='tbl_eligibilitySearch']/tbody/tr["+row+"]/td["+col+"]/a"));
       return driver.findElement(By.xpath(".//*[@id='"+row+"']/td["+col+"]/a"));
    }
    
    public WebElement loadingIcon () throws Exception {
        return driver.findElement(By.xpath(".//*[@id='refresh_tbl_eligibilitySearch']/div/span"));
    }
    public WebElement nextPagerIconInServiceSearchResult () throws Exception {
        return driver.findElement(By.xpath(".//*[@id='next_pager']/span"));
    }
    
    public WebElement lastPagerIconInServiceSearchResult () throws Exception {
        return driver.findElement(By.xpath(".//*[@id='last_pager']/span"));
    }
    
    public WebElement totalRecordInServiceSearchResult () throws Exception {
        return driver.findElement(By.xpath(".//*[@id='pager_right']/div"));
    }
    
    public WebElement keepSearchOpenInput () throws Exception {
        return driver.findElement(By.id("keepSearchOpen"));
    }
    
    public WebElement closeBtnInServiceSearchResult () throws Exception {
        return driver.findElement(By.xpath(".//*[@id='eligibilitySearchRow']/div[3]/button[2]"));
    }

}
