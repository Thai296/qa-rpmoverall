package com.newXp.tests;

import com.overall.accession.PatientServiceCenter.PSCPrepayConfig;
import com.overall.menu.MenuNavigation;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class PSCPrepayTest extends SeleniumBaseTest
{
    private MenuNavigation navigation;
    private PSCPrepayConfig pscPrepay;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"ssoUsername", "ssoPassword"})
    public void beforeMethod(String ssoUsername, String ssoPassword, Method method)
    {
        try
        {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            logIntoSso(ssoUsername, ssoPassword);
            navigation = new MenuNavigation(driver,config);
            navigation.navigateToPSCPrepayPage();
        }
        catch (Exception e)
        {
            logger.error("Error running BeforeMethod", e);
        }
    }

    @Test(priority = 1, description = "Verify Help Links")
    @Parameters({})
    public void testXPR_1934() throws Exception {
        logger.info("===== Testing - testXPR_1934 =====");
        pscPrepay = new PSCPrepayConfig(driver, new WebDriverWait(driver, 10));

        logger.info("Verify that the PSC Prepay page is displayed with correct page title");
        Assert.assertTrue(pscPrepay.pscPrepayPageTitle().isDisplayed());
        Assert.assertEquals(pscPrepay.pscPrepayPageTitle().getText(),"Pre-Payment","PSC Prepay screen should be displayed.");
        String parent = driver.getWindowHandle();

        logger.info("Click Help icon button in the Header");
        wait.until(ExpectedConditions.elementToBeClickable(pscPrepay.helpIconInHeaderSection()));
        pscPrepay.helpIconInHeaderSection().click();

        logger.info("Verify that Help file in the Header can be opened properly");
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_service_center_header.htm"), "Help file in Header is not opened.");
        Assert.assertTrue(pscPrepay.titleTextInHelp().getText().contains("PSC Pre-Payment Header"), "Help file in Patient Service Center Header should be opened.");
        Assert.assertTrue(pscPrepay.helpPageTableOfContentsLink().isDisplayed());
        driver.close();
        switchToParentWindow(parent);

        logger.info("Click Help icon button in the Order Information Section");
        wait.until(ExpectedConditions.elementToBeClickable(pscPrepay.helpIconInOrderInformationSection()));
        pscPrepay.helpIconInOrderInformationSection().click();

        logger.info("Verify that Help file in the Order Information Section can be opened properly");
        switchToPopupWin();
        logger.info("Verify url");
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_service_center_order_info.htm"),"Help file in Order Information Section not opened.");
        logger.info("Verify title");
        Assert.assertTrue(pscPrepay.titleTextInHelp().getText().contains("PSC Pre-Payment Order Information"), "Help file in Patient Service Center Order Information Section should be opened.");
        logger.info("Verify table of contents");
        Assert.assertTrue(pscPrepay.helpPageTableOfContentsLink().isDisplayed());
        driver.close();
        switchToParentWindow(parent);

        logger.info("Click Help icon button in the Patient Information Section");
        wait.until(ExpectedConditions.elementToBeClickable(pscPrepay.helpIconInPatientInformationSection()));
        pscPrepay.helpIconInPatientInformationSection().click();

        logger.info("Verify that Help page in Patient Information section is opened properly");
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_service_center_patient_info.htm"),"Help file in Patient Information Section not opened.");
        Assert.assertTrue(pscPrepay.titleTextInHelp().getText().contains("PSC Pre-Payment Patient Information"), "Help file in Patient Service Center Patient Information Section should be opened.");
        Assert.assertTrue(pscPrepay.helpPageTableOfContentsLink().isDisplayed());
        driver.close();
        switchToParentWindow(parent);

        logger.info("Click Help icon button in the Amount Section");
        wait.until(ExpectedConditions.elementToBeClickable(pscPrepay.helpIconInAmountSection()));
        pscPrepay.helpIconInAmountSection().click();

        logger.info("Verify that Help file in the Amount Section can be opened properly");
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_service_center_amount.htm"),"Help file in Amount Section not opened.");
        Assert.assertTrue(pscPrepay.titleTextInHelp().getText().contains("PSC Pre-Payment Amount"), "Help file in Patient Service Center Amount Section should be opened.");
        Assert.assertTrue(pscPrepay.helpPageTableOfContentsLink().isDisplayed());
        driver.close();
        switchToParentWindow(parent);

        logger.info("Click on Reset button");
        Assert.assertTrue(pscPrepay.resetButton().isDisplayed());
        pscPrepay.resetButton().click();
    }

    public void switchToParentWindow(String currentWindow) {
        driver.switchTo().window(currentWindow);
    }

}
