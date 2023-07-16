package domain.engines.x12interface;

import java.util.Objects;

public class EobClaimSupplementalAmtRecord {
    private String eobQualifierSuppAmt;
    private String amtStr;

    public String getEobQualifierSuppAmt() {
        return eobQualifierSuppAmt;
    }

    public void setEobQualifierSuppAmt(String eobQualifierSuppAmt) {
        this.eobQualifierSuppAmt = eobQualifierSuppAmt;
    }

    public String getAmtStr() {
        return amtStr;
    }

    public void setAmtStr(String amtStr) {
        this.amtStr = amtStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobClaimSupplementalAmtRecord that = (EobClaimSupplementalAmtRecord) o;
        return Objects.equals(eobQualifierSuppAmt, that.eobQualifierSuppAmt) &&
                Objects.equals(amtStr, that.amtStr);
    }

    @Override
    public int hashCode() {

        return Objects.hash(eobQualifierSuppAmt, amtStr);
    }

    @Override
    public String toString() {
        return "EobClaimSupplementalAmtRecord{" +
                "eobQualifierSuppAmt='" + eobQualifierSuppAmt + '\'' +
                ", amtStr='" + amtStr + '\'' +
                '}';
    }
}
