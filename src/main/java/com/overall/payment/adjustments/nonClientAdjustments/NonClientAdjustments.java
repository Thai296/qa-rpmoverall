package com.overall.payment.adjustments.nonClientAdjustments;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.xifin.utils.SeleniumBaseTest;

public class NonClientAdjustments {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;

	public NonClientAdjustments(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		this.wait=wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*Non client Adj table*/
	public WebElement nonClnAdjPageTitle(){
		return driver.findElement(By.xpath("//*[@id=\"nonClientAdjustmentsForm\"]/div/div[1]/div/div/div[2]/span"));
	}
	public WebElement accnIDFilterInput(){
		return driver.findElement(By.id("gs_accnId"));
	}
	public WebElement procCodeFilterInput(){
		return driver.findElement(By.id("gs_procdCd"));
	}
	public WebElement mod1FilterInput(){
		return driver.findElement(By.id("gs_mod1"));
	}
	public WebElement unitFilterInput(){
		return driver.findElement(By.id("gs_units"));
	}
	public WebElement billFilterInput(){
		return driver.findElement(By.id("gs_bill"));
	}
	public WebElement AdjPyrIDFilterInput(){
		return driver.findElement(By.id("gs_adjPyrId"));
	}
	public WebElement adjCdFilterInput(){
		return driver.findElement(By.id("gs_adjCd"));
	}
	public WebElement adjFilterInput(){
		return driver.findElement(By.id("gs_adj"));
	}
	public WebElement adjCmtFilterInput(){
		return driver.findElement(By.id("gs_adjCmt"));
	}
	public WebElement printFilterInput(){
		return driver.findElement(By.id("gs_print"));
	}
	public WebElement anticipatedNewFilterInput(){
		return driver.findElement(By.id("gs_anticipatedNew"));
	}
	public WebElement currentFilterInput(){
		return driver.findElement(By.id("gs_current"));
	}
	public WebElement newFilterInput(){
		return driver.findElement(By.id("gs_valNew"));
	}
	public WebElement deleteRowBtn(){
		return driver.findElement(By.id("jqgh_tbl_nonClientAdjustment_deleted"));
	}
	public WebElement rowTableNonClientAdj(int rowNumber){
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='tbl_nonClientAdjustment']//tr["+rowNumber+"]")));
	}
	public WebElement cellTableNonClientAdj(int colNumber, int rowNumber){
		return driver.findElement(By.xpath("//*[@id='tbl_nonClientAdjustment']//tr["+rowNumber+"]/td["+colNumber+"]"));
	}
	public WebElement delNonClientAdjCheckbox(int rowNumber){
		return driver.findElement(By.xpath("//*[@id='tbl_nonClientAdjustment']//tr["+rowNumber+"]/td[@aria-describedby='tbl_nonClientAdjustment_deleted']//input[@type='checkbox']"));
	}
	public WebElement printNonClientAdjCheckbox(int rowNumber){
		return driver.findElement(By.xpath("//*[@id='tbl_nonClientAdjustment']//tr["+rowNumber+"]/td[@aria-describedby='tbl_nonClientAdjustment_print']//input[@type='checkbox']"));
	}
	public WebElement editBtn(){
		return driver.findElement(By.id("edit_tbl_nonClientAdjustment"));
	}
	public WebElement cellTableNonClientAdjChecbox(int colNumber, int rowNumber){
		return driver.findElement(By.xpath("//*[@id='tbl_nonClientAdjustment']//tr["+rowNumber+"]/td["+colNumber+"]/input"));
	}
	public WebElement nonClnAdjTable(){
		return driver.findElement(By.id("tbl_nonClientAdjustment"));
	}
	
