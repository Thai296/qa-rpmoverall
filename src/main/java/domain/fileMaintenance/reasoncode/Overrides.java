package domain.fileMaintenance.reasoncode;

import java.util.Objects;

public class Overrides {
    private String payorId;
    private String payorName;
    private String payorGroup;
    private String clientId;
    private String clientName;
    private String clientAccountType;
    private String procCode;
    private boolean isManFmtForReSubmit;
    private boolean isAutoMatch;
    private boolean isMatchCompare;
    private boolean isManual;
    private String correspondence;
    private String correspondenceText;
    private String outsideAgency;
    private String finalAction;
    private String authorizedBy;
    private String adjustmentCode;
    private String submSvc;
    private String specificPayorId;
    private String outsideAgencyPreCorresp;

    public String getOutsideAgencyPreCorresp() {
        return outsideAgencyPreCorresp;
    }

    public void setOutsideAgencyPreCorresp(String outsideAgencyPreCorresp) {
        this.outsideAgencyPreCorresp = outsideAgencyPreCorresp;
    }

    public String getPayorId() {
        return payorId;
    }

    public void setPayorId(String payorId) {
        this.payorId = payorId;
    }

    public String getPayorName() {
        return payorName;
    }

    public void setPayorName(String payorName) {
        this.payorName = payorName;
    }

    public String getPayorGroup() {
        return payorGroup;
    }

    public void setPayorGroup(String payorGroup) {
        this.payorGroup = payorGroup;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAccountType() {
        return clientAccountType;
    }

    public void setClientAccountType(String clientAccountType) {
        this.clientAccountType = clientAccountType;
    }

    public String getProcCode() {
        return procCode;
    }

    public void setProcCode(String procCode) {
        this.procCode = procCode;
    }

    public boolean isManFmtForReSubmit() {
        return isManFmtForReSubmit;
    }

    public void setManFmtForReSubmit(boolean manFmtForReSubmit) {
        isManFmtForReSubmit = manFmtForReSubmit;
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

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAdjustmentCode() {
        return adjustmentCode;
    }

    public void setAdjustmentCode(String adjustmentCode) {
        this.adjustmentCode = adjustmentCode;
    }

    public String getSubmSvc() {
        return submSvc;
    }

    public void setSubmSvc(String submSvc) {
        this.submSvc = submSvc;
    }

    public String getSpecificPayorId() {
        return specificPayorId;
    }

    public void setSpecificPayorId(String specificPayorId) {
        this.specificPayorId = specificPayorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Overrides overrides = (Overrides) o;
        return isManFmtForReSubmit == overrides.isManFmtForReSubmit &&
                isAutoMatch == overrides.isAutoMatch &&
                isMatchCompare == overrides.isMatchCompare &&
                isManual == overrides.isManual &&
                Objects.equals(payorId, overrides.payorId) &&
                Objects.equals(payorName, overrides.payorName) &&
                Objects.equals(payorGroup, overrides.payorGroup) &&
                Objects.equals(clientId, overrides.clientId) &&
                Objects.equals(clientName, overrides.clientName) &&
                Objects.equals(clientAccountType, overrides.clientAccountType) &&
                Objects.equals(procCode, overrides.procCode) &&
                Objects.equals(correspondence, overrides.correspondence) &&
                Objects.equals(correspondenceText, overrides.correspondenceText) &&
                Objects.equals(outsideAgency, overrides.outsideAgency) &&
                Objects.equals(finalAction, overrides.finalAction) &&
                Objects.equals(authorizedBy, overrides.authorizedBy) &&
                Objects.equals(adjustmentCode, overrides.adjustmentCode) &&
                Objects.equals(submSvc, overrides.submSvc) &&
                Objects.equals(specificPayorId, overrides.specificPayorId) &&
                Objects.equals(outsideAgencyPreCorresp, overrides.outsideAgencyPreCorresp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(payorId, payorName, payorGroup, clientId, clientName, clientAccountType, procCode, isManFmtForReSubmit, isAutoMatch, isMatchCompare, isManual, correspondence, correspondenceText, outsideAgency, finalAction, authorizedBy, adjustmentCode, submSvc, specificPayorId, outsideAgencyPreCorresp);
    }

    @Override
    public String toString() {
        return "Overrides{" +
                "payorId='" + payorId + '\'' +
                ", payorName='" + payorName + '\'' +
                ", payorGroup='" + payorGroup + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientAccountType='" + clientAccountType + '\'' +
                ", procCode='" + procCode + '\'' +
                ", isManFmtForReSubmit=" + isManFmtForReSubmit +
                ", isAutoMatch=" + isAutoMatch +
                ", isMatchCompare=" + isMatchCompare +
                ", isManual=" + isManual +
                ", correspondence='" + correspondence + '\'' +
                ", correspondenceText='" + correspondenceText + '\'' +
                ", outsideAgency='" + outsideAgency + '\'' +
                ", finalAction='" + finalAction + '\'' +
                ", authorizedBy='" + authorizedBy + '\'' +
                ", adjustmentCode='" + adjustmentCode + '\'' +
                ", submSvc='" + submSvc + '\'' +
                ", specificPayorId='" + specificPayorId + '\'' +
                ", outsideAgencyPreCorresp='" + outsideAgencyPreCorresp + '\'' +
                '}';
    }
}
