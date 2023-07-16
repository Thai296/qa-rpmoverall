package com.overall.menu;

import com.eviware.soapui.support.StringUtils;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class MenuNavigation
{
	private static final Logger LOG = Logger.getLogger(MenuNavigation.class);
	
	private final RemoteWebDriver driver;
	private final Configuration config;
	
	public MenuNavigation(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.config = config;
	}
	
	public void navigateToTestCodePage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/testcode.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToAccnDetailPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accnprocessing.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToClientTransactionDetailPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clntransdet.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToIncrpricingPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/incrpricing.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToConsolidationRulesPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/payor/consolidationrules.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToNonClientAdjustmentsPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/payment/pymtnonclnadjs.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToRPM() throws MalformedURLException
	{
		navigateToUrl(new URL(config.getProperty(PropertyMap.RPM_URL)));
	}	
	
	public void navigateToAccnTestUpdatePage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accntestupdate.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToClientBatchSingleDemandStatementPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/batchsingledemandstmt.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToAccnHistoryLogPage()  throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accnhistory.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToSubmissionAndRemittancePage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintsubmremit.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToDocUploadStoragePage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/docstorewebapp?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToDocUploadStorageGetDocPage(int docStoreSeqId) throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/docstorewebapp/getdoc.html?id="+docStoreSeqId+"&orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToEligibilityConfigurationPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemainteligibilityconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToElectronicPaymentPostingWSPage() throws MalformedURLException
	{
		navigateToUrl(new URL(StringUtils.replace(config.getProperty(PropertyMap.EPPWS_URL), "/services?", "/?")));
	}

	public void navigateToEligibilityWsIndexPage() throws MalformedURLException
	{
		navigateToUrl(new URL(StringUtils.replace(config.getProperty(PropertyMap.ELIGWS_URL), "/services?", "/?")));
	}

	public void navigateToEligibilityWsHealthCheckPage() throws MalformedURLException
	{
		navigateToUrl(new URL(StringUtils.replace(config.getProperty(PropertyMap.ELIGWS_URL), "/services?", "/m0n1tor?")));
	}

	public void navigateToClaimStatusWSPage() throws MalformedURLException
	{
		// todo afa URL url = new URL(config.getUrl(UrlTyp.ELEC_PMT_POSTING_WS)+"/filemaint/filemainteligibilityconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		//navigateToUrl(url);
	}
	
	public void navigateToFacilityPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintfacility.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToAccessionWSPage() throws MalformedURLException
	{
		// todo afa URL url = new URL(config.getUrl(UrlTyp.ELEC_PMT_POSTING_WS)+"/filemaint/filemainteligibilityconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		//navigateToUrl(url);
	}

	public void navigateToReasonCodePage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintreasoncodeconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToFeeSchedulePage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintfeeschedule.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToPayorExclusionsPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clnpyrexcls.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToClientPricingConfigurationPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clnprcconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToPatientNotificationConfigurationPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintptnotificationletterconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToSpecialPriceTablePage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintspecialpricetable.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToAccessionManualPricingReleasePage() throws MalformedURLException
    {
        URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accessionmanualpricingrelease.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
        navigateToUrl(url);
    }

	public void navigateToClientPayorExclusionPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clnpyrexcls.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToAccessionDailyReceiptPage() throws MalformedURLException
    {
        URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accessiondailyreceipt.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
        navigateToUrl(url);
    }

	public void navigateToAccessionNotesPromisedPayment() throws MalformedURLException
	{
        URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accnnotespromisedpayments.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
        navigateToUrl(url);
    }
	
	public void navigateToLoginPage() throws MalformedURLException
	{	
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/login.html#?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
        navigateToUrl(url);
	}
	public void navigateToAccessionEPDunningLetterPage() throws MalformedURLException {
        URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accnepdunningletter.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
        navigateToUrl(url);
    }
	
	private void navigateToUrl(URL url)
	{
		LOG.info("Navigating to URL, orgAlias="+config.getProperty(PropertyMap.ORGALIAS)+", url="+url);
		driver.get(url.toString());
	}

	public void navigateToPatientEstimationPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/pscpatientestimation.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	public void navigateToSingleStatementPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accnsinglestatement.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	public void navigateToEligibilityResponseTranslationPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemainteligibilitytranslation.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	public void navigateToEPSearchPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accnepsearch.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	public void navigateToSuperSearchPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accnsupersearch.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	public void navigateToPSCPrepayPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/psc.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
    public void navigateToAccessionEPAssignmentPage() throws MalformedURLException
    {
    	URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accnepassignment.html?orgalias"+config.getProperty(PropertyMap.ORGALIAS));
    	navigateToUrl(url);
    }
	
	public void navigateToClnSubmConfig() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clnsubmconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToClientDemographicsPage() throws MalformedURLException{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clientdemographics.html?orgalias=" + config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToClientCorrespondenceLogTestPage() throws MalformedURLException
    {
        URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clientcorrespondencelog.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
        navigateToUrl(url);
    }
	
	public void navigateToPayorContractConfig() throws MalformedURLException{	
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/payor/payorcontractconfig.html?orgalias=" + config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

    
    public void navigateToClientEligibilityCensusConfigurationPage() throws MalformedURLException{    
        URL url  = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clienteligibilitycensusconfiguration.html?orgalias=" +config.getProperty(PropertyMap.ORGALIAS));
        navigateToUrl(url);
    }
    
    public void navigateToClnContactManager() throws MalformedURLException
    {
    	URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clientcontactmanager.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
    	navigateToUrl(url);
    }
    
	public void navigateToPayorFieldRequirementsPage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL) + "/payor/payorfieldrequirements.html?orgalias=" + config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
    
    public void navigateToHomePage() throws MalformedURLException {
        URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL) + "/index.html?orgalias=" + config.getProperty(PropertyMap.ORGALIAS));
        navigateToUrl(url);
    }
    
    public void navigateToPaymentDepositsPage() throws MalformedURLException {
        URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL) + "/payment/paymentdeposits.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
        navigateToUrl(url);
    }

    public void navigateToPhysicianLicensePage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintphysicianlicense.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
    }

    public void navigateToCrossReferenceConfigPage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintcrossrefconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
    public void navigateToClientBillingCatagoryPage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintclnbillingcategory.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

    public void navigateToPlaceOfServiceCodeConfigPage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintposcodeconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToPatientDemographicsPage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/accession/accessionpatientdemographics.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
    public void navigateToPayorContactManagerPage() throws MalformedURLException
   	{
   		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/payor/payorcontactmanager.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
   		navigateToUrl(url);
   	}

    public void navigateToDiagnosisCodePage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintdiagcode.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

    public void navigateToPayorGroupDemographicsPage() throws MalformedURLException{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/payor/payorgrpdemographics.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

    public void navigateToPayorDemographicsPage() throws MalformedURLException{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/payor/payordemographics.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
    }
	
    public void navigateToClientBillingRulesPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clientbillingrules.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

    public void navigateToFileMaintAdjustmentCodePage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL) + "/filemaint/filemaintadjcode.html?orgalias=" + config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

    public void navigateToClaimStatusConfigPage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintclaimstatusconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

    public void navigateToClientPhysicianAssignmentPage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clientphysassignment.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

    public void navigateToLogoConfigurationPage() throws MalformedURLException {
   		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintlogoconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
   		navigateToUrl(url);
   	}
    
    public void navigateToProcedureCodePage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintprocedurecode.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
    
    public void navigateToQuestionAssignmentPage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintquestionassignment.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
    
    public void navigateToTaxonomyCodePage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemainttaxonomycode.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

	public void navigateToQuestionDefinitionPage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/filemaint/filemaintquestiondefinition.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToClientLineItemDisputePage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/client/clnlineitemdispute.html?portalMenuEnabled=true&orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
	
	public void navigateToGooglePage() throws MalformedURLException {
		URL url = new URL("https://www.google.com/?hl=en-US");
		navigateToUrl(url);
	}

	public void navigateToAccessionPatientDemographicsPage() throws MalformedURLException {
        URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL) + "/accession/accessionpatientdemographics.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
        navigateToUrl(url);
    }
	public void navigateToClientPaymentsPage() throws MalformedURLException
	{
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/payment/clientpayments.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

    public void navigateToAccessionDiagnosis() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL) + "/accession/accndiagconfig.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}

    public void navigateToClientAuditLogPage() throws MalformedURLException {
		URL url = new URL(config.getProperty(PropertyMap.XIFINPORTAL_URL)+"/clientauditlog.html?orgalias="+config.getProperty(PropertyMap.ORGALIAS));
		navigateToUrl(url);
	}
}
