package domain.engines.x12interface;

import java.util.Objects;

public class EobClaimRecord {
    private String externalCtrlId;
    private String eobCodeClaimStatus;
    private String totChargeAmtStr;
    private String totPaidAmtStr;
    private String ptRespAmtStr;
    private String eobCodeClaimFiling;
    private String internalCtrlId;
    private String eobCodeFac;
    private int eobProvSummaryId;
    private String eobCodeClaimFrequency;
    private String ServiceProvName;
    private String eobQualifierSvcProvId;
    private String serviceProvExternalId;
    private String crossoverCarrierName;
    private String eobQualifierCrossoverId;
    private String crossoverCarrierExternalId;
    private String receivedByPyrDtStr;
    private String stmtPeriodStartDtStr;
    private String stmtPeriodEndDtStr;
    private String contactName;
    private String eobQualifierClmCntctNum;
    private String contactNumber;
    private String correctedPyrPrioName;
    private String eobQualCorrectPyrPrio;
    private String correctedPyrPrioId;
    private boolean isExcluded;
    private String reimbursementRate;
    private String hcpcsPayableAmtStr;
    private int pyrId;
    private String accnId;
    private boolean isProcessed;
    private String errorNote;
    private boolean isDiscrepancy;
    private int pyrPrio;
    private int reconcileStageId;
    private String drgCode;
    private String drgWeight;
    private String dischargeFraction;
    private String coverageExpDt;

    public String getStmtPeriodStartDtStr() {
        return stmtPeriodStartDtStr;
    }

    public void setStmtPeriodStartDtStr(String stmtPeriodStartDtStr) {
        this.stmtPeriodStartDtStr = stmtPeriodStartDtStr;
    }

    public String getExternalCtrlId() {
        return externalCtrlId;
    }

    public void setExternalCtrlId(String externalCtrlId) {
        this.externalCtrlId = externalCtrlId;
    }

    public String getEobCodeClaimStatus() {
        return eobCodeClaimStatus;
    }

    public void setEobCodeClaimStatus(String eobCodeClaimStatus) {
        this.eobCodeClaimStatus = eobCodeClaimStatus;
    }

    public String getTotChargeAmtStr() {
        return totChargeAmtStr;
    }

    public void setTotChargeAmtStr(String totChargeAmtStr) {
        this.totChargeAmtStr = totChargeAmtStr;
    }

    public String getTotPaidAmtStr() {
        return totPaidAmtStr;
    }

    public void setTotPaidAmtStr(String totPaidAmtStr) {
        this.totPaidAmtStr = totPaidAmtStr;
    }

    public String getPtRespAmtStr() {
        return ptRespAmtStr;
    }

    public void setPtRespAmtStr(String ptRespAmtStr) {
        this.ptRespAmtStr = ptRespAmtStr;
    }

    public String getEobCodeClaimFiling() {
        return eobCodeClaimFiling;
    }

    public void setEobCodeClaimFiling(String eobCodeClaimFiling) {
        this.eobCodeClaimFiling = eobCodeClaimFiling;
    }

    public String getInternalCtrlId() {
        return internalCtrlId;
    }

    public void setInternalCtrlId(String internalCtrlId) {
        this.internalCtrlId = internalCtrlId;
    }

    public String getEobCodeFac() {
        return eobCodeFac;
    }

    public void setEobCodeFac(String eobCodeFac) {
        this.eobCodeFac = eobCodeFac;
    }

    public int getEobProvSummaryId() {
        return eobProvSummaryId;
    }

    public void setEobProvSummaryId(int eobProvSummaryId) {
        this.eobProvSummaryId = eobProvSummaryId;
    }

    public String getEobCodeClaimFrequency() {
        return eobCodeClaimFrequency;
    }

    public void setEobCodeClaimFrequency(String eobCodeClaimFrequency) {
        this.eobCodeClaimFrequency = eobCodeClaimFrequency;
    }

    public String getServiceProvName() {
        return ServiceProvName;
    }

