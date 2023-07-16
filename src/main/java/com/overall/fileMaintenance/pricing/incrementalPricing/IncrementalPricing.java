package com.overall.fileMaintenance.pricing.incrementalPricing;

import com.overall.utils.IncrementalPricingUtils;
import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.mars.dao.IGenericDaoXifinRpm;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.rpm.ServiceDao;
import com.xifin.qa.dao.rpm.ServiceDaoImpl;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class IncrementalPricing
{
	private static final Logger LOG = Logger.getLogger(IncrementalPricing.class);
	private final RemoteWebDriver driver;
	private final IGenericDaoXifinRpm daoManagerXifinRpm;
	private final WebDriverWait wait;
	private final ServiceDao serviceDao;

	public IncrementalPricing(RemoteWebDriver driver, Configuration config, WebDriverWait wait)
	{
		this.driver = driver;
		this.daoManagerXifinRpm = new DaoManagerXifinRpm(config.getRpmDatabase());
		this.serviceDao=new ServiceDaoImpl(config.getRpmDatabase());
		this.wait=wait;
	}
	/*Incremental Pricing*/
	public WebElement inPriTitle(){
		return wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".platormPageTitle")));
	}

	public WebElement inPriLoadTableTitle(){
		return driver.findElement(By.xpath("//*[@id=\"incrementalPricingForm\"]/div/section/header/div/span/span"));
	}
	
	public WebElement inPriSearchIconBtn() {
		return driver.findElement(By.cssSelector("span[data-search-type='incrpricing']"));
	}
	
	public WebElement inPriIDInput() {
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='lookupIncrPricingId']")));
	}
	
	public WebElement inPriHelpIconBtn() {
		return driver.findElement(By.xpath("//*[@id=\"incrementalPricingForm\"]/div/section/div/div[1]/a"));
	}
	
	public WebElement inPriWarningMessContent() {
		return driver.findElement(By.xpath(".//*[@id='messagefor_lookupIncrPricingId']/div[2]"));
	}
	
	public WebElement inPriWarningMessCloseIconBtn() {
		return driver.findElement(By.xpath(".//*[@id='messagefor_lookupIncrPricingId']/div[1]/a"));
	}
	
	public WebElement pricingName() {
		return driver.findElement(By.id("name"));
	}
	
	public WebElement ruleSection(int index){
		return driver.findElement(By.xpath(".//*[@id='section_"+index+"']/div"));
	}
	public WebElement deleteARuleDialog() {
		return driver.findElement(By.xpath(".//*[@id='deleteARuleDialog']"));
	}	
	
	public void clickInPriSearchIconBtn() throws Exception{
		inPriSearchIconBtn().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Search Icon button.");
	}
	
	public void inputInPriID(String value) throws Exception{
		inPriIDInput().sendKeys(value);
		inPriIDInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		LOG.info("        Entered Incremental Pricing ID: " + value);
	}
	
	public void clickInPriHelpIconBtn() throws Exception{
		inPriHelpIconBtn().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Help Icon button.");
	}
	
	public void clickInPriWarningMessCloseIconBtn() throws Exception{
		inPriWarningMessCloseIconBtn().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Warning Message Close Icon button.");
	}
	
	public void inputPricingName(String value) {
		pricingName().sendKeys(value);
		pricingName().sendKeys(Keys.TAB);
		LOG.info("        Entered Pricing Name: " +value);
	}
	
	public WebElement incrementalPricingName(){
		return driver.findElement(By.xpath("//input[@id='name']"));
	}
	
	public WebElement ruleHeaderLeftTop(String value){
		return driver.findElement(By.xpath(".//*[@id='section_"+value+"']/header/div/span/span"));
	}
	
	public WebElement addNewRuleBtn() {
		return driver.findElement(By.xpath(".//*[@id='addNewRule']"));
	}
	public WebElement resetIncrPricingBtn() {
		return driver.findElement(By.xpath("//*[@id=\"Reset\"]"));
	}
	public WebElement submitIncrPricingBtn() {
		return driver.findElement(By.xpath("//*[@id=\"btnSaveAndClear\"]"));
	}
	
	public WebElement ruleGroup(String grpID) {
		return driver.findElement(By.xpath(".//*[@id='section_"+grpID+"']"));
	}	
	
	public WebElement deleteRuleLink(int index) {
		return driver.findElement(By.xpath(".//*[@id='deleteRuleId_"+index+"']/a/span"));
	}
	
	public WebElement warningMessage(){
		return driver.findElement(By.id("deleteARuleDialog"));
	}
	
	public WebElement okWarningMessage() {
		return driver.findElement(By.xpath("//button[contains(.,'OK')]"));
	}
	
	public WebElement verifyEmptyData(int index) {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('div#add_tbl_RulesTest_"+index+"')).get(0)");
	}
	
	public void clickSubmitIncrPricingBtn() throws Exception{
		submitIncrPricingBtn().click();
		Thread.sleep(2000);
		LOG.info("        Clicked Submit button.");
	}
	
	public void clickResetIncrPricingBtn() throws Exception{
		resetIncrPricingBtn().click();
		Thread.sleep(2000);
		LOG.info("        Clicked Reset button.");
	}
	
	/*table left*/
	public WebElement levelDropdown(int index){
		return driver.findElement(By.xpath(".//*[@id='ruleLevel_"+index+"']"));
	}
	public WebElement tableRuleQuantity(int index){
		return driver.findElement(By.xpath(".//*[@id='tbl_Rules_"+index+"']"));
	}
	
	public WebElement quantityTypeDropdown(int index){
		return driver.findElement(By.xpath(".//*[@id='quantityTypes_"+index+"']"));
	}
	
	public WebElement addNewRuleTblLeftBtn(int index){
		return driver.findElement(By.xpath(".//*[@id='add_tbl_Rules_"+index+"']"));
	}
	
	public WebElement editRuleTblLeftBtn(int index){
		return driver.findElement(By.xpath(".//*[@id='edit_tbl_Rules_"+index+"']"));
	}
	
	public WebElement delCheckboxTblLeft(int index){
		return driver.findElement(By.xpath(".//*[@id='tbl_Rules_"+index+"']/tbody/tr[2]/td[7]/input"));
	}
	
	public WebElement quantityTbl(int index){
		return driver.findElement(By.xpath(".//*[@id='tbl_Rules_"+index+"']"));
	}
	
	public WebElement fifthColumnTblLeft(int index){
		return driver.findElement(By.xpath(".//*[@id='tbl_Rules_"+index+"_isPerUnit']"));
	}
	
	public WebElement isPerUnitlbl(){
		return driver.findElement(By.id("tr_isPerUnit"));
	}
	
	public void clickAddNewRuleTblLeftBtn(int index) throws Exception{
		addNewRuleTblLeftBtn(index).click();
		Thread.sleep(1000);
		LOG.info("        Clicked Add New Quantity Rule button.");
	}
	
	/*table right*/	
	public WebElement addNewRuleTblRightBtn(int index){
		return driver.findElement(By.xpath(".//*[@id='add_tbl_RulesTest_"+index+"']"));
	}
	public WebElement tableRuleTest(int index){
		return driver.findElement(By.xpath(".//*[@id='tbl_RulesTest_"+index+"']"));
	}
	public WebElement includeRadio(int indexTable, int rowNumber){
		return driver.findElement(By.xpath("//*[@id='tbl_RulesTest_"+indexTable+"']/tbody/tr["+(rowNumber+1)+"]/td[9]/input[1]"));
	}
	public WebElement excludeRadio(int indexTable, int rowNumber){
		return driver.findElement(By.xpath("//*[@id='tbl_RulesTest_"+indexTable+"']/tbody/tr["+(rowNumber+1)+"]/td[9]/input[2]"));
	}
	public WebElement editRuleTblRightBtn(int  index){
		return driver.findElement(By.xpath(".//*[@id='edit_tbl_RulesTest_"+index+"']"));
	}
	public WebElement procIDColLabelIncludExcludTable(int indexTable){
		return driver.findElement(By.xpath(".//*[@id='tbl_RulesTest_"+indexTable+"_procId']/div"));
	}
	
	public WebElement basicTestCheckboxTblRight(int index){
		return driver.findElement(By.xpath(".//*[@id='basicTest_"+index+"']"));
	}
	
	public WebElement basicProcCheckboxTblRight(int index){
		return driver.findElement(By.xpath(".//*[@id='basicProc_"+index+"']"));
	}
	
	public WebElement delCheckboxTblRight(int  index){
		return driver.findElement(By.xpath(".//*[@id='tbl_RulesTest_"+index+"']/tbody/tr[2]/td[11]/input"));
	}
	
	public void clickAddNewRuleTblRightBtn(int index) throws Exception{
		addNewRuleTblRightBtn(index).click();
		Thread.sleep(1000);
		LOG.info("        Clicked Add New Inclusion & Exclusion button.");
	}
	
	/*Popup on Table left*/
	public WebElement addNewRulePopup(int index){
		return driver.findElement(By.xpath(".//*[@id='editmodtbl_Rules_"+index+"']"));
	}
	
	public WebElement quantityFromInput(){
		return driver.findElement(By.xpath(".//*[@id='quantityFrom']"));
	}
	
	public WebElement quantityToInput(){
		return driver.findElement(By.xpath(".//*[@id='quantityTo']"));
	}
	
	public WebElement price(){
		return driver.findElement(By.xpath(".//*[@id='price']"));
	}
	
	public WebElement perUnitCheckbox(){
		return driver.findElement(By.xpath(".//*[@id='isPerUnit']"));
	}
	
	public WebElement delCheckbox(){
		return driver.findElement(By.xpath(".//*[@id='isDeleted']"));
	}
	
	public WebElement okBtn(){
		return driver.findElement(By.xpath(".//*[@id='sData']"));
	}
	
	public WebElement cancelBtn(){
		return driver.findElement(By.xpath(".//*[@id='cData']"));
	}
	
	public void inputQuantityFrom(String value) {
		quantityFromInput().sendKeys(value);
		LOG.info("         Entered Quantity From: " + value);
	}
	
	public void inputQuantityTo(String value) {
		quantityToInput().sendKeys(value);
		LOG.info("         Entered Quantity To: " + value);
	}
	
	public void inputPrice(String value) {
		price().sendKeys(value);
		LOG.info("         Entered price: " + value);
	}
	
	public void clickOkButton() throws Exception{
		okBtn().click();
		Thread.sleep(2000);
		LOG.info("        Clicked ok button.");
	}
	
	public void clickCancelBtn() throws Exception{
		cancelBtn().click();
		Thread.sleep(1000);
		LOG.info("        Clicked Cancel button.");
	}
	
	/*Popup on Table right*/
	public WebElement addNewInclusionExclusionPopup(int index){
		return driver.findElement(By.xpath(".//*[@id='editmodtbl_RulesTest_"+index+"']"));
	}
	
	public WebElement procIdInput(){
		return driver.findElement(By.xpath(".//*[@id='procId']"));
	}
	
	public WebElement testIdInput(){
		return driver.findElement(By.xpath(".//*[@id='testId']"));
	}
	
	public WebElement testTypeDropdown(){
		return driver.findElement(By.xpath(".//*[@id='s2id_testTypeId']"));
	}
	
	public WebElement serviceTypeDropdown(){
		return driver.findElement(By.xpath(".//*[@id='s2id_serviceTypeId']")); 
	}
	
	public WebElement excludeCheckbox(){
		return driver.findElement(By.xpath(".//*[@id='isExclude']"));
	}
	
	public WebElement excludeRefTestsCheckbox(){
		return driver.findElement(By.xpath(".//*[@id='isExcludeRefTests']"));
	}
	
	public WebElement procInclusionExclusionRadioBtn(){
		return driver.findElement(By.xpath("//input[@value='Proc']"));
	}
	
	public WebElement procCodeHeaderInclusionExclusionTable(int index){
		return driver.findElement(By.xpath("//div[@id='jqgh_tbl_RulesTest_"+index+"_procId']"));
	}
	
	public WebElement serviceTypHeaderInclusionExclusionTable(int index){
		return driver.findElement(By.xpath("//div[@id='jqgh_tbl_RulesTest_"+index+"_serviceTypeName']"));
	}
	
	public WebElement testId(){
		return driver.findElement(By.id("testId"));
	}
	
	public WebElement excludeTypeDropDown(){
		return driver.findElement(By.id("excludeType"));
	}
	
	public WebElement inclusionTbl(int index){
		return driver.findElement(By.xpath(".//*[@id='gview_tbl_RulesTest_"+index+"']"));
	}
	
	public WebElement tblRuleTest(int index){
		return driver.findElement(By.xpath(".//*[@id='tbl_RulesTest_"+index+"']"));
	}
	
	public WebElement procCodeSearchIcon(){
		return driver.findElement(By.xpath(".//*[@id='tr_procId']/td[1]/div/a/span"));
	}
	
	public void inputProcId(String value) {
		procIdInput().sendKeys(value);
		LOG.info("         Entered Proc Code : " + value);
	}
	
	public void inputIncrementalPricingName(String value) {
		incrementalPricingName().sendKeys(value);
		LOG.info("         Entered Incremental Pricing Name : " + value);
	}	
	
	public void inputTestId(String value) {
		testId().sendKeys(value);
		LOG.info("        Entered Test ID: "+value);
	}
	
	public boolean checkRadioBtn(WebElement tableElement, String include){
		boolean checked = true;
		//get row count
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
		for(WebElement row: rows){
			List<WebElement> cols = row.findElements(By.xpath("td"));
			if(cols.get(3).getText().equals(include)){
				checked = row.findElement(By.tagName("input")).isSelected();
			}
		}
		return checked;
	}
	
	public String inputIncludeRule(SeleniumBaseTest b,String testDb)throws Exception{
		String include ="";
		List<String> testInfo = daoManagerXifinRpm.getTestCodeId(testDb);
		include = testInfo.get(1);
		Assert.assertTrue(b.isElementPresent(testId(), 5));
		inputTestId(testInfo.get(1));
		
		return include;
	}
	
	/*header component*/
	public WebElement pricingIdLbl(){
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("incrPricingId")));
	}
	
	public WebElement inPricingRuleTitle() {
		return driver.findElement(By.className("platormPageTitle"));
	}

	public WebElement serviceTypeCellInInclusionTable(int indexTable, int IndexRow){
		return driver.findElement(By.xpath(".//*[@id='tbl_RulesTest_"+indexTable+"']/tbody/tr["+IndexRow+"]"));
	}
	
	public WebElement serviceTypeCellInRuleQuantityTable(int indexTable, int IndexRow){
		return driver.findElement(By.xpath(".//*[@id='tbl_Rules_"+indexTable+"']/tbody/tr["+IndexRow+"]"));
	}
	
	public WebElement errorMessagePanel(){
		return driver.findElement(By.xpath("//ul[contains(@class,'serverInfoList')]"));
	}
	
	public WebElement errorMessageSection() {
		return driver.findElement(By.xpath(".//*[@id='sectionServerMessages']/div/div[2]/ul/li/span"));
	}
	
	public WebElement errorMessagePopupLeft() {
		return driver.findElement(By.xpath(".//*[@id='FormError']/td"));
	}
	
	public List<List<String>> addMultiIncludeRule(int index,int indextable,SeleniumBaseTest b,String testDb) throws Exception{
		IncrementalPricingUtils incrementalPricingUtils = new IncrementalPricingUtils(driver);
		List<List<String>> listIncludeRuleInfo = new ArrayList<>();
		List<String> singleIncludeRule = null;
		
		for (int i = 0; i < index; i++) {
			singleIncludeRule = new ArrayList<>();
			Assert.assertTrue(b.isElementPresent(addNewRuleTblRightBtn(0), 5),"        Add new  Inclusion/Exclusion button should show");
			b.clickHiddenPageObject(addNewRuleTblRightBtn(0), 0);
			String proCodeId = daoManagerXifinRpm.getProcedureCodeId(testDb).get(0);
			String serviceType = serviceDao.getServiceTypFromSvcTypAndProcCd().getDescr();
			while(b.getColumnValue(tableRuleTest(indextable), serviceType))
			{
				serviceType = serviceDao.getServiceTypFromSvcTypAndProcCd().getDescr();
			}
			Assert.assertTrue(b.isElementPresent(procIdInput(), 5),"        Proc Id input field should show");
			inputProcId(proCodeId);
			incrementalPricingUtils.selectDropDownJQGird(serviceTypeDropdown(),serviceType);
			b.selectCheckBox(excludeRefTestsCheckbox());
			Assert.assertTrue(b.isElementPresent(okBtn(), 5),"        ok button should show");
			clickOkButton();
			
			singleIncludeRule.add(proCodeId);
			singleIncludeRule.add(serviceType);
			
			listIncludeRuleInfo.add(singleIncludeRule);
		}
		
		return listIncludeRuleInfo;
	} 
	
	public List<String> addMultiInclusionExclusion(int index, int indextable, SeleniumBaseTest b, IGenericDaoXifinRpm daoManagerXifinRpm) throws Exception{
		IncrementalPricingUtils incrementalPricingUtils = new IncrementalPricingUtils(driver);
		List<String> listIncludeRuleInfo = new ArrayList<>();
		String data = "";
		
		for (int i = 0; i < index; i++) {
			b.clickHiddenPageObject(addNewRuleTblRightBtn(0), 0);
			boolean added = false;
			do {
				data = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm).get(0);

				boolean duplicate = b.getColumnValue(tableRuleTest(0), data);
				if (duplicate) {
					LOG.info("        Skip Facility: " + data + " because it's duplicate.");
					continue; 
				}
				added = (!duplicate);
			} while (!added);
			inputTestId(data);
			okBtn().click();
			listIncludeRuleInfo.add(data);
		}
		return listIncludeRuleInfo;
	} 
	
	public WebElement inclusionTblNumberofRow(int index){
		return driver.findElement(By.xpath("//*[@id=\"jqg2\"]/td["+index+"]"));
	}
	
	public void checkInputPricingId(String id, int timeOut) throws Exception{
		boolean flag = false ;
		int count = 0;
		do {
			inputInPriID(id);
			Thread.sleep(2000);
			flag = pricingName().isDisplayed();
			if(!flag)driver.navigate().refresh();
			if(count == timeOut)break;
			count++;
		} while (!flag);
	}	
		
	public WebElement addRecordMessError() {
		return driver.findElement(By.xpath(".//*[@id='FormError']/td"));
	}
	
	public WebElement quantityTblNumberofRow(int index){
		return driver.findElement(By.xpath(".//*[@id='gview_tbl_Rules_"+index+"']/div[1]/span[2]"));
	}
	
	public WebElement warningPopupForPricingName(){
		return driver.findElement(By.xpath(".//*[@id='messagefor_message0']/div[2]"));
	}
	
	public void clickOkButtonDeletePopUp() throws Exception{
		Actions action = new Actions(driver); 
		action.sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(1000);
		LOG.info("        Clicked ok button.");
	}
	
	public WebElement ruleSection(){
		return driver.findElement(By.xpath("html/body/section/div"));
	}
	
	public WebElement okBtnDeleteRuleDialog(){
		return driver.findElement(By.xpath("html/body/div[5]/div[3]/div/button[1]"));
	}
	
	public WebElement cancelBtnDeleteRuleDialog(){
		return driver.findElement(By.xpath("html/body/div[5]/div[3]/div/button[2]"));
	}
	
	public void clickOkBtnDeleteRuleDialog() throws Exception{
		Actions action = new Actions(driver); 
		action.sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(1000);
		LOG.info("        Clicked OK button.");
	}
	
	public void clickAddNewRuleBtn() throws Exception{
		addNewRuleBtn().click();
		Thread.sleep(1000);
		LOG.info("        Clicked Add New Rule button.");
	}	
	public WebElement procCodeColtblRight(){
		return driver.findElement(By.xpath(".//*[@id='jqgh_tbl_RulesTest_0_procId']"));
	}
	
	public WebElement valueOfRightTable(int index, int row, int col) {
		return driver.findElement(By.xpath(".//*[@id='tbl_RulesTest_"+index+"']/tbody/tr["+row+"]/td["+col+"]"));
	}
	
	public WebElement editRecordHeader(int index) {
		return driver.findElement(By.xpath(".//*[@id='edithdtbl_RulesTest_"+index+"']/span"));
	}
	public void clearData(WebElement element) throws InterruptedException{
		element.clear();
		element.sendKeys(Keys.TAB);
		Thread.sleep(1000);
		LOG.info("        Clear data");
	}
}
