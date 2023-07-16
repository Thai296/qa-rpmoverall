package com.overall.filemaint.logoConfiguration;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LogoConfiguration {
	private WebDriverWait wait;
	private final RemoteWebDriver driver;
	protected Logger logger;
		
	public LogoConfiguration(RemoteWebDriver driver, WebDriverWait wait)  {
		this.wait = wait;
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*** Load Logo Configuration Page ***/
	public WebElement logoConfigHeaderHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_logo_configuration_header']")));
	}
	
	public WebElement logoConfigLoadPageHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_logo_configuration_load_logo_configuration']")));
	}
	
	public WebElement logoConfigLoadPageTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='platormPageTitle']")));
	}
	
	public WebElement logoConfigLoadPageDocumentTypeDDL() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("s2id_logoType")));
	}
	
	public WebElement logoConfigLoadPageFacilityIdDDL() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("s2id_facilityAbbrev")));
	}
	
	public WebElement logoConfigLoadPageDocumentTypeDDLChosenTxt() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='s2id_logoType']/a/span[contains(@class,'select2-chosen')]")));
	}

	public void uploadLogo(String filePath) {
		xifinUploadUploader().sendKeys(filePath);
	}

	public WebElement logoConfigSelectedLogoName(String value) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[text()='"+value+"']")));
	}

	public WebElement malwareScanningStatus() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("malwareScanningStatus")));
	}

	public WebElement btnMalwereScanningCloseDialog() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn_malwereScanning_closeDialog")));
	}

	public WebElement logoLocationTxtInDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_selectedLogoName']//span[@class='select2-chosen']")));
	}
	
	public WebElement logoConfigLoadPageFacilityIDInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("facilityAbbrev")));
	}
	
	public WebElement logoConfigLoadPageFacilityIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("facilityAbbrevDisplay")));
	}
	
	public WebElement logoConfigLoadPageSearcgFacilityBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("facility_abbrev_search_btn")));
	}
	/*** End Load Logo Configuration Page ***/
	
	/*** Logo Configuration Detail Page ***/
	/*** Header ***/
	public WebElement logoConfigDetailPageTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
	}
	
	public WebElement logoConfigDetailPageDocumentTypeTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logoTypeDisplay")));
	}
	
	public WebElement logoConfigDetailPageFacilityIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("facilityAbbrevDisplay")));
	}
	
	public WebElement logoConfigDetailPageCurrentLogoNameTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("currentLogoName")));
	}
	
	public WebElement logoConfigDetailPageCurrentLogoUploadDtTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("currentLogoUploadDt")));
	}
	
	public WebElement logoConfigDetailPageHeaderHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_logo_configuration_header']")));
	}
	
	/*** Session 1 ***/
	public WebElement documentTypeTitleSectionTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class,'logoConfigSection')]//span[@class='titleText']")));
	}
	
	public WebElement documentTypeHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_logo_configuration_annual_disclosure_letter_logo']")));
	}
	
	public WebElement updateLogoRad() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']//input[@value='Update']")));
	}
	
	public WebElement uploadLogoRad() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']//input[@value='Add']")));
	}
	
	public WebElement uploadLogoBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xifinUpload_maskButton")));
	}
	
	public WebElement xifinUploadUploader() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xifinUpload_uploader")));
	}
	
	public WebElement uploadNameFileText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xifinUpload_countFile")));
	}
	
	public WebElement availableLogosDdl() {
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_selectedLogoName")));
	}
	
	public WebElement dateUploadInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("selectedLogoDateUploaded")));
	}
	
	public WebElement deleteFileBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deleteFile")));
	}
	
	public WebElement logoSizeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logoSize")));
	}
	
	public WebElement logoSizeTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='logoSize unit formfield']")));
	}
	
	public WebElement logoLocationDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_logoLocation")));
	}
	
	public WebElement logoLocationTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_logoLocation']//span[@class='select2-chosen']")));
	}
	
	public WebElement previewLogoBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("previewLogo")));
	}
	
	public void waitForLogoSessionDisplay(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.logoConfigSection section.groupContainer")));
	}
			/*** Current System Logos ***/
	
	public WebElement currentSystemLogosTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_currentSystemLogos")));
	}
	
	public WebElement currentSystemLogosTblLogoNameFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_fileName")));
	}
	
	public WebElement currentSystemLogosTblDocumentTypFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_docType")));
	}
	
	public WebElement currentSystemLogosTblDateUploadedFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_uploadDt")));
	}
	
	public WebElement currentSystemLogosTblLogoNameColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_currentSystemLogos']//tr["+row+"]/td[@aria-describedby='tbl_currentSystemLogos_fileName']")));
	}
	
	public WebElement currentSystemLogosTblDocumentTypColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_currentSystemLogos']//tr["+row+"]/td[@aria-describedby='tbl_currentSystemLogos_docType']")));
	}
	
	public WebElement currentSystemLogosTblDateUploadedColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@id='tbl_currentSystemLogos']//tr["+row+"]/td[@aria-describedby='tbl_currentSystemLogos_uploadDt']")));
	}
	
	public WebElement currentSystemLogosTblHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_logo_configuration_current_system_logos']")));
	}
	
	public WebElement currentSystemLogosTblFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_currentSystemLogos_pagernav")));
	}
	
	public WebElement currentSystemLogosTblPrevPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_currentSystemLogos_pagernav")));
	}
	
	public WebElement currentSystemLogosTblNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_currentSystemLogos_pagernav")));
	}
	
	public WebElement currentSystemLogosTblLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_currentSystemLogos_pagernav")));
	}

	public WebElement currentSystemLogosTblTotalPagesTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@id,'sp') and contains(@id,'tbl_currentSystemLogos_pagernav')]")));
	}
	
	public WebElement currentSystemLogosTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'tbl_currentSystemLogos_pagernav_center')]//parent::td//following-sibling::input")));
	}
	
	public WebElement currentSystemLogosTblTotalResulLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_currentSystemLogos_pagernav_right']/div")));
	}
	
	/*** Footer ***/
	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement saveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	/*** Message ***/
	public WebElement logoConfigErrorMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sectionServerMessages']//p")));
	}
	
	public WebElement logoConfigConfirmDialogMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dialog-confirm")));
	}
	
	public WebElement logoConfigConfirmDialogOkBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='dialog-confirm']//following-sibling::div//button/span[text()='OK']")));
	}
	
	public WebElement logoConfigConfirmDialogDeleteFileBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='dialog-confirm']//following-sibling::div//button/span[text()='Delete File']")));
	}

}
