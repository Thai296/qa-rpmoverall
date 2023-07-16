package com.overall.client.lineitemdispute;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LineItemDisputeDetail {
	private WebDriverWait wait;
	private static final Logger logger = Logger.getLogger(LineItemDisputeDetail.class);

	public LineItemDisputeDetail(WebDriverWait wait) {
		this.wait = wait;
	}
	
	public WebElement statusDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("statusId")));		
	}
	
	public WebElement disputeDetailLineById(int disputeDetailId) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[@id='"+disputeDetailId+"']")));		
	}
	
	public WebElement lineItemOnDisputeGridValue(int seqId, String disputeTyp, String cellName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[@id='"+seqId+"']//td[@aria-describedby='tbl_lineItemOn"+disputeTyp.replace(" ", "")+"_"+cellName+"']")));		
	}
	
	public WebElement saveBtn() {
	    return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("btnSave")));
	}
	
	public WebElement commentTextbox(int seqId) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(seqId+"_comments")));		
	}
}
