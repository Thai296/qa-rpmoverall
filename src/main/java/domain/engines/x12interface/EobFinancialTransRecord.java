package domain.engines.x12interface;

import java.util.Objects;

public class EobFinancialTransRecord {
    private String eobCodeTransHandling;
    private String transAmtStr;
    private String eobCodePmtMethod;
    private String effectiveDtStr;
    private boolean isReleased;
    private String creditOrDebit;
    private String payorsBankId;
    private String payorsBankAcctNum;
    private String providersBankId;
    private String providersBankAcctId;
    private String eftTranceNum;
    private String receiverId;
    private String versionIdCd;
    private String productionDtStr;
    private int eobFinancialTransSrcTyp;
    private int reconcileStageId;
    private int dupId;

    public String getProvidersBankId() {
        return providersBankId;
    }

    public void setProvidersBankId(String providersBankId) {
        this.providersBankId = providersBankId;
    }

    public String getEobCodeTransHandling() {
        return eobCodeTransHandling;
    }

    public void setEobCodeTransHandling(String eobCodeTransHandling) {
        this.eobCodeTransHandling = eobCodeTransHandling;
    }

    public String getTransAmtStr() {
        return transAmtStr;
    }

    public void setTransAmtStr(String transAmtStr) {
        this.transAmtStr = transAmtStr;
    }

    public String getEobCodePmtMethod() {
        return eobCodePmtMethod;
    }

    public void setEobCodePmtMethod(String eobCodePmtMethod) {
        this.eobCodePmtMethod = eobCodePmtMethod;
    }

    public String getEffectiveDtStr() {
        return effectiveDtStr;
    }

    public void setEffectiveDtStr(String effectiveDtStr) {
        this.effectiveDtStr = effectiveDtStr;
    }

    public boolean isReleased() {
        return isReleased;
    }

    public void setReleased(boolean released) {
        isReleased = released;
    }

    public String getCreditOrDebit() {
        return creditOrDebit;
    }

    public void setCreditOrDebit(String creditOrDebit) {
        this.creditOrDebit = creditOrDebit;
    }

    public String getPayorsBankId() {
        return payorsBankId;
    }

    public void setPayorsBankId(String payorsBankId) {
        this.payorsBankId = payorsBankId;
    }

    public String getPayorsBankAcctNum() {
        return payorsBankAcctNum;
    }

    public void setPayorsBankAcctNum(String payorsBankAcctNum) {
        this.payorsBankAcctNum = payorsBankAcctNum;
    }

    public String getProvidersBankAcctId() {
        return providersBankAcctId;
    }

    public void setProvidersBankAcctId(String providersBankAcctId) {
        this.providersBankAcctId = providersBankAcctId;
    }

    public String getEftTranceNum() {
        return eftTranceNum;
    }

    public void setEftTranceNum(String eftTranceNum) {
        this.eftTranceNum = eftTranceNum;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getVersionIdCd() {
        return versionIdCd;
    }

    public void setVersionIdCd(String versionIdCd) {
        this.versionIdCd = versionIdCd;
    }

    public String getProductionDtStr() {
        return productionDtStr;
    }

    public void setProductionDtStr(String productionDtStr) {
        this.productionDtStr = productionDtStr;
    }

    public int getEobFinancialTransSrcTyp() {
        return eobFinancialTransSrcTyp;
    }

    public void setEobFinancialTransSrcTyp(int eobFinancialTransSrcTyp) {
        this.eobFinancialTransSrcTyp = eobFinancialTransSrcTyp;
    }

    public int getReconcileStageId() {
        return reconcileStageId;
    }

    public void setReconcileStageId(int reconcileStageId) {
        this.reconcileStageId = reconcileStageId;
    }

    public int getDupId() {
        return dupId;
    }

    public void setDupId(int dupId) {
        this.dupId = dupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobFinancialTransRecord that = (EobFinancialTransRecord) o;
        return isReleased == that.isReleased &&
                eobFinancialTransSrcTyp == that.eobFinancialTransSrcTyp &&
                reconcileStageId == that.reconcileStageId &&
                dupId == that.dupId &&
                Objects.equals(eobCodeTransHandling, that.eobCodeTransHandling) &&
                Objects.equals(transAmtStr, that.transAmtStr) &&
                Objects.equals(eobCodePmtMethod, that.eobCodePmtMethod) &&
                Objects.equals(effectiveDtStr, that.effectiveDtStr) &&
                Objects.equals(creditOrDebit, that.creditOrDebit) &&
                Objects.equals(payorsBankId, that.payorsBankId) &&
                Objects.equals(payorsBankAcctNum, that.payorsBankAcctNum) &&
                Objects.equals(providersBankId, that.providersBankId) &&
                Objects.equals(providersBankAcctId, that.providersBankAcctId) &&
                Objects.equals(eftTranceNum, that.eftTranceNum) &&
                Objects.equals(receiverId, that.receiverId) &&
                Objects.equals(versionIdCd, that.versionIdCd) &&
                Objects.equals(productionDtStr, that.productionDtStr);
    }

    @Override
    public int hashCode() {

        return Objects.hash(eobCodeTransHandling, transAmtStr, eobCodePmtMethod, effectiveDtStr, isReleased, creditOrDebit, payorsBankId, payorsBankAcctNum, providersBankId, providersBankAcctId, eftTranceNum, receiverId, versionIdCd, productionDtStr, eobFinancialTransSrcTyp, reconcileStageId, dupId);
    }

    @Override
    public String toString() {
        return "EobFinancialTransRecord{" +
                "eobCodeTransHandling='" + eobCodeTransHandling + '\'' +
                ", transAmtStr='" + transAmtStr + '\'' +
                ", eobCodePmtMethod='" + eobCodePmtMethod + '\'' +
                ", effectiveDtStr='" + effectiveDtStr + '\'' +
                ", isReleased=" + isReleased +
                ", creditOrDebit='" + creditOrDebit + '\'' +
                ", payorsBankId='" + payorsBankId + '\'' +
                ", payorsBackAcctNum='" + payorsBankAcctNum + '\'' +
                ", providersBankId='" + providersBankId + '\'' +
                ", providersBankAcctId='" + providersBankAcctId + '\'' +
                ", eftTranceNum='" + eftTranceNum + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", versionIdCd='" + versionIdCd + '\'' +
                ", productionDtStr='" + productionDtStr + '\'' +
                ", eobFinancialTransSrcTyp=" + eobFinancialTransSrcTyp +
                ", reconcileStageId=" + reconcileStageId +
                ", dupId=" + dupId +
                '}';
    }
}
