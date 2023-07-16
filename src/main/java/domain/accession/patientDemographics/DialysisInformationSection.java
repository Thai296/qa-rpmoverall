package domain.accession.patientDemographics;

import java.sql.Date;

public class DialysisInformationSection {

    private String dialysisType="";
    private String medication="";
    private Date firstDateOfDialysis;

    public String getDialysisType() {
        return dialysisType;
    }

    public void setDialysisType(String dialysisType) {
        this.dialysisType = dialysisType;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public Date getFirstDateOfDialysis() {
        return firstDateOfDialysis;
    }

    public void setFirstDateOfDialysis(Date firstDateOfDialysis) {
        this.firstDateOfDialysis = firstDateOfDialysis;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dialysisType == null) ? 0 : dialysisType.hashCode());
        result = prime * result + ((firstDateOfDialysis == null) ? 0 : firstDateOfDialysis.hashCode());
        result = prime * result + ((medication == null) ? 0 : medication.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof DialysisInformationSection))
            return false;
        DialysisInformationSection other = (DialysisInformationSection) obj;
        if (dialysisType == null) {
            if (other.dialysisType != null)
                return false;
        } else if (!dialysisType.equals(other.dialysisType))
            return false;
        if (firstDateOfDialysis == null) {
            if (other.firstDateOfDialysis != null)
                return false;
        } else if (!firstDateOfDialysis.equals(other.firstDateOfDialysis))
            return false;
        if (medication == null) {
            if (other.medication != null)
                return false;
        } else if (!medication.equals(other.medication))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DialysisInformationSection [dialysisType=" + dialysisType + ", medication=" + medication + ", firstDateOfDialysis=" + firstDateOfDialysis + "]";
    }

}
