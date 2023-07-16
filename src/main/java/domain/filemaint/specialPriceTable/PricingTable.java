package domain.filemaint.specialPriceTable;

import java.sql.Date;

import com.mbasys.mars.persistance.ErrorCodeMap;
import com.xifin.util.Money;

public class PricingTable {
	private String itemAbbrev;
	private String prcId;
	private String testId;
	private String testAbbrev;
	private String procId;
	private String procModifier;
	private String itemName;
	private Date effDate;
	private Date expDate;
	private String retail;
	private Money mcExpect;
	private Money currentPrice;
	private Money newPrice;
	private boolean isFlatFee;
	private int disc;
	private boolean isDelete;
	private int resultCode = ErrorCodeMap.RECORD_FOUND;
	
	public String getItemAbbrev() {
		return itemAbbrev;
	}
	public void setItemAbbrev(String itemAbbrev) {
		this.itemAbbrev = itemAbbrev;
	}
	public String getPrcId() {
		return prcId;
	}
	public void setPrcId(String prcId) {
		this.prcId = prcId;
	}
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	public String getTestAbbrev() {
		return testAbbrev;
	}
	public void setTestAbbrev(String testAbbrev) {
		this.testAbbrev = testAbbrev;
	}
	public String getProcId() {
		return procId;
	}
	public void setProcId(String procId) {
		this.procId = procId;
	}
	public String getProcModifier() {
		return procModifier;
	}
	public void setProcModifier(String procModifier) {
		this.procModifier = procModifier;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Date getEffDate() {
		return effDate;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public Date getExpDate() {
		return expDate;
	}
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}
	public String getRetail() {
		return retail;
	}
	public void setRetail(String retail) {
		this.retail = retail;
	}
	public Money getMcExpect() {
		return mcExpect;
	}
	public void setMcExpect(Money mcExpect) {
		this.mcExpect = mcExpect;
	}
	public Money getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(Money currentPrice) {
		this.currentPrice = currentPrice;
	}
	public Money getNewPrice() {
		return newPrice;
	}
	public void setNewPrice(Money newPrice) {
		this.newPrice = newPrice;
	}
	public boolean isFlatFee() {
		return isFlatFee;
	}
	public void setFlatFee(boolean flatFee) {
		this.isFlatFee = flatFee;
	}
	public int getDisc() {
		return disc;
	}
	public void setDisc(int disc) {
		this.disc = disc;
	}
	public boolean isDelete() {
		return isDelete;
	}
	public void setDelete(boolean delete) {
		this.isDelete = delete;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentPrice == null) ? 0 : currentPrice.hashCode());
		result = prime * result + (isDelete ? 1231 : 1237);
		result = prime * result + disc;
		result = prime * result + ((effDate == null) ? 0 : effDate.hashCode());
		result = prime * result + ((expDate == null) ? 0 : expDate.hashCode());
		result = prime * result + (isFlatFee ? 1231 : 1237);
		result = prime * result + ((itemAbbrev == null) ? 0 : itemAbbrev.hashCode());
		result = prime * result + ((itemName == null) ? 0 : itemName.hashCode());
		result = prime * result + ((mcExpect == null) ? 0 : mcExpect.hashCode());
		result = prime * result + ((newPrice == null) ? 0 : newPrice.hashCode());
		result = prime * result + ((prcId == null) ? 0 : prcId.hashCode());
		result = prime * result + ((procId == null) ? 0 : procId.hashCode());
		result = prime * result + ((procModifier == null) ? 0 : procModifier.hashCode());
		result = prime * result + resultCode;
		result = prime * result + ((retail == null) ? 0 : retail.hashCode());
		result = prime * result + ((testAbbrev == null) ? 0 : testAbbrev.hashCode());
		result = prime * result + ((testId == null) ? 0 : testId.hashCode());
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
		PricingTable other = (PricingTable) obj;
		if (currentPrice == null) {
			if (other.currentPrice != null)
				return false;
		} else if (!currentPrice.equals(other.currentPrice))
			return false;
		if (isDelete != other.isDelete)
			return false;
		if (disc != other.disc)
			return false;
		if (effDate == null) {
			if (other.effDate != null)
				return false;
		} else if (!effDate.equals(other.effDate))
			return false;
		if (expDate == null) {
			if (other.expDate != null)
				return false;
		} else if (!expDate.equals(other.expDate))
			return false;
		if (isFlatFee != other.isFlatFee)
			return false;
		if (itemAbbrev == null) {
			if (other.itemAbbrev != null)
				return false;
		} else if (!itemAbbrev.equals(other.itemAbbrev))
			return false;
		if (itemName == null) {
			if (other.itemName != null)
				return false;
		} else if (!itemName.equals(other.itemName))
			return false;
		if (mcExpect == null) {
			if (other.mcExpect != null)
				return false;
		} else if (!mcExpect.equals(other.mcExpect))
			return false;
		if (newPrice == null) {
			if (other.newPrice != null)
				return false;
		} else if (!newPrice.equals(other.newPrice))
			return false;
		if (prcId == null) {
			if (other.prcId != null)
				return false;
		} else if (!prcId.equals(other.prcId))
			return false;
		if (procId == null) {
			if (other.procId != null)
				return false;
		} else if (!procId.equals(other.procId))
			return false;
		if (procModifier == null) {
			if (other.procModifier != null)
				return false;
		} else if (!procModifier.equals(other.procModifier))
			return false;
		if (resultCode != other.resultCode)
			return false;
		if (retail == null) {
			if (other.retail != null)
				return false;
		} else if (!retail.equals(other.retail))
			return false;
		if (testAbbrev == null) {
			if (other.testAbbrev != null)
				return false;
		} else if (!testAbbrev.equals(other.testAbbrev))
			return false;
		if (testId == null) {
			if (other.testId != null)
				return false;
		} else if (!testId.equals(other.testId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "pricingTable [itemAbbrev=" + itemAbbrev + ", prcId=" + prcId + ", testId=" + testId + ", testAbbrev="
				+ testAbbrev + ", procId=" + procId + ", procModifier=" + procModifier + ", itemName=" + itemName
				+ ", effDate=" + effDate + ", expDate=" + expDate + ", retail=" + retail + ", mcExpect=" + mcExpect
				+ ", currentPrice=" + currentPrice + ", newPrice=" + newPrice + ", flatFee=" + isFlatFee + ", disc="
				+ disc + ", delete=" + isDelete + ", resultCode=" + resultCode + "]";
	}
}
