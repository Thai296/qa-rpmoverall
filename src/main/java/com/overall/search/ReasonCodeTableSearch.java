package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ReasonCodeTableSearch {
    private RemoteWebDriver driver;
    protected Logger logger;

    public ReasonCodeTableSearch(RemoteWebDriver driver){
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }

    public WebElement reasonCodeTblIdInput() {
        return driver.findElement(By.id("reasonCodeTableId"));
    }

    public WebElement reasonCodeInput() {
        return driver.findElement(By.id("reasonCode"));
    }

    public WebElement pyrIdInput() {
        return driver.findElement(By.id("payorAbbrev"));
    }

    public WebElement nameInput() {
        return driver.findElement(By.id("name"));
    }

    public WebElement closeBtn() {
        return driver.findElement(By.xpath("//*[@value='Close']"));
    }

    public WebElement resetBtn() {
        return driver.findElement(By.xpath("//*[@value='Reset']"));
    }

    public WebElement searchBtn() {
        return driver.findElement(By.xpath("//*[@value='Submit']"));
    }
}
