package domain.fileMaintenance.facility;

import java.util.Set;


public class FacilityAddtionalInformation {
	private boolean isDiagnosticLab; 
	private boolean isMammogramHighRiskPatient;
	private boolean isMammogramLowRiskPatient;
	private boolean isPathology;
	private boolean isRadiology;
	private String taxId;
	private String clia;
	private String organizationalNpi;
	private String facilityNpi;
	private String taxonomyCode;
	private Set<String> facilityGroup;
	private String mammoCert;
	
	public boolean isDiagnosticLab() {
		return isDiagnosticLab;
	}
	public void setDiagnosticLab(boolean isDiagnosticLab) {
		this.isDiagnosticLab = isDiagnosticLab;
	}
	public boolean isMammogramHighRiskPatient() {
		return isMammogramHighRiskPatient;
	}
	public void setMammogramHighRiskPatient(boolean isMammogramHighRiskPatient) {
		this.isMammogramHighRiskPatient = isMammogramHighRiskPatient;
	}
	public boolean isMammogramLowRiskPatient() {
		return isMammogramLowRiskPatient;
	}
	public void setMammogramLowRiskPatient(boolean isMammogramLowRiskPatient) {
		this.isMammogramLowRiskPatient = isMammogramLowRiskPatient;
	}
	public boolean isPathology() {
		return isPathology;
	}
	public void setPathology(boolean isPathology) {
		this.isPathology = isPathology;
	}
	public boolean isRadiology() {
		return isRadiology;
	}
	public void setRadiology(boolean isRadiology) {
		this.isRadiology = isRadiology;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getClia() {
		return clia;
	}
	public void setClia(String clia) {
		this.clia = clia;
	}
	public String getOrganizationalNpi() {
		return organizationalNpi;
	}
	public void setOrganizationalNpi(String organizationalNpi) {
		this.organizationalNpi = organizationalNpi;
	}
	public String getTaxonomyCode() {
		return taxonomyCode;
	}
	public void setTaxonomyCode(String taxonomyCode) {
		this.taxonomyCode = taxonomyCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clia == null) ? 0 : clia.hashCode());
		result = prime * result
				+ ((facilityGroup == null) ? 0 : facilityGroup.hashCode());
		result = prime * result
				+ ((facilityNpi == null) ? 0 : facilityNpi.hashCode());
		result = prime * result + (isDiagnosticLab ? 1231 : 1237);
		result = prime * result + (isMammogramHighRiskPatient ? 1231 : 1237);
		result = prime * result + (isMammogramLowRiskPatient ? 1231 : 1237);
		result = prime * result + (isPathology ? 1231 : 1237);
		result = prime * result + (isRadiology ? 1231 : 1237);
		result = prime
				* result
				+ ((organizationalNpi == null) ? 0 : organizationalNpi
						.hashCode());
		result = prime * result + ((taxId == null) ? 0 : taxId.hashCode());
		result = prime * result
				+ ((taxonomyCode == null) ? 0 : taxonomyCode.hashCode());
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
		FacilityAddtionalInformation other = (FacilityAddtionalInformation) obj;
		if (clia == null) {
			if (other.clia != null)
				return false;
		} else if (!clia.equals(other.clia))
			return false;
		if (facilityGroup == null) {
			if (other.facilityGroup != null)
				return false;
		} else if (!facilityGroup.equals(other.facilityGroup))
			return false;
		if (facilityNpi == null) {
			if (other.facilityNpi != null)
				return false;
		} else if (!facilityNpi.equals(other.facilityNpi))
			return false;
		if (isDiagnosticLab != other.isDiagnosticLab)
			return false;
		if (isMammogramHighRiskPatient != other.isMammogramHighRiskPatient)
			return false;
		if (isMammogramLowRiskPatient != other.isMammogramLowRiskPatient)
			return false;
		if (isPathology != other.isPathology)
			return false;
		if (isRadiology != other.isRadiology)
			return false;
		if (organizationalNpi == null) {
			if (other.organizationalNpi != null)
				return false;
		} else if (!organizationalNpi.equals(other.organizationalNpi))
			return false;
		if (taxId == null) {
			if (other.taxId != null)
				return false;
		} else if (!taxId.equals(other.taxId))
			return false;
		if (taxonomyCode == null) {
			if (other.taxonomyCode != null)
				return false;
		} else if (!taxonomyCode.equals(other.taxonomyCode))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FacilityAddtionalInformation [isDiagnosticLab="
				+ isDiagnosticLab + ", isMammogramHighRiskPatient="
				+ isMammogramHighRiskPatient + ", isMammogramLowRiskPatient="
				+ isMammogramLowRiskPatient + ", isPathology=" + isPathology
				+ ", isRadiology=" + isRadiology + ", taxId=" + taxId
				+ ", clia=" + clia + ", organizationalNpi=" + organizationalNpi
				+ ", facilityNpi=" + facilityNpi + ", taxonomyCode="
				+ taxonomyCode + ", facilityGroup=" + facilityGroup
				+ ", mammoCert=" + mammoCert + "]";
	}
	public Set<String> getFacilityGroup() {
		return facilityGroup;
	}
	public void setFacilityGroup(Set<String> facilityGroup) {
		this.facilityGroup = facilityGroup;
	}
	public String getFacilityNpi() {
		return facilityNpi;
	}
	public void setFacilityNpi(String facilityNpi) {
		this.facilityNpi = facilityNpi;
	}
	public String getMammoCert() {
		return mammoCert;
	}
	public void setMammoCert(String mammoCert) {
		this.mammoCert = mammoCert;
	}



}
