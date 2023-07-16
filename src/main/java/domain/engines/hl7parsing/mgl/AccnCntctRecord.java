package domain.engines.hl7parsing.mgl;

import java.sql.Date;
import java.util.Objects;

public class AccnCntctRecord {
    private String userId;
    private Date cntctDt;
    private String note;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCntctDt() {
        return cntctDt;
    }

    public void setCntctDt(Date cntctDt) {
        this.cntctDt = cntctDt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccnCntctRecord that = (AccnCntctRecord) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(cntctDt, that.cntctDt) &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, cntctDt, note);
    }

    @Override
    public String toString() {
        return "AccnCntctRecord{" +
                "userId='" + userId + '\'' +
                ", cntctDt=" + cntctDt +
                ", note='" + note + '\'' +
                '}';
    }
}