    public void setServiceProvName(String serviceProvName) {
        ServiceProvName = serviceProvName;
    }

    public String getEobQualifierSvcProvId() {
        return eobQualifierSvcProvId;
    }

    public void setEobQualifierSvcProvId(String eobQualifierSvcProvId) {
        this.eobQualifierSvcProvId = eobQualifierSvcProvId;
    }

    public String getServiceProvExternalId() {
        return serviceProvExternalId;
    }

    public void setServiceProvExternalId(String serviceProvExternalId) {
        this.serviceProvExternalId = serviceProvExternalId;
    }

    public String getCrossoverCarrierName() {
        return crossoverCarrierName;
    }

    public void setCrossoverCarrierName(String crossoverCarrierName) {
        this.crossoverCarrierName = crossoverCarrierName;
    }

    public String getEobQualifierCrossoverId() {
        return eobQualifierCrossoverId;
    }

    public void setEobQualifierCrossoverId(String eobQualifierCrossoverId) {
        this.eobQualifierCrossoverId = eobQualifierCrossoverId;
    }

    public String getCrossoverCarrierExternalId() {
        return crossoverCarrierExternalId;
    }

    public void setCrossoverCarrierExternalId(String crossoverCarrierExternalId) {
        this.crossoverCarrierExternalId = crossoverCarrierExternalId;
    }

    public String getReceivedByPyrDtStr() {
        return receivedByPyrDtStr;
    }

    public void setReceivedByPyrDtStr(String receivedByPyrDtStr) {
        this.receivedByPyrDtStr = receivedByPyrDtStr;
    }

    public String getStmtPeriodEndDtStr() {
        return stmtPeriodEndDtStr;
    }

