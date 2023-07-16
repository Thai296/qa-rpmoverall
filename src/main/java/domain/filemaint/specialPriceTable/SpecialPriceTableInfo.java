package domain.filemaint.specialPriceTable;

import java.sql.Date;

public class SpecialPriceTableInfo {
	private String name;
	private String facAbbr;
	private boolean isClient;
	private boolean isNonClient;
	private String importPrcTblId;
	private int percent;
	private Date effDtAsOf;
	private Date createEffDt;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFacAbbr() {
		return facAbbr;
	}
	public void setFacAbbr(String facAbbr) {
		this.facAbbr = facAbbr;
	}
	public boolean isClient() {
		return isClient;
	}
	public void setClient(boolean isClient) {
		this.isClient = isClient;
	}
	public boolean isNonClient() {
		return isNonClient;
	}
	public void setNonClient(boolean isNonClient) {
		this.isNonClient = isNonClient;
	}
	public String getImportPrcTblId() {
		return importPrcTblId;
	}
	public void setImportPrcTblId(String importPrcTblId) {
		this.importPrcTblId = importPrcTblId;
	}
	public int getPercent() {
		return percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
	public Date getEffDtAsOf() {
		return effDtAsOf;
	}
	public void setEffDtAsOf(Date effDtAsOf) {
		this.effDtAsOf = effDtAsOf;
	}
	public Date getCreateEffDt() {
		return createEffDt;
	}
	public void setCreateEffDt(Date createEffDt) {
		this.createEffDt = createEffDt;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createEffDt == null) ? 0 : createEffDt.hashCode());
		result = prime * result + ((effDtAsOf == null) ? 0 : effDtAsOf.hashCode());
		result = prime * result + ((facAbbr == null) ? 0 : facAbbr.hashCode());
		result = prime * result + ((importPrcTblId == null) ? 0 : importPrcTblId.hashCode());
		result = prime * result + (isClient ? 1231 : 1237);
		result = prime * result + (isNonClient ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + percent;
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
		SpecialPriceTableInfo other = (SpecialPriceTableInfo) obj;
		if (createEffDt == null) {
			if (other.createEffDt != null)
				return false;
		} else if (!createEffDt.equals(other.createEffDt))
			return false;
		if (effDtAsOf == null) {
			if (other.effDtAsOf != null)
				return false;
		} else if (!effDtAsOf.equals(other.effDtAsOf))
			return false;
		if (facAbbr == null) {
			if (other.facAbbr != null)
				return false;
		} else if (!facAbbr.equals(other.facAbbr))
			return false;
		if (importPrcTblId == null) {
			if (other.importPrcTblId != null)
				return false;
		} else if (!importPrcTblId.equals(other.importPrcTblId))
			return false;
		if (isClient != other.isClient)
			return false;
		if (isNonClient != other.isNonClient)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (percent != other.percent)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "SpecialPriceTableInfo [name=" + name + ", facAbbr=" + facAbbr + ", isClient=" + isClient
				+ ", isNonClient=" + isNonClient + ", importPrcTblId=" + importPrcTblId + ", percent=" + percent
				+ ", effDtAsOf=" + effDtAsOf + ", createEffDt=" + createEffDt + "]";
	}
}
