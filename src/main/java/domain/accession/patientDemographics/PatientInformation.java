package domain.accession.patientDemographics;

import java.sql.Date;

public class PatientInformation {

    private String firstName = "";
    private String lastName = "";
    private String maritalStatus = "";
    private Date dateOfBirth;
    private String gender = "";
    private String notes = "";
    private Date dosOfMostMSPFrom;
    private boolean isCreateBadAddress;
    private String address1 = "";
    private String address2 = "";
    private String postalCode = "";
    private String city = "";
    private String state = "";
    private String country = "";
    private String email = "";
    private String homePhone = "";
    private String workPhone = "";

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getDosOfMostMSPFrom() {
        return dosOfMostMSPFrom;
    }

    public void setDosOfMostMSPFrom(Date dosOfMostMSPFrom) {
        this.dosOfMostMSPFrom = dosOfMostMSPFrom;
    }

    public boolean isCreateBadAddress() {
        return isCreateBadAddress;
    }

    public void setCreateBadAddress(boolean isCreateBadAddress) {
        this.isCreateBadAddress = isCreateBadAddress;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
        result = prime * result + ((address2 == null) ? 0 : address2.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
        result = prime * result + ((dosOfMostMSPFrom == null) ? 0 : dosOfMostMSPFrom.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((homePhone == null) ? 0 : homePhone.hashCode());
        result = prime * result + (isCreateBadAddress ? 1231 : 1237);
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((maritalStatus == null) ? 0 : maritalStatus.hashCode());
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((workPhone == null) ? 0 : workPhone.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PatientInformation))
            return false;
        PatientInformation other = (PatientInformation) obj;
        if (address1 == null) {
            if (other.address1 != null)
                return false;
        } else if (!address1.equals(other.address1))
            return false;
        if (address2 == null) {
            if (other.address2 != null)
                return false;
        } else if (!address2.equals(other.address2))
            return false;
        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (dateOfBirth == null) {
            if (other.dateOfBirth != null)
                return false;
        } else if (!dateOfBirth.equals(other.dateOfBirth))
            return false;
        if (dosOfMostMSPFrom == null) {
            if (other.dosOfMostMSPFrom != null)
                return false;
        } else if (!dosOfMostMSPFrom.equals(other.dosOfMostMSPFrom))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (gender == null) {
            if (other.gender != null)
                return false;
        } else if (!gender.equals(other.gender))
            return false;
        if (homePhone == null) {
            if (other.homePhone != null)
                return false;
        } else if (!homePhone.equals(other.homePhone))
            return false;
        if (isCreateBadAddress != other.isCreateBadAddress)
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (maritalStatus == null) {
            if (other.maritalStatus != null)
                return false;
        } else if (!maritalStatus.equals(other.maritalStatus))
            return false;
        if (notes == null) {
            if (other.notes != null)
                return false;
        } else if (!notes.equals(other.notes))
            return false;
        if (postalCode == null) {
            if (other.postalCode != null)
                return false;
        } else if (!postalCode.equals(other.postalCode))
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        if (workPhone == null) {
            if (other.workPhone != null)
                return false;
        } else if (!workPhone.equals(other.workPhone))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PatientInformation [firstName=" + firstName + ", lastName=" + lastName + ", maritalStatus=" + maritalStatus + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", notes=" + notes + ", dosOfMostMSPFrom=" + dosOfMostMSPFrom + ", isCreateBadAddress=" + isCreateBadAddress + ", address1=" + address1 + ", address2=" + address2 + ", postalCode=" + postalCode + ", city=" + city + ", state=" + state + ", country=" + country + ", email=" + email + ", homePhone=" + homePhone + ", workPhone=" + workPhone + "]";
    }

}
