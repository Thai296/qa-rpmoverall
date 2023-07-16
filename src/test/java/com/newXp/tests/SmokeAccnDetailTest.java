package com.newXp.tests;

import com.mbasys.common.utility.Money;
import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.accnProcErr.AccnProcErr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.ejb.entity.adjCd.AdjCd;
import com.mbasys.mars.ejb.entity.elcPmtDetail.ElcPmtDetail;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.phys.Phys;
import com.mbasys.mars.ejb.entity.physCredTyp.PhysCredTyp;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.zip.Zip;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.DatabaseMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionProcessing.*;
import com.overall.menu.MenuNavigation;
import com.overall.utils.AccnDetailUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.rpm.ErrorDao;
import com.xifin.qa.dao.rpm.domain.AccnAgncy;
import com.xifin.qa.dao.rpm.domain.QEp;
import com.xifin.qa.dao.rpm.domain.QEpErr;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

public class SmokeAccnDetailTest extends SeleniumBaseTest {

	private XifinAdminUtils xifinAdminUtils;
	private AccessionDetail accessionDetail;
	private LoadAccession loadAccession;
	private AccessionSearch accessionSearch;
	private AccessionSearchResults accessionSearchResults;
	private RecordPopUp recordPopUp;
	private RandomCharacter randomCharacter;
	private AccnDetailUtils accessionUtils;
	private TimeStamp timeStamp;
	private PhysicianSearch physicianSearch;
	private PhysicianSearchResults physicianSearchResults;
	private PatientSearch patientSearch;
	private PatientSearchResults patientSearchResults;
	private ZipCodeSearch zipCodeSearch;
	private ZipCodeSearchResults zipCodeSearchResults;
	private MenuNavigation navigation;
	private TestDataSetup testDataSetup;

	@BeforeMethod(alwaysRun = true)
	@Parameters({"email", "password"})
	public void beforeMethod(String email, String password,Method method)
	{
		try
		{
			logger.info("Running BeforeMethod");
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			logIntoSso(email, password);
			navigation = new MenuNavigation(driver, config);
			navigation.navigateToAccnDetailPage();
		}
		catch (SkipException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SkipException("Error running BeforeMethod", e);
		}
	}

