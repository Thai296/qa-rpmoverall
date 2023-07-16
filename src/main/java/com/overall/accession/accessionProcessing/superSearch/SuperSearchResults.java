package com.overall.accession.accessionProcessing.superSearch;

import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuperSearchResults {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public SuperSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement clientIdText() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('#oTable > tbody > tr:nth-child(2) > td:nth-child(2)')).get(0)"); 
	}

	public WebElement accnIdText() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='oTable']/tbody/tr[2]/td[5]/input"));
	}
	public WebElement actionDropdown() throws Exception {
		return driver.findElement(By.id("actionId"));
	}
	public WebElement actionDropdownPF() {
		return driver.findElement(By.id("s2id_actions"));
	}

	public WebElement claimTypeDropDown()
	{
		return driver.findElement(By.id("s2id_resubmitClaimAction"));
	}
	public WebElement adjCodeDropdownPF() {
		return driver.findElement(By.id("s2id_adjustmentCode"));
	}

	public WebElement selectAllOnPageCheckbox() throws Exception {
		return driver.findElement(By.id("allPage"));
	}
	
	public WebElement nextSearchBtn() throws Exception {
		return driver.findElement(By.id("next_btn"));
	}
	public WebElement newSearchBtn() throws Exception {
		return driver.findElement(By.id("newSrch_btn"));
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("Submit"));
	}
	public WebElement submitBtnPF() {
		return driver.findElement(By.id("btnSubmitAction"));
	}
	public WebElement selectCheckBox() throws Exception {
		return driver.findElement(By.id("selectChk0"));
	}
	public WebElement selectCheckBoxPF(int rowNum) {
		return driver.findElement(By.xpath(".//*[@id='"+rowNum+"']/td[20]/input"));
	}
	public void selectActionByText(String text) throws Exception{
		Select select = new Select(actionDropdown());
		select.selectByVisibleText(text);

		logger.debug("Select Action from drop down list");
	}
	public void selectActionByTextPF(String text) throws Exception{
		Select select = new Select(actionDropdown());
		select.selectByVisibleText(text);

		logger.debug("Select Action from drop down list");
	}
	public WebElement select2DropInput()
	{
		return driver.findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
	}
	
	public void forceAllAccnReprice(SeleniumBaseTest b, int limit, String value) throws Exception{
		
		for (int i=1; i < limit + 1; i++){
			b.selectItem(actionDropdown(), value);
			selectAllOnPageCheckbox().click();

			if(i == limit){
				submitBtn().click();
				logger.info(10 * limit + " accn have been repriced");
				break;
			}
			nextSearchBtn().click();
			for(int j=0; j<5; j++){
				if(b.isDropdownItemSelected(actionDropdown(), "")){
					break;
				}
			}
		}
		
	}
	public void setActionDropDownPF(String action, WebDriverWait wait)
	{
		WebElement actionDropdown = actionDropdownPF();
		wait.until(ExpectedConditions.elementToBeClickable(actionDropdown));
		actionDropdown.click();
		select2DropInput().sendKeys(action+ Keys.ENTER);
		logger.info("Entered Input Into Action DropDown: " + action);
	}

	public void setADjCodeDropDownPF(SeleniumBaseTest b,String adjCode, WebDriverWait wait) throws InterruptedException {
		WebElement actionDropdown = adjCodeDropdownPF();
		wait.until(ExpectedConditions.elementToBeClickable(actionDropdown));
		b.selectDropDownByText(actionDropdown, adjCode);
		logger.info("Entered Input Into Action DropDown: " + adjCode);
	}

	public void setClaimTypeDropDown(String claimType, WebDriverWait wait)
	{
		WebElement claimTypeDropDown = claimTypeDropDown();
		wait.until(ExpectedConditions.elementToBeClickable(claimTypeDropDown));
		claimTypeDropDown.click();
		select2DropInput().sendKeys(claimType+ Keys.ENTER);
		logger.info("Entered claim type into Claim Type drop down, claimType=" + claimType);
	}
}
