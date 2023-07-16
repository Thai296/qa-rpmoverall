package com.overall.support.google;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Google {
	private WebDriverWait wait;
	
	public Google(WebDriverWait wait) {
		this.wait = wait;
	}
	
	public WebElement signInButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='Sign in']")));
	}
	
	public WebElement gmailLinkText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='Gmail']")));
	}
	
}
