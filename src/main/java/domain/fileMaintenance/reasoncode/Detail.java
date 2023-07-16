package domain.fileMaintenance.reasoncode;

import java.util.Objects;

public class Detail {
    private String shortDescription;
    private String description;
    private String field;
    private boolean isUseManualDemandFormatForReSubmit;
    private String claimStatusErrorType;
    private String abnRelatedReason;
    private String abnSignedFinalAction;
    private boolean isNoAct;
    private boolean isAutoMatch;
    private boolean isMatchCompare;
    private boolean isManual;
    private String correspondence;
    private String correspondenceText;
    private String outsideAgency;
    private String finalAction;
    private String adjustmentCode;
    private String collectionsSubmId;
    private String pyrId;
    private String errorLevel;
    private String outsideAgencyPreCorresp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Detail detail = (Detail) o;
        return isUseManualDemandFormatForReSubmit == detail.isUseManualDemandFormatForReSubmit &&
                isNoAct == detail.isNoAct &&
                isAutoMatch == detail.isAutoMatch &&
                isMatchCompare == detail.isMatchCompare &&
                isManual == detail.isManual &&
                Objects.equals(shortDescription, detail.shortDescription) &&
                Objects.equals(description, detail.description) &&
                Objects.equals(field, detail.field) &&
                Objects.equals(claimStatusErrorType, detail.claimStatusErrorType) &&
                Objects.equals(abnRelatedReason, detail.abnRelatedReason) &&
                Objects.equals(abnSignedFinalAction, detail.abnSignedFinalAction) &&
                Objects.equals(correspondence, detail.correspondence) &&
                Objects.equals(correspondenceText, detail.correspondenceText) &&
                Objects.equals(outsideAgency, detail.outsideAgency) &&
                Objects.equals(finalAction, detail.finalAction) &&
                Objects.equals(adjustmentCode, detail.adjustmentCode) &&
                Objects.equals(collectionsSubmId, detail.collectionsSubmId) &&
                Objects.equals(pyrId, detail.pyrId) &&
                Objects.equals(errorLevel, detail.errorLevel) &&
                Objects.equals(outsideAgencyPreCorresp, detail.outsideAgencyPreCorresp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(shortDescription, description, field, isUseManualDemandFormatForReSubmit, claimStatusErrorType, abnRelatedReason, abnSignedFinalAction, isNoAct, isAutoMatch, isMatchCompare, isManual, correspondence, correspondenceText, outsideAgency, finalAction, adjustmentCode, collectionsSubmId, pyrId, errorLevel, outsideAgencyPreCorresp);
    }

    public String getOutsideAgencyPreCorresp() {
        return outsideAgencyPreCorresp;

    }

    public void setOutsideAgencyPreCorresp(String outsideAgencyPreCorresp) {
        this.outsideAgencyPreCorresp = outsideAgencyPreCorresp;
    }

    public String getErrorLevel() {
        return errorLevel;
    }

    public void setErrorLevel(String errorLevel) {
        this.errorLevel = errorLevel;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", field='" + field + '\'' +
                ", isUseManualDemandFormatForReSubmit=" + isUseManualDemandFormatForReSubmit +
                ", claimStatusErrorType='" + claimStatusErrorType + '\'' +
                ", abnRelatedReason='" + abnRelatedReason + '\'' +
                ", abnSignedFinalAction='" + abnSignedFinalAction + '\'' +
                ", isNoAct=" + isNoAct +
                ", isAutoMatch=" + isAutoMatch +
                ", isMatchCompare=" + isMatchCompare +
                ", isManual=" + isManual +
                ", correspondence='" + correspondence + '\'' +
                ", correspondenceText='" + correspondenceText + '\'' +
                ", outsideAgency='" + outsideAgency + '\'' +
                ", finalAction='" + finalAction + '\'' +
                ", adjustmentCode='" + adjustmentCode + '\'' +
                ", collectionsSubmId='" + collectionsSubmId + '\'' +
                ", pyrId='" + pyrId + '\'' +
                ", errorLevel='" + errorLevel + '\'' +
                ", outsideAgencyPreCorresp='" + outsideAgencyPreCorresp + '\'' +
                '}';
    }



    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isUseManualDemandFormatForReSubmit() {
        return isUseManualDemandFormatForReSubmit;
    }

    public void setUseManualDemandFormatForReSubmit(boolean useManualDemandFormatForReSubmit) {
        isUseManualDemandFormatForReSubmit = useManualDemandFormatForReSubmit;
    }

    public String getClaimStatusErrorType() {
        return claimStatusErrorType;
    }

    public void setClaimStatusErrorType(String claimStatusErrorType) {
        this.claimStatusErrorType = claimStatusErrorType;
    }

    public String getAbnRelatedReason() {
        return abnRelatedReason;
    }

    public void setAbnRelatedReason(String abnRelatedReason) {
        this.abnRelatedReason = abnRelatedReason;
    }

    public String getAbnSignedFinalAction() {
        return abnSignedFinalAction;
    }

    public void setAbnSignedFinalAction(String abnSignedFinalAction) {
        this.abnSignedFinalAction = abnSignedFinalAction;
    }

    public boolean isNoAct() {
        return isNoAct;
    }

    public void setNoAct(boolean noAct) {
        isNoAct = noAct;
    }

    public boolean isAutoMatch() {
        return isAutoMatch;
    }

    public void setAutoMatch(boolean autoMatch) {
        isAutoMatch = autoMatch;
    }

    public boolean isMatchCompare() {
        return isMatchCompare;
    }

    public void setMatchCompare(boolean matchCompare) {
        isMatchCompare = matchCompare;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setManual(boolean manual) {
        isManual = manual;
    }

    public String getCorrespondence() {
        return correspondence;
    }

    public void setCorrespondence(String correspondence) {
        this.correspondence = correspondence;
    }

    public String getCorrespondenceText() {
        return correspondenceText;
    }

    public void setCorrespondenceText(String correspondenceText) {
        this.correspondenceText = correspondenceText;
    }

    public String getOutsideAgency() {
        return outsideAgency;
    }

    public void setOutsideAgency(String outsideAgency) {
        this.outsideAgency = outsideAgency;
    }

    public String getFinalAction() {
        return finalAction;
    }

    public void setFinalAction(String finalAction) {
        this.finalAction = finalAction;
    }

    public String getAdjustmentCode() {
        return adjustmentCode;
    }

    public void setAdjustmentCode(String adjustmentCode) {
        this.adjustmentCode = adjustmentCode;
    }

    public String getCollectionsSubmId() {
        return collectionsSubmId;
    }

    public void setCollectionsSubmId(String collectionsSubmId) {
        this.collectionsSubmId = collectionsSubmId;
    }

    public String getPyrId() {
        return pyrId;
    }

    public void setPyrId(String pyrId) {
        this.pyrId = pyrId;
    }

}
