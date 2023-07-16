package com.overall.accession.accessionProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RecordPopUp {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;
	
	public RecordPopUp(RemoteWebDriver driver,WebDriverWait wait){
		this.driver = driver;
		this.wait=wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement OKBtn() throws Exception {
		return driver.findElement(By.id("sData"));
	}
	
	public WebElement contactInfoTextArea() throws Exception {
		return driver.findElement(By.id("contactInfo")); 
	}
	
	public WebElement createNewPatientEPIDlg() throws Exception {
		return driver.findElement(By.id("dlgCreateNewPatient")); 
	}
	
	public WebElement OKBtnInCreateNewPatientEPIDlg() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$(':button[class*=\"btn_submit\"][role=button]').click()[0]"); 
	}
	
	public WebElement dxCodeInput() throws Exception {
		//return driver.findElement(By.id("s2id_autogen23")); 
		//return driver.findElement(By.id("s2id_autogen15")); 
		return (WebElement) ((JavascriptExecutor) driver).executeScript(" return $('#s2id_dxCode [id*=\"s2id_autogen\"]')[0]"); 		
	}
	
	public WebElement clientContactInput() throws Exception {
		return driver.findElement(By.id("clientContact")); 
	}
	
	public WebElement newPayorInput() throws Exception {
		return driver.findElement(By.id("userAddedPayor")); 
	}
	
	public WebElement addBtnInAddPayorIDlg() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$(':button[class*=\"btn_submit\"][role=button]').click()[0]");
	}
	
	public WebElement patientDemoUpdateDlg() throws Exception {
		return driver.findElement(By.id("dlgPatientDemoUpdate")); 
	}
	
	public WebElement OKBtnInPatientDemoUpdateDlg() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$(':button[class*=\"btn_submit\"][role=button]').click()[0]"); 
	}
	
	public WebElement updatePtDemoInfoRadioBtn() throws Exception {
		return driver.findElement(By.id("updateInfo"));
	}
	
	public WebElement createNewPtDemoInfoRadioBtn() throws Exception {
		return driver.findElement(By.id("createNewPatientInfo"));
	}
	
	public WebElement occurrenceCodeDropdown() throws Exception {
		return driver.findElement(By.id("occurrenceCode")); 
	}
	
	public WebElement occurrenceDateInput() throws Exception {
		return driver.findElement(By.id("occurrenceDate")); 
	}
	
	public WebElement occurrenceCodePyrIdInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#TblGrid_tbl_occurrenceCodes #payorAbbrev')[0]"); 
	}
	
	public WebElement valueCodeDropdown() throws Exception {
		return driver.findElement(By.id("valueCode")); 
	}
	
	public WebElement valueAmountInput() throws Exception {
		return driver.findElement(By.id("valueAmount")); 
	}
	
	public WebElement valueCodePyrIdInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#TblGrid_tbl_valueCodes #payorAbbrev')[0]"); 
	}
	
	public WebElement renalDropDown() throws Exception {
		return driver.findElement(By.id("renal")); 
	}
	
	public WebElement mod4DropDown() throws Exception {
		return driver.findElement(By.id("mod4")); 
	}
	
	public WebElement adjCodeDropDown() throws Exception {
		return driver.findElement(By.id("adjCode")); 
	}
	
	public WebElement adjAmountInput() throws Exception {
		return driver.findElement(By.id("adjAmount")); 
	}
	
	public WebElement adjCommentInput() throws Exception {
		return driver.findElement(By.id("adjComment")); 
	}
	
	public String newBalanceText() throws Exception {				
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#TblGrid_tbl_billableProcedureCodeDetails [class=\"updatedBalanceInfo\"] [class*=\"newBalance\"]').text()");		
	}
	
	public WebElement postNowBtn() throws Exception {
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("pdata")));
	}
	
	public WebElement overrideDropDown() throws Exception {
		return driver.findElement(By.id("override")); 
	}
	
	public WebElement addOverridePyrIdDropDown() throws Exception {
		return driver.findElement(By.id("payorAbbrev")); 
	}
	
	public WebElement pyrIdDropDown() throws Exception {
		return driver.findElement(By.id("payorAbbrev"));
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setAddOverridePyrId(String str) throws Exception{
		Select oSelection = new Select(addOverridePyrIdDropDown());
		oSelection.selectByValue(str);			
		logger.info("        Selected Over-ride Payor ID: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setOverride(String str) throws Exception{
		Select oSelection = new Select(overrideDropDown());
		oSelection.selectByValue(str);			
		logger.info("        Selected Over-ride: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setAdjAmount(String str) throws Exception{
		adjAmountInput().sendKeys(str);		
		adjCommentInput().click();
		//adjAmountInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		logger.info("        Entered Adj Amount: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setAdjCode(String str) throws Exception{
		Select oSelection = new Select(adjCodeDropDown());
		oSelection.selectByValue(str);			
		logger.info("        Selected Adj Code: " + str);
	}
	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setMod4(String str) throws Exception{
		Select oSelection = new Select(mod4DropDown());
		oSelection.selectByValue(str);			
		logger.info("        Selected Mod 4: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setRenalFlag(String str) throws Exception{
		Select oSelection = new Select(renalDropDown());
		oSelection.selectByValue(str);			
		logger.info("        Selected Renal: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setOccurrenceCode(String selectStr) throws Exception{
		Select oSelection = new Select(occurrenceCodeDropdown());
		oSelection.selectByValue(selectStr);	
		Thread.sleep(1000);
		logger.info("        Selected Occurrence Code: " + selectStr);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setValueCode(String selectStr) throws Exception{
		Select oSelection = new Select(valueCodeDropdown());
		oSelection.selectByValue(selectStr);	
		Thread.sleep(1000);
		logger.info("        Selected Value Code: " + selectStr);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setValueAmount(String str) throws Exception{
		valueAmountInput().sendKeys(str);		
		valueAmountInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Value Amount: " + str);
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setOccurrenceDate(String str) throws Exception{
		occurrenceDateInput().clear();
		occurrenceDateInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Cleared Occurrence Date.");
		occurrenceDateInput().sendKeys(str);
		occurrenceDateInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Occurrence Date: " + str);
	}		
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clickOK() throws Exception{
		OKBtn().click();
		Thread.sleep(1000);
		logger.info("        Clicked the OK button.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clickOKInCreateNewPatientEPIDlg() throws Exception{
		OKBtnInCreateNewPatientEPIDlg();
		Thread.sleep(1000);
		logger.info("        Clicked the OK button in Create A New Patient EPI popup window.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clickOKInPatientDemoUpdateDlg() throws Exception{
		OKBtnInPatientDemoUpdateDlg();
		Thread.sleep(1000);
		logger.info("        Clicked the OK button in Patient Demographics Update popup window.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setContactInfo(String str) throws Exception{
		contactInfoTextArea().sendKeys(str);
		contactInfoTextArea().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Contact Info: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setDxCode(String str) throws Exception{
		dxCodeInput().sendKeys(str);
		dxCodeInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Dx Code: " + str);
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setClientContact(String str) throws Exception{
		clientContactInput().sendKeys(str);
		clientContactInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Client Contact: " + str);
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setNewPayor(String str) throws Exception{
		newPayorInput().sendKeys(str);
		newPayorInput().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("        Entered New Payor: " + str);
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setUpdatePtDemo() throws Exception{
		updatePtDemoInfoRadioBtn().click();
		Thread.sleep(1000);
		logger.info("        Selected to update the patient insurance information.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setPtDemoUpdate() throws Exception{
		createNewPtDemoInfoRadioBtn().click();
		Thread.sleep(1000);
		logger.info("        Selected to create new patient insurance information.");
	}

}
