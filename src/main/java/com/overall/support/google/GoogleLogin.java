package com.overall.support.google;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GoogleLogin {
private WebDriverWait wait;
	
	public GoogleLogin(WebDriverWait wait) {
		this.wait = wait;
	}
	
	public WebElement googleLogo() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logo")));
	}
	
	public WebElement userNameTextBox() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("identifierId")));
	}
	
	public WebElement nextToPasswordButton() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='identifierNext']//button")));
	}
	
	public WebElement passWordTextBox() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
	}
	
	public WebElement nextButton() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("passwordNext")));
	}
	
}
