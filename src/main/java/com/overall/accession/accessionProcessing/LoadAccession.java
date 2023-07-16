package com.overall.accession.accessionProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoadAccession
{
    private static String PLATFORM_FRAME_ID = "platformiframe";

    private RemoteWebDriver driver;
    protected Logger logger;

    public LoadAccession(RemoteWebDriver driver)
    {
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }

    /**
     * Returns the element for the Accession Id input field.
     * @return accession Id input field element
     */
    public WebElement accnIdInput()
    {
        return driver.findElement(By.id("lookupAccnId"));
    }

    public By getIsRequiredIndicatorLocator()
    {
        return By.xpath("//label[@for='lookupAccnId' and @class='isRequiredIndicator']");
    }

	//Accn Search Button
	public WebElement accnSearchBtn() throws Exception {
		return driver.findElement(By.xpath("/html/body/section/div/div/div[1]/section/div/div[2]/div/a/span"));
	}	

	public By platformFrameLocator()
    {
        return By.id(PLATFORM_FRAME_ID);
    }

    /**
     * Returns the element for the Accession Search button.
     * @return accession search button element
     */
    public WebElement getPlatformFrame()
    {
        return driver.findElement(platformFrameLocator());
    }

	public void clickAccnSearch() throws Exception{
		accnSearchBtn().click();
		logger.info("Clicked Accession Search button.");
	}	
	
    /**
     * Enters the given accession Id into the Accession Id input field,then tabs out.
     * @param accnId the accession Id to enter
     * @throws InterruptedException if a thread interruption occurs
     */
    public void setAccnId(String accnId) throws InterruptedException
    {
        accnIdInput().sendKeys(accnId);
        accnIdInput().sendKeys(Keys.TAB);
		Thread.sleep(7000);
    }

    /**
     * Enters the given accession Id into the Accession Id input field,then tabs out.
     * @param accnId the accession Id to enter
     * @param wait the explicit wait
     * @throws InterruptedException if a thread interruption occurs
     */
    public void setAccnId(String accnId, WebDriverWait wait)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(getIsRequiredIndicatorLocator()));
        wait.until(ExpectedConditions.elementToBeClickable(accnIdInput()));
        accnIdInput().clear();
        accnIdInput().sendKeys(accnId);
        accnIdInput().sendKeys(Keys.TAB);
    }
}
