package domain.accession.dailyReceipt;

public class DailyReceiptSummarySection {
    private String bagId="";
    private String drawerId="";
    private float cashCountedAtDrawerOpening;
    private float cashCountedAtDrawerClosing;
    private String cashDrawerOpenedBy="";
    private String cashDrawerClosedBy="";
    private String verifiedByUser="";
    private double totalCash;
	private float cashOverUnder;
    private String cashAcctUseOnly="";
    private float totalCheck;
    private String checkAcctUseOnly="";
    private String plsExplainAnyOveragesShortages="";
    
    public double getTotalCash() {
		return totalCash;
	}

	public void setTotalCheck(float totalCheck) {
		this.totalCheck = totalCheck;
	}

	public float getCashCountedAtDrawerOpening() {
		return cashCountedAtDrawerOpening;
	}

	public void setTotalCash(float totalCash) {
		this.totalCash = totalCash;
	}

    public void setCashCountedAtDrawerOpening(float cashCountedAtDrawerOpening) {
		this.cashCountedAtDrawerOpening = cashCountedAtDrawerOpening;
	}

	public String getBagId() {
        return bagId;
    }
    
    public void setBagId(String bagId) {
        this.bagId = bagId;
    }
    
    public String getDrawerId() {
        return drawerId;
    }
    
    public void setDrawerId(String drawerId) {
        this.drawerId = drawerId;
    }
    
    public float getCashCountedAtDrawerClosing() {
        return cashCountedAtDrawerClosing;
    }
    
    public void setCashCountedAtDrawerClosing(float cashCountedAtDrawerClosing) {
        this.cashCountedAtDrawerClosing = cashCountedAtDrawerClosing;
    }
    
    public String getCashDrawerOpenedBy() {
        return cashDrawerOpenedBy;
    }
    
    public void setCashDrawerOpenedBy(String cashDrawerOpenedBy) {
        this.cashDrawerOpenedBy = cashDrawerOpenedBy;
    }
    
    public String getCashDrawerClosedBy() {
        return cashDrawerClosedBy;
    }
    
    public void setCashDrawerClosedBy(String cashDrawerClosedBy) {
        this.cashDrawerClosedBy = cashDrawerClosedBy;
    }
    
    public String getVerifiedByUser() {
        return verifiedByUser;
    }
    
    public void setVerifiedByUser(String verifiedByUser) {
        this.verifiedByUser = verifiedByUser;
    }
    
    public float getCashOverUnder() {
        return cashOverUnder;
    }
    
    public void setCashOverUnder(float cashOverUnder) {
        this.cashOverUnder = cashOverUnder;
    }
    
    public String getCashAcctUseOnly() {
        return cashAcctUseOnly;
    }
    
    public void setCashAcctUseOnly(String cashAcctUseOnly) {
        this.cashAcctUseOnly = cashAcctUseOnly;
    }
    
    public String getCheckAcctUseOnly() {
        return checkAcctUseOnly;
    }
    
    public void setCheckAcctUseOnly(String checkAcctUseOnly) {
        this.checkAcctUseOnly = checkAcctUseOnly;
    }
    
    public String getPlsExplainAnyOveragesShortages() {
        return plsExplainAnyOveragesShortages;
    }
    
