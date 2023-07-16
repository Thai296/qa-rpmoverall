package domain.payor.payorContactManager;

public class Header {
	private String pyrId;
	private String name;
	private String grpName;
	
	public String getPyrId() {
		return pyrId;
	}

	public void setPyrId(String pyrId) {
		this.pyrId = pyrId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGrpName() {
		return grpName;
	}

	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grpName == null) ? 0 : grpName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pyrId == null) ? 0 : pyrId.hashCode());
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
		if (grpName == null) {
			if (other.grpName != null)
				return false;
		} else if (!grpName.equals(other.grpName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pyrId == null) {
			if (other.pyrId != null)
				return false;
		} else if (!pyrId.equals(other.pyrId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Header [pyrId=" + pyrId + ", name=" + name + ", grpName=" + grpName + "]";
	}

}
