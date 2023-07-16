package domain.engines.hl7parsing.mgl;

import java.sql.Date;
import java.util.Objects;

public class AccnPyrRecord {
    private int pyrId;
    private String grpId;
    private String subsId;
    private int relshpId;
    private String insLNm;
    private String insFNm;
    private String insAddr1;
    private String insAddr2;
    private String insZipId;
    private Date insDob;
    private String cmnt;

    public int getPyrId() {
        return pyrId;
    }

    public void setPyrId(int pyrId) {
        this.pyrId = pyrId;
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public String getSubsId() {
        return subsId;
    }

    public void setSubsId(String subsId) {
        this.subsId = subsId;
    }

    public int getRelshpId() {
        return relshpId;
    }

    public void setRelshpId(int relshpId) {
        this.relshpId = relshpId;
    }

    public String getInsLNm() {
        return insLNm;
    }

    public void setInsLNm(String insLNm) {
        this.insLNm = insLNm;
    }

    public String getInsFNm() {
        return insFNm;
    }

    public void setInsFNm(String insFNm) {
        this.insFNm = insFNm;
    }

    public String getInsAddr1() {
        return insAddr1;
    }

    public void setInsAddr1(String insAddr1) {
        this.insAddr1 = insAddr1;
    }

    public String getInsAddr2() {
        return insAddr2;
    }

    public void setInsAddr2(String insAddr2) {
        this.insAddr2 = insAddr2;
    }

    public String getInsZipId() {
        return insZipId;
    }

    public void setInsZipId(String insZipId) {
        this.insZipId = insZipId;
    }

    public Date getInsDob() {
        return insDob;
    }

    public void setInsDob(Date insDob) {
        this.insDob = insDob;
    }

    public String getCmnt() {
        return cmnt;
    }

    public void setCmnt(String cmnt) {
        this.cmnt = cmnt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccnPyrRecord that = (AccnPyrRecord) o;
        return pyrId == that.pyrId &&
                relshpId == that.relshpId &&
                Objects.equals(grpId, that.grpId) &&
                Objects.equals(subsId, that.subsId) &&
                Objects.equals(insLNm, that.insLNm) &&
                Objects.equals(insFNm, that.insFNm) &&
                Objects.equals(insAddr1, that.insAddr1) &&
                Objects.equals(insAddr2, that.insAddr2) &&
                Objects.equals(insZipId, that.insZipId) &&
                Objects.equals(insDob, that.insDob) &&
                Objects.equals(cmnt, that.cmnt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pyrId, grpId, subsId, relshpId, insLNm, insFNm, insAddr1, insAddr2, insZipId, insDob, cmnt);
    }

    @Override
    public String toString() {
        return "AccnPyrRecord{" +
                "pyrId=" + pyrId +
                ", grpId='" + grpId + '\'' +
                ", subsId='" + subsId + '\'' +
                ", relshpId=" + relshpId +
                ", insLNm='" + insLNm + '\'' +
                ", insFNm='" + insFNm + '\'' +
                ", insAddr1='" + insAddr1 + '\'' +
                ", insAddr2='" + insAddr2 + '\'' +
                ", insZipId='" + insZipId + '\'' +
                ", insDob=" + insDob +
                ", cmnt='" + cmnt + '\'' +
                '}';
    }
}
