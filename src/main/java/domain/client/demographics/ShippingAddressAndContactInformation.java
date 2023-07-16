package domain.client.demographics;

public class ShippingAddressAndContactInformation {
	
	private String contact1 = "";
	private boolean copyBillingAddress;
	private String address1 = "";
	private String address2 = "";
	private String country = "";
	private String postalCode = "";
	private String city = "";
	private String state = "";
	private String phone1 = "";
	private String fax1 = "";
	private String email1 = "";
	
	public String getContact1() {
		return contact1;
	}
	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}
	public boolean isCopyBillingAddress() {
		return copyBillingAddress;
	}
	public void setCopyBillingAddress(boolean copyBillingAddress) {
		this.copyBillingAddress = copyBillingAddress;
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
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getFax1() {
		return fax1;
	}
	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}
	public String getEmail1() {
		return email1;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result + ((address2 == null) ? 0 : address2.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((contact1 == null) ? 0 : contact1.hashCode());
		result = prime * result + (copyBillingAddress ? 1231 : 1237);
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((email1 == null) ? 0 : email1.hashCode());
		result = prime * result + ((fax1 == null) ? 0 : fax1.hashCode());
		result = prime * result + ((phone1 == null) ? 0 : phone1.hashCode());
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
		ShippingAddressAndContactInformation other = (ShippingAddressAndContactInformation) obj;
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
		if (contact1 == null) {
			if (other.contact1 != null)
				return false;
		} else if (!contact1.equals(other.contact1))
			return false;
		if (copyBillingAddress != other.copyBillingAddress)
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (email1 == null) {
			if (other.email1 != null)
				return false;
		} else if (!email1.equals(other.email1))
			return false;
		if (fax1 == null) {
			if (other.fax1 != null)
				return false;
		} else if (!fax1.equals(other.fax1))
			return false;
		if (phone1 == null) {
			if (other.phone1 != null)
				return false;
		} else if (!phone1.equals(other.phone1))
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
		return "ShippingAddressAndContactInformation ["
				+ "contact1=" + contact1
				+ ", copyBillingAddress="+ copyBillingAddress 
				+ ", address1=" + address1
				+ ", address2=" + address2 
				+ ", country=" + country
				+ ", postalCode=" + postalCode
				+ ", city=" + city 
				+ ", state=" + state 
				+ ", phone1=" + phone1
				+ ", fax1=" + fax1 
				+ ", email1=" + email1 
				+ "]";
	}

	
}
