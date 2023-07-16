package com.overall.accession.ErrorProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

public class EpSearchPlatform
{
    private RemoteWebDriver driver;
    protected Logger logger;

    public EpSearchPlatform(RemoteWebDriver driver){
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }
    public WebElement searchIdInput()  {
        return driver.findElement(By.id("lookupSearchId"));
    }
    public WebElement filter1Dropdown()  {
        return driver.findElement(By.id("filter1"));
    }

    public WebElement filter1Input()  {
        return driver.findElement(By.id("val1"));
    }
    public WebElement filterInput(int i)  {
        return driver.findElement(By.id("s2id_filter" + i));
    }
    public WebElement saveBtn() {
        return driver.findElement(By.id("btnSave"));
    }
    public WebElement resetBtn() {
        return driver.findElement(By.id("Reset"));
    }
    public void setFilterAndValueIntoSearchPF(String text, String value){
        Select selection = new Select(filter1Dropdown());
        selection.selectByVisibleText(text);
        filter1Input().sendKeys(value);
        logger.info("Entered value in Filter1 : " +text +", value="+ value);
    }
}
