package com.overall.fileMaintenance.fileMaintenanceTables;

//this is only used in testRPM_784 by Production Deployment Smoke Tests

import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.headerNavigation.HeaderNavigation;
import com.xifin.qa.config.Configuration;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EligibilityResponseTranslation
{
    private static final Logger LOG = Logger.getLogger(EligibilityResponseTranslation.class);

    private static String ID_MAIN_SECTIONS = "mainSections";
    private static String ID_SAVE_AND_CLEAR_BUTTON = "btnSaveAndClear";
    private static String ID_ELIG_SVC_ABBREV = "eligSvcAbbrev";
    private static String ID_LOOKUP_ELIG_SVC_ABBREV = "lookupEligSvcAbbrev";
    private static String ID_LOOKUP_ELIG_RESP_PYR_ABBREV = "lookupEligRespPyrAbbrv";
    private static String ID_ELIG_SVC_ID = "eligSvcAbbrev";
    
    private final RemoteWebDriver driver;
    private final Configuration config;
    private WebDriverWait wait;

    public EligibilityResponseTranslation(RemoteWebDriver driver, Configuration config, WebDriverWait wait)
    {
        this.driver = driver;
        this.config = config;
        this.wait = wait;
    }
    public WebElement contentFrame() throws Exception
    {
        return driver.findElement(By.id("content"));
    }

    public WebElement platformiFrame() throws Exception
    {
        return driver.findElement(By.id("platformiframe"));
    }

    public WebElement eligibilityTranslationTitle()
    {
        return driver.findElement(By.cssSelector(".platormPageTitle"));
    }

    public WebElement eligSvcIDInputInLoadPage()
    {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_LOOKUP_ELIG_SVC_ABBREV)));
    }

    public WebElement payorAbbrevInLoadPage()
    {
        return driver.findElement(By.id(ID_LOOKUP_ELIG_RESP_PYR_ABBREV));
    }



    public WebElement runAuditBtn() throws Exception
    {
        return driver.findElement(By.id("auditBtn"));
    }

    public WebElement helpIconInHeaderSection() throws Exception
    {
        return driver.findElement(By.cssSelector("a[data-help-id='p_eligibility_translation_header']"));
    }

    public WebElement helpIconInEliResponseTranslation() throws Exception
    {
        return driver.findElement(By.cssSelector("a[data-help-id='p_eligibility_translation_eligibility_response_translation']"));
    }

    public WebElement eligSvcIDInput() throws Exception
    {
        return driver.findElement(By.id(ID_ELIG_SVC_ABBREV));
    }

    public WebElement nameInput() throws Exception
    {
        return driver.findElement(By.id("svcName"));
    }

    public WebElement eligSVCid() throws Exception
    {
        return driver.findElement(By.id("eligSvcAbbrev"));
    }

    public WebElement loginIDInput() throws Exception
    {
        return driver.findElement(By.id("loginId"));
    }

    public WebElement passwordIDInput() throws Exception
    {
        return driver.findElement(By.id("passwd"));
    }

    public WebElement classNameInput() throws Exception
    {
        return driver.findElement(By.id("classNm"));
    }

    public WebElement exceptionAlertThreadholdInput() throws Exception
    {
        return driver.findElement(By.id("alrtThres"));
    }

    public WebElement serverDelayInput() throws Exception
    {
        return driver.findElement(By.id("serverDly"));
    }

    public WebElement serverTimeoutInput() throws Exception
    {
        return driver.findElement(By.id("serverTimeout"));
    }

    public WebElement resetBtn() throws Exception
    {
        return driver.findElement(By.id("Reset"));
    }

    public WebElement saveAndClearBtn()
    {
        return driver.findElement(By.id(ID_SAVE_AND_CLEAR_BUTTON));
    }


    public WebElement payorIdFilterInputInPayorSetupTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_payorSetup']//input[@id = 'gs_pyrAbbrv']"));
    }

    public WebElement helpIconInPayorSetup() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@class = 'helpIcon inlineIcon' and contains(@title, 'Payor Setup')]"));
    }

    //Eligibility Response Translation table
    public WebElement gviewTblEliResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.id("gview_tbl_eligibilityResponseTranslation"));
    }

    public WebElement tblEligibilityResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.id("tbl_eligibilityResponseTranslation"));
    }

    public WebElement eligibilityResponseTranslationTblCelData(int row, int col) throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_eligibilityResponseTranslation']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement titleTxtEliResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[3]/div/div/section/header/div[1]/span/span"));
    }

    public WebElement addIconEliResponseTranslationTbl()
    {
        return driver.findElement(By.id("add_tbl_eligibilityResponseTranslation"));
    }

    public WebElement editIconEliResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.id("edit_tbl_eligibilityResponseTranslation"));
    }

    public WebElement payorIdSearchBoxEliResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_eligibilityResponseTranslation']//input[@id = 'gs_pyrAbbrv']"));
    }

    public void setEligSvcIDInLoadPage(String eligSvcId, WebDriverWait wait)
    {
        WebElement elgSvcIdField = eligSvcIDInputInLoadPage();
        //wait.until(ExpectedConditions.elementToBeClickable(elgSvcIdField));
        Select select = new Select(elgSvcIdField);
        select.selectByValue(eligSvcId);
        LOG.info("Selected Eligibility Service ID: " + eligSvcId);
    }

    public void setPayorAbbrevInLoadPage(String payorAbbrv, WebDriverWait wait)
    {
        WebElement payorAbbrvField = payorAbbrevInLoadPage();
        wait.until(ExpectedConditions.elementToBeClickable(payorAbbrvField));
        Select select = new Select(payorAbbrvField);
        select.selectByValue(payorAbbrv);
        LOG.info("Selected Payor Abbrev: " + payorAbbrv + " from Dropdown List.");
    }

    public void clickAddResponseTranslationButton(WebDriverWait wait)
    {
        WebElement addResponseTranslationButton = addIconEliResponseTranslationTbl();
        wait.until(ExpectedConditions.elementToBeClickable(addResponseTranslationButton));
        addResponseTranslationButton.click();
    }

    // general add-edit records popup
    public WebElement editRecordTitleInEliResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='edithdtbl_eligibilityResponseTranslation']/span"));
    }

    public WebElement payorIdInputInPopup() throws Exception
    {
        return driver.findElement(By.id("pyrAbbrv"));
        //return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#TblGrid_tbl_payorSetup #pyrAbbrv')[0]");

    }

    public WebElement payorIdSearchIcon() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tr_pyrAbbrv']/td[1]/div/a/span"));
    }

    public WebElement payorNameTxtBox() throws Exception
    {
        return driver.findElement(By.id("pyrName"));
    }

    public WebElement oKBtn() throws Exception
    {
        return driver.findElement(By.id("sData"));
    }

    public WebElement cancelBtn() throws Exception
    {
        return driver.findElement(By.id("cData"));
    }

    public WebElement deleteCheckBox() throws Exception
    {
        return driver.findElement(By.id("deleted"));
    }

    // add-edit record popup on Payor Setup table
    public WebElement outPyrIdTxtBox() throws Exception
    {
        return driver.findElement(By.id("outPyrId"));
    }

    public WebElement activeCheckBox() throws Exception
    {
        return driver.findElement(By.id("preferred"));
    }

    public WebElement daysToCheckEligInput() throws Exception
    {
        return driver.findElement(By.id("pyrDaysToChkElig"));
    }

    public WebElement subServiceTxtBox() throws Exception
    {
        return driver.findElement(By.id("eligSubSvcAbbrv"));
    }

    public WebElement subServiceSearchIcon() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tr_eligSubSvcAbbrv']/td[1]/div/a/span"));
    }

    public WebElement translationEnableCheckBox() throws Exception
    {
        return driver.findElement(By.id("translationEnabled"));
    }

    public WebElement patientNameCheckBox() throws Exception
    {
        return driver.findElement(By.id("updatePtNm"));
    }

    public WebElement patientGenderCheckBox() throws Exception
    {
        return driver.findElement(By.id("updatePtGender"));
    }

    public WebElement patientAddressCheckBox() throws Exception
    {
        return driver.findElement(By.id("updatePtAddr"));
    }

    public WebElement insuredNameCheckBox() throws Exception
    {
        return driver.findElement(By.id("updateInsNm"));
    }

    public WebElement insuredGengerCheckBox() throws Exception
    {
        return driver.findElement(By.id("updateInsGender"));
    }

    public WebElement insuredAddressCheckBox() throws Exception
    {
        return driver.findElement(By.id("updateInsAddr"));
    }

    public WebElement relshpCheckBox() throws Exception
    {
        return driver.findElement(By.id("updateInsRelshp"));
    }

    public WebElement SubscrIDCheckBox() throws Exception
    {
        return driver.findElement(By.id("updateInsSubsId"));
    }

    //Add-Edit record popup on Eligibility Response Translation table
    public WebElement newPayorIdInput()
    {
        return driver.findElement(By.id("newPyrAbbrv"));
    }

    public WebElement newPayorIdSearchIcon() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tr_newPyrAbbrv']/td[1]/div/a/span"));
    }

    public WebElement newPayorNameTxtBox() throws Exception
    {
        return driver.findElement(By.id("newPyrName"));
    }

    public WebElement responseTxtBox()
    {
        return driver.findElement(By.id("resp"));
    }

    public WebElement prioDropDown() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='s2id_prio']/a"));
    }

    public WebElement checkEligCheckBox() throws Exception
    {
        return driver.findElement(By.id("checkElig"));
    }

    public void setPayorIdInPopup(String pyrId) throws Exception
    {
        payorIdInputInPopup().clear();
        payorIdInputInPopup().sendKeys(pyrId);
        payorIdInputInPopup().sendKeys(Keys.TAB);
        LOG.info("        Entered Payor ID: " + pyrId);
    }

    /*** Payor Search ***/
    //Payor Info
    public WebElement payorIdInput() throws Exception
    {
        return driver.findElement(By.id("payorAbbrev"));
    }

    public WebElement payorNameInput() throws Exception
    {
        return driver.findElement(By.id("payorName"));
    }

    public WebElement payorGroupNameInput() throws Exception
    {
        return driver.findElement(By.id("payorGroupName"));
    }

    public WebElement clearingHouseIdInput() throws Exception
    {
        return driver.findElement(By.id("clearingHouseId"));
    }

    public WebElement includeSuspendedPayors1Input() throws Exception
    {
        return driver.findElement(By.id("includeSuspendedPayors1"));
    }

    //Cross-Reference Info
    public WebElement xrefTypeInput() throws Exception
    {
        return driver.findElement(By.id("xrefType"));
    }

    public WebElement xrefMemberInput() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='s2id_xrefMemberDroplist']/a/span[1]"));
    }

    public WebElement dispXrefData1Input() throws Exception
    {
        return driver.findElement(By.id("dispXrefData1"));
    }

    //Address Info
    public WebElement addressInput() throws Exception
    {
        return driver.findElement(By.id("address"));
    }

    public WebElement cityInput() throws Exception
    {
        return driver.findElement(By.id("city"));
    }

    public WebElement stateDroplistInput() throws Exception
    {
        return driver.findElement(By.id("stateDroplist"));
    }

    public WebElement zipInput() throws Exception
    {
        return driver.findElement(By.id("zip"));
    }

    public WebElement countryDroplistInput() throws Exception
    {
        return driver.findElement(By.id("countryDroplist"));
    }

    public WebElement phoneInput() throws Exception
    {
        return driver.findElement(By.id("phone"));
    }

    public WebElement closeBtnInPayorSearch() throws Exception
    {
        return driver.findElement(By.xpath("html/body/section/div/div/div/button[3]"));
    }

    public WebElement resetBtnInPayorSearch() throws Exception
    {
        return driver.findElement(By.xpath("html/body/section/div/div/div/button[2]"));
    }

    public WebElement searchBtnInPayorSearch() throws Exception
    {
        return driver.findElement(By.xpath("html/body/section/div/div/div/button[1]"));
    }

    /*** Sub Service Search***/
    public WebElement subServiceSearchTitle() throws Exception
    {
        return driver.findElement(By.id("jqgh_tbl_eligibilitysubservicesearch_eligSubSvcName"));
    }

    public WebElement loadingIconInSubSerSearch() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='refresh_tbl_eligibilitysubservicesearch']/div/span"));
    }

    public WebElement totalRecordInSubSerSearch() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='eligibilitySubServiceSearch']/div[2]/div[5]/div[1]/table/tbody/tr/td[3]/div"));
    }

    public void setPayorNameField(String pyrName) throws Exception
    {
        payorNameTxtBox().clear();
        payorNameTxtBox().sendKeys(pyrName);
        payorNameTxtBox().sendKeys(Keys.TAB);
        LOG.info("Enter Payor Name");
    }

    public void setOutPyrIdField(String outPyrId) throws Exception
    {
        outPyrIdTxtBox().clear();
        outPyrIdTxtBox().sendKeys(outPyrId);
        outPyrIdTxtBox().sendKeys(Keys.TAB);
        LOG.info("Entered Outgoing Payor Id " + outPyrId);
    }

    public void setDaysToCheckElig(String days) throws Exception
    {
        daysToCheckEligInput().clear();
        daysToCheckEligInput().sendKeys(days);
        daysToCheckEligInput().sendKeys(Keys.TAB);
        LOG.info("Entered Days To Check Elig: " + days);
    }

    public void setSubServiceField(String subSvc) throws Exception
    {
        subServiceTxtBox().clear();
        subServiceTxtBox().sendKeys(subSvc);
        subServiceTxtBox().sendKeys(Keys.TAB);
        LOG.info("Enter Sub Service");
    }

    public void setNewPayorId(String newPyrId, WebDriverWait wait)
    {
        WebElement newPayorIdInput = newPayorIdInput();
        wait.until(ExpectedConditions.elementToBeClickable(newPayorIdInput));
        newPayorIdInput.clear();
        newPayorIdInput.sendKeys(newPyrId);
        newPayorIdInput.sendKeys(Keys.TAB);
        LOG.info("Entered New Payor ID: " + newPyrId);
    }

    public void setNewPayorNameField(String newPyrName) throws Exception
    {
        newPayorNameTxtBox().clear();
        newPayorNameTxtBox().sendKeys(newPyrName);
        newPayorNameTxtBox().sendKeys(Keys.TAB);
        LOG.info("Entered New Payor Name " + newPyrName);
    }

    public void setResponse(String resp, WebDriverWait wait)
    {
        WebElement responseTxtBox = responseTxtBox();
        wait.until(ExpectedConditions.elementToBeClickable(responseTxtBox));
        responseTxtBox.clear();
        responseTxtBox.sendKeys(resp);
        responseTxtBox.sendKeys(Keys.TAB);
        LOG.info("Entered Response: " + resp);
    }

    public WebElement prioDropDownList() throws Exception
    {
        return driver.findElement(By.id("prio"));
    }

    public void setPrio(String value) throws Exception
    {
        SeleniumBaseTest b = new SeleniumBaseTest();
        prioDropDown().click();
        b.selectItemByVal(prioDropDownList(), value);
        LOG.info("Selected Prio: " + value + " from Dropdown List.");
    }

    public void setPayorIdFilterInPayorSetupTbl(String value) throws Exception
    {
        payorIdFilterInputInPayorSetupTbl().click();
        payorIdFilterInputInPayorSetupTbl().clear();
        payorIdFilterInputInPayorSetupTbl().sendKeys(value);
        payorIdFilterInputInPayorSetupTbl().sendKeys(Keys.TAB);
        LOG.info("Entered Payor ID " + value + " in Payor Setup grid Payor ID filter.");
    }

    public void setPayorIdSearchOnEliResponseTranslationTbl(String value) throws Exception
    {
        payorIdSearchBoxEliResponseTranslationTbl().click();
        payorIdSearchBoxEliResponseTranslationTbl().clear();
        payorIdSearchBoxEliResponseTranslationTbl().sendKeys(value);
        payorIdSearchBoxEliResponseTranslationTbl().sendKeys(Keys.TAB);
        LOG.info("Enter Payor Id to Search Box");
    }

    //Validation Follow-Up Action Code Configuration
    public WebElement titleValidationFollowUpActionCodeConfigTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[4]/div/div/section/header/div[1]/span/span"));
    }

    public WebElement addBtnValidationFollowUpActionCodeConfigTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='add_tbl_eligibilityValidationFollowUp']/button"));
    }

    public WebElement followUpActionCodeDropDown() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='s2id_eligFollowUpActnTypId']/a"));
    }

    public WebElement actionDropDown() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='s2id_eligValidationActnTypId']/a"));
    }

    public WebElement curentPageValidationFollowUpActionCodeConfigTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_eligibilityValidationFollowUp_pagernav_center']/table/tbody/tr/td[4]/input"));
    }

    public WebElement firstPageIconValidationFollowUpActionCodeConfigTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='first_tbl_eligibilityValidationFollowUp_pagernav']/span"));
    }

    public WebElement prevPageIconValidationFollowUpActionCodeConfigTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='prev_tbl_eligibilityValidationFollowUp_pagernav']/span"));
    }

    public WebElement nextPageIconValidationFollowUpActionCodeConfigTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='next_tbl_eligibilityValidationFollowUp_pagernav']/span"));
    }

    public WebElement lastPageIconValidationFollowUpActionCodeConfigTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='last_tbl_eligibilityValidationFollowUp_pagernav']/span"));
    }

    public WebElement recordsListboxValidationFollowUpActionCodeConfigTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_eligibilityValidationFollowUp_pagernav_center']/table/tbody/tr/td[8]/select"));
    }

    public WebElement deleteBtnValidationFollowUpActionCodeConfigTbl() throws Exception
    {
        return driver.findElement(By.id("jqgh_tbl_eligibilityValidationFollowUp_deleted"));
    }

    public WebElement totalRecordOnValidationFollowUpActionCodeConfigTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_eligibilityValidationFollowUp_pagernav_right']/div"));
    }

    public WebElement followUpActionCodeSearchBox() throws Exception
    {
        return driver.findElement(By.id("gs_eligFollowUpActnTypId"));
    }

    public WebElement payorIdSearchBoxPayorSetupTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_payorSetup']//input[@id = 'gs_pyrAbbrv']"));
    }

    public WebElement select2DropInput()
    {
        return driver.findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
    }

    public WebElement validationFollowUpActionCodeConfigTblCelData(int row, int col) throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_eligibilityValidationFollowUp']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement getCellInputInValidationFollowUpActionCodeConfigTbl(int row, int col) throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_eligibilityValidationFollowUp']/tbody/tr[" + row + "]/td[" + col + "]/input"));
    }

    public WebElement dataLockError() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='sectionServerMessages']/div/div[1]/ul/li/p"));
    }
