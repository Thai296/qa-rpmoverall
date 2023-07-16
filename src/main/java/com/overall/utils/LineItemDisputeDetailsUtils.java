package com.overall.utils;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mbasys.mars.ejb.entity.clnInvoiceDisputeDetail.ClnInvoiceDisputeDetail;
import com.mbasys.mars.ejb.entity.disputeStatusTyp.DisputeStatusTyp;
import com.overall.client.lineitemdispute.LineItemDisputeDetail;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.ClientDao;
import com.xifin.qa.dao.rpm.ClientDaoImpl;
import com.xifin.qa.dao.rpm.DisputeDao;
import com.xifin.qa.dao.rpm.DisputeDaoImpl;
import com.xifin.selenium.SelectDropDown;
import com.xifin.selenium.TextInputElement;
import com.xifin.selenium.WebDriverInteractions;
import com.xifin.selenium.WebElementInteractions;
import com.xifin.utils.Assertions;
import com.xifin.utils.ConvertUtil;

public class LineItemDisputeDetailsUtils extends Assertions {
	private static final Logger logger = Logger.getLogger(LineItemDisputeDetailsUtils.class);
	private ClientDao clientDao;
	private DisputeDao disputeDao;
	private LineItemDisputeDetail lineItemDisputeDetail;
	private WebElementInteractions webElementInteractions;
	private WebDriverInteractions webDriverInteractions;
	private SelectDropDown selectDropDown;
	private TextInputElement textInputElement;
	
	public LineItemDisputeDetailsUtils(RemoteWebDriver driver, WebDriverWait wait, Configuration config) {
		clientDao = new ClientDaoImpl(config.getRpmDatabase());
		disputeDao = new DisputeDaoImpl(config.getRpmDatabase());
		lineItemDisputeDetail = new LineItemDisputeDetail(wait);
		webElementInteractions = new WebElementInteractions(driver, wait);
		webDriverInteractions = new WebDriverInteractions(driver);
		selectDropDown = new SelectDropDown(driver, wait);
		textInputElement = new TextInputElement(driver, wait);
	}
	
	public void verifyLineItemDisputeDetailScreenDisplayedCorrectly(List<ClnInvoiceDisputeDetail> clnInvoiceDisputeDetailList, int clnInvoiceDisputeSeqId)throws StaleElementReferenceException, InterruptedException, XifinDataAccessException, XifinDataNotFoundException {
		
		String disputeTyp = clientDao.getClnInvoiceDisputeTypTypId(clientDao.getClnInvoiceDisputeBySeqId(clnInvoiceDisputeSeqId).getDisputeTypId()).getDescr();
		for(ClnInvoiceDisputeDetail clnInvoiceDisputeDetail : clnInvoiceDisputeDetailList) {
			int seqId = clnInvoiceDisputeDetail.getSeqId();
			if(disputeTyp.equals("Dispute Price")) {
				String actualExpCharge = webElementInteractions.getAttributeFromWebElement(lineItemDisputeDetail.lineItemOnDisputeGridValue(seqId, disputeTyp, "expCharge"), "title");
				String actualDisputeAmt = webElementInteractions.getAttributeFromWebElement(lineItemDisputeDetail.lineItemOnDisputeGridValue(seqId, disputeTyp, "disputeAmt"), "title");
				verifyStringWithString(ConvertUtil.convertFloatOrDoubleToString(clnInvoiceDisputeDetail.getExpTestPrc()), actualExpCharge, "Expected Charge should be corrected");
				verifyStringWithString(ConvertUtil.convertFloatOrDoubleToString(clnInvoiceDisputeDetail.getLineItemDisputeAmt()), actualDisputeAmt, "Dispute Amount should be corrected");
			}
			String actualAccnId = webElementInteractions.getAttributeFromWebElement(lineItemDisputeDetail.lineItemOnDisputeGridValue(seqId, disputeTyp, "accnId"), "title");
			String actualStatus = webElementInteractions.getAttributeFromWebElement(lineItemDisputeDetail.lineItemOnDisputeGridValue(seqId, disputeTyp, "statusId"), "title");
			String actualClientNotes = webElementInteractions.getAttributeFromWebElement(lineItemDisputeDetail.lineItemOnDisputeGridValue(seqId, disputeTyp, "clientNotes"), "title");
			verifyStringWithString(clnInvoiceDisputeDetail.getAccnId(), actualAccnId, "        Accn Id should be corrected");
			verifyStringWithString(disputeDao.getDisputeStatusTypByTypId(clnInvoiceDisputeDetail.getDisputeStatusId()).getDescr(), actualStatus, "        Dispute status should be corrected");
			verifyStringWithString(clnInvoiceDisputeDetail.getClnDisputeNotes(), actualClientNotes, "        Client notes should be corrected");
		}
	}
	