	@Test(priority = 1, description = "Log into RPM and load an existing accn via Accession Search")
	public void testXPR_2() throws Exception {
		logger.info("===== Testing - testXPR_2 =====");
		accessionDetail = new AccessionDetail(driver, config, wait);
		loadAccession=new LoadAccession(driver);
		accessionSearch = new AccessionSearch(driver);
		accessionSearchResults = new AccessionSearchResults(driver);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Actions: - Click Accession Search button");
		assertTrue(isElementPresent(loadAccession.accnSearchBtn(), 5), "       accession search button must be displayed");
		loadAccession.clickAccnSearch();
		String parentWindow=switchToPopupWin();//Go to Accn Search popup window

		logger.info("*** Step 2 Expected Results: - Verify that its in the Accession Search window");
		assertTrue(isElementPresent(accessionSearch.accnSearchTitle(), 5),"        The Accession Search page title should be displayed");
		assertTrue(accessionSearch.accnSearchTitle().getText().trim().contains("Accession Search"));

		logger.info("*** Step 3 Actions: - Enter the Accn ID in Accession ID field in Accn Search window and click Search button");
		String accnId = accessionDao.getPricedAccession().getAccnId(); //Get accnId from DB
		accessionDetail.loadAccnOnAccnSearch(accnId);
		switchToPopupWin();//Go to Accn Search Results popup window

		logger.info("*** Step 3 Expected Results: - Verify that it's in the Accn Search Results window");
		assertTrue(isElementPresent(accessionSearchResults.accessionSearchResultsTitle(wait), 5),"        The Accession Search page title should be displayed");
		assertTrue(accessionSearchResults.accessionSearchResultsTitle(wait).getText().trim().contains("Accession Search Results"));

		logger.info("*** Step 4 Actions: - Click the Accn ID hyperlink in the Accn Search Results window");
		Assert.assertTrue(isElementPresent(accessionSearchResults.accnSearchResultTable(),5), "        Accession Search Results should be present.");
		Assert.assertTrue(selectColumnValue(accnId, accessionSearchResults.accnSearchResultTable()), "        Accn ID should be found in the Accn Search Results table.");
		switchToParentWin(parentWindow);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 20), "        Save button should be clickable.");

		logger.info("*** Step 4 Expected Results: - Verify that the accn is loaded properly in Detail page");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 5 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 5 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "AccessionErrorGrid-Fix and Unfix Denials error")
	public void testXPR_12() throws Exception {
		logger.info("===== Testing - testXPR_12 =====");
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		timeStamp = new TimeStamp();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Actions: - Load an existing accession with unfixed Denial errors in the Accession ID field and tab out");
		AccnProcErr accnProcErr = errorProcessingDao.getOneFixableDenialErrFromAccnProcErr(3);
		String accnId = accnProcErr.getAccnId();
		loadAccession.setAccnId(accnId);	//Load accnId
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");	//Wait until the page is loaded completely when Save button became clickable again

		logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly");

		logger.info("*** Step 3 Actions - Go to Accession Errors grid and click 'Show all unfixed errors' radio button");
		accessionDetail.setShowAllUnfixedErr();		//Click the Show All Unfixed Error radio button
		int errCdId = accnProcErr.getErrCd();	//Get the reason code abbrev and short description
		String reasonCDDescr = errorProcessingDao.getErrCodeAbbrevAndShortDescrAsReasoncdFromErrCdByErrCdId(errCdId);
		String procCdId =accnProcErr.getProcId();	//Get the row number for the reason code and the proc code id
		int rowNum = accessionDetail.getRowNumber(accessionDetail.currentAccnErrTable(), reasonCDDescr, procCdId);

		logger.info("*** Step 3 Expected Results: - Verify that the Denial error is in the Accession Errors table");
		Assert.assertNotEquals(rowNum, 0, reasonCDDescr + " and " + procCdId + " can't be found in Current Accession Error table");
		logger.info("        Found " + reasonCDDescr + " and " + procCdId + " at Row " + rowNum);

		logger.info("*** Step 4 Actions: - Find the Denial error which has Fix Error checkbox is enabled and click it");
		accessionDetail.setCurrAccnErrAction(rowNum);	//Click the Fix hyperlink in Action column for the selected row
		accessionDetail.setAccnErrGrid();	//Click Accession Error grid to expend the grid

		logger.info("*** Step 5 Actions: - Click Save button");
		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");
		int errSeqId = accnProcErr.getErrSeq();	//Get the error seq id

		logger.info("*** Step 5 Expected Results: - Verify in accn_proc_err table for the selected accn id and the error seq id and check if the fix_dt being added");
		Assert.assertEquals(df.format(errorProcessingDao.getAccnProcErrsByAccnIdAndErrSeq(accnId, errSeqId).getFixDt()), timeStamp.getCurrentDate());

		logger.info("*** Step 6 Actions: - Click 'Show all errors' radio button");
		accessionDetail.showAllErrorsRadioBtn().click();	//Click the Show All Unfixed Error radio button

		logger.info("*** Step 7 Actions: - Click the Unfix Error checkbox for the fixed error in the previous step");
		rowNum = accessionDetail.getRowNumber(accessionDetail.currentAccnErrTable(), reasonCDDescr, procCdId);	//Click the Unfix hyperlink
		System.out.println("rowNum=" + rowNum);
		accessionDetail.setFixedAccnErrAction(rowNum);

		logger.info("*** Step 8 Actions: - Click Save button");
		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save was not done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 8 Expected Results: - Verify in accn_proc_err table for the selected accn id and the error seq id and check if the fix_dt being cleared");	//Ensure the error got Unfixed in DB
		Assert.assertNull(errorProcessingDao.getAccnProcErrsByAccnIdAndErrSeq(accnId, errSeqId).getFixDt(), "        " + reasonCDDescr + " was not fixed.");

		logger.info("*** Step 9 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 9 Expected Results: - Verify that it's back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Add new Contact Notes")
	public void testXPR_14() throws Exception {
		logger.info("===== Testing - testXPR_14 =====");
		accessionDetail = new AccessionDetail(driver, config, wait);
		loadAccession =new LoadAccession(driver);
		randomCharacter = new RandomCharacter();	//Generate a random string
		timeStamp = new TimeStamp();
		recordPopUp = new RecordPopUp(driver,wait);	//Pass the driver to Add Record popup window
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
		String accnId = accessionDao.getPricedAccession().getAccnId();	//Get accnId from DB
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");	//Wait until the page is loaded completely when Save button became clickable again

		logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 3 Actions: - Click the Contact Notes grid to extend the grid");
		accessionDetail.setContactNotesGrid();

		logger.info("*** Step 3 Expected Results: - Verify that the Contact Notes grid is in Detail View");
		Assert.assertEquals(accessionDetail.contactNotesCurrentView().getAttribute("title"), "DETAIL view");

		logger.info("*** Step 4 Actions: - Click Add button and switch to the Add Record pops up window");
		accessionDetail.clickContactNotesAdd();

		logger.info("*** Step 4 Expected Results: - Verify that the Contact Info Text Area displays in Add Record window");
		Assert.assertTrue(recordPopUp.contactInfoTextArea().isDisplayed(), "        Contact Info Text Area should present.");

		logger.info("*** Step 5 Actions: - Enter Contact Info and click OK button");
		String contactInfoStr = randomCharacter.getRandomAlphaNumericString(8);
		recordPopUp.setContactInfo(contactInfoStr);
		recordPopUp.clickOK();

		logger.info("*** Step 5 Expected Results: - Verify that the newly created contact notes was added to the Contact Detail table");	//Back to Detail page
		Assert.assertTrue(accessionDetail.isColumnValueExist(accessionDetail.contactDetailTable(), contactInfoStr), "        " + contactInfoStr + " should show in Contact Detail table.");

		logger.info("*** Step 6 Actions: - Click Save button");
		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 6 Expected Results: - Verify that the Contact Notes was saved in DB");
		Assert.assertEquals(df.format(accessionDao.getAccnCntctByAccnIdAndCntctInfo(accnId, contactInfoStr).getCntctDt()), timeStamp.getCurrentDate());

		logger.info("*** Step 6 Expected Results: - Verify that the Contact Notes was displayed in Contact Notes Detail view");
		Assert.assertEquals(accessionDetail.contactNotesCurrentView().getAttribute("title"), "DETAIL view");
		Assert.assertTrue(accessionDetail.isColumnValueExist(accessionDetail.contactDetailTable(), contactInfoStr), "        " + contactInfoStr + " should show in Contact Detail table.");

		logger.info("*** Step 7 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable");
		accessionDetail.clickReset();

		logger.info("*** Step 7 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Physician Search")
	public void testXPR_21() throws Exception {
		logger.info("===== Testing - testXPR_21 =====");
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		physicianSearch = new PhysicianSearch(driver);
		physicianSearchResults = new PhysicianSearchResults(driver);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
		String accnId = accessionDao.getPricedAccession().getAccnId();//Get accnId from DB
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "Save button should be clickable.");	//Wait until the page is loaded completely when Save button became clickable again

		logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 3 Action: - Click the Physician Info grid to extend the grid");
		accessionDetail.setPhysicianInfoGrid();

		logger.info("*** Step 3 Expected Results: - Verify that the Physician Info grid is in Detail View");
		Assert.assertEquals(accessionDetail.physicianInfoCurrentView().getAttribute("title"), "DETAIL view");

		logger.info("*** Step 4 Actions: - Click the Physician Search icon button for Ordering Physician and go to Physician Search screen");
		accessionDetail.clickPhysicianSearch();
		String parentWin=switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Verify that it's on Physician Search screen");
		Assert.assertTrue(physicianSearch.pageTitleText().trim().contains("Physician Search"), "        Physician Search Page Title should show.");

		logger.info("*** Step 5 Actions: - In DB, perform a search and find Physician's info");
		String clnId = "100";
		Phys phys = physicianDao.getPhysByClnAbbrev(clnId);
		PhysCredTyp physCredTyp=physicianDao.getPhysicianCredTypByPhysCredTypId(phys.getPhysCredId());
		String physFName = phys.getPhysFname();
		String physLName = phys.getPhysLname();
		String credential = physCredTyp.getDescr();
		String npi = phys.getNpiId().toString();
		String upin = phys.getUpinId();
		String taxonomyCode = phys.getTaxonomyCd();

		logger.info("*** Step 6 Actions: - Enter Physician Info and click Search button");
		physicianSearch.setFirstName(physFName);
		physicianSearch.setLastName(physLName);
		physicianSearch.setCredentials(credential);
		physicianSearch.setNPI(npi);
		physicianSearch.setUPIN(upin);
		physicianSearch.setCLNID(clnId);
		physicianSearch.clickSearch();

		logger.info("*** Step 7 Actions: - Go to Physician Search Result screen");
		switchToPopupWin();

		logger.info("*** Step 7 Expected Results: - Verify that it's on Physician Search Result screen");
		Assert.assertTrue(physicianSearchResults.physicianSearchResultTable().isDisplayed(), "        Physician Search Result table should present.");

		logger.info("*** Step 8 Actions: - Click NPI hyperlink in the Physician Search Result table");
		physicianSearchResults.physicianSearchResultNPI(1,2).click();
		switchToWin(parentWin);	//Switch to parent window which is the Accession Detail screen in this case
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 20), "        Save button should be clickable.");	//Wait until the page is loaded completely when Save button became clickable again

		logger.info("*** Step 8 Expected Results: - Verify that the selected Physician info are displaying properly in UI");
		Assert.assertEquals(accessionDetail.npiInput().getAttribute("value"), npi, "        Physician NPI " + accessionDetail.npiInput().getText() + " should match " + npi + " in DB.");
		Assert.assertEquals(accessionDetail.upinInput().getAttribute("value"), upin, "        Physician UPIN " + accessionDetail.upinInput().getText() + " should match " + upin + " in DB.");
		String physFullName = physLName + ", " + physFName;
		Assert.assertEquals(accessionDetail.physNameText().getText(), physFullName, "        Physician Name " + accessionDetail.physNameText().getText() + " should match " + physFullName + " in DB.");
		Assert.assertEquals(accessionDetail.taxonomyCodeText().getText(), taxonomyCode, "        Physician Taxonomy Code " + accessionDetail.taxonomyCodeText().getText() + " should match " + taxonomyCode + " in DB.");

		logger.info("*** Step 9 Actions: - Click Save button");
		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 9 Expected Results: - Verify that the Physician info got saved in DB");
		int physSeqId = phys.getSeqId();
		Assert.assertEquals(accessionDao.getAccnPhysByAccnId(accnId).get(0).getPhysSeqId(), physSeqId);
		accessionDetail.setPhysicianInfoGrid();
		accessionDetail.setPhysicianInfoGrid();

		logger.info("*** Step 9 Expected Results: - Verify that the Physician info are displaying properly in Physician Info Summary view");
		Assert.assertEquals(accessionDetail.physicianInfoCurrentView().getAttribute("title"), "SUMMARY view");
		Assert.assertEquals(accessionDetail.physNameSummaryViewText().getText(), physFullName);
		Assert.assertEquals(accessionDetail.npiSummaryViewText().getText(), npi);
		Assert.assertEquals(accessionDetail.upinSummaryViewText().getText(), upin);
		Assert.assertEquals(accessionDetail.taxonomyCodeSummaryViewText().getText(), taxonomyCode);

		logger.info("*** Step 10 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable");
		accessionDetail.clickReset();

		logger.info("*** Step 10 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field was not found");
	}

	@Test(priority = 1, description = "Patient Search")
	public void testXPR_7() throws Exception {
		logger.info("===== Testing - testXPR_7 =====");
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		recordPopUp = new RecordPopUp(driver,wait);
		patientSearch = new PatientSearch(driver);
		patientSearchResults = new PatientSearchResults(driver);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
		String accnId = accessionDao.getPricedAccession().getAccnId();	//Get accnId from DB
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");	//Wait until the page is loaded completely when Save button became clickable again

		logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 3 Action: - Click the Create new EPI hyperlink and go to Create A New Patient EPI popup window");
		accessionDetail.clickCreateNewEPILink();
		String parentWindow=switchToPopupWin();	//Switch to Create A New Patient EPI popup window

		logger.info("*** Step 3 Expected Results: - Verify that it's on the Create A New Patient EPI popup window");
		Assert.assertTrue(recordPopUp.createNewPatientEPIDlg().isDisplayed(), "        Create A New Patient EPI popup window should present.");

		logger.info("*** Step 4 Action: - By default, select system generate an EPI option and click OK button");
		recordPopUp.clickOKInCreateNewPatientEPIDlg();

		logger.info("*** Step 4 Expected Results: - Verify that an EPI was generated in EPI entry field");
		String newEPI = accessionDetail.epiInput().getAttribute("value");
		Assert.assertNotNull(newEPI, "        No new EPI was generated.");

		logger.info("*** Step 5 Action: - Copy the newly generated EPI and enter the same ID into Client Patient ID and Client's Primary Facility Patient ID fields and tab out");
		String newClnPtId = newEPI + "CP";
		String newClnFacPtId = newEPI + "CFP";
		accessionDetail.setClientPatientId(newClnPtId);
		accessionDetail.setClientPrimaryFacilityPatientId(newClnFacPtId);

		logger.info("*** Step 6 Actions: - Click Save button");
		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save was not done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 6 Expected Results: - Verify that the values were displayed properly in UI");
		Assert.assertEquals(accessionDetail.epiInput().getAttribute("value"), newEPI);
		Assert.assertEquals(accessionDetail.clientPatientIdInput().getAttribute("value"), newClnPtId);
		Assert.assertEquals(accessionDetail.clientPrimaryFacilityPatientIdInput().getAttribute("value"), newClnFacPtId);

		logger.info("*** Step 6 Expected Results: - Verify that the values were saved properly in DB");
		Assert.assertEquals(patientDao.getPtV2ByAccnId(accnId).getEpi(), newEPI);	//Ensure that the new EPI was saved to PT view
		String clnAbbrev = accessionDetail.clientIdText().getAttribute("value");
		Assert.assertEquals(patientDao.getPtClnLnkByEPIAndClnAbbrev(newEPI, clnAbbrev).getSpecificPtId(), newClnPtId);	//Ensure that the new EPI was saved to PT_CLN_LNK table
		Assert.assertEquals(patientDao.getPtFacLnkByEPIAndClnAbbrev(newEPI, clnAbbrev).getSpecificPtId(), newClnFacPtId);	//Ensure that the new EPI was saved to PT_FAC_LNK table
		String ptDOB = accessionDetail.dobText().getText();
		String delims = "[,]";
		String[] tokens = accessionDetail.ptFullNameText().getText().split(delims);
		String ptFName = tokens[1];
		String ptLName = tokens[0];

		logger.info("*** Step 7 Actions: - Click Patient Search button and go to Patient Search window");
		accessionDetail.clickPatientSearch();
		switchToPopupWin();	//Switch to Patient Search window

		logger.info("*** Step 7 Expected Results - Verify that it's in the Patient Search window");
		Assert.assertTrue(patientSearch.patientIdInput().isDisplayed(), "        Patient ID field should present.");

		logger.info("*** Step 8 Actions - Enter values in Patient ID, FName, LName, DOB fields and check Show All Associated Client And Facility Patient IDs checkbox and click Search button and go to Patient Search Results window");
		patientSearch.setPatientId(newEPI);
		patientSearch.setFirstName(ptFName);
		patientSearch.setLastName(ptLName);
		patientSearch.setDOB(ptDOB);
		selectCheckBox(patientSearch.showAllClnFacPtIdCheckbox());
		patientSearch.clickSearch();
		switchToPopupWin();	//Switch to Patient Search Results window

		logger.info("*** Step 8 Expected Results - Verify that it's in the Patient Search Results window");
		Assert.assertTrue(patientSearchResults.patientSearchResultTable().isDisplayed(), "        Patient Search Results table should present.");

		logger.info("*** Step 8 Expected Results - Verify that the EPI, clnPtId and clnFacPtId are displaying in the Patient Search Results window");
		Assert.assertTrue(getColumnValue(patientSearchResults.patientSearchResultTable(), newEPI), "        EPI " + newEPI + " should be found in Patient Search Results window.");
		Assert.assertTrue(getColumnValue(patientSearchResults.patientSearchResultTable(), newClnPtId), "        Client Patient ID " + newClnPtId + " should be found in Patient Search Results window.");
		Assert.assertTrue(getColumnValue(patientSearchResults.patientSearchResultTable(), newClnFacPtId), "        Client's Primary Facility Patient ID " + newClnFacPtId + " should be found in Patient Search Results window.");

		logger.info("*** Step 9 Actions: - Click New Search button");
		patientSearchResults.clickNewSearch();

		logger.info("*** Step 9 Expected Results: - Verify that it's back to the Patient Search window and the fields were cleared.");
		Assert.assertEquals(patientSearch.patientIdInput().getAttribute("value"), "", "        Patient ID field should be cleared.");
		Assert.assertEquals(patientSearch.firstNameInput().getAttribute("value"), "", "        First Name field should be cleared.");
		Assert.assertEquals(patientSearch.lastNameInput().getAttribute("value"), "", "        Last Name field should be cleared.");
		Assert.assertEquals(patientSearch.dobInput().getAttribute("value"), "", "        Date of Birth field should be cleared.");

		logger.info("*** Step 10 Actions: - Click Close button in Patient Search window");
		patientSearch.clickClose();
		switchToWin(parentWindow);	//Switch to parent window which is the Accession Detail screen in this case

		logger.info("*** Step 10 Expected Results: - Verify that it's back to the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 20), "        Save button should be clickable.");	//Wait until the page is loaded completely when Save button became clickable again

		logger.info("*** Step 11 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 11 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Add new and void Diagnoses Code")
	public void testXPR_16() throws Exception {
		logger.info("===== Testing - testXPR_16 =====");
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		recordPopUp = new RecordPopUp(driver,wait);	//Pass the driver to Add Record popup window
		randomCharacter = new RandomCharacter();	//Generate a random string
		timeStamp = new TimeStamp();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
		String accnId = accessionDao.getPricedAccession().getAccnId();//Get accnId from DB
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

		logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 3 Actions: - Click Current Diagnoses grid");
		accessionDetail.setCurrentDiagnosesGrid();

		logger.info("*** Step 3 Expected Results: - Verify that the Current Diagnoses gird is in Detail view");
		Assert.assertEquals(accessionDetail.currentDiagnosesCurrentView().getAttribute("title"), "DETAIL view");

		logger.info("*** Step 4 Actions: - Click Add button and switch to Add Record popup window");
		clickHiddenPageObject(accessionDetail.diagnosisDetailAddBtn(), 0);	//In this case, the Diagnosis Detail grid is not in the view
		switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Verify that it's in Add Record popup window");
		Assert.assertTrue(recordPopUp.dxCodeInput().isDisplayed(), "        Dx Code input field should present.");

		logger.info("*** Step 5 Actions: - Enter values in Dx Code and Client Contact fields and tab out and click OK button");
		String dxCode = diagnosisCodeDao.getRandomDiagCd().getDiagCdId();
		String clientContact = randomCharacter.getRandomAlphaNumericString(8);
		recordPopUp.setDxCode(dxCode);
		recordPopUp.setClientContact(clientContact);
		recordPopUp.clickOK();

		logger.info("*** Step 5 Expected Results: - Verify that the Dx Code and Client Contact are showing properly in UI");
		Assert.assertEquals(accessionDetail.unSavedDiagnosisDetailText(1, 6).getText().trim(), dxCode, "        Diagnosis Code should be found in Diagnosis Detail Table.");
		Assert.assertEquals(accessionDetail.unSavedDiagnosisDetailText(1, 11).getText().trim(), clientContact, "        Client Contact should be found in Diagnosis Detail Table.");

		logger.info("*** Step 6 Actions: - Click Save button");
		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 6 Expected Results: - Verify that the Dx Code is saved in DB");
		int diagTypId = accessionDao.getAccnDiagByAccnIdDiagIdAndClnCntct(accnId, dxCode, clientContact).getDiagTypId();
		Assert.assertNotNull(diagTypId,"        Dx Code" + dxCode + " should be saved to DB");
		accessionDetail.setCurrentDiagnosesGrid();
		accessionDetail.setCurrentDiagnosesGrid();

		logger.info("*** Step 6 Expected Results: - Verify that the Dx Code is showing properly in Current Diagnoses grid Summary view");
		Assert.assertEquals(accessionDetail.currentDiagnosesCurrentView().getAttribute("title"), "SUMMARY view");	//Ensure that the Current Diagnoses grid is in Summary View
		String diagCdShortDescr = diagnosisCodeDao.getdiagCdByDiagCdIdAndDiagTypId(dxCode, diagTypId).getShortDescr();
		Assert.assertEquals(accessionDetail.diagnosisCodeInSummaryView(diagCdShortDescr), dxCode);

		logger.info("*** Step 7 Actions: - Click Current Diagnoses grid");
		accessionDetail.setCurrentDiagnosesGrid();

		logger.info("*** Step 7 Expected Results: - Verify that the Current Diagnoses gird is in Detail view");
		Assert.assertEquals(accessionDetail.currentDiagnosesCurrentView().getAttribute("title"), "DETAIL view");

		logger.info("*** Step 8 Actions: - Check the Delete checkbox for the newly added Dx Code and Save");
		int rowNum = accessionDetail.getRowNumberInWebTable(accessionDetail.accnDiagnosisDetailTable(), "tbl_accnDiagnosisDetail", dxCode, clientContact, 6, 11);	//dxCode (col=6) and clientContact (col=11) in accnDiagnosisDetailTable
		accessionDetail.checkDeleteDiagnosisCheckbox(rowNum-2);
		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save was not done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 8 Expected Results: - Verify that the deleted Dx Code was deleted from DB");
		Assert.assertEquals(df.format(accessionDao.getAccnDiagDelByAccnIdDiagIdAndClnCntct(accnId, dxCode, clientContact).getDelDt()), timeStamp.getCurrentDate());

		logger.info("*** Step 9 Expected Results: - Verify that the Current Diagnoses gird is in Detail view");
		Assert.assertEquals(accessionDetail.currentDiagnosesCurrentView().getAttribute("title"), "DETAIL view");

		logger.info("*** Step 10 Actions: - Verify that the deleted Dx Code is displaying in the Voided Diagnosis Detail grid");
		accessionDetail.getRowNumberInWebTable(accessionDetail.accnDiagnosisDetailVoiedTable(), "tbl_accnDiagnosisDetailVoided", dxCode, clientContact, 4, 8);

		logger.info("*** Step 11 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 11 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Change Accession Primary Payor")
	public void testXPR_3() throws Exception {
		logger.info("===== Testing - testXPR_3 =====");
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
		String accnId = accessionDao.getAccnWithOnePyr().getAccnId();	//Get a Priced accnId with one payor from DB
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 10), "        Save button should be clickable.");

		logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 3 Actions: - Type in a 3rd party Payor in the Insurance Info Payor ID field and tab out");
		String newPrimaryPyrAbbrev = payorDao.getThirdPartyPyrFromPYR().getPyrAbbrv();
		accessionDetail.clearPrimaryPyrId();
		accessionDetail.setPrimaryPyrId(newPrimaryPyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Save button");
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");
		accessionDetail.clickSave(wait);
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 4 Expected Results: - Verify that the new payor got saved in DB");
		Assert.assertEquals(payorDao.getPyrByAccnIdAndPyrPrio(accnId, 1).getPyrAbbrv(), newPrimaryPyrAbbrev);

		logger.info("*** Step 4 Expected Results: - Verify that the new payor shows properly in UI");
		Assert.assertEquals(accessionDetail.primaryPayorIDInput().getAttribute("value"), newPrimaryPyrAbbrev);

		logger.info("*** Step 4 Expected Results: - Verify that the accn is final reported.");
		Assert.assertTrue(accessionDetail.accessionStatusText().getText().contains("FnlRptd"), "        The accession status should be changed from " + accessionDetail.accessionStatusText().getText() + " to FnlRptd.");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 11);

		logger.info("*** Step 5 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 5 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Create new system generated EPI on Accn and Update Payor subscriber id, verify PT_PYR record after update")
	public void testXPR_19() throws Exception {
		logger.info("===== Testing - testXPR_19 =====");
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		recordPopUp = new RecordPopUp(driver,wait);
		randomCharacter = new RandomCharacter();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Actions: - Delete PT_PYR records for the selected accn and primary payor in DB");
		String accnId = accessionDao.getAccnWithOneThirdPartyPyr().getAccnId();	//Get a Priced accnId with one payor and EPI from DB
		String primaryPyrAbbrev = payorDao.getPyrByAccnIdAndPyrPrio(accnId, 1).getPyrAbbrv();
		patientDao.deletePtPyrByAccnIdAndPyrAbbrev(accnId, primaryPyrAbbrev);

		logger.info("*** Step 3 Actions: - Clear System Cache");
		xifinAdminUtils.clearDataCache();

		logger.info("*** Step 4 Actions: - Navigate to Accession tab Detail page");
		navigation.navigateToAccnDetailPage();

		logger.info("*** Step 4 Expected Results: - Verify that it's on the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");

		logger.info("*** Step 5 Action: - Load an existing accession in the Accession ID field and tab out");
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable");

		logger.info("*** Step 5 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 6 Action: - Click the Create new EPI hyperlink and go to Create A New Patient EPI popup window");
		accessionDetail.clickCreateNewEPILink();
		switchToPopupWin();	//Switch to Create A New Patient EPI popup window

		logger.info("*** Step 6 Expected Results: - Verify that it's on the Create A New Patient EPI popup window");
		Assert.assertTrue(recordPopUp.createNewPatientEPIDlg().isDisplayed(), "        Create A New Patient EPI popup window should present.");

		logger.info("*** Step 7 Action: - By default, select system generate an EPI option and click OK button");
		recordPopUp.clickOKInCreateNewPatientEPIDlg();

		logger.info("*** Step 7 Expected Results: - Verify that an EPI was generated in EPI entry field");
		String newEPI = accessionDetail.epiInput().getAttribute("value");
		Assert.assertNotNull(newEPI, "        No new EPI was generated.");

		logger.info("*** Step 8 Actions: - Click Save button");
		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save was not done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 8 Expected Results: - Verify that the values were displayed properly in UI");
		Assert.assertEquals(accessionDetail.epiInput().getAttribute("value"), newEPI);

		logger.info("*** Step 9 Action: - Update the payor's subscriber id and tab out and click Save");
		accessionDetail.clearPrimaryPyrSubsId();
		String subsId = randomCharacter.getRandomAlphaNumericString(9);
		accessionDetail.setPrimaryPyrSubsId(subsId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");	//Click Save button
		accessionDetail.clickSave(wait);	//		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 9 Expected Results: - Verify that the payor's subs id was updated in accn_pyr and pt_pyr tables");
		String dos = df.format(accessionDao.getAccn(accnId).getDos());
		Assert.assertEquals(accessionDao.getAccnPyrByAccnIdAndPyrPrio(accnId, 1).getSubsId(), subsId);
		Assert.assertEquals(patientDao.getPtPyrByAccnIdPyrPrioDOS(accnId, 1, dos).getSubsId(), subsId);

		logger.info("*** Step 9 Expected Results: - Verify that the updated payor's subs id was showing properly in UI.");
		Assert.assertEquals(accessionDetail.primaryPyrSubsIdInput().getAttribute("value"), subsId);

		logger.info("*** Step 10 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 10 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Ordered Test grid-Backout reprice by adding Renal flag")
	@Parameters({"project", "testSuite", "testCase"})
	public void testXPR_31(String project, String testSuite, String testCase) throws Exception {
		logger.info("===== Testing - testXPR_31 =====");
		accessionUtils = new AccnDetailUtils(driver, config);
		testDataSetup = new TestDataSetup(driver);
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		recordPopUp = new RecordPopUp(driver,wait);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Actions: - Create a new Priced accn with P payor");
        Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");

		logger.info("*** Step 2 Expect Results: - A new Priced accn was created.");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

		logger.info("*** Step 3 Action: - Load new Priced accession in the Accession ID field and tab out");
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");	//Wait until the page is loaded completely when Save button became clickable again

		logger.info("*** Step 3 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 4 Actions: - Select All Columns view in Ordered Test grid");
		accessionDetail.setOrderedTestView("All Columns");

		logger.info("*** Step 4 Expected Results: - Verify that Renal column shows");
		Assert.assertFalse(accessionDetail.renalColText().contains("none"), "        Renal Column should present.");	//style does not contain "display: none;" which means it's hiding

		logger.info("*** Step 5 Actions - Select a test in the Ordered Test grid and click Edit button");
		AccnTest accnTest = accessionDao.getAccnTestFromTestByAccnId(accnId);
		String orderedTestAbbrev = testDao.getTestByTestId(accnTest.getTestId()).getTestAbbrev();
		int accnTestSeqId = accnTest.getAccnTestSeqId();
		selectHiddenColumnValue(accessionDetail.orderedTestDetailTable(), orderedTestAbbrev);	//Click the selected Test Code row
		clickHiddenPageObject(accessionDetail.orderedTestDetailEditBtn(),0);	//Click Edit button

		logger.info("*** Step 5 Expected Results: - Verify that it is in Edit Record popup window");
		Assert.assertTrue(recordPopUp.renalDropDown().isDisplayed(), "        Renal dropdown should present.");

		logger.info("*** Step 6 Actions - Select Renal flag and click OK button");
		recordPopUp.setRenalFlag("Y");
		recordPopUp.clickOK();

		logger.info("*** Step 6 Expected Results: - Verify that the Renal flag is showing in the selected Ordered test row");
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.orderedTestDetailTable(), "tbl_orderedTestDetail", orderedTestAbbrev, "Yes", 5, 43), 0);

		logger.info("*** Step 7 Actions - Click Save button");
		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 7 Expected Results: - Verify that the Renal flag value 'Y' is save to DB");
		Assert.assertEquals(accessionDao.getAccnTestsByAccnIdAndAccnTestSeqId(accnId, accnTestSeqId).getBRenal(), "Y");

		logger.info("*** Step 7 Expected Results: - Verify that the Renal flag value 'Yes' displays in UI");
		accessionDetail.setOrderedTestView("All Columns");
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.orderedTestDetailTable(), "tbl_orderedTestDetail", orderedTestAbbrev, "Yes", 5, 43), 0);

		logger.info("*** Step 7 Expected Results: - Verify that the Accession Status was changed to FnlRptd");
		Assert.assertTrue(accessionDetail.accessionStatusText().getText().contains("FnlRptd"), "        " + accessionDetail.accessionStatusText().getText() + " should match " + "FnlRptd.");

		logger.info("*** Step 7 Expected Results: - Verify that the accn status was changed to 11 in DB");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 11);

		logger.info("*** Step 8 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 8 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Billable Proc Code grid-Add modifier")
	@Parameters({"project", "testSuite", "testCase"})
	public void testXPR_5(String project, String testSuite, String testCase) throws Exception {
		logger.info("===== Testing - testXPR_5 =====");
		accessionUtils = new AccnDetailUtils(driver, config);
		testDataSetup = new TestDataSetup(driver);
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		recordPopUp = new RecordPopUp(driver,wait);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Actions: - Create a new Priced accn with P payor");
        Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");

		logger.info("*** Step 2 Expect Results: - A new Priced accn was created.");
		Assert.assertNotNull(accnId, "        No new accession was created");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "       Accn Id = " + accnId + " should be Priced.");

		logger.info("*** Step 3 Action: - Load new priced accession in the Accession ID field and tab out");
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");	//Wait until the page is loaded completely when Save button became clickable again

		logger.info("*** Step 3 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 4 Actions - Select a proc code in the Billable Proc Code Detail grid");
		AccnProc accnProc = accessionDao.getAccnProcByAccnId(accnId);
		String procId = accnProc.getProcId();
		int accnTestSeqId = accnProc.getAccnTestSeqId();
		int rowNum = accessionDetail.getRowNumberInWebTable(accessionDetail.billableProcCodeDetailsTable(), "tbl_billableProcedureCodeDetails", procId, 8);	//Click the selected proc code row
		clickHiddenPageObject(accessionDetail.billableProcCodeDetailsText(rowNum, 1),0);

		logger.info("*** Step 5 Actions - Click the index column and select Edit Row hyperlink");
		accessionDetail.billableProcCodeDetailsEditRowLnk(rowNum-1);

		logger.info("*** Step 5 Expected Results: - Verify that Mod 4 dropdown list displays in the Edit Record window");
		Assert.assertTrue(recordPopUp.mod4DropDown().isDisplayed(), "        Mod 4 dropdown should present.");

		logger.info("*** Step 6 Actions - Select a Modifier in the Mod 4 drop down list and click OK");
		String modValue = "AA";
		recordPopUp.setMod4(modValue);
		recordPopUp.clickOK();

		logger.info("*** Step 6 Expected Results - Verify that the Modifier is showing in the UI");
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.billableProcCodeDetailsTable(), "tbl_billableProcedureCodeDetails", procId, modValue, 8, 14 ), 0);

		logger.info("*** Step 7 Actions - Click Save button");
		accessionDetail.clickSave();
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 7 Expected Results - Verify that the Modifier is added in DB");
		AccnProc accnProcValue = accessionDao.getAccnProcByAccnIdProcIdAndTestSeqId(accnId, procId, accnTestSeqId);	//The modifiers can be saved to any mod fields in accn_proc table
		List<String> modList=new ArrayList<>();
		modList.add(accnProcValue.getMod1Id());
		modList.add(accnProcValue.getMod2Id());
		modList.add(accnProcValue.getMod3Id());
		modList.add(accnProcValue.getMod4Id());
		Assert.assertTrue(accessionUtils.isValueExistInList(modList, modValue), "        The Modifier " + modValue + " should be saved in DB.");

		logger.info("*** Step 7 Expected Results - Verify that the Modifier is showing in UI");
		int colIndex2 = accessionDetail.colIndexInWebTable(accessionDetail.billableProcCodeDetailsTable(), "tbl_billableProcedureCodeDetails", 14, modValue, 4);
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.billableProcCodeDetailsTable(), "tbl_billableProcedureCodeDetails", procId, modValue, 8, colIndex2), 0);

		logger.info("*** Step 7 Expected Results - Verify that the accn is NOT back out repriced");
		Assert.assertTrue(accessionDetail.accessionStatusText().getText().contains("Priced"), "        " + accessionDetail.accessionStatusText().getText() + " should match " + "Priced.");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21);

		logger.info("*** Step 8 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 8 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Billable Proc Code grid-Add Adjustment")
	@Parameters({"project", "testSuite", "testCase"})
	public void testXPR_10(String project, String testSuite, String testCase) throws Exception {
		logger.info("===== Testing - testXPR_10 =====");
		accessionUtils = new AccnDetailUtils(driver, config);
		testDataSetup = new TestDataSetup(driver);
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		recordPopUp =  new RecordPopUp(driver,wait);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Actions: - Create a new Priced accn with P payor");
        Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");

		logger.info("*** Step 2 Expect Results: - A new Priced accn was created.");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

		logger.info("*** Step 3 Action: - Load new priced accession in the Accession ID field and tab out");
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");	//Wait until the page is loaded completely when Save button became clickable again

		logger.info("*** Step 3 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 4 Actions - Select a proc code in the Billable Proc Code Detail grid");
		AccnProc accnProc = accessionDao.getAccnProcByAccnId(accnId);
		String procId = accnProc.getProcId();
		int accnTestSeqId = accnProc.getAccnTestSeqId();
		int rowNum = accessionDetail.getRowNumberInWebTable(accessionDetail.billableProcCodeDetailsTable(), "tbl_billableProcedureCodeDetails", procId, 8);	//Click the selected proc code row
		clickHiddenPageObject(accessionDetail.billableProcCodeDetailsText(rowNum, 1),0);

		logger.info("*** Step 5 Actions - Click the index column and select Add Adjustment hyperlink");
		accessionDetail.billableProcCodeDetailsAddAdjLnk(rowNum-1);

		logger.info("*** Step 5 Expected Results - Verify that the Add Adjustment popup window shows");
		Assert.assertTrue(recordPopUp.adjCodeDropDown().isDisplayed(), "        Adj Code drop down list should show.");

		logger.info("*** Step 6 Actions - Choose an Adjustment Code and Amount and tab out");
		AdjCd adjCd = adjustmentCodeDao.getRandomAdjCdIsNotSysAdded();
		int adjCdId = adjCd.getAdjCdId();
		String adjAbbrev = adjCd.getAdjAbbrv();
		recordPopUp.setAdjCode(String.valueOf(adjCdId));
		float dueAmtWithBulkOriginal = accessionDao.getAccnProcByAccnIdProcIdAndTestSeqId(accnId, procId, accnTestSeqId).getDueAmtWithBulk();
		DecimalFormat form = new DecimalFormat("0.00");
		String adjAmtStr = String.valueOf(form.format(dueAmtWithBulkOriginal));
		float adjAmt = Float.parseFloat(adjAmtStr);
		recordPopUp.setAdjAmount(adjAmtStr);

		logger.info("*** Step 6 Expected Results - Verify that the New Balance in the Reference Information grid was updated properly");
		float dueAmtWithBulkUpdated = dueAmtWithBulkOriginal + adjAmt;	//Calculate the New Balance
		String dueAmtWithBulkUpdatedStr = String.valueOf(form.format(dueAmtWithBulkUpdated));	//Convert number to string, format the value to 2 decimals before converting to String
		Assert.assertEquals(recordPopUp.newBalanceText(), dueAmtWithBulkUpdatedStr);

		logger.info("*** Step 7 Actions - Click Post Now button");
		Assert.assertTrue(recordPopUp.postNowBtn().isEnabled(), "        Post Now button should show.");
		recordPopUp.postNowBtn().click();

		logger.info("*** Step 7 Expected Results - Verify that the Balance Due Amount was updated properly in Billable Procedure Code Detail table for the selected Proc Code");
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.billableProcCodeDetailsTable(), "tbl_billableProcedureCodeDetails", procId, dueAmtWithBulkUpdatedStr, 8, 42),0);

		logger.info("*** Step 7 Expected Results - Verify that the Due Amount was updated properly in DB and Payor Summary grid");
		ArrayList<String> dueAmountList = daoManagerPlatform.getDueAmtWithBulkFromACCNPROCByAccnIdPyrPrio(accnId, "1", testDb);
		String dueAmtInPyrSummary = dueAmountList.get(0);
		String pyrAbbrevInPyrSummary = dueAmountList.get(2);
		Assert.assertEquals(dueAmtInPyrSummary, dueAmtWithBulkUpdatedStr);	//Verify in DB
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.pyrSummaryTable(), "tbl_payorSummary", pyrAbbrevInPyrSummary, dueAmtInPyrSummary, 3, 8),0);	//Verify in UI

		logger.info("*** Step 7 Expected Results - Verify that the Adjustment Amount was updated properly in DB and Adjustment Summary grid");
		ArrayList<String> adjAmountList = daoManagerPlatform.getBilAdjAmtFromACCNADJByAccnIdPyrPrioAdjCdId(accnId, 1, adjCdId, testDb);
		String adjAmtInAdjSummary = adjAmountList.get(0);
		String pyrAbbrevInAdjSummary = adjAmountList.get(2);
		Assert.assertEquals(adjAmtInAdjSummary, adjAmtStr);	//Verify in DB
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.adjSummaryTable(), "tbl_adjustmentSummary", pyrAbbrevInAdjSummary, adjAbbrev, adjAmtInAdjSummary, 3, 5, 7), 0);	//Verify in UI

		logger.info("*** Step 7 Expected Results - Verify that the accn is NOT back out repriced");
		Assert.assertTrue(accessionDetail.accessionStatusText().getText().contains("Priced"), "        " + accessionDetail.accessionStatusText().getText() + " should match " + "Priced.");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21);

		logger.info("*** Step 8 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 8 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Bulk Transaction Detail grid-Add Override")
	@Parameters({"project", "testSuite", "testCase"})
	public void testXPR_22(String project, String testSuite, String testCase) throws Exception {
		logger.info("===== Testing - testXPR_22 =====");
		accessionUtils = new AccnDetailUtils(driver, config);
		testDataSetup = new TestDataSetup(driver);
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		recordPopUp =  new RecordPopUp(driver,wait);
		timeStamp =  new TimeStamp();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Actions: - Create a new Priced accn with P payor");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");

		logger.info("*** Step 2 Expect Results: - A new Priced accn was created.");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

		logger.info("*** Step 3 Action: - Load new priced accession in the Accession ID field and tab out");
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");	//Wait until the page is loaded completely when Save button became clickable again

		logger.info("*** Step 3 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 4 Actions: - Click Over-ride hyperlink in Bulk Transaction Detail grid");
		accessionDetail.addOverrideLink();

		logger.info("*** Step 4 Expected Results: - Verify that Add Over-ride popup window shows");
		Assert.assertTrue(recordPopUp.overrideDropDown().isDisplayed(), "        Over-ride drop down list should show.");

		logger.info("*** Step 5 Actions: - Select Over-ride and Payor ID");
		List<ErrCd> overrideList = errorProcessingDao.getErrCdByErrGrpId(5);
		String overrideCode = overrideList.get(0).getAbbrev();
		String overrideCodeShortDescr = overrideList.get(0).getShortDescr();
		int overrideCodeId = overrideList.get(0).getErrCd();
		String overrideCdDescrInCurAccnErr = overrideCode + "-" + overrideCodeShortDescr;
		recordPopUp.setOverride(overrideCode);
		Pyr pyr=payorDao.getPyrByAccnIdAndPyrPrio(accnId, 1);
		String pyrAbbrev=pyr.getPyrAbbrv();
		recordPopUp.setAddOverridePyrId(pyrAbbrev + " | 1");

		logger.info("*** Step 5 Expected Results: - Verify that Post Now button shows");
		Assert.assertTrue(recordPopUp.postNowBtn().isEnabled(), "        Post Now button should be enabled.");

		logger.info("*** Step 6 Actions: - Click Post Now button");
		recordPopUp.postNowBtn().click();

		logger.info("*** Step 6 Expected Results: - Verify that the Over-ride was saved to DB");
		String currentDate = timeStamp.getCurrentDate();
		Assert.assertNotNull(errorProcessingDao.getUnfixedAccnPyrErrsByAccnIdErrCdPyrPrioErrDt(accnId, overrideCodeId, 1, currentDate).getAccnId());

		logger.info("*** Step 6 Expected Results: - Verify that the accn was pushed to q_ep_man queue");
		Assert.assertNotNull(errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_MAN, accnId).getAccnId());

		logger.info("*** Step 6 Expected Results: - Verify that the Over-ride was added to the Bulk Payment And Details grid");
		accessionDetail.setBulkPmtView("All Columns");
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.bulkPmtAndDenialsTable(), "tbl_bulkPaymentsAndDenials", currentDate, overrideCode, pyrAbbrev, 2, 22, 28), 0);

		logger.info("*** Step 6 Expected Results: - Verify that the Over-ride displays in the Current Accession Errors grid");
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.currentAccnErrTable(), "tbl_accnCurrentErrors", currentDate, overrideCdDescrInCurAccnErr, pyrAbbrev, 3, 4, 8), 0);

		logger.info("*** Step 6 Expected Results - Verify that the accn is NOT back out repriced");
		Assert.assertTrue(accessionDetail.accessionStatusText().getText().contains("Priced"), "        " + accessionDetail.accessionStatusText().getText() + " should match " + "Priced.");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21);

		logger.info("*** Step 7 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 7 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Postal code search")
	public void testXPR_25() throws Exception {
		logger.info("===== Testing - testXPR_25 =====");
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		zipCodeSearch = new ZipCodeSearch(driver);
		zipCodeSearchResults = new ZipCodeSearchResults(driver);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
		String accnId = accessionDao.getAccnWithOnePyr().getAccnId();	//Get a Priced accnId with one payor from DB
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

		logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 3 Actions: - Click Postal Code Search button in Patient Info grid");
		accessionDetail.postalCodeSearchBtn();
		String parentWindow=switchToPopupWin();	//Switch to Search window

		logger.info("*** Step 3 Expected Results: - Verify that Zip Code Search window shows");
		Assert.assertTrue(zipCodeSearch.zipCodeInput().isDisplayed(), "        Zip Code Input field should show.");
		Assert.assertTrue(zipCodeSearch.cityInput().isDisplayed(), "        City Input field should show.");
		Assert.assertTrue(zipCodeSearch.stateDropDown().isDisplayed(), "        State drop down list should show.");

		logger.info("*** Step 4 Actions: - Click Search button without entering any values in the fields");
		zipCodeSearch.searchBtn().click();

		logger.info("*** Step 4 Expected Results: - Verify that Validation Error 'At least one field must be used to initiate a search' shows");
		Assert.assertTrue(zipCodeSearch.validationErrText().contains("At least one field must be used to initiate a search"), "        'At least one field must be used to initiate a search' validation error should show.");
		zipCodeSearch.validationErrCloseBtn();	//Click to close the Validation Error

		logger.info("*** Step 5 Actions: - Enter '*' in zip code search field and click search button");
		zipCodeSearch.setZipCode("*");
		zipCodeSearch.searchBtn().click();
		switchToPopupWin();	//Switch to Search Results window

		logger.info("*** Step 5 Expected Results: - Verify that Search Results window shows");
		Assert.assertTrue(zipCodeSearchResults.zipCodeSearchTable().isDisplayed(), "        Zip Code Search Results table should show.");

		logger.info("*** Step 5 Expected Results: - Verify that the number of Search Results returned match the number of the records in DB");
		String countInDB = String.valueOf(zipDao.getZipByZipNotZero().size());
		String pagingInfoInUI = zipCodeSearchResults.pageInfoText().replaceAll(",", "");
		Assert.assertTrue(pagingInfoInUI.contains(countInDB), "        The Search Results Paging Info " + pagingInfoInUI + " does not contain the number of records in DB " + countInDB);

		logger.info("*** Step 6 Actions: - Click New Search button");
		zipCodeSearchResults.newSearchBtn().click();
		switchToPopupWin();	//Switch back to Search window

		logger.info("*** Step 6 Expected Results: - Verify that it's back to the Search screen");
		Assert.assertTrue(zipCodeSearch.zipCodeInput().isDisplayed(), "        Zip Code Input field should show.");
		Assert.assertTrue(zipCodeSearch.cityInput().isDisplayed(), "        City Input field should show.");
		Assert.assertTrue(zipCodeSearch.stateDropDown().isDisplayed(), "        State drop down list should show.");

		logger.info("*** Step 7 Actions: - Enter '0' in zip code search field and click search button");
		zipCodeSearch.setZipCode("0");
		zipCodeSearch.searchBtn().click();
		switchToPopupWin();	//Switch to Search Results window

		logger.info("*** Step 7 Expected Results: - Verify that Search Results window shows");
		Assert.assertTrue(zipCodeSearchResults.zipCodeSearchTable().isDisplayed(), "        Zip Code Search Results table should show.");

		logger.info("*** Step 7 Expected Results: - Verify that no Search Results returned");
		Assert.assertTrue(zipCodeSearchResults.pageInfoText().contains("No Result"), "        No Result should returned.");

		logger.info("*** Step 8 Actions: - Click New Search button");
		zipCodeSearchResults.newSearchBtn().click();
		switchToPopupWin();	//Switch back to Search window

		logger.info("*** Step 8 Expected Results: - Verify that it's back to the Search screen");
		Assert.assertTrue(zipCodeSearch.zipCodeInput().isDisplayed(), "        Zip Code Input field should show.");
		Assert.assertTrue(zipCodeSearch.cityInput().isDisplayed(), "        City Input field should show.");
		Assert.assertTrue(zipCodeSearch.stateDropDown().isDisplayed(), "        State drop down list should show.");

		logger.info("*** Step 9 Actions: - Enter '92130' in zip code search field and click search button");
		zipCodeSearch.setZipCode("92130");
		zipCodeSearch.searchBtn().click();
		switchToPopupWin();	//Switch to Search Results window

		logger.info("*** Step 9 Expected Results: - Verify that Search Results window shows");
		Assert.assertTrue(zipCodeSearchResults.zipCodeSearchTable().isDisplayed(), "        Zip Code Search Results table should show.");

		logger.info("*** Step 9 Expected Results: - Verify that the Search Result show in the search results window");
		Zip zip = zipDao.getZipById("92130");
		String zipCode = zip.getZipId();
		String cityName = zip.getCtyNm();
		String state = zip.getStId();
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(zipCodeSearchResults.zipCodeSearchResultsTable(), "zipCodeSearchTable", zipCode, cityName, state, 2, 3, 4), 0);

		logger.info("*** Step 10 Actions: - Click Close button in Search Results window");
		zipCodeSearchResults.closeBtn().click();

		logger.info("*** Step 10 Expected Results: - Verify that it's on the Detail page");
		switchToParentWin(parentWindow);
		Assert.assertTrue(isElementPresent(accessionDetail.accnIdText(),5), "        Accession ID input field should present.");

		logger.info("*** Step 11 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 11 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description ="User Preferences - Save UP won't change the data on the ACCN")
	public void testXPR_643() throws Exception {
		logger.info("===== Testing - testXPR_643 =====");
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
		String accnId = accessionDao.getRandomAccnFromAccnByAccnStatus("11").getAccnId();//Get a final reported accn from DB
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

		logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 3 Action: - Go to Patient info section and update patient address");
		Assert.assertTrue(isElementPresent(accessionDetail.setUserPreferencesLink(),5), "        setUserPreferencesLink should present.");
		accessionDetail.clickSetUserPreferenceLink();
		switchToPopupWin();
		accessionDetail.clickAllPreferencesRadio(1);
		clickHiddenPageObject(accessionDetail.saveUserPreferenceButton(), 0);
		Assert.assertTrue(isElementPresent(accessionDetail.detailViewBtn(),5), "        Detail View button should show.");
		clickHiddenPageObject(accessionDetail.detailViewBtn(),0);
		Assert.assertTrue(isElementPresent(accessionDetail.sectionSearchInput(),5), "        Search Section input field should show.");
		accessionDetail.enterSectionSearchField("Patient");
		Assert.assertTrue(isElementPresent(accessionDetail.patientAddressLine(1),5), "        Patient address line 1 input field should show.");
		String oldPatientAddressLine1 = accessionDetail.patientAddressLine(1).getAttribute("value");
		accessionDetail.inputToPatientAddressLine(1, "new data"+oldPatientAddressLine1);

		logger.info("***Step 4 Actions: Focus on footer menu and Click on Collapse button");
		Assert.assertTrue(isElementPresent(accessionDetail.collapseViewBtn(),5), "        Collapse button should show.");
		accessionDetail.clickCollapseBtn();

		logger.info("***Step 4 Expected Results: Verify that ALL sections are collapsed");
		String position = "0px -16px";
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionErrorsBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionNotesBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapsePhysicianInfoSectionBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapsePhlebInfoSectionBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionPatientBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionDiagnosisBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionInsuranceBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionOccurrenceAndValuesBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionEligibilityTransactionsBtn(),position);
		accessionDetail.enterSectionSearchField("Claim Status Transactions ");
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClaimsStatusTransactionsBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionSubmitClaimsBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionTransactionsBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionAccnLevelPricingMessagesBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionConsolidationDetailsBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionBillableProcedureCodeAndBulkTransactionsBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClientSpecificQuestionsBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClinicalTrialBtn(),position);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionChainOfCustodyBtn(),position);

		logger.info("***Step 5 Actions: Click on Save User Preference Button");
		List<String> sectionBefore = accessionDetail.getAllDisplayOfSections();
		Assert.assertTrue(isElementPresent(accessionDetail.saveUserPreferenceButton(),5), "        Save User Preference Button should show.");
		clickHiddenPageObject(accessionDetail.saveUserPreferenceButton(),0);

		logger.info("***Step 6 Actions: Click on Reset button");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(),5), "        Reset Button should show.");
		accessionDetail.clickReset();
		closeAlertAndGetItsText(true);

		logger.info("***Step 6 Expected Results: Verify that Load Accession page is displayed");
		Assert.assertTrue(isElementPresent(accessionDetail.accnIdInput(),5), "        Accession ID input field should show.");

		logger.info("***Step 7 Actions: Load the same Accession again");
		accessionDetail.setAccnId(accnId);

		logger.info("***Step 7 Expected Result: Verify that ALL sections are collapsed and Patient address is not updated");
		List<String> sectionAfter = accessionDetail.getAllDisplayOfSections();
		Assert.assertEquals(sectionAfter,sectionBefore, "        All section are collapsed.");
		Assert.assertTrue(isElementPresent(accessionDetail.detailViewBtn(),5), "        Detail view button should show.");
		accessionDetail.clickDetailBtn();
		Assert.assertTrue(isElementPresent(accessionDetail.sectionSearchInput(),5), "        Search Section input field should show.");
		accessionDetail.setSectionSearch("Patient");
		Assert.assertTrue(isElementPresent(accessionDetail.patientAddressLine(1),5), "        Patient address line 1 input field should show.");
		Assert.assertEquals(accessionDetail.patientAddressLine(1).getAttribute("value"),oldPatientAddressLine1, "        Data on Patient Address Line 1 is not updated.");

		logger.info("*** Step 8 Actions: - Reset the User Preference back to it's default");
		Assert.assertTrue(isElementPresent(accessionDetail.collapseViewBtn(),5), "        Collapse button should show.");
		accessionDetail.clickCollapseBtn();
		Assert.assertTrue(isElementPresent(accessionDetail.setUserPreferencesLink(),5), "        setUserPreferencesLink should present.");
		accessionDetail.clickSetUserPreferenceLink();
		switchToPopupWin();
		accessionDetail.setDefaultUserPreference();
		clickHiddenPageObject(accessionDetail.saveUserPreferenceButton(),0);
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(),5), "        Reset Button should show.");
		accessionDetail.clickReset();
	}

	@Test(priority = 1, description = "User Preferences - Verify Set UP is working when user set different states")
	public void testXPR_644() throws Exception {
		logger.info("===== Testing - testXPR_644 =====");
		String detailIcon   = "-64px -16px";
		String summaryIcon  = "-32px -16px";
		String collapseIcon = "0px -16px";
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Action: - Load an existing accession in the Accession ID field and tab out");
		String accnId = accessionDao.getRandomAccnFromAccnByAccnStatus("11").getAccnId();	//Get a final reported accn from DB
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

		logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 3 Actions: Click on Collapse button at footer menu");
		Assert.assertTrue(isElementPresent(accessionDetail.setUserPreferencesLink(),5), "        setUserPreferencesLink should present.");
		accessionDetail.clickSetUserPreferenceLink();
		switchToPopupWin();
		accessionDetail.clickAllPreferencesRadio(1);
		Assert.assertTrue(isElementPresent(accessionDetail.saveUserPreferenceButton(),5), "        saveUserPreferenceButton should present.");
		clickHiddenPageObject(accessionDetail.saveUserPreferenceButton(),0);
		Assert.assertTrue(isElementPresent(accessionDetail.collapseViewBtn(),5), "        Collapse button should show.");
		accessionDetail.clickCollapseBtn();

		logger.info("*** Step 3 Expected Results: Accession Detail page is displayed and All section are collapsed");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");
		accessionDetail.enterSectionSearchField("Accession Errors");
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionNotesBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapsePhysicianInfoSectionBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapsePhlebInfoSectionBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionPatientBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionDiagnosisBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionInsuranceBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionOccurrenceAndValuesBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionEligibilityTransactionsBtn(), collapseIcon);
		accessionDetail.enterSectionSearchField("Claim Status Transactions ");
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClaimsStatusTransactionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionSubmitClaimsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionTransactionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionAccnLevelPricingMessagesBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionConsolidationDetailsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionBillableProcedureCodeAndBulkTransactionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClientSpecificQuestionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClinicalTrialBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionChainOfCustodyBtn(), collapseIcon);

		logger.info("*** Step 4 Actions: Click on Save User Preferences link and Reset the page");
		clickHiddenPageObject(accessionDetail.saveUserPreferenceButton(),0);
		accessionDetail.clickReset();

		logger.info("*** Step 4 Expected Results: Verify that it's back to the Load Accession screen");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");

		logger.info("*** Step 5 Actions: Load the same Accession again");
		loadAccession.setAccnId(accnId);
		accessionDetail.clickCollapseBtn();

		logger.info("*** Step 5 Expected Results: Verify that All sections are collapsed");
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionNotesBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapsePhysicianInfoSectionBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapsePhlebInfoSectionBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionPatientBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionDiagnosisBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionInsuranceBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionOccurrenceAndValuesBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionEligibilityTransactionsBtn(), collapseIcon);
		accessionDetail.enterSectionSearchField("Claim Status Transactions ");
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClaimsStatusTransactionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionSubmitClaimsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionTransactionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionAccnLevelPricingMessagesBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionConsolidationDetailsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionBillableProcedureCodeAndBulkTransactionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClientSpecificQuestionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClinicalTrialBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionChainOfCustodyBtn(), collapseIcon);

		logger.info("*** Step 6 Actions: Click on Set User Preference.");
		accessionDetail.clickSetUserPreferenceLink();

		logger.info("*** Step 6 Expected Results: Accession Detail Preference popup is displayed");
		Assert.assertTrue(isElementPresent(accessionDetail.accessionErrorsDetailRadio(),5), "        Accession Errors radio should present.");

		logger.info("*** Step 7 Actions: Set different States for each section and click Save button.");
		accessionDetail.clickAccnErorsHiddenRadio();
		accessionDetail.clickContactNotesDetailRadio();
		accessionDetail.clickPhysicianInfoSummaryRadio();
		clickHiddenPageObject(accessionDetail.saveUserPreferenceButton(),0);

		logger.info("*** Step 8 Actions: Reset the page and reload the same Accession");
		accessionDetail.clickReset();
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
		loadAccession.setAccnId(accnId);

		logger.info("*** Step 9 Expected Results: - Verify that the changed Grids will be displayed accordingly to updated User Preference setting and unchanged Grids are still collapsed");
		Assert.assertEquals(accessionDetail.collapseSectionErrorsBtn().getAttribute("title"),"HIDDEN view", "        Accession Errors grid should be hidden.");
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionNotesBtn(), detailIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapsePhysicianInfoSectionBtn(), summaryIcon);
		accessionDetail.clickCollapseBtn();	// unchanged Grids are still collapse
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapsePhlebInfoSectionBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionPatientBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionDiagnosisBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionInsuranceBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionOccurrenceAndValuesBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionEligibilityTransactionsBtn(), collapseIcon);
		accessionDetail.enterSectionSearchField("Claim Status Transactions ");
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClaimsStatusTransactionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionSubmitClaimsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionTransactionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionAccnLevelPricingMessagesBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionConsolidationDetailsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionBillableProcedureCodeAndBulkTransactionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClientSpecificQuestionsBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionClinicalTrialBtn(), collapseIcon);
		accessionDetail.checkSectionIsCollapsed(accessionDetail.collapseSectionChainOfCustodyBtn(), collapseIcon);

		logger.info("*** Step 10 Actions: - Reset the User Preference back to it's default");
		Assert.assertTrue(isElementPresent(accessionDetail.collapseViewBtn(),5), "        Collapse button should show.");
		accessionDetail.clickCollapseBtn();
		Assert.assertTrue(isElementPresent(accessionDetail.setUserPreferencesLink(),5), "        setUserPreferencesLink should present.");
		accessionDetail.clickSetUserPreferenceLink();
		switchToPopupWin();
		accessionDetail.setDefaultUserPreference();
		clickHiddenPageObject(accessionDetail.saveUserPreferenceButton(),0);
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(),5), "        Reset Button should show.");
		accessionDetail.clickReset();
	}

	@Test(priority = 1, description ="User Preferences - No fixable error will be fixed when Accession Error grid is hidden")
	public void testXPR_648() throws Exception {
		logger.info("===== Testing - testXPR_648 =====");
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Action: - Load an existing accession with fixable error in the Accession ID field and tab out");
		AccnProcErr accnProcErr = errorProcessingDao.getOneFixableDenialErrFromAccnProcErr(3);
		String accnId = accnProcErr.getAccnId();
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

		logger.info("*** Step 2 Expected Results: - Verify that the accn was loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 3 Actions: - Click on Set User Preference link and set Hidden State for Accn Errors section and Save");
		Assert.assertTrue(isElementPresent(accessionDetail.setUserPreferencesLink(),5), "        setUserPreferencesLink should present.");
		accessionDetail.clickSetUserPreferenceLink();
		switchToPopupWin();
		Assert.assertTrue(isElementPresent(accessionDetail.accessionErrorsHiddenRadio(),5), "        accessionErrorsHiddenRadio should present.");
		accessionDetail.clickGridsRadio(accessionDetail.accessionErrorsHiddenRadio(), "accessionErrorsHiddenRadio");
		clickHiddenPageObject(accessionDetail.saveUserPreferenceButton(),0);

		logger.info("*** Step 4 Actions: - Reset the page and reload the same Accession");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(),5), "        Reset button should present.");
		accessionDetail.clickReset();
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
		accessionDetail.setAccnId(accnId);

		logger.info("*** Step 4 Expected Results: - Verify that Accession Detail Page shows");
		Assert.assertTrue(isElementPresent(accessionDetail.accnIdText(),5), "        Accession ID text field should present.");

		logger.info("*** Step 5 Actions: - Click on Save and Clear Button");
		Assert.assertTrue(isElementPresent(accessionDetail.saveAndClearBtn(),5), "        Save and Clear button should present.");
		clickHiddenPageObject(accessionDetail.saveAndClearBtn(), 0);

		logger.info("*** Step 5 Expected Results: - Verify that it's back to Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");

		logger.info("*** Step 6 Actions: - Reload the same Accession");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
		accessionDetail.setAccnId(accnId);

		logger.info("*** Step 6 Expected Results: - Accession detail page is  displayed");
		Assert.assertTrue(isElementPresent(accessionDetail.accnIdText(),5), "        Accession ID text field should present.");
		Assert.assertEquals(accessionDetail.accnIdText().getAttribute("value"),accnId, "        Accession ID text field should present.");

		logger.info("*** Step 6 Expected Results: - Verify that the fixable error is still in DB with fix_dt = null");
		int errSeqId = accnProcErr.getErrSeq();	//Get the error seq id
		Assert.assertNull(errorProcessingDao.getAccnProcErrsByAccnIdAndErrSeq(accnId, errSeqId).getFixDt(), "       Fixable error on Accession " + accnId + " should not be fixed.");

		logger.info("*** Step 7 Actions: - Reset the User Preference back to it's default");
		Assert.assertTrue(isElementPresent(accessionDetail.collapseViewBtn(),5), "        Collapse button should show.");
		accessionDetail.clickCollapseBtn();
		Assert.assertTrue(isElementPresent(accessionDetail.setUserPreferencesLink(),5), "        setUserPreferencesLink should present.");
		accessionDetail.clickSetUserPreferenceLink();
		switchToPopupWin();
		accessionDetail.setDefaultUserPreference();
		clickHiddenPageObject(accessionDetail.saveUserPreferenceButton(),0);
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(),5), "        Reset Button should show.");
		accessionDetail.clickReset();
	}

	@Test(priority = 1, description = "Add Electronic Payment to Priced Accession")
	@Parameters({"project", "testSuite", "testCase", "cardNumber", "expirationMonth", "expirationYear","firstName","lastName","streetAddress","zipCode"})
	public void testXPR_1080(String project, String testSuite, String testCase, String cardNumber, String expirationMonth, String expirationYear,String firstName,String lastName,String streetAddress,String zipCode) throws Exception {
		logger.info("===== Testing - testXPR_1080 =====");
		randomCharacter = new RandomCharacter();
		accessionUtils = new AccnDetailUtils(driver, config);
		loadAccession = new LoadAccession(driver);
		accessionDetail = new AccessionDetail(driver, config, wait);
		testDataSetup = new TestDataSetup(driver);

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Accession Detail page");
		assertTrue(isElementPresent(accessionDetail.accessionDetailTitle(), 5),"        The Accession Detail page title should be displayed");
		assertTrue(accessionDetail.accessionDetailTitle().getText().trim().contains("Detail"),"        Page Title should be 'Detail'");

		logger.info("*** Step 2 Actions: - Send Accession WS to create a new Priced accession");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");

		logger.info("*** Step 2 Expected Results: - A new Priced accn was created.");
		Assert.assertNotNull(accnId, "        No new accession was created");

		logger.info("*** Step 3 Actions: - Load the Priced accession in the Accession ID field and tab out");
		loadAccession.setAccnId(accnId);
		accessionDetail = new AccessionDetail(driver, config, wait);
		Assert.assertTrue(isElementPresent(accessionDetail.accnIdText(),10), "Accession ID input field was not found");
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

		logger.info("*** Step 3 Expected Results: - Verify that the accn is loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded properly.");

		logger.info("*** Step 4 Actions: - Click on Electronic Payment Link in Billable Procedure Code and Bulk Transaction Detail section");
		assertTrue(isElementPresent(accessionDetail.detailViewAllIco(),5),"        Detail view all icon is displayed.");
		clickHiddenPageObject(accessionDetail.detailViewAllIco(), 0);
		scrollToElement(accessionDetail.bulkTransDetailSectionElcPaymentLnk());
		clickHiddenPageObject(accessionDetail.bulkTransDetailSectionElcPaymentLnk(), 0);

		logger.info("*** Step 4 Expected results: - Verify that Electronic Payment Wizard popup is displayed");
		Money moneyAmt = accessionDao.getAccn(accnId).dueAmt;
		String amount = String.valueOf(moneyAmt);
		String paymentCmt = "QA "+randomCharacter.getRandomAlphaNumericString(6);
		switchToFrame(accessionDetail.ptmBoxiFrame());

		logger.info("*** Step 4 Expected results: Verify that Account holder Information popup is shown and Pay button is disabled");
		assertTrue(isElementPresent(accessionDetail.payBtn(),5),"        Pay button is displayed.");
		assertFalse(accessionDetail.payBtn().isEnabled(),"        Pay button is disabled.");

		logger.info("*** Step 5 Actions: - Enter Amount and Cardholder Information");
		accessionDetail.setInputValue(accessionDetail.cardInformationAmountInput(), amount);
		accessionDetail.setInputValue(accessionDetail.cardNumberInput(), cardNumber);
		selectItemByVal(accessionDetail.expMonthInput(), expirationMonth);
		selectItemByVal(accessionDetail.expYearInput(), expirationYear);
		accessionDetail.setInputValue(accessionDetail.cvvInput(), "123");
		accessionDetail.setInputValue(accessionDetail.billingFirstNameInput(),firstName);
		accessionDetail.setInputValue(accessionDetail.billingLastNameInput(),lastName);
		accessionDetail.setInputValue(accessionDetail.billingStreetAddressInput(),streetAddress);
		accessionDetail.setInputValue(accessionDetail.billingZipCodeInput(),zipCode);

		logger.info("*** Step 6 Actions: - Click on Pay button");
		clickHiddenPageObject(accessionDetail.payBtn(), 0);

		logger.info("*** Step 6 Expected Results: - Verify that Confirm Payment Information is displayed and Card Number is hidden except the last 4 digits");
		assertTrue(isElementPresent(accessionDetail.confirmPaymentInformationTitle(),5),"       Confirm Transaction is displayed.");
		assertTrue(isElementPresent(accessionDetail.confirmPaymentInformationTxt(),5),"       Confirm Payment Information CardNumber is displayed.");
		String hiddenCardNumber = "***** "+cardNumber.substring(15);
		assertTrue(accessionDetail.confirmPaymentInformationTxt().getText().contains(hiddenCardNumber),"       Card Number is hidden except the last 4 digits.");

		logger.info("*** Step 7 Actions: - Click on Confirm button. Click Save and Clear Button");
		assertTrue(isElementPresent(accessionDetail.confirmPaymentInformationConfirmBtn(),5),"        Confirm button is displayed.");
		clickHiddenPageObject(accessionDetail.confirmPaymentInformationConfirmBtn(), 0);
		switchToDefaultWinFromFrame();
		if(isElementPresent(accessionDetail.transStatusCloseIco(),5)){
			clickHiddenPageObject(accessionDetail.transStatusCloseIco(), 0);
		}
		assertTrue(isElementPresent(accessionDetail.saveAndClearBtn(),5),"        Save and Clear button is displayed.");
		clickHiddenPageObject(accessionDetail.saveAndClearBtn(), 0);

		logger.info("*** Expected Results: - Verify that the Electronic Payment is saved in DB");
		List<ElcPmtDetail> elcPmtDetailList = paymentDao.getElcPmtDetailByAccnId(accnId);
		assertEquals(elcPmtDetailList.get(0).transAmt, moneyAmt,"        The Electronic Payment Amount " + amount + " should be saved to ELC_PMT_DETAIL table.");

		logger.info("*** Step 8 Actions: - Reload the Accession ID");
		loadAccession.setAccnId(accnId);
		Assert.assertTrue(isElementPresent(accessionDetail.accnIdText(),10), "Accession ID input field was not found");
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

		logger.info("*** Step 8 Expected Results: - Verify that the payment is displayed in Bulk Payments And Denials table");
		scrollToElement(accessionDetail.bulkPmtAndDenialsTable());
		assertTrue(isElementPresent(accessionDetail.bulkPmtAndDenialsTblPaymentAmountCol("last()"),5),"       New payment is displayed.");
		assertEquals(String.valueOf(moneyAmt),accessionDetail.bulkPmtAndDenialsTblPaymentAmountCol("last()").getText(),"        New payment is displayed.");

		logger.info("*** Step 9 Actions: - Reset the Detail page");
		Assert.assertTrue(isElementPresent(accessionDetail.resetBtn(), 5), "        Reset button should be clickable.");
		accessionDetail.clickReset();

		logger.info("*** Step 9 Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");
	}

	@Test(priority = 1, description = "Force Accn to EP Outside Agency Pre Corresp")
	@Parameters({"project", "testSuite", "testCase"})
	public void testXPR_1553(String project, String testSuite, String testCase) throws Exception {
		logger.info("===== Testing - testXPR_1553 =====");
		randomCharacter = new RandomCharacter();
		accessionUtils = new AccnDetailUtils(driver, config);
		accessionDetail = new AccessionDetail(driver, config, wait);
		timeStamp = new TimeStamp(driver);
		testDataSetup = new TestDataSetup(driver);
		navigation = new MenuNavigation(driver, config);
		loadAccession = new LoadAccession(driver);

		logger.info("*** Actions: - Set up Err Code SUBID");
		ErrCd errCd = errorDao.getErrCdByErrCd(2136); //2136: SUBID ErrCd
		errCd.setIsAutoMtch(false);
		errCd.setIsMtchCmp(false);
		errCd.setIsManActn(false);
		errCd.setCorrespTypId(0);
		errCd.setOutAgncyId(5); //5: FR-INS
		errCd.setFinalActnId(7); //7: Hold
		errCd.setOutAgncyIdPreCorresp(0);
		errorDao.setErrCd(errCd);

		logger.info("*** Actions: - Clear Cache");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

		logger.info("*** Actions: - Send WS request to create a Final Reported 3rd Party Accession with SUBID error");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");

		logger.info("*** Expected Results: - Verify that a new accession was generated");
		Assert.assertNotNull(accnId, "        A new Accession was generated.");

		logger.info("*** Actions: - Add Pt State and Pt City manually in DB");
		Accn accn = accessionDao.getAccn(accnId);
		accn.setPtStId("CA");
		accn.setPtCity("SAN DIEGO");
		accessionDao.setAccn(accn);
		logger.info("Wait for an accession to be out of Q_FR_PENDING");
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS*2);
		logger.info("Wait for an accession to be PRICED");
		accessionDao.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS);
		logger.info("Wait for an accession to be in the Q_EP_OUT_AGNCY queue");
		accessionDao.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY, QUEUE_WAIT_TIME_MS);

		logger.info("*** Expected Results: - Verify that the accession has SUBID accn_pyr_err");
		String currDtStr = timeStamp.getCurrentDate();
		AccnPyrErr accnPyrErrInfoList = errorProcessingDao.getUnfixedAccnPyrErrsByAccnIdErrCdPyrPrioErrDt(accnId, 2136,  1, currDtStr); //2136: SUBID ErrCd
		Assert.assertNotNull(accnPyrErrInfoList, "        Accession ID " + accnId + " should have SUBID accn_pyr_err.");
		logger.info("Wait for an accession to be out of Q_EP_OUT_AGNCY queue");
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY, QUEUE_WAIT_TIME_MS);

		logger.info("*** Expected Results: - Verify that the accession is out of Q_EP_OUT_AGNCY_ERR table");
		QEpErr qEpErr = errorProcessingDao.getQEpOutAgncyErrByAccnId(accnId);
		assertNull(qEpErr);

		logger.info("*** Expected Results: - Verify that the accession is added into ACCN_AGNCY table");
		AccnAgncy accnAgncy = accessionDao.getAccnAgncyByAccnId(accnId);
		assertNotNull(accnAgncy);

		logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD queue");
		QEp qEp = errorProcessingDao.getQEpHldByAccnId(accnId);
		assertNotNull(qEp);

		logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD_ERR table");
		qEpErr = errorProcessingDao.getQEpHldErrByAccnId( accnId);
		assertNotNull(qEpErr);

		logger.info("*** Expected Results: - Verify that ACCN_QUE is updated properly");
		AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
		assertEquals(accnQue.getQTyp(), 15); //15: EP Hold

		logger.info("*** Actions: - Set up Err Code SUBID in DB so it has PRE CORRESP prioritized action");
		errCd = errorDao.getErrCdByErrCd(2136); //2136: SUBID ErrCd
		errCd.setIsAutoMtch(false);
		errCd.setIsMtchCmp(false);
		errCd.setIsManActn(false);
		errCd.setCorrespTypId(0);
		errCd.setOutAgncyId(5); //5: FR-INS
		errCd.setFinalActnId(7); //7: Hold
		errCd.setOutAgncyIdPreCorresp(9); //9: FR-INS-PRE
		errorDao.setErrCd(errCd);

		logger.info("*** Actions: - Clear Cache");
		xifinAdminUtils.clearDataCache();

		logger.info("*** Actions: - Log into RPM");
		navigation.navigateToAccnDetailPage();

		logger.info("*** Expected Results: - Verify that user is logged in");
		loadAccession.setAccnId(accnId, wait);
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
		Assert.assertTrue(isElementPresent(accessionDetail.accnIdText(),10), "Accession ID input field was not found");
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

		logger.info("*** Actions: - Select 'Outside Agency Pre Corresp' in Force to EP dropdown");
		selectItemByVal(accessionDetail.forceToEpDropdown(wait), "OAPR");

		logger.info("*** Actions: - Click Save and Clear Button");
		clickHiddenPageObject(accessionDetail.saveAndClearBtn(), 0);

		logger.info("*** Expected Results: - Verify that it's back to Load Accession page");
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");

		logger.info("*** Expected Results: - Verify that the accession is in Q_EP_OUT_AGNCY_PRE_CORRESP queue");
		accnQue = accessionDao.getAccnQueByAccnId(accnId);	//Accn_que
		Assert.assertEquals(accnQue.getQTyp(), 16, "        Accession ID " + accnId + " should be in Q_Typ = 16 (EP Outside Agency Pre Corresp).");
		qEp = errorProcessingDao.getQEpOutAgncyPreCorrespByAccnId(accnId);	//Q_EP_OUT_AGNCY_PRE_CORRESP
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date currDt = new java.sql.Date(df.parse(currDtStr).getTime());
		String inDtStr = df.format(qEp.getInDt());
		Date inDt = new java.sql.Date(df.parse(inDtStr).getTime());
		assertEquals(inDt, currDt, "       Q_EP_OUT_AGNCY_PRE_CORRESP.IN_DT = " + currDt);

		logger.info("*** Expected Results: - Verify that the accession is in Q_EP_OUT_AGNCY_PRE_CORRESP_ERR table");
		qEpErr = errorProcessingDao.getQEpOutAgncyPreCorrespErrByAccnId(accnId);	//Q_EP_OUT_AGNCY_PRE_CORRESP_ERR
		assertEquals(qEpErr.getErrDt(), currDt);
	}
}

