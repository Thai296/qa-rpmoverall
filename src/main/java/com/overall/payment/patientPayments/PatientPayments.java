package com.overall.payment.patientPayments;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PatientPayments {
    protected Logger logger;
    private WebDriverWait wait;

    public PatientPayments(RemoteWebDriver driver, WebDriverWait wait) {
        this.wait= wait;
        logger = Logger.getLogger(this.getClass().getName()+ "],[" + driver);
    }
    public WebElement loadBatchSectionBatchIdDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_depBatchId")));
    }
}
