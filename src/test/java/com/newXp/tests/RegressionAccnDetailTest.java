package com.newXp.tests;

import com.mbasys.mars.ejb.entity.accnClintrl.AccnClintrl;
import com.mbasys.mars.ejb.entity.accnCoc.AccnCoc;
import com.mbasys.mars.ejb.entity.accnOccurrence.AccnOccurrence;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnValueCode.AccnValueCode;
import com.mbasys.mars.ejb.entity.occurrenceCodeTyp.OccurrenceCodeTyp;
import com.mbasys.mars.ejb.entity.valueCodeTyp.ValueCodeTyp;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.LoadAccession;
import com.overall.accession.accessionProcessing.RecordPopUp;
import com.overall.menu.MenuNavigation;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;

import static org.testng.Assert.assertTrue;

public class RegressionAccnDetailTest extends SeleniumBaseTest {
    private AccessionDetail accessionDetail;
    private LoadAccession loadAccession;
    private RecordPopUp recordPopUp;
    private RandomCharacter randomCharacter;
    private TimeStamp timeStamp;
    private MenuNavigation navigation;
    private static final SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeMethod(alwaysRun = true)
    @Parameters({"email", "password"})
    public void beforeMethod(String email, String password, Method method) {
        try {
            logger.info("Running BeforeMethod");
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            logIntoSso(email, password);
            navigation = new MenuNavigation(driver, config);
            navigation.navigateToAccnDetailPage();
        } catch (SkipException e) {
            throw e;
        } catch (Exception e) {
            throw new SkipException("Error running BeforeMethod", e);
        }
    }

