package domain.fileMaintenance.specialpricetable;

import java.util.Objects;

public class SpecialPriceTableInformation {
    private String specialPriceTableId;
    private String name;
    private String facility;
    private String accountType;
    private String displayOption;

    public String getSpecialPriceTableId() {
        return specialPriceTableId;
    }

    public void setSpecialPriceTableId(String specialPriceTableId) {
        this.specialPriceTableId = specialPriceTableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDisplayOption() {
        return displayOption;
    }

    public void setDisplayOption(String displayOption) {
        this.displayOption = displayOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialPriceTableInformation that = (SpecialPriceTableInformation) o;
        return Objects.equals(specialPriceTableId, that.specialPriceTableId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(facility, that.facility) &&
                Objects.equals(accountType, that.accountType) &&
                Objects.equals(displayOption, that.displayOption);
    }

    @Override
    public int hashCode() {

        return Objects.hash(specialPriceTableId, name, facility, accountType, displayOption);
    }

    @Override
    public String toString() {
        return "SpecialPriceTableInformation{" +
                "specialPriceTableId='" + specialPriceTableId + '\'' +
                ", name='" + name + '\'' +
                ", facility='" + facility + '\'' +
                ", accountType='" + accountType + '\'' +
                ", displayOption='" + displayOption + '\'' +
                '}';
    }
}
