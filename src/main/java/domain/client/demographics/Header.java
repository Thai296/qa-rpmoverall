package domain.client.demographics;

import java.sql.Date;

public class Header {

	private String clientId = "";
	private String clientName = "";
	private String accountType = "";
	private Date startDate;
	private String annualDisclosureLetter = "";
	private boolean doNotRequireOrderEntry;
	private String taxId = "";
	private int eav;
	private String primaryFacility = "";
	private int clientFacNPI;
	private String facAbbr = "";
	private String facNm = "";

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getAnnualDisclosureLetter() {
		return annualDisclosureLetter;
	}

	public void setAnnualDisclosureLetter(String annualDisclosureLetter) {
		this.annualDisclosureLetter = annualDisclosureLetter;
	}

	public boolean isDoNotRequireOrderEntry() {
		return doNotRequireOrderEntry;
	}

	public void setDoNotRequireOrderEntry(boolean doNotRequireOrderEntry) {
		this.doNotRequireOrderEntry = doNotRequireOrderEntry;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public int getEav() {
		return eav;
	}

	public void setEav(int eav) {
		this.eav = eav;
	}

	public String getPrimaryFacility() {
		return primaryFacility;
	}

	public void setPrimaryFacility(String primaryFacility) {
		this.primaryFacility = primaryFacility;
	}

	public int getClientFacNPI() {
		return clientFacNPI;
	}

	public void setClientFacNPI(int clientFacNPI) {
		this.clientFacNPI = clientFacNPI;
	}

	public String getFacAbbr() {
		return facAbbr;
	}

	public void setFacAbbr(String facAbbr) {
		this.facAbbr = facAbbr;
	}

	public String getFacNm() {
		return facNm;
	}

	public void setFacNm(String facNm) {
		this.facNm = facNm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((annualDisclosureLetter == null) ? 0 : annualDisclosureLetter.hashCode());
		result = prime * result + clientFacNPI;
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((clientName == null) ? 0 : clientName.hashCode());
		result = prime * result + (doNotRequireOrderEntry ? 1231 : 1237);
		result = prime * result + eav;
		result = prime * result + ((facAbbr == null) ? 0 : facAbbr.hashCode());
		result = prime * result + ((facNm == null) ? 0 : facNm.hashCode());
		result = prime * result + ((primaryFacility == null) ? 0 : primaryFacility.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((taxId == null) ? 0 : taxId.hashCode());
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
		if (annualDisclosureLetter == null) {
			if (other.annualDisclosureLetter != null)
				return false;
		} else if (!annualDisclosureLetter.equals(other.annualDisclosureLetter))
			return false;
		if (clientFacNPI != other.clientFacNPI)
			return false;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		if (doNotRequireOrderEntry != other.doNotRequireOrderEntry)
			return false;
		if (eav != other.eav)
			return false;
		if (facAbbr == null) {
			if (other.facAbbr != null)
				return false;
		} else if (!facAbbr.equals(other.facAbbr))
			return false;
		if (facNm == null) {
			if (other.facNm != null)
				return false;
		} else if (!facNm.equals(other.facNm))
			return false;
		if (primaryFacility == null) {
			if (other.primaryFacility != null)
				return false;
		} else if (!primaryFacility.equals(other.primaryFacility))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (taxId == null) {
			if (other.taxId != null)
				return false;
		} else if (!taxId.equals(other.taxId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Header ["
				+ "clientId=" + clientId
				+ ", clientName=" + clientName 
				+ ", accountType=" + accountType
				+ ", startDate=" + startDate
				+ ", annualDisclosureLetter=" + annualDisclosureLetter
				+ ", doNotRequireOrderEntry=" + doNotRequireOrderEntry
				+ ", taxId=" + taxId
				+ ", eav=" + eav
				+ ", primaryFacility=" + primaryFacility 
				+ ", clientFacNPI=" + clientFacNPI
				+ ", facAbbr=" + facAbbr
				+ ", facNm=" + facNm 
				+ "]";
	}

}
