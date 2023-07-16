package domain.engines.hl7parsing.mgl;

import java.util.Objects;

public class AccnTestRecord {
    private String mod1Id;
    private int facId;
    private int procTypId;
    private String procId;
    private boolean abnRec;
    private int units;
    private String pyrSvcAuthNum;

    public String getMod1Id() {
        return mod1Id;
    }

    public void setMod1Id(String mod1Id) {
        this.mod1Id = mod1Id;
    }

    public int getFacId() {
        return facId;
    }

    public void setFacId(int facId) {
        this.facId = facId;
    }

    public int getProcTypId() {
        return procTypId;
    }

    public void setProcTypId(int procTypId) {
        this.procTypId = procTypId;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public boolean isAbnRec() {
        return abnRec;
    }

    public void setAbnRec(boolean abnRec) {
        this.abnRec = abnRec;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getPyrSvcAuthNum() {
        return pyrSvcAuthNum;
    }

    public void setPyrSvcAuthNum(String pyrSvcAuthNum) {
        this.pyrSvcAuthNum = pyrSvcAuthNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccnTestRecord that = (AccnTestRecord) o;
        return facId == that.facId &&
                procTypId == that.procTypId &&
                abnRec == that.abnRec &&
                units == that.units &&
                Objects.equals(mod1Id, that.mod1Id) &&
                Objects.equals(procId, that.procId) &&
                Objects.equals(pyrSvcAuthNum, that.pyrSvcAuthNum);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mod1Id, facId, procTypId, procId, abnRec, units, pyrSvcAuthNum);
    }

    @Override
    public String toString() {
        return "AccnTestRecord{" +
                "mod1Id='" + mod1Id + '\'' +
                ", facId=" + facId +
                ", procTypId=" + procTypId +
                ", procId='" + procId + '\'' +
                ", abnRec=" + abnRec +
                ", units=" + units +
                ", pyrSvcAuthNum='" + pyrSvcAuthNum + '\'' +
                '}';
    }
}
