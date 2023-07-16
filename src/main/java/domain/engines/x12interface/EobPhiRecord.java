package domain.engines.x12interface;

import java.util.Objects;

public class EobPhiRecord {
    private String ptFNm;
    private String ptLNm;
    private String ptMNm;
    private String eobQualifierPtId;
    private String externalPtId;
    private String subsFNm;
    private String subsLNm;
    private String subsMNm;
    private String eobQualifierSubs;
    private String externalSubsId;
    private String correctedSubsFNm;
    private String correctedSubsLNm;
    private String correctedSubsMNm;
    private String correctedExternalSubsId;
    private String otherSubsFName;
    private String otherSubsLName;
    private String otherSubsMName;
    private String externalOtherSubsId;
    private String eobQualifierOtherSubsId;

    public String getPtFNm() {
        return ptFNm;
    }

    public void setPtFNm(String ptFNm) {
        this.ptFNm = ptFNm;
    }

    public String getPtLNm() {
        return ptLNm;
    }

    public void setPtLNm(String ptLNm) {
        this.ptLNm = ptLNm;
    }

    public String getPtMNm() {
        return ptMNm;
    }

    public void setPtMNm(String ptMNm) {
        this.ptMNm = ptMNm;
    }

    public String getEobQualifierPtId() {
        return eobQualifierPtId;
    }

    public void setEobQualifierPtId(String eobQualifierPtId) {
        this.eobQualifierPtId = eobQualifierPtId;
    }

    public String getExternalPtId() {
        return externalPtId;
    }

    public void setExternalPtId(String externalPtId) {
        this.externalPtId = externalPtId;
    }

    public String getSubsFNm() {
        return subsFNm;
    }

    public void setSubsFNm(String subsFNm) {
        this.subsFNm = subsFNm;
    }

    public String getSubsLNm() {
        return subsLNm;
    }

    public void setSubsLNm(String subsLNm) {
        this.subsLNm = subsLNm;
    }

    public String getSubsMNm() {
        return subsMNm;
    }

    public void setSubsMNm(String subsMNm) {
        this.subsMNm = subsMNm;
    }

    public String getEobQualifierSubs() {
        return eobQualifierSubs;
    }

    public void setEobQualifierSubs(String eobQualifierSubs) {
        this.eobQualifierSubs = eobQualifierSubs;
    }

    public String getExternalSubsId() {
        return externalSubsId;
    }

    public void setExternalSubsId(String externalSubsId) {
        this.externalSubsId = externalSubsId;
    }

    public String getCorrectedSubsFNm() {
        return correctedSubsFNm;
    }

    public void setCorrectedSubsFNm(String correctedSubsFNm) {
        this.correctedSubsFNm = correctedSubsFNm;
    }

    public String getCorrectedSubsLNm() {
        return correctedSubsLNm;
    }

    public void setCorrectedSubsLNm(String correctedSubsLNm) {
        this.correctedSubsLNm = correctedSubsLNm;
    }

    public String getCorrectedSubsMNm() {
        return correctedSubsMNm;
    }

    public void setCorrectedSubsMNm(String correctedSubsMNm) {
        this.correctedSubsMNm = correctedSubsMNm;
    }

    public String getCorrectedExternalSubsId() {
        return correctedExternalSubsId;
    }

    public void setCorrectedExternalSubsId(String correctedExternalSubsId) {
        this.correctedExternalSubsId = correctedExternalSubsId;
    }

    public String getOtherSubsFName() {
        return otherSubsFName;
    }

    public void setOtherSubsFName(String otherSubsFName) {
        this.otherSubsFName = otherSubsFName;
    }

    public String getOtherSubsLName() {
        return otherSubsLName;
    }

    public void setOtherSubsLName(String otherSubsLName) {
        this.otherSubsLName = otherSubsLName;
    }

    public String getOtherSubsMName() {
        return otherSubsMName;
    }

