package com.newXp.tests;

import com.mbasys.mars.ejb.entity.eligFollowUpActnTyp.EligFollowUpActnTyp;
import com.mbasys.mars.ejb.entity.eligFollowUpValidation.EligFollowUpValidation;
import com.mbasys.mars.ejb.entity.eligRejectRsnTyp.EligRejectRsnTyp;
import com.mbasys.mars.ejb.entity.eligRejectValidation.EligRejectValidation;
import com.mbasys.mars.ejb.entity.eligSvc.EligSvc;
import com.mbasys.mars.ejb.entity.eligValidationActnTyp.EligValidationActnTyp;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrElig.PyrElig;
import com.mbasys.mars.eligibility.EligMap;
import com.overall.fileMaintenance.fileMaintenanceTables.EligibilityConfiguration;
import com.overall.menu.MenuNavigation;
import com.overall.search.EligibilityServiceSearchResults;
import com.overall.search.EligibilitySubServiceSearchResults;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class SmokeEligibilityConfigurationTest extends SeleniumBaseTest
{
	private RandomCharacter randomCharacter;
	private EligibilityServiceSearchResults eligibilityServiceSearchResults;
	private EligibilitySubServiceSearchResults eligibilitySubServiceSearchResults;
	private EligibilityConfiguration eligibilityConfiguration;

	@BeforeMethod(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins",})
	public void beforeMethod(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String disableBrowserPlugins, Method method)
	{
		try
		{
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			logIntoSso(ssoUsername, ssoPassword);
			MenuNavigation navigation = new MenuNavigation(driver, config);
			navigation.navigateToEligibilityConfigurationPage();
		}
		catch (Exception e)
		{
			logger.error("Error running BeforeMethod", e);
		}
	}

	@Test(priority = 1, description = "Add non-XIFIN Eligibility Service with Payor Setup")
	public void testXPR_781() throws Exception {		
		logger.info("Testing - testXPR_781");    
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);

		String eligSvcId = "AutotestXPR781";
		String eligSvcName = eligSvcId+"Name";
		cleanUp(eligSvcId);

		logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");		
	
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5),"Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(),"Eligibility Configuration","Eligibility Service screen should be displayed.");
		
		logger.info("Enter a new Non-XIFIN Eligibility Service ID");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInputInLoadPage(),5),"Eligibility Service ID input field should be displayed.");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);
		
		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen. All fields are empty");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), " Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.loginIDInput(),5),"Login ID field should be displayed.");
		assertEquals(eligibilityConfiguration.loginIDInput().getText(),"","Login ID field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.classNameInput(),5), "ClassName field should be displayed.");
		assertEquals(eligibilityConfiguration.classNameInput().getText(),"","ClassName field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.nameInput(),5), "Name field should be displayed.");
		assertEquals(eligibilityConfiguration.nameInput().getText(),"","Name field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.passwordIDInput(),5),"Password field should be displayed.");
		assertEquals(eligibilityConfiguration.passwordIDInput().getText(),"","Password field should be empty.");
		
		logger.info("Enter valid data in Name, Login ID, Password, Classname, Exception Alert Threshold, Server Delay, Server Timeout fields");
		String loginId = randomCharacter.getRandomAlphaString(5);
		String passWd = loginId;
		String className = "com.mbasys.mars.dataService.eligibility.XifinEligibilityProvider";
		
		eligibilityConfiguration.enterEligibilityServiceInfos(loginId, className, eligSvcName, passWd, "5", "5", "5");
		
		logger.info("Add a new row in Payor Setup grid with valid Payor ID");
		List<Pyr> pyrInfoList = payorDao.get3rdPartyPyrNotInPyrElig();
		String pyrAbbrev = pyrInfoList.get(0).getPyrAbbrv();
		String pyrId = String.valueOf(pyrInfoList.get(0).getPyrId());
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblAddBtn(),5),"Add button icon should be displayed in Payor Setup table.");
		clickHiddenPageObject(eligibilityConfiguration.payorSetupTblAddBtn(),0);
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorIdInputInPopup(),5), "Payor ID field should be displayed in Add Record popup window.");
		eligibilityConfiguration.setPayorIdInPopup(pyrAbbrev);
		
		assertTrue(isElementPresent(eligibilityConfiguration.daysToCheckEligInput(),5),"Days to Check Elig Input field should be displayed.");
		eligibilityConfiguration.setDaysToCheckElig("10");
		
		assertTrue(isElementPresent(eligibilityConfiguration.oKBtn(),5),"OK button should be displayed in Add Record popup.");
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(),0);
		
		logger.info("Verify that a new row is added to the Payor Setup grid with correct data");				
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblCelData(2, 3),5),"Payor ID should be displayed.");
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev),"Payor ID " + pyrAbbrev + " should be displayed in the Payor Setup.");
				
		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));

		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
		
		logger.info("Verify that new records are saved properly in ELIG_SVC, PYR_ELIG and ELIG_RESP tables");		
		List<List<String>> eligSvcInfo = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("ELIG_SVC_NAME = '"+eligSvcName+"' and DESCR = '"+eligSvcId.toUpperCase()+"'", testDb);		
		List<List<String>> pyrEligInfo = daoManagerXifinRpm.getPyrEligInfoFromPYRELIGByConditions("PK_ELIG_SVC_ID = "+eligSvcInfo.get(0).get(4)+"", testDb);

		assertEquals(eligSvcInfo.get(0).get(0),eligSvcName, "New record should be added in Elig_Svc table with correct Elig Service Name.");
		assertEquals(eligSvcInfo.get(0).get(1),eligSvcId.toUpperCase(), "New record should be added in Elig_Svc table with correct Elig Service ID.");
		assertEquals(eligSvcInfo.get(0).get(2),loginId, "New record should be added in Elig_Svc table with correct Elig Service LoginID.");
		assertEquals(eligSvcInfo.get(0).get(3),className, "New record should be added in Elig_Svc table with correct Elig Service ClassName.");
		
		assertEquals(pyrEligInfo.get(0).get(0),pyrId, "Payor Record should be saved in Pyr_Elig table.");
	}

	@Test(priority = 1, description = "Add XIFIN Eligibility Service with FollowUp Action and Reject Reason Code Override")
	public void testXPR_786() throws Exception {		
		logger.info("===== Testing - testXPR_786 =====");
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);

		String eligSvcId = "AutotestXPR786";
		String name = "XIFIN";
		cleanUp(eligSvcId);logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");	
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5),"Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(),"Eligibility Configuration"," Eligibility Service screen should be displayed.");
		
		logger.info("Enter a new XIFIN Eligibility Service ID");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInputInLoadPage(),5),"Eligibility Service ID input field should be displayed.");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);
		
		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen; All fields are empty");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.loginIDInput(),5),"Login ID field should be displayed.");
		assertEquals(eligibilityConfiguration.loginIDInput().getText(),"","Login ID field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.classNameInput(),5), "ClassName field should be displayed.");
		assertEquals(eligibilityConfiguration.classNameInput().getText(),"","ClassName field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.nameInput(),5), "Name field should be displayed.");
		assertEquals(eligibilityConfiguration.nameInput().getText(),"","Name field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.passwordIDInput(),5),"Password field should be displayed.");
		assertEquals(eligibilityConfiguration.passwordIDInput().getText(),"","Password field should be empty.");
		
		logger.info("*** Step 3 Actions: - Enter valid data in Name, Login ID, Password, Classname, Exception Alert Threshold, Server Delay, Server Timeout fields");
		String loginId = randomCharacter.getRandomAlphaString(5);
		String passWd = loginId;
		String className = "com.mbasys.mars.dataService.eligibility.XifinEligibilityProvider";
		
		eligibilityConfiguration.enterEligibilityServiceInfos(loginId, className, name, passWd, "5", "5", "5");
		
		logger.info("*** Step 4 Actions: - Add a new row in Payor Setup grid with valid Payor ID");
		List<Pyr> pyrInfoList = payorDao.get3rdPartyPyrNotInPyrElig();
		String pyrAbbrev = pyrInfoList.get(0).getPyrAbbrv();
		String pyrId = String.valueOf(pyrInfoList.get(0).getPyrId());
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblAddBtn(),5),"Add button icon should be displayed in Payor Setup table.");
		clickHiddenPageObject(eligibilityConfiguration.payorSetupTblAddBtn(),0);
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorIdInputInPopup(),5), "Payor ID field should be displayed in Add Record popup window.");
		eligibilityConfiguration.setPayorIdInPopup(pyrAbbrev);
		
		assertTrue(isElementPresent(eligibilityConfiguration.daysToCheckEligInput(),5),"Days to Check Elig Input field should be displayed.");
		eligibilityConfiguration.setDaysToCheckElig("10");
		
		assertTrue(isElementPresent(eligibilityConfiguration.oKBtn(),5),"OK button should be displayed in Add Record popup.");
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(),0);
		
		logger.info("Verify that a new row is added to the Payor Setup grid with correct data");				
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblCelData(2, 3),5),"Payor ID should be displayed.");
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev),"Payor ID " + pyrAbbrev + " should be displayed in the Payor Setup.");
		
		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
		
		logger.info("Reload the Eligibility Service ID just created");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);		
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		logger.info("Add a new row in Validation Follow-Up Action Code Configuration grid");
		List<EligFollowUpActnTyp> eligFollowUpActnTypList = rpmDao.getEligFollowUpActnTyp(testDb);
		List<EligValidationActnTyp> eligValidationActnTypList = rpmDao.getEligValidationActnTyp(testDb);		
		
		assertTrue(isElementPresent(eligibilityConfiguration.addBtnValidationFollowUpActionCodeConfigTbl(),5),"Add button should be displayed in Validation Follow-Up Action Code Configuration grid.");
		clickHiddenPageObject(eligibilityConfiguration.addBtnValidationFollowUpActionCodeConfigTbl(),0);
		
		assertTrue(isElementPresent(eligibilityConfiguration.followUpActionCodeDropDown(),5), "Follow-Up Action dropdown should be displayed.");
		String followUpActnCodeStr = eligFollowUpActnTypList.get(0).getAbbrev().trim() + " - " + eligFollowUpActnTypList.get(0).getDescr().trim();
		eligibilityConfiguration.setFollowUpActionCode(followUpActnCodeStr);
		
		assertTrue(isElementPresent(eligibilityConfiguration.actionDropDown(),5), "Action dropdown should be displayed.");
		String actionStr = eligValidationActnTypList.get(0).getAbbrev().trim() + " - " + eligValidationActnTypList.get(0).getDescr().trim();
		eligibilityConfiguration.setAction(actionStr);
		
		assertTrue(isElementPresent(eligibilityConfiguration.oKBtn(),5),"OK Button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(),0);
		
		logger.info("Verify that a new row is added in the Validation Follow-Up Action Code Configuration grid");
		assertTrue(isElementPresent(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3),5),"Follow-Up Action Code should be displayed.");
		assertTrue(isElementPresent(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4),5),"Action should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3).getText(),followUpActnCodeStr, "Follow-Up Action Code " + followUpActnCodeStr + " should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4).getText(),actionStr, "Action " + actionStr + " should be displayed.");
		
		logger.info("Add a new row in Validation Reject Reason Code Override Configuration grid");
		scrollToElement(eligibilityConfiguration.valRejectResonTitle());
		List<EligRejectRsnTyp> eligRejectRsnTypList = rpmDao.getEligRejectRsnTyp(testDb);		
		
		String rejectReasonCodeStr = eligRejectRsnTypList.get(0).getAbbrev().trim() + " - " + eligRejectRsnTypList.get(0).getDescr().trim();
		String OverrideActionStr = eligValidationActnTypList.get(1).getAbbrev().trim() + " - " + eligValidationActnTypList.get(1).getDescr().trim();
		
		eligibilityConfiguration.setDataInValidationRejectReasonCodeOverrideConfig(rejectReasonCodeStr, OverrideActionStr);
		
		logger.info("Verify that a new row is added in Validation Reject Reason Code Override Configuration grid");
		assertEquals(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(),rejectReasonCodeStr), true,"Reject Reason Coe " + rejectReasonCodeStr + " should be added to table.");
				
		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
		
		logger.info("Verify that new records are saved properly in ELIG_SVC, PYR_ELIG and ELIG_RESP tables in DB");		
		List<List<String>> eligSvcInfo = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("ELIG_SVC_NAME = '"+name+"' and DESCR = '"+eligSvcId.toUpperCase()+"'", testDb);		
		List<List<String>> pyrEligInfo = daoManagerXifinRpm.getPyrEligInfoFromPYRELIGByConditions("PK_ELIG_SVC_ID = "+eligSvcInfo.get(0).get(4)+"", testDb);

		assertEquals(eligSvcInfo.get(0).get(0),name, "New record should be added in Elig_Svc table with correct Elig Service Name.");
		assertEquals(eligSvcInfo.get(0).get(1),eligSvcId.toUpperCase(), "New record should be added in Elig_Svc table with correct Elig Service ID.");
		assertEquals(eligSvcInfo.get(0).get(2),loginId, "New record should be added in Elig_Svc table with correct Elig Service LoginID.");
		assertEquals(eligSvcInfo.get(0).get(3),className, "New record should be added in Elig_Svc table with correct Elig Service ClassName.");
		
		assertEquals(pyrEligInfo.get(0).get(0),pyrId, "Payor Record should be saved in Pyr_Elig table.");
		
		logger.info("Verify that new records are saved properly in ELIG_REJECT_VALIDATION and ELIG_FOLLOW_UP_VALIDATION tables in DB");
		String eligSvcSeqId = eligSvcInfo.get(0).get(4);
		String rejectRsnCodeId = String.valueOf(eligRejectRsnTypList.get(0).getEligRejectRsnTypId());
		String validationActnTypId = String.valueOf(eligValidationActnTypList.get(1).getEligValidationActnTypId());
		List<EligRejectValidation> eligRejectValidationList = rpmDao.getEligRejectValidationByEligSvcIdRejectRsnTypIdValidationActnId(testDb, eligSvcSeqId, rejectRsnCodeId, validationActnTypId);
		assertTrue(eligRejectValidationList.size() > 0, "A new record should be added in ELIG_REJECT_VALIDATION table.");
		
		String followUpActnTypId = String.valueOf(eligFollowUpActnTypList.get(0).getEligFollowUpActnTypId());
		validationActnTypId = String.valueOf(eligValidationActnTypList.get(0).getEligValidationActnTypId());
		List<EligFollowUpValidation> eligFollowUpValidationList = rpmDao.getEligFollowUpValidationByEligSvcIdFollowUpActnTypIdValidationActnTypId(testDb, eligSvcSeqId, followUpActnTypId, validationActnTypId);
		assertTrue(eligFollowUpValidationList.size() > 0, "A new record should be added in ELIG_FOLLOW_UP_VALIDATION table.");
		
		logger.info("Reload the same Eligibility Service ID");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);		
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
				
		logger.info("Verify that a new row is added to the Payor Setup grid with correct data");			
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev),"Payor ID " + pyrAbbrev + " should be displayed in the Payor Setup.");
				
		logger.info("Verify that a new row is added in the Validation Follow-Up Action Code Configuration grid");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3).getText(),followUpActnCodeStr, "Follow-Up Action Code " + followUpActnCodeStr + " should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4).getText(),actionStr, "Action " + actionStr + " should be displayed.");
		
		logger.info("Verify that a new row is added in Validation Reject Reason Code Override Configuration grid");
		assertEquals(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(),rejectReasonCodeStr), true,"Reject Reason Code " + rejectReasonCodeStr + " should be added to table.");
	}

	@Test(priority = 1, description = "Update XIFIN Eligibility Service and ensure default/additional service types codes are not cleared out")
	@Parameters({"pyrAbbrev"})
	public void updateXifinEligibilityService(String pyrAbbrev) throws Exception {
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);

		String eligSvcId = "XIFIN";

		Pyr pyr = payorDao.getPyrByPyrAbbrv(pyrAbbrev);
		List<PyrElig> origPyrEligs = rpmDao.getPyrElig(null, pyr.getPyrId());
		Assert.assertEquals(origPyrEligs.size(), 1, "Expected 1 PyrElig record, pyrAbbrev="+pyrAbbrev+", pyrId="+pyr.getPyrId()+", pyrEligCnt="+origPyrEligs.size());
		Assert.assertNotNull(origPyrEligs.get(0).getDefaultSvcTypCd(), "Expected default service type code to be not null, pyrAbbrev="+pyrAbbrev+", pyrId="+pyr.getPyrId()+", defaultSvcTypCd="+origPyrEligs.get(0).getDefaultSvcTypCd());
		Assert.assertNotNull(origPyrEligs.get(0).getAddtlSvcTyps(), "Expected additional service types to be not null, pyrAbbrev="+pyrAbbrev+", pyrId="+pyr.getPyrId()+", defaultSvcTypCd="+origPyrEligs.get(0).getAddtlSvcTyps());

		logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5),"Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(),"Eligibility Configuration"," Eligibility Service screen should be displayed.");

		logger.info("Entering eligibility service ID, eligSvcId="+eligSvcId);
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInputInLoadPage(),5),"Eligibility Service ID input field should be displayed.");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);

		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");

		logger.info("Enter Payor ID in Payor Setup grid Payor ID filter");
		assertTrue(isElementPresent(eligibilityConfiguration.payorIdFilterInputInPayorSetupTbl(), 5)," Payor ID filter should be displayed.");
		eligibilityConfiguration.setPayorIdFilterInPayorSetupTbl(pyrAbbrev);
		setInputValue(eligibilityConfiguration.payorIdFilterInputInPayorSetupTbl(), pyrAbbrev);
		Thread.sleep(3000);

		boolean translationEnabledOrig = eligibilityConfiguration.translationEnabledCheckboxInputValue();
		Assert.assertEquals(translationEnabledOrig, origPyrEligs.get(0).getIsTranslationEnabled(), "Expected translation enabled checkbox to match DB value, pyrAbbrev="+pyrAbbrev+", pyrId="+pyr.getPyrId()+", translationEnabledDbValue="+origPyrEligs.get(0).isTranslationEnabled+", translationEnabledCheck");
		logger.info("Clicking translation enabled checkbox, pyrAbbrev="+pyrAbbrev+", translationEnabledOrigValue="+translationEnabledOrig);
		eligibilityConfiguration.clickTranslationEnabledCheckboxInput();
		boolean translationEnabledNew = eligibilityConfiguration.translationEnabledCheckboxInputValue();
		logger.info("Clicked translation enabled checkbox, pyrAbbrev="+pyrAbbrev+", translationEnabledOrigValue="+translationEnabledOrig+", translationEnabledNewValue="+translationEnabledNew);
		Assert.assertNotEquals(translationEnabledNew, translationEnabledOrig, "Expected translation enabled values to be different, pyrAbbrev="+pyrAbbrev+", translationEnabledOrigValue="+translationEnabledOrig+", translationEnabledNewValue="+translationEnabledNew);

		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);
		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");

		logger.info("Verifying PyrElig DB updates, pyrAbbrev="+pyrAbbrev+", pyrId="+pyr.getPyrId());
		List<PyrElig> newPyrEligs = rpmDao.getPyrElig(null, pyr.getPyrId());
		Assert.assertEquals(newPyrEligs.size(), 1, "Expected 1 PyrElig record, pyrAbbrev="+pyrAbbrev+", pyrId="+pyr.getPyrId()+", pyrEligCnt="+newPyrEligs.size());
		Assert.assertEquals(newPyrEligs.get(0).getIsTranslationEnabled(), translationEnabledNew, "Expected translation enabled value to match checkbox, pyrAbbrev="+pyrAbbrev+", pyrId="+pyr.getPyrId()+", translationEnabledDbValue="+newPyrEligs.get(0).getIsTranslationEnabled()+", translationEnabledCheckboxValue="+translationEnabledNew);
		Assert.assertEquals(newPyrEligs.get(0).getDefaultSvcTypCd(), origPyrEligs.get(0).getDefaultSvcTypCd(), "Expected new default service type code to equal original value, pyrAbbrev="+pyrAbbrev+", pyrId="+pyr.getPyrId()+", newDefaultSvcTypCd="+newPyrEligs.get(0).getDefaultSvcTypCd()+", origDefaultSvcTypCd="+origPyrEligs.get(0).getDefaultSvcTypCd());
		Assert.assertEquals(newPyrEligs.get(0).getAddtlSvcTyps(), origPyrEligs.get(0).getAddtlSvcTyps(), "Expected new additional service types to equal original value, pyrAbbrev="+pyrAbbrev+", pyrId="+pyr.getPyrId()+", newAdditionalSvcTyps="+newPyrEligs.get(0).getAddtlSvcTyps()+", origAdditionalSvcTyps="+origPyrEligs.get(0).getAddtlSvcTyps());
	}

	@Test(priority = 1, description = "Load Eligibility Services Id with Pk_Elig_Svc_id = 99999 via Search")
	public void testXPR_798() throws Exception {		
		logger.info("Testing - testXPR_798");    
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);
		eligibilityServiceSearchResults = new EligibilityServiceSearchResults(driver);
		logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5),"Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(),"Eligibility Configuration"," Eligibility Service screen should be displayed.");
		
		logger.info("Load Eligibility Service with pk_Elig_Svc_Id = 99999 via Eligibility Service Search");		
		List<List<String>> eligSvcInfoList = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("pk_Elig_Svc_Id = 99999", testDb);
		String eligSvcId = eligSvcInfoList.get(0).get(1);
		
		assertTrue(isElementPresent(eligibilityConfiguration.eligServiceIDSearchIconInLoadPage(), 5),"Search Icon in Load page show show.");
		clickHiddenPageObject(eligibilityConfiguration.eligServiceIDSearchIconInLoadPage(), 0);
		String parent = switchToPopupWin();  
		
		logger.info("Verify that Eligibility Service Search Result page is displayed");
		assertTrue(isElementPresent(eligibilityServiceSearchResults.colRowInServiceSearchResultHyberLink(2,2), 5),"Eligibility Service Search Result page is displayed.");  
		int rowNum = getRowNumberInWebTable(eligibilityServiceSearchResults.eligibilitySearchTbl(), "tbl_eligibilitySearch", eligSvcId.trim(), 2);
		
		logger.info("Select the Eligibility Service Id hyberLink in Search Results");
		clickHiddenPageObject(eligibilityServiceSearchResults.colRowInServiceSearchResultHyberLink(rowNum-1, 2), 0);
		
		switchToParentWin(parent);
		
		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen. All fields are empty.");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), " Eligibility Service ID " + eligSvcId + " should be displayed.");
				
		logger.info("Verify that message 'This record is not editable' is displayed");
		assertTrue(isElementPresent(eligibilityConfiguration.msgReturnedText(), 5),"A message is displayed.");
		assertEquals(eligibilityConfiguration.msgReturnedText().getText(), "This record is not editable.", "Eligibility Configuration screen is displayed error message 'This record is not editable'.");
		
		logger.info("Verify that the Save and Clear button is disable");		
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(), 5),"Save and clear button is displayed.");
		assertFalse(eligibilityConfiguration.saveAndClearBtn().isEnabled(),"The save and clear button is enabled.");
		
		logger.info("Click on Reset button");
		assertTrue(isElementPresent(eligibilityConfiguration.resetBtn(), 5),"Reset button is displayed.");
		clickHiddenPageObject(eligibilityConfiguration.resetBtn(), 0);
	}	
	
	@Test(priority = 1, description = "Verify Helps")
	public void testXPR_800() throws Exception {
		logger.info("===== Testing - testXPR_800 =====");
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);

		logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");			
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5),"Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(),"Eligibility Configuration","Eligibility Service screen should be displayed.");
		
		logger.info("Click Help icon button in the load page");
		eligibilityConfiguration.helpIconInLoadPage().click();
		
		logger.info("Verify that Help file can be opened properly");
		String parent = switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("p_eligibility_configuration_load_service_id.htm"),"Help file in Load page is opened.");
		assertTrue(eligibilityConfiguration.titleTextInHelp().getText().contains("Service ID Selection"), "Help file in Load page should be opened.");
		driver.close();
		switchToParentWin(parent);
		
		logger.info("Load Eligibility Service with pk_Elig_Svc_Id = 7777");		
		List<List<String>> eligSvcInfoList = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("pk_Elig_Svc_Id = 7777", testDb);
		String eligSvcId = eligSvcInfoList.get(0).get(1);
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);	
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
				
		logger.info("Click Help icon button in Header section");		
		assertTrue(isElementPresent(eligibilityConfiguration.helpIconInHeaderSection(),5),"Header menu: help icon should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.helpIconInHeaderSection(), 0);
		
		logger.info("Verify that Help page in Header section is opened properly");
		parent = switchToPopupWin();
		switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("p_eligibility_configuration_header.htm"),"Help file in Header section is opened.");
		assertTrue(eligibilityConfiguration.titleTextInHelp().getText().contains("Eligibility Configuration Header"), "Help file in Header section should be opened.");
		driver.close();
		switchToParentWin(parent);
		
		logger.info("Payor Setup section: Click help icon");		
		assertTrue(isElementPresent(eligibilityConfiguration.helpIconInPayorSetup(),5),"Payor Setup section: help icon should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.helpIconInPayorSetup(), 0);

		logger.info("Verify that Help page in Payor Setup grid is opened properly");		
		switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("p_eligibility_configuration_payor_setup.htm"),"Help page in Payor Setup grid is opened.");
		assertTrue(eligibilityConfiguration.titleTextInHelp().getText().contains("Payor Setup"), "Help file in Payor Setup grid should be opened.");
		driver.close();
		switchToParentWin(parent);
		
		logger.info("Click help icon in Validation Follow-Up Action Code Configuration grid");
		scrollToElement(eligibilityConfiguration.helpIconInValidationFollowUpAction());
		assertTrue(isElementPresent(eligibilityConfiguration.helpIconInValidationFollowUpAction(),5),"Help icon in Validation Follow-Up Action Code Configuration grid should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.helpIconInValidationFollowUpAction(), 0);
		
		logger.info("Verify that Help file in Validation Follow-Up Action Code Configuration grid is opened properly");		
		switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("p_eligibility_configuration_validation_follow_up_action_code_configuration.htm"),"Help page in Validation Follow-Up Action Code Configuration grid is opened.");
	
		logger.info(eligibilityConfiguration.titleTextInHelp().getText());
		
		assertTrue(eligibilityConfiguration.titleTextInHelp().getText().contains("Validation Follow-Up Action Code Configuration"), "Help file in Validation Follow-Up Action Code Configuration grid should be opened.");
		driver.close();
		switchToParentWin(parent);
		
		logger.info("Click help icon in Validation Reject Reason Code Override Configuration grid");
		scrollToElement(eligibilityConfiguration.helpIconInValidationRejectReasonCode());
		assertTrue(isElementPresent(eligibilityConfiguration.helpIconInValidationRejectReasonCode(),5),"Help icon should be displayed in Validation Reject Reason Code Override Configuration grid.");
		clickHiddenPageObject(eligibilityConfiguration.helpIconInValidationRejectReasonCode(), 0);
		
		logger.info("Verify that Help file is opened properly in Validation Reject Reason Code Override Configuration grid");		
		switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("p_eligibility_configuration_validation_reject_reason_code_override_configuration.htm"),"Help file is opened in Validation Reject Reason Code Override Configuration grid.");
		assertTrue(eligibilityConfiguration.titleTextInHelp().getText().contains("Validation Reject Reason Code Override Configuration"), "Help file in Validation Reject Reason Code Override Configuration grid should be opened.");
		driver.close();
		switchToParentWin(parent);		
		
		logger.info("Click help icon in page footer section");		
		scrollToElement(eligibilityConfiguration.helpIconInFooterSection());
		assertTrue(isElementPresent(eligibilityConfiguration.helpIconInFooterSection(),5),"Help icon should be displayed in page footer section.");
		clickHiddenPageObject(eligibilityConfiguration.helpIconInFooterSection(), 0);
		
		logger.info("Verify that Help file is opened properly in footer section");		
		switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("p_eligibility_configuration_summary.htm"),"Help file is opened in page footer section.");
		assertTrue(eligibilityConfiguration.titleTextInHelp().getText().contains("Eligibility Configuration Screen"), "Help file in footer section should be opened.");
		driver.close();
		switchToParentWin(parent);
		
		logger.info(" Click on Reset button");
		assertTrue(isElementPresent(eligibilityConfiguration.resetBtn(), 5),"Reset button is displayed.");
		clickHiddenPageObject(eligibilityConfiguration.resetBtn(), 0);
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Verify Run Audit button")
	public void testXPR_808() throws Exception {		
		logger.info("===== Testing - testXPR_808 =====");
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);

		String eligSvcId = "AutotestXPR808";
		String name = eligSvcId+"Name";
		cleanUp(eligSvcId);logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");		
	
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5),"Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(),"Eligibility Configuration","Eligibility Service screen should be displayed.");
		
		logger.info("Enter a new Non-XIFIN Eligibility Service ID");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInputInLoadPage(),5),"Eligibility Service ID input field should be displayed.");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);
		
		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen. All fields are empty.");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.loginIDInput(),5),"Login ID field should be displayed.");
		assertEquals(eligibilityConfiguration.loginIDInput().getText(),"","Login ID field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.classNameInput(),5), "ClassName field should be displayed.");
		assertEquals(eligibilityConfiguration.classNameInput().getText(),"","ClassName field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.nameInput(),5), "Name field should be displayed.");
		assertEquals(eligibilityConfiguration.nameInput().getText(),"","Name field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.passwordIDInput(),5),"Password field should be displayed.");
		assertEquals(eligibilityConfiguration.passwordIDInput().getText(),"","Password field should be empty.");
		
		logger.info("Enter valid data in Name, Login ID, Password, Classname, Exception Alert Threshold, Server Delay, Server Timeout fields");
		String loginId = randomCharacter.getRandomAlphaString(5);
		String passWd = loginId;
		String className = "com.mbasys.mars.dataService.eligibility.XifinEligibilityProvider";
		
		eligibilityConfiguration.enterEligibilityServiceInfos(loginId, className, name, passWd, "5", "5", "5");
		
		logger.info("Add a new row in Payor Setup grid with valid Payor ID");
		List<Pyr> pyrInfoList = payorDao.get3rdPartyPyrNotInPyrElig();
		String pyrAbbrev = pyrInfoList.get(0).getPyrAbbrv();

		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblAddBtn(),5),"Add button icon should be displayed in Payor Setup table.");
		clickHiddenPageObject(eligibilityConfiguration.payorSetupTblAddBtn(),0);
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorIdInputInPopup(),5), "Payor ID field should be displayed in Add Record popup window.");
		eligibilityConfiguration.setPayorIdInPopup(pyrAbbrev);
		
		assertTrue(isElementPresent(eligibilityConfiguration.daysToCheckEligInput(),5),"Days to Check Elig Input field should be displayed.");
		eligibilityConfiguration.setDaysToCheckElig("10");
		
		assertTrue(isElementPresent(eligibilityConfiguration.oKBtn(),5),"OK button should be displayed in Add Record popup.");
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(),0);
		
		logger.info("Verify that a new row is added to the Payor Setup grid with correct data");				
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblCelData(2, 3),5),"Payor ID should be displayed.");
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev),"Payor ID " + pyrAbbrev + " should be displayed in the Payor Setup.");
				
		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
				
		logger.info("Reload the same Eligibility Service ID");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);		
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), " Eligibility Service ID " + eligSvcId + " should be displayed.");
				
		logger.info("Click on Run Audit button");
		assertTrue(isElementPresent(eligibilityConfiguration.runAuditBtn(),5),"The Run Audit button is displayed.");
		clickHiddenPageObject(eligibilityConfiguration.runAuditBtn(), 0);
		
		logger.info("Verify that the data show properly in Audit Detail page");
		String parent = switchToPopupWin();
