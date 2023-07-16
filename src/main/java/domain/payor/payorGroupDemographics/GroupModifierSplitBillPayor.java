package domain.payor.payorGroupDemographics;


public class GroupModifierSplitBillPayor {
	private String modifier;
	private String billPayorId;
	private boolean deleted = false;
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getBillPayorId() {
		return billPayorId;
	}
	public void setBillPayorId(String billPayorId) {
		this.billPayorId = billPayorId;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((billPayorId == null) ? 0 : billPayorId.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((modifier == null) ? 0 : modifier.hashCode());
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
		GroupModifierSplitBillPayor other = (GroupModifierSplitBillPayor) obj;
		if (billPayorId == null) {
			if (other.billPayorId != null)
				return false;
		} else if (!billPayorId.equals(other.billPayorId))
			return false;
		if (deleted != other.deleted)
			return false;
		if (modifier == null) {
			if (other.modifier != null)
				return false;
		} else if (!modifier.equals(other.modifier))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "GroupModifierSplitBillPayor [modifier=" + modifier + ", billPayorId=" + billPayorId + ", deleted="
				+ deleted + "]";
	}
	
	
}
