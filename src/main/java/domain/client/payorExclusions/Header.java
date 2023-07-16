package domain.client.payorExclusions;

import java.util.Objects;

public class Header {
    private String clientAbbrv;
    private String accountType;
    private String name;

    public String getClientAbbrv() {
        return clientAbbrv;
    }

    public void setClientAbbrv(String clientAbbrv) {
        this.clientAbbrv = clientAbbrv;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(clientAbbrv, header.clientAbbrv) &&
                Objects.equals(accountType, header.accountType) &&
                Objects.equals(name, header.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientAbbrv, accountType, name);
    }

    @Override
    public String toString() {
        return "Header{" +
                "clientAbbrv='" + clientAbbrv + '\'' +
                ", accountType='" + accountType + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
