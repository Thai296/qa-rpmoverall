package com.overall.mar.menu;

/**
 * This enum define all links as Mar left side bar
 * 
 *
 */
public enum XPEnv {
	XP_ACCN_PATIENT_DEMOGRAPHICS("XP_AccnPatientDemographics"),
	XP_ACCN_SUPER_SEARCH("XP_AccnSuperSearch"),
	XP_PAYMENT_DEPOSITS("XP_PaymentDeposits"),
	XP_PATIENT_PAYMENTS("XP_PatientPayments"),
	XP_CLIENT_PAYMENTS("XP_ClientPayments"),
	XP_REFUND_SEARCH("XP_RefundSearch"),
	XP_ACCN_EP_SEARCH("XP_AccnEpSearch"),
	XP_ACCN_SINGLE_STATEMENT("XP_SingleStatement"),
	XP_DOCUMENT_MAINTENANCE("XP_DocumentMaintenance"), 
	XP_CLIENT_DEMOGRAPHICS("XP_DEMOGRAPHICS"),
	XP_CLIENT_PRICING_CONFIGURATION("XP_CLIENTPRICINGCONFIGURATION"),
	XP_ACCN_EP_SUMMARY("XP_AccnEpSummary"),
	XP_CLIENT_SUBMISSION_CONFIGURATION("XP_ClientSubmissionConfiguration"),
    XP_PAYOR_PRICING_CONFIGURATION("XP_PayorPricingConfiguration"),
    XP_PAYMENT_PAYMENT_SEARCH("XP_PaymentPaymentSearch"),
	XP_CLIENT_PRICE_INQUIRY("XP_ClientPriceInquiry"),
    XP_PAYOR_CONTACT_MANAGER("XP_PayorContactManager"),
    XP_PAYOR_GROUP_DEMOGRAPHICS("XP_PayorGroupDemographics"),
    XP_PAYOR_FIELD_REQUIREMENTS("XP_PayorFieldRequirements"),
	XP_FILE_MAINT_SPECIAL_PRICE_TABLE("XP_SpecialPriceTable"),
	XP_PAYOR_DEMOGRAPHICS("XP_PayorDemographics"),
	XP_ACCN_HL7_IMPORTER("XP_AccnHL7Importer"),
	XP_PAYOR_BILLING_RULES("XP_PayorBillingRules"),
	XP_DIALYSIS_FREQUENCY_CONTROL("XP_DialysisFrequencyControl"),
	XP_CLIENT_PHYSICIAN_ASSIGNMENT("XP_ClientPhysicianAssignment"),
	XP_FILE_MAINT_LOGO_CONFIGURATION("XP_Logo_Configuration"),
	XP_MPPR("XP_Multi_Proc_Payment_Reduction_Rule"),
	XP_FILE_MAINT_PHYSICIAN_LICENSE("XP_PhysicianLicense"),
	XP_FILE_MAINT_QUESTION_ASSIGNMENT("XP_QuestionAssignment"),
	XP_FILE_MAINT_REASON_CODE("XP_ReasonCode"),
	XP_FILE_MAINT_FEE_SCHEDULE("XP_FeeSchedule"),
	XP_FILE_MAINT_FACILITY("XP_Facility"),
	XP_FILE_MAINT_ELIGIBILITY_CONFIG("XP_Eligibility_Config"),
	XP_FILE_MAINT_CLIENT_BILLING_CATEGORY("XP_ClientBillingCategory"),
	SYSTEM_MANAGEMENT_LOCK_INFORMATION("Lock Information"),
	XP_FILE_MAINT_CROSS_REFERENCE_CONFIGURATION("XP_CrossReferenceConfiguration"), 
	XP_FILE_MAINT_CLAIM_STATUS_CONFIG("XP_FMClaimStatusConfig"),
	XP_FILE_MAINT_ADJUSTMENT_CODE ("XP_AdjustmentCode"),
	XP_FILE_MAINT_PATTERN_DEFINITION("XP_PatternDefinition"),
	XP_FILE_MAINT_DIAGNOSIS_CODE("XP_DiagnosisCode"),
    XP_FILE_MAINT_PLACE_OF_SERVICE_CODE_CONFIGURATION("XP_FileMaintPlaceOfServiceCodeConfiguration"),
	XP_FILE_MAINT_DOCUMENT_MAINTENANCE("XP_FileMaintDocumentMaintenance"),
	XP_FILE_MAINT_SUBMISSION_AND_REMITTANCE("XP_FileMaintSubmisstionAndRemittance"),
	XP_FILE_MAINT_TAXONOMY("XP_Taxonomy"),
	XP_CLIENT_CONTACT_MANAGER("XP_ClientContactManager"),
	XP_CLIENT_CORRESPONDENCE_LOG("XP_ClientCorrespondenceLog"),
	XP_ACCN_DAILY_RECEIPT("XP_AccnDailyReceipt"),
	XP_ACCN_EP_ASSIGNMENT("XP_AccnEpAssignment"),
	XP_ACCN_EP_DUNNING_LETTER("XP_AccnEpDunningLetter"),
	XP_ACCN_NOTES_PROMISED_PAYMENTS("XP_AccnNotesPromisedPayments"),
	XP_PAYOR_EXCLUSIONS("XP_PayorExclusions"),
	XP_ACCN_STANDING_ORDER("XP_AccnStandingOrder"),
	XP_PAYOR_CONTRACT_CONFIG("XP_PayorContractConfig"),
	XP_CLIENT_BILLING_RULES("XP_Client_Billing_Rules"),
	XP_FILE_MAINT_PAYMENT_GATEWAY_CONFIG("XP_PaymentGateway"),
	XP_ACCESSION_TEST_UPDATE("Xp_AccessionTestUpdate"),
	XP_FILE_MAINT_PROCEDURE_CODE("XP_ProcedureCode"),	
    XP_FILE_MAINT_REMITTANCE("XP_FileMaintRemittance"),
    XP_PAYMENT_NON_CLIENT_ADJUSTMENTS("XP_PaymentNonClientAdjustments"),
    XP_FILE_MAINT_QUESTION_DEFINITION("XP_FileMaintQuestionDefinition");
    
	public String marEnv;

	private XPEnv(String marEnv) {
		this.marEnv = marEnv;
	}

	public String getValue() {
		return this.marEnv;
	}
}
