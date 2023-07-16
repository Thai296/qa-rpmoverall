package domain.payor.payorDemographics;

import java.sql.Date;

import com.mbasys.mars.persistance.ErrorCodeMap;

public class AccountDemographicsTable {
	private int seqId;
	private Date effDate;
	private Date expDate;
	private String crossRefDesc = "";
	private boolean deleted;
	private int resultCode = ErrorCodeMap.RECORD_FOUND;

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
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
		result = prime * result + ((effDate == null) ? 0 : effDate.hashCode());
		result = prime * result + ((expDate == null) ? 0 : expDate.hashCode());
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
		AccountDemographicsTable other = (AccountDemographicsTable) obj;
		if (crossRefDesc == null) {
			if (other.crossRefDesc != null)
				return false;
		} else if (!crossRefDesc.equals(other.crossRefDesc))
			return false;
		if (deleted != other.deleted)
			return false;
		if (effDate == null) {
			if (other.effDate != null)
				return false;
		} else if (!effDate.equals(other.effDate))
			return false;
		if (expDate == null) {
			if (other.expDate != null)
				return false;
		} else if (!expDate.equals(other.expDate))
			return false;
		if (resultCode != other.resultCode)
			return false;
		if (seqId != other.seqId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AccountDemographicsTable ["
				+ "seqId=" + seqId 
				+ ", effDate=" + effDate 
				+ ", expDate=" + expDate
				+ ", crossRefDesc=" + crossRefDesc 
				+ ", deleted=" + deleted 
				+ ", resultCode=" + resultCode 
				+ "]";
	}

}
