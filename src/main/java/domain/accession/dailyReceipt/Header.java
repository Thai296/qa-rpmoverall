package domain.accession.dailyReceipt;

import java.sql.Date;

public class Header {
    private String dailyReceiptId="";
    private String paymentFacilityId;
    private String paymentUserId="";
    private String paymentFacilityName="";
    private String paymentUserName="";
    private int depositId;
    private Date dateOfDeposit;
    private int batchId;
    private String createdBy="";
    
    public String getDailyReceiptId() {
        return dailyReceiptId;
    }
    
    public void setDailyReceiptId(String dailyReceiptId) {
        this.dailyReceiptId = dailyReceiptId;
    }
    
    public String getPaymentFacilityId() {
        return paymentFacilityId;
    }
    
    public void setPaymentFacilityId(String paymentFacilityId) {
        this.paymentFacilityId = paymentFacilityId;
    }
    
    public String getPaymentUserId() {
        return paymentUserId;
    }
    
    public void setPaymentUserId(String paymentUserId) {
        this.paymentUserId = paymentUserId;
    }
    
    public String getPaymentFacilityName() {
        return paymentFacilityName;
    }
    
    public void setPaymentFacilityName(String paymentFacilityName) {
        this.paymentFacilityName = paymentFacilityName;
    }
    
    public String getPaymentUserName() {
        return paymentUserName;
    }
    
    public void setPaymentUserName(String paymentUserName) {
        this.paymentUserName = paymentUserName;
    }
    
    public int getDepositId() {
        return depositId;
    }
    
    public void setDepositId(int depositId) {
        this.depositId = depositId;
    }
    
    public Date getDateOfDeposit() {
        return dateOfDeposit;
    }
    
    public void setDateOfDeposit(Date dateOfDeposit) {
        this.dateOfDeposit = dateOfDeposit;
    }
    
    public int getBatchId() {
        return batchId;
    }
    
    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + batchId;
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = prime * result + ((dailyReceiptId == null) ? 0 : dailyReceiptId.hashCode());
        result = prime * result + ((dateOfDeposit == null) ? 0 : dateOfDeposit.hashCode());
        result = prime * result + depositId;
        result = prime * result + ((paymentFacilityId == null) ? 0 : paymentFacilityId.hashCode());
        result = prime * result + ((paymentFacilityName == null) ? 0 : paymentFacilityName.hashCode());
        result = prime * result + ((paymentUserId == null) ? 0 : paymentUserId.hashCode());
        result = prime * result + ((paymentUserName == null) ? 0 : paymentUserName.hashCode());
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
        if (batchId != other.batchId)
            return false;
        if (createdBy == null) {
            if (other.createdBy != null)
                return false;
        } else if (!createdBy.equals(other.createdBy))
            return false;
        if (dailyReceiptId == null) {
            if (other.dailyReceiptId != null)
                return false;
        } else if (!dailyReceiptId.equals(other.dailyReceiptId))
            return false;
        if (dateOfDeposit == null) {
            if (other.dateOfDeposit != null)
                return false;
        } else if (!dateOfDeposit.equals(other.dateOfDeposit))
            return false;
        if (depositId != other.depositId)
            return false;
        if (paymentFacilityId == null) {
            if (other.paymentFacilityId != null)
                return false;
        } else if (!paymentFacilityId.equals(other.paymentFacilityId))
            return false;
        if (paymentFacilityName == null) {
            if (other.paymentFacilityName != null)
                return false;
        } else if (!paymentFacilityName.equals(other.paymentFacilityName))
            return false;
        if (paymentUserId == null) {
            if (other.paymentUserId != null)
                return false;
        } else if (!paymentUserId.equals(other.paymentUserId))
            return false;
        if (paymentUserName == null) {
            if (other.paymentUserName != null)
                return false;
        } else if (!paymentUserName.equals(other.paymentUserName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Header ["
                    + "dailyReceiptId=" + dailyReceiptId
                    + ", paymentFacilityId=" + paymentFacilityId
                    + ", paymentUserId=" + paymentUserId
                    + ", paymentFacilityName=" + paymentFacilityName
                    + ", paymentUserName=" + paymentUserName
                    + ", depositId=" + depositId
                    + ", dateOfDeposit="+ dateOfDeposit
                    + ", batchId=" + batchId
                    + ", createdBy=" + createdBy
                + "]";
    }
    
    
    
}
