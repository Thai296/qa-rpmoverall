package com.overall.utils;

import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.systemSetting.SystemSetting;
import com.mbasys.mars.ejb.entity.systemSettingOverrideByFac.SystemSettingOverrideByFac;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.filemaint.logoConfiguration.LogoConfiguration;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.FacilityDaoImpl;
import com.xifin.qa.dao.rpm.RpmDaoImpl;
import com.xifin.qa.dao.rpm.SystemDaoImpl;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.RandomCharacter;
import domain.filemaint.logoConfiguration.AppealLetterLogo;
import domain.filemaint.logoConfiguration.LogoConfigurationHeader;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

import static org.testng.Assert.*;

public class LogoConfigurationUtil extends SeleniumBaseTest{
    RandomCharacter randomCharacter;
    XifinPortalUtils xifinPortalUtils;
    LogoConfiguration logoConfiguration;

    public String localPathFolder;
    public static final String EMPTY = "";
    public static final String EP_LETTER = "EP Letter";
    public static final String IMAGES_PATH = "images";
    public static final String APPEAL_LETTER = "Appeal Letter";
    public static final String WELCOME_LETTER = "Welcome Letter";
    public static final String TITLE_PAGE = "Logo Configuration";
    public static final String DATE_FORMAT_PATTERN = "MM/dd/yyyy";
    public static final String TEMP_PATH = "temp";
    public static final String CLIENT_STATEMENT = "Client Statement";
    public static final String ANNUAL_DISCLOSURE_LETTER = "Annual Disclosure Letter";

    public static final String LOGO_CONFIG_HELP_URL = "help/file_maintenance_tab/system_management_menu/p_logo_configuration_header.htm";
    public static final String APPEAL_LETTER_HELP_URL = "help/file_maintenance_tab/system_management_menu/p_logo_configuration_appeal_letter_logo.htm";
    public static final String CURRENT_LOGOS_HELP_URL = "help/file_maintenance_tab/system_management_menu/p_logo_configuration_current_system_logos.htm";

    public LogoConfigurationUtil(RemoteWebDriver driver, WebDriverWait wait, Configuration config){
        this.config = config;
        this.wait = wait;
        this.driver = driver;
        randomCharacter = new RandomCharacter();
        xifinPortalUtils = new XifinPortalUtils(driver);
        logoConfiguration = new LogoConfiguration(driver, wait);
        rpmDao = new RpmDaoImpl(config.getRpmDatabase());
        systemDao = new SystemDaoImpl(config.getRpmDatabase());
        facilityDao = new FacilityDaoImpl(config.getRpmDatabase());
    }

    public String creatJPGFile(String documentFileName, String inboundLocation) {
        String filePath = "";
        try {
            int width = 250;
            int height = 250;
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2d = bufferedImage.createGraphics();

            graphics2d.setColor(Color.WHITE);
            graphics2d.drawString(randomCharacter.getNonZeroRandomNumericString(10), 200, 200);

            graphics2d.dispose();
            File folderUpload = new File(inboundLocation);
            if(!folderUpload.exists()) {
                folderUpload.mkdir();
            }
            filePath = inboundLocation + documentFileName;
            File jpgFile = new File(filePath);
            if (jpgFile.createNewFile() && jpgFile.canWrite()) {
                ImageIO.write(bufferedImage, "jpg", jpgFile);
            } else {
                fail("Cannot create logo file to perform test, path:" + jpgFile.getAbsolutePath());
            }
        } catch (Exception e) {
            fail("Cannot create logo file to perform test, message: " + e.getMessage());
        }
        return filePath;
    }

    public void clickOnSaveAndClearBtn() throws Exception {
        clickHiddenPageObject(logoConfiguration.saveAndClearBtn(), 0);
        xifinPortalUtils.waitForPageLoaded(wait);
    }

