package com.overall.accession.accessionProcessing;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.rpm.AccessionDao;
import com.xifin.qa.dao.rpm.AccessionDaoImpl;
import com.xifin.qa.dao.rpm.PayorDao;
import com.xifin.qa.dao.rpm.PayorDaoImpl;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class AccessionDetail
{
    private static final Logger LOG = Logger.getLogger(AccessionDetail.class);

    private static final String ID_MAIN_SECTIONS = "mainSections";
    private static final String ID_SAVE_BUTTON = "btnSave";
    private static final String ID_ACCN_ID_TEXT = "accnId";
    private static final String ID_ACCN_ID_INPUT = "lookupAccnId";
    private static final String ID_PAYOR_TABS = "payorTabs";
    private static final String ID_PATIENT_FIRST_NAME = "patientFirstName";
    private static final String ID_PATIENT_LAST_NAME = "patientLastName";
    private static final String NAME_PATIENT_GENDER = "patientGender";
    private static final String NAME_INSURED_GENDER = "insuredGender";
    private static final String ID_INSURED_FIRST_NAME = "insuredFirstName";
    private static final String ID_INSURED_LAST_NAME = "insuredLastName";
    private static final String ID_INSURED_ZIP_CODE = "insuredZip";
    private static final String ID_INSURED_CITY = "insuredCity";
    private static final String ID_CLN_ABBRV_INPUT = "clientAbbrev";

    private final RemoteWebDriver driver;
    private final Configuration config;
    private final WebDriverWait wait;
    private int rowNumInTable;
    private final AccessionDao accessionDao;
    private final PayorDao payorDao;

    public AccessionDetail(RemoteWebDriver driver, Configuration config, WebDriverWait wait)
    {
        this.driver = driver;
        this.config = config;
        this.wait=wait;
        accessionDao = new AccessionDaoImpl(config.getRpmDatabase());
        payorDao = new PayorDaoImpl(config.getRpmDatabase());
    }

    public WebElement accnIdInput()
    {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ACCN_ID_INPUT)));
    }

    public WebElement primaryPyrSubsIdInput()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#payor_tab_A #subscriberId')[0]");
    }

    public WebElement accnIdText()
    {
        return driver.findElement(By.id(ID_ACCN_ID_TEXT));
    }

    public WebElement dosText()
    {
        return driver.findElement(By.xpath("/html/body/section/div/div/div[2]/div[1]/div/div[2]/div[3]/div[1]/div[2]/span"));
    }

    public WebElement dobText()
    {
        return driver.findElement(By.xpath("/html/body/section/div/div/div[2]/div[1]/div/div[2]/div[2]/div[3]/div[2]/span"));
    }

    public WebElement ptFullNameText()
    {
        return driver.findElement(By.xpath("/html/body/section/div/div/div[2]/div[1]/div/div[2]/div[2]/div[1]/div[2]/div"));
    }

    public WebElement clientIdText()
    {
        return driver.findElement(By.id("clientAbbrev"));
    }

    public WebElement accessionStatusText()
    {
        return driver.findElement(By.xpath("/html/body/section/div/div/div[2]/div[1]/div/div[2]/div[4]/div[1]/div[2]/span"));
    }

    public WebElement rePriceCheckbox()
    {
        return driver.findElement(By.id("reprice"));
    }

    public WebElement translateProcsCheckbox()
    {
        return driver.findElement(By.id("isTranslateProcs"));
    }

    public WebElement accessionErrorsGridText()
    {
        return driver.findElement(By.cssSelector("#sectionErrors > section > header > div:nth-child(1) > span > a"));
    }

    public WebElement showAllUnfixedErrRadioBtn()
    {
        return driver.findElement(By.id("radioUnFixedError"));
    }

    public WebElement showAllErrorsRadioBtn()
    {
        return driver.findElement(By.id("radioAllErrors"));
    }

    public WebElement currentAccnErrTable()
    {
        return driver.findElement(By.id("tbl_accnCurrentErrors"));
    }

    public WebElement fixedAccnErrTable()
    {
        return driver.findElement(By.id("tbl_accnFixedErrors"));
    }

    public WebElement contactDetailTable()
    {
        return driver.findElement(By.id("tbl_contactDetail"));
    }

    public WebElement resetBtn()
    {
        return driver.findElement(By.id("Reset"));
    }

    public WebElement saveBtn()
    {
        return driver.findElement(By.id(ID_SAVE_BUTTON));
    }

    public WebElement fixErrorCheckboxInAccessionError(int row)
    {
        return driver.findElement(By.xpath("//*[@id='tbl_accnCurrentErrors']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_accnCurrentErrors_fixedError']/input"));
    }

    public WebElement unfixErrorCheckboxInAccessionError(int row)
    {
        return driver.findElement(By.xpath("//*[@id='tbl_accnCurrentErrors']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_accnCurrentErrors_unfixedError']/input"));
    }

    public WebElement createNewEPILink()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$('.btnCreateNewPatientId').click()[0]");
    }

    public WebElement patientSearchBtn()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$(\"a[title='Patient Search']\").click()[0]");
    }

    public WebElement saveInProgressInfoText()
    {
        return driver.findElement(By.id("messagefor_message0"));
    }

    public WebElement accnErrorText(int row, int col)
    {
        return driver.findElement(By.xpath("//*[@id='tbl_accnCurrentErrors']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement reasonCodeText(int row, int col)
    {
        return driver.findElement(By.xpath(".//*[@id='" + row + "']/td[" + col + "]"));
    }

    public WebElement fixedAccnErrorText(int row, int col)
    {
        return driver.findElement(By.xpath("//*[@id='tbl_accnFixedErrors']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement contactNotesGridText()
    {
        return driver.findElement(By.cssSelector("#sectionNotes > section > header > div:nth-child(1) > span > span"));
    }

    public WebElement contactNotesAddBtn()
    {
        return driver.findElement(By.id("add_tbl_contactDetail"));
    }

    public WebElement diagnosisDetailAddBtn()
    {
        return driver.findElement(By.id("add_tbl_accnDiagnosisDetail"));
    }

    public WebElement contactNotesCurrentView()
    {
        return driver.findElement(By.cssSelector("#sectionNotes > section > header > div > span > a"));
    }

    public WebElement physicianInfoGridText()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#physicianInfoSection [class=\"titleText\"]')[0]");
    }

    public WebElement physicianInfoCurrentView()
    {
        return driver.findElement(By.cssSelector("#physicianInfoSection > div > section > header > div:nth-child(1) > span > a"));
    }

    public WebElement physicianSearchBtn()
    {
        return driver.findElement(By.cssSelector("#physicianInfoSection > div > section > div > div.detailView > div:nth-child(2) > div.unit.size10.nopad > div > a"));
    }

    public WebElement npiInput()
    {
        return driver.findElement(By.name("orderingPhysicianNpi"));
    }

    public WebElement npiSummaryViewText()
    {
        return driver.findElement(By.cssSelector("#physicianInfoSection > div > section > div > div.summaryView > div > div:nth-child(4) > span"));
    }

    public WebElement upinInput()
    {
        return driver.findElement(By.name("orderingPhysicianUpin"));
    }

    public WebElement upinSummaryViewText()
    {
        return driver.findElement(By.cssSelector("#physicianInfoSection > div > section > div > div.summaryView > div > div:nth-child(6) > span"));
    }

    public WebElement physNameText()
    {
        return driver.findElement(By.cssSelector("#physicianInfoSection > div > section > div > div.detailView > div:nth-child(2) > div.unit.size20 > span"));
    }

    public WebElement physNameSummaryViewText()
    {
        return driver.findElement(By.cssSelector("#physicianInfoSection > div > section > div > div.summaryView > div > div.unit.size20 > span"));
    }

    public WebElement taxonomyCodeText()
    {
        return driver.findElement(By.cssSelector("#physicianInfoSection > div > section > div > div.detailView > div:nth-child(2) > div.unit.lastUnit > span"));
    }

    public WebElement taxonomyCodeSummaryViewText()
    {
        return driver.findElement(By.cssSelector("#physicianInfoSection > div > section > div > div.summaryView > div > div.unit.lastUnit > span"));
    }

    public WebElement epiInput()
    {
        return driver.findElement(By.id("patientId"));
    }

    public WebElement searchInProgress()
    {
        return driver.findElement(By.className("xf_message_content"));
    }

    public WebElement searchInProgressMessageTitle()
    {
        return driver.findElement(By.xpath("//div[@class='xf_message_title')) and text()='Search in progress']"));
    }

    public WebElement clientPatientIdInput()
    {
        return driver.findElement(By.id("clientPatientId"));
    }

    public WebElement clientPrimaryFacilityPatientIdInput()
    {
        return driver.findElement(By.id("clientPrimaryFacilityPatientId"));
    }

    public WebElement currentDiagnosesGridText()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#sectionDiagnosis .titleText').click()[0]");
    }

    public WebElement currentDiagnosesCurrentView()
    {
        return driver.findElement(By.cssSelector("#sectionDiagnosis > div > section > header > div:nth-child(1) > span > a"));
    }

    public WebElement accnDiagnosisDetailTable()
    {
        return driver.findElement(By.id("tbl_accnDiagnosisDetail"));
    }

    public WebElement accnDiagnosisDetailVoiedTable()
    {
        return driver.findElement(By.id("tbl_accnDiagnosisDetailVoided"));
    }

    public WebElement unSavedDiagnosisDetailText(int row, int col)
    {
        return driver.findElement(By.xpath("//*[@id='jqg" + row + "'" + "]/td[" + col + "]"));
    }

    public WebElement checkDeleteDiagnosisCheckbox(int row)
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#tbl_accnDiagnosisDetail [class=\"deleteCell usedInline\"] [type=\"checkbox\"]')[" + row + "].click()");
    }

    public WebElement primaryPayorIDInput()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#payor_tab_A #payorAbbrev')[0]");
    }

    public WebElement secondaryPayorIDInput()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#payor_tab_B #payorAbbrev')[0]");
    }

    public WebElement secondaryPayorSubsIdInput()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#payor_tab_B #subscriberId')[0]");
    }

    public WebElement secondaryPayorRelationshipDropdown()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#payor_tab_B #insuredRelationship')[0]");
    }

    public WebElement addPayorLink()
    {
        return driver.findElement(By.id("btnAddPayor"));
    }

    public WebElement occurrenceCodeAddBtn()
    {
        return driver.findElement(By.id("add_tbl_occurrenceCodes"));
    }

    public WebElement valueCodeAddBtn()
    {
        return driver.findElement(By.id("add_tbl_valueCodes"));
    }

    public WebElement returnedErrorText()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#sectionServerMessages [class=\"serverErrorsList\"]')[0]");
    }

    public String diagnosisCodeInSummaryView(String titleStr)
    {
        String str = "return $('#sectionDiagnosis [class*=\"diagnosisSummaryItemTemplate\"][title=\"" + titleStr + "\"]').text()";
        return (String) ((JavascriptExecutor) driver).executeScript(str);
    }

    public WebElement occurrenceCodesTable()
    {
        return driver.findElement(By.id("tbl_occurrenceCodes"));
    }

    public WebElement valueCodesTable()
    {
        return driver.findElement(By.id("tbl_valueCodes"));
    }

    public WebElement checkOccurrenceCodeDeleteCheckbox(int row)
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#tbl_occurrenceCodes [class=\"deleteCell usedInline\"] [type=\"checkbox\"]')[" + row + "].click()");
    }

    public WebElement checkValueCodeDeleteCheckbox(int row)
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#tbl_valueCodes [class=\"deleteCell usedInline\"] [type=\"checkbox\"]')[" + row + "].click()");
    }

    public WebElement orderedTestViewDropDown()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#sectionTransactions [class=\"viewController autoWidth select2-offscreen\"]')[0]");
    }

    public WebElement orderedTestDetailTable()
    {
        return driver.findElement(By.id("tbl_orderedTestDetail"));
    }

    public WebElement orderedTestDetailEditBtn()
    {
        return driver.findElement(By.id("edit_tbl_orderedTestDetail"));
    }

    public WebElement billableProcCodeDetailsTable()
    {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tbl_billableProcedureCodeDetails")));
    }

    public WebElement billableProcCodeDetailsText(int row, int col)
    {
        String str = "//*[@id='tbl_billableProcedureCodeDetails']/tbody/tr[" + row + "]/td[" + col + "]";
        return driver.findElement(By.xpath(str));
    }

    public WebElement billableProcCodeDetailsEditRowLnk(int row)
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#messagefor_" + row + " a.btnEditRow').click()");
    }

    public String renalColText()
    {
        String str = "return $('#tbl_orderedTestDetail_renal').attr(\"style\")";
        return (String) ((JavascriptExecutor) driver).executeScript(str);
    }

    public String errorReturnedText()
    {
        String str = "return $('#sectionServerMessages [class=\"serverErrorsTitle\"] [class=\"emphasis\"]').text()";
        return (String) ((JavascriptExecutor) driver).executeScript(str);
    }

    public WebElement billableProcCodeDetailsAddAdjLnk(int row)
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#messagefor_" + row + " a.btnAddAdjustment').click()");
    }

    public WebElement pyrSummaryTable()
    {
        return driver.findElement(By.id("tbl_payorSummary"));
    }

    public WebElement adjSummaryTable()
    {
        return driver.findElement(By.id("tbl_adjustmentSummary"));
    }

    public WebElement addOverrideLink()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$('[class=\"btnAddOverride actionLink\"]').click()");
    }

    public WebElement bulkPmtViewDropDown()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"bulkPaymentsAndDenialsControls\"] [class*=\"viewController autoWidth select2-offscreen\"]')[0]");
    }

    public WebElement bulkPmtAndDenialsTable()
    {
        return driver.findElement(By.id("tbl_bulkPaymentsAndDenials"));
    }

    public WebElement webTableText(String tableName, int row, int col)
    {
        return driver.findElement(By.xpath("//*[@id='" + tableName + "']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement postalCodeSearchBtn()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$('a[title=\"Zip Code Search\"]')[0].click()");
    }

    public WebElement primaryPayorAbbrText()
    {
        return driver.findElement(By.xpath(".//*[@id='payor_tab_A']/div/div[2]/div/form/section/div/div[2]/div[1]/div[3]/div[2]/span"));
    }

    public WebElement orderedTestAbnReceievedCheckbox(int accnTestSeqId)
    {
        return driver.findElement(By.xpath("//table[@id='tbl_orderedTestDetail']/*/tr[@id='"+String.valueOf(accnTestSeqId)+"']/td[@data-field-id='abnReceived']/input"));
    }

    public WebElement clnIdInput() { return driver.findElementById(ID_CLN_ABBRV_INPUT); }

    public void setBulkPmtView(String str)
    {
        Select oSelection = new Select(bulkPmtViewDropDown());
        oSelection.selectByValue(str);
    }

    public void setOrderedTestView(String str)
    {
        Select oSelection = new Select(orderedTestViewDropDown());
        oSelection.selectByValue(str);
        LOG.info("        Selected View: " + str);
    }

    public void setsecondaryPayorRelationship(String str)
    {
        Select oSelection = new Select(secondaryPayorRelationshipDropdown());
        oSelection.selectByValue(str);
        LOG.info("        Selected Relationship: " + str);
    }

    public void setSecondaryPayorSubsId(String str) throws InterruptedException
    {
        secondaryPayorSubsIdInput().sendKeys(str);
        secondaryPayorSubsIdInput().sendKeys(Keys.TAB);
        Thread.sleep(1000);
        LOG.info("        Entered Subscriber ID: " + str);
    }

    public void setAccnId(String accnId) throws InterruptedException
    {
        Thread.sleep(5000);
        accnIdInput().sendKeys(accnId);
        accnIdInput().sendKeys(Keys.TAB);
        Thread.sleep(5000);
        LOG.info("        Entered AccnID: " + accnId);
    }

    /**
     * Enters the given accession Id in the input field, then TABs out.
     *
     * @param accnId the accession Id
     * @param wait   the wait handler
     */
    public void setAccnId(String accnId, WebDriverWait wait)
    {
        wait.until(ExpectedConditions.elementToBeClickable(accnIdInput()));
        accnIdInput().clear();
        accnIdInput().sendKeys(accnId);
        accnIdInput().sendKeys(Keys.TAB);
    }

    public void setClientPatientId(String id) throws InterruptedException
    {
        clientPatientIdInput().sendKeys(id);
        clientPatientIdInput().sendKeys(Keys.TAB);
        Thread.sleep(1000);
        LOG.info("        Entered Client Patient ID: " + id);
    }

    public void setClientPrimaryFacilityPatientId(String id) throws InterruptedException
    {
        clientPrimaryFacilityPatientIdInput().sendKeys(id);
        clientPrimaryFacilityPatientIdInput().sendKeys(Keys.TAB);
        Thread.sleep(1000);
        LOG.info("        Entered Client's Primary Facility Patient ID: " + id);
    }

    public void setPrimaryPyrId(String pyrId) throws InterruptedException
    {
        primaryPayorIDInput().sendKeys(pyrId);
        primaryPyrSubsIdInput().click();
        Thread.sleep(5000);
        LOG.info("        Entered Primary Payor: " + pyrId);
    }

    public void clearPrimaryPyrId() throws InterruptedException
    {
        primaryPayorIDInput().clear();
        primaryPayorIDInput().sendKeys(Keys.TAB);
        Thread.sleep(2000);
        LOG.info("        Primary Payor was cleared.");
    }

    public void clickSave(WebDriverWait wait)
    {
        WebElement saveButton = saveBtn();
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveButton.click();
        LOG.info("Wait until the Main section is visible");
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_MAIN_SECTIONS))));
        LOG.info("Wait until the ID_PAYOR_TABS section is visible");
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_PAYOR_TABS))));
        LOG.info("Wait until the saveButton section is clickable");
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
    }

    public void clickSave() throws InterruptedException
    {
        saveBtn().click();
        Thread.sleep(3000);
        LOG.info("        Clicked the Save button.");
    }

    public void clickReset()
    {
        resetBtn().click();
        LOG.info("        Clicked the Reset button.");
    }

    public void clickContactNotesAdd()
    {
        contactNotesAddBtn().click();
        LOG.info("        Clicked the Contact Notes Add button.");
    }

    public void setReprice()
    {
        rePriceCheckbox().click();
        LOG.info("        Checked the Reprice checkbox.");
    }

    public void setShowAllUnfixedErr() throws InterruptedException
    {
        showAllUnfixedErrRadioBtn().click();
        Thread.sleep(1000);
        LOG.info("        Checked the Show all unfixed errors radio button.");
    }
    public void setRelevantErrorRadio() throws InterruptedException
    {
        relevantErrorRadio().click();
        Thread.sleep(1000);
        LOG.info("        Checked the Show all Relevant Error Radio.");
    }

    public void setCurrAccnErrAction(int rowNum)
    {
        fixErrorCheckboxInAccessionError(rowNum).click();
        LOG.info("        Clicked the Fix Error checkbox at " + rowNum + " row in Accession Errors grid.");
    }

    public void setFixedAccnErrAction(int rowNum)
    {
        unfixErrorCheckboxInAccessionError(rowNum).click();
        LOG.info("        Clicked the Unfix checkbox at " + rowNum + " row in Accession Errors grid.");
    }

    public void setAccnErrGrid() throws InterruptedException
    {
        accessionErrorsGridText().click();
        Thread.sleep(1000);
        LOG.info("        Clicked the Accession Errors grid text.");
    }

    public void clearPrimaryPyrSubsId() throws InterruptedException
    {
        primaryPyrSubsIdInput().clear();
        primaryPyrSubsIdInput().sendKeys(Keys.TAB);
        Thread.sleep(2000);
        LOG.info("        Clear the subs id for the Primary Payor.");
    }

    public void setPrimaryPyrSubsId(String subsId) throws InterruptedException
    {
        primaryPyrSubsIdInput().sendKeys(subsId);
        primaryPayorIDInput().click();
        primaryPyrSubsIdInput().sendKeys(Keys.TAB);
        Thread.sleep(3000);
        LOG.info("        Entered SubsId: " + subsId + " for the Primary Payor.");
    }

    public void setContactNotesGrid() throws InterruptedException
    {
        contactNotesGridText().click();
        Thread.sleep(1000);
        LOG.info("        Clicked the Contact Notes grid text.");
    }

    public void setPhysicianInfoGrid() throws InterruptedException
    {
        physicianInfoGridText().click();
        Thread.sleep(1000);
        LOG.info("        Clicked the Physician Info grid text.");
    }

    public void clickPhysicianSearch()
    {
        physicianSearchBtn().click();
        LOG.info("        Clicked the Physician Search button.");
    }

    public void clickCreateNewEPILink()
    {
        createNewEPILink();
        LOG.info("        Clicked the Create new EPI link.");
    }

    public void clickPatientSearch()
    {
        patientSearchBtn();
        LOG.info("        Clicked the Patient Search button.");
    }

    public void setCurrentDiagnosesGrid() throws InterruptedException
    {
        currentDiagnosesGridText();
        Thread.sleep(1000);
        LOG.info("        Clicked the Current Diagnoses grid.");
    }

    public int getRowNumber(WebElement tableElement, String colVal1, String colVal2)
    {
        int rowNum = 0;
        List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
        int total_rows = rows.size();
        LOG.info("        Total Row Size " + total_rows);

        for (int i = 1; i < total_rows + 1; i++)
        {
            if (tableElement.equals(currentAccnErrTable()))
            {
                if (accnErrorText(i, 4).getText().trim().equals(colVal1) && accnErrorText(i, 13).getText().trim().equals(colVal2))
                {
                    LOG.info("        Match found in " + currentAccnErrTable().getAttribute("id") + " table at row " + i);
                    rowNum = i;
                    break;
                }
            } else
            {
                if (fixedAccnErrorText(i, 4).getText().trim().equals(colVal1) && fixedAccnErrorText(i, 13).getText().trim().equals(colVal2))
                {
                    LOG.info("        Match found in " + fixedAccnErrTable().getAttribute("id") + " table at row " + i);
                    rowNum = i;
                    break;
                }
            }
        }
        return rowNum;
    }

    public int getTotalRowSize(WebElement tableElement)
    {
        List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
        int total_rows = rows.size();
        LOG.info("        Total Row Size " + total_rows);
        return total_rows;
    }

    public int colIndexInWebTable(WebElement tableElement, String tableName, int colIndex, String colVal, int count)
    {
        int colIndexInner = 0;
        int totalRowCount = getTotalRowSize(tableElement);

        for (int i = 1; i < totalRowCount + 1; i++)
        {
            for (int j = 0; j < count; j++)
            {
                colIndexInner = colIndex - j;
                if (webTableText(tableName, i, colIndexInner).getText().equals(colVal))
                {
                    LOG.info("        Match found in " + tableName + " table at row " + i + " and column " + colIndexInner);
                    break;
                }
            }
        }
        return colIndexInner;
    }

    public int getRowNumberInWebTable(WebElement tableElement, String tableName, String colVal1, int colIndex1)
    {
        int rowNum = 0;
        int totalRowCount = getTotalRowSize(tableElement);

        for (int i = 1; i < totalRowCount + 1; i++)
        {
            if (webTableText(tableName, i, colIndex1).getText().trim().equals(colVal1))
            {
                LOG.info("        Match found in " + tableName + " table at row " + i);
                rowNum = i;
                break;
            }
        }
        return rowNum;
    }

    public int getRowNumberInWebTable(WebElement tableElement, String tableName, String colVal1, String colVal2, int colIndex1, int colIndex2)
    {
        int rowNum = 0;
        int totalRowCount = getTotalRowSize(tableElement);

        for (int i = 1; i < totalRowCount + 1; i++)
        {
            if (webTableText(tableName, i, colIndex1).getText().trim().equals(colVal1) && webTableText(tableName, i, colIndex2).getText().trim().equals(colVal2))
            {
                LOG.info("        Match found in " + tableName + " table at row " + i);
                rowNum = i;
                break;
            }
        }
        return rowNum;
    }

    public int getRowNumberInWebTable(WebElement tableElement, String tableName, String colVal1, String colVal2, String colVal3, int colIndex1, int colIndex2, int colIndex3)
    {
        int rowNum = 0;
        int totalRowCount = getTotalRowSize(tableElement);

        for (int i = 1; i < totalRowCount + 1; i++)
        {
            if (webTableText(tableName, i, colIndex1).getText().trim().equals(colVal1) &&
                    webTableText(tableName, i, colIndex2).getText().trim().equals(colVal2) &&
                    webTableText(tableName, i, colIndex3).getText().trim().equals(colVal3))
            {
                LOG.info("        Match found in " + tableName + " table at row " + i);
                rowNum = i;
                break;
            }
        }
        return rowNum;
    }

    public void deletePayorReq(String pyrAbbrev, int pyrReqFldRowCount, int pyrReqRowCount) throws Exception
    {
        if (pyrReqFldRowCount > 0)
        {
            payorDao.deleteFromPyrReqmntsByPyrAbbrev(pyrAbbrev);
            LOG.info("rows got deleted in PYR_REQMNTS_FLD.");
        } else
        {
            LOG.info("        No payor requirements found in PYR_REQMNTS_FLD.");
        }

        if (pyrReqRowCount > 0)
        {
            payorDao.deleteFromPyrReqmntsByPyrAbbrev(pyrAbbrev);
            LOG.info("rows got deleted in PYR_REQMNTS.");
        } else
        {
            LOG.info("        No payor requirements found in PYR_REQMNTS.");
        }

    }

    public boolean isColumnValueExist(WebElement element, String columnVal)
    {
        String str;
        boolean flag = true;
        boolean found = false;

        //Get row count
        List<WebElement> rows = element.findElements(By.tagName("tr"));
        LOG.info("        Row Count: " + rows.size());

        //Loop through each row
        for (WebElement row : rows)
        {
            //Get column count
            List<WebElement> cols = row.findElements(By.xpath("td"));
            LOG.info("        Col Count: " + cols.size());

            //Loop through each column
            for (WebElement col : cols)
            {
                str = col.getText().trim();
                //LOG.info(str);
                //Compare column value with the string passed
                if (str.contains(columnVal))
                {
                    flag = false;
                    found = true;
                    LOG.info("        Matching value found in webtable for: " + columnVal.replaceAll(",", ""));
                    break;
                }
            }
            if (!flag)
            {
                break;
            }
        }
        return found;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------
    //This method verifies if the Save in progress message is hidden
    //When errors returned, the Save in progress is still on the page but hidden
    public boolean isSaveInProgressHidden(int time) throws InterruptedException
    {
        boolean flag = false;
        int i = 0;
        while (i < time)
        {
            if (saveInProgressInfoText().getAttribute("style").contains("none"))
            {
                flag = true;
                LOG.info("        Save in progress is not visible.");
                break;
            }
            else
            {
                LOG.info("        Save in progress is visible.");
            }
            Thread.sleep(1000);
            i++;
        }
        return flag;
    }

    public boolean isErrorReturned()
    {
        boolean flag = false;

        if (errorReturnedText().equals("0") || errorReturnedText().equals(""))
        {
            LOG.info("        No error was returned.");
        } else
        {
            flag = true;
            LOG.info("        " + errorReturnedText() + " errors were returned.");
        }
        return flag;
    }

    public boolean isSaveInProgressNotPresent(int time) throws InterruptedException
    {
        boolean flag = false;
        int i = 0;

        while (i < time)
        {
            try
            {
                if (saveInProgressInfoText().isDisplayed() || saveInProgressInfoText().isEnabled() || !(saveInProgressInfoText().getAttribute("style").contains("none")))
                {
                    LOG.info("        Save in progress is visible.");
                }
            }
            catch (Exception e)
            {
                flag = true;
                LOG.info("        Save in progress is not visible.");
                break;
            }
            Thread.sleep(1000);
            i++;
        }
        return flag;
    }

    /**
     * Determines whether or not the accession has been fully loaded on the screen.
     *
     * @param accnId the accession Id
     * @param wait   the wait handler
     * @return true if the accession is loaded, otherwise false
     */
    public boolean isAccnLoaded(String accnId, WebDriverWait wait)
    {
        boolean isAccnLoaded = false;
        try
        {
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_MAIN_SECTIONS))));
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_PAYOR_TABS))));
            wait.until(ExpectedConditions.elementToBeClickable(saveBtn()));
            String loadedAccnId=StringUtils.trim(accnIdText().getAttribute("value"));
            LOG.info("Verifying accession ID, expectedAccnId="+accnId+", loadedAccnId="+loadedAccnId);
            isAccnLoaded = StringUtils.equalsIgnoreCase(loadedAccnId, accnId);
        }
        catch (Exception e)
        {
            LOG.warn("Exception occurred while checking if accession is loaded on Accession Detail, accnId="+accnId, e);
        }
        return isAccnLoaded;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------
    //This method checks if the accn was loaded properly within 10 seconds time of period
    public boolean isAccnLoaded(String accnId) throws Exception
    {
        boolean flag = false;
        int i = 0;
        int time = 20;
        Accn accn =accessionDao.getAccn(accnId);

        String ptFullNameInDb = accn.getPtLNm() + accn.getPtFNm().replaceAll(" ", "");
        String dos = new SimpleDateFormat("MM/dd/yyyy").format(accn.getDos());
        String ptDOB = new SimpleDateFormat("MM/dd/yyyy").format(accn.getPtDob());

        while (i < time)
        {
            if ((accnIdText().getAttribute("value").trim().equals(accnId))
                    && (dosText().getText().trim().equals(dos))
                    && (dobText().getText().trim().equals(ptDOB))
                    && (ptFullNameText().getText().replaceAll(",", "").replaceAll(" ", "").trim().equals(ptFullNameInDb)))
            {
                flag = true;
                LOG.info("        The Accession is loaded.");
                break;
            }
            else
            {
                LOG.info("        The Accession was not loaded.");
                Thread.sleep(1000);
                i++;
            }
        }

        return flag;
    }
    // ----------------------------------------------------------------------------------------------------------------------------------------
    //This method checks if the accn was done saving
    public boolean isSaveDone() throws InterruptedException
    {
        boolean flag = false;
        SeleniumBaseTest b = new SeleniumBaseTest();

        if (isSaveInProgressNotPresent(40))
        {
            //Click Save again in case of any error returns
            if (isErrorReturned())
            {
                if (b.isElementPresent(saveBtn(), 20))
                {
                    clickSave();
                    //if (!(b.isElementNotPresent(saveInProgressInfoText(), 30))){
                    if (!(isSaveInProgressNotPresent(40)))
                    {
                        flag = false;
                        LOG.info("        The Accession was not saved.");
                    } else
                    {
                        flag = true;
                        Thread.sleep(5000);
                        LOG.info("        The Accession was saved.");
                    }
                }
                else
                {
                    flag = true;
                    Thread.sleep(5000);
                    LOG.info("        The Accession was saved.");
                }
            }
            else
            {
                flag = true;
                Thread.sleep(5000);
                LOG.info("        The Accession was saved.");
            }
        }
        return flag;
    }

    /*Phlebotomist Info Section*/
    public WebElement phlebIDinput()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/div/div[3]/div[1]/div/div[2]/input"));
    }

    public WebElement phlebotomistNameText()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/div/div[3]/div[1]/div/div[3]/div[2]/span"));
    }

    public WebElement PSCLocationID()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/div/div[3]/div[2]/div[1]/div[2]/input"));
    }

    public WebElement phlebIDSearchIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/div/div[3]/div[1]/div/div[1]/div/a/span"));
    }

    public WebElement PSCLocationIDSearchIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/div/div[3]/div[2]/div[1]/div[1]/div/a/span"));
    }

    public WebElement facilityNameText()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/div/div[3]/div[2]/div[1]/div[3]/div[2]/span"));
    }

    public WebElement NPIText()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/div/div[3]/div[2]/div[1]/div[3]/div[4]/span"));
    }

    public WebElement facilityTypeText()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/div/div[3]/div[2]/div[1]/div[3]/div[6]/span"));
    }

    public WebElement phlebotomistTab()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/header/div[1]/span"));
    }

    public void inputPhlebotomistUserId(String value)
    {
        phlebIDinput().clear();
        phlebIDinput().sendKeys(value);
        phlebIDinput().sendKeys(Keys.TAB);
        LOG.info("        Input User Id for Phlebotomist Info section");
    }

    public void inputPSCLocation(String value)
    {
        PSCLocationID().clear();
        PSCLocationID().sendKeys(value);
        PSCLocationID().sendKeys(Keys.TAB);
        LOG.info("        Input PSC location for Phlebotomist Info section");
    }

    /*Phlebotomist Search*/
    public WebElement userId()
    {
        return driver.findElement(By.id("userId"));
    }

    public WebElement userSearchBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='userSearch']/div[2]/button[1]"));
    }

    public WebElement userSearchResultCellTbl(int row, int col)
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_usersearchresults']/tbody/tr[" + row + "]/td[" + col + "]/a"));
    }

    public WebElement userSearchResultTbl(int row, int col)
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_usersearchresults']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public void inputUserId(String value)
    {
        userId().clear();
        userId().sendKeys(value);
        LOG.info("        Input user Id");
    }

    public void clickUserSearch(int row, int col)
    {
        userSearchResultCellTbl(row, col).click();
        LOG.info("        Click on User Search Id");
    }

    /*Help icon on Page*/
    public WebElement helpIconOnAccnErrSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionErrors']/section/div/div[1]/a"));
    }

    public WebElement helpIconOnSectionNotesSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionNotes']/section/div/div[1]/a"));
    }

    public WebElement helpIconOnPhysicianInfoSection()
    {
        return driver.findElement(By.xpath(".//*[@id='physicianInfoSection']/div/section/div/div[1]/a"));
    }

    public WebElement helpIconOnPhlebInfoSection()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/div/div[1]/a"));
    }

    public WebElement helpIconOnPatientInfoSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionPatient']/div/div/form/section/div/div[1]/a"));
    }

    public WebElement helpIconOnDiagnosisSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionDiagnosis']/div/section/div/div[1]/a"));
    }

    public WebElement helpIconOnInsuranceInfoSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionInsurance']/div/section/div/div[1]/a"));
    }

    public WebElement helpIconOnInsuranceInfoSectionPayorInfo()
    {
        return driver.findElement(By.xpath(".//*[@id='payor_tab_A']/div/div[2]/div/form/section/div/div[1]/a"));
    }

    public WebElement helpIconOnInsuranceInfoSectionInsuredInfo()
    {
        return driver.findElement(By.xpath(".//*[@id='payor_tab_A']/div/div[2]/div/div[1]/form/section/div/div[1]/a"));
    }

    public WebElement helpIconOnInsuranceInfoSectionPayorNoteInfo()
    {
        return driver.findElement(By.xpath(".//*[@id='payor_tab_A']/div/div[2]/div/div[2]/form/section/div/div[1]/a"));
    }

    public WebElement helpIconOnInsuranceInfoSectionEmployeeInfo()
    {
        return driver.findElement(By.xpath(".//*[@id='payor_tab_A']/div/div[2]/div/div[3]/form/section/div/div[1]/a"));
    }

    public WebElement expandInsuranceInfoSectionBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionInsurance']/div/section/header/div[1]/span/a"));
    }

    public WebElement helpIconOnOccurrenceAndValuesSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionOccurrenceAndValues']/section/div/div[1]/a"));
    }

    public WebElement helpIconOnEligibilityTransactionsSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionEligibilityTransactions']/div/section/div/div[1]/a"));
    }

    public WebElement helpIconOnClaimsStatusTransactionsSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionClaimsStatusTransactions']/div/section/div/div[1]/a"));
    }

    public WebElement helpIconOnClaimsStatusHistorySection()
    {
        return driver.findElement(By.xpath("//*[@id=\"sectionClaimStatusHistory\"]/div/section/div/div[1]/a"));
    }
    public WebElement helpIconOnSubmitClaimsSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionSubmitClaims']/div/section/div/div[1]/a"));
    }

    public WebElement helpIconOnOrderedTestsSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionTransactions']/section/div/div[1]/a"));
    }

    public WebElement helpIconOnAccnLevelPricingSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionAccnLevelPricingMessages']/section/div/div[1]/a"));
    }

    public WebElement helpIconOnConsolidationSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionConsolidationDetails']/section/div/div[1]/a"));
    }

    public WebElement helpIconOnBillableProcedureCodeSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionBillableProcedureCodeAndBulkTransactions']/section/div/div[1]/a"));
    }

    public WebElement helpIconOnClnSpecQuestionSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionOtherDemo']/section[1]/div/div[1]/a"));
    }

    public WebElement helpIconOnChainOfCustodySection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionOtherDemo']/section[3]/div/div[1]/a"));
    }

    public WebElement noteSectionHeader()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionNotes']/section/header/div[1]/span"));
    }

    public WebElement addContactDetailIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='add_tbl_contactDetail']/div"));
    }

    public WebElement contactDetailGrid()
    {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_contactDetail']/div[1]"));
    }

    public WebElement sectionSearchField()
    {
        return driver.findElement(By.id("sectionSearchField"));
    }

    public WebElement helpIconOnClinicalTrialSection()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionOtherDemo']/section[2]/div/div[1]/a"));
    }

    public void clickHelpIconOnAccnErrSection()
    {
        helpIconOnAccnErrSection().click();
        LOG.info("        Click on Help Icon On Accession Error Section");
    }

    public void clickHelpIconOnSectionNotesSection()
    {
        helpIconOnSectionNotesSection().click();
        LOG.info("        Click on Help Icon On Section Notes Section");
    }

    public void clickHelpIconOnPhysicianInfoSection()
    {
        helpIconOnPhysicianInfoSection().click();
        LOG.info("        Click on Help Icon On Physician Info Section");
    }

    public void clickHelpIconOnPhlebInfoSection()
    {
        helpIconOnPhlebInfoSection().click();
        LOG.info("        Click on Help Icon On Phlebotomist Info Section");
    }

    public void clickHelpIconOnPatientInfoSection()
    {
        helpIconOnPatientInfoSection().click();
        LOG.info("        Click on Help Icon On Patient Info Section");
    }

    public void clickHelpIconOnDiagnosisSection()
    {
        helpIconOnDiagnosisSection().click();
        LOG.info("        Click on Help Icon On Diagnosis Section");
    }

    public void clickHelpIconOnInsuranceInfoSection()
    {
        helpIconOnInsuranceInfoSection().click();
        LOG.info("        Click on Help Icon On Insurance Info Section");
    }

    public void clickHelpIconOnInsuranceInfoSectionPayorInfo()
    {
        helpIconOnInsuranceInfoSectionPayorInfo().click();
        LOG.info("        Click on Help Icon On Insurance Info Section > Payor Info section");
    }

    public void clickHelpIconOnInsuranceInfoSectionInsuredInfo()
    {
        helpIconOnInsuranceInfoSectionInsuredInfo().click();
        LOG.info("        Click on Help Icon On Insurance Info Section > Insured Info section");
    }

    public void clickExpandInsuranceInfoSectionBtn()
    {
        expandInsuranceInfoSectionBtn().click();
        LOG.info("        Click on Expand Insurance Info Section button");
    }

    public void clickhelpIconOnInsuranceInfoSectionEmployeeInfo()
    {
        helpIconOnInsuranceInfoSectionEmployeeInfo().click();
        LOG.info("        Click on Help Icon On Insurance Info Section > Employee section");
    }

    public void clickhelpIconOnInsuranceInfoSectionPayorNoteInfo()
    {
        helpIconOnInsuranceInfoSectionPayorNoteInfo().click();
        LOG.info("        Click on Help Icon On Insurance Info Section > Payor Note section");
    }

    public void clickhelpIconOnOccurrenceAndValuesSection()
    {
        helpIconOnOccurrenceAndValuesSection().click();
        LOG.info("        Click on Help Icon On Occurrence And Values Section");
    }

    public void clickhelpIconOnEligibilityTransactionsSection()
    {
        helpIconOnEligibilityTransactionsSection().click();
        LOG.info("        Click on Help Icon On Eligibility Transactions Section");
    }

    public void clickhelpIconOnClaimsStatusTransactionsSection()
    {
        helpIconOnClaimsStatusTransactionsSection().click();
        LOG.info("        Click on Help Icon On Claims Status Transactions Section");
    }

    public void clickhelpIconOnSubmitClaimsSection()
    {
        helpIconOnSubmitClaimsSection().click();
        LOG.info("        Click on Help Icon On Submit Claims Section");
    }

    public void clickhelpIconOnOrderedTestsSection()
    {
        helpIconOnOrderedTestsSection().click();
        LOG.info("        Click on Help Icon On Ordered Tests Section");
    }

    public void clickhelpIconOnAccnLevelPricingSection()
    {
        helpIconOnAccnLevelPricingSection().click();
        LOG.info("        Click on Help Icon On Accession Level Pricing Section");
    }

    public void clickhelpIconOnConsolidationSection()
    {
        helpIconOnConsolidationSection().click();
        LOG.info("        Click on Help Icon On Consolidation Section");
    }

    public void clickhelpIconOnBillableProcedureCodeSection()
    {
        helpIconOnBillableProcedureCodeSection().click();
        LOG.info("        Click on Help Icon On Billable Procedure Code and Bulk Transaction Detail Section");
    }

    public void clickhelpIconOnClnSpecQuestionSection()
    {
        helpIconOnClnSpecQuestionSection().click();
        LOG.info("        Click on Help Icon On Client Specific Questions Section");
    }

    public void clickhelpIconOnChainOfCustodySection()
    {
        helpIconOnChainOfCustodySection().click();
        LOG.info("        Click on Help Icon On Chain of Custody Section");
    }

    public void clickhelpIconOnClinicalTrialSection()
    {
        helpIconOnClinicalTrialSection().click();
        LOG.info("        Click on Help Icon On Clinical Trial Section");
    }

    public void enterSectionSearchField(String val)
    {
        sectionSearchField().sendKeys(Keys.CONTROL + "a");
        sectionSearchField().sendKeys(val + Keys.ENTER);
        LOG.info("        Enter " + val + " to Section Search Field");
    }

    //Client Text
    public WebElement clientText()
    {
        return driver.findElement(By.xpath(".//span[@class='dataDisplay clientName truncate']"));
    }

    //Pull Reg
    public WebElement pullRegCheckBox()
    {
        return driver.findElement(By.id("pullReq"));
    }

    public void clickPullRegCheckBox()
    {
        pullRegCheckBox().click();
        LOG.info("        Click Pull Reg Check box");
    }

    //patient name
    public WebElement patientNameText()
    {
        return driver.findElement(By.xpath(".//*[@class='keyField dataDisplay patientFullName data-mirror truncate']"));
    }

    public WebElement patientNameLink()
    {
        return driver.findElement(By.xpath("html/body/section/div/div/div[2]/div[1]/div/div[2]/div[2]/div[1]/div[1]/label"));
    }

    public WebElement epiSearchIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='PatientSearch']/a/span"));
    }

    public WebElement postalSearchIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionPatient']/div/div/form/section/div/div[3]/div[1]/div[2]/div[2]/div/div[1]/div[3]/div[1]/div/a/span"));
    }

    public void clickPatientNameLink()
    {
        patientNameLink().click();
        LOG.info("        Click Patient Name Link");
    }

    //Epi
    public WebElement epiText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay patientId data-mirror']"));
    }

    public WebElement epiLink()
    {
        return driver.findElement(By.xpath("html/body/section/div/div/div[2]/div[1]/div/div[2]/div[2]/div[2]/div[1]/label"));
    }

    public void clickEpiLink()
    {
        epiLink().click();
        LOG.info("        Click Epi Link");
    }

    //Date of birth
    public WebElement dateOfBirthText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay dob patientDateOfBirth data-mirror']"));
    }

    public WebElement dateOfBirthLink()
    {
        return driver.findElement(By.xpath("html/body/section/div/div/div[2]/div[1]/div/div[2]/div[2]/div[3]/div[1]/label"));
    }

    public void clickDateOfBirthLink()
    {
        dateOfBirthLink().click();
        LOG.info("        Click Date of birth Link");
    }

    //Age and Gender
    public WebElement ageOnDOSText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay age']"));
    }

    public WebElement genderText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay patientGender data-mirror']"));
    }

    public WebElement genderLink()
    {
        return driver.findElement(By.xpath("html/body/section/div/div/div[2]/div[1]/div/div[2]/div[2]/div[4]/div[1]/label"));
    }

    public void clickGenderLink()
    {
        genderLink().click();
        LOG.info("        Click Age and Gender Link");
    }

    //View Document link - View Client Portal
    public WebElement viewDocumentLink()
    {
        return driver.findElement(By.xpath(".//*[@class='viewDocumentLabel']"));
    }

    public WebElement viewClientPortalDocumentLink()
    {
        return driver.findElement(By.xpath(".//*[@class='viewOrganizationDocumentLabel']"));
    }

    public void clickViewDocumentLink()
    {
        viewDocumentLink().click();
        LOG.info("        Click View Document Link");
    }

    public void clickViewClientPortalDocumentLink()
    {
        viewClientPortalDocumentLink().click();
        LOG.info("        Click View Client Portal Link");
    }

    //Date of serv and  final report Dt
    public WebElement dateOfServiceText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay dateOfService']"));
    }

    public WebElement finalReportDateText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay finalReportDate']"));
    }

    public WebElement orderingPhysicianNameText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay orderingPhysicianName data-mirror']"));
    }

    public WebElement orderingPhysicianLink()
    {
        return driver.findElement(By.xpath(".//*[@data-click-to-edit='physicianBlockTag .orderingPhysicianNpi']"));
    }

    public WebElement repriceCheckbox()
    {
        return driver.findElement(By.id("reprice"));
    }

    public void clickOrderingPhysicianLink()
    {
        orderingPhysicianLink().click();
        LOG.info("        Click Ordering Physician Link");
    }

    public void clickRepriceCheckbox(WebDriverWait wait)
    {
        wait.until(ExpectedConditions.elementToBeClickable(repriceCheckbox()));
        repriceCheckbox().click();
        LOG.info("        Click Reprice Checkbox");
    }

    //Date of serv and  final report Dt
    public WebElement balanceDueText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay displayMoney balanceDue']"));
    }

    //Accession Err
    public WebElement forceToCorrespondenceCheckbox()
    {
        return driver.findElement(By.id("forceToCorrespondence"));
    }

    public WebElement epHoldQueueCheckbox()
    {
        return driver.findElement(By.id("epHoldQueue"));
    }

    public WebElement relevantErrorRadio()
    {
        return driver.findElement(By.id("radioRelevantError"));
    }

    public WebElement unFixedErrorRadio()
    {
        return driver.findElement(By.id("radioUnFixedError"));
    }

    public WebElement accnCurErrorTbl()
    {
        return driver.findElement(By.id("tbl_accnCurrentErrors"));
    }

    public WebElement fixedAccnErrorTbl()
    {
        return driver.findElement(By.id("tbl_accnFixedErrors"));
    }

    public WebElement accnCurErrorCell(int row, int col)
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_accnCurrentErrors']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    //Contact notes
    public WebElement contactDetailTbl()
    {
        return driver.findElement(By.id("tbl_contactDetail"));
    }

    public WebElement addContactDetailBtn()
    {
        return driver.findElement(By.id("add_tbl_contactDetail"));
    }

    public WebElement editContactDetailBtn()
    {
        return driver.findElement(By.id("edit_tbl_contactDetail"));
    }

    //Physician Info
    public WebElement physicianSofCheckbox()
    {
        return driver.findElement(By.id("physicianSof"));
    }

    public WebElement orderingPhysicianFullNameText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay displayFullName orderingPhysicianFullName clearTextOnReset data-mirror']"));
    }

    public WebElement npiText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay npi orderingPhysicianNpi clearTextOnReset']"));
    }

    public WebElement upinText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay upin orderingPhysicianUpin clearTextOnReset']"));
    }

    public WebElement taxonomiCdText()
    {
        return driver.findElement(By.xpath(".//*[@class='dataDisplay taxonomyCode orderingPhysicianTaxonomyCode clearTextOnReset']"));
    }

    public WebElement orderingSearchIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='physicianInfoSection']/div/section/div/div[3]/div[2]/div[1]/div/a/span"));
    }

    public WebElement refferingSearchIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='physicianInfoSection']/div/section/div/div[3]/div[3]/div[1]/div/a/span"));
    }

    public WebElement primarySearchIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='physicianInfoSection']/div/section/div/div[3]/div[4]/div[1]/div/a/span"));
    }

    //General Btn
    public WebElement saveAndClearBtn()
    {
        return driver.findElement(By.id("btnSaveAndClear"));
    }

    public WebElement sectionServerMessages()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionServerMessages']/div/div[1]/ul"));
    }

    public WebElement collapseViewBtn()
    {
        //return driver.findElement(By.xpath(".//*[@class='toolbar-button btnCollapseView']"));
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"toolbar-button-text\"]')[7]");
    }

    public WebElement detailViewBtn()
    {
        return driver.findElement(By.xpath(".//*[@class='toolbar-button btnDetailView']"));
    }

    public WebElement sumaryViewBtn()
    {
        return driver.findElement(By.xpath(".//*[@class='toolbar-button btnSummaryView']"));
    }

    public WebElement sectionSearchInput()
    {
        return driver.findElement(By.id("sectionSearchField"));
    }

    public WebElement pageHelpLinkBtn()
    {
        return driver.findElement(By.id("pageHelpLink"));
    }

    //Eligibility Transactions

    public WebElement eligibilityServiceEnableIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionEligibilityTransactions']/div/section/header/div[1]/span/a"));
    }

    public WebElement eligibilityServiceText()
    {
        return driver.findElement(By.id("gs_eligibilityService"));
    }

    public WebElement eligibilityCheckDateText()
    {
        return driver.findElement(By.id("gs_eligibilityCheckDate"));
    }

    public WebElement eligibilityTransIDText()
    {
        return driver.findElement(By.id("gs_eligibilityTransId"));
    }

    public WebElement eligibilityViewResponseText()
    {
        return driver.findElement(By.id("gs_eligibilityViewResponse"));
    }

    //Claim Status Transactions
    public WebElement claimStatusTransEnableIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionClaimsStatusTransactions']/div/section/header/div[1]/span/a"));
    }

    public WebElement payorIDText()
    {
        return driver.findElement(By.id("gs_payorAbbrev"));
    }

    public WebElement text276Requester()
    {
        return driver.findElement(By.id("gs_requester"));
    }

    public WebElement text277TransDate()
    {
        return driver.findElement(By.id("gs_transactionDate"));
    }

    public WebElement claimStatusTransIDText()
    {
        return driver.findElement(By.id("gs_claimStatusTransId"));
    }

    public WebElement clearingHouseICNText()
    {
        return driver.findElement(By.id("gs_clearinghouseICN"));
    }

    public WebElement payorICNText()
    {
        return driver.findElement(By.id("gs_payorICN"));
    }

    public WebElement claimStatusTransViewResponseText()
    {
        return driver.findElement(By.id("gs_payorICN"));
    }

    //Submit Claim
    public WebElement submitClaimEnableIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionSubmitClaims']/div/section/header/div[1]/span/a"));
    }

    public WebElement depositDateText()
    {
        return driver.findElement(By.id("gs_depositDate"));
    }

    public WebElement payorAndSubscriberIDText()
    {
        return driver.findElement(By.name("payorDroplist"));
        //payorAbbrevAndSubscriberId
    }

    public WebElement payorAndSubscriberIDText2()
    {
        return driver.findElement(By.id("payorAbbrevAndSubscriberId"));
    }

    public WebElement icnText()
    {
        return driver.findElement(By.id("icn"));
    }

    public WebElement selectAttachmentType()
    {
        return wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.className("selectAttachmentType"))));
    }

    public WebElement paymentAmountText()
    {
        return driver.findElement(By.id("gs_paidAmount"));
    }

    public WebElement actionText()
    {
        return driver.findElement(By.className("actionDroplist"));
    }

    public WebElement formatText()
    {
        return driver.findElement(By.cssSelector(".formatDroplist.select2-offscreen"));
    }

    public WebElement selectMatchText()
    {
        return driver.findElement(By.className("select2-match"));
    }
    public WebElement conditionCodeText()
    {
        return driver.findElement(By.id("gs_conditionCode"));
    }

    public WebElement submitClaimDeleteCheckbox()
    {
        return driver.findElement(By.id("deleteRow"));
    }

    public WebElement submitClaimAddBtn()
    {
        return driver.findElement(By.id("add_tbl_submitClaims"));
    }

    public WebElement submitClaimEditBtn()
    {
        return driver.findElement(By.id("edit_tbl_submitClaims"));
    }

    //Ordered Tests
    public WebElement viewDropDown()
    {
        return driver.findElement(By.xpath(".//*[@id='s2id_autogen9']/a"));
    }

    public WebElement orderedTestEnableIcon()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionTransactions']/section/header/div[1]/span/a"));
    }

    public WebElement showAllCheckbox()
    {
        return driver.findElement(By.id("checkShowAllOrderTests"));
    }

    public WebElement testCodeIDText()
    {
        return driver.findElement(By.id("gs_testAbbrev"));
    }

    public WebElement testNameText()
    {
        return driver.findElement(By.id("gs_testName"));
    }

    public WebElement mod1Text()
    {
        return driver.findElement(By.id("gs_mod1"));
    }

    public WebElement mod2Text()
    {
        return driver.findElement(By.id("gs_mod2"));
    }

    public WebElement mod3Text()
    {
        return driver.findElement(By.id("gs_mod3"));
    }

    public WebElement mod4Text()
    {
        return driver.findElement(By.id("gs_mod4"));
    }

    public WebElement proCodeText()
    {
        return driver.findElement(By.id("gs_procId"));
    }

    public WebElement unitsBilledText()
    {
        return driver.findElement(By.id("gs_unitsBilled"));
    }

    public WebElement billedAmountText()
    {
        return driver.findElement(By.id("gs_billedAmount"));
    }

    public WebElement expectAmountText()
    {
        return driver.findElement(By.id("gs_expectAmount"));
    }

    public WebElement labMessageText()
    {
        return driver.findElement(By.id("gs_labMessage"));
    }

    public WebElement POSText()
    {
        return driver.findElement(By.id("gs_pos"));
    }

    public WebElement renderingPhysicianText()
    {
        return driver.findElement(By.id("gs_renderingPhysician"));
    }

    public WebElement priorAuthText()
    {
        return driver.findElement(By.id("gs_priorAuthNum"));
    }

    public WebElement noteText()
    {
        return driver.findElement(By.id("gs_note"));
    }

    public WebElement ABNReqText()
    {
        return driver.findElement(By.id("gs_abnReq"));
    }

    public WebElement ABNRecdText()
    {
        return driver.findElement(By.id("gs_abnReceived"));
    }

    public WebElement ordersTestEditBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='edit_tbl_orderedTestDetail']/div"));
    }

    public WebElement ordersTestColumnBtn()
    {
        return driver.findElement(By.id("columnChooser_tbl_orderedTestDetail"));
    }

    public WebElement roundTripCheckbox()
    {
        return driver.findElement(By.id("roundTrip"));
    }

    public WebElement mileageText()
    {
        return driver.findElement(By.id("tripMiles"));
    }

    public WebElement totalPatientText()
    {
        return driver.findElement(By.id("totalPatients"));
    }

    public WebElement phlebotomyStopsText()
    {
        return driver.findElement(By.id("phlebotomyStops"));
    }

    //User Search Page
    public WebElement userIdInput()
    {
        return driver.findElement(By.id("userId"));
    }

    public WebElement okBtnInPtDemoUpdate()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"btn lockDownOnSubmit btn_submit\"]')[0]");
    }

    //-------------------------------------------------------------------------------------------------------------------------
    public void clickOKBtnInPtDemoUpdate() throws InterruptedException
    {
        if(okBtnInPtDemoUpdate().isDisplayed())
        {
            okBtnInPtDemoUpdate().click();
        }
        Thread.sleep(5000);
        LOG.info("        Clicked OK Btn.");
    }


    public void clickDetailBtn()
    {
        detailViewBtn().click();
        LOG.info("        Clicked the detail View Btn.");
    }

    public void clickCollapseBtn()
    {
        collapseViewBtn().click();
        LOG.info("        Clicked the detail View Btn.");
    }

    public void clickPageHelpLinkBtn()
    {
        pageHelpLinkBtn().click();
        LOG.info("        Clicked the detail View Btn.");
    }

    public void scrollToElement(WebElement el) throws InterruptedException
    {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
        Thread.sleep(2000);
    }

    public void clickPhelebIDSearchIcon()
    {
        phlebIDSearchIcon().click();
        LOG.info("        Clicked the Phele Id Search icon.");
    }

    public void clickSumaryViewBtn()
    {
        sumaryViewBtn().click();
        LOG.info("        Clicked the Summary view button.");
    }

    public int getLeftSide(WebElement element)
    {
        //Used points class to get x coordinates of element.
        Point classname = element.getLocation();
        //Debug //System.out.println("Element's Position from left side"+xcordi +" pixels.");

        return classname.getX();
    }

    public int getTopSide(WebElement element)
    {
        //Used points class to get y coordinates of element.
        Point classname = element.getLocation();
        //Debug //System.out.println("Element's Position from top"+ycordi +" pixels.");

        return classname.getY();
    }

    //Facility Search pop-up
    public WebElement facilitySearchHeaderText()
    {
        return driver.findElement(By.xpath(".//*[@id='facilitySearch']/header/span"));
    }

    public WebElement facilityIDInput()
    {
        return driver.findElement(By.id("facilityAbbrev"));
    }

    public WebElement facilityNameInput()
    {
        return driver.findElement(By.id("facilityName"));
    }

    public WebElement contactNameInput()
    {
        return driver.findElement(By.id("contactName"));
    }

    public WebElement NPIInput()
    {
        return driver.findElement(By.id("npi"));
    }

    public WebElement licenseTypeDropdown()
    {
        return driver.findElement(By.id("licenseType"));
    }

    public WebElement licenseIDInput()
    {
        return driver.findElement(By.id("licenseId"));
    }

    public WebElement whollyOwnedSubsidiaryCheckbox()
    {
        return driver.findElement(By.xpath(".//*[@id='facilitySearch']/div/div/div[1]/div[2]/fieldset/div/div[4]/div/input[1]"));
    }

    public WebElement deleteBtn()
    {
        return driver.findElement(By.id("delete"));
    }

    public WebElement addressInput()
    {
        return driver.findElement(By.id("address"));
    }

    public WebElement cityInput()
    {
        return driver.findElement(By.id("city"));
    }

    public WebElement stateDroplist()
    {
        return driver.findElement(By.id("stateDroplist"));
    }

    public WebElement postalCodeInput()
    {
        return driver.findElement(By.id("zipCode"));
    }

    public WebElement phoneInput()
    {
        return driver.findElement(By.id("phone"));
    }

    public WebElement closeFacBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='facilityCodeSearch']/div[2]/button[3]"));
    }

    public WebElement resetFacBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='facilityCodeSearch']/div[2]/button[2]"));
    }

    public WebElement searchFacBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='facilityCodeSearch']/div[2]/button[1]"));
    }

    public WebElement facilityTableCell(int col, int row)
    {
        return driver.findElement(By.xpath(".//*[@id='facilitysearchTable']/tbody/tr[" + col + "]/td[" + row + "]"));
    }

    public WebElement facilityTableNameCell(int col)
    {
        return driver.findElement(By.xpath(".//*[@id='facilitysearchTable']/tbody/tr[" + col + "]/td[3]/a"));
    }

    public void setFacilityId(String facID) throws InterruptedException
    {
        facilityIDInput().sendKeys(facID);
        facilityIDInput().sendKeys(Keys.TAB);
        Thread.sleep(5000);
        LOG.info("        Entered facID: " + facID);
    }

    public void clickSearchFacBtn()
    {
        searchFacBtn().click();
        LOG.info("        Clicked Fac Search icon.");
    }

    public void clickFacilityTableNameCell(int col)
    {
        facilityTableNameCell(col).click();
        LOG.info("        Clicked Fac Table Name cell.");
    }

    //Diagnosis
    public WebElement addDiagnosisBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='add_tbl_accnDiagnosisDetail']/div/span"));
    }

    public WebElement cancelPopupBtn()
    {
        return driver.findElement(By.xpath("//*[@id='cData']"));
    }

    public WebElement okPopupBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sData']"));
    }

    public WebElement dxCodeInput()
    {
        return driver.findElement(By.id("s2id_autogen17"));
    }

    public WebElement dxNarrativeInput()
    {
        return driver.findElement(By.id("dxNarrative"));
    }

    public WebElement clientContactInput()
    {
        return driver.findElement(By.id("clientContact"));
    }

    public void setDxCode(String value)
    {
        dxCodeInput().sendKeys(value + Keys.TAB);
        LOG.info("       Entered Dx Code input");
    }

    public void setDxNarrative(String value)
    {
        dxNarrativeInput().sendKeys(value);
        dxNarrativeInput().sendKeys(Keys.TAB);
        LOG.info("       Entered Dx Narrative input");
    }

    public void setClientContact(String value)
    {
        clientContactInput().clear();
        clientContactInput().sendKeys(value + Keys.TAB);
        LOG.info("       Entered Dx Narrative input");
    }

    public void clickOkPopupBtn()
    {
        okPopupBtn().click();
        LOG.info("       Click Ok button");
    }

    public WebElement accnDiagnosisDetailTableCell(int row, int col)
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_accnDiagnosisDetail']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement consolidationDetailTable()
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_consolidationDetails']/tbody"));
    }

    public WebElement dxCodeSearchBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='tr_dxCode']/td[1]/div/a/span"));
    }

    public WebElement dxCodeText()
    {
        return driver.findElement(By.xpath("//*[@id='s2id_dxCode']/ul/li[1]/div"));
    }

    public void clickDxCodeSearchBtn()
    {
        dxCodeSearchBtn().click();
        LOG.info("       Click DX Code Search button");
    }

    //Insurance
    public WebElement payorIdSearchBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='payor_tab_A']/div/div[2]/div/form/section/div/div[2]/div[1]/div[2]/div[1]/div/a/span"));
    }

    public void waitUntilElementPresent(WebElement element, int timeToWait) throws InterruptedException
    {
        // NOTE : This function only use for element available before and element was hidden
        int i = 0;
        while (true)
        {
            if (element != null && element.isDisplayed()) return;
            else if (element == null) throw new NoSuchElementException("Element can't null");
            Thread.sleep(1000);
            i++;
            if (i >= timeToWait) throw new NoSuchElementException("Time out for waiting element visible");
        }
    }

    //Bulk Transaction
    public WebElement addAdjustmentLink()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionBillableProcedureCodeAndBulkTransactions']/section/div/div[4]/div[2]/div[2]/div[2]/div[2]/div[1]/div[1]/a"));
    }

    public WebElement adjCodeDrop()
    {
        return driver.findElement(By.xpath(".//*[@id='s2id_adjCode']/a"));
    }

    public WebElement adjAmount()
    {
        return driver.findElement(By.id("adjAmount"));
    }

    public WebElement adjComment()
    {
        return driver.findElement(By.id("adjComment"));
    }

    public WebElement payorIdDrop()
    {
        return driver.findElement(By.xpath(".//*[@id='s2id_payorAbbrev']/a"));
    }

    public void selectAdjCode(String value)
    {
        adjCodeDrop().click();
        adjCodeDrop().sendKeys(value);
        adjCodeDrop().sendKeys(Keys.TAB);
        LOG.info("        Select any Adj Code from drop down list");
    }

    public void inputAdjAmt(String value)
    {
        adjAmount().sendKeys(value);
        LOG.info("        Input adj amount");
    }

    public void inputAdjCmt(String value)
    {
        adjComment().sendKeys(value);
        LOG.info("        Input adj comment");
    }

    public void selectPayorId(String value)
    {
        payorIdDrop().click();
        payorIdDrop().sendKeys(value);
        payorIdDrop().sendKeys(Keys.TAB);
        LOG.info("        Select any PayorId from dropdown list");
    }

    public void enterEpiInput(String val)
    {
        epiInput().sendKeys(Keys.CONTROL + "a");
        epiInput().sendKeys(Keys.BACK_SPACE + val + Keys.ENTER);
        LOG.info("        Enter " + val + " to EPI input field.");
    }

    // Patient Info
    public WebElement postalZipInput()
    {
        return driver.findElement(By.id("patientZip"));
    }

    public WebElement postalCityInput()
    {
        return driver.findElement(By.id("patientCity"));
    }

    public WebElement postalStateDropDown()
    {
        return driver.findElement(By.id("patientState"));
    }

    public String getTotalResultSearch(WebElement pager)
    {
        String total = pager.getText();
        StringTokenizer strToken = new StringTokenizer(total, " ");
        String numberOfResult = "";
        while (strToken.hasMoreTokens())
        {
            numberOfResult = strToken.nextToken();
            if (numberOfResult.equalsIgnoreCase("records") || numberOfResult.isEmpty())
            {
                return "0";
            }
        }
        return numberOfResult;
    }

    public WebElement CoverageLimitationConfigOnMars()
    {
        return driver.findElement(By.xpath(".//*[@id='vMenu']/tbody/tr[1]/td/ul[1]/li[3]/a"));
    }

    public void clickCoverageLimitationConfigOnMar()
    {
        CoverageLimitationConfigOnMars().click();
        LOG.info("        Clicked on Coverage Limitation Config On Mars");
    }

    //Submit  Claims
    public WebElement payorAndSubIdDrop()
    {
        return driver.findElement(By.xpath(".//*[@id='tr_payorAbbrevAndSubscriberId']//div[starts-with(@id, 's2id_')]/a"));
    }

    public WebElement icn()
    {
        return driver.findElement(By.id("icn"));
    }

    public WebElement formatDrop()
    {
        return driver.findElement(By.xpath(".//*[@id='tr_format']//div[starts-with(@id, 's2id_')]/a"));
    }

    public WebElement submitClaimsTbl()
    {
        return driver.findElement(By.id("tbl_submitClaims"));
    }

    public void selectPayorAndSubId(String value)
    {
        payorAndSubIdDrop().click();
        payorAndSubIdDrop().sendKeys(value);
        payorAndSubIdDrop().sendKeys(Keys.TAB);
        LOG.info("        Select PayorID and Sub Id");
    }

    public void inputICN(String icn)
    {
        icn().sendKeys(icn);
        LOG.info("        Input ICN");
    }

    public void selectFomatDrop(String value)
    {
        formatDrop().click();
        formatDrop().sendKeys(value);
        formatDrop().sendKeys(Keys.TAB);
        LOG.info("        Select format");
    }


    public WebElement checkEligibilityCheckbox()
    {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("checkEligibility")));
    }

    public WebElement checkClaimStatusCheckbox()
    {
        return driver.findElement(By.id("checkClaimStatus"));
    }

    public void clickEligibilityCheckbox()
    {
        Actions actions = new Actions(driver);
        actions.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(By.className("insuredInfoBlockGroup"))));
        actions.perform();
        checkEligibilityCheckbox().click();
        LOG.info("Click on Eligibility checkbox.");
    }

    public void clickCheckClaimStatus()
    {
        checkClaimStatusCheckbox().click();
        LOG.info(("Clicked on Check Claim Status checkbox"));
    }

    public WebElement showEligibilityHistoryLink()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionEligibilityTransactions']/div/section/div/div[2]/div/a"));
    }

    public void clickshowEligibilityHistoryLink()
    {
        showEligibilityHistoryLink().click();
        LOG.info("        Click on Show Eligibility History Link on Section Eligibility Transactions.");
    }

    public WebElement employerAddress1OnEmployerInfoSection()
    {
        return driver.findElement(By.id("employerAddressLine1"));
    }

    public WebElement employerAddress2OnEmployerInfoSection()
    {
        return driver.findElement(By.id("employerAddressLine2"));
    }

    public void enteremployerAddress1OnEmployerInfoSection(String val)
    {
        employerAddress1OnEmployerInfoSection().sendKeys(val);
        LOG.info("        Enter " + val + " to Employer Address 1 On Employer Info Section.");
    }

    public void enteremployerAddress2OnEmployerInfoSection(String val)
    {
        employerAddress2OnEmployerInfoSection().sendKeys(val);
        LOG.info("        Enter " + val + " to Employer Address 2 On Employer Info Section.");
    }

    /*Element and function on Mars*/
    public WebElement pyrIDinputOnCoverageLimitationConfigOnPyrTabOnMars()
    {
        return driver.findElement(By.id("PyrAbbrv"));
    }

    public WebElement rowOnMultDiagReqTblOnPyrTab()
    {
        return driver.findElement(By.id("multDxRng"));
    }

    public WebElement MultDiagReqTblOnPyrTab()
    {
        return driver.findElement(By.id("multDxTable"));
    }

    public WebElement submitBtn()
    {
        return driver.findElement(By.id("btn_submit"));
    }

    public WebElement pyrIDinputAddNewPyrInsuranceSection()
    {
        return driver.findElement(By.id("userAddedPayor"));
    }

    public WebElement submitBtnAddNewPyrInsuranceSection()
    {
        return driver.findElement(By.xpath("html/body/div[26]/div[3]/div/button[1]"));
    }

    public WebElement pyrIDinputOnPyrInfoSection()
    {
        return driver.findElement(By.id("payorAbbrev"));
    }

    public void enterpyrIDinputOnCoverageLimitationConfigOnPyrTabOnMars(String val)
    {
        pyrIDinputOnCoverageLimitationConfigOnPyrTabOnMars().sendKeys(val + Keys.TAB);
        LOG.info("        Enter " + val + " to Payor ID input On Coverage Limitation Config On Payor Tab On Mars.");
    }

    public WebElement addDxBtnOnCoverageLimitationConfigOnPyrTabOnMars()
    {
        return driver.findElement(By.xpath(".//*[@id='pyrCvrgLmtConf']/table[8]/tbody/tr/td/input"));
    }

    public WebElement payorTabsInsuranceSection(String val)
    {
        return driver.findElement(By.xpath(".//*[@id='payorTabs']/ul//li//span[4][contains(text(), '" + val + "')]"));
    }

    public WebElement errMesHeader()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionServerMessages']/div/div[1]/ul/li/span"));
    }

    public WebElement accnErrorTbl()
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_accnCurrentErrors']/tbody"));
    }

    public WebElement payorTabPrio(String val)
    {
        return driver.findElement(By.xpath(".//*[@id='payorTabs']/ul//li[" + val + "]//span[2]"));
    }

    public WebElement payorTabName(String val)
    {
        return driver.findElement(By.xpath(".//*[@id='payorTabs']/ul//li[" + val + "]//span[4]"));
    }

    public void clickaddDxBtnOnCoverageLimitationConfigOnPyrTabOnMars()
    {
        addDxBtnOnCoverageLimitationConfigOnPyrTabOnMars().click();
        LOG.info("        Click on add Dx Button On Coverage Limitation Config On Payor Tab On Mars.");
    }

    public WebElement pyrNameOnCoverageLimitationConfigOnPyrTabOnMars()
    {
        return driver.findElement(By.id("PyrNm"));
    }

    public void enterrowOnMultDiagReqTblOnPyrTab(String val)
    {
        rowOnMultDiagReqTblOnPyrTab().sendKeys(val + Keys.ENTER);
        LOG.info("        Enter " + val + " to first row On Multiple Diagnoses Required Table On Payor Tab On Mars.");
    }

    public void clicksubmitBtn()
    {
        submitBtn().click();
        LOG.info("        Click submit button");
    }

    public void clickaddPayorLink()
    {
        addPayorLink().click();
        LOG.info("        Click add Payor Link");
    }

    public void enterpyrIDinputAddNewPyrInsuranceSection(String val)
    {
        pyrIDinputAddNewPyrInsuranceSection().sendKeys(val + Keys.ENTER);
        LOG.info("        Enter " + val + " to payor ID input Add New Payor Insurance Section.");
    }

    public void clicksubmitBtnAddNewPyrInsuranceSection()
    {
        submitBtnAddNewPyrInsuranceSection().click();
        LOG.info("        Click submit Btn on Add New Pyr Insurance Section");
    }

    public void clickpayorTabsInsuranceSection(String val)
    {
        payorTabsInsuranceSection(val).click();
        LOG.info("        Click " + val + " on payor Tabs on Insurance Section");
    }

    //pop up Billable procode
    public WebElement addOverrideButton()
    {
        return driver.findElement(By.xpath(".//*[@id='messagefor_1']/div[2]/div/div/div[3]/a"));
    }

    public WebElement pyrIdDropdown()
    {
        return driver.findElement(By.id("s2id_payorAbbrev"));
    }

    public WebElement overRideDropdown()
    {
        return driver.findElement(By.xpath(".//*[@id='s2id_override']"));
    }

    public WebElement postNowBtn() throws InterruptedException
    {
        Thread.sleep(2000);
        return driver.findElement(By.xpath(".//*[@id='pdata'][@class = 'fm-button ui-state-default ui-corner-all fm-button-icon-left ui-state-post']"));
    }

    public void selectDropDownJQGird(WebElement dropDown, String textSelect)
    {
        dropDown.findElement(By.tagName("a")).click();
        WebElement list = driver.findElement(By.xpath(".//*[@id='select2-drop']/ul"));

        List<WebElement> allRows = list.findElements(By.tagName("li"));
        // And iterate over them, getting the cells
        for (WebElement row : allRows)
        {
            if (row.getText().equalsIgnoreCase(textSelect))
            {
                row.click();
                LOG.info("        Clicked on dropdown table : " + textSelect);
                break;
            }
        }
    }

    public void clickPostNowBtn() throws InterruptedException
    {
        postNowBtn().click();
    }

    public void setSectionSearch(String text) throws InterruptedException
    {
        sectionSearchInput().sendKeys(text);
        Thread.sleep(5000);
        LOG.info("        Entered section search: " + text);
    }

    public WebElement orderedTestsTitle()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionTransactions']/section/header/div[1]/span/span"));
    }

    public WebElement showClipBoardBtn()
    {
        return driver.findElement(By.xpath("html/body/section/div/div/div[2]/div[4]/div/div[1]/div[5]/div"));
    }

    public void clickShowClipBoardBtn()
    {
        showClipBoardBtn().click();
        LOG.info("        Clicked Show Clipboard button.");
    }

    //show clipboard btn
    public WebElement clipBoardInput()
    {
        return driver.findElement(By.xpath("html/body/div[3]/div[2]/div/div/div"));
    }

    public WebElement addClipBoardBtn()
    {
        return driver.findElement(By.xpath("html/body/div[3]/div[2]/div/div/a"));
    }

    public WebElement removeClipBoardBtn()
    {
        return driver.findElement(By.xpath("html/body/div[3]/div[2]/div/ol/li/a[2]"));
    }

    public WebElement clipBoardText()
    {
        return driver.findElement(By.xpath("html/body/div[3]/div[2]/div/ol/li/div"));
    }

    public WebElement okBtnonConfirmPopup()
    {
        return driver.findElement(By.xpath("html/body/div[21]/div[3]/div/button[1]"));
    }

    public WebElement accnPrcRmkTbl()
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_accnCurrentErrors']/tbody"));
    }

    public WebElement cellOnbillableProcedureCodeDetailsTbl(int col, int row)
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_billableProcedureCodeDetails']/tbody/tr[" + col + "]/td[" + row + "]"));
    }

    public WebElement billableProcedureCodeDetailsTbl()
    {
        return driver.findElement(By.id("tbl_billableProcedureCodeDetails"));
    }

    public WebElement editPopupOnbillableProcedureCodeDetailsTbl()
    {
        return driver.findElement(By.id("editmodtbl_billableProcedureCodeDetails"));
    }

    public WebElement modDropdownOnEditPopupOnbillableProcedureCodeDetailsTbl(int val)
    {
        return driver.findElement(By.id("mod" + val + ""));
    }

    public WebElement allowSystemReorderBtn()
    {
        return driver.findElement(By.xpath("html/body/div[24]/div[3]/div/button[3]"));
    }

    public void clickAddClipBoardBtn()
    {
        addClipBoardBtn().click();
        LOG.info("        Clicked Add Clipboard button.");
    }

    public void removeAddClipBoardBtn()
    {
        removeClipBoardBtn().click();
        LOG.info("        Clicked Remove Clipboard button.");
    }

    public void setClipBoardInput(String text) throws InterruptedException
    {
        clipBoardInput().sendKeys(text);
        Thread.sleep(5000);
        LOG.info("        Entered clipboard: " + text);
    }

    public WebElement viewDocumentHeaderLink()
    {
        return driver.findElement(By.xpath(".//*[@id='viewDocumentHeaderInfo']/div[1]/a"));
    }

    public void clickViewDocumentHyperlink()
    {
        viewDocumentHeaderLink().click();
        LOG.info("        Clicked View Document hyperlink.");
    }

    public void clickokBtnonConfirmPopup()
    {
        okBtnonConfirmPopup().click();
        LOG.info("        Clicked Ok Btn On Confirm Popup.");
    }

    public void clickallowSystemReorderBtn() throws InterruptedException
    {
        allowSystemReorderBtn().click();
        Thread.sleep(10000);
        LOG.info("        Clicked allow System Reorder Btn.");
    }

    // Claim Status transaction
    public WebElement claimTransactionTbl()
    {
        return driver.findElement(By.id("tbl_claimsStatusTransactions"));
    }

    public int getRowNumInClaimTransactionTbl()
    {
        return rowNumInTable = driver.findElements(By.xpath("//table[@id='tbl_claimsStatusTransactions']/tbody/tr")).size();
    }

    public WebElement statusInClaimTransactionTblCelData(int row, int col)
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_claimsStatusTransactions']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    /*Ordered Test - edit popup*/
    public WebElement modDropdown(int number)
    {
        return driver.findElement(By.id("mod" + number + ""));
    }

    //insurance
    public WebElement addACannedNoteLink()
    {
        return driver.findElement(By.xpath(".//*[@id='payor_tab_A']/div/div[2]/div/div[2]/form/section/div/div[2]/div[1]/a"));
    }

    public WebElement cannedNoteCellLink(int row, int col)
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_cannedNotes']/tbody/tr[" + row + "]/td[" + col + "]/a"));
    }

    public WebElement cannedNoteCell(int row, int col)
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_cannedNotes']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement claimNotesInput()
    {
        return driver.findElement(By.id("claimNotes"));
    }

    public WebElement internalNoteInput()
    {
        return driver.findElement(By.id("internalNotes"));
    }

    public WebElement otherInfo1Input()
    {
        return driver.findElement(By.id("otherInfo1"));
    }

    public WebElement otherInfo2Input()
    {
        return driver.findElement(By.id("otherInfo2"));
    }

    public WebElement otherInfo3Input()
    {
        return driver.findElement(By.id("otherInfo3"));
    }

    public void clickAddACannedNoteLink()
    {
        addACannedNoteLink().click();
        LOG.info("        Clicked Add A Canned note hyperlink.");
    }

    public void clickCannedNoteCellLink(int row, int col)
    {
        cannedNoteCellLink(row, col).click();
        LOG.info("        Clicked Canned Note Cell link.");
    }

    public void setInternalNoteInput(String text) throws InterruptedException
    {
        internalNoteInput().sendKeys(text);
        Thread.sleep(5000);
        LOG.info("        Entered Internal Note: " + text);
    }

    public void setOtherInfo1Input(String text) throws InterruptedException
    {
        otherInfo1Input().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Other Info 1: " + text);
    }

    public void setOtherInfo2Input(String text) throws InterruptedException
    {
        otherInfo2Input().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Other Info 2: " + text);
    }

    public void setOtherInfo3Input(String text) throws InterruptedException
    {
        otherInfo3Input().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Other Info 3: " + text);
    }

    //insurance - employer
    public WebElement employerNameInput()
    {
        return driver.findElement(By.id("employerName"));
    }

    public WebElement employerStatusDDL()
    {
        return driver.findElement(By.id("employerStatusDroplist"));
    }

    public WebElement employerStatusDDLOptionText(int row)
    {
        return driver.findElement(By.xpath(".//*[@id='employerStatusDroplist']/option[" + row + "]"));
    }

    public WebElement employerAddr1Input()
    {
        return driver.findElement(By.id("employerAddressLine1"));
    }

    public WebElement employerAddr2Input()
    {
        return driver.findElement(By.id("employerAddressLine2"));
    }

    public WebElement employerPostalCodeInput()
    {
        return driver.findElement(By.id("employerZip"));
    }

    public WebElement employerPostalCodeSearchBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='payor_tab_A']/div/div[2]/div/div[3]/form/section/div/div[3]/div[2]/div/div/div[1]/div[3]/div[1]/div/a/span"));
    }

    public WebElement employerCityInput()
    {
        return driver.findElement(By.id("employerCity"));
    }

    public WebElement employerStateDDL()
    {
        return driver.findElement(By.id("employerState"));
    }

    public WebElement employerCountryDDL()
    {
        return driver.findElement(By.id("employerCountryCode"));
    }

    public WebElement employerEmailInput()
    {
        return driver.findElement(By.id("employerEmail"));
    }

    public WebElement employerWorkPhoneInput()
    {
        return driver.findElement(By.id("employerWorkPhone"));
    }

    public WebElement employerFaxInput()
    {
        return driver.findElement(By.id("employerFax"));
    }

    public void setEmployerNameInput(String text) throws InterruptedException
    {
        employerNameInput().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Employer Name: " + text);
    }

    public void setEmployerAddr1Input(String text) throws InterruptedException
    {
        employerAddr1Input().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Employer Addr 1: " + text);
    }

    public void setEmployerAddr2Input(String text) throws InterruptedException
    {
        employerAddr2Input().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Employer Addr 2: " + text);
    }

    public void setPostalInput(String text) throws InterruptedException
    {
        employerPostalCodeInput().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Employer Postal Code: " + text);
    }

    public void setEmployerCityInput(String text) throws InterruptedException
    {
        employerCityInput().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Employer City: " + text);
    }

    public void setEmployerEmailInput(String text) throws InterruptedException
    {
        employerEmailInput().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Employer Email: " + text);
    }

    public void setEmployerWorkPhoneInput(String text) throws InterruptedException
    {
        employerWorkPhoneInput().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Employer WorkPhone: " + text);
    }

    public void setEmployerFaxInput(String text) throws InterruptedException
    {
        employerFaxInput().sendKeys(text);
        Thread.sleep(1000);
        LOG.info("        Entered Employer Fax: " + text);
    }

    public void setEmployerPostalCodeInput(String text) throws InterruptedException
    {
        employerPostalCodeInput().sendKeys(text + Keys.TAB);
        Thread.sleep(1000);
        LOG.info("        Entered Employer Fax: " + text);
    }

    //patient info
    public WebElement patientLocationDDL()
    {
        return driver.findElement(By.id("patientLocation"));
    }

    public WebElement patientTypeDDL()
    {
        return driver.findElement(By.id("patientType"));
    }

    public WebElement patientMaritalStatusDDL()
    {
        return driver.findElement(By.id("maritalStatus"));
    }

    public WebElement onsetTypeDDL()
    {
        return driver.findElement(By.id("onsetType"));
    }

    public WebElement onsetDateInput()
    {
        return driver.findElement(By.id("onsetDate"));
    }

    public WebElement accidentCauseDDL()
    {
        return driver.findElement(By.id("accidentCause"));
    }

    public WebElement accidentStateDDL()
    {
        return driver.findElement(By.id("accidentState"));
    }

    public void setOnsetDateInput(String text) throws InterruptedException
    {
        onsetDateInput().sendKeys(text + Keys.TAB);
        Thread.sleep(1000);
        LOG.info("        Entered Onset Date: " + text);
    }

    public WebElement viewClientPortalDocUpDocument()
    {
        return driver.findElement(By.xpath(".//*[@id='viewDocumentHeaderInfo']/div[3]/a"));
    }

    public void clickViewClientPortalDocUpDocumentlink()
    {
        viewClientPortalDocUpDocument().click();
        LOG.info("        Clicked View Client Portal DocUp Document hyperlink.");
    }

    public WebElement phelebIDinput()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/div/div[3]/div[1]/div/div[2]/input"));
    }

    //New Elements : Set User Preferences
    //----------Accession Errors
    public WebElement accessionErrorsDetailRadio()
    {
        return driver.findElement(By.id("accessionErrorsRadioDetail"));
    }

    public WebElement accessionErrorsSummaryRadio()
    {
        return driver.findElement(By.id("accessionErrorsRadioSummary"));
    }

    public WebElement accessionErrorsCollapsedRadio()
    {
        return driver.findElement(By.id("accessionErrorsRadioCollapsed"));
    }

    public WebElement accessionErrorsHiddenRadio()
    {
        return driver.findElement(By.id("accessionErrorsRadioHidden"));
    }

    //----------Contact Notes
    public WebElement contactNotesDetailRadio()
    {
        return driver.findElement(By.id("contactNotesRadioDetail"));
    }

    public WebElement contactNotesSummaryRadio()
    {
        return driver.findElement(By.id("contactNotesRadioSummary"));
    }

    public WebElement contactNotesCollapsedRadio()
    {
        return driver.findElement(By.id("contactNotesRadioCollapsed"));
    }

    public WebElement contactNotesHiddenRadio()
    {
        return driver.findElement(By.id("contactNotesRadioHidden"));
    }

    //----------Physician Info
    public WebElement physicianInfoDetailRadio()
    {
        return driver.findElement(By.id("physicianInfoRadioDetail"));
    }

    public WebElement physicianInfoSummaryRadio()
    {
        return driver.findElement(By.id("physicianInfoRadioSummary"));
    }

    public WebElement physicianInfoCollapsedRadio()
    {
        return driver.findElement(By.id("physicianInfoRadioCollapsed"));
    }

    public WebElement physicianInfoHiddenRadio()
    {
        return driver.findElement(By.id("physicianInfoRadioHidden"));
    }

    //----------Phlebotomist Info
    public WebElement phlebotomistInfoDetailRadio()
    {
        return driver.findElement(By.id("phlebotomistInfoRadioDetail"));
    }

    public WebElement phlebotomistInfoSummaryRadio()
    {
        return driver.findElement(By.id("phlebotomistInfoRadioSummary"));
    }

    public WebElement phlebotomistInfoCollapsedRadio()
    {
        return driver.findElement(By.id("phlebotomistInfoRadioCollapsed"));
    }

    public WebElement phlebotomistInfoHiddenRadio()
    {
        return driver.findElement(By.id("phlebotomistInfoRadioHidden"));
    }

    //----------Patient Info
    public WebElement patientInfoDetailRadio()
    {
        return driver.findElement(By.id("patientInfoRadioDetail"));
    }

    public WebElement patientInfoSummaryRadio()
    {
        return driver.findElement(By.id("patientInfoRadioSummary"));
    }

    public WebElement patientInfoCollapsedRadio()
    {
        return driver.findElement(By.id("patientInfoRadioCollapsed"));
    }

    public WebElement patientInfoHiddenRadio()
    {
        return driver.findElement(By.id("patientInfoRadioHidden"));
    }

    //----------Current Diagnoses
    public WebElement currentDiagnosesDetailRadio()
    {
        return driver.findElement(By.id("currentDiagnosesRadioDetail"));
    }

    public WebElement currentDiagnosesSummaryRadio()
    {
        return driver.findElement(By.id("currentDiagnosesRadioSummary"));
    }

    public WebElement currentDiagnosesCollapsedRadio()
    {
        return driver.findElement(By.id("currentDiagnosesRadioCollapsed"));
    }

    public WebElement currentDiagnosesHiddenRadio()
    {
        return driver.findElement(By.id("currentDiagnosesRadioHidden"));
    }

    //----------Insurance Info
    public WebElement insuranceInfoDetailRadio()
    {
        return driver.findElement(By.id("insuranceInfoRadioDetail"));
    }

    public WebElement insuranceInfoSummaryRadio()
    {
        return driver.findElement(By.id("insuranceInfoRadioSummary"));
    }

    public WebElement insuranceInfoCollapsedRadio()
    {
        return driver.findElement(By.id("insuranceInfoRadioCollapsed"));
    }

    public WebElement insuranceInfoHiddenRadio()
    {
        return driver.findElement(By.id("insuranceInfoRadioHidden"));
    }

    public WebElement secondaryPyrTab()
    {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('a[href=\"#payor_tab_B\"]')[0]");
    }

    //----------Occurrence Codes and Value Codes
    public WebElement occurrenceAndValuesDetailRadio()
    {
        return driver.findElement(By.id("occurrenceAndValuesRadioDetail"));
    }

    public WebElement occurrenceAndValuesSummaryRadio()
    {
        return driver.findElement(By.id("occurrenceAndValuesRadioSummary"));
    }

    public WebElement occurrenceAndValuesCollapsedRadio()
    {
        return driver.findElement(By.id("occurrenceAndValuesRadioCollapsed"));
    }

    public WebElement occurrenceAndValuesHiddenRadio()
    {
        return driver.findElement(By.id("occurrenceAndValuesRadioHidden"));
    }

    //----------Eligibility Transactions
    public WebElement eligibilityTransactionsDetailRadio()
    {
        return driver.findElement(By.id("eligibilityTransactionsRadioDetail"));
    }

    public WebElement eligibilityTransactionsSummaryRadio()
    {
        return driver.findElement(By.id("eligibilityTransactionsRadioSummary"));
    }

    public WebElement eligibilityTransactionsCollapsedRadio()
    {
        return driver.findElement(By.id("eligibilityTransactionsRadioCollapsed"));
    }

    public WebElement eligibilityTransactionsHiddenRadio()
    {
        return driver.findElement(By.id("eligibilityTransactionsRadioHidden"));
    }

    //----------Claim Status Transactions
    public WebElement claimStatusTransactionsDetailRadio()
    {
        return driver.findElement(By.id("claimStatusHistoryRadioDetail"));
    }

    public WebElement claimStatusTransactionsSummaryRadio()
    {
        return driver.findElement(By.id("claimStatusTransactionsRadioSummary"));
    }

    public WebElement claimStatusTransactionsCollapsedRadio()
    {
        return driver.findElement(By.id("claimStatusTransactionsRadioCollapsed"));
    }

    public WebElement claimStatusTransactionsHiddenRadio()
    {
        return driver.findElement(By.id("claimStatusTransactionsRadioHidden"));
    }

    //----------Submit Claims
    public WebElement submitClaimsDetailRadio()
    {
        return driver.findElement(By.id("submitClaimsRadioDetail"));
    }

    public WebElement submitClaimsSummaryRadio()
    {
        return driver.findElement(By.id("submitClaimsRadioSummary"));
    }

    public WebElement submitClaimsCollapsedRadio()
    {
        return driver.findElement(By.id("submitClaimsRadioCollapsed"));
    }

    public WebElement submitClaimsHiddenRadio()
    {
        return driver.findElement(By.id("submitClaimsRadioHidden"));
    }

    //----------Ordered Tests
    public WebElement orderedTestsDetailRadio()
    {
        return driver.findElement(By.id("orderedTestsRadioDetail"));
    }

    public WebElement orderedTestsSummaryRadio()
    {
        return driver.findElement(By.id("orderedTestsRadioSummary"));
    }

    public WebElement orderedTestsCollapsedRadio()
    {
        return driver.findElement(By.id("orderedTestsRadioCollapsed"));
    }

    public WebElement orderedTestsHiddenRadio()
    {
        return driver.findElement(By.id("orderedTestsRadioHidden"));
    }

    //----------Accession Pricing Remarks
    public WebElement accessionPricingRemarksDetailRadio()
    {
        return driver.findElement(By.id("accessionPricingRemarksRadioDetail"));
    }

    public WebElement accessionPricingRemarksSummaryRadio()
    {
        return driver.findElement(By.id("accessionPricingRemarksRadioSummary"));
    }

    public WebElement accessionPricingRemarksCollapsedRadio()
    {
        return driver.findElement(By.id("accessionPricingRemarksRadioCollapsed"));
    }

    public WebElement accessionPricingRemarksHiddenRadio()
    {
        return driver.findElement(By.id("accessionPricingRemarksRadioHidden"));
    }

    //----------Consolidation
    public WebElement consolidationDetailRadio()
    {
        return driver.findElement(By.id("consolidationRadioDetail"));
    }

    public WebElement consolidationSummaryRadio()
    {
        return driver.findElement(By.id("consolidationRadioSummary"));
    }

    public WebElement consolidationCollapsedRadio()
    {
        return driver.findElement(By.id("consolidationRadioCollapsed"));
    }

    public WebElement consolidationHiddenRadio()
    {
        return driver.findElement(By.id("consolidationRadioHidden"));
    }

    //----------Billable Procedure Code and Bulk Transaction Detail
    public WebElement billPrcBulkTransDetailDetailRadio()
    {
        return driver.findElement(By.id("bpcAndbtdRadioDetail"));
    }

    public WebElement billPrcBulkTransDetailSummaryRadio()
    {
        return driver.findElement(By.id("bpcAndbtdRadioSummary"));
    }

    public WebElement billPrcBulkTransDetailCollapsedRadio()
    {
        return driver.findElement(By.id("bpcAndbtdRadioCollapsed"));
    }

    public WebElement billPrcBulkTransDetailHiddenRadio()
    {
        return driver.findElement(By.id("bpcAndbtdRadioHidden"));
    }

    //----------Client Specific Questions
    public WebElement clientSpecificQuestionsDetailRadio()
    {
        return driver.findElement(By.id("clientSpecificQuestionsRadioDetail"));
    }

    public WebElement clientSpecificQuestionsSummaryRadio()
    {
        return driver.findElement(By.id("clientSpecificQuestionsRadioSummary"));
    }

    public WebElement clientSpecificQuestionsCollapsedRadio()
    {
        return driver.findElement(By.id("clientSpecificQuestionsRadioCollapsed"));
    }

    public WebElement clientSpecificQuestionsHiddenRadio()
    {
        return driver.findElement(By.id("clientSpecificQuestionsRadioHidden"));
    }

    public WebElement addClientSpecificQuestionButton()
    {
        return driver.findElement(By.id("add_tbl_clientSpecificInfo"));
    }

    public WebElement clientSpecificQuestionDropDown()
    {
        return driver.findElement(By.id("question"));
    }

    public WebElement clientSpecificQuestionResponse()
    {
        return driver.findElement(By.id("response"));
    }

    //----------Clinical Trial
    public WebElement clinicalTrialDetailRadio()
    {
        return driver.findElement(By.id("clinicalTrialRadioDetail"));
    }

    public WebElement clinicalTrialSummaryRadio()
    {
        return driver.findElement(By.id("clinicalTrialRadioSummary"));
    }

    public WebElement clinicalTrialCollapsedRadio()
    {
        return driver.findElement(By.id("clinicalTrialRadioCollapsed"));
    }

    public WebElement clinicalTrialHiddenRadio()
    {
        return driver.findElement(By.id("clinicalTrialRadioHidden"));
    }

    //----------Chain of Custody
    public WebElement chainOfCustodyDetailRadio()
    {
        return driver.findElement(By.id("cocRadioDetail"));
    }

    public WebElement chainOfCustodySummaryRadio()
    {
        return driver.findElement(By.id("cocRadioSummary"));
    }

    public WebElement chainOfCustodyCollapsedCheckBox()
    {
        return driver.findElement(By.id("cocRadioCollapsed"));
    }

    public WebElement chainOfCustodyHiddenRadio()
    {
        return driver.findElement(By.id("cocRadioHidden"));
    }

    //---------GENERATE
    public WebElement cancelUserPreferenceButton()
    {
        return driver.findElement(By.xpath("//*[@id='cancelUserPreferenceButton']/span"));
    }

    public WebElement saveUserPreferenceButton()
    {
        return driver.findElement(By.xpath("//*[@id='saveUserPreferenceButton']/span"));
    }

    public WebElement setUserPreferencesLink()
    {
        return driver.findElement(By.id("setUserPreference"));
    }

    public WebElement saveUserPreferenceLink()
    {
        return driver.findElement(By.id("saveUserPreference"));
    }

    //---------Functions for GENERATE elements
    public void clickSaveUserPreferenceLink() throws InterruptedException
    {
        saveUserPreferenceLink().click();
        Thread.sleep(3000);
        LOG.info("        Clicked on Save User Preferences link.");
    }

    public void clickSetUserPreferenceLink()
    {
        setUserPreferencesLink().click();
        LOG.info("        Clicked on Set User Preferences link");
    }

    public void clickGridsRadio(WebElement e, String name)
    {
        e.click();
        LOG.info("        Clicked on " + name);
    }

    public WebElement patientAddressLine(int val)
    {
        return driver.findElement(By.xpath(".//*[@id='patientAddressLine" + val + "']"));
    }

    public void inputToPatientAddressLine(int line, String val)
    {
        patientAddressLine(line).clear();
        patientAddressLine(line).sendKeys(val);
        LOG.info("        Input " + val + " to Patient Address Line " + line);
    }

    public WebElement allGridCheckBox(int col, int row)
    {
        return driver.findElement(By.xpath(".//*[@id='dlgSetUserPreferences']/div/div/div[" + (col + 1) + "]/div[2]/label[" + row + "]/input"));
    }

    public boolean isAllFielsCollapse()
    {
        boolean isCollapse = true;
        List<WebElement> allElements = driver.findElements(By.cssSelector(".showDetailLink.ui-icon-white.ui-icon-triangle-1-n"));
        List<WebElement> el = new ArrayList<>();
        for (WebElement element : allElements)
        {
            if (element.getCssValue("background-position").equals("0px -16px"))
            {
                el.add(element);
            }
        }
        return isCollapse;
    }

    public WebElement setUserPreferencesPopup()
    {
        return driver.findElement(By.xpath(".//*[@id='dlgSetUserPreferences']/div"));
    }

    /*COLLAPSE BUTTON : ALL SECTION - START*/
    public WebElement collapseSectionErrorsBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionErrors']/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionNotesBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionNotes']/section/header/div[1]/span/a"));
    }

    public WebElement collapsePhysicianInfoSectionBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='physicianInfoSection']/div/section/header/div[1]/span/a"));
    }

    public WebElement collapsePhlebInfoSectionBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='phlebInfoSection']/div/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionPatientBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionPatient']/div/div/form/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionDiagnosisBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionDiagnosis']/div/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionInsuranceBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionInsurance']/div/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionOccurrenceAndValuesBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionOccurrenceAndValues']/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionEligibilityTransactionsBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionEligibilityTransactions']/div/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionClaimsStatusTransactionsBtn()
    {
        return driver.findElement(By.xpath("//*[@id=\"sectionClaimStatusHistory\"]/div/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionSubmitClaimsBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionSubmitClaims']/div/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionTransactionsBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionTransactions']/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionAccnLevelPricingMessagesBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionAccnLevelPricingMessages']/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionConsolidationDetailsBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionConsolidationDetails']/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionBillableProcedureCodeAndBulkTransactionsBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionBillableProcedureCodeAndBulkTransactions']/section/header/div[1]/span/a"));
    }

    public WebElement collapseSectionClientSpecificQuestionsBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionOtherDemo']/section[1]/header/div[1]/span/a"));
    }

    public WebElement collapseSectionClinicalTrialBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionOtherDemo']/section[2]/header/div[1]/span/a"));
    }

    public WebElement collapseSectionChainOfCustodyBtn()
    {
        return driver.findElement(By.xpath(".//*[@id='sectionOtherDemo']/section[3]/header/div[1]/span/a"));
    }

    public void checkSectionIsCollapsed(WebElement el, String position)
    {
        Assert.assertTrue(checkCssStyleForCollapsedSummaryDetailIcon(el, position), "        Section is not collapsed.");
    }

    public WebElement ptDemoUpdateUpdateRadioBtn()
    {
        return driver.findElement(By.id("updateInfo"));
    }

    public void clickUpdatePtDemoInfoRadioBtn()
    {
        ptDemoUpdateUpdateRadioBtn().click();
        LOG.info("        Clicked Update the patient insurance information Radio button.");
    }

    public boolean checkCssStyleForCollapsedSummaryDetailIcon(WebElement el, String position)
    {
        boolean isSectionCollapsed = false;
        if (el.getCssValue("background-position").equals(position))
        {
            //detail : -64px -16px
            //summary : -32px -16px
            //Collapse  : 0px -16px
            isSectionCollapsed = true;
        }
        return isSectionCollapsed;
    }
    /*COLLAPSE BUTTON : ALL SECTION - END*/

    public List<List<String>> radioDefautl()
    {
        List<WebElement> gridsSectionCount = driver.findElements(By.xpath("//div[@class='checkSetUserPreferencesContainer']//div[@class='unit size45']"));
        List<String> temp;
        List<List<String>> radioDefautl = new ArrayList<>();

        for (int i = 2; i <= gridsSectionCount.size(); i++)
        {
            WebElement section = driver.findElement(By.xpath("//div[@class='checkSetUserPreferencesContainer']/div[" + i + "]/div[@class='unit size45']"));
            temp = new ArrayList<>();
            temp.add(section.getText());
            for (int j = 1; j <= 4; j++)
            {
                WebElement state = driver.findElement(By.xpath("//div[@class='checkSetUserPreferencesContainer']/div[" + i + "]/div[@class='unit lastUnit']/label[" + j + "]/input"));
                if (state.getAttribute("checked") != null)
                {
                    state = driver.findElement(By.xpath("//div[@class='checkSetUserPreferencesContainer']/div[" + i + "]/div[@class='unit lastUnit']/label[" + j + "]"));
                    temp.add(state.getText());
                }
            }
            radioDefautl.add(temp);
        }
        return radioDefautl;
    }

    public void returnRadioDefautl(List<List<String>> radioDefautl) throws InterruptedException
    {
        List<WebElement> gridsSectionCount = driver.findElements(By.xpath("//div[@class='checkSetUserPreferencesContainer']//div[@class='unit size45']"));
        for (int i = 1; i < gridsSectionCount.size(); i++)
        {
            radioDefautl.size();
            String state = radioDefautl.get(i - 1).get(1);
            for (int j = 1; j <= 4; j++)
            {
                WebElement stateName = driver.findElement(By.xpath("//div[@class='checkSetUserPreferencesContainer']/div[" + (i + 1) + "]/div[@class='unit lastUnit']/label[" + j + "]"));
                if (stateName.getText().equals(state))
                {
                    stateName.click();
                    LOG.info("      Click on " + stateName.getText() + " radio");
                }
            }
        }
        SeleniumBaseTest b = new SeleniumBaseTest();
        Assert.assertTrue(b.isElementPresent(saveUserPreferenceButton(), 5), "        saveUserPreferenceButton should present.");
        clickGridsRadio(saveUserPreferenceButton(), "saveUserPreferenceButton");
    }

    public void setDefaultUserPreference() throws InterruptedException
    {
        SeleniumBaseTest b = new SeleniumBaseTest();
        Thread.sleep(3000);

        Assert.assertTrue(b.isElementPresent(accessionErrorsSummaryRadio(), 5), "        accessionErrorsSummaryRadio should present.");
        clickGridsRadio(accessionErrorsSummaryRadio(), "accessionErrorsSummaryRadio");

        Assert.assertTrue(b.isElementPresent(contactNotesSummaryRadio(), 5), "        contactNotesSummaryRadio should present.");
        clickGridsRadio(contactNotesSummaryRadio(), "contactNotesSummaryRadio");

        Assert.assertTrue(b.isElementPresent(physicianInfoSummaryRadio(), 5), "        physicianInfoSummaryRadio should present.");
        clickGridsRadio(physicianInfoSummaryRadio(), "physicianInfoSummaryRadio");

        Assert.assertTrue(b.isElementPresent(phlebotomistInfoSummaryRadio(), 5), "        phlebotomistInfoSummaryRadio should present.");
        clickGridsRadio(phlebotomistInfoSummaryRadio(), "phlebotomistInfoSummaryRadio");

        Assert.assertTrue(b.isElementPresent(patientInfoDetailRadio(), 5), "        patientInfoDetailRadio should present.");
        //clickGridsRadio(patientInfoHiddenRadio(), "patientInfoHiddenRadio");
        clickGridsRadio(patientInfoDetailRadio(), "patientInfoDetailRadio");

        Assert.assertTrue(b.isElementPresent(currentDiagnosesSummaryRadio(), 5), "        currentDiagnosesSummaryRadio should present.");
        clickGridsRadio(currentDiagnosesSummaryRadio(), "currentDiagnosesSummaryRadio");

        Assert.assertTrue(b.isElementPresent(insuranceInfoDetailRadio(), 5), "        insuranceInfoDetailRadio should present.");
        clickGridsRadio(insuranceInfoDetailRadio(), "insuranceInfoDetailRadio");

        Assert.assertTrue(b.isElementPresent(occurrenceAndValuesDetailRadio(), 5), "        occurrenceAndValuesDetailRadio should present.");
        clickGridsRadio(occurrenceAndValuesDetailRadio(), "occurrenceAndValuesDetailRadio");

        Assert.assertTrue(b.isElementPresent(eligibilityTransactionsDetailRadio(), 5), "        eligibilityTransactionsDetailRadio should present.");
        clickGridsRadio(eligibilityTransactionsDetailRadio(), "eligibilityTransactionsDetailRadio");

        Assert.assertTrue(b.isElementPresent(claimStatusTransactionsDetailRadio(), 5), "        claimStatusTransactionsDetailRadio should present.");
        clickGridsRadio(claimStatusTransactionsDetailRadio(), "claimStatusTransactionsDetailRadio");

        Assert.assertTrue(b.isElementPresent(submitClaimsDetailRadio(), 5), "        submitClaimsDetailRadio should present.");
        clickGridsRadio(submitClaimsDetailRadio(), "submitClaimsDetailRadio");

        Assert.assertTrue(b.isElementPresent(orderedTestsSummaryRadio(), 5), "        orderedTestsSummaryRadio should present.");
        clickGridsRadio(orderedTestsSummaryRadio(), "orderedTestsSummaryRadio");

        Assert.assertTrue(b.isElementPresent(accessionPricingRemarksSummaryRadio(), 5), "        accessionPricingRemarksSummaryRadio should present.");
        clickGridsRadio(accessionPricingRemarksSummaryRadio(), "accessionPricingRemarksSummaryRadio");

        Assert.assertTrue(b.isElementPresent(consolidationSummaryRadio(), 5), "        consolidationSummaryRadio should present.");
        clickGridsRadio(consolidationSummaryRadio(), "consolidationSummaryRadio");

        Assert.assertTrue(b.isElementPresent(billPrcBulkTransDetailDetailRadio(), 5), "        billPrcBulkTransDetailDetailRadio should present.");
        clickGridsRadio(billPrcBulkTransDetailDetailRadio(), "billPrcBulkTransDetailDetailRadio");

        Assert.assertTrue(b.isElementPresent(clientSpecificQuestionsDetailRadio(), 5), "        clientSpecificQuestionsDetailRadio should present.");
        clickGridsRadio(clientSpecificQuestionsDetailRadio(), "clientSpecificQuestionsDetailRadio");

        Assert.assertTrue(b.isElementPresent(clinicalTrialSummaryRadio(), 5), "        clinicalTrialSummaryRadio should present.");
        clickGridsRadio(clinicalTrialSummaryRadio(), "clinicalTrialSummaryRadio");

        Assert.assertTrue(b.isElementPresent(chainOfCustodySummaryRadio(), 5), "        chainOfCustodySummaryRadio should present.");
        clickGridsRadio(chainOfCustodySummaryRadio(), "chainOfCustodySummaryRadio");
    }

    public void clickAccnErorsHiddenRadio()
    {
        accessionErrorsHiddenRadio().click();
        LOG.info("        Click on accession Errors Hidden Radio");
    }

    public void clickContactNotesDetailRadio()
    {
        contactNotesDetailRadio().click();
        LOG.info("        Click on contact Notes Detail Radio");
    }

    public void clickPhysicianInfoSummaryRadio()
    {
        physicianInfoSummaryRadio().click();
        LOG.info("        Click on physician Info Summary Radio");
    }

    public void clickSaveUserPreferenceBtn() throws InterruptedException
    {
        saveUserPreferenceButton().click();
        Thread.sleep(3000);
        LOG.info("        Clicked on Save User Preferences Button");
    }

    public void clickAllPreferencesRadio(int stateSeq)
    {
        List<WebElement> allButton = driver.findElements(By.xpath(".//*[@id='dlgSetUserPreferences']/div/div/div/div[2]/label[" + stateSeq + "]/input"));
        for (WebElement button : allButton)
        {
            button.click();
        }
    }

    public int countAccnIdAndErrCdAndProcCdAndErrSeqFromACCNPROCERR(String value)
    {
        int count = 0;
        List<WebElement> errGrps = driver.findElements(By.xpath("//table[@id='tbl_accnCurrentErrors']/tbody/tr/td[5]"));
        for (int i = 0; i < errGrps.size() - 1; i++)
        {
            WebElement errGrp = driver.findElement(By.xpath("//table[@id='tbl_accnCurrentErrors']/tbody/tr[" + (i + 2) + "]/td[5]"));
            if (errGrp.getText().equals(value))
            {
                count++;
            }
        }
        return count;
    }

    public boolean checkAllSectionsAreHidden()
    {
        boolean isHidden = false;
        List<WebElement> sections = driver.findElements(By.xpath("html/body/section/div/div/div[2]/div[2]/div[@id='mainSections']/div"));
        for (int i = 2; i < sections.size(); i++)
        {
            WebElement section = driver.findElement(By.xpath("html/body/section/div/div/div[2]/div[2]/div[@id='mainSections']/div[" + i + "]//section/div"));
            isHidden = section.getCssValue("display").equals("none");
        }

        return isHidden;
    }

    public List<String> getAllDisplayOfSections()
    {
        List<String> temp = new ArrayList<>();
        List<WebElement> sections = driver.findElements(By.xpath("html/body/section/div/div/div[2]/div[2]/div[@id='mainSections']/div//section"));
        for (WebElement section : sections)
        {
            temp.add(section.getAttribute("data-current-state"));
        }
        return temp;
    }

    public void clickSecondaryPyrTab()
    {
        secondaryPyrTab().click();
        LOG.info("        Clicked Secondary Payor tab.");
    }

    public void clickSavdAndClearBtn() throws InterruptedException
    {
        saveAndClearBtn().click();
        Thread.sleep(3000);
        LOG.info("        Clicked Save And Clear button.");
    }

    /**
     * Clicks the Save and Clear button, then waits until save is complete.
     *
     * @param wait the wait handler
     */
    public void saveAndClear(WebDriverWait wait)
    {
        WebElement saveAndClearBtn = saveAndClearBtn();
        LOG.info("Waiting for Save and Clear button to be clickable");
        wait.until(ExpectedConditions.elementToBeClickable(saveAndClearBtn));
        saveAndClearBtn.sendKeys(Keys.ALT + "S");
        try
        {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));

        }
        catch (Exception e)
        {
            LOG.info("Save and Clear button is still visible");
            saveAndClearBtn.sendKeys(Keys.ALT + "S");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
        }
    }

    public void saveAndClearWithNewErrors(WebDriverWait wait)
    {
        WebElement saveAndClearBtn = saveAndClearBtn();
        accnIdText().sendKeys(Keys.ALT + "S");
        LOG.info(isErrorReturned());
        if (isErrorReturned())
        {
            accnIdText().sendKeys(Keys.ALT + "S");
        }

        wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(saveAndClearBtn)));
    }

    /**
     * Clicks the delete button for the payor at the given index.
     * @param index the index of the payor to delete
     * @
     */
    public void deletePayor(int index)
    {
        ((JavascriptExecutor) driver).executeScript("$(\"#payorTabs .payorTab:eq(" + index + ") .ui-icon-close\").click()");
    }

    public WebElement patientFirstName()
    {
        return driver.findElement(By.id(ID_PATIENT_FIRST_NAME));

    }

    public WebElement patientLastName()
    {
        return driver.findElement(By.id(ID_PATIENT_LAST_NAME));
    }

    public WebElement patientGender()
    {
        return driver.findElement(By.name(NAME_PATIENT_GENDER));
    }

    public WebElement insuredGender()
    {
        return driver.findElement(By.name(NAME_INSURED_GENDER));
    }

    public WebElement insuredFirstName()
    {
        return driver.findElement(By.id(ID_INSURED_FIRST_NAME));
    }

    public WebElement insuredLastName()
    {
        return driver.findElement(By.id(ID_INSURED_LAST_NAME));
    }

    public WebElement insuredZipCode()
    {
        return driver.findElement(By.id(ID_INSURED_ZIP_CODE));
    }

    public WebElement insuredAddress(int val)
    {
        return driver.findElement(By.xpath(".//*[@id='insuredAddressLine" + val + "']"));
    }

    public WebElement insuredCity()
    {
        return driver.findElement(By.id(ID_INSURED_CITY));
    }
    public WebElement accessionDetailTitle()
    {
        return driver.findElement(By.cssSelector(".platormPageTitle"));
    }

    public WebElement detailViewAllIco()
    {
        return driver.findElement(By.xpath(".//*[@title='Detail view all']"));
    }

    public WebElement bulkTransDetailSectionElcPaymentLnk()
    {
        return driver.findElement(By.xpath(".//*[contains(@class,'btnAddElectronicPayment')]"));
    }

    public WebElement cardInformationAmountInput()
    {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("ccAmountField")));
    }

    public WebElement cardInformationCommentInput()
    {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("comment")));
    }

    public WebElement cardNumberInput()
    {
        return driver.findElement(By.id("cardNumber"));
    }

    public WebElement expMonthInput()
    {
        return driver.findElement(By.id("expiryMonth"));
    }

    public WebElement expYearInput()
    {
        return driver.findElement(By.id("expiryYear"));
    }

    public WebElement cvvInput()
    {
        return driver.findElement(By.id("cvv"));
    }
    public WebElement billingFirstNameInput()
    {
        return driver.findElement(By.id("billingFirstName"));
    }
    public WebElement billingLastNameInput()
    {
        return driver.findElement(By.id("billingLastName"));
    }
    public WebElement billingStreetAddressInput()
    {
        return driver.findElement(By.id("billingAddressLine"));
    }
    public WebElement billingZipCodeInput()
    {
        return driver.findElement(By.id("billingPostalCode"));
    }


    public WebElement payBtn()
    {
        return driver.findElement(By.id("submitToConfirm"));
    }
    public WebElement closeBtn()
    {
        return driver.findElement(By.cssSelector(".ui-button-text"));
    }

    public WebElement ptmBoxiFrame()
    {
        return driver.findElement(By.id("xifinpciiframe"));
    }

    public WebElement confirmPaymentInformationTitle()
    {
        return driver.findElement(By.id("myModalLabel"));
    }

    public WebElement confirmPaymentInformationTxt()
    {
        return driver.findElement(By.xpath("//*[@id='confirmPaymentInfoModal']//div[@ng-show='isCreditCardPayment()']"));
    }

    public WebElement confirmPaymentInformationConfirmBtn()
    {
        return driver.findElement(By.id("submit"));
    }

    public WebElement transStatusCloseIco()
    {
        return driver.findElement(By.xpath(".//span[contains(text(),'Electronic Payment Wizard')]//parent::div/button"));
    }

    public WebElement bulkPmtAndDenialsTblPaymentAmountCol(String row)
    {
        return driver.findElement(By.xpath(".//table[@id='tbl_bulkPaymentsAndDenials']//tr[" + row + "]/td[@aria-describedby='tbl_bulkPaymentsAndDenials_paymentAmount']"));
    }

    public WebElement occurrenceCodeDropdown()
    {
        return driver.findElement(By.id("occurrenceCode"));
    }

    public WebElement occurrencePyrId()
    {
        return driver.findElement(By.id("payorAbbrev"));
    }

    public void setOccurrenceCodeId(String text, WebDriverWait wait)
    {
        WebElement occurrenceCodeDropDown = occurrenceCodeDropdown();
        Select select = new Select(occurrenceCodeDropDown);
        select.selectByVisibleText(text);
        LOG.info("Entered Input Into Occurrence Code DropDown: " + text);
    }

    public void selectPatientGender(String val)
    {
        Select select = new Select(patientGender());
        select.selectByValue(val);
        LOG.debug("Select patient Gender from drop down list");
    }

    public void selectInsuredGender(String val)
    {
        Select select = new Select(insuredGender());
        select.selectByValue(val);
        LOG.debug("Select patient Gender from drop down list");
    }

    public void updatePatientFirstName(String value)
    {
        patientFirstName().clear();
        patientFirstName().sendKeys(value);
        patientFirstName().sendKeys(Keys.TAB);
        LOG.debug("Patient First Name is updated to " + value);
    }

    public void updatePatientLastName(String value)
    {
        patientLastName().clear();
        patientLastName().sendKeys(value);
        patientLastName().sendKeys(Keys.TAB);
        LOG.debug("Patient Last Name is updated to " + value);
    }

    public void updateInsuredFirstName(String value)
    {
        insuredFirstName().clear();
        insuredFirstName().sendKeys(value);
        insuredFirstName().sendKeys(Keys.TAB);
        LOG.debug("Insured First Name is updated to " + value);
    }

    public void updateInsuredLastName(String value)
    {
        insuredLastName().clear();
        insuredLastName().sendKeys(value);
        insuredLastName().sendKeys(Keys.TAB);
        LOG.debug("Insured Last Name is updated to " + value);
    }

    public void updateInsuredCity(String value)
    {
        insuredCity().clear();
        insuredCity().sendKeys(value);
        insuredCity().sendKeys(Keys.TAB);
        LOG.debug("Insured City is updated to " + value);
    }

    public void updateInsuredZipCode(String value)
    {
        insuredZipCode().clear();
        insuredZipCode().sendKeys(value);
        insuredZipCode().sendKeys(Keys.TAB);
        LOG.debug("Insured Zip Code is updated to " + value);
    }

    public void inputToInsuredAddressLine(int line, String val)
    {
        insuredAddress(line).clear();
        insuredAddress(line).sendKeys(val);
        LOG.debug("Input " + val + " to Insured Address Line " + line);
    }

    public WebElement select2DropInput()
    {
        return driver.findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
    }

    public void clickSubmitClaimAddBtn(WebDriverWait wait)
    {
        wait.until(ExpectedConditions.elementToBeClickable(submitClaimAddBtn()));
        submitClaimAddBtn().click();
        LOG.info("Clicked submitClaimEditBtn");
        wait.until(ExpectedConditions.visibilityOf(submitClaimAddBtn()));

    }

    public void setPayorAndSubscriberID(String payorAndSubscriberID, WebDriverWait wait) throws InterruptedException
    {
        WebElement payorAndSubscriberIDDropDown = payorAndSubscriberIDText();
        Select select = new Select(payorAndSubscriberIDDropDown);
        select.selectByVisibleText(payorAndSubscriberID);
        try
        {
            LOG.info("Waiting for ok button to be clickable");
            wait.until(ExpectedConditions.elementToBeClickable(okPopupBtn()));
        }
        catch (NoSuchElementException e){
            LOG.info("Exception e="+e);
        }
        Thread.sleep(2000);
        LOG.info("Selected Payor and Subscriber Id: " + payorAndSubscriberID + " from Dropdown List.");

    }

    public void setActionText(String actionText, WebDriverWait wait)
    {
        WebElement actionDropDown = actionText();
        long startTime = System.currentTimeMillis();
        boolean isEnabled;
        do
        {
            wait.until(ExpectedConditions.elementToBeClickable(actionDropDown));
            actionDropDown.click();
            isEnabled = actionDropDown.isEnabled();
        }
        while (!isEnabled && System.currentTimeMillis() - startTime < 10000);
        LOG.info("DropDown is enabled: " + actionText);
        select2DropInput().sendKeys(actionText + Keys.TAB);
        LOG.info("Entered Input Into ActionText DropDown: " + actionText);
    }

    public void setFormatText(String formatText, WebDriverWait wait)
    {
        WebElement formatTextDropDown = formatText();
        long startTime = System.currentTimeMillis();
        boolean isEnabled;
        do
        {
            wait.until(ExpectedConditions.elementToBeClickable(formatTextDropDown));
            isEnabled = formatTextDropDown.isEnabled();
        }
        while (!isEnabled && System.currentTimeMillis() - startTime < 5000);
        Select select = new Select(formatTextDropDown);
        select.selectByVisibleText(formatText);
        LOG.info("Selected FormatText DropDown with: " + formatText);
    }

    public void setIcnText(String icn)
    {
        icnText().clear();
        icnText().sendKeys(icn);
        icnText().sendKeys(Keys.TAB);
        LOG.debug("Input " + icn + " to ICN");
    }

    public void setAttachmentType(String attachmentType)
    {
        LOG.debug("message=Selecting attachment type, attachmentType="+attachmentType);
        WebElement attachmentTypeSelect = selectAttachmentType();
        attachmentTypeSelect.sendKeys(attachmentType);
        attachmentTypeSelect.sendKeys(Keys.TAB);
    }

    public void setOccurrencePyr(String occurrencePyrAbbrev, WebDriverWait wait)
    {
        WebElement occurrencePyrId = occurrencePyrId();
        LOG.info("Clearing payor Id field");
        occurrencePyrId.clear();
        LOG.info("Sending occurrence payor Id, occurrencePyrAbbrev="+occurrencePyrAbbrev);
        Actions actions = new Actions(driver);
        actions.click(occurrencePyrId).sendKeys(occurrencePyrAbbrev).perform();
        LOG.info("Waiting for occurrence payor Id value, occurrencePyrAbbrev="+occurrencePyrAbbrev);
        wait.until(ExpectedConditions.textToBePresentInElementValue(occurrencePyrId, occurrencePyrAbbrev));
        LOG.info("Sending tab key");
        actions.sendKeys(Keys.TAB).perform();
    }


    public void setConditionCode(String value)
    {
        WebElement conditionCode = driver.findElement(By.xpath(".//*[@id='conditionCode']/option[" + value + "]"));
        conditionCode.click();
        LOG.debug("Select Condition Code from drop down list");
    }

    public WebElement forceToEpDropdown(WebDriverWait wait)
    {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("forceEpQueAction")));
    }

    public WebElement epHoldComments()
    {
        return driver.findElement(By.id("epHoldComments"));
    }

    public void setClientSpecificQuestion(String question, String response, WebDriverWait wait)
    {
        WebElement clientSpecificQuestionDropDown = clientSpecificQuestionDropDown();
        Select select = new Select(clientSpecificQuestionDropDown);
        select.selectByVisibleText(question);
        WebElement clientSpecificQuestionResponse = clientSpecificQuestionResponse();
        clientSpecificQuestionResponse.click();
        clientSpecificQuestionResponse.sendKeys(response);
        wait.until(ExpectedConditions.elementToBeClickable(okPopupBtn()));
        okPopupBtn().click();
        LOG.info("Entered Input Into Client Specific Question DropDown: " + response);
    }

    public WebElement fixUnfixErrEligAccnDetail()
    {
        return driver.findElement(By.xpath(".//td[contains(@class, 'fixedError')]/input"));
    }

    public void fixUnfixedErrEligAccnDetail()
    {
        fixUnfixErrEligAccnDetail().click();
        LOG.info("Fixed unfixed elig error on the accn.");
    }

    public void submitClaimsOnAccnDetail(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, WebDriverWait wait) throws Exception
    {
        submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait, false, false);
    }

    public void submitClaimsOnAccnDetail(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, WebDriverWait wait, boolean isAccnLoaded) throws Exception
    {
        submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait, isAccnLoaded, false);
    }

    public void submitClaimsOnAccnDetail(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, WebDriverWait wait, boolean isAccnLoaded, boolean isTranslateProcs) throws Exception
    {
        if (!isAccnLoaded)
        {
            LOG.info("Load Accn on Accn Detail");
            loadAccnOnAccnDetail(wait, accnId);
            LOG.info("Wait Accn to load");
        }

        LOG.info("Click on Submit Claim");
        clickHiddenPageObject(submitClaimAddBtn(), 0);

        LOG.info("Enter Claim Info into Submit Claim popup");

        enterSubmitClaimInfo(pyrAbbrv, subId, submSvcAbbrev, "Original", "", "", wait, isTranslateProcs);
        LOG.info("Enter Claim Info into Submit Claim popup");
        Assert.assertTrue(isAccnLoaded(accnId, wait));
        LOG.info("Click Save and Clear");
        accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetailTitle(), "Detail"));
    }

    public void clickHiddenPageObject(WebElement element, int elementArg) throws Exception
    {

        if (element != null)
        {
            driver.executeScript("arguments[" + elementArg + "].click();", element);
            LOG.info("        Click on element " + element);
            Thread.sleep(1000);
        }
        else
        {
            LOG.error("        Element " + element + " does not exist");
        }
    }

    public void enterSubmitClaimInfo(String primPyrAbbrv, String subId, String submSvcAbbrev, String action, String icn, String conditionCode, WebDriverWait wait) throws InterruptedException
    {
        enterSubmitClaimInfo(primPyrAbbrv, subId, submSvcAbbrev, action, icn, conditionCode, wait, false);
    }

    public void enterSubmitClaimInfo(String primPyrAbbrv, String subId, String submSvcAbbrev, String action, String icn, String conditionCode, WebDriverWait wait, boolean isTranslateProcs) throws InterruptedException
    {
        LOG.info("Waiting for submit claim popup");
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("edithdtbl_submitClaims"))));
        LOG.info("Setting payor and subscriber ID");
        String payorSubIdData = StringUtils.isNotBlank(subId) ? primPyrAbbrv + " | " + subId : primPyrAbbrv;
        setPayorAndSubscriberID(payorSubIdData, wait);
        if(!StringUtils.equals(action, "Original"))
        {
            LOG.info("Setting action, action="+action);
            wait.until(ExpectedConditions.elementToBeClickable(actionText()));
            setActionText(action, wait);
        }
        LOG.info("Setting format");
        setFormatText(submSvcAbbrev, wait);
        if(StringUtils.isNotBlank(icn))
        {
            LOG.info("Setting ICN, icn="+icn);
            setIcnText(icn);
        }
        if(StringUtils.isNotBlank(conditionCode) && StringUtils.equalsIgnoreCase(conditionCode,"D6"))
        {
            LOG.info("Setting condition code, conditionCode="+conditionCode);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("conditionCode"))));
            setConditionCode("7");
        }
        else if(StringUtils.isNotBlank(conditionCode))
        {
            LOG.info("Setting condition code, conditionCode="+conditionCode);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("conditionCode"))));
            setConditionCode("11");
        }
        if (isTranslateProcs)
        {
            translateProcsCheckbox().click();
        }
        LOG.info("Clicking OK button");
        wait.until(ExpectedConditions.elementToBeClickable(okPopupBtn()));
        okPopupBtn().click();
    }

    public void setForceToEP(String epQue, WebDriverWait wait)
    {
        WebElement forceToEP = forceToEpDropdown(wait);
        wait.until(ExpectedConditions.visibilityOf(forceToEP));
        Select select = new Select(forceToEP);
        select.selectByValue(epQue);
        LOG.info("Selected from Force to EP DropDown: " + epQue);
    }
    public void loadAccnOnAccnDetail(WebDriverWait wait, String accnId)
    {
        boolean isAccnLoaded;
        LoadAccession loadAccession = new LoadAccession(driver);
        loadAccession.setAccnId(accnId, wait);
        isAccnLoaded = isAccnLoaded(accnId, wait);
        if (!isAccnLoaded)
        {
            // try again...
            driver.navigate().refresh();
            loadAccession.setAccnId(accnId, wait);
            isAccnLoaded = isAccnLoaded(accnId, wait);
        }
        Assert.assertTrue(isAccnLoaded, "Unable to load accn on AccnDetail, accnId="+accnId);
        LOG.info("Loaded on Accession Detail: " + accnId);
    }
    public void loadAccnOnAccnSearch(String accnId) throws Exception
    {
        AccessionSearch accessionSearch=new AccessionSearch(driver);
        accessionSearch.setAccnId1(accnId);
        accessionSearch.searchBtn().click();
        LOG.info("Loaded on Accession Detail: " + accnId);
    }
    public void setInputValue(WebElement e, String value ) throws Exception{

        e.clear();

        Thread.sleep(2000);
        e.click();
        e.sendKeys(value);
        e.sendKeys(Keys.TAB);
        Thread.sleep(2000);
        LOG.info("        Entered value " + value);
    }
    public WebElement employerNameText() {
        return driver.findElement(By.id("cocEmployerName"));
    }

    public WebElement contactNameText() {
        return driver.findElement(By.id("cocEmployerContactName"));
    }

    public void setEmployerName(String employerName) throws InterruptedException {
        employerNameText().sendKeys(employerName);
        employerNameText().sendKeys(Keys.TAB);
        LOG.info("        Entered employerName: " + employerName);
    }

    public void setContactName(String contactName) throws InterruptedException {
        contactNameText().sendKeys(contactName);
        contactNameText().sendKeys(Keys.TAB);
        LOG.info("        Entered employerName: " + contactName);
    }

    public WebElement currentChainOfCustodyCurrentView() {
        return driver.findElement(By.cssSelector("#sectionOtherDemo > section.groupContainer.chainOfCustodyBlockGroup > header > div:nth-child(1) > span > a"));
    }
    public WebElement cocEmployerNameSummaryView(){
        return driver.findElement(By.cssSelector("span[data-mirror='cocEmployerName']"));
    }
    public WebElement cocContactNameSummaryView(){
        return driver.findElement(By.cssSelector("span[data-mirror='cocEmployerContactName']"));
    }
    public WebElement currentClinicalTrialView() {
        return driver.findElement(By.cssSelector("#sectionOtherDemo > section.groupContainer.clinicalTrialBlockGroup > header > div:nth-child(1) > span > a"));
    }
    public WebElement trialNameText() {
        return driver.findElement(By.id("trialName"));
    }

    public WebElement encounterIdText() {
        return driver.findElement(By.id("trialEncounterID"));
    }
    public WebElement encounterDateText(){
        return driver.findElement(By.id("trialEncounterDate"));
    }

    public void setTrialName(String trialName) throws InterruptedException {
        trialNameText().sendKeys(trialName);
        trialNameText().sendKeys(Keys.TAB);
        LOG.info("        Entered trialName: " + trialName);
    }

    public void setEncounterId(String encounterId) throws InterruptedException {
        encounterIdText().sendKeys(encounterId);
        encounterIdText().sendKeys(Keys.TAB);
        LOG.info("        Entered encounterId: " + encounterId);
    }
    public void setEncounterDate(String encounterDate) throws InterruptedException {
        encounterDateText().sendKeys(encounterDate);
        encounterDateText().sendKeys(Keys.TAB);
        LOG.info("        Entered encounterDate: " + encounterDate);
    }
    public WebElement clinTrilTrialNameSummaryView(){
        return driver.findElement(By.cssSelector("span[data-mirror='trialName']"));
    }
    public WebElement currentClnSpecificQuesView() {
        return driver.findElement(By.cssSelector("#sectionOtherDemo > section.groupContainer.clientSpecificInfoBlockGroup > header > div:nth-child(1) > span > a"));
    }
    public WebElement deletePayorTab(int value){
        return driver.findElement(By.xpath("//*[@id=\"payorTabs\"]/ul/li[" + value + "]/span"));

    }

    public void clickOrderedTestAbnReceivedCheckbox(int accnTestSeqId)
    {
        orderedTestAbnReceievedCheckbox(accnTestSeqId).click();
    }

    public void setPayors(List<AccnPyr> accnPyrs)
    {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoViewIfNeeded(true);", driver.findElement(By.xpath("//span[text()='Insurance Info']")));
        int currentPyrCnt = driver.findElements(By.xpath("//div[@id='payorTabs']/ul/descendant::li")).size();
        int pyrIndex = 0;
        for (AccnPyr accnPyr : accnPyrs)
        {
            if (!accnPyr.getIsJurisdiction())
            {
                if (pyrIndex < currentPyrCnt)
                {
                    // update payor tab
                    driver.findElements(By.xpath("//div[@id='payorTabs']/ul/descendant::li")).get(pyrIndex).click();
                    driver.findElements(By.xpath("//div[@id='payorTabs']/descendant::input[@id='payorAbbrev']")).get(pyrIndex).clear();
                    driver.findElements(By.xpath("//div[@id='payorTabs']/descendant::input[@id='payorAbbrev']")).get(pyrIndex).sendKeys(accnPyr.getPyrAbbrv());
                    driver.findElements(By.xpath("//div[@id='payorTabs']/descendant::input[@id='payorAbbrev']")).get(pyrIndex).sendKeys(Keys.TAB);
                    driver.findElements(By.xpath("//div[@id='payorTabs']/descendant::input[@id='subscriberId']")).get(pyrIndex).clear();
                    driver.findElements(By.xpath("//div[@id='payorTabs']/descendant::input[@id='subscriberId']")).get(pyrIndex).sendKeys(accnPyr.getSubsId());
                    driver.findElements(By.xpath("//div[@id='payorTabs']/descendant::input[@id='subscriberId']")).get(pyrIndex).sendKeys(Keys.TAB);
                }
                else
                {
                    // add new payor
                    driver.findElement(By.xpath("//a[@id='btnAddPayor']")).click();
                    driver.findElement(By.xpath("//input[@id='userAddedPayor']")).sendKeys(accnPyr.getPyrAbbrv());
                    driver.findElement(By.xpath("//button/span[text()='Add']")).click();
                    driver.findElements(By.xpath("//div[@id='payorTabs']/descendant::input[@id='subscriberId']")).get(pyrIndex).clear();
                    driver.findElements(By.xpath("//div[@id='payorTabs']/descendant::input[@id='subscriberId']")).get(pyrIndex).sendKeys(accnPyr.getSubsId());
                    driver.findElements(By.xpath("//div[@id='payorTabs']/descendant::input[@id='subscriberId']")).get(pyrIndex).sendKeys(Keys.TAB);
                }
                pyrIndex++;
            }
        }
        for (int i = pyrIndex; i < currentPyrCnt; i++)
        {
            driver.findElements(By.xpath("//div[@id='payorTabs']/ul/descendant::li")).get(pyrIndex).click();
            driver.findElement(By.xpath("//div[@id='payorTabs']/ul/descendant::li["+(pyrIndex+1)+"]/span")).click();
        }

    }
}