package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.ptDemoChk.PtDemoChk;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.qWriteOff.QWriteOff;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
import com.overall.menu.MenuNavigation;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;

import static com.mbasys.mars.persistance.SystemSettingMap.SS_AT_LEAST_ONE_PT_STMT;
import static com.mbasys.mars.persistance.SystemSettingMap.SS_CLN_STMT_OVERRIDE_MAIN_FAC_WITH_ORDERING_FAC;
import static org.testng.Assert.assertTrue;

public class WriteOffEngineTest extends SeleniumBaseTest {
    private static String errorNote = "|||Skip until at least one patient statement generated";

    @AfterSuite
    public void afterSuite() throws Exception{

        try {
            logger.info("**** After Suite ****");
            updateSystemSetting(SS_AT_LEAST_ONE_PT_STMT, "true","1");
        }catch (Exception e){
            Assert.fail("Error running AfterSuite", e);
        }finally {
            quitWebDriver();
        }

    }

    @Test(priority = 1, description = "Write Off Super Search")
    @Parameters({"project", "testSuite", "testCase","ssoUsername","ssoPassword"})
    public void WriteOffSuperSearch(String project, String testSuite, String testCase,String ssoUsername, String ssoPassword) throws Exception {
        MenuNavigation menuNavigation = new MenuNavigation(driver, config);
        SuperSearch superSearch = new SuperSearch(driver);
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 1 Actions - Create Accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("AccnID: " + accnId);
        String clnAbbrev = testProperties.getProperty("ClnAbbrev");

        logger.info("*** Step 1 Expected Results - New accession was generated");
        Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

        logger.info("*** If client id is 0 in accn table, update it to correct client id");
        Accn accn = accessionDao.getAccn(accnId);
        if((accn.getClnId()==0) || (accn.getPrimClnId()==0))
        {
            Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
            accessionDao.updateAccnSetClnIdAndPrimClnId(cln.clnId, accnId);
        }

        logger.info("*** Set Q_FR_PENDING for given accnId");
        if(accessionDao.isAccnInQueueByQTyp(accnId,AccnStatusMap.Q_FR_PENDING))
        {
            daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId,testDb);
        }

