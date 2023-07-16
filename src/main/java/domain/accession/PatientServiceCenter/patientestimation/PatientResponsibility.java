package domain.accession.PatientServiceCenter.patientestimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatientResponsibility
{
    private String estimationStatus;
    private String estimatedPatientResponsibility;
    private boolean isPatientResponsibilityAccepted;
    private String note;
    List<PatientResponsibilityDetail> details = new ArrayList<>();

    public String getEstimationStatus()
    {
        return estimationStatus;
    }

    public void setEstimationStatus(String estimationStatus)
    {
        this.estimationStatus = estimationStatus;
    }

    public String getEstimatedPatientResponsibility()
    {
        return estimatedPatientResponsibility;
    }

    public void setEstimatedPatientResponsibility(String estimatedPatientResponsibility)
    {
        this.estimatedPatientResponsibility = estimatedPatientResponsibility;
    }

    public boolean isPatientResponsibilityAccepted()
    {
        return isPatientResponsibilityAccepted;
    }

    public void setPatientResponsibilityAccepted(boolean patientResponsibilityAccepted)
    {
        isPatientResponsibilityAccepted = patientResponsibilityAccepted;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public List<PatientResponsibilityDetail> getDetails()
    {
        return details;
    }

    public void setDetails(List<PatientResponsibilityDetail> details)
    {
        this.details = details;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientResponsibility that = (PatientResponsibility) o;
        return isPatientResponsibilityAccepted == that.isPatientResponsibilityAccepted && Objects.equals(estimationStatus, that.estimationStatus) && Objects.equals(estimatedPatientResponsibility, that.estimatedPatientResponsibility) && Objects.equals(note, that.note) && Objects.equals(details, that.details);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(estimationStatus, estimatedPatientResponsibility, isPatientResponsibilityAccepted, note, details);
    }
}
