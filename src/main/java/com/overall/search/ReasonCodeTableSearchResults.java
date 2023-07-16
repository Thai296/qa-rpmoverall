package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ReasonCodeTableSearchResults {
    private RemoteWebDriver driver;
    protected Logger logger;

    public ReasonCodeTableSearchResults(RemoteWebDriver driver){
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }

    public WebElement reasonCodeSearchResultTbl() throws Exception{
        return driver.findElement(By.id("tbl_reasoncodesearchresults"));
    }

    public WebElement reasonCodeTblIdColTxt(String row) throws Exception{
        return driver.findElement(By.xpath(".//*[@id='tbl_reasoncodesearchresults']/tbody/tr["+row+"]/td[@aria-describedby='tbl_reasoncodesearchresults_abbrv']"));
    }

    public WebElement reasonCodeTblIdColLnk(String row) throws Exception{
        return driver.findElement(By.xpath(".//*[@id='tbl_reasoncodesearchresults']/tbody/tr["+row+"]/td[@aria-describedby='tbl_reasoncodesearchresults_abbrv']/a"));
    }

    public WebElement reasonCodeTblNameText(String row) throws Exception{
        return driver.findElement(By.xpath(".//*[@id='tbl_reasoncodesearchresults']/tbody/tr["+row+"]/td[@aria-describedby='tbl_reasoncodesearchresults_descr']"));
    }

    public WebElement newSearchBtn() throws Exception{
        return driver.findElement(By.xpath(".//*[@accesskey='s']"));
    }

    public WebElement closeBtn() throws Exception{
        return driver.findElement(By.xpath(".//*[@accesskey='c']"));
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
