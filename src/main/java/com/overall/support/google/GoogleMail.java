package com.overall.support.google;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GoogleMail {
	private WebDriverWait wait;
	private RemoteWebDriver driver;
	
	public GoogleMail(RemoteWebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}
	
	public WebElement searchMailInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='q']")));
	}
	
	public WebElement selectCheckBoxAll() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='search refinement']/following::div//div[@aria-label='Select']//span")));
	}
	
	public WebElement deleteMailButton() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-tooltip='Delete']")));
	}
	
	public List<WebElement> emailSearchList() {
		return driver.findElements(By.xpath("//div[@role='main']//tbody/tr/td[@data-tooltip='Select']"));
	}
	
	public WebElement firstEmail() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='main']//table[@role='grid']//tbody/tr[1]")));
	}
	
	public WebElement titleEmail() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//h2")));
	}
	
	public WebElement mailSend(String mail) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3//span[@email='" + mail + "']")));
	}
	
	//Detail informations of sender
	public WebElement showDetailButton() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='Show details']")));
	}
	
	public WebElement receiverMail() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='to:']/parent::td/following-sibling::td/span/span")));
	}
	
	public WebElement expandContentButton() {
		return driver.findElement(By.xpath("//div[@aria-label='Show trimmed content']/img/parent::div"));
	}
	
	public WebElement logoContent(String content) {
		return driver.findElement(By.xpath("//p[contains(., '" + content + "')]/preceding-sibling::p/img"));
	}
	
	public WebElement contentLineText(String content) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(.,  '" + content + "')]")));
	}
	
	public WebElement disputeDetailApprovedText(String invoiceId) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(., '#" + invoiceId + "')]/following::p[1]")));
	}
	
	public WebElement disputeDetailRejectedText(String invoiceId) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(., '#" + invoiceId + "')]/following::p[2]")));
	}
	
	public WebElement disputeDetailInProgressText(String invoiceId) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(., '#" + invoiceId + "')]/following::p[3]")));
	}
	
	public WebElement disputeDetailInDisputeText(String invoiceId) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(., '#" + invoiceId + "')]/following::p[4]")));
	}
	
	public WebElement footerMailLineText(String text) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul/li[contains(., '" + text + "')]")));
	}
}
