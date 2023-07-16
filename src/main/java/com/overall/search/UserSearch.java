package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class UserSearch {
    private RemoteWebDriver driver;
    protected Logger logger;
    
    public UserSearch(RemoteWebDriver driver) {
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }
    
    public WebElement searchPageTitle() {
        return driver.findElement(By.className("titleText"));
    }
    
    public WebElement searchPageHelpIco() {
        return driver.findElement(By.xpath("//*[@data-help-id='p_accession_user_search']"));
    }
    
    public WebElement userIdInput() {
        return driver.findElement(By.id("userId"));
    }
    
    public WebElement userGroupSel() {
        return driver.findElement(By.id("userGroupId"));
    }
    
    public WebElement firstNameInput() {
        return driver.findElement(By.id("firstName"));
    }
    
    public WebElement lastNameInput() {
        return driver.findElement(By.id("lastName"));
    }
    
    public WebElement includeDeletedRecordsChk() {
        return driver.findElement(By.id("inclDelRec"));
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
