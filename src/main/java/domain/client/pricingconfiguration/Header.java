package domain.client.pricingconfiguration;

import java.util.Objects;

public class Header {
    private String clientId;
    private String clientName;
    private String accountType;
    private String facAbbr;
    private String facNm;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getFacAbbr() {
        return facAbbr;
    }

    public void setFacAbbr(String facAbbr) {
        this.facAbbr = facAbbr;
    }

    public String getFacNm() {
        return facNm;
    }

    public void setFacNm(String facNm) {
        this.facNm = facNm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(clientId, header.clientId) &&
                Objects.equals(clientName, header.clientName) &&
                Objects.equals(accountType, header.accountType) &&
                Objects.equals(facAbbr, header.facAbbr) &&
                Objects.equals(facNm, header.facNm);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientId, clientName, accountType, facAbbr, facNm);
    }

    @Override
    public String toString() {
        return "Header{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", facAbbr='" + facAbbr + '\'' +
                ", facNm='" + facNm + '\'' +
                '}';
    }
}
