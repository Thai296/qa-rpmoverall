package domain.engines.x12interface;

import java.sql.Date;
import java.util.Objects;

public class X12InterchangeRecord {
    private int seqId;
    private String fileName;
    private int totTransSets;
    private String senderControlId;
    private String senderInterchangeDate;
    private Date creationDt;
    private int x12SenderId;
    private String senderTime;

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTotTransSets() {
        return totTransSets;
    }

    public void setTotTransSets(int totTransSets) {
        this.totTransSets = totTransSets;
    }

    public String getSenderControlId() {
        return senderControlId;
    }

    public void setSenderControlId(String senderControlId) {
        this.senderControlId = senderControlId;
    }

    public String getSenderInterchangeDate() {
        return senderInterchangeDate;
    }

    public void setSenderInterchangeDate(String senderInterchangeDate) {
        this.senderInterchangeDate = senderInterchangeDate;
    }

    public Date getCreationDt() {
        return creationDt;
    }

    public void setCreationDt(Date creationDt) {
        this.creationDt = creationDt;
    }

    public int getX12SenderId() {
        return x12SenderId;
    }

    public void setX12SenderId(int x12SenderId) {
        this.x12SenderId = x12SenderId;
    }

    public String getSenderTime() {
        return senderTime;
    }

    public void setSenderTime(String senderTime) {
        this.senderTime = senderTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        X12InterchangeRecord that = (X12InterchangeRecord) o;
        return seqId == that.seqId &&
                totTransSets == that.totTransSets &&
                x12SenderId == that.x12SenderId &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(senderControlId, that.senderControlId) &&
                Objects.equals(senderInterchangeDate, that.senderInterchangeDate) &&
                Objects.equals(creationDt, that.creationDt) &&
                Objects.equals(senderTime, that.senderTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(seqId, fileName, totTransSets, senderControlId, senderInterchangeDate, creationDt, x12SenderId, senderTime);
    }

    @Override
    public String toString() {
        return "X12InterchangeRecord{" +
                "seqId=" + seqId +
                ", fileName='" + fileName + '\'' +
                ", totTransSets=" + totTransSets +
                ", senderControlId='" + senderControlId + '\'' +
                ", senderInterchangeDate='" + senderInterchangeDate + '\'' +
                ", creationDt=" + creationDt +
                ", x12SenderId=" + x12SenderId +
                ", senderTime='" + senderTime + '\'' +
                '}';
    }
}
