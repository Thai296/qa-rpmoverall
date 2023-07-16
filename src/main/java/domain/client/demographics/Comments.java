/**
 * 
 */
package domain.client.demographics;

/**
 * @author thanh.tran
 *
 */
public class Comments {

	private String generalCommnets = "";
	private String internalsCommnets = "";

	public String getGeneralCommnets() {
		return generalCommnets;
	}

	public void setGeneralCommnets(String generalCommnets) {
		this.generalCommnets = generalCommnets;
	}

	public String getInternalsCommnets() {
		return internalsCommnets;
	}

	public void setInternalsCommnets(String internalsCommnets) {
		this.internalsCommnets = internalsCommnets;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((generalCommnets == null) ? 0 : generalCommnets.hashCode());
		result = prime * result + ((internalsCommnets == null) ? 0 : internalsCommnets.hashCode());
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
		Comments other = (Comments) obj;
		if (generalCommnets == null) {
			if (other.generalCommnets != null)
				return false;
		} else if (!generalCommnets.equals(other.generalCommnets))
			return false;
		if (internalsCommnets == null) {
			if (other.internalsCommnets != null)
				return false;
		} else if (!internalsCommnets.equals(other.internalsCommnets))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comments ["
				+ "generalCommnets=" + generalCommnets 
				+ ", internalsCommnets=" + internalsCommnets 
				+ "]";
	}

}
