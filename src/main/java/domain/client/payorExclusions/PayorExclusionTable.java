package domain.client.payorExclusions;

import java.sql.Date;
import java.util.Objects;

public class PayorExclusionTable {
    private Date effectiveDate;
    private Date expirationDate;
    private String payorId;
    private String payorName;

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPayorId() {
        return payorId;
    }

    public void setPayorId(String payorId) {
        this.payorId = payorId;
    }

    public String getPayorName() {
        return payorName;
    }

    public void setPayorName(String payorName) {
        this.payorName = payorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayorExclusionTable that = (PayorExclusionTable) o;
        return Objects.equals(effectiveDate, that.effectiveDate) &&
                Objects.equals(expirationDate, that.expirationDate) &&
                Objects.equals(payorId, that.payorId) &&
                Objects.equals(payorName, that.payorName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(effectiveDate, expirationDate, payorId, payorName);
    }

    @Override
    public String toString() {
        return "PayorExclusionTable{" +
                "effectiveDate=" + effectiveDate +
                ", expirationDate=" + expirationDate +
                ", payorId='" + payorId + '\'' +
                ", payorName='" + payorName + '\'' +
                '}';
    }
}
