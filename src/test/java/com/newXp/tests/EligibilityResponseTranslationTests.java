package com.newXp.tests;

import com.mbasys.mars.ejb.entity.eligResp.EligResp;
import com.mbasys.mars.ejb.entity.eligRespSubsRefTyp.EligRespSubsRefTyp;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.fileMaintenance.fileMaintenanceTables.EligibilityConfiguration;
import com.overall.fileMaintenance.fileMaintenanceTables.EligibilityResponseTranslation;
import com.overall.menu.MenuNavigation;
import com.overall.search.EligibilityServiceSearchResults;
import com.overall.search.EligibilitySubServiceSearchResults;
import com.overall.search.PayorSearchResults;
import com.overall.utils.EligibilityConstants;
import com.overall.utils.EligibilityUtils;
import com.overall.utils.TestCodeUtils;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import domain.fileMaintenance.EligibilityConfiguration.EligibilityTranslation;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class EligibilityResponseTranslationTests extends SeleniumBaseTest
{

	private SsoLogin ssoLogin;
	private TimeStamp timeStamp;
	private RandomCharacter randomCharacter;
	private EligibilityResponseTranslation eligibilityResponseTranslation;
	private EligibilityServiceSearchResults eligibilityServiceSearchResults;
	private EligibilityConfiguration eligibilityConfiguration;
	private EligibilitySubServiceSearchResults eligibilitySubServiceSearchResults;
	private PayorSearchResults payorSearchResults;
	private AccessionDetail accessionDetail;
	private TestCodeUtils testCodeUtils;
	private EligibilityUtils eligibilityUtils;
	private static String ID_LOOKUP_ELIG_SVC_ABBREV = "lookupEligSvcAbbrev";
	private static String ID_SAVE_AND_CLEAR_BUTTON = "btnSaveAndClear";
	private EligibilityConstants eligibilityConstants;
	private static String ID_LOOKUP_ELIG_RESP_PYR_ABBREV="lookupEligRespPyrAbbrv";
	private MenuNavigation navigation;

	@BeforeMethod(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
	public void beforeMethod(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String disableBrowserPlugins, Method method)
	{
		try
		{
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			logIntoSso(ssoUsername, ssoPassword);
			navigation = new MenuNavigation(driver, config);
			navigation.navigateToEligibilityResponseTranslationPage();
		}
		catch (Exception e)
		{
			logger.error("Error running BeforeMethod", e);
		}
	}

	@Test(priority = 1, description = "Add new Rule without Subs Override Type in Eligibility Response Translation for XIFIN Elig svc")
	@Parameters({ "eligSvcId", "pyrAbbrev", "newPyrAbbrev", "response", "matchType", "benefitType", "serviceTypeCode" })
	public void testXPR_1081(String eligSvcId, String pyrAbbrev, String newPyrAbbrev, String response, String matchType,
			String benefitType, String serviceTypeCode) throws Exception {
		logger.info("Testing - testXPR_1081");
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);
		cleanEligRespByResponse(response);

		eligibilityUtils = new EligibilityUtils(rpmDao, testDb);
		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);

		logger.info("Enter Pyr Abbrev");
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		EligibilityTranslation eligibilityTranslation = createEligTranslationSetupDataNoSubOverride(eligSvcId, pyrAbbrev,newPyrAbbrev, response, matchType, benefitType, serviceTypeCode, true, true, true);logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);
	
		logger.info("Enter New Payor Id");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getNewPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);

		logger.info("Choose Payor Prio, Match Type and Benefit Type");
		eligibilityResponseTranslation.setMatchTypeDropDown(eligibilityTranslation.getMatchType(), wait);
		eligibilityResponseTranslation.setBenefitTypeDropDown(eligibilityTranslation.getBenefitType(), wait);
		eligibilityResponseTranslation.setServiceTypeCode(eligibilityTranslation.getServiceTypeCode(), wait);

		logger.info("Check Allow Secondary Translation, Bypass Unknown Response Errors, Check Elig check boxes");
		checkNewPyrTranslationRuleCheckBoxes(eligibilityTranslation);

		logger.info("Enter Response");
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);logger.info("Click OK");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();
		EligResp expectedEligResp = eligibilityUtils.mapToEligResp(eligibilityTranslation);
		expectedEligResp.setAudUser(null);
		expectedEligResp.audDt = null;
		expectedEligResp.audRecId = 0;
		expectedEligResp.setResultCode(0);
		expectedEligResp.setEligRespSubsRefTypId(0);

		logger.info("Click on Save And Clear button");
		eligibilityResponseTranslation.clickSaveAndClear(wait);

		logger.info("Verifying eligibility translation Rule is saved in DB, for pyrAbbrev= " + pyrAbbrev);
		List<EligResp> eligResps = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertEquals(eligResps.size(), 1);

		logger.info("Verifying eligibility translation Rule has all checkboxes checked");
		Assert.assertTrue(eligResps.get(0).getIsAllowSecondary());
		Assert.assertTrue(eligResps.get(0).getIsCheckElig());
		Assert.assertTrue(eligResps.get(0).getIsAllowUnmapped());

		logger.info("Verifying eligibility translation Rule has Match Type, Benefit Type and Pyr Prio selected");
		Assert.assertEquals(eligResps.get(0).getEligRespMatchTypId(), 7);
		Assert.assertEquals(eligResps.get(0).getEligRespBenefitTypId(), 2);
		Assert.assertEquals(eligResps.get(0).getPrio(), 1);
		EligResp actualEligResp = eligResps.get(0);
		actualEligResp.setAudUser(null);
		actualEligResp.audDt = null;
		actualEligResp.audRecId = 0;
		actualEligResp.setResultCode(0);
		actualEligResp.setEligRespId(0);

		logger.info("Verifying eligibility translation Rule has No Subs Override Type saved, default 0 = "+actualEligResp.getEligRespSubsRefTypId());
		Assert.assertEquals(actualEligResp.getEligRespSubsRefTypId(), expectedEligResp.getEligRespSubsRefTypId());
		logger.info("expectedEligResp " + actualEligResp + " eligResp " + expectedEligResp);
		Assert.assertEquals(actualEligResp, expectedEligResp);

	}

	@Test(priority = 1, description = "Edit Payor Translation Rule for XIFIN elig svc on Eligibility Response Translation jsp")
	@Parameters({ "eligSvcId", "pyrAbbrev", "newPyrAbbrev", "response", "updatedPyrAbbrev", "matchType","benefitType", "subscrIdTyp"})
	public void testXPR_1082(String eligSvcId, String pyrAbbrev, String newPyrAbbrev, String response,String updatedPyrAbbrev, String matchType, String benefitType, String subscrIdTyp) throws Exception {

		logger.info("Testing - testXPR_1082");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);

		eligibilityUtils = new EligibilityUtils(rpmDao, testDb);
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);
		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Set Eligibility Translation Rule data with valid Payor ID");
		EligibilityTranslation eligibilityTranslation = createEligTranslationSetupDataNoSubOverride(eligSvcId, pyrAbbrev,newPyrAbbrev, response, null, null, null, false, false, false);

		logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);

		logger.info("Enter New Payor Id");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getNewPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Click save and Clear");
		eligibilityResponseTranslation.clickSaveAndClear(wait);

		logger.info("Verifying eligibility translation Rule is saved in DB, for pyrAbbrev= " + pyrAbbrev);
		List<EligResp> eligRespSaved = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertEquals(eligRespSaved.size(), 1);
		Assert.assertEquals(eligRespSaved.get(0).getEligRespSubsRefTypId(), 0);

		logger.info("Reload XIFIN Eligibility Service ID and Pyr Abbrev");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);

		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Find just created record and edit it");
		WebElement pyrTransRuleRow =driver.findElement(By.xpath("//table[@id='tbl_eligibilityResponseTranslation']/tbody/tr/td[contains(., '"+ response + "')]"));

		logger.info("pyrTransRuleRow"+pyrTransRuleRow);
		wait.until(ExpectedConditions.visibilityOf(pyrTransRuleRow));
		pyrTransRuleRow.click();
		clickHiddenPageObject(eligibilityResponseTranslation.editIconEliResponseTranslationTbl(), 0);

		logger.info("Verify that Edit Record Title is displayed");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.editRecordTitleInEliResponseTranslationTbl()));
		Assert.assertEquals(pyrAbbrev, eligibilityResponseTranslation.payorIdInputInPopup().getAttribute("value"));
		Assert.assertEquals(newPyrAbbrev, eligibilityResponseTranslation.newPayorIdInput().getAttribute("value"));

		logger.info("Edit New Pyr Record, Match Type, Benefit Type, Response and save changes");
		eligibilityTranslation = createEligTranslationSetupDataSubOverride(eligSvcId, pyrAbbrev, updatedPyrAbbrev, response,matchType, benefitType, null, true, true, true, subscrIdTyp);

		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getNewPyrId(), wait);
		eligibilityResponseTranslation.setBenefitTypeDropDown(eligibilityTranslation.getBenefitType(), wait);
		eligibilityResponseTranslation.setMatchTypeDropDown(eligibilityTranslation.getMatchType(), wait);
		eligibilityResponseTranslation.setSubIdTypeDropDownList(eligibilityTranslation.getSubIdType(), wait);
		checkNewPyrTranslationRuleCheckBoxes(eligibilityTranslation);

		EligResp updatedEligResp = eligibilityUtils.mapToEligResp(eligibilityTranslation);
		updatedEligResp.setAudUser(null);
		updatedEligResp.audDt = null;
		updatedEligResp.audRecId = 0;
		updatedEligResp.setResultCode(0);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Click save and Clear");
		eligibilityResponseTranslation.clickSaveAndClear(wait);

		logger.info("Verifying eligibility translation Rule is modified in DB, for pyrAbbrev= " + pyrAbbrev);
		eligRespSaved = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertEquals(eligRespSaved.size(), 1);

		logger.info("Create expected eligibility translation Rule data in DB, for pyrAbbrev= " + pyrAbbrev);
		EligResp actualEligResp = new EligResp(eligRespSaved.get(0));
		actualEligResp.setAudUser(null);
		actualEligResp.audDt = null;
		actualEligResp.audRecId = 0;
		actualEligResp.setResultCode(0);
		actualEligResp.setSvcTypCd(null);
		actualEligResp.setEligRespId(0);

		logger.info("updatedEligResp " + updatedEligResp + "actualEligResp " + actualEligResp);
		Assert.assertEquals(actualEligResp, updatedEligResp);
		EligRespSubsRefTyp eligRespSubsRefTyp = rpmDao.getEligRespSubsRefTyByDesrc(testDb, subscrIdTyp);
		Assert.assertEquals(actualEligResp.getEligRespSubsRefTypId(), eligRespSubsRefTyp.getTypId());
	}

	@Test(priority = 1, description = "Delete Payor Translation Rule for XIFIN elig svc on Eligibility Response Translation jsp")
	@Parameters({ "eligSvcId", "pyrAbbrev", "response" ,"subscrIdTyp"})
	public void testXPR_1083(String eligSvcId, String pyrAbbrev, String response, String subscrIdTyp) throws Exception {

		logger.info("Testing - testXPR_1083");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);

		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);
		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Set Eligibility Translation Rule data with valid Payor ID");
		EligibilityTranslation eligibilityTranslation = createEligTranslationSetupDataSubOverride(eligSvcId, pyrAbbrev, pyrAbbrev, response, null, null, null, false, false, false, subscrIdTyp);

		logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);

		logger.info("Enter New Payor Id");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Click save and Clear");
		eligibilityResponseTranslation.clickSaveAndClear(wait);

		logger.info("Verifying eligibility translation Rule is saved in DB, for pyrAbbrev= " + pyrAbbrev);
		List<EligResp> eligResps = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertEquals(eligResps.size(), 1);

		logger.info("Reload XIFIN Eligibility Service ID and Pyr Abbrev");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);

		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Find just created record and edit it");
		WebElement pyrTransRuleRow =driver.findElement(By.xpath("//table[@id='tbl_eligibilityResponseTranslation']/tbody/tr/td[contains(., '"+ response + "')]"));
		wait.until(ExpectedConditions.visibilityOf(pyrTransRuleRow));
		pyrTransRuleRow.click();
		clickHiddenPageObject(eligibilityResponseTranslation.editIconEliResponseTranslationTbl(), 0);

		logger.info("Verify that Edit Record is displayed");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.deleteCheckBox()));

		logger.info("Check Delete checkbox");
		eligibilityResponseTranslation.deleteCheckBox().click();

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Click save and Clear");
		eligibilityResponseTranslation.clickSaveAndClear(wait);

		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligibilityTranslationTitle()));
		logger.info("Make sure Pyr Trasnlation Rule is deleted");
		List<EligResp> deletedPyrEligResp = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertTrue(deletedPyrEligResp.isEmpty());

		logger.info("Reload XIFIN Eligibility Service ID and Pyr Abbrev");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);

		logger.info("Enter Pyr Abbrev");
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));

		logger.info("Verify that Pyr translation record is not displayed");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//table[@id='tbl_eligibilityResponseTranslation']/tbody/tr/td[contains(., '"+ response + "')]")));

		eligibilityResponseTranslation.clickSaveAndClear(wait);
	}

	@Test(priority = 1, description = "Veryfy Help page on XIFIN Eligibility Service Elig Response Translation grid")
	@Parameters({ "eligSvcId", "pyrAbbrev", "response" })
	public void testXPR_1084(String eligSvcId, String pyrAbbrev, String response) throws Exception {

		logger.info("Testing - testXPR_1084");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);

		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);
		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Verify that the Eligibility Translation page is displayed with correct page title");
		assertEquals(eligibilityResponseTranslation.eligibilityTranslationTitle().getText(),"Eligibility Translation");

		String parentWindow = driver.getWindowHandle();

		logger.info("Click Help icon in Eligibility Response Translation grid");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.helpIconInHeaderSection()));
		clickHiddenPageObject(eligibilityResponseTranslation.helpIconInHeaderSection(), 0);

		logger.info("Verify that Help page in Eligibility Response Translation grid is opened properly");
		switchToPopupWin();
		Assert.assertTrue(driver.getCurrentUrl().contains("p_eligibility_translation_header.htm"));
		Assert.assertTrue(eligibilityResponseTranslation.titleTextInHelp().getText().contains("Eligibility Translation Header"));
		driver.close();
		switchToParentWindow(parentWindow);

        Assert.assertTrue(isElementPresent(eligibilityResponseTranslation.helpIconInEliResponseTranslation(), 5));
		clickHiddenPageObject(eligibilityResponseTranslation.helpIconInEliResponseTranslation(), 0);
		switchToPopupWin();
		Assert.assertTrue(driver.getCurrentUrl().contains("p_eligibility_translation_eligibility_response_translation.htm"));
		Assert.assertTrue(eligibilityResponseTranslation.titleTextInHelp().getText().contains("Eligibility Response Translation"));
		driver.close();
		switchToParentWindow(parentWindow);

		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_LOOKUP_ELIG_SVC_ABBREV))));
	}


	@Test(priority = 1, description = "Create Payor Translation Rule and Reset on XIFIN Eligibility Config jsp - rule is not saved")
	@Parameters({ "eligSvcId", "pyrAbbrev", "response", "matchType", "benefitType", "subscrIdTyp" })
	public void testXPR_1085(String eligSvcId, String pyrAbbrev, String response, String matchType, String benefitType, String subscrIdTyp)
			throws Exception {

		logger.info("Testing - testXPR_1085");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);

		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);
		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Set Eligibility Translation Rule data with valid Payor ID");
		EligibilityTranslation eligibilityTranslation = createEligTranslationSetupDataSubOverride(eligSvcId, pyrAbbrev, pyrAbbrev, response, matchType,benefitType, null, true, true, true, subscrIdTyp);

		logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);

		logger.info("Enter New Payor Id, Response, Match and Benefit Types");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);
		eligibilityResponseTranslation.setMatchTypeDropDown(eligibilityTranslation.getMatchType(), wait);
		eligibilityResponseTranslation.setBenefitTypeDropDown(eligibilityTranslation.getBenefitType(), wait);
		eligibilityResponseTranslation.setSubIdTypeDropDownList(eligibilityTranslation.getSubIdType(), wait);
		checkNewPyrTranslationRuleCheckBoxes(eligibilityTranslation);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Make sure Record is displayed in the table");
		WebElement pyrTransRuleRow =driver.findElement(By.xpath("//table[@id='tbl_eligibilityResponseTranslation']/tbody/tr/td[contains(., '"+ response + "')]"));
		wait.until(ExpectedConditions.visibilityOf(pyrTransRuleRow));

		logger.info("Click on Reset button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.resetBtn()));
		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);
		String alert = driver.switchTo().alert().getText();
		logger.info(alert);
		driver.switchTo().alert().accept();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_LOOKUP_ELIG_SVC_ABBREV))));

		logger.info("Verifying eligibility translation Rule is NOT saved in DB, for pyrAbbrev= " + pyrAbbrev);
		List<EligResp> eligResps = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertTrue(eligResps.isEmpty());

		logger.info("Reload XIFIN Eligibility Service ID and Pyr Abbrev");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);

		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Pyr translation record is not displayed");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//table[@id='tbl_eligibilityResponseTranslation']/tbody/tr/td[contains(., '"+ response + "')]")));

		logger.info("Click Reset");
		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_LOOKUP_ELIG_SVC_ABBREV))));

	}

	@Test(priority = 1, description = "Create 2 new Rules, Second is duplicate and See Error Message 'A duplicate record was entered'")
	@Parameters({ "eligSvcId", "pyrAbbrev", "response" })
	public void testXPR_1086(String eligSvcId, String pyrAbbrev, String response) throws Exception {

		logger.info("Testing - testXPR_1086");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);

		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);
		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Set Eligibility Translation Rule data with valid Payor ID");
		EligibilityTranslation eligibilityTranslation = createEligTranslationSetupDataNoSubOverride(eligSvcId, pyrAbbrev, pyrAbbrev, response, null, null, null, false, false, false);

		logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);

		logger.info("Enter New Payor Id");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();
		//wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_SAVE_AND_CLEAR_BUTTON))));

		logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);

		logger.info("Enter New Payor Id");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Verify that error message 'A duplicate record was entered' is displayed. Click Cancel button.");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.errMsgInAddRecordPopup()));
		Assert.assertEquals(eligibilityResponseTranslation.errMsgInAddRecordPopup().getText(),"A duplicate record was entered","Error message 'A duplicate record was entered' should be displayed.");
		clickHiddenPageObject(eligibilityResponseTranslation.cancelBtn(), 0);

		logger.info("Click on Reset button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.resetBtn()));
		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);
		String alert = driver.switchTo().alert().getText();
		logger.info("Accept the Alert " + alert);
		driver.switchTo().alert().accept();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_LOOKUP_ELIG_SVC_ABBREV))));

		logger.info("Verifying eligibility translation Rule is NOT saved in DB, for pyrAbbrev= " + pyrAbbrev);
		List<EligResp> eligResps = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertTrue(eligResps.isEmpty());

		logger.info("Click on Reset button");
		wait.until(ExpectedConditions.elementToBeClickable(By.id(ID_LOOKUP_ELIG_SVC_ABBREV)));
		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);

	}

	@Test(priority = 1, description = "Create Duplicate Payor Translation Rule and See Error Message 'A duplicate record was entered'")
	@Parameters({ "eligSvcId", "pyrAbbrev", "response" })
	public void testXPR_1087(String eligSvcId, String pyrAbbrev, String response) throws Exception {

		logger.info("Testing - testXPR_1087");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);

		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);
		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Set Eligibility Translation Rule data with valid Payor ID");
		EligibilityTranslation eligibilityTranslation = createEligTranslationSetupDataNoSubOverride(eligSvcId, pyrAbbrev, pyrAbbrev, response, null, null,null, false, false, false);

		logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);

		logger.info("Enter New Payor Id");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Click save and Clear");
		eligibilityResponseTranslation.clickSaveAndClear(wait);

		logger.info("Make sure 1 Pyr Translation Rule is saved");
		List<EligResp> pyrEligResp = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertEquals(pyrEligResp.size(), 1);

		logger.info("Reload XIFIN Eligibility Service ID and Pyr Abbrev");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);

		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Click Add a new row in Payor Translation grid for Duplicate Record");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);

		logger.info("Enter The Same New Payor Id");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Verify that error message 'A duplicate record was entered' is displayed. Click Cancel button.");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.errMsgInAddRecordPopup()));
		assertEquals(eligibilityResponseTranslation.errMsgInAddRecordPopup().getText(),"A duplicate record was entered","Error message 'A duplicate record was entered' should be displayed.");
		clickHiddenPageObject(eligibilityResponseTranslation.cancelBtn(), 0);

		logger.info("Click on Reset button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.resetBtn()));
		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);
		String alert = driver.switchTo().alert().getText();

		logger.info("Accept the Alert " + alert);
		driver.switchTo().alert().accept();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_LOOKUP_ELIG_SVC_ABBREV))));

		logger.info("Verifying eligibility there is still 1 translation Rule is in DB, for pyrAbbrev= " + pyrAbbrev);
		pyrEligResp = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertEquals(pyrEligResp.size(), 1);
	}


	@Test(priority = 1, description = "Verify filters")
	@Parameters({ "eligSvcId", "pyrAbbrev", "newPyrAbbrev", "response", "matchType", "benefitType", "subscrIdTyp"})
	public void testXPR_1088(String eligSvcId, String pyrAbbrev, String newPyrAbbrev, String response, String matchType,
			String benefitType, String subscrIdTyp) throws Exception {

		logger.info("Testing - testXPR_1088");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);

		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);
		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Set Eligibility Translation Rule data with valid Payor ID");
		EligibilityTranslation eligibilityTranslation = createEligTranslationSetupDataSubOverride(eligSvcId, pyrAbbrev, newPyrAbbrev, response, matchType,benefitType, null, false, false, false,subscrIdTyp);

		logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);

		logger.info("Enter New Payor Id");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getNewPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);
		eligibilityResponseTranslation.setMatchTypeDropDown(eligibilityTranslation.getMatchType(), wait);
		eligibilityResponseTranslation.setBenefitTypeDropDown(eligibilityTranslation.getBenefitType(), wait);
		eligibilityResponseTranslation.setSubIdTypeDropDownList(eligibilityTranslation.getSubIdType(), wait);

		logger.info("Get New Payor Name");
		Pyr newPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrev);
		String newPyrName = newPyr.getName();

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Click save and Clear");
		eligibilityResponseTranslation.clickSaveAndClear(wait);

		logger.info("Verifying eligibility translation Rule is saved in DB, for pyrAbbrev= " + pyrAbbrev);
		List<EligResp> eligResps = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertEquals(eligResps.size(), 1);

		logger.info("Reload XIFIN Eligibility Service ID and Pyr Abbrev");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);

		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Verify that Pyr translation record is displayed");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligibilityResponseTranslationTbl()));

		logger.info("Enter New Payor ID in Eligibility Response Translation grid Payor ID filter");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.newPayorIdFilterInputInEligResponseTranslationTbl()));
		setInputValue(eligibilityResponseTranslation.newPayorIdFilterInputInEligResponseTranslationTbl(), newPyrAbbrev);

		logger.info("Verify that the search values are filtered properly in the Eligibility Response Translation grid");
		Assert.assertTrue(isElementPresent(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), 10),"Eligibility Response Translation grid should be displayed.");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), newPyrAbbrev),"New Payor ID " + newPyrAbbrev + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), newPyrName),"New Payor Name " + newPyrName + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), response),"Response " + response + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),matchType),"Match Type " + matchType + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),benefitType),"Benefit Type " + benefitType + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),subscrIdTyp),"Subs Override Type " + subscrIdTyp + " should show in the Eligibility Response Translation grid");

		setClearInputValue(eligibilityResponseTranslation.newPayorIdFilterInputInEligResponseTranslationTbl(), 0);

		logger.info("Enter Response in Eligibility Response Translation grid Payor ID filter");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.responseFilterInputInEligResponseTranslationTbl()));
		setInputValue(eligibilityResponseTranslation.responseFilterInputInEligResponseTranslationTbl(), response);

		logger.info("Verify that the search values are filtered properly in the Eligibility Response Translation grid");
		Assert.assertTrue(isElementPresent(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), 10),"Eligibility Response Translation grid should be displayed.");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), newPyrAbbrev),"New Payor ID " + newPyrAbbrev + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), newPyrName),"New Payor Name " + newPyrName + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), response),"Response " + response + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),matchType),"Match Type " + matchType + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),benefitType),"Benefit Type " + benefitType + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),subscrIdTyp),"Subs Override Type " + subscrIdTyp + " should show in the Eligibility Response Translation grid");

		setClearInputValue(eligibilityResponseTranslation.responseFilterInputInEligResponseTranslationTbl(), 0);

		logger.info("Enter Benefit Type and Match Type in Eligibility Response Translation grid Payor ID filter");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.matchTypeFilterInputInEligResponseTranslationTbl()));
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.benefitTypeFilterInputInEligResponseTranslationTbl()));
		setInputValue(eligibilityResponseTranslation.matchTypeFilterInputInEligResponseTranslationTbl(), matchType);
		setInputValue(eligibilityResponseTranslation.benefitTypeFilterInputInEligResponseTranslationTbl(), benefitType);
		Assert.assertTrue(isElementPresent(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), 10),"Eligibility Response Translation grid should be displayed.");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), newPyrAbbrev),"New Payor ID " + newPyrAbbrev + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), newPyrName),"New Payor Name " + newPyrName + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), response),"Response " + response + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),matchType),"Match Type " + matchType + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),benefitType),"Benefit Type " + benefitType + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),subscrIdTyp),"Subs Override Type " + subscrIdTyp + " should show in the Eligibility Response Translation grid");

		setClearInputValue(eligibilityResponseTranslation.responseFilterInputInEligResponseTranslationTbl(), 0);

		logger.info("Enter Subs Override Type in Eligibility Response Translation grid Payor ID filter");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.matchTypeFilterInputInEligResponseTranslationTbl()));
		setInputValue(eligibilityResponseTranslation.subIdTypeFilterInputInEligResponseTranslationTbl(), subscrIdTyp);
		Assert.assertTrue(isElementPresent(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), 10),"Eligibility Response Translation grid should be displayed.");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), newPyrAbbrev),"New Payor ID " + newPyrAbbrev + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), newPyrName),"New Payor Name " + newPyrName + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(), response),"Response " + response + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),matchType),"Match Type " + matchType + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),benefitType),"Benefit Type " + benefitType + " should show in the Eligibility Response Translation grid");
		Assert.assertTrue(getColumnValue(eligibilityResponseTranslation.eligibilityResponseTranslationTbl(),subscrIdTyp),"Subs Override Type " + subscrIdTyp + " should show in the Eligibility Response Translation grid");

		logger.info("Click on Reset button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.resetBtn()));
		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);

	}

	@Test(priority = 1, description = "Verify that Loading particular Elig Service and Payor Id locks XIFIN Eligibility Config jsp for Elig Svc and Pyr")
	@Parameters({ "eligSvcId", "pyrAbbrev", "response" })
	public void testXPR_1089(String eligSvcId, String pyrAbbrev, String response) throws Exception {

		logger.info("Testing - testXPR_1089");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);
		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);
		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		Thread.sleep(TimeUnit.SECONDS.toMillis(120));
		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_LOOKUP_ELIG_SVC_ABBREV))));
	}

	@Test(priority = 1, description = "Verify that (Error Msg) if same Elig Service and Payor Id entered in the previous TC")
	@Parameters({ "eligSvcId", "pyrAbbrev", "response" })
	public void testXPR_1090(String eligSvcId, String pyrAbbrev, String response) throws Exception {

		logger.info("Testing - testXPR_1090");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);
		logger.info("Waiting for the 1 test case to lock the screeen");
		Thread.sleep(TimeUnit.SECONDS.toMillis(60));

		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);
		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));


		logger.info(eligibilityResponseTranslation.dataLockError().getText());
		Assert.assertTrue(eligibilityResponseTranslation.dataLockError().isDisplayed());
		Assert.assertTrue(eligibilityResponseTranslation.dataLockError().getText().startsWith("Data locked - Unable to obtain lock on data"));

		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_LOOKUP_ELIG_SVC_ABBREV))));

	}

	@Test(priority = 1, description = "Verify Eligibility Services Id = HUMANA, Add new Pyr Translation Rule")
	@Parameters({ "eligSvcId", "pyrAbbrev", "response" })
	public void testXPR_1091(String eligSvcId, String pyrAbbrev, String response) throws Exception {
		logger.info("Testing - testXPR_1091");
		cleanEligRespByResponse(response);

		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);
		eligibilityUtils = new EligibilityUtils(rpmDao, testDb);
		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);

		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen");
		Assert.assertTrue(eligibilityResponseTranslation.eligSVCid().getAttribute("value").equalsIgnoreCase(eligSvcId),"Eligibility Service ID " + eligSvcId + " should be displayed.");
		EligibilityTranslation eligibilityTranslation = createEligTranslationSetupDataNoSubOverride(eligSvcId, pyrAbbrev, pyrAbbrev, response, null, null,null, false, false, false);

		logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);

		logger.info("Enter New Payor Id and Response");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Click save and Clear");
		eligibilityResponseTranslation.clickSaveAndClear(wait);

		EligResp expectedEligResp = eligibilityUtils.mapToEligResp(eligibilityTranslation);
		expectedEligResp.setAudUser(null);
		expectedEligResp.audDt = null;
		expectedEligResp.audRecId = 0;
		expectedEligResp.setResultCode(0);

		logger.info("Reload XIFIN Eligibility Service ID and Pyr Abbrev");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);

		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Make sure Record is displayed in the table");
		WebElement pyrTransRuleRow =driver.findElement(By.xpath("//table[@id='tbl_eligibilityResponseTranslation']/tbody/tr/td[contains(., '"+ response + "')]"));
		wait.until(ExpectedConditions.visibilityOf(pyrTransRuleRow));

		logger.info("Verifying eligibility translation Rule is saved in DB, for pyrAbbrev= " + pyrAbbrev);
		List<EligResp> eligResp = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertEquals(eligResp.size(), 1);
		logger.info("Verifying eligibility translation Rule is saved in DB, for pyrAbbrev= " + pyrAbbrev);
		Assert.assertFalse(eligResp.get(0).getIsAllowSecondary());
		Assert.assertFalse(eligResp.get(0).getIsCheckElig());
		Assert.assertFalse(eligResp.get(0).getIsAllowUnmapped());

		logger.info("Verifying eligibility translation Rule saved to DB");

		Assert.assertEquals(eligResp.get(0).getPrio(), 1);
		EligResp actualEligResp = new EligResp(eligResp.get(0));
		actualEligResp.setAudUser(null);
		actualEligResp.audDt = null;
		actualEligResp.audRecId = 0;
		actualEligResp.setResultCode(0);
		actualEligResp.isDirty = true;
		actualEligResp.setEligRespId(0);

		logger.info("expectedEligResp " + expectedEligResp + " actualEligResp " + actualEligResp);
		Assert.assertEquals(actualEligResp, expectedEligResp);
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.resetBtn()));
		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_LOOKUP_ELIG_SVC_ABBREV))));

	}

	@Test(priority = 1, description = "Add Payor Translation Rule fot XIFIN elig svc with Subs Override Type")
	@Parameters({ "eligSvcId", "pyrAbbrev", "newPyrAbbrev", "response", "matchType","benefitType", "subscrIdTyp" })
	public void testXPR_1589(String eligSvcId, String pyrAbbrev, String newPyrAbbrev, String response, String matchType, String benefitType, String subscrIdTyp) throws Exception {

		logger.info("Testing - testXPR_1589");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);
		eligibilityUtils = new EligibilityUtils(rpmDao, testDb);
		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);

		logger.info("Enter Pyr Abbrev: "+pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Edit New Pyr Record, Match Type, Benefit Type, Response and save changes");
		EligibilityTranslation eligibilityTranslation = createEligTranslationSetupDataSubOverride(eligSvcId, pyrAbbrev, newPyrAbbrev, response,matchType, benefitType, null, true, true, true, subscrIdTyp);

		logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);
		logger.info("Enter New Payor Id");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getNewPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);
		eligibilityResponseTranslation.setBenefitTypeDropDown(eligibilityTranslation.getBenefitType(), wait);
		eligibilityResponseTranslation.setMatchTypeDropDown(eligibilityTranslation.getMatchType(), wait);
		eligibilityResponseTranslation.setSubIdTypeDropDownList(eligibilityTranslation.getSubIdType(), wait);
		checkNewPyrTranslationRuleCheckBoxes(eligibilityTranslation);

		EligResp expectedEligResp = eligibilityUtils.mapToEligResp(eligibilityTranslation);
		expectedEligResp.setAudUser(null);

		expectedEligResp.audDt = null;
		expectedEligResp.audRecId = 0;
		expectedEligResp.setResultCode(0);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Click save and Clear");
		eligibilityResponseTranslation.clickSaveAndClear(wait);

		logger.info("Verifying eligibility translation Rule is modified in DB, for pyrAbbrev= " + pyrAbbrev);
		List <EligResp> eligRespSaved = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertEquals(eligRespSaved.size(), 1);

		logger.info("Make sure eligibility translation Rule data is saved in DB, for pyrAbbrev= " + pyrAbbrev);
		EligResp actualEligResp = new EligResp(eligRespSaved.get(0));
		actualEligResp.setAudUser(null);
		actualEligResp.audDt = null;
		actualEligResp.audRecId = 0;
		actualEligResp.setResultCode(0);
		actualEligResp.setEligRespId(0);

		EligRespSubsRefTyp eligRespSubsRefTyp=rpmDao.getEligRespSubsRefTyByDesrc(testDb, subscrIdTyp);
		logger.info("Verifying Subs Override Type " + eligRespSubsRefTyp.getTypId());
		Assert.assertEquals(actualEligResp.getEligRespSubsRefTypId(), eligRespSubsRefTyp.getTypId());
		logger.info("expectedEligResp " + actualEligResp + " eligResp " + expectedEligResp);
		Assert.assertEquals(actualEligResp, expectedEligResp);
	}

	@Test(priority = 1, description = "Error message: 'Cannot select Subscriber ID Type without Match Type ID' on Eligibility Response Translation jsp")
	@Parameters({ "eligSvcId", "pyrAbbrev", "response" ,"subscrIdTyp"})
	public void testXPR_1612(String eligSvcId, String pyrAbbrev, String response, String subscrIdTyp) throws Exception
	{
		logger.info("Testing - testXPR_1612");
		cleanEligRespByResponse(response);
		eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);

		logger.info("Enter XIFIN Eligibility Service ID");
		eligibilityResponseTranslation.setEligSvcIDInLoadPage(eligSvcId, wait);
		logger.info("Enter Pyr Abbrev: " + pyrAbbrev);
		eligibilityResponseTranslation.setPayorAbbrevInLoadPage(pyrAbbrev, wait);

		logger.info("Verify that Eligibility Service is loaded");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.eligSvcIDInput()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_modal_progress_message")));

		logger.info("Set Eligibility Translation Rule data with valid Payor ID");
		EligibilityTranslation eligibilityTranslation = createEligTranslationSetupDataSubOverride(eligSvcId, pyrAbbrev, pyrAbbrev, response, null, null, null, false, false, false, subscrIdTyp);

		logger.info("Click Add a new row in Payor Translation grid with valid Payor ID");
		eligibilityResponseTranslation.clickAddResponseTranslationButton(wait);

		logger.info("Enter New Payor Id");
		eligibilityResponseTranslation.setNewPayorId(eligibilityTranslation.getPyrId(), wait);
		eligibilityResponseTranslation.setResponse(eligibilityTranslation.getResponse(), wait);
		eligibilityResponseTranslation.setSubIdTypeDropDownList(eligibilityTranslation.getSubIdType(), wait);

		logger.info("Click OK button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.oKBtn()));
		eligibilityResponseTranslation.oKBtn().click();

		logger.info("Verify that error message 'Cannot select Subscriber ID Type without Match Type ID' is displayed. Click Cancel button.");
		wait.until(ExpectedConditions.visibilityOf(eligibilityResponseTranslation.errMsgInAddRecordPopup()));
		Assert.assertEquals(eligibilityResponseTranslation.errMsgInAddRecordPopup().getText(),"Cannot select Subscriber ID Type without Match Type ID.","Error message 'Cannot select Subscriber ID Type without Match Type ID' should be displayed.");
		clickHiddenPageObject(eligibilityResponseTranslation.cancelBtn(), 0);

		logger.info("Click on Reset button");
		wait.until(ExpectedConditions.elementToBeClickable(eligibilityResponseTranslation.resetBtn()));
		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);
		String alert = driver.switchTo().alert().getText();
		logger.info("Accept the Alert " + alert);
		driver.switchTo().alert().accept();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_LOOKUP_ELIG_SVC_ABBREV))));

		logger.info("Verifying eligibility translation Rule is NOT saved in DB, for pyrAbbrev= " + pyrAbbrev);
		List<EligResp> eligResps = rpmDao.getEligRespByResponse(testDb, response);
		Assert.assertTrue(eligResps.isEmpty());

		logger.info("Click on Reset button");
		wait.until(ExpectedConditions.elementToBeClickable(By.id(ID_LOOKUP_ELIG_SVC_ABBREV)));
		clickHiddenPageObject(eligibilityResponseTranslation.resetBtn(), 0);

	}

	public void checkNewPyrTranslationRuleCheckBoxes(EligibilityTranslation eligibilityTranslation) throws Exception {
		if (eligibilityTranslation.getIsAllowSecondaryTranslation()) {
			eligibilityResponseTranslation.checkAllowSecondaryTranslationCheckBox();
		}
		if (eligibilityTranslation.getIsBypassUnknownResponseErrors()) {
			eligibilityResponseTranslation.checkBypassUnknownResponseErrorsCheckBox();
		}
		if (eligibilityTranslation.getIsCheckElig()) {
			eligibilityResponseTranslation.checkCheckEligCheckBox();
		}
	}
	protected void cleanEligRespByResponse(String response) throws Exception {
		logger.info("Deleting PyrEligResp records, response=" + response);
		rpmDao.deleteEligRespByResp(testDb, response);

	}

	public EligibilityTranslation createEligTranslationSetupDataSubOverride(String eligSvcId, String pyrAbbrev,
			String newPyrAbbrev, String response, String matchType, String benefitType, String serviceTypeCode,
			boolean isAllowSecondaryTranslation, boolean isBypassUnknownResponseErrors, boolean isCheckElig, String respSubTyp) {
		EligibilityTranslation eligibilityTranslation = new EligibilityTranslation();
		eligibilityTranslation.setResponse(response);
		eligibilityTranslation.setMatchType(matchType);
		eligibilityTranslation.setBenefitType(benefitType);
		eligibilityTranslation.setServiceTypeCode(serviceTypeCode);
		eligibilityTranslation.setIsAllowSecondaryTranslation(isAllowSecondaryTranslation);
		eligibilityTranslation.setIsBypassUnknownResponseErrors(isBypassUnknownResponseErrors);
		eligibilityTranslation.setIsCheckElig(isCheckElig);
		eligibilityTranslation.setEligSvcId(eligSvcId);
		eligibilityTranslation.setPrio(1);
		eligibilityTranslation.setPyrId(pyrAbbrev);
		eligibilityTranslation.setNewPyrId(newPyrAbbrev);
		eligibilityTranslation.setSubIdType(respSubTyp);
		return eligibilityTranslation;
	}
	public EligibilityTranslation createEligTranslationSetupDataNoSubOverride(String eligSvcId, String pyrAbbrev,
																 String newPyrAbbrev, String response, String matchType, String benefitType, String serviceTypeCode,
																 boolean isAllowSecondaryTranslation, boolean isBypassUnknownResponseErrors, boolean isCheckElig) {
		EligibilityTranslation eligibilityTranslation = new EligibilityTranslation();
		eligibilityTranslation.setResponse(response);
		eligibilityTranslation.setMatchType(matchType);
		eligibilityTranslation.setBenefitType(benefitType);
		eligibilityTranslation.setServiceTypeCode(serviceTypeCode);
		eligibilityTranslation.setIsAllowSecondaryTranslation(isAllowSecondaryTranslation);
		eligibilityTranslation.setIsBypassUnknownResponseErrors(isBypassUnknownResponseErrors);
		eligibilityTranslation.setIsCheckElig(isCheckElig);
		eligibilityTranslation.setEligSvcId(eligSvcId);
		eligibilityTranslation.setPrio(1);
		eligibilityTranslation.setPyrId(pyrAbbrev);
		eligibilityTranslation.setNewPyrId(newPyrAbbrev);

		return eligibilityTranslation;
	}
	public void switchToParentWindow(String currentWindow) {
		driver.switchTo().window(currentWindow);
		//driver.switchTo().frame(driver.findElement(By.id("content")));
       // driver.switchTo().frame(driver.findElement(By.id("platformiframe")));
	}
}
