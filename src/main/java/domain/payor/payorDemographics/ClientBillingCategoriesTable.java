package domain.payor.payorDemographics;

import com.mbasys.mars.persistance.ErrorCodeMap;

public class ClientBillingCategoriesTable {

	private int seqId;
	private String Id = "";
	private String Description = "";
	private boolean isDeleted;
	private int resultCode = ErrorCodeMap.RECORD_FOUND;

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
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
		result = prime * result + ((Description == null) ? 0 : Description.hashCode());
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		result = prime * result + (isDeleted ? 1231 : 1237);
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
		ClientBillingCategoriesTable other = (ClientBillingCategoriesTable) obj;
		if (Description == null) {
			if (other.Description != null)
				return false;
		} else if (!Description.equals(other.Description))
			return false;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		if (isDeleted != other.isDeleted)
			return false;
		if (resultCode != other.resultCode)
			return false;
		if (seqId != other.seqId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientBillingCategoriesTable ["
				+ "seqId=" + seqId 
				+ ", Id=" + Id 
				+ ", Description=" + Description
				+ ", isDeleted=" + isDeleted 
				+ ", resultCode=" + resultCode 
				+ "]";
	}

}
