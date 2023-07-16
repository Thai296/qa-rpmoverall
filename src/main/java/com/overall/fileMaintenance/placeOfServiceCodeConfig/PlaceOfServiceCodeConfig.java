package com.overall.fileMaintenance.placeOfServiceCodeConfig;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PlaceOfServiceCodeConfig {
	private WebDriverWait wait;
	protected Logger logger;
	
	public PlaceOfServiceCodeConfig(RemoteWebDriver driver) {
		this.wait= new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement placeOfServiceTitle () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fmPosCodeConfigForm']/div[2]/div[1]/div/div/div[2]/span")));
	}
	
	public WebElement helpIconHeader () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fmPosCodeConfigForm']/div[2]/div[1]/div/div/div[1]/a")));
	}
	
	public WebElement helpIconBottom () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement clipBoardToolIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fmPosCodeConfigForm']/div[3]/div/div[1]/div[5]/div")));
	}
	
	public WebElement keyBoardShortcuts () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fmPosCodeConfigForm']/div[3]/div/div[1]/div[6]/div")));
	}
	
	public WebElement resetBtn () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement saveAndClearBtn () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	/*   Place of Service Code Configuration  */
	public WebElement placeOfServiceMainSectionHeader () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[2]/section/header/div[1]/span/span")));
	}
	
	public WebElement helpIconOfMainSection () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[2]/section/div/div[1]/a")));
	}
	
	public WebElement getCellOfTbl (int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_posCodeConfig']/tbody/tr["+row+"]/td["+col+"]")));
	}
	
	public WebElement getCellInputOfTbl (int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_posCodeConfig']/tbody/tr["+row+"]/td["+col+"]/input")));
	}
	
	public WebElement getLastRowInTable () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table#tbl_posCodeConfig tr:last-child")));
	}
	
	public WebElement getNumPagerNavigate () throws Exception{
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sp_1_tbl_posCodeConfig_pagernav")));
	}
	
	public WebElement addIconOfMainSectionTbl () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_posCodeConfig")));
	}
	
	public WebElement editIconOfMainSectionTbl () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_posCodeConfig")));
	}
	
	public WebElement fistPagerIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_posCodeConfig_pagernav")));
	}
	
	public WebElement prevPagerIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("'prev_tbl_posCodeConfig_pagernav")));
	}
	
	public WebElement nextPagerIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_posCodeConfig_pagernav")));
	}
	
	public WebElement lastPagerIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_posCodeConfig_pagernav")));
	}
	
	public WebElement totalRecordInPlaceOfServiceTbl () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_posCodeConfig_pagernav_right']/div")));
	}
	
	public WebElement popupWarmingHeader () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='alerthd_tbl_posCodeConfig']/span")));
	}
	
	public WebElement closeIconPopupWarming () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='alerthd_tbl_posCodeConfig']/a/span")));
	}
	
	public WebElement textMessWarming () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='alertcnt_tbl_posCodeConfig']/div")));
	}
	
	/* Popup Add and Edit */
	public WebElement headerPopup () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edithdtbl_posCodeConfig']/span")));
	}
	
	public WebElement messageMark () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_posCodeConfig']/tbody/tr[2]/td/div/label")));
	}
	
	public WebElement searchPayorIdIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_payor']/td[1]/div/a/span")));
	}
	
	public WebElement payorIdInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payor")));
	}
	
	public WebElement select2Drop () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));
	}
	
	public WebElement payorGroupInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_pyrGrpId")));
	}
	
	public WebElement perfFacillityInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_facId")));
	}

	public WebElement procCodeSearchIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_procCode']/td[1]/div/a")));
	}
	
	public WebElement procCodeInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procCode")));
	}
	
	public WebElement serviceTypeInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_svcTypId")));
	}

	public WebElement accnMarkCodeInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_rmkCdId")));
	}
	
	public WebElement modifierInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_modId")));
	}
	
	public WebElement clientAccntTypeInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_clnAccntTypId")));
	}
	
	public WebElement patientLocationInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_ptLocId")));
	}
	
	public WebElement patientTypeInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_ptTypId")));
	}
	
	public WebElement posCodeInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_posCodeId")));
	}
	
	public WebElement checkDeleteInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deleted")));
	}
	
	public WebElement okPopupBtn () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
	}
	
	public WebElement cancelPopupBtn () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
	}
	
	public WebElement getErrorMessInPopup () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='FormError']/td")));
	}
	
	/*  Method  */
	public void enterPayorIdInput (String value) {
		payorIdInput().clear();
		payorIdInput().sendKeys(value);
		payorIdInput().sendKeys(Keys.TAB);
		logger.info("        Enter Payor Id: " + value);
	}
	
	public void enterPayorGroupInput (String value) {
		payorGroupInput().click();
		select2Drop().sendKeys(value);
		select2Drop().sendKeys(Keys.TAB);
		logger.info("        Enter Payor Group: " + value);
	}
	
	public void enterPerfFacillityInput (String value) {
		perfFacillityInput().click();
		select2Drop().sendKeys(value);
		select2Drop().sendKeys(Keys.TAB);
		logger.info("        Enter perfFacillityInput: " + value);
	}
	
	public void enterProcCodeInput (String value) {
		procCodeInput().clear();
		procCodeInput().sendKeys(value);
		procCodeInput().sendKeys(Keys.TAB);
		logger.info("        Enter Payor Id: " + value);
	}
	
	public void enterServiceTypeInput (String value) {
		serviceTypeInput().click();
		select2Drop().sendKeys(value);
		select2Drop().sendKeys(Keys.TAB);
		logger.info("        Enter serviceType Input: " + value);
	}

	public void enterAccnMarkCodeInput (String value) {
		accnMarkCodeInput().click();
		select2Drop().sendKeys(value);
		select2Drop().sendKeys(Keys.TAB);
		logger.info("        Enter accnMarkCode Input: " + value);
	}
	
	public void enterModifierInput (String value) {
		modifierInput().click();
		select2Drop().sendKeys(value);
		select2Drop().sendKeys(Keys.TAB);
		logger.info("        Enter modifier Input: " + value);
	}
	
	public void enterClientAccntTypeInput (String value) {
		clientAccntTypeInput().click();
		select2Drop().sendKeys(value);
		select2Drop().sendKeys(Keys.TAB);
		logger.info("        Enter clientAccuntType Input: " + value);
	}
	
	public void enterPatientLocationInput (String value) {
		patientLocationInput().click();
		select2Drop().sendKeys(value);
		select2Drop().sendKeys(Keys.TAB);
		logger.info("        Enter patientLocation Input: " + value);
	}
	
	public void enterPatientTypeInput (String value) {
		patientTypeInput().click();
		select2Drop().sendKeys(value);
		select2Drop().sendKeys(Keys.TAB);
		logger.info("        Enter patientType Input: " + value);
	}
	
	public void enterPosCodeInput (String value) {
		posCodeInput().click();
		select2Drop().sendKeys(value);
		select2Drop().sendKeys(Keys.TAB);
		logger.info("        Enter posCode Input: " + value);
	}
	
	public void pressAltR() throws Exception {
        String selectAll = Keys.chord(Keys.ALT, "R");
        WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        body.sendKeys(selectAll);
        logger.info("        press Ctr + R");
    }
	
	public void enterPatx(String value) throws Exception{
		patientTypeSearchBox().clear();
		patientTypeSearchBox().sendKeys(value);
		patientTypeSearchBox().sendKeys(Keys.TAB);
		logger.info("        Enter value to Patient Type search box:" + value);
	}
	
	public void enterPosCodeSearchBox(String value) throws Exception{
		posCodeSearchBox().clear();
		posCodeSearchBox().sendKeys(value);
		posCodeSearchBox().sendKeys(Keys.TAB);
		logger.info("        Enter value to Pos Code search box:" + value);
	}
	public void enterPatientLocationSearchBox(String value) throws Exception{
		patientLocationSearchBox().clear();
		patientLocationSearchBox().sendKeys(value);
		patientLocationSearchBox().sendKeys(Keys.TAB);
		logger.info("        Enter value to Patient Location search box:" + value);
	}
	
	public void enterPayorIdSearchBox(String value) throws Exception{
		payorIdSearchBox().clear();
		payorIdSearchBox().sendKeys(value);
		payorIdSearchBox().sendKeys(Keys.TAB);
		logger.info("        Enter value to Payor Id search box:" + value);
	}
	
	public void enterPerformingFacilitySearchBox(String value) throws Exception{
		performingFacilitySearchBox().clear();
		performingFacilitySearchBox().sendKeys(value);
		performingFacilitySearchBox().sendKeys(Keys.TAB);
		logger.info("        Enter value to Performing Facility Searchbox search box:" + value);
	}
	public void enterProcedureCodeSearchBox(String value) throws Exception{
		procedureCodeSearchBox().clear();
		procedureCodeSearchBox().sendKeys(value);
		procedureCodeSearchBox().sendKeys(Keys.TAB);
		logger.info("        Enter value to Procedure Code Searchbox search box:" + value);
	}
	public void enterServiceTypeSearchBox(String value) throws Exception{
		serviceTypeSearchBox().clear();
		serviceTypeSearchBox().sendKeys(value);
		serviceTypeSearchBox().sendKeys(Keys.TAB);
		logger.info("        Enter value to Service Type Searchbox search box:" + value);
	}
	
	public void enterClientAccountTypeSearchBox(String value) throws Exception{
		clientAccountTypeSearchBox().clear();
		clientAccountTypeSearchBox().sendKeys(value);
		clientAccountTypeSearchBox().sendKeys(Keys.TAB);
		logger.info("        Enter value to Client Account Type Searchbox search box:" + value);
	}
	
	public void enterModifierSearchBox(String value) throws Exception{
		modifierSearchBox().clear();
		modifierSearchBox().sendKeys(value);
		modifierSearchBox().sendKeys(Keys.TAB);
		logger.info("        Enter value to Modifier Searchbox search box:" + value);
	}
	
	public WebElement payorIdSearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payor")));
	}
	public WebElement performingFacilitySearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_facId")));
	}
	public WebElement procedureCodeSearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_procCode")));
	}
	public WebElement serviceTypeSearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_svcTypId")));
	}
	public WebElement modifierSearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_modId")));
	}
	public WebElement clientAccountTypeSearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_clnAccntTypId")));
	}
	public WebElement patientLocationSearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_ptLocId")));
	}
	public WebElement patientTypeSearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_ptTypId")));
	}
	public WebElement posCodeSearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_posCodeId")));
	}
	
	public WebElement IDSearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_id")));
	}
	
	public void enterIDSearchBox(String value) throws Exception{
		IDSearchBox().clear();
		IDSearchBox().sendKeys(value);
		IDSearchBox().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("        Enter value to ID Searchbox search box:" + value);
	}
	
}
