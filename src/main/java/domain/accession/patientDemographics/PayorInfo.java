package domain.accession.patientDemographics;

public class PayorInfo {
    private int payorPriority;
    private String payorId = "";
    private String payorName = "";
    private String subscriberID = "";
    private String groupName = "";
    private String groupId = "";
    private String planId = "";
    private String caseId = "";

    public int getPayorPriority() {
        return payorPriority;
    }

    public void setPayorPriority(int payorPriority) {
        this.payorPriority = payorPriority;
    }

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

    public String getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((caseId == null) ? 0 : caseId.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + ((payorId == null) ? 0 : payorId.hashCode());
        result = prime * result + ((payorName == null) ? 0 : payorName.hashCode());
        result = prime * result + payorPriority;
        result = prime * result + ((planId == null) ? 0 : planId.hashCode());
        result = prime * result + ((subscriberID == null) ? 0 : subscriberID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PayorInfo))
            return false;
        PayorInfo other = (PayorInfo) obj;
        if (caseId == null) {
            if (other.caseId != null)
                return false;
        } else if (!caseId.equals(other.caseId))
            return false;
        if (groupId == null) {
            if (other.groupId != null)
                return false;
        } else if (!groupId.equals(other.groupId))
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
        if (payorPriority != other.payorPriority)
            return false;
        if (planId == null) {
            if (other.planId != null)
                return false;
        } else if (!planId.equals(other.planId))
            return false;
        if (subscriberID == null) {
            if (other.subscriberID != null)
                return false;
        } else if (!subscriberID.equals(other.subscriberID))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PayorInfo [payorPriority=" + payorPriority + ", payorId=" + payorId + ", payorName=" + payorName + ", subscriberID=" + subscriberID + ", groupName=" + groupName + ", groupId=" + groupId + ", planId=" + planId + ", caseId=" + caseId + "]";
    }

}
