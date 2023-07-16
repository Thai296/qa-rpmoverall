package domain.payment.deposits;

import java.sql.Date;

public class Header {
    private int depositId;
    private Date depositDate;
    private Date accountingDate;
    private float depositAmount;
    private String bank = "";
    private String eraId = "";
    private String eftId = "";
    private String user = "";
    private boolean deleteDeposit;

    public int getDepositId() {
        return depositId;
    }

    public void setDepositId(int depositId) {
        this.depositId = depositId;
    }

    public Date getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }

    public Date getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(Date accountingDate) {
        this.accountingDate = accountingDate;
    }

    public float getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(float depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getEraId() {
        return eraId;
    }

    public void setEraId(String eraId) {
        this.eraId = eraId;
    }

    public String getEftId() {
        return eftId;
    }

    public void setEftId(String eftId) {
        this.eftId = eftId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isDeleteDeposit() {
        return deleteDeposit;
    }

    public void setDeleteDeposit(boolean deleteDeposit) {
        this.deleteDeposit = deleteDeposit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountingDate == null) ? 0 : accountingDate.hashCode());
        result = prime * result + ((bank == null) ? 0 : bank.hashCode());
        result = prime * result + (deleteDeposit ? 1231 : 1237);
        result = prime * result + Float.floatToIntBits(depositAmount);
        result = prime * result + ((depositDate == null) ? 0 : depositDate.hashCode());
        result = prime * result + depositId;
        result = prime * result + ((eftId == null) ? 0 : eftId.hashCode());
        result = prime * result + ((eraId == null) ? 0 : eraId.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Header other = (Header) obj;
        if (accountingDate == null) {
            if (other.accountingDate != null)
                return false;
        } else if (!accountingDate.equals(other.accountingDate))
            return false;
        if (bank == null) {
            if (other.bank != null)
                return false;
        } else if (!bank.equals(other.bank))
            return false;
        if (deleteDeposit != other.deleteDeposit)
            return false;
        if (Float.floatToIntBits(depositAmount) != Float.floatToIntBits(other.depositAmount))
            return false;
        if (depositDate == null) {
            if (other.depositDate != null)
                return false;
        } else if (!depositDate.equals(other.depositDate))
            return false;
        if (depositId != other.depositId)
            return false;
        if (eftId == null) {
            if (other.eftId != null)
                return false;
        } else if (!eftId.equals(other.eftId))
            return false;
        if (eraId == null) {
            if (other.eraId != null)
                return false;
        } else if (!eraId.equals(other.eraId))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Header ["
                + "depositId=" + depositId
                + ", depositDate=" + depositDate
                + ", accountingDate=" + accountingDate
                + ", depositAmount=" + depositAmount
                + ", bank=" + bank
                + ", eraId=" + eraId
                + ", eftId=" + eftId
                + ", user=" + user
                + ", deleteDeposit=" + deleteDeposit
                + "]";
    }

}