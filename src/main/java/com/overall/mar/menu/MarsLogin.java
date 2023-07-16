package com.overall.mar.menu;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MarsLogin {
	private WebDriverWait wait;
	protected Logger logger;
	
	public MarsLogin(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement usernameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_username")));
	}
	
	public WebElement passwordInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_password")));
	}
	
	public WebElement logonBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Submit")));
	}
	
	public void login(String username, String password) throws Exception {
		usernameInput().sendKeys(username);
		passwordInput().sendKeys(password);		
		logonBtn().click();
		logger.info("Logging into Mars . Username: " + username);
	}
}
