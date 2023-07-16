package domain.payor.payorDemographics;

import java.sql.Date;

public class Header {
	private String payorId = "";
	private String payorName = "";
	private String groupName = "";
	private Date effectiveDate;
	private boolean workersComp;
	private boolean doNotRequireOrderEntry;
	private String signtureForCMS = "";
	private String website = "";
	private boolean exclFromCpSrchRslts;

	public String getPayorId() {
		return payorId;
	}

	public void setPayorId(String payorId) {
		this.payorId = payorId;
	}

	public String getPayorName() {
		return payorName;
	}

	public void setPayorName(String payorName) {
		this.payorName = payorName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public boolean isWorkersComp() {
		return workersComp;
	}

	public void setWorkersComp(boolean workersComp) {
		this.workersComp = workersComp;
	}

	public boolean isDoNotRequireOrderEntry() {
		return doNotRequireOrderEntry;
	}

	public void setDoNotRequireOrderEntry(boolean doNotRequireOrderEntry) {
		this.doNotRequireOrderEntry = doNotRequireOrderEntry;
	}

	public String getSigntureForCMS() {
		return signtureForCMS;
	}

	public void setSigntureForCMS(String signtureForCMS) {
		this.signtureForCMS = signtureForCMS;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public boolean isExclFromSrchRsltsInClnPortal() {
		return exclFromCpSrchRslts;
	}

	public void setExclFromSrchRsltsInClnPortal(boolean exclFromSrchRsltsInClnPortal) {
		this.exclFromCpSrchRslts = exclFromSrchRsltsInClnPortal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (doNotRequireOrderEntry ? 1231 : 1237);
		result = prime * result + ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result + (exclFromCpSrchRslts ? 1231 : 1237);
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((payorId == null) ? 0 : payorId.hashCode());
		result = prime * result + ((payorName == null) ? 0 : payorName.hashCode());
		result = prime * result + ((signtureForCMS == null) ? 0 : signtureForCMS.hashCode());
		result = prime * result + ((website == null) ? 0 : website.hashCode());
		result = prime * result + (workersComp ? 1231 : 1237);
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
		if (doNotRequireOrderEntry != other.doNotRequireOrderEntry)
			return false;
		if (effectiveDate == null) {
			if (other.effectiveDate != null)
				return false;
		} else if (!effectiveDate.equals(other.effectiveDate))
			return false;
		if (exclFromCpSrchRslts != other.exclFromCpSrchRslts)
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (payorId == null) {
			if (other.payorId != null)
				return false;
		} else if (!payorId.equals(other.payorId))
			return false;
		if (payorName == null) {
			if (other.payorName != null)
				return false;
		} else if (!payorName.equals(other.payorName))
			return false;
		if (signtureForCMS == null) {
			if (other.signtureForCMS != null)
				return false;
		} else if (!signtureForCMS.equals(other.signtureForCMS))
			return false;
		if (website == null) {
			if (other.website != null)
				return false;
		} else if (!website.equals(other.website))
			return false;
		if (workersComp != other.workersComp)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Header ["
				+ "payorId=" + payorId 
				+ ", payorName=" + payorName 
				+ ", groupName=" + groupName
				+ ", effectiveDate=" + effectiveDate 
				+ ", workersComp=" + workersComp 
				+ ", doNotRequireOrderEntry="+ doNotRequireOrderEntry 
				+ ", signtureForCMS=" + signtureForCMS 
				+ ", website=" + website
				+ ", exclFromSrchRsltsInClnPortal=" + exclFromCpSrchRslts 
				+ "]";
	}

}
