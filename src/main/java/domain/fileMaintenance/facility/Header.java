package domain.fileMaintenance.facility;

public class Header {
	private String facilityId;
	private String name;
	private String type;
	private String primaryFacility;
	private String billingFacility;
	private String website;
	private boolean isWhollyOwnedSubsidiary;
	private boolean isDeleteFacilityDemographicsRecord;
	private boolean isUseClnPrimaryFacBilling;

	public boolean getIsUseClnPrimaryFacBilling()
	{
		return isUseClnPrimaryFacBilling;
	}

	public void setIsUseClnPrimaryFacBilling(boolean useClnPrimaryFacBilling)
	{
		isUseClnPrimaryFacBilling = useClnPrimaryFacBilling;
	}

	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getPrimaryFacility() {
		return primaryFacility;
	}
	public void setPrimaryFacility(String primaryFacility) {
		this.primaryFacility = primaryFacility;
	}
	
	public String getBillingFacility() {
		return billingFacility;
	}
	public void setBillingFacility(String billingFacility) {
		this.billingFacility = billingFacility;
	}
	
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public boolean isWhollyOwnedSubsidiary() {
		return isWhollyOwnedSubsidiary;
	}	
	public void setWhollyOwnedSubsidiary(boolean isWhollyOwnedSubsidiary) {
		this.isWhollyOwnedSubsidiary = isWhollyOwnedSubsidiary;
	}
	
	public boolean isDeleteFacilityDemographicsRecord() {
		return isDeleteFacilityDemographicsRecord;
	}
	public void setDeleteFacilityDemographicsRecord(
			boolean isDeleteFacilityDemographicsRecord) {
		this.isDeleteFacilityDemographicsRecord = isDeleteFacilityDemographicsRecord;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((billingFacility == null) ? 0 : billingFacility.hashCode());
		result = prime * result
				+ ((facilityId == null) ? 0 : facilityId.hashCode());
		result = prime * result
				+ (isDeleteFacilityDemographicsRecord ? 1231 : 1237);
		result = prime * result + (isWhollyOwnedSubsidiary ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((primaryFacility == null) ? 0 : primaryFacility.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((website == null) ? 0 : website.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Header other = (Header) obj;
		if (billingFacility == null) {
			if (other.billingFacility != null)
				return false;
		} else if (!billingFacility.equals(other.billingFacility))
			return false;
		if (facilityId == null) {
			if (other.facilityId != null)
				return false;
		} else if (!facilityId.equals(other.facilityId))
			return false;
		if (isDeleteFacilityDemographicsRecord != other.isDeleteFacilityDemographicsRecord)
			return false;
		if (isWhollyOwnedSubsidiary != other.isWhollyOwnedSubsidiary)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (primaryFacility == null) {
			if (other.primaryFacility != null)
				return false;
		} else if (!primaryFacility.equals(other.primaryFacility))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (website == null) {
			if (other.website != null)
				return false;
		} else if (!website.equals(other.website))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Header [facilityId=" + facilityId + ", name=" + name
				+ ", type=" + type + ", primaryFacility=" + primaryFacility
				+ ", billingFacility=" + billingFacility + ", website="
				+ website + ", isWhollyOwnedSubsidiary="
				+ isWhollyOwnedSubsidiary
				+ ", isDeleteFacilityDemographicsRecord="
				+ isDeleteFacilityDemographicsRecord + "]";
	}
	
}
