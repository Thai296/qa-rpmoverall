package com.newXp.tests;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mbasys.mars.ejb.entity.phys.Phys;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.country.Country;
import com.mbasys.mars.ejb.entity.diagCd.DiagCd;
import com.mbasys.mars.ejb.entity.maritalStatusTyp.MaritalStatusTyp;
import com.mbasys.mars.ejb.entity.procCd.ProcCd;
import com.mbasys.mars.ejb.entity.pt.Pt;
import com.mbasys.mars.ejb.entity.ptClnLnk.PtClnLnk;
import com.mbasys.mars.ejb.entity.ptDemoChk.PtDemoChk;
import com.mbasys.mars.ejb.entity.ptDiag.PtDiag;
import com.mbasys.mars.ejb.entity.ptDialysisHistory.PtDialysisHistory;
import com.mbasys.mars.ejb.entity.ptPyrPhi.PtPyrPhi;
import com.mbasys.mars.ejb.entity.ptPyrSuspendReasonDt.PtPyrSuspendReasonDt;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrGrp.PyrGrp;
import com.mbasys.mars.ejb.entity.state.State;
import com.mbasys.mars.ejb.entity.systemSetting.SystemSetting;
import com.mbasys.mars.ejb.entity.zip.Zip;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionDetail.AccessionDetail;
import com.overall.accession.patientDemographics.PatientDemographics;
import com.overall.menu.MenuNavigation;
import com.overall.utils.PatientDemographicsUtil;
import com.overall.utils.XifinPortalUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.domain.AllAccnPt;
import com.xifin.qa.dao.rpm.domain.PtPhi;
import com.xifin.qa.dao.rpm.domain.PtPyrV2;
import com.xifin.qa.dao.rpm.domain.PtV2;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.TimeStamp;

import domain.accession.patientDemographics.AllAccessionsForThisPatientTable;
import domain.accession.patientDemographics.AssociatedPatientIDsTable;
import domain.accession.patientDemographics.DialysisInformationSection;
import domain.accession.patientDemographics.EmployerInfo;
import domain.accession.patientDemographics.Header;
import domain.accession.patientDemographics.InsuranceInformationSection;
import domain.accession.patientDemographics.InsuredInfo;
import domain.accession.patientDemographics.NonRPMDialysisPatientHistoryTable;
import domain.accession.patientDemographics.PatientDemographicsRequireFields;
import domain.accession.patientDemographics.PatientInformation;
import domain.accession.patientDemographics.PayorInfo;
import domain.accession.patientDemographics.PayorNotes;
import domain.accession.patientDemographics.PayorTab;
import domain.accession.patientDemographics.RPMDialysisPatientHistory;
import domain.accession.patientDemographics.SuspendedReasonTable;

public class PatientDemographicsTest extends SeleniumBaseTest {
	private static final String USA = "US";
    private static final String EMPTY = "";
    private static final String NEW_EPI = "New EPI";
    private static final String NEW_SSN = "New SSN";
    private static final String NEWEST_ROW = "last()";
    private static final String AREA_CODE_USA = "2066";
    private static final String UPDATE = "update a row";
    private static final String EXIST_EPI = "Exist EPI";
    private static final String EXIST_SSN = "Exist SSN";
    private static final String ADD_NEW = "add new row";
    private static final String SUFFIX_EMAIL = "@gmail.com";
    private static final String EMPTY_RECORDS = "Empty records";
    private static final String RELATIONSHIP_ORTHER_TYPE = "other";
    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    private static final String ADD_PAYOR_POPUP_TITLE = "Add Payor";
    private static final String PAYOR_INFO_SECTION_NM = "Payor Info";
    private static final String FULL_FIELDS = "all fields in section";
    private static final String ACCESSION_DETAIL_PAGE_TITLE = "Detail";
    private static final String PAYOR_NOTES_SECTION_NM = "Payor Notes";
    private static final String INSURED_INFO_SECTION_NM = "Insured Info";
    private static final String EMPLOYER_INFO_SECTION_NM = "Employer Info";
    private static final String REQUIRED_FIELDS = "only required field fields";
    private static final String EMPLOYMENT_STATUS_SELF_EMPL_TYPE = "4 - Self Empl";
    private static final String CREATE_NEW_EPI = "New EPI with Create new EPI popup";
    private static final String SOURCE_TYPE[] = { "Client", "Client Primary Facility" };
    private static final String CLIENT ="Client";
    private static final String PATIENT_INFORMATION_SECTION_NM = "Patient Information";
    private static final String DIALYSIS_INFORMATION_SECTION_NM = "Dialysis Information";
    private static final String PATIENT_DEMOGRAPHICS_PAGE_TITLE = "Patient Demographics";
    private static final String INSURANCE_INFORMATION_SECTION_NM = "Insurance Information";
    private static final String ASSOCIATED_PATIENT_IDS_SECTION_NM = "Associated Patient IDs";
    private static final String NEW_EPI_PATIENT_LAST_NAME = "New EPI with input pt last name";
    private static final String RPM_DIALYSIS_PATIENT_HISTORY_SECTION_NM = "RPM Dialysis Patient History ";
    private static final String FULL_FIELDS_WITHOUT_PAYOR_ID = "all fields in section without input PayorId";
    private static final String ALL_ACCESSIONS_FOR_THIS_PATIENT_SECTION_NM = "All Accessions for this Patient";
    private static final String NON_RPM_DIALYSIS_PATIENT_HISTORY_SECTION_NM = "Non-RPM Dialysis Patient History";
    private static final int MCARE_PYR_GRP = 1;
    private static final int PATIENT_PYR_GRP = 4;
    
    private Accn orgAccn;
    private Header actHeader;
    private TimeStamp timeStamp;
    private String userName = EMPTY;
    private Long validNpi;
    private AccessionDetail accessionDetail;
    private RandomCharacter randomCharacter;
    private XifinPortalUtils xifinPortalUtils;
    private PatientDemographics patientDemographics;
	private PatientDemographicsUtil patientDemographicsUtil;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"ssoXpUsername", "ssoXpPassword"})
    public void beforeMethod(String ssoXpUsername, String ssoXpPassword, Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            timeStamp = new TimeStamp();
            randomCharacter = new RandomCharacter();
            xifinPortalUtils = new XifinPortalUtils(driver);
            patientDemographics = new PatientDemographics(driver, wait);
            patientDemographicsUtil = new PatientDemographicsUtil(config);
            logIntoSso(ssoXpUsername, ssoXpPassword);
            MenuNavigation menuNavigation = new MenuNavigation(driver, config);
            menuNavigation.navigateToAccessionPatientDemographicsPage();
            this.userName = "x" + ssoXpUsername.substring(0, ssoXpUsername.indexOf("@"));
//            this.userName = "chava";
        } catch (Exception e) {
            logger.error("Error running BeforeMethod", e);
        }
    }
    
    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            revertAccn(orgAccn);
