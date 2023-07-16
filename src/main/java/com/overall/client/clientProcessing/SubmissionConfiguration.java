package com.overall.client.clientProcessing;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SubmissionConfiguration {
    protected Logger logger;
    private WebDriverWait wait;

    public SubmissionConfiguration(RemoteWebDriver driver, WebDriverWait wait){
        this.wait = wait;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }
    public WebElement loadClientSectionClientIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientAbbrev")));
    }
}
