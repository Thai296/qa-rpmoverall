//package com.mars.tests;
//
///*
//import java.io.File;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//*/
//import org.testng.Assert;
//import org.testng.annotations.Parameters;
//import org.testng.annotations.Test;
//
////import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//import com.overall.accession.accessionNavigation.AccessionNavigation;
//import com.overall.accession.accessionProcessing.AccessionDemographics;
///*
//import com.rpm.accession.accessionProcessing.AccessionSearch;
//import com.rpm.accession.accessionProcessing.AccessionSingleStatement;
//import com.rpm.accession.accessionProcessing.AccessionTransactionDetail;
//import com.rpm.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
//import com.rpm.fileMaintenance.sysMgt.DataCacheConfiguration;
//import com.rpm.fileMaintenance.sysMgt.TaskScheduler;
//import com.rpm.fileMaintenance.sysMgt.TaskStatus;
//import com.rpm.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
//*/
//import com.overall.headerNavigation.HeaderNavigation;
////import com.rpm.help.Help;
//import com.overall.accession.orderProcessing.AccnTestUpdateOld;
////import com.rpm.payor.payorDemographics.GroupDemographics;
////import com.rpm.payor.payorDemographics.PricingConfig;
////import com.rpm.payor.payorNavigation.PayorNavigation;
////import com.rpm.accession.accessionProcessing.superSearch.SuperSearch;
////import com.rpm.accession.accessionProcessing.superSearch.SuperSearchResults;
//import com.overall.utils.AccessionUtils;
////import com.xifin.ssologin.SsoLogin;
//import com.xifin.utils.SeleniumBaseTest;
//import com.xifin.qa.config.PropertyMap;
////import com.xifin.utils.TestDataSetup;
//import com.xifin.utils.TimeStamp;
//
//
//
//public class RegressionAccessionOrderProcessingTest extends SeleniumBaseTest  {
//
////	private SsoLogin ssoLogin;
//	private HeaderNavigation headerNavigation;
////	private Help help;
//	private AccessionNavigation accessionNavigation;
////	private SuperSearch superSearch;
////	private SuperSearchResults superSearchResults;
//	private AccessionDemographics accessionDemographics;
////	private AccessionSingleStatement accessionStatement;
////	private AccessionTransactionDetail accessionTransactionDetail;
////	private AccessionSearch accessionSearchPopup;
//	private AccnTestUpdateOld accnTestUpdate;
//	/*
//	private TestDataSetup testDataSetup;
//	private FileMaintenanceNavigation fileMaintenaceNavigation;
//	private PayorNavigation payorNavigation;
//	private GroupDemographics groupDemographics;
//	private PricingConfig pricingConfig;
//	private DataCacheConfiguration dataCacheConfiguration;
//	private TaskScheduler taskScheduler;
//	private TaskStatus taskStatus;
//	private FileMaintencePatternDefinition fileMaintencePatternDefinition;
//	*/
//	private TimeStamp timeStamp;
//	private AccessionUtils accessionUtils;
//
//
//
//	@Test(priority = 1, description = "Enter in Final Report Date of Today (T)")
//	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
//	public void testRPM_419(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
//		logger.info("***** Testing - testRPM_419 *****");
//
//		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
//		String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
//
//		String winHandler = driver.getWindowHandle();
//
//		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
//		headerNavigation = new HeaderNavigation(driver, config);
//		headerNavigation.navigateToAccessionTab();
//		switchToPopupWin();
//
//		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
//		accessionDemographics = new AccessionDemographics(driver);
//		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");
//
//		logger.info("*** Step 18 Actions: - Navigate to Accn Test Update");
//		driver.close();
//		switchToWin(winHandler);
//		switchToDefaultWinFromFrame();
//		accessionNavigation = new AccessionNavigation(driver, config);
//		accessionNavigation.navigateToAccnTestUpdateLinkLink();
//		switchToPopupWin();
//
//		logger.info("*** Step 18 Expected Results: - The Accn Test Update page displays");
//		accnTestUpdate = new AccnTestUpdateOld(driver);
//		Assert.assertEquals(accnTestUpdate.pageTitleText().getText().trim(), "Accession - Test Update", "       Accession Update Test page title should show.");
//
//		logger.info("*** Step 19 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
//		accnTestUpdate.searchAccnId(accnId);
//
//		logger.info("*** Step 19 Expected Results: - Verify that the patient name is correct");
//		Assert.assertTrue(isElementPresent(accnTestUpdate.ptNameInput(),5));
//		Assert.assertEquals(accnTestUpdate.ptNameInput().getAttribute("value"), daoManagerPatientPortal.getLnameByAccnId(accnId, testDb) + ", "+daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
//
//		logger.info("*** Step 20 Actions: - In the Final Report Date column enter in a future date");
//		accnTestUpdate = new AccnTestUpdateOld(driver);
//		timeStamp = new TimeStamp();
//		accnTestUpdate.setFinalRptDt(timeStamp.getCurrentDate());
//
//		logger.info("*** Step 20 Expected Results: - No pop-up messaging appears");
//		Assert.assertFalse(isAlertPresent());
//	}
//
//	@Test(priority = 1, description = "Enter in Final Report Date in the past")
//	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
//	public void testRPM_420(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
//		logger.info("***** Testing - testRPM_420 *****");
//
//		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
//		String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
//
//		String winHandler = driver.getWindowHandle();
//
//		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
//		headerNavigation = new HeaderNavigation(driver, config);
//		headerNavigation.navigateToAccessionTab();
//		switchToPopupWin();
//
//		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
//		accessionDemographics = new AccessionDemographics(driver);
//		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");
//
//		logger.info("*** Step 18 Actions: - Navigate to Accn Test Update");
//		driver.close();
//		switchToWin(winHandler);
//		switchToDefaultWinFromFrame();
//		accessionNavigation = new AccessionNavigation(driver, config);
//		accessionNavigation.navigateToAccnTestUpdateLinkLink();
//		switchToPopupWin();
//
//		logger.info("*** Step 18 Expected Results: - The Accn Test Update page displays");
//		accnTestUpdate = new AccnTestUpdateOld(driver);
//		Assert.assertEquals(accnTestUpdate.pageTitleText().getText().trim(), "Accession - Test Update", "       Accession Update Test page title should show.");
//
//		logger.info("*** Step 19 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
//		accnId = daoManagerAccnWS.getAccnIdFromAccnByDays("100", testDb);
//		accnTestUpdate.searchAccnId(accnId);
//
//		logger.info("*** Step 19 Expected Results: - Verify that the patient name is correct");
//		Assert.assertTrue(isElementPresent(accnTestUpdate.ptNameInput(),5));
//		Assert.assertEquals(accnTestUpdate.ptNameInput().getAttribute("value"), daoManagerPatientPortal.getLnameByAccnId(accnId, testDb) + ", "+daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
//
//		logger.info("*** Step 20 Actions: - In the Final Report Date column enter in a past date but after Dos");
//		accnTestUpdate = new AccnTestUpdateOld(driver);
//		timeStamp = new TimeStamp();
//		accnTestUpdate.setFinalRptDt(timeStamp.getPreviousDate("MM/dd/yyyy", 10));
//
//		logger.info("*** Step 20 Expected Results: - No pop-up messaging appears");
//		Assert.assertFalse(isAlertPresent());
//    }
//
//	@Test(priority = 1, description = "Enter in Final Report Date in the future")
//	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
//	public void testRPM_421(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
//		logger.info("***** Testing - testRPM_421 *****");
//
//		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
//		String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
//
//		String winHandler = driver.getWindowHandle();
//
//		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
//		headerNavigation = new HeaderNavigation(driver, config);
//		headerNavigation.navigateToAccessionTab();
//		switchToPopupWin();
//
//		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
//		accessionDemographics = new AccessionDemographics(driver);
//		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");
//
//		logger.info("*** Step 18 Actions: - Navigate to Accn Test Update");
//		driver.close();
//		switchToWin(winHandler);
//		switchToDefaultWinFromFrame();
//		accessionNavigation = new AccessionNavigation(driver, config);
//		accessionNavigation.navigateToAccnTestUpdateLinkLink();
//		switchToPopupWin();
//
//		logger.info("*** Step 18 Expected Results: - The Accn Test Update page displays");
//		accnTestUpdate = new AccnTestUpdateOld(driver);
//		Assert.assertEquals(accnTestUpdate.pageTitleText().getText().trim(), "Accession - Test Update", "       Accession Update Test page title should show.");
//
//		logger.info("*** Step 19 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
//		accnTestUpdate.searchAccnId(accnId);
//
//		logger.info("*** Step 19 Expected Results: - Verify that the patient name is correct");
//		Assert.assertTrue(isElementPresent(accnTestUpdate.ptNameInput(),5));
//		Assert.assertEquals(accnTestUpdate.ptNameInput().getAttribute("value"), daoManagerPatientPortal.getLnameByAccnId(accnId, testDb) + ", "+daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
//
//		logger.info("*** Step 20 Actions: - In the Final Report Date column enter in a future date");
//		accnTestUpdate = new AccnTestUpdateOld(driver);
//		accnTestUpdate.setFinalRptDt("t+10");
//
//		logger.info("*** Step 20 Expected Results: - Pop-up messaging appears alerting user date entered is in the future");
//		Assert.assertEquals(closeAlertAndGetItsText(true), "Future Final Report Date is not allowed.");
//    }
//
//
//
//}
