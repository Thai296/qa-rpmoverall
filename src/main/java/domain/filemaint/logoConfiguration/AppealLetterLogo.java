package domain.filemaint.logoConfiguration;

import java.sql.Date;

public class AppealLetterLogo {
	private boolean updateLogo;
	private boolean uploadLogo;
	private String availableLogos;
	private Date dateUploaded;
	private int logoSize;
	private String logoLocation;
	private String dir;

	public boolean isUpdateLogo() {
		return updateLogo;
	}

	public void setUpdateLogo(boolean updateLogo) {
		this.updateLogo = updateLogo;
	}

	public boolean isUploadLogo() {
		return uploadLogo;
	}

	public void setUploadLogo(boolean uploadLogo) {
		this.uploadLogo = uploadLogo;
	}

	public String getAvailableLogos() {
		return availableLogos;
	}

	public void setAvailableLogos(String availableLogos) {
		this.availableLogos = availableLogos;
	}

	public Date getDateUploaded() {
		return dateUploaded;
	}

	public void setDateUploaded(Date dateUploaded) {
		this.dateUploaded = dateUploaded;
	}

	public int getLogoSize() {
		return logoSize;
	}

	public void setLogoSize(int logoSize) {
		this.logoSize = logoSize;
	}

	public String getLogoLocation() {
		return logoLocation;
	}

	public void setLogoLocation(String logoLocation) {
		this.logoLocation = logoLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((availableLogos == null) ? 0 : availableLogos.hashCode());
		result = prime * result + ((dateUploaded == null) ? 0 : dateUploaded.hashCode());
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		result = prime * result + ((logoLocation == null) ? 0 : logoLocation.hashCode());
		result = prime * result + logoSize;
		result = prime * result + (updateLogo ? 1231 : 1237);
		result = prime * result + (uploadLogo ? 1231 : 1237);
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
		AppealLetterLogo other = (AppealLetterLogo) obj;
		if (availableLogos == null) {
			if (other.availableLogos != null)
				return false;
		} else if (!availableLogos.equals(other.availableLogos))
			return false;
		if (dateUploaded == null) {
			if (other.dateUploaded != null)
				return false;
		} else if (!dateUploaded.equals(other.dateUploaded))
			return false;
		if (dir == null) {
			if (other.dir != null)
				return false;
		} else if (!dir.equals(other.dir))
			return false;
		if (logoLocation == null) {
			if (other.logoLocation != null)
				return false;
		} else if (!logoLocation.equals(other.logoLocation))
			return false;
		if (logoSize != other.logoSize)
			return false;
		if (updateLogo != other.updateLogo)
			return false;
		if (uploadLogo != other.uploadLogo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AppealLetterLogo [updateLogo=" + updateLogo + ", uploadLogo=" + uploadLogo + ", availableLogos="
				+ availableLogos + ", dateUploaded=" + dateUploaded + ", logoSize=" + logoSize + ", logoLocation="
				+ logoLocation + ", dir=" + dir + "]";
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