//            cleanNewEPI(actHeader);
        } catch (Exception e) {
            logger.error("Error running afterMethod", e);
        }
    }
    
    /**
     * Test scripts
     * ================================================================================================================================
     */

    @Test(priority = 1, description = "XPR-1781: Accn Payment Demographics - Accession Detail page is displayed when click on AccnId link at All Accessions for this Patient grid")
    public void testXPR_1781() throws Exception {
        logger.info("==== Testing - testXPR_1781 ==== ");  
        
        try {
        	logger.info("**** Starting get data to investigate. ****");
        	List<AccnQue> ques = accessionDao.getAccnQueListByAccnIdMinQCnt("TCC965", 0);
        	for(AccnQue accnQue : ques) {
        		logger.info("AccnQue: " + accnQue);
        	}
        }catch(Exception e) {
        	logger.info("Data TCC965 not exists.");
        }
            
        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
        logger.info("*** Step 2 Action: Create new EPI with required fileds. Update ptSeqId to exist accn");
        actHeader = setDataPatientDemographicsSection(NEW_EPI, EMPTY, 0);
        logger.info("actHeader: " + actHeader);
        PatientInformation actPtientInformation = setPatientInfoByFieldTyp(REQUIRED_FIELDS);
        logger.info("actPtientInformation: " + actPtientInformation);
        PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS, new PayorInfo());
        logger.info("actPayorInfo: " + actPayorInfo);
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, actPtientInformation, actPayorInfo);
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 2 Expected Results: EPI is saved into DB correctly");
        xifinPortalUtils.waitForPageLoaded(wait);     
        verifyPatientDemographicsLoadPageIsDisplayed();
        verifyPatientDemographicsIsSavedInPTByRequireFields(actHeader, actPtientInformation, actPayorInfo);
        orgAccn = updatePtIdToAccn(actHeader.getEpi());
    
        logger.info("*** Step 3 Action: Enter EPI at step 2");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 3 Expected Results: The Patient Demographics detail page is displayed with correct header section and at sections:" + "- Associated Patient IDs" + "- Patient Information"
                + "- Insurance Information" + "- All Accessions for this Patient" + "- Dialysis Information");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, actPtientInformation, actPayorInfo);
        verifyAssociatedPatientIDsIsEmpty();
        verifyDialysisInformationSectionAreEmpty();
        verifyAllAccessionsForThisPatientDataIsDisplayedCorrectly(actHeader);
    
        logger.info("*** Step 4 Action: Go to All Accessions for this Patient grid. Click on any Accession ID link");
        setValueInSearchSectionInput(ALL_ACCESSIONS_FOR_THIS_PATIENT_SECTION_NM);
        String accnId = clickOnAccessionIDLinkByRow(NEWEST_ROW);
    
        logger.info("*** Step 4 Expected Results: The link opens up Accession Detail with EPI is displayed correctly");
		accessionDetail = new AccessionDetail(driver);
        verifyAccessionDetailPageIsDisplayedEPICorrectly(accnId, actHeader);
    
        logger.info("*** Step 5 Action: Click [Reset] button");
        clickOnAccessionDetailPageResetBtn();
    
        logger.info("*** Step 5 Expected Results: Accession Detail load page is displayed.");
        verifyAccessionDetailLoadPageIsDisplayed();
    
        logger.info("*** Step 6 Action: Clean data");
    }

    @Test(priority = 1, description = "XPR-1782: Accn Payment Demographics - Add a new record to Associated Patient IDs table")
    public void testXPR_1782() throws Exception {
        logger.info("==== Testing - testXPR_1782 ==== ");
        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 2 Action: Input an EPI which not existing in database. Then tab out");
        String newEpi = createNewDataNotInPT(NEW_EPI);
        actHeader = new Header();
        actHeader.setEpi(newEpi);
        enterValues(patientDemographics.ptDemoSectionEPIInput(), newEpi);
    
        logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with correct header data." + "- Associated Patient IDs, All Accessions table are empty"
                + "- All fields inside Patient Information section are empty, except [country] dropdown");
        verifyAssociatedPatientIDsIsEmpty();
        verifyAllAccessionsForThisPatientTableIsEmpty();
        verifyPatientInformationSectionsIsEmpty();
          
        logger.info("*** Step 3 Action: Go to Associated Patient IDs table, click add button");
        setValueInSearchSectionInput(ASSOCIATED_PATIENT_IDS_SECTION_NM);
        clickHiddenPageObject(patientDemographics.associatedPtIDsTblAddBtn(), 0);
    
        logger.info("*** Step 3 Expected Results: [Add record] popup is displayed.");
        verifyAssociatedPatientIdsAddPopupIsDisplayed("Add Record");
    
        logger.info("*** Step 4 Action: Input Patient ID, select Source Type is Client");
        AssociatedPatientIDsTable associatedPatientIDsTable = new AssociatedPatientIDsTable();
        String randomPatientId = randomCharacter.getRandomAlphaNumericString(6);
        enterValues(patientDemographics.associatedPtIdsAddEditPopupPatientIdInput(), randomPatientId);
        associatedPatientIDsTable.setPatientId(randomPatientId);
        String sourceTypeToSelectd = SOURCE_TYPE[0].toString();
        selectDropDown(patientDemographics.associatedPtIdsAddEditPopupSourceTypeDdl(), sourceTypeToSelectd);
        associatedPatientIDsTable.setSourceType(sourceTypeToSelectd);
    
        logger.info("*** Step 4 Expected Results: [Source Id] input is enable");
        assertTrue(isElementEnabled(patientDemographics.associatedPtIdsAddEditPopupSourceIdInput(), 5, true), "        Source ID input is enable");
    
        logger.info("*** Step 5 Action: Input Source ID with client ID from pre-condition sql (1)");
        Cln cln = clientDao.getRandomClnForAssociatedPatientInPatientDemographics();
        enterValues(patientDemographics.associatedPtIdsAddEditPopupSourceIdInput(), cln.getClnAbbrev());
        associatedPatientIDsTable.setSourceId(cln.getClnAbbrev());
        associatedPatientIDsTable.setSourceName(cln.getBilAcctNm());
    
        logger.info("*** Step 5 Expected Results: Long Term Diagnosis and Ordering Physician NPI fiels are enabled." + "Diagnosis Search and Physician Search are displayed.");
        assertFalse(isElementDisabled(patientDemographics.associatedPtIdsAddEditPopupLongTermDiagInput(), "class", "field_disabled", 5), "        Long Term Diagnosis input is enabled");
        assertFalse(isElementDisabled(patientDemographics.associatedPtIdsAddEditPopupOrderingPhysNpiInput(), "class", "field_disabled", 5), "        Ordering Physician NPI input is enabled");
        assertTrue(isElementPresent(patientDemographics.associatedPtIdsAddEditPopupLongTermDiagSearchBtn(), 5), "        Long Term Diagnosis Search button is displayed");
        assertTrue(isElementPresent(patientDemographics.associatedPtIdsAddEditPopupOrderingPhysNpiSearchBtn(), 5), "        Ordering Physician NPI Search button is displayed");
    
        logger.info("*** Step 6 Action: Input valid data for remain fields. Click [Ok] button");
        DiagCd diagCd = diagnosisCodeDao.getRandomDiagCd();
        enterValues(patientDemographics.associatedPtIdsAddEditPopupLongTermDiagInput(), diagCd.getDiagCdId());
        associatedPatientIDsTable.setLongTempDiagnosis(diagCd.getDiagCdId());
        
    	for(int i = 0; i < 20; i++) {
    		Phys phys = physicianDao.getRandomPhysHasValidNpi();
    		if (phys.getNpiId() > 0) {
    			validNpi =phys.getNpiId();
    			associatedPatientIDsTable.setOrderingPhysicianNPI(String.valueOf(phys.getNpiId()));
    			associatedPatientIDsTable.setOrderingPhysicianName(phys.getPhysLname() + ", " + phys.getPhysFname());
    			break;
    		}
    	}
    	enterValues(patientDemographics.associatedPtIdsAddEditPopupOrderingPhysNpiInput(), validNpi);
        clickHiddenPageObject(patientDemographics.associatedPtIdsAddEditPopupOkBtn(), 0);
        
    
        logger.info("*** Step 6 Expected Results: A new record added in [Associated Patient IDs] table with correct data added at step 4");
        verifyAssociatedPatientIDsIsDisplayedCorrectly(associatedPatientIDsTable);
    
        logger.info("*** Step 7 Action: Input valid data to all required fields");
        
        PatientInformation actPatientInformation = setPatientInfoByFieldTyp(REQUIRED_FIELDS);
        PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS, new PayorInfo());
        
    
        logger.info("*** Step 7 Expected Results: All required field displayed data correctly");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, actPatientInformation, actPayorInfo);
    
        logger.info("*** Step 8 Action: Click [Save and Clear] button");
        clickOnSaveAndClearBtn();
        
        logger.info("*** Step 8 Expected Results: Patient Demographics load page is displayed. New records add in PT, PT_CLN_LNK table with valid data");
        verifyPatientDemographicsLoadPageIsDisplayed();
        Pt actPt = verifyNewRecordIsSavedInPtTableCorrectly(actHeader.getEpi());
        verifyNewRecordIsSavedInPtClnLnkTableCorrectly(actPt.getSeqId());
    
        logger.info("*** Step 9 Action: Input created EPI on step 2");
        enterValues(patientDemographics.ptDemoSectionEPIInput(), actPt.getEpi());
    
        logger.info("*** Step 9 Expected Results: - The Patient Demographics detail page is displayed with correct header data."
                + "- Associated Patient IDs displayed correct data"
                + "- Patient Information section displayed correct data");
        Header expHeader = getValueHeaderSection();
        assertEquals(actHeader, expHeader, "        The Patient Demographics detail page is displayed with correct header data.");
        verifyAssociatedPatientIDsIsDisplayedCorrectly(associatedPatientIDsTable);
        PatientInformation expPatientInformation = getValuePatientInfoSection(REQUIRED_FIELDS);
        assertEquals(actPatientInformation, expPatientInformation, "        Patient Information section displayed correct data.");
    
        logger.info("*** Step 10 Action: Click [Reset] button");
        clickOnResetBtn();
    
        logger.info("*** Step 10 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "XPR-1783: Accn Payment Demographics - Load Patient Demographics page with new EPI, SSN with all required fields")
    public void testXPR_1783() throws Exception {
        logger.info("==== Testing - testXPR_1783 ==== ");

        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 2 Action: Input an EPI which not exists in database in [EPI] inputField");
        actHeader = setDataPatientDemographicsSection(NEW_EPI, EMPTY, 0);
    
        logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with correct header section and empty at sections:" + "- Associated Patient IDs"
                + "- Patient Information" + "- Insurance Information" + "- All Accessions for this Patient" + "- Dialysis Information");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, null, null);
        verifyAssociatedPatientIDsIsEmpty();
        verifyPatientInformationSectionsIsEmpty();
        verifyInsuredInfoSectionIsEmpty(actHeader, REQUIRED_FIELDS, null);
        verifyAllAccessionsForThisPatientTableIsEmpty();
        verifyDialysisInformationSectionAreEmpty();
    
        logger.info("*** Step 3 Action: Click [Reset] button");
        clickOnResetBtn();
    
        logger.info("*** Step 3 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 4 Action: Input an SSN which not exists in database in [Patient SSN] inputField.");
        actHeader = setDataPatientDemographicsSection(NEW_SSN, EMPTY, 0);
    
        logger.info("*** Step 4 Expected Results: The Patient Demographics detail page is displayed with correct header section and empty at sections:" + "- Associated Patient IDs"
                + "- Patient Information" 
                + "- Insurance Information" 
                + "- All Accessions for this Patient" 
                + "- Dialysis Information");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, null, null);
        verifyAssociatedPatientIDsIsEmpty();
        verifyPatientInformationSectionsIsEmpty();
        verifyInsuredInfoSectionIsEmpty(actHeader, REQUIRED_FIELDS, null);
        verifyAllAccessionsForThisPatientTableIsEmpty();
        verifyDialysisInformationSectionAreEmpty();
    
        logger.info("*** Step 5 Action: Click [Reset] button");
        clickOnResetBtn();
    
        logger.info("*** Step 5 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 6 Action: click [Create new EPI] link");
        clickOnCreateNewEPILnk();
    
        logger.info("*** Step 6 Expected Results: [Create A New Patient EPI] pop-up is appeared.");
        assertFalse(isElementHidden(patientDemographics.createNewPtEPIPopupCreateNewEPIRad(), 5), "        [Create A New Patient EPI] pop-up is appeared.");
    
        logger.info("*** Step 7 Action: Select [Let the system generate an EPI using] option. Input a new last name in [Patient Last Name] inputField");
        clickOnLetTheSystemGenerateAnEPIUsingRad();
        actHeader = setDataPatientDemographicsSection(NEW_EPI_PATIENT_LAST_NAME, EMPTY, 0);
        String ptLastMn = actHeader.getEpi();
        clickOnCreateNewEPIPopupOkBtn();
    
        logger.info("*** Step 7 Expected Results: The Patient Demographics detail page is displayed with correct header section and empty at sections:" + "- Associated Patient IDs"
                + "- in Patient Information section, [Last Name] inputField Displays corrected Patient Last Name data" 
                + "- Insurance Information" 
                + "- All Accessions for this Patient"
                + "- Dialysis Information");
        actHeader = setNewEpiByPatientLastNm(actHeader);
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, null, null);
        verifyAssociatedPatientIDsIsEmpty();
        verifyInsuredInfoSectionIsEmpty(actHeader, NEW_EPI_PATIENT_LAST_NAME, ptLastMn);
        verifyAllAccessionsForThisPatientTableIsEmpty();
        verifyDialysisInformationSectionAreEmpty();
        verifyPtLastNameAtPatientInformationSectionIsEmpty(NEW_EPI_PATIENT_LAST_NAME, ptLastMn);
    
        logger.info("*** Step 8 Action: Click [Reset] button");
        clickOnResetBtn();
    
        logger.info("*** Step 8 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 9 Action: click [Create new EPI] link");
        clickOnCreateNewEPILnk();
    
        logger.info("*** Step 9 Expected Results: [Create A New Patient EPI] pop-up is appeared.");
        assertFalse(isElementHidden(patientDemographics.createNewPtEPIPopupCreateNewEPIRad(), 5), "        [Create A New Patient EPI] pop-up is appeared.");
    
        logger.info("*** Step 10 Action: Select [Create new EPI] option." + "Input an EPI which not exists in database in [Create new EPI] inputField");
        clickOnCreateNewEPIRad();
        actHeader = setDataPatientDemographicsSection(CREATE_NEW_EPI, EMPTY, 0);
        clickOnCreateNewEPIPopupOkBtn();
    
        logger.info("*** Step 10 Expected Results: The Patient Demographics detail page is displayed with correct header section and empty at sections:" + "- Associated Patient IDs"
                + "- Patient Information" 
                + "- Insurance Information" 
                + "- All Accessions for this Patient" 
                + "- Dialysis Information");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, null, null);
        verifyAssociatedPatientIDsIsEmpty();
        verifyPatientInformationSectionsIsEmpty();
        verifyInsuredInfoSectionIsEmpty(actHeader, REQUIRED_FIELDS, null);
        verifyAllAccessionsForThisPatientTableIsEmpty();
        verifyDialysisInformationSectionAreEmpty();
    
        logger.info("*** Step 11 Action: Input all required fileds in this page");
        PatientInformation actPtientInformation = setPatientInfoByFieldTyp(REQUIRED_FIELDS);
        PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS, new PayorInfo());
    
        logger.info("*** Step 11 Expected Results: The required fields are displayed corrected value");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, actPtientInformation, actPayorInfo);
    
        logger.info("*** Step 12 Action: Click [Save and Clear] button");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 12 Expected Results: Patient Demographics load page is displayed. Data is saved to DB correctly");
        verifyPatientDemographicsLoadPageIsDisplayed();
        verifyPatientDemographicsIsSavedInPTByRequireFields(actHeader, actPtientInformation, actPayorInfo);
    
        logger.info("*** Step 13 Action: Input the EPI is created at step 10 again");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 13 Expected Results: - The Patient Demographics detail page is displayed with corrected data");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, actPtientInformation, actPayorInfo);
    
        logger.info("*** Step 14 Action: Click [Reset] button");
        clickOnResetBtn();
    
        logger.info("*** Step 14 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "XPR-1784: Accn Payment Demographics - Delete an existed record at Associated Patient IDs table")
    public void testXPR_1784() throws Exception {
        logger.info("==== Testing - testXPR_1784 ==== ");
        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 2 Action: Input an EPI which not existing in database. Then tab out");
        String newEpi = createNewDataNotInPT(NEW_EPI);
        enterValues(patientDemographics.ptDemoSectionEPIInput(), newEpi);
    
        logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with correct header data." + "- Associated Patient IDs, All Accessions table are empty"
                + "- All fields inside Patient Information section are empty, except [country] dropdown");
        verifyAssociatedPatientIDsIsEmpty();
        verifyAllAccessionsForThisPatientTableIsEmpty();
        verifyPatientInformationSectionsIsEmpty();
    
        logger.info("*** Step 3 Action: Go to Associated Patient IDs table, click add button");
        setValueInSearchSectionInput(ASSOCIATED_PATIENT_IDS_SECTION_NM);
        clickHiddenPageObject(patientDemographics.associatedPtIDsTblAddBtn(), 0);
    
        logger.info("*** Step 3 Expected Results: [Add record] popup is displayed.");
        verifyAssociatedPatientIdsAddPopupIsDisplayed("Add Record");
    
        logger.info("*** Step 4 Action: Input Patient ID, select Source Type is Client");
        AssociatedPatientIDsTable associatedPatientIDsTable = new AssociatedPatientIDsTable();
        String randomPatientId = randomCharacter.getRandomAlphaNumericString(6);
        enterValues(patientDemographics.associatedPtIdsAddEditPopupPatientIdInput(), randomPatientId);
        associatedPatientIDsTable.setPatientId(randomPatientId);
        selectDropDown(patientDemographics.associatedPtIdsAddEditPopupSourceTypeDdl(), CLIENT);
        associatedPatientIDsTable.setSourceType(CLIENT);
    
        logger.info("*** Step 4 Expected Results: [Source Id] input is enable");
        assertTrue(isElementPresent(patientDemographics.associatedPtIdsAddEditPopupSourceIdInput(), 5), "        At Add/Edit for Associated Patient IDs popup, Source ID input is displayed");
        assertTrue(isElementEnabled(patientDemographics.associatedPtIdsAddEditPopupSourceIdInput(), 5, true), "        Source ID input is enable");
    
        logger.info("*** Step 5 Action: Input Source ID with client ID from pre-condition sql (1)");
        Cln cln = clientDao.getRandomClnForAssociatedPatientInPatientDemographics();
        enterValues(patientDemographics.associatedPtIdsAddEditPopupSourceIdInput(), cln.getClnAbbrev());
        associatedPatientIDsTable.setSourceId(cln.getClnAbbrev());
        associatedPatientIDsTable.setSourceName(cln.getBilAcctNm());
    
        logger.info("*** Step 5 Expected Results: Long Term Diagnosis and Ordering Physician NPI fiels are enabled." + "Diagnosis Search and Physician Search are displayed.");
        assertFalse(isElementDisabled(patientDemographics.associatedPtIdsAddEditPopupLongTermDiagInput(), "class", "field_disabled", 5), "        Long Term Diagnosis input is enabled");
        assertFalse(isElementDisabled(patientDemographics.associatedPtIdsAddEditPopupOrderingPhysNpiInput(), "class", "field_disabled", 5), "        Ordering Physician NPI input is enabled");
        assertTrue(isElementPresent(patientDemographics.associatedPtIdsAddEditPopupLongTermDiagSearchBtn(), 5), "        Long Term Diagnosis Search button is displayed");
        assertTrue(isElementPresent(patientDemographics.associatedPtIdsAddEditPopupOrderingPhysNpiSearchBtn(), 5), "        Ordering Physician NPI Search button is displayed");
    
        logger.info("*** Step 6 Action: Input valid data for remain fields. Click [Ok] button");
        DiagCd diagCd = diagnosisCodeDao.getRandomDiagCd();
        enterValues(patientDemographics.associatedPtIdsAddEditPopupLongTermDiagInput(), diagCd.getDiagCdId());
        associatedPatientIDsTable.setLongTempDiagnosis(diagCd.getDiagCdId());
    	for(int i = 0; i < 20; i++) {
    		Phys phys = physicianDao.getRandomPhysHasValidNpi();
    		if (phys.getNpiId() > 0) {
    			validNpi = phys.getNpiId();
    			associatedPatientIDsTable.setOrderingPhysicianNPI(String.valueOf(phys.getNpiId()));
    			associatedPatientIDsTable.setOrderingPhysicianName(phys.getPhysLname() + ", " + phys.getPhysFname());
    			break;
    		}
    	}
        enterValues(patientDemographics.associatedPtIdsAddEditPopupOrderingPhysNpiInput(), validNpi);
    
        clickHiddenPageObject(patientDemographics.associatedPtIdsAddEditPopupOkBtn(), 0);
    
        logger.info("*** Step 6 Expected Results: A new record added in [Associated Patient IDs] table with correct data added at step 4");
        verifyAssociatedPatientIDsIsDisplayedCorrectly(associatedPatientIDsTable);
    
        logger.info("*** Step 7 Action: Input valid data to all required fields");
        actHeader = new Header();
        actHeader.setEpi(newEpi);
        PatientInformation actPtientInformation = setPatientInfoByFieldTyp(REQUIRED_FIELDS);
        PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS,  new PayorInfo());
    
        logger.info("*** Step 7 Expected Results: All required field displayed data correctly");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, actPtientInformation, actPayorInfo);
    
        logger.info("*** Step 8 Action: Click [Save and Clear] button");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 8 Expected Results: Patient Demographics load page is displayed. New records add in PT, PT_CLN_LNK table with valid data");
        verifyPatientDemographicsLoadPageIsDisplayed();
        Pt actPt = verifyNewRecordIsSavedInPtTableCorrectly(actHeader.getEpi());
        List<PtClnLnk> actPtClnLnks = verifyNewRecordIsSavedInPtClnLnkTableCorrectly(actPt.getSeqId());
    
        logger.info("*** Step 9 Action: Input created EPI on step 2");
        enterValues(patientDemographics.ptDemoSectionEPIInput(), actPt.getEpi());
    
        logger.info("*** Step 9 Expected Results: - The Patient Demographics detail page is displayed with correct header data." 
                + "- Associated Patient IDs displayed correct data"
                + "- Patient Information section displayed correct data");
        Header expHeader = getValueHeaderSection();
        assertEquals(actHeader, expHeader, "        The Patient Demographics detail page is displayed with correct header data.");
        verifyAssociatedPatientIDsIsDisplayedCorrectly(associatedPatientIDsTable);
        PatientInformation expPatientInformation = getValuePatientInfoSection(REQUIRED_FIELDS);
        assertEquals(actPtientInformation, expPatientInformation, "        Patient Information section displayed correct data.");
    
        logger.info("*** Step 10 Action: Select the created row at Associated Patient IDs, click on deleted checkbox");
        clickHiddenPageObject(patientDemographics.associatedPtIDsTblDeleteChk(NEWEST_ROW), 0);
    
        logger.info("*** Step 10 Expected Results: The record is marked as Deleted");
        assertEquals(isElementDisabled(patientDemographics.associatedPtIDsTblRow(NEWEST_ROW), "class", "rowMarkedForDelete", 5), true, "        The record is marked as Deleted");
    
        logger.info("*** Step 11 Action: Click [Save and Clear] button");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 11 Expected Results: Patient Demographics load page is displayed. Data at Associated Patient IDs is deleted in database.");
        verifyPatientDemographicsLoadPageIsDisplayed();
        actPtClnLnks = patientDao.getPtClnLnkByPtSeqId(actPt.getSeqId());
        assertEquals(actPtClnLnks.size(), 0, "        Data at Associated Patient IDs is deleted in database.");
        
        logger.info("*** Step 12 Action: Input created EPI on step 2");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
        
        logger.info("*** Step 12 Expected Results: The Patient Demographics detail page is displayed with correct header section and at sections:" 
        + "- Associated Patient IDs are empty" + "- Patient Information displayed correct data");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, actPtientInformation, actPayorInfo);
        verifyAssociatedPatientIDsIsEmpty();
        PatientInformation expPatientInformationAfterInputEPIAgain = getValuePatientInfoSection(REQUIRED_FIELDS);
        assertEquals(actPtientInformation, expPatientInformationAfterInputEPIAgain, "        Patient Information section displayed correct data.");
        logger.info("*** Step 13 Action: Click [Reset] button");
        clickOnResetBtn();
    
        logger.info("*** Step 13 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "XPR-1785: Accn Payment Demographics - Create new Patient and input all patient information")
    public void testXPR_1785() throws Exception {
        logger.info("==== Testing - testXPR_1785 ====");

        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 2 Action: Input an EPI which not exists in PT table. Then tab out");
        actHeader = setDataPatientDemographicsSection(NEW_EPI, EMPTY, 0);
    
        logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with correct header section and at sections:" 
                + "- Associated Patient IDs" 
                + "- Patient Information"
                + "- Insurance Information" 
                + "- All Accessions for this Patient" 
                + "- Dialysis Information");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, null, null);
        verifyAssociatedPatientIDsIsEmpty();
        verifyInsuredInfoSectionIsEmpty(actHeader, REQUIRED_FIELDS, null);
        verifyAllAccessionsForThisPatientTableIsEmpty();
        verifyDialysisInformationSectionAreEmpty();
    
        logger.info("*** Step 3 Action: Go to [Patient Information] section. Add valid data to all fields at the section. Leave [Create bad address record] as uncheck");
        PatientInformation actPtientInformation = setPatientInfoByFieldTyp(FULL_FIELDS);
    
        logger.info("*** Step 3 Expected Results: Data display correctly");
        verifyValuesAreDisplayedCorrectly(FULL_FIELDS, null, actPtientInformation, null);
    
        logger.info("*** Step 4 Action: Input all remain required fields");
        PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS, new PayorInfo());
    
        logger.info("*** Step 4 Expected Results: Data display correctly.");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, null, null, actPayorInfo);
    
        logger.info("*** Step 5 Action: Click [Save and Clear] button");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 5 Expected Results: - Patient Demographics load page is displayed. Data is saved to DB correctly");
        verifyPatientDemographicsLoadPageIsDisplayed();
        verifyPatientInformationDataIsSavedToDBCorrectly(actHeader, actPtientInformation);
    
        logger.info("*** Step 6 Action: Input created EPI on step 2");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 6 Expected Results: The Patient Demographics detail page is displayed with correct header data.");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, null, actPayorInfo);
        verifyValuesAreDisplayedCorrectly(FULL_FIELDS, null, actPtientInformation, null);
    
        logger.info("*** Step 7 Action: Go to [Patient Information] section, Update all enable fields with valid data");
        PatientInformation actUpdatedPtientInformation = setPatientInfoByFieldTyp(FULL_FIELDS);
    
        logger.info("*** Step 7 Expected Results: Data display correctly");
        verifyValuesAreDisplayedCorrectly(FULL_FIELDS, null, actUpdatedPtientInformation, null);
    
        logger.info("*** Step 8 Action: Click [Save and Clear] button");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 8 Expected Results: Patient Demographics load page is displayed. Data is updated to DB correctly");
        verifyPatientDemographicsLoadPageIsDisplayed();
        verifyPatientInformationDataIsSavedToDBCorrectly(actHeader, actUpdatedPtientInformation);
    }

    @Test(priority = 1, description = "XPR-1786: Accn Payment Demographics - Update Patient information with create a bad address record")
    public void testXPR_1786() throws Exception {
        logger.info("==== Testing - testXPR_1786 ====");
        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 2 Action: Input an EPI which not existing in database. Then tab out");
        String newEpi = createNewDataNotInPT(NEW_EPI);
        actHeader = new Header();
        actHeader.setEpi(newEpi);
        enterValues(patientDemographics.ptDemoSectionEPIInput(), newEpi);
    
        logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with correct header data." 
                + "- Associated Patient IDs, All Accessions table are empty"
                + "- All fields inside Patient Information section are empty, except [country] dropdown");
        verifyAssociatedPatientIDsIsEmpty();
        verifyAllAccessionsForThisPatientTableIsEmpty();
        verifyPatientInformationSectionsIsEmpty();
    
        logger.info("*** Step 3 Action: Go to [Patient Information] section, Add valid data to all fields at the section. Leave [Create bad address record] as uncheck");
        PatientInformation actPatientInformation = setPatientInfoByFieldTyp(FULL_FIELDS);
    
        logger.info("*** Step 3 Expected Results: Data display correctly");
        PatientInformation expPatientInformation = getValuePatientInfoSection(FULL_FIELDS);
        assertEquals(actPatientInformation, expPatientInformation, "        Data display correctly.");
    
        logger.info("*** Step 4 Action: Click on [Create bad address record] checkbox.");
        clickHiddenPageObject(patientDemographics.ptInfoSectionCreateBadAddressRecordChk(), 0);
    
        logger.info("*** Step 4 Expected Results: - [Suspended Reason] table is displayed at [Insurance Information] section"
                + "- At the new record at [Suspended Reason] table, Date, Reason and User is disable");
        assertTrue(isElementPresent(patientDemographics.suspendedReasonTbl(), 5), "        At Insurance Information section, the [Suspended Reason] table is displayed.");
        assertTrue(isElementDisabled(patientDemographics.suspendedReasonTblDateInput(), "class", "field_disabled", 5), "        At [Suspended Reason] table, the Date input is disabled.");
        assertTrue(isElementDisabled(patientDemographics.suspendedReasonTblReasonDdl(), "class", "select2-container-disabled", 5), "        At [Suspended Reason] table, the Reason dropdownlist is disabled.");
        assertTrue(isElementDisabled(patientDemographics.suspendedReasonTblUserInput(), "class", "field_disabled", 5), "        At [Suspended Reason] table, the User input is disabled.");
    
        logger.info("*** Step 5 Action: At [Suspended Reason] table, input value for the Note input. Add valid data for Payor ID input.");
        String note = randomCharacter.getNonZeroRandomNumericString(9);
        assertTrue(isElementPresent(patientDemographics.suspendedReasonTblNoteInput(), 5), "        At [Suspended Reason] table,  the Note input is displayed.");
        enterValues(patientDemographics.suspendedReasonTblNoteInput(), note);
        PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS, new PayorInfo());
    
        logger.info("*** Step 5 Expected Results: Data of all required field displayed correctly");
        PayorInfo expPayorInfo = getValuePayorInfoSection(REQUIRED_FIELDS);
        assertEquals(actPayorInfo, expPayorInfo, "        PayorInfo is displayed correctly");
        SuspendedReasonTable actSuspendedReasonTable = verifySuspendedReasonTblIsDisplayedCorrectly(note);
    
        logger.info("*** Step 6 Action: Click [Save and Clear] button");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 6 Expected Results: - Patient Demographics load page is displayed." 
                + "- New record added in PT, PT_PYR_SUSPEND_REASON_DT table with correct data");
        verifyPatientDemographicsLoadPageIsDisplayed();
        Pt actPt = verifyNewRecordIsSavedInPtTableCorrectly(newEpi);
        verifyPtPyrSuspendReasonDtIsSavedToDBCorrectly(actHeader, actSuspendedReasonTable);
    
        logger.info("*** Step 7 Action: Reload the EPI at step 2");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 7 Expected Results: Data at [Patient Information], [Insurance Information] displayed correctly");
        expPatientInformation = getValuePatientInfoSection(FULL_FIELDS);
        assertEquals(actPatientInformation, expPatientInformation, "        Data at [Patient Information] are displayed correctly.");
        expPayorInfo = getValuePayorInfoSection(REQUIRED_FIELDS);
        assertEquals(actPayorInfo, expPayorInfo, "        PayorInfo is displayed correctly.");
    
        logger.info("*** Step 8 Action: Go to [Suspended Reason], update the added record at step 3, mark Fix checkbox as checked");
        clickHiddenPageObject(patientDemographics.suspendedReasonTblFixChk(NEWEST_ROW), 0);
    
        logger.info("*** Step 8 Expected Results: Data display correctly at the record");
        assertTrue(patientDemographics.suspendedReasonTblFixChk(NEWEST_ROW).getAttribute("value").equals("true"), "        Data display correctly at the record.");
    
        logger.info("*** Step 9 Action: Click [Save and Clear] button");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 9 Expected Results: - Patient Demographics load page is displayed."
                + "- New record added in PT, PT_PYR_SUSPEND_REASON_DT table with correct data with fix_date at PT_PYR_SUSPEND_REASON_DT equal current date");
        verifyPatientDemographicsLoadPageIsDisplayed();
        Date fixDt = patientDao.getPtPyrSuspendReasonDtByPtSeqId(actPt.getSeqId()).get(0).getFixDt();
        String actFixDt = timeStamp.convertDateToString(fixDt, DEFAULT_DATE_FORMAT);
        String curDt = timeStamp.getCurrentDate(DEFAULT_DATE_FORMAT);
        assertEquals(actFixDt, curDt, "        The FIX_DATE field at PT_PYR_SUSPEND_REASON_DT table is updated equal with current date.");
    
        logger.info("*** Step 10 Action: Reload EPI at step 2");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 10 Expected Results: - [Suspened Reason] table at [Insurance Information] is not displayed."
                + "- [Create bad address record] checkbox at [Patient Information] is enable");
        assertTrue(isElementPresent(patientDemographics.suspendedReasonGroup(), 5), "        the [Suspended Reason] group is displayed.");
        assertTrue(isElementDisabled(patientDemographics.suspendedReasonGroup(), "style", "display: none", 5), "        [Suspened Reason] table at [Insurance Information] is not displayed.");
        assertTrue(isElementPresent(patientDemographics.ptInfoSectionCreateBadAddressRecordChk(), 5), "        At Patient Information section, the create bad address record checkbox is displayed.");
        assertTrue(isElementDisabled(patientDemographics.ptInfoSectionCreateBadAddressRecordChk(), "class", "field_disabled", 5) != true, "        At [Suspended Reason] table, the User input is enabled.");
    
        logger.info("*** Step 11 Action: Click [Reset button]");
        clickOnResetBtn();
    
        logger.info("*** Step 11 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "XPR-1787: Accn Payment Demographics - Verify Dialysis Information displayed correct data")
    public void testXPR_1787() throws Exception {
        logger.info("==== Testing - testXPR_1787 ====");
        DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    
        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 2 Action: Input an existing EPI in PT_V2 and PT_DIALYSIS_HISTORY_V tables. Then tab out");
        int ptSeqId = insertANewRecordInPtPhi().getSeqId();
        Pt pt = patientDao.getPtBySeqId(ptSeqId);
        
        PtDialysisHistory ptDialysisHistory = prepareDataPtDialysisHistory(pt);
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, pt.getEpi().trim(), pt.getPtSsn());
    
        logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with correct header data.");
        Header expHeader = getValueHeaderSection();
        assertEquals(actHeader, expHeader, "        The Patient Demographics detail page is displayed with correct header data.");
    
        logger.info("*** Step 3 Action: Go to Dialysis Information section and select non-empty option in [Dialysis Type] dropdown");
        setValueInSearchSectionInput(DIALYSIS_INFORMATION_SECTION_NM);
        selectDialysisType();
    
        logger.info("*** Step 3 Expected Results:[RPM Dialysis Patient History], [Non-RPM Dialysis Patient History] section is displayed");
        verifyRpmDialysisPatientHistoryIsDisplay();
        verifyNonRpmDialysisPatientHistoryIsDisplay();
    
        logger.info("*** Step 4 Action: Input valid data for [From] and [Though] fields, click on [Get RPM History] button");
        String fromDt = timeStamp.getPreviousDate(DEFAULT_DATE_FORMAT, 3000);
        enterValues(patientDemographics.rpmDialysisFromDtInput(), fromDt);
    
        String throughDt = timeStamp.getPreviousDate(DEFAULT_DATE_FORMAT, 1);
        enterValues(patientDemographics.rpmDialysisThroughDtInput(), throughDt);
        clickOnRpmDialysisGetRPMHistoryBtn();

        logger.info("*** Step 4 Expected Results: Verify [RPM Dialysis Patient History] table displayed correct data");
        List<RPMDialysisPatientHistory> actualRPMDialysisPatientHistorys = getDataForRPMDialysisPatientHistorytbl();
        List<RPMDialysisPatientHistory> expectedRPMDialysisPatientHistorys = patientDemographicsUtil.mapToRpmDialysisPatientHistory(new Date(dateFormat.parse(fromDt).getTime()),
                new Date(dateFormat.parse(throughDt).getTime()), pt.getEpi());
        assertEquals(actualRPMDialysisPatientHistorys, expectedRPMDialysisPatientHistorys, "        Data at [RPM Dialysis Patient History] table is displayed correctly");
    
        logger.info("*** Step 5 Action: Go to [Non-RPM Dialysis Patient History] section");
        setValueInSearchSectionInput(NON_RPM_DIALYSIS_PATIENT_HISTORY_SECTION_NM);
    
        logger.info("*** Step 5 Expected Results: At [Non-RPM Dialysis Patient History] tale displayed correct data corresponding to PT_DIALYSIS_HISTORY.");
        List<NonRPMDialysisPatientHistoryTable> actualNonRPMDialysisPatientHistorys = getDataForNonRPMDialysisPatientHistorytbl();
        List<NonRPMDialysisPatientHistoryTable> expectedNonRPMDialysisPatientHistoryTables = patientDemographicsUtil.mapToNonRpmDialysisPatientHistory(pt.getEpi());
        assertEquals(actualNonRPMDialysisPatientHistorys.toString(), expectedNonRPMDialysisPatientHistoryTables.toString(), "        Data at [RPM Dialysis Patient History] table is displayed correctly");
    
        logger.info("*** Step 6 Action: Click [Reset] button");
        clickOnResetBtn();
        clickOnElement(patientDemographics.warningPopupResetBtn());
    
        logger.info("*** Step 6 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
//        cleanPtDialysisHistory(ptDialysisHistory);
    }

    @Test(priority=1, description="XPR-1788: Accn Payment Demographics - Update full data in Insurance Information section.")
    public void testXPR_1788() throws Exception{
    	logger.info("==== Testing - testXPR_1788 ====");
    	
    	logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
    	verifyPatientDemographicsLoadPageIsDisplayed();
    	
    	logger.info("*** Step 2 Action: Input an EPI which not exists in PT table then tab out.");
    	actHeader = setDataPatientDemographicsSection(NEW_EPI,EMPTY,0); 
    	
    	logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with corrected header data and empty data in all sections: "
    			+ "Associated Patient IDs, Patient Information, Insurance Information, All Accessions for this Patient, Dialysis Information.");
    	verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, null, null);
    	verifyAssociatedPatientIDsIsEmpty();
    	verifyPatientInformationSectionsIsEmpty();
    	verifyInsuranceInformationWhenLoadNewEpi(actHeader);
    	verifyAllAccessionsForThisPatientTableIsEmpty();
    	verifyDialysisInformationSectionAreEmpty();
    	
    	logger.info("*** Step 3 Action: Input all required fields in this page.");
    	PatientInformation actPatientInformation = setPatientInfoByFieldTyp(REQUIRED_FIELDS);
    	PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS, new PayorInfo());
  
    	logger.info("*** Step 3 Expected Results: The required fields are displayed corrected value.");		
    	verifyRequiredFieldsDisplayCorrected(actHeader, actPatientInformation, actPayorInfo) ;
    	
    	logger.info("*** Step 4 Action: Scroll to [Insurance Information] section and input full fields in this section.");
    	setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
    	InsuranceInformationSection actInsuranceInfoSec1 = setValuesInInsuranceInformationSection(FULL_FIELDS_WITHOUT_PAYOR_ID, actPayorInfo, EMPTY, null) ;

    	logger.info("*** Step 4 Expected Results: All fields display corrected data.");
    	InsuranceInformationSection expInsuranceInfoSec1 = getValueInsuranceInformationSection(EMPTY,false);
    	logger.info("actInsuranceInfoSec1: " + actInsuranceInfoSec1);
    	logger.info("expInsuranceInfoSec1: " + expInsuranceInfoSec1);
    	assertEquals(actInsuranceInfoSec1, expInsuranceInfoSec1,"        At [Insurance Information] section, All fields display corrected data.");
   	
    	logger.info("*** Step 5 Action: Click [Add payor] link in the Insurance Information section.");
    	setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
		clickOnElement(patientDemographics.insuranceInfoSectionAddPyrBtn());
    	
		logger.info("*** Step 5 Expected Results: A [Add Payor] pop-up is display.");
		assertEquals(patientDemographics.addPyrPopupTitleTxt().getText(), ADD_PAYOR_POPUP_TITLE, "        [Add Payor] pop-up is displayed with correct title.");
		
        logger.info("*** Step 6 Action: Input payor ID to [New Payor ID] input field in pop-up, then click [Add] button.");
        PayorInfo actPayorInfo2 = inputPayorIDToNewPayorIDInputFieldInThePopup(actInsuranceInfoSec1.getPayorTabs().get(0).getPayorInfo().getPayorId());
        clickOnElement(patientDemographics.addPyrPopupAddBtn());
        
    	logger.info("*** Step 6 Expected Results: - A new tab with name is new payorID inputed is created. "
    				  + "- In the new tab: + The [payor Info] section displays corrected data of this new payorID."
    			+" [Insured Info] section displays with data at [Patient Information ] section."
			          + "+ The [Payor Notes]  and [Employer Info] section display with empty info.");
    	verifyPayorInfoSectionAddPyrTabNameIsDisplayedCorrectly(actPayorInfo2.getPayorId(),"2");
    	PayorInfo expPayorInfo2 = getValuePayorInfoSection(REQUIRED_FIELDS);
    	assertEquals(actPayorInfo2, expPayorInfo2,"        The [payor Info] section displays corrected data of this new payorID.");
    	verifyInsuredInfoSectionIsDisplayedWithSameDataAsTheFirstTab(actHeader, REQUIRED_FIELDS,actPatientInformation);
	    verifyPayorNotesSectionIsEmpty();
	    verifyEmployerInfoSectionIsEmpty();
    	
    	logger.info("*** Step 7 Action: Update full fields in 4 sections: [Payor Info, Insured Info, Payor Notes, Employer Info] of the new tab.");
    	List<PayorTab> payorTabs = new ArrayList<>();
    	setValueInSearchSectionInput(PAYOR_INFO_SECTION_NM);
    	actPayorInfo2 = setPayorInfo(REQUIRED_FIELDS, actPayorInfo);
