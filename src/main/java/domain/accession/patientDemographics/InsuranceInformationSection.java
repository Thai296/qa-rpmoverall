package domain.accession.patientDemographics;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class InsuranceInformationSection {
    private Date effectiveDate;
    private String clnBillingCategory;
    private boolean isDeleteEffectiveDate;
    private boolean isSuspended;
    private List<SuspendedReasonTable> suspendedReasons = new ArrayList<>();
    private List<PayorTab> payorTabs = new ArrayList<>();

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getClnBillingCategory() {
        return clnBillingCategory;
    }

    public void setClnBillingCategory(String clnBillingCategory) {
        this.clnBillingCategory = clnBillingCategory;
    }

    public boolean isDeleteEffectiveDate() {
        return isDeleteEffectiveDate;
    }

    public void setDeleteEffectiveDate(boolean isDeleteEffectiveDate) {
        this.isDeleteEffectiveDate = isDeleteEffectiveDate;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    public List<SuspendedReasonTable> getSuspendedReasons() {
        return suspendedReasons;
    }

    public void setSuspendedReasons(List<SuspendedReasonTable> suspendedReasons) {
        this.suspendedReasons = suspendedReasons;
    }

    public List<PayorTab> getPayorTabs() {
        return payorTabs;
    }

    public void setPayorTabs(List<PayorTab> payorTabs) {
        this.payorTabs = payorTabs;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clnBillingCategory == null) ? 0 : clnBillingCategory.hashCode());
		result = prime * result + ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result + (isDeleteEffectiveDate ? 1231 : 1237);
		result = prime * result + (isSuspended ? 1231 : 1237);
		result = prime * result + ((payorTabs == null) ? 0 : payorTabs.hashCode());
		result = prime * result + ((suspendedReasons == null) ? 0 : suspendedReasons.hashCode());
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
		InsuranceInformationSection other = (InsuranceInformationSection) obj;
		if (clnBillingCategory == null) {
			if (other.clnBillingCategory != null)
				return false;
		} else if (!clnBillingCategory.equals(other.clnBillingCategory))
			return false;
		if (effectiveDate == null) {
			if (other.effectiveDate != null)
				return false;
		} else if (!effectiveDate.equals(other.effectiveDate))
			return false;
		if (isDeleteEffectiveDate != other.isDeleteEffectiveDate)
			return false;
		if (isSuspended != other.isSuspended)
			return false;
		if (payorTabs == null) {
			if (other.payorTabs != null)
				return false;
		} else if (!payorTabs.equals(other.payorTabs))
			return false;
		if (suspendedReasons == null) {
			if (other.suspendedReasons != null)
				return false;
		} else if (!suspendedReasons.equals(other.suspendedReasons))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "InsuranceInformationSection [effectiveDate=" + effectiveDate + ", clnBillingCategory=" + clnBillingCategory + ", isDeleteEffectiveDate=" + isDeleteEffectiveDate + ", isSuspended=" + isSuspended + ", suspendedReasons=" + suspendedReasons + ", payorTabs=" + payorTabs + "]";
    }

}