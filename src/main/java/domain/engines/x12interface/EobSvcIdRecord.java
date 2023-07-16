package domain.engines.x12interface;

import java.util.Objects;

public class EobSvcIdRecord {
    private int seqId;
    private int eobSvcPmtSeqId;
    private int eobSvcIdTyp;
    private String referenceId;

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public int getEobSvcPmtSeqId() {
        return eobSvcPmtSeqId;
    }

    public void setEobSvcPmtSeqId(int eobSvcPmtSeqId) {
        this.eobSvcPmtSeqId = eobSvcPmtSeqId;
    }

    public int getEobSvcIdTyp() {
        return eobSvcIdTyp;
    }

    public void setEobSvcIdTyp(int eobSvcIdTyp) {
        this.eobSvcIdTyp = eobSvcIdTyp;
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
        EobSvcIdRecord that = (EobSvcIdRecord) o;
        return seqId == that.seqId &&
                eobSvcPmtSeqId == that.eobSvcPmtSeqId &&
                eobSvcIdTyp == that.eobSvcIdTyp &&
                Objects.equals(referenceId, that.referenceId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(seqId, eobSvcPmtSeqId, eobSvcIdTyp, referenceId);
    }

    @Override
    public String toString() {
        return "EobSvcIdRecord{" +
                "seqId=" + seqId +
                ", eobSvcPmtSeqId=" + eobSvcPmtSeqId +
                ", eobSvcIdTyp=" + eobSvcIdTyp +
                ", referenceId='" + referenceId + '\'' +
                '}';
    }
}