    @Test(priority = 1, description = "Log into RPM and load an existing accn in Detail and Reset")
    public void testXPR_1() throws Exception {
        logger.info("===== Testing - testXPR_1 =====");
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Detail page");
        assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5), "        The Accession Detail page title should be displayed");
        assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"), "        Page Title should be 'Detail'");

        logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
        //Get accnId from DB
        String accnId = accessionDao.getPricedAccession().getAccnId();
        Assert.assertTrue(isElementPresent(accessionDetail.accnIdInput(), 5), "        Accession ID input field should present.");
        loadAccession.setAccnId(accnId);

        //Wait until the page is loaded completely when Save button became clickable again
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

        logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 3 Actions: - Reset the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
        accessionDetail.clickReset();

        logger.info("*** Step 3 Expected Results: - Verify that it goes back to the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");
    }

    @Test(priority = 1, description = "Backout pricing by checking Reprice checkbox")
    @Parameters({"project", "testSuite", "testCase"})
    public void testXPR_4(String project, String testSuite, String testCase) throws Exception {
        logger.info("===== Testing - testXPR_4 =====");
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);

        logger.info("*** Step 1 Actions: - Create a new Priced accn with P payor");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);
        Assert.assertNotNull(accnId, "        No new accession was created");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS), "Accession is not out of Eligibility Queue");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");

        logger.info("*** Step 1 Expected Results: - A new Priced accn was created.");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Accession page");
        assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5), "        The Accession Detail page title should be displayed");
        assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"), "        Page Title should be 'Detail'");

        logger.info("*** Step 2 Actions: - Load the Priced accn and tab out");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");
        loadAccession.setAccnId(accnId);

        //Wait until the page is loaded completely when Save button became clickable again
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

        logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly in Detail page");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 3 Actions: - Check the Reprice checkbox and click Save button");
        accessionDetail.setReprice();
        accessionDetail.clickSave();

        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        //Ensure that the accn status was change to Final Reported in UI and DB
        logger.info("*** Step 3 Expected Results: - Verify that the Accession Status was changed to FnlRptd");
        Assert.assertTrue(accessionDetail.accessionStatusText().getText().contains("FnlRptd"), "        " + accessionDetail.accessionStatusText().getText() + " should match " + "FnlRptd.");

        logger.info("*** Step 3 Expected Results: - Verify that the accn status was changed to 11 in DB");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 11);

        logger.info("**** Step 4 Actions: - Reset the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
        accessionDetail.clickReset();

        logger.info("*** Step 5 Expected Results: - Verify that it goes back to the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");
    }

    @Test(priority = 1, description = "ValidationLogic-Payor field requirement error")
    @Parameters({"project", "testSuite", "testCase"})
    public void testXPR_6(String project, String testSuite, String testCase) throws Exception {
        logger.info("===== Testing - testXPR_6 =====");
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        navigation = new MenuNavigation(driver, config);

        logger.info("*** Step 1 Actions: - Create a new Priced accn with P payor");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);
        Assert.assertNotNull(accnId, "        No new accession was created");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS), "Accession is not out of Eligibility Queue");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");

        logger.info("*** Step 2 Actions: - In DB, add subs id payor requirement to the P payor");
        //pyrId = 4 (P payor), fldId = 4 (Subscriber #)
        int pyrReqFldRowCount = payorDao.getPyrReqmntsFldByPyrIdAndFldId(4, 4).size();
        int pyrReqRowCount = payorDao.getPyrReqmntsByPyrIdAndFldId(4, 4).size();
        //Delete the payor requirement if it exists
        accessionDetail.deletePayorReq(payorDao.getPyrByPyrId(4).getPyrAbbrv(), pyrReqFldRowCount, pyrReqRowCount);
        //Add payor requirement into PYR_REQMNTS
        pyrReqRowCount = daoManagerPlatform.setPyrReqmnsFromPYRREQMNTSByPyrIdAndEffDtAndFldIdAndPatternAndErrMsg(4, "01 jan 2002", 4, ".*", "Subs id is required", "01 jan 2002", testDb);

        logger.info("*** Step 2 Expected Results: - pyr requirement is added to PYR_REQMNTS table");
        Assert.assertEquals(pyrReqRowCount, 1);

        //Add payor requirement subs id field into PYR_REQMNTS_FLD
        logger.info("*** Step 2 Expected Results: - pyr field requirements are added to PYR_FLD_REQMNTS table");
        pyrReqFldRowCount = daoManagerPlatform.setPyrReqmnsFldFromPYRREQMNTSFLDByPyrIdAndEffDtAndFldIdAndScrnIdAndReqId(4, "01 jan 2002", 4, 1, 3, testDb);
        Assert.assertEquals(pyrReqFldRowCount, 1);
        pyrReqFldRowCount = daoManagerPlatform.setPyrReqmnsFldFromPYRREQMNTSFLDByPyrIdAndEffDtAndFldIdAndScrnIdAndReqId(4, "01 jan 2002", 4, 2, 3, testDb);
        Assert.assertEquals(pyrReqFldRowCount, 1);
        pyrReqFldRowCount = daoManagerPlatform.setPyrReqmnsFldFromPYRREQMNTSFLDByPyrIdAndEffDtAndFldIdAndScrnIdAndReqId(4, "01 jan 2002", 4, 3, 3, testDb);
        Assert.assertEquals(pyrReqFldRowCount, 1);
        pyrReqFldRowCount = daoManagerPlatform.setPyrReqmnsFldFromPYRREQMNTSFLDByPyrIdAndEffDtAndFldIdAndScrnIdAndReqId(4, "01 jan 2002", 4, 4, 3, testDb);
        Assert.assertEquals(pyrReqFldRowCount, 1);

        logger.info("*** Step 3 Actions: - Clear cache");
        xifinAdminUtils.clearDataCache();

        logger.info("**** Step 4 Actions: - Navigate to accn detail page");
        navigation.navigateToAccnDetailPage();
        //Go to main page

        logger.info("**** Step 4 Expected Results: - Verify that it's on the Accession Detail page");
        assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5), "        The Accession Detail page title should be displayed");
        assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"), "        Page Title should be 'Detail'");

        logger.info("*** Step 5 Actions: - Load the accn and tab out");
        loadAccession.setAccnId(accnId);
        //Wait until the page is loaded completely when Save button became clickable again
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

        logger.info("*** Step 5 Expected Results: - Verify that the accn is loaded properly");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 6 Actions: - Clear the subs id for the P payor and Save");
        accessionDetail.clearPrimaryPyrSubsId();
        accessionDetail.clickSave();
        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 6 Expected Results: - Verify that the SUBID payor error was added");
        Assert.assertTrue(accessionDetail.isColumnValueExist(accessionDetail.currentAccnErrTable(), "SUBID"), "        SUBID error should not exist in " + accessionDetail.currentAccnErrTable());

        logger.info("*** Step 7 Actions: - Save again to accept the error");
        //Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5));
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");
        accessionDetail.clickSave();
        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 7 Expected Results: Ensure the SUBID error is saved in DB");
        AccnPyrErr accnPyrErrList = errorProcessingDao.getUnfixedAccnPyrErrByAccnId(accnId);
        int errCd = accnPyrErrList.getErrCd();
        String reasonCode = errorProcessingDao.getErrCdByErrCdId(errCd).getAbbrev().trim() + "-" + errorProcessingDao.getErrCdByErrCdId(errCd).getShortDescr().trim();
        Assert.assertTrue(reasonCode.contains("SUBID"), "        " + errCd + " should be saved to accn " + accnId);

        logger.info("*** Step 8 Actions: - Add the subs id for the P payor and Save");
        Thread.sleep(2000);
        accessionDetail.setPrimaryPyrSubsId("A123456789");
        Thread.sleep(2000);
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");
        accessionDetail.clickSave();
        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 8 Expected Results: - Verify that the SUBID error was fixed in DB");
        Thread.sleep(2000);
        accnPyrErrList = errorProcessingDao.getUnfixedAccnPyrErrByAccnId(accnId);
        Assert.assertNull(accnPyrErrList, "        " + errCd + " was not fixed for accn " + accnId);

        logger.info("*** Step 8 Expected Results: - Verify that the SUBID error is NOT showing in UI");
        Assert.assertFalse(accessionDetail.isColumnValueExist(accessionDetail.currentAccnErrTable(), "SUBID"), "        SUBID error exists in " + accessionDetail.currentAccnErrTable());

        logger.info("*** Step 9 Actions: - Reset the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable");
        accessionDetail.clickReset();

        logger.info("*** Step 9 Expected Results: - Verify that it's back to the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");

        logger.info("*** Step 10 Actions: - In DB, remove the payor requirements for the P payor.");
        //Get pt payor abbrev
        String ptPyrAbbrev = payorDao.getPyrByPyrId(4).getPyrAbbrv();
        logger.info("*** Step 10 Expected Results: - Verify that the payor requirements were removed");
        payorDao.deleteFromPyrReqmntsFldByPyrAbbrev(ptPyrAbbrev);
        payorDao.deleteFromPyrReqmntsByPyrAbbrev(ptPyrAbbrev);

        logger.info("*** Step 10 Expected Results: - Cleared all Cache");
        xifinAdminUtils.clearDataCache();

    }

    @Test(priority = 1, description = "Accession Detail-Create a new EPI")
    public void testXPR_15() throws Exception {
        logger.info("===== Testing - testXPR_15 =====");
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);
        recordPopUp = new RecordPopUp(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");

        logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
        //Get accnId from DB
        String accnId = accessionDao.getPricedAccession().getAccnId();
        loadAccession.setAccnId(accnId);

        //Wait until the page is loaded completely when Save button became clickable again
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

        logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 3 Action: - Click the Create new EPI hyperlink and go to Create A New Patient EPI popup window");
        accessionDetail.clickCreateNewEPILink();

        //Switch to Create A New Patient EPI popup window
        switchToPopupWin();

        logger.info("*** Step 3 Expected Results: - Verify that it's on the Create A New Patient EPI popup window");
        Assert.assertTrue(recordPopUp.createNewPatientEPIDlg().isDisplayed(), "        Create A New Patient EPI popup window should present.");

        logger.info("*** Step 4 Action: - By default, select system generate an EPI option and click OK button");
        recordPopUp.clickOKInCreateNewPatientEPIDlg();

        logger.info("*** Step 4 Expected Results: - Verify that an EPI was generated in EPI entry field");
        String newEPI = accessionDetail.epiInput().getAttribute("value");
        Assert.assertNotNull(newEPI, "        A new EPI should be generated.");

        logger.info("*** Step 5 Action: - Copy the newly generated EPI and enter the same ID into Client Patient ID and Client's Primary Facility Patient ID fields and tab out");
        accessionDetail.setClientPatientId(newEPI);
        accessionDetail.setClientPrimaryFacilityPatientId(newEPI);

        logger.info("*** Step 6 Actions: - Click Save button");
        accessionDetail.clickSave();
        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save was not done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 6 Expected Results: - Verify that the values were displayed properly in UI");
        Assert.assertEquals(accessionDetail.epiInput().getAttribute("value"), newEPI);
        Assert.assertEquals(accessionDetail.clientPatientIdInput().getAttribute("value"), newEPI);
        Assert.assertEquals(accessionDetail.clientPrimaryFacilityPatientIdInput().getAttribute("value"), newEPI);

        logger.info("*** Step 6 Expected Results: - Verify that the values were saved properly in DB");
        //Ensure that the new EPI was saved to PT view
        Assert.assertEquals(patientDao.getPtV2ByAccnId(accnId).getEpi(), newEPI);

        String clnAbbrev = accessionDetail.clientIdText().getAttribute("value");
        //Ensure that the new EPI was saved to PT_CLN_LNK table
        Assert.assertEquals(patientDao.getPtClnLnkByEPIAndClnAbbrev(newEPI, clnAbbrev).getSpecificPtId(), newEPI);
        //Ensure that the new EPI was saved to PT_FAC_LNK table
        Assert.assertEquals(patientDao.getPtFacLnkByEPIAndClnAbbrev(newEPI, clnAbbrev).getSpecificPtId(), newEPI);

        logger.info("*** Step 7 Actions: - Reset the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable");
        accessionDetail.clickReset();

        logger.info("*** Step 7 Expected Results: - Verify that it goes back to the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");
    }

    @Test(priority = 1, description = "Add new non-primary accn payor")
    public void testXPR_20() throws Exception {
        logger.info("===== Testing - testXPR_20 =====");
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);
        recordPopUp = new RecordPopUp(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.accnIdInput(), 5), "        Accession ID input field should present.");

        logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
        //Get a Priced accnId with one payor from DB
        String accnId = daoManagerPlatform.getPricedAccnIdFromACCNWithOneThirdPartyPyr(testDb);
        accessionDetail.setAccnId(accnId);

        //Wait until the page is loaded completely when Save button became clickable again
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable");

        logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 3 Actions: - Click Add Payor hyperlink in Insurance Info grid");
        //In this case, the Add Payor link is not in the view
        clickHiddenPageObject(accessionDetail.addPayorLink(), 0);

        logger.info("*** Step 4 Actions: - Enter a P payor in the Add Payor popup window New Payor field and tab out and click Add button");
        String newPayorAbbrev = daoManagerPlatform.getPatientPayorAbbrev(testDb);
        //Pass the driver to Add Payor popup window
        recordPopUp.setNewPayor(newPayorAbbrev);
        recordPopUp.addBtnInAddPayorIDlg();

        logger.info("*** Step 4 Expected Results: - Verify that P payor was added as 2nd payor");
        Assert.assertEquals(accessionDetail.secondaryPayorIDInput().getAttribute("value"), newPayorAbbrev);

        logger.info("*** Step 5 Actions: - Enter a subs id and select Self Relationship for the P payor in the Insured Info grid and click Save button");
        String subsId = "A123456789";
        accessionDetail.setSecondaryPayorSubsId(subsId);
        //value="1" = "SELF"
        accessionDetail.setsecondaryPayorRelationship("1");

        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");
        accessionDetail.clickSave();

        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save was done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 5 Expected Results: - Verify that Patient Demographics Update message window shows");
        Assert.assertTrue(recordPopUp.patientDemoUpdateDlg().isDisplayed(), "        Patient Demographics Update dialog should be present");

        logger.info("*** Step 6 Actions: - Accept the default selection in Patient Demographics Update message window and click OK button");
        recordPopUp.clickOKInPatientDemoUpdateDlg();

        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 6 Expected Results: - Verify that P payor was saved to DB properly");
        Assert.assertEquals(daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 2, testDb), newPayorAbbrev);

        logger.info("*** Step 6 Expected Results: - Verify that P payor was displaying in UI properly");
        Assert.assertEquals(accessionDetail.secondaryPayorIDInput().getAttribute("value"), newPayorAbbrev);

        logger.info("*** Step 6 Expected Results: - Verify that the accn was not backout repriced.");
        Assert.assertTrue(accessionDetail.accessionStatusText().getText().contains("Priced"), "        " + accessionDetail.accessionStatusText().getText() + " should match " + "Priced.");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21);

        logger.info("*** Step 7 Actions: - Reset the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
        accessionDetail.clickReset();

        logger.info("*** Step 7 Expected Results: - Verify that it goes back to the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");

//        accessionDetail.setAccnId(accnId);
//
//        //Wait until the page is loaded completely when Save button became clickable again
//        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable");
//
//        clickHiddenPageObject(accessionDetail.deletePayorTab(2),5);
//        accessionDetail.clickSavdAndClearBtn();
//
//        logger.info("*** Step 5 Expected Results: - Verify that Patient Demographics Update message window shows");
//        Assert.assertTrue(recordPopUp.patientDemoUpdateDlg().isDisplayed(), "        Patient Demographics Update dialog should be present");
//
//        logger.info("*** Step 6 Actions: - Accept the default selection in Patient Demographics Update message window and click OK button");
//        recordPopUp.clickOKInPatientDemoUpdateDlg();
//
//        logger.info("*** Step 7 Expected Results: - Verify that it goes back to the Load Accession page");
//        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");
    }

    @Test(priority = 1, description = "Validation Logic for Medicare payors")
    public void testXPR_8() throws Exception {
        logger.info("===== Testing - testXPR_8 =====");
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);
        recordPopUp = new RecordPopUp(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.accnIdInput(), 5), "        Accession ID input field should present.");

        logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
        //Get a Priced accnId with one payor from DB
        String accnId = accessionDao.getAccnFromAccnPyrWithOnePyrByPyrGrpId(1).getAccnId();
        loadAccession.setAccnId(accnId);

        //Wait until the page is loaded completely when Save button became clickable again
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable");

        logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 3 Actions: - Click Add Payor hyperlink in Insurance Info grid");
        //In this case, the Add Payor link is not in the view
        clickHiddenPageObject(accessionDetail.addPayorLink(), 0);

        logger.info("*** Step 4 Actions: - Enter a new Medicare payor in the Add Payor popup window New Payor field and tab out and click Add button");
        String newMCPayorAbbrev = payorDao.getPyrFromPyrDtAndPyrByGrpId(1).getPyrAbbrv();
        //Pass the driver to Add Payor popup window
        recordPopUp.setNewPayor(newMCPayorAbbrev);
        recordPopUp.addBtnInAddPayorIDlg();

        logger.info("*** Step 4 Expected Results: - Verify that the new Medicare payor was added as 2nd payor");
        Assert.assertEquals(accessionDetail.secondaryPayorIDInput().getAttribute("value"), newMCPayorAbbrev);

        logger.info("*** Step 5 Actions: - Enter a subs id and select Self Relationship for the new Medicare payor in the Insured Info grid and click Save button");
        String subsId = "A123456789";
        accessionDetail.setSecondaryPayorSubsId(subsId);
        //value="1" = "SELF"
        accessionDetail.setsecondaryPayorRelationship("1");

        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");
        accessionDetail.clickSave();

        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");

        logger.info("*** Step 5 Expected Results: - Verify that an error - 'Only one non-system added Medicare payor is allowed.' was returned");
        Assert.assertTrue(accessionDetail.isErrorReturned(), "        An error should be returned.");
        Assert.assertTrue(accessionDetail.returnedErrorText().getText().contains("Only one non-system added Medicare payor is allowed"), "        Only one non-system added Medicare payor is allowed error message should show.");

        logger.info("*** Step 6 Actions: - Reset the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
        accessionDetail.clickReset();

        logger.info("*** Step 6 Expected Results: - Verify that it goes back to the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");
    }

    @Test(priority = 1, description = "Add Occurrence Codes and Value Codes")
    public void testXPR_11() throws Exception {
        logger.info("===== Testing - testXPR_11 =====");
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);
        recordPopUp = new RecordPopUp(driver, wait);
        timeStamp = new TimeStamp();
        randomCharacter = new RandomCharacter();

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.accnIdInput(), 5), "        Accession ID input field should present.");

        logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
        //Get a Priced accnId from DB
        String accnId = accessionDao.getAccnWithOnePyr().getAccnId();
        loadAccession.setAccnId(accnId);

        //Wait until the page is loaded completely when Save button became clickable again
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable");

        logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 3 Actions: - Click Add button in Occurrence Codes grid and select an Occurrence Code and click OK button in Add Record pop-up window.");
        //In this case, the Add button in Occurrence Codes grid is hidden
        clickHiddenPageObject(accessionDetail.occurrenceCodeAddBtn(), 0);

        //Ensure it's in the Add Record pop up window
        Assert.assertTrue(recordPopUp.occurrenceCodeDropdown().isDisplayed(), "        Occurrence Code - Description Drop down should present.");

        OccurrenceCodeTyp occurrenceCodeList = occurrenceDao.getOccurenceCodeTypByOccurenceTypId(8);
        int occurrenceCodeTypId = occurrenceCodeList.getOccurrenceCodeTypId();
        String occurrenceCodeAbbrev = occurrenceCodeList.getAbbrev().trim();
        String occurrenceCodeDescription = occurrenceCodeList.getAbbrev().trim() + " - " + occurrenceCodeList.getDescr().trim();

        //Select Occurrence Code from the dropdown
        recordPopUp.setOccurrenceCode(occurrenceCodeAbbrev);
        //Set Occurrence Date
        String currentDate = timeStamp.getCurrentDate();
        recordPopUp.setOccurrenceDate(currentDate);
        //Ensure the default Payor ID is ALL PAYORS
        Assert.assertEquals(recordPopUp.occurrenceCodePyrIdInput().getAttribute("value"), "ALL PAYORS");

        recordPopUp.clickOK();

        logger.info("*** Step 3 Expected Results: - Verify that the Occurrence Code was displaying in the Occurrence Codes table.");
        //Occurrence Code (col=3) and Date (col=4) in occurrenceCodeTable
        accessionDetail.getRowNumberInWebTable(accessionDetail.occurrenceCodesTable(), "tbl_occurrenceCodes", occurrenceCodeDescription, currentDate, 3, 4);

        logger.info("*** Step 4 Actions: - Click Add button in Value Codes grid and select a Value Code and enter a Value Amount and click OK button in Add Record pop-up window.");
        clickHiddenPageObject(accessionDetail.valueCodeAddBtn(), 0);

        Assert.assertTrue(recordPopUp.valueCodeDropdown().isDisplayed(), "        Value Code - Description Drop down should present.");

        ValueCodeTyp valueCodeList = valueCodeDao.getValueCodeTpByValueCodeTypId(5);
        int valueCodeTypId = valueCodeList.getValueCodeTypId();
        String valueCodeAbbrev = valueCodeList.getAbbrev().trim();
        String valueCodeDescription = valueCodeList.getAbbrev().trim() + " - " + valueCodeList.getDescr().trim();
        //Select Value Code from the dropdown
        recordPopUp.setValueCode(valueCodeAbbrev);
        //Set Value Code
        String valueAmount = randomCharacter.getRandomAlphaNumericString(5);
        recordPopUp.setValueAmount(valueAmount);
        //Ensure the default Payor ID is ALL PAYORS
        Assert.assertEquals(recordPopUp.valueCodePyrIdInput().getAttribute("value"), "ALL PAYORS");

        recordPopUp.clickOK();

        logger.info("*** Step 4 Expected Results: - Verify that the Value Code was displaying in the Value Codes table.");
        //Value Code (col=3) and Value Amount (col=4) in valueCodeTable
        accessionDetail.getRowNumberInWebTable(accessionDetail.valueCodesTable(), "tbl_valueCodes", valueCodeDescription, valueAmount, 3, 4);

        logger.info("*** Step 5 Actions: - Click Save.");
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");
        accessionDetail.clickSave();

        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save was done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 5 Expected Results: - Verify that Occurrence and Value Codes are saved in DB.");
        AccnOccurrence occurrenceCodeListInDB = accessionDao.getAccnOccurrenceByAccnId(accnId);
        Assert.assertEquals(occurrenceCodeListInDB.getOccurrenceCodeTypId(), occurrenceCodeTypId);
        Assert.assertEquals(occurrenceCodeListInDB.getOccurrenceDt(), new Date(DATE_FORMAT_YYYYMMDD.parse(timeStamp.getCurrentDate("yyyy-MM-dd")).getTime()));
        Assert.assertEquals(occurrenceCodeListInDB.getSpecificPyrId(), 0);
        int accnOccurrenceSeqId = occurrenceCodeListInDB.getSeqId();

        AccnValueCode valueCodeListInDB = accessionDao.getAccnValueCodeByAccnId(accnId);
        Assert.assertEquals(valueCodeListInDB.getValueCodeTypId(), valueCodeTypId);
        Assert.assertEquals(valueCodeListInDB.getValueAmount(), valueAmount);
        Assert.assertEquals(valueCodeListInDB.getSpecificPyrId(), 0);
        int accnValueCodeSeqId = valueCodeListInDB.getSeqId();

        logger.info("*** Step 5 Expected Results: - Verify that Occurrence and Value Codes are displaying in the Occurrence and Value Codes tables.");
        accessionDetail.getRowNumberInWebTable(accessionDetail.occurrenceCodesTable(), "tbl_occurrenceCodes", occurrenceCodeDescription, currentDate, 3, 4);
        accessionDetail.getRowNumberInWebTable(accessionDetail.valueCodesTable(), "tbl_valueCodes", valueCodeDescription, valueAmount, 3, 4);

        logger.info("*** Step 6 Actions: - Check Delete for an Occurrence Code in the Occurrence Codes table.");
        int rowNum = accessionDetail.getRowNumberInWebTable(accessionDetail.occurrenceCodesTable(), "tbl_occurrenceCodes", occurrenceCodeDescription, currentDate, 3, 4);
        accessionDetail.checkOccurrenceCodeDeleteCheckbox(rowNum - 2);

        logger.info("*** Step 7 Actions: - Check Delete for a Value Code in the Value Codes table.");
        rowNum = accessionDetail.getRowNumberInWebTable(accessionDetail.valueCodesTable(), "tbl_valueCodes", valueCodeDescription, valueAmount, 3, 4);
        accessionDetail.checkValueCodeDeleteCheckbox(rowNum - 2);

        logger.info("*** Step 8 Actions: - Click Save button.");
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");
        accessionDetail.clickSave();

        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save was done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 8 Expected Results: - Verify that the Occurrence and Value Codes were deleted in DB.");
        Assert.assertNull(accessionDao.getAccnOccurrenceBySeqId(accnOccurrenceSeqId));
        Assert.assertNull(accessionDao.getAccnValueCodeBySeqId(accnValueCodeSeqId));

        logger.info("*** Step 8 Expected Results: - Verify that the Occurrence and Value Codes were not displaying in UI.");
        Assert.assertEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.occurrenceCodesTable(), "tbl_occurrenceCodes", occurrenceCodeDescription, currentDate, 3, 4), 0);
        Assert.assertEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.valueCodesTable(), "tbl_valueCodes", valueCodeDescription, valueAmount, 3, 4), 0);

        logger.info("*** Step 9 Actions: - Reset the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
        accessionDetail.clickReset();

        logger.info("*** Step 9 Expected Results: - Verify that it goes back to the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");
    }

    @Test(priority = 1, description = "Xifin Portal - Accession-Detail-Add Employer info in Chain of Custody grid")
    @Parameters({"project", "testSuite", "testCase"})
    public void testXPR_13(String project, String testSuite, String testCase) throws Exception {
        logger.info("===== Testing - testXPR_13 =====");

        accessionDetail = new AccessionDetail(driver, config, wait);
        loadAccession = new LoadAccession(driver);
        randomCharacter = new RandomCharacter();

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
        assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5), "        The Accession Detail page title should be displayed");
        assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"), "        Page Title should be 'Detail'");

        logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
        Properties resultProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = resultProperties.getProperty("NewAccnID");
        loadAccession.setAccnId(accnId);
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");    //Wait until the page is loaded completely when Save button became clickable again

        logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 3 Action: - Click the Chain Of Custody grid to extend the grid");
        scrollToElement(accessionDetail.currentChainOfCustodyCurrentView());
        clickHiddenPageObject(accessionDetail.currentChainOfCustodyCurrentView(), 0);

        logger.info("*** Step 3 Expected Results: - Verify that the chain Of Custody grid is in Detail View");
        Assert.assertEquals(accessionDetail.currentChainOfCustodyCurrentView().getAttribute("title"), "DETAIL view");

        logger.info("*** Step 4 Actions: - Add Employer and Contact Info ");
        String employerName = "AUTOEMP" + randomCharacter.getRandomAlphaString(8);
        String contactName = "AUTOCONTACT" + randomCharacter.getRandomAlphaString(8);
        accessionDetail.setEmployerName(employerName);
        accessionDetail.setContactName(contactName);

        logger.info("*** Step 5 Actions: - Click Save button");
        accessionDetail.clickSave();
        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 5 Expected Results: - Verify that Chain of Custody id saved in DB");
        AccnCoc accnCoc = accessionDao.getAccnCocByAccnId(accnId);
        Assert.assertEquals(accnCoc.getEmplyrNm(), employerName);
        Assert.assertEquals(accnCoc.getEmplyrCntct(), contactName);

        logger.info("*** Step 5 Expected Results: - Verify that the Employer info was displayed in CoC Summary view");
        while (!accessionDetail.currentChainOfCustodyCurrentView().getAttribute("title").equals("SUMMARY view"))
            clickHiddenPageObject(accessionDetail.currentChainOfCustodyCurrentView(), 0);
        Assert.assertEquals(accessionDetail.cocEmployerNameSummaryView().getText(), employerName);
        Assert.assertEquals(accessionDetail.cocContactNameSummaryView().getText(), contactName);

        logger.info("*** Step 6 Actions: - Reset the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable");
        accessionDetail.clickReset();

        logger.info("*** Step 6 Expected Results: - Verify that it goes back to the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");

    }

    @Test(priority = 1, description = "Add Clinical Trial")
    @Parameters({"project", "testSuite", "testCase"})
    public void testXPR_17(String project, String testSuite, String testCase) throws Exception {
        logger.info("===== Testing - testXPR_17 =====");

        accessionDetail = new AccessionDetail(driver, config, wait);
        loadAccession = new LoadAccession(driver);
        randomCharacter = new RandomCharacter();
        timeStamp = new TimeStamp();

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
        assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5), "        The Accession Detail page title should be displayed");
        assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"), "        Page Title should be 'Detail'");

        logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
        Properties resultProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = resultProperties.getProperty("NewAccnID");
        loadAccession.setAccnId(accnId);
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");    //Wait until the page is loaded completely when Save button became clickable again

        logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 3 Action: - Click the Clinical Trial grid to extend the grid");
        scrollToElement(accessionDetail.currentClinicalTrialView());
        clickHiddenPageObject(accessionDetail.currentClinicalTrialView(), 0);

        logger.info("*** Step 3 Expected Results: - Verify that the Clinical Trial grid is in Detail View");
        Assert.assertEquals(accessionDetail.currentClinicalTrialView().getAttribute("title"), "DETAIL view");

        logger.info("*** Step 4 Actions: - Add Trial Name, Encounter Id and Encounter Date ");
        String trialName = "AUTOTRIAL" + randomCharacter.getRandomAlphaString(8);
        String encounterId = "AUTOENCOUNTER" + randomCharacter.getRandomAlphaString(8);
        String encounterDate = timeStamp.getCurrentDate();
        accessionDetail.setTrialName(trialName);
        accessionDetail.setEncounterId(encounterId);
        accessionDetail.setEncounterDate(encounterDate);

        logger.info("*** Step 5 Actions: - Click Save button");
        accessionDetail.clickSave();
        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 5 Expected Results: - Verify that Clinical Trial is saved in DB");
        AccnClintrl accnClintrl = accessionDao.getAccnClinTrialByAccnId(accnId);
        Assert.assertEquals(accnClintrl.getClintrlNm(), trialName);
        Assert.assertEquals(accnClintrl.getClintrlEncntr(), encounterId);

        logger.info("*** Step 5 Expected Results: - Verify that the Trial Name was displayed in Clinical Trial Summary view");
        while (!accessionDetail.currentClinicalTrialView().getAttribute("title").equals("SUMMARY view"))
            clickHiddenPageObject(accessionDetail.currentClinicalTrialView(), 0);
        Assert.assertEquals(accessionDetail.clinTrilTrialNameSummaryView().getText(), trialName);

        logger.info("*** Step 6 Actions: - Reset the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable");
        accessionDetail.clickReset();

        logger.info("*** Step 6 Expected Results: - Verify that it goes back to the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");

    }

    @Test(priority = 1, description = "Xifin Portal - Accession-Detail-Add Employer info in Chain of Custody grid")
    @Parameters({"project", "testSuite", "testCase"})
    public void testXPR_24(String project, String testSuite, String testCase) throws Exception {
        logger.info("===== Testing - testXPR_13 =====");

        accessionDetail = new AccessionDetail(driver, config, wait);
        loadAccession = new LoadAccession(driver);
        randomCharacter = new RandomCharacter();

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
        assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5), "        The Accession Detail page title should be displayed");
        assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"), "        Page Title should be 'Detail'");

        logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
        Properties resultProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = resultProperties.getProperty("NewAccnID");
        loadAccession.setAccnId(accnId);
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");    //Wait until the page is loaded completely when Save button became clickable again

        logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 3 Action: - Click the Chain Of Custody grid to extend the grid");
        scrollToElement(accessionDetail.currentChainOfCustodyCurrentView());
        clickHiddenPageObject(accessionDetail.currentChainOfCustodyCurrentView(), 0);

        logger.info("*** Step 3 Expected Results: - Verify that the chain Of Custody grid is in Detail View");
        Assert.assertEquals(accessionDetail.currentChainOfCustodyCurrentView().getAttribute("title"), "DETAIL view");

        logger.info("*** Step 4 Actions: - Add Employer and Contact Info ");
        String employerName = "AUTOEMP" + randomCharacter.getRandomAlphaString(8);
        String contactName = "AUTOCONTACT" + randomCharacter.getRandomAlphaString(8);
        accessionDetail.setEmployerName(employerName);
        accessionDetail.setContactName(contactName);

        logger.info("*** Step 5 Actions: - Click Save button");
        accessionDetail.clickSave();
        Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Step 5 Expected Results: - Verify that Chain of Custody id saved in DB");
        AccnCoc accnCoc = accessionDao.getAccnCocByAccnId(accnId);
        Assert.assertEquals(accnCoc.getEmplyrNm(), employerName);
        Assert.assertEquals(accnCoc.getEmplyrCntct(), contactName);

        logger.info("*** Step 5 Expected Results: - Verify that the Employer info was displayed in CoC Summary view");
        while (!accessionDetail.currentChainOfCustodyCurrentView().getAttribute("title").equals("SUMMARY view"))
            clickHiddenPageObject(accessionDetail.currentChainOfCustodyCurrentView(), 0);
        Assert.assertEquals(accessionDetail.cocEmployerNameSummaryView().getText(), employerName);
        Assert.assertEquals(accessionDetail.cocContactNameSummaryView().getText(), contactName);

        logger.info("*** Step 6 Actions: - Reset the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable");
        accessionDetail.clickReset();

        logger.info("*** Step 6 Expected Results: - Verify that it goes back to the Load Accession page");
        Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(), 5), "        Accession ID input field should present.");

    }

    @Test(priority = 1, description = "Verify Helps")
    public void testXPR_32() throws Exception {
        logger.info("===== Testing - testXPR_32 =====");
        accessionDetail = new AccessionDetail(driver, config, wait);
        loadAccession = new LoadAccession(driver);
        randomCharacter = new RandomCharacter();

        timeStamp = new TimeStamp(driver);

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
        assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5), "        The Accession Detail page title should be displayed");
        assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"), "        Page Title should be 'Detail'");

        logger.info("*** Step 1 Expected Results: - Verify that it's on the Detail page");
        Assert.assertTrue(isElementPresent(accessionDetail.accnIdInput(), 5), "        Accession ID input field should present.");

        logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
        //Get a Priced accnId from DB
        String accnId = accessionDao.getRandomDataFromACCN().getAccnId();
        loadAccession.setAccnId(accnId);

        //Wait until the page is loaded completely when Save button became clickable again
        Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable");

        logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

        logger.info("*** Action: - Click Help icon in Accession Errors section");
        clickHiddenPageObject(accessionDetail.helpIconOnAccnErrSection(), 0);

        logger.info("*** Expected Results: - Help page for Accession Error is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_errors.htm", "Accession Detail – Accession Errors"));

        logger.info("*** Action: - Click Help icon in Contact Note section");
        clickHiddenPageObject(accessionDetail.helpIconOnSectionNotesSection(), 0);

        logger.info("*** Expected Results: - Help page for Contact Note is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_contact_notes.htm", "Accession Detail – Contact Notes"));

        logger.info("*** Action: - Click Help icon in Physician Info section");
        clickHiddenPageObject(accessionDetail.helpIconOnPhysicianInfoSection(), 0);

        logger.info("*** Expected Results: - Help page for Physician Info is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_physician_info.htm", "Accession Detail – Physician Info"));

        logger.info("*** Action: - Click Help icon in Phlebotomist Info section");
        clickHiddenPageObject(accessionDetail.helpIconOnPhlebInfoSection(), 0);

        logger.info("*** Expected Results: - Help page for Phlebotomist Info is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_phlebotomist_info.htm", "Accession Detail – Phlebotomist Info"));

        logger.info("*** Action: - Click Help icon in Patient Info section");
        clickHiddenPageObject(accessionDetail.helpIconOnPatientInfoSection(), 0);

        logger.info("*** Expected Results: - Help page for Patient Info is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_patient_info.htm", "Accession Detail – Patient Info"));

        logger.info("*** Action: - Click Help icon in Current Diagnosis section");
        clickHiddenPageObject(accessionDetail.helpIconOnDiagnosisSection(), 0);

        logger.info("*** Expected Results: - Help page for Current Diagnosis is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_current_diagnoses.htm", "Accession Detail – Current Diagnoses"));

        logger.info("*** Action: - Click Help icon in Insurance Info section");
        clickHiddenPageObject(accessionDetail.helpIconOnInsuranceInfoSection(), 0);

        logger.info("*** Expected Results: - Help page for Insurance Ifo is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_insurance_info.htm", "Accession Detail – Insurance Info"));

        logger.info("*** Action: - Click Help icon in Insurance Info - Payor Info section");
        clickHiddenPageObject(accessionDetail.helpIconOnInsuranceInfoSectionPayorInfo(), 0);

        logger.info("*** Expected Results: - Help page for Insurance Info - Payor Info is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_ins_info_payor_info.htm", "Accession Detail – Insurance Info – Payor Info"));

        logger.info("*** Action: - Click Help icon in Insurance Info - Insured Info section");
        clickHiddenPageObject(accessionDetail.helpIconOnInsuranceInfoSectionInsuredInfo(), 0);

        logger.info("*** Expected Results: - Help page for Insurance Info - Insured Info is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_ins_info_insd_info.htm", "Accession Detail – Insurance Info – Insured Info"));

        logger.info("*** Action: - Click Help icon in Insurance Info - Payor Note Info section");
        clickHiddenPageObject(accessionDetail.helpIconOnInsuranceInfoSectionPayorNoteInfo(), 0);

        logger.info("*** Expected Results: - Help page for Insurance Info - Payor Note Info is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_ins_info_payor_notes.htm", "Accession Detail – Insurance Info – Payor Notes"));

        logger.info("*** Action: - Click Help icon in Insurance Info - Employer Info section");
        clickHiddenPageObject(accessionDetail.helpIconOnInsuranceInfoSectionEmployeeInfo(), 0);

        logger.info("*** Expected Results: - Help page for Insurance Info - Employer Info is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_ins_info_emp_info.htm", "Accession Detail – Insurance Info – Employer Info"));

        logger.info("*** Action: - Click Help icon in Occurrence Code and Value Code section");
        clickHiddenPageObject(accessionDetail.helpIconOnOccurrenceAndValuesSection(), 0);

        logger.info("*** Expected Results: - Help page for Occurrence Code and Value Code is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_occurrence_codes.htm", "Accession Detail – Occurrence Codes and Value Codes"));

        logger.info("*** Action: - Click Help icon in Eligibility Transaction section");
        clickHiddenPageObject(accessionDetail.helpIconOnEligibilityTransactionsSection(), 0);

        logger.info("*** Expected Results: - Help page for Eligibility Transaction is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_eligibility_transactions.htm", "Accession Detail – Eligibility Transactions"));

        logger.info("*** Action: - Click Help icon in Claim Status Transaction section");
        clickHiddenPageObject(accessionDetail.helpIconOnClaimsStatusHistorySection(), 0);

        logger.info("*** Expected Results: - Help page for Claim Status Transaction is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_claims_status.htm", "Accession Detail – Claim Status History"));

        logger.info("*** Action: - Click Help icon in Submit Claims section");
        clickHiddenPageObject(accessionDetail.helpIconOnSubmitClaimsSection(), 0);

        logger.info("*** Expected Results: - Help page for Submit Claims is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_submit_claims.htm", "Accession Detail – Submit Claims"));

        logger.info("*** Action: - Click Help icon in Ordered Tests section");
        clickHiddenPageObject(accessionDetail.helpIconOnOrderedTestsSection(), 0);

        logger.info("*** Expected Results: - Help page for Ordered Tests is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_ordered_tests.htm", "Accession Detail – Ordered Tests"));

        logger.info("*** Action: - Click Help icon in Accession Level Pricing section");
        clickHiddenPageObject(accessionDetail.helpIconOnAccnLevelPricingSection(), 0);

        logger.info("*** Expected Results: - Help page for Accession Level Pricing is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_accession_pricing_remarks.htm", "Accession Detail – Accession Pricing Remarks"));

        logger.info("*** Action: - Click Help icon in Consolidation section");
        clickHiddenPageObject(accessionDetail.helpIconOnConsolidationSection(), 0);

        logger.info("*** Expected Results: - Help page for Consolidation is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_consolidation.htm", "Accession Detail – Consolidation"));

        logger.info("*** Action: - Click Help icon in Billable Procedure Code and Bulk Transaction Detail section");
        clickHiddenPageObject(accessionDetail.helpIconOnBillableProcedureCodeSection(), 0);

        logger.info("*** Expected Results: - Help page for Billable Procedure Code and Bulk Transaction Detail is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_billable_procedure_code.htm", "Accession Detail – Billable Procedure Code and Bulk Transaction Detail"));

        logger.info("*** Action: - Click Help icon in Client Specific Question section");
        clickHiddenPageObject(accessionDetail.helpIconOnClnSpecQuestionSection(), 0);

        logger.info("*** Expected Results: - Help page for Client Specific Question is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_client_specific_questions.htm", "Accession Detail – Client Specific Questions"));

        logger.info("*** Action: - Click Help icon in Clinical Trial section");
        clickHiddenPageObject(accessionDetail.helpIconOnClinicalTrialSection(), 0);

        logger.info("*** Expected Results: - Help page for Clinical Trial is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_clinical_trial.htm", "Accession Detail – Clinical Trial"));

        logger.info("*** Action: - Click Help icon in Chain Of Custody section");
        clickHiddenPageObject(accessionDetail.helpIconOnChainOfCustodySection(), 0);

        logger.info("*** Expected Results: - Help page for Chain Of Custody is displayed with correct link");
        assertTrue(verifyHelpPageDisplayed("help/accession_tab/accession_processing_menu/p_accession_chain_of_custody.htm", "Accession Detail – Chain of Custody"));
    }
}
