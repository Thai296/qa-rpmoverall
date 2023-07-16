package com.newXp.tests;


import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.overall.fileMaintenance.sysMgt.SubmissionAndRemittance;
import com.overall.menu.MenuNavigation;
import com.overall.utils.XifinPortalUtils;
import com.xifin.util.DateConversion;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TimeStamp;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SmokeSubmissionAndRemittanceTest extends SeleniumBaseTest {

	private MenuNavigation navigation;
	private XifinPortalUtils xifinPortalUtils;

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

	//Load Incremental Price Table
	@Test(priority = 1, description = "Verify data displayed properly in Submissions grid")
	@Parameters({"submFileSeqId"})
	public void testSubmissionFilesPulledFromDocStore(Integer submFileSeqId) throws Exception {
		logger.info("===== Testing - testSubmissionFilesPulledFromDocStore =====");

		SubmFile submFile = submissionDao.getSubmFileBySubmFileSeqId(submFileSeqId);
		Date date = submFile.getFileCreatDt();

		SubmissionAndRemittance submissionAndRemittance = new SubmissionAndRemittance(driver, wait);
		TimeStamp timeStamp = new TimeStamp();
		navigation = new MenuNavigation(driver, config);
		xifinPortalUtils = new XifinPortalUtils(driver);

		logger.info("*** Step 1: User login successful and Submission Remittance screen is displayed.");
		navigation.navigateToSubmissionAndRemittancePage();
		assertTrue(isElementPresent(submissionAndRemittance.pagetitle(), 5),"        Submission and Remittance screen should be displayed.");

		logger.info("*** Step 2 Action: Input valid Create Date From = Create Date To = Current Date.");
		assertTrue(isElementPresent(submissionAndRemittance.creationFromDateInput(),5),"        Create Date From textbox should be existing.");
		assertTrue(isElementPresent(submissionAndRemittance.creationToDateInput(),5),"        Create Date To textbox should be existing.");
		String searchDate = DateConversion.marsFormat(date);
		logger.info("        Searching for date " + searchDate);
		submissionAndRemittance.setCreationFromAndCreationToDate(searchDate,searchDate);

		logger.info("*** Step 2 Expected Result: Data is displayed correctly.");
		List<SubmFile> submFiles = submissionDao.getSubmFilesByDataFmtTypIdFileCreatDt(new java.sql.Date(date.getTime()));
		assertTrue(isElementPresent(submissionAndRemittance.totalReCordsSubmissionTbl(),5),"        Total record should be displayed.");
		String totalRow=xifinPortalUtils.getTotalResultSearch(submissionAndRemittance.totalReCordsSubmissionTbl());
		String size=xifinPortalUtils.convertDecimalFormat(submFiles.size());
		assertEquals(size,totalRow,"        Number of rows is not displayed correctly.");

		logger.info("        Enter Filename in Submissions grid filter");
		assertTrue(isElementPresent(submissionAndRemittance.filenameFilterInputInSubmissionsTbl(), 5)," Filename filter should be displayed for Submissions grid.");
		//submissionAndRemittance.setPayorIdFilterInPayorSetupTbl(pyrAbbrev2);
		setInputValue(submissionAndRemittance.filenameFilterInputInSubmissionsTbl(), submFile.getFilename());
		Thread.sleep(3000);

		logger.info("*** Step 3 Actions: - Click the Statement hyperlink");
		assertTrue(isElementPresent(submissionAndRemittance.submissionTblCell(2,7), 5), "        Link should be visible.");
		clickHiddenPageObject(submissionAndRemittance.getTextOfColFileInSubmissionTblCell(2,7), 0);

		logger.info("*** Step 3 Expected Results: - Verify that the Statement file is opened properly");
		switchToPopupWin();
		String currentUrl = driver.getCurrentUrl();
		assertTrue(currentUrl.contains("getdoc.html?"), "        The file should be open using getdoc. ActualUrl=" + currentUrl);
		assertTrue(currentUrl.contains("&id=" + submFile.getDocStoreSeqId()), "        The file should be open with docStoreId. ActualUrl=" + currentUrl);
	}

}	
