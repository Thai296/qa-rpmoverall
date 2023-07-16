package domain.payment.deposits;

import java.sql.Date;

public class BankReconciliation {
    private int bankId;
    private Date bankDate;
    private float bankAmount;

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public Date getBankDate() {
        return bankDate;
    }

    public void setBankDate(Date bankDate) {
        this.bankDate = bankDate;
    }

    public float getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(float bankAmount) {
        this.bankAmount = bankAmount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(bankAmount);
        result = prime * result + ((bankDate == null) ? 0 : bankDate.hashCode());
        result = prime * result + bankId;
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
        BankReconciliation other = (BankReconciliation) obj;
        if (Float.floatToIntBits(bankAmount) != Float.floatToIntBits(other.bankAmount))
            return false;
        if (bankDate == null) {
            if (other.bankDate != null)
                return false;
        } else if (!bankDate.equals(other.bankDate))
            return false;
        if (bankId != other.bankId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BankReconciliation ["
                + "bankId=" + bankId
                + ", bankDate=" + bankDate
                + ", bankAmount=" + bankAmount
                + "]";
    }

}