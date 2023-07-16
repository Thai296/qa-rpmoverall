package domain.payor.payorDemographics;

public class AttachmentInformation {
	private String attchmentName = "";
	private String address1 = "";
	private String address2 = "";
	private String country = "";
	private String postalCode = "";
	private String city = "";
	private String state = "";
	private String fax = "";
	private String attchmentSubmissionId = "";

	public String getAttchmentName() {
		return attchmentName;
	}

	public void setAttchmentName(String attchmentName) {
		this.attchmentName = attchmentName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAttchmentSubmissionId() {
		return attchmentSubmissionId;
	}

	public void setAttchmentSubmissionId(String attchmentSubmissionId) {
		this.attchmentSubmissionId = attchmentSubmissionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result + ((address2 == null) ? 0 : address2.hashCode());
		result = prime * result + ((attchmentName == null) ? 0 : attchmentName.hashCode());
		result = prime * result + ((attchmentSubmissionId == null) ? 0 : attchmentSubmissionId.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
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
		AttachmentInformation other = (AttachmentInformation) obj;
		if (address1 == null) {
			if (other.address1 != null)
				return false;
		} else if (!address1.equals(other.address1))
			return false;
		if (address2 == null) {
			if (other.address2 != null)
				return false;
		} else if (!address2.equals(other.address2))
			return false;
		if (attchmentName == null) {
			if (other.attchmentName != null)
				return false;
		} else if (!attchmentName.equals(other.attchmentName))
			return false;
		if (attchmentSubmissionId == null) {
			if (other.attchmentSubmissionId != null)
				return false;
		} else if (!attchmentSubmissionId.equals(other.attchmentSubmissionId))
			return false;
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
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
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
		return "AttachmentInformation ["
				+ "attchmentName=" + attchmentName
				+ ", address1=" + address1
				+ ", address2="+ address2
				+ ", country=" + country
				+ ", postCode=" + postalCode
				+ ", city=" + city
				+ ", state=" + state
				+ ", fax=" + fax
				+ ", attchmentSubmissionId="
				+ attchmentSubmissionId
				+ "]";
	}

}