	public WebElement currentPageNumber(){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='tbl_nonClientAdjustment_pagernav_center']/table/tbody/tr/td[4]/input")));
	}
	public WebElement totalPageNumber(){
		return driver.findElement(By.xpath(".//*[@id='sp_1_tbl_nonClientAdjustment_pagernav']"));
	}
	public WebElement firstPageBtn(){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='first_tbl_nonClientAdjustment_pagernav']/span")));
	}
	public WebElement previousPageBtn(){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='prev_tbl_nonClientAdjustment_pagernav']/span")));
	}
	public WebElement nextPageBtn(){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='next_tbl_nonClientAdjustment_pagernav']/span")));
	}
	public WebElement lastPageBtn(){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='last_tbl_nonClientAdjustment_pagernav']/span")));
	}
	
	public WebElement row10(){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='tbl_nonClientAdjustment']//tr[11]/td[@title='10']")));
	}
	
	public WebElement totalPageLbl(){
		return driver.findElement(By.id("sp_1_tbl_nonClientAdjustment_pagernav"));
	}
	
	public WebElement errorHeaderContent(){
		return driver.findElement(By.xpath(".//*[@id='sectionServerMessages']/div/div[2]/ul/li[1]/span"));
	}

	/*genral element*/
	public WebElement uploadBtn(){
		return driver.findElement(By.id("btn_uploadNonClnAdj"));
	}
	
	public WebElement chooseFileBtn(){
		return driver.findElement(By.id("btnUpload"));
	}
	
	public WebElement xfnUploaderFile(){
		return driver.findElement(By.id("fileUpload"));
	}
	
	public WebElement cancelUploadDocumentBtn(){
		return driver.findElement(By.id("btn_cancelUploadDocuments"));
	}
	
	public WebElement uploadUploadDocumentBtn(){
		return driver.findElement(By.id("btn_uploadUploadDocuments"));
	}
	
	public WebElement resetBtn(){
		return driver.findElement(By.id("Reset"));
	}
	
	public WebElement postBtn(){
		return driver.findElement(By.id("btnSave"));
	}
	
	public WebElement helpIcon(){
		return driver.findElement(By.id("pageHelpLink"));
	}
	
	public WebElement errorsReturnedLb(){
		return driver.findElement(By.xpath("//div[@class='serverErrorsTitle']"));
	}
	
	public WebElement messagesReturnedLb(){
		return driver.findElement(By.xpath("//div[@class='serverInfoTitle']"));
	}
	
	public WebElement pageTitleLb(){
		return driver.findElement(By.xpath("//div[@class='step_non_cln_adj widePage']//span[@class='platormPageTitle']"));
	}
	
	public WebElement messagesReturnedAreabox(){
		return driver.findElement(By.xpath("//ul[@class='serverInfoList']"));
	}
	public WebElement helpIconOnTable(){
		  return driver.findElement(By.xpath(".//*[@id='mainSections']/div[2]/div[2]/div/div/section/div/div[1]/a"));
	}
	
	//Error or System message returned text
	public String msgReturnedTxt(String index){
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[class=\"serverInfoList\"] li>span')[" + index + "]).text()");		
	}
	
	public void clickHelpIconOnTable() throws Exception{
		  helpIconOnTable().click();
		  logger.info("        Clicked Help Icon.");
	}

	public String getTextCellTableNonClientAdj(int colNumber, int rowNumber){
		return cellTableNonClientAdj(colNumber,rowNumber).getText();
	}
	
	public String errMessage(){
		return (String) ((JavascriptExecutor) driver).executeScript("return $(\"#messagefor_message0 [class=xf_message_content]\").text()");		
	}	
	
	public List<String> getListRowDataInColumn(int colNumber){
		int totalRows = driver.findElements(By.xpath("//*[@id='tbl_nonClientAdjustment']/tbody/tr")).size() -1;
		List<String> returnList = new ArrayList<>();
		for (int i = 1; i <= totalRows; i++) {
			String newDollar = getTextCellTableNonClientAdj(colNumber, i+1);
			returnList.add(newDollar);
		}
		return returnList;
	}
	
	public int countRowInNonClientAdjTable(){
		int totalRows = driver.findElements(By.xpath("//*[@id='tbl_nonClientAdjustment']/tbody/tr")).size() -1;
		return totalRows;
	}
	
	public void clickUploadBtn() throws Exception{
		uploadBtn().click();
		Thread.sleep(2000);
		logger.info("        Clicked Upload button.");
	}
	public void clickRowOnTable(int row,int column) throws Exception{
		cellTableNonClientAdj(row,column).click();
		logger.info("        Clicked Row "+row+" and Column "+column+".");
	}
	
	public void clickCancelUploadDocumentBtn() throws Exception{
		cancelUploadDocumentBtn().click();
		logger.info("        Clicked Cancel Upload Document button.");
	}
	
	public void clickUploadUploadDocumentBtn() throws Exception{
		uploadUploadDocumentBtn().click();
		logger.info("        Clicked Upload Upload Document button.");
	}
	
	public void clickResetBtn() throws Exception{
		resetBtn().click();
		logger.info("        Clicked Reset button.");
	}
	
	public void clickPostBtn() throws Exception{
		postBtn().click();
		logger.info("        Clicked Post button.");
	}
	
	public void clickHelpIcon() throws Exception{
		helpIcon().click();
		logger.info("        Clicked Help Icon.");
	}
	
	public void chooseFileUpload(SeleniumBaseTest b, String fileName) throws Exception {
		b.uploadFile(xfnUploaderFile(), fileName);
	}
	public void clickEditBtn() throws Exception{
		editBtn().click();
		logger.info("        Clicked Edit button.");
}
	
	/*Edit window*/
	public WebElement adjID(){
		return driver.findElement(By.id("adj"));
	}
	
	public WebElement adjComment(){
		return driver.findElement(By.id("adjCmt"));
	}
	
	public WebElement printChkbox(){
		return driver.findElement(By.id("print"));
	}
	
	public WebElement deleteRowChkBox(){
		return driver.findElement(By.id("deleted"));
	}
	
	public WebElement OKBtn(){
		return driver.findElement(By.id("sData"));
	}
	
	public WebElement cancelBtn(){
		return driver.findElement(By.id("cData"));
	}
	
	public WebElement editPopupTitle(){
		return driver.findElement(By.xpath(".//*[@id='edithdtbl_nonClientAdjustment']/span"));
	}
	
	public void clickOKBtn() throws Exception{
		OKBtn().click();
		logger.info("        Clicked OK button.");
	}
	
	public void clickCancelBtn() throws Exception{
		cancelBtn().click();
		logger.info("        Clicked Cancel button.");
	}
	
	public void clickPrintChkBox() throws Exception{
		printChkbox().click();
		logger.info("        Clicked Print checkbox.");
	}
	
	public void clickDeleteRowChkBox() throws Exception{
		deleteRowChkBox().click();
		logger.info("        Clicked Delete Row checkbox.");
	}
	
	public void adjAmtInput(String adjAmt) throws Exception{
		adjID().clear();
		adjID().sendKeys(Keys.BACK_SPACE);
		adjID().sendKeys(Keys.BACK_SPACE);
		adjID().sendKeys(Keys.BACK_SPACE);
		adjID().sendKeys(Keys.BACK_SPACE);
		adjID().sendKeys(adjAmt);
		logger.info("        Entered Adj $: " + adjAmt);
	}
	
	public void adjCommentInput(String adjCommentStr) throws Exception{
		adjComment().sendKeys(adjCommentStr);
		logger.info("        Entered AdjComment: " + adjCommentStr);
	}
	
	public WebElement helpIconOnEditPopup(){
		return driver.findElement(By.xpath(".//*[@id='editmodtbl_nonClientAdjustment']/div[1]/a"));
	}
	
	public void clickHelpIconOnEditPopup() throws Exception{
		helpIconOnEditPopup().click();
		logger.info("        Clicked on Help Icon On Edit Popup.");
	}
	
	public void setCurrentPage(String pageNumber) throws Exception{
		currentPageNumber().sendKeys(pageNumber);
		logger.info("        Enter "+pageNumber+" to jump to page number :"+pageNumber+".");
	}
	public void clickFirstPage() throws Exception{
		firstPageBtn().click();
		Thread.sleep(1000);
		logger.info("        Clicked First page button.");
	}
	public void clickNextPage() throws Exception{
		nextPageBtn().click();
		Thread.sleep(1000);
		logger.info("        Clicked Next page button.");
	}
	public void clickPreviousPage() throws Exception{
		previousPageBtn().click();
		Thread.sleep(1000);
		logger.info("        Clicked Previous page button.");
	}
	public void clickLastPage() throws Exception{
		lastPageBtn().click();
		Thread.sleep(1000);
		logger.info("        Clicked Last page button.");
	}	
	
}

