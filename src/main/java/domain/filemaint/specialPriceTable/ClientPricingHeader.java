package domain.filemaint.specialPriceTable;

public class ClientPricingHeader {
	private String priceTableID;
	private String name;
	public String getPriceTableID() {
		return priceTableID;
	}
	public void setPriceTableID(String priceTableID) {
		this.priceTableID = priceTableID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((priceTableID == null) ? 0 : priceTableID.hashCode());
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
		ClientPricingHeader other = (ClientPricingHeader) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (priceTableID == null) {
			if (other.priceTableID != null)
				return false;
		} else if (!priceTableID.equals(other.priceTableID))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ClientPricingHeader [priceTableID=" + priceTableID + ", name=" + name + "]";
	}
	
}
