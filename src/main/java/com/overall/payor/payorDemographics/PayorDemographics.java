package com.overall.payor.payorDemographics;

import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PayorDemographics {
	protected Logger logger;
	private WebDriverWait wait;

	public PayorDemographics(RemoteWebDriver driver) {
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public WebElement payorDemographicsLoadPgTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
	}

	public WebElement payorDemographicsHeaderHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_payor_demographics_header']")));
	}

	public WebElement payorDemographicsHeaderRunAuditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}

	public WebElement payorDemographicsSectionLayout() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'lookupPayorDemographicsSection')]/parent::div")));
	}

	public WebElement payorDemographicsSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_payor_demographics_enter_payor_id']")));
	}

	public WebElement payorDemographicsSectionPyrIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupPyrAbbrevSrchBtn")));
	}

	public WebElement payorDemographicsSectionPyrIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupPyrAbbrv")));
	}

	public WebElement pyrIDInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupPyrAbbrv")));
	}

	public WebElement pyrNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrName")));
	}

	// Header
	public WebElement headerViewDocumentLnk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("viewDocumentLabel")));
	}

	public WebElement headerPayorIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrAbbrv")));
	}

	public WebElement headerPayorNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrName")));
	}

	public WebElement headerGroupNameDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_grpName")));
	}

	public WebElement headerGroupNameSelect() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("grpName")));
	}

	public WebElement headerGroupNameChosenTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_grpName']//span[contains(@class,'chosen')]")));
	}

	public WebElement headerEffectiveDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("setupDate")));
	}

	public WebElement headerWorkerCompChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("workCmp")));
	}

	public WebElement headerDoNotRequireOrderEntryChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("noRequireOrderEntry")));
	}

	public WebElement headerSignatureForCMSInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("signatureForCMS")));
	}

	public WebElement headerWebsiteInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("website")));
	}

	public WebElement headerExcludeFromSearchResultsInClnPortalChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("exclFromCpSrchRslts")));
	}

	public List<WebElement> listStates() {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='select2-drop']/ul/li/div/span")));
	}

	public WebElement state(String number) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='select2-drop']/ul/li[" + number + "]/div")));
	}

	// Order Entry Comments section

	public WebElement orderEntryCommentsSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_payor_demographics_order_entry_comments']")));
	}

	public WebElement orderEntryCommentsSectionOrderEntryCommentsInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("orderEntryComments")));
	}

	// Account Demographics section

	public WebElement accountDemographicsSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_payor_demographics_account_demographics']")));
	}

	public WebElement accountDemographicsTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_payorCrossReferenceInfo")));
	}

	public WebElement accountDemographicsTblEffectiveDateFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_effDate")));
	}

	public WebElement accountDemographicsTblExpirationDateFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_expDate")));
	}

	public WebElement accountDemographicsTblCrossRefDescFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_xRefId")));
	}

	public WebElement accountDemographicsTblEffectiveDateTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorCrossReferenceInfo']//tr[" + row + "]/td[@aria-describedby='tbl_payorCrossReferenceInfo_effDate']")));
	}

	public WebElement accountDemographicsTblExpirationDateTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorCrossReferenceInfo']//tr[" + row + "]/td[@aria-describedby='tbl_payorCrossReferenceInfo_expDate']")));
	}

	public WebElement accountDemographicsTblCrossRefDescTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorCrossReferenceInfo']//tr[" + row + "]/td[@aria-describedby='tbl_payorCrossReferenceInfo_xRefId']")));
	}

	public WebElement accountDemographicsTblDeletedChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorCrossReferenceInfo']//tr[" + row + "]/td[@aria-describedby='tbl_payorCrossReferenceInfo_deleted']/input")));
	}

	public WebElement accountDemographicsTblEffectiveDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorCrossReferenceInfo']//tr/td[@aria-describedby='tbl_payorCrossReferenceInfo_effDate']/input")));
	}

	public WebElement accountDemographicsTblExpirationDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorCrossReferenceInfo']//tr/td[@aria-describedby='tbl_payorCrossReferenceInfo_expDate']/input")));
	}

	public WebElement accountDemographicsTblCrossRefDescDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorCrossReferenceInfo']//tr/td[@aria-describedby='tbl_payorCrossReferenceInfo_xRefId']/div[contains(@id,'s2id')]")));
	}

	public WebElement accountDemographicsTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_payorCrossReferenceInfo_iladd")));
	}

	public WebElement accountDemographicsTblTotalRecordsTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorCrossReferenceInfo_pagernav_right']/div")));
	}

	public WebElement accountDemographicsTblNextPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_payorCrossReferenceInfo_pagernav")));
	}

	public WebElement accountDemographicsTblLastPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_payorCrossReferenceInfo_pagernav")));
	}

	public WebElement accountDemographicsTblFirstPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_payorCrossReferenceInfo_pagernav")));
	}

	public WebElement accountDemographicsTblPreviousPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_payorCrossReferenceInfo_pagernav")));
	}

	public WebElement accountDemographicsTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorCrossReferenceInfo_pagernav_center']//input[@class='ui-pg-input']")));
	}

	public WebElement accountDemographicsTblRownumSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_payorCrossReferenceInfo_pagernav_center']//select[@class='ui-pg-selbox']")));
	}

	// Detail Page Payor Address and Contact Information section
	public WebElement payorAddrAndContInfoSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_payor_demographics_payor_address']")));
	}

	public WebElement payorAddrAndContInfoSectionContactOneInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorContact")));
	}

	public WebElement payorAddrAndContInfoSectionPreferredContMethDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_payorPrefContactMethod")));
	}

	public WebElement payorAddrAndContInfoSectionPreferredContMethChosenTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_payorPrefContactMethod']//*[@class='select2-chosen']")));
	}

	public WebElement payorAddrAndContInfoSectionAddressOneInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorAddress1")));
	}

	public WebElement payorAddrAndContInfoSectionAddressTwoInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorAddress2")));
	}

	public WebElement payorAddrAndContInfoSectionCountryDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_payorCountry")));
	}

	public WebElement payorAddrAndContInfoSectionCountryChosenTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_payorCountry']//*[@class='select2-chosen']")));
	}

	public WebElement payorAddrAndContInfoSectionPostalCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorZip")));
	}

	public WebElement payorAddrAndContInfoSectionPostalCodeSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='payorZip']/parent::div/preceding-sibling::div//span")));
	}

	public WebElement payorAddrAndContInfoSectionCityInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorCity")));
	}

	public WebElement payorAddrAndContInfoSectionStateDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_payorState")));
	}

	public WebElement payorAddrAndContInfoSectionStateChosenTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_payorState']//*[@class='select2-chosen']")));
	}

	public WebElement payorAddrAndContInfoSectionPhoneOneInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorPhone1")));
	}

	public WebElement payorAddrAndContInfoSectionFaxOneInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorFax1")));
	}

	public WebElement payorAddrAndContInfoSectionEmailOneInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorEmail1")));
	}

	public WebElement payorAddrAndContInfoSectionContactTwoInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorContact2")));
	}

	public WebElement payorAddrAndContInfoSectionPhoneTwoInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorPhone2")));
	}

	public WebElement payorAddrAndContInfoSectionFaxTwoInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorFax2")));
	}

	public WebElement payorAddrAndContInfoSectionEmailTwoInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorEmail2")));
	}

	// Payor Refund Information section
	public WebElement payorRefundInfoSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_payor_demographics_payor_refund_information']")));
	}

	public WebElement payorRefundInfoSectionAddressOneInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refundAddress1")));
	}

	public WebElement payorRefundInfoSectionAddressTwoInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refundAddress2")));
	}

	public WebElement payorRefundInfoSectionCountryDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_refundCountry")));
	}

	public WebElement payorRefundInfoSectionCountryChosenTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_refundCountry']//*[@class='select2-chosen']")));
	}

	public WebElement payorRefundInfoSectionPostalCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refundZip")));
	}

	public WebElement payorRefundInfoSectionPostalCodeSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='refundZip']/parent::div/preceding-sibling::div//span")));
	}

	public WebElement payorRefundInfoSectionCityInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refundCity")));
	}

	public WebElement payorRefundInfoSectionStateDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_refundState")));
	}

	public WebElement payorRefundInfoSectionStateChosenTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_refundState']//*[@class='select2-chosen']")));
	}

	// Payor Appeal Information section
	public WebElement payorAppealInfoSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_payor_demographics_payor_appeal_information']")));
	}

	public WebElement payorAppealInfoSectionAppealNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appealName")));
	}

	public WebElement payorAppealInfoSectionAddressOneInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appealAddress1")));
	}

	public WebElement payorAppealInfoSectionAddressTwoInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appealAddress2")));
	}

	public WebElement payorAppealInfoSectionCountryDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_appealCountry")));
	}

	public WebElement payorAppealInfoSectionCountryChosenTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_appealCountry']//*[@class='select2-chosen']")));
	}

	public WebElement payorAppealInfoSectionPostalCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appealZip")));
	}

	public WebElement payorAppealInfoSectionPostalCodeSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='appealZip']/parent::div/preceding-sibling::div//span")));
	}

	public WebElement payorAppealInfoSectionCityInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appealCity")));
	}

	public WebElement payorAppealInfoSectionStateDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_appealState")));
	}

	public WebElement payorAppealInfoSectionFaxInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appealFax")));
	}

	public WebElement payorAppealInfoSectionAppealSubmissionIdDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_appealSubmissionId")));
	}

	// Attachment Information section
	public WebElement attachmentInfoSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_payor_demographics_attachment_information']")));
	}

	public WebElement attachmentInfoSectionAttachmentNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attachmentName")));
	}

	public WebElement attachmentInfoSectionAddressOneInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attachmentAddress1")));
	}

	public WebElement attachmentInfoSectionAddressTwoInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attachmentAddress2")));
	}

	public WebElement attachmentInfoSectionCountryDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_attachmentCountry")));
	}

	public WebElement attachmentInfoSectionPostalCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attachmentZip")));
	}

	public WebElement attachmentInfoSectionPostalCodeSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='attachmentZip']/parent::div/preceding-sibling::div//span")));
	}

	public WebElement attachmentInfoSectionCityInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attachmentCity")));
	}

	public WebElement attachmentInfoSectionStateDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_attachmentState")));
	}

	public WebElement attachmentInfoSectionStateChosenTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_attachmentState']//*[@class='select2-chosen']")));
	}

	public WebElement attachmentInfoSectionFaxInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attachmentFax")));
	}

	public WebElement attachmentInfoSectionAttchmentSubmIdDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_attachmentSubmissionId")));
	}

	public WebElement attachmentInfoSectionAttchmentSubmIdChosenTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_attachmentSubmissionId']//*[@class='select2-chosen']")));
	}

	// Remit Payor ID section
	public WebElement remitPayorIDSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_payor_demographics_remit_payor_id']")));
	}

	public WebElement remitPayorIDTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_remitPyrXrefInfo")));
	}

	public WebElement remitPayorIDTblRow(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_remitPyrXrefInfo']//tr[" + row + "]")));
	}

	public WebElement remitPayorIDTblRemitPayorIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_remitPyrXrefInfo")));
	}

	public WebElement pyrContact1Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PyrCnct")));
	}

	public WebElement pyrAddr1Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrAddr1")));
	}

	public WebElement pyrZipInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrZip")));
	}

	public WebElement remitPayorIDTblBankFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_bankId")));
	}

	public WebElement remitPayorIDTblRemitPayorIdLnk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_remitPyrXrefInfo']//tr[" + row + "]/td[@aria-describedby='tbl_remitPyrXrefInfo_eobExternalPyrId']/a")));
	}

	public WebElement remitPayorIDTblDefaultPayorChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_remitPyrXrefInfo']//tr[" + row + "]/td[@aria-describedby='tbl_remitPyrXrefInfo_defaultPyr']/input")));
	}

	public WebElement remitPayorIDTblBankTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_remitPyrXrefInfo']//tr[" + row + "]/td[@aria-describedby='tbl_remitPyrXrefInfo_bankId']")));
	}
	
	public WebElement pyrDemoErrText(int row1, int row2) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#pyrDemgrphc > table:nth-child(2) > tbody > tr:nth-child(" + row1 + ") > td:nth-child(" + row2 + ")")));
	}

	public WebElement groupNameDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PyrGrpN")));
	}

	public WebElement effDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SetupDt")));
	}

	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn_reset")));
	}

	public WebElement attachSubmID() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("attachSubmID")));
	}

	// -------------------------------------------------------------------------------------
	public void setEffDate(String str) {
		effDateInput().sendKeys(str);
		effDateInput().sendKeys(Keys.TAB);
		logger.info("        Entered Eff Date: " + str);
	}

	// -------------------------------------------------------------------------------------
	public void setPayrID(String str) throws Exception {
		pyrIDInput().sendKeys(str);
		pyrIDInput().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("        Entered Payor ID: " + str);
	}

	// -------------------------------------------------------------------------------------
	public void setPayrContact1(String str) throws Exception {
		pyrContact1Input().clear();
		pyrContact1Input().sendKeys(str);
		pyrContact1Input().sendKeys(Keys.TAB);
		logger.info("        Entered Payor Contact 1: " + str);
	}

	// -------------------------------------------------------------------------------------
	public void setPayrAddr1(String str) {
		pyrAddr1Input().clear();
		pyrAddr1Input().sendKeys(str);
		pyrAddr1Input().sendKeys(Keys.TAB);
		logger.info("        Entered Payor Addr1: " + str);
	}

	// -------------------------------------------------------------------------------------
	public void setPayrZip(String str) {
		pyrZipInput().clear();
		pyrZipInput().sendKeys(str);
		pyrZipInput().sendKeys(Keys.TAB);
		logger.info("        Entered Payor Zip: " + str);
	}

	// -------------------------------------------------------------------------------------
	public void setPayrName(String str) {
		pyrNameInput().sendKeys(str);
		pyrNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered Payor Name: " + str);
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setGroupName(String str) {
		Select oSelection = new Select(groupNameDropDown());
		oSelection.selectByValue(str);
		logger.info("        Selected Group Name: " + str);
	}

	public WebElement remitPayorIDTblRemitPayorIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_remitPyrXrefInfo']//tr/td[@aria-describedby='tbl_remitPyrXrefInfo_eobExternalPyrId']/input")));
	}

	public WebElement remitPayorIDTblBankDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_remitPyrXrefInfo']//tr/td[@aria-describedby='tbl_remitPyrXrefInfo_bankId']/div[contains(@id,'s2id')]")));
	}

	public WebElement remitPayorIDTblDeletedChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_remitPyrXrefInfo']//tr[" + row + "]/td[@aria-describedby='tbl_remitPyrXrefInfo_deleted']/input")));
	}

	public WebElement remitPayorIDTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_remitPyrXrefInfo_iladd")));
	}

	public WebElement remitPayorIDTblTotalRecordsTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_remitPyrXrefInfo_pagernav_right']/div")));
	}

	public WebElement remitPayorIDTblNextPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_remitPyrXrefInfo_pagernav")));
	}

	public WebElement remitPayorIDTblLastPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_remitPyrXrefInfo_pagernav")));
	}

	public WebElement remitPayorIDTblFirstPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_remitPyrXrefInfo_pagernav")));
	}

	public WebElement remitPayorIDTblPreviousPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_remitPyrXrefInfo_pagernav")));
	}

	public WebElement remitPayorIDTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_remitPyrXrefInfo_pagernav_center']//input[@class='ui-pg-input']")));
	}

	public WebElement remitPayorIDTblRownumSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_remitPyrXrefInfo_pagernav_center']//select[@class='ui-pg-selbox']")));
	}

	// Client Billing Categories section
	public WebElement clientBillingCategoriesSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_payor_demographics_client_billing_categories']")));
	}

	public WebElement clientBillingCategoriesTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_clientBillingCategories")));
	}

	public WebElement clientBillingCategoriesTblIDFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_cbcId")));
	}

	public WebElement clientBillingCategoriesTblDescriptionFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_cbcDescr")));
	}

	public WebElement clientBillingCategoriesTblIdTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clientBillingCategories']//tr[" + row + "]/td[@aria-describedby='tbl_clientBillingCategories_cbcId']")));
	}

	public WebElement clientBillingCategoriesTblDescriptionTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clientBillingCategories']//tr[" + row + "]/td[@aria-describedby='tbl_clientBillingCategories_cbcDescr']")));
	}

	public WebElement clientBillingCategoriesTblDeletedChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clientBillingCategories']//tr[" + row + "]/td[@aria-describedby='tbl_clientBillingCategories_deleted']/input")));
	}

	public WebElement clientBillingCategoriesTblIdDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clientBillingCategories']//tr/td[@aria-describedby='tbl_clientBillingCategories_cbcId']/div[contains(@id,'s2id')]")));
	}

	public WebElement clientBillingCategoriesTblIdChosenTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clientBillingCategories']//tr/td[@aria-describedby='tbl_clientBillingCategories_cbcId']/div[contains(@id,'s2id')]//*[@class='select2-chosen']")));
	}

	public WebElement clientBillingCategoriesTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_clientBillingCategories_iladd")));
	}

	public WebElement clientBillingCategoriesTblTotalRecordsTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientBillingCategories_pagernav_right']/div")));
	}

	public WebElement clientBillingCategoriesTblNextPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_clientBillingCategories_pagernav")));
	}

	public WebElement clientBillingCategoriesTblLastPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_clientBillingCategories_pagernav")));
	}

	public WebElement clientBillingCategoriesTblFirstPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_clientBillingCategories_pagernav")));
	}

	public WebElement clientBillingCategoriesTblPreviousPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_clientBillingCategories_pagernav")));
	}

	public WebElement clientBillingCategoriesTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clientBillingCategories_pagernav_center']//input[@class='ui-pg-input']")));
	}

	public WebElement clientBillingCategoriesTblRownumSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clientBillingCategories_pagernav_center']//select[@class='ui-pg-selbox']")));
	}

	public WebElement clientBillingCategoriesTblRow(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clientBillingCategories']//tr[" + row + "]")));
	}

	// FOOTER
	public WebElement footerHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
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

	public WebElement ServerErrorMessageSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionServerMessages")));
	}

	public List<WebElement> listServerErrorTxt() {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='sectionServerMessages']//ul[contains(@class,'serverErrorsList')]/li")));
	}

	public List<WebElement> fieldValidationForEmailContextTxt() {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='messagefor_payorEmail1']//div[contains(@class,'message_content')]")));
	}

	public List<WebElement> fieldValidationForEmailTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='messagefor_payorEmail1']//div[contains(@class,'message_handle')]")));
	}

	// Warning popup
	public WebElement warningPopupMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='confirmationDialog']//span")));
	}

	public WebElement warningPopupResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-describedby='confirmationDialog']//button[contains(@class,'btn_submit')]")));
	}

	public WebElement fieldValidationMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'messagefor')]/div[contains(@class,'message_content')]")));
	}
	
	public WebElement submitBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn_submit")));
	}
	
	public void submit() throws Exception {
		submitBtn().click();
		Thread.sleep(3000);
		logger.info("       Clicked Submit button");
	}
}
