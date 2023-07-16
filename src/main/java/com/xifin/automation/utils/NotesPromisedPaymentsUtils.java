package com.xifin.automation.utils;

import java.util.Properties;

import com.xifin.utils.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.overall.accession.notesPromisedPayments.NotesPromisedPayments;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;

public class NotesPromisedPaymentsUtils {
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private Configuration config;
	private SeleniumBaseTest baseTest;
	private NotesPromisedPayments notesPromisedPayments;
	private RemoteWebDriver driver;
	private WebDriverWait wait;
	private static final String FIRST_ROW = "2";
	
	public NotesPromisedPaymentsUtils(Configuration config, RemoteWebDriver driver, WebDriverWait wait) {
		this.config = config;
		this.driver = driver;
		this.wait = wait;
	}
	
	/**
     * Create new accession
     * @param project, testSuite, testCase
     * @return accnId String
     * @throws Exception
     */
	public String createANewAccn(String project, String testSuite, String testCase) throws Exception {
		logger.info("*** Creating new Accn");
		logger.info("*** OrgAlias: " + config.getProperty(PropertyMap.ORGALIAS));
		logger.info("*** endpoint ACCNWS_URL = " + config.getProperty(PropertyMap.ACCNWS_URL));
		Properties resultProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("*** Expected Results: - Verify that a new accnId is available for using");
		Assert.assertNotNull(accnId, "        A new AccnId should be created.");
		logger.info("*** Step Expected Results: - Ensure the new accnId was created :" + accnId);

		return accnId;
	}	
	/**
     * Search for the record by filter in Contact Detail grid
     * @param userId, contactDate, contactInfo, followUpDate, followUpIndividual
     * @throws Exception
     */
	public void searchNewRecordDislayedInContactDetail(String userId, String contactDate, String contactInfo, String followUpDate, String followUpIndividual) throws Exception {
		baseTest = new SeleniumBaseTest(driver);
		notesPromisedPayments = new NotesPromisedPayments(driver, wait);
		baseTest.enterValues(notesPromisedPayments.inputUserID(), userId);
		baseTest.enterValues(notesPromisedPayments.inputContactDate(), contactDate);
		baseTest.enterValues(notesPromisedPayments.inputContactInfo(), contactInfo);
		baseTest.enterValues(notesPromisedPayments.inputFollowupDate(), followUpDate);
		baseTest.enterValues(notesPromisedPayments.inputFollowUpIndividual(), followUpIndividual);
	}
	/**
     * Verify the record was added in ContactNote Detail grid
     * @param userId, contactDate, manualActivityCode, contactInfo, followUpDate, followUpIndividual
     * @throws Exception
     */
	public void verifyNewRecordAddedContactNotesDetail(String userId, String contactDate, String manualActivityCode, String contactInfo, String followUpDate, String followUpIndividual) throws Exception {		
		notesPromisedPayments = new NotesPromisedPayments(driver, wait);
		Assert.assertEquals(notesPromisedPayments.contactDetailCol(FIRST_ROW, "3").getText().trim(), userId);
		Assert.assertEquals(notesPromisedPayments.contactDetailCol(FIRST_ROW, "4").getText().trim(), contactDate);
		Assert.assertEquals(notesPromisedPayments.contactDetailCol(FIRST_ROW, "5").getText().trim(), manualActivityCode);
		Assert.assertEquals(notesPromisedPayments.contactDetailCol(FIRST_ROW, "6").getText().trim(), contactInfo);
		Assert.assertEquals(notesPromisedPayments.contactDetailCol(FIRST_ROW, "7").getText().trim(), followUpDate);
		Assert.assertEquals(notesPromisedPayments.contactDetailCol(FIRST_ROW, "9").getText().trim(), followUpIndividual);
	}
	/**
     * Clear all filter input value in Contact Detail grid
     * @throws Exception
     */
	public void clearAllFilterInput() throws Exception {
		notesPromisedPayments = new NotesPromisedPayments(driver, wait);
		notesPromisedPayments.inputUserID().clear();
		notesPromisedPayments.inputContactDate().clear();
		notesPromisedPayments.inputContactInfo().clear();
		notesPromisedPayments.inputFollowupDate().clear();
		notesPromisedPayments.inputFollowUpIndividual().clear();
	}
}
