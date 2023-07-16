package com.overall.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnInvoiceDispute.ClnInvoiceDispute;
import com.mbasys.mars.ejb.entity.clnInvoiceDisputeDetail.ClnInvoiceDisputeDetail;
import com.mbasys.mars.ejb.entity.clnInvoiceDisputeStatusEmail.ClnInvoiceDisputeStatusEmail;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.overall.client.lineitemdispute.LineItemDispute;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.rpm.ClientDao;
import com.xifin.qa.dao.rpm.ClientDaoImpl;
import com.xifin.selenium.TextInputElement;
import com.xifin.selenium.WebElementInteractions;
import com.xifin.util.DateConversion;
import com.xifin.utils.Assertions;
import com.xifin.utils.ListUtil;
import com.xifin.utils.TimeStamp;

public class LineItemDisputeUtils extends Assertions {
	private static final Logger logger = Logger.getLogger(LineItemDisputeUtils.class);
	private LineItemDispute lineItemDispute;
	private ClientDao clientDao;
	private TimeStamp timeStamp;
	private ListUtil listUtil;
	private TextInputElement textInputElement;
	private WebElementInteractions webElementInteractions;
	private static int WAIT_TIME = 10;
	
	public LineItemDisputeUtils(RemoteWebDriver driver, WebDriverWait wait, Configuration config) {
		clientDao = new ClientDaoImpl(config.getRpmDatabase());
		lineItemDispute = new LineItemDispute(driver, wait);
		timeStamp = new TimeStamp();
		listUtil = new ListUtil();
		textInputElement = new TextInputElement(driver, wait);
		webElementInteractions = new WebElementInteractions(driver, wait);
	}
	
