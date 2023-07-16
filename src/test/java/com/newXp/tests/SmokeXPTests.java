package com.newXp.tests;

import com.overall.accession.accessionProcessing.AccessionDiagnosis;
import com.overall.fileMaintenance.adjustmentCode.AdjustmentCode;
import com.overall.client.clientProcessing.AuditLog;
import com.overall.fileMaintenance.diagnosisCode.DiagnosisCode;
import com.overall.menu.MenuNavigation;
import com.overall.payment.clientPayments.ClientPayments;
import com.xifin.utils.SeleniumBaseTest;
import com.overall.payor.payorContactManager.PayorContactManager;

import domain.accession.PatientServiceCenter.patientestimation.PatientEstimation;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class SmokeXPTests extends SeleniumBaseTest {

    protected Logger logger;
    protected ClientPayments clientPayments;
    protected MenuNavigation menuNavigation;
    protected AuditLog auditLog;
    protected AdjustmentCode adjCode;
    protected MenuNavigation navigation;
    protected PatientEstimation patientEstimation;
    protected AccessionDiagnosis accessionDiagnosis;
    protected PayorContactManager payorContactManager;
    protected DiagnosisCode diagnosisCode;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"ssoUsername", "ssoPassword"})
    public void beforeMethod(String ssoUsername, String ssoPassword, Method method)
    {
        try
        {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            logIntoSso(ssoUsername, ssoPassword);
            menuNavigation = new MenuNavigation(driver, config);
            logger.info("*** Step 1 Expected Results: - User should be in homepage");
            String username = "x" + getUserNameFromEmail(ssoUsername);
            Assert.assertNotNull(ssoLogin.webElementByText(username));
        }
        catch (Exception e)
        {
            logger.error("Error running BeforeMethod", e);
        }
    }

    @Test(priority = 1, description = "Verify login to XP")
    @Parameters({"ssoUsername"})
    public void verifyLoginToXP(String ssoUsername) {
        logger.info("===== Verify login to XP =====");
        logger.info("*** Step 4 Expected Results: - Verify user should be in homepage");
        String username = "x" + getUserNameFromEmail(ssoUsername);
        Assert.assertNotNull(ssoLogin.webElementByText(username));

    }

    @Test(priority = 1, description = "Verify Accession Diagnosis Page Displays")
    @Parameters({"ssoUsername"})
    public void verifyAccessionDiagnosisPageLoads() throws Exception {
        accessionDiagnosis = new AccessionDiagnosis(driver);
        logger.info("===== Verify Accession Diagnosis Page Displays =====");

        logger.info("*** Step 2 Acions: - Navigate to Accession Diagnosis Page");
        menuNavigation.navigateToAccessionDiagnosis();

        logger.info("*** Step 2 Expected Results: - Accession Diagnosis Page is Displayed");
        Assert.assertEquals(accessionDiagnosis.appendFileText().getText(), "Append File");
        Assert.assertTrue(accessionDiagnosis.appendFileBtn().isDisplayed());
    }

    @Test(priority = 1, description = "Verify Payor Page Loads")
    @Parameters({"PayorID", "contDetailText"})
    public void verifyPayorPageLoads(String PayorID, String contDetailText) throws Exception {
        payorContactManager = new PayorContactManager(driver);
        logger.info("===== Verify Payor Page Loads =====");

        logger.info("*** Step 2 Actions: - Navigate to Payor Contact Manager Page");
        menuNavigation.navigateToPayorContactManagerPage();
        logger.info("*** Step 2 Expected Results: - Contact Manager Page is displayed");
        Assert.assertEquals(payorContactManager.payorContManagerLoadPgTitleTxt().getText(), "Contact Manager");

        logger.info("*** Step 3 Actions: - Input Payor Id");
        enterValues(payorContactManager.payorContManagerLoadPyrIdInput(), PayorID);

        logger.info("*** Step 3 Expected Results: - Contact Detail Page should display");
        WebElement contactDetailText = payorContactManager.contactDetailTblTitle();
        Assert.assertEquals(contactDetailText.getText(), contDetailText);
        Assert.assertTrue("User ID field is displayed", payorContactManager.contactDetailTblUserIdFilterInput().isDisplayed());

        logger.info("*** Step 4 Actions: - Click on Reset button");
        payorContactManager.clickResetBtn();

        logger.info("*** Step 4 Expected Results: - User should be taken back to Payor Contact Manager page");
        Assert.assertTrue(isElementPresent(payorContactManager.payorLoadPayorTblTitle(),5));
        Assert.assertEquals(payorContactManager.payorLoadPayorTblTitle().getText(), "Load Payor");
        Assert.assertFalse("Contact Detail should not be displayed", contactDetailText.isDisplayed());
    }

    @Test(priority = 1, description = "Verify Payments Page Loads")
    @Parameters({"batchID", "clnPayments", "clnPaymentsDetail"})
    public void verifyPaymentsPageDisplay(String batchID, String clnPayments, String clnPaymentsDetail) throws Exception {
        clientPayments = new ClientPayments(driver, wait);
        logger.info("===== Verify Payments Page Loads =====");

        logger.info("*** Step 2 - Navigate to Client Payments Page with URL");
        menuNavigation.navigateToClientPaymentsPage();
        Assert.assertEquals(clientPayments.findClientPaymentsTitle().getText(), "Client Payments");

        logger.info("*** Step 3 Actions: - Click on Batch ID Dropdown");
        WebElement batchTextBox = clientPayments.loadClientPaymentsSectionBatchIdDdl();
        batchTextBox.click();
        logger.info("*** Step 3 Expected Results: - Dropdown of Batch ID shows");

        logger.info("*** Step 4 Actions: - Enter Batch ID");
        enterValues(clientPayments.findBatchIdInput(), batchID);

        logger.info("*** Step 4 Expected Results: - Client Payments page should display");
        WebElement findClientPaymentsTblTitle = clientPayments.findClientPaymentsTblTitle();
        Assert.assertEquals(findClientPaymentsTblTitle.getText(), clnPayments);
        logger.info("*** Verify Client Payments Detail Show");
        WebElement ClientPaymentsDetailText = clientPayments.findClientPaymentsDetailText();
        Assert.assertEquals(ClientPaymentsDetailText.getText(), clnPaymentsDetail);

        logger.info("*** Step 5 Actions: - Click on Reset button");
        clientPayments.clickResetBtn();
        logger.info("*** Step 5 Expected Results: - User is taken back to Payments page, Client Payment section does not display, Batch ID input displays");
        Assert.assertFalse("Client Payment section doesn't show", findClientPaymentsTblTitle.isDisplayed());
        Assert.assertFalse("Client Payment Detail section doesn't show", ClientPaymentsDetailText.isDisplayed());
        Assert.assertTrue("Batch ID Text Box displays", isElementPresent(batchTextBox, 5));
    }

    @Test(priority = 1, description = "Verify Adjustment Code Page Loads")
    @Parameters({"AdjCd", "AdjCodeTblTitle"})
    public void verifyAdjustmentCodePageLoads(String AdjCd, String AdjCodeTblTitle) throws Exception {
        adjCode = new AdjustmentCode(driver);
        logger.info("===== Verify Adjustment Code Page Loads =====");

        logger.info("*** Step 2 Actions: - Navigate to Adjustment Code Page");
        menuNavigation.navigateToFileMaintAdjustmentCodePage();

        logger.info("*** Step 2 Expected Results: - Adjustment Code page should display");
        Assert.assertEquals(adjCode.adjCodeTblTitle().getText(), AdjCodeTblTitle);

        logger.info("*** Step 3 Actions: - Enter ADJ Code");
        enterValues(adjCode.adjusmentCodeAdjCdInput(), AdjCd);

        logger.info("*** Step 3 Expected Results: Cross Reference Section should display");
        WebElement crossReference = adjCode.adjCodeCrossReferenceTblTitle();
        Assert.assertNotNull("Cross Reference Section should show", crossReference);
        Assert.assertTrue(isElementPresent(crossReference, 2));

        logger.info("*** Step 4 Actions: - Click on Reset button");
        adjCode.clickResetBtn();

        logger.info("*** Step 4 Expected Results: User is taken back to Adjustment Code page, " +
                "Cross Reference Section is not displayed, ADJ Code Type is not displayed, and ADJ search button is displayed");
        Assert.assertFalse(isDisplayed(crossReference, 2));
        Assert.assertTrue(isElementPresent(adjCode.adjCodeSearchIcon(), 2));
        Assert.assertFalse(isDisplayed(adjCode.adjCodeTypeInput(), 2));
    }

    @Test(priority = 1, description = "Verify Patient Estimation Page Loads")
    @Parameters({"EstId"})
    public void verifyPatientEstimationPageLoads(String EstId) throws Exception {
        patientEstimation = new PatientEstimation(driver);
        logger.info("===== Verify Patient Estimation Page Loads =====");

        logger.info("*** Step 2 Actions: - Navigate to Patient Estimation Page");
        menuNavigation.navigateToPatientEstimationPage();

        logger.info("*** Step 2 Expected Results: - Patient Estimation page should display");
        Assert.assertEquals(patientEstimation.patientEstimationPageTitle().getText(), "Patient Estimation");

        logger.info("*** Step 3 Actions: - Enter Estimation ID");
        enterValues(patientEstimation.estimationIdInput(), EstId);

        logger.info("*** Step 3 Expected Results: Accession Information, Patient Information, Insurance Information, Order information and Patient Responsibility should display");
        WebElement AccessionInformationTblTitle = patientEstimation.AccessionInformationTblTitle();
        WebElement PatientInformationTblTitle = patientEstimation.PatientInformationTblTitle();
        WebElement InsuranceInformationTblTitle = patientEstimation.InsuranceInformationTblTitle();
        Assert.assertTrue(isElementPresent(AccessionInformationTblTitle, 2));
        Assert.assertTrue(isElementPresent(PatientInformationTblTitle, 2));
        Assert.assertTrue(isElementPresent(InsuranceInformationTblTitle, 2));

        logger.info("*** Step 4 Actions: - Click on Reset button");
        patientEstimation.clickResetBtn();

        logger.info("*** Step 4 Expected Results: User should be taken back to Load Patient Estimation, " +
                "Accession Information, Patient Information, Insurance Information should not be displayed");
        Assert.assertFalse(isDisplayed(AccessionInformationTblTitle, 2));
        Assert.assertFalse(isDisplayed(PatientInformationTblTitle, 2));
        Assert.assertFalse(isDisplayed(InsuranceInformationTblTitle, 2));
        Assert.assertTrue(isElementPresent(patientEstimation.loadPatientEstimationTblTitle(), 2));
    }

    @Test(priority = 1, description = "Verify Client Audit Log Page Loads")
    @Parameters({"clnID", "auditDetailText"})
    public void verifyClientAuditLogPageLoads(String clnID, String auditDetailText) throws Exception {
        auditLog = new AuditLog(driver, config);
        logger.info("===== Verify Client Audit Page Loads =====");

        logger.info("*** Step 2 Actions: - Navigate to Client Audit Log");
        menuNavigation.navigateToClientAuditLogPage();
        logger.info("*** Step 2 Expected Results: - Audit Log Page should display");
        Assert.assertEquals(auditLog.clnAuditLogTitle().getText(), "Audit Log");

        logger.info("*** Step 3 Actions: - Enter Client ID");
        enterValues(auditLog.clnIdInput(), clnID);

        logger.info("*** Step 3 Expected Results: - Audit Detail page should display");
        WebElement auditDetailTblTitle = auditLog.auditDetailTblTitle();
        Assert.assertEquals(auditDetailTblTitle.getText(), auditDetailText);

        logger.info("*** Step 4 Actions: - Click on Reset Button");
        auditLog.clickResetBtn();
        logger.info("*** Step 4 Expected Results: - User is taken back to Load Client page, Audit Detail section should not display, Load Client section should display");
        Assert.assertFalse("Audit Detail section does not display", auditDetailTblTitle.isDisplayed());
        Assert.assertTrue("Load Client section should display", isElementPresent(auditLog.loadClientTblTitle(),2));
    }

    @Test(priority = 1, description = "Verify Diagnosis Code Page Loads")
    @Parameters({"DiagCode", "DiagCodeInfoTitle"})
    public void verifyDiagnosisCodePageLoads(String DiagCode, String DiagCodeInfoTitle) throws Exception {
        diagnosisCode = new DiagnosisCode(wait);
        logger.info("===== Verify Diagnosis Code Page Loads =====");

        logger.info("*** Step 2 Actions: - Navigate to Diagnosis Code Page");
        menuNavigation.navigateToDiagnosisCodePage();
        logger.info("*** Step 2 Expected Results: - Diagnosis Code Page should display");
        Assert.assertEquals(diagnosisCode.diagnosisCodeTitle().getText(), "Diagnosis Code");

        logger.info("*** Step 3 Actions: - Enter Diagnosis Code");
        enterValues(diagnosisCode.diagCodeInput(), DiagCode);

        logger.info("*** Step 3 Expected Results: - Diagnosis Code Information should display");
        WebElement DiagCodeInfoTblTitle = diagnosisCode.diagCodeInfoTblTitle();
        Assert.assertEquals(DiagCodeInfoTblTitle.getText(), DiagCodeInfoTitle);

        logger.info("*** Step 4 Actions: - Click on Reset Button");
        diagnosisCode.clickResetBtn();
        logger.info("*** Step 4 Expected Results: - User is taken back to Load Diagnosis Code page, Diagnosis Code Information table should not display, Load Diagnosis Code section should display");
        Assert.assertFalse("Audit Detail section does not display", DiagCodeInfoTblTitle.isDisplayed());
        Assert.assertTrue("Load Client section should display", isElementPresent(diagnosisCode.loadDiagCodeTblTitle(),2));
    }
}