package com.overall.mar.menu;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.overall.lockInformation.LockInformation;

public class NavigationMar {
	private SeleniumBaseTest b;
	private RemoteWebDriver driver;	
	private static final Logger LOG = Logger.getLogger(NavigationMar.class);
	private Configuration config;
	protected Actions actions;
	
	public NavigationMar(SeleniumBaseTest b){
		this.b = b;
		this.driver = b.getDriver();
		this.config = b.getConfig();
	}	

	public void navigateToMars() throws Exception {
		URL url = new URL(config.getProperty(PropertyMap.RPM_URL));
		navigateToUrl(url);
	}
	
	private void navigateToUrl(URL url)
	{
		LOG.info("Navigating to MAR URL, orgAlias=" + config.getProperty(PropertyMap.ORGALIAS) + ", url=" + url);
		driver.get(url.toString());
	}
	
	/**
	 * Log into Mars and navigate to XP 
	 * @param marTabName
	 * @param marUsername
	 * @param marPassword
	 * @param envXP
	 * @throws Exception
	 */
	public void logIntoMars(String marTabName, String marUsername, String marPassword, String envXP) throws Exception {
		MarsLogin marsLogin = new MarsLogin(driver);
		marsLogin.login(marUsername, marPassword);
		this.navigateToMarTab(marTabName);
		this.navigateToScreenByEnv(envXP);
	}
	
	/***
	 * @param marTabName
	 * @throws Exception
	 * Click on Mar tabs.
	 */
	public void navigateToMarTab(String marTabName) throws Exception {
		TabLinks tabLinks = new TabLinks(driver);
		MarTabs marTab = MarTabs.valueOf(marTabName);
		b.switchToDefaultWinFromFrame();
		b.switchToFrame(tabLinks.headerMenuFrame());
		
		switch (marTab) {
			case ACCESSION:
				b.clickHiddenPageObject(tabLinks.accessionTab(), 0);
				break;
			case CLIENT:
				b.clickHiddenPageObject(tabLinks.clientTab(), 0);		
				break;
			case FILE_MAINTENANCE:
				b.clickHiddenPageObject(tabLinks.fileMaintenanceTab(), 0);
				break;
			case PAYMENT:
				b.clickHiddenPageObject(tabLinks.paymentTab(), 0);
				break;
			case PAYOR:
				b.clickHiddenPageObject(tabLinks.payorTab(), 0);
				break;
			case FINANCIAL_MANAGEMENT:
				b.clickHiddenPageObject(tabLinks.financialManagementTab(), 0);
				break;		   
			default:
				break;
		}
	}
	
