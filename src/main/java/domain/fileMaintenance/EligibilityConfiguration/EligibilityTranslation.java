package domain.fileMaintenance.EligibilityConfiguration;

public class EligibilityTranslation {

	private String pyrId;
	private String newPyrId;
	private String matchType;
	private String benefitType;
	private String response;
	private int prio;
	private String subIdType;
	private String serviceTypeCode;
	private Boolean isAllowSecondaryTranslation;
	private Boolean isBypassUnknownResponseErrors;
	private Boolean isCheckElig;
	private String EligSvcName;
	
	
	
	public String getPyrId() {
		return pyrId;
	}
	public void setPyrId(String pyrId) {
		this.pyrId = pyrId;
	}
	public String getNewPyrId() {
		return newPyrId;
	}
	public void setNewPyrId(String newPyrId) {
		this.newPyrId = newPyrId;
	}
	public String getMatchType() {
		return matchType;
	}
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	public String getBenefitType() {
		return benefitType;
	}
	public void setBenefitType(String benefitType) { this.benefitType = benefitType;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public int getPrio() {
		return prio;
	}
	public void setPrio(int prio) {
		this.prio = prio;
	}
	public String getSubIdType() {
		return subIdType;
	}
	public void setSubIdType(String subIdType) {
		this.subIdType = subIdType;
	}
	public String getServiceTypeCode() {
		return serviceTypeCode;
	}
	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}
	public Boolean getIsAllowSecondaryTranslation() {
		return isAllowSecondaryTranslation;
	}
	public void setIsAllowSecondaryTranslation(Boolean isAllowSecondaryTranslation) {
		this.isAllowSecondaryTranslation = isAllowSecondaryTranslation;
	}
	public Boolean getIsBypassUnknownResponseErrors() {
		return isBypassUnknownResponseErrors;
	}
	public void setIsBypassUnknownResponseErrors(Boolean isBypassUnknownResponseErrors) {
		this.isBypassUnknownResponseErrors = isBypassUnknownResponseErrors;
	}
	public Boolean getIsCheckElig() {
		return isCheckElig;
	}
	public void setIsCheckElig(Boolean isCheckElig) {
		this.isCheckElig = isCheckElig;
	}
	public String getEligSvcName() {
		return EligSvcName;
	}
	public void setEligSvcId(String eligSvcName) {
		EligSvcName = eligSvcName;
	}
}
