package domain.client.demographics;

public class NPISearchResults {
	private int Npi;
	private String NpiType = "";
	private String Name = "";

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

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result + Npi;
		result = prime * result + ((NpiType == null) ? 0 : NpiType.hashCode());
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
		NPISearchResults other = (NPISearchResults) obj;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		if (Npi != other.Npi)
			return false;
		if (NpiType == null) {
			if (other.NpiType != null)
				return false;
		} else if (!NpiType.equals(other.NpiType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NPISearchResults [Npi=" + Npi + ", NpiType=" + NpiType + ", Name=" + Name + "]";
	}

}
