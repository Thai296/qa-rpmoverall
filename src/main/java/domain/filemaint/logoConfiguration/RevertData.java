package domain.filemaint.logoConfiguration;

import java.util.ArrayList;
import java.util.List;

import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.systemSetting.SystemSetting;

public class RevertData {
	private List<SystemSetting> systemSetting = new ArrayList<SystemSetting>();
	private Fac fac;
	private String clnSmtm;

	public List<SystemSetting> getSystemSetting() {
		return systemSetting;
	}

	public void setSystemSetting(List<SystemSetting> systemSetting) {
		this.systemSetting = systemSetting;
	}

	public Fac getFac() {
		return fac;
	}

	public void setFac(Fac fac) {
		this.fac = fac;
	}

	public String getClnSmtm() {
		return clnSmtm;
	}

	public void setClnSmtm(String clnSmtm) {
		this.clnSmtm = clnSmtm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clnSmtm == null) ? 0 : clnSmtm.hashCode());
		result = prime * result + ((fac == null) ? 0 : fac.hashCode());
		result = prime * result + ((systemSetting == null) ? 0 : systemSetting.hashCode());
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
		RevertData other = (RevertData) obj;
		if (clnSmtm == null) {
			if (other.clnSmtm != null)
				return false;
		} else if (!clnSmtm.equals(other.clnSmtm))
			return false;
		if (fac == null) {
			if (other.fac != null)
				return false;
		} else if (!fac.equals(other.fac))
			return false;
		if (systemSetting == null) {
			if (other.systemSetting != null)
				return false;
		} else if (!systemSetting.equals(other.systemSetting))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RevertData [systemSetting=" + systemSetting + ", fac=" + fac + ", clnSmtm=" + clnSmtm + "]";
	}

}
