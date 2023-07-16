package domain.filemaint.physicianLicense;

public class AffiliatedLicense {
	private int pyrId;
	private String pyrAbbrv = "";
	private String pyrName = "";
	private int licTypId;
	private String licTypName = "";
	private String licId = "";
	public int getPyrId() {
		return pyrId;
	}
	public void setPyrId(int pyrId) {
		this.pyrId = pyrId;
	}
	public String getPyrAbbrv() {
		return pyrAbbrv;
	}
	public void setPyrAbbrv(String pyrAbbrv) {
		this.pyrAbbrv = pyrAbbrv;
	}
	public String getPyrName() {
		return pyrName;
	}
	public void setPyrName(String pyrName) {
		this.pyrName = pyrName;
	}
	public int getLicTypId() {
		return licTypId;
	}
	public void setLicTypId(int licTypId) {
		this.licTypId = licTypId;
	}
	public String getLicTypName() {
		return licTypName;
	}
	public void setLicTypName(String licTypName) {
		this.licTypName = licTypName;
	}
	public String getLicId() {
		return licId;
	}
	public void setLicId(String licId) {
		this.licId = licId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((licId == null) ? 0 : licId.hashCode());
		result = prime * result + licTypId;
		result = prime * result + ((licTypName == null) ? 0 : licTypName.hashCode());
		result = prime * result + ((pyrAbbrv == null) ? 0 : pyrAbbrv.hashCode());
		result = prime * result + pyrId;
		result = prime * result + ((pyrName == null) ? 0 : pyrName.hashCode());
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
		AffiliatedLicense other = (AffiliatedLicense) obj;
		if (licId == null) {
			if (other.licId != null)
				return false;
		} else if (!licId.equals(other.licId))
			return false;
		if (licTypId != other.licTypId)
			return false;
		if (licTypName == null) {
			if (other.licTypName != null)
				return false;
		} else if (!licTypName.equals(other.licTypName))
			return false;
		if (pyrAbbrv == null) {
			if (other.pyrAbbrv != null)
				return false;
		} else if (!pyrAbbrv.equals(other.pyrAbbrv))
			return false;
		if (pyrId != other.pyrId)
			return false;
		if (pyrName == null) {
			if (other.pyrName != null)
				return false;
		} else if (!pyrName.equals(other.pyrName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AffiliatedLicense [pyrId=" + pyrId + ", pyrAbbrv=" + pyrAbbrv + ", pyrName=" + pyrName + ", licTypId="
				+ licTypId + ", licTypName=" + licTypName + ", licId=" + licId + "]";
	}
}
