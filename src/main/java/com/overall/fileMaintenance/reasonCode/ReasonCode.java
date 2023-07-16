package com.overall.fileMaintenance.reasonCode;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReasonCode {
	private WebDriverWait wait;
	protected Logger logger;
		
	public ReasonCode(RemoteWebDriver driver, WebDriverWait wait)  {
		this.wait = wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*** Load Reason Code ***/
	public WebElement loadReasonCodePageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintReasonCodeConfigForm']//span[@class='platormPageTitle']")));
	}
	
	public WebElement layoutMainReasonCodeConfig() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class, 'layoutMainReasonCodeConfig')]")));
	}
	
	public WebElement reasonCodeErrGrpDDL() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_errGrpId")));
	}
	
	public WebElement reasonCodeTblIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("reasonCdTableID")));
	}
	
	public WebElement reasonCodeTblNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("reasonCdTableName")));
	}
	
	public WebElement reasonCodeTblIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("reasoncodetableId")));
	}
	
	public WebElement reasonCodeIdSearch() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("reasoncodeId")));
	}
	
	public WebElement reasonCodeIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("reasonCDID")));
	}
	
	public WebElement reasonCodeSectionLoadPageHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_reason_code_enter_reason_code']")));
	}
	
	/*** Header Section ***/
	public WebElement headerRunAuditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}
	
	public WebElement headerHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintReasonCodeConfigForm']//a[@data-help-id='p_reason_code_header']")));
	}
	
	public WebElement headerErrorGroupLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lblErrorGroup")));
	}
	
	public WebElement headerReasonCodeIdLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lblReasonCDID")));
	}
	
	public WebElement headerEffectiveDateSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("effDt")));
	}
	
	public WebElement headerEffectiveDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("txtEffDt")));
	}
	
	public WebElement headerDosEffectiveDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dosEffDt")));
	}
	
	public WebElement headerDeleteReasonCodeChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("isDeleted")));
	}
	
	public WebElement headerReasonCodeTableId() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lblReasonCdTableID")));
	}

	public WebElement headerName() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lblReasonCdTableName")));
	}
	
	public WebElement headerNewEffectiveDateBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='editEffDt']/span")));
	}
	
	/*** End of Header Section ***/

	/*** Detail Section - General ***/
	public WebElement detailSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_reason_code_detail']")));
	}
	
	public WebElement detailSectionShortDescrInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shortDescr")));
	}
	
	public WebElement detailSectionDescrInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("detlDescr")));
	}
	
	public WebElement detailSectionFieldDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_fldId")));
	}
	
	public WebElement detailSectionFieldChosenDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_fldId']//span[contains(@class,'chosen')]")));
	}
	
	public WebElement detailSectionClmStaErrTypInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("csErrTypDescr")));
	}
	
	public WebElement detailSectionUseManualDmdFmForReSubmChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("isResubUsingManFmt")));
	}
	
	public WebElement detailSectionNoActnChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("isNoActn")));
	}
	
	/*** Detail Section - Error Level Group ***/
	public WebElement errLvlGrpsRadByErrLvlName(String errLvlName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='errorLvContainer']//label[contains(text(),'"+errLvlName+"')]//ancestor::div[@class='unit']//input")));
	}
	
	/*** Detail Section - ABN Related Reason Group ***/
	public WebElement abnReltReasonGrpSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procCdSpcfc")));
	}
	
	public WebElement abnReltReasonGrpFnActRadByActName(String actName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='errorLvContainer']/following-sibling::div//label[contains(text(),'"+actName+"')]/../parent::div//input")));
	}
	
	public WebElement abnReltReasonGrpAdjCdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("adjCdIdAbnSignedAbbrev")));
	}
	
	public WebElement abnReltReasonGrpAdjCdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='errorLvContainer']/following-sibling::div//a[contains(@title,'Adj') and contains(@title,'Search')]")));
	}
	
	/*** Detail Section - Prioritized Action Group ***/
	public WebElement priorActnGrpChkByActName(String actName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='prioritizedContainer']//label[contains(text(),'"+actName+"')]//ancestor::div[@class='unit']//input")));
	}
	
	public WebElement priorActnGrpCorresSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correspTypId")));
	}
	
	public WebElement priorActnGrpCorresTxtInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ltrTxt")));
	}
	
	public WebElement priorActnGrpCorresOutAgncySel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("outAgncyId")));
	}
	
	/*** Detail Section - Final Action Group ***/
	public WebElement finalActGrpActnRadByActName(String actName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prioritizedContainer']/following-sibling::div//label[contains(text(),'"+actName+"')]/../parent::div//input")));
	}
	
	public WebElement finalActGrpCollectionsSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("collectnsSubmId")));
	}
	
	public WebElement finalActGrpAdjCdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prioritizedContainer']/following-sibling::div//a[contains(@title,'Adj') and contains(@title,'Search')]")));
	}
	
	public WebElement finalActGrpRepricePyrSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prioritizedContainer']/following-sibling::div//span[contains(@data-search-target-fields,'repriceToSpecificPyrAbbrev')]")));
	}
	
	public WebElement finalActGrpRepriceSpecialPyrInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("repriceToSpecificPyrAbbrev")));
	}
	
	public WebElement finalActGrpMoveToSpecialPyrSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prioritizedContainer']/following-sibling::div//span[contains(@data-search-target-fields,'specificPyrAbbrev')]")));
	}
	
	public WebElement finalActGrpMoveToPyrInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("specificPyrAbbrev")));
	}
	
	/*** Detail Section - Cross Reference Table ***/
	public WebElement crossReferenceTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_reasonCodeCrossReferences")));
	}
	
	public WebElement crossReferenceTblHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_reasonCodeCrossReferences")));
	}
	
	public WebElement crossReferenceTblEffDtFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_effDt")));
	}
	
	public WebElement crossReferenceTblExpDtFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_expDt")));
	}
	
	public WebElement crossReferenceTblDerscFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_xrefId")));
	}
	
	public WebElement crossReferenceTblEffDtInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_reasonCodeCrossReferences_effDt']/input")));
	}
	
	public WebElement crossReferenceTblExpDtInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_reasonCodeCrossReferences_expDt']/input")));
	}
	
	public WebElement crossReferenceTblDerscDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_reasonCodeCrossReferences_xrefId']/div")));
	}
	
	public WebElement crossReferenceTblDeleteCol(String croRefDescr) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_reasonCodeCrossReferences']//td[contains(text(),'"+croRefDescr+"')]//parent::tr/td[@aria-describedby='tbl_reasonCodeCrossReferences_deleted']/input")));
	}
	
	public WebElement crossReferenceTblEffDtCol(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_reasonCodeCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_reasonCodeCrossReferences_effDt']")));
	}
	
	public WebElement crossReferenceTblExpDtCol(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_reasonCodeCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_reasonCodeCrossReferences_expDt']")));
	}
	
	public WebElement crossReferenceTblCrossRefDesrcCol(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_reasonCodeCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_reasonCodeCrossReferences_xrefId']")));
	}
	
	public WebElement crossReferenceTblTotalResulLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_reasonCodeCrossReferences_pagernav_right']/div")));
	}
	
	public WebElement crossReferenceTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_reasonCodeCrossReferences_iladd")));
	}
	
	public WebElement crossReferenceTblHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_question_assignment']")));
	}
	
	public WebElement crossReferenceTblFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_reasonCodeCrossReferences_pagernav")));
	}
	
	public WebElement crossReferenceTblPrevPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_reasonCodeCrossReferences_pagernav")));
	}
	
	public WebElement crossReferenceTblNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_reasonCodeCrossReferences_pagernav")));
	}
	
	public WebElement crossReferenceTblLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_reasonCodeCrossReferences_pagernav")));
	}

	public WebElement crossReferenceTblTotalPagesText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@id,'sp') and contains(@id,'tbl_reasonCodeCrossReferences_pagernav')]")));
	}
	
	public WebElement crossReferenceTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'tbl_reasonCodeCrossReferences_pagernav_center')]//parent::td//following-sibling::input")));
	}
	
	public WebElement crossReferenceTblPageRowByDescr(String croRefDescr) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_reasonCodeCrossReferences']//td[contains(text(),'"+croRefDescr+"')]//parent::tr")));
	}
	
	/*** Override Section ***/
	public WebElement overridesSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']//a[@data-help-topic='Overrides']")));		
	}
	
	public WebElement overridesSectionOverridesTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_overrides")));		
	}
	
	public WebElement overridesSectionOverridesTblOverrideIdTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_overrideDisplay']")));		
	}
	
	public WebElement overridesSectionOverridesTblProcCodeTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_procId']")));		
	}
	
	public WebElement overridesSectionOverridesTblManFmtForReSubmitChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_resubUsingManFmt']/input")));		
	}
	
	public WebElement overridesSectionOverridesTblAutoMatchChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_autoMatch']/input")));		
	}
	
	public WebElement overridesSectionOverridesTblMatchCompareChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_matchCompare']/input")));		
	}
	
	public WebElement overridesSectionOverridesTblManualChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_forceToMan']/input")));		
	}
	
	public WebElement overridesSectionOverridesTblCorrespondenceTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_correspTypId']")));		
	}
	
	public WebElement overridesSectionOverridesTblOutsiteAgencyTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_outAgncyId']")));		
	}
	
	public WebElement overridesSectionOverridesTblFinalActionTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_finalActionDisplay']")));		
	}
	
	public WebElement overridesSectionOverridesTblAuthorizedByTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+ row +"]/td[@aria-describedby='tbl_overrides_userId']")));		
	}
	
	public WebElement overridesSectionOverridesTblDeleteChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides']//tr["+row+"]/td[@aria-describedby='tbl_overrides_deleted']/input")));		
	}
	
	public WebElement overridesSectionOverridesTblOverrideIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_overrideDisplay']")));		
	}
	
	public WebElement overridesSectionOverridesTblProcCodeFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_procId']")));		
	}
	
	public WebElement overridesSectionOverridesTblManFmtForReSubmitFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_resubUsingManFmt']")));		
	}
	
	public WebElement overridesSectionOverridesTblAutoMatchFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_autoMatch']")));		
	}
	
	public WebElement overridesSectionOverridesTblMatchCompareFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_matchCompare']")));		
	}
	
	public WebElement overridesSectionOverridesTblManualFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_forceToMan']")));		
	}
	
	public WebElement overridesSectionOverridesTblCorrespondenceFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_correspTypId']")));		
	}
	
	public WebElement overridesSectionOverridesTblOutsideAgencyFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_outAgncyId']")));		
	}
	
	public WebElement overridesSectionOverridesTblFinalActionFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_finalActionDisplay']")));		
	}
	
	public WebElement overridesSectionOverridesTblAuthorizedByFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_overrides']//input[@id='gs_userId']")));		
	}
	
	public WebElement overridesSectionOverridesTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_overrides']/button")));		
	}
	
	public WebElement overridesSectionOverridesTblEditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edit_tbl_overrides']/button")));		
	}
	
	public WebElement overridesSectionOverridesTblFirstNavBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_overrides_pagernav']/span")));		
	}
	
	public WebElement overridesSectionOverridesTblPrevNavBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_overrides_pagernav']/span")));		
	}
	
	public WebElement overridesSectionOverridesTblPageNumberInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides_pagernav_center']//input")));		
	}
	
	public WebElement overridesSectionOverridesTblNextNavBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_overrides_pagernav']/span")));		
	}
	
	public WebElement overridesSectionOverridesTblLastNavBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_overrides_pagernav']/span")));		
	}
	
	public WebElement overridesSectionOverridesTblPageSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides_pagernav_center']//select")));		
	}
		
	public WebElement overridesSectionOverridesTblTotalRepordTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_overrides_pagernav_right']/div")));		
	}
	
	public WebElement overridesSectionOverridesTblRow(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_overrides']//tr["+row+"]")));
	}
	
	/*** End of Override Section ***/

	/*** Overrides Section > Add Record Popup ***/
	public WebElement overridesSectionAddRecordPopupPayorSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_payorAbbrev']//span")));		
	}
	
	public WebElement overridesSectionAddRecordPopupPayorIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorAbbrev")));		
	}
	
	public WebElement overridesSectionAddRecordPopupPayorNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorName")));		
	}
	
	public WebElement overridesSectionAddRecordPopupPayorGroupDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_pyrGrpId']//div")));		
	}
	
	public WebElement overridesSectionAddRecordPopupClientSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_clientAbbrev']//span")));		
	}
	
	public WebElement overridesSectionAddRecordPopupClientIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientAbbrev")));		
	}
	
	public WebElement overridesSectionAddRecordPopupClientNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientName")));		
	}
	
	public WebElement overridesSectionAddRecordPopupClientAccountTypeDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_clnAccntTypId']//a")));		
	}
	
	public WebElement overridesSectionAddRecordPopupProcCodeSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='procedure_code_search_btn']/span")));		
	}
	
	public WebElement overridesSectionAddRecordPopupProcCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procId")));		
	}
	
	public WebElement overridesSectionAddRecordPopupManFmtForReSubmitChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("resubUsingManFmt")));		
	}
	
	public WebElement overridesSectionAddRecordPopupAutoMatchChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("autoMatch")));		
	}
	
	public WebElement overridesSectionAddRecordPopupMatchCompareChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("matchCompare")));		
	}
	
	public WebElement overridesSectionAddRecordPopupManualChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("forceToMan")));		
	}
	
	public WebElement overridesSectionAddRecordPopupCorrespondenceSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correspTypId")));		
	}
	
	public WebElement overridesSectionAddRecordPopupCorrespondenceTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ltrTxt")));		
	}
	
	public WebElement overridesSectionAddRecordPopupOutsideAgencySel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("outAgncyId")));		
	}
	
	public WebElement overridesSectionAddRecordPopupFinalActionDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_finalActnTyp']//div")));		
	}
	
	public WebElement overridesSectionAddRecordPopupAuthorizedByTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userId")));		
	}
	
	public WebElement overridesSectionAddRecordPopupOkBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));		
	}
	
	public WebElement overridesSectionAddRecordPopupCancelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));		
	}

	/*** End of Overrides Section > Add Record Popup ***/

	/*** Footer Section ***/
	public WebElement footerHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement footerShowClipboardIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintReasonCodeConfigForm']//div[@title='Show Clipboard']")));		
	}
	
	public WebElement footerShowKeyboardShortcutIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintReasonCodeConfigForm']//div[@title='Show Keyboard Shortcuts']")));		
	}
	
	public WebElement footerSectionSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionSearchField")));		
	}
	
	public WebElement footerResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));		
	}
	
	public WebElement footerSaveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));		
	}
	/*** End of Footer Section ***/
	
	/*** Warning Popup ***/
	public WebElement warningPopupResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//button/*[text()='Reset']")));		
	}
	
	public WebElement warningPopupCancelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//button/*[text()='Cancel']")));		
	}
	/*** End Warning Popup ***/
	
	public WebElement reasonCodeCrossReferenceRowCountLabel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gbox_tbl_reasonCodeCrossReferences']//span[@class='rowCount']")));
	}
	
	public WebElement reasonCodeErrGrpLabel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_errGrpId']//span[@class='select2-chosen']")));
	}
	
	public WebElement reasonCodeTableIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-search-type='reasoncodetable']")));
	}
	
	public WebElement reasonCodeTableSearchReasonCodeTableIDInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("reasonCodeTableId")));
	}
	
}
