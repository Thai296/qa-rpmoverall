package domain.client.physicianAssignment;

public class Header {
	private String clnAbbr;
	private String clnId;
	private String clnNm;
	private String clnAccountType;
	private String facAbbr;
	private String facNm;

	public String getClnAbbr() {
		return clnAbbr;
	}

	public void setClnAbbr(String clnAbbr) {
		this.clnAbbr = clnAbbr;
	}

	public String getClnId() {
		return clnId;
	}

	public void setClnId(String clnId) {
		this.clnId = clnId;
	}

	public String getClnNm() {
		return clnNm;
	}

	public void setClnNm(String clnNm) {
		this.clnNm = clnNm;
	}

	public String getClnAccountType() {
		return clnAccountType;
	}

	public void setClnAccountType(String clnAccountType) {
		this.clnAccountType = clnAccountType;
	}

	public String getFacAbbr() {
		return facAbbr;
	}

	public void setFacAbbr(String facAbbr) {
		this.facAbbr = facAbbr;
	}

	public String getFacNm() {
		return facNm;
	}

	public void setFacNm(String facNm) {
		this.facNm = facNm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clnAbbr == null) ? 0 : clnAbbr.hashCode());
		result = prime * result + ((clnAccountType == null) ? 0 : clnAccountType.hashCode());
		result = prime * result + ((clnId == null) ? 0 : clnId.hashCode());
		result = prime * result + ((clnNm == null) ? 0 : clnNm.hashCode());
		result = prime * result + ((facAbbr == null) ? 0 : facAbbr.hashCode());
		result = prime * result + ((facNm == null) ? 0 : facNm.hashCode());
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
		if (clnAbbr == null) {
			if (other.clnAbbr != null)
				return false;
		} else if (!clnAbbr.equals(other.clnAbbr))
			return false;
		if (clnAccountType == null) {
			if (other.clnAccountType != null)
				return false;
		} else if (!clnAccountType.equals(other.clnAccountType))
			return false;
		if (clnId == null) {
			if (other.clnId != null)
				return false;
		} else if (!clnId.equals(other.clnId))
			return false;
		if (clnNm == null) {
			if (other.clnNm != null)
				return false;
		} else if (!clnNm.equals(other.clnNm))
			return false;
		if (facAbbr == null) {
			if (other.facAbbr != null)
				return false;
		} else if (!facAbbr.equals(other.facAbbr))
			return false;
		if (facNm == null) {
			if (other.facNm != null)
				return false;
		} else if (!facNm.equals(other.facNm))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Header [clnAbbr=" + clnAbbr + ", clnId=" + clnId + ", clnNm=" + clnNm + ", clnAccountType="
				+ clnAccountType + ", facAbbr=" + facAbbr + ", facNm=" + facNm + "]";
	}

}
