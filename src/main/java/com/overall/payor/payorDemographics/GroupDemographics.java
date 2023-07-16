package com.overall.payor.payorDemographics;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class GroupDemographics {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private SeleniumBaseTest b;
	
	public GroupDemographics(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	//Payor Group Demographics
	public WebElement nameInput() throws Exception {
		return driver.findElement(By.id("GrpNm"));
	}
	
	public WebElement groupIdDropdown(String groupId) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#PyrGrpId')).val(\"" + groupId + "\")).trigger('onblur')[0]"); 
	}
	
	public WebElement groupIdDropDown() throws Exception {
		return driver.findElement(By.id("PyrGrpId"));
	}
	
	public WebElement contractedCheckBox() throws Exception {
		return driver.findElement(By.id("contracted"));
	}
	
	public boolean contractedCheckBoxText() throws Exception {
		return (boolean) ((JavascriptExecutor) driver).executeScript("return $('#contracted').attr(\"disabled\")"); 
	}
	
	public String woBasisText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#Basis').attr(\"value\")"); 
	}
	
	public WebElement minWOBalInput() throws Exception {
		return driver.findElement(By.id("MinBal"));
	}
	
	public WebElement woAgeInput() throws Exception {
		return driver.findElement(By.id("Age"));
	}
	
	public WebElement useOIGCheckBox() throws Exception {
		return driver.findElement(By.id("useOIG"));
	}
	
	public WebElement printADLCheckBox() throws Exception {
		return driver.findElement(By.id("printAnnualDisclLtr"));
	}
	
	public WebElement noteInput() throws Exception {
		return driver.findElement(By.id("note"));
	}

	//Dunning Cycle
	public WebElement addCycleBtn() throws Exception {
		//return driver.findElementByLinkText("btnAddRw1");
		return driver.findElement(By.id("btnAddRw1"));
	}
	
	public WebElement cycleCountText() throws Exception {
		return driver.findElement(By.id("oRwCnt"));
	}
	
	public WebElement prmySrvcId(int row, int col) throws Exception {
		return driver.findElement(By.xpath("//*[@id='oTable']/tbody/tr[" + row + "]/td[" + col + "]/select"));
	}
	
	public WebElement setCycleText(String cycleCount, int rowNum) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($(':input[name=\"cycle\"]')[" + rowNum + "]).val(\"" + cycleCount + "\")"); 
	}
	
	public WebElement setDaysText(String days, int rowNum) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($(':input[name=\"days\"]')[" + rowNum + "]).val(\"" + days + "\")"); 
	}
	
	public WebElement cycleIndexText(String index) throws Exception {
		return driver.findElement(By.cssSelector("#oTable > tbody > tr:nth-child(" + index + ") > td.tbbdy1a"));
	}
	
	public WebElement dunningCycleDeleteChkBox(String index) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('[name=\"delete\"]')[" + index + "]).click()"); 
	}
	
	//For Client Group only
	public WebElement mir90Input() throws Exception {
		return driver.findElement(By.id("mir90"));
	}
	
	//Procedure Code Override
	public WebElement addOverrideBtn() throws Exception {
		return driver.findElement(By.id("btnAddSubm"));
	}
	
	public WebElement codeOverrideCountText() throws Exception {
		return driver.findElement(By.id("sRwCnt"));
	}
	
	public WebElement submissionSrvcIdDropdown(int row, int col) throws Exception {
		return driver.findElement(By.xpath("//*[@id='sTable']/tbody/tr[" + row + "]/td[" + col + "]/select"));
	}
	
	public WebElement manualDemandFormatDropdown() throws Exception {
		return driver.findElement(By.id("manFrmt"));
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}
	
	//-------------------------------------------------------------
	public void clickSubmit() throws Exception{
		submitBtn().click();
		Thread.sleep(5000);
		logger.info("        Clicked Submit button.");
	}
	
	public void setGropuId(String groupId) throws Exception{
		//groupIdDropdown(groupId);
		b = new SeleniumBaseTest();
		b.selectItemByVal(groupIdDropDown(), groupId.trim());
		minWOBalInput().click();
		Thread.sleep(3000);
		//logger.info("        Selected GroupId: " + groupId);
	}	
	
	public int setProcCodeOverride() throws Exception{
		int count = 0;
		
		if(!codeOverrideCountText().getText().equals("")){
		
			count = Integer.parseInt(codeOverrideCountText().getText());
		}
		
		//Check if procedure code override got added
		addOverrideBtn().click();
		int updatedCount = Integer.parseInt(codeOverrideCountText().getText());
		
		if(updatedCount > count){
			logger.info("        Added Procedure Code Override");
			count = updatedCount;
		} else {
			logger.error("        Did Not Add Procedure Code Override");
			count = 0;
		}
		return count;
	}
	
	public int setCycle() throws Exception{

		int count = Integer.parseInt(cycleCountText().getText());
		
		//Check if there are cycle records
		if (cycleCountText().getText().equals("")){
			addCycleBtn().click();
			int updatedCount = Integer.parseInt(cycleCountText().getText());
			
			if(updatedCount > 0){
				logger.info("        Added Cycle");
				count = updatedCount;
			} else {
				logger.error("        Did Not Add Cycle");
				count = 0;
			}
		} 
		return count;
	}
	
	//-------------------------------------------------------------------------------------
	public void setMIR90(SeleniumBaseTest b, String str) throws Exception{
		b.clickHiddenPageObject(mir90Input(), 0);
		mir90Input().clear();
		mir90Input().sendKeys(str);
		mir90Input().sendKeys(Keys.TAB);
		logger.info("        Entered Monthly Interest Rate: " + str);
	}

}
