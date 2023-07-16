package com.overall.fileMaintenance.diagnosisCode;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DiagnosisCode {
    private WebDriverWait wait;
    protected Logger logger;

    public DiagnosisCode(WebDriverWait wait){
        this.wait = wait;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + wait);
    }

    /* Begin header section */
    public WebElement diagnosisCodeTitle(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
    }
    
    /* END OF header section */

    /* BEGIN Diagnosis Code Information section */
    public WebElement loadDiagCodeTblTitle() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='mainSections']/div[2]/div[1]/section/header/div/span/span")));
    }

    public WebElement diagCodeInfoTblTitle() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='anchor']/div/span/span")));
    }

    public WebElement helpInDiagCodeIcon(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_diagnosis_code_header']")));
    }

    public WebElement diagCodeTypDdl(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_diagCodeType")));
    }

    public WebElement searchIcon(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='diagSearchButton']/span")));
    }

    public WebElement diagCodeInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("diagnosisCode")));
    }

    public WebElement diagNameInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("diagCodeName")));
    }

    public WebElement diagDescriptionInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("diagCodeDescr")));
    }

    public WebElement specifilityRequiredCkb(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("specificityRequired")));
    }

    public WebElement deleteDiagCodeCkb(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deleteDiagCode")));
    }

    public WebElement resetBtn(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
    }

    public void clickResetBtn() {
        resetBtn().click();
    }

    /* END OF Diagnosis Code Information section */
    
    /* BEGIN OF Search Page section */
    public WebElement diagnosisCodeSearchTitle() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='diagnosisSearch']/header/span")));
    }

    public WebElement typeInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("diagnosisCodeType")));
    }

    public WebElement diagnosisCodeInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("diagnosisCode")));
    }

    public WebElement desciptionInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));
    }

    public WebElement inputCheck() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deleted")));
    }

    public WebElement closeBtnInDiagCodeSearch() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='diagnosisCodeSearch']/div[2]/button[3]")));
    }

    public WebElement resetBtnInDiagCodeSearch() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='diagnosisCodeSearch']/div[2]/button[2]")));
    }

    public WebElement searchBtnInDiagCodeSearch() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='diagnosisCodeSearch']/div[2]/button[1]")));
    }

    public void enterDiagnosisCodeInput(String value) {
        diagnosisCodeInput().clear();
        diagnosisCodeInput().sendKeys(value);
        diagnosisCodeInput().sendKeys(Keys.TAB);
        logger.info("        Enter Diagnosis Code Input.");
    }

    public void enterDescriptionInput(String value) {
        desciptionInput().clear();
        desciptionInput().sendKeys(value);
        desciptionInput().sendKeys(Keys.TAB);
        logger.info("        Enter Description Input.");
    }

    public void enterDiagCodeInput(String value){
        diagCodeInput().clear();
        diagCodeInput().sendKeys(value);
        diagCodeInput().sendKeys(Keys.TAB);
        logger.info("        Enter Diagnosis Code Input.");
    }

    /* BEGIN OF Search Result Page section */
    public WebElement diagCdSearchResultTitle() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_diagnosissearchTable']/div[1]/span")));
    }

    public WebElement getTextOfColDiagCdInSearchResultTblCell(int row, int col){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='diagnosissearchTable']/tbody/tr[" + row + "]/td[" + col + "]/a")));
    }

    public void pressAltR(){
        diagnosisCodeTitle().click();
        String selectAll = Keys.chord(Keys.ALT, "R");
        WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        body.sendKeys(selectAll);
        logger.info("        press Ctr + R");
    }
    
    /* END OF Search Result Page section */
}
