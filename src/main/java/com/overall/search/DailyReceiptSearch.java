package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DailyReceiptSearch {
    private RemoteWebDriver driver;
    protected Logger logger;
    
    public DailyReceiptSearch(RemoteWebDriver driver) {
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }
    
    public WebElement searchPageTitle() {
        return driver.findElement(By.className("titleText"));
    }
    
    public WebElement paymentFacilityIdInput() {
        return driver.findElement(By.id("facAbbrev"));
    }
    
    public WebElement nameInput() {
        return driver.findElement(By.id("facName"));
    }
    
    public WebElement receiptIdInput() {
        return driver.findElement(By.id("rcptAbbrev"));
    }
    
    public WebElement paymentUserIdInput() {
        return driver.findElement(By.id("userId"));
    }
    
    public WebElement creationDate() {
        return driver.findElement(By.id("createDate"));
    }
    
    public WebElement closeBtn() {
        return driver.findElement(By.name("Close"));
    }
    
    public WebElement resetBtn() {
        return driver.findElement(By.name("Cancel"));
    }
    
    public WebElement searchBtn() {
        return driver.findElement(By.name("Submit"));
    }
    
}
