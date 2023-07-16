package domain.accession.PatientServiceCenter.patientestimation;

import java.util.Objects;

public class PatientResponsibilityDetail
{
    private String procCode;
    private String units;
    private String billAmt;
    private String expectAmt;
    private String mod1;
    private String copayAmt;
    private String coinsPct;
    private String coinsAmt;
    private String dedAppliedAmt;
    private String dedRemainingAmt;
    private String dedAnnualAmt;

    public String getProcCode()
    {
        return procCode;
    }

    public void setProcCode(String procCode)
    {
        this.procCode = procCode;
    }

    public String getUnits()
    {
        return units;
    }

    public void setUnits(String units)
    {
        this.units = units;
    }

    public String getBillAmt()
    {
        return billAmt;
    }

    public void setBillAmt(String billAmt)
    {
        this.billAmt = billAmt;
    }

    public String getExpectAmt()
    {
        return expectAmt;
    }

    public void setExpectAmt(String expectAmt)
    {
        this.expectAmt = expectAmt;
    }

    public String getMod1()
    {
        return mod1;
    }

    public void setMod1(String mod1)
    {
        this.mod1 = mod1;
    }

    public String getCopayAmt()
    {
        return copayAmt;
    }

    public void setCopayAmt(String copayAmt)
    {
        this.copayAmt = copayAmt;
    }

    public String getCoinsPct()
    {
        return coinsPct;
    }

    public void setCoinsPct(String coinsPct)
    {
        this.coinsPct = coinsPct;
    }

    public String getCoinsAmt()
    {
        return coinsAmt;
    }

    public void setCoinsAmt(String coinsAmt)
    {
        this.coinsAmt = coinsAmt;
    }

    public String getDedAppliedAmt()
    {
        return dedAppliedAmt;
    }

    public void setDedAppliedAmt(String dedAppliedAmt)
    {
        this.dedAppliedAmt = dedAppliedAmt;
    }

    public String getDedRemainingAmt()
    {
        return dedRemainingAmt;
    }

    public void setDedRemainingAmt(String dedRemainingAmt)
    {
        this.dedRemainingAmt = dedRemainingAmt;
    }

    public String getDedAnnualAmt()
    {
        return dedAnnualAmt;
    }

    public void setDedAnnualAmt(String dedAnnualAmt)
    {
        this.dedAnnualAmt = dedAnnualAmt;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientResponsibilityDetail that = (PatientResponsibilityDetail) o;
        return Objects.equals(procCode, that.procCode) && Objects.equals(units, that.units) && Objects.equals(billAmt, that.billAmt) && Objects.equals(expectAmt, that.expectAmt) && Objects.equals(mod1, that.mod1) && Objects.equals(copayAmt, that.copayAmt) && Objects.equals(coinsPct, that.coinsPct) && Objects.equals(coinsAmt, that.coinsAmt) && Objects.equals(dedAppliedAmt, that.dedAppliedAmt) && Objects.equals(dedRemainingAmt, that.dedRemainingAmt) && Objects.equals(dedAnnualAmt, that.dedAnnualAmt);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(procCode, units, billAmt, expectAmt, mod1, copayAmt, coinsPct, coinsAmt, dedAppliedAmt, dedRemainingAmt, dedAnnualAmt);
    }
}
