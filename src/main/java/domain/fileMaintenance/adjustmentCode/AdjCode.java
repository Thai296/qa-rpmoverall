package domain.fileMaintenance.adjustmentCode;

public class AdjCode {
	private String adjCode = "";
	private String adjCodeType = "";
	private String glCode = "";
	private String glCdId = "";
	private String desc = "";
	private String note = "";
	private boolean inActiveAdjCode;
	@Override
	public String toString() {
		return "AdjCode [adjCode=" + adjCode + ", adjCodeType=" + adjCodeType + ", glCode=" + glCode + ", glCdId="
				+ glCdId + ", desc=" + desc + ", note=" + note + ", inActiveAdjCode=" + inActiveAdjCode + "]";
	}
	public String getAdjCode() {
		return adjCode;
	}
	public void setAdjCode(String adjCode) {
		this.adjCode = adjCode;
	}
	public String getAdjCodeType() {
		return adjCodeType;
	}
	public void setAdjCodeType(String adjCodeType) {
		this.adjCodeType = adjCodeType;
	}
	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
	public String getGlCdId() {
		return glCdId;
	}
	public void setGlCdId(String glCdId) {
		this.glCdId = glCdId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean isInActiveAdjCode() {
		return inActiveAdjCode;
	}
	public void setInActiveAdjCode(boolean inActiveAdjCode) {
		this.inActiveAdjCode = inActiveAdjCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adjCode == null) ? 0 : adjCode.hashCode());
		result = prime * result + ((adjCodeType == null) ? 0 : adjCodeType.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((glCdId == null) ? 0 : glCdId.hashCode());
		result = prime * result + ((glCode == null) ? 0 : glCode.hashCode());
		result = prime * result + (inActiveAdjCode ? 1231 : 1237);
		result = prime * result + ((note == null) ? 0 : note.hashCode());
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
		AdjCode other = (AdjCode) obj;
		if (adjCode == null) {
			if (other.adjCode != null)
				return false;
		} else if (!adjCode.equals(other.adjCode))
			return false;
		if (adjCodeType == null) {
			if (other.adjCodeType != null)
				return false;
		} else if (!adjCodeType.equals(other.adjCodeType))
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (glCdId == null) {
			if (other.glCdId != null)
				return false;
		} else if (!glCdId.equals(other.glCdId))
			return false;
		if (glCode == null) {
			if (other.glCode != null)
				return false;
		} else if (!glCode.equals(other.glCode))
			return false;
		if (inActiveAdjCode != other.inActiveAdjCode)
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		return true;
	}
	
}
