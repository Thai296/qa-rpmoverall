package domain.engines.x12interface;

import java.util.Objects;

public class EobSvcPmtRecord {
    private String procTypQualifier;
    private String procCd;
    private String proCdMod1;
    private String proCdMod2;
    private String proCdMod3;
    private String proCdMod4;
    private String procCdDescr;
    private String submittedCharge;
    private String paidAmtStr;
    private String revenueCd;
    private String units;
    private String altProcTypQualifier;
    private String altProcCd;
    private String altProcCdMod1;
    private String altProcCdMod2;
    private String altProcCdMod3;
    private String altProcCdMod4;
    private String altUnits;
    private String svcStartDtStr;
    private String svcEndDtStr;
    private String singleDaySvcDtStr;
    private String providerControlId;
    private String locationNumber;
    private int accnProcSeqId;


    public String getProcTypQualifier() {
        return procTypQualifier;
    }

    public void setProcTypQualifier(String procTypQualifier) {
        this.procTypQualifier = procTypQualifier;
    }

    public String getProcCd() {
        return procCd;
    }

    public void setProcCd(String procCd) {
        this.procCd = procCd;
    }

    public String getProCdMod1() {
        return proCdMod1;
    }

    public void setProCdMod1(String proCdMod1) {
        this.proCdMod1 = proCdMod1;
    }

    public String getProCdMod2() {
        return proCdMod2;
    }

    public void setProCdMod2(String proCdMod2) {
        this.proCdMod2 = proCdMod2;
    }

    public String getProCdMod3() {
        return proCdMod3;
    }

    public void setProCdMod3(String proCdMod3) {
        this.proCdMod3 = proCdMod3;
    }

    public String getProCdMod4() {
        return proCdMod4;
    }

    public void setProCdMod4(String proCdMod4) {
        this.proCdMod4 = proCdMod4;
    }

    public String getProcCdDescr() {
        return procCdDescr;
    }

    public void setProcCdDescr(String procCdDescr) {
        this.procCdDescr = procCdDescr;
    }

    public String getSubmittedCharge() {
        return submittedCharge;
    }

    public void setSubmittedCharge(String submittedCharge) {
        this.submittedCharge = submittedCharge;
    }

    public String getPaidAmtStr() {
        return paidAmtStr;
    }

    public void setPaidAmtStr(String paidAmtStr) {
        this.paidAmtStr = paidAmtStr;
    }

    public String getRevenueCd() {
        return revenueCd;
    }

