package domain.engines.epoutsideagencyengine;

import java.util.Objects;

public class CodeRyteRecord {
    private String header;
    private String accnId;
    private String dos;
    private String dob;
    private String gender;

    public CodeRyteRecord() {
    }

    public CodeRyteRecord(String line) {
        String[] lineArray = line.split("[|]");

        this.header = lineArray[0];
        this.accnId = lineArray[1];
        this.dos = lineArray[2];
        this.dob = lineArray[3];
        this.gender = lineArray[4];
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getAccnId() {
        return accnId;
    }

    public void setAccnId(String accnId) {
        this.accnId = accnId;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeRyteRecord that = (CodeRyteRecord) o;
        return Objects.equals(header, that.header) &&
                Objects.equals(accnId, that.accnId) &&
                Objects.equals(dos, that.dos) &&
                Objects.equals(dob, that.dob) &&
                Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode() {

        return Objects.hash(header, accnId, dos, dob, gender);
    }

    @Override
    public String toString() {
        return "CodeRyteRecord{" +
                "header='" + header + '\'' +
                ", accnId='" + accnId + '\'' +
                ", dos='" + dos + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
