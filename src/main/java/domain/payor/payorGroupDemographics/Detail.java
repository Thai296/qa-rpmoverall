package domain.payor.payorGroupDemographics;

import java.sql.Date;

public class Detail {
	// W/O section
	private  String basis;
	private int age;
	private float minWoBalance;
	
	private String manualDemandFormat;
	private boolean useOIGLogic;
	private boolean useGEMSLogic;
	private boolean annualDisclLetter;
	
	//Pricing Suspended section
	private Date pricingEffDate;
	private Date pricingExpDate;
	
	//Billing Suspended section
	private Date billingEffDate;
	private Date billingExpDate;
	
	//Indigent Discount Note(for only Group ID  is [Patient] )
	private String note;
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getBasis() {
		return basis;
	}
	public void setBasis(String basis) {
		this.basis = basis;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public float getMinWoBalance() {
		return minWoBalance;
	}
	public void setMinWoBalance(float minWoBalance) {
		this.minWoBalance = minWoBalance;
	}
	public String getManualDemandFormat() {
		return manualDemandFormat;
	}
	public void setManualDemandFormat(String manualDemandFormat) {
		this.manualDemandFormat = manualDemandFormat;
	}
	public boolean isUseOIGLogic() {
		return useOIGLogic;
	}
	public void setUseOIGLogic(boolean useOIGLogic) {
		this.useOIGLogic = useOIGLogic;
	}
	public boolean isUseGEMSLogic() {
		return useGEMSLogic;
	}
	public void setUseGEMSLogic(boolean useGEMSLogic) {
		this.useGEMSLogic = useGEMSLogic;
	}
	public boolean isAnnualDisclLetter() {
		return annualDisclLetter;
	}
	public void setAnnualDisclLetter(boolean annualDisclLetter) {
		this.annualDisclLetter = annualDisclLetter;
	}
	public Date getPricingEffDate() {
		return pricingEffDate;
	}
	public void setPricingEffDate(Date pricingEffDate) {
		this.pricingEffDate = pricingEffDate;
	}
	public Date getPricingExpDate() {
		return pricingExpDate;
	}
	public void setPricingExpDate(Date pricingExpDate) {
		this.pricingExpDate = pricingExpDate;
	}
	public Date getBillingEffDate() {
		return billingEffDate;
	}
	public void setBillingEffDate(Date billingEffDate) {
		this.billingEffDate = billingEffDate;
	}
	public Date getBillingExpDate() {
		return billingExpDate;
	}
	public void setBillingExpDate(Date billingExpDate) {
		this.billingExpDate = billingExpDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + (annualDisclLetter ? 1231 : 1237);
		result = prime * result + ((basis == null) ? 0 : basis.hashCode());
		result = prime * result + ((billingEffDate == null) ? 0 : billingEffDate.hashCode());
		result = prime * result + ((billingExpDate == null) ? 0 : billingExpDate.hashCode());
		result = prime * result + ((manualDemandFormat == null) ? 0 : manualDemandFormat.hashCode());
		result = prime * result + Float.floatToIntBits(minWoBalance);
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((pricingEffDate == null) ? 0 : pricingEffDate.hashCode());
		result = prime * result + ((pricingExpDate == null) ? 0 : pricingExpDate.hashCode());
		result = prime * result + (useGEMSLogic ? 1231 : 1237);
		result = prime * result + (useOIGLogic ? 1231 : 1237);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Detail other = (Detail) obj;
		if (age != other.age)
			return false;
		if (annualDisclLetter != other.annualDisclLetter)
			return false;
		if (basis == null) {
			if (other.basis != null)
				return false;
		} else if (!basis.equals(other.basis))
			return false;
		if (billingEffDate == null) {
			if (other.billingEffDate != null)
				return false;
		} else if (!billingEffDate.equals(other.billingEffDate))
			return false;
		if (billingExpDate == null) {
			if (other.billingExpDate != null)
				return false;
		} else if (!billingExpDate.equals(other.billingExpDate))
			return false;
		if (manualDemandFormat == null) {
			if (other.manualDemandFormat != null)
				return false;
		} else if (!manualDemandFormat.equals(other.manualDemandFormat))
			return false;
		if (Float.floatToIntBits(minWoBalance) != Float.floatToIntBits(other.minWoBalance))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (pricingEffDate == null) {
			if (other.pricingEffDate != null)
				return false;
		} else if (!pricingEffDate.equals(other.pricingEffDate))
			return false;
		if (pricingExpDate == null) {
			if (other.pricingExpDate != null)
				return false;
		} else if (!pricingExpDate.equals(other.pricingExpDate))
			return false;
		if (useGEMSLogic != other.useGEMSLogic)
			return false;
		if (useOIGLogic != other.useOIGLogic)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Detail [basis=" + basis + ", age=" + age + ", minWoBalance=" + minWoBalance + ", manualDemandFormat="
				+ manualDemandFormat + ", useOIGLogic=" + useOIGLogic + ", useGEMSLogic=" + useGEMSLogic
				+ ", annualDisclLetter=" + annualDisclLetter + ", pricingEffDate=" + pricingEffDate
				+ ", pricingExpDate=" + pricingExpDate + ", billingEffDate=" + billingEffDate + ", billingExpDate="
				+ billingExpDate + ", note=" + note + "]";
	}
}
