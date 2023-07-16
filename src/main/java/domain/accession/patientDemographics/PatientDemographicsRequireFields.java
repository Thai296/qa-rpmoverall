package domain.accession.patientDemographics;

public class PatientDemographicsRequireFields {
	 private String epi;
	 private int patientSSN;
	 private String postalCode = "";
     private String city = "";
     private String state = "";
     private String country = "";
     private int payorPriority;
     private String payorId;
     private String payorName;
     private String groupName;
	
	public String getEpi() {
		return epi;
	}
	public void setEpi(String epi) {
		this.epi = epi;
	}
	public int getPatientSSN() {
		return patientSSN;
	}
	public void setPatientSSN(int patientSSN) {
		this.patientSSN = patientSSN;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getPayorPriority() {
		return payorPriority;
	}
	public void setPayorPriority(int payorPriority) {
		this.payorPriority = payorPriority;
	}
	public String getPayorId() {
		return payorId;
	}
	public void setPayorId(String payorId) {
		this.payorId = payorId;
	}
	public String getPayorName() {
		return payorName;
	}
	public void setPayorName(String payorName) {
		this.payorName = payorName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((epi == null) ? 0 : epi.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + patientSSN;
		result = prime * result + ((payorId == null) ? 0 : payorId.hashCode());
		result = prime * result + ((payorName == null) ? 0 : payorName.hashCode());
		result = prime * result + payorPriority;
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		PatientDemographicsRequireFields other = (PatientDemographicsRequireFields) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (epi == null) {
			if (other.epi != null)
				return false;
		} else if (!epi.equals(other.epi))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (patientSSN != other.patientSSN)
			return false;
		if (payorId == null) {
			if (other.payorId != null)
				return false;
		} else if (!payorId.equals(other.payorId))
			return false;
		if (payorName == null) {
			if (other.payorName != null)
				return false;
		} else if (!payorName.equals(other.payorName))
			return false;
		if (payorPriority != other.payorPriority)
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PatientDemographicsRequireFields [epi=" + epi + ", patientSSN=" + patientSSN + ", postalCode="
				+ postalCode + ", city=" + city + ", state=" + state + ", country=" + country + ", payorPriority="
				+ payorPriority + ", payorId=" + payorId + ", payorName=" + payorName + ", groupName=" + groupName
				+ "]";
	}
	 
	
}
