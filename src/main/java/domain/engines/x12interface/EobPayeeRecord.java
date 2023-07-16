package domain.engines.x12interface;

import java.util.Objects;

public class EobPayeeRecord {
    private String name;
    private String externalId;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String eobQualifierAdditionalId;
    private String addionalId;
    private String eobQualifierExtIdQual;
    private int eobDeliverMthdTyp;
    private String remitRecipientName;
    private String remitCommNumber;

    public String getAddionalId() {
        return addionalId;
    }

    public void setAddionalId(String addionalId) {
        this.addionalId = addionalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    public String getEobQualifierAdditionalId() {
        return eobQualifierAdditionalId;
    }

    public void setEobQualifierAdditionalId(String eobQualifierAdditionalId) {
        this.eobQualifierAdditionalId = eobQualifierAdditionalId;
    }

    public String getEobQualifierExtIdQual() {
        return eobQualifierExtIdQual;
    }

    public void setEobQualifierExtIdQual(String eobQualifierExtIdQual) {
        this.eobQualifierExtIdQual = eobQualifierExtIdQual;
    }

    public int getEobDeliverMthdTyp() {
        return eobDeliverMthdTyp;
    }

    public void setEobDeliverMthdTyp(int eobDeliverMthdTyp) {
        this.eobDeliverMthdTyp = eobDeliverMthdTyp;
    }

    public String getRemitRecipientName() {
        return remitRecipientName;
    }

    public void setRemitRecipientName(String remitRecipientName) {
        this.remitRecipientName = remitRecipientName;
    }

    public String getRemitCommNumber() {
        return remitCommNumber;
    }

    public void setRemitCommNumber(String remitCommNumber) {
        this.remitCommNumber = remitCommNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobPayeeRecord that = (EobPayeeRecord) o;
        return eobDeliverMthdTyp == that.eobDeliverMthdTyp &&
                Objects.equals(name, that.name) &&
                Objects.equals(externalId, that.externalId) &&
                Objects.equals(address1, that.address1) &&
                Objects.equals(address2, that.address2) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state) &&
                Objects.equals(zip, that.zip) &&
                Objects.equals(eobQualifierAdditionalId, that.eobQualifierAdditionalId) &&
                Objects.equals(addionalId, that.addionalId) &&
                Objects.equals(eobQualifierExtIdQual, that.eobQualifierExtIdQual) &&
                Objects.equals(remitRecipientName, that.remitRecipientName) &&
                Objects.equals(remitCommNumber, that.remitCommNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, externalId, address1, address2, city, state, zip, eobQualifierAdditionalId, addionalId, eobQualifierExtIdQual, eobDeliverMthdTyp, remitRecipientName, remitCommNumber);
    }

    @Override
    public String toString() {
        return "EobPayeeRecord{" +
                "name='" + name + '\'' +
                ", externalId='" + externalId + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", eobQualifierAdditionalId='" + eobQualifierAdditionalId + '\'' +
                ", addionalId='" + addionalId + '\'' +
                ", eobQualifierExtIdQual='" + eobQualifierExtIdQual + '\'' +
                ", eobDeliverMthdTyp=" + eobDeliverMthdTyp +
                ", remitRecipientName='" + remitRecipientName + '\'' +
                ", remitCommNumber='" + remitCommNumber + '\'' +
                '}';
    }
}
