package com.overall.fileMaintenance.pricing;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.JavascriptExecutor;

import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class TestCode {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private SeleniumBaseTest b;
	
	public TestCode(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	
	public WebElement testId(){
		return driver.findElement(By.id("lookupTestAbbrv"));
	}
	public WebElement tabs(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('div.tabContainer')).get(0)");
		//return driver.findElement(By.className(""));
	}
	
	public List<WebElement> loadOption(){
		return driver.findElements(By.name("loadOption"));
	}
	
	public WebElement effDate(){
		return driver.findElement(By.name("testCode.effDate"));
	}
	
	public WebElement labTestCodeID(){
		return driver.findElement(By.id("oldBillingTestNum"));
	}
	
	/*public WebElement okBtn(){
		//return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('button.btn_submit lockDownOnSubmit')).get(0)");
		return driver.findElement(By.xpath("html/body/div[4]/div[3]/div/button[1]"));
	}	*/
	
	public WebElement singleTestRadio() {
		return driver.findElement(By.xpath(".//*[@id='profileNoLoadDialog']/fieldset/div/div[1]/div/input"));
	}

	public WebElement profileTestRadio() {
		return driver.findElement(By.xpath(".//*[@id='profileNoLoadDialog']/fieldset/div/div[2]/div/input"));
	}
	public WebElement noLoadTestRadio() {
		return driver.findElement(By.xpath(".//*[@id='profileNoLoadDialog']/fieldset/div/div[3]/div/input"));
	}

	public WebElement labelTest() {
		return driver.findElement(By.id("labelTest"));
	}

	public WebElement createOptionOKBtn() {
		return driver.findElement(By.xpath("html/body/div[4]/div[3]/div/button[1]"));
	}
	public WebElement createScreen() {
		return driver.findElement(By.xpath(".//*[@id='profileNoLoadDialog']"));
	}
	public WebElement noLoadTestScreen() {
		return driver.findElement(By.xpath(".//*[@id='testCodeForm']/div[4]/div[3]/div[1]/div/div[1]/div/div[1]/span"));
	}
	public WebElement effectiveDatelabel() {
		return driver.findElement(By.xpath(".//*[@id='effDateLb']"));
	}
	
	public WebElement testName() throws Exception{
		return driver.findElement(By.id("name"));
	}
	
	public WebElement testNote() {
		return driver.findElement(By.id("testNotes"));
	}
	public WebElement saveAndClearButton() {
		return driver.findElement(By.id("btnSaveAndClear"));
	}
	public WebElement LabelTest() {
		return driver.findElement(By.id("labelTest"));
	}
	public WebElement testCodePageTitle() {
		return driver.findElement(By.xpath("//*[@id='testCodeForm']/div[4]/div[1]/div/span"));
	}
	
	public WebElement testIDLabel() {
		return driver.findElement(By.xpath(".//*[@id='testCodeForm']/div[4]/div[2]/section/div/div[2]/div/label"));
	}
	
	//TestID input field
		public WebElement testIdInput() throws Exception{
			return driver.findElement(By.id("lookupTestAbbrv"));
		}
		
		public WebElement resetBtn() throws Exception{
			return driver.findElement(By.id("Reset"));
		}
		
		public WebElement saveAndClearBtn() throws Exception{
			return driver.findElement(By.id("btnSaveAndClear"));
		}
		
		public WebElement selectEffDate() throws Exception{
			return driver.findElement(By.id("effDate"));
		}
		
		public WebElement addEffDate() throws Exception{
			return driver.findElement(By.id("creEffDate"));
		}
		
		public WebElement testNameInput() throws Exception{
			return driver.findElement(By.id("testName"));
		}
		
		public WebElement expDateInput() throws Exception{
			return driver.findElement(By.id("expDate"));
		}
		
		public WebElement selectTestType() throws Exception{
			return driver.findElement(By.id("testType"));
		}
		
		public WebElement selectDeps() throws Exception{
			return driver.findElement(By.id("deps"));
		}
		
		public WebElement labTestInput() throws Exception{
			return driver.findElement(By.id("oldBillingTestNum"));
		}
		
		public WebElement maxDis() throws Exception{
			return driver.findElement(By.id("maxDiscPct"));
		}
		
		
		
		public WebElement componentTab() throws Exception{
			return driver.findElement(By.xpath(".//*[@id='ui-id-1']"));
		}
		
		public WebElement addComponent() {
			return driver.findElement(By.xpath(".//*[@id='add_tbl_componentTable']/div"));
		}
		
		public WebElement testID() throws Exception{
			return driver.findElement(By.id("testAbbrev"));
		}
		
		public WebElement verifyTestIDinTestCodepage() throws Exception{
			return driver.findElement(By.id("TestAbbrv"));
		}
		
			
		public WebElement okBtn() {
			return driver.findElement(By.id("sData"));
		}
		
		
		
		public WebElement testIdTable() throws Exception{
			return driver.findElement(By.xpath(".//*[@id='jqg1']/td[4]"));
		}
		
		public WebElement exclusionsTab(){
			return driver.findElement(By.id("ui-id-7"));
		}
		
		public WebElement payorGroupTbl() throws Exception{
			return driver.findElement(By.xpath(".//*[@id='gview_tbl_pyrGrpExclTable']/div[1]"));
		}
		
		public WebElement addPayorGroup() {
			return driver.findElement(By.xpath(".//*[@id='add_tbl_pyrGrpExclTable']/div"));
		}
		
		public WebElement addPayorExclusion(){
			return driver.findElement(By.xpath(".//*[@id='add_tbl_pyrExclTable']/div"));
		}
		
		public WebElement groupIdDropdown(){
			return driver.findElement(By.xpath(".//*[@id='s2id_pyrGrpId']/a"));
		}
		public WebElement groupPayortable() throws Exception{
			return driver.findElement(By.xpath(".//*[@id='jqg2']/td[2]"));
		}
		public WebElement groupIdSearch() throws Exception{
			return driver.findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
		}
		public WebElement addPayorId() throws Exception{
			return driver.findElement(By.id("pyrAbbrv"));
		}
		
		public WebElement payorName() throws Exception{
			return driver.findElement(By.xpath(".//*[@id='jqg1']/td[3]"));
		}
		public WebElement payorTbl() throws Exception{
			return driver.findElement(By.id("tbl_pyrExclTable"));
		}
		
		public void clickCreateOption(){
			createOptionOKBtn().click();
			logger.info("        Click Ok button ");
		}
		
		// set testId
		public void setTestId(String testId) throws Exception{
				testIdInput().sendKeys(testId);
				testIdInput().sendKeys(Keys.TAB);
				logger.info("        Entered TestId: " + testId);
		}
		
		public void setTestName(String testName) throws Exception{
			testNameInput().sendKeys(testName);
			logger.info("        Enterred TestName: "  + testName);
		}
		
		public void selectTestType(String testType) throws Exception{
			selectTestType().sendKeys(testType);
			logger.info("        Select testType: " + testType);
		}
		
		public void setEffectiveDate(String effDate) throws Exception{
			addEffDate().sendKeys(effDate);
			addEffDate().sendKeys(Keys.TAB);
			logger.info("        Add Effective date: " + effDate);
		}
		
		public void clickAddComponent() {
			addComponent().click();
			logger.info("        CLick on Add Component icon");
		}
		
		public void setComponentTestId(String testID) throws Exception{
			testID().sendKeys(testID);
			testID().sendKeys(Keys.TAB);
			logger.info("        Add new TestId for component"  + testID);
		}
		
		public void clickOkCompBtn(){
			okBtn().click();
			logger.info("        Click on OK button to create new component");
		}
		public void clickExclusionTab(){
			exclusionsTab().click();
			logger.info("        Click on Exclusions tab");
		}
		public void clickAddPayorGroupBt(){
			addPayorGroup().click();
			logger.info("        Click on Add Payor Group icon");
		}
		public void clickAddPayorExclusionBtn(){
			addPayorExclusion().click();
			logger.info("        Click on Add Payor Exclusion icon");
		}
		public void setGroupId(String groupId) throws Exception{
			groupIdSearch().sendKeys(groupId);
			groupIdSearch().sendKeys(Keys.ENTER);
			logger.info("        Input GroupId in selectbox");
		}
		
		public void clickGroupIdList(){
			groupIdDropdown().click();		
			logger.info("        Click on Group ID dropdown list");
		}
		
		public void setPayorID(String payorId) throws Exception{
			addPayorId().sendKeys(payorId);
			logger.info("        Input PayorId into PayorId field");
		}
		public void clickSaveAndClearBtn() throws Exception{
			saveAndClearBtn().click();
			logger.info("        Click on Save And CLear button");
		}
	
	
	public void inputTestName(String testName) throws Exception{
		testNameInput().sendKeys(testName);
		logger.info("        Input test Name : " + testName);
	}
	
	public void inputTestNote(String testNote){
		testNote().sendKeys(testNote);
		logger.info("        Input test Note : " + testNote);
	}
	
	public void inputTestCodeId(String testCodeId){
		testId().sendKeys(testCodeId);
		testId().sendKeys(Keys.TAB);
		
		logger.info("        Input TestCode ID : " + testCodeId);
	}
	
	
	
	
}
