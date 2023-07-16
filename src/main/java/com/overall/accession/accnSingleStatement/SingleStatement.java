package com.overall.accession.accnSingleStatement;

import com.xifin.qa.page.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SingleStatement extends BasePage
{
    private static final String SELECT2_ID_CLAIM_TYPE = "s2id_claimTyp";

    public SingleStatement(RemoteWebDriver driver, WebDriverWait wait, String methodName)
    {
        super(driver, wait, methodName);
    }

    // Header section
    public WebElement singleStatementHeaderTitleTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
    }

    public WebElement singleStatementHeaderHelpIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_accession_single_stmt_header']")));
    }

    // Client Statement section
    public WebElement clientStatementSubmDate() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'clientStatement')]//*[contains(@class,'clnSubmDt') and contains(@class,'dataDisplay')]")));
    }

    public WebElement clientStatementSubmfile() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'clientStatement')]//*[@id='submFile']/span")));
    }

    // Load Accession Section
    public WebElement singleStatementLoadVisitHelpIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_accession_single_stmt_load_visit']")));
    }

    public WebElement loadAccnSecHelpIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_accession_single_statement_load_accession']")));
    }

    public WebElement loadAccnSecSearchIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupAccnSrchBtn")));
    }

    public WebElement loadAccnSecAccnIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupAccnId")));
    }
    public WebElement accnIdHeader() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("accnIdHeader")));
    }

    // Header section after load accn id

    public WebElement singleStatementAccnSearchBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[class*= 'btnAccnSearchHeader']")));
    }

    public WebElement singleStatementHeaderViewClientPortalDocLnk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class ='viewOrganizationDocumentLabel']")));
    }

    public WebElement singleStatementHeaderViewDocLnk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class ='viewDocumentLabel']")));
    }

    public WebElement singleStatementHeaderLinkedLnk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id ='btnLinkedAccns']")));
    }

    public WebElement singleStatementHeaderFacAbbrevTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@class,'facAbbrv')]")));
    }

    public WebElement singleStatementHeaderFacNameTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@class,'facName')]")));
    }

    public WebElement singleStatementHeaderAccnIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name = 'accnId']")));
    }

    public WebElement singleStatementHeaderClientIdTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'clnId')]")));
    }

    public WebElement singleStatementHeaderClientNameTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnNm")));
    }

    public WebElement singleStatementHeaderReqIdTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[substring(@class, string-length(@class) - 4) = 'reqId']")));
    }

    public WebElement singleStatementHeaderPatientNameTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptFullName")));
    }

    public WebElement singleStatementHeaderEpiTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'ptId')]")));
    }

    public WebElement singleStatementHeaderDateOfBirthTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'ptDob')]")));
    }

    public WebElement singleStatementHeaderAgeOnDOSTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@class,'ageOnD')]")));
    }

    public WebElement singleStatementHeaderGenderTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'ptSex')]")));
    }

    public WebElement singleStatementHeaderDateOfServiceTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'dos')]")));
    }

    public WebElement singleStatementHeaderFinalReportDateTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'finalRptDt')]")));
    }

    public WebElement singleStatementHeaderPriceDateTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'prcDt')]")));
    }

    public WebElement singleStatementHeaderOrderingPhysicianTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("orderingPhys")));
    }

    public WebElement singleStatementHeaderAccessionStatusTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'accnStatus')]")));
    }

    public WebElement singleStatementHeaderStatementStatusTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'stmtStatus')]")));
    }

    public WebElement singleStatementHeaderOriginalBalanceTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'originalBalance')]")));
    }

    public WebElement singleStatementHeaderBalanceDueTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'dueAmt')]")));
    }

    // Linked Accessions popup
    public WebElement LinkedAccessionsPopupTitleTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"messagefor_btnLinkedAccns\"]//*[@class=\"xf_message_title\"]")));
    }

    public WebElement LinkedAccessionsPopupLinkedAccessionLnk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"messagefor_btnLinkedAccns\"]//*[@class=\"linkedAccessionsList\"]//li[not(contains(@class,'hideOnload'))]//a")));
    }

    // Client Statement Section
    public WebElement ClientStatementSecTitleTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'clientStatement')]//span[@class='titleText']")));
    }

    // Current billed payors section

    public WebElement currentBilledPayorsSecHelpIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_accession_single_stmt_current_billed_payors']")));
    }

    public WebElement currentBilledPayorsSecNoteInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("notes")));
    }

    public WebElement currentBilledPayorsSecPrinterConfigDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("printerConfig")));
    }

    public WebElement currentBilledPayorsSecProcsWithBalanceRad() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procsWithBal")));
    }

    public WebElement currentBilledPayorsSecProcsWithoutBalanceRad() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procsWithoutBal")));
    }

    public WebElement currentBilledPayorsSecSendToPtStmtVendorChk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sendToPtStmtVendor")));
    }

    public WebElement currentBilledPayorsSecDoNotDisplayPostedPmtsAdjsChk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("doNotDisplayPostedPmtsAdjs")));
    }

    public WebElement currentBilledPayorsSecIcnDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_icn")));
    }

    public WebElement currentBilledPayorsSecWillSubmitChk() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("willSubmit")));
    }

    public WebElement currentBilledPayorsSecInstitutionalBillTypeDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_claimTyp")));
    }

    public WebElement currentBilledPayorsSecInstitutionalBillTypeDdlChosen() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_claimTyp']//*[@class='select2-chosen']")));
    }

    public WebElement currentBilledPayorsSecConditionCodeDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_ub92CodeTyp")));
    }

    // Current billed payors Tbl

    public WebElement currentBilledPayorsTbl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_currentBilledPayors")));
    }

    public WebElement currentBilledPayorsTblDataRow(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//tr[" + row + "]")));
    }

    public WebElement currentBilledPayorsTblPayorPriorityCol(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//tr[" + row + "]/*[@aria-describedby='tbl_currentBilledPayors_prio']")));
    }

    public WebElement currentBilledPayorsTblPayorIdCol(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//tr[" + row + "]/*[@aria-describedby='tbl_currentBilledPayors_pyrAbbrv']")));
    }

    public WebElement currentBilledPayorsTblPayorIdColByText(String payorAbbrev) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//*[@aria-describedby='tbl_currentBilledPayors_pyrAbbrv'][@title,'" + payorAbbrev + "']")));
    }

    public WebElement currentBilledPayorsTblDueAmountCol(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//tr[" + row + "]/*[@aria-describedby='tbl_currentBilledPayors_amtDue']")));
    }

    public WebElement currentBilledPayorsTblStatementTypeColTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//tr[" + row + "]/*[@aria-describedby='tbl_currentBilledPayors_submissionSvcId']")));
    }

    public WebElement currentBilledPayorsTblStatementTypeColDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//*[@aria-describedby='tbl_currentBilledPayors_submissionSvcId']/div")));
    }

    public WebElement currentBilledPayorsTblAppealLetterColTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//tr[" + row + "]/*[@aria-describedby='tbl_currentBilledPayors_appealLetter']")));
    }

    public WebElement currentBilledPayorsTblAppealLetterColDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//*[@aria-describedby='tbl_currentBilledPayors_appealLetter']/div")));
    }

    public WebElement currentBilledPayorsTblOverrideCodeColTxt(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//tr[" + row + "]/*[@aria-describedby='tbl_currentBilledPayors_overrideCode']")));
    }

    public WebElement currentBilledPayorsTblOverrideCodeColDdl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//*[@aria-describedby='tbl_currentBilledPayors_overrideCode']/div")));
    }

    public WebElement currentBilledPayorsTblRowCountTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gbox_tbl_currentBilledPayors']//*[contains(@class,'rowCount')]")));
    }

    public WebElement currentBilledPayorsTblStatementTypeColDdl(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//tr[" + row + "]/*[@aria-describedby='tbl_currentBilledPayors_submissionSvcId']/div")));
    }

    public WebElement currentBilledPayorsTblStatementTypeColDdlChosen(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//tr[" + row + "]/*[@aria-describedby='tbl_currentBilledPayors_submissionSvcId']/div//*[contains(@class,'select2-chosen')]")));
    }

    public WebElement currentBilledPayorsTblAppealLetterColDdlChosen(String row) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_currentBilledPayors']//tr[" + row + "]/*[@aria-describedby='tbl_currentBilledPayors_appealLetter']/div//*[contains(@class,'select2-chosen')]")));
    }

    // Warning popup
    public WebElement warningPopupTitleTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-dialog-title")));
    }

    public WebElement warningPopupContentTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='confirmationDialog']//span")));
    }

    public WebElement warningPopupCancelBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='ui-dialog-buttonset']//span[text()='Cancel']")));
    }

    public WebElement warningPopupResetBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='ui-dialog-buttonset']//span[text()='Reset']")));
    }

    // Footer section
    public WebElement footerHelpIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
    }

    public WebElement footerResetBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
    }

    public WebElement footerSaveBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSave")));
    }
    public WebElement currentBilledPayorsSecInstitutionalBillTypeInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_claimTyp")));
    }
    public WebElement currentBilledPayorsSecInstitutionalBillTypeSelect() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("claimTyp")));
    }

    // This element is not appear in Single Statement page, This element belong to
    // PDF display page
    // We place this element inside here because the PDF has only one element.
    public WebElement pdfDisplayError() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='criticalError']//*[contains(@class,'titleText')]")));
    }
    public WebElement saveInProgressInfoText()
    {
        return driver.findElement(By.id("messagefor_message0"));
    }

    public boolean isAccnLoaded(String accnId, WebDriverWait wait)
    {
        boolean isAccnLoaded = false;
        try
        {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("messagefor_message0")));
            wait.until(ExpectedConditions.elementToBeClickable(footerResetBtn()));;
            logger.info("Verifying accession ID, expectedAccnId="+accnId);
            isAccnLoaded = true;
        }
        catch (Exception e)
        {
            logger.warn("Exception occurred while checking if accession is loaded on Single Statement, accnId="+accnId, e);
        }
        return isAccnLoaded;
    }

    public int getRowNumInTable()
    {
        return driver.findElements(By.xpath("//table[@id='tbl_currentBilledPayors']/tbody/tr")).size();
    }
    public int getColumnCount()//*[@id="1"]
    {
        return driver.findElements(By.xpath("//table[@id='tbl_currentBilledPayors']/tbody/tr/td")).size();
    }

    public WebElement statementTypeDropDownByRowNum(int rowNum) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(rowNum+"_submissionSvcId")));
    }
    public WebElement statementTypeInputByRowNum(int rowNum) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_currentBilledPayors']/tbody/tr[" + rowNum + "]")));
    }

    public WebElement statementToSpecPyrCheckBox() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("willSubmit")));
    }

    public boolean setStatementToSpecPyr()
    {
        driver.executeScript("arguments[0].click();", statementToSpecPyrCheckBox());
        return statementToSpecPyrCheckBox().isSelected();
    }

    public void setSubmSvcInStatementType(int rowNum, String submSvc)
    {
        WebElement submSvcDropDown = statementTypeDropDownByRowNum(rowNum);
        logger.debug("Found submission service dropdown element, rowNum="+rowNum);
        Select select = new Select(submSvcDropDown);
        logger.debug("Created select for dropdown, rowNum="+rowNum);
        select.selectByVisibleText(submSvc);
        logger.info("Selected item from dropdown, submSvc=" + submSvc);
    }

    public String setClaimTypeDropDown(String claimType)
    {
        WebElement claimTypeBlock = driver.findElementByClassName("claimTypBlock");
        scrollIntoView(claimTypeBlock);
        return setSelect2DropDown(SELECT2_ID_CLAIM_TYPE, claimType);
    }

    public void clickCurrentBilledPayorsRow(int row)
    {
        statementTypeInputByRowNum(row).click();
        logger.info("Clicked on the Row in Current Billed Payors");
    }
}
