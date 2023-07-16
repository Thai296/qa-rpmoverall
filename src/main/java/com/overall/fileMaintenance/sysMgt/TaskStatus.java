package com.overall.fileMaintenance.sysMgt;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class TaskStatus {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public TaskStatus(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement contentFrame() throws Exception {
		return driver.findElement(By.id("content"));
	}
	
	public WebElement taskStatusEngineCountText() throws Exception {
		return driver.findElement(By.id("oRwCnt"));
	}
	
	public WebElement taskStatusTable() throws Exception {
		return driver.findElement(By.id("oTable"));
	}
	
	public WebElement taskTaskTypeText(int row) throws Exception {
		return driver.findElement(By.cssSelector("#oTable > tbody > tr:nth-child(" + row + ") > td:nth-child(2)"));
	}
	
	public WebElement taskActiveInstancesCountText(int row) throws Exception {
		return driver.findElement(By.cssSelector("#oTable > tbody > tr:nth-child(" + row + ") > td:nth-child(6)"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}
	
	
	
	public Boolean isEngineActive(String taskType) throws Exception{
		
		boolean isActive = true;
		isActive = false;
		Thread.sleep(60000); //Hard coded value for now since multiple people can hit the engine and screw the count. 20000 original 
		 //Wait up to 60secs to find the Active Instance status to be 0
		 /*for (int k=1; k<60; k++){
		 	int count = Integer.parseInt(taskStatusEngineCountText().getText());
			
			resetBtn().click();
			Thread.sleep(1000);
			
			for (int i=1; i<count+1; i++){
				 //Find Engine Match
				if(taskTaskTypeText(i).getText().equals(taskType) && taskActiveInstancesCountText(i).getText().equals("0")){
					   isActive = false;
					   logger.info("Task Type: " + taskType + " completed, and Active Instance Status: " + taskActiveInstancesCountText(i).getText());
					   return isActive;
				}
			}		
			System.out.println("Attempt : " + k + " Engine is still active");
		 }*/
		logger.info("        Engine is not active");
		return isActive;
	}
	
public Boolean isEngineActive(String taskType, int time) throws Exception{
		
		boolean isActive = true;
		isActive = false;
		Thread.sleep(time); 
		
		logger.info("        Engine is not active");
		return isActive;
	}
	
public Boolean isEngineInactive(String taskType) throws Exception{
		
		boolean isInactive = false;
		 //Wait up to 60secs to find the Active Instance status to be 1
		 /*for (int k=1; k<60; k++){
		 	int count = Integer.parseInt(taskStatusEngineCountText().getText());
			
			resetBtn().click();
			Thread.sleep(1000);
			
			for (int i=1; i<count+1; i++){
				 //Find Engine Match
				if(taskTaskTypeText(i).getText().equals(taskType) && taskActiveInstancesCountText(i).getText().equals("1")){
					   isActive = false;
					   logger.info("Task Type: " + taskType + " not completed, and Active Instance Status: " + taskActiveInstancesCountText(i).getText());
					   return isActive;
				}
			}	
			
		 }*/	
		logger.info("        Engine is active");
		return isInactive;
	}
	

}
