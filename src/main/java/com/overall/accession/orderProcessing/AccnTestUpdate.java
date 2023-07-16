package com.overall.accession.orderProcessing;

import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.testng.Assert.assertTrue;

public class AccnTestUpdate {

	private final RemoteWebDriver driver;
	protected final Logger logger;
	private final WebDriverWait wait;
	private SeleniumBaseTest b;
	private final static String ID_MAIN_SECTIONS = "mainSections";
	
	public AccnTestUpdate(RemoteWebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" +driver );
		this.wait = wait;
	}

	//========Load Accession page========
	//Webelements
	public WebElement loadPageTitle(){
		return driver.findElement(By.cssSelector(".platormPageTitle"));
	}	
	
	public WebElement lookUpAccnIdInput(){
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lookupAccnId")));
	}	
	
	public WebElement accnIdSearchBtn(){
		return driver.findElement(By.id("accn_id_search_btn"));
	}
	
	public WebElement helpIconLink(String index){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"helpIcon inlineIcon\"]')[" + index + "]");
	}
	
	//Actions
	public void setLookUpAccnId(String accnId) throws Exception{		
		lookUpAccnIdInput().sendKeys(accnId);		
		lookUpAccnIdInput().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("        Entered Accession ID: " + accnId);
	}
	
	public void clickHelpIconLink(String index) throws Exception{		
		helpIconLink(index).click();
		Thread.sleep(2000);
		logger.info("        Clicked Help icon link.");
	}
	
	public void clickAccnIdSearchBtn(){
		accnIdSearchBtn().click();		
		logger.info("        Clicked Accession ID Search button.");
	}
	
	//========Accession Test Update page========
	//Webelements
	public String accnTestUpdatePageTitle(){
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[class=\"platormPageTitle\"]')[0]).text()");
	}
	
	public WebElement accnIdInput(){
		return driver.findElement(By.id("accnId"));
	}
	
	public WebElement dosInput(){
		return driver.findElement(By.id("dos"));
	}

	public WebElement ptNameInput(){
		return driver.findElement(By.id("ptFullName"));
	}
	
	public WebElement addNewRowBtn(){
		return driver.findElement(By.id("tbl_orderedAccnTestUpdateDetail_iladd"));
	}
	
	public WebElement newTestIdInput(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"editable testAbbrev\"]')[0]");
	}
	
	public WebElement newFinalReportDateInput(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[name=\"finalReportDate\"][class*=\"editable date\"]')[0]");
	}

	public WebElement saveAndClearBtn(){
		return driver.findElement(By.id("btnSaveAndClear"));
	}
	
	public WebElement newTestInfoInput(int col){
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='tbl_orderedAccnTestUpdateDetail']/tbody/tr[last()]/td["+col+"]/input")));
	}
	
	public WebElement returnedErrMsg() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='sectionServerMessages']/div/div[1]/ul")));
	}
	
	public WebElement resetBtn() {
		return driver.findElement(By.id("Reset"));
	}
	
	public WebElement testIdFilterInput(){
		return driver.findElement(By.id("gs_testAbbrev"));
	}
	
	public WebElement manualPriceInput(int accnTestSeqId){
		return driver.findElement(By.id(accnTestSeqId + "_manualPrice"));
	}
	
	public WebElement manualPriceText(int accnTestSeqId, String manualPrc){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#" + accnTestSeqId + " [title= \"" + manualPrc + "\"]')[0]");
	}
	
	public WebElement posInput(){
		return driver.findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
	}
	
	public WebElement pageHelpLink(){
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"accnTestUpdateForm\"]/div[2]/div[3]/div/div[1]/div[1]/div")));
		}
	
	public WebElement clnBillingRulesChkbox(){
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("checkClientBillingRules")));
	}	
	
	public WebElement roundTripChkbox(){
		return driver.findElement(By.id("checkRoundTrip"));
	}
	
	public WebElement mileageInput(){
		return driver.findElement(By.id("mileage"));
	}
	
	public WebElement phlebotomyStopsInput(){
		return driver.findElement(By.id("phlebotomyStops"));
	}
	
	public WebElement totalPatientsInput(){
		return driver.findElement(By.id("totalPatients"));
	}
	
	public WebElement testId(int accnTestSeqId)
	{
		return driver.findElement(By.id(""+accnTestSeqId+""));
	}
	
	public WebElement deleteChkbox(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[tabindex=\"0\"] [name=\"isDelete\"]')[0]");
	}
	
	public WebElement clnIdInput(){
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("clientAbbrev")));
	}
	
	public WebElement mod1DropDownArrowBtn(String accnTestSeqId){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[aria-describedby=\"tbl_orderedAccnTestUpdateDetail_mod1\"] #s2id_" + accnTestSeqId + "_mod1 [class=\"select2-arrow\"]')[0]");
	}
	
	public WebElement mod1DropDown(String accnTestSeqId){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[aria-describedby=\"tbl_orderedAccnTestUpdateDetail_mod1\"] #" + accnTestSeqId + "_mod1')[0]");			
	}
	
	public WebElement mod1DropDown(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"editable mod1Select\"]')[0]");
	}
	
	public WebElement mod2DropDown(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"editable mod2Select\"]')[0]");
	}
	
	public WebElement unitsInput(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[name=\"units\"][class=\"editable\"]')[0]");
	}
	
	public WebElement labMsgInput(){
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='tbl_orderedAccnTestUpdateDetail']/tbody/tr[last()]/td[21]/div/ul/li/input")));
	}	
	
	public WebElement labMsgDropdown(String accnTestSeqId){
		return driver.findElement(By.id(accnTestSeqId + "_labMessage"));
	}
	
	public WebElement searchInputInDropdown(){
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='select2-drop']/div/input")));
	}
	
	public WebElement renalDropdown(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"editable renalSelect\"]')[0]");
	}
	
	public WebElement posDropdown(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class*=\"editable postSelect\"]')[0]");
	}
	
	public WebElement renderingPhysDropdown(int accnTestSeqId){
		return driver.findElement(By.id(accnTestSeqId + "_renderingPhysician"));
	}
	
	public WebElement labNotesInput(int accnTestSeqId){
		return driver.findElement(By.id(accnTestSeqId + "_notes"));
	}
	
	public WebElement testValues(int accnTestSeqId, String value){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#" + accnTestSeqId + " [title=\"" + value + "\"]')[0]");
	}
	
	public WebElement selectedRenderingPhys(int accnTestSeqId){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#" + accnTestSeqId + " [aria-describedby=\"tbl_orderedAccnTestUpdateDetail_renderingPhysician\"]')[0]");
	}
	
	public WebElement clnBillChkbox(int accnTestSeqId){
		//return driver.findElement(By.id(accnTestSeqId + "_isClnBill"));
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#" + accnTestSeqId + "_isClnBill')[0]");
	}
	
	public WebElement saveInProgressInfoText(){
		return driver.findElement(By.id("messagefor_message0")); 
	}
	
	public WebElement renderingPhysSearchBtn(){
		//return driver.findElement(By.xpath(".//*[@id='physicianSearch']/div/span"));
		return driver.findElement(By.id("physicianSearchButton"));
	}

	public WebElement newRenderingPhys(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[editable=\"1\"] [aria-describedby*=\"renderingPhysician\"]')[0]");
	}	
	
	public WebElement linkedAccnsLink(){
		return driver.findElement(By.id("btnLinkedAccns"));
	}
	
	public WebElement linkedAccnsList(int index) {		
		return driver.findElement(By.xpath(".//*[@id='messagefor_btnLinkedAccns']/div[2]/ul/li[" + index + "]/a"));
	}
	
	public WebElement payorName(){
		return driver.findElement(By.id("payorName"));
	}
	
	public WebElement helpHeaderIconLink(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[data-help-id=\"p_accession_test_update_header\"]')[0]");
	}
	
	public WebElement helpSectionIconLink() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[data-help-id=\"p_accession_test_update_section\"]')[0]");
	}

	//Actions
	public void clickLinkedAccnsList(int index) {
		linkedAccnsList(index).click();
		logger.info("        Clicked Linked Accession.");
	}
	
	public void clickLinkedAccnsLink(){
		linkedAccnsLink().click();
		logger.info("        Clicked Linked Accessions Link");
	} 
	
	public void clickRenderingPhysSearchBtn() {
		renderingPhysSearchBtn().click();
		logger.info("        Checked Rendering Physician Search button.");
	}
	
	//This method checks if the Save is done 
	public boolean isSaveDone(int time) throws Exception{
		boolean flag = false;
		int i = 0;		
		
		while (i < time) {
			try{
				if(saveInProgressInfoText().isDisplayed() || saveInProgressInfoText().isEnabled() || !(saveInProgressInfoText().getAttribute("style").contains("none"))){					
					logger.info("        Save is not done.");					
				}
			}catch (Exception e){		
				flag = true;
				logger.info("        Save is done.");	
				break;
			}
			Thread.sleep(1000);
			i++;		
		}
		
		return flag;	
	}
	
	public void clickClnBillChkbox(int accnTestSeqId){
		clnBillChkbox(accnTestSeqId).click();
		logger.info("        Checked Cln Bill checkbox.");
	}
	
	public void setClnIdInput(String value)throws Exception{
		clnIdInput().clear();
		clnIdInput().sendKeys(value);
		clnIdInput().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("        Entered Client ID: " + value);
	}
	
	public void setLabNotes(int accnTestSeqId, String value){
		labNotesInput(accnTestSeqId).clear();
		labNotesInput(accnTestSeqId).sendKeys(value);
		labNotesInput(accnTestSeqId).sendKeys(Keys.TAB);
		logger.info("        Entered Lab Notes: " + value);
	}
	
	public void clickTestId(int accnTestSeqId) {
		wait.until(ExpectedConditions.elementToBeClickable(testId(accnTestSeqId)));
		testId(accnTestSeqId).click();
		logger.info("        Clicked the Test row.");
	}
	
	public void setMileage(String value){
		mileageInput().clear();
		mileageInput().sendKeys(value);
		mileageInput().sendKeys(Keys.TAB);
		logger.info("        Entered Mileage: " + value);
	}
	
	public void setPhlebotomyStops(String value){
		phlebotomyStopsInput().clear();
		phlebotomyStopsInput().sendKeys(value);
		phlebotomyStopsInput().sendKeys(Keys.TAB);
		logger.info("        Entered Phlebotomy Stops: " + value);
	}
	
	public void setTotalPatients(String value){
		totalPatientsInput().clear();
		totalPatientsInput().sendKeys(value);
		totalPatientsInput().sendKeys(Keys.TAB);
		logger.info("        Entered Total Patients: " + value);
	}
	
	public void clickPageHelpLink() {
		pageHelpLink().click();
		logger.info("        Clicked the Page Help link.");
	}
	
	public void clickAddNewRowBtn() {
		addNewRowBtn().click();
		logger.info("        Clicked the Add new row button.");
	}
	
	public void setTestId(String testAbbrev) throws Exception{		
		newTestIdInput().sendKeys(testAbbrev);		
		newTestIdInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		logger.info("        Entered Test ID: " + testAbbrev);
	}
	
	public void setFinalReportDate(String date) throws Exception{
		Thread.sleep(1000);
		newFinalReportDateInput().clear();
		newFinalReportDateInput().sendKeys(date);		
		newFinalReportDateInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Final Report Date: " + date);
	}
	public void setProfileFinalReportDate(String date) throws Exception{
		newFinalReportDateInput().clear();
		newFinalReportDateInput().sendKeys(date);
		newFinalReportDateInput().sendKeys(Keys.TAB);
		newFinalReportDateInput().sendKeys(date);
		newFinalReportDateInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Final Report Date: " + date);
	}
	
	public void clickSaveAndClearBtn() throws Exception{
		saveAndClearBtn().click();
		Thread.sleep(8000);
		logger.info("        Clicked Save And Clear button.");
	}

	public void setNewTestInfo(int row, int col, String value){
		newTestInfoInput(col).clear();
		newTestInfoInput(col).sendKeys(value);

		logger.info("       Entered "+value+" to "+" Row "+row+" and Column "+col);
	}
	
	public void clickResetBtn() throws Exception{
		resetBtn().click();
		Thread.sleep(2000);
		logger.info("        Clicked the Reset button.");
	}
	
	public void setTestIdFilter(String testAbbrev) throws Exception{		
		testIdFilterInput().sendKeys(testAbbrev);		
		testIdFilterInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		logger.info("        Entered Test ID: " + testAbbrev + " in the filter.");
	}
	
	public void setPOS(String pos) {
		posDropdown().click();
		posInput().sendKeys(pos);
		posInput().sendKeys(Keys.TAB); 
		logger.info("        Entered POS "+ pos);
	}
	
	public void setUnits(String value) throws Exception{
		unitsInput().clear();		
		unitsInput().sendKeys(value);
		Thread.sleep(2000);
		unitsInput().sendKeys(Keys.TAB); 
		Thread.sleep(2000);
		logger.info("        Entered Units: "+ value);
	}
	
	public void setLabMsg(String value) {
		labMsgInput().click();
		labMsgInput().sendKeys(value);
		labMsgInput().sendKeys(Keys.TAB); 
		logger.info("        Entered Lab Message: "+ value);
	}
	
	public void setValueInDropdown(WebElement e, String value)throws Exception{
		e.findElement(By.xpath(".//a/span[2]/b")).click();
		searchInputInDropdown().sendKeys(value);
		Thread.sleep(2000);
		searchInputInDropdown().sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		logger.info("        Selected : "+ value);
	}
	
	public WebElement modDropdown(int testSeqId, String seqId){
		return driver.findElement(By.id(testSeqId + "_mod" + seqId));
	}
	
	public WebElement unitsInput(int testSeqId){
		return driver.findElement(By.id(testSeqId + "_units"));
	}
	
	public WebElement testAbbrev(String accnTestSeqId){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#" + accnTestSeqId + "')[0]");
	}
	
	public void setUnits(int testSeqId, String value) throws Exception{
		unitsInput(testSeqId).clear();	
		Thread.sleep(2000);
		unitsInput(testSeqId).sendKeys(value);
		Thread.sleep(2000);
		unitsInput(testSeqId).sendKeys(Keys.TAB);
		Thread.sleep(2000);
		logger.info("        Entered Units: "+ value);
	}
	
	public WebElement checkClnBillChkbox(int accnTestSeqId){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#" + accnTestSeqId + "_isClnBill').click()");
	}
	
	public WebElement clickLinkedLnk(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#btnLinkedAccns').click()");
	}
	
	public WebElement clickTestAbbrev(int accnTestSeqId){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#" + accnTestSeqId + "').click()");
	}
	
	public WebElement clickHelpIconLinks(String index){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('[class=\"helpIcon inlineIcon\"]')[" + index + "].click()");
	}
	
	public WebElement clickHelpIconHeaderLink(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('[data-help-id=\"p_accession_test_update_header\"]')[0].click()");
	}
	
	public WebElement clickHelpIconSectionLink(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('[data-help-id=\"p_accession_test_update_section\"]')[0].click()");
	}	
	
	public void updateTestForAllFields (int accnTestSeqId, String mod1, String mod2, String unit, String manualPrice, String labMsg , String labNotes, String POS, int renderingPhys, String finalReportDt, String renal) throws Exception {
		SeleniumBaseTest b = new SeleniumBaseTest();
		
		//Set Mod1
		assertTrue(b.isElementPresent(modDropdown(accnTestSeqId, "1"), 5),"       Mod1 Dropdown is displayed.");
		b.selectItemByVal(modDropdown(accnTestSeqId, "1"), mod1);
		Thread.sleep(2000);
		//Set Mod2
		assertTrue(b.isElementPresent(modDropdown(accnTestSeqId, "2"), 5),"       Mod2 Dropdown is displayed.");
		b.selectItemByVal(modDropdown(accnTestSeqId, "2"), mod2);
		Thread.sleep(2000);
		//Set Unit		
		assertTrue(b.isElementPresent(unitsInput(accnTestSeqId), 5),"       Unit Input is displayed.");
		setUnits(accnTestSeqId, unit);

//		manualPriceInput(accnTestSeqId).clear();
		assertTrue(b.isElementPresent(newTestInfoInput(19),5),"       Manual Price Input field should be displayed.");
		setNewTestInfo(2,19,manualPrice);

		assertTrue(b.isElementPresent(labMsgInput(), 5),"       Lab Message Input is displayed.");
		setLabMsg(labMsg);
		
		assertTrue(b.isElementPresent(labNotesInput(accnTestSeqId), 5),"       Lab Notes Input is displayed.");
		setLabNotes(accnTestSeqId, labNotes);		

		assertTrue(b.isElementPresent(renderingPhysDropdown(accnTestSeqId), 5),"       Rendering Physician Input is displayed.");
		//b.selectItem(renderingPhysDropdown(accnTestSeqId), renderingPhys);
		b.selectItemByVal(renderingPhysDropdown(accnTestSeqId), String.valueOf(renderingPhys));

		assertTrue(b.isElementPresent(posDropdown(), 5),"       POS dropdown list is displayed.");
		setValueInDropdown(posDropdown(),POS);

		assertTrue(b.isElementPresent(newFinalReportDateInput(), 5),"       Final Report Date Input is displayed.");
		setFinalReportDate(finalReportDt);

//		assertTrue(b.isElementPresent(renalDropdown(), 5),"       Renal dropdown is displayed.");
//		setValueInDropdown(renalDropdown(),renal);
	}
	
	public boolean isAccnLoaded(String accnId, WebDriverWait wait)
	{
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_MAIN_SECTIONS))));		
		wait.until(ExpectedConditions.visibilityOf(saveAndClearBtn()));
		return StringUtils.equalsIgnoreCase(StringUtils.trim(accnIdInput().getAttribute("value")), accnId);
	}

	public WebElement renderingPhysicianResultDropDown(){
		return driver.findElement(By.xpath("//*[@id=\"select2-drop\"]/ul/li/div"));
	}
	public void clickNewRenderingPhys() {
		newRenderingPhys().click();
		logger.info("        Checked Rendering Physician Search button.");
	}
	public WebElement renderingPhysInputBox()
	{
		return driver.findElement(By.xpath("//*[@id=\"select2-drop\"]/div/input"));
	}

	public void setNPIInput(String value) {
		renderingPhysInputBox().sendKeys(value);
		renderingPhysInputBox().sendKeys(Keys.TAB);
		logger.info("        Entered NPI: " + value);
	}
}

