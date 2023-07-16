package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnDiag.AccnDiag;
import com.mbasys.mars.ejb.entity.accnErr.AccnErr;
import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.pt.Pt;
import com.mbasys.mars.ejb.entity.ptClnLnk.PtClnLnk;
import com.mbasys.mars.ejb.entity.ptDemoChk.PtDemoChk;
import com.mbasys.mars.ejb.entity.ptStdOrder.PtStdOrder;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.qEpReassess.QEpReassess;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.PatientProcessing.PatientDemographics;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.RecordPopUp;
import com.overall.menu.MenuNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PtDemoSweeperEngineTest extends SeleniumBaseTest  {

	private TestDataSetup testDataSetup;
	private TimeStamp timeStamp;
	private XifinAdminUtils xifinAdminUtils;
	private RandomCharacter randomCharacter;
	private PatientDemographics ptDemo;
	private AccessionDetail accessionDetail;
	private RecordPopUp recordPopUp;
	private MenuNavigation navigation;

	@BeforeSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins","UpdatedPerformingFacId","pyrAbbrv"})
	public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins,int UpdatedPerformingFacId, String pyrAbbrv)
	{
		try
		{
			logger.info("Running BeforeSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
			updateSystemSetting(SystemSettingMap.SS_FR_PENDING_FOR_ELIG, "False", "0");
			payorDao.deleteFromPyrFacJurisPyrByFacIdPyrId(UpdatedPerformingFacId, payorDao.getPyrByPyrAbbrev(pyrAbbrv).getPyrId());
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

	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String ssoUsername, String ssoPassword, Method method) {
		try {
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			logIntoSso(ssoUsername, ssoPassword);
			navigation = new MenuNavigation(driver, config);
			logger.info("Running BeforeMethod");
		} catch (Exception e) {
			logger.error("Error running BeforeMethod", e);
		}
	}
	@AfterSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins","UpdatedPerformingFacId","pyrAbbrv"})
	public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins,int UpdatedPerformingFacId, String pyrAbbrv)
	{
		try
		{
			logger.info("Running AfterSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "True", "1");
			updateSystemSetting(SystemSettingMap.SS_FR_PENDING_FOR_ELIG, "True", "1");
			payorDao.deleteFromPyrFacJurisPyrByFacIdPyrId(UpdatedPerformingFacId, payorDao.getPyrByPyrAbbrev(pyrAbbrv).getPyrId());
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
	@Test(priority = 1, description = "Status = 0, Accn is Final Reported, Suspend Pt and PT_DEMO_CHK.Eff_Dt is null")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "isSuspend"})
	public void testPFER_318(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword,boolean isSuspend) throws Exception {
		logger.info("========Testing - PFER_318========");

		timeStamp  = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		ptDemo  = new PatientDemographics(driver, wait);
		xifinAdminUtils = new XifinAdminUtils(driver, config);

		String sysDt = timeStamp.getCurrentDate();
		daoManagerPlatform.setValuesFromTable("pt_demo_chk", "PROCESSED_DT = to_date('" + sysDt + "', 'MM/dd/yyyy') AND STATUS = 1", "STATUS in (0, 2)", testDb);

		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("        AccnID: " + accnId);

		logger.info("*** Step 2 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
    	String pyrAbbrev = accessionDao.getAccnPyrFromAccnByAccnId(accnId).getPyrAbbrv();

		logger.info("*** Step 3 Actions: - Create a new Suspended Patient in RPM Patient Demographics");
		switchToDefaultWinFromFrame();
		navigation.navigateToPatientDemographicsPage();
		switchToPopupWin();

		String epi   = "EPI" + timeStamp.getTimeStamp();
		String zipCd = "92129";
		String randomVal = randomCharacter.getRandomAlphaString(8);
		String pyrAbbrevInPtDemo = payorDao.getPyrFromPyrDtAndPyrByGrpId(10).getPyrAbbrv();
		String PtDOB = timeStamp.getPreviousDate("MM/dd/yyyy",8999);

		ptDemo.setEPI(epi);
		/*
		Note:
			* A social security number is a nine-digit number broken into three groups
			* in the form XXX-XX-XXXX.  The first three digits (the "area") are between
			* 001 and 799 (inclusive).  The next two digits (the "group") are between
			* 01 and 99 (inclusive).  The last four digits (the "serial") are between
			* 0001 and 9999 (inclusive).
			* */
		String ssn = "6" + randomCharacter.getNonZeroRandomNumericString(2) + randomCharacter.getNonZeroRandomNumericString(2) + "8" + randomCharacter.getNonZeroRandomNumericString(3);
		ptDemo.setPtSSN(ssn);
		ptDemo.setPtLastName("LN" + randomVal);
		ptDemo.setPtFirstName("FN" + randomVal);
		ptDemo.setPtDOB(PtDOB);
		ptDemo.setPtGender(ptDemo.ptGenderDropDown(), "1");
		ptDemo.setPtAddr1("ADDR1" + randomVal);
		ptDemo.setPtAddr2("ADDR2" + randomVal);
		ptDemo.setPtZip(zipCd);
		ptDemo.setPtComments("Pt Comments: " + randomVal);
		ptDemo.setPayorId(pyrAbbrevInPtDemo);
		ptDemo.setInsSubsId(randomVal);
		Thread.sleep(7000);
		ptDemo.checkSuspendReason(isSuspend);
		ptDemo.submitBtn().click();
		Thread.sleep(7000);

		logger.info("*** Step 3 Expected Results: - Verify that data are added in PT, PT_PYR, PT_PYR_SUSPEND_REASON_DT and PT_DEMO_CHK tables in DB");
		Pt ptInfo = patientDao.getSuspPtByEpiAndPyrAbbrev(epi, pyrAbbrevInPtDemo);
		Assert.assertNotNull(ptInfo, "        EPI = " + epi + " should be added to the Patient Demo related tables");

		logger.info("*** Step 4 Actions: - Update PT_DEMO_CHK and ACCN tables in DB so the engine can process");
		Assert.assertTrue(daoManagerPlatform.setValuesFromPTDEMOCHKByPTSeqId(String.valueOf(ptInfo.getSeqId()),"EFF_DT =''",testDb)>0,"        PT_DEMO_CHK is updated");
		PtDemoChk newPtDemoInfo1 = patientDao.getLatestPtDemoChk();
		int status = newPtDemoInfo1.getStatus();
		String effDT = String.valueOf(newPtDemoInfo1.getEffDt());
		int updateAccn = daoManagerXifinRpm.setValuesFromACCNByAccnId(accnId, "FK_PT_SEQ_ID = "+ptInfo.getSeqId(), testDb) ;

		logger.info("*** Step 4 Expected Results: - Verify that the Data are available for testing");
		Assert.assertEquals(status, 0, "        Status = 0 in PT_Demo_Chk.");
		Assert.assertEquals(effDT,"null","        EffDT is null in PT_Demo_Chk.") ;
		Assert.assertNotEquals(updateAccn,0,"        Accn is update.") ;

		logger.info("*** Step 5 Actions: - Run PF - Pt Demo Sweeper Engine.");
		isProcessedByPtDemoSweeper(newPtDemoInfo1.getPtSeqId(),QUEUE_WAIT_TIME_MS);
    	Thread.sleep(7000);

		logger.info("*** Step 5 Expected Results: - Verify that the Pt Demo information got updated in the ACCN table and PT View in DB");
		//update in PT_DEMO_CHK
		String currDt = timeStamp.getCurrentDate("yyyy-MM-dd");
		PtDemoChk newPtDemoInfo2 = patientDao.getLatestPtDemoChk();
		int ptSeqId = newPtDemoInfo2.getPtSeqId();
		int processedAccn = accessionDao.getAccnByPtSeqId(ptSeqId);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date effectiveDate = new java.sql.Date(df.parse(currDt).getTime());

		Thread.sleep(2000);

		Assert.assertEquals(newPtDemoInfo2.getStatus(),  1, "        Status should be 1.");
		Assert.assertEquals(newPtDemoInfo2.getProcessedDt(),  effectiveDate, "        Processed date should be "+currDt+".");
		Assert.assertEquals(newPtDemoInfo2.getNumAccnsProcessed(),  processedAccn, "        Processed Accn should be "+processedAccn+".");

		//Accn table: update PT address with data is get from PT table
		Accn accnPtDemoInfoList = accessionDao.getAccnFromAccnAndCountryByAccnId(accnId);
		Pt ptInfoFromPT = patientDao.getPtBySeqId(ptSeqId);

		Assert.assertEquals(accnPtDemoInfoList.getPtFNm(),  ptInfoFromPT.getPtFNm(), "        pt_f_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtLNm(),  ptInfoFromPT.getPtLNm(), "        pt_l_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr1(),  ptInfoFromPT.getPtAddr1(), "        pt_addr1 is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr2(),  ptInfoFromPT.getPtAddr2(), "        pt_addr2 is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtZipId(),  ptInfoFromPT.getPtZipId(), "        fk_pt_zip_id is update in Accn.");

		logger.info("*** Step 5 Expected Results: - Verify that the Payor is not updated in the ACCN_PYR table in DB");
		Assert.assertEquals(accessionDao.getAccnPyrFromAccnByAccnId(accnId).getPyrAbbrv(),  pyrAbbrev, "        The Payor " + pyrAbbrev + " on the Accession " + accnId + " should not be updated to " + pyrAbbrevInPtDemo);
	}


	@Test(priority = 1, description = "Status=2, Accn is Final Reported, Suspend Pt, and PT_DEMO_CHK.eff_Dt is not null")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "isSuspend"})
	public void testPFER_329(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, boolean isSuspend) throws Exception {
		logger.info("========Testing - PFER_329========");

		timeStamp  = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		ptDemo  = new PatientDemographics(driver, wait);
		xifinAdminUtils = new XifinAdminUtils(driver, config);

		String sysDt = timeStamp.getCurrentDate();
		daoManagerPlatform.setValuesFromTable("pt_demo_chk", "PROCESSED_DT = to_date('" + sysDt + "', 'MM/dd/yyyy'), STATUS = 1", "STATUS in (0, 2)", testDb);

		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("        AccnID: " + accnId);

    	logger.info("*** Step 2 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
    	String pyrAbbrev = accessionDao.getAccnPyrFromAccnByAccnId(accnId).getPyrAbbrv();

		logger.info("*** Step 3 Actions: - Create a new Suspended Patient in RPM Patient Demographics");
		switchToDefaultWinFromFrame();
		navigation.navigateToPatientDemographicsPage();
		switchToPopupWin();

		String epi   = "EPI" + timeStamp.getTimeStamp();
		String zipCd = "92127";//daoManagerXifinRpm.getZipCodeFromZIP(testDb).get(0);
		String randomVal = randomCharacter.getRandomAlphaString(8);
		String pyrAbbrevInPtDemo = payorDao.getPyrFromPyrDtAndPyrByGrpId(10).getPyrAbbrv();
		String PtDOB = timeStamp.getPreviousDate("MM/dd/yyyy",8999);

		ptDemo.setEPI(epi);
		/*
		Note:
			* A social security number is a nine-digit number broken into three groups
			* in the form XXX-XX-XXXX.  The first three digits (the "area") are between
			* 001 and 799 (inclusive).  The next two digits (the "group") are between
			* 01 and 99 (inclusive).  The last four digits (the "serial") are between
			* 0001 and 9999 (inclusive).
			* */
		String ssn = "5" + randomCharacter.getNonZeroRandomNumericString(2) + randomCharacter.getNonZeroRandomNumericString(2) + "7" + randomCharacter.getNonZeroRandomNumericString(3);
		ptDemo.setPtSSN(ssn);
		ptDemo.setPtLastName("LN" + randomVal);
		ptDemo.setPtFirstName("FN" + randomVal);
		ptDemo.setPtDOB(PtDOB);
		ptDemo.setPtGender(ptDemo.ptGenderDropDown(), "1");
		ptDemo.setPtAddr1("ADDR1" + randomVal);
		ptDemo.setPtAddr2("ADDR2" + randomVal);
		ptDemo.setPtZip(zipCd);
		ptDemo.setPtComments("Pt Comments: " + randomVal);
		ptDemo.setPayorId(pyrAbbrevInPtDemo);
		ptDemo.setInsSubsId(randomVal);
		Thread.sleep(5000);
		ptDemo.checkSuspendReason(isSuspend);
		ptDemo.submitBtn().click();
		Thread.sleep(7000);

		logger.info("*** Step 3 Expected Results: - Verify that data are added in PT, PT_PYR, PT_PYR_SUSPEND_REASON_DT and PT_DEMO_CHK tables in DB");
		Pt ptInfo = patientDao.getSuspPtByEpiAndPyrAbbrev(epi, pyrAbbrevInPtDemo);
		Assert.assertNotNull(ptInfo, "        EPI = " + epi + " should be added to the Patient Demo related tables");

		logger.info("*** Step 4 Actions: - Update PT_DEMO_CHK and ACCN tables in DB so the engine can process");
		Assert.assertTrue(daoManagerPlatform.setValuesFromPTDEMOCHKByPTSeqId(String.valueOf(ptInfo.getSeqId()),"EFF_DT =''",testDb)>0,"        PT_DEMO_CHK is updated");
		PtDemoChk newPtDemoInfo1 = patientDao.getLatestPtDemoChk();
		int updateAccn = daoManagerXifinRpm.setValuesFromACCNByAccnId(accnId, "FK_PT_SEQ_ID = "+ ptInfo.getSeqId(), testDb) ;

	    String currDt = timeStamp.getCurrentDate();
	    String dtStr = "to_date('" + currDt + "', 'MM/dd/yyyy') ";
	    daoManagerPlatform.setValuesFromTableByColNameValue("PT_DEMO_CHK","status = 2, EFF_DT = " + dtStr, "FK_PT_SEQ_ID", String.valueOf(newPtDemoInfo1.getPtSeqId()), testDb);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date convertedCurrDt = new java.sql.Date(df.parse(timeStamp.getCurrentDate("yyyy-MM-dd")).getTime());

		logger.info("*** Step 4 Expected Results: - Verify that the Data are available for testing");
		newPtDemoInfo1 = patientDao.getLatestPtDemoChk();
		int status = newPtDemoInfo1.getStatus();
		java.sql.Date effDt = newPtDemoInfo1.getEffDt();
		Assert.assertEquals(status, 2, "        Status = 2 in PT_Demo_Chk.");
		Assert.assertEquals(effDt, convertedCurrDt, "        EffDT is " + currDt + " in PT_Demo_Chk.") ;
		Assert.assertNotEquals(updateAccn,0,"        Accn is update.") ;

		logger.info("*** Step 5 Actions: - Run PF - Pt Demo Sweeper Engine.");
		isProcessedByPtDemoSweeper(newPtDemoInfo1.getPtSeqId(),QUEUE_WAIT_TIME_MS);
    	Thread.sleep(5000);

		logger.info("*** Step 5 Expected Results: - Verify that the Pt Demo information got updated in the ACCN table and PT View in DB");
		Thread.sleep(5000);
		PtDemoChk newPtDemoInfo2 = patientDao.getLatestPtDemoChk();
		int ptSeqId = newPtDemoInfo2.getPtSeqId();
		int processedAccn = accessionDao.getAccnByPtSeqId(ptSeqId);

		Assert.assertEquals(newPtDemoInfo2.getStatus(),  1, "        Status should be 1.");
		Assert.assertEquals(newPtDemoInfo2.getProcessedDt(),  convertedCurrDt, "        Processed date should be "+currDt+".");
		Assert.assertEquals(newPtDemoInfo2.getNumAccnsProcessed(),  processedAccn, "        Processed Accn should be "+processedAccn+".");

		//Accn table: update PT address with data is get from PT table
		Accn accnPtDemoInfoList = accessionDao.getAccnFromAccnAndCountryByAccnId(accnId);
		Pt ptInfoFromPT = patientDao.getPtBySeqId(ptSeqId);

		Assert.assertEquals(accnPtDemoInfoList.getPtFNm(),  ptInfoFromPT.getPtFNm(), "        pt_f_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtLNm(),  ptInfoFromPT.getPtLNm(), "        pt_l_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr1(),  ptInfoFromPT.getPtAddr1(), "        pt_addr1 is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr2(),  ptInfoFromPT.getPtAddr2(), "        pt_addr2 is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtZipId(),  ptInfoFromPT.getPtZipId(), "        fk_pt_zip_id is update in Accn.");

		logger.info("*** Step 5 Expected Results: - Verify that the Payor is updated in the ACCN_PYR table in DB");

	}

	@Test(priority = 1, description = "Status=2,Priced Accn,Suspend Pt,PT_DEMO_CHK.eff_dt not null, Reassess Accn")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "isSuspend"})
	public void testPFER_340(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, boolean isSuspend) throws Exception {
		logger.info("========Testing - PFER_340========");

		timeStamp  = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		ptDemo  = new PatientDemographics(driver, wait);
		xifinAdminUtils = new XifinAdminUtils(driver, config);

		String sysDt = timeStamp.getCurrentDate();
		daoManagerPlatform.setValuesFromTable("pt_demo_chk", "PROCESSED_DT = to_date('" + sysDt + "', 'MM/dd/yyyy'), STATUS = 1", "STATUS in (0, 2)", testDb);

		logger.info("*** Step 2 Actions: - Create a new Priced Accession with new EPI");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL), null);
    	logger.info("        AccnID: " + accnId);

    	logger.info("*** Step 2 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
    	String pyrAbbrev = accessionDao.getAccnPyrFromAccnByAccnId(accnId).getPyrAbbrv();

    	Accn accnInfoList = accessionDao.getAccn(accnId);
    	int ptSeqId = accnInfoList.getPtSeqId();

		logger.info("*** Step 3 Actions: - Update the Patient (EPI) Info on the Accession in RPM Patient Demographics");
		switchToDefaultWinFromFrame();
		navigation.navigateToPatientDemographicsPage();
		switchToPopupWin();

		String epi   = patientDao.getPtBySeqId(ptSeqId).getEpi();//"EPI" + timeStamp.getTimeStamp();
		String randomVal = randomCharacter.getRandomAlphaString(8);
		String pyrAbbrevInPtDemo = "P";
		ptDemo.setEPI(epi);
		String ssn = "5" + randomCharacter.getNonZeroRandomNumericString(2) + randomCharacter.getNonZeroRandomNumericString(2) + "7" + randomCharacter.getNonZeroRandomNumericString(3);
		ptDemo.setPtSSN(ssn);
		ptDemo.setPtLastName("LN" + randomVal);
		ptDemo.setPtFirstName("FN" + randomVal);
		ptDemo.setPtAddr1("ADDR1" + randomVal);
		ptDemo.setPtAddr2("ADDR2" + randomVal);
		ptDemo.setPtComments("Pt Comments: " + randomVal);
		ptDemo.setPayorId(pyrAbbrevInPtDemo);
		Thread.sleep(5000);
		ptDemo.checkSuspendReason(isSuspend);
		ptDemo.submitBtn().click();
		Thread.sleep(7000);

		logger.info("*** Step 3 Expected Results: - Verify that data are in PT, PT_PYR, PT_PYR_SUSPEND_REASON_DT and PT_DEMO_CHK tables in DB");
		Pt ptInfo = patientDao.getSuspPtByEpiAndPyrAbbrev(epi, pyrAbbrevInPtDemo);
		Assert.assertNotNull(ptInfo, "        EPI = " + epi + " should be in the Patient Demo related tables");

		logger.info("*** Step 4 Actions: - Update PT_DEMO_CHK in DB so the engine can process");
	    //Set status = 2 (Retry), eff_dt to match the pt_pyr.eff_dt in pt_demo_chk
	    String currDt = timeStamp.getCurrentDate();
	    String dtStr = "to_date('" + currDt + "', 'MM/dd/yyyy') ";
	    daoManagerPlatform.setValuesFromTableByColNameValue("PT_DEMO_CHK","status = 2, EFF_DT = " + dtStr, "FK_PT_SEQ_ID", String.valueOf(ptSeqId), testDb);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date convertedCurrDt = new java.sql.Date(df.parse(timeStamp.getCurrentDate("yyyy-MM-dd")).getTime());

		logger.info("*** Step 4 Expected Results: - Verify that the Data are available for testing");
		PtDemoChk ptDemoChkInfoList = patientDao.getUnprocessedPtDemoChkBySeqIdAndStatus(ptSeqId, 2);
		int status = ptDemoChkInfoList.getStatus();
		java.sql.Date effDt = ptDemoChkInfoList.getEffDt();
		Assert.assertEquals(status, 2, "        Status = 2 in PT_Demo_Chk.");
		Assert.assertEquals(effDt, convertedCurrDt, "        EffDT is " + currDt + " in PT_Demo_Chk.") ;

		logger.info("*** Step 5 Actions: - Run PF - Pt Demo Sweeper Engine.");
		isProcessedByPtDemoSweeper(ptDemoChkInfoList.getPtSeqId(),QUEUE_WAIT_TIME_MS);
    	Thread.sleep(5000);

		logger.info("*** Step 5 Expected Results: - Verify that the Pt Demo information got updated in the ACCN table and PT View in DB");
		ptDemoChkInfoList = patientDao.getProcessedPtDemoChkBySeqIdAndStatus(ptSeqId,1);
		int processedAccn = accessionDao.getAccnByPtSeqId(ptSeqId);

		Assert.assertEquals(ptDemoChkInfoList.getProcessedDt(),  convertedCurrDt, "        Processed date should be "+currDt+".");
		Assert.assertEquals(ptDemoChkInfoList.getNumAccnsProcessed(),  processedAccn, "        Processed Accn should be "+processedAccn+".");

		//Accn table: update PT address with data is get from PT table
		Accn accnPtDemoInfoList = accessionDao.getAccnFromAccnAndCountryByAccnId(accnId);
		Pt ptInfoFromPT = patientDao.getPtBySeqId(ptSeqId);

		Assert.assertEquals(accnPtDemoInfoList.getPtFNm(),  ptInfoFromPT.getPtFNm(), "        pt_f_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtLNm(),  ptInfoFromPT.getPtLNm(), "        pt_l_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr1(),  ptInfoFromPT.getPtAddr1(), "        pt_addr1 is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr2(),  ptInfoFromPT.getPtAddr2(), "        pt_addr2 is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtZipId(),  ptInfoFromPT.getPtZipId(), "        fk_pt_zip_id is update in Accn.");

		logger.info("*** Step 5 Expected Results: - Verify that the Payor is updated in the ACCN_PYR table in DB");
		Assert.assertEquals(payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv(),  pyrAbbrevInPtDemo, "        The Payor " + pyrAbbrev + " on the Accession " + accnId + " should be updated to " + pyrAbbrevInPtDemo);

		logger.info("*** Step 5 Expected Results: - Verify that the record is moved from Q_EP_REASSESS table in DB");
		QEpReassess qEpReassessInfo = accessionDao.getQEpReassesByAccnId(accnId);
		Assert.assertNull(qEpReassessInfo, "        Accession " + accnId + " should not be in Q_EP_REASSESS.");

		logger.info("*** Step 5 Expected Results: - Verify that the info in ACCN_PROC were removed in DB");
		AccnProc accnProcInfoList = accessionDao.getAccnProcByAccnId(accnId);
		Assert.assertNull(accnProcInfoList, "        Accession " + accnId + " accn_proc info should be removed.");

	}

	@Test(priority = 1, description = "Status=2, Process Patient Standing Order")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "isSuspend"})
	public void testPFER_333(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, boolean isSuspend) throws Exception {
		logger.info("========Testing - PFER_333========");

		timeStamp  = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		ptDemo  = new PatientDemographics(driver, wait);
		xifinAdminUtils = new XifinAdminUtils(driver, config);

		String sysDt = timeStamp.getCurrentDate();
		daoManagerPlatform.setValuesFromTable("pt_demo_chk", "PROCESSED_DT = to_date('" + sysDt + "', 'MM/dd/yyyy'), STATUS = 1", "STATUS in (0, 2)", testDb);

		logger.info("*** Step 2 Actions: - Create a new Priced Accession with new EPI");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL), null);
    	logger.info("        AccnID: " + accnId);

    	logger.info("*** Step 2 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

    	Accn accnInfoList = accessionDao.getAccn(accnId);
    	int ptSeqId = accnInfoList.getPtSeqId();

		logger.info("*** Step 3 Actions: - Update the Patient (EPI) Info on the Accession in RPM Patient Demographics");
		switchToDefaultWinFromFrame();
		navigation.navigateToPatientDemographicsPage();
		switchToPopupWin();

		String epi   = patientDao.getPtBySeqId(ptSeqId).getEpi();
		String randomVal = randomCharacter.getRandomAlphaString(8);
		String pyrAbbrevInPtDemo = "P";
		ptDemo.setEPI(epi);
		String ssn = "5" + randomCharacter.getNonZeroRandomNumericString(2) + randomCharacter.getNonZeroRandomNumericString(2) + "7" + randomCharacter.getNonZeroRandomNumericString(3);
		ptDemo.setPtSSN(ssn);
		ptDemo.setPtLastName("LN" + randomVal);
		ptDemo.setPtFirstName("FN" + randomVal);
		ptDemo.setPtAddr1("ADDR1" + randomVal);
		ptDemo.setPtAddr2("ADDR2" + randomVal);

		ptDemo.setPtComments("Pt Comments: " + randomVal);
		ptDemo.setPayorId(pyrAbbrevInPtDemo);
		Thread.sleep(5000);
		ptDemo.checkSuspendReason(isSuspend);
		ptDemo.submitBtn().click();
		Thread.sleep(7000);

		logger.info("*** Step 3 Expected Results: - Verify that data are in PT, PT_PYR, PT_PYR_SUSPEND_REASON_DT and PT_DEMO_CHK tables in DB");
		Pt ptInfo = patientDao.getSuspPtByEpiAndPyrAbbrev(epi, pyrAbbrevInPtDemo);
		Assert.assertNotNull(ptInfo, "        EPI = " + epi + " should be in the Patient Demo related tables");

		logger.info("*** Step 4 Actions: - Update PT_DEMO_CHK in DB so the engine can process");
	    //Set status = 2 (Retry)
	    String currDt = timeStamp.getCurrentDate();
	    daoManagerPlatform.setValuesFromTableByColNameValue("PT_DEMO_CHK","status = 2, EFF_DT = null", "FK_PT_SEQ_ID", String.valueOf(ptSeqId), testDb);

		logger.info("*** Step 4 Expected Results: - Verify that the Data are available for testing");
		PtDemoChk ptDemoChkInfoList = patientDao.getUnprocessedPtDemoChkBySeqIdAndStatus(ptSeqId,2);
		int status = ptDemoChkInfoList.getStatus();
		java.sql.Date effDt = ptDemoChkInfoList.getEffDt();
		Assert.assertEquals(status, 2, "        Status = 2 in PT_Demo_Chk.");
		Assert.assertNull(effDt, "        EffDT is null in PT_Demo_Chk.");

		logger.info("*** Step 5 Actions: - Create new data in Q_Pt_Order_Exp table in DB");
		PtStdOrder ptStdOrderInfoList = patientDao.getRandomPtStdOrder();
		int ptStdOrderSeqId = ptStdOrderInfoList.getSeqId();
		java.sql.Date expDt = ptStdOrderInfoList.getEffDt();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String newExpDt = timeStamp.getNextDay("MM/dd/yyyy", expDt);
		daoManagerPlatform.insertValuesFromQPTSTDORDEREXP(String.valueOf(ptStdOrderSeqId), newExpDt, String.valueOf(ptSeqId), "0", testDb);

		logger.info("*** Step 6 Actions: - Update data in PT_STD_ORDER table in DB");
		daoManagerPlatform.setValuesFromTableByColNameValue("PT_STD_ORDER","FK_PT_SEQ_ID = "+ptSeqId,"PK_SEQ_ID", String.valueOf(ptStdOrderSeqId),  testDb);

		logger.info("*** Step 6 Expected Results: - Verify that data is available for testing in DB");
		Assert.assertNotNull(patientDao.getPtStdOrderBySeqId(ptStdOrderSeqId), "       New Record is added into Db");

		logger.info("*** Step 7 Actions: - Run PF - Pt Demo Sweeper Engine.");
		isProcessedByPtDemoSweeper(ptDemoChkInfoList.getPtSeqId(),QUEUE_WAIT_TIME_MS);
    	Thread.sleep(5000);

		logger.info("*** Step 7 Expected Results: - Verify that the record is processed properly and the Status, Process_Dt and NUM_ACCNS_PROCESSED are updated in PT_DEMO_CHK table in DB");
		ptDemoChkInfoList = patientDao.getProcessedPtDemoChkBySeqIdAndStatus(ptSeqId,1);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date convertedCurrDt = new java.sql.Date(df.parse(timeStamp.getCurrentDate("yyyy-MM-dd")).getTime());
		Assert.assertEquals(ptDemoChkInfoList.getProcessedDt(),  convertedCurrDt, "        Processed date should be "+currDt+".");

		int processedAccn = accessionDao.getAccnByPtSeqId(ptSeqId);
		Assert.assertEquals(ptDemoChkInfoList.getNumAccnsProcessed(),  processedAccn, "        Processed Accn should be "+processedAccn+".");

		logger.info("*** Step 7 Expected Results: - Verify that Pt Demo information got updated in the ACCN table properly in DB");

		Accn accnPtDemoInfoList = accessionDao.getAccnFromAccnAndCountryByAccnId(accnId);
		Pt ptInfoFromPT = patientDao.getPtBySeqId(ptSeqId);

		Assert.assertEquals(accnPtDemoInfoList.getPtFNm(),  ptInfoFromPT.getPtFNm(), "        pt_f_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtLNm(),  ptInfoFromPT.getPtLNm(), "        pt_l_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr1(),  ptInfoFromPT.getPtAddr1(), "        pt_addr1 is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr2(),  ptInfoFromPT.getPtAddr2(), "        pt_addr2 is update in Accn.");
		Date convertedExpDt = new java.sql.Date(df.parse(timeStamp.getNextDay("yyyy-MM-dd", expDt)).getTime());

		logger.info("*** Step 7 Expected Results: - Verify that EXP_DT in PT_STD_ORDER table is updated properly in DB");
		ptStdOrderInfoList = patientDao.getPtStdOrderBySeqId(ptStdOrderSeqId);
		Assert.assertEquals(ptStdOrderInfoList.getExpDt(), convertedExpDt, "        PT_STD_ORDER.ExpDt should be updated to " + newExpDt);

		logger.info("*** Step 7 Expected Results: - Verify that the record in Q_PT_STD_ORDER_EXP table is deleted in DB");
		Assert.assertNull(patientDao.getQPtStdOrderExpBySeqId(ptStdOrderSeqId), "       Pt Std Order Seq Id = " + ptStdOrderSeqId + " should be deleted from table PT_STD_ORDER_EXP.");

	}

	@Test(priority = 1, description = "Status=2, Jurisdiction Payer")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "isSuspend","UpdatedPerformingFacId","pyrAbbrv","jurisPyrAbbrev"})
	public void testPFER_339(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, boolean isSuspend,int UpdatedPerformingFacId, String pyrAbbrv, String jurisPyrAbbrev) throws Exception {
		logger.info("======== Testing - PFER_339 ========");

		timeStamp  = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		ptDemo  = new PatientDemographics(driver, wait);
		xifinAdminUtils = new XifinAdminUtils(driver, config);

		String sysDt = timeStamp.getCurrentDate();
		daoManagerPlatform.setValuesFromTable("pt_demo_chk", "PROCESSED_DT = to_date('" + sysDt + "', 'MM/dd/yyyy'), STATUS = 1", "STATUS in (0, 2)", testDb);

		//Pre-condition: Make sure that
		//update PYR_REQMNTS set PATTERN_OVRRDE = '.*' where PK_FLD_ID = 4;

		logger.info("*** Step 2 Actions: - Create a new Priced Accession with new EPI");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL), null);
    	logger.info("        AccnID: " + accnId);

    	logger.info("*** Step 2 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
    	String pyrAbbrev = accessionDao.getAccnPyrFromAccnByAccnId(accnId).getPyrAbbrv();

    	logger.info("*** Step 3 Actions: - Turn on System_Setting 1110 (Jurisdiction Logic) in DB");
    	daoManagerXifinRpm.setSysSettings(1, 1110, testDb);

    	logger.info("*** Step 4 Actions: - Setup test data in PYR_FAC_JURIS_PYR table in DB");
    	Accn accnInfoList = accessionDao.getAccn(accnId);
    	int ptSeqId = accnInfoList.getPtSeqId();
    	int clnId = accnInfoList.getClnId();
    	//Update CLN.FK_ORDERING_FAC_ID = 1
    	int origPerformingFacId = clientDao.getClnByClnId(clnId).getOrderingFacId();
    	daoManagerPlatform.setValuesFromTableByColNameValue("CLN", "FK_ORDERING_FAC_ID = 1", "pk_cln_id ", String.valueOf(clnId), testDb);

    	int pyrId = payorDao.getPyrByPyrAbbrev(pyrAbbrv).getPyrId();
    	int jurisPyrId = payorDao.getPyrByPyrAbbrev(jurisPyrAbbrev).getPyrId();
		Assert.assertTrue(payorDao.insertFacJurisPyrFromPyrFacJurisPyr(UpdatedPerformingFacId, pyrId, jurisPyrId) > 0, "        A new record should be inserted into PYR_FAC_JURIS_PYR table.");

		logger.info("*** Step 5 Actions: - Update the Patient (EPI) Info on the Accession and update the Patient Payor to be the payor in the PYR_FAC_JURIS_PYR table in RPM Patient Demographics");
		switchToDefaultWinFromFrame();
		navigation.navigateToPatientDemographicsPage();
		switchToPopupWin();

		String epi   = patientDao.getPtBySeqId(ptSeqId).getEpi();
		String randomVal = randomCharacter.getRandomAlphaString(8);

		ptDemo.setEPI(epi);
		String ssn = "5" + randomCharacter.getNonZeroRandomNumericString(2) + randomCharacter.getNonZeroRandomNumericString(2) + "7" + randomCharacter.getNonZeroRandomNumericString(3);
		ptDemo.setPtSSN(ssn);
		ptDemo.setPtLastName("LN" + randomVal);
		ptDemo.setPtFirstName("FN" + randomVal);
		ptDemo.setPtAddr1("ADDR1" + randomVal);
		ptDemo.setPtAddr2("ADDR2" + randomVal);
		ptDemo.setPtComments("Pt Comments: " + randomVal);
		ptDemo.setPayorId(pyrAbbrv);
		Thread.sleep(5000);
		ptDemo.checkSuspendReason(isSuspend);
		ptDemo.submitBtn().click();
		Thread.sleep(7000);

		logger.info("*** Step 5 Expected Results: - Verify that data are in PT, PT_PYR, PT_PYR_SUSPEND_REASON_DT and PT_DEMO_CHK tables in DB");
		Pt ptInfo = patientDao.getSuspPtByEpiAndPyrAbbrev(epi, pyrAbbrev);
		Assert.assertNotNull(ptInfo, "        EPI = " + epi + " should be in the Patient Demo related tables");

		logger.info("*** Step 6 Actions: - Update PT_DEMO_CHK in DB so the engine can process");
	    //Set status = 2 (Retry), eff_dt to match the pt_pyr.eff_dt in pt_demo_chk
	    String currDt = timeStamp.getCurrentDate();
	    String dtStr = "to_date('" + currDt + "', 'MM/dd/yyyy') ";
	    daoManagerPlatform.setValuesFromTableByColNameValue("PT_DEMO_CHK","status = 2, EFF_DT = " + dtStr, "FK_PT_SEQ_ID", String.valueOf(ptSeqId), testDb);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date convertedCurrDt = new java.sql.Date(df.parse(timeStamp.getCurrentDate("yyyy-MM-dd")).getTime());

		logger.info("*** Step 6 Expected Results: - Verify that the Data are available for testing");
		PtDemoChk ptDemoChkInfoList = patientDao.getUnprocessedPtDemoChkBySeqIdAndStatus(ptSeqId,2);
		int status = ptDemoChkInfoList.getStatus();
		java.sql.Date effDt = ptDemoChkInfoList.getEffDt();
		Assert.assertEquals(status, 2, "        Status = 2 in PT_Demo_Chk.");
		Assert.assertEquals(effDt, convertedCurrDt, "        EffDT is " + currDt + " in PT_Demo_Chk.") ;

		logger.info("*** Step 7 Actions: - Clear System Cache");
		xifinAdminUtils.clearDataCache();
		Thread.sleep(3000);

		logger.info("*** Step 8 Actions: - Run PF - Pt Demo Sweeper Engine");
		isProcessedByPtDemoSweeper(ptDemoChkInfoList.getPtSeqId(),QUEUE_WAIT_TIME_MS);
		Thread.sleep(5000);

		logger.info("*** Step 8 Expected Results: - Verify that the Pt Demo information got updated in the ACCN table and PT View in DB");
		ptDemoChkInfoList = patientDao.getProcessedPtDemoChkBySeqIdAndStatus(ptSeqId,1);
		int processedAccn = accessionDao.getAccnByPtSeqId(ptSeqId);

		Assert.assertEquals(ptDemoChkInfoList.getProcessedDt(),  convertedCurrDt, "        Processed date should be "+currDt+".");
		Assert.assertEquals(ptDemoChkInfoList.getNumAccnsProcessed(),  processedAccn, "        Processed Accn should be "+processedAccn+".");

		//Accn table: update PT address with data is get from PT table
		Accn accnPtDemoInfoList = accessionDao.getAccnFromAccnAndCountryByAccnId(accnId);
		Pt ptInfoFromPT = patientDao.getPtBySeqId(ptSeqId);

		Assert.assertEquals(accnPtDemoInfoList.getPtFNm(),  ptInfoFromPT.getPtFNm(), "        pt_f_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtLNm(),  ptInfoFromPT.getPtLNm(), "        pt_l_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr1(),  ptInfoFromPT.getPtAddr1(), "        pt_addr1 is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr2(),  ptInfoFromPT.getPtAddr2(), "        pt_addr2 is update in Accn.");

		logger.info("*** Step 8 Expected Results: - Verify that the Payor is updated and the Jurisdiction Payor is added in the ACCN_PYR table in DB");
		Assert.assertNotNull(accessionDao.getAccnPyrByAccnIdPyrIdPyrPrio(accnId, pyrId,1),  "        The primary Payor " + pyrId + " on the Accession " + accnId + " should be updated to " + pyrId);
 		Assert.assertNotNull(accessionDao.getAccnPyrByAccnIdPyrIdPyrPrio(accnId, jurisPyrId,2),  "        The Jurisdiction Payor " + jurisPyrId + "  on the Accession " + accnId + " should be added.");

		logger.info("*** Step 8 Expected Results: - Verify that the Accession is backout pricing in DB");
		Assert.assertNotEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        Accession " + accnId + " should be back out pricing.");

		logger.info("*** Step 9 Actions: - Clear test data");
		daoManagerPlatform.setValuesFromTableByColNameValue("CLN", "FK_ORDERING_FAC_ID = " + origPerformingFacId, "pk_cln_id ", String.valueOf(clnId), testDb);
		xifinAdminUtils.clearDataCache();

	}


	@Test(priority = 1, description = "Status=0, FinalReported Accn, Non-Suspend Pt, Validate Accn")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "isSuspend"})
	public void testPFER_347(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, boolean isSuspend) throws Exception {
		logger.info("======== Testing - PFER_347 ========");

		timeStamp  = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		ptDemo  = new PatientDemographics(driver, wait);
		xifinAdminUtils = new XifinAdminUtils(driver, config);

		String sysDt = timeStamp.getCurrentDate();
		daoManagerPlatform.setValuesFromTable("pt_demo_chk", "PROCESSED_DT = to_date('" + sysDt + "', 'MM/dd/yyyy'), STATUS = 1", "STATUS in (0, 2)", testDb);

		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession with UNKPYR via AccnWS");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL), null);
    	logger.info("        AccnID: " + accnId);

    	logger.info("*** Step 2 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

    	Accn accnInfoList = accessionDao.getAccn(accnId);
    	int ptSeqId = accnInfoList.getPtSeqId();

		logger.info("*** Step 3 Actions: - Update the Patient (EPI) Info on the Accession in RPM Patient Demographics");
		switchToDefaultWinFromFrame();
		navigation.navigateToPatientDemographicsPage();
		switchToPopupWin();

		String epi   = patientDao.getPtBySeqId(ptSeqId).getEpi();
		String randomVal = randomCharacter.getRandomAlphaString(8);
		int pyrId = 4;
		String pyrAbbrevInPtDemo = payorDao.getPyrByPyrId(pyrId).getPyrAbbrv();
		String subsId = randomCharacter.getNonZeroRandomNumericString(9);
		ptDemo.setEPI(epi);
		String ssn = "5" + randomCharacter.getNonZeroRandomNumericString(2) + randomCharacter.getNonZeroRandomNumericString(2) + "7" + randomCharacter.getNonZeroRandomNumericString(3);
		ptDemo.setPtSSN(ssn);
		ptDemo.setPtLastName("LN" + randomVal);
		ptDemo.setPtFirstName("FN" + randomVal);
		ptDemo.setPtAddr1("ADDR1" + randomVal);
		ptDemo.setPtAddr2("ADDR2" + randomVal);
		ptDemo.setPtComments("Pt Comments: " + randomVal);
		ptDemo.setPayorId(pyrAbbrevInPtDemo);
		ptDemo.setInsSubsId(subsId);
		Thread.sleep(5000);
		ptDemo.checkSuspendReason(isSuspend);
		ptDemo.submitBtn().click();
		Thread.sleep(5000);

		logger.info("*** Step 3 Expected Results: - Verify that data are added in PT, PT_PYR and PT_DEMO_CHK tables in DB");
		PtDemoChk ptInfo = patientDao.getNonSuspPtByEpiAndPyrAbbrev(epi, pyrAbbrevInPtDemo);
		Assert.assertNotNull(ptInfo, "        EPI = " + epi + " should be added to the Patient Demo related tables");

		logger.info("*** Step 3 Expected Results: - Verify that the Data are available for testing");
		PtDemoChk ptDemoChkInfoList = patientDao.getUnprocessedPtDemoChkBySeqIdAndStatus(ptSeqId,0);
		int status = ptDemoChkInfoList.getStatus();
		java.sql.Date effDt = ptDemoChkInfoList.getEffDt();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date currDt = new java.sql.Date(df.parse(timeStamp.getCurrentDate("yyyy-MM-dd")).getTime());
		Assert.assertEquals(status, 0, "        Status = 0 in PT_Demo_Chk for dk_pt_seq_id = " + ptSeqId);
		Assert.assertEquals(effDt, currDt, "        Eff_DT should be " + currDt + " in PT_Demo_Chk table for dk_pt_seq_id = " + ptSeqId);

		logger.info("*** Step 4 Actions: - Run PF - Pt Demo Sweeper Engine");
		isProcessedByPtDemoSweeper(ptDemoChkInfoList.getPtSeqId(),QUEUE_WAIT_TIME_MS);
    	Thread.sleep(5000);

		logger.info("*** Step 4 Expected Results: - Verify that the record got processed properly in PT_DEMO_CHK table in DB");
		ptDemoChkInfoList = patientDao.getProcessedPtDemoChkBySeqIdAndStatus(ptSeqId,1);
		int processedAccn = accessionDao.getAccnByPtSeqId(Integer.parseInt(String.valueOf(ptSeqId)));

		Assert.assertEquals(ptDemoChkInfoList.getProcessedDt(),  currDt, "        Processed date should be "+currDt+".");
		Assert.assertEquals(ptDemoChkInfoList.getNumAccnsProcessed(),  processedAccn, "        Processed Accn should be "+processedAccn+".");

		logger.info("*** Step 4 Expected Results: - Verify that the Pt Demo information got updated in the ACCN table and PT View in DB");
		//Accn table: update PT address with data is get from PT table
		Accn accnPtDemoInfoList = accessionDao.getAccnFromAccnAndCountryByAccnId(accnId);
		Pt ptInfoFromPT = patientDao.getPtBySeqId(ptSeqId);

		Assert.assertEquals(accnPtDemoInfoList.getPtFNm(),  ptInfoFromPT.getPtFNm(), "        pt_f_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtLNm(),  ptInfoFromPT.getPtLNm(), "        pt_l_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr1(),  ptInfoFromPT.getPtAddr1(), "        pt_addr1 is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr2(),  ptInfoFromPT.getPtAddr2(), "        pt_addr2 is update in Accn.");

		logger.info("*** Step 4 Expected Results: - Verify that the Payor and subscriber ID are updated in the ACCN_PYR table in DB");
		AccnPyr accnPyrInfoList = accessionDao.getAccnPyrByAccnIdPyrIdPyrPrio(accnId, pyrId, 1);
		Assert.assertNotNull(accnPyrInfoList,  "        The primary Payor " + pyrId + " on the Accession " + accnId + " should be updated to " + pyrId);
		Assert.assertEquals(accnPyrInfoList.getSubsId(), subsId, "        Subscriber ID " + subsId + " should be added to ACCN_PYR for Accession " + accnId + " Payor " + pyrAbbrevInPtDemo);

		logger.info("*** Step 4 Expected Results: - Verify that the ACCN_PYR_ERR was removed in DB");
		AccnPyrErr accnPyrErrInfoList = errorProcessingDao.getAccnPyrErrByAccnIdErrCdAndErrDt(accnId, 2136, currDt);
		Assert.assertEquals(accnPyrErrInfoList.getPyrPrio(), 0,  "        ACCN_PYR_ERR 2136 (Subscriber id invalid/missing) pyr_prio = 0 for Accession " + accnId);

		logger.info("*** Step 4 Expected Results: - Verify that the ACCN_ERR was fixed in DB");
		AccnErr accnErrInfoList = accessionDao.getAccnErrByAccnIdErrDtAndErrCd(accnId,  currDt,1111);
		java.sql.Date fixDt = accnErrInfoList.getFixDt();
		Assert.assertEquals(fixDt, currDt, "        ACCN_ERR 1111 (Unknown Payor) should be fixed for Accession " + accnId);

	}

	@Test(priority = 1, description = "Status = 0, Priced Accn, Resubmit")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "isSuspend"})
	public void testPFER_334(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, boolean isSuspend) throws Exception {
		logger.info("========Testing - PFER_334========");

		timeStamp  = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		ptDemo  = new PatientDemographics(driver, wait);
		xifinAdminUtils = new XifinAdminUtils(driver, config);

		//Set all others to be processed so the engine will only pick up specified record from pt_demo_chk
		String sysDt = timeStamp.getCurrentDate();
		daoManagerPlatform.setValuesFromTable("pt_demo_chk", "PROCESSED_DT = to_date('" + sysDt + "', 'MM/dd/yyyy'), STATUS = 1", "STATUS in (0, 2)", testDb);

		logger.info("*** Step 2 Actions: - Create a new Priced Accession with new EPI");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL), null);
    	logger.info("        AccnID: " + accnId);

    	logger.info("*** Step 2 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
    	String pyrAbbrev = accessionDao.getAccnPyrFromAccnByAccnId(accnId).getPyrAbbrv();

    	Accn accnInfoList = accessionDao.getAccn(accnId);
    	int ptSeqId = accnInfoList.getPtSeqId();

    	logger.info("*** Step 3 Actions: - Load the accession in XP-Detail screen");
		switchToDefaultWinFromFrame();
		navigation.navigateToAccnDetailPage();
		accessionDetail = new AccessionDetail(driver, config, wait);

		logger.info("*** Step 3 Expected Results: - Verify that the Accession is loaded properly in the Detail screen");
		Assert.assertTrue(isElementPresent(accessionDetail.accnIdInput(), 10));
		accessionDetail.setAccnId(accnId);
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId,wait));

		logger.info("*** Step 4 Actions - Select a proc code in the Billable Proc Code Detail grid");
		AccnProc billableProcCodesList = accessionDao.getAccnProcByAccnId(accnId);
		String procId = billableProcCodesList.getProcId();

		//Click the selected proc code row
		int rowNum = accessionDetail.getRowNumberInWebTable(accessionDetail.billableProcCodeDetailsTable(), "tbl_billableProcedureCodeDetails", procId, 8);
		clickHiddenPageObject(accessionDetail.billableProcCodeDetailsText(rowNum, 1),0);

		logger.info("*** Step 5 Actions - Click the index column and select Edit Row hyperlink");
		accessionDetail.billableProcCodeDetailsEditRowLnk(rowNum-1);

		logger.info("*** Step 5 Expected Results: - Verify that Payor ID dropdown list displays in the Edit Record window");
		recordPopUp = new RecordPopUp(driver,wait);
		Assert.assertTrue(recordPopUp.pyrIdDropDown().isDisplayed(), "        Payor ID dropdown should present.");

		logger.info("*** Step 6 Actions - Select 'P' Payor from the Payor ID drop down list and click OK");
		String ptPyrAbbrev = payorDao.getPyrByPyrId(4).getPyrAbbrv();
		selectItemByVal(recordPopUp.pyrIdDropDown(), ptPyrAbbrev + " | 2");
		recordPopUp.clickOK();

		logger.info("*** Step 6 Expected Results - Verify that the P is showing in the UI");
		Assert.assertNotEquals(accessionDetail.getRowNumberInWebTable(accessionDetail.billableProcCodeDetailsTable(), "tbl_billableProcedureCodeDetails", procId, ptPyrAbbrev, 8, 50 ), 0);

		logger.info("*** Step 7 Actions - Click Save button");
		accessionDetail.clickSave();

		logger.info("*** Step 8 Actions - Enter P payor Subscriber Id and Relationship in Insurance Info grid");
		clickHiddenPageObject(accessionDetail.secondaryPyrTab(),0);

		String ptPyrSubsId = randomCharacter.getRandomNumericString(9);
		accessionDetail.setSecondaryPayorSubsId(ptPyrSubsId);

		accessionDetail.setsecondaryPayorRelationship("1"); // 1 = Self

		logger.info("*** Step 9 Actions - Click Save button. Choose to update Patient Insurance Info and click OK button");
		accessionDetail.clickSave();
		accessionDetail.saveAndClearBtn();

		logger.info("*** Step 9 Expected Results - Verify that the procedure code should be changed to pointing to the 2nd P payor in Q_ACCN_SUBM table in DB");
		QAccnSubm qAccnSubmInfoList = submissionDao.getQAccnSubmsByAccnIdAndPyrPrio(accnId, 2);
		Assert.assertEquals(qAccnSubmInfoList.getPyrId(), 4, "        The procedure code should be changed to pointing to 2nd P payor.");

		logger.info("*** Step 9 Expected Results - Verify that the procedure code that was changed to pointing to the 2nd P payor should still be Priced (21)");
		List<AccnProc> accnProcInfoList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procId);
		Assert.assertEquals(accnProcInfoList.get(0).getSubmPyrPrio(), 2, "        The procedure code " + procId + " should be changed to pointing to 2nd payor.");
		Assert.assertEquals(accnProcInfoList.get(0).getStaId(), 21, "        The procedure code " + procId + " status should be 21 (Priced).");

		logger.info("*** Step 9 Expected Results - Verify that P payor should be added to the ACCN_PYR table as 2nd payor");
		AccnPyr accnPyrInfoList = accessionDao.getAccnPyrByAccnIdPyrIdPyrPrio(accnId, 4,2);
		Assert.assertEquals(accnPyrInfoList.getSubsId(), ptPyrSubsId, "        The Subscriber ID for P Payor  should be " + ptPyrSubsId);

		logger.info("*** Step 9 Expected Results - Verify that the Accession should still be Priced (21)");
		accnInfoList = accessionDao.getAccn(accnId);
		Assert.assertEquals(accnInfoList.getStaId(), 21, "        The Accession " + accnId + " status should be 21 (Priced).");

		accessionDetail.clickReset();

		logger.info("*** Step 10 Actions: - Update the Patient (EPI) Info and 2nd payor on the Accession in RPM Patient Demographics");
		switchToDefaultWinFromFrame();
		navigation.navigateToPatientDemographicsPage();
		logger.info("Clicked Patient Demographics Link");
		if(isAlertPresent()){
			closeAlertAndGetItsText(true);
		}
		switchToPopupWin();

		String epi   = patientDao.getPtBySeqId(ptSeqId).getEpi();
		String randomVal = randomCharacter.getRandomAlphaString(8);
		String pyrAbbrevInPtDemo = payorDao.getPyrFromPyrDtAndPyrByGrpId(10).getPyrAbbrv();

    	//Disable and delete the pyr field requirement
    	daoManagerAccnWS.setBDiagReqBSpcCDReqByPyrAbrrev(pyrAbbrevInPtDemo, testDb);
    	payorDao.deleteFromPyrReqmntsFldByPyrAbbrev(pyrAbbrevInPtDemo);
    	payorDao.deleteFromPyrReqmntsByPyrAbbrev(pyrAbbrevInPtDemo);

		ptDemo.setEPI(epi);

		clickHiddenPageObject(ptDemo.pyrTab("2"), 0);
		driver.executeScript("arguments[0].scrollIntoView(true);", ptDemo.pyrIdInput("1"));
		ptDemo.setPayorId(pyrAbbrevInPtDemo, "1");//Set 2nd Payor ID
		ptDemo.setInsSubsId(randomVal, "1");
		Thread.sleep(5000);
		ptDemo.checkSuspendReason(isSuspend);
		ptDemo.submitBtn().click();
		Thread.sleep(5000);

		logger.info("*** Step 10 Expected Results: - Verify that data are in PT, PT_PYR, PT_PYR_SUSPEND_REASON_DT and PT_DEMO_CHK tables in DB");
		Pt ptInfo = patientDao.getSuspPtByEpiAndPyrAbbrev(epi, pyrAbbrevInPtDemo);
		Assert.assertNotNull(ptInfo, "        EPI = " + epi + " should be in the Patient Demo related tables");

		logger.info("*** Step 11 Actions: - Update PT_DEMO_CHK in DB so the engine can process");
	    //Set status = 0 (Unprocessed), eff_dt to match the pt_pyr.eff_dt in pt_demo_chk
	    String currDt = timeStamp.getCurrentDate();
	    String dtStr = "to_date('" + currDt + "', 'MM/dd/yyyy') ";
	    daoManagerPlatform.setValuesFromTableByColNameValue("PT_DEMO_CHK","status = 0, EFF_DT = " + dtStr, "FK_PT_SEQ_ID", String.valueOf(ptSeqId), testDb);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date convertedCurrDt = new java.sql.Date(df.parse(timeStamp.getCurrentDate("yyyy-MM-dd")).getTime());

		logger.info("*** Step 11 Expected Results: - Verify that the Data are available for testing");
		PtDemoChk ptDemoChkInfoList = patientDao.getUnprocessedPtDemoChkBySeqIdAndStatus(ptSeqId,0);
		int status = ptDemoChkInfoList.getStatus();
		java.sql.Date effDt = ptDemoChkInfoList.getEffDt();
		Assert.assertEquals(status, 0, "        Status = 0 in PT_Demo_Chk.");
		Assert.assertEquals(effDt, convertedCurrDt, "        EffDT is " + currDt + " in PT_Demo_Chk.") ;

		logger.info("*** Step 12 Actions: - Run PF - Pt Demo Sweeper Engine");
		isProcessedByPtDemoSweeper(ptDemoChkInfoList.getPtSeqId(),QUEUE_WAIT_TIME_MS);
    	Thread.sleep(5000);

		logger.info("*** Step 12 Expected Results: - Verify that the Pt Demo information got updated in the ACCN table and PT View in DB");
		Thread.sleep(5000);
		ptDemoChkInfoList = patientDao.getProcessedPtDemoChkBySeqIdAndStatus(ptSeqId,1);
		int processedAccn = accessionDao.getAccnByPtSeqId(ptSeqId);

		Assert.assertEquals(ptDemoChkInfoList.getProcessedDt(),  convertedCurrDt, "        Processed date should be "+currDt+".");
		Assert.assertEquals(ptDemoChkInfoList.getNumAccnsProcessed(),  processedAccn, "        Processed Accn should be "+processedAccn+".");

		logger.info("*** Step 12 Expected Results: - Verify that the 2nd Payor is updated in the ACCN_PYR table in DB");
		int newPyrId = payorDao.getPyrByPyrAbbrev(pyrAbbrevInPtDemo).getPyrId();
		accnPyrInfoList = accessionDao.getAccnPyrByAccnIdPyrIdPyrPrio(accnId, newPyrId, 2);
		Assert.assertEquals(accnPyrInfoList.getSubsId(), randomVal, "        The Subscriber ID for Payor " + pyrAbbrevInPtDemo + " should be " + randomVal);

		logger.info("*** Step 12 Expected Results - Verify that the Accession should still be Priced (21)");
		accnInfoList = accessionDao.getAccn(accnId);
		Assert.assertEquals(accnInfoList.getStaId(), 21, "        The Accession " + accnId + " status should be 21 (Priced).");

		logger.info("*** Step 12 Expected Results - Verify that the procedure code should be changed to pointing to the new 2nd payor in Q_ACCN_SUBM table in DB");
		qAccnSubmInfoList = submissionDao.getQAccnSubmsByAccnIdAndPyrPrio(accnId, 2);
		Assert.assertEquals(qAccnSubmInfoList.getPyrId(), newPyrId, "        The procedure code " + procId + " should be changed to pointing to 2nd payor" + pyrAbbrevInPtDemo);

	}
