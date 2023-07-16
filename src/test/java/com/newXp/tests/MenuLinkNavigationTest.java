package com.newXp.tests;

import com.overall.accession.ErrorProcessing.EPSearch;
import com.overall.accession.PatientProcessing.PatientDemographics;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.accessionHistoryLog.AccessionHistoryLog;
import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
import com.overall.accession.accnEpSummary.EpSummary;
import com.overall.accession.accnSingleStatement.SingleStatement;
import com.overall.accession.notesPromisedPayments.NotesPromisedPayments;
import com.overall.accession.orderProcessing.AccnTestUpdate;
import com.overall.client.clientBillingRules.ClientBillingRules;
import com.overall.client.clientInquiry.clientBatchSingleDemandStatement.ClientBatchSingleDemandStatement;
import com.overall.client.clientInquiry.clientTransactionDetail.ClientTransactionDetail;
import com.overall.client.clientProcessing.ClientContactManager;
import com.overall.client.clientProcessing.ClientDemographics;
import com.overall.client.clientProcessing.ClientPricingConfiguration;
import com.overall.client.clientProcessing.EligCensusConfig;
import com.overall.client.clientProcessing.SubmissionConfiguration;
import com.overall.fileMaintenance.fileMaintenanceTables.EligibilityConfiguration;
import com.overall.fileMaintenance.fileMaintenanceTables.EligibilityResponseTranslation;
import com.overall.fileMaintenance.fileMaintenanceTables.ReasonCode;
import com.overall.fileMaintenance.pricing.FeeSchedule;
import com.overall.fileMaintenance.pricing.SpecialPriceTable;
import com.overall.fileMaintenance.pricing.incrementalPricing.IncrementalPricing;
import com.overall.fileMaintenance.pricing.testCode.TestCode;
import com.overall.fileMaintenance.sysMgt.DocumentUploadAndStorage;
import com.overall.fileMaintenance.sysMgt.SubmissionAndRemittance;
import com.overall.filemaint.logoConfiguration.LogoConfiguration;
import com.overall.filemaint.physicianLicense.PhysicianLicense;
import com.overall.financialManagement.endOfMonth.EomSchedule;
import com.overall.financialManagement.endOfMonth.FinancialManagement;
import com.overall.menu.MenuLinkNavigation;
import com.overall.payment.clientPayments.ClientPayments;
import com.overall.payment.expectPriceDiscrepancies.ExpectPriceDiscrepancies;
import com.overall.payment.nonClientPayments.NonClientPayments;
import com.overall.payment.nonClientSummary.NonClientSummary;
import com.overall.payment.patientPayments.PatientPayments;
import com.overall.payment.paymentPayments.Deposits;
import com.overall.payment.paymentPayments.PaymentSearch;
import com.overall.payor.consolidation.consolidationRules.ConsolidationRules;
import com.overall.payor.payorDemographics.PayorDemographics;
import com.overall.payor.payorDemographics.PricingConfig;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MenuLinkNavigationTest extends SeleniumBaseTest {
    public AccessionDetail accessionDetail;
    public SuperSearch superSearch;
    public PatientDemographics patientDemographics;
    public ClientPricingConfiguration clientPricingConfiguration;
    public ClientTransactionDetail clientTransactionDetail;
    public EligibilityConfiguration eligibilityConfiguration;
    public ReasonCode reasonCode;
    public TestCode testCode;
    public SubmissionAndRemittance submissionAndRemittance;
    public PayorDemographics payorDemographics;
    public AccessionHistoryLog accessionHistoryLog;
    public SingleStatement singleStatement;
    public AccnTestUpdate accnTestUpdate;
    public PhysicianLicense physicianLicense;
    public DocumentUploadAndStorage documentUploadAndStorage;
    public PricingConfig pricingConfig;
    public Actions builder;
    public MenuLinkNavigation menuLinkNavigation;
    public EPSearch epSearch;
    public ClientDemographics clientDemographics;
    public EligibilityResponseTranslation eligibilityResponseTranslation;
    public Deposits deposits;
    public ClientContactManager clientContactManager;
    public EligCensusConfig eligCensusConfig;
    public SubmissionConfiguration submissionConfiguration;
    public NonClientSummary nonClientSummary;
    public NotesPromisedPayments notesPromisedPayments;
    public ClientPayments clientPayments;
    public ExpectPriceDiscrepancies expectPriceDiscrepancies;
    public NonClientPayments nonClientPayments;
    public PatientPayments patientPayments;
    public FinancialManagement financialManagement;
    public EpSummary epSummary;
    public ClientBillingRules clientBillingRules;
    public ClientBatchSingleDemandStatement clientBatchSingleDemandStatement;
    public FeeSchedule feeSchedule;
    public IncrementalPricing incrementalPricing;
    public SpecialPriceTable specialPriceTable;
    public LogoConfiguration logoConfiguration;
    public PaymentSearch paymentSearch;
    public ConsolidationRules consolidationRules;
    public EomSchedule eomSchedule;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"ssoUsername", "ssoPassword"})
    public void beforeMethod(String ssoUsername, String ssoPassword, Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            logIntoSso(ssoUsername, ssoPassword);
        } catch (Exception e) {
            logger.error("Error running BeforeMethod", e);
        }
    }

    @Test(priority = 1, description = "Accession - Accession Processing - Detail - Landing page")
    public void testXPR_70() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Accession Menu and click Detail");
        builder = new Actions(driver);
        hoverOverAccessionMenu().perform();
        builder.moveToElement(menuLinkNavigation.accessionDetailLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Accession Detail Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        accessionDetail = new AccessionDetail(driver, config, wait);
        assertTrue(isElementPresent(accessionDetail.accnIdInput(), 5), "        The Accession Detail should be displayed");
    }

    @Test(priority = 1, description = "Accession - Accession Processing - Super Search - Landing page")
    public void testXPR_71() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Accession Menu and click Super Search");
        builder = new Actions(driver);
        hoverOverAccessionMenu().perform();
        builder.moveToElement(menuLinkNavigation.accessionSuperSearchLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Super Search Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        superSearch = new SuperSearch(driver, config, wait);
        assertTrue(isElementPresent(superSearch.searchIdInputPF(), 5), "        The Super Search page should be displayed");
    }

    @Test(priority = 1, description = "Accession - Error Processing - EP Search - Landing page")
    public void testXPR_72() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Accession Menu and click EP Search");
        builder = new Actions(driver);
        hoverOverAccessionMenu().perform();
        builder.moveToElement(menuLinkNavigation.accessionEPSearchLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on EP Search Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        epSearch = new EPSearch(driver, wait);
        assertTrue(isElementPresent(epSearch.epSearchSearchIdInput(), 5), "        The EP Search page should be displayed");
    }

    @Test(priority = 1, description = "Accession - Patient Processing - Patient Demographics - Landing page")
    public void testXPR_73() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Accession Menu and click Patient Demographics");
        builder = new Actions(driver);
        hoverOverAccessionMenu().perform();
        builder.moveToElement(menuLinkNavigation.accessionPatientDemographicsLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Patient Demographics Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        patientDemographics = new PatientDemographics(driver, wait);
        assertTrue(isElementPresent(patientDemographics.epiInput(), 5), "        The Patient Demographics page should be displayed");
    }

    @Test(priority = 1, description = "Client - Client Processing - Demographics - Landing page")
    public void testXPR_74() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Client Menu and click Demographics");
        builder = new Actions(driver);
        hoverOverClientMenu().perform();
        builder.moveToElement(menuLinkNavigation.clientDemographicsLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Demographics Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        clientDemographics = new ClientDemographics(driver, wait);
        assertTrue(isElementPresent(clientDemographics.clientDemographicsLoadClientIdInput(), 5), "        The Demographics page should be displayed");
    }

    @Test(priority = 1, description = "Client - Client Processing - Pricing Configuration - Landing page")
    public void testXPR_75() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Client Menu and click Pricing Configuration");
        builder = new Actions(driver);
        hoverOverClientMenu().perform();
        builder.moveToElement(menuLinkNavigation.pricingConfigLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Pricing Configuration Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        clientPricingConfiguration = new ClientPricingConfiguration(driver, wait);
        assertTrue(isElementPresent(clientPricingConfiguration.loadClientSectionClientIdInput(), 5), "        The Pricing Configuration page should be displayed");
    }

    @Test(priority = 1, description = "Client - Client Processing - Submission Configuration - Landing page")
    public void testXPR_76() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Client Menu and click Submission Configuration");
        builder = new Actions(driver);
        hoverOverClientMenu().perform();
        builder.moveToElement(menuLinkNavigation.submissionConfigurationLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Submission Configuration Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        submissionConfiguration = new SubmissionConfiguration(driver, wait);
        assertTrue(isElementPresent(submissionConfiguration.loadClientSectionClientIdInput(), 5), "        The Submission Configuration page should be displayed");
    }

    @Test(priority = 1, description = "Client - Client Processing - Transaction Detail - Landing page")
    public void testXPR_77() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Client Menu and click Transaction Detail");
        builder = new Actions(driver);
        hoverOverClientMenu().perform();
        builder.moveToElement(menuLinkNavigation.transactionDetailLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Transaction Detail Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
        assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5), "        The Transaction Detail page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - Tables - Eligibility Configuration - Landing page")
    public void testXPR_78() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Eligibility Configuration");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder.moveToElement(menuLinkNavigation.eligibilityConfigurationLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Eligibility Configuration Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        eligibilityConfiguration = new EligibilityConfiguration(driver, wait, methodName);
        assertTrue(isElementPresent(eligibilityConfiguration.eligSvcIDInputInLoadPage(), 5), "        The Eligibility Configuration page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - Tables - Eligibility Translation - Landing page")
    public void testXPR_79() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Eligibility Translation");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder.moveToElement(menuLinkNavigation.eligibilityTranslationLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Eligibility Translation Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        eligibilityResponseTranslation = new EligibilityResponseTranslation(driver, config, wait);
        assertTrue(isElementPresent(eligibilityResponseTranslation.eligSvcIDInputInLoadPage(), 5), "        The Eligibility Translation page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - Tables - Reason Code - Landing page")
    public void testXPR_80() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Reason Code");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder.moveToElement(menuLinkNavigation.reasonCodeLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Reason Code Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        reasonCode = new ReasonCode(driver, wait);
        assertTrue(isElementPresent(reasonCode.errorGroupDropDown(), 5), "        The Reason Code page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - Pricing - Test Code - Landing page")
    public void testXPR_81() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Test Code");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder
                .moveToElement(menuLinkNavigation.testCodeLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Test Code Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        testCode = new TestCode(driver, config, wait);
        assertTrue(isElementPresent(testCode.testId(), 5), "        The Test Code page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - System Management - Submission and Remittance - Landing page")
    public void testXPR_82() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Submission and Remittance");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder.moveToElement(menuLinkNavigation.submissionRemitLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Submission and Remittance Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        submissionAndRemittance = new SubmissionAndRemittance(driver, wait);
        assertTrue(isElementPresent(submissionAndRemittance.creationFromDateInput(), 5), "        The Submission and Remittance page should be displayed");
    }

    @Test(priority = 1, description = "Payment - Payments - Deposits - Landing page")
    public void testXPR_83() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Payments Menu and click Deposits");
        builder = new Actions(driver);
        hoverOverPaymentMenu().perform();
        builder.moveToElement(menuLinkNavigation.paymentDepositLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Deposits Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        deposits = new Deposits(driver, wait);
        assertTrue(isElementPresent(deposits.depositIDInput(), 5), "        The Deposit page should be displayed");
    }

    @Test(priority = 1, description = "Payment - Payments - Non-Client Summary - Landing page")
    public void testXPR_84() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Payments Menu and click Non-Client Summary");
        builder = new Actions(driver);
        hoverOverPaymentMenu().perform();
        builder.moveToElement(menuLinkNavigation.nonClientSummaryLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Non-Client Summary Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        nonClientSummary = new NonClientSummary(driver, wait);
        assertTrue(isElementPresent(nonClientSummary.batchIDSelect(), 5), "        The Non-Client Summary page should be displayed");
    }

    @Test(priority = 1, description = "Payor - Demographics - Demographics - Landing page")
    public void testXPR_85() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Payor Menu and click Demographics");
        builder = new Actions(driver);
        hoverOverPayorMenu().perform();
        builder.moveToElement(menuLinkNavigation.payorDemographicsLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Payor Demographics Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        payorDemographics = new PayorDemographics(driver);
        assertTrue(isElementPresent(payorDemographics.pyrIDInput(), 5), "        The Payor Demographics page should be displayed");
    }

    @Test(priority = 1, description = "Financial Management - End of Month - Closing Package - Landing page")
    public void testXPR_86() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Financial Management Menu and click Closing Package");
        builder = new Actions(driver);
        hoverOverFinancialManagementMenu().perform();
        builder.moveToElement(menuLinkNavigation.closingPackageLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();
//        Thread.sleep(3000);

        logger.info("*** Step 2 Expected Result - Verify that it's on Closing Package Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        financialManagement = new FinancialManagement(driver, wait);
        driver.switchTo().frame(financialManagement.rpmiFrame());
        assertTrue(isElementPresent(financialManagement.accountingPeriodDropdown(), 5), "        The EOM Facility Closing Package page should be displayed");
    }

    @Test(priority = 1, description = "Accession - Accession Processing - History Log - Landing page")
    public void testXPR_87() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Accession Menu and click History Log");
        builder = new Actions(driver);
        hoverOverAccessionMenu().perform();
        builder.moveToElement(menuLinkNavigation.historyLogLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on History Log Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        accessionHistoryLog = new AccessionHistoryLog(driver, wait);
        assertTrue(isElementPresent(accessionHistoryLog.accnIDInput(), 5), "        The Accession History Log page should be displayed");
    }

    @Test(priority = 1, description = "Accession - Accession Processing - Notes & Promised Payments - Landing page")
    public void testXPR_88() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Accession Menu and click Notes & Promised Payments");
        builder = new Actions(driver);
        hoverOverAccessionMenu().perform();
        builder.moveToElement(menuLinkNavigation.notesandPromisedPaymentsLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Notes & Promised Payments Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        notesPromisedPayments = new NotesPromisedPayments(driver, wait);
        assertTrue(isElementPresent(notesPromisedPayments.loadAccessionSectionAccessionIdInput(), 5), "        The Notes & Promised Payments page should be displayed");
    }

    @Test(priority = 1, description = "Accession - Accession Processing - Single Statement - Landing page")
    public void testXPR_89() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Accession Menu and click Single Statement");
        builder = new Actions(driver);
        hoverOverAccessionMenu().perform();
        builder.moveToElement(menuLinkNavigation.singleStatementLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Single Statement Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        singleStatement = new SingleStatement(driver, wait, methodName);
        assertTrue(isElementPresent(singleStatement.loadAccnSecAccnIdInput(), 5), "        The Single Statement page should be displayed");
    }

    @Test(priority = 1, description = "Accession - Order Processing - Test Update - Landing page")
    public void testXPR_90() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Accession Menu and click Test Update");
        builder = new Actions(driver);
        hoverOverAccessionMenu().perform();
        builder.moveToElement(menuLinkNavigation.testUpdateLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Test Update Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        accnTestUpdate = new AccnTestUpdate(driver, wait);
        assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 5), "        The Accession Test Update page title should be displayed");
    }

    @Test(priority = 1, description = "Client - Client Processing - Contact Manager - Landing page")
    public void testXPR_91() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Client Menu and click Contact Manager");
        builder = new Actions(driver);
        hoverOverClientMenu().perform();
        builder.moveToElement(menuLinkNavigation.contactManagerLink())
                .click()
                .build().perform();
        moveAwayFromMenu();

        logger.info("*** Step 2 Expected Result - Verify that it's on Contact Manager Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        clientContactManager = new ClientContactManager(driver, wait);
        assertTrue(isElementPresent(clientContactManager.loadClientSectionClientIdInput(), 5), "        The client Contact Manager page title should be displayed");
    }

    @Test(priority = 1, description = "Client - Client Processing - Eligibility Census Configuration - Landing page")
    public void testXPR_98() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Client Menu and click Eligibility Census Configuration");
        builder = new Actions(driver);
        hoverOverClientMenu().perform();
        builder.moveToElement(menuLinkNavigation.eligibilityCensusConfigurationLink())
                .click()
                .build().perform();
        moveAwayFromMenu();

        logger.info("*** Step 2 Expected Result - Verify that it's on Eligibility Census Configuration Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        eligCensusConfig = new EligCensusConfig(driver, wait);
        assertTrue(isElementPresent(eligCensusConfig.eligibilityCensusConfigLoadClientIdInput(), 5), "        The Client Eligibility Census Configuration page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - Tables - Physician License - Landing page")
    public void testXPR_106() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Physician License");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder.moveToElement(menuLinkNavigation.physicianLicenseLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Physician License Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        physicianLicense = new PhysicianLicense(driver);
        assertTrue(isElementPresent(physicianLicense.npiIdLookupInput(), 5), "        The File Maintenance Physician License page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - System Management - Document Upload and Storage - Landing page")
    public void testXPR_131() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Document Upload and Storage");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder.moveToElement(menuLinkNavigation.documentUploadAndStorage())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Document Upload and Storage Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        documentUploadAndStorage = new DocumentUploadAndStorage(driver, wait);
        assertTrue(isElementPresent(documentUploadAndStorage.viewByDropdownList(), 5), "        The File Maintenance Document Upload and Storage page should be displayed");
    }

    @Test(priority = 1, description = "Payment - Payments - Client Payments - Landing page")
    public void testXPR_134() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Payment and click Client Payments");
        builder = new Actions(driver);
        hoverOverPaymentMenu().perform();
        builder.moveToElement(menuLinkNavigation.clientPayments())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Client Payments Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        clientPayments = new ClientPayments(driver, wait);
        assertTrue(isElementPresent(clientPayments.loadClientPaymentsSectionBatchIdDdl(), 5), "        The Client Payments page should be displayed");
    }

    @Test(priority = 1, description = "Payment - Payments - Expect Price Discrepancies - Landing page")
    public void testXPR_145() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Payment and click Expect Price Discrepancies");
        builder = new Actions(driver);
        hoverOverPaymentMenu().perform();
        builder.moveToElement(menuLinkNavigation.expectPriceDiscrepancies())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Expect Price Discrepancies Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        expectPriceDiscrepancies = new ExpectPriceDiscrepancies(driver, wait);
        assertTrue(isElementPresent(expectPriceDiscrepancies.loadExpectPriceDescrepenciesSectionBatchIdDdl(), 5), "        The Payment Expect Price Discrepancies page should be displayed");
    }

    @Test(priority = 1, description = "Payment - Payments - Non-Client Payments - Landing page")
    public void testXPR_146() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Payment and click Non-Client Payments");
        builder = new Actions(driver);
        hoverOverPaymentMenu().perform();
        builder.moveToElement(menuLinkNavigation.nonClientPayments())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Non-Client Payments Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        nonClientPayments = new NonClientPayments(driver, wait);
        assertTrue(isElementPresent(nonClientPayments.loadNonClientPaymentsSectionBatchIdDdl(), 5), "        The Payments Non-Client Payments page should be displayed");
    }

    @Test(priority = 1, description = "Payment - Payments - Patient Payments - Landing page")
    public void testXPR_147() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Payment and click Patient Payments");
        builder = new Actions(driver);
        hoverOverPaymentMenu().perform();
        builder.moveToElement(menuLinkNavigation.patientPaymentsLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Patient Payments Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        patientPayments = new PatientPayments(driver, wait);
        assertTrue(isElementPresent(patientPayments.loadBatchSectionBatchIdDdl(), 5), "        The Payments Patient Payments page should be displayed");
    }

    @Test(priority = 1, description = "Payor - Demographics - Pricing Configuration - Landing page")
    public void testXPR_149() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Payor and click Pricing Configuration");
        builder = new Actions(driver);
        hoverOverPayorMenu().perform();
        builder.moveToElement(menuLinkNavigation.pricingConfigurationLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Pricing Configuration Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        pricingConfig = new PricingConfig(driver, wait);
        assertTrue(isElementPresent(pricingConfig.payorIdInput(), 5), "        The Payor Pricing Configuration page should be displayed");
    }

    @Test(priority = 1, description = "Accession - Error Processing - EP Summary - Landing page")
    public void testXPR_150() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results - Verify that the user id logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Accession and click EP Summary");
        builder = new Actions(driver);
        hoverOverAccessionMenu().perform();
        builder.moveToElement(menuLinkNavigation.epSummaryLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on EP Summary Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        epSummary = new EpSummary(driver, wait);
        assertTrue(isElementPresent(epSummary.errorDate(), 5), "        The Accession EP Summary page should be displayed");
    }

    @Test(priority = 1, description = "Client - Client Processing - Billing Rules - Landing page")
    public void testXPR_152() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results - Verify that the user id logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Client and click Billing Rules");
        builder = new Actions(driver);
        hoverOverClientMenu().perform();
        builder.moveToElement(menuLinkNavigation.billingRulesLink())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Billing Rules Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        clientBillingRules = new ClientBillingRules(driver, wait);
        assertTrue(isElementPresent(clientBillingRules.clientBillingRulesClientIdInput(), 5), "        The Client Billing Rules page should be displayed");
    }

    @Test(priority = 1, description = "Client - Client Inquiry - Batch Single Demand Statement - Landing page")
    public void testXPR_153() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results - Verify that the user id logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Client and click Batch Single Demand Statement");
        builder = new Actions(driver);
        hoverOverClientMenu().perform();
        builder.moveToElement(menuLinkNavigation.batchSingleDemandStatement())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Batch Single Demand Statement Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        clientBatchSingleDemandStatement = new ClientBatchSingleDemandStatement(driver, wait);
        assertTrue(isElementPresent(clientBatchSingleDemandStatement.addBtn(), 5), "        The Client Batch Single Demand Statement page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - Pricing - Fee Schedule - Landing page")
    public void testXPR_155() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Fee Schedule");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder.moveToElement(menuLinkNavigation.feeSchedule())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Fee Schedule Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        feeSchedule = new FeeSchedule(driver, wait);
        assertTrue(isElementPresent(feeSchedule.feeScheduleIdInput(), 5), "        The File Maintenance Fee Schedule page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - Pricing - Incremental Price Table - Landing page")
    public void testXPR_167() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Incremental Price Table");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder.moveToElement(menuLinkNavigation.incrementalPriceTable())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Incremental Price Table Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        incrementalPricing = new IncrementalPricing(driver, config, wait);
        assertEquals(incrementalPricing.inPriTitle().getText().trim(), "Incremental Price Table", "        The File Maintenance Incremental Price Table page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - Pricing - Special Price Table - Landing page")
    public void testXPR_168() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Special Price Table");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder.moveToElement(menuLinkNavigation.specialPriceTable())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Special Price Table Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        specialPriceTable = new SpecialPriceTable(driver, wait);
        assertEquals(specialPriceTable.loadSpecialPriceTablePageTitleTxt().getText().trim(), "Special Price Table", "        The File Maintenance Special Price Table page should be displayed");
    }

    @Test(priority = 1, description = "File Maintenance - System Management - Logo Configuration - Landing page")
    public void testXPR_169() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to File Maintenance Menu and click Logo Configuration");
        builder = new Actions(driver);
        hoverOverFileMaintMenu().perform();
        builder.moveToElement(menuLinkNavigation.logoConfiguration())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Logo Configuration");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        logoConfiguration = new LogoConfiguration(driver, wait);
        assertTrue(isElementPresent(logoConfiguration.logoConfigLoadPageDocumentTypeDDL(), 5), "        The File Maintenance Logo Configuration page should be displayed");
    }

    @Test(priority = 1, description = "Payment - Payments - Payment Search - Landing page")
    public void testXPR_170() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Payment and click Payment Search");
        builder = new Actions(driver);
        hoverOverPaymentMenu().perform();
        builder.moveToElement(menuLinkNavigation.paymentSearch())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Payment Search Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        paymentSearch = new PaymentSearch(driver, wait);
        assertTrue(isElementPresent(paymentSearch.PaymentTypeDropDown(), 5), "        The Payments Payment Search page should be displayed");
    }

    @Test(priority = 1, description = "Payor - Consolidation - Procedure Code Rules - Landing page")
    public void testXPR_178() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Payor and click Procedure Code Rules");
        builder = new Actions(driver);
        hoverOverPayorMenu().perform();
        builder.moveToElement(menuLinkNavigation.procedureCodeRules())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on Procedure Code Rules Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        consolidationRules = new ConsolidationRules(driver, wait);
        assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5), "        The Payor Procedure Code Rules page should be displayed");
    }

    @Test(priority = 1, description = "Financial Management - End of Month - EOM Schedule - Landing page")
    public void testXPR_191() throws Exception {
        menuLinkNavigation = new MenuLinkNavigation(driver, wait);

        logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
        Assert.assertTrue(isElementPresent(menuLinkNavigation.xifinLogo(), 5), "        Xifin Logo should be available.");

        logger.info("*** Step 2 Action - Navigate to Financial Management Menu and click EOM Schedule");
        builder = new Actions(driver);
        hoverOverFinancialManagementMenu().perform();
        builder.moveToElement(menuLinkNavigation.eomSchedule())
                .click()
                .build().perform();
        moveAwayFromMenu().perform();

        logger.info("*** Step 2 Expected Result - Verify that it's on EOM Schedule Page");
        driver.switchTo().frame(menuLinkNavigation.contentFrame());
        eomSchedule = new EomSchedule(driver, wait);
        assertTrue(isElementPresent(eomSchedule.yearDropDown(), 5), "        The EOM Facility EOM Schedule page should be displayed");
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public Action hoverOverAccessionMenu() {
        return builder
                .moveToElement(menuLinkNavigation.accessionMenu())
                .build();
    }

    public Action moveAwayFromMenu() {
        return builder
                .moveToElement(menuLinkNavigation.xifinLogo())
                .build();
    }

    public Action hoverOverClientMenu() {
        return builder
                .moveToElement(menuLinkNavigation.clientMenu())
                .build();
    }

    public Action hoverOverFileMaintMenu() {
        return builder
                .moveToElement(menuLinkNavigation.fileMaintenance())
                .build();
    }

    public Action hoverOverPaymentMenu() {
        return builder.moveToElement(menuLinkNavigation.paymentMenu())
                .build();
    }

    public Action hoverOverPayorMenu() {
        return builder.moveToElement(menuLinkNavigation.payorMenu())
                .build();
    }

    public Action hoverOverFinancialManagementMenu() {
        return builder.moveToElement(menuLinkNavigation.financialManagementMenu())
                .build();
    }

}