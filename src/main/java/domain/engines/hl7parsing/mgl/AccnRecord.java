package domain.engines.hl7parsing.mgl;

import java.sql.Date;
import java.util.Objects;

public class AccnRecord {
    private Date dos;
    private String ptLName;
    private String ptFName;
    private String ptMName;
    private Date ptDob;
    private int ptSex;
    private int clnId;
    private String ptAddr1;
    private String ptAddr2;
    private String ptZip;
    private String ptHomePhone;
    private int ptSsn;
    private String reqId;
    private String ptCity;
    private int ptCountry;

    public Date getDos() {
        return dos;
    }

    public void setDos(Date dos) {
        this.dos = dos;
    }

    public String getPtLName() {
        return ptLName;
    }

    public void setPtLName(String ptLName) {
        this.ptLName = ptLName;
    }

    public String getPtFName() {
        return ptFName;
    }

    public void setPtFName(String ptFName) {
        this.ptFName = ptFName;
    }

    public String getPtMName() {
        return ptMName;
    }

    public void setPtMName(String ptMName) {
        this.ptMName = ptMName;
    }

    public Date getPtDob() {
        return ptDob;
    }

    public void setPtDob(Date ptDob) {
        this.ptDob = ptDob;
    }

    public int getPtSex() {
        return ptSex;
    }

    public void setPtSex(int ptSex) {
        this.ptSex = ptSex;
    }

    public int getClnId() {
        return clnId;
    }

    public void setClnId(int clnId) {
        this.clnId = clnId;
    }

    public String getPtAddr1() {
        return ptAddr1;
    }

    public void setPtAddr1(String ptAddr1) {
        this.ptAddr1 = ptAddr1;
    }

    public String getPtAddr2() {
        return ptAddr2;
    }

    public void setPtAddr2(String ptAddr2) {
        this.ptAddr2 = ptAddr2;
    }

    public String getPtZip() {
        return ptZip;
    }

    public void setPtZip(String ptZip) {
        this.ptZip = ptZip;
    }

    public String getPtHomePhone() {
        return ptHomePhone;
    }

    public void setPtHomePhone(String ptHomePhone) {
        this.ptHomePhone = ptHomePhone;
    }

    public int getPtSsn() {
        return ptSsn;
    }

    public void setPtSsn(int ptSsn) {
        this.ptSsn = ptSsn;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getPtCity() {
        return ptCity;
    }

    public void setPtCity(String ptCity) {
        this.ptCity = ptCity;
    }

    public int getPtCountry() {
        return ptCountry;
    }

    public void setPtCountry(int ptCountry) {
        this.ptCountry = ptCountry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccnRecord that = (AccnRecord) o;
        return ptSex == that.ptSex &&
                clnId == that.clnId &&
                ptSsn == that.ptSsn &&
                ptCountry == that.ptCountry &&
                Objects.equals(dos, that.dos) &&
                Objects.equals(ptLName, that.ptLName) &&
                Objects.equals(ptFName, that.ptFName) &&
                Objects.equals(ptMName, that.ptMName) &&
                Objects.equals(ptDob, that.ptDob) &&
                Objects.equals(ptAddr1, that.ptAddr1) &&
                Objects.equals(ptAddr2, that.ptAddr2) &&
                Objects.equals(ptZip, that.ptZip) &&
                Objects.equals(ptHomePhone, that.ptHomePhone) &&
                Objects.equals(reqId, that.reqId) &&
                Objects.equals(ptCity, that.ptCity);
    }

    @Override
    public int hashCode() {

        return Objects.hash(dos, ptLName, ptFName, ptMName, ptDob, ptSex, clnId, ptAddr1, ptAddr2, ptZip, ptHomePhone, ptSsn, reqId, ptCity, ptCountry);
    }

    @Override
    public String toString() {
        return "AccnRecord{" +
                "dos=" + dos +
                ", ptLName='" + ptLName + '\'' +
                ", ptFName='" + ptFName + '\'' +
                ", ptMName='" + ptMName + '\'' +
                ", ptDob=" + ptDob +
                ", ptSex=" + ptSex +
                ", clnId=" + clnId +
                ", ptAddr1='" + ptAddr1 + '\'' +
                ", ptAddr2='" + ptAddr2 + '\'' +
                ", ptZip='" + ptZip + '\'' +
                ", ptHomePhone='" + ptHomePhone + '\'' +
                ", ptSsn=" + ptSsn +
                ", reqId='" + reqId + '\'' +
                ", ptCity='" + ptCity + '\'' +
                ", ptCountry=" + ptCountry +
                '}';
    }
}
