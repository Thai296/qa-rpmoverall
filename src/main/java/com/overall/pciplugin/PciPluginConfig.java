package com.overall.pciplugin;

import com.xifin.qa.config.Configuration;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.xifin.qa.config.PropertyMap.PCI_PLUGIN_TEST_HARNESS_URL;

public class PciPluginConfig {
    private final RemoteWebDriver driver;
    private Logger logger;
    private final Iframe iframe;
    private final Configuration config;
    private final String elementId;

    public PciPluginConfig(RemoteWebDriver driver, Configuration config, String elementId) {
        this.driver = driver;
        this.iframe = new Iframe();
        this.logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
        this.config = config;
        this.elementId = elementId;
    }

    public void navigateToConfluenceTestHarness() throws MalformedURLException {
        URL url = new URL(config.getProperty(PCI_PLUGIN_TEST_HARNESS_URL));
        navigateToUrl(url);
    }

    public List<WebElement> getAllFieldConfigElements() {
        List<WebElement> allFieldConfigs = new ArrayList<>();
        allFieldConfigs.add(firstNameFieldInput());
        allFieldConfigs.add(lastNameFieldInput());
        allFieldConfigs.add(securityCodeFieldInput());
        allFieldConfigs.add(addressFieldInput());
        allFieldConfigs.add(postalCodeFieldInput());
        allFieldConfigs.add(emailFieldInput());
        allFieldConfigs.add(commentFieldInput());

        return allFieldConfigs;
    }

    public enum FieldConfigurationEnum {
        HIDDEN("Hidden"),
        REQUIRED("Required"),
        RECOMMENDED("Recommended"),
        OPTIONAL("Optional");

        private final String value;

        FieldConfigurationEnum(String value) {
            this.value = value;
        }

        public static FieldConfigurationEnum parse(String value) {
            // Default to optional
            FieldConfigurationEnum configuredValue = OPTIONAL;
            for (FieldConfigurationEnum fieldConfigurationEnum : FieldConfigurationEnum.values()) {
                if (fieldConfigurationEnum.toString().equalsIgnoreCase(value)) {
                    configuredValue = fieldConfigurationEnum;
                    break;
                }
            }
            return configuredValue;
        }

        public String toString() {
            return value;
        }
    }

    public enum IframeEnum {
        XIFIN_PCI_IFRAME("xifinpciiframe"),
        TOKENIZER_IFRAME("tokenizerframe"),
        DEFAULT("");

        private final String elementId;

        IframeEnum(String elementId) {
            this.elementId = elementId;
        }

        public String getElementId() {
            return elementId;
        }
    }

    public WebElement errorTextTextbox() {
        return driver.findElement(By.id("errorText"));
    }

    public WebElement paymentIframeElement() {
        return driver.findElement(By.xpath("//*[@id='" + elementId + "']/iframe"));
    }

    public WebElement setXPAccnButton() {
        return driver.findElement(By.id("setXPAccnButton"));
    }

    public WebElement setXPClnButton() {
        return driver.findElement(By.id("setXPClnButton"));
    }

    public WebElement setCPButton() {
        return driver.findElement(By.id("setCPButton"));
    }

    public WebElement setPPButton() {
        return driver.findElement(By.id("setPPButton"));
    }

    public WebElement createFormButton() {
        return driver.findElement(By.id("createFormButton"));
    }

    public WebElement clearResultsButton() {
        return driver.findElement(By.id("clearResultsButton"));
    }

    public WebElement orgAliasInput() {
        return driver.findElement(By.id("orgalias"));
    }

    public WebElement localhostRadioButton() {
        return driver.findElement(By.id("localRadio"));
    }

    public WebElement qaRadioButton() {
        return driver.findElement(By.id("qaRadio"));
    }

    public WebElement uatRadioButton() {
        return driver.findElement(By.id("uatRadio"));
    }

    public WebElement prodRadioButton() {
        return driver.findElement(By.id("prodRadio"));
    }

    public WebElement prepayRadioButton() {
        return driver.findElement(By.id("prepayradio"));
    }

    public WebElement clnRadioButton() {
        return driver.findElement(By.id("clnradio"));
    }

