package com.newXp.tests;

import com.mbasys.mars.ejb.entity.npi.Npi;
import com.mbasys.mars.ejb.entity.phys.Phys;
import com.mbasys.mars.ejb.entity.physPyrClnLic.PhysPyrClnLic;
import com.mbasys.mars.ejb.entity.physPyrLic.PhysPyrLic;
import com.mbasys.mars.ejb.entity.physPyrLicTyp.PhysPyrLicTyp;
import com.mbasys.mars.ejb.entity.physStateLic.PhysStateLic;
import com.mbasys.mars.ejb.entity.physXref.PhysXref;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrGrp.PyrGrp;
import com.mbasys.mars.ejb.entity.pyrGrpPhysExcl.PyrGrpPhysExcl;
import com.mbasys.mars.ejb.entity.pyrPhysExcl.PyrPhysExcl;
import com.mbasys.mars.ejb.entity.state.State;
import com.mbasys.mars.ejb.entity.taxonomy.Taxonomy;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.overall.filemaint.physicianLicense.PhysicianLicense;
import com.overall.menu.MenuNavigation;
import com.overall.search.PhysicianSearch;
import com.overall.search.PhysicianSearchResults;
import com.overall.search.TaxonomyCodeSearch;
import com.overall.search.TaxonomyCodeSearchResults;
import com.overall.utils.PhysicianLicenseUtils;
import com.overall.utils.XifinPortalUtils;
import com.xifin.qa.dao.rpm.domain.PayorOverrideRemit;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TimeStamp;
import domain.filemaint.physicianLicense.AffiliatedLicense;
import domain.filemaint.physicianLicense.PayorExclusion;
import domain.filemaint.physicianLicense.PayorGroupExclusion;
import domain.filemaint.physicianLicense.PhysicianCrossReference;
import domain.filemaint.physicianLicense.PhysicianLicenseSec;
import domain.filemaint.physicianLicense.StateLicenseSec;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class PhysicianLicenseTest extends SeleniumBaseTest {
	
	private int timeOut = 45;
	private TimeStamp timeStamp;
	private RandomCharacter randomCharacter;
	private PhysicianSearch physicianSearch;
	private XifinPortalUtils xifinPortalUtils;
	private PhysicianLicense physicianLicense;
	private TaxonomyCodeSearch taxonomyCodeSearch;
	private PhysicianLicenseUtils physicianLicenseUtils;
	private PhysicianSearchResults physicianLicenseResults;
	private TaxonomyCodeSearchResults taxonomyCodeSearchResults;
	private Npi npi;
	private Phys phys;
	private List<PhysXref> lstPhysXref;
	private List<PyrPhysExcl> lstPyrPhysExcl;
	private List<PyrGrpPhysExcl> lstPyrGrpPhysExcl;

	private static final String EMPTY = "";
	private static final String ALL_VALUE = "*";
	private static final String NO_RECORD = "0";
	private static final String FIRST_ROW = "2";
	private static final String SECOND_ROW = "3";
	private static final String ONE_RECORD = "1";
	private static final String LAST_ROW = "last()";
	private static final String EMPTY_RECORD = "Empty records";
	private static final String INTERNAL_NOTE = "Internal Note";
	private static final String OIG_EXCLUSION = "OIG Exclusion";
	private static final String STATE_LICENSES = "State Licenses";
	private static final String PAYOR_EXCLUSION = "Payor Exclusion";
	private static final String DATE_FORMATTED_DEFAULT = "MM/dd/yyyy";
	private static final String AFFILIATED_NUMBER = "Affiliated Number";
	private static final String AFFILIATED_LICENSES = "Affiliated Licenses";
	private static final String PHYSICIAN_SEARCH_TITLE = "Physician Search";
	private static final String TAXONOMY_CODE_SEARCH = "Taxonomy Code Search";
	private static final String PHYSICIAN_LICENSE_TITLE = "Physician License";
	private static final String PAYOR_GROUP_EXCLUSION = "Payor Group Exclusion";
	private static final String CLIENT_SPECIFIC_AFFILIATED = "Client Specific Affiliated";
	private static final String PHYSICIAN_CROSS_REFERENCES = "Physician Cross References";
	private static final String PHYSICIAN_SEARCH_RESULT_TITLE = "Physician Search Result";
	private static final String TAXONOMY_CODE_SEARCH_RESULT = "Taxonomy Code Search Result";
	private static final String CLIENT_SPECIFIC_AFFILIATED_LICENSES = "Client Specific Affiliated Licenses";

	@BeforeMethod(alwaysRun = true)
	@Parameters({ "ssoUsername", "ssoPassword", "timeout" })
	public void beforeMethod(String ssoUsername, String ssoPassword, int timeout, Method method) {
		try {
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			xifinPortalUtils = new XifinPortalUtils(driver, config);
			logIntoSso(ssoUsername, ssoPassword);
			MenuNavigation menuNavigation = new MenuNavigation(driver, config);
	        menuNavigation.navigateToPhysicianLicensePage();
	        this.timeOut = timeout;
		} catch (Exception e) {
			logger.error("Error running BeforeMethod", e);
		}
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method) {
		try {
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			if (lstPhysXref != null && lstPhysXref.size() > 0) {
				for (PhysXref physXref : lstPhysXref) {
					xifinPortalUtils.deletePhysXref(physXref);
				}
			}
			if (lstPyrPhysExcl != null && lstPyrPhysExcl.size() > 0) {
				for (PyrPhysExcl pyrPhysExcl : lstPyrPhysExcl) {
					xifinPortalUtils.deletePyrPhysExcl(pyrPhysExcl);
				}
			}
			if (lstPyrGrpPhysExcl != null && lstPyrGrpPhysExcl.size() > 0) {
				for (PyrGrpPhysExcl pyrGrpPhysExcl : lstPyrGrpPhysExcl) {
					xifinPortalUtils.deletePyrGrpPhysExcl(pyrGrpPhysExcl);
				}
			}
			xifinPortalUtils.deletePhys(phys);
			xifinPortalUtils.deleteNpi(npi);
		} catch (Exception e) {
			logger.error("Error running afterMethod", e);
		}
	}

	@Test(priority = 1, description = "Update Physician License with input valid data")
	public void testXPR_998() throws Throwable {		
		logger.info("===== Testing - testXPR_998 =====");
		timeStamp = new TimeStamp();
		randomCharacter = new RandomCharacter();
		physicianSearch = new PhysicianSearch(driver);
		physicianLicense = new PhysicianLicense(driver);
		physicianLicenseResults = new PhysicianSearchResults(driver);
		Date currentDate = ConvertUtil.convertStringToSQLDate(timeStamp.getCurrentDate(DATE_FORMATTED_DEFAULT), DATE_FORMATTED_DEFAULT);

		logger.info("*** Prepare data: Create NPI and Phys.");
		phys = prepareData();

		logger.info("*** Step 1 Expected result: User login successful and Load NPI/UPIN page is displayed.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Action: Click on Search NPI/UPIN icon.");
		clickHiddenPageObject(physicianLicense.physSearchIcon(), 0);

		logger.info("*** Step 2 Expected result: Search Physician License page is displayed.");
		String physLicParent = switchToPopupWin();
		verifyPhysicianSearchPageIsDisplayed();

		logger.info("*** Step 3 Action: Input * at physician Name field. Click on Search button.");
		enterValues(physicianSearch.physSrchPageFirstNameInput(), ALL_VALUE);
		enterValues(physicianSearch.npiInput(), npi.getNpi());
		clickHiddenPageObject(physicianSearch.searchBtn(), 0);

		logger.info("*** Step 3 Expected result: Search result page is displayed.");
		verifyPhysicianLicenseResultsPageIsDisplayed();

		logger.info("*** Step 4 Action: Select any NPI hyperlink in Search Result page.");
		clickHiddenPageObject(physicianLicenseResults.physSrchResultsTable(2, 2), 0);

		logger.info("*** Step 4 Expected result: Search result page is closed and Physician License page is displayed.");
		switchToParentWin(physLicParent);
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(npi.getNpi()), EMPTY);

		logger.info("*** Step 5 Action: Physician License section: Change Name and Address. "
																+ "Taxonomy: Change new toxonomy Code."
																+ "Update Credential."
																+ "Change Postal Code");
		String oldPostal = getInputVal(physicianLicense.phyLicSectionPostalCodeInput());
		String oldTaxonomy = getInputVal(physicianLicense.taxonomyGrpTaxonomyCodeInput());
		String oldCredential = physicianLicense.phyLicSectionCredentialsText().getText();

		PhysicianLicenseSec expPhysLicSec = new PhysicianLicenseSec();
		expPhysLicSec.setfName(randomCharacter.getRandomAlphaString(5));
		expPhysLicSec.setlName(randomCharacter.getRandomAlphaString(5));
		expPhysLicSec.setAddress1(randomCharacter.getRandomAlphaNumericString(5));
		expPhysLicSec.setAddress2(randomCharacter.getRandomAlphaNumericString(5));
		expPhysLicSec.setTaxonomy(taxonomyDao.getTaxonomyByCondition(" and PK_TAXONOMY_CD not in ('"+oldTaxonomy+"') order by dbms_random.value", currentDate).get(0).getTaxonomyCd());
		expPhysLicSec.setCredential(physicianDao.getPhysCredTypByPhysicianNotInPhysCredTypId(oldCredential).getPhysCredTypId());
		expPhysLicSec.setPostalCode(zipDao.getRandomZipCodeByConditions(" and Pk_zip_id not in ('"+oldPostal+"') ").get(0));
		setRequiredValuesPhysLicSection(expPhysLicSec);

		logger.info("*** Step 5 Expected result: New Taxonomy Code is displayed in Taxonomy Section.");
		PhysicianLicenseSec actPhysLicSec = getValuesPhysicianLicenseSection();
		verifyChangeOfRequiredValuesInPhysicianLicenseSection(actPhysLicSec, expPhysLicSec);

		logger.info("*** Step 6 Action: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();

		logger.info("*** Step 6 Expected result: New data is saved in PHYS tbl.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();
		Phys physInfo = physicianDao.getPhysByNpiId(npi.getNpi());
		verifyPhysTblIsSavedCorrectly(expPhysLicSec, physInfo);

		driver.quit();
	}

	@Test(priority = 1, description = "Update Physician License with new Taxonomy Code is get from Search result")
	public void testXPR_999() throws Throwable {		
		logger.info("===== Testing - testXPR_999 =====");  	

		timeStamp = new TimeStamp();
		randomCharacter = new RandomCharacter();
		physicianSearch = new PhysicianSearch(driver);
		physicianLicense = new PhysicianLicense(driver);
		taxonomyCodeSearch = new TaxonomyCodeSearch(driver);
		physicianLicenseResults = new PhysicianSearchResults(driver);
		taxonomyCodeSearchResults = new TaxonomyCodeSearchResults(driver);
		Date currentDate = ConvertUtil.convertStringToSQLDate(timeStamp.getCurrentDate(DATE_FORMATTED_DEFAULT), DATE_FORMATTED_DEFAULT);

		logger.info("*** Prepare data: Create NPI and Phys.");
		phys = prepareData();

		logger.info("*** Step 1 Expected result: User login successful and Load NPI/UPIN page is displayed.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Action: Click on Search NPI/UPIN icon.");
		clickHiddenPageObject(physicianLicense.physSearchIcon(), 0);

		logger.info("*** Step 2 Expected result: Search Physician License page is displayed.");
		String parent = switchToPopupWin();
		verifyPhysicianSearchPageIsDisplayed();

		logger.info("*** Step 3 Action: Input * at physician Name field. Click on Search button.");
		enterValues(physicianSearch.physSrchPageFirstNameInput(), ALL_VALUE);
		enterValues(physicianSearch.npiInput(), npi.getNpi());
		clickHiddenPageObject(physicianSearch.searchBtn(), 0);

		logger.info("*** Step 3 Expected result: Search result page is displayed.");
		verifyPhysicianLicenseResultsPageIsDisplayed();

		logger.info("*** Step 4 Action: Select any NPI hyperlink in Search Result page.");
		clickHiddenPageObject(physicianLicenseResults.physSrchResultsTable(2, 2), 0);

		logger.info("*** Step 4 Expected result: Search result page is closed and Physician License page is displayed.");
		switchToParentWin(parent);
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(npi.getNpi()), EMPTY);

		logger.info("*** Step 5 Action: Physician License section: Taxonomy: Click on Search Taxonomy icon. Input * at taxonomy code field. Click on Search button.");
		clickHiddenPageObject(physicianLicense.taxonomyGrpTaxonomyCodeSearchIcon(), 0);
		parent = switchToPopupWin();
		verifyTaxonomyCodeSearchPageIsDisplayed();
		enterValues(taxonomyCodeSearch.taxCdSearchTaxIdInput(), ALL_VALUE);
		clickHiddenPageObject(taxonomyCodeSearch.taxCdSearchSearchBtn(), 0);

		verifyTaxonomyCodeSearchResultPageIsDisplayedCorrectly();
		String newTaxonomy = taxonomyCodeSearchResults.taxCdSearchResultDataLink(1).getText();
		String totalResults = ConvertUtil.convertDecimalFormat(Integer.parseInt(getTotalResultSearch(taxonomyCodeSearchResults.taxCdSearchResultTotalRecordsLbl())));
		clickHiddenPageObject(taxonomyCodeSearchResults.taxCdSearchResultDataLink(1), 0);

		logger.info("*** Step 5 Expected result: All Taxonomy Code are displayed on Search result.");
		switchToParentWin(parent);
		List<Taxonomy> taxonomy = taxonomyDao.getTaxonomyByCondition(EMPTY, currentDate);
		assertEquals(taxonomy.size(), Integer.parseInt(totalResults), "        The Taxonomy input is displayed.");
		assertEquals(newTaxonomy,getInputVal(physicianLicense.taxonomyGrpTaxonomyCodeInput()),"        Taxonomy Code is displayed correctly.");

		logger.info("*** Step 6 Action: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();

		logger.info("*** Step 6 Expected result: New data is saved in PHYS tbl.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();
		Phys physInfo = physicianDao.getPhysByNpiId(npi.getNpi());
		assertEquals(physInfo.getTaxonomyCd(), newTaxonomy,"        Taxonomy Code is saved.");

		driver.quit();
	}

	@Test(priority = 1, description = "Add multi State Licence with valid input")
	public void testXPR_1000() throws Throwable {		
		logger.info("===== Testing - testXPR_1000 =====");

		timeStamp = new TimeStamp();
		randomCharacter = new RandomCharacter();
		physicianSearch = new PhysicianSearch(driver);
		physicianLicense = new PhysicianLicense(driver);
		physicianLicenseUtils = new PhysicianLicenseUtils(driver);

		logger.info("*** Step 1 Expected result: User login successful and Load NPI/UPIN page is displayed.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Action: Load valid NPI.");
		phys = prepareData();		
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 2 Expected result: Search result page is closed and Physician License page is displayed.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		logger.info("*** Step 3 Action: State License: Add multi record.");
		setValueSearchInput(STATE_LICENSES);
		State state1 = stateDao.getStateFromZipByCtyNmNotNull();
		StateLicenseSec expStaLic1 = setExpectedStaLicSecRecord(state1);
		clickHiddenPageObject(physicianLicense.staLicSectionStaLicTblAddButton(), 0);
		setValuesStaLicSec(expStaLic1, FIRST_ROW);

		State state2 = stateDao.getStateFromZipByStateNotInStId(expStaLic1.getState());
		StateLicenseSec expStaLic2 = setExpectedStaLicSecRecord(state2);
		clickHiddenPageObject(physicianLicense.staLicSectionStaLicTblAddButton(), 0);
		setValuesStaLicSec(expStaLic2, SECOND_ROW);
		pressKeyShorcut(Keys.ENTER, EMPTY);

		logger.info("*** Step 3 Expected result: multi new records are displayed on tbl.");
		xifinPortalUtils.waitForPageLoaded(wait);
		verifyStaLicSectionDisplayedCorrectly(FIRST_ROW, expStaLic1);
		verifyStaLicSectionDisplayedCorrectly(SECOND_ROW, expStaLic2);

		logger.info("*** Step 4 Action: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();
		switchToPopupWin(5);
		assertTrue(physicianLicenseUtils.clickDoubleOnSaveAndClearBtn(this), "        The Save and Clear button is displayed.");

		logger.info("*** Step 4 Expected result: New data is saved in PHYS tbl.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 5 Action: Reload NPI.");
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 5 Expected result: New record are shown in the table. New records are added into PHYS_STA_LIC tbl");
		List<String>states = Arrays.asList(physicianLicense.staLicSectionStaLicTblStateColText(FIRST_ROW).getText(), physicianLicense.staLicSectionStaLicTblStateColText(SECOND_ROW).getText());
		List<String>statesLicIds = Arrays.asList(physicianLicense.staLicSectionStaLicTblLicenseColText(FIRST_ROW).getText(), physicianLicense.staLicSectionStaLicTblLicenseColText(SECOND_ROW).getText());
		assertTrue(states.contains(expStaLic1.getState())&&states.contains(expStaLic2.getState()), "        The States text are displayed.");
		assertTrue(statesLicIds.contains(expStaLic1.getLicense())&&statesLicIds.contains(expStaLic2.getLicense()), "        The License Ids are  displayed.");

		List<PhysStateLic> physStaLic = physicianDao.getPhysStateLicByPhysSeqId(phys.getSeqId());
		verifyDataIsSavedInPhysStaLicTblCorrectly(physStaLic, 0, expStaLic1);
		verifyDataIsSavedInPhysStaLicTblCorrectly(physStaLic, 1, expStaLic2);

		logger.info("*** Step 6 Action: Remove records.");
		clickHiddenPageObject(physicianLicense.staLicSectionStaLicTblDeleteColCheckbox(FIRST_ROW), 0);
		clickHiddenPageObject(physicianLicense.staLicSectionStaLicTblDeleteColCheckbox(SECOND_ROW), 0);
		clickOnSaveAndClearBtn();
		physStaLic = physicianDao.getPhysStateLicByPhysSeqId(phys.getSeqId());
		assertEquals(physStaLic.size(), 0, "        Removed records.");

		driver.quit();
	}

	@Test(priority = 1,description = "Load PhysicianLicense with valid UPIN.")
	public void testXPR_1001() throws Throwable{
		logger.info("==== Testing - testXPR_1001 ====");

		timeStamp = new TimeStamp();
		randomCharacter = new RandomCharacter();
		physicianLicense = new PhysicianLicense(driver);

		logger.info("*** Step 1 Expected results: User login successful and Load Physician Page is displayed.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Action: Load valid UPIN.");
		phys = prepareData();
		setValueUpinIdLookup(phys.getUpinId());

		logger.info("*** Step 2 Expected results: Physician License page is display with correct information.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(EMPTY, phys.getUpinId());

		Phys phyInfo = physicianDao.getPhysByUpinId(phys.getUpinId());
		verifyPhysicianLicensePageIsDisplayedDataCorrectly(phyInfo);

		driver.quit();
	}

	@Test(priority = 1,description = "Add multi Affiliated Licenses with valid input.")
	public void testXPR_1002() throws Throwable{
		logger.info("==== Testing - testXPR_1002 ====");

		timeStamp = new TimeStamp();
		randomCharacter = new RandomCharacter();
		physicianLicense = new PhysicianLicense(driver);

		logger.info("*** Step 1 Expected results: User login successful and Load Physician Page is displayed.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Action: Load valid NPI that not in Phys_State_Lic.");
		phys = prepareData();
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 2 Expected results: Physician License page is displayed with correct information.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		Phys phyInfo = physicianDao.getPhysByNpiId(phys.getNpiId());
		assertEquals(phyInfo.getPhysLname(), getInputVal(physicianLicense.phyLicSectionLastNameInput()), "        The phyLic Section Last Name Input should be correct data.");
		assertEquals(phyInfo.getPhysFname(), getInputVal(physicianLicense.phyLicSectionFirstNameInput()), "        The phyLic Section First Name Input should be correct data.");
		assertEquals(ConvertUtil.isNull(phyInfo.getAddr1(), EMPTY), getInputVal(physicianLicense.phyLicSectionAddressOneInput()), "        The phyLic Section Address 1 Input should be correct data.");
		assertEquals(countryDao.getCountryByCntryId(phyInfo.getCntryId()).getName(), physicianLicense.phyLicCountryDropDown().getText(), "        The phyLic Country should be correct data.");
		assertEquals(ConvertUtil.isNull(phyInfo.getAddr2(), EMPTY), getInputVal(physicianLicense.phyLicSectionAddressTwoInput()), "        The phyLic Section Address 2 Input should be correct data.");
		assertEquals(ConvertUtil.isNull(phyInfo.getCity(), EMPTY), getInputVal(physicianLicense.phyLicSectionCityInput()), "        The phyLic Section City Input should be correct data.");
		assertEquals(phyInfo.getZipId().replace("-", EMPTY), getInputVal(physicianLicense.phyLicSectionPostalCodeInput()).replace("-", EMPTY), "        The phyLic Section Postal Code Search input should be correct data.");
		assertEquals(ConvertUtil.isNull(phyInfo.getStId(), EMPTY), getInputVal(physicianLicense.phyLicSectionStateInput()), "        The phyLic Section State Input should be correct data.");
		assertEquals(phyInfo.getOverrideSvcFacId() == 0 ? EMPTY : rpmDao.getFacByFacId(null, phyInfo.getOverrideSvcFacId()).getName(), physicianLicense.phyLicSectionOverideServiceFacilityLocationDropDown().getText(), "        The phyLic Section Overide Service Facility Location should be correct data.");
		assertEquals(EMPTY, physicianLicense.phyLicSectionAddressText().getText(), "        The phyLicSectionAddressText should be correct data.");
		assertEquals(EMPTY, physicianLicense.phyLicSectionNpiText().getText(), "        The phyLic Section Npi Text should be correct data.");
		assertEquals(EMPTY, physicianLicense.phyLicSectionTaxIdText().getText(), "        The phyLicSectionTaxId Text should be correct data.");
		assertEquals(physicianDao.getPhysSpecTypByPhysSpecId(phyInfo.getPhysSpecId()).getDescr().trim(), physicianLicense.phyLicSectionSpecialtyDropDown().getText(), "        The phyLic Section Specialty should be correct data.");
		assertEquals(phyInfo.getPhysCredId(), physicianLicense.phyLicSectionCredentialsText().getText(), "        The phyLic Section Credentials should be correct data.");
		assertEquals(ConvertUtil.isNull(phyInfo.getTaxId(), EMPTY), physicianLicense.taxId().getText(), "        The phyLic Section Specialty should be correct data.");

		/* Taxonomy Group */
		if (phyInfo.getTaxonomyCd() == null) {
			assertEquals(ConvertUtil.isNull(phyInfo.getTaxonomyCd(), EMPTY), getInputVal(physicianLicense.taxonomyGrpTaxonomyCodeInput()), "        The Taxonomy Grp Taxonomy Code Input should be correct data.");
		}
		if (phyInfo.getTaxonomyCd() != null) {
			assertEquals(phyInfo.getTaxonomyCd(), getInputVal(physicianLicense.taxonomyGrpTaxonomyCodeInput()), "        The Taxonomy Grp Taxonomy Code Input should be correct data.");
			Taxonomy taxonomyInfo = taxonomyDao.getTaxonomyByTaxonomyCd(phyInfo.getTaxonomyCd());
			assertEquals(ConvertUtil.isNull(taxonomyInfo.getProviderTypDescr(), EMPTY), physicianLicense.taxonomyGrpProviderTypeDescriptionText().getText(), "        The Taxonomy Grp Provider Type Description Text should be correct data.");
			assertEquals(ConvertUtil.isNull(taxonomyInfo.getClassification(), EMPTY), physicianLicense.taxonomyGrpClassificationText().getText(), "        The Taxonomy Grp Classification Text should be correct data.");
			assertEquals(ConvertUtil.isNull(taxonomyInfo.getSpecialization(), EMPTY), physicianLicense.taxonomyGrpSpecializationText().getText(), "        The Taxonomy Grp Specialization Text should be correct data.");
		}

		/* Taxonomy Group */
		assertEquals(EMPTY, physicianLicense.taxonomyGrpTaxonomyCodeInput().getText(), "        The Taxonomy Grp Taxonomy Code Input should be correct data.");
		//  State Licenses
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.staLicSectionStaLicTblTotalRecord()), "        No record is shown in table.");
		//  Affiliated Licenses
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.affLicTblTotalResultText()), "        No record is shown in table.");
		//  Client Specific Affiliated Licenses
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.clnSpeAffLicTblTotalResultText()), "        No record is shown in table.");
		//  OIG Exclusions
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.oigExclusTblTotalResultText()), "        No record is shown in table.");
		//  Payor Group Exclusion
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.pyrGrpExclNavBar()), "        No record is shown in table.");
		//  Payor Exclusion
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.pyrExclNavBar()), "        No record is shown in table.");
		//  Physician Cross References
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.physicianCrossReferencesNavBar()), "        No record is shown in table.");
		//  Internal Note
		assertEquals(EMPTY, getTotalResultSearch(physicianLicense.internalNote()), "        Internal Note in table should be empty.");

		logger.info("*** Step 3 Actions: Affiliated Licenses: Add multi payor licenses.");		
		setValueSearchInput(AFFILIATED_LICENSES);
		AffiliatedLicense affiliatedLicense1 = addNewRecordInAffiliatedLicenseGrid(EMPTY);
		AffiliatedLicense affiliatedLicense2 = addNewRecordInAffiliatedLicenseGrid(affiliatedLicense1.getPyrAbbrv());

		logger.info("*** Step 3 Expected Results: Multi new records are displayed in table.");
		verifyDataIsShownIntoAffiliatedLicenseGridCorrectly(affiliatedLicense1);
		verifyDataIsShownIntoAffiliatedLicenseGridCorrectly(affiliatedLicense2);

		logger.info("*** Step 4 Actions: Click on Save and Clear Button.");
		clickOnSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: System will take user return Load Physician page. New records are added in PHYS_PYR_LIC table.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();
		PhysPyrLic newRecord = physicianDao.getPhysPyrLicByPyrIdPyrLicTypAndProviderId(affiliatedLicense1.getPyrId() ,affiliatedLicense1.getLicTypId() ,affiliatedLicense1.getLicId() );
		assertNotNull(newRecord, "        A record is added in PHYS_PYR_LIC table with correct data.");
		PhysPyrLic newRecord2 = physicianDao.getPhysPyrLicByPyrIdPyrLicTypAndProviderId(affiliatedLicense2.getPyrId(), affiliatedLicense2.getLicTypId(), affiliatedLicense2.getLicId() );
		assertNotNull(newRecord2, "        A record is added in PHYS_PYR_LIC table with correct data.");

		logger.info("*** Step 5 Actions: Reload NPI.");
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 5 Expected Results: New records are shown in the table.");
		setValueSearchInput(AFFILIATED_LICENSES);
		verifyDataIsShownIntoAffiliatedLicenseGridCorrectly(affiliatedLicense1);
		verifyDataIsShownIntoAffiliatedLicenseGridCorrectly(affiliatedLicense2);

		logger.info("*** Step 6 Actions: Remove new records.");
		cleanPhysPyrLic(newRecord.getPhysSeqId());

		driver.quit();
	}

	@Test(priority = 1,description = "Add multi Client Specific Affiliated Licenses with valid input.")
	public void testXPR_1003() throws Throwable{
		logger.info("==== Testing - testXPR_1003 ====");

		timeStamp = new TimeStamp();
		randomCharacter = new RandomCharacter();
		physicianLicense = new PhysicianLicense(driver);

		logger.info("*** Step 1 Expected results: User login successful and Load Physician Page is displayed.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Action: Load valid NPI that not in Phys_State_Lic.");
		phys = prepareData();
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 2 Expected results: Physician License page is display with correct information.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		Phys phyInfo = physicianDao.getPhysByNpiId(phys.getNpiId());
		assertEquals(phyInfo.getPhysLname(), getInputVal(physicianLicense.phyLicSectionLastNameInput()), "        The phyLic Section Last Name Input should be correct data.");
		assertEquals(phyInfo.getPhysFname(), getInputVal(physicianLicense.phyLicSectionFirstNameInput()), "        The phyLic Section First Name Input should be correct data.");
		assertEquals(phyInfo.getAddr1(), getInputVal(physicianLicense.phyLicSectionAddressOneInput()), "        The phyLic Section Address 1 Input should be correct data.");
		assertEquals(countryDao.getCountryByCntryId(phyInfo.getCntryId()).getName(), physicianLicense.phyLicCountryDropDown().getText(), "        The phyLic Country should be correct data.");
		assertEquals(ConvertUtil.isNull(phyInfo.getAddr2(), EMPTY), getInputVal(physicianLicense.phyLicSectionAddressTwoInput()), "        The phyLic Section Address 2 Input should be correct data.");
		assertEquals(ConvertUtil.isNull(phyInfo.getCity(), EMPTY), getInputVal(physicianLicense.phyLicSectionCityInput()), "        The phyLic Section City Input should be correct data.");
		assertEquals(phyInfo.getZipId().replace("-", EMPTY), getInputVal(physicianLicense.phyLicSectionPostalCodeInput()).replace("-", EMPTY), "        The phyLic Section Postal Code Search input should be correct data.");
		assertEquals(ConvertUtil.isNull(phyInfo.getStId(), EMPTY), physicianLicense.phyLicSectionStateInput().getAttribute("value"), "        The phyLic Section State Input should be correct data.");
		assertEquals(phyInfo.getOverrideSvcFacId() == 0 ? EMPTY : rpmDao.getFacByFacId(null, phyInfo.getOverrideSvcFacId()).getName(), physicianLicense.phyLicSectionOverideServiceFacilityLocationDropDown().getText(), "        The phyLic Section Overide Service Facility Location should be correct data.");
		assertEquals(EMPTY, physicianLicense.phyLicSectionAddressText().getText(), "        The phyLicSectionAddressText should be correct data.");
		assertEquals(EMPTY, physicianLicense.phyLicSectionNpiText().getText(), "        The phyLic Section Npi Text should be correct data.");
		assertEquals(EMPTY, physicianLicense.phyLicSectionTaxIdText().getText(), "        The phyLicSectionTaxId Text should be correct data.");
		assertEquals(physicianDao.getPhysSpecTypByPhysSpecId(phyInfo.getPhysSpecId()).getDescr().trim(), physicianLicense.phyLicSectionSpecialtyDropDown().getText(), "        The phyLic Section Specialty should be correct data.");
		assertEquals(ConvertUtil.isNull(phyInfo.getPhysCredId(), EMPTY), physicianLicense.phyLicSectionCredentialsText().getText(), "        The phyLic Section Credentials should be correct data.");
		assertEquals(ConvertUtil.isNull(phyInfo.getTaxId(), EMPTY), physicianLicense.taxId().getText(), "        The phyLic Section Specialty should be correct data.");

		/* Taxonomy Group */
		if (phyInfo.getTaxonomyCd() == null) {
			assertEquals(ConvertUtil.isNull(phyInfo.getTaxonomyCd(), EMPTY), getInputVal(physicianLicense.taxonomyGrpTaxonomyCodeInput()), "        The Taxonomy Grp Taxonomy Code Input should be correct data.");
		}

		if (phyInfo.getTaxonomyCd() != null) {
			assertEquals(phyInfo.getTaxonomyCd(), physicianLicense.taxonomyGrpTaxonomyCodeInput().getAttribute("value"), "        The Taxonomy Grp Taxonomy Code Input should be correct data.");
			Taxonomy taxonomyInfo = taxonomyDao.getTaxonomyByTaxonomyCd(phyInfo.getTaxonomyCd());
			assertEquals(ConvertUtil.isNull(taxonomyInfo.getProviderTypDescr(), EMPTY), physicianLicense.taxonomyGrpProviderTypeDescriptionText().getText(), "        The Taxonomy Grp Provider Type Description Text should be correct data.");
			assertEquals(ConvertUtil.isNull(taxonomyInfo.getClassification(), EMPTY), physicianLicense.taxonomyGrpClassificationText().getText(), "        The Taxonomy Grp Classification Text should be correct data.");
			assertEquals(ConvertUtil.isNull(taxonomyInfo.getSpecialization(), EMPTY), physicianLicense.taxonomyGrpSpecializationText().getText(), "        The Taxonomy Grp Specialization Text should be correct data.");
			
		}
		assertEquals(EMPTY, physicianLicense.taxonomyGrpTaxonomyCodeInput().getText(), "        The Taxonomy Grp Taxonomy Code Input should be correct data.");
		//  State Licenses
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.staLicSectionStaLicTblTotalRecord()), "        No record is shown in table.");
		//  Affiliated Licenses
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.affLicTblTotalResultText()), "        No record is shown in table.");
		//  Client Specific Affiliated Licenses
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.clnSpeAffLicTblTotalResultText()), "        No record is shown in table.");
		//  OIG Exclusions
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.oigExclusTblTotalResultText()), "        No record is shown in table.");
		//  Payor Group Exclusion
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.pyrGrpExclNavBar()), "        No record is shown in table.");
		//  Payor Exclusion
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.pyrExclNavBar()), "        No record is shown in table.");
		//  Physician Cross References
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.physicianCrossReferencesNavBar()), "        No record is shown in table.");
		//  Internal Note
		assertEquals(EMPTY, getTotalResultSearch(physicianLicense.internalNote()), "        Internal Note in table should be empty.");

		logger.info("*** Step 3 Actions: Client Specific Affiliated Licenses: Add multi Client Specific Affiliated Licenses.");		
		PayorOverrideRemit pyrInfo1 = payorDao.getPayorOverrideRemit().get(0);
		PayorOverrideRemit pyrInfo2 = payorDao.getPayorOverrideRemit().get(0);
		while (pyrInfo1.getPyrAbbrv().equals(pyrInfo2.getPyrAbbrv())) {
			pyrInfo2 = payorDao.getPayorOverrideRemit().get(0);
		}
		List<String> clnInfo = clientDao.getClnInfoFromClnAndFacAndCountryAndClnSubm();

		//add the first record
		clickHiddenPageObject(physicianLicense.clnSpeAffLicTblAddBtn(), 0);
		enterValues(physicianLicense.clnSpeAffLicTblPyrIdColInput(), pyrInfo1.getPyrAbbrv());
		String random = randomCharacter.getRandomNumericString(4);
		enterValues(physicianLicense.clnSpeAffLicTblLicenseIdColInput(), random);
		enterValues(physicianLicense.clnSpeAffLicTblClnIdColInput(), clnInfo.get(0));
		//add the second record
		clickHiddenPageObject(physicianLicense.clnSpeAffLicTblAddBtn(), 0);
		enterValues(physicianLicense.clnSpeAffLicTblPyrIdColInput(), pyrInfo2.getPyrAbbrv());
		String random2 = randomCharacter.getRandomNumericString(4);
		enterValues(physicianLicense.clnSpeAffLicTblLicenseIdColInput(), random2 );
		enterValues(physicianLicense.clnSpeAffLicTblClnIdColInput(), clnInfo.get(0));

		logger.info("*** Step 3 Expected Results: Multi new records are displayed in table.");
		enterValues(physicianLicense.clnSpeAffLicTblPyrIdFilterInput(), pyrInfo1.getPyrAbbrv() );
		enterValues(physicianLicense.clnSpeAffLicTblClnIdFilterInput(), clnInfo.get(0)  );
		enterValues(physicianLicense.clnSpeAffLicTblLicenseIdFilterInput(), random);
		assertEquals(ONE_RECORD, getTotalResultSearch(physicianLicense.clnSpeAffLicTblTotalResultText()), "        A record is shown in table.");

		enterValues(physicianLicense.clnSpeAffLicTblPyrIdFilterInput(), pyrInfo2.getPyrAbbrv());
		enterValues(physicianLicense.clnSpeAffLicTblClnIdFilterInput(), clnInfo.get(0));
		enterValues(physicianLicense.clnSpeAffLicTblLicenseIdFilterInput(), random2);
		assertEquals(ONE_RECORD, getTotalResultSearch(physicianLicense.clnSpeAffLicTblTotalResultText()), "        A record is shown in table.");

		logger.info("*** Step 4 Actions: Click on Save and Clear Button.");
		clickOnSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: System will take user return Load Physician page. New records are added in PHYS_PYR_CLN_LIC table.");
		assertEquals(PHYSICIAN_LICENSE_TITLE, physicianLicense.physicianTitle().getText(), "        The title of load Physician License page should be 'Physician License'.");
		PhysPyrClnLic newRecord = physicianDao.getPhysPyrClnLicByClnIdPyrIdAndProviderId(Integer.parseInt(clnInfo.get(1)),pyrInfo1.getPyrId(),random);
		assertNotNull(newRecord, "        A records are added in PHYS_PYR_CLN_LIC table with correct data.");
		PhysPyrClnLic newRecord2 = physicianDao.getPhysPyrClnLicByClnIdPyrIdAndProviderId(Integer.parseInt(clnInfo.get(1)),pyrInfo2.getPyrId(),random2);
		assertNotNull(newRecord2, "        A records are added in PHYS_PYR_CLN_LIC table with correct data.");

		logger.info("*** Step 5 Actions: Reload NPI.");
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 5 Expected Results: New records are shown in the table.");
		enterValues(physicianLicense.clnSpeAffLicTblPyrIdFilterInput(), pyrInfo1.getPyrAbbrv());
		enterValues(physicianLicense.clnSpeAffLicTblClnIdFilterInput(), clnInfo.get(0));
		enterValues(physicianLicense.clnSpeAffLicTblLicenseIdFilterInput(), random);
		assertEquals(ONE_RECORD, getTotalResultSearch(physicianLicense.clnSpeAffLicTblTotalResultText()), "        A record is shown in table.");

		enterValues(physicianLicense.clnSpeAffLicTblPyrIdFilterInput(), pyrInfo2.getPyrAbbrv());
		enterValues(physicianLicense.clnSpeAffLicTblClnIdFilterInput(), clnInfo.get(0));
		enterValues(physicianLicense.clnSpeAffLicTblLicenseIdFilterInput(), random2);
		assertEquals(ONE_RECORD, getTotalResultSearch(physicianLicense.clnSpeAffLicTblTotalResultText()), "        A record is shown in table.");

		logger.info("*** Step 6 Actions: Remove new records.");
		cleanPhysPyrClnLic(newRecord.getPhysSeqId());
		cleanPhysPyrClnLic(newRecord2.getPhysSeqId());
		updatePhys(phys.getSeqId(), phys.getOverrideSvcFacId());

		driver.quit();
	}
	
	@Test(priority = 1, description = "Add multi Payor Group Exclusion with valid input.")
	public void testXPR_1004() throws Throwable {
		logger.info("===== Testing - testXPR_1004 =====");  	

		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);		
		physicianLicense = new PhysicianLicense(driver);

		logger.info("*** Step 1 Expected Results: User login successful and Load Physician page is displayed.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Actions: Load valid NPI that not in Phys_State_Lic.");
		phys = prepareData();
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 2 Expected Results: Physician License page is displayed correct information.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		logger.info("*** Step 3 Actions: Payor Group Exclusion: Add multi payor group exclusion.");
		setValueSearchInput(PAYOR_GROUP_EXCLUSION);
		String effDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(1, 5));
		String expDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(6, 10));
		PayorGroupExclusion payorGroupExclusion1 = addNewRecordInPayorGroupExclusionGrid(EMPTY, effDate, expDate);
		effDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(11, 15));
		expDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(16, 20));
		PayorGroupExclusion payorGroupExclusion2 = addNewRecordInPayorGroupExclusionGrid(payorGroupExclusion1.getGroupName(), effDate, expDate);

		logger.info("*** Step 3 Expected Results: Multi new records are displayed in table.");
		verifyDataIsShownIntoPayorGroupExclusionGridCorrectly(payorGroupExclusion1);
		verifyDataIsShownIntoPayorGroupExclusionGridCorrectly(payorGroupExclusion2);

		logger.info("*** Step 4 Actions: Click on Save and Clear Button.");
		clickOnSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: System will take user return Load Physician page. New records are added in PYR_GRP_PHYS_EXCL table.");
		lstPyrGrpPhysExcl = payorDao.getPyrGrpPhysExclByConditions("WHERE FK_PHYS_SEQ_ID="+ phys.getSeqId());
		assertTrue(lstPyrGrpPhysExcl.size() > 0, "        New records are added in PYR_GRP_PHYS_EXCL table.");

		logger.info("*** Step 5 Actions: Reload NPI.");
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 5 Expected Results: New records are shown in the table.");
		setValueSearchInput(PAYOR_GROUP_EXCLUSION);
		verifyDataIsShownIntoPayorGroupExclusionGridCorrectly(payorGroupExclusion1);
		verifyDataIsShownIntoPayorGroupExclusionGridCorrectly(payorGroupExclusion2);

		driver.quit();
	}

	@Test(priority = 1, description = "Add multi Payor Exclusion with valid input.")
	public void testXPR_1005() throws Throwable {
		logger.info("===== Testing - testXPR_1005 =====");  	

		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);		
		physicianLicense = new PhysicianLicense(driver);

		logger.info("*** Step 1 Expected Results: User login successful and Load Physician page is displayed.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Actions: Load valid NPI that not in Phys_State_Lic.");
		phys = prepareData();
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 2 Expected Results: Physician License page is displayed correct information.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		logger.info("*** Step 3 Actions: Payor Exclusion: Add multi payor exclusion.");
		setValueSearchInput(PAYOR_EXCLUSION);
		String effDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(1, 5));
		String expDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(6, 10));
		PayorExclusion payorExclusion1 = addNewRecordInPayorExclusionGrid(EMPTY, effDate, expDate);
		effDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(11, 15));
		expDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(16, 20));
		PayorExclusion payorExclusion2 = addNewRecordInPayorExclusionGrid(payorExclusion1.getPyrAbbrv().trim(), effDate, expDate);

		logger.info("*** Step 3 Expected Results: Multi new records are displayed in table.");
		verifyDataIsShownIntoPayorExclusionGridCorrectly(payorExclusion1);
		verifyDataIsShownIntoPayorExclusionGridCorrectly(payorExclusion2);

		logger.info("*** Step 4 Actions: Click on Save and Clear Button.");
		clickOnSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: System will take user return Load Physician page. New records are added in PYR_PHYS_EXCL table.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();
		lstPyrPhysExcl = payorDao.getPyrPhysExclByConditions(" WHERE FK_PHYS_SEQ_ID=" + phys.getSeqId());
		assertTrue(lstPyrPhysExcl.size() > 0, "        New records are added in PYR_PHYS_EXCL table.");

		logger.info("*** Step 5 Actions: Reload NPI.");
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 5 Expected Results: New records are shown in the table.");
		setValueSearchInput(PAYOR_EXCLUSION);
		verifyDataIsShownIntoPayorExclusionGridCorrectly(payorExclusion1);
		verifyDataIsShownIntoPayorExclusionGridCorrectly(payorExclusion2);

		driver.quit();
	}

	@Test(priority = 1, description = "Add multi Physician Cross Reference with valid input.")
	public void testXPR_1006() throws Throwable {
		logger.info("===== Testing - testXPR_1006 =====");  	

		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);		
		physicianLicense = new PhysicianLicense(driver);		

		logger.info("*** Step 1 Expected Results: User login successful and Load Physician page is displayed.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Actions: Load valid NPI that not in Phys_State_Lic.");
		phys = prepareData();
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 2 Expected Results: Physician License page is displayed correct information.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		logger.info("*** Step 3 Actions: Physician Cross Reference: Add multi Physician Cross Reference.");
		setValueSearchInput(PHYSICIAN_CROSS_REFERENCES);
		String effDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(1, 5));
		String expDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(6, 10));
		PhysicianCrossReference physicianCrossReference1 = addNewRecordPhysicianCrossReferenceGrid(EMPTY, effDate, expDate);
		effDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(11, 15));
		expDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, RandomCharacter.getRandomIntInRange(16, 20));
		PhysicianCrossReference physicianCrossReference2 = addNewRecordPhysicianCrossReferenceGrid(physicianCrossReference1.getDesc().trim(), effDate, expDate);
		logger.info("*** TYPE 1: " + physicianCrossReference1.getDesc());
		logger.info("*** TYPE 2: " + physicianCrossReference2.getDesc());

		logger.info("*** Step 3 Expected Results: Multi new records are displayed in table.");
		verifyDataIsShownIntoAffiliatedLicenseGridCorrectly(physicianCrossReference1);
		verifyDataIsShownIntoAffiliatedLicenseGridCorrectly(physicianCrossReference2);

		logger.info("*** Step 4 Actions: Click on Save and Clear Button.");
		clickOnSaveAndClearBtn();
		Thread.sleep(2000);

		logger.info("*** Step 4 Expected Results: System will take user return Load Physician page. New records are added in PYR_PHYS_EXCL table.");
		lstPhysXref = physicianDao.getPhysXrefByPhysSeqId(phys.getSeqId());
		assertTrue(lstPhysXref.size() > 0, "        New records are added in PHYS_XREF table.");

		logger.info("*** Step 5 Actions: Reload NPI.");
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 5 Expected Results: New records are shown in the table.");
		setValueSearchInput(PHYSICIAN_CROSS_REFERENCES);
		verifyDataIsShownIntoAffiliatedLicenseGridCorrectly(physicianCrossReference1);
		verifyDataIsShownIntoAffiliatedLicenseGridCorrectly(physicianCrossReference2);

		driver.quit();
	}

	@Test(priority = 1, description = "Delete record in table")
	public void testXPR_1007() throws Throwable {		
		logger.info("===== Testing - testXPR_1007 =====");  	

		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);		
		physicianLicense = new PhysicianLicense(driver);
		physicianLicenseUtils = new PhysicianLicenseUtils(driver);

		logger.info("*** Step 1 Expected Results: User login successful. The Physician License page is shown.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Actions: Load valid NPI that not in PHYS_STATE_LIC.");
		phys = prepareData();
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 2 Expected Results: Physician License page is displayed with correct information.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		logger.info("*** Step 3 Actions: Add new record in all tables.");
		/*State Licenses*/
		String randomLic = randomCharacter.getNonZeroRandomNumericString(10);
		setValueSearchInput(STATE_LICENSES);
		String state1License = randomCharacter.getRandomAlphaNumericString(10);
		State state1 = stateDao.getStateFromZipByCtyNmNotNull();
		clickHiddenPageObject(physicianLicense.staLicSectionStaLicTblAddButton(), 0);
		enterValues(physicianLicense.staLicSectionStaLicTblLicenseColInput(FIRST_ROW), state1License);
		selectDropDown(physicianLicense.staLicSectionStaLicTblStateColDropDown(FIRST_ROW), state1.getName());
		pressKeyShorcut(Keys.ENTER, EMPTY);

		/*Affiliated Licenses*/
		setValueSearchInput(AFFILIATED_LICENSES);
		clickOnElement(physicianLicense.affLicTblAddBtn());
		String pyr = payorDao.getPyrWherePyrAbbrevIsNotUCHC().getPyrAbbrv();
		enterValues(physicianLicense.affLicTblPyrIdColInput(), pyr);
		selectFromDropdown(physicianLicense.affLicTblLicenseTypIdColDropdownList(), AFFILIATED_NUMBER);
		clickOnElement(physicianLicense.affLicTblLicenseIdColText(LAST_ROW));
		enterValues(physicianLicense.affLicTblLicenseIdColInput(), randomLic);
		clickOnElement(physicianLicense.physicianTitle());

		/*Client Specific Affiliated Licenses*/
		setValueSearchInput(CLIENT_SPECIFIC_AFFILIATED_LICENSES);
		clickOnElement(physicianLicense.clnSpeAffLicTblAddBtn());
		enterValues(physicianLicense.clnSpeAffLicTblPyrIdColInput(), pyr);
		String clnID = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		enterValues(physicianLicense.clnSpeAffLicTblClnIdColInput(), clnID);
		enterValues(physicianLicense.clnSpeAffLicTblLicenseIdColInput(), randomLic);
		clickOnElement(physicianLicense.physicianTitle());

		/*OIG Exclusions*/
		setValueSearchInput(OIG_EXCLUSION);
		String effDate = timeStamp.getCurrentDate(DATE_FORMATTED_DEFAULT);	
		String expDate = timeStamp.getFutureDate(DATE_FORMATTED_DEFAULT, 2);	
		clickOnElement(physicianLicense.oigExclusTblAddBtn());
		enterValues(physicianLicense.oigExclusTblEffectiveDtColInput(), effDate);
		enterValues(physicianLicense.oigExclusTblExpirationDtColInput(), expDate);
		clickOnElement(physicianLicense.physicianTitle());

		/*Payor Group Exclusion*/
		setValueSearchInput(PAYOR_GROUP_EXCLUSION);
		PyrGrp pyrGrp = payorDao.getRandomPyrGrpByNonClnNonPt();
		clickOnElement(physicianLicense.addNewPayorGroupExclusionbtn());
		enterValues(physicianLicense.effDateOnPayorGroupExclTblInput(LAST_ROW), effDate);
		enterValues(physicianLicense.expDateOnPayorGroupExclTblInput(LAST_ROW), expDate);
		selectDropDown(physicianLicense.pyrGrpIdOnPayorGroupExclTbl(LAST_ROW), pyrGrp.getGrpNm());
		clickOnElement(physicianLicense.physicianTitle());

		/*Payor Exclusion*/
		setValueSearchInput(PAYOR_EXCLUSION);
		clickOnElement(physicianLicense.addNewPayorExclusionbtn());
		enterValues(physicianLicense.effDateOnPayorExclTblInput(LAST_ROW), effDate);
		enterValues(physicianLicense.expDateOnPayorExclTblInput(LAST_ROW), expDate);
		clickOnElement(physicianLicense.pyrIdCellOnPayorExclTbl(LAST_ROW));
		enterValues(physicianLicense.pyrIdInputOnPayorExclTbl(LAST_ROW), pyr);
		clickOnElement(physicianLicense.physicianTitle());

		 /*Physician Cross References*/
		setValueSearchInput(PHYSICIAN_CROSS_REFERENCES);
		clickOnElement(physicianLicense.addPhysicianCrossReferencesbtn());
		enterValues(physicianLicense.effDateOnCrossReferencesTblInput(LAST_ROW), effDate);
		enterValues(physicianLicense.expDateOnCrossReferencesTblInput(LAST_ROW), expDate);

		List<String> xrefInfo = crossReferenceDao.getXrefFromXrefAndConditionByXrefCat("Physician","AND A.PK_XREF_ID not in (Select PK_XREF_ID from PHYS_XREF)");
		enterValues(physicianLicense.xrefOnCrossReferencesTbl(LAST_ROW), xrefInfo.get(0));
		clickOnElement(physicianLicense.physicianTitle());

		/*Internal Note*/
		setValueSearchInput(INTERNAL_NOTE);
		enterValues(physicianLicense.internalNote(), randomLic);

		logger.info("*** Step 3 Expected Result: New record is shown in table.");
		assertEquals(physicianLicense.affLicTblLicenseIdColText(LAST_ROW).getText(),randomLic,"        New record is shown in table.");
		assertEquals(physicianLicense.clnSpeAffLicTblLicenseIdColText(LAST_ROW).getText(),randomLic,"        New record is shown in table.");
		assertEquals(physicianLicense.oigExclusTblExpirationDtColText(LAST_ROW).getText(),expDate,"        New record is shown in table.");
		assertEquals(physicianLicense.staLicSectionStaLicTblLicenseColText(FIRST_ROW).getText(), state1License, "        State License is displayed");
		assertEquals(physicianLicense.staLicSectionStaLicTblStateColText(FIRST_ROW).getText(), state1.getName(), "        Physician License page is displayed");
		assertEquals(physicianLicense.expDateOnPayorGroupExclTbl(LAST_ROW).getText(),expDate,"        New record is shown in table.");
		assertEquals(physicianLicense.xrefTextOnCrossReferencesTbl(LAST_ROW).getText(),xrefInfo.get(0),"        New record is shown in table.");
		assertEquals(physicianLicense.expDateOnPayorExclTbl(LAST_ROW).getText(),expDate,"        New record is shown in table.");

		logger.info("*** Step 4 Action: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();
		physicianLicenseUtils.setPhysicianLicense(physicianLicense);
		physicianLicenseUtils.clickDoubleOnSaveAndClearBtn(this);

		logger.info("*** Step 4 Expected Result: System take user return to the Load Phys, New record is added in PHYS_STATE_LIC, PHYS_PYR_LIC, PHYS_PYR_CLN_LIC, PYR_GRP_PHYS_EXCL, PYR_PHYS_EXCL, PHYS_XREF.");
		List<PhysStateLic> physStateLic1 = physicianDao.getPhysStateLicByPhysSeqId(phys.getSeqId());
		List<PhysPyrClnLic> physPyrClnLic1 = physicianDao.getPhysPyrClnLicByPhysSeqId(phys.getSeqId());
		List<PyrGrpPhysExcl> pyrGrpPhysExcl1 = payorDao.getPyrGrpPhysExclByConditions(" WHERE FK_PHYS_SEQ_ID = '" + phys.getSeqId() + "'");
		List<PyrPhysExcl> pyrPhysExcl1 = payorDao.getPyrPhysExclByConditions(" WHERE FK_PHYS_SEQ_ID = '" + phys.getSeqId() + "'");
		List<PhysXref> physXref1 = physicianDao.getPhysXrefByPhysSeqId(phys.getSeqId());

		assertTrue(physXref1.size() > 0, "        Data should be added to PHYS_XREF.");
		assertTrue(pyrPhysExcl1.size() > 0, "        Data should be added to PYR_PHYS_EXCL.");
		assertTrue(physStateLic1.size() > 0, "        Data should be added to PHYS_STATE_LIC.");
		assertTrue(physPyrClnLic1.size() > 0, "        Data should be added to PHYS_PYR_CLN_LIC.");
		assertTrue(pyrGrpPhysExcl1.size() > 0, "        Data should be added to PYR_GRP_PHYS_EXCL.");

		logger.info("*** Step 5 Actions: Reload NPI.");
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 5 Expected Results: Physician License Page is displayed.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		logger.info("*** Step 6 Actions: Check on Delete check box for all tables.");
		/*State Licenses*/
		setValueSearchInput(STATE_LICENSES);
		clickOnElement(physicianLicense.staLicSectionStaLicTblDeleteColCheckbox(LAST_ROW));
		boolean isRowOnStaLicMarkedForDelete = rowIsMarkForDelete(physicianLicense.staLicSectionStaLicTbl(), LAST_ROW);

		/*Affiliated Licenses*/
		setValueSearchInput(AFFILIATED_LICENSES);
		clickOnElement(physicianLicense.affLicTblDeletedColCheckbox(LAST_ROW));
		boolean isRowOnAffLicMarkedForDelete = rowIsMarkForDelete(physicianLicense.affLicTbl(), LAST_ROW);

		/*Client Specific Affiliated Licenses*/
		setValueSearchInput(CLIENT_SPECIFIC_AFFILIATED_LICENSES);
		clickOnElement(physicianLicense.clnSpeAffLicTblDeletedColCheckbox(LAST_ROW));
		boolean isRowOnClnSpeAffLicMarkedForDelete = rowIsMarkForDelete(physicianLicense.clnSpeAffLicTbl(), LAST_ROW);

		/*OIG Exclusions*/
		setValueSearchInput(OIG_EXCLUSION);
		clickOnElement(physicianLicense.oigExclusTblDeletedColCheckbox(LAST_ROW));
		boolean isRowOnOIGExclMarkedForDelete = rowIsMarkForDelete(physicianLicense.oigExclusTbl(), LAST_ROW);

		/*Payor Group Exclusion*/
		setValueSearchInput(PAYOR_GROUP_EXCLUSION);
		clickOnElement(physicianLicense.deleteChkboxOnPayorGroupExclTbl(LAST_ROW));
		boolean isRowOnPyrGrpExclMarkedForDelete = rowIsMarkForDelete(physicianLicense.payorGrpExclusionTbl(), LAST_ROW);

		/*Payor Exclusion*/
		setValueSearchInput(PAYOR_EXCLUSION);
		clickOnElement(physicianLicense.deletePyrExclCkboxOnPayorExclTbl(LAST_ROW));
		boolean isRowOnPyrExclMarkedForDelete = rowIsMarkForDelete(physicianLicense.payorExclusionTbl(), LAST_ROW);

		/*Physician Cross References*/
		setValueSearchInput(PHYSICIAN_CROSS_REFERENCES);
		clickOnElement(physicianLicense.deleteChkboxOnCrossReferencesTbl(LAST_ROW));
		boolean isRowOnPhysCrossRefMarkedForDelete = rowIsMarkForDelete(physicianLicense.physicianCrossReferencesTbl(), LAST_ROW);

		logger.info("*** Step 6 Expected Results: Row should be marked for delete.");
		assertTrue(isRowOnStaLicMarkedForDelete, "        Row should be marked for delete.");
		assertTrue(isRowOnAffLicMarkedForDelete, "        Row should be marked for delete.");
		assertTrue(isRowOnPyrExclMarkedForDelete, "        Row should be marked for delete.");
		assertTrue( isRowOnOIGExclMarkedForDelete, "        Row should be marked for delete.");
		assertTrue(isRowOnPyrGrpExclMarkedForDelete, "        Row should be marked for delete.");
		assertTrue(isRowOnPhysCrossRefMarkedForDelete,"        Row should be marked for delete.");
		assertTrue(isRowOnClnSpeAffLicMarkedForDelete,"        Row should be marked for delete.");

		logger.info("*** Step 7 Expected Results: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();

		logger.info("*** Step 7 Expected Result: System take user return to the Load Phys, New record is removed out of PHYS_STATE_LIC, PHYS_PYR_LIC, PHYS_PYR_CLN_LIC, PYR_GRP_PHYS_EXCL_BC, PYR_PHYS_EXCL_BC, PHYS_XREF.");
		List<PhysStateLic> physStateLic2 = physicianDao.getPhysStateLicByPhysSeqId(phys.getSeqId());
		List<PhysPyrClnLic> physPyrClnLic2 =physicianDao.getPhysPyrClnLicByPhysSeqId(phys.getSeqId());
		List<PyrGrpPhysExcl> pyrGrpPhysExcl2 = payorDao.getPyrGrpPhysExclByConditions(" WHERE FK_PHYS_SEQ_ID = '" + phys.getSeqId() + "'");
		List<PyrPhysExcl> pyrPhysExcl2 = payorDao.getPyrPhysExclByConditions(" WHERE FK_PHYS_SEQ_ID = '" + phys.getSeqId() + "'");
		List<PhysXref> physXref2 = physicianDao.getPhysXrefByPhysSeqId(phys.getSeqId());

		assertTrue(physXref2.size() < physXref1.size(), "        Data should be removed out of PHYS_XREF.");
		assertTrue(pyrPhysExcl2.size() < pyrPhysExcl1.size(), "        Data should be removed out of PYR_PHYS_EXCL.");
		assertTrue(physStateLic2.size() < physStateLic1.size(), "        Data should be removed out of PHYS_STATE_LIC.");
		assertTrue(physPyrClnLic2.size() < physPyrClnLic1.size(), "        Data should be removed out of PHYS_PYR_CLN_LIC.");
		assertTrue(pyrGrpPhysExcl2.size() < pyrGrpPhysExcl1.size(), "        Data should be removed out of PYR_GRP_PHYS_EXCL.");

		driver.quit();
	}
	
	@Test(priority = 1, description = "Verify Help icon")
	public void testXPR_1008() throws Throwable {		
		logger.info("===== Testing - testXPR_1008 =====");  	

		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);		
		physicianLicense = new PhysicianLicense(driver);
		physicianLicenseUtils = new PhysicianLicenseUtils(driver);

		logger.info("*** Step 1 Expected Results: User login successful. The Physician License page is shown.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Actions: Click on Help icon at the load page.");
		clickOnElement(physicianLicense.physSectionHelpIcon());

		logger.info("*** Step 2 Expected Results: Help page should be shown.");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "p_physician_license_load_npi_upin.htm", "        Help page at Load Facility Page should be displayed.");

		logger.info("*** Step 3 Actions: Load valid NPI.");
		Phys phys = physicianDao.getPhysByNpiAndUpinIsNotNullAndNotInPhysStateLic();
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 3 Expected Results: Physician License page is displayed with correct information.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		logger.info("*** Step 4 Actions: Click on help icon at Physician License.");
		clickOnElement(physicianLicense.helpIconOnPhysLicSection());

		logger.info("*** Step 4 Expected Results: Help page should be shown.");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "p_physician_license_physician_license.htm", "        Help page at Load Physician License should be displayed.");

		logger.info("*** Step 5 Actions: Click on help icon at State Licenses section.");
		setValueSearchInput(STATE_LICENSES);
		clickOnElement(physicianLicense.helpIconOnStateLicSection());

		logger.info("*** Step 5 Expected Results: Help page should be shown.");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "p_physician_license_state_licenses.htm", "        Help page at State License should be displayed.");

		logger.info("*** Step 6 Actions: Click on help icon at Affiliated Licenses section.");
		setValueSearchInput(AFFILIATED_LICENSES);
		clickOnElement(physicianLicense.affLicSectionHelpIcon());

		logger.info("*** Step 6 Expected Results: Help page should be shown.");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "p_physician_license_affiliated_licenses.htm", "        Help page at Affiliated Licenses should be displayed.");

		logger.info("*** Step 7 Actions: Click on help icon at Client Specific Affiliated Licenses section.");
		setValueSearchInput(CLIENT_SPECIFIC_AFFILIATED);
		clickOnElement(physicianLicense.clnSpeAffLicSectionHelpIcon());

		logger.info("*** Step 7 Expected Results: Help page should be shown.");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "p_physician_license_client_specific_affiliated_licenses.htm", "        Help page at Client Specific Affiliated Licenses should be displayed.");

		logger.info("*** Step 8 Actions: Click on help icon at OIG Exclusion section.");
		setValueSearchInput(OIG_EXCLUSION);
		clickOnElement(physicianLicense.oigExclusSectionHelpIcon());

		logger.info("*** Step 8 Expected Results: Help page should be shown.");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "p_physician_license_oig_exclusions.htm", "        Help page at Load OIG Exclusion Licenses should be displayed.");

		logger.info("*** Step 9 Actions: Click on help icon at Payor Group Exclusion section.");
		setValueSearchInput(PAYOR_GROUP_EXCLUSION);
		clickOnElement(physicianLicense.helpIconOnPayorGroupExclusionTbl());

		logger.info("*** Step 9 Expected Results: Help page should be shown.");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "p_physician_license_payor_group_exclusion.htm", "        Help page at Payor Group Exclusion should be displayed.");

		logger.info("*** Step 10 Actions: Click on help icon at Payor Exclusion section.");
		setValueSearchInput(PAYOR_EXCLUSION);
		clickOnElement(physicianLicense.helpIconOnPayorExclusionTbl());

		logger.info("*** Step 10 Expected Results: Help page should be shown.");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "p_physician_license_payor_exclusion.htm", "        Help page at Payor Exclusion should be displayed.");

		logger.info("*** Step 11 Actions: Click on help icon at Physician Cross References section.");
		setValueSearchInput(PHYSICIAN_CROSS_REFERENCES);
		clickOnElement(physicianLicense.helpIconOnCrossReferencesTbl());

		logger.info("*** Step 11 Expected Results: Help page should be shown.");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "p_physician_license_physician_cross_references.htm", "        Help page at Physician Cross References should be displayed.");

		logger.info("*** Step 12 Actions: Click on help icon at Internal Note section.");
		setValueSearchInput(INTERNAL_NOTE);
		clickOnElement(physicianLicense.helpIconOnInternalNoteTbl());

		logger.info("*** Step 12 Expected Results: Help page should be shown.");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "p_physician_license_internal_note.htm", "        Help page at Internal Note should be displayed.");

		driver.quit();
	}
	
	@Test(priority = 1,description = "Verify Reset button.")
	public void testXPR_1009() throws Throwable{
		logger.info("==== Testing - testXPR_1009 ====");

		timeStamp = new TimeStamp();
		randomCharacter = new RandomCharacter();
		physicianLicense = new PhysicianLicense(driver);

		logger.info("*** Step 1 Expected results: User login successful and Load Physician Page is displayed.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 2 Action: Load valid NPI that not in PHYS_STATE_LIC.");
		phys = prepareData();
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 2 Expected results: Physician License page is displayed.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		logger.info("*** Step 3 Actions: Click on Reset button .");		
		clickOnElement(physicianLicense.resetBtn());

		logger.info("*** Step 3 Expected Results: System will take user return Load Physician page.");
		verifyTheLoadPhysicianLicensePageIsDisplayed();

		logger.info("*** Step 4 Action: Reload NPI.");
		setValueNpiIdLookup(phys.getNpiId());

		logger.info("*** Step 4 Expected results: Physician License page is load.");
		verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String.valueOf(phys.getNpiId()), EMPTY);

		logger.info("*** Step 5 Action: Add new record at State License.");
		setValueSearchInput(STATE_LICENSES);
		String state1License = randomCharacter.getRandomAlphaNumericString(10);
		State state1 = stateDao.getStateFromZipByCtyNmNotNull();
		clickHiddenPageObject(physicianLicense.staLicSectionStaLicTblAddButton(), 0);
		enterValues(physicianLicense.staLicSectionStaLicTblLicenseColInput(FIRST_ROW), state1License);
		selectDropDown(physicianLicense.staLicSectionStaLicTblStateColDropDown(FIRST_ROW), state1.getName());
		pressKeyShorcut(Keys.ENTER, EMPTY);

		logger.info("*** Step 5 Expected Results: New record is shown in table.");
		xifinPortalUtils.waitForPageLoaded(wait);
		assertEquals(physicianLicense.staLicSectionStaLicTblLicenseColText(FIRST_ROW).getText(),state1License,"        State License is displayed");
		assertEquals(physicianLicense.staLicSectionStaLicTblStateColText(FIRST_ROW).getText(),state1.getName(),"        Physician License page is displayed");

		logger.info("*** Step 6 Actions: Click on Reset Button.");
		clickOnElement(physicianLicense.resetBtn());

		logger.info("*** Step 6 Expected Results: The warning message 'Changes have been made to this Physician License. Are you sure you want to reset the page' is displayed.");
		assertEquals(physicianLicense.confirmDialog().getText(), "Changes have been made to this Physician License. Are you sure you want to reset the page?", "        The confirmation dialog should be displayed 'Changes have been made to this Physician License. Are you sure you want to reset the page?'");

		logger.info("*** Step 7 Actions: Click on Reset Button.");
		clickOnElement(physicianLicense.resetButtonOnConfimDialog());

		logger.info("*** Step 7 Expected Results: System take user return the Load page and no record added in PHYS_STATE_LIC");
		verifyTheLoadPhysicianLicensePageIsDisplayed();
		List<PhysStateLic> physStateLic = physicianDao.getPhysStateLicByPhysSeqId(phys.getSeqId());
		assertEquals(physStateLic.size(), 0, "        System shouldn't be added data into PHYS_STATE_LIC");

		driver.quit();
	}

	private PayorGroupExclusion addNewRecordInPayorGroupExclusionGrid(String curPyrGrpName, String effDate, String expDate) throws Exception {
		clickOnElement(physicianLicense.addNewPayorGroupExclusionbtn());

		clickOnElement(physicianLicense.effDateOnPayorGroupExclTbl(LAST_ROW));
		enterValues(physicianLicense.effDateOnPayorGroupExclTblInput(LAST_ROW), effDate);
		clickOnElement(physicianLicense.expDateOnPayorGroupExclTbl(LAST_ROW));
		enterValues(physicianLicense.expDateOnPayorGroupExclTblInput(LAST_ROW), expDate);

		String pyrGrpName = getRandomPyrGroupName(curPyrGrpName);
		selectDropDown(physicianLicense.pyrGrpIdOnPayorGroupExclTbl(LAST_ROW), pyrGrpName);

		PayorGroupExclusion payorGroupExclusion = new PayorGroupExclusion();
		payorGroupExclusion.setEffDt(ConvertUtil.convertStringToSQLDate(effDate, DATE_FORMATTED_DEFAULT));
		payorGroupExclusion.setExpDt(ConvertUtil.convertStringToSQLDate(expDate, DATE_FORMATTED_DEFAULT));
		payorGroupExclusion.setGroupName(pyrGrpName);

		return payorGroupExclusion;
	}

	private AffiliatedLicense addNewRecordInAffiliatedLicenseGrid(String pyrAbbrv) throws Exception  {
		clickHiddenPageObject(physicianLicense.affLicTblAddBtn(), 0);

		PayorOverrideRemit payorOverrideRemit = getRandomPayorOverrideRemit(pyrAbbrv);
		PhysPyrLicTyp physPyrLicTyp = physicianDao.getPhysPyrLicTypNotNullDescr();
		String licenseId = randomCharacter.getRandomNumericString(4);

		enterValues(physicianLicense.affLicTblPyrIdColInput(), payorOverrideRemit.getPyrAbbrv());
		selectDropDown(physicianLicense.affLicTblLicenseTypIdColDropdownList(), physPyrLicTyp.getDescr());
		clickOnElement(physicianLicense.affLicTblLicenseIdColText(LAST_ROW));
		enterValues(physicianLicense.affLicTblLicenseIdColInput(), licenseId);

		AffiliatedLicense affiliatedLicense = new AffiliatedLicense();
		affiliatedLicense.setPyrId(payorOverrideRemit.getPyrId());
		affiliatedLicense.setPyrAbbrv(payorOverrideRemit.getPyrAbbrv());
		affiliatedLicense.setPyrName(payorOverrideRemit.getName());
		affiliatedLicense.setLicTypId(physPyrLicTyp.getPhysPyrLicTypId());
		affiliatedLicense.setLicTypName(physPyrLicTyp.getDescr());
		affiliatedLicense.setLicId(licenseId);

		return affiliatedLicense;
	}

	private PhysicianCrossReference addNewRecordPhysicianCrossReferenceGrid(String desc, String effDate, String expDate) throws Exception {
		clickOnElement(physicianLicense.addPhysicianCrossReferencesbtn());

		clickOnElement(physicianLicense.effDateOnCrossReferencesTbl(LAST_ROW));
		enterValues(physicianLicense.effDateOnCrossReferencesTblInput(LAST_ROW), effDate);
		clickOnElement(physicianLicense.expDateOnCrossReferencesTbl(LAST_ROW));
		enterValues(physicianLicense.expDateOnCrossReferencesTblInput(LAST_ROW), expDate);

		String xrefDesc = getRandomXrefDesc(desc);
		enterValues(physicianLicense.xrefOnCrossReferencesTbl(LAST_ROW), xrefDesc);

		PhysicianCrossReference physicianCrossReference = new PhysicianCrossReference();
		physicianCrossReference.setEffDt(ConvertUtil.convertStringToSQLDate(effDate, DATE_FORMATTED_DEFAULT));
		physicianCrossReference.setExpDt(ConvertUtil.convertStringToSQLDate(expDate, DATE_FORMATTED_DEFAULT));
		physicianCrossReference.setDesc(xrefDesc);

		return physicianCrossReference;
	}

	private PayorExclusion addNewRecordInPayorExclusionGrid(String pyrAbbrv, String effDate, String expDate) throws Exception {
		clickOnElement(physicianLicense.addNewPayorExclusionbtn());

		clickOnElement(physicianLicense.effDateOnPayorExclTbl(LAST_ROW));
		enterValues(physicianLicense.effDateOnPayorExclTblInput(LAST_ROW), effDate);
		clickOnElement(physicianLicense.expDateOnPayorExclTbl(LAST_ROW));
		enterValues(physicianLicense.expDateOnPayorExclTblInput(LAST_ROW), expDate);

		Pyr pyr = getRandomPyr(pyrAbbrv);
		enterValues(physicianLicense.pyrIdInputOnPayorExclTbl(LAST_ROW), pyr.getPyrAbbrv());

		PayorExclusion payorExclusion = new PayorExclusion();
		payorExclusion.setEffDt(ConvertUtil.convertStringToSQLDate(effDate, DATE_FORMATTED_DEFAULT));
		payorExclusion.setExpDt(ConvertUtil.convertStringToSQLDate(expDate, DATE_FORMATTED_DEFAULT));
		payorExclusion.setPyrId(pyr.getPyrId());
		payorExclusion.setPyrAbbrv(pyr.getPyrAbbrv());
		payorExclusion.setPyrName(pyr.getName());

		return payorExclusion;
	}

	private void cleanPhysPyrLic(int physSeqId) throws Exception {
    	PhysPyrLic physPyrLic = physicianDao.getPhysPyrLicByPhysSeqId(physSeqId);
    	xifinPortalUtils.deletePhysPyrLic(physPyrLic);
	}

	private void cleanPhysPyrClnLic(int physSeqId) throws Exception {
		List<PhysPyrClnLic> physPyrClnLics = physicianDao.getPhysPyrClnLicByPhysSeqId(physSeqId);
		for (PhysPyrClnLic physPyrClnLic : physPyrClnLics)
			xifinPortalUtils.deletePhysPyrClnLic(physPyrClnLic);
	}

	private void clickOnSaveAndClearBtn() throws Exception {
		clickHiddenPageObject(physicianLicense.saveAndClearBtn(), 0);
		xifinPortalUtils.waitForPageLoaded(wait);
	}

	private void setValueSearchInput(String sectionName) throws Exception {
		enterValues(physicianLicense.sectionSearchInput(), sectionName);
	}

	private void setValueNpiIdLookup(Long npiId) throws Exception {
		xifinPortalUtils.checkElementIsAvailable(physicianLicense.npiIdLookupInput());
		enterValues(physicianLicense.npiIdLookupInput(), String.valueOf(npiId));
		xifinPortalUtils.waitForPageLoaded(wait);
	}

	private void setValueUpinIdLookup(String upinId) throws Exception {
		xifinPortalUtils.checkElementIsAvailable(physicianLicense.upinIdLookupInput());
		enterValues(physicianLicense.upinIdLookupInput(), upinId);
		xifinPortalUtils.waitForPageLoaded(wait);
	}

	private void setValuesStaLicSec(StateLicenseSec staLic, String rowNum) throws Exception {
		enterValues(physicianLicense.staLicSectionStaLicTblLicenseColInput(rowNum), staLic.getLicense());
		selectDropDown(physicianLicense.staLicSectionStaLicTblStateColDropDown(rowNum), staLic.getState());
	}

	private void setRequiredValuesPhysLicSection(PhysicianLicenseSec physLicSec) throws Exception {
		enterValues(physicianLicense.phyLicSectionLastNameInput(), physLicSec.getlName());
		enterValues(physicianLicense.phyLicSectionFirstNameInput(), physLicSec.getfName());
		enterValues(physicianLicense.phyLicSectionAddressOneInput(), physLicSec.getAddress1());
		enterValues(physicianLicense.phyLicSectionAddressTwoInput(), physLicSec.getAddress2());
		enterValues(physicianLicense.phyLicSectionPostalCodeInput(), physLicSec.getPostalCode());
		enterValues(physicianLicense.taxonomyGrpTaxonomyCodeInput(), physLicSec.getTaxonomy());
		selectDropDown(physicianLicense.phyLicSectionCredentialsDropDown(), physLicSec.getCredential());
	}

	private StateLicenseSec setExpectedStaLicSecRecord(State state) throws Exception {
		StateLicenseSec staLicSec = new StateLicenseSec();
		staLicSec.setLicense(randomCharacter.getRandomAlphaNumericString(10));
		staLicSec.setState(state.getName());
		staLicSec.setStateId(state.getStId());
		return staLicSec;
	}

	private void setValuesFilterPayorGroupExclusion(PayorGroupExclusion payorGroupExclusion) throws Exception {
		enterValues(physicianLicense.payorGrpExclusionTblEffDtFilterInput(), timeStamp.convertDateToString(payorGroupExclusion.getEffDt(), DATE_FORMATTED_DEFAULT));
		enterValues(physicianLicense.payorGrpExclusionTblExpDtFilterInput(), timeStamp.convertDateToString(payorGroupExclusion.getExpDt(), DATE_FORMATTED_DEFAULT));
		enterValues(physicianLicense.payorGrpExclusionTblGroupFilterInput(), payorGroupExclusion.getGroupName());
	}

	private void setValuesFilterAffiliatedLicense(AffiliatedLicense affiliatedLicense) throws Exception {
		enterValues(physicianLicense.affLicTblPyrIdFilterInput(), affiliatedLicense.getPyrAbbrv());
		enterValues(physicianLicense.affLicTblPyrNameFilterInput(), affiliatedLicense.getPyrName());
		enterValues(physicianLicense.affLicTblLicenseTypIdFilterInput(), affiliatedLicense.getLicTypName());
		enterValues(physicianLicense.affLicTblLicenseIdFilterInput(), affiliatedLicense.getLicId());
	}

	private void setValuesFilterPhysicianCrossReference(PhysicianCrossReference physicianCrossReference) throws Exception {
		enterValues(physicianLicense.physicianCrossReferencesTblEffDtFilterInput(), timeStamp.convertDateToString(physicianCrossReference.getEffDt(), DATE_FORMATTED_DEFAULT));
		enterValues(physicianLicense.physicianCrossReferencesTblExpDtFilterInput(), timeStamp.convertDateToString(physicianCrossReference.getExpDt(), DATE_FORMATTED_DEFAULT));
		enterValues(physicianLicense.physicianCrossReferencesTblXrefDescFilterInput(), physicianCrossReference.getDesc());
	}

	private void setValuesFilterPayorExclusion(PayorExclusion payorExclusion) throws Exception {
		enterValues(physicianLicense.payorExclusionTblEffDtFilterInput(), timeStamp.convertDateToString(payorExclusion.getEffDt(), DATE_FORMATTED_DEFAULT));
		enterValues(physicianLicense.payorExclusionTblExpDtFilterInput(), timeStamp.convertDateToString(payorExclusion.getExpDt(), DATE_FORMATTED_DEFAULT));
		enterValues(physicianLicense.payorExclusionTblPayorIdFilterInput(), payorExclusion.getPyrAbbrv());
		enterValues(physicianLicense.payorExclusionTblPayorNameFilterInput(), payorExclusion.getPyrName());
	}

	private PhysicianLicenseSec getValuesPhysicianLicenseSection() throws Exception {
		PhysicianLicenseSec physLicSec = new PhysicianLicenseSec();
		physLicSec.setlName(getInputVal(physicianLicense.phyLicSectionLastNameInput()));
		physLicSec.setfName(getInputVal(physicianLicense.phyLicSectionFirstNameInput()));
		physLicSec.setAddress1(getInputVal(physicianLicense.phyLicSectionAddressOneInput()));
		physLicSec.setAddress2(getInputVal(physicianLicense.phyLicSectionAddressTwoInput()));
		physLicSec.setPostalCode(getInputVal(physicianLicense.phyLicSectionPostalCodeInput()));
		physLicSec.setTaxonomy(getInputVal(physicianLicense.taxonomyGrpTaxonomyCodeInput()));
		physLicSec.setCredential(physicianLicense.phyLicSectionCredentialsText().getText());
		physLicSec.setCountry(physicianLicense.phyLicCountryDropDown().getText());
		physLicSec.setCity(getInputVal(physicianLicense.phyLicSectionCityInput()));
		physLicSec.setStateId(getInputVal(physicianLicense.phyLicSectionStateInput()));
		physLicSec.setOverServFacLocation(physicianLicense.phyLicSectionOverideServiceFacilityLocationDropDown().getText());
		physLicSec.setFacAddress(physicianLicense.phyLicSectionAddressText().getText());
		physLicSec.setNpi(Long.parseLong(physicianLicense.phyLicSectionNpiText().getText().equals(EMPTY) ? NO_RECORD : physicianLicense.phyLicSectionNpiText().getText()));
		physLicSec.setFacTaxId(physicianLicense.phyLicSectionTaxIdText().getText());
		physLicSec.setSpecialty(physicianLicense.phyLicSectionSpecialtyDropDown().getText());
		physLicSec.setTaxId(physicianLicense.taxId().getText());
		physLicSec.setTaxonomy(getInputVal(physicianLicense.taxonomyGrpTaxonomyCodeInput()));
		physLicSec.setProvTypeDescr(physicianLicense.taxonomyGrpProviderTypeDescriptionText().getText());
		physLicSec.setClassification(physicianLicense.taxonomyGrpClassificationText().getText());
		physLicSec.setSpecialization(physicianLicense.taxonomyGrpSpecializationText().getText());

		return physLicSec;
	}

	private String getRandomPyrGroupName(String curPyrGrpName) throws Exception {		
		String pyrGrpName;
		do {
			PyrGrp lstPyrGrp1 = payorDao.getRandomPyrGrpFromPyrGrp();
			pyrGrpName = lstPyrGrp1.getGrpNm();
		} while (pyrGrpName.equals(curPyrGrpName));

		return pyrGrpName;
	}

	private PayorOverrideRemit getRandomPayorOverrideRemit(String pyrAbbrv) throws Exception {
		PayorOverrideRemit payorOverrideRemit = new PayorOverrideRemit();
		do {
			payorOverrideRemit = payorDao.getPayorOverrideRemit().get(0);
		} while (payorOverrideRemit.getPyrAbbrv().equals(pyrAbbrv));

		return payorOverrideRemit;
	}

	private String getRandomXrefDesc(String desc) throws Exception {
		String xrefDesc = EMPTY;
		do {
			List<String> phyXref = crossReferenceDao.getXrefFromXrefAndConditionByXrefCat("Physician"," AND A.PK_XREF_ID not in (Select PK_XREF_ID from PHYS_XREF)");
			xrefDesc = phyXref.get(0).trim();
		} while (xrefDesc.equalsIgnoreCase(desc));

		return xrefDesc;
	}

	private Pyr getRandomPyr(String pyrAbbrv) throws Exception {
		Pyr pyr = new Pyr();
		do {
			try {
				pyr = payorDao.getRandomPyr();
			} catch (Exception e) {
				pyr = new Pyr();
			}
		} while (pyr.getPyrAbbrv().equals(pyrAbbrv));

		return pyr;
	}

	private void updatePhys(int seqId, int overrideSvcFacId) throws Exception {
		Phys phys = physicianDao.getPhysBySeqId(seqId);
		phys.setOverrideSvcFacId(overrideSvcFacId);
		phys.setResultCode(ErrorCodeMap.UPDATED_RECORD);
		physicianDao.setPhys(phys);
	}

	/**
	 * Prepare data for NPI and Phys
	 * @return Phys object
	 * @throws Exception
	 */
	private Phys prepareData() throws Exception {
		npi = xifinPortalUtils.createNpi();
		Phys actPhys = xifinPortalUtils.createPhys(npi);
		assertNotNull(actPhys.getNpiId(), "        The new record is created into Phys table.");

		return actPhys;
	}

	private void verifyStaLicSectionDisplayedCorrectly(String rowNum, StateLicenseSec staLic) throws Exception {
		assertEquals(physicianLicense.staLicSectionStaLicTblLicenseColText(rowNum).getText(), staLic.getLicense(), "        State License is displayed");
		assertEquals(physicianLicense.staLicSectionStaLicTblStateColText(rowNum).getText(), staLic.getState(), "        Physician License page is displayed");
	}

	private void verifyDataIsSavedInPhysStaLicTblCorrectly(List<PhysStateLic> physStaLic, int i, StateLicenseSec staLic) throws Exception {
		assertEquals(physStaLic.get(i).getStId(),staLic.getStateId(),"        Physician State License is added");
		assertEquals(physStaLic.get(i).getStLicId(),staLic.getLicense(),"        Physician License Id is added");
	}

	private void verifyTheLoadPhysicianLicensePageIsDisplayed() throws Exception {
		xifinPortalUtils.waitForPageLoaded(wait);
		waitUntilElementIsDisplayed(physicianLicense.lookupPhyLicIdSection(), timeOut, false);
		assertEquals(physicianLicense.physicianTitle().getText(), PHYSICIAN_LICENSE_TITLE, "        The Physician License page is displayed.");
	}

	private void verifyTaxonomyCodeSearchResultPageIsDisplayedCorrectly() throws Exception {
		xifinPortalUtils.waitForPageLoaded(wait);
		assertEquals(taxonomyCodeSearchResults.taxCdSearchResultTblHeaderLbl().getText(), TAXONOMY_CODE_SEARCH_RESULT, "        The Taxonomy Code Search Result page is displayed.");
	}

	private void verifyPhysicianSearchPageIsDisplayed() throws Exception {
		xifinPortalUtils.waitForPageLoaded(wait);
		assertEquals(physicianSearch.physcianSearchTitle().getText(), PHYSICIAN_SEARCH_TITLE, "        The Physician Search page is displayed.");
	}

	private void verifyPhysicianLicenseResultsPageIsDisplayed() throws Exception {
		xifinPortalUtils.waitForPageLoaded(wait);
		assertEquals(physicianLicenseResults.physSearchResultTitle().getText(), PHYSICIAN_SEARCH_RESULT_TITLE, "        The Physician Search Results page is displayed.");
	}

	private void verifyPhysTblIsSavedCorrectly(PhysicianLicenseSec physLicSec, Phys phys) throws Exception {
		assertEquals(phys.getPhysLname(), physLicSec.getlName(),"        Physician License page is displayed");
		assertEquals(phys.getPhysFname(), physLicSec.getfName(),"        Physician License page is displayed");
		assertEquals(phys.getAddr1(), physLicSec.getAddress1(),"        Physician License page is displayed");
		assertEquals(phys.getAddr2(), physLicSec.getAddress2(),"        Physician License page is displayed");
		assertEquals(phys.getZipId(), physLicSec.getPostalCode(),"        Physician License page is displayed");
		assertEquals(phys.getTaxonomyCd(), physLicSec.getTaxonomy(),"        Physician License page is displayed");
		assertEquals(phys.getPhysCredId(), physLicSec.getCredential(),"        Physician License page is displayed");
	}

	private void verifyTaxonomyCodeSearchPageIsDisplayed() throws Exception {
		xifinPortalUtils.waitForPageLoaded(wait);
		assertEquals(taxonomyCodeSearch.taxCdSearchPageTitle().getText(), TAXONOMY_CODE_SEARCH, "        The Taxonomy Code Search page is displayed.");
	}

	private void verifyThePhysicianLicenseDetailPageIsLoadedCorrectly(String npi, String upin) throws Exception {
		xifinPortalUtils.waitForPageLoaded(wait);
		waitUntilElementIsDisplayed(physicianLicense.fileMaintPhyLicHeaderContent(), timeOut, false);
		assertEquals(physicianLicense.physicianTitle().getText(), PHYSICIAN_LICENSE_TITLE, "        The Physician License page is displayed.");
		if (npi.equals(EMPTY)) {
			assertEquals(getInputVal(physicianLicense.headerUpinIdInput()), upin, "        Upin is displayed correctly.");
		} else {
			assertEquals(getInputVal(physicianLicense.headerNpiIdInput()), npi, "        Npi is displayed correctly.");
		}
	}

	private void verifyChangeOfRequiredValuesInPhysicianLicenseSection(PhysicianLicenseSec actPhysLicSec, PhysicianLicenseSec expPhysLicSec) throws Exception {
		assertEquals(actPhysLicSec.getlName(), expPhysLicSec.getlName(), "        The Last Name input is displayed correctly.");
		assertEquals(actPhysLicSec.getfName(), expPhysLicSec.getfName(), "        The First Name input is displayed correctly.");
		assertEquals(actPhysLicSec.getAddress1(), expPhysLicSec.getAddress1(), "        The Address 1 input is displayed correctly.");
		assertEquals(actPhysLicSec.getAddress2(), expPhysLicSec.getAddress2(), "        The Address 2 input is displayed correctly.");
		assertEquals(actPhysLicSec.getTaxonomy(), expPhysLicSec.getTaxonomy(), "        The Taxonomy input is displayed correctly.");
		assertEquals(actPhysLicSec.getCredential(), expPhysLicSec.getCredential(), "        The Credentials dropdown input is displayed correctly.");
		assertEquals(actPhysLicSec.getPostalCode(), expPhysLicSec.getPostalCode(), "        The Postal Code input is displayed correctly.");
	}

	private void verifyPhysicianLicensePageIsDisplayedDataCorrectly(Phys phys) throws Exception {
		assertEquals(phys.getPhysLname(), getInputVal(physicianLicense.phyLicSectionLastNameInput()), "        The phyLic Section Last Name Input should be correct data.");
		assertEquals(phys.getPhysFname(), getInputVal(physicianLicense.phyLicSectionFirstNameInput()), "        The phyLic Section First Name Input should be correct data.");
		assertEquals(phys.getAddr1(), getInputVal(physicianLicense.phyLicSectionAddressOneInput()), "        The phyLic Section Address 1 Input should be correct data.");
		assertEquals(countryDao.getCountryByCntryId(phys.getCntryId()).getName(), physicianLicense.phyLicCountryDropDown().getText(), "        The phyLic Country should be correct data.");
		assertEquals(ConvertUtil.isNull(phys.getAddr2(), EMPTY), getInputVal(physicianLicense.phyLicSectionAddressTwoInput()), "        The phyLic Section Address 2 Input should be correct data.");

		assertEquals(ConvertUtil.isNull(phys.getCity(), EMPTY), getInputVal(physicianLicense.phyLicSectionCityInput()), "        The phyLic Section City Input should be correct data.");
		assertEquals(getInputVal(physicianLicense.phyLicSectionPostalCodeInput()).replace("-", EMPTY), phys.getZipId().replace("-", EMPTY),"        The phyLic Section Postal Code Search input should be correct data.");
		assertEquals(ConvertUtil.isNull(phys.getStId(), EMPTY), getInputVal(physicianLicense.phyLicSectionStateInput()), "        The phyLic Section State Input should be correct data.");
		assertEquals((phys.getOverrideSvcFacId() == 0) ? EMPTY : rpmDao.getFacByFacId(null, phys.getOverrideSvcFacId()).getAbbrv(), physicianLicense.phyLicSectionOverideServiceFacilityLocationDropDown().getText(), "        The phyLic Section Overide Service Facility Location should be correct data.");
		assertEquals(EMPTY, physicianLicense.phyLicSectionAddressText().getText(), "        The phyLicSectionAddressText should be correct data.");
		assertEquals(EMPTY, physicianLicense.phyLicSectionNpiText().getText(), "        The phyLic Section Npi Text should be correct data.");
		assertEquals(EMPTY, physicianLicense.phyLicSectionTaxIdText().getText(), "        The phyLicSectionTaxId Text should be correct data.");
		assertEquals(physicianDao.getPhysSpecTypByPhysSpecId(phys.getPhysSpecId()).getDescr().trim(), physicianLicense.phyLicSectionSpecialtyDropDown().getText(), "        The phyLic Section Specialty should be correct data.");
		assertEquals(phys.getPhysCredId(), physicianLicense.phyLicSectionCredentialsText().getText(), "        The phyLic Section Credentials should be correct data.");
		assertEquals(ConvertUtil.isNull(phys.getTaxId(), EMPTY), physicianLicense.taxId().getText(), "        The phyLic Section Specialty should be correct data.");

		/* Taxonomy Group */
		if (phys.getTaxonomyCd() == null) {
			assertEquals(ConvertUtil.isNull(phys.getTaxonomyCd(), EMPTY), getInputVal(physicianLicense.taxonomyGrpTaxonomyCodeInput()), "        The Taxonomy Grp Taxonomy Code Input should be correct data.");
		}
		if (phys.getTaxonomyCd() != null) {
			Taxonomy taxonomyInfo = taxonomyDao.getTaxonomyByTaxonomyCd(phys.getTaxonomyCd());
			assertEquals(getInputVal(physicianLicense.taxonomyGrpTaxonomyCodeInput()), phys.getTaxonomyCd(),"        The Taxonomy Grp Taxonomy Code Input should be correct data.");
			assertEquals(physicianLicense.taxonomyGrpProviderTypeDescriptionText().getText(), ConvertUtil.isNull(taxonomyInfo.getProviderTypDescr(), EMPTY),"        The Taxonomy Grp Provider Type Description Text should be correct data.");
			assertEquals(physicianLicense.taxonomyGrpClassificationText().getText(), ConvertUtil.isNull(taxonomyInfo.getClassification(), EMPTY),"        The Taxonomy Grp Classification Text should be correct data.");
			assertEquals(physicianLicense.taxonomyGrpSpecializationText().getText(), ConvertUtil.isNull(taxonomyInfo.getSpecialization(), EMPTY),"        The Taxonomy Grp Specialization Text should be correct data.");
		}
		//  State Licenses
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.staLicSectionStaLicTblTotalRecord()), "        No record is shown in table.");
		//  Affiliated Licenses
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.affLicTblTotalResultText()), "        No record is shown in table.");
		//  Client Specific Affiliated Licenses
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.clnSpeAffLicTblTotalResultText()), "        No record is shown in table.");
		//  OIG Exclusions
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.oigExclusTblTotalResultText()), "        No record is shown in table.");
		//  Payor Group Exclusion
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.pyrGrpExclNavBar()), "        No record is shown in table.");
		//  Payor Exclusion
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.pyrExclNavBar()), "        No record is shown in table.");
		//  Physician Cross References
		assertEquals(NO_RECORD, getTotalResultSearch(physicianLicense.physicianCrossReferencesNavBar()), "        No record is shown in table.");
		//  Internal Note
		assertEquals(EMPTY, getTotalResultSearch(physicianLicense.internalNote()), "        Internal Note in table should be empty.");
	}

	private void verifyDataIsShownIntoPayorGroupExclusionGridCorrectly(PayorGroupExclusion payorGroupExclusion) throws Exception {
		setValuesFilterPayorGroupExclusion(payorGroupExclusion);
		String totalRecord = physicianLicense.pyrGrpExclNavBar().getText().trim();
		assertNotEquals(EMPTY_RECORD, totalRecord);
	}

	private void verifyDataIsShownIntoAffiliatedLicenseGridCorrectly(AffiliatedLicense affiliatedLicense) throws Exception {
		setValuesFilterAffiliatedLicense(affiliatedLicense);
		String totalRecord = physicianLicense.affLicTblTotalResultText().getText().trim();
		assertNotEquals(EMPTY_RECORD, totalRecord);
	}

	private void verifyDataIsShownIntoAffiliatedLicenseGridCorrectly(PhysicianCrossReference physicianCrossReference) throws Exception {
		setValuesFilterPhysicianCrossReference(physicianCrossReference);
		String totalRecord = physicianLicense.physicianCrossReferencesNavBar().getText().trim();
		assertNotEquals(EMPTY_RECORD, totalRecord);
	}

	private void verifyDataIsShownIntoPayorExclusionGridCorrectly(PayorExclusion payorExclusion) throws Exception {
		setValuesFilterPayorExclusion(payorExclusion);
		String totalRecord = physicianLicense.pyrExclNavBar().getText().trim();
		assertNotEquals(EMPTY_RECORD, totalRecord);
	}
}