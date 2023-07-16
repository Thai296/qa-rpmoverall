package com.newXp.tests;

import com.mbasys.mars.ejb.entity.country.Country;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.facBillToOverride.FacBillToOverride;
import com.mbasys.mars.ejb.entity.facBillToOverrideLnk.FacBillToOverrideLnk;
import com.mbasys.mars.ejb.entity.facFacGrp.FacFacGrp;
import com.mbasys.mars.ejb.entity.facLic.FacLic;
import com.mbasys.mars.ejb.entity.facPyrGrpRemit.FacPyrGrpRemit;
import com.mbasys.mars.ejb.entity.facPyrRemit.FacPyrRemit;
import com.mbasys.mars.ejb.entity.facTyp.FacTyp;
import com.mbasys.mars.ejb.entity.facXref.FacXref;
import com.mbasys.mars.ejb.entity.npi.Npi;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrFacJurisPyr.PyrFacJurisPyr;
import com.mbasys.mars.ejb.entity.pyrGrp.PyrGrp;
import com.mbasys.mars.ejb.entity.state.State;
import com.mbasys.mars.ejb.entity.taxonomy.Taxonomy;
import com.mbasys.mars.ejb.entity.testDt.TestDt;
import com.mbasys.mars.ejb.entity.zip.Zip;
import com.mbasys.mars.persistance.MiscMap;
import com.overall.accession.accessionProcessing.ZipCodeSearch;
import com.overall.accession.accessionProcessing.ZipCodeSearchResults;
import com.overall.fileMaintenance.fileMaintenanceTables.Facility;
import com.overall.menu.MenuNavigation;
import com.overall.search.NpiGlobalSearch;
import com.overall.search.NpiGlobalSearchResults;
import com.overall.search.NpiSearch;
import com.overall.search.NpiSearchResults;
import com.overall.utils.XifinPortalUtils;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.domain.CrossReferenceDescription;
import com.xifin.qa.dao.rpm.domain.EntityNpiSearchResult;
import com.xifin.qa.dao.rpm.domain.GlobalNpiSearchResult;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ListUtil;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import domain.fileMaintenance.facility.BillingOverride;
import domain.fileMaintenance.facility.CorrespondenceAddress;
import domain.fileMaintenance.facility.DefaultRemitToAddress;
import domain.fileMaintenance.facility.FacilityAdditionalInformation;
import domain.fileMaintenance.facility.FacilityAddressAndContactInformation;
import domain.fileMaintenance.facility.FacilityCrossReference;
import domain.fileMaintenance.facility.Header;
import domain.fileMaintenance.facility.JurisdictionPayor;
import domain.fileMaintenance.facility.PayorGroupOverrideRemitAddress;
import domain.fileMaintenance.facility.PayorOverrideRemitAddress;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.testng.Assert.*;

public class FacilityTest extends SeleniumBaseTest {
	private Facility facility;
	private RandomCharacter randomCharacter;
	private NpiSearch npiSearch;
	private NpiSearchResults npiSearchResults;
	private NpiGlobalSearch npiGlobalSearch;
	private NpiGlobalSearchResults npiGlobalSearchResults;
	private XifinPortalUtils xifinPortalUtils;


