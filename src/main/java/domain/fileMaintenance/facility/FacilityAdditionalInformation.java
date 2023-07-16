package domain.fileMaintenance.facility;

import java.util.Objects;
import java.util.Set;


public class FacilityAdditionalInformation {
    private boolean isDiagnosticLab;
    private boolean isMammogramHighRiskPatient;
    private boolean isMammogramLowRiskPatient;
    private boolean isPathology;
    private boolean isRadiology;
    private String taxId;
    private String clia;
    private String organizationalNpi;
    private String facilityNpi;
    private String taxonomyCode;
    private Set<String> facilityGroup;
    private String facilitySpecialtyType;

    @Override
    public String toString()
    {
        return "FacilityAdditionalInformation{" +
                "isDiagnosticLab=" + isDiagnosticLab +
                ", isMammogramHighRiskPatient=" + isMammogramHighRiskPatient +
                ", isMammogramLowRiskPatient=" + isMammogramLowRiskPatient +
                ", isPathology=" + isPathology +
                ", isRadiology=" + isRadiology +
                ", taxId='" + taxId + '\'' +
                ", clia='" + clia + '\'' +
                ", organizationalNpi='" + organizationalNpi + '\'' +
                ", facilityNpi='" + facilityNpi + '\'' +
                ", taxonomyCode='" + taxonomyCode + '\'' +
                ", facilityGroup=" + facilityGroup +
                ", facilitySpecialtyType='" + facilitySpecialtyType + '\'' +
                ", mammoCert='" + mammoCert + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FacilityAdditionalInformation that = (FacilityAdditionalInformation) o;
        return isDiagnosticLab == that.isDiagnosticLab &&
                isMammogramHighRiskPatient == that.isMammogramHighRiskPatient &&
                isMammogramLowRiskPatient == that.isMammogramLowRiskPatient &&
                isPathology == that.isPathology &&
                isRadiology == that.isRadiology &&
                Objects.equals(taxId, that.taxId) &&
                Objects.equals(clia, that.clia) &&
                Objects.equals(organizationalNpi, that.organizationalNpi) &&
                Objects.equals(facilityNpi, that.facilityNpi) &&
                Objects.equals(taxonomyCode, that.taxonomyCode) &&
                Objects.equals(facilityGroup, that.facilityGroup) &&
                Objects.equals(facilitySpecialtyType, that.facilitySpecialtyType) &&
                Objects.equals(mammoCert, that.mammoCert);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(isDiagnosticLab, isMammogramHighRiskPatient, isMammogramLowRiskPatient, isPathology, isRadiology, taxId, clia, organizationalNpi, facilityNpi, taxonomyCode, facilityGroup, facilitySpecialtyType, mammoCert);
    }

    public String getFacilitySpecialtyType()
    {
        return facilitySpecialtyType;
    }

    public void setFacilitySpecialtyType(String facilitySpecialtyType)
    {
        this.facilitySpecialtyType = facilitySpecialtyType;
    }


    private String mammoCert;


    public boolean isDiagnosticLab() {
        return isDiagnosticLab;
    }
    public void setDiagnosticLab(boolean isDiagnosticLab) {
        this.isDiagnosticLab = isDiagnosticLab;
    }
    public boolean isMammogramHighRiskPatient() {
        return isMammogramHighRiskPatient;
    }
    public void setMammogramHighRiskPatient(boolean isMammogramHighRiskPatient) {
        this.isMammogramHighRiskPatient = isMammogramHighRiskPatient;
    }
    public boolean isMammogramLowRiskPatient() {
        return isMammogramLowRiskPatient;
    }
    public void setMammogramLowRiskPatient(boolean isMammogramLowRiskPatient) {
        this.isMammogramLowRiskPatient = isMammogramLowRiskPatient;
    }
    public boolean isPathology() {
        return isPathology;
    }
    public void setPathology(boolean isPathology) {
        this.isPathology = isPathology;
    }
    public boolean isRadiology() {
        return isRadiology;
    }
    public void setRadiology(boolean isRadiology) {
        this.isRadiology = isRadiology;
    }
    public String getTaxId() {
        return taxId;
    }
    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }
    public String getClia() {
        return clia;
    }
    public void setClia(String clia) {
        this.clia = clia;
    }
    public String getOrganizationalNpi() {
        return organizationalNpi;
    }
    public void setOrganizationalNpi(String organizationalNpi) {
        this.organizationalNpi = organizationalNpi;
    }
    public String getTaxonomyCode() {
        return taxonomyCode;
    }
    public void setTaxonomyCode(String taxonomyCode) {
        this.taxonomyCode = taxonomyCode;
    }

    public Set<String> getFacilityGroup() {
        return facilityGroup;
    }
    public void setFacilityGroup(Set<String> facilityGroup) {
        this.facilityGroup = facilityGroup;
    }
    public String getFacilityNpi() {
        return facilityNpi;
    }
    public void setFacilityNpi(String facilityNpi) {
        this.facilityNpi = facilityNpi;
    }
    public String getMammoCert() {
        return mammoCert;
    }
    public void setMammoCert(String mammoCert) {
        this.mammoCert = mammoCert;
    }



}

