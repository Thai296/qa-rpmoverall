package com.overall.fileMaintenance.pricing.testCode;

import com.eviware.soapui.support.StringUtils;
import com.mbasys.mars.ejb.entity.dept.Dept;
import com.mbasys.mars.ejb.entity.prc.Prc;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrGrp.PyrGrp;
import com.mbasys.mars.ejb.entity.test.Test;
import com.mbasys.mars.ejb.entity.testDt.TestDt;
import com.mbasys.mars.ejb.entity.testProf.TestProf;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.overall.utils.TestCodeUtils;
import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.mars.dao.IGenericDaoXifinRpm;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.platform.dao.IGenericDaoPlatform;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.rpm.*;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class TestCode 
{
	private static final Logger LOG = Logger.getLogger(TestCode.class);
	
	private final RemoteWebDriver driver;
	private final Configuration config;
	private final WebDriverWait wait;
	private final IGenericDaoPlatform daoManagerPlatform;
	private final IGenericDaoXifinRpm daoManagerXifinRpm;
	private PayorDao payorDao;
	private final TestCodeUtils testCodeUtils;
	private TestDao testDao;
	private DepDao depDao;
	private PrcDao prcDao;
	private RandomCharacter randomCharacter;
	private RpmDao rpmDao;
	private int procTypeId;

	public TestCode(RemoteWebDriver driver, Configuration config, WebDriverWait wait)
	{
		this.driver = driver;
		this.config = config;
		this.daoManagerPlatform = new DaoManagerPlatform(config.getRpmDatabase());
		this.daoManagerXifinRpm = new DaoManagerXifinRpm(config.getRpmDatabase());
		this.testCodeUtils = new TestCodeUtils(driver);
		this.wait = wait;
		this.payorDao= new PayorDaoImpl(config.getRpmDatabase());
		this.depDao= new DepDaoImpl(config.getRpmDatabase());
		this.testDao= new TestDaoImpl(config.getRpmDatabase());
		this.prcDao= new PrcDaoImpl(config.getRpmDatabase());
		this.randomCharacter = new RandomCharacter();
		this.rpmDao = new RpmDaoImpl(config.getRpmDatabase());
	}

	public WebElement testId(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupTestAbbrv")));
	}
	
	public WebElement tabs(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('div.tabContainer')).get(0)");		
	}
	
	public List<WebElement> loadOption(){
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name("loadOption")));
	}
	
	public WebElement loadOptionRadioBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("loadOption")));
	}
	
	public WebElement effDate(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("testCode.effDate")));		
	}
	
	public WebElement selectEffDate(){
//		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('select#effDate')).get(0)");	
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='effDate']")));
	}
	
	public WebElement currEffDate(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#effDate')[0]");
	}
	
	public WebElement labTestCodeID(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("oldBillingTestNum")));
	}
	
	/*public WebElement okBtn(){
		//return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('button.btn_submit lockDownOnSubmit')).get(0)");
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/div[4]/div[3]/div/button[1]"));
	}	*/
	
	public WebElement singleTestRadio() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='profileNoLoadDialog']//input[@value='SingleTest']")));
	}

	public WebElement profileTestRadio() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='profileNoLoadDialog']//input[@value='Profile']")));
	}
	public WebElement noLoadTestRadio() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='profileNoLoadDialog']//input[@value='Noload']")));
	}

	public WebElement labelTest() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("labelTest")));
	}

	public WebElement createOptionOKBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Create options']/parent::div/following-sibling::div[contains(@class, 'ui-dialog-buttonpane')]//*[text()='OK']")));
	}
	public WebElement createScreen() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='profileNoLoadDialog']")));
	}
	public WebElement noLoadTestScreen() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='testCodeForm']/div[4]/div[3]/div[1]/div/div[1]/div/div[1]/span")));
	}
	public WebElement effectiveDatelabel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='effDateLb']")));
	}
	
	public WebElement testName(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
	}
	
	public WebElement testNote() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testNotes")));
	}
	public WebElement saveAndClearButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	public WebElement LabelTest() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("labelTest")));
	}
	public WebElement testCodePageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".platormPageTitle")));
	}
	
	public WebElement testIDLabel() {
		return  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='testAbbrv']")));
	}
	
	public WebElement deleteCheckbok() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("delCheck")));
	}
	//TestID input field
		public WebElement testIdInput(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupTestAbbrv")));
		}
		
		public WebElement resetBtn(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
		}
		
		public WebElement saveAndClearBtn(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
		}
		
		public WebElement effDateDropDown(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='effDate']")));
		}
		
		public WebElement addEffDate(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("creEffDate")));
		}
		
		public WebElement testNameInput(){
			return wait.until(ExpectedConditions.presenceOfElementLocated((By.id("testName"))));
		}
		
		public WebElement expDateInput(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("expDate")));
		}
		
		public WebElement selectTestType(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testType")));
		}
		
		public WebElement selectDeps(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deps")));
		}
		
		public WebElement labTestInput(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("oldBillingTestNum")));
		}
		
		public WebElement maxDis(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("maxDiscPct")));
		}				
		
	public WebElement saveInProgressInfoText(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messagefor_message0"))); 
	}	
		public WebElement componentTab(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Components']")));
		}
		
		public WebElement componentTable(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_componentTable")));
		}
		
		public WebElement addComponent() {
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='add_tbl_componentTable']/div")));
		}
		
		public WebElement testID(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testAbbrev")));
		}
		
	public WebElement verifyTestIDinTestCodepage(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[@id='testAbbrv']")));
	}
			
		public WebElement okBtn() {
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
		}
		
		public WebElement cancelBtn() {
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
		}
		
		public WebElement testIdTable(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-describedby='tbl_componentTable_testAbbrev']")));
		}
		
		public WebElement payorGroupTbl(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_pyrGrpExclTable']/div[1]")));
		}
		
		public WebElement addPayorGroup() {
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='add_tbl_pyrGrpExclTable']/div")));
		}
		
		public WebElement addPayorExclusion(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_pyrExclTable']/div")));
		}
		
		public WebElement groupIdDropdown(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_pyrGrpId']/a")));
		}
		public WebElement payorGroupExclusionsByRow(int row){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_pyrGrpExclTable']//tr[" + row + "]/td[@aria-describedby='tbl_pyrGrpExclTable_pyrGrpId']")));
		}
		public WebElement groupIdSearch(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));
		}
		public WebElement addPayorId(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrAbbrv")));
		}
		
		public WebElement payorName(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='jqg1']/td[3]")));
		}
		public WebElement payorTbl(){
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_pyrExclTable")));
		}
		
	public WebElement payorIDOfPayorExclusionsByRow(int row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_pyrExclTable']//tr[" + row + "]/td[@aria-describedby='tbl_pyrExclTable_pyrAbbrv']")));
	}
		
	public WebElement messageTestName(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messagefor_testName")));
	}


	public WebElement messageCreEffDate(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messagefor_creEffDate")));
	}

	public WebElement messageContent(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("xf_message_content")));
	}
	public WebElement cancelButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='profileNoLoadDialog']/following-sibling::div//span[text()='Cancel']")));
	}
	
	public WebElement searchButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("submit")));
	}
	
	public WebElement addProcedureBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='add_tbl_procedureTable']/div")));
	}
	
	public WebElement editProcedureBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='edit_tbl_procedureTable']/div")));
	}

	public WebElement alertProcedureWarning() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='alertcnt_tbl_procedureTable']/div")));
	}
	public WebElement addFacilityBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='add_tbl_facilityTable']/div")));
	}
	
	public WebElement minPriceFacilityTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[@aria-describedby='tbl_facilityTable_colMinPrice']")));
	}
	public WebElement editFacilityBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='edit_tbl_facilityTable']/div")));
	}
	public WebElement procedureTab(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Procedures']")));
	}
	
	public WebElement facilityTab() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()= 'Facilities']")));
	}
	
	public WebElement procedureCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='procId']")));
	}
	
	public WebElement procedureCodeName() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shortDescr")));
	}
	public WebElement searchProcedureIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='TblGrid_tbl_procedureTable']//*[@title='Procedure Code Search']")));
	}
	
	public WebElement searchProcedureIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcId")));
	}
	
	public WebElement labCostInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colLabCost")));
	}

	public WebElement minPriceInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colMinPrice")));
	}
	
	public WebElement recordInFacilitiesTableByRowAndAttribute(int row, String attribute) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_facilityTable']//tr[" + row + "]/td[@aria-describedby='tbl_facilityTable_col" +  attribute + "']")));
	}
	
	public WebElement testCodeIdLableInTestCodePage(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testAbbrv")));
	}
	
	public WebElement procedureTableType() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_procTypId")));
	}
	
	public WebElement GroupIdTypeModifier() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_colPyrGrpId")));
	}
	public WebElement procedureTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_procedureTable")));
	}
	
	public WebElement facilityTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_facilityTable")));
	}
	
	public WebElement performingFacility() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_perfFacAtt")));
	}
	
	public WebElement performingFacilityTableList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='select2-drop']/ul")));
	}
	
	public WebElement procedureTableList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/ul")));
	}
	
	public WebElement procedureTableInProcedureTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[@aria-describedby='tbl_procedureTable_descr']")));
	}

	public WebElement procedureCodeIdInProcedureTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[@aria-describedby='tbl_procedureTable_procId']")));
	}

	public WebElement firstCellFacilityTable() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('td[aria-describedby=\"tbl_facilityTable_colPerfFac\"]')).get(0)");
	}

	public WebElement editFacilityTableButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_facilityTable")));
	}

	public WebElement alertModifiedPopupFacilityTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("alertmod_tbl_facilityTable")));
	}

	public WebElement pleaseSelectRowPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("alertmod_tbl_facilityTable")));
	}

	public WebElement modTableFilterGrpId() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gs_colPyrGrpId']")));
	}

	public WebElement modTableFilterMod1() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gs_modId1Path']")));
	}

	public WebElement modTableFilterMod2() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gs_modId2Path']")));
	}

	public WebElement modTableFilterExprDate() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gs_colPyrExpDt']")));
	}
	
	public WebElement closeSelectRowWarningBtn(String tableId){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#" + tableId + " a[class*=\"ui-jqdialog-titlebar-close\"]')[0]");		
	}
	
	public void inputModTableFilterGrpId(String text) throws InterruptedException{
		modTableFilterGrpId().clear();
		modTableFilterGrpId().sendKeys(text);
		modTableFilterGrpId().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input " + text + " on payor group table filter");
	}

	public void inputModTableFilterMod1(String text) throws InterruptedException{
		modTableFilterMod1().clear();
		modTableFilterMod1().sendKeys(text);
		modTableFilterMod1().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input " + text + " on payor group table filter");
	}

	public void inputModTableFilterMod2(String text) throws InterruptedException{
		modTableFilterMod2().clear();
		modTableFilterMod2().sendKeys(text);
		modTableFilterMod2().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input " + text + " on payor group table filter");
	}

	public void inputModTableFilterExprDate(String text) throws InterruptedException{
		modTableFilterExprDate().clear();
		modTableFilterExprDate().sendKeys(text);
		modTableFilterExprDate().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input " + text + " on payor group table filter");
	}

	public void clearAllGrpPayorFilter() throws InterruptedException {
		//Infonam//modTableFilterGrpId().clear();
		//Infonam//modTableFilterMod1().clear();
		//Infonam//modTableFilterMod2().clear();
		//Infonam//modTableFilterExprDate().clear();
		clearData(modTableFilterGrpId());
		clearData(modTableFilterMod1());
		clearData(modTableFilterMod2());
		clearData(modTableFilterExprDate());
	}

	public void clickEditFacilityTableButton() {
		editFacilityTableButton().click();
		LOG.info("        Click on Edit Button on Facility Table");
	}

	public void clickProcedureTableInProcedureTable(){
		procedureTableInProcedureTable().click();
		LOG.info("        CLick on procedure table");
	}

	public void setexpDateInput(String date){
		expDateInput().sendKeys(date);
		LOG.info("        input Exp Date : " + date);
	}
	public void clickAddProcedureBtn(){
		addProcedureBtn().click();
		LOG.info("        Click on Add Procedures Button icon");
	}
	
	public void clickEditProcedureBtn(){
		editProcedureBtn().click();
		LOG.info("        Click on Add Procedures Button icon");
	}
	public void clickAddFacilityBtn(){
		addFacilityBtn().click();
		LOG.info("        Click on Add Facility Button icon");
	}

	public void clickCancelFacilityBtn(){
		cancelBtn().click();
		LOG.info("        Click cancel button on Add New Facility Popup");
	}

	public void clickEditFacilityBtn(){
		editFacilityBtn().click();
		LOG.info("        Click on Edit Facility Button icon");
	}
	public void clickSearchProcedureIcon(){
		searchProcedureIcon().click();
		LOG.info("        Click on Search Procedure icon");
	}
	
	public void clickProcedureTableType(){
		procedureTableType().click();
		LOG.info("        Click on Procedure Table");
	}

	//04.Fee Schedule

	public WebElement getRowOfPyrGrpExclusion(int indexRow){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_pyrGrpExclTable']/tbody/tr["+(indexRow+1)+"]")));
	}
	public WebElement getRowOfPyrExclusion(int indexRow){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_pyrExclTable']/tbody/tr["+(indexRow+1)+"]")));
	}

	public WebElement FSIdFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_prcAbbrev")));
	}

	public WebElement editPopupFeeSchedule(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_feeScheduleTable")));
	}

	public void inputFSIdFilter(String value) {
		FSIdFilter().sendKeys(value);
		FSIdFilter().sendKeys(Keys.TAB);
		LOG.info("        Input " + value + " Fee Sched ID Filter ");
	}

	/*05 Component*/
	public WebElement unitsComponent(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='units']")));
	}

	public WebElement testIDInProcedureTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[@aria-describedby='tbl_componentTable_testAbbrev']")));
	}

	public WebElement addComponentDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='editcnttbl_componentTable']")));
	}

	public WebElement editComponentDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='editcnttbl_componentTable']")));
	}

	public WebElement componentDialogFormError(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}

	public WebElement delCheckComponent(){
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='delCheck']")));
	}

	public WebElement testIdFilterComponent(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='gs_testAbbrev']")));
	}

	public WebElement nameFilterComponent(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='gs_name']")));
	}

	public void setTestIdFilterComponent(String testId) throws InterruptedException{
		testIdFilterComponent().sendKeys(testId);
		testIdFilterComponent().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        input test Id :" + testId);
	}

	public void setNameFilterComponent(String name) throws InterruptedException{
		nameFilterComponent().sendKeys(name);
		nameFilterComponent().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        input Name :" + name);
	}

	public void setUnitsComponent(String units){
		unitsComponent().sendKeys(units);
		LOG.info("        Input units in component : " + units);
	}

	public WebElement EffDateColumn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jqgh_tbl_feeScheduleTable_effDt")));
	}

	public WebElement editXrefButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_feeScheduleTable")));
	}

	public WebElement alertpopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("alerthd_tbl_feeScheduleTable")));
	}

	public WebElement errorList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sectionServerMessages']/div/div/ul/li/span")));
	}

	public void clickEditXrefButton() {
		editXrefButton().click();
		LOG.info("        Click on Edit button");
	}
	public void clickEffDateColumn() {
		EffDateColumn().click();
		LOG.info("        Click Eff Date Column");
	}
	public void clickCancelCompBtn(){
		cancelBtn().click();
		LOG.info("        Click on OK button to create new component");
	}

	//06_Xref
	public WebElement xrefTab(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@id='ui-id-6']")));
	}

	public WebElement tblXrefTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_testXrefTable")));
	}
	public WebElement crossReffElement(String crossReff){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@id='tbl_testXrefTable']//td[@title='" + crossReff + "']")));
	}

	public WebElement addNewXrefBtn (){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_testXrefTable']/div")));
	}

	public WebElement errorPopupAddNewXref() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messagefor_colEffDtX")));
	}

	public WebElement crossReferenceDescriptionGroup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='s2id_colRefIdXValue']/a")));
	}

	public WebElement effDtXref(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='colEffDtX']")));
	}

	public WebElement expDtXref(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='colExpDt']")));
	}

	public WebElement alertXrefDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("alertmod_tbl_testXrefTable")));
	}

	public WebElement alertXrefDialogMessage(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("alertcnt_tbl_testXrefTable")));
	}

	public WebElement crossRefDesList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/ul")));
	}

	public WebElement xrefDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_testXrefTable")));
	}

	public WebElement crossReferenceDescrDropdownText(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_colRefIdXValue']/a/span[1]")));
	}

	public WebElement deleteCheckBoxXref(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colIsDeleteX")));
	}

	public WebElement getRowXrefTable(int indexOfRow){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_testXrefTable']/tbody/tr["+(indexOfRow)+"]")));
	}

	public WebElement editXrefBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_testXrefTable")));
	}

	public WebElement xrefDialogFormError(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}

	public WebElement colRefIdXValue(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('td[aria-describedby=tbl_testXrefTable_colRefIdXValue]')).get(0)");
	}

	public WebElement xrefTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='jqg2']/td[2]")));
	}

	public WebElement hideGridXrefTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_testXrefTable']/div[1]/a/span")));
	}

	public WebElement wrapperGridXrefTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_testXrefTable']/div[2]")));
	}

	public String getTextFromXrefTable(int row, int col){
		return getRowXrefTable(row).findElement(By.xpath("td["+col+"]")).getText();
	}

	public WebElement filterXrefDescrInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_colRefIdXValue")));
	}

	public WebElement filterXrefEffDateInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_colEffDtX")));
	}

	public WebElement effDateColumnCrossReferenceTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jqgh_tbl_testXrefTable_colEffDtX")));
	}

	public WebElement editXrefDialog() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edithdtbl_testXrefTable")));
	}

	public WebElement getRowOfCrossRefTable(int indexRow){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_testXrefTable']/tbody/tr["+(indexRow+1)+"]")));
	}

	public WebElement addNewCrossRefField() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_colRefIdXValue")));
	}

	public void clickEffDateColumnCrossReferenceTable() throws InterruptedException{
		effDateColumnCrossReferenceTable().click();
		Thread.sleep(1000);
		LOG.info("        Clicked on effective date column in cross reference table");
	}

	public void filterXrefEffDate(String eff) throws InterruptedException{
		filterXrefEffDateInput().clear();
		filterXrefEffDateInput().sendKeys(eff);
		filterXrefEffDateInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		LOG.info("        Filter Xref effective date : " + eff);
	}

	public void filterXrefDescr(String descr) throws InterruptedException{
		filterXrefDescrInput().clear();
		filterXrefDescrInput().sendKeys(descr);
		filterXrefDescrInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		LOG.info("        Filter Xref Descr : " + descr);
	}

	public void clickHideGridXrefTable() throws InterruptedException{
		hideGridXrefTable().click();
		Thread.sleep(1000);
		LOG.info("        Clicked hide grid xref table");
	}

	public void clickEditXrefBtn(){
		editXrefBtn().click();
		LOG.info("        Clicked edit xref button");
	}

	public void setEffDtXref(String effDate){
		effDtXref().sendKeys(effDate);
		LOG.info("        Input effective date : " + effDate);
	}

	public void setExpDtXref(String exDate){
		expDtXref().sendKeys(exDate);
		LOG.info("        Input Expiration  date : " + exDate);
	}

	public void clickOkXrefBtn(){
		okBtn().click();
		LOG.info("         Click Ok Button on Add Xref pop up");
	}

	public void clickCancelXrefBtn(){
		cancelBtn().click();
		LOG.info("         Click cancel Button on Add Xref pop up");
	}

	public void clickAddNewXrefBtn(){
		addNewXrefBtn().click();
		LOG.info("          Click Add Xref button");
	}

	public void clickXrefTab(){
		xrefTab().click();
		LOG.info("          Click Xref tab");
	}
	/* 07 Exlusion*/

	public WebElement exclusionsTab(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Exclusions']")));
	}

	public WebElement pyrGrpExclusionTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_pyrGrpExclTable")));
	}
	public WebElement pyrExclusionTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_pyrExclTable")));
	}
	public WebElement PayorGroupDescription() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_pyrGrpId']/a/span[2]/b")));
	}
	public WebElement PayorGroupIDExclusionList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/ul")));
	}
	public WebElement firstCellPyrGrpExclusionTable() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('td[aria-describedby=\"tbl_pyrGrpExclTable_pyrGrpId\"]')).get(0)");
	}
	public WebElement firstCellPyrExclusionTable() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('td[aria-describedby=\"tbl_pyrExclTable_pyrAbbrv\"]')).get(0)");
	}
	public WebElement pyrGrpExclusionEditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_pyrGrpExclTable")));
	}
	public WebElement pyrGrpIdDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_pyrGrpId")));
	}
	public WebElement firstRowInPyrGrpExclusionTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pyrGrpExclTable']/tbody/tr[2]")));
	}
	public WebElement firstRowInPyrExclusionTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pyrExclTable']/tbody/tr[2]")));
	}
	public WebElement arrowShowHidePyrGrpBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_pyrGrpExclTable']/div[1]/a/span")));
	}
	public WebElement showHidePyrBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_pyrExclTable']/div[1]/a")));
	}
	public WebElement arrowShowHidePyrBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_pyrExclTable']/div[1]/a/span")));
	}
	public WebElement pyrGrpExclusionTblHeader() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_pyrGrpExclTable']/div[2]")));
	}
	public WebElement pyrGrpExclusionTblContent() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_pyrGrpExclTable']/div[3]")));
	}
	public WebElement pyrExclusionTblHeader() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_pyrExclTable']/div[2]")));
	}
	public WebElement showHidePyrGrpBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_pyrGrpExclTable']/div[1]/a")));
	}
	public WebElement pyrExclusionEditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_pyrExclTable")));
	}
	public WebElement pyrExclusionTblContent() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_pyrExclTable']/div[3]")));
	}
	public WebElement widgetOverlay() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-widget-overlay")));
	}
	public WebElement alertPyrExclusionTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("alerthd_tbl_pyrExclTable")));
	}
	public WebElement grpIdExclusionFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_pyrGrpId")));
	}
	public WebElement pyrIdExclusionFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_pyrAbbrv")));
	}
	public WebElement errorMessage() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='FormError']/td")));
	}
	public WebElement PayorGroupExclusionTabDialogFormError(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}
	public WebElement addPayorGroupExclusionDialog() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edithdtbl_pyrGrpExclTable")));
	}
	public WebElement editPayorExclusionDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='edithdtbl_pyrExclTable']")));
	}
	
	public WebElement addPayorExclusionDialog() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='edithdtbl_pyrExclTable']//*[text()='Add Record']")));
	}

	public void clickFirstCellOnPyrGrp() {
		firstCellPyrGrpExclusionTable().click();
		LOG.info("        Click on first cell on payor group table");
	}
	public void setGrpIdFilter(String input) throws InterruptedException {
		grpIdExclusionFilter().clear();
		grpIdExclusionFilter().sendKeys(input);
		grpIdExclusionFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Enter input into Group ID filter");
	}
	public void setPyrIdFilter(String input) throws InterruptedException {
		pyrIdExclusionFilter().clear();
		pyrIdExclusionFilter().sendKeys(input);
		pyrIdExclusionFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Enter input into Payor ID filter");
	}
	public void clickShowHidePyrBtn() throws Exception {
		showHidePyrBtn().click();
		Thread.sleep(1000);
		LOG.info("        Click on show/hide button for payor table");
	}
	public void inputPayorIDExclusion(String value){
		addPayorId().clear();
		addPayorId().sendKeys(value);
		LOG.info("        Input "+value+" to Payor ID");
	}
	public void clickPyrExclusionEditBtn() {
		pyrExclusionEditBtn().click();
		LOG.info("        Click on edit button for payor exclusion table");
	}
	public void clickShowHidePyrGrpBtn() throws Exception {
		showHidePyrGrpBtn().click();
		Thread.sleep(1000);
		LOG.info("        Click on show/hide button for payor group table");
	}
	public void clickOkPyrGrpExclusion(){
		okBtn().click();
		LOG.info("        CLick ok Pyr Grp Exclusion");
	}
	public void clickCancelPyrGrpExclusion(){
		cancelBtn().click();
		LOG.info("        CLick Cancel Pyr Grp Exclusion");
	}

	public void clickOkPyrExclusion(){
		okBtn().click();
		LOG.info("        CLick ok Pyr  Exclusion");
	}
	public void clickCancelPyrExclusion(){
		cancelBtn().click();
		LOG.info("        CLick Cancel Pyr  Exclusion");
	}

	public void clickPyrGrpExclusionEditBtn() {
		pyrGrpExclusionEditBtn().click();
		LOG.info("        Click on edit button for payor group exclusion table");
	}

	/*08 Suppress Cross-Test Consolidations*/
	public WebElement addSCTButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_consTable")));
	}
	public WebElement sCTTab(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ui-id-8")));
	}
	public WebElement sCTTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_consTable")));
	}
	public WebElement effDtOnSCTT() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("effDt")));
	}
	public WebElement payorIdSuppressCTTable(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('td[aria-describedby=tbl_consTable_pyrAbbrv]')).get(0)");
	}
	public WebElement expDtOnSCTT() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("expDt")));
	}	
	
	public WebElement getRowOnTableSCT(int index) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_consTable']/tbody/tr["+index+"]")));
	}
	
	public WebElement hideGridSCTTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_consTable']/div[1]/a/span")));
	}
	public WebElement wrapperTableSCT() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_consTable']/div[3]")));
	}
	public WebElement payorSCTFormError() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}
	public WebElement payorSCTFormErrorMsg() {
		return payorSCTFormError().findElement(By.tagName("td"));
	}
	public WebElement deleteCheckBoxSCT() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("delCheck")));
	}
	public WebElement payorFilterSCT() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_consTable']/div[2]/div/table/thead/tr[2]/th[2]/div/input")));
	}
	public WebElement effFilterSCT() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_consTable']/div[2]/div/table/thead/tr[2]/th[3]/div/input")));
	}
	public WebElement expFilterSCT() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_consTable']/div[2]/div/table/thead/tr[2]/th[4]/div/input")));
	}
	public WebElement addNewSCTFPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editcnttbl_consTable")));
	}
	public void filterPayorIDSCT(String filter){
		payorFilterSCT().clear();
		payorFilterSCT().sendKeys(filter);
		payorFilterSCT().sendKeys(Keys.TAB);
		LOG.info("        Filter Payor ID in Suppress Table " + filter);
	}
	public void filterEffSCT(String filter){
		effFilterSCT().clear();
		effFilterSCT().sendKeys(filter);
		effFilterSCT().sendKeys(Keys.TAB);
		LOG.info("        Filter Effective date in Suppress Table " + filter);
	}
	public void filterExpSCT(String filter){
		expFilterSCT().clear();
		expFilterSCT().sendKeys(filter);
		expFilterSCT().sendKeys(Keys.TAB);
		LOG.info("        Filter Exp date in Suppress Table " + filter);
	}
	public void clickOkOnPayorSCT(){
		okBtn().click();
		LOG.info("        Clicked OK on add/edit Payor Suppress Cross-Test Consolidations.");
	}
	public void clickCancelOnPayorSCT(){
		cancelBtn().click();
		LOG.info("        Clicked Cancel on add/edit Payor Suppress Cross-Test Consolidations.");
	}
	public void clickHideGridSCTTable() throws InterruptedException{
		hideGridSCTTable().click();
		Thread.sleep(1000);
		LOG.info("        Clicked hide/show Suppress Cross-Test Consolidations table.");
	}
	public void clickAddSCTButton(){
		addSCTButton().click();
		LOG.info("        Clicked add Suppress Cross-Test Consolidations button.");
	}
	public void clickSCTTab(){
		sCTTab().click();
		LOG.info("        Clicked Suppress Cross-Test Consolidations tab.");
	}
	public void inputEffDtOnSCTT(String eff){
		effDtOnSCTT().sendKeys(eff);
		LOG.info("        Input "+eff+" on effective date Suppress Cross-Test Consolidations.");
	}
	public void inputExpDtOnSCTT(String exp){
		expDtOnSCTT().sendKeys(exp);
		LOG.info("        Input "+exp+" on exp date Suppress Cross-Test Consolidations.");
	}

	//09 help button 
	public WebElement testCodeHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='testCodeForm']/div[5]/div[2]/section/div/div[1]/a")));
	}

	public WebElement effDateHelpIcon() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='testCodeForm']/div[5]/div[3]/div[2]/div[2]/div[1]/div/section/div/div[1]/a")));
	}

	//All actions and functions
	public void clickTestCodeHelpIcon() {
		testCodeHelpIcon().click();
		LOG.info("        Clicked on Help icon in Test code form");
	}

	public void clickeffDateHelpIcon() {
		effDateHelpIcon().click();
		LOG.info("        Clicked on Help icon in Effective Date Information section");
	}

	public boolean checkDeleteRowInPyrGrpExclusionTbl() {
		boolean deleted = false;
		String disabledClass = firstRowInPyrGrpExclusionTbl().getAttribute("class");
		if (disabledClass.contains("rowMarkedForDelete")) {
			deleted = true;
		}

		return deleted;
	}

	public boolean checkDeleteRowInPyrExclusionTbl() {
		boolean deleted = false;
		String disabledClass = firstRowInPyrExclusionTbl().getAttribute("class");
		if (disabledClass.contains("rowMarkedForDelete")) {
			deleted = true;
		}

		return deleted;
	}

	public String selectFirstRowExclusionTbl(SeleniumBaseTest b, String testCodeId, String testDb) throws Exception {
		String name;
		if (firstCellPyrExclusionTable() != null){
			firstCellPyrExclusionTable().click();
			name = firstCellPyrExclusionTable().getText();
			LOG.info("        Select first row in facility table");
		} else {
			List<String> payorExclusion = addPayorExclusions(2, b, true, testDb);
			clickSaveAndClearBtn();
			Thread.sleep(3000);
			checkInputTestCodeId(testCodeId);
			//callBack this function
			return payorExclusion.get(0);
		}
		return name;
	}

	public String selectFirstRowPyrGrpExclusionTbl(SeleniumBaseTest b, String testCodeId, String testDb) throws Exception {
		String name;
		if (firstCellPyrGrpExclusionTable() != null){
			name = firstCellPyrGrpExclusionTable().getText();
			LOG.info("        Select first row in facility table");
			Thread.sleep(1000);
		} else {
			List<String> payorInfo =  addPayorGroupExclusions(1, b,true, testDb);
			clickSaveAndClearBtn();
			checkInputTestCodeId(testCodeId);
			clickExclusionTab();
			name = payorInfo.get(0);
		}
		return name;
	}

	public String selectPayorGroupDescriptionExclusionTab(String value){
		PayorGroupDescription().click();
		WebElement list = PayorGroupIDExclusionList();
		List<WebElement> allRows = list.findElements(By.tagName("li"));
		String xref = "";

		for (WebElement row : allRows) {
			xref = row.getText();
			if (xref.equals(value)) {
				row.click();
				LOG.info("        Clicked Payor ID  : " + xref);
				break;
			}
		}
		return xref;
	}

	public List<String> addSuppressCT(String effDate, String expDate, boolean clickOk, TestCode testCode, SeleniumBaseTest b, String testDb) throws Exception{
		
		TimeStamp timeStamp = new TimeStamp(driver);
		List<String> result = new ArrayList<>();
		clickSCTTab();
		testCodeUtils.scrollIntoView(addSCTButton());
		clickAddSCTButton();
		String payorID = payorDao.getPayorId().getPyrAbbrv();
		Assert.assertTrue(b.isElementPresent(testCode.addPayorId(), 5));
		testCode.setPayorID(payorID);

		String effInput = effDate;
		if(effDate.length()<1) effInput = timeStamp.getCurrentDate("MM/dd/yyyy");
		Assert.assertTrue(b.isElementPresent(effDtOnSCTT(), 5));
		effDtOnSCTT().clear();
		inputEffDtOnSCTT(effInput);

		if(expDate.length()>0) {
			Assert.assertTrue(b.isElementPresent(expDtOnSCTT(), 5));
			expDtOnSCTT().clear();
			inputExpDtOnSCTT(expDate);
		}
		if(clickOk) testCode.okBtn().click();

		result.add(payorID);
		result.add(effInput);
		return result;
	}

	public List<String> editSuppressCT(int indexRow,String effDate, String expDate, boolean clickOk, SeleniumBaseTest b) throws Exception{
		List<String> result = new ArrayList<>();
		clickSCTTab();
		
		testCodeUtils.scrollIntoView(getRowOnTableSCT(indexRow+1));
		testCodeUtils.doubleClickOnRowByIndex(sCTTable(), indexRow);

		if(effDate.length()>0){
			Assert.assertTrue(b.isElementPresent(effDtOnSCTT(), 5));
			effDtOnSCTT().clear();
			inputEffDtOnSCTT(effDate);
		}
		effDate = effDtOnSCTT().getAttribute("value");

		if(expDate.length()>0) {
			Assert.assertTrue(b.isElementPresent(expDtOnSCTT(), 5));
			expDtOnSCTT().clear();
			inputExpDtOnSCTT(expDate);
		}
		expDate = expDtOnSCTT().getAttribute("value");

		String payorID = addPayorId().getAttribute("value"); 

		if(clickOk) okBtn().click();

		result.add(effDate);
		result.add(expDate);
		result.add(payorID);

		return result;
	}

	public List<String> addPayorGroupExclusions(int numberOfPyrGrp,SeleniumBaseTest b,Boolean isClickOkbtn, String testDb) throws Exception{
		List<String> listPayorGroupAdded = new ArrayList<>();

		b.isElementPresent(exclusionsTab(), 5);
		clickExclusionTab();
		boolean isDuplicateID = false;
		for (int i=1; i<= numberOfPyrGrp; i++) {
			testCodeUtils.scrollIntoView(addPayorGroup());
			clickAddPayorGroupBt();
			String PyrGrpExclusionID;
			do {
				PyrGrpExclusionID = daoManagerXifinRpm.getPayorGroupExclusion(testDb);
				isDuplicateID = b.getColumnValue(pyrGrpExclusionTbl(), PyrGrpExclusionID);
				if (isDuplicateID) LOG.info("        Skip Payor Group ID : " + PyrGrpExclusionID + " because it's duplicate.");
			} while (isDuplicateID);

			selectPayorGroupDescriptionExclusionTab(PyrGrpExclusionID);
			listPayorGroupAdded.add(PyrGrpExclusionID);
			if (isClickOkbtn) {
				clickOkPyrGrpExclusion();
			} else {
				clickCancelPyrGrpExclusion();
			}
		}
		return listPayorGroupAdded;
	}

	public List<String> addPayorExclusions(int numberOfPyrId,SeleniumBaseTest b,Boolean isClickOkbtn, String testDb) throws Exception{
		List<String> listPayorIdAdded = new ArrayList<>();
		
		b.isElementPresent(exclusionsTab(), 5);
		clickExclusionTab();
		boolean isDuplicateID = false;

		for (int i = 1; i <= numberOfPyrId; i++) {
			testCodeUtils.scrollIntoView(addPayorExclusion());
			clickAddPayorExclusionBtn();
			String PyrExclusionID;
			do {
				List<String> listPayorID;
				listPayorID = daoManagerXifinRpm.getPayorIdExclusion(testDb);
				PyrExclusionID = listPayorID.get(1);
				isDuplicateID = b.getColumnValue(pyrExclusionTbl(), PyrExclusionID);
				if (isDuplicateID) LOG.info("        Skip Payor ID : " + PyrExclusionID + " because it's duplicate.");
			} while (isDuplicateID);

			inputPayorIDExclusion(PyrExclusionID);
			listPayorIdAdded.add(PyrExclusionID);
			if (isClickOkbtn) {
				clickOkPyrExclusion();
			} else {
				clickCancelPyrExclusion();
			}
		}
		return listPayorIdAdded;
	}

	public List<List<String>> addXref(int numberOfXref,SeleniumBaseTest b,String effDate, String expDate, boolean confirm, String testDb) throws Exception{
		List<String> listXrefChild = new ArrayList<>();
		List<List<String>> listXrefssAdded = new ArrayList<>();
		
		clickXrefTab();
		boolean isDuplicateID = false;
		for (int i=1; i<= numberOfXref; i++) {
			testCodeUtils.scrollIntoView(addNewXrefBtn());
			clickAddNewXrefBtn();
			String crossReferenceDescription;
			do {
				List<String> listPayorInfo= daoManagerXifinRpm.getCrossReferenceDescXref(testDb);
				crossReferenceDescription = listPayorInfo.get(1);
				isDuplicateID = b.getColumnValue(tblXrefTable(), crossReferenceDescription);
				if (isDuplicateID) LOG.info("        Skip XRef : " + crossReferenceDescription + " because it's duplicate.");
			} while (isDuplicateID);

			selectCrossReferenceDescription(crossReferenceDescription);
			setEffDtXref(effDate);
			if (!expDate.isEmpty())setExpDtXref(expDate);
			listXrefChild.add(crossReferenceDescription);
			listXrefChild.add(effDate);
			listXrefChild.add(expDate);
			listXrefssAdded.add(listXrefChild);
			if(confirm)clickOkXrefBtn();else clickCancelXrefBtn();	
		}
		return listXrefssAdded;
	}


	public String selectCrossReferenceDescription(String value){
		crossReferenceDescriptionGroup().click();
		WebElement list = crossRefDesList();
		List<WebElement> allRows = list.findElements(By.tagName("li"));
		String xref = "";

		for (WebElement row : allRows) {
			xref = row.getText();
			if (xref.equals(value)) {
				row.click();
				LOG.info("        Clicked xref  : " + xref);
				break;
			}
		}
		return xref;
	}

	public void searchProcedureCode(String procedureCode){
		searchProcedureIdInput().sendKeys(procedureCode);
		searchButton().click();
		LOG.info("        Search with id procedure : " + procedureCode);
	}
	
	public void editModifierGroupPayorWithBlankFields(int numRows, String testDb, String testCodeId) throws Exception {
		
		SeleniumBaseTest b = new SeleniumBaseTest();
		if (numRows <= 1) {
			String minEffDate = effDateDropDown().getText();
			String effDateInput = testCodeUtils.getNextYear(minEffDate);
			addGroupModifiers(1, b, effDateInput,true, testDb);
			clickSaveAndClearBtn();
			inputTestCodeId(testCodeId);
			editModifierGroupPayorWithBlankFields(2, testDb, testCodeId);
		} else {
			selectFirstRowInPayorGroupSpecificModifierTable();
			clickeditPayorGrpModifierButton();
			selectGroupModifier(true, 0); // Select modifier 1
			selectGroupModifier(false, 0); // Select modifier 2
		}
	}

	public boolean verifyPayorDeletedSCTTable(String payorID, String effDate, String expDate){
		// Verify that PAYOR have (ID, Eff date, Exp date) deleted from table
		int i= 1;
		String pID, pEff, pExp;
		List<WebElement> rows = sCTTable().findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			pID = getRowOnTableSCT(i).findElement(By.xpath("td[2]")).getText();
			pEff = getRowOnTableSCT(i).findElement(By.xpath("td[3]")).getText();
			pExp = getRowOnTableSCT(i).findElement(By.xpath("td[4]")).getText();
			if (pID.equalsIgnoreCase(payorID) && pEff.equalsIgnoreCase(effDate) && pExp.equalsIgnoreCase(expDate))	return false;
			i++;
		}
		return true;
	}

	public int findRowPayorInSuppress(String payorId, String effDate){
		// Find row index of payor added to table suppress
		int i= 1;
		String pID, pEff;
		List<WebElement> rows = sCTTable().findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			pID = getRowOnTableSCT(i).findElement(By.xpath("td[2]")).getText();
			pEff = getRowOnTableSCT(i).findElement(By.xpath("td[3]")).getText();
			if (i>1 && (pID.equalsIgnoreCase(payorId) && pEff.equalsIgnoreCase(effDate))) return i;
			i++;
		}
		return 0;
	}

	public String getTextFromTableSCT(int indexCol,int indexRow){
		return getRowOnTableSCT(indexRow).findElement(By.xpath("td["+indexCol+"]")).getText();
	}

	/*
	public boolean verifyFilterSCT(String payorID, String effDate, String expDate){
		// Verify that table Suppress filter correct with payor id, eff date, exp date
		int i= 2;
		String pID, pEff, pExp;
		List<WebElement> rows = sCTTable().findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			WebElement rowOnTableSCT = getRowOnTableSCT(i);			
			pID = rowOnTableSCT.findElement(By.xpath("td[2]")).getText().trim();			
			pEff = rowOnTableSCT.findElement(By.xpath("td[3]")).getText().trim();
			pExp = rowOnTableSCT.findElement(By.xpath("td[4]")).getText().trim();			

			if ((i>2) && (!pID.equalsIgnoreCase(payorID)) || (!pEff.equalsIgnoreCase(effDate)) || (!pExp.equalsIgnoreCase(expDate))) return false;			
			i++;
		}
		
		return true;
	}
	*/
	
	public boolean verifyFilterSCT(String payorID, String effDate, String expDate){
		// Verify that table Suppress filter correct with payor id, eff date, exp date
		int i= 2;
		String pID, pEff, pExp;
		boolean flag = false;
		List<WebElement> rows = sCTTable().findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			WebElement rowOnTableSCT = getRowOnTableSCT(i);			
			pID = rowOnTableSCT.findElement(By.xpath("td[2]")).getText().trim();			
			pEff = rowOnTableSCT.findElement(By.xpath("td[3]")).getText().trim();
			pExp = rowOnTableSCT.findElement(By.xpath("td[4]")).getText().trim();			
			
			if ((pID.equalsIgnoreCase(payorID) && (pEff.equalsIgnoreCase(effDate)) && (pExp.equalsIgnoreCase(expDate)) )) {
				LOG.info("        The filters in Suppress Cross-Test Consolidations table work properly.");
				flag = true;	
				break;
			}
			else{
				i++;
			}
		}
		
		return flag;
	}

	public List<String> getAllFieldsInFirstRow(WebElement tableElement) {
		List<String> result = new ArrayList<>();

		// Get row count
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));

		// loop through each row
		for (WebElement row : rows) {
			// get column count
			List<WebElement> cols = row.findElements(By.xpath("td"));
			int i = 1;
			for (WebElement col : cols) {
				if (i == 12) break;
				if(!col.getText().isEmpty()){
					result.add(col.getText());
				}
				i++;
			}
		}

		return result;

	}

	public List<List<String>> addComponent(int numberOfXref,SeleniumBaseTest b,String effDate, String Units, boolean validTestId, String typeTest, boolean confirm, String testDb) throws Exception{
		List<String> listComponentChild = new ArrayList<>();
		List<List<String>> listComponentsAdded = new ArrayList<>();
		List<String> listPayorInfo;
		Assert.assertTrue(b.isElementPresent(componentTab(), 5));
		clickComponentTab();
		boolean isDuplicateID = false;

		for (int i=1; i<= numberOfXref; i++) {
			Assert.assertTrue(b.isElementPresent(addComponent(), 5));
			testCodeUtils.scrollIntoView(addComponent());
			clickAddComponent();
			String testId;
			do {
				if(typeTest.contains("s") || typeTest.contains("S"))
					listPayorInfo = daoManagerXifinRpm.getSingleTestByEffDate(effDate, validTestId, testDb);
				else 
					listPayorInfo = daoManagerXifinRpm.getProfileTestByEffDate(effDate, validTestId, testDb);

				testId = listPayorInfo.get(1);
				isDuplicateID = b.getColumnValue(componentTable(), testId);
				if (isDuplicateID) LOG.info("        Skip Payor : " + testId + " because it's duplicate.");
			} while (isDuplicateID);

			setComponentTestId(testId);
			if(!Units.isEmpty())setUnitsComponent(Units);
			listComponentChild.add(testId);
			listComponentChild.add(Units);
			listComponentChild.add(listPayorInfo.get(2));//test code name
			listComponentChild.add(listPayorInfo.get(3));//eff date

			listComponentsAdded.add(listComponentChild);
			if(confirm)clickOkCompBtn();else clickCancelCompBtn();	
		}
		return listComponentsAdded;
	}

	public void selectFirstRowInFacilityTable() throws Exception {
		SeleniumBaseTest b = new SeleniumBaseTest();
		if (firstCellFacilityTable() != null){
			firstCellFacilityTable().click();
			LOG.info("        Select first row in facility table");
		} else {
			clickAddFacilityBtn();
			selectPerformingFacility(2);
			String minPrice = "1";
			String labCost  = minPrice;
			Assert.assertTrue(b.isElementPresent(labCostInput(), 5));
			inputLabCost(labCost);
			Assert.assertTrue(b.isElementPresent(minPriceInput(), 5));
			inputMinPrice(minPrice);
			Assert.assertTrue(b.isElementPresent(okBtn(), 5));
			clickOkFacilityBtn();

			//callBack this function
			selectFirstRowInFacilityTable();
		}
	}

	public void selectProcedureTable(String procedureType) throws InterruptedException{
		Actions actions = new Actions(driver);
		procedureTableType().findElement(By.tagName("a")).click();
		WebElement list = procedureTableList(); 
		// Now get all the li elements from the ul list
		List<WebElement> allRows = list.findElements(By.tagName("li")); 
		// And iterate over them, getting the cells 
		for (WebElement row : allRows) {
	        if (row.getText().equalsIgnoreCase(procedureType)) {
			row.click();
			actions.click().build().perform();
			LOG.info("        Clicked Procedure Table : " + procedureType);
			break;
			}
	    }
	}
	
	public void selectPerformingFacility(String performingFacility){
		performingFacility().findElement(By.tagName("a")).click();
		WebElement list = performingFacilityTableList();
		// Now get all the li elements from the ul list
		List<WebElement> allRows = list.findElements(By.tagName("li")); 
		// And iterate over them, getting the cells 
		for (WebElement row : allRows) {
	        if (row.getText().equalsIgnoreCase(performingFacility)) {
			row.click();
			LOG.info("        Clicked Performing Facility Table : " + performingFacility);
			break;
			}
	    }
	}
	
	public String selectPerformingFacility(int index){
		performingFacility().findElement(By.tagName("a")).click();
		WebElement list = performingFacilityTableList();
		// Now get all the li elements from the ul list
		List<WebElement> allRows = list.findElements(By.tagName("li"));
		String performingFac = "";
		int i=0;
		// And iterate over them, getting the cells 
		for (WebElement row : allRows) {
	        if (i == index) {
        	performingFac = row.getText();
			row.click();
			LOG.info("        Clicked Performing Facility Table : " + performingFac);
			break;
			}
	        i++;
	    }
		return performingFac;
	}
	
	public void selectGroupIdModifier(String groupId){
		GroupIdTypeModifier().findElement(By.tagName("a")).click();
		WebElement list = procedureTableList(); 
		// Now get all the li elements from the ul list
		List<WebElement> allRows = list.findElements(By.tagName("li")); 
		// And iterate over them, getting the cells 
		for (WebElement row : allRows) {
			if (row.getText().equalsIgnoreCase(groupId)) {
				row.click();
				LOG.info("        Clicked GroupId Table : " + groupId);
				break;
			}
		}
	}
	public void clickOkProcedureBtn(){
		okBtn().click();
		LOG.info("        Click on OK button to create new Procedure");
	}
	
	public void clickResetButton(){
		resetBtn().click();
		LOG.info("        Reset clicked");
	}

	public void clickCancelProcedureBtn(){
		cancelBtn().click();
		LOG.info("        Click on cancel button to create new Procedure");
	}
	public void clickFacilityTab(){
		facilityTab().click();
		LOG.info("        Click on Facility tab");
	}
	
	public void clickProcedureTab(){
		procedureTab().click();
		LOG.info("        Click on procedure tab");
	}
	
	public void clickOkFacilityBtn(){
		okBtn().click();
		LOG.info("        Click on OK button to create new Procedure");
	}
	
	public void inputLabCost(String labCost){
		labCostInput().sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		labCostInput().sendKeys(labCost);
		LOG.info("        Entered Lab Cost: " + labCost);
	}
	
	public void inputMinPrice(String minPrice){
		minPriceInput().sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		minPriceInput().sendKeys(minPrice);
		LOG.info("        Entered Min Price: " + minPrice);
	}
	
	public void inputProcedureCode(String procedureCode){
		procedureCodeInput().clear();
		procedureCodeInput().sendKeys(procedureCode);
		procedureCodeInput().sendKeys(Keys.TAB);
		LOG.info("        Input procedure code : " + procedureCode);
	}
	
