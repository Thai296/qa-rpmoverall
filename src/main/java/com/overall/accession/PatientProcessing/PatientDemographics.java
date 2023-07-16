package com.overall.accession.PatientProcessing;


import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PatientDemographics {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private  WebDriverWait wait;

	public PatientDemographics(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		this.wait=wait;
	}
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public String pageTitleTextInFrame() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($($('#contentarea').contents()).contents().find('[class=\"blue\"]')[0]).text()"); 
	}
	
	public WebElement epiInput() throws Exception {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("patientId")));
	}
	
	public String epiInputInFrameText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($($('#contentarea').contents()).contents().find('#ptId')[0]).val()"); 
	}
	
	public WebElement ptDemoSearchBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[title=\"Patient Demographic Search\"]')[0]"); 
	}
	
	public WebElement expDateInput() throws Exception {
		return driver.findElement(By.id("expDt"));
	}
	
	public WebElement createNewEPIBtn() throws Exception {
		return driver.findElement(By.id("btnEpiGen"));
	}
	
	public WebElement modifyEPIInput() throws Exception {
		return driver.findElement(By.id("newptId"));
	}
	
	//Associated Patient IDs
	public WebElement addPatientBtn() throws Exception {
		return driver.findElement(By.id("btnAddAsscPt"));
	}
	
	public WebElement deleteAssocPtIdBtn() throws Exception {
		return driver.findElement(By.id("isDelPtVal"));
	}
	
	public String asscPtIdText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#asscPtId').val()"); 
	}
	
	public String asscSrcTypText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#asscsrcType').val()"); 
	}
	
	public String asscSrcIdText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#asscsrcId').val()"); 
	}
	
	public WebElement editAsscPtIdBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[title=\"Edit Associated Patient Id\"]')[0]"); 
	}
	
	//Create/Edit Associated Patient ID popup window
	public WebElement asscPtIdPageTitle() throws Exception {
		return driver.findElement(By.id("ui-dialog-title-associatedPtId"));
	}
	
	public WebElement asscPtIdInput() throws Exception {
		return driver.findElement(By.id("aAsscPtId"));
	}
	
	public WebElement asscSrcTypDropDown() throws Exception {
		return driver.findElement(By.id("aSrcType"));
	}
	
	public WebElement asscClnSrcIdInput() throws Exception {
		return driver.findElement(By.id("aClnSrcId"));
	}
	
	public WebElement clnSearchBtn() throws Exception {
		return driver.findElement(By.id("aclnSrcButton"));
	}
	
	public WebElement asscSubmitBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('[aria-labelledby*=\"associatedPtId\"] [type=\"button\"]')[0].click()"); 
	}
	
	public WebElement asscFacSrcIdDropDown() throws Exception {
		return driver.findElement(By.id("aFacSrcId"));
	}
	
	//Patient Information
	public WebElement ptLastNameInput() throws Exception {
		return driver.findElement(By.id("ptLastName"));
	}
	
	public WebElement ptFirstNameInput() throws Exception {
		return driver.findElement(By.id("ptFirstName"));
	}
	
	public WebElement ptZipInput() throws Exception {
		return driver.findElement(By.id("ptPostalCode"));
	}
	
	public WebElement ptSSNInput() throws Exception {
		return driver.findElement(By.id("headerSSN"));
	}
	
	public WebElement ptDOBInput() throws Exception {
		return driver.findElement(By.id("ptDateOfBirth"));
	}
	
	public WebElement ptGenderDropDown() throws Exception {
		return driver.findElement(By.id("ptGender"));
	}
	
	public WebElement ptMaritalStatusDropDown() throws Exception {
		return driver.findElement(By.id("ptMaritalStaTyp"));
	}
	
	public WebElement ptAddr1Input() throws Exception {
		return driver.findElement(By.id("ptAddress1"));
	}
	
	public WebElement ptAddr2Input() throws Exception {
		return driver.findElement(By.id("ptAddress2"));
	}
	
	public WebElement ptHmPhoneInput() throws Exception {
		return driver.findElement(By.id("ptPhone1"));
	}
	
	public WebElement ptWkPhoneInput() throws Exception {
		return driver.findElement(By.id("ptPhone2"));
	}
	
	public WebElement ptEmailInput() throws Exception {
		return driver.findElement(By.id("ptEmail"));
	}
	
	public WebElement ptCommentsInput() throws Exception {
		return driver.findElement(By.id("ptComments"));
	}
	
	public WebElement ptMSPDOSInput() throws Exception {
		return driver.findElement(By.id("ptMSPDos"));
	}
	
	public WebElement ptCountryDropDown() throws Exception {
		return driver.findElement(By.id("ptCountry"));
	}
	
	public WebElement ptCityInput() throws Exception {
		return driver.findElement(By.id("ptCity"));
	}
	
	public WebElement ptStateDropDown() throws Exception {
		return driver.findElement(By.id("ptState"));
	}
	
	public WebElement suspBadAddrCheckBox() throws Exception {
		return driver.findElement(By.id("suspended"));
	}
	
	//Insurance Information
	public WebElement payorIDInput() throws Exception {
		return driver.findElement(By.id("payorAbbrev"));
	}
	
	public WebElement createNewEffDateBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('[alt=\"Create New Eff. Date\"]')[0].click()"); 
	}
	
	public WebElement newEffDateInput() throws Exception {
		return driver.findElement(By.id("newEffDt"));
	}
	
	public WebElement effDateDropDown() throws Exception {
		return driver.findElement(By.id("effDt"));
	}	
	
	public WebElement deleteEffDateCheckBox() throws Exception {
		return driver.findElement(By.id("isDelEffDtRecs"));
	}
	
	public WebElement subsIdInput() throws Exception {
		return driver.findElement(By.id("subscriberId"));
	}
	
	public WebElement grpIdInput() throws Exception {
		return driver.findElement(By.id("grpId"));
	}
	
	public WebElement grpNameInput() throws Exception {
		return driver.findElement(By.id("grpName"));
	}
	
	public WebElement planIdInput() throws Exception {
		return driver.findElement(By.id("planId"));
	}
	
	public WebElement insuredLNameInput() throws Exception {
		return driver.findElement(By.id("insLNm"));
	}
	
	public WebElement insuredFNameInput() throws Exception {
		return driver.findElement(By.id("insFNm"));
	}
	
	public WebElement rlshpDropDown() throws Exception {
		return driver.findElement(By.id("relshpTypId"));
	}
	
	public WebElement insuredDOBInput() throws Exception {
		return driver.findElement(By.id("insDOB"));
	}
	
	public WebElement insuredSexDropDown() throws Exception {
		return driver.findElement(By.id("insSex"));
	}
	
	public WebElement insuredAddr1Input() throws Exception {
		return driver.findElement(By.id("insAddr1"));
	}
	
	public WebElement insuredAddr2Input() throws Exception {
		return driver.findElement(By.id("insAddr2"));
	}
	
	public WebElement insuredCityInput() throws Exception {
		return driver.findElement(By.id("insCity"));
	}
	
	public WebElement insuredStateDropDown() throws Exception {
		return driver.findElement(By.id("insStId"));
	}
	
	public WebElement insuredZipInput() throws Exception {
		return driver.findElement(By.id("insZipId"));
	}
	
	public WebElement insuredCountryDropDown() throws Exception {
		return driver.findElement(By.id("insCountry"));
	}
	
	public WebElement insuredHmPhnInput() throws Exception {
		return driver.findElement(By.id("insHmPhn"));
	}
	
	public WebElement insuredWkPhnInput() throws Exception {
		return driver.findElement(By.id("insWrkPhn"));
	}
	
	public WebElement claimNotesInput() throws Exception {
		return driver.findElement(By.id("otherInfo"));
	}
	
	public WebElement internalNotesInput() throws Exception {
		return driver.findElement(By.id("cmnt"));
	}
	
	public WebElement otherInfo1Input() throws Exception {
		return driver.findElement(By.id("otherInfo1"));
	}
	
	public WebElement otherInfo2Input() throws Exception {
		return driver.findElement(By.id("otherInfo2"));
	}
	
	public WebElement otherInfo3Input() throws Exception {
		return driver.findElement(By.id("otherInfo3"));
	}
	
	public WebElement caseIdInput() throws Exception {
		return driver.findElement(By.id("caseId"));
	}
	
	public WebElement insuredSSNInput() throws Exception {
		return driver.findElement(By.id("insSsn"));
	}
	
	public WebElement emplNameInput() throws Exception {
		return driver.findElement(By.id("employerName"));
	}
	
	public WebElement emplStatusDropDown() throws Exception {
		return driver.findElement(By.id("emplStatus"));
	}
	
	public WebElement emplAddr1Input() throws Exception {
		return driver.findElement(By.id("employerAddr1"));
	}
	
	public WebElement emplAddr2Input() throws Exception {
		return driver.findElement(By.id("employerAddr2"));
	}
	
	public WebElement emplCityInput() throws Exception {
		return driver.findElement(By.id("employerCity"));
	}
	
	public WebElement emplStateDropDown() throws Exception {
		return driver.findElement(By.id("employerState"));
	}
	
	public WebElement emplZipInput() throws Exception {
		return driver.findElement(By.id("employerZipId"));
	}
	
	public WebElement emplCountryDropDown() throws Exception {
		return driver.findElement(By.id("employerCountry"));
	}
	
	public WebElement emplPhoneInput() throws Exception {
		return driver.findElement(By.id("employerPhn"));
	}
	
	public WebElement emplFaxInput() throws Exception {
		return driver.findElement(By.id("employerFax"));
	}
	
	public WebElement suspSummaryText() throws Exception {
		return driver.findElement(By.id("summary"));
	}
	
	public WebElement suspShowDetailLink() throws Exception {
		return driver.findElement(By.id("showLink"));
	}
	
	public WebElement suspNoteLink() throws Exception {
		return driver.findElement(By.name("suspNoteImg"));
	}
	
	public WebElement suspFixCheckBox() throws Exception {
		//eturn driver.findElement(By.id("9840_fix"));
		return driver.findElement(By.xpath("//*[@id=\"tbl_suspendedReason\"]/tbody/tr[2]/td[7]/input"));
	}
	
	public WebElement suspendedCheckBox() throws Exception {
		return driver.findElement(By.id("isSusp"));
	}
	
	public WebElement pyrIdInput(String index) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[name=\"payorAbbrev\"]')[" + index + "]");
	}
	public WebElement pyrTab(String index) throws Exception {
		return driver.findElement(By.xpath("//*[@id=\"payorTabs\"]/ul/li["+index+"]/a"));
	}
	public WebElement subsIdInput(String index) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[name=\"subscriberId\"]')[" + index + "]");
	}
	
	//Dialysis Information
	public WebElement dialysisTypeDropDown() throws Exception {
		return driver.findElement(By.id("dialTypId"));
	}
	
	public WebElement medicationDropDown() throws Exception {
		return driver.findElement(By.id("medTypId"));
	}
	
	public WebElement firstDialDtInput() throws Exception {
		return driver.findElement(By.id("firstDialDt"));
	}
	
	public WebElement addRowBtn() throws Exception {
		return driver.findElement(By.id("btnAddNXdph"));
	}
	
	public WebElement dialPtHistDOSInput() throws Exception {
		return driver.findElement(By.id("nxdphDOS"));
	}
	
	public WebElement procCdSearchBtn() throws Exception {
		return driver.findElement(By.id("btnPrcCodeSrch"));
	}
	
	public WebElement procCdInput() throws Exception {
		return driver.findElement(By.id("nxdphProc"));
	}
	
	//All Accessions tab
	public WebElement allAccnTab() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('[href=\"#accnTab\"]')[0].click()"); 
	}
	
	public WebElement accnIdLink() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[name=\"accnId\"]')[0]"); 
	}
	
	//Page
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btnSaveAndClear"));
	}
	
	//Create New Patient popup window
	public WebElement ptLNameInput() throws Exception {
		return driver.findElement(By.id("sysGen_lname"));
	}
	
	public WebElement okBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[aria-labelledby*=\"genEPI\"] [type=\"button\"]')[1]"); 
	}
	
	//Create/Edit Suspend Reason popup window
	public WebElement suspPopEditorTitle() throws Exception {
		return driver.findElement(By.id("ui-dialog-title-suspEditor"));
	}
	
	public String suspPopDateText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#pSuspDate').val()");
	}
	
	public WebElement suspPopReasonDropDown() throws Exception {
		return driver.findElement(By.id("pSuspRsn"));
	}
	
	public WebElement suspPopNoteInput() throws Exception {
		return driver.findElement(By.id("hpSuspNote"));
	}
	
	public String suspPopNoteText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#hpSuspNote').val()");
	}
	
	public WebElement suspPopFixCheckBox() throws Exception {
		return driver.findElement(By.id("pSuspFix"));
	}
	
	public WebElement suspPopOkBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"ui-state-default ui-corner-all\"]')[5]"); 
	}
	
	public WebElement suspPopCancelBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"ui-state-default ui-corner-all\"]')[4]"); 
	}

	//-------------------------------------------------------------------------------------
	public void setSuspNote(String str) throws Exception{	
		suspPopNoteInput().clear();
		suspPopNoteInput().sendKeys(str);	
		suspPopNoteInput().sendKeys(Keys.TAB);
		logger.info("        Entered Suspend Note: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setEmplPhone(String str) throws Exception{	
		emplPhoneInput().clear();
		emplPhoneInput().sendKeys(str);	
		emplPhoneInput().sendKeys(Keys.TAB);
		logger.info("        Entered Employer Phone: " + str);
	}

	//-------------------------------------------------------------------------------------
	public void setEmplFax(String str) throws Exception{	
		emplFaxInput().clear();
		emplFaxInput().sendKeys(str);	
		emplFaxInput().sendKeys(Keys.TAB);
		logger.info("        Entered Employer Fax: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setEmplZip(String str) throws Exception{	
		emplZipInput().clear();
		emplZipInput().sendKeys(str);	
		emplZipInput().sendKeys(Keys.TAB);
		logger.info("        Entered Employer Zip: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setEmplAddr2(String str) throws Exception{	
		emplAddr2Input().clear();
		emplAddr2Input().sendKeys(str);	
		emplAddr2Input().sendKeys(Keys.TAB);
		logger.info("        Entered Employer Addr2: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setEmplAddr1(String str) throws Exception{	
		emplAddr1Input().clear();
		emplAddr1Input().sendKeys(str);	
		emplAddr1Input().sendKeys(Keys.TAB);
		logger.info("        Entered Employer Addr1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setEmplName(String str) throws Exception{	
		emplNameInput().clear();
		emplNameInput().sendKeys(str);	
		emplNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered Employer Name: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setClaimNotes(String str) throws Exception{	
		claimNotesInput().clear();
		claimNotesInput().sendKeys(str);
		internalNotesInput().click();
		//claimNotesInput().sendKeys(Keys.TAB);
		logger.info("        Entered Claim Notes: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setInternalNotes(String str) throws Exception{	
		internalNotesInput().clear();
		internalNotesInput().sendKeys(str);
		otherInfo1Input().click();
		//claimNotesInput().sendKeys(Keys.TAB);
		logger.info("        Entered Internal Notes: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setOtherInfo1(String str) throws Exception{	
		otherInfo1Input().clear();
		otherInfo1Input().sendKeys(str);
		otherInfo2Input().click();
		//claimNotesInput().sendKeys(Keys.TAB);
		logger.info("        Entered Other Info1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setOtherInfo2(String str) throws Exception{	
		otherInfo2Input().clear();
		otherInfo2Input().sendKeys(str);
		otherInfo3Input().click();
		//claimNotesInput().sendKeys(Keys.TAB);
		logger.info("        Entered Other Info2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setOtherInfo3(String str) throws Exception{	
		otherInfo3Input().clear();
		otherInfo3Input().sendKeys(str);
		caseIdInput().click();
		//claimNotesInput().sendKeys(Keys.TAB);
		logger.info("        Entered Other Info3: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setCaseId(String str) throws Exception{	
		caseIdInput().clear();
		caseIdInput().sendKeys(str);
		insuredSSNInput().click();
		//claimNotesInput().sendKeys(Keys.TAB);
		logger.info("        Entered Case ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setInsurdHmPhn(String str) throws Exception{	
		insuredHmPhnInput().clear();
		insuredHmPhnInput().sendKeys(str);
		insuredHmPhnInput().sendKeys(Keys.TAB);
		logger.info("        Entered Insured Hm Phn: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setInsurdWkPhn(String str) throws Exception{	
		insuredWkPhnInput().clear();
		insuredWkPhnInput().sendKeys(str);
		insuredWkPhnInput().sendKeys(Keys.TAB);
		logger.info("        Entered Insured Wk Phn: " + str);
	}

	//-------------------------------------------------------------------------------------
	public void setInsurdSSN(String str) throws Exception{	
		insuredSSNInput().clear();
		insuredSSNInput().sendKeys(str);
		insuredSSNInput().sendKeys(Keys.TAB);
		logger.info("        Entered Insured SSN: " + str);
	}	
	
	
	//-------------------------------------------------------------------------------------
	public void setInsurdAddr1(String str) throws Exception{	
		insuredAddr1Input().clear();
		insuredAddr1Input().sendKeys(str);
		insuredAddr1Input().sendKeys(Keys.TAB);
		logger.info("        Entered Insured Addr1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setInsurdAddr2(String str) throws Exception{	
		insuredAddr2Input().clear();
		insuredAddr2Input().sendKeys(str);
		insuredAddr2Input().sendKeys(Keys.TAB);
		logger.info("        Entered Insured Addr2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setInsurdZip(String str) throws Exception{	
		insuredZipInput().clear();
		insuredZipInput().sendKeys(str);
		insuredZipInput().sendKeys(Keys.TAB);
		logger.info("        Entered Insured ZIP: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setInsurdDOB(String str) throws Exception{	
		insuredDOBInput().clear();
		insuredDOBInput().sendKeys(str);
		insuredDOBInput().sendKeys(Keys.TAB);
		logger.info("        Entered Insured DOB: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setInsurdLName(String str) throws Exception{	
		insuredLNameInput().clear();
		insuredLNameInput().sendKeys(str);
		insuredLNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered Insured Last Name: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setInsurdFName(String str) throws Exception{	
		insuredFNameInput().clear();
		insuredFNameInput().sendKeys(str);
		insuredFNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered Insured First Name: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPlanId(String str) throws Exception{	
		planIdInput().clear();
		planIdInput().sendKeys(str);
		planIdInput().sendKeys(Keys.TAB);
		logger.info("        Entered Plan Id: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setGrpName(String str) throws Exception{	
		grpNameInput().clear();
		grpNameInput().sendKeys(str);
		grpNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered Group Name: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setGrpId(String str) throws Exception{	
		grpIdInput().clear();
		grpIdInput().sendKeys(str);
		grpIdInput().sendKeys(Keys.TAB);
		logger.info("        Entered Group ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setInsSubsId(String str) throws Exception{	
		subsIdInput().clear();
		subsIdInput().sendKeys(str);
		subsIdInput().sendKeys(Keys.TAB);
		logger.info("        Entered Insured Subscriber: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setNewEPI(String str) throws Exception{			
		modifyEPIInput().sendKeys(str);
		modifyEPIInput().sendKeys(Keys.TAB);
		logger.info("        Entered new EPI: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setNewEffDate(String str) throws Exception{			
		newEffDateInput().sendKeys(str);
		newEffDateInput().sendKeys(Keys.TAB);
		logger.info("        Entered new Eff Date: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtFirstName(String str) throws Exception{	
		ptFirstNameInput().clear();
		ptFirstNameInput().sendKeys(str);
		ptFirstNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient First Name: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtSSN(String str) throws Exception{	
		ptSSNInput().clear();
		ptSSNInput().sendKeys(str);
		ptSSNInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient SSN: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtDOB(String str) throws Exception{	
		ptDOBInput().clear();
		ptDOBInput().sendKeys(str);
		ptDOBInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient DOB: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtAddr1(String str) throws Exception{
		ptAddr1Input().clear();
		ptAddr1Input().sendKeys(str);
		ptAddr1Input().sendKeys(Keys.TAB);
		logger.info("        Entered Patient Addr1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtAddr2(String str) throws Exception{	
		ptAddr2Input().clear();
		ptAddr2Input().sendKeys(str);
		ptAddr2Input().sendKeys(Keys.TAB);
		logger.info("        Entered Patient Addr2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtHmPhone(String str) throws Exception{		
		ptHmPhoneInput().clear();
		ptHmPhoneInput().sendKeys(str);
		ptHmPhoneInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient Home Phone: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtWkPhone(String str) throws Exception{	
		ptWkPhoneInput().clear();
		ptWkPhoneInput().sendKeys(str);
		ptWkPhoneInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient Work Phone: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtEmail(String str) throws Exception{	
		ptEmailInput().clear();
		ptEmailInput().sendKeys(str);
		ptEmailInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient Email: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtComments(String str) throws Exception{	
		ptCommentsInput().clear();
		ptCommentsInput().sendKeys(str);
		ptCommentsInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient Comments: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtMSPDOS(String str) throws Exception{	
		ptMSPDOSInput().clear();
		ptMSPDOSInput().sendKeys(str);
		ptMSPDOSInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient DOS of Most Recent MSP Form: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setAsscClnPtId(String str) throws Exception{		
		asscPtIdInput().sendKeys(str);
		asscPtIdInput().sendKeys(Keys.TAB);
		logger.info("        Entered Client Patient ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setAsscClnSrcId(String str) throws Exception{		
		asscClnSrcIdInput().sendKeys(str);
		asscClnSrcIdInput().sendKeys(Keys.TAB);
		logger.info("        Entered Client Source ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setDialPtHistDOS(String str) throws Exception{	
		dialPtHistDOSInput().clear();
		dialPtHistDOSInput().sendKeys(str);
		dialPtHistDOSInput().sendKeys(Keys.TAB);
		logger.info("        Entered Dialysis Patient History DOS: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setFirstDialDate(String str) throws Exception{		
		firstDialDtInput().clear();
		firstDialDtInput().sendKeys(str);
		firstDialDtInput().sendKeys(Keys.TAB);
		logger.info("        Entered First Date of Dialysis: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPayorId(String str) throws Exception{		
		payorIDInput().clear();
		payorIDInput().sendKeys(str);
		payorIDInput().sendKeys(Keys.TAB);
		logger.info("        Entered Payor ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtZip(String str) throws Exception{	
		ptZipInput().clear();
		ptZipInput().sendKeys(str);
		ptZipInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient Zip: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setNewPtLName(String str) throws Exception{		
		ptLNameInput().sendKeys(str);
		ptLNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient Last Name: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setExpDate(String str) throws Exception{	
		expDateInput().clear();
		expDateInput().sendKeys(str);
		expDateInput().sendKeys(Keys.TAB);
		logger.info("        Entered Exp Date: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setEPI(String str) throws Exception{		
		epiInput().sendKeys(str);
		//epiInput().sendKeys(Keys.TAB);
		epiInput().click();
		epiInput().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("        Entered EPI: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtLastName(String str) throws Exception{	
		ptLastNameInput().clear();
		ptLastNameInput().sendKeys(str);
		ptLastNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient Last Name: " + str);
	}
	
	public void checkSuspendReason(boolean isSuspend) throws Exception{
		if (isSuspend) {
			suspBadAddrCheckBox().click();
			Thread.sleep(3000);
			logger.info("       Clicked Suspend");
//			switchToPopupWin();
//			suspPopOkBtn().click();
		}
	}
	
	//-------------------------------------------------------------------------------------
	public void setPtGender(WebElement element, String value) throws Exception{	
		SeleniumBaseTest b = new SeleniumBaseTest();
		b.selectItemByVal(element, value);
		
		String gender = "";
		switch(value){
			case "1" :
				gender = "Male";
				break;
			case "2" :
				gender = "Female";
				break;
			default:

		}
		
		logger.info("        Selected Patient Gender: " + gender);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPayorId(String str, String index) throws Exception{		
		pyrIdInput(index).clear();
		pyrIdInput(index).sendKeys(str);
		pyrIdInput(index).sendKeys(Keys.TAB);
		logger.info("        Entered Payor ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setInsSubsId(String str, String index) throws Exception{	
		subsIdInput(index).clear();
		subsIdInput(index).sendKeys(str);
		subsIdInput(index).sendKeys(Keys.TAB);
		logger.info("        Entered Insured Subscriber: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void clickSuspendedShowDetailLnk() throws Exception{	
		suspShowDetailLink().click();
		logger.info("        Checked the Suspended Show Detail hyperlink.");
	}

	//-------------------------------------------------------------------------------------
	public void checkSuspendedFixChkBox() throws Exception{	
		suspFixCheckBox().click();
		logger.info("        Checked the Suspended Fix checkbox.");
	}	

	//-------------------------------------------------------------------------------------
	public void checkSuspendedFixChkBoxInPopup() throws Exception{	
		suspPopFixCheckBox().click();
		logger.info("        Checked the Suspended Fix checkbox in Edit Suspend Reason popup window.");
	}	
	
	//-------------------------------------------------------------------------------------
	public void clickOkBtnInPopup() throws Exception{	
		suspPopOkBtn().click();
		logger.info("        Checked Ok button in Edit Suspend Reason popup window.");
	}
	
	//-------------------------------------------------------------------------------------
	public void setUnSuspendPt() throws Exception{	
		checkSuspendedFixChkBox();
		logger.info("        UnSupended Patient.");
	}
}
