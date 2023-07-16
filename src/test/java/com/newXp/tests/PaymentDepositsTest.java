package com.newXp.tests;

import com.mbasys.mars.ejb.entity.bankTrans.BankTrans;
import com.mbasys.mars.ejb.entity.depBatch.DepBatch;
import com.overall.menu.MenuNavigation;
import com.overall.payment.paymentPayments.Deposits;
import com.overall.utils.PaymentDepositsUtil;
import com.overall.utils.XifinPortalUtils;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.TimeStamp;
import domain.payment.deposits.BankReconciliation;
import domain.payment.deposits.BatchesTable;
import domain.payment.deposits.Header;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class PaymentDepositsTest extends SeleniumBaseTest{

    private String userName;
    private TimeStamp timeStamp;
    private Deposits deposits;
    private RandomCharacter randomCharacter;
    private XifinPortalUtils xifinPortalUtils;
    private PaymentDepositsUtil depositsUtil;

    private static final String WIP_TEXT = "WIP";
    private static final String EMPTY_STRING = "";
    private static final String LAST_ROW = "last()";
    private static final String ASSIGNED_TEXT = "Assigned";
    private static final String REMIT_ERROR_TEXT = "Remit Error";
    private static final String DATE_FORMATTED_DEFAULT = "MM/dd/yyyy";
    private static final String RELOAD_DEPOSITS = "Reload Deposit Id";
    private static final String WARNING_MSG = "Changes have been made to this page. Are you sure you want to reset the page?";
    private static final String DEPOSIT_AMT_NOT_EQUALS_SUM_OF_BATCH_AMT_MSG = "Deposit Amount does not equal sum of batch amounts.";

    @BeforeMethod(alwaysRun = true)
    @Parameters({"ssoXpUsername", "ssoXpPassword"})
    public void beforeMethod(String ssoXpUsername, String ssoXpPassword) {
        try {
            xifinPortalUtils = new XifinPortalUtils(driver);
            depositsUtil = new PaymentDepositsUtil(driver, wait, config);
            MenuNavigation menuNavigation = new MenuNavigation(driver, config);
            logIntoSso(ssoXpUsername, ssoXpPassword);
            menuNavigation.navigateToPaymentDepositsPage();
            this.userName = "x"+ssoXpUsername.substring(0, ssoXpUsername.indexOf("@"));
        } catch (Exception e) {
            logger.error("Error running BeforeMethod", e);
        }
    }

    @Test(priority = 1, description = "Add A New Deposit With Required Fields")
    public void addDepWithReqFields() throws Exception {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);

        logger.info("*** Step 1 Expected Results: User login successfully. The Deposits page is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed.");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data.");
        Header actHeader = depositsUtil.setValuesAtHeader();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, EMPTY_STRING);

        logger.info("*** Step 4 Action: Click Add icon and enter valid data at the fields (Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) in Batches tbl.");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable actBatchesTable = depositsUtil.setBatchesValue(actHeader.getDepositAmount());

        logger.info("*** Step 4 Expected Results: The new row is added in Batches tbl with correct data.");
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTable, LAST_ROW);

        logger.info("*** Step 5 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 5 Expected Results: Load [Deposits] screen is displayed. Data are saved to 2 tables: Dep, Dep_Batch with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeader,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(actBatchesTable, actHeader.getDepositId(), 1);

        logger.info("*** Step 6 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 6 Expected Results: The Deposits Detail screen is displayed correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, RELOAD_DEPOSITS);
        depositsUtil.verifyBatchesSectionIsDisplayedCorrectly(actBatchesTable, LAST_ROW);

        logger.info("*** Step 7 Action: Click on Reset btn");
        deposits.clickOnResetBtn();

        logger.info("*** Step 7 Expected Results: Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "Add A New Deposit With Required Fields and checked Post Non-AR Amt column At Batches tbl")
    public void addDepPostNonARAmtChecked() throws Throwable {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);

        logger.info("*** Step 1 Expected Results: User login success. Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data");
        Header actHeader = depositsUtil.setValuesAtHeader();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        Header expHeader = depositsUtil.getHeaderValue("");
        assertEquals(expHeader.toString(), actHeader.toString(), "        Data is displayed correctly");

        logger.info("*** Step 4 Action: Click Add icon and populate data in the fields (Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) in Batches tbl."
                +   " Click on the Post Non-AR Amt column of the new row");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable expBatchesTable = depositsUtil.setBatchesValue(actHeader.getDepositAmount());

        logger.info("*** Step 4 Expected Results: Data of the new row is displayed incorrectly.");
        BatchesTable actBatchesTable = depositsUtil.getBatchesValueByInput("last()");
        assertEquals(actBatchesTable.toString(), expBatchesTable.toString(), "        Data of the new row at Batches table is displayed incorrectly");

        logger.info("*** Step 5 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 5 Expected Results: Load [Deposits] screen is displayed. Data are saved to 2 tables: Dep, Dep_Batch with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeader,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(expBatchesTable, expHeader.getDepositId(), 1);

        logger.info("*** Step 6 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 6 Expected Results: The Deposits Detail screen is displayed correctly.");
        actHeader.setUser(this.userName);
        actHeader.setAccountingDate(ConvertUtil.convertStringToSQLDate(depositsUtil.determineDepAccountingDate(), DATE_FORMATTED_DEFAULT));
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, RELOAD_DEPOSITS);
        depositsUtil.verifyBatchesSectionIsDisplayedCorrectly(actBatchesTable, "last()");

        logger.info("*** Step 7 Action: Click on Reset button");
        deposits.clickOnResetBtn();

        logger.info("*** Step 7 Expected Results: Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "Add A New Deposit With All Fields")
    public void addDepWithAllFields() throws Exception {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);

        logger.info("*** Step 1 Expected Results: User login successfully. The Deposits page is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed.");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data.");
        Header actHeader = depositsUtil.setValuesAtHeader();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, EMPTY_STRING);

        logger.info("*** Step 4 Action: Input into the Bank Transaction ID input.");
        BankReconciliation actBankReconciliation = depositsUtil.setValueInBankTransationSection();
        depositsUtil.selectBank(actHeader.getBank());

        logger.info("*** Step 4 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliation);

        logger.info("*** Step 5 Action: Click Add icon and input data in the fields "
                + "(Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) of [Batches] tbl. Tab out");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable actBatchesTable = depositsUtil.setBatchesValue(actHeader.getDepositAmount());

        logger.info("*** Step 5 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTable, LAST_ROW);

        logger.info("*** Step 6 Action: Input value at Note in Deposit Notes section");
        String note = depositsUtil.setValueAtDepositsNoteSection();

        logger.info("*** Step 6 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyDepositNotesSecTionIsDisplayedCorrectly(note);

        logger.info("*** Step 7 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 7 Expected Results: Load [Deposits] screen is displayed."
                + "- Data are saved to 3 tables: Dep, Dep_Batch, DEP_BANK_TRANS with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeader,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(actBatchesTable, actHeader.getDepositId(), 1);
        depositsUtil.verifyDataAtBankReconciliationSectionIsSavedInDepBankTransTbl(actBankReconciliation, actHeader.getDepositId());
        depositsUtil.verifyDataAtDepositNotesSectionIsSavedInDepTbl(actHeader.getDepositId(), note);

        logger.info("*** Step 8 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 8 Expected Results: The Deposits Detail screen is displayed correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, RELOAD_DEPOSITS);
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliation);
        depositsUtil.verifyDepositNotesSecTionIsDisplayedCorrectly(note);
        depositsUtil.verifyBatchesSectionIsDisplayedCorrectly(actBatchesTable, LAST_ROW);

        logger.info("*** Step 10 Action: Click on Reset btn");
        deposits.clickOnResetBtn();

        logger.info("*** Step 10 Expected Results: Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "Update a record in Batches tbl and values in Note field")
    public void updRecInBatchesTable() throws Exception {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);

        logger.info("*** Step 1 Expected Results: User login successfully. The Deposits page is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed.");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data.");
        Header actHeader = depositsUtil.setValuesAtHeader();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, EMPTY_STRING);

        logger.info("*** Step 4 Action: Input the Bank Transaction ID input.");
        BankReconciliation actBankReconciliation = depositsUtil.setValueInBankTransationSection();
        depositsUtil.selectBank(actHeader.getBank());

        logger.info("*** Step 4 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliation);

        logger.info("*** Step 5 Action: Click Add icon and populate values at the fields (Payor ID, Batch Amount) in Batches tbl"
                + "- Assigned User,  G/L Account are epmty"
                + "- Input value at Note in Deposit Notes section");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable actBatchesTable = depositsUtil.setPayorIdAtBatchesTable(new BatchesTable());
        actBatchesTable = depositsUtil.setBatchAmountAtBatchesTable(actBatchesTable, actHeader.getDepositAmount());
        String note = depositsUtil.setValueAtDepositsNoteSection();

        logger.info("*** Step 5 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTable, LAST_ROW);
        depositsUtil.verifyDepositNotesSecTionIsDisplayedCorrectly(note);

        logger.info("*** Step 6 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 6 Expected Results: - Load [Deposits] screen is displayed."
                + "- Data are saved to 3 tables: Dep, Dep_Batch, DEP_BANK_TRANS with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeader,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(actBatchesTable, actHeader.getDepositId(), 1);
        depositsUtil.verifyDataAtBankReconciliationSectionIsSavedInDepBankTransTbl(actBankReconciliation, actHeader.getDepositId());

        logger.info("*** Step 7 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 7 Expected Results: The Deposits Detail screen is displayed with data correctly with Status in Batches tbl = 'Unassigned'."
                + "- [Post Non-AR Amt] checkbox is disable.");
        actBatchesTable = depositsUtil.verifyValueStatusAtBatchesSectionIsDisplayedUnAssigned(actBatchesTable, LAST_ROW);
        depositsUtil.verifyStatusOfPostNonARAmtColAndDeleteColInBatchesTblAreEnable(LAST_ROW, false, true);

        logger.info("*** Step 8 Action: At Batches tbl, set Non-Rmp amount select valid data at [G/L Account] field");
        clickHiddenPageObject(deposits.batchesTblBatchIdColTxt(LAST_ROW), 0);
        actBatchesTable = depositsUtil.setNonARAmountAtBatchesTable(actBatchesTable);
        actBatchesTable = depositsUtil.selectGLAccountAtBatchesTbl(actBatchesTable);

        logger.info("*** Step 8 Expected Results: [Post Non-AR Amt] checkbox is enable.");
        depositsUtil.verifyStatusOfPostNonARAmtColAndDeleteColInBatchesTblAreEnable(LAST_ROW, true, true);

        logger.info("*** Step 9 Action: - Update all fields at header (Deposit Date input with value less then the current date)"
                + "- Update all fields of the new record at Baths tbl with valid data (Make sure Batch Amount column Equal with Deposit Amount at header)"
                + "- Update the Note input at [Deposit Notes] section");
        Header actHeaderUpdated = depositsUtil.updateValueAtHeader(actHeader);
        BatchesTable actBatchesTableUpdated = depositsUtil.updateValuesAtBatchesTable(actBatchesTable, actHeaderUpdated, LAST_ROW);
        String noteUpdated = depositsUtil.setValueAtDepositsNoteSection();

        logger.info("*** Step 9 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeaderUpdated, EMPTY_STRING);
        depositsUtil.verifyDepositNotesSecTionIsDisplayedCorrectly(noteUpdated);
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTableUpdated, LAST_ROW);
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliation);

        logger.info("*** Step 10 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 10 Expected Results: - Load [Deposits] screen is displayed."
                + "- Data are updated to 2 tables: Dep, Dep_Batch with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        actBatchesTableUpdated.setStatus(EMPTY_STRING);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeaderUpdated,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(actBatchesTableUpdated, actHeaderUpdated.getDepositId(), 1);
        depositsUtil.verifyDataAtDepositNotesSectionIsSavedInDepTbl(actHeaderUpdated.getDepositId(), noteUpdated);

        logger.info("*** Step 11 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 11 Expected Results: The Deposits Detail screen is displayed correctly with Status in Baths tbl = 'Assigned'.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeaderUpdated, RELOAD_DEPOSITS);
        depositsUtil.verifyDepositNotesSecTionIsDisplayedCorrectly(noteUpdated);
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliation);
        depositsUtil.verifyBatchesSectionIsDisplayedCorrectly(actBatchesTableUpdated, LAST_ROW);

        logger.info("*** Step 12 Action: Click on Reset btn");
        deposits.clickOnResetBtn();

        logger.info("*** Step 12 Expected Results: Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

    }

    @Test(priority = 1, description = "Update value in Bank Transaction ID field")
    public void updValInBankTransIdFld() throws Throwable {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);
        BankTrans bankTransUpdated = depositsUtil.createBankTrans();

        logger.info("*** Step 1 Expected Results: User login success.  Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data");
        Header actHeader = depositsUtil.setValuesAtHeader();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        Header expHeader = depositsUtil.getHeaderValue("");
        assertEquals(actHeader.toString(), expHeader.toString(), "        Data is displayed correctly");

        logger.info("*** Step 4 Action: Input the Bank Transaction ID input with random data");
        BankReconciliation actBankReconciliation = depositsUtil.setValueInBankTransationSection();
        depositsUtil.setReconciledAmt(String.valueOf(expHeader.getDepositAmount() -1));
        enterValues(deposits.bnkReconciliationTblBnkTransIdFilter(), actBankReconciliation.getBankId());
        depositsUtil.selectBank(actHeader.getBank());

        logger.info("*** Step 4 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliation);

        logger.info("*** Step 5 Action: Click Add icon and input data in the fields (Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) of [Batches] tbl. Tab out");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable expBatchesTable = depositsUtil.setBatchesValue(expHeader.getDepositAmount());

        logger.info("*** Step 5 Expected Results: The new row is added in Batches tbl with correct data.");
        BatchesTable actBatchesTable = depositsUtil.getBatchesValueByInput("last()");
        assertEquals(actBatchesTable.toString(), expBatchesTable.toString(), "        Data of the new row at Batches table is displayed incorrectly");

        logger.info("*** Step 6 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 6 Expected Results: Load [Deposits] screen is displayed. Data is save to 3 tables: Dep, Dep_Batch, DEP_BANK_TRANS with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeader,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(expBatchesTable, expHeader.getDepositId(), 1);
        depositsUtil.verifyDataAtBankReconciliationSectionIsSavedInDepBankTransTbl(actBankReconciliation, expHeader.getDepositId());

        logger.info("*** Step 7 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), expHeader.getDepositId());

        logger.info("*** Step 7 Expected Results: The Deposits Detail screen is displayed correctly.");
        actHeader.setUser(this.userName);
        actHeader.setAccountingDate(ConvertUtil.convertStringToSQLDate(depositsUtil.determineDepAccountingDate(), DATE_FORMATTED_DEFAULT));
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, RELOAD_DEPOSITS);
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliation);
        depositsUtil.verifyBatchesSectionIsDisplayedCorrectly(actBatchesTable, "last()");

        logger.info("*** Step 8 Action: Update Bank Transaction ID with valid data and different with original Bank Transaction ID at step 4");
        deposits.clickOnBnkTransactionIdInput();
        BankReconciliation actBankReconciliationUpdate = depositsUtil.setValueInBankTransationSection(bankTransUpdated);
        depositsUtil.selectBank(actHeader.getBank());

        logger.info("*** Step 8 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliationUpdate);

        logger.info("*** Step 9 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 9 Expected Results: Load [Deposits] screen is displayed. Data are updated to DEP_BANK_TRANS table with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtBankReconciliationSectionIsSavedInDepBankTransTbl(actBankReconciliationUpdate, expHeader.getDepositId());

        logger.info("*** Step 10 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), expHeader.getDepositId());

        logger.info("*** Step 10 Expected Results: The Deposits Detail screen is displayed correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, RELOAD_DEPOSITS);

        logger.info("*** Step 11 Action: Click on Reset btn");
        deposits.clickOnResetBtn();

        logger.info("*** Step 11 Expected Results: Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

    }

    @Test(priority = 1, description = "Delete A Record in Batches tbl")
    public void dltRecInBatchesTbl() throws Throwable {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);

        logger.info("*** Step 1 Expected Results: User login success. Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data. Input value at Note in Deposit Notes section");
        Header expHeader = depositsUtil.setValuesAtHeader();
        String expNotes = depositsUtil.setValueAtDepositsNoteSection();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        Header actHeader = depositsUtil.getHeaderValue("");
        assertEquals(actHeader.toString(), expHeader.toString(), "        Added data is displayed correctly");
        depositsUtil.verifyDepositNotesSecTionIsDisplayedCorrectly(expNotes);

        logger.info("*** Step 4 Action: Add new two rows in Batches tbl with [Deposit Amount] input at Header = Total the [Batch Amount] at the two new rows");
        List<BatchesTable> actBatchesTables = depositsUtil.setBatchesTableMultipleRows(2, expHeader.getDepositAmount());

        logger.info("*** Step 5 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyBatchesDataAddedCorrectly(2, actBatchesTables );

        logger.info("*** Step 5 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 5 Expected Results: Load [Deposits] screen is displayed. Data are saved to 2 tables: Dep, Dep_Batch with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 6 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), expHeader.getDepositId());

        logger.info("*** Step 6 Expected Results: The Deposits Detail screen is displayed with data correctly.");
        depositsUtil.verifyBatchesDataAddedCorrectlyAfterReload(2, actBatchesTables );

        logger.info("*** Step 7 Action: Click the Delete checkbox on the first row in Batches tbl");
        BatchesTable oldTotalRow = depositsUtil.getRowTotalValue();
        clickHiddenPageObject(deposits.batchesTblDeleteColChk(String.valueOf(2)), 0);

        logger.info("*** Step 7 Expected Results: The row is remaked as deleted. The 'Total' will be automatically counted decrease/increase when delete checbox is checked.");
        depositsUtil.verifyRowMarkedAsDeleted(2);
        depositsUtil.verifyTotalDisplayedCorrectlyAfterDeleteRow(actBatchesTables.get(0), oldTotalRow);

        logger.info("*** Step 8 Action: Update the Deposit Amount input at Header = Total the Batch Amount column");
        depositsUtil.updateTheDepositAmountInputAtHeader(expHeader);

        logger.info("*** Step 8 Expected Results: Data is displayed correctly.");
        actHeader = depositsUtil.getHeaderValue(EMPTY_STRING);
        assertEquals(actHeader.getDepositAmount(), expHeader.getDepositAmount(),"        Data is displayed correctly at the header.");

        logger.info("*** Step 9 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 9 Expected Results: Load [Deposits] screen is displayed. The record in Dep_Batch tbl is removed correctly.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDepBatchRemoveInDB(expHeader, actBatchesTables);

        logger.info("*** Step 10 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), expHeader.getDepositId());

        logger.info("*** Step 10 Expected Results: The Deposits Detail screen is displayed correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, RELOAD_DEPOSITS);
        depositsUtil.verifyBatchesSectionIsDisplayedCorrectly(actBatchesTables.get(actBatchesTables.size()-1), LAST_ROW);

        logger.info("*** Step 11 Action: Click on Reset btn");
        deposits.clickOnResetBtn();

        logger.info("*** Step 11 Expected Results: Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "Delete A Deposit")
    public void deleteDeposit() throws Throwable {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);
        logger.info("*** Step 1 Expected Results: User login success. Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon.");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed.");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data.");
        Header actHeader = depositsUtil.setValuesAtHeader();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        Header expHeader = depositsUtil.getHeaderValue(EMPTY_STRING);
        assertEquals(actHeader.toString(), expHeader.toString(),"        Data is displayed correctly at the header.");

        logger.info("*** Step 4 Action: Input the Bank Transaction ID field with random data");
        BankReconciliation actBankReconciliation = depositsUtil.setValueInBankTransationSection();
        depositsUtil.selectBank(actHeader.getBank());

        logger.info("*** Step 4 Expected Results: Add icon and input data in the fields Data is displayed correctly.");
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliation);

        logger.info("*** Step 5 Action: Click Add icon and input data in the fields "
                + "(Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) of [Batches] tbl. Tab out.");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable actBatchesTable = depositsUtil.setBatchesValue(actHeader.getDepositAmount());

        logger.info("*** Step 5 Expected Results: The new row is added in Batches tbl with correct data.");
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTable,LAST_ROW);

        logger.info("*** Step 6 Action: Click on the [Save and Clear] button.");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 6 Expected Results: The [Deposits] screen is displayed. Data are saved to 3 tables: Dep, Dep_Batch, DEP_BANK_TRANS with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeader,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(actBatchesTable,actHeader.getDepositId(), 1);
        depositsUtil.verifyDataAtBankReconciliationSectionIsSavedInDepBankTransTbl(actBankReconciliation,actHeader.getDepositId());

        logger.info("*** Step 7 Action: Reload the [Deposit ID] above.");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 7 Expected Results: The Deposits Detail screen is displayed with data correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, RELOAD_DEPOSITS);
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliation);
        depositsUtil.verifyDepositNotesSecTionIsDisplayedCorrectly(EMPTY_STRING);
        depositsUtil.verifyBatchesSectionIsDisplayedCorrectly(actBatchesTable, LAST_ROW);

        logger.info("*** Step 8 Action: Click on the Delete Deposit checkbox.");
        clickOnElement(deposits.headerDeleteDepositChk());

        logger.info("*** Step 8 Expected Results: The Delete Deposit checkbox is checked.");
        depositsUtil.verifyDeleteDepositCheckboxIsChecked();

        logger.info("*** Step 9 Action: Click on the [Save and Clear] button.");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 9 Expected Results: Load [Deposits] screen is displayed."
                + "- Data are removed in 3 tables: Dep, Dep_Batch, DEP_BANK_TRANS.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyNoRecordExistsInDepDepBatchDepBankTransTbl(actHeader.getDepositId());

        logger.info("*** Step 10 Action: Reload the [Deposit ID] above.");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 10 Expected Results: The warning message is displayed with content: 'Invalid Deposit ID: + DepositID + does not exist.'.");
        depositsUtil.verifyErrorMessageInvalidDepositID(actHeader);
    }

    @Test(priority = 1, description = "Verify The Reset Button")
    public void verifyResetBtn() throws Throwable {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);

        logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon.");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed.");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data."
                + "- Input random value in the Note field at [Deposit Notes] grid.");
        Header actHeader = depositsUtil.setValuesAtHeader();
        depositsUtil.setValueAtDepositsNoteSection();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        Header expHeader = depositsUtil.getHeaderValue(EMPTY_STRING);
        assertEquals(actHeader.toString(), expHeader.toString(), "        Data is displayed correctly at the header.");

        logger.info("*** Step 4 Action: Input the Bank Transaction ID input with random data is got from (1).");
        BankReconciliation actBankReconciliation = depositsUtil.setValueInBankTransationSection();
        depositsUtil.selectBank(actHeader.getBank());

        logger.info("*** Step 4 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyBankReconciliationSectionInputIsDisplayedCorrectly(actBankReconciliation);

        logger.info("*** Step 5 Action: - Click Add icon and input data in the fields "
                + "(Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) of [Batches] tbl. Tab out.");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable actBatchesTable = depositsUtil.setBatchesValue(actHeader.getDepositAmount());

        logger.info("*** Step 5 Expected Results: The new row is added in Batches tbl with correct data.");
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTable, LAST_ROW);

        logger.info("*** Step 6 Action: Update the Deposit Amount input at Header = Total the Batch Amount column.");
        depositsUtil.updateTheDepositAmountInputAtHeader(actHeader);

        logger.info("*** Step 6 Expected Results: Data is displayed correctly.");
        expHeader = depositsUtil.getHeaderValue(EMPTY_STRING);
        assertEquals(actHeader.toString(), expHeader.toString(), "        Data is displayed correctly at the header.");

        logger.info("*** Step 7 Action: Click on the [Reset] button.");
        deposits.clickOnResetBtn();

        logger.info("*** Step 7 Expected Results: The Warning popup is displayed with content: 'Changes have been made to this page. Are you sure you want to reset the page?'.");
        depositsUtil.verifyWarningPopupIsDisplayedWithCorrectContent();

        logger.info("*** Step 8 Action: Click on the Reset button at the Warning popup.");
        clickOnElement(deposits.warningPopupResetBtn());

        logger.info("*** Step 8 Expected Results: Load [Deposits] screen is displayed."
                + "- No record is added in 3 tables: Dep, Dep_Batch, DEP_BANK_TRANS.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyNoRecordExistsInDepDepBatchDepBankTransTbl(actHeader.getDepositId());
    }

    @Test(priority = 1, description = "Verify the Deposit Amount Not Equals the Total Batch Amount of Batches tbl")
    public void verifyDepAmtNotEqlsTotalBatchAmt() throws Exception {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);

        logger.info("*** Step 1 Expected Results: User login successfully. The Deposits page is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed.");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data. Input random value in the Note field at [Deposit Notes] grid");
        Header actHeader = depositsUtil.setValuesAtHeader();
        String actNote = depositsUtil.setValueAtDepositsNoteSection();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, "");
        depositsUtil.verifyDepositNotesSecTionIsDisplayedCorrectly(actNote);

        logger.info("*** Step 4 Action: Click Add icon and Input data in the fields (Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) of [Batches] tbl. Tab out.");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable actBatchesTable = depositsUtil.setBatchesValue(actHeader.getDepositAmount());

        logger.info("*** Step 4 Expected Results: The new row is added in Batches tbl with correct data.");
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTable, "2");

        logger.info("*** Step 5 Action: Update value at [Deposit Amount] field at header that is different with value at [Batch Amount] in Batches tbl");
        float actDepositAmount = depositsUtil.setValueDepositAmount(actBatchesTable.getBatchAmount(), false);

        logger.info("*** Step 5 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyDepositAmountIsDisplayedCorrectly(actDepositAmount);

        logger.info("*** Step 6 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 6 Expected Results: The Error Retuns section is displayed with Warning message: 'Deposit Amount does not equal sum of batch amounts.'");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyErrorIsDisplayedCorrectly(DEPOSIT_AMT_NOT_EQUALS_SUM_OF_BATCH_AMT_MSG);

        logger.info("*** Step 7 Action: Click on Reset btn");
        deposits.clickOnResetBtn();

        logger.info("*** Step 7 Expected Results: The Warning popup is displayed with content: 'Changes have been made to this page. Are you sure you want to reset the page?'");
        depositsUtil.verifyWarningPopupIsDisplayedCorrectly(WARNING_MSG);

        logger.info("*** Step 8 Action: Click on [Reset] btn at warning popup");
        deposits.clickOnResetBtnAtWarningPopup();

        logger.info("*** Step 8 Expected Results: Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "Verify Field Validation popup is displayed when required fields are empty")
    public void verifyPopUp() throws Exception {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);

        logger.info("*** Step 1 Expected Results: User login successfully. The Deposits page is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed.");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 3 Expected Results: The warning popup is displayed at the Deposit Date input with message: 'This field is missing or invalid.'");
        xifinPortalUtils.waitForPageLoaded(wait);
        assertEquals(deposits.dateValidationPopupMessageTxt().getText(), "This field is missing or invalid.", "        The warning popup is displayed at the Deposit Date input with message: 'This field is missing or invalid.'");

        logger.info("*** Step 4 Action: Close the warning popup. Input the current date in the Deposit Date input");
        clickHiddenPageObject(deposits.dateValidationPopupCloseIco(), 0);

        String actDepositDate = timeStamp.getCurrentDate(DATE_FORMATTED_DEFAULT);
        enterValues(deposits.headerDepositDateInput(), actDepositDate);

        logger.info("*** Step 4 Expected Results: Data is displayed correctly.");
        String expDepositDate = deposits.headerDepositDateInput().getAttribute("value");
        assertEquals(actDepositDate, expDepositDate, "        Data is displayed correctly.");

        logger.info("*** Step 5 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 5 Expected Results: The warning popup is displayed at the Bank dropdownlist with message: 'This field is missing or invalid.'");
        xifinPortalUtils.waitForPageLoaded(wait);
        assertEquals(deposits.bankValidationPopupMessageTxt().getText(), "This field is missing or invalid.", "        The warning popup is displayed at the Bank dropdownlist with message: 'This field is missing or invalid.'");
        clickHiddenPageObject(deposits.bankValidationPopupCloseIco(), 0);

        logger.info("*** Step 6 Action: Click on the [Reset] button");
        deposits.clickOnResetBtn();

        logger.info("*** Step 6 Expected Results: The confirmed popup is displayed with content: 'Changes have been made to this page. Are you sure you want to reset the page?'");
        depositsUtil.verifyWarningPopupIsDisplayedCorrectly(WARNING_MSG);

        logger.info("*** Step 7 Action: Click on [Reset] btn at the popup");
        deposits.clickOnResetBtnAtWarningPopup();

        logger.info("*** Step 7 Expected Results: Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "Verify DepositId exist in Dep_Batch with FK_STA_ID = 4 (FK_DEP_BATCH_TYP_ID != 5)")
    public void verifyDepExistInDepBatch() throws Throwable {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);

        logger.info("*** Step 1 Expected Results: User login success. Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon.");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed.");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data.");
        Header actHeader = depositsUtil.setValuesAtHeader();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        Header expHeader = depositsUtil.getHeaderValue(EMPTY_STRING);
        assertEquals(actHeader.toString(), expHeader.toString(), "        Data is displayed correctly at the header.");

        logger.info("*** Step 4 Action: - Click Add icon and enter valid data at the fields (Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) in Batches tbl.");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable actBatchesTable = depositsUtil.setBatchesValue(actHeader.getDepositAmount());

        logger.info("*** Step 4 Expected Results: The new row is added in Batches tbl with correct data.");
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTable, LAST_ROW);

        logger.info("*** Step 5 Action: Click on the [Save and Clear] button.");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 5 Expected Results: Load [Deposits] screen is displayed. Data are saved to 2 tables: Dep, Dep_Batch with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeader,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(actBatchesTable,actHeader.getDepositId(), 1);

        logger.info("*** Step 6 Action: Reload the [Deposit ID] above.");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 6 Expected Results: - The Deposits Detail screen is displayed correctly with Status in Batches tbl = Assigned."
                + " [Post Non-AR Amt] field, delete row icon in Batches are enable");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, RELOAD_DEPOSITS);
        depositsUtil.verifyBankReconciliationSectionIsEmpty();
        depositsUtil.verifyDepositNotesSecTionIsDisplayedCorrectly(EMPTY_STRING);
        depositsUtil.verifyBatchesSectionIsDisplayedCorrectly(actBatchesTable, LAST_ROW);
        depositsUtil.verifyStatusColInBatchesTblIsDisplayedCorrectly("2", ASSIGNED_TEXT);
        depositsUtil.verifyStatusOfPostNonARAmtColAndDeleteColInBatchesTblAreEnable("2", true,true);

        logger.info("*** Step 7 Action: - Click on Reset btn."
                + "- Update Dep_Batch.FK_STA_ID = 4 of Deposit below."
                + "- Reload the [Deposit ID] above.");
        deposits.clickOnResetBtn();
        xifinPortalUtils.waitForPageLoaded(wait);
        List<DepBatch> depBatchs = rpmDao.getDepBatchByDepId(testDb, actHeader.getDepositId());
        depositsUtil.updateDepBatchByStaId(depBatchs.get(0), 4);
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 7 Expected Results:  The Deposits Detail screen is displayed correctly with Status in Batches tbl = Remit Error."
                + "- [Post Non-AR Amt] field, delete row icon in Batches are disable.");
        depositsUtil.verifyStatusColInBatchesTblIsDisplayedCorrectly("2", REMIT_ERROR_TEXT);
        depositsUtil.verifyStatusOfPostNonARAmtColAndDeleteColInBatchesTblAreEnable("2", false, false);

        logger.info("*** Step 8 Action: - Click Add icon and enter valid data at the fields (Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) in Batches tbl. "
                + "Update [Deposit Amount] at header = Total Batch Amount in Batches tbl");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable actBatchesTable2 = depositsUtil.setBatchesValue(actHeader.getDepositAmount());
        depositsUtil.updateTheDepositAmountInputAtHeader(actHeader);

        logger.info("*** Step 8 Expected Results: The new row is added in Batches tbl with correct data.");
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTable2, LAST_ROW);

        logger.info("*** Step 9 Action: - Click on the [Save and Clear] button.");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 9 Expected Results: - Load [Deposits] screen is displayed."
                + "- Data are updated in Dep table and added in Dep_Batch tbl with correct data."
                + "- The old row in Dep_Batch that FK_STA_ID = 4 is updated FK_STA_ID = 2.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeader,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(actBatchesTable2, actHeader.getDepositId(), 2);
        depositsUtil.verifyTheFirstRowInDepBatchTblIsUpdatedCorrectly(actHeader.getDepositId(), 0, 2);

        logger.info("*** Step 10 Action: Reload the [Deposit ID] above.");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 10 Expected Results: - The Deposits Detail screen is displayed correctly with Status in Batches tbl = Assigned."
                + "- [Post Non-AR Amt] field, delete row icon in Batches are enable.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, RELOAD_DEPOSITS);
        depositsUtil.verifyBankReconciliationSectionIsEmpty();
        depositsUtil.verifyDepositNotesSecTionIsDisplayedCorrectly(EMPTY_STRING);
        depositsUtil.verifyBatchesSectionIsDisplayedCorrectly(actBatchesTable2, LAST_ROW);
        depositsUtil.verifyStatusColInBatchesTblIsDisplayedCorrectly("2", ASSIGNED_TEXT);
        depositsUtil.verifyStatusOfPostNonARAmtColAndDeleteColInBatchesTblAreEnable("2", true, true);

        logger.info("*** Step 11 Action: Click on Reset btn.");
        deposits.clickOnResetBtn();

        logger.info("*** Step 11 Expected Results: Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "Verify DepositId exist in Dep_Batch with FK_DEP_BATCH_TYP_ID = 5")
    public void verifyDepExistInDepBatchTyp() throws Exception {
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter(driver);
        deposits = new Deposits(driver, wait);

        logger.info("*** Step 1 Expected Results: User login successfully. The Deposits page is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on the Create Deposit icon");
        deposits.clickOnCreateDepositIco();

        logger.info("*** Step 2 Expected Results: The Deposits Detail screen is displayed.");
        depositsUtil.verifyPaymentDepositsDetailPageIsDisplayed();
        depositsUtil.verifyDepositsDetailScreenIsEmpty();

        logger.info("*** Step 3 Action: Input the required fields at header with valid data");
        Header actHeader = depositsUtil.setValuesAtHeader();

        logger.info("*** Step 3 Expected Results: Data is displayed correctly.");
        depositsUtil.verifyHeaderIsDisplayedCorrectly(actHeader, "");

        logger.info("*** Step 4 Action: Click Add icon and enter valid data at the fields (Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) in Batches tbl.");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable actBatchesTable = depositsUtil.setBatchesValue(actHeader.getDepositAmount());

        logger.info("*** Step 4 Expected Results: The new row is added in Batches tbl with correct data.");
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTable, "2");

        logger.info("*** Step 5 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 5 Expected Results: Load [Deposits] screen is displayed. Data are saved to 2 tables: Dep, Dep_Batch with correct data.");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeader,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(actBatchesTable, actHeader.getDepositId(), 1);

        logger.info("*** Step 6 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 6 Expected Results: The Deposits Detail screen is displayed correctly with Status in Batches tbl = Assigned."
                + "- [Post Non-AR Amt] field, delete row icon in Batches are enable");
        depositsUtil.verifyStatusColInBatchesTblIsDisplayedCorrectly("2", ASSIGNED_TEXT);
        depositsUtil.verifyStatusOfPostNonARAmtColAndDeleteColInBatchesTblAreEnable("2", true, true);

        logger.info("*** Step 7 Action: Click on Reset btn."
                + "- Update Dep_Batch.FK_DEP_BATCH_TYP_ID = 5 of Deposit below"
                + "- Reload the [Deposit ID] above");
        deposits.clickOnResetBtn();
        List<DepBatch> depBatchs = rpmDao.getDepBatchByDepId(testDb, actHeader.getDepositId());
        depositsUtil.updateDepBatch(depBatchs.get(0), 5);
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 7 Expected Results: - The Deposits Detail screen is displayed correctly with Status in Batches tbl = Assigned."
                + "- [Post Non-AR Amt] field, delete row icon in Batches are disable");
        depositsUtil.verifyStatusColInBatchesTblIsDisplayedCorrectly("2", ASSIGNED_TEXT);
        depositsUtil.verifyStatusOfPostNonARAmtColAndDeleteColInBatchesTblAreEnable("2", false, false);

        logger.info("*** Step 8 Action: Click Add icon and enter valid data at the fields (Payor ID, Assigned User, Non-AR Amount, G/L Account, Note) in Batches tbl.");
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        BatchesTable actBatchesTable2 = depositsUtil.setBatchesValue(actHeader.getDepositAmount());
        float actDepositAmount = depositsUtil.setValueDepositAmount(actBatchesTable.getBatchAmount() + actBatchesTable2.getBatchAmount(), true);

        logger.info("*** Step 8 Expected Results: The new row is added in Batches tbl with correct data.");
        depositsUtil.verifyNewRowInBatchesTblIsAddedCorrectly(actBatchesTable2, LAST_ROW);
        depositsUtil.verifyDepositAmountIsDisplayedCorrectly(actDepositAmount);
        actHeader.setDepositAmount(actDepositAmount);

        logger.info("*** Step 9 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 9 Expected Results: - Load [Deposits] screen is displayed."
                + "- Data are saved to 2 tables: Dep, Dep_Batch with correct data."
                + "- The old row in Dep_Batch that FK_DEP_BATCH_TYP_ID  = 5 is updated FK_DEP_BATCH_TYP_ID = 1 and FK_STA_ID = 3");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyDataAtHeaderAreSavedInDepTbl(actHeader,this.userName);
        depositsUtil.verifyDataAtBatchesTblIsSavedInDepBatchTbl(actBatchesTable2, actHeader.getDepositId(), 2);
        depositsUtil.verifyTheFirstRowInDepBatchTblIsUpdatedCorrectly(actHeader.getDepositId(), 1, 3);

        logger.info("*** Step 10 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 10 Expected Results: The Deposits Detail screen is displayed correctly with Status in Batches tbl = WIP."
                + "- [Post Non-AR Amt] field, delete row icon in Batches are enable");
        depositsUtil.verifyStatusColInBatchesTblIsDisplayedCorrectly("2", WIP_TEXT);
        depositsUtil.verifyStatusOfPostNonARAmtColAndDeleteColInBatchesTblAreEnable("2", true, true);

        logger.info("*** Step 11 Action: Click on the [Save and Clear] button");
        deposits.clickOnSaveAndClearBtn();

        logger.info("*** Step 11 Expected Results: Load [Deposits] screen is displayed. The old row in Dep_Batch that FK_STA_ID = 3 is updated FK_STA_ID = 2");
        xifinPortalUtils.waitForPageLoaded(wait);
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
        depositsUtil.verifyTheFirstRowInDepBatchTblIsUpdatedCorrectly(actHeader.getDepositId(), 1, 2);

        logger.info("*** Step 12 Action: Reload the [Deposit ID] above");
        enterValues(deposits.depositsSectionDepositIdInput(), actHeader.getDepositId());

        logger.info("*** Step 12 Expected Results: The Deposits Detail screen is displayed correctly with Status in Batches tbl = Assigned."
                + "- [Post Non-AR Amt] field, delete row icon in Batches are enable");
        depositsUtil.verifyStatusColInBatchesTblIsDisplayedCorrectly("2", ASSIGNED_TEXT);
        depositsUtil.verifyStatusOfPostNonARAmtColAndDeleteColInBatchesTblAreEnable("2", true, true);

        logger.info("*** Step 13 Action: Click on Reset btn.");
        deposits.clickOnResetBtn();

        logger.info("*** Step 13 Expected Results: Load [Deposits] screen is displayed.");
        depositsUtil.verifyPaymentDepositsLoadPageIsDisplayed();
    }

}