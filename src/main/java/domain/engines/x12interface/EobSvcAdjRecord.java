package domain.engines.x12interface;

import java.util.Objects;

public class EobSvcAdjRecord {
    private int eobSvcPmtId;
    private String eobCodeAdjGrp;
    private String reasonCd;
    private String amtStr;
    private String quantity;

    public int getEobSvcPmtId() {
        return eobSvcPmtId;
    }

    public void setEobSvcPmtId(int eobSvcPmtId) {
        this.eobSvcPmtId = eobSvcPmtId;
    }

    public String getEobCodeAdjGrp() {
        return eobCodeAdjGrp;
    }

    public void setEobCodeAdjGrp(String eobCodeAdjGrp) {
        this.eobCodeAdjGrp = eobCodeAdjGrp;
    }

    public String getReasonCd() {
        return reasonCd;
    }

    public void setReasonCd(String reasonCd) {
        this.reasonCd = reasonCd;
    }

    public String getAmtStr() {
        return amtStr;
    }

    public void setAmtStr(String amtStr) {
        this.amtStr = amtStr;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobSvcAdjRecord that = (EobSvcAdjRecord) o;
        return eobSvcPmtId == that.eobSvcPmtId &&
                Objects.equals(eobCodeAdjGrp, that.eobCodeAdjGrp) &&
                Objects.equals(reasonCd, that.reasonCd) &&
                Objects.equals(amtStr, that.amtStr) &&
                Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {

        return Objects.hash(eobSvcPmtId, eobCodeAdjGrp, reasonCd, amtStr, quantity);
    }

    @Override
    public String toString() {
        return "EobSvcAdjRecord{" +
                "eobSvcPmtId=" + eobSvcPmtId +
                ", eobCodeAdjGrp='" + eobCodeAdjGrp + '\'' +
                ", reasonCd='" + reasonCd + '\'' +
                ", amtStr='" + amtStr + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
