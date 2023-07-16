package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnEligHist.AccnEligHist;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.taskTyp.TaskTyp;
import com.mbasys.mars.eligibility.EligMap;
import com.mbasys.mars.errorProcessing.EpConstants;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.LoadAccession;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.headerNavigation.HeaderNavigation;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class EligibilityBaseTest extends SeleniumBaseTest
{
    protected static long QUEUE_POLL_TIME = TimeUnit.SECONDS.toMillis(5);
    protected static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(10);
    protected static final Set<Integer> ACCN_STA_TYP_PRICED_OR_ZBAL;
    static
    {
        ACCN_STA_TYP_PRICED_OR_ZBAL = new HashSet<>();
        ACCN_STA_TYP_PRICED_OR_ZBAL.add(AccnStatusMap.ACCN_STATUS_PRICED);
        ACCN_STA_TYP_PRICED_OR_ZBAL.add(AccnStatusMap.ACCN_STATUS_ZBAL);
    }
//    WebElement xifinLogo = driver.findElementById("logo_image");
    protected Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    protected TestParameters parameters;

    @BeforeTest(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins", "accnId"})
    public void beforeTest(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String disableBrowserPlugins, @Optional String accnId)
    {
        try
        {
            logger.info("Running BeforeTest");
            parameters = new TestParameters();
            parameters.setPlatform(platform);
            parameters.setBrowserName(browser);
            parameters.setBrowserVersion(version);
            parameters.setPort(port);
            parameters.setHub(hub);
            parameters.setTimeout(timeout);
            parameters.setSsoUsername(ssoUsername);
            parameters.setSsoPassword(ssoPassword);
            parameters.setDisableBrowserPlugins(disableBrowserPlugins);
            parameters.setAccnId(accnId);
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            // Make sure accession is out of eligibility queue
            Date newDt = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365));
            accessionDao.updateQEligDates(accnId, newDt, newDt);
            Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Failed to leave eligibility queue, accnId=" + accnId + ", waitTime=" + QUEUE_WAIT_TIME+ ",");
            cleanUpAccn(accnId);
            // Make sure accession is out of pricing queue
            Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME), "Failed to leave pricing queue, accnId=" + accnId + ", waitTime=" + QUEUE_WAIT_TIME+ ",");
        }
        catch (SkipException e)
        {
            logger.warn("Skipped exception thrown during BeforeTest", e);
        }
        catch (Exception e)
        {
            logger.warn("Error running BeforeTest", e);
            throw new SkipException("Error running BeforeTest", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    protected static void verifyAccnEligDataIsCleared(List<AccnEligHist> accnEligHists, List<AccnPyr> accnPyrs)
    {
        Assert.assertTrue(accnEligHists.isEmpty(), "Failed to clear accnEligHist data,");
        for (AccnPyr accnPyr : accnPyrs)
        {
            Assert.assertNull(accnPyr.getEligSvcName(), "Failed to clear payor eligSvc,");
            Assert.assertNull(accnPyr.getEligChkDt(), "Failed to clear payor eligChkDt,");
            Assert.assertEquals(accnPyr.getEligStaTypId(), EligMap.ELIG_STA_TYP_NOT_CHECKED, "Failed to clear payor eligStaTyp,");
        }
    }
    protected static boolean isOpenedErrsOnAccn(List<AccnPyrErr> accnPyrErrs)
    {
        boolean isOpenedErrsOnAccn = false;
        for (AccnPyrErr accnPyrErr : accnPyrErrs)
        {
            if(accnPyrErr.getFixDt().equals(null)){
                isOpenedErrsOnAccn= true;
            }
        }
        return isOpenedErrsOnAccn;
    }

    protected void verifyUnfixedUnbillableAccnPyrErrs(Set<String> errAbbrevs, Accn accn, AccnPyr accnPyr) throws XifinDataAccessException, XifinDataNotFoundException
    {
        List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accn.getAccnId(), accnPyr.getPyrPrio(), false);
        Assert.assertEquals(accnPyrErrs.size(), errAbbrevs.size(), "Unfixed unbillable error count not matched, accnId=" + accn.getAccnId() + ", expectedErrCnt=" + errAbbrevs.size() + ", actualErrCnt=" + accnPyrErrs.size());
        for (String errAbbrev : errAbbrevs)
        {
            List<AccnPyrErr> matchingAccnPyrErrs = new ArrayList<>();
            ErrCd errCd = rpmDao.getErrCd(testDb, errAbbrev, EpConstants.DNL_TBL_ID_UNBILL, accn.getDos());
            for (AccnPyrErr accnPyrErr : accnPyrErrs)
            {
                if (accnPyrErr.getErrCd() == errCd.getErrCd())
                {
                    matchingAccnPyrErrs.add(accnPyrErr);
                }
            }
            Assert.assertEquals(matchingAccnPyrErrs.size(), 1, "Unfixed unbillable error not matched, accnId=" + accn.getAccnId() + ", errAbbrev=" + errAbbrev + ", pyrPrio=" + accnPyr.getPyrPrio()+ ",");
        }
    }

    protected void cleanUpAccn(String accnId) throws Exception
    {
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);

        for (AccnEligHist accnEligHist : accnEligHists)
        {
            logger.info("Deleting XifinEligTransData, XifinEligTransLogRej, XifinEligTransLog, transId=" + accnEligHist.getTransId());
            daoManagerEligibilityWS.deleteXifinEligTransRecords(accnEligHist.getTransId());
        }
        logger.info("Deleting AccnEligHist records, accnId=" + accnId);
        accessionDao.deleteAccnEligHistByAccnId(accnId);
        logger.info("Deleting AccnPyrDel records, accnId=" + accnId);
        accessionDao.deleteAccnPyrDelByAccnId(accnId);
        for (AccnPyr accnPyr : accessionDao.getAccnPyrs(accnId))
        {
            logger.info("Clearing AccnPyr eligibility fields, accnId=" + accnId + ", pyrPrio=" + accnPyr.getPyrPrio() + ", pyrId=" + accnPyr.getPyrId());
            accnPyr.setEligChkDt(null);
            accnPyr.setEligChkDtAsString(null);
            accnPyr.setEligStaTypId(0);
            accnPyr.setEligStaTypDescr(null);
            accnPyr.setEligSvcName(null);
            accessionDao.setAccnPyr(accnPyr);
        }
        logger.info("Deleting all prior submissions, accnId=" + accnId);
        submissionDao.deleteSubmissions(rpmDao.getQAccnSubm(testDb, accnId));
        accessionDao.deleteAccnPyrDelByAccnId(accnId);
        logger.info("Deleting AccnCntct records, accnId=" + accnId);
        accessionDao.deleteAccnCntctByAccnId(accnId);
        logger.info("Deleting PtEstimation records, accnId=" + accnId);
        accessionDao.deletePtEstimationByAccnId(accnId);
    }

    protected void updateRpmEngines(List<Integer> taskTypIds, String interval, int concurrency) throws Exception
    {
        Set<String> taskTypAbbrvs = new TreeSet<>();
        for (int taskTypId : taskTypIds)
        {
            TaskTyp taskTyp = rpmDao.getTaskTyp(testDb, taskTypId);
            taskTypAbbrvs.add(taskTyp.getDescr());
        }

        logger.info("Updating RPM engines, tasks=" + taskTypAbbrvs + ", concurrency=" + concurrency);
        HeaderNavigation headerNavigation = new HeaderNavigation(driver, config);
        headerNavigation.fileMaintenanceTab();
        FileMaintenanceNavigation fileMaintenanceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenanceNavigation.taskSchedulerLink();
        TaskScheduler taskScheduler = new TaskScheduler(driver);
        switchToFrame(taskScheduler.contentFrameLocator());
        taskScheduler.setTaskSchedules(taskTypIds, "now", interval, concurrency);
        switchToDefaultWinFromFrame();
    }

    protected void loadAccession(String accnId) throws Exception
    {
        logger.info("Loading accession on Accn Detail, accnId=" + accnId);
        LoadAccession loadAccession = new LoadAccession(driver);
        loadAccession.setAccnId(accnId, wait);
    }

    protected boolean isOutOfPricingQueue(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime +=  startTime;
        boolean isInQueue = accessionDao.isInPricingQueue(accnId);
        if (isInQueue)
        {
            accessionDao.clearQPriceError(accnId);
        }
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

    public static class TestParameters
    {
        // System properties
        private String version;
        private String buildType;
        // Test parameters
        private String browserName;
        private String browserVersion;
        private String platform;
        private int port;
        private String hub;
        private int timeout;
        private String disableBrowserPlugins;
        private String ssoUsername;
        private String ssoPassword;
        private String accnId;

        public TestParameters()
        {
            this.version = System.getProperty("version");
            this.buildType = System.getProperty("buildType");
        }

        public String getVersion()
        {
            return version;
        }

        public void setVersion(String version)
        {
            this.version = version;
        }

        public String getBuildType()
        {
            return buildType;
        }

        public void setBuildType(String buildType)
        {
            this.buildType = buildType;
        }

        public String getBrowserName()
        {
            return browserName;
        }

        public void setBrowserName(String browserName)
        {
            this.browserName = browserName;
        }

        public String getBrowserVersion()
        {
            return browserVersion;
        }

        public void setBrowserVersion(String browserVersion)
        {
            this.browserVersion = browserVersion;
        }

        public String getPlatform()
        {
            return platform;
        }

        public void setPlatform(String platform)
        {
            this.platform = platform;
        }

        public int getPort()
        {
            return port;
        }

        public void setPort(int port)
        {
            this.port = port;
        }

        public String getHub()
        {
            return hub;
        }

        public void setHub(String hub)
        {
            this.hub = hub;
        }

        public int getTimeout()
        {
            return timeout;
        }

        public void setTimeout(int timeout)
        {
            this.timeout = timeout;
        }

        public String getDisableBrowserPlugins()
        {
            return disableBrowserPlugins;
        }

        public void setDisableBrowserPlugins(String disableBrowserPlugins)
        {
            this.disableBrowserPlugins = disableBrowserPlugins;
        }

        public String getSsoUsername()
        {
            return ssoUsername;
        }

        public void setSsoUsername(String ssoUsername)
        {
            this.ssoUsername = ssoUsername;
        }

        public String getSsoPassword()
        {
            return ssoPassword;
        }

        public void setSsoPassword(String ssoPassword)
        {
            this.ssoPassword = ssoPassword;
        }

        public String getAccnId()
        {
            return accnId;
        }

        public void setAccnId(String accnId)
        {
            this.accnId = accnId;
        }

    }
	public void updatePatientDemographics(String address1, String address2, String patientFirstName, String patientLastName, String genderValue, String zipCode, String city) throws Exception {
		
		AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
		accessionDetail.inputToPatientAddressLine(1, address1);
		accessionDetail.inputToPatientAddressLine(2, address2);
        accessionDetail.updatePatientFirstName(patientFirstName);
        accessionDetail.updatePatientLastName(patientLastName);
        accessionDetail.selectPatientGender(genderValue);
        accessionDetail.postalZipInput().sendKeys(zipCode);
        accessionDetail.postalCityInput().clear();
        accessionDetail.postalCityInput().sendKeys(city);
	}

	public void updateInsuredDemographics(String address1, String address2, String patientFirstName, String patientLastName, String genderValue, String zipCode, String city) throws Exception {
		
		AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
		accessionDetail.inputToInsuredAddressLine(1, address1);
        accessionDetail.inputToInsuredAddressLine(2, address2);
        accessionDetail.updateInsuredFirstName(patientFirstName);
        accessionDetail.updateInsuredLastName(patientLastName);
        accessionDetail.selectInsuredGender(genderValue);
        accessionDetail.updateInsuredZipCode(zipCode);
        accessionDetail.updateInsuredCity(city);
    }

    public List<AccnEligHist> waitForEligibilityCheck(String accnId)
            throws XifinDataAccessException, InterruptedException
    {
        long curTime, startTime = System.currentTimeMillis();
        long endTime = startTime + QUEUE_WAIT_TIME;
        while (accessionDao.getAccnEligHistByAccnId(accnId).isEmpty() && accessionDao.isInEligibilityQueue(accnId) && (curTime = System.currentTimeMillis()) < endTime)
        {
            logger.info("Waiting for eligibility check, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(curTime-startTime) + "s, maxTime=" + TimeUnit.MILLISECONDS.toSeconds(QUEUE_WAIT_TIME) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
        }
        return accessionDao.getAccnEligHistByAccnId(accnId);
    }

    protected void verifyEligTransLog(AccnEligHist accnEligHist) throws Exception
    {
        try
        {
            logger.info("Check Xifin_elig_trans_log record, transactionId=" + accnEligHist.getTransId());
            List<String> eligTransLog = daoManagerEligibilityWS.getEligTransHistFromXIFINELIGTRANSLOGByTransId(accnEligHist.getTransId());
            Assert.assertTrue(eligTransLog.size() > 0, "Eligibility transaction log can not be found,");
            Assert.assertTrue(eligTransLog.size() == 6, "Expected 6 columns in eligTransLog result,");
            logger.info("Check Xifin_elig_trans_data record, seqId=" + eligTransLog.get(5));
            List<String> eligTransData = daoManagerEligibilityWS.getEligTransDataBySeqId(Integer.valueOf(eligTransLog.get(5)));
            Assert.assertTrue(eligTransData.size() > 0, "Eligibility transaction data can not be found");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Assert.fail(e.getMessage(), e);
        }
    }

    protected void verifyEligTransLog(AccnEligHist accnEligHist, int eligStaTyp) throws Exception
    {
        try
        {
            logger.info("Check Xifin_elig_trans_log record, transactionId=" + accnEligHist.getTransId() + ", eligStaTyp=" + eligStaTyp);
            List<String> eligTransLog = daoManagerEligibilityWS.getEligTransHistFromXIFINELIGTRANSLOGByTransId(accnEligHist.getTransId());
            Assert.assertTrue(eligTransLog.size() > 0, "Eligibility transaction log can not be found,");
            Assert.assertTrue(eligTransLog.size() == 6, "Expected 6 columns in eligTransLog result,");
            Assert.assertEquals(Integer.parseInt(eligTransLog.get(4)), eligStaTyp, "Expected different elig status");
            logger.info("Check Xifin_elig_trans_data record, seqId=" + eligTransLog.get(5));
            List<String> eligTransData = daoManagerEligibilityWS.getEligTransDataBySeqId(Integer.valueOf(eligTransLog.get(5)));
            Assert.assertTrue(eligTransData.size() > 0, "Eligibility transaction data can not be found");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Assert.fail(e.getMessage(), e);
        }
    }

    protected void verifyEligTransLog(AccnEligHist accnEligHist, int eligStaTyp, String svcTypCd) throws Exception
    {
        try
        {
            logger.info("Check Xifin_elig_trans_log record, transactionId=" + accnEligHist.getTransId() + ", eligStaTyp=" + eligStaTyp + ", svcTyp=" + svcTypCd);
            List<String> eligTransLog = daoManagerEligibilityWS.getEligTransHistFromXIFINELIGTRANSLOGByTransId(accnEligHist.getTransId());
            Assert.assertTrue(eligTransLog.size() > 0, "Eligibility transaction log can not be found,");
            Assert.assertTrue(eligTransLog.size() == 6, "Expected 6 columns in eligTransLog result,");
            Assert.assertTrue(eligTransLog.get(3).contains(svcTypCd), "Expected different service type");
            Assert.assertEquals(Integer.parseInt(eligTransLog.get(4)), eligStaTyp, "Expected different elig status");
            logger.info("Check Xifin_elig_trans_data record, seqId=" + eligTransLog.get(5));
            List<String> eligTransData = daoManagerEligibilityWS.getEligTransDataBySeqId(Integer.valueOf(eligTransLog.get(5)));
            Assert.assertTrue(eligTransData.size() > 0, "Eligibility transaction data can not be found");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Assert.fail(e.getMessage(), e);
        }
    }
}
