package domain.payor.payorContactManager;

import java.sql.Date;

import com.mbasys.mars.persistance.ErrorCodeMap;

public class ContactDetail {

	private int seqId;
	private String userId;
	private Date contactDt;
	private String contactInfo;
	private Date followUpDt;
	private String followUpUserId;
	private boolean followUpComplete; 
	private boolean contactDetailVoid;
	private String cpyNotePyrs;
	private Boolean deleted;
	private int resultCode  = ErrorCodeMap.RECORD_FOUND;
	
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Date getFollowUpDt() {
		return followUpDt;
	}
	public void setFollowUpDt(Date followUpDt) {
		this.followUpDt = followUpDt;
	}
	public String getFollowUpUserId() {
		return followUpUserId;
	}
	public void setFollowUpUserId(String followUpUserId) {
		this.followUpUserId = followUpUserId;
	}
	public boolean isFollowUpComplete() {
		return followUpComplete;
	}
	public void setFollowUpComplete(boolean followUpComplete) {
		this.followUpComplete = followUpComplete;
	}
	public boolean isContactDetailVoid() {
		return contactDetailVoid;
	}
	public void setContactDetailVoid(boolean contactDetailVoid) {
		this.contactDetailVoid = contactDetailVoid;
	}
	public String getCpyNotePyrs() {
		return cpyNotePyrs;
	}
	public void setCpyNotePyrs(String cpyNotePyrs) {
		this.cpyNotePyrs = cpyNotePyrs;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public Date getContactDt() {
		return contactDt;
	}
	public void setContactDt(Date contactDt) {
		this.contactDt = contactDt;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contactDetailVoid ? 1231 : 1237);
		result = prime * result + ((contactDt == null) ? 0 : contactDt.hashCode());
		result = prime * result + ((contactInfo == null) ? 0 : contactInfo.hashCode());
		result = prime * result + ((cpyNotePyrs == null) ? 0 : cpyNotePyrs.hashCode());
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + (followUpComplete ? 1231 : 1237);
		result = prime * result + ((followUpDt == null) ? 0 : followUpDt.hashCode());
		result = prime * result + ((followUpUserId == null) ? 0 : followUpUserId.hashCode());
		result = prime * result + resultCode;
		result = prime * result + seqId;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		ContactDetail other = (ContactDetail) obj;
		if (contactDetailVoid != other.contactDetailVoid)
			return false;
		if (contactDt == null) {
			if (other.contactDt != null)
				return false;
		} else if (!contactDt.equals(other.contactDt))
			return false;
		if (contactInfo == null) {
			if (other.contactInfo != null)
				return false;
		} else if (!contactInfo.equals(other.contactInfo))
			return false;
		if (cpyNotePyrs == null) {
			if (other.cpyNotePyrs != null)
				return false;
		} else if (!cpyNotePyrs.equals(other.cpyNotePyrs))
			return false;
		if (deleted == null) {
			if (other.deleted != null)
				return false;
		} else if (!deleted.equals(other.deleted))
			return false;
		if (followUpComplete != other.followUpComplete)
			return false;
		if (followUpDt == null) {
			if (other.followUpDt != null)
				return false;
		} else if (!followUpDt.equals(other.followUpDt))
			return false;
		if (followUpUserId == null) {
			if (other.followUpUserId != null)
				return false;
		} else if (!followUpUserId.equals(other.followUpUserId))
			return false;
		if (resultCode != other.resultCode)
			return false;
		if (seqId != other.seqId)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "contractDetail [seqId=" + seqId + ", userId=" + userId + ", contractDt=" + contactDt
				+ ", contractInfo=" + contactInfo + ", followUpDt=" + followUpDt + ", followUpUserId=" + followUpUserId
				+ ", followUpComplete=" + followUpComplete + ", contactDetailVoid=" + contactDetailVoid
				+ ", cpyNotePyrs=" + cpyNotePyrs + ", deleted=" + deleted + ", resultCode=" + resultCode + "]";
	}
}
