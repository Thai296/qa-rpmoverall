package domain.client.physicianAssignment;

public class PhysicianAssignmentTbl {
	private String npi;
	private String upin;
	private String lastNm;
	private String firstNm;
	private String postalCd;
	private String stateId;
	private String speacialty;
	private String credentials;
	private boolean isDeleted;

	public String getNpi() {
		return (npi==null || npi.equals(" ")) ?"":npi;
	}

	public void setNpi(String npi) {
		this.npi = npi;
	}

	public String getUpin() {
		return (upin==null || upin.equals(" ")) ?"":upin;
	}

	public void setUpin(String upin) {
		this.upin = (upin==null || upin.equals(" ")) ?"":upin;
	}

	public String getLastNm() {
		return (lastNm==null || lastNm.equals(" ")) ?"":lastNm;
	}

	public void setLastNm(String lastNm) {
		this.lastNm = (lastNm==null || lastNm.equals(" ")) ?"":lastNm;
	}

	public String getFirstNm() {
		return (firstNm==null || firstNm.equals(" ")) ?"":firstNm;
	}

	public void setFirstNm(String firstNm) {
		this.firstNm = firstNm;
	}

	public String getPostalCd() {
		return postalCd;
	}

	public void setPostalCd(String postalCd) {
		this.postalCd = (postalCd==null || postalCd.equals(" ")) ?"":postalCd;
	}

	public String getStateId() {
		return (stateId==null || stateId.equals(" ")) ?"":stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = (stateId==null || stateId.equals(" ")) ?"":stateId;
	}

	public String getSpeacialty() {
		return (speacialty==null || speacialty.equals(" ")) ?"":speacialty;
	}

	public void setSpeacialty(String speacialty) {
		this.speacialty = (speacialty==null || speacialty.equals(" ")) ?"":speacialty;
	}

	public String getCredentials() {
		return (credentials==null || credentials.equals(" ")) ?"":credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = (credentials==null || credentials.equals(" ")) ?"":credentials;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((credentials == null) ? 0 : credentials.hashCode());
		result = prime * result + ((firstNm == null) ? 0 : firstNm.hashCode());
		result = prime * result + (isDeleted ? 1231 : 1237);
		result = prime * result + ((lastNm == null) ? 0 : lastNm.hashCode());
		result = prime * result + ((npi == null) ? 0 : npi.hashCode());
		result = prime * result + ((postalCd == null) ? 0 : postalCd.hashCode());
		result = prime * result + ((speacialty == null) ? 0 : speacialty.hashCode());
		result = prime * result + ((stateId == null) ? 0 : stateId.hashCode());
		result = prime * result + ((upin == null) ? 0 : upin.hashCode());
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
		PhysicianAssignmentTbl other = (PhysicianAssignmentTbl) obj;
		if (credentials == null) {
			if (other.credentials != null)
				return false;
		} else if (!credentials.equals(other.credentials))
			return false;
		if (firstNm == null) {
			if (other.firstNm != null)
				return false;
		} else if (!firstNm.equals(other.firstNm))
			return false;
		if (isDeleted != other.isDeleted)
			return false;
		if (lastNm == null) {
			if (other.lastNm != null)
				return false;
		} else if (!lastNm.equals(other.lastNm))
			return false;
		if (npi == null) {
			if (other.npi != null)
				return false;
		} else if (!npi.equals(other.npi))
			return false;
		if (postalCd == null) {
			if (other.postalCd != null)
				return false;
		} else if (!postalCd.equals(other.postalCd))
			return false;
		if (speacialty == null) {
			if (other.speacialty != null)
				return false;
		} else if (!speacialty.equals(other.speacialty))
			return false;
		if (stateId == null) {
			if (other.stateId != null)
				return false;
		} else if (!stateId.equals(other.stateId))
			return false;
		if (upin == null) {
			if (other.upin != null)
				return false;
		} else if (!upin.equals(other.upin))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PhysicianAssignment [npi=" + npi + ", upin=" + upin + ", lastNm=" + lastNm + ", firstNm=" + firstNm
				+ ", postalCd=" + postalCd + ", stateId=" + stateId + ", speacialty=" + speacialty + ", credentials="
				+ credentials + ", isDeleted=" + isDeleted + "]";
	}

}
