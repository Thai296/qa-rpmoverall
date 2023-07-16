package domain.client.demographics;

public class GlobalNPISearchResults {
	private int Npi;
	private String NpiType = "";
	private String ProviderOrganizationName = "";
	private String ProviderName = "";

	public int getNpi() {
		return Npi;
	}

	public void setNpi(int npi) {
		Npi = npi;
	}

	public String getNpiType() {
		return NpiType;
	}

	public void setNpiType(String npiType) {
		NpiType = npiType;
	}

	public String getProviderOrganizationName() {
		return ProviderOrganizationName;
	}

	public void setProviderOrganizationName(String providerOrganizationName) {
		ProviderOrganizationName = providerOrganizationName;
	}

	public String getProviderName() {
		return ProviderName;
	}

	public void setProviderName(String providerName) {
		ProviderName = providerName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Npi;
		result = prime * result + ((NpiType == null) ? 0 : NpiType.hashCode());
		result = prime * result + ((ProviderName == null) ? 0 : ProviderName.hashCode());
		result = prime * result + ((ProviderOrganizationName == null) ? 0 : ProviderOrganizationName.hashCode());
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
		GlobalNPISearchResults other = (GlobalNPISearchResults) obj;
		if (Npi != other.Npi)
			return false;
		if (NpiType == null) {
			if (other.NpiType != null)
				return false;
		} else if (!NpiType.equals(other.NpiType))
			return false;
		if (ProviderName == null) {
			if (other.ProviderName != null)
				return false;
		} else if (!ProviderName.equals(other.ProviderName))
			return false;
		if (ProviderOrganizationName == null) {
			if (other.ProviderOrganizationName != null)
				return false;
		} else if (!ProviderOrganizationName.equals(other.ProviderOrganizationName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GlobalNPISearchResults [Npi=" + Npi + ", NpiType=" + NpiType + ", ProviderOrganizationName="
				+ ProviderOrganizationName + ", ProviderName=" + ProviderName + "]";
	}
}
