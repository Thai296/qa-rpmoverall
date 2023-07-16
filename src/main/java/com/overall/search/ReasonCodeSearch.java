package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ReasonCodeSearch {
    private RemoteWebDriver driver;
    protected Logger logger;

    public ReasonCodeSearch(RemoteWebDriver driver){
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }

    public WebElement reasonCodeInput() {
        return driver.findElement(By.id("reasonCode"));
    }

    public WebElement fieldDropdown() {
        return driver.findElement(By.id("field"));
    }

    public WebElement errorGroupDropdown() { return driver.findElement(By.id("errorGroup")); }

    public WebElement reasonCodeTableInput() {
        return driver.findElement(By.id("reasonCodeTable"));
    }

    public WebElement claimStatusErrorTypeDropdown() {
        return driver.findElement(By.id("claimStatusErrorType"));
    }

    public WebElement shortDescriptionInput() {
        return driver.findElement(By.id("shortDescription"));
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


    public WebElement errorGroupDropdownText() throws Exception {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"errorGroupDisplay\"]')[0]");
    }


}