    public WebElement accnRadioButton() {
        return driver.findElement(By.id("accnradio"));
    }

    public WebElement resultText() {
        return driver.findElement(By.id("resultText"));
    }

    public WebElement resultButton(int resultNumber) {
        return driver.findElement(By.id("resultButton" + resultNumber));
    }

    public WebElement userIdInput() {
        return driver.findElement(By.id("userid"));
    }

    public WebElement urlInput() {
        return driver.findElement(By.id("url"));
    }

    public WebElement amountInput() {
        return driver.findElement(By.id("amount"));
    }

    public WebElement sequenceNumberInput() {
        return driver.findElement(By.id("sequenceNumber"));
    }

    public WebElement orgNameInput() {
        return driver.findElement(By.id("orgName"));
    }

    public WebElement sourceInput() {
        return driver.findElement(By.id("source"));
    }

    public WebElement commentInput() {
        return driver.findElement(By.id("comment"));
    }

    public WebElement ccEnabledInput() {
        return driver.findElement(By.id("ccEnabled"));
    }

    public WebElement allowPayPalFromEmbeddingSite() {
        return driver.findElement(By.id("allowPayPalFromEmbeddingSite"));
    }

    public WebElement echeckEnabledInput() {
        return driver.findElement(By.id("echeckEnabled"));
    }

    public WebElement firstNameFieldInput() {
        return driver.findElement(By.id("firstNameField"));
    }

    public WebElement lastNameFieldInput() {
        return driver.findElement(By.id("lastNameField"));
    }

    public WebElement securityCodeFieldInput() {
        return driver.findElement(By.id("securityCodeField"));
    }

    public WebElement addressFieldInput() {
        return driver.findElement(By.id("addressField"));
    }

    public WebElement postalCodeFieldInput() {
        return driver.findElement(By.id("postalCodeField"));
    }

    public WebElement emailFieldInput() {
        return driver.findElement(By.id("emailField"));
    }

    public WebElement commentFieldInput() {
        return driver.findElement(By.id("commentField"));
    }

    public WebElement allowInternationalAddressesInput() {
        return driver.findElement(By.id("allowInternationalAddresses"));
    }

    public WebElement saveCardsEnabledInput() {
        return driver.findElement(By.id("saveCardsEnabled"));
    }

    public WebElement minimumPaymentAmountInput() {
        return driver.findElement(By.id("minimumPaymentAmount"));
    }

    public WebElement maximumPaymentAmountInput() {
        return driver.findElement(By.id("maximumPaymentAmount"));
    }

    public WebElement saveCommentOnOrderInput() {
        return driver.findElement(By.id("saveCommentOnOrder"));
    }

    public WebElement displayPrintOnStmtInput() {
        return driver.findElement(By.id("displayPrintOnStmt"));
    }

    public WebElement savedCardsInput() {
        return driver.findElement(By.id("savedCards"));
    }

    public WebElement supportedCardTypesInput() {
        return driver.findElement(By.id("supportedCardTypes"));
    }

    public WebElement firstNameInput() {
        return driver.findElement(By.id("firstName"));
    }

    public WebElement lastNameInput() {
        return driver.findElement(By.id("lastName"));
    }

    public WebElement addressInput() {
        return driver.findElement(By.id("address"));
    }

    public WebElement postalCodeInput() {
        return driver.findElement(By.id("postalCode"));
    }

    public WebElement countryCodeInput() {
        return driver.findElement(By.id("countryCode"));
    }

    public WebElement emailInput() {
        return driver.findElement(By.id("email"));
    }

    public WebElement verticalLayoutInput() {
        return driver.findElement(By.id("verticalLayout"));
    }

    public WebElement compactLayoutInput() {
        return driver.findElement(By.id("compactLayout"));
    }

    public WebElement fontFamilyInput() {
        return driver.findElement(By.id("fontFamily"));
    }

    public WebElement fontSizeInput() {
        return driver.findElement(By.id("fontSize"));
    }

    public WebElement displayLegendInput() {
        return driver.findElement(By.id("displayLegend"));
    }