//    	setValuesInInsuranceInformationSection(actInsuranceInfoSec1.getClnBillingCategory());
    	PayorTab payorTab = setValuesInPayorInfoTab(FULL_FIELDS_WITHOUT_PAYOR_ID, actPayorInfo2, ADD_NEW, actPatientInformation);
    	
    	payorTabs.add(actInsuranceInfoSec1.getPayorTabs().get(0));
    	payorTabs.add(payorTab);
    	InsuranceInformationSection actInsuranceInfoSec2 = actInsuranceInfoSec1;
    	actInsuranceInfoSec2.setPayorTabs(payorTabs);
    	actInsuranceInfoSec2.getPayorTabs().get(1).getPayorInfo().setPayorPriority(2);
    	
    	logger.info("*** Step 7 Expected Results: All fileds display corrected data.");	
    	InsuranceInformationSection expInsuranceInfoSec2 = getValueInsuranceInformationSection(ADD_NEW,false);
    	logger.info("actInsuranceInfoSec2: " + actInsuranceInfoSec2);
    	logger.info("expInsuranceInfoSec2: " + expInsuranceInfoSec2);
    	assertEquals(actInsuranceInfoSec2, expInsuranceInfoSec2,"        At [Insurance Information] section, All fields display corrected data.");
    	
    	logger.info("*** Step 8 Action: Click [Save and Clear] button.");
    	clickOnSaveAndClearBtn();
    	
    	logger.info("*** Step 8 Expected Results: Patient Demographics load page is displayed.  New records added to database.");
    	verifyPatientDemographicsLoadPageIsDisplayed();
    	//pyr 1
		verifyPayorInfoUpdateCorrectedData(actHeader.getEpi(), actInsuranceInfoSec1, actInsuranceInfoSec1.getEffectiveDate(), 0, true, 1);
		verifyInsuredInfoUpdateCorrectedData(actHeader.getEpi(), actInsuranceInfoSec1, actInsuranceInfoSec1.getEffectiveDate(), 0, true, 1);
		verifyPayorNotesUpdateCorrectedData(actHeader.getEpi(), actInsuranceInfoSec1, actInsuranceInfoSec1.getEffectiveDate(), 0, true, 1);
		verifyEmployerInfoUpdateCorrectedData(actHeader.getEpi(), actInsuranceInfoSec1, actInsuranceInfoSec1.getEffectiveDate(), 0, true, 1);

    	//pyr 2
    	verifyPayorInfoUpdateCorrectedData(actHeader.getEpi(), actInsuranceInfoSec2, actInsuranceInfoSec1.getEffectiveDate(), 1,true,2);
    	verifyInsuredInfoUpdateCorrectedData(actHeader.getEpi(), actInsuranceInfoSec2,actInsuranceInfoSec1.getEffectiveDate(), 1,true,2);
    	verifyPayorNotesUpdateCorrectedData(actHeader.getEpi(), actInsuranceInfoSec2,actInsuranceInfoSec1.getEffectiveDate(), 1,true,2);
    	verifyEmployerInfoUpdateCorrectedData(actHeader.getEpi(), actInsuranceInfoSec2,actInsuranceInfoSec1.getEffectiveDate(), 1,true,2);
    	
    	logger.info("*** Step 9 Action: Reload EPI at step 2.");
    	Header actHeaderLoad = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(),0); 
    	
    	logger.info("*** Step 9 Expected Results: The Patient Demographics detail page is displayed with correct header data.");		
		Header expHeaderLoad = getValueHeaderSection();
    	assertEquals(actHeaderLoad, expHeaderLoad,"        The Patient Demographics detail page is displayed with correct header data.");

    	logger.info("*** Step 10 Action: Scroll to [Insurance Information] section.");
    	setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);

    	logger.info("*** Step 10 Expected Results: Tab1 and Tab2 display corrected data.");
        clickOnSuspendedReasonTblCellRow("2");
    	expInsuranceInfoSec1 = getValueInsuranceInformationSection(EMPTY,false);
    	assertEquals(actInsuranceInfoSec1, expInsuranceInfoSec1,"        At [Insurance Information] section, All fields display corrected data.");
		clickHiddenPageObject(patientDemographics.addPyrTab("2"), 0);
		expInsuranceInfoSec2 = getValueInsuranceInformationSection(ADD_NEW,false);
    	assertEquals(actInsuranceInfoSec2, expInsuranceInfoSec2,"        At [Insurance Information] section, All fields display corrected data.");
    	
    	logger.info("*** Step 11 Action: Click [Reset] button.");
    	clickOnResetBtn();

    	logger.info("*** Step 11 Expected Results: Patient Demographics load page is displayed.");
    	verifyPatientDemographicsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "XPR-1789: Accn Payment Demographics - Add new record to Non-RPM Dialysis Patient History table")
    public void testXPR_1789() throws Exception {
        logger.info("==== Testing - testXPR_1789 ==== ");

        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 2_Prepare Action: Create new EPI with required fileds. Update ptSeqId to exist accn");
        actHeader = setDataPatientDemographicsSection(NEW_EPI, EMPTY, 0);
        PatientInformation actPtientInformation = setPatientInfoByFieldTyp(REQUIRED_FIELDS);
        PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS, new PayorInfo());
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, actPtientInformation, actPayorInfo);
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 2_Prepare Expected Results: EPI is saved into DB correctly");
        verifyPatientDemographicsIsSavedInPTByRequireFields(actHeader, actPtientInformation, actPayorInfo);
    
        logger.info("*** Step 2 Action: Enter EPI at step 2");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with correct header section and at sections:"
                + "- Associated Patient IDs"
                + "- Patient Information"
                + "- Insurance Information"
                + "- All Accessions for this Patient"
                + "- Dialysis Information");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, actPtientInformation, actPayorInfo);
        verifyAssociatedPatientIDsIsEmpty();
        verifyDialysisInformationSectionAreEmpty();
    
        logger.info("*** Step 3 Action: Go to Dialysis Information section, select non-empty option in [Dialysis Type].");
        setValueInSearchSectionInput(DIALYSIS_INFORMATION_SECTION_NM);
        selectDialysisType();
    
        logger.info("*** Step 3 Expected Results: [RPM Dialysis Patient History], [Non-RPM Dialysis Patient History] section is displayed."
                + "- Add button at [Non-RPM Dialysis Patient History] table is enable");
        verifyRpmDialysisPatientHistoryIsDisplay();
        verifyNonRpmDialysisPatientHistoryIsDisplay();
        verifyAddNewRowBtnEnable();
    
        logger.info("*** Step 4 Action: Click [Add] button at [Non-RPM Dialysis Patient History] table, enter valid data to all enable fields");
        clickHiddenPageObject(patientDemographics.nonRpmDialysisTblAddBtn(), 0);
        NonRPMDialysisPatientHistoryTable expectedNonRPMDialysisPatientHistoryTable = setValueToNonRPMDialysisPatientHistoryTbl(ADD_NEW, null);
    
        logger.info("*** Step 4 Expected Results: Data displayed correctly");
        NonRPMDialysisPatientHistoryTable actualNonRPMDialysisPatientHistoryTable = getNonRPMDialysisPatientHistoryTable(NEWEST_ROW);
        assertEquals(actualNonRPMDialysisPatientHistoryTable, expectedNonRPMDialysisPatientHistoryTable, "        [Non-RPM Dialysis Patient History] table - Data displayed correctly");
    
        logger.info("*** Step 5 Action: Click [Save And Clear] button");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 5 Expected Results: Patient Demographics load page is displayed. A new record added to PT_DIALYSIS_HISTORY table");
        verifyPatientDemographicsLoadPageIsDisplayed();
        actualNonRPMDialysisPatientHistoryTable = patientDemographicsUtil.mapNonRPMDialysisPatientHistoryTable(expectedNonRPMDialysisPatientHistoryTable.getDos(), expectedNonRPMDialysisPatientHistoryTable.getCompositeRoutine());
        assertTrue(actualNonRPMDialysisPatientHistoryTable.equals(expectedNonRPMDialysisPatientHistoryTable), "        A new record added to PT_DIALYSIS_HISTORY table corrected");
    
        logger.info("*** Step 6 Action: Enter EPI at step 2");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 6 Expected Results: [Non-RPM Dialysis Patient History] table displayed correct data");
        verifyNonRpmDialysisPatientHistoryIsDisplay();
        expectedNonRPMDialysisPatientHistoryTable = getNonRPMDialysisPatientHistoryTable(NEWEST_ROW);
        assertTrue(actualNonRPMDialysisPatientHistoryTable.equals(expectedNonRPMDialysisPatientHistoryTable), "        [Non-RPM Dialysis Patient History] table displayed correct data");
    
        logger.info("*** Step 7 Action: Click [Reset] button");
        clickOnResetBtn();
    
        logger.info("*** Step 7 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 6 Action: Clean data");
