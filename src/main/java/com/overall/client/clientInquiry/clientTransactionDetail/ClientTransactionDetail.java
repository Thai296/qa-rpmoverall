package com.overall.client.clientInquiry.clientTransactionDetail;

import com.overall.utils.ClientTransactionDetailUtils;
import com.overall.utils.TestCodeUtils;
import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.mars.dao.IGenericDaoXifinRpm;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.rpm.ClientDao;
import com.xifin.qa.dao.rpm.ClientDaoImpl;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClientTransactionDetail {
    private static final Logger LOG = Logger.getLogger(ClientTransactionDetail.class);
    private final RemoteWebDriver driver;
    private final IGenericDaoXifinRpm daoManagerXifinRpm;
    private final TestCodeUtils testCodeUtils;
    private final WebDriverWait wait;
    private final ClientDao clientDao;

    public ClientTransactionDetail(RemoteWebDriver driver, Configuration config, WebDriverWait wait) {
        this.driver = driver;
        this.daoManagerXifinRpm = new DaoManagerXifinRpm(config.getRpmDatabase());
        this.testCodeUtils = new TestCodeUtils(driver);
        this.wait = wait;
        clientDao = new ClientDaoImpl(config.getRpmDatabase());
    }

    public WebElement clnTransDetPageTitle() {
        return driver.findElement(By.xpath("//span[@class='platormPageTitle']"));
    }

    public By clientIdInputLocator() {
        return By.xpath("//input[@id='lookupClientAbbrev']");
    }

    public WebElement clientIdInput() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(clientIdInputLocator()));
    }

    public void setClientIdInput(String clientId) throws Exception {
        clientIdInput().sendKeys(clientId);
        clientIdInput().sendKeys(Keys.TAB);
        Thread.sleep(3000);
        LOG.info("         Entered Client ID: " + clientId);
    }

    public List<String> getClientIdAndStatementHaveBilledAccession(String dbEnv) throws Exception {
        String clientID;

        while (true) {
            clientID = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
            List<String> listStatement = daoManagerXifinRpm.getListStatementDateByClientID(clientID, dbEnv);
            for (String statement : listStatement) {
                if (daoManagerXifinRpm.getListClientBillAccByClientIDAndStatementDate(clientID, statement, dbEnv).size() > 0) {
                    List<String> list = new ArrayList<>();
                    list.add(clientID);
                    list.add(statement);
                    return list;
                }
            }
        }
    }

    public List<String> getClientIdAndStatementHaveMore10BilledAccession(String dbEnv) throws Exception {
        // This method use for verify next page, previous page, first page, last page of table Billed Accession
        String clientID;

        while (true) {
            clientID = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
            List<String> listStatement = daoManagerXifinRpm.getListStatementDateByClientID(clientID, dbEnv);
            for (String statement : listStatement) {
                if (daoManagerXifinRpm.getListClientBillAccByClientIDAndStatementDate(clientID, statement, dbEnv).size() > 10) {
                    List<String> list = new ArrayList<>();
                    list.add(clientID);
                    list.add(statement);
                    return list;
                }
            }
        }
    }

    //Client Account Aging Balances
    public WebElement clientAABShowHideTableIcon() {
        return driver.findElement(By.xpath("//*[@id='gview_tbl_clientAccAgBal']/div[1]/a/span"));
    }

    public WebElement clientAABTable() {
        return driver.findElement(By.id("tbl_clientAccAgBal"));
    }

    public WebElement lastPaymentDateInput() {
        return driver.findElement(By.id("lastPmtDt"));
    }

    public WebElement clientAABAmountInput() {
        return driver.findElement(By.id("lastPmtAmt"));
    }

    public WebElement lastPricingActivityDateInput() {
        return driver.findElement(By.id("lastPrcDt"));
    }

    public WebElement printerConfigDropdown() {
        return driver.findElement(By.id("printerConfig"));
    }

    public WebElement clientIDLabel() {
        return driver.findElement(By.id("clnAbbrv"));
    }

    public WebElement clientIDLabel(String labelName) {
        return driver.findElement(By.xpath("//label[@id='clnAbbrv' and text()='"+ labelName + "']"));
    }
    
    public WebElement clientNameLabel() {
        return driver.findElement(By.id("clnName"));
    }

    public WebElement accountTypeLabel() {
        return driver.findElement(By.id("accountType"));
    }

    public WebElement primaryIDLabel() {
        return driver.findElement(By.id("primaryId"));
    }

    public WebElement primaryNameLabel() {
        return driver.findElement(By.id("primaryName"));
    }

    public WebElement clientSearchIcon() {
        return driver.findElement(By.id("client_abbrev_search_btn"));
    }

    public WebElement facilityHeaderLabel() {
        return driver.findElement(By.id("facilityHeaderInfo"));
    }

    public WebElement viewDocumentLink() {
        return driver.findElement(By.xpath("//*[@id='viewDocumentHeaderInfo']/div[1]/a"));
    }

    public WebElement viewACMEDocumentLink() {
        return driver.findElement(By.xpath("//*[@id='viewDocumentHeaderInfo']/div[1]/a"));
    }

    public WebElement titlePage() {
        return driver.findElement(By.xpath("//*[@id='clientTransactionDetailForm']/div[1]/div[3]/div[1]/div/div[1]/div[2]/span"));
    }

    public WebElement clientAABHeader() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_clientAccAgBal']/div[2]"));
    }

    public WebElement clientNotFoundMessageContainer() {
        return driver.findElement(By.xpath(".//*[@id='clientTransactionDetailForm']/div[1]/div[2]/section/div/div[2]/div[2]/div[1]"));
    }

    public void clickViewDocumentLink() {
        viewDocumentLink().click();
        LOG.info("       View document link clicked");
    }

    public void clickViewACMEDocumentLink() {
        viewACMEDocumentLink().click();
        LOG.info("       View ACME document link clicked");
    }

    public void clickClientAABShowHideTableIcon() throws Exception {
        Thread.sleep(2000);
        clientAABShowHideTableIcon().click();
        LOG.info("       Switch show/hide table Client Account Aging Balances");
    }

    public void inputLastPaymentDate(String text) {
        lastPaymentDateInput().sendKeys(text);
        LOG.info("       Input :" + text + " in last payment date input");
    }

    public void inputClientAABAmount(String text) {
        clientAABAmountInput().sendKeys(text);
        LOG.info("       Input :" + text + " in amount of Client Account Aging Balances");
    }

    public void inputLastPricingActivityDate(String text) {
        lastPricingActivityDateInput().sendKeys(text);
        LOG.info("       Input :" + text + " in Last Pricing Activity Date");
    }

    public void clickClientSearchIcon() {
        clientSearchIcon().click();
        LOG.info("       Click client search icon");
    }

    /*------- Statement -------*/
    public WebElement iconViewPDF() {
        return driver.findElement(By.xpath("//*[@id=\"mainSections\"]/div[3]/div/div/section/div/div[2]/div[1]/img"));
    }

    public WebElement iconViewExcel() {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[3]/div/div/section/div/div[2]/div[1]/img[1]"));
    }

    public WebElement iconHideStatement() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_previousStatement']/div[1]/a/span"));
    }

    public WebElement wrapperOfPreviousStatementTable() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_previousStatement']/div[3]"));
    }

    public WebElement nextPageStatementIcon() {
        return driver.findElement(By.xpath(".//*[@id='next_tbl_previousStatement_pagernav']/span"));
    }

    public WebElement previousPageStatementIcon() {
        return driver.findElement(By.xpath(".//*[@id='prev_tbl_previousStatement_pagernav']/span"));
    }

    public WebElement lastPageStatementIcon() {
        return driver.findElement(By.xpath(".//*[@id='last_tbl_previousStatement_pagernav']/span"));
    }

    public WebElement firstPageStatementIcon() {
        return driver.findElement(By.xpath(".//*[@id='first_tbl_previousStatement_pagernav']/span"));
    }

    public WebElement currentPageInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_previousStatement_pagernav_center']/table/tbody/tr/td[4]/input"));
    }

    public WebElement totalPageStatement() {
        return driver.findElement(By.id("sp_1_tbl_previousStatement_pagernav"));
    }

    public void clickIconViewPDF() {
        iconViewPDF().click();
        LOG.info("        Clicked View Current PDF Statement icon button.");
    }

    public void clickIconViewExcel() {
        iconViewExcel().click();
        LOG.info("        Clicked View Current Excel Statement icon button.");
    }

    public void clickHideStatementIcon() {
        iconHideStatement().click();
        LOG.info("*       Click on Hide Statement icon");
    }

    public void clickNextPageStatementIcon() {
        nextPageStatementIcon().click();
        LOG.info("        Click on Next Page icon at Previous statement table");
    }

    public void clickPreviousPageStatementIcon() {
        previousPageStatementIcon().click();
        LOG.info("        Click on Previous Page icon at Previous Statement table");
    }

    public void clickLastPageStatementIcon() {
        lastPageStatementIcon().click();
        LOG.info("        Click on last page icon at previous statement table");
    }

    public void clickFirstPageStatementIcon() {
        firstPageStatementIcon().click();
        LOG.info("        Click on first page icon at Previous Statement table");
    }

    /*------- Client Accession Detail -------*/
    public WebElement statementDateDropdown() {
        return driver.findElement(By.id("submDt"));
    }

    public WebElement statementDateInput() {
        return driver.findElement(By.xpath(".//*[@id='inputSubmDt']"));
    }

    public WebElement createNewStatementDateIcon() {
        return driver.findElement(By.xpath(".//*[@id='toggleSubmDtInput']"));
    }

    public WebElement getStatementDateInfoBtn() {
        return driver.findElement(By.xpath("//button[@id='btnGetSDI']"));
    }

    public WebElement originalStatementAmountInput() {
        return driver.findElement(By.xpath(".//*[@id='origAmt']"));
    }

    public WebElement statementAmountDueInput() {
        return driver.findElement(By.xpath(".//*[@id='invAmt']"));
    }

    public WebElement statementTableValue() {
        return driver.findElement(By.xpath(".//*[@id='tbl_previousStatement']/tbody/tr/td/a"));
    }

    public WebElement statementTable() {
        return driver.findElement(By.id("tbl_previousStatement"));
    }

    public WebElement statementTableStatementDate() {
        return driver.findElement(By.xpath(".//*[@id='gs_stmtDate']"));
    }

    public WebElement accessionIDLinkRow(String row) {
        return driver.findElement(By.xpath("//*[@id='tbl_clnBilledAccessions']/tbody/tr[" + row + "]/td[3]/a"));
    }

    public WebElement accessionIDLink(String value) {
        return driver.findElement(By.xpath("//*[@id='tbl_clnBilledAccessions']/tbody//a[@data-id='" + value + "']"));
    }

    public void clickAccessionIDLinkRow(String row) {
        accessionIDLinkRow(row).click();
        LOG.info("        Click on row: " + row + " on table Accession");
    }

    public void clickAccessionID(String value) {
        accessionIDLink(value).click();
        LOG.info("        Click on accession id: " + value + " on table Accession");
    }

    public void inputStatementTblStatementDate(String value) throws Exception {
        statementTableStatementDate().sendKeys(value);
        Thread.sleep(1000);
        LOG.info("        Entered  " + value + " in Statement Date in Previous Statements table.");
    }

    public void clickCreateNewStatementDateIcon() {
        createNewStatementDateIcon().click();
        LOG.info("        Click on Create New Statement Date Icon (pencil icon)");
    }

    public void inputStatementDate(String value) {
        statementDateInput().sendKeys(value);
        statementDateInput().sendKeys(Keys.TAB);
        LOG.info("        Entered  Statement Date: " + value);
    }

    public void clickGetStatementDateInfoBtn() throws Exception {
        getStatementDateInfoBtn().click();
        Thread.sleep(2000);
        LOG.info("        Click on Get Statement Date Info Button");
    }

    public void inputOriginalStatementAmount(String value) {
        originalStatementAmountInput().sendKeys(value);
        LOG.info("        Input " + value + " to Original Statement Amount");
    }

    public void inputStatementAmountDue(String value) {
        statementDateInput().sendKeys(value);
        LOG.info("        Input " + value + " to Statement Amount Due");
    }

    public boolean verifyDataCorrespondingStatementDate(String statementDate, String clientID, String dbEnv) throws Exception {
        return verifyDataOfClientBillAccCorrespondingStatementDate(statementDate, clientID, dbEnv);
    }

    public void clickStatementFileLink(int row, int col) {
        statementTableValue().click();
        LOG.info("         Click on any file name at Previous statement table");
    }

    /*------- Reconciled Statement -------*/
    public WebElement reconciledPDFStatementBtn() {
        return driver.findElement(By.xpath(".//*[@id='btnViewPdfReconciledStmt']"));
    }

    public WebElement reconciledExcelStatementBtn() {
        return driver.findElement(By.id("btnViewExcelReconciledStmt"));
    }

    public void clickReconciledPDFStatementBtn() {
        reconciledPDFStatementBtn().click();
        LOG.info("        Click on Reconciled PDF Statement Button");
    }

    public void clickReconciledExcelStatementBtn() {
        reconciledExcelStatementBtn().click();
        LOG.info("        Click on Reconciled Excel Statement Button");
    }

    /*------- Client Payments --------*/
    public WebElement addNewClientPaymentBtn() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#add_tbl_clientPayment')[0]");
    }

    public WebElement tranIdClientPayment(String tranId) {
        return driver.findElement(By.xpath("//td[contains(@title,'" + tranId + "')]"));
    }

    public WebElement clientPaymentDialog() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('div[aria-describedby=\"editmodtbl_clientPayment\"]')[0]");
    }

    public WebElement addNewClientPaymentPopup() {
        return driver.findElement(By.xpath(".//*[@id='editmodtbl_clientPayment']"));
    }

    public WebElement paymentMethodDropdown() {
        return driver.findElement(By.xpath(".//*[@id='s2id_paymentMethod']/a"));
    }

    public WebElement subPaymentMethodDropdown() {
        return driver.findElement(By.xpath(".//*[@id='select2-drop']/ul"));
    }

    public WebElement cardTypeDropdown() {
        return driver.findElement(By.xpath(".//*[@id='s2id_ccType']/a/span[2]/b"));
    }

    public WebElement subcardTypeDropdown() {
        return driver.findElement(By.xpath(".//*[@id='select2-drop']/ul"));
    }

    public WebElement cardNumberInput() {
        return driver.findElement(By.xpath(".//*[@id='cardNumber']"));
    }

    public WebElement firstNameInput() {
        return driver.findElement(By.xpath(".//*[@id='firstName']"));
    }

    public WebElement lastNameInput() {
        return driver.findElement(By.xpath(".//*[@id='lastName']"));
    }

    public WebElement streetAddressInput() {
        return driver.findElement(By.xpath(".//*[@id='address']"));
    }

    public WebElement zipCodeInput() {
        return driver.findElement(By.xpath(".//*[@id='zipCode']"));
    }

    public WebElement expirationDateInput() {
        return driver.findElement(By.xpath(".//*[@id='expirationDate']"));
    }

    public WebElement amountInput() {
        return driver.findElement(By.xpath(".//*[@id='amount']"));
    }

    public WebElement commentInput() {
        return driver.findElement(By.id("notes"));
    }

    public WebElement PrintOnClientStatementCheckbox() {
        return driver.findElement(By.xpath(".//*[@id='print']"));
    }

    public WebElement invoicePayment() {
        return driver.findElement(By.xpath(".//*[@id='editcnttbl_clientPayment']/div[2]/div/div[2]/table/tbody/tr/td[3]/input"));
    }

    public WebElement continueButton() {
        return driver.findElement(By.xpath("//button[contains(.,'Continue >>')]"));
    }

    public WebElement cancelButton() {
        return driver.findElement(By.xpath("//a[@id='cData']"));
    }

    public WebElement clientPaymentTable() {
        return driver.findElement(By.id("gview_tbl_clientPayment"));
    }

    public WebElement totalDueAmount() {
        return driver.findElement(By.xpath("//td[@class='dueAmount displayMoney totalDueAmount']"));
    }

    public WebElement cancelClientPaymentBtn() {
        return driver.findElement(By.id("cData"));
    }

    public WebElement clientPaymentTbl() {
        return driver.findElement(By.id("editcnttbl_clientPayment"));
    }

    public WebElement confirmTransactionBtn() {
        return driver.findElement(By.id("btn_confirmClientPayment"));
    }

    public WebElement okTransactionProcessBtn() {
        return driver.findElement(By.xpath("//button[contains(.,'OK')]"));
    }

    public WebElement creditElectronicPaymentDetails() {
        return driver.findElement(By.id("trxnType"));
    }

    public WebElement voidElectronicPaymentDetails() {
        return driver.findElement(By.xpath("//button[contains(.,'Void')]"));
    }

    public WebElement ccTypeIconBtn() {
//        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"icon_payment_method icon_visa\"]')[0]");
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@title='VISA']/div")));
    }

    public WebElement confirmTransPopup() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"ui-draggable\"][role=\"dialog\"]')[0]");
    }

    public WebElement transStatusPopup() {
        return driver.findElement(By.id("ui-id-4"));
    }

    public WebElement loadInProgressMsgText() {
        return driver.findElement(By.id("messagefor_message0"));
    }

    public WebElement successResponseText() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"isSatisfiedIndicator responseSuccess\"]')[0]");
    }

    public WebElement processingMsgText() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"processingMessage\"]')[0]");
    }

    public WebElement clnPmtTable() {
        return driver.findElement(By.id("tbl_clientPayment"));
    }

    public WebElement ccTypeFilterInput() {
        return driver.findElement(By.id("gs_ccType"));
    }

    public void setCCTypeFilterInput(String value) {
        ccTypeFilterInput().sendKeys(value);
        ccTypeFilterInput().sendKeys(Keys.TAB);
        LOG.info("        Entered CC Type " + value);
    }

    public void clickOKBtnInTransStatusPopup() throws InterruptedException {
        okTransactionProcessBtn().click();
        Thread.sleep(1000);
        LOG.info("        Clicked OK button.");
    }

    public void clickCancelBtn() throws InterruptedException {
        cancelButton().click();
        Thread.sleep(1000);
        LOG.info("        Click reset button");
    }

    public void clickAddNewClientPaymentBtn() {
        addNewClientPaymentBtn().click();
        LOG.info("        Clicked on Add New Client Payment Button.");
    }

    public void inputCardNumber(String value) {
        cardNumberInput().sendKeys(value);
        cardNumberInput().sendKeys(Keys.TAB);
        LOG.info("        Input " + value + " to Card Number");
    }

    public void clickContinueButton() throws Exception {
        continueButton().click();
        Thread.sleep(1000);
        LOG.info("        Clicked Continue Button.");
    }

    public void clickCreditButton() throws Exception {
        creditElectronicPaymentDetails().click();
        Thread.sleep(1000);
        LOG.info("        Clicked Credit button.");
    }

    public void clickVoidButton() throws Exception {
        voidElectronicPaymentDetails().click();
        Thread.sleep(1000);
        LOG.info("        Click on Void Button");
    }

    public void inputPaymentAmount(String value) {
        amountInput().sendKeys(Keys.CONTROL + "a");
        amountInput().sendKeys(Keys.DELETE);
        amountInput().sendKeys(value);
        amountInput().sendKeys(Keys.TAB);
        LOG.info("        Entered Payment Amount: " + value);
    }

    public void clickPaymentMethod() {
        paymentMethodDropdown().click();
        LOG.info("        Click on Payment Method dropdown list");
    }

    public void clickCardType() {
        cardTypeDropdown().click();
        LOG.info("       Click on Card Type dropdown list");
    }

    public void inputFirstName(String firstName) {
        firstNameInput().sendKeys(firstName);
        LOG.info("        Entered first name : " + firstName);
    }

    public void inputExpirationDateInput(String expirationDate) {
        expirationDateInput().sendKeys(expirationDate);
        expirationDateInput().sendKeys(Keys.TAB);
        LOG.info("        Entered Expiration Date : " + expirationDate);
    }

    public void clickCancelClientPaymentBtn() {
        cancelClientPaymentBtn().click();
        LOG.info("       Clicked Cancel button in Client Payment popup.");
    }

    public void clickConfirmTransactionBtn() {
        confirmTransactionBtn().click();
        LOG.info("        Clicked Confirm Transaction button.");
    }

    public void clickOkTransactionProcessBtn() throws Exception {
        okTransactionProcessBtn().click();
        Thread.sleep(1000);
        LOG.info("        Clicked Ok button.");
    }

    public void inputComment(String value) {
        commentInput().clear();
        commentInput().sendKeys(value);
        LOG.info("        Entered Comments: " + value);
    }

    /*------- Client Adjustments -------*/
    public WebElement clientAdjustmentTbl() {
        return driver.findElement(By.id("tbl_clnAdjustments"));
    }

    public WebElement adjCodeCellClientAdjustmentTable() {
        return driver.findElement(By.xpath("//td[@aria-describedby='tbl_clnAdjustments_adjCode']"));
    }
    public WebElement adjCodeCellClientAdjustmentTable(String adjCode) {
        return driver.findElement(By.xpath("//td[@aria-describedby='tbl_clnAdjustments_adjCode'][text()='" + adjCode + "']"));
    }

    public WebElement editClientAdjustmentDialog() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('div#editcnttbl_clnAdjustments')).get(0)");
    }

    public WebElement addNewClientAdjustmentsBtn() {
        return driver.findElement(By.xpath(".//*[@id='add_tbl_clnAdjustments']/div"));
    }

    public WebElement addNewClientAdjustmentsPopup() {
        return driver.findElement(By.xpath(".//*[@id='editcnttbl_clnAdjustments']"));
    }

    public WebElement adjustmentsDateInput() {
        return driver.findElement(By.xpath(".//*[@id='adjDate']"));
    }

    public WebElement adjustmentsCodeDropdown() {
        return driver.findElement(By.xpath(".//*[@id='s2id_adjCode']"));
    }

    public WebElement subAdjCodeDropdown() {
        return driver.findElement(By.xpath(".//*[@id='select2-drop']/ul"));
    }

    public WebElement reviewAdjustmentsAccn() {
        return driver.findElement(By.xpath(".//*[@id='revADJAccn']"));
    }

    public WebElement printStatementCheckbox() {
        return driver.findElement(By.xpath(".//*[@id='printStmnt']"));
    }

    public WebElement cannedNotesBtn() {
        return driver.findElement(By.xpath(".//*[@id='addCannedNoteLink']"));
    }

    public WebElement cannedNotesTbl() {
        return driver.findElement(By.id("gview_tbl_cannednotes"));
    }

    public WebElement cancelAddAdjustmentBtn() {
        return driver.findElement(By.id("cData"));
    }

    public WebElement pagerOfTotalResultAdjTbl() {
        return driver.findElement(By.xpath("//*[@id='tbl_clnAdjustments_pagernav_right']/div"));
    }

    public WebElement currentAmountDueAdj() {
        return driver.findElement(By.id("currentAmountDue"));
    }

    public WebElement newAmountDueAdj() {
        return driver.findElement(By.id("newAmountDue"));
    }

    public WebElement okClientAdjBtn() {
        return driver.findElement(By.id("sData"));
    }

    public WebElement clientAdjustmentTable() {
        return driver.findElement(By.id("gview_tbl_clnAdjustments"));
    }

    public WebElement validationClientAdjMessage() {
        return driver.findElement(By.xpath("//*[@id='messagefor_revADJAccn']/div[2]"));
    }

    public WebElement errorMessageClientAdj() {
        return driver.findElement(By.xpath(".//*[@id='FormError']/td"));
    }

    public void clickCannedNotesBtn() {
        cannedNotesBtn().click();
        LOG.info("        Clicked on Canned Notes Button");
    }

    public void clickAddNewClientAdjustmentsBtn() {
        addNewClientAdjustmentsBtn().click();
        LOG.info("        Clicked on Add new Adjustment Button");
    }

    public void clickCancelAddAdjustmentBtn() {
        cancelAddAdjustmentBtn().click();
        LOG.info("        Clicked on Cancel Add Adjustment Button");
    }

    public void inputRevAdjAccnId(String accnId) {
        reviewAdjustmentsAccn().clear();
        reviewAdjustmentsAccn().sendKeys(accnId);
        reviewAdjustmentsAccn().sendKeys(Keys.TAB);
        LOG.info("        Entered " + accnId + " in the Rev Adj Accn field");
    }

    public void clickOkClientAdjBtn() {
        okClientAdjBtn().click();
        LOG.info("        Clicked on OK button of New Client Adjustment popup");
    }

    public void clickAdjustmentDropdown() {
        adjustmentsCodeDropdown().click();
        LOG.info("        Clicked on Adjustment Code dropdown");
    }

    /*------- Client Billed Accessions -------*/
    public WebElement pagerOfTotalResultClientBillAccTbl() {
        return driver.findElement(By.xpath("//*[@id='tbl_clnBilledAccessions_pagernav_right']/div"));
    }

    public WebElement accDetailBtn() {
        return driver.findElement(By.id("btnAccnDetail"));
    }

    public WebElement clientBillAccTbl() {
        return driver.findElement(By.id("tbl_clnBilledAccessions"));
    }

    public WebElement nextPageClientBillAccTbl() {
        return driver.findElement(By.xpath("//*[@id='next_tbl_clnBilledAccessions_pagernav']/span"));
    }

    public WebElement previousPageClientBillAccTbl() {
        return driver.findElement(By.xpath("//*[@id='prev_tbl_clnBilledAccessions_pagernav']/span"));
    }

    public WebElement firstPageClientBillAccTbl() {
        return driver.findElement(By.xpath("//*[@id='first_tbl_clnBilledAccessions_pagernav']/span"));
    }

    public WebElement lastPageClientBillAccTbl() {
        return driver.findElement(By.xpath("//*[@id='last_tbl_clnBilledAccessions_pagernav']/span"));
    }

    public WebElement pageNavigateClientBillAccTbl() {
        return driver.findElement(By.id("pg_tbl_clnBilledAccessions_pagernav"));
    }

    public WebElement keepResultWindowOpenCheckbox() {
        return driver.findElement(By.id("keepResultsWindowOpen"));
    }

    public WebElement DOSFilter() {
        return driver.findElement(By.xpath(".//*[@id='gs_dos']"));
    }

    public WebElement accnIdFilter() {
        return driver.findElement(By.xpath(".//*[@id='gs_accnId']"));
    }

    public WebElement ptNameFilter() {
        return driver.findElement(By.xpath(".//*[@id='gs_ptName']"));
    }

    public WebElement showHideClientBilledAccessionsIcon() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_clnBilledAccessions']/div[1]/a/span"));
    }

    public WebElement footerBilledAccession() {
        return driver.findElement(By.xpath("//*[@id='tbl_clnBilledAccessions_pagernav']"));
    }

    public WebElement chargeColumnClnBilledAccnTable() {
        return driver.findElement(By.xpath(".//*[@id='jqgh_tbl_clnBilledAccessions_charge']"));
    }

    public void clickClientTransactionDetailResetButton() throws Exception {
        clientTransactionDetailResetButton().click();
        Thread.sleep(2000);
        LOG.info(" 		Clicked Reset button.");
    }

    public void clickAccDetailBtn() {
        accDetailBtn().click();
        LOG.info("        Click on Acc Detail Button");
    }

    public void inputDOSFilter(String value) {
        DOSFilter().sendKeys(value);
        DOSFilter().sendKeys(Keys.TAB);
        LOG.info("        Entered " + value + " to DOS Filter");
    }

    public void inputAccnIdFilter(String value) {
        accnIdFilter().sendKeys(value);
        accnIdFilter().sendKeys(Keys.TAB);
        LOG.info("        Entered " + value + " to Accn ID Filter");
    }

    public void inputPtNameFilter(String value) {
        ptNameFilter().sendKeys(value);
        ptNameFilter().sendKeys(Keys.TAB);
        LOG.info("        Entered " + value + " to Pt Name Filter");
    }

    /*client - Transaction Detail*/
    public WebElement clientTransactionDetailResetButton() {
        return driver.findElement(By.xpath(".//*[@id='Reset']"));
    }

    public WebElement clientTransactionDetailSaveButton() {
        return driver.findElement(By.xpath(".//*[@id='btnSave']"));
    }

    public WebElement clientTransactionDetailHelpIcon() {
        return driver.findElement(By.xpath(".//*[@id='pageHelpLink']"));
    }

    public WebElement clientTransactionDetailShowClipBoardIcon() {
        return driver.findElement(By.xpath(".//*[@id='clientTransactionDetailForm']/div[1]/div[3]/div[3]/div/div[1]/div[5]/div"));
    }

    public WebElement clientTransactionDetailShowKeyboardShortcutsIcon() {
        return driver.findElement(By.xpath(".//*[@id='clientTransactionDetailForm']/div[1]/div[3]/div[3]/div/div[1]/div[6]/div"));
    }

    public WebElement clientIDLblInAccDetailPopup() {
        return driver.findElement(By.id("client-view"));
    }

    public WebElement statementDateInputInAccDetailPopup() {
        return driver.findElement(By.id("statementDate"));
    }

    public WebElement showHideGridIconInAccDetailPopup() {
        return driver.findElement(By.xpath("//*[@id='gview_tbl_clnBilledAccessions']/div[1]/a"));
    }

    public WebElement wrapperTableInAccDetailPopup() {
        return driver.findElement(By.xpath("//*[@id='gview_tbl_clnBilledAccessions']/div[3]"));
    }

    public WebElement filterAccIdInputInAccDetailPopup() {
        return driver.findElement(By.id("gs_accnId"));
    }

    public void setFilterAccIdInputInAccDetailPopup(String value) throws Exception {
        filterAccIdInputInAccDetailPopup().sendKeys(value);
        filterAccIdInputInAccDetailPopup().sendKeys(Keys.TAB);
        LOG.info("        Entered Accn ID: " + value);
        Thread.sleep(2000);
    }

    public void clickClientTransactionDetailSaveButton() throws Exception {
        clientTransactionDetailSaveButton().click();
        Thread.sleep(3000);
        LOG.info(" 		Clicked Save button.");
    }

    public void clickClientTransactionDetailHelpIcon() {
        clientTransactionDetailHelpIcon().click();
        LOG.info(" 		Clicked on Help Icon on Client Transaction Detail page");
    }

    public void clickClientTransactionDetailShowClipBoardIcon() {
        clientTransactionDetailShowClipBoardIcon().click();
        LOG.info(" 		Clicked on Show Clip Board Icon on Client Transaction Detail page");
    }

    public void clickClientTransactionDetailShowKeyboardShortcutsIcon() {
        clientTransactionDetailShowKeyboardShortcutsIcon().click();
        LOG.info(" 		Clicked on Show Keyboard Shortcuts Icon on Client Transaction Detail page");
    }

    public boolean verifyDataOfClientBillAccCorrespondingStatementDate(String statementDate, String clientID, String dbEnv) throws Exception {
        List<List<String>> listBill = daoManagerXifinRpm.getListClientBillAccByClientIDAndStatementDate(clientID, statementDate, dbEnv);
        return Integer.parseInt(testCodeUtils.getTotalResultSearch(pagerOfTotalResultClientBillAccTbl())) == listBill.size();
    }

    /*Documents Table Details*/

    public WebElement documentsTableCreationDateInput() {
        return driver.findElement(By.xpath(".//*[@id='gs_creationDate']"));
    }

    public WebElement documentsTableDescriptionInput() {
        return driver.findElement(By.xpath(".//*[@id='gs_desc']"));
    }

    public WebElement documentsTableFileInput() {
        return driver.findElement(By.xpath(".//*[@id='gs_file']"));
    }

    public WebElement documentsTableProcessedDate() {
        return driver.findElement(By.xpath(".//*[@id='gs_processedDate']"));
    }

    public WebElement documentsTableHelpIcon() {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[10]/div/div/section/div/div[1]/a"));
    }

    public WebElement documentsTableShowHideIcon() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_clnEPDocs']/div[1]/a/span"));
    }

    public WebElement documentsTablePagingInfo() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clnEPDocs_pagernav_right']/div"));
    }

    public WebElement documentsTableFirstIcon() {
        return driver.findElement(By.xpath(".//*[@id='first_tbl_clnEPDocs_pagernav']/span"));
    }

    public WebElement documentsTablePreviousIcon() {
        return driver.findElement(By.xpath(".//*[@id='prev_tbl_clnEPDocs_pagernav']/span"));
    }

    public WebElement documentsTableNumberOfPerPageInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clnEPDocs_pagernav_center']/table/tbody/tr/td[4]/input"));
    }

    public WebElement documentsTableNumberOfPageLabel() {
        return driver.findElement(By.xpath(".//*[@id='sp_1_tbl_clnEPDocs_pagernav']"));
    }

    public WebElement documentsTableNextIcon() {
        return driver.findElement(By.xpath(".//*[@id='next_tbl_clnEPDocs_pagernav']/span"));
    }

    public WebElement documentsTableLatestIcon() {
        return driver.findElement(By.xpath(".//*[@id='last_tbl_clnEPDocs_pagernav']/span"));
    }

    public WebElement documentTable() {
        return driver.findElement(By.id("tbl_clnEPDocs"));
    }

    public WebElement FileLink(int row) {
        return driver.findElement(By.xpath("//*[@id='tbl_clnEPDocs']/tbody/tr[" + row + "]/td[4]/a"));
    }

    public void clickOnFileLink(int row) throws InterruptedException {
        FileLink(row).click();
        Thread.sleep(1000);
        LOG.info(" 		Clicked on file link");
    }

    public void inputDocumentTableCreationDate(String value) throws Exception {
        documentsTableCreationDateInput().sendKeys(value);
        Thread.sleep(1000);
        LOG.info(" 		Entered " + value + " in Creation Date in Previous Statements table.");
    }

    public void inputDocumentTableDescription(String value) throws Exception {
        documentsTableDescriptionInput().sendKeys(value);
        Thread.sleep(1000);
        LOG.info(" 		Entered " + value + " in Description in Previous Statements table.");
    }

    public void inputDocumentTableFile(String value) throws Exception {
        documentsTableFileInput().sendKeys(value);
        Thread.sleep(1000);
        LOG.info(" 		Entered " + value + " to File in Documents table");
    }

    public void inputDocumentTableProcessedDate(String value) throws Exception {
        documentsTableProcessedDate().sendKeys(value);
        Thread.sleep(1000);
        LOG.info(" 		Entered " + value + " in Processed Date in Previous Statements table");
    }

    public void clickDocumentsTableHelpIcon() {
        documentsTableHelpIcon().click();
        LOG.info(" 		Clicked on Help Icon (Question icon) on Client Transaction Detail page ");
    }

    public void clickDocumentTableShowHideIcon() {
        documentsTableShowHideIcon().click();
        LOG.info(" 		Clicked on Show/Hide Icon (Circle Triangle icon) on Documents table");
    }

    public void clickDocumentsTableFirstIcon() {
        documentsTableFirstIcon().click();
        LOG.info(" 		Clicked on First Icon on Documents table");
    }

    public void clickDocumentsTablePreviousIcon() {
        documentsTablePreviousIcon().click();
        LOG.info(" 		Clicked on Previous Icon on Documents table");
    }

    public void inputDocumentsNumberOfPerPageInput(String value) {
        documentsTableNumberOfPerPageInput().sendKeys(value);
        LOG.info(" 		Entered " + value + " Number of entire per page in Documents table");
    }

    public void clickDocumentsTableNextIcon() throws Exception {
        documentsTableNextIcon().click();
        LOG.info(" 		Clicked on Next Icon in Documents table");
    }

    public void clickDocumentsTableLatestIcon() throws Exception {
        documentsTableLatestIcon().click();
        LOG.info(" 		Clicked on Latest Icon in Documents table");
    }

    //Error message
    public WebElement messageContent() {
        return driver.findElement(By.id("messagefor_amount"));
    }

    public void addNewClientPayment(String paymentMethod, String cardType, String cardNumber, String expirationDate, String paymentAmount) throws Exception {
        selectPaymentMethod(paymentMethod);
        selectCardType(cardType);
        inputCardNumber(cardNumber);
        inputExpirationDateInput(expirationDate);
        inputPaymentAmount(paymentAmount);
        Thread.sleep(2000);
    }

    public void checkInputClientId(String id) throws Exception {
        boolean flag;
        int count = 0;
        do {
            setClientIdInput(id);
            Thread.sleep(2000);
            flag = clientNameLabel().isDisplayed();
            if (!flag) driver.navigate().refresh();
            if (count == 10) break;
            count++;
        } while (!flag);
    }

    public String selectPaymentMethod(String value) {
        paymentMethodDropdown().click();
        WebElement list = subPaymentMethodDropdown();
        List<WebElement> allRows = list.findElements(By.tagName("li"));
        String paymentMethod = "";

        for (WebElement row : allRows) {
            paymentMethod = row.getText();
            if (paymentMethod.equals(value)) {
                row.click();
                LOG.info("        Clicked payment Method  : " + paymentMethod);
                break;
            }
        }
        return paymentMethod;

    }

    public String selectCardType(String value) {
        cardTypeDropdown().click();
        WebElement list = subcardTypeDropdown();
        List<WebElement> allRows = list.findElements(By.tagName("li"));
        String cardType = "";

        for (WebElement row : allRows) {
            cardType = row.getText();
            if (cardType.equals(value)) {
                row.click();
                LOG.info("        Clicked card Type  : " + cardType);
                break;
            }
        }
        return cardType;

    }

    public WebElement demo() {
        return driver.findElement(By.xpath("//input[@class='select2-input select2-focused']"));
    }

    public String selectAdjCode() {
        adjustmentsCodeDropdown().findElement(By.tagName("a")).click();
        String adjCode = "";
        WebElement list = subAdjCodeDropdown();
        List<WebElement> allRows = list.findElements(By.tagName("li"));
        for (WebElement row : allRows) {
            adjCode = row.getText();
            if (!adjCode.isEmpty()) {
                row.click();
                LOG.info("        Clicked on Adj Code:" + adjCode);
                break;
            }
        }
        return adjCode;
    }

    public List<WebElement> listElementInvoicePayment() {
        return driver.findElements(By.xpath("html/body/div[2]/div[2]/div[2]/div/div[2]/table/tbody/tr/td/input"));
    }

    public List<WebElement> listElementEditInvoicePayment() {
        return driver.findElements(By.xpath("html/body/div[2]/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/input"));
    }

    public List<String> getListInvoicePayment(List<WebElement> listElement) {
        List<String> listInvoicePayment = new ArrayList<>();
        for (WebElement webElement : listElement) {
            String invoicePay = webElement.getAttribute("value");
            listInvoicePayment.add(invoicePay);
        }
        LOG.info("        List Invoice Payment:" + listInvoicePayment);
        return listInvoicePayment;
    }

    public double totalInvoicePayment(List<WebElement> listElement) {
        int count = 0;
        double sum = 0;

        for (String invoicePayment : getListInvoicePayment(listElement)) {
            if (!invoicePayment.isEmpty()) {
                count++;
                sum = Double.parseDouble(invoicePayment);
                if (count > 1) {
                    sum = sum + Double.parseDouble(invoicePayment);
                }
            }
        }

        LOG.info("        Total Invoice Payment:" + sum);
        return sum;
    }

    public String getStatementDateHaveDocument(String clientId, String tesDb) throws Exception {
        ClientTransactionDetailUtils clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
        String statementDate = "";
        List<String> listStatement = daoManagerXifinRpm.getListStatementDateByClientID(clientId, tesDb);
        List<String> listDocument;
        for (int i = 0; i <= listStatement.size(); i++) {
            if (i == listStatement.size()) {
                break;
            } else {
                listDocument = daoManagerXifinRpm.getClientDocumentByClientId(clientId, clientTransactionDetailUtils.formatDate(listStatement.get(i), "dd-MMM-yyyy"), tesDb);
                if (listDocument.size() > 0) {
                    statementDate = listStatement.get(i);
                    break;
                }
            }
        }
        return statementDate;
    }

    //XIFIN Single Sign On
    public WebElement xfSSOLoginTitle() {
        return driver.findElement(By.xpath(".//*[@id='content']/article/div/div/table/tbody/tr/td[1]/h1"));
    }

    public WebElement xfSSOEmailInput() {
        return driver.findElement(By.xpath(".//*[@id='username']"));
    }

    public WebElement xfSSOPassInput() {
        return driver.findElement(By.xpath(".//*[@id='password']"));
    }

    //Error Mess Client Accession Detail
    public WebElement accDErrorMessContent() {
        return driver.findElement(By.xpath(".//*[@id='messagefor_inputSubmDt']/div[2]"));
    }

    public WebElement accDErrorMessCloseIcon() {
        return driver.findElement(By.xpath(".//*[@id='messagefor_inputSubmDt']/div[1]/a"));
    }

    public WebElement accDErrorMessContentFutureDate() {
        return driver.findElement(By.xpath(".//*[@id='sectionServerMessages']/div/div[2]/ul/li/span"));
    }

    public void clickAccDErrorMessCloseIcon() {
        accDErrorMessCloseIcon().click();
        LOG.info(" 		Clicked on Close Icon on Error message");
    }

    public WebElement userIDInput() {
        return driver.findElement(By.xpath(".//*[@id='userId']"));
    }

    //Add Record Client Adjustment
    public WebElement adjCodeDropdown() {
        return driver.findElement(By.xpath(".//*[@id='s2id_adjCode']"));
    }

    public void inputAddRecordAmount(String value) {
        amountInput().sendKeys(value);
        amountInput().sendKeys(Keys.TAB);
        LOG.info(" 		Entered Amount " + value + " in Add Record of Client Adjustment");
    }

    public WebElement AdjTableTotalResult() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clnAdjustments_pagernav_right']/div"));
    }

    public WebElement adjShowHideIcon() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_clnAdjustments']/div[1]/a/span"));
    }

    public WebElement adjTableHeader() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_clnAdjustments']/div[2]"));
    }

    public WebElement adjAmountColumn() {
        return driver.findElement(By.xpath(".//*[@id='jqgh_tbl_clnAdjustments_amount']"));
    }

    public WebElement adjTableAdjDateInput() {
        return driver.findElement(By.xpath(".//*[@id='gs_adjDate']"));
    }

    public WebElement adjTableAdjCodeInput() {
        return driver.findElement(By.xpath(".//*[@id='gs_adjCode']"));
    }

    public WebElement adjTableAdjAmountInput() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_clnAdjustments']/div[2]/div/table/thead/tr[2]/th[6]/div/input"));
    }

    public WebElement adjTableAdjUserIdInput() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_clnAdjustments']/div[2]/div/table/thead/tr[2]/th[10]/div/input"));
    }

    public WebElement adjTableValue(int row, int col) {
        return driver.findElement(By.xpath(".//*[@id='tbl_clnAdjustments']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public void inputAdjTableAdjDate(String value) throws Exception {
        adjTableAdjDateInput().sendKeys(value);
        adjTableAdjDateInput().sendKeys(Keys.TAB);
        Thread.sleep(2000);
        LOG.info(" 		Entered ADJ Date: " + value + " in Client Adjustment");
    }

    public void inputAdjTableAdjCode(String value) throws Exception {
        adjTableAdjCodeInput().sendKeys(value);
        adjTableAdjCodeInput().sendKeys(Keys.TAB);
        Thread.sleep(2000);
        LOG.info(" 		Entered ADJ Code: " + value + " in Client Adjustment");
    }

    public void inputAdjTableAdjAmount(String value) throws Exception {
        adjTableAdjAmountInput().sendKeys(value);
        adjTableAdjAmountInput().sendKeys(Keys.TAB);
        Thread.sleep(2000);
        LOG.info(" 		Entered ADJ Amount: " + value + " in Client Adjustment");
    }

    public void inputAdjTableAdjUserId(String value) throws Exception {
        adjTableAdjUserIdInput().sendKeys(value);
        adjTableAdjUserIdInput().sendKeys(Keys.TAB);
        Thread.sleep(2000);
        LOG.info(" 		Entered ADJ User ID: " + value + " in Client Adjustment");
    }

    public List<List<String>> addClientAdjustment(int index, String testDb, SeleniumBaseTest b) throws Exception {
        RandomCharacter randomCharacter = new RandomCharacter(driver);
        List<List<String>> listInput = new ArrayList<>();
        List<String> listADJ;
        ArrayList<String> adjInfo;
        for (int i = 0; i < index; i++) {
            adjInfo = daoManagerXifinRpm.getADJInfo(testDb);
            listADJ = new ArrayList<>();
            String adjAccn = adjInfo.get(1);
            String adjDate = adjInfo.get(4);
            String adjUserId = adjInfo.get(6);
            Assert.assertTrue(b.isElementPresent(addNewClientAdjustmentsBtn(), 5), "        The Add button should show.");
            b.clickHiddenPageObject(addNewClientAdjustmentsBtn(), 0);
            inputRevAdjAccnId(adjAccn);
            String amount = randomCharacter.getRandomNumericString(1);
            inputAddRecordAmount(amount);
            String adjCode = selectAdjCode();
            clickOkClientAdjBtn();
            listADJ.add(adjCode);
            listADJ.add(adjAccn);
            listADJ.add(amount);
            listADJ.add(adjDate);
            listADJ.add(adjUserId);

            listInput.add(listADJ);
        }
        return listInput;
    }

    public void clearData(WebElement element) throws InterruptedException {
        element.clear();
        element.sendKeys(Keys.CLEAR);
        element.sendKeys(Keys.TAB);
        Thread.sleep(2000);
        LOG.info("        Cleared Input field.");
    }

    public String checkStatementSate(String statementDate) throws Exception {
        TimeStamp timeStamp = new TimeStamp(driver);
        return statementDate.isEmpty() ? timeStamp.getCurrentDate() : statementDate;
    }

    public WebElement clnPmtNotesFilter() {
        return driver.findElement(By.id("gs_notes"));
    }

    public void enterClnPmtNotesFilter(String str) throws Exception {
        clnPmtNotesFilter().clear();
        clnPmtNotesFilter().sendKeys(str);
        clnPmtNotesFilter().sendKeys(Keys.TAB);
        Thread.sleep(2000);
        LOG.info(" 		Entered Notes in Client Payments filter: " + str);
    }

    public WebElement clickAddBtnInClnPmtGrid() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#add_tbl_clientPayment > div').click()");
    }

    public void enterNotes(String value) {
        commentInput().sendKeys(value);
        commentInput().sendKeys(Keys.TAB);
        LOG.info("        Entered Notes: " + value);
    }

    public WebElement clickCCTypeIconBtn() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$('[class*=\"icon_payment_method icon\"]')[0].click()");
    }

    public String switchToPopupWin() throws InterruptedException {
        String parentWindow = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> it = handles.iterator();
        String switchWin;

        while (it.hasNext()) {
            switchWin = it.next();
            driver.switchTo().window(switchWin);
            try {
                driver.manage().window().maximize();
            } catch (WebDriverException e) {
                // Not all popups can be maximized
                LOG.warn("Unable to maximize window");
            }
        }
        Thread.sleep(2000);
        return parentWindow;
    }
}
