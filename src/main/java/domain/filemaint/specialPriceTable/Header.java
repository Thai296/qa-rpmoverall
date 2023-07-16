package domain.filemaint.specialPriceTable;

public class Header {
	private String specicalPriceTableId;
	private String accountType;
	private String displayOption;
	private String name;
	private String facAbbr;
	private int facId;
	private boolean discountable;
	
	public int getFacId() {
		return facId;
	}
	public void setFacId(int facId) {
		this.facId = facId;
	}
	public String getSpecicalPriceTableId() {
		return specicalPriceTableId;
	}
	public void setSpecicalPriceTableId(String specicalPriceTableId) {
		this.specicalPriceTableId = specicalPriceTableId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getDisplayOption() {
		return displayOption;
	}
	public void setDisplayOption(String displayOption) {
		this.displayOption = displayOption;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFacAbbr() {
		return facAbbr;
	}
	public void setFacAbbr(String facilityId) {
		this.facAbbr = facilityId;
	}
	public boolean isDiscountable() {
		return discountable;
	}
	public void setDiscountable(boolean discountable) {
		this.discountable = discountable;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + (discountable ? 1231 : 1237);
		result = prime * result + ((displayOption == null) ? 0 : displayOption.hashCode());
		result = prime * result + ((facAbbr == null) ? 0 : facAbbr.hashCode());
		result = prime * result + facId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((specicalPriceTableId == null) ? 0 : specicalPriceTableId.hashCode());
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
		Header other = (Header) obj;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (discountable != other.discountable)
			return false;
		if (displayOption == null) {
			if (other.displayOption != null)
				return false;
		} else if (!displayOption.equals(other.displayOption))
			return false;
		if (facAbbr == null) {
			if (other.facAbbr != null)
				return false;
		} else if (!facAbbr.equals(other.facAbbr))
			return false;
		if (facId != other.facId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (specicalPriceTableId == null) {
			if (other.specicalPriceTableId != null)
				return false;
		} else if (!specicalPriceTableId.equals(other.specicalPriceTableId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Header [specicalPriceTableId=" + specicalPriceTableId + ", accountType=" + accountType
				+ ", displayOption=" + displayOption + ", name=" + name + ", facAbbr=" + facAbbr + ", facId=" + facId
				+ ", discountable=" + discountable + "]";
	}
}
