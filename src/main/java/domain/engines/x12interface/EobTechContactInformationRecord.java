package domain.engines.x12interface;

import java.util.Objects;

public class EobTechContactInformationRecord {
    private int eobCommTypSeqId;
    private String contactNumber;

    public int getEobCommTypSeqId() {
        return eobCommTypSeqId;
    }

    public void setEobCommTypSeqId(int eobCommTypSeqId) {
        this.eobCommTypSeqId = eobCommTypSeqId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobTechContactInformationRecord that = (EobTechContactInformationRecord) o;
        return eobCommTypSeqId == that.eobCommTypSeqId &&
                Objects.equals(contactNumber, that.contactNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(eobCommTypSeqId, contactNumber);
    }

    @Override
    public String toString() {
        return "EobTechContactInformationRecord{" +
                "eobCommTypSeqId=" + eobCommTypSeqId +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}