	/***
	 * @param envXP
	 * @throws Exception
	 * Click on Screen tab links
	 */
	public void navigateToScreenByEnv(String envXP) throws Exception {
		TabLinks tabLinks = new TabLinks(driver);
		XPEnv xpEnv = XPEnv.valueOf(envXP);
		b.switchToDefaultWinFromFrame();
		b.switchToFrame(tabLinks.menuFrame());
		
		switch (xpEnv) {
			case XP_ACCN_PATIENT_DEMOGRAPHICS:
				b.clickHiddenPageObject(tabLinks.patientDemographicsScreenTabLink(), 0);
				break;
			case XP_ACCN_SUPER_SEARCH:
				b.clickHiddenPageObject(tabLinks.superSearchScreenTabLink(), 0);
				break;
			case XP_PAYMENT_DEPOSITS:
				b.clickHiddenPageObject(tabLinks.paymentDepositsScreenTabLink(), 0);
				break;
			case XP_PATIENT_PAYMENTS:
	            b.clickHiddenPageObject(tabLinks.patientPaymentScreenTabLink(), 0);        
	            break;			
			case XP_CLIENT_PAYMENTS:
				b.clickHiddenPageObject(tabLinks.clientPaymentScreenTabLink(), 0);   
				break;
			case XP_REFUND_SEARCH:
				b.clickHiddenPageObject(tabLinks.refundSearchScreenTabLink(), 0);  
				break;
			case XP_ACCN_EP_SEARCH:
				b.clickHiddenPageObject(tabLinks.epSearchScreenTabLink(), 0);
				break;
			case XP_ACCN_SINGLE_STATEMENT:
				b.clickHiddenPageObject(tabLinks.singleStatementScreenTabLink(), 0);
				break;
			case XP_DOCUMENT_MAINTENANCE:
				b.clickHiddenPageObject(tabLinks.documentMaintenanceScreenTabLink(), 0);
				break;
			case XP_CLIENT_DEMOGRAPHICS:
				b.clickHiddenPageObject(tabLinks.clientDemographicsTabLink(), 0);
				break;
			case XP_CLIENT_PRICING_CONFIGURATION:
				b.clickHiddenPageObject(tabLinks.clientPricingConfigurationTabLink(), 0);
				break;
			case XP_ACCN_EP_SUMMARY:
	            b.clickHiddenPageObject(tabLinks.epSummaryScreenTabLink(), 0);
	            break;  
			case XP_CLIENT_SUBMISSION_CONFIGURATION:
	            b.clickHiddenPageObject(tabLinks.clientSubmissionConfigurationTabLink(), 0);
	            break;
			case XP_PAYOR_PRICING_CONFIGURATION:
	            b.clickHiddenPageObject(tabLinks.payorPricingConfigNewTabLink(), 0);
	            break;
	        case XP_PAYMENT_PAYMENT_SEARCH:
	            b.clickOnElement(tabLinks.paymentPaymentSearchTabLink());
	            break;
	        case XP_CLIENT_PRICE_INQUIRY:
	            b.clickOnElement(tabLinks.clientPriceInquiryTabLink());
	        	break;
	        case XP_PAYOR_CONTACT_MANAGER:
	            b.clickOnElement(tabLinks.payorContactManagerTabLink());
	        	break;
	        case XP_PAYOR_GROUP_DEMOGRAPHICS:
	            b.clickOnElement(tabLinks.payorGroupDemographicsTabLink());
	            break;
	        case XP_PAYOR_FIELD_REQUIREMENTS:
	        	b.clickOnElement(tabLinks.payorFieldRequirementsTabLink());
	            break;
	        case XP_FILE_MAINT_SPECIAL_PRICE_TABLE:
	        	b.clickOnElement(tabLinks.fileMaintSpecialPriceTableTabLink());
	        	break;
	        case XP_PAYOR_DEMOGRAPHICS:
	        	b.clickOnElement(tabLinks.payorDemographicsTabLink());
	        	break;
	        case XP_ACCN_HL7_IMPORTER:
	        	b.clickOnElement(tabLinks.Hl7ImporterScreenTabLink());
	        	break;
	        case XP_PAYOR_BILLING_RULES:
	        	b.clickOnElement(tabLinks.payorBillingRulesTabLink());
	        	break;
	        case XP_DIALYSIS_FREQUENCY_CONTROL:
	        	b.clickOnElement(tabLinks.payorDialFreqControlTabLink());
	        	break;
	        case XP_CLIENT_PHYSICIAN_ASSIGNMENT:
	        	b.clickOnElement(tabLinks.clientPhysicianAssignmentTabLink());
	        	break;
	        case XP_FILE_MAINT_LOGO_CONFIGURATION:
	        	b.clickOnElement(tabLinks.fileMainLogoConfigurationTabLink());
	        	break;
	        case XP_MPPR:
	          	b.clickOnElement(tabLinks.fileMainMultiProcPaymentReductionRuleTabLink());
	           	break;	
	        case XP_FILE_MAINT_PHYSICIAN_LICENSE:
	        	b.clickOnElement(tabLinks.fileMainPhysicianLicenseTabLink());
	        	break;
	        case XP_FILE_MAINT_QUESTION_ASSIGNMENT:
	        	b.clickOnElement(tabLinks.fileMainQuestionAssignmentTabLink());
	        	break;
	        case XP_FILE_MAINT_REASON_CODE:
	        	b.clickOnElement(tabLinks.fileMainReasonCodeTabLink());
	        	break;
	        case XP_FILE_MAINT_FEE_SCHEDULE:
	        	b.clickOnElement(tabLinks.fileMainFeeScheduleTabLink());
	        	break;
	        case XP_FILE_MAINT_FACILITY:
	        	b.clickOnElement(tabLinks.fileMainFacilityTabLink());
	        	break;
	        case XP_FILE_MAINT_CLIENT_BILLING_CATEGORY:
	        	b.clickOnElement(tabLinks.fileMainClientBillingCategoryTabLink());
	        	break;
	        case SYSTEM_MANAGEMENT_LOCK_INFORMATION:
	        	b.clickOnElement(tabLinks.systemManagementLockInformationTabLink());
	        	break;
	        case XP_FILE_MAINT_ELIGIBILITY_CONFIG:
	        	b.clickOnElement(tabLinks.fileMainEligibilityConfigTabLink());
	        	break;
        	case XP_FILE_MAINT_DIAGNOSIS_CODE:
        		b.clickOnElement(tabLinks.fileMainDiagnosisCodeTabLink());
        		break;
        	case XP_FILE_MAINT_ADJUSTMENT_CODE:
        		b.clickOnElement(tabLinks.fileMainAdjustmentCodeTabLink());
        		break;
        	case XP_FILE_MAINT_CROSS_REFERENCE_CONFIGURATION:
	        	b.clickOnElement(tabLinks.fileMainCrossReferenceConfigurationTabLink());
	        	break;
        	case XP_FILE_MAINT_CLAIM_STATUS_CONFIG:
	        	b.clickOnElement(tabLinks.fileMainClaimStatusConfigTabLink());
	        	break;
        	case XP_FILE_MAINT_PATTERN_DEFINITION:
        		b.clickOnElement(tabLinks.fileMainPatternDefinitionTabLink());
	        	break;
        	case XP_FILE_MAINT_DOCUMENT_MAINTENANCE:
        	    b.clickOnElement(tabLinks.fileMainDocMaintenceTabLink());
        	    break;
        	case XP_FILE_MAINT_PLACE_OF_SERVICE_CODE_CONFIGURATION:
        	    b.clickOnElement(tabLinks.fileMainPlaceOfServiceCodeConfigurationTabLink());
        	    break;
        	case XP_FILE_MAINT_SUBMISSION_AND_REMITTANCE:
        		b.clickOnElement(tabLinks.fileMainSubmissionAndRemittanceTabLink());
        	    break;
        	case XP_FILE_MAINT_TAXONOMY:
	            b.clickOnElement(tabLinks.fileMainTaxonomyTabLink());
	            break;
        	case XP_CLIENT_CONTACT_MANAGER:
        		b.clickOnElement(tabLinks.clientContactManagerTabLink());
	            break;
        	case XP_CLIENT_CORRESPONDENCE_LOG:
        		b.clickOnElement(tabLinks.clientCorrespondenceLogTabLink());
	            break;
        	case XP_ACCN_DAILY_RECEIPT:
        		b.clickOnElement(tabLinks.accnDailyReceiptTabLink());
	            break;
        	case XP_ACCN_EP_ASSIGNMENT:
        		b.clickOnElement(tabLinks.accnEpAssignmentTabLink());
	            break;
        	case XP_ACCN_EP_DUNNING_LETTER:
        		b.clickOnElement(tabLinks.accnEpDunningLetterTabLink());
	            break;
        	case XP_ACCN_NOTES_PROMISED_PAYMENTS:
        		b.clickOnElement(tabLinks.accnNotesPromisedPaymentsTabLink());
	            break;
        	case XP_PAYOR_EXCLUSIONS:
	        	b.clickOnElement(tabLinks.payorDemographicsTabLink());
	        	break;
        	case XP_ACCN_STANDING_ORDER:
	        	b.clickOnElement(tabLinks.accnStandingOrderTabLink());
	        	break;
        	case XP_PAYOR_CONTRACT_CONFIG:
	        	b.clickOnElement(tabLinks.payorContractConfigTabLink());
	        	break;
        	case XP_CLIENT_BILLING_RULES:
	            b.clickOnElement(tabLinks.clientBillingRulesTabLink());
	            break;
        	case XP_FILE_MAINT_PAYMENT_GATEWAY_CONFIG:
	            b.clickOnElement(tabLinks.fileMainPaymentGatewayTabLink());
	            break;
        	case XP_ACCESSION_TEST_UPDATE:
	            b.clickOnElement(tabLinks.accessionTestUpdateTabLink());
	            break;
        	case XP_FILE_MAINT_PROCEDURE_CODE:
        		b.clickOnElement(tabLinks.fileMainProcedureCodeTabLink());
	            break;
        	case XP_FILE_MAINT_REMITTANCE:
        		b.clickOnElement(tabLinks.fileMainRemittanceTabLink());
	            break;
        	case XP_PAYMENT_NON_CLIENT_ADJUSTMENTS:
	            b.clickOnElement(tabLinks.paymentNonClientAdjustmentsTabLink());
	            break;
        	case XP_FILE_MAINT_QUESTION_DEFINITION:
        		b.clickOnElement(tabLinks.fileMainQuestionDefinitionTabLink());
	        	break;    
	        default:
				String url = "Invalid Environment";
	            LOG.error("        Invalid Environment");
	            LOG.info("        Testing Against: Url: " + url);
	            break;
		}
		
		b.switchToDefaultWinFromFrame();
		b.switchToFrame(tabLinks.contentFrame());
		if (xpEnv != XPEnv.SYSTEM_MANAGEMENT_LOCK_INFORMATION) {
			b.switchToFrame(tabLinks.platformFrame());
		}
	}
	
	/**
	 * unlock data by element in the Lock Information table as the Break checkbox
	 * @param element
	 * @throws Exception
	 */
	public void unLockData(String type) throws Exception {
		try {
			this.navigateToScreenByEnv(XPEnv.SYSTEM_MANAGEMENT_LOCK_INFORMATION.toString());
			
			LockInformation lockInformation = new LockInformation(b.getWait());
			b.waitUntilElementPresent(lockInformation.submitBtn(), 5);
			
			List<WebElement> lstWebElement = new ArrayList<WebElement>();
			try {
				lstWebElement = lockInformation.lockInformationTblItemChk(type);
			} catch (Exception e) {
				LOG.error("        Element not found: " + e.getMessage());
			}
			
			if (lstWebElement != null && lstWebElement.size() > 0) {
				for (WebElement element : lstWebElement) {
					b.clickHiddenPageObject(element, 0);
				}
				b.clickHiddenPageObject(lockInformation.submitBtn(), 0);
			}
		} catch (Exception e) {
			LOG.error("        Unlock error: " + e.getMessage());
		}
	}
}
