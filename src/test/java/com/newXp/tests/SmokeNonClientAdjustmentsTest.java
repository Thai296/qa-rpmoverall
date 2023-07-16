package com.newXp.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnAdj.AccnAdj;
import com.overall.menu.MenuNavigation;
import com.overall.payment.adjustments.nonClientAdjustments.NonClientAdjustments;
import com.overall.utils.AccnDetailUtils;
import com.overall.utils.NonClientAdjustmentsUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.*;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import com.mbasys.mars.persistance.SystemSettingMap;


import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SmokeNonClientAdjustmentsTest extends SeleniumBaseTest {

	private NonClientAdjustments nonClientAdjustments;
	private NonClientAdjustmentsUtils nonClientAdjUtils;
	private RandomCharacter randomCharacter;
	
	private AccnDetailUtils accessionUtils;
	private TestDataSetup testDataSetup;
	private MenuNavigation navigation;
	private String version = System.getProperty("version");
	private String buildType = System.getProperty("buildType");

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
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Non-Client Adjustments-Upload file with Adj $ < bill $")
	@Parameters({"email", "password","project","testSuite","testCase","propLevel","propName","wsUsername","wsPassword", "xapEnv", "eType1", "eType2", "engConfigDB"})
	public void testXPR_499(String email, String password, String project, String testSuite,String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType1, String eType2, String engConfigDB) throws Exception {
    	logger.info("===== Testing - testXPR_499 =====");    
    	
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
    	nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
    	randomCharacter = new RandomCharacter(driver);
		navigation = new MenuNavigation(driver, config);
		accessionUtils = new AccnDetailUtils(driver, config);

		String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, false);

		logger.info("*** Expected Results: - Ensure the Accession is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");
    	    	
    	logger.info("*** Actions: - Create CSV upload file");
		List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+uploadFileName;
    	float adjCost = 1;
    	List<Float> adjCostList = Arrays.asList(new Float[]{adjCost});
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjCost;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    	
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjCostList, anticipateNewList, filePath);
    	    	
    	logger.info("*** Actions: - Navigate to Xifinportal Non-Client Adjustments page");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Expected Results: - Verify that user is logged in");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Actions: - Click on Upload button");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadBtn(), 5));
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Expected results: - Upload Documents popup is displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadUploadDocumentBtn(), 5),"        Upload Documents popup is displayed");
		
		logger.info("*** Actions: - Upload csv file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
		waitUntilElementIsNotVisible(nonClientAdjustments.uploadUploadDocumentBtn(), 50);

		logger.info("*** Expected results: - Verify that a new row is added into table with correct data");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsDisplayValidData(nonClientAdjustments),"        All rows is displayed with correct data");
		String dueAmt = accessionDao.getAccn(accnId).getDueAmtAsMoney().toString();
		String currentDollars = nonClientAdjustments.getListRowDataInColumn(13).get(0);
		Assert.assertEquals(nonClientAdjUtils.getNumberFromText(dueAmt), nonClientAdjUtils.getNumberFromText(currentDollars));
		Assert.assertEquals(adjPyrID, nonClientAdjustments.getListRowDataInColumn(7),"        Adj PyrId is displayed correctly");
		Assert.assertEquals(adjCDList, nonClientAdjustments.getListRowDataInColumn(8),"        Adj Cd is displayed correctly");
				
		logger.info("*** Actions: - Click Post button");
		List<String> listNewDollars = nonClientAdjustments.getListRowDataInColumn(14);
		logger.info("        listNewDollars= "+listNewDollars);
		List<String> listAccnId = nonClientAdjustments.getListRowDataInColumn(2);
		logger.info("        listAccnId = " + listAccnId);
		Assert.assertTrue(isElementPresent(nonClientAdjustments.postBtn(), 5));
		nonClientAdjustments.clickPostBtn();
		
		logger.info("*** Expected results: - Verify that proper data is saved into DB");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsSavedCorrect(listAccnId, listNewDollars));
		
		logger.info("*** Actions: - Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath)){
			nonClientAdjUtils.deleteFile(filePath);
		}		
    }	
	
	@SuppressWarnings("unchecked")
	@Test(priority=1,description="Non-Client Adjustments-Upload twice times for the same file")
	@Parameters({"email","password","project","testSuite","testCase","propLevel","propName","wsUsername","wsPassword", "xapEnv", "eType1", "eType2", "engConfigDB"})
	public void testXPR_500(String email, String password, String project, String testSuite, String testCase, String propLevel,String propName, String wsUsername, String wsPassword, String xapEnv, String eType1, String eType2, String engConfigDB) throws Exception{
		logger.info("==== Testing - testXPR_500 ====");
		navigation = new MenuNavigation(driver, config);
		accessionUtils = new AccnDetailUtils(driver, config);

		String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, false);

		logger.info("*** Expected Results: - Ensure the Accession is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");
    	    	
    	logger.info("*** Actions: Create CSV upload file");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
    	nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
    	randomCharacter = new RandomCharacter(driver);
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+uploadFileName;
    	float adjCost = 1;
    	List<Float> adjCostList = Arrays.asList(new Float[]{adjCost});
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjCost;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    	
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjCostList, anticipateNewList, filePath);
    	    	
    	logger.info("*** Actions: - Navigate to Xifinportal Non-Client Adjustments page");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Expected Results: - Verify that user is logged in");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Action: Click on Upload button");
		wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.uploadBtn()));
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Expected result: Upload Documents popup is displayed");
		wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.uploadUploadDocumentBtn()));
		
		logger.info("*** Action: Upload csv file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));
		
		logger.info("*** Expected result: Verify that a new row is added into table with correct data");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsDisplayValidData(nonClientAdjustments),"        All rows is displayed with correct data");
		String dueAmt = accessionDao.getAccn(accnId).getDueAmtAsMoney().toString();
		String currentDollars = nonClientAdjustments.getListRowDataInColumn(13).get(0);
		Assert.assertEquals(nonClientAdjUtils.getNumberFromText(dueAmt), nonClientAdjUtils.getNumberFromText(currentDollars));
		Assert.assertEquals(adjPyrID, nonClientAdjustments.getListRowDataInColumn(7),"        Adj PyrId is displayed correctly");
		Assert.assertEquals(adjCDList, nonClientAdjustments.getListRowDataInColumn(8),"        Adj Cd is displayed correctly");
				
		logger.info("*** Action: Click Post button");
		List<String> listNewDollars = nonClientAdjustments.getListRowDataInColumn(14);
		logger.info("        listNewDollars= "+listNewDollars);
		List<String> listAccnId = nonClientAdjustments.getListRowDataInColumn(2);
		logger.info("        listAccnId = " + listAccnId);
		nonClientAdjustments.clickPostBtn();
		
		logger.info("*** Expected result: Verify that proper data is saved into DB");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsSavedCorrect(listAccnId, listNewDollars));
		
		logger.info("*** Action: Upload the same file again");
		nonClientAdjustments.clickUploadBtn();
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));
		
		logger.info("*** Expected result: Verify that a new row is added into table with correct data");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsDisplayValidData(nonClientAdjustments),"        New row is added into the table");
		List<String> listCurrent = nonClientAdjustments.getListRowDataInColumn(13);
		Assert.assertEquals(nonClientAdjUtils.getNumberFromText(listNewDollars.get(0)), nonClientAdjUtils.getNumberFromText(listCurrent.get(0)),"        Current$ = previous New$");
		
		logger.info("*** Action: Click Post button");
		List<String> listNewDollarsNew= nonClientAdjustments.getListRowDataInColumn(14);
		List<String> listAccnNew = nonClientAdjustments.getListRowDataInColumn(2);
		nonClientAdjustments.clickPostBtn();
		
		logger.info("*** Expected result:  Verify that proper data is saved into DB");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsSavedCorrect(listAccnNew, listNewDollarsNew),"        New data are saved into DB ");
		
		logger.info("*** Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath)){
			nonClientAdjUtils.deleteFile(filePath);
		}
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Upload file with multi rows data")
	@Parameters({"email", "password","documentFile","project", "testSuite", "testCase", "propLevel", "propName",  "wsUsername", "wsPassword", "xapEnv", "eType1", "eType2", "engConfigDB"})
	public void testXPR_501(String email, String password,String documentFile, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType1, String eType2, String engConfigDB) throws Exception {
		logger.info("===== Testing - testXPR_501 =====");
		navigation = new MenuNavigation(driver, config);
		accessionUtils = new AccnDetailUtils(driver, config);

        String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, false);

		logger.info("*** Expected Results: - Ensure the Accession1 is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");
        String accnId2 = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, true);

        logger.info("*** Expected Results: - Ensure the Accession2 is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId2).getStaId(), 21, "        The Accession Status for " + accnId2 + " should be Priced (21).");
	   	nonClientAdjustments = new NonClientAdjustments(driver, wait);
    	nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
    	randomCharacter = new RandomCharacter(driver);

    	logger.info("*** Actions: Create CSV upload file with multiple lines");
    	List<String> accnList = Arrays.asList(new String[] {accnId, accnId2});
    	File file = new File(System.getProperty("user.dir")).getParentFile();
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+uploadFileName;
    	float adjCost = 1;
    	float adjCost2 = 2;
    	List<Float> adjCostList = Arrays.asList(new Float[]{adjCost, adjCost2});
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	String pyrId2 = payorDao.getPyrFromAccnPyrByAccnId(accnId2).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId, pyrId2});
    	String adjCD = "COL";
    	String adjCD2 = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD, adjCD2});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	String billAmt2 = accessionDao.getAccn(accnId2).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt, billAmt2});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjCost;
    	float anticipatedNew2 = Float.parseFloat(billAmt2) + adjCost2;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew, anticipatedNew2});    	
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjCostList, anticipateNewList, filePath);
    	    	
    	logger.info("*** Actions: - Navigate to Xifinportal Non-Client Adjustments page");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Expected Results: - Verify that user is logged in");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Action: Click on Upload button");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Expected result: Upload Documents popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.uploadUploadDocumentBtn()));
		
		logger.info("*** Action: Upload csv file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
        waitUntilElementIsNotVisible(nonClientAdjustments.uploadUploadDocumentBtn(), 50);
		
		logger.info("*** Expected Result: - Verify that Multiple rows in the file are displayed in table with correct data");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsDisplayValidData(nonClientAdjustments),"        All rows must be displayed with correct data");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.postBtn()));//////////////////
		
		logger.info("*** Actions: - Click Post button");
		List<String> listNewDollars = nonClientAdjustments.getListRowDataInColumn(14);
		List<String> listAccnIDInTable = nonClientAdjustments.getListRowDataInColumn(2);
		nonClientAdjustments.clickPostBtn();

		logger.info("*** Expected Result: - Data in Multiple rows are saved to database with Due Amount = New$ column");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsSavedCorrect(listAccnIDInTable,listNewDollars),"        All rows should be saved to database correctly.");
		
		logger.info("*** Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath)){
			nonClientAdjUtils.deleteFile(filePath);
		}	
	}
	
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Upload new file with invalid AccnID")
	public void testXPR_502() throws Exception {
		logger.info("===== Testing - testXPR_502 =====");    

    	logger.info("*** Step 1 Actions: - Create a csv file with Non-existing accession");
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter(driver);
		String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";		
		String filePath = (new File(System.getProperty("user.dir")).getParentFile() + File.separator + "src" + File.separator + "test" + File.separator + "resources");
		List<String> listAccnID = Arrays.asList(new String[]{"NONEXISTINGACCN" + randomCharacter.getRandomAlphaString(6)});
		List<Float> listAdjAmt = Arrays.asList(new Float[]{(float) 10});
		nonClientAdjUtils.creatUploadNonClnAdjInvalidAccnIDFile(listAccnID, listAdjAmt, listAdjAmt, filePath+File.separator+uploadFileName); // Create file before upload
		
		logger.info("*** Step 1 Expected Results: - Verify that the file is available for testing");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " + filePath+File.separator+uploadFileName + " should be available.");
		
		logger.info("*** Step 2 Actions: - Log into RPM with SSO username and password");
		nonClientAdjustments = new NonClientAdjustments(driver, wait);
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();

		logger.info("*** Step 2 Expected Results: - Verify that the Non-Client Adjustments page is displayed");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(isElementPresent(nonClientAdjustments.pageTitleLb(), 5),"        Page title should be displayed");
		Assert.assertTrue(nonClientAdjustments.pageTitleLb().getText().equalsIgnoreCase("Non-Client Adjustments"),"        Page title should be displayed correctly.");
		
		logger.info("*** Step 3 Actions: - Click Upload button");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadBtn(), 5),"        Upload button should be displayed");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Step 3 Expected Result: - Verify that the Upload Documents popup displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.chooseFileBtn(), 5),"        Upload file button should be displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadUploadDocumentBtn(), 5),"        Upload button should be displayed");
		
		logger.info("*** Step 4 Actions: - Select the CSV file with invalid Accession ID. Click Upload button");
		uploadFile(nonClientAdjustments.xfnUploaderFile(), uploadFileName);//Select file have multi row with valid data
		nonClientAdjustments.clickUploadUploadDocumentBtn();//Click upload button
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));

		logger.info("*** Step 4 Expected Result: - Verify that the row is displayed as Deleted row. Error message 'AccnID is invalid' is displayed");
		Assert.assertTrue(Arrays.asList(nonClientAdjustments.rowTableNonClientAdj(2).getAttribute("class").split(" ")).contains("rowMarkedForDelete"),"        The 1st row in Non-Client Adjustments table should be marked as Deleted.");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.messagesReturnedAreabox(),5),"        Error message must be display");
		Assert.assertTrue(nonClientAdjustments.messagesReturnedAreabox().getText().equalsIgnoreCase("Error loading row 1: ID "+listAccnID.get(0)+" not found;"),"       Error message should be display correctly"); 

		logger.info("*** Step 5 Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}
	}
	
	@Test(priority = 1, description = "Upload a file contains > 10 rows and verify the pager navigation buttons")
	public void testXPR_503() throws Exception {
		logger.info("===== Testing - testXPR_503 =====");    	
		
		logger.info("*** Step 1 Actions: - Get more than 10 Priced Accession from DB");
		List<String> listAccnID = new ArrayList<>();
		List<Accn> accnList = accessionDao.getPricedThirdPartyPyrAccn(16);
		for (Accn strings : accnList) {
			listAccnID.add(strings.getAccnId());
		}    	
		//System.out.print("listAccnID= " + listAccnID);
		
		logger.info("*** Step 2 Actions: - Create file data with selected Accessions");		
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter(driver);
		String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
		String filePath = (new File(System.getProperty("user.dir")).getParentFile() + File.separator + "src" + File.separator + "test" + File.separator + "resources");		
		nonClientAdjUtils.creatUploadNonClnAdjFile(listAccnID, 10, filePath+File.separator+uploadFileName); // Create file before upload
		
		logger.info("*** Step 2 Expected Results: - Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " +  filePath+File.separator+uploadFileName + " should be available.");
		
		logger.info("*** Step 3 Actions: - Log into RPM with SSO username and password");
		nonClientAdjustments = new NonClientAdjustments(driver, wait);  
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Non-Client Adjustment page is displayed");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(isElementPresent(nonClientAdjustments.pageTitleLb(), 5),"        Page title should be displayed");
		Assert.assertTrue(nonClientAdjustments.pageTitleLb().getText().equalsIgnoreCase("Non-Client Adjustments"),"        Page title should be displayed correct");
		
		logger.info("*** Step 4 Actions: - Click Upload button");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadBtn(), 5),"        Upload button should be displayed.");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Step 4 Expected Result: - Verify that Upload Documents popup displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.chooseFileBtn(), 5),"        Upload file button should be displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadUploadDocumentBtn(), 5),"        Upload button should be displayed");
		
		logger.info("*** Step 5 Actions: - Select the CSV file and click Upload button");
		uploadFile(nonClientAdjustments.xfnUploaderFile(), uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
		Thread.sleep(2000);

		logger.info("*** Step 5 Expected Result: - Verify that multiple rows in the file are displayed in the Non-Client Adjustments table that has 2 pages");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsDisplayValidData(nonClientAdjustments),"        All rows should be displayed with correct data");
		Assert.assertEquals(Integer.parseInt(nonClientAdjustments.totalPageLbl().getText()),2,"        Two pages should show.");
				
		logger.info("*** Step 6 Actions: Click First, Previous, Next, and Last pager navigation buttons");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.firstPageBtn(), 5),"        First page button should show");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.previousPageBtn(), 5),"        Previous page button should show");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.nextPageBtn(), 5),"        Next page button should show");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.lastPageBtn(), 5),"        Last page button should show");
		nonClientAdjustments.clickLastPage();
		String cLast     = nonClientAdjustments.currentPageNumber().getAttribute("value");
		
		nonClientAdjustments.clickPreviousPage();
		String cPrevious = nonClientAdjustments.currentPageNumber().getAttribute("value");
		
		nonClientAdjustments.clickNextPage();
		String cNext     = nonClientAdjustments.currentPageNumber().getAttribute("value");
		
		nonClientAdjustments.clickFirstPage();
		String cFirst    = nonClientAdjustments.currentPageNumber().getAttribute("value");
		String totalPage = nonClientAdjustments.totalPageNumber().getText();
		
		logger.info("*** Step 6 Expected Results: Verify that the pager navigation buttons are taking the user go to the correct page");
		Assert.assertEquals(cLast,totalPage, "        Page number should be "+""+totalPage+" when clicked Last page button");
		Assert.assertEquals(Integer.parseInt(cPrevious),Integer.parseInt(totalPage)-1, "        Page number should be "+""+(Integer.parseInt(totalPage)-1)+" when clicked Previous page button");
		Assert.assertEquals(cNext,totalPage, "        Page number should be "+""+totalPage+" when clicked Next page button");
		Assert.assertEquals(cFirst,"1", "        Page number should be 1 when clicked First page button");				
		
		logger.info("*** Step 7 Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}
	}
	
	@Test(priority = 1, description = "Upload file without any content")
	public void testXPR_504() throws Exception {
		logger.info("===== Testing - testXPR_504 =====");

		logger.info("*** Step 1 Actions: - Create an empty CSV file");
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter(driver);
		String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
		String filePath = (new File(System.getProperty("user.dir")).getParentFile() + File.separator + "src" + File.separator + "test" + File.separator + "resources");
		nonClientAdjUtils.createEmptyFile(filePath+File.separator+uploadFileName); // Create file before upload
		
		logger.info("*** Step 1 Expected Results: - Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV File: " + filePath+File.separator+uploadFileName + "should be available.");

		logger.info("*** Step 2 Actions: - Log into RPM with SSO username and password");
		nonClientAdjustments = new NonClientAdjustments(driver, wait);   
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Non-Client Adjustments page is displayed");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(isElementPresent(nonClientAdjustments.pageTitleLb(), 5),"        Page title should be displayed");
		Assert.assertTrue(nonClientAdjustments.pageTitleLb().getText().equalsIgnoreCase("Non-Client Adjustments"),"        Page title should be displayed correct");
		
		logger.info("*** Step 3 Actions: - Click Upload button");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadBtn(), 5),"        Upload button should be displayed");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Step 3 Expected Result: - Verify that the Upload Documents popup displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.chooseFileBtn(), 5),"        Upload file button should be displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadUploadDocumentBtn(), 5),"        Upload button should be displayed");
		
		logger.info("*** Step 4 Actions: - Select the empty CSV file and click Upload button");
		uploadFile(nonClientAdjustments.xfnUploaderFile(), uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
		waitUntilElementIsNotVisible(nonClientAdjustments.uploadUploadDocumentBtn(), 50);

		logger.info("*** Step 4 Expected Result: - Verify that an error message was displayed");
		Assert.assertTrue(nonClientAdjustments.errMessage().contains("Selected file is empty!"), "        Error message: 'Selected file is empty!' should show.");
		
		logger.info("*** Step 5 Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}
	}	
	
	@Test(priority = 1, description = "Verify filter functions")
	public void testXPR_513() throws Exception {
		logger.info("===== Testing - testXPR_513 =====");    	

		logger.info("*** Step 1 Actions: - Get more than 10 Priced Accession from DB");
		List<String> listAccnID = new ArrayList<>();
		List<Accn> accnList = accessionDao.getPricedThirdPartyPyrAccn(16);
		for (Accn accn : accnList) {
			listAccnID.add(accn.getAccnId());
		}    	

		logger.info("*** Step 2 Actions: - Create file data with selected Accessions");		
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter(driver);
		String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
		String filePath = (new File(System.getProperty("user.dir")).getParentFile() + File.separator + "src" + File.separator + "test" + File.separator + "resources");		
		nonClientAdjUtils.creatUploadNonClnAdjFile(listAccnID, 10, filePath+File.separator+uploadFileName);
		
		logger.info("*** Step 2 Expected Results: - Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " +  filePath+File.separator+uploadFileName + " should be available.");
		
		logger.info("*** Step 3 Actions: - Log into RPM with SSO username and password");
		nonClientAdjustments = new NonClientAdjustments(driver, wait);  
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();

		logger.info("*** Step 3 Expected Results: - Verify that the Non-Client Adjustment page is displayed");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(isElementPresent(nonClientAdjustments.pageTitleLb(), 5),"        Page title should be displayed");
		Assert.assertTrue(nonClientAdjustments.pageTitleLb().getText().equalsIgnoreCase("Non-Client Adjustments"),"        Page title should be displayed correct");
		
		logger.info("*** Step 4 Actions: - Click Upload button");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadBtn(), 5),"        Upload button should be displayed.");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Step 4 Expected Result: - Verify that Upload Documents popup displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.chooseFileBtn(), 5),"        Upload file button should be displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadUploadDocumentBtn(), 5),"        Upload button should be displayed");
		
		logger.info("*** Step 5 Actions: - Select the CSV file and click Upload button");
		uploadFile(nonClientAdjustments.xfnUploaderFile(), uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
		Thread.sleep(2000);

		logger.info("*** Step 5 Expected Result: - Verify that multiple rows in the file are displayed in the Non-Client Adjustments table that has 2 pages");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsDisplayValidData(nonClientAdjustments),"        All rows should be displayed with correct data");
		Assert.assertEquals(Integer.parseInt(nonClientAdjustments.totalPageLbl().getText()),2,"        Two pages should show.");		
		
		logger.info("*** Step 6 Actions: Enter an existing Accession ID in the list in Accn ID column");
		assertTrue(isElementPresent(nonClientAdjustments.accnIDFilterInput(), 5),"        Accn ID filter field should be displayed");
		String searchAccnId = listAccnID.get(5);
		nonClientAdjUtils.inputText(nonClientAdjustments.accnIDFilterInput(), searchAccnId, true);

		logger.info("*** Step 6 Expected Results: - Verify that matched Accession is displayed in the result list");
		assertTrue(isElementPresent(nonClientAdjustments.cellTableNonClientAdj(2, 2), 5),"        Accn ID column should be displayed");
		assertEquals(nonClientAdjustments.cellTableNonClientAdj(2, 2).getText(), searchAccnId,"        The Accn ID: "  + searchAccnId + " should be displayed in the list.");	
		
		logger.info("*** Step 7 Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}
	}	
	
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Upload file have Adjustment $ is negative number")
	@Parameters({"email", "password","project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType1", "eType2", "engConfigDB"})
	public void testXPR_514(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType1, String eType2, String engConfigDB) throws Exception {
		    	
    	logger.info("===== Testing - testXPR_514 =====");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter();
		testDataSetup = new TestDataSetup();
		navigation = new MenuNavigation(driver, config);
		accessionUtils = new AccnDetailUtils(driver, config);
        String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, false);

		logger.info("*** Expected Results: - Ensure the Accession is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");
    	    	
    	logger.info("*** Actions: Create CSV upload file");
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();    	   	
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources";//+File.separator+uploadFileName;
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	 
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
       	float adjAmt = -Float.valueOf(billAmt);
    	List<Float> adjAmtList = Arrays.asList(new Float[]{adjAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjAmt;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    	
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjAmtList, anticipateNewList, filePath+File.separator+uploadFileName);
			
		logger.info("*** Expected Result:  Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " + filePath+File.separator+uploadFileName + "shouls be available.");
		
    	logger.info("*** Actions: - Navigate to Xifinportal Non-Client Adjustments page");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Expected Results: - Verify that user is logged in");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Action: Click Upload button");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Expected result: Upload Documents popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.uploadUploadDocumentBtn()));
		
		logger.info("*** Action: Upload CSV file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));

		logger.info("*** Expected result: Verify that a new row is added into table with correct data");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsDisplayValidData(nonClientAdjustments),"        All rows is displayed with correct data");
		String dueAmt = accessionDao.getAccn(accnId).getDueAmtAsMoney().toString();
		String currentDollars = nonClientAdjustments.getListRowDataInColumn(13).get(0);
		Assert.assertEquals(nonClientAdjUtils.getNumberFromText(dueAmt), nonClientAdjUtils.getNumberFromText(currentDollars));
		Assert.assertEquals(adjPyrID, nonClientAdjustments.getListRowDataInColumn(7),"        Adj PyrId is displayed correctly");
		Assert.assertEquals(adjCDList, nonClientAdjustments.getListRowDataInColumn(8),"        Adj Cd is displayed correctly");
				
		logger.info("*** Action: Click Post button");
		List<String> listNewDollars = nonClientAdjustments.getListRowDataInColumn(14);
		logger.info("        listNewDollars= "+listNewDollars);
		List<String> listAccnId = nonClientAdjustments.getListRowDataInColumn(2);
		logger.info("        listAccnId = " + listAccnId);
		nonClientAdjustments.clickPostBtn();
		
		logger.info("*** Expected result: Verify that proper data is saved into DB");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsSavedCorrect(listAccnId, listNewDollars));
		
		logger.info("*** Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}
	}		
	
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Error Message shows when Cash Adjustment is more than Payor Payment")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType1", "eType2", "engConfigDB"})
	public void testXPR_642(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType1, String eType2, String engConfigDB) throws Exception {
		    	
    	logger.info("===== Testing - testXPR_642 =====");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter();
		testDataSetup = new TestDataSetup();
		navigation = new MenuNavigation(driver, config);
		accessionUtils = new AccnDetailUtils(driver, config);
        String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, false);

		logger.info("*** Expected Results: - Ensure the Accession is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");
    	    	
    	logger.info("*** Actions: Create CSV upload file");
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();    	   	
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources";//+File.separator+uploadFileName;
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
       	float adjAmt = -Float.valueOf(billAmt)-200;
    	List<Float> adjAmtList = Arrays.asList(new Float[]{adjAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjAmt;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjAmtList, anticipateNewList, filePath+File.separator+uploadFileName);
			
		logger.info("*** Expected Result:  Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " + filePath+File.separator+uploadFileName + "should be available.");
		
    	logger.info("*** Actions: - Navigate to Xifinportal Non-Client Adjustments page");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Expected Results: - Verify that user is logged in");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Action: Click Upload button");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Expected result: Upload Documents popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.uploadBtn()));
		
		logger.info("*** Action: Upload CSV file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));

		logger.info("*** Expected Results: - Verify that a new rows is added into table as deleted row");
		Assert.assertTrue(Arrays.asList(nonClientAdjustments.rowTableNonClientAdj(2).getAttribute("class").split(" ")).contains("rowMarkedForDelete"),"        The 1st row in Non-Client Adjustments table should be marked as Deleted.");
		
		logger.info("*** Expected Results: - Verify that an error message shows with information about the Non Cash Adjustment can't exceed payor payment");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.messagesReturnedAreabox(),5),"        Error message must be display");		
		String [] errMsgList = nonClientAdjustments.msgReturnedTxt("0").split(", "); 

		String adjAmtInErrMsg = String.format("%.2f", adjAmt);
		assertTrue(errMsgList[0].trim().contains("Non cash adjustment " + adjAmtInErrMsg + " cannot exceed payor payments 0.00"),"        Error Message: Error loading row 1: Non cash adjustment "+ adjAmtInErrMsg +" cannot exceed payor payments 0.00 should show.");
		
		float dueAmtInErrMsg= nonClientAdjUtils.getNumberFromText(errMsgList[1].split(" ")[2]);
		assertEquals(dueAmtInErrMsg,Float.parseFloat(billAmt),"        Error Message: due amount " +  billAmt + " should show.");

	    String billAmtInTable = nonClientAdjustments.cellTableNonClientAdj(6,2).getText();
	    assertEquals(errMsgList[2].trim(),"and bill price "+billAmtInTable+";","        Error Message: bill price " + billAmtInTable + " should show.");
	    
		logger.info("*** Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}			  
	}	
	
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Upload a CSV file that has invalid Adjustment Payor")
	public void testXPR_515() throws Exception {

		logger.info("===== Testing - testXPR_515 =====");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
    	nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
    	randomCharacter = new RandomCharacter(driver);
 
    	logger.info("*** Step 1 Actions: Create CSV upload file with a non-existing payor");
		List<Accn> pricedAccnList = accessionDao.getPricedThirdPartyPyrAccn(2);
		String accnId = pricedAccnList.get(0).getAccnId();    	    	    	    	
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();    	
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+uploadFileName;
    	float adjCost = 1;
    	List<Float> adjCostList = Arrays.asList(new Float[]{adjCost});
    	String PyrID = "NONEXISTPYR";
    	List<String> adjPyrID = Arrays.asList(new String[]{PyrID});
    	String adjCD = "COL";//daoManagerPlatform.getRandomAdjCdIdAdjAbbrvFromADJCD(testDb).get(1);
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjCost;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    	
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjCostList, anticipateNewList, filePath);
    	
		logger.info("*** Step 1 Expected Result:  Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath),"        CSV file: " + filePath + "should be available.");
		    	    	
    	logger.info("*** Step 2 Actions: - Log into Non-Client Adjustments screen with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();

		logger.info("*** Step 2 Expected Results: - Verify that user is logged in");	
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Step 3 Action: Click Upload button");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadBtn(), 5));
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Step 3 Expected result: Verify that the Upload Documents popup is displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadUploadDocumentBtn(), 5),"        Upload Documents popup is displayed");
		
		logger.info("*** Step 4 Action: Upload a CSV file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
		waitUntilElementIsNotVisible(nonClientAdjustments.uploadUploadDocumentBtn(), 10);

		logger.info("*** Step 4 Expected Results: - Verify that a new rows is added into table as a deleted row");
		Assert.assertTrue(Arrays.asList(nonClientAdjustments.rowTableNonClientAdj(2).getAttribute("class").split(" ")).contains("rowMarkedForDelete"),"        The 1st row in Non-Client Adjustments table should be marked as Deleted.");
				
		logger.info("*** Step 4 Expected Results: - Verify that an error message shows with information about can't find the Payor");			
		assertTrue(isElementPresent(nonClientAdjustments.messagesReturnedAreabox(), 5),"         Message areabox is displayed");
		assertEquals(nonClientAdjustments.messagesReturnedAreabox().getText(),"Error loading row 1: Error locating payor "+PyrID+";","         Error Message: " + "Error loading row 1: Error locating payor "+PyrID+"; should be displayed.");
		
		logger.info("*** Step 5 Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath)){
			nonClientAdjUtils.deleteFile(filePath);
		}		
    }
	
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Update non-Client Adjustments with valid Adj$ and Print is checked")
	@Parameters({"email", "password","project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType1", "eType2", "engConfigDB"})
	public void testXPR_505(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType1, String eType2, String engConfigDB) throws Exception {
		    	
    	logger.info("===== Testing - testXPR_505 =====");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter();
		testDataSetup = new TestDataSetup();
		navigation = new MenuNavigation(driver, config);
		accessionUtils = new AccnDetailUtils(driver, config);
        String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, false);

		logger.info("*** Expected Results: - Ensure the Accession is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");
    	    	
    	logger.info("*** Actions: Create CSV upload file");
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();    	   	
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources";//+File.separator+uploadFileName;
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
       	float adjAmt = Float.valueOf(billAmt)-1;
    	List<Float> adjAmtList = Arrays.asList(new Float[]{adjAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjAmt;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    	
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjAmtList, anticipateNewList, filePath+File.separator+uploadFileName);
			
		logger.info("*** Expected Result:  Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " + filePath+File.separator+uploadFileName + "should be available.");
		
    	logger.info("*** Actions: - Navigate to Xifinportal Non-Client Adjustments page");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Expected Results: - Verify that user is logged in");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Action: Click Upload button");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Expected result: Upload Documents popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.uploadBtn()));

		logger.info("*** Action: Upload CSV file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));
		
		logger.info("*** Expected Results: Verify that a new row is added into Non-Client Adjustments table with correct data: New$ = Adj$ + Current$, Current$ = Due_Amt");
		String accnIDCell = nonClientAdjustments.getTextCellTableNonClientAdj(2, 2);
		String newCell = nonClientAdjustments.getTextCellTableNonClientAdj(14, 2);
    	String currentCell = nonClientAdjustments.getTextCellTableNonClientAdj(13, 2);
    	String adjCell = nonClientAdjustments.getTextCellTableNonClientAdj(9, 2);
    	Float total = nonClientAdjUtils.getNumberFromText(adjCell) + nonClientAdjUtils.getNumberFromText(currentCell);
    	String due_amt = nonClientAdjUtils.formatDecimalPoint(Double.parseDouble(accessionDao.getAccn(accnIDCell).getDueAmtAsMoney().toString()));
    	
    	Assert.assertEquals(nonClientAdjUtils.getNumberFromText(newCell), total, "        The value in New should equal to Adj + Current.");
    	Assert.assertEquals(currentCell, due_amt, "        The value in Current should equal to dueAmt in the Accn table.");
    	
		logger.info("*** Actions: Select the new row. Click Edit button");
		clickHiddenPageObject(nonClientAdjustments.cellTableNonClientAdj(2, 2), 0);
		nonClientAdjustments.clickEditBtn();

		logger.info("*** Expected Results: Verify that the Edit popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.editPopupTitle()));
		
		logger.info("*** Actions: Update Adj$. Enter Adj comment. Check Print checkbox. Click OK button");
		String newAdjAmt = String.valueOf(adjAmt + 0.5);
		String newAdjComment = "Auto comment " + accnId;
		nonClientAdjustments.adjAmtInput(newAdjAmt);
		nonClientAdjustments.adjCommentInput(newAdjComment);
		nonClientAdjustments.clickPrintChkBox();
		nonClientAdjustments.clickOKBtn();
		
		logger.info("*** Expected Result: Verify that the data are updated and displayed properly: New$ = newAdj$ + current$");
		String updatedNewCell = nonClientAdjustments.getTextCellTableNonClientAdj(14, 2);
    	String updatedCurrentCell = nonClientAdjustments.getTextCellTableNonClientAdj(13, 2);
    	Float totalNew = nonClientAdjUtils.getNumberFromText(newAdjAmt) + nonClientAdjUtils.getNumberFromText(updatedCurrentCell);
    	Assert.assertEquals(nonClientAdjUtils.getNumberFromText(updatedNewCell), totalNew, "        The value in New should equal to Adj$ + Current$.");
		Assert.assertEquals(nonClientAdjustments.getTextCellTableNonClientAdj(10, 2), newAdjComment, "        Adj Comment should be displayed.");
		
		logger.info("*** Expected Result: Verify that the Print checkbox is checked in Non-Client Adjustments table");
		Assert.assertTrue(isChecked(nonClientAdjustments.cellTableNonClientAdjChecbox(11, 2)),"        Print checkbox should be checked");
		
		logger.info("*** Actions: Click Post button");
		nonClientAdjustments.clickPostBtn();

		logger.info("*** Expected Result: Verify that the updated data is saved in DB properly");
		String updatedDueAmt = nonClientAdjUtils.formatDecimalPoint(Double.parseDouble(String.valueOf(accessionDao.getAccn(accnId).getDueAmtAsMoney())));
		Assert.assertEquals(updatedNewCell, updatedDueAmt,"        The value in New$ should equal to Due_amt: " + updatedDueAmt);
		
		AccnAdj accnAdjInfo = accessionDao.getAccnAdjByAccnIdAndSeqId(accnId, 1);
		String adjCommentStr = accnAdjInfo.getNote();
		Assert.assertEquals(adjCommentStr, newAdjComment,"        The Adj Comment should be " + newAdjComment);		
		
		boolean isPrintNote = accnAdjInfo.getIsPrintNote();
		Assert.assertTrue(isPrintNote,"        The B_PRINT_NOTE should be True (1).");
		
		logger.info("*** Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Delete an non-client adjustment")
	@Parameters({"email", "password","project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType1", "eType2", "engConfigDB"})
	public void testXPR_507(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType1, String eType2, String engConfigDB) throws Exception {
		    	
    	logger.info("===== Testing - testXPR_507 =====");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter();
		testDataSetup = new TestDataSetup();
		navigation = new MenuNavigation(driver, config);
		accessionUtils = new AccnDetailUtils(driver, config);

        String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, false);

		logger.info("*** Expected Results: - Ensure the Accession is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");
    	    	
    	logger.info("*** Actions: Create CSV upload file");
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();    	   	
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources";//+File.separator+uploadFileName;
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
       	float adjAmt = Float.valueOf(billAmt)-1;
    	List<Float> adjAmtList = Arrays.asList(new Float[]{adjAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjAmt;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    	
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjAmtList, anticipateNewList, filePath+File.separator+uploadFileName);
			
		logger.info("*** Expected Result:  Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " + filePath+File.separator+uploadFileName + "should be available.");
		
    	logger.info("*** Actions: - Navigate to Xifinportal Non-Client Adjustments page");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Expected Results: - Verify that user is logged in");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Action: Click Upload button");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Expected result: Upload Documents popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.uploadBtn()));

		logger.info("*** Action: Upload CSV file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));
		
		logger.info("*** Expected Results: Verify that a new row is added into Non-Client Adjustments table with correct data: New$ = Adj$ + Current$, Current$ = Due_Amt");
		String accnIDCell = nonClientAdjustments.getTextCellTableNonClientAdj(2, 2);
		String newCell = nonClientAdjustments.getTextCellTableNonClientAdj(14, 2);
    	String currentCell = nonClientAdjustments.getTextCellTableNonClientAdj(13, 2);
    	String adjCell = nonClientAdjustments.getTextCellTableNonClientAdj(9, 2);
    	Float total = nonClientAdjUtils.getNumberFromText(adjCell) + nonClientAdjUtils.getNumberFromText(currentCell);
    	String due_amt = nonClientAdjUtils.formatDecimalPoint(Double.parseDouble(accessionDao.getAccn(accnIDCell).getDueAmtAsMoney().toString()));
    	Assert.assertEquals(nonClientAdjUtils.getNumberFromText(newCell), total, "        The value in New should equal to Adj + Current.");
    	Assert.assertEquals(currentCell, due_amt, "        The value in Current should equal to dueAmt in the Accn table.");
    	
		logger.info("*** Actions: Select the new row. Click Edit button");
		clickHiddenPageObject(nonClientAdjustments.cellTableNonClientAdj(2, 2), 0);
		nonClientAdjustments.clickEditBtn();

		logger.info("*** Expected Results: Verify that the Edit popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.editPopupTitle()));
		
		logger.info("*** Actions: Update Adj$. Enter Adj comment. Check Print checkbox. Check Delete Row checkbox. Click OK button");
		String newAdjAmt = String.valueOf(adjAmt + 0.5);
		String newAdjComment = "Auto comment " + accnId;
		nonClientAdjustments.adjAmtInput(newAdjAmt);
		nonClientAdjustments.adjCommentInput(newAdjComment);
		nonClientAdjustments.clickPrintChkBox();
		nonClientAdjustments.clickDeleteRowChkBox();
		nonClientAdjustments.clickOKBtn();
		
		logger.info("*** Expected Result: Verify that the data are updated and displayed properly: New$ = newAdj$ + current$");
		String updatedNewCell = nonClientAdjustments.getTextCellTableNonClientAdj(14, 2);
    	String updatedCurrentCell = nonClientAdjustments.getTextCellTableNonClientAdj(13, 2);
    	Float totalNew = nonClientAdjUtils.getNumberFromText(newAdjAmt) + nonClientAdjUtils.getNumberFromText(updatedCurrentCell);
    	Assert.assertEquals(nonClientAdjUtils.getNumberFromText(updatedNewCell), totalNew, "        The value in New should equal to Adj$ + Current$.");
		Assert.assertEquals(nonClientAdjustments.getTextCellTableNonClientAdj(10, 2), newAdjComment, "        Adj Comment should be displayed.");
		
		logger.info("*** Expected Result: Verify that the Print checkbox is checked in Non-Client Adjustments table");
		Assert.assertTrue(isChecked(nonClientAdjustments.cellTableNonClientAdjChecbox(11, 2)),"        Print checkbox should be checked");
		
		logger.info("*** Actions: Click Post button");
		nonClientAdjustments.clickPostBtn();

		logger.info("*** Expected Result: Verify that the data is NOT updated and saved in DB");
		String dueAmtInDB = nonClientAdjUtils.formatDecimalPoint(Double.parseDouble(accessionDao.getAccn(accnId).getDueAmtAsMoney().toString()));
		Assert.assertEquals(nonClientAdjUtils.formatDecimalPoint(Double.parseDouble(billAmt)), dueAmtInDB,"        The value in New$ should equal to Due_amt: " + dueAmtInDB);
		AccnAdj accnAdjInfo=null;
		try{
			accnAdjInfo = accessionDao.getAccnAdjByAccnIdAndSeqId(accnId, 1);
		}
		catch (Exception e){Assert.assertNull(accnAdjInfo,"        The Adjustments are not posted.");}
		
		logger.info("*** Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}
	}	
	
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Uncheck Delete Row checkbox")
	@Parameters({"email", "password","project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType1", "eType2", "engConfigDB"})
	public void testXPR_508(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType1, String eType2, String engConfigDB) throws Exception {
		    	
    	logger.info("===== Testing - testXPR_508 =====");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter();
		testDataSetup = new TestDataSetup();
		navigation = new MenuNavigation(driver, config);
		accessionUtils = new AccnDetailUtils(driver, config);

        String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, false);

		logger.info("*** Expected Results: - Ensure the Accession is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");
    	    	
    	logger.info("*** Actions: Create CSV upload file");
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();    	   	
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources";//+File.separator+uploadFileName;
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
       	float adjAmt = Float.valueOf(billAmt)-1;
    	List<Float> adjAmtList = Arrays.asList(new Float[]{adjAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjAmt;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    	
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjAmtList, anticipateNewList, filePath+File.separator+uploadFileName);
			
		logger.info("*** Expected Result:  Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " + filePath+File.separator+uploadFileName + "should be available.");
		
    	logger.info("*** Actions: - Navigate to Xifinportal Non-Client Adjustments page");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Expected Results: - Verify that user is logged in");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Action: Click Upload button");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Expected result: Upload Documents popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.uploadBtn()));

		logger.info("*** Action: Upload CSV file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));
		
		logger.info("*** Expected Results: Verify that a new row is added into Non-Client Adjustments table with correct data: New$ = Adj$ + Current$, Current$ = Due_Amt");
		String accnIDCell = nonClientAdjustments.getTextCellTableNonClientAdj(2, 2);
		String newCell = nonClientAdjustments.getTextCellTableNonClientAdj(14, 2);
    	String currentCell = nonClientAdjustments.getTextCellTableNonClientAdj(13, 2);
    	String adjCell = nonClientAdjustments.getTextCellTableNonClientAdj(9, 2);
    	Float total = nonClientAdjUtils.getNumberFromText(adjCell) + nonClientAdjUtils.getNumberFromText(currentCell);
    	String due_amt = nonClientAdjUtils.formatDecimalPoint(Double.parseDouble(accessionDao.getAccn(accnIDCell).getDueAmtAsMoney().toString()));
    	
    	Assert.assertEquals(nonClientAdjUtils.getNumberFromText(newCell), total, "        The value in New should equal to Adj + Current.");
    	Assert.assertEquals(currentCell, due_amt, "        The value in Current should equal to dueAmt in the Accn table.");
    	
		logger.info("*** Actions: Select the new row. Click Edit button");
		clickHiddenPageObject(nonClientAdjustments.cellTableNonClientAdj(2, 2), 0);
		nonClientAdjustments.clickEditBtn();

		logger.info("*** Expected Results: Verify that the Edit popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.editPopupTitle()));
		
		logger.info("*** Actions: Update Adj$. Enter Adj comment. Check Print checkbox. Check Delete Row checkbox. Click OK button");
		String newAdjAmt = String.valueOf(adjAmt + 0.5);
		String newAdjComment = "Auto comment " + accnId;
		Assert.assertTrue(isElementPresent(nonClientAdjustments.adjID()   , 5),"        Adj Comment text field should be displayed");
		nonClientAdjustments.adjAmtInput(newAdjAmt);
		nonClientAdjustments.adjCommentInput(newAdjComment);
		nonClientAdjustments.clickPrintChkBox();
		nonClientAdjustments.clickDeleteRowChkBox();
		nonClientAdjustments.clickOKBtn();
		
		logger.info("*** Expected Result: Verify that the data are updated and displayed properly: New$ = newAdj$ + current$");
		String updatedNewCell = nonClientAdjustments.getTextCellTableNonClientAdj(14, 2);
    	String updatedCurrentCell = nonClientAdjustments.getTextCellTableNonClientAdj(13, 2);
    	Float totalNew = nonClientAdjUtils.getNumberFromText(newAdjAmt) + nonClientAdjUtils.getNumberFromText(updatedCurrentCell);
    	Assert.assertEquals(nonClientAdjUtils.getNumberFromText(updatedNewCell), totalNew, "        The value in New should equal to Adj$ + Current$.");
		Assert.assertEquals(nonClientAdjustments.getTextCellTableNonClientAdj(10, 2), newAdjComment, "        Adj Comment should be displayed.");
		
		logger.info("*** Expected Result: Verify that the Print checkbox is checked in Non-Client Adjustments table");
		Assert.assertTrue(isChecked(nonClientAdjustments.cellTableNonClientAdjChecbox(11, 2)),"        Print checkbox should be checked");		
		
		logger.info("*** Actions: Uncheck the Delete Row checkbox in Non-Client Adjustments table");
		nonClientAdjustments.cellTableNonClientAdjChecbox(15, 2).click();
		
		logger.info("*** Expected Result: Verify that the selected row is displayed as not deleted");
		Assert.assertEquals(nonClientAdjustments.cellTableNonClientAdjChecbox(15, 2).getAttribute("value"),"false","        Delete Row Checkbox should be unchecked.");
				
		logger.info("*** Actions: Click Post button");
		nonClientAdjustments.clickPostBtn();

		logger.info("*** Expected Result: Verify that the updated data is saved in DB properly");
		String updatedDueAmt = nonClientAdjUtils.formatDecimalPoint(Double.parseDouble(accessionDao.getAccn(accnId).getDueAmtAsMoney().toString()));
		Assert.assertEquals(updatedNewCell, updatedDueAmt,"        The value in New$ should equal to Due_amt: " + updatedDueAmt);

		AccnAdj accnAdjInfo = accessionDao.getAccnAdjByAccnIdAndSeqId(accnId, 1);
		String adjCommentStr = accnAdjInfo.getNote();
		Assert.assertEquals(adjCommentStr, newAdjComment,"        The Adj Comment should be " + newAdjComment);		
		
		boolean isPrintNote = accnAdjInfo.getIsPrintNote();
		Assert.assertTrue(isPrintNote, "        The B_PRINT_NOTE should be True (1).");
		
		logger.info("*** Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}
	}
	
	@Test(priority = 1, description = "Verify Help icon in Non-Client Adjustments table")
    public void testXPR_509() throws Exception {
		logger.info("===== Testing - testXPR_509 =====");
		
		logger.info("*** Step 1 Actions: Log into Non-Client Adjustment with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();

		logger.info("*** Step 1 Expected Results: Verify that user is logged into Non-Client Adjustments");
		nonClientAdjustments = new NonClientAdjustments(driver, wait);
		Assert.assertTrue(isElementPresent(nonClientAdjustments.nonClnAdjPageTitle(), 5),"        Non-Client Adjustments Page title should show.");
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().equalsIgnoreCase("Non-Client Adjustments"),"          Page title should be 'Non-Client Adjustments'.");
		
		logger.info("*** Step 2 Actions: Click Help icon in Non-Client Adjustments table");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.helpIconOnTable(), 5),"        Help icon in Non-Client Adjustments table should show.");
		nonClientAdjustments.clickHelpIconOnTable();

		logger.info("*** Step 2 Expected Results: Verify that proper Help page is shown");
		switchToPopupWin();
		
		String URL = driver.getCurrentUrl();
		Assert.assertTrue(URL.contains("p_non_client_adjustments.htm"),"        Help file for 'Adjustments - Non-Client Adjustments should show.");
	}
	
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Verify Help icon in Edit Record popup")
	public void testXPR_510() throws Exception {
		    	
    	logger.info("===== Testing - testXPR_510 =====");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);    	
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter(driver);
				
    	String accnId = accessionDao.getPricedThirdPartyAccnWOErrorFromAccnAndAccnPyr().getAccnId();
    	    	
    	logger.info("*** Step 1 Actions: Create a CSV upload file");      	
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();    	   	
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources";
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
       	float adjAmt = Float.valueOf(billAmt)-1;
    	List<Float> adjAmtList = Arrays.asList(new Float[]{adjAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjAmt;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});  
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjAmtList, anticipateNewList, filePath+File.separator+uploadFileName);
			
		logger.info("*** Step 1 Expected Result:  Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " + filePath+File.separator+uploadFileName + "should be available.");
		
    	logger.info("*** Step 2 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();

		logger.info("*** Step 2 Expected Results: - Verify that Non-Client Adjustments screen displays");	
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Step  3 Action: Click Upload button");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadBtn(), 5));
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Step 3 Expected result: Verify that the Upload Documents popup is displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadUploadDocumentBtn(), 5),"        Upload Documents popup is displayed");
		
		logger.info("*** Step 4 Action: Upload the CSV file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
		waitUntilElementIsNotVisible(nonClientAdjustments.uploadUploadDocumentBtn(), 10);

		logger.info("*** Step 4 Expected Results: Verify that a new row is added in the Non-Client Adjustments table");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.nonClnAdjTable(), 5),"        Non Client Adjustment table should show");
		Assert.assertTrue(getColumnValue(nonClientAdjustments.nonClnAdjTable(), accnId),"        "+accnId+" should display");
		
		logger.info("*** Step 5 Actions: Select the new row and click Edit button");
		nonClientAdjustments.clickRowOnTable(2,2);
		Assert.assertTrue(isElementPresent(nonClientAdjustments.editBtn(), 5),"        Edit button should show");
		nonClientAdjustments.clickEditBtn();
		
		logger.info("*** Step 5 Expected Results: Verify that the Edit Record popup is displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.cancelUploadDocumentBtn(), 5),"        Cancel button should show");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadUploadDocumentBtn(), 5),"        Upload button should show");
		
		logger.info("*** Step 6 Actions: Click Help Icon");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.helpIconOnEditPopup(), 5),"        Help icon should show");
		nonClientAdjustments.clickHelpIconOnEditPopup();

		logger.info("*** Step 6 Expected Results: Verify that proper Help page is displayed");
		switchToPopupWin();
		String URL = driver.getCurrentUrl();
		Assert.assertTrue(URL.contains("p_non_client_adjustments_add.htm"),"        Proper Help file should be opened");
		
		logger.info("*** Step 7 Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}		
	}		
	
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Upload file have Anticipate New $ is not equal to New$")
	@Parameters({"email", "password","project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType1", "eType2", "engConfigDB"})
	public void testXPR_511(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType1, String eType2, String engConfigDB) throws Exception {
		    	
    	logger.info("===== Testing - testXPR_511 =====");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter(driver);
		navigation = new MenuNavigation(driver, config);
		accessionUtils = new AccnDetailUtils(driver, config);

        String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, false);

		logger.info("*** Expected Results: - Ensure the Accession is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");
    	    	
    	logger.info("*** Actions: Create CSV upload file with 'Anticipated New $' not equals to 'New $'");
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();    	   	
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources";//+File.separator+uploadFileName;
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
       	float adjAmt = Float.valueOf(billAmt)-1;
    	List<Float> adjAmtList = Arrays.asList(new Float[]{adjAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjAmt - Float.parseFloat("0.5");
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    	
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	nonClientAdjUtils.creatNonClnAdjUploadMiniReqFile(accnList, adjPyrID, adjCDList, billList, adjAmtList, anticipateNewList, filePath+File.separator+uploadFileName);
			
		logger.info("*** Expected Result:  Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " + filePath+File.separator+uploadFileName + "should be available.");
		
    	logger.info("*** Actions: - Navigate to Xifinportal Non-Client Adjustments page");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();
		
		logger.info("*** Expected Results: - Verify that user is logged in");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Action: Click Upload button");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Expected result: Upload Documents popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.uploadUploadDocumentBtn()));
		
		logger.info("*** Action: Upload CSV file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));
		
		logger.info("*** Expected Results: Verify that a new row is added into Non-Client Adjustments table with the data in the upload file");
		String accnIDCell = nonClientAdjustments.getTextCellTableNonClientAdj(2, 2);
		String newCell = nonClientAdjustments.getTextCellTableNonClientAdj(14, 2);
    	String currentCell = nonClientAdjustments.getTextCellTableNonClientAdj(13, 2);
    	String adjCell = nonClientAdjustments.getTextCellTableNonClientAdj(9, 2);
    	Float total = nonClientAdjUtils.getNumberFromText(adjCell) + nonClientAdjUtils.getNumberFromText(currentCell);
    	String due_amt = nonClientAdjUtils.formatDecimalPoint(Double.parseDouble(accessionDao.getAccn(accnIDCell).getDueAmtAsMoney().toString()));
    	
    	Assert.assertEquals(nonClientAdjUtils.getNumberFromText(newCell), total, "        The value in New should equal to Adj + Current.");
    	Assert.assertEquals(currentCell, due_amt, "        The value in Current should equal to dueAmt in the Accn table.");
    			
		logger.info("*** Expected Results: Verify that an Warning message is displayed 'Warning loading row 1: New $ does not equal the Anticipated new $'");
		Assert.assertTrue(nonClientAdjustments.errorHeaderContent().getText().contains("New $ does not equal the Anticipated New $"),"        Error message ' New $ does not equal the Anticipated New $' should show.");
		
		logger.info("*** Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}
	}		
	
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Upload a CSV file contains the Proc Code not on the Accession")
	public void testXPR_517() throws Exception {

    	logger.info("===== Testing - testXPR_517 =====");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
    	nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		randomCharacter = new RandomCharacter(driver);
    	    	
    	logger.info("*** Step 1 Actions: Create CSV upload file with the Proc Code not on the Accession");      	
		List<Accn> pricedAccnList = accessionDao.getPricedThirdPartyPyrAccn(2);						
		String accnId = pricedAccnList.get(0).getAccnId();
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();    	 
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+uploadFileName;
    	float adjCost = 1;
    	List<Float> adjCostList = Arrays.asList(new Float[]{adjCost});
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjCost;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    
    	String procCd = "P9603"; //Proc code for TRAVEL test
    	List<String> procCdList = Arrays.asList(new String[]{procCd});
    	nonClientAdjUtils.creatNonClnAdjUploadFileWithProcCode(accnList, adjPyrID, procCdList, 1, adjCDList, billList, adjCostList, anticipateNewList, filePath);
    	
		logger.info("*** Step 2 Expected Results: - Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath),"        CSV file: " +  filePath + " should be available.");
    	    	
    	logger.info("*** Step 2 Actions: - Log into Non-Client Adjustments screen with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();

		logger.info("*** Step 2 Expected Results: - Verify that Non-Client Adjustments displays");	
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Step 3 Action: Click Upload button");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadBtn(), 5));
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Step 3 Expected result: Verify that the Upload Documents popup is displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadUploadDocumentBtn(), 5),"        Upload Documents popup is displayed");
		
		logger.info("*** Step 4 Action: Upload the CSV file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
		waitUntilElementIsNotVisible(nonClientAdjustments.uploadUploadDocumentBtn(), 10);

		logger.info("*** Step 4 Expected Results: - Verify that a new rows is added into table as a deleted row");
		Assert.assertTrue(Arrays.asList(nonClientAdjustments.rowTableNonClientAdj(2).getAttribute("class").split(" ")).contains("rowMarkedForDelete"),"        The 1st row in Non-Client Adjustments table should be marked as Deleted.");
				
		logger.info("*** Step 4 Expected Results: - Verify that an error message shows with information the procedure code not found");			
		assertTrue(isElementPresent(nonClientAdjustments.messagesReturnedAreabox(), 5),"         Message areabox is displayed");
		assertTrue(nonClientAdjustments.messagesReturnedAreabox().getText().contains("Unique procedure code " + procCd + " not found"),"         Error Message: Unique procedure code " + procCd + " not found should be displayed.");	

		logger.info("*** Step 5 Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath)){
			nonClientAdjUtils.deleteFile(filePath);
		}
    }
	
	@Test(priority = 1, description = "Verify Reset button")
	public void testXPR_516() throws Exception {

		logger.info("===== Testing - testXPR_516 =====");

		logger.info("*** Step 1 Actions: - Get Priced Accessions from DB");
		List<String> listAccnID = new ArrayList<>();
		List<Accn> accnList = accessionDao.getPricedThirdPartyPyrAccn(6);
		for (Accn strings : accnList) {
			listAccnID.add(strings.getAccnId());
		}    			
		
		logger.info("*** Step 2 Actions: - Create file data with selected Accessions");		
		nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
		String filePath = (new File(System.getProperty("user.dir")).getParentFile() + File.separator + "src" + File.separator + "test" + File.separator + "resources");	
		randomCharacter = new RandomCharacter(driver);
		String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";	
		nonClientAdjUtils.creatUploadNonClnAdjFile(listAccnID, 10, filePath+File.separator+uploadFileName); // Create file before upload
		
		logger.info("*** Step 2 Expected Results: - Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName),"        CSV file: " +  filePath+File.separator+uploadFileName + " should be available.");
		
		logger.info("*** Step 3 Actions: - Log into Non-Client Adjustments screen with SSO username and password");
		nonClientAdjustments = new NonClientAdjustments(driver, wait);  
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();

		logger.info("*** Step 3 Expected Results: - Verify that the Non-Client Adjustment page is displayed");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(isElementPresent(nonClientAdjustments.pageTitleLb(), 5),"        Page title should be displayed");
		Assert.assertTrue(nonClientAdjustments.pageTitleLb().getText().equalsIgnoreCase("Non-Client Adjustments"),"        Page title should be displayed correct");
		
		logger.info("*** Step 4 Actions: - Click Upload button");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadBtn(), 5),"        Upload button should be displayed.");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Step 4 Expected Result: - Verify that Upload Documents popup displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.chooseFileBtn(), 5),"        Upload file button should be displayed");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.uploadUploadDocumentBtn(), 5),"        Upload button should be displayed");
		
		logger.info("*** Step 5 Actions: - Select the CSV file and click Upload button");
		uploadFile(nonClientAdjustments.xfnUploaderFile(), uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));

		logger.info("*** Step 5 Expected Result: - Verify that multiple rows in the file are displayed in the Non-Client Adjustments table");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsDisplayValidData(nonClientAdjustments),"        All rows should be displayed with correct data");
		Assert.assertTrue(isElementPresent(nonClientAdjustments.resetBtn(), 5),"        Reset button should be displayed");
		
		logger.info("*** Step 6 Actions: - Click Reset button");
		nonClientAdjustments.clickResetBtn();

		logger.info("*** Step 6 Expected Result: - Verify that all row are cleared from the Non-Client Adjustments table");
		Assert.assertEquals(nonClientAdjustments.countRowInNonClientAdjTable(),0,"        All rows should be cleared.");
		
		logger.info("*** Step 7 Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath+File.separator+uploadFileName)){
			nonClientAdjUtils.deleteFile(filePath+File.separator+uploadFileName);
		}
	}	
	
	@SuppressWarnings("unchecked")
	@Test(priority = 1, description = "Upload a CSV file contains Proc Code")
	@Parameters({"email", "password","project","testSuite","testCase","propLevel","propName","wsUsername","wsPassword", "xapEnv", "eType1", "eType2", "engConfigDB"})
	public void testXPR_518(String email, String password, String project, String testSuite,String testCase,String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType1, String eType2, String engConfigDB) throws Exception {

    	logger.info("===== Testing - testXPR_518 =====");
    	nonClientAdjustments = new NonClientAdjustments(driver, wait);
    	nonClientAdjUtils = new NonClientAdjustmentsUtils(driver, config);
    	randomCharacter = new RandomCharacter(driver);
		navigation = new MenuNavigation(driver, config);
		accessionUtils = new AccnDetailUtils(driver, config);

        String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.ACCNWS_URL), xapEnv, eType1, eType2, engConfigDB, false);

		logger.info("*** Expected Results: - Ensure the Accession is Priced");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");
    	    	
    	logger.info("*** Actions: Create CSV upload file");
    	List<String> accnList = Arrays.asList(new String[] {accnId});
    	File file = new File(System.getProperty("user.dir")).getParentFile();
    	String uploadFileName = "NonClnAdjUpload_" + randomCharacter.getRandomAlphaString(4) + ".csv";	    	
    	String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+uploadFileName;
    	float adjCost = 1;
    	List<Float> adjCostList = Arrays.asList(new Float[]{adjCost});
    	String pyrId = payorDao.getPyrFromAccnPyrByAccnId(accnId).getPyrAbbrv();
    	List<String> adjPyrID = Arrays.asList(new String[]{pyrId});
    	String adjCD = "COL";//daoManagerPlatform.getRandomAdjCdIdAdjAbbrvFromADJCD(testDb).get(1);
    	List<String> adjCDList = Arrays.asList(new String[]{adjCD});
    	String billAmt = accessionDao.getAccn(accnId).getBilPrcAsMoney().toString();
    	List<String> billList = Arrays.asList(new String[]{billAmt});
    	float anticipatedNew = Float.parseFloat(billAmt) + adjCost;
    	List<Float> anticipateNewList = Arrays.asList(new Float[]{anticipatedNew});    	
    	String procCd = accessionDao.getAccnTestsByAccnId(accnId).get(0).getProcId();
    	List<String> procCdList = Arrays.asList(new String[]{procCd});
    	int units = accessionDao.getAccnTestsByAccnId(accnId).get(0).getUnits();
    	nonClientAdjUtils.creatNonClnAdjUploadFileWithProcCode(accnList, adjPyrID, procCdList, units, adjCDList, billList, adjCostList, anticipateNewList, filePath);
    	
		logger.info("*** Expected Results: - Verify that the CSV file is available");
		Assert.assertTrue(nonClientAdjUtils.isFileExist(filePath),"        CSV file: " +  filePath + " should be available.");
    	    	
    	logger.info("*** Actions: - Navigate to Xifinportal Non-Client Adjustments page");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToNonClientAdjustmentsPage();

		logger.info("*** Expected Results: - Verify that user is logged in");
		nonClientAdjUtils.waitForLoginSuccess();
		Assert.assertTrue(nonClientAdjustments.nonClnAdjPageTitle().getText().contains("Non-Client Adjustments"),"        Non Client Adjustments page is displayed");		
		
		logger.info("*** Action: Click on Upload button");
		nonClientAdjustments.clickUploadBtn();
		
		logger.info("*** Expected result: Upload Documents popup is displayed");
        wait.until(ExpectedConditions.visibilityOf(nonClientAdjustments.uploadUploadDocumentBtn()));
		
		logger.info("*** Action: Upload csv file");
		nonClientAdjustments.chooseFileUpload(this, uploadFileName);
		nonClientAdjustments.clickUploadUploadDocumentBtn();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn_uploadUploadDocuments")));

		logger.info("*** Expected result: Verify that a new row is added into table with correct data");
		Thread.sleep(2000);
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsDisplayValidData(nonClientAdjustments),"        All rows is displayed with correct data");
		String dueAmt = accessionDao.getAccn(accnId).getDueAmtAsMoney().toString();
		String currentDollars = nonClientAdjustments.getListRowDataInColumn(13).get(0);
		Assert.assertEquals(nonClientAdjUtils.getNumberFromText(dueAmt), nonClientAdjUtils.getNumberFromText(currentDollars));
		Assert.assertEquals(adjPyrID, nonClientAdjustments.getListRowDataInColumn(7),"        Adj PyrId is displayed correctly");
		Assert.assertEquals(adjCDList, nonClientAdjustments.getListRowDataInColumn(8),"        Adj Cd is displayed correctly");
				
		logger.info("*** Action: Click Post button");
		List<String> listNewDollars = nonClientAdjustments.getListRowDataInColumn(14);
		logger.info("        listNewDollars= "+listNewDollars);
		List<String> listAccnId = nonClientAdjustments.getListRowDataInColumn(2);
		logger.info("        listAccnId = " + listAccnId);
		nonClientAdjustments.clickPostBtn();
		
		logger.info("*** Expected result: Verify that proper data is saved into DB");
		Assert.assertTrue(nonClientAdjUtils.verifyAllRowIsSavedCorrect(listAccnId, listNewDollars));
		
		logger.info("*** Action: Delete the test CSV file");
		if (nonClientAdjUtils.isFileExist(filePath)){
			nonClientAdjUtils.deleteFile(filePath);
		}
    }

}
