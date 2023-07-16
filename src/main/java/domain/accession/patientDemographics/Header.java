package domain.accession.patientDemographics;

public class Header {
    private String epi;
    private int patientSSN;

    public String getEpi() {
		return epi;
	}

	public void setEpi(String epi) {
		this.epi = epi;
	}

    public int getPatientSSN() {
        return patientSSN;
    }

    public void setPatientSSN(int patientSSN) {
        this.patientSSN = patientSSN;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((epi == null) ? 0 : epi.hashCode());
		result = prime * result + patientSSN;
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
		if (epi == null) {
			if (other.epi != null)
				return false;
		} else if (!epi.equals(other.epi))
			return false;
		if (patientSSN != other.patientSSN)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Header [epi=" + epi + ", patientSSN=" + patientSSN + "]";
	}

}