//		waitUntilElementPresent(eligibilityConfiguration.auditDetailTblRowCol(2,2), 20);
//		assertTrue(isElementPresent(eligibilityConfiguration.totalRecordsInAuditDetail(),15),"Total Record In Audit Detail page is displayed.");
//
//		assertTrue(eligibilityConfiguration.auditDetailTblRowCol(2,3).getText().equals("C"), "Action in Audit Detail should be 'C'.");
//		assertTrue(eligibilityConfiguration.auditDetailTblRowCol(2,4).getText().equals("ELIG_SVC"), "Table in Audit Detail should be 'ELIG_SVC'.");
		logger.info("Verify that the data show properly in Audit Detail page");
		//String parent = switchToPopupWin();
//		waitUntilElementPresent(eligibilityConfiguration.auditDetailTblRowCol(2,2), 20);
//		assertTrue(isElementPresent(eligibilityConfiguration.totalRecordsInAuditDetail(),15),"Total Record In Audit Detail page is displayed.");
//
//		assertTrue(eligibilityConfiguration.auditDetailTblRowCol(2,3).getText().equals("C"), "Action in Audit Detail should be 'C'.");
//		assertTrue(eligibilityConfiguration.auditDetailTblRowCol(2,4).getText().equals("ELIG_SVC"), "Table in Audit Detail should be 'ELIG_SVC'.");
//TODO when BI environment will be made available such that we can resume running these tests.
//		assertTrue(isElementPresent(reasonCode.auditDetailTable(), 5), "       Audit Detail table is shown.");
		logger.info("*** Expected Results: - Verify that the audit data display Critical Error in Audit Detail page");
		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.auditDetailTable()));
