package com.overall.payment.clientPayments;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.xifin.qa.config.PropertyMap;
import java.net.MalformedURLException;
import java.net.URL;

public class ClientPayments {

    protected Logger logger;
    private WebDriverWait wait;

    public ClientPayments(RemoteWebDriver driver, WebDriverWait wait) {
        this.wait= wait;
        logger = Logger.getLogger(this.getClass().getName()+ "],[" + driver);
    }

    public WebElement findClientPaymentsTitle() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
    }

    public WebElement loadClientPaymentsSectionBatchIdDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_depBatchIdSelect")));
    }

    public WebElement findBatchIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='select2-drop']/div/input")));
    }

    public WebElement findClientPaymentsDetailText() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_paymentDetail']/div[1]/span[1]")));
    }

    public WebElement findClientPaymentsTblTitle() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='mainSections']/div[2]/div/section/header/div[1]/span/span")));
    }

    public WebElement resetBtn() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("Reset")));
    }

    public void clickResetBtn() {
        resetBtn().click();
    }
}
