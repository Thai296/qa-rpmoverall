package com.overall.payor.payorDemographics;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PricingConfig {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;
	
	public PricingConfig(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		this.wait=wait;
	}
	
	//Pricing config
	public WebElement payorIdInput() throws Exception {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pyrAbbrv")));
	}
	
	public WebElement payorIdInput(String payorId) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#pyrAbbrv')).val(\"" + payorId + "\")).trigger('onblur')[0]"); 
	}
	
	public WebElement insNameInput() throws Exception {
		return driver.findElement(By.name("pyrNm"));
	}
	
	public WebElement grpNameInput() throws Exception {
		return driver.findElement(By.name("pyrGrpNm"));
	}
	
	public WebElement reasonCodeInput() throws Exception {
		return driver.findElement(By.id("rsnCd"));
	}
	
	public WebElement refPhysIdDropdown() throws Exception {
		return driver.findElement(By.id("provRqmnts"));
	}
	
	public WebElement woBasisDropdown() throws Exception {
		return driver.findElement(By.id("pyrAutoBasis"));
	}
	
	public WebElement minWoBalInput() throws Exception {
		return driver.findElement(By.id("pyrMinWoBal"));
	}
	
	public WebElement woAgeInput() throws Exception {
		return driver.findElement(By.name("pyrWoAge"));
	}
	
	public WebElement flTmLmtInput() throws Exception {
		return driver.findElement(By.name("flTmLmt"));
	}
	
	public WebElement reFlTmLmtInput() throws Exception {
		return driver.findElement(By.name("reFlTmLmt"));
	}
	
	public WebElement applTmLmtInput() throws Exception {
		return driver.findElement(By.name("applTmLmt"));
	}
	
	public WebElement diagMethodDropdown() throws Exception {
		return driver.findElement(By.name("diagMethod"));
	}
	
	public WebElement diagReqCheckbox() throws Exception {
		return driver.findElement(By.name("diagReq"));
	}
	
	public WebElement printTestNameOnUnlistedProcsCheckbox() throws Exception {
		return driver.findElement(By.name("printTestNameOnUnlistedProcs"));
	}
	
	//Submission Service Procedure Code Override
	public WebElement subSrvProcCodeOverrideCountText() throws Exception {
		return driver.findElement(By.id("sRwCnt"));
	}
	
	public WebElement submissionServiceDropdown(int row) throws Exception{
		return driver.findElement(By.xpath("//*[@id='sTable']/tbody/tr[" + row + "]/td[3]/select"));
	}
	
	public WebElement addOverrideBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('input[name = btnAddSubm]')).trigger('addRow1')[0].click()"); 
	}
	
	//Dunning Cycle
	public WebElement addCycleBtn() throws Exception {
		return driver.findElementByLinkText("btnAddCycl");
	}
	
	public WebElement cycleCountText() throws Exception {
		return driver.findElement(By.id("dRwCnt"));
	}
	
	public WebElement prmySrvcIdDropdown(int row, int col) throws Exception {
		return driver.findElement(By.xpath("//*[@id='dTable']/tbody/tr[" + row + "]/td[" + col  + "]/select"));
	}
	
	//Submission Service Provider
	public WebElement addProviderBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('input[name = btnAddProv]')).trigger('addRow1')[0].click()"); 
	}
	
	public WebElement providerCountText() throws Exception {
		return driver.findElement(By.id("pRwCnt"));
	}
	
	public WebElement submSrvcProviderDropdown(int row) throws Exception {
		return driver.findElement(By.xpath("//*[@id='pTable']/tbody/tr[" + row + "]/td[3]/select"));
	}
	
	//Manual Demand Submission Service
	public WebElement manualSubmSrvcDropdown() throws Exception {
		return driver.findElement(By.xpath("//*[@id='cTransPage']/div[6]/table[1]/tbody/tr/td[2]/select"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}
	
	public WebElement auditBtn() throws Exception {
		return driver.findElement(By.id("auditbtn"));
	}
	
	public void resetPayor() throws Exception{
		resetBtn().click();
		logger.info("Reset Payor");
	}
	
	public void runAudit() throws Exception{
		auditBtn().click();
		logger.info("Run Audit");
	}
	
	public void setPayorId(String payorId) throws Exception{
		payorIdInput(payorId);
		logger.info("Entered PayorId: " + payorId);
	}
	
	public int setSubSrvProcCodeOverride() throws Exception{
		int count = 0;
		
		if(!subSrvProcCodeOverrideCountText().getText().equals("")){
			count = Integer.parseInt(subSrvProcCodeOverrideCountText().getText());
		}
		
		//Check if procedure code override got added
		addOverrideBtn();
		int updatedCount = Integer.parseInt(subSrvProcCodeOverrideCountText().getText());
		
		if(updatedCount > count){
			logger.info("Added Procedure Code Override");
			count = updatedCount;
		} else {
			logger.error("Did Not Add Procedure Code Override");
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
				logger.info("Added Cycle");
				count = updatedCount;
			} else {
				logger.error("Did Not Add Cycle");
				count = 0;
			}
		} 
		return count;
	}
	
	public int setProvider() throws Exception{

		int count = Integer.parseInt(providerCountText().getText());
		
		//Check if there are provider records
		addProviderBtn();
		int updatedCount = Integer.parseInt(providerCountText().getText());
		
		if(updatedCount > 0){
			logger.info("Added Provider");
			count = updatedCount;
		} else {
			logger.error("Did Not Add Provider");
			count = 0;
		}
		
		return count;
	}
	
	public boolean reqCheck(String val) throws Exception{
		boolean flag = false;
		if (!val.equals("")) {
			if(val.equals("1")){
				logger.info("Checkbox required checked");
				return true;
			} 
		} else {
			logger.error("Must enter valid value, " + val + " is not valid");
		}
		return flag;
	}
}
