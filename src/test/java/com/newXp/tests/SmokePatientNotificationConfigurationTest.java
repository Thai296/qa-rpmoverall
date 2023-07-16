package com.newXp.tests;

import com.mbasys.mars.ejb.entity.ptNotifLtrConfig.PtNotifLtrConfig;
import com.mbasys.mars.ejb.entity.ptNotifLtrConfigLnk.PtNotifLtrConfigLnk;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.PyrGrpMap;
import com.overall.fileMaintenance.fileMaintenanceTables.FileMaintPtNotificationLetterConfig;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.xap.utils.XifinAdminUtils;
import domain.fileMaintenance.PtNotificationConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertTrue;

public class SmokePatientNotificationConfigurationTest extends SeleniumBaseTest
{
    private RandomCharacter randomCharacter;
    private FileMaintPtNotificationLetterConfig fileMaintPtNotificationLetterConfig;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            cleanUpPtNotifLtrConfigLnkAndLink(testDb);
            new XifinAdminUtils(driver, config).clearDataCache();
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
    @Parameters({"ssoUsername", "ssoPassword"})
    public void beforeMethod(String ssoUsername, String ssoPassword, Method method)
    {
        try
        {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            logIntoSso(ssoUsername, ssoPassword);
        }
        catch (Exception e)
        {
            logger.error("Error running BeforeMethod", e);
        }
    }


    @Test(priority = 1, description = "Add Letter with All Inclusions")
    public void testXPR_1777() throws Exception
    {
        logger.info("Testing - testXPR_1777");
        fileMaintPtNotificationLetterConfig = new FileMaintPtNotificationLetterConfig(driver);
        randomCharacter = new RandomCharacter(driver);

        // First, load VOs and populate domain object
        logger.info("Create Data Value Object for the test");
        PtNotificationConfig config = new PtNotificationConfig();
        config.setPyr(rpmDao.getPyrByPyrGrpId(testDb, PyrGrpMap.MCAID_PYR_GRP));
        config.setPyrGrp(rpmDao.getPyrGrpByPyrGrpId(testDb, PyrGrpMap.MCAID_PYR_GRP));
        config.setLetterId("TEST1777" + randomCharacter.getRandomAlphaString(5));
        config.setClnAccntTyp(rpmDao.getClnAccntTypByDescr(testDb, MiscMap.CLN_ACCNT_TYP_CAPITATED_DESCR));
        config.setCln(rpmDao.getClnByClnAccntTyp(testDb, MiscMap.CLN_ACCNT_TYP_HOSPITAL_TYPE));
        config.setTaxonomy(rpmDao.getTaxonomy(testDb).get(0));
        config.setTestTyp(rpmDao.getTestTypByTestTypId(testDb, MiscMap.TEST_TYP_DNA));
        config.setTest(rpmDao.getTest(testDb));
        config.setClnFac(rpmDao.getFacByFacId(testDb, MiscMap.MAIN_FACILITY_FAC_ID));

        logger.info("Verify that the Patient Notification Letter Configuration page is displayed with correct page title");
        wait.until(ExpectedConditions.visibilityOf(fileMaintPtNotificationLetterConfig.ptNotificationConfigPageTitle()));
        Assert.assertEquals(fileMaintPtNotificationLetterConfig.ptNotificationConfigPageTitle().getText(), "Patient Notification Letter Configuration");
        logger.info("Count rows and columns in the Patient Notification Letter Configuration table");
        int rowCount = fileMaintPtNotificationLetterConfig.getRowNumInTable();
        logger.info("There are existing rows - " + rowCount);

        logger.info("Add a new Patient Notification Letter Configuration Row");
        fileMaintPtNotificationLetterConfig.ptNotifConfigAddBtn().click();
        wait.until(ExpectedConditions.elementToBeClickable(fileMaintPtNotificationLetterConfig.pyrGrpInclInput()));

        logger.info("Make sure New Record is blank");
        Assert.assertTrue(addRecordPtNotifConfigIsBlank());

        logger.info("Enter valid data in each compatible field");
        fileMaintPtNotificationLetterConfig.enterCriteriaForInclusion(config);

        Assert.assertTrue(fileMaintPtNotificationLetterConfig.oKBtn().isDisplayed());
        logger.info("Ok button is displayed");
        clickHiddenPageObject(fileMaintPtNotificationLetterConfig.oKBtn(), 0);

        logger.info("Verify that a new row is added to the Patient Notification Letter Configuration grid with correct data");
        assertTrue(isElementPresent(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCount + 1, 20), 5), "Letter ID should be displayed.");

