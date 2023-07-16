package com.overall.financialManagement.endOfMonth;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EomSchedule {
    private RemoteWebDriver driver;
    protected Logger logger;
    private WebDriverWait wait;

    public EomSchedule(RemoteWebDriver driver, WebDriverWait wait){
        this.driver=driver;
        this.wait=wait;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }
    public WebElement eomScheduleLoadPgTitleTxt() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.className("platormPageTitle")));
    }
    public WebElement yearDropDown(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_selectYear")));
    }
}
