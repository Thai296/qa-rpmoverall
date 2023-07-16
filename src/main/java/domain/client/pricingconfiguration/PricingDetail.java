package domain.client.pricingconfiguration;

import java.sql.Date;
import java.util.Objects;

public class PricingDetail {
    private String clientId = "";
    private String clientName = "";
    private Date invoiceDt;
    private Double begAmt = 0.00;
    private Double retroAdj = 0.00;
    private Double endAmt = 0.00;
    private Double totalAdj = 0.00;
    private String cmt = "";

    private Date effectiveDate;
    private Date revisionDate;
    private String perDiemType = "";
    private boolean carveouts = false;
    private Double perDiemAmount = 0.00;
    private String incrementTable = "";
    private boolean clnPricingSuspendPrice = false;
    private String clnPricingRetailFeeSchedule = "";
    private String clnPricingFeeSchedule = "";
    private String clnPricingSpecialPriceTable = "";
    private String clnPricingBillExpectPrice = "";
    private Double clnPricingDiscount = 0.00;
    private Double clnPricingTradeDiscount = 0.00;
    private Double clnPricingTax = 0.00;
    private boolean nonClnPricingSuspendPrice = false;
    private String nonClnPricingRetailFeeSchedule = "";
    private String nonClnPricingFeeSchedule = "";
    private String nonClnPricingSpecialPriceTable = "";
    private Double nonClnPricingDiscount = 0.00;
    private Double nonClnPricingTax = 0.00;
    private Double clnMaxDiscountLvl0 = 0.00;
    private Double clnMaxDiscountLvl1 = 0.00;
    private Double clnMaxDiscountLvl2 = 0.00;
    private Double clnMaxDiscountLvl3 = 0.00;
    private Double clnMaxDiscountLvl4 = 0.00;
    private Double clnMaxDiscountLvl5 = 0.00;
    private Double clnMaxDiscountLvl6 = 0.00;
    private Double clnMaxDiscountLvl7 = 0.00;
    private Double clnMaxDiscountLvl8 = 0.00;
    private Double clnMaxDiscountLvl9 = 0.00;

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

    public Date getInvoiceDt() {
        return invoiceDt;
    }

    public void setInvoiceDt(Date invoiceDt) {
        this.invoiceDt = invoiceDt;
    }

    public Double getBegAmt() {
        return begAmt;
    }

    public void setBegAmt(Double begAmt) {
        this.begAmt = begAmt;
    }

    public Double getRetroAdj() {
        return retroAdj;
    }

    public void setRetroAdj(Double retroAdj) {
        this.retroAdj = retroAdj;
    }

    public Double getEndAmt() {
        return endAmt;
    }

    public void setEndAmt(Double endAmt) {
        this.endAmt = endAmt;
    }

    public Double getTotalAdj() {
        return totalAdj;
    }

