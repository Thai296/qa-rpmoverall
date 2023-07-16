package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DailyReceiptSearchResults {

    private RemoteWebDriver driver;
    protected Logger logger;
    
    public DailyReceiptSearchResults(RemoteWebDriver driver) {
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }
    
    public WebElement dailyReceiptSearchResultsHelpIco() throws Exception{
        return driver.findElement(By.xpath("//*[@data-help-id='p_daily_receipt_search_results']"));
    }
    
    public WebElement pageTitle() throws Exception{
        return driver.findElement(By.xpath("//*[contains(@class,'searchGridHeader')]//span"));
    }
    
    public WebElement dailyReceiptSearchResultsTbl() throws Exception{
        return driver.findElement(By.xpath("//*[@id='dailyreceiptsearchTable']"));
    }
    
    public WebElement dailyReceiptSearchResultsTblReceiptIDLnk(String row) throws Exception {
        return driver.findElement(By.xpath("//*[@id='dailyreceiptsearchTable']//tr["+row+"]/td[@aria-describedby='dailyreceiptsearchTable_dailyRcptAbbrev']/a"));
    }
    
    public WebElement dailyReceiptSearchResultsTblPaymentFacilityIdTxt(String row) throws Exception {
        return driver.findElement(By.xpath("//*[@id='dailyreceiptsearchTable']//tr["+row+"]/td[@aria-describedby='dailyreceiptsearchTable_facAbbrv']"));
    }
    
    public WebElement dailyReceiptSearchResultsTblDayOfPaymentUserIDTxt(String row) throws Exception {
        return driver.findElement(By.xpath("//*[@id='dailyreceiptsearchTable']//tr["+row+"]/td[@aria-describedby='dailyreceiptsearchTable_userId']"));
    }
    
    public WebElement dailyReceiptSearchResultsTblDayOfCreationDateTxt(String row) throws Exception {
        return driver.findElement(By.xpath("//*[@id='dailyreceiptsearchTable']//tr["+row+"]/td[@aria-describedby='dailyreceiptsearchTable_userId']"));
    }
    
    public WebElement dailyReceiptSearchResultsTblReloadGridBtn() {
        return driver.findElement(By.id("refresh_dailyreceiptsearchTable"));
    }
    
    public WebElement dailyReceiptSearchResultsTblFirstPageIco() {
        return driver.findElement(By.id("first_pager"));
    }

    public WebElement dailyReceiptSearchResultsTblPrevPageIco() {
        return driver.findElement(By.id("prev_pager"));
    }

    public WebElement dailyReceiptSearchResultsTblPageInput() {
        return driver.findElement(By.xpath("//*[@id='pager_center']//input[@class='ui-pg-input']"));
    }

    public WebElement dailyReceiptSearchResultsTblNextPageIco() {
        return driver.findElement(By.id("next_pager"));
    }

    public WebElement dailyReceiptSearchResultsTblLastPageIco() {
        return driver.findElement(By.id("last_pager"));
    }

    public WebElement dailyReceiptSearchResultsTblRowNumSel() {
        return driver.findElement(By.xpath("//*[@id='pager_center']//select[@class='ui-pg-selbox']"));
    }

    public WebElement dailyReceiptSearchResultsTblTotalRecordTxt() {
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
