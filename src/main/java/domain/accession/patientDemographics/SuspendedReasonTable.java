package domain.accession.patientDemographics;

import java.sql.Date;

public class SuspendedReasonTable {
    private Date date;
    private String reason;
    private String notes;
    private String user;
    private boolean isFix;
    private boolean isDeleted;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isFix() {
        return isFix;
    }

    public void setFix(boolean isFix) {
        this.isFix = isFix;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + (isDeleted ? 1231 : 1237);
        result = prime * result + (isFix ? 1231 : 1237);
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SuspendedReasonTable))
            return false;
        SuspendedReasonTable other = (SuspendedReasonTable) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (isDeleted != other.isDeleted)
            return false;
        if (isFix != other.isFix)
            return false;
        if (notes == null) {
            if (other.notes != null)
                return false;
        } else if (!notes.equals(other.notes))
            return false;
        if (reason == null) {
            if (other.reason != null)
                return false;
        } else if (!reason.equals(other.reason))
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
        return "SuspendedReason [date=" + date + ", reason=" + reason + ", notes=" + notes + ", user=" + user + ", isFix=" + isFix + ", isDeleted=" + isDeleted + "]";
    }

}