	@BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "disableBrowserPlugins", "facAbbrev1", "facAbbrev2", "facAbbrev3", "facAbbrev4"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, @Optional String disableBrowserPlugins, String facAbbrev1, String facAbbrev2, String facAbbrev3, String facAbbrev4)
    {
        try
        {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            cleanUpFacility(Arrays.asList(facAbbrev1, facAbbrev2, facAbbrev3, facAbbrev4));
			XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
			xifinAdminUtils.clearDataCache();
        }
        catch (Exception e)
        {
            Assert.fail("Error running BeforeSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "disableBrowserPlugins", "facAbbrev1", "facAbbrev2", "facAbbrev3", "facAbbrev4"})
    public void AfterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, @Optional String disableBrowserPlugins, String facAbbrev1, String facAbbrev2, String facAbbrev3, String facAbbrev4)
    {
        try
        {
            logger.info("Running AfterSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            cleanUpFacility(Arrays.asList(facAbbrev1, facAbbrev2, facAbbrev3, facAbbrev4));
            XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
			xifinAdminUtils.clearDataCache();
        }
        catch (Exception e)
        {
            Assert.fail("Error running AfterSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"ssoUsername", "ssoPassword"})
    public void beforeMethod(String ssoUsername, String ssoPassword, Method method)
    {
        try
        {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "["+method.getName()));
			MenuNavigation navigation = new MenuNavigation(driver, config);
			logIntoSso(ssoUsername, ssoPassword);
			navigation.navigateToFacilityPage();

        }
        catch (Exception e)
        {
            logger.error("Error running BeforeMethod", e);
        }
    }    

	@Test(priority = 1, description = "Add Facility with Type is Draw Station")
	@Parameters({"facAbbrev1"})
	public void testXPR_956(String facAbbrev1) throws Exception {
		logger.info("===== Testing - testXPR_956 =====");
		facility = new Facility(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Expected Results: - Verify that the Load Facility page is displayed");
		assertTrue(isOnLoadFacilityPage());

		logger.info("*** Action: - Enter a new Facility ID and tab out");
		facility.setFacilityId(facAbbrev1, wait);
		logger.info("       New Facility ID " + facAbbrev1);

		logger.info("*** Expected Results: - Verify that the Facility page is displayed with all fields are empty");
		verifyAllFieldsAreCleared(facAbbrev1);

		logger.info("*** Action: - Select Type is Draw Station and enter values in Header section");
		Header expectedHeader = setValuesInFacilityHeader(MiscMap.FAC_TYP_PSC);

		logger.info("*** Action: - Enter values in Facility Additional Information section");
		FacilityAdditionalInformation expectedFacilityAdditionalInformation = setValuesInFacilityAdditionalInformation("Draw Station");

		logger.info("*** Action: - Enter values in Facility Address And Contact Information section");
		FacilityAddressAndContactInformation expectedFacilityAddressAndContactInformation = setValuesInFacilityAddressAndContactInformation();

		logger.info("*** Action: - Enter values in Correspondence Address section");
		CorrespondenceAddress expectedCorrespondenceAddress = setValuesInCorrespondenceAddress();

		logger.info("*** Action: - Enter values in Default Remit To Address section");
		DefaultRemitToAddress expectedDefaultRemitToAddress = setValuesInDefaultRemitToAddress();

		logger.info("*** Action: - Enter values in Facility Cross Reference section");
		FacilityCrossReference expectedFacilityCrossReference = setValuesInFacilityCrossReference("1");

		logger.info("*** Action: - Enter values in Jurisdiction Payor section");
		//The domain class JurisdictionPayor should represent 1 row in the table. To represent the entire table, you'd have a list of rows: List<JurisdictionPayor> jurisdictionPayors;
		JurisdictionPayor expectedJurisdictionPayors = setValuesInJurisdictionPayor();

		logger.info("*** Action: - Enter values in Billing Override section");
		BillingOverride expectedBillingOverride = setValuesInBillingOverride();

		logger.info("*** Action: - Enter values in Remit Address Override - Payor Group section");
		PayorGroupOverrideRemitAddress expectedPayorGroupOverrideRemitAddress = setValuesInPayorGroupOverrideRemitAddress();

		logger.info("*** Action: - Enter values in Remit Address Override - Payor section");
		PayorOverrideRemitAddress expectedPayorOverrideRemitAddress = setValuesInPayorOverrideRemitAddress();

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(facility.saveAndClearBtn());

		logger.info("*** Expected Results: - Verify that the Load Facility page is displayed");
		assertTrue(isOnLoadFacilityPage());

		Fac fac = facilityDao.getFacByAbbrv(facAbbrev1);
		List<FacFacGrp> facFacGrpList = facilityDao.getFacFacGrpByFacAbbrev(facAbbrev1);

		logger.info("***  Expected Results: - Verify that the data in Header section are saved properly in DB");
		verifyHeaderDataSaved(expectedHeader, fac);

		logger.info("*** Expected Results: - Verify that the data in Facility Additional Information section are saved properly in DB");
		verifyFacilityAdditionalInformationDataSaved(expectedFacilityAdditionalInformation, "Draw Station", facAbbrev1);

		logger.info("*** Expected Results: - Verify that the data in Facility Address and Contact Information section are saved properly in DB");
		verifyFacilityAddressAndContactInformationDataSaved(expectedFacilityAddressAndContactInformation, fac);

		logger.info("*** Expected Results: - Verify that the data in Correspondence Address section are saved properly in DB");
		verifyCorrespondenceAddressDataSaved(expectedCorrespondenceAddress, fac);

		logger.info("*** Expected Results: - Verify that the data in Default Remit To Address section are saved properly in DB");
		verifyDefaultRemitToAddressDataSaved(expectedDefaultRemitToAddress, fac);

		logger.info("***  Expected Results: - Verify that the data in Facility Cross Reference section are saved properly in DB");
		int xRefId = crossReferenceDao.getXrefByDescr(expectedFacilityCrossReference.getCrossReferenceDescription().split("-")[1]).getXrefId();
		FacXref facXrefData = facilityDao.getFacXrefByFacAbbrevAndXrefId(facAbbrev1, xRefId);
		verifyFacilityCrossReferenceDataSaved(expectedFacilityCrossReference,facXrefData);

		logger.info("***  Expected Results: - Verify that the data in Jurisdiction Payor section are saved properly in DB");
		List<PyrFacJurisPyr> pyrFacJurisPyr = payorDao.getPyrFacJurisPyrByFacId(fac.getFacId());
		verifyJurisdictionPayorDataSaved(expectedJurisdictionPayors, pyrFacJurisPyr.get(0));

		logger.info("***  Expected Results: - Verify that the data in Billing Override section are saved properly in DB");
		List<FacBillToOverride> facBillToOverride = facilityDao.getFacBillToOverrideByFacId(fac.getFacId());
		FacBillToOverrideLnk facBillToOverrideLnk = facilityDao.getFacBillToOverrideLnkByOverrideSeqId(facBillToOverride.get(0).getSeqId());
		verifyBillingOverrideDataSaved(expectedBillingOverride, facBillToOverrideLnk, facBillToOverride.get(0));

		logger.info("***  Expected Results: - Verify that the data in Remit Address Override - Payor Group section are saved properly in DB");
		FacPyrGrpRemit facPyrGrpRemit = facilityDao.getFacPyrGrpRemitByFacId(fac.getFacId());
		verifyPayorGroupOverrideRemitAddressDataSaved(expectedPayorGroupOverrideRemitAddress, facPyrGrpRemit);

		logger.info("***  Expected Results: - Verify that the data in Remit Address Override - Payor section are saved properly in DB");
		FacPyrRemit facPyrRemit = facilityDao.getFacPyrRemitByFacId(fac.getFacId());
		verifyPayorOverrideRemitAddressDataSaved(expectedPayorOverrideRemitAddress, facPyrRemit);

		logger.info("*** Action: - Load the newly created Facility ID and tab out");
		facility.setFacilityId(facAbbrev1,wait);
		facility.isFacilityLoaded(facAbbrev1,wait);

		logger.info("*** Expected Results: - Verify that the data in Header section are displayed properly in UI");
		Header actualHeader = getValuesInFacilityHeader();
		assertEquals(actualHeader, expectedHeader);

		logger.info("*** Expected Results: - Verify that the data in Facility Additional Information section are displayed properly in UI");
		FacilityAdditionalInformation actualFacilityAdditionalInformation = getValuesInFacilityAdditionalInformation("Draw Station");
		assertEquals(actualFacilityAdditionalInformation, expectedFacilityAdditionalInformation);

		logger.info("*** Expected Results: - Verify that the data in Facility Address and Contact Information section are displayed properly in UI");
		FacilityAddressAndContactInformation actualFacilityAddressAndContactInformation = getValuesInFacilityAddressAndContactInformation();
		assertEquals(actualFacilityAddressAndContactInformation, expectedFacilityAddressAndContactInformation);

		logger.info("*** Expected Results: - Verify that the data in Correspondence Address section are displayed properly in UI");
		CorrespondenceAddress actualCorrespondenceAddress = getValuesInCorrespondenceAddress();
		assertEquals(actualCorrespondenceAddress, expectedCorrespondenceAddress);

		logger.info("*** Expected Results: - Verify that the data in Default Remit To Address section are displayed properly in UI");
		DefaultRemitToAddress actualDefaultRemitToAddress = getValuesInDefaultRemitToAddress();
		assertEquals(actualDefaultRemitToAddress, expectedDefaultRemitToAddress);

		logger.info("***  Expected Results: - Verify that the data in Facility Cross Reference section are displayed properly in UI");
		FacilityCrossReference actualFacilityCrossReference = getValuesInFacilityCrossReference("1");
		assertEquals(actualFacilityCrossReference, expectedFacilityCrossReference);

		logger.info("***  Expected Results: - Verify that the data in Jurisdiction Payor section are displayed properly in UI");
		JurisdictionPayor actualJurisdictionPayorInformation = getValuesInJurisdictionPayor();
		assertEquals(actualJurisdictionPayorInformation, expectedJurisdictionPayors);

		logger.info("***  Expected Results: - Verify that the data in Billing Override section are displayed properly in UI");
		BillingOverride actualBillingOverrideInformation = getValuesInBillingOverride(1);
		assertEquals(actualBillingOverrideInformation, expectedBillingOverride);

		logger.info("***  Expected Results: - Verify that the data in Remit Address Override - Payor Group section are displayed properly in UI");
		PayorGroupOverrideRemitAddress actualPayorGroupOverrideRemitAddressInformation = getValuesInPayorGroupOverrideRemitAddress();
		assertEquals(actualPayorGroupOverrideRemitAddressInformation, expectedPayorGroupOverrideRemitAddress);

		logger.info("***  Expected Results: - Verify that the data in Remit Address Override - Payor section are displayed properly in UI");
		PayorOverrideRemitAddress actualPayorOverrideRemitAddressInformation = getValuesInPayorOverrideRemitAddress();
		assertEquals(actualPayorOverrideRemitAddressInformation, expectedPayorOverrideRemitAddress);

		logger.info("*** Action: - Reset");
		clickOnElement(facility.resetBtn());
		assertTrue(isOnLoadFacilityPage());
	}

	@Test(priority = 1, description = "Add Facility with Type is Hospital Lab")
	@Parameters({"facAbbrev2"})
	public void testXPR_957(String facAbbrev2) throws Exception {
		logger.info("===== Testing - testXPR_957 =====");
		facility = new Facility(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Expected Results: - Verify that the Load Facility page is displayed");
		assertTrue(isOnLoadFacilityPage());

		logger.info("*** Action: - Enter a new Facility ID and tab out");
		facility.setFacilityId(facAbbrev2, wait);
		logger.info("       New Facility ID " + facAbbrev2);

		logger.info("*** Expected Results: - Verify that the Facility page is displayed with all fields are empty");
		verifyAllFieldsAreCleared(facAbbrev2);

		logger.info("*** Action: - Select Type is Hospital Lab and enter values in Header section");
		Header expectedHeader = setValuesInFacilityHeader(MiscMap.FAC_TYP_HOSP_LAB);

		logger.info("*** Action: - Enter values in Facility Additional Information section");
		FacilityAdditionalInformation expectedFacilityAdditionalInformation = setValuesInFacilityAdditionalInformation("Hospital Lab");

		logger.info("*** Action: - Enter values in Facility Address And Contact Information section");
		FacilityAddressAndContactInformation expectedFacilityAddressAndContactInformation = setValuesInFacilityAddressAndContactInformation();

		logger.info("*** Action: - Enter values in Default Remit To Address section");
		DefaultRemitToAddress expectedDefaultRemitToAddress = setValuesInDefaultRemitToAddress();

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(facility.saveAndClearBtn());

		logger.info("*** Expected Results: - Verify that the Load Facility page is displayed");
		assertTrue(isOnLoadFacilityPage());

		Fac fac = facilityDao.getFacByAbbrv( facAbbrev2);
		List<FacFacGrp> facFacGrpList = facilityDao.getFacFacGrpByFacAbbrev(facAbbrev2);

		logger.info("***  Expected Results: - Verify that the data in Header section are saved properly in DB");
		verifyHeaderDataSaved(expectedHeader, fac);

		logger.info("*** Expected Results: - Verify that the data in Facility Additional Information section are saved properly in DB");
		verifyFacilityAdditionalInformationDataSaved(expectedFacilityAdditionalInformation, "Draw Station", facAbbrev2);

		logger.info("*** Expected Results: - Verify that the data in Facility Address and Contact Information section are saved properly in DB");
		verifyFacilityAddressAndContactInformationDataSaved(expectedFacilityAddressAndContactInformation, fac);

		logger.info("*** Expected Results: - Verify that the data in Default Remit To Address section are saved properly in DB");
		verifyDefaultRemitToAddressDataSaved(expectedDefaultRemitToAddress, fac);

		logger.info("*** Action: - Load the newly created Facility ID and tab out");
		facility.setFacilityId(facAbbrev2,wait);
		facility.isFacilityLoaded(facAbbrev2,wait);

		logger.info("*** Expected Results: - Verify that the data in Header section are displayed properly in UI");
		Header actualHeader = getValuesInFacilityHeader();
		assertEquals(actualHeader, expectedHeader);

		logger.info("*** Expected Results: - Verify that the data in Facility Additional Information section are displayed properly in UI");
		FacilityAdditionalInformation actualFacilityAdditionalInformation=getValuesInFacilityAdditionalInformation("Hospital Lab");
		assertEquals(actualFacilityAdditionalInformation, expectedFacilityAdditionalInformation);

		logger.info("*** Expected Results: - Verify that the data in Facility Address and Contact Information section are displayed properly in UI");
		FacilityAddressAndContactInformation actualFacilityAddressAndContactInformation = getValuesInFacilityAddressAndContactInformation();
		assertEquals(actualFacilityAddressAndContactInformation, expectedFacilityAddressAndContactInformation);

		logger.info("*** Expected Results: - Verify that the data in Default Remit To Address section are displayed properly in UI");
		DefaultRemitToAddress actualDefaultRemitToAddress = getValuesInDefaultRemitToAddress();
		assertEquals(actualDefaultRemitToAddress, expectedDefaultRemitToAddress);

		logger.info("*** Action: - Reset");
		clickOnElement(facility.resetBtn());
		assertTrue(isOnLoadFacilityPage());
	}

	@Test(priority = 1, description = "Verify Help")
	public void testXPR_978() throws Exception {
		logger.info("===== Testing - testXPR_978 =====");
		facility = new Facility(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Expected Results: - Verify that the Load Facility page is displayed");
		assertTrue(isOnLoadFacilityPage());

		logger.info("*** Action: - Click on Help icon in Load Facility page Header section");
		clickOnElement(facility.helpIconAtHead());

		logger.info("*** Expected Results: - Verify that Facility Header help page is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_header.htm", "Facility Header"));

		logger.info("*** Action: - Click on Help icon in Load Facility page Enter Facility section");
		clickOnElement(facility.helpIconInFacilitySection());

		logger.info("*** Expected Results: - Verify that Facility section help page is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_enter_facility.htm", "Facility ID"));

		logger.info("*** Action: - Enter a new Facility ID and tab out");
		String facAbbrev = "AUTOFAC" + randomCharacter.getNonZeroRandomNumericString(6);
		facility.setFacilityId(facAbbrev, wait);
		logger.info("       New Facility ID " + facAbbrev);

		logger.info("*** Expected Results: - Verify that the Facility page is displayed with all fields are empty");
		verifyAllFieldsAreCleared(facAbbrev);

		logger.info("*** Action: - Click on Help icon in Header section");
		clickOnElement(facility.helpIconInHeaderSection());

		logger.info("*** Expected Results: - Verify that Facility Header Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_header.htm", "Facility Header"));

		logger.info("*** Action: - Click on Help icon in Facility Additional Information section");
		clickOnElement(facility.helpIconInFacilityAdditionalInformationSection());

		logger.info("*** Expected Results: - Verify that Facility Additional Information Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_facility_additional_information.htm", "Additional Information"));

		logger.info("*** Action: - Click on Help icon in Facility Address and Contact Information section");
		scrollToSection("Facility Address and Contact Information");
		clickOnElement(facility.helpIconInFacilityAddrCntctInfoSection());

		logger.info("*** Expected Results: - Verify that Facility Address and Contact Information Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_facility_address_contact_information.htm", "Facility Address and Contact Information"));

		logger.info("*** Action: - Click on Help icon in Correspondence Address section");
		scrollToSection("Correspondence Address");
		clickOnElement(facility.helpIconInCorrespondenceAddressSection());

		logger.info("*** Expected Results: - Verify that Correspondence Address Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_correspondence_address.htm", "Correspondence Address"));

		logger.info("*** Action: - Click on Help icon in Default Remit To Address section");
		scrollToSection("Default Remit To Address");
		clickOnElement(facility.helpIconInDefaultRemitToAddressSection());

		logger.info("*** Expected Results: - Verify that Default Remit To Address Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_default_remit_to_address.htm", "Default Remit to Address"));

		logger.info("*** Action: - Click on Help icon in Facility Cross Reference section");
		scrollToSection("Facility Cross Reference");
		clickOnElement(facility.helpIconInFacilityCrossReferenceSection());

		logger.info("*** Expected Results: - Verify that Facility Cross Reference Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_facility_cross_reference.htm", "Facility Cross-Reference"));

		logger.info("*** Action: - Click on Help icon in Jurisdiction Payor section");
		scrollToSection("Jurisdiction Payor");
		clickOnElement(facility.helpIconInJurisdictionPayorSection());

		logger.info("*** Expected Results: - Verify that Jurisdiction Payor Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_jurisdiction_payor.htm", "Jurisdiction Payor"));

		logger.info("*** Action: - Click on Help icon in Billing Override section");
		scrollToSection("Billing Override");
		clickOnElement(facility.helpIconInBillingOverrideSection());

		logger.info("*** Expected Results: - Verify that Billing Override Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_payor_billing_override.htm", "Facility – Billing Override"));

		logger.info("*** Action: - Click on Help icon in Remit Address Override section");
		scrollToSection("Remit Address Override");
		clickOnElement(facility.helpIconInRemitAddressOverrideSection());

		logger.info("*** Expected Results: - Verify that Remit Address Override Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_facility_remit_address_override.htm", "Remit Address Override"));

		logger.info("*** Action: - Reset");
		clickOnElement(facility.resetBtn());
		assertTrue(isOnLoadFacilityPage());
	}

	@Test(priority = 1, description = "Verify Run Audit button")
	@Parameters({"facAbbrev3"})
	public void testXPR_979(String facAbbrev3) throws Exception {
		logger.info("===== Testing - testXPR_979 =====");
		facility = new Facility(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Expected Results: - Verify that the Load Facility page is displayed");
		assertTrue(isOnLoadFacilityPage());

		logger.info("*** Action: - Enter a new Facility ID and tab out");
//		String facAbbrev = facAbbrev3;//"AUTOFAC" + randomCharacter.getNonZeroRandomNumericString(6);
		facility.setFacilityId(facAbbrev3, wait);
		logger.info("       New Facility ID " + facAbbrev3);

		logger.info("*** Expected Results: - Verify that the Facility page is displayed with all fields are empty");
		verifyAllFieldsAreCleared(facAbbrev3);

		logger.info("*** Action: - Select Type is Hospital Lab and enter values in Header section");
		Header expectedHeader = setValuesInFacilityHeader(MiscMap.FAC_TYP_HOSP_LAB);

		logger.info("*** Action: - Enter values in Facility Additional Information section");
		FacilityAdditionalInformation expectedFacilityAdditionalInformation = setValuesInFacilityAdditionalInformation("Hospital Lab");

		logger.info("*** Action: - Enter values in Facility Address And Contact Information section");
		FacilityAddressAndContactInformation expectedFacilityAddressAndContactInformation = setValuesInFacilityAddressAndContactInformation();

		logger.info("*** Action: - Enter values in Default Remit To Address section");
		DefaultRemitToAddress expectedDefaultRemitToAddress = setValuesInDefaultRemitToAddress();

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(facility.saveAndClearBtn());

		logger.info("*** Expected Results: - Verify that the Load Facility page is displayed");
		assertTrue(isOnLoadFacilityPage());

		Fac fac = facilityDao.getFacByAbbrv( facAbbrev3);
		List<FacFacGrp> facFacGrpList = facilityDao.getFacFacGrpByFacAbbrev(facAbbrev3);

		logger.info("***  Expected Results: - Verify that the data in Header section are saved properly in DB");
		verifyHeaderDataSaved(expectedHeader, fac);

		logger.info("*** Expected Results: - Verify that the data in Facility Additional Information section are saved properly in DB");
		verifyFacilityAdditionalInformationDataSaved(expectedFacilityAdditionalInformation, "Draw Station", facAbbrev3);

		logger.info("*** Expected Results: - Verify that the data in Facility Address and Contact Information section are saved properly in DB");
		verifyFacilityAddressAndContactInformationDataSaved(expectedFacilityAddressAndContactInformation, fac);

		logger.info("*** Expected Results: - Verify that the data in Default Remit To Address section are saved properly in DB");
		verifyDefaultRemitToAddressDataSaved(expectedDefaultRemitToAddress, fac);

		logger.info("*** Action: - Load the newly created Facility ID and tab out");
		facility.setFacilityId(facAbbrev3,wait);
		facility.isFacilityLoaded(facAbbrev3,wait);

		logger.info("*** Action: - Click on Run Audit button");
		clickOnElement(facility.runAuditBtn());

		logger.info("*** Expected Results: - Verify that Audit detail page is displayed properly");
		String parentWindow = switchToPopupWin();
		assertTrue(isElementPresent(facility.auditDetailTbl(), 5), "       Audit Detail table is shown.");

		WebElement action = facility.auditDetailTbl().findElement(By.xpath("//tbody/tr/td[contains(text(), 'C')]"));
		assertTrue(action.isDisplayed());
		WebElement table = facility.auditDetailTbl().findElement(By.xpath("//tbody/tr/td[contains(text(), 'FAC')]"));
		assertTrue(table.isDisplayed());

		switchToParentWin(parentWindow);

		logger.info("*** Action: - Reset");
		clickOnElement(facility.resetBtn());
		assertTrue(isOnLoadFacilityPage());
	}

	@Test(priority = 1, description = "NPI Search, NPI Global Search and NPPES Search")
	public void testXPR_981() throws Exception {
		logger.info("===== Testing - testXPR_981 =====");
		facility = new Facility(driver, wait);
		randomCharacter = new RandomCharacter(driver);
		npiSearch = new NpiSearch(driver);
		npiSearchResults = new NpiSearchResults(driver);
		npiGlobalSearch = new NpiGlobalSearch(driver);
		npiGlobalSearchResults = new NpiGlobalSearchResults(driver);
		xifinPortalUtils=new XifinPortalUtils(driver);

		logger.info("*** Expected Results: - Verify that the Load Facility page is displayed");
		assertTrue(isOnLoadFacilityPage());

		logger.info("*** Action: - Enter a new Facility ID and tab out");
		String facAbbrev = "AUTOFAC" + randomCharacter.getNonZeroRandomNumericString(6);
		facility.setFacilityId(facAbbrev, wait);
		logger.info("       New Facility ID " + facAbbrev);

		logger.info("*** Expected Results: - Verify that the Facility page is displayed with all fields are empty");
		verifyAllFieldsAreCleared(facAbbrev);

		logger.info("*** Action: - Click License Information Organization NPI NPI Search icon button");
		assertTrue(isElementPresent(facility.orgNPISearchIcon(),5),"        NPI Search Icon button should be displayed.");
		clickHiddenPageObject(facility.orgNPISearchIcon(),0);
		String parentWin = switchToPopupWin();

		logger.info("*** Expected Results: - Verify that NPI Search page is displayed");
		wait.until(ExpectedConditions.visibilityOf(npiSearch.npiTitle()));

		logger.info("*** Action: - Select NPI Entity");
		String npiEntity = getNPIEntity();
		selectItem(npiSearch.npiEntityDropdown(), npiEntity);

		logger.info("*** Expected Results: - Verify that Search fields are displayed properly based on selected NPI Entity");
		verifyNPISearchFieldsDisplayed(npiEntity);

		logger.info("*** Action: - Click on Search button");
		clickHiddenPageObject(npiSearch.npiSearchButton(),0);

		switchToPopupWin();

		logger.info("*** Expected Results: - Verify that NPI Search Results are displayed properly based on selected NPI Entity");
		verifyNPISearchResults(npiEntity);

		logger.info("*** Action: - Select the first NPI link in the NPI Search Results");
		String npi = npiSearchResults.getLinkValueFromWebTable("npisearchTable", 2, 2).getText().trim();
		xifinPortalUtils.selectFirstRowOnTable(npiSearchResults.npiTable(),1);
		switchToParentWin(parentWin);

		logger.info("*** Expected Results: - Verify that the NPI Search Results page is closed and the selected NPI is auto-populated in the Organization NPI Input field");
		wait.until(ExpectedConditions.visibilityOf(facility.facilityTile()));
		assertEquals(facility.organizationalNpiText(), npi, "         The Organizational NPI = " + npi + " should be populated.");

		logger.info("*** Action: - Click on Facility NPI Global Organization NPI search icon button");
		clickHiddenPageObject(facility.facNPIGlobalSearchIcon(),0);
		parentWin = switchToPopupWin();

		logger.info("*** Expected Results: - Verify that the NPI Global Search page is displayed");
		assertEquals(npiGlobalSearch.npiGlobalTitle().getText(),"NPI Global Search","        NPI Global Search screen should show.");

		logger.info("*** Action: - Select NPI Type");
		String npiType = getNpiType();
		selectItem(npiGlobalSearch.npiTypeInput(), npiType);

		logger.info("*** Expected Results: - Verify that the Provider Info are displayed properly based on the selected NPI Type");
		verifyNPIGlobalSearchProviderInfoDisplayed(npiType);

		logger.info("*** Action: - Click on Search button");
		clickHiddenPageObject(npiGlobalSearch.searchIconBtn(), 0);

		logger.info("*** Expected Results: - Verify that the Global NPI Search Results are displayed properly based on selected NPI Type");
		verifyGlobalNPISearchResults(npiType);

		logger.info("*** Action: - Select the first NPI link in the Global NPI Search Results");
		npi = npiSearchResults.getLinkValueFromWebTable("npisearchTable", 2, 2).getText().trim();
		xifinPortalUtils.selectFirstRowOnTable(npiSearchResults.npiTable(),1);
		switchToParentWin(parentWin);

		logger.info("*** Expected Results: - Verify that the Global NPI Search Results page is closed and the selected NPI is auto-populated in the Facility NPI Input field");
		wait.until(ExpectedConditions.visibilityOf(facility.facilityTile()));
		assertEquals(facility.facilityNpiText(), npi, "         The Facility NPI = " + npi + " should be populated.");

		logger.info("*** Action: - Click NPPES Search icon button");
		clickHiddenPageObject(facility.orgNPPESSearchIcon(), 0);
		parentWin = switchToPopupWin();

		logger.info("*** Expected Results: - Verify that the NPPES Webpage displayed");
		assertTrue(driver.getCurrentUrl().contains("https://nppes.cms.hhs.gov/#/"), "       The NPPES webpage should show.");

		switchToParentWin(parentWin);

		logger.info("*** Action: - Reset");
		clickOnElement(facility.resetBtn());
		assertTrue(isOnLoadFacilityPage());
	}

	@Test(priority = 1, description = "Add Facility with Type is Reference Lab")
	@Parameters({"facAbbrev4", "facTyp"})
	public void testXPR_959(String facAbbrev4, String facTyp) throws Exception {
		logger.info("===== Testing - testXPR_959 =====");
		logger.info("Create test Facility with type - Reference Lab");
		createNewFacility(facAbbrev4);
		Fac fac;

		logger.info("Clear XP Cache");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		facility = new Facility(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Expected Results: - Verify that the Load Facility page is displayed");
		MenuNavigation navigation = new MenuNavigation(driver, config);
		navigation.navigateToFacilityPage();
		assertTrue(isOnLoadFacilityPage());

		logger.info("*** Action: - Enter a new Facility ID and tab out");
//		String facilityAbbrev = facAbbrev;
		facility.setFacilityId(facAbbrev4, wait);
		logger.info("       New Facility ID " + facAbbrev4);

		logger.info("*** Action: - Enter values in Header section");
		Header expectedHeader = setValuesInFacilityHeaderReferenceLab("Reference Lab", facAbbrev4);

		logger.info("*** Action: - Enter values in Facility Additional Information section");
		FacilityAdditionalInformation expectedFacilityAdditionalInformation = setValuesInFacilityAdditionalInformation(facTyp);

		logger.info("*** Action: - Enter values in Facility Address And Contact Information section");
		FacilityAddressAndContactInformation expectedFacilityAddressAndContactInformation = setValuesInFacilityAddressAndContactInformation();

		logger.info("*** Action: - Enter values in Correspondence Address section");
		CorrespondenceAddress expectedCorrespondenceAddress = setValuesInCorrespondenceAddressReferenceLab();

		logger.info("*** Action: - Enter values in Default Remit To Address section");
		DefaultRemitToAddress expectedDefaultRemitToAddress = setValuesInDefaultRemitToAddress();

		logger.info("*** Action: - Enter values in Facility Cross Reference section");
		FacilityCrossReference expectedFacilityCrossReference = setValuesInFacilityCrossReference("1");

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(facility.saveAndClearBtn());

		logger.info("*** Expected Results: - Verify that the Load Facility page is displayed");
		assertTrue(isOnLoadFacilityPage());

		fac = facilityDao.getFacByAbbrv( facAbbrev4);
		List<FacFacGrp> facFacGrpList = facilityDao.getFacFacGrpByFacAbbrev(facAbbrev4);

		logger.info("***  Expected Results: - Verify that the data in Header section are saved properly in DB");
		verifyHeaderDataSaved(expectedHeader, fac);

		logger.info("*** Expected Results: - Verify that the data in Facility Additional Information section are saved properly in DB");
		verifyFacilityAdditionalInformationDataSaved(expectedFacilityAdditionalInformation, facTyp, facAbbrev4);

		logger.info("*** Expected Results: - Verify that the data in Facility Address and Contact Information section are saved properly in DB");
		verifyFacilityAddressAndContactInformationDataSaved(expectedFacilityAddressAndContactInformation, fac);

		logger.info("*** Expected Results: - Verify that the data in Correspondence Address section are saved properly in DB");
		verifyCorrespondenceAddressDataSaved(expectedCorrespondenceAddress, fac);

		logger.info("*** Expected Results: - Verify that the data in Default Remit To Address section are saved properly in DB");
		verifyDefaultRemitToAddressDataSaved(expectedDefaultRemitToAddress, fac);

		logger.info("***  Expected Results: - Verify that the data in Facility Cross Reference section are saved properly in DB");
		int xRefId = crossReferenceDao.getXrefByDescr( expectedFacilityCrossReference.getCrossReferenceDescription().split("-")[1]).getXrefId();
		FacXref facXrefData = facilityDao.getFacXrefByFacAbbrevAndXrefId(facAbbrev4, xRefId);
		verifyFacilityCrossReferenceDataSaved(expectedFacilityCrossReference,facXrefData);

		logger.info("*** Action: - Load the newly created Facility ID and tab out");
		facility.setFacilityId(facAbbrev4,wait);
		facility.isFacilityLoaded(facAbbrev4,wait);

		logger.info("*** Expected Results: - Verify that the data in Header section are displayed properly in UI");
		Header actualHeader = getValuesInFacilityHeaderReferenceLab();
		assertEquals(actualHeader, expectedHeader);

		logger.info("*** Expected Results: - Verify that the data in Facility Additional Information section are displayed properly in UI");
		FacilityAdditionalInformation actualFacilityAdditionalInformation = getValuesInFacilityAdditionalInformation(facTyp);
		assertEquals(actualFacilityAdditionalInformation, expectedFacilityAdditionalInformation);

		logger.info("*** Expected Results: - Verify that the data in Facility Address and Contact Information section are displayed properly in UI");
		FacilityAddressAndContactInformation actualFacilityAddressAndContactInformation = getValuesInFacilityAddressAndContactInformation();
		assertEquals(actualFacilityAddressAndContactInformation, expectedFacilityAddressAndContactInformation);

		logger.info("*** Expected Results: - Verify that the data in Correspondence Address section are displayed properly in UI");
		CorrespondenceAddress actualCorrespondenceAddress = getValuesInCorrespondenceAddress();
		assertEquals(actualCorrespondenceAddress, expectedCorrespondenceAddress);

		logger.info("*** Expected Results: - Verify that the data in Default Remit To Address section are displayed properly in UI");
		DefaultRemitToAddress actualDefaultRemitToAddress = getValuesInDefaultRemitToAddress();
		assertEquals(actualDefaultRemitToAddress, expectedDefaultRemitToAddress);

		logger.info("***  Expected Results: - Verify that the data in Facility Cross Reference section are displayed properly in UI");
		FacilityCrossReference actualFacilityCrossReference = getValuesInFacilityCrossReference("1");
		assertEquals(actualFacilityCrossReference, expectedFacilityCrossReference);

		logger.info("*** Action: - Reset");
		clickOnElement(facility.resetBtn());
		assertTrue(isOnLoadFacilityPage());
	}

	private void createNewFacility(String facAbbrev4) throws XifinDataAccessException
	{
		Fac fac = new Fac();
		int nextSeq = databaseSequenceDao.getNextSequenceFromOracle("FAC_PK_FAC_ID_SEQ");
		logger.info(nextSeq);
		fac.setFacId(nextSeq);
		fac.setAbbrv(facAbbrev4);
		fac.setName(facAbbrev4);
		fac.setFacTypId(5);
		fac.setNpi(14744666L);
		facilityDao.setFac(fac);
	}

	private boolean isOnLoadFacilityPage() throws Exception{
		boolean isOnPage = false;
		facility = new Facility(driver, wait);

		assertTrue(isElementPresent(facility.facilityTile(),5),"        The load Facility page should be displayed.");
		isOnPage = facility.facilityTile().getText().equals("Facility");

		assertTrue(isElementPresent(facility.facIdInput(),5),"        The Facility ID input field should be displayed.");

		return isOnPage;
	}

	private void verifyAllFieldsAreCleared(String facAbbrev) throws Exception {
		assertTrue(isElementPresent(facility.facilityTile(),5),"        The detail Facility page should be displayed.");
		assertTrue(isElementPresent(facility.facId(), 5),"        The FacilityId should be displayed.");
		assertEquals(facility.facId().getText(), facAbbrev.toUpperCase(), "        The FacilityId should be displayed.");
		assertEquals(facility.primFacIdText().getText(), " ", "        The primary FacId should be empty.");
		assertEquals(facility.billingFacIdText().getText(), "", "        The Billing FacId should be empty.");
		assertEquals(facility.websiteInput().getText(), "", "        The Website should be empty.");
		assertEquals(facility.facTypeText().getText(), "", "        The Facility Name Input should be empty.");
		assertEquals(facility.facNameInput().getAttribute("value"), "", "        The Facility Type should be empty.");
		assertFalse(facility.whollyOwnedSubsidiaryChkBox().getAttribute("class").contains("dirty"));
		assertFalse(facility.whollyOwnedSubsidiaryChkBox().getAttribute("class").contains("dirty"));

		//Facility Additional Information grid
		//SS#4201(Service Types Supported); SVC_TYP table
		assertFalse(facility.serviceTypesChkBox("Diagnostic Lab").getAttribute("class").contains("dirty"));
		assertFalse(facility.serviceTypesChkBox("Mammogram, High Risk Patient").getAttribute("class").contains("dirty"));
		assertFalse(facility.serviceTypesChkBox("Mammogram, Low Risk Patient").getAttribute("class").contains("dirty"));
		assertFalse(facility.serviceTypesChkBox("Pathology").getAttribute("class").contains("dirty"));
		assertFalse(facility.serviceTypesChkBox("Radiology").getAttribute("class").contains("dirty"));
		assertEquals(facility.licenseIdInput("Tax ID").getText(), "", "        The Tax ID textbox should be empty.");
		assertEquals(facility.licenseIdInput("Organizational NPI").getText(), "", "        The Organizational NPI textbox should be empty.");
		assertEquals(facility.licenseIdInput("Facility NPI").getText(), "", "        The Facility NPI textbox should be empty.");
		assertEquals(facility.taxonomyCodeInput().getText(), "", "        The Taxonomy Code textbox should be empty.");
		assertEquals("Empty records", facility.facGrpTblPagerNavRight().getText(), "        Facility Group table should be empty.");

		//Facility Address and Contact Information grid
		assertEquals(facility.facContact1Input().getText(), "", "        The Contact 1 textbox should be empty.");
		assertEquals(facility.facAddr1Input().getText(), "", "        The Address 1 textbox should be empty.");
		assertEquals(facility.facAddr2Input().getText(), "", "        The Address 2 textbox should be empty.");
		assertEquals(facility.facCountryInput().getText(), "", "        The Facility Address and Contact Information > Country textbox should be empty.");
		assertEquals(facility.facZipInput().getText(), "", "        The Postal Code textbox should be empty.");
		assertEquals(facility.facPhone1Input().getText(), "", "        The Address 2 textbox should be empty.");
		assertEquals(facility.facFax1Input().getText(), "", "        The Fax 1 textbox should be empty.");
		assertEquals(facility.facContact2Input().getText(), "", "        The Contact 2 textbox should be empty.");
		assertEquals(facility.facPhone2Input().getText(), "", "        The Phone 2 textbox should be empty.");
		assertEquals(facility.facFax2Input().getText(), "", "        The Fax 2 textbox should be empty.");
		assertEquals(facility.facStateInput().getText(), "", "        The State textbox should be empty.");
		assertEquals(facility.facCityInput().getText(), "", "        The city textbox should be empty.");
		assertEquals(facility.facEmail1Input().getText(), "", "        The Email 1 textbox should be empty.");
		assertEquals(facility.facEmail2Input().getText(), "", "        The Email 2 textbox should be empty.");

		//Correspondence Address grid
		assertEquals(facility.correspFacNameInput().getText(), "", "        The facilityName textbox should be empty.");
		assertEquals(facility.correspAddr1Input().getText(), "", "        The address1 textbox should be empty.");
		assertEquals(facility.correspAddr2Input().getText(), "", "        The address2 textbox should be empty.");
		assertEquals(facility.correspEmail1Input().getText(), "", "       Correspondence Email1 input field should be empty.");

		//Default Remit To Address grid
		assertEquals(facility.defaultRemitToAddressSectionRemitFacilityNameTextbox().getText(), "", "        The Default Remit To Address Section > Facility Name textbox should be blank.");
		assertEquals(facility.defaultRemitToAddressSectionRemitAddressTextbox(1).getText(), "", "        The Default Remit To Address Section > Address 1 textbox should be blank.");
		assertEquals(facility.defaultRemitToAddressSectionRemitAddressTextbox(2).getText(), "", "        The Default Remit To Address Section > Address 2 textbox should be blank.");
		assertEquals( facility.defaultRemitToAddressSectionCountryCombobox().getText(), "USA", "        The Default Remit To Address Section > Country combobox should be shown as default (USA).");
		assertEquals(facility.defaultRemitToAddressSectionPostalCodeTextBox().getText(), "", "        The Default Remit To Address Section > Postal Code textbox should be blank.");
		assertEquals(facility.defaultRemitToAddressSectionPhoneTextBox(1).getText(), "", "        The Default Remit To Address Section > Phone 1 textbox should be blank.");
		assertEquals(facility.defaultRemitToAddressSectionPhoneTextBox(2).getText(), "", "        The Default Remit To Address Section > Phone 2 textbox should be blank.");
		assertEquals(facility.defaultRemitToAddressSectionFax1TextBox().getText(), "", "        The Default Remit To Address Section > Fax 1 textbox should be blank.");
		assertEquals(facility.defaultRemitToAddressSectionStateCombobox().getText(), "", "        The Default Remit To Address Section > State combobox should be blank.");
		assertEquals(facility.defaultRemitToAddressSectionCityTextBox().getText(), "", "        The Default Remit To Address Section > City textbox should be blank.");
		assertEquals(facility.defaultRemitToAddressSectionEmailTextBox().getText(), "", "        The Default Remit To Address Section > Email textbox should be blank.");

		//Facility Cross Reference grid
		assertEquals(facility.facilityCrossReferenceTableBottomNav().getText(), "Empty records", "        Facility Cross Reference table should be empty.");
		//Jurisdiction Payor grid
		assertEquals(facility.jurisdictionPayorTableBottomNav().getText(), "Empty records", "        Jurisdiction Payor table should be empty.");
		//Billing Override grid
		assertEquals(facility.billingOverrideTableBottomNav().getText(), "Empty records", "        Billing Override table should be empty.");
		//Remit Address Override grid
		assertEquals(facility.remitAddressOverrideTablePayorGroupOverrideRemitAddressBottomNav().getText(),"Empty records", "        Remit Address Override > Payor Group Override Remit Address table should be empty.");
		assertEquals(facility.remitAddressOverrideTablePayorOverrideRemitAddressBottomNav().getText(), "Empty records",  "        Remit Address Override > Payor Override Remit Address table should be empty.");
	}

	private Header setValuesInFacilityHeader(int facTypId) throws Exception{
		Header header = new Header();
		FacTyp facTyp = facilityDao.getFacTypByFacTypId(facTypId);

		//Primary Facility disabled
		assertTrue(facility.primFacIdText().getAttribute("onclick").contains("false"),"        Primary Facility dropdown should be disabled.");
		header.setPrimaryFacility(null);
		//Billing Facility
		Fac billingfacility = facilityDao.getFacsByFacTypId(MiscMap.FAC_TYP_REMOTE_AFFIL).get(0);
		String billingFac = billingfacility.getAbbrv()+" - "+billingfacility.getName();
		clickOnElement(facility.billingFacIdText());
		List<String> allOptionsBillingFac = facility.getAllOptionsFromBillingFac();
		String firstOption = allOptionsBillingFac.get(0);
		assertEquals(firstOption, "");
		assertTrue(allOptionsBillingFac.contains("Use Client Primary Facility"));
		setInputValue(facility.inputFieldOnDropdownbox(),billingFac);
		header.setBillingFacility(billingFac);
		//Website
		String url="www.google.com";
		setInputValue(facility.websiteInput(),url);
		header.setWebsite(url);
		//Name
		String facNm="AUTOFACNAME"+randomCharacter.getNonZeroRandomNumericString(6);
		setInputValue(facility.facNameInput(),facNm);
		header.setName(facNm);
		//Type
		clickOnElement(facility.facTypeDropdownList());
		setInputValue(facility.inputFieldOnDropdownbox(),facTyp.getDescr());
		header.setType(facTyp.getDescr());
		//Wholly Owned Subsidiary: Checked
		selectCheckBox(facility.whollyOwnedSubsidiaryChkBox());
		header.setWhollyOwnedSubsidiary(true);
		//Delete Facility Demographics Record: Not checked
		header.setDeleteFacilityDemographicsRecord(false);

		return header;
	}

	private Header setValuesInFacilityHeaderReferenceLab(String facType, String facAbbrev) throws Exception{
		Header header = new Header();

		//Primary Facility disabled
		assertTrue(facility.primFacIdText().getAttribute("onclick").contains("false"),"        Primary Facility dropdown should be disabled.");
		logger.info("Make sure the facility type is ReferenceLab");
		assertEquals(facility.facTypeDropdownList().getText(),facType);
		clickOnElement(facility.billingFacIdText());
		List<String> allOptionsBillingFac = facility.getAllOptionsFromBillingFac();
		String firstOption = allOptionsBillingFac.get(0);
		assertNotEquals(firstOption, "");
		assertTrue(allOptionsBillingFac.contains("Use Client Primary Facility"));
		setInputValue(facility.inputFieldOnDropdownbox(),firstOption);
		//header.setBillingFacility(billingFac);
		assertTrue(facility.whollyOwnedSubsidiaryChkBox().isDisplayed());
		//Website
		String url="www.google.com";
		setInputValue(facility.websiteInput(),url);
		header.setBillingFacility(firstOption);
		header.setWebsite(url);
		header.setWhollyOwnedSubsidiary(false);
		//Delete Facility Demographics Record: Not checked
		header.setDeleteFacilityDemographicsRecord(false);
		header.setName(facAbbrev);
		header.setType(facType);
		header.setIsUseClnPrimaryFacBilling(true);
		return header;
	}
	private Header getValuesInFacilityHeaderReferenceLab() {
		Header header = new Header();

		//Primary Facility disabled
		assertTrue(facility.primFacIdText().getAttribute("onclick").contains("false"),"        Primary Facility dropdown should be disabled.");
		header.setPrimaryFacility(null);
		//Billing Facility
		header.setBillingFacility(facility.billingFacIdDropdownList().getText().trim());
		//Website
		header.setWebsite(facility.websiteInput().getText().trim());
		//Name
		header.setName(facility.facNameText().trim());
		//Type
		header.setType(facility.facTypeDropdownList().getText().trim());
		//Wholly Owned Subsidiary: Checked
		header.setWhollyOwnedSubsidiary(false);
		//Delete Facility Demographics Record: Not checked
		header.setDeleteFacilityDemographicsRecord(false);

		return header;
	}

	private Header getValuesInFacilityHeader() {
		Header header = new Header();

		//Primary Facility disabled
		assertTrue(facility.primFacIdText().getAttribute("onclick").contains("false"),"        Primary Facility dropdown should be disabled.");
		header.setPrimaryFacility(null);
		//Billing Facility
		header.setBillingFacility(facility.billingFacIdDropdownList().getText().trim());
		//Website
		header.setWebsite(facility.websiteInput().getText().trim());
		//Name
		header.setName(facility.facNameText().trim());
		//Type
		header.setType(facility.facTypeDropdownList().getText().trim());
		//Wholly Owned Subsidiary: Checked
		header.setWhollyOwnedSubsidiary(true);
		//Delete Facility Demographics Record: Not checked
		header.setDeleteFacilityDemographicsRecord(false);

		return header;
	}

	private FacilityAdditionalInformation setValuesInFacilityAdditionalInformation(String facTyp) throws Exception {
		FacilityAdditionalInformation facilityAdditionalInformation = new FacilityAdditionalInformation();
		boolean isDiagnosticLab=false;
		boolean isMammogramHighRiskPatient=false;
		boolean isMammogramLowRiskPatient=false;
		boolean isPathology=false;
		boolean isRadiology=false;

		//Service Types (The Service Types options will be hidden if Facility Type is Draw Station)
		String clia=randomCharacter.getNonZeroRandomNumericString(2)+randomCharacter.getRandomAlphaString(1)+randomCharacter.getNonZeroRandomNumericString(7);
		String mammoCert=randomCharacter.getNonZeroRandomNumericString(10);
		if (!facTyp.equalsIgnoreCase("Draw Station")){
			selectCheckBox(facility.serviceTypesChkBox("Diagnostic Lab"));
			selectCheckBox(facility.serviceTypesChkBox("Mammogram, High Risk Patient"));
			selectCheckBox(facility.serviceTypesChkBox("Mammogram, Low Risk Patient"));
			selectCheckBox(facility.serviceTypesChkBox("Pathology"));
			selectCheckBox(facility.serviceTypesChkBox("Radiology"));
			//CLIA input shows when selected Diagnostic Lab
			setInputValue(facility.licenseIdInput("CLIA"),clia);
			//Mammo Cert input shows when selected Mammogram Service Type
			setInputValue(facility.licenseIdInput("Mammo Cert"),mammoCert);

			isDiagnosticLab=true;
			isMammogramHighRiskPatient=true;
			isMammogramLowRiskPatient=true;
			isPathology=true;
			isRadiology=true;
		}
		//Facility Specialty Information

		//License Information
		String taxId=randomCharacter.getNonZeroRandomNumericString(10);
		String organizationalNpi=randomCharacter.getNonZeroRandomNumericString(10);
		String facilityNpi=randomCharacter.getNonZeroRandomNumericString(10);
		String taxonomyCode=taxonomyDao.getTaxonomy().getTaxonomyCd();
		String facSpecialtyType = "Critical Access Hospital";
		facility.setFacSpecialtyType(facSpecialtyType, wait);

		setInputValue(facility.licenseIdInput("Tax ID"),taxId);
		setInputValue(facility.licenseIdInput("Organizational NPI"),organizationalNpi);
		setInputValue(facility.licenseIdInput("Facility NPI"),facilityNpi);
		setInputValue(facility.taxonomyCodeInput(),taxonomyCode);

		//Facility Group
		clickOnElement(facility.facGrpTbladdIcon());
		clickOnElement(facility.facGrpDropdown());
		selectItem(facility.facGrpIdDropdown(),"Hospitals");

		if (!facTyp.equalsIgnoreCase("Draw Station")){
            facilityAdditionalInformation.setTaxId(taxId);
            facilityAdditionalInformation.setOrganizationalNpi(organizationalNpi);
            facilityAdditionalInformation.setFacilityNpi(facilityNpi);
            facilityAdditionalInformation.setTaxonomyCode(taxonomyCode);
            facilityAdditionalInformation.setFacilityGroup(new HashSet<>(Collections.singletonList("Hospitals")));
            facilityAdditionalInformation.setDiagnosticLab(isDiagnosticLab);
            facilityAdditionalInformation.setMammogramHighRiskPatient(isMammogramHighRiskPatient);
            facilityAdditionalInformation.setMammogramLowRiskPatient(isMammogramLowRiskPatient);
            facilityAdditionalInformation.setPathology(isPathology);
            facilityAdditionalInformation.setRadiology(isRadiology);
            facilityAdditionalInformation.setClia(clia);
            facilityAdditionalInformation.setMammoCert(mammoCert);
            facilityAdditionalInformation.setFacilitySpecialtyType(facSpecialtyType);

		}
		else{
            facilityAdditionalInformation.setTaxId(taxId);
            facilityAdditionalInformation.setOrganizationalNpi(organizationalNpi);
            facilityAdditionalInformation.setFacilityNpi(facilityNpi);
            facilityAdditionalInformation.setTaxonomyCode(taxonomyCode);
            facilityAdditionalInformation.setFacilityGroup(new HashSet<>(Collections.singletonList("Hospitals")));
			facilityAdditionalInformation.setFacilitySpecialtyType(facSpecialtyType);
		}

		return facilityAdditionalInformation;
	}

	private FacilityAdditionalInformation getValuesInFacilityAdditionalInformation(String facTyp) {
		FacilityAdditionalInformation facilityAdditionalInformation = new FacilityAdditionalInformation();

        facilityAdditionalInformation.setTaxId(facility.taxIdText());
        facilityAdditionalInformation.setOrganizationalNpi(facility.organizationalNpiText());
        facilityAdditionalInformation.setFacilityNpi(facility.facilityNpiText());
        facilityAdditionalInformation.setTaxonomyCode(facility.taxonomyCodeText().split("-")[0].trim());
        facilityAdditionalInformation.setFacilitySpecialtyType(facility.facSpecialtyType());

		int facilityGroupTotalRecord=Integer.valueOf(facility.facilityGroupTblTotalRecord().getText().trim().split(" ")[5]);
		//System.out.println("facilityGroupTotalRecord=" + facilityGroupTotalRecord);
		List<String> facGrpList=new ArrayList<>();
		for (int i = 0; i < facilityGroupTotalRecord; i++){
			facGrpList.add(facility.facilityGroupTblFacilityGroupColText(i).getText().trim());
		}
        facilityAdditionalInformation.setFacilityGroup(new HashSet<>(facGrpList));

		if (!facTyp.equals("Draw Station")){
            facilityAdditionalInformation.setDiagnosticLab(facility.isServiceTypeChkBoxChecked(0));
            facilityAdditionalInformation.setMammogramHighRiskPatient(facility.isServiceTypeChkBoxChecked(1));
            facilityAdditionalInformation.setMammogramLowRiskPatient(facility.isServiceTypeChkBoxChecked(2));
            facilityAdditionalInformation.setPathology(facility.isServiceTypeChkBoxChecked(3));
            facilityAdditionalInformation.setRadiology(facility.isServiceTypeChkBoxChecked(4));
            facilityAdditionalInformation.setClia(facility.cliaText());
            facilityAdditionalInformation.setMammoCert(facility.mammoCertText());
		}

		return facilityAdditionalInformation;
	}

	private FacilityAddressAndContactInformation setValuesInFacilityAddressAndContactInformation() throws Exception{
		FacilityAddressAndContactInformation facilityAddressAndContactInformation=new FacilityAddressAndContactInformation();

		String contact1="FACCONTACT1"+randomCharacter.getNonZeroRandomNumericString(6);
		String address1="FACADDR1"+randomCharacter.getNonZeroRandomNumericString(6);
		String address2="FACADDR2"+randomCharacter.getNonZeroRandomNumericString(6);
		String stateId="CA";
		List<Zip> zipList=zipDao.getZipByState(stateId);
		String postalCode=zipList.get(0).zipId;
		String city=zipList.get(0).ctyNm;
		String state=stateDao.getStateByStateId(stateId).get(0).name;
		String phone1="8585677788";
		String fax1=phone1;
		String email1="qatester1@xifin.com";
		String contact2="FACCONTACT2"+randomCharacter.getNonZeroRandomNumericString(6);
		String phone2="6199995678";
		String fax2=phone2;
		String email2="qatester2@xifin.com";
		String country="USA";

		setInputValue(facility.facContact1Input(),contact1);
		setInputValue(facility.facAddr1Input(),address1);
		setInputValue(facility.facAddr2Input(),address2);
		setInputValue(facility.facPhone1Input(),phone1);
		setInputValue(facility.facFax1Input(),fax1);
		setInputValue(facility.facEmail1Input(),email1);
		setInputValue(facility.facContact2Input(),contact2);
		setInputValue(facility.facPhone2Input(),phone2);
		setInputValue(facility.facFax2Input(),fax2);
		setInputValue(facility.facEmail2Input(),email2);

		clickOnElement(facility.facZipSearchIconBtn());
		String parrentWin = switchToPopupWin();
		ZipCodeSearch zipCodeSearch = new ZipCodeSearch(driver);
		ZipCodeSearchResults zipCodeSearchResults = new ZipCodeSearchResults(driver);
		setInputValue(zipCodeSearch.zipCodeInput(),postalCode);
		clickOnElement(zipCodeSearch.searchBtn());
		xifinPortalUtils = new XifinPortalUtils(driver);
		xifinPortalUtils.selectFirstRowOnTable(zipCodeSearchResults.zipCodeSearchResultsTable(),1);
		switchToParentWin(parrentWin);
		//Verify that the City and State values are populated after selecting the zip code
		assertEquals(postalCode, facility.facZipInput().getAttribute("value"), "         The Facility Postal Code " + postalCode + " should be populated.");
		assertEquals(city, facility.facCityInput().getAttribute("value"), "         The Facility City " + city + " should be populated.");
		assertEquals(state, facility.facState().getText(), "         The Facility State " + state + " should be populated.");

		setInputValue(facility.facZipInput(),postalCode+"-1234");

		facilityAddressAndContactInformation.setContact1(contact1);
		facilityAddressAndContactInformation.setAddress1(address1);
		facilityAddressAndContactInformation.setAddress2(address2);
		facilityAddressAndContactInformation.setCountry(country);
		facilityAddressAndContactInformation.setState(state);
		facilityAddressAndContactInformation.setPostalCode(postalCode+"-1234");
		facilityAddressAndContactInformation.setPhone1(phone1);
		facilityAddressAndContactInformation.setFax1(fax1);
		facilityAddressAndContactInformation.setEmail1(email1);
		facilityAddressAndContactInformation.setContact2(contact2);
		facilityAddressAndContactInformation.setPhone2(phone2);
		facilityAddressAndContactInformation.setFax2(fax2);
		facilityAddressAndContactInformation.setEmail2(email2);

		return facilityAddressAndContactInformation;
	}

	private FacilityAddressAndContactInformation getValuesInFacilityAddressAndContactInformation() {
		FacilityAddressAndContactInformation facilityAddressAndContactInformation=new FacilityAddressAndContactInformation();

		facilityAddressAndContactInformation.setContact1(facility.facContact1Input().getAttribute("value"));
		facilityAddressAndContactInformation.setAddress1(facility.facAddr1Input().getAttribute("value"));
		facilityAddressAndContactInformation.setAddress2(facility.facAddr2Input().getAttribute("value"));
		facilityAddressAndContactInformation.setCountry(facility.facCountryTxt().getText());
		facilityAddressAndContactInformation.setPostalCode(facility.facZipInput().getAttribute("value"));
		facilityAddressAndContactInformation.setPhone1(facility.facPhone1Input().getAttribute("value").replaceAll("[^0-9]", "").trim());
		facilityAddressAndContactInformation.setFax1(facility.facFax1Input().getAttribute("value").replaceAll("[^0-9]", "").trim());
		facilityAddressAndContactInformation.setContact2(facility.facContact2Input().getAttribute("value"));
		facilityAddressAndContactInformation.setPhone2(facility.facPhone2Input().getAttribute("value").replaceAll("[^0-9]", "").trim());
		facilityAddressAndContactInformation.setFax2(facility.facFax2Input().getAttribute("value").replaceAll("[^0-9]", "").trim());
		facilityAddressAndContactInformation.setState(facility.facStateTxt().getText());
		//facilityAddressAndContactInformation.setCity(facility.facCityInput().getAttribute("value"));
		facilityAddressAndContactInformation.setEmail1(facility.facEmail1Input().getAttribute("value"));
		facilityAddressAndContactInformation.setEmail2(facility.facEmail2Input().getAttribute("value"));

		return facilityAddressAndContactInformation;
	}

	private CorrespondenceAddress setValuesInCorrespondenceAddress() throws Exception{
		CorrespondenceAddress correspondenceAddress=new CorrespondenceAddress();

		String facilityName="FACN"+randomCharacter.getNonZeroRandomNumericString(6);
		String address1="FACADDR1"+randomCharacter.getNonZeroRandomNumericString(6);
		String address2="FACADDR2"+randomCharacter.getNonZeroRandomNumericString(6);
		String stateId="CA";
		List<Zip> zipList=zipDao.getZipByState(stateId);
		String postalCode=zipList.get(0).zipId;
		String city=zipList.get(0).ctyNm;
		String state=stateDao.getStateByStateId(stateId).get(0).name;
		String phone1="8585677788";
		String fax1=phone1;
		String email1="qatester1@xifin.com";
		String country="USA";

		setInputValue(facility.correspFacNameInput(),facilityName);
		setInputValue(facility.correspAddr1Input(),address1);
		setInputValue(facility.correspAddr2Input(),address2);
		setInputValue(facility.correspZipInput(),postalCode);
		setInputValue(facility.correspPhone1Input(),phone1);
		setInputValue(facility.correspFax1Input(),fax1);
		setInputValue(facility.correspEmail1Input(),email1);
		setInputValue(facility.correspCityInput(),city);
		clickOnElement(facility.correspSateDropdown());
		selectItem(facility.correspSateDropdown(),state);

		correspondenceAddress.setFacilityName(facilityName);
		correspondenceAddress.setAddress1(address1);
		correspondenceAddress.setAddress2(address2);
		correspondenceAddress.setCountry(country);
		correspondenceAddress.setPostalCode(postalCode);
		correspondenceAddress.setPhone1(phone1);
		correspondenceAddress.setFax1(fax1);
		correspondenceAddress.setEmail1(email1);
		correspondenceAddress.setCity(city);
		correspondenceAddress.setState(state);

		return correspondenceAddress;
	}

	private CorrespondenceAddress setValuesInCorrespondenceAddressReferenceLab() throws Exception{
		CorrespondenceAddress correspondenceAddress=new CorrespondenceAddress();

		String facilityName="FACN"+randomCharacter.getNonZeroRandomNumericString(6);
		String address1="FACADDR1"+randomCharacter.getNonZeroRandomNumericString(6);
		String address2="FACADDR2"+randomCharacter.getNonZeroRandomNumericString(6);
		String stateId="CA";
		List<Zip> zipList=zipDao.getZipByState(stateId);
		String postalCode=zipList.get(0).zipId+"-1234";
		String city=zipList.get(0).ctyNm;
		String state=stateDao.getStateByStateId(stateId).get(0).name;
		String phone1="8585677788";
		String fax1=phone1;
		String email1="qatester1@xifin.com";
		String country="USA";

		setInputValue(facility.correspFacNameInput(),facilityName);
		setInputValue(facility.correspAddr1Input(),address1);
		setInputValue(facility.correspAddr2Input(),address2);
		setInputValue(facility.correspZipInput(),postalCode);
		setInputValue(facility.correspPhone1Input(),phone1);
		setInputValue(facility.correspFax1Input(),fax1);
		setInputValue(facility.correspEmail1Input(),email1);
		setInputValue(facility.correspCityInput(),city);
		clickOnElement(facility.correspSateDropdown());
		selectItem(facility.correspSateDropdown(),state);

		correspondenceAddress.setFacilityName(facilityName);
		correspondenceAddress.setAddress1(address1);
		correspondenceAddress.setAddress2(address2);
		correspondenceAddress.setCountry(country);
		correspondenceAddress.setPostalCode(postalCode);
		correspondenceAddress.setPhone1(phone1);
		correspondenceAddress.setFax1(fax1);
		correspondenceAddress.setEmail1(email1);
		correspondenceAddress.setCity(city);
		correspondenceAddress.setState(state);

		return correspondenceAddress;
	}

	private CorrespondenceAddress getValuesInCorrespondenceAddress() {
		CorrespondenceAddress correspondenceAddress=new CorrespondenceAddress();

		correspondenceAddress.setFacilityName(facility.correspFacNameInput().getAttribute("value"));
		correspondenceAddress.setAddress1(facility.correspAddr1Input().getAttribute("value"));
		correspondenceAddress.setAddress2(facility.correspAddr2Input().getAttribute("value"));
		correspondenceAddress.setCountry(facility.correspCountryTxt().getText());
		correspondenceAddress.setPostalCode(facility.correspZipInput().getAttribute("value"));
		correspondenceAddress.setPhone1(facility.correspPhone1Input().getAttribute("value").replaceAll("[^0-9]", "").trim());
		correspondenceAddress.setFax1(facility.correspFax1Input().getAttribute("value").replaceAll("[^0-9]", "").trim());
		correspondenceAddress.setState(facility.correspStateTxt().getText());
		correspondenceAddress.setCity(facility.correspCityInput().getAttribute("value"));
		correspondenceAddress.setEmail1(facility.correspEmail1Input().getAttribute("value"));

		return correspondenceAddress;
	}


	private DefaultRemitToAddress setValuesInDefaultRemitToAddress() throws Exception{
		DefaultRemitToAddress defaultRemitToAddress=new DefaultRemitToAddress();

		String facilityName="FACN"+randomCharacter.getNonZeroRandomNumericString(6);
		String address1="FACADDR1"+randomCharacter.getNonZeroRandomNumericString(6);
		String address2="FACADDR2"+randomCharacter.getNonZeroRandomNumericString(6);
		String stateId="FL";
		List<Zip> zipList=zipDao.getZipByState(stateId);
		String postalCode=zipList.get(0).zipId+"-1234";
		String city=zipList.get(0).ctyNm;
		String state=stateDao.getStateByStateId(stateId).get(0).name;
		String phone1="6195677788";
		String phone2="7145677788";
		String fax1=phone1;
		String email1="qatester2@xifin.com";
		String country="USA";

		setInputValue(facility.remitFacilityNameInput(),facilityName);
		setInputValue(facility.remitAddress1Input(),address1);
		setInputValue(facility.remitAddress2Input(),address2);
		setInputValue(facility.remitPostalCodeInput(),postalCode);
		setInputValue(facility.remitPhone1Input(),phone1);
		setInputValue(facility.remitPhone2Input(),phone2);
		setInputValue(facility.remitFax1Input(),fax1);
		setInputValue(facility.remitEmail1Input(),email1);
		setInputValue(facility.remitCityInput(),city);
		clickOnElement(facility.remitStateDropdpwn());
		selectItem(facility.remitStateDropdpwn(),state);

		defaultRemitToAddress.setFacilityName(facilityName);
		defaultRemitToAddress.setAddress1(address1);
		defaultRemitToAddress.setAddress2(address2);
		defaultRemitToAddress.setCountry(country);
		defaultRemitToAddress.setPostalCode(postalCode);
		defaultRemitToAddress.setPhone1(phone1);
		defaultRemitToAddress.setPhone2(phone2);
		defaultRemitToAddress.setFax1(fax1);
		defaultRemitToAddress.setEmail1(email1);
		defaultRemitToAddress.setCity(city);
		defaultRemitToAddress.setState(state);

		return defaultRemitToAddress;
	}

	private DefaultRemitToAddress getValuesInDefaultRemitToAddress() {
		DefaultRemitToAddress defaultRemitToAddress = new DefaultRemitToAddress();

		defaultRemitToAddress.setFacilityName(facility.remitFacilityNameInput().getAttribute("value"));
		defaultRemitToAddress.setAddress1(facility.remitAddress1Input().getAttribute("value"));
		defaultRemitToAddress.setAddress2(facility.remitAddress2Input().getAttribute("value"));
		defaultRemitToAddress.setCountry(facility.remitCountryTxt().getText());
		defaultRemitToAddress.setPostalCode(facility.remitPostalCodeInput().getAttribute("value"));
		defaultRemitToAddress.setPhone1(facility.remitPhone1Input().getAttribute("value").replaceAll("[^0-9]", "").trim());
		defaultRemitToAddress.setPhone2(facility.remitPhone2Input().getAttribute("value").replaceAll("[^0-9]", "").trim());
		defaultRemitToAddress.setFax1(facility.remitFax1Input().getAttribute("value").replaceAll("[^0-9]", "").trim());
		defaultRemitToAddress.setState(facility.remitStateTxt().getText());
		defaultRemitToAddress.setCity(facility.remitCityInput().getAttribute("value"));
		defaultRemitToAddress.setEmail1(facility.remitEmail1Input().getAttribute("value"));

		return defaultRemitToAddress;
	}

	private void verifyHeaderDataSaved(Header expectedHeader, Fac fac) {
		assertEquals(fac.getName(), expectedHeader.getName());
		assertEquals(fac.getFacUrl(), expectedHeader.getWebsite());
		assertEquals(fac.isAffil, expectedHeader.isWhollyOwnedSubsidiary());
		assertEquals(fac.isUseClnPrimaryFacBilling, expectedHeader.getIsUseClnPrimaryFacBilling());
	}

	private void verifyFacilityAdditionalInformationDataSaved(FacilityAdditionalInformation expectedFacilityAdditionalInformation, String facType, String facAbbrev) throws Exception{
		//License Information
		if (!facType.equalsIgnoreCase("Draw Station")){
			FacLic facLic = facilityDao.getFacLicByFacAbbrevAndDescr( facAbbrev, "CLIA");
			assertEquals(expectedFacilityAdditionalInformation.getClia(), facLic.getLicId());
			facLic = facilityDao.getFacLicByFacAbbrevAndDescr( facAbbrev, "Mammo Cert");
			assertEquals(expectedFacilityAdditionalInformation.getMammoCert(), facLic.getLicId());
		}

		FacLic facLic = facilityDao.getFacLicByFacAbbrevAndDescr( facAbbrev, "Tax ID");
		assertEquals(expectedFacilityAdditionalInformation.getTaxId(), facLic.getLicId());
		facLic = facilityDao.getFacLicByFacAbbrevAndDescr( facAbbrev, "Organizational NPI");
		assertEquals(expectedFacilityAdditionalInformation.getOrganizationalNpi(), facLic.getLicId());
		facLic = facilityDao.getFacLicByFacAbbrevAndDescr( facAbbrev, "Facility NPI");
		assertEquals(expectedFacilityAdditionalInformation.getFacilityNpi(), facLic.getLicId());
		facLic = facilityDao.getFacLicByFacAbbrevAndDescr( facAbbrev, "Taxonomy Code");
		assertEquals(expectedFacilityAdditionalInformation.getTaxonomyCode(), facLic.getLicId());

		//Facility Group
		Set<String> facGrpSet = expectedFacilityAdditionalInformation.getFacilityGroup();
		List<String> expectedfacGrpList = new ArrayList<>(facGrpSet);
		List<String> actualFacGrpList = daoManagerPlatform.getFacGrpDescrByFacAbbrev(facAbbrev, testDb);
		ListUtil listUtil = new ListUtil();
		assertTrue(listUtil.compareLists(expectedfacGrpList, actualFacGrpList), "       The Facility Group for Facility ID " + facAbbrev + " is not matching.");
	}

	private void verifyFacilityAddressAndContactInformationDataSaved(FacilityAddressAndContactInformation expectedFacilityAddressAndContactInformation, Fac fac) {
		assertEquals(fac.getContct1(), expectedFacilityAddressAndContactInformation.getContact1());
		assertEquals(fac.getAddr1(), expectedFacilityAddressAndContactInformation.getAddress1());
		assertEquals(fac.getAddr2(), expectedFacilityAddressAndContactInformation.getAddress2());
		assertEquals(fac.getZipId(), expectedFacilityAddressAndContactInformation.getPostalCode().replaceAll("-","").trim());
		assertEquals(fac.getContct1Phn(), expectedFacilityAddressAndContactInformation.getPhone1());
		assertEquals(fac.getContct1Fax(), expectedFacilityAddressAndContactInformation.getFax1());
		assertEquals(fac.getContct2(), expectedFacilityAddressAndContactInformation.getContact2());
		assertEquals(fac.getContct2Fax(), expectedFacilityAddressAndContactInformation.getFax2());
		assertEquals(fac.getContct1Email(), expectedFacilityAddressAndContactInformation.getEmail1());
		assertEquals(fac.getContct2Email(), expectedFacilityAddressAndContactInformation.getEmail2());
	}

	private void verifyCorrespondenceAddressDataSaved(CorrespondenceAddress expectedCorrespondenceAddress, Fac fac) {
		assertEquals(fac.getCorrespContct(), expectedCorrespondenceAddress.getFacilityName());
		assertEquals(fac.getCorrespAddr1(), expectedCorrespondenceAddress.getAddress1());
		assertEquals(fac.getCorrespAddr2(), expectedCorrespondenceAddress.getAddress2());
		assertEquals(fac.getCorrespZip(), expectedCorrespondenceAddress.getPostalCode().replaceAll("-","").trim());
		assertEquals(fac.getCorrespPhn(), expectedCorrespondenceAddress.getPhone1());
		assertEquals(fac.getCorrespFax(), expectedCorrespondenceAddress.getFax1());
		assertEquals(fac.getCorrespCity(), expectedCorrespondenceAddress.getCity());
		assertEquals(fac.getCorrespEmail(), expectedCorrespondenceAddress.getEmail1());
	}

	private void verifyDefaultRemitToAddressDataSaved(DefaultRemitToAddress expectedDefaultRemitToAddress, Fac fac) {
		assertEquals(fac.getRemitName(), expectedDefaultRemitToAddress.getFacilityName());
		assertEquals(fac.getRemitAddr1(), expectedDefaultRemitToAddress.getAddress1());
		assertEquals(fac.getRemitAddr2(), expectedDefaultRemitToAddress.getAddress2());
		assertEquals(fac.getRemitZipId(), expectedDefaultRemitToAddress.getPostalCode().replaceAll("-","").trim());
		assertEquals(fac.getRemitPhn1(), expectedDefaultRemitToAddress.getPhone1());
		assertEquals(fac.getRemitPhn2(), expectedDefaultRemitToAddress.getPhone2());
		assertEquals(fac.getRemitFax(), expectedDefaultRemitToAddress.getFax1());
		assertEquals(fac.getRemitCity(), expectedDefaultRemitToAddress.getCity());
		assertEquals(fac.getRemitEmail(), expectedDefaultRemitToAddress.getEmail1());
	}

	private FacilityCrossReference setValuesInFacilityCrossReference(String index) throws Exception{
		FacilityCrossReference facilityCrossReference = new FacilityCrossReference();

		TimeStamp timeStamp = new TimeStamp();
		CrossReferenceDescription facXref = crossReferenceDao.getXrefByXrefCatDescr("Facility");
		int crossReferenceId = facXref.getXrefId();
		String crossReferenceDescription = facXref.getDescr();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		setInputValue(facility.sectionSearchInput(),"Facility Cross Reference");
		clickOnElement(facility.facCrossRefTblAddIcon());
		String effectiveDateStr = timeStamp.getCurrentDate("MM/dd/yyyy");
		String expirationDateStr = timeStamp.getFutureDate("MM/dd/yyyy", 100);
		setInputValue(facility.FacilityCrossReferenceEffectiveDateInput(index),effectiveDateStr);
		setInputValue(facility.FacilityCrossReferenceExpirationDateInput(index),expirationDateStr);
		java.sql.Date effectiveDate = new java.sql.Date(df.parse(effectiveDateStr).getTime());
		java.sql.Date expirationDate = new java.sql.Date(df.parse(expirationDateStr).getTime());

		selectItemByVal(facility.crossReferenceDescriptionDropdownInFacilityCrossReference(1), String.valueOf(crossReferenceId));
		facilityCrossReference.setEffectiveDate(effectiveDate);
		facilityCrossReference.setExpirationDate(expirationDate);
		facilityCrossReference.setCrossReferenceDescription(crossReferenceDescription);

		return facilityCrossReference;
	}

	private JurisdictionPayor setValuesInJurisdictionPayor() throws Exception{
		JurisdictionPayor jurisdictionPayor = new JurisdictionPayor();

//		scrollToElement(facility.jurisPyrTblAddIcon());
		clickOnElement(facility.jurisPyrTblAddIcon());
		clickOnElement(facility.jurisPyrTblPyrColSearchIcon("last()"));
		String parent = switchToPopupWin();
		clickOnElement(facility.payorSearchResultTableSearchIcon());
		Pyr pyr = payorDao.getPyrByPyrAbbrv( facility.payorSearchResultTblIdColTxt("2").getText());
		String pyrId = String.valueOf(pyr.getPyrId());
		clickOnElement(facility.payorSearchResultTblIdColTxt("2"));
		switchToParentWin(parent);

		clickOnElement(facility.jurisPyrTblJurisPyrColSearchIcon("last()"));
		parent = switchToPopupWin();
		clickOnElement(facility.payorSearchResultTableSearchIcon());
		pyr = payorDao.getPyrByPyrAbbrv( facility.payorSearchResultTblIdColTxt("3").getText());
		String jurisPyrId = String.valueOf(pyr.getPyrId());
		clickOnElement(facility.payorSearchResultTblIdColTxt("3"));

		jurisdictionPayor.setPayor(pyrId);
		jurisdictionPayor.setJurisdictionPayor(jurisPyrId);
		switchToParentWin(parent);

		return jurisdictionPayor;
	}

	private BillingOverride setValuesInBillingOverride() throws Exception{
		BillingOverride billingOverride = new BillingOverride();

		PyrGrp pyrGrpIncl = payorDao.getPyrGrp();
		String pyrGrpNm = pyrGrpIncl.getGrpNm();

		Npi npi = npiDao.getNpiByNpiTypId(2);
		String npiStr = String.valueOf(npi.getNpi());

		Taxonomy taxonomyList = taxonomyDao.getTaxonomy();
		String taxonomyCode = taxonomyList.getTaxonomyCd();

		String taxId = randomCharacter.getNonZeroRandomNumericString(10);
		String name = randomCharacter.getRandomAlphaString(6);
		String address1 = "ADDR1 " + randomCharacter.getRandomAlphaString(6);

		scrollToElement(facility.billOverrTblAddIcon());
		clickOnElement(facility.billOverrTblAddIcon());
		selectDropDownByText(facility.billOverrTblPyrGrpInclDdl(), pyrGrpNm);
		setInputValue(facility.billingOverrideAddRecordNpiInput(), npiStr);
		setInputValue(facility.billingOverrideAddRecordTaxIdInput(), taxId);
		setInputValue(facility.billingOverrideAddRecordTaxonomyCodeInput(), taxonomyCode);
		setInputValue(facility.billingOverrideAddRecordNameInput(), name);
		setInputValue(facility.billingOverrideAddRecordAddress1Input(), address1);

		clickHiddenPageObject(facility.submitButton(), 0);

		billingOverride.setOverrideId(payorDao.getPyrGrpByPyrGrpId(pyrGrpIncl.getPyrGrpId()).getGrpNm());
		billingOverride.setNpi(npiStr);
		billingOverride.setTaxId(taxId);
		billingOverride.setTaxonomyCode(taxonomyCode);
		billingOverride.setName(name);
		billingOverride.setAddress(address1 + "USA");

		return billingOverride;
	}

	private PayorGroupOverrideRemitAddress setValuesInPayorGroupOverrideRemitAddress() throws Exception{
		PayorGroupOverrideRemitAddress payorGroupOverrideRemitAddress = new PayorGroupOverrideRemitAddress();
		PyrGrp pyrGrpIncl = payorDao.getPyrGrpNonPatient();
		String name = randomCharacter.getRandomAlphaNumericString(5);
		String address1="FACADDR1"+randomCharacter.getNonZeroRandomNumericString(6);
		String stateId= "CA";
		List<Zip> zipList=zipDao.getZipByState(stateId);
		String postalCode=zipList.get(0).zipId;
		String city=zipList.get(0).ctyNm;
		String state = stateDao.getStateByStateId(stateId).get(0).name;
		String countryNm="USA";
		Country country = countryDao.getCountryByName(countryNm);
		String email="qatester2@xifin.com";

		scrollToElement(facility.pyrGrpOverRemitAddrTblSAddIcon());
		clickOnElement(facility.pyrGrpOverRemitAddrTblSAddIcon());

		facility.selectDropDownByText(facility.pyrGrpOverRemitAddrTblPyrGrpColDropdownList("last()"), pyrGrpIncl.getGrpNm());
		clickOnElement(facility.pyrGrpOverRemitAddrTblNameColInput("last()"));
		setInputValue(facility.pyrGrpOverRemitAddrTblNameColInput("last()"), name);
		clickOnElement(facility.pyrGrpOverRemitAddrTblAddr1ColInput("last()"));
		setInputValue(facility.pyrGrpOverRemitAddrTblAddr1ColInput("last()"), address1);
		clickOnElement(facility.pyrGrpOverRemitAddrTblCityColInput("last()"));
		setInputValue(facility.pyrGrpOverRemitAddrTblCityColInput("last()"), city);
		clickOnElement(facility.pyrGrpOverRemitAddrTblEmailColInput("last()"));
		setInputValue(facility.pyrGrpOverRemitAddrTblEmailColInput("last()"), email);
		selectDropDown(facility.pyrGrpOverRemitAddrTblStateColDropdownList("last()"), state);
		scrollToElement(facility.pyrGrpOverRemitAddrTblCountryColDropdownList("last()"));
		selectDropDown(facility.pyrGrpOverRemitAddrTblCountryColDropdownList("last()"), countryNm);
		setInputValue(facility.pyrGrpOverRemitAddrTblPostalCdColInput("last()"), postalCode);

		payorGroupOverrideRemitAddress.setPayorGroup(String.valueOf(pyrGrpIncl.getPyrGrpId()));
		payorGroupOverrideRemitAddress.setName(name);
		payorGroupOverrideRemitAddress.setAddress1(address1);
		payorGroupOverrideRemitAddress.setPostalCode(postalCode);
		payorGroupOverrideRemitAddress.setCity(city);
		payorGroupOverrideRemitAddress.setState(stateId);
		payorGroupOverrideRemitAddress.setCountry(String.valueOf(country.getCntryId()));
		payorGroupOverrideRemitAddress.setEmail(email);

		return payorGroupOverrideRemitAddress;
	}

	private PayorOverrideRemitAddress setValuesInPayorOverrideRemitAddress() throws Exception{
		PayorOverrideRemitAddress payorOverrideRemitAddress = new PayorOverrideRemitAddress();
		String name = randomCharacter.getRandomAlphaNumericString(5);
		String address1="FACADDR1"+randomCharacter.getNonZeroRandomNumericString(6);
		String stateId="CA";
		List<Zip> zipList=zipDao.getZipByState(stateId);
		String postalCode=zipList.get(0).getZipId()+"-3081";
		String city = "SAN DIEGO";
		String state=stateDao.getStateByStateId(stateId).get(0).name;
		String countryNm = "USA";
		Country country = countryDao.getCountryByName( countryNm);
		String email = "qatester2@xifin.com";

		scrollToElement(facility.pyrOverRemitAddrTblAddIcon());
		clickOnElement(facility.pyrOverRemitAddrTblAddIcon());
		clickOnElement(facility.pyrOverRemitAddrTblPyrColSearchIcon("last()"));
		String parent = switchToPopupWin();
		clickOnElement(facility.payorSearchResultTableSearchIcon());
		Pyr pyr = payorDao.getPyrByPyrAbbrv( facility.payorSearchResultTblIdColTxt("2").getText());
		String pyrId = String.valueOf(pyr.getPyrId());
		clickOnElement(facility.payorSearchResultTblIdColTxt("2"));
		switchToParentWin(parent);
		setInputValue(facility.pyrOverRemitAddrTblNameColInput("last()"), name);
		setInputValue(facility.pyrOverRemitAddrTblAddr1ColInput("last()"), address1);
		setInputValue(facility.pyrOverRemitAddrTblCityColInput("last()"), city);
		clickOnElement(facility.pyrOverRemitAddrTblEmailColInput("last()"));
		setInputValue(facility.pyrOverRemitAddrTblEmailColInput("last()"), email);
		scrollToElement(facility.pyrOverRemitAddrTblStateColDropdownList("last()"));
		selectDropDown(facility.pyrOverRemitAddrTblStateColDropdownList("last()"), state);
		setInputValue(facility.pyrOverRemitAddrTblPostalCdColInput("last()"), postalCode);

		payorOverrideRemitAddress.setPayor(pyrId);
		payorOverrideRemitAddress.setName(name);
		payorOverrideRemitAddress.setAddress1(address1);
		payorOverrideRemitAddress.setPostalCode(postalCode);
		payorOverrideRemitAddress.setCity(city);
		payorOverrideRemitAddress.setState(stateId);
		payorOverrideRemitAddress.setCountry(String.valueOf(country.getCntryId()));
		payorOverrideRemitAddress.setEmail(email);

		return payorOverrideRemitAddress;
	}

	private void verifyFacilityCrossReferenceDataSaved(FacilityCrossReference expectedFacilityCrossReference, FacXref facXref) throws XifinDataNotFoundException, XifinDataAccessException {
		assertEquals(expectedFacilityCrossReference.getEffectiveDate(), facXref.getEffDt());
		assertEquals(expectedFacilityCrossReference.getExpirationDate(), facXref.getExpDt());
		//assertEquals(expectedFacilityCrossReference.getCrossReferenceDescription(), String.valueOf(facXref.getXrefId()));
		assertEquals(crossReferenceDao.getXrefByDescr( expectedFacilityCrossReference.getCrossReferenceDescription().split("-")[1]).getXrefId(), facXref.getXrefId());
	}

	private void verifyJurisdictionPayorDataSaved(JurisdictionPayor expectedJurisdictionPayor, PyrFacJurisPyr pyrFacJurisPyr) {
		assertEquals(String.valueOf(pyrFacJurisPyr.getPyrId()), expectedJurisdictionPayor.getPayor());
		assertEquals(String.valueOf(pyrFacJurisPyr.getJurisdictionPyrId()), expectedJurisdictionPayor.getJurisdictionPayor());
	}

	private void verifyBillingOverrideDataSaved(BillingOverride expectedBillingOverride, FacBillToOverrideLnk facBillToOverrideLnk, FacBillToOverride facBillToOverride) throws XifinDataNotFoundException, XifinDataAccessException {
		assertEquals(expectedBillingOverride.getOverrideId(), payorDao.getPyrGrpByPyrGrpId(Integer.valueOf(facBillToOverrideLnk.getOverrideId())).getGrpNm());
		assertEquals(expectedBillingOverride.getNpi(), String.valueOf(facBillToOverride.getNpiId()));
		assertEquals(expectedBillingOverride.getTaxId(), String.valueOf(facBillToOverride.getTaxId()));
		assertEquals(expectedBillingOverride.getTaxonomyCode(), String.valueOf(facBillToOverride.getTaxonomyCd()));
		assertEquals(expectedBillingOverride.getName(), String.valueOf(facBillToOverride.getFacilityBillingName()));
		assertEquals(expectedBillingOverride.getAddress(), (facBillToOverride.getAddr1()+"USA"));
	}

	private void verifyPayorGroupOverrideRemitAddressDataSaved(PayorGroupOverrideRemitAddress expectedPayorGroupOverrideRemitAddress, FacPyrGrpRemit facPyrGrpRemit) {
		assertEquals(expectedPayorGroupOverrideRemitAddress.getPayorGroup(), String.valueOf(facPyrGrpRemit.getPyrGrpId()));
		assertEquals(expectedPayorGroupOverrideRemitAddress.getName(), facPyrGrpRemit.getRemitName());
		assertEquals(expectedPayorGroupOverrideRemitAddress.getAddress1(), facPyrGrpRemit.getRemitAddr1());
		assertEquals(expectedPayorGroupOverrideRemitAddress.getPostalCode().replaceAll("-","").trim(), String.valueOf(facPyrGrpRemit.getRemitZipId()));
		assertEquals(expectedPayorGroupOverrideRemitAddress.getCity(), facPyrGrpRemit.getRemitCity());
		assertEquals(expectedPayorGroupOverrideRemitAddress.getState(), facPyrGrpRemit.getRemitStId());
		assertEquals(expectedPayorGroupOverrideRemitAddress.getEmail(), facPyrGrpRemit.getRemitEmail());
		assertEquals(expectedPayorGroupOverrideRemitAddress.getCountry(), String.valueOf(facPyrGrpRemit.getRemitCntryId()));
	}

	private void verifyPayorOverrideRemitAddressDataSaved(PayorOverrideRemitAddress expectedPayorOverrideRemitAddress, FacPyrRemit facPyrRemit) {
		assertEquals(expectedPayorOverrideRemitAddress.getPayor(), String.valueOf(facPyrRemit.getPyrId()));
		assertEquals(expectedPayorOverrideRemitAddress.getName(), facPyrRemit.getRemitName());
		assertEquals(expectedPayorOverrideRemitAddress.getAddress1(), facPyrRemit.getRemitAddr1());
		expectedPayorOverrideRemitAddress.setPostalCode(expectedPayorOverrideRemitAddress.getPostalCode().replaceAll("-","").trim());
		assertEquals(expectedPayorOverrideRemitAddress.getPostalCode().replaceAll("-","").trim(), String.valueOf(facPyrRemit.getRemitZipId()));
		assertEquals(expectedPayorOverrideRemitAddress.getCity(), facPyrRemit.getRemitCity());
		assertEquals(expectedPayorOverrideRemitAddress.getState(), facPyrRemit.getRemitStId());
		assertEquals(expectedPayorOverrideRemitAddress.getEmail(), facPyrRemit.getRemitEmail());
		assertEquals(expectedPayorOverrideRemitAddress.getCountry(), String.valueOf(facPyrRemit.getRemitCntryId()));
	}

	private FacilityCrossReference getValuesInFacilityCrossReference(String index) throws Exception{
		FacilityCrossReference facilityCrossReference = new FacilityCrossReference();

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		scrollToElement(facility.effDateTitleOnFacilityCrossReferenceSection());

		java.sql.Date effectiveDate = new java.sql.Date(df.parse(facility.effDateTitleOnFacilityCrossReferenceSection().getText()).getTime());
		Date expirationDate = new java.sql.Date(df.parse(facility.expDateTitleOnFacilityCrossReferenceSection().getText()).getTime());

		facilityCrossReference.setEffectiveDate(effectiveDate);
		facilityCrossReference.setExpirationDate(expirationDate);
		facilityCrossReference.setCrossReferenceDescription(facility.facilityCrossReferenceDescription(index).getText().trim());

		return facilityCrossReference;
	}

	private JurisdictionPayor getValuesInJurisdictionPayor() throws XifinDataNotFoundException, XifinDataAccessException {
		JurisdictionPayor jurisdictionPayor = new JurisdictionPayor();

		Pyr pyr = payorDao.getPyrByPyrAbbrv( facility.jurisPyrTblPyrColTxt("last()").getText());
		Pyr jusrisPyr = payorDao.getPyrByPyrAbbrv( facility.jurisPyrTblJurisPyrColTxt("last()").getText());
		jurisdictionPayor.setPayor(String.valueOf(pyr.getPyrId()));
		jurisdictionPayor.setJurisdictionPayor(String.valueOf(jusrisPyr.getPyrId()));

		return jurisdictionPayor;
	}

	private BillingOverride getValuesInBillingOverride(int index) throws Exception{
		BillingOverride billingOverride = new BillingOverride();

		scrollToElement(facility.billOverrTblAddIcon());
		String orgPyrGrp = facility.billOverrTblOverrIdCol("last()").getText();
		String overrideId = orgPyrGrp.substring(18, orgPyrGrp.length()).trim();

		billingOverride.setOverrideId(overrideId);
		billingOverride.setNpi(facility.billOverrTblNpiCol(index).getText());
		billingOverride.setTaxId(facility.billOverrTblTaxIdCol(index).getText());
		billingOverride.setTaxonomyCode(facility.billOverrTblTaxonomyCdCol(index).getText().split("-")[0]);
		billingOverride.setName(facility.billOverrTblNameCol(index).getText());
		billingOverride.setAddress(facility.billOverrTblAddressCol(index).getAttribute("title"));

		return billingOverride;
	}

	private PayorGroupOverrideRemitAddress getValuesInPayorGroupOverrideRemitAddress() throws Exception{
		PayorGroupOverrideRemitAddress payorGroupOverrideRemitAddress = new PayorGroupOverrideRemitAddress();

		scrollToElement(facility.pyrGrpOverRemitAddrTblSAddIcon());
		PyrGrp pyrGrpId = payorDao.getPyrGrpByPyrGrpNm( facility.pyrGrpOverRemitAddrTblPyrGrpColText("last()").getText());
		String countryNm = facility.pyrGrpOverRemitAddrTblCountryColText("last()").getText();
		Country country = countryDao.getCountryByName( countryNm);
		String stateNm = facility.pyrGrpOverRemitAddrTblStateColText("last()").getText();
		State state = stateDao.getStateByName(stateNm);

		payorGroupOverrideRemitAddress.setPayorGroup(String.valueOf(pyrGrpId.getPyrGrpId()));
		payorGroupOverrideRemitAddress.setName(facility.pyrGrpOverRemitAddrTblNameColText("last()").getText());
		payorGroupOverrideRemitAddress.setAddress1(facility.pyrGrpOverRemitAddrTblAddr1ColText("last()").getText());
		payorGroupOverrideRemitAddress.setPostalCode(facility.pyrGrpOverRemitAddrTblPostalCdColText("last()").getText());
		payorGroupOverrideRemitAddress.setCity(facility.pyrGrpOverRemitAddrTblCityColText("last()").getText());
		payorGroupOverrideRemitAddress.setState(state.stId);
		payorGroupOverrideRemitAddress.setCountry(String.valueOf(country.getCntryId()));
		payorGroupOverrideRemitAddress.setEmail(facility.pyrGrpOverRemitAddrTblEmailColText("last()").getText());

		return payorGroupOverrideRemitAddress;
	}

	private PayorOverrideRemitAddress getValuesInPayorOverrideRemitAddress() throws Exception {
		PayorOverrideRemitAddress payorOverrideRemitAddress = new PayorOverrideRemitAddress();

		scrollToElement(facility.pyrGrpOverRemitAddrTblSAddIcon());
		String countryNm = facility.pyrOverRemitAddrTblCountryColText("last()").getText();
		Country country = countryDao.getCountryByName( countryNm);
		String stateNm = facility.pyrOverRemitAddrTblStateColText("last()").getText();
		State state = stateDao.getStateByName( stateNm);
		Pyr pyr = payorDao.getPyrByPyrAbbrv( facility.pyrOverRemitAddrTblPyrColText("last()").getText());

		payorOverrideRemitAddress.setPayor(String.valueOf(pyr.getPyrId()));
		payorOverrideRemitAddress.setName(facility.pyrOverRemitAddrTblNameColText("last()").getText());
		payorOverrideRemitAddress.setAddress1(facility.pyrOverRemitAddrTblAddr1ColText("last()").getText());
		payorOverrideRemitAddress.setPostalCode(facility.pyrOverRemitAddrTblPostalCdColText("last()").getText());
		payorOverrideRemitAddress.setCity(facility.pyrOverRemitAddrTblCityColText("last()").getText());
		payorOverrideRemitAddress.setState(state.stId);
		payorOverrideRemitAddress.setCountry(String.valueOf(country.getCntryId()));
		payorOverrideRemitAddress.setEmail(facility.pyrOverRemitAddrTblEmailColText("last()").getText());

		return payorOverrideRemitAddress;
	}

	private void scrollToSection(String strSearch) throws InterruptedException {
		facility.sectionSearchInput().clear();
		facility.sectionSearchInput().sendKeys(strSearch);
		Thread.sleep(1500);
	}

	private String getNPIEntity() {
		Random random = new Random();

		//List<String> npiEntityList = Arrays.asList("Client","Facility","Physician");
		List<String> npiEntityList = Arrays.asList("Client", "Physician");
		int index = random.nextInt(npiEntityList.size());

		return npiEntityList.get(index);
	}

	private void verifyNPISearchFieldsDisplayed(String npiEntity) throws Exception {
		switch(npiEntity)
		{
			case "Client":
				assertTrue(isElementPresent(npiSearch.clientNameInput(),5),"        Client Name input field should be displayed when Client NPI Entity is selected.");
				break;
			case "Facility":
				assertTrue(isElementPresent(npiSearch.facilityInput(), 5),"        Facility input field should be displayed when Facility NPI Entity is selected.");
				break;
			case "Physician":
				assertTrue(isElementPresent(npiSearch.firstNameInput(), 5),"        First Name input field should be displayed when Physician NPI Entity is selected.");
				assertTrue(isElementPresent(npiSearch.lastNameInput(), 5),"        Last Name input field should be displayed when Physician NPI Entity is selected.");
				break;
		}
	}

	private void verifyNPISearchResults(String npiEntity) throws Exception {
		wait.until(ExpectedConditions.visibilityOf(npiSearchResults.npiSearchResultTitle()));

		switch(npiEntity)
		{
			case "Client":
				logger.info("*** Expected Results: - Verify that the total search result records in DB and UI are matching");
				int totalRecordInUI = Integer.parseInt(npiSearchResults.npiSearchResultsTblViewResultsLable().getText().trim().split(" ")[5].replaceAll(",", ""));
				int totalRecordInDB = npiDao.getNpiSearchFromNpiByNpiEntity("Client").size();
				assertEquals(totalRecordInDB, totalRecordInUI, "       Total Record show in the NPI Search Results should be " + totalRecordInDB);

				logger.info("*** Expected Results: - Verify that the top 20 search result records in DB and UI are matching");
				List<EntityNpiSearchResult> clientNpiSearchResultTop20ListInDB = rpmDao.getClientNpiSearchResultTop20(testDb);
				List<EntityNpiSearchResult> clientNpiSearchResultTop20ListInUI = getNpiSearchResultTop20List();
				assertEquals(clientNpiSearchResultTop20ListInDB, clientNpiSearchResultTop20ListInUI);

				break;
			case "Facility":
				logger.info("*** Expected Results: - Verify that the total search result records in DB and UI are matching");
				totalRecordInUI = Integer.parseInt(npiSearchResults.npiSearchResultsTblViewResultsLable().getText().trim().split(" ")[5].replaceAll(",", ""));
				totalRecordInDB = npiDao.getNpiSearchFromNpiByNpiEntity("Facility").size();
				assertEquals(totalRecordInDB, totalRecordInUI, "       Total Record show in the NPI Search Results should be " + totalRecordInDB);

				logger.info("*** Expected Results: - Verify that the top 20 search result records in DB and UI are matching");
				List<EntityNpiSearchResult> facilityNpiSearchResultTop20ListInDB = rpmDao.getFacilityNpiSearchResultTop20(testDb);
				List<EntityNpiSearchResult> facilityNpiSearchResultTop20ListInUI = getNpiSearchResultTop20List();
				assertEquals(facilityNpiSearchResultTop20ListInDB, facilityNpiSearchResultTop20ListInUI);

				break;
			case "Physician":
				logger.info("*** Expected Results: - Verify that the total search result records in DB and UI are matching");
				totalRecordInUI = Integer.parseInt(npiSearchResults.npiSearchResultsTblViewResultsLable().getText().trim().split(" ")[5].replaceAll(",", ""));
				totalRecordInDB = npiDao.getNpiSearchFromNpiByNpiEntity("Physician").size();
				assertEquals(totalRecordInDB, totalRecordInUI, "       Total Record show in the NPI Search Results should be " + totalRecordInDB);

				logger.info("*** Expected Results: - Verify that the top 20 search result records in DB and UI are matching");
				List<EntityNpiSearchResult> physicianNpiSearchResultTop20ListInDB = rpmDao.getPhysicianNpiSearchResultTop20(testDb);
				List<EntityNpiSearchResult> physicianNpiSearchResultTop20ListInUI = getNpiSearchResultTop20List();
				assertEquals(physicianNpiSearchResultTop20ListInDB, physicianNpiSearchResultTop20ListInUI);

				break;
		}
	}

	private List<EntityNpiSearchResult> getNpiSearchResultTop20List() {
		List<EntityNpiSearchResult> entityNpiSearchResults  = new ArrayList<>();
		NpiSearchResults npiSearchResults = new NpiSearchResults(driver);

		for (int i = 1; i <= 20; i++){
			EntityNpiSearchResult entityNpiSearchResult = new EntityNpiSearchResult();
			//NPI
			entityNpiSearchResult.setNpi_id(Integer.valueOf(npiSearchResults.getLinkValueFromWebTable("npisearchTable", i+1, 2).getText().trim()));
			//Name
			entityNpiSearchResult.setName(npiSearchResults.getValueFromWebTable("npisearchTable", i+1, 4).getText().trim());
			//NPI Type
			entityNpiSearchResult.setNpi_type(npiSearchResults.getValueFromWebTable("npisearchTable", i+1, 3).getText().trim());
			entityNpiSearchResults.add(entityNpiSearchResult);
		}

		return entityNpiSearchResults;
	}

	private String getNpiType() {
		Random random = new Random();

		List<String> npiTypeList = Arrays.asList("Individual","Organization");
		int index = random.nextInt(npiTypeList.size());

		return npiTypeList.get(index);
	}

	private void verifyNPIGlobalSearchProviderInfoDisplayed(String npiEntity) throws Exception {
		switch(npiEntity)
		{
			case "Individual":
				assertTrue(isElementPresent(npiGlobalSearch.firstNameInput(),5),"        First Name input field should show when selected Individual NPI Type.");
				assertTrue(isElementPresent(npiGlobalSearch.lastNameInput(),5),"        Last Name input field should show when selected Individual NPI Type.");
				break;
			case "Organization":
				assertTrue(isElementPresent(npiGlobalSearch.organizationNameInput(), 5),"        Organization Name input field should be displayed when Organization NPI Entity is selected.");
				break;
		}
	}

	private void verifyGlobalNPISearchResults(String npiType) throws Exception {
		wait.until(ExpectedConditions.visibilityOf(npiGlobalSearchResults.npiSearchResultTitle()));

		switch(npiType)
		{
			case "Individual":
				List<GlobalNpiSearchResult> globalNpiSearchResultList = npiDao.getGlobalNpiSearchResultByNpiType( npiType.toUpperCase());
				logger.info("*** Expected Results: - Verify that the search result records are displayed properly");
				List<GlobalNpiSearchResult> npiGlobalSearchResultTop1ListInDB = new ArrayList<>();
				npiGlobalSearchResultTop1ListInDB.add(globalNpiSearchResultList.get(0));
				List<GlobalNpiSearchResult> npiGlobalSearchResultTop1ListInUI = getNpiGlobalSearchResultTop1List(npiType);
				assertEquals(npiGlobalSearchResultTop1ListInDB, npiGlobalSearchResultTop1ListInUI);

				break;
			case "Organization":
				globalNpiSearchResultList = npiDao.getGlobalNpiSearchResultByNpiType( npiType.toUpperCase());
				logger.info("*** Expected Results: - Verify that the search result records are displayed properly");
				npiGlobalSearchResultTop1ListInDB = new ArrayList<>();
				npiGlobalSearchResultTop1ListInDB.add(globalNpiSearchResultList.get(0));
				npiGlobalSearchResultTop1ListInUI = getNpiGlobalSearchResultTop1List(npiType);
				assertEquals(npiGlobalSearchResultTop1ListInDB, npiGlobalSearchResultTop1ListInUI);

				break;
		}
	}

	private List<GlobalNpiSearchResult> getNpiGlobalSearchResultTop1List(String npiType) {
		List<GlobalNpiSearchResult> globalNpiSearchResults  = new ArrayList<>();
		NpiGlobalSearchResults npiGlobalSearchResults = new NpiGlobalSearchResults(driver);

		switch(npiType)
		{
			case "Individual":
				GlobalNpiSearchResult globalNpiSearchResult = new GlobalNpiSearchResult();
				//RowNum
				globalNpiSearchResult.setRowNum(Integer.valueOf(npiGlobalSearchResults.getValueFromWebTable("npisearchTable", 2, 1).getText().trim()));
				//EntityTypeCd
				globalNpiSearchResult.setEntityTypeCd(1);
				//NPI
				globalNpiSearchResult.setNpi(Integer.valueOf(npiGlobalSearchResults.getLinkValueFromWebTable("npisearchTable", 2, 2).getText().trim()));
				//NPI Type
				globalNpiSearchResult.setAbbrev(npiGlobalSearchResults.getValueFromWebTable("npisearchTable", 2, 3).getText().trim());
				//Provider Last Name
				globalNpiSearchResult.setProvdrLastNm(npiGlobalSearchResults.getValueFromWebTable("npisearchTable", 2, 5).getText().trim().split(" ")[0].replaceAll(",", "").trim());
				//Provider First Name
				globalNpiSearchResult.setProvdr1StNm(npiGlobalSearchResults.getValueFromWebTable("npisearchTable", 2, 5).getText().trim().split(" ")[1].trim());
				globalNpiSearchResults.add(globalNpiSearchResult);

				break;
			case "Organization":
				globalNpiSearchResult = new GlobalNpiSearchResult();
				//RowNum
				globalNpiSearchResult.setRowNum(Integer.valueOf(npiGlobalSearchResults.getValueFromWebTable("npisearchTable", 2, 1).getText().trim()));
				//EntityTypeCd
				globalNpiSearchResult.setEntityTypeCd(2);
				//NPI
				globalNpiSearchResult.setNpi(Integer.valueOf(npiGlobalSearchResults.getLinkValueFromWebTable("npisearchTable", 2, 2).getText().trim()));
				//NPI Type
				globalNpiSearchResult.setAbbrev(npiGlobalSearchResults.getValueFromWebTable("npisearchTable", 2, 3).getText().trim());
				//Provider Organization Name
				globalNpiSearchResult.setProvdrOrgNm(npiGlobalSearchResults.getValueFromWebTable("npisearchTable", 2, 4).getText().trim());
				globalNpiSearchResults.add(globalNpiSearchResult);

				break;
		}

		return globalNpiSearchResults;
	}

	private void cleanUpFacility(List<String> facAbbrevList) throws XifinDataAccessException {
		for (String facAbbrev : facAbbrevList) {
		    logger.info("cleaning up");
			facilityDao.deleteFacSvcLnkByFacAbbrev(facAbbrev);
			facilityDao.deleteFacFacGrpByFacAbbrev(facAbbrev);
			facilityDao.deleteFacLicByFacAbbrev(facAbbrev);
			facilityDao.deleteFacXrefByFacAbbrev(facAbbrev);
			List<TestDt> testDt = testDao.getTestDtByDefaultFac(facAbbrev);
			for(TestDt testId : testDt){
				testDao.deleteTestProc(testId.getTestId());
			}
			testDao.deleteTestFacByFacAbbrev(facAbbrev);
			testDao.deleteTestDtByDefaultFac(facAbbrev);
			facilityDao.deleteFacBillToOverrideLnkByFacAbbrev(facAbbrev);
			facilityDao.deleteFacBillToOverrideByFacAbbrev(facAbbrev);
			facilityDao.deleteFacPyrGrpRemitByFacAbbrev(facAbbrev);
			facilityDao.deleteFacPyrRemitByFacAbbrev(facAbbrev);
			payorDao.deletePyrFacJurisPyrByFacAbbrev(facAbbrev);
			facilityDao.deleteFacByFacAbbrev(facAbbrev);
		}
	}
}
