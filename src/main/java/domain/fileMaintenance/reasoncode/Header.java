package domain.fileMaintenance.reasoncode;

import java.sql.Date;
import java.util.Objects;

public class Header {
    private String name;
    private String errorGroup;
    private String reasonCodeId;
    private String reasonCodeTableId;
    private Date effectiveDate;
    private Date dosEffectiveDate;
    private boolean isDeleteReasonCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getErrorGroup() {
        return errorGroup;
    }

    public void setErrorGroup(String errorGroup) {
        this.errorGroup = errorGroup;
    }

    public String getReasonCodeId() {
        return reasonCodeId;
    }

    public void setReasonCodeId(String reasonCodeId) {
        this.reasonCodeId = reasonCodeId;
    }

    public String getReasonCodeTableId() {
        return reasonCodeTableId;
    }

    public void setReasonCodeTableId(String reasonCodeTableId) {
        this.reasonCodeTableId = reasonCodeTableId;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getDosEffectiveDate() {
        return dosEffectiveDate;
    }

    public void setDosEffectiveDate(Date dosEffectiveDate) {
        this.dosEffectiveDate = dosEffectiveDate;
    }

    public boolean isDeleteReasonCode() {
        return isDeleteReasonCode;
    }

    public void setDeleteReasonCode(boolean deleteReasonCode) {
        isDeleteReasonCode = deleteReasonCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return isDeleteReasonCode == header.isDeleteReasonCode &&
                Objects.equals(name, header.name) &&
                Objects.equals(errorGroup, header.errorGroup) &&
                Objects.equals(reasonCodeId, header.reasonCodeId) &&
                Objects.equals(reasonCodeTableId, header.reasonCodeTableId) &&
                Objects.equals(effectiveDate, header.effectiveDate) &&
                Objects.equals(dosEffectiveDate, header.dosEffectiveDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, errorGroup, reasonCodeId, reasonCodeTableId, effectiveDate, dosEffectiveDate, isDeleteReasonCode);
    }

    @Override
    public String toString() {
        return "Header{" +
                "name='" + name + '\'' +
                ", errorGroup='" + errorGroup + '\'' +
                ", reasonCodeId='" + reasonCodeId + '\'' +
                ", reasonCodeTableId='" + reasonCodeTableId + '\'' +
                ", effectiveDate=" + effectiveDate +
                ", dosEffectiveDate=" + dosEffectiveDate +
                ", isDeleteReasonCode=" + isDeleteReasonCode +
                '}';
    }

}
