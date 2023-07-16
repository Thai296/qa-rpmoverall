package com.overall.search;

import com.overall.utils.TestCodeUtils;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.rpm.PrcDao;
import com.xifin.qa.dao.rpm.PrcDaoImpl;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

public class IncrementalPricingSearchResults 
{
	private static final Logger LOG = Logger.getLogger(IncrementalPricingSearchResults.class);
	
	private final RemoteWebDriver driver;
	private final Configuration config;
	private final PrcDao prcDao;

	public IncrementalPricingSearchResults(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.config = config;
		this.prcDao=new PrcDaoImpl(config.getRpmDatabase());
	}
	
	public WebElement inPriSearchResultTitle() {
		return driver.findElement(By.xpath(".//*[@id='gview_incrPricingSearchTable']/div[1]/span"));
	}
	
	public WebElement inPriSearchResultHelpIconBtn() {
		return driver.findElement(By.xpath(".//*[@id='pageHelpLink']"));
	}
	
	public WebElement inPriSearchResultCloseBtn() {
		return driver.findElement(By.xpath(".//*[@id='incrPricingSearch']/div[3]/button[2]"));
	}
	
	public WebElement inPriSearchResultNewSearchBtn() {
		return driver.findElement(By.xpath(".//*[@id='incrPricingSearch']/div[3]/button[1]"));
	}
	
	public WebElement inPriSearchResultKeepSearchOpenCheckbox() {
		return driver.findElement(By.xpath(".//*[@id='keepSearchOpen']"));
	}
	
	public WebElement inPriSearchResultTable() {
		return driver.findElement(By.xpath(".//*[@id='incrPricingSearchTable']"));
	}
	
	public WebElement inPriSearchResultPriTableColumn(int rol, int col) {
		return driver.findElement(By.xpath(".//*[@id='incrPricingSearchTable']/tbody/tr["+rol+"]/td["+col+"]"));
	}
	
	public WebElement inPriSearchResultPriIDColumn(int rol, int col) {
		return driver.findElement(By.xpath(".//*[@id='incrPricingSearchTable']/tbody/tr["+rol+"]/td["+col+"]/a"));
	}		
	
	public WebElement inPriSearchResultReloadGrid() {
		return driver.findElement(By.xpath(".//*[@id='refresh_incrPricingSearchTable']/div/span"));
	}
	
	public WebElement inPriSearchResultFirstIcon() {
		return driver.findElement(By.xpath(".//*[@id='first_pager']/span"));
	}
	
	public WebElement inPriSearchResultPreviousIcon() {
		return driver.findElement(By.xpath(".//*[@id='prev_pager']/span"));
	}
	
	public WebElement inPriSearchResultNextIcon() {
		return driver.findElement(By.xpath(".//*[@id='next_pager']/span"));
	}
	
	public WebElement inPriSearchResultLastIcon() {
		return driver.findElement(By.xpath(".//*[@id='last_pager']/span"));
	}
	
	public WebElement inPriSearchResultPageInput() {
		return driver.findElement(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[4]/input"));
	}
	
	public WebElement inPriSearchResultTotalPage() {
		return driver.findElement(By.xpath(".//*[@id='sp_1_pager']"));
	}
	
	public WebElement inPriSearchResultTotalNumberOfRowDropdown() {
		return driver.findElement(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[8]/select"));
	}
	
	public WebElement inPriSearchResultTotalResult() {
		return driver.findElement(By.xpath(".//*[@id='pager_right']/div"));
	}
	
	public void clickInPriSearchHelpIconBtn() throws Exception{
		inPriSearchResultHelpIconBtn().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Search Result Help Icon button");
	}

	public void clickInPriSearchCloseBtn() throws Exception{
		inPriSearchResultCloseBtn().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Search Result Close button");
	}
	
	public void clickInPriSearchNewSearchBtn() throws Exception{
		inPriSearchResultNewSearchBtn().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Search Result New Search button");
	}
	
	public void clickInPriSearchKeepSearchOpenChechbox() throws Exception{
		inPriSearchResultKeepSearchOpenCheckbox().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Search Result Keep Search Open checkbox");
	}
	
	public void inputInPriSearchPageInput(String value) {
		inPriSearchResultPageInput().sendKeys(value);
		LOG.info("        Entered "+value+" to Incremental Pricing Search Result Page Input field");
	}
	
	public void clickInPriSearchFirstIcon() throws Exception{
		inPriSearchResultFirstIcon().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Search Result First Icon");
	}
	
	public void clickInPriSearchPreviousIcon() throws Exception{
		inPriSearchResultPreviousIcon().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Search Result Previous Icon");
	}
	
	public void clickInPriSearchNextIcon() throws Exception{
		inPriSearchResultNextIcon().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Search Result Next Icon");
	}
	
	public void clickInPriSearchLastIcon() throws Exception{
		inPriSearchResultLastIcon().click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Search Result Last Icon");
	}
	
	public void clickInPriSearchPriIDColumn(int rol, int col) throws Exception{
		inPriSearchResultPriIDColumn(rol, col).click();
		Thread.sleep(2000);
		LOG.info("        Clicked on Incremental Pricing Search Result Pricing ID");
	}
	
	public void selectInPriSearchTotalNumberOfRowDropdown(String value) {
		Select dropdown = new Select(inPriSearchResultTotalNumberOfRowDropdown());
		dropdown.selectByVisibleText(value);
		LOG.info("        Selected "+value+" in Number of row dropdown list");	
	}
	
	public int getTotalPricingID() throws Exception{
		int data = prcDao.getCountFromPrcHavingIncrementalPrcId();
		LOG.info(data);
		return data;
	}
	
	public int numberOfPricingInResult() {
		TestCodeUtils testCodeUtils = new TestCodeUtils(driver);
		WebElement result = driver.findElement(By.xpath(".//*[@id='pager_right']/div"));
		String [] text = result.getText().split("of");		
		return text.length <= 1 ? 0 : Integer.parseInt(testCodeUtils.convertDecimalFormat(Integer.parseInt(text[1].trim())));		
	}
	
	public int numOfRecordsInResult() {
		WebElement result = driver.findElement(By.xpath(".//*[@id='pager_right']/div"));
		String [] text = result.getText().split("of");
		System.out.println(text[1]);
		return Integer.parseInt(text[1].replaceAll("[,]", "").trim());
	}	

}
