package com.newXp.tests;


import com.overall.client.clientInquiry.clientBatchSingleDemandStatement.ClientBatchSingleDemandStatement;
import com.overall.menu.MenuNavigation;
import com.overall.search.ClientSearch;
import com.overall.search.ClientSearchResults;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.TimeStamp;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SmokeClientBatchSingleDemandStatementTest extends SeleniumBaseTest {

	private TimeStamp timeStamp;
	private MenuNavigation navigation;
	private ClientBatchSingleDemandStatement clnBatchSingleDemandStmt;
	private FileManipulation fileManipulation;
	private ClientSearch clientSearch;
	private ClientSearchResults clientSearchResults;

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
	@Test(priority = 1, description = "Generate client statement with Client without SubClient")
	@Parameters({"formatType"})
	public void testXPR_624(String formatType) throws Exception {
		logger.info("===== Testing - testXPR_624 =====");    
		clnBatchSingleDemandStmt = new ClientBatchSingleDemandStatement(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		fileManipulation = new FileManipulation(driver);
		
		logger.info("*** Step 1 Actions: - Log into Client Batch Single Demand Statement page with SSO username and password");				
		navigation.navigateToClientBatchSingleDemandStatementPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Client Batch Single Demand Statement page is displayed with correct page title");	
		Assert.assertEquals(clnBatchSingleDemandStmt.pageTitle().getText(), "Batch Single Demand Statement", "       Page title: Batch Single Demand Statement Page is displayed.");

		logger.info("*** Step 2 Actions: - Click on Add button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.addBtn(), 5),"        Add button is displayed.");
		clnBatchSingleDemandStmt.clickAddBtn();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Add Record popup window is displayed");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientIdInput(), 5),"        Add Record popup is displayed.");
		
		logger.info("*** Step 3 Actions: - Add a Client that has no subClient and FK_SUMB_SVC_ID != 0 and due_amt > 0 and has activity for the month");
		String accountingPeriodEndDtStr = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(71, testDb);
		List<List<String>> clientInfoList = daoManagerXifinRpm.getClnHaveNoSubClnHaveSubmSvcDueAmtFromCLNSUBM(accountingPeriodEndDtStr, testDb);
		String clnAbbrev = clientInfoList.get(0).get(0);
		String clnName = clientInfoList.get(0).get(1);
		clnBatchSingleDemandStmt.setClientId(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client is loaded properly and the Include 'B' Clients checkbox is disabled in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName, "       Client Name " + clnName + " should be displayed.");
		Assert.assertFalse(clnBatchSingleDemandStmt.includeBClnChkBox().isEnabled(), "       Include 'B' Client checkbox should be disabled.");
		
		logger.info("*** Step 4 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added to the Batch Clients table");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev),"        Client ID " + clnAbbrev + " should show in Batch Clients table.");
		
		logger.info("*** Step 5 Actions: - Click on Generate Statement button and accept the page Alert");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.generateStmBtn(), 5),"        Generate Statement button is displayed.");
		clnBatchSingleDemandStmt.clickGenerateStmBtn();		
		clnBatchSingleDemandStmt.isSaveDone();		
		closeAlertAndGetItsText(true);
	
		logger.info("*** Step 5 Expected Results: - Verify that Client Statements for the client and all the clients will be generated");
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		
		List<String> clientSubmInfoList = daoManagerXifinRpm.getClnBtchFromSUBMFILEByClientId(clnAbbrev,testDb);
		String dir = clientSubmInfoList.get(1).trim().replaceAll("[^0-9]", "");
		String fileLocation = dirBase + dir + File.separator;//daoManagerXifinRpm.getLocationELIGFromSYSTEMSETTING(testDb)+"/"+dir;
		String clnStmtFileName = clientSubmInfoList.get(2).trim();
		File fGenerated = new File(fileLocation + clnStmtFileName);
		Assert.assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");		

		logger.info("*** Step 5 Expected Results: - Verify that more than one Client Statements will be generated");
		String currDt = timeStamp.getCurrentDate();
		List<List<String>> submFileInfoList = daoManagerXifinRpm.getSubmFileInfoFromSUBMFILEByCreatDtProcessedDt(currDt, currDt, testDb);
		List<String> fileSeqIdList = new ArrayList<>();
		for (List<String> strings : submFileInfoList) {
			fileSeqIdList.add(strings.get(0));
		}
		
		Assert.assertTrue(fileSeqIdList.size()>1, "       More than one documents should be generated.");

		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fGenerated = new File(fileLocation + clnStmtFileName);
			assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");
		}
		
		logger.info("*** Step 8 Actions: - Clear test data");
		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fileManipulation.deleteFile(fileLocation, clnStmtFileName);
			daoManagerXifinRpm.deleteSubmFileFromSUBMFileBySubmFileSeqId(s, testDb);
		}	
	
		driver.close();
	}
	
	@Test(priority = 1, description = "Generate client statement with Client has SubClient")
	@Parameters({"formatType"})
	public void testXPR_622(String formatType) throws Exception {
		logger.info("===== Testing - testXPR_622 =====");    
		clnBatchSingleDemandStmt = new ClientBatchSingleDemandStatement(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		fileManipulation = new FileManipulation(driver);
		
		logger.info("*** Step 1 Actions: - Log into Client Batch Single Demand Statement page with SSO username and password");				
		navigation.navigateToClientBatchSingleDemandStatementPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Client Batch Single Demand Statement page is displayed with correct page title");	
		Assert.assertEquals(clnBatchSingleDemandStmt.pageTitle().getText(), "Batch Single Demand Statement", "       Page title: Batch Single Demand Statement Page is displayed.");

		logger.info("*** Step 2 Actions: - Click on Add button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.addBtn(), 5),"        Add button is displayed.");
		clnBatchSingleDemandStmt.clickAddBtn();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Add Record popup window is displayed");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientIdInput(), 5),"        Add Record popup is displayed.");
		
		logger.info("*** Step 3 Actions: - Add a Client that has subClient and FK_SUMB_SVC_ID != 0 and due_amt > 0 and has activity for the month");
		String accountingPeriodEndDtStr = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(71, testDb);
		List<List<String>> clientInfoList = daoManagerXifinRpm.getClnHaveNoSubClnHaveSubmSvcDueAmtFromCLNSUBM(accountingPeriodEndDtStr, testDb);	
		String clnAId = clientInfoList.get(0).get(2);
		String clnBId = clientInfoList.get(1).get(2);
		int maxIndex = daoManagerPlatform.getMaxIndexFromCLNPTLNK(testDb);
		String nextVal = String.valueOf(maxIndex + 1);
		daoManagerPlatform.addClnPtLnkFromCLNPTLNK(nextVal, clnAId, clnBId, testDb);	
		
		String clnAbbrev = clientInfoList.get(0).get(0);
		String clnName = clientInfoList.get(0).get(1);
		clnBatchSingleDemandStmt.setClientId(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client is loaded properly and the Include 'B' Clients checkbox is enabled in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName, "       Client Name " + clnName + " should be displayed.");
		Assert.assertTrue(clnBatchSingleDemandStmt.includeBClnChkBox().isEnabled(), "       Include 'B' Client checkbox should be enabled.");
		
		logger.info("*** Step 4 Actions: - Check Include B Checkbox");
		clnBatchSingleDemandStmt.checkIncludeBCheckbox();
		
		logger.info("*** Step 5 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 5 Expected Results: - Verify that a new record is added to the Batch Clients table and Include 'B' Client checkbox is checked");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev),"        Client ID " + clnAbbrev + " should show in Batch Clients table.");
		
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientTblCheckbox(2,4), 5), "Include 'B' Clients checkbox should show.");
		assertEquals(clnBatchSingleDemandStmt.batchClientTblCheckbox(2, 4).getAttribute("value"), "true", "Include 'B' Clients checkbox should be checked.");
				
		logger.info("*** Step 5 Actions: - Click on Generate Statement button and accept the page Alert");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.generateStmBtn(), 5),"        Generate Statement button is displayed.");
		clnBatchSingleDemandStmt.clickGenerateStmBtn();		
		clnBatchSingleDemandStmt.isSaveDone();		
		closeAlertAndGetItsText(true);		
	
		logger.info("*** Step 5 Expected Results: - Verify that Client Statements for the client and all the clients will be generated");
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		
		List<String> clientSubmInfoList = daoManagerXifinRpm.getClnBtchFromSUBMFILEByClientId(clnAbbrev,testDb);
		String dir = clientSubmInfoList.get(1).trim().replaceAll("[^0-9]", "");
		String fileLocation = dirBase + dir + File.separator;//daoManagerXifinRpm.getLocationELIGFromSYSTEMSETTING(testDb)+"/"+dir;
		String clnStmtFileName = clientSubmInfoList.get(2).trim();
		File fGenerated = new File(fileLocation + clnStmtFileName);
		Assert.assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");		

		logger.info("*** Step 5 Expected Results: - Verify that more than one Client Statements will be generated");
		String currDt = timeStamp.getCurrentDate();
		List<List<String>> submFileInfoList = daoManagerXifinRpm.getSubmFileInfoFromSUBMFILEByCreatDtProcessedDt(currDt, currDt, testDb);
		List<String> fileSeqIdList = new ArrayList<>();
		for (List<String> strings : submFileInfoList) {
			fileSeqIdList.add(strings.get(0));
		}
		
		Assert.assertTrue(fileSeqIdList.size()>1, "       More than one documents should be generated.");

		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fGenerated = new File(fileLocation + clnStmtFileName);
			assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");
		}
		
		logger.info("*** Step 6 Actions: - Clear test data");
		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fileManipulation.deleteFile(fileLocation, clnStmtFileName);
			daoManagerXifinRpm.deleteSubmFileFromSUBMFileBySubmFileSeqId(s, testDb);
		}	
		
		daoManagerPlatform.deleteFromCLNPTLNKByClnPtLnkId(nextVal, testDb);
	
	}

	@Test(priority = 1, description = "Generate multiple Client statements that have setup EXCEL")
	@Parameters({"formatType"})
	public void testXPR_626(String formatType) throws Exception {
		logger.info("===== Testing - testXPR_626 =====");    
		clnBatchSingleDemandStmt = new ClientBatchSingleDemandStatement(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		fileManipulation = new FileManipulation(driver);
		
		logger.info("*** Step 1 Actions: - Log into Client Batch Single Demand Statement page with SSO username and password");				
		navigation.navigateToClientBatchSingleDemandStatementPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Client Batch Single Demand Statement page is displayed with correct page title");	
		Assert.assertEquals(clnBatchSingleDemandStmt.pageTitle().getText(), "Batch Single Demand Statement", "       Page title: Batch Single Demand Statement Page is displayed.");

		logger.info("*** Step 2 Actions: - Click on Add button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.addBtn(), 5),"        Add button is displayed.");
		clnBatchSingleDemandStmt.clickAddBtn();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Add Record popup window is displayed");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientIdInput(), 5),"        Add Record popup is displayed.");
		
		logger.info("*** Step 3 Actions: - Add a Client that FK_SUMB_SVC_ID != 0 and due_amt > 0 and has setup Excel and has activity for the month");
		String accountingPeriodEndDtStr = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(71, testDb);
		List<List<String>> clientInfoList = daoManagerXifinRpm.getClnHaveSubmSvcDueAmtExcelFromCLNSUBM(accountingPeriodEndDtStr, testDb);	
		String clnAbbrev = clientInfoList.get(0).get(1);
		String clnName = clientInfoList.get(0).get(2);
		clnBatchSingleDemandStmt.setClientId(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client is loaded properly in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName, "       Client Name " + clnName + " should be displayed.");

		logger.info("*** Step 4 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added to the Batch Clients table");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev),"        Client ID " + clnAbbrev + " should show in Batch Clients table.");
		
		logger.info("*** Step 5 Actions: - Click on Generate Statement button and accept the page Alert");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.generateStmBtn(), 5),"        Generate Statement button is displayed.");
		clnBatchSingleDemandStmt.clickGenerateStmBtn();		
		clnBatchSingleDemandStmt.isSaveDone();		
		closeAlertAndGetItsText(true);		
	
		logger.info("*** Step 5 Expected Results: - Verify that Client Statements for the selected client and all clients were generated");
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		
		List<String> clientSubmInfoList = daoManagerXifinRpm.getClnBtchFromSUBMFILEByClientId(clnAbbrev,testDb);
		String dir = clientSubmInfoList.get(1).trim().replaceAll("[^0-9]", "");
		String fileLocation = dirBase + dir + File.separator;
		String clnStmtFileName = clientSubmInfoList.get(2).trim();
		File fGenerated = new File(fileLocation + clnStmtFileName);
		Assert.assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");		

		logger.info("*** Step 5 Expected Results: - Verify that more than two Client Statements were generated");
		String currDt = timeStamp.getCurrentDate();
		List<List<String>> submFileInfoList = daoManagerXifinRpm.getSubmFileInfoFromSUBMFILEByCreatDtProcessedDt(currDt, currDt, testDb);
		List<String> fileSeqIdList = new ArrayList<>();
		List<String> fileNameList = new ArrayList<>();
		for (List<String> strings : submFileInfoList) {
			fileSeqIdList.add(strings.get(0));
			fileNameList.add(strings.get(5));
		}
		
		Assert.assertTrue(fileSeqIdList.size()>2, "       More than two documents should be generated.");

		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fGenerated = new File(fileLocation + clnStmtFileName);
			assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");
		}
		
		logger.info("*** Step 5 Expected Results: - Verify that Excel Client Statements was generated");
		boolean xlsFlag = false;
		for (String s : fileNameList) {
			if (s.contains(".xls")) {
				xlsFlag = true;
				logger.info("       Excel Client Statement file was generated.");
			}
		}
		Assert.assertTrue(xlsFlag, "        Excel Client Statement file should be generated.");		
		
		logger.info("*** Step 6 Actions: - Clear test data");
		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fileManipulation.deleteFile(fileLocation, clnStmtFileName);
			daoManagerXifinRpm.deleteSubmFileFromSUBMFileBySubmFileSeqId(s, testDb);
		}	
	
	}
	
	@Test(priority = 1, description = "Generate Client statements by adding multiple clients")
	@Parameters({"formatType"})
	public void testXPR_705(String formatType) throws Exception {
		logger.info("===== Testing - testXPR_705 =====");    
		clnBatchSingleDemandStmt = new ClientBatchSingleDemandStatement(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		fileManipulation = new FileManipulation(driver);
		
		logger.info("*** Step 1 Actions: - Log into Client Batch Single Demand Statement page with SSO username and password");				
		navigation.navigateToClientBatchSingleDemandStatementPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Client Batch Single Demand Statement page is displayed with correct page title");	
		Assert.assertEquals(clnBatchSingleDemandStmt.pageTitle().getText(), "Batch Single Demand Statement", "       Page title: Batch Single Demand Statement Page is displayed.");

		logger.info("*** Step 2 Actions: - Click on Add button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.addBtn(), 5),"        Add button is displayed.");
		clnBatchSingleDemandStmt.clickAddBtn();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Add Record popup window is displayed");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientIdInput(), 5),"        Add Record popup is displayed.");
		
		logger.info("*** Step 3 Actions: - Add a Client that has no subClient and FK_SUMB_SVC_ID != 0 and due_amt > 0 and has activity for the month");
		String accountingPeriodEndDtStr = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(71, testDb);
		List<List<String>> clientInfoList = daoManagerXifinRpm.getClnHaveNoSubClnHaveSubmSvcDueAmtFromCLNSUBM(accountingPeriodEndDtStr, testDb);	
		String clnAbbrev1 = clientInfoList.get(0).get(0);
		String clnName1 = clientInfoList.get(0).get(1);
		clnBatchSingleDemandStmt.setClientId(clnAbbrev1);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client is loaded properly in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName1, "       Client Name " + clnName1 + " should be displayed.");

		logger.info("*** Step 4 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added to the Batch Clients table");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev1),"        Client ID " + clnAbbrev1 + " should show in Batch Clients table.");
		
		logger.info("*** Step 5 Actions: - Add another Client that has no subClient and FK_SUMB_SVC_ID != 0 and due_amt > 0 and has activity for the month");
		clientInfoList = daoManagerXifinRpm.getClnHaveNoSubClnHaveSubmSvcDueAmtFromCLNSUBM(accountingPeriodEndDtStr, testDb);
		String clnAbbrev2 = clientInfoList.get(0).get(0);
		String clnName2 = clientInfoList.get(0).get(1);
		
		while (clnAbbrev1.equals(clnAbbrev2)){
			clientInfoList = daoManagerXifinRpm.getClnHaveNoSubClnHaveSubmSvcDueAmtFromCLNSUBM(accountingPeriodEndDtStr, testDb);
			clnAbbrev2 = clientInfoList.get(0).get(0);
			clnName2 = clientInfoList.get(0).get(1);
		}		
		
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.addBtn(), 5),"        Add button is displayed.");
		clnBatchSingleDemandStmt.clickAddBtn();		
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientIdInput(), 5),"        Add Record popup is displayed.");		
		clnBatchSingleDemandStmt.setClientId(clnAbbrev2);
		
		logger.info("*** Step 5 Expected Results: - Verify that the Client is loaded properly in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName2, "       Client Name " + clnName2 + " should be displayed.");

		logger.info("*** Step 6 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added to the Batch Clients table");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev2),"        Client ID " + clnAbbrev2 + " should show in Batch Clients table.");
				
		logger.info("*** Step 7 Actions: - Click on Generate Statement button and accept the page Alert");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.generateStmBtn(), 5),"        Generate Statement button is displayed.");
		clnBatchSingleDemandStmt.clickGenerateStmBtn();		
		clnBatchSingleDemandStmt.isSaveDone();		
		closeAlertAndGetItsText(true);		
	
		logger.info("*** Step 7 Expected Results: - Verify that Client Statements for the client and all the clients will be generated");
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		
		List<String> clientSubmInfoList = daoManagerXifinRpm.getClnBtchFromSUBMFILEByClientId(clnAbbrev1,testDb);
		String dir = clientSubmInfoList.get(1).trim().replaceAll("[^0-9]", "");
		String fileLocation = dirBase + dir + File.separator;//daoManagerXifinRpm.getLocationELIGFromSYSTEMSETTING(testDb)+"/"+dir;
		String clnStmtFileName = clientSubmInfoList.get(2).trim();
		File fGenerated = new File(fileLocation + clnStmtFileName);
		Assert.assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");	
		
		clientSubmInfoList = daoManagerXifinRpm.getClnBtchFromSUBMFILEByClientId(clnAbbrev2,testDb);
		clnStmtFileName = clientSubmInfoList.get(2).trim();		
		fGenerated = new File(fileLocation + clnStmtFileName);
		Assert.assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");		

		logger.info("*** Step 7 Expected Results: - Verify that three Client Statements will be generated");
		String currDt = timeStamp.getCurrentDate();
		List<List<String>> submFileInfoList = daoManagerXifinRpm.getSubmFileInfoFromSUBMFILEByCreatDtProcessedDt(currDt, currDt, testDb);
		List<String> fileSeqIdList = new ArrayList<>();
		for (List<String> strings : submFileInfoList) {
			fileSeqIdList.add(strings.get(0));
		}
		
		Assert.assertTrue(fileSeqIdList.size()>2, "       Three documents should be generated.");

		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fGenerated = new File(fileLocation + clnStmtFileName);
			assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");
		}
		
		logger.info("*** Step 7 Expected Results: - Verify that Client Statements will be generated for each added Client");
		boolean clnFlag1 = false;
		boolean clnFlag2 = false;
		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			if (clnStmtFileName.contains(clnAbbrev1)) {
				clnFlag1 = true;
			}
			if (clnStmtFileName.contains(clnAbbrev2)) {
				clnFlag2 = true;
			}
		}
		
		Assert.assertTrue(clnFlag1, "        Client Statement for Client" + clnAbbrev1 + " should be generated in " + fileLocation + " folder.");
		Assert.assertTrue(clnFlag2, "        Client Statement for Client" + clnAbbrev2 + " should be generated in " + fileLocation + " folder.");		
		
		logger.info("*** Step 8 Actions: - Clear test data");
		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fileManipulation.deleteFile(fileLocation, clnStmtFileName);
			daoManagerXifinRpm.deleteSubmFileFromSUBMFileBySubmFileSeqId(s, testDb);
		}	
	
	}
	
	@Test(priority = 1, description = "Delete Clients in Table")
	@Parameters({"formatType"})
	public void testXPR_628(String formatType) throws Exception {
		logger.info("===== Testing - testXPR_628 =====");    
		clnBatchSingleDemandStmt = new ClientBatchSingleDemandStatement(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		fileManipulation = new FileManipulation(driver);
		
		logger.info("*** Step 1 Actions: - Log into Client Batch Single Demand Statement page with SSO username and password");				
		navigation.navigateToClientBatchSingleDemandStatementPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Client Batch Single Demand Statement page is displayed with correct page title");	
		Assert.assertEquals(clnBatchSingleDemandStmt.pageTitle().getText(), "Batch Single Demand Statement", "       Page title: Batch Single Demand Statement Page is displayed.");

		logger.info("*** Step 2 Actions: - Click on Add button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.addBtn(), 5),"        Add button is displayed.");
		clnBatchSingleDemandStmt.clickAddBtn();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Add Record popup window is displayed");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientIdInput(), 5),"        Add Record popup is displayed.");
		
		logger.info("*** Step 3 Actions: - Add a Client that has no subClient and FK_SUMB_SVC_ID != 0 and due_amt > 0 and has activity for the month");
		String accountingPeriodEndDtStr = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(71, testDb);
		List<List<String>> clientInfoList = daoManagerXifinRpm.getClnHaveNoSubClnHaveSubmSvcDueAmtFromCLNSUBM(accountingPeriodEndDtStr, testDb);	
		String clnAbbrev = clientInfoList.get(0).get(0);
		String clnName = clientInfoList.get(0).get(1);
		clnBatchSingleDemandStmt.setClientId(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client is loaded properly and the Include 'B' Clients checkbox is disabled in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName, "       Client Name " + clnName + " should be displayed.");
		Assert.assertFalse(clnBatchSingleDemandStmt.includeBClnChkBox().isEnabled(), "       Include 'B' Client checkbox should be disabled.");
		
		logger.info("*** Step 4 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added to the Batch Clients table");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev),"        Client ID " + clnAbbrev + " should show in Batch Clients table.");
		
		logger.info("*** Step 5 Actions: - Click on Delete checkbox");
		clnBatchSingleDemandStmt.clickDeleteCheckboxOnTable(2);		
		
		logger.info("*** Step 6 Actions: - Click on Generate Statement button and accept the page Alert");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.generateStmBtn(), 5),"        Generate Statement button is displayed.");
		clnBatchSingleDemandStmt.clickGenerateStmBtn();		
		clnBatchSingleDemandStmt.isSaveDone();		
		closeAlertAndGetItsText(true);				
	
		logger.info("*** Step 6 Expected Results: - Verify that Client Statements won't be generated");
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		
		List<String> clientSubmInfoList = daoManagerXifinRpm.getClnBtchFromSUBMFILEByClientId(clnAbbrev,testDb);
		String dir = clientSubmInfoList.get(1).trim().replaceAll("[^0-9]", "");
		String fileLocation = dirBase + dir + File.separator;
		String clnStmtFileName = clientSubmInfoList.get(2).trim();
		File fGenerated = new File(fileLocation + clnStmtFileName);
		
		String[] accountingDtArray = accountingPeriodEndDtStr.split("/");
		String folderStr = accountingDtArray[2] + accountingDtArray[0]; 
		
		if (dir.equals(folderStr)){		
			Assert.assertFalse(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " was generated in " + fileLocation + " folder.");		
		}
		else{
			Assert.assertFalse(false, "       Client Statement files for Client " + clnAbbrev + " were generated.");
		}
		
	}
	
	@Test(priority = 1, description = "Cancel deleting Clients in Table")
	@Parameters({"formatType"})
	public void testXPR_630(String formatType) throws Exception {
		logger.info("===== Testing - testXPR_630 =====");    
		clnBatchSingleDemandStmt = new ClientBatchSingleDemandStatement(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		fileManipulation = new FileManipulation(driver);
		
		logger.info("*** Step 1 Actions: - Log into Client Batch Single Demand Statement page with SSO username and password");				
		navigation.navigateToClientBatchSingleDemandStatementPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Client Batch Single Demand Statement page is displayed with correct page title");	
		Assert.assertEquals(clnBatchSingleDemandStmt.pageTitle().getText(), "Batch Single Demand Statement", "       Page title: Batch Single Demand Statement Page is displayed.");

		logger.info("*** Step 2 Actions: - Click on Add button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.addBtn(), 5),"        Add button is displayed.");
		clnBatchSingleDemandStmt.clickAddBtn();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Add Record popup window is displayed");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientIdInput(), 5),"        Add Record popup is displayed.");
		
		logger.info("*** Step 3 Actions: - Add a Client that has no subClient and FK_SUMB_SVC_ID != 0 and due_amt > 0 and has activity for the month");
		String accountingPeriodEndDtStr = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(71, testDb);
		List<List<String>> clientInfoList = daoManagerXifinRpm.getClnHaveNoSubClnHaveSubmSvcDueAmtFromCLNSUBM(accountingPeriodEndDtStr, testDb);	
		String clnAbbrev = clientInfoList.get(0).get(0);
		String clnName = clientInfoList.get(0).get(1);
		clnBatchSingleDemandStmt.setClientId(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client is loaded properly in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName, "       Client Name " + clnName + " should be displayed.");

		logger.info("*** Step 4 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added to the Batch Clients table");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev),"        Client ID " + clnAbbrev + " should show in Batch Clients table.");
		
		logger.info("*** Step 5 Actions: - Click on Delete checkbox");
		clnBatchSingleDemandStmt.clickDeleteCheckboxOnTable(2);		
		
		logger.info("*** Step 6 Actions: - Uncheck Delete checkbox");
		clnBatchSingleDemandStmt.clickDeleteCheckboxOnTable(2);		
		
		logger.info("*** Step 6 Expected Results: - Verify that Delete checkbox for the Client is unchecked");
		assertEquals(clnBatchSingleDemandStmt.batchClientTblCheckbox(2, 6).getAttribute("value"), "false", "        This row should NOT bemarked deleted.");
		
		logger.info("*** Step 7 Actions: - Click on Generate Statement button and accept the page Alert");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.generateStmBtn(), 5),"        Generate Statement button is displayed.");
		clnBatchSingleDemandStmt.clickGenerateStmBtn();		
		clnBatchSingleDemandStmt.isSaveDone();		
		closeAlertAndGetItsText(true);				
	
		logger.info("*** Step 7 Expected Results: - Verify that Client Statements will be generated");
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		
		List<String> clientSubmInfoList = daoManagerXifinRpm.getClnBtchFromSUBMFILEByClientId(clnAbbrev,testDb);
		String dir = clientSubmInfoList.get(1).trim().replaceAll("[^0-9]", "");
		String fileLocation = dirBase + dir + File.separator;
		String clnStmtFileName = clientSubmInfoList.get(2).trim();
		File fGenerated = new File(fileLocation + clnStmtFileName);
		Assert.assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");		
		
		logger.info("*** Step 7 Expected Results: - Verify that more than one Client Statements will be generated");
		String currDt = timeStamp.getCurrentDate();
		List<List<String>> submFileInfoList = daoManagerXifinRpm.getSubmFileInfoFromSUBMFILEByCreatDtProcessedDt(currDt, currDt, testDb);
		List<String> fileSeqIdList = new ArrayList<>();
		for (List<String> strings : submFileInfoList) {
			fileSeqIdList.add(strings.get(0));
		}
		
		Assert.assertTrue(fileSeqIdList.size()>1, "       More than one documents should be generated.");

		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fGenerated = new File(fileLocation + clnStmtFileName);
			assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");
		}
		
		logger.info("*** Step 9 Actions: - Clear test data");
		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fileManipulation.deleteFile(fileLocation, clnStmtFileName);
			daoManagerXifinRpm.deleteSubmFileFromSUBMFileBySubmFileSeqId(s, testDb);
		}	

	}
	
	@Test(priority = 1, description = "Load a Client with no activity or balance due = 0")
	public void testXPR_625() throws Exception {
		logger.info("===== Testing - testXPR_625 =====");    
		clnBatchSingleDemandStmt = new ClientBatchSingleDemandStatement(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		fileManipulation = new FileManipulation(driver);
		
		logger.info("*** Step 1 Actions: - Log into Client Batch Single Demand Statement page with SSO username and password");				
		navigation.navigateToClientBatchSingleDemandStatementPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Client Batch Single Demand Statement page is displayed with correct page title");	
		Assert.assertEquals(clnBatchSingleDemandStmt.pageTitle().getText(), "Batch Single Demand Statement", "       Page title: Batch Single Demand Statement Page is displayed.");

		logger.info("*** Step 2 Actions: - Click on Add button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.addBtn(), 5),"        Add button is displayed.");
		clnBatchSingleDemandStmt.clickAddBtn();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Add Record popup window is displayed");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientIdInput(), 5),"        Add Record popup is displayed.");
		
		logger.info("*** Step 3 Actions: - Add a Client that has no activity for the month or balance due = 0");
		String accountingPeriodEndDtStr = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(71, testDb);
		List<String> clientInfoList = daoManagerXifinRpm.getClnHasNoActivityFromCLNSUBM(accountingPeriodEndDtStr, testDb);	
		String clnAbbrev = clientInfoList.get(1);
		String clnName = clientInfoList.get(2);
		clnBatchSingleDemandStmt.setClientId(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client is loaded properly in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName, "       Client Name " + clnName + " should be displayed.");

		logger.info("*** Step 3 Expected Results: - Verify that a Warning message should show in Add Record window");
		String warningMsg = "Warning: " + clnAbbrev.toUpperCase();
		Assert.assertTrue(clnBatchSingleDemandStmt.popupErrMsgTxt().getText().contains(warningMsg),"        A warning message should show.");		
		
		logger.info("*** Step 4 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added to the Batch Clients table");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev),"        Client ID " + clnAbbrev + " should show in Batch Clients table.");
		
		logger.info("*** Step 5 Actions: - Click on Generate Statement button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.generateStmBtn(), 5),"        Generate Statement button is displayed.");
		clnBatchSingleDemandStmt.clickGenerateStmBtn();		
		
		logger.info("*** Step 5 Expected Results: - Verify that a Warning message should show");		
		Assert.assertTrue(clnBatchSingleDemandStmt.errMsgTxt().getText().contains(warningMsg),"        A warning message should show.");	
		
	}

	@Test(priority = 1, description = "Generate Client Statement by updating Client")
	@Parameters({"formatType"})
	public void testXPR_629(String formatType) throws Exception {
		logger.info("===== Testing - testXPR_629 =====");    
		clnBatchSingleDemandStmt = new ClientBatchSingleDemandStatement(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		fileManipulation = new FileManipulation(driver);
		
		logger.info("*** Step 1 Actions: - Log into Client Batch Single Demand Statement page with SSO username and password");				
		navigation.navigateToClientBatchSingleDemandStatementPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Client Batch Single Demand Statement page is displayed with correct page title");	
		Assert.assertEquals(clnBatchSingleDemandStmt.pageTitle().getText(), "Batch Single Demand Statement", "       Page title: Batch Single Demand Statement Page is displayed.");

		logger.info("*** Step 2 Actions: - Click on Add button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.addBtn(), 5),"        Add button is displayed.");
		clnBatchSingleDemandStmt.clickAddBtn();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Add Record popup window is displayed");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientIdInput(), 5),"        Add Record popup is displayed.");
		
		logger.info("*** Step 3 Actions: - Add a Client that has no activity for the month or balance due = 0");
		String accountingPeriodEndDtStr = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(71, testDb);
		List<String> clientInfoList = daoManagerXifinRpm.getClnHasNoActivityFromCLNSUBM(accountingPeriodEndDtStr, testDb);	
		String clnAbbrev = clientInfoList.get(1);
		String clnName = clientInfoList.get(2);
		clnBatchSingleDemandStmt.setClientId(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client is loaded properly in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName, "       Client Name " + clnName + " should be displayed.");

		logger.info("*** Step 3 Expected Results: - Verify that a Warning message should show in Add Record window");
		String warningMsg = "Warning: " + clnAbbrev.toUpperCase();
		Assert.assertTrue(clnBatchSingleDemandStmt.popupErrMsgTxt().getText().contains(warningMsg),"        A warning message should show.");		
		
		logger.info("*** Step 4 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added to the Batch Clients table");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev),"        Client ID " + clnAbbrev + " should show in Batch Clients table.");
		
		logger.info("*** Step 5 Actions: - Click on Generate Statement button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.generateStmBtn(), 5),"        Generate Statement button is displayed.");
		clnBatchSingleDemandStmt.clickGenerateStmBtn();		
		
		logger.info("*** Step 5 Expected Results: - Verify that a Warning message should show");		
		Assert.assertTrue(clnBatchSingleDemandStmt.errMsgTxt().getText().contains(warningMsg),"        A warning message should show.");	
		
		logger.info("*** Step 6 Actions: - Select the existing row in the Batch Clients table and click Edit button");
		clnBatchSingleDemandStmt.clickBatchClientTblCell(2,2);
		clnBatchSingleDemandStmt.clickEditBtn();
		
		logger.info("*** Step 7 Actions: - Enter a new Client that has no subClient and FK_SUMB_SVC_ID != 0 and due_amt > 0 and has activity for the month");
		List<List<String>> clientInfoList2 = daoManagerXifinRpm.getClnHaveNoSubClnHaveSubmSvcDueAmtFromCLNSUBM(accountingPeriodEndDtStr, testDb);	
		clnAbbrev = clientInfoList2.get(0).get(0);
		clnName = clientInfoList2.get(0).get(1);
		
		clnBatchSingleDemandStmt.setClientId(clnAbbrev);
		
		logger.info("*** Step 7 Expected Results: - Verify that the Client is loaded properly in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName, "       Client Name " + clnName + " should be displayed.");		
		
		logger.info("*** Step 8 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 8 Expected Results: - Verify that a new record is added to the Batch Clients table");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev),"        Client ID " + clnAbbrev + " should show in Batch Clients table.");
		
		logger.info("*** Step 9 Actions: - Click on Generate Statement button and accept the page Alert");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.generateStmBtn(), 5),"        Generate Statement button is displayed.");
		clnBatchSingleDemandStmt.clickGenerateStmBtn();		
		clnBatchSingleDemandStmt.isSaveDone();		
		closeAlertAndGetItsText(true);				
	
		logger.info("*** Step 9 Expected Results: - Verify that Client Statements will be generated for the updated Client");
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		
		List<String> clientSubmInfoList = daoManagerXifinRpm.getClnBtchFromSUBMFILEByClientId(clnAbbrev,testDb);
		String dir = clientSubmInfoList.get(1).trim().replaceAll("[^0-9]", "");
		String fileLocation = dirBase + dir + File.separator;
		String clnStmtFileName = clientSubmInfoList.get(2).trim();
		File fGenerated = new File(fileLocation + clnStmtFileName);
		Assert.assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");		
		
		logger.info("*** Step 9 Expected Results: - Verify that more than one Client Statements will be generated");
		String currDt = timeStamp.getCurrentDate();
		List<List<String>> submFileInfoList = daoManagerXifinRpm.getSubmFileInfoFromSUBMFILEByCreatDtProcessedDt(currDt, currDt, testDb);
		List<String> fileSeqIdList = new ArrayList<>();
		for (List<String> strings : submFileInfoList) {
			fileSeqIdList.add(strings.get(0));
		}
		
		Assert.assertTrue(fileSeqIdList.size()>1, "       More than one documents should be generated.");

		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fGenerated = new File(fileLocation + clnStmtFileName);
			assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");
		}
		
		logger.info("*** Step 10 Actions: - Clear test data");
		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fileManipulation.deleteFile(fileLocation, clnStmtFileName);
			daoManagerXifinRpm.deleteSubmFileFromSUBMFileBySubmFileSeqId(s, testDb);
		}			

	}

	@Test(priority = 1, description = "Load Client ID via Client Search")
	@Parameters({"formatType"})
	public void testXPR_641(String formatType) throws Exception {
		logger.info("===== Testing - testXPR_641 =====");    
		clnBatchSingleDemandStmt = new ClientBatchSingleDemandStatement(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		fileManipulation = new FileManipulation(driver);
		clientSearch = new ClientSearch(driver);
		clientSearchResults = new ClientSearchResults(driver);
		
		logger.info("*** Step 1 Actions: - Log into Client Batch Single Demand Statement page with SSO username and password");				
		navigation.navigateToClientBatchSingleDemandStatementPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Client Batch Single Demand Statement page is displayed with correct page title");	
		Assert.assertEquals(clnBatchSingleDemandStmt.pageTitle().getText(), "Batch Single Demand Statement", "       Page title: Batch Single Demand Statement Page is displayed.");

		logger.info("*** Step 2 Actions: - Click on Add button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.addBtn(), 5),"        Add button is displayed.");
		clnBatchSingleDemandStmt.clickAddBtn();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Add Record popup window is displayed");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientIdInput(), 5),"        Add Record popup is displayed.");
		
		logger.info("*** Step 3 Actions: - Find a Client that has no subClient and FK_SUMB_SVC_ID != 0 and due_amt > 0 and has activity for the month via Client Search; Click Client Search button");
		String accountingPeriodEndDtStr = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(71, testDb);
		List<List<String>> clientInfoList = daoManagerXifinRpm.getClnHaveNoSubClnHaveSubmSvcDueAmtFromCLNSUBM(accountingPeriodEndDtStr, testDb);	
		String clnAbbrev = clientInfoList.get(0).get(0);
		String clnName = clientInfoList.get(0).get(1);
		
		clnBatchSingleDemandStmt.clickClnSearchBtn();
		String parentWindow = switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client Search window displayed");
		Assert.assertEquals(clientSearch.clientSearchTitle().getText(), "Client Search");
		
		logger.info("*** Step 4 Actions: - Enter the Client Abbrev in Client ID input field and click Search button");
		Assert.assertTrue(isElementPresent(clientSearch.clientIDInput(), 5),"        Client search ID input is displayed");
		clientSearch.inputClientID(clnAbbrev);
		Assert.assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 5),"        Client search ID input is displayed");
		clientSearch.clickClientSearchSearchButton();
		
		logger.info("*** Step 4 Expected Results: - Verify that the Client Search Results window displayed");
		Assert.assertEquals(clientSearchResults.clientSearchResultTitle().getText(), "Client Search Results");
		
		logger.info("*** Step 5 Actions: - Select the Client ID in the Search Results window");
		Assert.assertTrue(isElementPresent(clientSearchResults.clientSearchResultsTblClientID(2, 2), 5),"        ClientID in search result is displayed");
		clientSearchResults.clickClientSearchTblClientID(2, 2);
		
		switchToParentWin(parentWindow);		
		
		logger.info("*** Step 5 Expected Results: - Verify that the Client is loaded properly in Add Record window");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.clientNameText(), 5),"        Client Name field is displayed.");
		Assert.assertEquals(clnBatchSingleDemandStmt.clientNameText().getAttribute("value"), clnName, "       Client Name " + clnName + " should be displayed.");	
		
		logger.info("*** Step 6 Actions: - Click on OK button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.okBtn(), 5),"        OK button is displayed.");
		clnBatchSingleDemandStmt.clickOkBtn();
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added to the Batch Clients table");
		assertTrue(isElementPresent(clnBatchSingleDemandStmt.batchClientsTable(), 5));
		assertTrue(isTextExistsInWebTable(clnBatchSingleDemandStmt.batchClientsTable(), clnAbbrev),"        Client ID " + clnAbbrev + " should show in Batch Clients table.");
		
		logger.info("*** Step 7 Actions: - Click on Generate Statement button");
		Assert.assertTrue(isElementPresent(clnBatchSingleDemandStmt.generateStmBtn(), 5),"        Generate Statement button is displayed.");
		clnBatchSingleDemandStmt.clickGenerateStmBtn();		
		clnBatchSingleDemandStmt.isSaveDone();		
		closeAlertAndGetItsText(true);				
	
		logger.info("*** Step 7 Expected Results: - Verify that Client Statements will be generated for the updated Client");
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		
		List<String> clientSubmInfoList = daoManagerXifinRpm.getClnBtchFromSUBMFILEByClientId(clnAbbrev,testDb);
		String dir = clientSubmInfoList.get(1).trim().replaceAll("[^0-9]", "");
		String fileLocation = dirBase + dir + File.separator;
		String clnStmtFileName = clientSubmInfoList.get(2).trim();
		File fGenerated = new File(fileLocation + clnStmtFileName);
		Assert.assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");		
		
		logger.info("*** Step 7 Expected Results: - Verify that more than one Client Statements will be generated");
		String currDt = timeStamp.getCurrentDate();
		List<List<String>> submFileInfoList = daoManagerXifinRpm.getSubmFileInfoFromSUBMFILEByCreatDtProcessedDt(currDt, currDt, testDb);
		List<String> fileSeqIdList = new ArrayList<>();
		for (List<String> strings : submFileInfoList) {
			fileSeqIdList.add(strings.get(0));
		}
		
		Assert.assertTrue(fileSeqIdList.size()>1, "       More than one documents should be generated.");

		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fGenerated = new File(fileLocation + clnStmtFileName);
			assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + clnStmtFileName + " should be generated in " + fileLocation + " folder.");
		}
		
		logger.info("*** Step 8 Actions: - Clear test data");
		for (String s : fileSeqIdList) {
			clnStmtFileName = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getFilename().trim();
			dir = submissionDao.getSubmFileBySubmFileSeqId(Integer.parseInt(s)).getDir().trim().replaceAll("[^0-9]", "");
			fileLocation = dirBase + dir + File.separator;

			fileManipulation.deleteFile(fileLocation, clnStmtFileName);
			daoManagerXifinRpm.deleteSubmFileFromSUBMFileBySubmFileSeqId(s, testDb);
		}			

	}
	
	@Test(priority = 1, description = "Verify Help file can be opened properly")
	public void testXPR_631() throws Exception {
		logger.info("===== Testing - testXPR_631 =====");    
		clnBatchSingleDemandStmt = new ClientBatchSingleDemandStatement(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);
		fileManipulation = new FileManipulation(driver);
		
		logger.info("*** Step 1 Actions: - Log into Client Batch Single Demand Statement page with SSO username and password");				
		navigation.navigateToClientBatchSingleDemandStatementPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Client Batch Single Demand Statement page is displayed with correct page title");	
		Assert.assertEquals(clnBatchSingleDemandStmt.pageTitle().getText(), "Batch Single Demand Statement", "       Page title: Batch Single Demand Statement Page is displayed.");

		logger.info("*** Step 2 Actions: - Click Help button");
		clnBatchSingleDemandStmt.clickHelpBtn();
		
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Help file can be opened properly");
		String helpURL = driver.getCurrentUrl();		
		Assert.assertTrue(helpURL.contains("p_batch_single_demand_statement.htm"), "        Batch Single Demand Statement help file should be displayed.");		
		Assert.assertTrue(helpFileTitle().getText().contains("Batch Single Demand Statement"), "        Batch Single Demand Statement help file should be displayed.");
		
	}

}	
