package domain.fileMaintenance.reasoncode;

import java.sql.Date;
import java.util.Objects;

public class CrossReference {
    private Date effectiveDate;
    private Date expirationDate;
    private String crossReferenceDescription;

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

    public String getCrossReferenceDescription() {
        return crossReferenceDescription;
    }

    public void setCrossReferenceDescription(String crossReferenceDescription) {
        this.crossReferenceDescription = crossReferenceDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrossReference that = (CrossReference) o;
        return Objects.equals(effectiveDate, that.effectiveDate) &&
                Objects.equals(expirationDate, that.expirationDate) &&
                Objects.equals(crossReferenceDescription, that.crossReferenceDescription);
    }

    @Override
    public int hashCode() {

        return Objects.hash(effectiveDate, expirationDate, crossReferenceDescription);
    }

    @Override
    public String toString() {
        return "CrossReference{" +
                "effectiveDate=" + effectiveDate +
                ", expirationDate=" + expirationDate +
                ", crossReferenceDescription='" + crossReferenceDescription + '\'' +
                '}';
    }
}