//		public void setFololowUpActnIdSearch(String value) throws Exception{
//			followUpActionCodeSearchBox().clear();
//			followUpActionCodeSearchBox().sendKeys(value);
//			followUpActionCodeSearchBox().sendKeys(Keys.TAB);
//			LOG.info("        Enter Follow-Up Action ID to search box");
//		}

    public void setFollowUpActionCode(String value) throws Exception
    {
        followUpActionCodeDropDown().click();
        select2DropInput().sendKeys(value);
        select2DropInput().sendKeys(Keys.TAB);
        LOG.info("Selected Follow-Up Action ID " + value);
    }

    public void setAction(String value) throws Exception
    {
        actionDropDown().click();
        select2DropInput().sendKeys(value);
        select2DropInput().sendKeys(Keys.TAB);
        LOG.info("Selected Action " + value);
    }

    /*** Validation Reject Reason Code Override Configuration ***/

    public WebElement valRejectResonTitle() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[4]/div/div/section/header/div[1]/span/span"));
    }

    public WebElement rejectReasonInput() throws Exception
    {
        return driver.findElement(By.id("gs_eligRejectRsnTypId"));
    }

    public WebElement actionInput() throws Exception
    {
        return driver.findElement(By.id("gs_eligValidationActnTypId"));
    }

    public WebElement deleteInput() throws Exception
    {
        return driver.findElement(By.id("gs_deleted"));
    }

    public WebElement valRejectReasonTbl() throws Exception
    {
        return driver.findElement(By.id("tbl_eligibilityValidationRejectReason"));
    }

    public WebElement addBtnOfValRejectReasonTbl() throws Exception
    {
        return driver.findElement(By.id("add_tbl_eligibilityValidationRejectReason"));
    }

    public WebElement totalRecordOfValRejectReasonTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_eligibilityValidationRejectReason_pagernav_right']/div"));
    }

    public WebElement addRecordTitleOfValRejectReasonTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='edithdtbl_eligibilityValidationRejectReason']/span"));
    }

    public WebElement rejectReasonCodeInputOfValRejectReasonTbl() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='s2id_eligRejectRsnTypId']/a/span[1]"));
    }

    public WebElement msgReturnedText() throws Exception
    {
        return driver.findElement(By.xpath("//*[@id='sectionServerMessages']/div/div[2]/ul"));
    }

    public WebElement helpIconInValidationFollowUpAction() throws Exception
    {
        return driver.findElement(By.xpath("//*[@id='mainSections']/div[3]/div/div/section/div/div[1]/a"));
    }

    public WebElement helpIconInValidationRejectReasonCode() throws Exception
    {
        return driver.findElement(By.xpath("//*[@id='mainSections']/div[4]/div/div/section/div/div[1]/a"));
    }

    public void enterToRejectReasonInput(String value) throws Exception
    {
        rejectReasonInput().clear();
        rejectReasonInput().sendKeys(value);
        rejectReasonInput().sendKeys(Keys.TAB);
        LOG.info("        Enter reject Reason Input: " + value);
    }

    public void enterToActionInput(String value) throws Exception
    {
        actionInput().clear();
        actionInput().sendKeys(value);
        actionInput().sendKeys(Keys.TAB);
        LOG.info("        Enter action Input: " + value);
    }

    public void enterEligibilityServiceInfos(String loginId, String className, String name, String password, String exceptAlert, String delay, String timeout) throws Exception
    {
        SeleniumBaseTest b = new SeleniumBaseTest();

        b.isElementPresent(loginIDInput(), 5);
        loginIDInput().clear();
        loginIDInput().sendKeys(loginId);
        loginIDInput().sendKeys(Keys.TAB);

        b.isElementPresent(classNameInput(), 5);
        classNameInput().clear();
        classNameInput().sendKeys(className);
        classNameInput().sendKeys(Keys.TAB);

        b.isElementPresent(nameInput(), 5);
        nameInput().clear();
        nameInput().sendKeys(name);
        nameInput().sendKeys(Keys.TAB);

        b.isElementPresent(passwordIDInput(), 5);
        passwordIDInput().clear();
        passwordIDInput().sendKeys(password);
        passwordIDInput().sendKeys(Keys.TAB);

        b.isElementPresent(exceptionAlertThreadholdInput(), 5);
        exceptionAlertThreadholdInput().clear();
        Thread.sleep(2000);
        exceptionAlertThreadholdInput().sendKeys(exceptAlert);
        exceptionAlertThreadholdInput().sendKeys(Keys.TAB);

        b.isElementPresent(serverDelayInput(), 5);
        serverDelayInput().clear();
        Thread.sleep(2000);
        serverDelayInput().sendKeys(delay);
        serverDelayInput().sendKeys(Keys.TAB);

        b.isElementPresent(serverTimeoutInput(), 5);
        serverTimeoutInput().clear();
        Thread.sleep(2000);
        serverTimeoutInput().sendKeys(timeout);
        serverTimeoutInput().sendKeys(Keys.TAB);

        LOG.info("Entered Eligibility Configuration Information with: Login ID = " + loginId + ": Classname = " + className + ": Name = " + name + ": Password = " + password + ": Exception Alert Threshold = " + exceptAlert + ": Server Delay: " + delay + ": Server Timout = " + timeout);
    }

    public void enterToPyrIdInPayorSearch(String value) throws Exception
    {
        payorIdInput().clear();
        payorIdInput().sendKeys(value);
        payorIdInput().sendKeys(Keys.TAB);
        LOG.info("        Enter Payor Id Input: " + value);
    }

    public WebElement rejectReasonSectionrejectReasonCdDropdown() throws Exception
    {
        return driver.findElement(By.xpath("//*[@id='s2id_eligRejectRsnTypId']/a[1]"));
    }

    public WebElement rejectReasonSectionActionDropdown() throws Exception
    {
        return driver.findElement(By.xpath("//*[@id='s2id_eligValidationActnTypId']/a[1]/span[1]"));
    }

    public WebElement addNewEligibilityValidationRejectReasonBtn() throws Exception
    {
        return driver.findElement(By.xpath("//*[@id='add_tbl_eligibilityValidationRejectReason']/button[1]/div[1]"));
    }

    public WebElement inputFieldsOnDropdown() throws Exception
    {
        return driver.findElement(By.xpath("//*[@id='select2-drop']/div[1]/input[1]"));
    }

    public WebElement addNewEligibilityResponseTranslationBtn() throws Exception
    {
        return driver.findElement(By.xpath("//*[@id='add_tbl_eligibilityResponseTranslation']/button[1]/div[1]"));
    }

    /*** Eligibility Sub Service Search Results ***/

    public WebElement eligibilitySubSvcSearchTbl() throws Exception
    {
        return driver.findElement(By.id("eligibilitySubSvcSearch"));
    }

    public WebElement eligibilitySubSvcSearchCelData(int row, int col) throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_eligibilitySubSvcSearch']/tbody/tr[" + row + "]/td[" + col + "]/a"));
    }

    public WebElement eligibilitySubSvcSearchTblTitle() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_eligibilitySubSvcSearch']/div[1]/span"));
    }

    public WebElement rowOnPayorSetupTblPyorSetupSection(int row, int col) throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_payorSetup']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement editPayorSetupPopup() throws Exception
    {
        return driver.findElement(By.id("edithdtbl_payorSetup"));
    }


    public WebElement deleteCheckBoxValidationRejectReasonCodeTbl(int row, int col) throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_eligibilityValidationRejectReason']/tbody/tr[" + row + "]/td[" + col + "]/input"));
    }

    public WebElement helpIconInServiceSearchResult() throws Exception
    {
        return driver.findElement(By.id("pageHelpLink"));
    }

    public WebElement payorSetupTblCelInput(int row, int col) throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_payorSetup']/tbody/tr[" + row + "]/td[" + col + "]/input"));
    }

    public WebElement titleTextInHelp() throws Exception
    {
        return driver.findElement(By.xpath("/html/body/div[1]/h1"));
    }

    public WebElement helpIconInLoadPage() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id=\"fileMaintEligibilityConfigForm\"]/div[1]/div[2]/section/div/div[1]/a"));
    }

    public WebElement helpIconInFooterSection() throws Exception
    {
        return driver.findElement(By.id("pageHelpLink"));
    }

    public WebElement auditDetailTblRowCol(int row, int col) throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement totalRecordsInAuditDetail() throws Exception
    {
        return driver.findElement(By.xpath(".//*[@id='pager_right']/div"));
    }

    public WebElement errMsgInAddRecordPopup() throws Exception
    {
        //return driver.findElement(By.xpath(".//*[@id='FormError']/td/span"));
        return driver.findElement(By.id("FormError"));
    }

    public WebElement eligibilityResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.id("tbl_eligibilityResponseTranslation"));
    }

    public WebElement eligibilityValidationFollowUpTbl() throws Exception
    {
        return driver.findElement(By.id("tbl_eligibilityValidationFollowUp"));
    }

    public WebElement newPayorIdFilterInputInEligResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.id("gs_newPyrAbbrv"));
    }

    public WebElement payorIdFilterInputInEligResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.id("gs_pyrAbbrv"));
    }

    public WebElement responseFilterInputInEligResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.id("gs_resp"));
    }

    public WebElement matchTypeFilterInputInEligResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.id("gs_eligRespMatchTypId"));
    }

    public WebElement benefitTypeFilterInputInEligResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.id("gs_eligRespBenefitTypId"));
    }

    public WebElement followUpActionCodeFilter() throws Exception
    {
        return driver.findElement(By.id("gs_eligFollowUpActnTypId"));
    }

    public WebElement actionFilterInValidationFollowUp() throws Exception
    {
        return driver.findElement(By.id("gs_eligValidationActnTypId"));
    }

    public WebElement rejectReasonCodeFilter() throws Exception
    {
        return driver.findElement(By.id("gs_eligRejectRsnTypId"));
    }

    public WebElement matchType()
    {
        return driver.findElement(By.id("s2id_eligRespMatchTypId"));
    }

    public WebElement benefitType()
    {
        return driver.findElement(By.id("s2id_eligRespBenefitTypId"));
    }

    public WebElement serviceTypeCode()
    {
        return driver.findElement(By.id("s2id_eligRespSvcCdTypId"));
    }

    public WebElement allowSecondaryTranslationCheckBox() throws Exception
    {
        return driver.findElement(By.id("allowSecondary"));
    }

    public WebElement bypassUnknownResponseErrorsCheckBox() throws Exception
    {
        return driver.findElement(By.id("allowUnmapped"));
    }

    public boolean isEligSvcLoaded(String eligSvcId, WebDriverWait wait) throws Exception
    {
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_MAIN_SECTIONS))));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_SAVE_AND_CLEAR_BUTTON))));
        return StringUtils.equalsIgnoreCase(StringUtils.trim(eligSvcIDInput().getAttribute("value")), eligSvcId);

    }

    public void setPrioDropDown(String value) throws Exception
    {
        prioDropDown().click();
        select2DropInput().sendKeys(value);
        select2DropInput().sendKeys(Keys.TAB);
        LOG.info("Selected PrioDropDown by value");
    }

    public void setMatchTypeDropDown(String matchType, WebDriverWait wait) throws Exception
    {
        WebElement matchTypeDropDown = matchType();
        wait.until(ExpectedConditions.elementToBeClickable(matchTypeDropDown));
        matchTypeDropDown.click();
        select2DropInput().sendKeys(matchType+Keys.ENTER);
        LOG.info("Entered Input Into MatchType DropDown: " + matchType);
    }

    public void setBenefitTypeDropDown(String benefitType, WebDriverWait wait)
    {
        WebElement benefitTypeDropDown = benefitType();
        wait.until(ExpectedConditions.elementToBeClickable(benefitTypeDropDown));
        benefitTypeDropDown.click();
        select2DropInput().sendKeys(benefitType+Keys.ENTER);
        LOG.info("Entered Input Into BenefitType DropDown: " + benefitType);
    }

    public void setServiceTypeCode(String svcTypeCode, WebDriverWait wait)
    {
        WebElement svcTypeCodeDropDown = serviceTypeCode();
        wait.until(ExpectedConditions.elementToBeClickable(svcTypeCodeDropDown));
        svcTypeCodeDropDown.click();
        select2DropInput().sendKeys(svcTypeCode+Keys.ENTER);
        LOG.info("Entered Input Into serviceTypeCode DropDown: " + svcTypeCode);
    }

    public void checkAllowSecondaryTranslationCheckBox() throws Exception
    {
        allowSecondaryTranslationCheckBox().click();
        LOG.info("Selected allow Secondary Translation Check Box");
    }

    public void checkBypassUnknownResponseErrorsCheckBox() throws Exception
    {
        bypassUnknownResponseErrorsCheckBox().click();
        LOG.info("Selected Bypass Unknown Response Errors Check Box");
    }

    public void checkCheckEligCheckBox() throws Exception
    {
        checkEligCheckBox().click();
        LOG.info("Selected check Elig Check Box");
    }

    public void checkDeleteCheckBox() throws Exception
    {
        deleteCheckBox().click();
        LOG.info("Selected delete Check Box");
    }

