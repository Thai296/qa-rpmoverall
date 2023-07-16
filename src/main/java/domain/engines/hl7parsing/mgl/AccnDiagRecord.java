package domain.engines.hl7parsing.mgl;

import java.util.Objects;

public class AccnDiagRecord {
    private String diagCdId;
    private int diagTypId;

    public String getDiagCdId() {
        return diagCdId;
    }

    public void setDiagCdId(String diagCdId) {
        this.diagCdId = diagCdId;
    }

    public int getDiagTypId() {
        return diagTypId;
    }

    public void setDiagTypId(int diagTypId) {
        this.diagTypId = diagTypId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccnDiagRecord accnDiag = (AccnDiagRecord) o;
        return diagTypId == accnDiag.diagTypId &&
                Objects.equals(diagCdId, accnDiag.diagCdId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(diagCdId, diagTypId);
    }

    @Override
    public String toString() {
        return "AccnDiagRecord{" +
                "diagCdId='" + diagCdId + '\'' +
                ", diagTypId=" + diagTypId +
                '}';
    }
}
