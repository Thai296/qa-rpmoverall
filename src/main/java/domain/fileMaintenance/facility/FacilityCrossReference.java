package domain.fileMaintenance.facility;

import java.sql.Date;

public class FacilityCrossReference {
	private Date effectiveDate;
	private Date expirationDate;
	private String crossReferenceDescription;
	
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getCrossReferenceDescription() {
		return crossReferenceDescription;
	}
	public void setCrossReferenceDescription(String crossReferenceDescription) {
		this.crossReferenceDescription = crossReferenceDescription;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((crossReferenceDescription == null) ? 0
						: crossReferenceDescription.hashCode());
		result = prime * result
				+ ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result
				+ ((expirationDate == null) ? 0 : expirationDate.hashCode());
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
		FacilityCrossReference other = (FacilityCrossReference) obj;
		if (crossReferenceDescription == null) {
			if (other.crossReferenceDescription != null)
				return false;
		} else if (!crossReferenceDescription
				.equals(other.crossReferenceDescription))
			return false;
		if (effectiveDate == null) {
			if (other.effectiveDate != null)
				return false;
		} else if (!effectiveDate.equals(other.effectiveDate))
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "FacilityCrossReference [effectiveDate=" + effectiveDate
				+ ", expirationDate=" + expirationDate
				+ ", crossReferenceDescription=" + crossReferenceDescription
				+ "]";
	}
	
	

}
