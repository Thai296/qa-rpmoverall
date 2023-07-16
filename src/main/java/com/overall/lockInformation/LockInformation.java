package com.overall.lockInformation;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LockInformation {
	private WebDriverWait wait;
	protected Logger logger;

	public LockInformation(WebDriverWait wait) {
		this.wait = wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + wait);
	}

	public List<WebElement> lockInformationTblItemChk(String itemName) {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//tr[descendant::input[@value='"+ itemName +"']]//input[@type='checkbox']")));
	}
		
	public WebElement submitBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn_submit")));
	}
}