        logger.info(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCount + 1, 20).getText());
        assertTrue(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCount + 1, 20).getText().equalsIgnoreCase(config.getLetterId()), "Letter ID " + config.getLetterId() + " should be displayed in the grid.");

        logger.info("Click on Save And Clear button");
        assertTrue(isElementPresent(fileMaintPtNotificationLetterConfig.saveAndClearBtn(), 5), "Save and Clear button should be displayed.");
        fileMaintPtNotificationLetterConfig.saveAndClearBtn().click();

        logger.info("Verify that new records are displayed properly");
        assertTrue(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCount + 1, 20).isDisplayed());

        logger.info("Verify that new records are created in DB.");
        wait.until(ExpectedConditions.elementToBeClickable(fileMaintPtNotificationLetterConfig.saveAndClearBtn()));

        logger.info("Getting actual Pt Notification Letter Config records from DB.");
        PtNotifLtrConfig actualPtNotifLtrConfig = rpmDao.getPtNotifLtrConfigByLetterId(testDb, config.getLetterId());
        List<PtNotifLtrConfigLnk> actualPtNotifLtrConfigLnks = rpmDao.getPtNotifLtrConfigByLnks(testDb, actualPtNotifLtrConfig.getSeqId());
        actualPtNotifLtrConfig.setAudUser(null);
        actualPtNotifLtrConfig.audDt = null;
        actualPtNotifLtrConfig.audRecId = 0;
        actualPtNotifLtrConfig.setSeqId(0);
        Assert.assertEquals(actualPtNotifLtrConfig, config.getPtNotifLtrConfig());

        for (PtNotifLtrConfigLnk lnk : actualPtNotifLtrConfigLnks)
        {
            lnk.setAudUser(null);
            lnk.audDt = null;
            lnk.audRecId = 0;
            lnk.setSeqId(0);
            lnk.setPtNotifLtrConfigSeqId(0);
        }
        List<PtNotifLtrConfigLnk> expectedPtNotifLtrConfigLinks = config.getPtNotifLtrConfigLnks(true);

        Assert.assertEquals(actualPtNotifLtrConfigLnks.size(), expectedPtNotifLtrConfigLinks.size());

        Set<PtNotifLtrConfigLnk> expectedSet = new HashSet<>(expectedPtNotifLtrConfigLinks);
        Set<PtNotifLtrConfigLnk> actualSet = new HashSet<>(actualPtNotifLtrConfigLnks);
        logger.info("Make sure actual and expected Lists of Pt Notif Letter Config Lnk are equal");
        Assert.assertEquals(actualSet, expectedSet);
    }

    @Test(priority = 1, description = "Add Letter with all Exclusions in it")
    public void testXPR_1778() throws Exception
    {
        logger.info("Testing - testXPR_1778");
        fileMaintPtNotificationLetterConfig = new FileMaintPtNotificationLetterConfig(driver);
        randomCharacter = new RandomCharacter(driver);

        // First, load VOs and populate domain object
        logger.info("Create Data Value Object for the test");
        PtNotificationConfig config = new PtNotificationConfig();
        config.setPyr(rpmDao.getPyrByPyrGrpId(testDb, PyrGrpMap.MCARE_PYR_GRP));
        config.setPyrGrp(rpmDao.getPyrGrpByPyrGrpId(testDb, PyrGrpMap.MCARE_PYR_GRP));
        config.setLetterId("TEST1778" + randomCharacter.getRandomAlphaString(5));
        config.setClnAccntTyp(rpmDao.getClnAccntTypByDescr(testDb, MiscMap.CLN_ACCNT_TYP_DIALYSIS_DESCR));
        config.setCln(rpmDao.getClnByClnAccntTyp(testDb, MiscMap.CLN_ACCNT_TYP_HOSPITAL_TYPE));
        config.setTaxonomy(rpmDao.getTaxonomy(testDb).get(0));
        config.setTestTyp(rpmDao.getTestTypByTestTypId(testDb, MiscMap.TEST_TYP_ONCOLOGY));
        config.setTest(rpmDao.getTest(testDb));
        config.setClnFac(rpmDao.getFacByFacId(testDb, MiscMap.MAIN_FACILITY_FAC_ID));

        logger.info("Verify that the Patient Notification Letter Configuration page is displayed with correct page title");
        wait.until(ExpectedConditions.visibilityOf(fileMaintPtNotificationLetterConfig.ptNotificationConfigPageTitle()));
        Assert.assertEquals(fileMaintPtNotificationLetterConfig.ptNotificationConfigPageTitle().getText(), "Patient Notification Letter Configuration");
        logger.info("Count rows and columns in the Patient Notification Letter Configuration table");
        int rowCount = fileMaintPtNotificationLetterConfig.getRowNumInTable();
        logger.info("There are existing rows - " + rowCount);

        logger.info("Add a new Patient Notification Letter Configuration Row");
        fileMaintPtNotificationLetterConfig.ptNotifConfigAddBtn().click();
        wait.until(ExpectedConditions.elementToBeClickable(fileMaintPtNotificationLetterConfig.pyrGrpInclInput()));

        logger.info("Make sure New Record is blank");
        Assert.assertTrue(addRecordPtNotifConfigIsBlank());

        logger.info("Enter valid data in each compatible field");
        fileMaintPtNotificationLetterConfig.enterCriteriaForExclusion(config);

        Assert.assertTrue(fileMaintPtNotificationLetterConfig.oKBtn().isDisplayed());
        logger.info("Ok button is displayed");
        clickHiddenPageObject(fileMaintPtNotificationLetterConfig.oKBtn(), 0);

        logger.info("Verify that a new row is added to the Patient Notification Letter Configuration grid with correct data");
        assertTrue(isElementPresent(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCount + 1, 20), 5), "Letter ID should be displayed.");

        logger.info(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCount + 1, 20).getText());
        assertTrue(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCount + 1, 20).getText().equalsIgnoreCase(config.getLetterId()), "Letter ID " + config.getLetterId() + " should be displayed in the grid.");

        logger.info("Click on Save And Clear button");
        assertTrue(isElementPresent(fileMaintPtNotificationLetterConfig.saveAndClearBtn(), 5), "Save and Clear button should be displayed.");
        fileMaintPtNotificationLetterConfig.saveAndClearBtn().click();

        logger.info("Verify that new records are displayed properly.");
        assertTrue(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCount + 1, 20).isDisplayed());

        logger.info("Verify that new records are created in DB.");
        wait.until(ExpectedConditions.elementToBeClickable(fileMaintPtNotificationLetterConfig.saveAndClearBtn()));

        logger.info("Getting actual Pt Notification Letter Config records from DB.");
        PtNotifLtrConfig actualPtNotifLtrConfig = rpmDao.getPtNotifLtrConfigByLetterId(testDb, config.getLetterId());
        List<PtNotifLtrConfigLnk> actualPtNotifLtrConfigLnks = rpmDao.getPtNotifLtrConfigByLnks(testDb, actualPtNotifLtrConfig.getSeqId());
        actualPtNotifLtrConfig.setAudUser(null);
        actualPtNotifLtrConfig.audDt = null;
        actualPtNotifLtrConfig.audRecId = 0;
        actualPtNotifLtrConfig.setSeqId(0);
        Assert.assertEquals(actualPtNotifLtrConfig, config.getPtNotifLtrConfig());

        for (PtNotifLtrConfigLnk lnk : actualPtNotifLtrConfigLnks)
        {
            lnk.setAudUser(null);
            lnk.audDt = null;
            lnk.audRecId = 0;
            lnk.setSeqId(0);
            lnk.setPtNotifLtrConfigSeqId(0);
        }
        List<PtNotifLtrConfigLnk> expectedPtNotifLtrConfigLinks = config.getPtNotifLtrConfigLnks(false);

        Assert.assertEquals(actualPtNotifLtrConfigLnks.size(), expectedPtNotifLtrConfigLinks.size());

        Set<PtNotifLtrConfigLnk> expectedSet = new HashSet<>(expectedPtNotifLtrConfigLinks);
        Set<PtNotifLtrConfigLnk> actualSet = new HashSet<>(actualPtNotifLtrConfigLnks);
        logger.info("Comparing actual and expected Lists of Pt Notif Letter Config Lnk");
        Assert.assertEquals(actualSet, expectedSet);
    }

    @Test(priority = 2, description = "Add and Delete Letter with all Exclusions in it")
    public void testXPR_1779() throws Exception
    {
        logger.info("Testing - testXPR_1779");
        fileMaintPtNotificationLetterConfig = new FileMaintPtNotificationLetterConfig(driver);
        randomCharacter = new RandomCharacter(driver);

        // First, load VOs and populate domain object
        logger.info("Create Data Value Object for the test");
        PtNotificationConfig config = new PtNotificationConfig();
        config.setPyr(rpmDao.getPyrByPyrGrpId(testDb, PyrGrpMap.MCARE_PYR_GRP));
        config.setPyrGrp(rpmDao.getPyrGrpByPyrGrpId(testDb, PyrGrpMap.MCARE_PYR_GRP));
        config.setLetterId("TEST1779" + randomCharacter.getRandomAlphaString(5));
        config.setClnAccntTyp(rpmDao.getClnAccntTypByDescr(testDb, MiscMap.CLN_ACCNT_TYP_DIALYSIS_DESCR));
        config.setCln(rpmDao.getClnByClnAccntTyp(testDb, MiscMap.CLN_ACCNT_TYP_HOSPITAL_TYPE));
        config.setTaxonomy(rpmDao.getTaxonomy(testDb).get(0));
        config.setTestTyp(rpmDao.getTestTypByTestTypId(testDb, MiscMap.TEST_TYP_ONCOLOGY));
        config.setTest(rpmDao.getTest(testDb));
        config.setClnFac(rpmDao.getFacByFacId(testDb, MiscMap.MAIN_FACILITY_FAC_ID));

        logger.info("Verify that the Patient Notification Letter Configuration page is displayed with correct page title");
        wait.until(ExpectedConditions.visibilityOf(fileMaintPtNotificationLetterConfig.ptNotificationConfigPageTitle()));
        Assert.assertEquals(fileMaintPtNotificationLetterConfig.ptNotificationConfigPageTitle().getText(), "Patient Notification Letter Configuration");
        logger.info("Count rows and columns in the Patient Notification Letter Configuration table");
        int rowCountBefore = fileMaintPtNotificationLetterConfig.getRowNumInTable();
        logger.info("There are existing rows - " + rowCountBefore);

        logger.info("Add a new Patient Notification Letter Configuration Row");
        fileMaintPtNotificationLetterConfig.ptNotifConfigAddBtn().click();
        wait.until(ExpectedConditions.elementToBeClickable(fileMaintPtNotificationLetterConfig.pyrGrpInclInput()));

        logger.info("Make sure New Record is blank");
        Assert.assertTrue(addRecordPtNotifConfigIsBlank());

        logger.info("Enter valid data in each compatible field");
        fileMaintPtNotificationLetterConfig.enterCriteriaForExclusion(config);

        Assert.assertTrue(fileMaintPtNotificationLetterConfig.oKBtn().isDisplayed());
        logger.info("Ok button is displayed");
        clickHiddenPageObject(fileMaintPtNotificationLetterConfig.oKBtn(), 0);

        logger.info("Verify that a new row is added to the Patient Notification Letter Configuration grid with correct data");
        assertTrue(isElementPresent(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCountBefore + 1, 20), 5), "Letter ID should be displayed.");

        logger.info(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCountBefore + 1, 20).getText());
        assertTrue(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCountBefore + 1, 20).getText().equalsIgnoreCase(config.getLetterId()), "Letter ID " + config.getLetterId() + " should be displayed in the grid.");

        logger.info("Click on Save And Clear button");
        assertTrue(isElementPresent(fileMaintPtNotificationLetterConfig.saveAndClearBtn(), 5), "Save and Clear button should be displayed.");
        fileMaintPtNotificationLetterConfig.saveAndClearBtn().click();

        logger.info("Verify that new records are displayed properly.");
        assertTrue(fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCountBefore + 1, 20).isDisplayed());

        logger.info("Verify that new records are created in DB.");
        wait.until(ExpectedConditions.elementToBeClickable(fileMaintPtNotificationLetterConfig.saveAndClearBtn()));

        logger.info("Getting actual Pt Notification Letter Config records from DB.");
        PtNotifLtrConfig actualPtNotifLtrConfig = rpmDao.getPtNotifLtrConfigByLetterId(testDb, config.getLetterId());
        List<PtNotifLtrConfigLnk> actualPtNotifLtrConfigLnks = rpmDao.getPtNotifLtrConfigByLnks(testDb, actualPtNotifLtrConfig.getSeqId());
        actualPtNotifLtrConfig.setAudUser(null);
        actualPtNotifLtrConfig.audDt = null;
        actualPtNotifLtrConfig.audRecId = 0;
        actualPtNotifLtrConfig.setSeqId(0);
        Assert.assertEquals(actualPtNotifLtrConfig, config.getPtNotifLtrConfig());

        for (PtNotifLtrConfigLnk lnk : actualPtNotifLtrConfigLnks)
        {
            lnk.setAudUser(null);
            lnk.audDt = null;
            lnk.audRecId = 0;
            lnk.setSeqId(0);
            lnk.setPtNotifLtrConfigSeqId(0);
        }
        List<PtNotifLtrConfigLnk> expectedPtNotifLtrConfigLinks = config.getPtNotifLtrConfigLnks(false);

        Assert.assertEquals(actualPtNotifLtrConfigLnks.size(), expectedPtNotifLtrConfigLinks.size());

        Set<PtNotifLtrConfigLnk> expectedSet = new HashSet<>(expectedPtNotifLtrConfigLinks);
        Set<PtNotifLtrConfigLnk> actualSet = new HashSet<>(actualPtNotifLtrConfigLnks);
        logger.info("Comparing actual and expected Lists of Pt Notif Letter Config Lnk");
        Assert.assertEquals(actualSet, expectedSet);

        logger.info("Click on the record " + fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCountBefore + 1, 20).getText());
        String letterIdToDel = fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCountBefore + 1, 20).getText();
        fileMaintPtNotificationLetterConfig.ptNotifTblCelData(rowCountBefore + 1, 20).click();
        fileMaintPtNotificationLetterConfig.ptNotifConfigEditBtn().click();
        wait.until(ExpectedConditions.elementToBeClickable(fileMaintPtNotificationLetterConfig.oKBtn()));
        fileMaintPtNotificationLetterConfig.deleteButton().click();
        logger.info("Ok button is displayed");
        clickHiddenPageObject(fileMaintPtNotificationLetterConfig.oKBtn(), 0);
        logger.info("Click on Save And Clear button");
        assertTrue(isElementPresent(fileMaintPtNotificationLetterConfig.saveAndClearBtn(), 5), "Save and Clear button should be displayed.");
        clickHiddenPageObject(fileMaintPtNotificationLetterConfig.saveAndClearBtn(), 0);

        logger.info("Make sure Patient Notification Letter Configuration table has 1 less record in it");
        int rowCountAfterDeleted = fileMaintPtNotificationLetterConfig.getRowNumInTable();
        logger.info("There are existing rows - " + rowCountAfterDeleted);
        Assert.assertEquals(rowCountBefore, rowCountAfterDeleted);

        logger.info("Make sure the records were deleted from DB");
        Assert.assertNull(rpmDao.getPtNotifLtrConfigByLetterId(testDb, letterIdToDel));
    }

    @Test(priority = 1, description = "Verify Help page on Pt Notification Letter")
    public void testXPR_1780() throws Exception
    {
        logger.info("Testing - testXPR_1780");
        fileMaintPtNotificationLetterConfig = new FileMaintPtNotificationLetterConfig(driver);
        randomCharacter = new RandomCharacter(driver);

        logger.info("Verify that the Patient Notification Letter Configuration page is displayed with correct page title");
        wait.until(ExpectedConditions.visibilityOf(fileMaintPtNotificationLetterConfig.ptNotificationConfigPageTitle()));
        Assert.assertEquals(fileMaintPtNotificationLetterConfig.ptNotificationConfigPageTitle().getText(), "Patient Notification Letter Configuration");


        logger.info("Click Help icon in the Header Pt Notification Letter grid");
        wait.until(ExpectedConditions.visibilityOf(fileMaintPtNotificationLetterConfig.helpIconInHeaderSection()));
        clickHiddenPageObject(fileMaintPtNotificationLetterConfig.helpIconInHeaderSection(), 0);

        logger.info("Verify that Help page in the Header Pt Notification Letter grid is opened properly");
        String parentWindow = switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_notification_letter_configuration_header.htm"));
        Assert.assertTrue(fileMaintPtNotificationLetterConfig.titleTextInHelp().getText().contains("Patient Notification Letter Configuration Header"));
        driver.close();
        switchToParentWin(parentWindow);

        logger.info("Verify that Help page in the Grid Pt Notification Letter grid is opened properly");
        Assert.assertTrue(isElementPresent(fileMaintPtNotificationLetterConfig.helpIconInPtNotificationLetterConfig(), 5));
        clickHiddenPageObject(fileMaintPtNotificationLetterConfig.helpIconInPtNotificationLetterConfig(), 0);
        parentWindow = switchToPopupWin();
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_notification_letter_configuration_main_grid.htm"));
        Assert.assertTrue(fileMaintPtNotificationLetterConfig.titleTextInHelp().getText().contains("Patient Notification Letter Configuration Grid"));
        driver.close();
        switchToParentWin(parentWindow);

        clickHiddenPageObject(fileMaintPtNotificationLetterConfig.resetBtn(), 0);
        wait.until(ExpectedConditions.elementToBeClickable(fileMaintPtNotificationLetterConfig.saveAndClearBtn()));
    }



    private boolean addRecordPtNotifConfigIsBlank()
    {
        try
        {
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.pyrGrpInclInput().getText(), "", "Payor Group Inclusions field should be empty.");
            Assert.assertTrue(fileMaintPtNotificationLetterConfig.pyrGrpExclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.pyrGrpExclInput().getText(), "", "Payor Group Exclusion field should be empty.");
            Assert.assertTrue(fileMaintPtNotificationLetterConfig.pyrInclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.pyrInclInput().getText(), "", "Payor Inclusions field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.pyrExclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.pyrExclInput().getText(), "", "Payor Exclusion field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.clnAcctTypInclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.clnAcctTypInclInput().getText(), "", "Client Account Type Inclusions field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.clnAcctTypExclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.clnAcctTypExclInput().getText(), "", "Client Account Type Exclusion field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.clnInclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.clnInclInput().getText(), "", "Client Inclusions field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.clnExclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.clnExclInput().getText(), "", "Client Exclusion field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.taxonomyCdInclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.taxonomyCdInclInput().getText(), "", "Taxonomy Code Inclusions field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.taxonomyCdExclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.taxonomyCdExclInput().getText(), "", "Taxonomy Code Exclusion field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.testTypInclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.testTypInclInput().getText(), "", "Test Type Inclusions field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.testTypExclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.testTypExclInput().getText(), "", "Test Type Exclusion field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.testInclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.testInclInput().getText(), "", "Test Inclusions field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.testExclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.testExclInput().getText(), "", "Test Exclusion field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.clnFacInclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.clnFacInclInput().getText(), "", "Client Facility Inclusions field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.clnFacExclInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.clnFacExclInput().getText(), "", "Client Facility Exclusion field should be empty.");

            Assert.assertTrue(fileMaintPtNotificationLetterConfig.letterIdInput().isDisplayed());
            Assert.assertEquals(fileMaintPtNotificationLetterConfig.letterIdInput().getText(), "", "Letter ID field should be empty.");
            return true;
        }
        catch (Exception e)
        {
            logger.info("Add Record on Patient Notification Letter Config is not blank");
            return false;
        }
    }

    private void cleanUpPtNotifLtrConfigLnkAndLink(String dbEnv) throws Exception
    {
        logger.info("Clearing Pt Notification Ltr Config");
        rpmDao.deletePtNotificationLtrConfigLnk(dbEnv);
        logger.info("Deleting Pt Notification Ltr Config");
        rpmDao.deletePtNotificationLtrConfig(dbEnv);
    }
}
