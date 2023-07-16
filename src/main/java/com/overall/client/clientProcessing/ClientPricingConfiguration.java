package com.overall.client.clientProcessing;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClientPricingConfiguration {
    private RemoteWebDriver driver;
    protected Logger logger;
    private WebDriverWait wait;

    public ClientPricingConfiguration(RemoteWebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
        this.wait=wait;
    }

    /***
     *  Pricing Configuration Load page  - Done
     */
    public WebElement pricingConfigLoadPageTitleTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
    }

    public WebElement loadClientSectionHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_client_pricing_configuration_load_client_id']"));
    }

    public WebElement loadClientSection() {
        return driver.findElement(By.xpath("//*[@id='lookupClientAbbrev']//ancestor::*[contains(@class,'loadClientGroup ')]//parent::div"));
    }

    public WebElement loadClientSectionClientIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientAbbrev")));
    }

    public WebElement loadClientSectionClientSearchIco() {
        return driver.findElement(By.xpath(".//*[@id='client_abbrev_search_btn']/span"));
    }

    /***
     *  Pricing Configuration Detail page
     */
    // Header -> Done
    public WebElement headerHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_client_pricing_configuration_header']"));
    }

    public WebElement headerClientIdTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnAbbrev")));
    }

    public WebElement headerClientNameTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnName")));
    }

    public WebElement headerAccountTypeTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("accountType")));
    }

    public WebElement headerWarningMessageTxt() {
        return driver.findElement(By.id("message"));
    }

    public WebElement headerViewDocumentLnk() {
        return driver.findElement(By.xpath(".//*[@id='viewDocumentHeaderInfo']//*[contains(@class,'viewDocumentContainer')]/a"));
    }

    public WebElement headerViewQa07DocumentLnk() {
        return driver.findElement(By.xpath(".//*[@id='viewDocumentHeaderInfo']//*[contains(@class,'viewOrganizationDocumentContainer')]/a"));
    }

    public WebElement headerFacAbbrevTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='facilityHeaderInfo']//span[contains(@class,'clientOrderingFacAbbrev')]")));
    }

    public WebElement headerFacNameTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='facilityHeaderInfo']//span[contains(@class,'clientOrderingFacName')]")));
    }

    public WebElement headerInfoMessageTxt() {
        return driver.findElement(By.id("message"));
    }

    // Secondary Clients Section -> done
    public WebElement secondarySection() {
        return driver.findElement(By.xpath(".//*[contains(@class,'layoutComponent secondaryClients')]"));
    }

    public WebElement secondaryHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_client_pricing_configuration_secondary_clients']"));
    }
    public WebElement secondaryClnTbl() {
        return driver.findElement(By.id("tbl_sencondayclients"));
    }

    public WebElement secondaryClnTblRow(String row)  {
        return driver.findElement(By.xpath("//*[@id='tbl_sencondayclients']//tr["+row+"]"));
    }

    // Secondary Client filter elements
    public WebElement secondaryClnTblClnIdFilterInput() {
        return driver.findElement(By.id("gs_sdryClnAbbrev"));
    }

    public WebElement secondaryClnTblClnNameFilterInput() {
        return driver.findElement(By.id("gs_sdryClnNm"));
    }

    public WebElement secondaryClnTblEffectiveDateFilterInput() {
        return driver.findElement(By.id("gs_sdryClnEffDt"));
    }

    public WebElement secondaryClnTblExpirationDateFilterInput() {
        return driver.findElement(By.id("gs_sdryClnExpDt"));
    }

    public WebElement secondaryClnTblSuspendThruEOMFilterInput() {
        return driver.findElement(By.id("gs_sdrySuspThruEom"));
    }

    public WebElement secondaryClnTblClnIdInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_sencondayclients']//*[@aria-describedby='tbl_sencondayclients_sdryClnAbbrev']/input"));
    }

    public WebElement secondaryClnTblClnIdTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_sencondayclients']//tr["+row+"]//*[@aria-describedby='tbl_sencondayclients_sdryClnAbbrev']"));
    }

    public WebElement secondaryClnTblClnNameTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_sencondayclients']//tr["+row+"]//*[@aria-describedby='tbl_sencondayclients_sdryClnNm']"));
    }

    public WebElement secondaryClnTblEffDtInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_sencondayclients']//*[@aria-describedby='tbl_sencondayclients_sdryClnEffDt']/input"));
    }

    public WebElement secondaryClnTblEffDtTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_sencondayclients']//tr["+row+"]//*[@aria-describedby='tbl_sencondayclients_sdryClnEffDt']"));
    }

    public WebElement secondaryClnTblExpDtInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_sencondayclients']//*[@aria-describedby='tbl_sencondayclients_sdryClnExpDt']/input"));
    }

    public WebElement secondaryClnTblExpDtTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_sencondayclients']//tr["+row+"]//*[@aria-describedby='tbl_sencondayclients_sdryClnExpDt']"));
    }

    public WebElement secondaryClnTblSuspendThruEOMChk(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_sencondayclients']//tr["+row+"]//*[@aria-describedby='tbl_sencondayclients_sdrySuspThruEom']/input"));
    }

    public WebElement secondaryClnTblDeleteChk(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_sencondayclients']//tr["+row+"]//*[@aria-describedby='tbl_sencondayclients_deleted']/input"));
    }

    public WebElement secondaryClnTblAddBtn() {
        return driver.findElement(By.xpath(".//*[@id='tbl_sencondayclients_iladd']/button"));
    }

    public WebElement secondaryClnTblFirstPageIco()  {
        return driver.findElement(By.id("first_tbl_sencondayclients_pagernav"));
    }

    public WebElement secondaryClnTblPrevPageIco()  {
        return driver.findElement(By.id("prev_tbl_sencondayclients_pagernav"));
    }

    public WebElement secondaryClnTblPageInput()  {
        return driver.findElement(By.xpath("//*[@id='tbl_sencondayclients_pagernav_center']//input[@class='ui-pg-input']"));
    }

    public WebElement secondaryClnTblNextPageIco()  {
        return driver.findElement(By.id("next_tbl_sencondayclients_pagernav"));
    }

    public WebElement secondaryClnTblLastPageIco()  {
        return driver.findElement(By.id("last_tbl_sencondayclients_pagernav"));
    }

    public WebElement secondaryClnTblRowNumSel()  {
        return driver.findElement(By.xpath("//*[@id='tbl_sencondayclients_pagernav_center']//select[@class='ui-pg-selbox']"));
    }

    public WebElement secondaryClnTblTotalRecordTxt()  {
        return driver.findElement(By.xpath("//*[@id='tbl_sencondayclients_pagernav_right']/div"));
    }

    public WebElement secondaryClnTblTotalPageLbl()  {
        return driver.findElement(By.xpath(".//*[contains(@id,'sp') and contains(@id,'tbl_sencondayclients_pagernav')]"));
    }

    // Client Links
    public WebElement clnLinkSectionHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_client_pricing_configuration_client_links']"));
    }

    public WebElement clnLinkSectionParentClientTxt()  {
        return driver.findElement(By.id("parentCln"));
    }

    public WebElement clnLinkSectionSubClnTbl() {
        return driver.findElement(By.id("tbl_subclients"));
    }

    public WebElement clientLinkSectionSubclientsGrid() {
        return driver.findElement(By.xpath("//*[@id='tbl_subclients']//ancestor::div[contains(@class,'subclients')]"));
    }

    public WebElement clnLinkSectionSubClnTblClnIdFilterInput() {
        return driver.findElement(By.id("gs_linkedBClnAbbrev"));
    }

    public WebElement clnLinkSectionSubClnTblRow(String row)  {
        return driver.findElement(By.xpath("//*[@id='tbl_subclients']//tr["+row+"]"));
    }
    public WebElement clnLinkSectionSubClnTblClnIdInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_subclients']//*[@aria-describedby='tbl_subclients_linkedBClnAbbrev']/input"));
    }

    public WebElement clnLinkSectionSubClnTblClnIdTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_subclients']//tr["+row+"]//*[@aria-describedby='tbl_subclients_linkedBClnAbbrev']"));
    }

    public WebElement clnLinkSectionSubClnTblDeletedChk(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_subclients']//tr["+row+"]//*[@aria-describedby='tbl_subclients_deleted']/input"));
    }

    public WebElement clnLinkSectionSubClnTblAddBtn() {
        return driver.findElement(By.xpath(".//*[@id='tbl_subclients_iladd']/button"));
    }

    public WebElement clnLinkSectionSubClnTblFirstPageIco()  {
        return driver.findElement(By.id("first_tbl_subclients_pagernav"));
    }

    public WebElement clnLinkSectionSubClnTblPrevPageIco()  {
        return driver.findElement(By.id("prev_tbl_subclients_pagernav"));
    }

    public WebElement clnLinkSectionSubClnTblPageInput()  {
        return driver.findElement(By.xpath("//*[@id='tbl_subclients_pagernav_center']//input[@class='ui-pg-input']"));
    }

    public WebElement clnLinkSectionSubClnTblNextPageIco()  {
        return driver.findElement(By.id("next_tbl_subclients_pagernav"));
    }

    public WebElement clnLinkSectionSubClnTblLastPageIco()  {
        return driver.findElement(By.id("last_tbl_subclients_pagernav"));
    }

    public WebElement clnLinkSectionSubClnTblRowNumSel()  {
        return driver.findElement(By.xpath("//*[@id='tbl_subclients_pagernav_center']//select[@class='ui-pg-selbox']"));
    }

    public WebElement clnLinkSectionSubClnTblTotalRecordTxt()  {
        return driver.findElement(By.xpath("//*[@id='tbl_subclients_pagernav_right']/div"));
    }

    public WebElement clnLinkSectionSubClnTblTotalPageLbl()  {
        return driver.findElement(By.xpath(".//*[contains(@id,'sp') and contains(@id,'tbl_subclients_pagernav')]"));
    }

    // Pricing Detail Section -> DONE
    public WebElement prcDetailSectionWarningPopupTxt()  {
        return driver.findElement(By.className("xf_message_content"));
    }

    public WebElement prcDetailSectionWarningPopupCloseBtn()  {
        return driver.findElement(By.className("xf_message_close"));
    }

    public WebElement prcDetailHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_client_pricing_configuration_pricing_detail']"));
    }

    public WebElement prcDetailSectionEffDtDdl()  {
        return driver.findElement(By.id("s2id_pdEffDt"));
    }

    public WebElement prcDetailSectionEffDtInput()  {
        return wait.until(ExpectedConditions.elementToBeClickable((By.id("txtPdEffDt"))));
    }

    public WebElement prcDetailSectionRevDtTxt()  {
        return driver.findElement(By.id("revDtDisplay"));
    }

    public WebElement prcDetailSectionPerDiemTypeDdl()  {
        return driver.findElement(By.id("s2id_perDiemTyp"));
    }

    public WebElement prcDetailSectionCarveoutsChk()  {
        return driver.findElement(By.id("carveOutRefLab"));
    }

    public WebElement prcDetailSectionPerDiemAmtInput()  {
        return driver.findElement(By.id("perDiemRate"));
    }

    public WebElement prcDetailSectionIncrementalTblDdl()  {
        return driver.findElement(By.id("s2id_incrId"));
    }

    // Pricing Detail Section -> Client Pricing Group
    public WebElement prcDetailSectionClnPrcGrpSuspendPriceChk()  {
        return driver.findElement(By.id("clnPrcSusp"));
    }

    public WebElement prcDetailSectionClnPrcGrpRetailFSDdl()  {
        return driver.findElement(By.id("s2id_clnRtlFeeSchPrcId"));
    }

    public WebElement prcDetailSectionClnPrcGrpFeeScheduleDdl()  {
        return driver.findElement(By.id("s2id_clnFeeSchPrcId"));
    }

    public WebElement prcDetailSectionClnPrcGrpSpcPrcSrchBtn()  {
        return driver.findElement(By.id("lookupClnSpcPrcSrchBtn"));
    }

    public WebElement prcDetailSectionClnPrcGrpSpcPrcTblInput()  {
        return driver.findElement(By.id("clnSpcPrc"));
    }

    public WebElement prcDetailSectionClnPrcGrpBillExpectPriceBtn()  {
        return driver.findElement(By.id("lookupClExpPrcTblSrchBtn"));
    }

    public WebElement prcDetailSectionClnPrcGrpBillExpectPriceInput()  {
        return driver.findElement(By.id("clnExpPrcAbv"));
    }

    public WebElement prcDetailSectionClnPrcGrpDiscountInput()  {
        return driver.findElement(By.id("clnFeeSchPctDisc"));
    }

    public WebElement prcDetailSectionClnPrcGrpTradeDiscountInput()  {
        return driver.findElement(By.id("clnTradeDisc"));
    }

    public WebElement prcDetailSectionClnPrcGrpTaxInput()  {
        return driver.findElement(By.id("clTaxPct"));
    }

    public WebElement prcDetailSectionClnPrcGrpClnSpcPrcTblLnk() {
        return driver.findElement(By.id("clnSpcPrcTbl"));
    }

    public WebElement prcDetailSectionClnPrcGrpClnRetailFSLnk() {
        return driver.findElement(By.id("clnRetailFS"));
    }

    public WebElement prcDetailSectionClnPrcGrpClnFeeScheduleLnk() {
        return driver.findElement(By.id("clnFeeSchedule"));
    }

    public WebElement prcDetailSectionClnPrcGrpClnUsgClnSpcPrcTblLnk() {
        return driver.findElement(By.id("clnUsgClnSpcPrcTbl"));
    }

    public WebElement prcDetailSectionClnPrcGrpClnUsgClnRetailFSLnk() {
        return driver.findElement(By.id("clnUsgClnRetailFS"));
    }

    public WebElement prcDetailSectionClnPrcGrpClnUsgClnFeeScheduleLnk() {
        return driver.findElement(By.id("clnUsgClnFeeSchedule"));
    }

    // Pricing Detail Section -> Non-Client Pricing Group
    public WebElement prcDetailSectionNonClnPrcGrpSuspendPriceChk()  {
        return driver.findElement(By.id("nonclnPrcSusp"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpRetailFSDdl()  {
        return driver.findElement(By.id("s2id_nonClnRtlFeeSchPrcId"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpFeeScheduleDdl()  {
        return driver.findElement(By.id("s2id_nonClnFeeSchPrcId"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpSpcPrcSrchBtn()  {
        return driver.findElement(By.id("lookupNonclnSpcPrcSrchBtn"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpSpcPrcTblInput()  {
        return driver.findElement(By.id("nonclnSpcPrc"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpDiscountInput()  {
        return driver.findElement(By.id("nonClnFeeSchPctDisc"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpTaxInput()  {
        return driver.findElement(By.id("nonClTaxPct"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpNonClnSpcPrcTblLnk() {
        return driver.findElement(By.id("nonClnSpcPrcTbl"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpNonClnRetailFSLnk() {
        return driver.findElement(By.id("nonClnRetailFS"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpNonClnFeeScheduleLnk() {
        return driver.findElement(By.id("nonClnFeeSchedule"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpClnUsgNonClnSpcPrcTblLnk() {
        return driver.findElement(By.id("clnUsgNonClnSpcPrcTbl"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpClnUsgNonClnRetailFSLnk() {
        return driver.findElement(By.id("clnUsgNonClnRetailFS"));
    }

    public WebElement prcDetailSectionNonClnPrcGrpClnUsgNonClnFeeScheduleLnk() {
        return driver.findElement(By.id("clnUsgNonClnFeeSchedule"));
    }

    // Pricing Detail Section -> Client Maximum Discount Group
    public WebElement prcDetailSectionClnMaxDisGrpLvl0DiscInput()  {
        return driver.findElement(By.id("lvl0Disc"));
    }

    public WebElement prcDetailSectionClnMaxDisGrpLvl1DiscInput()  {
        return driver.findElement(By.id("lvl1Disc"));
    }

    public WebElement prcDetailSectionClnMaxDisGrpLvl2DiscInput()  {
        return driver.findElement(By.id("lvl2Disc"));
    }

    public WebElement prcDetailSectionClnMaxDisGrpLvl3DiscInput()  {
        return driver.findElement(By.id("lvl3Disc"));
    }

    public WebElement prcDetailSectionClnMaxDisGrpLvl4DiscInput()  {
        return driver.findElement(By.id("lvl4Disc"));
    }

    public WebElement prcDetailSectionClnMaxDisGrpLvl5DiscInput()  {
        return driver.findElement(By.id("lvl5Disc"));
    }

    public WebElement prcDetailSectionClnMaxDisGrpLvl6DiscInput()  {
        return driver.findElement(By.id("lvl6Disc"));
    }

    public WebElement prcDetailSectionClnMaxDisGrpLvl7DiscInput()  {
        return driver.findElement(By.id("lvl7Disc"));
    }

    public WebElement prcDetailSectionClnMaxDisGrpLvl8DiscInput()  {
        return driver.findElement(By.id("lvl8Disc"));
    }

    public WebElement prcDetailSectionClnMaxDisGrpLvl9DiscInput()  {
        return driver.findElement(By.id("lvl9Disc"));
    }

    // Recurring Charges Section
    public WebElement recChgHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_client_pricing_configuration_recurring_charges']"));
    }

    public WebElement recurringChargesMessageInfoTxt() {
        return driver.findElement(By.xpath(".//*[contains(@class,'recChrgMessageInfo')]"));
    }

    // Recurring Charges on Statement Table
    public WebElement recurringChargesOnStatementTbl() {
        return driver.findElement(By.id("tbl_recurringChargesOnStatement"));
    }

    public WebElement recurringChargesOnStatementTblEffectiveDateFilterInput() {
        return driver.findElement(By.id("gs_recChgEffDt"));
    }

    public WebElement recurringChargesOnStatementTblExpireDateFilterInput() {
        return driver.findElement(By.id("gs_recChgExpDt"));
    }

    public WebElement recurringChargesOnStatementTblAdjustmentCodeFilterInput() {
        return driver.findElement(By.id("gs_recChgAdjCd"));
    }

    public WebElement recurringChargesOnStatementTblChargeFilterInput() {
        return driver.findElement(By.id("gs_recChg"));
    }

    public WebElement recurringChargesOnStatementTblNotesFilterInput() {
        return driver.findElement(By.id("gs_recChgCmnt"));
    }

    public WebElement recurringChargesOnStatementTblBillFreqFilterInput() {
        return driver.findElement(By.id("gs_recChgBillFreq"));
    }

    public WebElement recurringChargesOnStatementTblRow(String row)  {
        return driver.findElement(By.xpath("//*[@id='tbl_recurringChargesOnStatement']//tr["+row+"]"));
    }
    public WebElement recurringChargesOnStatementTblEffectiveDateInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//*[@aria-describedby='tbl_recurringChargesOnStatement_recChgEffDt']/input"));
    }

    public WebElement recurringChargesOnStatementTblEffectiveDateTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnStatement_recChgEffDt']"));
    }

    public WebElement recurringChargesOnStatementTblExpireDateInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//*[@aria-describedby='tbl_recurringChargesOnStatement_recChgExpDt']/input"));
    }

    public WebElement recurringChargesOnStatementTblExpireDateTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnStatement_recChgExpDt']"));
    }

    public WebElement recurringChargesOnStatementTblAdjustmentCodeDdl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//*[@aria-describedby='tbl_recurringChargesOnStatement_recChgAdjCd']//div[contains(@id,'s2id')]"));
    }

    public WebElement recurringChargesOnStatementTblAdjustmentCodeTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnStatement_recChgAdjCd']"));
    }

    public WebElement recurringChargesOnStatementTblChargeInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//*[@aria-describedby='tbl_recurringChargesOnStatement_recChg']/input"));
    }

    public WebElement recurringChargesOnStatementTblChargeTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnStatement_recChg']"));
    }

    public WebElement recurringChargesOnStatementTblNotesInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//*[@aria-describedby='tbl_recurringChargesOnStatement_recChgCmnt']/input"));
    }

    public WebElement recurringChargesOnStatementTblNotesTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnStatement_recChgCmnt']"));
    }

    public WebElement recurringChargesOnStatementTblBillFreqDdl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//*[@aria-describedby='tbl_recurringChargesOnStatement_recChgBillFreq']//div[contains(@id,'s2id')]"));
    }

    public WebElement recurringChargesOnStatementTblBillFreqTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnStatement_recChgBillFreq']"));
    }

    public WebElement recurringChargesOnStatementTblDeletedChk(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnStatement_deleted']/input"));
    }

    public WebElement recurringChargesOnStatementTblAddBtn() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnStatement_iladd']/button"));
    }

    public WebElement recurringChargesOnStatementTblFirstPageIco()  {
        return driver.findElement(By.id("first_tbl_recurringChargesOnStatement_pagernav"));
    }

    public WebElement recurringChargesOnStatementTblPrevPageIco()  {
        return driver.findElement(By.id("prev_tbl_recurringChargesOnStatement_pagernav"));
    }

    public WebElement recurringChargesOnStatementTblPageInput()  {
        return driver.findElement(By.xpath("//*[@id='tbl_recurringChargesOnStatement_pagernav_center']//input[@class='ui-pg-input']"));
    }

    public WebElement recurringChargesOnStatementTblNextPageIco()  {
        return driver.findElement(By.id("next_tbl_recurringChargesOnStatement_pagernav"));
    }

    public WebElement recurringChargesOnStatementTblLastPageIco()  {
        return driver.findElement(By.id("last_tbl_recurringChargesOnStatement_pagernav"));
    }

    public WebElement recurringChargesOnStatementTblRowNumSel()  {
        return driver.findElement(By.xpath("//*[@id='tbl_recurringChargesOnStatement_pagernav_center']//select[@class='ui-pg-selbox']"));
    }

    public WebElement recurringChargesOnStatementTblTotalRecordTxt()  {
        return driver.findElement(By.xpath("//*[@id='tbl_recurringChargesOnStatement_pagernav_right']/div"));
    }

    public WebElement recurringChargesOnStatementTblTotalPageLbl()  {
        return driver.findElement(By.xpath(".//*[contains(@id,'sp') and contains(@id,'tbl_recurringChargesOnStatement_pagernav')]"));
    }

    // Recurring Charges on Accession Table
    public WebElement recurringChargesOnAccessionTbl() {
        return driver.findElement(By.id("tbl_recurringChargesOnAccession"));
    }

    public WebElement recurringChargesOnAccessionTblEffectiveDateFilterInput() {
        return driver.findElement(By.id("gs_recChgAccnEffDt"));
    }

    public WebElement recurringChargesOnAccessionTblExpireDateFilterInput() {
        return driver.findElement(By.id("gs_recChgAccnExpDt"));
    }

    public WebElement recurringChargesOnAccessionTblTestIDFilterInput() {
        return driver.findElement(By.id("gs_recChgAccnTestId"));
    }

    public WebElement recurringChargesOnAccessionTblRow(String row)  {
        return driver.findElement(By.xpath("//*[@id='tbl_recurringChargesOnAccession']//tr["+row+"]"));
    }

    public WebElement recurringChargesOnAccessionTblEffectiveDateInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnAccession']//*[@aria-describedby='tbl_recurringChargesOnAccession_recChgAccnEffDt']/input"));
    }

    public WebElement recurringChargesOnAccessionTblEffectiveDateTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnAccession']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnAccession_recChgAccnEffDt']"));
    }

    public WebElement recurringChargesOnAccessionTblExpireDateInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnAccession']//*[@aria-describedby='tbl_recurringChargesOnAccession_recChgAccnExpDt']/input"));
    }

    public WebElement recurringChargesOnAccessionTblExpireDateTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnAccession']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnAccession_recChgAccnExpDt']"));
    }

    public WebElement recurringChargesOnAccessionTblTestIdBtn() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnAccession']//*[@aria-describedby='tbl_recurringChargesOnAccession_recChgAccnTestId']/div"));
    }

    public WebElement recurringChargesOnAccessionTblTestIdInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnAccession']//*[@aria-describedby='tbl_recurringChargesOnAccession_recChgAccnTestId']/input"));
    }

    public WebElement recurringChargesOnAccessionTblTestIdTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnAccession']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnAccession_recChgAccnTestId']"));
    }

    public WebElement recurringChargesOnAccessionTblDeletedChk(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnAccession']//tr["+row+"]//*[@aria-describedby='tbl_recurringChargesOnAccession_deleted']/input"));
    }

    public WebElement recurringChargesOnAccessionTblAddBtn() {
        return driver.findElement(By.xpath(".//*[@id='tbl_recurringChargesOnAccession_iladd']/button"));
    }

    public WebElement recurringChargesOnAccessionTblFirstPageIco()  {
        return driver.findElement(By.id("first_tbl_recurringChargesOnAccession_pagernav"));
    }

    public WebElement recurringChargesOnAccessionTblPrevPageIco()  {
        return driver.findElement(By.id("prev_tbl_recurringChargesOnAccession_pagernav"));
    }

    public WebElement recurringChargesOnAccessionTblPageInput()  {
        return driver.findElement(By.xpath("//*[@id='tbl_recurringChargesOnAccession_pagernav_center']//input[@class='ui-pg-input']"));
    }

    public WebElement recurringChargesOnAccessionTblNextPageIco()  {
        return driver.findElement(By.id("next_tbl_recurringChargesOnAccession_pagernav"));
    }

    public WebElement recurringChargesOnAccessionTblLastPageIco()  {
        return driver.findElement(By.id("last_tbl_recurringChargesOnAccession_pagernav"));
    }

    public WebElement recurringChargesOnAccessionTblRowNumSel()  {
        return driver.findElement(By.xpath("//*[@id='tbl_recurringChargesOnAccession_pagernav_center']//select[@class='ui-pg-selbox']"));
    }

    public WebElement recurringChargesOnAccessionTblTotalRecordTxt()  {
        return driver.findElement(By.xpath("//*[@id='tbl_recurringChargesOnAccession_pagernav_right']/div"));
    }

    public WebElement recurringChargesOnAccessionTblTotalPageLbl()  {
        return driver.findElement(By.xpath(".//*[contains(@id,'sp') and contains(@id,'tbl_recurringChargesOnAccession_pagernav')]"));
    }

    // Purchase Order Section -> DONE
    public WebElement purchaseOrderHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_client_pricing_configuration_purchase_order']"));
    }

    public WebElement purchaseOrderTbl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder']"));
    }

    public WebElement purchaseOrderTblPoNumberFilterInput() {
        return driver.findElement(By.xpath(".//*[@id='gs_poNumber']"));
    }

    public WebElement purchaseOrderTblIssueDateFilterInput() {
        return driver.findElement(By.xpath(".//*[@id='gs_poIssueDt']"));
    }

    public WebElement purchaseOrderTblExpirationDateFilterInput() {
        return driver.findElement(By.xpath(".//*[@id='gs_poExpDt']"));
    }

    public WebElement purchaseOrderTblPoAmountFilterInput() {
        return driver.findElement(By.xpath(".//*[@id='gs_poAmt']"));
    }

    public WebElement purchaseOrderTblRow(String row)  {
        return driver.findElement(By.xpath("//*[@id='tbl_purchaseOrder']//tr["+row+"]"));
    }

    public WebElement purchaseOrderTblPoNumberInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder']//*[@aria-describedby='tbl_purchaseOrder_poNumber']/input"));
    }

    public WebElement purchaseOrderTblPoNumberTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder']//tr["+row+"]//*[@aria-describedby='tbl_purchaseOrder_poNumber']"));
    }

    public WebElement purchaseOrderTblIssueDateInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder']//*[@aria-describedby='tbl_purchaseOrder_poIssueDt']/input"));
    }

    public WebElement purchaseOrderTblIssueDateTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder']//tr["+row+"]//*[@aria-describedby='tbl_purchaseOrder_poIssueDt']"));
    }

    public WebElement purchaseOrderTblExpirationDateInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder']//*[@aria-describedby='tbl_purchaseOrder_poExpDt']/input"));
    }

    public WebElement purchaseOrderTblExpirationDateTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder']//tr["+row+"]//*[@aria-describedby='tbl_purchaseOrder_poExpDt']"));
    }

    public WebElement purchaseOrderTblPoAmountInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder']//*[@aria-describedby='tbl_purchaseOrder_poAmt']/input"));
    }

    public WebElement purchaseOrderTblPoAmountTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder']//tr["+row+"]//*[@aria-describedby='tbl_purchaseOrder_poAmt']"));
    }

    public WebElement purchaseOrderTblDeletedChk(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder']//tr["+row+"]//*[@aria-describedby='tbl_purchaseOrder_deleted']/input"));
    }

    public WebElement purchaseOrderTblAddBtn() {
        return driver.findElement(By.xpath(".//*[@id='tbl_purchaseOrder_iladd']/button"));
    }

    public WebElement purchaseOrderTblFirstPageIco()  {
        return driver.findElement(By.id("first_tbl_purchaseOrder_pagernav"));
    }

    public WebElement purchaseOrderTblPrevPageIco()  {
        return driver.findElement(By.id("prev_tbl_purchaseOrder_pagernav"));
    }

    public WebElement purchaseOrderTblPageInput()  {
        return driver.findElement(By.xpath("//*[@id='tbl_purchaseOrder_pagernav_center']//input[@class='ui-pg-input']"));
    }

    public WebElement purchaseOrderTblNextPageIco()  {
        return driver.findElement(By.id("next_tbl_purchaseOrder_pagernav"));
    }

    public WebElement purchaseOrderTblLastPageIco()  {
        return driver.findElement(By.id("last_tbl_purchaseOrder_pagernav"));
    }

    public WebElement purchaseOrderTblRowNumSel()  {
        return driver.findElement(By.xpath("//*[@id='tbl_purchaseOrder_pagernav_center']//select[@class='ui-pg-selbox']"));
    }

    public WebElement purchaseOrderTblTotalRecordTxt()  {
        return driver.findElement(By.xpath("//*[@id='tbl_purchaseOrder_pagernav_right']/div"));
    }

    public WebElement purchaseOrderTblTotalPageLbl()  {
        return driver.findElement(By.xpath(".//*[contains(@id,'sp') and contains(@id,'tbl_purchaseOrder_pagernav')]"));
    }

    // Miscellaneous Section -> DONE
    public WebElement miscellaneousSectionHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_client_pricing_configuration_miscellaneous']"));
    }

    public WebElement miscellaneousSectionBillReferralsToClientChk()  {
        return driver.findElement(By.id("billReferralsToClient"));
    }

    public WebElement miscellaneousSectionPriceReferralsAtCostChk()  {
        return driver.findElement(By.id("priceReferralsAtCost"));
    }

    public WebElement miscellaneousSectionPriceReferralsAtCostInput()  {
        return driver.findElement(By.id("prcRefAddedCost"));
    }

    public WebElement miscellaneousSectionReferralHandlingTestIdBtn()  {
        return driver.findElement(By.xpath("//*[@data-search-target-fields='#prcRefTestId']"));
    }

    public WebElement miscellaneousSectionReferralHandlingTestIdInput()  {
        return driver.findElement(By.id("prcRefTestId"));
    }

    public WebElement miscellaneousSectionHandFeeMinInput()  {
        return driver.findElement(By.id("handFeeMin"));
    }

    public WebElement miscellaneousSectionHandFeeMaxInput()  {
        return driver.findElement(By.id("handFeeMax"));
    }

    public WebElement miscellaneousSectionHandFeePercentOfFeeInput()  {
        return driver.findElement(By.id("handFeePct"));
    }

    public WebElement miscellaneousSectionDirectBillingExemptChk()  {
        return driver.findElement(By.id("directBillingExempt"));
    }

    public WebElement miscellaneousSectionSuspendPricingThroughEOMChk()  {
        return driver.findElement(By.id("suspPrcngThruEom"));
    }

    public WebElement miscellaneousSectionPrntCompOnWelcomeLtrsChk()  {
        return driver.findElement(By.id("prntCompOnWelcomeLtrs"));
    }

    public WebElement miscellaneousSectionHospitalAdmitRuleChk()  {
        return driver.findElement(By.id("hospitalAdmitRule"));
    }

    public WebElement miscellaneousSectionAutoTransferCreditChk()  {
        return driver.findElement(By.id("autoTransferCredit"));
    }

    public WebElement miscellaneousSectionOverridePyrBtn()  {
        return driver.findElement(By.xpath("//*[@data-search-target-fields='#hl7OverridePyr']"));
    }

    public WebElement miscellaneousSectionOverridePyrInput()  {
        return driver.findElement(By.id("hl7OverridePyr"));
    }

    // Client Billing Categories Section -> DONE
    public WebElement clnBillCatSectionHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_client_pricing_configuration_client_billing_categories']"));
    }

    public WebElement clientBillingCategoriesTbl() {
        return driver.findElement(By.id("tbl_clientBillingCategories"));
    }

    public WebElement clientBillingCategoriesTblEffDtFilterInput() {
        return driver.findElement(By.id("gs_cbcEffDt"));
    }

    public WebElement clientBillingCategoriesTblExpDtFilterInput() {
        return driver.findElement(By.id("gs_cbcExpDt"));
    }

    public WebElement clientBillingCategoriesTblClnBillCatFilterInput() {
        return driver.findElement(By.id("gs_clnBillCategory"));
    }

    public WebElement clientBillingCategoriesTblPricingTypeFilterInput() {
        return driver.findElement(By.id("gs_cbcPrcTyp"));
    }

    public WebElement clientBillingCategoriesTblCapRateFilterInput() {
        return driver.findElement(By.id("gs_cbcCapRate"));
    }

    public WebElement clientBillingCategoriesTblCarveOutRefTestsFilterInput() {
        return driver.findElement(By.id("gs_hCbcCarveOutRefTests"));
    }

    public WebElement clientBillingCategoriesTblCarveOutFSFilterInput() {
        return driver.findElement(By.id("gs_cbcCarveOutFS"));
    }

    public WebElement clientBillingCategoriesTblFeeScheduleFilterInput() {
        return driver.findElement(By.id("gs_cbcFS"));
    }

    public WebElement clientBillingCategoriesTblDiscountFilterInput() {
        return driver.findElement(By.id("gs_discount"));
    }

    public WebElement clientBillingCategoriesTblRow(String row)  {
        return driver.findElement(By.xpath("//*[@id='tbl_clientBillingCategories']//tr["+row+"]"));
    }

    public WebElement clientBillingCategoriesTblEffDtInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//*[@aria-describedby='tbl_clientBillingCategories_cbcEffDt']/input"));
    }

    public WebElement clientBillingCategoriesTblEffDtTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//tr["+row+"]//*[@aria-describedby='tbl_clientBillingCategories_cbcEffDt']"));
    }

    public WebElement clientBillingCategoriesTblExpDtInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//*[@aria-describedby='tbl_clientBillingCategories_cbcExpDt']/input"));
    }

    public WebElement clientBillingCategoriesTblExpDtTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//tr["+row+"]//*[@aria-describedby='tbl_clientBillingCategories_cbcExpDt']"));
    }

    public WebElement clientBillingCategoriesTblClnBillCatDdl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//*[@aria-describedby='tbl_clientBillingCategories_clnBillCategory']//div[contains(@id,'s2id')]"));
    }

    public WebElement clientBillingCategoriesTblClnBillCatTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//tr["+row+"]//*[@aria-describedby='tbl_clientBillingCategories_clnBillCategory']"));
    }

    public WebElement clientBillingCategoriesTblPricingTypeDdl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//*[@aria-describedby='tbl_clientBillingCategories_cbcPrcTyp']//div[contains(@id,'s2id')]"));
    }

    public WebElement clientBillingCategoriesTblPricingTypeTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//tr["+row+"]//*[@aria-describedby='tbl_clientBillingCategories_cbcPrcTyp']"));
    }

    public WebElement clientBillingCategoriesTblCapRateInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//*[@aria-describedby='tbl_clientBillingCategories_cbcCapRate']/input"));
    }

    public WebElement clientBillingCategoriesTblCapRateTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//tr["+row+"]//*[@aria-describedby='tbl_clientBillingCategories_cbcCapRate']"));
    }

    public WebElement clientBillingCategoriesTblCarveOutRefTestsChk(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//tr["+row+"]//*[@aria-describedby='tbl_clientBillingCategories_hCbcCarveOutRefTests']/input"));
    }

    public WebElement clientBillingCategoriesTblCarveOutFSDdl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//*[@aria-describedby='tbl_clientBillingCategories_cbcCarveOutFS']//div[contains(@id,'s2id')]"));
    }

    public WebElement clientBillingCategoriesTblCarveOutFSTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//tr["+row+"]//*[@aria-describedby='tbl_clientBillingCategories_cbcCarveOutFS']"));
    }

    public WebElement clientBillingCategoriesTblFeeScheduleTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//tr["+row+"]//*[@aria-describedby='tbl_clientBillingCategories_cbcFS']"));
    }

    public WebElement clientBillingCategoriesTblFeeScheduleDdl() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//*[@aria-describedby='tbl_clientBillingCategories_cbcFS']//div[contains(@id,'s2id')]"));
    }

    public WebElement clientBillingCategoriesTblDiscountInput() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//*[@aria-describedby='tbl_clientBillingCategories_discount']/input"));
    }

    public WebElement clientBillingCategoriesTblDiscountTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories']//tr["+row+"]//*[@aria-describedby='tbl_clientBillingCategories_discount']"));
    }

    public WebElement clientBillingCategoriesTblAddBtn() {
        return driver.findElement(By.xpath(".//*[@id='tbl_clientBillingCategories_iladd']/button"));
    }

    public WebElement clientBillingCategoriesTblFirstPageIco()  {
        return driver.findElement(By.id("first_tbl_clientBillingCategories_pagernav"));
    }

    public WebElement clientBillingCategoriesTblPrevPageIco()  {
        return driver.findElement(By.id("prev_tbl_clientBillingCategories_pagernav"));
    }

    public WebElement clientBillingCategoriesTblPageInput()  {
        return driver.findElement(By.xpath("//*[@id='tbl_clientBillingCategories_pagernav_center']//input[@class='ui-pg-input']"));
    }

    public WebElement clientBillingCategoriesTblNextPageIco()  {
        return driver.findElement(By.id("next_tbl_clientBillingCategories_pagernav"));
    }

    public WebElement clientBillingCategoriesTblLastPageIco()  {
        return driver.findElement(By.id("last_tbl_clientBillingCategories_pagernav"));
    }

    public WebElement clientBillingCategoriesTblRowNumSel()  {
        return driver.findElement(By.xpath("//*[@id='tbl_clientBillingCategories_pagernav_center']//select[@class='ui-pg-selbox']"));
    }

    public WebElement clientBillingCategoriesTblTotalRecordTxt()  {
        return driver.findElement(By.xpath("//*[@id='tbl_clientBillingCategories_pagernav_right']/div"));
    }

    public WebElement clientBillingCategoriesTblTotalPageLbl()  {
        return driver.findElement(By.xpath(".//*[contains(@id,'sp') and contains(@id,'tbl_clientBillingCategories_pagernav')]"));
    }

    // Retroactive Price Impact Section - Done
    public WebElement retroactivePrcImpactTbl() {
        return driver.findElement(By.id("tbl_priceTableRetro"));
    }

    public WebElement retroactivePrcImpactTblRow(String row)  {
        return driver.findElement(By.xpath("//*[@id='tbl_priceTableRetro']//tr["+row+"]"));
    }

    public WebElement retroactivePrcImpactTblClnIdTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_priceTableRetro']//tr["+row+"]//*[@aria-describedby='tbl_priceTableRetro_clnAbbrev']"));
    }

    public WebElement retroactivePrcImpactTblClnNameTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_priceTableRetro']//tr["+row+"]//*[@aria-describedby='tbl_priceTableRetro_clnName']"));
    }

    public WebElement retroactivePrcImpactTblInvoiceDtTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_priceTableRetro']//tr["+row+"]//*[@aria-describedby='tbl_priceTableRetro_submDt']"));
    }

    public WebElement retroactivePrcImpactTblBegAmtTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_priceTableRetro']//tr["+row+"]//*[@aria-describedby='tbl_priceTableRetro_origAmt']"));
    }

    public WebElement retroactivePrcImpactTblRetroAmtTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_priceTableRetro']//tr["+row+"]//*[@aria-describedby='tbl_priceTableRetro_retroAmt']"));
    }

    public WebElement retroactivePrcImpactTblEndPrcTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_priceTableRetro']//tr["+row+"]//*[@aria-describedby='tbl_priceTableRetro_newPrice']"));
    }

    public WebElement retroactivePrcImpactTblTotalAdjTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_priceTableRetro']//tr["+row+"]//*[@aria-describedby='tbl_priceTableRetro_totalAdj']"));
    }

    public WebElement retroactivePrcImpactTblCommentTxt(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_priceTableRetro']//tr["+row+"]//*[@aria-describedby='tbl_priceTableRetro_comment']"));
    }

    public WebElement retroactivePrcImpactTblFirstPageIco()  {
        return driver.findElement(By.id("first_tbl_priceTableRetro_pagernav"));
    }

    public WebElement retroactivePrcImpactTblPrevPageIco()  {
        return driver.findElement(By.id("prev_tbl_priceTableRetro_pagernav"));
    }

    public WebElement retroactivePrcImpactTblPageInput()  {
        return driver.findElement(By.xpath("//*[@id='tbl_priceTableRetro_pagernav_center']//input[@class='ui-pg-input']"));
    }

    public WebElement retroactivePrcImpactTblNextPageIco()  {
        return driver.findElement(By.id("next_tbl_priceTableRetro_pagernav"));
    }

    public WebElement retroactivePrcImpactTblLastPageIco()  {
        return driver.findElement(By.id("last_tbl_priceTableRetro_pagernav"));
    }

    public WebElement retroactivePrcImpactTblRowNumSel()  {
        return driver.findElement(By.xpath("//*[@id='tbl_priceTableRetro_pagernav_center']//select[@class='ui-pg-selbox']"));
    }

    public WebElement retroactivePrcImpactTblTotalRecordTxt()  {
        return driver.findElement(By.xpath("//*[@id='tbl_priceTableRetro_pagernav_right']/div"));
    }

    public WebElement retroactivePrcImpactTblTotalPageTxt()  {
        return driver.findElement(By.xpath(".//*[contains(@id,'sp') and contains(@id,'tbl_priceTableRetro_pagernav')]"));
    }

    public WebElement retroactivePrcImpactTbltotalRetroTxt()  {
        return driver.findElement(By.xpath(".//*[contains(@class,'totalRetro')]"));
    }

    public List<WebElement> retroactivePrcImpactTblAllRow() {
        return driver.findElements(By.xpath("//*[@id='tbl_priceTableRetro']//tr[@tabindex]"));
    }

    public WebElement retroactivePrcImpactTotalEstimatedImpactTxt()  {
        return driver.findElement(By.xpath(".//*[@data-help-id='total_retro']"));
    }

    // Footer -> DONE
    public WebElement footerHelpIco() {
        return driver.findElement(By.id("pageHelpLink"));
    }

    public WebElement footerClipboardToolbarIco() {
        return driver.findElement(By.xpath(".//*[contains(@title,'Show Clipboard')]"));
    }

    public WebElement footerKeyboardIco() {
        return driver.findElement(By.xpath(".//*[contains(@title,'Show Keyboard Shortcuts')]"));
    }

    public WebElement footerSectionSearchFieldInput() {
        return driver.findElement(By.id("sectionSearchField"));
    }

    public WebElement footerResetBtn()  {
        return driver.findElement(By.id("Reset"));
    }

    public WebElement footerSaveBtn()  {
        return driver.findElement(By.id("btnSave"));
    }

    public WebElement footerSaveAndClearBtn()  {
        return driver.findElement(By.id("btnSaveAndClear"));
    }

    public WebElement warningPopup() {
        return driver.findElement(By.xpath("//*[@aria-describedby='confirmationDialog']"));
    }

    public WebElement warningPopupWarningTitleTxt() {
        return driver.findElement(By.xpath("//*[@aria-describedby='confirmationDialog']//span[@class='ui-dialog-title']"));
    }

    public WebElement warningPopupWarningTxt() {
        return driver.findElement(By.xpath("//*[@id='confirmationDialog']//span"));
    }

    public WebElement warningPopupResetBtn()  {
        return driver.findElement(By.xpath(".//*[@aria-describedby='confirmationDialog']//span[text()='Reset']/parent::button"));
    }

    public WebElement warningPopupCancelBtn()  {
        return driver.findElement(By.xpath(".//*[@aria-describedby='confirmationDialog']//span[text()='Cancel']/parent::button"));
    }

    public WebElement fieldValidationMessageTxt() {
        return driver.findElement(By.xpath("//*[contains(@id,'messagefor')]//div[text()='Field Validation']/ancestor::div//div[contains(@class,'xf_message_content')]"));
    }

    public WebElement fieldValidationCloseIco() {
        return driver.findElement(By.xpath("//*[contains(@id,'messagefor')]//*[contains(@class,'xf_message_close')]"));
    }

    public WebElement fieldValidationBlock() {
        return driver.findElement(By.xpath("//*[contains(@id,'messagefor')]"));
    }
    public WebElement editPdEffDt(){
        return driver.findElement(By.xpath("//*[@id=\"editPdEffDt\"]/span"));
    }
}
