package domain.engines.x12interface;

import java.util.Objects;

public class EobSvcSupplementalAmtRecord {
    private String eobQualifier;
    private String amtStr;

    public String getEobQualifier() {
        return eobQualifier;
    }

    public void setEobQualifier(String eobQualifier) {
        this.eobQualifier = eobQualifier;
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
        EobSvcSupplementalAmtRecord that = (EobSvcSupplementalAmtRecord) o;
        return Objects.equals(eobQualifier, that.eobQualifier) &&
                Objects.equals(amtStr, that.amtStr);
    }

    @Override
    public int hashCode() {

        return Objects.hash(eobQualifier, amtStr);
    }

    @Override
    public String toString() {
        return "EobSvcSupplementalAmtRecord{" +
                "eobQualifier='" + eobQualifier + '\'' +
                ", amtStr='" + amtStr + '\'' +
                '}';
    }
}
