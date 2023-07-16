package com.newXp.tests;

import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrDt.PyrDt;
import com.mbasys.mars.ejb.entity.pyrSvc.PyrSvc;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.overall.fileMaintenance.claimStatusConfig.ClaimStatusConfig;
import com.overall.menu.MenuNavigation;
import com.overall.search.PayorSearchResults;
import com.overall.utils.XifinPortalUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import com.xifin.xap.utils.XifinAdminUtils;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ClaimStatusConfigTest extends SeleniumBaseTest {

    private XifinPortalUtils xifinPortalUtils;
    private ClaimStatusConfig claimStatusConfig;
    private PayorSearchResults payorSearchResults;
    private List<PyrSvc> pyrSvcs;

    private static final String ADD_RECORD = "Add Record";
    private static final String LAST_ROW_INDEX = "last()";
    private static final String EDIT_RECORD = "Edit Record";
    private static final String PAYOR_SEARCH = "Payor Search";
    private static final String AUDIT_DETAIL = "Audit Detail";
    private static final String DATE_FORMATTED_DEFAULT = "MM/dd/yyyy";
    private static final String DATE_FORMATTED_DATABASE = "yyyy-MM-dd";
    private static final String CLAIM_STATUS_CONFIGURATION_PAGE_TITLE = "Claim Status Configuration";
    private static final String OUTGOING_PAYOR_ID_SEARCH_RESULTS = "Outgoing Payor ID Search Results";
    private static final String HEADER_HELP_LINK = "help/file_maintenance_tab/codes_menu/p_claim_status_configuration_header";
    private static final String FOOTER_HELP_LINK = "help/file_maintenance_tab/codes_menu/p_claim_status_configuration_summary";
    private static final String CLAIM_STATUS_CONFIG_SECTION_HELP_LINK = "help/file_maintenance_tab/codes_menu/p_claim_status_configuration";

    @BeforeMethod(alwaysRun = true)
    @Parameters({ "ssoXpUsername", "ssoXpPassword" })
    public void beforeMethod(String ssoXpUsername, String ssoXpPassword, Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            xifinPortalUtils = new XifinPortalUtils(driver, config);
            new XifinAdminUtils(driver, config).clearDataCache();

            logIntoSso(ssoXpUsername, ssoXpPassword);
            MenuNavigation menuNavigation = new MenuNavigation(driver, config);
            menuNavigation.navigateToClaimStatusConfigPage();
        } catch (Exception e) {
            logger.error("Error running BeforeMethod", e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
//			if (pyrSvcs != null && pyrSvcs.size() > 0) {
//				for (PyrSvc pyrSvc : pyrSvcs) {
//					xifinPortalUtils.deletePyrSvc(pyrSvc);
//				}
//			}
        } catch (Exception e) {
            logger.error("Error running afterMethod", e);
        }
    }

    @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoXpUsername", "ssoXpPassword", "disableBrowserPlugins"})
    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoXpUsername, String ssoXpPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running AfterSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            new XifinAdminUtils(driver, config).clearDataCache();
        }
        catch (Exception e)
        {
            Assert.fail("Error running AfterSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

//	Disabling test - we do not want to modify Claim Status configurations for random payors, as it will cause issues with other
//  Automation suites. This test should be re-written to use a new, dedicated Payor ID.
//	@Test(priority = 1, description = "Add Payor setup with valid data input")
//	public void testXPR_737() throws Throwable {
//		logger.info("===== Testing - testXPR_737 =====");
//

//		claimStatusConfig = new ClaimStatusConfig(driver, wait);
//		payorSearchResults = new PayorSearchResults(driver, wait);
//
//		logger.info("*** Step 1 Expected Result: User login successfully, FileMaintenance - Claim Status Configuration screen is displayed.");
//		verifyClaimStatusConfigPageIsDisplayed();
//
//		logger.info("*** Step 2 Action: Click on Add icon");
//		clickHiddenPageObject(claimStatusConfig.addBtn(), 0);
//
//		logger.info("*** Step 2 Expected Result: Add Record popup is displayed");
//		verifyAddRecordPopUpIsDisplayed();
//
//		logger.info("*** Step 3 Action:  Input valid Payor Id, Outgoing PayorId, Click on Ok button");
//		List<String> curPyrList = claimStatusConfig.getListDataInColPayorSetupTbl(3);
//		Pyr pyrInfo = payorDao.getRandomPyr();
//		for (int i = 0; i < curPyrList.size(); i++) {
//			if (pyrInfo.getPyrAbbrv().equals(curPyrList.get(i)) && !pyrInfo.getPyrAbbrv().contains("UCHC") ) {
//				pyrInfo = payorDao.getRandomPyr();
//			}
//		}
//		String pyrAbbrev = pyrInfo.getPyrAbbrv();
//		claimStatusConfig.enterPayorIdTxt(pyrAbbrev);
//
//		clickHiddenPageObject(claimStatusConfig.outPyrIdSearch(), 0);
//		String parentWin = switchToPopupWin();
//		String outPyrId = claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2).getText().trim();
//		clickHiddenPageObject(claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2), 0);
//		switchToParentWin(parentWin);
//		assertEquals(getInputVal(claimStatusConfig.outPyrIdTxt()), outPyrId, "        Outgoing Payor Id is displayed.");
//		clickHiddenPageObject(claimStatusConfig.okBtn(), 0);
//
//		logger.info("*** Step 3 Expected Result: New record is displayed in the table with: Active is check and Correctly payor information.");
//		List<String> newPyrInfo = claimStatusConfig.checkPyrInfo(pyrAbbrev);
//		assertTrue(newPyrInfo.size() > 0, "        New Payor is displayed in the table.");
//		assertEquals(newPyrInfo.get(4), "true", "        Active checkbox is checked.");
//		assertEquals(newPyrInfo.get(2), outPyrId, "        Outgoing Payor Id is displayed.");
//
//		logger.info("*** Step 4 Action: Click on Save button");
//		clickOnSaveBtn();
//
//		logger.info("*** Step 4 Expected Result: New record is add into Payor_Svc table and is displayed in grid");
//		pyrSvcs = payorDao.getPyrSvcByPyrId(pyrInfo.getPyrId());
//		verifyNewDataIsAddedIntoPyrSvc(pyrSvcs, pyrInfo.getPyrAbbrv(), outPyrId, true);
//
//	}

//	Disabling test - we do not want to modify Claim Status configurations for random payors, as it will cause issues with other
//  Automation suites. This test should be re-written to use a new, dedicated Payor ID.
//	@Test(priority = 1, description = "Add Payor setup with Active is uncheck")
//	public void testXPR_738() throws Throwable {
//		logger.info("===== Testing - testXPR_738 =====");
//
//		claimStatusConfig = new ClaimStatusConfig(driver, wait);
//		payorSearchResults = new PayorSearchResults(driver, wait);
//
//		logger.info("*** Step 1 Expected result: User login successful. Claim Status Configuration screen is displayed.");
//		verifyClaimStatusConfigPageIsDisplayed();
//
//		logger.info("*** Step 2 Action: Click on Add icon from grid.");
//		clickHiddenPageObject(claimStatusConfig.addBtn(), 0);
//
//		logger.info("*** Step 2 Expected result: Add record popup is displayed.");
//		verifyAddRecordPopUpIsDisplayed();
//
//		logger.info("*** Step 3 Action: Input valid PayorId, Outgoing PayorId, uncheck Active checkbox, click on OK button.");
//		Pyr pyrInfo = payorDao.getRandomPyrByPyrIdNotInPyrSvc();
//		claimStatusConfig.enterPayorIdTxt(pyrInfo.getPyrAbbrv());
//
//		clickHiddenPageObject(claimStatusConfig.outPyrIdSearch(), 0);
//		String parent = switchToPopupWin();
//		String outgoingPayorIdSelect = claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2).getText().trim();
//		clickHiddenPageObject(claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2), 0);
//		switchToParentWin(parent);
//
//		// uncheck Active checkbox
//		clickHiddenPageObject(claimStatusConfig.activeCheckBox(), 0);
//
//		// click on OK button
//		clickHiddenPageObject(claimStatusConfig.okBtn(), 0);
//
//		logger.info("*** Step 3 Expected result: New record is displayed in the table with Active is uncheck and correctly payor information.");
//		// correctly payor information
//		assertEquals(claimStatusConfig.payorSetupTblPayorIdColTxt(LAST_ROW_INDEX).getText().trim(), pyrInfo.getPyrAbbrv(), "        Display correctly payorID information.");
//		assertEquals(claimStatusConfig.payorSetupTblOutgoingPayorIdColTxt(LAST_ROW_INDEX).getText().trim(), outgoingPayorIdSelect, "        Display correctly OutgoingPayorId information.");
//		assertEquals(getInputVal(claimStatusConfig.payorSetupTblActiveColChk(LAST_ROW_INDEX)), "false", "        The active marked uncheck.");
//
//		logger.info("*** Step 5 Action: Click on save button.");
//		clickOnSaveBtn();
//
//		logger.info("*** Step 5 Expected result: New record is displayed in grid and is added into the PYR_SVC table with b_enable = 0.");
//		assertTrue(isInPayorSvc(pyrInfo.getPyrId()), "           The new record is presented Payor Svc table ");
//		pyrSvcs = payorDao.getPyrSvcByPyrId(pyrInfo.getPyrId());
//		verifyNewDataIsAddedIntoPyrSvc(pyrSvcs, pyrInfo.getPyrAbbrv(), outgoingPayorIdSelect, false);
//		assertEquals(pyrSvcs.get(0).getIsEnabled(), false, "        A new record is added into the PYR_SVC table.");
//
//	}
//
// 	Disabling test - we do not want to modify Claim Status configurations for random payors, as it will cause issues with other
//  Automation suites. This test should be re-written to use a new, dedicated Payor ID.
//	@Test(priority = 1, description = "Update Outgoing Payor with valid PayorId")
//	public void testXPR_739() throws Throwable {
//		logger.info("===== Testing - testXPR_739 =====");
//

//		claimStatusConfig = new ClaimStatusConfig(driver, wait);
//		payorSearchResults = new PayorSearchResults(driver, wait);
//
//		logger.info("*** Step 1 Expected Result: User login successfully, FileMaintenance - Claim Status Configuration screen is displayed.");
//		verifyClaimStatusConfigPageIsDisplayed();
//
//		logger.info("*** Step 2 Action: Add new Payor Id with valid data");
//		clickHiddenPageObject(claimStatusConfig.addBtn(), 0);
//		List<String> curPyrList = claimStatusConfig.getListDataInColPayorSetupTbl(3);
//		Pyr pyrInfo = payorDao.getRandomPyrByPyrIdNotInPyrSvc();
//		for (int i = 0; i < curPyrList.size(); i++) {
//			if (pyrInfo.getPyrAbbrv().equals(curPyrList.get(i))) {
//				pyrInfo = payorDao.getRandomPyrByPyrIdNotInPyrSvc();
//			}
//		}
//		String pyrAbbrev = pyrInfo.getPyrAbbrv();
//		claimStatusConfig.enterPayorIdTxt(pyrAbbrev);
//
//		clickHiddenPageObject(claimStatusConfig.outPyrIdSearch(), 0);
//		String popupWin = switchToPopupWin();
//		String oldOutPyrId = claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2).getText().trim();
//		clickHiddenPageObject(claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2), 0);
//		switchToParentWin(popupWin);
//		assertEquals(getInputVal(claimStatusConfig.outPyrIdTxt()), oldOutPyrId, "        Outgoing Payor Id is displayed.");
//		clickHiddenPageObject(claimStatusConfig.okBtn(), 0);
//
//		logger.info("*** Step 2 Expected Result: New Payor is displayed in PayorSetup table.");
//		List<String> newRowPyrInfo = claimStatusConfig.checkPyrInfo(pyrAbbrev);
//		assertTrue(newRowPyrInfo.size() > 0, "        New Payor is displayed in the table.");
//		assertEquals(newRowPyrInfo.get(2), oldOutPyrId, "        Outgoing Payor Id is displayed.");
//
//		logger.info("*** Step 3 Action:  Click on Save button.");
//		clickOnSaveBtn();
//
//		logger.info("*** Step 3 Expected Result: New record is added into PYR_SVC table with correct data.");
//		assertTrue(isInPayorSvc(pyrInfo.getPyrId()), "           The new record is presented Payor Svc table ");
//		pyrSvcs = payorDao.getPyrSvcByPyrId(pyrInfo.getPyrId());
//		assertTrue(pyrSvcs.size() > 0, "        A new record is added into the PYR_SVC table.");
//		assertEquals(pyrSvcs.get(0).getOutPyrId(), oldOutPyrId, "        Outgoing PayorId is added into PYR_SVC table.");
//
//		logger.info("*** Step 4 Action: Select the record at step 2, click on Edit icon.");
//		filterPayorId(pyrAbbrev);
//		claimStatusConfig.selectRowByValue(pyrAbbrev);
//
//		logger.info("*** Step 4 Expected Result: The edit popup is displayed with correctly data");
//		assertEquals(pyrAbbrev, getInputVal(claimStatusConfig.payorIdTxt()), "        PayorId is displayed correctly.");
//		assertEquals(getInputVal(claimStatusConfig.outPyrIdTxt()), oldOutPyrId, "        Outgoing Payor Id is displayed correctly.");
//
//		logger.info("*** Step 5 Action: Enter new Outgoing Payor Id, Click on OK button.");
//		clickHiddenPageObject(claimStatusConfig.outPyrIdSearch(), 0);
//		popupWin = switchToPopupWin();
//		String newOutPyrId = claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2).getText().trim();
//		clickHiddenPageObject(claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2), 0);
//		switchToParentWin(popupWin);
//		assertEquals(getInputVal(claimStatusConfig.outPyrIdTxt()), newOutPyrId, "        New Outgoing Payor Id is displayed.");
//		clickHiddenPageObject(claimStatusConfig.okBtn(), 0);
//
//		logger.info("*** Step 5 Expected Result: New Outgoing PayorId is displayed in table.");
//		newRowPyrInfo = claimStatusConfig.checkPyrInfo(pyrAbbrev);
//		assertTrue(newRowPyrInfo.size() > 0, "        Payor is displayed in the table.");
//		assertEquals(newRowPyrInfo.get(2), newOutPyrId, "        New Outgoing Payor Id is displayed.");
//
//		logger.info("*** Step 6 Action: Click on Save button");
//		clickOnSaveBtn();
//
//		logger.info("*** Step 6 Expected Result: The record in PYR_SVC is updated with new Outgoing Payor Id and is displayed on grid.");
//		pyrSvcs = payorDao.getPyrSvcByPyrId(pyrInfo.getPyrId());
//		assertEquals(pyrSvcs.get(0).getOutPyrId(), newOutPyrId, "        New outoing PayorId is updated to PYR_SVC table.");
//		verifyNewDataIsAddedIntoPyrSvc(pyrSvcs, pyrInfo.getPyrAbbrv(), newRowPyrInfo.get(2), true);
//
//	}
//
// 	Disabling test - we do not want to modify Claim Status configurations for random payors, as it will cause issues with other
//  Automation suites. This test should be re-written to use a new, dedicated Payor ID.
//	@Test(priority = 1, description = "Delete Payor in the table")
//	public void testXPR_740() throws Throwable {
//		logger.info("===== Testing - testXPR_740 =====");
//
//		claimStatusConfig = new ClaimStatusConfig(driver, wait);
//		payorSearchResults = new PayorSearchResults(driver, wait);
//
//		logger.info("*** Step 1 Expected Result: User login successfully - FileMaintenance - Claim Status Configuration screen is displayed.");
//		verifyClaimStatusConfigPageIsDisplayed();
//
//		logger.info("*** Step 2 Action: Add new Payor Id with valid data.");
//		clickHiddenPageObject(claimStatusConfig.addBtn(), 0);
//		List<String> curPyrList = claimStatusConfig.getListDataInColPayorSetupTbl(3);
//		Pyr pyrInfo = payorDao.getRandomPyrByPyrIdNotInPyrSvc();
//		for (int i = 0; i < curPyrList.size(); i++) {
//			if (pyrInfo.getPyrAbbrv().equals(curPyrList.get(i))) {
//				pyrInfo = payorDao.getRandomPyrByPyrIdNotInPyrSvc();
//			}
//		}
//		String pyrAbbrev = pyrInfo.getPyrAbbrv();
//		claimStatusConfig.enterPayorIdTxt(pyrAbbrev);
//
//		clickHiddenPageObject(claimStatusConfig.outPyrIdSearch(), 0);
//		String popupWin = switchToPopupWin();
//		String outPyrId = claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2).getText().trim();
//		clickHiddenPageObject(claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2), 0);
//		switchToParentWin(popupWin);
//		assertEquals(getInputVal(claimStatusConfig.outPyrIdTxt()), outPyrId, "        Outgoing Payor Id is added.");
//		clickHiddenPageObject(claimStatusConfig.okBtn(), 0);
//
//		logger.info("*** Step 2 Expected Result: New record is displayed in the table.");
//		List<String> newRowPyrInfo = claimStatusConfig.checkPyrInfo(pyrAbbrev);
//		assertTrue(newRowPyrInfo.size() > 0, "        New Payor is displayed in the table.");
//		assertEquals(newRowPyrInfo.get(2), outPyrId, "        Outgoing Payor Id is displayed.");
//
//		logger.info("*** Step 3 Action:  Click on Save button");
//		clickOnSaveBtn();
//
//		logger.info("*** Step 3 Expected Result: new record is added into PYR_SVC table with valid data.");
//		Thread.sleep(2000);
//		pyrSvcs = payorDao.getPyrSvcByPyrId(pyrInfo.getPyrId());
//		assertTrue(pyrSvcs.size() > 0, "        A new record is added into the PYR_SVC table.");
//
//		logger.info("*** Step 4 Action: Select the record at step 2 - click on Edit icon.");
//		filterPayorId(pyrAbbrev);
//		claimStatusConfig.selectRowByValue(pyrAbbrev);
//		logger.info("*** Step 4 Expected Result: Edit record popup is displayed with correctly information.");
//		assertEquals(claimStatusConfig.editRecordPopupTitle().getText().trim(), EDIT_RECORD, "        Edit record popup is displayed.");
//		assertEquals(pyrAbbrev, getInputVal(claimStatusConfig.payorIdTxt()), "        PayorId is displayed correctly.");
//		assertEquals(getInputVal(claimStatusConfig.outPyrIdTxt()), outPyrId, "        Outgoing Payor Id is displayed correctly.");
//
//		logger.info("*** Step 5 Action: Check on Delete checkbox - Click on OK button.");
//		clickHiddenPageObject(claimStatusConfig.deleteCheckBox(), 0);
//		assertTrue(claimStatusConfig.deleteCheckBox().isSelected(), "        Delete checkbox is checked.");
//		clickHiddenPageObject(claimStatusConfig.okBtn(), 0);
//
//		logger.info("*** Step 5 Expected Result: The delete checkbox is checked in table.");
//		assertEquals(claimStatusConfig.checkDeletedRows(), true, "        Delete checkbox is checked.");
//
//		logger.info("*** Step 6 Action: Click on Save button");
//		clickOnSaveBtn();
//
//		logger.info("*** Step 6 Expected Result: The record for selected PayorId at step 5 is removed out of the PYR_SVC table.");
//		pyrSvcs = payorDao.getPyrSvcByPyrId(pyrInfo.getPyrId());
//		assertTrue(pyrSvcs.size() < 1, "        The record for selected PayorId at step 5 is removed out of the PYR_SVC table.");
//
//	}

    /**
     * Test XPR_752 is disabled because ODS enabled (SS#26 = 1) does not work in our QA environment
     * If SS#26 = 1 (ODS enabled). Audit screen return Critical Error
     * If SS#26 = 0 (ODS disabled). Audit screens to fail to display
     * @throws Throwable
     */

    /**
     @Test(priority = 1, description = "Verify Run Audit Button")
     public void testXPR_752() throws Throwable {
     logger.info("===== Testing - testXPR_752 =====");

     timeStamp = new TimeStamp(driver);
     claimStatusConfig = new ClaimStatusConfig(driver, wait);
     payorSearchResults = new PayorSearchResults(driver, wait);

     logger.info("*** Step 1 Expected Result: User login successfully, FileMaintenance - Claim Status Configuration screen is displayed.");
     verifyClaimStatusConfigPageIsDisplayed();

     logger.info("*** Step 2 Action: Add new Payor into Payor Setup grid with valid payorId and Outgoing Payor Id.");
     clickHiddenPageObject(claimStatusConfig.addBtn(), 0);
     List<String> curPyrList = claimStatusConfig.getListDataInColPayorSetupTbl(3);
     Pyr pyrInfo = payorDao.getRandomPyrByPyrIdNotInPyrSvc();
     for (int i = 0; i < curPyrList.size(); i++) {
     if (pyrInfo.getPyrAbbrv().equals(curPyrList.get(i))) {
     pyrInfo = payorDao.getRandomPyrByPyrIdNotInPyrSvc();
     }
     }
     String pyrAbbrev = pyrInfo.getPyrAbbrv();
     claimStatusConfig.enterPayorIdTxt(pyrAbbrev);

     clickHiddenPageObject(claimStatusConfig.outPyrIdSearch(), 0);
     String popupWin = switchToPopupWin();
     String oldOutPyrId = claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2).getText().trim();
     clickHiddenPageObject(claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2), 0);
     switchToParentWin(popupWin);
     assertEquals(getInputVal(claimStatusConfig.outPyrIdTxt()), oldOutPyrId, "        Outgoing Payor Id is added.");
     clickHiddenPageObject(claimStatusConfig.okBtn(), 0);

     logger.info("*** Step 2 Expected Result: New Payor is displayed in the PayorSetup table.");
     List<String> newRowPyrInfo = claimStatusConfig.checkPyrInfo(pyrAbbrev);
     assertTrue(newRowPyrInfo.size() > 0, "        New Payor is displayed in the table.");
     assertEquals(newRowPyrInfo.get(2), oldOutPyrId, "        Outgoing Payor Id is displayed.");

     logger.info("*** Step 3 Action:  Click on Save button");
     clickOnSaveBtn();

     logger.info("*** Step 3 Expected Result: New record is saved into PYR_SVC table.");
     pyrSvcs = payorDao.getPyrSvcByPyrId(pyrInfo.getPyrId());
     assertTrue(pyrSvcs.size() > 0, "        A new record is added into the PYR_SVC table.");
     assertEquals(pyrSvcs.get(0).getOutPyrId(), oldOutPyrId, "        Outgoing Payor Id is added into PYR_SVC table for new Payor.");

     logger.info("*** Step 4 Action: Selected new payorId at step 2 - Click on Edit icon - Update Outgoing Payor Id - Click on Ok button.");
     filterPayorId(pyrAbbrev);
     claimStatusConfig.selectRowByValue(pyrAbbrev);
     assertEquals(claimStatusConfig.editRecordPopupTitle().getText().trim(), EDIT_RECORD, "        Edit Record popup title is displayed");

     clickHiddenPageObject(claimStatusConfig.outPyrIdSearch(), 0);
     popupWin = switchToPopupWin();
     String newOutPyrId = claimStatusConfig.outgoingPayorIdSearchResutLnk(LAST_ROW_INDEX).getText().trim();
     clickHiddenPageObject(claimStatusConfig.outgoingPayorIdSearchResutLnk(LAST_ROW_INDEX), 0);
     switchToParentWin(popupWin);
     assertEquals(getInputVal(claimStatusConfig.outPyrIdTxt()), newOutPyrId, "        New Outgoing Payor Id is displayed.");
     clickHiddenPageObject(claimStatusConfig.okBtn(), 0);
     logger.info("*** Step 4 Expected Result: New outoing PayorId is shown in the PayorSetup table.");
     newRowPyrInfo = claimStatusConfig.checkPyrInfo(pyrAbbrev);
     assertTrue(newRowPyrInfo.size() > 0, "        New Payor is displayed in the table.");
     assertEquals(newRowPyrInfo.get(2), newOutPyrId, "        Outgoing Payor Id is displayed.");

     logger.info("*** Step 5 Action: Click on Save button");
     clickOnSaveBtn();

     logger.info("*** Step 5 Expected Result: Outgoing Payor Id is save in PYR_SVC as updated.");
     pyrSvcs = payorDao.getPyrSvcByPyrId(pyrInfo.getPyrId());
     String audDtString = new SimpleDateFormat(DATE_FORMATTED_DATABASE).format(pyrSvcs.get(0).getAudDt().getTime());
     String dateUpdate = timeStamp.getConvertedDate(DATE_FORMATTED_DATABASE, DATE_FORMATTED_DEFAULT, audDtString);
     assertEquals(pyrSvcs.get(0).getOutPyrId(), newOutPyrId, "        Outgoing Payor Id is save in PYR_SVC as updated.");

     logger.info("*** Step 6 Action: Click on Run Audit button");
     claimStatusConfig.runAudit();

     logger.info("*** Step 6 Expected Result: The Audit detail page is displayed with correctly information such as: Date, Action, Table, Field, Old value and New value.");
     popupWin = switchToPopupWin();
     assertEquals(claimStatusConfig.auditLogTitle().getText().trim(), AUDIT_DETAIL, "        Audit Detail title is displayed");
     assertTrue(claimStatusConfig.getRowDataInUuditLogTbl(2, 2).getText().trim().contains(dateUpdate), "        Date is displayed.");
     assertEquals(claimStatusConfig.getRowDataInUuditLogTbl(2, 3).getText().trim(), "U", "         Updated Action is displayed.");
     assertEquals(claimStatusConfig.getRowDataInUuditLogTbl(2, 4).getText().trim(), "PYR_SVC", "         table is displayed.");
     assertEquals(claimStatusConfig.getRowDataInUuditLogTbl(2, 5).getText().trim(), "OUT_PYR_ID", "         Field is displayed.");
     assertEquals(claimStatusConfig.getRowDataInUuditLogTbl(2, 6).getText().trim(), oldOutPyrId, "         Old value is displayed.");
     assertEquals(claimStatusConfig.getRowDataInUuditLogTbl(2, 7).getText().trim(), newOutPyrId, "         New value is displayed.");
     driver.close();
     switchToParentWin(popupWin);

     }*/

// 	Disabling test - we do not want to modify Claim Status configurations for random payors, as it will cause issues with other
//  Automation suites. This test should be re-written to use a new, dedicated Payor ID.
//	@Test(priority = 1, description = "Search outgoing payorId")
//	public void testXPR_753() throws Throwable {
//		logger.info("===== Testing - testXPR_753 =====");
//

//		claimStatusConfig = new ClaimStatusConfig(driver, wait);
//		payorSearchResults = new PayorSearchResults(driver, wait);
//
//		logger.info("*** Step 1 Expected result: User login successful. Claim Status Configuration screen is displayed.");
//		verifyClaimStatusConfigPageIsDisplayed();
//
//		logger.info("*** Step 2 Action: Click on Add icon from grid.");
//		clickHiddenPageObject(claimStatusConfig.addBtn(), 0);
//
//		logger.info("*** Step 2 Expected result: Add record popup is displayed.");
//		assertTrue(isElementPresent(claimStatusConfig.addRecordPopupTitle(), 5), "        Add record popup is displayed.");
//
//		logger.info("*** Step 3 Action: Input valid PayorId, click on Outgoing PayorId icon.");
//		Pyr pyrInfo = payorDao.getRandomPyrByPyrIdNotInPyrSvc();
//		claimStatusConfig.enterPayorIdTxt(pyrInfo.getPyrAbbrv());
//		clickHiddenPageObject(claimStatusConfig.outPyrIdSearch(), 0);
//
//		logger.info("*** Step 3 Expected result: The Outgoing payorId search result page is display correctly.");
//		String parent = switchToPopupWin();
//		verifyOutgoingPayorIdSearchResultsIsDisplayed();
//
//		logger.info("*** Step 4 Action: Select any PayorId from search result page.");
//		String outgoingPayorIdSelect = claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2).getText().trim();
//		clickHiddenPageObject(claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2), 0);
//
//		logger.info("*** Step 4 Expected result: The search result page is close. The selected outgoing payorId is populated at outgoing payorId fields of Add record popup.");
//		switchToParentWin(parent);
//		assertEquals(getInputVal(claimStatusConfig.outPyrIdTxt()), outgoingPayorIdSelect, "        The selected outgoing payorId is populated at outgoing payorId fields of Add record popup.");
//		clickHiddenPageObject(claimStatusConfig.okBtn(), 0);
//
//		logger.info("*** Step 5 Action: Click on save button.");
//		clickOnSaveBtn();
//
//		logger.info("*** Step 5 Expected result: A new record is added into the PYR_SVC table and is dislpayed on grid");
//		pyrSvcs = payorDao.getPyrSvcByPyrId(pyrInfo.getPyrId());
//		verifyNewDataIsAddedIntoPyrSvc(pyrSvcs, pyrInfo.getPyrAbbrv(), outgoingPayorIdSelect, true);
//
//	}

    @Test(priority = 1, description = "Select PayorId from Search Result page")
    @Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
    public void testXPR_754(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Throwable {
        logger.info("===== Testing - testXPR_754 =====");

        claimStatusConfig = new ClaimStatusConfig(driver, wait);
        payorSearchResults = new PayorSearchResults(driver, wait);
        String payorId = createANewPayorForSearchPage(project,testSuite,testCase,propLevel,propName,wsUsername, wsPassword);
        Pyr newPayor = payorDao.getPyrByPyrAbbrv(payorId);
        payorDao.updatePrcSuspSetFalseByPyrId(newPayor.getPyrId());

        logger.info("*** Step 1 Expected result: User login successful. Claim Status Configuration screen is displayed.");
        verifyClaimStatusConfigPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on Add icon from grid.");
        clickHiddenPageObject(claimStatusConfig.addBtn(), 0);

        logger.info("*** Step 2 Expected result: Add record popup is displayed.");
        verifyAddRecordPopUpIsDisplayed();

        logger.info("*** Step 3 Action: Click on PayorId search icon.");
        clickHiddenPageObject(claimStatusConfig.addRecordPayorIdSearch(), 0);

        logger.info("*** Step 3 Expected result: Paypor Search page is displayed.");
        String parent = switchToPopupWin();
        assertEquals(claimStatusConfig.payorSearchPageTitle().getText().trim(), PAYOR_SEARCH, "        Paypor Search page is displayed with title 'Payor Search'.");

        logger.info("*** Step 4 Action: Input new payorId at PayorId field, click on Search button.");
        claimStatusConfig.enterPayorIdInPayorSearchPage(payorId);
        clickHiddenPageObject(claimStatusConfig.searchBtnInPayorSearchPage(), 0);

        logger.info("*** Step 4 Expected result: Search result page is displayed with new payorId is display.");
        switchToPopupWin(5);
        claimStatusConfig.waitForSearchResultLoadingSuccessfully(30);

        logger.info("*** Step 5 Action: Selected new PayorId at search result page.");
        int num = 2;
        String payorIdSelect = payorSearchResults.getPayorIdCellInTable(num, 2).getText().trim();
        clickHiddenPageObject(payorSearchResults.getPayorIdCellInTable(num, 2), 0);

        logger.info("*** Step 5 Expected result: The search result page is close. The selected payorId is populated at add record popup.");
        switchToParentWin(parent);
        assertEquals(getInputVal(claimStatusConfig.payorIdTxt()), payorIdSelect, "        The selected payorId is populated at add record popup.");

        clickHiddenPageObject(claimStatusConfig.outPyrIdSearch(), 0);
        parent = switchToPopupWin();
        verifyOutgoingPayorIdSearchResultsIsDisplayed();
        String outgoingPayorIdSelect = claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2).getText().trim();
        clickHiddenPageObject(claimStatusConfig.outgoingPayorIdSearchResultCell(2, 2), 0);
        switchToParentWin(parent);
        waitUntilElementPresent(claimStatusConfig.addBtn(), 10);

        claimStatusConfig.setServiceType("Claim Status Batch");
        claimStatusConfig.setSubmSvc("TEST-212");

        logger.info("*** Step 6 Action: Click OK button.");
        clickHiddenPageObject(claimStatusConfig.okBtn(), 0);

        logger.info("*** Step 6 Expected result: New Payor is shown in the grid.");
        assertTrue(getColumnValue(claimStatusConfig.payorSetupTbl(), payorIdSelect), "        New Payor is shown in the grid.");

        logger.info("*** Step 7 Action: Click on save button.");
        clickOnSaveBtn();

        logger.info("*** Step 7 Expected result: A new record is added into the PYR_SVC table and is displayed on grid");
        Pyr pyrInDb = payorDao.getPyrByPyrAbbrv(payorIdSelect);
        assertTrue(isInPayorSvc(pyrInDb.getPyrId()), "           The new record is presented Payor Svc table ");
        pyrSvcs = payorDao.getPyrSvcByPyrId(pyrInDb.getPyrId());
        verifyNewDataIsAddedIntoPyrSvc(pyrSvcs, payorIdSelect, outgoingPayorIdSelect, true);
        logger.info("*** Step 8 Action: - Revert data in PYR_SVC, Payor tbls.");
        cleanDataInPyr(newPayor);

    }

    @Test(priority = 1, description = "Verify Help Button")
    public void testXPR_755() throws Throwable {
        logger.info("===== Testing - testXPR_755 =====");

        claimStatusConfig = new ClaimStatusConfig(driver, wait);
        payorSearchResults = new PayorSearchResults(driver, wait);

        logger.info("*** Step 1 Expected Result: User login successfully - FileMaintenance - Claim Status Configuration screen is displayed.");
        verifyClaimStatusConfigPageIsDisplayed();

        logger.info("*** Step 2 Action: Click on Help Icon at Header menu");
        clickHiddenPageObject(claimStatusConfig.helpIconOnHeaderMenu(), 0);

        logger.info("*** Step 2 Expected Result: Help page is shown");
        xifinPortalUtils.verifyHelpPageIsDisplayed(this, HEADER_HELP_LINK, null);

        logger.info("*** Step 3 Action: Click on Help Icon on Claim Status Config Section");
        clickHiddenPageObject(claimStatusConfig.helpIconOnClaimStatusSection(), 0);

        logger.info("*** Step 3 Expected Result: Help page is shown");
        xifinPortalUtils.verifyHelpPageIsDisplayed(this, CLAIM_STATUS_CONFIG_SECTION_HELP_LINK, null);

        logger.info("*** Step 4 Action: Click on Help Icon on Footer menu");
        clickHiddenPageObject(claimStatusConfig.helpIconOnFooter(), 0);

        logger.info("*** Step 4 Expected Result: Help page is shown");
        xifinPortalUtils.verifyHelpPageIsDisplayed(this, FOOTER_HELP_LINK, null);

    }

    private void clickOnSaveBtn() throws Exception {
        clickHiddenPageObject(claimStatusConfig.saveBtn(), 0);
        xifinPortalUtils.waitForPageLoaded(wait);
    }

    private void filterPayorId(String payorId){
        claimStatusConfig.enterPayorIdFilter(payorId);
        xifinPortalUtils.waitForPageLoaded(wait);
    }

    private void verifyNewDataIsAddedIntoPyrSvc(List<PyrSvc> pyrSvcs, String payorIdSelect, String outgoingPayorIdSelect, boolean isActive) throws Exception {
        assertTrue(pyrSvcs.size() > 0, "        A new record is added into the PYR_SVC table.");
        filterPayorId(payorIdSelect);
        String index = LAST_ROW_INDEX;
        List<WebElement> payorSetupTblPayorIdColTxts = claimStatusConfig.payorSetupTblPayorIdColTxts();
        if(payorSetupTblPayorIdColTxts.size()> 1){
            for (int i = 0; i < payorSetupTblPayorIdColTxts.size(); i++) {
                if(payorSetupTblPayorIdColTxts.get(i).getText().trim().equals(payorIdSelect)){
                    index = String.valueOf(i+1);
                    break;
                }
            }
        }
        assertEquals(claimStatusConfig.payorSetupTblPayorIdColTxt(index).getText().trim(), payorIdSelect, "        Display correctly payorID information.");
        assertEquals(claimStatusConfig.payorSetupTblOutgoingPayorIdColTxt(index).getText().trim(), outgoingPayorIdSelect, "        Display correctly OutgoingPayorId information.");
        assertEquals(getInputVal(claimStatusConfig.payorSetupTblActiveColChk(index)), String.valueOf(isActive), "        The active marked " + (isActive == true ? "check." : "uncheck."));
    }

    private void verifyAddRecordPopUpIsDisplayed() throws Exception {
        assertEquals(claimStatusConfig.addRecordPopupTitle().getText().trim(), ADD_RECORD, "        Add Record text is displayed.");
    }

    private void verifyClaimStatusConfigPageIsDisplayed() throws Exception {
        assertEquals(claimStatusConfig.fileMaintenancePageTittle().getText().trim(), CLAIM_STATUS_CONFIGURATION_PAGE_TITLE, "        FileMaintenance - Claim Status Configuration screen is displayed.");
    }

    private void verifyOutgoingPayorIdSearchResultsIsDisplayed() throws Exception {
        assertEquals(claimStatusConfig.outgoingPayorIdResearchPageTitle().getText().trim(), OUTGOING_PAYOR_ID_SEARCH_RESULTS, "        The Outgoing payorId search result page is display correctly 'Outgoing Payor Id Search Results'.");
    }

    private String createANewPayorForSearchPage(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception{

        TestDataSetup testDataSetup = new TestDataSetup(driver);

        logger.info("*** Prepare data Action: Prepare valid Data."
                + " Create a new Payor Id via Payor WS"
        );
        logger.info("*** OrgAlias: "+ config.getProperty(PropertyMap.ORGALIAS));
        logger.info("*** Username: "+  wsUsername);
        logger.info("*** Password: "+  wsPassword);
        logger.info("*** dbEnv: "+  testDb);
        logger.info("*** endpoint PAYORWS_URL = "+ config.getProperty(PropertyMap.PAYORWS_URL));
        String payorId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.PAYORWS_URL));
        logger.info("*** Expected Results: - Verify that a new Client is avalable for using");
        Thread.sleep(3000);
        Assert.assertTrue(payorId != null);
        logger.info("*** Step Expected Results: - Ensure the new payorId was created :" + payorId);

        return payorId;
    }

    private void cleanDataInPyr(Pyr pyr) throws Exception {
        if (pyr == null) { return; }
        if (pyrSvcs != null && pyrSvcs.size() > 0) {
            for (PyrSvc pyrSvc : pyrSvcs) {
                xifinPortalUtils.deletePyrSvc(pyrSvc);
            }
        }
        List<PyrDt> pyrDts = payorDao.getPyrDtByPyrId(pyr.getPyrId());
        for (PyrDt pyrDt : pyrDts) {
            pyrDt.setResultCode(ErrorCodeMap.DELETED_RECORD);
            payorDao.setPyrDt(pyrDt);
        }
        pyr.setResultCode(ErrorCodeMap.DELETED_RECORD);
        payorDao.setPyr(pyr);
    }

    public boolean isInPayorSvc(int payorId) throws Exception
    {
        long startTime = System.currentTimeMillis();
        QUEUE_WAIT_TIME_MS += startTime;
        boolean isInPyrSvc = payorDao.isInPayorSvc(payorId);
        while (!isInPyrSvc && System.currentTimeMillis() < QUEUE_WAIT_TIME_MS)
        {
            logger.info("Waiting for payorId in Payor Svc, accnId=" + payorId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInPyrSvc = payorDao.isInPayorSvc(payorId);
        }
        return isInPyrSvc;
    }
}
