package domain.engines.x12interface;

import java.util.Objects;

public class EobSvcRemarkCdRecord {
    private String remarkCd;

    public String getRemarkCd() {
        return remarkCd;
    }

    public void setRemarkCd(String remarkCd) {
        this.remarkCd = remarkCd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EobSvcRemarkCdRecord that = (EobSvcRemarkCdRecord) o;
        return Objects.equals(remarkCd, that.remarkCd);
    }

    @Override
    public int hashCode() {

        return Objects.hash(remarkCd);
    }

    @Override
    public String toString() {
        return "EobSvcRemarkCdRecord{" +
                "remarkCd='" + remarkCd + '\'' +
                '}';
    }
}
