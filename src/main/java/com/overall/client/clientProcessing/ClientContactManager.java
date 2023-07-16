package com.overall.client.clientProcessing;

import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClientContactManager {
	
	private final RemoteWebDriver driver;
	protected final Logger logger;
	private final WebDriverWait wait;

	public ClientContactManager(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		this.wait=wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	/*----------WEB ELEMENT GENERAL----------*/
	public WebElement tableContactManger() throws Exception {
		return driver.findElement(By.id("cntctHdrTable"));			
	}
	public WebElement clientLink() throws Exception {
		return driver.findElementByLinkText("Client");
	}	
	public WebElement submitBtn() throws Exception{
		return driver.findElement(By.id("btn_submit"));
	}
	public WebElement resetBtn() throws Exception{
		return driver.findElement(By.id("btn_reset"));				
	}
	public WebElement loadClientSectionClientIdInput() {
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("lookupClientId")));
	}
	
	/*---------WEB ELEMENT CREATE NEW CLIENT----------*/
	public WebElement clientIdInput(){
		return driver.findElement(By.id("clnAbbrev"));
	}
	public WebElement clientNameInput(){
		return driver.findElement(By.id("bilAcctNm"));
	}
	public WebElement startDateInput(){
		return driver.findElement(By.id("startDate"));
	}
	public WebElement taxIdInput(){
		return driver.findElement(By.id("clTaxId"));
	}
	public WebElement primaryFacility(){
		return driver.findElement(By.id("prmryFacId"));
	}
	public WebElement selectAccountTyp(){
		return driver.findElement(By.id("accntTypId"));
	}
	public WebElement bilAddInput(){
		return driver.findElement(By.id("bilAddr1"));
	}
	public WebElement bilContactNameinput(){
		return driver.findElement(By.id("bilCntct"));
	}
	public WebElement bilAdd2Input(){
		return driver.findElement(By.id("bilAddr2"));
	}
	public WebElement bilPhoneInput(){
		return driver.findElement(By.id("bilPhone1"));
	}
	public WebElement bilZipInput(){
		return driver.findElement(By.id("bilZip"));
	}
	public WebElement bilEmailInput(){
		return driver.findElement(By.id("bilEmail"));
	}
	public WebElement bil2NameInput(){
		return driver.findElement(By.id("bil2Cntct"));
	}
	public WebElement streetPhoneInput(){
		return driver.findElement(By.id("lbPhone1"));
	}
	public WebElement correspAddInput(){
		return driver.findElement(By.id("corAddr1"));
	}
	public WebElement correspZipInput(){
		return driver.findElement(By.id("corZip"));
	}
	public WebElement correspPhoneInput(){
		return driver.findElement(By.id("corPhone1"));
	}
	
	/*----------METHOD CREATE NEW CLIENT----------*/
	
	public void clickResetBtn() throws Exception{
		resetBtn().click();
		logger.info("         Click on Reset Button");
	}
	public void inputClientId(String clientId) throws Exception{
		clientIdInput().sendKeys(clientId);
		clientIdInput().sendKeys(Keys.TAB);
		logger.info("        enter client id: " + clientId);
	}
	public void inputClientName(String clientName) throws Exception{
		clientNameInput().sendKeys(clientName);
		logger.info("        enter client name : " + clientName);
	}
	public void selectaccountTyp(SeleniumBaseTest b, String value) throws Exception{
		b.selectItem(selectAccountTyp(), value);
		logger.info("        select account Type: " + value);
	}
	public void inputStartDate(String startDate) throws Exception{
		startDateInput().sendKeys(startDate);
		logger.info("        Input start date: " + startDate);
	}
	public void selectPrimartyFacility(SeleniumBaseTest b, String value) throws Exception{
		b.selectItem(primaryFacility(), value);
		logger.info("        Select primary facility:" + value);
	}
	public void inputBilAddInput(String bilAdd) throws Exception{
		bilAddInput().sendKeys(bilAdd);
		logger.info("        Input Billing address: " + bilAdd);
	}
	public void inputBilContactName(String bilContNam) throws Exception{
		bilContactNameinput().sendKeys(bilContNam);
		logger.info("        Input Billing contact name :" + bilContNam);
	}
	public void inputBilAdd2(String bildAdd2) throws Exception{
		bilAdd2Input().sendKeys(bildAdd2);
		logger.info("        Input Billing Address 2: " + bildAdd2);
	}
	public void inputBilZip(String bilZip) throws Exception{
		bilZipInput().sendKeys(bilZip);
		logger.info("        Input Billing zip: " + bilZip);
	}
	public void inputBilPhon(String bilPhon) throws Exception{
		bilPhoneInput().sendKeys(bilPhon);
		logger.info("        Input Billing phone: " + bilPhon);
	}
	public void inputBilEmail(String bilConEmail) throws Exception{
		bilEmailInput().sendKeys(bilConEmail);
		logger.info("        Input Billing contact email: "+ bilConEmail);
	}
	public void inputBilContName2(String bilCntName2) throws Exception{
		bilContactNameinput().sendKeys(bilCntName2);
	}
	public void inputStreetPhon(String streetPhon) throws Exception{
		streetPhoneInput().sendKeys(streetPhon);
		logger.info("        Input Street Address Phone: " + streetPhon);
	}
	public void inputCorrespAdd(String correspAdd) throws Exception{
		correspAddInput().sendKeys(correspAdd);
		logger.info("        Input Corresp Address:" + correspAdd);
	}
	public void inputCorrespZip(String correspZip) throws Exception{
		correspZipInput().sendKeys(correspZip);
		logger.info("        Input Corresp Zip: " + correspZip);
	}
	public void inputCorresPhon(String correspPhon) throws Exception{
		correspPhoneInput().sendKeys(correspPhon);
		logger.info("        Input Corresp phone: " + correspPhon);
	}
	public void createClient(SeleniumBaseTest b, String clientId, String clientName, String accountTyp, String startDate, String primaryFacility, String bilAdd, String bilZip, String bilPhone, String streetPhone, String correspAdd, String correspZip, String correspPhone) throws Exception{
		inputClientId(clientId);		
		inputClientName(clientName);
		selectaccountTyp(b, accountTyp);
		inputStartDate(startDate);
		selectPrimartyFacility(b, primaryFacility);
		inputBilAddInput(bilAdd);
		inputBilZip(bilZip);
		inputBilPhon(bilPhone);
		inputStreetPhon(streetPhone);
		inputCorrespAdd(correspAdd);
		inputCorrespZip(correspZip);
		inputCorresPhon(correspPhone);
		
		// Click on Submit btn
		submitBtn().click();
		logger.info("         New Client Id is create with client Id is: "+clientId+"");
	}
	public void updateClient(SeleniumBaseTest b, String clientName, String bilAdd, String correspAdd) throws Exception{
		inputClientName(clientName);
		inputBilAddInput(bilAdd);
		inputCorrespAdd(correspAdd);
		//Click on Submit btn
		submitBtn().click();
		logger.info("        Update Client ID with clientId is : ");
	}
	public void updateClientContact(SeleniumBaseTest b, String bilAdd1, String bilAdd2, String bilContactName, String bilEmail, String bilName2) throws Exception{
		inputBilAddInput(bilAdd1);
		inputBilAdd2(bilAdd2);
		inputBilContactName(bilContactName);
		inputBilEmail(bilEmail);
		inputBilContName2(bilName2);
		//Click on Submit btn
		submitBtn().click();
		logger.info("       Update Client Contact with Client Id is:");
	}
}
