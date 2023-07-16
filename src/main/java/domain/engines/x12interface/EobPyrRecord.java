package domain.engines.x12interface;

import java.util.Objects;

public class EobPyrRecord {
    private String name;
    private String externalIdCd;
    private String address1;
    private String address2;
    private String city;
    private String st;
    private String zipCd;
    private String additionalExternalIdCd;
    private String contactName;
    private String contactCommunicationsNumber1;
    private String contactCommunicationsNumber2;
    private String contactCommunicationsNumber3;
    private String eobQualifierAdditionalId;
    private String taxId;
    private String webSiteUrl;
    private String contactCommunicationsQual1;
    private String contactCommunicationsQual2;
    private String contactCommunicationsQual3;
    private String origCompanyId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalIdCd() {
        return externalIdCd;
    }

    public void setExternalIdCd(String externalIdCd) {
        this.externalIdCd = externalIdCd;
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

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getZipCd() {
        return zipCd;
    }

    public void setZipCd(String zipCd) {
        this.zipCd = zipCd;
    }

    public String getAdditionalExternalIdCd() {
        return additionalExternalIdCd;
    }

    public void setAdditionalExternalIdCd(String additionalExternalIdCd) {
        this.additionalExternalIdCd = additionalExternalIdCd;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactCommunicationsNumber1() {
        return contactCommunicationsNumber1;
    }

    public void setContactCommunicationsNumber1(String contactCommunicationsNumber1) {
        this.contactCommunicationsNumber1 = contactCommunicationsNumber1;
    }

    public String getContactCommunicationsNumber2() {
        return contactCommunicationsNumber2;
    }

    public void setContactCommunicationsNumber2(String contactCommunicationsNumber2) {
        this.contactCommunicationsNumber2 = contactCommunicationsNumber2;
    }

    public String getContactCommunicationsNumber3() {
        return contactCommunicationsNumber3;
    }

    public void setContactCommunicationsNumber3(String contactCommunicationsNumber3) {
        this.contactCommunicationsNumber3 = contactCommunicationsNumber3;
    }

    public String getEobQualifierAdditionalId() {
        return eobQualifierAdditionalId;
    }

    public void setEobQualifierAdditionalId(String eobQualifierAdditionalId) {
        this.eobQualifierAdditionalId = eobQualifierAdditionalId;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl;
    }

    public String getContactCommunicationsQual1() {
        return contactCommunicationsQual1;
    }

    public void setContactCommunicationsQual1(String contactCommunicationsQual1) {
        this.contactCommunicationsQual1 = contactCommunicationsQual1;
    }

    public String getContactCommunicationsQual2() {
        return contactCommunicationsQual2;
    }

    public void setContactCommunicationsQual2(String contactCommunicationsQual2) {
        this.contactCommunicationsQual2 = contactCommunicationsQual2;
    }

    public String getContactCommunicationsQual3() {
        return contactCommunicationsQual3;
    }

    public void setContactCommunicationsQual3(String contactCommunicationsQual3) {
        this.contactCommunicationsQual3 = contactCommunicationsQual3;
    }

    public String getOrigCompanyId() {
        return origCompanyId;
    }

    public void setOrigCompanyId(String origCompanyId) {
        this.origCompanyId = origCompanyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobPyrRecord that = (EobPyrRecord) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(externalIdCd, that.externalIdCd) &&
                Objects.equals(address1, that.address1) &&
                Objects.equals(address2, that.address2) &&
                Objects.equals(city, that.city) &&
                Objects.equals(st, that.st) &&
                Objects.equals(zipCd, that.zipCd) &&
                Objects.equals(additionalExternalIdCd, that.additionalExternalIdCd) &&
                Objects.equals(contactName, that.contactName) &&
                Objects.equals(contactCommunicationsNumber1, that.contactCommunicationsNumber1) &&
                Objects.equals(contactCommunicationsNumber2, that.contactCommunicationsNumber2) &&
                Objects.equals(contactCommunicationsNumber3, that.contactCommunicationsNumber3) &&
                Objects.equals(eobQualifierAdditionalId, that.eobQualifierAdditionalId) &&
                Objects.equals(taxId, that.taxId) &&
                Objects.equals(webSiteUrl, that.webSiteUrl) &&
                Objects.equals(contactCommunicationsQual1, that.contactCommunicationsQual1) &&
                Objects.equals(contactCommunicationsQual2, that.contactCommunicationsQual2) &&
                Objects.equals(contactCommunicationsQual3, that.contactCommunicationsQual3) &&
                Objects.equals(origCompanyId, that.origCompanyId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, externalIdCd, address1, address2, city, st, zipCd, additionalExternalIdCd, contactName, contactCommunicationsNumber1, contactCommunicationsNumber2, contactCommunicationsNumber3, eobQualifierAdditionalId, taxId, webSiteUrl, contactCommunicationsQual1, contactCommunicationsQual2, contactCommunicationsQual3, origCompanyId);
    }

    @Override
    public String toString() {
        return "EobPyrRecord{" +
                "name='" + name + '\'' +
                ", externalIdCd='" + externalIdCd + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", st='" + st + '\'' +
                ", zipCd='" + zipCd + '\'' +
                ", additionalExternalIdCd='" + additionalExternalIdCd + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactCommunicationsNumber1='" + contactCommunicationsNumber1 + '\'' +
                ", contactCommunicationsNumber2='" + contactCommunicationsNumber2 + '\'' +
                ", contactCommunicationsNumber3='" + contactCommunicationsNumber3 + '\'' +
                ", eobQualifierAdditionalId='" + eobQualifierAdditionalId + '\'' +
                ", taxId='" + taxId + '\'' +
                ", webSiteUrl='" + webSiteUrl + '\'' +
                ", contactCommunicationsQual1='" + contactCommunicationsQual1 + '\'' +
                ", contactCommunicationsQual2='" + contactCommunicationsQual2 + '\'' +
                ", contactCommunicationsQual3='" + contactCommunicationsQual3 + '\'' +
                ", origCompanyId='" + origCompanyId + '\'' +
                '}';
    }
}
