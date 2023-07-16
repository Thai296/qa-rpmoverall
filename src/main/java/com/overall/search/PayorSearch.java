package com.overall.search;

import com.eviware.soapui.support.StringUtils;
import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.mars.dao.IGenericDaoXifinRpm;
import com.xifin.qa.config.Configuration;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class PayorSearch {
	private static final Logger LOG = Logger.getLogger(PayorSearch.class);

	private final RemoteWebDriver driver;
	private final Configuration config;
	private WebDriverWait wait;

	private IGenericDaoXifinRpm daoManagerXifinRpm;

	public PayorSearch(RemoteWebDriver driver, Configuration config) {
		this.driver = driver;
		this.config = config;
		this.wait = new WebDriverWait(driver, 10);
		this.daoManagerXifinRpm = new DaoManagerXifinRpm(config.getRpmDatabase());
	}
	
	public WebElement payorGroupDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("payorGroupName")));
	}

	public WebElement payorIDTextInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("payorAbbrev")));
	}

	public WebElement payorNameTextInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("payorName")));
	}

	public Select payorGroupSelect() {
		Select dropdown = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.name("payorGroupName"))));
		return dropdown;
	}

	public WebElement payorGroupSelectWebElement() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorGroupName")));
	}

	public Select XRefTypeSelect() {
		Select dropdown = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.name("xrefType"))));
		return dropdown;
	}

	public WebElement XRefTypeSelectWebElement() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("xrefType")));
	}

	public WebElement XRefMemberDropList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xrefMemberSelect")));
	}

	public WebElement addressInputText() {
		// return driver.findElement(By.id("address"));
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("address1")));
	}

	public WebElement cityInputText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("city")));
	}

	public Select stateSelect() {
		Select dropdown = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("stateDroplist"))));
		return dropdown;
	}

	public WebElement stateSelectWebElement() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("stateDroplist")));
	}

	public WebElement postalCodeInputText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("zip")));
	}

	public Select countrySelect() {
		Select dropdown = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("countryDroplist"))));
		return dropdown;
	}

	public WebElement countrySelectWebElement() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("countryDroplist")));
	}

	public WebElement phoneInputText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("phone")));
	}

	public WebElement closeBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Close")));
	}

	public WebElement searchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Submit")));
	}

	public WebElement xrefTypeDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("xrefType")));
	}

	public WebElement address1() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("address1")));
	}

	public WebElement clearingHouseIdTextInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("clearingHouseId")));
	}

	public WebElement resetSearchButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Cancel")));
	}

	// all action on payor search popup window

	public void clickResetSearch() {
		resetSearchButton().click();
		LOG.info("        Reset search clicked");
	}

	public void enterClearingHouseId(String data) {
		clearingHouseIdTextInput().clear();
		clearingHouseIdTextInput().sendKeys(excerptNull(data));
		clearingHouseIdTextInput().sendKeys(Keys.TAB);
		LOG.info("        Enter valid data to ClearingHouseID textbox in Payor Search");
	}

	public void enterPayorID(String data) throws Exception {
		payorIDTextInput().clear();
		payorIDTextInput().sendKeys(excerptNull(data));
		payorIDTextInput().sendKeys(Keys.TAB);
		LOG.info("        Enter valid data to Payor ID textbox in Payor Search");
	}

	public void enterPayorName(String data) throws Exception {
		payorNameTextInput().clear();
		payorNameTextInput().sendKeys(excerptNull(data));
		payorNameTextInput().sendKeys(Keys.TAB);
		LOG.info("        Enter valid data to Payor Name textbox in Payor Search");
	}

	public void selectPayorGroup(String data) throws Exception {
		payorGroupSelect().selectByValue(excerptNull(data));
		LOG.info("        Select valid data to Payor Group select in Payor Search");
	}

	public void selectXRefTypeByIndex(int index) {
		try {
			XRefTypeSelect().selectByIndex(index);
		} catch (Exception e) {
			selectXRefTypeByIndex(1);
		}

	}

	public void selectXRefType(String data) {
		String value = excerptNull(data);
		if (value.equals("")) {
			XRefTypeSelect().selectByValue(value);
			LOG.info("        Select valid data to XRefType select in Payor Search");
		} else {
			String[] subValue = value.split("-");
			if (subValue.length != 2) {
				XRefTypeSelect().selectByValue("");
				LOG.info("        Select valid data to XRefType select in Payor Search");
			} else {
				XRefTypeSelect().selectByValue(subValue[0].trim());
				LOG.info("        Select valid data to XRefType select in Payor Search");
			}
		}
	}

	public void enterAddress(String data) {
		addressInputText().clear();
		addressInputText().sendKeys(excerptNull(data));
		addressInputText().sendKeys(Keys.TAB);
		LOG.info("        Enter valid data to Address textbox in Payor Search");
	}

	public void enterCity(String data) {
		cityInputText().clear();
		cityInputText().sendKeys(excerptNull(data));
		cityInputText().sendKeys(Keys.TAB);
		LOG.info("        Enter valid data to City textbox in Payor Search");
	}

	public void selectState(String data) {
		stateSelect().selectByValue(excerptNull(data));
		LOG.info("        Select valid data to State select in Payor Search");
	}

	public void enterPostalCode(String data) {
		postalCodeInputText().clear();
		postalCodeInputText().sendKeys(excerptNull(data));
		postalCodeInputText().sendKeys(Keys.TAB);
		LOG.info("        Enter valid data to Postal textbox in Payor Search");
	}

	public void selectCountry(String data) {
		countrySelect().selectByValue(excerptNull(data));
		LOG.info("        Select valid data to Country select in Payor Search");
	}

	public void enterPhoneNumber(String data) {
		phoneInputText().clear();
		phoneInputText().sendKeys(excerptNull(data));
		phoneInputText().sendKeys(Keys.TAB);
		LOG.info("        Select valid data to Phone select in Payor Search");
	}

	public void clickCloseBtn() {
		closeBtn().click();
		LOG.info("        Click close button in payor search popup window");
	}

	public void clickSearchBtn() {
		searchBtn().click();
		LOG.info("        Click search button in payor search popup window");
	}

	public List<String> enterFieldsToPayorSearchPopupWindow(String testDb) throws Exception {
		List<String> data = daoManagerXifinRpm.getRandomAllPayorInfo(testDb);

		enterPayorID(data.get(1));
		enterPayorName(data.get(2));
		selectPayorGroup(data.get(16));
		enterClearingHouseId(data.get(20));
		selectXRefType(data.get(21));
		enterAddress(data.get(4));
		enterCity(data.get(15));
		selectState(data.get(17));
		enterPostalCode(data.get(6));
		selectCountry(data.get(18));
		enterPhoneNumber(data.get(3));

		return data;
	}

	public String excerptNull(String data) {
		if (StringUtils.isNullOrEmpty(data)) {
			return "";
		} else {
			return data.trim();
		}
	}

	public WebElement payorSearchTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("titleText")));
	}
	public List<String> enterNoPayorContactFieldsToPayorSearchPopupWindow(String testDb) throws Exception {
		List<String> data = daoManagerXifinRpm.getAllPayorInfoNoPayorContact(testDb);

		enterPayorID(data.get(1));
		enterPayorName(data.get(2));
		selectPayorGroup(data.get(16));
		enterClearingHouseId(data.get(20));
		selectXRefType(data.get(21));
		enterAddress(data.get(4));
		enterCity(data.get(15));
		selectState(data.get(17));
		enterPostalCode(data.get(6));
		selectCountry(data.get(18));
		enterPhoneNumber(data.get(3));

		return data;
	}
	
	public List<String> enterPayorInfoToPayorSearchPopupWindow(String testDb) throws Exception {
		List<String> data = daoManagerXifinRpm.getAllPayorInfoNoPayorContact(testDb);

		enterPayorID(data.get(1));
		enterPayorName(data.get(2));
		selectPayorGroup(data.get(16));
		return data;
	}
}
