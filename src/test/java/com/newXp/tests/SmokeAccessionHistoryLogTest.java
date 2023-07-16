package com.newXp.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnProcErr.AccnProcErr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.agncy.Agncy;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.lisInterfaceHistErr.LisInterfaceHistErr;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionProcessing.accessionHistoryLog.AccessionHistoryLog;
import com.overall.menu.MenuNavigation;
import com.overall.search.AccessionSearch;
import com.overall.search.AccessionSearchResults;
import com.overall.utils.XifinPortalUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.rpm.ErrorDao;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ListUtil;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class SmokeAccessionHistoryLogTest extends SeleniumBaseTest {
	
	private TimeStamp timeStamp;
	private MenuNavigation navigation;	
	private AccessionHistoryLog accessionHistoryLog;
	private ListUtil listUtil;
	private XifinPortalUtils xifinPortalUtils;
	private AccessionSearch accessionSearch;
	private AccessionSearchResults accessionSearchResults;

	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String ssoUsername, String ssoPassword, Method method)
	{
		try
		{
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			logIntoSso(ssoUsername, ssoPassword);
		}
		catch (Exception e)
		{
			logger.error("Error running BeforeMethod", e);
		}
	}
	@Test(priority = 1, description = "Verify Accession Information in header section")
	public void testXPR_693() throws Exception {
		logger.info("===== Testing - testXPR_693 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		
		timeStamp = new TimeStamp(driver);				
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();
		
		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");
		
		logger.info("*** Step 2 Actions: - Load an Accession ID");
		Accn accnIDInfo = accessionDao.getAccnByStaIdStmtStatusAndReqIdNotNull(21);
		String accnId = accnIDInfo.getAccnId();
		Accn accnInfo = accessionDao.getAccn(accnId);
		String reqId = accnInfo.getReqId();
		String ptFullName=accnInfo.getPtLNm() +", "+accnInfo.getPtFNm();
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(accnInfo.getDos().toString());
		String dos = targetFormat.format(date1);
		Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(accnInfo.getFinalRptDt().toString());
		String finalReportedDt = targetFormat.format(date2);
		Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(accnInfo.getPrcDt().toString());
		String priceDt = targetFormat.format(date3);
		String stmtStatus = accnInfo.getStmtStatus();
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String orgBal = formatter.format(Float.valueOf(String.valueOf(accnInfo.getExpPrcAsMoney()))).replace("$", "").trim();
		String balDue = formatter.format(Float.valueOf(String.valueOf(accnInfo.getDueAmtAsMoney()))).replace("$", "").trim();
		int clnId = accnInfo.getClnId();
		Cln clnInfo =  clientDao.getClnByClnId(clnId);
		String clnAbbrev = clnInfo.getClnAbbrev().trim();
		String clnName = clnInfo.getBilAcctNm().trim();
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession info (AccnID, Client ID, Client Name, Requisition ID, Patient Name, DOS/Final Report/Price Date, Statement/Accession Status, Original Balance, Balance Due) are displayed properly");
		assertTrue(isElementPresent(accessionHistoryLog.clnIDText(), 5),"        The Client ID Text field is displayed.");
		assertEquals(accessionHistoryLog.clnIDText().getText(),clnAbbrev, "        The Client ID " + clnAbbrev + " should show.");
		
		assertTrue(isElementPresent(accessionHistoryLog.clnNameText(), 5),"        The Client Name is displayed");
		assertEquals(accessionHistoryLog.clnNameText().getText(),clnName, "        The Client Name " + clnName + " should show.");
		
		assertTrue(isElementPresent(accessionHistoryLog.reqIDText(), 5), "        The Requisition ID Text field is displayed");
		assertEquals(accessionHistoryLog.reqIDText().getText(),reqId, "        The Requisition ID " + reqId + " should show.");
		
		assertTrue(isElementPresent(accessionHistoryLog.ptFullNameText(),5),"        The Patient Full Name text field is displayed.");
		assertEquals(accessionHistoryLog.ptFullNameText().getText(),ptFullName, "        The Patient Full Name " + ptFullName + " should show.");

		assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");
		
		assertTrue(isElementPresent(accessionHistoryLog.finalReportDtText(),5),"        The Final Report Date text field is displayed");
		assertEquals(accessionHistoryLog.finalReportDtText().getText(),finalReportedDt, "        The Final Report Date " + finalReportedDt + " should show.");
		
		assertTrue(isElementPresent(accessionHistoryLog.priceDtText(),5),"        The Price Date text field is displayed");
		assertEquals(accessionHistoryLog.priceDtText().getText(),priceDt, "        The Price Date " + priceDt + " should show.");
		
		assertTrue(isElementPresent(accessionHistoryLog.stmtStatusText(),5),"        The Statement Status text field is displayed");
		assertEquals(accessionHistoryLog.stmtStatusText().getText(),stmtStatus,"        The Statement Status " + stmtStatus + " should show.");
		
		assertTrue(isElementPresent(accessionHistoryLog.origBalText(),5),"        The Original Balance text field is displayed");
		assertEquals(accessionHistoryLog.origBalText().getText(), orgBal, "        The Original Balance " + orgBal + " should show.");
		
		assertTrue(isElementPresent(accessionHistoryLog.balDueText(),5),"        The Balance Due text field is displayed");
		assertEquals(accessionHistoryLog.balDueText().getText(), balDue, "        The Balance Due " + balDue + " should show.");
		
		logger.info("*** Step 3 Actions: - Reset the page");		
		accessionHistoryLog.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Load an Accn that has no error history")
	@Parameters({"project", "testSuite", "testCase"})
	public void testXPR_694(String project, String testSuite, String testCase) throws Exception {
		logger.info("===== Testing - testXPR_694 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);				
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");		
		
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");		
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
    	logger.info("       AccnID: " + accnId);
    	
    	logger.info("*** Step 2 Expected Results: Verify that a new accession was created");
		Assert.assertNotNull(accnId);
		
		logger.info("*** Step 3 Actions: - Load the new Accession that has no Error History");		
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 3 Expected results: - Verify that the Accession History Log page is loaded with Error Processing History table is empty");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");		
		
		assertTrue(isElementPresent(accessionHistoryLog.errProcessingHistoryTableTotalPageNum(), 5),"        The total Recode Of Error Processing History is displayed");
		String totalNumOfPage = accessionHistoryLog.errProcessingHistoryTableTotalPageNum().getText();		
		assertEquals(totalNumOfPage, "0", "        The Accn History Log Error Processing History table is empty.");
		
		logger.info("*** Step 4 Actions: - Reset the page");		
		accessionHistoryLog.clickResetBtn();
	}

	@Test(priority = 1, description = "Verify filter functions in Process Queue History table")
	@Parameters({"project", "testSuite", "testCase"})
	public void testXPR_695(String project, String testSuite, String testCase) throws Exception {
		logger.info("===== Testing - testXPR_695 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();
		
		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");		
		
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");		
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("       AccnID: " + accnId);
    	
    	logger.info("*** Step 2 Expected Results: Verify that a new accession was created");
		Assert.assertNotNull(accnId);
		
		logger.info("*** Step 3 Actions: - Load the newly created Accession");		
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 3 Expected results: - Verify that the Accession History Log page is loaded properly");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");			
		
		logger.info("*** Step 4 Actions: - Enter Queue type in Queue column filter");
		assertTrue(isElementPresent(accessionHistoryLog.queueFilterInput(), 5),"        Queue filter input field in Process Queue History should be displayed.");
		String qTyp = daoManagerXifinRpm.getAccnQInfoFromACCNQUEByAccnIdqTypUserId(accnId,"","", null).get(0).get(0).trim();
		accessionHistoryLog.setQueueFilter(qTyp);
		
		logger.info("*** Step 4 Expected Results: - Verify that search result shows that matches the value entered in the Queue column filter");
		List<List<String>> totalRecordsInDB = daoManagerXifinRpm.getAccnQInfoFromACCNQUEByAccnIdqTypUserId(accnId, qTyp,"", null);
		String totalRecordsInUI = xifinPortalUtils.getTotalResultSearch(accessionHistoryLog.totalRecordFooterPQHTbl());
		List<String> listInUI = xifinPortalUtils.getValuesInColTable(this, "tbl_processQueueHistory", 2, Integer.parseInt(totalRecordsInUI), 10, accessionHistoryLog.nextBtn());
		List<String> listInDB = new ArrayList<>();
		for (List<String> strings : totalRecordsInDB) {
			listInDB.add(strings.get(0));
		}
		boolean isListMatch =  listUtil.compareLists(listInUI,listInDB);
		assertTrue(isListMatch, "        Process Queue History in UI and DB do not match.");
		assertEquals(Integer.parseInt(totalRecordsInUI), totalRecordsInDB.size(),"        Total Records in UI and DB do not match.");
		accessionHistoryLog.clearTextbox(accessionHistoryLog.queueFilterInput());
		
		logger.info("*** Step 5 Actions: - Enter User Id in User/Process column filter.");
		assertTrue(isElementPresent(accessionHistoryLog.userProcessFilterOnPQHTbl(), 5),"        User/Process filter input field in Process Queue History table should be displayed.");
		String userId = daoManagerXifinRpm.getAccnQInfoFromACCNQUEByAccnIdqTypUserId(accnId,"","", null).get(0).get(1);
		accessionHistoryLog.setUserProcessFilter(userId);
		
		logger.info("*** Step 5 Expected Results: - Verify that the search results match the filter values");
		totalRecordsInDB = daoManagerXifinRpm.getAccnQInfoFromACCNQUEByAccnIdqTypUserId(accnId, "",userId, null);
		totalRecordsInUI = accessionHistoryLog.totalRecordFooterPQHTbl().getText().split("of")[1].trim();
		listInUI = xifinPortalUtils.getValuesInColTable(this, "tbl_processQueueHistory", 5, Integer.parseInt(totalRecordsInUI), 10, accessionHistoryLog.nextBtn());
		listInDB = new ArrayList<>();
		for (List<String> strings : totalRecordsInDB) {
			listInDB.add(strings.get(1));
		}
		isListMatch =  listUtil.compareLists(listInUI,listInDB);
		assertTrue(isListMatch, "        Process Queue History in UI and DB do not match.");
		assertEquals(Integer.parseInt(totalRecordsInUI), totalRecordsInDB.size(),"        Total Records in UI and DB do not match.");
				
		logger.info("*** Step 6 Actions: - Reset the page");		
		accessionHistoryLog.clickResetBtn();
	}

	@Test(priority = 1, description = "Verify filter functions in Error Processing History table")
	public void testXPR_696() throws Exception {
		logger.info("===== Testing - testXPR_696 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");			
		
		logger.info("*** Step 2 Actions: - Load the Accession that has ACCN_PROC_ERR");		
		AccnProcErr accnProcErrInfoList = accessionDao.getFixedAccnProcErrFromAccnProcErr();
		String accnId = accnProcErrInfoList.getAccnId();
		
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession History Log page is loaded properly");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");			
		
		logger.info("*** Step 3 Actions: - Enter value in the EP Date column filter");
		assertTrue(isElementPresent(accessionHistoryLog.errorProcessingHistoryTbl(), 5),"        Error Processing History Table should be displayed.");
		String epDate = String.valueOf(accnProcErrInfoList.getErrDt());
		accessionHistoryLog.setEpDateInput(epDate);
		
		logger.info("*** Step 3 Expected Results: - Verify that the search results listed in the table match the criteria");
		List<List<String>> totalRecordsInDB = daoManagerXifinRpm.getAccnErrorInfo(accnId, "",epDate,"", null);
		String totalRecordsInUI = xifinPortalUtils.getTotalResultSearch(accessionHistoryLog.totalRecordsErrorProcessHistoryFooterTbl());
		assertEquals(Integer.parseInt(totalRecordsInUI), totalRecordsInDB.size(),"        The total records filtered by EP date should be " + totalRecordsInDB.size());
		
		List<String> listInUI = xifinPortalUtils.getValuesInColTable(this, "tbl_errorProcessingHistory", 2, Integer.parseInt(totalRecordsInUI), 10, accessionHistoryLog.nextIcon());
		List<String> listInDB = new ArrayList<>();
		for (List<String> strings : totalRecordsInDB) {
			listInDB.add(strings.get(3));
		}
		boolean isListMatch =  listUtil.compareLists(listInUI,listInDB);
		assertTrue(isListMatch, "        Total Records in UI and DB do not match.");
		accessionHistoryLog.clearTextbox(accessionHistoryLog.epDateInput());
		
		logger.info("*** Step 4 Actions: - Enter value in the Fixed Date column filter");
		String fixedDate = accnProcErrInfoList.getFixDt().toString();
		accessionHistoryLog.setFixedDateInput(fixedDate);
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results listed in the table match the criteria");
		totalRecordsInDB = daoManagerXifinRpm.getAccnErrorInfo(accnId, "","",fixedDate, null);
		totalRecordsInUI = xifinPortalUtils.getTotalResultSearch(accessionHistoryLog.totalRecordsErrorProcessHistoryFooterTbl());
		assertEquals(Integer.parseInt(totalRecordsInUI), totalRecordsInDB.size(),"        The total records filtered by Fixed date should be " + totalRecordsInDB.size());
		
		listInUI = xifinPortalUtils.getValuesInColTable(this, "tbl_errorProcessingHistory", 2, Integer.parseInt(totalRecordsInUI), 10, accessionHistoryLog.nextIcon());
		listInDB = new ArrayList<>();
		for (List<String> strings : totalRecordsInDB) {
			listInDB.add(strings.get(3));
		}
		isListMatch =  listUtil.compareLists(listInUI,listInDB);
		assertTrue(isListMatch, "        Total Records in UI and DB do not match.");
		accessionHistoryLog.clearTextbox(accessionHistoryLog.fixedDateInput());
		
		logger.info("*** Step 5 Actions: - Enter value in the Error Code column filter");
		List<List<String>> accnErrList = daoManagerXifinRpm.getAccnErrorInfo(accnId,"","","", null);
		String errCd = accessionHistoryLog.getDataNotNull(accnErrList, 0)+": "+accessionHistoryLog.getDataNotNull(accnErrList, 1);
		accessionHistoryLog.setErrorCodeInput(errCd);
		
		logger.info("*** Step 5 Expected Results: - Verify that the search results listed in the table match the criteria");
		totalRecordsInDB = daoManagerXifinRpm.getAccnErrorInfo(accnId, errCd,"","", null);
		totalRecordsInUI = xifinPortalUtils.getTotalResultSearch(accessionHistoryLog.totalRecordsErrorProcessHistoryFooterTbl());
		assertEquals(Integer.parseInt(totalRecordsInUI), totalRecordsInDB.size(), "        The total records filtered by Error Code should be " + totalRecordsInDB.size());
		
		listInUI = xifinPortalUtils.getValuesInColTable(this, "tbl_errorProcessingHistory", 2, Integer.parseInt(totalRecordsInUI), 10, accessionHistoryLog.nextIcon());
		listInDB = new ArrayList<>();
		for (List<String> strings : totalRecordsInDB) {
			listInDB.add(strings.get(3));
		}
		isListMatch =  listUtil.compareLists(listInUI,listInDB);
		assertTrue(isListMatch, "        Total Records in UI and DB do not match.");
				
		logger.info("*** Step 6 Actions: - Reset the page");		
		accessionHistoryLog.clickResetBtn();
	}

	@Test(priority = 1, description = "Load an Accn that has Interface Errors")
	@Parameters({"accnId"})
	public void testXPR_697(String accnId) throws Exception {
		logger.info("===== Testing - testXPR_697 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();
		
		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");			
		
		logger.info("*** Step 2 Actions: - Load the Accession that has LIS_INTERFACE_HIST_ERR");
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession History Log page is loaded properly");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");			
				
		logger.info("*** Step 2 Expected results: - Verify that the Interface Errors are displaying properly");
		assertTrue(isElementPresent(accessionHistoryLog.totalRecordsInterfaceErrorTbl(), 5),"        Total Record text in Interface Errors is displayed.");
		int totalRecordsInDB = Integer.parseInt(xifinPortalUtils.getTotalResultSearch(accessionHistoryLog.totalRecordsInterfaceErrorTbl()));
		List<LisInterfaceHistErr> interfaceErrList = lisInterfaceHistDao.getLisInterfaceHistErrFromLisInterfaceHistErrMsgTypByAccnId(accnId);
		assertEquals( interfaceErrList.size(), totalRecordsInDB,"        The Load Accession History Log page is displayed with valid data at Interface Errors table");
		
		assertTrue(isElementPresent(accessionHistoryLog.nextIconInterfaceErrorTbl(), 5),"        Next Icon Interface Error table is displayed");
		List<String> interfaceErrsInDb = new ArrayList<>();
		for(LisInterfaceHistErr interfaceErr : interfaceErrList){
			interfaceErrsInDb.add(interfaceErr.getMessage().trim());
		}
		List<String> interfaceErrsInUI = xifinPortalUtils.getValuesInColTable(this,"tbl_interfaceErrors",5, totalRecordsInDB,10,accessionHistoryLog.nextIconInterfaceErrorTbl());
		boolean result = listUtil.compareLists(interfaceErrsInDb,interfaceErrsInUI);
		assertTrue(result,"        The Load Accession History Log page is displayed with valid data at Interface Errors table");				
	
		logger.info("*** Step 3 Actions: - Reset the page");		
		accessionHistoryLog.clickResetBtn();
	}

	@Test(priority = 1, description = "Load an Accn that has Correspondence History")
	public void testXPR_698() throws Exception {
		logger.info("===== Testing - testXPR_698 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");			
		
		logger.info("*** Step 2 Actions: - Load the Accession that has Correspondence History");
		String accnId = accessionDao.getRandomSubmFileAudit().getAccnId();
		
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession History Log page is loaded properly");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");			
				
		logger.info("*** Step 2 Expected results: - Verify that the Correspondence History for the Accession are displaying properly");
		assertTrue(isElementPresent(accessionHistoryLog.corresHisTbl(), 5),"        Correspondence History table is displayed.");
		int totalRecordsInDB = Integer.parseInt(xifinPortalUtils.getTotalResultSearch(accessionHistoryLog.totalRecodeOfCorrespHisTbl()));
		List<List<String>> correspHistList = daoManagerXifinRpm.getAccnCorrespHisFromSUBMFILEAUDITByAccnId(accnId, null);
		assertEquals( correspHistList.size(), totalRecordsInDB,"        The total records in DB and UI in Correspondence History for Accession " + accnId + " are not matching.");
		
		assertTrue(isElementPresent(accessionHistoryLog.nextIconCorrespondenceHistoryTbl(), 5),"        Next Icon Correspondence History table is displayed");
		List<String> correspHistInDb = new ArrayList<>();
		for(List<String> correspondenceHistory : correspHistList){
			correspHistInDb.add(correspondenceHistory.get(1));
		}
		
		List<String> correspHistInUI = xifinPortalUtils.getValuesInColTable(this,"tbl_correspondenceHistory",5, totalRecordsInDB,10,accessionHistoryLog.nextIconCorrespondenceHistoryTbl());
		boolean result = listUtil.compareLists(correspHistInDb, correspHistInUI);
		assertTrue(result,"        The Accession History Log page displays Correspondence History properly.");				
	
		logger.info("*** Step 3 Actions: - Reset the page");		
		accessionHistoryLog.clickResetBtn();
	}

	@Test(priority = 1, description = "View Statement file in Correspondence History table")
	@Parameters({"accnId"})
	public void testXPR_699(String accnId) throws Exception {
		logger.info("===== Testing - testXPR_699 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");			
		
		logger.info("*** Step 2 Actions: - Load the Accession that has Correspondence History and statement has been generated");		
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession History Log page is loaded properly");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");		
		
		logger.info("*** Step 3 Actions: - Click the Statement hyperlink");
		assertTrue(isElementPresent(accessionHistoryLog.stmtLnk(), 5), "        Link should be visible.");
		clickHiddenPageObject(accessionHistoryLog.stmtLnk(), 0);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Statement file is opened properly");
		switchToPopupWin();
		String helpUrl = driver.getCurrentUrl();
		assertTrue(helpUrl.contains("docstorewebapp/getdoc.html"), "        The file should be open.");
		
		String text = accessionHistoryLog.fileText().getText().replaceAll("|", "").trim();
		assertTrue(text.contains(accnId), "        The statement file for Accession " + accnId + " should be opened.");
	}

	@Test(priority = 1, description = "Verify that all Help pages can be opened properly")
	public void testXPR_701() throws Exception {
		logger.info("===== Testing - testXPR_701 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();
		
		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");			
			
		logger.info("*** Step 2 Actions: - Load an Accession ID");
		Accn accnIDInfo = accessionDao.getAccnByStaIdStmtStatusAndReqIdNotNull(21);
		String accnId = accnIDInfo.getAccnId();
		
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession History Log page is loaded properly");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");			
		
		logger.info("*** Step 3 Actions: - Click on Help icon button in Process Queue History grid.");
		assertTrue(isElementPresent(accessionHistoryLog.helpIconProcessQueue(), 5), "        Help icon button in Process Queue History grid should show.");
		clickHiddenPageObject(accessionHistoryLog.helpIconProcessQueue(), 0);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Help page can be opened properly");
		String parent = switchToPopupWin();		
		assertTrue(driver.getCurrentUrl().contains("p_history_log_process_queue_history.htm"), "        Process Queue History grid help page should be opened.");		
		assertTrue(helpFileTitle().getText().contains("Process Queue History"), "       Process Queue History grid help page should be displayed.");
		driver.close();
		switchToParentWin(parent);
		
		logger.info("*** Step 4 Actions: Click on Help Icon in Error Processing History grid");		
		assertTrue(isElementPresent(accessionHistoryLog.helpIconOnErrorProcessingHistory(),5), "           Help icon button in Error Processing History grid should show.");
		clickHiddenPageObject(accessionHistoryLog.helpIconOnErrorProcessingHistory(), 0);		
		switchToPopupWin();		
		
		logger.info("*** Step 4 Expected Results: - Verify that Help page can be opened properly");
		assertTrue(driver.getCurrentUrl().contains("p_history_log_error_processing_history.htm"), "        Error Processing History grid Help page should be opened.");
		assertTrue(helpFileTitle().getText().contains("Error Processing History"), "       Error Processing grid help page should be displayed.");
		driver.close();
		switchToParentWin(parent);
		
		logger.info("*** Step 5 Actions: Click on Help Icon in Interface Errors grid");		
		assertTrue(isElementPresent(accessionHistoryLog.helpIconInterfaceErrorTbl(),5), "           Help Icon button in Interface Errors grid should show.");
		clickHiddenPageObject(accessionHistoryLog.helpIconInterfaceErrorTbl(), 0);		
		switchToPopupWin();		
		
		logger.info("*** Step 5 Expected Results: - Verify that Help page can be opened properly");
		assertTrue(driver.getCurrentUrl().contains("p_history_log_interface_errors.htm"), "        Help window should be opened");
		assertTrue(helpFileTitle().getText().contains("Interface Errors"), "       Interface Errors grid help page should be displayed.");
		driver.close();
		switchToParentWin(parent);
		
		logger.info("*** Step 6 Actions: Click on Help icon button in Correspondence History grid");		
		assertTrue(isElementPresent(accessionHistoryLog.helpIconOfCorresHisTbl(),5), "           Help icon button in Correspondence History grid should show.");
		clickHiddenPageObject(accessionHistoryLog.helpIconOfCorresHisTbl(), 0);
		switchToPopupWin();		
		
		logger.info("*** Step 6 Expected Results: - Verify that Help page can be opened properly");
		assertTrue(driver.getCurrentUrl().contains("p_history_log_correspondence_history.htm"), "        Help window should be opened");
		assertTrue(helpFileTitle().getText().contains("Correspondence History"), "       Correspondence History grid help page should be displayed.");
		driver.close();
		switchToParentWin(parent);		
		
		logger.info("*** Step 7 Actions: Click on Page Help icon button in Footer grid");
		assertTrue(isElementPresent(accessionHistoryLog.pageHelpBtn(), 5), "           Page Help icon button in Footer grid should show.");
		clickHiddenPageObject(accessionHistoryLog.pageHelpBtn(), 0);
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that Help page can be opened properly");
		assertTrue(driver.getCurrentUrl().contains("p_history_log_summary.htm"), "        Help window should be opened");
		assertTrue(helpFileTitle().getText().contains("Accession History Log Screen"), "       Page Help page should be displayed.");
		driver.close();
		switchToParentWin(parent);		
		
		logger.info("*** Step 8 Actions: Click on Page Help icon button in Header");
		assertTrue(isElementPresent(accessionHistoryLog.pageHelpIcon(), 5), "           Page Help icon button in Header should show.");
		clickHiddenPageObject(accessionHistoryLog.pageHelpIcon(), 0);
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - Verify that Help page can be opened properly");
		assertTrue(driver.getCurrentUrl().contains("accession_processing_menu/p_history_log_header.htm"), "        Help window should be opened");
		assertTrue(helpFileTitle().getText().contains("Accession History Log Header"), "       Page Help page should be displayed.");
		driver.close();
		switchToParentWin(parent);		
		
		logger.info("*** Step 9 Actions: - Reset the page");		
		accessionHistoryLog.clickResetBtn();
	}

	@Test(priority = 1, description = "Load an Accn from Accession Search")
	public void testXPR_706() throws Exception {
		logger.info("===== Testing - testXPR_706 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);
		accessionSearch = new AccessionSearch(driver);
		accessionSearchResults = new AccessionSearchResults(driver);
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");			
							
		logger.info("*** Step 2 Actions: - Click Accession Search icon button");
		accessionHistoryLog.clickAccnSearchInLoadPageBtn();
		
		String parent = switchToPopupWin();
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession Search screen is displayed");
		Assert.assertTrue(isElementPresent(accessionSearch.accnIdInput(), 5),"        Accession ID input field in Accession Search page is displayed.");
		
		logger.info("*** Step 3 Actions: - Enter an Accession ID in the Accession ID input field and click Search button");
		Accn accnIDInfo = accessionDao.getAccnByStaIdStmtStatusAndReqIdNotNull(21);
		String accnId = accnIDInfo.getAccnId();		
		accessionSearch.setAccnId(accnId);
		
		accessionSearch.clickSearchBtn();
		
		logger.info("*** Step 3 Expected results: - Verify that the Accession Search Results page is displayed");
		Assert.assertTrue(isElementPresent(accessionSearchResults.accnSearchResultTable(), 5),"        Accession Search Results should show.");
		
		logger.info("*** Step 4 Actions: - Select an Accession");
		accessionSearchResults.selectAccnSrchResults(2, 2);
		
		switchToParentWin(parent);
		Thread.sleep(3000);
		
		logger.info("*** Step 4 Expected results: - Verify that the Accession is loaded properly in Accession History Log page");		
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),10),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");			
		
		logger.info("*** Step 5 Actions: - Reset the page");		
		accessionHistoryLog.clickResetBtn();
	}
	
	@Test(priority = 1, description = "View Ack file in Correspondence History table")
	@Parameters({"accnId"})
	public void testXPR_700(String accnId) throws Exception {
		logger.info("===== Testing - testXPR_700 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");			
		
		logger.info("*** Step 2 Actions: - Load the Accession that has Correspondence History and statement has been generated");				
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession History Log page is loaded properly");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");		
		
		logger.info("*** Step 3 Actions: - Click the Ack Filename hyperlink");
		assertTrue(isElementPresent(accessionHistoryLog.ackFileNameLnk(), 5), "        Link should be visible.");
		clickHiddenPageObject(accessionHistoryLog.ackFileNameLnk(), 0);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Ack file is opened properly");
		switchToPopupWin();
		String helpUrl = driver.getCurrentUrl();
		assertTrue(helpUrl.contains("ACK_DC754817F9DB45D48AA67D0C04DA01F1_Z01.txt"), "        The file should be opened.");
		
		String text = accessionHistoryLog.fileText().getText().trim();
		System.out.println(text);		
		assertTrue(text.contains("CRSP78460"), "        The Ack file for Accession " + accnId + " should be opened.");
	}
	
	@Test(priority = 1, description = "Verify View Organization Document hyperlink works")
	public void testXPR_722() throws Exception {
		logger.info("===== Testing - testXPR_722 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");			
			
		logger.info("*** Step 2 Actions: - Load an Accession ID");
		Accn accnIDInfo = accessionDao.getAccnByStaIdStmtStatusAndReqIdNotNull(21);
		String accnId = accnIDInfo.getAccnId();
		
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession History Log page is loaded properly");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");			
		
		logger.info("*** Step 3 Actions: - Click View Organization Document link");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.viewOrgDocumentLnk(),5),"        The Organization Document link should show.");
		clickHiddenPageObject(accessionHistoryLog.viewOrgDocumentLnk(), 0);
		
		logger.info("*** Step 3 Expected results: - Verify that the link specified in the System setting 11002 opens");
		String parent = switchToPopupWin();
		String getURL = driver.getCurrentUrl().substring(0, driver.getCurrentUrl().length()-1);
		String ssValue = systemDao.getSystemSetting(11002).getDataValue().replaceAll("[+]", "").trim();
		Assert.assertEquals(getURL,ssValue,"        The link " + ssValue + " specified in the System setting 11002 should be opened.");
		driver.close();
		switchToParentWin(parent);
		
		logger.info("*** Step 4 Actions: - Reset the page");		
		accessionHistoryLog.clickResetBtn();
	}

	@Test(priority = 1, description = "Verify View Document hyperlink works")
	@Parameters({"accnId"})
	public void testXPR_721(String accnId) throws Exception {
		logger.info("===== Testing - testXPR_721 =====");    
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);

		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");			
			
		logger.info("*** Step 2 Actions: - Load an Accession ID");		
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession History Log page is loaded properly");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");			
		
		logger.info("*** Step 3 Actions: - Click View Organization Document link");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.viewDocumentLnk(), 5),"        The View Document link should show.");
		clickHiddenPageObject(accessionHistoryLog.viewDocumentLnk(), 0);
		
		String parent = switchToPopupWin();		
		
		logger.info("*** Step 3 Expected results: - Verify that DocStore WebApps page can be opened properly");
		String currentUrl = driver.getCurrentUrl();		
		assertTrue(currentUrl.contains("docstorewebapp"),"        DocStore Webapp screen should be opened.");
		assertTrue(driver.getTitle().contains("Document Upload And Storage"), "       The DocStore WebApps page should show.");

		driver.close();
		switchToParentWin(parent);		
		
		logger.info("*** Step 4 Actions: - Reset the page");		
		accessionHistoryLog.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Verify the Print Correspondence History icon button works")
	public void testXPR_720() throws Exception {
		logger.info("===== Testing - testXPR_720 =====");
		
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");				
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");			
		
		logger.info("*** Step 2 Actions: - Load the Accession that has Correspondence History");
		String accnId = accessionDao.getRandomSubmFileAudit().getAccnId();
		
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);
		
		accessionHistoryLog.setAccnIDInput(accnId);
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession History Log page is loaded properly");		
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");		
		
		logger.info("*** Step 3 Actions: - Click on Print icon button in  Correspondence History grid");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.printIconOfCorresHisTbl(),5),"        The Print icon button should show. ");
		clickHiddenPageObject(accessionHistoryLog.printIconOfCorresHisTbl(), 0);
		
		logger.info("*** Step 3 Expected results: - Verify that a new window opens and the Correspondence History are displayed in a preview page");
		WebDriver popup = driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
		Assert.assertEquals(popup.getTitle(),"Correspondence History - XIFIN Portal");
		Assert.assertEquals(popup.getCurrentUrl(),"chrome://print/");
	}

	@Test(priority = 1, description = "View Doc Store Statement file in Correspondence History table")
	@Parameters({"accnId"})
	public void testDocStoreStatementLink(String accnId) throws Exception {
		logger.info("===== Testing - testDocStoreStatementLink =====");

		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);
		listUtil = new ListUtil();
		xifinPortalUtils = new XifinPortalUtils(driver);

		logger.info("*** Step 1 Actions: - Log into  page with SSO username and password");
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Step 1 Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");

		logger.info("*** Step 2 Actions: - Load the Accession that has Correspondence History and statement has been generated");
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);

		accessionHistoryLog.setAccnIDInput(accnId);

		logger.info("*** Step 2 Expected results: - Verify that the Accession History Log page is loaded properly");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");

		logger.info("*** Step 3 Actions: - Click the Statement hyperlink");
		assertTrue(isElementPresent(accessionHistoryLog.stmtLnk(), 5), "        Link should be visible.");
		clickHiddenPageObject(accessionHistoryLog.stmtLnk(), 0);

		logger.info("*** Step 3 Expected Results: - Verify that the Statement file is opened properly");
		switchToPopupWin();
		String helpUrl = driver.getCurrentUrl();
		assertTrue(helpUrl.contains("getdoc.html?"), "        The file should be open. ActualUrl=" + helpUrl);
	}

	@Test(priority = 1, description = "Load accn in Outside Agency Pre Corresp queue")
	@Parameters({"project", "testSuite", "testCase"})
	public void testXPR_1613(String project, String testSuite, String testCase) throws Exception {
		logger.info("===== Testing - testXPR_1613 =====");
		accessionHistoryLog = new  AccessionHistoryLog(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Actions: - Set up AGNCY HOLD_DAYS");
		Agncy agncy = agencyDao.getAgncyByAbbrev("FR-ADD-PRE");
		agncy.setHoldDays(0); //Hold the accn for 0 days
		agencyDao.setAgncy(agncy);

		logger.info("*** Actions: - Set Payor to require the Dx Code");
		Pyr pyr = payorDao.getPyrByPyrAbbrv("BSEAST"); //The pyr used for PFER-598
		pyr.setIsDiagReq(true);
		payorDao.setPyr(pyr);

		logger.info("*** Actions: - Set up Err Code NODIAG");
		ErrCd errCd = errorDao.getErrCdByErrCd(1105); //1105: NODIAG ErrCd
		errCd.setIsAutoMtch(false);
		errCd.setIsMtchCmp(false);
		errCd.setIsManActn(false);
		errCd.setCorrespTypId(0);
		errCd.setOutAgncyId(0);
		errCd.setFinalActnId(7); //7: Hold
		errCd.setOutAgncyIdPreCorresp(6); //6: FR-ADD-PRE
		errorDao.setErrCd(errCd);

		logger.info("*** Actions: - Log into  page with SSO username and password");
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");

		logger.info("*** Actions: - Send WS request to create a Final Reported 3rd Party Accession with NODIAG error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

		logger.info("*** Expected Results: - Verify that a new accession was generated");
		Assert.assertNotNull(accnId, "        A new Accession was generated.");

		logger.info("*** Expected Results: - Ensure the primary accession is Priced and test is split properly");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");
		Assert.assertTrue(accessionDao.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_PRICE,QUEUE_WAIT_TIME_MS*2), "Accession is not out of FR Pending Queue");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");

		logger.info("*** Actions: - Run PF Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the accession has NODIAG accn_pyr_err");
		String currDtStr = timeStamp.getCurrentDate();
		AccnPyrErr accnPyrErrInfoList = errorProcessingDao.getAccnPyrErrByAccnIdErrCdPyrPrioErrDt(accnId, 1105,1, currDtStr); //1105: NODIAG ErrCd
		Assert.assertNotNull(accnPyrErrInfoList, "        Accession ID " + accnId + " should have NODIAG accn_pyr_err.");

		logger.info("*** Expected Results: - Verify that the accession is in Q_EP_OUT_AGNCY_PRE_CORRESP queue");
		//Accn_que
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY_PRE_CORRESP, QUEUE_WAIT_TIME_MS));

		//Q_EP_OUT_AGNCY_PRE_CORRESP
        logger.info("*** Actions: - Switch to Accession History Log screen");
		navigation.navigateToAccnHistoryLogPage();

		logger.info("*** Expected results: - Verify that the Load Accession History screen is displayed");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5),"        Load Accn History screen is displayed");

		logger.info("*** Actions: - Load the new Accession in Accession History Log screen");
		Accn accnInfo = accessionDao.getAccn(accnId);
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(accnInfo.getDos()));
		String dos = targetFormat.format(date1);

		accessionHistoryLog.setAccnIDInput(accnId);

		logger.info("*** Expected results: - Verify that the Accession History Log page is loaded");
		Assert.assertTrue(isElementPresent(accessionHistoryLog.dosText(),5),"        The Date of Service text field is displayed");
		Assert.assertEquals(accessionHistoryLog.dosText().getText(),dos, "        The Date of Service " + dos + " should show.");

		logger.info("*** Expected results: - Verify that the Error Processing History is showing the Outside Agency Pre Corresp information properly");
		//EP Date
		assertEquals(accessionHistoryLog.epDateInErrorProcessingHistoryTbl(2).getAttribute("title").trim(), dos);
		//Reason Code
		assertTrue(accessionHistoryLog.reasonCodeInErrorProcessingHistoryTbl("UNB: NODIAG").isDisplayed());
		//Payor ID
		String accnPyrAbbrev = payorDao.getPyrByAccnIdAndPyrPrio(accnId, 1).getPyrAbbrv().toUpperCase();
		assertTrue(accessionHistoryLog.payorIdInErrorProcessingHistoryTbl(accnPyrAbbrev).isDisplayed());
		//Outside Agency Pre Corresp
		wait.until(ExpectedConditions.visibilityOf(accessionHistoryLog.ourtsideagencyPreCorrespInErrorProcessingHistoryTbl(2)));

		logger.info("*** Actions: - Reset the page");
		accessionHistoryLog.clickResetBtn();
	}
}	
