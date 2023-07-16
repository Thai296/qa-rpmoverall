package com.overall.client.demographics;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Demographics {
	private WebDriverWait wait;
	protected Logger logger;
	
	public Demographics(RemoteWebDriver driver) {
		this.wait = new WebDriverWait(driver,10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" +driver );
	}
	
	/* Client Demographics Load Page */
	
	public WebElement clientDemographicsLoadPgTitleTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
	}

	public WebElement clientDemographicsLoadClientSection(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'layoutMainpLookup')]")));
	}
	
	public WebElement clientDemographicsLoadClientIdInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientId")));
	}
	
	public WebElement clientDemographicsLoadClientIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientSrchBtn")));
	}
	
	public WebElement clientDemographicsLoadClientSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_load_client_id']")));
	}
	
	/* Client Demographics Detail */
	/*
	 * Header
	 */
	public WebElement clnDemographicsHeaderContent() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientDemographicsForm']//div[contains(@class,'clnDemographicsHeaderContent')]")));
	}

	public WebElement headerHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_header']")));
	}
	
	public WebElement headerClientIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientAbbrev")));
	}
	
	public WebElement headerClientNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientName")));
	}
	
	public WebElement headerAccountTypDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_accountType")));
	}

	public WebElement headerStartDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("startDate")));
	}
	
	public WebElement headerAnnualDisclosureLetterDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_annualDisclosureLetterMonth")));
	}
	
	public WebElement headerTaxIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("taxId")));
	}
	
	public WebElement headerEAVInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("eav")));
	}
	
	public WebElement headerDoNotRequireOrderEntryChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("doNotRequireOrderEntry")));
	}
	
	public WebElement headerPrimaryFacilityDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_primaryFac")));
	}
	
	public WebElement headerClientFacNpiInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientFacilityNpi")));
	}

	public WebElement headerClientFacNpiSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientFacilityNpi']/parent::div/preceding-sibling::div//a[@title='NPI Search']")));
	}
	
	public WebElement headerClientFacNpiGlobalSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientFacilityNpi']/parent::div/preceding-sibling::div//a[@title='NPI Global Search']")));
	}
	
	public WebElement headerClientFacNPPESSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientFacilityNpi']/parent::div/preceding-sibling::div//a[@title='NPPES Search']")));
	}
	
	public WebElement headerViewDocumentLnk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//a[@class='viewDocumentLabel']")));
	}
	
	public WebElement headerViewClientPortalDocUpDocumentLnk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//a[@class='viewOrganizationDocumentLabel']")));
	}
	
	public WebElement headerFacAbbrevTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='facilityHeaderInfo']//span[contains(@class,'clientOrderingFacAbbrev')]")));
	}
	
	public WebElement headerFacNameTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='facilityHeaderInfo']//span[contains(@class,'clientOrderingFacName')]")));
	}
	// copy Default from section
	public WebElement copyDefaultFromSectionClientIdInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientCopyId")));
	}
	
	public WebElement copyDefaultFromSectionSearchClientIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientCopySrchBtn")));
	}
	
	public WebElement copyDefaultFromSectionClientNameTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[contains(@class,'clientNameCopy')]")));
	}
	
	public WebElement copyDefaultFromSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_copy_defaults_from']")));
	}
	
	//  Nursing Home section 
	public WebElement nursingHomeSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_nursing_home']")));
	}
	
	public WebElement nursingHomeSectionPerformEligCensusCheckingChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("performEligibilityCensusChecking")));
	}
	
	public WebElement nursingHomeSectionCrossClientsChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("performCrossClients")));
	}
	
	public WebElement nursingHomeSectionPerformBillingAssignmentChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("performBillingAssignment")));
	}
	
	//  Comments  section
	public WebElement commentsSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_comments']")));
	}
	
	public WebElement commentsSectionGeneralCommentsInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("generalComments")));
	}
	
	public WebElement commentsSectionInternalCommentsInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("internalComments")));
	}
	
	// Cross References section
	public WebElement crossReferencesSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_cross_references']")));
	}
	
	public WebElement crossReferencesTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_crossReferences")));
	}
	
	public WebElement crossReferencesTblEffDateFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_effectiveDate")));
	}
	
	public WebElement crossReferencesTblExpDateFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_expirationDate")));
	}
	
	public WebElement crossReferencesTblCrossRefDescFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_xrefId")));
	}
	
	public WebElement crossReferencesTblRow(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr["+row+"]")));
	}
	
	public WebElement crossReferencesTblEffDateTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr["+row+"]/td[@aria-describedby='tbl_crossReferences_effectiveDate']")));
	}
	
	public WebElement crossReferencesTblExpDateTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr["+row+"]/td[@aria-describedby='tbl_crossReferences_expirationDate']")));
	}
	
	public WebElement crossReferencesTblCrossRefDescTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr["+row+"]/td[@aria-describedby='tbl_crossReferences_xrefId']")));
	}
	
	public WebElement crossReferencesTblDeletedChk(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr["+row+"]/td[@aria-describedby='tbl_crossReferences_deleted']/input")));
	}
	
	public WebElement crossReferencesTblEffDateInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr/td[@aria-describedby='tbl_crossReferences_effectiveDate']/input")));
	}
	
	public WebElement crossReferencesTblExpDateInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr/td[@aria-describedby='tbl_crossReferences_expirationDate']/input")));
	}
	
	public WebElement crossReferencesTblCrossRefDescDdl()throws Exception{
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr/td[@aria-describedby='tbl_crossReferences_xrefId']/div[contains(@id,'s2id')]")));
	}
	
	public WebElement crossReferencesTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_crossReferences_iladd")));
	}
	
	public WebElement crossReferencesTblTotalRecordsTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_crossReferences_pagernav_right']/div")));
	}
	
	public WebElement crossReferencesTblNextPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_crossReferences_pagernav")));
	}
	
	public WebElement crossReferencesTblLastPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_crossReferences_pagernav")));
	}
	
	public WebElement crossReferencesTblFirstPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_crossReferences_pagernav")));
	}
	
	public WebElement crossReferencesTblPreviousPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_crossReferences_pagernav")));
	}
	
	public WebElement crossReferencesTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences_pagernav_center']//input[@class='ui-pg-input']")));
	}
	
	public WebElement crossReferencesTblRownumSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences_pagernav_center']//select[@class='ui-pg-selbox']")));
	}
	
	// Billing Address and Contact Information section
	public WebElement billingAddressAndContactInfoSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_billing_address_and_contact_information']")));
	}

	public WebElement billingAddressAndContactInfoSectionContactOneInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrContact1")));
	}
	
	public WebElement billingAddressAndContactInfoSectionPrefContactMthdDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_billAddrPerferredContactMethod")));
	}
	
	public WebElement billingAddressAndContactInfoSectionPrefContactMthdChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_billAddrPerferredContactMethod']//span[@class='select2-chosen']")));
	}
	
	public WebElement billingAddressAndContactInfoSectionAddressOneInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrAddr1")));
	}
	
	public WebElement billingAddressAndContactInfoSectionAddressTwoInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrAddr2")));
	}
	public WebElement billingAddressAndContactInfoSectionCountryDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_billAddrCountry")));
	}
	
	public WebElement billingAddressAndContactInfoSectionCountryChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_billAddrCountry']//span[@class='select2-chosen']")));
	}
	
	public WebElement billingAddressAndContactInfoSectionPostalCodeInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrPostCode")));
	}
	
	public WebElement billingAddressAndContactInfoSectionPostalCodeSearchIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='billAddrPostCode']/parent::div/preceding-sibling::div//a")));
	}
	
	public WebElement billingAddressAndContactInfoSectionCityInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrCity")));
	}
	
	public WebElement billingAddressAndContactInfoSectionStateDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_billAddrState")));
	}
	
	public WebElement billingAddressAndContactInfoSectionStateChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_billAddrState']//span[@class='select2-chosen']")));
	}
	
	public WebElement billingAddressAndContactInfoSectionPhone1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrPhone1")));
	}
	
	public WebElement billingAddressAndContactInfoSectionFax1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrFax1")));
	}
	
	public WebElement billingAddressAndContactInfoSectionEmail1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrEmail1")));
	}
	
	
	public WebElement billingAddressAndContactInfoSectionContact2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrContact2")));
	}
	
	public WebElement billingAddressAndContactInfoSectionPhone2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrPhone2")));
	}
	
	public WebElement billingAddressAndContactInfoSectionFax2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrFax2")));
	}
	
	public WebElement billingAddressAndContactInfoSectionEmail2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrEmail2")));
	}
	
	//   Street Address and Contact Information section
	public WebElement streetAddressAndContactInfoSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_street_address_and_contact_information']")));
	}

	public WebElement streetAddressAndContactInfoSectionContact1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrContact1")));
	}
	
	public WebElement streetAddressAndContactInfoSectionPrefContactMthdDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_streetAddrPerferredContactMethod")));
	}
	
	public WebElement streetAddressAndContactInfoSectionPrefContactMthdChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_streetAddrPerferredContactMethod']//span[@class='select2-chosen']")));
	}
	
	public WebElement streetAddressAndContactInfoSectionCopyFromBillingAddrChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrCopy2Street")));
	}
	
	public WebElement streetAddressAndContactInfoSectionAddress1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrAddr1")));
	}
	
	public WebElement streetAddressAndContactInfoSectionAddress2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrAddr2")));
	}
	public WebElement streetAddressAndContactInfoSectionCountryDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_streetAddrCountry")));
	}
	
	public WebElement streetAddressAndContactInfoSectionCountryChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_streetAddrCountry']//span[@class='select2-chosen']")));
	}
	
	public WebElement streetAddressAndContactInfoSectionPostalCodeInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrPostCode")));
	}
	
	public WebElement streetAddressAndContactInfoSectionPostalCodeSearchIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='streetAddrPostCode']/parent::div/preceding-sibling::div//a")));
	}
	
	public WebElement streetAddressAndContactInfoSectionCityInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrCity")));
	}
	
	public WebElement streetAddressAndContactInfoSectionStateDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_streetAddrState")));
	}
	
	public WebElement streetAddressAndContactInfoSectionStateChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_streetAddrState']//span[@class='select2-chosen']")));
	}
	
	public WebElement streetAddressAndContactInfoSectionPhone1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrPhone1")));
	}
	
	public WebElement streetAddressAndContactInfoSectionFax1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrFax1")));
	}
	
	public WebElement streetAddressAndContactInfoSectionEmail1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrEmail1")));
	}
	
	public WebElement streetAddressAndContactInfoSectionContact2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrContact2")));
	}
	
	public WebElement streetAddressAndContactInfoSectionPhone2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrPhone2")));
	}
	
	public WebElement streetAddressAndContactInfoSectionFax2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrFax2")));
	}
	
	public WebElement streetAddressAndContactInfoSectionEmail2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("streetAddrEmail2")));
	}

	//  Correspondence Address and Contact Information section
	public WebElement correspondenceAddressAndContactInfoSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_correspondence_address_and_contact_information']")));
	}

	public WebElement correspondenceAddressAndContactInfoSectionContact1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correspAddrContact1")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionCopyFromBillingAddrChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrCopy2Correspondence")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionAddress1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correspAddrAddr1")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionAddress2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correspAddrAddr2")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionCountryDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_correspAddrCountry")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionCountryChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_correspAddrCountry']//span[@class='select2-chosen']")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionPostalCodeInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correspAddrPostCode")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionPostalCodeSearchIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='correspAddrPostCode']/parent::div/preceding-sibling::div//a")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionCityInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correspAddrCity")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionStateDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_correspAddrState")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionStateChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_correspAddrState']//span[@class='select2-chosen']")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionPhone1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correspAddrPhone1")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionFax1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correspAddrFax1")));
	}
	
	public WebElement correspondenceAddressAndContactInfoSectionEmail1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("correspAddrEmail1")));
	}
	
	//  Client Refund Address Information section	
	public WebElement clientRefundAddressInfoSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_client_refund_address_information']")));
	}

	public WebElement clientRefundAddressInfoSectionAddress1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnRefundAddrAddr1")));
	}
	
	public WebElement clientRefundAddressInfoSectionAddress2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnRefundAddrAddr2")));
	}
	
	public WebElement clientRefundAddressInfoSectionCountryDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_clnRefundAddrCountry")));
	}
	
	public WebElement clientRefundAddressInfoSectionCountryChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_clnRefundAddrCountry']//span[@class='select2-chosen']")));
	}
	
	public WebElement clientRefundAddressInfoSectionPostalCodeInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnRefundAddrPostCode")));
	}
	
	public WebElement clientRefundAddressInfoSectionPostalCodeSearchIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clnRefundAddrPostCode']/parent::div/preceding-sibling::div//a")));
	}
	
	public WebElement clientRefundAddressInfoSectionCityInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnRefundAddrCity")));
	}
	
	public WebElement clientRefundAddressInfoSectionStateDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_clnRefundAddrState")));
	}
	
	public WebElement clientRefundAddressInfoSectionStateChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_clnRefundAddrState']//span[@class='select2-chosen']")));
	}
	
	//  Shipping Address and Contact Information section
	public WebElement shippingAddressAndContactInfoSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_demographics_shipping_address_and_contact_information']")));
	}

	public WebElement shippingAddressAndContactInfoSectionContact1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shippingAddrContact1")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionCopyFromBillingAddrChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billAddrCopy2Shipping")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionAddress1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shippingAddrAddr1")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionAddress2Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shippingAddrAddr2")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionCountryDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_shippingAddrCountry")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionCountryChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_shippingAddrCountry']//span[@class='select2-chosen']")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionPostalCodeInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shippingAddrPostCode")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionPostalCodeSearchIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='shippingAddrPostCode']/parent::div/preceding-sibling::div//a")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionCityInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shippingAddrCity")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionStateDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_shippingAddrState")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionStateChosenTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_shippingAddrState']//span[@class='select2-chosen']")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionPhone1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shippingAddrPhone1")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionFax1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shippingAddrFax1")));
	}
	
	public WebElement shippingAddressAndContactInfoSectionEmail1Input(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shippingAddrEmail1")));
	}	
	
	// footer section
	
	public WebElement footerHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement footerResetBtn()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement footerSaveAndClearBtn()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	public WebElement footerSearchSectionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionSearchField")));
	}
	
	public WebElement warningPopupMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='confirmationDialog']//span")));
	}
	
	public WebElement warningPopupResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-describedby='confirmationDialog']//button[contains(@class,'btn_submit')]")));
	}
	
	public WebElement fieldValidationMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'messagefor')]//div[text()='Field Validation']/ancestor::div//div[contains(@class,'xf_message_content')]")));
	}
	
	public WebElement errorReturnMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='serverErrorsList']//p")));
	}

	public WebElement crossReferencesTblCrossRefTypeDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr/td[@aria-describedby='tbl_crossReferences_xrefTypeId']/div[contains(@id,'s2id')]")));
	}
	
	public WebElement crossReferencesTblMemberAbbrvDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr/td[@aria-describedby='tbl_crossReferences_xrefAbbrev']/div[contains(@id,'s2id')]")));
	}
	
	public WebElement crossReferencesTblCrossRefTypeIdFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_xrefTypeId")));
	}

	public WebElement crossReferencesTblMemberAbbrvFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_xrefAbbrev")));
	}
	
	public WebElement crossReferencesTblCrossRefTypeTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr["+row+"]/td[@aria-describedby='tbl_crossReferences_xrefTypeId']")));
	}
	
	public WebElement crossReferencesTblMemberAbbrvTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr["+row+"]/td[@aria-describedby='tbl_crossReferences_xrefAbbrev']")));
	}
	
	public WebElement crossReferencesTblNextDeletedChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_crossReferences']//tr/td[@aria-describedby='tbl_crossReferences_xrefAbbrev']/div[contains(@id,'s2id')]/ancestor::td/following-sibling::td[@aria-describedby='tbl_crossReferences_deleted']/input")));
	}
}