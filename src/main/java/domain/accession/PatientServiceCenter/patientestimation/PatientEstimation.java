package domain.accession.PatientServiceCenter.patientestimation;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PatientEstimation
{
    private String estimationId;
    private String accnId;
    private PatientInformation patientInformation;
    private InsuranceInformation insuranceInformation;
    private OrderInformation orderInformation;
    private PatientResponsibility patientResponsibility;

    private final RemoteWebDriver driver;
    private WebDriverWait wait;

    public PatientEstimation(RemoteWebDriver driver)
    {
        this.wait = new WebDriverWait(driver, 10);
        this.driver = driver;
    }

    public WebElement patientEstimationPageTitle() {
        return driver.findElementByClassName("platormPageTitle");
    }

    public WebElement estimationIdInput() {
        return driver.findElementById("lookupEstimationId");
    }

    public WebElement AccessionInformationTblTitle() {
        return driver.findElementByXPath("//*[@id='mainSections']/div/div[2]/div[1]/div/section/header/div[1]/span/span");
    }

    public WebElement PatientInformationTblTitle() {
        return driver.findElementByXPath("//*[@id='mainSections']/div/div[2]/div[2]/div/section/header/div[1]/span/span");
    }

    public WebElement InsuranceInformationTblTitle() {
        return driver.findElementByXPath("//*[@id='mainSections']/div/div[2]/div[3]/div/section/header/div[1]/span/span");
    }

    public WebElement resetBtn() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("Reset")));
    }

    public WebElement loadPatientEstimationTblTitle() {
        return driver.findElementByXPath("//*[@id='pscPatientEstimationForm']/div[2]/section/header/div/span/span");
    }

    public void clickResetBtn() {
        resetBtn().click();
    }

    public String getEstimationId()
    {
        return estimationId;
    }

    public void setEstimationId(String estimationId)
    {
        this.estimationId = estimationId;
    }

    public String getAccnId()
    {
        return accnId;
    }

    public void setAccnId(String accnId)
    {
        this.accnId = accnId;
    }

    public PatientInformation getPatientInformation()
    {
        return patientInformation;
    }

    public void setPatientInformation(PatientInformation patientInformation)
    {
        this.patientInformation = patientInformation;
    }

    public InsuranceInformation getInsuranceInformation()
    {
        return insuranceInformation;
    }

    public void setInsuranceInformation(InsuranceInformation insuranceInformation)
    {
        this.insuranceInformation = insuranceInformation;
    }

    public OrderInformation getOrderInformation()
    {
        return orderInformation;
    }

    public void setOrderInformation(OrderInformation orderInformation)
    {
        this.orderInformation = orderInformation;
    }

    public PatientResponsibility getPatientResponsibility()
    {
        return patientResponsibility;
    }

    public void setPatientResponsibility(PatientResponsibility patientResponsibility)
    {
        this.patientResponsibility = patientResponsibility;
    }
}
