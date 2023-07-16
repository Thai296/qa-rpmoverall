package com.overall.accession.PatientServiceCenter;

import com.overall.electronicpayment.ClientTransactionDetailElectronicPayment;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PSCPrepayConfig extends SeleniumBaseTest
{
    private RemoteWebDriver driver;
    private Logger logger;
    private WebDriverWait wait;
    private SeleniumBaseTest b;

    public PSCPrepayConfig(RemoteWebDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }

    /*** PSC Prepayment Load page***/
    public WebElement pscPrepayPageTitle () {
        return driver.findElement(By.xpath("//*[@id=\"pscPrepaymentForm\"]/div[1]/div/div/div[2]/span"));
    }

    public WebElement orderIdInput() { return driver.findElement(By.id("orderId")); }

    public WebElement amountInput() { return driver.findElement(By.id("amount")); }

    public WebElement helpIconInHeaderSection()
    {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-help-id='p_patient_service_center_header']")));
    }

    public WebElement helpIconInOrderInformationSection()
    {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-help-id='p_patient_service_center_order_info']")));
    }

    public WebElement helpIconInPatientInformationSection()
    {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-help-id='p_patient_service_center_patient_info']")));
    }

    public WebElement helpIconInAmountSection()
    {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-help-id='p_patient_service_center_amount']")));
    }

    public WebElement resetButton()
    {
        return driver.findElement(By.id("Reset"));
    }

    public WebElement titleTextInHelp() {
        return driver.findElement(By.xpath("/html/body/div[1]/h1"));
    }

    public WebElement helpPageTableOfContentsLink()
    {
        return driver.findElement(By.linkText("Table of Contents"));
    }

    public WebElement enterCardButton()
    {
        return driver.findElement(By.id("fallBackBtn"));
    }

    public void setOrderIdInput(String orderId)
    {
        WebElement testOrderId = orderIdInput();
        testOrderId.clear();
        testOrderId.sendKeys(orderId);
        logger.info("Entered Order ID: " + orderId);
    }

    public void setAmountInput(String pmtAmnt)
    {
        amountInput().sendKeys(pmtAmnt);
        logger.info("Entered Order ID: " + pmtAmnt);
    }

    public WebElement patientPrepaymentIFrame(){
        return driver.findElement(By.id("xifinpciiframe"));
    }

    public WebElement transactionId(){
        return driver.findElement(By.xpath("//div[contains(@class,'transactionId')]"));
    }

    public WebElement finishBtn(){
        return driver.findElement(By.xpath("//button//span[text()='Finish']"));
    }

    public String getTransId(SeleniumBaseTest b, String orderId, String pmtAmnt, String cardNumber, String expMonth, String expYr) throws Exception {
        ClientTransactionDetailElectronicPayment clientTransactionDetailElectronicPayment = new ClientTransactionDetailElectronicPayment(driver);
        logger.info("*** Actions: - Enter Order Id, Amount in Pre-Payment and click Enter Card");
        setOrderIdInput(orderId);
        setAmountInput(pmtAmnt);
        enterCardButton().click();
        b.switchToFrame(patientPrepaymentIFrame());

        logger.info("*** Actions: - Enter Card Number, Month and Year and click Pay");
        clientTransactionDetailElectronicPayment.setCardNumber(cardNumber);
        clientTransactionDetailElectronicPayment.setMonth(expMonth);
        clientTransactionDetailElectronicPayment.setYear(expYr);
        b.clickHiddenPageObject(clientTransactionDetailElectronicPayment.payBtn(), 0);

        logger.info("*** Actions: - Confirm");
        b.clickHiddenPageObject(clientTransactionDetailElectronicPayment.confirmBtn(), 0);
        b.switchToPopupWin();

        logger.info("*** Actions: - Get Transaction Id and Finish");
        String transId = transactionId().getText();
        finishBtn().click();
        return transId;
    }
}
