package com.overall.client.lineitemdispute;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LineItemDispute {
	private WebDriverWait wait;
	private RemoteWebDriver driver;
	public LineItemDispute(RemoteWebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}
	
	public WebElement notiSuccessMessage() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sendNotificationSuccess")));
	}
	
	public WebElement removeSelectedClientDefaultButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='s2id_selectionIdClients']//li[@class='select2-search-choice']/a")));
	}
	
	public WebElement clientsInputDropdownList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_autogen1")));
	}
	
	public WebElement selectedClientInDropdownList(String value) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='s2id_selectionIdClients']/ul/li/div[text()='" + value + "']")));
	}
	
	public WebElement lineItemDisputeFirstRow(String description) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr[2]//td[@aria-describedby='" + description + "']")));
	}
	
	public List<WebElement> lineItemStatementDate() {
		return driver.findElements(By.xpath("//tbody/tr//td[@aria-describedby='tbl_lineItemDispute_statementDt']"));
	}
	
	public WebElement lineItemDisputeClientIdCheckbox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("clnAbbrev")));
	}
	
	public WebElement lineItemDisputeTextboxByName(String textboxName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name(textboxName)));		
	}
	
	public WebElement viewDetailButtonByDisputeSeqId(int disputeSeqId) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[@id='"+disputeSeqId+"']//td[@title='View Details']")));		
	}
	
	public WebElement disputeNotificationsGridTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Dispute Notifications']")));		
	}
	
	public WebElement disputeNotificationsClientDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[text()='Clients']/parent::div/following-sibling::div//ul[@class='select2-choices']")));		
	}
	
	public WebElement clientDropdownInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[@class='select2-search-field']/input")));		
	}
	
	public WebElement clientDropdownSelectedOption(String optionName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@class='select2-choices']//div[text()='"+optionName+"']")));		
	}
	
	public WebElement clientDropdownCloseButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("select2-search-choice-close")));		
	}
	
	public WebElement onlySendForUpdatedDisputesCheckbox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sendDisputeUpdateStatusNotificationCheckbox")));		
	}
	
	public WebElement sendNotificationsButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSendNotification")));		
	}
	
	public WebElement sendNotificationErrorLabel() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sendNotificationError")));		
	}
	
	public List<WebElement> lineItemDisputeAllRows() {
		return driver.findElements(By.xpath("//table[@id='tbl_lineItemDispute']//tr[not(@class='jqgfirstrow')]"));
	}
	
	public List<WebElement> lineItemDisputeSpecificCol(String description) {
		return driver.findElements(By.xpath("//table[@id='tbl_lineItemDispute']//tr[not(@class='jqgfirstrow')]/td[@aria-describedby='" + description + "']"));
	}
}
