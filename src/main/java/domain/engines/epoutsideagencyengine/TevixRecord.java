package domain.engines.epoutsideagencyengine;

import java.util.List;
import java.util.Objects;

public class TevixRecord {
    private String accnId;
    private String ssn;
    private String lastName;
    private String firstName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String homePhone;
    private String dob;
    private String dos;
    private String gender;

    public TevixRecord(){};

    public TevixRecord (String line){
        String[] lineArray = line.split("[|]");

        accnId = lineArray[0];
        ssn = lineArray[1];
        lastName = lineArray[2];
        firstName = lineArray[3];
        address1 = lineArray[4];
        address2 = lineArray[5];
        city = lineArray[6];
        state = lineArray[7];
        zip = lineArray[8];
        homePhone = lineArray[9];
        dob = lineArray[10];
        dos = lineArray[11];
        gender = lineArray[12];
    }

    public String getAccnId() {
        return accnId;
    }

    public void setAccnId(String accnId) {
        this.accnId = accnId;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TevixRecord that = (TevixRecord) o;
        return Objects.equals(accnId, that.accnId) &&
                Objects.equals(ssn, that.ssn) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(address1, that.address1) &&
                Objects.equals(address2, that.address2) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state) &&
                Objects.equals(zip, that.zip) &&
                Objects.equals(homePhone, that.homePhone) &&
                Objects.equals(dob, that.dob) &&
                Objects.equals(dos, that.dos) &&
                Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode() {

        return Objects.hash(accnId, ssn, lastName, firstName, address1, address2, city, state, zip, homePhone, dob, dos, gender);
    }

    @Override
    public String toString() {
        return "TevixRecord{" +
                "accnId='" + accnId + '\'' +
                ", ssn='" + ssn + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", homePhone='" + homePhone + '\'' +
                ", dob='" + dob + '\'' +
                ", dos='" + dos + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
