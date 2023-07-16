package com.pfEngines.tests;

import com.mbasys.common.utility.Money;
import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnCntct.AccnCntct;
import com.mbasys.mars.ejb.entity.accnEligHist.AccnEligHist;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.ptEstimation.PtEstimation;
import com.mbasys.mars.ejb.entity.ptEstimationBenefit.PtEstimationBenefit;
import com.mbasys.mars.ejb.entity.ptEstimationProc.PtEstimationProc;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrElig.PyrElig;
import com.mbasys.mars.ejb.entity.qElig.QElig;
import com.mbasys.mars.ejb.entity.qPtEstimation.QPtEstimation;
import com.mbasys.mars.ejb.entity.qValidateAccn.QValidateAccn;
import com.mbasys.mars.eligibility.EligMap;
import com.mbasys.mars.errorProcessing.EpConstants;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.DbErrorMap;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.mbasys.mars.utility.cache.CacheMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.orderProcessing.AccnTestUpdate;
import com.overall.menu.MenuNavigation;
import com.overall.utils.EligibilityConstants;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.utils.ClearCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

public class PatientEstimationEngineTest extends EligibilityBaseTest
{
    private MenuNavigation navigation;
    private AccessionDetail accessionDetail;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            // Disable excess Selenium logging
            java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(java.util.logging.Level.OFF);
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            logIntoSso(ssoUsername, ssoPassword);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "True", "1");
            updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE, "True", "1");
            updateSystemSetting(SystemSettingMap.SS_CREATE_ELIGPE_ERRORS, "True", "1");
            updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_CALCULATE_SELF_PAY, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_CALCULATE_MAXIMUM_PATIENT_RESPONSIBILITY, "False", "0");
            ClearCacheUtil clearCacheUtil = new ClearCacheUtil(orgAlias, config.getProperty(PropertyMap.XIFINADMINPORTAL_URL));
            clearCacheUtil.clearCache(CacheMap.SYSTEM_SETTINGS);
            logger.info("Cleared cache");
            driver.close();
        }
        catch (SkipException e)
        {
            logger.warn("Skipped exception thrown during BeforeSuite action", e);
        }
        catch (Exception e)
        {
            throw new SkipException("Error running BeforeSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method)
    {
        try
        {
            logIntoSso(parameters.getSsoUsername(), parameters.getSsoPassword());
            navigation = new MenuNavigation(driver, config);
            navigation.navigateToAccnDetailPage();
            logger.info("Passed before method");
        }
        catch (SkipException e)
        {
            logger.error("Failed before method", e);
            throw e;
        }
        catch (Exception e)
        {
            logger.error("Failed before method", e);
            throw new SkipException("Error running BeforeMethod", e);
        }
    }

    @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running AfterSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            logIntoSso(ssoUsername, ssoPassword);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_CREATE_ELIGPE_ERRORS, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_CALCULATE_SELF_PAY, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_CALCULATE_MAXIMUM_PATIENT_RESPONSIBILITY, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_ALLOW_EP_FOR_ACCNS_IN_MANUAL_PRICING_RELEASE, "False", "0");
            ClearCacheUtil clearCacheUtil = new ClearCacheUtil(orgAlias, config.getProperty(PropertyMap.XIFINADMINPORTAL_URL));
            clearCacheUtil.clearCache(CacheMap.SYSTEM_SETTINGS);
        }
        catch (SkipException e)
        {
            logger.warn("Skipped exception thrown during AfterSuite", e);
        }
        catch (Exception e)
        {
            throw new SkipException("Error running AfterSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @Test(priority = 1, description = "Patient Estimation Engine - Payor date table in-network for DOS, use in-network coverage calculation")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testRpmNew992298(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: RpmNew-992298, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, accnCntct, true, true, true);
    }

    @Test(priority = 1, description = "Patient Estimation Engine - Payor date table in-network for DOS, use out-of-network coverage calculation")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testRpmNew992299(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: RpmNew-992299, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, accnCntct, true, true, false);
    }

    @Test(priority = 1, description = "Patient Estimation Engine - Payor date entry missing, add error")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testRpmNew992300(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: RpmNew-992301, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn_ pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, accnCntct, false, false, false);
    }

    @Test(priority = 1, description = "Patient Estimation Engine - Payor date table in-network for DOS, elig only has out-of-network, no benefit record")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testRpmNew992302(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: RpmNew-992302, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, accnCntct, true, false, false);
    }

    @Test(priority = 1, description = "Patient Estimation Engine - Re-run Eligibility check with new primary pyr (translation)")
    @Parameters({"accnId", "pyrAbbrv", "eligPyrAbbrv", "ptEstPyrAbbrv", "accnCntct"})
    public void testRpmNew992366(String accnId, String pyrAbbrv, String eligPyrAbbrv, String ptEstPyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: RpmNew-992366, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        patientEstimationTranslationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, accnCntct, true, true, false, eligPyrAbbrv, ptEstPyrAbbrv);
    }

    @Test(priority = 1, description = "Patient Estimation Engine - Accession remains in estimation queue when payor translates but new pyr elig not setup")
    @Parameters({"accnId", "pyrAbbrv", "eligPyrAbbrv", "ptEstPyrAbbrv", "accnCntct"})
    public void testRpmNew992367(String accnId, String pyrAbbrv, String eligPyrAbbrv, String ptEstPyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: RpmNew-992367, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        patientEstimationTranslationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, DbErrorMap.ELIGPE, false, accnCntct, false, false, true, eligPyrAbbrv, ptEstPyrAbbrv);
    }

    @Test(priority = 1, description = "Patient Estimation Engine - Patient estimation is performed for Q_PT_ESTIMATION")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testRpmNew992368(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: RpmNew-992368, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, accnCntct, true, true, true);
    }

    @Test(priority = 1, description = "Patient Estimation Engine - ELIGPE Error Is Added")
    @Parameters({"accnId", "pyrAbbrv"})
    public void testELIGPEErrorIsAdded(String accnId, String pyrAbbrv) throws Exception
    {
        logger.info("Starting Test Case: testELIGPEErrorIsAdded, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Accn accn = accessionDao.getAccn(accnId);
        ErrCd ec = rpmDao.getErrCd(null, DbErrorMap.ELIGPE, EpConstants.DNL_TBL_ID_UNBILL, accn.getDos());

        deleteErrorsAndRevalidate(accn, new HashSet(Arrays.asList(DbErrorMap.ELIGPE)));

        logger.info("Verifying ELIGPE error is added by Validation Engine, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        List<AccnPyrErr> eligpeErrs = new ArrayList<>();
        for (AccnPyrErr ape : accessionDao.getAccnPyrErrsByAccnId(accnId, false, false))
        {
            if (ape.getErrCd() == ec.getErrCd() && ape.getFixDt() == null && ape.getPyrPrio() == 1)
            {
                eligpeErrs.add(ape);
            }
        }
        Assert.assertEquals(eligpeErrs.size(), 1, "Expected 1 unfixed ELIGPE error");

        logger.info("Fixing ELIGPE error, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        eligpeErrs.get(0).setFixDt(TODAYS_DT);
        eligpeErrs.get(0).setFixUserId("xqatester");
        accessionDao.setAccnPyrErr(eligpeErrs.get(0));

        logger.info("Verifying fixed ELIGPE error is not re-opened by Validation Engine, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        QValidateAccn qValidateAccn = rpmDao.getQValidateAccnByAccnId(null, accnId);
        if (qValidateAccn == null)
        {
            qValidateAccn = new QValidateAccn();
            qValidateAccn.setAccnId(accnId);
        }
        qValidateAccn.setIsErr(false);
        qValidateAccn.setPriority(3);
        qValidateAccn.setValidateTypId(16);
        accessionDao.setQValidateAccn(qValidateAccn);
        Assert.assertTrue(isOutOfValidationQueue(accnId, QUEUE_WAIT_TIME_MS));

        eligpeErrs.clear();
        for (AccnPyrErr ape : accessionDao.getAccnPyrErrsByAccnId(accnId, false, false))
        {
            if (ape.getErrCd() == ec.getErrCd() && ape.getFixDt() == null && ape.getPyrPrio() == 1)
            {
                eligpeErrs.add(ape);
            }
        }
        Assert.assertEquals(eligpeErrs.size(), 0, "Expected 0 unfixed ELIGPE error");
    }

    @Test(priority = 1, description = "Patient Estimation Engine - ELIGPE Error Is Not Added For Patient Payor")
    @Parameters({"accnId", "pyrAbbrv"})
    public void testELIGPEErrorNotAddedForPatientPayor(String accnId, String pyrAbbrv) throws Exception
    {
        logger.info("Starting Test Case: testELIGPEErrorNotAddedForPatientPayor, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_NOT_CHECKED, null, false, null, false, false, false);
    }

    @Test(priority = 1, description = "Patient Estimation Engine - ELIGPE Error Is Not Added For UNK Payor")
    @Parameters({"accnId", "pyrAbbrv"})
    public void testELIGPEErrorNotAddedForUNKPayor(String accnId, String pyrAbbrv) throws Exception
    {
        logger.info("Starting Test Case: testELIGPEErrorNotAddedForUNKPayor, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_NOT_CHECKED, null, false, null, false, false, false, new HashSet<Integer>(Arrays.asList(AccnStatusMap.ACCN_STATUS_EP_UNPRICEABLE)));
    }

    @Test(priority = 1, description = "Patient Estimation Engine - ELIGPE error is not added for QELIG accession when eligibility check is Ineligible")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testELIGPEErrorNotAddedWhenEligCheckIsIneligible(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: testELIGPEErrorNotAddedWhenEligCheckIsIneligible, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_INELIGIBLE, DbErrorMap.INELIGIBLE, true, accnCntct, false, false, false, Set.of(AccnStatusMap.ACCN_STATUS_REPORTED, AccnStatusMap.ACCN_STATUS_PRICED, AccnStatusMap.ACCN_STATUS_ZBAL));
    }

    @Test(priority = 1, description = "Coinsurance rate greater than 100% is not applied")
    @Parameters({"accnId", "pyrAbbrv"})
    public void testCoinsuranceRateOver100Pct(String accnId, String pyrAbbrv) throws Exception
    {
        logger.info("Starting Test Case: testCoinsuranceRateOver100Pct, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        deletePtEstimationData(accnId);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, null, true, false, false);
        logger.info("Verifying coinsurnce was not applied, accnId=" + accnId);
        List<PtEstimation> ptEstimations = accessionDao.getPtEstimationsByAccnId(accnId);
        Assert.assertEquals(ptEstimations.size(), 1);
        Assert.assertEquals(ptEstimations.get(0).getPtRespAmtAsMoney(), new Money(0));
        Money appliedPrAmt = new Money(0);
        for (PtEstimationProc ptEstimationProc : accessionDao.getPtEstimationProcsByPtEstimationId(Arrays.asList(ptEstimations.get(0).getSeqId())))
        {
            for (PtEstimationBenefit ptEstimationBenefit : accessionDao.getPtEstimationBenefitByPtEstimationProcId(Arrays.asList(ptEstimationProc.getSeqId())))
            {
                appliedPrAmt.add(ptEstimationBenefit.getAppliedAmtAsMoney());
            }
        }
        Assert.assertEquals(appliedPrAmt, new Money(0));
    }

    @Test(priority = 1, description = "Accession Is Validated After Estimation")
    @Parameters({"accnId", "ptDob", "pyrAbbrv", "accnPyrErr", "errNote"})
    public void testAccnIsValidatedAfterEstimation(String accnId, String ptDob, String pyrAbbrv, String accnPyrErr, String errNote) throws Exception
    {
        logger.info("Starting Test Case: testAccnIsValidatedAfterEstimation, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Accn accn = accessionDao.getAccn(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1, "Expected payors do not match, accnId="+accnId+", expectedPyrSize=1, actualPyrSize="+accnPyrs.size());
        Pyr pyr = rpmDao.getPyrByPyrId(null, accnPyrs.get(0).getPyrId());
        Assert.assertEquals(pyr.getPyrAbbrv(), pyrAbbrv, "Expected payors do not match, accnId="+accnId+", expectedPyrAbbrv="+pyrAbbrv+", actualPyrAbbrv="+pyr.getPyrAbbrv());
        deletePtEstimationData(accnId);
        try
        {
            ErrCd ec = rpmDao.getErrCd(null, accnPyrErr, EpConstants.DNL_TBL_ID_UNBILL, accn.getDos());
            // Enable EP in Manual Price Release (accession is in QFRPending)
            updateSystemSetting(SystemSettingMap.SS_ALLOW_EP_FOR_ACCNS_IN_MANUAL_PRICING_RELEASE, "True", "1");
            new ClearCacheUtil(config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.XIFINADMINPORTAL_URL)).clearCache(CacheMap.SYSTEM_SETTINGS);
            // Delete the error that will be added after the Estimation generates, and revalidate
            deleteErrorsAndRevalidate(accn, new HashSet<>(Arrays.asList(accnPyrErr)));
            logger.info("Verifying accn status is Pending, accnId=" + accnId);
            accn = accessionDao.getAccn(accnId);
            Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_PENDING, "Expected Pending status, accnId="+accnId+", actualStaId="+accn.getStaId()+", expectedStaId="+AccnStatusMap.ACCN_STATUS_PENDING);
            logger.info("Queuing Patient Estimation, accnId=" + accnId);
            QPtEstimation qPtEstimation = accessionDao.getQPtEstimation(accnId);
            if (qPtEstimation == null)
            {
                qPtEstimation = new QPtEstimation();
                qPtEstimation.setAccnId(accnId);
                qPtEstimation.setUserId("xqatester");
                qPtEstimation.setInDt(new Date(System.currentTimeMillis()));
            }
            qPtEstimation.setIsErr(false);
            qPtEstimation.setNote(null);
            databaseSequenceDao.setValueObject(qPtEstimation);
            Assert.assertTrue(isOutOfQPtEstimationQueue(accnId, QUEUE_WAIT_TIME), "Accession was not moved from Patient Estimation Queue, accnId="+accnId);
            Assert.assertTrue(isOutOfValidationQueue(accnId, QUEUE_WAIT_TIME), "Accession was not moved from Validation Queue, accnId="+accnId);
            List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
            Assert.assertEquals(accnPyrErrs.size(), 1, "Expected error was not added, accnId="+accnId+", expectedErrCnt=1, actualErrCnt="+accnPyrErrs.size());
            Assert.assertEquals(accnPyrErrs.get(0).getErrCd(), ec.getErrCd(), "Expected error was not added, accnId="+accnId+", expectedErrCd="+ec.getErrCd()+", actualErrCd="+accnPyrErrs.get(0).getErrCd());
            Assert.assertEquals(accnPyrErrs.get(0).getNote(), errNote, "Expected error note, accnId="+accnId+", expectedErrNote="+errNote+", actualErrNote="+accnPyrErrs.get(0).getNote());
        }
        finally
        {
            updateSystemSetting(SystemSettingMap.SS_ALLOW_EP_FOR_ACCNS_IN_MANUAL_PRICING_RELEASE, "False", "0");
            new ClearCacheUtil(config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.XIFINADMINPORTAL_URL)).clearCache(CacheMap.SYSTEM_SETTINGS);
        }
    }

    @Test(priority = 1, description = "Apply coinsurance for service type 30")
    @Parameters({"accnId", "pyrAbbrv", "coinsuranceRate"})
    public void applyCoinsuranceForServiceType30(String accnId, String pyrAbbrv, String coinsuranceRate) throws Exception
    {
        logger.info("Starting Test Case: applyCoinsuranceForServiceType30, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        deletePtEstimationData(accnId);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, null, true, true, true);

        List<PtEstimation> ptEstimations = accessionDao.getPtEstimationsByAccnId(accnId);
        Assert.assertEquals(ptEstimations.size(), 1, "Expected 1 Patient Estimation record, accnId="+accnId+", actualCount="+ptEstimations.size());
        List<PtEstimationProc> ptEstimationProcs = accessionDao.getPtEstimationProcsByPtEstimationId(Collections.singletonList(ptEstimations.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationProcs.size(), 1, "Expected 1 Patient Estimation Proc record, accnId="+accnId+", actualCount="+ptEstimationProcs.size());
        List<PtEstimationBenefit> ptEstimationBenefits = accessionDao.getPtEstimationBenefitByPtEstimationProcId(Collections.singletonList(ptEstimationProcs.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationBenefits.size(), 1, "Expected 1 Patient Estimation Benefit record, accnId="+accnId+", actualCount="+ptEstimationBenefits.size());

        logger.info("Verifying applied coinsurance data, accnId=" + accnId);
        float expectedAppliedCoinsuranceRate = Float.parseFloat(coinsuranceRate);
        Assert.assertEquals(ptEstimationBenefits.get(0).getBenefitTypId(), MiscMap.PT_ESTIMATION_BENEFIT_TYP_COINSURANCE, "Expected applied benefit type to be Coinsurance, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getSvcTyps(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode(), "Expected applied service type to be 30, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getTimePeriod(), "Visit", "Expected applied time period to be 'Visit', accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getRate(), expectedAppliedCoinsuranceRate, "Expected applied coinsurance rate does not match, accnId="+accnId);
        Assert.assertTrue(ptEstimationBenefits.get(0).getIsApplied(), "Expected coinsurance applied flag to be True, accnId="+accnId);
        Money actualAppliedCoinsuranceAmount = ptEstimationBenefits.get(0).getAppliedAmtAsMoney();

        logger.info("Verifying proc data, accnId=" + accnId);
        Assert.assertTrue(StringUtils.isNotBlank(ptEstimationProcs.get(0).getProcCd()), "Expected proc code to not be empty, accnId="+accnId);
        //Assert.assertFalse(StringUtils.contains(ptEstimationProcs.get(0).getSvcTyp(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode()), "Expected proc service type to not be 30, accnId="+accnId);
        Money expectAmount = ptEstimationProcs.get(0).getExpAmtAsMoney();
        Money expectedAppliedCoinsuranceAmount = expectAmount.multiplyBD(expectedAppliedCoinsuranceRate);
        Assert.assertEquals(actualAppliedCoinsuranceAmount, expectedAppliedCoinsuranceAmount, "Expected applied coinsurnace amount does not match, accnId="+accnId);

        logger.info("Verifying estimation data, accnId=" + accnId);
        Assert.assertTrue(DateUtils.isSameDay(ptEstimations.get(0).getEstimationDt(), new Date(System.currentTimeMillis())), "Expected estimation date to be today, accnId="+accnId);
        Assert.assertEquals(ptEstimations.get(0).getPtRespAmtAsMoney(), expectedAppliedCoinsuranceAmount, "Expected patient responsibility amount to be equal to applied coinsurnace amount, accnId="+accnId);
    }

    @Test(priority = 1, description = "Prioritize coinsurance for matching service type")
    @Parameters({"accnId", "pyrAbbrv", "coinsuranceRate"})
    public void prioritizeCoinsuranceForMatchingServiceType(String accnId, String pyrAbbrv, String coinsuranceRate) throws Exception
    {
        logger.info("Starting Test Case: applyCoinsuranceForServiceType30, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        deletePtEstimationData(accnId);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, null, true, true, true);

        List<PtEstimation> ptEstimations = accessionDao.getPtEstimationsByAccnId(accnId);
        Assert.assertEquals(ptEstimations.size(), 1, "Expected 1 Patient Estimation record, accnId="+accnId+", actualCount="+ptEstimations.size());
        List<PtEstimationProc> ptEstimationProcs = accessionDao.getPtEstimationProcsByPtEstimationId(Collections.singletonList(ptEstimations.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationProcs.size(), 1, "Expected 1 Patient Estimation Proc record, accnId="+accnId+", actualCount="+ptEstimationProcs.size());
        List<PtEstimationBenefit> ptEstimationBenefits = accessionDao.getPtEstimationBenefitByPtEstimationProcId(Collections.singletonList(ptEstimationProcs.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationBenefits.size(), 1, "Expected 1 Patient Estimation Benefit record, accnId="+accnId+", actualCount="+ptEstimationBenefits.size());

        logger.info("Verifying applied coinsurance data, accnId=" + accnId);
        float expectedAppliedCoinsuranceRate = Float.parseFloat(coinsuranceRate);
        Assert.assertEquals(ptEstimationBenefits.get(0).getBenefitTypId(), MiscMap.PT_ESTIMATION_BENEFIT_TYP_COINSURANCE, "Expected applied benefit type to be Coinsurance, accnId="+accnId);
        Assert.assertNotEquals(ptEstimationBenefits.get(0).getSvcTyps(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode(), "Expected applied service type to not be 30, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getSvcTyps(), ptEstimationProcs.get(0).getSvcTyp(), "Expected applied service type to be equal to proc service type, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getTimePeriod(), "Visit", "Expected applied time period to be 'Visit', accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getRate(), expectedAppliedCoinsuranceRate, "Expected applied coinsurance rate does not match, accnId="+accnId);
        Assert.assertTrue(ptEstimationBenefits.get(0).getIsApplied(), "Expected coinsurance applied flag to be True, accnId="+accnId);
        Money actualAppliedCoinsuranceAmount = ptEstimationBenefits.get(0).getAppliedAmtAsMoney();

        logger.info("Verifying proc data, accnId=" + accnId);
        Assert.assertTrue(StringUtils.isNotBlank(ptEstimationProcs.get(0).getProcCd()), "Expected proc code to not be empty, accnId="+accnId);
        Assert.assertFalse(StringUtils.contains(ptEstimationProcs.get(0).getSvcTyp(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode()), "Expected proc service type to not be 30, accnId="+accnId);
        Money expectAmount = ptEstimationProcs.get(0).getExpAmtAsMoney();
        Money expectedAppliedCoinsuranceAmount = expectAmount.multiplyBD(expectedAppliedCoinsuranceRate);
        Assert.assertEquals(actualAppliedCoinsuranceAmount, expectedAppliedCoinsuranceAmount, "Expected applied coinsurnace amount does not match, accnId="+accnId);

        logger.info("Verifying estimation data, accnId=" + accnId);
        Assert.assertTrue(DateUtils.isSameDay(ptEstimations.get(0).getEstimationDt(), new Date(System.currentTimeMillis())), "Expected estimation date to be today, accnId="+accnId);
        Assert.assertEquals(ptEstimations.get(0).getPtRespAmtAsMoney(), expectedAppliedCoinsuranceAmount, "Expected patient responsibility amount to be equal to applied coinsurnace amount, accnId="+accnId);
    }

    @Test(priority = 1, description = "Apply copay for service type 30")
    @Parameters({"accnId", "pyrAbbrv", "copayAmt"})
    public void applyCopayForServiceType30(String accnId, String pyrAbbrv, String copayAmt) throws Exception
    {
        logger.info("Starting Test Case: applyCopayForServiceType30, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        deletePtEstimationData(accnId);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, null, true, true, true);

        List<PtEstimation> ptEstimations = accessionDao.getPtEstimationsByAccnId(accnId);
        Assert.assertEquals(ptEstimations.size(), 1, "Expected 1 Patient Estimation record, accnId="+accnId+", actualCount="+ptEstimations.size());
        List<PtEstimationProc> ptEstimationProcs = accessionDao.getPtEstimationProcsByPtEstimationId(Collections.singletonList(ptEstimations.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationProcs.size(), 1, "Expected 1 Patient Estimation Proc record, accnId="+accnId+", actualCount="+ptEstimationProcs.size());
        List<PtEstimationBenefit> ptEstimationBenefits = accessionDao.getPtEstimationBenefitByPtEstimationProcId(Collections.singletonList(ptEstimationProcs.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationBenefits.size(), 1, "Expected 1 Patient Estimation Benefit record, accnId="+accnId+", actualCount="+ptEstimationBenefits.size());

        logger.info("Verifying applied copay data, accnId=" + accnId);
        Money expectedCopayAmt = new Money(copayAmt);
        Assert.assertEquals(ptEstimationBenefits.get(0).getBenefitTypId(), MiscMap.PT_ESTIMATION_BENEFIT_TYP_COPAYMENT, "Expected applied benefit type to be Copayment, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getSvcTyps(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode(), "Expected applied service type to be 30, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getTimePeriod(), "Visit", "Expected applied time period to be 'Visit', accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getVisitAmtAsMoney(), expectedCopayAmt, "Expected per-visit copay amt does not match, accnId="+accnId);
        Assert.assertTrue(ptEstimationBenefits.get(0).getIsApplied(), "Expected copay applied flag to be True, accnId="+accnId);
        Money actualAppliedCopayAmount = ptEstimationBenefits.get(0).getAppliedAmtAsMoney();

        logger.info("Verifying proc data, accnId=" + accnId);
        Assert.assertTrue(StringUtils.isNotBlank(ptEstimationProcs.get(0).getProcCd()), "Expected proc code to not be empty, accnId="+accnId);
        //Assert.assertFalse(StringUtils.contains(ptEstimationProcs.get(0).getSvcTyp(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode()), "Expected proc service type to not be 30, accnId="+accnId);
        Money expectAmount = ptEstimationProcs.get(0).getExpAmtAsMoney();
        Assert.assertEquals(actualAppliedCopayAmount, expectedCopayAmt, "Expected applied copay amount does not match, accnId="+accnId);

        logger.info("Verifying estimation data, accnId=" + accnId);
        Assert.assertTrue(DateUtils.isSameDay(ptEstimations.get(0).getEstimationDt(), new Date(System.currentTimeMillis())), "Expected estimation date to be today, accnId="+accnId);
        Assert.assertEquals(ptEstimations.get(0).getPtRespAmtAsMoney(), expectedCopayAmt, "Expected patient responsibility amount to be equal to applied copay amount, accnId="+accnId);
    }

    @Test(priority = 1, description = "Prioritize copay for matching service type")
    @Parameters({"accnId", "pyrAbbrv", "copayAmt"})
    public void prioritizeCopayForMatchingServiceType(String accnId, String pyrAbbrv, String copayAmt) throws Exception
    {
        logger.info("Starting Test Case: applyCopayForServiceType30, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        deletePtEstimationData(accnId);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, null, true, true, true);

        List<PtEstimation> ptEstimations = accessionDao.getPtEstimationsByAccnId(accnId);
        Assert.assertEquals(ptEstimations.size(), 1, "Expected 1 Patient Estimation record, accnId="+accnId+", actualCount="+ptEstimations.size());
        List<PtEstimationProc> ptEstimationProcs = accessionDao.getPtEstimationProcsByPtEstimationId(Collections.singletonList(ptEstimations.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationProcs.size(), 1, "Expected 1 Patient Estimation Proc record, accnId="+accnId+", actualCount="+ptEstimationProcs.size());
        List<PtEstimationBenefit> ptEstimationBenefits = accessionDao.getPtEstimationBenefitByPtEstimationProcId(Collections.singletonList(ptEstimationProcs.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationBenefits.size(), 1, "Expected 1 Patient Estimation Benefit record, accnId="+accnId+", actualCount="+ptEstimationBenefits.size());

        logger.info("Verifying applied copay data, accnId=" + accnId);
        Money expectedCopayAmt = new Money(copayAmt);
        Assert.assertEquals(ptEstimationBenefits.get(0).getBenefitTypId(), MiscMap.PT_ESTIMATION_BENEFIT_TYP_COPAYMENT, "Expected applied benefit type to be Copayment, accnId="+accnId);
        Assert.assertNotEquals(ptEstimationBenefits.get(0).getSvcTyps(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode(), "Expected applied service type to not be 30, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getSvcTyps(), ptEstimationProcs.get(0).getSvcTyp(), "Expected applied service type to be equal to proc service type, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getTimePeriod(), "Visit", "Expected applied time period to be 'Visit', accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getVisitAmtAsMoney(), expectedCopayAmt, "Expected per-visit copay amt does not match, accnId="+accnId);
        Assert.assertTrue(ptEstimationBenefits.get(0).getIsApplied(), "Expected coinsurance applied flag to be True, accnId="+accnId);
        Money actualAppliedCopayAmount = ptEstimationBenefits.get(0).getAppliedAmtAsMoney();

        logger.info("Verifying proc data, accnId=" + accnId);
        Assert.assertTrue(StringUtils.isNotBlank(ptEstimationProcs.get(0).getProcCd()), "Expected proc code to not be empty, accnId="+accnId);
        Assert.assertFalse(StringUtils.contains(ptEstimationProcs.get(0).getSvcTyp(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode()), "Expected proc service type to not be 30, accnId="+accnId);
        Money expectAmount = ptEstimationProcs.get(0).getExpAmtAsMoney();
        Assert.assertEquals(actualAppliedCopayAmount, expectedCopayAmt, "Expected applied copay amount does not match, accnId="+accnId);

        logger.info("Verifying estimation data, accnId=" + accnId);
        Assert.assertTrue(DateUtils.isSameDay(ptEstimations.get(0).getEstimationDt(), new Date(System.currentTimeMillis())), "Expected estimation date to be today, accnId="+accnId);
        Assert.assertEquals(ptEstimations.get(0).getPtRespAmtAsMoney(), expectedCopayAmt, "Expected patient responsibility amount to be equal to applied copay amount, accnId="+accnId);
    }

    @Test(groups = {"calculateMin"}, priority = 1, description = "Calculate minimum patient responsibility when configured (SS 18005 is False)")
    @Parameters({"accnId", "pyrAbbrv", "copayAmt", "coinsuranceRate"})
    public void calculateMinimumPatientResponsibility(String accnId, String pyrAbbrv, String copayAmt, String coinsuranceRate) throws Exception
    {
        logger.info("Starting Test Case: calculateMinimumPatientResponsibility, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv+", copayAmt="+copayAmt+", coinsuranceRate="+coinsuranceRate);
        deletePtEstimationData(accnId);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, null, true, true, true);

        List<PtEstimation> ptEstimations = accessionDao.getPtEstimationsByAccnId(accnId);
        Assert.assertEquals(ptEstimations.size(), 1, "Expected 1 Patient Estimation record, accnId="+accnId+", actualCount="+ptEstimations.size());
        List<PtEstimationProc> ptEstimationProcs = accessionDao.getPtEstimationProcsByPtEstimationId(Collections.singletonList(ptEstimations.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationProcs.size(), 1, "Expected 1 Patient Estimation Proc record, accnId="+accnId+", actualCount="+ptEstimationProcs.size());
        List<PtEstimationBenefit> ptEstimationBenefits = accessionDao.getPtEstimationBenefitByPtEstimationProcId(Collections.singletonList(ptEstimationProcs.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationBenefits.size(), 2, "Expected 2 Patient Estimation Benefit records, accnId="+accnId+", actualCount="+ptEstimationBenefits.size());

        logger.info("Verifying proc data, accnId=" + accnId);
        Assert.assertTrue(StringUtils.isNotBlank(ptEstimationProcs.get(0).getProcCd()), "Expected proc code to not be empty, accnId="+accnId);

        logger.info("Verifying applied patient responsibility data, accnId=" + accnId);
        Money expectedCopayAmt = new Money(copayAmt);
        Assert.assertEquals(ptEstimationBenefits.get(0).getBenefitTypId(), MiscMap.PT_ESTIMATION_BENEFIT_TYP_COPAYMENT, "Expected applied benefit type to be Copayment, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getSvcTyps(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode(), "Expected applied service type to be 30, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getTimePeriod(), "Visit", "Expected applied time period to be 'Visit', accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getVisitAmtAsMoney(), expectedCopayAmt, "Expected per-visit copay amt does not match, accnId="+accnId);
        Assert.assertTrue(ptEstimationBenefits.get(0).getIsApplied(), "Expected copay applied flag to be True, accnId="+accnId);
        Money actualAppliedCopayAmount = ptEstimationBenefits.get(0).getAppliedAmtAsMoney();
        Assert.assertEquals(actualAppliedCopayAmount, expectedCopayAmt, "Expected applied copay amount does not match, accnId="+accnId);

        float expectedAppliedCoinsuranceRate = Float.parseFloat(coinsuranceRate);
        Assert.assertEquals(ptEstimationBenefits.get(1).getBenefitTypId(), MiscMap.PT_ESTIMATION_BENEFIT_TYP_COINSURANCE, "Expected applied benefit type to be Coinsurance, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(1).getSvcTyps(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode(), "Expected applied service type to be 30, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(1).getSvcTyps(), ptEstimationProcs.get(0).getSvcTyp(), "Expected applied service type to be equal to proc service type, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(1).getTimePeriod(), "Visit", "Expected applied time period to be 'Visit', accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(1).getRate(), expectedAppliedCoinsuranceRate, "Expected applied coinsurance rate does not match, accnId="+accnId);
        Assert.assertTrue(ptEstimationBenefits.get(1).getIsApplied(), "Expected coinsurance applied flag to be True, accnId="+accnId);
        Money actualAppliedCoinsuranceAmount = ptEstimationBenefits.get(1).getAppliedAmtAsMoney();
        Money expectAmount = ptEstimationProcs.get(0).getExpAmtAsMoney();
        Money expectedAppliedCoinsuranceAmount = expectAmount.multiplyBD(expectedAppliedCoinsuranceRate);
        Assert.assertEquals(actualAppliedCoinsuranceAmount, expectedAppliedCoinsuranceAmount, "Expected applied coinsurnace amount does not match, accnId="+accnId);

        logger.info("Verifying estimation data, accnId=" + accnId);
        Assert.assertTrue(DateUtils.isSameDay(ptEstimations.get(0).getEstimationDt(), new Date(System.currentTimeMillis())), "Expected estimation date to be today, accnId="+accnId);
        Assert.assertEquals(ptEstimations.get(0).getPtRespAmtAsMoney(), expectedCopayAmt, "Expected patient responsibility amount to be equal to applied copay amount, accnId="+accnId);
    }

    @Test(groups = {"calculateMax"}, dependsOnGroups = {"calculateMin"}, alwaysRun = true, priority = 1, description = "Calculate maximum patient responsibility when configured (SS 18005 is True)")
    @Parameters({"accnId", "pyrAbbrv", "copayAmt", "coinsuranceRate"})
    public void calculateMaximumPatientResponsibility(String accnId, String pyrAbbrv, String copayAmt, String coinsuranceRate) throws Exception
    {
        logger.info("Starting Test Case: calculateMaximumPatientResponsibility, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv+", copayAmt="+copayAmt+", coinsuranceRate="+coinsuranceRate);
        updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_CALCULATE_MAXIMUM_PATIENT_RESPONSIBILITY, "True", "1");
        ClearCacheUtil clearCacheUtil = new ClearCacheUtil(config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.XIFINADMINPORTAL_URL));
        clearCacheUtil.clearCache(CacheMap.SYSTEM_SETTINGS);
        deletePtEstimationData(accnId);
        patientEstimationDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, null, true, true, true);

        List<PtEstimation> ptEstimations = accessionDao.getPtEstimationsByAccnId(accnId);
        Assert.assertEquals(ptEstimations.size(), 1, "Expected 1 Patient Estimation record, accnId="+accnId+", actualCount="+ptEstimations.size());
        List<PtEstimationProc> ptEstimationProcs = accessionDao.getPtEstimationProcsByPtEstimationId(Collections.singletonList(ptEstimations.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationProcs.size(), 1, "Expected 1 Patient Estimation Proc record, accnId="+accnId+", actualCount="+ptEstimationProcs.size());
        List<PtEstimationBenefit> ptEstimationBenefits = accessionDao.getPtEstimationBenefitByPtEstimationProcId(Collections.singletonList(ptEstimationProcs.get(0).getSeqId()));
        Assert.assertEquals(ptEstimationBenefits.size(), 2, "Expected 2 Patient Estimation Benefit records, accnId="+accnId+", actualCount="+ptEstimationBenefits.size());

        logger.info("Verifying proc data, accnId=" + accnId);
        Assert.assertTrue(StringUtils.isNotBlank(ptEstimationProcs.get(0).getProcCd()), "Expected proc code to not be empty, accnId="+accnId);

        logger.info("Verifying applied patient responsibility data, accnId=" + accnId);
        Money expectedCopayAmt = new Money(copayAmt);
        Assert.assertEquals(ptEstimationBenefits.get(0).getBenefitTypId(), MiscMap.PT_ESTIMATION_BENEFIT_TYP_COPAYMENT, "Expected applied benefit type to be Copayment, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getSvcTyps(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode(), "Expected applied service type to be 30, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getTimePeriod(), "Visit", "Expected applied time period to be 'Visit', accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(0).getVisitAmtAsMoney(), expectedCopayAmt, "Expected per-visit copay amt does not match, accnId="+accnId);
        Assert.assertTrue(ptEstimationBenefits.get(0).getIsApplied(), "Expected copay applied flag to be True, accnId="+accnId);
        Money actualAppliedCopayAmount = ptEstimationBenefits.get(0).getAppliedAmtAsMoney();
        Assert.assertEquals(actualAppliedCopayAmount, expectedCopayAmt, "Expected applied copay amount does not match, accnId="+accnId);

        float expectedAppliedCoinsuranceRate = Float.parseFloat(coinsuranceRate);
        Assert.assertEquals(ptEstimationBenefits.get(1).getBenefitTypId(), MiscMap.PT_ESTIMATION_BENEFIT_TYP_COINSURANCE, "Expected applied benefit type to be Coinsurance, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(1).getSvcTyps(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_30.getCode(), "Expected applied service type to be 30, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(1).getSvcTyps(), ptEstimationProcs.get(0).getSvcTyp(), "Expected applied service type to be equal to proc service type, accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(1).getTimePeriod(), "Visit", "Expected applied time period to be 'Visit', accnId="+accnId);
        Assert.assertEquals(ptEstimationBenefits.get(1).getRate(), expectedAppliedCoinsuranceRate, "Expected applied coinsurance rate does not match, accnId="+accnId);
        Assert.assertTrue(ptEstimationBenefits.get(1).getIsApplied(), "Expected coinsurance applied flag to be True, accnId="+accnId);
        Money actualAppliedCoinsuranceAmount = ptEstimationBenefits.get(1).getAppliedAmtAsMoney();
        Money expectAmount = ptEstimationProcs.get(0).getExpAmtAsMoney();
        Money expectedAppliedCoinsuranceAmount = expectAmount.multiplyBD(expectedAppliedCoinsuranceRate);
        Assert.assertEquals(actualAppliedCoinsuranceAmount, expectedAppliedCoinsuranceAmount, "Expected applied coinsurnace amount does not match, accnId="+accnId);

        logger.info("Verifying estimation data, accnId=" + accnId);
        Assert.assertTrue(DateUtils.isSameDay(ptEstimations.get(0).getEstimationDt(), new Date(System.currentTimeMillis())), "Expected estimation date to be today, accnId="+accnId);
        Assert.assertEquals(ptEstimations.get(0).getPtRespAmtAsMoney(), expectedCopayAmt, "Expected patient responsibility amount to be equal to applied copay amount, accnId="+accnId);
    }

    private void patientEstimationDeterminationTest(String accnId, String pyrAbbrv, int eligStaTypId, String eligErrorAbbrev, boolean isStayInEligQue, String accnCntct, boolean isExpectPtEstimation, boolean isExpectPtEstimationBenefit, boolean isInNetwork) throws Exception
    {
        patientEstimationDeterminationTest(accnId, pyrAbbrv, eligStaTypId, eligErrorAbbrev, isStayInEligQue, accnCntct, isExpectPtEstimation, isExpectPtEstimationBenefit, isInNetwork, ACCN_STA_TYP_PRICED_OR_ZBAL);
    }

    private void patientEstimationDeterminationTest(String accnId, String pyrAbbrv, int eligStaTypId, String eligErrorAbbrev, boolean isStayInEligQue, String accnCntct, boolean isExpectPtEstimation, boolean isExpectPtEstimationBenefit, boolean isInNetwork, Set<Integer>expectedAccnStaTyps) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        if (eligStaTypId != EligMap.ELIG_STA_TYP_NOT_CHECKED)
        {
            verifyPyrEligSetup(pyr, true);
        }

        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying accession status, accnId=" + accnId + ", staId=" + origAccn.getStaId() + ", expectedStaIds=" + expectedAccnStaTyps);
        Assert.assertTrue(expectedAccnStaTyps.contains(origAccn.getStaId()));

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertFalse(origAccnPyrs.isEmpty());
        deleteXifinEligTransBySubId(origAccnPyrs.get(0).getSubsId());
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);
        logger.info("Verified accession eligibility history is cleared, accnId=" + accnId);

        logger.info("Load on accession detail screen, accnId=" + accnId);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        logger.info("Loaded on accession detail screen, accnId=" + accnId);

        // Verify the primary payor is correct
        if (!accessionDetail.primaryPayorAbbrText().getText().equals(pyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + pyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, pyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }

        if (eligErrorAbbrev != null && !accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, 1, false).isEmpty())
        {
            logger.info("Fixing the eligibility error, accnId=" + accnId + ", errorAbbrev=" + eligErrorAbbrev);
            try
            {
                accessionDetail.fixUnfixedErrEligAccnDetail();
                accessionDetail.saveAndClear(wait);
                accessionDetail.setAccnId(accnId, wait);
            }
            catch (WebDriverException e)
            {
                logger.debug("Unable to fix expected eligibility error, accnId=" + accnId + ", errorAbbrev=" + eligErrorAbbrev);
            }
        }
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, 1, false).isEmpty(), "Expected no open errors for primary payor, accnId=" + accnId);

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Unpriceable accns won't go through Elig Queue via Force to Reprice. Need to send back thru QFrPending.
        if (expectedAccnStaTyps.contains(AccnStatusMap.ACCN_STATUS_EP_UNPRICEABLE))
        {
            navigation.navigateToAccnTestUpdatePage();
            AccnTestUpdate atu = new AccnTestUpdate(driver,wait);
            atu.setLookUpAccnId(accnId);
            assertTrue(atu.isAccnLoaded(accnId, wait));
            atu.clickSaveAndClearBtn();
            assertTrue(isOutOfQFrPendingQueue(accnId, QUEUE_WAIT_TIME*2));
        }

        // Reload accession from DB
        origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertNull(origAccn.getPrcDt(), "Expected pricing backed out status, accnId=" + accnId +", prcDt="+origAccn.getPrcDt());

        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        // Verify eligibility data
        Integer translatedPyrId = null;
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        verifyAccnEligHist(eligStaTypId, pyr, accnEligHists, translatedPyrId);

        // If accession was staying in elig queue, make sure elig status, date last check, checked performed is updated,
        // update the dates on the q_elig record so it moves along on next engine pass.
        if (isStayInEligQue)
        {
            verifyQEligAndMoveAlong(accnId, eligStaTypId, accnCntct, pyr);
        }

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        List<AccnPyr> newAccnPyrs = verifyAccnPyr(accnId, eligStaTypId, pyr);

        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME * 5));
        logger.info("Verifying patient and insured demographic data is not updated, accnId=" + accnId);
        verifyDemoData(origAccn, origAccnPyrs, accessionDao.getAccn(accnId), newAccnPyrs);

        Assert.assertTrue(isOutOfQPtEstimationQueue(accnId, QUEUE_WAIT_TIME * 5));
        verifyPtEstimation(accnId, isExpectPtEstimation, isExpectPtEstimationBenefit, isInNetwork, origAccn, accessionDao.getAccnEligHistByAccnId(accnId), pyr.getPyrId(), pyr.getPyrId());

        origAccn = accessionDao.getAccn(accnId);
        if (!isStayInEligQue)
        {
            Assert.assertEquals(origAccn.getStaId(),
                    expectedAccnStaTyps.contains(AccnStatusMap.ACCN_STATUS_EP_UNPRICEABLE) ?
                            AccnStatusMap.ACCN_STATUS_EP_UNPRICEABLE : AccnStatusMap.ACCN_STATUS_PRICED);
        }
        verifyErrors(accnId, eligErrorAbbrev, origAccn, newAccnPyrs);
    }

    private void patientEstimationTranslationDeterminationTest(String accnId, String pyrAbbrv, int eligStaTypId, String eligErrorAbbrev, boolean isStayInEligQue, String accnCntct, boolean isExpectPtEstimation, boolean isExpectPtEstimationBenefit, boolean isInNetwork, String eligTranslatedPyrAbbrv, String ptEstPyrAbbrv) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        verifyPyrEligSetup(pyr, true);
        Pyr eligTranslatedPyr = rpmDao.getPyrByPyrAbbrv(testDb, eligTranslatedPyrAbbrv);
        //verifyPyrEligSetup(eligTranslatedPyr, true);
        Pyr ptEstTranslatedPyr = rpmDao.getPyrByPyrAbbrv(testDb, ptEstPyrAbbrv);

        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying accession status is priced or zbal, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(ACCN_STA_TYP_PRICED_OR_ZBAL.contains(origAccn.getStaId()));

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertFalse(origAccnPyrs.isEmpty());
        deleteXifinEligTransBySubId(origAccnPyrs.get(0).getSubsId());
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);
        logger.info("Verified accession eligibility history is cleared, accnId=" + accnId);

        logger.info("Load on accession detail screen, accnId=" + accnId);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        logger.info("Loaded on accession detail screen, accnId=" + accnId);

        boolean isPayorChanged = false;

        if (eligErrorAbbrev != null)
        {
            logger.info("Fixing the eligibility error, accnId=" + accnId + ", errorAbbrev=" + eligErrorAbbrev);
            try
            {
                accessionDetail.fixUnfixedErrEligAccnDetail();
            }
            catch (WebDriverException e)
            {
                logger.debug("Unable to fix expected eligibility error, accnId=" + accnId + ", errorAbbrev=" + eligErrorAbbrev);
            }
        }

        // Verify the primary payor is correct
        if (!accessionDetail.primaryPayorAbbrText().getText().equals(pyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + pyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, pyrAbbrv);
            isPayorChanged = true;
        }

        if (!isPayorChanged)
        {
            logger.info("Forcing accession to reprice, accnId=" + accnId);
            accessionDetail.clickRepriceCheckbox(wait);
        }

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.saveAndClear(wait);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, 1, false).isEmpty(), "Expected no open errors for primary payor, accnId=" + accnId);

        // Reload accession from DB
        origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertEquals(origAccn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected final reported status, accnId=" + accnId);

        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        verifyAccnEligHist(eligStaTypId, pyr, accnEligHists, eligTranslatedPyr.getPyrId());

        // If accession was staying in elig queue, make sure elig status, date last check, checked performed is updated,
        // update the dates on the q_elig record so it moves along on next engine pass.
        if (isStayInEligQue)
        {
            verifyQEligAndMoveAlong(accnId, eligStaTypId, accnCntct, pyr);
        }

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        List<AccnPyr> newAccnPyrs = verifyAccnPyr(accnId, eligStaTypId, eligTranslatedPyr);

        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME * 5));
        logger.info("Verifying patient and insured demographic data is not updated, accnId=" + accnId);
        verifyDemoData(origAccn, origAccnPyrs, accessionDao.getAccn(accnId), newAccnPyrs);

        boolean isOutOfQPtEstimationQueue = isOutOfQPtEstimationQueue(accnId, QUEUE_WAIT_TIME * 2);
        if (isOutOfQPtEstimationQueue)
        {
            verifyPtEstimation(accnId, isExpectPtEstimation, isExpectPtEstimationBenefit, isInNetwork, origAccn, accessionDao.getAccnEligHistByAccnId(accnId), eligTranslatedPyr.getPyrId(), ptEstTranslatedPyr.getPyrId());
        }
        else if (!isExpectPtEstimation)
        {
            verifyPtEstimationErr(accnId, ptEstPyrAbbrv, ptEstTranslatedPyr);
        }

        origAccn = accessionDao.getAccn(accnId);
        if (!isStayInEligQue)
        {
            Assert.assertEquals(origAccn.getStaId(), AccnStatusMap.ACCN_STATUS_PRICED);
        }
        verifyErrors(accnId, eligErrorAbbrev, origAccn, newAccnPyrs);
    }

    private void verifyPtEstimationErr(String accnId, String ptEstPyrAbbrv, Pyr ptEstTranslatedPyr) throws XifinDataAccessException
    {
        List<PyrElig> ptEstPyrElig = rpmDao.getPyrElig(null, ptEstTranslatedPyr.getPyrId());
        Assert.assertTrue(ptEstPyrElig.isEmpty());
        QPtEstimation qPtEstimation = accessionDao.getQPtEstimation(accnId);
        Assert.assertNotNull(qPtEstimation);
        Assert.assertTrue(qPtEstimation.getIsErr(), "Expected error in qPtEstimation, accnId=" + accnId);
        Assert.assertEquals(qPtEstimation.getNote(), "[12002 - Eligibility check failed for Payor " + ptEstPyrAbbrv + ": Not Checked - Eligibility not found.]", "No match on qPtEstimation note, accnId=" + accnId);
    }

    private void verifyQEligAndMoveAlong(String accnId, int eligStaTypId, String accnCntct, Pyr pyr) throws Exception
    {
        QElig qElig = null;
        try
        {
            qElig = accessionDao.getQEligByAccnId(accnId);
        }
        catch (Exception e)
        {
            Assert.fail("Could not find qElig entry for accession to stay in queue, accnId=" + accnId);
        }
        logger.info("Making sure qElig data is updated, accnId=" + accnId + " qElig=" + qElig);
        Assert.assertEquals(qElig.getEligStaTypId(), eligStaTypId);
        if (qElig.getEligStaTypId() != (EligMap.ELIG_STA_TYP_UNREACHABLE))
        {
            Assert.assertNotNull(qElig.getDtLstChk());
        }
        Assert.assertEquals(qElig.getChksPrfrmd(), 1);

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId=" + accnId + " qElig=" + accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));

        logger.info("Updating eligibility check dates to force accession out of eligibility queue, accnId=" + accnId);
        Date newDt = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1) * (pyr.getDaysToChkElig() + 1));
        accessionDao.updateQEligDates(accnId, newDt, newDt);
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");
    }

    private List<AccnPyr> verifyAccnPyr(String accnId, int eligStaTypId, Pyr pyr) throws XifinDataAccessException
    {
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertFalse(newAccnPyrs.isEmpty());
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());

        // Unreachable and Not Checked status will not update accession payor elig fields
        if (eligStaTypId == EligMap.ELIG_STA_TYP_UNREACHABLE || eligStaTypId == EligMap.ELIG_STA_TYP_NOT_CHECKED)
        {
            Assert.assertEquals(newAccnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_NOT_CHECKED, "Expected eligStaTyp to not be updated,");
            Assert.assertNull(newAccnPyrs.get(0).getEligSvcName());
            Assert.assertNull(newAccnPyrs.get(0).getEligChkDt());
        }
        else
        {
            Assert.assertEquals(newAccnPyrs.get(0).getEligStaTypId(), eligStaTypId);
            Assert.assertEquals(newAccnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
            Assert.assertNotNull(newAccnPyrs.get(0).getEligChkDt());
        }
        return newAccnPyrs;
    }

    private void verifyErrors(String accnId, String eligErrorAbbrev, Accn origAccn, List<AccnPyr> newAccnPyrs) throws XifinDataAccessException, XifinDataNotFoundException
    {
        if (eligErrorAbbrev == null)
        {
            logger.info("Verifying no new errors added, accnId=" + accnId);
            Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        }
        else
        {
            logger.info("Verifying eligibility error is added, accnId=" + accnId + ", errorAbbrev=" + eligErrorAbbrev);
            verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(eligErrorAbbrev)), origAccn, newAccnPyrs.get(0));
        }
    }

    private void verifyPtEstimation(String accnId, boolean isExpectPtEstimation, boolean isExpectPtEstimationBenefit, boolean isInNetwork, Accn accn, List<AccnEligHist> accnEligHists, int pyrId, int respPyrId) throws XifinDataAccessException
    {
        List<String> transIds = new ArrayList<>();
        List<PtEstimation> ptEstimations = new ArrayList<>();
        if (!accnEligHists.isEmpty())
        {
            transIds.add(accnEligHists.get(0).getTransId());
            ptEstimations = accessionDao.getPtEstimationsByTransIds(transIds);
        }
        if (ptEstimations.isEmpty())
        {
            ptEstimations = accessionDao.getPtEstimationsByAccnInfo(accn.getPtDob(), accn.getPtFNm(), accn.getPtLNm());
        }

        if (isExpectPtEstimation)
        {
            Assert.assertEquals(ptEstimations.size(), 1, "Expected ptEstimation entry, accnId=" + accnId + ", eligTransIds=" + transIds);
            Assert.assertEquals(ptEstimations.get(0).getPyrIdResp(), respPyrId);
            Assert.assertEquals(ptEstimations.get(0).getPyrId(), pyrId);

            List<Integer> ptEstimationIds = new ArrayList<>();
            ptEstimationIds.add(ptEstimations.get(0).getSeqId());
            List<PtEstimationProc> ptEstimationProcs = accessionDao.getPtEstimationProcsByPtEstimationId(ptEstimationIds);
            Assert.assertEquals(ptEstimationProcs.size(), 1, "Expected ptEstimationProc entry, accnId=" + accnId + ", ptEstimationIds=" + ptEstimationIds);

            List<Integer> ptEstimationProcIds = new ArrayList<>();
            ptEstimationProcIds.add(ptEstimationProcs.get(0).getSeqId());
            List<PtEstimationBenefit> ptEstimationBenefits = accessionDao.getPtEstimationBenefitByPtEstimationProcId(ptEstimationProcIds);
            if (isExpectPtEstimationBenefit)
            {
                Assert.assertFalse(ptEstimationBenefits.isEmpty(), "Expected ptEstimationBenefit entry, accnId=" + accnId + ", ptEstimationProcIds=" + ptEstimationProcIds);
                List<PtEstimationBenefit> ptEstimationBenefitNetworkList = new ArrayList<>();
                for (PtEstimationBenefit ptEstimationBenefit : ptEstimationBenefits)
                {
                    if (ptEstimationBenefit.getIsInNetwork() == isInNetwork)
                    {
                        ptEstimationBenefitNetworkList.add(ptEstimationBenefit);
                    }
                }
                Assert.assertFalse(ptEstimationBenefitNetworkList.isEmpty(), "Expected ptEstimationBenefits with network status");
            }
            else if (!ptEstimationBenefits.isEmpty())
            {
                Assert.fail("No Patient Benefits were expected, accnId=" + accnId);
            }
        }
        else if (!ptEstimations.isEmpty())
        {
            Assert.fail("Patient estimation was not expected, accnId=" + accnId);
        }
    }

    private void verifyDemoData(Accn origAccn, List<AccnPyr> origAccnPyrs, Accn newAccn, List<AccnPyr> newAccnPyrs)
    {
        Assert.assertEquals(origAccn.getPtFNm(), newAccn.getPtFNm());
        Assert.assertEquals(origAccn.getPtLNm(), newAccn.getPtLNm());
        Assert.assertEquals(origAccn.getPtAddr1(), newAccn.getPtAddr1());
        Assert.assertEquals(origAccn.getPtAddr2(), newAccn.getPtAddr2());
        Assert.assertEquals(origAccn.getPtSex(), newAccn.getPtSex());
        Assert.assertEquals(origAccnPyrs.get(0).getSubsId(), newAccnPyrs.get(0).getSubsId());
        Assert.assertEquals(origAccnPyrs.get(0).getInsFNm(), newAccnPyrs.get(0).getInsFNm());
        Assert.assertEquals(origAccnPyrs.get(0).getInsLNm(), newAccnPyrs.get(0).getInsLNm());
        Assert.assertEquals(origAccnPyrs.get(0).getInsAddr1(), newAccnPyrs.get(0).getInsAddr1());
        Assert.assertEquals(origAccnPyrs.get(0).getInsAddr2(), newAccnPyrs.get(0).getInsAddr2());
        Assert.assertEquals(origAccnPyrs.get(0).getInsSex(), newAccnPyrs.get(0).getInsSex());
        Assert.assertEquals(origAccnPyrs.get(0).getRelshpId(), newAccnPyrs.get(0).getRelshpId());
    }

    private void verifyAccnEligHist(int eligStaTypId, Pyr pyr, List<AccnEligHist> accnEligHists, Integer translatedPyrId)
    {
        if (eligStaTypId == EligMap.ELIG_STA_TYP_NOT_CHECKED)
        {
            Assert.assertEquals(accnEligHists.size(), 0, "Expected accnEligHist was not empty,");
        }
        else
        {
            Assert.assertEquals(accnEligHists.size(), 1, "Expected accnEligHist was not found,");
            Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
            Assert.assertEquals(accnEligHists.get(0).getTranslatedPyrId(), translatedPyrId);
            Assert.assertNotNull(accnEligHists.get(0).getTransDt());
            Assert.assertNotNull(accnEligHists.get(0).getTransId());
            Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), eligStaTypId, "Accession has wrong eligStaTyp,");
        }
    }

    private void verifyPyrEligSetup(Pyr pyr, boolean isTranslationEnabled) throws XifinDataAccessException
    {
        PyrElig pyrElig = null;
        for (PyrElig pe : rpmDao.getPyrEligByPyrIdEligSvcId(testDb, pyr.getPyrId(), EligMap.XIFIN))
        {
            if (!pe.getIsDeleted() && pe.getIsPreferred())
            {
                pyrElig = pe;
            }
        }
        Assert.assertNotNull(pyrElig);
        // Translation should be disabled
        Assert.assertEquals(pyrElig.getIsTranslationEnabled(), isTranslationEnabled);
        // All demographic updates should be disabled
        Assert.assertFalse(pyrElig.getIsUpdateInsSubsId());
        Assert.assertFalse(pyrElig.getIsUpdateInsNm());
        Assert.assertFalse(pyrElig.getIsUpdateInsAddr());
        Assert.assertFalse(pyrElig.getIsUpdateInsGender());
        Assert.assertFalse(pyrElig.getIsUpdateInsRelshp());
        Assert.assertFalse(pyrElig.getIsUpdatePtNm());
        Assert.assertFalse(pyrElig.getIsUpdatePtAddr());
        Assert.assertFalse(pyrElig.getIsUpdatePtGender());
        // Days to check elig should be at least 1
        Assert.assertTrue(pyr.getDaysToChkElig() > 0);
    }

    private boolean isOutOfQPtEstimationQueue(String accnId, long maxTime) throws InterruptedException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        QPtEstimation qPtEstimation = accessionDao.getQPtEstimation(accnId);
        boolean isInQueue = qPtEstimation != null && !qPtEstimation.getIsErr();
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit patient estimation queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            qPtEstimation = accessionDao.getQPtEstimation(accnId);
            isInQueue = qPtEstimation != null && !qPtEstimation.getIsErr();
        }
        return !isInQueue;
    }

    private boolean isOutOfValidationQueue(String accnId, long maxTime) throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = accessionDao.isInValidationQueue(accnId);
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit validation queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            isInQueue = accessionDao.isInValidationQueue(accnId);
        }
        return !isInQueue;
    }


    protected void setOriginalClnToAccn(AccessionDetail accessionDetail, String accnId, String origClnAbbrv) throws InterruptedException
    {
        WebElement clientIdInput = accessionDetail.clnIdInput();
        wait.until(ExpectedConditions.visibilityOf(clientIdInput));
        Thread.sleep(4000);
        clientIdInput.clear();
        Thread.sleep(4000);
        clientIdInput.sendKeys(origClnAbbrv + Keys.TAB);
        Thread.sleep(4000);
    }

    protected void setOriginalPyrToAccn(AccessionDetail accessionDetail, String accnId, String origPyrAbbrv) throws InterruptedException
    {
        WebElement primaryPyrId = accessionDetail.primaryPayorIDInput();
        wait.until(ExpectedConditions.visibilityOf(primaryPyrId));
        Thread.sleep(4000);
        primaryPyrId.clear();
        Thread.sleep(4000);
        primaryPyrId.sendKeys(origPyrAbbrv + Keys.TAB);
        Thread.sleep(4000);
    }

    private void deleteXifinEligTransBySubId(String subId) throws XifinDataAccessException
    {
        daoManagerEligibilityWS.deleteXifinEligTransBySubId(config.getProperty(PropertyMap.ORGALIAS), subId);
    }


    protected boolean isOutOfQFrPendingQueue(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = accessionDao.isInQFrPendingQueue(accnId);
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInQueue = accessionDao.isInQFrPendingQueue(accnId);
        }
        return !isInQueue;
    }

    private void deletePtEstimationData(String accnId) throws XifinDataAccessException
    {
        logger.info("Deleting PtEstimation records , accnId="+accnId);
        for (PtEstimation ptEstimation : accessionDao.getPtEstimationsByAccnId(accnId))
        {
            logger.info("Deleting PtEstimation, accnId="+accnId+", ptEstimationSeqId="+ptEstimation.getSeqId());
            for (PtEstimationProc ptEstimationProc : accessionDao.getPtEstimationProcsByPtEstimationId(Arrays.asList(ptEstimation.getSeqId())))
            {
                logger.info("Deleting PtEstimationProc, accnId="+accnId+", ptEstimationSeqId="+ptEstimation.getSeqId()+", ptEstimationProcSeqId="+ptEstimationProc.getSeqId());
                for (PtEstimationBenefit ptEstimationBenefit : accessionDao.getPtEstimationBenefitByPtEstimationProcId(Arrays.asList(ptEstimationProc.getSeqId())))
                {
                    logger.info("Deleting PtEstimationBenefit, accnId="+accnId+", ptEstimationSeqId="+ptEstimation.getSeqId()+", ptEstimationBenefitSeqId="+ptEstimationBenefit.getSeqId());
                    ptEstimationBenefit.setResultCode(ErrorCodeMap.DELETED_RECORD);
                    databaseSequenceDao.setValueObject(ptEstimationBenefit);
                }
                ptEstimationProc.setResultCode(ErrorCodeMap.DELETED_RECORD);
                databaseSequenceDao.setValueObject(ptEstimationProc);
            }
            ptEstimation.setResultCode(ErrorCodeMap.DELETED_RECORD);
            databaseSequenceDao.setValueObject(ptEstimation);
        }
    }

    private void deleteErrorsAndRevalidate(Accn accn, Set<String> errCdAbbrevs) throws XifinDataAccessException, InterruptedException, XifinDataNotFoundException
    {
        Set<Integer> errCds = new HashSet<>();
        for (String errCdAbbrev : errCdAbbrevs)
        {
            errCds.add(rpmDao.getErrCd(null, errCdAbbrev, EpConstants.DNL_TBL_ID_UNBILL, accn.getDos()).getErrCd());
        }

        logger.info("Deleting AccnPyrErrs, accnId=" + accn.getAccnId()+", errCdAbbrevs="+errCdAbbrevs);
        for (AccnPyrErr ape : accessionDao.getAccnPyrErrsByAccnId(accn.getAccnId(), true, true))
        {
            if (errCds.contains(ape.getErrCd()) && ape.getPyrPrio() == 1)
            {
                ape.setResultCode(ErrorCodeMap.DELETED_RECORD);
                accessionDao.setAccnPyrErr(ape);
            }
        }

        logger.info("Validating accession, accnId=" + accn.getAccnId());
        QValidateAccn qValidateAccn = rpmDao.getQValidateAccnByAccnId(null, accn.getAccnId());
        if (qValidateAccn == null)
        {
            qValidateAccn = new QValidateAccn();
            qValidateAccn.setAccnId(accn.getAccnId());
        }
        qValidateAccn.setIsErr(false);
        qValidateAccn.setPriority(3);
        qValidateAccn.setValidateTypId(16);
        accessionDao.setQValidateAccn(qValidateAccn);
        Assert.assertTrue(isOutOfValidationQueue(accn.getAccnId(), QUEUE_WAIT_TIME_MS));
    }
}
