package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.RandomCharacter;
import com.overall.utils.TestCodeUtils;

public class NpiGlobalSearchResults {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private DaoManagerXifinRpm daoManagerXifinRpm;
	private SeleniumBaseTest b;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;

	public NpiGlobalSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*NPI Search Results*/	
	public WebElement npiSearchResultTitle() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='gview_npisearchTable']/div[1]/span"));
	} 
	
	public WebElement npiTable() throws Exception{
		return driver.findElement(By.id("npisearchTable"));
	}
	
	public WebElement npiTitle() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='gview_npisearchTable']/div[1]/span"));
	}
	
	public WebElement npiSearchResultsTblNPINumber(int row, int col) throws Exception{
		return driver.findElement(By.xpath("//*[@id='npisearchTable']/tbody/tr["+ row +"]/td["+ col +"]/a"));
	}
	
	public WebElement npiSearchResultsTblReloadGrid() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='refresh_npisearchTable']/div/span"));
	}
	
	public WebElement npiSearchResultsTblFirstIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='first_pager']/span"));
	}
	
	public WebElement npiSearchResultsTblPreviousIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='prev_pager']/span"));
	}
	
	public WebElement npiSearchResultsTblNumberOfPageInput() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[4]/input"));
	}
	
	public WebElement npiSearchResultsTblNumberOfEntirePageLabel() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='sp_1_pager']"));
	}
	
	public WebElement npiSearchResultsTblNextIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='next_pager']/span"));
	}
	
	public WebElement npiSearchResultsTblLastestIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='last_pager']/span"));
	}
	
	public WebElement npiSearchResultsTblShowNumberOfResultDropdown() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[8]/select"));
	}
	
	public WebElement  npiSearchResultsTblViewResultsLable() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='pager_right']/div"));
	}
	
	public WebElement npiSearchResultsKeepSearchOpenCheckbox() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='keepSearchOpen']"));
	}
	
	public WebElement npiSearchResultsHelpLink() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='pageHelpLink']"));
	}
	
	public WebElement npiSearchResultsCloseButton() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='npiSearch']/div[3]/button[2]"));
	}
	
	public WebElement npiSearchResultsNewSearchButton() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='npiSearch']/div[3]/button[1]"));
	}
	
	
	
	
	public void clickNPISearchResultsTblNPINumber(int row, int col) throws Exception {	
		String NIP =npiSearchResultsTblNPINumber(row,col).getText();
		npiSearchResultsTblNPINumber(row,col).click();		
		logger.info("        Click link " + NIP + " in row " + row);
	}
	
	public void clickNPISearchResultsTblReloadGrid() throws Exception{
		npiSearchResultsTblReloadGrid().click();
		logger.info("        Click on Reload Icon on NPI search results table");
	}
	
	public void clickNPISearchResultsTblFirstIcon() throws Exception{
		npiSearchResultsTblFirstIcon().click();
		logger.info("        Click on First Icon on NPI search results table");		
	}
	
	public void clickNPISearchResultsTblPreviuosicon() throws Exception{
		npiSearchResultsTblPreviousIcon().click();
		logger.info("        Click on Previous Icon on NPI search results table");
	}
	
	public void inputNPISearchResultsTblNumberOfPage(String value) throws Exception{
		npiSearchResultsTblNumberOfPageInput().sendKeys(value);
		logger.info("        Input "+value+"to Number Of Page on NPI search results table");
	}
	
	public void clickNPISearchResultsTblNextIcon() throws Exception{
		npiSearchResultsTblNextIcon().click();
		logger.info("        Click on Next Icon on NPI search results table");
	}
	
	public void  clickNPISearchResultsTblLastestIcon() throws Exception{
		npiSearchResultsTblLastestIcon().click();
		logger.info("        Click on Lastest Icon on NPI search results table");
	}
	
	public void selectNPISearchResultsTblShowNumberOfResultsDropdown(String value) throws Exception{
		Select dropdown = new Select(npiSearchResultsTblShowNumberOfResultDropdown());
		dropdown.selectByVisibleText(value);
		logger.info("        Select "+value+" in The Number of Results dropdownn on NPI search results table");
	}
	
	public void clickNPISearchResultsKeepSearchOpenCheckbox() throws Exception{
		npiSearchResultsKeepSearchOpenCheckbox().click();
		logger.info("        Click on Keep Search Open checkbox on NPI search results");
	}
	
	public void clickNPISearchResultsHelplink() throws Exception{
		npiSearchResultsHelpLink().click();
		logger.info("        Click on Help link on NPI search results");
	}
	
	public void clickNPISearchResultsCloseButton() throws Exception{
		npiSearchResultsCloseButton().click();
		logger.info("        Click on Close button on NPI search results");
	}
	
	public void clickNPISearchResultsNewSearchButton() throws Exception{
		npiSearchResultsNewSearchButton().click();
		logger.info("        Click on New Search button on NPI search results");
	}
	
	public WebElement getLinkValueFromWebTable(String tableId, int row, int col) {
		return driver.findElement(By.xpath("//*[@id='" + tableId + "']/tbody/tr[" + row + "]/td[" + col + "]/a"));		
	}
	
	public WebElement getValueFromWebTable(String tableId, int row, int col) {
		return driver.findElement(By.xpath("//*[@id='" + tableId + "']/tbody/tr[" + row + "]/td[" + col + "]"));
	}
	
	
}
