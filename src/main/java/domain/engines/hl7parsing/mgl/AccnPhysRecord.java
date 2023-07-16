package domain.engines.hl7parsing.mgl;

import java.util.Objects;

public class AccnPhysRecord {
    private int physSeqId;
    private int accnPhysTypId;

    public int getPhysSeqId() {
        return physSeqId;
    }

    public void setPhysSeqId(int physSeqId) {
        this.physSeqId = physSeqId;
    }

    public int getAccnPhysTypId() {
        return accnPhysTypId;
    }

    public void setAccnPhysTypId(int accnPhysTypId) {
        this.accnPhysTypId = accnPhysTypId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccnPhysRecord that = (AccnPhysRecord) o;
        return physSeqId == that.physSeqId &&
                accnPhysTypId == that.accnPhysTypId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(physSeqId, accnPhysTypId);
    }

    @Override
    public String toString() {
        return "AccnPhysRecord{" +
                "physSeqId=" + physSeqId +
                ", accnPhysTypId=" + accnPhysTypId +
                '}';
    }
}