//    public void navigateToEligTranslationScreen(WebDriverWait wait) throws Exception
//    {
//        HeaderNavigation headerNavigation = new HeaderNavigation(driver, config);
//        headerNavigation.fileMaintenanceTab();
//        FileMaintenanceNavigation fileMaintenanceNavigation = new FileMaintenanceNavigation(driver, config);
//        fileMaintenanceNavigation.eligibilityTranslationLink();
//        EligibilityResponseTranslation eligibilityTranslation = new EligibilityResponseTranslation(driver, config, wait);
//        driver.switchTo().frame(eligibilityTranslation.contentFrame());
//        driver.switchTo().frame(eligibilityTranslation.platformiFrame());
//        wait.until(ExpectedConditions.textToBePresentInElement(eligibilityTranslationTitle(), "Eligibility Translation"));
//    }

    public void clickSaveAndClear(WebDriverWait wait)
    {
        WebElement saveAndClearButton = saveAndClearBtn();
        wait.until(ExpectedConditions.elementToBeClickable(saveAndClearButton));
      //  saveAndClearButton.click();
        saveAndClearButton.sendKeys(Keys.ALT+"S");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_SAVE_AND_CLEAR_BUTTON)));
    }
    public WebElement subIdTypeFilterInputInEligResponseTranslationTbl() throws Exception
    {
        return driver.findElement(By.id("gs_eligRespSubsRefTypId"));
    }
    public WebElement subIdTypeDropDownList() throws Exception
    {
        return driver.findElement(By.id("s2id_eligRespSubsRefTypId"));
    }

    public void setSubIdTypeDropDownList(String value, WebDriverWait wait) throws Exception
    {
        WebElement subIdTypeDropDownList = subIdTypeDropDownList();
        wait.until(ExpectedConditions.elementToBeClickable(subIdTypeDropDownList));
        subIdTypeDropDownList.click();
        select2DropInput().sendKeys(value+Keys.ENTER);
        LOG.info("Selected Subscriber ID Type: " + value + " from Dropdown List.");
    }
}