    public void setPlsExplainAnyOveragesShortages(String plsExplainAnyOveragesShortages) {
        this.plsExplainAnyOveragesShortages = plsExplainAnyOveragesShortages;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bagId == null) ? 0 : bagId.hashCode());
		result = prime * result + ((cashAcctUseOnly == null) ? 0 : cashAcctUseOnly.hashCode());
		result = prime * result + Float.floatToIntBits(cashCountedAtDrawerClosing);
		result = prime * result + Float.floatToIntBits(cashCountedAtDrawerOpening);
		result = prime * result + ((cashDrawerClosedBy == null) ? 0 : cashDrawerClosedBy.hashCode());
		result = prime * result + ((cashDrawerOpenedBy == null) ? 0 : cashDrawerOpenedBy.hashCode());
		result = prime * result + Float.floatToIntBits(cashOverUnder);
		result = prime * result + ((checkAcctUseOnly == null) ? 0 : checkAcctUseOnly.hashCode());
		result = prime * result + ((drawerId == null) ? 0 : drawerId.hashCode());
		result = prime * result
				+ ((plsExplainAnyOveragesShortages == null) ? 0 : plsExplainAnyOveragesShortages.hashCode());
		long temp;
		temp = Double.doubleToLongBits(totalCash);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(totalCheck);
		result = prime * result + ((verifiedByUser == null) ? 0 : verifiedByUser.hashCode());
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
		DailyReceiptSummarySection other = (DailyReceiptSummarySection) obj;
		if (bagId == null) {
			if (other.bagId != null)
				return false;
		} else if (!bagId.equals(other.bagId))
			return false;
		if (cashAcctUseOnly == null) {
			if (other.cashAcctUseOnly != null)
				return false;
		} else if (!cashAcctUseOnly.equals(other.cashAcctUseOnly))
			return false;
		if (Float.floatToIntBits(cashCountedAtDrawerClosing) != Float.floatToIntBits(other.cashCountedAtDrawerClosing))
			return false;
		if (Float.floatToIntBits(cashCountedAtDrawerOpening) != Float.floatToIntBits(other.cashCountedAtDrawerOpening))
			return false;
		if (cashDrawerClosedBy == null) {
			if (other.cashDrawerClosedBy != null)
				return false;
		} else if (!cashDrawerClosedBy.equals(other.cashDrawerClosedBy))
			return false;
		if (cashDrawerOpenedBy == null) {
			if (other.cashDrawerOpenedBy != null)
				return false;
		} else if (!cashDrawerOpenedBy.equals(other.cashDrawerOpenedBy))
			return false;
		if (Float.floatToIntBits(cashOverUnder) != Float.floatToIntBits(other.cashOverUnder))
			return false;
		if (checkAcctUseOnly == null) {
			if (other.checkAcctUseOnly != null)
				return false;
		} else if (!checkAcctUseOnly.equals(other.checkAcctUseOnly))
			return false;
		if (drawerId == null) {
			if (other.drawerId != null)
				return false;
		} else if (!drawerId.equals(other.drawerId))
			return false;
		if (plsExplainAnyOveragesShortages == null) {
			if (other.plsExplainAnyOveragesShortages != null)
				return false;
		} else if (!plsExplainAnyOveragesShortages.equals(other.plsExplainAnyOveragesShortages))
			return false;
		if (Double.doubleToLongBits(totalCash) != Double.doubleToLongBits(other.totalCash))
			return false;
		if (Float.floatToIntBits(totalCheck) != Float.floatToIntBits(other.totalCheck))
			return false;
		if (verifiedByUser == null) {
			if (other.verifiedByUser != null)
				return false;
		} else if (!verifiedByUser.equals(other.verifiedByUser))
			return false;
		return true;
	}


    public float getTotalCheck() {
		return totalCheck;
	}

	public void setTotalCash(double totalCash) {
		this.totalCash = totalCash;
	}

	@Override
	public String toString() {
		return "DailyReceiptSummarySection [bagId=" + bagId + ", drawerId=" + drawerId + ", cashCountedAtDrawerOpening="
				+ cashCountedAtDrawerOpening + ", cashCountedAtDrawerClosing=" + cashCountedAtDrawerClosing
				+ ", cashDrawerOpenedBy=" + cashDrawerOpenedBy + ", cashDrawerClosedBy=" + cashDrawerClosedBy
				+ ", verifiedByUser=" + verifiedByUser + ", totalCash=" + totalCash + ", cashOverUnder=" + cashOverUnder
				+ ", cashAcctUseOnly=" + cashAcctUseOnly + ", totalCheck=" + totalCheck + ", checkAcctUseOnly="
				+ checkAcctUseOnly + ", plsExplainAnyOveragesShortages=" + plsExplainAnyOveragesShortages + "]";
	}

    
}
