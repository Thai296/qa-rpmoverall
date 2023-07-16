package com.overall.menu;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MenuLinkNavigation {
    private static final Logger LOG = Logger.getLogger(MenuLinkNavigation.class);
    private final RemoteWebDriver driver;
    private final WebDriverWait wait;

    public MenuLinkNavigation(RemoteWebDriver driver, WebDriverWait wait){
        this.driver=driver;
        this.wait = wait;
    }
    public WebElement xifinLogo(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logo_image")));
    }
    public WebElement accessionMenu(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='accn']/span")));
    }
    public WebElement accessionDetailLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='detail']/a")));
    }
    public WebElement accessionSuperSearchLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='supsearch']/a")));
    }
    public WebElement accessionEPSearchLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='epsearch']/a")));
    }
    public WebElement accessionPatientDemographicsLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='patientdemographics']/a")));
    }
    public WebElement clientMenu(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='client']/span")));
    }
    public WebElement clientDemographicsLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='clientdemographics']/a")));
    }
    public WebElement pricingConfigLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='clnprcconfig']/a")));
    }
    public WebElement transactionDetailLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='clntransdet']/a")));
    }
    public WebElement fileMaintenance(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='filemaint']/span")));
    }
    public WebElement eligibilityConfigurationLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='fmeligibilityconfig']/a")));
    }
    public WebElement eligibilityTranslationLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='fmeligibilitytranslation']/a")));
    }
    public WebElement reasonCodeLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='fmreasoncodeconfig']/a")));
    }
    public WebElement testCodeLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='testcode']/a")));
    }
    public WebElement submissionRemitLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='fmsubmremit']/a")));
    }
    public WebElement paymentDepositLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='paymentdeposits']/a")));
    }
    public WebElement submissionConfigurationLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='clnsubmconfig']/a")));
    }
    public WebElement nonClientSummaryLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='nonclientsummary']/a")));
    }
    public WebElement payorMenu(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='payor']/span")));
    }
    public WebElement payorDemographicsLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='payordemographics']/a")));
    }
    public WebElement contentFrame(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));
    }
    public WebElement financialManagementMenu(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='misc']/span")));
    }
    public WebElement closingPackageLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='eom']/a")));
    }
    public WebElement historyLogLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='history']/a")));
    }
    public WebElement notesandPromisedPaymentsLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='acnnotesprmspmts']/a")));
    }
    public WebElement singleStatementLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='singlestatement']/a")));
    }
    public WebElement testUpdateLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='accntestupdate']/a")));
    }
    public WebElement contactManagerLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='clientcontactmanager']/a")));
    }
    public WebElement eligibilityCensusConfigurationLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='clienteligibilitycensusconfiguration']/a")));
    }
    public WebElement physicianLicenseLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='fmphysicianlicense']/a")));
    }
    public WebElement documentUploadAndStorage(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='docstorewebapp']/a")));
    }
    public WebElement paymentMenu(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='payment']/span")));
    }
    public WebElement clientPayments(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='clientpayments']/a")));
    }
    public WebElement expectPriceDiscrepancies(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='paymentexpectpricedescrep']/a")));
    }
    public WebElement nonClientPayments(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='paymentnonclientpayments']/a")));
    }
    public WebElement patientPaymentsLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='paymentpatientpayments']/a")));
    }
    public WebElement pricingConfigurationLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='payorpricingconfig']/a")));
    }
    public WebElement epSummaryLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='epsummary']/a")));
    }
    public WebElement billingRulesLink(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='clientbillingrules']/a")));
    }
    public WebElement batchSingleDemandStatement(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='batchsingledemandstmt']/a")));
    }
    public WebElement feeSchedule(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='fmfeeschedule']/a")));
    }
    public WebElement incrementalPriceTable(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='incrpricingtable']/a")));
    }
    public WebElement specialPriceTable(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='fmspecialpricetable']/a")));
    }
    public WebElement logoConfiguration(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='fmlogoconfig']/a")));
    }
    public WebElement paymentSearch(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='paymentsearch']/a")));
    }
    public WebElement procedureCodeRules(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='consolidationrules']/a")));
    }
    public WebElement eomSchedule(){
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='eomschedule']/a")));
    }
}
