package domain.accession.patientDemographics;

import java.sql.Date;

public class AllAccessionsForThisPatientTable {

    private Date dos;
    private String accessionId;
    private String accessionStatus;
    private String orderingPhysician;
    private String clientId;
    private String primaryPayor;
	private Double paid = 0.00;
	private Double adj = 0.00;
	private Double balanceDue = 0.00;
    private String statementStatus;

    public Date getDos() {
        return dos;
    }

    public void setDos(Date dos) {
        this.dos = dos;
    }

    public String getAccessionId() {
        return accessionId;
    }

    public void setAccessionId(String accessionId) {
        this.accessionId = accessionId;
    }

    public String getAccessionStatus() {
        return accessionStatus;
    }

    public void setAccessionStatus(String accessionStatus) {
        this.accessionStatus = accessionStatus;
    }

    public String getOrderingPhysician() {
        return orderingPhysician;
    }

    public void setOrderingPhysician(String orderingPhysician) {
        this.orderingPhysician = orderingPhysician;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPrimaryPayor() {
        return primaryPayor;
    }

    public void setPrimaryPayor(String primaryPayor) {
        this.primaryPayor = primaryPayor;
    }

    public Double getPaid() {
		return paid;
	}

	public void setPaid(Double paid) {
		this.paid = paid;
	}

	public Double getAdj() {
		return adj;
	}

	public void setAdj(Double adj) {
		this.adj = adj;
	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(Double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public String getStatementStatus() {
        return statementStatus;
    }

    public void setStatementStatus(String statementStatus) {
        this.statementStatus = statementStatus;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessionId == null) ? 0 : accessionId.hashCode());
		result = prime * result + ((accessionStatus == null) ? 0 : accessionStatus.hashCode());
		result = prime * result + ((adj == null) ? 0 : adj.hashCode());
		result = prime * result + ((balanceDue == null) ? 0 : balanceDue.hashCode());
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((dos == null) ? 0 : dos.hashCode());
		result = prime * result + ((orderingPhysician == null) ? 0 : orderingPhysician.hashCode());
		result = prime * result + ((paid == null) ? 0 : paid.hashCode());
		result = prime * result + ((primaryPayor == null) ? 0 : primaryPayor.hashCode());
		result = prime * result + ((statementStatus == null) ? 0 : statementStatus.hashCode());
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
		AllAccessionsForThisPatientTable other = (AllAccessionsForThisPatientTable) obj;
		if (accessionId == null) {
			if (other.accessionId != null)
				return false;
		} else if (!accessionId.equals(other.accessionId))
			return false;
		if (accessionStatus == null) {
			if (other.accessionStatus != null)
				return false;
		} else if (!accessionStatus.equals(other.accessionStatus))
			return false;
		if (adj == null) {
			if (other.adj != null)
				return false;
		} else if (!adj.equals(other.adj))
			return false;
		if (balanceDue == null) {
			if (other.balanceDue != null)
				return false;
		} else if (!balanceDue.equals(other.balanceDue))
			return false;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (dos == null) {
			if (other.dos != null)
				return false;
		} else if (!dos.equals(other.dos))
			return false;
		if (orderingPhysician == null) {
			if (other.orderingPhysician != null)
				return false;
		} else if (!orderingPhysician.equals(other.orderingPhysician))
			return false;
		if (paid == null) {
			if (other.paid != null)
				return false;
		} else if (!paid.equals(other.paid))
			return false;
		if (primaryPayor == null) {
			if (other.primaryPayor != null)
				return false;
		} else if (!primaryPayor.equals(other.primaryPayor))
			return false;
		if (statementStatus == null) {
			if (other.statementStatus != null)
				return false;
		} else if (!statementStatus.equals(other.statementStatus))
			return false;
		return true;
	}

    @Override
	public String toString() {
		return "AllAccessionsForThisPatientTable [dos=" + dos + ", accessionId=" + accessionId + ", accessionStatus="
				+ accessionStatus + ", orderingPhysician=" + orderingPhysician + ", clientId=" + clientId
				+ ", primaryPayor=" + primaryPayor + ", paid=" + paid + ", adj=" + adj + ", balanceDue=" + balanceDue
				+ ", statementStatus=" + statementStatus + "]";
	}

}
