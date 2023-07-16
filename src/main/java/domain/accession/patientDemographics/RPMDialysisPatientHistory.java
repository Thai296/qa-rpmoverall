package domain.accession.patientDemographics;

import java.sql.Date;

public class RPMDialysisPatientHistory {
    private String accessionId;
    private Date dos;
    private String compositeRoutine;
    private String description;
    private String modifiers;

    public String getAccessionId() {
        return accessionId;
    }

    public void setAccessionId(String accessionId) {
        this.accessionId = accessionId;
    }

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

    public String getModifiers() {
        return modifiers;
    }

    public void setModifiers(String modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessionId == null) ? 0 : accessionId.hashCode());
        result = prime * result + ((compositeRoutine == null) ? 0 : compositeRoutine.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((dos == null) ? 0 : dos.hashCode());
        result = prime * result + ((modifiers == null) ? 0 : modifiers.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof RPMDialysisPatientHistory))
            return false;
        RPMDialysisPatientHistory other = (RPMDialysisPatientHistory) obj;
        if (accessionId == null) {
            if (other.accessionId != null)
                return false;
        } else if (!accessionId.equals(other.accessionId))
            return false;
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
        if (modifiers == null) {
            if (other.modifiers != null)
                return false;
        } else if (!modifiers.equals(other.modifiers))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RPMDialysisPatientHistory [accessionId=" + accessionId + ", dos=" + dos + ", compositeRoutine=" + compositeRoutine + ", description=" + description + ", modifiers=" + modifiers + "]";
    }

}
