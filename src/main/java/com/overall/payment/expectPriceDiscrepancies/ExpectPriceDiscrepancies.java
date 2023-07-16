package com.overall.payment.expectPriceDiscrepancies;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExpectPriceDiscrepancies {

    private RemoteWebDriver driver;
    private WebDriverWait wait;

    public ExpectPriceDiscrepancies(RemoteWebDriver driver, WebDriverWait wait){
        this.wait=wait;
        this.driver=driver;
    }

    public WebElement loadExpectPriceDescrepenciesSectionBatchIdDdl(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("depBatchId")));
    }
}
