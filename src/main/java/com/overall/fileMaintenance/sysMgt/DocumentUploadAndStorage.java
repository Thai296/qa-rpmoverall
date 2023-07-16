package com.overall.fileMaintenance.sysMgt;

import com.xifin.accnws.dao.DaoManagerAccnWS;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.utils.SeleniumBaseTest;
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

import java.util.ArrayList;
import java.util.List;

public class DocumentUploadAndStorage
{
	public static final String PAGE_TITLE = "Document Upload And Storage";
	public static final String DUPLICATE_DOCUMENT_ERROR = "Is an existing document";
	public static final String MATCHING_CONTENT_ERROR = "Matches an existing document";
	public static final String EMPTY_DOCUMENT_ERROR = "Is an empty document";
	public static final String SYSTEM_CATEGORY = "System";

	private RemoteWebDriver driver;
	private WebDriverWait wait;

	protected Logger logger;
	
	public DocumentUploadAndStorage(RemoteWebDriver driver, WebDriverWait wait)
	{
		this.driver = driver;
		this.wait = wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public WebElement pageTitle()
	{
		return driver.findElement(By.xpath("html/body/section/div/div/section/header/span"));
	}

	public WebElement uploadDocumentButton()
	{
		return driver.findElement(By.id("btn_uploadDocuments"));
	}

	public WebElement xfnUploaderFile()
	{
		return driver.findElement(By.id("xifinUpload_uploader"));
	}

	public WebElement categoryDropdown()
	{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('select.documentCategoryUpload.comboboxCategoryUploadDialog')).get(0)");
	}

	public WebElement categoryIdInput()
	{

		return driver.findElement(By.id("lookupAccnId"));

	}

	public void setCategoryId(String value)
	{
		WebElement categoryIdInput = categoryIdInput();
		categoryIdInput.clear();
		categoryIdInput.sendKeys(value);
	}

	public WebElement documentTypeDropdown()
	{
		return driver.findElement(By.xpath("//label[contains(.,'Document Type')]/following::select[1]"));
	}

	public void setDocumentType(String value)
	{
		WebElement documentTypeDropdown = documentTypeDropdown();
		documentTypeDropdown.click();
		documentTypeDropdown.sendKeys(value);
	}

	public WebElement uploadButton()
	{
		return driver.findElement(By.id("btn_uploadUploadDocuments"));
	}

	public void clickUploadButton()
	{
		WebElement uploadButton = uploadButton();
		uploadButton.click();
		wait.until(ExpectedConditions.invisibilityOf(uploadButton));
	}

	public WebElement document(String filename)
	{
		return driver.findElement(By.xpath("//*[@id='currentDocumentGird']//td[@title='"+filename+"']"));
	}

