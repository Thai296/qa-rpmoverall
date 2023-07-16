package com.pfEngines.tests;

import com.mbasys.common.utility.Money;
import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnAdj.AccnAdj;
import com.mbasys.mars.ejb.entity.accnPmt.AccnPmt;
import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.accnProcErr.AccnProcErr;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.bank.Bank;
import com.mbasys.mars.ejb.entity.bankAcct.BankAcct;
import com.mbasys.mars.ejb.entity.dep.Dep;
import com.mbasys.mars.ejb.entity.depBatch.DepBatch;
import com.mbasys.mars.ejb.entity.depBatchSeq.DepBatchSeq;
import com.mbasys.mars.ejb.entity.eobClaim.EobClaim;
import com.mbasys.mars.ejb.entity.eobFinancialTrans.EobFinancialTrans;
import com.mbasys.mars.ejb.entity.eobPhi.EobPhi;
import com.mbasys.mars.ejb.entity.eobPyr.EobPyr;
import com.mbasys.mars.ejb.entity.eobPyrXref.EobPyrXref;
import com.mbasys.mars.ejb.entity.eobSvcAdj.EobSvcAdj;
import com.mbasys.mars.ejb.entity.eobSvcPmt.EobSvcPmt;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.qValidateAccn.QValidateAccn;
import com.mbasys.mars.ejb.entity.systemSetting.SystemSetting;
import com.mbasys.mars.errorProcessing.EpConstants;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.DbErrorMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.PmtMap;
import com.mbasys.mars.persistance.PyrGrpMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.mbasys.mars.reconciliation.ReconciliationStage;
import com.mbasys.mars.validation.ValidateQueueMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.menu.MenuNavigation;
import com.pfEngines.tests.EligibilityBaseTest.TestParameters;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ReconciliationEngineTest extends SeleniumBaseTest
{
    private static final String USER_ID_RECONCILIATION_ENGINE = "ReconciliationEngine";
    private static final SimpleDateFormat EFT_DATE_FMT = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    private static final int ADJ_CD_CONALL = 2;
    private static final int ADJ_CD_SEQUEST = 5;
    private static final int DEP_BATCH_STA_TYP_WIP = 3;
    private static final int DEP_BATCH_TYP_PAYMENTS = 1;
    private static final String EOB_CODE_ADJ_GRP_PATIENT = "PR";
    private static final String EOB_ADJ_REASON_CD_DED = "1";
    private static final String EOB_ADJ_REASON_CD_COINS = "2";
    private static final String EOB_ADJ_REASON_CD_COPAY = "3";

    protected static long QUEUE_POLL_TIME = TimeUnit.SECONDS.toMillis(5);
    protected static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(10);

    private AccessionDetail accessionDetail;
    private MenuNavigation navigation;
    protected TestParameters parameters;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins", "defaultBankId"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword,
                            @Optional String disableBrowserPlugins, @Optional String defaultBankId)
    {
        try
        {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            logIntoSso(ssoUsername, ssoPassword);
            logger.info("Update SS 5012 Default Bank Id");
            updateSystemSetting(SystemSettingMap.SS_DEFAULT_BANK_ID, "True", defaultBankId);
            clearDataCache();
        }
        catch (Exception e)
        {
            Assert.fail("Error running BeforeSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins", "accnId", "internalCtrlId"})
    public void beforeTest(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword,
                           String disableBrowserPlugins, @Optional String accnId, @Optional String internalCtrlId)
    {
        try
        {
            logger.info("Running BeforeMethod");
           // super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            cleanUpClaim(accnId, internalCtrlId);
            logIntoSso(ssoUsername, ssoPassword);
        }
        catch (Exception e)
        {
            Assert.fail("Error running BeforeMethod", e);
        }
    }

    @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword,
                           @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running AfterSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            logIntoSso(ssoUsername, ssoPassword);
            logger.info("Update SS 5012 Default Bank Id");
            updateSystemSetting(SystemSettingMap.SS_DEFAULT_BANK_ID, "True", "1");
            clearDataCache();
        }
        catch (Exception e)
        {
            Assert.fail("Error running AfterSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @Test(priority = 1, description = "Process record with eob_claim.fk_reconcile_stage_id =10, error_note='Unexpected error:  Failed to shuffle payors due to: failed to validate'")
    @Parameters({"accnId", "internalCtrlId"})
    public void testPFER_567(String accnId, String internalCtrlId) throws Exception
    {
        logger.info("Running test PFER-567");
        logger.info("Make sure claim is ready to be processed by Reconciliation Engine");

        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(), 1);
        EobClaim eobClaim = eobClaims.get(0);

        logger.info("Reset Eob Stage for a claim");
        resetEobStageForClaim(accnId, internalCtrlId);

        logger.info("Wait for Reconciliation Engine to process the claim");
        int reconcStageId = waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(reconcStageId > 0);

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_claim.reconcile_stage_id = 999");
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        eobClaim = eobClaims.get(0);
        logger.info("Verify Reconciliation Engine processed the claim - checking eob_financial_trans.reconcile_stage_id = 999");
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        Assert.assertEquals(eobClaim.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify Reconciliation Engine processed the claim - records in dep, dep_batch, dep_batch_seq are created");
        Dep dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());
        //Assert.assertTrue(dep.getEntries()==1);

        logger.info("Verify Reconciliation Engine processed the claim - records in accn_pmt, accn_adj are created");
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtByDepId(testDb, dep.getDepId());
        Assert.assertTrue(accnPmts.size() > 0);
        List<AccnAdj> accnAdjs = rpmDao.getAccnAdjByDepId(testDb, dep.getDepId());
        Assert.assertTrue(accnAdjs.size() > 0);
    }

    @Test(priority = 1, description = "Don't change SubId to corrected on accn if pyr is non-medicare pyr'")
    @Parameters({"accnId", "pyrAbbrv", "oldSubId", "newSubId", "internalCtrlId"})
    public void testPFER_568(String accnId, String pyrAbbrv, String oldSubId, String newSubId, String internalCtrlId) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running test PFER-568");
        logger.info("Make sure claim is ready to be processed by Reconciliation Engine");

        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(), 1);
        EobClaim eobClaim = eobClaims.get(0);
        navigation.navigateToAccnDetailPage();
        logger.info("Load accn on Accn Detail jsp");
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());

        logger.info("Make sure that SubID is oldSubId " + oldSubId);
        if (!accessionDetail.primaryPyrSubsIdInput().getAttribute("value").equalsIgnoreCase(oldSubId))
        {
            setOldSubIdToAccn(accessionDetail, accnId, oldSubId);
        }

        Assert.assertEquals(oldSubId, accessionDetail.primaryPyrSubsIdInput().getAttribute("value"));
        accessionDetail.saveAndClear(wait);
        logger.info("Make sure the eob_phi.corrected_external_subs_id = newSubId");
        EobPhi eobPhi = rpmDao.getEobPhi(testDb, eobClaim.getSeqId());
        Assert.assertEquals(eobPhi.getCorrectedExternalSubsId(), newSubId);

        logger.info("Make sure the pyr on the accn is not medicare pyr");
        int pyrGroup = pyr.getPyrGrpId();
        Assert.assertNotEquals(pyrGroup, PyrGrpMap.MCARE_PYR_GRP);

        logger.info("Reset Eob Stage for a claim ="+eobClaim);
        resetEobStageForClaim(accnId, internalCtrlId);
        logger.info("Wait for Reconciliation Engine to process the claim");
        int reconcStageId = waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(reconcStageId > 0);

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_claim.reconcile_stage_id = 999");
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        eobClaim = eobClaims.get(0);
        logger.info("Verify Reconciliation Engine processed the claim - checking eob_financial_trans.reconcile_stage_id = 999");
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        Assert.assertEquals(eobClaim.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify Reconciliation Engine did not update accn_pyr.subs_id");
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.get(0).getSubsId(), oldSubId);

        logger.info("Verify Reconciliation Engine processed the claim - records in dep, dep_batch, dep_batch_seq are created");
        Dep dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());

        logger.info("Verify Reconciliation Engine processed the claim - records in accn_pmt, accn_adj are created");
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtByDepId(testDb, dep.getDepId());

        Assert.assertTrue(accnPmts.size() > 0);
        List<AccnAdj> accnAdjs = rpmDao.getAccnAdjByDepId(testDb, dep.getDepId());
        Assert.assertTrue(accnAdjs.size() > 0);
    }

    @Test(priority = 1, description = "Change SubId to corrected on accn if pyr is medicare pyr'")
    @Parameters({"accnId", "pyrAbbrv", "oldSubId", "newSubId", "internalCtrlId"})
    public void testPFER_569(String accnId, String pyrAbbrv, String oldSubId, String newSubId, String internalCtrlId) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        navigation = new MenuNavigation(driver, config);

        logger.info("Running test PFER-569");
        logger.info("Make sure claim is ready to be processed by Reconciliation Engine");
        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(), 1);
        EobClaim eobClaim = eobClaims.get(0);

        logger.info("Load accn on Accn Detail jsp");
        navigation.navigateToAccnDetailPage();
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());

        logger.info("Make sure that SubID is oldSubId " + oldSubId);
        if (!StringUtils.equals(accessionDetail.primaryPyrSubsIdInput().getAttribute("value"), oldSubId))
        {
            setOldSubIdToAccn(accessionDetail, accnId, oldSubId);
        }

        Assert.assertEquals(oldSubId, accessionDetail.primaryPyrSubsIdInput().getAttribute("value"));
        accessionDetail.saveAndClear(wait);

        logger.info("Make sure the eob_phi.corrected_external_subs_id = " + newSubId);
        EobPhi eobPhi = rpmDao.getEobPhi(testDb, eobClaim.getSeqId());
        Assert.assertEquals(eobPhi.getCorrectedExternalSubsId(), newSubId);

        logger.info("Make sure the pyr on the accn is  medicare pyr");
        int pyrGroup = pyr.getPyrGrpId();
        Assert.assertEquals(pyrGroup, PyrGrpMap.MCARE_PYR_GRP);

        logger.info("Reset Eob Stage for a claim ="+eobClaim);
        resetEobStageForClaim(accnId, internalCtrlId);

        logger.info("Wait for Reconciliation Engine to process the claim");
        int reconcStageId = waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(reconcStageId > 0);

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_claim.reconcile_stage_id = 999");
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        eobClaim = eobClaims.get(0);
        logger.info("Verify Reconciliation Engine processed the claim - checking eob_financial_trans.reconcile_stage_id = 999");
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        Assert.assertEquals(eobClaim.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify Reconciliation Engine did not update accn_pyr.subs_id");
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.get(0).getSubsId(), newSubId);

        logger.info("Verify Reconciliation Engine processed the claim - records in dep, dep_batch, dep_batch_seq are created");
        Dep dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());
        //Assert.assertTrue(dep.getEntries()==1);

        logger.info("Verify Reconciliation Engine processed the claim - records in accn_pmt, accn_adj are created");
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtByDepId(testDb, dep.getDepId());
        Assert.assertTrue(accnPmts.size() > 0);
        List<AccnAdj> accnAdjs = rpmDao.getAccnAdjByDepId(testDb, dep.getDepId());
        Assert.assertTrue(accnAdjs.size() > 0);
    }

    @Test(priority = 1, description = "Reconc eng considers for claim adjustments calculating paid amounts for Dep_bat pyr is medicare pyr")
    @Parameters({"accnId", "pyrAbbrv", "totalPaid", "internalCtrlId"})
    public void testPFER_570(String accnId, String pyrAbbrv, String totalPaid, String internalCtrlId) throws Exception
    {
        logger.info("Running test PFER-570");
        logger.info("Make sure claim is ready to be processed by Reconciliation Engine");
        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(), 1);
        EobClaim eobClaim = eobClaims.get(0);

        logger.info("Reset Eob Stage for a claim ="+eobClaim);
        resetEobStageForClaim(accnId, internalCtrlId);
        // Assert.assertTrue(eobClaim.getReconcileStageId() == 0);

        logger.info("Wait for Reconciliation Engine to process the claim");
        int reconcStageId = waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(reconcStageId > 0);

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_claim.reconcile_stage_id = 999");
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        eobClaim = eobClaims.get(0);
        logger.info("Verify Reconciliation Engine processed the claim - checking eob_financial_trans.reconcile_stage_id = 999");
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        Assert.assertEquals(eobClaim.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify discrepancy is created - eob_claim.b_discrepancy = 1");
        Assert.assertTrue(eobClaim.getIsDiscrepancy());

        logger.info("Verify total paid amount in eob_claim = CLP04 - eob_claim.tot_paid_str = "+totalPaid);
        Assert.assertEquals(eobClaim.getTotPaidAmtStr(), totalPaid);

        logger.info("Verify Reconciliation Engine processed the claim - records in dep, dep_batch, dep_batch_seq are created");
        Dep dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());

        logger.info("Verify Accn_pmt associated with dep");
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtByDepId(testDb, dep.getDepId());
        Assert.assertTrue(accnPmts.isEmpty());

        logger.info("Verify Accn_adj associated with dep");
        List<AccnAdj> accnAdjs = rpmDao.getAccnAdjByDepId(testDb, dep.getDepId());
        Assert.assertTrue(accnAdjs.isEmpty());
    }

    @Test(priority = 1, description = "Reconc eng assigned The fk_bank_id from BANK_ACCT is assigned to DEP.FK_BANK_ID when matches 835 BPR13&15")
    @Parameters({"accnId", "pyrAbbrv", "totalPaid", "providersBankId", "providersBankAcctNum", "bankId", "internalCtrlId"})
    public void testPFER_597(String accnId, String pyrAbbrv, String totalPaid, String providersBankId, String providersBankAcctNum, String bankId, String internalCtrlId) throws Exception
    {
        logger.info("Running test PFER-597");
        logger.info("Make sure eob_financial_trans record has providers_bank_id and providers_bamk_acct_id saved");
        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(), 1);
        EobClaim eobClaim = eobClaims.get(0);
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getProvidersBankId(), providersBankId);
        Assert.assertEquals(eobFinancialTrans.getProvidersBankAcctId(), providersBankAcctNum);

        logger.info("Make sure bank_acct record exist and matching providersBankId and providersBankAcctNum from 835 file");
        List <BankAcct> bankAcct = rpmDao.getBankAcctByBankCodeAndAccount(testDb, Integer.parseInt(providersBankId), Integer.parseInt(providersBankAcctNum));
        Assert.assertTrue(bankAcct.size()>0);
        Assert.assertEquals(bankAcct.get(0).bankId, Integer.parseInt(bankId));

        logger.info("Make sure claim is ready to be processed by Reconciliation Engine");
        logger.info("Reset Eob Stage for a claim ="+eobClaim);
        resetEobStageForClaim(accnId, internalCtrlId);

        logger.info("Wait for Reconciliation Engine to process the claim");
        int reconcStageId = waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(reconcStageId > 0);

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_claim.reconcile_stage_id = 999");
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        eobClaim = eobClaims.get(0);
        logger.info("Verify Reconciliation Engine processed the claim - checking eob_financial_trans.reconcile_stage_id = 999");
        eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        Assert.assertEquals(eobClaim.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify eob_financial_trans.PROVIDERS_BANK_ID and eob_financial_trans.PROVIDERS_BANK_ACCT_ID saved as providersBankId and providersBankAcctNum from 835 file");
        Assert.assertEquals(eobFinancialTrans.getProvidersBankId(), providersBankId);
        Assert.assertEquals(eobFinancialTrans.getProvidersBankAcctId(), providersBankAcctNum);

        logger.info("Verify no discrepancy is created - eob_claim.b_discrepancy = 0");
        Assert.assertFalse(eobClaim.getIsDiscrepancy());

        logger.info("Verify total paid amount in eob_claim = CLP04 - eob_claim.tot_paid_str = "+totalPaid);
        Assert.assertEquals(eobClaim.getTotPaidAmtStr(), totalPaid);

        logger.info("Verify Reconciliation Engine processed the claim - records in dep, dep_batch, dep_batch_seq are created");
        Dep dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());
        logger.info("Verify dep.FK_BANK_ID = "+bankId);
        Assert.assertEquals(dep.getBankId(), Integer.parseInt(bankId));

        logger.info("Verify Accn_pmt associated with dep");
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtByDepId(testDb, dep.getDepId());
        Assert.assertFalse(accnPmts.isEmpty());

        logger.info("Verify Accn_adj associated with dep");
        List<AccnAdj> accnAdjs = rpmDao.getAccnAdjByDepId(testDb, dep.getDepId());
        Assert.assertFalse(accnAdjs.isEmpty());
    }

    @Test(priority = 1, description = "Reconc eng assigned DEP.BANK_ID based on the default payor's FK_BANK_ID from EOB_PYR_XREF for the EOB_EXTERNAL_PAYOR_ID from the deposit")
    @Parameters({"accnId", "pyrAbbrv", "totalPaid", "providersBankId", "providersBankAcctNum", "bankId", "internalCtrlId"})
    public void testPFER_599(String accnId, String pyrAbbrv, String totalPaid, String providersBankId, String providersBankAcctNum, String bankId, String internalCtrlId) throws Exception
    {
        logger.info("Running test PFER-599");
        logger.info("Make sure eob_financial_trans record has providers_bank_id and providers_bamk_acct_id saved");
        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(), 1);
        EobClaim eobClaim = eobClaims.get(0);
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getProvidersBankId(), providersBankId);
        Assert.assertEquals(eobFinancialTrans.getProvidersBankAcctId(), providersBankAcctNum);

        logger.info("Make sure there is a eob_pyr_xref record saved");
        EobPyr eobPyr = rpmDao.getEobPyrByEobFinanTransId(testDb, eobFinancialTrans.getSeqId());
        EobPyrXref eobPyrXref = rpmDao.getDefaultEobPyrXrefByEobExternalPyrId(testDb, eobPyr.getExternalIdCd());
        Assert.assertEquals(eobPyrXref.getBankId(), Integer.parseInt(bankId));

        logger.info("Make sure bank_acct record does not exist for providersBankId and providersBankAcctNum from 835 file");
        List <BankAcct> bankAcct = rpmDao.getBankAcctByBankCodeAndAccount(testDb, Integer.parseInt(providersBankId), Integer.parseInt(providersBankAcctNum));
        Assert.assertEquals(bankAcct.size(), 0);

        logger.info("Reset Eob Stage for a claim ="+eobClaim);
        resetEobStageForClaim(accnId, internalCtrlId);

        logger.info("Make sure claim is ready to be processed by Reconciliation Engine");

        logger.info("Wait for Reconciliation Engine to process the claim");
        int reconcStageId = waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(reconcStageId > 0);

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_financial_trans.reconcile_stage_id = 999");
        eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify eob_financial_trans.PROVIDERS_BANK_ID and eob_financial_trans.PROVIDERS_BANK_ACCT_ID saved as providersBankId and providersBankAcctNum from 835 file");
        Assert.assertEquals(eobFinancialTrans.getProvidersBankId(), providersBankId);
        Assert.assertEquals(eobFinancialTrans.getProvidersBankAcctId(), providersBankAcctNum);

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_claim.reconcile_stage_id = 999");
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        eobClaim = eobClaims.get(0);
        Assert.assertEquals(eobClaim.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify no discrepancy is created - eob_claim.b_discrepancy = 0");
        Assert.assertFalse(eobClaim.getIsDiscrepancy());

        logger.info("Verify total paid amount in eob_claim = CLP04 - eob_claim.tot_paid_str = "+totalPaid);
        Assert.assertEquals(eobClaim.getTotPaidAmtStr(), totalPaid);

        logger.info("Verify Reconciliation Engine processed the claim - records in dep, dep_batch, dep_batch_seq are created");
        Dep dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());

        logger.info("Verify Reconciliation Engine assigned DEP.BANK_ID based on the default payor's FK_BANK_ID from EOB_PYR_XREF"+bankId);
        Assert.assertEquals(dep.getBankId(), eobPyrXref.getBankId());

        logger.info("Verify Accn_pmt associated with dep");
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtByDepId(testDb, dep.getDepId());
        Assert.assertFalse(accnPmts.isEmpty());

        logger.info("Verify Accn_adj associated with dep");
        List<AccnAdj> accnAdjs = rpmDao.getAccnAdjByDepId(testDb, dep.getDepId());
        Assert.assertFalse(accnAdjs.isEmpty());
    }

    @Test(priority = 1, description = "Reconc eng assigned DEP.BANK_ID based on the default payor's FK_BANK_ID from SS 1502")
    @Parameters({"accnId", "pyrAbbrv", "totalPaid", "providersBankId", "providersBankAcctNum", "defaultBankId", "internalCtrlId"})
    public void testPFER_600(String accnId, String pyrAbbrv, String totalPaid, String providersBankId, String providersBankAcctNum, String defaultBankId, String internalCtrlId) throws Exception
    {
        logger.info("Running test PFER-600");
        logger.info("Make sure eob_financial_trans record has providers_bank_id and providers_bamk_acct_id saved");
        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(),1);
        EobClaim eobClaim = eobClaims.get(0);
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getProvidersBankId(), providersBankId);
        Assert.assertEquals(eobFinancialTrans.getProvidersBankAcctId(), providersBankAcctNum);

        logger.info("Make sure there is a eob_pyr_xref record saved");
        EobPyr eobPyr = rpmDao.getEobPyrByEobFinanTransId(testDb, eobFinancialTrans.getSeqId());
        EobPyrXref eobPyrXref = rpmDao.getDefaultEobPyrXrefByEobExternalPyrId(testDb, eobPyr.getExternalIdCd());
        int bankId = eobPyrXref.getBankId();
        Assert.assertEquals(bankId, 0);

        logger.info("Make sure bank_acct record does not exist for providersBankId and providersBankAcctNum from 835 file");
        List <BankAcct> bankAcct = rpmDao.getBankAcctByBankCodeAndAccount(testDb, Integer.parseInt(providersBankId), Integer.parseInt(providersBankAcctNum));
        Assert.assertEquals(bankAcct.size(), 0);

        logger.info("Make sure SS 1502 is saved");
        SystemSetting systemSetting =systemDao.getSystemSetting(SystemSettingMap.SS_DEFAULT_BANK_ID);
        int systemSettingBankId = Integer.parseInt(systemSetting.getDataValue());

        logger.info("Reset Eob Stage for a claim ="+eobClaim);
        resetEobStageForClaim(accnId, internalCtrlId);

        logger.info("Wait for Reconciliation Engine to process the claim");
        int reconcStageId = waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(reconcStageId > 0);

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_financial_trans.reconcile_stage_id = 999");
        eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_claim.reconcile_stage_id = 999");
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        eobClaim = eobClaims.get(0);
        Assert.assertEquals(eobClaim.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify no discrepancy is created - eob_claim.b_discrepancy = 0");
        Assert.assertFalse(eobClaim.getIsDiscrepancy());

        logger.info("Verify total paid amount in eob_claim = CLP04 - eob_claim.tot_paid_str = "+totalPaid);
        Assert.assertEquals(eobClaim.getTotPaidAmtStr(), totalPaid);

        logger.info("Verify Reconciliation Engine processed the claim - records in dep, dep_batch, dep_batch_seq are created");
        Dep dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());

        logger.info("Verify Reconciliation Engine DEP bankId is pulled from System Setting 5012 "+systemSettingBankId);
        Assert.assertNotEquals(dep.getBankId(), eobPyrXref.getBankId());
        Assert.assertEquals(dep.getBankId(), systemSettingBankId);

        logger.info("Verify Accn_pmt associated with dep");
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtByDepId(testDb, dep.getDepId());
        Assert.assertFalse(accnPmts.isEmpty());

        logger.info("Verify Accn_adj associated with dep");
        List<AccnAdj> accnAdjs = rpmDao.getAccnAdjByDepId(testDb, dep.getDepId());
        Assert.assertFalse(accnAdjs.isEmpty());
    }

    @Test(priority = 1, description = "Tax Proc Allowed Amount = $0 Is Applied Correctly")
    @Parameters({"accnId", "internalCtrlId"})
    public void testRPM108294(String accnId, String internalCtrlId) throws Exception
    {
        logger.info("Running test RPM108294");
        logger.info("Verifying tax procs on accession, accnId="+accnId);
        Set<Integer> taxProcSeqIds = new HashSet<>();
        Money billAmtForTaxProcs = new Money(0);
        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(null, accnId);
        for (AccnProc ap : accnProcs)
        {
            if (ap.getTaxProcSeqId() != null && ap.getTaxProcSeqId() > 0)
            {
                taxProcSeqIds.add(ap.getTaxProcSeqId());
            }
        }
        for (AccnProc ap : accnProcs)
        {
            if (taxProcSeqIds.contains(ap.getAccnProcSeqId()))
            {
                billAmtForTaxProcs.add(ap.getBilPrcAsMoney());
            }
        }
        Assert.assertTrue(taxProcSeqIds.size() > 0, "Accession does not contain any tax procs");

        logger.info("Resetting EOB records for claim, internalCtrlId="+internalCtrlId);
        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(), 1);
        resetEobStageForClaim(accnId, internalCtrlId);

        EobClaim eobClaim = eobClaims.get(0);
        int reconcStageId = waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(reconcStageId > 0);

        logger.info("Verify Reconciliation Engine processed the claim - checking reconcile stage on EOB Claim");
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        eobClaim = eobClaims.get(0);
        Assert.assertEquals(eobClaim.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        logger.info("Verify Reconciliation Engine processed the EFT - checking reconcile stage on EFT");
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify no discrepancy is created");
        Assert.assertFalse(eobClaim.getIsDiscrepancy());

        logger.info("Verify deposit is created");
        Dep dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());
        logger.info("Verify accession payments for tax procs");
        List<AccnPmt> accnPmtsForTaxProcs = new ArrayList<>();
        Money allowAmtForTaxProcs = new Money(0);
        Money billAmtForTaxProcsFromEob = new Money(0);
        for (AccnPmt accnPmt : rpmDao.getAccnPmtsByAccnId(null, accnId))
        {
            Assert.assertEquals(accnPmt.getDepId(), dep.getDepId(), "Payment from unknown deposit found");
            if (accnPmt.getAccnProcSeqId() > 0 && taxProcSeqIds.contains(accnPmt.getAccnProcSeqId()))
            {
                accnPmtsForTaxProcs.add(accnPmt);
                allowAmtForTaxProcs.add(accnPmt.getAllowAmtAsMoney());
                billAmtForTaxProcsFromEob.add(accnPmt.getBillAmtFromEobAsMoney());
            }
        }
        Assert.assertEquals(accnPmtsForTaxProcs.size(), taxProcSeqIds.size(), "Incorrect payment count for tax procs");
        Assert.assertEquals(allowAmtForTaxProcs, Money.ZERO, "Allow amount for tax procs is not $0");
        Assert.assertEquals(billAmtForTaxProcsFromEob, billAmtForTaxProcs, "Bill amount for tax procs is not equal to bill amount from EOB");

        logger.info("Verify accession adjustments for tax procs");
        List<AccnAdj> accnAdjsForTaxProcs = new ArrayList<>();
        Money conallAmtForTaxProcs = new Money(0);
        for (AccnAdj accnAdj : rpmDao.getAccnAdjsByAccnId(null, accnId))
        {
            Assert.assertEquals(accnAdj.getDepId(), dep.getDepId(), "Adjustment from unknown deposit found");
            if (accnAdj.getAccnProcSeqId() > 0 && taxProcSeqIds.contains(accnAdj.getAccnProcSeqId()))
            {
                Assert.assertEquals(accnAdj.getAdjCdId(), MiscMap.ADJ_CD_PAYMENT, "Non-conall adjustment found for tax proc");
                accnAdjsForTaxProcs.add(accnAdj);
                conallAmtForTaxProcs.add(accnAdj.getBilAdjAmtAsMoney());
            }
        }
        Assert.assertTrue(accnAdjsForTaxProcs.isEmpty(), "Found conall adjustments on tax procs");
        Assert.assertEquals(conallAmtForTaxProcs, Money.ZERO, "Conall amount for tax procs is not $0");
    }

    @Test(priority = 1, description = "Tax Proc Allowed Amount > $0 Is Applied Correctly")
    @Parameters({"accnId", "internalCtrlId"})
    public void testRPM108295(String accnId, String internalCtrlId) throws Exception
    {
        logger.info("Running test RPM108295");
        logger.info("Verifying tax procs on accession, accnId="+accnId);
        Set<Integer> taxProcSeqIds = new HashSet<>();
        Money billAmtForTaxProcs = new Money(0);
        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(null, accnId);
        for (AccnProc ap : accnProcs)
        {
            if (ap.getTaxProcSeqId() != null && ap.getTaxProcSeqId() > 0)
            {
                taxProcSeqIds.add(ap.getTaxProcSeqId());
            }
        }
        for (AccnProc ap : accnProcs)
        {
            if (taxProcSeqIds.contains(ap.getAccnProcSeqId()))
            {
                billAmtForTaxProcs.add(ap.getBilPrcAsMoney());
            }
        }
        Assert.assertTrue(taxProcSeqIds.size() > 0, "Accession does not contain any tax procs");

        logger.info("Resetting EOB records for claim, internalCtrlId="+internalCtrlId);
        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(),1);
        resetEobStageForClaim(accnId, internalCtrlId);

        EobClaim eobClaim = eobClaims.get(0);
        int reconcStageId = waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(reconcStageId > 0);

        logger.info("Verify Reconciliation Engine processed the claim - checking reconcile stage on EOB Claim");
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        eobClaim = eobClaims.get(0);
        Assert.assertEquals(eobClaim.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        logger.info("Verify Reconciliation Engine processed the EFT - checking reconcile stage on EFT");
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify no discrepancy is created");
        Assert.assertFalse(eobClaim.getIsDiscrepancy());

        logger.info("Verify deposit is created");
        Dep dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());
        logger.info("Verify accession payments for tax procs");
        List<AccnPmt> accnPmtsForTaxProcs = new ArrayList<>();
        Money allowAmtForTaxProcs = new Money(0);
        Money billAmtForTaxProcsFromEob = new Money(0);
        for (AccnPmt accnPmt : rpmDao.getAccnPmtsByAccnId(null, accnId))
        {
            Assert.assertEquals(accnPmt.getDepId(), dep.getDepId(), "Payment from unknown deposit found");
            if (accnPmt.getAccnProcSeqId() > 0 && taxProcSeqIds.contains(accnPmt.getAccnProcSeqId()))
            {
                accnPmtsForTaxProcs.add(accnPmt);
                allowAmtForTaxProcs.add(accnPmt.getAllowAmtAsMoney());
                billAmtForTaxProcsFromEob.add(accnPmt.getBillAmtFromEobAsMoney());
            }
        }
        Assert.assertEquals(accnPmtsForTaxProcs.size(), taxProcSeqIds.size(), "Incorrect payment count for tax procs");
        Assert.assertTrue(allowAmtForTaxProcs.greaterThan(Money.ZERO), "Allow amount for tax procs is not > $0");
        Assert.assertEquals(billAmtForTaxProcsFromEob, billAmtForTaxProcs, "Bill amount for tax procs is not equal to bill amount from EOB");

        logger.info("Verify accession adjustments for tax procs");
        List<AccnAdj> accnAdjsForTaxProcs = new ArrayList<>();
        Money conallAmtForTaxProcs = new Money(0);
        for (AccnAdj accnAdj : rpmDao.getAccnAdjsByAccnId(null, accnId))
        {
            Assert.assertEquals(accnAdj.getDepId(), dep.getDepId(), "Adjustment from unknown deposit found");
            if (accnAdj.getAccnProcSeqId() > 0 && taxProcSeqIds.contains(accnAdj.getAccnProcSeqId()))
            {
                Assert.assertEquals(accnAdj.getAdjCdId(), MiscMap.ADJ_CD_PAYMENT, "Non-conall adjustment found for tax proc");
                accnAdjsForTaxProcs.add(accnAdj);
                conallAmtForTaxProcs.add(accnAdj.getBilAdjAmtAsMoney());
            }
        }
        Assert.assertEquals(accnAdjsForTaxProcs.size(), taxProcSeqIds.size(), "Verify conall adjustments on tax procs");
        Money expectedConallAmt = new Money(billAmtForTaxProcs);
        expectedConallAmt.subtract(allowAmtForTaxProcs);
        expectedConallAmt.negate();
        Assert.assertEquals(conallAmtForTaxProcs, expectedConallAmt, "Conall amount for tax procs is correct");
    }

    @Test(priority = 1, description = "Test Pseudo-reprice is performed")
    @Parameters({"accnId", "origPyrAbbrv", "eobPyrAbbrv", "internalCtrlId"})
    public void testPseudoRepriceIsPerformed(String accnId, String origPyrAbbrv, String eobPyrAbbrv, String internalCtrlId) throws Exception
    {
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(null, origPyrAbbrv);
        Pyr eobPyr = rpmDao.getPyrByPyrAbbrv(null, eobPyrAbbrv);

        logger.info("Clearing all QAccnSubm records except the one that matches the ICN for EOB, accnId="+accnId+", icn="+internalCtrlId);
        Set<String> icns = new HashSet<>();
        icns.add(internalCtrlId);
        cleanQASForPyrOnAccnExceptICNs(accnId, 0, icns);

        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(),1);

        List<QAccnSubm> origQasList = submissionDao.getQAccnSubmsByAccnId(accnId);
        Assert.assertEquals(origQasList.size(),1);
        Assert.assertTrue(origQasList.get(0).getSubmFileSeqId() > 0);

        logger.info("Verifying primary and eob payor, origPyrAbbrv="+origPyrAbbrv+", eobPyrAbbrv="+eobPyrAbbrv);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 2);
        for (AccnPyr accnPyr : accnPyrs)
        {
            if (accnPyr.getPyrId() == origPyr.getPyrId())
            {
                accnPyr.setPyrPrio(1);
            }
            else if (accnPyr.getPyrId() == eobPyr.getPyrId())
            {
                accnPyr.setPyrPrio(2);
            }
            accessionDao.setAccnPyr(accnPyr);
        }
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 2);
        Assert.assertEquals(accnPyrs.get(0).getPyrId(), origPyr.getPyrId());
        Assert.assertEquals(accnPyrs.get(1).getPyrId(), eobPyr.getPyrId());

        logger.info("Repricing accession, accnId="+accnId);
        QValidateAccn qva;
        try
        {
            qva = accessionDao.getQValidateAccn(accnId);
            qva.setIsErr(false);
        }
        catch (XifinDataNotFoundException e)
        {
            qva = new QValidateAccn();
            qva.setAccnId(accnId);
        }
        qva.setValidateTypId(ValidateQueueMap.RE_VALIDATE_RE_PRICE);
        qva.setPriority(3);
        accessionDao.setQValidateAccn(qva);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfValidationQueue(accnId, ValidateQueueMap.RE_VALIDATE_RE_PRICE, QUEUE_WAIT_TIME));
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Expected accn to leave eligibility queue, accnId=" + accnId);
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME), "Expected accn to leave pricing queue, accnId=" + accnId);
        Accn accn = accessionDao.getAccn(accnId);
        // Update accnProc.submFileSeqId to point back to the original submitted claim, since they are zeroed out after reprice.
        for (AccnProc accnProc : rpmDao.getAccnProcsByAccnId(null, accnId))
        {
            accnProc.setSubmFileSeqId(origQasList.get(0).getSubmFileSeqId());
            rpmDao.set(accnProc);
        }
        List<QAccnSubm> unprocessedQasList = new ArrayList<>();
        for (QAccnSubm qas : submissionDao.getQAccnSubmsByAccnId(accnId))
        {
            if (qas.getNextSubmDt() != null)
            {
                unprocessedQasList.add(qas);
            }
        }
        Assert.assertEquals(unprocessedQasList.size(), 1, "Expected 1 unprocessed QAccnSubm record, accnId="+accnId+", unprocessedQasCnt="+unprocessedQasList.size());
        Assert.assertEquals(unprocessedQasList.get(0).getPyrId(), origPyr.getPyrId(), "Expected unprocessed QAccnSubm record to be for original primary payor, accnId="+accnId+", unprocessedQasPyrId="+unprocessedQasList.get(0).getPyrId()+", origPyrId="+origPyr.getPyrId());
        Assert.assertEquals(unprocessedQasList.get(0).getPyrPrio(), 1, "Expected unprocessed QAccnSubm record to be for payor prio 1, accnId="+accnId+", unprocessedQasPyrPrio="+unprocessedQasList.get(0).getPyrPrio());

        java.sql.Date newNextSubmDt =  new java.sql.Date(DateUtils.addDays(unprocessedQasList.get(0).getNextSubmDt(), 365).getTime());
        logger.info("Updating next subm date, accnId="+accnId+", docSeqId="+unprocessedQasList.get(0).getDocSeqId()+", origNextSubmDt="+unprocessedQasList.get(0).getNextSubmDt()+", newNextSubmDt="+newNextSubmDt);
        unprocessedQasList.get(0).setNextSubmDt(newNextSubmDt);
        rpmDao.set(unprocessedQasList.get(0));

        logger.info("Resetting EOB stage, accnId="+accnId+", internalCtrlId="+internalCtrlId);
        resetEobStageForClaim(accnId, internalCtrlId);

        logger.info("Wait for Reconciliation Engine to process the claim, accnId="+accnId);
        Assert.assertEquals(waitForReconciliationEngine(eobClaims.get(0), QUEUE_WAIT_TIME_MS) , ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(),1);
        EobFinancialTrans eft = rpmDao.getEobFinancialTrans(testDb, eobClaims.get(0).getEobFinancialTransId());
        Assert.assertEquals(eft.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify EOB processed from secondary payor, accnId="+accnId);
        Assert.assertEquals(eobClaims.get(0).getPyrId(), eobPyr.getPyrId());
        Assert.assertEquals(eobClaims.get(0).getPyrPrio(), 1);

        logger.info("Verify payors have been shuffled, accnId="+accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 2);
        Assert.assertEquals(accnPyrs.get(0).getPyrPrio(), 1);
        Assert.assertEquals(accnPyrs.get(0).getPyrId(), eobPyr.getPyrId());
        Assert.assertEquals(accnPyrs.get(1).getPyrPrio(), 2);
        Assert.assertEquals(accnPyrs.get(1).getPyrId(), origPyr.getPyrId());

        int origPrcCnt = accn.getPrcCnt();
        logger.info("Verify accession is still priced, accnId="+accnId);
        accn = accessionDao.getAccn(accnId);
        Assert.assertNotNull(accn.getPrcDt());
        Assert.assertEquals(accn.getPrcCnt(), origPrcCnt+1);
        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(null, accnId);
        Assert.assertFalse(accnProcs.isEmpty());
        Assert.assertEquals(accnProcs.get(0).getJurisdictionPyrId(), origPyr.getPyrId());
        List<DepBatchSeq> depBatchSeqs = paymentDao.getDepBatchSeqsByAccnId(accnId);
        Assert.assertEquals(depBatchSeqs.size(), 1);
        Assert.assertEquals(depBatchSeqs.get(0).getPyrId(), eobPyr.getPyrId());
        Assert.assertEquals(depBatchSeqs.get(0).getPyrPrio(), 1);
        Assert.assertEquals(depBatchSeqs.get(0).getPrevStaId(), AccnStatusMap.ACCN_STATUS_PRICED);
        unprocessedQasList.clear();
        for (QAccnSubm qas : submissionDao.getQAccnSubmsByAccnId(accnId))
        {
            if (qas.getNextSubmDt() != null)
            {
                unprocessedQasList.add(qas);
            }
        }
        Assert.assertTrue(unprocessedQasList.isEmpty(), "Expected no unprocessed QAS records, accnId="+accnId+", unprocessedQasCnt="+unprocessedQasList.size());
    }

    @Test(priority = 1, description = "Test Pseudo-reprice is not performed for Limited Coverage")
    @Parameters({"accnId", "origPyrAbbrv", "eobPyrAbbrv", "internalCtrlId"})
    public void testPseudoRepriceIsNotPerformedForLimitedCoverage(String accnId, String origPyrAbbrv, String eobPyrAbbrv, String internalCtrlId) throws Exception
    {
        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(),1);

        logger.info("Verifying primary and eob payor, origPyrAbbrv="+origPyrAbbrv+", eobPyrAbbrv="+eobPyrAbbrv);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 2);
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(null, origPyrAbbrv);
        Pyr eobPyr = rpmDao.getPyrByPyrAbbrv(null, eobPyrAbbrv);
        for (AccnPyr accnPyr : accnPyrs)
        {
            if (accnPyr.getPyrId() == origPyr.getPyrId())
            {
                accnPyr.setPyrPrio(1);
            }
            else if (accnPyr.getPyrId() == eobPyr.getPyrId())
            {
                accnPyr.setPyrPrio(2);
            }
            accessionDao.setAccnPyr(accnPyr);
        }
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 2);
        Assert.assertEquals(accnPyrs.get(0).getPyrId(), origPyr.getPyrId());
        Assert.assertEquals(accnPyrs.get(1).getPyrId(), eobPyr.getPyrId());

        logger.info("Repricing accession, accnId="+accnId);
        QValidateAccn qva;
        try
        {
            qva = accessionDao.getQValidateAccn(accnId);
            qva.setIsErr(false);
        }
        catch (XifinDataNotFoundException e)
        {
            qva = new QValidateAccn();
            qva.setAccnId(accnId);
        }
        qva.setValidateTypId(ValidateQueueMap.RE_VALIDATE_RE_PRICE);
        qva.setPriority(3);
        accessionDao.setQValidateAccn(qva);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfValidationQueue(accnId, ValidateQueueMap.RE_VALIDATE_RE_PRICE, QUEUE_WAIT_TIME));
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Expected accn to leave eligibility queue, accnId=" + accnId);
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME), "Expected accn to leave pricing queue, accnId=" + accnId);

        Accn accn = accessionDao.getAccn(accnId);

        logger.info("Verifying no open LCNOABN errors, accnId="+accnId);
        ErrCd lcnoabnErr = rpmDao.getErrCd(null, DbErrorMap.LCNOABN, EpConstants.DNL_TBL_ID_UNBILL, accn.getDos());
        List<AccnProcErr> lcnoabnErrs = new ArrayList<>();
        for (AccnProcErr ape : rpmDao.getUnfixedAccnProcErrsByAccnId(null, accnId))
        {
            if (ape.getPyrPrio() > 0 && ape.getErrCd() == lcnoabnErr.getErrCd())
            {
                lcnoabnErrs.add(ape);
            }
        }
        Assert.assertTrue(lcnoabnErrs.isEmpty());

        logger.info("Resetting EOB stage, accnId="+accnId+", internalCtrlId="+internalCtrlId);
        resetEobStageForClaim(accnId, internalCtrlId);

        logger.info("Wait for Reconciliation Engine to process the claim, accnId="+accnId);
        Assert.assertEquals(waitForReconciliationEngine(eobClaims.get(0), QUEUE_WAIT_TIME_MS) , ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(),1);
        EobFinancialTrans eft = rpmDao.getEobFinancialTrans(testDb, eobClaims.get(0).getEobFinancialTransId());
        Assert.assertEquals(eft.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify EOB processed from secondary payor, accnId="+accnId);
        Assert.assertEquals(eobClaims.get(0).getPyrId(), eobPyr.getPyrId());
        Assert.assertEquals(eobClaims.get(0).getPyrPrio(), 1);

        logger.info("Verify payors have been shuffled, accnId="+accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 2);
        Assert.assertEquals(accnPyrs.get(0).getPyrPrio(), 1);
        Assert.assertEquals(accnPyrs.get(0).getPyrId(), eobPyr.getPyrId());
        Assert.assertEquals(accnPyrs.get(1).getPyrPrio(), 2);
        Assert.assertEquals(accnPyrs.get(1).getPyrId(), origPyr.getPyrId());

        int origPrcCnt = accn.getPrcCnt();
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Expected accn to leave eligibility queue, accnId=" + accnId);
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME), "Expected accn to leave pricing queue, accnId=" + accnId);
        logger.info("Verify accession has been repriced, accnId="+accnId);
        accn = accessionDao.getAccn(accnId);
        Assert.assertNotNull(accn.getPrcDt());
        Assert.assertEquals(accn.getPrcCnt(), origPrcCnt+1);
        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(null, accnId);
        Assert.assertFalse(accnProcs.isEmpty());
        Assert.assertEquals(accnProcs.get(0).getJurisdictionPyrId(), eobPyr.getPyrId());
        List<DepBatchSeq> depBatchSeqs = paymentDao.getDepBatchSeqsByAccnId(accnId);
        Assert.assertEquals(depBatchSeqs.size(), 1);
        Assert.assertEquals(depBatchSeqs.get(0).getPyrId(), eobPyr.getPyrId());
        Assert.assertEquals(depBatchSeqs.get(0).getPyrPrio(), 1);
        Assert.assertEquals(depBatchSeqs.get(0).getPrevStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);

        logger.info("Verify LCNOABN has been added, accnId="+accnId);
        for (AccnProcErr ape : rpmDao.getUnfixedAccnProcErrsByAccnId(null, accnId))
        {
            if (ape.getPyrPrio() > 0 && ape.getErrCd() == lcnoabnErr.getErrCd())
            {
                lcnoabnErrs.add(ape);
            }
        }
        Assert.assertEquals(lcnoabnErrs.size(), 1);
        Assert.assertEquals(lcnoabnErrs.get(0).getPyrPrio(), 1);
        Assert.assertTrue(DateUtils.isSameDay(lcnoabnErrs.get(0).getErrDt(), TODAYS_DT));
        // Pricing engine is not setting FK_PYR_PRIO correctly for this error
        // Assert.assertEquals(lcnoabnErrs.get(0).getPyrId(), eobPyr.getPyrId());
    }

    @Test(priority = 1, description = "Test batch is not released when expect price discrepancies exist")
    @Parameters({"eobFinancialTransId"})
    public void testBatchNotReleasedWhenExpectPriceDiscrepanciesExist(int eobFinancialTransId) throws Exception
    {
        List<String> accnIds = deleteDepData(eobFinancialTransId);
        Assert.assertEquals(accnIds.size(), 2, "Expected 2 accession IDs in EOB");

        for (String accnId : accnIds)
        {
            logger.info("Repricing accession, accnId="+accnId);
            QValidateAccn qva;
            try
            {
                qva = accessionDao.getQValidateAccn(accnId);
                qva.setIsErr(false);
            }
            catch (XifinDataNotFoundException e)
            {
                qva = new QValidateAccn();
                qva.setAccnId(accnId);
            }
            qva.setValidateTypId(ValidateQueueMap.RE_VALIDATE_RE_PRICE);
            qva.setPriority(3);
            accessionDao.setQValidateAccn(qva);
        }

        for (String accnId : accnIds)
        {
            Assert.assertTrue(accessionDao.waitForAccnToBeOutOfValidationQueue(accnId, ValidateQueueMap.RE_VALIDATE_RE_PRICE, QUEUE_WAIT_TIME));
            Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Expected accn to leave eligibility queue, accnId=" + accnId);
            Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME), "Expected accn to leave pricing queue, accnId=" + accnId);
        }

        resetEob(eobFinancialTransId);
        List<EobClaim> eobClaims = rpmDao.getEobClaimsByEobFinancialTransId(eobFinancialTransId);

        for (EobClaim eobClaim : eobClaims)
        {
            Assert.assertEquals(waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        }

        EobFinancialTrans eft = rpmDao.getEobFinancialTrans(testDb, eobFinancialTransId);
        Assert.assertEquals(eft.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId(), "EFT Reconciliation not complete");

        logger.info("Verifying deposit data, eobFinancialTransId="+eobFinancialTransId);
        Dep dep = rpmDao.getDepByEobFinancialTransId(null, eobFinancialTransId);
        List<String> depAccnIds = new ArrayList<>();
        for (DepBatchSeq depBatchSeq : rpmDao.getDepBatchSeqByDepId(null, dep.getDepId()))
        {
            depAccnIds.add(depBatchSeq.getAccnId());
        }
        Assert.assertEquals(depAccnIds, accnIds, "Deposit accession IDs do not match expected");

        List<DepBatch> depBatchList = rpmDao.getDepBatchByDepId(null, dep.getDepId());
        Assert.assertEquals(depBatchList.size(), 1, "Deposit batch count does not match expected");

        Assert.assertFalse(depBatchList.get(0).getIsDiscrpRelease(), "Deposit batch should not be released, depId="+depBatchList.get(0).getDepId()+", batchId="+depBatchList.get(0).getBatchId());
    }

    @Test(priority = 1, description = "Test batch released when no expect price discrepancies exist")
    @Parameters({"eobFinancialTransId", "bankAbbrv", "conallAmt"})
    public void testBatchReleasedWhenNoExpectPriceDiscrepanciesExist(int eobFinancialTransId, String bankAbbrv, String conallAmt) throws Exception
    {
        Money expectedConallAmt = new Money(conallAmt);

        List<String> accnIds = deleteDepData(eobFinancialTransId);
        Assert.assertEquals(accnIds.size(), 1, "Expected 1 accession ID in EOB");

        List<EobClaim> eobClaims = rpmDao.getEobClaimsByEobFinancialTransId(eobFinancialTransId);
        Assert.assertEquals(eobClaims.size(), 1, "Expected 1 EobClaim, eobFinancialTransId="+eobFinancialTransId+", eobClaimCnt="+eobClaims.size());
        List<EobSvcPmt> eobSvcPmts = rpmDao.getEobSvcPmtsByEobClaimId(eobClaims.get(0).getSeqId());
        Assert.assertEquals(eobSvcPmts.size(), 1, "Expected 1 EobSvcPmt, eobFinancialTransId="+eobFinancialTransId+", eobSvcPmtCnt="+eobSvcPmts.size());
        List<EobSvcAdj> eobSvcAdjs = rpmDao.getEobSvcAdjByEobSvcPmtId(null, eobSvcPmts.get(0).getSeqId());
        Assert.assertTrue(eobSvcAdjs.size() > 0, "Expected 1 or more EobSvcAdj, eobFinancialTransId="+eobFinancialTransId+", eobSvcAdjCnt="+eobSvcAdjs.size());

        logger.info("Repricing accession, accnId="+accnIds.get(0));
        QValidateAccn qva;
        try
        {
            qva = accessionDao.getQValidateAccn(accnIds.get(0));
            qva.setIsErr(false);
        }
        catch (XifinDataNotFoundException e)
        {
            qva = new QValidateAccn();
            qva.setAccnId(accnIds.get(0));
        }
        qva.setValidateTypId(ValidateQueueMap.RE_VALIDATE_RE_PRICE);
        qva.setPriority(3);
        accessionDao.setQValidateAccn(qva);

        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfValidationQueue(accnIds.get(0), ValidateQueueMap.RE_VALIDATE_RE_PRICE, QUEUE_WAIT_TIME));
        Assert.assertTrue(isOutOfEligibilityQueue(accnIds.get(0), QUEUE_WAIT_TIME), "Expected accn to leave eligibility queue, accnId=" + accnIds.get(0));
        Assert.assertTrue(isOutOfPricingQueue(accnIds.get(0), QUEUE_WAIT_TIME), "Expected accn to leave pricing queue, accnId=" + accnIds.get(0));

        resetEob(eobFinancialTransId);

        Assert.assertEquals(waitForReconciliationEngine(eobClaims.get(0), QUEUE_WAIT_TIME_MS), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        EobFinancialTrans eft = rpmDao.getEobFinancialTrans(testDb, eobFinancialTransId);
        Assert.assertEquals(eft.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId(), "EFT Reconciliation not complete, eobFinancialTransId="+eobFinancialTransId);

        eobClaims = rpmDao.getEobClaimsByEobFinancialTransId(eobFinancialTransId);
        Assert.assertEquals(eobClaims.get(0).getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId(), "EobClaim reconciliation not complete, eobFinancialTransId="+eobFinancialTransId+", eobClaimId="+eobClaims.get(0).getSeqId());

        Assert.assertTrue(eobClaims.get(0).getPyrId() > 0, "EobClaim payor ID should be > 0, eobFinancialTransId="+eobFinancialTransId+", eobClaimId="+eobClaims.get(0).getSeqId()+", eobClaimPyrId="+eobClaims.get(0).getPyrId());
        Assert.assertEquals(eobClaims.get(0).getPyrPrio(), 1, "EobClaim payor prio should be 1, eobFinancialTransId="+eobFinancialTransId+", eobClaimId="+eobClaims.get(0).getSeqId()+", eobClaimPyrPrio="+eobClaims.get(0).getPyrPrio());

        logger.info("Verifying deposit data, eobFinancialTransId="+eobFinancialTransId);
        Dep dep = rpmDao.getDepByEobFinancialTransId(null, eobFinancialTransId);
        Bank bank = bankDao.getBankByBankAbbrev(bankAbbrv);
        Assert.assertEquals(dep.getBankId(), bankDao.getBankByBankAbbrev(bankAbbrv).getBankId(), "Bank ID does not match expected value, depId="+dep.getDepId()+", actualBankId="+dep.getBankId()+", expectedBankId="+bank.getBankId());
        Assert.assertEquals(dep.getUserId(), USER_ID_RECONCILIATION_ENGINE, "User ID does not match expected value, depId="+dep.getDepId()+", actualUserId="+dep.getUserId()+", expectedUserId="+USER_ID_RECONCILIATION_ENGINE);
        Assert.assertNull(dep.getPostDt(), "Post Date should be null, depId="+dep.getDepId()+", actualPostDt="+dep.getPostDt());
        Money eftTransAmt = new Money(eft.getTransAmtStr());
        Assert.assertEquals(dep.getDepAmtAsMoney(), eftTransAmt, "Deposit amount should equal value from EFT, depId="+dep.getDepId()+", actualDepAmt="+dep.getDepAmtAsMoney()+", eftTransAmt="+eftTransAmt);
        Assert.assertFalse(dep.getIsPosted(), "Deposit should not be posted, depId="+dep.getDepId());
        Assert.assertEquals(dep.getEftId(), eft.getEftTraceNum(), "Deposit EFT ID should match EFT trace number, depId="+dep.getDepId()+", eftId="+dep.getEftId()+", eftTraceNum="+eft.getEftTraceNum());
        Assert.assertEquals(dep.getEntries(), 1, "Deposit should have 1 entry, depId="+dep.getDepId()+", entries="+dep.getEntries());
        Assert.assertEquals(dep.getPostedCount(), 0, "Deposit should have 0 posted entries, depId="+dep.getDepId()+", postedCount="+dep.getPostedCount());
        Assert.assertTrue(DateUtils.isSameDay(dep.getAcctngDt(), new Date()), "Deposit accounting date should be today, depId="+dep.getDepId()+", acctngDt="+dep.getAcctngDt());
        Date eftEffectiveDt = EFT_DATE_FMT.parse(eft.getEffectiveDtStr());
        Assert.assertTrue(DateUtils.isSameDay(dep.getRemitDt(), eftEffectiveDt), "Deposit remit date should match EFT, depId="+dep.getDepId()+", remitDt="+dep.getRemitDt()+", eftEffectiveDt="+eftEffectiveDt);

        List<DepBatch> depBatches  = rpmDao.getDepBatchByDepId(null, dep.getDepId());
        Assert.assertEquals(depBatches.size(), 1, "Deposit should have 1 batch, depId="+dep.getDepId()+", depBatchCnt="+depBatches.size());
        Assert.assertEquals(depBatches.get(0).getStaId(), DEP_BATCH_STA_TYP_WIP, "Deposit batch status should be WIP (3), depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId()+", depBatchStaId="+depBatches.get(0).getStaId());
        Assert.assertEquals(depBatches.get(0).getUserId(), USER_ID_RECONCILIATION_ENGINE, "Deposit batch user ID does not match, depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId()+", userId="+depBatches.get(0).getUserId()+", expectedUserId="+USER_ID_RECONCILIATION_ENGINE);
        Assert.assertEquals(depBatches.get(0).getPyrId(), eobClaims.get(0).getPyrId(), "Deposit batch payor ID does not match EobClaim, depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId()+", depBatchPyrId="+depBatches.get(0).getPyrId()+", eobClaimPyrId="+eobClaims.get(0).getPyrId());
        Assert.assertEquals(depBatches.get(0).getBatchAmtAsMoney(), eftTransAmt, "Deposit batch amount should equal value from EFT, depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId()+", actualDepBatchAmt="+depBatches.get(0).getBatchAmtAsMoney()+", eftTransAmt="+eftTransAmt);
        Assert.assertEquals(depBatches.get(0).getApplAmtAsMoney(), eftTransAmt, "Deposit batch applied amount should equal value from EFT, depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId()+", actualDepBatchAppliedAmt="+depBatches.get(0).getApplAmtAsMoney()+", eftTransAmt="+eftTransAmt);
        Assert.assertEquals(depBatches.get(0).getAdjAmtAsMoney(), expectedConallAmt, "Deposit batch adjustment amount should equal expected conall amount, depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId()+", depBatchAdjAmt="+depBatches.get(0).getAdjAmtAsMoney()+", expectedConallAmt="+expectedConallAmt);
        Assert.assertFalse(depBatches.get(0).getIsPosted(), "Deposit batch should not be posted, depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId());
        Assert.assertEquals(depBatches.get(0).getPostAmtAsMoney(), new Money(0), "Deposit batch posted amount should be 0, depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId()+", depBatchPostedAmt="+depBatches.get(0).getPostAmtAsMoney());
        Assert.assertEquals(depBatches.get(0).getPostedCount(), 0, "Deposit batch posted count should be 0, depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId()+", depBatchPostedCount="+depBatches.get(0).getPostedCount());
        Assert.assertEquals(depBatches.get(0).getEntries(), 1, "Deposit batch should contain 1 entry, depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId()+", entries="+depBatches.get(0).getEntries());
        Assert.assertTrue(depBatches.get(0).getIsDiscrpRelease(), "Deposit batch should be marked as disrepancies released, depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId());
        Assert.assertEquals(depBatches.get(0).getDepBatchTypId(), DEP_BATCH_TYP_PAYMENTS, "Deposit batch type should be Payments (1), depId="+dep.getDepId()+", depBatchId="+depBatches.get(0).getBatchId()+", depBatchTypId="+depBatches.get(0).getDepBatchTypId());

        List<DepBatchSeq> depBatchSeqs = rpmDao.getDepBatchSeqByDepId(null, dep.getDepId());
        Assert.assertEquals(depBatchSeqs.size(), 1, "Deposit should have 1 DepBatchSeq, depId="+dep.getDepId()+", depBatchSeqCnt="+depBatchSeqs.size());
        Assert.assertEquals(depBatchSeqs.get(0).getPyrId(), eobClaims.get(0).getPyrId(), "DepBatchSeq payor ID should match payor ID from EobClaim, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId()+", depBatchSeqPyrId="+depBatchSeqs.get(0).getPyrId()+", eobClaimPyrId="+eobClaims.get(0).getPyrId());
        Assert.assertEquals(depBatchSeqs.get(0).getPyrId(), eobClaims.get(0).getPyrId(), "DepBatchSeq payor prio should match payor prio from EobClaim, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId()+", depBatchSeqPyrId="+depBatchSeqs.get(0).getPyrPrio()+", eobClaimPyrId="+eobClaims.get(0).getPyrPrio());
        Assert.assertEquals(depBatchSeqs.get(0).getAccnId(), accnIds.get(0), "DepBatchSeq accession ID is incorrect, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId()+", depBatchSeqAccnId="+depBatchSeqs.get(0).getAccnId()+", expectedAccnId="+accnIds.get(0));
        Assert.assertEquals(depBatchSeqs.get(0).getChkNum(), eft.getEftTraceNum(), "DepBatchSeq check number is incorrect, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId()+", depBatchSeqCheckNum="+depBatchSeqs.get(0).getChkNum()+", expectedCheckNum="+eft.getEftTraceNum());
        Money eobClaimTotalChargeAmt = new Money(eobClaims.get(0).getTotChargeAmtStr());
        Assert.assertEquals(depBatchSeqs.get(0).getAmtDueAsMoney(), eobClaimTotalChargeAmt, "DepBatchSeq amount due is incorrect, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId()+", depBatchSeqAmtDue="+depBatchSeqs.get(0).getAmtDueAsMoney()+", expectedAmtDue="+eobClaimTotalChargeAmt);
        Money eobClaimTotalPaidAmt = new Money(eobClaims.get(0).getTotPaidAmtStr());
        Assert.assertEquals(depBatchSeqs.get(0).getPaidAmtAsMoney(), eobClaimTotalPaidAmt, "DepBatchSeq amount paid is incorrect, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId()+", depBatchSeqAmtPaid="+depBatchSeqs.get(0).getPaidAmtAsMoney()+", expectedAmtPaid="+eobClaimTotalPaidAmt);
        Assert.assertEquals(depBatchSeqs.get(0).getAdjAmtAsMoney(), expectedConallAmt, "DepBatchSeq adjustment amount is incorrect, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId()+", depBatchSeqAdjAmt="+depBatchSeqs.get(0).getAdjAmtAsMoney()+", expectedAdjAmt="+expectedConallAmt);
        Assert.assertFalse(depBatchSeqs.get(0).getIsPosted(), "DepBatchSeq should not be posted, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId());
        Assert.assertFalse(depBatchSeqs.get(0).getIsSusp(), "DepBatchSeq should not be suspense, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId());
        Assert.assertTrue(depBatchSeqs.get(0).getIsAccptAssgn(), "DepBatchSeq should be marked as accepts assignment, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId());
        Assert.assertEquals(depBatchSeqs.get(0).getIcn(), eobClaims.get(0).getExternalCtrlId(), "DepBatchSeq ICN is incorrect, depId="+depBatchSeqs.get(0).getDepId()+", depBatchId="+depBatchSeqs.get(0).getDepBatchId()+", depBatchSeqId="+depBatchSeqs.get(0).getDepBatchSeqId()+", depBatchSeqIcn="+depBatchSeqs.get(0).getIcn()+", expectedIcn="+eobClaims.get(0).getExternalCtrlId());

        List<AccnPmt> bulkAccnPmts = new ArrayList<>();
        List<AccnPmt> procAccnPmts = new ArrayList<>();
        for (AccnPmt accnPmt : rpmDao.getAccnPmtByDepId(null, dep.getDepId()))
        {
            if (accnPmt.getAccnProcSeqId() > 0)
            {
                procAccnPmts.add(accnPmt);
            }
            else
            {
                bulkAccnPmts.add(accnPmt);
            }
        }

        Assert.assertEquals(bulkAccnPmts.size(), 1, "Expected 1 bulk AccnPmt record, depId="+dep.getDepId()+", bulkAccnPmtCnt="+bulkAccnPmts.size());
        Assert.assertTrue(DateUtils.isSameDay(bulkAccnPmts.get(0).getPmtDt(), eftEffectiveDt), "Expected bulk AccnPmt payment date did not match, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtPmtDt="+bulkAccnPmts.get(0).getPmtDt()+", eftEffectiveDt="+eftEffectiveDt);
        Assert.assertEquals(bulkAccnPmts.get(0).getPmtTypId(), PmtMap.PMT_TYP_CHECK, "Expected bulk AccnPmt payment type did not match, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtPmtTypId="+bulkAccnPmts.get(0).getPmtTypId()+", expectedPmtTypId="+PmtMap.PMT_TYP_CHECK);
        Assert.assertEquals(bulkAccnPmts.get(0).getUserId(), USER_ID_RECONCILIATION_ENGINE, "Expected bulk AccnPmt user ID did not match, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtUserId="+bulkAccnPmts.get(0).getUserId()+", expectedUserId="+USER_ID_RECONCILIATION_ENGINE);
        Assert.assertEquals(bulkAccnPmts.get(0).getPyrId(), eobClaims.get(0).getPyrId(), "Expected bulk AccnPmt payor ID did not match, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtPyrId="+bulkAccnPmts.get(0).getPyrId()+", expectedPyrId="+eobClaims.get(0).getPyrId());
        Assert.assertEquals(bulkAccnPmts.get(0).getPmtPyrPrio(), eobClaims.get(0).getPyrPrio(), "Expected bulk AccnPmt payor prio did not match, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtPyrPrio="+bulkAccnPmts.get(0).getPmtPyrPrio()+", expectedPyrPrio="+eobClaims.get(0).getPyrPrio());
        Assert.assertEquals(bulkAccnPmts.get(0).getAllowAmtAsMoney(), new Money(0), "Bulk AccnPmt allow amount should be 0, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtAllowAmt="+bulkAccnPmts.get(0).getAllowAmtAsMoney());
        Assert.assertEquals(bulkAccnPmts.get(0).getDedAmtAsMoney(), new Money(0), "Bulk AccnPmt deductible amount should be 0, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtDedAmt="+bulkAccnPmts.get(0).getDedAmtAsMoney());
        Assert.assertEquals(bulkAccnPmts.get(0).getPaidAmtAsMoney(), new Money(0), "Bulk AccnPmt paid amount should be 0, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtPaidAmt="+bulkAccnPmts.get(0).getPaidAmtAsMoney());
        Assert.assertEquals(bulkAccnPmts.get(0).getCopayAmtAsMoney(), new Money(0), "Bulk AccnPmt copay amount should be 0, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtCopayAmt="+bulkAccnPmts.get(0).getCopayAmtAsMoney());
        Assert.assertEquals(bulkAccnPmts.get(0).getCoInsuranceAmtAsMoney(), new Money(0), "Bulk AccnPmt coinsurance amount should be 0, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtCoinsuranceAmt="+bulkAccnPmts.get(0).getCoInsuranceAmtAsMoney());
        Assert.assertEquals(bulkAccnPmts.get(0).getSequestAmtAsMoney(), new Money(0), "Bulk AccnPmt sequestration amount should be 0, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtSequestAmt="+bulkAccnPmts.get(0).getSequestAmtAsMoney());
        Assert.assertEquals(bulkAccnPmts.get(0).getPtRespAmtAsMoney(), new Money(0), "Bulk AccnPmt patient responsibility amount should be 0, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtPtRespAmt="+bulkAccnPmts.get(0).getPtRespAmtAsMoney());
        Assert.assertEquals(bulkAccnPmts.get(0).getBillAmtFromEobAsMoney(), new Money(0), "Bulk AccnPmt bill amount from EOB should be 0, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtBillAmtFromEob="+bulkAccnPmts.get(0).getBillAmtFromEobAsMoney());
        Assert.assertEquals(bulkAccnPmts.get(0).getUnitsPd(), 0, "Bulk AccnPmt units paid should be 0, accnId="+bulkAccnPmts.get(0).getAccnId()+", bulkAccnPmtUnitsPd="+bulkAccnPmts.get(0).getUnitsPd());
        Assert.assertTrue(bulkAccnPmts.get(0).getIsBulk(), "Bulk AccnPmt bulk flag should be true, accnId="+bulkAccnPmts.get(0).getAccnId());
        Assert.assertFalse(bulkAccnPmts.get(0).getIsPosted(), "Bulk AccnPmt should not be posted, accnId="+bulkAccnPmts.get(0).getAccnId());

        Assert.assertEquals(procAccnPmts.size(), 1, "Expected 1 proc-level AccnPmt record, depId="+dep.getDepId()+", accnPmtCnt="+procAccnPmts.size());
        Assert.assertTrue(DateUtils.isSameDay(procAccnPmts.get(0).getPmtDt(), eftEffectiveDt), "Expected proc-level AccnPmt payment date did not match, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtDt="+procAccnPmts.get(0).getPmtDt()+", eftEffectiveDt="+eftEffectiveDt);
        Assert.assertEquals(procAccnPmts.get(0).getPmtTypId(), PmtMap.PMT_TYP_CHECK, "Expected proc-level AccnPmt payment type did not match, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtPmtTypId="+procAccnPmts.get(0).getPmtTypId()+", expectedPmtTypId="+PmtMap.PMT_TYP_CHECK);
        Assert.assertEquals(procAccnPmts.get(0).getUserId(), USER_ID_RECONCILIATION_ENGINE, "Expected proc-level AccnPmt user ID did not match, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtUserId="+procAccnPmts.get(0).getUserId()+", expectedUserId="+USER_ID_RECONCILIATION_ENGINE);
        Assert.assertEquals(procAccnPmts.get(0).getPyrId(), eobClaims.get(0).getPyrId(), "Expected proc-level AccnPmt payor ID did not match, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtPyrId="+procAccnPmts.get(0).getPyrId()+", expectedPyrId="+eobClaims.get(0).getPyrId());
        Assert.assertEquals(procAccnPmts.get(0).getPmtPyrPrio(), eobClaims.get(0).getPyrPrio(), "Expected proc-level AccnPmt payor prio did not match, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtPyrPrio="+procAccnPmts.get(0).getPmtPyrPrio()+", expectedPyrPrio="+eobClaims.get(0).getPyrPrio());
        Assert.assertEquals(procAccnPmts.get(0).getBillAmtFromEobAsMoney(), eobClaimTotalChargeAmt, "Proc-level AccnPmt bill amount from EOB should equal EobClaim, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtBillAmtFromEob="+procAccnPmts.get(0).getBillAmtFromEobAsMoney()+", eobClaimTotalChargeAmt="+eobClaimTotalChargeAmt);
        Assert.assertEquals(procAccnPmts.get(0).getSequestAmtAsMoney(), new Money(0), "Proc-level AccnPmt sequestration amount should be 0, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtSequestAmt="+procAccnPmts.get(0).getSequestAmtAsMoney());
        Assert.assertEquals(procAccnPmts.get(0).getPaidAmtAsMoney(), eobClaimTotalPaidAmt, "Proc-level AccnPmt paid amount should match EobClaim, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtPaidAmt="+procAccnPmts.get(0).getPaidAmtAsMoney()+", eobClaimTotalPaidAmt="+eobClaimTotalPaidAmt);
        Money dedAmtFromEob = getEobAdjAmt(eobSvcAdjs, EOB_CODE_ADJ_GRP_PATIENT, EOB_ADJ_REASON_CD_DED);
        Assert.assertEquals(procAccnPmts.get(0).getDedAmtAsMoney(), dedAmtFromEob, "Proc-level AccnPmt deductible amount should match EOB, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtDedAmt="+procAccnPmts.get(0).getDedAmtAsMoney()+", eobDedAmt="+dedAmtFromEob);
        Money copayAmtFromEob = getEobAdjAmt(eobSvcAdjs, EOB_CODE_ADJ_GRP_PATIENT, EOB_ADJ_REASON_CD_COPAY);
        Assert.assertEquals(procAccnPmts.get(0).getCopayAmtAsMoney(), copayAmtFromEob, "Proc-level AccnPmt copay amount should match EOB, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtCopayAmt="+procAccnPmts.get(0).getCopayAmtAsMoney()+", eobCopayAmt="+copayAmtFromEob);
        Money coinsAmtFromEob = getEobAdjAmt(eobSvcAdjs, EOB_CODE_ADJ_GRP_PATIENT, EOB_ADJ_REASON_CD_COINS);
        Assert.assertEquals(procAccnPmts.get(0).getCoInsuranceAmtAsMoney(), coinsAmtFromEob, "Proc-level AccnPmt co-insurance amount should match EOB, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtCoinsAmt="+procAccnPmts.get(0).getCoInsuranceAmtAsMoney()+", eobCoinsAmt="+coinsAmtFromEob);
        Assert.assertEquals(procAccnPmts.get(0).getPtRespAmtAsMoney(), new Money(0), "Proc-level AccnPmt other patient responsibility amount should be 0, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtPtRespAmt="+procAccnPmts.get(0).getPtRespAmtAsMoney());
        Assert.assertEquals(procAccnPmts.get(0).getProcId(), eobSvcPmts.get(0).getProcCd(), "Proc-level AccnPmt proc code should match EOB, accnId="+procAccnPmts.get(0).getAccnId()+", procId="+procAccnPmts.get(0).getProcId()+", eobProcId="+eobSvcPmts.get(0).getProcCd());
        Assert.assertEquals(procAccnPmts.get(0).getUnitsPd(), 1, "Proc-level AccnPmt units paid should be 1, accnId="+procAccnPmts.get(0).getAccnId()+", accnPmtUnitsPd="+procAccnPmts.get(0).getUnitsPd());
        Assert.assertFalse(procAccnPmts.get(0).getIsBulk(), "Proc-level AccnPmt bulk flag should be false, accnId="+procAccnPmts.get(0).getAccnId());
        Assert.assertFalse(procAccnPmts.get(0).getIsPosted(), "Proc-level AccnPmt should not be posted, accnId="+procAccnPmts.get(0).getAccnId());

        List<AccnAdj> accnAdjs = rpmDao.getAccnAdjByDepId(null, dep.getDepId());
        Assert.assertEquals(accnAdjs.size(), 1, "Expected 1 AccnAdj record, depId="+dep.getDepId()+", accnAdjCnt="+accnAdjs.size());
        Assert.assertEquals(accnAdjs.get(0).getAccnId(), accnIds.get(0),"Expected AccnAdj accession Id to match, depId="+dep.getDepId()+", accnAdjAccnId="+accnAdjs.get(0).getAccnId()+", expectedAccnId="+accnIds.get(0));
        Assert.assertEquals(accnAdjs.get(0).getAccnProcSeqId(), procAccnPmts.get(0).getAccnProcSeqId(),"Expected AccnAdj proc seq Id to match, depId="+dep.getDepId()+", accnAdjProcSeqId="+accnAdjs.get(0).getAccnProcSeqId()+", expectedAccnProcSeqId="+procAccnPmts.get(0).getAccnProcSeqId());
        Assert.assertEquals(accnAdjs.get(0).getProcId(), procAccnPmts.get(0).getProcId(),"Expected AccnAdj proc Id to match, depId="+dep.getDepId()+", accnAdjProcId="+accnAdjs.get(0).getProcId()+", expectedAccnProcId="+procAccnPmts.get(0).getProcId());
        Assert.assertEquals(accnAdjs.get(0).getAdjCdId(), PmtMap.ADJ_CD_SYSTEM,"Expected AccnAdj adjustment code to be CONALL (2), depId="+dep.getDepId()+", adjCd="+accnAdjs.get(0).getAdjCdId());
        Assert.assertEquals(accnAdjs.get(0).getUserId(), USER_ID_RECONCILIATION_ENGINE,"Expected AccnAdj user ID to match, depId="+dep.getDepId()+", accnAdjUserId="+accnAdjs.get(0).getUserId()+", expectedUserId="+USER_ID_RECONCILIATION_ENGINE);
        Assert.assertTrue(DateUtils.isSameDay(accnAdjs.get(0).getAdjDt(), eftEffectiveDt), "Expected AccnAdj adjustment date did not match, accnId="+accnAdjs.get(0).getAccnId()+", adjDt="+accnAdjs.get(0).getAdjDt()+", eftEffectiveDt="+eftEffectiveDt);
        Assert.assertEquals(accnAdjs.get(0).getBilAdjAmtAsMoney(), expectedConallAmt, "AccnAdj conall amount should match expected conall amount, accnId="+accnAdjs.get(0).getAccnId()+", adjAmt="+accnAdjs.get(0).getBilAdjAmtAsMoney()+", expectedConallAmt="+expectedConallAmt);
        Assert.assertFalse(accnAdjs.get(0).getIsBulk(), "AccnAdj bulk flag should be false, accnId="+accnAdjs.get(0).getAccnId());
        Assert.assertFalse(accnAdjs.get(0).getIsPosted(), "AccnAdj should not be posted, accnId="+accnAdjs.get(0).getAccnId());
        Assert.assertEquals(accnAdjs.get(0).getPyrId(), eobClaims.get(0).getPyrId(), "Expected AccnAdj payor ID did not match, accnId="+accnAdjs.get(0).getAccnId()+", accnAdjPyrId="+accnAdjs.get(0).getPyrId()+", expectedPyrId="+eobClaims.get(0).getPyrId());
        Assert.assertEquals(accnAdjs.get(0).getAdjPyrPrio(), eobClaims.get(0).getPyrPrio(), "Expected AccnAdj payor prio did not match, accnId="+accnAdjs.get(0).getAccnId()+", accnPmtPyrPrio="+accnAdjs.get(0).getAdjPyrPrio()+", expectedPyrPrio="+eobClaims.get(0).getPyrPrio());

    }

    @Test(priority = 1, description = "Sequestration amount is used in determining allowed amount and negative adjustments")
    @Parameters({"accnId", "internalCtrlId"})
    public void testSequestration(String accnId, String internalCtrlId) throws Exception
    {
        logger.info("Running testSequestration");
        logger.info("Make sure claim is ready to be processed by Reconciliation Engine");
        List<EobClaim>  eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        Assert.assertEquals(eobClaims.size(), 1);
        EobClaim eobClaim = eobClaims.get(0);

        logger.info("Reset Eob Stage for a claim ="+eobClaim);
        resetEobStageForClaim(accnId, internalCtrlId);
        // Assert.assertTrue(eobClaim.getReconcileStageId() == 0);

        logger.info("Wait for Reconciliation Engine to process the claim");
        int reconcStageId = waitForReconciliationEngine(eobClaim, QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(reconcStageId > 0);

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_claim.reconcile_stage_id = 999");
        eobClaims = rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId);
        eobClaim = eobClaims.get(0);

        logger.info("Verify Reconciliation Engine created accnPmt and accnAdj");

        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(null, accnId);
        Assert.assertEquals(accnProcs.size(), 1, "Expected 1 accession proc, accnId="+accnId+", accnProcCnt="+accnProcs.size());
        List<AccnPmt> accnPmts = new ArrayList<>();
        for (AccnPmt accnPmt : accessionDao.getAccnPmtByAccnIdPyrId(accnId, eobClaim.getPyrId()))
        {
            if (accnPmt.getAccnProcSeqId() == accnProcs.get(0).getAccnProcSeqId())
            {
                accnPmts.add(accnPmt);
            }
        }
        Assert.assertEquals(accnPmts.size(), 1, "Expected 1 AccnPmt for proc, accnId="+accnId+", accnProcSeqId="+accnProcs.get(0).getAccnProcSeqId()+", accnPmtCnt="+accnPmts.size());
        Assert.assertEquals(accnPmts.get(0).getSequestAmtAsMoney(), new Money(50), "Sequestration amount is not correct, accnId="+accnId);
        Assert.assertEquals(accnPmts.get(0).getAllowAmtAsMoney(), new Money(150), "Allowed amount does not have expected value, accnId="+accnId);

        List<AccnAdj> sequestAdjs = new ArrayList<>();
        List<AccnAdj> conallAdjs = new ArrayList<>();
        for (AccnAdj accnAdj : accessionDao.getAccnAdjByAccnId(accnId))
        {
            if (accnAdj.getAccnProcSeqId() == accnProcs.get(0).getAccnProcSeqId() && accnAdj.getPmtSeq() == accnPmts.get(0).getPmtSeq())
            {
                switch (accnAdj.getAdjCdId())
                {
                    case ADJ_CD_SEQUEST:
                        sequestAdjs.add(accnAdj);
                        break;
                    case ADJ_CD_CONALL:
                        conallAdjs.add(accnAdj);
                        break;
                }
            }
        }
        Assert.assertEquals(sequestAdjs.size(), 1, "Expected 1 SEQUEST adj for proc, accnId="+accnId+", accnProcSeqId="+accnProcs.get(0).getAccnProcSeqId()+", sequestAdjCnt="+sequestAdjs.size());
        Assert.assertEquals(sequestAdjs.get(0).getBilAdjAmtAsMoney(), new Money(-50), "SEQUEST bill adjustment is not correct, accnId="+accnId);
        Assert.assertEquals(sequestAdjs.get(0).getExpAdjAmtAsMoney(), new Money(-50), "SEQUEST expect adjustment is not correct, accnId="+accnId);
        Assert.assertEquals(sequestAdjs.get(0).getPyrId(), eobClaim.getPyrId(), "SEQUEST adjustment payor ID is not correct, accnId="+accnId);
        Assert.assertEquals(sequestAdjs.get(0).getAdjPyrPrio(), eobClaim.getPyrPrio(), "SEQUEST adjustment payor prio is not correct, accnId="+accnId);
        Assert.assertTrue(sequestAdjs.get(0).getIsSystemAdj(), "SEQUEST adjustment system adjustment flag is not correct, accnId="+accnId);

        Assert.assertEquals(conallAdjs.size(), 1, "Expected 1 CONALL adj for proc, accnId="+accnId+", accnProcSeqId="+accnProcs.get(0).getAccnProcSeqId()+", conallAdjCnt="+conallAdjs.size());
        Assert.assertEquals(conallAdjs.get(0).getBilAdjAmtAsMoney(), new Money(-100), "CONALL bill adjustment is not correct, accnId="+accnId);
        Assert.assertEquals(conallAdjs.get(0).getExpAdjAmtAsMoney(), new Money(-100), "CONALL expect adjustment is not correct, accnId="+accnId);
        Assert.assertEquals(conallAdjs.get(0).getPyrId(), eobClaim.getPyrId(), "CONALL adjustment payor ID is not correct, accnId="+accnId);
        Assert.assertEquals(conallAdjs.get(0).getAdjPyrPrio(), eobClaim.getPyrPrio(), "CONALL adjustment payor prio is not correct, accnId="+accnId);
        Assert.assertTrue(conallAdjs.get(0).getIsSystemAdj(), "CONALL adjustment system adjustment flag is not correct, accnId="+accnId);

        logger.info("Verify Reconciliation Engine processed the claim - checking eob_financial_trans.reconcile_stage_id = 999");
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        Assert.assertEquals(eobFinancialTrans.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());
        Assert.assertEquals(eobClaim.getReconcileStageId(), ReconciliationStage.RECONCILIATION_COMPLETE.stageId());

        logger.info("Verify Reconciliation Engine processed the claim - records in dep, dep_batch, dep_batch_seq are created");
        Dep dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());
    }

    protected List<String> deleteDepData(int eobFinancialTransId) throws XifinDataNotFoundException, XifinDataAccessException
    {
        List<String> accnIds = new ArrayList<>();
        try
        {
            Dep dep = rpmDao.getDepByEobFinancialTransId(null, eobFinancialTransId);
            logger.info("Deleting deposit data, depId="+dep.getDepId());
            rpmDao.deleteAccnPmtByDepId(null, dep.getDepId());
            rpmDao.deleteAccnAdjByDepId(null, dep.getDepId());
            rpmDao.deleteAccnPyrErrByDepId(null, dep.getDepId());
            rpmDao.deleteAccnProcErrByDepId(null, dep.getDepId());
            rpmDao.deleteDepBatchSeqByDepId(null, dep.getDepId());
            rpmDao.deleteDepBatchByDepId(null, dep.getDepId());
            rpmDao.deleteDepByDepId(dep.getDepId());
        }
        catch (XifinDataNotFoundException e)
        {
            logger.info("No deposit found, eobFinancialTransId="+eobFinancialTransId);
        }

        for (EobClaim eobClaim : rpmDao.getEobClaimsByEobFinancialTransId(eobFinancialTransId))
        {
            if (StringUtils.isNotBlank(eobClaim.getAccnId()))
            {
                try
                {
                    Accn accn = accessionDao.getAccn(eobClaim.getAccnId());
                    accnIds.add(accn.getAccnId());
                    if (accn.getStaId() == AccnStatusMap.ACCN_STATUS_PMTPOST)
                    {
                        logger.info("Clearing payment posting status, accnId="+accn.getAccnId());
                        accn.setStaId(AccnStatusMap.ACCN_STATUS_PRICED);
                        accessionDao.setAccn(accn);
                    }
                }
                catch (XifinDataNotFoundException e)
                {
                    logger.info("No accession found, accnId="+eobClaim.getAccnId());
                }
            }
        }
        return accnIds;
    }


    protected void resetEob(int eobFinancialTransId) throws XifinDataNotFoundException, XifinDataAccessException
    {
        logger.info("Resetting EOB transaction, eobFinancialTransId="+eobFinancialTransId);
        for (EobClaim eobClaim : rpmDao.getEobClaimsByEobFinancialTransId(eobFinancialTransId))
        {
            eobClaim.setPyrId(0);
            eobClaim.setPyrPrio(0);
            eobClaim.setIsExcluded(false);
            eobClaim.setIsDiscrepancy(false);
            eobClaim.setReconcileStageId(ReconciliationStage.NO_RECONCILIATION_ATTEMPTED.stageId());
            rpmDao.setEobClaim(testDb, eobClaim);
        }
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobFinancialTransId);
        eobFinancialTrans.setReconcileStageId(ReconciliationStage.NO_RECONCILIATION_ATTEMPTED.stageId());
        rpmDao.setEobFinancialTrans(null, eobFinancialTrans);
    }


    protected void cleanUpClaim(String accnId, String internalCtrlId) throws Exception
    {
        Set<Integer> eobFinancialTransIds = new HashSet<>();
        if (StringUtils.isNotEmpty(internalCtrlId))
        {
            for (EobClaim eobClaim : rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId))
            {
                eobClaim.setPyrId(0);
                eobClaim.setPyrPrio(0);
                eobClaim.setIsExcluded(false);
                eobClaim.setIsDiscrepancy(false);
                eobClaim.setErrorNote(null);
                rpmDao.setEobClaim(testDb, eobClaim);
                eobFinancialTransIds.add(eobClaim.getEobFinancialTransId());
            }
        }
        for (Integer eobFinancialTransId : eobFinancialTransIds)
        {
            EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobFinancialTransId);
            if (eobFinancialTrans.getReconcileStageId() != ReconciliationStage.NO_RECONCILIATION_ATTEMPTED.stageId())
            {
                //eobFinancialTrans.setReconcileStageId(ReconciliationStage.NO_RECONCILIATION_ATTEMPTED.stageId());
                //rpmDao.setEobFinancialTrans(testDb, eobFinancialTrans);
                logger.info("Updated eob_financial_trans record for eobFinancialTransSeqId = " + eobFinancialTrans.getSeqId());
                Dep dep = null;
                try
                {
                    dep = rpmDao.getDepByEobFinancialTransId(testDb, eobFinancialTrans.getSeqId());
                }
                catch (XifinDataNotFoundException e)
                {
                    logger.debug("message=Deposit not found, eobFinancialTransId= " + eobFinancialTransId);
                }
                if (dep != null)

                {
                    dep.setEobFinancialTransId(0);
                    rpmDao.setDep(testDb, dep);

                    rpmDao.deleteDepBatchSeqByDepId(testDb, dep.getDepId());
                    logger.info("Deleted Dep_batch_seq record for DepId = " + dep.getDepId());

                    rpmDao.deleteDepBatchByDepId(testDb, dep.getDepId());
                    logger.info("Deleted Dep_batch record for DepId = " + dep.getDepId());

                    rpmDao.deleteDepByDepId(dep.getDepId());
                    logger.info("Deleted Dep record, depId=" + dep.getDepId());

                    rpmDao.deleteAccnPmtByDepId(testDb, dep.getDepId());
                    logger.info("Deleted Accn_pmt record for DepId = " + dep.getDepId());

                    rpmDao.deleteAccnAdjByDepId(testDb, dep.getDepId());
                    logger.info("Deleted Accn_adj record for DepId = " + dep.getDepId());

                    rpmDao.deleteAccnProcErrByDepId(testDb, dep.getDepId());
                    logger.info("Deleted Accn_proc_err record for DepId = " + dep.getDepId());

                    rpmDao.deleteAccnPyrErrByDepId(testDb, dep.getDepId());
                    logger.info("Delete Accn_pyr_err record for DepId = " + dep.getDepId());
                }
            }
        }

        if (StringUtils.isNotEmpty(accnId))
        {
            accessionDao.deleteAccnPyrDelByAccnId(accnId);
            logger.info("Delete Accn_pyr_del record for AccnId = " + accnId);

            Accn accn = accessionDao.getAccn(accnId);
            if (accn.getStaId() == AccnStatusMap.ACCN_STATUS_PMTPOST)
            {
                accn.setStaId(AccnStatusMap.ACCN_STATUS_PRICED);
                accessionDao.setAccn(accn);
            }
        }
    }

    protected int waitForReconciliationEngine(EobClaim eobClaim, long maxTime)
            throws InterruptedException, XifinDataAccessException, XifinDataNotFoundException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId());
        int reconcileStageId = eobFinancialTrans.getReconcileStageId();
        while (reconcileStageId != ReconciliationStage.RECONCILIATION_COMPLETE.stageId() && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for Reconciliation Engine to process the claim file, accnId=" + eobClaim.getAccnId() + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            reconcileStageId = rpmDao.getEobFinancialTrans(testDb, eobClaim.getEobFinancialTransId()).getReconcileStageId();
        }
        return reconcileStageId;
    }

    private void setOldSubIdToAccn(AccessionDetail accessionDetail, String accnId, String oldSubId) throws Exception
    {
        accessionDetail.primaryPyrSubsIdInput().clear();
        accessionDetail.primaryPyrSubsIdInput().sendKeys(oldSubId + Keys.TAB);
        accessionDetail.accnIdText().sendKeys(Keys.ALT+"S");
        Boolean isElementPresent = driver.findElement(By.id("dlgPatientDemoUpdate")).isDisplayed();
        if (isElementPresent)
        {
            accessionDetail.clickOKBtnInPtDemoUpdate();
        }
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
    }

    protected void resetEobStageForClaim(String accnId, String internalCtrlId) throws Exception
    {
        Set<Integer> eobFinancialTransIds = new HashSet<>();
        if (StringUtils.isNotEmpty(internalCtrlId))
        {
            for (EobClaim eobClaim : rpmDao.getEobClaimsByInternalCtrlId(testDb, internalCtrlId))
            {
                eobClaim.setReconcileStageId(ReconciliationStage.NO_RECONCILIATION_ATTEMPTED.stageId());
                rpmDao.setEobClaim(testDb, eobClaim);
                eobFinancialTransIds.add(eobClaim.getEobFinancialTransId());
                for (EobSvcPmt esp : rpmDao.getEobSvcPmtsByEobClaimId(eobClaim.getSeqId()))
                {
                    esp.setAccnProcSeqId(0);
                    rpmDao.set(esp);
                }
            }
        }
        for (Integer eobFinancialTransId : eobFinancialTransIds)
        {

            EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTrans(testDb, eobFinancialTransId);
            if (eobFinancialTrans.getReconcileStageId() != ReconciliationStage.NO_RECONCILIATION_ATTEMPTED.stageId())
            {
                eobFinancialTrans.setReconcileStageId(ReconciliationStage.NO_RECONCILIATION_ATTEMPTED.stageId());
                rpmDao.setEobFinancialTrans(testDb, eobFinancialTrans);
                logger.info("Updated eob_financial_trans record for eobFinancialTransSeqId = " + eobFinancialTrans.getSeqId());
            }
        }
    }

    private boolean isOutOfPricingQueue(String accnId, long maxTime) throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = accessionDao.isInPricingQueue(accnId);
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            isInQueue = accessionDao.isInPricingQueue(accnId);
        }
        return !isInQueue;
    }

    protected boolean isOutOfEligibilityQueue(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = accessionDao.isInEligibilityQueue(accnId);
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            isInQueue = accessionDao.isInEligibilityQueue(accnId);
        }
        return !isInQueue;
    }

    /**
     * For a particular accession and payor, delete all the QAS entries except those that match the docSeqId list.
     *
     * @param accnId              Accession ID
     * @param pyrId               Payor ID
     * @param icns Set of ICNs
     * @throws Exception Exception
     */
    protected void cleanQASForPyrOnAccnExceptICNs(String accnId, int pyrId, Set<String> icns) throws Exception {
        logger.info("Clearing QAS for Pyr on accn, accnId=" + accnId + ", pyrId=" + pyrId);
        List<QAccnSubm> qasListToDelete = new ArrayList<>();
        for (QAccnSubm accnSubm : rpmDao.getQAccnSubm(null, accnId))
        {
            if ((pyrId == 0 || accnSubm.getPyrId() == pyrId) && !icns.contains(accnSubm.getAccnId()+'Z'+accnSubm.getClaimIdSuffix())) {
                qasListToDelete.add(accnSubm);
            }
        }
        submissionDao.deleteSubmissions(qasListToDelete);
    }

    private Money getEobAdjAmt(List<EobSvcAdj> eobSvcAdjs, String eobCodeAdjGrp, String eobAdjReasonCd)
    {
        Money adjAmt = new Money(0);
        for (EobSvcAdj eobSvcAdj : eobSvcAdjs)
        {
            if (StringUtils.equalsIgnoreCase(eobSvcAdj.getEobCodeAdjGrp(), eobCodeAdjGrp) && StringUtils.equalsIgnoreCase(eobSvcAdj.getReasonCd(), eobAdjReasonCd))
            {
                adjAmt.add(new Money(eobSvcAdj.getAmtStr()));
            }
        }
        return adjAmt;
    }
}
