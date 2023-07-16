package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClientSearch {
	private WebDriverWait wait;
	protected Logger logger;

	public ClientSearch(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*Client Search*/
	public WebElement clientSearchTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientSearch']/section/header/span")));
	}
	
	public WebElement clientSearchLookupID() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='lookupClientAbbrev']")));
	}
	
	public WebElement clientSearchIconBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='client_abbrev_search_btn']")));
	}
	
	public WebElement clientSearchCloseButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientSearch']/div[2]/button[3]")));
	}
	
	public WebElement clientSearchResetButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientSearch']/div[2]/button[2]")));
	}
	
	public WebElement clientSearchSearchButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientSearch']/div[2]/button[1]")));
	}
	
	public WebElement clientSearchHelpLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pageHelpLink']")));
	}
	
	public void clickClientSearchIconBtn() throws Exception {
		clientSearchIconBtn().click();
		Thread.sleep(1000);
		logger.info("        Click on client Search Icon button");
	}
	
	public void clickClientSearchCloseButton() throws Exception {
		clientSearchCloseButton().click();
		logger.info("        Click on client search Close button");
	}
	
	public void clickClientSearchResetButton() throws Exception {
		clientSearchResetButton().click();
		logger.info("        Click on client search Reset button");
	}
	
	public void clickClientSearchSearchButton() throws Exception {
		clientSearchSearchButton().click();
		Thread.sleep(2000);
		logger.info("        Click on client search Search button");
	}
	
	public void clickClientSearchHelpLink() throws Exception {
		clientSearchHelpLink().click();
		logger.info("        Click on client search Help Link");
	}
	
	public void inputClientSearchLookupID(String value) throws Exception {
		clientSearchLookupID().sendKeys(value);
		clientSearchLookupID().sendKeys(Keys.TAB);
		logger.info("        Entered Client ID: " + value);
	}

	/*Client Info*/
	public WebElement clientIDInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientAbbrev']")));
	}										
	
	public WebElement clientNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientName']")));
	}										
	
	public WebElement npiInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npi']")));
	}
	
	public WebElement npiSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='npi_srch']")));
	}
	
	public WebElement primaryFacilityDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientPrimaryFacility']")));
	}
	
	public void inputClientID(String value) {
		clientIDInput().sendKeys(value);
		clientIDInput().sendKeys(Keys.TAB);
		logger.info("        Entered Client ID: " + value);
	}
	
	public void inputClientName(String value) {
		clientNameInput().sendKeys(value);
		logger.info("        Entered Client Name: " + value);
	}
	
	public void selectPrimaryFacilityDropdown(String value)  {
		Select dropdown = new Select(primaryFacilityDropdown());
		dropdown.selectByValue(value);
		logger.info("        Selected "+value+" in Primary Facility dropdown list");	
	}
	
	/*Address Info*/
	public WebElement clientSearchAddressInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='address']")));
	}
	
	public WebElement clientSearchCityInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='city']")));
	}
	
	public WebElement clientSearchStateDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='stateDroplist']")));
	}
	
	public WebElement clientSearchPostalCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='zip']")));
	}
	
	public WebElement clientSearchCountryDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='countryDroplist']")));
	}
	
	public void inputClientSearchAddress(String value){
		clientSearchAddressInput().sendKeys(value);
		logger.info("        Input "+value+" to client search Address");
	}
	
	public WebElement clientSearchAddress2Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='address2']")));
	}
	
	public void inputClientSearchAddress2(String value){
		clientSearchAddress2Input().sendKeys(value);
		logger.info("        Input "+value+" to client search Address2");
	}
	
	public void inputClientSearchCity(String value){
		clientSearchCityInput().sendKeys(value);
		logger.info("        Input "+value+" to client search City");
	}
	
	public void inputClientSearchPostalCode(String value){
		clientSearchPostalCodeInput().sendKeys(value);
		logger.info("        Input "+value+" to client search Postal Code");
	}
	
	public void selectClientSearchCountryDropdown(String value) throws Exception{
		Select dropdown = new Select(clientSearchCountryDropdown());
		dropdown.selectByVisibleText(value);
		Thread.sleep(1000);
		logger.info("        Select "+value+" in Country dropdown list");
	}
	
	public void selectClientSearchStateDropdown(String value) throws Exception{
		Select dropdown = new Select(clientSearchStateDropdown());
		dropdown.selectByVisibleText(value);
		Thread.sleep(1000);
		logger.info("        Select "+value+" in State dropdown list");
	}
	
	/*Cross-Reference Info */
	public WebElement clientSearchEffectiveDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='effectiveDate']")));
	}
	
	public WebElement clientSearchXRefTypeDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='xrefId']")));
	}
	
	public WebElement clientSearchXRefMemberDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='xrefMemberDroplist']")));
	}
	
	public WebElement clientSearchDisplayXrefDataCheckbox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='dispXrefData1']")));
	}
		
	public void inputClientSearchEffectiveDate(int MM, int DD, int YYYY) {
		String value = "Time: " + MM + "/" + DD + "/" + YYYY;
		clientSearchEffectiveDateInput().sendKeys(MM + "/" + DD + "/" + YYYY);
		logger.info("        Input " + value + " to client search Effective Date.");
	}
	
	public void inputClientSearchEffectiveDateByString(String date) {
		clientSearchEffectiveDateInput().sendKeys(date);
		logger.info("        Input " + date + " to client search Effective Date.");
	}
	
	public void selectClientSearchXRefTypeDropdown(String value) throws Exception {
		Select dropdown = new Select(clientSearchXRefTypeDropdown());
		dropdown.selectByVisibleText(value);
		Thread.sleep(1000);
		logger.info("        Select "+value+" in Xref Type dropdown list");
	}
	
	public void selectClientSearchXRefMemberDropdown(String value) throws Exception {
		Select dropdown = new Select(clientSearchXRefMemberDropdown());
		dropdown.selectByVisibleText(value);
		Thread.sleep(1000);
		logger.info("        Select "+value+" in Xref Member dropdown list");
	}
	
	public void clickClientSearchDisplayXrefDataCheckbox() {
		clientSearchDisplayXrefDataCheckbox().click();
		logger.info("        Click on client search Display Xref Data checkbox");
	}
	
	//Validation error
	public WebElement errorMessageClientSearchTitle(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_message0']/div[1]/div")));
	}

	public WebElement errorMessageClientSearchContent(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_message0']/div[2]")));
	}
	
	public WebElement errorMessageClientSearchCloseIcon(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_message0']/div[1]/a")));
	}
	
	public void clickErrorMessageClientSearchCloseIcon() {
		errorMessageClientSearchCloseIcon().click();
		logger.info("        Click on Close icon of Validation Error popup");
	}
	
}