        logger.info("*** Step 2 Action - Run OE Posting Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "OePostingEngine", "SSO_APP_STAGING", false);
        Thread.sleep(5000);

        logger.info("*** Step 2 Expected Result - Ensure AccnId is processed by OePosting Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_FR_PENDING), " Accession is still in Q_FR_PENDING");

        logger.info("*** Step 3 Action - Run Pricing Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "PricingEngine", "SSO_APP_STAGING", true);
        Thread.sleep(5000);

        logger.info("*** Step 3 Expected Result - Accession is Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession is not Priced");

        logger.info("*** Run PtDemoSweeper Engine");
        List<PtDemoChk> ptDemoChks = patientDao.getPtDemoChksByPtSeqId(accn.getPtSeqId());
        if(!ptDemoChks.isEmpty()){
            xifinAdminUtils.runPFEngine(this,ssoUsername,ssoPassword,null,"PtDemoSweeperEngine", "SSO_APP_STAGING",true);
            Thread.sleep(5000);

            logger.info("*** Expected Result - Record was processed by the PtDemoSweeper Engine");
            ptDemoChks = patientDao.getPtDemoChksByPtSeqId(accn.getPtSeqId());
            Assert.assertTrue(ptDemoChks.get(0).getNumAccnsProcessed() > 0);
        }

        logger.info("*** Set Last Subm Date for SubmCvs to null using SubmSvcSeqId");
        int submSvcSeqId = daoManagerXifinRpm.getSubmSvcSeqIdByAccnId(accnId, testDb);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvcSeqId);

        logger.info("*** Step 4 Action - Run NonClnSubm Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword,null,"NonClnSubmEngine", "SSO_APP_STAGING",true);
        Thread.sleep(5000);

        logger.info("*** Step 4 Expected Result - Accession was processed by NonClnSubm Engine");
        List<QAccnSubm> qAccnSubmList = rpmDao.getQAccnSubm(null, accnId);
        boolean isQAccnSubmProcessed = false;
        int submFileSeqId = 0;
        for (QAccnSubm qAccnSubm : qAccnSubmList){
            if (qAccnSubm.getSubmFileSeqId() > 0) {
                submFileSeqId = qAccnSubm.getSubmFileSeqId();
                isQAccnSubmProcessed = true;
                break;
            }
        }
        Assert.assertTrue(isQAccnSubmProcessed, "QAccnSubm was not processed");


        logger.info("*** Step 5 Action - Run NonClnStatementEngine Engine");
        xifinAdminUtils.runPFEngine(this,ssoUsername,ssoPassword,null,"NonClnStmtEngine", "SSO_APP_STAGING",true);
        Thread.sleep(5000);

        logger.info("*** Step 5 Expected Result - Statement was generated");
        SubmFile submFile = submissionDao.getSubmFileBySubmFileSeqId(submFileSeqId);
        Assert.assertNotNull(submFile.getFilename(), " No file generated");

        logger.info("*** Step 6 Action - Write Off Accession using Accession Processing - Super Search");
        menuNavigation.navigateToSuperSearchPage();
        superSearch.writeOffAccn(this, accnId, wait);

        logger.info("*** Step 6 Expected Result - Accession is in Write Off Que");
        Assert.assertEquals(accessionDao.getCurrentAccnQueQTyp(accnId), AccnStatusMap.Q_WRITE_OFF);

        logger.info("*** Step 7 Action - Run Write Off Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword,null,"WriteOffEngine", "SSO_APP_STAGING",true);
        Thread.sleep(5000);

        logger.info("*** Step 7 Expected Results - Accession is out of Write Off Queue");
        Assert.assertNotEquals(accessionDao.getCurrentAccnQueQTyp(accnId), AccnStatusMap.Q_WRITE_OFF);

        logger.info("*** Step 7 Expected Results - Accession is in ACCN_ADJ");
        Assert.assertNotNull(accessionDao.getAccnAdjByAccnId(accnId), "Accession should appear in Accn_Adj");

        logger.info("*** Step 7 Expected Results - Accession status is ZBAL");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL);
    }

    @Test(priority = 1, description = "Verify Error Note")
    @Parameters({"project", "testSuite", "testCase","ssoUsername","ssoPassword"})
    public void writeOffErrorNote(String project, String testSuite, String testCase,String ssoUsername, String ssoPassword) throws Exception {
        MenuNavigation menuNavigation = new MenuNavigation(driver, config);
        SuperSearch superSearch = new SuperSearch(driver);
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 1 Actions - Create Accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("AccnID: " + accnId);
        String clnAbbrev = testProperties.getProperty("ClnAbbrev");

        logger.info("*** Step 1 Expected Results - New accession was generated");
        Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

        logger.info("*** If client id is 0 in accn table, update it to correct client id");
        Accn accn = accessionDao.getAccn(accnId);
        if((accn.getClnId()==0) || (accn.getPrimClnId()==0))
        {
            Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
            accessionDao.updateAccnSetClnIdAndPrimClnId(cln.clnId, accnId);
        }

        logger.info("*** Set Q_FR_PENDING for given accnId");
        if(accessionDao.isAccnInQueueByQTyp(accnId,AccnStatusMap.Q_FR_PENDING))
        {
            daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId,testDb);
        }

        logger.info("*** Step 2 Action - Run OE Posting Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "OePostingEngine", "SSO_APP_STAGING", false);
        Thread.sleep(5000);

        logger.info("*** Step 2 Expected Result - Ensure AccnId is processed by OePosting Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_FR_PENDING), " Accession is still in Q_FR_PENDING");

        logger.info("*** Step 3 Action - Run Pricing Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "PricingEngine", "SSO_APP_STAGING", true);
        Thread.sleep(5000);

        logger.info("*** Step 3 Expected Result - Accession is Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession is not Priced");

        logger.info("*** Step 4 Action - Write Off Accession using Accession Processing - Super Search");
        menuNavigation.navigateToSuperSearchPage();
        superSearch.writeOffAccn(this, accnId, wait);

        logger.info("*** Step 4 Expected Result - Accession is in Write Off Que");
        Assert.assertEquals(accessionDao.getCurrentAccnQueQTyp(accnId), AccnStatusMap.Q_WRITE_OFF);

        logger.info("*** Step 5 Action - Run Write Off Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword,null,"WriteOffEngine", "SSO_APP_STAGING",true);
        Thread.sleep(5000);

        logger.info("*** Step 5 Expected Results - Verify that the error note is displayed");
        QWriteOff qWriteOff = accessionDao.getQwriteOffByAccId(accnId);
        assertTrue(qWriteOff.getNote().contains(errorNote));
    }

    /**
     * Make sure this test is always the last test to run.
     * This test involves changing a system setting, in which relys on the afterSuite method to change that system setting back.
     * If another test is added after this one, the new test could be affected by the altered system setting.
     */
    @Test(priority = 1, description = "Verify Write Off Without Statment And SS24 Is False")
    @Parameters({"project", "testSuite", "testCase","ssoUsername","ssoPassword"})
    public void writeOffWithOutStatementAndSS24(String project, String testSuite, String testCase,String ssoUsername, String ssoPassword) throws Exception {
        MenuNavigation menuNavigation = new MenuNavigation(driver, config);
        SuperSearch superSearch = new SuperSearch(driver);
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 1 Actions - Create Accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("AccnID: " + accnId);
        String clnAbbrev = testProperties.getProperty("ClnAbbrev");

        logger.info("*** Step 1 Expected Results - New accession was generated");
        Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

        logger.info("*** If client id is 0 in accn table, update it to correct client id");
        Accn accn = accessionDao.getAccn(accnId);
        if((accn.getClnId()==0) || (accn.getPrimClnId()==0))
        {
            Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
            accessionDao.updateAccnSetClnIdAndPrimClnId(cln.clnId, accnId);
        }

        logger.info("*** Set Q_FR_PENDING for given accnId");
        if(accessionDao.isAccnInQueueByQTyp(accnId,AccnStatusMap.Q_FR_PENDING))
        {
            daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId,testDb);
        }

        logger.info("*** Step 2 Action - Run OE Posting Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "OePostingEngine", "SSO_APP_STAGING", false);
        Thread.sleep(5000);

        logger.info("*** Step 2 Expected Result - Ensure AccnId is processed by OePosting Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_FR_PENDING), " Accession is still in Q_FR_PENDING");

        logger.info("*** Step 3 Action - Run Pricing Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "PricingEngine", "SSO_APP_STAGING", true);
        Thread.sleep(5000);

        logger.info("*** Step 3 Expected Result - Accession is Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession is not Priced");

        logger.info("*** Step 4 Action - Write Off Accession using Accession Processing - Super Search");
        menuNavigation.navigateToSuperSearchPage();
        superSearch.writeOffAccn(this, accnId, wait);

        logger.info("*** Step 4 Expected Result - Accession is in Write Off Que");
        Assert.assertEquals(accessionDao.getCurrentAccnQueQTyp(accnId), AccnStatusMap.Q_WRITE_OFF);

        logger.info("*** Step 5 Set System Setting 24 to false");
        updateSystemSetting(SS_AT_LEAST_ONE_PT_STMT, "false","0");

        logger.info("*** Step 6 Action - Run Write Off Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword,null,"WriteOffEngine", "SSO_APP_STAGING",true);
        Thread.sleep(5000);

        logger.info("*** Step 6 Expected Results - Accession is out of Write Off Queue");
        Assert.assertNotEquals(accessionDao.getCurrentAccnQueQTyp(accnId), AccnStatusMap.Q_WRITE_OFF);

        logger.info("*** Step 6 Expected Results - Accession is in ACCN_ADJ");
        Assert.assertNotNull(accessionDao.getAccnAdjByAccnId(accnId), "Accession should appear in Accn_Adj");

        logger.info("*** Step 6 Expected Results - Accession status is ZBAL");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL);

    }

    }