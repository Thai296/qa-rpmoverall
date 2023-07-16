package com.overall.client.clientBillingRules;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClientBillingRules {
	private WebDriverWait wait;
	private RemoteWebDriver driver;
	protected Logger logger;
	
	public ClientBillingRules(RemoteWebDriver driver, WebDriverWait wait) {
		this.wait = wait;
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" +driver );
	}
	
	/* Client Billing Rules Load page */
	
	public WebElement clientBillingRulesLoadPgTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));		
	}
		
	public WebElement clientBillingRulesLoadPgHeaderHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_billing_rules_header']")));
	}
	public WebElement loadClientSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientBillingRulesForm']//*[contains(@class,'layoutMainpLookup')]")));
	}
	
	public WebElement clientBillingRulesClientIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientId")));
	}
	
	public WebElement clientBillingRulesStep2ClientIdTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("clientIdTxt")));
    }
	
	public WebElement clientBillingRulesClientIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientSrchBtn")));
	}
	
	public WebElement clientBillingRulesLoadClientSectionHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_billing_rules_load_client_id']")));
	}
	
	
	/* Client Billing Rules details page */
	
	// Header
	public WebElement headerHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_billing_rules_header']")));
	}
	
	public WebElement headerViewDocumentLnk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='viewDocumentHeaderInfo']//a[contains(@class,'viewDocumentLabel')]")));
	}
	
	public WebElement headerViewClientPortalDocUpDocument() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='viewDocumentHeaderInfo']//a[contains(@class,'viewOrganizationDocumentLabel')]")));
	}

	public WebElement headerClientOrderingFacAbbrevTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='facilityHeaderInfo']//span[contains(@class,'clientOrderingFacAbbrev')]")));
	}
	
	public WebElement headerClientOrderingFacNameTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='facilityHeaderInfo']//span[contains(@class,'clientOrderingFacName')]")));  
	}

	public WebElement headerClientIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientBillingRulesForm']//span[contains(@class,'clientIdTxt')]")));
	}

	public WebElement headerClientNmTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientBillingRulesForm']//span[contains(@class,'clientNameTxt')]")));
	}
	
	public WebElement headerClientAccountTypeTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientBillingRulesForm']//span[contains(@class,'accountTypeTxt')]")));
	}
	
	// BILLING RULES SECTION
	
	public WebElement billingRulesSectionNameTitleTxt(String sectionName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='titleTextContainer']//span[@class='titleText'][.='"+sectionName+"']")));
	}
	
	public WebElement billingRulesSectionHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_client_billing_rules_billing_rules']")));
	}
	
	public WebElement billingRulesSectionClientIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientCopySrchBtn")));
	}
	public WebElement billingRulesSectionClientIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientCopyId")));
	}
	
	public WebElement billingRulesTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_billingRules")));
	}
	
	public WebElement billingRulesTblPriorityColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_priority")));
	}
	
	public WebElement billingRulesTblBillClientIdColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_billClientAbbrv")));
	}
	
	public WebElement billingRulesTblBillPayorIdColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_billPayorAbbrv")));
	}
	
	public WebElement billingRulesTblRetainInsuredInfoColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_retainInsuredInfo")));
	}
	
	public WebElement billingRulesTblInclusionsExclusionsColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gbox_tbl_billingRules']//*[@id='gs_overrideDisplay']")));
	}
	
	public WebElement billingRulesTblNoteColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_notes")));
	}
	
	public WebElement billingRulesTblRow(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_billingRules']//tr["+row+"]")));
	}
	
	public WebElement billingRulesTblPriorityColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_billingRules']//tr["+row+"]/td[@aria-describedby='tbl_billingRules_priority']")));
	}
	
	public WebElement billingRulesTblBillClientIdColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_billingRules']//tr["+row+"]/td[@aria-describedby='tbl_billingRules_billClientAbbrv']")));
	}
	
	public WebElement billingRulesTblBillPayorIdColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_billingRules']//tr["+row+"]/td[@aria-describedby='tbl_billingRules_billPayorAbbrv']")));
	}
	
	public WebElement billingRulesTblRetainInsuredInfoColChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_billingRules']//tr["+row+"]/td[@aria-describedby='tbl_billingRules_retainInsuredInfo']/input")));
	}
	
	public WebElement billingRulesTblInclusionsExclusionsColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_billingRules']//tr["+row+"]/td[@aria-describedby='tbl_billingRules_overrideDisplay']")));
	}
	
	public WebElement billingRulesTblNoteColHiddenTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_billingRules']//tr["+row+"]/td[@aria-describedby='tbl_billingRules_notes']//span")));
	}
	
	public WebElement billingRulesTblDeletedColChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_billingRules']//tr["+row+"]/td[@aria-describedby='tbl_billingRules_deleted']/input")));
	}
	
	public WebElement billingRulesTblTotalRecordTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_billingRules_pagernav_right']/div")));
	}
	
	public WebElement billingRulesTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_billingRules")));
	}
	
	public WebElement billingRulesTblEditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_billingRules")));
	}
	
	public WebElement contactDetailTblTitlePopupTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='edithdtbl_contactDetail']/span")));
	}
	
	public WebElement billingRulesNotePopupNoteTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dlgPopupNoteDisplay")));
	}
	
	public WebElement billingRulesNotePopupOkBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='ui-dialog-buttonset']//button[contains(@class,'lockDownOnSubmit btn_submit')]")));
	}
	// Add/Edit popup of Billing Rules section
	
	public WebElement billingRulesTblPopupTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='edithdtbl_billingRules']/span")));
	}
	
	public WebElement billingRulesTblPopupPriorityInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("priority")));
	}
	
	public WebElement billingRulesTblPopupRetainInsuredInfoChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("retainInsuredInfo")));
	}
	
	public WebElement billingRulesTblPopupPatientTypeDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_patientTyp")));
	}
	
	public WebElement billingRulesTblPopupPayorGroupInclusionDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_billPyrGrpIncl")));
	}
	
	public WebElement billingRulesTblPopupPayorGroupInclusionDelIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_billPyrGrpIncl']//*[@class='select2-search-choice']//a")));
	}
	
	public WebElement billingRulesTblPopupPayorGroupExclusionDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_billPyrGrpExcl")));
	}
	
	public WebElement billingRulesTblPopupPayorGroupExclusionDelIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_billPyrGrpExcl']//*[@class='select2-search-choice']//a")));
	}
	
	public WebElement billingRulesTblPopupPayorInclusionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billPyrIncl")));
	}
	
	public WebElement billingRulesTblPopupPayorInclusionSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_billPyrIncl']//*[@data-help-id='payor_id_search']")));
	}
	
	public WebElement billingRulesTblPopupPayorExclusionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billPyrExcl")));
	}
	
	public WebElement billingRulesTblPopupPayorExclusionSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_billPyrExcl']//*[@data-help-id='payor_id_search']")));
	}
	
	public WebElement billingRulesTblPopupTestTypeInclusionDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_testTypeIncl")));
	}
	
	public WebElement billingRulesTblPopupTestTypeInclusionDelIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_testTypeIncl']//*[@class='select2-search-choice']//a")));
	}
	
	public WebElement billingRulesTblPopupTestTypeExclusionDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_testTypeExcl")));
	}
	
	public WebElement billingRulesTblPopupTestTypeExclusionDelIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_testTypeExcl']//*[@class='select2-search-choice']//a")));
	}
	
	public WebElement billingRulesTblPopupTestInclusionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testIncl")));
	}
	
	public WebElement billingRulesTblPopupTestExclusionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testExcl")));
	}
	
	public WebElement billingRulesTblPopupTestInclusionSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_testIncl']//*[@data-help-id='testcode_srch']")));
	}
	
	public WebElement billingRulesTblPopupTextExclusionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testExcl")));
	}
	
	public WebElement billingRulesTblPopupTestExclusionSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_testInclusions']//*[@data-help-id='testcode_srch']")));
	}
	
	public WebElement billingRulesTblPopupBillClientIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billClientAbbrv")));
	}
	
	public WebElement billingRulesTblPopupBillClientIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_billClientAbbrv']//*[@id='lookupClientSrchBtn']")));
	}
	
	public WebElement billingRulesTblPopupBillPayorIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billPayorAbbrv")));
	}
	
	public WebElement billingRulesTblPopupBillPayorIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_billPayorAbbrv']//*[@data-help-id='payor_id_search']")));
	}

	public WebElement billingRulesTblPopupNotesInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("notes")));
	}
	
	public WebElement billingRulesTblPopupOkBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
	}
	
	public WebElement billingRulesTblPopupCancleBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
	}
	
	/*  Payor Test EP Rules section */
	public WebElement payorTestEPRulesSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_billing_rules_payor_test_ep_rules']"))); 
	}
	
	public WebElement pyrTestEPRulesTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_payorTestEpRules")));
	}
	
	public WebElement pyrTestEPRulesTblTestIdColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_testDisplay"))); 
	}
	
	public WebElement pyrTestEPRulesTblTestTypeColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_testTypeDisplay"))); 
	}
	
	public WebElement pyrTestEPRulesTblPayorGroupColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorGroupAbbrv"))); 
	}
	
	public WebElement pyrTestEPRulesTblPayorColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorAbbrv"))); 
	}
	
	public WebElement pyrTestEPRulesTblInclusionsExclusionsColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gbox_tbl_payorTestEpRules']//input[@id='gs_overrideDisplay']"))); 
	}
	
	public WebElement pyrTestEPRulesTblTestIdColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorTestEpRules']//tr["+row+"]/td[@aria-describedby='tbl_payorTestEpRules_testDisplay']"))); 
	}
	
	public WebElement pyrTestEPRulesTblTestTypeColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorTestEpRules']//tr["+row+"]/td[@aria-describedby='tbl_payorTestEpRules_testTypeDisplay']"))); 
	}
	
	public WebElement pyrTestEPRulesTblPayorGroupColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorTestEpRules']//tr["+row+"]/td[@aria-describedby='tbl_payorTestEpRules_payorGroupAbbrv']")));  
	}
	
	public WebElement pyrTestEPRulesTblPayorColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorTestEpRules']//tr["+row+"]/td[@aria-describedby='tbl_payorTestEpRules_payorAbbrv']"))); 
	}
	
	public WebElement pyrTestEPRulesTblInclusionsExclusionsColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorTestEpRules']//tr["+row+"]/td[@aria-describedby='tbl_payorTestEpRules_overrideDisplay']"))); 
	}
	
	public WebElement pyrTestEPRulesTblDeletedColChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorTestEpRules']//tr["+row+"]/td[@aria-describedby='tbl_payorTestEpRules_deleted']/input")));
	}
	
	public WebElement pyrTestEPRulesTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_payorTestEpRules")));
	}

	public WebElement pyrTestEPRulesTblPopupTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='edithdtbl_payorTestEpRules']/span")));
	}
	
	public WebElement pyrTestEPRulesTblEditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_payorTestEpRules")));
	}

	public WebElement pyrTestEPRulesTblTotalRecordTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorTestEpRules_pagernav_right']/div")));
	}	
	
	// Add/Edit popup in Payor Test EP Rules Section
	
	public WebElement pyrTestEPRulesSectionPopupTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='edithdtbl_payorTestEpRules']/span")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupTestInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testAbbrv")));
	}
	
	public void enterPyrTestEPRulesSectionPopupTest(String value) {
		WebElement wElement = pyrTestEPRulesSectionPopupTestInput();
		wElement.clear();
		wElement.sendKeys(value + Keys.TAB);
		try {
			waitForValueOfAttributeContains(wElement, 5, "value", value);
		}catch(TimeoutException e) {
			wElement.clear();
			wElement.sendKeys(value);
			wElement.sendKeys(value + Keys.TAB);
		}	    
	}
	
	public WebElement pyrTestEPRulesSectionPopupTestSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tr_testAbbrv']//*[@data-help-id='testcode_srch']")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupTestOnlyAllowRad() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("onlyAllowTestId")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupTestDontAllowRad() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dontAllowTestId")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupTestTypeDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_testTypId']//*[@id='s2id_testTypId']")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupTestTypeOnlyAllowRad() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("onlyAllowTestTypId")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupTestTypeDontAllowRad() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dontAllowTestTypId")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupPayorGroupDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tr_payorGroupId']//*[@id='s2id_payorGroupId']")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupPayorInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorAbbrv")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupPayorSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tr_payorAbbrv']//*[@data-help-id='payor_id_search']")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupPayorGroupExclusionDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tr_testPayorGroupExcl']//*[@id='s2id_testPayorGroupExcl']")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupPayorGroupExclusionDelIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tr_testPayorGroupExcl']//a")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupPayorGroupExclusionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tr_testPayorGroupExcl']//input")));
	}
	
	public void clearAllTestEPRulesSectionPopupPayorGroupExclusion() {
		List<WebElement> pyrGroupExclusions = driver.findElements(By.xpath("//tr[@id='tr_testPayorGroupExcl']//ul[@class='select2-choices']//li//a[@class='select2-search-choice-close']"));
		for (WebElement webElement : pyrGroupExclusions) {
			webElement.click();
		}
	}
	
	public WebElement pyrTestEPRulesSectionPopupPayorExclusionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testPayorExcl")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupPayorExclusionSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tr_testPayorExcl']//*[@data-help-id='payor_id_search']")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupOkBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='FrmGrid_tbl_payorTestEpRules']//parent::div//*[@id='sData']")));
	}
	
	public WebElement pyrTestEPRulesSectionPopupCancelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='FrmGrid_tbl_payorTestEpRules']//parent::div//*[@id='cData']")));
	}

	/* footer */
	
	public WebElement footerHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_billing_rules_summary']")));
	}
	
	public WebElement footerResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement footerSaveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	public WebElement warningPopupWarningTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='confirmationDialog']//span")));
	}
	
	public WebElement warningPopupBtn(String btnNm) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//span[text()='"+btnNm+"']/parent::button")));
	}
	
	public void waitForValueOfAttributeContains(final WebElement element,int timeout, final String attribute, final String expectedValue) throws TimeoutException {
        new WebDriverWait(driver, timeout).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return element.getAttribute(attribute).contains(expectedValue);
            }
        });
    }
	
}

