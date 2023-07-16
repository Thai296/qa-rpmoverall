package domain.filemaint.physicianLicense;

import java.sql.Date;

public class PayorExclusion {
	private Date effDt;
	private Date expDt;
	private int pyrId;
	private String pyrAbbrv = "";
	private String pyrName = "";
	public Date getEffDt() {
		return effDt;
	}
	public void setEffDt(Date effDt) {
		this.effDt = effDt;
	}
	public Date getExpDt() {
		return expDt;
	}
	public void setExpDt(Date expDt) {
		this.expDt = expDt;
	}
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((effDt == null) ? 0 : effDt.hashCode());
		result = prime * result + ((expDt == null) ? 0 : expDt.hashCode());
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
		PayorExclusion other = (PayorExclusion) obj;
		if (effDt == null) {
			if (other.effDt != null)
				return false;
		} else if (!effDt.equals(other.effDt))
			return false;
		if (expDt == null) {
			if (other.expDt != null)
				return false;
		} else if (!expDt.equals(other.expDt))
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
		return "PayorExclusion [effDt=" + effDt + ", expDt=" + expDt + ", pyrId=" + pyrId + ", pyrAbbrv=" + pyrAbbrv
				+ ", pyrName=" + pyrName + "]";
	}
}
