package com.newXp.tests;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnInvoiceDispute.ClnInvoiceDispute;
import com.mbasys.mars.ejb.entity.clnInvoiceDisputeDetail.ClnInvoiceDisputeDetail;
import com.mbasys.mars.ejb.entity.clnInvoiceDisputeStatusEmail.ClnInvoiceDisputeStatusEmail;
import com.overall.client.lineitemdispute.LineItemDispute;
import com.overall.client.lineitemdispute.LineItemDisputeDetail;
import com.overall.menu.MenuNavigation;
import com.overall.utils.LineItemDisputeDetailsUtils;
import com.overall.utils.LineItemDisputeUtils;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;

public class SmokeClientLineItemDisputeTest extends SeleniumBaseTest {
	private LineItemDisputeUtils lineItemDisputeUtils;
	private LineItemDisputeDetailsUtils lineItemDisputeDetailsUtils;
	private LineItemDisputeDetail lineItemDisputeDetail;
	private LineItemDispute lineItemDispute;
	private MenuNavigation navigation;
	private ClnInvoiceDisputeDetail revertClnInvoiceDisputeDetail;
	private ClnInvoiceDispute revertClnInvoiceDispute;
	private ClnInvoiceDisputeStatusEmail revertClnInvoiceDisputeStatusEmail;
	private RandomCharacter randomCharacter;
	
	private final int IN_DISPUTE_DISPUTE_STATUS_ID = 1;
	private final int APPROVED_DISPUTE_STATUS_ID = 2;
	private final int IN_PROGRESS_DISPUTE_STATUS_ID = 6;
	private static int WAIT_TIME = (int)QUEUE_POLL_TIME_MS;
	
	//At time develop these test scripts, there is no way to login into Google in Xifin's environment. 
	//In future if has solution for this issue, please open all lines below of scripts related to login GG.
	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword", "mailUsername", "mailPassword"})
	public void preConditions(String ssoUsername, String ssoPassword, String gmailUsername, String gmailPassword) throws Exception {
		try {
			navigation = new MenuNavigation(driver, config);
			lineItemDisputeDetailsUtils = new LineItemDisputeDetailsUtils(driver, wait, config);
			lineItemDisputeUtils = new LineItemDisputeUtils(driver, wait, config);
			lineItemDispute = new LineItemDispute(driver, wait);
			lineItemDisputeDetail = new LineItemDisputeDetail(wait);
			randomCharacter = new RandomCharacter();
			
			clearDataCache();
			logger.info("*** Login to Sso ***");
			logIntoSso(ssoUsername, ssoPassword);
			navigation.navigateToClientLineItemDisputePage();
		} catch (Exception e) {
			logger.error("        Error running PreConditions", e);
		}
	}
	
	@Test(description = "Verify that a new grid Dispute Notifications should be displayed correct") 
	public void verifyGridDisputeNotificationDisplayedCorrect() throws Exception {
		logger.info("=== Starting test case verifyGridDisputeNotificationDisplayedCorrect ===");
		logger.info("*** Step 1 Expected Results: - Verify the Dispute Notifications grid displayed correctly");
		lineItemDisputeUtils.verifyDisputeNotificationGridDisplayedCorrectly();
		
		logger.info("*** Step 2 Action: - Click on View Details link for the Client ID which the status is 'Pending Review'");
        ClnInvoiceDispute clnInvoiceDispute = clientDao.getListClnInvoiceDisputeNotFinalized().get(0);
		lineItemDisputeUtils.filterSelectedDisputeItem(clnInvoiceDispute);
		webElementInteractions.clickOnElement(lineItemDispute.viewDetailButtonByDisputeSeqId(clnInvoiceDispute.getSeqId()));
		
		logger.info("*** Step 2 Expected Results: - Line Item Dispute Details screen displayed correctly");
		switchToPopUp.switchToPopupWin(5);
		List<ClnInvoiceDisputeDetail> clnInvoiceDisputeDetails = clientDao.getAllClnInvoiceDisputeDetailByClnInvoiceDisputeId(clnInvoiceDispute.getSeqId());
		lineItemDisputeDetailsUtils.verifyLineItemDisputeDetailScreenDisplayedCorrectly(clnInvoiceDisputeDetails, clnInvoiceDispute.getSeqId());
	}
	
