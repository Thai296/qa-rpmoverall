package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class QuestionSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public QuestionSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	//title of the question search popup
	public WebElement questionSearchTitle() throws Exception {		
	    return driver.findElement(By.xpath(".//*[@id='gview_tbl_questionIdSearch']//span"));
	}
	
	public WebElement questionSearchTbl() throws Exception {		
	    return driver.findElement(By.id("tbl_questionIdSearch"));
	}
	
	public WebElement questionSearchTblQuestionIDColLink(String row) throws Exception {		
	    return driver.findElement(By.xpath(".//*[@id='tbl_questionIdSearch']//tr["+ row +"]/td[@aria-describedby='tbl_questionIdSearch_questionId']/a"));
	}
	
	public WebElement helpBtn() throws Exception {		
	    return driver.findElement(By.id("pageHelpLink"));
	}		
}