    public WebElement recurringEndDateInput() {
        return driver.findElement(By.id("recurringEndDate"));
    }

    public WebElement buttonPrimaryColorInput() {
        return driver.findElement(By.id("buttonPrimaryColor"));
    }

    public WebElement buttonSecondaryColorInput() {
        return driver.findElement(By.id("buttonSecondaryColor"));
    }

    public WebElement cancelButtonPrimaryColorInput() {
        return driver.findElement(By.id("cancelButtonPrimaryColor"));
    }

    public WebElement cancelButtonSecondaryColorInput() {
        return driver.findElement(By.id("cancelButtonSecondaryColor"));
    }

    public WebElement useDefaultCancelButtonsInput() {
        return driver.findElement(By.id("useDefaultCancelButtons"));
    }

    public WebElement useButtonGradientsInput() {
        return driver.findElement(By.id("useButtonGradients"));
    }

    public WebElement overrideStringsInput() {
        return driver.findElement(By.id("overrideStrings"));
    }

    public WebElement payPalLoginEmail() {
        return driver.findElement(By.xpath("//input[@id='email']"));
    }

    public WebElement payPalLoginPassword() {
        return driver.findElement(By.id("password"));
    }

    public WebElement payPalLogin() {
        return driver.findElement(By.id("btnLogin"));
    }

    public WebElement payPalNextBtn() {
        return driver.findElement(By.id("btnNext"));
    }

    public WebElement payPalBalanceBtn() {
        return driver.findElement(By.xpath("//label[@for='BALANCEUSD-funding-option']//span[@class='ppvx_radio__check-icon___2-9-22']"));
    }

    public WebElement payIn4Btn() {
        return driver.findElement(By.xpath("//img[@alt='Pay in 4']"));

    }


    public WebElement payPalSubmitBtn() {
        return driver.findElement(By.id("payment-submit-btn"));
    }

    public WebElement payIn4CreditUnionIcon() {
        return driver.findElement(By.xpath("//span[@data-testid='bankIcon']"));
    }

    public WebElement payIn4AgreementCheckbox() {
        return driver.findElement((By.xpath("//label[@class='ppvx_checkbox__label']")));
    }
    // payin4 continue button.
    public WebElement payIn4ContinueBtn() {
        return driver.findElement(By.xpath("//button[text()='Continue']"));
    }

    public WebElement payIn4AgreeAndApplyBtn() {
        return driver.findElement(By.xpath("//button[text()='Agree and Apply']"));
    }
    // payin4 Agree and continue button button.
    public WebElement payIn4AgreeAndContinueBtn() {
        return driver.findElement(By.xpath("//button[text()='Agree and Continue']"));
    }

    public WebElement payIn4PayBtn() {
        return driver.findElement(By.id("payment-submit-btn"));
    }// payin4 Pay button.

    public WebElement payPalCheckoutFrame () { return driver.findElement(By.xpath("//*[@id=\"hermione-container\"]/div[2]/iframe"));}


    public WebElement payPalCheckoutWindowID() {
        return driver.findElement(By.id("main"));
    }


    private void navigateToUrl(URL url) {
        logger.info("Navigating to URL, url=" + url);
        driver.get(url.toString());
    }

    public Iframe getIframe() {
        return iframe;
    }

    public class Iframe {
        private TokenizerIframe tokenizerIframe;

        Iframe() {
            logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
            tokenizerIframe = new TokenizerIframe();
        }

        /**
         * Find the version of the PCI-WS by parsing the HTML inside the iframe,
         *
         * @return
         */
        public String getPciWsVersion() {
            String version = "UNKNOWN";
            try {
                version = driver.getPageSource().replaceAll("\n", "").replaceAll("^.*<!-- Version (.*?) -->.*$", "v$1");
            } catch (Exception e) {
                logger.warn("Unable to determine PCI-WS version from iframe script imports.", e);
            }
            return version;
        }

        public WebElement cardNumberInput() {
            return driver.findElement(By.id("cardNumber"));
        }

        public WebElement expiryMonthInput() {
            return driver.findElement(By.id("expiryMonth"));
        }

