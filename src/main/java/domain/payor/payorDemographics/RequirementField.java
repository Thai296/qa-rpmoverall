package domain.payor.payorDemographics;

import java.sql.Date;

public class RequirementField {

	private String headerPyrName = "";
	private String headerGrpName = "";
	private Date effectiveDate;

	// Payor Address and Contact Information section
	private String address1 = "";
	private String postalCode;

	public String getHeaderPyrName() {
		return headerPyrName;
	}

	public void setHeaderPyrName(String headerPyrName) {
		this.headerPyrName = headerPyrName;
	}

	public String getHeaderGrpName() {
		return headerGrpName;
	}

	public void setHeaderGrpName(String headerGrpName) {
		this.headerGrpName = headerGrpName;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result + ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result + ((headerGrpName == null) ? 0 : headerGrpName.hashCode());
		result = prime * result + ((headerPyrName == null) ? 0 : headerPyrName.hashCode());
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
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
		RequirementField other = (RequirementField) obj;
		if (address1 == null) {
			if (other.address1 != null)
				return false;
		} else if (!address1.equals(other.address1))
			return false;
		if (effectiveDate == null) {
			if (other.effectiveDate != null)
				return false;
		} else if (!effectiveDate.equals(other.effectiveDate))
			return false;
		if (headerGrpName == null) {
			if (other.headerGrpName != null)
				return false;
		} else if (!headerGrpName.equals(other.headerGrpName))
			return false;
		if (headerPyrName == null) {
			if (other.headerPyrName != null)
				return false;
		} else if (!headerPyrName.equals(other.headerPyrName))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RequirementField ["
				+ "headerPyrName=" + headerPyrName 
				+ ", headerGrpName=" + headerGrpName
				+ ", effectiveDate=" + effectiveDate 
				+ ", address1=" + address1 
				+ ", postalCode=" + postalCode 
				+ "]";
	}

}
