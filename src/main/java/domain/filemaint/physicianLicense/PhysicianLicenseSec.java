package domain.filemaint.physicianLicense;

public class PhysicianLicenseSec {
	private String fName = "";
	private String lName = "";
	private String address1 = "";
	private String address2 = "";
	private String postalCode = "";
	private String taxonomy = "";
	private String credential = "";
	private String country = "";
	private String city = "";
	private String overServFacLocation = "";
	private String facAddress = "";
	private long npi;
	private String taxId = "";
	private String specialty = "";
	private String stateId = "";
	private String facTaxId = "";
	private String provTypeDescr = "";
	private String classification = "";
	private String specialization = "";

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
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

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOverServFacLocation() {
		return overServFacLocation;
	}

	public void setOverServFacLocation(String overServFacLocation) {
		this.overServFacLocation = overServFacLocation;
	}

	public String getFacAddress() {
		return facAddress;
	}

	public void setFacAddress(String facAddress) {
		this.facAddress = facAddress;
	}

	public long getNpi() {
		return npi;
	}

	public void setNpi(long npi) {
		this.npi = npi;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getFacTaxId() {
		return facTaxId;
	}

	public void setFacTaxId(String facTaxId) {
		this.facTaxId = facTaxId;
	}

	public String getProvTypeDescr() {
		return provTypeDescr;
	}

	public void setProvTypeDescr(String provTypeDescr) {
		this.provTypeDescr = provTypeDescr;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result + ((address2 == null) ? 0 : address2.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((classification == null) ? 0 : classification.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((credential == null) ? 0 : credential.hashCode());
		result = prime * result + ((fName == null) ? 0 : fName.hashCode());
		result = prime * result + ((facAddress == null) ? 0 : facAddress.hashCode());
		result = prime * result + ((facTaxId == null) ? 0 : facTaxId.hashCode());
		result = prime * result + ((lName == null) ? 0 : lName.hashCode());
		result = prime * result + (int) (npi ^ (npi >>> 32));
		result = prime * result + ((overServFacLocation == null) ? 0 : overServFacLocation.hashCode());
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
		result = prime * result + ((provTypeDescr == null) ? 0 : provTypeDescr.hashCode());
		result = prime * result + ((specialization == null) ? 0 : specialization.hashCode());
		result = prime * result + ((specialty == null) ? 0 : specialty.hashCode());
		result = prime * result + ((stateId == null) ? 0 : stateId.hashCode());
		result = prime * result + ((taxId == null) ? 0 : taxId.hashCode());
		result = prime * result + ((taxonomy == null) ? 0 : taxonomy.hashCode());
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
		PhysicianLicenseSec other = (PhysicianLicenseSec) obj;
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
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (classification == null) {
			if (other.classification != null)
				return false;
		} else if (!classification.equals(other.classification))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (credential == null) {
			if (other.credential != null)
				return false;
		} else if (!credential.equals(other.credential))
			return false;
		if (fName == null) {
			if (other.fName != null)
				return false;
		} else if (!fName.equals(other.fName))
			return false;
		if (facAddress == null) {
			if (other.facAddress != null)
				return false;
		} else if (!facAddress.equals(other.facAddress))
			return false;
		if (facTaxId == null) {
			if (other.facTaxId != null)
				return false;
		} else if (!facTaxId.equals(other.facTaxId))
			return false;
		if (lName == null) {
			if (other.lName != null)
				return false;
		} else if (!lName.equals(other.lName))
			return false;
		if (npi != other.npi)
			return false;
		if (overServFacLocation == null) {
			if (other.overServFacLocation != null)
				return false;
		} else if (!overServFacLocation.equals(other.overServFacLocation))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		if (provTypeDescr == null) {
			if (other.provTypeDescr != null)
				return false;
		} else if (!provTypeDescr.equals(other.provTypeDescr))
			return false;
		if (specialization == null) {
			if (other.specialization != null)
				return false;
		} else if (!specialization.equals(other.specialization))
			return false;
		if (specialty == null) {
			if (other.specialty != null)
				return false;
		} else if (!specialty.equals(other.specialty))
			return false;
		if (stateId == null) {
			if (other.stateId != null)
				return false;
		} else if (!stateId.equals(other.stateId))
			return false;
		if (taxId == null) {
			if (other.taxId != null)
				return false;
		} else if (!taxId.equals(other.taxId))
			return false;
		if (taxonomy == null) {
			if (other.taxonomy != null)
				return false;
		} else if (!taxonomy.equals(other.taxonomy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PhysicianLicenseSec [fName=" + fName + ", lName=" + lName + ", address1=" + address1 + ", address2="
				+ address2 + ", postalCode=" + postalCode + ", taxonomy=" + taxonomy + ", credential=" + credential
				+ ", country=" + country + ", city=" + city + ", overServFacLocation=" + overServFacLocation
				+ ", facAddress=" + facAddress + ", npi=" + npi + ", taxId=" + taxId + ", specialty=" + specialty
				+ ", stateId=" + stateId + ", facTaxId=" + facTaxId + ", provTypeDescr=" + provTypeDescr
				+ ", classification=" + classification + ", specialization=" + specialization + "]";
	}
}
