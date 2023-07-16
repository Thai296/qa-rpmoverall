package com.overall.mar.menu;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Element for Mar tabs
 *
 */
public class TabLinks {
	private WebDriverWait wait;
	protected Logger logger;
	
	public TabLinks(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	/***
	 * Mar HTML IFRAME tag
	 * @return
	 */
	public WebElement menuFrame() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menu")));		
	}
	
	public WebElement rpmiFrame() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rpmiframe")));		
	}
	
	public WebElement contentFrame() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("content")));		
	}
	
	public WebElement platformFrame() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("platformiframe")));		
	}
	
	public WebElement headerMenuFrame() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("marsheader")));		
	}
	
	// Begin tab element section
	
	public WebElement accessionTab() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[id='accn'] > a")));		
	}
	
	public WebElement clientTab() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[id='cln'] > a")));		
	}
	
	public WebElement fileMaintenanceTab() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[id='fm'] > a")));		
	}
	
	public WebElement paymentTab() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[id='payment'] > a")));		
	}
	
	public WebElement payorTab() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[id='pyr'] > a")));		
	}
	
	public WebElement financialManagementTab() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[id='misc'] > a")));		
	}
	
	// Begin Mar tab link element
	
	public WebElement patientDemographicsScreenTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='acnPatDemogrphcPlatform']")));
	}
	
	public WebElement superSearchScreenTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='acnSupSrchPlatform']")));
	}
	
	public WebElement paymentDepositsScreenTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pymtDepositsPlatform']")));
	}
	
	public WebElement patientPaymentScreenTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pymtPtPmtsPlatform']")));
    }
	
	public WebElement clientPaymentScreenTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pymtClnPmtsPlatform']")));  
    }
	
	public WebElement refundSearchScreenTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pymtRefundBatchSrchPlatform']"))); 
    }
	
	public WebElement epSearchScreenTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='epSrchPlatform']")));
	}
	
	public WebElement singleStatementScreenTabLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='acnStmntPlatform']")));
	}
	
	public WebElement documentMaintenanceScreenTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='/fileMaint/fmDocMaintPlatform.jsp']"))); 
    }
	
	public WebElement epSummaryScreenTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='epSumPlatform.jsp']"))); 
    }
	
	public WebElement clientDemographicsTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='clDemogrphcsPlatform']")));
    }
	
	public WebElement clientPricingConfigurationTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='clPrcngPlatform']")));
    }
	
	public WebElement clientSubmissionConfigurationTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='clSubmConfigPlatform']")));
    }
	
	public WebElement payorDemographicsTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pyrDemgrphcPlatform']")));
    }
	
	public WebElement payorPricingConfigTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pyrPrcngConfig']")));
    }

	public WebElement payorPricingConfigNewTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pyrPrcngConfigPlatform']")));
    }
	
	public WebElement paymentPaymentSearchTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pymtSrchPlatform']")));
    }
	
	public WebElement clientPriceInquiryTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='clPrcInqryPlatform']")));
    }
	
	public WebElement payorContactManagerTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pyrCntctMgrPlatform']")));
    }
	
	public WebElement payorGroupDemographicsTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pyrGrpDemgrphcPlatform']")));
    }
	
	public WebElement payorFieldRequirementsTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pyrFldReqmntsPlatform']")));
    }	
	
	public WebElement fileMaintSpecialPriceTableTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmSpecialPriceTablePlatform']")));
	}
	
	public WebElement Hl7ImporterScreenTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='acnHl7ImporterPlatform']")));
	}
	
	public WebElement payorBillingRulesTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pyrBillingRulesPlatform']")));
	}
	
	public WebElement payorDialFreqControlTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pyrDialFrqCtrlPlatform']")));
	}
	
	public WebElement clientPhysicianAssignmentTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='clnPhysAssgnPlatform']")));
	}
	
	public WebElement fileMainLogoConfigurationTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmLogoConfigPlatform']")));
	}
	
	public WebElement fileMainMultiProcPaymentReductionRuleTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmMPPRRulePlatform']")));
	}
	
	public WebElement fileMainPhysicianLicenseTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmPhyLicConfPlatform']")));
	}
	
	public WebElement fileMainQuestionAssignmentTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmOEquestAsgnmtPlatform']")));
	}
	
	public WebElement fileMainReasonCodeTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmRsnCdConfigPlatform']")));
	}
	
	public WebElement fileMainFeeScheduleTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmFeeSchedulePlatform']")));
	}
	
	public WebElement fileMainFacilityTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmFacDemogrphcsPlatform']")));
	}
	
	public WebElement fileMainEligibilityConfigTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmEligConfigPlatform']")));
	}
	
	public WebElement fileMainClientBillingCategoryTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmClnBillingCategoryPlatform']")));
	}
	
	public WebElement systemManagementLockInformationTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='lockInfo']")));
	}
	
	public WebElement fileMainDiagnosisCodeTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='/fileMaint/fmDgnsisCdesNew.jsp']")));
    }
	
	public WebElement fileMainCrossReferenceConfigurationTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmClnCrssRefPlatform']")));
	}
	
	public WebElement fileMainClaimStatusConfigTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmClaimStatusConfigPlatform']")));
	}
	
	public WebElement fileMainAdjustmentCodeTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='/fileMaint/fmAdjstmntPlatform.jsp']")));
    }
	
	public WebElement fileMainPatternDefinitionTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmOEvalPatMaintPlatform']")));
	}

    public WebElement fileMainDocMaintenceTabLink() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='/fileMaint/fmDocMaintPlatform.jsp']")));
    }
    
    public WebElement fileMainPlaceOfServiceCodeConfigurationTabLink() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmPosOverridePlatform']")));
    }
    
    public WebElement fileMainSubmissionAndRemittanceTabLink() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmSubmRemitPlatform']")));
    }
	
	public WebElement fileMainTaxonomyTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='/fileMaint/fmTaxonomyCodePlatform.jsp']")));
    }
	
	public WebElement clientContactManagerTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='clNotesPrmsPmtsPlatform']")));
    }
	
	public WebElement clientCorrespondenceLogTabLink(){	
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='clCorLogPlatform']")));
    }
	
	public WebElement accnDailyReceiptTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='acnDlyRcptPlatform']")));
    }
	
	public WebElement accnEpAssignmentTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='epAssignmentPlatform']")));
    }
	
	public WebElement accnEpDunningLetterTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='epDunLtrPlatform']")));
    }
	
	public WebElement accnNotesPromisedPaymentsTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='acnNotesPrmsPmtsPlatform']")));
    }
	
	public WebElement payorExclusionsTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='clPyrExclsnPlatform']")));
    }
	
	public WebElement accnStandingOrderTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='acnStndngOrderPlatform']")));
    }
	
	public WebElement payorContractConfigTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pyrContrctConfigPlatform']")));
    }
	
	public WebElement clientBillingRulesTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='clnBillingRulesPlatform']")));  
    }
	
	public WebElement fileMainPaymentGatewayTabLink(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='/fileMaint/fmPaymentGatewaySettingsPlatform.jsp']")));
    }
	
	public WebElement accessionTestUpdateTabLink() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='acnLabComponentNew']")));  
	}
	
	public WebElement fileMainProcedureCodeTabLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='fmProcCodePlatform']")));
	}

	public WebElement fileMainRemittanceTabLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='/fileMaint/fmX12RemtPlatform.jsp']")));
	}

	public WebElement paymentNonClientAdjustmentsTabLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='pymtNonClnAdjs']")));
	}
    
    public WebElement fileMainQuestionDefinitionTabLink() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-page*='/fileMaint/fmOEquestMaintPlatform.jsp']")));
    }
}