    public void setStmtPeriodEndDtStr(String stmtPeriodEndDtStr) {
        this.stmtPeriodEndDtStr = stmtPeriodEndDtStr;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEobQualifierClmCntctNum() {
        return eobQualifierClmCntctNum;
    }

    public void setEobQualifierClmCntctNum(String eobQualifierClmCntctNum) {
        this.eobQualifierClmCntctNum = eobQualifierClmCntctNum;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCorrectedPyrPrioName() {
        return correctedPyrPrioName;
    }

    public void setCorrectedPyrPrioName(String correctedPyrPrioName) {
        this.correctedPyrPrioName = correctedPyrPrioName;
    }

    public String getEobQualCorrectPyrPrio() {
        return eobQualCorrectPyrPrio;
    }

    public void setEobQualCorrectPyrPrio(String eobQualCorrectPyrPrio) {
        this.eobQualCorrectPyrPrio = eobQualCorrectPyrPrio;
    }

    public String getCorrectedPyrPrioId() {
        return correctedPyrPrioId;
    }

    public void setCorrectedPyrPrioId(String correctedPyrPrioId) {
        this.correctedPyrPrioId = correctedPyrPrioId;
    }

    public boolean isExcluded() {
        return isExcluded;
    }

    public void setExcluded(boolean excluded) {
        isExcluded = excluded;
    }

    public String getReimbursementRate() {
        return reimbursementRate;
    }

    public void setReimbursementRate(String reimbursementRate) {
        this.reimbursementRate = reimbursementRate;
    }

    public String getHcpcsPayableAmtStr() {
        return hcpcsPayableAmtStr;
    }

    public void setHcpcsPayableAmtStr(String hcpcsPayableAmtStr) {
        this.hcpcsPayableAmtStr = hcpcsPayableAmtStr;
    }

    public int getPyrId() {
        return pyrId;
    }

    public void setPyrId(int pyrId) {
        this.pyrId = pyrId;
    }

    public String getAccnId() {
        return accnId;
    }

    public void setAccnId(String accnId) {
        this.accnId = accnId;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public String getErrorNote() {
        return errorNote;
    }

    public void setErrorNote(String errorNote) {
        this.errorNote = errorNote;
    }

    public boolean isDiscrepancy() {
        return isDiscrepancy;
    }

    public void setDiscrepancy(boolean discrepancy) {
        isDiscrepancy = discrepancy;
    }

    public int getPyrPrio() {
        return pyrPrio;
    }

    public void setPyrPrio(int pyrPrio) {
        this.pyrPrio = pyrPrio;
    }

    public int getReconcileStageId() {
        return reconcileStageId;
    }

    public void setReconcileStageId(int reconcileStageId) {
        this.reconcileStageId = reconcileStageId;
    }

    public String getDrgCode() {
        return drgCode;
    }

    public void setDrgCode(String drgCode) {
        this.drgCode = drgCode;
    }

    public String getDrgWeight() {
        return drgWeight;
    }

    public void setDrgWeight(String drgWeight) {
        this.drgWeight = drgWeight;
    }

    public String getDischargeFraction() {
        return dischargeFraction;
    }

    public void setDischargeFraction(String dischargeFraction) {
        this.dischargeFraction = dischargeFraction;
    }

    public String getCoverageExpDt() {
        return coverageExpDt;
    }

    public void setCoverageExpDt(String coverageExpDt) {
        this.coverageExpDt = coverageExpDt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobClaimRecord that = (EobClaimRecord) o;
        return eobProvSummaryId == that.eobProvSummaryId &&
                isExcluded == that.isExcluded &&
                pyrId == that.pyrId &&
                isProcessed == that.isProcessed &&
                isDiscrepancy == that.isDiscrepancy &&
                pyrPrio == that.pyrPrio &&
                reconcileStageId == that.reconcileStageId &&
                Objects.equals(externalCtrlId, that.externalCtrlId) &&
                Objects.equals(eobCodeClaimStatus, that.eobCodeClaimStatus) &&
                Objects.equals(totChargeAmtStr, that.totChargeAmtStr) &&
                Objects.equals(totPaidAmtStr, that.totPaidAmtStr) &&
                Objects.equals(ptRespAmtStr, that.ptRespAmtStr) &&
                Objects.equals(eobCodeClaimFiling, that.eobCodeClaimFiling) &&
                Objects.equals(internalCtrlId, that.internalCtrlId) &&
                Objects.equals(eobCodeFac, that.eobCodeFac) &&
                Objects.equals(eobCodeClaimFrequency, that.eobCodeClaimFrequency) &&
                Objects.equals(ServiceProvName, that.ServiceProvName) &&
                Objects.equals(eobQualifierSvcProvId, that.eobQualifierSvcProvId) &&
                Objects.equals(serviceProvExternalId, that.serviceProvExternalId) &&
                Objects.equals(crossoverCarrierName, that.crossoverCarrierName) &&
                Objects.equals(eobQualifierCrossoverId, that.eobQualifierCrossoverId) &&
                Objects.equals(crossoverCarrierExternalId, that.crossoverCarrierExternalId) &&
                Objects.equals(receivedByPyrDtStr, that.receivedByPyrDtStr) &&
                Objects.equals(stmtPeriodStartDtStr, that.stmtPeriodStartDtStr) &&
                Objects.equals(stmtPeriodEndDtStr, that.stmtPeriodEndDtStr) &&
                Objects.equals(contactName, that.contactName) &&
                Objects.equals(eobQualifierClmCntctNum, that.eobQualifierClmCntctNum) &&
                Objects.equals(contactNumber, that.contactNumber) &&
                Objects.equals(correctedPyrPrioName, that.correctedPyrPrioName) &&
                Objects.equals(eobQualCorrectPyrPrio, that.eobQualCorrectPyrPrio) &&
                Objects.equals(correctedPyrPrioId, that.correctedPyrPrioId) &&
                Objects.equals(reimbursementRate, that.reimbursementRate) &&
                Objects.equals(hcpcsPayableAmtStr, that.hcpcsPayableAmtStr) &&
                Objects.equals(accnId, that.accnId) &&
                Objects.equals(errorNote, that.errorNote) &&
                Objects.equals(drgCode, that.drgCode) &&
                Objects.equals(drgWeight, that.drgWeight) &&
                Objects.equals(dischargeFraction, that.dischargeFraction) &&
                Objects.equals(coverageExpDt, that.coverageExpDt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(externalCtrlId, eobCodeClaimStatus, totChargeAmtStr, totPaidAmtStr, ptRespAmtStr, eobCodeClaimFiling, internalCtrlId, eobCodeFac, eobProvSummaryId, eobCodeClaimFrequency, ServiceProvName, eobQualifierSvcProvId, serviceProvExternalId, crossoverCarrierName, eobQualifierCrossoverId, crossoverCarrierExternalId, receivedByPyrDtStr, stmtPeriodStartDtStr, stmtPeriodEndDtStr, contactName, eobQualifierClmCntctNum, contactNumber, correctedPyrPrioName, eobQualCorrectPyrPrio, correctedPyrPrioId, isExcluded, reimbursementRate, hcpcsPayableAmtStr, pyrId, accnId, isProcessed, errorNote, isDiscrepancy, pyrPrio, reconcileStageId, drgCode, drgWeight, dischargeFraction, coverageExpDt);
    }

    @Override
    public String toString() {
        return "EobClaimRecord{" +
                "externalCtrlId='" + externalCtrlId + '\'' +
                ", eobCodeClaimStatus='" + eobCodeClaimStatus + '\'' +
                ", totChargeAmtStr='" + totChargeAmtStr + '\'' +
                ", totPaidAmtStr='" + totPaidAmtStr + '\'' +
                ", ptRespAmtStr='" + ptRespAmtStr + '\'' +
                ", eobCodeClaimFiling='" + eobCodeClaimFiling + '\'' +
                ", internalCtrlId='" + internalCtrlId + '\'' +
                ", eobCodeFac='" + eobCodeFac + '\'' +
                ", eobProvSummaryId=" + eobProvSummaryId +
                ", eobCodeClaimFrequency='" + eobCodeClaimFrequency + '\'' +
                ", ServiceProvName='" + ServiceProvName + '\'' +
                ", eobQualifierSvcProvId='" + eobQualifierSvcProvId + '\'' +
                ", serviceProvExternalId='" + serviceProvExternalId + '\'' +
                ", crossoverCarrierName='" + crossoverCarrierName + '\'' +
                ", eobQualifierCrossoverId='" + eobQualifierCrossoverId + '\'' +
                ", crossoverCarrierExternalId='" + crossoverCarrierExternalId + '\'' +
                ", receivedByPyrDtStr='" + receivedByPyrDtStr + '\'' +
                ", stmtPeriodStartDtStr='" + stmtPeriodStartDtStr + '\'' +
                ", stmtPeriodEndDtStr='" + stmtPeriodEndDtStr + '\'' +
                ", contactName='" + contactName + '\'' +
                ", eobQualifierClmCntctNum='" + eobQualifierClmCntctNum + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", correctedPyrPrioName='" + correctedPyrPrioName + '\'' +
                ", eobQualCorrectPyrPrio='" + eobQualCorrectPyrPrio + '\'' +
                ", correctedPyrPrioId='" + correctedPyrPrioId + '\'' +
                ", isExcluded=" + isExcluded +
                ", reimbursementRate='" + reimbursementRate + '\'' +
                ", hcpcsPayableAmtStr='" + hcpcsPayableAmtStr + '\'' +
                ", pyrId=" + pyrId +
                ", accnId='" + accnId + '\'' +
                ", isProcessed=" + isProcessed +
                ", errorNote='" + errorNote + '\'' +
                ", isDiscrepancy=" + isDiscrepancy +
                ", pyrPrio=" + pyrPrio +
                ", reconcileStageId=" + reconcileStageId +
                ", drgCode='" + drgCode + '\'' +
                ", drgWeight='" + drgWeight + '\'' +
                ", dischargeFraction='" + dischargeFraction + '\'' +
                ", coverageExpDt='" + coverageExpDt + '\'' +
                '}';
    }
}