    public void setOtherSubsMName(String otherSubsMName) {
        this.otherSubsMName = otherSubsMName;
    }

    public String getExternalOtherSubsId() {
        return externalOtherSubsId;
    }

    public void setExternalOtherSubsId(String externalOtherSubsId) {
        this.externalOtherSubsId = externalOtherSubsId;
    }

    public String getEobQualifierOtherSubsId() {
        return eobQualifierOtherSubsId;
    }

    public void setEobQualifierOtherSubsId(String eobQualifierOtherSubsId) {
        this.eobQualifierOtherSubsId = eobQualifierOtherSubsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobPhiRecord that = (EobPhiRecord) o;
        return Objects.equals(ptFNm, that.ptFNm) &&
                Objects.equals(ptLNm, that.ptLNm) &&
                Objects.equals(ptMNm, that.ptMNm) &&
                Objects.equals(eobQualifierPtId, that.eobQualifierPtId) &&
                Objects.equals(externalPtId, that.externalPtId) &&
                Objects.equals(subsFNm, that.subsFNm) &&
                Objects.equals(subsLNm, that.subsLNm) &&
                Objects.equals(subsMNm, that.subsMNm) &&
                Objects.equals(eobQualifierSubs, that.eobQualifierSubs) &&
                Objects.equals(externalSubsId, that.externalSubsId) &&
                Objects.equals(correctedSubsFNm, that.correctedSubsFNm) &&
                Objects.equals(correctedSubsLNm, that.correctedSubsLNm) &&
                Objects.equals(correctedSubsMNm, that.correctedSubsMNm) &&
                Objects.equals(correctedExternalSubsId, that.correctedExternalSubsId) &&
                Objects.equals(otherSubsFName, that.otherSubsFName) &&
                Objects.equals(otherSubsLName, that.otherSubsLName) &&
                Objects.equals(otherSubsMName, that.otherSubsMName) &&
                Objects.equals(externalOtherSubsId, that.externalOtherSubsId) &&
                Objects.equals(eobQualifierOtherSubsId, that.eobQualifierOtherSubsId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ptFNm, ptLNm, ptMNm, eobQualifierPtId, externalPtId, subsFNm, subsLNm, subsMNm, eobQualifierSubs, externalSubsId, correctedSubsFNm, correctedSubsLNm, correctedSubsMNm, correctedExternalSubsId, otherSubsFName, otherSubsLName, otherSubsMName, externalOtherSubsId, eobQualifierOtherSubsId);
    }

    @Override
    public String toString() {
        return "EobPhiRecord{" +
                "ptFNm='" + ptFNm + '\'' +
                ", ptLNm='" + ptLNm + '\'' +
                ", ptMNm='" + ptMNm + '\'' +
                ", eobQualifierPtId='" + eobQualifierPtId + '\'' +
                ", externalPtId='" + externalPtId + '\'' +
                ", subsFNm='" + subsFNm + '\'' +
                ", subsLNm='" + subsLNm + '\'' +
                ", subsMNm='" + subsMNm + '\'' +
                ", eobQualifierSubs='" + eobQualifierSubs + '\'' +
                ", externalSubsId='" + externalSubsId + '\'' +
                ", correctedSubsFNm='" + correctedSubsFNm + '\'' +
                ", correctedSubsLNm='" + correctedSubsLNm + '\'' +
                ", correctedSubsMNm='" + correctedSubsMNm + '\'' +
                ", correctedExternalSubsId='" + correctedExternalSubsId + '\'' +
                ", otherSubsFName='" + otherSubsFName + '\'' +
                ", otherSubsLName='" + otherSubsLName + '\'' +
                ", otherSubsMName='" + otherSubsMName + '\'' +
                ", externalOtherSubsId='" + externalOtherSubsId + '\'' +
                ", eobQualifierOtherSubsId='" + eobQualifierOtherSubsId + '\'' +
                '}';
    }
}
