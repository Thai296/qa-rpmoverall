package domain.payor.payorGroupDemographics;

import com.mbasys.common.utility.Money;
import com.mbasys.mars.persistance.ErrorCodeMap;

@SuppressWarnings("deprecation")
public class ClientDunningOptions {
	private int seqId;
	private String agingBucket;
	private int docId;
	private Money mir;
	private int resultCode = ErrorCodeMap.RECORD_FOUND;

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public String getAgingBucket() {
		return agingBucket;
	}

	public void setAgingBucket(String agingBucket) {
		this.agingBucket = agingBucket;
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public Money getMir() {
		return mir;
	}

	public void setMir(Money monIntRate) {
		this.mir = monIntRate;
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
		result = prime * result + ((agingBucket == null) ? 0 : agingBucket.hashCode());
		result = prime * result + docId;
		result = prime * result + ((mir == null) ? 0 : mir.hashCode());
		result = prime * result + resultCode;
		result = prime * result + seqId;
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
		ClientDunningOptions other = (ClientDunningOptions) obj;
		if (agingBucket == null) {
			if (other.agingBucket != null)
				return false;
		} else if (!agingBucket.equals(other.agingBucket))
			return false;
		if (docId != other.docId)
			return false;
		if (mir == null) {
			if (other.mir != null)
				return false;
		} else if (!mir.equals(other.mir))
			return false;
		if (resultCode != other.resultCode)
			return false;
		if (seqId != other.seqId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientDunningOptions [seqId=" + seqId + ", agingBucket=" + agingBucket + ", docId=" + docId + ", mir="
				+ mir + ", resultCode=" + resultCode + "]";
	}

}
