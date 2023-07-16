package com.overall.fileMaintenance.clientBillingCategory;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClientBillingCategory {
	private WebDriverWait wait;
	protected Logger logger;

	public ClientBillingCategory(WebDriverWait wait) {
		this.wait= wait;
		logger = Logger.getLogger(this.getClass().getSimpleName());
	}

	public WebElement clientBillingCategoryPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='fmClientBillingForm']//span[@class='platormPageTitle']")));
	}

	public WebElement gridViewTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gview_tbl_fmclientbilling")));
	}

	public WebElement tableClientBilling() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_fmclientbilling")));
	}
	
	public WebElement ClientBillingCategoryTblIdColtxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-labelledby='gbox_tbl_fmclientbilling']//tr["+ row +"]//input[@id='gs_abbrev']")));
	}
	
	public WebElement ClientBillingCategoryTblRow(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_fmclientbilling']//tr["+ row +"]")));
	}

	public WebElement addIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_fmclientbilling']/div")));
	}

	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}

	public WebElement addRecordPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='editmodtbl_fmclientbilling']//span[text()='Add Record']")));
	}
	
	public WebElement editRecordPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='editmodtbl_fmclientbilling']//span[text()='Edit Record']")));
	}

	public WebElement IDField() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("abbrev")));
	}

	public WebElement descField() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("descr")));
	}

	public WebElement serviceCodeField() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("serviceCode")));
	}

	public WebElement descOnClnStmtField() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnStmtDescr")));
	}

	public WebElement dialysisCheckbox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dialysis")));
	}

	public WebElement billRentalDropBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_billToRenalTests']/a")));
	}

	public WebElement billNonRentalDropBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_billToNonRenalTests']/a")));
	}

	public WebElement OKBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
	}

	public WebElement errorMsg() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}

	public WebElement idErrorMsg() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='FormError']/td")));
	}

	public WebElement billRentalDropdownInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));
	}

	public void setIDField(String id) {
		IDField().sendKeys(id);
		IDField().sendKeys(Keys.TAB);
		logger.info("        Enter ID: " + id);
	}

	public void setDescriptionField(String desc) {
		descField().sendKeys(desc);
		descField().sendKeys(Keys.TAB);
		logger.info("        Enter description: " + desc);
	}

	public void setServiceCodeField(String serviceCode) {
		serviceCodeField().sendKeys(serviceCode);
		serviceCodeField().sendKeys(Keys.TAB);
		logger.info("        Enter service code on client statement: " + serviceCode);
	}

	public void setDescOnStmtField(String descStmt) {
		descOnClnStmtField().sendKeys(descStmt);
		descOnClnStmtField().sendKeys(Keys.TAB);
		logger.info("        Enter description on client Statement: " + descStmt);
	}

	public void clickBillRentalDropdownList() {
		billRentalDropBox().click();
	}

	public void clickBillNonRentalDropdownList() {
		billNonRentalDropBox().click();
	}

	public void selectBillRentalDropdown(String value) {
		clickBillRentalDropdownList();
		billRentalDropdownInput().sendKeys(value);
		billRentalDropdownInput().sendKeys(Keys.TAB);
		logger.info("        Select Bill Rental dropdown list: " + value);
	}

	public void selectBillNonRentalDropdownList(String value) {
		clickBillNonRentalDropdownList();
		billRentalDropdownInput().sendKeys(value);
		billRentalDropdownInput().sendKeys(Keys.TAB);
		logger.info("        Select bill Non rental dropdown list: " + value);
	}

	public WebElement clientBillingCategoryHelpIconOnHeader() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//a[contains(@class, 'helpIcon') and not (contains(@title, 'section'))]")));
	}

	public WebElement clientBillingCategoryHelpIconOnClnBillingSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class, 'helpIcon') and contains(@title, 'section')]")));
	}

	public WebElement popupTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edithdtbl_fmclientbilling']/span")));
	}

	public WebElement warningMsgTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ui-id-1")));
	}

	public WebElement warningMsgTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='confirmationDialog']//span")));
	}
	
	public WebElement lockMsgTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fmClientBillingForm']//p")));
	}
	
	public By lockMsgTxtLocator() {
		return By.xpath(".//*[@id='fmClientBillingForm']//p");
	}

	public WebElement warningMsgResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class = 'ui-button-text' and contains(text(),'Reset')]")));
	}

	public WebElement warningMsgCancelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class = 'ui-button-text' and contains(text(),'Cancel')]")));
	}

	public WebElement getDataCelOnClnBilTbl(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_fmclientbilling']/tbody/tr[" + row + "]/td[" + col + "]")));
	}

	public WebElement saveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}

	public WebElement idSearchTxtBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_abbrev")));
	}

	public void setSearchIDTxtBox(String value) {
		try {
			idSearchTxtBox().sendKeys(value);
			idSearchTxtBox().sendKeys(Keys.TAB);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.info("Interrupted Exception", e);
		}
		logger.info("        Enter Search ID: " + value);
	}

	public void inputClnBillingInfo(String id, String desc, String svcCode, String descOnClnStm, String billRental,
			String billNonRental) {
		IDField().clear();
		setIDField(id);
		descField().clear();
		setDescriptionField(desc);
		serviceCodeField().clear();
		setServiceCodeField(svcCode);
		descOnClnStmtField().clear();
		setDescOnStmtField(descOnClnStm);
		selectBillRentalDropdown(billRental);
		selectBillNonRentalDropdownList(billNonRental);
		logger.info("        Input Client Biling Category Info ");
	}

	public WebElement billRentalDropBoxText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_billToRenalTests']/a/span[1]")));
	}

	public WebElement billNonRentalDropBoxText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_billToNonRenalTests']/a/span[1]")));
	}

	public void clearSearchIDTxtBox() {
		idSearchTxtBox().click();
		idSearchTxtBox().clear();
		idSearchTxtBox().sendKeys(Keys.BACK_SPACE);
		logger.info("        Clear Search ID");
	}
	
	public WebElement lastDialysisCheckboxInClnBillingCategoryTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[last()]//*[@aria-describedby='tbl_fmclientbilling_dialysis']/input")));
	}
	
	public By savingLoadingMessage() {
		return By.className("xf_progress_msg_div");
	}
}
