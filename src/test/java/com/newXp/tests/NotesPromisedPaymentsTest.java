package com.newXp.tests;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mbasys.mars.ejb.entity.accnCntct.AccnCntct;
import com.mbasys.mars.ejb.entity.accnCntctManActivityTyp.AccnCntctManActivityTyp;
import com.overall.accession.accessionProcessing.LoadAccession;
import com.overall.accession.notesPromisedPayments.NotesPromisedPayments;
import com.overall.menu.MenuNavigation;
import com.xifin.automation.utils.NotesPromisedPaymentsUtils;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;

public class NotesPromisedPaymentsTest extends SeleniumBaseTest {
	private NotesPromisedPayments notesPromisedPayments;
	private NotesPromisedPaymentsUtils notesPromisedPaymentsUtils;
	private LoadAccession loadAccession;
	private RandomCharacter randomCharacter;
	private TimeStamp timeStamp;
	private MenuNavigation navigation;
	private AccnCntctManActivityTyp accnCntctManActivityTyp;
	private AccnCntctManActivityTyp accnCntctManActivityBefore;
	private AccnCntctManActivityTyp accnCntctManActivityAfter;
	private boolean noFollowUpBefore;
	private boolean noFollowUpAfter;
	
	private final static String DATE_FORMAT = "MM/dd/yyyy";
	private final static String EMPTY_STRING = "";
	private String accnId;
	
