package domain.client.payorExclusions;

import java.sql.Date;
import java.util.Objects;

public class PayorGroupExclusionTable {
    private Date effectiveDate;
    private Date expirationDate;
    private String groupName;

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayorGroupExclusionTable that = (PayorGroupExclusionTable) o;
        return Objects.equals(effectiveDate, that.effectiveDate) &&
                Objects.equals(expirationDate, that.expirationDate) &&
                Objects.equals(groupName, that.groupName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(effectiveDate, expirationDate, groupName);
    }

    @Override
    public String toString() {
        return "PayorGroupExclusionTable{" +
                "effectiveDate=" + effectiveDate +
                ", expirationDate=" + expirationDate +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
