package domain.payor.payorDemographics;

import com.mbasys.mars.persistance.ErrorCodeMap;

public class RemitPayorIDTable {
	private int seqId;
	private String remitPayorId = "";
	private boolean defaultPayor;
	private String bank = "";
	private boolean deleted;
	private int resultCode = ErrorCodeMap.RECORD_FOUND;

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public String getRemitPayorId() {
		return remitPayorId;
	}

	public void setRemitPayorId(String remitPayorId) {
		this.remitPayorId = remitPayorId;
	}

	public boolean isDefaultPayor() {
		return defaultPayor;
	}

	public void setDefaultPayor(boolean defaultPayor) {
		this.defaultPayor = defaultPayor;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.deleted = isDeleted;
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
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		result = prime * result + (defaultPayor ? 1231 : 1237);
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((remitPayorId == null) ? 0 : remitPayorId.hashCode());
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
		RemitPayorIDTable other = (RemitPayorIDTable) obj;
		if (bank == null) {
			if (other.bank != null)
				return false;
		} else if (!bank.equals(other.bank))
			return false;
		if (defaultPayor != other.defaultPayor)
			return false;
		if (deleted != other.deleted)
			return false;
		if (remitPayorId == null) {
			if (other.remitPayorId != null)
				return false;
		} else if (!remitPayorId.equals(other.remitPayorId))
			return false;
		if (resultCode != other.resultCode)
			return false;
		if (seqId != other.seqId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RemitPayorIDTable ["
				+ "seqId=" + seqId 
				+ ", remitPayorId=" + remitPayorId 
				+ ", defaultPayor=" + defaultPayor
				+ ", bank=" + bank 
				+ ", Deleted=" + deleted 
				+ ", resultCode=" + resultCode 
				+ "]";
	}

}
