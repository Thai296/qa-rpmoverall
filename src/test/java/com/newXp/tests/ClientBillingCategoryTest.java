package com.newXp.tests;

import com.mbasys.mars.ejb.entity.clnBillingAssignmentTyp.ClnBillingAssignmentTyp;
import com.mbasys.mars.ejb.entity.clnBillingCategory.ClnBillingCategory;
import com.overall.fileMaintenance.clientBillingCategory.ClientBillingCategory;
import com.overall.menu.MenuNavigation;
import com.overall.utils.ClientBillingCategoryUtils;
import com.overall.utils.XifinPortalUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.*;

public class ClientBillingCategoryTest extends SeleniumBaseTest {

	private static final String FIRST_ROW = "2";
	private static final String ADD_RECORD = "Add Record";
	private static final String ID_ERROR_MESSAGE = "ID: Field is required";
	private static final String CLIENT_BILLING_CATEGORY_TITLE = "Client Billing Category";
	private static final String WARNING_MESSAGE = "Changes have been made to this page. Are you sure you want to reset the page?";
	private static final String FM_CLIENT_BILLING_CATEGORY_TBL = "tbl_fmclientbilling";
	private static final int CLIENT_BILLING_CATEGORY_TBL_ID = 3;
	private static final int CLIENT_BILLING_CATEGORY_TBL_DESC = 4;
	private static final int CLIENT_BILLING_CATEGORY_TBL_SVC_CODE = 5;
	private static final int CLIENT_BILLING_CATEGORY_TBL_DESC_CLN_STMT = 6;
	private static final int CLIENT_BILLING_CATEGORY_TBL_BILL_RENTAL = 8;
	private static final int CLIENT_BILLING_CATEGORY_TBL_BILL_NON_RENTAL = 9;
	private String clnBillingCatId;
	private String clnBillingCatIdUpdate;
	
	private Actions actions;
	private MenuNavigation menuNavigation;
	private RandomCharacter randomCharacter; 
	private XifinPortalUtils xifinPortalUtils;
	private ClientBillingCategory clientBillingCategory;
	private ClientBillingCategoryUtils clientBillingCategoryUtils;
	
