package domain.payor.payorGroupDemographics;

import com.mbasys.mars.persistance.ErrorCodeMap;

public class DunningCycle {
	private int cycle;
	private int days;
	private String priServiceId;
	private String nonPriServiceId;
	private String message;
	private boolean delete;
	private int resultCode = ErrorCodeMap.RECORD_FOUND;
	private int submSvcPrimary;
	private int submSvcNonPrimary;
	private int docId;
	
	public int getSubmSvcPrimary() {
		return submSvcPrimary;
	}
	public void setSubmSvcPrimary(int submSvcPrimary) {
		this.submSvcPrimary = submSvcPrimary;
	}
	public int getSubmSvcNonPrimary() {
		return submSvcNonPrimary;
	}
	public void setSubmSvcNonPrimary(int submSvcNonPrimary) {
		this.submSvcNonPrimary = submSvcNonPrimary;
	}
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public String getPriServiceId() {
		return priServiceId;
	}
	public void setPriServiceId(String priServiceId) {
		this.priServiceId = priServiceId;
	}
	public String getNonPriServiceId() {
		return nonPriServiceId;
	}
	public void setNonPriServiceId(String nonPriServiceId) {
		this.nonPriServiceId = nonPriServiceId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
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
		result = prime * result + cycle;
		result = prime * result + days;
		result = prime * result + (delete ? 1231 : 1237);
		result = prime * result + docId;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((nonPriServiceId == null) ? 0 : nonPriServiceId.hashCode());
		result = prime * result + ((priServiceId == null) ? 0 : priServiceId.hashCode());
		result = prime * result + resultCode;
		result = prime * result + submSvcNonPrimary;
		result = prime * result + submSvcPrimary;
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
		DunningCycle other = (DunningCycle) obj;
		if (cycle != other.cycle)
			return false;
		if (days != other.days)
			return false;
		if (delete != other.delete)
			return false;
		if (docId != other.docId)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (nonPriServiceId == null) {
			if (other.nonPriServiceId != null)
				return false;
		} else if (!nonPriServiceId.equals(other.nonPriServiceId))
			return false;
		if (priServiceId == null) {
			if (other.priServiceId != null)
				return false;
		} else if (!priServiceId.equals(other.priServiceId))
			return false;
		if (resultCode != other.resultCode)
			return false;
		if (submSvcNonPrimary != other.submSvcNonPrimary)
			return false;
		if (submSvcPrimary != other.submSvcPrimary)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "DunningCycle [cycle=" + cycle + ", days=" + days + ", priServiceId=" + priServiceId
				+ ", nonPriServiceId=" + nonPriServiceId + ", message=" + message + ", delete=" + delete
				+ ", resultCode=" + resultCode + ", submSvcPrimary=" + submSvcPrimary + ", submSvcNonPrimary="
				+ submSvcNonPrimary + ", docId=" + docId + "]";
	}
}