//        cleanNewPtDialysisHistory(actualNonRPMDialysisPatientHistoryTable);
    }

    @Test(priority = 1, description = "XPR-1790: Accn Payment Demographics - Update record at Non - RPM Dialysis Patient History table.")
    public void testXPR_1790() throws Exception {
        logger.info("==== Testing - testXPR_1790 ==== ");

        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 2 Action: Input an EPI not existed in PT table. Then tab out.");
        actHeader = setDataPatientDemographicsSection(NEW_EPI, EMPTY, 0);
    
        logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with corrected header data and empty data in all sections: "
                + "Associated Patient IDs, Patient Information, Insurance Information, All Accessions for this Patient, Dialysis Information.");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, null, null);
        verifyAssociatedPatientIDsIsEmpty();
        verifyPatientInformationSectionsIsEmpty();
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
        verifyInsuranceInformationWhenLoadNewEpi(actHeader);
        verifyAllAccessionsForThisPatientTableIsEmpty();
        verifyDialysisInformationSectionAreEmpty();
    
        logger.info("*** Step 3 Action: Go to Dialysis Information section, select non-empty option in [Dialysis Type].");
        PatientInformation actPtientInformation = setPatientInfoByFieldTyp(REQUIRED_FIELDS);
        PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS, new PayorInfo());
        setValueInSearchSectionInput(DIALYSIS_INFORMATION_SECTION_NM);
        selectDialysisType();
    
        logger.info("*** Step 3 Expected Results: [RPM Dialysis Patient History], [Non-RPM Dialysis Patient History] section is displayed."
                + " - Add button at [Non-RPM Dialysis Patient History] table is enable.");
        verifyRpmDialysisPatientHistoryIsDisplay();
        verifyNonRpmDialysisPatientHistoryIsDisplay();
        verifyAddNewRowBtnEnable();
    
        logger.info("*** Step 4 Action: Click [Add] button at [Non-RPM Dialysis Patient History] table, enter valid data to all enable fields.");
        clickHiddenPageObject(patientDemographics.nonRpmDialysisTblAddBtn(), 0);
        NonRPMDialysisPatientHistoryTable actualNonRPMDialysisPatientHistoryTable = setValueToNonRPMDialysisPatientHistoryTbl(ADD_NEW, null);
    
        logger.info("*** Step 4 Expected Results: Data displayed correctly.");
        NonRPMDialysisPatientHistoryTable expectedNonRPMDialysisPatientHistoryTable = getNonRPMDialysisPatientHistoryTable(NEWEST_ROW);
        assertEquals(actualNonRPMDialysisPatientHistoryTable, expectedNonRPMDialysisPatientHistoryTable, "        [Non-RPM Dialysis Patient History] table - Data displayed correctly");
    
        logger.info("*** Step 5 Action: Click [Save and Clear] button.");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 5 Expected Results: Patient Demographics load page is displayed. A new record added to PT_DIALYSIS_HISTORY table.");
        verifyPatientDemographicsLoadPageIsDisplayed();
        actualNonRPMDialysisPatientHistoryTable = patientDemographicsUtil.mapNonRPMDialysisPatientHistoryTable(expectedNonRPMDialysisPatientHistoryTable.getDos(),
                expectedNonRPMDialysisPatientHistoryTable.getCompositeRoutine());
        assertEquals(actualNonRPMDialysisPatientHistoryTable, expectedNonRPMDialysisPatientHistoryTable, "        A new record added to PT_DIALYSIS_HISTORY table corrected");
        verifyPatientDemographicsIsSavedInPTByRequireFields(actHeader, actPtientInformation, actPayorInfo);
    
        logger.info("*** Step 6 Action: Reload EPI and SSN at step 2.");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 6 Expected Results: - [RPM Dialysis Patient History], [Non-RPM Dialysis Patient History] section is displayed."
                + "- [Non-RPM Dialysis Patient History] table displayed correct data added at step 4.");
        verifyRpmDialysisPatientHistoryIsDisplay();
        setValueInSearchSectionInput(NON_RPM_DIALYSIS_PATIENT_HISTORY_SECTION_NM);
        verifyNonRpmDialysisPatientHistoryIsDisplay();
        expectedNonRPMDialysisPatientHistoryTable = getNonRPMDialysisPatientHistoryTable(NEWEST_ROW);
        assertTrue(actualNonRPMDialysisPatientHistoryTable.equals(expectedNonRPMDialysisPatientHistoryTable), "        [Non-RPM Dialysis Patient History] table displayed correct data");
    
        logger.info("*** Step 7 Action: In [Non-RPM Dialysis Patient History ] tbl, click on the added row, update all enable fields with valid data.");
        NonRPMDialysisPatientHistoryTable actNonRPMDialysisPatientHistoryTableUpdate = setValueToNonRPMDialysisPatientHistoryTbl(UPDATE, actualNonRPMDialysisPatientHistoryTable);
    
        logger.info("*** Step 7 Expected Results: Data displayed correctly.");
        NonRPMDialysisPatientHistoryTable expNonRPMDialysisPatientHistoryTblUpdate = getNonRPMDialysisPatientHistoryTable(NEWEST_ROW);
        assertEquals(actNonRPMDialysisPatientHistoryTableUpdate, expNonRPMDialysisPatientHistoryTblUpdate, "        [Non-RPM Dialysis Patient History] table - Data displayed correctly.");
    
        logger.info("*** Step 8 Action: Click [Save and Clear] button.");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 8 Expected Results: Patient Demographics load page is displayed. The new record is updated to PT_DIALYSIS_HISTORY table.");
        verifyPatientDemographicsLoadPageIsDisplayed();
        expNonRPMDialysisPatientHistoryTblUpdate = patientDemographicsUtil.mapNonRPMDialysisPatientHistoryTable(expNonRPMDialysisPatientHistoryTblUpdate.getDos(),
                expNonRPMDialysisPatientHistoryTblUpdate.getCompositeRoutine());
        assertTrue(actNonRPMDialysisPatientHistoryTableUpdate.equals(expNonRPMDialysisPatientHistoryTblUpdate), "        A new record added to PT_DIALYSIS_HISTORY table corrected");
        verifyPatientDemographicsIsSavedInPTByRequireFields(actHeader, actPtientInformation, actPayorInfo);
    
        logger.info("*** Step 9 Action: Reload EPI and SSN at step 2.");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 9 Expected Results: - [RPM Dialysis Patient History], [Non-RPM Dialysis Patient History] section is displayed."
                + "[Non-RPM Dialysis Patient History] table displayed correct data added at step 4.");
        verifyRpmDialysisPatientHistoryIsDisplay();
        setValueInSearchSectionInput(NON_RPM_DIALYSIS_PATIENT_HISTORY_SECTION_NM);
        verifyNonRpmDialysisPatientHistoryIsDisplay();
        expNonRPMDialysisPatientHistoryTblUpdate = getNonRPMDialysisPatientHistoryTable(NEWEST_ROW);
        assertTrue(actNonRPMDialysisPatientHistoryTableUpdate.equals(expNonRPMDialysisPatientHistoryTblUpdate), "        [Non-RPM Dialysis Patient History] table displayed correct data");
    
        logger.info("*** Step 10 Action: Click [Reset] button.");
        clickOnResetBtn();
        clickOnElement(patientDemographics.warningPopupResetBtn());
    
        logger.info("*** Step 10 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    
        logger.info("*** Step 11 Action: Clean data.");
//        cleanNewPtDialysisHistory(actNonRPMDialysisPatientHistoryTableUpdate);
    }

    @Test(priority = 1, description = "XPR-1791: Accn Payment Demographics - Add new [Effective Date] in Insurance Information section")
    public void testXPR_1791() throws Exception {
        logger.info("==== Testing - testXPR_1791 ==== ");
        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
        
    	logger.info("*** Step 2 Action: Input an EPI which not exists in PT table then tab out.");
    	actHeader = setDataPatientDemographicsSection(NEW_EPI,EMPTY,0); 
    	
    	logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with corrected header data and empty data in all sections: "
    			+ "Associated Patient IDs, Patient Information, Insurance Information, All Accessions for this Patient, Dialysis Information.");
    	verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, null, null);
    	verifyAssociatedPatientIDsIsEmpty();
    	verifyPatientInformationSectionsIsEmpty();
    	verifyInsuranceInformationWhenLoadNewEpi(actHeader);
    	verifyAllAccessionsForThisPatientTableIsEmpty();
    	verifyDialysisInformationSectionAreEmpty();
    	
        logger.info("*** Step 3 Action: Input all required fields in this page.");
        PatientInformation actPtientInformation = setPatientInfoByFieldTyp(REQUIRED_FIELDS);
        PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS, new PayorInfo());

        logger.info("*** Step 3 Expected Results: The required fields are displayed corrected value.");
        verifyRequiredFieldsDisplayCorrected(actHeader, actPtientInformation, actPayorInfo);
    
        logger.info("*** Step 4 Action: Scroll to [Insurance Information] section and input full fields in this section");
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
        InsuranceInformationSection actualOldInsuranceInformationSection = setValuesInInsuranceInformationSection(FULL_FIELDS_WITHOUT_PAYOR_ID, actPayorInfo, EMPTY,null);
    
        logger.info("*** Step 4 Expected Results: All fileds display corrected data");
        InsuranceInformationSection expectedOldInsuranceInformationSection = getValueInsuranceInformationSection(EMPTY,false);
        assertEquals(actualOldInsuranceInformationSection, expectedOldInsuranceInformationSection, "        Insurance Information] section - all field display corrected data");
    
        logger.info("*** Step 5 Action: Click [Save And Clear] button");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 5 Expected Results: Patient Demographics load page is displayed. A new record added to database");
        verifyPatientDemographicsLoadPageIsDisplayed();
        verifyPayorInfoUpdateCorrectedData(actHeader.getEpi(), actualOldInsuranceInformationSection, actualOldInsuranceInformationSection.getEffectiveDate(), 0, false, 0);
        verifyInsuredInfoUpdateCorrectedData(actHeader.getEpi(), actualOldInsuranceInformationSection, actualOldInsuranceInformationSection.getEffectiveDate(), 0, false, 0);
        verifyPayorNotesUpdateCorrectedData(actHeader.getEpi(), actualOldInsuranceInformationSection, actualOldInsuranceInformationSection.getEffectiveDate(), 0, false, 0);
        verifyEmployerInfoUpdateCorrectedData(actHeader.getEpi(), actualOldInsuranceInformationSection, actualOldInsuranceInformationSection.getEffectiveDate(), 0, false, 0);
    
        logger.info("*** Step 6 Action: Reload EPI at step 2");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 6 Expected Results: The Patient Demographics detail page is displayed with correct header data");
        verifyPatientDemographicsLoadPageIsDisplayed();
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
        clickOnSuspendedReasonTblCellRow("2");
        expectedOldInsuranceInformationSection = getValueInsuranceInformationSection(EMPTY,false);
        assertEquals(actualOldInsuranceInformationSection, expectedOldInsuranceInformationSection, "        Insurance Information] section - all field display corrected data");
    
        logger.info("*** Step 7 Action: Click at [Create New Effective Date] icon in Insurance Information section");
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
        clickHiddenPageObject(patientDemographics.insuranceInfoSectionAddEffDtIcon(), 0);
    
        logger.info("*** Step 7 Expected Results: [Effective Date] inputField is appeared");
        assertTrue(isElementPresent(patientDemographics.insuranceInfoSectioEffDtInput(), 5), "        [Effective Date] inputField is appeared");
    
        logger.info("*** Step 8 Action: Input a new Date into [Effective Date] inputField and update new value for all fields in Insurance Information section");
        String newEffDt = timeStamp.getFutureDate(DEFAULT_DATE_FORMAT, 10);
        enterValues(patientDemographics.insuranceInfoSectioEffDtInput(), newEffDt);
        PayorInfo originPayorInfo = new PayorInfo(); 
        originPayorInfo.setPayorId(actPayorInfo.getPayorId());
        PayorInfo actualNewPayorInfo = setPayorInfo(FULL_FIELDS, originPayorInfo);
    
        logger.info("*** Step 8 Expected Results: all fileds in Payor Info section display corrected data");
        PayorInfo expectedNewPayorInfo = getValuePayorInfoSection(FULL_FIELDS);
        assertEquals(actualNewPayorInfo, expectedNewPayorInfo, "        all fileds in Payor Info section display corrected data");
    
        logger.info("*** Step 9 Action: Click [Save And Clear] button");
        clickOnSaveAndClearBtn();
        InsuranceInformationSection actualNewInsuranceInformationSection = actualOldInsuranceInformationSection;
        actualNewInsuranceInformationSection.getPayorTabs().get(0).setPayorInfo(expectedNewPayorInfo);
        actualNewInsuranceInformationSection.setClnBillingCategory(EMPTY);
    
        logger.info("*** Step 9 Expected Results: Patient Demographics load page is displayed. A new record added to database");
        verifyPatientDemographicsLoadPageIsDisplayed();
        verifyPayorInfoUpdateCorrectedData(actHeader.getEpi(), actualNewInsuranceInformationSection, ConvertUtil.convertStringToSQLDate(newEffDt, DEFAULT_DATE_FORMAT), 0,false,0);
    
        logger.info("*** Step 10 Action: Reload EPI at step 2");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 10 Expected Results: The Patient Demographics detail page is displayed with correct header data");
        verifyNonRpmDialysisPatientHistoryIsDisplay();
    
        logger.info("*** Step 11 Action: Scroll to [Insurance Information] section and click to [Effective Date] ddl");
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
        clickOnElement(patientDemographics.insuranceInfoSectionEffDtDdlArrow());
    
        logger.info("*** Step 11 Expected Results: 2 Effect date options are displayed corrected in [Effective Date] ddl. The [Delete Effective Date] checkbox is enabled");
        assertEquals(newEffDt, patientDemographics.insuranceInfoSectionEffDtDdlOption("1").getText(), "        New effective date option are displayed correctly");
        assertEquals(timeStamp.convertDateToString(actualOldInsuranceInformationSection.getEffectiveDate()), patientDemographics.insuranceInfoSectionEffDtDdlOption("2").getText(), "        Old effective date option are displayed correctly");
        assertTrue(isElementEnabled(patientDemographics.insuranceInfoSectionDeleteEffDtChk(), 5, true), "        The [Delete Effective Date] checkbox is enabled");
    
        logger.info("*** Step 12 Action: Select the option is created at step 2");
        clickOnElement(patientDemographics.insuranceInfoSectionEffDtDdlArrow());
        selectDropDownJQGridClickOnly(patientDemographics.insuranceInfoSectionEffDtDdl(),
        		timeStamp.convertDateToString(actualOldInsuranceInformationSection.getEffectiveDate()));
    
        logger.info("*** Step 12 Expected Results: The [Insurance Information] section display corrected data with the effect date option at step 2");
        PayorInfo oldPayorInfo = getValuePayorInfoSection(FULL_FIELDS);
        assertEquals(oldPayorInfo, expectedOldInsuranceInformationSection.getPayorTabs().get(0).getPayorInfo(), "        Payor Info section are empty");
    
        logger.info("*** Step 13 Action: Select the option is created at step 8");
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
        selectDropDown(patientDemographics.insuranceInfoSectionEffDtDdl(), newEffDt);
    
        logger.info("*** Step 13 Expected Results: The [Insurance Information] section display corrected data with the effective date option at step 8");
        verifyPayorInfoUpdateCorrectedData(actHeader.getEpi(), actualNewInsuranceInformationSection, ConvertUtil.convertStringToSQLDate(newEffDt, DEFAULT_DATE_FORMAT), 0, false, 0);
    
        logger.info("*** Step 14 Action: Check [Delete Effective Date] checkbox");
        clickHiddenPageObject(patientDemographics.insuranceInfoSectionDeleteEffDtChk(), 0);
    
        logger.info("*** Step 14 Expected Results: the [Delete Effective Date] checkbox is checked");
        assertTrue(patientDemographics.insuranceInfoSectionDeleteEffDtChk().isSelected(), "      The [Delete Effective Date] checkbox is checked");
    
        logger.info("*** Step 15 Action: Click [Save And Clear] button");
        clickOnSaveAndClearBtn();
    
        logger.info("*** Step 15 Expected Results: Patient Demographics load page is displayed. The new record is added in step 8 now is deleted");
        verifyPatientDemographicsLoadPageIsDisplayed();
        verifyNewEffDtIsDeletedInDB(actHeader.getEpi(), ConvertUtil.convertStringToSQLDate(newEffDt, DEFAULT_DATE_FORMAT), 0);
    
        logger.info("*** Step 16 Action: Reload EPI at step 2");
        actHeader = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);
    
        logger.info("*** Step 16 Expected Results: The Patient Demographics detail page is displayed with correct header data");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, actPtientInformation, actPayorInfo);
    
        logger.info("*** Step 17 Action: Scroll to [Insurance Information] section and click to [Effective Date] ddl");
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
        clickHiddenPageObject(patientDemographics.insuranceInfoSectionEffDtDdl(), 0);
    
        logger.info("*** Step 17 Expected Results: The Effect date options is created at step 8 is removed in [Effective Date] ddl");
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
        clickOnElement(patientDemographics.insuranceInfoSectionEffDtDdlArrow());
        assertEquals(timeStamp.convertDateToString(actualOldInsuranceInformationSection.getEffectiveDate()), patientDemographics.insuranceInfoSectionEffDtDdlOption("last()").getText(), "        Old effective date option are display corrected");
    
        logger.info("*** Step 18 Action: Click [Reset] button");
        clickOnResetBtn();
    
        logger.info("*** Step 18 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    }

    @Test(priority = 1, description = "XPR-1792: Accn Payment Demographics - Check [Suspended] checkbox in Insurance Information section.")
    public void testXPR_1792() throws Exception {
        logger.info("==== Testing - testXPR_1792 ==== ");

        logger.info("*** Step 1 Expected Results: User login successfull. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();

        logger.info("*** Step 2 Action: Input an EPI which not exists in PT table then tab out.");
        actHeader = setDataPatientDemographicsSection(NEW_EPI, EMPTY, 0);

        logger.info("*** Step 2 Expected Results: The Patient Demographics detail page is displayed with corrected header data and empty data in all sections:"
                + "Associated Patient IDs, Patient Information, Insurance Information, All Accessions for this Patient, Dialysis Information.");
        verifyValuesAreDisplayedCorrectly(REQUIRED_FIELDS, actHeader, null, null);
        verifyAssociatedPatientIDsIsEmpty();
        verifyPatientInformationSectionsIsEmpty();
        verifyInsuranceInformationWhenLoadNewEpi(actHeader);
        verifyAllAccessionsForThisPatientTableIsEmpty();
        verifyDialysisInformationSectionAreEmpty();

        logger.info("*** Step 3 Action: Input all required fields in this page.");
        PatientInformation actPtientInformation = setPatientInfoByFieldTyp(REQUIRED_FIELDS);
        PayorInfo actPayorInfo = setPayorInfo(REQUIRED_FIELDS, new PayorInfo());

        logger.info("*** Step 3 Expected Results: The required fields are displayed corrected value.");
        verifyRequiredFieldsDisplayCorrected(actHeader, actPtientInformation, actPayorInfo);

        logger.info("*** Step 4 Action: Check to [Suspended] checkbox in Insurance Information section.");
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
        InsuranceInformationSection actInsuranceInfoSec = new InsuranceInformationSection();
        checkInSuspendedCheckboxInsuranceInfoSection(actInsuranceInfoSec);

        logger.info("*** Step 4 Expected Results: - The checkbox is checked and disabled."
                + "- The [Suspended Reason] table is appeared."
                + "- In [Suspended Reason] table has a new row is ready to add new with [Date] colum get from [efftective Date] inputField, [User] column get from user name log in.");
        verifySuspendedCheckboxInsuranceInfoSectionIsCheckedAndDisabled();
        isElementPresent(patientDemographics.suspendedReasonGroup(), 5);
//        assertTrue(patientDemographics.suspendedReasonGroup().getAttribute("style").contains("display: block;"), "        The [Suspended Reason] table is appeared.");
        verifySuspendedReasonTblIsDisplayedCorrectly();

        logger.info("*** Step 5 Action: Input full value for the new row of [Suspended Reason] table.");
        List<SuspendedReasonTable> suspReasonTbls = new ArrayList<>();
        SuspendedReasonTable actSuspendedReasonTable = setValuesInSuspendedReasonTbl(EMPTY);
        suspReasonTbls.add(actSuspendedReasonTable);
        actInsuranceInfoSec.setSuspendedReasons(suspReasonTbls);

        logger.info("*** Step 5 Expected Results: The new row displays corrected in [Suspended Reason] table.");
    	filterSuspendedReasonTbl(actSuspendedReasonTable);	//Issue at filter feature of Reason field
        verifyTheNewRowDisplaysCorrectedInSuspendedReasonTbl();

        logger.info("*** Step 6 Action: Click [Save and Clear] button.");
        clickOnSaveAndClearBtn();

        logger.info("*** Step 6 Expected Results: Patient Demographics load page is displayed. A new record added in PT_PYR_SUSPEND_REASON_DT tbl.");
        verifyPatientDemographicsLoadPageIsDisplayed();
        SuspendedReasonTable actSuspendedReasonTableLoad = patientDemographicsUtil.mapSuspendedReasonTable(actHeader.getEpi());
        assertEquals(actSuspendedReasonTableLoad, actSuspendedReasonTable, "        A new record added in PT_PYR_SUSPEND_REASON_DT tbl.");

        logger.info("*** Step 7 Action: Reload EPI at step 2.");
        Header actHeaderLoad = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);

        logger.info("*** Step 7 Expected Results: The Patient Demographics detail page is displayed with correct header data.");
        Header expHeaderLoad = getValueHeaderSection();
        assertEquals(actHeaderLoad, expHeaderLoad, "        The Patient Demographics detail page is displayed with correct header data.");

        logger.info("*** Step 8 Action: Scroll to [Insurance Information] section.");
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);

        logger.info("*** Step 8 Expected Results: The [Suspended] checkbox is checked and disabled. The new row displays corrected in [Suspended Reason] table.");
        verifySuspendedCheckboxInsuranceInfoSectionIsCheckedAndDisabled();
        SuspendedReasonTable expSuspendedReasonTableLoad = getValuesInSuspendedReasonTbl(EMPTY);
        assertEquals(actSuspendedReasonTable, expSuspendedReasonTableLoad, "        The new row displays corrected in [Suspended Reason] table.");

        logger.info("*** Step 9 Action: Select the new row is added and update it.");
        clickHiddenPageObject(patientDemographics.suspendedReasonTblCellRow(NEWEST_ROW), 0);
        SuspendedReasonTable actSuspendedReasonTableUpdate = setValuesInSuspendedReasonTbl(UPDATE);

        logger.info("*** Step 9 Expected Results: - The updated row displays corrected in [Suspended Reason] table. The [deleted] checkbox of this row is disabled.");
        SuspendedReasonTable expSuspendedReasonTableUpdate = getValuesInSuspendedReasonTbl(UPDATE);
        assertEquals(actSuspendedReasonTableUpdate, expSuspendedReasonTableUpdate, "        The updated row displays corrected in [Suspended Reason] table.");
        verifyDeleteCheckboxIsDisable();

        logger.info("*** Step 10 Action: Click [Save and Clear] button.");
        clickOnSaveAndClearBtn();

        logger.info("*** Step 10 Expected Results: Patient Demographics load page is displayed. The updated record is updated successfully in DB.");
        verifyPatientDemographicsLoadPageIsDisplayed();
        SuspendedReasonTable actSuspendedReasonTableUpdateLoad = patientDemographicsUtil.mapSuspendedReasonTable(actHeader.getEpi());
        assertEquals(actSuspendedReasonTableUpdateLoad, expSuspendedReasonTableUpdate, "        The updated record is updated successfully in PT_PYR_SUSPEND_REASON_DT tbl.");

        logger.info("*** Step 11 Action: Reload EPI at step 2.");
        Header actHeaderLoad1 = setDataPatientDemographicsSection(EXIST_EPI, actHeader.getEpi(), 0);

        logger.info("*** Step 11 Expected Results: The Patient Demographics detail page is displayed with correct header data.");
        Header expHeaderLoad1 = getValueHeaderSection();
        assertEquals(actHeaderLoad1, expHeaderLoad1, "        The Patient Demographics detail page is displayed with correct header data.");

        logger.info("*** Step 12 Action: Scroll to [Insurance Information] section.");
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);

        logger.info("*** Step 12 Expected Results: The [Suspended] checkbox is checked and disabled. The updated row displays corrected in [Suspended Reason] table.");
        verifySuspendedCheckboxInsuranceInfoSectionIsCheckedAndDisabled();
        SuspendedReasonTable expSuspendedReasonTableUpdateLoad = getValuesInSuspendedReasonTbl(EMPTY);
        assertEquals(actSuspendedReasonTableUpdateLoad, expSuspendedReasonTableUpdateLoad, "        The updated row displays corrected in [Suspended Reason] table.");

        logger.info("*** Step 13 Action: Click [Reset] button.");
        clickOnResetBtn();

        logger.info("*** Step 13 Expected Results: Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();
    }

    /**
     * Methods ============================================================================================================
     * @throws Exception 
     * 
     */
    
    private void filterSuspendedReasonTbl(SuspendedReasonTable actSuspendedReasonTable) throws Exception{
         enterValues(patientDemographics.suspendedReasonTblDateFilterInput(), timeStamp.convertDateToString(actSuspendedReasonTable.getDate(),DEFAULT_DATE_FORMAT));
//         terraLogicUtil.enterValues(patientDemographics.suspendedReasonTblReasonFilterInput(), actSuspendedReasonTable.getReason());//Issue at filter feature of Reason field
         enterValues(patientDemographics.suspendedReasonTblNoteFilterInput(), actSuspendedReasonTable.getNotes());
         enterValues(patientDemographics.suspendedReasonTblUserFilterInput(), actSuspendedReasonTable.getUser());
    }

    private List<NonRPMDialysisPatientHistoryTable> getDataForNonRPMDialysisPatientHistorytbl() throws Exception {
        List<NonRPMDialysisPatientHistoryTable> nonRPMDialysisPatientHistoryTables = new ArrayList<>();
    
        assertTrue(isElementPresent(patientDemographics.nonRpmDialysisTblTotalRecordsTxt(), 5), "        Total Records is displayed");
        int totalRecords = Integer.parseInt(getTotalResultSearch(patientDemographics.nonRpmDialysisTblTotalRecordsTxt()));
        if (totalRecords > 0) {
            int totalPages = Integer.parseInt(patientDemographics.nonRpmDialysisTblTotalPagesLbl().getText());
            while (totalPages >= 1) {
                List<WebElement> rowsOnTable = patientDemographics.nonRpmDialysisTblDataRow();
                for (int i = 1; i <= rowsOnTable.size(); i++) {
                    nonRPMDialysisPatientHistoryTables.add(getOneRowNonRpmDialysisPatientHistoryTbl(String.valueOf(String.valueOf(i + 1))));
                }
                if (totalPages > 1) {
                    assertTrue(isElementPresent(patientDemographics.rpmDialysisTblNextPageIco(), 1), "        Next icon is displayed");
                    clickHiddenPageObject(patientDemographics.rpmDialysisTblNextPageIco(), 0);
                }
                totalPages--;
            }
        }
        return nonRPMDialysisPatientHistoryTables;
    }

    private List<RPMDialysisPatientHistory> getDataForRPMDialysisPatientHistorytbl() throws Exception {
        List<RPMDialysisPatientHistory> rpmDialysisPatientHistories = new ArrayList<>();
    
        assertTrue(isElementPresent(patientDemographics.rpmDialysisTblTotalRecordsTxt(), 5), "        Total Records is displayed");
        int totalRecords = Integer.parseInt(getTotalResultSearch(patientDemographics.rpmDialysisTblTotalRecordsTxt()));
        if (totalRecords > 0) {
            int totalPages = Integer.parseInt(patientDemographics.rpmDialysisTblTotalPagesLbl().getText());
            while (totalPages >= 1) {
                List<WebElement> rowsOnTable = patientDemographics.rpmDialysisTblDataRow();
                for (int i = 1; i <= rowsOnTable.size(); i++) {
                    rpmDialysisPatientHistories.add(getOneRowRpmDialysisPatientHistoryTbl(String.valueOf(i + 1)));
                }
    
                if (totalPages > 1) {
                    assertTrue(isElementPresent(patientDemographics.rpmDialysisTblNextPageIco(), 1), "        Next icon is displayed");
                    clickHiddenPageObject(patientDemographics.rpmDialysisTblNextPageIco(), 0);
                }
                totalPages--;
            }
        }
        
        return rpmDialysisPatientHistories;
    }

    private DialysisInformationSection getDialysisInformationSectionValue() throws Exception {
        DialysisInformationSection dialysisInformationSection = new DialysisInformationSection();
        setValueInSearchSectionInput(DIALYSIS_INFORMATION_SECTION_NM);
    
        assertTrue(isElementPresent(patientDemographics.dialysisInformationDialysisTypDdl(), 5), "        Dialysis Type dropdown are present");
        String dialysisType = getSelectedTextOnDropdown(patientDemographics.dialysisInformationDialysisTypDdl()).getText();
    
        assertTrue(isElementPresent(patientDemographics.dialysisInformationMedicationDdl(), 5), "        Medication dropdown are present");
        String medication = getSelectedTextOnDropdown(patientDemographics.dialysisInformationMedicationDdl()).getText();
    
        assertTrue(isElementPresent(patientDemographics.dialysisInformationFirstDateInput(), 5), "        First date of Dialysis are present");
        String firstDateOfDialysisAsStr = patientDemographics.dialysisInformationFirstDateInput().getAttribute("value");
    
        dialysisInformationSection.setDialysisType(dialysisType);
        dialysisInformationSection.setMedication(medication);
        dialysisInformationSection.setFirstDateOfDialysis(firstDateOfDialysisAsStr.trim().equals(EMPTY) == true ? null : ConvertUtil.convertStringToSQLDate(firstDateOfDialysisAsStr, DEFAULT_DATE_FORMAT));
    
        return dialysisInformationSection;
    }

    private NonRPMDialysisPatientHistoryTable getNonRPMDialysisPatientHistoryTable(String rowNum) throws Exception {
        NonRPMDialysisPatientHistoryTable nonRPMDialysisPatientHistoryTable = new NonRPMDialysisPatientHistoryTable();
    
        nonRPMDialysisPatientHistoryTable.setDos(ConvertUtil.convertStringToSQLDate(patientDemographics.nonRpmDialysisTblDosTxt(rowNum).getAttribute("title"), DEFAULT_DATE_FORMAT));
        nonRPMDialysisPatientHistoryTable.setCompositeRoutine(patientDemographics.nonRpmDialysisTblCompositeRoutineTxt(rowNum).getAttribute("title"));
        nonRPMDialysisPatientHistoryTable.setDescription(patientDemographics.nonRpmDialysisTblDescriptionTxt(rowNum).getAttribute("title").toUpperCase());
    
        return nonRPMDialysisPatientHistoryTable;
    }

    private NonRPMDialysisPatientHistoryTable getOneRowNonRpmDialysisPatientHistoryTbl(String row) throws Exception {
        NonRPMDialysisPatientHistoryTable nonRPMDialysisPatientHistoryTable = new NonRPMDialysisPatientHistoryTable();
        DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    
        assertTrue(isElementPresent(patientDemographics.nonRpmDialysisTblDosTxt(row), 5), "        DOS at Non RPM Dialysis Patient History is present");
        String dosAsString = patientDemographics.nonRpmDialysisTblDosTxt(row).getText();
        patientDemographics.nonRpmDialysisTblDosTxt(row).getText();
        nonRPMDialysisPatientHistoryTable.setDos(dosAsString.trim().equals(EMPTY) == true ? null : new Date(dateFormat.parse(dosAsString).getTime()));
    
        assertTrue(isElementPresent(patientDemographics.nonRpmDialysisTblCompositeRoutineTxt(row), 5), "        Composite/Routine at Non RPM Dialysis Patient History is present");
        nonRPMDialysisPatientHistoryTable.setCompositeRoutine(patientDemographics.nonRpmDialysisTblCompositeRoutineTxt(row).getText());
        assertTrue(isElementPresent(patientDemographics.nonRpmDialysisTblDescriptionTxt(row), 5), "        Description at Non RPM Dialysis Patient History is present");
        nonRPMDialysisPatientHistoryTable.setDescription(patientDemographics.nonRpmDialysisTblDescriptionTxt(row).getText());
    
        assertTrue(isElementPresent(patientDemographics.nonRpmDialysisTblDeletedChk(row), 5), "        Deleted checkbox at Non RPM Dialysis Patient History is present");
        nonRPMDialysisPatientHistoryTable.setDeleted(patientDemographics.nonRpmDialysisTblDeletedChk(row).isSelected());
    
        return nonRPMDialysisPatientHistoryTable;
    }

    private RPMDialysisPatientHistory getOneRowRpmDialysisPatientHistoryTbl(String row) throws Exception {
        RPMDialysisPatientHistory rpmDialysisPatientHistory = new RPMDialysisPatientHistory();
        DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    
        assertTrue(isElementPresent(patientDemographics.rpmDialysisTblAccessionIdTxt(row), 5), "        Accession ID at RPM Dialysis Patient History is present");
        rpmDialysisPatientHistory.setAccessionId(patientDemographics.rpmDialysisTblAccessionIdTxt(row).getText());
        assertTrue(isElementPresent(patientDemographics.rpmDialysisTblDosTxt(row), 5), "        DOS at RPM Dialysis Patient History is present");
        String dosAsString = patientDemographics.rpmDialysisTblDosTxt(row).getText();
        rpmDialysisPatientHistory.setDos(dosAsString.trim().equals(EMPTY) == true ? null : new Date(dateFormat.parse(dosAsString).getTime()));
        rpmDialysisPatientHistory.setCompositeRoutine(patientDemographics.rpmDialysisTblCompositeRoutineTxt(row).getText());
        rpmDialysisPatientHistory.setDescription(patientDemographics.rpmDialysisTblDescriptionTxt(row).getText());
        rpmDialysisPatientHistory.setModifiers(patientDemographics.rpmDialysisTblModifiersTxt(row).getText().trim());
    
        return rpmDialysisPatientHistory;
    }

    private List<AllAccessionsForThisPatientTable> getValueAtAllAccessionsForThisPatientTable() throws Exception {
        List<AllAccessionsForThisPatientTable> allAccessionsForThisPatientTables = new ArrayList<>();
        AllAccessionsForThisPatientTable allAccessionsForThisPatientTable = new AllAccessionsForThisPatientTable();
    
        assertTrue(isElementPresent(patientDemographics.allAccessionsTblTotalPagesTxt(), 5), "        All Accessions For This Patient Table, total page should be shown.");
        int totalRecords = Integer.parseInt(getTotalResultSearch(patientDemographics.allAccessionsTblTotalRecordsTxt()));
        if (totalRecords > 0) {
            int totalPages = Integer.parseInt(patientDemographics.allAccessionsTblTotalPagesTxt().getText());
            while (totalPages >= 1) {
                List<WebElement> totalRows = patientDemographics.allAccessionsTblAllRows();
                for (int row = 1; row <= totalRows.size(); row++) {
                    assertTrue(isElementPresent(patientDemographics.allAccessionsTblDosTxt(String.valueOf(row + 1)), 1), "        All Accessions For This Patient Table, Dos should be shown.");
                    allAccessionsForThisPatientTable.setDos(ConvertUtil.convertStringToSQLDate(patientDemographics.allAccessionsTblDosTxt(String.valueOf(row + 1)).getText(), DEFAULT_DATE_FORMAT));
                    allAccessionsForThisPatientTable.setAccessionId(patientDemographics.allAccessionsTblAccnIdLnk(String.valueOf(row + 1)).getText());
                    allAccessionsForThisPatientTable.setAccessionStatus(patientDemographics.allAccessionsTblAccessionStatusTxt(String.valueOf(row + 1)).getText());
                    allAccessionsForThisPatientTable.setOrderingPhysician(patientDemographics.allAccessionsTblOrderingPhysicianTxt(String.valueOf(row + 1)).getText().trim());
                    allAccessionsForThisPatientTable.setClientId(patientDemographics.allAccessionsTblclientIdTxt(String.valueOf(row + 1)).getText());
                    allAccessionsForThisPatientTable.setPrimaryPayor(patientDemographics.allAccessionsTblPrimaryPayorTxt(String.valueOf(row + 1)).getText().trim());
                    allAccessionsForThisPatientTable.setPaid(Double.parseDouble(patientDemographics.allAccessionsTblPaidAmountTxt(String.valueOf(row + 1)).getText()));
                    allAccessionsForThisPatientTable.setAdj(Double.parseDouble(patientDemographics.allAccessionsTblAdjAmountTxt(String.valueOf(row + 1)).getText()));
                    allAccessionsForThisPatientTable.setBalanceDue(Double.parseDouble(patientDemographics.allAccessionsTblBalanceDueTxt(String.valueOf(row + 1)).getText()));
                    allAccessionsForThisPatientTable.setStatementStatus(patientDemographics.allAccessionsTblStatementStatusTxt(String.valueOf(row + 1)).getText().trim());
                    allAccessionsForThisPatientTables.add(allAccessionsForThisPatientTable);
                }
                if (totalPages > 1) {
                    assertTrue(isElementPresent(patientDemographics.allAccessionsTblNextPageIco(), 1), "        Next icon is displayed");
                    clickHiddenPageObject(patientDemographics.allAccessionsTblNextPageIco(), 0);
                }
                totalPages--;
            }
        }
    
        return allAccessionsForThisPatientTables;
    }

    private Header getValueHeaderSection() throws StaleElementReferenceException, InterruptedException {
        Header header = new Header();
        assertTrue(isElementPresent(patientDemographics.headerSSNInput(), 10), "        At the header, SSNInput should be shown.");
        int ssn = Integer.parseInt((patientDemographics.headerSSNInput().getAttribute("value").isEmpty() ? "0" : patientDemographics.headerSSNInput().getAttribute("value").replace("-", EMPTY)));
        header.setEpi(patientDemographics.headerEpiTxt().getAttribute("value").trim());
        header.setPatientSSN(ssn);
    
        return header;
    }

    private InsuranceInformationSection getValueInsuranceInformationSection(String type, boolean reloadFlag) throws Exception {
        InsuranceInformationSection insuranceInformationSection = new InsuranceInformationSection();
    
        // Insurance Information common fields
        setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
        insuranceInformationSection.setEffectiveDate(ConvertUtil.convertStringToSQLDate(patientDemographics.insuranceInfoSectionEffDtDdl().getText(), DEFAULT_DATE_FORMAT));
//        insuranceInformationSection.setClnBillingCategory(terraLogicUtil.getCurrentSelectTextInJQGridDropdown(patientDemographics.insuranceInfoSectionclnBillCategoryDdl()));
        insuranceInformationSection.setDeleteEffectiveDate(patientDemographics.insuranceInfoSectionDeleteEffDtChk().isSelected());
        insuranceInformationSection.setSuspended(patientDemographics.insuranceInfoSectionSuspendedChk().isSelected());
    
        // Suspended Reason tbl
        List<SuspendedReasonTable> suspendedReasonTables = new ArrayList<>();
        if (insuranceInformationSection.isSuspended()) {
            SuspendedReasonTable suspendedReasonTable = new SuspendedReasonTable();
            if(!reloadFlag){
	            suspendedReasonTable.setDate(ConvertUtil.convertStringToSQLDate(patientDemographics.suspendedReasonTblDateInput().getAttribute("value"), DEFAULT_DATE_FORMAT));
	            suspendedReasonTable.setReason(patientDemographics.suspendedReasonTblReasonDdl().getText());
	            suspendedReasonTable.setNotes(patientDemographics.suspendedReasonTblNoteInput().getAttribute("value"));
	            suspendedReasonTable.setUser(patientDemographics.suspendedReasonTblUserInput().getAttribute("value"));
	            suspendedReasonTable.setFix(patientDemographics.suspendedReasonTblFixChk(NEWEST_ROW).isSelected());
	            suspendedReasonTable.setDeleted(patientDemographics.suspendedReasonTblDeleteChk(NEWEST_ROW).isSelected());
            }else{
            	suspendedReasonTable.setDate(ConvertUtil.convertStringToSQLDate(patientDemographics.suspendedReasonTblDateTxt(NEWEST_ROW).getAttribute("value"), DEFAULT_DATE_FORMAT));
	            suspendedReasonTable.setReason(patientDemographics.suspendedReasonTblReasonTxt(NEWEST_ROW).getText());
	            suspendedReasonTable.setNotes(patientDemographics.suspendedReasonTblNoteTxt(NEWEST_ROW).getAttribute("value"));
	            suspendedReasonTable.setUser(patientDemographics.suspendedReasonTblUserTxt(NEWEST_ROW).getAttribute("value"));
	            suspendedReasonTable.setFix(patientDemographics.suspendedReasonTblFixChk(NEWEST_ROW).isSelected());
	            suspendedReasonTable.setDeleted(patientDemographics.suspendedReasonTblDeleteChk(NEWEST_ROW).isSelected());
            }
            suspendedReasonTables.add(suspendedReasonTable);
        }
        insuranceInformationSection.setSuspendedReasons(suspendedReasonTables);
    
        // add data from list tabs
        List<PayorTab> payorTabs = new ArrayList<>();
        for (int tabNum = 1; tabNum <= patientDemographics.listPyrTabs().size(); tabNum++) {
            PayorTab payorTab = new PayorTab();
            setValueInSearchSectionInput(INSURANCE_INFORMATION_SECTION_NM);
            // select tab to get value
            assertTrue(isElementPresent(patientDemographics.addPyrTab(String.valueOf(tabNum)), 5), "        Tab_" + tabNum + " is displayed");
            clickOnElement(patientDemographics.addPyrTab(String.valueOf(tabNum)));
    
            // payor info section
            setValueInSearchSectionInput(PAYOR_INFO_SECTION_NM);
            PayorInfo payorInfo = getValuePayorInfoSection(FULL_FIELDS);
            payorTab.setPayorInfo(payorInfo);
    
            // Insured info section
            setValueInSearchSectionInput(INSURED_INFO_SECTION_NM);
            InsuredInfo insuredInfo = getValueInsuredInfoSection();
            payorTab.setInsuredInfo(insuredInfo);
    
            // payor notes section
            setValueInSearchSectionInput(PAYOR_NOTES_SECTION_NM);
            PayorNotes payorNotes = getValuesInPayorNotesSection();
            payorTab.setPayorNotes(payorNotes);
    
            // Employer info section
            setValueInSearchSectionInput(EMPLOYER_INFO_SECTION_NM);
            EmployerInfo employerInfo = getValuesInEmployerInfoSection();
            payorTab.setEmployerInfo(employerInfo);
            payorTabs.add(payorTab);
        }
        insuranceInformationSection.setPayorTabs(payorTabs);
    
        return insuranceInformationSection;
    }

    private InsuredInfo getValueInsuredInfoSection() throws Exception {
        InsuredInfo insuredInfo = new InsuredInfo();
        setValueInSearchSectionInput(INSURED_INFO_SECTION_NM);
        assertTrue(isElementPresent(patientDemographics.insuredInfoSectionRelationshipDdl(), 1), "        Relation ship DDl is displayed");
        assertTrue(isElementPresent(patientDemographics.insuredInfoSectionFirstnameInput(), 1), "        FirstnameInput is displayed");
  
        insuredInfo.setRelationship(patientDemographics.insuredInfoSectionRelationshipDdl().getText());
        insuredInfo.setFirstName(patientDemographics.insuredInfoSectionFirstnameInput().getAttribute("value"));
        insuredInfo.setLastName(patientDemographics.insuredInfoSectionLastnameInput().getAttribute("value"));
        insuredInfo.setDateOfBirth(ConvertUtil.convertStringToSQLDate(patientDemographics.insuredInfoSectionDateOfBirthInput().getAttribute("value"), DEFAULT_DATE_FORMAT));
        insuredInfo.setGender(patientDemographics.insuredInfoSectionGenderDdl().getText());
        insuredInfo.setSsn(patientDemographics.insuredInfoSectionSsnInput().getAttribute("value").replace("-", EMPTY));
        insuredInfo.setAddress1(patientDemographics.insuredInfoSectionAddress1Input().getAttribute("value"));
        insuredInfo.setAddress2(patientDemographics.insuredInfoSectionAddress2Input().getAttribute("value"));
        insuredInfo.setPostalCode(patientDemographics.insuredInfoSectionPostalCodeInput().getAttribute("value"));
        insuredInfo.setCity(patientDemographics.insuredInfoSectionCityInput().getAttribute("value").trim());
        insuredInfo.setState(patientDemographics.insuredInfoSectionStateDdl().getText().trim());
        insuredInfo.setCountry(patientDemographics.insuredInfoSectionCountryDdl().getText());
        insuredInfo.setHomePhone(StringUtils.replaceChars(patientDemographics.insuredInfoSectionHomePhoneInput().getAttribute("value"),"()- ", null));
        insuredInfo.setWorkPhone(StringUtils.replaceChars(patientDemographics.insuredInfoSectionWorkPhoneInput().getAttribute("value"),"()- ", null));
    
        return insuredInfo;
    }

    private PatientInformation getValuePatientInfoSection(String type) throws Exception {
        PatientInformation patientInformation = new PatientInformation();
    
        setValueInSearchSectionInput(PATIENT_INFORMATION_SECTION_NM);
        switch (type) {
            case REQUIRED_FIELDS:
                assertTrue(isElementPresent(patientDemographics.ptInfoSectionPostalCodeInput(), 1), "        Last name input is displayed");
                assertTrue(isElementPresent(patientDemographics.ptInfoSectionCityInput(), 1), "        First name input is displayed");
                break;
    
            case FULL_FIELDS:
                assertTrue(isElementPresent(patientDemographics.ptInfoSectionLtNameInput(), 1), "        Last name input is displayed");
                assertTrue(isElementPresent(patientDemographics.ptInfoSectionFtNameInput(), 1), "        First name input is displayed");
    
                patientInformation.setLastName(patientDemographics.ptInfoSectionLtNameInput().getAttribute("value"));
                patientInformation.setFirstName(patientDemographics.ptInfoSectionFtNameInput().getAttribute("value"));
                patientInformation.setDateOfBirth(ConvertUtil.convertStringToSQLDate(patientDemographics.ptInfoSectionDOBInput().getAttribute("value"), DEFAULT_DATE_FORMAT));
                patientInformation.setGender(getSelectedTextOnDropdown(patientDemographics.ptInfoSectionGenderDdl()).getText());
                patientInformation.setMaritalStatus(getSelectedTextOnDropdown(patientDemographics.ptInfoSectionMaritalStatusDdl()).getText());
                patientInformation.setNotes(patientDemographics.ptInfoSectionCmtInput().getAttribute("value"));
                patientInformation.setDosOfMostMSPFrom(ConvertUtil.convertStringToSQLDate(patientDemographics.ptInfoSectionDOSFormInput().getAttribute("value"), DEFAULT_DATE_FORMAT));
                patientInformation.setAddress1(patientDemographics.ptInfoSectionAddr1Input().getAttribute("value"));
                patientInformation.setAddress2(patientDemographics.ptInfoSectionAddr2Input().getAttribute("value"));
                patientInformation.setHomePhone(StringUtils.replaceChars(patientDemographics.ptInfoSectionHomePhnInput().getAttribute("value"),"()- ", null));
                patientInformation.setWorkPhone(StringUtils.replaceChars(patientDemographics.ptInfoSectionWorkPhnInput().getAttribute("value"),"()- ", null));
                patientInformation.setEmail(patientDemographics.ptInfoSectionEmailInput().getAttribute("value"));
                break;
        }
    
        patientInformation.setPostalCode(patientDemographics.ptInfoSectionPostalCodeInput().getAttribute("value"));
        patientInformation.setCity(patientDemographics.ptInfoSectionCityInput().getAttribute("value").trim());
        patientInformation.setState(patientDemographics.ptInfoSectionStateDdl().getText().trim());
        patientInformation.setCountry(getSelectedTextOnDropdown(patientDemographics.ptInfoSectionCntryDdl()).getText());
    
        return patientInformation;
    }

    private PayorInfo getValuePayorInfoSection(String type) throws Exception {
        PayorInfo payorInfo = new PayorInfo();
    
        setValueInSearchSectionInput(PAYOR_INFO_SECTION_NM);
        switch (type) {
            case REQUIRED_FIELDS:
                payorInfo.setPayorPriority(Integer.parseInt(patientDemographics.pyrInfoSectionPyrPrioInput().getAttribute("value")));
                payorInfo.setPayorId(patientDemographics.pyrInfoSectionPayoridInput().getAttribute("value"));
                payorInfo.setPayorName(patientDemographics.pyrInfoSectionPayornameTxt().getText());
                payorInfo.setGroupName(patientDemographics.pyrInfoSectionGroupNameInput().getAttribute("value"));
                break;
    
            case FULL_FIELDS:
                payorInfo.setPayorPriority(Integer.parseInt(patientDemographics.pyrInfoSectionPyrPrioInput().getAttribute("value")));
                payorInfo.setPayorId(patientDemographics.pyrInfoSectionPayoridInput().getAttribute("value").trim());
                payorInfo.setPayorName(patientDemographics.pyrInfoSectionPayornameTxt().getText());
                payorInfo.setSubscriberID(patientDemographics.pyrInfoSectionSubscriberIdInput().getAttribute("value"));
                payorInfo.setGroupName(patientDemographics.pyrInfoSectionGroupNameInput().getAttribute("value"));
                payorInfo.setGroupId(patientDemographics.pyrInfoSectionGroupIdInput().getAttribute("value"));
                payorInfo.setPlanId(patientDemographics.pyrInfoSectionPlanIdInput().getAttribute("value"));
                payorInfo.setCaseId(patientDemographics.pyrInfoSectionCaseIdInput().getAttribute("value"));
                break;

           default:
        	   break;
        }
    
        return payorInfo;
    }

    private EmployerInfo getValuesInEmployerInfoSection() throws Exception {
        EmployerInfo employerInfo = new EmployerInfo();
        setValueInSearchSectionInput(EMPLOYER_INFO_SECTION_NM);
        employerInfo.setEmployerName(patientDemographics.employerInfoSectionEmployerNameInput().getAttribute("value"));
        employerInfo.setEmployerStatus(patientDemographics.employerInfoSectionEmploymentStatusDdl().getText());
        employerInfo.setAddress1(patientDemographics.employerInfoSectionAddress1Input().getAttribute("value"));
        employerInfo.setAddress2(patientDemographics.employerInfoSectionAddress2Input().getAttribute("value"));
        employerInfo.setPostalCode(patientDemographics.employerInfoSectionPostalCodeInput().getAttribute("value"));
        employerInfo.setState(patientDemographics.employerInfoSectionStateDdl().getText().trim());
        employerInfo.setCity(patientDemographics.employerInfoSectionCityInput().getAttribute("value").trim());
        employerInfo.setCountry(patientDemographics.employerInfoSectionCountryDdl().getText());
        employerInfo.setWorkPhone(StringUtils.replaceChars(patientDemographics.employerInfoSectionWorkPhoneInput().getAttribute("value"),"()- ", null));
        employerInfo.setFax(StringUtils.replaceChars(patientDemographics.employerInfoSectionFaxInput().getAttribute("value"),"()- ", null));

        return employerInfo;
    }

    private PayorNotes getValuesInPayorNotesSection() throws Exception {
        PayorNotes payorNotes = new PayorNotes();
        setValueInSearchSectionInput(PAYOR_NOTES_SECTION_NM);
        payorNotes.setClaimNotes(patientDemographics.payorNotesSectionClaimNotesInput().getAttribute("value"));
        payorNotes.setInteralNotes(patientDemographics.payorNotesSectionInternalNotesInput().getAttribute("value"));
        payorNotes.setOrtherInfo1(patientDemographics.payorNotesSectionOtherInfo1Input().getAttribute("value"));
        payorNotes.setOrtherInfo2(patientDemographics.payorNotesSectionOtherInfo2Input().getAttribute("value"));
        payorNotes.setOrtherInfo3(patientDemographics.payorNotesSectionOtherInfo3Input().getAttribute("value"));
    
        return payorNotes;
    }

    private SuspendedReasonTable getValuesInSuspendedReasonTbl(String type) throws Exception {
        SuspendedReasonTable suspReasonTbl = new SuspendedReasonTable();
        DateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String dos = EMPTY;
        String reason = EMPTY;
        String note = EMPTY;
        String user = EMPTY;
        
        if (type.equals(UPDATE)) {
            dos = patientDemographics.suspendedReasonTblDateInput().getAttribute("value");
            reason = patientDemographics.suspendedReasonTblReasonDdl().getText();
            note = patientDemographics.suspendedReasonTblNoteInput().getAttribute("value");
            user = patientDemographics.suspendedReasonTblUserInput().getAttribute("value");
        } else {
            reason = patientDemographics.suspendedReasonTblReasonTxt(NEWEST_ROW).getText();
            dos = patientDemographics.suspendedReasonTblDateTxt(NEWEST_ROW).getText();
            note = patientDemographics.suspendedReasonTblNoteTxt(NEWEST_ROW).getText();
            user = patientDemographics.suspendedReasonTblUserTxt(NEWEST_ROW).getText();
        }
        boolean fix = patientDemographics.suspendedReasonTblFixChk(NEWEST_ROW).isSelected();
    
        suspReasonTbl.setDate(new Date(formatter.parse(dos).getTime()));
        suspReasonTbl.setReason(reason);
        suspReasonTbl.setNotes(note);
        suspReasonTbl.setUser(user);
        suspReasonTbl.setFix(fix);
        
        return suspReasonTbl;
    }
    
    private PtPhi insertANewRecordInPtPhi(){
    	PtPhi ptPhi = new PtPhi();
    	try {
//    		int ptSeqId = patientDao.getRandomPtSeqIdNotExistInPtPhiFromPtDialysisHistoryV();
//    		if(ptSeqId == 0) {
//    			ptSeqId = patientDao.getRandomPtSeqIdNotExistInPtPhiFromPt2();
//    		}
    		int ptSeqId = patientDao.getRandomPt().getSeqId();
    		ptPhi.setSeqId(ptSeqId);
    		ptPhi.setPtSsn(Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(3)));
    		ptPhi.setPtFNm(randomCharacter.getRandomAlphaString(6));
    		ptPhi.setPtLNm(randomCharacter.getRandomAlphaString(6));
    		ptPhi.setPtDob(ConvertUtil.convertStringToSQLDate(timeStamp.getCurrentDate(DEFAULT_DATE_FORMAT),DEFAULT_DATE_FORMAT));
    		ptPhi.setPtAddr1(randomCharacter.getRandomAlphaString(6));
    		ptPhi.setResultCode(ErrorCodeMap.NEW_RECORD);
    		patientDao.setPtPhi(ptPhi);
    	} catch(Exception exception) {
    		logger.info(exception.getMessage());
    	}
	    return ptPhi;
    }

    private Header setDataPatientDemographicsSection(String typ, String epi, int ssn) throws Exception {
        Header header = new Header();

        switch (typ) {
            case NEW_EPI:
                String newEpi = createNewDataNotInPT(NEW_EPI);
                enterValues(patientDemographics.ptDemoSectionEPIInput(), newEpi);

                header.setEpi(newEpi);
                break;
            case NEW_SSN:
                String newSSN = createNewDataNotInPT(NEW_SSN);
                enterValues(patientDemographics.ptDemoSectionSSNInput(), newSSN);

                header.setEpi(newSSN);
                header.setPatientSSN(Integer.parseInt(newSSN));
                break;
            case EXIST_EPI:
                enterValues(patientDemographics.ptDemoSectionEPIInput(), epi);

                header.setEpi(epi);
                header.setPatientSSN(ssn);
                break;
            case EXIST_SSN:
                enterValues(patientDemographics.ptDemoSectionSSNInput(), ssn);

                header.setEpi(epi);
                header.setPatientSSN(ssn);
                break;
            case NEW_EPI_PATIENT_LAST_NAME:
                String newpt = createNewDataNotInPT(NEW_EPI);
                enterValues(patientDemographics.createNewPtEPIPopupPtLastNmInput(), newpt);
                header.setEpi(newpt);
                break;
            case CREATE_NEW_EPI:
                newEpi = createNewDataNotInPT(NEW_EPI);
                enterValues(patientDemographics.createNewPtEPIPopupCreateNewEPIInput(), newEpi);

                header.setEpi(newEpi);
                break;
        }

        return header;
    }

    private Header setNewEpiByPatientLastNm(Header actHeader) throws Exception {
        assertTrue(isElementPresent(patientDemographics.headerSSNInput(), 5), "        Page title txt is displayed");
        actHeader.setEpi(patientDemographics.headerEpiTxt().getAttribute("value"));
    
        return actHeader;
    }

    private PatientInformation setPatientInfoByFieldTyp(String type) throws Exception {
        PatientInformation patientInfo = new PatientInformation();
        Zip zip = zipDao.getDataWithLengthZipIdFromZIP();
        State state = rpmDao.getStateByStateId(testDb, zip.getStId()).get(0);
        Country county = countryDao.getCountryByAbbrv(USA);
        String lstNm = randomCharacter.getRandomAlphaString(6);
        String fstNm = randomCharacter.getRandomAlphaString(6);
        String dob = timeStamp.getPreviousDate(DEFAULT_DATE_FORMAT, 15);
        String gender = genderDao.getGenderType().getDescr();
        MaritalStatusTyp maritalStatusTyp = maritalStatusDao.getMaritalStatusTyp();
        String maritalStatus = maritalStatusTyp.getAbbrev() + " - " + maritalStatusTyp.getDescr();
        String cmt = randomCharacter.getRandomAlphaNumericString(10);
        String dos = timeStamp.getPreviousDate(DEFAULT_DATE_FORMAT, 10);
        String addr1 = randomCharacter.getRandomAlphaString(6);
        String addr2 = randomCharacter.getRandomAlphaString(6);
        String hmPhone = AREA_CODE_USA + randomCharacter.getNonZeroRandomNumericString(6);
        String wkPhone = AREA_CODE_USA + randomCharacter.getNonZeroRandomNumericString(6);
        String email = randomCharacter.getRandomAlphaNumericString(5) + SUFFIX_EMAIL;
    
        setValueInSearchSectionInput(PATIENT_INFORMATION_SECTION_NM);
        switch (type) {
            case REQUIRED_FIELDS:
                enterValues(patientDemographics.ptInfoSectionPostalCodeInput(), zip.getZipId());
                break;
                
            case FULL_FIELDS:
                enterValues(patientDemographics.ptInfoSectionLtNameInput(), lstNm);
                enterValues(patientDemographics.ptInfoSectionFtNameInput(), fstNm);
                enterValues(patientDemographics.ptInfoSectionDOBInput(), dob);
                selectDropDownJQGridClickOnly(patientDemographics.ptInfoSectionGenderS2(), gender);
                selectDropDownJQGridClickOnly(patientDemographics.ptInfoSectionMaritalStatusS2(), maritalStatus);
                enterValues(patientDemographics.ptInfoSectionCmtInput(), cmt);
                enterValues(patientDemographics.ptInfoSectionDOSFormInput(), dos);
                enterValues(patientDemographics.ptInfoSectionAddr1Input(), addr1);
                enterValues(patientDemographics.ptInfoSectionAddr2Input(), addr2);
                enterValues(patientDemographics.ptInfoSectionHomePhnInput(), hmPhone);
                enterValues(patientDemographics.ptInfoSectionWorkPhnInput(), wkPhone);
                enterValues(patientDemographics.ptInfoSectionPostalCodeInput(), zip.getZipId());
                enterValues(patientDemographics.ptInfoSectionEmailInput(), email);
    
                patientInfo.setLastName(lstNm);
                patientInfo.setFirstName(fstNm);
                patientInfo.setDateOfBirth(ConvertUtil.convertStringToSQLDate(dob, DEFAULT_DATE_FORMAT));
                patientInfo.setGender(gender);
                patientInfo.setMaritalStatus(maritalStatus);
                patientInfo.setNotes(cmt);
                patientInfo.setDosOfMostMSPFrom(ConvertUtil.convertStringToSQLDate(dos, DEFAULT_DATE_FORMAT));
                patientInfo.setAddress1(addr1);
                patientInfo.setAddress2(addr2);
                patientInfo.setHomePhone(hmPhone);
                patientInfo.setWorkPhone(wkPhone);
                patientInfo.setEmail(email);
    
                break;
        }
    
        patientInfo.setPostalCode(zip.getZipId().trim());
        patientInfo.setCity(zip.getCtyNm().trim());
        patientInfo.setCountry(county.getName().trim());
        patientInfo.setState(state.getName().trim());
    
        return patientInfo;
    }

    private PayorInfo setPayorInfo(String type, PayorInfo orgPayorInfo) throws XifinDataAccessException, XifinDataNotFoundException, Exception {
        PayorInfo payorInfo = new PayorInfo();
        Pyr pyr = new Pyr();
        PyrGrp pyrGrp = new PyrGrp();
        
        String groupId = randomCharacter.getRandomNumericString(3);
        String subscriberId = randomCharacter.getRandomNumericString(5);
        String planId = randomCharacter.getRandomNumericString(3);
        String caseId = randomCharacter.getRandomNumericString(5);
        Date effdt = ConvertUtil.convertStringToSQLDate(timeStamp.getCurrentDate(), DEFAULT_DATE_FORMAT);
        if (!type.equals(FULL_FIELDS_WITHOUT_PAYOR_ID)) {
            //No other payors allowed after Patient Payor so pyr.GRP_NM()!= Patient
        	pyr = payorDao.getPyrIsNotBelongToClnGrpFromPYR(effdt);
        	while (pyr.getPyrGrpId() == PATIENT_PYR_GRP) {
	            pyr = payorDao.getPyrIsNotBelongToClnGrpFromPYR(effdt);
        	}
        	pyrGrp = rpmDao.getPyrGrpByPyrGrpId(testDb, pyr.getPyrGrpId());
        }
        
        if(pyr != new Pyr() && !orgPayorInfo.getPayorId().isEmpty()){
            Pyr pyr1 = payorDao.getPyrByPyrAbbrv(orgPayorInfo.getPayorId());
        	while (pyr.getPyrGrpId() == MCARE_PYR_GRP ||pyr.getPyrGrpId() == pyr1.getPyrGrpId()) {
                pyr = payorDao.getPyrIsNotBelongToClnGrpFromPYR(effdt);
            }
        	pyrGrp = rpmDao.getPyrGrpByPyrGrpId(testDb, pyr.getPyrGrpId());
        }
        
        switch (type) {
            case REQUIRED_FIELDS:
                setValueInSearchSectionInput(PAYOR_INFO_SECTION_NM);
                setPyrInput(pyr.getPyrAbbrv());
                break;
    
            case FULL_FIELDS:
                enterValues(patientDemographics.pyrInfoSectionPayoridInput(), pyr.getPyrAbbrv());
                enterValues(patientDemographics.pyrInfoSectionSubscriberIdInput(), subscriberId);
                enterValues(patientDemographics.pyrInfoSectionGroupIdInput(), groupId);
                enterValues(patientDemographics.pyrInfoSectionPlanIdInput(), planId);
                enterValues(patientDemographics.pyrInfoSectionCaseIdInput(), caseId);
    
                payorInfo.setSubscriberID(subscriberId);
                payorInfo.setGroupId(groupId);
                payorInfo.setPlanId(planId);
                payorInfo.setCaseId(caseId);
                break;
    
            case FULL_FIELDS_WITHOUT_PAYOR_ID:
                enterValues(patientDemographics.pyrInfoSectionSubscriberIdInput(), subscriberId);
                enterValues(patientDemographics.pyrInfoSectionGroupIdInput(), groupId);
                enterValues(patientDemographics.pyrInfoSectionPlanIdInput(), planId);
                enterValues(patientDemographics.pyrInfoSectionCaseIdInput(), caseId);
    
                payorInfo.setPayorPriority(orgPayorInfo.getPayorPriority());
                payorInfo.setPayorId(orgPayorInfo.getPayorId());
                payorInfo.setPayorName(orgPayorInfo.getPayorName());
                payorInfo.setGroupName(orgPayorInfo.getGroupName());
                payorInfo.setSubscriberID(subscriberId);
                payorInfo.setGroupId(groupId);
                payorInfo.setPlanId(planId);
                payorInfo.setCaseId(caseId);
                break;

            default:
            	break;
        }
    
        if (!type.equals(FULL_FIELDS_WITHOUT_PAYOR_ID)) {
            payorInfo.setPayorPriority(1);
            payorInfo.setPayorId(pyr.getPyrAbbrv());
            payorInfo.setPayorName(pyr.getName());
            payorInfo.setGroupName(pyrGrp.getGrpNm().toUpperCase());
        }
    
        return payorInfo;
    }

    private String setPyrInput(String pyrId) throws Exception {
    	assertTrue(isElementPresent(patientDemographics.pyrInfoSectionPayoridInput(), 5), "        EPI input is displayed");
        enterValues(patientDemographics.pyrInfoSectionPayoridInput(), pyrId);
        return pyrId;
	}
    private void setValueInSearchSectionInput(String sectionNm) throws Exception {
        assertTrue(isElementPresent(patientDemographics.footerSearchInput(), 10), "        At footer, search input should be shown.");
        enterValues(patientDemographics.footerSearchInput(), sectionNm);
    }

    private EmployerInfo setValuesInEmployerInfoSection() throws Exception {
        EmployerInfo employerInfo = new EmployerInfo();
        setValueInSearchSectionInput(EMPLOYER_INFO_SECTION_NM);
        String employerName = randomCharacter.getRandomAlphaString(5);
        String employmentStatus = EMPLOYMENT_STATUS_SELF_EMPL_TYPE;
        String address1 = randomCharacter.getRandomAlphaString(5);
        String address2 = randomCharacter.getRandomAlphaString(5);
        Zip zip = zipDao.getDataWithLengthZipIdFromZIP();
        String postalCode = zip.getZipId().trim();
        String city = zip.getCtyNm().trim();
        State state = rpmDao.getStateByStateId(testDb, zip.getStId()).get(0);
        Country country = countryDao.getCountryByAbbrv(USA);
        String workPhone = AREA_CODE_USA + randomCharacter.getNonZeroRandomNumericString(6);
        String fax = AREA_CODE_USA + randomCharacter.getNonZeroRandomNumericString(6);

        enterValues(patientDemographics.employerInfoSectionEmployerNameInput(), employerName);
        selectDropDown(patientDemographics.employerInfoSectionEmploymentStatusDdl(), employmentStatus);
        enterValues(patientDemographics.employerInfoSectionAddress1Input(), address1);
        enterValues(patientDemographics.employerInfoSectionAddress2Input(), address2);
        enterValues(patientDemographics.employerInfoSectionPostalCodeInput(), postalCode + Keys.TAB);
        enterValues(patientDemographics.employerInfoSectionWorkPhoneInput(), workPhone);
        enterValues(patientDemographics.employerInfoSectionFaxInput(), fax);
    
        employerInfo.setEmployerName(employerName);
        employerInfo.setEmployerStatus(employmentStatus);
        employerInfo.setAddress1(address1);
        employerInfo.setAddress2(address2);
        employerInfo.setPostalCode(postalCode);
        employerInfo.setCity(city);
        employerInfo.setState(state.getName().trim());
        employerInfo.setCountry(country.getName().trim());
        employerInfo.setWorkPhone(workPhone);
        employerInfo.setFax(fax);
        return employerInfo;
    }

    private InsuranceInformationSection setValuesInInsuranceInformationSection(String fieldType, PayorInfo orgPayorInfo, String type,PatientInformation actPatientInformation) throws Exception {
		//Input full fields in this section
		InsuranceInformationSection insuranceInfo = new InsuranceInformationSection();
//		String clnBillingCategory = clientDao.getRandomClnBillCategory().getAbbrev();
//	    terraLogicUtil.selectDropDownJQGridNoTagA(patientDemographics.insuranceInfoSectionclnBillCategoryDdl(), clnBillingCategory);
	    SystemSetting ss = systemDao.getSystemSetting(1);
        String effectiveDateDataAsStr = ss.dataValue == null ? timeStamp.getCurrentDate(DEFAULT_DATE_FORMAT): ss.dataValue;
//	    insuranceInfo.setClnBillingCategory(clnBillingCategory.trim());
	    insuranceInfo.setEffectiveDate(ConvertUtil.convertStringToSQLDate(effectiveDateDataAsStr, DEFAULT_DATE_FORMAT));
//	    
	    checkInSuspendedCheckboxInsuranceInfoSection(insuranceInfo);
	    
	    //Suspended Reason Tbl
	    List<SuspendedReasonTable> suspReasonTbls = new ArrayList<SuspendedReasonTable>();
	    SuspendedReasonTable suspReasonTbl = setValuesInSuspendedReasonTbl(EMPTY);
	    suspReasonTbls.add(suspReasonTbl);
	    insuranceInfo.setSuspendedReasons(suspReasonTbls);
	    
	    //Payor Info Tab
	    List<PayorTab> payorTabs = new ArrayList<PayorTab>();
	    PayorTab payorTab = setValuesInPayorInfoTab(fieldType, orgPayorInfo, type,actPatientInformation);
	    payorTabs.add(payorTab) ;
	    insuranceInfo.setPayorTabs(payorTabs);
	    return insuranceInfo;
	}

    private InsuredInfo setValuesInInsuredInfoSection(String type, PatientInformation actPatientInformation) throws Exception {
	    InsuredInfo insuredInfo = new InsuredInfo();
	    setValueInSearchSectionInput(INSURED_INFO_SECTION_NM);
	    
	    DateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	    String relationship = RELATIONSHIP_ORTHER_TYPE;
	    String firstName = randomCharacter.getRandomAlphaString(5);
	    String lastName = randomCharacter.getRandomAlphaString(5);
	    String dateOfBirth = timeStamp.getCurrentDate(DEFAULT_DATE_FORMAT);
	    String gender = genderDao.getGenderType().getDescr();
	    String ptSsn = randomCharacter.getNonZeroRandomNumericString(9);
	    String address1InsuredInfo = randomCharacter.getRandomAlphaString(5);
	    String address2InsuredInfo = randomCharacter.getRandomAlphaString(5);
	    String postalCodeInsuredInfo = EMPTY;
	    String cityInsuredInfo = EMPTY;
	    String stateInsuredInfo = EMPTY;
		if (!type.equals(ADD_NEW)) {
	    	Zip zipInsuredInfo = zipDao.getDataWithLengthZipIdFromZIP();
		    postalCodeInsuredInfo = zipInsuredInfo.getZipId();
		    cityInsuredInfo = zipInsuredInfo.getCtyNm().trim();
		    stateInsuredInfo = rpmDao.getStateByStateId(testDb, zipInsuredInfo.getStId()).get(0).getName().trim();
		} else {
		    postalCodeInsuredInfo = actPatientInformation.getPostalCode();
		    cityInsuredInfo = actPatientInformation.getCity().trim();
		    stateInsuredInfo = actPatientInformation.getState().trim();
	    }
		Country countryInsuredInfo = countryDao.getCountryByAbbrv(USA);
		String homePhoneInsuredInfo = AREA_CODE_USA + randomCharacter.getNonZeroRandomNumericString(6);
		String workPhoneInsuredInfo = AREA_CODE_USA + randomCharacter.getNonZeroRandomNumericString(6);

        selectDropDown(patientDemographics.insuredInfoSectionRelationshipDdl(), relationship);
        enterValues(patientDemographics.insuredInfoSectionFirstnameInput(), firstName);
        enterValues(patientDemographics.insuredInfoSectionLastnameInput(), lastName);
        enterValues(patientDemographics.insuredInfoSectionDateOfBirthInput(), dateOfBirth);
        selectDropDown(patientDemographics.insuredInfoSectionGenderDdl(), gender);
        enterValues(patientDemographics.insuredInfoSectionSsnInput(), ptSsn);
        enterValues(patientDemographics.insuredInfoSectionAddress1Input(), address1InsuredInfo);
        enterValues(patientDemographics.insuredInfoSectionAddress2Input(), address2InsuredInfo);
        enterValues(patientDemographics.insuredInfoSectionPostalCodeInput(), postalCodeInsuredInfo + Keys.TAB);
        enterValues(patientDemographics.insuredInfoSectionHomePhoneInput(), homePhoneInsuredInfo);
        enterValues(patientDemographics.insuredInfoSectionWorkPhoneInput(), workPhoneInsuredInfo);
	    
	    insuredInfo.setRelationship(relationship);
	    insuredInfo.setFirstName(firstName);
	    insuredInfo.setLastName(lastName);
	    insuredInfo.setDateOfBirth(new Date(formatter.parse(dateOfBirth).getTime()));
	    insuredInfo.setGender(gender);
	    insuredInfo.setSsn(ptSsn);
	    insuredInfo.setAddress1(address1InsuredInfo);
	    insuredInfo.setAddress2(address2InsuredInfo);
	    insuredInfo.setPostalCode(postalCodeInsuredInfo);
	    insuredInfo.setCity(cityInsuredInfo);
	    insuredInfo.setState(stateInsuredInfo);
	    insuredInfo.setCountry(countryInsuredInfo.getName());
	    insuredInfo.setHomePhone(homePhoneInsuredInfo);
	    insuredInfo.setWorkPhone(workPhoneInsuredInfo);
	    return insuredInfo;
	}

    private PayorTab setValuesInPayorInfoTab(String fieldType, PayorInfo orgPayorInfo, String type, PatientInformation actPatientInformation) throws Exception {
	    timeStamp = new TimeStamp();
	    randomCharacter = new RandomCharacter();
	    PayorTab payorTab = new PayorTab();
	    //Payor Info section
	    PayorInfo payorInfo =  setPayorInfo(fieldType, orgPayorInfo);
	    // Insured Info section
	    InsuredInfo insuredInfo = setValuesInInsuredInfoSection(type,actPatientInformation);
	    //Payor Notes section
	    PayorNotes payorNotes = setValuesInPayorNotesSection();
	    // Employer Info section
	    EmployerInfo employerInfo = setValuesInEmployerInfoSection();
	    payorTab.setPayorInfo(payorInfo);
	    payorTab.setInsuredInfo(insuredInfo);
	    payorTab.setEmployerInfo(employerInfo);
	    payorTab.setPayorNotes(payorNotes);
	    
	    return payorTab;
	}

    private PayorNotes setValuesInPayorNotesSection() throws Exception {
        PayorNotes payorNotes = new PayorNotes();
    
        setValueInSearchSectionInput(PAYOR_NOTES_SECTION_NM);
        String claimNotes = randomCharacter.getNonZeroRandomNumericString(10);
        String internalNotes = randomCharacter.getNonZeroRandomNumericString(10);
        String otherInfo1 = randomCharacter.getNonZeroRandomNumericString(10);
        String otherInfo2 = randomCharacter.getNonZeroRandomNumericString(10);
        String otherInfo3 = randomCharacter.getNonZeroRandomNumericString(10);

        enterValues(patientDemographics.payorNotesSectionClaimNotesInput(), claimNotes);
        enterValues(patientDemographics.payorNotesSectionInternalNotesInput(), internalNotes);
        enterValues(patientDemographics.payorNotesSectionOtherInfo1Input(), otherInfo1);
        enterValues(patientDemographics.payorNotesSectionOtherInfo2Input(), otherInfo2);
        enterValues(patientDemographics.payorNotesSectionOtherInfo3Input(), otherInfo3);
    
        payorNotes.setClaimNotes(claimNotes);
        payorNotes.setInteralNotes(internalNotes);
        payorNotes.setOrtherInfo2(otherInfo2);
        payorNotes.setOrtherInfo1(otherInfo1);
        payorNotes.setOrtherInfo3(otherInfo3);
    
        return payorNotes;
    }

    private SuspendedReasonTable setValuesInSuspendedReasonTbl(String type) throws Exception {
	    String note = randomCharacter.getRandomAlphaString(6);
	    SuspendedReasonTable suspReasonTbl = new SuspendedReasonTable();
	    String ptSuspendReasonTyp = patientDao.getDataFromPtSuspendReasonTyp().getDescr();
	    assertTrue(isElementPresent(patientDemographics.suspendedReasonTblReasonDdl(), 5), "        In Suspended Reason Tbl, ReasonDdl is present");
		if (type.equals(UPDATE)) {
	    	ptSuspendReasonTyp = patientDemographics.suspendedReasonTblReasonDdl().getText();
		} else {
            selectDropDown(patientDemographics.suspendedReasonTblReasonDdl(), ptSuspendReasonTyp);
	    }
        enterValues(patientDemographics.suspendedReasonTblNoteInput(), note);
		if(!patientDemographics.suspendedReasonTblNoteInput().getAttribute("value").equals(note)){
            enterValues(patientDemographics.suspendedReasonTblNoteInput(), note);
		}
	    suspReasonTbl.setDate(ConvertUtil.convertStringToSQLDate(patientDemographics.suspendedReasonTblDateInput().getAttribute("value"), DEFAULT_DATE_FORMAT));
	    suspReasonTbl.setUser(patientDemographics.suspendedReasonTblDateInput().getAttribute("value"));
	    suspReasonTbl.setUser(userName);	    
	    suspReasonTbl.setReason(ptSuspendReasonTyp);
	    suspReasonTbl.setNotes(note);
	    suspReasonTbl.setFix(patientDemographics.suspendedReasonTblFixChk("2").isSelected());
	    
	    return suspReasonTbl;
	}

    private NonRPMDialysisPatientHistoryTable setValueToNonRPMDialysisPatientHistoryTbl(String type, NonRPMDialysisPatientHistoryTable nonRPMDialysisPatientHistoryTableOld) throws Exception {
        NonRPMDialysisPatientHistoryTable nonRPMDialysisPatientHistoryTable = new NonRPMDialysisPatientHistoryTable();
        String dos = EMPTY;
        ProcCd procCd = null;
        String procCdId = EMPTY;
        switch (type) {
            case ADD_NEW:
                dos = timeStamp.getCurrentDate(DEFAULT_DATE_FORMAT);
                procCdId = procedureCodeDao.getRandomProcCd().getProcId();
                procCd = procedureCodeDao.getListProcCdByProcId(procCdId).get(0);

                enterValues(patientDemographics.nonRpmDialysisTblDosInput(NEWEST_ROW), dos);
                enterValues(patientDemographics.nonRpmDialysisTblCompositeRoutineInput(NEWEST_ROW), procCd.getProcId());

                nonRPMDialysisPatientHistoryTable.setDos(ConvertUtil.convertStringToSQLDate(dos, DEFAULT_DATE_FORMAT));
                nonRPMDialysisPatientHistoryTable.setCompositeRoutine(procCd.getProcId());
                nonRPMDialysisPatientHistoryTable.setDescription(procCd.getDescr().toUpperCase());
                break;

            case UPDATE:
                clickHiddenPageObject(patientDemographics.nonRpmDialysisTblRow(NEWEST_ROW), 0);

                dos = timeStamp.getNextDay(DEFAULT_DATE_FORMAT, nonRPMDialysisPatientHistoryTableOld.getDos());
                procCdId = procedureCodeDao.getRandomProcCd().getProcId();
                procCd = procedureCodeDao.getListProcCdByProcId(procCdId).get(0);
                while (nonRPMDialysisPatientHistoryTableOld.getCompositeRoutine().equals(procCd.getProcId())) {
                    procCdId = procedureCodeDao.getRandomProcCd().getProcId();
                    procCd = procedureCodeDao.getListProcCdByProcId(procCdId).get(0);
                }

                enterValues(patientDemographics.nonRpmDialysisTblDosInput(NEWEST_ROW), dos);
                enterValues(patientDemographics.nonRpmDialysisTblCompositeRoutineInput(NEWEST_ROW), procCd.getProcId());

                nonRPMDialysisPatientHistoryTable.setDos(ConvertUtil.convertStringToSQLDate(dos, DEFAULT_DATE_FORMAT));
                nonRPMDialysisPatientHistoryTable.setCompositeRoutine(procCd.getProcId());
                nonRPMDialysisPatientHistoryTable.setDescription(procCd.getDescr().toUpperCase());
                break;

            default:
            	break;
        }

        return nonRPMDialysisPatientHistoryTable;
    }

    private void checkInSuspendedCheckboxInsuranceInfoSection(InsuranceInformationSection insuranceInfo) throws Exception {
        clickHiddenPageObject(patientDemographics.insuranceInfoSectionSuspendedChk(), 0);
        insuranceInfo.setSuspended(true);
    }

    private void cleanNewEPI(Header header) throws Exception {
    	if (header == null) { return; }
        Pt pt = patientDao.getPtByEpi(header.getEpi());

        try {
            List<PtPyrSuspendReasonDt> ptPyrSuspendReasonDts = patientDao.getPtPyrSuspendReasonDtByPtSeqId(pt.getSeqId());
            cleanPtPyrSuspendReasonDt(ptPyrSuspendReasonDts);
        } catch (Exception e) {
            logger.info("        No record in PtPyrSuspendReasonDt");
        }

        try {
            List<PtClnLnk> actPtClnLnks = patientDao.getPtClnLnkByPtSeqId(pt.getSeqId());
            cleanPtClnLnk(actPtClnLnks);
        } catch (Exception e) {
            logger.info("        No record in PtClnLnk");
        }

        try {
            List<PtPyrPhi> ptPyrPhis = new ArrayList<>();
            List<PtPyrV2> ptPyrV2s = patientDao.getPatientFromPtPyrV2BySeqId(pt.getSeqId());
            for (PtPyrV2 ptPyrV2 : ptPyrV2s) {
                PtPyrPhi ptPyrPhi = patientDao.getPatientFromPtPyrPhiBySeqId(ptPyrV2.getSeqId());
                ptPyrPhis.add(ptPyrPhi);
            }
            cleanPtPyrPhi(ptPyrPhis);
            cleanPtPyrV2(ptPyrV2s);
        } catch (Exception e) {
            logger.info("        No record in PtPyrPhi");
        }

        try {
            PtPhi ptPhi = patientDao.getPatientFromPTPHIBySeqId(pt.getSeqId());
            cleanPtPhi(ptPhi);
        } catch (Exception e) {
            logger.info("        No record in PtPhi");
        }
        
        try {
        	List<PtDemoChk> ptDemoChks = patientDao.getPtDemoChksByPtSeqId(pt.getSeqId());
        	for(PtDemoChk ptDemoChk : ptDemoChks){
        		cleanPtDemoChk(ptDemoChk);
        	}
        } catch (Exception e) {
            logger.info("        No record in PtDemoChk");
        }

        try {
            PtV2 ptV2 = patientDao.getPatientFromPTV2BySeqId(pt.getSeqId());
            cleanPtV2(ptV2);
        } catch (Exception e) {
            logger.info("        No record in PtPhi");
        }
    }

    private void cleanNewPtDialysisHistory(NonRPMDialysisPatientHistoryTable actualNonRPMDialysisPatientHistoryTable) throws XifinDataAccessException, XifinDataNotFoundException {
        PtDialysisHistory ptDialysisHistory = patientDao.getPtDialysisHistoryByDosProcId(actualNonRPMDialysisPatientHistoryTable.getDos(), actualNonRPMDialysisPatientHistoryTable.getCompositeRoutine());

        if (ptDialysisHistory != null) {
            ptDialysisHistory.setResultCode(ErrorCodeMap.DELETED_RECORD);
            patientDao.setPtDialysisHistory(ptDialysisHistory);
        }
    }

    private void cleanPtClnLnk(List<PtClnLnk> ptClnLnks) throws Exception {
        for (PtClnLnk ptClnLnk : ptClnLnks) {
            List<PtDiag> ptDiags = patientDao.getPtDiagByPtClnLnkSeqId(ptClnLnk.getSeqId());
            for (PtDiag ptDiag : ptDiags) {
                ptDiag.setResultCode(ErrorCodeMap.DELETED_RECORD);
                patientDao.setPtDiag(ptDiag);
            }
            ptClnLnk.setResultCode(ErrorCodeMap.DELETED_RECORD);
            patientDao.setPtClnLnk(ptClnLnk);
        }
    }

    private void cleanPtDemoChk(PtDemoChk ptDemoChk) throws Exception {
    	if (ptDemoChk == null) { return; }
        ptDemoChk.setResultCode(ErrorCodeMap.DELETED_RECORD);
        patientDao.setPtDemoChk(ptDemoChk);
    }

    private void cleanPtPhi(PtPhi ptPhi) throws Exception {
    	if (ptPhi == null) { return; }
        ptPhi.setResultCode(ErrorCodeMap.DELETED_RECORD);
        patientDao.setPtPhi(ptPhi);
    }

    private void cleanPtPyrPhi(List<PtPyrPhi> ptPyrPhis) throws Exception {
        for (PtPyrPhi ptPyrPhi : ptPyrPhis) {
            ptPyrPhi.setResultCode(ErrorCodeMap.DELETED_RECORD);
            patientDao.setPtPyrPhi(ptPyrPhi);
        }
    }
    
    private void cleanPtPyrV2(List<PtPyrV2> ptPyrV2s) throws Exception {
        for (PtPyrV2 ptPyrV2 : ptPyrV2s) {
        	ptPyrV2.setResultCode(ErrorCodeMap.DELETED_RECORD);
            patientDao.setPtPyrV2(ptPyrV2);
        }
    }

    private void cleanPtPyrSuspendReasonDt(List<PtPyrSuspendReasonDt> ptPyrSuspendReasonDts) throws Exception {
        for (PtPyrSuspendReasonDt ptPyrSuspendReasonDt : ptPyrSuspendReasonDts) {
            ptPyrSuspendReasonDt.setResultCode(ErrorCodeMap.DELETED_RECORD);
            patientDao.setPtPyrSuspendReasonDt(ptPyrSuspendReasonDt);
        }
    }

    private void cleanPtV2(PtV2 ptV2) throws Exception {
    	if (ptV2 == null) { return; }
    	ptV2.setResultCode(ErrorCodeMap.DELETED_RECORD);
        patientDao.setPtV2(ptV2);
    }

    private void clickOnSuspendedReasonTblCellRow(String string) throws Exception {
	    clickHiddenPageObject(patientDemographics.suspendedReasonTblCellRow("2"), 0);
	    xifinPortalUtils.waitForPageLoaded(wait);
	}

	private void clickOnAccessionDetailPageResetBtn() throws Exception {
        clickHiddenPageObject(accessionDetail.resetBtn(), 0);
        xifinPortalUtils.waitForPageLoaded(wait);
    }

    private String clickOnAccessionIDLinkByRow(String row) throws Exception {
        String accnId = patientDemographics.allAccessionsTblAccnIdLnk(row).getText();
        clickHiddenPageObject(patientDemographics.allAccessionsTblAccnIdLnk(row), 0);
        return accnId;
    }

    private void clickOnCreateNewEPILnk() throws Exception {
        clickHiddenPageObject(patientDemographics.ptDemoSectionCreateNewEPIIco(), 0);
        xifinPortalUtils.waitForPageLoaded(wait);
    }

    private void clickOnCreateNewEPIPopupOkBtn() throws Exception {
        clickHiddenPageObject(patientDemographics.createNewPtEPIPopupOkBtn(), 0);
        xifinPortalUtils.waitForPageLoaded(wait);
    }

    private void clickOnCreateNewEPIRad() throws Exception {
        clickHiddenPageObject(patientDemographics.createNewPtEPIPopupCreateNewEPIRad(), 0);
        xifinPortalUtils.waitForPageLoaded(wait);
    }

    private void clickOnLetTheSystemGenerateAnEPIUsingRad() throws Exception {
        clickHiddenPageObject(patientDemographics.createNewPtEPIPopupLetSysGenerateRad(), 0);
        xifinPortalUtils.waitForPageLoaded(wait);
    }

    private void clickOnResetBtn() throws Exception {
        clickHiddenPageObject(patientDemographics.footerResetBtn(), 0);
        xifinPortalUtils.waitForPageLoaded(wait);
    }

    private void clickOnSaveAndClearBtn() throws Exception {
        clickHiddenPageObject(patientDemographics.footerSaveAndClearBtn(), 0);
        xifinPortalUtils.waitForPageLoaded(wait);
    }
    
    private void clickOnRpmDialysisGetRPMHistoryBtn() throws Exception{
    	 clickHiddenPageObject(patientDemographics.rpmDialysisGetRPMHistoryBtn(), 0);
    	 wait.equals(ExpectedConditions.invisibilityOf(patientDemographics.rmpGetRMPHistoryProcessing()));
    }

    private String createNewDataNotInPT(String typ) throws Exception {
        boolean isExist = true;
        String newData = randomCharacter.getNonZeroRandomNumericString(9);

        while (isExist) {
            Pt pt = null;

            switch (typ) {
                case NEW_EPI:
                    pt = patientDao.getPtByEpi(newData);
                    break;
                case NEW_SSN:
                    try {
                        pt = patientDao.getPtBySsn(Integer.parseInt(newData)).get(0);
                    } catch (Exception e) {
                    }
                    break;
            }

            if (pt == null || pt.getSeqId() == 0) {
                isExist = false;
            } else {
                newData = randomCharacter.getNonZeroRandomNumericString(9);
            }
        }

        return newData;
    }

    private PayorInfo inputPayorIDToNewPayorIDInputFieldInThePopup(String pyrId1) throws Exception {
        PayorInfo payorInfo = new PayorInfo();
        Pyr pyr2 = payorDao.getRandomDataForPyrSearchResultFromPyr();
        while (pyr2.getPyrAbbrv().equals(pyrId1) || pyr2.getPyrGrpId() == MCARE_PYR_GRP ) {
            pyr2 = payorDao.getRandomDataForPyrSearchResultFromPyr();
        }
        String pyrGrpName = rpmDao.getPyrGrpByPyrGrpId(testDb, pyr2.getPyrGrpId()).getGrpNm();
        payorInfo.setPayorPriority(2);
        payorInfo.setPayorId(pyr2.getPyrAbbrv());
        payorInfo.setPayorName(pyr2.getName());
        payorInfo.setGroupName(pyrGrpName);
        assertTrue(isElementPresent(patientDemographics.addPyrPopupNewPyrInput(), 5), "        At Add Pyr Popup, New Pyr Input is displayed.");
        enterValues(patientDemographics.addPyrPopupNewPyrInput(), pyr2.getPyrAbbrv());
        return payorInfo;
    }

    private void revertAccn(Accn accn) throws Exception {
    	if (accn == null) { return; }
        Accn expAccn = accessionDao.getAccn(accn.getAccnId());
        expAccn.setPtSeqId(accn.getPtSeqId());
        accessionDao.setAccn(expAccn);
    }

    private String selectDialysisType() throws Exception {
        String dialysisTypeSelected = dialDao.getDialTyp().getDescr();
        selectDropDownJQGridClickOnly(patientDemographics.dialysisInformationDialysisTypS2(), dialysisTypeSelected);
        return dialysisTypeSelected;
    }

    private Accn updatePtIdToAccn(String epi) throws XifinDataAccessException, XifinDataNotFoundException, Exception {
        Pt pt = patientDao.getPtByEpi(epi);
        Accn accn = accessionDao.getRandomDataFromACCN();
        Accn orgAccn = accessionDao.getAccn(accn.getAccnId());
        accn.setPtSeqId(pt.getSeqId());
        accessionDao.setAccn(accn);

        return orgAccn;
    }

    private void verifyAccessionDetailLoadPageIsDisplayed() throws Exception {
        wait.until(ExpectedConditions.elementToBeClickable(accessionDetail.headerLoadPageTitleTxt()));
        assertTrue(isElementPresent(accessionDetail.headerLoadPageTitleTxt(), 5), "        Accession Detail Page title txt is displayed");
        assertTrue(accessionDetail.headerLoadPageTitleTxt().getText().equalsIgnoreCase(ACCESSION_DETAIL_PAGE_TITLE), "        The page title should be displayed '" + ACCESSION_DETAIL_PAGE_TITLE + "'");
    }

    private void verifyAccessionDetailPageIsDisplayedEPICorrectly(String accnId, Header actHeader) throws Exception {
        assertTrue(isElementPresent(accessionDetail.headerDetailPageTitleTxt(), 10), "        Accession Detail Page title txt is displayed");
        assertTrue(accessionDetail.headerDetailPageTitleTxt().getText().equals(ACCESSION_DETAIL_PAGE_TITLE), "        The page title should be displayed '" + ACCESSION_DETAIL_PAGE_TITLE + "'");
        assertEquals(accnId, accessionDetail.accnIdText().getAttribute("value"), "        Accession Id is displayed correctly");
        assertEquals(actHeader.getEpi(), accessionDetail.epiTxt().getText(), "        EPI is displayed correctly");
    }

    private void verifyAddNewRowBtnEnable() throws Exception {
    	assertTrue(isElementEnabled(patientDemographics.nonRpmDialysisTblAddBtn(), 5, true), "        Non-RPM Dialysis Patient History table - add button is enable");
    }

    private void verifyAllAccessionsForThisPatientDataIsDisplayedCorrectly(Header actHeader) throws XifinDataAccessException, XifinDataNotFoundException, Exception {
        Pt pt = patientDao.getPtByEpi(actHeader.getEpi());
        List<AllAccnPt> allAccnPts = accessionDao.getAllAccnPtFromAccnByPtSeqId(pt.getSeqId());
        List<AllAccessionsForThisPatientTable> actAllAccessionsForThisPatientTables = patientDemographicsUtil.mapAllAccessionsForThisPatientTable(allAccnPts);
        List<AllAccessionsForThisPatientTable> expAllAccessionsForThisPatientTables = getValueAtAllAccessionsForThisPatientTable();

        assertEquals(actAllAccessionsForThisPatientTables, expAllAccessionsForThisPatientTables, "        All Accessions For This Patient Data Is Displayed Correctly");
    }

    private void verifyAllAccessionsForThisPatientTableIsEmpty() throws Exception {
        assertEquals(patientDemographics.allAccessionsTblTotalRecordsTxt().getText(), EMPTY_RECORDS, "        All Accessions for this Patient Table is empty");
    }

    private void verifyAssociatedPatientIdsAddPopupIsDisplayed(String popupTitle) throws Exception {
        assertEquals(patientDemographics.associatedPtIdsAddEditPopupTitle().getText().trim(), popupTitle, "        Popup title should '" + popupTitle + "'.");
    }

    private void verifyAssociatedPatientIDsIsDisplayedCorrectly(AssociatedPatientIDsTable associatedPatientIDsTable) throws Exception {
        assertTrue(isElementPresent(patientDemographics.associatedPtIDsTbl(), 5), "        The [Associated Patient IDs] table is displayed.");

        enterValues(patientDemographics.associatedPtIDsTblPatientIdFilterInput(), associatedPatientIDsTable.getPatientId());
        enterValues(patientDemographics.associatedPtIDsTblSourceTypeFilterInput(), associatedPatientIDsTable.getSourceType());
        enterValues(patientDemographics.associatedPtIDsTblSourceIdFilterInput(), associatedPatientIDsTable.getSourceId());
        enterValues(patientDemographics.associatedPtIDsTblSourceNameFilterInput(), associatedPatientIDsTable.getSourceName());
        enterValues(patientDemographics.associatedPtIDsTblLongTermDiagFilterInput(), associatedPatientIDsTable.getLongTempDiagnosis());
        enterValues(patientDemographics.associatedPtIDsTblOrderingPhysicianNpiFilterInput(), associatedPatientIDsTable.getOrderingPhysicianNPI());
        enterValues(patientDemographics.associatedPtIDsTblOrderingPhysicianNameFilterInput(), associatedPatientIDsTable.getOrderingPhysicianName());
        assertTrue(patientDemographics.associatedPtIDsTblTotalResultTxt().getText().equals(EMPTY_RECORDS) != true, "        The [Associated Patient IDs] table is displayed data correctly.");
    }

    private void verifyAssociatedPatientIDsIsEmpty() throws Exception {
        xifinPortalUtils.waitForPageLoaded(wait);
    	assertTrue(isElementPresent(patientDemographics.associatedPtIDsTblTotalResultTxt(), 5), "        At the Associated Patient IDs table, the total record Text is displayed.");
        assertEquals(patientDemographics.associatedPtIDsTblTotalResultTxt().getText(), EMPTY_RECORDS, "        Associated Patient IDs table is empty");
    }

    private void verifyDeleteCheckboxIsDisable() throws Exception {
        assertTrue(isElementPresent(patientDemographics.suspendedReasonTblDeleteChk(NEWEST_ROW), 5), "        Suspended Reason tbl, Fix Text is displayed.");
        assertEquals(patientDemographics.suspendedReasonTblDeleteChk(NEWEST_ROW).getAttribute("disabled"), "true", "        Suspended Reason tbl, Delete checkbox is disable.");
    }

    private void verifyDialysisInformationSectionAreEmpty() throws Exception {
        DialysisInformationSection dialysisInformationSection = getDialysisInformationSectionValue();
        assertEquals(dialysisInformationSection, new DialysisInformationSection(), "        Dialysis Information is empty");
    }

    private void verifyEmployerInfoSectionIsEmpty() throws Exception {
        EmployerInfo actmployerInfo = getValuesInEmployerInfoSection();
        actmployerInfo.setCountry(EMPTY);
        assertEquals(actmployerInfo, new EmployerInfo(), "        Employer Info section are empty");
    }

    private void verifyEmployerInfoUpdateCorrectedData(String epi, InsuranceInformationSection actualInsuranceInformationSection, Date effDt, int tabNum, boolean mutiRecord, int pyrPrio) throws Exception {
        EmployerInfo actualEmployerInfo = patientDemographicsUtil.mapEmployerInfo(epi, USA, effDt,mutiRecord,pyrPrio);
        EmployerInfo exptectedEmployerInfo = actualInsuranceInformationSection.getPayorTabs().get(tabNum).getEmployerInfo();
        assertEquals(actualEmployerInfo, exptectedEmployerInfo, "        A new record added to PT_PYR_PHI table corrected");
    }

    private void verifyInsuranceInformationWhenLoadNewEpi(Header header) throws Exception {
        assertTrue(isElementPresent(patientDemographics.insuranceInfoSectionEffDtDdl(), 5), "        At Insurance Information section, Effective date dropdown is displayed.");
        String effectiveDateUIAsStr = patientDemographics.insuranceInfoSectionEffDtDdl().getText();
        SystemSetting ss = systemDao.getSystemSetting(1);
        String effectiveDateDataAsStr = ss.dataValue == null ? timeStamp.getCurrentDate(DEFAULT_DATE_FORMAT): ss.dataValue;
        assertEquals(ConvertUtil.convertStringToSQLDate(effectiveDateUIAsStr, DEFAULT_DATE_FORMAT), ConvertUtil.convertStringToSQLDate(effectiveDateDataAsStr, DEFAULT_DATE_FORMAT), "        Effective Date display incorrectly:");

        String clientBillingCategory = patientDemographics.insuranceInfoSectionclnBillCategoryDdl().getText();
        assertTrue(clientBillingCategory.trim().equals(EMPTY), "        Client Billing Category is display correctly");

        verifyPayorInfoSectionIsEmpty();
        verifyInsuredInfoSectionIsEmpty(header, REQUIRED_FIELDS, null);
        verifyPayorNotesSectionIsEmpty();
        verifyEmployerInfoSectionIsEmpty();
    }

    private void verifyInsuredInfoSectionIsEmpty(Header header, String typ, String lastNm) throws Exception {
        InsuredInfo expInsuredInfo = getValueInsuredInfoSection();
        InsuredInfo actInsuredInfo = new InsuredInfo();
        Country country = countryDao.getCountryByAbbrv(USA);
        actInsuredInfo.setCountry(country.getName());
        actInsuredInfo.setRelationship(relationshipDao.getDataFromRelshpTypByRelshpTypId(1).getDescr());
        actInsuredInfo.setSsn(String.valueOf(header.getPatientSSN() == 0 ? EMPTY : header.getPatientSSN()));
        if (typ.equals(NEW_EPI_PATIENT_LAST_NAME)) {
            actInsuredInfo.setLastName(lastNm);
        }
        assertEquals(actInsuredInfo, expInsuredInfo, "        Insured Info section are empty");
    }

    private void verifyInsuredInfoUpdateCorrectedData(String epi, InsuranceInformationSection actualInsuranceInformationSection, Date effDt, int tabNum, boolean mutiRecord, int pyrPrio) throws Exception {
        InsuredInfo actualInsuredInfo = patientDemographicsUtil.mapInsuredInfo(epi, USA, effDt,mutiRecord,pyrPrio);
        InsuredInfo exptectedInsuredInfo = actualInsuranceInformationSection.getPayorTabs().get(tabNum).getInsuredInfo();
        assertEquals(actualInsuredInfo, exptectedInsuredInfo, "        A new record added to PT_PYR_PHI table corrected");
    }

    private void verifyNewEffDtIsDeletedInDB(String epi, Date newEffDt, int tabNum){
    	PtPyrV2 ptPyrV2 = null;
    	PtPyrPhi ptPyrPhi = null;
		try {
			Pt pt = patientDao.getPtByEpi(String.valueOf(epi));
			ptPyrV2 = patientDao.getPatientFromPtPyrV2BySeqIdAndEffDt(pt.getSeqId(), newEffDt);
			ptPyrPhi = patientDao.getPatientFromPtPyrPhiBySeqId(ptPyrV2.getSeqId());
		} catch (Exception e) {
		}
        assertEquals(ptPyrV2, null, "        A new record deleted to PT_PYR_V2 table corrected");
        assertEquals(ptPyrPhi, null, "        A new record deleted to PT_PYR_PHI table corrected");
    }
  

    private List<PtClnLnk> verifyNewRecordIsSavedInPtClnLnkTableCorrectly(int ptSeqId) throws Exception {
        List<PtClnLnk> actPtClnLnks = patientDao.getPtClnLnkByPtSeqId(ptSeqId);
        assertTrue(actPtClnLnks.size() != 0, "        New record is saved in PT_CLN_LNK table with valid data");
        return actPtClnLnks;
    }

    private Pt verifyNewRecordIsSavedInPtTableCorrectly(String epi) throws Exception {
        Pt actPt = patientDao.getPtByEpi(epi);
        assertNotNull(actPt, "        New record save in PT table with valid data, epi=" + epi);
        return actPt;
    }

    private void verifyNonRpmDialysisPatientHistoryIsDisplay() throws Exception {
        setValueInSearchSectionInput(NON_RPM_DIALYSIS_PATIENT_HISTORY_SECTION_NM);

        assertTrue(isElementPresent(patientDemographics.nonRpmDialysisTbl(), 5), "        Non-RPM Dialysis Patient History section - Non-RPM Dialysis Patient History table is displayed");
        assertTrue(isElementPresent(patientDemographics.nonRpmDialysisChooseFileNameTxt(), 5), "        RPM Dialysis Patient History section - Choose File text is displayed");
        assertTrue(isElementPresent(patientDemographics.nonRpmDialysisAppendBtn(), 5), "        RPM Dialysis Patient History section - Append btn is displayed");
    }

    private void verifyPatientDemographicsIsSavedInPTByRequireFields(Header header, PatientInformation patientInformation, PayorInfo payorInfo) throws Exception {
        Assert.assertNotNull(patientDemographicsUtil);
        Assert.assertNotNull(header);
        Assert.assertNotNull(patientInformation);
        Assert.assertNotNull(payorInfo);
        PatientDemographicsRequireFields actPtDemoRequireFields = patientDemographicsUtil.mapPatientDemographicsAtRequireFieldsFromDB(header, patientInformation, payorInfo);
        PatientDemographicsRequireFields expPtDemoRequireFields = patientDemographicsUtil.mapPatientDemographicsAtRequireFields(header, patientInformation, payorInfo);
        assertEquals(actPtDemoRequireFields,expPtDemoRequireFields, "        Data are saved in DB correctly at require fields.");
    }

    private void verifyPatientDemographicsLoadPageIsDisplayed() throws Exception {
        wait.until(ExpectedConditions.elementToBeClickable(patientDemographics.headerPageTitleTxt()));
        assertTrue(patientDemographics.headerPageTitleTxt().getText().equals(PATIENT_DEMOGRAPHICS_PAGE_TITLE), "        The page title should be displayed '" + PATIENT_DEMOGRAPHICS_PAGE_TITLE + "'");
        assertFalse(isElementHidden(patientDemographics.ptDemoSection(), 10), "         Patient Demographics section is displayed");
    }

    private void verifyPatientInformationDataIsSavedToDBCorrectly(Header header, PatientInformation actPtientInformation) throws Exception {
        Pt pt = patientDao.getPtByEpi(header.getEpi());
        PatientInformation expPtientInformation = patientDemographicsUtil.mapPatientInformationSection(pt, USA);
        assertEquals(actPtientInformation, expPtientInformation, "        A new record added to PT table corrected");
    }

    private void verifyPatientInformationSectionsIsEmpty() throws Exception {
        PatientInformation expPatientInformation = getValuePatientInfoSection(FULL_FIELDS);
        expPatientInformation.setCountry(EMPTY);
        assertEquals(new PatientInformation(), expPatientInformation, "        All fields inside Patient Information section are empty, except [country] dropdown");
    }

    private void verifyPayorInfoSectionIsEmpty() throws Exception {
        PayorInfo actPayorInfo = getValuePayorInfoSection(FULL_FIELDS);
        PayorInfo expPayorInfo = new PayorInfo();
        expPayorInfo.setPayorPriority(1);
        assertEquals(actPayorInfo, expPayorInfo, "        Payor Info section are empty");
    }

    private void verifyPayorInfoUpdateCorrectedData(String epi, InsuranceInformationSection actualInsuranceInformationSection, Date effDt, int tabNum, boolean mutiRecord, int pyrPrio) throws Exception {
        String pyrAbbrev = actualInsuranceInformationSection.getPayorTabs().get(tabNum).getPayorInfo().getPayorId();
        PayorInfo actualPayorInfo = patientDemographicsUtil.mapPayorInfo(epi, pyrAbbrev, effDt,mutiRecord,pyrPrio);
        List<PayorTab> payorTabs = actualInsuranceInformationSection.getPayorTabs();
        PayorInfo exptectedPayorInfo = new PayorInfo();
        for(PayorTab payorTab: payorTabs){
        	if(payorTab.getPayorInfo().getPayorPriority() == actualPayorInfo.getPayorPriority()){
        		exptectedPayorInfo = payorTabs.get(tabNum).getPayorInfo();
        	}
        }
        assertEquals(actualPayorInfo, exptectedPayorInfo, "        A new record added to PT_PYR_V2 table corrected");
    }

    private void verifyPayorNotesSectionIsEmpty() throws Exception {
        PayorNotes actPayorNotes = getValuesInPayorNotesSection();
        assertEquals(actPayorNotes, new PayorNotes(), "        Payor Notes section are empty");
    }

    private void verifyPayorNotesUpdateCorrectedData(String epi, InsuranceInformationSection actualInsuranceInformationSection, Date effDt, int tabNum, boolean mutiRecord, int pyrPrio) throws Exception {
        PayorNotes actualPayorNotes = patientDemographicsUtil.mapPayorNotes(epi, effDt,mutiRecord,pyrPrio);
        PayorNotes exptectedPayorNotes = actualInsuranceInformationSection.getPayorTabs().get(tabNum).getPayorNotes();
        assertEquals(actualPayorNotes, exptectedPayorNotes, "        A new record added to PT_PYR_V2 table correcte");
    }

    private void verifyPtLastNameAtPatientInformationSectionIsEmpty(String typ, String ptLastMn) throws Exception {
        setValueInSearchSectionInput(PATIENT_INFORMATION_SECTION_NM);
        PatientInformation actPatientInformation = new PatientInformation();
        Country county = countryDao.getCountryByAbbrv(USA);
        if (typ.equals(NEW_EPI_PATIENT_LAST_NAME)) {
            actPatientInformation.setLastName(ptLastMn);
        }
        actPatientInformation.setCountry(county.getName());
        PatientInformation expPatientInformation = getValuePatientInfoSection(FULL_FIELDS);
        assertEquals(actPatientInformation, expPatientInformation, "        Insured Info section are empty");
    }

    private void verifyPtPyrSuspendReasonDtIsSavedToDBCorrectly(Header header, SuspendedReasonTable actSuspendedReasonTable) throws Exception {
        SuspendedReasonTable expSuspendedReasonTable = patientDemographicsUtil.mapSuspendedReasonTable(header.getEpi());
        assertEquals(actSuspendedReasonTable, expSuspendedReasonTable, "        A new record added to PT_PYR_SUSPEND_REASON_DT table correctly.");
    }

    private void verifyRequiredFieldsDisplayCorrected(Header actualHeader, PatientInformation actualPatientInformation, PayorInfo actualPayorInfo) throws Exception {
        Header expectedHeader = getValueHeaderSection();
        PatientInformation expectedPatientInformation = getValuePatientInfoSection(REQUIRED_FIELDS);
        PayorInfo expectedPayorInfo = getValuePayorInfoSection(REQUIRED_FIELDS);

        assertEquals(actualHeader, expectedHeader, "        Header data is displayed correctly");
        assertEquals(actualPatientInformation, expectedPatientInformation, "        Patient Information data is displayed correctly");
        assertEquals(actualPayorInfo, expectedPayorInfo, "        PayorInfo data is displayed correctly");
    }

    private void verifyRpmDialysisPatientHistoryIsDisplay() throws Exception {
        setValueInSearchSectionInput(RPM_DIALYSIS_PATIENT_HISTORY_SECTION_NM);

        assertTrue(isElementPresent(patientDemographics.rpmDialysisFromDtInput(), 5), "        RPM Dialysis Patient History section  - From date input is displayed");
        assertTrue(isElementPresent(patientDemographics.rpmDialysisThroughDtInput(), 5), "        RPM Dialysis Patient History section  - through date input is displayed");
        assertTrue(isElementPresent(patientDemographics.rpmDialysisGetRPMHistoryBtn(), 5), "        RPM Dialysis Patient History section  - RPM History btn is displayed");
        assertTrue(isElementPresent(patientDemographics.rpmDialysisTbl(), 5), "        RPM Dialysis Patient History section  - RPM Dialysis Patient History table is displayed");
    }

    private void verifySuspendedCheckboxInsuranceInfoSectionIsCheckedAndDisabled() throws Exception {
        assertNotEquals(patientDemographics.insuranceInfoSectionSuspendedChk().getAttribute("value"), "false", "        Suspended Checkbox in Insurance Info Section Is Checked.");
        assertTrue(patientDemographics.insuranceInfoSectionSuspendedChk().getAttribute("class").contains("field_disabled"), "        Suspended Checkbox in Insurance Info Section Is Disabled.");
    }

    private void verifySuspendedReasonTblIsDisplayedCorrectly() throws Exception {
        SystemSetting ss = systemDao.getSystemSetting(SystemSettingMap.SS_SYSTEM_OVERRIDE_DATE);
        String valueSS1 = ss.dataValue == null ? timeStamp.getCurrentDate(DEFAULT_DATE_FORMAT): ss.dataValue;
        assertEquals(patientDemographics.suspendedReasonTblDateInput().getAttribute("value"), valueSS1, "        In [Suspended Reason] table has a new row is ready to add new with [Date] input get from SS#1.");
        assertEquals(patientDemographics.suspendedReasonTblUserInput().getAttribute("value"), userName, "        In [Suspended Reason] table, [User] colum get from user name log in.");
    }

    private SuspendedReasonTable verifySuspendedReasonTblIsDisplayedCorrectly(String note) throws Exception {
        SuspendedReasonTable actSuspendedReasonTable = new SuspendedReasonTable();
        enterValues(patientDemographics.suspendedReasonTblNoteFilterInput(), note);
        assertFalse(patientDemographics.suspendedReasonTblTotalRecordTxt().getText().equals(EMPTY_RECORDS), "        At [Suspended Reason] table,  Note is displayed correctly.");

        Date date = ConvertUtil.convertStringToSQLDate(patientDemographics.suspendedReasonTblDateTxt(NEWEST_ROW).getText(), DEFAULT_DATE_FORMAT);
        boolean isFix = patientDemographics.suspendedReasonTblFixChk(NEWEST_ROW).getAttribute("value").equals("true");
        actSuspendedReasonTable.setDate(date);
        actSuspendedReasonTable.setReason(patientDemographics.suspendedReasonTblReasonTxt(NEWEST_ROW).getText());
        actSuspendedReasonTable.setNotes(patientDemographics.suspendedReasonTblNoteTxt(NEWEST_ROW).getText());
        actSuspendedReasonTable.setUser(patientDemographics.suspendedReasonTblUserTxt(NEWEST_ROW).getText());
        actSuspendedReasonTable.setFix(isFix);

        return actSuspendedReasonTable;
    }

    private void verifyTheNewRowDisplaysCorrectedInSuspendedReasonTbl() throws Exception {
        assertTrue(Integer.parseInt(getTotalResultSearch(patientDemographics.suspendedReasonTblTotalRecordTxt())) > 0, "        Exist records in the table.");
        assertEquals(patientDemographics.suspendedReasonTblFixChk(NEWEST_ROW).isSelected(), false, "        In [Suspended Reason] table, [Fix] Checkbox is displayed.");
    }

    private void verifyValuesAreDisplayedCorrectly(String typEnter, Header actHeader, PatientInformation actPtientInformation, PayorInfo actPayorInfo) throws Exception {
    	if (actHeader != null) {
    		wait.until(ExpectedConditions.elementToBeClickable(patientDemographics.headerPageTitleTxt()));
            Header expHeader = getValueHeaderSection();
            assertEquals(actHeader, expHeader, "        Header is displayed correctly");
        }
        if (actPtientInformation != null) {
            PatientInformation expPatientInformation = getValuePatientInfoSection(typEnter);
            assertEquals(actPtientInformation, expPatientInformation, "        PatientInformation is displayed correctly");
        }
        if (actPayorInfo != null) {
            PayorInfo expPayorInfo = getValuePayorInfoSection(typEnter);
            assertEquals(actPayorInfo, expPayorInfo, "        PayorInfo is displayed correctly");
        }
    }
    
    private void verifyPayorInfoSectionAddPyrTabNameIsDisplayedCorrectly(String pyrId, String tabNum) throws Exception {
        assertEquals(patientDemographics.addPyrTabName(tabNum).getText(), pyrId, "        Add Pyr Tab Name is displayed correctly.");
    }
    
    private void verifyInsuredInfoSectionIsDisplayedWithSameDataAsTheFirstTab(Header header, String typ, PatientInformation patientInformation) throws Exception {
        InsuredInfo expInsuredInfo = getValueInsuredInfoSection();
        InsuredInfo actInsuredInfo = new InsuredInfo();
        Country country = countryDao.getCountryByAbbrv(USA);
        actInsuredInfo.setCountry(country.getName());
        actInsuredInfo.setRelationship(relationshipDao.getDataFromRelshpTypByRelshpTypId(1).getDescr());
        actInsuredInfo.setSsn(String.valueOf(header.getPatientSSN() == 0 ? EMPTY : header.getPatientSSN()));
        actInsuredInfo.setPostalCode(patientInformation.getPostalCode());
        actInsuredInfo.setCity(patientInformation.getCity().trim());
        assertEquals(actInsuredInfo, expInsuredInfo, "        Insured Info section is displayed correctly.");
    }
    
    
    private PtDialysisHistory prepareDataPtDialysisHistory(Pt pt) throws Exception {    
    	int seqId = databaseSequenceDao.getNextSequenceFromOracle("PT_DIALYSIS_HISTORY_SEQ");
    	PtDialysisHistory ptDialysisHistory = new PtDialysisHistory();
    	ptDialysisHistory.setResultCode(ErrorCodeMap.NEW_RECORD);
    	ptDialysisHistory.setSeqId(seqId);
    	ptDialysisHistory.setPtSeqId(pt.getSeqId());
    	java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    	ptDialysisHistory.setDos(date);
    	ProcCd procCd = procedureCodeDao.getRandomProcCd();
    	ptDialysisHistory.setProcId(procCd.getProcId());
    	patientDao.setPtDialysisHistory(ptDialysisHistory);
    	return ptDialysisHistory;
    }
    
    private void cleanPtDialysisHistory(PtDialysisHistory ptDialysisHistory) throws XifinDataAccessException {
    	ptDialysisHistory.setResultCode(ErrorCodeMap.DELETED_RECORD);
    	patientDao.setPtDialysisHistory(ptDialysisHistory);
    }
}
