package com.newXp.tests;

import com.mbasys.mars.utility.cache.CacheMap;
import com.overall.pciplugin.PciPluginConfig;
import com.overall.pciplugin.PciPluginConfig.IframeEnum;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.overall.pciplugin.PciPluginConfig.FieldConfigurationEnum.HIDDEN;
import static com.overall.pciplugin.PciPluginConfig.FieldConfigurationEnum.REQUIRED;

public class PciPluginEmbedTest extends SeleniumBaseTest {
    // Name this separate from PCI-WS Soap Automation tests so that can tell the difference in ELC_PMT_DETAIL
    private static final String USER_ID = "xqatester";
    private static final String ELEMENT_ID_CONFIG_PARAM = "pmtbox";
    private static final int OVERRIDE_TIMEOUT_SECONDS = 0;

    private PciPluginConfig pciPluginConfig;
    private int resultNumber = 0;
    private int defaultTimeoutSeconds;
    private String orgAlias;
    private IframeEnum currentFrame = IframeEnum.DEFAULT;
    private Map<String, PciPluginConfig.FieldConfigurationEnum> fieldConfigurationMap;
    private boolean isTokenizerIframePresent = false;


    /**
     * Method to run before every test in this suite. This will reinitialize the PciPluginConfig and navigate to the
     * test harness, as well as set the initial values into elements on the test harness.
     *
     * @param timeout
     * @param method
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters({"timeout", "gatewayName"})
    public void beforeMethod(int timeout, Method method,String gatewayName ) {
        logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
        this.defaultTimeoutSeconds = timeout;
        if(gatewayName == null){

            gatewayName = "FirstData";
        }
        try {
            this.orgAlias = config.getProperty(PropertyMap.ORGALIAS);
            pciPluginConfig = new PciPluginConfig(driver, config, ELEMENT_ID_CONFIG_PARAM);
            switchToDefaultGateway(gatewayName);
            clearDataCacheByCacheName(CacheMap.ELC_PMT_GATEWAY);
            navigateToTestHarness();
            initializeTestHarness();
            clearResults();
        } catch (Exception e) {
            Assert.fail("Error initializing test", e);
        }
    }

    /**
     * Initialize the payment iframe in each of the default screen configurations using the buttons on the test harness.
     * Verify that the iframe is initialized as expected in each case.
     *
     * @throws Exception
     */
    @Test(priority = 1, description = "Verify Default Configurations")
    public void testXPR_1949() throws Exception {
        logger.info("===== Testing - testXPR_1949:PCI Plugin - Verify Default Configurations =====");

        // Set XP Accn Detail
        createDefaultXpAccnDetailPaymentFrame();
        verifyCreditCardAndAchPaymentsAllowedWithMinimumRequiredFields();

        // Set XP Cln Detail
        createDefaultXpClnDetailPaymentFrame();
        verifyCreditCardAndAchPaymentsAllowedWithMinimumRequiredFields();

        // Set Patient Portal prepayment
        createDefaultPatientPortalPaymentFrame();
        verifyCreditCardAndAchPaymentsAllowedWithMinimumRequiredFields();

        // Set Client Portal
        createDefaultClientPortalPaymentFrame();
        verifyCreditCardAndAchPaymentsAllowedWithMinimumRequiredFields();
    }

    /**
     * Initialize a PP iframe and verify that a credit card transaction is allowed by the frame
     * A CC prepayment transaction is submitted to PCI-WS.
     *
     * @throws Exception
     */
    @Test(priority = 1, description = "Credit Card Prepayment")
    public void testXPR_1950() throws Exception {
        logger.info("===== Testing - XPR-1950:PCI Plugin - Credit Card Prepayment =====");

        // Set Patient Portal prepayment
        initializeDefaultConfiguration(pciPluginConfig.setPPButton(), pciPluginConfig.prepayRadioButton(), false);

        // Disable ACH Payments for this iframe
        pciPluginConfig.echeckEnabledInput().sendKeys("false");

        createAndSwitchToIframe();

        // Enter generic valid CC information
        enterGenericCreditCardPaymentInformationIntoIframe(pciPluginConfig.getIframe());

        submitPaymentThroughIframe(pciPluginConfig.getIframe(), true);

        checkResultsAfterPayment(true);

        incrementResultNumber();
    }

    /**
     * Initialize a PP iframe with CC payments disabled and verify that an ACH transaction is allowed by the frame
     * An ACH prepayment transaction is submitted to PCI-WS.
     *
     * @throws Exception
     */
    @Test(priority = 1, description = "ACH Prepayment")
    public void testXPR_1951() throws Exception {
        logger.info("===== Testing - XPR-1951:PCI Plugin - ACH Prepayment =====");

        // Set Patient Portal prepayment
        createDefaultPatientPortalPaymentFrame();

        // Enter generic valid ACH information
        populateOnlyRequiredFieldsAch(pciPluginConfig.getIframe());

        submitPaymentThroughIframe(pciPluginConfig.getIframe(), true);

        checkResultsAfterPayment(true);

        incrementResultNumber();
    }

    /**
     * Initialize a PP iframe with CC payments disabled and verify that an ACH transaction is allowed by the frame
     * No actual payment is submitted to PCI-WS.
     *
     * @throws Exception
     */
    @Test(priority = 1, description = "Load Iframe with CC Payments Disabled")
    public void testXPR_1952() throws Exception {
        logger.info("===== Testing - XPR-1952:PCI Plugin - Load Iframe with CC Payments Disabled =====");

        // Set Patient Portal prepayment
        initializeDefaultConfiguration(pciPluginConfig.setPPButton(), pciPluginConfig.prepayRadioButton(), false);

        // Disable ACH Payments for this iframe
        pciPluginConfig.ccEnabledInput().sendKeys("false");

        createAndSwitchToIframe();

        // Enter generic valid Echeck information into all required fields
        populateOnlyRequiredFieldsAch(pciPluginConfig.getIframe());

        submitPaymentThroughIframe(pciPluginConfig.getIframe(), false);
    }

    /**
     * Initialize a PP iframe with ACH payments disabled and verify that a credit card transaction is allowed by the frame
     * No actual payment is submitted to PCI-WS.
     *
     * @throws Exception
     */
    @Test(priority = 1, description = "Load Iframe with ACH Payments Disabled")
    public void testXPR_1953() throws Exception {
        logger.info("===== Testing - XPR-1953:PCI Plugin - Load Iframe with ACH Payments Disabled =====");

        // Set Patient Portal prepayment
        initializeDefaultConfiguration(pciPluginConfig.setPPButton(), pciPluginConfig.prepayRadioButton(), false);

        // Disable ACH Payments for this iframe
        pciPluginConfig.echeckEnabledInput().sendKeys("false");

        createAndSwitchToIframe();

        // Enter generic valid CC information in all the required fields
        populateOnlyRequiredFieldsCreditCard(pciPluginConfig.getIframe());

        submitPaymentThroughIframe(pciPluginConfig.getIframe(), false);
    }

