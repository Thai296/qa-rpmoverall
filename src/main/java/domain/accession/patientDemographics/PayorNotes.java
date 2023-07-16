package domain.accession.patientDemographics;

public class PayorNotes {

	private String claimNotes = "";
    private String interalNotes = "";
    private String ortherInfo1 = "";
    private String ortherInfo2 = "";
    private String ortherInfo3 = "";

    public String getClaimNotes() {
        return claimNotes;
    }

    public void setClaimNotes(String claimNotes) {
        this.claimNotes = claimNotes;
    }

    public String getInteralNotes() {
        return interalNotes;
    }

    public void setInteralNotes(String interalNotes) {
        this.interalNotes = interalNotes;
    }

    public String getOrtherInfo1() {
        return ortherInfo1;
    }

    public void setOrtherInfo1(String ortherInfo1) {
        this.ortherInfo1 = ortherInfo1;
    }

    public String getOrtherInfo2() {
        return ortherInfo2;
    }

    public void setOrtherInfo2(String ortherInfo2) {
        this.ortherInfo2 = ortherInfo2;
    }

    public String getOrtherInfo3() {
        return ortherInfo3;
    }

    public void setOrtherInfo3(String ortherInfo3) {
        this.ortherInfo3 = ortherInfo3;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((claimNotes == null) ? 0 : claimNotes.hashCode());
        result = prime * result + ((interalNotes == null) ? 0 : interalNotes.hashCode());
        result = prime * result + ((ortherInfo1 == null) ? 0 : ortherInfo1.hashCode());
        result = prime * result + ((ortherInfo2 == null) ? 0 : ortherInfo2.hashCode());
        result = prime * result + ((ortherInfo3 == null) ? 0 : ortherInfo3.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PayorNotes))
            return false;
        PayorNotes other = (PayorNotes) obj;
        if (claimNotes == null) {
            if (other.claimNotes != null)
                return false;
        } else if (!claimNotes.equals(other.claimNotes))
            return false;
        if (interalNotes == null) {
            if (other.interalNotes != null)
                return false;
        } else if (!interalNotes.equals(other.interalNotes))
            return false;
        if (ortherInfo1 == null) {
            if (other.ortherInfo1 != null)
                return false;
        } else if (!ortherInfo1.equals(other.ortherInfo1))
            return false;
        if (ortherInfo2 == null) {
            if (other.ortherInfo2 != null)
                return false;
        } else if (!ortherInfo2.equals(other.ortherInfo2))
            return false;
        if (ortherInfo3 == null) {
            if (other.ortherInfo3 != null)
                return false;
        } else if (!ortherInfo3.equals(other.ortherInfo3))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PayorNotes [claimNotes=" + claimNotes + ", interalNotes=" + interalNotes + ", ortherInfo1=" + ortherInfo1 + ", ortherInfo2=" + ortherInfo2 + ", ortherInfo3=" + ortherInfo3 + "]";
    }

}
