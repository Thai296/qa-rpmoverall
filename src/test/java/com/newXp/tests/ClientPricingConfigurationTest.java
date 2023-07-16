package com.newXp.tests;


import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnAccntTyp.ClnAccntTyp;
import com.mbasys.mars.ejb.entity.clnDt.ClnDt;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.prc.Prc;
import com.mbasys.mars.ejb.entity.qRetro.QRetro;
import com.mbasys.mars.ejb.entity.qRetroAccn.QRetroAccn;
import com.mbasys.mars.ejb.entity.systemSetting.SystemSetting;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.client.clientProcessing.ClientPricingConfiguration;
import com.overall.menu.MenuNavigation;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.domain.ClientRetroPriceImpact;
import com.xifin.util.MarsDate;
import com.xifin.utils.ListUtil;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TimeStamp;
import domain.client.pricingconfiguration.Header;
import domain.client.pricingconfiguration.RetroactivePriceImpact;
import org.apache.log4j.Logger;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import static org.testng.Assert.*;


public class ClientPricingConfigurationTest extends SeleniumBaseTest {
	private TimeStamp timeStamp;
	private ClientPricingConfiguration clientPricingConfiguration;

	private static final Object PRICING_CONFIGURATION_TITLE = "Pricing Configuration";
	private static final String EMPTY = "";
	private static final String DATE_FORMAT = "MM/dd/yyyy";
	private static final String DOLLAR_ICON = "$";
	private static final SimpleDateFormat DATE_FORMAT_MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
	private final String effDt="12/31/2010";

	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String ssoUsername, String ssoPassword, Method method) {
		try {
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			MenuNavigation navigation = new MenuNavigation(driver, config);
			logIntoSso(ssoUsername, ssoPassword);
			navigation.navigateToClientPricingConfigurationPage();

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
			Date effectiveDate = new Date(DATE_FORMAT_MMDDYYYY.parse(effDt).getTime());
			cleanUpTestData(Arrays.asList(clientId1));
			clientDao.deleteClnDtByClnIdAndClnEffDt(clientDao.getClnByClnAbbrev(clientId1).getClnId(),effectiveDate);

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
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "disableBrowserPlugins", "clientId1"})
	public void AfterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, @Optional String disableBrowserPlugins, String clientId1)
	{
		try
		{
			logger.info("Running AfterSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			Date effectiveDate = new Date(DATE_FORMAT_MMDDYYYY.parse(effDt).getTime());
			cleanUpTestData(Arrays.asList(clientId1));
			clientDao.deleteClnDtByClnIdAndClnEffDt(clientDao.getClnByClnAbbrev(clientId1).getClnId(),effectiveDate);
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

	@Test(priority = 1, description = "Verify retro pricing can be triggered")
	@Parameters({"clientId1", "specialPriceTable"})
	public void testXPR_1698(String clientId1, String specialPriceTable) throws Exception {
		logger.info("===== Testing - testXPR_1698 =====");
		ClientPricingConfiguration clientPricingConfiguration = new ClientPricingConfiguration(driver, wait);
		timeStamp = new TimeStamp(driver);
		int newSpecialPriceTableId = prcDao.getPrcByPrcAbbrev(specialPriceTable).get(0).getPrcId();

		logger.info("*** Step 1 Action: - Load a Client Abbrev in Load Client page");
		verifyLoadClientPageIsDisplayed();
		assertTrue(isElementPresent(clientPricingConfiguration.loadClientSectionClientIdInput(), 5),"        Client Pricing Configuration load page client input is displayed");
		setInputValue(clientPricingConfiguration.loadClientSectionClientIdInput(), clientId1);

		logger.info("*** Expected Results: - Verify that the Pricing Configuration page is displayed");
		Header expectedHeader = setValueToHeader(clientId1);
		Header actualHeader = getValuesInHeader();
		assertEquals(actualHeader, expectedHeader, "       Client Pricing Configuration page Header section is displayed.");

		logger.info("*** Action: - Change the Special Price Table in Pricing Detail grid to another one");
		clickHiddenPageObject(clientPricingConfiguration.editPdEffDt(), 0);
		setInputValue(clientPricingConfiguration.prcDetailSectionEffDtInput(), effDt);
		setInputValue(clientPricingConfiguration.prcDetailSectionClnPrcGrpSpcPrcTblInput(), specialPriceTable);

		logger.info("*** Action: - Click Save button");
		assertTrue(isElementPresent(clientPricingConfiguration.footerSaveBtn(), 5),"        Save button is displayed.");
		clickOnElement(clientPricingConfiguration.footerSaveBtn());

		logger.info("*** Expected Results: - Verify that Retroactive Price Impact grid shows");
		int clnId = clientDao.getClnByClnAbbrev(clientId1).getClnId();
		List<ClientRetroPriceImpact> clientRetroPriceImpactList = getClientRetroPriceImpact(clnId, newSpecialPriceTableId);
		List<RetroactivePriceImpact> actualRetroactivePriceImpactList = getRetroactivePriceImpactValuesInDB(clientRetroPriceImpactList);
		List<RetroactivePriceImpact> expectedRetroactivePriceImpactList = getRetroactivePriceImpactValuesInUI();
		verifyRetroactivePriceImpactShows(actualRetroactivePriceImpactList, expectedRetroactivePriceImpactList);///////////////not working

		logger.info("*** Action: - Click Save button");
		assertTrue(isElementPresent(clientPricingConfiguration.footerSaveBtn(), 5),"        Save button is displayed.");
		clickOnElement(clientPricingConfiguration.footerSaveBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Pricing Configuration page");
		Thread.sleep(3000);
		actualHeader = getValuesInHeader();
		assertEquals(actualHeader, expectedHeader, "       Client Pricing Configuration page Header section is displayed.");

		logger.info("*** Action: - Click Save and Clear button");
		assertTrue(isElementPresent(clientPricingConfiguration.footerSaveAndClearBtn(), 5),"        Save and Clear button at footer is displayed");
		clickOnElement(clientPricingConfiguration.footerSaveAndClearBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Client page");
		verifyLoadClientPageIsDisplayed();

		logger.info("*** Expected Results: - Verify that a new record is added to q_retro table in DB");
		String inDtStr = timeStamp.getCurrentDate();
		Date inDt = new Date(DATE_FORMAT_MMDDYYYY.parse(inDtStr).getTime());
		QRetro qRetro = qRetroDao.getQRetroByClnIdInDt(clnId, inDt).get(0);
		int retroBatchId = verifyNewRecordIsAddedToQRETROInDB(qRetro, actualRetroactivePriceImpactList, clnId);

		logger.info("*** Expected Results: - Verify that multiple records are added to q_retro_accn table in DB");
		verifyMultiRecordsAddedToQRETROACCNInDB(retroBatchId, clientRetroPriceImpactList);

		logger.info("*** Expected Results: - Verify that the cln_dt record got updated with the new special price table id in DB");
		Date effectiveDate = new Date(DATE_FORMAT_MMDDYYYY.parse(effDt).getTime());
		ClnDt clnDt = clientDao.getClnDtByClnIdEffDt(clnId, effectiveDate);
		Prc prc = prcDao.getPrcByPrcAbbrev(specialPriceTable).get(0);
		verifyRecordUpdatedInCLNDTInDB(clnDt, prc.getPrcId());
//		clientDao.deleteClnDtByClnIdAndClnEffDt(clnId,effectiveDate);

	}

	//===================== Utils methods =========================
	private void cleanUpTestData(List<String> clientIdList) throws XifinDataAccessException, XifinDataNotFoundException {
		for (String clientAbbrev: clientIdList) {
			logger.info("       ========== Cleaning up data for Client " + clientAbbrev + " ==========");
			int clientId = clientDao.getClnByClnAbbrev(clientAbbrev).getClnId();
			List<QRetro> qRetroList = qRetroDao.getQRetroByClnId(clientId);

			//q_retro_accn, q_retro
			for (QRetro qRetro:qRetroList){
				int retroBatchId = qRetro.getRetroBatchId();
				accessionDao.deleteQRetroAccnByRetroBatchId(retroBatchId);
				qRetroDao.deleteQRetroByRetroBatchId(retroBatchId);
			}

			//cln_dt
			ClnDt clnDt = clientDao.getClnDtWithMaxEffDtByClnId(clientId);
			clnDt.setClnSpcPrcId(0);
			clientDao.setClnDt(clnDt);
		}
	}

	private void verifyLoadClientPageIsDisplayed() throws Exception {
		clientPricingConfiguration = new ClientPricingConfiguration(driver, wait);
		wait.until(ExpectedConditions.elementToBeClickable(clientPricingConfiguration.pricingConfigLoadPageTitleTxt()));
		assertTrue(isElementPresent(clientPricingConfiguration.pricingConfigLoadPageTitleTxt(), 5),"        Client Pricing Configuration load page title is displayed");
		assertEquals(PRICING_CONFIGURATION_TITLE, clientPricingConfiguration.pricingConfigLoadPageTitleTxt().getText(), "        Client Pricing Configuration title equals 'Pricing Configuration'");

		assertTrue(isElementEnabled(clientPricingConfiguration.pricingConfigLoadPageTitleTxt(), 5, true),"        Load Client section at Load Page is displayed");
	}

	private Header setValueToHeader(String clnAbbrv) throws Exception {
		Header header = new Header();
		Cln cln;

		cln = clientDao.getClnByClnAbbrev(clnAbbrv);
		ClnAccntTyp clnAccntTyp = clientDao.getClnAccnTypByAccnTypId(cln.getAcctTypId());
		Fac fac = facilityDao.getFacByFacId(cln.getOrderingFacId());

		header.setClientId(clnAbbrv);
		header.setClientName(cln.getBilAcctNm().toUpperCase());
		header.setAccountType(clnAccntTyp.getDescr());
		header.setFacAbbr(fac.getAbbrv());
		header.setFacNm(fac.getName());

		return header;
	}

	private Header getValuesInHeader() throws XifinDataAccessException {
		Header header = new Header();
		clientPricingConfiguration = new ClientPricingConfiguration(driver, wait);

		wait.until(ExpectedConditions.elementToBeClickable(clientPricingConfiguration.headerClientIdTxt()));

		Cln cln = clientDao.getClnByClnAbbrev(clientPricingConfiguration.headerClientIdTxt().getText());
		header.setClientId(cln.getClnAbbrev());
		header.setClientName(clientPricingConfiguration.headerClientNameTxt().getText());
		header.setAccountType(clientPricingConfiguration.headerAccountTypeTxt().getText());
		header.setFacAbbr(clientPricingConfiguration.headerFacAbbrevTxt().getText());
		header.setFacNm(clientPricingConfiguration.headerFacNameTxt().getText());

		return header;
	}

	private List<ClientRetroPriceImpact> getClientRetroPriceImpact(int clnId, int newSpecialPriceTableId) throws XifinDataNotFoundException, XifinDataAccessException {
		ClnDt clnDt = clientDao.getClnDtWithMaxEffDtByClnId(clnId);

		List<ClientRetroPriceImpact> clientRetroPriceImpactList = clientDao.getClientRetroPriceImpact(clnDt, newSpecialPriceTableId);

		return clientRetroPriceImpactList;
	}

	private List<RetroactivePriceImpact> getRetroactivePriceImpactValuesInDB(List<ClientRetroPriceImpact> clientRetroPriceAccnTests) {
		List<RetroactivePriceImpact> retroactivePriceImpactTbls = new ArrayList<>();
		String clnId = EMPTY;
		Date invoiceDt = null;
		double totalEstImp = 0;
		for (int i = 0; i < clientRetroPriceAccnTests.size(); i++) {
			double begAmt = 0;
			double retroAdj = 0;
			double endAmt = 0;
			double totalAdj = 0;
			double totalAdjTmp = 0;
			String cmt = EMPTY;
			boolean isExist = true;
			RetroactivePriceImpact retroactivePriceImpactTbl = new RetroactivePriceImpact();
			if (clnId.equals(EMPTY)) {
				clnId = clientRetroPriceAccnTests.get(i).getClientId();
				invoiceDt = clientRetroPriceAccnTests.get(i).getInvoiceDt();
			}

			while (i < clientRetroPriceAccnTests.size()) {
				retroactivePriceImpactTbl.setClientId(clientRetroPriceAccnTests.get(i).getClientId());
				retroactivePriceImpactTbl.setClientName(clientRetroPriceAccnTests.get(i).getClientName());
				while (clientRetroPriceAccnTests.get(i).getClientId().equals(clnId)) {
					Date tempInvoiceDt = clientRetroPriceAccnTests.get(i).getInvoiceDt();
					if (tempInvoiceDt.equals(invoiceDt)) {
						retroactivePriceImpactTbl.setInvoiceDt(clientRetroPriceAccnTests.get(i).getInvoiceDt());
						begAmt += clientRetroPriceAccnTests.get(i).getBegAmt();
						retroAdj += clientRetroPriceAccnTests.get(i).getRetroAdj();
						totalAdj += clientRetroPriceAccnTests.get(i).getRetroAdj();
						endAmt += clientRetroPriceAccnTests.get(i).getEndAmt();
						cmt = getRetroComment(clientRetroPriceAccnTests.get(i));
						i++;
						if (i == clientRetroPriceAccnTests.size()) {
							invoiceDt = null;
							isExist = false;
						} else {
							invoiceDt = clientRetroPriceAccnTests.get(i).getInvoiceDt();
						}
					}
					if (!tempInvoiceDt.equals(invoiceDt)) {
						totalAdjTmp += retroAdj;
						totalEstImp += totalAdjTmp;
						retroactivePriceImpactTbl.setInvoiceDt(tempInvoiceDt);
						retroactivePriceImpactTbl.setBegAmt(roundNumber(begAmt));
						retroactivePriceImpactTbl.setRetroAdj(roundNumber(retroAdj));
						retroactivePriceImpactTbl.setEndAmt(roundNumber(endAmt));
						retroactivePriceImpactTbl.setCmt(cmt);
						retroactivePriceImpactTbls.add(retroactivePriceImpactTbl);
						retroactivePriceImpactTbl = new RetroactivePriceImpact();
						begAmt = 0;
						retroAdj = 0;
						endAmt = 0;
						totalAdjTmp = 0;
					}
					if (!isExist) {
						break;
					}
				}
				retroactivePriceImpactTbls.get(retroactivePriceImpactTbls.size()-1).setTotalAdj(roundNumber(totalAdj));
				if (!isExist) {
					break;
				}
				clnId = clientRetroPriceAccnTests.get(i).getClientId();
				totalAdj = 0;
			}
		}
		retroactivePriceImpactTbls.get(0).setTotalEstImp(roundNumber(totalEstImp));
		return retroactivePriceImpactTbls;
	}

	private String getRetroComment(ClientRetroPriceImpact clientRetroPriceAccnTest) {
		String cmt = EMPTY;
		MarsDate todayLastYear = MarsDate.now(-365);
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		MarsDate acctPd = null;
		try {
			SystemSetting ss = systemDao.getSystemSetting(SystemSettingMap.SS_ACCOUNT_PERIOD_END_DATE);
			acctPd = new MarsDate(format.parse(ss.getDataValue()).getTime());
		} catch (Exception e) {
		}
		if (clientRetroPriceAccnTest.getInvoiceDt().before(todayLastYear)) {
			cmt = "Retro period is more than one year.";
		} else {
			try {
				if (acctPd != null && acctPd.inSameMonth(clientRetroPriceAccnTest.getInvoiceDt())) {
					cmt = "Reprice current month.";
				}
			} catch (Exception e) {

			}
		}
		return cmt;
	}

	private double roundNumber(double dollars) { //round 2 num after .
		dollars =(Math.round(dollars * 100.0))/ 100.0;
		return dollars;
	}

	private List<RetroactivePriceImpact> getRetroactivePriceImpactValuesInUI() throws Exception {
		List<RetroactivePriceImpact> retroactivePriceImpactTbls = new ArrayList<>();
		RetroactivePriceImpact retroactivePriceImpactTbl;// = new RetroactivePriceImpact();
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		clientPricingConfiguration = new ClientPricingConfiguration(driver, wait);


		assertTrue(isElementPresent(clientPricingConfiguration.retroactivePrcImpactTblTotalRecordTxt(), 5),"        Total Records Txt is displayed");
		assertTrue(isElementPresent(clientPricingConfiguration.retroactivePrcImpactTblTotalPageTxt(), 5),"        Total Pages Txt is displayed");
		scrollToElement(clientPricingConfiguration.retroactivePrcImpactTblTotalRecordTxt());

		int totalRecords = Integer.parseInt(getTotalResultSearch(clientPricingConfiguration.retroactivePrcImpactTblTotalRecordTxt()));
		int totalPages = Integer.parseInt(clientPricingConfiguration.retroactivePrcImpactTblTotalPageTxt().getText());
		if (totalRecords > 0) {
			while (totalPages >= 1) {
				while (totalPages >= 1) {
					List<WebElement> retroactivePrcImpactTblAllRow = clientPricingConfiguration.retroactivePrcImpactTblAllRow();
					for (int i = 1; i <= retroactivePrcImpactTblAllRow.size(); i++) {
						retroactivePriceImpactTbl = new RetroactivePriceImpact();
						assertTrue(isElementPresent(clientPricingConfiguration.retroactivePrcImpactTblClnIdTxt(String.valueOf(i + 1)), 1), "         Retroactive Price Impact table - Cln id is displayed");
						retroactivePriceImpactTbl.setClientId(convertNull(clientPricingConfiguration.retroactivePrcImpactTblClnIdTxt(String.valueOf(i + 1)).getText()));
						retroactivePriceImpactTbl.setClientName(convertNull(clientPricingConfiguration.retroactivePrcImpactTblClnNameTxt(String.valueOf(i + 1)).getText()));
						retroactivePriceImpactTbl.setInvoiceDt(new Date(formatter.parse(clientPricingConfiguration.retroactivePrcImpactTblInvoiceDtTxt(String.valueOf(i + 1)).getText()).getTime()));
						retroactivePriceImpactTbl.setBegAmt(Double.parseDouble(convertNumber(clientPricingConfiguration.retroactivePrcImpactTblBegAmtTxt(String.valueOf(i + 1)).getText()).replace(",", "")));
						retroactivePriceImpactTbl.setRetroAdj(Double.parseDouble(convertNumber(clientPricingConfiguration.retroactivePrcImpactTblRetroAmtTxt(String.valueOf(i + 1)).getText()).replace(",", "")));
						retroactivePriceImpactTbl.setEndAmt(Double.parseDouble(convertNumber(clientPricingConfiguration.retroactivePrcImpactTblEndPrcTxt(String.valueOf(i + 1)).getText()).replace(",", "")));
						retroactivePriceImpactTbl.setTotalAdj(Double.parseDouble(convertNumber(clientPricingConfiguration.retroactivePrcImpactTblTotalAdjTxt(String.valueOf(i + 1)).getText()).replace(",", "")));
						retroactivePriceImpactTbl.setCmt(convertNull(clientPricingConfiguration.retroactivePrcImpactTblCommentTxt(String.valueOf(i + 1)).getText()));
						retroactivePriceImpactTbls.add(retroactivePriceImpactTbl);
					}
					if (totalPages > 1) {
						assertTrue(isElementPresent(clientPricingConfiguration.retroactivePrcImpactTblNextPageIco(), 1),"        Next icon is displayed");
						clickHiddenPageObject(clientPricingConfiguration.retroactivePrcImpactTblNextPageIco(), 0);
					}
					totalPages--;
				}
			}
		}
		assertTrue(isElementPresent(clientPricingConfiguration.retroactivePrcImpactTotalEstimatedImpactTxt(), 1),"        Next icon is displayed");
		double totalEstImp = Double.parseDouble(clientPricingConfiguration.retroactivePrcImpactTotalEstimatedImpactTxt().getText().split(": ")[1].replace(DOLLAR_ICON, "").replace(",", ""));
		retroactivePriceImpactTbls.get(0).setTotalEstImp(totalEstImp);
		return retroactivePriceImpactTbls;
	}

	private String convertNumber(String data) {
		return ((data == null || (data != null && data.trim().length() == 0)) ? "0" : data);
	}

	private String convertNull(String data){
		return ((data == null || (data != null && data.trim().length() == 0)) ? "" : data);
	}

	/* Get total Row of Table in Result page and format number if its size > 3 */
	public String getTotalResultSearch(WebElement pager) {
		String total = pager.getText();
		StringTokenizer strToken = new StringTokenizer(total, " ");
		String numberOfResult = "";
		while (strToken.hasMoreTokens()) {
			numberOfResult = strToken.nextToken();
			if (numberOfResult.equalsIgnoreCase("records")
					|| numberOfResult.isEmpty() ||numberOfResult.equalsIgnoreCase("Result") || numberOfResult.equalsIgnoreCase("record")) {
				return "0";
			}
		}
		return numberOfResult;
	}

	private void verifyRetroactivePriceImpactShows(List<RetroactivePriceImpact> actRetroactivePriceImpactTbls, List<RetroactivePriceImpact> expRetroactivePriceImpactTbls) {
		ListUtil listUtil = new ListUtil();
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		List<String> clnIdDB = new ArrayList<>();
		List<String> clnNmDB = new ArrayList<>();
		List<String> invoiceDtDB = new ArrayList<>();
		List<Double> begAmtDB = new ArrayList<>();
		List<Double> retroAdjDB = new ArrayList<>();
		List<Double> endAmtDB = new ArrayList<>();
		List<Double> totalAdjDB = new ArrayList<>();
		double totalEstImpDB = actRetroactivePriceImpactTbls.get(0).getTotalEstImp();
		for (RetroactivePriceImpact actRetroactivePriceImpactTbl : actRetroactivePriceImpactTbls) {
			clnIdDB.add(actRetroactivePriceImpactTbl.getClientId());
			clnNmDB.add(actRetroactivePriceImpactTbl.getClientName());
			invoiceDtDB.add(formatter.format(actRetroactivePriceImpactTbl.getInvoiceDt()));
			begAmtDB.add(actRetroactivePriceImpactTbl.getBegAmt());
			retroAdjDB.add(actRetroactivePriceImpactTbl.getRetroAdj());
			endAmtDB.add(actRetroactivePriceImpactTbl.getEndAmt());
			totalAdjDB.add(actRetroactivePriceImpactTbl.getTotalAdj());
		}

		List<String> clnIdUI = new ArrayList<>();
		List<String> clnNmUI = new ArrayList<>();
		List<String> invoiceDtUI = new ArrayList<>();
		List<Double> begAmtUI = new ArrayList<>();
		List<Double> retroAdjUI = new ArrayList<>();
		List<Double> endAmtUI = new ArrayList<>();
		List<Double> totalAdjUI = new ArrayList<>();
		double totalEstImpUI = expRetroactivePriceImpactTbls.get(0).getTotalEstImp();

		for (RetroactivePriceImpact expRetroactivePriceImpactTbl : expRetroactivePriceImpactTbls) {
			clnIdUI.add(expRetroactivePriceImpactTbl.getClientId());
			clnNmUI.add(expRetroactivePriceImpactTbl.getClientName());
			invoiceDtUI.add(formatter.format(expRetroactivePriceImpactTbl.getInvoiceDt()));
			begAmtUI.add(expRetroactivePriceImpactTbl.getBegAmt());
			retroAdjUI.add(expRetroactivePriceImpactTbl.getRetroAdj());
			endAmtUI.add(expRetroactivePriceImpactTbl.getEndAmt());
			totalAdjUI.add(expRetroactivePriceImpactTbl.getTotalAdj());
		}

		assertEquals(totalEstImpDB, totalEstImpUI, "        Total Estimated Impact is displayed correctly");

		//Remove empty elements from list
		clnIdDB.removeAll(Arrays.asList("", null));
		clnIdUI.removeAll(Arrays.asList("", null));
		clnNmDB.removeAll(Arrays.asList("", null));
		clnNmUI.removeAll(Arrays.asList("", null));

		assertTrue(listUtil.compareLists(clnIdDB, clnIdUI), "        Client Id is displayed correctly");
		assertTrue(listUtil.compareLists(clnNmDB, clnNmUI), "        Client Name is displayed correctly");

	}

	private int verifyNewRecordIsAddedToQRETROInDB(QRetro qRetro, List<RetroactivePriceImpact> actRetroactivePriceImpactTbl, int expectedClnId) throws Exception {
		String curDt = timeStamp.getCurrentDate();
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Date inDt = new Date(formatter.parse(curDt).getTime());

		assertNotNull(qRetro, "       New Record is added to Q_Retro table.");
		assertEquals(qRetro.getClnId(), expectedClnId,"       ClnId is saved correctly");
		assertEquals(qRetro.getInDt(), inDt,"       inDt is saved correctly");
		assertEquals(Double.parseDouble(String.valueOf(qRetro.getEstimateImpactAmtAsMoney())), actRetroactivePriceImpactTbl.get(0).getTotalEstImp(),"       TotalEstImp is saved correctly");

		return qRetro.getRetroBatchId();
	}

	private void verifyMultiRecordsAddedToQRETROACCNInDB(int batchId, List<ClientRetroPriceImpact> clientRetroPriceAccnTests)  throws Exception {
		ListUtil listUtil = new ListUtil();
		List<QRetroAccn> qRetroAccns = accessionDao.getQRetroAccnByRetroBatchId(batchId);
		List<String> actAccnId = new ArrayList<>();
		List<Integer> actTest = new ArrayList<>();
		List<Integer> actAccnTestSeqId = new ArrayList<>();
		for (QRetroAccn qRetroAccn : qRetroAccns) {
			actAccnId.add(qRetroAccn.getAccnId());
			actTest.add(qRetroAccn.getTestId());
			actAccnTestSeqId.add(qRetroAccn.getAccnTestSeqId());
		}

		List<String> expAccnId = new ArrayList<>();
		List<Integer> expTest = new ArrayList<>();
		List<Integer> expAccnTestSeqId = new ArrayList<>();
		for (ClientRetroPriceImpact clientRetroPriceAccnTest : clientRetroPriceAccnTests) {
			expAccnId.add(clientRetroPriceAccnTest.getAccnId());
			expTest.add(clientRetroPriceAccnTest.getTestId());
			expAccnTestSeqId.add(clientRetroPriceAccnTest.getAccnTestSeqId());
		}

		assertTrue(listUtil.compareLists(actAccnId, expAccnId), "        Accn Id is added correctly");
		assertTrue(listUtil.compareLists(actTest, expTest), "        Test is added correctly");
		assertTrue(listUtil.compareLists(actAccnTestSeqId, expAccnTestSeqId), "        Accn Test Seq id is added correctly");
	}

	private void verifyRecordUpdatedInCLNDTInDB(ClnDt clnDt, int expectedPrcId) {
		assertEquals(clnDt.getClnSpcPrcId(), expectedPrcId,"        The Cln_Dt record got updated with the new special price table id");
	}

}