    /**
     * Initialize a PP iframe with PayPal button and verify that PayPal transaction is done through PayPal Checkout
     * A Braintree/PayPal transaction is submitted to PCI-WS
     * @throws Exception
     */
    @Test(priority = 1, description = "Add PayPal In PCI Iframe")
    public void testXPR_1954() throws Exception {

        // Set Patient Portal prepayment
        createDefaultPatientPortalPaymentFrame();

        logger.info("==Testing - testXPR_1954:PCI Plugin for PayPal ==Verify PayPal button configurations");
        GetPayPal();
        switchToPaypalWin();
        Thread.sleep(10000);
        checkResultsAfterPayment(true);
        incrementResultNumber();
        logger.info("successful Response ");

    }

    /**
     * Initialize a PP iframe with PayPalPayLater button and verify that PayPalPayLater(Pay in 4 installments) transaction is done through PayPal Checkout
     * A Braintree/PayPal transaction is submitted to PCI-WS
     * @throws Exception
     */
    @Test(priority = 1, description = "Add PalPayLater In PCI Iframe")
    public void testXPR_1955() throws Exception {

        createDefaultPatientPortalPaymentFrameForPayLater();

        logger.info("===== Testing - testXPR_1955:PCI Plugin for PayPal/PayLater -Verify PayPal/PayLater button configurations =====");
        GetPayPalPayLater();

        switchToPayLaterWin();
        Thread.sleep(10000);

        checkResultsAfterPayment(true);

        incrementResultNumber();
        logger.info("successful Response ");

    }

    /**
     * Populate all the required fields for both CC and ACH and ensure that payments are allowed in both cases.
     *
     * @throws InterruptedException
     */
    private void verifyCreditCardAndAchPaymentsAllowedWithMinimumRequiredFields() throws InterruptedException {
        // Ensure that a pmt can be submitted with the minimum required CC fields (but do not submit)
        populateOnlyRequiredFieldsCreditCard(pciPluginConfig.getIframe());
        submitPaymentThroughIframe(pciPluginConfig.getIframe(), false);
        // Ensure that a pmt can be submitted with the minimum required ACH fields (but do not submit)
        populateOnlyRequiredFieldsAch(pciPluginConfig.getIframe());
        submitPaymentThroughIframe(pciPluginConfig.getIframe(), false);
    }

    /**
     * Click the "Set XP Cln Detail" button and then switch to the new iframe
     *
     * @throws InterruptedException
     */
    private void createDefaultXpClnDetailPaymentFrame() throws InterruptedException {
        switchToFrame(IframeEnum.DEFAULT);
        initializeDefaultConfiguration(pciPluginConfig.setXPClnButton(), pciPluginConfig.clnRadioButton(), true);
    }

    /**
     * Click the "Set Patient Portal" button and then switch to the new iframe
     *
     * @throws InterruptedException
     */
    private void createDefaultPatientPortalPaymentFrame() throws InterruptedException {
        switchToFrame(IframeEnum.DEFAULT);
        initializeDefaultConfiguration(pciPluginConfig.setPPButton(), pciPluginConfig.prepayRadioButton(), true);
    }

    /**
     * Click the "Set Patient Portal" button and then switch to the new iframe and enter amount greater than $30 to see PayPal PayLater button.
     *
     * @throws InterruptedException
     */
    private void createDefaultPatientPortalPaymentFrameForPayLater() throws InterruptedException {
        switchToFrame(IframeEnum.DEFAULT);
        initializeDefaultConfigurationforPayPalPaylater(pciPluginConfig.setPPButton(), pciPluginConfig.prepayRadioButton(), true);
    }

    /**
     * Click the "Set XP Accn Detail" button and then switch to the new iframe
     *
     * @throws InterruptedException
     */
    private void createDefaultXpAccnDetailPaymentFrame() throws InterruptedException {
        switchToFrame(IframeEnum.DEFAULT);
        initializeDefaultConfiguration(pciPluginConfig.setXPAccnButton(), pciPluginConfig.accnRadioButton(), true);
    }

    /**
     * Click the "Set Client Portal" button and then switch to the new iframe
     *
     * @throws InterruptedException
     */
    private void createDefaultClientPortalPaymentFrame() throws InterruptedException {
        switchToFrame(IframeEnum.DEFAULT);
        initializeDefaultConfiguration(pciPluginConfig.setCPButton(), pciPluginConfig.clnRadioButton(), true);
    }

    /**
     * Initialize one of the test harness's default configurations. If initializeIframe is true, initialize and switch
     * to the iframe, otherwise do not create the frame tio allow more time for specific configurations
     *
     * @param targetConfig
     * @param orderDetailsRadioButton
     * @param initializeFrame
     * @throws InterruptedException
     */
    private void initializeDefaultConfiguration(WebElement targetConfig, WebElement orderDetailsRadioButton, boolean initializeFrame) throws InterruptedException {
        clickOnConfigurationButton(targetConfig);

        verifyOrderDetailType(orderDetailsRadioButton);

        if (initializeFrame) {
            createAndSwitchToIframe();
        }
    }

    /**
     * Initialize one of the test harness's  configurations to initiate PayPalPayLater button.. If initializeIframe is true, initialize and switch
     * to the iframe, otherwise do not create the frame to allow more time for specific configurations
     *
     * @param targetConfig
     * @param orderDetailsRadioButton
     * @param initializeFrame
     * @throws InterruptedException
     */

    private void initializeDefaultConfigurationforPayPalPaylater(WebElement targetConfig, WebElement orderDetailsRadioButton, boolean initializeFrame) throws InterruptedException {

        clickOnConfigurationButton(targetConfig);

        verifyOrderDetailType(orderDetailsRadioButton);
        pciPluginConfig.amountInput().clear();
        pciPluginConfig.amountInput().sendKeys("35");

        if (initializeFrame) {
            createAndSwitchToIframe();
        }
    }

