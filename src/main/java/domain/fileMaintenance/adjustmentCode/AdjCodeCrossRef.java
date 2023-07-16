package domain.fileMaintenance.adjustmentCode;

import java.sql.Date;

public class AdjCodeCrossRef {
	private Date effDt;
	private Date expDt;
	private int xrefId;
	private String crossRefDesc = "";
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
	public int getXrefId() {
		return xrefId;
	}
	public void setXrefId(int xrefId) {
		this.xrefId = xrefId;
	}
	public String getCrossRefDesc() {
		return crossRefDesc;
	}
	public void setCrossRefDesc(String crossRefDesc) {
		this.crossRefDesc = crossRefDesc;
	}
	@Override
	public String toString() {
		return "AdjCodeCrossRef [effDt=" + effDt + ", expDt=" + expDt + ", xrefId=" + xrefId + ", crossRefDesc="
				+ crossRefDesc + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crossRefDesc == null) ? 0 : crossRefDesc.hashCode());
		result = prime * result + ((effDt == null) ? 0 : effDt.hashCode());
		result = prime * result + ((expDt == null) ? 0 : expDt.hashCode());
		result = prime * result + xrefId;
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
		AdjCodeCrossRef other = (AdjCodeCrossRef) obj;
		if (crossRefDesc == null) {
			if (other.crossRefDesc != null)
				return false;
		} else if (!crossRefDesc.equals(other.crossRefDesc))
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
		if (xrefId != other.xrefId)
			return false;
		return true;
	}
}
