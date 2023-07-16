package domain.client.demographics;

import java.sql.Date;

import com.mbasys.mars.persistance.ErrorCodeMap;

public class CrossReferencesTable {

	private int seqId;
	private Date effectiveDate;
	private Date expirationDate;
	private String crossRefDesc = "";
	private boolean deleted;
	private int resultCode = ErrorCodeMap.RECORD_FOUND;

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getCrossRefDesc() {
		return crossRefDesc;
	}

	public void setCrossRefDesc(String crossRefDesc) {
		this.crossRefDesc = crossRefDesc;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crossRefDesc == null) ? 0 : crossRefDesc.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + resultCode;
		result = prime * result + seqId;
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
		CrossReferencesTable other = (CrossReferencesTable) obj;
		if (crossRefDesc == null) {
			if (other.crossRefDesc != null)
				return false;
		} else if (!crossRefDesc.equals(other.crossRefDesc))
			return false;
		if (deleted != other.deleted)
			return false;
		if (effectiveDate == null) {
			if (other.effectiveDate != null)
				return false;
		} else if (!effectiveDate.equals(other.effectiveDate))
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (resultCode != other.resultCode)
			return false;
		if (seqId != other.seqId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CrossReferencesTable ["
				+ "seqId=" + seqId 
				+ ", effectiveDate=" + effectiveDate 
				+ ", expirationDate="+ expirationDate 
				+ ", crossRefDesc=" + crossRefDesc 
				+ ", deleted=" + deleted 
				+ ", resultCode="+ resultCode 
				+ "]";
	}

}
