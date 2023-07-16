package com.overall.payment.nonClientSummary;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NonClientSummary {
    private RemoteWebDriver driver;
    private WebDriverWait wait;
    public NonClientSummary(RemoteWebDriver driver, WebDriverWait wait){
        this.driver=driver;
        this.wait=wait;
    }
    public WebElement batchIDSelect(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("depBatchIdSelect")));
    }
}
