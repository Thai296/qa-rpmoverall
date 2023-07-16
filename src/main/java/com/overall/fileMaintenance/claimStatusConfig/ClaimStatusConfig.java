package com.overall.fileMaintenance.claimStatusConfig;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class ClaimStatusConfig {

	private WebDriverWait wait;
	private RemoteWebDriver driver;
	protected Logger logger;

	public ClaimStatusConfig(RemoteWebDriver driver, WebDriverWait wait) {
		this.wait = wait;
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public WebElement fileMaintenancePageTittle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class, 'platormPageTitle')]")));
	}

	public WebElement fileMaintenanceTittleTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='claimStatusConfig']/div/div[2]/div[2]/div[2]/div/section/header/div[1]/span/span")));
	}

	public WebElement runAuditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}

	public WebElement payorSetupTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_claimStatusConfigDetail")));
	}

	public WebElement payorIdFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorAbbrev")));
	}

	public WebElement nameFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorName")));
	}

	public WebElement outgoingPayorIdFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_outPyrId")));
	}

	public WebElement addBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_claimStatusConfigDetail']/div")));
	}

	public WebElement editBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edit_tbl_claimStatusConfigDetail']/div")));
	}

	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}

	/*public WebElement saveBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(text(),'ave And Clear')]/span[contains(text(),'S')]")));
	}*/
	
	public WebElement saveBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}

	public WebElement payorIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorAbbrev")));
	}

	public WebElement payorNameTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorName")));
	}

	public WebElement outPyrIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("outPyrId")));
	}

	public WebElement addRecordPayorIdSearch() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_payorAbbrev']/td[1]/div/a/span")));
	}

	public WebElement outPyrIdSearch() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_outPyrId']/td[1]/div/a/span")));
	}

	public WebElement activeCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("active")));
	}

	public WebElement okBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
	}

	public WebElement cancelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
	}

	public WebElement deleteCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("colHDelete")));
	}

	public WebElement addRecordPopupTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edithdtbl_claimStatusConfigDetail']/span")));
	}

	public WebElement editRecordPopupTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edithdtbl_claimStatusConfigDetail']/span")));
	}

	public WebElement claimStatusConfigDetailPopup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_claimStatusConfigDetail")));
	}

	public void enterPayorIdTxt(String value) {
		payorIdTxt().sendKeys(value);
		payorIdTxt().sendKeys(Keys.TAB);
		logger.info("        Input " + value + " into PayorId");
	}

	public WebElement outgoingPayorIdResearchPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_outgoingPayorIdSearch']/div[1]/span")));
	}

	public WebElement payorSearchPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='payorSearch']/section/header/span")));
	}

	public WebElement payorIdFieldInPayorSearchPage() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorAbbrev")));
	}

	public void enterPayorIdInPayorSearchPage(String value) {
		payorIdFieldInPayorSearchPage().sendKeys(value);
		payorIdFieldInPayorSearchPage().sendKeys(Keys.TAB);
		logger.info("        Input " + value + " into payorId field of Payor Search Page");
	}

	public WebElement searchBtnInPayorSearchPage() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/section/div/div/div/button[1]")));
	}

	public WebElement getRowCount() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class, 'rowCount')]")));
	}

	public WebElement getRowColInTbl(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_claimStatusConfigDetail']/tbody/tr[" + row + "]/td[" + col + "]")));
	}

	public WebElement getRowColInputInTbl(int row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_claimStatusConfigDetail']/tbody/tr[" + row + "]/td[6]/input")));
	}

	public List<String> getListDataInColPayorSetupTbl(int colNumber) {
		int totalRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(".//*[@id='gbox_tbl_claimStatusConfigDetail']//div//tbody/tr[contains(@class,'ui-widget-content')]"))).size();
		List<String> returnList = new ArrayList<String>();
		for (int i = 1; i <= totalRows; i++) {
			String celText = getRowColInTbl(i, colNumber).getText();
			returnList.add(celText);
		}
		return returnList;
	}

	public WebElement pyrIDColOnTbl(int row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_claimStatusConfigDetail']/tbody/tr[" + row + "]/td[3]")));
	}

	public WebElement auditLogWaitTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_auditlogwait")));
	}

	public WebElement auditLogTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-jqgrid-title")));
	}

	public WebElement getRowDataInUuditLogTbl(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr[" + row + "]/td[" + col + "]")));
	}

	public WebElement payorSearchTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorsearchTable")));
	}

	public void enterPayorIdFilter(String value) {
		payorIdFilter().clear();
	    payorIdFilter().sendKeys(value);
	    payorIdFilter().sendKeys(Keys.ENTER);
		logger.info("        Input " + value + " into payorId field of PayorId Search ");
	}

	public WebElement totalRecordInPayorSearchResultTable() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}

	public void waitForSearchResultLoadingSuccessfully(int timeout) {
		WebDriverWait customWait = new WebDriverWait(driver, timeout);
		customWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_payorsearchTable")));
	}

	public void selectRowByValue(String value) throws Exception {
		Actions actions = new Actions(driver);
		List<WebElement> rows = payorSetupTbl().findElements(By.tagName("tr"));
		for (int i = 2; i <= rows.size(); i++) {
			String val = pyrIDColOnTbl(i).getText().trim();
			if (val.equalsIgnoreCase(value.trim())) {
				actions.moveToElement(pyrIDColOnTbl(i)).doubleClick().build().perform();
				wait.until(ExpectedConditions.visibilityOf(claimStatusConfigDetailPopup()));
				break;
			}
		}
	}

	public WebElement rowOntbl(String id) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_claimStatusConfigDetail']/tbody/tr[@id='" + id + "']")));
	}

	public WebElement rowOnTblByLocation(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_claimStatusConfigDetail']/tbody/tr[" + row + "]/td[" + col + "]")));
	}

	public WebElement outgoingPayorIdSearchResultCell(int row, int col) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='tbl_outgoingPayorIdSearch']/tbody/tr[" + row + "]/td[" + col + "]/a")));
    }

	public WebElement outgoingPayorIdSearchResutLnk(String row) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='tbl_outgoingPayorIdSearch']/tbody/tr[" + row + "]/td[2]/a")));
    }

	public WebElement select2DropInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));
	}

	public List<String> checkPyrInfo(String value) {
		List<String> temp = new ArrayList<String>();
		List<WebElement> rows = payorSetupTbl().findElements(By.tagName("tr"));
		for (int i = 2; i <= rows.size(); i++) {
			String val = pyrIDColOnTbl(i).getText().trim();
			if (val.equalsIgnoreCase(value)) {
				for (int j = 3; j < 10; j++) {
					temp.add(rowOnTblByLocation(i, j).getText().trim());
					if (j == 6) {
						WebElement checkbox = rowOnTblByLocation(i, j).findElement(By.tagName("input"));
						if (checkbox.getAttribute("checked").equalsIgnoreCase("true")) {
							temp.add("true");
						}
					}
				}
			}
		}
		return temp;
	}

	public WebElement helpIconOnHeaderMenu() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_claim_status_configuration_header']")));
	}

	public WebElement helpIconOnClaimStatusSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_claim_status_configuration']")));
	}

	public WebElement helpIconOnFooter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_claim_status_configuration_summary']")));
	}
	
	public WebElement deleteCheckBoxOnTbl(int row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_claimStatusConfigDetail']/tbody/tr["+row+"]/td[7]/input")));
	}
	
	public boolean checkDeletedRows() {
		boolean isChecked = false;
		List<WebElement> rows = payorSetupTbl().findElements(By.tagName("tr"));
		for (int i = 2; i <= rows.size(); i++) {
			if (deleteCheckBoxOnTbl(i).getAttribute("checked") != null) {
				if (deleteCheckBoxOnTbl(i).getAttribute("checked").equalsIgnoreCase("true")) {
					isChecked = true;
					break;
				}
			}
		}
		return isChecked;
	}
	
	public WebElement payorSetupTblPayorIdColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_claimStatusConfigDetail']//tr[contains(@class,'ui-widget-content')]["+ row +"]/td[@aria-describedby='tbl_claimStatusConfigDetail_payorAbbrev']")));
	}

	public List<WebElement> payorSetupTblPayorIdColTxts() {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(".//table[@id='tbl_claimStatusConfigDetail']//tr[contains(@class,'ui-widget-content')]//td[@aria-describedby='tbl_claimStatusConfigDetail_payorAbbrev']")));
	}
	
	public WebElement payorSetupTblNameColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_claimStatusConfigDetail']//tr[contains(@class,'ui-widget-content')]["+ row +"]/td[@aria-describedby='tbl_claimStatusConfigDetail_payorName']")));
	}
	
	public WebElement payorSetupTblOutgoingPayorIdColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_claimStatusConfigDetail']//tr[contains(@class,'ui-widget-content')]["+ row +"]/td[@aria-describedby='tbl_claimStatusConfigDetail_outPyrId']")));
	}
	
	public WebElement payorSetupTblActiveColChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_claimStatusConfigDetail']//tr[contains(@class,'ui-widget-content')]["+ row +"]/td[@aria-describedby='tbl_claimStatusConfigDetail_active']/input")));
	}
	
	public WebElement payorSetupTblIsDeletedColChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_claimStatusConfigDetail']//tr[contains(@class,'ui-widget-content')]["+ row +"]/td[@aria-describedby='tbl_claimStatusConfigDetail_colHDelete']/input")));
	}
	
	public WebElement payorGroupFieldInPayorSearchPageDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorGroupName")));
	}

	public WebElement svcTypDropDown()
	{
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_svcTypeId']/a")));
	}

	public WebElement submSvcDropDown()
	{
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_submSvcId']/a")));
	}

	public void waitForChildWindowDisplay(){
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			@Override
			public Boolean apply(WebDriver input) {
				return driver.getWindowHandles().size()>1;
			}
		});
	}

	public void runAudit(){
		wait.until(ExpectedConditions.elementToBeClickable(runAuditBtn()));
		runAuditBtn().click();
		waitForChildWindowDisplay();
	}

	public WebElement iconDeleteInPayorSetup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='jqgh_tbl_claimStatusConfigDetail_colHDelete']")));
	}

	public void setServiceType(String value)
	{
		svcTypDropDown().click();
		select2DropInput().sendKeys(value);
		select2DropInput().sendKeys(Keys.TAB);
		logger.info("Selected Service Type, value=" + value);
	}

	public void setSubmSvc(String value)
	{
		submSvcDropDown().click();
		select2DropInput().sendKeys(value);
		select2DropInput().sendKeys(Keys.TAB);
		logger.info("Selected Submission Service ID, value=" + value);
	}
}
