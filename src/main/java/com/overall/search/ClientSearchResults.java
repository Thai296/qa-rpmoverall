package com.overall.search;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.xifin.utils.SeleniumBaseTest;

public class ClientSearchResults {
	private WebDriverWait wait;
	protected Logger logger;

	public ClientSearchResults(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*Client Search Results*/
	public WebElement clientSearchResultTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_clientsearchTable']/div[1]/span")));
	}
	
	public WebElement clientSearchResultsTblClientID(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td["+ col +"]/a")));
	}
	
	public WebElement clientSearchResultsCell(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td["+ col +"]")));
	}
	
	public WebElement clientSearchResultsTblClientIDColLink(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_clientAbbrev']/a")));
	}
	
	public WebElement clientSearchResultsTblClientNameColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_clientName']")));
	}
	
	public WebElement clientSearchResultsTblFullAddressColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_fullAddress']")));
	}
	
	public WebElement clientSearchResultsTblReloadGridIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='refresh_clientsearchTable']/div/span")));
	}
	
	public WebElement clientSearchResultTblFirstIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_pager']/span")));
	}
	
	public WebElement clientSearchResultTblPreviousIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_pager']/span")));
	}
	
	public WebElement clientSearchResultTblNumberOfPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[4]/input")));
	}
	
	public WebElement clientSearchResultTblNumberOfPageLabel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sp_1_pager']")));
	}
	
	public WebElement clientSearchResultTblNextIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}
	
	public WebElement clientSearchResultTblLastestIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_pager']/span")));
	}
	
	public WebElement clientSearchResultTblShowNumberOfResultDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[8]/select")));
	}
	
	public WebElement clientSearchResultTblViewResultsLable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	
	public WebElement clientSearchResultKeepSearchOpenCheckbox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='keepSearchOpen']")));
	}
	
	public WebElement clientSearchResultCloseButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientSearch']/div[3]/button[2]")));
	}
	
	public WebElement clientSearchResultNewSearchButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='clientSearch']/div[3]/button[1]")));
	}
	
	public WebElement clientSearchResultHelpLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pageHelpLink']")));
	}
			
	public void clickClientSearchTblClientID(int row, int col) throws Exception { 
		String ClientID = clientSearchResultsTblClientID(row,col).getText();
		clientSearchResultsTblClientID(row,col).click();	
		Thread.sleep(3000);
		logger.info("        Clicked link " + ClientID + " at row " + row);		
	}
	
	public void clickClientSearchResultsTblReloadGridIcon() {
		clientSearchResultsTblReloadGridIcon().click();
		logger.info("        Click on Reload Grid icon on Client Search Results table");	
	}
	
	public void clickClientSearchResultTblFirstIcon(SeleniumBaseTest b) throws Exception {
		b.clickHiddenPageObject(clientSearchResultTblFirstIcon(), 0);
		Thread.sleep(3000);
		logger.info("        Click on First icon on Client Search Results table");
	}
	
	public void clickClientSearchResultTblPreviousIcon(SeleniumBaseTest b) throws Exception {
		b.clickHiddenPageObject(clientSearchResultTblPreviousIcon(), 0);
		Thread.sleep(3000);
		logger.info("        Click on Previous icon on Client Search Results table");
	}
	
	public void inputClientSearchResultTblNumberOfPage(String value) {
		clientSearchResultTblNumberOfPageInput().sendKeys(value);
		logger.info("        Input into The Number of per page of Client Search Results table");
	}
	
	public void clickClientSearchResultTblNextIcon(SeleniumBaseTest b) throws Exception {
		b.clickHiddenPageObject(clientSearchResultTblNextIcon(), 0);
		Thread.sleep(3000);
		logger.info("        Click on Next icon on Client Search Results table");
	}
	
	public void clickClientSearchResultTblLastestIcon(SeleniumBaseTest b) throws Exception {
		b.clickHiddenPageObject(clientSearchResultTblLastestIcon(), 0);
		Thread.sleep(5000);
		logger.info("        Click on Lastest icon on Client Search Results table");
	}	

	public void selectClientSearchResultTblShowNumberOfResultDropdown(String value) {
		Select dropdown = new Select(clientSearchResultTblShowNumberOfResultDropdown());
		dropdown.selectByVisibleText(value);
		logger.info("        Select "+value+" in Number of Result dropdown list");	
	}
	
	public void clickClientSearchResultKeepSearchOpenCheckbox() throws Exception {
		clientSearchResultKeepSearchOpenCheckbox().click();
		Thread.sleep(5000);
		logger.info("        Click on Keep Search Open checkbox on Client Search Results page");	
	}
	
	public void clickClientSearchResultCloseButton() throws Exception {
		clientSearchResultCloseButton().click();
		logger.info("        Click on Close button on Client Search Results page");	
	}
	
	public void clickClientSearchResultNewSearchButton() throws Exception {
		clientSearchResultNewSearchButton().click();
		Thread.sleep(5000);
		logger.info("        Click on New Search button on Client Search Results page");	
	}
	
	public void clickClientSearchResultHelpLink() {
		clientSearchResultHelpLink().click();
		logger.info("        Click on help link on Client Search Results page");		
	}
	
	//Table View - Client payor search results (Clients using the fee schedule)
	public WebElement viewClientPayorSearchResultTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageTitleContent")));
	}
	
	public WebElement viewPriceTblIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnPyrTblID")));
	}
	
	public WebElement viewNameTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnPyrTblDescr")));
	}
	
	public WebElement viewHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement viewTblNextIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_clnPyrPrcs_pagernav")));
	}
	
	public WebElement viewTotalRecordsInTblTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clnPyrPrcs_pagernav_right']/div")));
	}
	
	public WebElement viewCloseBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("Close")));
	}
	
	public WebElement address1InClientSearchResults(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_address1']")));
	}

	public WebElement address2InClientSearchResults(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_address2']")));
	}
	
	public WebElement clientSearchResultTblCityTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_city']")));
	}
	
	public WebElement clientSearchResultTblStateTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_state']")));
	}
	
	public WebElement clientSearchResultTblPostalCodeTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_zip']")));
	}
	
	public WebElement clientSearchResultTblCountryTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_countryId']")));
	}
	
	public WebElement clientSearchResultTblAccountTypeTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_accountType']")));
	}	
	
	public WebElement clientSearchResultTblPrimaryFacilityTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_clientPrimaryFacility']")));
	}
	
	public WebElement clientSearchResultTblBalanceDueTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_dueAmt']")));
	}
	
	public List<WebElement> clientSearchResultTblAllDataRows() {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='clientsearchTable']//tr[@tabindex='-1']")));
	}
	
	public WebElement clientSearchResultTblTotalPagesTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='pager_center']//span[contains(@id,'sp') and contains(@id,'pager')]")));
	}
	
	/**
	 * Get value from Client Search Table follow by row number and name of column
	 * @param row
	 * @param columnName
	 * @return
	 */
	public WebElement clientSearchResultsCellByRowAndColumnName(String row, String columnName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientsearchTable']/tbody/tr["+ row +"]/td[@aria-describedby='clientsearchTable_"+ columnName +"']")));
	}
}