//		Assert.assertTrue(eligibilityConfiguration.auditDetailErrMsg().getText().contains("Critical error occurred during processing:"));
		logger.info("Error Message "+eligibilityConfiguration.auditDetailTable().getText());
		//logger.info("*** Expected Results: - Verify that the audit data display properly in Audit Detail page");
		//List<String> auditDetailList = Arrays.asList("U", "ERR_CD", "ABBREV", originalShortDescription.toUpperCase(), newShortDescription.toUpperCase());
//		List<String> auditDetailList = Arrays.asList("U", "ERR_CD", "SHORT_DESCR", originalShortDescription.toUpperCase(), newShortDescription.toUpperCase());
//		verifyAuditDetailDisplayed(auditDetailList);
		driver.close();
		switchToParentWin(parent);
		
		logger.info("Click on Reset button");
		assertTrue(isElementPresent(eligibilityConfiguration.resetBtn(), 5),"Reset button is displayed.");
		clickHiddenPageObject(eligibilityConfiguration.resetBtn(), 0);
	}	
	
	@Test(priority = 1, description = "Validation-Do not allow duplication")
	public void testXPR_920() throws Exception {		
		logger.info("Testing - testXPR_920");
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);

		String eligSvcId = "AutotestXPR920";
		String name = "XIFIN";
		cleanUp(eligSvcId);logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");	
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5),"Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(),"Eligibility Configuration","Eligibility Service screen should be displayed.");
		
		logger.info("Enter a new XIFIN Eligibility Service ID");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInputInLoadPage(),5),"Eligibility Service ID input field should be displayed.");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);
		
		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen. All fields are empty.");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.loginIDInput(),5),"Login ID field should be displayed.");
		assertEquals(eligibilityConfiguration.loginIDInput().getText(),"","Login ID field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.classNameInput(),5), "ClassName field should be displayed.");
		assertEquals(eligibilityConfiguration.classNameInput().getText(),"","ClassName field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.nameInput(),5), "Name field should be displayed.");
		assertEquals(eligibilityConfiguration.nameInput().getText(),"","Name field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.passwordIDInput(),5),"Password field should be displayed.");
		assertEquals(eligibilityConfiguration.passwordIDInput().getText(),"","Password field should be empty.");
		
		logger.info("Enter valid data in Name, Login ID, Password, Classname, Exception Alert Threshold, Server Delay, Server Timeout fields");
		String loginId = randomCharacter.getRandomAlphaString(5);
		String passWd = loginId;
		String className = "com.mbasys.mars.dataService.eligibility.XifinEligibilityProvider";
		
		eligibilityConfiguration.enterEligibilityServiceInfos(loginId, className, name, passWd, "5", "5", "5");
		
		logger.info("Add a new row in Payor Setup grid with valid Payor ID");
		List<Pyr> pyrInfoList = payorDao.get3rdPartyPyrNotInPyrElig();
		String pyrAbbrev = pyrInfoList.get(0).getPyrAbbrv();
		String pyrId = String.valueOf(pyrInfoList.get(0).getPyrId());
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblAddBtn(),5),"Add button icon should be displayed in Payor Setup table.");
		clickHiddenPageObject(eligibilityConfiguration.payorSetupTblAddBtn(),0);
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorIdInputInPopup(),5), "Payor ID field should be displayed in Add Record popup window.");
		eligibilityConfiguration.setPayorIdInPopup(pyrAbbrev);
		
		assertTrue(isElementPresent(eligibilityConfiguration.daysToCheckEligInput(),5)," Days to Check Elig Input field should be displayed.");
		eligibilityConfiguration.setDaysToCheckElig("10");
		
		assertTrue(isElementPresent(eligibilityConfiguration.oKBtn(),5)," OK button should be displayed in Add Record popup.");
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(),0);
		
		logger.info("Verify that a new row is added to the Payor Setup grid with correct data");				
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblCelData(2, 3),5),"Payor ID should be displayed.");
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev),"Payor ID " + pyrAbbrev + " should be displayed in the Payor Setup.");
		
		logger.info("Add duplicate Payor in Payor Setup grid");		
		clickHiddenPageObject(eligibilityConfiguration.payorSetupTblAddBtn(),0);
		eligibilityConfiguration.setPayorIdInPopup(pyrAbbrev);
		
		logger.info("Verify that error message 'Table already contains this payor, cannot add a duplicate.' is displayed. Click Cancel button.");
		assertTrue(isElementPresent(eligibilityConfiguration.errMsgInAddRecordPopup(),5),"Error message should be displayed.");
		assertEquals(eligibilityConfiguration.errMsgInAddRecordPopup().getText(), "Table already contains this payor, cannot add a duplicate.", "Error message 'Table already contains this payor, cannot add a duplicate.' should be displayed.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.cancelBtn(),5),"Cancel button should be displayed on Add Payor popup.");
		clickHiddenPageObject(eligibilityConfiguration.cancelBtn(),0);		
				
		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
		
		logger.info("Reload the Eligibility Service ID just created");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);		
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		logger.info("Add a new row in Validation Follow-Up Action Code Configuration grid");
		List<EligFollowUpActnTyp> eligFollowUpActnTypList = rpmDao.getEligFollowUpActnTyp(testDb);
		List<EligValidationActnTyp> eligValidationActnTypList = rpmDao.getEligValidationActnTyp(testDb);		
		
		assertTrue(isElementPresent(eligibilityConfiguration.addBtnValidationFollowUpActionCodeConfigTbl(),5),"Add button should be displayed in Validation Follow-Up Action Code Configuration grid.");
		clickHiddenPageObject(eligibilityConfiguration.addBtnValidationFollowUpActionCodeConfigTbl(),0);
		
		assertTrue(isElementPresent(eligibilityConfiguration.followUpActionCodeDropDown(),5), "Follow-Up Action dropdown should be displayed.");
		String followUpActnCodeStr = eligFollowUpActnTypList.get(0).getAbbrev().trim() + " - " + eligFollowUpActnTypList.get(0).getDescr().trim();
		eligibilityConfiguration.setFollowUpActionCode(followUpActnCodeStr);
		
		assertTrue(isElementPresent(eligibilityConfiguration.actionDropDown(),5), "Action dropdown should be displayed.");
		String actionStr = eligValidationActnTypList.get(0).getAbbrev().trim() + " - " + eligValidationActnTypList.get(0).getDescr().trim();
		eligibilityConfiguration.setAction(actionStr);
		
		assertTrue(isElementPresent(eligibilityConfiguration.oKBtn(),5)," OK Button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(),0);
		
		logger.info("Verify that a new row is added in the Validation Follow-Up Action Code Configuration grid");
		assertTrue(isElementPresent(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3),5),"Follow-Up Action Code should be displayed.");
		assertTrue(isElementPresent(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4),5),"Action should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3).getText(),followUpActnCodeStr, "Follow-Up Action Code " + followUpActnCodeStr + " should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4).getText(),actionStr, " Action " + actionStr + " should be displayed.");
		
		logger.info("Add duplicate row in Validation Follow-Up Action Code Configuration grid");		
		clickHiddenPageObject(eligibilityConfiguration.addBtnValidationFollowUpActionCodeConfigTbl(),0);
		eligibilityConfiguration.setFollowUpActionCode(followUpActnCodeStr);
		eligibilityConfiguration.setAction(actionStr);
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(),0);
		
		logger.info("Verify that error message 'A duplicate record was entered' is displayed. Click Cancel button.");
		assertTrue(isElementPresent(eligibilityConfiguration.errMsgInAddRecordPopup(),5),"Error message should be displayed.");
		assertEquals(eligibilityConfiguration.errMsgInAddRecordPopup().getText(), "A duplicate record was entered", "Error message 'A duplicate record was entered' should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.cancelBtn(),0);		
		
		logger.info("Add a new row in Validation Reject Reason Code Override Configuration grid");
		scrollToElement(eligibilityConfiguration.valRejectResonTitle());
		List<EligRejectRsnTyp> eligRejectRsnTypList = rpmDao.getEligRejectRsnTyp(testDb);		
		
		String rejectReasonCodeStr = eligRejectRsnTypList.get(0).getAbbrev().trim() + " - " + eligRejectRsnTypList.get(0).getDescr().trim();
		String OverrideActionStr = eligValidationActnTypList.get(1).getAbbrev().trim() + " - " + eligValidationActnTypList.get(1).getDescr().trim();

		eligibilityConfiguration.setDataInValidationRejectReasonCodeOverrideConfig(rejectReasonCodeStr, OverrideActionStr);
		
		logger.info("Verify that a new row is added in Validation Reject Reason Code Override Configuration grid");
		assertEquals(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(),rejectReasonCodeStr), true,"Reject Reason Coe " + rejectReasonCodeStr + " should be added to table.");
		
		logger.info("***Add duplicate row in Validation Reject Reason Code Override Configuration grid");

		eligibilityConfiguration.setDataInValidationRejectReasonCodeOverrideConfig(rejectReasonCodeStr, OverrideActionStr);

		logger.info("Verify that error message 'A duplicate record was entered' is displayed. Click Cancel button.");
		assertTrue(isElementPresent(eligibilityConfiguration.errMsgInAddRecordPopup(),5),"Error message should be displayed.");
		assertEquals(eligibilityConfiguration.errMsgInAddRecordPopup().getText(), "A duplicate record was entered", "Error message 'A duplicate record was entered' should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.cancelBtn(),0);		
				
		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
		
		logger.info("Verify that new records are saved properly in ELIG_SVC, PYR_ELIG and ELIG_RESP tables in DB");		
		List<List<String>> eligSvcInfo = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("ELIG_SVC_NAME = '"+name+"' and DESCR = '"+eligSvcId.toUpperCase()+"'", testDb);		
		List<List<String>> pyrEligInfo = daoManagerXifinRpm.getPyrEligInfoFromPYRELIGByConditions("PK_ELIG_SVC_ID = "+eligSvcInfo.get(0).get(4)+"", testDb);
		
		assertEquals(eligSvcInfo.get(0).get(0),name, " New record should be added in Elig_Svc table with correct Elig Service Name.");
		assertEquals(eligSvcInfo.get(0).get(1),eligSvcId.toUpperCase(), " New record should be added in Elig_Svc table with correct Elig Service ID.");
		assertEquals(eligSvcInfo.get(0).get(2),loginId, " New record should be added in Elig_Svc table with correct Elig Service LoginID.");
		assertEquals(eligSvcInfo.get(0).get(3),className, " New record should be added in Elig_Svc table with correct Elig Service ClassName.");
		
		assertEquals(pyrEligInfo.get(0).get(0),pyrId, " Payor Record should be saved in Pyr_Elig table.");
		
		logger.info("Verify that new records are saved properly in ELIG_REJECT_VALIDATION and ELIG_FOLLOW_UP_VALIDATION tables in DB");		
		String eligSvcSeqId = eligSvcInfo.get(0).get(4);
		String rejectRsnCodeId = String.valueOf(eligRejectRsnTypList.get(0).getEligRejectRsnTypId());
		String validationActnTypId = String.valueOf(eligValidationActnTypList.get(1).getEligValidationActnTypId());
		List<EligRejectValidation> eligRejectValidationList = rpmDao.getEligRejectValidationByEligSvcIdRejectRsnTypIdValidationActnId(testDb, eligSvcSeqId, rejectRsnCodeId, validationActnTypId);
		assertTrue(eligRejectValidationList.size() > 0, "A new record should be added in ELIG_REJECT_VALIDATION table.");
				
		String followUpActnTypId = String.valueOf(eligFollowUpActnTypList.get(0).getEligFollowUpActnTypId());
		validationActnTypId = String.valueOf(eligValidationActnTypList.get(0).getEligValidationActnTypId());
		List<EligFollowUpValidation> eligFollowUpValidationList = rpmDao.getEligFollowUpValidationByEligSvcIdFollowUpActnTypIdValidationActnTypId(testDb, eligSvcSeqId, followUpActnTypId, validationActnTypId);
		assertTrue(eligFollowUpValidationList.size() > 0, "A new record should be added in ELIG_FOLLOW_UP_VALIDATION table.");
		
		logger.info("Reload the same Eligibility Service ID");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);		
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
				
		logger.info("Verify that a new row is added to the Payor Setup grid with correct data");			
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev),"Payor ID " + pyrAbbrev + " should be displayed in the Payor Setup.");
				
		logger.info("Verify that a new row is added in the Validation Follow-Up Action Code Configuration grid");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3).getText(),followUpActnCodeStr, "Follow-Up Action Code " + followUpActnCodeStr + " should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4).getText(),actionStr, "Action " + actionStr + " should be displayed.");
		
		logger.info("Verify that a new row is added in Validation Reject Reason Code Override Configuration grid");
		assertEquals(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(),rejectReasonCodeStr), true,"Reject Reason Coe " + rejectReasonCodeStr + " should be added to table.");
	}	
	
	@Test(priority = 1, description = "Reload Eligibility Services ID via Search")
	public void testXPR_799() throws Exception
	{
		logger.info("===== Testing - testXPR_799 =====");
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);
		eligibilityServiceSearchResults = new EligibilityServiceSearchResults(driver);
		logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5), " Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(), "Eligibility Configuration", " Eligibility Service screen should be displayed.");

		logger.info("Load the Eligibility Service with pk_Elig_Svc_Id = 99999");
		List<List<String>> eligSvcInfoList = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("pk_Elig_Svc_Id = 99999", testDb);
		String eligSvcId = eligSvcInfoList.get(0).get(1);
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);

		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(), 5), "Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");

		logger.info("Click Eligibility Search icon button");
		assertTrue(isElementPresent(eligibilityConfiguration.searchBtnEligConfig(), 5), "Search Icon in Eligibility Configuration page should show.");
		clickHiddenPageObject(eligibilityConfiguration.searchBtnEligConfig(), 0);
		String parent = switchToPopupWin();

		logger.info("Verify that Eligibility Service Search Result page is displayed");
		assertTrue(isElementPresent(eligibilityServiceSearchResults.colRowInServiceSearchResultHyberLink(2, 2), 5), "Eligibility Service Search Result page is displayed.");

		logger.info("Select different Eligibility Service Id hyberLink in Search Results");
		eligSvcInfoList = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("pk_Elig_Svc_Id = 7777", testDb);
		String newEligSvcId = eligSvcInfoList.get(0).get(1);
		int rowNum = getRowNumberInWebTable(eligibilityServiceSearchResults.eligibilitySearchTbl(), "tbl_eligibilitySearch", newEligSvcId.trim(), 2);
		logger.info(newEligSvcId.trim() + " " + rowNum);
		clickHiddenPageObject(eligibilityServiceSearchResults.colRowInServiceSearchResultHyberLink(rowNum - 1, 2), 0);

		switchToParentWin(parent);

		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(), 5), "Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(newEligSvcId), " Different Eligibility Service ID " + newEligSvcId + " should be displayed.");

		List<List<String>> newEligSvcInfoList = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("DESCR ='" + newEligSvcId.toUpperCase() + "'", testDb);
		assertEquals(eligibilityConfiguration.nameInput().getAttribute("value"), newEligSvcInfoList.get(0).get(0), "Name is displayed correctly.");
		assertEquals(eligibilityConfiguration.loginIDInput().getAttribute("value"), newEligSvcInfoList.get(0).get(2), "Login ID is displayed correctly.");
		assertEquals(eligibilityConfiguration.classNameInput().getAttribute("value"), newEligSvcInfoList.get(0).get(3), "Classname is displayed correctly.");
		assertEquals(eligibilityConfiguration.exceptionAlertThreadholdInput().getAttribute("value"), newEligSvcInfoList.get(0).get(10), "Exception Alert Threshold is displayed correctly.");
		assertEquals(eligibilityConfiguration.serverDelayInput().getAttribute("value"), newEligSvcInfoList.get(0).get(6), "Server Delay is displayed correctly.");
		assertEquals(eligibilityConfiguration.serverTimeoutInput().getAttribute("value"), newEligSvcInfoList.get(0).get(7), "Server Timeout is displayed correctly.");

		logger.info("Click on Reset button");
		assertTrue(isElementPresent(eligibilityConfiguration.resetBtn(), 5), "Reset button is displayed.");
		clickHiddenPageObject(eligibilityConfiguration.resetBtn(), 0);
	}
	
	@Test(priority = 1, description = "Verify Delete")
	public void testXPR_948() throws Exception {		
		logger.info("Testing - testXPR_948");
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);

		String eligSvcId = "AutotestXPR948";
		String name = "XIFIN";
		cleanUp(eligSvcId);logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");	
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5),"Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(),"Eligibility Configuration","Eligibility Service screen should be displayed.");
		
		logger.info("Enter a new XIFIN Eligibility Service ID");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInputInLoadPage(),5),"Eligibility Service ID input field should be displayed.");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);
		
		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen; All fields are empty");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), " Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.loginIDInput(),5),"Login ID field should be displayed.");
		assertEquals(eligibilityConfiguration.loginIDInput().getText(),"","Login ID field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.classNameInput(),5), "ClassName field should be displayed.");
		assertEquals(eligibilityConfiguration.classNameInput().getText(),"","ClassName field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.nameInput(),5), "Name field should be displayed.");
		assertEquals(eligibilityConfiguration.nameInput().getText(),"","Name field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.passwordIDInput(),5),"Password field should be displayed.");
		assertEquals(eligibilityConfiguration.passwordIDInput().getText(),"","Password field should be empty.");
		
		logger.info("Enter valid data in Name, Login ID, Password, Classname, Exception Alert Threshold, Server Delay, Server Timeout fields");
		String loginId = randomCharacter.getRandomAlphaString(5);
		String passWd = loginId;
		String className = "com.mbasys.mars.dataService.eligibility.XifinEligibilityProvider";
		
		eligibilityConfiguration.enterEligibilityServiceInfos(loginId, className, name, passWd, "5", "5", "5");
		
		logger.info("*** Step 4 Actions: - Add a new row in Payor Setup grid with valid Payor ID");
		List<Pyr> pyrInfoList = payorDao.get3rdPartyPyrNotInPyrElig();
		String pyrAbbrev = pyrInfoList.get(0).getPyrAbbrv();
		String pyrId = String.valueOf(pyrInfoList.get(0).getPyrId());
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblAddBtn(),5),"Add button icon should be displayed in Payor Setup table.");
		clickHiddenPageObject(eligibilityConfiguration.payorSetupTblAddBtn(),0);
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorIdInputInPopup(),5), "Payor ID field should be displayed in Add Record popup window.");
		eligibilityConfiguration.setPayorIdInPopup(pyrAbbrev);
		
		assertTrue(isElementPresent(eligibilityConfiguration.daysToCheckEligInput(),5),"Days to Check Elig Input field should be displayed.");
		eligibilityConfiguration.setDaysToCheckElig("10");
		
		assertTrue(isElementPresent(eligibilityConfiguration.oKBtn(),5),"OK button should be displayed in Add Record popup.");
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(),0);
		
		logger.info("Verify that a new row is added to the Payor Setup grid with correct data");				
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblCelData(2, 3),5),"Payor ID should be displayed.");
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev),"Payor ID " + pyrAbbrev + " should be displayed in the Payor Setup.");
				
		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
		
		logger.info("Reload the Eligibility Service ID just created");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);		
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		logger.info("Add a new row in Validation Follow-Up Action Code Configuration grid");
		List<EligFollowUpActnTyp> eligFollowUpActnTypList = rpmDao.getEligFollowUpActnTyp(testDb);
		List<EligValidationActnTyp> eligValidationActnTypList = rpmDao.getEligValidationActnTyp(testDb);		
		
		assertTrue(isElementPresent(eligibilityConfiguration.addBtnValidationFollowUpActionCodeConfigTbl(),5),"Add button should be displayed in Validation Follow-Up Action Code Configuration grid.");
		clickHiddenPageObject(eligibilityConfiguration.addBtnValidationFollowUpActionCodeConfigTbl(),0);
		
		assertTrue(isElementPresent(eligibilityConfiguration.followUpActionCodeDropDown(),5), "Follow-Up Action dropdown should be displayed.");
		String followUpActnCodeStr = eligFollowUpActnTypList.get(0).getAbbrev().trim() + " - " + eligFollowUpActnTypList.get(0).getDescr().trim();
		eligibilityConfiguration.setFollowUpActionCode(followUpActnCodeStr);
		
		assertTrue(isElementPresent(eligibilityConfiguration.actionDropDown(),5), "Action dropdown should be displayed.");
		String actionStr = eligValidationActnTypList.get(0).getAbbrev().trim() + " - " + eligValidationActnTypList.get(0).getDescr().trim();
		eligibilityConfiguration.setAction(actionStr);
		
		assertTrue(isElementPresent(eligibilityConfiguration.oKBtn(),5),"OK Button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(),0);
		
		logger.info("Verify that a new row is added in the Validation Follow-Up Action Code Configuration grid");
		assertTrue(isElementPresent(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3),5),"Follow-Up Action Code should be displayed.");
		assertTrue(isElementPresent(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4),5),"Action should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3).getText(),followUpActnCodeStr, "Follow-Up Action Code " + followUpActnCodeStr + " should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4).getText(),actionStr, "Action " + actionStr + " should be displayed.");
		
		logger.info("Add a new row in Validation Reject Reason Code Override Configuration grid");
		scrollToElement(eligibilityConfiguration.valRejectResonTitle());
		List<EligRejectRsnTyp> eligRejectRsnTypList = rpmDao.getEligRejectRsnTyp(testDb);		
		
		String rejectReasonCodeStr = eligRejectRsnTypList.get(0).getAbbrev().trim() + " - " + eligRejectRsnTypList.get(0).getDescr().trim();
		String OverrideActionStr = eligValidationActnTypList.get(1).getAbbrev().trim() + " - " + eligValidationActnTypList.get(1).getDescr().trim();
		
		eligibilityConfiguration.setDataInValidationRejectReasonCodeOverrideConfig(rejectReasonCodeStr, OverrideActionStr);
		
		logger.info("Verify that a new row is added in Validation Reject Reason Code Override Configuration grid");
		assertEquals(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(),rejectReasonCodeStr), true,"Reject Reason Coe " + rejectReasonCodeStr + " should be added to table.");
				
		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
		
		logger.info("Verify that new records are saved properly in ELIG_SVC, PYR_ELIG and ELIG_RESP tables in DB");		
		List<List<String>> eligSvcInfo = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("ELIG_SVC_NAME = '"+name+"' and DESCR = '"+eligSvcId.toUpperCase()+"'", testDb);		
		List<List<String>> pyrEligInfo = daoManagerXifinRpm.getPyrEligInfoFromPYRELIGByConditions("PK_ELIG_SVC_ID = "+eligSvcInfo.get(0).get(4)+"", testDb);

		assertEquals(eligSvcInfo.get(0).get(0),name, "New record should be added in Elig_Svc table with correct Elig Service Name.");
		assertEquals(eligSvcInfo.get(0).get(1),eligSvcId.toUpperCase(), "New record should be added in Elig_Svc table with correct Elig Service ID.");
		assertEquals(eligSvcInfo.get(0).get(2),loginId, "New record should be added in Elig_Svc table with correct Elig Service LoginID.");
		assertEquals(eligSvcInfo.get(0).get(3),className, "New record should be added in Elig_Svc table with correct Elig Service ClassName.");
		
		assertEquals(pyrEligInfo.get(0).get(0),pyrId, "Payor Record should be saved in Pyr_Elig table.");	
		
		logger.info("Verify that new records are saved properly in ELIG_REJECT_VALIDATION and ELIG_FOLLOW_UP_VALIDATION tables in DB");
		String eligSvcSeqId = eligSvcInfo.get(0).get(4);
		String rejectRsnCodeId = String.valueOf(eligRejectRsnTypList.get(0).getEligRejectRsnTypId());
		String validationActnTypId = String.valueOf(eligValidationActnTypList.get(1).getEligValidationActnTypId());
		List<EligRejectValidation> eligRejectValidationList = rpmDao.getEligRejectValidationByEligSvcIdRejectRsnTypIdValidationActnId(testDb, eligSvcSeqId, rejectRsnCodeId, validationActnTypId);
		assertTrue(eligRejectValidationList.size() > 0, "A new record should be added in ELIG_REJECT_VALIDATION table.");
				
		String followUpActnTypId = String.valueOf(eligFollowUpActnTypList.get(0).getEligFollowUpActnTypId());
		validationActnTypId = String.valueOf(eligValidationActnTypList.get(0).getEligValidationActnTypId());
		List<EligFollowUpValidation> eligFollowUpValidationList = rpmDao.getEligFollowUpValidationByEligSvcIdFollowUpActnTypIdValidationActnTypId(testDb, eligSvcSeqId, followUpActnTypId, validationActnTypId);
		assertTrue(eligFollowUpValidationList.size() > 0, "A new record should be added in ELIG_FOLLOW_UP_VALIDATION table.");
		
		logger.info("Reload the same Eligibility Service ID");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);		
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
				
		logger.info("Verify that a new row is added to the Payor Setup grid with correct data");			
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev),"Payor ID " + pyrAbbrev + " should be displayed in the Payor Setup.");
					
		logger.info("Verify that a new row is added in the Validation Follow-Up Action Code Configuration grid");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3).getText(),followUpActnCodeStr, "Follow-Up Action Code " + followUpActnCodeStr + " should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4).getText(),actionStr, "Action " + actionStr + " should be displayed.");
		
		logger.info("Verify that a new row is added in Validation Reject Reason Code Override Configuration grid");
		assertEquals(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(),rejectReasonCodeStr), true,"Reject Reason Code " + rejectReasonCodeStr + " should be added to table.");
				
		logger.info("Delete in Payor Setup, Validation Follow-Up Action Code Configuration and Validation Reject Reason Code Override Configuration girds");
		eligibilityConfiguration.clickPayorSetupTableDeleteCheckBox(2);

		assertTrue(isElementPresent(eligibilityConfiguration.getCellInputInValidationFollowUpActionCodeConfigTbl(2,5),5),"Delete checkbox in Validation Follow-Up Action Code Configuration grid is displayed.");
		clickHiddenPageObject(eligibilityConfiguration.getCellInputInValidationFollowUpActionCodeConfigTbl(2,5), 0);

		scrollToElement(eligibilityConfiguration.deleteCheckBoxValidationRejectReasonCodeTbl(2,5));
		assertTrue(isElementPresent(eligibilityConfiguration.deleteCheckBoxValidationRejectReasonCodeTbl(2,5), 5),"Delete checkbox in Validation Reject Reason Code Override Configuration grid should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.deleteCheckBoxValidationRejectReasonCodeTbl(2, 5), 0);
		
		logger.info("Click on Save And Clear button");		
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
		
		logger.info("Reload the same Eligibility Service ID");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);	
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
				
		logger.info("Verify that no data displayed in Payor Setup, Validation Follow-Up Action Code Configuration and Validation Reject Reason Code Override Configuration girds");
		//Payor Setup
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblTotalRecord(),10),"Total record should be displayed on Payor Setup grid.");
		assertTrue(eligibilityConfiguration.payorSetupTblTotalRecord().getText().contains("Empty records"), "Empty records should show in the grid.");
	
		//Validation Follow-Up Action Code Configuration
		assertTrue(isElementPresent(eligibilityConfiguration.totalRecordOnValidationFollowUpActionCodeConfigTbl(),10),"Total record should be displayed in Validation Follow-Up Action Code Configuration grid.");
		assertTrue(eligibilityConfiguration.totalRecordOnValidationFollowUpActionCodeConfigTbl().getText().contains("Empty records"), "Empty records should show in the grid.");

		//Validation Reject Reason Code Override Configuration
		assertTrue(isElementPresent(eligibilityConfiguration.totalRecordOfValRejectReasonTbl(),10),"Total record should be displayed on ValRejectReason grid.");
		assertTrue(eligibilityConfiguration.totalRecordOfValRejectReasonTbl().getText().contains("Empty records"), "Empty records should show in the grid.");

		logger.info("Click on Reset button");		
		clickHiddenPageObject(eligibilityConfiguration.resetBtn(), 0);
	}

	@Test(priority = 1, description = "Verify Update via Searches")
	public void testXPR_949() throws Exception {		
		logger.info("Testing - testXPR_949");    
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);
		eligibilitySubServiceSearchResults = new EligibilitySubServiceSearchResults(driver);

		String eligSvcId = "AutotestXPR949";
		String name = eligSvcId+"Name";
		cleanUp(eligSvcId);logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");			
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5),"Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(),"Eligibility Configuration","Eligibility Service screen should be displayed.");
		
		logger.info("Enter a new Non-XIFIN Eligibility Service ID");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInputInLoadPage(),5),"Eligibility Service ID input field should be displayed.");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);
		
		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen. All fields are empty");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.loginIDInput(),5),"Login ID field should be displayed.");
		assertEquals(eligibilityConfiguration.loginIDInput().getText(),""," Login ID field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.classNameInput(),5), "ClassName field should be displayed.");
		assertEquals(eligibilityConfiguration.classNameInput().getText(),""," ClassName field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.nameInput(),5), "Name field should be displayed.");
		assertEquals(eligibilityConfiguration.nameInput().getText(),""," Name field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.passwordIDInput(),5),"Password field should be displayed.");
		assertEquals(eligibilityConfiguration.passwordIDInput().getText(),""," Password field should be empty.");
		
		logger.info("Enter valid data in Name, Login ID, Password, Classname, Exception Alert Threshold, Server Delay, Server Timeout fields");
		String loginId = randomCharacter.getRandomAlphaString(5);
		String passWd = loginId;
		String className = "com.mbasys.mars.dataService.eligibility.XifinEligibilityProvider";
		
		eligibilityConfiguration.enterEligibilityServiceInfos(loginId, className, name, passWd, "5", "5", "5");
		
		logger.info("Add a new row in Payor Setup grid with valid Payor ID");
		List<Pyr> pyrInfoList = payorDao.get3rdPartyPyrNotInPyrElig();
		String pyrAbbrev = pyrInfoList.get(0).getPyrAbbrv();
		String pyrId = String.valueOf(pyrInfoList.get(0).getPyrId());
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblAddBtn(),5),"Add button icon should be displayed in Payor Setup table.");
		clickHiddenPageObject(eligibilityConfiguration.payorSetupTblAddBtn(),0);
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorIdInputInPopup(),5), "Payor ID field should be displayed in Add Record popup window.");
		eligibilityConfiguration.setPayorIdInPopup(pyrAbbrev);
		
		assertTrue(isElementPresent(eligibilityConfiguration.daysToCheckEligInput(),5),"Days to Check Elig Input field should be displayed.");
		eligibilityConfiguration.setDaysToCheckElig("10");
		
		assertTrue(isElementPresent(eligibilityConfiguration.oKBtn(),5),"OK button should be displayed in Add Record popup.");
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(),0);
		
		logger.info("Verify that a new row is added to the Payor Setup grid with correct data");				
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblCelData(2, 3),5)," Payor ID should be displayed.");
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev)," Payor ID " + pyrAbbrev + " should be displayed in the Payor Setup.");
				
		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
		
		logger.info("Verify that new records are saved properly in ELIG_SVC, PYR_ELIG and ELIG_RESP tables");		
		List<List<String>> eligSvcInfo = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("ELIG_SVC_NAME = '"+name+"' and DESCR = '"+eligSvcId.toUpperCase()+"'", testDb);		
		List<List<String>> pyrEligInfo = daoManagerXifinRpm.getPyrEligInfoFromPYRELIGByConditions("PK_ELIG_SVC_ID = "+eligSvcInfo.get(0).get(4)+"", testDb);
		
		assertEquals(eligSvcInfo.get(0).get(0),name, " New record should be added in Elig_Svc table with correct Elig Service Name.");
		assertEquals(eligSvcInfo.get(0).get(1),eligSvcId.toUpperCase(), " New record should be added in Elig_Svc table with correct Elig Service ID.");
		assertEquals(eligSvcInfo.get(0).get(2),loginId, " New record should be added in Elig_Svc table with correct Elig Service LoginID.");
		assertEquals(eligSvcInfo.get(0).get(3),className, " New record should be added in Elig_Svc table with correct Elig Service ClassName.");
		
		assertEquals(pyrEligInfo.get(0).get(0),pyrId, "Payor Record should be saved in Pyr_Elig table.");
				
		logger.info("Reload the same Eligibility Service ID");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);	
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5)," Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), " Eligibility Service ID " + eligSvcId + " should be displayed.");
				
		logger.info("Edit the row in Payor Setup grid via Eligibility Sub Service Search and click OK button");
		clickHiddenPageObject(eligibilityConfiguration.rowOnPayorSetupTblPyorSetupSection(2, 3), 0);
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblEditBtn(),5)," Edit button in Payor Setup table should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.payorSetupTblEditBtn(), 0);		
		
		String newOutgoingPyrId = "OUTGOINGPYRID"+ randomCharacter.getRandomNumericString(2);		
		assertTrue(isElementPresent(eligibilityConfiguration.outPyrIdTxtBox(),5)," Out Pyr Id Text Box should be displayed.");
		eligibilityConfiguration.setOutPyrIdField(newOutgoingPyrId);
		
		String newDaysToCheck = randomCharacter.getNonZeroRandomNumericString(1);
		eligibilityConfiguration.setDaysToCheckElig(newDaysToCheck);
		
		assertTrue(isElementPresent(eligibilityConfiguration.subServiceSearchIcon(), 5)," Sub Service search icon is available.");
		clickHiddenPageObject(eligibilityConfiguration.subServiceSearchIcon(), 0);		
		String parent = switchToPopupWin();
		assertEquals(eligibilitySubServiceSearchResults.eligibilitySubSvcSearchTblTitle().getText(),"Eligibility Sub Service Search Results"," Eligibility Sub Service Search Result page should be displayed correctly.");
		String subService = eligibilitySubServiceSearchResults.eligibilitySubSvcSearchCelData(2, 2).getText();
		assertTrue(isElementPresent(eligibilitySubServiceSearchResults.eligibilitySubSvcSearchCelData(2, 2), 5)," Eligibility Sub Service Search Result page is displayed");		
		clickHiddenPageObject(eligibilitySubServiceSearchResults.eligibilitySubSvcSearchCelData(2,2), 0);
		
		switchToParentWin(parent);
		assertTrue(isElementPresent(eligibilityConfiguration.oKBtn(), 5));
		clickHiddenPageObject(eligibilityConfiguration.oKBtn(), 0);
		
		logger.info("Verify that the updated data is shown in the Payor Setup grid");
		assertEquals(eligibilityConfiguration.rowOnPayorSetupTblPyorSetupSection(2, 5).getText().toUpperCase(), newOutgoingPyrId.toUpperCase(), " The new Outgoing Payor ID " + newOutgoingPyrId + " should show.");
		assertEquals(eligibilityConfiguration.rowOnPayorSetupTblPyorSetupSection(2, 7).getText(), newDaysToCheck," The updated Days to Check Elig " + newDaysToCheck + " should show.");
		assertEquals(eligibilityConfiguration.rowOnPayorSetupTblPyorSetupSection(2, 8).getText(), subService.toUpperCase()," The new Sub Service " +  subService + " should show.");
		
		logger.info("Click on Save And Clear button");		
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", " Load Eligibility Configuration screen should show.");
		
		logger.info("Reload the same Eligibility Service ID");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);	
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		logger.info("Verify that the updated data show properly in Payor Setup grid");		
		assertTrue(eligibilityConfiguration.rowOnPayorSetupTblPyorSetupSection(2, 5).getText().equals(newOutgoingPyrId)," Updated Outgoing Payor Id " + newOutgoingPyrId + " should show.");
		assertTrue(eligibilityConfiguration.rowOnPayorSetupTblPyorSetupSection(2, 7).getText().equals(newDaysToCheck)," Updated Days to Check Elig " + newDaysToCheck + " should show.");
		assertTrue(eligibilityConfiguration.rowOnPayorSetupTblPyorSetupSection(2, 8).getText().equals(subService)," Updated Sub Service " + subService + " should show.");
		
		logger.info("Update data in Login ID, Password, Classname, Exception Alert Threshold, Server Delay, Server Timeout fields in header section");
		name = eligSvcId+"Name"+"updated";
		passWd = loginId;
		className = "com.mbasys.mars.dataService.eligibility.roster.RosterEligibilityProvider";
		
		eligibilityConfiguration.enterEligibilityServiceInfos(loginId, className, name, passWd, "15", "5000", "5000");		
		
		logger.info("Click on Save And Clear button");		
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");
		
		logger.info("Reload the same Eligibility Service ID");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);	
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5)," Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), "Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		logger.info("Verify that the updated Eligibiltiy Configuration information show properly in the Header section");
		assertEquals(eligibilityConfiguration.loginIDInput().getAttribute("value"), loginId," Updated Login ID " + loginId + " should show.");		
		assertEquals(eligibilityConfiguration.nameInput().getAttribute("value"), name," Updated Name " + name + " should show.");		
		assertEquals(eligibilityConfiguration.passwordIDInput().getAttribute("value"), StringUtils.EMPTY, " Updated Password " + passWd + " should not display on screen.");
		assertEquals(eligibilityConfiguration.classNameInput().getAttribute("value"), className, " Updated Classname " + className + " should show.");
		assertEquals(eligibilityConfiguration.exceptionAlertThreadholdInput().getAttribute("value"), "15", " Updated Exception Alert Threshold 15 should show.");
		assertEquals(eligibilityConfiguration.serverDelayInput().getAttribute("value"), "5000", " Updated Server Delay 5000 should show.");
		assertEquals(eligibilityConfiguration.serverTimeoutInput().getAttribute("value"), "5000", " Updated Server Timout 5000 should show.");

		logger.info("Verifying EligSvc password DB fields");
		EligSvc eligSvc = rpmDao.getEligSvcByEligSvcDesrc(null, StringUtils.upperCase(eligSvcId));
		assertNotNull(eligSvc, "ElgSvc record could not be found in DB, eligSvcDescr="+StringUtils.upperCase(eligSvcId));
		assertNull(eligSvc.getPasswd(), "EligSvc plaintext password field should be empty, eligSvcDescr="+eligSvc.getDescr()+", passwd="+eligSvc.getPasswd());
		assertNotNull(eligSvc.getPasswdEncrypted(), "EligSvc encrypted password field should not be empty, eligSvcDescr="+eligSvc.getDescr()+", passwdEncrypted="+eligSvc.getPasswdEncrypted());

		logger.info("Click on Reset button");		
		clickHiddenPageObject(eligibilityConfiguration.resetBtn(), 0);
	}
	
	@Test(priority = 1, description = "Verify filters")
	public void testXPR_921() throws Exception {		
		logger.info("Testing - testXPR_921");
		eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
		randomCharacter = new RandomCharacter(driver);

		String eligSvcId = "AutotestXPR921";
		String name = "XIFIN";
		cleanUp(eligSvcId);logger.info("Verify that the Eligibility Configuration page is displayed with correct page title");	
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityConfigPageTitle(), 5),"Eligibility Service title is displayed.");
		assertEquals(eligibilityConfiguration.eligibilityConfigPageTitle().getText(),"Eligibility Configuration","Eligibility Service screen should be displayed.");
		
		logger.info("Enter a new XIFIN Eligibility Service ID");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInputInLoadPage(),5),"Eligibility Service ID input field should be displayed.");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);
		
		logger.info("Verify that Eligibility Service header fields are displayed in Eligibility Configuration screen; All fields are empty");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), " Eligibility Service ID " + eligSvcId + " should be displayed.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.loginIDInput(),5),"Login ID field should be displayed.");
		assertEquals(eligibilityConfiguration.loginIDInput().getText(),""," Login ID field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.classNameInput(),5), "ClassName field should be displayed.");
		assertEquals(eligibilityConfiguration.classNameInput().getText(),""," ClassName field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.nameInput(),5), "Name field should be displayed.");
		assertEquals(eligibilityConfiguration.nameInput().getText(),""," Name field should be empty.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.passwordIDInput(),5),"Password field should be displayed.");
		assertEquals(eligibilityConfiguration.passwordIDInput().getText(),""," Password field should be empty.");
		
		logger.info("Enter valid data in Name, Login ID, Password, Classname, Exception Alert Threshold, Server Delay, Server Timeout fields");
		String loginId = randomCharacter.getRandomAlphaString(5);
		String passWd = loginId;
		String className = "com.mbasys.mars.dataService.eligibility.XifinEligibilityProvider";
		
		eligibilityConfiguration.enterEligibilityServiceInfos(loginId, className, name, passWd, "5", "5", "5");		
		
		logger.info("Click on Save And Clear button");		
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", " Load Eligibility Configuration screen should show.");
		
		logger.info("Reload the Eligibility Service ID just created");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);		
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5)," Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), " Eligibility Service ID " + eligSvcId + " should be displayed.");
				
		logger.info("Add two new records in Payor Setup grid with valid Payor IDs");
		List<Pyr> pyrInfoList1 = payorDao.get3rdPartyPyrNotInPyrElig();
		String pyrAbbrev1 = pyrInfoList1.get(0).getPyrAbbrv();		
		List<Pyr> pyrInfoList2 = payorDao.get3rdPartyPyrNotInPyrElig();
		String pyrAbbrev2 = pyrInfoList2.get(0).getPyrAbbrv();		
		String pyrName2 = daoManagerXifinRpm.getPayorNameByPayorID(pyrAbbrev2, testDb);
		
		String daysToCheckElig1 = "10";
		String daysToCheckElig2 = "20";
		
		eligibilityConfiguration.addRecordInPayorSetupRequiredFields(pyrAbbrev1, daysToCheckElig1);
		eligibilityConfiguration.addRecordInPayorSetupRequiredFields(pyrAbbrev2, daysToCheckElig2);
		
		logger.info("Verify that two rows are added to the Payor Setup grid with correct data");				
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblCelData(2, 3),5)," Payor ID should be displayed.");
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev1)," Payor ID " + pyrAbbrev1 + " should be displayed in the Payor Setup.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTblCelData(3, 3),5)," Payor ID should be displayed.");
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(3, 3).getText().equalsIgnoreCase(pyrAbbrev2)," Payor ID " + pyrAbbrev2 + " should be displayed in the Payor Setup.");
	
		logger.info("Add two new records in Validation Follow-Up Action Code Configuration grid");
		List<EligFollowUpActnTyp> eligFollowUpActnTypList = rpmDao.getEligFollowUpActnTyp(testDb);
		List<EligValidationActnTyp> eligValidationActnTypList = rpmDao.getEligValidationActnTyp(testDb);
		Assert.assertTrue(eligFollowUpActnTypList.size() >= 2, "Must have at least 2 Folloy-up action types");
		Assert.assertTrue(eligValidationActnTypList.size() >= 2, "Must have at least 2 Validation action types");

		String followUpActnCodeStr1 = eligFollowUpActnTypList.get(0).getAbbrev().trim() + " - " + eligFollowUpActnTypList.get(0).getDescr().trim();		
		String followUpActnCodeStr2 = eligFollowUpActnTypList.get(1).getAbbrev().trim() + " - " + eligFollowUpActnTypList.get(1).getDescr().trim();
				
		String actionStr1 = eligValidationActnTypList.get(0).getAbbrev().trim() + " - " + eligValidationActnTypList.get(0).getDescr().trim();
		String actionStr2 = eligValidationActnTypList.get(1).getAbbrev().trim() + " - " + eligValidationActnTypList.get(1).getDescr().trim();

		eligibilityConfiguration.addRecordInValidationFollowUpActionCodeConfig(followUpActnCodeStr1, actionStr1);
		Thread.sleep(3000);
		scrollToElement(eligibilityConfiguration.titleValidationFollowUpActionCodeConfigTbl());
		eligibilityConfiguration.addRecordInValidationFollowUpActionCodeConfig(followUpActnCodeStr2, actionStr2);
		
		logger.info("Verify that two new rows are added in the Validation Follow-Up Action Code Configuration grid");
		assertTrue(isElementPresent(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3),5)," Follow-Up Action Code should be displayed.");
		assertTrue(isElementPresent(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4),5)," Action should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,3).getText(),followUpActnCodeStr1, " Follow-Up Action Code " + followUpActnCodeStr1 + " should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(2,4).getText(),actionStr1, " Action " + actionStr1 + " should be displayed.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(3,3),5)," Follow-Up Action Code should be displayed.");
		assertTrue(isElementPresent(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(3,4),5)," Action should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(3,3).getText(),followUpActnCodeStr2, " Follow-Up Action Code " + followUpActnCodeStr2 + " should be displayed.");
		assertEquals(eligibilityConfiguration.validationFollowUpActionCodeConfigTblCelData(3,4).getText(),actionStr2, " Action " + actionStr2 + " should be displayed.");
				
		logger.info("Add two new rows in Validation Reject Reason Code Override Configuration grid");
		scrollToElement(eligibilityConfiguration.valRejectResonTitle());
		List<EligRejectRsnTyp> eligRejectRsnTypList = rpmDao.getEligRejectRsnTyp(testDb);		
		
		String rejectReasonCodeStr1 = eligRejectRsnTypList.get(0).getAbbrev().trim() + " - " + eligRejectRsnTypList.get(0).getDescr().trim();
		String OverrideActionStr1 = eligValidationActnTypList.get(1).getAbbrev().trim() + " - " + eligValidationActnTypList.get(1).getDescr().trim();
		
		eligRejectRsnTypList = rpmDao.getEligRejectRsnTyp(testDb);
		String rejectReasonCodeStr2 = eligRejectRsnTypList.get(0).getAbbrev().trim() + " - " + eligRejectRsnTypList.get(0).getDescr().trim();
		eligValidationActnTypList = rpmDao.getEligValidationActnTyp(testDb);
		String OverrideActionStr2 = eligValidationActnTypList.get(1).getAbbrev().trim() + " - " + eligValidationActnTypList.get(1).getDescr().trim();
		
		eligibilityConfiguration.setDataInValidationRejectReasonCodeOverrideConfig(rejectReasonCodeStr1, OverrideActionStr1);
		eligibilityConfiguration.setDataInValidationRejectReasonCodeOverrideConfig(rejectReasonCodeStr2, OverrideActionStr2);
		
		logger.info("Verify that two new rows are added in Validation Reject Reason Code Override Configuration grid");
		assertEquals(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(),rejectReasonCodeStr1), true," Reject Reason Code " + rejectReasonCodeStr1 + " should be added to table.");
		assertEquals(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(),rejectReasonCodeStr2), true," Reject Reason Code " + rejectReasonCodeStr2 + " should be added to table.");
				
		logger.info("Click on Save And Clear button");
		assertTrue(isElementPresent(eligibilityConfiguration.saveAndClearBtn(),5), "Save and Clear button should be displayed.");
		clickHiddenPageObject(eligibilityConfiguration.saveAndClearBtn(),0);

		wait.until(ExpectedConditions.visibilityOf(eligibilityConfiguration.eligSvcIDInputInLoadPage()));
		
		logger.info("Verify that it's back to the Load Eligibility Configuration screen");
		assertEquals(eligibilityConfiguration.eligSvcIDInputInLoadPage().getAttribute("value"),"", "Load Eligibility Configuration screen should show.");

		logger.info("Reload the same Eligibility Service ID");
		eligibilityConfiguration.setEligSvcIDInLoadPage(eligSvcId);		
		
		logger.info("Verify that Eligibility Service is loaded");
		assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInput(),5),"Eligibility Service ID input field should be displayed.");
		assertTrue(eligibilityConfiguration.eligSvcIDInput().getAttribute("value").equalsIgnoreCase(eligSvcId), " Eligibility Service ID " + eligSvcId + " should be displayed.");
				
		logger.info("Verify that newly added data are displayed properly in all grids");
		assertTrue(isElementPresent(eligibilityConfiguration.payorSetupTbl(),5),"Payor Setup grid should be displayed.");
		assertTrue(getColumnValue(eligibilityConfiguration.payorSetupTbl(),pyrAbbrev1),"Payor ID " + pyrAbbrev1 + "should be displayed in Payor Setup grid");
		assertTrue(getColumnValue(eligibilityConfiguration.payorSetupTbl(),pyrAbbrev2),"Payor ID " + pyrAbbrev2 + "should be displayed in Payor Setup grid");
		
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityValidationFollowUpTbl(),5),"Eligibility Validation Follow-Up grid should be displayed.");
		assertTrue(getColumnValue(eligibilityConfiguration.eligibilityValidationFollowUpTbl(),followUpActnCodeStr1), " Follow-Up Action Code " + followUpActnCodeStr1 + " should be displayed in the Validation Follow-Up Action Code Configuration grid.");
		assertTrue(getColumnValue(eligibilityConfiguration.eligibilityValidationFollowUpTbl(),followUpActnCodeStr2), " Follow-Up Action Code " + followUpActnCodeStr2 + " should be displayed in the Validation Follow-Up Action Code Configuration grid.");
		
		assertTrue(isElementPresent(eligibilityConfiguration.valRejectReasonTbl(),5),"Validation Reject Reason grid should be displayed.");
		assertEquals(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(), rejectReasonCodeStr1), true," Reject Reason Code " + rejectReasonCodeStr1 + " should be displayed in Validation Reject Reason Code Override Configuration grid.");
		assertEquals(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(), rejectReasonCodeStr2), true," Reject Reason Code " + rejectReasonCodeStr2 + " should be displayed in Validation Reject Reason Code Override Configuration grid.");
				
		logger.info("Enter Payor ID in Payor Setup grid Payor ID filter");
		assertTrue(isElementPresent(eligibilityConfiguration.payorIdFilterInputInPayorSetupTbl(), 5)," Payor ID filter should be displayed.");
		eligibilityConfiguration.setPayorIdFilterInPayorSetupTbl(pyrAbbrev2);		
		setInputValue(eligibilityConfiguration.payorIdFilterInputInPayorSetupTbl(), pyrAbbrev2);
		Thread.sleep(3000);
		
		logger.info("Verify that the searched values are filtered properly in Payor Setup grid");			
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 3).getText().equalsIgnoreCase(pyrAbbrev2)," Payor ID " + pyrAbbrev2 + " should be displayed in the Payor Setup.");
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 4).getText().equalsIgnoreCase(pyrName2)," Payor Name " + pyrName2 + " should show in the Payor Setup grid");
		assertTrue(eligibilityConfiguration.payorSetupTblCelData(2, 7).getText().equalsIgnoreCase(daysToCheckElig2)," Days to Check Elig " + daysToCheckElig2 + " should show in the Payor Setup grid");
		
		logger.info("Enter value in Action filter in Validation Follow-Up Action Code Configuration grid");
		scrollToElement(eligibilityConfiguration.titleValidationFollowUpActionCodeConfigTbl());
		assertTrue(isElementPresent(eligibilityConfiguration.actionFilterInValidationFollowUp(),5),"Action filter should be displayed.");
		setInputValue(eligibilityConfiguration.actionFilterInValidationFollowUp(), actionStr2);
		
		logger.info("Verify that the search values are filtered properly in Validation Follow-Up Action Code Configuration grid");
		assertTrue(isElementPresent(eligibilityConfiguration.eligibilityValidationFollowUpTbl(),10),"Validation Reject Reason Code Override Configuration grid should be displayed.");
		assertTrue(getColumnValue(eligibilityConfiguration.eligibilityValidationFollowUpTbl(), followUpActnCodeStr2),"Follow-up Action Code " + followUpActnCodeStr2 + " should show in Validation Reject Reason Code Override Configuration grid.");
		assertTrue(getColumnValue(eligibilityConfiguration.eligibilityValidationFollowUpTbl(), actionStr2)," Action " + actionStr2 + "should show in Validation Reject Reason Code Override Configuration grid.");
				
		logger.info("Enter value in Reject Reason Code filter in Validation Reject Reason Code Override Configuration grid");
		scrollToElement(eligibilityConfiguration.valRejectResonTitle());
		assertTrue(isElementPresent(eligibilityConfiguration.rejectReasonCodeFilter(),5),"Reject Reason Code filter should be displayed.");
		setInputValue(eligibilityConfiguration.rejectReasonCodeFilter(), rejectReasonCodeStr2);
		
		logger.info("Verify that the search values are filtered properly in Validation Reject Reason Code Override Configuration grid");
		assertTrue(isElementPresent(eligibilityConfiguration.valRejectReasonTbl(),10),"Validation Reject Reason Code Override Configuration grid should be displayed.");
		assertTrue(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(), rejectReasonCodeStr2),"Reject Reason Code " + rejectReasonCodeStr2 + " should show in Validation Reject Reason Code Override Configuration grid.");
		assertTrue(getColumnValue(eligibilityConfiguration.valRejectReasonTbl(), OverrideActionStr2),"Action " + OverrideActionStr2 + " should show in Validation Reject Reason Code Override Configuration grid.");
				
		logger.info("Click on Reset button");		
		clickHiddenPageObject(eligibilityConfiguration.resetBtn(), 0);
	}

	private void cleanUp(String descr) throws Exception
	{
		logger.info("Cleaning up, elig service, descr="+descr);
		List<List<String>> eligSvcInfo = daoManagerXifinRpm.getEligSvcInfoFromELIGSVCByConditions("DESCR = '"+ StringUtils.upperCase(descr)+"'", null);
		if (eligSvcInfo.size() > 0)
		{
			String eligSvcId = eligSvcInfo.get(0).get(4);
			if (!StringUtils.equals(eligSvcId, String.valueOf(EligMap.XIFIN)))
			{
				daoManagerPlatform.deleteDataFromTableByCondition("PYR_ELIG", "PK_ELIG_SVC_ID = '" + eligSvcId + "'", null);
				daoManagerPlatform.deleteDataFromTableByCondition("ELIG_REJECT_VALIDATION", "FK_ELIG_SVC_ID = '" + eligSvcId + "'", null);
				daoManagerPlatform.deleteDataFromTableByCondition("ELIG_FOLLOW_UP_VALIDATION", "FK_ELIG_SVC_ID = '" + eligSvcId + "'", null);
				daoManagerPlatform.deleteDataFromTableByCondition("ELIG_SVC", "PK_ELIG_SVC_ID = '" + eligSvcId + "'", null);
			}
		}
	}
}	
