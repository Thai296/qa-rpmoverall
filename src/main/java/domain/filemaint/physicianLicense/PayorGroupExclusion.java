package domain.filemaint.physicianLicense;

import java.sql.Date;

public class PayorGroupExclusion {
	private Date effDt;
	private Date expDt;
	private String groupName = "";
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Override
	public String toString() {
		return "PayorGroupExclusion [effDt=" + effDt + ", expDt=" + expDt + ", groupName=" + groupName + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((effDt == null) ? 0 : effDt.hashCode());
		result = prime * result + ((expDt == null) ? 0 : expDt.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
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
		PayorGroupExclusion other = (PayorGroupExclusion) obj;
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
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}
}
