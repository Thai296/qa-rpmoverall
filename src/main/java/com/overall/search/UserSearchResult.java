package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class UserSearchResult {
    private RemoteWebDriver driver;
    protected Logger logger;
    
    public UserSearchResult(RemoteWebDriver driver) {
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }
    
    public WebElement dailyReceiptSearchResultsHelpIco() throws Exception{
        return driver.findElement(By.xpath("//*[@data-help-id='p_accession_user_search_results']"));
    }
    
    public WebElement pageTitle() throws Exception{
        return driver.findElement(By.xpath("//*[contains(@class,'searchGridHeader')]//span"));
    }
    
    public WebElement userSearchResultTbl() throws Exception{
        return driver.findElement(By.xpath("//*[@id='tbl_usersearchresults']"));
    }
    
    public WebElement userSearchResultTblUserIdLnk(String row) throws Exception {
        return driver.findElement(By.xpath("//*[@id='tbl_usersearchresults']//tr["+row+"]/td[@aria-describedby='tbl_usersearchresults_userId']/a"));
    }
    
    public WebElement userSearchResultTblFirstNameTxt(String row) throws Exception {
        return driver.findElement(By.xpath("//*[@id='tbl_usersearchresults']//tr["+row+"]/td[@aria-describedby='tbl_usersearchresults_firstName']"));
    }
    
    public WebElement userSearchResultTblDayOfLastNameTxt(String row) throws Exception {
        return driver.findElement(By.xpath("//*[@id='tbl_usersearchresults']//tr["+row+"]/td[@aria-describedby='tbl_usersearchresults_lastName']"));
    }
    
    public WebElement userSearchResultTblReloadGridBtn() {
        return driver.findElement(By.id("refresh_tbl_usersearchresults"));
    }
    
    public WebElement userSearchResultTblFirstPageIco() {
        return driver.findElement(By.id("first_pager"));
    }

    public WebElement userSearchResultTblPrevPageIco() {
        return driver.findElement(By.id("prev_pager"));
    }

    public WebElement userSearchResultTblPageInput() {
        return driver.findElement(By.xpath("//*[@id='pager_center']//input[@class='ui-pg-input']"));
    }

    public WebElement userSearchResultTblNextPageIco() {
        return driver.findElement(By.id("next_pager"));
    }

    public WebElement userSearchResultTblLastPageIco() {
        return driver.findElement(By.id("last_pager"));
    }

    public WebElement userSearchResultTblRowNumSel() {
        return driver.findElement(By.xpath("//*[@id='pager_center']//select[@class='ui-pg-selbox']"));
    }

    public WebElement userSearchResultTblTotalRecordTxt() {
        return driver.findElement(By.xpath("//*[@id='pager_right']/div"));
    }
    
    public WebElement keepSearchOpenChk() {
        return driver.findElement(By.id("keepSearchOpen"));
    }
    
    public WebElement closeBtn() {
        return driver.findElement(By.name("Close"));
    }
    
    public WebElement newSearchBtn() {
        return driver.findElement(By.name("Submit"));
    }
    
}