	public void updateDisputeDetailsStatus(int seqId, int statusTypId) throws Exception {
		webDriverInteractions.scrollToElement(lineItemDisputeDetail.disputeDetailLineById(seqId));
		webElementInteractions.clickOnElement(lineItemDisputeDetail.disputeDetailLineById(seqId));
		webElementInteractions.clickOnElement(lineItemDisputeDetail.statusDropdown());
		selectDropDown.selectOptionByVisibleText(lineItemDisputeDetail.statusDropdown(), disputeDao.getDisputeStatusTypByTypId(statusTypId).getDescr());
	}
	
	public int updateDisputeDetailsStatus(ClnInvoiceDisputeDetail existClnInvDisputeDetail) throws Exception {
		DisputeStatusTyp updatedStatusType = disputeDao.getRandomDisputeStatusTypNotEqualsGivenId(existClnInvDisputeDetail.getDisputeStatusId());
		
		webElementInteractions.clickOnElement(lineItemDisputeDetail.disputeDetailLineById(existClnInvDisputeDetail.getSeqId()));
		webElementInteractions.clickOnElement(lineItemDisputeDetail.statusDropdown());
		selectDropDown.selectOptionByVisibleText(lineItemDisputeDetail.statusDropdown(), updatedStatusType.getDescr());
		
		return updatedStatusType.getDisputeStatusId();
	}
	
	public void updateDisputeDetailsComment(int seqId, String comment) throws Exception {
		webDriverInteractions.scrollToElement(lineItemDisputeDetail.disputeDetailLineById(seqId));
		webElementInteractions.clickOnElement(lineItemDisputeDetail.disputeDetailLineById(seqId));
		textInputElement.enterValues(lineItemDisputeDetail.commentTextbox(seqId), comment);
	}
	
	public void verifyDisputeDetailStatusIsUpdated(int seqId, int disputeDetailsStatusId) throws XifinDataAccessException, XifinDataNotFoundException {
		ClnInvoiceDisputeDetail clnInvoiceDisputeDetail = clientDao.getClnInvoiceDisputeDetailBySeqId(seqId);
		verifyEquals(clnInvoiceDisputeDetail.getDisputeStatusId(), disputeDetailsStatusId, "        ClnInvoiceDisputeDetail status id should be updated");
	}
	
	public void verifyDisputeDetailCommentIsUpdated(int seqId, String comment) throws XifinDataAccessException, XifinDataNotFoundException {
		ClnInvoiceDisputeDetail clnInvoiceDisputeDetail = clientDao.getClnInvoiceDisputeDetailBySeqId(seqId);
		verifyStringWithString(clnInvoiceDisputeDetail.getComments(), comment, "        Dispute detail comment should be corrected");
	}
	
	public void verifySaveBtnIsNotPresent(int waitTime) throws Exception {
		verifyTrue(webElementInteractions.isElementNotPresent(lineItemDisputeDetail.saveBtn(), waitTime), "The Save button should not present");
	}
}
