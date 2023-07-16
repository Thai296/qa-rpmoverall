package domain.accession.patientDemographics;

public class AssociatedPatientIDsTable {
    private String patientId = "";
    private String sourceType = "";
    private String sourceId = "";
    private String sourceName = "";
    private String longTempDiagnosis = "";
    private String orderingPhysicianNPI = "";
    private String orderingPhysicianName = "";
    private boolean isDeleted;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getLongTempDiagnosis() {
        return longTempDiagnosis;
    }

    public void setLongTempDiagnosis(String longTempDiagnosis) {
        this.longTempDiagnosis = longTempDiagnosis;
    }

    public String getOrderingPhysicianNPI() {
        return orderingPhysicianNPI;
    }

    public void setOrderingPhysicianNPI(String orderingPhysicianNPI) {
        this.orderingPhysicianNPI = orderingPhysicianNPI;
    }

    public String getOrderingPhysicianName() {
        return orderingPhysicianName;
    }

    public void setOrderingPhysicianName(String orderingPhysicianName) {
        this.orderingPhysicianName = orderingPhysicianName;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isDeleted ? 1231 : 1237);
        result = prime * result + ((longTempDiagnosis == null) ? 0 : longTempDiagnosis.hashCode());
        result = prime * result + ((orderingPhysicianNPI == null) ? 0 : orderingPhysicianNPI.hashCode());
        result = prime * result + ((orderingPhysicianName == null) ? 0 : orderingPhysicianName.hashCode());
        result = prime * result + ((patientId == null) ? 0 : patientId.hashCode());
        result = prime * result + ((sourceId == null) ? 0 : sourceId.hashCode());;
        result = prime * result + ((sourceName == null) ? 0 : sourceName.hashCode());
        result = prime * result + ((sourceType == null) ? 0 : sourceType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AssociatedPatientIDsTable))
            return false;
        AssociatedPatientIDsTable other = (AssociatedPatientIDsTable) obj;
        if (isDeleted != other.isDeleted)
            return false;
        if (longTempDiagnosis == null) {
            if (other.longTempDiagnosis != null)
                return false;
        } else if (!longTempDiagnosis.equals(other.longTempDiagnosis))
            return false;
        if (orderingPhysicianNPI == null) {
            if (other.orderingPhysicianNPI != null)
                return false;
        } else if (!orderingPhysicianNPI.equals(other.orderingPhysicianNPI))
            return false;
        if (orderingPhysicianName == null) {
            if (other.orderingPhysicianName != null)
                return false;
        } else if (!orderingPhysicianName.equals(other.orderingPhysicianName))
            return false;
        if (patientId == null) {
            if (other.patientId != null)
                return false;
        } else if (!patientId.equals(other.patientId))
            return false;
        if (sourceId != other.sourceId)
            return false;
        if (sourceName == null) {
            if (other.sourceName != null)
                return false;
        } else if (!sourceName.equals(other.sourceName))
            return false;
        if (sourceType == null) {
            if (other.sourceType != null)
                return false;
        } else if (!sourceType.equals(other.sourceType))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AssociatedPatientIDsTable [patientId=" + patientId + ", sourceType=" + sourceType + ", sourceId=" + sourceId + ", sourceName=" + sourceName + ", longTempDiagnosis=" + longTempDiagnosis + ", orderingPhysicianNPI=" + orderingPhysicianNPI + ", orderingPhysicianName=" + orderingPhysicianName + ", isDeleted=" + isDeleted + "]";
    }

}
