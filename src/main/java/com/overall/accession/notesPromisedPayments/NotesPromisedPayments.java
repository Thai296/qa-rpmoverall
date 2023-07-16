package com.overall.accession.notesPromisedPayments;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotesPromisedPayments {
	private RemoteWebDriver driver;
	private WebDriverWait wait;
	protected Logger logger;

	public NotesPromisedPayments(RemoteWebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public WebElement loadAccessionSectionAccessionIdInput() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupAccnId")));
	}

	public WebElement notesPromisedPaymentsForm() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("accnNotesPromisedPaymentsForm")));
	}

	public WebElement addRecordButton() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_contactDetail")));
	}

	public WebElement editRecordButton() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_contactDetail")));
	}

	public WebElement manualActivityCodeDropdown() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_manualActivityCode")));
	}

	public WebElement contactInfoTextArea() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("contactInfo")));
	}

	public WebElement followUpDateStateBar() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("followUpDate")));
	}

	public WebElement followUpIndividualDropdown() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_followUpIndividual")));
	}

	public WebElement saveButtonInAddRecordAndEditRecordPopup() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
	}

	// Accession Contact detail
	public WebElement firstUserID() {
		return wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id='tbl_contactDetail']/tbody/tr[2]/td[3]")));
	}

	public WebElement firstContactDate() {
		return wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id='tbl_contactDetail']/tbody/tr[2]/td[4]")));
	}

	public WebElement firstManualActivityCode() {
		return wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id='tbl_contactDetail']/tbody/tr[2]/td[5]")));
	}

	public WebElement firstContactInfo() {
		return wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id='tbl_contactDetail']/tbody/tr[2]/td[6]")));
	}

	public WebElement firstFollowUpDate() {
		return wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id='tbl_contactDetail']/tbody/tr[2]/td[7]")));
	}

	public WebElement firstFollowUpIndividual() {
		return wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id='tbl_contactDetail']/tbody/tr[2]/td[9]")));
	}

	public WebElement firstFollowUpComplete() {
		return wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id='tbl_contactDetail']/tbody/tr[2]/td[10]")));
	}

	public WebElement firstPrintableNotes() {
		return wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id='tbl_contactDetail']/tbody/tr[2]/td[11]")));
	}

	public WebElement firstPrintOnStatement() {
		return wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id='tbl_contactDetail']/tbody/tr[2]/td[12]")));
	}

	public WebElement firstDeleteContactRecord() {
		return wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//*[@id='tbl_contactDetail']/tbody/tr[2]/td[13]")));
	}

	public WebElement saveAndClearButton() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}

	public WebElement editModTable() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_contactDetail")));
	}

	public WebElement contactDateText() throws Exception {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contactDate")));
	}

	public WebElement userIdText() throws Exception {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dataDisplay")));
	}

	// Elementto input value for contact detail
	public WebElement inputUserID() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gbox_tbl_contactDetail']//input[@id='gs_userId']")));
	}

	public WebElement inputContactDate() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gbox_tbl_contactDetail']//input[@id='gs_contactDate']")));
	}

	public WebElement inputManActivityCode() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gbox_tbl_contactDetail']//input[@id='gs_manualActivityCode']")));
	}

	public WebElement inputContactInfo() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gbox_tbl_contactDetail']//input[@id='gs_contactInfo']")));
	}

	public WebElement inputFollowupDate() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gbox_tbl_contactDetail']//input[@id='gs_followUpDate']")));
	}

	public WebElement inputFollowUpIndividual() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gbox_tbl_contactDetail']//input[@id='gs_followUpIndividual']")));
	}

	public WebElement inputPrintableNotes() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#gs_printableNotes")));
	}

	public WebElement contactDetailTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_contactDetail")));
	}
	
	public WebElement contactDetailCol(String row, String col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@id='tbl_contactDetail']//tr[" + row + "]/td[" + col + "]")));
	}	
}