    public void setTotalAdj(Double totalAdj) {
        this.totalAdj = totalAdj;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getPerDiemType() {
        return perDiemType;
    }

    public void setPerDiemType(String perDiemType) {
        this.perDiemType = perDiemType;
    }

    public boolean isCarveouts() {
        return carveouts;
    }

    public void setCarveouts(boolean carveouts) {
        this.carveouts = carveouts;
    }

    public Double getPerDiemAmount() {
        return perDiemAmount;
    }

    public void setPerDiemAmount(Double perDiemAmount) {
        this.perDiemAmount = perDiemAmount;
    }

    public String getIncrementTable() {
        return incrementTable;
    }

    public void setIncrementTable(String incrementTable) {
        this.incrementTable = incrementTable;
    }

    public boolean isClnPricingSuspendPrice() {
        return clnPricingSuspendPrice;
    }

    public void setClnPricingSuspendPrice(boolean clnPricingSuspendPrice) {
        this.clnPricingSuspendPrice = clnPricingSuspendPrice;
    }

    public String getClnPricingRetailFeeSchedule() {
        return clnPricingRetailFeeSchedule;
    }

    public void setClnPricingRetailFeeSchedule(String clnPricingRetailFeeSchedule) {
        this.clnPricingRetailFeeSchedule = clnPricingRetailFeeSchedule;
    }

    public String getClnPricingFeeSchedule() {
        return clnPricingFeeSchedule;
    }

    public void setClnPricingFeeSchedule(String clnPricingFeeSchedule) {
        this.clnPricingFeeSchedule = clnPricingFeeSchedule;
    }

    public String getClnPricingSpecialPriceTable() {
        return clnPricingSpecialPriceTable;
    }

    public void setClnPricingSpecialPriceTable(String clnPricingSpecialPriceTable) {
        this.clnPricingSpecialPriceTable = clnPricingSpecialPriceTable;
    }

    public String getClnPricingBillExpectPrice() {
        return clnPricingBillExpectPrice;
    }

    public void setClnPricingBillExpectPrice(String clnPricingBillExpectPrice) {
        this.clnPricingBillExpectPrice = clnPricingBillExpectPrice;
    }

    public Double getClnPricingDiscount() {
        return clnPricingDiscount;
    }

    public void setClnPricingDiscount(Double clnPricingDiscount) {
        this.clnPricingDiscount = clnPricingDiscount;
    }

    public Double getClnPricingTradeDiscount() {
        return clnPricingTradeDiscount;
    }

    public void setClnPricingTradeDiscount(Double clnPricingTradeDiscount) {
        this.clnPricingTradeDiscount = clnPricingTradeDiscount;
    }

    public Double getClnPricingTax() {
        return clnPricingTax;
    }

    public void setClnPricingTax(Double clnPricingTax) {
        this.clnPricingTax = clnPricingTax;
    }

    public boolean isNonClnPricingSuspendPrice() {
        return nonClnPricingSuspendPrice;
    }

    public void setNonClnPricingSuspendPrice(boolean nonClnPricingSuspendPrice) {
        this.nonClnPricingSuspendPrice = nonClnPricingSuspendPrice;
    }

    public String getNonClnPricingRetailFeeSchedule() {
        return nonClnPricingRetailFeeSchedule;
    }

    public void setNonClnPricingRetailFeeSchedule(String nonClnPricingRetailFeeSchedule) {
        this.nonClnPricingRetailFeeSchedule = nonClnPricingRetailFeeSchedule;
    }

    public String getNonClnPricingFeeSchedule() {
        return nonClnPricingFeeSchedule;
    }

    public void setNonClnPricingFeeSchedule(String nonClnPricingFeeSchedule) {
        this.nonClnPricingFeeSchedule = nonClnPricingFeeSchedule;
    }

    public String getNonClnPricingSpecialPriceTable() {
        return nonClnPricingSpecialPriceTable;
    }

    public void setNonClnPricingSpecialPriceTable(String nonClnPricingSpecialPriceTable) {
        this.nonClnPricingSpecialPriceTable = nonClnPricingSpecialPriceTable;
    }

    public Double getNonClnPricingDiscount() {
        return nonClnPricingDiscount;
    }

    public void setNonClnPricingDiscount(Double nonClnPricingDiscount) {
        this.nonClnPricingDiscount = nonClnPricingDiscount;
    }

    public Double getNonClnPricingTax() {
        return nonClnPricingTax;
    }

    public void setNonClnPricingTax(Double nonClnPricingTax) {
        this.nonClnPricingTax = nonClnPricingTax;
    }

    public Double getClnMaxDiscountLvl0() {
        return clnMaxDiscountLvl0;
    }

    public void setClnMaxDiscountLvl0(Double clnMaxDiscountLvl0) {
        this.clnMaxDiscountLvl0 = clnMaxDiscountLvl0;
    }

    public Double getClnMaxDiscountLvl1() {
        return clnMaxDiscountLvl1;
    }

    public void setClnMaxDiscountLvl1(Double clnMaxDiscountLvl1) {
        this.clnMaxDiscountLvl1 = clnMaxDiscountLvl1;
    }

    public Double getClnMaxDiscountLvl2() {
        return clnMaxDiscountLvl2;
    }

    public void setClnMaxDiscountLvl2(Double clnMaxDiscountLvl2) {
        this.clnMaxDiscountLvl2 = clnMaxDiscountLvl2;
    }

    public Double getClnMaxDiscountLvl3() {
        return clnMaxDiscountLvl3;
    }

    public void setClnMaxDiscountLvl3(Double clnMaxDiscountLvl3) {
        this.clnMaxDiscountLvl3 = clnMaxDiscountLvl3;
    }

    public Double getClnMaxDiscountLvl4() {
        return clnMaxDiscountLvl4;
    }

    public void setClnMaxDiscountLvl4(Double clnMaxDiscountLvl4) {
        this.clnMaxDiscountLvl4 = clnMaxDiscountLvl4;
    }

    public Double getClnMaxDiscountLvl5() {
        return clnMaxDiscountLvl5;
    }

    public void setClnMaxDiscountLvl5(Double clnMaxDiscountLvl5) {
        this.clnMaxDiscountLvl5 = clnMaxDiscountLvl5;
    }

    public Double getClnMaxDiscountLvl6() {
        return clnMaxDiscountLvl6;
    }

    public void setClnMaxDiscountLvl6(Double clnMaxDiscountLvl6) {
        this.clnMaxDiscountLvl6 = clnMaxDiscountLvl6;
    }

    public Double getClnMaxDiscountLvl7() {
        return clnMaxDiscountLvl7;
    }

    public void setClnMaxDiscountLvl7(Double clnMaxDiscountLvl7) {
        this.clnMaxDiscountLvl7 = clnMaxDiscountLvl7;
    }

    public Double getClnMaxDiscountLvl8() {
        return clnMaxDiscountLvl8;
    }

    public void setClnMaxDiscountLvl8(Double clnMaxDiscountLvl8) {
        this.clnMaxDiscountLvl8 = clnMaxDiscountLvl8;
    }

    public Double getClnMaxDiscountLvl9() {
        return clnMaxDiscountLvl9;
    }

    public void setClnMaxDiscountLvl9(Double clnMaxDiscountLvl9) {
        this.clnMaxDiscountLvl9 = clnMaxDiscountLvl9;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingDetail that = (PricingDetail) o;
        return carveouts == that.carveouts &&
                clnPricingSuspendPrice == that.clnPricingSuspendPrice &&
                nonClnPricingSuspendPrice == that.nonClnPricingSuspendPrice &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(clientName, that.clientName) &&
                Objects.equals(invoiceDt, that.invoiceDt) &&
                Objects.equals(begAmt, that.begAmt) &&
                Objects.equals(retroAdj, that.retroAdj) &&
                Objects.equals(endAmt, that.endAmt) &&
                Objects.equals(totalAdj, that.totalAdj) &&
                Objects.equals(cmt, that.cmt) &&
                Objects.equals(effectiveDate, that.effectiveDate) &&
                Objects.equals(revisionDate, that.revisionDate) &&
                Objects.equals(perDiemType, that.perDiemType) &&
                Objects.equals(perDiemAmount, that.perDiemAmount) &&
                Objects.equals(incrementTable, that.incrementTable) &&
                Objects.equals(clnPricingRetailFeeSchedule, that.clnPricingRetailFeeSchedule) &&
                Objects.equals(clnPricingFeeSchedule, that.clnPricingFeeSchedule) &&
                Objects.equals(clnPricingSpecialPriceTable, that.clnPricingSpecialPriceTable) &&
                Objects.equals(clnPricingBillExpectPrice, that.clnPricingBillExpectPrice) &&
                Objects.equals(clnPricingDiscount, that.clnPricingDiscount) &&
                Objects.equals(clnPricingTradeDiscount, that.clnPricingTradeDiscount) &&
                Objects.equals(clnPricingTax, that.clnPricingTax) &&
                Objects.equals(nonClnPricingRetailFeeSchedule, that.nonClnPricingRetailFeeSchedule) &&
                Objects.equals(nonClnPricingFeeSchedule, that.nonClnPricingFeeSchedule) &&
                Objects.equals(nonClnPricingSpecialPriceTable, that.nonClnPricingSpecialPriceTable) &&
                Objects.equals(nonClnPricingDiscount, that.nonClnPricingDiscount) &&
                Objects.equals(nonClnPricingTax, that.nonClnPricingTax) &&
                Objects.equals(clnMaxDiscountLvl0, that.clnMaxDiscountLvl0) &&
                Objects.equals(clnMaxDiscountLvl1, that.clnMaxDiscountLvl1) &&
                Objects.equals(clnMaxDiscountLvl2, that.clnMaxDiscountLvl2) &&
                Objects.equals(clnMaxDiscountLvl3, that.clnMaxDiscountLvl3) &&
                Objects.equals(clnMaxDiscountLvl4, that.clnMaxDiscountLvl4) &&
                Objects.equals(clnMaxDiscountLvl5, that.clnMaxDiscountLvl5) &&
                Objects.equals(clnMaxDiscountLvl6, that.clnMaxDiscountLvl6) &&
                Objects.equals(clnMaxDiscountLvl7, that.clnMaxDiscountLvl7) &&
                Objects.equals(clnMaxDiscountLvl8, that.clnMaxDiscountLvl8) &&
                Objects.equals(clnMaxDiscountLvl9, that.clnMaxDiscountLvl9);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientId, clientName, invoiceDt, begAmt, retroAdj, endAmt, totalAdj, cmt, effectiveDate, revisionDate, perDiemType, carveouts, perDiemAmount, incrementTable, clnPricingSuspendPrice, clnPricingRetailFeeSchedule, clnPricingFeeSchedule, clnPricingSpecialPriceTable, clnPricingBillExpectPrice, clnPricingDiscount, clnPricingTradeDiscount, clnPricingTax, nonClnPricingSuspendPrice, nonClnPricingRetailFeeSchedule, nonClnPricingFeeSchedule, nonClnPricingSpecialPriceTable, nonClnPricingDiscount, nonClnPricingTax, clnMaxDiscountLvl0, clnMaxDiscountLvl1, clnMaxDiscountLvl2, clnMaxDiscountLvl3, clnMaxDiscountLvl4, clnMaxDiscountLvl5, clnMaxDiscountLvl6, clnMaxDiscountLvl7, clnMaxDiscountLvl8, clnMaxDiscountLvl9);
    }