    public void setRevenueCd(String revenueCd) {
        this.revenueCd = revenueCd;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getAltProcTypQualifier() {
        return altProcTypQualifier;
    }

    public void setAltProcTypQualifier(String altProcTypQualifier) {
        this.altProcTypQualifier = altProcTypQualifier;
    }

    public String getAltProcCd() {
        return altProcCd;
    }

    public void setAltProcCd(String altProcCd) {
        this.altProcCd = altProcCd;
    }

    public String getAltProcCdMod1() {
        return altProcCdMod1;
    }

    public void setAltProcCdMod1(String altProcCdMod1) {
        this.altProcCdMod1 = altProcCdMod1;
    }

    public String getAltProcCdMod2() {
        return altProcCdMod2;
    }

    public void setAltProcCdMod2(String altProcCdMod2) {
        this.altProcCdMod2 = altProcCdMod2;
    }

    public String getAltProcCdMod3() {
        return altProcCdMod3;
    }

    public void setAltProcCdMod3(String altProcCdMod3) {
        this.altProcCdMod3 = altProcCdMod3;
    }

    public String getAltProcCdMod4() {
        return altProcCdMod4;
    }

    public void setAltProcCdMod4(String altProcCdMod4) {
        this.altProcCdMod4 = altProcCdMod4;
    }

    public String getAltUnits() {
        return altUnits;
    }

    public void setAltUnits(String altUnits) {
        this.altUnits = altUnits;
    }

    public String getSvcStartDtStr() {
        return svcStartDtStr;
    }

    public void setSvcStartDtStr(String svcStartDtStr) {
        this.svcStartDtStr = svcStartDtStr;
    }

    public String getSvcEndDtStr() {
        return svcEndDtStr;
    }

    public void setSvcEndDtStr(String svcEndDtStr) {
        this.svcEndDtStr = svcEndDtStr;
    }

    public String getSingleDaySvcDtStr() {
        return singleDaySvcDtStr;
    }

    public void setSingleDaySvcDtStr(String singleDaySvcDtStr) {
        this.singleDaySvcDtStr = singleDaySvcDtStr;
    }

    public String getProviderControlId() {
        return providerControlId;
    }

    public void setProviderControlId(String providerControlId) {
        this.providerControlId = providerControlId;
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    public int getAccnProcSeqId() {
        return accnProcSeqId;
    }

    public void setAccnProcSeqId(int accnProcSeqId) {
        this.accnProcSeqId = accnProcSeqId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobSvcPmtRecord that = (EobSvcPmtRecord) o;
        return accnProcSeqId == that.accnProcSeqId &&
                Objects.equals(procTypQualifier, that.procTypQualifier) &&
                Objects.equals(procCd, that.procCd) &&
                Objects.equals(proCdMod1, that.proCdMod1) &&
                Objects.equals(proCdMod2, that.proCdMod2) &&
                Objects.equals(proCdMod3, that.proCdMod3) &&
                Objects.equals(proCdMod4, that.proCdMod4) &&
                Objects.equals(procCdDescr, that.procCdDescr) &&
                Objects.equals(submittedCharge, that.submittedCharge) &&
                Objects.equals(paidAmtStr, that.paidAmtStr) &&
                Objects.equals(revenueCd, that.revenueCd) &&
                Objects.equals(units, that.units) &&
                Objects.equals(altProcTypQualifier, that.altProcTypQualifier) &&
                Objects.equals(altProcCd, that.altProcCd) &&
                Objects.equals(altProcCdMod1, that.altProcCdMod1) &&
                Objects.equals(altProcCdMod2, that.altProcCdMod2) &&
                Objects.equals(altProcCdMod3, that.altProcCdMod3) &&
                Objects.equals(altProcCdMod4, that.altProcCdMod4) &&
                Objects.equals(altUnits, that.altUnits) &&
                Objects.equals(svcStartDtStr, that.svcStartDtStr) &&
                Objects.equals(svcEndDtStr, that.svcEndDtStr) &&
                Objects.equals(singleDaySvcDtStr, that.singleDaySvcDtStr) &&
                Objects.equals(providerControlId, that.providerControlId) &&
                Objects.equals(locationNumber, that.locationNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(procTypQualifier, procCd, proCdMod1, proCdMod2, proCdMod3, proCdMod4, procCdDescr, submittedCharge, paidAmtStr, revenueCd, units, altProcTypQualifier, altProcCd, altProcCdMod1, altProcCdMod2, altProcCdMod3, altProcCdMod4, altUnits, svcStartDtStr, svcEndDtStr, singleDaySvcDtStr, providerControlId, locationNumber, accnProcSeqId);
    }

    @Override
    public String toString() {
        return "EobSvcPmtRecord{" +
                "procTypQualifier='" + procTypQualifier + '\'' +
                ", procCd='" + procCd + '\'' +
                ", proCdMod1='" + proCdMod1 + '\'' +
                ", proCdMod2='" + proCdMod2 + '\'' +
                ", proCdMod3='" + proCdMod3 + '\'' +
                ", proCdMod4='" + proCdMod4 + '\'' +
                ", procCdDescr='" + procCdDescr + '\'' +
                ", submittedCharge='" + submittedCharge + '\'' +
                ", paidAmtStr='" + paidAmtStr + '\'' +
                ", revenueCd='" + revenueCd + '\'' +
                ", units='" + units + '\'' +
                ", altProcTypQualifier='" + altProcTypQualifier + '\'' +
                ", altProcCd='" + altProcCd + '\'' +
                ", altProcCdMod1='" + altProcCdMod1 + '\'' +
                ", altProcCdMod2='" + altProcCdMod2 + '\'' +
                ", altProcCdMod3='" + altProcCdMod3 + '\'' +
                ", altProcCdMod4='" + altProcCdMod4 + '\'' +
                ", altUnits='" + altUnits + '\'' +
                ", svcStartDtStr='" + svcStartDtStr + '\'' +
                ", svcEndDtStr='" + svcEndDtStr + '\'' +
                ", singleDaySvcDtStr='" + singleDaySvcDtStr + '\'' +
                ", providerControlId='" + providerControlId + '\'' +
                ", locationNumber='" + locationNumber + '\'' +
                ", accnProcSeqId=" + accnProcSeqId +
                '}';
    }
}
