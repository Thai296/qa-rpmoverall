package com.newXp.tests;

import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.filemaint.logoConfiguration.LogoConfiguration;
import com.overall.menu.MenuNavigation;
import com.overall.utils.LogoConfigurationUtil;
import com.overall.utils.XifinPortalUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import domain.filemaint.logoConfiguration.AppealLetterLogo;
import domain.filemaint.logoConfiguration.LogoConfigurationHeader;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.LocalFileDetector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;

import static com.mbasys.mars.persistance.SystemSettingMap.SS_CLN_STMT_OVERRIDE_MAIN_FAC_WITH_ORDERING_FAC;
import static org.testng.Assert.*;

public class LogoConfigurationTest extends SeleniumBaseTest {

    private LogoConfiguration logoConfiguration;
    private XifinPortalUtils xifinPortalUtils;
    private LogoConfigurationUtil logoConfigurationUtil;
    private String fileToDelete;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"ssoXpUsername", "ssoXpPassword"})
    public void beforeMethod(String ssoXpUsername, String ssoXpPassword, Method method) {
        try {
            logoConfigurationUtil = new LogoConfigurationUtil(driver, wait, config);
            driver.setFileDetector(new LocalFileDetector());
            xifinPortalUtils = new XifinPortalUtils(driver);
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            logIntoSso(ssoXpUsername, ssoXpPassword);
            MenuNavigation menuNavigation = new MenuNavigation(driver, config);
            menuNavigation.navigateToLogoConfigurationPage();
        } catch (Exception e) {
            logger.error("Error running BeforeMethod", e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            logger.info("Running AfterMethod");
            if (null != logoConfigurationUtil.localPathFolder) {
                logoConfigurationUtil.deleteFile(fileToDelete);
                logoConfigurationUtil.deleteFile(logoConfigurationUtil.localPathFolder);
            }
        } catch (Exception e) {
            Assert.fail("Error running AfterMethod", e);
        } finally {
            quitWebDriver();
        }
    }

    @Test(priority = 1, description = "Annual Disclosure letter - Update Logo")
    public void annualDisclosureLetterUpdateLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);

        logger.info("*** Step 1 Expected Result: User login successful");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Action: Select document type : Annual Disclosure letter. Input with valid Facility ID");
        String docTyp = LogoConfigurationUtil.ANNUAL_DISCLOSURE_LETTER;
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(docTyp, null);

        logger.info("*** Step 2 Expected Result: Logo Configuration page is displayed with Document type - Annual Disclosure Letter");
        xifinPortalUtils.waitForPageLoaded(wait);
        LogoConfigurationHeader actualHeader = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(actualHeader.getDocumentType(), expectedHeader.getDocumentType(), "        Logo Configuration page is not displayed correctly");

        logger.info("*** Step 3 Action: Check on Update Logo radio btn: "
                + "Select logo from Available Logos select box,"
                + "Update logo Location,"
                + "Update logo size");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadLogo();
        String uploadFile = appealLetterLogo.getAvailableLogos();
        navigateRefresh();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        logoConfigurationUtil.setValuesLogoConfiguration(docTyp, expectedHeader.getFac());
        logoConfigurationUtil.updateLogo(appealLetterLogo, docTyp);

        logger.info("*** Step 3 Expected Result: Date Uploaded field is updated");
        String newDateInfo = logoConfiguration.dateUploadInput().getAttribute("value");
        assertNotEquals(newDateInfo, appealLetterLogo.getDateUploaded(), "        Date Uploaded field is not updated.");

        logger.info("*** Step 4 Action: Click on Save and Clear btn, reload Annual Disclosure Letter");
        logoConfigurationUtil.clickOnSaveAndClearBtn();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        logoConfigurationUtil.setValuesLogoConfiguration(docTyp, expectedHeader.getFac());

        logger.info("*** Step 4 Expected Result: Current Logo in header menu is displayed correctly "
                + "- The SS#1721 is updated based on new logo,"
                + "- The SS#1723 is updated based on new logo location,"
                + "- The SS#1722 is updated based on new logo size");
        xifinPortalUtils.waitForPageLoaded(wait);
        actualHeader = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(actualHeader.getDocumentType(), expectedHeader.getDocumentType(), "        Logo Configuration page is not displayed correctly");
        logoConfigurationUtil.verifyUpdateLogoSizeLocation(SystemSettingMap.SS_CLN_LETTER_ANNUAL_DISCLOSURE_LOGO, SystemSettingMap.SS_CLN_LETTER_ANNUAL_DISCLOSURE_LOGO_SIZE, SystemSettingMap.SS_CLN_LETTER_ANNUAL_DISCLOSURE_LOGO_LOCATION, appealLetterLogo);

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + uploadFile;
    }

    @Test(priority = 1, description = "Annual Disclosure letter - Upload Logo")
    public void annualDisclosureLetterUploadLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);
        Fac fac = facilityDao.getRandomFac();

        logger.info("*** Step 1 Expected Result: User login is successful");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Action: Select document type : Annual Disclosure letter. Input with valid Facility ID");
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(LogoConfigurationUtil.ANNUAL_DISCLOSURE_LETTER, fac);

        logger.info("*** Step 2 Expected Result: Logo Configuration page is displayed with Document type - Annual Disclosure Letter.");
        xifinPortalUtils.waitForPageLoaded(wait);
        LogoConfigurationHeader actualHeader = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(actualHeader.getDocumentType(), expectedHeader.getDocumentType(), "        Logo Configuration page is not displayed correctly");

        logger.info("*** Step 3 Action: Click on Upload Radio Button: Select any image");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadTempLogo();

        logger.info("*** Step 3 Expected Result: Image name is displayed next to Upload Logo btn"
                + "- The image is saved on folder");
        logoConfigurationUtil.verifyImageIsSavedAtTempRoot(appealLetterLogo);

        logger.info("*** Step 4 Action: Select Update logo");
        clickHiddenPageObject(logoConfiguration.updateLogoRad(), 0);

        logger.info("*** Step 4 Expected Result: New image is shown at the bottom of the list");
        logoConfigurationUtil.checkMalwareScanningIsDisplayed();
        String name = logoConfiguration.availableLogosDdl().getText().toLowerCase();
        assertEquals(name, appealLetterLogo.getAvailableLogos().toLowerCase(), "        New image is not shown in the bottom of the list.");

        logger.info("*** Step 5 Action: Click on Save and Clear btn, reload Annual Disclosure Letter again.");
        logoConfigurationUtil.clickOnSaveAndClearBtn();
        navigateRefresh();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.setValuesLogoConfiguration(LogoConfigurationUtil.ANNUAL_DISCLOSURE_LETTER, expectedHeader.getFac());

        logger.info("*** Step 5 Expected Result: Current Logo in header menu is displayed correctly "
                + "- The selected logo is displayed in Current System Logos table "
                + "- New logo image is saved in folder");
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);
        logoConfigurationUtil.verifyImageIsSavedAtRoot(appealLetterLogo);

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + appealLetterLogo.getAvailableLogos();
    }

    @Test(priority = 1, description = "Annual Disclosure letter - Delete logo file")
    public void annualDisclosureLetterDeleteLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);

        logger.info("*** Step 1 Expected Result: User login is successful");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Action: Select document type: Annual Disclosure letter. Input with valid Facility ID");
        String docTyp = LogoConfigurationUtil.ANNUAL_DISCLOSURE_LETTER;
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(docTyp, null);

        logger.info("*** Step 2 Expected Result: Logo Configuration page is displayed with Document type - Annual Disclosure Letter");
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 3 Action: Select any available logo from the dropdown list: click on Delete file.");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadTempLogo();
        clickHiddenPageObject(logoConfiguration.updateLogoRad(), 0);
        logoConfigurationUtil.checkMalwareScanningIsDisplayed();
        boolean isExist = false;
        String logoImgs = logoConfiguration.logoLocationTxtInDropdown().getText().trim();
        logger.info("Logo is selected : " + logoImgs);
        logger.info("Appeal letter logo : " + appealLetterLogo.getAvailableLogos());
        if (logoImgs.equalsIgnoreCase(appealLetterLogo.getAvailableLogos())) {
            isExist = true;
        }
        assertTrue(isExist, "        New image is not shown at the bottom of the list.");
        logoConfigurationUtil.deleteLogoFileFromDdl();

        logger.info("*** Step 3 Expected Result: The selected file is removed from the list");
        xifinPortalUtils.waitForPageLoaded(wait);
        isExist = logoConfigurationUtil.checkExistInAvailableLogos(logoConfiguration.availableLogosDdl(), appealLetterLogo.getAvailableLogos());
        assertFalse(isExist, "        The selected file was not removed from the list.");
        clickHiddenPageObject(logoConfiguration.saveAndClearBtn(), 0);
        logoConfigurationUtil.deleteFile(appealLetterLogo.getDir() + File.separator + appealLetterLogo.getAvailableLogos());

        docTyp = LogoConfigurationUtil.APPEAL_LETTER;
        logger.info("*** Step 4 Action: Reload with Document type is " + docTyp);
        expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(docTyp, null);

        logger.info("*** Step 4 Expected Result: Logo Configuration page is displayed with Document type: " + docTyp);
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 5 Action: Select any available logo from the dropdown list: click on Delete file");
        appealLetterLogo = logoConfigurationUtil.uploadTempLogo();
        clickHiddenPageObject(logoConfiguration.updateLogoRad(), 0);
        logoConfigurationUtil.checkMalwareScanningIsDisplayed();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoImgs = logoConfiguration.logoLocationTxtInDropdown().getText().trim();
        logger.info("Logo is selected : " + logoImgs);
        logger.info("Appeal letter logo : " + appealLetterLogo.getAvailableLogos());
        if (logoImgs.equalsIgnoreCase(appealLetterLogo.getAvailableLogos())) {
            isExist = true;
        }
        assertTrue(isExist, "        New image is not shown at the bottom of the list.");
        logoConfigurationUtil.deleteLogoFileFromDdl();

        logger.info("*** Step 5 Expected Result: The selected file is removed from the list");
        xifinPortalUtils.waitForPageLoaded(wait);
        isExist = logoConfigurationUtil.checkExistInAvailableLogos(logoConfiguration.availableLogosDdl(), appealLetterLogo.getAvailableLogos());
        assertFalse(isExist, "        The selected file was not removed");
        clickHiddenPageObject(logoConfiguration.saveAndClearBtn(), 0);
        logoConfigurationUtil.deleteFile(appealLetterLogo.getDir() + File.separator + appealLetterLogo.getAvailableLogos());

        docTyp = LogoConfigurationUtil.CLIENT_STATEMENT;
        logger.info("*** Step 6 Action: Reload a Document type: " + docTyp);
        expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(docTyp, null);

        logger.info("*** Step 6 Expected Result: Logo Configuration page is displayed with Document type:" + docTyp);
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 7 Action: Select any available logo from the dropdown list. Click on Delete file.");
        appealLetterLogo = logoConfigurationUtil.uploadTempLogo();
        clickHiddenPageObject(logoConfiguration.updateLogoRad(), 0);
        logoConfigurationUtil.checkMalwareScanningIsDisplayed();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoImgs = logoConfiguration.logoLocationTxtInDropdown().getText().trim();
        logger.info("Logo is selected : " + logoImgs);
        logger.info("Appeal letter logo : " + appealLetterLogo.getAvailableLogos());
        if (logoImgs.equalsIgnoreCase(appealLetterLogo.getAvailableLogos())) {
            isExist = true;
        }
        assertTrue(isExist, "        New image is not shown at the bottom of the list.");
        logoConfigurationUtil.deleteLogoFileFromDdl();

        logger.info("*** Step 7 Expected Result: The selected file is removed from the list");
        xifinPortalUtils.waitForPageLoaded(wait);
        isExist = logoConfigurationUtil.checkExistInAvailableLogos(logoConfiguration.availableLogosDdl(), appealLetterLogo.getAvailableLogos());
        assertFalse(isExist, "        The selected file was not removed from the list");
        clickHiddenPageObject(logoConfiguration.saveAndClearBtn(), 0);
        logoConfigurationUtil.deleteFile(appealLetterLogo.getDir() + File.separator + appealLetterLogo.getAvailableLogos());

        docTyp = LogoConfigurationUtil.EP_LETTER;
        logger.info("*** Step 8 Action: Reload a Document type: " + docTyp);
        navigateRefresh();
        xifinPortalUtils.waitForPageLoaded(wait);
        expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(docTyp, null);

        logger.info("*** Step 8 Expected Result: Logo Configuration page is displayed with Document type: " + docTyp);
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 9 Action: Select any available logo from the dropdown list. Click on Delete file");
        appealLetterLogo = logoConfigurationUtil.uploadTempLogo();
        clickHiddenPageObject(logoConfiguration.updateLogoRad(), 0);
        logoConfigurationUtil.checkMalwareScanningIsDisplayed();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoImgs = logoConfiguration.logoLocationTxtInDropdown().getText().trim();
        logger.info("Logo is selected : " + logoImgs);
        logger.info("Appeal letter logo : " + appealLetterLogo.getAvailableLogos());
        if (logoImgs.equalsIgnoreCase(appealLetterLogo.getAvailableLogos())) {
            isExist = true;
        }
        assertTrue(isExist, "        New image is not shown at the bottom of the list");
        logoConfigurationUtil.deleteLogoFileFromDdl();
        xifinPortalUtils.waitForPageLoaded(wait);

        logger.info("*** Step 9 Expected Result: The selected file is removed from the list");
        isExist = logoConfigurationUtil.checkExistInAvailableLogos(logoConfiguration.availableLogosDdl(), appealLetterLogo.getAvailableLogos());
        assertFalse(isExist, "        The selected file was not removed from the list");
        clickHiddenPageObject(logoConfiguration.saveAndClearBtn(), 0);
        logoConfigurationUtil.deleteFile(appealLetterLogo.getDir() + File.separator + appealLetterLogo.getAvailableLogos());

        docTyp = LogoConfigurationUtil.WELCOME_LETTER;
        logger.info("*** Step 10 Action: Reload Document type: " + docTyp);
        navigateRefresh();
        xifinPortalUtils.waitForPageLoaded(wait);
        expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(docTyp, null);

        logger.info("*** Step 10 Expected Result: Logo Configuration page is displayed with Document type - " + docTyp);
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 11 Action: Select any available logo from the dropdown list. Click on Delete file");
        appealLetterLogo = logoConfigurationUtil.uploadTempLogo();
        assertTrue(isElementPresent(logoConfiguration.updateLogoRad(), 5), "        Update Logo Rad is not displayed");
        clickHiddenPageObject(logoConfiguration.updateLogoRad(), 0);
        logoConfigurationUtil.checkMalwareScanningIsDisplayed();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoImgs = logoConfiguration.logoLocationTxtInDropdown().getText().trim();
        logger.info("Logo is selected : " + logoImgs);
        logger.info("Appeal letter logo : " + appealLetterLogo.getAvailableLogos());
        if (logoImgs.equalsIgnoreCase(appealLetterLogo.getAvailableLogos())) {
            isExist = true;
        }
        assertTrue(isExist, "        New image is not shown at the bottom of the list");
        logoConfigurationUtil.deleteLogoFileFromDdl();

        logger.info("*** Step 11 Expected Result: The selected file is removed from the list");
        xifinPortalUtils.waitForPageLoaded(wait);
        isExist = logoConfigurationUtil.checkExistInAvailableLogos(logoConfiguration.availableLogosDdl(), appealLetterLogo.getAvailableLogos());
        assertFalse(isExist, "        The selected file was not removed from the list");
        clickHiddenPageObject(logoConfiguration.saveAndClearBtn(), 0);

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + appealLetterLogo.getAvailableLogos();
    }

    @Test(priority = 1, description = "Appeal Letter - Update logo.")
    public void appealLetterUpdateLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);

        logger.info("*** Step 1 Expected Result: User login is successful");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Action: Select document type: Appeal letter. Input with valid Facility ID");
        String docType = LogoConfigurationUtil.APPEAL_LETTER;
        Fac fac = facilityDao.getFacByMainFacAndRmtMainFac(MiscMap.FAC_TYP_MAIN_LAB, MiscMap.FAC_TYP_REMOTE_AFFIL);
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(docType, fac);

        logger.info("*** Step 2 Expected Result: Logo Configuration page is displayed with Document type - Appeal Letter.");
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 3 Action: Check on Update Logo radio btn: "
                + "Select logo from Available Logos select box, "
                + "Update logo Location, "
                + "Update logo size.");
        String oldDate = logoConfiguration.dateUploadInput().getAttribute("value");
        logger.info("Date info before upload Logo" + oldDate);
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadLogo();
        String uploadFile = appealLetterLogo.getAvailableLogos();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.setValuesLogoConfiguration(docType, fac);
        assertTrue(isElementPresent(logoConfiguration.dateUploadInput(), 5), "        Date Upload Input is not displayed.");
        String newDateInfo = logoConfiguration.dateUploadInput().getAttribute("value");
        logoConfigurationUtil.updateLogo(appealLetterLogo, docType);

        logger.info("*** Step 3 Expected Result: Date Uploaded field is updated");
        assertNotEquals(newDateInfo, oldDate, "        Date Uploaded field was not updated");

        logger.info("*** Step 4 Action: Click on Save and Clear btn, reload Appeal letter");
        logoConfigurationUtil.clickOnSaveAndClearBtn();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        logoConfigurationUtil.setValuesLogoConfiguration(docType, fac);

        logger.info("*** Step 4 Expected Results: Current Logo in Header menu is displayed correctly."
                + "The SS#1751 is updated based on new logo, "
                + "The SS#1753 is updated based on new logo location, "
                + "The SS#1752 is updated based on new logo size");
        xifinPortalUtils.waitForPageLoaded(wait);
        LogoConfigurationHeader header = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(header.getDocumentType(), expectedHeader.getDocumentType(), "        Document Type is not displayed correctly.");
        logoConfigurationUtil.verifyLogoSizeLocation(fac.getFacId(), 0, SystemSettingMap.SS_APPEAL_LTR_LOGO_SIZE, SystemSettingMap.SS_APPEAL_LTR_LOGO_LOCATION, appealLetterLogo);

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + uploadFile;
    }

    @Test(priority = 1, description = "Appeal Letter - Upload logo")
    public void appealLetterUploadLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);

        logger.info("*** Step 1 Expected Results: User login is successful. Logo Configuration page is displayed");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Actions: Select document type: Appeal Letter.");
        String documentTyp = LogoConfigurationUtil.APPEAL_LETTER;
        Fac fac = facilityDao.getFacByMainFacAndRmtMainFac(MiscMap.FAC_TYP_MAIN_LAB, MiscMap.FAC_TYP_REMOTE_AFFIL);

        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(documentTyp, fac);

        logger.info("*** Step 2 Expected Results: Logo Configuration detail page is displayed with Document type - Appeal Letter");
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 3 Actions: Check on Upload Logo radio button. Select any image");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadTempLogo();
        String uploadFile = appealLetterLogo.getAvailableLogos();

        logger.info("*** Step 3 Expected Results: Image name is displayed next to Upload Logo button | The image is saved in folder");
        assertEquals(logoConfiguration.uploadNameFileText().getText(), appealLetterLogo.getAvailableLogos(), "        Image name is not displayed next to Upload Logo btn");
        logoConfigurationUtil.verifyImageIsSavedAtTempRoot(appealLetterLogo);

        logger.info("*** Step 4 Actions: Select Update logo");
        clickHiddenPageObject(logoConfiguration.updateLogoRad(), 0);
        logoConfigurationUtil.checkMalwareScanningIsDisplayed();

        logger.info("*** Step 4 Expected Results: New image is shown at the bottom of the list");
        assertTrue(logoConfiguration.availableLogosDdl().isDisplayed());
        String[] arrOfStr = appealLetterLogo.getAvailableLogos().split("\\.(?=[^.]+$)");
        String logo = arrOfStr.length > 1 ? (arrOfStr[0].toUpperCase() + "." + arrOfStr[1]) : LogoConfigurationUtil.EMPTY;
        assertTrue(xifinPortalUtils.checkElementIsAvailable(logoConfiguration.logoConfigSelectedLogoName(logo)), "        New image is not displayed at the bottom of the list");

        logger.info("*** Step 5 Actions: Click Save and Clear button and Reload Appeal letter");
        logoConfigurationUtil.clickOnSaveAndClearBtn();
        logoConfigurationUtil.setValuesLogoConfiguration(documentTyp, expectedHeader.getFac());

        logger.info("*** Step 5 Expected Results: Current Logo in header menu is displayed correctly."
                + "Fac.Cln_Stmt_Logo is updated based on new Logo image |New logo image is saved in folder "
                + "and the image is removed from the temp folder.");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        LogoConfigurationHeader header = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(header.getDocumentType(), expectedHeader.getDocumentType(), "        Document Type is not displayed correctly.");
        logoConfigurationUtil.verifyImageIsSavedAtRoot(appealLetterLogo);

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + uploadFile;
    }

    @Test(priority = 1, description = "Client Statement - Update logo")
    public void clientStatementUpdateLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);
        updateSystemSetting(SS_CLN_STMT_OVERRIDE_MAIN_FAC_WITH_ORDERING_FAC, "1","1");

        logger.info("*** Step 1 Expected result: User login successful and Logo Configuration screen is displayed");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Action: Select document type: Client Statement. Select valid FacId");
        Fac fac = facilityDao.getFacByMainFacAndRmtMainFac(MiscMap.FAC_TYP_MAIN_LAB, MiscMap.FAC_TYP_REMOTE_AFFIL);

        String documentTyp = LogoConfigurationUtil.CLIENT_STATEMENT;
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(documentTyp, fac);

        logger.info("*** Step 2 Expected result: Logo Configuration page is displayed with Document type - Client Statement.");
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 3 Action: Check on Update Logo radio btn: select logo from Available Logos select box. Update Logo size");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadLogo();
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        String uploadFile = appealLetterLogo.getAvailableLogos();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.setValuesLogoConfiguration(documentTyp, fac);
        logoConfigurationUtil.updateLogo(appealLetterLogo, documentTyp);

        logger.info("*** Step 3 Expected result: Date Uploaded field is updated");
        String newDateInfo = logoConfiguration.dateUploadInput().getAttribute("value");
        assertNotEquals(newDateInfo, appealLetterLogo.getDateUploaded(), "        Date Uploaded is not displayed correctly.");

        logger.info("*** Step 4 Action: Click on Save and Clear button");
        logoConfigurationUtil.clickOnSaveAndClearBtn();

        logger.info("*** Step 4 Expected result: The confirmation message is displayed");
        String actMessage = logoConfiguration.logoConfigConfirmDialogMessageTxt().getText();
        String expMessage = "The facility logo has been changed to " + appealLetterLogo.getAvailableLogos() + ".\nUpdating this logo will update the logo for all document types using this facility.\nWould you like to proceed?";
        logger.info("actMessage: 	" + actMessage);
        logger.info("expMessage: 	" + expMessage);
        assertEquals(actMessage, expMessage, "        The confirmation message is not displayed");

        logger.info("*** Step 5 Action: Click on Ok of confirmation popup and Reload Client Statement");
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        logger.info("Click on OK button if change size confirmation popup is displayed");
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        logoConfigurationUtil.setValuesLogoConfiguration(documentTyp, fac);

        logger.info("*** Step 5 Expected result: Current Logo in header menu is displayed correctly. "
                + "Fac.Cln_Stmt_Logo is updated based on new Logo image."
                + "The SS#1528 is updated based on new logo size.");
        xifinPortalUtils.waitForPageLoaded(wait);
        LogoConfigurationHeader header = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(header.getDocumentType(), expectedHeader.getDocumentType(), "        Document Type is not displayed correctly.");
        logoConfigurationUtil.verifyLogoSizeLocation(SystemSettingMap.SS_CLN_STMT_LOGO_SIZE, 0, appealLetterLogo);

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + uploadFile;
    }

    @Test(priority = 1, description = "Client Statement - Upload logo")
    public void clientStatementUploadLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);

        logger.info("*** Step 1 Expected Results: User login successful. Logo Configuration page is displayed.");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Actions: Select document type: Client Statement.");
        String documentTyp = LogoConfigurationUtil.CLIENT_STATEMENT;
        Fac fac = facilityDao.getFacByMainFacAndRmtMainFac(MiscMap.FAC_TYP_MAIN_LAB, MiscMap.FAC_TYP_REMOTE_AFFIL);
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(documentTyp, fac);

        logger.info("*** Step 2 Expected Results: Logo Configuration detail page is displayed with Document type - Client Statement.");
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 3 Actions: Check on Upload Logo radio button. Select any image.");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadTempLogo();
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        String uploadFile = appealLetterLogo.getAvailableLogos();

        logger.info("*** Step 3 Expected Results: Image name is displayed next to Upload Logo button | The image is saved in folder");
        assertEquals(logoConfiguration.uploadNameFileText().getText(), appealLetterLogo.getAvailableLogos(), "        Image name is not displayed next to Upload Logo btn. ");

        logoConfigurationUtil.verifyImageIsSavedAtTempRoot(appealLetterLogo);

        logger.info("*** Step 4 Actions: Select Update logo.");
        clickHiddenPageObject(logoConfiguration.updateLogoRad(), 0);

        logger.info("*** Step 4 Expected Results: New image is shown at the bottom of the list");
        logoConfigurationUtil.checkMalwareScanningIsDisplayed();
        assertTrue(logoConfiguration.availableLogosDdl().isDisplayed());
        String[] arrOfStr = appealLetterLogo.getAvailableLogos().split("\\.(?=[^.]+$)");
        String logo = arrOfStr.length > 1 ? (arrOfStr[0].toUpperCase() + "." + arrOfStr[1]) : LogoConfigurationUtil.EMPTY;
        assertTrue(isElementPresent(logoConfiguration.logoConfigSelectedLogoName(logo), 5), "        New image is not displayed at the bottom of the list.");

        logger.info("*** Step 5 Actions: Click Save and Clear button and Reload Client Statement");
        logoConfigurationUtil.clickOnSaveAndClearBtn();
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.setValuesLogoConfiguration(documentTyp, fac);

        logger.info("*** Step 5 Expected Results: Fac.Cln_Stmt_Logo is updated based on new Logo image."
                + " New logo image is saved in folder and the image is removed from the temporary folder.");
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        LogoConfigurationHeader header = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(header.getDocumentType(), expectedHeader.getDocumentType(), "        Document Type is not displayed correctly");
        logoConfigurationUtil.verifyImageIsSavedAtRoot(appealLetterLogo);

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + uploadFile;
    }

    @Test(priority = 1, description = "EP Letter - Update logo")
    public void epLetterUpdateLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);

        logger.info("*** Step 1 Expected result: User login successful and Logo Configuration screen is displayed");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Action: Select document type is EP Letter. Select valid FacId");
        String documentTyp = LogoConfigurationUtil.EP_LETTER;
        Fac fac = facilityDao.getFacByMainFacAndRmtMainFac(MiscMap.FAC_TYP_MAIN_LAB, MiscMap.FAC_TYP_REMOTE_AFFIL);
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(documentTyp, fac);

        logger.info("*** Step 2 Expected Result: Logo Configuration page is displayed with Document type - EP Letter.");
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 3 Action: Check on Update Logo radio btn: select logo from Available Logos select box. Update Logo Location. Update logo size");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadLogo();
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        String uploadFile = appealLetterLogo.getAvailableLogos();
        navigateRefresh();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.setValuesLogoConfiguration(documentTyp, fac);
        logoConfigurationUtil.updateLogo(appealLetterLogo, documentTyp);

        logger.info("*** Step 3 Expected result: Date Uploaded field is updated");
        String newDateInfo = logoConfiguration.dateUploadInput().getAttribute("value");
        assertNotEquals(newDateInfo, appealLetterLogo.getDateUploaded(), "        Date Uploaded is not displayed correctly.");

        logger.info("*** Step 4 Action: Click on Save and Clear button");
        assertTrue(isElementPresent(logoConfiguration.saveAndClearBtn(), 5), "        Save and Clear button is not displayed.");
        logoConfigurationUtil.clickOnSaveAndClearBtn();

        logger.info("*** Step 4 Expected result: The confirmation message is displayed");
        logoConfigurationUtil.verifyConfirmationMessage(appealLetterLogo);

        logger.info("*** Step 5 Action: Accept confirmation popup and Reload EP Letter");
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        logoConfigurationUtil.setValuesLogoConfiguration(documentTyp, fac);

        logger.info("*** Step 5 Expected result: Current Logo in header menu is displayed correctly."
                + "Fac.Cln_Stmt_Logo is updated based on new Logo image. "
                + "The SS#1409 is updated based on new logo size."
                + "The SS#1391 is updated based on new logo location.");
        xifinPortalUtils.waitForPageLoaded(wait);
        LogoConfigurationHeader header = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(header.getDocumentType(), expectedHeader.getDocumentType(), "        Document Type is not displayed correctly");
        logger.info("appealLetterLogo:		" + appealLetterLogo);
        logoConfigurationUtil.verifyLogoSizeLocation(SystemSettingMap.SS_EP_LTR_LOGO_SCALE_SIZE, SystemSettingMap.SS_EP_LTR_LOGO_LOCATION, appealLetterLogo);

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + uploadFile;
    }

    @Test(priority = 1, description = "EP Letter - Upload logo")
    public void epLetterUploadLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);

        logger.info("*** Step 1 Expected Result: User login successful");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Action: Select document type is EP Letter. Input with valid Facility ID");
        String docTyp = LogoConfigurationUtil.EP_LETTER;
        Fac fac = facilityDao.getFacByMainFacAndRmtMainFac(MiscMap.FAC_TYP_MAIN_LAB, MiscMap.FAC_TYP_REMOTE_AFFIL);
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(docTyp, fac);

        logger.info("*** Step 2 Expected Result: Logo Configuration page is displayed with Document type - EP Letter.");
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);

        logger.info("*** Step 3 Action: Check on Upload Logo radio btn: "
                + "Select logo from Available Logos select box. "
                + "Update logo Location. "
                + "Update logo size.");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadLogo();
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        logoConfigurationUtil.deleteFile(appealLetterLogo.getDir() + File.separator + appealLetterLogo.getAvailableLogos());
        String uploadFile = appealLetterLogo.getAvailableLogos();
        logoConfigurationUtil.setValuesLogoConfiguration(docTyp, fac);
        logoConfigurationUtil.updateLogo(appealLetterLogo, docTyp);

        logger.info("*** Step 3 Expected Result: Date Uploaded field is updated");
        String newDateInfo = logoConfiguration.dateUploadInput().getAttribute("value");
        assertNotEquals(newDateInfo, appealLetterLogo.getDateUploaded(), "        Date Uploaded field is not updated.");

        logger.info("*** Step 4 Action: Click on Save and Clear btn, Reload EP Letter");
        logoConfigurationUtil.clickOnSaveAndClearBtn();
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        logoConfigurationUtil.setValuesLogoConfiguration(docTyp, fac);

        logger.info("*** Step 4 Expected Result: Current Logo in header menu is displayed correctly "
                + "- The SS#1409 is updated based on new logo location "
                + "- The SS#1391 is updated based on new logo size");
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationHeader(expectedHeader);
        logoConfigurationUtil.verifyLogoSizeLocation(SystemSettingMap.SS_EP_LTR_LOGO_SCALE_SIZE, SystemSettingMap.SS_EP_LTR_LOGO_LOCATION, appealLetterLogo);//System setting not found, systemSettingId=1409facId= 86655

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + uploadFile;
    }

    @Test(priority = 1, description = "Welcome Letter - Update logo")
    public void welcomeLetterUpdateLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);

        logger.info("*** Step 1 Expected Result: User login successful");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Action: Select document type: Welcome Letter. Input with valid Facility ID");
        String docTyp = LogoConfigurationUtil.WELCOME_LETTER;
        Fac fac = facilityDao.getFacByMainFacAndRmtMainFac(MiscMap.FAC_TYP_MAIN_LAB, MiscMap.FAC_TYP_REMOTE_AFFIL);
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(docTyp, fac);

        logger.info("*** Step 2 Expected Result: Logo Configuration page is displayed with Document type - Welcome Letter.");
        xifinPortalUtils.waitForPageLoaded(wait);
        LogoConfigurationHeader actualHeader = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(actualHeader.getDocumentType(), expectedHeader.getDocumentType(), "        Logo Configuration page is not displayed correctly");
        assertEquals(actualHeader.getFacilityAbbrev(), fac.getAbbrv() + " - " + fac.getName(), "        Logo Configuration page is not displayed correctly");

        logger.info("*** Step 3 Action: Check on Update Logo radio btn: "
                + "Select logo from Available Logos select box. "
                + "Update logo Location. "
                + "Update logo size.");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadLogo();
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        logoConfigurationUtil.deleteFile(appealLetterLogo.getDir() + File.separator + appealLetterLogo.getAvailableLogos());
        String uploadFile = appealLetterLogo.getAvailableLogos();
        navigateRefresh();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.setValuesLogoConfiguration(docTyp, fac);
        logoConfigurationUtil.updateLogo(appealLetterLogo, docTyp);

        logger.info("*** Step 3 Expected Result: Date Uploaded field is updated");
        String newDateInfo = logoConfiguration.dateUploadInput().getAttribute("value");
        assertNotEquals(newDateInfo, appealLetterLogo.getDateUploaded(), "        Date Uploaded field is not updated.");

        logger.info("*** Step 4 Action: Click on Save and Clear btn, reload Annual Disclosure Letter again.");
        logoConfigurationUtil.clickOnSaveAndClearBtn();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();
        logoConfigurationUtil.setValuesLogoConfiguration(docTyp, fac);

        logger.info("*** Step 4 Expected Result: Current Logo in header menu is displayed correctly "
                + "- The SS#1703 is updated based on new logo size ");
        xifinPortalUtils.waitForPageLoaded(wait);
        actualHeader = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(actualHeader.getDocumentType(), expectedHeader.getDocumentType(), "        Logo Configuration page is displayed correctly");
        logoConfigurationUtil.verifyLogoSizeLocation(fac.getFacId(), 0, SystemSettingMap.SS_WELCOME_LTR_LOGO_SIZE, 0, appealLetterLogo);

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + uploadFile;
    }

    @Test(priority = 1, description = "Welcome Letter - Upload logo.")
    public void welcomeLetterUploadLogo() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);

        logger.info("*** Step 1 Expected Result: User login successful");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Action: Select document type: Welcome Letter. Input with valid Facility ID");
        String docTyp = LogoConfigurationUtil.WELCOME_LETTER;
        Fac fac = facilityDao.getFacByMainFacAndRmtMainFac(MiscMap.FAC_TYP_MAIN_LAB, MiscMap.FAC_TYP_REMOTE_AFFIL);
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(docTyp, fac);

        logger.info("*** Step 2 Expected Result: Logo Configuration page is displayed with Document type - Welcome Letter");
        xifinPortalUtils.waitForPageLoaded(wait);
        LogoConfigurationHeader actualHeader = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(actualHeader.getDocumentType(), expectedHeader.getDocumentType(), "        Logo Configuration page is not displayed correctly");
        assertEquals(actualHeader.getFacilityAbbrev(), fac.getAbbrv() + " - " + fac.getName(), "        Logo Configuration page is not displayed correctly");

        logger.info("*** Step 3 Action: Click on Upload Radio Button: Select any image");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadTempLogo();
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();

        logger.info("*** Step 3 Expected Result: Image name is displayed next to Upload Logo btn"
                + "- The image is saved in folder");
        logoConfigurationUtil.verifyImageIsSavedAtTempRoot(appealLetterLogo);

        logger.info("*** Step 4 Action: Select Update logo");
        clickHiddenPageObject(logoConfiguration.updateLogoRad(), 0);

        logger.info("*** Step 4 Expected Result: New image is shown at the bottom of the list");
        logoConfigurationUtil.checkMalwareScanningIsDisplayed();
        String name = logoConfiguration.availableLogosDdl().getText().toLowerCase();
        assertEquals(name, appealLetterLogo.getAvailableLogos().toLowerCase(), "        New image is not shown at the bottom of the list.");

        logger.info("*** Step 5 Action: Click on Save and Clear btn, reload Welcome Letter");
        logoConfigurationUtil.clickOnSaveAndClearBtn();
        logoConfigurationUtil.clickOnConfirmDialogOkIfDisplayed();
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfigurationUtil.setValuesLogoConfiguration(docTyp, fac);

        logger.info("*** Step 5 Expected Result: Current Logo in header menu is displayed correctly "
                + "- New logo image is save in folder and the image is removed from the temporary folder"
                + "- The selected logo is displayed in Current System Logos table ");
        xifinPortalUtils.waitForPageLoaded(wait);
        actualHeader = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(actualHeader.getDocumentType(), expectedHeader.getDocumentType(), "        Logo Configuration page is not displayed correctly");

        logoConfigurationUtil.verifyImageIsSavedAtRoot(appealLetterLogo);

        fileToDelete = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + LogoConfigurationUtil.IMAGES_PATH + File.separator + appealLetterLogo.getAvailableLogos();
    }

    @Test(priority = 1, description = "Verify Help icon.")
    public void verifyHelpIcon() throws Throwable {
        logoConfiguration = new LogoConfiguration(driver, wait);

        logger.info("*** Step 1 Expected Result: User login successful");
        logoConfigurationUtil.verifyLogoConfigurationPageIsDisplayed();

        logger.info("*** Step 2 Actions: Click on Help icon at the Logo Configuration load page");
        clickHiddenPageObject(logoConfiguration.logoConfigHeaderHelpIco(), 0);

        logger.info("*** Step 2 Expected Results: Help page is displayed");
        xifinPortalUtils.verifyHelpPageIsDisplayed(this, LogoConfigurationUtil.LOGO_CONFIG_HELP_URL, null);

        logger.info("*** Step 3 Actions: Select Document type - Appeal Letter");
        Fac fac = facilityDao.getFacByMainFacAndRmtMainFac(MiscMap.FAC_TYP_MAIN_LAB, MiscMap.FAC_TYP_REMOTE_AFFIL);
        LogoConfigurationHeader expectedHeader = logoConfigurationUtil.setValuesLogoConfiguration(LogoConfigurationUtil.APPEAL_LETTER, fac);

        logger.info("*** Step 3 Expected Results: Logo Configuration page is displayed");
        xifinPortalUtils.waitForPageLoaded(wait);
        LogoConfigurationHeader actualHeader = logoConfigurationUtil.getValuesLogoConfigurationHeader();
        assertEquals(actualHeader.getDocumentType(), expectedHeader.getDocumentType(), "        Logo Configuration page is not displayed correctly");

        logger.info("*** Step 4 Actions: Click on Help icon at the header menu");
        clickHiddenPageObject(logoConfiguration.logoConfigDetailPageHeaderHelpIco(), 0);

        logger.info("*** Step 4 Expected Results: Help page is displayed");
        xifinPortalUtils.verifyHelpPageIsDisplayed(this, LogoConfigurationUtil.LOGO_CONFIG_HELP_URL, null);

        logger.info("*** Step 5 Actions: Click on Help icon at Document Type section");
        AppealLetterLogo appealLetterLogo = logoConfigurationUtil.uploadTempLogo();
        logoConfigurationUtil.deleteFile(appealLetterLogo.getDir() + File.separator + appealLetterLogo.getAvailableLogos());
        clickHiddenPageObject(logoConfiguration.documentTypeHelpIco(), 0);

        logger.info("*** Step 5 Expected Results: Help page is displayed");
        xifinPortalUtils.verifyHelpPageIsDisplayed(this, LogoConfigurationUtil.APPEAL_LETTER_HELP_URL, null);

        logger.info("*** Step 6 Actions: Click on Help icon at Current System Logo section.");
        clickHiddenPageObject(logoConfiguration.currentSystemLogosTblHelpIcon(), 0);

        logger.info("*** Step 6 Expected Results: Help page is displayed");
        xifinPortalUtils.verifyHelpPageIsDisplayed(this, LogoConfigurationUtil.CURRENT_LOGOS_HELP_URL, null);
    }
}