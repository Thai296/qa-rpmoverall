package com.overall.utils;

import com.mbasys.mars.ejb.entity.bank.Bank;
import com.mbasys.mars.ejb.entity.bankBaiTyp.BankBaiTyp;
import com.mbasys.mars.ejb.entity.bankFileHist.BankFileHist;
import com.mbasys.mars.ejb.entity.bankTrans.BankTrans;
import com.mbasys.mars.ejb.entity.dep.Dep;
import com.mbasys.mars.ejb.entity.depBankTrans.DepBankTrans;
import com.mbasys.mars.ejb.entity.depBatch.DepBatch;
import com.mbasys.mars.ejb.entity.users.Users;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.payment.paymentPayments.Deposits;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.BankDaoImpl;
import com.xifin.qa.dao.rpm.DepDaoImpl;
import com.xifin.qa.dao.rpm.GeneralLedgerCodeDaoImpl;
import com.xifin.qa.dao.rpm.PaymentDaoImpl;
import com.xifin.qa.dao.rpm.PayorDaoImpl;
import com.xifin.qa.dao.rpm.RpmDaoImpl;
import com.xifin.qa.dao.rpm.SystemDaoImpl;
import com.xifin.qa.dao.rpm.DataBaseSequenceDaoImpl;
import com.xifin.utils.*;
import domain.payment.deposits.BankReconciliation;
import domain.payment.deposits.BatchesTable;
import domain.payment.deposits.Header;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class PaymentDepositsUtil extends SeleniumBaseTest{
    Deposits deposits;
    ConvertUtil convertUtil;
    RandomCharacter randomCharacter;
    DateFormat formatter;
    TimeStamp timeStamp;
    XifinPortalUtils xifinPortalUtils;


    private static final String EMPTY_STRING = "";
    private static final String COMPLETE_STRING = "Complete";
    private static final String DATE_FORMATTED_DEFAULT = "MM/dd/yyyy";
    private static final String WARNING_TITLE = "Warning";
    private static final String PAYMENT_DEPOSITS_TITLE = "Deposits";
    private static final String SUFFIX_ERROR_MESSAGE_TEXT = " does not exist.";
    private static final String PRE_ERROR_MESSAGE_TEXT = "Invalid Deposit ID: ";
    private static final String ADD_NEW_DEPOSITS = " Add new record when click on Create Deposis icon";
    private static final String WARNING_MSG = "Changes have been made to this page. Are you sure you want to reset the page?";

    public PaymentDepositsUtil(RemoteWebDriver driver, WebDriverWait wait, Configuration config){
        this.config = config;
        this.wait = wait;
        this.driver = driver;
        timeStamp = new TimeStamp();
        deposits = new Deposits(driver,wait);
        convertUtil = new ConvertUtil();
        randomCharacter = new RandomCharacter(driver);
        formatter = new SimpleDateFormat(DATE_FORMATTED_DEFAULT);
        xifinPortalUtils = new XifinPortalUtils(driver);
        paymentDao = new PaymentDaoImpl(config.getRpmDatabase());
        systemDao = new SystemDaoImpl(config.getRpmDatabase());
        payorDao = new PayorDaoImpl(config.getRpmDatabase());
        rpmDao = new RpmDaoImpl(config.getRpmDatabase());
        bankDao = new BankDaoImpl(config.getRpmDatabase());
        depDao = new DepDaoImpl(config.getRpmDatabase());
        databaseSequenceDao = new DataBaseSequenceDaoImpl(config.getRpmDatabase());
        generalLedgerCodeDao = new GeneralLedgerCodeDaoImpl(config.getRpmDatabase());
    }

    public Header mapToHeader(int depID) throws Exception {
        Header header = new Header();
        Dep dep = paymentDao.getDepByDepId(depID);

        header.setDepositId(dep.getDepId());
        header.setDepositDate(dep.getRemitDt());
        header.setAccountingDate(dep.getAcctngDt());
        header.setDepositAmount((float) dep.getDepAmt());
        header.setBank(bankDao.getBankByBankId(dep.getBankId()).getAbbrv());
        header.setEraId(dep.getEraId() == null ? EMPTY_STRING : dep.getEraId());
        header.setEftId(dep.getEftId() == null ? EMPTY_STRING : dep.getEftId());
        header.setUser(dep.getUserId());
        header.setDeleteDeposit(dep.isDeleted);

        return header;
    }

    public BatchesTable mapToBatches(int depID, int batchId) throws Exception {
        BatchesTable batchesTable = new BatchesTable();

        DepBatch depBatch = paymentDao.getDepBatchByDepIdAndBatchId(depID, batchId);

        batchesTable.setBatchId(depBatch.getBatchId());
        batchesTable.setPayorId(payorDao.getPyrByPyrId(depBatch.getPyrId()).getPyrAbbrv());
        batchesTable.setBatchAmount((float) depBatch.getBatchAmt());
        float unappliedAmount = (float) (depBatch.getBatchAmt() - depBatch.getApplAmt() - depBatch.getNonArAmt());
        batchesTable.setUnappliedAmount(unappliedAmount);
        batchesTable.setAppliedAmount((float) depBatch.getApplAmt());
        batchesTable.setPostedAmount((float) depBatch.getPostAmt());
        batchesTable.setLastActivity(depBatch.getLastActvDt());
        if (depBatch.getStaId() == 6) {
            batchesTable.setStatus(COMPLETE_STRING);
        }

        batchesTable.setAssignedUser(convertUtil.convertNull(depBatch.getUserId()));
        batchesTable.setNonARAmount((float)depBatch.getNonArAmt());
        batchesTable.setGlAccount(depBatch.getGlCdId().equals("0") ? EMPTY_STRING : generalLedgerCodeDao.getGlCdByGlCdId(depBatch.getGlCdId()).getDescr());
        batchesTable.setNotes(convertUtil.convertNull(depBatch.getNote()));
        batchesTable.setPostNonArAmt(depBatch.getNonArPostDt() != null);

        return batchesTable;
    }

    public BankReconciliation mapToBankReconciliation(int depID) throws Exception {
        BankReconciliation bankReconciliation = new BankReconciliation();

        DepBankTrans depBankTrans = bankDao.getDepBankTransByDepId(depID);
        BankTrans bankTrans = bankDao.getBankTransBySeqId(depBankTrans.getBankTransSeqId());
        bankReconciliation.setBankId(depBankTrans.getBankTransSeqId());
        bankReconciliation.setBankDate(ConvertUtil.convertStringToSQLDate(formatter.format(bankTrans.getBankDt()), DATE_FORMATTED_DEFAULT));
        bankReconciliation.setBankAmount(bankTrans.getAmt());

        return bankReconciliation;
    }
    public BatchesTable setBatchAmountAtBatchesTable(BatchesTable actBatchesTable, float depositAmt) throws Exception {
        enterValues(deposits.batchesTblBatchAmtColInput(), depositAmt);

        actBatchesTable.setBatchAmount(depositAmt);
        actBatchesTable.setUnappliedAmount(depositAmt);

        return actBatchesTable;
    }

    public BatchesTable setBatchesValue(float depositAmt) throws Exception{
        BatchesTable batchesTable = new BatchesTable();
        Users assignedUsers = paymentDao.getRandomAssignedUserForPaymentDeposit();

        String payorID = payorDao.getRandomPyr().getPyrAbbrv();
        enterValues(deposits.batchesTblPayorIdColInput(), payorID);

        String depositAmtCurrentAsString = deposits.batchesTblBatchAmtColInput().getAttribute("value");
        float depositAmtCurrentAsFloat = depositAmtCurrentAsString.equals("") ? 0 : Float.parseFloat(depositAmtCurrentAsString);
        if(depositAmtCurrentAsFloat != depositAmt) {
            enterValues(deposits.batchesTblBatchAmtColInput(), depositAmt);
        }
        enterValues(deposits.batchesTblAssignedUserColInput(), assignedUsers.getUserId());

        String nonArAmt = randomCharacter.getRandomNumericString(3);
        enterValues(deposits.batchesTblNonARAmtColInput(), nonArAmt);

        String glAccount = generalLedgerCodeDao.getGlCd().getDescr();
        selectItem(deposits.batchesTblGLAccountColSel(), glAccount);

        String randomNotes = randomCharacter.getRandomAlphaNumericString(10);
        enterValues(deposits.batchesTblNoteColInput(), randomNotes);

        batchesTable.setBatchId(Integer.valueOf(deposits.batchesTblDataRows().size()));
        batchesTable.setPayorId(payorID);
        batchesTable.setBatchAmount(depositAmt);
        batchesTable.setUnappliedAmount(depositAmt - Float.valueOf(nonArAmt));
        batchesTable.setAppliedAmount(0);
        batchesTable.setPostedAmount(0);
        batchesTable.setLastActivity(null);
        batchesTable.setStatus("");
        batchesTable.setAssignedUser(assignedUsers.getUserId());
        batchesTable.setNonARAmount(Float.valueOf(nonArAmt));
        batchesTable.setGlAccount(glAccount);
        batchesTable.setNotes(randomNotes);
        batchesTable.setPostNonArAmt(deposits.batchesTblPostNonARAmtColChk("last()").isSelected());

        return batchesTable;
    }

    public List<BatchesTable> setBatchesTableMultipleRows(int rownum, float totalDepositAmt) throws Exception {
        List<BatchesTable> batchesTables = new ArrayList<>();
        float appliedAmt = 0;
        for (int i = 0; i < rownum-1; i++) {
            clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
            batchesTables.add(setBatchesValue(totalDepositAmt - 1));
            appliedAmt += totalDepositAmt - 1;
        }
        clickHiddenPageObject(deposits.batchesTblAddBtn(), 0);
        batchesTables.add(setBatchesValue(totalDepositAmt - appliedAmt));

        return batchesTables;
    }

    public Header setValuesAtHeader() throws Exception{
        Header header = new Header();

        int depositId = Integer.parseInt(deposits.headerDepositIdTxt().getAttribute("value"));
        String depositDate = timeStamp.getCurrentDate(DATE_FORMATTED_DEFAULT);
        float depositAmount = Float.parseFloat(randomCharacter.getNonZeroRandomNumericString(2));
        String bank = bankDao.getBank().getAbbrv();

        setValuesAtHeader(depositDate,depositAmount,  bank);
        header.setDepositId(depositId);
        header.setDepositDate(new Date(formatter.parse(depositDate).getTime()));
        header.setDepositAmount(depositAmount);
        header.setBank(bank);

        return header;
    }


    private void setValuesAtHeader(String depositDate,float depositAmount, String bank) throws Exception {

        enterValues(deposits.headerDepositDateInput(), depositDate);
        String depositAmountCurrentAsString = deposits.headerDepositAmtInput().getAttribute("value");
        float depositAmountCurrentAsFloat = depositAmountCurrentAsString.equals("") ? 0 : Float.parseFloat(depositAmountCurrentAsString);
        if(depositAmountCurrentAsFloat!= depositAmount) {
            enterValues(deposits.headerDepositAmtInput(), depositAmount);
        }
        selectItem(deposits.headerBankSel(), bank);
    }

    private BankTrans prepareBankTrans() throws Exception {
        BankTrans bankTrans;
        try {
            bankTrans = bankDao.getBankTransWithAmtNotNullFromBankTrans();
        }
        catch (XifinDataNotFoundException e) {
            bankTrans = createBankTrans();
        }
        catch (Exception e) {
            throw e;
        }
        return bankTrans;
    }

    public BankTrans createBankTrans() throws Exception {
        BankTrans bankTrans = new BankTrans();
        bankTrans.setResultCode(ErrorCodeMap.NEW_RECORD);
        Bank bank = bankDao.getBank();
        bankTrans.setBankId(bank.getBankId());
        int nextSeqId = databaseSequenceDao.getNextSeqIdFromTBLByColName("PK_SEQ_ID", "bank_trans");
        int seqId = nextSeqId == 0 ? 1 : nextSeqId;
        bankTrans.setSeqId(seqId);
        BankFileHist bankFileHist = paymentDao.getRandomBankFileHist();
        if (bankFileHist == null){
            prepareBankFileHist();
            bankFileHist = paymentDao.getRandomBankFileHist();
        }
        bankTrans.setBankFileHistSeqId(bankFileHist.getSeqId());
        BankBaiTyp bankBaiTypId = paymentDao.getRandomBankBaiTyp();
        bankTrans.setBankBaiTypId(bankBaiTypId.getBankBaiTypId());
        bankTrans.setAcctNum(randomCharacter.getNonZeroRandomNumericString(5));
        bankTrans.setBankDt(getCurrentTimeStamp());
        bankTrans.setAmt(Float.parseFloat(randomCharacter.getNonZeroRandomNumericString(5)));
        paymentDao.setBankTrans(bankTrans);
        return bankTrans;
    }
    private BankFileHist prepareBankFileHist() throws Exception {
        BankFileHist bankFileHist = paymentDao.getRandomBankFileHist();
        if(bankFileHist == null) {
            bankFileHist = new BankFileHist();
            bankFileHist.setResultCode(ErrorCodeMap.NEW_RECORD);
            int nextSeqId = databaseSequenceDao.getNextSeqIdFromTBLByColName("PK_SEQ_ID", "bank_file_hist");
            int seqId = nextSeqId == 0 ? 1 : nextSeqId;
            bankFileHist.setSeqId(seqId);
            bankFileHist.setFileName(randomCharacter.getRandomAlphaNumericString(10));
            bankFileHist.setLoadDt(getCurrentTimeStamp());
            String bankFileFmtTypId = paymentDao.getRandomBankFileFmtTyp();
            bankFileHist.setBankFileFmtTypId(bankFileFmtTypId);
            String bankFileResultTypId = paymentDao.getRandomBankFileResultTyp();
            bankFileHist.setBankFileResultTypId(bankFileResultTypId);
            bankFileHist.setErrCnt(0);
            bankFileHist.setDupCnt(0);
            paymentDao.setBankFileHist(bankFileHist);
        }
        return bankFileHist;
    }

    private Timestamp getCurrentTimeStamp() {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return new java.sql.Timestamp(sqlDate.getTime());
    }

    public BankReconciliation setValueInBankTransationSection() throws Exception {
        deposits.clickOnBnkReconciliationAddBtn();
        BankTrans bankTrans = null;
        try {
            bankTrans = bankDao.getBankTransWithAmtNotNullFromBankTrans();
        }
        catch (XifinDataNotFoundException e){
            prepareBankTrans();
        }
        return setValueInBankTransationSection(bankTrans);
    }


    public BankReconciliation setValueInBankTransationSection(BankTrans bankTrans) throws Exception {
        xifinPortalUtils.waitForPageLoaded(wait);
        BankReconciliation bankReconciliation = new BankReconciliation();
        enterValues(deposits.bnkReconciliationTblBnkTransIdInput(), bankTrans.getSeqId());
        bankReconciliation.setBankAmount(bankTrans.getAmtAsMoney().floatValue());
        bankReconciliation.setBankDate(ConvertUtil.convertStringToSQLDate(formatter.format(bankTrans.getBankDt()), DATE_FORMATTED_DEFAULT));
        bankReconciliation.setBankId(bankTrans.getSeqId());

        enterValues(deposits.bnkReconciliationTblBnkTransIdFilter(), bankTrans.getSeqId());
        return bankReconciliation;
    }

    public String setValueAtDepositsNoteSection() throws Exception {
        String note = randomCharacter.getRandomAlphaNumericString(15);
        enterValues(deposits.depositNotesSectionNoteInput(), note);
        deposits.depositNotesSectionNoteInput().click();
        return note;
    }
    public float setValueDepositAmount(float totalBatchAmount, boolean isEqualTotal) throws Exception {
        float actDepositAmount = totalBatchAmount;

        if(!isEqualTotal) {
            do {
                actDepositAmount = Float.parseFloat(randomCharacter.getNonZeroRandomNumericString(2));
            } while (actDepositAmount == totalBatchAmount);
        }
        enterValues(deposits.headerDepositAmtInput(), actDepositAmount);

        return actDepositAmount;
    }

    public BatchesTable setPayorIdAtBatchesTable(BatchesTable actBatchesTable) throws Exception {
        String payorID = payorDao.getRandomPyr().getPyrAbbrv();

        enterValues(deposits.batchesTblPayorIdColInput(), payorID);

        actBatchesTable.setBatchId(Integer.valueOf(deposits.batchesTblDataRows().size()));
        actBatchesTable.setPayorId(payorID);

        return actBatchesTable;
    }

    public BatchesTable setNonARAmountAtBatchesTable(BatchesTable actBatchesTable) throws Exception {
        String nonArAmt = randomCharacter.getRandomNumericString(3);

        enterValues(deposits.batchesTblNonARAmtColInput(), nonArAmt);

        actBatchesTable.setNonARAmount(Float.parseFloat(nonArAmt));
        actBatchesTable.setUnappliedAmount(actBatchesTable.getBatchAmount() - Float.valueOf(nonArAmt));

        return actBatchesTable;
    }

    public Header getHeaderValue(String typ) throws Exception {
        Header header = new Header();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");

        wait.until(ExpectedConditions.elementToBeClickable(deposits.headerDeleteDepositChk()));
        assertTrue(isElementPresent(deposits.headerDeleteDepositChk(), 5), "        Delete Deposit at header is displayed");

        String depositId = deposits.headerDepositIdTxt().getAttribute("value");
        String depositDtAsStr = deposits.headerDepositDateInput().getAttribute("value").trim();
        String accountingDtAsStr = deposits.headerAccountingDateInput().getAttribute("value").trim();
        String depositAmt = deposits.headerDepositAmtInput().getAttribute("value").trim();
        String bank = getSelectedTextOnDropdown(deposits.headerBankSel()).getText();
        String eraId = deposits.headerEraIdTxt().getText().trim();
        String effId = deposits.headerEffIdTxt().getText().trim();
        String user = deposits.headerUserTxt().getText().trim();
        boolean isDeleted = deposits.headerDeleteDepositChk().isSelected();

        if(!typ.equals(ADD_NEW_DEPOSITS)){
            header.setDepositId(Integer.parseInt(depositId));
        }
        header.setDepositDate(ConvertUtil.convertStringToSQLDate(depositDtAsStr, DATE_FORMATTED_DEFAULT));
        header.setAccountingDate(ConvertUtil.convertStringToSQLDate(accountingDtAsStr, DATE_FORMATTED_DEFAULT));
        header.setDepositAmount(decimalFormat.parse(depositAmt).floatValue());
        header.setBank(bank);
        header.setEraId(eraId);
        header.setEftId(effId);
        header.setUser(user);
        header.setDeleteDeposit(isDeleted);

        return header;
    }

    private BatchesTable getBatchesValue(String row) throws Exception {
        BatchesTable batchesTable = new BatchesTable();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");

        assertTrue(isElementPresent(deposits.batchesTblDeleteColChk(row), 5), "        Deleted at Batches table is displayed");

        String batchId = deposits.batchesTblBatchIdColTxt(row).getText();
        String payorId = deposits.batchesTblPayorIdColTxt(row).getText();
        String batchAmt = deposits.batchesTblBatchAmtColTxt(row).getText();
        String unappliedAmt = deposits.batchesTblUnappliedAmtColTxt(row).getText();
        String appliedAmt = deposits.batchesTblAppliedAmtColTxt(row).getText();
        String postedAmt = deposits.batchesTblPostedAmtColTxt(row).getText();
        String lastActivity = deposits.batchesTblLastActivityColTxt(row).getText();
        String status = deposits.batchesTblStatusColTxt(row).getText().trim();
        String assignedUser = deposits.batchesTblAssignedUserColTxt(row).getText().trim();
        String nonARAmt = deposits.batchesTblNonARAmtColTxt(row).getText();
        String glAccount = deposits.batchesTblGLAccountColTxt(row).getText();
        String notes = deposits.batchesTblNoteColTxt(row).getText();
        boolean postNonARAmt = deposits.batchesTblPostNonARAmtColChk(row).isSelected();
        boolean delete = deposits.batchesTblDeleteColChk(row).isSelected();

        batchesTable.setBatchId(Integer.parseInt(batchId));
        batchesTable.setPayorId(payorId);
        batchesTable.setBatchAmount(decimalFormat.parse(batchAmt).floatValue());
        batchesTable.setUnappliedAmount(decimalFormat.parse(unappliedAmt).floatValue());
        batchesTable.setAppliedAmount(decimalFormat.parse(appliedAmt).floatValue());
        batchesTable.setPostedAmount(decimalFormat.parse(postedAmt).floatValue());
        if (!lastActivity.isBlank()) {
            batchesTable.setLastActivity(ConvertUtil.convertStringToSQLDate(lastActivity, DATE_FORMATTED_DEFAULT));
        }
        batchesTable.setStatus(status);
        batchesTable.setAssignedUser(assignedUser);
        batchesTable.setNonARAmount(decimalFormat.parse(nonARAmt).floatValue());
        batchesTable.setGlAccount(glAccount);
        batchesTable.setNotes(notes);
        batchesTable.setPostNonArAmt(postNonARAmt);
        batchesTable.setDeleted(delete);

        return batchesTable;
    }

    private BankReconciliation getValuesInputInBankReconciliationSection() throws Exception {
        BankReconciliation bankReconciliation = new BankReconciliation();

        //Get value Bank Reconciliation
        String bnkDate = deposits.bnkReconciliationTblBnkDateTxt().getText();
        Date bnkDt = (bnkDate.isEmpty() ? null : ConvertUtil.convertStringToSQLDate(bnkDate, DATE_FORMATTED_DEFAULT));

        String amt = deposits.bnkReconciliationTblBnkAmountTxt().getText();
        float bnkAmt = (amt.isEmpty() ? 0 : Float.parseFloat(amt.replace(",", "")));

        String id = deposits.bnkReconciliationTblBnkTransIdTxt().getText();
        int bnkId = (id.isEmpty() ? 0 : Integer.parseInt(id));

        //Set value Bank Reconciliation
        bankReconciliation.setBankAmount(bnkAmt);
        bankReconciliation.setBankDate(bnkDt);
        bankReconciliation.setBankId(bnkId);

        return bankReconciliation;
    }

    public BatchesTable getRowTotalValue() throws ParseException {
        BatchesTable batchesTable = new BatchesTable();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");

        batchesTable.setBatchAmount(decimalFormat.parse(deposits.batchesTblTotalBatchAmtColTxt().getText()).floatValue());
        batchesTable.setUnappliedAmount(decimalFormat.parse(deposits.batchesTblTotalUnappliedAmtColTxt().getText()).floatValue());
        batchesTable.setAppliedAmount(decimalFormat.parse(deposits.batchesTblTotalAppliedAmtColTxt().getText()).floatValue());
        batchesTable.setPostedAmount(decimalFormat.parse(deposits.batchesTblTotalPostedAmtColTxt().getText()).floatValue());
        batchesTable.setNonARAmount(decimalFormat.parse(deposits.batchesTblTotalNonARAmtColTxt().getText()).floatValue());

        return batchesTable;
    }

    BatchesTable batchesTable = new BatchesTable();
    public BatchesTable getBatchesValueByInput(String row) throws Exception {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");

        String batchId = deposits.batchesTblBatchIdInput().getAttribute("value");
        String payorId = deposits.batchesTblPayorIdColInput().getAttribute("value");
        String batchAmt = deposits.batchesTblBatchAmtColInput().getAttribute("value");
        String unappliedAmt = deposits.batchesTblUnappliedAmtColInput().getAttribute("value");
        String appliedAmt = deposits.batchesTblAppliedAmtColInput().getAttribute("value");
        String postedAmt = deposits.batchesTblPostedAmtColInput().getAttribute("value");
        String lastActivity = deposits.batchesTblLastActivityColTxt(row).getText();
        String status = deposits.batchesTblStatusColTxt(row).getText().trim();
        String assignedUser = deposits.batchesTblAssignedUserColInput().getAttribute("value");
        String nonARAmt = deposits.batchesTblNonARAmtColInput().getAttribute("value");
        String glAccount = getSelectedTextOnDropdown(deposits.batchesTblGLAccountColSel()).getText();
        String notes = deposits.batchesTblNoteColInput().getAttribute("value");
        boolean postNonARAmt = deposits.batchesTblPostNonARAmtColChk(row).isSelected();
        boolean delete = deposits.batchesTblDeleteColChk(row).isSelected();

        batchesTable.setBatchId(Integer.parseInt(batchId));
        batchesTable.setPayorId(payorId);
        batchesTable.setBatchAmount(decimalFormat.parse(batchAmt).floatValue());
        batchesTable.setUnappliedAmount(decimalFormat.parse(unappliedAmt).floatValue());
        batchesTable.setAppliedAmount(decimalFormat.parse(appliedAmt).floatValue());
        batchesTable.setPostedAmount(decimalFormat.parse(postedAmt).floatValue());
        if (!lastActivity.isBlank()) {
            batchesTable.setLastActivity(ConvertUtil.convertStringToSQLDate(lastActivity, DATE_FORMATTED_DEFAULT));
        }
        batchesTable.setStatus(status);
        batchesTable.setAssignedUser(assignedUser);
        batchesTable.setNonARAmount(decimalFormat.parse(nonARAmt).floatValue());
        batchesTable.setGlAccount(glAccount);
        batchesTable.setNotes(notes);
        batchesTable.setPostNonArAmt(postNonARAmt);
        batchesTable.setDeleted(delete);

        return batchesTable;
    }

    public BatchesTable selectGLAccountAtBatchesTbl(BatchesTable actBatchesTable) throws Exception {
        String glAccount =generalLedgerCodeDao.getGlCd().getDescr();
        selectItem(deposits.batchesTblGLAccountColSel(), glAccount);
        actBatchesTable.setGlAccount(glAccount);
        return actBatchesTable;
    }

    public void updateDepBatch(DepBatch depBatch, int depBatchTypId) throws Exception {
        depBatch.setDepBatchTypId(depBatchTypId);
        depBatch.setModified(true);
        depBatch.setResultCode(ErrorCodeMap.RECORD_FOUND);
        paymentDao.setDepBatch(depBatch);
    }

    public void updateTheDepositAmountInputAtHeader(Header header) throws Exception{
        float totalBatchAmt = Float.parseFloat(deposits.batchesTblTotalBatchAmtColTxt().getText().replace(",", EMPTY_STRING));
        enterValues(deposits.headerDepositAmtInput(), totalBatchAmt);
        header.setDepositAmount(totalBatchAmt);
    }

    public Header updateValueAtHeader(Header actHeader) throws Exception {
        Header header = new Header();
        String depositDate = timeStamp.convertDateToString(actHeader.getDepositDate(), DATE_FORMATTED_DEFAULT);
        float depositAmount = actHeader.getDepositAmount();
        String bank = bankDao.getBank().getAbbrv();
        setValuesAtHeader(depositDate, depositAmount,bank);

        header.setAccountingDate(actHeader.getAccountingDate());
        header.setUser(actHeader.getUser());
        header.setDepositId(actHeader.getDepositId());
        header.setDepositDate(actHeader.getDepositDate());
        header.setDepositAmount(depositAmount);
        header.setBank(bank);
        return header;
    }

    public BatchesTable updateValuesAtBatchesTable(BatchesTable actBatchesTable, Header actHeaderUpdated, String row) throws Exception {
        clickHiddenPageObject(deposits.batchesTblBatchIdColTxt(row), 0);

        BatchesTable batchesTable = setBatchesValue(actHeaderUpdated.getDepositAmount());

        batchesTable.setLastActivity(actBatchesTable.getLastActivity());
        batchesTable.setStatus(actBatchesTable.getStatus());

        return batchesTable;
    }

    public void updateDepBatchByStaId(DepBatch depBatch, int staId) throws Exception {
        depBatch.setStaId(staId);
        depBatch.setModified(true) ;
        depBatch.setResultCode(ErrorCodeMap.RECORD_FOUND);
        paymentDao.setDepBatch(depBatch);
    }

    public void selectBank(String bank) throws Exception{
        selectItem(deposits.headerBankSel(), bank);
    }

    public void verifyBankReconciliationSectionIsEmpty() {
        int totalFields = Integer.parseInt(getTotalResultSearch(deposits.bnkReconciliationTblTotalRecordsTxt()));
        assertEquals(0, totalFields, "        Bank Reconciliation  section is empty");
    }

    public void verifyBankReconciliationSectionInputIsDisplayedCorrectly(BankReconciliation actBankReconciliation) throws Exception {
        BankReconciliation expBankReconciliation = getValuesInputInBankReconciliationSection();
        assertEquals(actBankReconciliation.toString(), expBankReconciliation.toString(), "        Bank fields in the bank Reconciliation section is displayed correctly");
    }

    private void verifyBatchesSectionIsEmpty() {
        int totalResult = Integer.parseInt(getTotalResultSearch(deposits.batchesTblTotalRecordTxt()));
        assertEquals(0, totalResult, "        Batches section is empty");
    }

    public void verifyBatchesSectionIsDisplayedCorrectly(BatchesTable actBatchesTable, String row) throws Exception {
        BatchesTable expBatchesTable = getBatchesValue(row);
        actBatchesTable.setLastActivity(ConvertUtil.convertStringToSQLDate(timeStamp.getCurrentDate(DATE_FORMATTED_DEFAULT), DATE_FORMATTED_DEFAULT));
        actBatchesTable.setStatus(depDao.getDepStaTypByStaId(2).getDescr());

        assertEquals(actBatchesTable.toString(), expBatchesTable.toString(), "        Batches Table section is displayed correctly");
    }

    public void verifyBatchesDataAddedCorrectlyAfterReload(int rownum, List<BatchesTable> actBatchesTables) throws Exception {
        for (int i = 0; i < rownum; i++) {
            verifyBatchesSectionIsDisplayedCorrectly(actBatchesTables.get(i), String.valueOf(i+2));
        }
    }

    public void verifyBatchesDataAddedCorrectly(int rownum, List<BatchesTable> actBatchesTables) throws Exception {
        for (int i = 0; i < rownum; i++) {
            if (i == rownum - 1) {
                assertEquals(actBatchesTables.get(i), getBatchesValueByInput("last()"), "        Added data is displayed correctly");
            } else {
                assertEquals(actBatchesTables.get(i), getBatchesValue(String.valueOf(i + 2)), "        Added data is displayed correctly");
            }
        }
    }

    public void verifyDataAtHeaderAreSavedInDepTbl(Header expHeader,String userName) throws Exception{
        Header actHeader = mapToHeader(expHeader.getDepositId());
        expHeader.setUser(userName);
        expHeader.setAccountingDate(ConvertUtil.convertStringToSQLDate(determineDepAccountingDate(),DATE_FORMATTED_DEFAULT));
        assertEquals(actHeader.toString(), expHeader.toString(), "        Data at Header are Saved correctly In Dep Tbl.");
    }

    public String determineDepAccountingDate() throws Exception {
        String systemDate = systemDao.getSystemSetting(SystemSettingMap.SS_SYSTEM_OVERRIDE_DATE).getDataValue();
        return systemDate != null ? systemDate : timeStamp.getCurrentDate(DATE_FORMATTED_DEFAULT);
    }

    public void verifyDataAtBatchesTblIsSavedInDepBatchTbl(BatchesTable expBatchesTable, int depID, int batchID) throws Exception{
        Date currentDate = ConvertUtil.convertStringToSQLDate(timeStamp.getCurrentDate(DATE_FORMATTED_DEFAULT), DATE_FORMATTED_DEFAULT);
        BatchesTable actBatchesTable = mapToBatches(depID, batchID);
        expBatchesTable.setLastActivity(currentDate);

        assertEquals(actBatchesTable.toString(), expBatchesTable.toString(),"        Data at Batches are Saved correctly In Dep_Batch Tbl.");
    }

    public void verifyDataAtBankReconciliationSectionIsSavedInDepBankTransTbl(BankReconciliation expBankReconciliation, int depID) throws Exception{
        BankReconciliation actBankReconciliation = mapToBankReconciliation(depID);
        assertEquals(actBankReconciliation.toString(), expBankReconciliation.toString(),"        Data at Bank Reconciliation Section are Saved correctly In Dep Bank Trans Tbl.");
    }

    public void verifyDataAtDepositNotesSectionIsSavedInDepTbl(int depID, String expDepositNotes) throws Exception{
        Dep dep = paymentDao.getDepByDepId(depID);
        assertEquals(dep.getNote(), expDepositNotes,"        Data at Deposit Notes Section are Saved correctly In Dep Tbl.");
    }

    public void verifyDeleteDepositCheckboxIsChecked(){
        assertTrue(deposits.headerDeleteDepositChk().isSelected(),"        Payment Deposits load page is checked.");
    }

    public void verifyDepositsDetailScreenIsEmpty() throws Exception {
        verifyHeaderIsDisplayedCorrectly(new Header(), ADD_NEW_DEPOSITS);
        verifyBankReconciliationSectionIsEmpty();
        verifyBatchesSectionIsEmpty();
        verifyDepositNotesSecTionIsDisplayedCorrectly(EMPTY_STRING);
    }

    public void verifyDepositNotesSecTionIsDisplayedCorrectly(String value) {
        assertEquals(value, deposits.depositNotesSectionNoteInput().getAttribute("value"), "        Bank Reconciliation section is empty");
    }

    public void verifyDepositAmountIsDisplayedCorrectly(float actDepositAmount) throws Exception {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
        String depositAmt = deposits.headerDepositAmtInput().getAttribute("value").trim();
        float expDepositAmount = decimalFormat.parse(depositAmt).floatValue();
        assertEquals(actDepositAmount, expDepositAmount, "        Data is displayed correctly.");
    }

    public void verifyDepBatchRemoveInDB(Header header, List<BatchesTable> actBatchesTables) {
        DepBatch depBatch = null;
        try {
            depBatch = paymentDao.getDepBatchByDepIdAndBatchId(header.getDepositId(), actBatchesTables.get(0).getBatchId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(depBatch, "        Dep_Batch record has removed in database");
    }

    public void verifyErrorMessageInvalidDepositID(Header header){
        String actWarningMessage = PRE_ERROR_MESSAGE_TEXT+header.getDepositId()+SUFFIX_ERROR_MESSAGE_TEXT;
        String expWarningMessage = deposits.loadDepositsPageErrorMessageTxt().getText();

        assertEquals(actWarningMessage,expWarningMessage,"        The warning message is displayed with content: '"+PRE_ERROR_MESSAGE_TEXT+header.getDepositId()+SUFFIX_ERROR_MESSAGE_TEXT);
    }

    public void verifyErrorIsDisplayedCorrectly(String message) {
        String expMessage = deposits.errorSectionMessageTxt().getText();
        assertEquals(message, expMessage, "        The Error Retuns section, the warning message is displayed correctly.");
    }

    public void verifyHeaderIsDisplayedCorrectly(Header actHeader, String typ) throws Exception {
        Header expHeader = getHeaderValue(typ);
        assertEquals(actHeader.toString(), expHeader.toString(), "        Header Section Is Displayed Correctly");
    }

    public void verifyNewRowInBatchesTblIsAddedCorrectly(BatchesTable actBatchesTable, String row) throws Exception {
        BatchesTable expBatchesTable = getBatchesValueByInput(row);
        assertEquals(actBatchesTable.toString(), expBatchesTable.toString(), "        The new row is added in Batches tbl with correct data.");
    }

    public void verifyNoRecordExistsInDepDepBatchDepBankTransTbl(int depId){
        Dep dep;
        DepBatch depBatch;
        DepBankTrans depBankTrans;
        boolean flag =  false;
        try {
            dep = paymentDao.getDepByDepId(depId);
        } catch (Exception e) {
            dep = null;
        }

        try {
            depBatch = rpmDao.getDepBatchByDepId(testDb, depId).get(0);
        } catch (Exception e) {
            depBatch = null;
        }
        try {
            depBankTrans = bankDao.getDepBankTransByDepId(depId);
        } catch (Exception e) {
            depBankTrans = null;
        }
        if ((dep == null) || (depBatch == null) || (depBankTrans == null)) {
            flag = true;
        }
        assertTrue(flag,"        Data Is Removed In Dep, Dep_Batch, Dep_Bank_Trans tbls.");
    }

    public void verifyPaymentDepositsLoadPageIsDisplayed() throws Exception {
        assertEquals(deposits.paymentDepositsPageTitleTxt().getText(), PAYMENT_DEPOSITS_TITLE, "        Deposits load page is displayed");
        assertFalse(isElementHidden(deposits.depositsSection(), 5), "        Deposits section at load page is displayed");
    }

    public void verifyPaymentDepositsDetailPageIsDisplayed() throws Exception {
        assertEquals(deposits.paymentDepositsPageTitleTxt().getText(), PAYMENT_DEPOSITS_TITLE, "        Deposits load page is displayed");
        assertFalse(isElementHidden(deposits.headerDepositIdTxt(), 5), "        Deposits section at load page is displayed");
    }

    public void verifyRowMarkedAsDeleted(int row) throws Exception {
        assertTrue(isElementPresent(deposits.batchesTblRowNum(row), 5), "        Batch Delete checkbox is present");
        assertTrue(isElemAttrMatch(deposits.batchesTblRowNum(row), "class", "rowMarkedForDelete", 5), "         Batch row is marked as deleted");
    }

    public void verifyStatusColInBatchesTblIsDisplayedCorrectly(String row, String expStatus) throws Exception {
        String actStatus = deposits.batchesTblStatusColTxt(row).getText();
        assertEquals(actStatus, expStatus, "        The Deposits Detail screen is displayed correctly with Status in Batches tbl = "+ expStatus +".");
    }


    public void verifyStatusOfPostNonARAmtColAndDeleteColInBatchesTblAreEnable(String row, boolean postStatus, boolean deleteStatus) throws Exception {
        assertTrue(isElementEnabled(deposits.batchesTblPostNonARAmtColChk(row), 5, postStatus), "        [Post Non-AR Amt] field in Batches table is enabled.");
        assertTrue(isElementEnabled(deposits.batchesTblDeleteColChk(row), 5, deleteStatus), "        [Delete] field in Batches table is enabled.");
    }

    public void verifyTheFirstRowInDepBatchTblIsUpdatedCorrectly(int depositId, int expDepBatchTypId, int expStaId) throws Exception {
        List<DepBatch> depBatchs = rpmDao.getDepBatchByDepId(testDb, depositId);
        DepBatch depBatch = depBatchs.get(0);
        if(expDepBatchTypId>0){
            assertEquals(depBatch.getDepBatchTypId(), expDepBatchTypId, "        The DEP_BATCH_TYP_ID field of the old row in Dep_Batch is updated correctly.");
        }
        assertEquals(expStaId, depBatch.getStaId(), "        The STA_ID field of the old row in Dep_Batch is updated correctly.");
    }

    public void verifyTotalDisplayedCorrectlyAfterDeleteRow(BatchesTable deletedBatchesRow, BatchesTable rowTotal) throws ParseException {
        BatchesTable currentRowTotal = getRowTotalValue();

        rowTotal.setBatchAmount(rowTotal.getBatchAmount() - deletedBatchesRow.getBatchAmount());
        rowTotal.setAppliedAmount(rowTotal.getAppliedAmount() - deletedBatchesRow.getAppliedAmount());
        rowTotal.setUnappliedAmount(rowTotal.getUnappliedAmount() - deletedBatchesRow.getUnappliedAmount());
        rowTotal.setPostedAmount(rowTotal.getPostedAmount() - deletedBatchesRow.getPostedAmount());
        rowTotal.setNonARAmount(rowTotal.getNonARAmount() - deletedBatchesRow.getNonARAmount());

        assertEquals(currentRowTotal.toString(), rowTotal.toString(), "        Row total has updated correctly");
    }

    public BatchesTable verifyValueStatusAtBatchesSectionIsDisplayedUnAssigned(BatchesTable actBatchesTable, String row) throws Exception {
        BatchesTable expBatchesTable = getBatchesValue(row);
        actBatchesTable.setLastActivity(ConvertUtil.convertStringToSQLDate(timeStamp.getCurrentDate(DATE_FORMATTED_DEFAULT), DATE_FORMATTED_DEFAULT));
        actBatchesTable.setStatus(depDao.getDepStaTypByStaId(1).getDescr().trim());
        assertEquals(actBatchesTable.toString(), expBatchesTable.toString(), "        Batches Table section is displayed correctly");

        return actBatchesTable;
    }

    public void verifyWarningPopupIsDisplayedWithCorrectContent(){
        String warningPopupTile = deposits.warningPopupTitleTxt().getText();
        assertEquals(WARNING_TITLE, warningPopupTile,"                Warning popup is displayed with correct title.");

        String warningMessage = deposits.warningPopupMessageTxt().getText();
        assertEquals(warningMessage, WARNING_MSG,"                Warning popup is displayed with correct content.");
    }

    public void verifyWarningPopupIsDisplayedCorrectly(String message){
        String expMessage = deposits.warningPopupMessageTxt().getText();
        assertEquals(message, expMessage, "        The warning popup, message is displayed correctly.");
    }
    public void setReconciledAmt(String amount) throws InterruptedException {
        deposits.reconciledAmtInputTxt().click();
        enterValues(deposits.reconciledAmtInput(), amount);
    }
}