    @Override
    public String toString() {
        return "PricingDetail{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", invoiceDt=" + invoiceDt +
                ", begAmt=" + begAmt +
                ", retroAdj=" + retroAdj +
                ", endAmt=" + endAmt +
                ", totalAdj=" + totalAdj +
                ", cmt='" + cmt + '\'' +
                ", effectiveDate=" + effectiveDate +
                ", revisionDate=" + revisionDate +
                ", perDiemType='" + perDiemType + '\'' +
                ", carveouts=" + carveouts +
                ", perDiemAmount=" + perDiemAmount +
                ", incrementTable='" + incrementTable + '\'' +
                ", clnPricingSuspendPrice=" + clnPricingSuspendPrice +
                ", clnPricingRetailFeeSchedule='" + clnPricingRetailFeeSchedule + '\'' +
                ", clnPricingFeeSchedule='" + clnPricingFeeSchedule + '\'' +
                ", clnPricingSpecialPriceTable='" + clnPricingSpecialPriceTable + '\'' +
                ", clnPricingBillExpectPrice='" + clnPricingBillExpectPrice + '\'' +
                ", clnPricingDiscount=" + clnPricingDiscount +
                ", clnPricingTradeDiscount=" + clnPricingTradeDiscount +
                ", clnPricingTax=" + clnPricingTax +
                ", nonClnPricingSuspendPrice=" + nonClnPricingSuspendPrice +
                ", nonClnPricingRetailFeeSchedule='" + nonClnPricingRetailFeeSchedule + '\'' +
                ", nonClnPricingFeeSchedule='" + nonClnPricingFeeSchedule + '\'' +
                ", nonClnPricingSpecialPriceTable='" + nonClnPricingSpecialPriceTable + '\'' +
                ", nonClnPricingDiscount=" + nonClnPricingDiscount +
                ", nonClnPricingTax=" + nonClnPricingTax +
                ", clnMaxDiscountLvl0=" + clnMaxDiscountLvl0 +
                ", clnMaxDiscountLvl1=" + clnMaxDiscountLvl1 +
                ", clnMaxDiscountLvl2=" + clnMaxDiscountLvl2 +
                ", clnMaxDiscountLvl3=" + clnMaxDiscountLvl3 +
                ", clnMaxDiscountLvl4=" + clnMaxDiscountLvl4 +
                ", clnMaxDiscountLvl5=" + clnMaxDiscountLvl5 +
                ", clnMaxDiscountLvl6=" + clnMaxDiscountLvl6 +
                ", clnMaxDiscountLvl7=" + clnMaxDiscountLvl7 +
                ", clnMaxDiscountLvl8=" + clnMaxDiscountLvl8 +
                ", clnMaxDiscountLvl9=" + clnMaxDiscountLvl9 +
                '}';
    }
}
