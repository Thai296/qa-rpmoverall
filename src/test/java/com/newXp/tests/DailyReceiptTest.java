package com.newXp.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.xifin.utils.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPmt.AccnPmt;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.dep.Dep;
import com.mbasys.mars.ejb.entity.depBatch.DepBatch;
import com.mbasys.mars.ejb.entity.depBatchSeq.DepBatchSeq;
import com.mbasys.mars.ejb.entity.dlyRcpt.DlyRcpt;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.systemSetting.SystemSetting;
import com.mbasys.mars.ejb.entity.userFacAccess.UserFacAccess;
import com.mbasys.mars.ejb.entity.users.Users;
import com.mbasys.mars.persistance.PmtMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.qa.dao.rpm.domain.AccnDailyReceipt;
import com.xifin.qa.dao.rpm.domain.ReceiptLog;
import com.overall.accession.dailyReceipt.DailyReceipt;
import com.overall.menu.MenuNavigation;
import com.overall.utils.DailyReceiptUtils;
import com.overall.utils.XifinPortalUtils;

import domain.accession.dailyReceipt.DailyReceiptSummarySection;
import domain.accession.dailyReceipt.Header;
import jodd.typeconverter.Convert;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class DailyReceiptTest extends SeleniumBaseTest{

	private String userName;
	private TimeStamp timeStamp;
	private DailyReceipt dailyReceipt;
	private RandomCharacter randomCharacter;
	private XifinPortalUtils xifinPortalUtils;
	private DailyReceiptUtils dailyReceiptUtils;

	private Dep dep = null;
	private Accn accn = null;
	private DlyRcpt dlyRcpt = null;
	private AccnPmt accnPmt = null;
	private DepBatch depBatch = null;
	private DepBatchSeq depBatchSeq = null;
	private UserFacAccess userFacAccess = null;
	
	private static final int TIME_OUT = 20;

	private static final String EMPTY = "";
	private static final String RELOAD = "reload";
	private static final String UNDEFINDED_NUMBER = "NaN";
	private static final String CREATE_NEW = "create new";
	private static final String FORMAT_DATE = "MM/dd/yyyy";
	private static final String DAILY_RECEIPT_TITLE = "Daily Receipt";
	private static final String FULL_FIELDS = "create / update full fields type";
	private static final String LOAD_EXISTED_DEFAULT_CASH_AT_DRAWER_OPENING = "0.00";
	private static final String REQUIRE_FIELDS = "create / update required fields type";
	private static final String NOTE_IN_DEP_TBL = "Created using Daily Receipts screen.";
	private static final String CREATED_NEW_DAILY_RECEIPT_POPUP_TITLE = "Created New Daily Receipt";
    private static final String ERROR_MESSAGE_INVALID_DAILY_RECEIPT_ID = "Invalid Daily Receipt ID.";
    private static final String ERROR_MESSAGE_ACCESS_DENIED_BY_FAC = "User access denied for facility"; 
    private static final String FOOTER_HELP_LINK = "help/accession_tab/order_processing_menu/p_daily_receipt_summary.htm";
    private static final Object YOU_MUST_RECORD_WHO_OPENED_THE_CASH_DRAWER_ERROR_MESSAGE = "You must record who opened the cash drawer.";
    private static final String DAILY_RECEIPT_LOAD_HEADER_HELP_LINK = "help/accession_tab/order_processing_menu/p_daily_receipt_header.htm";
    private static final String ERROR_MESSAGE_RETURNED_MISSING_EXPLAIN_ANY_OVERAGES = "A explanation of why the cash is over/under is required.";
	private static final String DAILY_RECEIPT_DETAILS_HELP_LINK = "help/accession_tab/order_processing_menu/p_daily_receipt_daily_receipt_details.htm";
	private static final String DAILY_RECEIPT_SUMMARY_HELP_LINK = "help/accession_tab/order_processing_menu/p_daily_receipt_daily_receipt_summary.htm";
	private static final String WARNING_POPUP_MESSAGE_RESET_DIRTY_DATA = "Changes have been made to this daily receipt. Are you sure you want to reset the page?";
	private static final String DAILY_RECEIPT_LOAD_DAILY_RECEIPT_HELP_LINK = "help/accession_tab/order_processing_menu/p_daily_receipt_load_daily_receipt_id.htm";
	
	@BeforeMethod(alwaysRun = true)
    @Parameters({"ssoXpUsername", "ssoXpPassword"})
    public void beforeMethod(String ssoXpUsername, String ssoXpPassword, Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            xifinPortalUtils = new XifinPortalUtils(driver, config);
            dailyReceiptUtils = new DailyReceiptUtils(driver, systemDao, accessionDao, patientDao);
            logIntoSso(ssoXpUsername, ssoXpPassword);
            MenuNavigation menuNavigation = new MenuNavigation(driver, config);
            menuNavigation.navigateToAccessionDailyReceiptPage();
            userName = ssoXpUsername.substring(0, ssoXpUsername.indexOf("@"));
        } catch (Exception e) {
            logger.error("Error running BeforeMethod", e);
        }
    }

	@AfterMethod(alwaysRun = true)
    public void afterMethod(Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            xifinPortalUtils.deleteUserFacAccess(userFacAccess);
            xifinPortalUtils.deleteAccnPmt(accnPmt);
            xifinPortalUtils.deleteDlyReceipt(dlyRcpt); 
            xifinPortalUtils.deleteDepBatchSeq(depBatchSeq);
            xifinPortalUtils.deleteDepBatch(depBatch);
            xifinPortalUtils.deleteDep(dep);
            xifinPortalUtils.deleteAccn(accn);
        } catch (Exception e) {
            logger.error("Error running afterMethod", e);
        }
    }
	/**
	 * Test scripts================================================================================================================================
	 */
	
	@Test(priority = 1, description = "Verify loading existing Daily Receipt")
	public void testXPR_1679() throws Throwable {
	    logger.info("===== Testing - testXPR_1679 =====");
	    
	    dailyReceipt = new DailyReceipt(driver);
	    
	    logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
	    verifyDailyReceiptLoadPageIsDisplayed();
	    
	    logger.info("*** Step 2 Action: Load valid Daily Receipt ID by sql (1)");
	    Header expHeader = new Header();
	    DailyReceiptSummarySection actDailyReceiptSummarySection = new DailyReceiptSummarySection();
	    setValueToDailyReceiptIDInput(expHeader, actDailyReceiptSummarySection, EMPTY);
	    
	    logger.info("*** Step 2 Expected Results: Detail Daily Receipt screen is displayed correct: Header data, Daily Receipt Summary section data, Daily Receipt Details section data.");
	    veifyHeaderDisplayedCorrectData(expHeader, EMPTY);
	    verifyDailyReceiptSummarySectionDisplayedCorrectData(actDailyReceiptSummarySection);
	    verifyDailyReceiptDetailsTableDisplayedCorrectly(expHeader.getDailyReceiptId(), null, null, EMPTY);
	    
	    driver.quit();
	}
	
	@Test(priority = 1, description = "Verify Help icon")
    public void testXPR_1680() throws Throwable {
        logger.info("===== Testing - testXPR_1680 =====");
        
        dailyReceipt = new DailyReceipt(driver);
        
        logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
        verifyDailyReceiptLoadPageIsDisplayed();
        
        logger.info("*** Step 2 Action: In Load Daily Receipt page, at Header, Click on help icon.");
        clickOnElement(dailyReceipt.headerHelpIco());
        
        logger.info("*** Step 2 Expected Results: The help page is displayed correctly the link.");
        verifyHelpPageIsDisplayed(DAILY_RECEIPT_LOAD_HEADER_HELP_LINK);
        		
        logger.info("*** Step 3 Action: At Daily Receipt section, click on help icon.");
        clickOnElement(dailyReceipt.dailyReceiptLoadPageDailyReceiptSectionHelpIco());
        
        logger.info("*** Step 3 Expected Results: The help page is displayed correctly the link.");
        verifyHelpPageIsDisplayed(DAILY_RECEIPT_LOAD_DAILY_RECEIPT_HELP_LINK);

        logger.info("*** Step 4 Action: Click on Create New Daily Receipt link.");
        prepareData(true);
	    clickHiddenPageObject(dailyReceipt.loadPageCreateNewDailyReceiptLnk(), 0);
	    setValidDataCreateNewDailyReceipt(accnPmt);
	    clickOnGetPaymentBtn();
        
        logger.info("*** Step 4 Expected Results: Detail Daily Receipt screen is displayed.");
        verifyDailyReceiptDetailPageIsDisplayed();
        
        logger.info("*** Step 5 Action: In Detail Daily Receipt page, at Header, click on help icon.");
        clickOnElement(dailyReceipt.headerHelpIco());
        
        logger.info("*** Step 5 Expected Results: The help page is displayed correctly the link.");
        verifyHelpPageIsDisplayed(DAILY_RECEIPT_LOAD_HEADER_HELP_LINK);

        logger.info("*** Step 6 Action: At Daily Receipt Summary section, click on help icon.");
        clickOnElement(dailyReceipt.dailyReceiptSummarySectionHelpIco());
        
        logger.info("*** Step 6 Expected Results: The help page is displayed correctly the link.");
        verifyHelpPageIsDisplayed(DAILY_RECEIPT_SUMMARY_HELP_LINK);

        logger.info("*** Step 7 Action: At Daily Receipt Details section, click on help icon.");
        clickOnElement(dailyReceipt.dailyReceiptDetailsSectionHelpIco());
       
        logger.info("*** Step 7 Expected Results: The help page is displayed correctly the link.");
        verifyHelpPageIsDisplayed(DAILY_RECEIPT_DETAILS_HELP_LINK);
        
        logger.info("*** Step 8 Action: Click on help icon at footer.");
        clickOnElement(dailyReceipt.footerHelpIco());
       
        logger.info("*** Step 8 Expected Results: The help page is displayed correctly the link.");
        verifyHelpPageIsDisplayed(FOOTER_HELP_LINK);
        
        logger.info("*** Step 9 Action: Click on Reset btn.");
        clickOnResetBtn();
        
        logger.info("*** Step 9 Expected Results: Load [Daily Receipt] screen is displayed.");
        verifyDailyReceiptLoadPageIsDisplayed();

        driver.quit();
    }

	@Test(priority = 1, description = "Verify creating new Daily Receipt with input all required fields")
    public void testXPR_1681() throws Throwable {
        logger.info("===== Testing - testXPR_1681 =====");

        dailyReceipt = new DailyReceipt(driver);
        Header actHeader = new Header();
        DailyReceiptSummarySection actDailyReceiptSummarySection = new DailyReceiptSummarySection();
        
        logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
        verifyDailyReceiptLoadPageIsDisplayed();
        
        logger.info("*** Step 2 Action: Click on Create New Daily Receipt link.");
        prepareData(true);
        clickHiddenPageObject(dailyReceipt.loadPageCreateNewDailyReceiptLnk(), 0);
       
        logger.info("*** Step 2 Expected Results: Payment Facility ID input and Payment Facility Name are displayed. "
        		+ "Payment User ID and Payment User Name are displayed.");
        verifyDailyReceiptSectionWhenClickOnCreateNewDailyReceiptLink();
        
        logger.info("*** Step 3 Action: Enter valid Payment Facility ID and Payment User ID.");
        AccnDailyReceipt expectedAccnDailyReceipt = setValidDataCreateNewDailyReceipt(accnPmt);
        
        logger.info("*** Step 3 Expected Results: Data are displayed correctly.");
        verifyCreateNewDailyReceiptSectionIsDisplayCorrectlyData(expectedAccnDailyReceipt, actHeader, actDailyReceiptSummarySection);
        
        logger.info("*** Step 4 Action: Click on Get Payments button.");
        clickOnGetPaymentBtn();
       
        logger.info("*** Step 4 Expected Results: Detail Daily Receipt screen is displayed correct: Header data, Daily Receipt Details section data.");
        Header expHeader = getHeaderValues(CREATE_NEW);
        assertEquals(actHeader.toString(), expHeader.toString(), "        Header is diplayed correct data");
        verifyDailyReceiptDetailsTableDisplayedCorrectly(expHeader.getDailyReceiptId(), actHeader.getPaymentFacilityId(), actHeader.getPaymentUserId(), CREATE_NEW);
        verifyDailyReceiptSummarySectionDisplayedCorrectData(actDailyReceiptSummarySection);
        
        logger.info("*** Step 5 Action: Input all required fields.");
        actDailyReceiptSummarySection = setValuesAtRequireFieldInDailyReceiptSummarySection();
        
        logger.info("*** Step 5 Expected Results: Data are displayed correctly.");
        DailyReceiptSummarySection expDailyReceiptSummarySection = getDailyReceiptSummarySectionValue();
        assertEquals(actDailyReceiptSummarySection.toString(), expDailyReceiptSummarySection.toString(),"        Data are displayed correctly.");
        
        logger.info("*** Step 6 Action: Click Save And Clear button.");
        clickOnSaveAndClearBtn();
        
        logger.info("*** Step 6 Expected Results: Created New Daily Receipt popup is displayed: "
        		+ "- New Daily Receipt is created successfully. "
        		+ "- New DLY_RECEIPT record is created. "
        		+ "- New record is created in table DEP to show total deposit amount. "
        		+ "- New record is created in table DEP_BATCH to show full amount with DEP_BATCH.FK_PYR_ID equal to DATA_VALUE in sql (1). "
        		+ "- One row in DEP_BATCH_SEQ per ACCN_PMT record is created. "
        		+ "- The loaded ACCN_PMT records are updated.");
        assertEquals(CREATED_NEW_DAILY_RECEIPT_POPUP_TITLE, dailyReceipt.createdNewDailyReceiptPopupTitleTxt().getText(), "        Created New Daily Receipt Popup, Title Txt is displayed corectly.");
        clickHiddenPageObject(dailyReceipt.createdNewDailyReceiptPopupCloseIco(), 0);        
        dlyRcpt = verifyDataIsSaveInDlyRecriptTbl(actHeader, actDailyReceiptSummarySection, REQUIRE_FIELDS);
        dep = verifyDataIsSaveInDepTbl(actHeader.getPaymentFacilityId(), actDailyReceiptSummarySection, actHeader);
        depBatch = verifyDataIsSaveInDepBatchTbl(dep.getDepId(), actDailyReceiptSummarySection, actHeader);
        depBatchSeq = verifyDataIsSaveInDepBatchSeqTbl(dlyRcpt, dep.getDepId(), depBatch.getBatchId(), actHeader);
        verifyDataIsUpdateInAccnPmtTbl(depBatchSeq, dlyRcpt, depBatch, dep, actHeader) ;
        
        logger.info("*** Step 7 Action: Reload deposit Id at step 2.");
        setValueToDailyReceiptIDInput(actHeader, actDailyReceiptSummarySection, RELOAD);
       
        logger.info("*** Step 7 Expected Results: Data are displayed correctly.");
        veifyHeaderDisplayedCorrectData(actHeader, EMPTY);
        
        logger.info("*** Step 8 Action: Click on Reset btn.");
        clickOnResetBtn();
        
        logger.info("*** Step 8 Expected Results:  Load Daily Receipt page is displayed.");
        verifyDailyReceiptLoadPageIsDisplayed();
        
        driver.quit();
    }

	@Test(priority = 1, description = "Verify creating new Daily Receipt with input full fields")
	public void testXPR_1682() throws Throwable {
	    logger.info("===== Testing - testXPR_1682 =====");
	    
	    dailyReceipt = new DailyReceipt(driver);
	    randomCharacter = new RandomCharacter(driver);
	    
	    logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
	    verifyDailyReceiptLoadPageIsDisplayed();
	    
	    logger.info("*** Step 2 Action: Click on Create New Daily Receipt link");
	    prepareData(true);
	    clickHiddenPageObject(dailyReceipt.loadPageCreateNewDailyReceiptLnk(), 0);
	    
	    logger.info("*** Step 2 Expected Results: Payment Facility ID input and Payment Facility Name are displayed.Payyment User ID and Payment User Name are displayed.");
	    verifyCreateNewDailyReceiptSectionIsDisplay();
	    
	    logger.info("*** Step 3 Action: Enter valid Payment Facility ID and Payment User ID");
	    AccnDailyReceipt expectedAccnDailyReceipt = setValidDataCreateNewDailyReceipt(accnPmt);
	    
	    logger.info("*** Step 3 Expected Results: Data are displayed correctly.");
	    Header actualHeader = new Header();
	    DailyReceiptSummarySection actualDailyReceiptSummarySection = new DailyReceiptSummarySection();
	    verifyCreateNewDailyReceiptSectionIsDisplayCorrectlyData(expectedAccnDailyReceipt, actualHeader, actualDailyReceiptSummarySection);
	    
	    logger.info("*** Step 4 Action: Click on Get Payments button");
	    clickOnGetPaymentBtn();
	    
	    logger.info("*** Step 4 Expected Results: Detail Daily Receipt screen is displayed correct:"
	    		+ " - Header data."
	    		+ " - Daily Receipt Details section data.");
	    Header expHeader = getHeaderValues(CREATE_NEW);
	    assertEquals(actualHeader.toString(), expHeader.toString(), "        Header is diplayed correct data");
	    verifyDailyReceiptDetailsTableDisplayedCorrectly(expHeader.getDailyReceiptId(), actualHeader.getPaymentFacilityId(),actualHeader.getPaymentUserId(), CREATE_NEW);
	    verifyDailyReceiptSummarySectionDisplayedCorrectData(actualDailyReceiptSummarySection);
	    
	    logger.info("*** Step 5 Expected Results: Input all fields ");
	    setValuesAllFieldsInDailyReceiptSummarySection(actualDailyReceiptSummarySection);
	    
	    logger.info("*** Step 5 Action: Data are displayed correctly");
	    verifyDailyReceiptSummarySectionDisplayedCorrectData(actualDailyReceiptSummarySection);
	    
	    logger.info("*** Step 6 Action: Click Save And Clear button.");
	    clickOnSaveAndClearBtn();
	    
	    logger.info("*** Step 6 Expected Results: Created New Daily Receipt popup is displayed. "
	    		+ "- New Daily Receipt is created successfully. "
	    		+ "- New DLY_RECEIPT record is created. "
	    		+ "- New record is created in table DEP to show total deposit amount. "
	    		+ "- New record is created in table DEP_BATCH to show full amount with DEP_BATCH.FK_PYR_ID equal to DATA_VALUE in sql (1). "
	    		+ "- One row in DEP_BATCH_SEQ per ACCN_PMT record is created. "
	    		+ "- The loaded ACCN_PMT records are updated.");
	    assertEquals(CREATED_NEW_DAILY_RECEIPT_POPUP_TITLE, dailyReceipt.createdNewDailyReceiptPopupTitleTxt().getText(), "        Created New Daily Receipt Popup, Title Txt is displayed corectly.");
	    clickHiddenPageObject(dailyReceipt.createdNewDailyReceiptPopupCloseIco(), 0);
	    dlyRcpt = verifyDataIsSaveInDlyRecriptTbl(actualHeader, actualDailyReceiptSummarySection, FULL_FIELDS);
	    dep = verifyDataIsSaveInDepTbl(actualHeader.getPaymentFacilityId(), actualDailyReceiptSummarySection, actualHeader);
	    depBatch = verifyDataIsSaveInDepBatchTbl(dep.getDepId(), actualDailyReceiptSummarySection, actualHeader);
	    depBatchSeq = verifyDataIsSaveInDepBatchSeqTbl(dlyRcpt, dep.getDepId(),depBatch.getBatchId(), actualHeader);
	    verifyDataIsUpdateInAccnPmtTbl(depBatchSeq, dlyRcpt, depBatch, dep, actualHeader) ;
	    
	    logger.info("*** Step 7 Action: Reload deposit Id at step 2.");
	    setValueToDailyReceiptIDInput(actualHeader, actualDailyReceiptSummarySection, RELOAD);
	
	    logger.info("*** Step 7 Expected Results: Data are displayed correctly.");
	    veifyHeaderDisplayedCorrectData(actualHeader, EMPTY);
	    
	    logger.info("*** Step 8 Action: Click on Reset btn.");
	    clickOnResetBtn();
	    
	    logger.info("*** Step 8 Expected Results:  Load Daily Receipt page is displayed.");
	    verifyDailyReceiptLoadPageIsDisplayed();
	    
	    driver.quit();
	}

	@Test(priority = 1, description = "Verify Reset button")
	public void testXPR_1683() throws Throwable {
	    logger.info("===== Testing - testXPR_1683 =====");

	    dailyReceipt = new DailyReceipt(driver);
	    randomCharacter = new RandomCharacter(driver);
	    
	    logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
	    verifyDailyReceiptLoadPageIsDisplayed();
	    
	    logger.info("*** Step 2 Action: Load valid Daily Receipt ID by sql (1)");
	    Header expHeader = new Header();
	    DailyReceiptSummarySection expDailyReceiptSummarySection = new DailyReceiptSummarySection();
	    setValueToDailyReceiptIDInput(expHeader, expDailyReceiptSummarySection, EMPTY);
	    
	    logger.info("*** Step 2 Expected Results: Detail Daily Receipt screen is displayed correct: Header data. Daily Receipt Summary section data. Daily Receipt Details section data.");
	    veifyHeaderDisplayedCorrectData(expHeader, EMPTY);
	    verifyDailyReceiptSummarySectionDisplayedCorrectData(expDailyReceiptSummarySection);
	    verifyDailyReceiptDetailsTableDisplayedCorrectly(expHeader.getDailyReceiptId(), null, null, EMPTY);
	    
	    logger.info("*** Step 3 Action: Click on Reset button");
	    clickOnResetBtn();
	    xifinPortalUtils.deleteAccnPmt(accnPmt);
        xifinPortalUtils.deleteDlyReceipt(dlyRcpt); 
        xifinPortalUtils.deleteDepBatchSeq(depBatchSeq);
        xifinPortalUtils.deleteDepBatch(depBatch);
        xifinPortalUtils.deleteDep(dep);
        xifinPortalUtils.deleteAccn(accn);
	    
	    logger.info("*** Step 3 Expected Results: Load Daily Receipt page is displayed.");
	    verifyDailyReceiptLoadPageIsDisplayed();
	    
	    logger.info("*** Step 4 Action: Click on Create New Daily Receipt link");
	    prepareData(true);
	    clickHiddenPageObject(dailyReceipt.loadPageCreateNewDailyReceiptLnk(),0);
	    
	    logger.info("*** Step 4 Expected Results: Payment Facility ID input and Payment Facility Name are displayed. Payment User ID and Payment User Name are displayed.");
	    verifyDailyReceiptSectionWhenClickOnCreateNewDailyReceiptLink();
	    
	    logger.info("*** Step 5 Action: Enter valid Payment Facility ID and Payment User ID");
	    AccnDailyReceipt expectedAccnDailyReceipt = setValidDataCreateNewDailyReceipt(accnPmt);
	    
	    logger.info("*** Step 5 Expected Results: Data are displayed correctly");
	    verifyCreateNewDailyReceiptSectionIsDisplayCorrectlyData(expectedAccnDailyReceipt, expHeader, expDailyReceiptSummarySection);
	    
	    logger.info("*** Step 6 Action: Click on Reset button");
	    clickOnResetBtn();
	    xifinPortalUtils.deleteAccnPmt(accnPmt);
	    xifinPortalUtils.deleteAccn(accn);
	    
	    logger.info("*** Step 6 Expected Results: Load Daily Receipt page is displayed.");
	    verifyDailyReceiptLoadPageIsDisplayed();
	    
	    logger.info("*** Step 7 Action: Click on Create New Daily Receipt link");
	    prepareData(true);
	    clickHiddenPageObject(dailyReceipt.loadPageCreateNewDailyReceiptLnk(),0);
	
	    logger.info("*** Step 7 Expected Results: Payment Facility ID input and Payment Facility Name are displayed. Payment User ID and Payment User Name are displayed.");
	    verifyDailyReceiptSectionWhenClickOnCreateNewDailyReceiptLink();
	    
	    logger.info("*** Step 8 Action: Enter valid Payment Facility ID and Payment User ID");
	    AccnDailyReceipt expAccnDailyReceipt = setValidDataCreateNewDailyReceipt(accnPmt);
	    
	    logger.info("*** Step 8 Expected Results: Data are displayed correctly");
	    Header actHeader = new Header();
	    DailyReceiptSummarySection actualDailyReceiptSummarySection = new DailyReceiptSummarySection();
	    verifyCreateNewDailyReceiptSectionIsDisplayCorrectlyData(expAccnDailyReceipt, actHeader, actualDailyReceiptSummarySection);
	    
	    logger.info("*** Step 9 Action: Click on Get Payments button");
	    clickOnGetPaymentBtn();
	    
	    logger.info("*** Step 9 Expected Results: Detail Daily Receipt screen is displayed correct: Header data. Daily Receipt Details section data.");
	    veifyHeaderDisplayedCorrectData(actHeader, CREATE_NEW);
	    verifyDailyReceiptSummarySectionDisplayedCorrectData(actualDailyReceiptSummarySection);
	    
	    logger.info("*** Step 10 Action: Input all required fields");
	    setValuesAllFieldsInDailyReceiptSummarySection(actualDailyReceiptSummarySection);
	    
	    logger.info("*** Step 10 Expected Results: Data are displayed correctly");
	    verifyDailyReceiptSummarySectionDisplayedCorrectData(actualDailyReceiptSummarySection);
	    
	    logger.info("*** Step 11 Action: Click on Reset button");
	    clickOnResetBtn();
	    
	    logger.info("*** Step 11 Expected Results: A warning popup is displayed with 'Changes have been made to this daily receipt. Are you sure you want to reset the page?' message.");
	    assertEquals(dailyReceipt.warningPopupWarningTxt().getText(), WARNING_POPUP_MESSAGE_RESET_DIRTY_DATA, "        Warning popup displayed correctly data");
	    
	    logger.info("*** Step 12 Action: Click on Reset button on warning popup");
	    clickHiddenPageObject(dailyReceipt.warningPopupResetBtn(), 0);
	    
	    logger.info("*** Step 12 Expected Results: Load Daily Receipt page is displayed. Data is not saved in DB");
	    verifyDailyReceiptLoadPageIsDisplayed();
	    verifyDataNotSaveInDb(actHeader);
	    
	    driver.quit();
	}

	@Test(priority = 1, description = "EP Dunning Letter - Verify help icons")
    public void testXPR_1684() throws Throwable {
        logger.info("===== Testing - testXPR_1684 =====");
        dailyReceipt = new DailyReceipt(driver);
        randomCharacter = new RandomCharacter();
        
        logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
        verifyDailyReceiptLoadPageIsDisplayed();
        
        logger.info("*** Step 2 Action: Input invalid Daily Receipt Id");
        String invalidDailyReceiptId = randomCharacter.getRandomAlphaNumericString(10);
	    enterValues(dailyReceipt.loadPageDailyReceiptIDInput(), invalidDailyReceiptId);
        
        logger.info("*** Step 2 Expected Results: Error message displays: Invalid Daily Receipt ID. 'XXX' does not exist.");
	    assertEquals(dailyReceipt.loadPageMessageTxt().getText(),"Invalid Daily Receipt ID. '" + invalidDailyReceiptId + "' does not exist." , "        Error message displays: Invalid Daily Receipt ID. 'XXX' does not exist is displayed correctly");
        
        driver.quit();
    }
	
	@Test(priority = 1, description = "Verify loading Daily Receipt does not exist in DB")
	public void testXPR_1685() throws Throwable {
	    logger.info("===== Testing - testXPR_1685 =====");
	    dailyReceipt = new DailyReceipt(driver);
	    randomCharacter = new RandomCharacter(driver);
	    
	    logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
	    verifyDailyReceiptLoadPageIsDisplayed();
	    
	    logger.info("*** Step 2 Action: Input invalid Daily Receipt Id.");
	    String invalidDailyReceiptID = randomCharacter.getRandomAlphaNumericString(4);
	    enterValues(dailyReceipt.loadPageDailyReceiptIDInput(), invalidDailyReceiptID );
	    
	    logger.info("*** Step 2 Expected Results: Error message displays: Error message displays: 'Invalid Daily Receipt ID. 'XXX' does not exist.'.");
		String errMessage = ERROR_MESSAGE_INVALID_DAILY_RECEIPT_ID + " '" + invalidDailyReceiptID + "' does not exist.";
	    assertEquals(errMessage, dailyReceipt.loadPageDailyErrorMessageTxt().getText().trim(), "        At load Page Daily, Error Message Txt is displayed correctly content.");
	    
	    driver.quit();
	}

	@Test(priority = 1, description = "Verify error message when 'Cash counted at drawer opening by' field is not inputted")
    public void testXPR_1686() throws Throwable {
		logger.info("===== Testing - testXPR_1686 =====");
        dailyReceipt = new DailyReceipt(driver);
        randomCharacter = new RandomCharacter(driver);
        
        logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
        verifyDailyReceiptLoadPageIsDisplayed();
        
        logger.info("*** Step 2 Action: Click on Create New Daily Receipt link");
        prepareData(true);
        clickHiddenPageObject(dailyReceipt.loadPageCreateNewDailyReceiptLnk(), 0);
        
        logger.info("*** Step 2 Expected Results: Payment Facility ID input and Payment Facility Name are displayed.Payyment User ID and Payment User Name are displayed.");
        verifyCreateNewDailyReceiptSectionIsDisplay();
        
        logger.info("*** Step 3 Action: Enter valid Payment Facility ID and Payment User ID");
        AccnDailyReceipt expectedAccnDailyReceipt = setValidDataCreateNewDailyReceipt(accnPmt);
        
        logger.info("*** Step 3 Expected Results: Data are displayed correctly.");
        Header actualHeader = new Header();
        DailyReceiptSummarySection actualDailyReceiptSummarySection = new DailyReceiptSummarySection();
        verifyCreateNewDailyReceiptSectionIsDisplayCorrectlyData(expectedAccnDailyReceipt, actualHeader, actualDailyReceiptSummarySection);
        
        logger.info("*** Step 4 Action: Click on Get Payments button");
        clickOnGetPaymentBtn();
        
        logger.info("*** Step 4 Expected Results: Detail Daily Receipt screen is displayed correct:"
        		+ " - Header data."
        		+ " - Daily Receipt Details section data.");
        Header expHeader = getHeaderValues(CREATE_NEW);
        assertEquals(actualHeader.toString(), expHeader.toString(), "        Header is diplayed correct data");
        verifyDailyReceiptSummarySectionDisplayedCorrectData(actualDailyReceiptSummarySection);
        
        logger.info("*** Step 5 Action: Enter required field except 'Cash counted at drawer opening by' field");
  		enterValues(dailyReceipt.dailyReceiptSummarySectionCashCountedAtDrawerOpeningByInput(), EMPTY);
      		
        logger.info("*** Step 5 Expected Results: Data are displayed correctly.");
        assertEquals(EMPTY, dailyReceipt.dailyReceiptSummarySectionCashCountedAtDrawerOpeningByInput().getAttribute("value"), "        Data are displayed correctly.");
        
        logger.info("*** Step 6 Action: Click Save And Clear button.");
        clickOnSaveAndClearBtn();
        
        logger.info("*** Step 6 Expected Results: Error messages displays: 'You must record who opened the cash drawer'");
        String actErrorMessage = dailyReceipt.errorsReturnedMessageTxt().get(0).getText();
        assertEquals(actErrorMessage, YOU_MUST_RECORD_WHO_OPENED_THE_CASH_DRAWER_ERROR_MESSAGE, "        error message is displayed correctly.");
        
        logger.info("*** Step 9 Action: Click on Reset btn.");
        clickOnResetBtn();
        
        logger.info("*** Step 9 Expected Results: Load [Daily Receipt] screen is displayed.");
        verifyDailyReceiptLoadPageIsDisplayed();

        driver.quit();
    }

	@Test(priority = 1, description = "Verify correct error msg displays when load a Daily Receipt which doesn't have access based on facility")
    public void testXPR_1687() throws Throwable {
        logger.info("===== Testing - testXPR_1687 =====");
        dailyReceipt = new DailyReceipt(driver);
        
        logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
        verifyDailyReceiptLoadPageIsDisplayed();
        
        logger.info("*** Step 2 Action: Prepare data with User access denied for facility.");
        dlyRcpt = createDlyRcpt(false);
        enterValues(dailyReceipt.loadPageDailyReceiptIDInput(), dlyRcpt.getAbbrv());
        
        logger.info("*** Step 2 Expected Results: Error message displays: 'User access denied for facility'");
        SystemSetting ss = systemDao.getSystemSetting(SystemSettingMap.SS_SECURITY_BY_FACILITY);
		boolean val = Convert.toBoolean(ss.getDataValue());
		if (val) {
			String actErrMsg = dailyReceipt.loadPageDailyErrorMessageTxt().getText().trim();
			assertEquals(actErrMsg, ERROR_MESSAGE_ACCESS_DENIED_BY_FAC , "        Error message is displayed correctly");
		}

        driver.quit();
    }

	@Test(priority = 1, description = "Verify error message displays when 'Please explain any overages or shortage' is empty ")
    public void testXPR_1688() throws Throwable {
        logger.info("===== Testing - testXPR_1688 =====");
        
        dailyReceipt = new DailyReceipt(driver);
        randomCharacter = new RandomCharacter(driver);
        Header actHeader = new Header();
        DailyReceiptSummarySection actDailyReceiptSummarySection = new DailyReceiptSummarySection();
        
        logger.info("*** Step 1 Expected Results: User login success. Load Daily Receipt page is displayed.");
        verifyDailyReceiptLoadPageIsDisplayed();
        
        logger.info("*** Step 2 Action: Click on Create New Daily Receipt link");
        prepareData(true);
        clickHiddenPageObject(dailyReceipt.loadPageCreateNewDailyReceiptLnk(),0);
        
        logger.info("*** Step 2 Expected Results: Payment Facility ID input and Payment Facility Name are displayed. Payment User ID and Payment User Name are displayed.");
        verifyDailyReceiptSectionWhenClickOnCreateNewDailyReceiptLink();
        
        logger.info("*** Step 3 Action: Enter valid Payment Facility ID and Payment User ID");
        AccnDailyReceipt expectedAccnDailyReceipt = setValidDataCreateNewDailyReceipt(accnPmt);
        
        logger.info("*** Step 3 Expected Results: Data are displayed correctly.");
        verifyCreateNewDailyReceiptSectionIsDisplayCorrectlyData(expectedAccnDailyReceipt, actHeader, actDailyReceiptSummarySection);
        
        logger.info("*** Step 4 Action: Click on Get Payments button");
        clickOnGetPaymentBtn();
        
        logger.info("*** Step 4 Expected Results: Detail Daily Receipt screen is displayed correct: Header data. Daily Receipt Details section data.");
        veifyHeaderDisplayedCorrectData(actHeader, CREATE_NEW);
        verifyDailyReceiptSummarySectionDisplayedCorrectData(actDailyReceiptSummarySection);
        
        logger.info("*** Step 5 Action: Enter value to all fields except Please explain any overages or shortages filed.");
        setValuesAllFieldsInDailyReceiptSummarySection(actDailyReceiptSummarySection);
        dailyReceipt.dailyReceiptSummarySectionPlsExplainAnyOveragesShortagesInput().clear();
        actDailyReceiptSummarySection.setPlsExplainAnyOveragesShortages(EMPTY);
        
        logger.info("*** Step 5 Expected Results: Data are displayed correctly.");
        verifyDailyReceiptSummarySectionDisplayedCorrectData(actDailyReceiptSummarySection);
        
        logger.info("*** Step 6 Action: Click Save And Clear button");
        clickOnSaveAndClearBtn();
        
        logger.info("*** Step 6 Expected Results: Error message displays: 'A explanation of why the cash is over/under is required.'");
        assertEquals(dailyReceipt.errorsReturnedMessageTxt().get(0).getText(), ERROR_MESSAGE_RETURNED_MISSING_EXPLAIN_ANY_OVERAGES, "        Errors returned message displayed correctly");
        
        logger.info("*** Step 7 Action: Click on Reset button.");
        clickOnResetBtn();
        clickHiddenPageObject(dailyReceipt.warningPopupResetBtn(), 0);

        logger.info("*** Step 7 Expected Results: Load Daily Receipt page is displayed.");
        verifyDailyReceiptLoadPageIsDisplayed();

        driver.quit();
    }

	/***
	 * Methods ====================================================================================================================
	 */

	/**
	 * prepare data for 2 tables: Accn and Accn_Pmt
	 * @param isAllowAccess : False: the login user access denied with FacId otherwise access is allowed
	 * @throws Exception
	 */
	private void prepareData(boolean isAllowAccess) throws Exception {
		SystemSetting ss = systemDao.getSystemSetting(SystemSettingMap.SS_SECURITY_BY_FACILITY);
		boolean val = Convert.toBoolean(ss.getDataValue());
		String userId = null;
		Cln cln = null;

		if (val) {
			userId = this.userName;
			UserFacAccess actUserFacAccess = usersDao.getRandomUserFacAccessByUserId(userId);
			if (isAllowAccess) { 
				cln = clientDao.getRandomClnByOrderingFacId(actUserFacAccess.getFacId());
			} else { // using for tc 1687
				cln = clientDao.getRandomClnByNotEqualsOrderingFacId(actUserFacAccess.getFacId());
				userFacAccess = xifinPortalUtils.createUserFacAccess(cln.getOrderingFacId(), userId);
			}
		} else {
			cln = clientDao.getRandomClnHasValidNpi();
		}

		accn = xifinPortalUtils.createAccn(cln.getClnId());
        accnPmt = xifinPortalUtils.createAccnPmt(accn.getAccnId(), PmtMap.PMT_TYP_CASH, cln.getOrderingFacId(), userId);
	}

	private AccnDailyReceipt setValidDataCreateNewDailyReceipt(AccnPmt actAccnPmt) throws Exception {
		Fac fac = rpmDao.getFacByFacId(testDb, actAccnPmt.getPmtFacId());
		
		AccnDailyReceipt actualAccnDailyReceipt = new AccnDailyReceipt();
		actualAccnDailyReceipt.setFacId(fac.getFacId());
		actualAccnDailyReceipt.setFacAbbrev(fac.getAbbrv());
		actualAccnDailyReceipt.setUserId(actAccnPmt.getUserId());
		actualAccnDailyReceipt.setPmtFacId(actAccnPmt.getPmtFacId());

		enterValues(dailyReceipt.loadPagePaymentFacilityIdInput(), actualAccnDailyReceipt.getFacAbbrev());
        enterValues(dailyReceipt.loadPagePaymentUserIdInput(), actualAccnDailyReceipt.getUserId());
		
		return actualAccnDailyReceipt;
	}

	private DailyReceiptSummarySection setValuesAtRequireFieldInDailyReceiptSummarySection() throws Exception{
		DailyReceiptSummarySection dailyReceiptSummarySection = new DailyReceiptSummarySection();
		String cashDrawerOpenedBy = usersDao.getRandomUsersByNotDeletedAndNotSystem().getUserId();
	    String cashDrawerClosedBy = usersDao.getRandomUsersByNotDeletedAndNotSystem().getUserId();
	    String cashCountedAtDrawerClosing = dailyReceipt.dailyReceiptSummarySectionTotalsCashInput().getAttribute("value");

        enterValues(dailyReceipt.dailyReceiptSummarySectionCashCountedAtDrawerOpeningByInput(), cashDrawerOpenedBy);
        enterValues(dailyReceipt.dailyReceiptSummarySectionCashCountedAtDrawerClosingByInput(), cashDrawerClosedBy);
        enterValues(dailyReceipt.dailyReceiptSummarySectionCashAtDrawerClosingInput(), cashCountedAtDrawerClosing);

		String cashCountedAtDrawerOpening = dailyReceipt.dailyReceiptSummarySectionCashAtDrawerOpeningInput().getAttribute("value");
		String totalCash = dailyReceipt.dailyReceiptSummarySectionTotalsCashInput().getAttribute("value");
		String totalCheck = dailyReceipt.dailyReceiptSummarySectionTotalCheckInput().getAttribute("value");
		
		dailyReceiptSummarySection.setCashDrawerOpenedBy(cashDrawerOpenedBy);
		dailyReceiptSummarySection.setCashDrawerClosedBy(cashDrawerClosedBy);
		dailyReceiptSummarySection.setCashCountedAtDrawerClosing(Float.parseFloat(cashCountedAtDrawerClosing.replace(",", EMPTY)));
		dailyReceiptSummarySection.setCashCountedAtDrawerOpening(Float.parseFloat(cashCountedAtDrawerOpening.replace(",", EMPTY)));
		dailyReceiptSummarySection.setTotalCash(Float.parseFloat(totalCash.replace(",", EMPTY)));
		dailyReceiptSummarySection.setTotalCheck(Float.parseFloat(totalCheck.replace(",", EMPTY)));
		
		return dailyReceiptSummarySection;
	}

	/**
	 * Create a daily receipt 
	 * @param isAllowAccess : False: the login user access denied with FacId otherwise access is allowed
	 * @return
	 * @throws Exception
	 */
	private DlyRcpt createDlyRcpt(boolean isAllowAccess) throws Exception {
		logger.info("*** Action: Click on Create New Daily Receipt link");
	    clickHiddenPageObject(dailyReceipt.loadPageCreateNewDailyReceiptLnk(), 0);
	    
	    logger.info("*** Expected Results: Payment Facility ID input and Payment Facility Name are displayed.Payyment User ID and Payment User Name are displayed.");
	    verifyCreateNewDailyReceiptSectionIsDisplay();
	    
	    logger.info("*** Action: Enter valid Payment Facility ID and Payment User ID");
        prepareData(isAllowAccess);
	    AccnDailyReceipt expectedAccnDailyReceipt = setValidDataCreateNewDailyReceipt(accnPmt);
	    
	    logger.info("*** Expected Results: Data are displayed correctly.");
	    Header actualHeader = new Header();
	    DailyReceiptSummarySection actualDailyReceiptSummarySection = new DailyReceiptSummarySection();
	    verifyCreateNewDailyReceiptSectionIsDisplayCorrectlyData(expectedAccnDailyReceipt, actualHeader, actualDailyReceiptSummarySection);
	    
	    logger.info("*** Action: Click on Get Payments button");
	    clickOnGetPaymentBtn();
	    
	    logger.info("*** Expected Results: Detail Daily Receipt screen is displayed correct:"
	    		+ " - Header data."
	    		+ " - Daily Receipt Details section data.");
	    Header expHeader = getHeaderValues(CREATE_NEW);
	    assertEquals(actualHeader.toString(), expHeader.toString(), "        Header is diplayed correct data");
	    verifyDailyReceiptDetailsTableDisplayedCorrectly(expHeader.getDailyReceiptId(), actualHeader.getPaymentFacilityId(),actualHeader.getPaymentUserId(), CREATE_NEW);
	    verifyDailyReceiptSummarySectionDisplayedCorrectData(actualDailyReceiptSummarySection);
	    
	    logger.info("*** Expected Results: Input all fields ");
	    setValuesAllFieldsInDailyReceiptSummarySection(actualDailyReceiptSummarySection);
	    
	    logger.info("*** Action: Data are displayed correctly");
	    verifyDailyReceiptSummarySectionDisplayedCorrectData(actualDailyReceiptSummarySection);
	    
	    logger.info("*** Action: Click Save And Clear button.");
	    clickOnSaveAndClearBtn();
	    
	    logger.info("*** Expected Results: Created New Daily Receipt popup is displayed. "
	    		+ "- New Daily Receipt is created successfully. "
	    		+ "- New DLY_RECEIPT record is created. "
	    		+ "- New record is created in table DEP to show total deposit amount. "
	    		+ "- New record is created in table DEP_BATCH to show full amount with DEP_BATCH.FK_PYR_ID equal to DATA_VALUE in sql (1). "
	    		+ "- One row in DEP_BATCH_SEQ per ACCN_PMT record is created. "
	    		+ "- The loaded ACCN_PMT records are updated.");
	    assertEquals(CREATED_NEW_DAILY_RECEIPT_POPUP_TITLE, dailyReceipt.createdNewDailyReceiptPopupTitleTxt().getText(), "        Created New Daily Receipt Popup, Title Txt is displayed corectly.");
	    clickHiddenPageObject(dailyReceipt.createdNewDailyReceiptPopupCloseIco(), 0);
	    
	    dlyRcpt = verifyDataIsSaveInDlyRecriptTbl(actualHeader, actualDailyReceiptSummarySection, FULL_FIELDS);
	    dep = verifyDataIsSaveInDepTbl(actualHeader.getPaymentFacilityId(), actualDailyReceiptSummarySection, actualHeader);
	    depBatch = verifyDataIsSaveInDepBatchTbl(dep.getDepId(), actualDailyReceiptSummarySection, actualHeader);
	    depBatchSeq = verifyDataIsSaveInDepBatchSeqTbl(dlyRcpt, dep.getDepId(),depBatch.getBatchId(), actualHeader);
	    verifyDataIsUpdateInAccnPmtTbl(depBatchSeq, dlyRcpt, depBatch, dep, actualHeader) ;
	    
	    return dlyRcpt;
	}

	@SuppressWarnings("deprecation")
	private void setValueToDailyReceiptIDInput(Header header, DailyReceiptSummarySection dailyReceiptSummarySection, String type) throws Exception {
		DlyRcpt actDlyRcpt = new DlyRcpt();
		if (type.equals(RELOAD)) {
			actDlyRcpt = accessionDao.getDlyRcptByAbbrv(header.getDailyReceiptId());
		} else {
			actDlyRcpt = createDlyRcpt(true);
		}
		Fac fac = rpmDao.getFacByFacId(testDb, actDlyRcpt.getPhlebFacId());
		Users user = usersDao.getUsersByUserId(actDlyRcpt.getPhlebUserId());

	    enterValues(dailyReceipt.loadPageDailyReceiptIDInput(), actDlyRcpt.getAbbrv());

	    header.setDailyReceiptId(ConvertUtil.convertNull(actDlyRcpt.getAbbrv()));
	    header.setPaymentFacilityId(ConvertUtil.convertNull(fac.getAbbrv()));
	    header.setPaymentFacilityName(ConvertUtil.replaceMultilpleSpace(fac.getName()));
	    header.setPaymentUserId(ConvertUtil.convertNull(actDlyRcpt.getPhlebUserId()));
	    header.setPaymentUserName(concatFullName(user.getFNm(), user.getLNm()));
	    header.setDepositId(actDlyRcpt.getCashCheckDepId());
	    header.setDateOfDeposit(actDlyRcpt.getCreatDt());
	    header.setBatchId(actDlyRcpt.getCashCheckBatchId());
	    header.setCreatedBy(ConvertUtil.convertNull(actDlyRcpt.getCreatUserId()));
	    
	    dailyReceiptSummarySection.setBagId(ConvertUtil.convertNull(actDlyRcpt.getBagId()));
	    dailyReceiptSummarySection.setDrawerId(ConvertUtil.convertNull(actDlyRcpt.getDrwrId()));
	    dailyReceiptSummarySection.setCashCountedAtDrawerOpening(Float.parseFloat(LOAD_EXISTED_DEFAULT_CASH_AT_DRAWER_OPENING));
	    dailyReceiptSummarySection.setCashDrawerOpenedBy(ConvertUtil.convertNull(actDlyRcpt.getCashDrwrOpenedBy()));
	    dailyReceiptSummarySection.setCashCountedAtDrawerClosing(actDlyRcpt.getCashAtClose());
	    dailyReceiptSummarySection.setCashDrawerClosedBy(ConvertUtil.convertNull(actDlyRcpt.getCashDrwrClosedBy()));
	    dailyReceiptSummarySection.setVerifiedByUser(ConvertUtil.convertNull(actDlyRcpt.verifiedByUser));
	    float totalCash = accessionDao.getSumPaidAmtFromAccnPmtByDlyRcptId(actDlyRcpt.getDlyRcptId());
	    dailyReceiptSummarySection.setTotalCash(totalCash);
	    float totalCheck = accessionDao.getSumPaidAmtFromAccnPmtByDlyRcptIdAndPmtTypId(actDlyRcpt.getDlyRcptId(), PmtMap.PMT_TYP_CHECK); 
	    dailyReceiptSummarySection.setTotalCheck(totalCheck);
	    dailyReceiptSummarySection.setCashOverUnder(actDlyRcpt.getTotCashOverUnder());
	    dailyReceiptSummarySection.setCashAcctUseOnly(actDlyRcpt.getTotCashAccntngUse() == null ? EMPTY : actDlyRcpt.getTotCashAccntngUse().trim());
	    dailyReceiptSummarySection.setCheckAcctUseOnly(actDlyRcpt.getTotCheckAccntngUse() == null ? EMPTY : actDlyRcpt.getTotCashAccntngUse().trim());
	    dailyReceiptSummarySection.setPlsExplainAnyOveragesShortages(ConvertUtil.convertNull(actDlyRcpt.getOverUnderCmnt()));
	}


	private void setValuesAllFieldsInDailyReceiptSummarySection(DailyReceiptSummarySection dailyReceiptSummarySection) throws Exception {
		randomCharacter = new RandomCharacter();
		
		String bagId = randomCharacter.getRandomAlphaString(5);
		String drawerId = randomCharacter.getRandomAlphaString(5);
		String cashDrawerOpenedBy = usersDao.getRandomUsersByNotDeletedAndNotSystem().getUserId();
	    String cashDrawerClosedBy = usersDao.getRandomUsersByNotDeletedAndNotSystem().getUserId();
	    String overagesOrShortages = randomCharacter.getRandomAlphaString(20);
	    
	   	float cashCountedAtDrawerClosing = Float.parseFloat(dailyReceipt.dailyReceiptSummarySectionTotalsCashInput().getAttribute("value")) + Float.parseFloat(randomCharacter.getRandomNumericString(3));
	   	float over = cashCountedAtDrawerClosing - Float.parseFloat(dailyReceipt.dailyReceiptSummarySectionTotalsCashInput().getAttribute("value"));

        enterValues(dailyReceipt.dailyReceiptSummarySectionBagIdInput(), bagId);
        enterValues(dailyReceipt.dailyReceiptSummarySectionDrawerIdInput(), drawerId);
        enterValues(dailyReceipt.dailyReceiptSummarySectionCashCountedAtDrawerOpeningByInput(), cashDrawerOpenedBy);
        enterValues(dailyReceipt.dailyReceiptSummarySectionCashCountedAtDrawerClosingByInput(), cashDrawerClosedBy);
        enterValues(dailyReceipt.dailyReceiptSummarySectionCashAtDrawerClosingInput(), String.valueOf(cashCountedAtDrawerClosing * 10 ));
        enterValues(dailyReceipt.dailyReceiptSummarySectionTotalsCashOverUnderInput(), String.valueOf(over * 10));
        enterValues(dailyReceipt.dailyReceiptSummarySectionPlsExplainAnyOveragesShortagesInput(), overagesOrShortages);
		
		dailyReceiptSummarySection.setBagId(bagId);
		dailyReceiptSummarySection.setDrawerId(drawerId);
		dailyReceiptSummarySection.setCashDrawerOpenedBy(cashDrawerOpenedBy);
		dailyReceiptSummarySection.setCashDrawerClosedBy(cashDrawerClosedBy);
		dailyReceiptSummarySection.setCashCountedAtDrawerClosing(cashCountedAtDrawerClosing);
		dailyReceiptSummarySection.setCashOverUnder(over);
		dailyReceiptSummarySection.setPlsExplainAnyOveragesShortages(overagesOrShortages);
	}
	
	private void clickOnSaveAndClearBtn() throws Exception {
		clickHiddenPageObject(dailyReceipt.footerSaveAndClearBtn(), 0);
		xifinPortalUtils.waitForPageLoaded(wait);
	}

	private void clickOnResetBtn() throws Exception {
		clickHiddenPageObject(dailyReceipt.footerResetBtn(), 0);
		xifinPortalUtils.waitForPageLoaded(wait);
	}

	private void clickOnGetPaymentBtn() throws Exception {
		clickOnElement(dailyReceipt.loadPageGetPaymentsBtn());
		xifinPortalUtils.waitForPageLoaded(wait);
	}

	private String concatFullName(final String firstName, final String lastName) {
	    if (firstName != null) {
	        return lastName + ", " + firstName;
	    } else if (lastName != null) {
	        return lastName;
	    } else {
	        return EMPTY;
	    }
	}

	/**
	 * Get total cash or total check
	 * @param accnPmts: the Accn_Pmt list
	 * @param isGetTotalCash: True is get Total Cash otherwise get Total Check
	 * @return
	 */
    @SuppressWarnings("deprecation")
	private float getTotalCashOrTotalCheck(List<AccnPmt> accnPmts, boolean isGetTotalCash) {
        float total = 0;
        for (AccnPmt accnPmt: accnPmts) {
        	if (isGetTotalCash) {
	            if (accnPmt.getPmtTypId() == PmtMap.PMT_TYP_CASH || accnPmt.getPmtTypId() == PmtMap.PMT_TYP_CASH_DISCOUNT) {
	                total += accnPmt.getPaidAmtAsMoney().floatValue();
	            }
        	} else {
        		if (accnPmt.getPmtTypId() == PmtMap.PMT_TYP_CHECK) {
	                total += accnPmt.getPaidAmtAsMoney().floatValue();
	            }
        	}
        }
        return total;
    }
    
	private void verifyDataNotSaveInDb(Header headerData) throws Exception {
	    DlyRcpt actDlyRcpt = accessionDao.getDlyRcptByAbbrv(headerData.getDailyReceiptId());
	    assertEquals(actDlyRcpt, null, "        Daily Receipt isn't saved in database");
	}

	private void verifyHelpPageIsDisplayed(String pagaUrl) throws Exception {
	    String parentWindow = switchToPopupWin();
	    assertTrue(driver.getCurrentUrl().contains(pagaUrl), "        Help page is displayed.");
	    driver.close();
	    switchToParentWin(parentWindow);
	}

	private void verifyDailyReceiptLoadPageIsDisplayed() throws Exception {
	    wait.until(ExpectedConditions.elementToBeClickable(dailyReceipt.pageTitle()));
	    assertTrue(dailyReceipt.pageTitle().getText().equalsIgnoreCase(DAILY_RECEIPT_TITLE), "        The page title should be displayed '"+DAILY_RECEIPT_TITLE+"'");
	    assertFalse(isElementHidden(dailyReceipt.dailyReceiptLoadPageDailyReceiptSection(), 5), "         Daily Receipt section at load page is displayed");
	    assertTrue(isElementEnabled(dailyReceipt.loadPageDailyReceiptIDInput(), 5, true), "        Daily Receipt section is enable");
	}

	private void verifyDailyReceiptDetailPageIsDisplayed() throws Exception {
	    wait.until(ExpectedConditions.elementToBeClickable(dailyReceipt.pageTitle()));
		assertTrue(dailyReceipt.pageTitle().getText().equalsIgnoreCase(DAILY_RECEIPT_TITLE), "        The page title should be displayed '" + DAILY_RECEIPT_TITLE + "'.");
	    assertTrue(isElementHidden(dailyReceipt.dailyReceiptLoadPageDailyReceiptSection(), 5), "         Daily Receipt section at load page is displayed.");
	    assertFalse(isElementEnabled(dailyReceipt.loadPageDailyReceiptIDInput(), 5, true), "        Daily Receipt section is enable.");
	}

	private void verifyCreateNewDailyReceiptSectionIsDisplay() throws Exception {
		assertTrue(isElementPresent(dailyReceipt.loadPagePaymentFacilityIdInput(), 5), "        Payment Facility Id inputField at loader page is displayed");
		assertTrue(isElementPresent(dailyReceipt.loadPagePaymentFacilityNameInput(), 5), "        Payment Facility Name inputField at loader page is displayed");
		assertTrue(isElementPresent(dailyReceipt.loadPagePaymentUserIdInput(), 5), "        Payment User Id inputField at loader page is displayed");
		assertTrue(isElementPresent(dailyReceipt.loadPagePaymentUserNameInput(), 5), "        Payment User Name inputField at loader page is displayed");
		assertTrue(isElementPresent(dailyReceipt.loadPageGetPaymentsBtn(), 5), "        Get Payments button at loader page is displayed");
	}

	private void veifyHeaderDisplayedCorrectData(Header actHeader, String pageType) throws Exception {
	    Header expHeader = getHeaderValues(pageType);
	    assertEquals(actHeader.toString(), expHeader.toString(), "        Header is diplayed correct data");
	}

	private void verifyDailyReceiptSectionWhenClickOnCreateNewDailyReceiptLink() throws Exception{
		assertEquals(dailyReceipt.loadPagePaymentFacilityIdInput().getText().trim(), EMPTY, "        At Load Page, Payment Facility Id Input is empty.");
		assertEquals(dailyReceipt.loadPagePaymentFacilityNameInput().getText().trim(), EMPTY, "        At Load Page, Payment Facility Name Input is empty.");
		assertEquals(dailyReceipt.loadPagePaymentUserIdInput().getText().trim(), EMPTY, "        At Load Page, Payment User Id Input is empty.");
		assertEquals(dailyReceipt.loadPagePaymentUserNameInput().getText().trim(), EMPTY, "        At Load Page, Payment User Name Input is empty.");
	}

	@SuppressWarnings("deprecation")
	private Dep verifyDataIsSaveInDepTbl(String facId,DailyReceiptSummarySection actDailyReceiptSummarySection, Header header) throws Exception{
		timeStamp = new TimeStamp();
		String currentDate = timeStamp.getCurrentDate(FORMAT_DATE);
		DateFormat formatter = new SimpleDateFormat(FORMAT_DATE);
		Date expDate = new Date(formatter.parse(currentDate).getTime());
		Dep actDep = paymentDao.getDepByFacAbbrev(facId).get(0);

		assertEquals(actDep.getNote(), NOTE_IN_DEP_TBL, "        Data is saved correctly.");
		assertEquals(actDep.getDepAmt(), actDailyReceiptSummarySection.getTotalCash(), "        Data is saved correctly.");
		assertEquals(actDep.getUserId(), header.getPaymentUserId(), "        Data is saved correctly.");
		assertEquals(actDep.getRemitDt(), expDate, "        Data is saved correctly.");
		assertEquals(actDep.getAcctngDt(), expDate, "        Data is saved correctly.");
		assertEquals(actDep.getIsCreditCard(), false, "        Data is saved correctly.");
		assertEquals(actDep.getIsPosted(), false, "        Data is saved correctly.");
		assertEquals(actDep.getFacId(), rpmDao.getFacByAbbrv(testDb, header.getPaymentFacilityId()).getFacId(), "        Data is saved correctly.");

		return actDep;
	}

	@SuppressWarnings("deprecation")
	private DepBatch verifyDataIsSaveInDepBatchTbl(int depSeq, DailyReceiptSummarySection actDailyReceiptSummarySection, Header header) throws Exception{
		String currentDate = timeStamp.getCurrentDate(FORMAT_DATE);
		DateFormat formatter = new SimpleDateFormat(FORMAT_DATE);
		String sysSS73 = systemDao.getSystemSetting(SystemSettingMap.SS_PATIENT_PYR_ID_TO_ADD).getDataValue();
		DepBatch actDepBatch = rpmDao.getDepBatchByDepId(testDb, depSeq).get(0);

		assertEquals(actDepBatch.getPyrId(),Integer.parseInt(sysSS73), "        Data is saved correctly.");
		assertEquals(actDepBatch.getUserId(),header.getPaymentUserId(), "        Data is saved correctly.");
		assertEquals(actDepBatch.getIsDeleted(), false, "        Data is saved correctly.");
		assertEquals(actDepBatch.getIsPosted(), false, "        Data is saved correctly.");
		assertEquals(actDepBatch.getLastActvDt().toString(), new Date(formatter.parse(currentDate).getTime()).toString(), "        Data is saved correctly.");
		assertEquals(actDepBatch.getApplAmtAsMoney().toString(), ConvertUtil.convertStringtoMoney(String.valueOf(actDailyReceiptSummarySection.getTotalCash()+ actDailyReceiptSummarySection.getTotalCheck())),"        Data is saved correctly.");
		assertEquals(actDepBatch.getBatchAmtAsMoney().toString(), ConvertUtil.convertStringtoMoney(String.valueOf(actDailyReceiptSummarySection.getTotalCash()+ actDailyReceiptSummarySection.getTotalCheck())),"        Data is saved correctly.");

		return actDepBatch;
	}

	/**
	 * Get all values of items into the Out Patient Center Receipt Log Table
	 * @return
	 * @throws Exception
	 */
	private List<ReceiptLog> getValuesOfOutPatientCenterReceipLogTable() throws Exception {
		List<ReceiptLog> expReceiptLogs = new ArrayList<ReceiptLog>(); 
		int totalRecords = Integer.parseInt(getTotalResultSearch(dailyReceipt.outpatientCenterReceiptLogTblTotalRecordTxt()));
	    if (totalRecords > 0) {
	        int totalPages = Integer.parseInt(dailyReceipt.outpatientCenterReceiptLogTotalPageLbl().getText());
	        while (totalPages >= 1) {
	            List<WebElement> outpatientCenterReceiptLogAllRow = dailyReceipt.outpatientCenterReceiptLogTblAllRow();

	            for (int i = 1; i <= outpatientCenterReceiptLogAllRow.size(); i++) {
					String index = String.valueOf(i + 1);

	                int pmtSeq = ConvertUtil.convertStringToInt(dailyReceipt.outpatientCenterReceiptLogTblPmtSeqTxt(index).getAttribute("title"));
	                String accnId = ConvertUtil.convertNull(dailyReceipt.outpatientCenterReceiptLogTblAccesionIdTxt(index).getText());
	                String epi = dailyReceipt.outpatientCenterReceiptLogTblEpiTxt(index).getText().trim();
	                String patientName = dailyReceipt.outpatientCenterReceiptLogTblPatientNameTxt(index).getText().trim();
	                String cashAmt = dailyReceipt.outpatientCenterReceiptLogTblCashTxt(index).getText();
	                String checkAmt = dailyReceipt.outpatientCenterReceiptLogTblCheckAmountTxt(index).getText();
	                String checkNumber = dailyReceipt.outpatientCenterReceiptLogTblCheckNumberTxt(index).getText().trim();

	                ReceiptLog receiptLog = new ReceiptLog();
	    	        receiptLog.setPmtSeq(pmtSeq);
	    	        receiptLog.setAccnId(accnId);
	    	        receiptLog.setEpi(epi);
	    	        receiptLog.setPatientFullName(patientName);
	    	        receiptLog.setCashAmount(cashAmt.equals(UNDEFINDED_NUMBER) ? EMPTY : cashAmt);
	    	        receiptLog.setCheckAmount(checkAmt);
	    	        receiptLog.setCheckNumber(checkNumber);

	    	        expReceiptLogs.add(receiptLog);
	            }

				if (totalPages > 1) {
	                clickHiddenPageObject(dailyReceipt.outpatientCenterReceiptLogTblNextPageIco(), 0);
	            }
	            totalPages--;
	        }
	    }
		return expReceiptLogs;
	}

	private void verifyDailyReceiptDetailsTableDisplayedCorrectly(String dailyReceiptId, String facAbbrv, String userId, String type) throws Exception {
	    List<ReceiptLog> actReceiptLogs = dailyReceiptUtils.mapToReceiptLog(dailyReceiptId, facAbbrv, userId, type);
	    List<ReceiptLog> expReceiptLogs = getValuesOfOutPatientCenterReceipLogTable();
	    assertEquals(actReceiptLogs, expReceiptLogs, "         Detail Daily Receipt screen is displayed correctly.");
	}

	@SuppressWarnings("deprecation")
	private DlyRcpt verifyDataIsSaveInDlyRecriptTbl(Header header,DailyReceiptSummarySection actDailyReceiptSummarySection, String type) throws Exception{
		timeStamp = new TimeStamp();
		String currentDate = timeStamp.getCurrentDate(FORMAT_DATE);
		DateFormat formatter = new SimpleDateFormat(FORMAT_DATE);
		DlyRcpt actDlyRcpt = accessionDao.getDlyRcptByAbbrv(header.getDailyReceiptId());

		assertEquals(actDlyRcpt.getPhlebUserId(), header.getPaymentUserId(), "        Data is saved correctly.");
		assertEquals(actDlyRcpt.getCashAtClose(), actDailyReceiptSummarySection.getCashCountedAtDrawerClosing(), "        Data is saved correctly.");
		assertEquals(actDlyRcpt.getCreatDt().toString(), new Date(formatter.parse(currentDate).getTime()).toString(), "        Data is saved correctly.");
		assertEquals(actDlyRcpt.getCashDrwrOpenedBy(), actDailyReceiptSummarySection.getCashDrawerOpenedBy(), "        Data is saved correctly.");
		assertEquals(actDlyRcpt.getCashDrwrClosedBy(), actDailyReceiptSummarySection.getCashDrawerClosedBy(), "        Data is saved correctly.");

		if (type.equals(FULL_FIELDS)) {
			assertEquals(actDlyRcpt.getBagId(), actDailyReceiptSummarySection.getBagId(),"        Bag Id Data is saved correctly.");
			assertEquals(actDlyRcpt.getDrwrId(), actDailyReceiptSummarySection.getDrawerId(),"        Drawer Id Data is saved correctly.");
			assertEquals(actDlyRcpt.getTotCashOverUnder(), actDailyReceiptSummarySection.getCashOverUnder(),"        Cash Over Under Id Data is saved correctly.");
			assertEquals(actDlyRcpt.getOverUnderCmnt(), actDailyReceiptSummarySection.getPlsExplainAnyOveragesShortages(),"        Over Under Cmnt Data is saved correctly.");
		}

		return actDlyRcpt;
	}

	private void verifyDailyReceiptSummarySectionDisplayedCorrectData(DailyReceiptSummarySection actDailyReceiptSummarySection) throws Exception {
	    DailyReceiptSummarySection expDailyReceiptSummarySection = getDailyReceiptSummarySectionValue();
	    assertEquals(actDailyReceiptSummarySection, expDailyReceiptSummarySection, "        Daily Receipt Summary Section data is displayed correct data");
	}

	private DepBatchSeq verifyDataIsSaveInDepBatchSeqTbl(DlyRcpt actDlyRcpt, int depSeq, int depBatchId, Header header) throws Exception{
		DepBatchSeq actDepBatchSeq = paymentDao.getDepBatchSeqByDepId(depSeq).get(0);
		AccnPmt actAccnPmt = accessionDao.getAccnPmtByDailyReceiptId(actDlyRcpt.getDlyRcptId()).get(0);

		assertEquals(actDepBatchSeq.getDepBatchId(), depBatchId, "        Data is saved correctly.");
		assertTrue(actDepBatchSeq.getAudUser().contains(userName), "        Data is saved correctly.");
		assertEquals(actDepBatchSeq.getAccnId(), actAccnPmt.getAccnId(), "        Data is saved correctly.");
		assertEquals(actDepBatchSeq.getPyrPrio(), actAccnPmt.getPmtPyrPrio(), "        Data is saved correctly.");
		assertEquals(actDepBatchSeq.getChkNum(), actAccnPmt.getChkNum(), "        Data is saved correctly.");
		assertEquals(actDepBatchSeq.getNonClientNextAction(), PmtMap.NEXT_ACTION_NO_ACTION, "        Data is saved correctly.");
		assertEquals(actDepBatchSeq.getPaidAmtAsMoney(), actAccnPmt.getPaidAmtAsMoney(), "        Data is saved correctly.");
	
		return actDepBatchSeq;
	}

	private List<AccnPmt> verifyDataIsUpdateInAccnPmtTbl(DepBatchSeq actDepBatchSeq, DlyRcpt actDlyRcpt, DepBatch actDepBatch, Dep actDep, Header header) throws Exception{
		List<AccnPmt> accnPmts = accessionDao.getAccnPmtByDailyReceiptId(actDlyRcpt.getDlyRcptId());
		for (AccnPmt accnPmt : accnPmts) {
			assertEquals(accnPmt.getDepId(),actDep.getDepId(), "        Data is saved correctly.");
			assertEquals(accnPmt.getDepBatchId(),actDepBatch.getBatchId(), "        Data is saved correctly.");
			assertEquals(accnPmt.getDepBatchSeqId(),actDepBatchSeq.getDepBatchSeqId(), "        Data is saved correctly.");
			assertEquals(accnPmt.getUserId(),header.getPaymentUserId(), "        Data is saved correctly.");
		}

		return accnPmts;
	}

	private void verifyCreateNewDailyReceiptSectionIsDisplayCorrectlyData(AccnDailyReceipt accnDailyReceipt, Header header, DailyReceiptSummarySection dailyReceiptSummarySection) throws Exception {
		String facName = rpmDao.getFacByAbbrv(testDb, accnDailyReceipt.getFacAbbrev()).getName();
		Users user = usersDao.getUsersByUserId(accnDailyReceipt.getUserId());
		String fullName = concatFullName(user.getFNm(), user.getLNm()); 

		assertEquals(accnDailyReceipt.getFacAbbrev(), dailyReceipt.loadPagePaymentFacilityIdInput().getAttribute("value"), "        Payment Facility Id inputField is displayed correctly");
		assertEquals(facName, dailyReceipt.loadPagePaymentFacilityNameInput().getAttribute("value"), "        Payment Facility Name inputField is displayed correctly");
		assertEquals(accnDailyReceipt.getUserId(), dailyReceipt.loadPagePaymentUserIdInput().getAttribute("value"), "        Payment User Id inputField is displayed correctly");
		assertEquals(fullName, dailyReceipt.loadPagePaymentUserNameInput().getAttribute("value"), "        Payment User Name inputField is displayed correctly");

		header.setDailyReceiptId(dailyReceipt.loadPageDailyReceiptIDInput().getAttribute("value"));
	    header.setPaymentFacilityId(accnDailyReceipt.getFacAbbrev());
	    header.setPaymentFacilityName(ConvertUtil.standardizeString(facName));
	    header.setPaymentUserId(accnDailyReceipt.getUserId());
	    header.setPaymentUserName(fullName);
	    dailyReceiptSummarySection.setCashCountedAtDrawerOpening(Float.parseFloat(systemDao.getSystemSetting(SystemSettingMap.SS_DLY_RCPT_CASH_AT_OPEN).getDataValue()));

	    List<AccnPmt> accnPmts;
	    String sysSS40 = systemDao.getSystemSetting(SystemSettingMap.SS_SECURITY_BY_FACILITY).getDataValue();
		if (sysSS40.equals("1")) {
	        accnPmts = accessionDao.getAccnPmtByUserIdAndFacAbbrevAndAccnIdInUserFacAccess(accnDailyReceipt.getFacAbbrev(), accnDailyReceipt.getUserId());
	    } else {
	    	accnPmts = accessionDao.getAccnPmtByUserIdAndFacAbbrev(accnDailyReceipt.getFacAbbrev(), accnDailyReceipt.getUserId());
	    }
	    float totalCash = this.getTotalCashOrTotalCheck(accnPmts, true);
	    float totalCheck = this.getTotalCashOrTotalCheck(accnPmts, false); 

	    dailyReceiptSummarySection.setTotalCash(totalCash);
	    dailyReceiptSummarySection.setTotalCheck(totalCheck);
	}

	private String getDlyRcptIdOnUIByTimeOut(int timeOut) throws Exception {
		String dlyRcptId = EMPTY;

    	for(int time = 0; time <= timeOut; time ++){
    		Thread.sleep(1000);
    		dlyRcptId = dailyReceipt.headerDailyReceiptIdTxt().getText().trim();

    		if(!dlyRcptId.equals(EMPTY)){
				break;
			}
    	}

    	return dlyRcptId;
	}

	private Header getHeaderValues(String type) throws Exception {
	    Header returnedHeader = new Header();
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	    String dlyRcptId = getDlyRcptIdOnUIByTimeOut(TIME_OUT);
	    String pmtFacId = dailyReceipt.headerPaymentFacilityIdTxt().getText().trim();
	    String pmtFacNm = dailyReceipt.headerPaymentFacilityNameTxt().getText().trim();
	    String pmtUserId = dailyReceipt.headerPaymentUserIdTxt().getText().trim();
	    String pmtUserNm = dailyReceipt.headerPaymentUserNameTxt().getText().trim();

	    returnedHeader.setDailyReceiptId(dlyRcptId);
	    returnedHeader.setPaymentFacilityId(pmtFacId);
	    returnedHeader.setPaymentFacilityName(ConvertUtil.standardizeString(pmtFacNm));
	    returnedHeader.setPaymentUserId(pmtUserId);
	    returnedHeader.setPaymentUserName(pmtUserNm.contains("null") ? EMPTY : pmtUserNm);

		if (!type.equals(CREATE_NEW)) {
	    	int depositId = ConvertUtil.convertStringToInt(dailyReceipt.headerDepositIDTxt().getText().trim());
	    	int batchId = ConvertUtil.convertStringToInt(dailyReceipt.headerBatchIdTxt().getText().trim());
	    	Date dateOfDeposit = new Date(dateFormat.parse(dailyReceipt.headerDateOfDepositTxt().getText().trim()).getTime());
	    	String createdBy = dailyReceipt.headerCreatedByTxt().getText().trim();

		    returnedHeader.setDepositId(depositId);
		    returnedHeader.setBatchId(batchId);
		    returnedHeader.setDateOfDeposit(dateOfDeposit);
		    returnedHeader.setCreatedBy(createdBy);
	    }

	    return returnedHeader;
	}

	private DailyReceiptSummarySection getDailyReceiptSummarySectionValue() throws Exception {
	    String totalCashOverUnder = defaultIfNull(dailyReceipt.dailyReceiptSummarySectionTotalsCashOverUnderInput().getAttribute("value"), EMPTY);
	    String totalAcctUse = defaultIfNull(dailyReceipt.dailyReceiptSummarySectionTotalAcctUseOnlyInput().getAttribute("value"), EMPTY);
	    String checkAcctUse = defaultIfNull(dailyReceipt.dailyReceiptSummarySectionCheckAcctUseOnlyInput().getAttribute("value"), EMPTY);
	    String bagId = defaultIfNull(dailyReceipt.dailyReceiptSummarySectionBagIdInput().getAttribute("value"), EMPTY);
	    String drawerId = defaultIfNull(dailyReceipt.dailyReceiptSummarySectionDrawerIdInput().getAttribute("value"), EMPTY);
	    float cashCountedAtDrawerOpening = ConvertUtil.convertStringToFloat(dailyReceipt.dailyReceiptSummarySectionCashAtDrawerOpeningInput().getAttribute("value"));
	    float cashCountedAtDrawerClosing = ConvertUtil.convertStringToFloat(dailyReceipt.dailyReceiptSummarySectionCashAtDrawerClosingInput().getAttribute("value"));
	    String cashDrawerOpenedBy = defaultIfNull(dailyReceipt.dailyReceiptSummarySectionCashCountedAtDrawerOpeningByInput().getAttribute("value"), EMPTY);
	    String cashDrawerClosedBy = defaultIfNull(dailyReceipt.dailyReceiptSummarySectionCashCountedAtDrawerClosingByInput().getAttribute("value"), EMPTY);
	    String verifiedByUser = defaultIfNull(dailyReceipt.dailyReceiptSummarySectionVerifiedInAccountingByInput().getAttribute("value"), EMPTY);
	    float totalCash = ConvertUtil.convertStringToFloat(dailyReceipt.dailyReceiptSummarySectionTotalsCashInput().getAttribute("value"));
	    float cashOverUnder = ConvertUtil.convertStringToFloat(totalCashOverUnder);
	    float totalCheck = ConvertUtil.convertStringToFloat(dailyReceipt.dailyReceiptSummarySectionTotalCheckInput().getAttribute("value"));
	    String plsExplainAnyOveragesShortages = defaultIfNull(dailyReceipt.dailyReceiptSummarySectionPlsExplainAnyOveragesShortagesInput().getAttribute("value"), EMPTY);
	    
	    DailyReceiptSummarySection dailyReceiptSummarySection = new DailyReceiptSummarySection();
	    dailyReceiptSummarySection.setBagId(bagId);
	    dailyReceiptSummarySection.setDrawerId(drawerId);
	    dailyReceiptSummarySection.setCashCountedAtDrawerOpening(cashCountedAtDrawerOpening);
	    dailyReceiptSummarySection.setCashCountedAtDrawerClosing(cashCountedAtDrawerClosing);
	    dailyReceiptSummarySection.setCashDrawerOpenedBy(cashDrawerOpenedBy);
	    dailyReceiptSummarySection.setCashDrawerClosedBy(cashDrawerClosedBy);
	    dailyReceiptSummarySection.setVerifiedByUser(verifiedByUser);
	    dailyReceiptSummarySection.setTotalCash(totalCash);
	    dailyReceiptSummarySection.setCashOverUnder(cashOverUnder);
	    dailyReceiptSummarySection.setCashAcctUseOnly(totalAcctUse);
	    dailyReceiptSummarySection.setTotalCheck(totalCheck);
	    dailyReceiptSummarySection.setCheckAcctUseOnly(checkAcctUse);
	    dailyReceiptSummarySection.setPlsExplainAnyOveragesShortages(plsExplainAnyOveragesShortages);

	    return dailyReceiptSummarySection;
	}
	
}