    /**
     * Verify that this payment received a response and then log that response.
     * Verify that the payment was successful when assertPaymentSuccess == true by checking the button for the "green" CSS class,
     * which indicates a successful payment
     *
     * @param assertPaymentSuccess
     */
    private void checkResultsAfterPayment(boolean assertPaymentSuccess) {
        try {
            switchToFrame(IframeEnum.DEFAULT);
            logger.info("Checking results for result #" + resultNumber);
            wait.until(ExpectedConditions.elementToBeClickable(pciPluginConfig.resultButton(resultNumber)));

            logger.info(pciPluginConfig.resultText().getAttribute("value"));

            if (assertPaymentSuccess) {
                Assert.assertTrue(elementHasClass(pciPluginConfig.resultButton(resultNumber), "green"), "PCI Transaction was not successful.");
                logger.info("Received \"APPROVED\" response from PCI Transaction.");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception while checking results", e);
        }
    }

    /**
     * Return true if the provided element contains the specified class
     *
     * @param element
     * @param className
     * @return
     */
    private boolean elementHasClass(WebElement element, String className) {
        for (String c : element.getAttribute("class").split(" ")) {
            if (c.equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Click on one of the pre-set configuration buttons on the test harness screen.
     * This will set many of the configuration fields with the expected default configuration for that screen
     *
     * @param configButton
     * @throws InterruptedException
     */
    private void clickOnConfigurationButton(WebElement configButton) throws InterruptedException {
        logger.info("Click on '" + configButton.getText() + "' button");
        scrollToElement(configButton);
        configButton.click();
        Thread.sleep(1000);
    }

    /**
     * Verify that the specified order detail type radio button is selected
     *
     * @param radioButton
     * @throws InterruptedException
     */
    private void verifyOrderDetailType(WebElement radioButton) throws InterruptedException {
        logger.info("Verify that the '" + radioButton.getAttribute("id") + "' Order Details radio button is selected");
        scrollToElement(radioButton);
        Assert.assertTrue(radioButton.isSelected());
    }

    /**
     * Clear any pre-existing results from the screen
     */
    private void clearResults() {
        if (pciPluginConfig.clearResultsButton().isDisplayed()) {
            logger.info("Clear Results button is displayed, clearing results now");
            pciPluginConfig.clearResultsButton().click();
        }
        resultNumber = 0;
    }

    /**
     * Navigate to the PCI Payment Plugin test harness
     *
     * @throws MalformedURLException
     */
    private void navigateToTestHarness() throws MalformedURLException {
        logger.info("Navigate to PCI Test Harness in Confluence");
        pciPluginConfig.navigateToConfluenceTestHarness();

        logger.info("Verify that the PCI Test Harness is displayed with orgalias input");
        Assert.assertTrue(pciPluginConfig.orgAliasInput().isDisplayed(), "The orgAlias input is not displayed on the test harness.");
    }

    /**
     * Initialize the test harness with an orgAlias and userId, and set the target environment (QA, local, etc.)
     *
     * @throws InterruptedException
     */
    private void initializeTestHarness() throws InterruptedException {
        WebElement targetRadioButton;

        // If this is the "dev" orgAlias, then use the localhost setting, else use QA
        if ("dev".equalsIgnoreCase(orgAlias)) {
            targetRadioButton = pciPluginConfig.localhostRadioButton();
        } else {
            targetRadioButton = pciPluginConfig.qaRadioButton();
        }

        logger.info("Selecting target environment \"" + targetRadioButton.getAttribute("id") + "\" with orgalias \""
                + orgAlias + "\" and user \"" + USER_ID + "\"");
        scrollToElement(targetRadioButton);
        targetRadioButton.click();
        scrollToElement(pciPluginConfig.orgAliasInput());
        pciPluginConfig.orgAliasInput().clear();
        pciPluginConfig.orgAliasInput().sendKeys(orgAlias);
        scrollToElement(pciPluginConfig.userIdInput());
        pciPluginConfig.userIdInput().clear();
        pciPluginConfig.userIdInput().sendKeys(USER_ID);

        Assert.assertTrue(targetRadioButton.isSelected(), "Target environment radio button does not match expected value, should be \"" + targetRadioButton.getAttribute("id") + "\"");
        Assert.assertEquals(pciPluginConfig.orgAliasInput().getAttribute("value"), orgAlias, "orgAlias input element does not match expected value.");
        Assert.assertEquals(pciPluginConfig.userIdInput().getAttribute("value"), USER_ID, "userId input element does not match expected value.");
    }

    private void submitPaymentThroughIframe(PciPluginConfig.Iframe iframe, boolean submitPayment) throws InterruptedException {
        if (isTokenizerIframePresent) {
            Thread.sleep(2000); // let the tokenizer frame finish processing
        }
        Assert.assertTrue(iframe.submitToConfirmButton().isEnabled(), "Main payment button is not enabled after entering payment information");
        logger.info("Click the main 'Pay' button");
        Thread.sleep(10000);
        iframe.submitToConfirmButton().click();

        logger.info("Wait until the secondary submit button is shown");
        wait.until(ExpectedConditions.visibilityOf(iframe.submitButton()));
        wait.until(ExpectedConditions.elementToBeClickable(iframe.submitButton()));
        wait.until(ExpectedConditions.elementToBeClickable(iframe.modalCancelButton()));
        Assert.assertTrue(iframe.submitButton().isDisplayed(), "Secondary payment button is not displayed.");
        if (submitPayment) {
            // Note: Selenium is having issues clicking the buttons inside the modal, use tabbing as a workaround
            logger.info("Submitting payment to the PCI-WS through the embedded iframe.");
            new Actions(driver).moveToElement(driver.findElement(By.id("confirmPaymentInfoModal"))).click()
                    .sendKeys(Keys.TAB, Keys.TAB, Keys.TAB, Keys.ENTER).build().perform();
        } else {
            logger.info("Closing current submit modal");
            new Actions(driver).moveToElement(driver.findElement(By.id("confirmPaymentInfoModal"))).click()
                    .sendKeys(Keys.TAB, Keys.TAB, Keys.ENTER).build().perform();
        }
        wait.until(ExpectedConditions.invisibilityOf(iframe.modalCancelButton()));
    }

    /**
     * Simple method to check whether or not the tokenizer frame is present
     *
     * @return
     */
    private boolean isTokenizerFramePresent() {
        boolean isTokenizerPresent = false;
        try {
            WebElement tokenizerFrame = pciPluginConfig.getIframe().getTokenizerIframeElement();
            isTokenizerPresent = tokenizerFrame.getAttribute("src") != null;
        } catch (NoSuchElementException e) {
            // Tokenizer iframe is not present
        }
        return isTokenizerPresent;
    }

    private void createAndSwitchToIframe() throws InterruptedException {
        fieldConfigurationMap = getStringFieldConfigurationEnumMap();

        logger.info("Create the payment form");
        pciPluginConfig.createFormButton().click();

        // wait several seconds
        Thread.sleep(2000);

        // switch to the iframe
        switchToNewIframe();

        // Check to see if the tokenizer iframe is present
        isTokenizerIframePresent = isTokenizerFramePresent();

        logger.info("Verify that the main submit button is visible");
        Assert.assertTrue(pciPluginConfig.getIframe().submitToConfirmButton().isDisplayed());

        // validate that the configured values are displayed in the plugin as they should be
        verifyIframeFieldsMatchConfiguredValues(pciPluginConfig.getIframe());
    }

    private Map<String, PciPluginConfig.FieldConfigurationEnum> getStringFieldConfigurationEnumMap() throws InterruptedException {
        // Store the configuration info about the configured fields
        Map<String, PciPluginConfig.FieldConfigurationEnum> fieldConfigMap = new HashMap<>();

        logger.info("Retrieving and storing current field configuration settings.");
        for (WebElement element : pciPluginConfig.getAllFieldConfigElements()) {
            fieldConfigMap.put(element.getAttribute("id"), PciPluginConfig.FieldConfigurationEnum.parse(element.getAttribute("value")));
        }

        return fieldConfigMap;
    }

    private void switchToNewIframe() throws InterruptedException {
        switchToFrame(IframeEnum.DEFAULT);
        // Check for any initialization errors
        if (pciPluginConfig.errorTextTextbox().isDisplayed()) {
            logger.error("An error was thrown when the payment frame was initialized, error=\""
                    + pciPluginConfig.errorTextTextbox().getAttribute("value") + "\"");
            Assert.fail();
        }

        logger.info("Scroll to the pci iframe element");
        scrollToElement(pciPluginConfig.paymentIframeElement());

        wait.until(ExpectedConditions.elementToBeClickable(pciPluginConfig.paymentIframeElement()));

        switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);

        logger.info("Switched to the pci iframe, waiting until the main submit button is clickable");
        logger.info("PCI-WS version: " + pciPluginConfig.getIframe().getPciWsVersion());

        wait.until(ExpectedConditions.visibilityOf(pciPluginConfig.getIframe().submitToConfirmButton()));
        scrollToElement(pciPluginConfig.getIframe().submitToConfirmButton());
    }

    private void verifyIframeFieldsMatchConfiguredValues(PciPluginConfig.Iframe iframe) throws InterruptedException {
        boolean configurationMatch = validateIframeFieldConfiguration(iframe) && validateAdditionalConfigurations();

        // Assert that all fields on the iframe match their expected values
        Assert.assertTrue(configurationMatch, "The configured values do not match the configuration of the payment iframe.");
    }

    private boolean validateIframeFieldConfiguration(PciPluginConfig.Iframe iframe) throws InterruptedException {
        boolean configurationMatch = true;

        for (String id : fieldConfigurationMap.keySet()) {
            boolean switchedToAchRadioBtn = false;
            boolean skipCheck = false;
            boolean skipStatusCheck = false;

            logger.info("Verifying status of the iframe " + id + " input, expected to be " + fieldConfigurationMap.get(id));
            // Switch to the ACH payment radio button to check the visibility of the "email" field. If no ACH radio button, skip this field.
            if ("emailField".equalsIgnoreCase(id)) {
                if (iframe.getAchRadioBtn().isDisplayed()) {
                    logger.info("Switching to the ACH radio  button to check on input field " + id);
                    switchedToAchRadioBtn = true;
                    switchToAchPaymentRadioBtn(iframe);
                } else {
                    logger.info("ACH radio button is not displayed, skipping validation for field " + id);
                    continue;
                }
            }

            // Skip the check for the CVV field in the iframe if CC payments are disabled
            if ("securityCodeField".equalsIgnoreCase(id) && !iframe.getCreditCardRadioBtn().isDisplayed()) {
                logger.info("Credit Card radio button is not displayed, skipping validation for field " + id);
                continue;
            }

            WebElement iframeInputField = null;

            try {
                // Get the element from the iframe based on the configuration field in the test harness
                if (isTokenizerIframePresent && "securityCodeField".equalsIgnoreCase(id)) {
                    IframeEnum previousFrame = currentFrame;
                    logger.info("Tokenizer iframe is present, checking for CVV field inside the tokenizer frame.");
                    // Switch to the frame
                    switchToFrame(IframeEnum.TOKENIZER_IFRAME);
                    // skip the required flag check, if present in the iframe then it is required
                    iframeInputField = getIframeElementByConfigurationField(iframe, id);
                    skipStatusCheck = true;
                    switchToFrame(previousFrame);
                } else {
                    iframeInputField = getIframeElementByConfigurationField(iframe, id);
                }
            } catch (NoSuchElementException e) {
                if (fieldConfigurationMap.get(id) != HIDDEN) {
                    configurationMatch = false;
                    logger.error(id + " field does not exist in the iframe.");
                }
                skipCheck = true;
            }

            if (skipStatusCheck) {
                logger.info("skipping required/optional check for tokenizer frame, elementId=" + id);
            } else if (iframeInputField != null) {
                // Get the two spans using Xpath that hold the indicators for "Required" and "Recommended" fields
                WebElement fieldRequiredSpan = iframe.getRequiredSpanByFieldId(iframeInputField.getAttribute("id"));
                WebElement fieldRecommendedSpan = iframe.getRecommendedSpanByFieldId(iframeInputField.getAttribute("id"));

                // For each of the configured fields, ensure that the field visibility matches expected
                switch (fieldConfigurationMap.get(id)) {
                    case HIDDEN: // field is not shown
                        configurationMatch = validateHiddenField(configurationMatch, id, iframeInputField);
                        break;
                    case OPTIONAL: // field is shown with no flags
                        configurationMatch = validateOptionalField(configurationMatch, id, iframeInputField, fieldRequiredSpan, fieldRecommendedSpan);
                        break;
                    case REQUIRED: // field is shown with red asterisk, unless all displayed fields are required, then no asterisks
                        configurationMatch = validateRequiredField(configurationMatch, id, iframeInputField, fieldRequiredSpan, fieldRecommendedSpan);
                        break;
                    case RECOMMENDED: // field is shown with blue flag
                        configurationMatch = validateRecommendedField(configurationMatch, id, iframeInputField, fieldRequiredSpan, fieldRecommendedSpan);
                        break;
                }
            } else if (!skipCheck) {
                configurationMatch = false;
                logger.error("Unable to find element for id, id=" + id);
            }

            if (switchedToAchRadioBtn && iframe.getCreditCardRadioBtn().isDisplayed()) {
                switchToCreditCardPaymentRadioBtn(iframe);
            }
        }
        return configurationMatch;
    }

    private boolean validateHiddenField(boolean configurationMatch, String id, WebElement iframeInputField) {
        if (iframeInputField.isDisplayed()) {
            logger.error(id + " field is displayed in iframe when it should be hidden.");
            configurationMatch = false;
        }
        return configurationMatch;
    }

    private boolean validateRecommendedField(boolean configurationMatch, String id, WebElement iframeInputField, WebElement fieldRequiredSpan, WebElement fieldRecommendedSpan) {
        if (!iframeInputField.isDisplayed()) {
            logger.error(id + " field is hidden in iframe when it should be 'Recommended'.");
            configurationMatch = false;
        } else if (fieldRequiredSpan.isDisplayed()) {
            logger.error(id + " field has a 'Required' indicator when should be 'Recommended'.");
            configurationMatch = false;
        } else if (!fieldRecommendedSpan.isDisplayed()) {
            configurationMatch = false;
            logger.error(id + " field is displayed in iframe without a 'Recommended' indicator.");
        }
        return configurationMatch;
    }

    private boolean validateRequiredField(boolean configurationMatch, String id, WebElement iframeInputField, WebElement fieldRequiredSpan, WebElement fieldRecommendedSpan) {
        if (!iframeInputField.isDisplayed()) {
            logger.error(id + " field is hidden in iframe when it should be 'Required'.");
            configurationMatch = false;
        } else if (fieldRecommendedSpan.isDisplayed()) {
            logger.error(id + " field has a 'Recommended' indicator when should be 'Required'.");
            configurationMatch = false;
        } else if (!fieldRequiredSpan.isDisplayed()) {
            logger.error(id + " field is displayed in iframe without a 'Required' indicator.");
        }
        return configurationMatch;
    }

    private boolean validateOptionalField(boolean configurationMatch, String id, WebElement iframeInputField, WebElement fieldRequiredSpan, WebElement fieldRecommendedSpan) {
        if (!iframeInputField.isDisplayed()) {
            logger.error(id + " field is hidden in iframe when it should be 'Optional'.");
            configurationMatch = false;
        } else if (fieldRequiredSpan.isDisplayed() || fieldRecommendedSpan.isDisplayed()) {
            logger.error(id + " field has a 'Recommended' or 'Required' indicator when should be optional.");
            configurationMatch = false;
        }
        return configurationMatch;
    }


    private boolean validateAdditionalConfigurations() throws InterruptedException {
        switchToFrame(IframeEnum.DEFAULT);

        boolean configurationMatch = validateCreditCardRadioBtn()
                && validateEcheckRadioBtn()
                && validatePrintOnStmt()
              && validateLegend();

        switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);

        return configurationMatch;
    }

    private boolean validateCreditCardRadioBtn() throws InterruptedException {
        return validatePaymentBtn(pciPluginConfig.ccEnabledInput(), "ccEnabled");
    }

    private boolean validateEcheckRadioBtn() throws InterruptedException {
        return validatePaymentBtn(pciPluginConfig.echeckEnabledInput(), "echeckEnabled");
    }

    private boolean validatePrintOnStmt() throws InterruptedException {
        String value = pciPluginConfig.displayPrintOnStmtInput().getAttribute("value");

        // Switch back to the main document
        switchToFrame(IframeEnum.DEFAULT);

        // element is hidden by default, and only shown if comment field is also shown
        if (fieldConfigurationMap.get("comment") != HIDDEN && "true".equalsIgnoreCase(value)) {
            logger.info("Verifying status of the iframe \"Print on Statement\" checkbox, expected to be visible.");
            return validateElementExistsFromConfigValue(pciPluginConfig.getIframe(),
                    pciPluginConfig.displayPrintOnStmtInput().getAttribute("id"));
        } else {
            logger.info("Verifying status of the iframe \"Print on Statement\" checkbox, expected to be hidden.");
            return validateElementDoesNotExistFromConfigValue(pciPluginConfig.getIframe(),
                    pciPluginConfig.displayPrintOnStmtInput().getAttribute("id"));
        }
    }

    private boolean validateLegend() throws InterruptedException {
        String value = pciPluginConfig.displayLegendInput().getAttribute("value");

        // element is hidden by default
        if ("true".equalsIgnoreCase(value)) {
            logger.info("Verifying status of the iframe Legend element, expected to be visible.");
            return validateElementExistsFromConfigValue(pciPluginConfig.getIframe(),
                    pciPluginConfig.displayLegendInput().getAttribute("id"));
        } else {
            logger.info("Verifying status of the iframe Legend element, expected to be hidden.");
            return validateElementDoesNotExistFromConfigValue(pciPluginConfig.getIframe(),
                    pciPluginConfig.displayLegendInput().getAttribute("id"));
        }
    }

    /**
     * Validate that when a payment button config is set to be false (echeckEnabled, ccEnabled), then that
     * button is hidden on the payment frame. Any other setting, the button should be displayed.
     *
     * @param btn
     * @param configFieldId
     * @return
     */
    private boolean validatePaymentBtn(WebElement btn, String configFieldId) throws InterruptedException {
        String value = btn.getAttribute("value");
        if ("false".equalsIgnoreCase(value)) {
            logger.info("Verifying status of the iframe \"" + configFieldId + "\" button, expected to be hidden.");
            return validateElementDoesNotExistFromConfigValue(pciPluginConfig.getIframe(), configFieldId);
        } else {
            logger.info("Verifying status of the iframe \"" + configFieldId + "\" button, expected to be visible.");
            return validateElementExistsFromConfigValue(pciPluginConfig.getIframe(), configFieldId);
        }
    }

    /**
     * Validate that the iframe element associated with the current configuration element is displayed
     * correctly inside the iframe
     *
     * @param iframe
     * @param configId
     * @return
     */
    private boolean validateElementExistsFromConfigValue(PciPluginConfig.Iframe iframe, String configId) throws InterruptedException {
        switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);
        boolean elementExists = false;
        try {
            if (getIframeElementByConfigurationField(iframe, configId).isDisplayed()) {
                elementExists = true;
            } else {
                logger.error("Element should be displayed in iframe: " + configId);
            }
        } catch (NoSuchElementException e) {
            logger.error("NoSuchElementException. Element should be displayed in iframe: " + configId);
        } finally {
            switchToFrame(IframeEnum.DEFAULT);
        }
        return elementExists;
    }

    private boolean validateElementDoesNotExistFromConfigValue(PciPluginConfig.Iframe iframe, String configId) throws InterruptedException {
        switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);
        boolean elementDoesNotExist = false;
        try {
            if (getIframeElementByConfigurationField(iframe, configId).isDisplayed()) {
                logger.error("Element should not be displayed in iframe: " + configId);
            } else {
                elementDoesNotExist = true;
            }

        } catch (NoSuchElementException e) {
            // This is expected if the element does not exist in the DOM (ng-if, etc.)
            elementDoesNotExist = true;
        } finally {
            switchToFrame(IframeEnum.DEFAULT);
        }
        return elementDoesNotExist;
    }

