package com.overall.accession.patientDemographics;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PatientDemographics {
	private WebDriverWait wait;
    protected Logger logger;
    protected RemoteWebDriver driver;

    public PatientDemographics(RemoteWebDriver driver, WebDriverWait wait) {
    	this.wait = wait;
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }

    /*
     * Patient Demographics load page
     */
     
    public WebElement ptDemoSection() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='sectionAccessionDailyReceipt']")));
    }

    public WebElement ptDemoSectionHelpIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-help-id='p_patient_demographics_load_patient_demographics_id']")));
    }

    public WebElement ptDemoSectionEPIInput() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("patientId")));
    }

    public WebElement ptDemoSectionEPISearchIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupDlyRcptSrchBtn")));
    }

    public WebElement ptDemoSectionCreateNewEPIIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span[class*='btnCreateNewEPI']")));
    }

    public WebElement ptDemoSectionSSNInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("patientSsn")));
    }

    /*
     * Create a new patient EPI link
     */
    public WebElement createNewPtEPIPopupLetSysGenerateRad() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='sysWillGenEPI']")));
    }

    public WebElement createNewPtEPIPopupPtLastNmInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='patientLNameForSysGenEPI']")));
    }

    public WebElement createNewPtEPIPopupCreateNewEPIRad() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='userWillGenEPI']")));
    }

    public WebElement createNewPtEPIPopupCreateNewEPIInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='userGenEPI']")));
    }

    public WebElement createNewPtEPIPopupCanCelBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-describedby='dlgCreateNewPatient'] button[class*='btn_submit'] + button")));
    }

    public WebElement createNewPtEPIPopupOkBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-describedby='dlgCreateNewPatient'] button[class*='btn_submit']")));
    }

    /*
     * Patient Demographics Detail page
     */
    public WebElement headerPageTitleTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='platormPageTitle']")));
    }

    public WebElement headerEpiTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("headerEPI")));
    }

    public WebElement headerSSNInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("headerSSN")));
    }

    public WebElement headerViewOrganizationDocumentLnk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[class='viewOrganizationDocumentLabel']")));
    }

    /*
     * Associated Patient IDs section
     */
    public WebElement associatedPtIDsTbl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_patientAssociatedPt")));
    }

    public WebElement associatedPtIDsTblPatientIdFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_associatedPtId")));
    }

    public WebElement associatedPtIDsTblSourceTypeFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_sourceTypeDisplay")));
    }

    public WebElement associatedPtIDsTblSourceIdFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_patientAssociatedPt']//*[@id='gs_sourceId']")));
    }

    public WebElement associatedPtIDsTblSourceNameFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_patientAssociatedPt']//*[@id='gs_sourceName']")));
    }

    public WebElement associatedPtIDsTblLongTermDiagFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_patientAssociatedPt']//*[@id='gs_longTermDiagnosis']")));
    }

    public WebElement associatedPtIDsTblOrderingPhysicianNpiFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_patientAssociatedPt']//*[@id='gs_orderingPhysicianNpi']")));
    }

    public WebElement associatedPtIDsTblOrderingPhysicianNameFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_patientAssociatedPt']//*[@id='gs_orderingPhysicianName']")));
    }

    public WebElement associatedPtIDsTblRow(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_patientAssociatedPt']//tr[" + row + "]")));
    }

    public WebElement associatedPtIDsTblPatientIdTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_patientAssociatedPt']//tr[" + row + "]//td[@aria-describedby='tbl_patientAssociatedPt_associatedPtId']")));
    }

    public WebElement associatedPtIDsTblSourceTypTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_patientAssociatedPt']//tr[" + row + "]//td[@aria-describedby='tbl_patientAssociatedPt_sourceTypeDisplay']")));
    }

    public WebElement associatedPtIDsTblSourceIdTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_patientAssociatedPt']//tr[" + row + "]//td[@aria-describedby='tbl_patientAssociatedPt_sourceId']")));
    }

    public WebElement associatedPtIDsTblSourceNmTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_patientAssociatedPt']//tr[" + row + "]//td[@aria-describedby='tbl_patientAssociatedPt_sourceName']/div")));
    }

    public WebElement associatedPtIDsTblLongTermDiagTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_patientAssociatedPt']//tr[" + row + "]//td[@aria-describedby='tbl_patientAssociatedPt_longTermDiagnosis']/div")));
    }

    public WebElement associatedPtIDsTblOrderingPhysNPITxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_patientAssociatedPt']//tr[" + row + "]//td[@aria-describedby='tbl_patientAssociatedPt_orderingPhysicianNpi']")));
    }

    public WebElement associatedPtIDsTblOrderingPhysNmTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_patientAssociatedPt']//tr[" + row + "]//td[@aria-describedby='tbl_patientAssociatedPt_orderingPhysicianName']")));
    }

    public WebElement associatedPtIDsTblDeleteChk(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_patientAssociatedPt']//tr[" + row + "]//td[@aria-describedby='tbl_patientAssociatedPt_deleted']/input")));
    }

    public WebElement associatedPtIDsTblAddBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("*[id='add_tbl_patientAssociatedPt'] > button")));
    }

    public WebElement associatedPtIDsTblEditBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("*[id='edit_tbl_patientAssociatedPt'] > button")));
    }

    public WebElement associatedPtIDsTblFirstPageBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_patientAssociatedPt_pagernav")));
    }

    public WebElement associatedPtIDsTblPrevPageBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_patientAssociatedPt_pagernav")));
    }

    public WebElement associatedPtIDsTblNextPageBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_patientAssociatedPt_pagernav")));
    }

    public WebElement associatedPtIDsTblLastPageBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_patientAssociatedPt_pagernav")));
    }

    public WebElement associatedPtIDsTblTotalPagesTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("*[id='tbl_patientAssociatedPt_pagernav_center'] span[id*='sp'][id*='tbl_patientAssociatedPt_pagernav']")));
    }

    public WebElement associatedPtIDsTblTotalResultTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("*[id='tbl_patientAssociatedPt_pagernav_right'] > div")));
    }

    public WebElement associatedPtIDsTblPatientIDInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='associatedPtId']")));
    }

    public WebElement associatedPtIDsTblSourceTypDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='s2id_sourceType']")));
    }

    public WebElement associatedPtIDsTblSourceIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='sourceIdByClnTemp']")));
    }

    public WebElement associatedPtIDsTblSourceIdDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id='s2id_sourceIdByFacTemp']")));
    }

    public WebElement associatedPtIDsTblLongTermDiagInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='longTermDiagnosis']")));
    }

    public WebElement associatedPtIDsTblOrderingPhysNPIInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='orderingPhysicianNpi']")));
    }

    public WebElement associatedPtIDsTblOrderingPhysNmInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id='orderingPhysicianName']")));
    }

    // begin Associated Patient popup elements
    public WebElement associatedPtIdsAddEditPopup() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_patientAssociatedPt")));
    }

    public WebElement associatedPtIdsAddEditPopupTitle() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='editmodtbl_patientAssociatedPt']//span[contains(@class,'ui-jqdialog-title')]")));
    }

    public WebElement associatedPtIdsAddEditPopupPatientIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='editmodtbl_patientAssociatedPt']//*[@id='associatedPtId']")));
    }

    public WebElement associatedPtIdsAddEditPopupSourceTypeDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='editmodtbl_patientAssociatedPt']//*[@id='s2id_sourceType']")));
    }

    public WebElement associatedPtIdsAddEditPopupSourceIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='editmodtbl_patientAssociatedPt']//*[@id='sourceIdByClnTemp']")));
    }

    public WebElement associatedPtIdsAddEditPopupSourceIdDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='editmodtbl_patientAssociatedPt']//*[@id='s2id_sourceIdByFacTemp']")));
    }

    public WebElement associatedPtIdsAddEditPopupSourceNameInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='editmodtbl_patientAssociatedPt']//*[@id='sourceName']")));
    }

    public WebElement associatedPtIdsAddEditPopupLongTermDiagSearchBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupDiagnosisSrchBtn")));
    }

    public WebElement associatedPtIdsAddEditPopupLongTermDiagInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='editmodtbl_patientAssociatedPt']//*[@id='longTermDiagnosis']")));
    }

    public WebElement associatedPtIdsAddEditPopupOrderingPhysNpiSearchBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupPhysicianSrchBtn")));
    }

    public WebElement associatedPtIdsAddEditPopupOrderingPhysNpiInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='editmodtbl_patientAssociatedPt']//*[@id='orderingPhysicianNpi']")));
    }

    public WebElement associatedPtIdsAddEditPopupOrderingPhysNameInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='editmodtbl_patientAssociatedPt']//*[@id='orderingPhysicianName']")));
    }

    public WebElement associatedPtIdsAddEditPopupOkBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
    }

    public WebElement associatedPtIdsAddEditPopupCancelBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
    }

    /*
     * Patient Information section
     */
    public WebElement ptInfoSectionHelpIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptFirstName")));
    }

    public WebElement ptInfoSectionFtNameInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptFirstName")));
    }

    public WebElement ptInfoSectionLtNameInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptLastName")));
    }

    public WebElement ptInfoSectionDOBInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptDateOfBirth")));
    }

    public WebElement ptInfoSectionGenderDdl() {
        return driver.findElement(By.id("ptGender"));
    }

    public WebElement ptInfoSectionGenderS2() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_ptGender")));
    }

    public WebElement ptInfoSectionMaritalStatusDdl() {
        return driver.findElement(By.id("ptMaritalStatus"));
    }

    public WebElement ptInfoSectionMaritalStatusS2() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_ptMaritalStatus")));
    }

    public WebElement ptInfoSectionCmtInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptComments")));
    }

    public WebElement ptInfoSectionDOSFormInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptDosOfMostRecentMspForm")));
    }

    public WebElement ptInfoSectionCreateBadAddressRecordChk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("suspendForBadAddress")));
    }

    public WebElement ptInfoSectionAddr1Input() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptAddress1")));
    }

    public WebElement ptInfoSectionAddr2Input() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptAddress2")));
    }

    public WebElement ptInfoSectionPostalCodeInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptPostalCode")));
    }

    public WebElement ptInfoSectionCityInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptCity")));
    }

    public WebElement ptInfoSectionStateDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_ptState")));
    }

    public WebElement ptInfoSectionStateTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[@id='s2id_ptState']//span")));
    }

    public WebElement ptInfoSectionCntryDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptCountry")));
    }

    public WebElement ptInfoSectionEmailInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptEmail")));
    }

    public WebElement ptInfoSectionHomePhnInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptHomePhone")));
    }

    public WebElement ptInfoSectionWorkPhnInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptWorkPhone")));
    }

    /*
     * Insurance Information section
     */
    public WebElement insuranceInfoSectionEffDtDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_selectedEffDt")));
    }

    public WebElement insuranceInfoSectionEffDtDdlArrow() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_selectedEffDt']//b")));
    }

    public WebElement insuranceInfoSectionEffDtDdlOption(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='select2-drop']/ul/li[" + row + "]/div")));
    }

    public WebElement insuranceInfoSectioEffDtInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("txtEffDt")));
    }

    public WebElement insuranceInfoSectionAddEffDtIcon() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("addEffDt")));
    }

    public WebElement insuranceInfoSectionclnBillCategoryDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#s2id_clientBillingCategory a")));
    }

    public WebElement insuranceInfoSectionDeleteEffDtChk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deleteEffDt")));
    }

    public WebElement insuranceInfoSectionSuspendedChk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("suspended")));
    }

    public WebElement insuranceInfoSectionAddPyrBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnAddPayor")));
    }

    /*
     * Insurance Information section Suspended Reason table
     */
    public WebElement suspendedReasonGroup() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_suspendedReason']//ancestor::div[contains(@class,'tblSuspended')]")));
    }

    public WebElement suspendedReasonTbl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_suspendedReason")));
    }

    public WebElement suspendedReasonTblDateInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("*[id='tbl_suspendedReason'] td[aria-describedby='tbl_suspendedReason_reasonDt'] > input")));
    }

    public WebElement suspendedReasonTblReasonDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("*[id='tbl_suspendedReason'] td[aria-describedby='tbl_suspendedReason_suspendReasonType'] > div")));
    }

    public WebElement suspendedReasonTblCellRow(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_suspendedReason']//tr[" + row + "]")));
    }

    public WebElement suspendedReasonTblDateTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_suspendedReason']//tr[" + row + "]//td[@aria-describedby='tbl_suspendedReason_reasonDt']")));
    }

    public WebElement suspendedReasonTblReasonTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_suspendedReason']//tr[" + row + "]//td[@aria-describedby='tbl_suspendedReason_suspendReasonType']")));
    }

    public WebElement suspendedReasonTblNoteTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_suspendedReason']//tr[" + row + "]//td[@aria-describedby='tbl_suspendedReason_note']/div")));
    }

    public WebElement suspendedReasonTblUserTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_suspendedReason']//tr[" + row + "]//td[@aria-describedby='tbl_suspendedReason_createUserId']")));
    }

    public WebElement suspendedReasonTblNoteInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("*[id='tbl_suspendedReason'] td[aria-describedby='tbl_suspendedReason_note'] textarea")));
    }

    public WebElement suspendedReasonTblUserInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("*[id='tbl_suspendedReason'] td[aria-describedby='tbl_suspendedReason_createUserId'] input")));
    }

    public WebElement suspendedReasonTblFixChk(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_suspendedReason']//tr[" + row + "]//td[@aria-describedby='tbl_suspendedReason_fix']/input")));
    }

    public WebElement suspendedReasonTblDeleteChk(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_suspendedReason']//tr[" + row + "]//td[@aria-describedby='tbl_suspendedReason_deleted']/input")));
    }

    public WebElement suspendedReasonTblDateFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_reasonDt")));
    }

    public WebElement suspendedReasonTblReasonFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_suspendReasonType")));
    }

    public WebElement suspendedReasonTblNoteFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_note")));
    }

    public WebElement suspendedReasonTblUserFilterInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_createUserId")));
    }

    public WebElement suspendedReasonTblTotalRecordTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_suspendedReason_pagernav_right']//div")));
    }

    /*
     * Insurance Information section Add Pyr section
     */
    public List<WebElement> listPyrTabs() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(".//*[@id='payorTabs']//li")));
    }

    public WebElement addPyrTab(String tabNum) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='payorTabs']//li[" + tabNum + "]")));
    }

    public WebElement addPyrTabName(String tabNum) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='payorTabs']//li[" + tabNum + "]//span[@class='tabPayorAbbrev']")));
    }

    public WebElement addPyrCloseTabIcon(String tabNum) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='payorTabs']//li[" + tabNum + "]//span[@class='ui-icon ui-icon-close']")));
    }

    public WebElement addPyrPopupTitleTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-describedby='dlgAddPayor']//*[@class='ui-dialog-title']")));
    }

    public WebElement addPyrPopupNewPyrInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userAddedPayor")));
    }

    public WebElement addPyrPopupNewPyrSearchIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id='dlgAddPayor'] a[title='Payor Search']")));
    }

    public WebElement addPyrPopupAddBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id='dlgAddPayor'] + div button[class*='btn_submit']")));
    }

    public WebElement addPyrPopupCancelBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id='dlgAddPayor'] + div button[class*='btn_submit'] + button")));
    }

    /*
     * Insurance Information section Add Pyr section Payor Info -----------------------------------------------------
     */
    public WebElement pyrInfoSectionPyrPrioInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='payorPrio']")));
    }

    public WebElement pyrInfoSectionPayoridInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='payorAbbrev']")));
    }

    public WebElement pyrInfoSectionPayornameTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] span[class*='payorName clearTextOnReset']")));
    }

    public WebElement pyrInfoSectionSubscriberIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='subscriberId']")));
    }

    public WebElement pyrInfoSectionGroupNameInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='groupName']")));
    }

    public WebElement pyrInfoSectionGroupIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='groupId']")));
    }

    public WebElement pyrInfoSectionPlanIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='planId']")));
    }

    public WebElement pyrInfoSectionCaseIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='caseId']")));
    }

    /*
     * Insurance Information section Add Payor section Insured Info -----------------------------------------------------
     */

    public WebElement insuredInfoSectionRelationshipDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='s2id_insuredRelationship']")));
    }

    public WebElement insuredInfoSectionFirstnameInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='insuredFirstName']")));
    }

    public WebElement insuredInfoSectionLastnameInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='insuredLastName']")));
    }

    public WebElement insuredInfoSectionDateOfBirthInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='insuredDateOfBirth']")));
    }

    public WebElement insuredInfoSectionGenderDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='s2id_insuredGender']")));
    }

    public WebElement insuredInfoSectionSsnInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='insuredSsn']")));
    }

    public WebElement insuredInfoSectionAddress1Input() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='insuredAddressLine1']")));
    }

    public WebElement insuredInfoSectionAddress2Input() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='insuredAddressLine2']")));
    }

    public WebElement insuredInfoSectionPostalCodeInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='insuredZip']")));
    }

    public WebElement insuredInfoSectionCityInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='insuredCity']")));
    }

    public WebElement insuredInfoSectionStateDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='s2id_insuredState']")));
    }

    public WebElement insuredInfoSectionCountryDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='s2id_insuredCountryCode']")));
    }

    public WebElement insuredInfoSectionHomePhoneInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='insuredHomePhone']")));
    }

    public WebElement insuredInfoSectionWorkPhoneInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='insuredWorkPhone']")));
    }

    /*
     * Insurance Information section Add Pyr section Payor Notes -----------------------------------------------------
     */
    public WebElement payorNotesSectionClaimNotesInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='claimNotes']")));
    }

    public WebElement payorNotesSectionInternalNotesInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='internalNotes']")));
    }

    public WebElement payorNotesSectionOtherInfo1Input() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='otherInfo1']")));
    }

    public WebElement payorNotesSectionOtherInfo2Input() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='otherInfo2']")));
    }

    public WebElement payorNotesSectionOtherInfo3Input() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='otherInfo3']")));
    }

    /*
     * Insurance Information section Add Pyr section Employer Info ----------------------------------------------------
     */
    public WebElement employerInfoSectionEmployerNameInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='employerName']")));
    }

    public WebElement employerInfoSectionEmploymentStatusDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='s2id_employerStatus']")));
    }

    public WebElement employerInfoSectionAddress1Input() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='employerAddressLine1']")));
    }

    public WebElement employerInfoSectionAddress2Input() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='employerAddressLine2']")));
    }

    public WebElement employerInfoSectionPostalCodeInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='employerZip']")));
    }

    public WebElement employerInfoSectionCityInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='employerCity']")));
    }

    public WebElement employerInfoSectionStateDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='s2id_employerState']")));
    }

    public WebElement employerInfoSectionCountryDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='s2id_employerCountryCode']")));
    }

    public WebElement employerInfoSectionWorkPhoneInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='employerPhone']")));
    }

    public WebElement employerInfoSectionFaxInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-expanded='true'] *[id='employerFax']")));
    }

    /*
     * All Accessions for this Patient table ----------------------------------------------------------------------------------------------------------
     */
    public WebElement allAccessionsTbl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_allAccessions")));
    }

    public WebElement eallAccessionsTblRow(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_allAccessions']//tr[" + row + "]")));
    }

    public WebElement allAccessionsTblDosTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions']//tr[" + row + "]//td[@aria-describedby='tbl_allAccessions_dos']")));
    }

    public WebElement allAccessionsTblAccnIdLnk(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions']//tr[" + row + "]//td[@aria-describedby='tbl_allAccessions_accnId']/a")));
    }

    public WebElement allAccessionsTblAccessionStatusTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions']//tr[" + row + "]//td[@aria-describedby='tbl_allAccessions_accnStatus']")));
    }

    public WebElement allAccessionsTblOrderingPhysicianTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions']//tr[" + row + "]//td[@aria-describedby='tbl_allAccessions_orderingPhysician']")));
    }

    public WebElement allAccessionsTblclientIdTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions']//tr[" + row + "]//td[@aria-describedby='tbl_allAccessions_clientAbbrev']")));
    }

    public WebElement allAccessionsTblPrimaryPayorTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions']//tr[" + row + "]//td[@aria-describedby='tbl_allAccessions_primaryPayorAbbrev']")));
    }

    public WebElement allAccessionsTblPaidAmountTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions']//tr[" + row + "]//td[@aria-describedby='tbl_allAccessions_paidAmount']")));
    }

    public WebElement allAccessionsTblAdjAmountTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions']//tr[" + row + "]//td[@aria-describedby='tbl_allAccessions_adjAmount']")));
    }

    public WebElement allAccessionsTblBalanceDueTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions']//tr[" + row + "]//td[@aria-describedby='tbl_allAccessions_balanceDue']")));
    }

    public WebElement allAccessionsTblStatementStatusTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions']//tr[" + row + "]//td[@aria-describedby='tbl_allAccessions_statementStatus']")));
    }

    public WebElement allAccessionsTblDosFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_allAccessions']//*[@id='gs_dos']")));
    }

    public WebElement allAccessionsTblAccessionFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_allAccessions']//*[@id='gs_accnId']")));
    }

    public WebElement allAccessionsTblAccessionStatusFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_allAccessions']//*[@id='gs_accnStatus']")));
    }

    public WebElement allAccessionsTblOrderingPhysicianFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_allAccessions']//*[@id='gs_orderingPhysician']")));
    }

    public WebElement allAccessionsTblclientIdFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_allAccessions']//*[@id='gs_clientAbbrev']")));
    }

    public WebElement allAccessionsTblPrimaryPayorFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_allAccessions']//*[@id='gs_primaryPayorAbbrev']")));
    }

    public WebElement allAccessionsTblPaidAmountFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_allAccessions']//*[@id='gs_paidAmount']")));
    }

    public WebElement allAccessionsTblAdjAmountFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_allAccessions']//*[@id='gs_adjAmount']")));
    }

    public WebElement allAccessionsTblBalanceDueFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_allAccessions']//*[@id='gs_balanceDue']")));
    }

    public WebElement allAccessionsTblStatementStatusFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_allAccessions']//*[@id='gs_statementStatus']")));
    }

    public WebElement allAccessionsTblTotalRecordsTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_allAccessions_pagernav_right']/div")));
    }

    public WebElement allAccessionsTblTotalPagesTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("*[id='tbl_allAccessions_pagernav_center'] span[id*='sp'][id*='tbl_allAccessions_pagernav']")));
    }

    public WebElement allAccessionsTblNextPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_allAccessions_pagernav")));
    }

    public WebElement allAccessionsTblLastPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_allAccessions_pagernav")));
    }

    public WebElement allAccessionsTblFirstPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_allAccessions_pagernav")));
    }

    public WebElement allAccessionsTblPreviousPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_allAccessions_pagernav")));
    }

    public WebElement allAccessionsTblPageInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_allAccessions_pagernav_center']//input[@class='ui-pg-input']")));
    }

    public WebElement allAccessionsTblRownumSel() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_allAccessions_pagernav_center']//select[@class='ui-pg-selbox']")));
    }

    public List<WebElement> allAccessionsTblAllRows() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("*[id='tbl_allAccessions'] tr[tabindex='-1']")));
    }

    /*
     * Dialysis Information section ------------------------------------------------------------------------------------------------------
     */

    public WebElement dialysisInformationDialysisTypDdl() {
        return driver.findElement(By.id("dialysisTypeId"));
    }

    public WebElement dialysisInformationDialysisTypS2() {
        return  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_dialysisTypeId")));
    }

    public WebElement dialysisInformationMedicationDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("medTypeId")));
    }

    public WebElement dialysisInformationFirstDateInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dateOfFirstDialysis")));
    }

    /*
     * RPM Dialysis Patient History section --------------------------------------------------------------------------------------------------
     */

    public WebElement rpmDialysisFromDtInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("start")));
    }

    public WebElement rpmDialysisThroughDtInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("end")));
    }

    public WebElement rpmDialysisGetRPMHistoryBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnGetRPMHistory")));
    }

    // RPM Dialysis Patient History table
    public WebElement rpmDialysisTbl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_xifinDialysisPtHistory")));
    }

    public WebElement rpmDialysisTblAddBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("*[id='tbl_xifinDialysisPtHistory'] > button")));
    }

    public WebElement rpmDialysisTblRow(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_xifinDialysisPtHistory']//tr[" + row + "]")));
    }

    public List<WebElement> rpmDialysisTblDataRow() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='tbl_xifinDialysisPtHistory']//tr[@tabindex]")));
    }

    public WebElement rpmDialysisTblAccessionIdTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_xifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_xifinDialysisPtHistory_accnId']")));
    }

    public WebElement rpmDialysisTblDosTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_xifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_xifinDialysisPtHistory_dos']")));
    }

    public WebElement rpmDialysisTblCompositeRoutineTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_xifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_xifinDialysisPtHistory_compositeRoutine']")));
    }

    public WebElement rpmDialysisTblDescriptionTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_xifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_xifinDialysisPtHistory_description']")));
    }

    public WebElement rpmDialysisTblModifiersTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_xifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_xifinDialysisPtHistory_modifiers']")));
    }

    public WebElement rpmDialysisTblAccessionIdFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_xifinDialysisPtHistory']//*[@id='gs_accnId']")));
    }

    public WebElement rpmDialysisTblDosFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_xifinDialysisPtHistory']//*[@id='gs_dos']")));
    }

    public WebElement rpmDialysisTblCompositeRoutineFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_xifinDialysisPtHistory']//*[@id='gs_compositeRoutine']")));
    }

    public WebElement rpmDialysisTblDescriptionFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_xifinDialysisPtHistory']//*[@id='gs_description']")));
    }

    public WebElement rpmDialysisTblModifiersFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_xifinDialysisPtHistory']//*[@id='gs_modifiers']")));
    }

    public WebElement rpmDialysisTblTotalRecordsTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_xifinDialysisPtHistory_pagernav_right']/div")));
    }

    public WebElement rpmDialysisTblNextPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_xifinDialysisPtHistory_pagernav")));
    }

    public WebElement rpmDialysisTblLastPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_xifinDialysisPtHistory_pagernav")));
    }

    public WebElement rpmDialysisTblFirstPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_xifinDialysisPtHistory_pagernav")));
    }

    public WebElement rpmDialysisTblPreviousPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_xifinDialysisPtHistory_pagernav")));
    }

    public WebElement rpmDialysisTblPageInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_xifinDialysisPtHistory_pagernav_center']//input[@class='ui-pg-input']")));
    }

    public WebElement rpmDialysisTblRownumSel() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_xifinDialysisPtHistory_pagernav_center']//select[@class='ui-pg-selbox']")));
    }

    public WebElement rpmDialysisTblTotalPagesLbl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='sp_1_tbl_xifinDialysisPtHistory_pagernav']")));
    }
    
    public WebElement rmpGetRMPHistoryProcessing(){
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messagefor_message0")));
    }

    /*
     * Non-RPM Dialysis Patient History section ---------------------------------------------------------------------------------
     */

    public WebElement nonRpmDialysisTblAddBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_nonXifinDialysisPtHistory_iladd")));
    }

    public WebElement nonRpmDialysisTbl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_nonXifinDialysisPtHistory")));
    }

    public WebElement nonRpmDialysisTblRow(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_nonXifinDialysisPtHistory']//tr[" + row + "]")));
    }

    public List<WebElement> nonRpmDialysisTblDataRow() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='tbl_nonXifinDialysisPtHistory']//tr[@tabindex]")));
    }

    public WebElement nonRpmDialysisTblDosTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_nonXifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_nonXifinDialysisPtHistory_dos']")));
    }

    public WebElement nonRpmDialysisTblCompositeRoutineTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_nonXifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_nonXifinDialysisPtHistory_compositeRoutine']")));
    }

    public WebElement nonRpmDialysisTblDescriptionTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_nonXifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_nonXifinDialysisPtHistory_description']")));
    }

    public WebElement nonRpmDialysisTblDeletedChk(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_nonXifinDialysisPtHistory']//tr[" + row + "]/td[@aria-describedby='tbl_nonXifinDialysisPtHistory_deleted']/input")));
    }

    public WebElement nonRpmDialysisTblDosInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_nonXifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_nonXifinDialysisPtHistory_dos']//input")));
    }

    public WebElement nonRpmDialysisTblCompositeRoutineSearchBtn(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_nonXifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_nonXifinDialysisPtHistory_compositeRoutine']//a")));
    }

    public WebElement nonRpmDialysisTblCompositeRoutineInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_nonXifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_nonXifinDialysisPtHistory_compositeRoutine']//input")));
    }

    public WebElement nonRpmDialysisTblDescriptionInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_nonXifinDialysisPtHistory']//tr[" + row + "]//td[@aria-describedby='tbl_nonXifinDialysisPtHistory_description']//input")));
    }

    public WebElement nonRpmDialysisTblDosFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_nonXifinDialysisPtHistory']//*[@id='gs_dos']")));
    }

    public WebElement nonRpmDialysisTblCompositeRoutineFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_nonXifinDialysisPtHistory']//*[@id='gs_compositeRoutine']")));
    }

    public WebElement nonRpmDialysisTblDescriptionFilterInput(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_nonXifinDialysisPtHistory']//*[@id='gs_description']")));
    }

    public WebElement nonRpmDialysisTblTotalRecordsTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_nonXifinDialysisPtHistory_pagernav_right']/div")));
    }

    public WebElement nonRpmDialysisTblNextPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_nonXifinDialysisPtHistory_pagernav")));
    }

    public WebElement nonRpmDialysisTblLastPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_nonXifinDialysisPtHistory_pagernav")));
    }

    public WebElement nonRpmDialysisTblFirstPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_nonXifinDialysisPtHistory_pagernav")));
    }

    public WebElement nonRpmDialysisTblPreviousPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_nonXifinDialysisPtHistory_pagernav")));
    }

    public WebElement nonRpmDialysisTblPageInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_nonXifinDialysisPtHistory_pagernav_center']//input[@class='ui-pg-input']")));
    }

    public WebElement nonRpmDialysisTblRownumSel() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_nonXifinDialysisPtHistory_pagernav_center']//select[@class='ui-pg-selbox']")));
    }

    public WebElement nonRpmDialysisTblTotalPagesLbl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='sp_1_tbl_nonXifinDialysisPtHistory_pagernav']")));
    }

    // NON-RPM Dialysis Patient History section
    public WebElement nonRpmDialysisChooseFileInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appendFile")));
    }

    public WebElement nonRpmDialysisChooseFileBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='fakeFileInput']/button")));
    }

    public WebElement nonRpmDialysisChooseFileNameTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='fakeFileInput']/span")));
    }

    public WebElement nonRpmDialysisAppendBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupDlyRcptSrchBtn")));
    }

    /*
     * Footer section and warning pop-up -------------------------------------------------------------------------------------------------------
     */
    public WebElement footerResetBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
    }

    public WebElement footerSaveAndClearBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
    }

    public WebElement warningPopupWarningTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='confirmationDialog']//span")));
    }

    public WebElement warningPopupResetBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//span[text()='Reset']/parent::button")));
    }

    public WebElement warningPopupCancelBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//span[text()='Cancel']/parent::button")));
    }

    public List<WebElement> errorsReturnedMessageTxts() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='sectionServerMessages']//ul[@class='serverErrorsList']//p")));
    }

    public WebElement footerSearchInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionSearchField")));
    }
}
