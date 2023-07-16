package domain.payor.payorGroupDemographics;

public class Header {
	private String grpId;
	private String name;
	private boolean contracted;
	
	public String getGrpId() {
		return grpId;
	}
	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isContracted() {
		return contracted;
	}
	public void setContracted(boolean contracted) {
		this.contracted = contracted;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contracted ? 1231 : 1237);
		result = prime * result + ((grpId == null) ? 0 : grpId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Header other = (Header) obj;
		if (contracted != other.contracted)
			return false;
		if (grpId == null) {
			if (other.grpId != null)
				return false;
		} else if (!grpId.equals(other.grpId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Header [grpId=" + grpId + ", name=" + name + ", contracted=" + contracted + "]";
	}
}
