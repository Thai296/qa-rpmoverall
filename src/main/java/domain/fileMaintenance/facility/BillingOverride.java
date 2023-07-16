package domain.fileMaintenance.facility;

public class BillingOverride {
	private String overrideId;
	private String npi;
	private String taxId;
	private String taxonomyCode;
	private String name;
	private String address;
	
	public String getOverrideId() {
		return overrideId;
	}
	public void setOverrideId(String overrideId) {
		this.overrideId = overrideId;
	}
	public String getNpi() {
		return npi;
	}
	public void setNpi(String npi) {
		this.npi = npi;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getTaxonomyCode() {
		return taxonomyCode;
	}
	public void setTaxonomyCode(String taxonomyCode) {
		this.taxonomyCode = taxonomyCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((npi == null) ? 0 : npi.hashCode());
		result = prime * result
				+ ((overrideId == null) ? 0 : overrideId.hashCode());
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
		BillingOverride other = (BillingOverride) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (npi == null) {
			if (other.npi != null)
				return false;
		} else if (!npi.equals(other.npi))
			return false;
		if (overrideId == null) {
			if (other.overrideId != null)
				return false;
		} else if (!overrideId.equals(other.overrideId))
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
		return "BillingOverride [overrideId=" + overrideId + ", npi=" + npi
				+ ", taxId=" + taxId + ", taxonomyCode=" + taxonomyCode
				+ ", name=" + name + ", address=" + address + "]";
	}
	
	

}