    public boolean checkExistInAvailableLogos(WebElement element, String logoItem) throws Exception {
        boolean isExist = false;
        assertTrue(isElementPresent(element, 15), "        Available Logos Ddl is displayed.");
        List<String> logoImgs = null;
        try{
            logoImgs = getAllValuesInJQGrid(element);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        if (logoImgs.size() > 0) {
            for (String item : logoImgs) {
                item = logoImgs.listIterator().toString();
                if (item.equals(logoItem.toLowerCase())) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }

    public void deleteFile(String filePath) {
        if (filePath == null || filePath.equals(EMPTY)) return;
        File file = new File(filePath);
        file.delete();
        logger.info("      " + filePath + " was deleted.");
    }

    public void deleteLogoFileFromDdl() throws Exception {
        clickHiddenPageObject(logoConfiguration.deleteFileBtn(), 0);
        clickHiddenPageObject(logoConfiguration.logoConfigConfirmDialogDeleteFileBtn(), 0);
    }

    public LogoConfigurationHeader getValuesLogoConfigurationHeader() throws Exception {
        LogoConfigurationHeader header = new LogoConfigurationHeader();
        DateFormat formatDate = new SimpleDateFormat(DATE_FORMAT_PATTERN);

        header.setDocumentType(logoConfiguration.logoConfigDetailPageDocumentTypeTxt().getText());
        header.setFacilityAbbrev(logoConfiguration.logoConfigDetailPageFacilityIdTxt().getText());
        String currentLogoName = logoConfiguration.logoConfigDetailPageCurrentLogoNameTxt().getText();

        if (!currentLogoName.equals(EMPTY)) {
            header.setCurrentLogoName(currentLogoName);
        }
        String logoUploadDtTxt = logoConfiguration.logoConfigDetailPageCurrentLogoUploadDtTxt().getText();

        if (!logoUploadDtTxt.equals(EMPTY)) {
            header.setCurrentLogoUploadDt(new Date(formatDate.parse(logoConfiguration.logoConfigDetailPageCurrentLogoUploadDtTxt().getText()).getTime()));
        }

        return header;
    }

    public List<String> getLogoLocationByDocTyp(String docTyp) {
        Random rand = new Random();
        List <String> result = new ArrayList<>();
        List<String> requireds;
        List <String> requiredsId;

        if (!docTyp.equals(EP_LETTER)) {
            requireds =  Arrays.asList( "Upper Left", "Centered", "Upper Right", "Top Margin & Centered");
            requiredsId = Arrays.asList("1", "2", "3","4");
        } else {
            requireds =  Arrays.asList( "Next to Address", "Upper Left", "Centered", "Top Margin & Centered");
            requiredsId = Arrays.asList("1", "2", "3","4");
        }

        int randomIndex = rand.nextInt(requireds.size());
        String randomElement = requireds.get(randomIndex);
        String randomElementId = requiredsId.get(randomIndex);
        result.add(randomElement);//0
        result.add(randomElementId);//1

        return result;
    }

    public String getNewLogoFromListLogos(List<String> logos , String oldLogo) {
        String result = EMPTY;
        for (String logo : logos) {
            if (!oldLogo.contains(logo) && !logo.trim().isEmpty()) {
                result = logo;
            }
        }

        return result;
    }

    public boolean isFacLogoEnabled(String logoType) {
        boolean isFacLogoEnabled = false;
        int ssToCheck = 0;

        switch(logoType) {
            case CLIENT_STATEMENT:
                ssToCheck = SystemSettingMap.SS_CLN_STMT_OVERRIDE_MAIN_FAC_WITH_ORDERING_FAC;
                break;
            case EP_LETTER:
                ssToCheck = SystemSettingMap.SS_EP_OVERRIDE_MAIN_FAC_WITH_CLN_ORDERING_FAC;
                break;
            case APPEAL_LETTER:
                ssToCheck = SystemSettingMap.SS_FACILITY_TO_USE_ON_APPEAL_LETTERS;
                break;
            case ANNUAL_DISCLOSURE_LETTER:
                ssToCheck = SystemSettingMap.SS_CLN_LETTER_ANNUAL_DISCLOSURE_OVERRIDE_MAIN_FAC_WITH_CLN_ORDERING_FAC;
                break;
            case WELCOME_LETTER:
                ssToCheck = SystemSettingMap.SS_WELCOME_LTR_OVERRIDE_MAIN_FAC_WITH_CLN_ORDERING_FAC;
                break;
            default:
                break;
        }
        try {
            isFacLogoEnabled = systemDao.getSystemSetting(ssToCheck).getDataValue().equals("1");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return isFacLogoEnabled;
    }

    public LogoConfigurationHeader setValuesLogoConfiguration(String docType, Fac fac) throws Exception {
        LogoConfigurationHeader header = new LogoConfigurationHeader();
        selectDropDownByText(logoConfiguration.logoConfigLoadPageDocumentTypeDDL(),docType);
        header.setDocumentType(docType);
        if(isFacLogoEnabled(docType)) {
            String facFullName;
            if(fac == null) {
                Fac facRandom = facilityDao.getFacByMainFacAndRmtMainFac(MiscMap.FAC_TYP_MAIN_LAB, MiscMap.FAC_TYP_REMOTE_AFFIL);
                facFullName = facRandom.getAbbrv() + "-" + facRandom.getName();
            }else {
                facFullName = fac.getAbbrv() + "-" + fac.getName();
            }
            selectDropDownByText(logoConfiguration.logoConfigLoadPageFacilityIdDDL(), facFullName);
            header.setFacilityAbbrev(facFullName);
        }
        xifinPortalUtils.waitForPageLoaded(wait);
        logoConfiguration.waitForLogoSessionDisplay();
        xifinPortalUtils.waitForPageLoaded(wait);
        return header;
    }

    public AppealLetterLogo uploadLogo() throws Exception {
        AppealLetterLogo appealLetterLogo = uploadTempLogo();
        clickOnSaveAndClearBtn();
        return appealLetterLogo;
    }

    public AppealLetterLogo uploadTempLogo() throws Exception {
        DateFormat formatDate = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        AppealLetterLogo appealLetterLogo = new AppealLetterLogo();

        String imgName = randomCharacter.getRandomAlphaNumericString(10) + ".jpg";
        File directory = new File(".");
        String rootDirectory = directory.getCanonicalPath() + File.separator + "logo-uploads" + File.separator;
        logger.info("========== ImageName = " + imgName);
        logger.info("========== rootDirectory = " + rootDirectory);
        String filePath = creatJPGFile(imgName, rootDirectory);
        String date = logoConfiguration.dateUploadInput().getAttribute("value");
        clickHiddenPageObject(logoConfiguration.uploadLogoRad(), 0);
        assertTrue(isElementPresent(logoConfiguration.xifinUploadUploader(), 5),"        Date Upload Input is displayed.");
        logoConfiguration.uploadLogo(filePath);
        Thread.sleep(3000);
        appealLetterLogo.setAvailableLogos(imgName);

        if (!date.equals(EMPTY)) {
            appealLetterLogo.setDateUploaded(new Date(formatDate.parse(date).getTime()));
        }
        appealLetterLogo.setDir(rootDirectory);

        // Keep the file path as a class variable to cleaning up files
        localPathFolder = filePath;
        return appealLetterLogo;
    }

    public void updateLogo(AppealLetterLogo appealLetterLogo, String docTyp) throws Exception {
        DateFormat formatDate = new SimpleDateFormat(DATE_FORMAT_PATTERN);

        clickHiddenPageObject(logoConfiguration.updateLogoRad(), 0);
        List<String> logoImgs = getAllValuesInJQGrid(logoConfiguration.availableLogosDdl());
        String oldLocation = logoConfiguration.logoLocationTxt().getText();
        String imgName = getNewLogoFromListLogos(logoImgs, appealLetterLogo.getAvailableLogos());

        String dtUpdate = logoConfiguration.dateUploadInput().getAttribute("value");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        enterValues(logoConfiguration.logoSizeInput(), randomNum);
        xifinPortalUtils.waitForPageLoaded(wait);
        if (!docTyp.equals(CLIENT_STATEMENT)) {
            if (!docTyp.equals(WELCOME_LETTER)) {
                List<String> logoLocations = getLogoLocationByDocTyp(docTyp);
                String logoLocation = logoLocations.get(0);
                while (logoLocation.equals(oldLocation)){
                    logoLocations = getLogoLocationByDocTyp(docTyp);
                    logoLocation = logoLocations.get(0);
                }
                logger.info("        Logo Location is: " + logoLocations.get(0));
                selectDropDownJQGridByTyping(logoConfiguration.logoLocationDdl(), logoLocation);
                appealLetterLogo.setLogoLocation(logoLocations.get(1));
            }
        }

        if (!dtUpdate.equals(EMPTY)) {
            appealLetterLogo.setDateUploaded(new Date(formatDate.parse(dtUpdate).getTime()));
        }
        xifinPortalUtils.waitForPageLoaded(wait);
        assertTrue(isElementPresent(logoConfiguration.availableLogosDdl(), 5),"        Available Logos Ddl is not displayed.");
        selectDropDownJQGridByTyping(logoConfiguration.availableLogosDdl(), imgName);
        appealLetterLogo.setAvailableLogos(imgName);
        appealLetterLogo.setLogoSize(Integer.parseInt(randomNum));
    }

    public void verifyUpdateLogoSizeLocation(int sysLogo,int sysLogoSize, int sysLogoLocation, AppealLetterLogo appealLetterLogo) throws Exception {
        if (!(sysLogoSize == 0)) {
            SystemSetting newLogoSize = systemDao.getSystemSetting(sysLogoSize);
            assertEquals(newLogoSize.getDataValue(), String.valueOf(Float.parseFloat(String.valueOf(appealLetterLogo.getLogoSize()))/100), "        The SS# "+sysLogoSize+" is not updated based on new logo size.");
        }
        if (!(sysLogoLocation == 0)) {
            SystemSetting newLogoLocation = systemDao.getSystemSetting(sysLogoLocation);
            assertEquals(newLogoLocation.getDataValue(), appealLetterLogo.getLogoLocation(), "        The SS#" + sysLogoLocation + " is not updated based on new logo location.");
        }

        if (!(sysLogo == 0)) {
            SystemSetting newLogo = systemDao.getSystemSetting(sysLogo);
            assertTrue(newLogo.getDataValue().contains(appealLetterLogo.getAvailableLogos()), "        The SS#"+sysLogo+" is not updated based on new logo.");
        }
    }

    public void verifyLogoSizeLocation(int sysLogoSize, int sysLogoLocation, AppealLetterLogo appealLetterLogo) throws Exception {

        if (!(sysLogoSize == 0)) {
            SystemSetting newLogoSize = systemDao.getSystemSetting(sysLogoSize);
            assertEquals(newLogoSize.getDataValue(), String.valueOf(Float.parseFloat(String.valueOf(appealLetterLogo.getLogoSize()))/100), "        The SS# " +sysLogoSize+" is updated based on new logo size.");
        }
        if (!(sysLogoLocation == 0)) {
            SystemSetting newLogoLocation = systemDao.getSystemSetting(sysLogoLocation);
            assertEquals(newLogoLocation.getDataValue(), appealLetterLogo.getLogoLocation(), "        The SS# " + sysLogoLocation + " is not updated based on new logo location.");
        }
    }


    public void verifyLogoSizeLocation(int facId,int sysLogo, int sysLogoSize, int sysLogoLocation, AppealLetterLogo appealLetterLogo) throws XifinDataAccessException, XifinDataNotFoundException {
        if (!(sysLogoSize == 0)) {
            SystemSettingOverrideByFac newLogoSize = systemDao.getSystemSettingOverrideByFac(facId, sysLogoSize);
            assertEquals(newLogoSize.getDataValue(), String.valueOf(Float.parseFloat(String.valueOf(appealLetterLogo.getLogoSize()))/100), "        The SS#"+sysLogoSize+" is not updated based on new logo size.");
        }
        if (!(sysLogoLocation == 0)) {
            SystemSettingOverrideByFac newLogoLocation = systemDao.getSystemSettingOverrideByFac(facId, sysLogoLocation);
            assertEquals(newLogoLocation.getDataValue(), appealLetterLogo.getLogoLocation(), "        The SS#" + sysLogoLocation + " is not updated based on new logo location.");
        }
        if (!(sysLogo == 0)) {
            SystemSettingOverrideByFac newLogo = systemDao.getSystemSettingOverrideByFac(facId, sysLogo);
            assertTrue(newLogo.getDataValue().contains(appealLetterLogo.getAvailableLogos()), "        The SS#" + sysLogo + " is not updated based on new logo.");
        }
    }

    public void verifyLogoConfigurationPageIsDisplayed() {
        assertEquals(TITLE_PAGE, logoConfiguration.logoConfigLoadPageTitleTxt().getText(), "        The title of Load Logo Configuration page should be 'Logo Configuration'");
    }

    public void verifyImageIsSavedAtTempRoot(AppealLetterLogo appealLetterLogo) {
        assertEquals(appealLetterLogo.getAvailableLogos(), logoConfiguration.uploadNameFileText().getText(), "        Image name is not displayed next to Upload Logo btn.");
        String path = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + IMAGES_PATH + File.separator + TEMP_PATH + File.separator + appealLetterLogo.getAvailableLogos();
        File file = new File(path);
        assertTrue(file.exists(), "       The image is save in " + path + ".");
    }

    public void verifyImageIsSavedAtRoot(AppealLetterLogo appealLetterLogo) {
        String pathImages = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + IMAGES_PATH + File.separator + appealLetterLogo.getAvailableLogos();
        boolean isExistInImages = new File(pathImages).exists();
        assertTrue(isExistInImages, "       New logo image is saved in " + pathImages + ".");
    }

    public void verifyConfirmationMessage(AppealLetterLogo appealLetterLogo) {
        try {
            boolean isCorrected = false;
            if(logoConfiguration.logoConfigConfirmDialogMessageTxt().isDisplayed()  & logoConfiguration.logoConfigConfirmDialogOkBtn().isDisplayed()) {
                String actMessage = logoConfiguration.logoConfigConfirmDialogMessageTxt().getText();
                logger.info("actMessage: 		" + actMessage);
                String expMessage1 = "The facility logo has been changed to " + appealLetterLogo.getAvailableLogos()
                        + ".\nUpdating this logo will update the logo for all document types using this facility.\nWould you like to proceed?";
                String expMessage2 = "The logo size has been changed to " + appealLetterLogo.getLogoSize()
                        + ".\nUpdating this logo will update the size for all EP Letter facility logos.\nWould you like to proceed?";
                if (actMessage.equalsIgnoreCase(expMessage1) || actMessage.equalsIgnoreCase(expMessage2)) {
                    isCorrected = true;
                }
                assertTrue(isCorrected, "        The confirmation message is not displayed.");
            }
        }catch (NoSuchElementException | org.openqa.selenium.TimeoutException | ElementNotInteractableException e) {
            logger.error(e.getMessage());
        }
    }

    public void verifyLogoConfigurationHeader(LogoConfigurationHeader expectedHeader) throws Exception {
        LogoConfigurationHeader actualHeader = getValuesLogoConfigurationHeader();
        assertEquals(actualHeader.getDocumentType(), expectedHeader.getDocumentType(),"        Logo Configuration page is not displayed correctly");
        if(isFacLogoEnabled(expectedHeader.getDocumentType())) {
            assertEquals(actualHeader.getFacilityAbbrev().replace(" ",""), expectedHeader.getFacilityAbbrev().replace(" ", ""),"        Logo Configuration page is not displayed correctly");
        }
    }

    public void checkMalwareScanningIsDisplayed() throws Exception {
        if(logoConfiguration.malwareScanningStatus().isDisplayed()) {
            clickHiddenPageObject(logoConfiguration.btnMalwereScanningCloseDialog(), 0);
        }
    }

    public void clickOnConfirmDialogOkIfDisplayed() throws Exception{
        try{
            if(logoConfiguration.logoConfigConfirmDialogOkBtn().isDisplayed()) {
                clickHiddenPageObject(logoConfiguration.logoConfigConfirmDialogOkBtn(), 0);
            }
        }catch(NoSuchElementException | org.openqa.selenium.TimeoutException | ElementNotInteractableException e){
            logger.error(e.getMessage());
        }
    }

}