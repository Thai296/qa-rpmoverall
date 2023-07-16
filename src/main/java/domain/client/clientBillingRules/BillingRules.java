package domain.client.clientBillingRules;

import com.mbasys.mars.persistance.ErrorCodeMap;

public class BillingRules {
	private String clientIdCopied = "";
	
	private int seqId;
	private int priority;
	private String patientTyp;
	private String billPyrGrpIncl;
	private String billPyrGrpExcl;
	private String billPyrIncl;
	private String payorInclusions;
	private String billPyrExcl;
	private String payorExclusions;
	private String testTypeIncl;
	private String testTypeExcl;
	private String testIncl;
	private String testInclusions;
	private String testExcl;
	private String testExclusions;
	private String billClientAbbrv;
	private int billClientId;
	private String billPayorAbbrv;
	private int billPayorId;
	private boolean retainInsuredInfo;
	private String overrideDisplay;
	private String notes;
	private int resultCode = ErrorCodeMap.RECORD_FOUND;
	
	private boolean deleted;
	
	public String getClientIdCopied() {
		return clientIdCopied;
	}
	public void setClientIdCopied(String clientCopied) {
		this.clientIdCopied = clientCopied;
	}
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getPatientTyp() {
		return patientTyp;
	}
	public void setPatientTyp(String patientTyp) {
		this.patientTyp = patientTyp;
	}
	public String getBillPyrGrpIncl() {
		return billPyrGrpIncl;
	}
	public void setBillPyrGrpIncl(String billPyrGrpIncl) {
		this.billPyrGrpIncl = billPyrGrpIncl;
	}
	public String getBillPyrGrpExcl() {
		return billPyrGrpExcl;
	}
	public void setBillPyrGrpExcl(String billPyrGrpExcl) {
		this.billPyrGrpExcl = billPyrGrpExcl;
	}
	public String getBillPyrIncl() {
		return billPyrIncl;
	}
	public void setBillPyrIncl(String billPyrIncl) {
		this.billPyrIncl = billPyrIncl;
	}
	public String getPayorInclusions() {
		return payorInclusions;
	}
	public void setPayorInclusions(String payorInclusions) {
		this.payorInclusions = payorInclusions;
	}
	public String getBillPyrExcl() {
		return billPyrExcl;
	}
	public void setBillPyrExcl(String billPyrExcl) {
		this.billPyrExcl = billPyrExcl;
	}
	public String getPayorExclusions() {
		return payorExclusions;
	}
	public void setPayorExclusions(String payorExclusions) {
		this.payorExclusions = payorExclusions;
	}
	public String getTestTypeIncl() {
		return testTypeIncl;
	}
	public void setTestTypeIncl(String testTypeIncl) {
		this.testTypeIncl = testTypeIncl;
	}
	public String getTestTypeExcl() {
		return testTypeExcl;
	}
	public void setTestTypeExcl(String testTypeExcl) {
		this.testTypeExcl = testTypeExcl;
	}
	public String getTestIncl() {
		return testIncl;
	}
	public void setTestIncl(String testIncl) {
		this.testIncl = testIncl;
	}
	public String getTestInclusions() {
		return testInclusions;
	}
	public void setTestInclusions(String testInclusions) {
		this.testInclusions = testInclusions;
	}
	public String getTestExcl() {
		return testExcl;
	}
	public void setTestExcl(String testExcl) {
		this.testExcl = testExcl;
	}
	public String getTestExclusions() {
		return testExclusions;
	}
	public void setTestExclusions(String testExclusions) {
		this.testExclusions = testExclusions;
	}
	public String getBillClientAbbrv() {
		return billClientAbbrv;
	}
	public void setBillClientAbbrv(String billClientAbbrv) {
		this.billClientAbbrv = billClientAbbrv;
	}
	public int getBillClientId() {
		return billClientId;
	}
	public void setBillClientId(int billClientId) {
		this.billClientId = billClientId;
	}
	public String getBillPayorAbbrv() {
		return billPayorAbbrv;
	}
	public void setBillPayorAbbrv(String billPayorAbbrv) {
		this.billPayorAbbrv = billPayorAbbrv;
	}
	public int getBillPayorId() {
		return billPayorId;
	}
	public void setBillPayorId(int billPayorId) {
		this.billPayorId = billPayorId;
	}
	public boolean isRetainInsuredInfo() {
		return retainInsuredInfo;
	}
	public void setRetainInsuredInfo(boolean retainInsuredInfo) {
		this.retainInsuredInfo = retainInsuredInfo;
	}
	public String getOverrideDisplay() {
		return overrideDisplay;
	}
	public void setOverrideDisplay(String overrideDisplay) {
		this.overrideDisplay = overrideDisplay;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
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
		result = prime * result + ((billClientAbbrv == null) ? 0 : billClientAbbrv.hashCode());
		result = prime * result + billClientId;
		result = prime * result + ((billPayorAbbrv == null) ? 0 : billPayorAbbrv.hashCode());
		result = prime * result + billPayorId;
		result = prime * result + ((billPyrExcl == null) ? 0 : billPyrExcl.hashCode());
		result = prime * result + ((billPyrGrpExcl == null) ? 0 : billPyrGrpExcl.hashCode());
		result = prime * result + ((billPyrGrpIncl == null) ? 0 : billPyrGrpIncl.hashCode());
		result = prime * result + ((billPyrIncl == null) ? 0 : billPyrIncl.hashCode());
		result = prime * result + ((clientIdCopied == null) ? 0 : clientIdCopied.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((overrideDisplay == null) ? 0 : overrideDisplay.hashCode());
		result = prime * result + ((patientTyp == null) ? 0 : patientTyp.hashCode());
		result = prime * result + ((payorExclusions == null) ? 0 : payorExclusions.hashCode());
		result = prime * result + ((payorInclusions == null) ? 0 : payorInclusions.hashCode());
		result = prime * result + priority;
		result = prime * result + resultCode;
		result = prime * result + (retainInsuredInfo ? 1231 : 1237);
		result = prime * result + seqId;
		result = prime * result + ((testExcl == null) ? 0 : testExcl.hashCode());
		result = prime * result + ((testExclusions == null) ? 0 : testExclusions.hashCode());
		result = prime * result + ((testIncl == null) ? 0 : testIncl.hashCode());
		result = prime * result + ((testInclusions == null) ? 0 : testInclusions.hashCode());
		result = prime * result + ((testTypeExcl == null) ? 0 : testTypeExcl.hashCode());
		result = prime * result + ((testTypeIncl == null) ? 0 : testTypeIncl.hashCode());
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
		BillingRules other = (BillingRules) obj;
		if (billClientAbbrv == null) {
			if (other.billClientAbbrv != null)
				return false;
		} else if (!billClientAbbrv.equals(other.billClientAbbrv))
			return false;
		if (billClientId != other.billClientId)
			return false;
		if (billPayorAbbrv == null) {
			if (other.billPayorAbbrv != null)
				return false;
		} else if (!billPayorAbbrv.equals(other.billPayorAbbrv))
			return false;
		if (billPayorId != other.billPayorId)
			return false;
		if (billPyrExcl == null) {
			if (other.billPyrExcl != null)
				return false;
		} else if (!billPyrExcl.equals(other.billPyrExcl))
			return false;
		if (billPyrGrpExcl == null) {
			if (other.billPyrGrpExcl != null)
				return false;
		} else if (!billPyrGrpExcl.equals(other.billPyrGrpExcl))
			return false;
		if (billPyrGrpIncl == null) {
			if (other.billPyrGrpIncl != null)
				return false;
		} else if (!billPyrGrpIncl.equals(other.billPyrGrpIncl))
			return false;
		if (billPyrIncl == null) {
			if (other.billPyrIncl != null)
				return false;
		} else if (!billPyrIncl.equals(other.billPyrIncl))
			return false;
		if (clientIdCopied == null) {
			if (other.clientIdCopied != null)
				return false;
		} else if (!clientIdCopied.equals(other.clientIdCopied))
			return false;
		if (deleted != other.deleted)
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (overrideDisplay == null) {
			if (other.overrideDisplay != null)
				return false;
		} else if (!overrideDisplay.equals(other.overrideDisplay))
			return false;
		if (patientTyp == null) {
			if (other.patientTyp != null)
				return false;
		} else if (!patientTyp.equals(other.patientTyp))
			return false;
		if (payorExclusions == null) {
			if (other.payorExclusions != null)
				return false;
		} else if (!payorExclusions.equals(other.payorExclusions))
			return false;
		if (payorInclusions == null) {
			if (other.payorInclusions != null)
				return false;
		} else if (!payorInclusions.equals(other.payorInclusions))
			return false;
		if (priority != other.priority)
			return false;
		if (resultCode != other.resultCode)
			return false;
		if (retainInsuredInfo != other.retainInsuredInfo)
			return false;
		if (seqId != other.seqId)
			return false;
		if (testExcl == null) {
			if (other.testExcl != null)
				return false;
		} else if (!testExcl.equals(other.testExcl))
			return false;
		if (testExclusions == null) {
			if (other.testExclusions != null)
				return false;
		} else if (!testExclusions.equals(other.testExclusions))
			return false;
		if (testIncl == null) {
			if (other.testIncl != null)
				return false;
		} else if (!testIncl.equals(other.testIncl))
			return false;
		if (testInclusions == null) {
			if (other.testInclusions != null)
				return false;
		} else if (!testInclusions.equals(other.testInclusions))
			return false;
		if (testTypeExcl == null) {
			if (other.testTypeExcl != null)
				return false;
		} else if (!testTypeExcl.equals(other.testTypeExcl))
			return false;
		if (testTypeIncl == null) {
			if (other.testTypeIncl != null)
				return false;
		} else if (!testTypeIncl.equals(other.testTypeIncl))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BillingRule [clientCopied=" + clientIdCopied + ", seqId=" + seqId + ", priority=" + priority
				+ ", patientTyp=" + patientTyp + ", billPyrGrpIncl=" + billPyrGrpIncl + ", billPyrGrpExcl="
				+ billPyrGrpExcl + ", billPyrIncl=" + billPyrIncl + ", payorInclusions=" + payorInclusions
				+ ", billPyrExcl=" + billPyrExcl + ", payorExclusions=" + payorExclusions + ", testTypeIncl="
				+ testTypeIncl + ", testTypeExcl=" + testTypeExcl + ", testIncl=" + testIncl + ", testInclusions="
				+ testInclusions + ", testExcl=" + testExcl + ", testExclusions=" + testExclusions
				+ ", billClientAbbrv=" + billClientAbbrv + ", billClientId=" + billClientId + ", billPayorAbbrv="
				+ billPayorAbbrv + ", billPayorId=" + billPayorId + ", retainInsuredInfo=" + retainInsuredInfo
				+ ", overrideDisplay=" + overrideDisplay + ", notes=" + notes + ", resultCode=" + resultCode
				+ ", deleted=" + deleted + "]";
	}
}