	@BeforeMethod(alwaysRun = true)
    @Parameters({ "marTabName", "marUsername", "marPassword", "ssoXpUsername", "ssoXpPassword" })
    public void beforeMethod(String marTabName, String marUsername, String marPassword, String ssoXpUsername, String ssoXpPassword, Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            String userName = ssoXpUsername.substring(0, ssoXpUsername.indexOf("@"));
            //String userName = ssoXpUsername.substring(ssoXpUsername.indexOf("_") + 1, ssoXpUsername.length()); // using for offsite tester
            xifinPortalUtils = new XifinPortalUtils(driver, config);
            logIntoSso(ssoXpUsername, ssoXpPassword);
            menuNavigation = new MenuNavigation(driver, config);
	        menuNavigation.navigateToClientBillingCatagoryPage();
	        xifinPortalUtils.waitForPageLoaded(wait);
	        clientBillingCategoryUtils = new ClientBillingCategoryUtils(driver, config, wait);
	        clientBillingCategoryUtils.unlockUserDataIfAny(ssoXpUsername, ssoXpPassword, this, xifinPortalUtils);	
        } catch (Exception e) {
            logger.error("Error running BeforeMethod", e);
        }
    }
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws XifinDataAccessException {
		String methodName = result.getMethod().getMethodName();
		
		logger.info("*** Starting revert data ***");
		switch(methodName) {
		case "testXPR_756":
			clientBillingCategoryUtils.deleteClnBillingCategoryById(clnBillingCatId);
			break;
		case "testXPR_757":
			clientBillingCategoryUtils.deleteClnBillingCategoryById(clnBillingCatId);
			break;
		case "testXPR_758":
			clientBillingCategoryUtils.deleteClnBillingCategoryById(clnBillingCatId);
			clientBillingCategoryUtils.deleteClnBillingCategoryById(clnBillingCatIdUpdate);
			break;
		case "testXPR_762":
			clientBillingCategoryUtils.deleteClnBillingCategoryById(clnBillingCatId);
			break;
		default:
			logger.info("*** No need to revert data ***");
		}
	}

	/**
	 * Test Script
	 */
	
	@Test(priority = 1, description = "Add Client Billing Category with valid data")
	public void testXPR_756() throws Throwable {		
		logger.info("===== Testing - testXPR_756 =====");
		randomCharacter = new RandomCharacter(driver);
		clientBillingCategory = new ClientBillingCategory(wait);;
		
		clnBillingCatId = clientBillingCategoryUtils.getRandomClnBillingCatIdNotExisted();
		String desc = "DESC" + randomCharacter.getRandomAlphaString(4);
		String serviceCode = "SVC Code" + randomCharacter.getRandomNumericString(2);
		String descOnClnStmt = "desc on ClnStmt" + randomCharacter.getRandomAlphaString(4);
		ClnBillingAssignmentTyp billList = clientDao.getRandomClnBillingAssignmentTyp();
		String billRental = billList.getAbbrev();
		int billTypId = billList.getBillingAssignmentTypId();
		
		logger.info("*** Step 1 Expected Result: User login successfully - Client Billing Category screen is displayed.");
		assertEquals(clientBillingCategory.clientBillingCategoryPageTitle().getText(), CLIENT_BILLING_CATEGORY_TITLE,"        The Client Billing Category screen is displayed");
		
		logger.info("*** Step 2 Action: Click on Add icon");
		clickHiddenPageObject(clientBillingCategory.addIcon(), 0);
		
		logger.info("*** Step 2 Expected Result: Verify that the Add record popup is displayed");
		assertTrue(clientBillingCategory.addRecordPopup().isDisplayed(), "        The add record popup is displayed");
		
		logger.info("*** Step 3 Action: Enter data to all fields in Add record popup | Click on 'OK' button");
		clientBillingCategory.setIDField(clnBillingCatId);
		clientBillingCategory.setDescriptionField(desc);
		clientBillingCategory.setServiceCodeField(serviceCode);
		clientBillingCategory.setDescOnStmtField(descOnClnStmt);
		clientBillingCategory.selectBillRentalDropdown(billRental);
		clientBillingCategory.selectBillNonRentalDropdownList(billRental);
		clickHiddenPageObject(clientBillingCategory.OKBtn(), 0);
		
		logger.info("*** Step 3 Expected Result: new record is shown in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_ID).getText(), clnBillingCatId, "        Id is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC).getText(), desc, "        Description is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_SVC_CODE).getText(), serviceCode, "        Service code on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC_CLN_STMT).getText(), descOnClnStmt, "        Description on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_RENTAL).getText(), billRental, "        Bill rental tests to is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_NON_RENTAL).getText(), billRental, "        Bill non-rental tests to is added in the grid");
		
		logger.info("*** Step 4 Action: Click on Save button");
		clientBillingCategoryUtils.clickOnSaveAndClearBtn(this);

		logger.info("*** Step 4 Expected result: new record is save in the table");
		ClnBillingCategory clnBillingCategory = clientDao.getDataFromClnBillingCategoryByAbbrev(clnBillingCatId);
		assertEquals(clnBillingCategory.getAbbrev(), clnBillingCatId, "        New record is added into CLN_BILLING_CATEGORY table with correct id");
		assertEquals(clnBillingCategory.getDescr(), desc, "        New record is added into CLN_BILLING_CATEGORY table with correct description");
		assertEquals(clnBillingCategory.getServiceCode(), serviceCode, "        New record is added into CLN_BILLING_CATEGORY table with correct service code");
		assertEquals(clnBillingCategory.getClnStmtDescr(), descOnClnStmt, "        New record is added into CLN_BILLING_CATEGORY table with correct desc on client statement");
		assertEquals(clnBillingCategory.getBillToRenalTests(), billTypId, "        New record is added into CLN_BILLING_CATEGORY table with correct bill rental");
		assertEquals(clnBillingCategory.getBillToNonRenalTests(), billTypId, "        New record is added into CLN_BILLING_CATEGORY table with correct bill non-rental");
		
		clientBillingCategory.setSearchIDTxtBox(clnBillingCatId);
		assertEquals(clientBillingCategory.ClientBillingCategoryTblIdColtxt(FIRST_ROW).getAttribute("value"), clnBillingCatId, "        The new record is shown in the grid.");
	}

	@Test(priority = 1, description="Add Client Billing Category with dialysis is check")
	public void testXPR_757() throws Throwable{
		logger.info("==========Testing - XPR_757=============");
		randomCharacter = new RandomCharacter(driver);
		clientBillingCategory = new ClientBillingCategory(wait);;
		
		clnBillingCatId = clientBillingCategoryUtils.getRandomClnBillingCatIdNotExisted();
		String desc = "Desc" + randomCharacter.getRandomAlphaString(8);
		String serviceCode = "ServiceCode" + randomCharacter.getRandomAlphaNumericString(8);
		String descOnClnStmt = "desc On Cln Stmt" + randomCharacter.getRandomAlphaString(4);
		ClnBillingAssignmentTyp billList = clientDao.getRandomClnBillingAssignmentTyp();
		String billRental = billList.getAbbrev();
		int billTypId = billList.getBillingAssignmentTypId();
		
		logger.info("*** Step 1 Expected result: User login success");
		assertEquals(clientBillingCategory.clientBillingCategoryPageTitle().getText(), CLIENT_BILLING_CATEGORY_TITLE,"        The Client Billing Category screen is displayed");
		
		logger.info("*** Step 2 Action: Add new record with dialysis is check");
		clickHiddenPageObject(clientBillingCategory.addIcon(), 0);
		
		clientBillingCategory.setIDField(clnBillingCatId);
		clientBillingCategory.setDescriptionField(desc);
		clientBillingCategory.setServiceCodeField(serviceCode);
		clientBillingCategory.setDescOnStmtField(descOnClnStmt);
		selectCheckBox(clientBillingCategory.dialysisCheckbox());
		clientBillingCategory.selectBillRentalDropdown(billRental);
		clientBillingCategory.selectBillNonRentalDropdownList(billRental);
		clickHiddenPageObject(clientBillingCategory.OKBtn(), 0);

		logger.info("*** Step 2 Expected result: New record is displayed in the gird");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_ID).getText(), clnBillingCatId, "        Id is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC).getText(), desc, "        Description is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_SVC_CODE).getText(), serviceCode, "        Service code on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC_CLN_STMT).getText(), descOnClnStmt, "        Description on client statement is added in the grid");
		assertTrue(clientBillingCategory.lastDialysisCheckboxInClnBillingCategoryTbl().isSelected(), "        Dialysis checkbox is checked in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_RENTAL).getText(), billRental, "        Bill rental tests to is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_NON_RENTAL).getText(), billRental, "        Bill non-rental tests to is added in the grid");
		
		logger.info("*** Step 3 Action: Click on Save button");
		clientBillingCategoryUtils.clickOnSaveAndClearBtn(this);
		
		logger.info("*** Step 3 Expected result: The screen is still displayed with new record. New record is save in CLN_BILLING_CATEGORY");
		clientBillingCategory.setSearchIDTxtBox(clnBillingCatId);
		assertEquals(clientBillingCategory.ClientBillingCategoryTblIdColtxt(FIRST_ROW).getAttribute("value"), clnBillingCatId, "        The new record is shown in the grid.");
		
		ClnBillingCategory clnBillingCategory = clientDao.getDataFromClnBillingCategoryByAbbrev(clnBillingCatId);
		assertEquals(clnBillingCategory.getAbbrev(), clnBillingCatId, "        New record is added into CLN_BILLING_CATEGORY table with correct id");
		assertEquals(clnBillingCategory.getDescr(), desc, "        New record is added into CLN_BILLING_CATEGORY table with correct description");
		assertEquals(clnBillingCategory.getServiceCode(), serviceCode, "        New record is added into CLN_BILLING_CATEGORY table with correct service code");
		assertEquals(clnBillingCategory.getClnStmtDescr(), descOnClnStmt, "        New record is added into CLN_BILLING_CATEGORY table with correct desc on client statement");
		assertEquals(clnBillingCategory.getIsDialysis(), true, "        New record is added into CLN_BILLING_CATEGORY table with correct dialysis selected");
		assertEquals(clnBillingCategory.getBillToRenalTests(), billTypId, "        New record is added into CLN_BILLING_CATEGORY table with correct bill rental");
		assertEquals(clnBillingCategory.getBillToNonRenalTests(), billTypId, "        New record is added into CLN_BILLING_CATEGORY table with correct bill non-rental");
	}
	
	@Test(priority = 1, description = "Update record with valid data")
	public void testXPR_758() throws Throwable {		
		logger.info("===== Testing - testXPR_758 =====");  	
		actions = new Actions(driver);
		randomCharacter = new RandomCharacter(driver);
		clientBillingCategory = new ClientBillingCategory(wait);;
		
		clnBillingCatId = clientBillingCategoryUtils.getRandomClnBillingCatIdNotExisted();
		String description = randomCharacter.getRandomAlphaNumericString(10);
		String svcCode = randomCharacter.getRandomAlphaNumericString(10);
		String descOnClnStm = randomCharacter.getRandomAlphaNumericString(20);
		ClnBillingAssignmentTyp billRentalType = clientDao.getRandomClnBillingAssignmentTyp();
		ClnBillingAssignmentTyp billNonRentalType = clientBillingCategoryUtils.getClnBillingAssignmentTypDifferentGivenOne(billRentalType);

		String billRental = billRentalType.getAbbrev();
		int billRentalTypId = billRentalType.getBillingAssignmentTypId();
		String billNonRental = billNonRentalType.getAbbrev();
		int billNonRentalTypId = billNonRentalType.getBillingAssignmentTypId();
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Client Billing Category screen is displayed.");
		assertEquals(clientBillingCategory.clientBillingCategoryPageTitle().getText(), CLIENT_BILLING_CATEGORY_TITLE,"        The Client Billing Category screen is displayed");
		
		logger.info("*** Step 2 Action: Add new Client Billing Category.");
		clickHiddenPageObject(clientBillingCategory.addIcon(), 0);
		clientBillingCategory.inputClnBillingInfo(clnBillingCatId, description, svcCode, descOnClnStm, billRental, billNonRental);
		clickHiddenPageObject(clientBillingCategory.OKBtn(), 0);
			
		logger.info("*** Step 2 Expected Result: New record is shown in the grid.");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_ID).getText(), clnBillingCatId, "        Id is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC).getText(), description, "        Description is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_SVC_CODE).getText(), svcCode, "        Service code on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC_CLN_STMT).getText(), descOnClnStm, "        Description on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_RENTAL).getText(), billRental, "        Bill rental tests to is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_NON_RENTAL).getText(), billNonRental, "        Bill non-rental tests to is added in the grid");
		
		logger.info("*** Step 3 Action:  Click on Save button");
		clientBillingCategoryUtils.clickOnSaveAndClearBtn(this);
		
		logger.info("*** Step 3 Expected Result: New record is added in CLN_BILLING_CATEGORY table. The new record is still shown in the grid");
		clientBillingCategory.setSearchIDTxtBox(clnBillingCatId);
		assertEquals(clientBillingCategory.ClientBillingCategoryTblIdColtxt(FIRST_ROW).getAttribute("value"), clnBillingCatId, "        The new record is shown in the grid.");
		
		ClnBillingCategory clnBillingCategory = clientDao.getDataFromClnBillingCategoryByAbbrev(clnBillingCatId);
		assertEquals(clnBillingCategory.getAbbrev(), clnBillingCatId, "        New record is added into CLN_BILLING_CATEGORY table with correct id");
		assertEquals(clnBillingCategory.getDescr(), description, "        New record is added into CLN_BILLING_CATEGORY table with correct description");
		assertEquals(clnBillingCategory.getServiceCode(), svcCode, "        New record is added into CLN_BILLING_CATEGORY table with correct service code");
		assertEquals(clnBillingCategory.getClnStmtDescr(), descOnClnStm, "        New record is added into CLN_BILLING_CATEGORY table with correct desc on client statement");
		assertEquals(clnBillingCategory.getIsDialysis(), false, "        New record is added into CLN_BILLING_CATEGORY table with correct dialysis selected");
		assertEquals(clnBillingCategory.getBillToRenalTests(), billRentalTypId, "        New record is added into CLN_BILLING_CATEGORY table with correct bill rental");
		assertEquals(clnBillingCategory.getBillToNonRenalTests(), billNonRentalTypId, "        New record is added into CLN_BILLING_CATEGORY table with correct bill non-rental");
		
		logger.info("*** Step 4 Action: Select new record, double click on that record");
		xifinPortalUtils.waitForPageLoaded(wait);
		actions.doubleClick(clientBillingCategory.ClientBillingCategoryTblRow(FIRST_ROW)).perform();
		
		logger.info("*** Step 4 Expected Result: The edit popup is displayed with correct information.");
		assertEquals(clientBillingCategory.IDField().getAttribute("value"), clnBillingCatId, "        ID infomation is displayed correctly.");
		assertEquals(clientBillingCategory.descField().getAttribute("value"), description, "        Description infomation is displayed correctly.");
		assertEquals(clientBillingCategory.serviceCodeField().getAttribute("value"), svcCode, "        Service Code infomation is displayed correctly.");
		assertEquals(clientBillingCategory.descOnClnStmtField().getAttribute("value"),descOnClnStm, "         Description on Client Statement information is displayed correctly.");
		
		logger.info("*** Step 5 Action: Change ID and Description, select Bill Rentel test to, click OK button.");
		clnBillingCatIdUpdate = clientBillingCategoryUtils.getRandomClnBillingCatIdUpdateHaveNotExisted(clnBillingCatId);
		String newDescription = clientBillingCategoryUtils.getRandomValueDifferentToOldValue(description);
		String newSvcCode = clientBillingCategoryUtils.getRandomValueDifferentToOldValue(svcCode);
		String newDescOnClnStm = clientBillingCategoryUtils.getRandomValueDifferentToOldValue(descOnClnStm);
		ClnBillingAssignmentTyp newBillRentalType = clientBillingCategoryUtils.getClnBillingAssignmentTypDifferentGivenOne(billRentalType);
		ClnBillingAssignmentTyp newBillNonRentalType = clientBillingCategoryUtils.getClnBillingAssignmentTypDifferentGivenOne(newBillRentalType);
		
		String newBillRental = newBillRentalType.getAbbrev();
		int newBillRentalTypId = newBillRentalType.getBillingAssignmentTypId();
		String newBillNonRental = newBillNonRentalType.getAbbrev();
		int newBillNonRentalTypId = newBillNonRentalType.getBillingAssignmentTypId();
		
		clientBillingCategory.inputClnBillingInfo(clnBillingCatIdUpdate, newDescription, newSvcCode, newDescOnClnStm, newBillRental, newBillNonRental);
		clickHiddenPageObject(clientBillingCategory.OKBtn(),0);
		
		logger.info("*** Step 5 Expected Result: The update data is shown in the grid.");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_ID).getText(), clnBillingCatIdUpdate, "        Id is updated in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC).getText(), newDescription, "        Description is updated in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_SVC_CODE).getText(), newSvcCode, "        Service code on client statement is updated in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC_CLN_STMT).getText(), newDescOnClnStm, "        Description on client statement is updated in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_RENTAL).getText(), newBillRental, "        Bill rental tests to is updated in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_NON_RENTAL).getText(), newBillNonRental, "        Bill non-rental tests to is updated in the grid");
		
		logger.info("*** Step 6 Action: Click on Save button");
		clientBillingCategoryUtils.clickOnSaveAndClearBtn(this);
		
		logger.info("*** Step 6 Expected Result: The update information is save in CLN_BILLING_CATEGORY table. The page is still displayed.");
		clientBillingCategory.setSearchIDTxtBox(clnBillingCatIdUpdate);
		assertEquals(clientBillingCategory.ClientBillingCategoryTblIdColtxt(FIRST_ROW).getAttribute("value"), clnBillingCatIdUpdate, "        The update record is shown in the grid.");
		
		ClnBillingCategory clnBillingCategoryUpdate = clientDao.getDataFromClnBillingCategoryByAbbrev(clnBillingCatIdUpdate);
		assertEquals(clnBillingCategoryUpdate.getAbbrev(), clnBillingCatIdUpdate, "        New record is updated into CLN_BILLING_CATEGORY table with correct id");
		assertEquals(clnBillingCategoryUpdate.getDescr(), newDescription, "        New record is updated into CLN_BILLING_CATEGORY table with correct description");
		assertEquals(clnBillingCategoryUpdate.getServiceCode(), newSvcCode, "        New record is updated into CLN_BILLING_CATEGORY table with correct service code");
		assertEquals(clnBillingCategoryUpdate.getClnStmtDescr(), newDescOnClnStm, "        New record is updated into CLN_BILLING_CATEGORY table with correct desc on client statement");
		assertEquals(clnBillingCategoryUpdate.getIsDialysis(), false, "        New record is updated into CLN_BILLING_CATEGORY table with correct dialysis selected");
		assertEquals(clnBillingCategoryUpdate.getBillToRenalTests(), newBillRentalTypId, "        New record is updated into CLN_BILLING_CATEGORY table with correct bill rental");
		assertEquals(clnBillingCategoryUpdate.getBillToNonRenalTests(), newBillNonRentalTypId, "        New record is updated into CLN_BILLING_CATEGORY table with correct bill non-rental");
	}
	
	@Test(priority = 1, description = "Verify Help icon")
	public void testXPR_760() throws Throwable {		
		logger.info("===== Testing - testXPR_760 =====");
		randomCharacter = new RandomCharacter(driver);
		clientBillingCategory = new ClientBillingCategory(wait);;

		logger.info("*** Step 1 Expected Result: User login successfully, the Client Billing Category screen is displayed.");
		assertEquals(clientBillingCategory.clientBillingCategoryPageTitle().getText(), CLIENT_BILLING_CATEGORY_TITLE,"        The Client Billing Category screen is displayed");
		
		logger.info("*** Step 2 Action: Click on Help icon at header menu.");
		clickHiddenPageObject(clientBillingCategory.clientBillingCategoryHelpIconOnHeader(),0);
		
		logger.info("*** Step 2 Expected Result: The help page is displayed.");
		assertTrue(clientBillingCategoryUtils.verifyHelpPageIsDisplayedAndSwitchingToParentWindow(this, "help/file_maintenance_tab/codes_menu/p_client_billing_category_header"), "        The help page is displayed.");
		
		logger.info("*** Step 3 Action:  Click on Help icon at Client Billing Category section");
		clickHiddenPageObject(clientBillingCategory.clientBillingCategoryHelpIconOnClnBillingSection(),0);
		
		logger.info("*** Step 3 Expected Result: The help page is displayed");
		assertTrue(clientBillingCategoryUtils.verifyHelpPageIsDisplayedAndSwitchingToParentWindow(this, "help/file_maintenance_tab/codes_menu/p_client_billing_category"), "        The help page is displayed.");
	}
	
	@Test(priority = 1, description = "Verify Reset button")
	public void testXPR_761() throws Throwable {		
		logger.info("===== Testing - testXPR_761 =====");  	
		actions = new Actions(driver);
		randomCharacter = new RandomCharacter(driver);
		clientBillingCategory = new ClientBillingCategory(wait);;
		
		String id = clientBillingCategoryUtils.getRandomClnBillingCatIdNotExisted();
		String description = randomCharacter.getRandomAlphaNumericString(10);
		String svcCode = randomCharacter.getRandomAlphaNumericString(10);
		String descOnClnStm = randomCharacter.getRandomAlphaNumericString(20);
		ClnBillingAssignmentTyp billRentalType = clientDao.getRandomClnBillingAssignmentTyp();
		ClnBillingAssignmentTyp billNonRentalType = clientBillingCategoryUtils.getClnBillingAssignmentTypDifferentGivenOne(billRentalType);

		String billRental = billRentalType.getAbbrev();
		String billNonRental = billNonRentalType.getAbbrev();
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Client Billing Category screen is displayed.");
		assertEquals(clientBillingCategory.clientBillingCategoryPageTitle().getText(), CLIENT_BILLING_CATEGORY_TITLE,"        The Client Billing Category screen is displayed");
		
		logger.info("*** Step 2 Action: Add new record into grid.");
		clickHiddenPageObject(clientBillingCategory.addIcon(),0);
		assertEquals(clientBillingCategory.popupTitle().getText(), ADD_RECORD, "        Add Record popup is displayed.");
	
		clientBillingCategory.inputClnBillingInfo(id, description, svcCode, descOnClnStm, billRental, billNonRental);
		clickHiddenPageObject(clientBillingCategory.OKBtn(),0);
			
		logger.info("*** Step 2 Expected Result: New record is shown in the grid.");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_ID).getText(), id, "        Id is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC).getText(), description, "        Description is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_SVC_CODE).getText(), svcCode, "        Service code on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC_CLN_STMT).getText(), descOnClnStm, "        Description on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_RENTAL).getText(), billRental, "        Bill rental tests to is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_NON_RENTAL).getText(), billNonRental, "        Bill non-rental tests to is added in the grid");
		
		logger.info("*** Step 3 Action:  Click on Reset button");
		clickHiddenPageObject(clientBillingCategory.resetBtn(),0);
		
		logger.info("*** Step 3 Expected Result: the confirmation message 'Changes have been made to this accession. Are you sure you want to reset the page?' is displayed.");
		assertEquals(clientBillingCategory.warningMsgTxt().getText(), WARNING_MESSAGE, "        confirmation message 'Changes have been made to this accession. Are you sure you want to reset the page?' is displayed.");
				
		logger.info("*** Step 4 Action: Click on Reset button");
		clickHiddenPageObject(clientBillingCategory.warningMsgResetBtn(),0);
				
		logger.info("*** Step 4 Expected Result: The new record is not shown in the grid. The new record is not added into CLN_BILLING_CATEGORY table");
		clientBillingCategory.setSearchIDTxtBox(id);
		assertNotEquals(lastCellTbl("tbl_fmclientbilling",3).getText(),id, "        New record is not shown in the grid.");
		clientBillingCategory.clearSearchIDTxtBox();
		ClnBillingCategory clnBillingCategory = clientDao.getDataFromClnBillingCategoryByAbbrev(id);
		assertNull(clnBillingCategory, "        The new record is not added into CLN_BILLING_CATEGORY table.");
		
		logger.info("*** Step 5 Action: Add new record into the grid.");
		clickHiddenPageObject(clientBillingCategory.addIcon(),0);
		assertEquals(clientBillingCategory.popupTitle().getText(), ADD_RECORD, "        Add Record popup is displayed.");
		
		clientBillingCategory.inputClnBillingInfo(id, description, svcCode, descOnClnStm, billRental, billNonRental);
		clickHiddenPageObject(clientBillingCategory.OKBtn(),0);
		
		logger.info("*** Step 5 Expected Result: New record is shown in the grid.");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_ID).getText(), id, "        Id is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC).getText(), description, "        Description is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_SVC_CODE).getText(), svcCode, "        Service code on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC_CLN_STMT).getText(), descOnClnStm, "        Description on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_RENTAL).getText(), billRental, "        Bill rental tests to is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_NON_RENTAL).getText(), billNonRental, "        Bill non-rental tests to is added in the grid");
		
		logger.info("*** Step 6 Action: Press ALT+R");
		actions.keyDown(Keys.ALT).sendKeys("R").keyUp(Keys.ALT).build().perform();
		
		logger.info("*** Step 6 Expected Result: The confirmation message 'Changes have been made to this accession. Are you sure you want to reset the page?' is displayed.");
		assertEquals(clientBillingCategory.warningMsgTxt().getText(), WARNING_MESSAGE, "        confirmation message 'Changes have been made to this accession. Are you sure you want to reset the page?' is displayed.");
				
		logger.info("*** Step 7 Action: Click on Cancel button.");
		clickHiddenPageObject(clientBillingCategory.warningMsgCancelBtn(),0);
		
		logger.info("*** Step 7 Expected Result: The confirmation message is closed.");
		assertFalse(clientBillingCategory.warningMsgTitle().isDisplayed(), "        The confirmation message is closed.");
		assertTrue(isElementPresent(clientBillingCategory.idSearchTxtBox(),5), "        Search Text Box is displayed.");
		
		logger.info("*** Step 7 Expected Result: New record is still displayed in the grid. New record is not added in Cln_Billing_Caterogry table");
		clientBillingCategory.setSearchIDTxtBox(id);
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_ID).getText(), id, "        Id is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC).getText(), description, "        Description is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_SVC_CODE).getText(), svcCode, "        Service code on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC_CLN_STMT).getText(), descOnClnStm, "        Description on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_RENTAL).getText(), billRental, "        Bill rental tests to is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_NON_RENTAL).getText(), billNonRental, "        Bill non-rental tests to is added in the grid");
		
		clnBillingCategory = clientDao.getDataFromClnBillingCategoryByAbbrev(id);
		assertNull(clnBillingCategory, "        The new record is not added into CLN_BILLING_CATEGORY table.");
	}
	
	@Test(priority = 1, description = "Delete record from DB")
	public void testXPR_762() throws Throwable {		
		logger.info("===== Testing - testXPR_762 =====");
		randomCharacter = new RandomCharacter(driver);
		clientBillingCategory = new ClientBillingCategory(wait);;
		
		clnBillingCatId = clientBillingCategoryUtils.getRandomClnBillingCatIdNotExisted();
		String description = randomCharacter.getRandomAlphaNumericString(10);
		String svcCode = randomCharacter.getRandomAlphaNumericString(10);
		String descOnClnStm = randomCharacter.getRandomAlphaNumericString(20);
		ClnBillingAssignmentTyp billRentalType = clientDao.getRandomClnBillingAssignmentTyp();
		ClnBillingAssignmentTyp billNonRentalType = clientBillingCategoryUtils.getClnBillingAssignmentTypDifferentGivenOne(billRentalType);

		String billRental = billRentalType.getAbbrev();
		int billRentalTypId = billRentalType.getBillingAssignmentTypId();
		String billNonRental = billNonRentalType.getAbbrev();
		int billNonRentalTypId = billNonRentalType.getBillingAssignmentTypId();
		
		logger.info("*** Step 1 Expected Result: User login successfully. The Client Billing Category screen is displayed.");
		assertEquals(clientBillingCategory.clientBillingCategoryPageTitle().getText(), CLIENT_BILLING_CATEGORY_TITLE,"        The Client Billing Category screen is displayed");

		logger.info("*** Step 2 Action: Create a new record in CLN_BILLING_CATEGORY table.");
		clickHiddenPageObject(clientBillingCategory.addIcon(), 0);
		clientBillingCategory.inputClnBillingInfo(clnBillingCatId, description, svcCode, descOnClnStm, billRental, billNonRental);
		clickHiddenPageObject(clientBillingCategory.OKBtn(), 0);
		
		logger.info("*** Step 2 Expected Result: A new record is displayed in the grid.");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_ID).getText(), clnBillingCatId, "        Id is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC).getText(), description, "        Description is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_SVC_CODE).getText(), svcCode, "        Service code on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC_CLN_STMT).getText(), descOnClnStm, "        Description on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_RENTAL).getText(), billRental, "        Bill rental tests to is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_NON_RENTAL).getText(), billNonRental, "        Bill non-rental tests to is added in the grid");
		
		logger.info("*** Step 3 Action: Click on Save button");
		clientBillingCategoryUtils.clickOnSaveAndClearBtn(this);
		
		logger.info("*** Step 3 Expected Result: A new record is added in CLN_BILLING_CATEGORY.");
		ClnBillingCategory clnBillingCategory = clientDao.getDataFromClnBillingCategoryByAbbrev(clnBillingCatId);
		assertNotNull(clnBillingCategory,"        A new record is added in CLN_BILLING_CATEGORY.");
		assertEquals(clnBillingCategory.getAbbrev(), clnBillingCatId, "        New record is added into CLN_BILLING_CATEGORY table with correct id");
		assertEquals(clnBillingCategory.getDescr(), description, "        New record is added into CLN_BILLING_CATEGORY table with correct description");
		assertEquals(clnBillingCategory.getServiceCode(), svcCode, "        New record is added into CLN_BILLING_CATEGORY table with correct service code");
		assertEquals(clnBillingCategory.getClnStmtDescr(), descOnClnStm, "        New record is added into CLN_BILLING_CATEGORY table with correct desc on client statement");
		assertEquals(clnBillingCategory.getIsDialysis(), false, "        New record is added into CLN_BILLING_CATEGORY table with correct dialysis selected");
		assertEquals(clnBillingCategory.getBillToRenalTests(), billRentalTypId, "        New record is added into CLN_BILLING_CATEGORY table with correct bill rental");
		assertEquals(clnBillingCategory.getBillToNonRenalTests(), billNonRentalTypId, "        New record is added into CLN_BILLING_CATEGORY table with correct bill non-rental");
		
		logger.info("*** Step 3 Expected Result: Verify a new record at step 2.");
		clientBillingCategory.setSearchIDTxtBox(clnBillingCatId);
		assertEquals(clientBillingCategory.ClientBillingCategoryTblIdColtxt(FIRST_ROW).getAttribute("value"), clnBillingCatId, "        The new record is shown in the grid.");
		
		logger.info("*** Step 4 Action: Remove one record in CLN_BILLING_CATEGORY table.");
		clientBillingCategoryUtils.deleteClnBillingCategoryById(clnBillingCatId);
		
		logger.info("*** Step 4 Expected Result: The record is removed.");
		clnBillingCategory = clientDao.getDataFromClnBillingCategoryByAbbrev(clnBillingCatId);
		assertNull(clnBillingCategory, "        The record is removed from CLN_BILLING_CATEGORY table.");
		
		logger.info("*** Step 5 Action: Click on Save button");
		clientBillingCategoryUtils.clickOnSaveAndClearBtn(this);
		
		logger.info("*** Step 5 Expected Result: The selected record at step 2 does not shown in the grid");
		assertTrue(isElementPresent(clientBillingCategory.idSearchTxtBox(),5),"        Search text box is display.");
		clientBillingCategory.setSearchIDTxtBox(clnBillingCatId);
		assertTrue(isElementPresent(clientBillingCategory.tableClientBilling(),5), "        Client Billing table is display.");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_ID).getText(), "", "        The selected record at step 1 is not shown in the grid.");
	}
	
	@Test(priority = 1, description = "Add client billing without input require fields")
	public void testXPR_764() throws Throwable {		
		logger.info("===== Testing - testXPR_764 =====");
		randomCharacter = new RandomCharacter(driver);
		clientBillingCategory = new ClientBillingCategory(wait);;
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Client Billing Category screen is displayed.");
		assertEquals(clientBillingCategory.clientBillingCategoryPageTitle().getText(), CLIENT_BILLING_CATEGORY_TITLE,"        The Client Billing Category screen is displayed");
		
		logger.info("*** Step 2 Action: Add new record into grid.");
		clickHiddenPageObject(clientBillingCategory.addIcon(),0);
		
		logger.info("*** Step 2 Expected Result: Add Record pop-up is displayed");
		assertEquals(clientBillingCategory.popupTitle().getText(), ADD_RECORD, "        Add Record popup is displayed.");
		
		logger.info("*** Step 3 Action:  Input data for some fields, Leave ID field is empty.");
		String id = "";
		String description = randomCharacter.getRandomAlphaNumericString(10);
		String svcCode = randomCharacter.getRandomAlphaNumericString(10);
		String descOnClnStm = randomCharacter.getRandomAlphaNumericString(20);
		String billRental = "INSURANCE";
		String billNonRental = "CLIENT";
		assertTrue(isElementPresent(clientBillingCategory.IDField(),5), "        ID field is displayed.");
		assertTrue(isElementPresent(clientBillingCategory.descField(),5), "        Description field is displayed.");
		assertTrue(isElementPresent(clientBillingCategory.serviceCodeField(),5), "        Service Code on Client Statement field is displayed.");
		assertTrue(isElementPresent(clientBillingCategory.descOnClnStmtField(),5), "        Description on Client Statement field is displayed.");
		assertTrue(isElementPresent(clientBillingCategory.billRentalDropBox(),5), "        Bill Renal Tests to is displayed.");
		assertTrue(isElementPresent(clientBillingCategory.billNonRentalDropBox(),5), "        Bill Non-Renal Tests to field is displayed.");
		clientBillingCategory.inputClnBillingInfo(id, description, svcCode, descOnClnStm, billRental, billNonRental);
		clickHiddenPageObject(clientBillingCategory.OKBtn(),0);
		
		logger.info("*** Step 3 Expected Result: The error message 'ID: Field is required' is displayed.");
		assertEquals(clientBillingCategory.idErrorMsg().getText(), ID_ERROR_MESSAGE,"        'ID: Field is required' is displayed.");
	}
	
	@Test(priority = 1, description = "Verify Reset button after add new record")
	public void testXPR_780() throws Throwable {		
		logger.info("===== Testing - testXPR_780 =====");
		randomCharacter = new RandomCharacter(driver);
		clientBillingCategory = new ClientBillingCategory(wait);;

		String id = clientBillingCategoryUtils.getRandomClnBillingCatIdNotExisted();
		String description = randomCharacter.getRandomAlphaNumericString(10);
		String svcCode = randomCharacter.getRandomAlphaNumericString(10);
		String descOnClnStm = randomCharacter.getRandomAlphaNumericString(20);
		ClnBillingAssignmentTyp billRentalType = clientDao.getRandomClnBillingAssignmentTyp();
		ClnBillingAssignmentTyp billNonRentalType = clientBillingCategoryUtils.getClnBillingAssignmentTypDifferentGivenOne(billRentalType);

		String billRental = billRentalType.getAbbrev();
		String billNonRental = billNonRentalType.getAbbrev();
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Client Billing Category screen is displayed.");
		assertEquals(clientBillingCategory.clientBillingCategoryPageTitle().getText(), CLIENT_BILLING_CATEGORY_TITLE,"        The Client Billing Category screen is displayed");
		
		logger.info("*** Step 2 Action: Add new record into grid.");
		clickHiddenPageObject(clientBillingCategory.addIcon(), 0);
		assertEquals(clientBillingCategory.popupTitle().getText(), ADD_RECORD, "        Add Record popup is displayed.");
		clientBillingCategory.inputClnBillingInfo(id, description, svcCode, descOnClnStm, billRental, billNonRental);
		clickHiddenPageObject(clientBillingCategory.OKBtn(),0);
		
		logger.info("*** Step 2 Expected Result: New record is shown in the grid.");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_ID).getText(), id, "        Id is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC).getText(), description, "        Description is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_SVC_CODE).getText(), svcCode, "        Service code on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_DESC_CLN_STMT).getText(), descOnClnStm, "        Description on client statement is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_RENTAL).getText(), billRental, "        Bill rental tests to is added in the grid");
		assertEquals(lastCellTbl(FM_CLIENT_BILLING_CATEGORY_TBL, CLIENT_BILLING_CATEGORY_TBL_BILL_NON_RENTAL).getText(), billNonRental, "        Bill non-rental tests to is added in the grid");
		
		logger.info("*** Step 3 Action: Filter Id follow data at step 2.");
		clientBillingCategory.setSearchIDTxtBox(id);
		
		logger.info("*** Step 3 Expected Result: A new record is show in the grid.");
		assertEquals(id,lastCellTbl("tbl_fmclientbilling",3).getText(),"        The new record is displayed.");
		
		logger.info("*** Step 4 Action: Click on reset button.");
		clickHiddenPageObject(clientBillingCategory.resetBtn(),0);

		logger.info("*** Step 4 Expected Result: The warming message is displayed.");
		assertEquals(clientBillingCategory.warningMsgTxt().getText(), WARNING_MESSAGE, "        confirmation message 'Changes have been made to this accession. Are you sure you want to reset the page?' is displayed.");
	}
	
}
