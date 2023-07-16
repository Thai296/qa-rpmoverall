package domain.engines.x12interface;

import java.util.Objects;

public class EobPyrContactRecord {
    private int eobCntctInfoSeqId;

    public int getEobCntctInfoSeqId() {
        return eobCntctInfoSeqId;
    }

    public void setEobCntctInfoSeqId(int eobCntctInfoSeqId) {
        this.eobCntctInfoSeqId = eobCntctInfoSeqId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobPyrContactRecord that = (EobPyrContactRecord) o;
        return eobCntctInfoSeqId == that.eobCntctInfoSeqId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(eobCntctInfoSeqId);
    }
}
