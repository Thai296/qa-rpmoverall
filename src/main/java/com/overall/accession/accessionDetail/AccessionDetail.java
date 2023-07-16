package com.overall.accession.accessionDetail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.utils.SeleniumBaseTest;

public class AccessionDetail {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private SeleniumBaseTest b;
	private WebDriverWait wait;
	
	public AccessionDetail(RemoteWebDriver driver){
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement headerDetailPageTitleTxt(){
		return driver.findElementByXPath("//div[contains(@class,'step') and not(contains(@style,'display: none'))]//div[contains(@class,'accnDetailGroup')]//span[@class='platormPageTitle']");
	}
	
	public WebElement titlePageTitleTxt(){
		return driver.findElementByClassName("platormPageTitle");
	}
	
	public WebElement headerLoadPageTitleTxt(){
		return driver.findElementByCssSelector("div[class*='step1'] span[class='platormPageTitle']");
	}
	
	public WebElement epiTxt() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span[data-mirror='patientId']"))); 
	}
	
	public WebElement accnIdInput() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupAccnId")));
	}
	
	public WebElement primaryPyrSubsIdInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#payor_tab_A #subscriberId')[0]");
	}	
	
	public WebElement containerAccnId() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'step2')]")));
	} 
	
	public WebElement accnIdText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("accnId")));
	}

	public WebElement dosText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/section/div/div/div[2]/div[1]/div/div[2]/div[3]/div[1]/div[2]/span")));
	}
	
	public WebElement dobText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/section/div/div/div[2]/div[1]/div/div[2]/div[2]/div[3]/div[2]/span")));
	}	
	
	public WebElement ptFullNameText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[@data-mirror='patientFullName']")));
	}
	
	public WebElement clientIdText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientAbbrev")));
	}
	
	public WebElement viewDocumentHeaderInfoText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("viewDocumentHeaderInfo")));
	}
	
	public WebElement accessionStatusText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/section/div/div/div[2]/div[1]/div/div[2]/div[4]/div[1]/div[2]/span")));
	}
	
	public WebElement rePriceCheckbox() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("reprice")));
	}
	
	public WebElement accessionErrorsGridText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#sectionErrors > section > header > div:nth-child(1) > span > a")));
	}	

	public WebElement showAllUnfixedErrRadioBtn() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("radioUnFixedError")));
	}
	
	public WebElement currentAccnErrTable() throws Exception {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_accnCurrentErrors"))); 
	}	
	
	public WebElement fixedAccnErrTable() throws Exception {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_accnFixedErrors"))); 
	}	
	
	public WebElement contactDetailTable() throws Exception {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_contactDetail"))); 
	}
	
	public WebElement contactDetailSummaryTable() throws Exception {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_contactDetail_summary"))); 
	}
	
	//Accession Errors
	public WebElement reasonCodeFilterInput() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_reasonCode"))); 
	}	
	
	public WebElement resetBtn() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset"))); 
	}	
	
	public WebElement saveBtn() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSave"))); 
	}	
	
	public WebElement currAccnErrActionLink(int row) throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#tbl_accnCurrentErrors > tbody > tr:nth-child(" + row + ") > td:nth-child(20) > a')).click()[0]");	
	}	

	public WebElement fixedAccnErrActionLink(int row) throws Exception {	
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#tbl_accnFixedErrors > tbody > tr:nth-child(" + row + ") > td:nth-child(20) > a')).click()[0]");	
	}	
	
	public WebElement createNewEPILink() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('.btnCreateNewPatientId').click()[0]");
	}
	
	public WebElement patientSearchBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$(\"a[title='Patient Search']\").click()[0]");
	}
	
	public WebElement saveInProgressInfoText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messagefor_message0"))); 
	}	
		
	public WebElement accnErrorText(int row, int col) throws Exception {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_accnCurrentErrors']/tbody/tr[" + row + "]/td[" + col + "]")));
	}	
	
	public WebElement fixedAccnErrorText(int row, int col) throws Exception {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_accnFixedErrors']/tbody/tr[" + row + "]/td[" + col + "]")));
	}
	
	public WebElement contactNotesGridText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#sectionNotes > section > header > div:nth-child(1) > span > span")));
	}
	
	public WebElement contactNotesAddBtn() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_contactDetail")));
	}
	
	public WebElement diagnosisDetailAddBtn() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_accnDiagnosisDetail")));
	}
	
	public WebElement contactNotesCurrentView() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#sectionNotes > section > header > div > span > a")));
	}
	
	public WebElement physicianInfoGridText() throws Exception {
		//return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#physicianInfoSection > div > section > header > div:nth-child(1) > span > span")));
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#physicianInfoSection [class=\"titleText\"]')[0]");
	}
	
	public WebElement physicianInfoCurrentView() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#physicianInfoSection > div > section > header > div:nth-child(1) > span > a")));
	}
	
	public WebElement physicianSearchBtn() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#physicianInfoSection > div > section > div > div.detailView > div:nth-child(2) > div.unit.size10.nopad > div > a")));
	}
	
	public WebElement npiInput() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("orderingPhysicianNpi")));
	}
	
	public WebElement npiSummaryViewText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#physicianInfoSection > div > section > div > div.summaryView > div > div:nth-child(4) > span")));
	}
	
	public WebElement upinInput() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("orderingPhysicianUpin")));
	}
	
	public WebElement upinSummaryViewText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#physicianInfoSection > div > section > div > div.summaryView > div > div:nth-child(6) > span")));
	}
	
	public WebElement physNameText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#physicianInfoSection > div > section > div > div.detailView > div:nth-child(2) > div.unit.size20 > span"))); 
	}
	
	public WebElement physNameSummaryViewText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#physicianInfoSection > div > section > div > div.summaryView > div > div.unit.size20 > span"))); 
	}
	
	public WebElement taxonomyCodeText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#physicianInfoSection > div > section > div > div.detailView > div:nth-child(2) > div.unit.lastUnit > span"))); 
	}
	
	public WebElement taxonomyCodeSummaryViewText() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#physicianInfoSection > div > section > div > div.summaryView > div > div.unit.lastUnit > span"))); 
	}
	
	public WebElement epiInput() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("patientId"))); 
	}
	
	public WebElement clientPatientIdInput() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientPatientId"))); 
	}
	
	public WebElement clientPrimaryFacilityPatientIdInput() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientPrimaryFacilityPatientId"))); 
	}
	
	public WebElement currentDiagnosesGridText() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#sectionDiagnosis .titleText').click()[0]");	
	}
	
	public WebElement currentDiagnosesCurrentView() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#sectionDiagnosis > div > section > header > div:nth-child(1) > span > a")));
	}
	
	public WebElement accnDiagnosisDetailTable() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_accnDiagnosisDetail"))); 
	}
	
	public WebElement accnDiagnosisDetailVoiedTable() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_accnDiagnosisDetailVoided"))); 
	}
	
	public WebElement unSavedDiagnosisDetailText(int row, int col) throws Exception {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='jqg" + row + "'" + "]/td[" + col + "]")));
		//return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_accnDiagnosisDetail']/tbody/tr['jqg" + row + "']/td[" + col + "]")));
	}
	
	public WebElement savedDiagnosisDetailText(int row, int col) throws Exception {				
		String str = "//*[@id='tbl_accnDiagnosisDetail']/tbody/tr[" + row + "]/td[" + col + "]";		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(str)));		
	}
	
	public WebElement voidedDiagnosisDetailText(int row, int col) throws Exception {				
		String str = "//*[@id='tbl_accnDiagnosisDetailVoided']/tbody/tr[" + row + "]/td[" + col + "]";			
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(str)));		
	}
	
	public WebElement checkDeleteDiagnosisCheckbox(int row) throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#tbl_accnDiagnosisDetail [class=\"deleteCell usedInline\"] [type=\"checkbox\"]')[" + row + "].click()");	
	}
	
	public WebElement primaryPayorIDInput() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#payor_tab_A #payorAbbrev')[0]");	
	}
	
	public WebElement secondaryPayorIDInput() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#payor_tab_B #payorAbbrev')[0]");	
	}
	
	public WebElement secondaryPayorSubsIdInput() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#payor_tab_B #subscriberId')[0]");	
	}
	
	public WebElement secondaryPayorRelationshipDropdown() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#payor_tab_B #insuredRelationship')[0]");	
	}
	
	public WebElement OKBtnInWarningAboutReorderingPayorsDlg() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$(':button[class*=\"btn_submit\"][role=\"button\"]')[0].click()"); 														 
	}
	
	public WebElement warningAboutReorderingPayorsDlg() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dlgWarningAboutReorderingPayors"))); 
	}
	
	public WebElement addPayorLink() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnAddPayor"))); 
	}
	
	public WebElement occurrenceCodeAddBtn() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_occurrenceCodes"))); 
	}
	
	public WebElement valueCodeAddBtn() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_valueCodes"))); 
	}
	
	public WebElement returnedErrorText() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#sectionServerMessages [class=\"serverErrorsList\"]')[0]");	
	}
	
	public String diagnosisCodeInSummaryView(String titleStr) throws Exception {
		String str = "return $('#sectionDiagnosis [class*=\"diagnosisSummaryItemTemplate\"][title=\"" + titleStr + "\"]').text()";		
		return (String) ((JavascriptExecutor) driver).executeScript(str);		
	}
	
	public WebElement occurrenceCodesTable() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_occurrenceCodes"))); 
	}
	
	public WebElement valueCodesTable() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_valueCodes"))); 
	}
	
	public WebElement occurrenceCodeText(int row, int col) throws Exception {				
		String str = "//*[@id='tbl_occurrenceCodes']/tbody/tr[" + row + "]/td[" + col + "]";			
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(str)));		
	}
	
	public WebElement valueCodeText(int row, int col) throws Exception {				
		String str = "//*[@id='tbl_valueCodes']/tbody/tr[" + row + "]/td[" + col + "]";			
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(str)));		
	}
	
	public WebElement checkOccurrenceCodeDeleteCheckbox(int row) throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#tbl_occurrenceCodes [class=\"deleteCell usedInline\"] [type=\"checkbox\"]')[" + row + "].click()");	
	}
	
	public WebElement checkValueCodeDeleteCheckbox(int row) throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#tbl_valueCodes [class=\"deleteCell usedInline\"] [type=\"checkbox\"]')[" + row + "].click()");	
	}	
	
	public WebElement orderedTestViewDropDown() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#sectionTransactions [class=\"viewController autoWidth select2-offscreen\"]')[0]");	
	}
	
	public WebElement orderedTestDetailTable() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_orderedTestDetail"))); 
	}
	
	public WebElement orderedTestDetailEditBtn() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_orderedTestDetail"))); 
	}
	
	public WebElement orderedTestDetailText(int row, int col) throws Exception {				
		String str = "//*[@id='tbl_orderedTestDetail']/tbody/tr[" + row + "]/td[" + col + "]";			
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(str)));		
	}
	
	public WebElement billableProcCodeDetailsTable() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_billableProcedureCodeDetails"))); 
	}
	
	public WebElement billableProcCodeDetailsText(int row, int col) throws Exception {				
		String str = "//*[@id='tbl_billableProcedureCodeDetails']/tbody/tr[" + row + "]/td[" + col + "]";			
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(str)));		
	}
	
	public WebElement billableProcCodeDetailsEditRowLnk(int row) throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#messagefor_" + row + " a.btnEditRow').click()");	
	}
	
	public String renalColText() throws Exception {
		String str = "return $('#tbl_orderedTestDetail_renal').attr(\"style\")";		
		return (String) ((JavascriptExecutor) driver).executeScript(str);		
	}
	
	public String errorReturnedText() throws Exception {
		String str = "return $('#sectionServerMessages [class=\"serverErrorsTitle\"] [class=\"emphasis\"]').text()";		
		return (String) ((JavascriptExecutor) driver).executeScript(str);		
	}
	
	public WebElement billableProcCodeDetailsAddAdjLnk(int row) throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#messagefor_" + row + " a.btnAddAdjustment').click()");	
	}
	
	public WebElement pyrSummaryTable() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_payorSummary"))); 
	}
	
	public WebElement pyrSummaryText(int row, int col) throws Exception {				
		String str = "//*[@id='tbl_payorSummary']/tbody/tr[" + row + "]/td[" + col + "]";			
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(str)));		
	}
	
	public WebElement adjSummaryTable() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_adjustmentSummary"))); 
	}
	
	public WebElement adjSummaryText(int row, int col) throws Exception {				
		String str = "//*[@id='tbl_adjustmentSummary']/tbody/tr[" + row + "]/td[" + col + "]";			
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(str)));		
	}
	
	public WebElement addOverrideLink() throws Exception {				
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('[class=\"btnAddOverride actionLink\"]').click()");		
	}
	
	public WebElement bulkPmtViewDropDown() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"bulkPaymentsAndDenialsControls\"] [class*=\"viewController autoWidth select2-offscreen\"]')[0]");			
	}
	
	public WebElement bulkPmtAndDenialsTable() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_bulkPaymentsAndDenials"))); 
	}	
	
	public WebElement webTableText(String tableName, int row, int col) throws Exception {							
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='" + tableName + "']/tbody/tr[" + row + "]/td[" + col + "]")));		
	}
	
	public WebElement postalCodeSearchBtn() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('a[title=\"Zip Code Search\"]')[0].click()");			
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setBulkPmtView(String str) throws Exception{
		Select oSelection = new Select(bulkPmtViewDropDown());
		oSelection.selectByValue(str);			
		logger.info("        Selected View: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clickOrderedTestDetailEdit() throws Exception{
		orderedTestDetailEditBtn().click();
		logger.info("        Clicked the Ordered Test Detail Edit button.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setOrderedTestView(String str) throws Exception{
		Select oSelection = new Select(orderedTestViewDropDown());
		oSelection.selectByValue(str);			
		logger.info("        Selected View: " + str);
	}
	
	public void clickAddOccurrenceCode() throws Exception{
		occurrenceCodeAddBtn().click();
		logger.info("        Clicked the Occurrence Codes Add button.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setsecondaryPayorRelationship(String str) throws Exception{
		Select oSelection = new Select(secondaryPayorRelationshipDropdown());
		oSelection.selectByValue(str);			
		logger.info("        Selected Relationship: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setSecondaryPayorSubsId(String str) throws Exception{
		secondaryPayorSubsIdInput().sendKeys(str);
		secondaryPayorSubsIdInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Subscriber ID: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setAccnId(String accnId) throws Exception{
		accnIdInput().sendKeys(accnId);
		accnIdInput().sendKeys(Keys.TAB);
		Thread.sleep(5000);
		logger.info("        Entered AccnID: " + accnId);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setClientPatientId(String id) throws Exception{
		clientPatientIdInput().sendKeys(id);
		clientPatientIdInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Client Patient ID: " + id);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setClientPrimaryFacilityPatientId(String id) throws Exception{
		clientPrimaryFacilityPatientIdInput().sendKeys(id);
		clientPrimaryFacilityPatientIdInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Client's Primary Facility Patient ID: " + id);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setPrimaryPyrId(String pyrId) throws Exception{		
		primaryPayorIDInput().sendKeys(pyrId);
		primaryPyrSubsIdInput().click();
		//primaryPayorIDInput().sendKeys(Keys.TAB);
		Thread.sleep(5000);
		logger.info("        Entered Primary Payor: " + pyrId);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clearPrimaryPyrId() throws Exception{		
		primaryPayorIDInput().clear();
		primaryPayorIDInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		logger.info("        Primary Payor was cleared.");
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clickSave() throws Exception{
		saveBtn().click();
		Thread.sleep(3000);
		logger.info("        Clicked the Save button.");
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clickReset() throws Exception{
		resetBtn().click();
		logger.info("        Clicked the Reset button.");
	}	
	
	public void clickContactNotesAdd() throws Exception{
		contactNotesAddBtn().click();
		logger.info("        Clicked the Contact Notes Add button.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clickDiagnosisDetailAdd() throws Exception{
		diagnosisDetailAddBtn().click();
		logger.info("        Clicked the Diagnosis Detail Add button.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setReprice() throws Exception{
		rePriceCheckbox().click();
		logger.info("        Checked the Reprice checkbox.");
	}		

	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setShowAllUnfixedErr() throws Exception{
		showAllUnfixedErrRadioBtn().click();
		Thread.sleep(1000);
		logger.info("        Checked the Show all unfixed errors radio button.");
	}	

	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setCurrAccnErrAction(int rowNum) throws Exception{
		currAccnErrActionLink(rowNum);
		logger.info("        Clicked the Fix hyperlink at " + rowNum + " row in Current Accession Errors grid.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setFixedAccnErrAction(int rowNum) throws Exception{
		fixedAccnErrActionLink(rowNum);
		logger.info("        Clicked the Unfix hyperlink at " + rowNum + " row in Fixed Accession Errors grid.");
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setAccnErrGrid() throws Exception{
		accessionErrorsGridText().click();		
		Thread.sleep(1000);
		logger.info("        Clicked the Accession Errors grid text.");
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clearPrimaryPyrSubsId() throws Exception{
		primaryPyrSubsIdInput().clear();
		primaryPyrSubsIdInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		logger.info("        Clear the subs id for the Primary Payor.");
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setPrimaryPyrSubsId(String subsId) throws Exception{
		primaryPyrSubsIdInput().sendKeys(subsId);		
		primaryPayorIDInput().click();
		//primaryPyrSubsIdInput().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("        Entered SubsId: " + subsId + " for the Primary Payor.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setContactNotesGrid() throws Exception{
		contactNotesGridText().click();		
		Thread.sleep(1000);
		logger.info("        Clicked the Contact Notes grid text.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setPhysicianInfoGrid() throws Exception{
		physicianInfoGridText().click();		
		Thread.sleep(1000);
		logger.info("        Clicked the Physician Info grid text.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clickPhysicianSearch() throws Exception{
		physicianSearchBtn().click();
		logger.info("        Clicked the Physician Search button.");
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void clickCreateNewEPILink() throws Exception{
		createNewEPILink();
		logger.info("        Clicked the Create new EPI link.");
	}
	
	public void clickPatientSearch() throws Exception{
		patientSearchBtn();
		logger.info("        Clicked the Patient Search button.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setCurrentDiagnosesGrid() throws Exception{
		currentDiagnosesGridText();		
		Thread.sleep(1000);
		logger.info("        Clicked the Current Diagnoses grid.");
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method returns the row number when found a match in the accn error webtable for the passing two columns values
	public int getRowNumber(WebElement tableElement, String colVal1, String colVal2) throws Exception {		
		int rowNum = 0;
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));		
		int total_rows = rows.size();
		logger.info("        Total Row Size " + total_rows);
		
		for (int i=1; i<total_rows + 1; i++){
			//System.out.println("Row Num---" + i + "---");			
			if(tableElement.equals(currentAccnErrTable())){				
				if(accnErrorText(i,3).getText().trim().equals(colVal1) && accnErrorText(i,11).getText().trim().equals(colVal2)) {				
					logger.info("        Match found in " + currentAccnErrTable().getAttribute("id") +  " table at row " + i);
					rowNum = i;
					break;
				}
			} else {				
				if(fixedAccnErrorText(i,3).getText().trim().equals(colVal1) && fixedAccnErrorText(i,11).getText().trim().equals(colVal2)) {				
					logger.info("        Match found in " + fixedAccnErrTable().getAttribute("id") +  " table at row " + i);
					rowNum = i;
					break;
				}
			} 			
		}
		return rowNum;
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//Returns total row counts for a webtable
	public int getTotalRowSize(WebElement tableElement)  throws Exception {		
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));		
		int total_rows = rows.size();
		logger.info("        Total Row Size " + total_rows);
		return total_rows;
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method loops through a table columns (count) times and returns the colIndex when found a match in a WebTable
	public int colIndexInWebTable( WebElement tableElement, String tableName, int colIndex, String colVal, int count) throws Exception {		
		int colIndexInner = 0;
		int totalRowCount = getTotalRowSize(tableElement);		
				
		for (int i=1; i<totalRowCount + 1; i++){
			//System.out.println("Row Num---" + i + "---");				
			for(int j=0;j<count;j++){
				colIndexInner = colIndex - j;
				//System.out.println("Col Num---" + colIndexInner + "---");
				if(webTableText(tableName, i, colIndexInner).getText().equals(colVal)){		
					logger.info("        Match found in " + tableName +  " table at row " + i + " and column " + colIndexInner);
					break;
				}
			}
		}
		return colIndexInner;
	}
		
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This Overloading method returns the row number when found a match in a webtable for the passing ONE column value and index
	public int getRowNumberInWebTable(WebElement tableElement, String tableName, String colVal1, int colIndex1) throws Exception {
		int rowNum = 0;
		int totalRowCount = getTotalRowSize(tableElement);
		
		for (int i=1; i<totalRowCount + 1; i++){
			//System.out.println("Row Num---" + i + "---");					
			if(webTableText(tableName,i,colIndex1).getText().trim().equals(colVal1)) {				
				logger.info("        Match found in " + tableName +  " table at row " + i);
				rowNum = i;
				break;
			}			
		}
		return rowNum;
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This Overloading method returns the row number when found a match in a webtable for the passing TWO columns values and indexes
	public int getRowNumberInWebTable(WebElement tableElement, String tableName, String colVal1, String colVal2, int colIndex1, int colIndex2) throws Exception {
		int rowNum = 0;
		int totalRowCount = getTotalRowSize(tableElement);
		
		for (int i=1; i<totalRowCount + 1; i++){
			//System.out.println("Row Num---" + i + "---");					
			if(webTableText(tableName,i,colIndex1).getText().trim().equals(colVal1) && webTableText(tableName,i,colIndex2).getText().trim().equals(colVal2)) {				
				logger.info("        Match found in " + tableName +  " table at row " + i);
				rowNum = i;
				break;
			}			
		}
		return rowNum;
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This Overloading method returns the row number when found a match in a webtable for the passing THREE columns values and indexes
	public int getRowNumberInWebTable(WebElement tableElement, String tableName, String colVal1, String colVal2, String colVal3, int colIndex1, int colIndex2, int colIndex3) throws Exception {
		int rowNum = 0;
		int totalRowCount = getTotalRowSize(tableElement);
		
		for (int i=1; i<totalRowCount + 1; i++){
			//System.out.println("Row Num---" + i + "---");					
			if(webTableText(tableName,i,colIndex1).getText().trim().equals(colVal1) && 
					webTableText(tableName,i,colIndex2).getText().trim().equals(colVal2) && 
					webTableText(tableName,i,colIndex3).getText().trim().equals(colVal3)) {				
				logger.info("        Match found in " + tableName +  " table at row " + i);
				rowNum = i;
				break;
			}			
		}
		return rowNum;
	}


	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method delete the specified payor/payor fields requirements if there is any
	public void deletePayorReq(int pyrId, int pyrReqFldRowCount, int pyrReqRowCount, String testDb) throws Exception{
		daoManagerPlatform = new DaoManagerPlatform(b.getConfig().getRpmDatabase());
		
		if (pyrReqFldRowCount > 0){
			int i = daoManagerPlatform.deletePyrReqmntsFldFromPYRREQMNTSFLDByPyrId(pyrId, testDb);
			logger.info("        " + i + " rows got deleted in PYR_REQMNTS_FLD.");
		}else{
			logger.info("        No payor requirements found in PYR_REQMNTS_FLD.");
		}
		
		if (pyrReqRowCount > 0){
			int j = daoManagerPlatform.deletePyrReqmntsFromPYRREQMNTSByPyrId(pyrId, testDb);
			logger.info("        " + j + " rows got deleted in PYR_REQMNTS.");
		}else{
			logger.info("        No payor requirements found in PYR_REQMNTS.");
		}	
		
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method verifies that the passed string value exists(not exact match) in the web table
	public boolean isColumnValueExist(WebElement element, String columnVal) {
		String str;
		boolean flag = true;
		boolean found = false;

		//Get row count
		List<WebElement> rows = element.findElements(By.tagName("tr"));		
		logger.info("        Row Count: " + rows.size());

		//Loop through each row
		for (WebElement row : rows) {
			//Get column count
			List<WebElement> cols = row.findElements(By.xpath("td"));			
			logger.info("        Col Count: " + cols.size());

			//Loop through each column
			for (WebElement col : cols) {
				str = col.getText().trim();
				//logger.info(str);
				//Compare column value with the string passed
				if (str.contains(columnVal)) {
					flag = false;
					found = true;
					logger.info("        Matching value found in webtable for: " + columnVal.replaceAll(",",""));
					break;
				}
			}
			if (!flag) {
				break;
			}
		}
		return found;
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method verifies if the Save in progress message is hidden
	//When errors returned, the Save in progress is still on the page but hidden
	public boolean isSaveInProgressHidden(int time) throws Exception{
		boolean flag = false;
		int i = 0;
		while (i < time) {
			if(saveInProgressInfoText().getAttribute("style").contains("none")){
				flag = true;
				logger.info("        Save in progress is not visible.");
				break;
			}else{
				logger.info("        Save in progress is visible.");
			}
			Thread.sleep(1000);
			i++;
		}
		return flag;	
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method checks if there is any error returned
	public boolean isErrorReturned() throws Exception{
		boolean flag = false;	
		
		if(errorReturnedText().equals("0") || errorReturnedText().equals("")){
			logger.info("        No error was returned.");
		}else{
			flag = true;
			logger.info("        " + errorReturnedText() + " errors were returned.");		
		}
		return flag;	
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method checks if the Save is done 
	public boolean isSaveInProgressNotPresent(int time) throws Exception{
		boolean flag = false;
		int i = 0;
		
		//Override the timeout in SeleniumBaseTest
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);		
		
		while (i < time) {
			try{
				if(saveInProgressInfoText().isDisplayed() || saveInProgressInfoText().isEnabled() || !(saveInProgressInfoText().getAttribute("style").contains("none"))){					
					logger.info("        Save in progress is visible.");					
				}
			}catch (Exception e){		
				flag = true;
				logger.info("        Save in progress is not visible.");	
				break;
			}
			Thread.sleep(1000);
			i++;		
		}
		return flag;	
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method checks if the accn was loaded properly within 10 seconds time of period
	public boolean isAccnLoaded(String accnId, String testDb) throws Exception{
		boolean flag = false;	
		int i = 0;
		int time = 10;
		daoManagerPatientPortal = new DaoManagerPatientPortal(b.getConfig().getRpmDatabase());		
				
		String ptFullNameInDb = daoManagerPatientPortal.getLnameByAccnId(accnId, testDb) + daoManagerPatientPortal.getFnameByAccnId(accnId, testDb).replaceAll(" ", "");	
		String dos = daoManagerPatientPortal.getDosByAccnId(accnId, testDb);
		String ptDOB = daoManagerPatientPortal.getDobByAccnId(accnId, testDb);
				
		while (i < time) {	
			if ((accnIdText().getAttribute("value").trim().equals(accnId)) 					 
					&& (dosText().getText().trim().equals(dos)) 					
					&& (dobText().getText().trim().equals(ptDOB))
					&& (ptFullNameText().getText().replaceAll(",","").replaceAll(" ", "").trim().equals(ptFullNameInDb)) )  			
			{
				flag = true;
				logger.info("        The Accession is loaded.");
				break;
			}else {
				logger.info("        The Accession was not loaded.");	
				Thread.sleep(1000);
				i++;
			}	
		}
		
		return flag;		
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method checks if the accn was done saving 
	public boolean isSaveDone() throws Exception{
		boolean flag = false;	
		b = new SeleniumBaseTest();
		
		if(isSaveInProgressNotPresent(40)){		
			//Click Save again in case of any error returns
			if (isErrorReturned()){	
				if (b.isElementPresent(saveBtn(), 20)){						
					clickSave();						
					//if (!(b.isElementNotPresent(saveInProgressInfoText(), 30))){
					if (!(isSaveInProgressNotPresent(40))){
						flag = false;
						logger.info("        The Accession was not saved.");
					}else{
						flag = true;
						Thread.sleep(5000);
						logger.info("        The Accession was saved.");
					}
				}else {
					flag = true;
					Thread.sleep(5000);
					logger.info("        The Accession was saved.");
				}		
			}else{
				flag = true;
				Thread.sleep(5000);
				logger.info("        The Accession was saved.");
			}
		}
		return flag;		
	}
	
		
}