	public void clickSaveDocumentsButton()
	{
		wait.until(ExpectedConditions.elementToBeClickable(By.id("btn_saveDocuments"))).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("xf_dialog_progress_msg_div")));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("malwareScanningStatus")));
	}

	public void clickDialogUploadErrorCancelButton()
	{
		WebElement dialogUploadErrorCancelButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='ui-dialog-buttonset']/button[contains(., 'Cancel')]")));
		dialogUploadErrorCancelButton.click();
		wait.until(ExpectedConditions.invisibilityOf(dialogUploadErrorCancelButton));
	}

	public WebElement cancelUploadButton()
	{
		return driver.findElement(By.id("btn_cancelUploadDocuments"));
	}

	public WebElement dialogUploadConfirm()
	{
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dialogUploadConfirm")));
	}

	public WebElement documentLinks(String filename)
	{
		return driver.findElement(By.xpath("//*[@id='currentDocumentGird']//a[@data-filename='"+filename+"']"));
	}

	public WebElement loadDocumentButton()
	{
		return driver.findElement(By.id("btn_searchDocument"));
	}

	public WebElement checkboxOnDocument(int id)
	{
		return driver.findElement(By.xpath(".//*[@id='"+id+"']/td[2]/input"));
	}

	public WebElement uploadDateOnDocument(int id)
	{
		return driver.findElement(By.xpath(".//*[@id='" + id + "']/td[5]"));
	}

	public WebElement uploadByOnDocument(int id)
	{
		return driver.findElement(By.xpath(".//*[@id='"+id+"']/td[6]/div"));
	}

	public WebElement documentTypeOnDocument(int id)
	{
		return driver.findElement(By.xpath(".//*[@id='"+id+"']/td[8]/div"));
	}

	public WebElement deleteSelectedRowButton()
	{
		return driver.findElement(By.id("btn_deleteDocuments"));
	}

	public WebElement confirmDeleteButton()
	{
		return driver.findElement(By.id("btn_deleteDocument"));
	}

	public WebElement categoryIdSearchByAccessionButton()
	{
		return driver.findElementByXPath("//div[contains(@class, 'dialogUploadContainer')]//a[contains(@class, 'accnSearch')]");
	}

	public WebElement viewByDropdownList()
	{
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("viewBy")));
	}

	public WebElement loadDocumentInput()
	{
		return driver.findElement(By.id("userSelectedId"));
	}

	public WebElement commentInput()
	{
		return driver.findElementByXPath("//div[contains(@class, 'dialogUploadContainer')]//textArea[contains(@class, 'commentDocument')]");
	}

	public void setLoadDocumentInput(String text)
	{
		WebElement loadDocumentInput = loadDocumentInput();
		loadDocumentInput.clear();
		loadDocumentInput.sendKeys(text);
	}

	public void setCommentInput(String text)
	{
		WebElement commentInput = commentInput();
		commentInput.click();
		commentInput.clear();
		commentInput.sendKeys(text);
	}

	// todo: afa
	
	public WebElement editSelctRowBtn() throws Exception {
		return driver.findElement(By.id("btn_editDocuments"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_cancelDocuments"));
	}
	
	public WebElement helpLink() throws Exception {
		return driver.findElement(By.id("pageHelpLink"));
	}
	
	public WebElement closeMessageBtn(){
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('span.ui-button-text')).get(2)");
	}
	
	public void selectviewByDropdownList(String value) throws Exception{
		Select dropdown = new Select(viewByDropdownList());
		dropdown.selectByValue(value);
		Thread.sleep(3000);
		logger.info("        Selected "+value+" in View By dropdown list");	
	}
	
	public void clickCloseErrorMessage() throws Exception{
		errorMessageCloseBtn().click();
		logger.info("        Clicked Close button in Error message popup window.");
	}
	
	
	// Element of Tbl Current Document
	public WebElement curDocumentTbl() throws Exception {
		return driver.findElement(By.id("currentDocumentGird"));
	}
	
	public WebElement allboxCheckbox() throws Exception {
		return driver.findElement(By.id("checkboxAll"));
	}

	
	public WebElement fromDateInput() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/thead/tr[3]/th[5]/input[1]"));
	}
	
	public WebElement toDateInput() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/thead/tr[3]/th[5]/input[2]"));
	}
	
	public WebElement curDocumentTblFilter(int row, int col) throws Exception{
		return driver.findElement(By.xpath("//*[@id='currentDocumentGird']/thead/tr["+ row +"]/th["+ col +"]/input"));
	}
	
	public WebElement currentDocumentsGrid(int row, int col) throws Exception{
		WebDriverWait wait = new WebDriverWait(driver, 100);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='currentDocumentGird']/tbody/tr[" + row + "]/td[" + col + "]")));
		return element;
	}
	
	public WebElement currentDocumentsGrid(int col) throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tbody//td["+col+"]"));
	}
	
	public WebElement documentLinks(int row, int col) throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tbody/tr["+row+"]/td["+col+"]/div"));
	}

	public WebElement documentNotPresent(String documentFileName) throws Exception{
		try{
//			return driver.findElement(By.xpath("//*[@id='currentDocumentGird']//td[@title='"+documentFileName+"']"));
			return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('#currentDocumentGird//a[data-filename=\""+documentFileName+"\"]')).get(0)");
		} catch (Exception e){
			return null;
		}
	}
	
	public WebElement documentCol(String documentFileName, int col) throws Exception{
//		return element[n] next to document column which have row = documentFileName
//		Example : Get column "Date uploaded" of Document file a.txt => documentCol("a.txt", 1) because Date uploaded next to Document column 1 step.
		return driver.findElement(By.xpath("//*[@id='currentDocumentGird']//td[@title='"+documentFileName+"']/following::td["+col+"]"));
	}
	public WebElement documentColComment(String documentFileName) throws Exception{
		return driver.findElement(By.xpath("//*[@id='currentDocumentGird']//td[@title='"+documentFileName+"']/following::td[4]/div[1]"));
	}
	public WebElement firstIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tfoot/tr/td/div/div[1]/div/div[1]/span"));
	}
	
	public WebElement prevIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tfoot/tr/td/div/div[1]/div/div[2]/span"));
	}
	
	public WebElement curPageInput() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tfoot/tr/td/div/div[1]/div/div[5]/input"));
	}
	
	public WebElement totalPagelb() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tfoot/tr/td/div/div[1]/div/div[6]/span"));
	}
	
	public WebElement nextIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tfoot/tr/td/div/div[1]/div/div[8]/span"));
	}
	
	public WebElement endIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tfoot/tr/td/div/div[1]/div/div[9]/span"));
	}
	
	public WebElement pageSetDropdown() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tfoot/tr/td/div/div[1]/div/div[10]/select"));
	}
	
	public WebElement viewOfValue() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tfoot/tr/td/div/div[2]/span"));
	}
	
	public WebElement noResult() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tbody/tr/td"));
	}

	public void enterFromDateInput(String value) throws Exception{
		fromDateInput().sendKeys(value);
		logger.info("        Entered From Date " + value + " in Current Document table.");
	}
	
	public void enterToDateInput(String value) throws Exception{
		toDateInput().sendKeys(value);
		logger.info("        Entered To Date " + value + " in Current Document table.");
	}
	
	public void enterCurDocumentTblCell(WebElement element,String value) throws Exception{
		element.sendKeys(value);
		logger.info("        Enter Data "+ value + " in Current Document table.");
	}
	
	public void enterCurPageInput(String value) throws Exception{
		curPageInput().sendKeys(value);
		logger.info("        Entered Current Page in Current Document table.");
	}	
	
	// Elements of Edit Document Popup
	public WebElement editDocumentPopupTitle() throws Exception{
		return driver.findElement(By.id("ui-dialog-title-1"));
	}
	
	public WebElement documentNameLbl() throws Exception{
		return driver.findElement(By.xpath("html/body/div[3]/div[2]/div[1]/div[2]/div[2]"));
	}
	
	public WebElement documentDateUploadLbl() throws Exception{
		return driver.findElement(By.id("documentDateUpload"));
	}
	
	public WebElement documentUploadByLbl() throws Exception{
		return driver.findElement(By.id("documentUploadedBy"));
	}
	
	public WebElement editCategoryDropdownList() throws Exception{
		return driver.findElement(By.id("documentCategory"));
	}
	
	public WebElement categoryIdSearchBtn() throws Exception{
		return driver.findElement(By.xpath("html/body/div[3]/div[2]/div[1]/div[4]/div[4]/a[2]/span"));
	}
	
	public WebElement categoryIdEditAccessionInput() throws Exception{
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($.find('input.documentCategoryId.categoryIDEditDialog')).get(0)");
	}
	
	public WebElement editDocumentCommentInput() throws Exception{
		return driver.findElement(By.id("commentDocument"));
	}
	
	public WebElement cancelEditBtn() throws Exception{
		return driver.findElement(By.id("btn_cancelEditDocuments"));
	}
	
	public WebElement saveEditBtn() throws Exception{
		return driver.findElement(By.id("btn_saveEditDocuments"));
	}
	
	public WebElement requisitionIdSearchBtn() throws Exception{
		return driver.findElement(By.xpath("html/body/div[3]/div[2]/div[1]/div[5]/div[3]/a[2]/span"));
	}
	
	public WebElement requisitionIdSearchInput() throws Exception{
		return driver.findElement(By.id("documentRequisitionId"));
	}
	
	public WebElement availableTestIDsList() throws Exception{
		return driver.findElement(By.id("availableTestIds"));
	}
	
	public WebElement addTestIdsToRightBtn() throws Exception{
		return driver.findElement(By.xpath("html/body/div[3]/div[2]/div[1]/div[7]/div[3]/div/button[1]"));
	}
	
	public WebElement addTestIdsToLeftBtn() throws Exception{
		return driver.findElement(By.xpath("html/body/div[3]/div[2]/div[1]/div[7]/div[3]/div/button[2]"));
	}
	
	public WebElement linkedTestIdsList() throws Exception{
		return driver.findElement(By.id("linkedTestIds"));
	}
	
	public void enterCategoryIdEditAccessionInput(String value) throws Exception{
		categoryIdEditAccessionInput().clear();
		categoryIdEditAccessionInput().sendKeys(value);
		categoryIdEditAccessionInput().sendKeys(Keys.TAB);
		logger.info("        Entered Category ID " + value + " in Edit Accession Document.");
	}
	
	public void enterEditDocumentCommentInput(String value) throws Exception{
		editDocumentCommentInput().sendKeys(value);
		logger.info("        Entered Comment " + value + " in Current Document table.");
	}
	
	// Elements of Delete Popup
	public WebElement deletePopupTitle() throws Exception{
		return driver.findElement(By.id("ui-dialog-title-2"));
	}
	
	public WebElement cancelDeleteBtn() throws Exception{
		return driver.findElement(By.id("btn_cancelDeleteDocument"));
	}
	
	public WebElement closeDelPopupIcon() throws Exception{
		return driver.findElement(By.xpath("html/body/div[4]/div[1]/a/span"));
	}
	
	//Elements of Upload Document Popup
	public WebElement chooseFileBtn() throws Exception{
		return driver.findElement(By.id("xifinUpload_maskButton"));
	}

	public WebElement countFileLbl() throws Exception{
		return driver.findElement(By.id("xifinUpload_countFile"));
	}
	
	public WebElement documentTypeUploadDocDropdownList() throws Exception{
		//return driver.findElement(By.xpath("html/body/div[2]/div[2]/div[2]/div[5]/div[5]/div[2]/select"));
		return driver.findElement(By.xpath(".//*[@id='ui-id-1']/div[2]/div[5]/div[5]/div[2]/select"));
	}

	public WebElement tempColumn() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='currentDocumentGird']/tbody//td[4]"));
	}
	
	public WebElement categoryIdUploadDocBtn() throws Exception{
		return driver.findElement(By.xpath("html/body/div[2]/div[2]/div[2]/div[5]/div[2]/div[2]/a[3]"));
	}
	
	public WebElement categoryIdUploadDocBtn2() throws Exception{
		return driver.findElement(By.xpath(".//div[contains(@class,'ui-dialog')]//div[contains(@class,'size10')]/a[contains(@class,'clientSearch')]"));
	}
	
	public WebElement requisitionIdUploadDocBtn() throws Exception{
		return driver.findElement(By.xpath("html/body/div[2]/div[2]/div[2]/div[5]/div[3]/div[2]/a[2]"));
	}
	
	public WebElement selectTestIDsUploadDocList() throws Exception{
		return driver.findElement(By.xpath("html/body/div[2]/div[2]/div[2]/div[5]/div[4]/div[1]/select"));
	}
	
	public WebElement addTestIdsToRightUploadDocBtn() throws Exception{
		return driver.findElement(By.xpath("html/body/div[2]/div[2]/div[2]/div[5]/div[4]/div[2]/div/button[1]"));
	}
	
	public WebElement addTestIdsToLeftUploadDocBtn() throws Exception{
		return driver.findElement(By.xpath("html/body/div[2]/div[2]/div[2]/div[5]/div[4]/div[2]/div/button[2]"));
	}
	
	public WebElement linkedTestIdsUploadDocList() throws Exception{
		return driver.findElement(By.xpath("html/body/div[2]/div[2]/div[2]/div[5]/div[4]/div[3]/select"));
	}
	
	public WebElement continueUploadBtn() throws Exception{
		return driver.findElement(By.xpath("//*[contains(@class, 'btn btn_submit floatLeft')]"));
	}

	public WebElement warningMessageContent(){
		return driver.findElement(By.xpath(".//*[@id='messagefor_message0']/div[1]/div"));
	}
	
	public WebElement warningMessageText(){
		return driver.findElement(By.xpath(".//*[@id='messagefor_message0']/div[2]"));
	}
	
	public WebElement closeWarningMessageIcon(){
		return driver.findElement(By.xpath(".//*[@id='messagefor_message0']/div[1]/a"));
	}
	
	public WebElement errorMessageContent(){
		return driver.findElement(By.xpath(".//div[contains(@class,'xf_message_container') and not (contains(@style,'display: none'))]//div[contains(@class,'xf_message_content')]"));
	}
	
	
	public WebElement tempUploadData(){
		return driver.findElement(By.cssSelector(".tempUploadData .filename_wrapper"));
	}
	
	public WebElement errorMessageCloseBtn(){
		return driver.findElement(By.xpath(".//div[contains(@class,'xf_message_container') and not (contains(@style,'display: none'))]//a[contains(@class,'xf_message_close')]"));
	}

	public void clickCategoryIdUploadDocBtn() throws Exception{
		categoryIdUploadDocBtn().click();
		logger.info("        Clicked category ID Doc button.");
	}
	public void clickCategoryIdUploadDocBtn2() throws Exception{
		categoryIdUploadDocBtn2().click();
		logger.info("        Clicked category ID Doc Button.");
	}
	public void closeErrorMessage() throws Exception {
		errorMessageCloseBtn().click();
		logger.info("        Clicked Close error message button.");
	}

	public WebElement btnCategoryIDSearchByClient() throws Exception{
		return driver.findElement(By.xpath("html/body/div[2]/div[2]/div[2]/div[5]/div[2]/div[2]/a[2]/span"));
	}

	public void clickToDocumentLinkOnGrid(String documentFileName) throws Exception{
		driver.findElement(By.xpath("//*[@id='currentDocumentGird']//a[@data-filename='"+documentFileName+"']")).click();
		logger.info("        Opened document file : "+documentFileName);
	}
	
	public void editDocumentLinkOnGrid(String documentFileName) throws Exception{
		Actions actions = new Actions(driver);
		WebElement trDateDocument = driver.findElement(By.xpath("//*[@id='currentDocumentGird']//td[@title='"+documentFileName+"']/following::td"));
		actions.moveToElement(trDateDocument).doubleClick().perform();
		logger.info("        Double clicked on document file: "+documentFileName);
	}

	public List<String> createAccnDocumentFileName(String seperator,SeleniumBaseTest b, String dbEnv) throws Exception{
		List<String> listDoc;
		String documentFileName, accnID, docTyp;
		DaoManagerPlatform daoManagerPlatform= new DaoManagerPlatform(b.getConfig().getRpmDatabase());
		DaoManagerAccnWS daoManagerAccnWS= new DaoManagerAccnWS(b.getConfig().getRpmDatabase());
		
		do{
			accnID = daoManagerAccnWS.getExistingAccnId(dbEnv);
			docTyp = daoManagerPlatform.getRandomDocStoreTypFromDOCSTORETYP(dbEnv).get(1);
			documentFileName = "Accn"+seperator+accnID+seperator+docTyp+".txt";
			listDoc = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(documentFileName,dbEnv);
		} while(listDoc.size()>0);

		List<String> listAccnFileName = new ArrayList<String>();
		listAccnFileName.add(accnID);
		listAccnFileName.add(docTyp);
		listAccnFileName.add(documentFileName);
		
		logger.info("        Created Accession Document file name: "+documentFileName);
		
		return listAccnFileName;
	}

	public void clickSaveEditBtn() throws Exception{
		saveEditBtn().click();
		Thread.sleep(3000);
		logger.info("        Clicked Save button in Edit Document Record popup window.");
	}
}
