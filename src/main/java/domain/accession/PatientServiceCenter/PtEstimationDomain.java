package domain.accession.PatientServiceCenter;

import com.xifin.utils.SeleniumBaseTest;

public class PtEstimationDomain extends SeleniumBaseTest
{
    private String ptFirstName;
    private String ptLastName;
    private String ptDateOfBirth;
    private String ptGender;
    private String subscriberId;
    private String relationship;
    private String insuredFirstName;
    private String insuredLastName;
    private String insuredDateOfBirth;
    private String insuredGender;
    private String payorId;
    private String clientId;
    private String DateOfService;
    private String testId;
    private String testDescription;
    private String payorName;
    private String orderId;
    private Boolean isResp;
    private String note;
    private String accnId;

    public String getTestId()
    {
        return testId;
    }

    public void setTestId(String testId)
    {
        this.testId = testId;
    }

    public String getTestDescription()
    {
        return testDescription;
    }

    public void setTestDescription(String testDescription)
    {
        this.testDescription = testDescription;
    }

    public String getPtDateOfBirth()
    {
        return ptDateOfBirth;
    }

    public void setPtDateOfBirth(String ptDateOfBirth)
    {
        this.ptDateOfBirth = ptDateOfBirth;
    }

    public String getInsuredDateOfBirth()
    {
        return insuredDateOfBirth;
    }

    public void setInsuredDateOfBirth(String insuredDateOfBirth)
    {
        this.insuredDateOfBirth = insuredDateOfBirth;
    }

    public String getDateOfService()
    {
        return DateOfService;
    }

    public void setDateOfService(String dateOfService)
    {
        DateOfService = dateOfService;
    }
    public String getPtFirstName()
    {
        return ptFirstName;
    }

    public void setPtFirstName(String ptFirstName)
    {
        this.ptFirstName = ptFirstName;
    }

    public String getPtLastName()
    {
        return ptLastName;
    }

    public void setPtLastName(String ptLastName)
    {
        this.ptLastName = ptLastName;
    }

    public String getPtGender()
    {
        return ptGender;
    }

    public void setPtGender(String ptGender)
    {
        this.ptGender = ptGender;
    }

    public String getSubscriberId()
    {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId)
    {
        this.subscriberId = subscriberId;
    }

    public String getRelationship()
    {
        return relationship;
    }

    public void setRelationship(String relationship)
    {
        this.relationship = relationship;
    }

    public String getInsuredFirstName()
    {
        return insuredFirstName;
    }

    public void setInsuredFirstName(String insuredFirstName)
    {
        this.insuredFirstName = insuredFirstName;
    }

    public String getInsuredLastName()
    {
        return insuredLastName;
    }

    public void setInsuredLastName(String insuredLastName)
    {
        this.insuredLastName = insuredLastName;
    }

    public String getInsuredGender()
    {
        return insuredGender;
    }

    public void setInsuredGender(String insuredGender)
    {
        this.insuredGender = insuredGender;
    }

    public String getPayorId()
    {
        return payorId;
    }

    public void setPayorId(String payorId)
    {
        this.payorId = payorId;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getPayorName()
    {
        return payorName;
    }

    public void setPayorName(String payorName)
    {
        this.payorName = payorName;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setIsResp(Boolean isResp)
    {
        this.isResp = isResp;
    }

    public Boolean getIsResp()
    {
        return isResp;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }

    public void setAccnId(String accnId)
    {
        this.accnId = accnId;
    }

    public String getAccnId()
    {
        return accnId;
    }
}