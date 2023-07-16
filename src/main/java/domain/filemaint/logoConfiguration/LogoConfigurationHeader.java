package domain.filemaint.logoConfiguration;

import com.mbasys.mars.ejb.entity.fac.Fac;

import java.sql.Date;

public class LogoConfigurationHeader {
	private String documentType;
	private String currentLogoName;
	private Date currentLogoUploadDt;
	private String facilityAbbrev;
	private Fac fac;

	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	public void setFacilityAbbrev(String facilityAbbrev) {
		this.facilityAbbrev = facilityAbbrev;
	}
	
	public String getFacilityAbbrev() {
		return facilityAbbrev;
	}
	
	public String getCurrentLogoName() {
		return currentLogoName;
	}
	public void setCurrentLogoName(String currentLogoName) {
		this.currentLogoName = currentLogoName;
	}
	public Date getCurrentLogoUploadDt() {
		return currentLogoUploadDt;
	}
	public void setCurrentLogoUploadDt(Date currentLogoUploadDt) {
		this.currentLogoUploadDt = currentLogoUploadDt;
	}
	public void setFac(Fac fac) {
		this.fac = fac;
	}

	public Fac getFac() {
		return fac;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentLogoName == null) ? 0 : currentLogoName.hashCode());
		result = prime * result + ((currentLogoUploadDt == null) ? 0 : currentLogoUploadDt.hashCode());
		result = prime * result + ((documentType == null) ? 0 : documentType.hashCode());
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
		LogoConfigurationHeader other = (LogoConfigurationHeader) obj;
		if (currentLogoName == null) {
			if (other.currentLogoName != null)
				return false;
		} else if (!currentLogoName.equals(other.currentLogoName))
			return false;
		if (currentLogoUploadDt == null) {
			if (other.currentLogoUploadDt != null)
				return false;
		} else if (!currentLogoUploadDt.equals(other.currentLogoUploadDt))
			return false;
		if (documentType == null) {
			if (other.documentType != null)
				return false;
		} else if (!documentType.equals(other.documentType))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Header [documentType=" + documentType + ", currentLogoName=" + currentLogoName + ", currentLogoUploadDt=" + currentLogoUploadDt + "]";
	}
}
