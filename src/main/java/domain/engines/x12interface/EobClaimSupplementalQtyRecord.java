package domain.engines.x12interface;

import java.util.Objects;

public class EobClaimSupplementalQtyRecord {
    private String eobQualifierClmSuppQty;
    private String qty;

    public String getEobQualifierClmSuppQty() {
        return eobQualifierClmSuppQty;
    }

    public void setEobQualifierClmSuppQty(String eobQualifierClmSuppQty) {
        this.eobQualifierClmSuppQty = eobQualifierClmSuppQty;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobClaimSupplementalQtyRecord that = (EobClaimSupplementalQtyRecord) o;
        return Objects.equals(eobQualifierClmSuppQty, that.eobQualifierClmSuppQty) &&
                Objects.equals(qty, that.qty);
    }

    @Override
    public int hashCode() {

        return Objects.hash(eobQualifierClmSuppQty, qty);
    }

    @Override
    public String toString() {
        return "EobClaimSupplementalQtyRecord{" +
                "eobQualifierClmSuppQty='" + eobQualifierClmSuppQty + '\'' +
                ", qty='" + qty + '\'' +
                '}';
    }
}