	@Test(description = "Verify that a new row should be added on the cln_invoice_dispute_status_email table when the user updates disputes status on the table")
	public void verifyNewRowAddedWhenUserUpdatesDisputesStatus() throws Exception {
		logger.info("=== Starting test case verifyNewRowAddedWhenUserUpdatesDisputesStatus ===");
		logger.info("*** Step 1 Expected Results: - Verify the Dispute Notifications grid displayed correctly");
		lineItemDisputeUtils.verifyDisputeNotificationGridDisplayedCorrectly();
		
		logger.info("*** Step 2 Action: - Click on View Details link for the Client ID which the status is 'Pending Review'");
		revertClnInvoiceDispute = clientDao.getListClnInvoiceDisputeNotFinalized().get(0);
		revertClnInvoiceDisputeStatusEmail = clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		lineItemDisputeUtils.filterSelectedDisputeItem(revertClnInvoiceDispute);
		webElementInteractions.clickOnElement(lineItemDispute.viewDetailButtonByDisputeSeqId(revertClnInvoiceDispute.getSeqId()));
		
		logger.info("*** Step 2 Expected Results: - Line Item Dispute Details screen displayed correctly");
		switchToPopUp.switchToPopupWin(5);
		List<ClnInvoiceDisputeDetail> clnInvoiceDisputeDetails = clientDao.getAllClnInvoiceDisputeDetailByClnInvoiceDisputeId(revertClnInvoiceDispute.getSeqId());
		lineItemDisputeDetailsUtils.verifyLineItemDisputeDetailScreenDisplayedCorrectly(clnInvoiceDisputeDetails, revertClnInvoiceDispute.getSeqId());
		
		logger.info("*** Step 3 Action: - Update the dispute status for the accession on the Line Item on Dispute table");
		lineItemDisputeUtils.deleteExistingStatusEmail(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		revertClnInvoiceDisputeDetail = clientDao.getNonFinalizedClnInvoiceDisputeDetailByClnInvoiceDisputeId(revertClnInvoiceDispute.getSeqId());
		lineItemDisputeDetailsUtils.updateDisputeDetailsStatus(revertClnInvoiceDisputeDetail.getSeqId(), APPROVED_DISPUTE_STATUS_ID);
		
		logger.info("*** Step 4 Action: - Click on Save button");
		webElementInteractions.clickOnElement(lineItemDisputeDetail.saveBtn());
		
		logger.info("*** Step 4 Expected Results: "
				+ "- The Line Item on Dispute Details screen should be closed after saving."
				+ "- Status typ should be updated in clnInvoiceDisputeDetail table."
				+ "- New record should be created in clnInvoiceDisputeStatusEmail");
		lineItemDisputeDetailsUtils.verifySaveBtnIsNotPresent(WAIT_TIME);
		lineItemDisputeDetailsUtils.verifyDisputeDetailStatusIsUpdated(revertClnInvoiceDisputeDetail.getSeqId(), APPROVED_DISPUTE_STATUS_ID);
		lineItemDisputeUtils.verifyNewRecordIsCreatedInClnInvoiceStatusEmail(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
	}
	
	@Test(description = "Verify that the cln_invoice_dispute_status_email table should be overwrited when the user update the existing data")
	public void verifyDisputeStatusOverwritedWhenUserUpdateExistingData() throws Exception {
		logger.info("=== Starting test case verifyDisputeStatusOverwritedWhenUserUpdateExistingData ===");
		logger.info("*** Step 1 Expected Results: - Verify the Dispute Notifications grid displayed correctly");
		lineItemDisputeUtils.verifyDisputeNotificationGridDisplayedCorrectly();
		
		logger.info("*** Step 2 Action: - Click on View Details link for the Client ID which the status is 'Pending Review'");
		revertClnInvoiceDispute = clientDao.getListClnInvoiceDisputeNotFinalized().get(0);
		revertClnInvoiceDisputeStatusEmail = clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		lineItemDisputeUtils.filterSelectedDisputeItem(revertClnInvoiceDispute);
		webElementInteractions.clickOnElement(lineItemDispute.viewDetailButtonByDisputeSeqId(revertClnInvoiceDispute.getSeqId()));
		
		logger.info("*** Step 2 Expected Results: - Line Item Dispute Details screen displayed correctly");
		String parentWin = switchToPopUp.switchToPopupWin(5);
		List<ClnInvoiceDisputeDetail> clnInvoiceDisputeDetails = clientDao.getAllClnInvoiceDisputeDetailByClnInvoiceDisputeId(revertClnInvoiceDispute.getSeqId());
		lineItemDisputeDetailsUtils.verifyLineItemDisputeDetailScreenDisplayedCorrectly(clnInvoiceDisputeDetails, revertClnInvoiceDispute.getSeqId());
		
		logger.info("*** Step 3 Action: - Update the dispute status for the accession on the Line Item on Dispute table");
		lineItemDisputeUtils.deleteExistingStatusEmail(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		revertClnInvoiceDisputeDetail = clientDao.getNonFinalizedClnInvoiceDisputeDetailByClnInvoiceDisputeId(revertClnInvoiceDispute.getSeqId());
		int originClnInvoiceDisputeDetailStatusId = revertClnInvoiceDisputeDetail.getDisputeStatusId();
		int intendedDisputeStatusId = IN_DISPUTE_DISPUTE_STATUS_ID + IN_PROGRESS_DISPUTE_STATUS_ID - originClnInvoiceDisputeDetailStatusId;
		lineItemDisputeDetailsUtils.updateDisputeDetailsStatus(revertClnInvoiceDisputeDetail.getSeqId(), intendedDisputeStatusId);
		
		logger.info("*** Step 4 Action: - Click on Save button");
		webElementInteractions.clickOnElement(lineItemDisputeDetail.saveBtn());
		
		logger.info("*** Step 4 Expected Results: "
				+ "- The Line Item on Dispute Details screen should be closed after saving."
				+ "- Status typ should be updated in clnInvoiceDisputeDetail table."
				+ "- New record should be created in clnInvoiceDisputeStatusEmail");
		lineItemDisputeDetailsUtils.verifySaveBtnIsNotPresent(WAIT_TIME);
		lineItemDisputeDetailsUtils.verifyDisputeDetailStatusIsUpdated(revertClnInvoiceDisputeDetail.getSeqId(), intendedDisputeStatusId);
		lineItemDisputeUtils.verifyNewRecordIsCreatedInClnInvoiceStatusEmail(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		ClnInvoiceDisputeStatusEmail disputeStatusEmailBeforeUpdate = clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		
		logger.info("*** Step 5 Action: - Selected updated client dispute");
		switchToPopUp.switchToParentWin(parentWin);
		lineItemDisputeUtils.filterSelectedDisputeItem(revertClnInvoiceDispute);
		webElementInteractions.clickOnElement(lineItemDispute.viewDetailButtonByDisputeSeqId(revertClnInvoiceDispute.getSeqId()));
		
		logger.info("*** Step 5 Expected Results: - Line Item Dispute Details screen displayed correctly");
		switchToPopUp.switchToPopupWin(5);
		clnInvoiceDisputeDetails = clientDao.getAllClnInvoiceDisputeDetailByClnInvoiceDisputeId(revertClnInvoiceDispute.getSeqId());
		lineItemDisputeDetailsUtils.verifyLineItemDisputeDetailScreenDisplayedCorrectly(clnInvoiceDisputeDetails, revertClnInvoiceDispute.getSeqId());
	
		logger.info("*** Step 6 Action: - Update status for seletected dispute details");
		lineItemDisputeDetailsUtils.updateDisputeDetailsStatus(revertClnInvoiceDisputeDetail.getSeqId(), originClnInvoiceDisputeDetailStatusId);
		
		logger.info("*** Step 7 Action: - Click on Save button");
		webElementInteractions.clickOnElement(lineItemDisputeDetail.saveBtn());
		
		logger.info("*** Step 7 Expected Results: "
				+ "- The Line Item on Dispute Details screen should be closed after saving."
				+ "- Status typ should be updated in clnInvoiceDisputeDetail table."
				+ "- Existing record should be updated in clnInvoiceDisputeStatusEmail");
		lineItemDisputeDetailsUtils.verifySaveBtnIsNotPresent(WAIT_TIME);
		ClnInvoiceDisputeStatusEmail disputeStatusEmailAfterUpdate = clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		lineItemDisputeDetailsUtils.verifyDisputeDetailStatusIsUpdated(revertClnInvoiceDisputeDetail.getSeqId(), originClnInvoiceDisputeDetailStatusId);
		lineItemDisputeUtils.verifyExistingRecordIsUpdatedInClnInvoiceStatusEmail(disputeStatusEmailBeforeUpdate, disputeStatusEmailAfterUpdate);
	}
	
	@Test(description = "Verify that the data should NOT be saved on the new table cln_invoice_dispute_status_email")
	public void verifyDataNotSaveOnTheNewTable() throws Exception {
		logger.info("=== Starting test case verifyDataNotSaveOnTheNewTable ===");
		logger.info("*** Step 1 Expected Results: - Verify the Dispute Notifications grid displayed correctly");
		lineItemDisputeUtils.verifyDisputeNotificationGridDisplayedCorrectly();
		
		logger.info("*** Step 2 Action: - Click on View Details link for the Client ID which the status is 'Pending Review'");
		revertClnInvoiceDispute = clientDao.getListClnInvoiceDisputeNotFinalized().get(0);
		revertClnInvoiceDisputeStatusEmail = clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		lineItemDisputeUtils.filterSelectedDisputeItem(revertClnInvoiceDispute);
		webElementInteractions.clickOnElement(lineItemDispute.viewDetailButtonByDisputeSeqId(revertClnInvoiceDispute.getSeqId()));
		
		logger.info("*** Step 2 Expected Results: - Line Item Dispute Details screen displayed correctly");
		String parentWin = switchToPopUp.switchToPopupWin(5);
		List<ClnInvoiceDisputeDetail> clnInvoiceDisputeDetails = clientDao.getAllClnInvoiceDisputeDetailByClnInvoiceDisputeId(revertClnInvoiceDispute.getSeqId());
		lineItemDisputeDetailsUtils.verifyLineItemDisputeDetailScreenDisplayedCorrectly(clnInvoiceDisputeDetails, revertClnInvoiceDispute.getSeqId());
		
		logger.info("*** Step 3 Action: - Update the dispute status for the accession on the Line Item on Dispute table");
		lineItemDisputeUtils.deleteExistingStatusEmail(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		revertClnInvoiceDisputeDetail = clientDao.getNonFinalizedClnInvoiceDisputeDetailByClnInvoiceDisputeId(revertClnInvoiceDispute.getSeqId());
		int originClnInvoiceDisputeDetailStatusId = revertClnInvoiceDisputeDetail.getDisputeStatusId();
		int intendedDisputeStatusId = IN_DISPUTE_DISPUTE_STATUS_ID + IN_PROGRESS_DISPUTE_STATUS_ID - originClnInvoiceDisputeDetailStatusId;
		lineItemDisputeDetailsUtils.updateDisputeDetailsStatus(revertClnInvoiceDisputeDetail.getSeqId(), intendedDisputeStatusId);
		
		logger.info("*** Step 4 Action: - Click on Save button");
		webElementInteractions.clickOnElement(lineItemDisputeDetail.saveBtn());
		
		logger.info("*** Step 4 Expected Results: "
				+ "- The Line Item on Dispute Details screen should be closed after saving."
				+ "- Status typ should be updated in clnInvoiceDisputeDetail table."
				+ "- New record should be created in clnInvoiceDisputeStatusEmail");
		lineItemDisputeDetailsUtils.verifyDisputeDetailStatusIsUpdated(revertClnInvoiceDisputeDetail.getSeqId(), intendedDisputeStatusId);
		lineItemDisputeUtils.verifyNewRecordIsCreatedInClnInvoiceStatusEmail(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		ClnInvoiceDisputeStatusEmail disputeStatusEmailBeforeUpdate = clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		
		logger.info("*** Step 5 Action: - Selected updated client dispute");
		switchToPopUp.switchToParentWin(parentWin);
		lineItemDisputeUtils.filterSelectedDisputeItem(revertClnInvoiceDispute);
		webElementInteractions.clickOnElement(lineItemDispute.viewDetailButtonByDisputeSeqId(revertClnInvoiceDispute.getSeqId()));
		
		logger.info("*** Step 5 Expected Results: - Line Item Dispute Details screen displayed correctly");
		switchToPopUp.switchToPopupWin(5);
		clnInvoiceDisputeDetails = clientDao.getAllClnInvoiceDisputeDetailByClnInvoiceDisputeId(revertClnInvoiceDispute.getSeqId());
		lineItemDisputeDetailsUtils.verifyLineItemDisputeDetailScreenDisplayedCorrectly(clnInvoiceDisputeDetails, revertClnInvoiceDispute.getSeqId());
	
		logger.info("*** Step 6 Action: - Update comment for seletected dispute details");
		String comment = randomCharacter.getRandomAlphaNumericString(10);
		lineItemDisputeDetailsUtils.updateDisputeDetailsComment(revertClnInvoiceDisputeDetail.getSeqId(), comment);;
		
		logger.info("*** Step 7 Action: - Click on Save button");
		webElementInteractions.clickOnElement(lineItemDisputeDetail.saveBtn());
		
		logger.info("*** Step 7 Expected Results: "
				+ "- The Line Item on Dispute Details screen should be closed after saving."
				+ "- Comment should be updated in clnInvoiceDisputeDetail table."
				+ "- Existing record should be updated in clnInvoiceDisputeStatusEmail");
		ClnInvoiceDisputeStatusEmail disputeStatusEmailAfterUpdate = clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(revertClnInvoiceDispute.getSubmDt(), revertClnInvoiceDispute.getClnId());
		lineItemDisputeDetailsUtils.verifyDisputeDetailCommentIsUpdated(revertClnInvoiceDisputeDetail.getSeqId(), comment);
		lineItemDisputeUtils.verifyExistingRecordIsNotUpdatedInClnInvoiceStatusEmail(disputeStatusEmailBeforeUpdate, disputeStatusEmailAfterUpdate);
	}
	
	@Test(description = "Verify that the Send Notifications button should be disabled when no selected client")
	public void verifySendNotificationButtonDisabledWhenNoSelectedClient() throws Exception {
		logger.info("=== Starting test case verifySendNotificationButtonDisabledWhenNoSelectedClient ===");
		logger.info("*** Step 1 Expected Results: - Verify the Dispute Notifications grid displayed correctly");
		lineItemDisputeUtils.verifyDisputeNotificationGridDisplayedCorrectly();
		
		logger.info("*** Step 2 Action: - Scroll to Send Notifications button");
		webDriverInteractions.scrollToElement(lineItemDispute.sendNotificationsButton());
		
		logger.info("*** Step 2 Expected Results: - Verify Send Notifications button is enabled by default");
		assertTrue(webElementInteractions.isElementEnabled(lineItemDispute.sendNotificationsButton(), WAIT_TIME, true), "        Send Notification button should enabled");
		
		logger.info("*** Step 3 Action: - Clear the All Clients option");
		webElementInteractions.clickOnElement(lineItemDispute.clientDropdownCloseButton());
		
		logger.info("*** Step 3 Expected Results: - Verify Send Notifications button is disabled");
		assertTrue(webElementInteractions.isElementNotPresent(lineItemDispute.clientDropdownCloseButton(), WAIT_TIME), "        The All Clients option should be cleared");
		assertTrue(webElementInteractions.isElementEnabled(lineItemDispute.sendNotificationsButton(), WAIT_TIME, false), "        Send Notification button should disabled");
	}
	
	@Test(description = "Verify the clients dropdownlist should be automatically cleared when switching between All Clients and the specific clients")
	public void verifyTheClientShouldBeClearedWhenSwitchingToAllClients() throws Exception {
		logger.info("=== Starting test case verifyTheClientShouldBeClearedWhenSwitchingToAllClients ===");
		logger.info("*** Step 1 Expected Results: - Verify the Dispute Notifications grid displayed correctly");
		lineItemDisputeUtils.verifyDisputeNotificationGridDisplayedCorrectly();
		
		logger.info("*** Step 2 Expected Results: - Verify the All Clients option is displayed in Clients dropdown");
		assertTrue(lineItemDisputeUtils.isClientDropdownOptionDisplayed("All Clients", WAIT_TIME), "        All Client option should be displayed in Clients dropdown");
		
		logger.info("*** Step 2 Action: - Select random client in the Client dropdown list");
		Cln randomClnDisputeAbbrv = clientDao.getClnByClnId(clientDao.getClnInvoiceDispute().getClnId());
		String clientDropdownOption = randomClnDisputeAbbrv.getClnAbbrev() + " - " + randomClnDisputeAbbrv.getBilAcctNm();
		lineItemDisputeUtils.selectClientsDropdown(randomClnDisputeAbbrv.getClnAbbrev());
		
		logger.info("*** Step 2 Expected Results: - Verify the selected option is displayed");
		assertTrue(lineItemDisputeUtils.isClientDropdownOptionDisplayed(clientDropdownOption, WAIT_TIME), "        Selected client option should be displayed in Clients dropdown");
		
		logger.info("*** Step 3 Action: - Select All Clients option");
		lineItemDisputeUtils.selectClientsDropdown("All Clients");

		logger.info("*** Step 3 Expected Results: - Verify the All Clients option is displayed and selected option is cleared");
		assertTrue(lineItemDisputeUtils.isClientDropdownOptionDisplayed("All Clients", WAIT_TIME), "        All Client option should be displayed in Clients dropdown");
		assertFalse(lineItemDisputeUtils.isClientDropdownOptionDisplayed(clientDropdownOption, WAIT_TIME), "        Selected client option should be displayed in Clients dropdown");
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown() throws Exception {
		
		if (lineItemDisputeUtils == null) {
			lineItemDisputeUtils = new LineItemDisputeUtils(driver, wait, config);
		}
		
		logger.info("=== Starting revert data for test case " + methodName + " ===");
		switch(methodName) {
		case "verifyNewRowAddedWhenUserUpdatesDisputesStatus":
		case "verifyDisputeStatusOverwritedWhenUserUpdateExistingData":
		case "verifyDataNotSaveOnTheNewTable":
			lineItemDisputeUtils.revertDataForFinalizedInvoice(revertClnInvoiceDispute, revertClnInvoiceDisputeDetail, revertClnInvoiceDisputeStatusEmail);
			revertClnInvoiceDispute = null;
			revertClnInvoiceDisputeDetail = null;
			revertClnInvoiceDisputeStatusEmail = null;
			logger.info("=== Revert done for test case " + methodName + " ===");
			break;
		default:
			logger.info("!!!There is no need to revert.");
			break;
		}
	
	}
	
}
