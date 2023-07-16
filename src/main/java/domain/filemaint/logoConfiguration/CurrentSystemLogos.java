package domain.filemaint.logoConfiguration;

import java.sql.Date;

public class CurrentSystemLogos {
	private String fileName;
	private String docType;
	private Date uploadDt;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public Date getUploadDt() {
		return uploadDt;
	}
	public void setUploadDt(Date uploadDt) {
		this.uploadDt = uploadDt;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docType == null) ? 0 : docType.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((uploadDt == null) ? 0 : uploadDt.hashCode());
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
		CurrentSystemLogos other = (CurrentSystemLogos) obj;
		if (docType == null) {
			if (other.docType != null)
				return false;
		} else if (!docType.equals(other.docType))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (uploadDt == null) {
			if (other.uploadDt != null)
				return false;
		} else if (!uploadDt.equals(other.uploadDt))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "CurrentSystemLogos [fileName=" + fileName + ", docType=" + docType + ", uploadDt=" + uploadDt + "]";
	}
}
