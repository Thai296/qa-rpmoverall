package com.overall.electronicpayment;

import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.testng.Assert.assertTrue;

public class ClientTransactionDetailElectronicPayment {
    private RemoteWebDriver driver;
    protected Logger logger;
    private String accnId;
    private DaoManagerPlatform daoManagerPlatform;
    private DaoManagerPatientPortal daoManagerPatientPortal;
    private SeleniumBaseTest b;
    
    public ClientTransactionDetailElectronicPayment(RemoteWebDriver driver)
    {
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }
	
    //****** Payment Information screen ******//
	public WebElement paymentAmountInput(){
		return driver.findElement(By.id("amount"));
	}
	
	public WebElement commentsInput(){
		return driver.findElement(By.id("notes"));
	}
	
	public WebElement printOnClientStatementChkBox(){
		return driver.findElement(By.id("print"));
	}
	
	public WebElement continueBtn(){
		//return driver.findElement(By.id("sData"));
		return driver.findElement(By.xpath("//*[@id='Act_Buttons']/td[2]/button"));
	}
	
	public WebElement cancelBtnInPayment(){
		return driver.findElement(By.id("cData"));
	}
	
   public void setPaymentAmount(String amount) throws Exception
    {
	   paymentAmountInput().clear();
	   paymentAmountInput().sendKeys(amount);
	   paymentAmountInput().sendKeys(Keys.TAB);
       logger.info("       Entered Payment Amount " + amount);
    }
   
   public void setComments(String comment) throws Exception
   {
	   commentsInput().clear();
	   commentsInput().sendKeys(comment);
	   commentsInput().sendKeys(Keys.TAB);
	   logger.info("       Entered Comments " + comment);
   }
   
   public void setPrintOnClientStatement() throws Exception
   {
	   printOnClientStatementChkBox().click();
	   logger.info("       Checked on 'Print On client statement' Checkbox.");
   }
   
   public void clickContinue() throws Exception
   {
	   continueBtn().click();
	   logger.info("       Clicked Continue Button.");
   }
   
   public void clickCancelInPayment() throws Exception
   {
	   cancelBtnInPayment().click();
	   logger.info("       Clicked Cancel Button.");
   }
   
   
   //****** Cardholder Information screen ******//
	public WebElement cardNumberInput(){
		return driver.findElement(By.id("cardNumber"));
	}
	
	public WebElement monthDropdown(){
		return driver.findElement(By.id("expiryMonth"));
	}
	
	public WebElement yearDropdown(){
		return driver.findElement(By.id("expiryYear"));
	}
	
	public WebElement securityCodeInput(){
		return driver.findElement(By.id("cvv"));
	}
	
	public WebElement firstNameInput(){
		return driver.findElement(By.id("billingFirstName"));
	}
	
	public WebElement lastNameInput(){
		return driver.findElement(By.id("billingLastName"));
	}
	
	public WebElement streetAddressInput(){
		return driver.findElement(By.id("billingAddressLine"));
	}
	
	public WebElement zipCodeInput(){
		return driver.findElement(By.id("billingPostalCode"));
	}	
	
	public WebElement payBtn(){
		return driver.findElement(By.id("submitToConfirm"));
	}
	
	public WebElement ptmBoxiFrame(){
		return driver.findElement(By.xpath(".//*[@id='pmtbox']/iframe"));
	}
	
