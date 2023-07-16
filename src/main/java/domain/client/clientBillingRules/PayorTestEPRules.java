package domain.client.clientBillingRules;

import com.mbasys.mars.persistance.ErrorCodeMap;

public class PayorTestEPRules {
	private int seqId;
	private String testAbbrv="";
	private String testId="";
	private String testTypId="";
	private String onlyAllowTestId="";
	private String dontAllowTestId="";
	private String onlyAllowTestTypId="";
	private String dontAllowTestTypId="";
	private String testDisplay="";
	private String testTypeDisplay="";
	private String payorGroupAbbrv="";
	private String payorGroupId="";
	private String payorAbbrv="";
	private String payorId="";
	private String testPayorGroupExcl="";
	private String testPayorExcl="";
	private String payorExclusions="";
	private String payorGroupExclusions="";
	private boolean overrideDisplay;
	private int resultCode = ErrorCodeMap.RECORD_FOUND;

	private boolean deleted;

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public String getTestAbbrv() {
		return testAbbrv;
	}

	public void setTestAbbrv(String testAbbrv) {
		this.testAbbrv = testAbbrv;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getTestTypId() {
		return testTypId;
	}

	public void setTestTypId(String testTypId) {
		this.testTypId = testTypId;
	}

	public String getOnlyAllowTestId() {
		return onlyAllowTestId;
	}

	public void setOnlyAllowTestId(String onlyAllowTestId) {
		this.onlyAllowTestId = onlyAllowTestId;
	}

	public String getDontAllowTestId() {
		return dontAllowTestId;
	}

	public void setDontAllowTestId(String dontAllowTestId) {
		this.dontAllowTestId = dontAllowTestId;
	}

	public String getOnlyAllowTestTypId() {
		return onlyAllowTestTypId;
	}

	public void setOnlyAllowTestTypId(String onlyAllowTestTypId) {
		this.onlyAllowTestTypId = onlyAllowTestTypId;
	}

	public String getDontAllowTestTypId() {
		return dontAllowTestTypId;
	}

	public void setDontAllowTestTypId(String dontAllowTestTypId) {
		this.dontAllowTestTypId = dontAllowTestTypId;
	}

	public String getTestDisplay() {
		return testDisplay;
	}

	public void setTestDisplay(String testDisplay) {
		this.testDisplay = testDisplay;
	}

	public String getTestTypeDisplay() {
		return testTypeDisplay;
	}

	public void setTestTypeDisplay(String testTypeDisplay) {
		this.testTypeDisplay = testTypeDisplay;
	}

	public String getPayorGroupAbbrv() {
		return payorGroupAbbrv;
	}

	public void setPayorGroupAbbrv(String payorGroupAbbrv) {
		this.payorGroupAbbrv = payorGroupAbbrv;
	}

	public String getPayorGroupId() {
		return payorGroupId;
	}

	public void setPayorGroupId(String payorGroupId) {
		this.payorGroupId = payorGroupId;
	}

	public String getPayorAbbrv() {
		return payorAbbrv;
	}

	public void setPayorAbbrv(String payorAbbrv) {
		this.payorAbbrv = payorAbbrv;
	}

	public String getPayorId() {
		return payorId;
	}

	public void setPayorId(String payorId) {
		this.payorId = payorId;
	}

	public String getTestPayorGroupExcl() {
		return testPayorGroupExcl;
	}

	public void setTestPayorGroupExcl(String testPayorGroupExcl) {
		this.testPayorGroupExcl = testPayorGroupExcl;
	}

	public String getTestPayorExcl() {
		return testPayorExcl;
	}

	public void setTestPayorExcl(String testPayorExcl) {
		this.testPayorExcl = testPayorExcl;
	}

	public String getPayorExclusions() {
		return payorExclusions;
	}

	public void setPayorExclusions(String payorExclusions) {
		this.payorExclusions = payorExclusions;
	}

	public String getPayorGroupExclusions() {
		return payorGroupExclusions;
	}

	public void setPayorGroupExclusions(String payorGroupExclusions) {
		this.payorGroupExclusions = payorGroupExclusions;
	}

	public boolean isOverrideDisplay() {
		return overrideDisplay;
	}

	public void setOverrideDisplay(boolean overrideDisplay) {
		this.overrideDisplay = overrideDisplay;
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
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((dontAllowTestId == null) ? 0 : dontAllowTestId.hashCode());
		result = prime * result + ((dontAllowTestTypId == null) ? 0 : dontAllowTestTypId.hashCode());
		result = prime * result + ((onlyAllowTestId == null) ? 0 : onlyAllowTestId.hashCode());
		result = prime * result + ((onlyAllowTestTypId == null) ? 0 : onlyAllowTestTypId.hashCode());
		result = prime * result + (overrideDisplay ? 1231 : 1237);
		result = prime * result + ((payorAbbrv == null) ? 0 : payorAbbrv.hashCode());
		result = prime * result + ((payorExclusions == null) ? 0 : payorExclusions.hashCode());
		result = prime * result + ((payorGroupAbbrv == null) ? 0 : payorGroupAbbrv.hashCode());
		result = prime * result + ((payorGroupExclusions == null) ? 0 : payorGroupExclusions.hashCode());
		result = prime * result + ((payorGroupId == null) ? 0 : payorGroupId.hashCode());
		result = prime * result + ((payorId == null) ? 0 : payorId.hashCode());
		result = prime * result + resultCode;
		result = prime * result + seqId;
		result = prime * result + ((testAbbrv == null) ? 0 : testAbbrv.hashCode());
		result = prime * result + ((testDisplay == null) ? 0 : testDisplay.hashCode());
		result = prime * result + ((testId == null) ? 0 : testId.hashCode());
		result = prime * result + ((testPayorExcl == null) ? 0 : testPayorExcl.hashCode());
		result = prime * result + ((testPayorGroupExcl == null) ? 0 : testPayorGroupExcl.hashCode());
		result = prime * result + ((testTypId == null) ? 0 : testTypId.hashCode());
		result = prime * result + ((testTypeDisplay == null) ? 0 : testTypeDisplay.hashCode());
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
		PayorTestEPRules other = (PayorTestEPRules) obj;
		if (deleted != other.deleted)
			return false;
		if (dontAllowTestId == null) {
			if (other.dontAllowTestId != null)
				return false;
		} else if (!dontAllowTestId.equals(other.dontAllowTestId))
			return false;
		if (dontAllowTestTypId == null) {
			if (other.dontAllowTestTypId != null)
				return false;
		} else if (!dontAllowTestTypId.equals(other.dontAllowTestTypId))
			return false;
		if (onlyAllowTestId == null) {
			if (other.onlyAllowTestId != null)
				return false;
		} else if (!onlyAllowTestId.equals(other.onlyAllowTestId))
			return false;
		if (onlyAllowTestTypId == null) {
			if (other.onlyAllowTestTypId != null)
				return false;
		} else if (!onlyAllowTestTypId.equals(other.onlyAllowTestTypId))
			return false;
		if (overrideDisplay != other.overrideDisplay)
			return false;
		if (payorAbbrv == null) {
			if (other.payorAbbrv != null)
				return false;
		} else if (!payorAbbrv.equals(other.payorAbbrv))
			return false;
		if (payorExclusions == null) {
			if (other.payorExclusions != null)
				return false;
		} else if (!payorExclusions.equals(other.payorExclusions))
			return false;
		if (payorGroupAbbrv == null) {
			if (other.payorGroupAbbrv != null)
				return false;
		} else if (!payorGroupAbbrv.equals(other.payorGroupAbbrv))
			return false;
		if (payorGroupExclusions == null) {
			if (other.payorGroupExclusions != null)
				return false;
		} else if (!payorGroupExclusions.equals(other.payorGroupExclusions))
			return false;
		if (payorGroupId == null) {
			if (other.payorGroupId != null)
				return false;
		} else if (!payorGroupId.equals(other.payorGroupId))
			return false;
		if (payorId == null) {
			if (other.payorId != null)
				return false;
		} else if (!payorId.equals(other.payorId))
			return false;
		if (resultCode != other.resultCode)
			return false;
		if (seqId != other.seqId)
			return false;
		if (testAbbrv == null) {
			if (other.testAbbrv != null)
				return false;
		} else if (!testAbbrv.equals(other.testAbbrv))
			return false;
		if (testDisplay == null) {
			if (other.testDisplay != null)
				return false;
		} else if (!testDisplay.equals(other.testDisplay))
			return false;
		if (testId == null) {
			if (other.testId != null)
				return false;
		} else if (!testId.equals(other.testId))
			return false;
		if (testPayorExcl == null) {
			if (other.testPayorExcl != null)
				return false;
		} else if (!testPayorExcl.equals(other.testPayorExcl))
			return false;
		if (testPayorGroupExcl == null) {
			if (other.testPayorGroupExcl != null)
				return false;
		} else if (!testPayorGroupExcl.equals(other.testPayorGroupExcl))
			return false;
		if (testTypId == null) {
			if (other.testTypId != null)
				return false;
		} else if (!testTypId.equals(other.testTypId))
			return false;
		if (testTypeDisplay == null) {
			if (other.testTypeDisplay != null)
				return false;
		} else if (!testTypeDisplay.equals(other.testTypeDisplay))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PayorTestEPRules [seqId=" + seqId + ", testAbbrv=" + testAbbrv + ", testId=" + testId + ", testTypId="
				+ testTypId + ", onlyAllowTestId=" + onlyAllowTestId + ", dontAllowTestId=" + dontAllowTestId
				+ ", onlyAllowTestTypId=" + onlyAllowTestTypId + ", dontAllowTestTypId=" + dontAllowTestTypId
				+ ", testDisplay=" + testDisplay + ", testTypeDisplay=" + testTypeDisplay + ", payorGroupAbbrv="
				+ payorGroupAbbrv + ", payorGroupId=" + payorGroupId + ", payorAbbrv=" + payorAbbrv + ", payorId="
				+ payorId + ", testPayorGroupExcl=" + testPayorGroupExcl + ", testPayorExcl=" + testPayorExcl
				+ ", payorExclusions=" + payorExclusions + ", payorGroupExclusions=" + payorGroupExclusions
				+ ", overrideDisplay=" + overrideDisplay + ", resultCode=" + resultCode + ", deleted=" + deleted + "]";
	}
}
