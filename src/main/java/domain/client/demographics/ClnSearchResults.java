package domain.client.demographics;

public class ClnSearchResults {
	private String clientId = "";
	private String clientName = "";
	private String address1 = "";
	private String address2 = "";
	private String city = "";
	private String state = "";
	private int postalCode;
	private String country = "";
	private String accountType = "";
	private String primaryFacility = "";
	private float balanceDue;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getPrimaryFacility() {
		return primaryFacility;
	}

	public void setPrimaryFacility(String primaryFacility) {
		this.primaryFacility = primaryFacility;
	}

	public float getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(float balanceDue) {
		this.balanceDue = balanceDue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result + ((address2 == null) ? 0 : address2.hashCode());
		result = prime * result + Float.floatToIntBits(balanceDue);
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((clientName == null) ? 0 : clientName.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + postalCode;
		result = prime * result + ((primaryFacility == null) ? 0 : primaryFacility.hashCode());
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
		ClnSearchResults other = (ClnSearchResults) obj;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
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
		if (Float.floatToIntBits(balanceDue) != Float.floatToIntBits(other.balanceDue))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (postalCode != other.postalCode)
			return false;
		if (primaryFacility == null) {
			if (other.primaryFacility != null)
				return false;
		} else if (!primaryFacility.equals(other.primaryFacility))
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
		return "ClnSearchResults [clientId=" + clientId + ", clientName=" + clientName + ", address1=" + address1
				+ ", address2=" + address2 + ", city=" + city + ", state=" + state + ", postalCode=" + postalCode
				+ ", country=" + country + ", accountType=" + accountType + ", primaryFacility=" + primaryFacility
				+ ", balanceDue=" + balanceDue + "]";
	}
}
