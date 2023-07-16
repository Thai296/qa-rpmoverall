package com.overall.payor.payorGroupDemographics;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GroupDemographics {
	private WebDriverWait wait;
	protected Logger logger;
	
	public GroupDemographics(RemoteWebDriver driver) {
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" +driver );
	}
	
	/*Payor Group Demographics Load Page*/
	public WebElement payorGrpDemoLoadPgTitleTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
	}
	
	public WebElement payorGrpDemoLoadPgGrpIdDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_pyrGrpId")));
	}
	
	public WebElement payorGrpDemoLoadPgGrpDemoSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_group_demographics_load_group_id']")));
	}
	
	/*Payor Group Demographics Detail*/
	/** Header **/
	public WebElement headerHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_group_demographics_header']")));
	}
	
	public WebElement headerRunAuditIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}
	
	public WebElement headerGrpIdTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrGrpIdTxt")));
	}
	
	public WebElement headerGrpNmTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrGrpNameTxt")));
	}
	
	public WebElement headerContractedChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("contracted")));
	}
	
	/** Detail section **/
	public WebElement detailSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_group_demographics_detail']")));
	}	
	
	public WebElement detailSectionWOBasisDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_basis")));
	}	
	
	public WebElement detailSectionWOBasisChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_basis']//span[@class='select2-chosen']")));
	}	
	
	public WebElement detailSectionWOAgeInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("woAge")));
	}
	
	public WebElement detailSectionMinWOBalanceInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("minWoBal")));
	}
	
	public WebElement detailSectionManualDemandFormatDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_dataFmtManDemand")));
	}
	
	public WebElement detailSectionnManualDemandFormatChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_dataFmtManDemand']//span[@class='select2-chosen']")));
	}
	
	public WebElement detailSectionExclusionLogicChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("useOIG")));
	}
	
	public WebElement detailSectionGEMSLogicChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("useGEMSLogic")));
	}
	
	public WebElement detailSectionAnnualDisclLtrChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("printAnnualDisclLtr")));
	}
	
	public WebElement detailSectionIndigentDiscountNoteInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("note")));
	}
	
	/** Detail section - Pricing Suspended Grp **/
	public WebElement layoutMainpPayorGrpDemoGrphc() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='payorGrpDemoGrphcForm']//div[contains(@class,'layoutMainpPayorGrpDemoGrphc')]")));
	}

	public WebElement layoutLoadMainpPayorGrpDemoGrphc() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='step step2']")));
	}

	public WebElement PricingStEffDtInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcStEffDt")));
	}
	
	public WebElement PricingStExpDtInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcStExpDt")));
	}
	
	/** Detail section - Billing Suspended Grp **/
	public WebElement billingStEffDtInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("blngStEffDt")));
	}
	
	public WebElement billingStExpDtInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("blngStExpDt")));
	}
	
	/** Dunning Cycle section **/
	public WebElement dunningCycleTblAddIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_dunningCycle_iladd")));
	}
	
	public WebElement dunningCycleSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_group_demographics_dunning_cycle']")));
	}
	
	public WebElement dunningCycleTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_dunningCycle")));
	}
	
	public WebElement dunningCycleTblCycleColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_submCnt']")));
	}
	
	public WebElement dunningCycleTblDaysFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_daysToNextCycle")));
	}
	
	public WebElement dunningCycleTblDaysColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_daysToNextCycle']")));
	}
	
	public WebElement dunningCycleTblDaysColInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_daysToNextCycle']/input")));
	}
	
	public WebElement dunningCycleTblPrimaryServIdFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_submSvcPrimary")));
	}
	
	public WebElement dunningCycleTblPrimaryServIdColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_submSvcPrimary']")));
	}
	
	public WebElement dunningCycleTblPrimaryServIdColDdl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_submSvcPrimary']/div")));
	}
	
	public WebElement dunningCycleTblPrimaryServIdColChosenTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_submSvcPrimary']//span[@class='select2-chosen']")));
	}
	
	public WebElement dunningCycleTblNonPrimaryServIdFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_submSvcNonPrimary")));
	}
	
	public WebElement dunningCycleTblNonPrimaryServIdColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_submSvcNonPrimary']")));
	}
	
	public WebElement dunningCycleTblNonPrimaryServIdColDdl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_submSvcNonPrimary']/div")));
	}
	
	public WebElement dunningCycleTblNonPrimaryServIdColChosenTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_submSvcNonPrimary']//span[@class='select2-chosen']")));
	}
	
	public WebElement dunningCycleTblMessageFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_docId")));
	}
	
	public WebElement dunningCycleTblMessageColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_docId']")));
	}
	
	public WebElement dunningCycleTblMessageColDdl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_docId']/div")));
	}
	
	public WebElement dunningCycleTblMessageColChosenTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_docId']//span[@class='select2-chosen']")));
	}
	
	public WebElement dunningCycleTblDeletedColChk(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_dunningCycle']//tr["+row+"]/td[@aria-describedby='tbl_dunningCycle_deleted']/input")));
	}
	
	public WebElement dunningCycleTblTotalRecords(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_dunningCycle_pagernav_right']/div")));
	}
	
	
	/** Client Dunning Options **/
	public WebElement clnDunningOptionsSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_group_demographics_client_dunning_options']")));
	}
	
	public WebElement clnDunningOptionsSectionCompoundInterestChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("applyCompoundInterest")));
	}
	
	public WebElement clnDunningOptionsTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_clnDunningOptions")));
	}
	
	public WebElement clnDunningOptionsTblAgingBucketColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clnDunningOptions']//tr["+row+"]/td[@aria-describedby='tbl_clnDunningOptions_agingBucket']")));
	}
	
	public WebElement clnDunningOptionsTblDocColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clnDunningOptions']//tr["+row+"]/td[@aria-describedby='tbl_clnDunningOptions_docId']")));
	}
	
	public WebElement clnDunningOptionsTblDocColDdl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clnDunningOptions']//tr["+row+"]/td[@aria-describedby='tbl_clnDunningOptions_docId']/div")));
	}
	
	public WebElement clnDunningOptionsTblDocColChosen(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clnDunningOptions']//tr["+row+"]/td[@aria-describedby='tbl_clnDunningOptions_docId']//span[@class='select2-chosen']")));
	}
	
	public WebElement clnDunningOptionsTblMthRateColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clnDunningOptions']//tr["+row+"]/td[@aria-describedby='tbl_clnDunningOptions_mir']")));
	}
	
	public WebElement clnDunningOptionsTblMthRateColInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clnDunningOptions']//tr["+row+"]/td[@aria-describedby='tbl_clnDunningOptions_mir']/input")));
	}
	
	
	/** Procedure Code Override section **/
	public WebElement procCdOverrSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_group_demographics_procedure_code_override']")));
	}
	
	public WebElement procCdOverrTblProcedureCodeFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_procId")));
	}

	public WebElement procCdOverrTblSubmissionServiceFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_submSvcSeqId")));
	}
	
	public WebElement procCdOverrTblProcCdInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_procCdOverrides_procId']//input[contains(@id,'_procId')]")));
	}
	
	public WebElement procCdOverrTblSubmSvcSeqIdDDL(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_procCdOverrides_submSvcSeqId']//div[contains(@id,'submSvcSeqId')]")));
	}
	
	public WebElement procCdOverrTblProcCdSearchIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[contains(@data-search-target-fields,'procId')]")));
	}
	
	public WebElement procCdOverrDeleteCheckbox(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_procCdOverrides_deleted']//input[contains(@id,'deleted')]")));
	}
	
	public WebElement procCdOverrTblAddIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_procCdOverrides_iladd")));
	}
	
	public WebElement procCdOverrTblTotalRecords(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_procCdOverrides_pagernav_right']/div")));
	}
	
	public WebElement procCdOverridesTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_procCdOverrides")));
	}
	
	public WebElement rowInProcCdOverridesTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_procCdOverrides']//tr["+row+"]")));
	}
	/** Group Modifier Split Bill to Payor section **/
	public WebElement grpModSpitBillSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_group_demographics_group_modifier_split_bill']")));
	}
	
	public WebElement grpModSpitBilTbllModifierDDL(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_modifierBillToPayor']//*[@aria-describedby='tbl_modifierBillToPayor_modId']//div[contains(@id,'modId')]")));
	}
	
	public WebElement grpModSpitBillTblBillPyrIdInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_modifierBillToPayor']//*[@aria-describedby='tbl_modifierBillToPayor_splitPyrAbbrv']//input[contains(@id,'splitPyrAbbrv')]")));
	}
	
	public WebElement grpModSpitBillTblBillPyrIdTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_modifierBillToPayor']//tr["+row+"]/*[@aria-describedby='tbl_modifierBillToPayor_splitPyrAbbrv']")));
	}
	
	public WebElement grpModSpitBillTblModifierFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_modId")));
	}
	
	public WebElement grpModSpitBillTblBillPyrIdFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_splitPyrAbbrv")));
	}
	
	public WebElement grpModSpitBillTblBillPyrSearchIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[contains(@data-search-target-fields,'splitPyrAbbrv')]")));
	}
	
	public WebElement grpModSpitBillTblDeleteCheckbox(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_modifierBillToPayor']//*[@aria-describedby='tbl_modifierBillToPayor_deleted']//input[contains(@id,'_deleted')]")));
	}
	
	public WebElement grpModSpitBillTblAddBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_modifierBillToPayor_iladd")));
	}
	
	public WebElement grpModSpitBillTblTotalRecords(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_modifierBillToPayor_pagernav_right']/div")));
	}
	
	public WebElement deleteAtRowInGrpModSpitBillTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_modifierBillToPayor']//tr["+row+"]//input[@type='checkbox']")));
	}
	
	public WebElement grpModSpitBillTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_modifierBillToPayor")));
	}
	/** Footer **/
	public WebElement footerHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_group_demographics_summary']")));
	}
	
	public WebElement resetBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement saveAndClearBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	public WebElement sectionSearchFieldInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionSearchField")));
	}
	
	public WebElement confirmationDialogNameBtn(String nmBtn){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='confirmationDialog']//following-sibling::div//span[text()='"+nmBtn+"']")));
	}
	
	/*Audit Detail*/

	
}
