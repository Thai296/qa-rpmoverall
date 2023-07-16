package com.overall.accession.accessionProcessing.accessionHistoryLog;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class AccessionHistoryLog {

    private final RemoteWebDriver driver;
    private final Logger logger;
    private final WebDriverWait wait;

    public AccessionHistoryLog(RemoteWebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
        this.wait = wait;
    }

    /* -------------- Process Queue History -------------- */

    public WebElement accnIDInput() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lookupAccnId")));
    }

    public WebElement accessionHistoryLogPageTitle() {
        return driver.findElement(By.xpath(".//*[@id='accnHistoryForm']/div[1]/div/div/span"));
    }

    public WebElement accessionHistoryLogTitle() {
        return driver.findElement(By.xpath(".//*[@id='accnHistoryForm']/div[2]/div[1]/div/div[1]/div[1]/span"));
    }

    public WebElement processQueueHistoryHeaderSection() {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[2]/section/header/div[1]/span/span"));
    }

    public WebElement processQueueHistoryTbl() {
        return driver.findElement(By.id("tbl_processQueueHistory"));
    }

    public WebElement collapseExpandBtnOnPQHTbl() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_processQueueHistory']/div[1]/a/span"));
    }

    public WebElement queueFilterInput() {
        return driver.findElement(By.id("gs_queue"));
    }

    public WebElement dateInFilterOnPQHTbl() {
        return driver.findElement(By.id("gs_dateIn"));
    }

    public WebElement dateOutFilterOnPQHTbl() {
        return driver.findElement(By.id("gs_dateOut"));
    }

    public WebElement userProcessFilterOnPQHTbl() {
        return driver.findElement(By.id("gs_userProcess"));
    }

    public WebElement commentFilterOnPQHTbl() {
        return driver.findElement(By.id("gs_note"));
    }

    public WebElement pqhTblFollowRow(int row, int col) {
        return driver.findElement(By.xpath(".//*[@id='tbl_processQueueHistory']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement helpIconProcessQueue() {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[2]/section/div/div[1]/a"));
    }

    public void clearTextbox(WebElement e) {
        e.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        e.sendKeys(Keys.BACK_SPACE);
    }

    public void setQueueFilter(String val) throws Exception {
        queueFilterInput().sendKeys(val);
        queueFilterInput().sendKeys(Keys.TAB);
        Thread.sleep(2000);
        logger.info("        Entered " + val + " in Queue filter input field in Process Queue History table.");
    }

    public void setUserProcessFilter(String val) throws Exception {
        userProcessFilterOnPQHTbl().sendKeys(val);
        userProcessFilterOnPQHTbl().sendKeys(Keys.TAB);
        Thread.sleep(2000);
        logger.info("        Entered : " + val + " in User/Process filter input field in Process Queue History table.");
    }

    public WebElement totalRecodeOfPQHTbl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_processQueueHistory_pagernav_right']/div"));
    }

    /* -------------- Correspondence History -------------- */

    public WebElement corresHisTbl() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_correspondenceHistory']/div[3]"));
    }

    public WebElement printIconOfCorresHisTbl() {
        return driver.findElement(By.id("printGrid"));
    }

    public WebElement helpIconOfCorresHisTbl() {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[4]/section/div/div[1]/a"));
    }

    public WebElement collectResponseIconOfCorresHisTbl() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_correspondenceHistory']/div[1]/a/span"));
    }

    public WebElement creationDateFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_creationDate"));
    }

    public WebElement claimNumberFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_claimNumber"));
    }

    public WebElement payorFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_payorAbbrev"));
    }

    public WebElement statementFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_statement"));
    }

    public WebElement submIDFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_submId"));
    }

    public WebElement billTypeFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_billType"));
    }

    public WebElement conditionCodeFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_conditionCode"));
    }

    public WebElement icnFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_icn"));
    }

    public WebElement processedFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_processed"));
    }

    public WebElement dateProcessedFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_processedDate"));
    }

    public WebElement noteFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_note"));
    }

    public WebElement ackFilenameFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_ackFilename"));
    }

    public WebElement ackProcessedDateFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_ackProcessedDate"));
    }

    public WebElement ackValidationIdFilterOfCorresHisTbl() {
        return driver.findElement(By.id("gs_ackValidationId"));
    }

    public WebElement totalRecodeOfCorrespHisTbl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_correspondenceHistory_pagernav_right']/div"));
    }

    public WebElement coresHisTblFolowRow(String row, String col) {
        return driver.findElement(By.xpath(".//*[@id='tbl_correspondenceHistory']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement coresHisTblFolowRowLink(String row, String col) {
        return driver.findElement(By.xpath(".//*[@id='tbl_correspondenceHistory']/tbody/tr[" + row + "]/td[" + col + "]/div/span"));
    }

    public WebElement coresHisTblFolowRowInput(String row, String col) {
        return driver.findElement(By.xpath(".//*[@id='tbl_correspondenceHistory']/tbody/tr[" + row + "]/td[" + col + "]/input"));
    }

    public WebElement stmtLnk() {
        return driver.findElement(By.xpath(".//*[@id=\"tbl_correspondenceHistory\"]/tbody/tr[2]/td[5]/div/span"));
    }

    public WebElement ackFileNameLnk() {
        return driver.findElement(By.xpath(".//*[@id=\"tbl_correspondenceHistory\"]/tbody/tr[2]/td[13]/div[2]/span"));
    }

    /* -------------- Interface Error -------------- */
    public WebElement helpIconInterfaceErrorTbl() {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[5]/section/div/div[1]/a"));
    }

    public WebElement collapseInterfaceErrorTblIcon() {
        return driver.findElement(By.xpath(".//*[@id='gview_tbl_interfaceErrors']/div[1]/a/span"));
    }

    public WebElement interfaceErrorTbl() {
        return driver.findElement(By.id("tbl_interfaceErrors"));
    }

    public WebElement errorDtFilterInterfaceErrorTblInput() {
        return driver.findElement(By.id("gs_errorDate"));
    }

    public WebElement fileNameFilterInterfaceErrorTblInput() {
        return driver.findElement(By.id("gs_filenameAndUser"));
    }

    public WebElement errorTypFilterInterfaceErrorTblInput() {
        return driver.findElement(By.id("gs_errorType"));
    }

    public WebElement errorFilterInterfaceErrorTblInput() {
        return driver.findElement(By.id("gs_error"));
    }

    public WebElement totalRecordsInterfaceErrorTbl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_interfaceErrors_pagernav_right']/div"));
    }

    public WebElement interfaceErrorTblRow(int row, int col) {
        return driver.findElement(By.xpath(".//*[@id='tbl_interfaceErrors']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    //Elements of Error Processing History
    //Search input----------------------------------------------------------------------------------------------------------------------------------------
    public WebElement epDateInput() {
        return driver.findElement(By.id("gs_epDate"));
    }

    public WebElement fixedDateInput() {
        return driver.findElement(By.id("gs_fixedDate"));
    }

    public WebElement errorCodeInput() {
        return driver.findElement(By.id("gs_errorCode"));
    }

    public WebElement testInput() {
        return driver.findElement(By.id("gs_testAbbrev"));
    }

    public WebElement procInput() {
        return driver.findElement(By.id("gs_procId"));
    }

    public WebElement batchIdInput() {
        return driver.findElement(By.id("gs_batchId"));
    }

    public WebElement autoCopyInput() {
        return driver.findElement(By.id("gs_autoCopy"));
    }

    public WebElement autoCopyDateInput() {
        return driver.findElement(By.id("gs_autoCopyDate"));
    }

    public WebElement matchCopyInput() {
        return driver.findElement(By.id("gs_matchCopy"));
    }

    public WebElement matchCopyDateInput() {
        return driver.findElement(By.id("gs_matchCopyDate"));
    }

    public WebElement manualInput() {
        return driver.findElement(By.id("gs_manual"));
    }

    public WebElement manualDateInput() {
        return driver.findElement(By.id("gs_manualDate"));
    }

    public WebElement correspondenceInput() {
        return driver.findElement(By.id("gs_correspondence"));
    }

    public WebElement correspondenceDateInput() {
        return driver.findElement(By.id("gs_correspondenceDate"));
    }

    public WebElement outsideAgencyInput() {
        return driver.findElement(By.id("gs_outsideAgency"));
    }

    public WebElement outsideAgencyDateInput() {
        return driver.findElement(By.id("gs_outsideAgencyDate"));
    }

    public WebElement holdInput() {
        return driver.findElement(By.id("gs_hold"));
    }

    public WebElement holdDateInput() {
        return driver.findElement(By.id("gs_holdDate"));
    }

    public WebElement notesInput() {
        return driver.findElement(By.id("gs_notes"));
    }

    public WebElement totalRecordsErrorProcessHistoryFooterTbl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_errorProcessingHistory_pagernav_right']/div"));
    }

    public WebElement helpIconOnErrorProcessingHistory() {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[3]/section/div/div[1]/a"));
    }

    //Elements of content grid cell ----------------------------------------------------------------------------------------------------------------------------------------
    public WebElement epDateInErrorProcessingHistoryTbl(int rowNum) {
        return driver.findElement(By.xpath("//*[@id='tbl_errorProcessingHistory']/tbody/tr[" + rowNum + "]/td[@aria-describedby='tbl_errorProcessingHistory_epDate']"));
    }

    public WebElement fixedDateGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[3]"));
    }

    public WebElement reasonCodeInErrorProcessingHistoryTbl(String text) {
        return driver.findElement(By.xpath("//*[@id='tbl_errorProcessingHistory']/tbody//td[contains(@title,'" + text + "')]"));
    }

    public WebElement payorIdInErrorProcessingHistoryTbl(String accnPyrAbbrev) {
        return driver.findElement(By.xpath("//*[@id='tbl_errorProcessingHistory']/tbody//td[contains(@title,'" + accnPyrAbbrev + "')]"));
    }

    public WebElement ourtsideagencyPreCorrespInErrorProcessingHistoryTbl(int rowNum) {
        return driver.findElement(By.xpath("//*[@id=\"tbl_errorProcessingHistory\"]/tbody/tr[" + rowNum + "]"));
    }

    public WebElement testGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[5]"));
    }

    public WebElement procGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[6]"));
    }

    public WebElement batchIdGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[7]"));
    }

    public WebElement autoCopyGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[8]"));
    }

    public WebElement autoCopyDateGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[9]"));
    }

    public WebElement matchCopyGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[10]"));
    }

    public WebElement matchCopyDateGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[11]"));
    }

    public WebElement manualGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[12]"));
    }

    public WebElement manualDateGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[13]"));
    }

    public WebElement correspondenceGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[14]"));
    }

    public WebElement correspondenceDateGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[15]"));
    }

    public WebElement outsideAgencyGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[16]"));
    }

    public WebElement outsideAgencyDateGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[17]"));
    }

    public WebElement holdGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[18]"));
    }

    public WebElement holdDateGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[19]"));
    }

    public WebElement notesGridcell(int rowId) {
        return driver.findElement(By.xpath(".//*[@id='" + rowId + "']/td[20]"));
    }

    //Elements of paging section----------------------------------------------------------------------------------------------------------------------------------------
    public WebElement pageInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_errorProcessingHistory_pagernav_center']/table/tbody/tr/td[4]/input"));
    }

    public WebElement errProcessingHistoryTableTotalPageNum() {
        return driver.findElement(By.id("sp_1_tbl_errorProcessingHistory_pagernav"));
    }

    public WebElement nextIcon() {
        return driver.findElement(By.xpath(".//*[@id='next_tbl_errorProcessingHistory_pagernav']/span"));
    }

    public WebElement nextToLastIcon() {
        return driver.findElement(By.xpath(".//*[@id='last_tbl_errorProcessingHistory_pagernav']/span"));
    }

    public WebElement prevIcon() {
        return driver.findElement(By.xpath(".//*[@id='prev_tbl_errorProcessingHistory_pagernav']/span"));
    }

    public WebElement prevToFirstIcon() {
        return driver.findElement(By.xpath(".//*[@id='first_tbl_errorProcessingHistory_pagernav']/span"));
    }

    //Functions of Error Processing History
    // ----------------------------------------------------------------------------------------------------------------------------------------
    public void setEpDateInput(String epDate) throws Exception {
        epDateInput().sendKeys(epDate);
        epDateInput().sendKeys(Keys.TAB);
        Thread.sleep(2000);
        logger.info("        Entered " + epDate + " in EP Date column filter.");
    }

    public void setFixedDateInput(String fixedDateInput) throws Exception {
        fixedDateInput().sendKeys(fixedDateInput);
        fixedDateInput().sendKeys(Keys.TAB);
        Thread.sleep(5000);
        logger.info("        input into fixedDateInput.");
    }

    public void setErrorCodeInput(String errorCodeInput) throws Exception {
        errorCodeInput().sendKeys(errorCodeInput);
        errorCodeInput().sendKeys(Keys.TAB);
        Thread.sleep(5000);
        logger.info("        input into errorCodeInput.");
    }

    public void setTestInput(String testInput) {
        testInput().sendKeys(testInput);
        testInput().sendKeys(Keys.TAB);
        logger.info("        input into testInput.");
    }

    public void setProcInput(String procInput) {
        procInput().sendKeys(procInput);
        procInput().sendKeys(Keys.TAB);
        logger.info("        input into procInput.");
    }

    public void setBatchIdInput(String batchIdInput) {
        batchIdInput().sendKeys(batchIdInput);
        batchIdInput().sendKeys(Keys.TAB);
        logger.info("        input into batchIdInput.");
    }

    public void setAutoCopyInput(String autoCopyInput) {
        autoCopyInput().sendKeys(autoCopyInput);
        autoCopyInput().sendKeys(Keys.TAB);
        logger.info("        input into autoCopyInput.");
    }

    public void setAutoCopyDateInput(String autoCopyDateInput) {
        autoCopyDateInput().sendKeys(autoCopyDateInput);
        autoCopyDateInput().sendKeys(Keys.TAB);
        logger.info("        input into autoCopyDateInput.");
    }

    public void setMatchCopyInput(String matchCopyInput) {
        matchCopyInput().sendKeys(matchCopyInput);
        matchCopyInput().sendKeys(Keys.TAB);
        logger.info("        input into matchCopyInput.");
    }

    public void setMatchCopyDateInput(String matchCopyDateInput) {
        matchCopyDateInput().sendKeys(matchCopyDateInput);
        matchCopyDateInput().sendKeys(Keys.TAB);
        logger.info("        input into matchCopyDateInput.");
    }

    public void setManualInput(String manualInput) {
        manualInput().sendKeys(manualInput);
        manualInput().sendKeys(Keys.TAB);
        logger.info("        input into manualInput.");
    }

    public void setManualDateInput(String manualDateInput) {
        manualDateInput().sendKeys(manualDateInput);
        manualDateInput().sendKeys(Keys.TAB);
        logger.info("        input into manualDateInput.");
    }

    public void setCorrespondenceInput(String correspondenceInput) {
        correspondenceInput().sendKeys(correspondenceInput);
        correspondenceInput().sendKeys(Keys.TAB);
        logger.info("        input into correspondenceInput.");
    }

    public void setCorrespondenceDateInput(String correspondenceDateInput) {
        correspondenceDateInput().sendKeys(correspondenceDateInput);
        correspondenceDateInput().sendKeys(Keys.TAB);
        logger.info("        input into correspondenceDateInput.");
    }

    public void setOutsideAgencyInput(String outsideAgencyInput) {
        outsideAgencyInput().sendKeys(outsideAgencyInput);
        outsideAgencyInput().sendKeys(Keys.TAB);
        logger.info("        input into outsideAgencyInput.");
    }

    public void setOutsideAgencyDateInput(String outsideAgencyDateInput) {
        outsideAgencyDateInput().sendKeys(outsideAgencyDateInput);
        outsideAgencyDateInput().sendKeys(Keys.TAB);
        logger.info("        input into outsideAgencyDateInput.");
    }

    public void setHoldInput(String holdInput) {
        holdInput().sendKeys(holdInput);
        holdInput().sendKeys(Keys.TAB);
        logger.info("        input into holdInput.");
    }

    public void setHoldDateInput(String holdDateInput) {
        holdDateInput().sendKeys(holdDateInput);
        holdDateInput().sendKeys(Keys.TAB);
        logger.info("        input into holdDateInput.");
    }

    public void setNotesInput(String notesInput) {
        notesInput().sendKeys(notesInput);
        notesInput().sendKeys(Keys.TAB);
        logger.info("        input into notesInput.");
    }

    /*---------------Header  --------------------*/
    public WebElement headerTitle() {
        return driver.findElement(By.xpath(".//*[@id='accnHistoryForm']/div[2]/div[1]/div/div[1]/div[1]/span"));
    }

    public WebElement viewDocumentLnk() {
        return driver.findElement(By.xpath(".//*[@id='viewDocumentHeaderInfo']/div[1]/a"));
    }

    public WebElement viewOrgDocumentLnk() {
        return driver.findElement(By.xpath(".//*[@id='viewDocumentHeaderInfo']/div[3]/a"));
    }

    public WebElement facilityAbbrevTextbox() {
        return driver.findElement(By.xpath(".//*[@id='facilityHeaderInfo']/span[1]"));
    }

    public WebElement facilityNameTextbox() {
        return driver.findElement(By.xpath(".//*[@id='facilityHeaderInfo']/span[2]"));
    }

    public WebElement accnSearchButton() {
        return driver.findElement(By.xpath(".//*[@id='accnHistoryForm']/div[2]/div[1]/div/div[2]/div[1]/div[1]/div[1]/div/a/span"));
    }

    public WebElement accnID() {
        return driver.findElement(By.id("accnId"));
    }

    public WebElement clnIDText() {
        return driver.findElement(By.id("clientAbbrev"));
    }

    public WebElement clnNameText() {
        return driver.findElement(By.id("clientName"));
    }

    public WebElement reqIDText() {
        return driver.findElement(By.className("requisitionId"));
    }

    public WebElement ptFullNameText() {
        return driver.findElement(By.className("patientFullName"));
    }

    public WebElement ptLNameText() {
        return driver.findElement(By.xpath(".//*[@id='accnHistoryForm']/div[3]/div[1]/div/div[2]/div[2]/div[2]/div[2]/div"));
    }

    public WebElement dosText() {
        return driver.findElement(By.className("dateOfService"));
    }

    public WebElement finalReportDtText() {
        return driver.findElement(By.className("finalReportDate"));
    }

    public WebElement priceDtText() {
        return driver.findElement(By.className("priceDate"));
    }

    public WebElement stmtStatusText() {
        return driver.findElement(By.className("statementStatus"));
    }

    public WebElement accnStatusText() {
        return driver.findElement(By.xpath(".//*[@id='accnHistoryForm']/div[2]/div[1]/div/div[2]/div[4]/div[2]/div[2]/span"));
    }

    public WebElement origBalText() {
        return driver.findElement(By.className("originalBalance"));
    }

    public WebElement balDueText() {
        return driver.findElement(By.className("balanceDue"));
    }

    /*---------------Footer Menu--------------------*/
    public WebElement pageHelpBtn() {
        return driver.findElement(By.id("pageHelpLink"));
    }

    public WebElement resetButton() {
        return driver.findElement(By.id("Reset"));
    }

    public WebElement errorProcessingHistorySection() {
        return driver.findElement(By.xpath(".//*[@id='mainSections']/div[3]/section/header/div[1]/span"));
    }

    public WebElement errorProcessingHistoryTbl() {
        return driver.findElement(By.id("tbl_processQueueHistory"));
    }

    public WebElement singleFieldQueue(int id) {
        return driver.findElement(By.xpath(".//*[@id='tbl_processQueueHistory']/tbody/tr[@id='" + id + "']/td[2]"));
    }

    public List<WebElement> totalRowOfQueue() {
        return driver.findElements(By.xpath(".//*[@id='tbl_processQueueHistory']/tbody/tr/td[2]"));
    }

    public WebElement nextBtn() {
        return driver.findElement(By.xpath(".//*[@id='next_tbl_processQueueHistory_pagernav']/span"));
    }

    public void clickPageHelpBtn() {
        pageHelpBtn().click();
        logger.info("        Clicked Page Help icon button.");
    }

    public void setAccnIDInput(String val) throws Exception {
        accnIDInput().sendKeys(val);
        accnIDInput().sendKeys(Keys.TAB);
        Thread.sleep(3000);
        logger.info("        Entered Accession ID: " + val + " .");
    }

    public void clickResetBtn() {
        resetButton().click();
        logger.info("        Clicked Reset button.");
    }

    public WebElement nextIconInterfaceErrorTbl() {
        return driver.findElement(By.xpath("//*[@id='next_tbl_interfaceErrors_pagernav']/span"));
    }

    public WebElement errorProcessingTbl() {
        return driver.findElement(By.id("tbl_errorProcessingHistory"));
    }

    public WebElement nextIconCorrespondenceHistoryTbl() {
        return driver.findElement(By.xpath("//*[@id='next_tbl_correspondenceHistory_pagernav']/span"));
    }

    public WebElement nextIconProceessQueueHistoryTbl() {
        return driver.findElement(By.xpath(".//*[@id='next_tbl_processQueueHistory_pagernav']/span"));
    }

    public WebElement nextIconErrorProcessingHistoryTbl() {
        return driver.findElement(By.xpath(".//*[@id='next_tbl_errorProcessingHistory_pagernav']/span"));
    }

    public WebElement totalRecordFooterPQHTbl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_processQueueHistory_pagernav_right']/div"));
    }

    public WebElement accnSearchInLoadPageBtn() {
        return driver.findElement(By.xpath(".//*[@id='accnHistoryForm']/div[2]/section/div/div[2]/div/a/span"));
    }

    public WebElement titleAccnSearchOnSearchPage() {
        return driver.findElement(By.xpath(".//*[@id='accessionSearch']/header/span"));
    }

    public void clickAccnSearchInLoadPageBtn() {
        accnSearchInLoadPageBtn().click();
        logger.info("        Clicked Accession Search icon button.");
    }

    public WebElement accnLink() {
        return driver.findElement(By.xpath(".//*[@id='accessionsearchTable']/tbody/tr[2]/td[2]/a"));
    }

    public String getDataNotNull(List<List<String>> listData, int dataIndex) {
        String temp = listData.get(0).get(dataIndex);
        if (temp == null) {
            while (temp == null) {
                for (List<String> listDatum : listData) {
                    temp = listDatum.get(dataIndex);
                    if (temp != null) {
                        break;
                    }
                }
            }
        }
        return temp;
    }

    public WebElement fileText() {
        return driver.findElement(By.xpath("//*[/html/body/pre/text()]"));
    }

    public WebElement docStoreWebAppsPageTitle() {
        return driver.findElement(By.xpath("/html/head/title"));
    }

    public List<String> openNewTab() {
        driver.findElement(By.cssSelector("Body")).sendKeys(Keys.CONTROL + "t");
        return new ArrayList<>(driver.getWindowHandles());
    }

    public WebElement printCorrsTableRow(int row, int col) {
        return driver.findElement(By.xpath("/html/body/section/div/div/table/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement helpFileText() {
        return driver.findElement(By.xpath("/html/body/h1"));
    }

    public WebElement pageHelpFileText() {
        return driver.findElement(By.xpath("/html/body/h2[1]"));
    }

    public WebElement pageHelpIcon() {
        return driver.findElement(By.xpath("//*[@id=\"accnHistoryForm\"]/div[3]/div[1]/div/div[1]/div[1]/a"));
    }

}

