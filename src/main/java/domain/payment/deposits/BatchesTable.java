package domain.payment.deposits;

import java.sql.Date;

public class BatchesTable {
    private int batchId;
    private String payorId = "";
    private float batchAmount;
    private float unappliedAmount;
    private float appliedAmount;
    private float postedAmount;
    private Date lastActivity;
    private String status = "";
    private String assignedUser = "";
    private float nonARAmount;
    private String glAccount = "";
    private String notes = "";
    private boolean postNonArAmt;
    private boolean deleted;

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public String getPayorId() {
        return payorId;
    }

    public void setPayorId(String payorId) {
        this.payorId = payorId;
    }

    public float getBatchAmount() {
        return batchAmount;
    }

    public void setBatchAmount(float batchAmount) {
        this.batchAmount = batchAmount;
    }

    public float getUnappliedAmount() {
        return unappliedAmount;
    }

    public void setUnappliedAmount(float unappliedAmount) {
        this.unappliedAmount = unappliedAmount;
    }

    public float getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(float appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public float getPostedAmount() {
        return postedAmount;
    }

    public void setPostedAmount(float postedAmount) {
        this.postedAmount = postedAmount;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    public float getNonARAmount() {
        return nonARAmount;
    }

    public void setNonARAmount(float nonARAmount) {
        this.nonARAmount = nonARAmount;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isPostNonArAmt() {
        return postNonArAmt;
    }

    public void setPostNonArAmt(boolean postNonArAmt) {
        this.postNonArAmt = postNonArAmt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(appliedAmount);
        result = prime * result + ((assignedUser == null) ? 0 : assignedUser.hashCode());
        result = prime * result + Float.floatToIntBits(batchAmount);
        result = prime * result + batchId;
        result = prime * result + (deleted ? 1231 : 1237);
        result = prime * result + ((glAccount == null) ? 0 : glAccount.hashCode());
        result = prime * result + ((lastActivity == null) ? 0 : lastActivity.hashCode());
        result = prime * result + Float.floatToIntBits(nonARAmount);
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime * result + ((payorId == null) ? 0 : payorId.hashCode());
        result = prime * result + (postNonArAmt ? 1231 : 1237);
        result = prime * result + Float.floatToIntBits(postedAmount);
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + Float.floatToIntBits(unappliedAmount);
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
        BatchesTable other = (BatchesTable) obj;
        if (Float.floatToIntBits(appliedAmount) != Float.floatToIntBits(other.appliedAmount))
            return false;
        if (assignedUser == null) {
            if (other.assignedUser != null)
                return false;
        } else if (!assignedUser.equals(other.assignedUser))
            return false;
        if (Float.floatToIntBits(batchAmount) != Float.floatToIntBits(other.batchAmount))
            return false;
        if (batchId != other.batchId)
            return false;
        if (deleted != other.deleted)
            return false;
        if (glAccount == null) {
            if (other.glAccount != null)
                return false;
        } else if (!glAccount.equals(other.glAccount))
            return false;
        if (lastActivity == null) {
            if (other.lastActivity != null)
                return false;
        } else if (!lastActivity.equals(other.lastActivity))
            return false;
        if (Float.floatToIntBits(nonARAmount) != Float.floatToIntBits(other.nonARAmount))
            return false;
        if (notes == null) {
            if (other.notes != null)
                return false;
        } else if (!notes.equals(other.notes))
            return false;
        if (payorId == null) {
            if (other.payorId != null)
                return false;
        } else if (!payorId.equals(other.payorId))
            return false;
        if (postNonArAmt != other.postNonArAmt)
            return false;
        if (Float.floatToIntBits(postedAmount) != Float.floatToIntBits(other.postedAmount))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (Float.floatToIntBits(unappliedAmount) != Float.floatToIntBits(other.unappliedAmount))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BatchesTable ["
                + "batchId=" + batchId
                + ", payorId=" + payorId
                + ", batchAmount=" + batchAmount
                + ", unappliedAmount=" + unappliedAmount
                + ", appliedAmount=" + appliedAmount
                + ", postedAmount=" + postedAmount
                + ", lastActivity=" + lastActivity
                + ", status=" + status
                + ", assignedUser=" + assignedUser
                + ", nonARAmount=" + nonARAmount
                + ", glAccount=" + glAccount
                + ", notes=" + notes
                + ", postNonArAmt=" + postNonArAmt
                + ", deleted=" + deleted
                + "]";
    }

}