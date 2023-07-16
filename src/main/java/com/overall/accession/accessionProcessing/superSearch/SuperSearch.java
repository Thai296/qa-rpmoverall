package com.overall.accession.accessionProcessing.superSearch;

import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.patientportal.dao.IGenericDaoPatientPortal;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.platform.dao.IGenericDaoPlatform;
import com.xifin.qa.config.Configuration;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SuperSearch {
	protected static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(1);
	private final RemoteWebDriver driver;
	private Configuration config;
	private IGenericDaoPlatform platformDao;
	private IGenericDaoPatientPortal patientPortalDao;
	private WebDriverWait wait;

	private static final Logger logger = Logger.getLogger(SuperSearch.class);
	public SuperSearch(RemoteWebDriver driver){
		this.driver = driver;
	}
	public SuperSearch(RemoteWebDriver driver, Configuration config, WebDriverWait wait)
	{
		this.driver = driver;
		this.config = config;
		this.wait=wait;
		this.platformDao = new DaoManagerPlatform(config.getRpmDatabase());
		this.patientPortalDao = new DaoManagerPatientPortal(config.getRpmDatabase());
	}
	
	public WebElement contentFrame() throws Exception {
		return driver.findElement(By.id("content"));
	}
	public WebElement accessionSuperSearchTitle()
	{
		return driver.findElement(By.cssSelector(".platormPageTitle"));
	}
	
	public WebElement searchIdInput() throws Exception {
		//return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#content').contents().find('#searchId')[0]"); 
		return driver.findElement(By.id("searchId"));
	}
	public WebElement searchIdInputPF() throws Exception {
		//return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#content').contents().find('#searchId')[0]");
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("lookupSearchId")));
	}
	
	public WebElement filter1Dropdown() throws Exception {
		return driver.findElement(By.id("filter1"));
	}
	
	public WebElement filterDropdown(int i) throws Exception {
		return driver.findElement(By.id("filter" + i));
	}
	
	public WebElement filter1Input()  {
		return driver.findElement(By.id("val1"));
	}
	
	public WebElement filterInput(int i) {
		return driver.findElement(By.id("val" + i));
	}
		
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	public WebElement includeEpCheckbox() {
		return driver.findElement(By.name("inclEp"));
	}
	public WebElement includeEpCheckboxPF() {
		return driver.findElement(By.name("includeEpAccnChkBox"));
	}
	public WebElement filterInputPF(int i)  {
		return driver.findElement(By.id("s2id_filter" + i));
	}
	public WebElement select2DropInput()
    {
        return driver.findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
    }
	public WebElement saveBtnPF() {
		return driver.findElement(By.id("btnSave"));
	}

	//------------------------------------------------------------------------------------------------------------------------------
	public void superSearchAccn(SeleniumBaseTest b, String accnId) throws Exception{
		b.selectItem(filter1Dropdown(), "Accession");
		filter1Input().sendKeys(accnId);
		submitBtn().click();
		logger.info("        Entered data in search filter");
	}
	
	//------------------------------------------------------------------------------------------------------------------------------
	public void setFilter1(String str, String value) throws Exception{
        Select select = new Select(filter1Dropdown());
		select.selectByValue(str);
        filter1Input().sendKeys(value);
        logger.info("Selected Filter1: " + str);
        logger.info("Entered value in Filter1 : " + value);
	}
	
	public void setFilter(SeleniumBaseTest b, int filterNum, String filter, String value) throws Exception{
        b.selectItem(filterDropdown(filterNum), filter);  
        filterInput(filterNum).sendKeys(value);
        logger.info("Selected Filter: " + filterNum + ", Selected: " + filter + ", Entered Value: " + value);
	}
	public void setFilterByText(String text, String value) throws Exception{
		Select select = new Select(filter1Dropdown());
		select.selectByVisibleText(text);
		filter1Input().sendKeys(value);
		logger.info("Selected Filter1: " + text);
		logger.info("Entered value in Filter1 : " + value);
	}

	public void setFilter1DropDown(int filterNumber, String filter, WebDriverWait wait)
    {
        WebElement filterInputPFDropDown = filterInputPF(filterNumber);
        wait.until(ExpectedConditions.elementToBeClickable(filterInputPFDropDown));
		filterInputPFDropDown.click();
        select2DropInput().sendKeys(filter+ Keys.ENTER);
        logger.info("Entered Input Into Filter DropDown: " + filter);
    }
    public void setFilterAndValueIntoSearch(int filterNumber, String filter, String value, WebDriverWait wait){
		setFilter1DropDown(filterNumber, filter, wait);
		filterInput(filterNumber).sendKeys(value);
		logger.info("Entered value in Filter1 : " + value);

	}
	public void performCheckClaimStatusActionOnSuperSearch(String accnId,  WebDriverWait wait) throws Exception
	{
		 int currentWindowCount = driver.getWindowHandles().size();
		logger.info("Load accn on Accn Super Search - " + accnId);
		setFilterAndValueIntoSearch(1, "Accession", accnId, wait);
		includeEpCheckboxPF().click();
		saveBtnPF().click();
		waitForSuperSearchResultsScreen(currentWindowCount, QUEUE_WAIT_TIME);
		logger.info("Perform the action Check Claim Status on Accn Super Search - " + accnId);
		performActionInSuperSearchPF("Check Claim Status", 1, wait);
		waitForSuperSearchResultsScreenToCollapse(currentWindowCount, QUEUE_WAIT_TIME);
	}
	public void performActionInSuperSearchPF(String action, int rowNumber, WebDriverWait wait) throws Exception
	{
		SuperSearchResults superSearchResults = new SuperSearchResults(driver);
		wait.until(ExpectedConditions.elementToBeClickable(superSearchResults.submitBtnPF()));
		superSearchResults.selectCheckBoxPF(rowNumber).click();
		superSearchResults.setActionDropDownPF(action, wait);
		superSearchResults.submitBtnPF().click();
	}
	public void performActionInSuperSearchAndAdjCode(SeleniumBaseTest b, String action, String adjCode, int rowNumber, WebDriverWait wait) throws Exception
	{
		SuperSearchResults superSearchResults = new SuperSearchResults(driver);
		wait.until(ExpectedConditions.elementToBeClickable(superSearchResults.submitBtnPF()));
		superSearchResults.selectCheckBoxPF(rowNumber).click();
		superSearchResults.setActionDropDownPF(action, wait);
		superSearchResults.setADjCodeDropDownPF(b, adjCode, wait);
		superSearchResults.submitBtnPF().click();
	}

	public void waitForSuperSearchResultsScreen(final int currentWindowCount, long maxTime) throws InterruptedException
	{
		WebDriverWait popupWait = new WebDriverWait(driver, TimeUnit.MILLISECONDS.toSeconds(maxTime));
		popupWait.until(new ExpectedCondition<Boolean>()
		{
			public Boolean apply(WebDriver d)
			{
				return (d.getWindowHandles().size() > currentWindowCount);
			}
		});
		switchToPopupWin();
	}
	public String switchToPopupWin() throws InterruptedException
	{
		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> it = handles.iterator();
		String switchWin = null;

		while (it.hasNext()){
			switchWin = it.next();
			logger.info("        Switching to Pop Up Window: " + driver.switchTo().window(switchWin).getTitle());
			driver.switchTo().window(switchWin);
			try
			{
				driver.manage().window().maximize();
			}
			catch (WebDriverException e)
			{
				// Not all popups can be maximized
				logger.warn("Unable to maximize window, window="+driver.getTitle());
			}
		}
		Thread.sleep(2000);
		return parentWindow;
	}
	private void waitForSuperSearchResultsScreenToCollapse(final int currentWindowCount, long maxTime) throws InterruptedException
	{
		WebDriverWait popupWait = new WebDriverWait(driver, TimeUnit.MILLISECONDS.toSeconds(maxTime));
		popupWait.until(new ExpectedCondition<Boolean>()
		{
			public Boolean apply(WebDriver d)
			{
				return (d.getWindowHandles().size() == currentWindowCount);
			}
		});
	}

	public void writeOffAccn(SeleniumBaseTest b, String accnId, WebDriverWait wait) throws Exception {
		int currentWindowCount = driver.getWindowHandles().size();
		String winHandler = driver.getWindowHandle();

		logger.info("Load accnId on Accession Processing - Super Search - " + accnId);
		setFilterAndValueIntoSearch(1, "Accession", accnId, wait);
		saveBtnPF().click();
		waitForSuperSearchResultsScreen(currentWindowCount, QUEUE_WAIT_TIME);

		logger.info("Write-Off Accession on Accn Super Search Results - " + accnId);
		performActionInSuperSearchAndAdjCode(b,"Write-Off Accession","TEST",1,wait);
		waitForSuperSearchResultsScreenToCollapse(currentWindowCount, QUEUE_WAIT_TIME);
		b.switchToWin(winHandler);
		Thread.sleep(3000);
	}

	public void performForceToResubmitActionOnSuperSearch(String accnId,  String submissionPyrAbbrv, WebDriverWait wait) throws Exception
	{
		int currentWindowCount = driver.getWindowHandles().size();
		logger.info("Loading accession on Super Search, accnId=" + accnId+", submissionPyrAbbrv="+submissionPyrAbbrv);
		setFilterAndValueIntoSearch(1, "Accession", accnId, wait);
		setFilterAndValueIntoSearch(2, "Payor ID - Current Submission Payor", submissionPyrAbbrv, wait);
		saveBtnPF().click();
		waitForSuperSearchResultsScreen(currentWindowCount, QUEUE_WAIT_TIME);
		logger.info("Performing the action Force to Resubmit on Super Search, accnId="+ accnId+", submissionPyrAbbrv="+submissionPyrAbbrv);
		SuperSearchResults superSearchResults = new SuperSearchResults(driver);
		wait.until(ExpectedConditions.elementToBeClickable(superSearchResults.submitBtnPF()));
		superSearchResults.selectCheckBoxPF(1).click();
		superSearchResults.setActionDropDownPF("Force to Resubmit", wait);
		superSearchResults.setClaimTypeDropDown("Original", wait);
		superSearchResults.submitBtnPF().click();
		waitForSuperSearchResultsScreenToCollapse(currentWindowCount, QUEUE_WAIT_TIME);
	}

	public void performForceToResubmitActionOnSuperSearch(String accnId, WebDriverWait wait) throws Exception
	{
		int currentWindowCount = driver.getWindowHandles().size();
		logger.info("Loading accession on Super Search, accnId=" + accnId);
		setFilterAndValueIntoSearch(1, "Accession", accnId, wait);
		saveBtnPF().click();
		waitForSuperSearchResultsScreen(currentWindowCount, QUEUE_WAIT_TIME);
		logger.info("Performing the action Force to Resubmit on Super Search, accnId="+ accnId);
		SuperSearchResults superSearchResults = new SuperSearchResults(driver);
		wait.until(ExpectedConditions.elementToBeClickable(superSearchResults.submitBtnPF()));
		superSearchResults.selectCheckBoxPF(1).click();
		superSearchResults.setActionDropDownPF("Force to Resubmit", wait);
		superSearchResults.submitBtnPF().click();
		waitForSuperSearchResultsScreenToCollapse(currentWindowCount, QUEUE_WAIT_TIME);
	}
}
