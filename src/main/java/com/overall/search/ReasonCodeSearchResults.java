package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ReasonCodeSearchResults {
    private RemoteWebDriver driver;
    protected Logger logger;

    public ReasonCodeSearchResults(RemoteWebDriver driver){
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }

    public WebElement reasonCodeSearchResultTbl() {
        return driver.findElement(By.id("tbl_reasoncodesearchresults"));
    }

    public WebElement reasonCodeColLnk(String row) throws Exception{
        return driver.findElement(By.xpath(".//*[@id='tbl_reasoncodesearchresults']/tbody/tr["+row+"]/td[@aria-describedby='tbl_reasoncodesearchresults_reasonCodeId']/a"));
    }

    public WebElement reasonCodeShortDescrColTxt(String row) throws Exception{
        return driver.findElement(By.xpath(".//*[@id='tbl_reasoncodesearchresults']/tbody/tr["+row+"]/td[@aria-describedby='tbl_reasoncodesearchresults_shortDescription']"));
    }

    public WebElement closeBtn() {
        return driver.findElement(By.xpath("//*[@value='Close']"));
    }

    public WebElement newSearchBtn() {
        return driver.findElement(By.xpath("//*[@value='Submit']"));
    }

    public WebElement keepSearchOpenChk() throws Exception{
        return driver.findElement(By.id("keepSearchOpen"));
    }

    public WebElement totalResultsLbl() throws Exception{
        return driver.findElement(By.id("pager_right"));
    }

    public WebElement navBtn(String nav) throws Exception{
        // first , prev , next ,last
        return driver.findElement(By.id(""+nav+"_pager"));
    }

}
