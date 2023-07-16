package domain.accession.patientDemographics;

public class PayorTab {
    private PayorInfo payorInfo = new PayorInfo();
    private InsuredInfo insuredInfo = new InsuredInfo();
    private PayorNotes payorNotes = new PayorNotes();
    private EmployerInfo employerInfo = new EmployerInfo();

    public PayorInfo getPayorInfo() {
        return payorInfo;
    }

    public void setPayorInfo(PayorInfo payorInfo) {
        this.payorInfo = payorInfo;
    }

    public InsuredInfo getInsuredInfo() {
        return insuredInfo;
    }

    public void setInsuredInfo(InsuredInfo insuredInfo) {
        this.insuredInfo = insuredInfo;
    }

    public PayorNotes getPayorNotes() {
        return payorNotes;
    }

    public void setPayorNotes(PayorNotes payorNotes) {
        this.payorNotes = payorNotes;
    }

    public EmployerInfo getEmployerInfo() {
        return employerInfo;
    }

    public void setEmployerInfo(EmployerInfo employerInfo) {
        this.employerInfo = employerInfo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((employerInfo == null) ? 0 : employerInfo.hashCode());
        result = prime * result + ((insuredInfo == null) ? 0 : insuredInfo.hashCode());
        result = prime * result + ((payorInfo == null) ? 0 : payorInfo.hashCode());
        result = prime * result + ((payorNotes == null) ? 0 : payorNotes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PayorTab))
            return false;
        PayorTab other = (PayorTab) obj;
        if (employerInfo == null) {
            if (other.employerInfo != null)
                return false;
        } else if (!employerInfo.equals(other.employerInfo))
            return false;
        if (insuredInfo == null) {
            if (other.insuredInfo != null)
                return false;
        } else if (!insuredInfo.equals(other.insuredInfo))
            return false;
        if (payorInfo == null) {
            if (other.payorInfo != null)
                return false;
        } else if (!payorInfo.equals(other.payorInfo))
            return false;
        if (payorNotes == null) {
            if (other.payorNotes != null)
                return false;
        } else if (!payorNotes.equals(other.payorNotes))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PayorTab [payorInfo=" + payorInfo + ", insuredInfo=" + insuredInfo + ", payorNotes=" + payorNotes + ", employerInfo=" + employerInfo + "]";
    }

}
