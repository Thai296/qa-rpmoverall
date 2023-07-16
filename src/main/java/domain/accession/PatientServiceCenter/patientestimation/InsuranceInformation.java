package domain.accession.PatientServiceCenter.patientestimation;

import java.util.Objects;

public class InsuranceInformation
{
    private String payorId;
    private String payorName;
    private boolean includeAllPayors;
    private String subscriberId;
    private String relationship;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;

    public String getPayorId()
    {
        return payorId;
    }

    public void setPayorId(String payorId)
    {
        this.payorId = payorId;
    }

    public String getPayorName()
    {
        return payorName;
    }

    public void setPayorName(String payorName)
    {
        this.payorName = payorName;
    }

    public boolean isIncludeAllPayors()
    {
        return includeAllPayors;
    }

    public void setIncludeAllPayors(boolean includeAllPayors)
    {
        this.includeAllPayors = includeAllPayors;
    }

    public String getSubscriberId()
    {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId)
    {
        this.subscriberId = subscriberId;
    }

    public String getRelationship()
    {
        return relationship;
    }

    public void setRelationship(String relationship)
    {
        this.relationship = relationship;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getDob()
    {
        return dob;
    }

    public void setDob(String dob)
    {
        this.dob = dob;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InsuranceInformation that = (InsuranceInformation) o;

        if (includeAllPayors != that.includeAllPayors) return false;
        if (!Objects.equals(payorId, that.payorId)) return false;
        if (!Objects.equals(subscriberId, that.subscriberId)) return false;
        if (!Objects.equals(relationship, that.relationship)) return false;
        if (!Objects.equals(firstName, that.firstName)) return false;
        if (!Objects.equals(lastName, that.lastName)) return false;
        if (!Objects.equals(dob, that.dob)) return false;
        return Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode()
    {
        int result = payorId != null ? payorId.hashCode() : 0;
        result = 31 * result + (includeAllPayors ? 1 : 0);
        result = 31 * result + (subscriberId != null ? subscriberId.hashCode() : 0);
        result = 31 * result + (relationship != null ? relationship.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        return result;
    }
}
