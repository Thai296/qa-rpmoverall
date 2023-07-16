package com.overall.accession.accessionProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class AccessionDemographics {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public AccessionDemographics(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	//Page Errors
	public String eligibleMsgText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('body > table > tbody > tr > td').text()"); 
	}
	
	public String ineligibleErrMsgText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('body > table:nth-child(11) > tbody > tr > td')[0]).text()"); 
	}
	
	
	public WebElement accnIdInput(String accnId) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#accnId')).val(\"" + accnId + "\")).trigger('onblur')[0]"); 
	}
	
	//Overload
	public WebElement accnIdInput() throws Exception {
		return driver.findElement(By.id("accnId"));
	}
	
	public WebElement accnIdInputInFrame(String accnId) throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#contentarea').contents()).contents().find('#accnId').val(\"" + accnId + "\").trigger('onblur')[0]");		
	}
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public String pageTitleTextInFrame() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($($('#contentarea').contents()).contents().find('[class=\"blue\"]')[0]).text()"); 
	}
	
	
	public WebElement accnSearchBtn() throws Exception {
		return driver.findElement(By.id("accnSrch"));
	}
	
	public WebElement ptFNameInput() throws Exception {
		return driver.findElement(By.id("ptFNm"));
	}
	
	public WebElement ptLNameInput() throws Exception {
		return driver.findElement(By.id("ptLNm"));
	}
	
	public WebElement accnStatIdInput() throws Exception {
		return driver.findElement(By.id("accnStatId"));
	}
	
	public WebElement clnAbbrvInput() throws Exception {
		return driver.findElement(By.id("clnAbbrv"));
	}
	
	public WebElement ptAddr1Input() throws Exception {
		return driver.findElement(By.id("ptAddr1"));
	}
	
	public WebElement ptSexDropdown() throws Exception {
		return driver.findElement(By.id("ptSex"));
	}
	
	public WebElement ptZipInput() throws Exception {
		return driver.findElement(By.id("ptZip"));
	}	
	
	public WebElement forceToCorrespCheckbox() throws Exception {
		return driver.findElement(By.id("forceToCorresp"));
	}
	
	//Current Accesion Errors
	public WebElement reasonCodeText() throws Exception {
		return driver.findElement(By.id("errCd"));
	}
	
	public WebElement reasonCodeWellExamText() throws Exception {
		return driver.findElement(By.id("errCdAbbrv"));
	}
	
	public WebElement errTable(int row) throws Exception {
		return driver.findElement(By.xpath("//*[@id='errTable']/tbody/tr[" + row + "]/td[5]/input[1]"));
	}
	
	public WebElement errTables() throws Exception {
		return driver.findElement(By.id("errTable"));
	}
	
	public WebElement errorCountText() throws Exception {
		return driver.findElement(By.id("errRwCnt"));
	}
	
	public WebElement accnErrorReasonCodeText(String errorName) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($(':input[value=\"" + errorName +  "\"]')).get(0)");
	}
	
	//Patient Demographics
	public WebElement createNewEpiBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($(':button[title=\"Create New EPI\"]')).get(0)"); 
	}
	
	public WebElement orderNpiInput() throws Exception {
		return driver.findElement(By.id("orderNpi"));
	}
	
	public WebElement orderPhysIdInput() throws Exception {
		return driver.findElement(By.id("orderPhysId"));
	}
	
	public WebElement epiInput() throws Exception {
		return driver.findElement(By.id("ptId"));
	}
	
	public String epiInputInFrameText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($($('#contentarea').contents()).contents().find('#ptId')[0]).val()"); 
	}
	
	public String epiText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('#ptId')).val()"); 
	}
	
	public WebElement phlebFacAbbrvInput() throws Exception {
		return driver.findElement(By.id("phlebFacAbbrv"));
	}
	
	public WebElement phlebFacDescrInput() throws Exception {
		return driver.findElement(By.id("phlebFacDescr"));
	}
	
	public WebElement subsIdInput() throws Exception {
		return driver.findElement(By.id("subsId"));
	}	
	
	public WebElement indigentInput() throws Exception {
		return driver.findElement(By.id("miscIndgnt"));
	}
	
	public WebElement ptDOBInput() throws Exception {
		return driver.findElement(By.id("ptDob"));
	}
	
	//Create New Patient Popup
	public WebElement createNewPatientPopupTitleText() throws Exception {
		return driver.findElement(By.id("ui-dialog-title-genEPI"));
	}
	
	public WebElement createNewPatientPopupOkBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('div:nth-child(11) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(2) > span')).get(0)"); 
	}
	
	public WebElement createNewPatientPopupCancelBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('div:nth-child(11) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span')).get(0)"); 
	}
		
	//Insurance Info
	public WebElement payorIdInput() throws Exception {
		return driver.findElement(By.id("pyrAbbrv"));
	}	
	
	public WebElement insProviderIdInput() throws Exception {
		return driver.findElement(By.id("prvdrNum"));
	}
	
	public WebElement insPyrPrioInput() throws Exception {
		return driver.findElement(By.id("pyrPrio"));
	}
	
	public WebElement primaryPyrRlshpDropDown() throws Exception {
		return driver.findElement(By.id("relshpTypId"));
	}
	
	public WebElement primaryPyrSubsIdInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('.tbbdy1 #subsId')[0]"); 
	}
	
	//Eligibility Transactions
	public WebElement checkEligibilityCheckBox() throws Exception {
		return driver.findElement(By.id("checkElig"));
	}
	
	public WebElement showEligHistoryLink() throws Exception {
		return driver.findElement(By.id("showEligHistory"));
	}
	
	public WebElement eligTransHistoryTable() throws Exception {
		return driver.findElement(By.id("tbl_eligTrxn"));
	}
	
	public WebElement eligTransIDSortBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#tbl_eligTrxn > thead > tr > th:nth-child(5)').click()"); 
	}
	
	public String eligTransIdText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[class=\"cellText trxnID\"]')[0]).text()"); 
	}
	
	public String eligTransPyrNameText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#tbl_eligTrxn > tbody > tr:nth-child(1) > td:nth-child(2)').text()"); 
	}
	
	public String eligTransEligServiceText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#tbl_eligTrxn > tbody > tr:nth-child(1) > td:nth-child(3)').text()"); 
	}
	
	public String eligTransEligChkDtText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[class*=\"eligCheckDateAsSeconds\"]')[0]).text()"); 
	}
	
	public WebElement eligRespViewBtn() throws Exception {		
		return driver.findElement(By.cssSelector("#tbl_eligTrxn > tbody > tr > td.cellCenter > div"));
	}
	
	public String emptyEligTransHistText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('#tbl_eligTrxn [class=\"xf_empty_grid_row\"]')[0]).text()"); 
	}
	
	//Dialysis Information
	public WebElement forceToRepriceCheckbox() throws Exception {
		return driver.findElement(By.id("rePrice"));
	}	
		
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}
	
	/*Submit Claims*/
	public WebElement submitClaimBtn() throws Exception {
		return driver.findElement(By.id("addRowClaim"));
	}
	
	public WebElement payorClaimDropdown(int rowIndex) throws Exception {
		return driver.findElement(By.xpath("*//table[@id='claimTable']//tbody/tr["+rowIndex+"]/td[3]/select[@id='claimPyrKey']"));
	}
	public WebElement formatClaimDropdown(int rowIndex) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 30);
	    return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("*//table[@id='claimTable']//tbody/tr["+rowIndex+"]/td[7]/select[@id='claimFormat']")));
	}
	
	public WebElement attachTypesDropdown() throws Exception {
		return driver.findElement(By.id("attchType0"));
	}
	public WebElement payorClaimDropdown() throws Exception {
	
		return driver.findElement(By.xpath(".//*[@id='claimPyrKey']"));
	}
	public WebElement formatClaimDropdown() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 30);
	    return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='claimFormat']")));
	}
	

	public void submit() throws Exception{
		submitBtn().click();
		Thread.sleep(3000);
		logger.info("       Clicked Submit button");
	}	
	
	public void reset() throws Exception{
		resetBtn().click();
		Thread.sleep(3000);
		logger.info("       Clicked Reset button");
	}
	
	public void searchAccnId(String accnId) throws Exception{
		accnIdInput(accnId);
		logger.info("        Entered AccnID: " + accnId);
	}
	
	public void setforceToCorresp() throws Exception{
		forceToCorrespCheckbox().click();
		submitBtn().click();
		logger.info("        Submitted Force to Corresp");
	}
	
	public void naviateToCreateNewPatient() throws Exception{
		createNewEpiBtn().click();
		logger.info("        Clicked Create New EPI button");
	}
	
	public void sysGenerateEpi() throws Exception{
		createNewPatientPopupOkBtn().click();
		logger.info("        Generated System EPI");
	}
	
	public void addEpi() throws Exception{
		createNewPatientPopupOkBtn().click();		
		submitBtn().click();
		logger.info("        Submitted EPI");
	}	
	
	public void setPayorId(String payorId) throws Exception{
		payorIdInput().clear();
		payorIdInput().sendKeys(payorId);
		submitBtn().click();
		logger.info("        Submitted PayorId");
	}		

	public void setNewPayorId(String str) throws Exception{
		payorIdInput().clear();
		payorIdInput().sendKeys(str);		
		logger.info("        Entered Payor ID: " + str);
	}
	
	public void setIndigent(String str) throws Exception{
		indigentInput().clear();
		indigentInput().sendKeys(str);		
		logger.info("        Entered Indigent: " + str);
	}	
	
	public void setPrimaryPyrSubsId(String str) throws Exception{
		primaryPyrSubsIdInput().clear();
		primaryPyrSubsIdInput().sendKeys(str);		
		logger.info("        Entered Primary Payor Subscriber ID: " + str);
	}
	
	public void clickSubmitClaimBtn() throws Exception{
		submitClaimBtn().click();
		logger.info("         Click Submit Claim button");
	}
	
	public void clearPtDOBInput() throws Exception{
		ptAddr1Input().clear();
		ptAddr1Input().sendKeys(Keys.TAB);
		logger.info("         Cleared value in Patient DOB Input field.");
	}
	
	public void clearPrimaryPyrSubsIdInput() throws Exception{
		primaryPyrSubsIdInput().clear();
		primaryPyrSubsIdInput().sendKeys(Keys.TAB);
		logger.info("         Cleared value in Primary Payor Subscr ID Input field.");
	}

	public void clearPtAddr1Input() throws Exception{
		ptAddr1Input().clear();
		ptAddr1Input().sendKeys(Keys.TAB);
		logger.info("         Cleared value in Patient Address1 Input field.");
	}
	public void selectAttachTypesDropdown(String str) throws Exception{
		
		attachTypesDropdown().sendKeys(str);		
	    logger.info("        Selected Claim Attachment type " + str);
    }
	
}
