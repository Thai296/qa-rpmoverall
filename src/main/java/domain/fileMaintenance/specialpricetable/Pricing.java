package domain.fileMaintenance.specialpricetable;

//import com.xifin.util.Money;
import com.mbasys.common.utility.Money;

import java.sql.Date;
import java.util.Objects;

public class Pricing {
    //Basis Type: Test Code
    private String testCode;
    private String name;
    private Date effectiveDate;
    private Date expirationDate;
    private Money retail;
    private Money current;
    private Money newPrice;
    //Basis Type: Procedure Code
    private String procedureCode;
    private String mod;
    private Money mcExp;
    private String primaryPayor;
    private boolean isDeleted;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Money getRetail() {
        return retail;
    }

    public void setRetail(Money retail) {
        this.retail = retail;
    }

    public Money getCurrent() {
        return current;
    }

    public void setCurrent(Money current) {
        this.current = current;
    }

    public Money getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Money newPrice) {
        this.newPrice = newPrice;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public Money getMcExp() {
        return mcExp;
    }

    public void setMcExp(Money mcExp) {
        this.mcExp = mcExp;
    }

    public String getPrimaryPayor() {
        return primaryPayor;
    }

    public void setPrimaryPayor(String primaryPayor) {
        this.primaryPayor = primaryPayor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pricing pricing = (Pricing) o;
        return isDeleted == pricing.isDeleted &&
                Objects.equals(testCode, pricing.testCode) &&
                Objects.equals(name, pricing.name) &&
                Objects.equals(effectiveDate, pricing.effectiveDate) &&
                Objects.equals(expirationDate, pricing.expirationDate) &&
                Objects.equals(retail, pricing.retail) &&
                Objects.equals(current, pricing.current) &&
                Objects.equals(newPrice, pricing.newPrice) &&
                Objects.equals(procedureCode, pricing.procedureCode) &&
                Objects.equals(mod, pricing.mod) &&
                Objects.equals(mcExp, pricing.mcExp) &&
                Objects.equals(primaryPayor, pricing.primaryPayor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(testCode, name, effectiveDate, expirationDate, retail, current, newPrice, procedureCode, mod, mcExp, primaryPayor, isDeleted);
    }

    @Override
    public String toString() {
        return "Pricing{" +
                "testCode='" + testCode + '\'' +
                ", name='" + name + '\'' +
                ", effectiveDate=" + effectiveDate +
                ", expirationDate=" + expirationDate +
                ", retail=" + retail +
                ", current=" + current +
                ", newPrice=" + newPrice +
                ", procedureCode='" + procedureCode + '\'' +
                ", mod='" + mod + '\'' +
                ", mcExp=" + mcExp +
                ", primaryPayor='" + primaryPayor + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
