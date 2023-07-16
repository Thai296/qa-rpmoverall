package domain.client.pricingconfiguration;

import java.sql.Date;
import java.util.Objects;

public class RetroactivePriceImpact {
    private String clientId = "";
    private String clientName = "";
    private Date invoiceDt;
    private Double begAmt = 0.00;
    private Double retroAdj = 0.00;
    private Double endAmt = 0.00;
    private Double totalAdj = 0.00;
    private String cmt = "";
    private Double totalEstImp = 0.00;
    private String accnId = "";
    private int testId;
    private int accnTestSeqId;

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

    public Double getTotalEstImp() {
        return totalEstImp;
    }

    public void setTotalEstImp(Double totalEstImp) {
        this.totalEstImp = totalEstImp;
    }

    public String getAccnId() {
        return accnId;
    }

    public void setAccnId(String accnId) {
        this.accnId = accnId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getAccnTestSeqId() {
        return accnTestSeqId;
    }

    public void setAccnTestSeqId(int accnTestSeqId) {
        this.accnTestSeqId = accnTestSeqId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RetroactivePriceImpact that = (RetroactivePriceImpact) o;
        return testId == that.testId &&
                accnTestSeqId == that.accnTestSeqId &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(clientName, that.clientName) &&
                Objects.equals(invoiceDt, that.invoiceDt) &&
                Objects.equals(begAmt, that.begAmt) &&
                Objects.equals(retroAdj, that.retroAdj) &&
                Objects.equals(endAmt, that.endAmt) &&
                Objects.equals(totalAdj, that.totalAdj) &&
                Objects.equals(cmt, that.cmt) &&
                Objects.equals(totalEstImp, that.totalEstImp) &&
                Objects.equals(accnId, that.accnId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientId, clientName, invoiceDt, begAmt, retroAdj, endAmt, totalAdj, cmt, totalEstImp, accnId, testId, accnTestSeqId);
    }

    @Override
    public String toString() {
        return "RetroactivePriceImpact{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", invoiceDt=" + invoiceDt +
                ", begAmt=" + begAmt +
                ", retroAdj=" + retroAdj +
                ", endAmt=" + endAmt +
                ", totalAdj=" + totalAdj +
                ", cmt='" + cmt + '\'' +
                ", totalEstImp=" + totalEstImp +
                ", accnId='" + accnId + '\'' +
                ", testId=" + testId +
                ", accnTestSeqId=" + accnTestSeqId +
                '}';
    }
}
