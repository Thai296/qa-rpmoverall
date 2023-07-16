package com.overall.accession.accnEpSummary;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EpSummary {
    protected Logger logger;
    private WebDriverWait wait;

    public EpSummary(RemoteWebDriver driver, WebDriverWait wait) {
        this.wait= wait;
        logger = Logger.getLogger(this.getClass().getName() + "],[" +driver );
    }
    public WebElement epSummaryHeaderTitleTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
    }
    public WebElement errorDate(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_bsdOn")));
    }
}
