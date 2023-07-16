package domain.engines.refund;

import com.xifin.util.Money;

import java.sql.Date;
import java.util.Objects;

public class GenericRefundTxtFile {
    private String payeeId;
    private String payeeFullName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private Date refundDate;
    private String accessionId;
    private Money refundAmount;

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeFullName() {
        return payeeFullName;
    }

    public void setPayeeFullName(String payeeFullName) {
        this.payeeFullName = payeeFullName;
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

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public String getAccessionId() {
        return accessionId;
    }

    public void setAccessionId(String accessionId) {
        this.accessionId = accessionId;
    }

    public Money getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Money refundAmount) {
        this.refundAmount = refundAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericRefundTxtFile that = (GenericRefundTxtFile) o;
        return Objects.equals(payeeId, that.payeeId) &&
                Objects.equals(payeeFullName, that.payeeFullName) &&
                Objects.equals(address1, that.address1) &&
                Objects.equals(address2, that.address2) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state) &&
                Objects.equals(zip, that.zip) &&
                Objects.equals(refundDate, that.refundDate) &&
                Objects.equals(accessionId, that.accessionId) &&
                Objects.equals(refundAmount, that.refundAmount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(payeeId, payeeFullName, address1, address2, city, state, zip, refundDate, accessionId, refundAmount);
    }

    @Override
    public String toString() {
        return "GenericRefundTxtFile{" +
                "payeeId='" + payeeId + '\'' +
                ", payeeFullName='" + payeeFullName + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", refundDate=" + refundDate +
                ", accessionId='" + accessionId + '\'' +
                ", refundAmount=" + refundAmount +
                '}';
    }
}
