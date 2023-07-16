package domain.fileMaintenance.facility;

import java.util.HashMap;

public class JurisdictionPayor {	
	private String payor;
	private String jurisdictionPayor;
	
	public String getPayor() {
		return payor;
	}
	public void setPayor(String payor) {
		this.payor = payor;
	}
	public String getJurisdictionPayor() {
		return jurisdictionPayor;
	}
	public void setJurisdictionPayor(String jurisdictionPayor) {
		this.jurisdictionPayor = jurisdictionPayor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((jurisdictionPayor == null) ? 0 : jurisdictionPayor
						.hashCode());
		result = prime * result + ((payor == null) ? 0 : payor.hashCode());
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
		JurisdictionPayor other = (JurisdictionPayor) obj;
		if (jurisdictionPayor == null) {
			if (other.jurisdictionPayor != null)
				return false;
		} else if (!jurisdictionPayor.equals(other.jurisdictionPayor))
			return false;
		if (payor == null) {
			if (other.payor != null)
				return false;
		} else if (!payor.equals(other.payor))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "JurisdictionPayor [payor=" + payor + ", jurisdictionPayor="
				+ jurisdictionPayor + "]";
	}

}
