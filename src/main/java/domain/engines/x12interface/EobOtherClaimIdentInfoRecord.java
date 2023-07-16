package domain.engines.x12interface;

import java.util.Objects;

public class EobOtherClaimIdentInfoRecord {
    private String eobQualifierIdentInfo;
    private String identityInfo;

    public String getEobQualifierIdentInfo() {
        return eobQualifierIdentInfo;
    }

    public void setEobQualifierIdentInfo(String eobQualifierIdentInfo) {
        this.eobQualifierIdentInfo = eobQualifierIdentInfo;
    }

    public String getIdentityInfo() {
        return identityInfo;
    }

    public void setIdentityInfo(String identityInfo) {
        this.identityInfo = identityInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobOtherClaimIdentInfoRecord that = (EobOtherClaimIdentInfoRecord) o;
        return Objects.equals(eobQualifierIdentInfo, that.eobQualifierIdentInfo) &&
                Objects.equals(identityInfo, that.identityInfo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(eobQualifierIdentInfo, identityInfo);
    }

    @Override
    public String toString() {
        return "EobOtherClaimIdentInfoRecord{" +
                "eobQualifierIdentInfo='" + eobQualifierIdentInfo + '\'' +
                ", identityInfo='" + identityInfo + '\'' +
                '}';
    }
}
