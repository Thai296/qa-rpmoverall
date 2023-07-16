package domain.filemaint.physicianLicense;

import java.sql.Date;

public class PhysicianCrossReference {
	private Date effDt;
	private Date expDt;
	private String desc = "";
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((effDt == null) ? 0 : effDt.hashCode());
		result = prime * result + ((expDt == null) ? 0 : expDt.hashCode());
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
		PhysicianCrossReference other = (PhysicianCrossReference) obj;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
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
		return true;
	}
	@Override
	public String toString() {
		return "PhysicianCrossReference [effDt=" + effDt + ", expDt=" + expDt + ", desc=" + desc + "]";
	}
}
