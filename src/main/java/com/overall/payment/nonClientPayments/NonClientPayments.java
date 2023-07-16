package com.overall.payment.nonClientPayments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NonClientPayments {
    private RemoteWebDriver driver;
    private WebDriverWait wait;

    public NonClientPayments(RemoteWebDriver driver, WebDriverWait wait){

        this.wait=wait;
        this.driver=driver;
    }
    public WebElement loadNonClientPaymentsSectionBatchIdDdl(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("s2id_depBatchId")));
    }
}
