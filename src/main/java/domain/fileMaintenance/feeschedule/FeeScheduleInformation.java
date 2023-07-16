package domain.fileMaintenance.feeschedule;

import java.util.Objects;

public class FeeScheduleInformation {
    private String feeScheduleId;
    private boolean isDiscountable;
    private String name;
    private String facility;
    private String accountType;
    private String priceType;
    private String basisType;
    private String displayOption;

    public String getFeeScheduleId() {
        return feeScheduleId;
    }

    public void setFeeScheduleId(String feeScheduleId) {
        this.feeScheduleId = feeScheduleId;
    }

    public boolean isDiscountable() {
        return isDiscountable;
    }

    public void setDiscountable(boolean discountable) {
        isDiscountable = discountable;
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

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getBasisType() {
        return basisType;
    }

    public void setBasisType(String basisType) {
        this.basisType = basisType;
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
        FeeScheduleInformation that = (FeeScheduleInformation) o;
        return isDiscountable == that.isDiscountable &&
                Objects.equals(feeScheduleId, that.feeScheduleId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(facility, that.facility) &&
                Objects.equals(accountType, that.accountType) &&
                Objects.equals(priceType, that.priceType) &&
                Objects.equals(basisType, that.basisType) &&
                Objects.equals(displayOption, that.displayOption);
    }

    @Override
    public int hashCode() {

        return Objects.hash(feeScheduleId, isDiscountable, name, facility, accountType, priceType, basisType, displayOption);
    }

    @Override
    public String toString() {
        return "FeeScheduleInformation{" +
                "feeScheduleId='" + feeScheduleId + '\'' +
                ", isDiscountable=" + isDiscountable +
                ", name='" + name + '\'' +
                ", facility='" + facility + '\'' +
                ", accountType='" + accountType + '\'' +
                ", priceType='" + priceType + '\'' +
                ", basisType='" + basisType + '\'' +
                ", displayOption='" + displayOption + '\'' +
                '}';
    }
}
