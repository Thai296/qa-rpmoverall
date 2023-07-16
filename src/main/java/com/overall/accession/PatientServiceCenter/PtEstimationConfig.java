package com.overall.accession.PatientServiceCenter;

import com.xifin.qa.page.BasePage;
import domain.accession.PatientServiceCenter.PtEstimationDomain;
import domain.accession.PatientServiceCenter.patientestimation.InsuranceInformation;
import domain.accession.PatientServiceCenter.patientestimation.OrderInformation;
import domain.accession.PatientServiceCenter.patientestimation.PatientInformation;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class PtEstimationConfig extends BasePage
{
    private static final String RELATIONSHIP_SELF = "self";

    public PtEstimationConfig(RemoteWebDriver driver, WebDriverWait wait, String methodName)
    {
        super(driver, wait, methodName);
    }

    private By getDetailTableProcCodeLocator()
    {
        return By.xpath("(//td[@aria-describedby='tbl_procCodeInfo_procCd'])[1]");
    }

    private By getDetailTableUnitsLocator()
    {
        return By.cssSelector("td[aria-describedby='tbl_procCodeInfo_units']");
    }
    private By getDetailBillPriceLocator()
    {
        return By.cssSelector("td[aria-describedby='tbl_procCodeInfo_bilPrc']");
    }

    /*** Patient Estimation Load page***/
    public WebElement patientEstimationPageTitle () {
        return driver.findElement(By.xpath("//*[@id='pscPatientEstimationForm']/div[1]/div/div/div[2]/span"));
    }
    public WebElement accnInput()
    {
        return driver.findElement(By.id("accnId"));
    }
    public WebElement estimationIdInput()
    {
        return driver.findElement(By.id("lookupEstimationId"));
    }

    public WebElement estimationId()
    {
        return driver.findElement(By.id("estimationId"));
    }
    public WebElement createNewEstimation()
    {
        return driver.findElement(By.id("btnCreateNewEstimationContainer"));
    }
    public WebElement ptFirstNameInput()
    {
        return driver.findElement(By.id("patientFirstName"));
    }
    public WebElement ptLastNameInput()
    {
        return driver.findElement(By.id("patientLastName"));
    }
    public WebElement ptDateOfBirthInput()
    {
        return driver.findElement(By.id("patientDob"));
    }
    public WebElement ptGenderDropDown()
    {
        return driver.findElement(By.id("patientGender"));
    }

    public String getSelectedPtGenderText()
    {
        return new Select(ptGenderDropDown()).getFirstSelectedOption().getText();
    }

    public String getSelectedPayorIdText()
    {
        return new Select(payorDropDown()).getFirstSelectedOption().getText();
    }

    public String getSelectedRelationshipText()
    {
        return new Select(relationshipDropDown()).getFirstSelectedOption().getText();
    }

    public String getSelectedInsuredGenderText()
    {
        return new Select(insuredGenderDropDown()).getFirstSelectedOption().getText();
    }


    public WebElement insuredFirstNameInput()
    {
        return driver.findElement(By.id("insuredFirstName"));
    }
    public WebElement insuredLastNameInput()
    {
        return driver.findElement(By.id("insuredLastName"));
    }
    public WebElement insuredDateOfBirthInput()
    {
        return driver.findElement(By.id("insuredDob"));
    }
    public WebElement insuredGenderDropDown()
    {
        return driver.findElement(By.id("insuredGender"));
    }
    public WebElement subscriberIdInput()
    {
        return driver.findElement(By.id("subscriberId"));
    }
    public WebElement relationshipDropDown()
    {
        return driver.findElement(By.id("insuredRelationshipId"));
    }
    public WebElement payorDropDown()
    {
        return driver.findElement(By.id("payorAbbrev"));
    }
    public WebElement acceptRespInput()
    {
        return driver.findElement(By.id("patientRespAccepted"));
    }
    public WebElement noteInput()
    {
        return driver.findElement(By.id("estimationNote"));
    }
    public WebElement orderIdInput()
    {
        return driver.findElement(By.id("orderId"));
    }
    public WebElement clientIdInput()
    {
        return driver.findElement(By.id("clientId"));
    }
    public WebElement dateOfServiceInput()
    {
        return driver.findElement(By.id("dateOfService"));
    }
    public WebElement addTestsButton()
    {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("tbl_addTests_iladd")));
    }
    public WebElement estimatePtResponsibilityButton()
    {
        return driver.findElement(By.id("estimatePtRespBtn"));
    }

    public WebElement resetButton()
    {
        return driver.findElement(By.id("Reset"));
    }
    public WebElement saveButton()
    {
        return driver.findElement(By.id("btnSave"));
    }
    public WebElement helpIconInHeaderSection()
    {
        return driver.findElement(By.xpath("//*[contains(@class,'helpIcon inlineIcon')]"));
    }
    public WebElement helpIconInAccnInfoSection()  {
        return driver.findElement(By.xpath("//*[@id=\"mainSections\"]/div/div[2]/div[1]/div/section/div/div[1]/a"));
    }
    public WebElement helpIconInPtInfoSection()  {
        return driver.findElement(By.xpath("//*[@id=\"mainSections\"]/div/div[2]/div[2]/div/section/div/div[1]/a"));
    }
    public WebElement helpIconInInsuredInfoSection() {
        return driver.findElement(By.xpath("//*[@id=\"mainSections\"]/div/div[2]/div[3]/div/section/div/div[1]/a"));
    }
    public WebElement helpIconInOrderInfoSection(){
        return driver.findElement(By.xpath("//*[@id=\"mainSections\"]/div/div[2]/div[4]/div/section/div/div[1]/a"));
    }
    public WebElement helpIconInPtResponsibilitySection() {
        return driver.findElement(By.xpath("//*[@id=\"mainSections\"]/div/div[2]/div[5]/div/section/div/div[1]/a"));
    }
    public WebElement titleTextInHelp() {
        return driver.findElement(By.xpath("/html/body/div[1]/h1"));
    }
    public WebElement helpIconInFooterSection()  {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
    }
    public WebElement testIdInput()  {
        return wait.until(ExpectedConditions.elementToBeClickable(By.name("testAbbrev")));
    }
    public By testIdLabelLocator(int row)
    {
        return By.xpath("(//td[@aria-describedby='tbl_addTests_testAbbrev'])["+row+"]");
    }

    public WebElement testDescriptionInput(int row)  {
        return driver.findElement(By.name("//*[@id='jqg" + row + "']/td[4]/div"));
    }
    public WebElement helpPageTableOfContentsLink()
    {
        return driver.findElement(By.linkText("Table of Contents"));
    }

    public WebElement ptRespExpandGridButton()
    {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='gview_tbl_procCodeInfo']//a[1]")));
        Actions builder = new Actions(driver).moveToElement(element);
        builder.build().perform();
        return element;
    }

    public WebElement ptEstimationDetailTable()
    {
        return driver.findElement(By.id("tbl_procCodeInfo"));
    }

    public WebElement getEstimatedPatientResponsibility() {
        return driver.findElement(By.id("estimatedPatientResp"));
    }

    public String getEstimatedPatientResponsibilityAmount() {
        return driver.findElement(By.id("estimatedPatientResp")).getAttribute("value");
    }

    public WebElement getProcCodeRow1Copay() {
        return driver.findElement(By.id("1_copay"));
    }

    public WebElement getEstimationId() {
        return driver.findElement(By.id("estimationSeqId"));
    }

    public WebElement getPayorUpdatedLabel()
    {
        return driver.findElement(By.xpath("//label[@class='dataDisplay']"));
    }

    public WebElement updatedDemoDataByField(String field)
    {
        return driver.findElement(By.xpath("//*[contains(text(),'"+ StringUtils.upperCase(field)+"')]"));
    }

    public WebElement ptEstimationDetailTableProcCd()
    {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(getDetailTableProcCodeLocator()));
    }

    public WebElement ptEstimationDetailTableUnits()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(getDetailTableUnitsLocator()));
        return driver.findElement(getDetailTableUnitsLocator());
    }
    public WebElement ptEstimationDetailTableProcCdBillPrice()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(getDetailBillPriceLocator()));
        return driver.findElement(getDetailBillPriceLocator());
    }
    public WebElement ptEstimationDetailTableExpPrice()
    {
        return driver.findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_expPrc']"));
    }
    public WebElement ptEstimationDetailTableMod1()
    {
        return driver.findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_mod1Id']"));
    }
    public WebElement ptEstimationDetailTableCopay()
    {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_copay']")));
    }
    public WebElement ptEstimationDetailTableCoinsurance()
    {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_coinsurance']")));
    }
    public WebElement ptEstimationDetailTableCoinsuranceRate()
    {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_coinsuranceRate']")));
    }
    public WebElement ptEstimationDetailTableDeductible()
    {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_deductible']")));
    }
    public WebElement ptEstimationDetailTableDeductibleRemaining()
    {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_deductibleRemaining']")));
    }
    public WebElement editProcCodesButton() {
        return driver.findElement(By.id("tbl_procCodeInfo_iledit"));
    }

    public WebElement ptEstimationDetailTableDeductibleAnnual()
    {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_deductibleAnnual']")));
    }

    public int getPatientResponsibilityDetailRowCount()
    {
        return ptEstimationDetailTable().findElements(By.tagName("tr")).size()-1;
    }

    public String getPatientResponsibilityDetailProcCd(int row)
    {
        return ptEstimationDetailTable().findElement(By.id(String.valueOf(row+1))).findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_procCd']")).getText();
    }

    public String getPatientResponsibilityDetailUnits(int row)
    {
        return ptEstimationDetailTable().findElement(By.id(String.valueOf(row+1))).findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_units']")).getText();
    }

    public String getPatientResponsibilityDetailBillPrice(int row)
    {
        return ptEstimationDetailTable().findElement(By.id(String.valueOf(row+1))).findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_bilPrc']")).getText();
    }

    public String getPatientResponsibilityDetailExpectPrice(int row)
    {
        return ptEstimationDetailTable().findElement(By.id(String.valueOf(row+1))).findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_expPrc']")).getText();
    }

    public String getPatientResponsibilityDetailCopayAmt(int row)
    {
        return ptEstimationDetailTable().findElement(By.id(String.valueOf(row+1))).findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_copay']")).getText();
    }

    public String getPatientResponsibilityDetailCoinsPct(int row)
    {
        return ptEstimationDetailTable().findElement(By.id(String.valueOf(row+1))).findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_coinsuranceRate']")).getText();
    }

    public String getPatientResponsibilityDetailCoinsAmt(int row)
    {
        return ptEstimationDetailTable().findElement(By.id(String.valueOf(row+1))).findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_coinsurance']")).getText();
    }

    public String getPatientResponsibilityDetailDeductible(int row)
    {
        return ptEstimationDetailTable().findElement(By.id(String.valueOf(row+1))).findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_deductible']")).getText();
    }

    public String getPatientResponsibilityDetailRemainingDeductible(int row)
    {
        return ptEstimationDetailTable().findElement(By.id(String.valueOf(row+1))).findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_deductibleRemaining']")).getText();
    }

    public String getPatientResponsibilityDetailAnnualDeductible(int row)
    {
        return ptEstimationDetailTable().findElement(By.id(String.valueOf(row+1))).findElement(By.cssSelector("td[aria-describedby='tbl_procCodeInfo_deductibleAnnual']")).getText();
    }

    public void setCreateNewInput()
    {
        WebElement createNewEstimation = createNewEstimation();
        createNewEstimation.click();
        logger.info("Clicked to create new estimation");
    }
    public void setEstimationIdInput(String estimationId)
    {
        WebElement estimationIdField = estimationIdInput();
        estimationIdField.clear();
        estimationIdField.sendKeys(estimationId);
        logger.info("Entered Estimation (or lookup) ID: " + estimationId);
        estimationIdField.sendKeys(Keys.TAB);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("xf_toaster_container")));
    }
    public void setAccnIdInput(String accnId)
    {
        WebElement testAccnId = accnInput();
        testAccnId.clear();
        testAccnId.sendKeys(accnId);
        logger.info("Entered Accn ID: " + accnId);
        testAccnId.sendKeys(Keys.TAB);
    }
    public void setPtFirstNameInput(String ptFirstName)
    {
        WebElement testFirstName = ptFirstNameInput();
        testFirstName.clear();
        testFirstName.sendKeys(ptFirstName);
        logger.info("Entered Pt First Name: " + ptFirstName);
    }
    public void setPtLastNameInput(String ptLastName)
    {
        WebElement testLastName = ptLastNameInput();
        testLastName.clear();
        testLastName.sendKeys(ptLastName);
        logger.info("Entered Pt Last Name: " + ptLastName);
    }
    public void setPtDateOfBirthInput(String ptDateOfBirthInput)
    {
        WebElement testPtDateOfBirth = ptDateOfBirthInput();
        testPtDateOfBirth.clear();
        testPtDateOfBirth.sendKeys(ptDateOfBirthInput);
        logger.info("Entered Pt DOB: " + ptDateOfBirthInput);
    }
    public void setInsuredFirstNameInput(String insuredFirstName)
    {
        WebElement testInsuredName = insuredFirstNameInput();
        testInsuredName.clear();
        testInsuredName.sendKeys(insuredFirstName);
        logger.info("Entered Insured First Name: " + insuredFirstName);
    }
    public void setInsuredLastNameInput(String insuredLastName)
    {
        WebElement testInsuredName = insuredLastNameInput();
        testInsuredName.clear();
        testInsuredName.sendKeys(insuredLastName);
        logger.info("Entered Insured Last Name: " + insuredLastName);
    }
    public void setInsuredDateOfBirthInput(String insuredDateOfBirthInput)
    {
        WebElement testInsuredDateOfBirth = insuredDateOfBirthInput();
        testInsuredDateOfBirth.clear();
        testInsuredDateOfBirth.sendKeys(insuredDateOfBirthInput);
        logger.info("Entered Insured DOB: " + insuredDateOfBirthInput);
    }
    public void setSubscriberIdInput(String subscriberIdInput)
    {
        WebElement testSubscriberId = subscriberIdInput();
        testSubscriberId.clear();
        testSubscriberId.sendKeys(subscriberIdInput);
        logger.info("Entered Subscriber ID: " + subscriberIdInput);
    }
    public void setOrderIdInput(String orderIdInput)
    {
        WebElement testOrderIdInput = orderIdInput();
        testOrderIdInput.clear();
        testOrderIdInput.sendKeys(orderIdInput);
        logger.info("Entered Order ID: " + orderIdInput);
    }
    public void setClientIdInput(String clientIdInput)
    {
        WebElement testClientIdInput = clientIdInput();
        testClientIdInput.clear();
        testClientIdInput.sendKeys(clientIdInput);
        logger.info("Entered Client ID: " + clientIdInput);
    }
    public void setDateOfServiceInput(String dateOfServiceInput)
    {
        WebElement testDateOfService = dateOfServiceInput();
        testDateOfService.clear();
        testDateOfService.sendKeys(dateOfServiceInput);
        logger.info("Entered Date of Service: " + dateOfServiceInput);
    }
    public void setTestIdInput(String testId, int row)
    {
        WebElement testTestId = testIdInput();
        logger.info("Entering Test Id: " + testId);
        testTestId.click();
        testTestId.clear();
        testTestId.sendKeys(testId+Keys.TAB);
        logger.info("Rechecking Test Id after tabbing out...");
        wait.until(ExpectedConditions.textToBe(testIdLabelLocator(row), testId));
        logger.info("Confirmed Entered Test Id successfully: " + testId);
    }
    public void setPtGenderDropDown(String gender)
    {
        WebElement ptGender = ptGenderDropDown();
        Select select = new Select(ptGender);
        select.selectByVisibleText(gender);
        logger.info("Selected Patient Gender: " + gender + " from Dropdown List.");
        ptGender.sendKeys(Keys.TAB);
    }
    public void setInsuredGenderDropDown(String gender)
    {
        WebElement insuredGender = insuredGenderDropDown();
        Select select = new Select(insuredGender);
        select.selectByVisibleText(gender);
        logger.info("Selected Insured Gender: " + gender + " from Dropdown List.");
        insuredGender.sendKeys(Keys.TAB);
    }

    public void setRelationshipDropDown(String relationship)
    {
        WebElement insuredRelationship = relationshipDropDown();
        Select select = new Select(insuredRelationship);
        select.selectByVisibleText(relationship);
        logger.info("Selected Insured Relationship: " + relationship + " from Dropdown List.");
        insuredRelationship.sendKeys(Keys.TAB);
    }
    public void setPayorDropDown(String pyrAbbrev, String pyrName)
    {
        WebElement payorId = payorDropDown();
        Select select = new Select(payorId);
        select.selectByVisibleText(pyrAbbrev+" - "+pyrName);
        logger.info("Selected Payor Id: " + pyrAbbrev+" - "+pyrName + " from Dropdown List.");
        payorId.sendKeys(Keys.TAB);
    }
    public void setAcceptRespInput(Boolean acceptRespInput)
    {
        WebElement testAcceptResp = acceptRespInput();
        if (acceptRespInput)
        {
            testAcceptResp.click();
        }
        logger.info("Entered Accept Responsibility: " + acceptRespInput);
    }
    public void setNoteInput(String noteInput)
    {
        WebElement testNote = noteInput();
        testNote.clear();
        testNote.sendKeys(noteInput);
        logger.info("Entered Note: " + noteInput);
    }
    public void enterAccnInfoDataForPatientEstimation(PtEstimationDomain ptEstimationDomain)
    {
        setAccnIdInput(ptEstimationDomain.getAccnId());
    }

    public void enterAccessionId(String accnId)
    {
        setAccnIdInput(accnId);
    }

    public void enterCreateNewEstimation()
    {
        setCreateNewInput();
    }
    public void enterEstimationId(String estimationId)
    {
        setEstimationIdInput(estimationId);
    }
    public void enterPatientInfoDataForPatientEstimation(PtEstimationDomain ptEstimationDomain, String relationshipTyp)
    {
        setPtFirstNameInput(ptEstimationDomain.getPtFirstName());
        setPtLastNameInput(ptEstimationDomain.getPtLastName());
        setPtDateOfBirthInput(String.valueOf(ptEstimationDomain.getPtDateOfBirth()));
        setPtGenderDropDown(ptEstimationDomain.getPtGender());

        logger.info("Entered Patient Information");
        logger.info("Patient Information Details (" + ptEstimationDomain.getPtFirstName() + ", " + ptEstimationDomain.getPtLastName() + ", " +
                ptEstimationDomain.getPtDateOfBirth() + ", " + ptEstimationDomain.getPtGender());
        if (!relationshipTyp.equals("self"))
        {
            setInsuredFirstNameInput(ptEstimationDomain.getInsuredFirstName());
            setInsuredLastNameInput(ptEstimationDomain.getInsuredLastName());
            setInsuredDateOfBirthInput(String.valueOf(ptEstimationDomain.getInsuredDateOfBirth()));
            setInsuredGenderDropDown(ptEstimationDomain.getInsuredGender());
            logger.info("Patient Insured Details (" + ptEstimationDomain.getInsuredFirstName()
                    + ", " + ptEstimationDomain.getInsuredLastName() + ", " + ptEstimationDomain.getInsuredDateOfBirth()
                    + ", " + ptEstimationDomain.getInsuredGender());
        }
    }

    public void enterPatientInformation(PatientInformation patientInformation)
    {
        if (patientInformation == null)
        {
            return;
        }
        setPtFirstNameInput(patientInformation.getFirstName());
        setPtLastNameInput(patientInformation.getLastName());
        setPtDateOfBirthInput(patientInformation.getDob());
        setPtGenderDropDown(patientInformation.getGender());
    }

    public void enterInsuranceInformation(InsuranceInformation insuranceInformation)
    {
        setPayorDropDown(insuranceInformation.getPayorId(), insuranceInformation.getPayorName());
        setSubscriberIdInput(insuranceInformation.getSubscriberId());
        setRelationshipDropDown(insuranceInformation.getRelationship());
        if (!StringUtils.equalsIgnoreCase(insuranceInformation.getRelationship(), RELATIONSHIP_SELF))
        {
            setInsuredFirstNameInput(insuranceInformation.getFirstName());
            setInsuredLastNameInput(insuranceInformation.getLastName());
            setInsuredDateOfBirthInput(insuranceInformation.getDob());
            setInsuredGenderDropDown(insuranceInformation.getGender());
        }
    }

    public void enterOrderInformation(OrderInformation orderInformation)
    {
        setOrderIdInput(orderInformation.getOrderId());
        setClientIdInput(orderInformation.getClientId());
        setDateOfServiceInput(orderInformation.getDos());
        int testCnt = 1;
        for (String testId : orderInformation.getTestIds())
        {
            addTestsButton().click();
            setTestIdInput(testId, testCnt++);
        }
    }

    public void clickEstimatePtResponsibilityButton()
    {
        scrollIntoView(estimatePtResponsibilityButton());
        wait.until(ExpectedConditions.elementToBeClickable(estimatePtResponsibilityButton()));
        estimatePtResponsibilityButton().click();
        ptRespExpandGridButton().click();
        wait.until(ExpectedConditions.visibilityOf(ptEstimationDetailTable()));
    }

    public void enterInsuredInfoDataForPatientEstimation(PtEstimationDomain ptEstimationDomain)
    {
        if (ptEstimationDomain.getRelationship().equals("self"))
        {
            setSubscriberIdInput(ptEstimationDomain.getSubscriberId());
            setRelationshipDropDown(ptEstimationDomain.getRelationship());
            logger.info("Patient Information Details (" + ptEstimationDomain.getSubscriberId() + ", " + ptEstimationDomain.getRelationship());
        }
        else
        {
            setSubscriberIdInput(ptEstimationDomain.getSubscriberId());
            setRelationshipDropDown(ptEstimationDomain.getRelationship());
            setInsuredFirstNameInput(ptEstimationDomain.getInsuredFirstName());
            setInsuredLastNameInput(ptEstimationDomain.getInsuredLastName());
            setInsuredDateOfBirthInput(String.valueOf(ptEstimationDomain.getInsuredDateOfBirth()));
            setInsuredGenderDropDown(ptEstimationDomain.getInsuredGender());
            logger.info("Patient Information Details (" + ptEstimationDomain.getSubscriberId() + ", " + ptEstimationDomain.getRelationship() + ", " +
                    ptEstimationDomain.getInsuredFirstName() + ", " + ptEstimationDomain.getInsuredLastName() + ", " + ptEstimationDomain.getInsuredDateOfBirth() + ", " + ptEstimationDomain.getInsuredGender());
        }
    }
    public void enterOrderInfoDataForPatientEstimation(PtEstimationDomain ptEstimationDomain)
    {
        setPayorDropDown(ptEstimationDomain.getPayorId(), ptEstimationDomain.getPayorName());
        setOrderIdInput(ptEstimationDomain.getOrderId());
        setClientIdInput(ptEstimationDomain.getClientId());
        setDateOfServiceInput(String.valueOf(ptEstimationDomain.getDateOfService()));
        addTestsButton().click();
        setTestIdInput(ptEstimationDomain.getTestId(), 1);
        logger.info("Order Information Details =" + ptEstimationDomain.getPayorId() + ", " + ptEstimationDomain.getClientId() + ", " +
                ptEstimationDomain.getDateOfService() + ", " +ptEstimationDomain.getTestId());
    }
    public void enterRespInfoDataForPatientEstimation(PtEstimationDomain ptEstimationDomain)
    {
        setAcceptRespInput(ptEstimationDomain.getIsResp());
        setNoteInput(ptEstimationDomain.getNote());
        logger.info("Resp Information Details =" + ptEstimationDomain.getIsResp() + ", " + ptEstimationDomain.getNote());
    }

    public void enterPtRespAmtAndProcDataForPatientEstimation(List<String> inputValues) {
        setPtRespAmtInput(inputValues.get(9));
        setProcCodeRow1Copay(inputValues.get(3));
        logger.info("Patient Resp Amt = " + inputValues.get(9) + ", Copay = " + inputValues.get(3));
    }

    public void setPtRespAmtInput(String amount) {
        WebElement ptRespAmtId = getEstimatedPatientResponsibility();
        ptRespAmtId.clear();
        ptRespAmtId.sendKeys(amount);
        logger.info("Entered Pt Resp Amt = " + amount);
    }

    public void setProcCodeRow1Copay(String amount) {
        editProcCodesButton().click();
        WebElement row1Copay = getProcCodeRow1Copay();
        row1Copay.sendKeys(amount);
        row1Copay.sendKeys(Keys.ENTER);
        logger.info("Entered Proc Code Row 1 Copay = " + amount);
    }
}