	public void filterSelectedDisputeItem(ClnInvoiceDispute clnInvoiceDispute) throws Exception {
		String clnAbbrev = clientDao.getClnByClnId(clnInvoiceDispute.getClnId()).getClnAbbrev();
		String disputeAmt = formatDisputeAmount(clnInvoiceDispute.getDisputeAmt());
		String disputeAdj = formatDisputeAmount(clnInvoiceDispute.getDisputeAdj());
		String stmDt = DateConversion.dateToLgYrDtString(clnInvoiceDispute.getSubmDt());
		String disputeDt = DateConversion.dateToLgYrDtString(clnInvoiceDispute.getDisputeDt());
		String disputeTyp = clientDao.getClnInvoiceDisputeTypTypId(clnInvoiceDispute.getDisputeTypId()).getDescr();
		String disputeInitiator = clnInvoiceDispute.getClientPortalUserId();
		
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("clnAbbrev"), clnAbbrev);
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("statementDt"), stmDt);
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("disputeAmt"), disputeAmt);
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("disputeAdj"), disputeAdj);
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("disputeType"), disputeTyp);
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("disputeDt"), disputeDt);
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("disputeInitiator"), disputeInitiator);
	}
	
	public void filterSelectedDisputeItemWithClientId(ClnInvoiceDispute clnInvoiceDispute) throws Exception {
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeClientIdCheckbox(), clientDao.getClnByClnId(clnInvoiceDispute.getClnId()).getClnAbbrev());
	}
	
	public void revertDataForFinalizedInvoice(ClnInvoiceDispute clnInvoiceDispute, ClnInvoiceDisputeDetail clnInvoiceDisputeDetail, ClnInvoiceDisputeStatusEmail clnInvoiceDisputeStatusEmail) throws XifinDataAccessException {
		if (clnInvoiceDispute != null) {
			clnInvoiceDispute.setModified(true);
			clnInvoiceDispute.setResultCode(ErrorCodeMap.RECORD_FOUND);
			clientDao.setClnInvoiceDispute(clnInvoiceDispute);
		}
		if (clnInvoiceDisputeDetail != null) {
			clnInvoiceDisputeDetail.setModified(true);
			clnInvoiceDisputeDetail.setResultCode(ErrorCodeMap.RECORD_FOUND);
			clientDao.setClnInvoiceDisputeDetail(clnInvoiceDisputeDetail);
		}
		// For case new record ClnInvoiceDisputeStatusEmail inserted in db
		if (clnInvoiceDispute != null && clnInvoiceDisputeStatusEmail == null) {
			ClnInvoiceDisputeStatusEmail newClnInvoiceDisputeStatusEmail = clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(clnInvoiceDispute.getSubmDt(), clnInvoiceDispute.getClnId());
			newClnInvoiceDisputeStatusEmail.setResultCode(ErrorCodeMap.DELETED_RECORD);
			clientDao.setClnInvoiceDisputeStatusEmail(newClnInvoiceDisputeStatusEmail);
		}
		// For case existing record ClnInvoiceDisputeStatusEmail updated
		else if (clnInvoiceDisputeStatusEmail != null) {
			clnInvoiceDisputeStatusEmail.setModified(true);
			clnInvoiceDisputeStatusEmail.setResultCode(ErrorCodeMap.RECORD_FOUND);
			clientDao.setClnInvoiceDisputeStatusEmail(clnInvoiceDisputeStatusEmail);
		}
		logger.info("Revert Cln invoice successfully");
	}
	
	public void verifyDisputeNotificationGridDisplayedCorrectly() throws Exception {
		verifyTrue(webElementInteractions.isElementPresent(lineItemDispute.disputeNotificationsGridTitle(), WAIT_TIME), "        Dispute Notification grid title should be displayed");
		verifyTrue(webElementInteractions.isElementPresent(lineItemDispute.disputeNotificationsClientDropdown(), WAIT_TIME), "        Dispute Notifications client dropdown should be displayed");
		verifyTrue(webElementInteractions.isElementPresent(lineItemDispute.onlySendForUpdatedDisputesCheckbox(), WAIT_TIME), "        Only Send For Updated Disputes Checkbox should be displayed");
		verifyTrue(webElementInteractions.isElementPresent(lineItemDispute.sendNotificationsButton(), WAIT_TIME), "        Send Notifications button should be displayed");
	}
	
	public String getStatementDateOnGridFiltered() {
		String value = lineItemDispute.lineItemDisputeFirstRow("tbl_lineItemDispute_statementDt").getText();
		logger.info("StatementDate on grid: " + value);
		return value;
	}
	
	public List<String> getListStatementDateOnGridFiltered() {
		List<String> statementDates = new ArrayList<>();
		for (WebElement statementDate : lineItemDispute.lineItemStatementDate()) {
			statementDates.add(statementDate.getText());
		}
		logger.info("Total statement date found: " + statementDates.size());
		return statementDates;
	}
	
	public String selectClientInDropdownListByClientId(int clientId) throws Exception {
		Cln cln = clientDao.getClnByClnId(clientId);
		String clnName = cln.getClnAbbrev() + " - " + cln.getBilAcctNm();
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.clientsInputDropdownList(), cln.getClnAbbrev());
		return clnName;
	}
	
	public void selectAllClientInDropdownList() throws Exception {
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.clientsInputDropdownList(), "All Clients");
	}
	
	public void deleteExistingStatusEmail(java.sql.Date submDt, int clnId) throws XifinDataAccessException {
		ClnInvoiceDisputeStatusEmail clnInvoiceDisputeStatusEmail = clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(submDt, clnId);
		if (clnInvoiceDisputeStatusEmail != null) {
			clnInvoiceDisputeStatusEmail.setResultCode(ErrorCodeMap.DELETED_RECORD);
			clientDao.setClnInvoiceDisputeStatusEmail(clnInvoiceDisputeStatusEmail);
		}
		verifyNull(clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(submDt, clnId), "        clnInvoiceDisputeStatusEmail should be deleted");
		logger.info("*** Delete clnInvoiceDisputeStatusEmail successfully ***");
	}
	
	public void verifyNewRecordIsCreatedInClnInvoiceStatusEmail(java.sql.Date submDt, int clnId) throws XifinDataAccessException {
		ClnInvoiceDisputeStatusEmail clnInvoiceDisputeStatusEmail = clientDao.getClnInvoiceDisputeStatusEmailBySubmDtAndClnId(submDt, clnId);
		verifyDateWithDate(clnInvoiceDisputeStatusEmail.getDisputeStatusChangeDt(), timeStamp.getCurrentDateAsSqlDate(), "        DisputeStatusChangeDt should match");
	}
	
	public void verifyExistingRecordIsUpdatedInClnInvoiceStatusEmail(ClnInvoiceDisputeStatusEmail beforeRecord, ClnInvoiceDisputeStatusEmail afterRecord) {
		Date beforeDt = beforeRecord.getDisputeStatusChangeDt();
		Date afterDt = afterRecord.getDisputeStatusChangeDt();
		verifyDateWithDate(beforeDt, timeStamp.getCurrentDateAsSqlDate(), "        DisputeStatusChangeDt should match");
		verifyDateWithDate(afterDt, timeStamp.getCurrentDateAsSqlDate(), "        DisputeStatusChangeDt should match");
		verifyTrue(afterDt.getTime() > beforeDt.getTime(), "        Dispute Status Change Date should be updated");
	}
	
	public void verifyExistingRecordIsNotUpdatedInClnInvoiceStatusEmail(ClnInvoiceDisputeStatusEmail beforeRecord, ClnInvoiceDisputeStatusEmail afterRecord) {
		Date beforeDt = beforeRecord.getDisputeStatusChangeDt();
		Date afterDt = afterRecord.getDisputeStatusChangeDt();
		verifyDateWithDate(afterDt, beforeDt, "        Dispute Status Change Date should not be updated");
	}
	
	public int getRecordInLineItemDispute(String type) throws Exception {
		List<ClnInvoiceDispute> clnInvoiceDisputeList = clientDao.getListClnInvoiceDisputeNotFinalized();
		List<String> clnAbbrevList = new ArrayList<>();
	
		for (ClnInvoiceDispute item : clnInvoiceDisputeList) {
			clnAbbrevList.add(clientDao.getClnByClnId(item.getClnId()).getClnAbbrev());
		}
		
		//Remove duplicate clnAbbrev
		clnAbbrevList = listUtil.removeDuplicateStringInList(clnAbbrevList);
		
		//Filter clnAbbrev
		for (String clnAbbrev : clnAbbrevList) {
			textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("clnAbbrev"), clnAbbrev);
			
			//Get all statement date
			List<String> statementDateList = new ArrayList<>();
 			for (WebElement e : lineItemDispute.lineItemDisputeSpecificCol("tbl_lineItemDispute_statementDt")) {
				statementDateList.add(e.getText());
			}
			
			statementDateList = listUtil.removeDuplicateStringInList(statementDateList);
			
			if (type.equals("cc")) {
				for (String statementDate : statementDateList) {
					textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("statementDt"), statementDate);
					if(lineItemDispute.lineItemDisputeAllRows().size() == 1 || listUtil.removeDuplicateStringInList(getDisputeInitiatorFiltered()).size() == 1) {
						return Integer.valueOf(webElementInteractions.getAttributeFromWebElement(lineItemDispute.lineItemDisputeFirstRow("tbl_lineItemDispute_seqId"), "title"));
					}
				}
			} else if (type.equals("bcc")) {
				// If statement are the same -> is likely bcc
				if (statementDateList.size() > 1) {
  					for (String statementDate : statementDateList) {
 						textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("statementDt"), statementDate);
						
  						if (lineItemDispute.lineItemDisputeAllRows().size() > 1) {
							List<String> disputeInitiatorList = getDisputeInitiatorFiltered();
							
							//If list of Dispute Initiator more than 1 -> definitely bcc case
 							if(listUtil.removeDuplicateStringInList(disputeInitiatorList).size() > 1) {
								return Integer.valueOf(webElementInteractions.getAttributeFromWebElement(lineItemDispute.lineItemDisputeFirstRow("tbl_lineItemDispute_seqId"), "title"));
							}
						}
					}
				}
			}
		}
		
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.lineItemDisputeTextboxByName("clnAbbrev"), clnAbbrevList.get(0));
		return Integer.valueOf(webElementInteractions.getAttributeFromWebElement(lineItemDispute.lineItemDisputeFirstRow("tbl_lineItemDispute_seqId"), "title"));
	}
	
	private List<String> getDisputeInitiatorFiltered() {
		List<String> disputeInitiatorList = new ArrayList<>();
		for (WebElement e : lineItemDispute.lineItemDisputeSpecificCol("tbl_lineItemDispute_disputeInitiator")) {
			disputeInitiatorList.add(e.getText());
		}
		
		return disputeInitiatorList;
	}
	
	public void selectClientsDropdown(String dropdownOption) throws Exception {
		textInputElement.sendKeysWithClearAndTab(lineItemDispute.clientDropdownInput(), dropdownOption);
	}
	
	public boolean isClientDropdownOptionDisplayed(String dropdownOption, int timeOut) throws Exception {
		try {
			
			return webElementInteractions.isElementPresent(lineItemDispute.clientDropdownSelectedOption(dropdownOption), timeOut);
		
		} catch (Exception e) {
			
			logger.info("All Clients option is displayed in Clients dropdown ", e);
		}
		return false;
	}
	
	public void verifySendNotificationErrorIsDisplayed() throws InterruptedException {
		verifyTrue(webElementInteractions.isElementPresent(lineItemDispute.sendNotificationErrorLabel(), WAIT_TIME), "        Send Notification error pop up should be displayed");
		verifyStringWithString(lineItemDispute.sendNotificationErrorLabel().getText(), "Unable to send notification because no updates have been made to the existing disputes.", "        Send Notification error pop up text should be corrected");
	}
	
	private String formatDisputeAmount(float disputeAmt) {
		if (disputeAmt == (long) disputeAmt) {
			return String.format("%d", (long) disputeAmt);
		}
		return String.format("%s", disputeAmt);
	}
	
	public void verifyDisputeStatusEmailSentDtUpdated(ClnInvoiceDisputeStatusEmail clnInvDisStaEmail, ClnInvoiceDisputeStatusEmail disputeStatusEmailBeforeUpdate, ClnInvoiceDisputeStatusEmail disputeStatusEmailAfterUpdate) {
		// If clnInvDisStaEmail is null, this is new record just inserted into ClnInvoiceDisputeStatusEmail tbl
		// if not there is an existing record
		if (clnInvDisStaEmail == null || clnInvDisStaEmail.getDisputeStatusEmailSentDt() == null) {
			logger.info("New record just inserted into ClnInvoiceDisputeStatusEmail tbl, verify DisputeStatusEmailSentDt is null");
			verifyNotNull(disputeStatusEmailAfterUpdate.getDisputeStatusEmailSentDt(), "        DisputeStatusEmailSentDt should null");
		} else {
			logger.info("Existing record just updated in ClnInvoiceDisputeStatusEmail, verify DisputeStatusEmailSentDt updated");
			verifyTrue(disputeStatusEmailAfterUpdate.getDisputeStatusEmailSentDt().getTime() > disputeStatusEmailBeforeUpdate.getDisputeStatusEmailSentDt().getTime(), "        Time of DisputeStatusEmailSentDt should updated");
		}
	}
	
	public void unCheckedOnlySendForUpdatedDisputes() throws Exception {
		if (lineItemDispute.onlySendForUpdatedDisputesCheckbox().isSelected()) {
			webElementInteractions.clickOnElement(lineItemDispute.onlySendForUpdatedDisputesCheckbox());
		}
	}
}