// commented this test case as per Chava's guidance - If this test case is failing on previous prod builds , you can comment out the test.
//	@Test(priority = 1, description = "Status=2, Priced Accn, Suspend Pt, Pt got unSuspended and Accn is in Q_ACCN_SUBM")
//	@Parameters({"email", "password", "project", "testSuite", "testCase", "isSuspend"})
//	public void testPFER_353(String email, String password, String project, String testSuite, String testCase, boolean isSuspend) throws Exception {
//		logger.info("========Testing - PFER_353========");
//		TimeStamp timeStamp  = new TimeStamp(driver);
//		PatientDemographics ptDemo  = new PatientDemographics(driver, wait);
//		randomCharacter = new RandomCharacter(driver);
//		//Set all others to be processed so the engine will only pick up specified record from pt_demo_chk
//		String sysDt = timeStamp.getCurrentDate();
//		daoManagerPlatform.setValuesFromTable("pt_demo_chk", "PROCESSED_DT = to_date('" + sysDt + "', 'MM/dd/yyyy'), STATUS = 1", "STATUS in (0, 2)", testDb);
//
//
//		logger.info("*** Step 1 Actions: - Create a new Priced Patient Payor Accession with new EPI");
//        Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
//        String accnId = resultProperties.getProperty("NewAccnID");
//        logger.info("        AccnID: " + accnId);
//
//        Assert.assertTrue(isOutOfQFrPendingQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of FR Pending Queue");
//        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");
//
//        logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
//    	Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");
//    	String pyrAbbrev = daoManagerXifinRpm.getPyrInfoFromACCNPYRByAccnId(accnId, testDb).get(2);
//
//    	ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
//    	String ptSeqId = accnInfoList.get(43);
//
//		logger.info("*** Step 2 Actions: - Load the EPI and suspend Patient in XP Patient Demographics and submit");
//		switchToDefaultWinFromFrame();
//		navigation.navigateToPatientDemographicsPage();
//		switchToPopupWin();
//		String winHandler = driver.getWindowHandle();
//
//		String epi   = daoManagerPlatform.getPtDemoInfoFromPTByPtSeqId(ptSeqId, testDb).get(0);
//		String randomVal = randomCharacter.getRandomAlphaString(8);
//
//		ptDemo.setEPI(epi);
//		logger.info("set EPI Done");
//		ptDemo.checkSuspendReason(isSuspend);
//		logger.info("set suspend done");
//		ptDemo.submitBtn().click();
//
//		logger.info("*** Step 2 Expected Results: - Verify that data are in PT_V2, PT_PYR_V2, PT_PYR_SUSPEND_REASON_DT and PT_DEMO_CHK tables in DB");
//		List<String> ptInfo = daoManagerPlatform.getSuspPtByEpiAndPyrAbbrev(epi, pyrAbbrev, testDb);
//		Assert.assertTrue(ptInfo.size() > 0, "        EPI = " + epi + " should be in the Patient Demo related tables.");
//
//		logger.info("*** Step 3 Actions: - Update PT_DEMO_CHK in DB so the engine can process");
//	    String currDt = timeStamp.getCurrentDate();
//	    String dtStr = "to_date('" + currDt + "', 'MM/dd/yyyy') ";
//	    daoManagerPlatform.setValuesFromTableByColNameValue("PT_DEMO_CHK","status = 2,NUM_ACCNS_PROCESSED = 0, EFF_DT = " + dtStr, "FK_PT_SEQ_ID", ptSeqId, testDb);
//
//		logger.info("*** Step 3 Expected Results: - Verify that the Data are available for testing");
//		List<String> ptDemoChkInfoList = daoManagerPlatform.getUnProcessedPtDemoChkInfoFromPTDEMOCHKByPtSeqIdStatus(ptSeqId, "2", testDb);
//		int status = Integer.parseInt(ptDemoChkInfoList.get(0));
//		String effDt = ptDemoChkInfoList.get(4);
//		Assert.assertTrue(status == 2,"        Status = 2 in PT_Demo_Chk.") ;
//		Assert.assertEquals(effDt, currDt, "        EffDT is " + currDt + " in PT_Demo_Chk.") ;
//
//		logger.info("*** Step 4 Expected Results: - Verify that Pt Demo record got processed in PT_DEMO_CHK table in DB");
//		Assert.assertTrue(isProcessedByPtDemoSweeper(Integer.parseInt(ptSeqId), QUEUE_WAIT_TIME_MS), "Patient Demo Check Is Not Processed");
//
//		ptDemoChkInfoList = daoManagerPlatform.getProcessedPtDemoChkInfoFromPTDEMOCHKByPtSeqIdStatus(ptSeqId, "1", testDb);
//		String processedAccn = String.valueOf(daoManagerPlatform.getAccnIdsFromACCNByPtSeqId(ptSeqId, testDb).size());
//
//		Assert.assertEquals(ptDemoChkInfoList.get(1),  currDt, "        Processed date should be "+currDt+".");
//		Assert.assertEquals(ptDemoChkInfoList.get(2),  processedAccn, "        Processed Accn should be "+processedAccn+".");
//
//		logger.info("*** Step 4 Expected Results: - Verify that the Accession has ADDRESS (Pt Suspend, err_cd = 2000) error in ACCN_PYR_ERR table in DB");
//		ArrayList<String> accnPyrErrInfoList = daoManagerPlatform.getAccnPyrErrInfoFromACCNPYRERRByAccnIdErrCdErrDt(accnId, "2000", currDt, testDb);
//		Assert.assertTrue(accnPyrErrInfoList.size() > 0, "        The Accession " + accnId + " should have ADDRESS (Pt Suspend, err_cd = 2000) error in ACCN_PYR_ERR table.");
//
//		logger.info("*** Step 4 Expected Results - Verify that the Accession should still be Priced");
//		accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
//		Assert.assertEquals(accnInfoList.get(2), "21", "        The Accession " + accnId + " status should be 21 (Priced).");
//
//		logger.info("*** Step 4 Expected Results - Verify that the Accession is not in Q_ACCN_SUBM table in DB");
//		List<String> qAccnSubmInfoList = daoManagerXifinRpm.getQAccnSubmInfoFromQACCNSUBMByAccnIdPyrPrio(accnId, "1", testDb);
//		Assert.assertTrue(qAccnSubmInfoList.isEmpty(), "        The Accession " + accnId + " should NOT in Q_ACCN_SUBM table.");
//
//        switchToWin(winHandler);
//        switchToDefaultWinFromFrame();
//
//        logger.info("*** Step 5 Actions: - Load the same EPI in RPM Patient Demo screen and un-suspend the patient and submit");
//
//		switchToDefaultWinFromFrame();
//		navigation.navigateToPatientDemographicsPage();
//		switchToPopupWin();
//		//String winHandler = driver.getWindowHandle();
//
//		ptDemo.setEPI(epi);
//		logger.info("set EPI Done");
//		Thread.sleep(5000);
//		ptDemo.setPtAddr1("Updated ADDR1" + randomVal);
//		ptDemo.setUnSuspendPt();
//		ptDemo.submitBtn().click();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 5 Expected Results: - Verify that the Data are available for testing");
//		ptDemoChkInfoList = daoManagerPlatform.getUnProcessedPtDemoChkInfoFromPTDEMOCHKByPtSeqIdStatus(ptSeqId, "0", testDb);
//		status = Integer.parseInt(ptDemoChkInfoList.get(0));
//		effDt = ptDemoChkInfoList.get(4);
//		Assert.assertTrue(status == 0,"        Status = 0 in PT_Demo_Chk.") ;
//		Assert.assertEquals(effDt, currDt, "        EffDT is " + currDt + " in PT_Demo_Chk.") ;
//
//		Assert.assertTrue(isProcessedByPtDemoSweeper(Integer.parseInt(ptSeqId), QUEUE_WAIT_TIME_MS), "Patient Demo Check Is Not Processed");
//
//		logger.info("*** Step 6 Expected Results: - Verify that the Accession has ADDRESS (Pt Suspend, err_cd = 2000) error is fixed in ACCN_PYR_ERR table in DB");
//		accnPyrErrInfoList = daoManagerPlatform.getAccnPyrErrInfoFromACCNPYRERRByAccnIdErrCdErrDt(accnId, "2000", currDt, testDb);
//		Assert.assertEquals(accnPyrErrInfoList.get(1), currDt, "        The Accession " + accnId + " ADDRESS (Pt Suspend, err_cd = 2000) error should be fixed in ACCN_PYR_ERR table.");
//
//		logger.info("*** Step 6 Expected Results - Verify that the Accession should still be Priced");
//		accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
//		Assert.assertEquals(accnInfoList.get(2), "21", "        The Accession " + accnId + " status should be 21 (Priced).");
//
//		logger.info("*** Step 6 Expected Results - Verify that the Accession is in Q_ACCN_SUBM table in DB");
//		qAccnSubmInfoList = daoManagerXifinRpm.getQAccnSubmInfoFromQACCNSUBMByAccnIdPyrPrio(accnId, "1", testDb);
//		Assert.assertTrue(qAccnSubmInfoList.size() > 0, "        The Accession " + accnId + " should in Q_ACCN_SUBM table.");
//
//	}

	@Test(priority = 1, description = "Status=0, FinalReported Accn, Non-Suspend Pt, copy PT_DIAG to ACCN_DIAG")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "isSuspend"})
	public void testPFER_354(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, boolean isSuspend) throws Exception {
		logger.info("======== Testing - PFER_354 ========");

		timeStamp  = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		ptDemo  = new PatientDemographics(driver, wait);
		xifinAdminUtils = new XifinAdminUtils(driver, config);

		String sysDt = timeStamp.getCurrentDate();
		daoManagerPlatform.setValuesFromTable("pt_demo_chk", "PROCESSED_DT = to_date('" + sysDt + "', 'MM/dd/yyyy'), STATUS = 1", "STATUS in (0, 2)", testDb);

		//Pre-condition: Make sure that
		//update PYR_REQMNTS set PATTERN_OVRRDE = '.*' where PK_FLD_ID = 4;

		logger.info("*** Step 2 Actions: - Create a new Patient Payor Final Reported Accession with new EPI");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL), null);
    	logger.info("        AccnID: " + accnId);

    	logger.info("*** Step 2 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
    	String pyrAbbrev = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();

       	logger.info("*** Step 3 Actions: - Setup test data in CLN, PT_CLN_LNK and PT_DIAG tables in DB");
    	Accn accnInfoList = accessionDao.getAccn(accnId);
    	int ptSeqId = accnInfoList.getPtSeqId();
    	int clnId = accnInfoList.getClnId();
    	String ptClnId = "PTCLNLNK" + randomCharacter.getRandomAlphaNumericString(8);

    	//Set CLN.FK_ACCT_TYP_ID = 9 (NURSING HOME)
    	int clnCount = daoManagerPlatform.setValuesFromTableByColNameValue("CLN", "FK_ACCT_TYP_ID = 9", "pk_cln_id ", String.valueOf(clnId), testDb);
    	//Add a new record in pt_cln_lnk table
    	int ptClnLnkCount = daoManagerPlatform.addPtClnLnkFromPTCLNLNK(String.valueOf(ptSeqId), String.valueOf(clnId), ptClnId, "0", testDb);
    	//Add a new record in pt_diag table
    	PtClnLnk ptClnLnkInfoList = patientDao.getPtClnLnkByPtSeqIdClnId(ptSeqId,clnId);
    	int ptClnLnkseqId = ptClnLnkInfoList.getSeqId();
    	int diagCdTyp = accessionDao.getAccnDiagByAccnIdDiagSeq(accnId, 1).getDiagTypId();
    	String diagCdId = diagnosisCodeDao.getDiagCdByDiagTypId(diagCdTyp).getDiagCdId();
    	int ptDiagCount = daoManagerPlatform.addPtDiagFromPTDIAG(diagCdId, String.valueOf(ptClnLnkseqId), "2", testDb);

    	logger.info("*** Step 3 Expected Results: - Verify that the test data are available in DB");
    	Assert.assertTrue(clnCount > 0, "        CLN is not updated.");
//    	Assert.assertTrue(ptClnLnkCount > 0, "        PT_CLN_LNK is not updated.");
    	Assert.assertTrue(ptDiagCount > 0, "        PT_DIAG is not updated.");

		logger.info("*** Step 4 Actions: - Update the Patient (EPI) Info on the Accession in RPM Patient Demographics");
		switchToDefaultWinFromFrame();
		navigation.navigateToPatientDemographicsPage();
		switchToPopupWin();

		String epi   = patientDao.getPtBySeqId(ptSeqId).getEpi();
		String randomVal = randomCharacter.getRandomAlphaString(8);
		int pyrId = 4;
		String pyrAbbrevInPtDemo = payorDao.getPyrByPyrId(pyrId).getPyrAbbrv();
		String subsId = randomCharacter.getNonZeroRandomNumericString(9);
		ptDemo.setEPI(epi);
		String ssn = "5" + randomCharacter.getNonZeroRandomNumericString(2) + randomCharacter.getNonZeroRandomNumericString(2) + "7" + randomCharacter.getNonZeroRandomNumericString(3);
		ptDemo.setPtSSN(ssn);
		ptDemo.setPtLastName("LN" + randomVal);
		ptDemo.setPtFirstName("FN" + randomVal);
		ptDemo.setPtAddr1("ADDR1" + randomVal);
		ptDemo.setPtAddr2("ADDR2" + randomVal);
		ptDemo.setPtComments("Pt Comments: " + randomVal);
		ptDemo.setPayorId(pyrAbbrevInPtDemo);
		ptDemo.setInsSubsId(subsId);
		Thread.sleep(5000);
		ptDemo.checkSuspendReason(isSuspend);
		Thread.sleep(7000);
		ptDemo.submitBtn().click();
		Thread.sleep(7000);

		logger.info("*** Step 4 Expected Results: - Verify that data are added in PT, PT_PYR and PT_DEMO_CHK tables in DB");
		PtDemoChk ptInfo = patientDao.getNonSuspPtByEpiAndPyrAbbrev(epi, pyrAbbrevInPtDemo);
		Assert.assertNotNull(ptInfo, "        EPI = " + epi + " should be added to the Patient Demo related tables");

		logger.info("*** Step 4 Expected Results: - Verify that the Data are available for testing in DB");
		PtDemoChk ptDemoChkInfoList = patientDao.getUnprocessedPtDemoChkBySeqIdAndStatus(ptSeqId,0);
		int status = ptDemoChkInfoList.getStatus();
		java.sql.Date effDt = ptDemoChkInfoList.getEffDt();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date currDt = new java.sql.Date(df.parse(timeStamp.getCurrentDate("yyyy-MM-dd")).getTime());
		Assert.assertEquals(status, 0, "        Status = 0 in PT_Demo_Chk for dk_pt_seq_id = " + ptSeqId);
		Assert.assertEquals(effDt, currDt, "        Eff_DT should be " + currDt + " in PT_Demo_Chk table for dk_pt_seq_id = " + ptSeqId);

		logger.info("*** Step 5 Actions: - Clear system cache");
	   	xifinAdminUtils.clearDataCache();
    	Thread.sleep(5000);

    	logger.info("*** Step 6 Actions: - Run PF - Pt Demo Sweeper Engine");
		isProcessedByPtDemoSweeper(ptSeqId,QUEUE_WAIT_TIME_MS);
    	Thread.sleep(5000);

		logger.info("*** Step 6 Expected Results: - Verify that the record got processed properly in PT_DEMO_CHK table in DB");
		ptDemoChkInfoList = patientDao.getProcessedPtDemoChkBySeqIdAndStatus(ptSeqId,1);
		int processedAccn = accessionDao.getAccnByPtSeqId(ptSeqId);

		Assert.assertEquals(ptDemoChkInfoList.getProcessedDt(),  currDt, "        Processed date should be "+currDt+".");
		Assert.assertEquals(ptDemoChkInfoList.getNumAccnsProcessed(),  processedAccn, "        Processed Accn should be "+processedAccn+".");

		logger.info("*** Step 6 Expected Results: - Verify that the Pt Demo information got updated in the ACCN table and PT View in DB");
		//Accn table: update PT address with data is get from PT table
		Accn accnPtDemoInfoList = accessionDao.getAccnFromAccnAndCountryByAccnId(accnId);
		Pt ptInfoFromPT = patientDao.getPtBySeqId(ptSeqId);

		Assert.assertEquals(accnPtDemoInfoList.getPtFNm(),  ptInfoFromPT.getPtFNm(), "        pt_f_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtLNm(),  ptInfoFromPT.getPtLNm(), "        pt_l_nm is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr1(),  ptInfoFromPT.getPtAddr1(), "        pt_addr1 is update in Accn.");
		Assert.assertEquals(accnPtDemoInfoList.getPtAddr2(),  ptInfoFromPT.getPtAddr2(), "        pt_addr2 is update in Accn.");

		logger.info("*** Step 6 Expected Results: - Verify that the PT_DIAG was copied to ACCN_DIAG in DB");
		AccnDiag accnDiagInfoList = accessionDao.getAccnDiagByAccnIdDiagSeq(accnId, 2);
    	String accnDiagId = accnDiagInfoList.getDiagCdId();
    	Assert.assertEquals(accnDiagId, diagCdId, "        Diag CD: " + diagCdId + " in PT_DIAG should be copied to ACCN_DIAG for Accession " + accnId);

    	logger.info("*** Step 6 Expected Results: - Verify that 'Patient LT Diag' was added to the ACCN_DIAG.CMNT in DB");
    	String cmnt = accnDiagInfoList.getCmnt().trim();
    	Assert.assertEquals(cmnt, "Patient LT Diag", "        'Patient LT Diag' should be added to ACCN_DIAG.CMNT for Accession " + accnId);

    	logger.info("*** Step 7 Actions: - Clear test data");
    	patientDao.deletePtDiagByPtClnLnkSeqId(ptClnLnkseqId);
    	patientDao.deletePtClnLnkBySeqId(ptClnLnkseqId);

	}

	protected boolean isProcessedByPtDemoSweeper(int ptSeqId, long maxTime) throws InterruptedException, XifinDataAccessException
	{
		long startTime = System.currentTimeMillis();
		maxTime += startTime;
		boolean isProcessed;
		do
		{
			isProcessed = true;
			for (PtDemoChk ptDemoChk : patientDao.getPtDemoChksByPtSeqId(ptSeqId))
			{
				if (ptDemoChk.getProcessedDt() == null)
				{
					isProcessed = false;
				}
			}
			logger.info("Waiting for patient demo sweeper engine, ptSeqId=" + ptSeqId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME_MS);
		}
		while (!isProcessed && System.currentTimeMillis() < maxTime);
		return isProcessed;
	}


    protected boolean isOutOfPricingQueue(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = accessionDao.isInPricingQueue(accnId);
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInQueue = accessionDao.isInPricingQueue(accnId);
        }
        return !isInQueue;
    }

    protected boolean isOutOfQFrPendingQueue(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = accessionDao.isInQFrPendingQueue(accnId);
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInQueue = accessionDao.isInQFrPendingQueue(accnId);
        }
        return !isInQueue;
    }
}