    private void switchToCreditCardPaymentRadioBtn(PciPluginConfig.Iframe iframe) throws InterruptedException {
        switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);
        Assert.assertTrue(iframe.getCreditCardRadioBtn().isDisplayed(), "Credit Card radio button is not displayed in the current payment iframe.");
        logger.info("Switching back to the CC radio button.");
        iframe.getCreditCardRadioBtn().click();
        if (isTokenizerIframePresent) {
            wait.until(ExpectedConditions.visibilityOf(iframe.getTokenizerIframeElement()));
        } else {
            wait.until(ExpectedConditions.visibilityOf(iframe.cardNumberInput()));
        }
    }

    private void switchToAchPaymentRadioBtn(PciPluginConfig.Iframe iframe) throws InterruptedException {
        switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);
        Assert.assertTrue(iframe.getAchRadioBtn().isDisplayed(), "ACH radio button is not displayed in the current payment iframe.");
        iframe.getAchRadioBtn().click();
        wait.until(ExpectedConditions.visibilityOf(iframe.accountNumberInput()));
    }

    private void switchToPayPalPaymentBtn(PciPluginConfig.Iframe iframe) throws InterruptedException {
        switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);
        Assert.assertTrue(iframe.getPayPalRadioBtn().isDisplayed(), "PayPal radio button is not displayed in the current payment iframe.");

        iframe.getPayPalRadioBtn().click();
        wait.until(ExpectedConditions.visibilityOf(iframe.payPalButton()));
        logger.info(" waited for blue PayPal button to show up");

        iframe.payPalButton().click();
        logger.info("clicked on Blue PayPal button.");
        driver.switchTo().window(switchToPopupWin()).getTitle();

    }

    public String switchToPaypalWin() throws InterruptedException {
        String parentWindow = driver.getWindowHandle();
        logger.info("parent window=" + driver.getTitle());
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> it = handles.iterator();
        String switchWin = null;

        while (it.hasNext()) {
            switchWin = it.next();
            logger.info("        Switching to Pop Up Window: " + driver.switchTo().window(switchWin).getTitle());
            driver.switchTo().window(switchWin);
            try {
                driver.manage().window().maximize();
                loginToPayPal();
                logger.info("Log in to PayPal account");

                switchToPopupWin();
                logger.info("switched to Pop Up window=" + driver.getTitle());

                waitUntilElementPresent(pciPluginConfig.payPalBalanceBtn(), 60);
                pciPluginConfig.payPalBalanceBtn().click();
                logger.info("clicked on PayPal balance button");

                pciPluginConfig.payPalSubmitBtn().click();
                logger.info("clicked on complete purchase btn");

            } catch (WebDriverException e) {
                // Not all popups can be maximized
                logger.warn("Unable to maximize window, window=" + driver.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        driver.switchTo().window(parentWindow);
        return parentWindow;


    }

    private void GetPayPal() throws InterruptedException {
        switchToPayPalPaymentBtn(pciPluginConfig.getIframe());

    }

    private void GetPayPalPayLater() throws InterruptedException {
        switchToPayLaterPaymentBtn(pciPluginConfig.getIframe());
    }


    private void switchToPayLaterPaymentBtn(PciPluginConfig.Iframe iframe) throws InterruptedException {
        switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);
        Assert.assertTrue(iframe.getPayPalRadioBtn().isDisplayed(), "PayPal radio button is not displayed in the current payment iframe.");

        iframe.getPayPalRadioBtn().click();
        logger.info("clicked on PayPal/PayLater radio button");

        wait.until(ExpectedConditions.visibilityOf(iframe.getPayLaterBtn()));
        logger.info(" waited for blue PayLater button to show up");

        iframe.getPayLaterBtn().click();
        logger.info("clicked on Blue PayLater button.");
        driver.switchTo().window(switchToPopupWin()).getTitle();

    }

    public String switchToPayLaterWin() throws InterruptedException {
        String parentWindow = driver.getWindowHandle();
        logger.info("parent window=" + driver.getTitle());
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> it = handles.iterator();
        String switchWin = null;

        while (it.hasNext()) {
            switchWin = it.next();
            logger.info("        Switching to Pop Up Window: " + driver.switchTo().window(switchWin).getTitle());
            driver.switchTo().window(switchWin);
            try {
                driver.manage().window().maximize();
                loginToPayPal();
                logger.info("Log in to PayPal account");

                switchToPopupWin();
                logger.info("switched to pop window=" + driver.getTitle());

                //starts Pay in4 methods
                waitUntilElementPresent(pciPluginConfig.payIn4Btn(), 60);
                pciPluginConfig.payIn4Btn().click();
                logger.info("clicked pay later btn");
                pciPluginConfig.payPalSubmitBtn().click();
                logger.info("clicked on complete purchase btn");

                switchToPayPalFrame(pciPluginConfig.payPalCheckoutFrame());
                logger.info("switched to frame = " + driver.getTitle());
                waitUntilElementPresent(pciPluginConfig.payIn4ContinueBtn(), 60);
                logger.info("element is present");
                pciPluginConfig.payIn4ContinueBtn().click();
                logger.info("clicked on continue button");

                Thread.sleep(10000);
                switchToDefaultWinFromFrame();
                switchToPayPalFrame(pciPluginConfig.payPalCheckoutFrame());
                logger.info("switched to frame = " + driver.getTitle());
                waitUntilElementPresent(pciPluginConfig.payIn4AgreeAndApplyBtn(), 60);
                pciPluginConfig.payIn4AgreeAndApplyBtn().click();
                logger.info("clicked on Agree and Apply button");

                pciPluginConfig.payIn4CreditUnionIcon().click();
                logger.info("clicked on credit union icon");

                waitUntilElementPresent(pciPluginConfig.payIn4AgreementCheckbox(), 60);
                selectCheckBox(pciPluginConfig.payIn4AgreementCheckbox());
                logger.info("clicked on checkbox");

                pciPluginConfig.payIn4AgreeAndContinueBtn().click();
                logger.info("clicked on agree and continue  button");
                pciPluginConfig.payIn4PayBtn().click();
                logger.info("clicked on Pay  button");
                Thread.sleep(2000);

            } catch (WebDriverException e) {
                // Not all popups can be maximized
                logger.warn("Unable to maximize window, window=" + driver.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        driver.switchTo().window(parentWindow);
        return parentWindow;


    }

    private void loginToPayPal() {
        logger.info("Log in to PayPal account");
        pciPluginConfig.payPalLoginEmail().sendKeys("sb-dru7x21624644@personal.example.com");
        pciPluginConfig.payPalNextBtn().click();
        logger.info("clicked on next btn");
        pciPluginConfig.payPalLoginPassword().sendKeys("Ke8-M=Vu");
        logger.info("entered password");
        pciPluginConfig.payPalLogin().click();
        logger.info("clicked on login btn");
    }

    private WebElement getIframeElementByConfigurationField(PciPluginConfig.Iframe iframe, String configFieldName) {
        try {
            driver.manage().timeouts().implicitlyWait(OVERRIDE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            switch (configFieldName) {
                case "firstNameField":
                    return iframe.firstNameFieldInput();
                case "lastNameField":
                    return iframe.lastNameFieldInput();
                case "securityCodeField":
                    return isTokenizerIframePresent ? iframe.getTokenizerIframe().cvvFieldInput() : iframe.cvvFieldInput();
                case "addressField":
                    return iframe.addressFieldInput();
                case "postalCodeField":
                    return iframe.postalCodeFieldInput();
                case "emailField":
                    return iframe.emailFieldInput();
                case "commentField":
                    return iframe.commentFieldInput();
                case "ccEnabled":
                    return iframe.getCreditCardRadioBtn();
                case "echeckEnabled":
                    return iframe.getAchRadioBtn();
                case "allowPayPalFromEmbeddingSite":
                    return iframe.getPayPalRadioBtn();
                case "displayPrintOnStmt":
                    return iframe.getPrintOnStatementCheckbox();
                case "displayLegend":
                    return iframe.getLegendDiv();
                default:
                    throw new NoSuchElementException("Incorrect config element name: " + configFieldName);
            }
        } catch (NoSuchElementException e) {
            logger.info("NoSuchElementException searching for the id associated with configFieldName=" + configFieldName);
            throw e;
        } finally {
            driver.manage().timeouts().implicitlyWait(defaultTimeoutSeconds, TimeUnit.SECONDS);
        }
    }

    private void enterGenericCreditCardPaymentInformationIntoIframe(PciPluginConfig.Iframe iframe) throws InterruptedException {
        logger.info("Enter generic Credit Card payment information");
        if (isTokenizerIframePresent) {
            // Switch to the internal iframe
            switchToFrame(IframeEnum.TOKENIZER_IFRAME);
            // Send generic VISA credit card number
            iframe.getTokenizerIframe().cardNumberInput().sendKeys("4111111111111111");
            // Press the down key a random(1-12, inclusive) number of times to select expiration month element
            iframe.getTokenizerIframe().expiryMonthInput().sendKeys(generateDownArrowSequence(1, 12, "Expiry Month"));
            // Press the down key a random(2-11, inclusive) number of times to select expiration year element (between next year and 10 years from now)
            iframe.getTokenizerIframe().expiryYearInput().sendKeys(generateDownArrowSequence(2, 11, "Expiry Year"));
            // Send '123' as the generic CVV
            iframe.getTokenizerIframe().cvvFieldInput().sendKeys("123", Keys.TAB);
            Thread.sleep(2000);
            // Switch back to the XIFIN iframe
            switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);
        } else {
            // Send generic VISA credit card number
            iframe.cardNumberInput().sendKeys("4111111111111111");
            // Press the down key a random(1-12, inclusive) number of times to select expiration month element
            iframe.expiryMonthInput().sendKeys(generateDownArrowSequence(1, 12, "Expiry Month"));
            // Press the down key a random(2-11, inclusive) number of times to select expiration year element (between next year and 10 years from now)
            iframe.expiryYearInput().sendKeys(generateDownArrowSequence(2, 11, "Expiry Year"));
            // Send '123' as the generic CVV
            iframe.cvvFieldInput().sendKeys("123");
        }
    }

    /**
     * Populate the minimum required fields to make a credit card payment for the current iframe
     *
     * @param iframe
     * @throws InterruptedException
     */

    private void populateOnlyRequiredFieldsCreditCard(PciPluginConfig.Iframe iframe) throws InterruptedException {
        switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);
        wait.until(ExpectedConditions.visibilityOf(iframe.cardNumberInput()));

        if (isTokenizerIframePresent) {
            // Switch to the internal iframe
            switchToFrame(IframeEnum.TOKENIZER_IFRAME);
            // Send generic VISA credit card number
            iframe.getTokenizerIframe().cardNumberInput().sendKeys("4111111111111111");
            // Press the down key a random(1-12, inclusive) number of times to select expiration month element
            iframe.getTokenizerIframe().expiryMonthInput().sendKeys(generateDownArrowSequence(1, 12, "Expiry Month"));
            // Press the down key a random(2-11, inclusive) number of times to select expiration year element (between next year and 10 years from now)
            iframe.getTokenizerIframe().expiryYearInput().sendKeys(generateDownArrowSequence(2, 11, "Expiry Year"));
            Thread.sleep(2000);
            // Switch back to the XIFIN iframe
            switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);
        } else {
            // Send generic VISA credit card number, always required
            iframe.cardNumberInput().sendKeys("4111111111111111");
            // Press the down key a random(1-12, inclusive) number of times to select expiration month element
            iframe.expiryMonthInput().sendKeys(generateDownArrowSequence(1, 12, "Expiry Month"));
            // Press the down key a random(2-11, inclusive) number of times to select expiration year element (between next year and 10 years from now)
            iframe.expiryYearInput().sendKeys(generateDownArrowSequence(2, 11, "Expiry Year"));

        }

        populateRequiredFieldsForCurrentBtn(iframe);
    }

    /**
     * Generate a CharSequence of Keys.DOWN of random length to select an element from a dropdown list using the DOWN key
     *
     * @return
     */
    private CharSequence[] generateDownArrowSequence(int minPresses, int maxPresses, String elementLabel) {
        // Randomly generate the number of times to press the down arrow
        int numDownChars = (int) (Math.random() * maxPresses + minPresses);

        List<Keys> keysToSend = new ArrayList<>();

        // Build up a List of this many elements
        for (int i = 0; i < numDownChars; i++) {
            keysToSend.add(Keys.DOWN);
        }

        logger.info("Randomly selecting nth value for \"" + elementLabel + "\": " + numDownChars);

        // Convert this list to a CharSequence that can be used using the selenium SendKeys method
        return keysToSend.toArray(new CharSequence[0]);
    }

    /**
     * Populate the minimum required fields to make an ACH payment for the current iframe
     *
     * @param iframe
     * @throws InterruptedException
     */
    private void populateOnlyRequiredFieldsAch(PciPluginConfig.Iframe iframe) throws InterruptedException {
        enterGenericEcheckPaymentInformationIntoIframe(iframe);
        populateRequiredFieldsForCurrentBtn(iframe);
    }

    /**
     * Populate any required non-payment fields in the iframe for this payment button with valid data
     *
     * @param iframe
     * @throws InterruptedException
     */
    private void populateRequiredFieldsForCurrentBtn(PciPluginConfig.Iframe iframe) throws InterruptedException {
        for (String configFieldName : fieldConfigurationMap.keySet()) {
            // Populate every required field in the credit card radio button with valid data
            try {
                // if this is the tokenizer frame, the CVV will be REQUIRED even when it is configured as OPTIONAL or RECOMMENDED
                if (isTokenizerIframePresent && "securityCodeField".equals(configFieldName)
                        && iframe.getTokenizerIframeElement().isDisplayed()
                        && fieldConfigurationMap.get(configFieldName) != HIDDEN) {
                    // Switch to the internal tokenizer iframe
                    switchToFrame(IframeEnum.TOKENIZER_IFRAME);
                    populateElementWithDummyData(getIframeElementByConfigurationField(iframe, configFieldName));
                }
                // We already validated that this field exists
                else if (fieldConfigurationMap.get(configFieldName) == REQUIRED) {

                    populateElementWithDummyData(getIframeElementByConfigurationField(iframe, configFieldName));

                }
            } catch (NoSuchElementException e) {
                // Ignore values not located on the current payment button
                logger.info("Ignoring required field controlled by \"" + configFieldName + "\", is not visible in current payment button.");
            } finally {
                // Switch back to the normal frame
                switchToFrame(IframeEnum.XIFIN_PCI_IFRAME);
            }
        }
    }

    /**
     * Populate the provided iframe input element with dummy data. Must already be inside iframe.
     *
     * @param elem
     * @throws InterruptedException
     */
    private void populateElementWithDummyData(WebElement elem) throws InterruptedException {
        String keysToSend = null;

        switch (elem.getAttribute("id")) {
            case "cvv":
            case "cccvvfield":
                keysToSend = "123";
                break;
            case "billingFirstName":
                // 50% chance to enter a middle name as well
                elem.clear();
                keysToSend = "FIRST" + RandomStringUtils.randomAlphabetic(5).toUpperCase()
                        + ((new Random()).nextInt(2) == 1 ? " MID"
                        + RandomStringUtils.randomAlphabetic(4).toUpperCase() : "");
                break;
            case "billingLastName":
                elem.clear();
                keysToSend = "LAST" + RandomStringUtils.randomAlphabetic(5).toUpperCase();
                break;
            case "billingAddressLine":
                elem.clear();
                keysToSend = RandomStringUtils.randomNumeric(4) + " " + RandomStringUtils.randomAlphabetic(7).toUpperCase() + " RD";
                break;
            case "billingPostalCode":
                elem.clear();
                keysToSend = "12345";
                break;
            case "billingEmail":
                elem.clear();
                keysToSend = RandomStringUtils.randomAlphabetic(8).toLowerCase() + "@xifinqa.com";
                break;
        }

        if (keysToSend != null) {
            scrollToElement(elem);
            logger.info("Populating the required \"" + elem.getAttribute("id")
                    + "\" field with \"" + keysToSend + "\"");
            elem.sendKeys(keysToSend);
        }
    }

    /**
     * Switch to the ACH payment button and enter generic valid ACH payment information
     *
     * @param iframe
     * @throws InterruptedException
     */
    private void enterGenericEcheckPaymentInformationIntoIframe(PciPluginConfig.Iframe iframe) throws InterruptedException {
        // Switch to the ACH radio button
        switchToAchPaymentRadioBtn(iframe);

        logger.info("Enter generic ACH payment information");
        // Send generic 8-digit account number
        String accountNumber = RandomStringUtils.randomNumeric(8);
        iframe.accountNumberInput().sendKeys(accountNumber);
        iframe.confirmAccountNumberInput().sendKeys(accountNumber);
        // Send generic routing number
        iframe.routingNumberInput().sendKeys("021000021");
    }

    /**
     * Increment the resultNumber of the current screen. Only required when sending actual PCI transactions.
     */
    private void incrementResultNumber() {
        // increment resultNumber
        logger.info("Incrementing resultNumber to " + (resultNumber + 1));
        resultNumber++;
    }

    public void switchToPayPalFrame(WebElement element)
    {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        driver.switchTo().frame(element);
    }

    private void switchToDefaultGateway(String gatewayName) throws InterruptedException, XifinDataAccessException {

        boolean isFirstData = "FirstData".equals(gatewayName);
        boolean isCardConnect = "CardConnect".equals(gatewayName);
        boolean isPayPal = "PayPal".equals(gatewayName);
        boolean isAuthorizeNet = "Authorize.Net".equals(gatewayName);
        boolean isBraintree = "Braintree".equals(gatewayName);

        rpmDao.updateElcPmtGatewayDefaultByGatewayName(testDb, "FirstData", isFirstData);
        rpmDao.updateElcPmtGatewayDefaultByGatewayName(testDb, "CardConnect", isCardConnect);
        rpmDao.updateElcPmtGatewayDefaultByGatewayName(testDb, "PayPal", isPayPal);
        rpmDao.updateElcPmtGatewayDefaultByGatewayName(testDb, "Authorize.Net", isAuthorizeNet);
        rpmDao.updateElcPmtGatewayDefaultByGatewayName(testDb, "Braintree", isBraintree);
        logger.info("Updating default payment gateway, gateway= " + gatewayName);

    }


    public void switchToFrame(IframeEnum targetFrame) {
        if (currentFrame != targetFrame) {
            driver.switchTo().defaultContent();

            if (StringUtils.isNotBlank(targetFrame.getElementId())) {
                // for the tokenizer iframe, we first need to enter the XIFIN Iframe
                if (targetFrame == IframeEnum.TOKENIZER_IFRAME) {
                    driver.switchTo().frame(IframeEnum.XIFIN_PCI_IFRAME.getElementId());
                }
                logger.info("Switching to frame: " + targetFrame.name());
                driver.switchTo().frame(targetFrame.getElementId());
            } else {
                logger.info("Switching to default frame");
            }
            currentFrame = targetFrame;
        }


    }
}
