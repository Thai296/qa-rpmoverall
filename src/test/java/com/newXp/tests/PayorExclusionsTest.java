package com.newXp.tests;


import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrClnExcl.PyrClnExcl;
import com.mbasys.mars.ejb.entity.pyrGrp.PyrGrp;
import com.mbasys.mars.ejb.entity.pyrGrpClnExcl.PyrGrpClnExcl;
import com.overall.client.clientProcessing.PayorExclusions;
import com.overall.menu.MenuNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TimeStamp;
import domain.client.payorExclusions.Header;
import domain.client.payorExclusions.PayorExclusionTable;
import domain.client.payorExclusions.PayorGroupExclusionTable;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


public class PayorExclusionsTest extends SeleniumBaseTest {
	private TimeStamp timeStamp;
	private PayorExclusions payorExclusions;

	private static final String EMPTY = "";
	private static final String ADDNEW = "Add Data";
	private static final String UPDATE = "Update Data";
	private static final String DELETED = "Delete Data";
	private static final String PAYOR_EXCLUSIONS_TITLE = "Payor Exclusions";



	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String ssoUsername, String ssoPassword, Method method) {
		try {
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			MenuNavigation navigation = new MenuNavigation(driver, config);
			logIntoSso(ssoUsername, ssoPassword);
			navigation.navigateToPayorExclusionsPage();

		} catch (Exception e) {
			logger.error("Error running BeforeMethod", e);
		}
	}

	@BeforeSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "disableBrowserPlugins", "clientId1"})
	public void BeforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, @Optional String disableBrowserPlugins, String clientId1)
	{
		try
		{
			logger.info("Running BeforeSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			cleanUpTestData(Arrays.asList(clientId1));
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


	@Test(priority = 1, description = "Add new and delete Payor Exclusion and Payor Group Exclusion")
	@Parameters({"ssoUsername", "ssoPassword", "clientId1"})
	public void testXPR_1619(String ssoUsername, String ssoPassword, String clientId1) throws Exception {
		logger.info("===== Testing - testXPR_1619 =====");
		TimeStamp timeStamp = new TimeStamp();
		PayorExclusions payorExclusions = new PayorExclusions(driver);

		logger.info("*** Expected Results: - User login successfully. Load Payor Exclusions page is displayed.");
		verifyPayorExclusionsLoadPageIsDisplayed();

		logger.info("*** Action: - Load an existing Client ID which has no Payor Group and Payor Exclusions, tab out.");
		setInputValue(payorExclusions.payorExclusionsLoadClientIdInput(), clientId1);

		Header expectedHeader = setValueInHeader(clientId1);

		logger.info("*** Expected Results: - Verify that Payor Exclusions page is loaded");
		Header actualHeader = getValuesInHeader();
		assertEquals(actualHeader,expectedHeader,"        Payor Exclusions page Header is displayed with correct data.");

		logger.info("*** Action: - Add a new row in Payor Exclusion table");
		PayorExclusionTable expectedPayorExclusionTable = setValuesInPayorExclusionTable();

		logger.info("*** Action: - Add a new row in Payor Group Exclusion table");
		PayorGroupExclusionTable expectedPayorGroupExclusionTable = setValuesInPayorGroupExclusionTable();

		logger.info("*** Action: - Click on Save and Clear button");
		assertTrue(isElementPresent(payorExclusions.footerSaveAndClearBtn(), 5), "        The Save and Clear button is displayed.");
		clickOnElement(payorExclusions.footerSaveAndClearBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Client page");
		verifyPayorExclusionsLoadPageIsDisplayed();

		logger.info("*** Expected Results: - Verify that the Data are saved correctly in PYR_CLN_EXCL and PYR_GRP_CLN_EXCL tables in DB");
		verifyPayorExclusionDataSaved(ADDNEW, expectedHeader.getClientAbbrv(), expectedPayorExclusionTable.getPayorId(), expectedPayorExclusionTable);
		verifyPayorGroupExclusionDataSaved(ADDNEW, expectedHeader.getClientAbbrv(), expectedPayorGroupExclusionTable.getGroupName(), expectedPayorGroupExclusionTable);

		logger.info("*** Action: - Load the same Client ID and tab out.");
		setInputValue(payorExclusions.payorExclusionsLoadClientIdInput(), clientId1);

		logger.info("*** Expected Results: - Verify that Payor Exclusions page is loaded");
		assertEquals(actualHeader,expectedHeader,"        Payor Exclusions page Header is displayed with correct data.");

		logger.info("*** Expected Results: - Verify that the data in Payor Exclusion and Payor Group Exclusion are displayed properly in UI");
		PayorExclusionTable actualPayorExclusionTable = getValuesInPayorExclusionTable("last()");
		assertEquals(actualPayorExclusionTable, expectedPayorExclusionTable,"        Payor Exclusion data are displayed correctly.");
		PayorGroupExclusionTable actualPayorGroupExclusionTable = getValuesInPayorGroupExclusionTable("last()");
		assertEquals(actualPayorGroupExclusionTable, expectedPayorGroupExclusionTable,"        Payor Group Exclusion data are displayed correctly.");

		logger.info("*** Action: - Click on Delete checkbox in Payor Exclusion table");
		clickHiddenPageObject(payorExclusions.payorExclusionTblDeletedChk("last()"),0);

		logger.info("*** Action: - Click on Delete checkbox in Payor Group Exclusion table");
		clickHiddenPageObject(payorExclusions.payorGrpExclusionTblDeletedChk("last()"),0);

		logger.info("*** Action: - Click on Save and Clear button");
		assertTrue(isElementPresent(payorExclusions.footerSaveAndClearBtn(), 5), "        The Save and Clear button is displayed.");
		clickOnElement(payorExclusions.footerSaveAndClearBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Client page");
		verifyPayorExclusionsLoadPageIsDisplayed();

		logger.info("*** Expected Results: - Verify that data are saved properly in DB");
		verifyPayorExclusionDataSaved(DELETED, expectedHeader.getClientAbbrv(), expectedPayorExclusionTable.getPayorId(), expectedPayorExclusionTable);
		verifyPayorGroupExclusionDataSaved(DELETED, expectedHeader.getClientAbbrv(), expectedPayorGroupExclusionTable.getGroupName(), expectedPayorGroupExclusionTable);

		logger.info("*** Action: - Load the same Client ID and tab out.");
		setInputValue(payorExclusions.payorExclusionsLoadClientIdInput(), clientId1);

		logger.info("*** Expected Results: - Verify that Payor Exclusions page is loaded");
		assertEquals(actualHeader,expectedHeader,"        Payor Exclusions page Header is displayed with correct data.");

		logger.info("*** Expected Results: - Verify that the records are deleted in Payor Exclusions and Payor Group Exclusions tables");
		verifyPayorExclusionTableIsEmpty(expectedPayorExclusionTable);
		verifyPayorGroupExclusionTableIsEmpty(expectedPayorGroupExclusionTable);

		logger.info("*** Action: - Click on Reset button.");
		clickOnElement(payorExclusions.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Client page");
		verifyPayorExclusionsLoadPageIsDisplayed();

		driver.quit();
	}







	private void cleanUpTestData(List<String> clientIdList) throws XifinDataAccessException {
		for (String clientId: clientIdList) {
			logger.info("       ========== Cleaning up data for Client " + clientId + " ==========");

			rpmDao.deletePyrClnExclByClnAbbrev(testDb, clientId);
			rpmDao.deletePyrGrpClnExclByClnAbbrev(testDb, clientId);
		}
	}

	private Header setValueInHeader(String clientAbbrv) throws Exception {
		Header header = new Header();

		Cln cln = rpmDao.getClnByClnAbbrev(testDb, clientAbbrv);
		header.setClientAbbrv(clientAbbrv);
		header.setName(cln.getBilAcctNm());
		String accountType = rpmDao.getClnAccntTypByAccntTypId(testDb, cln.getAcctTypId()).getDescr().trim();
		header.setAccountType(accountType);

		return header;
	}

	private Header getValuesInHeader(){
		Header header = new Header();
		payorExclusions = new PayorExclusions(driver);

		wait.until(ExpectedConditions.elementToBeClickable(payorExclusions.headerClientIdTxt()));

		header.setClientAbbrv(payorExclusions.headerClientIdTxt().getText());
		header.setName(payorExclusions.headerClientNameTxt().getText());
		header.setAccountType(payorExclusions.headerAccountTypTxt().getText());

		return header;
	}

	private void verifyPayorExclusionsLoadPageIsDisplayed() throws Exception {
		payorExclusions = new PayorExclusions(driver);

		wait.until(ExpectedConditions.elementToBeClickable(payorExclusions.payorExclusionsLoadPgTitleTxt()));
		assertTrue(payorExclusions.payorExclusionsLoadPgTitleTxt().getText().equals(PAYOR_EXCLUSIONS_TITLE), "        Payor Exclusions load page title should be "+PAYOR_EXCLUSIONS_TITLE+".");
		wait.until(ExpectedConditions.elementToBeClickable(payorExclusions.payorExclusionsLoadClientSection()));
		assertFalse(isElementHidden(payorExclusions.payorExclusionsLoadClientSection(), 5),"        Load client section is displayed");
	}

	private PayorExclusionTable setValuesInPayorExclusionTable() throws Exception {
		payorExclusions = new PayorExclusions(driver);
		timeStamp = new TimeStamp(driver);
		PayorExclusionTable payorExclusionTable = new PayorExclusionTable();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String date = timeStamp.getCurrentDate("MM/dd/yyyy");
		Date effDt = new Date(formatter.parse(date).getTime());
		Date expDt = new Date(formatter.parse(date).getTime());
		String pyrAbbrev = daoManagerPlatform.getRandomPYRfromPYR_DTandPYR(testDb).get(2);

		clickOnElement(payorExclusions.payorExclusionTblAddBtn());

		assertTrue(isElementPresent(payorExclusions.payorExclusionTblEffDateInput(), 5), "        Payor Exclusion Table: Effective Date input field is displayed.");
		setInputValue(payorExclusions.payorExclusionTblEffDateInput(), date);
		setInputValue(payorExclusions.payorExclusionTblExpDateInput(), date);
		setInputValue(payorExclusions.payorExclusionTblPayorIdInput(), pyrAbbrev);

		payorExclusionTable.setEffectiveDate(effDt);
		payorExclusionTable.setExpirationDate(expDt);
		payorExclusionTable.setPayorId(pyrAbbrev);
		payorExclusionTable.setPayorName(rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrev).getName().trim());

		return payorExclusionTable;
	}


	private PayorGroupExclusionTable setValuesInPayorGroupExclusionTable() throws Exception {
		payorExclusions = new PayorExclusions(driver);
		timeStamp = new TimeStamp(driver);
		PayorGroupExclusionTable payorGroupExclusionTable = new PayorGroupExclusionTable();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String date = timeStamp.getCurrentDate("MM/dd/yyyy");
		Date effDt = new Date(formatter.parse(date).getTime());
		Date expDt = new Date(formatter.parse(date).getTime());
		PyrGrp pyrGrp = rpmDao.getPyrGrp(testDb);

		clickOnElement(payorExclusions.payorGrpExclusionTblAddBtn());

		assertTrue(isElementPresent(payorExclusions.payorGrpExclusionTblEffDateInput(), 5), "        Payor Group Exclusion Table: Effective Date input field is displayed.");
		setInputValue(payorExclusions.payorGrpExclusionTblEffDateInput(), date);
		setInputValue(payorExclusions.payorGrpExclusionTblExpDateInput(), date);
		selectDropDownByText(payorExclusions.payorGrpExclusionTblGroupNameDdl(), pyrGrp.getGrpNm());

		payorGroupExclusionTable.setEffectiveDate(effDt);
		payorGroupExclusionTable.setExpirationDate(expDt);
		payorGroupExclusionTable.setGroupName(pyrGrp.getGrpNm());

		return payorGroupExclusionTable;
	}

	private void verifyPayorExclusionDataSaved(String editType, String clnAbbrev, String pyrAbbrev, PayorExclusionTable payorExclusionTable) throws XifinDataAccessException {
		boolean isEmpty = false;
		PyrClnExcl pyrClnExcl = null;

		try {
			pyrClnExcl = rpmDao.getPyrClnExclByClnAbbrevPyrAbbrev(testDb, clnAbbrev, pyrAbbrev);
		}catch (Exception e) {
			isEmpty = true;
			logger.info("       No record found in pyr_cln_excl for clnAbbrev = " + clnAbbrev + " and pyrAbbrev = " + pyrAbbrev);
		}

		switch (editType) {
			case ADDNEW:
			case UPDATE:
				assertEquals(pyrClnExcl.getEffDt(), payorExclusionTable.getEffectiveDate());
				assertEquals(pyrClnExcl.getExpDt(), payorExclusionTable.getExpirationDate());
				break;
			case DELETED:
				assertEquals(isEmpty, true);
		}
	}

	private void verifyPayorGroupExclusionDataSaved(String editType, String clnAbbrev,  String grpNm, PayorGroupExclusionTable payorGroupExclusionTable) throws Exception {
		boolean isEmpty = false;
		PyrGrpClnExcl pyrGrpClnExcl = null;

		try {
			pyrGrpClnExcl = rpmDao.getPyrGrpClnExclByClnAbbrevGrpNm(testDb, clnAbbrev ,grpNm);
		}catch (Exception e) {
			isEmpty = true;
			logger.info("       No record found in pyr_grp_cln_excl for clnAbbrev = " + clnAbbrev + " and grpNm = " + grpNm);
		}

		switch (editType) {
			case ADDNEW:
			case UPDATE:
				assertEquals(pyrGrpClnExcl.getEffDt(), payorGroupExclusionTable.getEffectiveDate());
				assertEquals(pyrGrpClnExcl.getExpDt(), payorGroupExclusionTable.getExpirationDate());
				break;
			case DELETED:
				assertEquals(isEmpty, true);
		}
	}

	private PayorExclusionTable getValuesInPayorExclusionTable(String row) throws Exception {
		payorExclusions = new PayorExclusions(driver);
		PayorExclusionTable payorExclusionTable = new PayorExclusionTable();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		Date effDt = new Date(formatter.parse(payorExclusions.payorExclusionTblEffDateTxt(row).getText()).getTime());
		Date expDt = new Date(formatter.parse(payorExclusions.payorExclusionTblExpDateTxt(row).getText()).getTime());
		Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, payorExclusions.payorExclusionTblPayorIdTxt(row).getText());

		payorExclusionTable.setEffectiveDate(effDt);
		payorExclusionTable.setExpirationDate(expDt);
		payorExclusionTable.setPayorId(pyr.getPyrAbbrv());
		payorExclusionTable.setPayorName(rpmDao.getPyrByPyrAbbrv(testDb, pyr.getPyrAbbrv()).getName().trim());

		return payorExclusionTable;
	}

	private PayorGroupExclusionTable getValuesInPayorGroupExclusionTable(String row) throws Exception {
		payorExclusions = new PayorExclusions(driver);
		PayorGroupExclusionTable payorGroupExclusionTable = new PayorGroupExclusionTable();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		Date effDt = new Date(formatter.parse(payorExclusions.payorGrpExclusionTblEffDateTxt(row).getText()).getTime());
		Date expDt = new Date(formatter.parse(payorExclusions.payorGrpExclusionTblExpDateTxt(row).getText()).getTime());
		PyrGrp pyrGrp = rpmDao.getPyrGrpByGrpNm(testDb, payorExclusions.payorGrpExclusionTblGroupNameTxt(row).getText());

		payorGroupExclusionTable.setEffectiveDate(effDt);
		payorGroupExclusionTable.setExpirationDate(expDt);
		payorGroupExclusionTable.setGroupName(pyrGrp.getGrpNm());

		return payorGroupExclusionTable;
	}

	private void verifyPayorExclusionTableIsEmpty(PayorExclusionTable payorExclusionTable) throws Exception {
		assertTrue(isElementPresent(payorExclusions.payorExclusionTblTotalRecordsTxt(), 5),"        Payor Exclusion table total result is displayed.");
		assertEquals(payorExclusions.payorExclusionTblTotalRecordsTxt().getText(), "Empty records");
	}

	private void verifyPayorGroupExclusionTableIsEmpty(PayorGroupExclusionTable payorGroupExclusionTable) throws Exception {
		assertTrue(isElementPresent(payorExclusions.payorGrpExclusionTblTotalRecordsTxt(), 5),"        Payor Group Exclusion table total result is displayed.");
		assertEquals(payorExclusions.payorGrpExclusionTblTotalRecordsTxt().getText(), "Empty records");
	}

}

