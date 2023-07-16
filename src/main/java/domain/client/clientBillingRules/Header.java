package domain.client.clientBillingRules;

public class Header {
	private String facility;
	private String clnId;
	private String clnName;
	private String accType;
	
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public String getClnId() {
		return clnId;
	}
	public void setClnId(String clnId) {
		this.clnId = clnId;
	}
	public String getClnName() {
		return clnName;
	}
	public void setClnName(String clnName) {
		this.clnName = clnName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accType == null) ? 0 : accType.hashCode());
		result = prime * result + ((clnId == null) ? 0 : clnId.hashCode());
		result = prime * result + ((clnName == null) ? 0 : clnName.hashCode());
		result = prime * result + ((facility == null) ? 0 : facility.hashCode());
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
		Header other = (Header) obj;
		if (accType == null) {
			if (other.accType != null)
				return false;
		} else if (!accType.equals(other.accType))
			return false;
		if (clnId == null) {
			if (other.clnId != null)
				return false;
		} else if (!clnId.equals(other.clnId))
			return false;
		if (clnName == null) {
			if (other.clnName != null)
				return false;
		} else if (!clnName.equals(other.clnName))
			return false;
		if (facility == null) {
			if (other.facility != null)
				return false;
		} else if (!facility.equals(other.facility))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Header [facility=" + facility + ", clnId=" + clnId + ", clnName=" + clnName + ", accType=" + accType
				+ "]";
	}
}