        public WebElement expiryYearInput() {
            return driver.findElement(By.id("expiryYear"));
        }

        public WebElement cvvFieldInput() {
            return driver.findElement(By.id("cvv"));
        }

        public WebElement accountNumberInput() {
            return driver.findElement(By.id("accountNumber"));
        }

        public WebElement payPalButton() {
            return driver.findElement(By.id("payPalButton"));
        }


        public WebElement confirmAccountNumberInput() {
            return driver.findElement(By.id("confirmAccountNumber"));
        }

        public WebElement routingNumberInput() {
            return driver.findElement(By.id("routingNumber"));
        }

        public WebElement submitToConfirmButton() {
            return driver.findElement(By.id("submitToConfirm"));
        }

        // inside the pop-up modal inside the iframe - secondary confirmation
        public WebElement submitButton() {
            return driver.findElement(By.id("submit"));
        }

        // inside the pop-up modal inside the iframe - secondary cancel
        public WebElement modalCancelButton() {
            return driver.findElement(By.id("modalCancel"));
        }

        public WebElement firstNameFieldInput() {
            return driver.findElement(By.id("billingFirstName"));
        }

        public WebElement lastNameFieldInput() {
            return driver.findElement(By.id("billingLastName"));
        }

        public WebElement addressFieldInput() {
            return driver.findElement(By.id("billingAddressLine"));
        }

        public WebElement postalCodeFieldInput() {
            return driver.findElement(By.id("billingPostalCode"));
        }

        public WebElement emailFieldInput() {
            return driver.findElement(By.id("billingEmail"));
        }

        public WebElement commentFieldInput() {
            return driver.findElement(By.id("comment"));
        }

        public WebElement cardNumberLabel() {
            return driver.findElement(By.id("cardNumberLabel"));
        }

        public WebElement getRecommendedSpanByFieldId(String fieldId) {
            return "comment".equalsIgnoreCase(fieldId) ?
                    driver.findElement(By.xpath("//*[@id='" + fieldId + "']/../../div[2]/label/*[contains(@class,'blueFlagIcon')]"))
                    : driver.findElement(By.xpath("//*[@id='" + fieldId + "']/../../label/*[contains(@class,'blueFlagIcon')]"));
        }

        public WebElement getRequiredSpanByFieldId(String fieldId) {
            return "comment".equalsIgnoreCase(fieldId) ?
                    driver.findElement(By.xpath("//*[@id='" + fieldId + "']/../../div[2]/label/*[contains(@class,'required-asterisk')]"))
                    : driver.findElement(By.xpath("//*[@id='" + fieldId + "']/../../label/*[contains(@class,'required-asterisk')]"));
        }

        public WebElement getAchRadioBtn() {
            return driver.findElement(By.id("achRadio"));
        }

        public WebElement getPayPalRadioBtn() {
            return driver.findElement(By.id("payPalRadio"));
        }

        public WebElement getPayLaterBtn() {
            return driver.findElement(By.id("payLaterButton"));
        }

        public WebElement getCreditCardRadioBtn() {
            return driver.findElement(By.id("creditCardRadio"));
        }

        public WebElement getPrintOnStatementCheckbox() {
            return driver.findElement(By.id("printOnStatement"));
        }

        public WebElement getLegendDiv() {
            return driver.findElement(By.xpath("//*[@id='panelBody']/form/fieldset/div[@ng-if='configs.displayLegend && showDisplayLegend']"));
        }

        public WebElement getTokenizerIframeElement() {
            return driver.findElement(By.id("tokenizerframe"));
        }

        public TokenizerIframe getTokenizerIframe() {
            return tokenizerIframe;
        }

        public class TokenizerIframe {
            public WebElement cardNumberInput() {
                return driver.findElement(By.id("ccnumfield"));
            }

            public WebElement expiryMonthInput() {
                return driver.findElement(By.id("ccexpirymonth"));
            }

            public WebElement expiryYearInput() {
                return driver.findElement(By.id("ccexpiryyear"));
            }

            public WebElement cvvFieldInput() {
                return driver.findElement(By.id("cccvvfield"));
            }
        }
    }
}
