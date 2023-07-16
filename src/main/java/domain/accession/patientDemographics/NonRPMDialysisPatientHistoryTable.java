package domain.accession.patientDemographics;

import java.sql.Date;

public class NonRPMDialysisPatientHistoryTable {
    private Date dos;
    private String compositeRoutine;
    private String description;
    private boolean isDeleted;

    public Date getDos() {
        return dos;
    }

    public void setDos(Date dos) {
        this.dos = dos;
    }

    public String getCompositeRoutine() {
        return compositeRoutine;
    }

    public void setCompositeRoutine(String compositeRoutine) {
        this.compositeRoutine = compositeRoutine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        result = prime * result + ((compositeRoutine == null) ? 0 : compositeRoutine.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((dos == null) ? 0 : dos.hashCode());
        result = prime * result + (isDeleted ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof NonRPMDialysisPatientHistoryTable))
            return false;
        NonRPMDialysisPatientHistoryTable other = (NonRPMDialysisPatientHistoryTable) obj;
        if (compositeRoutine == null) {
            if (other.compositeRoutine != null)
                return false;
        } else if (!compositeRoutine.equals(other.compositeRoutine))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (dos == null) {
            if (other.dos != null)
                return false;
        } else if (!dos.equals(other.dos))
            return false;
        if (isDeleted != other.isDeleted)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "NonRPMDialysisPatientHistoryTable [dos=" + dos + ", compositeRoutine=" + compositeRoutine + ", description=" + description + ", isDeleted=" + isDeleted + "]";
    }

}
