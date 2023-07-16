package domain.payor.payorGroupDemographics;

public class ProcedureCodeOverride {
	private String procedureCode;
	private String submissionService;
	private boolean deleted = false;
	
	public String getProcedureCode() {
		return procedureCode;
	}
	public void setProcedureCode(String procedureCode) {
		this.procedureCode = procedureCode;
	}
	public String getSubmissionService() {
		return submissionService;
	}
	public void setSubmissionService(String submissionService) {
		this.submissionService = submissionService;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Override
	public String toString() {
		return "ProcedureCodeOverride [procedureCode=" + procedureCode + ", submissionService=" + submissionService
				+ ", deleted=" + deleted + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((procedureCode == null) ? 0 : procedureCode.hashCode());
		result = prime * result + ((submissionService == null) ? 0 : submissionService.hashCode());
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
		ProcedureCodeOverride other = (ProcedureCodeOverride) obj;
		if (deleted != other.deleted)
			return false;
		if (procedureCode == null) {
			if (other.procedureCode != null)
				return false;
		} else if (!procedureCode.equals(other.procedureCode))
			return false;
		if (submissionService == null) {
			if (other.submissionService != null)
				return false;
		} else if (!submissionService.equals(other.submissionService))
			return false;
		return true;
	}
}