//	public void clickCancelBtn(){
//		cancelButton().click();
//		LOG.info("        Clicked Cancel button");
//	}
	public void clickCreateOption() throws InterruptedException{
		createOptionOKBtn().click();
		Thread.sleep(1000);
			LOG.info("        Clicked OK button ");
		}
		
		// set testId
		public void setTestId(String testId){
				testIdInput().sendKeys(testId);
				testIdInput().sendKeys(Keys.TAB);
				LOG.info("        Entered TestId: " + testId);
		}
		
		public void setTestName(String testName){
			testNameInput().sendKeys(testName);
			LOG.info("        Entered TestName: "  + testName);
		}
		
		public void selectTestType(String testType){
			selectTestType().sendKeys(testType);
			LOG.info("        Select testType: " + testType);
		}
		
		public void setEffectiveDate(String effDate){
			addEffDate().sendKeys(effDate);
			addEffDate().sendKeys(Keys.TAB);
			LOG.info("        Entered Effective Date: " + effDate);
		}
		
		public void clickAddComponent() {
			addComponent().click();
			LOG.info("        Clicked on Add Component icon");
		}
		
		public void setComponentTestId(String testID){
			testID().sendKeys(testID);
			testID().sendKeys(Keys.TAB);
			LOG.info("        Add new TestId for component"  + testID);
		}
		
		public void clickOkCompBtn(){
			okBtn().click();
			LOG.info("        Click on OK button to create new component");
		}
	public void clickOkPayorBtn(){
		addPayorId().click();
		addPayorId().sendKeys(Keys.ENTER);
		LOG.info("        Click on OK button to create new component");
	}
		public void clickExclusionTab(){
			exclusionsTab().click();
			LOG.info("        Click on Exclusions tab");
		}
		public void clickAddPayorGroupBt(){
			addPayorGroup().click();
			LOG.info("        Click on Add Payor Group icon");
		}
		public void clickAddPayorExclusionBtn(){
			addPayorExclusion().click();
			LOG.info("        Click on Add Payor Exclusion icon");
		}
		public void setGroupId(String groupId) {
			groupIdSearch().sendKeys(groupId);
			groupIdSearch().sendKeys(Keys.ENTER);
			LOG.info("        Input GroupId in selectbox");
		}
		
		public void clickGroupIdList(){
			groupIdDropdown().click();		
			LOG.info("        Click on Group ID dropdown list");
		}
		
		public void setPayorID(String payorId){
			addPayorId().sendKeys(payorId);
			LOG.info("        Input PayorId into PayorId field");
		}

	public void clickResetBtn() throws Exception{
		resetBtn().click();
		LOG.info("        Clicked on Reset button");
	}
	
	public void clickSaveAndClearBtn() throws Exception{
		saveAndClearBtn().click();
		LOG.info("        Clicked on Save And Clear button");
	}	
	
	public void inputTestName(String testName){
		testNameInput().sendKeys(testName);
		LOG.info("        Entered Test Name: " + testName);
	}
	
	public void clickComponentTab () {
		componentTab().click();
		LOG.info("        Click Component tab");
	}

	public void inputTestNote(String testNote){
		testNote().sendKeys(testNote);
		LOG.info("        Entered test Note : " + testNote);
	}
	
	public void inputTestCodeId(String testCodeId) throws Exception{
		testId().sendKeys(testCodeId);
		testId().sendKeys(Keys.TAB);
		LOG.info("        Entered TestCode ID : " + testCodeId);
	}	

	//Effective date
	public WebElement createEffDateIcon(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("creNewEffDt")));
	}

	public WebElement copyEffDt(){
//		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('select#cpyEffDt')).get(0)");
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cpyEffDt")));
	}

	public WebElement okCpyEffDate(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='effDateCopyDialog']/following-sibling::div//*[text()='OK']")));
	}

	public WebElement cancelEffCopyDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='effDateCopyDialog']/following-sibling::div//*[text()='Cancel']")));
	}

	public WebElement errorListPanel(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("serverErrorsList")));
	}

	public String getAlertTestCode(int index){
		return errorListPanel().findElement(By.xpath("//ul/li["+index+"]/span")).getText();
	}


	public WebElement procedureExpDateInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("expDt")));
	}

	public WebElement effCopyDialog(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('div[aria-describedby=\"effDateCopyDialog\"]')).get(0)");
	}

	public WebElement performingFacilityLink(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated (By.xpath("//*[@id='tbl_facilityTable']/tbody/tr["+ row +"]/td["+ col +"]")));
	}

	public WebElement editfacilityDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='edithdtbl_facilityTable']/span")));
	}

	public WebElement colFacilityInfo1(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colInfo1")));
	}

	public WebElement warningDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ui-id-11")));
	}

	public WebElement warningOkBtnDialog(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('div[aria-describedby=\"effDateDataHasChangedDialog\"] .ui-dialog-buttonset > button:nth-child(1)')).get(0)");
	}

	public WebElement warningCancelBtnDialog(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('div[aria-describedby=\"effDateDataHasChangedDialog\"] .ui-dialog-buttonset > button:nth-child(2)')).get(0)");
	}

	public WebElement modifierTab() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='ui-id-4']")));
	}

	public WebElement addNewPayorIDButton (){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_pyrModTable']/div")));
	}

	public WebElement payorIdPayorModifierTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[@aria-describedby='tbl_pyrModTable_colPyrAbbrv']")));
	}
	public WebElement addNewGroupPayorButton (){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_pyrGrpModTable']/div")));
	}

	public WebElement payorIdModifiersInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colPyrAbbrv")));
	}

	public WebElement modifierDopdownOnAddPayorID(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_modId1Path']/a")));   
	}

	public WebElement modifierDopdownInputOnAddPayorID(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));   		
	}

	public WebElement expDateOnAddNewPayor(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colPyrExpDt")));
	}

	public WebElement okButtonOnAddNewPayorID(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sData']")));
	}

	public WebElement payorSpecificModifiersTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_pyrModTable")));
	}

	public WebElement payorGroupModifiersTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_pyrGrpModTable")));
	}
	public void clickOkButtonOnAddNewPayorID(){
		okButtonOnAddNewPayorID().click();
		LOG.info("        Click Ok button");
	}

	public void setExpDateOnAddNewPayor(String date){
		expDateOnAddNewPayor().sendKeys(date);
		LOG.info("        Input " + date + " to Expiration Date");
	}

	public void setModifierDopdownOnAddPayorID(String value){
		clickModifierDopdownOnAddPayorID();
		modifierDopdownInputOnAddPayorID().sendKeys(value);
		modifierDopdownInputOnAddPayorID().sendKeys(Keys.TAB);
		LOG.info("        Input " + value + " to Modifier 1");
	}
	
	// Added by yli	
	public void setModifier1InAddRecordPopup(String value){
		modifierDopdownInputOnAddPayorID().click();
		modifierDopdownInputOnAddPayorID().sendKeys(value);
		modifierDopdownInputOnAddPayorID().sendKeys(Keys.TAB);
		LOG.info("        Entered Modifier 1: " + value);
	}		

	public void setPayorIdModifiersInput(String payorID){
		payorIdModifiersInput().clear();
		payorIdModifiersInput().sendKeys(payorID);
		payorIdModifiersInput().sendKeys(Keys.TAB);
		LOG.info("        Input " + payorID + " to Payor ID");
	}

	public void clickModifierDopdownOnAddPayorID(){
		modifierDopdownOnAddPayorID().click();		
		LOG.info("        Click on Modifier 1 dropdown");
	}

	public void clickAddPayorIDBt(){
		addNewPayorIDButton().click();
		LOG.info("        Click on Add Payor ID button");
	}

	public void clickAddGroupPayorBt(){
		addNewGroupPayorButton().click();
		LOG.info("        Click on Add Group Payor button");
	}

	public void clickModifierTab(){
		modifierTab().click();
		LOG.info("        Click on Modifier tab");
	}

	public void clickWarningOkBtbDialog(){
		warningOkBtnDialog().click();
		LOG.info("        Click on ok Button on Warning dialog");
	}

	public void clickWarningCancelBtbDialog(){
		warningCancelBtnDialog().click();
		LOG.info("        Click on Cancel Button on Warning dialog");
	}

	public void setColFacilityInfo1(String info1){
		colFacilityInfo1().sendKeys(info1);
		LOG.info("        Input information for Facility");
	}

	public void inputProcedureExpDate(String exp){
		procedureExpDateInput().sendKeys(exp);
		procedureExpDateInput().sendKeys(Keys.TAB);
		LOG.info("        Input " + exp + " to Exp date of Procedure");
	}

	public void clickokCpyEffDate(){
		okCpyEffDate().click();
		LOG.info("        Click on Ok on Copy Effective Date pop up");
	}

	public WebElement WarningCloseBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/div[2]/div[1]/a/span")));
	}

	public void clickWarningCloseBtn() throws InterruptedException{
		WarningCloseBtn().click();
		Thread.sleep(1000);
		LOG.info("        Click close warning button");
	}
	public void clickCancelEffDateCopyDialog(){
		cancelEffCopyDialog().click();
		LOG.info("        Click on cancel on Copy Effective Date pop up");
	}

	public void clickCreateEffDateIcon() {
		createEffDateIcon().click();
		LOG.info("        Click on Pencil Icon");
	}

	public String addNewFacility(WebElement tableElement, SeleniumBaseTest b) throws Exception{
		
		String facility;
		if (b.getTableTotalRowSize(tableElement) <= 1) {
			clickFacilityTab();
			testCodeUtils.scrollIntoView(addFacilityBtn());
			clickAddFacilityBtn();
			facility = selectPerformingFacility(2);
			String minPrice = "1";
			String labCost  = minPrice;
			Assert.assertTrue(b.isElementPresent(labCostInput(), 5));
			inputLabCost(labCost);
			Assert.assertTrue(b.isElementPresent(minPriceInput(), 5));
			inputMinPrice(minPrice);
			Assert.assertTrue(b.isElementPresent(okBtn(), 5));
			clickOkFacilityBtn();
		}
		else{
			clickFacilityTab();
			facility = performingFacilityLink(2, 3).getText();
		}
		return facility;
	}

	public void clickDoubleOnTable(WebElement tableElement, String columnValue, SeleniumBaseTest b ) throws Exception{
		b.selectColumnValue(tableElement, columnValue);
	}

	//End Effective date

	//Procedure Tab
	public WebElement editProcPopup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edithdtbl_procedureTable")));
	}

	public WebElement proCodeErrorMessageDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[@class='ui-state-error']")));
	}

	public WebElement addRecordProcedurePopUp(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(.,'Add Record')]")));
	}

	public WebElement editRecordProcedurePopUp(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(.,'Edit Record')]")));
	}

	public WebElement sortByNameOnProcedureTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='jqgh_tbl_procedureTable_shortDescr']")));
	}

	public WebElement closeErrorMessage(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='xf_message_close']")));
	}

	public WebElement procNamelbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shortDescr")));
	}

	public WebElement hideShowProcedureTableIcon(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_procedureTable']/div[1]/a")));
	}

	public WebElement wrapperOfProcedureTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_procedureTable']/div[3]")));
	}

	public WebElement procTableFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_procedureTable']/div[2]/div/table/thead/tr[2]/th[3]/div/input")));
	}

	public WebElement procedureDialogFormError(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}

	public WebElement cancelProcedureDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
	}
	public WebElement procNameFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_shortDescr")));
	}

	public WebElement procCodeFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_procedureTable']/div[2]/div/table/thead/tr[2]/th[4]/div/input")));
	}

	public WebElement expDateFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_expDt")));
	}

	public void inputExpDateFilter(String text){
		expDateFilter().clear();
		expDateFilter().sendKeys(text);
		expDateFilter().sendKeys(Keys.TAB);
		LOG.info("        Input " + text + " on Expiration Date");
	}

	public void setProcNameFilter(String procName) throws InterruptedException{
		procNameFilter().clear();
		procNameFilter().sendKeys(procName);
		procNameFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input "+procName+" to Procedure Name");
	}

	public void clearData(WebElement element) throws InterruptedException{
		element.clear();
		element.sendKeys(Keys.CLEAR);
		element.sendKeys(Keys.TAB);
		Thread.sleep(1000);
		LOG.info("        Clear data");
	}

	public void setProcCodeFilter(String procCode) throws InterruptedException{
		procCodeFilter().clear();
		procCodeFilter().sendKeys(procCode);		
		procCodeFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input "+procCode+" to Procedure Code");
	}

	public void inputProcTableFilter(String text) throws InterruptedException{
		procTableFilter().clear();
		procTableFilter().sendKeys(text);
		procTableFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input " + text + " on procedure table filter");
	}

	public void clickHideShowProcedureTableIcon() throws InterruptedException{
		hideShowProcedureTableIcon().click();
		Thread.sleep(1000);
		LOG.info("        Clicked on hide/show procedure table");
	}

	public void clickcloseErrorMessage() throws InterruptedException{
		closeErrorMessage().click();
		Thread.sleep(1000);
		LOG.info("        Click Close Error message");
	}

	public void clicksortNameOnProcedureTable(){
		sortByNameOnProcedureTable().click();
		LOG.info("        Click Sort Name on procedure Table");
	}

	public void clearInputProcedureCode() throws Exception{
		procedureCodeInput().clear();
		procedureCodeInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Clear input procedure code and tab out");
	}

	public void clickCancelProcedureDialog(){
		cancelProcedureDialog().click();
		LOG.info("        Clicked cancel add/edit procedure dialog");
	}

	//facility tab 

	public WebElement companyCostInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colCompanyCost")));
	}

	public void inputCompanyCostInput(String companyCost) {
		companyCostInput().sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		companyCostInput().sendKeys(companyCost);
		LOG.info("        Entered Company Cost : " + companyCost);
	}
	public WebElement info1Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colInfo1")));
	}

	public void inputInfo1Input(String info) {
		info1Input().sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		info1Input().sendKeys(info);
		LOG.info("        Input Info1 input : " + info);
	}

	public WebElement info2Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colInfo2")));
	}

	public WebElement facilityDialogFormError(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}

	public WebElement facilityDialogWrapper(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_facilityTable")));
	}

	public WebElement performingFacilityFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_colPerfFac")));
	}

	public WebElement labCostFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_colLabCost")));
	}

	public WebElement companyCostFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_colCompanyCost")));
	}

	public WebElement minPriceFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_colMinPrice")));
	}

	public WebElement labCostCol(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_facilityTable_colLabCost']/div")));
	}

	public WebElement companyCostCol(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_facilityTable_colCompanyCost']/div")));
	}

	public WebElement minPriceCol(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_facilityTable_colMinPrice']/div")));
	}

	public WebElement hideShowFacilityTableIcon(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_facilityTable']/div[1]/a")));
	}

	public WebElement wrapperOfFacilityTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_facilityTable']/div[3]")));
	}

	public WebElement facilityTablePopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_facilityTable")));
	}

	public WebElement expDateFacilityPopup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colExpDateTF")));
	}

	public WebElement errorPopup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messagefor_colExpDateTF")));
	}

	public void inputExpDateFacilityPopup(String expDate) {
		expDateFacilityPopup().sendKeys(expDate);
		expDateFacilityPopup().sendKeys(Keys.TAB);
		LOG.info("        Input "+expDate+" to Expiration Date");
	}

	public void inputPerformingFacilityFilter(String filter) throws InterruptedException{
		performingFacilityFilterInput().clear();
		performingFacilityFilterInput().sendKeys(filter);
		performingFacilityFilterInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Filter performing facility : " + filter);
	}

	public void inputLabCostFilter(String filter){
		labCostFilterInput().clear();
		labCostFilterInput().sendKeys(filter);
		performingFacilityFilterInput().sendKeys(Keys.TAB);
		LOG.info("        Filter lab cost : " + filter);
	}

	public void inputCompanyCostFilter(String filter){
		companyCostFilterInput().clear();
		companyCostFilterInput().sendKeys(filter);
		performingFacilityFilterInput().sendKeys(Keys.TAB);
		LOG.info("        Filter company cost : " + filter);
	}

	public void inputMinPriceFilter(String filter){
		minPriceFilterInput().clear();
		minPriceFilterInput().sendKeys(filter);
		performingFacilityFilterInput().sendKeys(Keys.TAB);
		LOG.info("        Filter min price : " + filter);
	}

	public void inputInfo2Input(String info) {
		info2Input().sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		info2Input().sendKeys(info);
		LOG.info("        Input Info2 input : " + info);
	}

	public void clickHideShowFacilityTableIcon() throws InterruptedException{
		hideShowFacilityTableIcon().click();
		Thread.sleep(1000);
		LOG.info("        Clicked to icon show/hide facility table");
	}

	public void clickLabCostCol() throws InterruptedException{
		labCostCol().click();
		Thread.sleep(3000);
		LOG.info("        Lab Cost Column clicked");
	}

	public void clickCompanyCostCol() throws InterruptedException{
		companyCostCol().click();
		Thread.sleep(3000);
		LOG.info("        Company Cost Column clicked");
	}

	public void clickMinPriceCol() throws InterruptedException{
		minPriceCol().click();
		Thread.sleep(3000);
		LOG.info("        Min Price Column clicked");
	}

	//Modifier
	public WebElement modifier1DropDown(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='TblGrid_tbl_pyrModTable']/tbody/tr[4]/td[2]/div/a")));
	}

	public WebElement modifier2DropDown(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='TblGrid_tbl_pyrModTable']/tbody/tr[5]/td[2]/div/a")));
	}

	public WebElement groupModifier1DropDown(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_modId1Path']/a")));
	}

	public WebElement groupModifier2DropDown(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_modId2Path']/a")));
	}

	public WebElement modifier1TableList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/ul")));
	}

	public WebElement modifier2TableList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/ul")));
	}

	public WebElement expirationDatePayorGroup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='colPyrExpDt']")));
	}

	public WebElement editPayorGrpModifierButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edit_tbl_pyrGrpModTable']/div")));
	}

	public WebElement addNewPayorPopup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_pyrModTable")));
	}

	public WebElement editPayorDialog(){
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editmodtbl_pyrModTable")));
	}

	public WebElement alertPyrGrpModTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='alertcnt_tbl_pyrGrpModTable']/div")));
	}

	public WebElement payorGroupDialogFormError(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}
	public WebElement payorSpecificmodifierDialogFormError(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}

	public WebElement deletePayorCheckBox(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colHDelete")));
	}

	public WebElement cancelAddPayorGroup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='TblGrid_tbl_pyrGrpModTable_2']/tbody/tr[2]/td[2]/a[2]")));
	}

	public WebElement addPayorGroupDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_pyrGrpModTable")));
	}

	public WebElement editPayorGroupDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_pyrGrpModTable")));
	}

	public WebElement wrapperOfGroupSpecificModifierTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_pyrGrpModTable']/div[2]")));
	}

	public WebElement getRowOfPayorGroupSpecificModifiersTable(int indexRow){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_pyrGrpModTable']/tbody/tr["+(indexRow+1)+"]")));
	}

	public WebElement hideGroupSpecificModifierTableButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_pyrGrpModTable']/div[1]/a/span")));
	}

	public WebElement wrapperOfSpecificModifierTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_pyrModTable']/div[2]")));
	}

	public WebElement hideSpecificModifierTableButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_pyrModTable']/div[1]/a/span")));
	}

	public WebElement payorIDFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_colPyrAbbrv")));
	}

	public WebElement modifier1Filter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_pyrModTable']/div[2]/div/table/thead/tr[2]/th[3]/div/input")));
	}

	public WebElement modifier2Filter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_pyrModTable']/div[2]/div/table/thead/tr[2]/th[4]/div/input")));
	}

	public WebElement firstCellInPayorGroupModifierTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("td[aria-describedby='tbl_pyrGrpModTable_colPyrGrpId']")));
	}

	public WebElement expirationDateFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_pyrModTable']/div[2]/div/table/thead/tr[2]/th[6]/div/input")));
	}

	public WebElement getRowOfPayorSpecificModifiersTable(int indexRow){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_pyrModTable']/tbody/tr["+(indexRow+1)+"]")));
	}
	
	public WebElement changeTestCodeIdButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("changeTestCdId")));
	}
	
	public WebElement changeTestCodeIdPopupTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Change Test Code ID' and @class='ui-dialog-title']")));
	}
	
	public WebElement currentTestCodeIdField() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testCdIdDialog")));
	}
	
	public WebElement newTestCodeIdField() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("newTestCdId")));
	}
	
	public WebElement okChangeTestCodeIdPopupButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-describedby='changeTestCodeIdDialog']//span[text()='OK']")));
	}
	
	public WebElement validationMessageOfChangeTestCodeIdPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("xf_message_content")));
	}
	
	public WebElement closeValidationMessageOfChangeTestCodeIdPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("xf_message_close")));
	}
	
	public WebElement addRecordComponentsPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='edithdtbl_componentTable']//*[text()='Add Record']")));
	}


	public void selectFirstRowInPayorGroupSpecificModifierTable(){
		firstCellInPayorGroupModifierTable().click();
		LOG.info("        Select first row in Payor Group Specific Modifier table");
	}

	public void inputModifier1Filter(String value) throws InterruptedException{
		modifier1Filter().sendKeys(value);
		modifier1Filter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input " + value + " to modifier1 filter");
	}

	public void inputModifier2Filter(String value) throws InterruptedException{
		modifier2Filter().sendKeys(value);
		modifier2Filter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input " + value + " to modifier2 filter");
	}

	public void inputExpirationDateFilter(String value) throws InterruptedException{
		expirationDateFilter().sendKeys(value);
		expirationDateFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input " + value + " to Expiration Date filter");
	}

	public void inputpayorIDFilter(String value) throws InterruptedException{
		payorIDFilter().sendKeys(value);
		payorIDFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input " + value + " to PayorID filter");
	}

	public void clickHideSpecificModifierTableButton() throws InterruptedException{
		hideSpecificModifierTableButton().click();
		Thread.sleep(1000);
		LOG.info("        Click hide Specific Modifier Table Button");
	}

	public void clickHideGroupSpecificModifierTableButton() throws InterruptedException{
		hideGroupSpecificModifierTableButton().click();
		Thread.sleep(1000);
		LOG.info("        Click hide Specific Modifier Table Button");
	}

	public void clickOkOnEditModifierGroup(){
		okBtn().click();
		LOG.info("        Click Ok edit Payor group in Modifiers");
	}

	public void clickCancelAddPayorGroup(){
		cancelAddPayorGroup().click();
		LOG.info("        Click Cancel add Payor Group Dialog");
	}

	public void clickeditPayorGrpModifierButton(){
		editPayorGrpModifierButton().click();
		LOG.info("         click Edit button on payor group modifier");
	}

	public void setExpirationDatePayorGroup(String exprirationDate) {
		expirationDatePayorGroup().sendKeys(exprirationDate);
		LOG.info("       Input valid expiration date : " + exprirationDate);
	}

	public void clickOkPayorGrpBtn(){
		okBtn().click();
		LOG.info("        Click Ok Payor Group");
	}
	public void clickOkOnAddModifier(){
		okBtn().click();
		LOG.info("        Click Ok add Payor in Modifiers");
	}

	public void clickCancelPayorGrpBtn(){
		cancelBtn().click();
		LOG.info("        Click Cancel Payor Group");
	}

	//Modifier - payor Search
	public WebElement addPayorSpecBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_pyrModTable")));
	}

	public void clickAddPayorSpecBtn() {
		addPayorSpecBtn().click();
		LOG.info("        Click on Add Payor Specific button");
	}

	public WebElement searchPayorIDBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tr_colPyrAbbrv']/td[1]/div/a")));
	}

	public void clickSearchPayorIDBtn() {
		searchPayorIDBtn().click();
		LOG.info("        Click on Search Payor ID button");
	}


	public String excerptNull(String data) {
		if (StringUtils.isNullOrEmpty(data)) {
			return "";
		} else {
			return data.trim();
		}
	}

	public String generatePayorIDNotExist(String testDb) throws Exception {
		List<String> data = daoManagerXifinRpm.getAllPayorID(testDb);

		RandomCharacter randomCharacter = new RandomCharacter();
		boolean flag = true;
		String randomNumber = null;

		while (flag) {
			randomNumber = randomCharacter.getRandomAlphaString(5);
			if (!data.contains(randomNumber)) {
				flag = false;
				break;
			}
		}
		return randomNumber;
	}

	public int getNumberPayor(String testDb) throws Exception {
		List<String> data = daoManagerXifinRpm.getAllPayorID(testDb);
		return data.size();
	}

	public int numberOfPayorInResult() {
		WebElement result = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='pager_right']/div")));
		String text = result.getText();
		return Integer.parseInt(text.substring(text.length() - 3, text.length()));
	}

	public boolean checkDefaultBlankSelect(WebElement element) {
		boolean isBlank = false;
		Select select = new Select(element);
		WebElement selectedOption = select.getFirstSelectedOption();

		//Infonam//if (selectedOption.getText().equals("")) {
		if (selectedOption.getText().trim().equals("")) {
			isBlank = true;
		}

		return isBlank;
	}

	//Fee Schedule
	public WebElement hideShowFeeScheduleTableIcon(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_feeScheduleTable']/div[1]/a")));
	}

	public WebElement feeScheduleTab() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='ui-id-5']")));
	}

	public WebElement addNewFSBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_feeScheduleTable")));
	}

	public WebElement feeSchIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcAbbrev")));
	}

	public WebElement feeSchTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_feeScheduleTable")));
	}

	public WebElement addFSPopup() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edithdtbl_feeScheduleTable")));
	}

	public WebElement effDtAddNewFS() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("effDt")));
	}

	public WebElement feeScheduleTablePagernav(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_feeScheduleTable_pagernav")));
	}

	public WebElement feeScheduleIdFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_prcAbbrev")));
	}

	public WebElement feeScheduleNameFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_descr")));
	}

	public WebElement feeScheduleTypeFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_bClientBased")));
	}

	public WebElement feeScheduleEffDtFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_effDt")));
	}

	public WebElement feeScheduleExpDtFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_expDt")));
	}

	public WebElement feeScheduleCurPriceFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_curPrice")));
	}

	public WebElement bClientBased(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bClientBased")));
	}

	public WebElement feeScheduleNewPriceFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_newPrice")));
	}

	public WebElement feeScheduleDialogFormError(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}

	public void setFeeScheduleIdFilter(String feeScheduleId) throws InterruptedException{
		feeScheduleIdFilter().clear();
		feeScheduleIdFilter().sendKeys(feeScheduleId);
		feeScheduleIdFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input Fee Schedule Id : " + feeScheduleId);
	}

	public void setFeeScheduleNameFilter(String feeScheduleName) throws InterruptedException{
		feeScheduleNameFilter().clear();
		feeScheduleNameFilter().sendKeys(feeScheduleName);
		feeScheduleNameFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input Fee Schedule Name : " + feeScheduleName);
	}

	public void setFeeScheduleTypeFilter(String feeScheduleType) throws InterruptedException{
		feeScheduleTypeFilter().clear();
		feeScheduleTypeFilter().sendKeys(feeScheduleType);
		feeScheduleTypeFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input Fee Schedule Type : " + feeScheduleType);
	}

	public void setFeeScheduleEffDtFilter(String feeScheduleEffDt) throws InterruptedException{
		feeScheduleEffDtFilter().clear();
		feeScheduleEffDtFilter().sendKeys(feeScheduleEffDt);
		feeScheduleEffDtFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input Fee Schedule Effective date : " + feeScheduleEffDt);
	}

	public void setFeeScheduleExpDtFilter(String feeScheduleExpDt) throws InterruptedException{
		feeScheduleExpDtFilter().clear();
		feeScheduleExpDtFilter().sendKeys(feeScheduleExpDt);
		feeScheduleExpDtFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input Fee Schedule Expiration date : " + feeScheduleExpDt);
	}

	public void setFeeScheduleCurPriceFilter(String feeScheduleCurPrice) throws InterruptedException{
		feeScheduleCurPriceFilter().clear();
		feeScheduleCurPriceFilter().sendKeys(feeScheduleCurPrice);
		feeScheduleCurPriceFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input Fee Schedule Current price  : " + feeScheduleCurPrice);
	}

	public void setFeeScheduleNewPriceFilter(String feeScheduleNewPrice) throws InterruptedException{
		feeScheduleNewPriceFilter().clear();
		feeScheduleNewPriceFilter().sendKeys(feeScheduleNewPrice);
		feeScheduleNewPriceFilter().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		LOG.info("        Input Fee Schedule  New price  : " + feeScheduleNewPrice);
	}

	public void clickHideShowFeeScheduleTableIcon() throws Exception{
		hideShowFeeScheduleTableIcon().click();
		Thread.sleep(1000);
		LOG.info("        Clicked collapse collapse button.");
	}

	public void clickAddNewFSBtn(){
		addNewFSBtn().click();
		LOG.info("        Clicked add new feeSchedule button");
	}

	public void clickfeeScheduleTab(){
		feeScheduleTab().click();
		LOG.info("        Clicked feeSchedule Tab");
	}

	public void inputfeeSchId(String value){
		feeSchIdInput().sendKeys(value);
		feeSchIdInput().sendKeys(Keys.TAB);
		LOG.info("        Entered " + value + " to Fee Schedule Id ");
	}

	public void inputEffDtAddNewFS(String value){
		effDtAddNewFS().clear();
		effDtAddNewFS().sendKeys(value);
		effDtAddNewFS().sendKeys(Keys.TAB);
		LOG.info("        Entered " + value + " to Effective Date ");
	}
	public WebElement newPriceFS() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("newPrice")));
	}
	public void inputnewPriceFS(String value){
		newPriceFS().clear();
		newPriceFS().sendKeys(value);
		LOG.info("        Entered " + value + " to New Price ");
	}

	public void clickOkFeeScheduleBtn() throws Exception {
		okBtn().click();
		Thread.sleep(3000);
		LOG.info("        Clicked on Ok Fee Schedule button.");
	}

	public void clickCancelFeeScheduleBtn(){
		cancelBtn().click();
		LOG.info("        Click on cancel Fee Schedule button");
	}

	public WebElement expDtAddnewFS() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("expDt")));
	}
	public void inputExpDtAddnewFS(String value){
		expDtAddnewFS().clear();
		expDtAddnewFS().sendKeys(value);
		expDtAddnewFS().sendKeys(Keys.TAB);
		LOG.info("        Input " + value + " Expiration Date ");
	}

	public List<String> addNewFeeSchedule(SeleniumBaseTest b, String effDate, String expDate,  String newPrice, Boolean isOkClick, String testDb) throws Exception{
		
		List<String> feeSchedule = new ArrayList<>();
		Prc feeScheduleDb= prcDao.getFeeScheduleId();

		Assert.assertTrue(b.isElementPresent(feeScheduleTab(), 5));
		clickfeeScheduleTab();
		testCodeUtils.scrollIntoView(addNewFSBtn());
		clickAddNewFSBtn();
		Assert.assertTrue(b.isElementPresent(addFSPopup(), 5));
		String specialFSID = feeScheduleDb.getPrcAbbrev();
		Assert.assertTrue(b.isElementPresent(feeSchIdInput(), 5));
		inputfeeSchId(specialFSID);		
		inputEffDtAddNewFS(effDate);
		inputExpDtAddnewFS(expDate);
		inputnewPriceFS(newPrice);
		String type = bClientBased().getAttribute("value");
		Assert.assertTrue(b.isElementPresent(okBtn(), 5));
		Assert.assertTrue(b.isElementPresent(cancelBtn(), 5));
		if(isOkClick) {clickOkFeeScheduleBtn();}else {clickCancelFeeScheduleBtn();}
		feeSchedule.add(specialFSID);
		feeSchedule.add(feeScheduleDb.getDescr());
		feeSchedule.add(type);
		feeSchedule.add(effDate);
		feeSchedule.add(expDate);
		feeSchedule.add(newPrice);

		return feeSchedule;

	}

	public int getRowIndex(WebElement table, String valueToFind){
		// Return number of row (start from 1) have valueToFind, return -1 = NOT FOUND ;
		int rowNumber = -1;
		int i = 1;
		// Get row count
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(By.xpath("td"));
			for (WebElement col : cols) {
				if(!col.getText().isEmpty()){
					if (col.getText().equalsIgnoreCase(valueToFind)) {
						LOG.info("        Found : " + valueToFind + " in row " + i);
						return i;
					}
				}
			}
			i++;
		}
		return rowNumber;
	}

	public List<String> getAllPerformingFacilityInTable(WebElement tableElement) {
		List<String> result = new ArrayList<>();

		// Get row count
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(By.xpath("td"));
			result.add(cols.get(2).getText());
		}
		result.remove(0);

		return result;
	}

	public void editModifierGroupPayor(int numRows, String testDb, String testCodeId) throws Exception {

		//Infonam//RandomCharacter randomCharacter = new RandomCharacter(driver);
		SeleniumBaseTest b= new SeleniumBaseTest();
		if (numRows <= 1) {
			String minEffDate = effDateDropDown().getText();
			String effDateInput = testCodeUtils.getNextYear(minEffDate);
			addGroupModifiers(1, b, effDateInput,true, testDb);
			clickSaveAndClearBtn();
			inputTestCodeId(testCodeId);
			editModifierGroupPayor(2, testDb, testCodeId);
		} else {
			selectFirstRowInPayorGroupSpecificModifierTable();
			clickeditPayorGrpModifierButton();
			//Infonam//selectGroupModifier(true, Integer.parseInt(randomCharacter.getRandomNumericString(1))); // Select modifier 1
			//Infonam//selectGroupModifier(false, Integer.parseInt(randomCharacter.getRandomNumericString(1)));// Select modifier 2
			selectGroupModifier(true, getNonZeroRandomNumber(1)); // Select modifier 1
			selectGroupModifier(false, getNonZeroRandomNumber(1));// Select modifier 2
		}
	}

	public String selectPerformingFacilityUnique(List<String> existFacility){
		performingFacility().findElement(By.tagName("a")).click();
		WebElement list = performingFacilityTableList();
		// Now get all the li elements from the ul list
		List<WebElement> allRows = list.findElements(By.tagName("li"));
		allRows.remove(0);
		String performingFac = "";

		// And iterate over them, getting the cells 
		for (WebElement row : allRows) {
			if(!existFacility.contains(row.getText())) {
				performingFac = row.getText();
				row.click();
				LOG.info("        Clicked Performing Facility Table : " + performingFac);
				break;
			}
		}
		return performingFac;
	}

	public String inputAllFacilityFieldsUnique(WebElement tableElement, SeleniumBaseTest b) throws Exception{
		String facility;

		List<String> allPerformFac = getAllPerformingFacilityInTable(tableElement);

		facility = selectPerformingFacilityUnique(allPerformFac);
		RandomCharacter randomCharacter = new RandomCharacter(driver);
		String price = testCodeUtils.remove0Number(randomCharacter.getRandomNumericString(1));

		Assert.assertTrue(b.isElementPresent(labCostInput(), 5));
		inputLabCost(price);
		LOG.info("        Input data into Lab Cost input");

		Assert.assertTrue(b.isElementPresent(companyCostInput(), 5));
		inputCompanyCostInput(price);
		LOG.info("        Input data into Company Cost input");

		Assert.assertTrue(b.isElementPresent(minPriceInput(), 5));
		inputMinPrice(price);
		LOG.info("        Input data into Min Price input");

		Assert.assertTrue(b.isElementPresent(info1Input(), 5));
		inputInfo1Input("Edited fields for info 1");
		LOG.info("        Input data into Info 1 input");

		Assert.assertTrue(b.isElementPresent(info2Input(), 5));
		inputInfo2Input("Edited fields for info 2");
		LOG.info("        Input data into Info 2 input");

		String minEffDate = effDateDropDown().getText();
		String effDateInput = testCodeUtils.getNextYear(minEffDate); // Always input eff date greater than existing eff date.
		inputExpDateFacilityPopup(effDateInput);

		return facility;
	}

	public void setDefaultFacility(WebElement tableElement, String facility) {

		// Get row count
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(By.xpath("td"));
			if ((cols.get(2).getText()).equals(facility)) {
				WebElement input = row.findElement(By.tagName("input"));
				input.click();
			}
		}
	}

	public boolean checkRadioButtonDefault(WebElement tableElement, String facility) {
		boolean checked = false;
		// Get row count
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(By.xpath("td"));
			if ((cols.get(2).getText()).equals(facility)) {
				checked = row.findElement(By.tagName("input")).isSelected();
			}
		}

		return checked;

	}

	public List<String> editFieldsInEditPopup(SeleniumBaseTest b) throws Exception {

		List<String> list = new ArrayList<>();
		RandomCharacter randomCharacter = new RandomCharacter(driver);

		Assert.assertTrue(b.isElementPresent(labCostInput(), 5));
		String labCost = testCodeUtils.remove0Number(randomCharacter.getRandomNumericString(1));
		list.add(labCost);
		inputLabCost(labCost);
		LOG.info("        Input data into Lab Cost input");

		Assert.assertTrue(b.isElementPresent(companyCostInput(), 5));
		String companyCost = labCost;
		list.add(companyCost);
		inputCompanyCostInput(companyCost);
		LOG.info("        Input data into Company Cost input");

		Assert.assertTrue(b.isElementPresent(minPriceInput(), 5));
		String minPrice = labCost;
		list.add(minPrice);
		inputMinPrice(minPrice);
		LOG.info("        Input data into Min Price input");

		Assert.assertTrue(b.isElementPresent(info1Input(), 5));
		info1Input().clear();
		String info1 ="info1"+randomCharacter.getRandomAlphaString(10);
		list.add(info1);
		inputInfo1Input(info1);
		LOG.info("        Input data into Info 1 input :" +info1);

		Assert.assertTrue(b.isElementPresent(info2Input(), 5));
		info2Input().clear();
		String info2 ="info2"+randomCharacter.getRandomAlphaString(10);
		list.add(info2);
		inputInfo2Input(info2);
		LOG.info("        Input data into Info 2 :" +info2);

		return list;
	}

	public String getNewProcCodeIdInDb(SeleniumBaseTest b , String currentProcCodeId, String procCodeTable, String testDb) throws Exception{
		boolean flag = false;
		String procCodeId = "";
		List<String> proCodeInfo = daoManagerXifinRpm.getProcedureCodeByProcCodeTable(procCodeTable,testDb);
		List<String> proCodeInfoTemp;
		for (int i = 0; i < proCodeInfo.size(); i++) {
			if(flag) break;
			procCodeId = proCodeInfo.get(0);
			while (b.getColumnValue(procedureTable(), currentProcCodeId)){
				proCodeInfoTemp = daoManagerXifinRpm.getProcedureCodeByProcCodeTable(procCodeTable,testDb);
				procCodeId = proCodeInfoTemp.get(0);
				currentProcCodeId = procCodeId;
				flag = true;
			} 
		}

		return procCodeId;
	}

	public String getNewProcTableInDb(SeleniumBaseTest b , String currentProcTable, String testDb) throws Exception{
		boolean flag = false;
		String procTable = "";
		List<String> proCodeInfo = daoManagerXifinRpm.getProcedureCodeId(testDb);
		List<String> proCodeInfoTemp;
		for (int i = 0; i < proCodeInfo.size(); i++) {
			if(flag) break;
			procTable = proCodeInfo.get(1);
		
			while (b.getColumnValue(procedureTable(), currentProcTable)){
				proCodeInfoTemp = daoManagerXifinRpm.getProcedureCodeId(testDb);
				procTable = proCodeInfoTemp.get(1);
				currentProcTable = procTable;
				flag = true;
			} 
		}

		return procTable;
	}
	public List<String> getNewPayorGroupIdInDb(SeleniumBaseTest b ,String testDb) throws Exception{
		List<String> newpayorGroup = new ArrayList<>();
		List<String> proCodeInfo = daoManagerXifinRpm.getGroupPayor(testDb);
		List<String> proCodeInfoTemp;
		String payorGroupName ="";
		String payorGrpId ="";
		for (int i = 0; i < proCodeInfo.size(); i++) {
			payorGrpId = proCodeInfo.get(0);
			payorGroupName = proCodeInfo.get(1);
			while (b.getColumnValue(payorGroupModifiersTable(), payorGroupName)){
				proCodeInfoTemp = daoManagerXifinRpm.getGroupPayor(testDb);
				payorGrpId = proCodeInfoTemp.get(0);
				payorGroupName = proCodeInfoTemp.get(1);
			} 

		}
		newpayorGroup.add(payorGrpId);
		newpayorGroup.add(payorGroupName);
		return newpayorGroup;
	}

	public String getNewPayorSpecificModifiersIdInDb(SeleniumBaseTest b ,String testDb) throws Exception{
		String payorId = payorDao.getPayorId().getPyrAbbrv();
		while (b.getColumnValue(payorSpecificModifiersTable(), payorId)){
			payorId = payorDao.getPayorId().getPyrAbbrv();
		} 

		return payorId;
	}

	public List<List<String>> addMultiProcedures(int numberOfProcAdd, SeleniumBaseTest baseTest, TestCode testCode, String testDb) throws Exception{

		//Return List Procedures | Procedure[i][0] : ID , Procedure[i][1] : Table Name , Procedure[i][2] : Description | i: index of list Procedures

		List<List<String>> procedureCodeAdded = new ArrayList<>();
		for (int i=1; i<= numberOfProcAdd; i++){
			List<String> procedure = addProcedure(baseTest, testCode, "",testDb);
			procedureCodeAdded.add(procedure);
		}
		return procedureCodeAdded;
	}

	public List<String> addProcedure(SeleniumBaseTest baseTest, TestCode testCode, String date, String testDb) throws Exception{

		//Return Procedure added | Procedure[0] : ID , Procedure[1] : Table Name  , Procedure[2] : Description
		testCode.clickProcedureTab();
		testCodeUtils.scrollIntoView(testCode.addProcedureBtn());
		testCode.clickAddProcedureBtn();

		boolean finishAdd = false;
		List<String> procedureCodeTemp;
		String procedureCodeId;
		String procedureCodeTable;
		String procedureCodeDescr;

		do {			
			procedureCodeTemp = daoManagerXifinRpm.getProcedureCodeId(testDb);
			procedureCodeId = procedureCodeTemp.get(0);
			procedureCodeTable = procedureCodeTemp.get(1);
			procedureCodeDescr = procedureCodeTemp.get(3);

			testCode.selectProcedureTable(procedureCodeTable);
			testCode.inputProcedureCode(procedureCodeId);
			if (!date.equals("")) {
				testCode.inputProcedureExpDate(date);
			}
//			testCode.procedureCodeInput().sendKeys(Keys.TAB); // tab out procedure code input to close Procedure table list popup
			testCode.clickOkProcedureBtn();

			LOG.info("        Found procedure code : " + procedureCodeId + " in procedure table : " + procedureCodeTable);

			finishAdd = baseTest.getColumnValue(testCode.procedureTable(), procedureCodeId);

			if (!finishAdd){

//				clickCancelProcedureDialog();
				testCode.clickAddProcedureBtn();
				LOG.info("        Skip procedure code : " + procedureCodeId + " in procedure table : " + procedureCodeTable);
			}

		} while (!finishAdd);

		LOG.info("        Added procedure code : " + procedureCodeId + " in procedure table : " + procedureCodeTable);
		Assert.assertTrue(baseTest.getColumnValue(testCode.procedureTable(), procedureCodeId), "        Procedures must be added in table");

		List<String> procedure = new ArrayList<>();
		procedure.add(procedureCodeId);
		procedure.add(procedureCodeTable);
		procedure.add(procedureCodeDescr);

		return procedure;
	}

	public List<List<String>> addMultiProcedures(int numberOfProcAdd, SeleniumBaseTest baseTest, TestCode testCode, String testDb, String testAbbrev) throws Exception{

		//Return List Procedures | Procedure[i][0] : ID , Procedure[i][1] : Table Name , Procedure[i][2] : Description | i: index of list Procedures

		List<List<String>> procedureCodeAdded = new ArrayList<>();
		for (int i=1; i<= numberOfProcAdd; i++){
			List<String> procedure = addProcedure(baseTest, testCode, "",testDb, testAbbrev);
			procedureCodeAdded.add(procedure);
		}
		return procedureCodeAdded;
	}

	public List<String> addProcedure(SeleniumBaseTest baseTest, TestCode testCode, String date, String testDb, String testAbbrev) throws Exception{

		//Return Procedure added | Procedure[0] : ID , Procedure[1] : Table Name  , Procedure[2] : Description
		testCode.clickProcedureTab();
		testCodeUtils.scrollIntoView(testCode.addProcedureBtn());
		testCode.clickAddProcedureBtn();

		boolean finishAdd = false;
		List<String> procedureCodeTemp;
		String procedureCodeId;
		String procedureCodeTable;
		String procedureCodeDescr;

		Test test = rpmDao.getTestByTestAbbrev(testDb,testAbbrev);
		int testId = test.getTestId();
		List <Integer> procTypesList = testDao.getTestProcTypesByTestId(testId);
		if(procTypeId != 0){
			procTypesList.add(procTypeId);
		}

		do {
			//to avoid duplicate testProcType
			do {
				procedureCodeTemp = daoManagerXifinRpm.getProcedureCodeId(testDb);
			} while (procTypesList.contains(Integer.parseInt(procedureCodeTemp.get(2))));

			procTypeId = Integer.parseInt(procedureCodeTemp.get(2));
			procedureCodeId = procedureCodeTemp.get(0);
			procedureCodeTable = procedureCodeTemp.get(1);
			procedureCodeDescr = procedureCodeTemp.get(3);

			testCode.selectProcedureTable(procedureCodeTable);
			testCode.inputProcedureCode(procedureCodeId);
			if (!date.equals("")) {
				testCode.inputProcedureExpDate(date);
			}
			testCode.clickOkProcedureBtn();

			LOG.info("        Found procedure code : " + procedureCodeId + " in procedure table : " + procedureCodeTable);

			finishAdd = baseTest.getColumnValue(testCode.procedureTable(), procedureCodeId);

			if (!finishAdd){
				testCode.clickAddProcedureBtn();
				LOG.info("        Skip procedure code : " + procedureCodeId + " in procedure table : " + procedureCodeTable);
			}

		} while (!finishAdd);

		LOG.info("        Added procedure code : " + procedureCodeId + " in procedure table : " + procedureCodeTable);
		Assert.assertTrue(baseTest.getColumnValue(testCode.procedureTable(), procedureCodeId), "        Procedures must be added in table");

		List<String> procedure = new ArrayList<>();
		procedure.add(procedureCodeId);
		procedure.add(procedureCodeTable);
		procedure.add(procedureCodeDescr);

		return procedure;
	}

	//Not working!!!!!!!!!!!!
	//Use daoManagerPlatform.getPerformFacility(testDb) instead
	public boolean isAvailablePerformingFacility(String performingFacility, TestCode testCode){
		boolean result = false;
		
		testCode.performingFacility().findElement(By.tagName("a")).click();//infonam
		WebElement list = testCode.performingFacilityTableList();//infonam
		List<WebElement> allRows = list.findElements(By.tagName("li")); 
		for (WebElement row : allRows) {
			if (row.getText().equalsIgnoreCase(performingFacility)) {
				LOG.info("        Performing facility available : " + performingFacility);
				result = true;
				break;
			}
		}
		//infonam//testCode.performingFacility().findElement(By.tagName("a")).click();
		return result;
	}	

	public List<String> addMultiFacility(int numberOfFacilityAdd, SeleniumBaseTest b, TestCode testCode, String testDb) throws Exception{
		List<String> facilities = new ArrayList<>();
		testCode.clickFacilityTab();
		boolean finish = false;
		//Infonam//DaoManagerXifinRpm

		for(int i=1; i<=numberOfFacilityAdd; i++){

			String facility;
			do {

				finish = false;
				testCodeUtils.scrollIntoView(addFacilityBtn());
				testCode.clickAddFacilityBtn();
				boolean added = false;

				do {
					//Infonam//facility = daoManagerXifinRpm.getFacilityInfo(testDb).get(1);
					//Infonam//boolean available = isAvailablePerformingFacility(facility, testCode);
					//Infonam//if (!available) {
					//Infonam//LOG.info("        Skip Facility: " + facility + " because it's not available in Performing Facility select table.");
					//Infonam//continue; // Not available performing facility will do loop again
					//Infonam//}
					facility = daoManagerPlatform.getPerformFacility(testDb).get(0).trim();

					boolean duplicate = b.getColumnValue(testCode.facilityTable(), facility);
					if (duplicate) {
						LOG.info("        Skip Facility: " + facility + " because it's duplicate.");
						continue; // Duplicate performing facility will do loop again
					}

					//Infonam//added =  (available==true) && (duplicate==false) ; 
					added = (!duplicate);
				} while (!added);

				//Infonam//LOG.info("        Found facility : " + facilities);
				LOG.info("        Found facility: " + facility);
				testCode.selectPerformingFacility(facility);

				String labCost  = String.valueOf(2 + (int)(Math.random() * ((100 - 2) + 1))); // Max = 100, min = 2
				String minPrice = String.valueOf(101 + (int)(Math.random() * ((200 - 101) + 1))); // Max = 200, min = 101 | minPrice >= labCost

				Assert.assertTrue(b.isElementPresent(testCode.labCostInput(), 5));
				testCode.labCostInput().click();
				testCode.labCostInput().clear();
				testCode.inputLabCost(labCost);
				
				//yli
				Assert.assertTrue(b.isElementPresent(testCode.companyCostInput(), 5));
				testCode.companyCostInput().click();
				testCode.companyCostInput().clear();
				testCode.inputCompanyCostInput(labCost);				
				
				Assert.assertTrue(b.isElementPresent(testCode.minPriceInput(), 5));
				testCode.minPriceInput().click();
				testCode.minPriceInput().clear();
				testCode.inputMinPrice(minPrice);
				Assert.assertTrue(b.isElementPresent(testCode.okBtn(), 5));
				testCode.clickOkFacilityBtn();
				finish = b.isElementHidden(facilityDialogWrapper(), 5);

			} while(!finish);

			Assert.assertTrue(b.getColumnValue(testCode.facilityTable(), facility),"        Facility should be added in table");

			facilities.add(facility);
			LOG.info("        Added facility " + facility);
		}

		return facilities;
	}

	public List<List<String>> addModifiers(int numberOfModifiers,SeleniumBaseTest b,String expDate) throws Exception{
		List<String> listPayorsChild = new ArrayList<>();
		List<List<String>> listPayorsAdded = new ArrayList<>();
		Thread.sleep(1000);
		clickModifierTab();
		boolean isDuplicateID = false;
		for (int i=1; i<=numberOfModifiers; i++) {
			testCodeUtils.scrollIntoView(addNewPayorIDButton());
			clickAddPayorIDBt();
			String payorID;
			do {
				payorID = payorDao.getPayorId().getPyrAbbrv();
				isDuplicateID = b.getColumnValue(payorSpecificModifiersTable(), payorID);
				if (isDuplicateID) LOG.info("        Skip Payor : " + payorID + " because it's duplicate.");
			} while (isDuplicateID);

			setPayorIdModifiersInput(payorID);
			//Infonam//String modifier1 = selectModifier(true, 2); // Select modifier 1
			//Infonam//String modifier2 = selectModifier(false, 2);// Select modifier 2
			String modifier1 = selectModifier(true, getNonZeroRandomNumber(1)); // Select modifier 1
			String modifier2 = selectModifier(false, getNonZeroRandomNumber(1));// Select modifier 2
			if (!expDate.isEmpty()){
				setExpDateOnAddNewPayor(expDate);
			}
			clickOkOnAddModifier();
			listPayorsChild.add(payorID);
			listPayorsChild.add(modifier1);
			listPayorsChild.add(modifier2);
			listPayorsChild.add(expDate);
			listPayorsAdded.add(listPayorsChild);
			LOG.info("        Added payor : " + payorID);
		}

		return listPayorsAdded;
	}

	public List<List<String>> addGroupModifiers(int numberOfModifiers,SeleniumBaseTest b,String expirationDate, boolean confirm,String testDb) throws Exception{
		List<List<String>> listPayorsAdded = new ArrayList<>();
		List<String> payorAdd;
		clickModifierTab();
		boolean isDuplicateID = false;

		for (int i=1; i<=numberOfModifiers; i++) {
			testCodeUtils.scrollIntoView(addNewGroupPayorButton());
			clickAddGroupPayorBt();
			String payorGroupName;
			int payorGroupId;
			do {
				PyrGrp listGroupPayorsInfo = payorDao.getRandomPyrGrp();
				payorGroupId = listGroupPayorsInfo.getPyrGrpId();
				payorGroupName = listGroupPayorsInfo.getGrpNm();
				isDuplicateID = b.getColumnValue(payorGroupModifiersTable(), payorGroupName);				
				if (isDuplicateID) LOG.info("        Skip Payor : " + payorGroupName + " because it's duplicate.");
			} while (isDuplicateID);

			selectGroupIdModifier(payorGroupName);
			//Infonam//String mod1 = selectGroupModifier(true, 2); // Select modifier 1
			//Infonam//String mod2 = selectGroupModifier(false, 2);// Select modifier 2
			String mod1 = selectGroupModifier(true, getNonZeroRandomNumber(1)); // Select modifier 1
			String mod2 = selectGroupModifier(false, getNonZeroRandomNumber(1));// Select modifier 2
			if (!expirationDate.isEmpty()) {setExpirationDatePayorGroup(expirationDate);}
			if(confirm){clickOkPayorGrpBtn();}else{clickCancelAddPayorGroup();}
			payorAdd = new ArrayList<>();
			payorAdd.add(String.valueOf(payorGroupId));
			payorAdd.add(payorGroupName);
			payorAdd.add(mod1);
			payorAdd.add(mod2);
			payorAdd.add(expirationDate);
			listPayorsAdded.add(payorAdd);
			LOG.info("        Added Group Payor ID : " + payorGroupName);
		}

		return listPayorsAdded;
	}

	public String selectModifier(boolean selectModifier1,int index){
		if (selectModifier1) modifier1DropDown().click(); 
		else modifier2DropDown().click();

		WebElement list = selectModifier1 ? modifier1TableList() : modifier2TableList();
		List<WebElement> allRows = list.findElements(By.tagName("li"));
		String modifier = "";
		int i=0;
		for (WebElement row : allRows) {
			if (i == index) {
				modifier = row.getText();
				row.click();
				LOG.info("        Clicked Modifier 1 : " + modifier);
				break;
			}
			i++;
		}
		return modifier;
	}

	public String selectGroupModifier(boolean selectModifier1,int index){
		if (selectModifier1) groupModifier1DropDown().click(); 
		else groupModifier2DropDown().click();

		WebElement list = selectModifier1 ? modifier1TableList() : modifier2TableList();
		List<WebElement> allRows = list.findElements(By.tagName("li"));
		String modifier = "";
		int i=0;
		for (WebElement row : allRows) {
			if (i == index) {
				modifier = row.getText();
				row.click();
				LOG.info("        Clicked Modifier 1 : " + modifier);
				break;
			}
			i++;
		}
		return modifier;
	}

	public List<String> findFSNonEffDateAndExpDate(WebElement fsTable){
		List<String> str = new ArrayList<>();
		List<WebElement> rows = fsTable.findElements(By.tagName("tr"));
		int i = 1;
		for (WebElement row : rows) {
			if (i==1) {i++; continue;} // Ignore first row

			String effDate = row.findElement(By.xpath("td[5]")).getText();
			String expDate = row.findElement(By.xpath("td[6]")).getText();
			if (effDate.trim().length()<1 && expDate.trim().length()<1){
				str.add(row.findElement(By.xpath("td[1]")).getText()); // Index of row
				str.add(row.findElement(By.xpath("td[2]")).getText()); // FS ID
				str.add(row.findElement(By.xpath("td[3]")).getText()); // Name
				str.add(row.findElement(By.xpath("td[4]")).getText()); // Type
				str.add(row.findElement(By.xpath("td[7]")).getText()); // Current $
				str.add(row.findElement(By.xpath("td[8]")).getText()); // New $
				break;
			}
		}
		return str;
	}

	//This method checks if the accn was done saving
	public boolean isSaveDone(String procId, String testCodeId, String effDate, String dbEnv) throws Exception{
		boolean flag = false;	
		int count = 0;
		do {
			if (count == 10) {
				break;
			}
			flag = daoManagerXifinRpm.checkTestProcCodeInfoInDb(procId, testCodeId, effDate, dbEnv);
			count ++;
			Thread.sleep(1000);
		} while (!flag);
		return flag;		
	}

	public String isSaveDone(Object object){
		int count = 0;
		String str = "" ;
		do {
			if (count == 10) {
				break;
			}else {
				try {
					str = object.toString();
					count ++;
					Thread.sleep(1000);					
				} catch (Exception e) {
					str = "";
				}
			}

		} while (str.isEmpty());	
		return str;		
	}
	
	public Integer getNonZeroRandomNumber(int length) throws Exception{		
		RandomCharacter randomCharacter = new RandomCharacter(driver);
		String str = randomCharacter.getRandomNumericString(length);
		
		while (str.equals("0")){
			str = randomCharacter.getRandomNumericString(length);
		}
		
		return Integer.parseInt(str);
	}
	
	public boolean isSorting(SeleniumBaseTest b, WebElement element, int column, String sortOrder) throws Exception{
		boolean flag = false;	
		int count = 0;
		do {
			if (count == 10) {
				break;
			}
			//Infonam//flag =  b.getSortingComparisonOnTable(element, column, sortOrder);
			flag =  b.compareSortedNumberValuesInTable(element, column, sortOrder, "###.##");
			count++;
		} while (!flag);
		return flag;		
	}
	
	public List<String> checkPyrGrpExclusion(SeleniumBaseTest b, boolean isClickOk, String testDb) throws Exception{
		List<String> listExclusion = new ArrayList<>();
		Assert.assertTrue(b.isElementPresent(exclusionsTab(), 5),"        Exclusion Tab should show.");
		clickExclusionTab();
		Assert.assertTrue(b.isElementPresent(pyrGrpExclusionTbl(), 5),"        Payor Group Exclusion table should show.");

		if (b.getTableTotalRowSize(pyrGrpExclusionTbl()) == getTotalPyrGrpExclusion()) {
			LOG.info("        Payor Group Exclusion is full");
			return listExclusion;
		}
		if (b.getTableTotalRowSize(pyrGrpExclusionTbl()) == 1) {
			listExclusion = addPayorGroupExclusions(2, b, isClickOk, testDb);
			return listExclusion;
		}
		else {
			listExclusion = addPayorGroupExclusions(1, b, isClickOk, testDb);
		}
		return listExclusion;
	}
	
	public int getTotalPyrGrpExclusion(){
		testCodeUtils.scrollIntoView(addPayorGroup());
		clickAddPayorGroupBt();
		PayorGroupDescription().click();
		List<WebElement> rows = PayorGroupIDExclusionList().findElements(By.tagName("li"));		
		int totalRows = rows.size();
		LOG.info("        Total Payor Group Exclusions: " + totalRows);
		groupIdSearch().sendKeys(Keys.TAB);
		clickCancelPayorGrpBtn();
		return totalRows;
	}

	//public void checkInputTestCodeId(String id) throws InterruptedException{
	public void checkInputTestCodeId(String id) throws Exception{	
		boolean flag = false ;
		int count = 0;
		do {
			inputTestCodeId(id);
			Thread.sleep(1000);
			flag = labelTest().isDisplayed();
			if(!flag)driver.navigate().refresh();
			if(count ==10)break;
			count++;
		} while (!flag);
	}
	
	//public void checkInputTestCodeIdWithNewTestID(String id) throws InterruptedException{
	public void checkInputTestCodeIdWithNewTestID(String id) throws Exception{
		boolean flag = false ;
		int count = 0;
		do {
			inputTestCodeId(id);
			Thread.sleep(1000);
			flag = createOptionOKBtn().isDisplayed();
			if(!flag)driver.navigate().refresh();
			if(count ==10)break;
			count++;
		} while (!flag);
	}
	
	public void closeAllMessageClose(){
		try {
			Actions action = new Actions(driver);

			List<WebElement> links = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("xf_message_close")));
			for (WebElement myElement : links){
				String link = myElement.getText(); 
				if (link.isEmpty()){
					action.moveToElement(myElement).click().build().perform();
					Thread.sleep(2000);
				}
			} 
		}catch (Exception e){
			System.out.println("error "+e);
		}
	}
	/*------------------- Create Method --------------------*/
	public void createSingleTest(String testID, String testDb, boolean addSuppress, boolean addXref, SeleniumBaseTest b) throws Exception{
		if (!daoManagerXifinRpm.checkTestIsAvailable(testID, testDb)) {
			checkInputTestCodeIdWithNewTestID(testID);
			Assert.assertTrue(b.isElementPresent(createScreen(), 5), "        Create Screen should be available.");
			Assert.assertTrue(b.isElementPresent(singleTestRadio(), 5), "        Single test radio must be displayed");
			b.selectCheckBox(singleTestRadio());
			Assert.assertTrue(b.isElementPresent(createOptionOKBtn(), 5), "        OK button on Create Option must be displayed");
			clickCreateOption();
			Assert.assertTrue(b.isElementPresent(labelTest(), 5), "        The label test must be displayed");
			Assert.assertEquals(labelTest().getText(), "(Single Test)", "        Label should be (Single Test)");

			Assert.assertTrue(b.isElementPresent(testNameInput(), 5), "        Testname input must be displayed");
			setTestName(testID + " name");
			Assert.assertTrue(b.isElementPresent(addEffDate(), 5), "        Effective date must be displayed");
			TimeStamp timeStamp = new TimeStamp();
			String curdate = timeStamp.getCurrentDate();
			setEffectiveDate(curdate);
			
			b.waitUntilElementPresent(procedureTab(), 15);
			Assert.assertTrue(b.isElementPresent(procedureTab(), 5), "        Procedures tab should be displayed");
			clickProcedureTab();
			Assert.assertTrue(b.isElementPresent(addProcedureBtn(), 5), "        Add procedure button must be displayed");
			clickAddProcedureBtn();
			List<String> procedureCode = daoManagerXifinRpm.getProcedureCodeId(testDb);
			Assert.assertTrue(b.isElementPresent(procedureCodeInput(), 5), "        Procedure code input must be displayed");
			selectProcedureTable(procedureCode.get(1));
			inputProcedureCode(procedureCode.get(0));
			Assert.assertEquals(procedureCodeName().getAttribute("value"), procedureCode.get(3), "        Procedure code name must be matching with Procedure code name of procedure code " + procedureCode.get(1));
			clickOkProcedureBtn();

			Assert.assertTrue(b.isElementPresent(procedureTable(), 5), "        Procedure table must be displayed");
			Assert.assertTrue(b.getColumnValue(procedureTable(), procedureCode.get(1)), "        Procedures must be added in table");

			Assert.assertTrue(b.isElementPresent(facilityTab(), 5), "        Facility tab must be displayed");
			clickFacilityTab();
			Assert.assertTrue(b.isElementPresent(addFacilityBtn(), 5), "        Add facility button must be displayed");
			clickAddFacilityBtn();
			String facility = "Acme lab - ACME";
			selectPerformingFacility(facility);
			RandomCharacter randomCharacter = new RandomCharacter();
			String minPrice = randomCharacter.getNonZeroRandomNumericString(2) + ".00";
			String labCost  = minPrice;
			Assert.assertTrue(b.isElementPresent(labCostInput(), 5), "        Lab cost input must be displayed");
			inputLabCost(labCost);
			Assert.assertTrue(b.isElementPresent(minPriceInput(), 5), "        Min price input must be displayed");
			inputMinPrice(minPrice);
			Assert.assertTrue(b.isElementPresent(okBtn(), 5), "        OK button must be displayed");
			clickOkFacilityBtn();

			Assert.assertTrue(b.isElementPresent(facilityTable(), 5), "        Facility table must be displayed");
			Assert.assertTrue(b.getColumnValue(facilityTable(), facility), "        Facility must be added in table");
			Assert.assertEquals(recordInFacilitiesTableByRowAndAttribute(2, "PerfFac").getText(), facility, "        Facility must be added in table");
			Assert.assertEquals(recordInFacilitiesTableByRowAndAttribute(2, "LabCost").getText(), labCost, "        Lab cost must be added in table");
			Assert.assertEquals(recordInFacilitiesTableByRowAndAttribute(2, "MinPrice").getText(), minPrice, "        Min price must be added in table");

			if (addSuppress){
				addSuppressCT("", "", true, this, b, testDb);
			}
			if (addXref){
				timeStamp = new TimeStamp();
				addXref(1, b, timeStamp.getCurrentDate(), "", true, testDb);
			}

			Dept depInfo = depDao.getDepartment();
			Assert.assertTrue(b.isElementPresent(selectDeps(), 5), "        Select department must be displayed");
			b.selectItem(selectDeps(), depInfo.getDescr());

			Assert.assertTrue(b.isElementPresent(saveAndClearButton(), 5), "       Save and clear button must be available");
			clickSaveAndClearBtn();
			b.waitUntilElementIsNotVisible(saveAndClearBtn(), 15);
			checkInputTestCodeId(testID);
		} else {
	        daoManagerPlatform.deletePyrExclFromTESTPYREXCLByTestAbbrev(testID, testDb);//yli
	        daoManagerPlatform.deleteTestPyrModFromTESTPYRMODByTestAbbrev(testID, testDb);//yli
	        Thread.sleep(1000);
	        
			checkInputTestCodeId(testID);
			TimeStamp timeStamp = new TimeStamp();
			boolean isClickSaveBtn = false;
			if (addSuppress){
				addSuppressCT("", "", true, this, b, testDb);
				isClickSaveBtn = true;
			}
			if (addXref){				
				addXref(1, b, timeStamp.getCurrentDate(), "", true, testDb);
				isClickSaveBtn = true;
			}
			if(isClickSaveBtn){
				Assert.assertTrue(b.isElementPresent(saveAndClearButton(), 5), "       Save and clear button must be available");
				clickSaveAndClearBtn();
				checkInputTestCodeId(testID);
			}
		}
	}

	public void createProfileTest(String testID, String testDb, SeleniumBaseTest b) throws Exception{

		if (!daoManagerXifinRpm.checkTestIsAvailable(testID, testDb)) {
			checkInputTestCodeIdWithNewTestID(testID);
			Assert.assertTrue(b.isElementPresent(createScreen(), 5), "        Create Screen should be available.");
			Assert.assertTrue(b.isElementPresent(profileTestRadio(), 5), "        Single test radio must be displayed");
			b.selectCheckBox(profileTestRadio());
			Assert.assertTrue(b.isElementPresent(createOptionOKBtn(), 5), "        OK button on Create Option must be displayed");
			clickCreateOption();
			Assert.assertTrue(b.isElementPresent(labelTest(), 5), "        Profile test page label test must be displayed");
			Assert.assertTrue(labelTest().getText().equalsIgnoreCase("(Profile)"), "       Test label should be Profile");

			Assert.assertTrue(b.isElementPresent(testNameInput(), 5), "        Testname input must be displayed");
			setTestName(testID + " name");
			Assert.assertTrue(b.isElementPresent(addEffDate(), 5), "        Effective date must be displayed");
			TimeStamp timeStamp = new TimeStamp();
			String curdate = timeStamp.getCurrentDate();
			setEffectiveDate(curdate);

			Assert.assertTrue(b.isElementPresent(componentTab(), 5), "        Tab table should be displayed");
			addComponent(1, b, curdate, "", true, "s", true, testDb);

			Assert.assertTrue(b.isElementPresent(saveAndClearBtn(), 5), "        Save and clear button must be displayed");
			clickSaveAndClearBtn();
			checkInputTestCodeId(testID);
		}
		else{
			checkInputTestCodeId(testID);
		}
	}
	public String checkPayorSpecificModifiers(String testId, String testDb) throws Exception{
		SeleniumBaseTest b = new SeleniumBaseTest();
		String existPayorId;
		String newPayorId;
		List<List<String>> listModifier = addModifiers(1, b, "");
		List<String> singleListModifier = listModifier.get(0);
		newPayorId  = singleListModifier.get(0);
		Assert.assertTrue(b.isElementPresent(payorSpecificModifiersTable(), 5),"        Payor Specific modifier table must be available");
		Assert.assertTrue(b.getColumnValue(payorSpecificModifiersTable(), newPayorId), "        New record must be added in table");
		existPayorId = payorIdPayorModifierTable().getText();
		if(existPayorId.equalsIgnoreCase(newPayorId)){
			Assert.assertTrue(b.isElementPresent(saveAndClearBtn(), 5),"        Save and clear button must be available");
			clickSaveAndClearBtn();
			Assert.assertTrue(b.isElementPresent(testIdInput(), 5),"        testId input field should show");
			checkInputTestCodeId(testId);
			Assert.assertTrue(b.isElementPresent(testNameInput(), 5), "        Test name input must be available");
			Assert.assertTrue(b.isElementPresent(labelTest(),5),"        Test label must be available.");
			Assert.assertTrue(labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");
			return newPayorId;
		}
		return existPayorId;
	}

	public String checkXrefInXrefTable(){
		return colRefIdXValue() != null ? colRefIdXValue().getText():"";
	}
	
	public String checkSuppressCT(){
		return payorIdSuppressCTTable() != null ? payorIdSuppressCTTable().getText():"";
	}
	
	public String checkPayorExclusions(String profileId , String testDb) throws Exception{
		SeleniumBaseTest b = new SeleniumBaseTest();
		String payorId;
		if(firstCellPyrExclusionTable() != null){
			return firstCellPyrExclusionTable().getText();
		}else{
			Assert.assertTrue(b.isElementPresent(exclusionsTab(), 5), "        Exclusion tab should show.");
			clickExclusionTab();
			Assert.assertTrue(b.isElementPresent(pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");
			payorId = addPayorExclusions(2, b, true, testDb).get(0);
			Assert.assertTrue(b.isElementPresent(saveAndClearBtn(), 5), "        Save and Clear button should show.");
			clickSaveAndClearBtn();
			Assert.assertTrue(b.isElementPresent(testIdInput(), 5), "        Test ID input should show.");
			checkInputTestCodeId(profileId);
			Assert.assertTrue(b.isElementPresent(exclusionsTab(), 5), "        Exclusion tab should show.");
			clickExclusionTab();
			Assert.assertTrue(b.getColumnValue(pyrExclusionTbl(), payorId), "       New Payor is added to table");
			return payorId;
		}
	}
	
	//------------------------------------------------------------------------------------------
	//This method verifies if the an element is displayed
    public boolean isElementDisplayed(WebElement element, int time) throws Exception{
       boolean flag = false;
       int i = 0;
       while (i < time) {
          if(!(element.getText().isEmpty())){
                flag = true;
                LOG.info("        Element is displayed.");
                break;
          }
          Thread.sleep(1000);
          i++;
       }
       return flag;  
    }
    
	public WebElement searchInProgressInfoText(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("load_testsearchTable"))); 
	}

	public WebElement loadPayorSearchTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("load_payorsearchTable")));
	}

	public WebElement procedureTableDropdownInAddRecord(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procTypId"))); 
	}

	public boolean isSaveInProgressNotPresent(int time) throws Exception{
		boolean flag = false;
		int i = 0;

		while (i < time) {
			try{
				if(saveInProgressInfoText().isDisplayed() || saveInProgressInfoText().isEnabled() || !(saveInProgressInfoText().getAttribute("style").contains("none"))){
					LOG.info("        Save in progress is visible.");
				}
			}catch (Exception e){
				flag = true;
				LOG.info("        Save in progress is not visible.");
				break;
			}
			Thread.sleep(1000);
			i++;
		}
		return flag;
	}

	public boolean isSearchInProgressNotPresent(int time) throws Exception{
		boolean flag = false;
		int i = 0;

		while (i < time) {
			try{
				if(!(searchInProgressInfoText().getAttribute("style").contains("none"))){
					LOG.info("        Loading is visible.");
				}
			}catch (Exception e){
				flag = true;
				LOG.info("        Loading is not visible.");
				break;
			}
			Thread.sleep(1000);
			i++;
		}
		return flag;
	}
	
	public List<String> getTestCodeInfoOnHeaderOfTestCode() throws Exception {
		List<String> list = new ArrayList<>();
		list.add(0, testCodeUtils.getTextValue(testIDLabel(), 30));
		list.add(1, testCodeUtils.getAttributeValue(testNameInput(), 30));
		list.add(2, testCodeUtils.getTextSelectedInDropdown(selectTestType()));
		list.add(3, testCodeUtils.getAttributeValue(labTestCodeID(), 30));
		LOG.info("        Test Code information is " + list);
		return list;
	}
	
	public String getNewTestCodeIdNotExistedInSystem(String oldTestAbbrev) throws Exception {
		String newTestCodeId;
		
		do {
			newTestCodeId = randomCharacter.getRandomAlphaNumericString(17) + "_-_";
		} while (testDao.getTestByTestAbbrev(newTestCodeId) != null && newTestCodeId.equals(oldTestAbbrev));
		
		return newTestCodeId;
	}
	
	public void updateTestWithOldvalue(String newTestAbbrev, String oldTestAbbrev) throws XifinDataAccessException {
		Test test = testDao.getTestByTestAbbrev(newTestAbbrev);
		if (test != null) {
			test.setTestAbbrev(oldTestAbbrev);
			test.setResultCode(ErrorCodeMap.RECORD_FOUND);
			testDao.setTest(test);
			assertEquals(test.getTestAbbrev(), oldTestAbbrev, "Test Abbrev should be reverted correctly with value " + oldTestAbbrev);
		}
	}
	
	public String getDifferentTestAbbrev(String testAbberv) throws Exception {
		String newTestAbbrev;
		do {
			newTestAbbrev = daoManagerXifinRpm.getRandomTestCodeId(null).get(1);
		} while (newTestAbbrev.equals(testAbberv));
		
		return newTestAbbrev;
	}
	
	public void deleteNoLoadTestByTestAbbrev(String testAbbrev) throws XifinDataAccessException {
		LOG.info("*** Starting delete Test ***");
		Test test = testDao.getTestByTestAbbrev(testAbbrev);

		if (test != null) {
			deleteTestDtByTest(test);
			test.setResultCode(ErrorCodeMap.DELETED_RECORD);
			test.setModified(true);
			testDao.setTest(test);
			LOG.info("*** Record Deleted with test ***" + test);
		}
	}
	
	public void deleteProfileTestByTestAbbrev(String testAbbrev, Pyr pyr, PyrGrp pyrGrp) throws XifinDataAccessException {
		LOG.info("*** Starting delete Test ***");
		Test test = testDao.getTestByTestAbbrev(testAbbrev);

		if (test != null) {
			deleteTestProfByTest(test);
			testDao.deleteTestPyrExclByTestIdAndPyrId(test.getTestId(), pyr.getPyrId());
			testDao.deleteTestPyrGrpExclByTestIdAndPyrGrpId(test.getTestId(), pyrGrp.getPyrGrpId());
			deleteTestDtByTest(test);
			test.setResultCode(ErrorCodeMap.DELETED_RECORD);
			test.setModified(true);
			testDao.setTest(test);
			LOG.info("*** Record Deleted with test ***" + test);
		}
	}
	
	public void deleteSingleTestByTestAbbrev(String testAbbrev) throws XifinDataAccessException {
		LOG.info("*** Starting delete Test ***");
		Test test = testDao.getTestByTestAbbrev(testAbbrev);

		if (test != null) {
			testDao.deleteTestProc(test.getTestId());
			deleteTestDtByTest(test);
			testDao.deleteTestFacByTestId(test.getTestId());
			test.setResultCode(ErrorCodeMap.DELETED_RECORD);
			test.setModified(true);
			testDao.setTest(test);
			LOG.info("*** Record Deleted with test ***" + test);
		}
	}

	public void deleteTestDtByTest(Test test) throws XifinDataAccessException {
		LOG.info("*** Starting delete TestDt ***");
		for (TestDt testDt : testDao.getTestDtByTestAbbrv(test.getTestAbbrev())) {
			if (testDt != null) {
				testDt.setResultCode(ErrorCodeMap.DELETED_RECORD);
				testDt.setModified(true);
				testDao.setTestDt(testDt);
			}
		}
	}
	
	public void deleteTestProfByTest(Test test) throws XifinDataAccessException {
		LOG.info("*** Starting delete TestProf ***");
		for (TestProf testProf : testDao.getTestProfById(test.getTestId())) {
			if (testProf != null) {
				testProf.setResultCode(ErrorCodeMap.DELETED_RECORD);
				testProf.setModified(true);
				testDao.setTestProf(testProf);
			}
		}
	}
}
