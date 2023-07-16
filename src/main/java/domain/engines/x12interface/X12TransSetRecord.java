package domain.engines.x12interface;

import java.sql.Date;
import java.util.Objects;

public class X12TransSetRecord {
    private int x12TransSetTyp;
    private int processStatusId;
    private String externalCtrlId;
    private Date creationDt;
    private Date lastProcessAttemptDt;
    private String functionalGrpExtCtrlId;

    public int getX12TransSetTyp() {
        return x12TransSetTyp;
    }

    public void setX12TransSetTyp(int x12TransSetTyp) {
        this.x12TransSetTyp = x12TransSetTyp;
    }

    public int getProcessStatusId() {
        return processStatusId;
    }

    public void setProcessStatusId(int processStatusId) {
        this.processStatusId = processStatusId;
    }

    public String getExternalCtrlId() {
        return externalCtrlId;
    }

    public void setExternalCtrlId(String externalCtrlId) {
        this.externalCtrlId = externalCtrlId;
    }

    public Date getCreationDt() {
        return creationDt;
    }

    public void setCreationDt(Date creationDt) {
        this.creationDt = creationDt;
    }

    public Date getLastProcessAttemptDt() {
        return lastProcessAttemptDt;
    }

    public void setLastProcessAttemptDt(Date lastProcessAttemptDt) {
        this.lastProcessAttemptDt = lastProcessAttemptDt;
    }

    public String getFunctionalGrpExtCtrlId() {
        return functionalGrpExtCtrlId;
    }

    public void setFunctionalGrpExtCtrlId(String functionalGrpExtCtrlId) {
        this.functionalGrpExtCtrlId = functionalGrpExtCtrlId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        X12TransSetRecord that = (X12TransSetRecord) o;
        return x12TransSetTyp == that.x12TransSetTyp &&
                processStatusId == that.processStatusId &&
                Objects.equals(externalCtrlId, that.externalCtrlId) &&
                Objects.equals(creationDt, that.creationDt) &&
                Objects.equals(lastProcessAttemptDt, that.lastProcessAttemptDt) &&
                Objects.equals(functionalGrpExtCtrlId, that.functionalGrpExtCtrlId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(x12TransSetTyp, processStatusId, externalCtrlId, creationDt, lastProcessAttemptDt, functionalGrpExtCtrlId);
    }

    @Override
    public String toString() {
        return "X12TransSetRecord{" +
                "x12TransSetTyp=" + x12TransSetTyp +
                ", processStatusId=" + processStatusId +
                ", externalCtrlId='" + externalCtrlId + '\'' +
                ", creationDt=" + creationDt +
                ", lastProcessAttemptDt=" + lastProcessAttemptDt +
                ", functionalGrpExtCtrlId='" + functionalGrpExtCtrlId + '\'' +
                '}';
    }
}
