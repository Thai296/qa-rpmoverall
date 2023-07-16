package domain.accession.patientDemographics;

public class EmployerInfo {
    
    private String employerName = "";
    private String employerStatus = "";
    private String address1 = "";
    private String address2 = "";
    private String postalCode = "";
    private String city = "";
    private String state = "";
    private String country = "";
    private String workPhone = "";
    private String fax = "";

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getEmployerStatus() {
        return employerStatus;
    }

    public void setEmployerStatus(String employerStatus) {
        this.employerStatus = employerStatus;
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
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
        result = prime * result + ((address2 == null) ? 0 : address2.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((employerName == null) ? 0 : employerName.hashCode());
        result = prime * result + ((employerStatus == null) ? 0 : employerStatus.hashCode());
        result = prime * result + ((fax == null) ? 0 : fax.hashCode());
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
        if (!(obj instanceof EmployerInfo))
            return false;
        EmployerInfo other = (EmployerInfo) obj;
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
        if (employerName == null) {
            if (other.employerName != null)
                return false;
        } else if (!employerName.equals(other.employerName))
            return false;
        if (employerStatus == null) {
            if (other.employerStatus != null)
                return false;
        } else if (!employerStatus.equals(other.employerStatus))
            return false;
        if (fax == null) {
            if (other.fax != null)
                return false;
        } else if (!fax.equals(other.fax))
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
        return "EmployerInfo [employerName=" + employerName + ", employerStatus=" + employerStatus + ", address1=" + address1 + ", address2=" + address2 + ", postalCode=" + postalCode + ", city=" + city + ", state=" + state + ", country=" + country + ", workPhone=" + workPhone + ", fax=" + fax + "]";
    }

}
