package domain.engines.x12interface;

import java.util.Objects;

public class EobClaimRenderingProvIdRecord {
    private int eobRenderingProvIdTyp;
    private String referenceId;

    public int getEobRenderingProvIdTyp() {
        return eobRenderingProvIdTyp;
    }

    public void setEobRenderingProvIdTyp(int eobRenderingProvIdTyp) {
        this.eobRenderingProvIdTyp = eobRenderingProvIdTyp;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobClaimRenderingProvIdRecord that = (EobClaimRenderingProvIdRecord) o;
        return eobRenderingProvIdTyp == that.eobRenderingProvIdTyp &&
                Objects.equals(referenceId, that.referenceId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(eobRenderingProvIdTyp, referenceId);
    }

    @Override
    public String toString() {
        return "EobClaimRenderingProvIdRecord{" +
                "eobRenderingProvIdTyp=" + eobRenderingProvIdTyp +
                ", referenceId='" + referenceId + '\'' +
                '}';
    }
}