    @BeforeMethod(alwaysRun = true)
    @Parameters({"ssoUsername", "ssoPassword"})
    public void beforeMethod(String ssoUsername, String ssoPassword, Method method)
    {
    	notesPromisedPaymentsUtils = new NotesPromisedPaymentsUtils(config, driver, wait);
    	notesPromisedPayments = new NotesPromisedPayments(driver, wait);
    	loadAccession = new LoadAccession(driver);
    	randomCharacter = new RandomCharacter();
    	timeStamp = new TimeStamp();    	
        try
        {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "["+method.getName()));
			navigation = new MenuNavigation(driver, config);
			logIntoSso(ssoUsername, ssoPassword);
        }
        catch (Exception e)
        {
            logger.error("Error running BeforeMethod", e);
        }
    }
    
    @AfterMethod(alwaysRun = true)
    public void afterMethod() throws Exception {
    	logger.info("**** Reverting data");
    	if(accnId != null) {
	    	accessionDao.deleteAccn(accnId);
	    	accnId = null;
    	}    	
    	if(accnCntctManActivityTyp != null) {
    		accnCntctManActivityTyp.setIsNoFollowUp(noFollowUpBefore);
            accnCntctManActivityTyp.setModified(true);
            accessionDao.setAccnCntctManActivityTyp(accnCntctManActivityTyp);
            accnCntctManActivityTyp = null;
    	}   	
    	if(accnCntctManActivityBefore != null) {
    		accnCntctManActivityBefore.setIsNoFollowUp(noFollowUpBefore);
    		accnCntctManActivityBefore.setModified(true);
            accessionDao.setAccnCntctManActivityTyp(accnCntctManActivityBefore);
            accnCntctManActivityBefore = null;
    	}    	
    	if(accnCntctManActivityAfter != null) {
    		accnCntctManActivityAfter.setIsNoFollowUp(noFollowUpAfter);
    		accnCntctManActivityAfter.setModified(true);
            accessionDao.setAccnCntctManActivityTyp(accnCntctManActivityAfter);
            accnCntctManActivityAfter = null;
    	}
    }
    
    @Test(priority = 1, description = "Verify that Follow-up date and Follow-up individual fields are disabled when user selects Manual Activity Code that has b_no_follow_up = 1 on Notes and Promised payments screen")
	@Parameters({"project", "testSuite", "testCase"})
	public void fUpDtAndFUpIndDisabledWhenManActivityCdNoFUp1(String project, String testSuite, String testCase) throws Exception {
        logger.info("==== Testing - testC16735 ==== ");
        logger.info("*** Prepare data");
        // Setup AccnCntctManActivityTyp with b_no_follow_up = 1
	    accnCntctManActivityTyp = accessionDao.getAccnCntctManActivityTypById(1);
		noFollowUpBefore = accnCntctManActivityTyp.getIsNoFollowUp();
		accnCntctManActivityTyp.setIsNoFollowUp(true);
		accnCntctManActivityTyp.setModified(true);
		accessionDao.setAccnCntctManActivityTyp(accnCntctManActivityTyp);
		clearDataCache();
		navigation.navigateToAccessionNotesPromisedPayment();
        
        logger.info("*** Step 1: Expected: Notes & Promised Payments page is displayed");        
        Assert.assertTrue(isElementPresent(notesPromisedPayments.loadAccessionSectionAccessionIdInput(), 5), "        The Notes & Promised Payments page should be displayed");
        
        logger.info("*** Step 2: Create new accn");
        accnId = notesPromisedPaymentsUtils.createANewAccn(project,testSuite,testCase);
        
        logger.info("*** Step 2: Action: Input accn ID into search field");
        loadAccession.setAccnId(accnId);
        
        logger.info("*** Step 2: Expected: Notes & Promised Payments detail page is displayed");    
        Assert.assertTrue(isElementPresent(notesPromisedPayments.notesPromisedPaymentsForm(), 60), "   Notes & Promised Payments detail page should be displayed");
        
        logger.info("*** Step 3: Action: In Contact Detail grid, click on Add button");    
        clickOnElement(notesPromisedPayments.addRecordButton());
        
        logger.info("*** Step 3: Expected: The Add Record pop-up display");    
        Assert.assertTrue(isElementPresent(notesPromisedPayments.editModTable(), 60), "   contact Detail Add Record Form should be displayed");
               
        String contactDate = getInputVal(notesPromisedPayments.contactDateText());
        String userId = notesPromisedPayments.userIdText().getText();
        String manualActivityCode = accnCntctManActivityTyp.getAbbrev() + " - " + accnCntctManActivityTyp.getDescr();

        logger.info("*** Step 4: Action: Action: Select 'Manual Activity Code' that has ACCN_CNTCT_MAN_ACTIVITY_TYP.B_NO_FOLLOW_UP = 1 and tab out");    
        selectFromDropdown(notesPromisedPayments.manualActivityCodeDropdown(), manualActivityCode);
        
        logger.info("*** Step 4: Expected: The 'Follow-up Date' and 'Follow-up Individual' are disable and cleared correctly");    
        Assert.assertFalse(notesPromisedPayments.followUpDateStateBar().isEnabled(), "  Follow-up Date should be disabled");
        Assert.assertTrue(isElementDisabled(notesPromisedPayments.followUpIndividualDropdown(), "class", "disabled", 2), "  Follow-up Individual should be disabled");
        Assert.assertEquals(notesPromisedPayments.followUpDateStateBar().getAttribute("value"), EMPTY_STRING, "  Follow-up Date should be blank");
        Assert.assertEquals(notesPromisedPayments.followUpIndividualDropdown().getText(), EMPTY_STRING, "  Follow-up Individual should be blank");
        
        logger.info("*** Step 5: Action: Input value to 'Contact Info', click 'Save' button");
        String contactInfo = randomCharacter.getRandomAlphaString(10);
        setInputValue(notesPromisedPayments.contactInfoTextArea(), contactInfo);
        clickOnElement(notesPromisedPayments.saveButtonInAddRecordAndEditRecordPopup());
        
        logger.info("*** Step 5: Expected: - The 'Add Record' pop-up closes "
        								+ "- Accession Notes & Promised Payment screen displayed "
        								+ "- New record is displayed on Contact Detail grid");  
        Assert.assertFalse(notesPromisedPayments.editModTable().isDisplayed(), "The add record popup should be not displayed");
        Assert.assertTrue(isElementPresent(notesPromisedPayments.notesPromisedPaymentsForm(), 5), "        The Notes & Promised Payments page should be displayed");
        notesPromisedPaymentsUtils.verifyNewRecordAddedContactNotesDetail(userId, contactDate, manualActivityCode, contactInfo, EMPTY_STRING, EMPTY_STRING);
        
        logger.info("*** Step 6: Action: Click Save and Clear button");
        clickHiddenPageObject(notesPromisedPayments.saveAndClearButton(), 0);
        
        logger.info("*** Step 6: Expected: - Data is saved successfully, Accession Notes & Promised Payment screen step 1 is displayed"
        								+ "- New record is added into table ACCN_CNTCT with correct data");
        int accnManId = accnCntctManActivityTyp.getAccnCntctManTypId(); 
        AccnCntct accnCntct = accessionDao.getAccnCntctByAccnIdAndCntctInfo(accnId, contactInfo);
        Assert.assertTrue(isElementPresent(notesPromisedPayments.loadAccessionSectionAccessionIdInput(), 5), "        The Notes & Promised Payments page should be displayed");
		AccnCntctManActivityTyp accnCntctManActivityTypInDb = accessionDao.getAccnCntctManActivityTypById(accnManId);
		String expectedManualActivityCode = accnCntctManActivityTypInDb.getAbbrev() + " - " + accnCntctManActivityTypInDb.getDescr();
		String expectedContactDate = new SimpleDateFormat(DATE_FORMAT).format(accnCntct.getCntctDt());
		Assert.assertEquals(accnCntct.getUserId(), userId, "   User Id should be added into ACCN_CNTCT table as inputted");
		Assert.assertEquals(expectedContactDate, contactDate, "   Contact Date should be added into ACCN_CNTCT table as inputted");
		Assert.assertEquals(expectedManualActivityCode, manualActivityCode, "   Manual Activity Code should be added into ACCN_CNTCT table as inputted");
		Assert.assertEquals(accnCntct.getCntctInfo(), contactInfo, "   Contact Info should be added into ACCN_CNTCT table as inputted");
		Assert.assertNull(accnCntct.getFollowUpDt(), "   Follow-up Date should be null"); // Follow-up Date was a blank as selecting 'Manual Activity Code' that has ACCN_CNTCT_MAN_ACTIVITY_TYP.B_NO_FOLLOW_UP = 1
    	Assert.assertNull(accnCntct.getFollowUpUserId(), "   Follow-up Individual should be null"); // Follow-up Individual was a blank as selecting 'Manual Activity Code' that has ACCN_CNTCT_MAN_ACTIVITY_TYP.B_NO_FOLLOW_UP = 1
    }
    	
	@Test(priority = 1, description = "Verify Update Contact Detail value correctly when changed Manual Activity Code from the one that have b_no_follow_up = 0 to one that has b_no_follow_up = 1 on Notes and Promised payments screen")
	@Parameters({"project", "testSuite", "testCase"})
	public void updateCntctDetailWhenManActivityCdFromNoFUp0ToNoFUp1(String project, String testSuite, String testCase) throws Exception {
        logger.info("==== Testing - testC16736 ==== ");
        logger.info("*** Prepare data");
        // Setup AccnCntctManActivityTyp with b_no_follow_up = 0
        accnCntctManActivityBefore = accessionDao.getAccnCntctManActivityTypById(1);
        noFollowUpBefore = accnCntctManActivityBefore.getIsNoFollowUp();
        accnCntctManActivityBefore.setIsNoFollowUp(false);
        accnCntctManActivityBefore.setModified(true);
        accessionDao.setAccnCntctManActivityTyp(accnCntctManActivityBefore);
        // Setup AccnCntctManActivityTyp with b_no_follow_up = 1
        accnCntctManActivityAfter = accessionDao.getAccnCntctManActivityTypById(2);
        noFollowUpAfter = accnCntctManActivityAfter.getIsNoFollowUp();
        accnCntctManActivityAfter.setIsNoFollowUp(true);
        accnCntctManActivityAfter.setModified(true);
        accessionDao.setAccnCntctManActivityTyp(accnCntctManActivityAfter);
        clearDataCache();
		navigation.navigateToAccessionNotesPromisedPayment();
        
        logger.info("*** Expected: Notes & Promised Payments page is displayed");        
        Assert.assertTrue(isElementPresent(notesPromisedPayments.loadAccessionSectionAccessionIdInput(), 5), "        The Notes & Promised Payments page should be displayed");
        
        logger.info("*** Create new accn");
        accnId = notesPromisedPaymentsUtils.createANewAccn(project,testSuite,testCase);
        
        logger.info("*** Step 1: Action: Input accn ID into search field");
        loadAccession.setAccnId(accnId);
        
        logger.info("*** Step 1: Expected: Notes & Promised Payments detail page is displayed");    
        Assert.assertTrue(isElementPresent(notesPromisedPayments.notesPromisedPaymentsForm(), 60), "   Notes & Promised Payments detail page should be displayed");
        
        logger.info("*** Step 2: Action: In 'Contact Detail' grid, click on 'Add' button");
        clickHiddenPageObject(notesPromisedPayments.addRecordButton(), 0);
        
        logger.info("*** Step 2: Expected: The 'Add Record' pop-up is displayed");    
        Assert.assertTrue(isElementPresent(notesPromisedPayments.editModTable(), 5), "The add record popup should be displayed");
        
        logger.info("*** Step 3: Action: Click on Manual Activity Code dropdown, select record that has ACCN_CNTCT_MAN_ACTIVITY_TYP.B_NO_FOLLOW_UP = 0");
        String contactDate = getInputVal(notesPromisedPayments.contactDateText());
        String userId = notesPromisedPayments.userIdText().getText();       
        String accnCntctManActivityTypBefore = accnCntctManActivityBefore.getAbbrev() + " - " + accnCntctManActivityBefore.getDescr();
        selectDropDown(notesPromisedPayments.manualActivityCodeDropdown(), accnCntctManActivityTypBefore);
        
        logger.info("*** Step 3: Expected: Follow-up Date' and 'Follow-up Individual' are enabled and blank");
        Assert.assertTrue(notesPromisedPayments.followUpDateStateBar().isEnabled(), "  Follow-up Date should be enabled");
        Assert.assertTrue(notesPromisedPayments.followUpIndividualDropdown().isEnabled(), "  Follow-up Individual should be enabled");
        Assert.assertEquals(notesPromisedPayments.followUpDateStateBar().getAttribute("value"), EMPTY_STRING, "  Follow-up Date should be blank");
        Assert.assertEquals(notesPromisedPayments.followUpIndividualDropdown().getText(), EMPTY_STRING, "  Follow-up Individual should be blank");
        
        logger.info("*** Step 4: Action: Input valid values for 'Contact Info', 'Follow-up Date' and 'Follow-up Individual' fields and then Save 'Add Record' pop-up");
        String contactInfo = randomCharacter.getRandomAlphaString(10);
        String followUpDate = timeStamp.getFutureDate(DATE_FORMAT, Integer.parseInt(accnCntctManActivityBefore.getDefaultDays() + randomCharacter.getRandomNumericString(2)));
        String followUpIndividual = clientDao.getClnCntctFollowUpUserIdNotNull().getFollowUpUserId();
        
        setInputValue(notesPromisedPayments.contactInfoTextArea(), contactInfo);        
		setInputValue(notesPromisedPayments.followUpDateStateBar(), followUpDate);
		selectDropDown(notesPromisedPayments.followUpIndividualDropdown(), followUpIndividual);
        clickHiddenPageObject(notesPromisedPayments.saveButtonInAddRecordAndEditRecordPopup(), 0);
        
        logger.info("*** Step 4: Expected: - The 'Add Record' pop-up closes"
        								+ "- Accession Notes & Promised Payment screen displayed "
        								+ "- New record is displayed on Contact Detail grid");  
        Assert.assertTrue(isElementPresent(notesPromisedPayments.notesPromisedPaymentsForm(), 5), "        The Notes & Promised Payments page should be displayed");
        notesPromisedPaymentsUtils.searchNewRecordDislayedInContactDetail(userId, contactDate, contactInfo, followUpDate, followUpIndividual);
        notesPromisedPaymentsUtils.verifyNewRecordAddedContactNotesDetail(userId, contactDate, accnCntctManActivityTypBefore, contactInfo, followUpDate, followUpIndividual);
        
        logger.info("*** Step 5: Action: Double click on the new row in 'Contact Detail' for editing that record");   
        doubleClickHiddenPageObject(notesPromisedPayments.firstUserID(), 0);
        
        logger.info("*** Step 5: Expected: The 'Edit Record' pop-up display");
        Assert.assertTrue(isElementPresent(notesPromisedPayments.editModTable(), 5), "The Edit Record popup should be displayed");
        
        logger.info("*** Step 6: Action: Select 'Manual Activity Code' that has ACCN_CNTCT_MAN_ACTIVITY_TYP.B_NO_FOLLOW_UP = 1 and tab out");       
        String accnCntctManActivityTypAfter = accnCntctManActivityAfter.getAbbrev() + " - " + accnCntctManActivityAfter.getDescr();
        selectFromDropdown(notesPromisedPayments.manualActivityCodeDropdown(), accnCntctManActivityTypAfter);
        
        logger.info("*** Step 6: Expected: Follow-up Date and Follow-up Individual are disabled and blank");    
        Assert.assertFalse(notesPromisedPayments.followUpDateStateBar().isEnabled(), "  Follow-up Date should be disabled");
        Assert.assertTrue(isElementDisabled(notesPromisedPayments.followUpIndividualDropdown(), "class", "disabled", 5), "  Follow-up Individual should be disabled");
        Assert.assertEquals(notesPromisedPayments.followUpDateStateBar().getAttribute("value"), EMPTY_STRING, "  Follow-up Date should be blank");
        Assert.assertEquals(notesPromisedPayments.followUpIndividualDropdown().getText(), EMPTY_STRING, "  Follow-up Individual should be blank");
        
        logger.info("*** Step 7: Action: Click on 'Save' button in 'Edit Record' pop-up");
		clickHiddenPageObject(notesPromisedPayments.saveButtonInAddRecordAndEditRecordPopup(), 0);
		
		logger.info("*** Step 7: Expected: - The 'Edit Record' pop-up closes"
				+ "- Accession Notes & Promised Payment screen displayed"
				+ "- The new value is updated correctly on Contact Detail grid with 'Follow up Date' and 'Follow up Individual' fields are empty");																																																																																																																																																																																																																																																	
        Assert.assertTrue(isElementPresent(notesPromisedPayments.notesPromisedPaymentsForm(), 5), "        The Notes & Promised Payments page should be displayed");
        notesPromisedPaymentsUtils.clearAllFilterInput();
        notesPromisedPaymentsUtils.searchNewRecordDislayedInContactDetail(userId, contactDate, contactInfo, EMPTY_STRING, EMPTY_STRING);
        notesPromisedPaymentsUtils.verifyNewRecordAddedContactNotesDetail(userId, contactDate, accnCntctManActivityTypAfter,contactInfo, EMPTY_STRING, EMPTY_STRING);
        
        logger.info("*** Step 8: Action: Click Save and Clear button");
        clickHiddenPageObject(notesPromisedPayments.saveAndClearButton(), 0);
        Thread.sleep(5000);
        
        logger.info("*** Step 8: Expected: - Data is saved successfully, Accession Notes & Promised Payment screen step 1 is displayed"
        								+ "- New record is added into table ACCN_CNTCT with correct data");
        Assert.assertTrue(isElementPresent(notesPromisedPayments.loadAccessionSectionAccessionIdInput(), 5), "        The Notes & Promised Payments page should be displayed");
        int accnManId = accnCntctManActivityAfter.getAccnCntctManTypId(); 
		AccnCntct accnCntct = accessionDao.getAccnCntctByAccnIdAndCntctInfo(accnId, contactInfo);
		AccnCntctManActivityTyp accnCntctManActivityTypInDb = accessionDao.getAccnCntctManActivityTypById(accnManId);
		String expAccnCntctManActivityTyp = accnCntctManActivityTypInDb.getAbbrev() + " - " + accnCntctManActivityTypInDb.getDescr();
		Assert.assertEquals(userId, accnCntct.getUserId(), "   User Id should be added into ACCN_CNTCT table as inputted");
    	Assert.assertEquals(contactDate, new SimpleDateFormat(DATE_FORMAT).format(accnCntct.getCntctDt()), "   Contact Date should be added into ACCN_CNTCT table as inputted");
    	Assert.assertEquals(expAccnCntctManActivityTyp, accnCntctManActivityTypAfter, "   Manual Activity Code should be added into ACCN_CNTCT table as inputted");
    	Assert.assertEquals(contactInfo, accnCntct.getCntctInfo(), "   Contact Info should be added into ACCN_CNTCT table as inputted");
    	Assert.assertNull(accnCntct.getFollowUpDt(), "   Follow-up Date should be null"); // Follow-up Date was a blank as selecting 'Manual Activity Code' that has ACCN_CNTCT_MAN_ACTIVITY_TYP.B_NO_FOLLOW_UP = 1
    	Assert.assertNull(accnCntct.getFollowUpUserId(), "   Follow-up Individual should be null"); // Follow-up Individual was a blank as selecting 'Manual Activity Code' that has ACCN_CNTCT_MAN_ACTIVITY_TYP.B_NO_FOLLOW_UP = 1
	}
	
	@Test(priority = 1, description = "Verify update Contact Detail value correctly when changed Manual Activity Code from the one that have b_no_follow_up = 1 to one that have b_no_follow_up = 0 on Notes and Promised payments screen")
	@Parameters({"project", "testSuite", "testCase"})
	public void updateCntctDetailWhenManActivityCdFromNoFUp1ToNoFUp0(String project, String testSuite, String testCase) throws Exception {
        logger.info("==== Testing - testC16737 ==== ");
        logger.info("*** Prepare data");
        // Setup AccnCntctManActivityTyp with b_no_follow_up = 1
        accnCntctManActivityBefore = accessionDao.getAccnCntctManActivityTypById(1);
        noFollowUpBefore = accnCntctManActivityBefore.getIsNoFollowUp();
        accnCntctManActivityBefore.setIsNoFollowUp(true);
        accnCntctManActivityBefore.setModified(true);
        accessionDao.setAccnCntctManActivityTyp(accnCntctManActivityBefore);
        // Setup AccnCntctManActivityTyp with b_no_follow_up = 0
        accnCntctManActivityAfter = accessionDao.getAccnCntctManActivityTypById(2);
        noFollowUpAfter = accnCntctManActivityAfter.getIsNoFollowUp();
        accnCntctManActivityAfter.setIsNoFollowUp(false);
        accnCntctManActivityAfter.setModified(true);
        accessionDao.setAccnCntctManActivityTyp(accnCntctManActivityAfter);        
        clearDataCache();
		navigation.navigateToAccessionNotesPromisedPayment();
        
        logger.info("*** Expected: Notes & Promised Payments page is displayed");        
        Assert.assertTrue(isElementPresent(notesPromisedPayments.loadAccessionSectionAccessionIdInput(), 5), "        The Notes & Promised Payments page should be displayed");
        
        logger.info("*** Create new accn");
        accnId = notesPromisedPaymentsUtils.createANewAccn(project,testSuite,testCase);
        
        logger.info("*** Step 1: Action: Input accn ID into search field");
        loadAccession.setAccnId(accnId);
        
        logger.info("*** Step 1: Expected: Notes & Promised Payments detail page is displayed");    
        Assert.assertTrue(isElementPresent(notesPromisedPayments.notesPromisedPaymentsForm(), 60), "   Notes & Promised Payments detail page should be displayed");
        
        logger.info("*** Step 2: Action: In 'Contact Detail' grid, click on 'Add' button");
        clickHiddenPageObject(notesPromisedPayments.addRecordButton(), 0);
        
        logger.info("*** Step 2: Expected: The 'Add Record' pop-up is displayed");    
        Assert.assertTrue(isElementPresent(notesPromisedPayments.editModTable(), 5), "The add record popup should be displayed");
        
        logger.info("*** Step 3: Action: Select 'Manual Activity Code' that has ACCN_CNTCT_MAN_ACTIVITY_TYP.B_NO_FOLLOW_UP = 1 and tab out");
        String contactDate = getInputVal(notesPromisedPayments.contactDateText());
        String userId = notesPromisedPayments.userIdText().getText();        
        String accnCntctManActivityTypBefore = accnCntctManActivityBefore.getAbbrev() + " - " + accnCntctManActivityBefore.getDescr();
        selectFromDropdown(notesPromisedPayments.manualActivityCodeDropdown(), accnCntctManActivityTypBefore);
        
        logger.info("*** Step 3: Expected: The 'Follow-up Date' and 'Follow-up Individual' are disable and cleared correctly");    
        Assert.assertFalse(notesPromisedPayments.followUpDateStateBar().isEnabled(), "  Follow-up Date should be disabled");
        Assert.assertTrue(isElementDisabled(notesPromisedPayments.followUpIndividualDropdown(), "class", "disabled", 5), "  Follow-up Individual should be disabled");
        Assert.assertEquals(notesPromisedPayments.followUpDateStateBar().getAttribute("value"), EMPTY_STRING, "  Follow-up Date should be blank");
        Assert.assertEquals(notesPromisedPayments.followUpIndividualDropdown().getText(), EMPTY_STRING, "  Follow-up Individual should be blank");
        
        logger.info("*** Step 4: Action: Input value to 'Contact Info', click 'Save' button");
        String contactInfo = randomCharacter.getRandomAlphaString(10);
        setInputValue(notesPromisedPayments.contactInfoTextArea(), contactInfo);
        clickHiddenPageObject(notesPromisedPayments.saveButtonInAddRecordAndEditRecordPopup(), 0);
        
        logger.info("*** Step 4: Expected: - The 'Add Record' pop-up closes"
        								+ "- Accession Notes & Promised Payment screen displayed "
        								+ "- New record is displayed on Contact Detail grid");  
        Assert.assertTrue(isElementPresent(notesPromisedPayments.notesPromisedPaymentsForm(), 5), "        The Notes & Promised Payments page should be displayed");
        notesPromisedPaymentsUtils.verifyNewRecordAddedContactNotesDetail(userId, contactDate, accnCntctManActivityTypBefore, contactInfo, EMPTY_STRING, EMPTY_STRING);
        
        logger.info("*** Step 5: Action: Double click on the new row in 'Contact Detail' for editing that record");
        notesPromisedPaymentsUtils.searchNewRecordDislayedInContactDetail(userId, contactDate, contactInfo, EMPTY_STRING, EMPTY_STRING);
        doubleClickHiddenPageObject(notesPromisedPayments.firstUserID(), 0);
        
        logger.info("*** Step 5: Expected: The 'Edit Record' pop-up display");
        Assert.assertTrue(isElementPresent(notesPromisedPayments.editModTable(), 5), "The Edit Record popup should be displayed");
        
        logger.info("*** Step 6: Action: Click on Manual Activity Code dropdown, select record that has ACCN_CNTCT_MAN_ACTIVITY_TYP.B_NO_FOLLOW_UP = 0");       
        String accnCntctManActivityTypAfter = accnCntctManActivityAfter.getAbbrev() + " - " + accnCntctManActivityAfter.getDescr();
        selectDropDown(notesPromisedPayments.manualActivityCodeDropdown(), accnCntctManActivityTypAfter);
        
        logger.info("*** Step 6: Expected: Follow-up Date' and 'Follow-up Individual' are enabled and blank");
        Assert.assertTrue(notesPromisedPayments.followUpDateStateBar().isEnabled(), "  Follow-up Date should be enabled");
        Assert.assertTrue(notesPromisedPayments.followUpIndividualDropdown().isEnabled(), "  Follow-up Individual should be enabled");
        Assert.assertEquals(notesPromisedPayments.followUpDateStateBar().getAttribute("value"), EMPTY_STRING, "  Follow-up Date should be blank");
        Assert.assertEquals(notesPromisedPayments.followUpIndividualDropdown().getText(), EMPTY_STRING, "  Follow-up Individual should be blank");
        
        logger.info("*** Step 7: Action: - Enter valid value to 'Follow-up Date' and 'Follow-up Individual' fields"
        							  + "- Click on 'Save' button in 'Edit Record' pop-up");
        String followUpDate = timeStamp.getFutureDate(DATE_FORMAT, Integer.parseInt(accnCntctManActivityAfter.getDefaultDays() + randomCharacter.getRandomNumericString(2)));
		setInputValue(notesPromisedPayments.followUpDateStateBar(), followUpDate);
		
		String followUpIndividual = clientDao.getClnCntctFollowUpUserIdNotNull().getFollowUpUserId();
		selectFromDropdown(notesPromisedPayments.followUpIndividualDropdown(), followUpIndividual);
		clickHiddenPageObject(notesPromisedPayments.saveButtonInAddRecordAndEditRecordPopup(), 0);
		
        logger.info("*** Step 7: Expected: - The 'Edit Record' pop-up closes"
        								+ "- Accession Notes & Promised Payment screen displayed"
        								+ "- The new value is updated correctly on Contact Detail grid with correct value of 'Follow up Date' and 'Follow up Individual' fields");																																																																																																																																																																																																																																																	
        Assert.assertTrue(isElementPresent(notesPromisedPayments.notesPromisedPaymentsForm(), 5), "        The Notes & Promised Payments page should be displayed");
        notesPromisedPaymentsUtils.searchNewRecordDislayedInContactDetail(userId, contactDate, contactInfo, followUpDate, followUpIndividual);
        notesPromisedPaymentsUtils.verifyNewRecordAddedContactNotesDetail(userId, contactDate, accnCntctManActivityTypAfter, contactInfo, followUpDate, followUpIndividual);
        
        logger.info("*** Step 8: Action: Click Save and Clear button");
        clickHiddenPageObject(notesPromisedPayments.saveAndClearButton(), 0);
        
        logger.info("*** Step 8: Expected: - Data is saved successfully, Accession Notes & Promised Payment screen step 1 is displayed"
        								+ "- New record is added into table ACCN_CNTCT with correct data");
        Assert.assertTrue(isElementPresent(notesPromisedPayments.loadAccessionSectionAccessionIdInput(), 5), "        The Notes & Promised Payments page should be displayed");
        int accnManId = accnCntctManActivityAfter.getAccnCntctManTypId(); 
		AccnCntct accnCntct = accessionDao.getAccnCntctByAccnIdAndCntctInfo(accnId, contactInfo);
		AccnCntctManActivityTyp accnCntctManActivityTypInDb = accessionDao.getAccnCntctManActivityTypById(accnManId);
		String expAccnCntctManActivityTyp = accnCntctManActivityTypInDb.getAbbrev() + " - " + accnCntctManActivityTypInDb.getDescr();
		Assert.assertEquals(userId, accnCntct.getUserId(), "   User Id should be added into ACCN_CNTCT table as inputted");
    	Assert.assertEquals(contactDate, new SimpleDateFormat(DATE_FORMAT).format(accnCntct.getCntctDt()), "   Contact Date should be added into ACCN_CNTCT table as inputted");
    	Assert.assertEquals(expAccnCntctManActivityTyp, accnCntctManActivityTypAfter, "   Manual Activity Code should be added into ACCN_CNTCT table as inputted");
    	Assert.assertEquals(contactInfo, accnCntct.getCntctInfo(), "   Contact Info should be added into ACCN_CNTCT table as inputted");
    	Assert.assertEquals(followUpDate,new SimpleDateFormat(DATE_FORMAT).format(accnCntct.getFollowUpDt()), "   Follow-up Date should be added into ACCN_CNTCT table as inputted");
    	Assert.assertEquals(followUpIndividual, accnCntct.getFollowUpUserId(), "   Follow-up Individual should be added into ACCN_CNTCT table as inputted");
	}
}
