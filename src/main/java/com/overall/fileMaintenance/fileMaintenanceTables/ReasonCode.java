package com.overall.fileMaintenance.fileMaintenanceTables;

import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ReasonCode {
	private RemoteWebDriver driver;
	protected Logger logger;
	private SeleniumBaseTest b;
	private WebDriverWait wait;

	public ReasonCode(RemoteWebDriver driver, WebDriverWait wait)  {
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		this.wait=wait;
	}
	/*** Load Reason Code ***/
	public WebElement loadReasonCodePageTitle() {
		return driver.findElement(By.xpath(".//*[@id='fileMaintReasonCodeConfigForm']//span[@class='platormPageTitle']"));
	}
	public WebElement reasonCodeTitle()
	{
		return driver.findElement(By.cssSelector(".platormPageTitle"));
	}
	public WebElement errorGroupDropDown() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("s2id_errGrpId")));
	}

    public WebElement selectErrorGroupDropDown() {
        return driver.findElement(By.id("errGrpId"));
    }

	public WebElement reasonCodeTblIdInput() {
		return driver.findElement(By.id("reasonCdTableID"));
	}

	public WebElement csErrorTypeCd() {
		return driver.findElement(By.id("claimStatusErrorType"));
	}

	public WebElement reasonCodeTblNameInput() {
		return driver.findElement(By.id("reasonCdTableName"));
	}

	public WebElement reasonCodeTblIdSearchIcon() {
		return driver.findElement(By.id("reasoncodetableId"));
	}

	public WebElement reasonCodeIdSearchIcon() {
		return driver.findElement(By.id("reasoncodeId"));
	}

	public WebElement reasonCodeIdInput() {
		return driver.findElement(By.id("reasonCDID"));
	}

	public WebElement reasonCodeSectionLoadPageHelpIcon() {
		return driver.findElement(By.xpath(".//*[@data-help-id='p_reason_code_enter_reason_code']"));
	}

	/*** Header Section ***/
	public WebElement headerRunAuditBtn() {
		return driver.findElement(By.id("auditBtn"));
	}

	public WebElement headerHelpIcon() {
		return driver.findElement(By.xpath(".//*[@id='fileMaintReasonCodeConfigForm']//a[@data-help-id='p_reason_code_header']"));
	}

	public WebElement headerErrorGroupLbl() {
		return driver.findElement(By.id("lblErrorGroup"));
	}

	public WebElement headerReasonCodeIdLbl() {
		return driver.findElement(By.id("lblReasonCDID"));
	}

	public WebElement headerEffectiveDateSel() {
		return driver.findElement(By.id("effDt"));
	}

	public WebElement headerEffectiveDateInput() {
		return driver.findElement(By.id("txtEffDt"));
	}

	public WebElement headerDosEffectiveDateInput() {
		return driver.findElement(By.id("dosEffDt"));
	}

	public WebElement headerDeleteReasonCodeChkBox() {
		return driver.findElement(By.id("isDeleted"));
	}

	public WebElement headerReasonCodeTableId() {
		return driver.findElement(By.id("lblReasonCdTableID"));
	}

	public WebElement headerName() {
		return driver.findElement(By.id("lblReasonCdTableName"));
	}

	public WebElement headerNewEffectiveDateBtn() {
		return driver.findElement(By.xpath(".//*[@id='editEffDt']/span"));
	}

	/*** End of Header Section ***/

	/*** Detail Section - General ***/
	public WebElement detailSectionHelpIcon() {
		return driver.findElement(By.xpath(".//*[@data-help-id='p_reason_code_detail']"));
	}

	public WebElement detailSectionShortDescrInput() {
		return driver.findElement(By.id("shortDescr"));
	}

	public WebElement detailSectionDescrInput() {
		return driver.findElement(By.id("detlDescr"));
	}

	public WebElement detailSectionFieldDropDown() {
		return driver.findElement(By.id("s2id_fldId"));
	}

	public WebElement detailSectionFieldChosenDdl() {
		return driver.findElement(By.xpath(".//*[@id='s2id_fldId']//span[contains(@class,'chosen')]"));
	}

	public WebElement detailSectionClmStaErrTypInput() {
		return driver.findElement(By.id("csErrTypDescr"));
	}

	public WebElement detailSectionUseManualDmdFmForReSubmChkBox() {
		return driver.findElement(By.id("isResubUsingManFmt"));
	}

	public WebElement detailSectionNoActnCheckbox() {
		return driver.findElement(By.id("isNoActn"));
	}

	/*** Detail Section - Error Level Group ***/
	public WebElement errLvlGrpsRadByErrLvlName(String errLvlName) {
		return driver.findElement(By.xpath(".//*[@id='errorLvContainer']//label[contains(text(),'"+errLvlName+"')]//ancestor::div[@class='unit']//input"));
	}

	/*** Detail Section - ABN Related Reason Group ***/
	public WebElement abnRelatedReasonDropdown() {
		return driver.findElement(By.id("procCdSpcfc"));
	}

	public WebElement abnReltReasonGrpFnActRadByActName(String actName) {
		return driver.findElement(By.xpath(".//*[@id='errorLvContainer']/following-sibling::div//label[contains(text(),'"+actName+"')]/../parent::div//input"));
	}

	public WebElement abnSignedFinalActionAdjustmentCodeInput() {
		return driver.findElement(By.id("adjCdIdAbnSignedAbbrev"));
	}


	public WebElement abnReltReasonGrpAdjCdSearchIco() {
		return driver.findElement(By.xpath(".//*[@id='errorLvContainer']/following-sibling::div//a[contains(@title,'Adj') and contains(@title,'Search')]"));
	}

	/*** Detail Section - Prioritized Action Group ***/
	public WebElement priorActnGrpChkByActName(String actName) {
		return driver.findElement(By.xpath("//*[@id='prioritizedContainer']//label[contains(text(),'"+actName+"')]//ancestor::div[@class='unit']//input"));
	}

	public WebElement priorActnGrpCorrespondenceDropdown() {
		return driver.findElement(By.id("correspTypId"));
	}

	public WebElement priorActnGrpCorrespondenceTextInput() {
		return driver.findElement(By.id("ltrTxt"));
	}

	public WebElement priorActnGrpOutsideAgencyDropdown() {
		return driver.findElement(By.id("outAgncyId"));
	}

	public WebElement outsideAgencyPreCorrespDropdown() {
		return driver.findElement(By.id("outAgncyPreCorrespId"));
	}

	/*** Detail Section - Final Action Group ***/
	public WebElement finalActGrpActnRadByActName(String actName) {
		return driver.findElement(By.xpath(".//*[@id='prioritizedContainer']/following-sibling::div//label[contains(text(),'"+actName+"')]/../parent::div//input"));
	}

	public WebElement finalActGrpCollectionsSel() {
		return driver.findElement(By.id("collectnsSubmId"));
	}

	public WebElement finalActGrpAdjCdSearchIco() {
		return driver.findElement(By.xpath(".//*[@id='prioritizedContainer']/following-sibling::div//a[contains(@title,'Adj') and contains(@title,'Search')]"));
	}

	public WebElement finalActGrpRepricePyrSearchIco() {
		return driver.findElement(By.xpath(".//*[@id='prioritizedContainer']/following-sibling::div//span[contains(@data-search-target-fields,'repriceToSpecificPyrAbbrev')]"));
	}

	public WebElement finalActGrpRepriceSpecialPyrInput() {
		return driver.findElement(By.id("repriceToSpecificPyrAbbrev"));
	}

	public WebElement finalActGrpMoveToSpecialPyrSearchIco() {
		return driver.findElement(By.xpath(".//*[@id='prioritizedContainer']/following-sibling::div//span[contains(@data-search-target-fields,'specificPyrAbbrev')]"));
	}

	public WebElement finalActGrpMoveToPyrInput() {
		return driver.findElement(By.id("specificPyrAbbrev"));
	}

	/*** Detail Section - Cross Reference Table ***/
	public WebElement crossReferenceTbl() {
		return driver.findElement(By.id("tbl_reasonCodeCrossReferences"));
	}

	public WebElement crossReferenceTblHelpIco() {
		return driver.findElement(By.id("tbl_reasonCodeCrossReferences"));
	}

	public WebElement crossReferenceTblEffDtFilter() {
		return driver.findElement(By.id("gs_effDt"));
	}

	public WebElement crossReferenceTblExpDtFilter() {
		return driver.findElement(By.id("gs_expDt"));
	}

	public WebElement crossReferenceTblDerscFilter() {
		return driver.findElement(By.id("gs_xrefId"));
	}

	public WebElement crossReferenceTblEffDtInput() {
		return driver.findElement(By.xpath(".//*[@aria-describedby='tbl_reasonCodeCrossReferences_effDt']/input"));
	}

	public WebElement crossReferenceTblExpDtInput() {
		return driver.findElement(By.xpath(".//*[@aria-describedby='tbl_reasonCodeCrossReferences_expDt']/input"));
	}

	public WebElement crossReferenceTblDerscDdl() {
		return driver.findElement(By.xpath(".//*[@aria-describedby='tbl_reasonCodeCrossReferences_xrefId']/div"));
	}

	public WebElement crossReferenceTblDeleteCol(String croRefDescr) {
		return driver.findElement(By.xpath(".//table[@id='tbl_reasonCodeCrossReferences']//td[contains(text(),'"+croRefDescr+"')]//parent::tr/td[@aria-describedby='tbl_reasonCodeCrossReferences_deleted']/input"));
	}

	public WebElement crossReferenceTblEffDtCol(String row) {
		return driver.findElement(By.xpath(".//table[@id='tbl_reasonCodeCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_reasonCodeCrossReferences_effDt']"));
	}

	public WebElement crossReferenceTblExpDtCol(String row) {
		return driver.findElement(By.xpath(".//table[@id='tbl_reasonCodeCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_reasonCodeCrossReferences_expDt']"));
	}

	public WebElement crossReferenceTblCrossRefDescrCol(String row) {
		return driver.findElement(By.xpath(".//table[@id='tbl_reasonCodeCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_reasonCodeCrossReferences_xrefId']"));
	}

	public WebElement crossReferenceTblTotalResulLbl() {
		return driver.findElement(By.xpath(".//*[@id='tbl_reasonCodeCrossReferences_pagernav_right']/div"));
	}

	public WebElement crossReferenceTblAddBtn() {
		return driver.findElement(By.id("tbl_reasonCodeCrossReferences_iladd"));
	}

	public WebElement crossReferenceTblHelpIcon() {
		return driver.findElement(By.xpath(".//*[@data-help-id='p_reason_code_cross_reference']"));
	}

	public WebElement crossReferenceTblFirstPageIcon() {
		return driver.findElement(By.id("first_tbl_reasonCodeCrossReferences_pagernav"));
	}

	public WebElement crossReferenceTblPrevPageIcon() {
		return driver.findElement(By.id("prev_tbl_reasonCodeCrossReferences_pagernav"));
	}

	public WebElement crossReferenceTblNextPageIcon() {
		return driver.findElement(By.id("next_tbl_reasonCodeCrossReferences_pagernav"));
	}

	public WebElement crossReferenceTblLastPageIcon() {
		return driver.findElement(By.id("last_tbl_reasonCodeCrossReferences_pagernav"));
	}

	public WebElement crossReferenceTblTotalPagesText() {
		return driver.findElement(By.xpath("//span[contains(@id,'sp') and contains(@id,'tbl_reasonCodeCrossReferences_pagernav')]"));
	}

	public WebElement crossReferenceTblPageInput() {
		return driver.findElement(By.xpath("//*[contains(@id,'tbl_reasonCodeCrossReferences_pagernav_center')]//parent::td//following-sibling::input"));
	}

	public WebElement crossReferenceTblPageRowByDescr(String croRefDescr) {
		return driver.findElement(By.xpath(".//table[@id='tbl_reasonCodeCrossReferences']//td[contains(text(),'"+croRefDescr+"')]//parent::tr"));
	}

	/*** Override Section ***/
	public WebElement overridesSectionHelpIcon() {
		return driver.findElement(By.xpath(".//*[@id='mainSections']//a[@data-help-topic='Overrides']"));
	}

	public WebElement overridesSectionOverridesTbl() {
		return driver.findElement(By.id("tbl_overrides"));
	}

	public WebElement overridesSectionOverridesTblOverrideIdTxt(String row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_overrideDisplay']"));
	}

	public WebElement overridesSectionOverridesTblProcCodeTxt(String row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_procId']"));
	}

	public WebElement overridesSectionOverridesTblManFmtForReSubmitChk(String row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_resubUsingManFmt']/input"));
	}

	public WebElement overridesSectionOverridesTblAutoMatchChk(String row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_autoMatch']/input"));
	}

	public WebElement overridesSectionOverridesTblMatchCompareChk(String row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_matchCompare']/input"));
	}

	public WebElement overridesSectionOverridesTblManualChk(String row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_forceToMan']/input"));
	}

	public WebElement overridesSectionOverridesTblCorrespondenceTxt(String row) {
		//return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_correspTypId']/div/a"));
		return driver.findElement(By.xpath("/html/body/section/div/div/form/div[3]/div[1]/div/div[3]/div/div/section/div/div[2]/div/div/div[3]/div[3]/div/table/tbody/tr[2]/td[18]/div/a"));

	}

	public WebElement overridesSectionOverridesTblOutsiteAgencyTxt(String row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_outAgncyId']"));
	}

	public WebElement overridesSectionOverridesTblOutsiteAgencyPreCorrespTxt(String row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_outAgncyPreCorrespId']"));
	}

	public WebElement overridesSectionOverridesTblFinalActionTxt(String row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_finalActionDisplay']"));
	}

	public WebElement overridesSectionOverridesTblAuthorizedByTxt(String row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_userId']"));
	}

	public WebElement overridesSectionOverridesTblDeleteChkbox(int row) {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+row+"]/td[@aria-describedby='tbl_overrides_deleted']/input"));
	}

	public WebElement overridesSectionOverridesTblOverrideIdFilterInput() {
		return driver.findElement(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_overrideDisplay']"));
	}

	public WebElement overridesSectionOverridesTblProcCodeFilterInput() {
		return driver.findElement(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_procId']"));
	}

	public WebElement overridesSectionOverridesTblManFmtForReSubmitFilterInput() {
		return driver.findElement(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_resubUsingManFmt']"));
	}

	public WebElement overridesSectionOverridesTblAutoMatchFilterInput() {
		return driver.findElement(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_autoMatch']"));
	}

	public WebElement overridesSectionOverridesTblMatchCompareFilterInput() {
		return driver.findElement(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_matchCompare']"));
	}

	public WebElement overridesSectionOverridesTblManualFilterInput() {
		return driver.findElement(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_forceToMan']"));
	}

	public WebElement overridesSectionOverridesTblCorrespondenceFilterInput() {
		return driver.findElement(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_correspTypId']"));
	}

	public WebElement overridesSectionOverridesTblOutsideAgencyFilterInput() {
		return driver.findElement(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_outAgncyId']"));
	}

	public WebElement overridesSectionOverridesTblFinalActionFilterInput() {
		return driver.findElement(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_finalActionDisplay']"));
	}

	public WebElement overridesSectionOverridesTblAuthorizedByFilterInput() {
		return driver.findElement(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_userId']"));
	}

	public WebElement overridesSectionOverridesTblAddBtn() {
		return driver.findElement(By.xpath(".//*[@id='add_tbl_overrides']/button"));
	}

	public WebElement overridesSectionOverridesTblEditBtn() {
		return driver.findElement(By.xpath(".//*[@id='edit_tbl_overrides']/button"));
	}

	public WebElement overridesSectionOverridesTblFirstNavBtn() {
		return driver.findElement(By.xpath(".//*[@id='first_tbl_overrides_pagernav']/span"));
	}

	public WebElement overridesSectionOverridesTblPrevNavBtn() {
		return driver.findElement(By.xpath(".//*[@id='prev_tbl_overrides_pagernav']/span"));
	}

	public WebElement overridesSectionOverridesTblPageNumberInput() {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides_pagernav_center']//input"));
	}

	public WebElement overridesSectionOverridesTblNextNavBtn() {
		return driver.findElement(By.xpath(".//*[@id='next_tbl_overrides_pagernav']/span"));
	}

	public WebElement overridesSectionOverridesTblLastNavBtn() {
		return driver.findElement(By.xpath(".//*[@id='last_tbl_overrides_pagernav']/span"));
	}

	public WebElement overridesSectionOverridesTblPageSel() {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides_pagernav_center']//select"));
	}

	public WebElement overridesSectionOverridesTblTotalRepordTxt() {
		return driver.findElement(By.xpath(".//*[@id='tbl_overrides_pagernav_right']/div"));
	}

	public WebElement overridesSectionOverridesTblRow(String row) {
		return driver.findElement(By.xpath(".//table[@id='tbl_overrides']//tr["+row+"]"));
	}

	/*** End of Override Section ***/

	/*** Overrides Section > Add Record Popup ***/
	public WebElement overridesSectionAddRecordPopupPayorSearchBtn() {
		return driver.findElement(By.xpath(".//*[@id='tr_payorAbbrev']//span"));
	}

	public WebElement overridesSectionAddRecordPopupPayorIdInput() {
		return driver.findElement(By.id("payorAbbrev"));
	}

	public WebElement overridesSectionAddRecordPopupPayorNameInput() {
		return driver.findElement(By.id("payorName"));
	}

	public WebElement overridesSectionAddRecordPopupPayorGroupDdl() {
		return driver.findElement(By.xpath(".//*[@id='tr_pyrGrpId']//div"));
	}

	public WebElement overridesSectionAddRecordPopupClientSearchBtn() {
		return driver.findElement(By.xpath(".//*[@id='tr_clientAbbrev']//span"));
	}

	public WebElement overridesSectionAddRecordPopupClientIdInput() {
		return driver.findElement(By.id("clientAbbrev"));
	}

	public WebElement overridesSectionAddRecordPopupClientNameInput() {
		return driver.findElement(By.id("clientName"));
	}

	public WebElement overridesSectionAddRecordPopupClientAccountTypeDdl() {
		return driver.findElement(By.xpath(".//*[@id='s2id_clnAccntTypId']//a"));
	}

	public WebElement overridesSectionAddRecordPopupProcCodeSearchBtn() {
		return driver.findElement(By.xpath(".//*[@id='procedure_code_search_btn']/span"));
	}

	public WebElement overridesSectionAddRecordPopupProcCodeInput() {
		return driver.findElement(By.id("procId"));
	}

	public WebElement overridesSectionAddRecordPopupManFmtForReSubmitChk() {
		return driver.findElement(By.id("resubUsingManFmt"));
	}

	public WebElement overridesSectionAddRecordPopupAutoMatchChk() {
		return driver.findElement(By.id("autoMatch"));
	}

	public WebElement overridesSectionAddRecordPopupMatchCompareChk() {
		return driver.findElement(By.id("matchCompare"));
	}

	public WebElement overridesSectionAddRecordPopupManualChk() {
		return driver.findElement(By.id("forceToMan"));
	}

	public WebElement overridesSectionAddRecordPopupCorrespondenceDropdown() {
		return driver.findElement(By.id("correspTypId"));
	}

	public WebElement overridesSectionAddRecordPopupCorrespondenceTxt() {
		return driver.findElement(By.id("ltrTxt"));
	}

	public WebElement overridesSectionAddRecordPopupOutsideAgencyDropdown() {
		return driver.findElement(By.id("outAgncyId"));
	}

	public WebElement overridesSectionAddRecordPopupOutsideAgencyPreCorrespDropdown() {
		return driver.findElement(By.id("outAgncyPreCorrespId"));
	}

	public WebElement overridesSectionAddRecordPopupFinalActionDdl() {
		return driver.findElement(By.xpath(".//*[@id='tr_finalActnTyp']//div"));
	}

	public WebElement overridesSectionAddRecordPopupAuthorizedByTxt() {
		return driver.findElement(By.id("userId"));
	}

	public WebElement overridesSectionAddRecordPopupOkBtn() {
		return driver.findElement(By.id("sData"));
	}

	public WebElement overridesSectionAddRecordPopupCancelBtn() {
		return driver.findElement(By.id("cData"));
	}

	/*** End of Overrides Section > Add Record Popup ***/

	/*** Footer Section ***/
	public WebElement footerHelpIco() {
		return driver.findElement(By.id("pageHelpLink"));
	}

	public WebElement footerShowClipboardIco() {
		return driver.findElement(By.xpath(".//*[@id='fileMaintReasonCodeConfigForm']//div[@title='Show Clipboard']"));
	}

	public WebElement footerShowKeyboardShortcutIco() {
		return driver.findElement(By.xpath(".//*[@id='fileMaintReasonCodeConfigForm']//div[@title='Show Keyboard Shortcuts']"));
	}

	public WebElement footerSectionSearchInput() {
		return driver.findElement(By.id("sectionSearchField"));
	}

	public WebElement footerResetBtn() {
		return driver.findElement(By.id("Reset"));
	}

	public WebElement footerSaveAndClearBtn() {
		return driver.findElement(By.id("btnSaveAndClear"));
	}
	/*** End of Footer Section ***/

	/*** Warning Popup ***/
	public WebElement warningPopupResetBtn() {
		return driver.findElement(By.xpath(".//button/*[text()='Reset']"));
	}

	public WebElement warningPopupCancelBtn() {
		return driver.findElement(By.xpath(".//button/*[text()='Cancel']"));
	}
	/*** End Warning Popup ***/

	public WebElement reasonCodeCrossReferenceRowCountLabel() {
		return driver.findElement(By.xpath(".//*[@id='gbox_tbl_reasonCodeCrossReferences']//span[@class='rowCount']"));
	}

	public WebElement errorGroupDropdownTxt() {
		return driver.findElement(By.xpath(".//*[@id='s2id_errGrpId']//span[@class='select2-chosen']"));
	}

	public WebElement reasonCodeTableIdSearchIcon() {
		return driver.findElement(By.xpath(".//*[@data-search-type='reasoncodetable']"));
	}

	public WebElement reasonCodeTableSearchReasonCodeTableIDInput() {
		return driver.findElement(By.id("reasonCodeTableId"));
	}

	public WebElement noActionCheckbox() {
		return driver.findElement(By.id("isNoActn"));
	}

	public WebElement autoMatchCheckbox() {
		return driver.findElement(By.id("isAutoMtch"));
	}

	public WebElement matchCompareCheckbox() {
		return driver.findElement(By.id("isMtchCmp"));
	}

	public WebElement manualCheckbox() {
		return driver.findElement(By.id("isManActn"));
	}

	public WebElement clickManualCheckbox() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#prioritizedContainer #isManActn')[0]).click()");
	}

	public WebElement finalActionRadioBtn(int index) {
		return driver.findElement(By.id("finalActnId" + index));
	}

	public WebElement fieldDropdown() {
		return driver.findElement(By.id("fldId"));
	}

	public WebElement addBtnInOverrides() {
		return driver.findElement(By.id("add_tbl_overrides"));
	}

	public WebElement addBtnInCrossReference() {
		return driver.findElement(By.id("tbl_reasonCodeCrossReferences_iladd"));
	}

	public WebElement auditDetailTable() {
		return driver.findElement(By.id("tbl_auditlogwait"));
	}

	public WebElement auditDetailErrMsg() {
		return driver.findElement(By.id("errorMsg"));
	}

	public WebElement helpFileTitle()  {
		return driver.findElement(By.xpath("/html/body/h1"));
	}

	public WebElement effectiveDateDropDown() {
		return driver.findElement(By.xpath("//*[@id='effDt']/option[2]"));
	}

	public WebElement specificPayorIdInputInAddRecord() {
		return driver.findElement(By.id("specificPayorAbbrev"));
	}

	public WebElement crossReferenceDescriptionDropdown() {
		return driver.findElement(By.xpath(".//*[@aria-describedby='tbl_reasonCodeCrossReferences_xrefId']/select"));
	}

	public WebElement finalActionDropdownInAddRecord() {
		return driver.findElement(By.id("finalActnTyp"));
	}

	public WebElement finalActionDropdownInput() {
		return driver.findElement(By.xpath("//*[@id='s2id_finalActnTyp']/input"));
	}

    public WebElement fieldDropdownText()  {
        return driver.findElement(By.xpath("//*[@id='s2id_fldId']/a/span[1]"));
    }

    public WebElement finalActionLabelInDetail(int index) {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[for=\"finalActnId" + index + "\"]')[0]");
    }

    public WebElement correspondenceInOverridesTable(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_correspTypId']/div/span"));
    }

    public WebElement footerSection() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"layoutComponent layoutFooter sectionSpecial hideOnload\"]')[0]");
    }

    public WebElement payorNameInOverridesTableOverrideId(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_payorName']"));
    }

    public WebElement clientNameInOverridesTableOverrideId(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_clientName']"));
    }

	public WebElement abnSignedFinalActionRadioBtn(int index) {
        return driver.findElement(By.id("finalActnIdAbnSigned" + index));
    }

    public WebElement errorLevelRadioBtn(int index) {
        return driver.findElement(By.id("errLvlTypId" + index));
    }

    public WebElement errorLevelLabel(int index) {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[for=\"errLvlTypId" + index + "\"]')[0]");
    }

    public WebElement unbillableErrorTypeDropdown() {
	    return driver.findElement(By.id("errTypId"));
    }

	public WebElement deleteChkboxInCrossReferenceTable(int index) {
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='tbl_reasonCodeCrossReferences']//tr[" + index + "]/td[@aria-describedby='tbl_reasonCodeCrossReferences_deleted']/input")));
	}


    public WebElement reasonCoderErrorMessageInLoadPage() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"sectionTextBottom reasonCodeNotFoundMessageContainer\"]')[0]");
    }
    public void selectDropDownByErrorGroup(String reasonCode, WebDriverWait wait)
    {
        WebElement reasonCodeGrp = selectErrorGroupDropDown();
        wait.until(ExpectedConditions.elementToBeClickable(reasonCodeGrp));
        Select select = new Select(reasonCodeGrp);
        select.selectByVisibleText(reasonCode);
        logger.info("Selected Reason Code ID: " + reasonCode);
    }

	public void selectDropDownByErrorType(String csErrorTypeCd, WebDriverWait wait)
	{
		WebElement csErrorTypeCdGrp = csErrorTypeCd();
		wait.until(ExpectedConditions.elementToBeClickable(csErrorTypeCdGrp));
		Select select = new Select(csErrorTypeCdGrp);
		select.selectByVisibleText(csErrorTypeCd);
		logger.info("Selected Claim Status Error Type: " + csErrorTypeCd);
	}

    public WebElement overridesTableInOverridesSection(){
		return driver.findElement(By.xpath("//*[@id=\"tbl_overrides\"]/tbody/tr[2]/td[3]"));
		}
	public WebElement overridesTable() {
		return driver.findElement(By.id("gview_tbl_overrides"));
	}
	public WebElement overridesTableRow(){
		return driver.findElement(By.xpath("//*[@id=\"tbl_overrides\"]/tbody/tr[2]"));
	}

	public void overrideTableEditFirstRow(){
		overridesTableRow().click();
		logger.info("table row selected");
		overridesSectionOverridesTblEditBtn().click();
		logger.info("edit button clicked");
	}
	
}
