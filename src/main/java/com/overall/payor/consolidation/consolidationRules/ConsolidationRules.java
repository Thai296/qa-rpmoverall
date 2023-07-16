package com.overall.payor.consolidation.consolidationRules;

import com.xifin.mars.dao.DaoManagerXifinRpm;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.utils.SeleniumBaseTest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ConsolidationRules {

	private RemoteWebDriver driver;
	protected Logger logger;
	private WebDriverWait wait;
	
	public ConsolidationRules(RemoteWebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" +driver );
	}

	/*General*/
	public WebElement consolidationTitle(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".platormPageTitle")));
	}
	
	public WebElement consolidationIDInput(){
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("lookupConsolidationId")));
	}
	
	public WebElement consolidationRulesIdTextBox(){
		  return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("consolidationRulesId")));
	}
	
	public WebElement addConsPyrGrpBtn(){
		return driver.findElement(By.id("add_tbl_consolidationPayorGroups"));
	}
	
	public WebElement errorMessageLbl(){
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//ul[@class='serverErrorsList']")));
	}
	
	public WebElement errorMessageRows(int row){
		return driver.findElement(By.xpath(".//*[@id='sectionServerMessages']/div/div[1]/ul/li["+row+"]/span"));
	}
		
	public WebElement searchConsBtn(){
		//return driver.findElement(By.xpath(".//*[@id='consolidationRulesForm']/div[1]/section/div/div[2]/div/a/span"));
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[data-search-type=\"consolidation\"]')[0]");
	}
	
	public WebElement runAuditBtn(){
		return driver.findElement(By.id("btn_audit"));
	}
	
	public WebElement auditDetailTblCell(int row){
		return driver.findElement(By.xpath(".//*[@id='tbl_auditlogwait']//tr["+row+"]/td[4]"));
	}
	
	/*Consolidation rule add page*/
	
	public WebElement consolidationDescInput(){
		return driver.findElement(By.id("consolidationDescr"));
	}
	
	public WebElement consolidationPrioritySelect(){
		return driver.findElement(By.id("consolidationRulesPriority"));
	}
	
	public WebElement activeCheckbox(){
		return driver.findElement(By.id("isActive"));
	}
	
	public WebElement saveBtn(){
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSave")));
	}
	
	public WebElement resetBtn(){
		return driver.findElement(By.id("Reset"));
	}
	
	public WebElement accnIDSearch(){
		//return driver.findElement(By.xpath(".//input[@id='accnId'][@class='accnId accnTestRuleInput toCaps']"));
		return driver.findElement(By.xpath(".//input[@id='accnId'][@class='lookupAccnId accnTestRuleInput accessionId dataDisplay accnId utilityfield toCaps']"));

	}
	
	public WebElement testRuleBtn(){
		return driver.findElement(By.id("btnTestRules"));
	}
	
	public WebElement sectionSearchInput(){
		return driver.findElement(By.id("sectionSearchField"));
	}
	
	public WebElement deleteRuleCheckbox(){
		return driver.findElement(By.id("deleteRule"));
	}
	
	//use for all section
	public WebElement deleteCheck(){
		return driver.findElement(By.id("deleted"));
	}
	
	public WebElement OKBtn(){
		return driver.findElement(By.id("sData"));
	}
	
	public WebElement cancelBtn(){
		return driver.findElement(By.id("cData"));
	}
	
	public WebElement cellTableGrid(int rowNumber, int colNumber){
		return driver.findElement(By.xpath("//*[@id='jqg"+rowNumber+"']/td["+colNumber+"]"));
	}
	
	public WebElement sectionErrorMessage(){
		return driver.findElement(By.xpath(".//*[@id='sectionServerMessages']/div/div[1]/ul"));
	}
	
	
	/* payor info section */
	public WebElement addPayorGroupBtn(){
		return driver.findElement(By.id("add_tbl_consolidationPayorGroups"));
	}
	
	public WebElement editPayorGroupBtn(){
		return driver.findElement(By.xpath("//*[@id='edit_tbl_consolidationPayorGroups']/div"));
	}
	
	public WebElement addPayorBtn(){
		return driver.findElement(By.id("add_tbl_consolidationPayors"));
	}
	
	public WebElement editPayorBtn(){
		return driver.findElement(By.xpath("//*[@id='edit_tbl_consolidationPayors']/div"));
	}
	
	public WebElement payorGroupSelect(){
		return driver.findElement(By.id("s2id_payorGroupId"));
	}
	
	public WebElement payorAbbrInput(){
		return driver.findElement(By.id("payorAbbrev"));
	}
	
	public WebElement addPayorPopup(){
		return driver.findElement(By.id("editcnttbl_consolidationPayors"));
	}
	public WebElement dosEffDateInput(){
		return driver.findElement(By.id("dosEffectiveDate"));
	}
	
	public WebElement calendateEffDateInput(){
		return driver.findElement(By.id("calendarEffectiveDate"));
	}
	
	public WebElement expDateInput(){
		return driver.findElement(By.id("expiryDate"));
	}
	
	public WebElement payorGrpTbl(){
		return driver.findElement(By.id("tbl_consolidationPayorGroups"));
	}
	
	public WebElement payortbl(){
		return driver.findElement(By.id("tbl_consolidationPayors"));
	}
	
	public WebElement pyrGrpDropDownBtnOnAddNewConsPayorGrpPopup() {
		return driver.findElement(By.xpath(".//*[@id='s2id_payorGroupId']/a/span[2]/b"));
	}
	
	public WebElement DropDownInput() {
		return driver.findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
	}
	
	public WebElement addNewDiagnPopup() {
		return driver.findElement(By.id("editcnttbl_consolidationDiags"));
	}
	
	public WebElement payorTblCell(int row, int col){
		return driver.findElement(By.xpath("//*[@id='tbl_consolidationPayors']//tr["+row+"]/td["+col+"]"));
	}
	
	public WebElement payorGroupTblCell(int row, int col){
		return driver.findElement(By.xpath("//*[@id='tbl_consolidationPayorGroups']//tr["+row+"]/td["+col+"]"));
	}
	
	public WebElement delPayorCheckbox(int row){
		return driver.findElement(By.xpath("//*[@id='tbl_consolidationPayors']//tr["+row+"]/td[10]/input"));
	}
	
	public WebElement delPayorGroupCheckbox(int row){
		return driver.findElement(By.xpath("//*[@id='tbl_consolidationPayorGroups']//tr["+row+"]/td[8]/input"));
	}
	
	public WebElement errorMessageAddPayorPopup(){
		 WebDriverWait wait = new WebDriverWait(driver, 5);
		  return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='FormError']/td")));
	}
	
	/* Diagnosis section */
	public WebElement addDiagnosisBtn(){
		return driver.findElement(By.id("add_tbl_consolidationDiags"));
	}
	
	public WebElement editDiagnosisBtn(){
		return driver.findElement(By.xpath("//*[@id='edit_tbl_consolidationDiags']/div"));
	}
	
	public WebElement diagnosisCodeID(){
		return driver.findElement(By.id("diagCode"));
	}
	
	public WebElement diagnosisDesc(){
		return driver.findElement(By.id("diagDescr"));
	}
	
	public WebElement diagnosisSearchBtn(){
		return driver.findElement(By.xpath("//*[@id='tr_diagCode']/td[1]/div/a/span"));
	}
	
	public WebElement diagnosisSearchTitle(){
		return driver.findElement(By.xpath("/html/head/title"));
	}
	
	public WebElement diagnosisCodeTypeSearch(){
		return driver.findElement(By.id("diagnosisCodeType"));
	}
	
	public WebElement diagnosisCodeSearch(){
		return driver.findElement(By.id("diagnosisCode"));
	}
	
	public WebElement diagnosisDescSearch(){
		return driver.findElement(By.id("description"));
	}
	
	public WebElement closeBtnDiagnosicSearch(){
		return driver.findElement(By.xpath("//*[@id='diagnosisCodeSearch']/div[2]/button[3]"));
	}
	
	public WebElement resetBtnDiagnosisSearch(){
		return driver.findElement(By.xpath("//*[@id='diagnosisCodeSearch']/div[2]/button[2]"));
	}
	
	public WebElement searchBtnDiagnosisSearch(){
		return driver.findElement(By.xpath("//*[@id='diagnosisCodeSearch']/div[2]/button[1]"));
	}
	
	public WebElement closeDiagSearchbtnAtSearchresultPage(){
		return driver.findElement(By.xpath(".//*[@id='diagnosisCodeSearch']/div[3]/button[2]"));
	}
	
	public WebElement radioBtnNA(){
		return driver.findElement(By.id("diagRequirementNA"));
	}
	
	public WebElement radioBtnNone(){
		return driver.findElement(By.id("diagRequirementNone"));
	}
	
	public WebElement radioBtnOnePlus(){
		return driver.findElement(By.id("diagRequirementOnePlus"));
	}
	
	public WebElement radioBtnAll(){
		return driver.findElement(By.id("diagRequirementAll"));
	}
	
	public WebElement deleteDiagnosisCheckbox(int row){
		return driver.findElement(By.xpath("//*[@id='tbl_consolidationDiags']//tr["+row+"]/td[4]/input"));
	}
	
	public WebElement diagnosisCodeTypeDropDown(){
		  return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("diagnosisCodeType")));
    }
	
	public WebElement DiagCodeSearchResultTable() {
		return driver.findElement(By.id("diagnosissearchTable"));
	}

	public WebElement cellOnDiagCodeSearchResult(int col, int row, String attr) {
		return driver.findElement(By.xpath(".//*[@id='diagnosissearchTable']/tbody/tr[" + col + "]/td[" + row + "]/" + attr + ""));
	}
	
	public WebElement cellOnDiagnosisTbl(int row, int col){
		return driver.findElement(By.xpath("//*[@id='tbl_consolidationDiags']//tr["+row+"]/td["+col+"]"));
	}
	
	public WebElement diagnosisSearchHeader(){
		return driver.findElement(By.xpath(".//*[@id='diagnosisSearch']/header/span"));
	}

	public WebElement newSeachbtnAtDiagnosisResult(){
		return driver.findElement(By.xpath(".//*[@id='diagnosisCodeSearch']/div[3]/button[1]"));
	}
	
	/*Client Account info section*/
	public WebElement addClientAcctInforBtn(){
		return driver.findElement(By.id("add_tbl_consolidationclientAccountTypes"));
	}
	
	public WebElement editClientAcctInfoBtn(){
		return driver.findElement(By.xpath("//*[@id='edit_tbl_consolidationclientAccountTypes']/div"));
	}
	
	public WebElement clientAcctTypeSelect(){
		return driver.findElement(By.xpath("//*[@id='tr_clnAccntTypeId']/td[2]"));
	}
	public WebElement addClientAcctTypeDropDownBtn() {
		return driver.findElement(By.xpath(".//*[@id='s2id_clnAccntTypeId']/a/span[2]/b"));
	}
	
	public WebElement clientAccountTypeTable() {
		return driver.findElement(By.id("tbl_consolidationclientAccountTypes"));
	}
	
	public WebElement editClnAccntTypPopup() {
		return driver.findElement(By.id("editcnttbl_consolidationclientAccountTypes"));
	}
	
	public WebElement delClnAccnCheckbox(int row){
		return driver.findElement(By.xpath("//*[@id='tbl_consolidationclientAccountTypes']//tr["+row+"]/td[4]/input"));
	}
	
	/*Level section*/
	public WebElement modifierConsolidationCheckbox(){
		return driver.findElement(By.id("checkModConsolidation"));
	}
	
	public WebElement minProcCodeMatch(int row){
		return driver.findElement(By.id("minMatch_"+row+""));
	}
	
	public WebElement minUnit(int row){
		return driver.findElement(By.id("minUnits_"+row+""));
	}
	
	public WebElement sameUnit(int row){
		return driver.findElement(By.id("sameUnits_"+row+""));
	}
	
	public WebElement addReqBtn(int levelRow){
		return driver.findElement(By.id("add_tbl_requirements_level_"+levelRow+""));
	}
	
	public WebElement editReqBtn(int levelRow){
		return driver.findElement(By.xpath("//*[@id='edit_tbl_requirements_level_"+levelRow+"']/div"));
	}
	
	public WebElement proCodeID(){
		return driver.findElement(By.id("procCode"));
	}
	
	public WebElement minService(){
		return driver.findElement(By.id("min"));
	}
	
	public WebElement maxService(){
		return driver.findElement(By.id("max"));
	}
	
	public WebElement requirementsTbl(int levelRow){
		return driver.findElement(By.id("tbl_requirements_level_"+levelRow+""));
	}
	
	public WebElement linksTbl(int levelRow){
		return driver.findElement(By.id("tbl_links_level_"+levelRow+""));
	}
	
	public WebElement multiConsolidationCheckbox(){
		return driver.findElement(By.id("checkMultiples"));
	}
	
	public WebElement requirementsTblCell(int row, int col, int level){
		return driver.findElement(By.xpath("//*[@id='tbl_requirements_level_"+level+"']//tr["+row+"]/td["+col+"]"));
	}
	
	public WebElement delRequireCheckbox(int row, int level){
		return driver.findElement(By.xpath("//*[@id='tbl_requirements_level_"+level+"']//tr["+row+"]/td[7]/input"));
	}
	
	public WebElement levelFieldset(int level){
		return driver.findElement(By.xpath("//*[@id='levelNo_"+level+"']"));
	}
	
	/*Procedure popup*/
	public WebElement proCodeIDSearch(){
		return driver.findElement(By.id("prcId"));
	}
	
	public WebElement proDescSearch(){
		return driver.findElement(By.id("descr"));
	}
	
	public WebElement proCodeTypeSearch(){
		return driver.findElement(By.id("procTypeId"));
	}
	
	public WebElement revenueCodeSearch(){
		return driver.findElement(By.id("revenueCode"));
	}
	
	public WebElement serviceTypeSearch(){
		return driver.findElement(By.id("svcTyp"));
	}
	
	public WebElement serviceLevelSearch(){
		return driver.findElement(By.id("svcLvl"));
	}
	
	public WebElement inputDeletedRecord(){
		return driver.findElement(By.id("includeDeletedRecords"));
	}
	
	public WebElement closeProCodeSearchBtn(){
		return driver.findElement(By.xpath("//*[@id='procCodeSearch']/div[2]/button[3]"));
	}
	
	public WebElement resetProCodeSearchBtn(){
		return driver.findElement(By.xpath("//*[@id='procCodeSearch']/div[2]/button[2]"));
	}
	
	public WebElement searchProCodeSearchBtn(){
		return driver.findElement(By.xpath("//*[@id='procCodeSearch']/div[2]/button[1]"));
	}
	
	/*Link section*/
	
	public WebElement addLinkBtn(int levelRow){
		return driver.findElement(By.id("add_tbl_links_level_"+levelRow+""));
	}
	
	public WebElement editLinkBtn(){
		return driver.findElement(By.xpath("//*[@id='edit_tbl_links_level_1']/div"));
	}
	
	public WebElement reqProCode(){
		return driver.findElement(By.id("requiredProcCode"));
	}
	
	public WebElement resultingProCode(){
		return driver.findElement(By.id("resultingProcCode"));
	}
	
	public WebElement addNewLevel(){
		return driver.findElement(By.id("addLevel"));
	}
	
	public WebElement linkTblCell(int row, int col, int level){
		return driver.findElement(By.xpath("//*[@id='tbl_links_level_"+level+"']//tr["+row+"]/td["+col+"]"));
	}
	
	public WebElement delLinkCheckbox(int row, int level){
		return driver.findElement(By.xpath("//*[@id='tbl_links_level_"+level+"']//tr["+row+"]/td[9]/input"));
	}
	
	public WebElement deleteLinkIcon(int level){
		return driver.findElement(By.xpath(".//*[@id='levelNo_"+level+"']/div[1]/div[1]/div[2]/a"));
	}
	
	public WebElement warningComfirmOkBtn(){
		return driver.findElement(By.xpath("/html/body/div[21]/div[3]/div/button[1]"));
	}

	/*Consolidation Result Section*/
	public WebElement addConsoResultBtn() throws InterruptedException{
		return wait.until(ExpectedConditions.elementToBeClickable((By.id("add_tbl_consolidationResults"))));
	}
	
	public WebElement editConsoResultBtn(){
		return driver.findElement(By.xpath("//*[@id='edit_tbl_consolidationResults']/div"));
	}
	
	public WebElement consolidationResultTbl(){
		return driver.findElement(By.id("tbl_consolidationResults"));
	}
	
	public WebElement numberService(){
		return driver.findElement(By.id("numServices"));
	}
	
	public WebElement maxTotal(){
		return driver.findElement(By.id("maxTotal"));
	}
	
	public WebElement totalSameUnitOnce(){
		return driver.findElement(By.id("totalSameUnitsOnce"));
	}
	public WebElement addNewConsolidationPopup(){
		return driver.findElement(By.id("editcnttbl_consolidationResults"));
	}
	public By getNewConsolidationPopupLocator()
	{
		return By.id("editcnttbl_consolidationResults");
	}
	public WebElement consolidationResultTblCell(int row, int col){
		return driver.findElement(By.xpath("//*[@id='tbl_consolidationResults']//tr["+row+"]/td["+col+"]"));
	}
	
	/*Consolidation Search and Search Result page*/
	public WebElement delResultCheckbox(int row){
		return driver.findElement(By.xpath("//*[@id='tbl_consolidationResults']//tr["+row+"]/td[10]/input"));
	}
	
	//-----Element Search Page
	public WebElement searchConsolidationIDInput(){
		return (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("consolidationId")));
	}
	public WebElement searchDescriptionInput(){
		return driver.findElement(By.id("description"));
	}
	public WebElement searchPayorGroupCombobox(){
		return driver.findElement(By.id("payorGroup"));
	}
	public WebElement searchPayorIDInput(){
		return driver.findElement(By.id("payorId"));
	}
	public WebElement searchPayorIDIcon(){
		return driver.findElement(By.xpath(".//*[@id='consolidationSearch']/div/div/div/div/fieldset/div/div[4]/div[1]/a/span"));
	}
	public WebElement searchRequiredProcedureCodeInput(){
		return driver.findElement(By.id("requiredProcedureId"));
	}
	public WebElement searchRequiredProcedureCodeIcon(){
		return driver.findElement(By.xpath(".//*[@id='consolidationSearch']/div/div/div/div/fieldset/div/div[5]/div[1]/a/span"));
	}
	public WebElement searchConsolidatedProcedureCodeInput(){
		return driver.findElement(By.id("consProcedureId"));
	}
	public WebElement searchConsolidatedProcedureCodeIcon(){
		return driver.findElement(By.xpath(".//*[@id='consolidationSearch']/div/div/div/div/fieldset/div/div[6]/div[1]/a/span"));
	}
	public WebElement totalConsolidationResultLabel(){
		return driver.findElement(By.xpath(".//*[@id='pager_right']/div"));
	}
	public WebElement pageHelpLink(){
		return driver.findElement(By.id("pageHelpLink"));
	}
	
	//-----Element Search Page --- > Deleted section
	public WebElement deleteRuleInUseCheckbox(){
		return driver.findElement(By.id("deletedRulesInUse"));
	}
	public WebElement deleteRuleDeletedCheckbox(){
		return driver.findElement(By.id("deletedRulesDeleted"));
	}
	public WebElement deleteRuleAllCheckbox(){
		return driver.findElement(By.id("deletedRulesAll"));
	}
	//-----Element Search Page --- > Active section
	public WebElement ActiveRuleActiveCheckbox(){
		return driver.findElement(By.id("activeRulesActive"));
	}
	public WebElement ActiveRuleInactiveCheckbox(){
		return driver.findElement(By.id("activeRulesInactive"));
	}
	public WebElement ActiveRuleAllCheckbox(){
		return driver.findElement(By.id("activeRulesAll"));
	}
	//-----Element Search Page --- > Footer button
	
	public WebElement closeSearchPageBtn(){
		return driver.findElement(By.xpath(".//*[@id='consolidationSearch']/div[2]/button[3]"));
	}
	public WebElement resetSearchPageBtn(){
		return driver.findElement(By.xpath(".//*[@id='consolidationSearch']/div[2]/button[2]"));
	}
	public WebElement searchSearchPageBtn(){
		return driver.findElement(By.xpath(".//*[@id='consolidationSearch']/div[2]/button[1]"));
	}
	
	
	/*Consolidation rule Search Result page*/
	public WebElement consolSearchResultTbl(){
		return driver.findElement(By.id("gview_consolidationsearchTable"));
	}
	
	public WebElement consolSearchResultKeepSearchOpenCheckbox(){
		return driver.findElement(By.id("keepSearchOpen"));
	}
	
	public WebElement consolSearchResultCloseBtn(){
		return driver.findElement(By.xpath("//*[@class='closeAction btn btn_close']"));
	}
	
	public WebElement consolSearchResultNewSearchBtn(){
		return driver.findElement(By.xpath("//*[@class='submitAction btn btn_submit lockDownOnSubmit']"));
	}
	
	public WebElement consolSearchResultReloadIcon(){
		return driver.findElement(By.id("refresh_consolidationsearchTable"));
	}
	
	public WebElement consolSearchResultConsolidationId(int row, int col) throws Exception{
		return driver.findElement(By.xpath(".//*[@id='consolidationsearchTable']/tbody/tr["+row+"]/td["+col+"]/a"));
	}
	
	public WebElement consolSearchResultTblCell(int row, int col) throws Exception{
		return driver.findElement(By.xpath(".//*[@id='consolidationsearchTable']/tbody/tr["+row+"]/td["+col+"]"));
	}
	
	public WebElement consolSearchResultNextpageIcon() throws Exception{
		return driver.findElement(By.id("next_pager"));
	}
	
	public WebElement consolSearchResultLastpageIcon() throws Exception{
		return driver.findElement(By.id("last_pager"));
	}
	
	public WebElement consolSearchResultPrevpageIcon() throws Exception{
		return driver.findElement(By.id("prev_pager"));
	}
	
	public WebElement consolSearchResultFirstpageIcon() throws Exception{
		return driver.findElement(By.id("first_pager"));
	}
	
	public WebElement consolSearchResultRightPageLable() throws Exception{
		return driver.findElement(By.xpath("//*[@class='ui-paging-info']"));
	}
	
	public WebElement consolSearchResultEntryPerPageCombobox() throws Exception{
		return driver.findElement(By.xpath("//*[@class='ui-pg-selbox']"));
	}
	
	public WebElement loadPageHelpIcon() throws Exception{
		return driver.findElement(By.xpath("//*[@id='consolidationRulesForm']/div[1]/div[2]/section/div/div[1]/a"));
	}
	
	public WebElement consolidationSearchPopupHeader() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='consolidationSearch']/header/span"));
	}
	
	/*Test Consolidation Rules*/
	public WebElement testConsolRulesTblCell(int row, int col){
		return driver.findElement(By.xpath(".//*[@id='tbl_consolidationAccnTests']/tbody/tr["+row+"]/td["+col+"]"));
	}
	
	public WebElement testConsolRulesTblAccnIdLbl(){
		return driver.findElement(By.id("accnId2"));
	}
	
	//Table section 
	public WebElement diagnosTable(){
		return driver.findElement(By.id("tbl_consolidationDiags"));
	}
	
	public WebElement clientAccountTable(){
		return driver.findElement(By.id("tbl_consolidationclientAccountTypes"));
	}
	
	//-----Function--------->Enter
	
	public void inputToSearchConsolidationIDInput(String val){
		searchConsolidationIDInput().sendKeys(val+Keys.TAB);
		logger.info("        Entered "+val+" in Search Consolidation ID.");
	}
	
	public void inputToSearchDescriptionInput(String val){
		searchDescriptionInput().sendKeys(val);
		logger.info("        Entered "+val+" in Search Consolidation Description.");
	}
	
	public void inputToSearchPayorIDInput(String val){
		searchPayorIDInput().sendKeys(val+Keys.TAB);
		logger.info("        Entered "+val+" in Search Consolidation Description.");
	}
	
	public void inputToAccnIDSearch(String val){
		accnIDSearch().sendKeys(val);
		logger.info("        Entered "+val+" in Accn Id Search Input field.");
	}
	
	public void inputToConsolidationIDInput(String value){
		//consolidationIDInput().sendKeys(val+Keys.ENTER);
		consolidationIDInput().sendKeys(value);
		consolidationIDInput().sendKeys(Keys.TAB);
		logger.info("        Entered  Consolidation ID: " + value);
	}
	
	public void inputMinProCodeMatch(String val, int row){
		minProcCodeMatch(row).clear();
		minProcCodeMatch(row).sendKeys(val);
		minProcCodeMatch(row).sendKeys(Keys.TAB);
		logger.info("        Entered "+val+" in Min Procedure Code Match.");
	}
	
	public void inputMinUnit(String val, int row){
		minUnit(row).clear();
		minUnit(row).sendKeys(val);
		minUnit(row).sendKeys(Keys.TAB);
		logger.info("        Entered "+val+" in Min Unit.");
	}
	
	public void inputConsolidationDesc(String val){
		consolidationDescInput().clear();
		consolidationDescInput().sendKeys(val);
		consolidationDescInput().sendKeys(Keys.TAB);
		logger.info("        Entered "+val+" in Description input field.");
	}
	
	public void inputProCode(String val){
		proCodeID().clear();
		proCodeID().sendKeys(val);
		proCodeID().sendKeys(Keys.TAB);
		logger.info("        Entered "+val+" in Procedure Code input field.");
	}
	
	public void inputMinService(String val){
		minService().clear();
		minService().sendKeys(val);
		minService().sendKeys(Keys.TAB);
		logger.info("        Entered "+val+" in Min Service.");
	}
	
	public void inputMaxService(String val){
		maxService().sendKeys(val);
		maxService().sendKeys(Keys.TAB);
		logger.info("        Entered "+val+" in Max Service.");
	}
	
	public void inputReqProCode(String val){
		reqProCode().sendKeys(val);
		reqProCode().sendKeys(Keys.TAB);
		logger.info("        Entered "+val+" in Required Procedure Code input field.");
	}
	
	public void inputResultingProCode(String val){
		resultingProCode().sendKeys(val);
		resultingProCode().sendKeys(Keys.TAB);
		logger.info("        Entered "+val+" in Resulting Procedure Code input field.");
	}
	
	public void inputDosEffDt(String val){
		dosEffDateInput().clear();
		dosEffDateInput().sendKeys(val);
		logger.info("        Enter Dos Effective Date for Payor Group");
	}
	
	public void inputCalEffDt(String val){
		calendateEffDateInput().clear();
		calendateEffDateInput().sendKeys(val);
		logger.info("        Enter Calendar Eff Date for Payor Group");
	}
	
	public void inputExpDt(String val){
		expDateInput().clear();
		expDateInput().sendKeys(val);
		logger.info("        Enter Exp Date for Payor Group");
	}
	
	//-----Function--------->Click
	
	public void clickConsolSearchResultCloseBtn(){
		consolSearchResultCloseBtn().click();
		logger.info("        Click on Close button.");
	}
	
	public void clickConsolSearchResultNewSearchBtn(){
		consolSearchResultNewSearchBtn().click();
		logger.info("        Click on New Search button.");
	}
	
	public void clickLoadPageHelpIcon() throws Exception{
		loadPageHelpIcon().click();
		logger.info("        Click on Help Icon.");
	}
	
	public void clickTestRuleBtn() throws Exception{
		testRuleBtn().click();
		logger.info("        Click on Test Rule button.");
	}
	
	public void clickAddConsPyrGrpBtn() throws Exception{
		addConsPyrGrpBtn().click();		
		logger.info("        Clicked Add button in Payor Groups grid.");
	}
	
	public void inputConsDesc(String val){
		consolidationDescInput().clear();
		consolidationDescInput().sendKeys(val);
		logger.info("        Enter "+val+"for Description field");
	}
	
	public void clickAddConsResultBtn() throws Exception{
		addConsoResultBtn().click();
		Thread.sleep(1000);
		logger.info("        Clicked Add button in Consolidation Results grid.");
	}
	
	public void clickOkBtn() throws Exception{
		OKBtn().click();
		Thread.sleep(5000);
		logger.info("        Clicked OK button.");
	}
	
	public void clickOnSaveBtn() throws Exception{
		saveBtn().click();
		Thread.sleep(3000);
		logger.info("        Clicked Save button.");
	}
	
	public void clickResetBtn() throws Exception{
		resetBtn().click();
		Thread.sleep(2000);
		logger.info("        Clicked Reset button.");
	}
	
	public void clickRunAuditBtn() throws Exception{
		runAuditBtn().click();
		logger.info("        Clicked Run audit button.");
	}
	
	public void clickOnModifierConsolidationCheckbox() throws Exception{
		modifierConsolidationCheckbox().click();
		logger.info("        Click on Modifier Consolidation checkbox.");
	}
	
	public void clickAddNewLevelBtn() throws Exception{
		addNewLevel().click();
		Thread.sleep(1000);
		logger.info("        Clicked Add New Level button.");
	}
	
	
	public void addProcCodeToLevelAndResult(String procCodeIDReq,String procCodeResult, String maxService, String minService, String minimumProcCodeMatch, String minimumUnit, String levelRow, SeleniumBaseTest b) throws Exception{
		addProcCodeToRequirementTbl(procCodeIDReq, maxService, minService, levelRow, b);
		addProcCodeToLinkTbl(procCodeIDReq, procCodeResult,maxService, minService, levelRow, b);
		addMinProcMatchAndMinUnit(minimumProcCodeMatch,minimumUnit,levelRow);
		addProcCodeToResultTbl(procCodeIDReq,b);
	}
	
	public void addProcCodeToRequirementTbl(String procCodeID, String maxService, String minService, String levelRow, SeleniumBaseTest b) throws Exception{
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addReqBtn(Integer.valueOf(levelRow)));
	
		Assert.assertTrue(b.isElementPresent(addReqBtn(Integer.valueOf(levelRow)), 5),"        Add requirement button must available");
		b.clickHiddenPageObject(this.addReqBtn(Integer.valueOf(levelRow)), 0);
		
		Assert.assertTrue(b.isElementPresent(proCodeID(), 5),"        Procedure Code ID must available");
		Assert.assertTrue(b.isElementPresent(minService(), 5),"        Min service input must available");
		Assert.assertTrue(b.isElementPresent(maxService(),5),"        Max service input must available");
		Assert.assertTrue(b.isElementPresent(OKBtn(),5),"        OK Button must available");
		inputProCode(procCodeID);
		minService().clear();
		inputMinService(minService);
		maxService().clear();
		inputMaxService(maxService);
		clickOkBtn();
	}
	
	public void addProcCodeToLinkTbl(String procCodeIDReq,String procCodeResult, String maxService, String minService, String levelRow, SeleniumBaseTest b) throws Exception{
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addLinkBtn(Integer.valueOf(levelRow)));
		Assert.assertTrue(b.isElementPresent(addLinkBtn(Integer.valueOf(levelRow)),5),"        Add link button should show.");
		b.clickHiddenPageObject(this.addLinkBtn(Integer.valueOf(levelRow)), 0);
		
		Assert.assertTrue(b.isElementPresent(reqProCode(), 5),"        Required Procedure Code ID should show.");
		Assert.assertTrue(b.isElementPresent(resultingProCode(), 5),"        Result Procedure Code ID should show.");
		inputReqProCode(procCodeIDReq);
		inputResultingProCode(procCodeResult);
		clickOkBtn();
		
	}
	
	public void addMinProcMatchAndMinUnit(String minimumProcCodeMatch, String minimumUnit,String levelRow) throws Exception{
		minProcCodeMatch(Integer.valueOf(levelRow)).clear();
		inputMinProCodeMatch(minimumProcCodeMatch, Integer.valueOf(levelRow));
		minUnit(Integer.valueOf(levelRow)).clear();
		inputMinUnit(minimumUnit,Integer.valueOf(levelRow));
	}
	
	public void addProcCodeToResultTbl(String procCodeID, SeleniumBaseTest b) throws Exception{
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addConsoResultBtn());
		Assert.assertTrue(b.isElementPresent(addConsoResultBtn(), 5),"        Add button should show.");
		b.clickHiddenPageObject(addConsoResultBtn(),0);
		Assert.assertTrue(b.isElementPresent(proCodeID(), 5),"        Procedure Code input field should show.");
		inputProCode(procCodeID);
		Assert.assertTrue(b.isElementPresent(OKBtn(), 5),"        OK Button should show.");
		clickOkBtn();
	}
	
	public void scrollToElement(WebElement el) throws InterruptedException{
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
		Thread.sleep(1000);
	}
	
	public String addNewProcCodeInResultTblNotInLevelSection(int levelOfLevelSec, DaoManagerXifinRpm daoManagerXifinRpm, SeleniumBaseTest b, String testDb) throws Exception{
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addConsoResultBtn());
		
		String procCode = daoManagerXifinRpm.getProcedureCodeId(testDb).get(0);
		int i=2;
		String xpath ="";
		while (true){
				xpath = "return document.evaluate(\"//*[@id='tbl_requirements_level_1']//tr["+i+"]/td[3]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";
				WebElement el = (WebElement) ((JavascriptExecutor) driver).executeScript(xpath);
				if (el == null) break;
				if (el.getText().equalsIgnoreCase(procCode)){
					procCode = daoManagerXifinRpm.getProcedureCodeId(testDb).get(0);
					i=2;
				}
				i++;
		}
		
		Assert.assertTrue(b.isElementPresent(addConsoResultBtn(), 5),"        Add Consolidation Result button must available");
		clickAddConsResultBtn();
		Assert.assertTrue(b.isElementPresent(proCodeID(), 5),"        Procedure Code ID input must available");
		proCodeID().clear();
		inputProCode(procCode);
		clickOkBtn();
		
		minProcCodeMatch(levelOfLevelSec).clear();
		inputMinProCodeMatch("1", levelOfLevelSec);
		minUnit(levelOfLevelSec).clear();
		inputMinUnit("1",levelOfLevelSec);
		
		return procCode;
	}
	
	public boolean checkProcCodeAddedToTableResult(String procCode){
		int i=2;
		String xpath="";
		WebElement el=null;
		while (true){
				xpath = "return document.evaluate(\"//*[@id='tbl_consolidationResults']//tr["+i+"]/td[3]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";
				el = (WebElement) ((JavascriptExecutor) driver).executeScript(xpath);
				if(el==null) return false;
				if (el.getText().equalsIgnoreCase(procCode)) return true;
				i++;
		}
	}
	
	public boolean checkValueIsAvailableInTable(WebElement table, int indexCol, String valueCheck){ // this function get better performance than getColumValue in common
		int i=2;
		String xpath="";
		String idTable = table.getAttribute("id");
		WebElement el=null;
		while (true){
				xpath = "return document.evaluate(\"//*[@id='"+idTable+"']//tr["+i+"]/td["+indexCol+"]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";
				el = (WebElement) ((JavascriptExecutor) driver).executeScript(xpath);
				if(el==null) return false;
				if (el.getText().equalsIgnoreCase(valueCheck)) return true;
				i++;
		}
	}
	
	public String getProcedureCodeNotDuplicate(String procedureCodeID, DaoManagerXifinRpm daoManagerXifinRpm, String testDb) throws Exception{
		String result = daoManagerXifinRpm.getProcedureCodeId(testDb).get(0);
		while(result.equalsIgnoreCase(procedureCodeID)) result = daoManagerXifinRpm.getProcedureCodeId(testDb).get(0);
		return result;
	}
	public String getClnAccntTypNotDuplicate(String ClnAccntTyp, DaoManagerXifinRpm daoManagerXifinRpm, String testDb) throws Exception{
		String result = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb).get(1);
		while(result.equalsIgnoreCase(ClnAccntTyp)) result = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb).get(1);
		return result;
	}
	
	public void clickOnMultiConsolidationCheckbox() throws Exception{
		multiConsolidationCheckbox().click();
		logger.info("        Click on Modifier Consolidation checkbox.");
	}
	
	public void inputPayorId(String val) throws InterruptedException{
		payorAbbrInput().clear();
		payorAbbrInput().sendKeys(val);
		dosEffDateInput().click();
//		payorAbbrInput().sendKeys(Keys.TAB);
		logger.info("        Enter PayorID for Payor");
	}
	
	public void addProcCodeInLevelSection(String procCodeID,String procCodeID1, String maxService, String minService, String minimumProcCodeMatch, String minimumUnit, int levelRow, SeleniumBaseTest b) throws Exception{
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addReqBtn(levelRow));
		
		Assert.assertTrue(b.isElementPresent(minProcCodeMatch(levelRow), 5),"        Min procedure code match input field should show.");
		Assert.assertTrue(b.isElementPresent(minUnit(levelRow), 5),"        Minimum unit input field should show.");
		inputMinProCodeMatch(minimumProcCodeMatch, levelRow);
		inputMinUnit(minimumUnit,levelRow);		
		
		Assert.assertTrue(b.isElementPresent(addReqBtn(levelRow), 5),"        Add requirement button must available");
		//b.clickHiddenPageObject(this.addReqBtn(levelRow), 0);
		addReqBtn(levelRow).click();
		Thread.sleep(1000);
		
		Assert.assertTrue(b.isElementPresent(proCodeID(), 5),"        Procedure Code ID field should show.");
		Assert.assertTrue(b.isElementPresent(minService(), 5),"        Min service input field should show.");
		Assert.assertTrue(b.isElementPresent(maxService(),5),"        Max service input field should show.");
		Assert.assertTrue(b.isElementPresent(OKBtn(),5),"        OK Button field should show.");		
		inputProCode(procCodeID);
		inputMinService(minService);
		inputMaxService(maxService);		
		clickOkBtn();
		
		Assert.assertTrue(b.isElementPresent(addLinkBtn(levelRow),5),"        Add link button should show");
		//b.clickHiddenPageObject(this.addLinkBtn(levelRow), 0);
		addLinkBtn(levelRow).click();
		Thread.sleep(1000);
		
		Assert.assertTrue(b.isElementPresent(reqProCode(), 5),"        Required Procedure Code ID should show");
		Assert.assertTrue(b.isElementPresent(resultingProCode(), 5),"        Result Procedure Code ID should show");
		inputReqProCode(procCodeID);
		inputResultingProCode(procCodeID1);
		clickOkBtn();
	}

	
	public void addDataToPyrGrp(String val, String date,SeleniumBaseTest b) throws Exception {
		Assert.assertTrue(b.isElementPresent(addConsPyrGrpBtn(), 5),"        Add Consolidation Payor Group should show.");
		clickAddConsPyrGrpBtn();
		Assert.assertTrue(b.isElementPresent(pyrGrpDropDownBtnOnAddNewConsPayorGrpPopup(), 5),"        Payor Group Drop Down Button on add new Cons Payor Group should show.");
		clickPyrGrpDropDownBtnOnAddNewConsPayorGrpPopup();
		Assert.assertTrue(b.isElementPresent(DropDownInput(), 5),"        Dropdown should show.");
		enterDropDownInput(val);
		Assert.assertTrue(b.isElementPresent(dosEffDateInput(), 5),"        DOS Effective Date Input should show.");
		inputDosEffDt(date);
		Assert.assertTrue(b.isElementPresent(calendateEffDateInput(), 5),"        Calendar Effective Date Input should show.");
		inputCalEffDt(date);
		Assert.assertTrue(b.isElementPresent(OKBtn(), 5),"        Ok button Payor Group should show.");
		clickOkBtn();
		logger.info("        Added Payor Group.");
	}
	
	public void addDataToPyr(String val, String date,SeleniumBaseTest b) throws Exception {
		Assert.assertTrue(b.isElementPresent(addPayorBtn(), 5),"        Add Payor button should show.");
		addPayorBtn().click();
		Assert.assertTrue(b.isElementPresent(payorAbbrInput(), 5),"        Payor Abbrv Input should show.");
		inputPayorId(val);
		Assert.assertTrue(b.isElementPresent(dosEffDateInput(), 5),"        DOS Effective Date Input should show.");
		inputDosEffDt(date);
		Assert.assertTrue(b.isElementPresent(OKBtn(), 5),"        Calendar Effective Date Input should show.");
		inputCalEffDt(date);
		Assert.assertTrue(b.isElementPresent(OKBtn(), 5),"        Ok button Payor Group should show.");
		clickOkBtn();
		logger.info("        Added Payor.");
	}
	
	public void addDataToClntAcctTyp(String val,SeleniumBaseTest b) throws Exception {
		Assert.assertTrue(b.isElementPresent(addClientAcctInforBtn(), 5),"        Add Client Account Type button should show.");
		b.clickHiddenPageObject(addClientAcctInforBtn(), 0);
		Assert.assertTrue(b.isElementPresent(addClientAcctTypeDropDownBtn(), 5),"        Add Client Account Type DropDown button should show.");
		clickaddClientAcctTypeDropDownBtn();
		enterDropDownInput(val);
		Assert.assertTrue(b.isElementPresent(OKBtn(), 5),"        Ok button Payor Group should show.");
		clickOkBtn();
		logger.info("        Added Client Account Type.");
	}
	
	public void addDataToConsolidationResultsTable(String proceCD,SeleniumBaseTest b) throws Exception {
		Assert.assertTrue(b.isElementPresent(addConsoResultBtn(), 5),"        Add New Consolidation button should show");
		//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addConsoResultBtn());
		clickAddConsResultBtn();
		Assert.assertTrue(b.isElementPresent(getNewConsolidationPopupLocator()),"        Add New Consolidation Popup should show");
		inputProCode(proceCD);
		wait.until(ExpectedConditions.elementToBeClickable(OKBtn()));
		Assert.assertTrue(b.isElementPresent(OKBtn(), 5),"        Ok button Payor Group should show.");
		clickOkBtn();
	}
	
	public void clickAddDiagnosisBtn() throws Exception{
		addDiagnosisBtn().click();
		logger.info("        Click Add Diagnosis Button.");
	}
	
	public void clickDiagnosisSearchBtn() throws Exception{
		diagnosisSearchBtn().click();
		logger.info("        Click Diagnosis Search Button.");
	}
	
	public void clickDiagnosisSearchBtnOnSearchDiagPopup() throws Exception {
		searchBtnDiagnosisSearch().click();
		logger.info("        Click to Search Diagnosis Button on Search Diagnosis popup.");
	}
	public void clickCellOnDiagCodeSearchResult(int col , int row ,String attr) throws Exception {
		cellOnDiagCodeSearchResult(col, row, attr).click();
		logger.info("        Click to First Element On Diagnosis Code Search Result.");
	}
	public void clickaddClientAcctInforBtn() throws Exception {
		addClientAcctInforBtn().click();
		logger.info("        Click to Add Client Account Information Button.");
	}
	
	public void clickaddClientAcctTypeDropDownBtn() throws Exception {
		addClientAcctTypeDropDownBtn().click();
		logger.info("        Click to Add Client Account Type DropDown Button.");
	}
	public void enterDropDownInput(String val) throws Exception {
		DropDownInput().sendKeys(val);
		DropDownInput().sendKeys(Keys.TAB);
		logger.info("        Enter "+val+" to Element");
	}
	
	public void clickPyrGrpDropDownBtnOnAddNewConsPayorGrpPopup() throws Exception {
		pyrGrpDropDownBtnOnAddNewConsPayorGrpPopup().click();
		logger.info("        Click to PyrGrp DropDown Button On Add New Consolidate Payor Group Popup");
	}
	public void clickNonRadioBtn() throws Exception {
		radioBtnNone().click();
		logger.info("        Clicked Radio Button - None.");
	}
	
	public void clickSearchConsBtn() throws Exception {
		searchConsBtn().click();
		logger.info("        Click to Search Consolidation Button");
	}
	
	/*Util function*/
	public boolean rowIsMarkForDelete(WebElement table, int row){
		String idTable = table.getAttribute("id");
		WebElement el = driver.findElement(By.xpath("//*[@id='"+idTable+"']//tr["+row+"]"));
		return el.getAttribute("class").contains("rowMarkedForDelete");
	}

	public boolean checkValueInList(List<String> list, String value){
		for(int i=0; i < list.size(); i++)	if (list.get(i).equalsIgnoreCase(value)) return true;
		return false;
	}
	
	public boolean checkValueInListList(List<List<String>> list, String value, int colToCompare){
		for(int i=0; i < list.size(); i++)	if (list.get(i).get(colToCompare).equalsIgnoreCase(value)) return true;
		return false;
	}
	
	public void waitToStartPage(){
		WebElement titleEl = null;
		while(true){
			try{
				titleEl = driver.findElement(By.xpath("//*[@id='consolidationRulesForm']/div[1]/div[1]/div/span"));
				if (titleEl.isDisplayed()) return;
			} catch (Exception e){}	
		}
	}
	
	public void clickDeleteRuleDeletedCheckbox() throws Exception {
		deleteRuleDeletedCheckbox().click();
		logger.info("        Click to Delete Rule Deleted Checkbox.");
	}
	public void clickSearchSearchPageBtn() throws Exception {
		searchSearchPageBtn().click();
		logger.info("        Click to Search button on Search Page.");
	}
	public void clickPageHelpLink() throws Exception {
		pageHelpLink().click();
		logger.info("        Click to Page Help Link.");
	}
	
	public void inputConsolidationDescriptionSearchInput(String val) throws Exception {
		diagnosisDescSearch().sendKeys(val);
		diagnosisDescSearch().sendKeys(Keys.TAB);
		logger.info("        Enter "+val+" to Element");
	}
	
	public void inputDiagnosisId(String val){
		diagnosisCodeID().clear();
		diagnosisCodeID().sendKeys(val);
		diagnosisCodeID().sendKeys(Keys.TAB);
		logger.info("        Enter to diagnosis Id Input");
	}
	public void inputDiagnosisDesc(String val){
		diagnosisDesc().clear();
		diagnosisDesc().sendKeys(val);
		logger.info("        Input to diagnosis description Input");
	}
	
	public void clickActiveRuleInactiveCheckbox() throws Exception {
		ActiveRuleInactiveCheckbox().click();
		logger.info("        Click Inactive radio button on Search window.");
	}
	
	public void clickActiveCheckbox() throws Exception {
		activeCheckbox().click();
		logger.info("        Clicked Active checkbox on the Consolidation Rules page.");
	}
	public void inputSearchRequiredProcedureCode(String val){
		searchRequiredProcedureCodeInput().sendKeys(val);
		searchRequiredProcedureCodeInput().sendKeys(Keys.TAB);
		logger.info("        Enter "+val+" to Search Required Procedure Code Input");
	}
	public void inputSearchConsolidatedProcedureCode(String val){
		searchConsolidatedProcedureCodeInput().sendKeys(val);
		searchConsolidatedProcedureCodeInput().sendKeys(Keys.TAB);
		logger.info("        Enter "+val+" to Search Consolidated Procedure Code Input");
	}
	public void inputSearchPayorID(String val){
		searchPayorIDInput().sendKeys(val);
		searchPayorIDInput().sendKeys(Keys.TAB);
		logger.info("        Enter "+val+" to Search PayorID Input");
	}
	public void clickResetSearchPageBtn() throws Exception {
		resetSearchPageBtn().click();
		logger.info("        Click Reset Search page.");
	}
	
	public void addDiagnosisToDiagnosisTbl(String diagnosis, SeleniumBaseTest b) throws Exception{
		Assert.assertTrue(b.isElementPresent(addDiagnosisBtn(), 5),"        Add Diagnosis button should show.");
		clickAddDiagnosisBtn();
		Assert.assertTrue(b.isElementPresent(diagnosisCodeID(), 5),"        Diagcode input should show.");
		inputDiagnosisId(diagnosis);
		Assert.assertTrue(b.isElementPresent(OKBtn(), 5),"        Save button should show.");
		clickOkBtn();
		
	}
	public void inputDiagDescSearch(String val) throws Exception{
		diagnosisDescSearch().clear();
		diagnosisDescSearch().sendKeys(val);
		logger.info("        Input Diagnosis Desc");
	}
	
	public void selectDropDownJQGird(WebElement dropDown,String textSelect) throws InterruptedException{
		dropDown.findElement(By.tagName("a")).click();
		WebElement list = driver.findElement(By.xpath(".//*[@id='select2-drop']/ul"));

		List<WebElement> allRows = list.findElements(By.tagName("li")); 
		// And iterate over them, getting the cells 
		for (WebElement row : allRows) {
			if (row.getText().equalsIgnoreCase(textSelect)) {
				row.click();
				logger.info("        Clicked on dropdown table : " + textSelect);
				break;
			}
		}
	}

	public void selectCheckBoxOnTable(String idTable, int indexColCheckValue, int indexColCheckBox,String valueToCheck, SeleniumBaseTest b) throws Exception {
		int i=2;
		WebElement tableRow;
		String xpath="";
		while(true){
			xpath = "return document.evaluate(\"//*[@id='"+idTable+"']//tr["+i+"]/td["+indexColCheckValue+"]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";
			tableRow = (WebElement) ((JavascriptExecutor) driver).executeScript(xpath);
			if (tableRow!=null && tableRow.getText().equalsIgnoreCase(valueToCheck)){
				String checkBoxEl = "return document.evaluate(\"//*[@id='"+idTable+"']//tr["+i+"]/td["+indexColCheckBox+"]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";
				b.selectCheckBox((WebElement) ((JavascriptExecutor) driver).executeScript(checkBoxEl));
				return;
			}
			if (tableRow==null) return;
			i++;
		}
	}
	
	public List<String> getListValueOnTable(String idTable, int indexColToGet) throws Exception {
		int i=2;
		WebElement tableRow;
		String xpath="";
		List<String> result = new ArrayList<>();
		while(true){
			xpath = "return document.evaluate(\"//*[@id='"+idTable+"']//tr["+i+"]/td["+indexColToGet+"]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";
			tableRow = (WebElement) ((JavascriptExecutor) driver).executeScript(xpath);
			if (tableRow!=null)
				result.add(tableRow.getText());
			else 
				return result;
			i++;
		}
	}
	
	public void inputDiagnosisCodeSearch(String val){
		diagnosisCodeSearch().sendKeys(val);
		diagnosisCodeSearch().sendKeys(Keys.TAB);
		logger.info("        Enter "+val+" to Diagnosis Code Search input");
	}
	public String verifySearchResult(String numOfConsDB) throws InterruptedException{
		Thread.sleep(1000);
		String numOfConsUI = this.totalConsolidationResultLabel().getText();
	    String totalCons = "";
	    if (!numOfConsUI.equalsIgnoreCase("No Result")) {
	    	totalCons = numOfConsUI.substring(numOfConsUI.lastIndexOf(" ") + 1);
	    }
	    else {
	    	totalCons = "0";
	    }
		return totalCons;
	   
	}

	public String getDiagnosisCodeNotDuplicate(String diagCode, DaoManagerXifinRpm daoManagerXifinRpm, String dbEnv) throws Exception{
		String newCode = daoManagerXifinRpm.getRandomDiagnosisIdFromDIAGCD(dbEnv).get(0);
		while(newCode.equalsIgnoreCase(diagCode)){
			newCode = daoManagerXifinRpm.getRandomDiagnosisIdFromDIAGCD(dbEnv).get(0);
		}
		return newCode;
	}
	
	public void clickOnPayorCell(int row, int col) throws Exception{
		  payorTblCell(row, col).click();
		  logger.info("        Click on payor table cell.");
	}
	
	public void clickOnPayorGrpCell(int row, int col) throws Exception{
		  payorGroupTblCell(row, col).click();
		  logger.info("        Click on payor group table cell.");
	}
	
	public void clickOnNewSearchBtnAtDiagnosisResult() throws Exception{
		  newSeachbtnAtDiagnosisResult().click();
		  logger.info("        Click on New Search btn.");
	}
	
	public void clickOnCancelBtn() throws Exception{
		  cancelBtn().click();
		  logger.info("        Click on Cancel btn.");
	}
	
	public String convertDecimalFormat(int number){
		DecimalFormat Formatter = new DecimalFormat("###,###,###");
		return Formatter.format(number);

	}

	public String verifySearchResultInSearchCons(String numOfConsDB) throws Exception{
		Thread.sleep(1000);
		String numOfConsUI = this.consolSearchResultRightPageLable().getText();
	    String totalCons = "";
	    if (!numOfConsUI.equalsIgnoreCase("No Result")) {
	    	totalCons = numOfConsUI.substring(numOfConsUI.lastIndexOf(" ") + 1);
	    }
	    else {
	    	totalCons = "0";
	    }
		return totalCons;
	   
	}
	public void entersectionSearchInput(String val) throws Exception{
		sectionSearchInput().sendKeys(val);
		  logger.info("        Enter "+val+" to Section Search Input.");
	}
	
}