   public WebElement cancelBtnInCardholder() throws Exception {
	   return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\"]')[1]");
    }
   
   public WebElement backBtn() throws Exception
   {      
	   return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\"]')[0]");
   }
	
   public void setCardNumber(String cardNumber) throws Exception
    {
	   cardNumberInput().clear();
	   cardNumberInput().sendKeys(cardNumber);
	   cardNumberInput().sendKeys(Keys.TAB);
       logger.info("       Entered Card Number " + cardNumber);
    }
   
   public void setMonth(String monthNumber) throws Exception
   {
	   SeleniumBaseTest b = new SeleniumBaseTest();
	   b.selectItemByVal(monthDropdown(), monthNumber);
	   logger.info("       Selected Month.");
   }
   
   public void setYear(String yearNumber) throws Exception
   {
	   SeleniumBaseTest b = new SeleniumBaseTest();
	   b.selectItemByVal(yearDropdown(), yearNumber);
	   logger.info("       Selected Year.");
   }
   
   public void setSecurityCode(String securityCode) {
	   securityCodeInput().clear();
	   securityCodeInput().sendKeys(securityCode);
	   securityCodeInput().sendKeys(Keys.TAB);
       logger.info("       Entered Security Code " + securityCode);
   }
   
   public void setFirstName(String firstName) {
	   firstNameInput().clear();
	   firstNameInput().sendKeys(firstName);
	   firstNameInput().sendKeys(Keys.TAB);
       logger.info("       Entered First Name " + firstName);
   }
   
   public void setLastName(String lastName) {
	   lastNameInput().clear();
	   lastNameInput().sendKeys(lastName);
	   lastNameInput().sendKeys(Keys.TAB);
       logger.info("       Entered Last Name " + lastName);
   }
   
   public void setStreetAddress(String streetAddress) {
	   streetAddressInput().clear();
	   streetAddressInput().sendKeys(streetAddress);
	   streetAddressInput().sendKeys(Keys.TAB);
       logger.info("       Entered Street Address " + streetAddress);
   }
   
   public void setZipCode(String zipCode) {
	   zipCodeInput().clear();
	   zipCodeInput().sendKeys(zipCode);
	   zipCodeInput().sendKeys(Keys.TAB);
       logger.info("       Entered Zip Code " + zipCode);
   }
   
   public void clickPay() {
	   payBtn().click();
	   logger.info("       Clicked Pay Button.");
   }
   
   public void clickCancelInCardholder() throws Exception
   {
	   cancelBtnInCardholder().click();
	   logger.info("       Clicked Cancel Button.");
   }
   
   public void clickBack() throws Exception
   {
	   backBtn().click();
	   logger.info("       Clicked Back Button.");
   }  
   
   //****** Confirmation screen ******//
   
	public WebElement confirmBtn(){
		return driver.findElement(By.id("submit"));
	}
	
	public WebElement cancelBtnInConfirmation(){
		return driver.findElement(By.id("modalCancel"));
	} 
	
	public WebElement confirmPaymentInformationTitle(){
		return driver.findElement(By.id("myModalLabel"));
	}
	
	public WebElement confirmPaymentInformationTxt(){
		return driver.findElement(By.xpath("//*[@id='confirmPaymentInfoModal']//div[@ng-show='isCreditCardPayment()']"));
	}
	
	//****** Transaction Status screen ******//
   public WebElement okBtn() throws Exception
   {
       //return driver.findElement(By.xpath("/html/body/div[12]/div[3]/div/button/span"));
	   return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"ui-button-text\"]')[4]");
   }
   
   public WebElement TransactionId() throws Exception
   {       
	   return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"unit size1of2 lastUnit transactionId\"]')[0]");
   }
   
   public void clickConfirm() throws Exception
   {
	   confirmBtn().click();
	   logger.info("       Clicked Confirm Button.");
   }
   
   public void clickCancelInConfirmation() throws Exception
   {
	   cancelBtnInConfirmation().click();
	   logger.info("       Clicked Cancel Button.");
   }
   
   public void clickOk() throws Exception
   {
	   okBtn().click();
	   logger.info("       Clicked OK Button.");
   }
   
   public void setElectronicPayment(SeleniumBaseTest b, String cardNumber, String expMonth, String expYr, String securityCode, String pmtAmount, String comment, WebDriverWait wait) throws Exception{
	   logger.info("*** Actions: - Enter Payment Amount in Electronic Payment Details page and click Continue button");
	   wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("FrmGrid_tbl_clientPayment"))));
	   setPaymentAmount(pmtAmount);
	   setComments(comment);
	   clickContinue();
	   
	   //driver.switchTo().frame(0);
	   b.switchToFrame(ptmBoxiFrame());
	   logger.info("*** Actions: - Enter Card Number, Expiration Date and click Pay button in Electronic Payment Details & Cardholder Information page");
	   wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("cardNumber"))));
	   setCardNumber(cardNumber);
	   setMonth(expMonth);
	   setYear(expYr);
	   setSecurityCode(securityCode);	  
	   wait.until(ExpectedConditions.elementToBeClickable(payBtn()));
	   clickPay();	   	   
	   
	   logger.info("*** Actions: - Click Confirm button in Confirm Payment Information popup");	   
	   assertTrue(b.isElementPresent(confirmPaymentInformationTitle(),5),"       Confirm Payment Information popup is displayed.");	 
	   assertTrue(b.isElementPresent(confirmPaymentInformationTxt(),5),"       Confirm Payment Information CardNumber is displayed.");
	   String hiddenCardNumber = "***** "+cardNumber.substring(15,cardNumber.length());
	   assertTrue(confirmPaymentInformationTxt().getText().contains(hiddenCardNumber),"       Card Number is hidden except the last 4 digits.");	  
	   b.clickHiddenPageObject(confirmBtn(), 0);
	   
	   b.switchToPopupWin();
	   logger.info("*** Actions: - Click OK button in Transaction Status popup");
	   wait.until(ExpectedConditions.visibilityOf(TransactionId()));
	   wait.until(ExpectedConditions.elementToBeClickable(okBtn()));	 
	   clickOk();
   }
   
   public void cancelElectronicPayment(SeleniumBaseTest b, String cardNumber, String expMonth, String expYr, String pmtAmount, WebDriverWait wait, String securityCode, String firstName, String lastName, String streetAddress, String zipCode) throws Exception{
	   logger.info("*** Actions: - Enter Payment Amount in Electronic Payment Details page and click Continue button");	   
	   wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("FrmGrid_tbl_clientPayment"))));
	   setPaymentAmount(pmtAmount);	   
	   clickContinue();	   
	  
	   b.switchToFrame(ptmBoxiFrame());
	   logger.info("*** Actions: - Enter Card Number, Expiration Date and click Pay button in Electronic Payment Details & Cardholder Information page");
	   wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("cardNumber"))));
	   setCardNumber(cardNumber);
	   setMonth(expMonth);
	   setYear(expYr);
	   setSecurityCode(securityCode);
	   setFirstName(firstName);
	   setLastName(lastName);
	   setStreetAddress(streetAddress);
	   setZipCode(zipCode);
	   wait.until(ExpectedConditions.elementToBeClickable(payBtn()));

	   clickPay();	   
	   
	   logger.info("*** Actions: - Click Cancel button in Confirm Payment Information popup");	   	
	   assertTrue(b.isElementPresent(confirmPaymentInformationTitle(),5),"       Confirm Payment Information popup is displayed.");	
	   wait.until(ExpectedConditions.elementToBeClickable(cancelBtnInConfirmation()));	  
	   b.clickHiddenPageObject(cancelBtnInConfirmation(), 0);
	   
	   logger.info("*** Actions: - Click Cancel button in Electronic Payment Details & Cardholder Information page");
	   driver.switchTo().defaultContent();
	   b.clickHiddenPageObject(cancelBtnInCardholder(), 0);	   
   }


}
