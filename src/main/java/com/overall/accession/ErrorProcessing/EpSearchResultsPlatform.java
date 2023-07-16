package com.overall.accession.ErrorProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

public class EpSearchResultsPlatform
{
    private RemoteWebDriver driver;
    protected Logger logger;

    public EpSearchResultsPlatform(RemoteWebDriver driver){
    this.driver = driver;
    logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
}

    public WebElement submitBtn() {
        return driver.findElement(By.id("btnSubmitAction"));
    }
    public WebElement actionDropdownPF() {
        return driver.findElement(By.id("actions"));
    }
    public WebElement selectCheckBoxPF(int rowNum) {
        return driver.findElement(By.xpath(".//*[@id='"+rowNum+"']/td[20]/input"));
    }
    public void setActionDropDown(String action)
    {
        Select select = new Select(actionDropdownPF());
        select.selectByVisibleText(action);
        logger.info("Entered Input Into Action DropDown: " + action);
    }
}
