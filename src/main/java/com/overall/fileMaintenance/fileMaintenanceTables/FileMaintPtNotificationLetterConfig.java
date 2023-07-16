package com.overall.fileMaintenance.fileMaintenanceTables;

        import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
        import domain.fileMaintenance.PtNotificationConfig;
        import org.apache.log4j.Logger;
        import org.openqa.selenium.By;
        import org.openqa.selenium.Keys;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.remote.RemoteWebDriver;
        import org.openqa.selenium.support.ui.Select;

/**
 * File Maintenance Patient Notification Letter Config
 */
public class FileMaintPtNotificationLetterConfig extends SeleniumBaseTest
{
    private RemoteWebDriver driver;
    private Logger logger;

    int rowNumInTable;
    int columnCount;

    private static String ID_SAVE_AND_CLEAR_BUTTON = "btnSaveAndClear";

    private static String ID_DELETE_BUTTON = "deleted";

    public FileMaintPtNotificationLetterConfig(RemoteWebDriver driver)
    {
        this.driver = driver;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }
    public WebElement helpIconInHeaderSection() throws Exception
    {
        return driver.findElement(By.cssSelector("a[data-help-id='p_patient_notification_letter_configuration_header']"));
    }

    public WebElement helpIconInPtNotificationLetterConfig() throws Exception
    {
        return driver.findElement(By.cssSelector("a[data-help-id='p_patient_notification_letter_configuration_main_grid']"));
    }

    public WebElement contentFrame()
    {
        return driver.findElement(By.id("content"));
    }

    public WebElement loginIDInput()
    {
        return driver.findElement(By.id("loginId"));
    }

    public WebElement resetBtn()
    {
        return driver.findElement(By.id("Reset"));
    }

    public WebElement saveAndClearBtn()
    {
        return driver.findElement(By.id(ID_SAVE_AND_CLEAR_BUTTON));
    }

    public WebElement ptNotifTblCelData(int row, int col)
    {
        return driver.findElement(By.xpath(".//*[@id='tbl_ptNotificationLetterConfig']/tbody/tr[" + row + "]/td[" + col + "]"));
    }

    public WebElement payorIdInputInPopup()
    {
        return driver.findElement(By.id("pyrAbbrv"));

    }
    public WebElement deleteButton()
    {
        return driver.findElement(By.id(ID_DELETE_BUTTON));

    }

    public WebElement oKBtn()
    {
        return driver.findElement(By.id("sData"));
    }

    public WebElement cancelBtn() throws Exception
    {
        return driver.findElement(By.id("cData"));
    }

    public WebElement select2DropInput()
    {
        return driver.findElement(By.xpath(".//*[@id='select2-drop']/div/input"));
    }

    public int getRowNumInTable()
    {
        return rowNumInTable = driver.findElements(By.xpath("//table[@id='tbl_ptNotificationLetterConfig']/tbody/tr")).size();
    }


    public int getColumnCount()
    {
        return columnCount = driver.findElements(By.xpath("//table[@id='tbl_ptNotificationLetterConfig']/tbody/tr/td")).size();
    }

    public void enterCriteriaForInclusion(PtNotificationConfig config)
    {
        setPyrGrpInclDropDown(config.getPyrGrp().getGrpNm());
        setPyrInclInput(config.getPyr().getPyrAbbrv());
        setClnAcctTypInclDropDown(config.getClnAccntTyp().getDescr());
        setClnInclInput(config.getCln().getClnAbbrev());
        setTaxonomyCdInclInput(config.getTaxonomy().getTaxonomyCd());
        setTestTypInclDropDown(config.getTestTyp().getAbbrev());
        setTestInclInput(config.getTest().getTestAbbrev());
        setClnFacInclDropDown(config.getClnFac().getAbbrv() + " - " + config.getClnFac().getName());
        setLetterIdInput(config.getLetterId());

        logger.info("Entered Patient Notification Letter Configuration with: Letter ID = " + config.getLetterId());
        logger.info("Letter Config Details (" + config.getPyrGrp().getGrpNm() + ", " + config.getPyr().getPyrAbbrv() + ", " +
                config.getClnAccntTyp().getDescr() + ", " + config.getCln().getClnAbbrev() + ", " + config.getTaxonomy().getTaxonomyCd() + ", " + config.getTestTyp().getAbbrev() + ", " +
                config.getTest().getTestAbbrev() + ", " + config.getClnFac().getAbbrv() + ", " +
                config.getLetterId() + ")");
    }
    public void enterCriteriaForExclusion(PtNotificationConfig config)
    {
        setPyrGrpExclDropDown(config.getPyrGrp().getGrpNm());
        setPyrExclInput(config.getPyr().getPyrAbbrv());
        setClnAcctTypExclDropDown(config.getClnAccntTyp().getDescr());
        setClnExclInput(config.getCln().getClnAbbrev());
        setTaxonomyCdExclInput(config.getTaxonomy().getTaxonomyCd());
        setTestTypExclDropDown(config.getTestTyp().getAbbrev());
        setTestExclInput(config.getTest().getTestAbbrev());
        setClnFacExclDropDown(config.getClnFac().getAbbrv() + " - " + config.getClnFac().getName());
        setLetterIdInput(config.getLetterId());

        logger.info("Entered Patient Notification Letter Configuration with: Letter ID = " + config.getLetterId());
        logger.info("Letter Config Details (" + config.getPyrGrp().getGrpNm() + ", " + config.getPyr().getPyrAbbrv() + ", " +
                config.getClnAccntTyp().getDescr() + ", " + config.getCln().getClnAbbrev() + ", " + config.getTaxonomy().getTaxonomyCd() + ", " + config.getTestTyp().getAbbrev() + ", " +
                config.getTest().getTestAbbrev() + ", " + config.getClnFac().getAbbrv() + ", " +
                config.getLetterId() + ")");
    }

    private void setLetterIdInput(String letterId)
    {
        if (!letterId.equals(""))
        {
            WebElement letterIdIn = letterIdInput();
            letterIdIn.clear();
            letterIdIn.sendKeys(letterId);
            logger.info("Entered Letter Id: " + letterId);
        }
    }

    private void setClnFacExclDropDown(String clnFacExcl)
    {
        WebElement clnFacExclIn = clnFacExclDropDown();
        if (!clnFacExcl.equals(""))
        {
            Select select = new Select(clnFacExclIn);
            select.selectByVisibleText(clnFacExcl);
            logger.info("Selected Client Fac Exclusions: " + clnFacExcl + " from Dropdown List.");
        } else
        {
            clnFacExclIn.sendKeys(Keys.TAB);
        }
    }

    private void setClnFacInclDropDown(String clnFacIncl)
    {
        WebElement clnFacInclIn = clnFacInclDropDown();
        if (!clnFacIncl.equals(""))
        {

            Select select = new Select(clnFacInclIn);
            select.selectByVisibleText(clnFacIncl);
            logger.info("Selected Client Fac Inclusions: " + clnFacIncl + " from Dropdown List.");
        } else
        {
            clnFacInclInput().sendKeys(Keys.TAB);
        }
    }

    private void setTestExclInput(String testExcl)
    {
        if (!testExcl.equals(""))
        {
            WebElement testExclIn = testExclInput();
            testExclIn.clear();
            testExclIn.sendKeys(testExcl);
            //  testExclIn.sendKeys(Keys.TAB);
            logger.info("Entered Test Exclusions: " + testExcl);
        }
    }

    private void setTestInclInput(String testIncl)
    {
        if (!testIncl.equals(""))
        {
            WebElement testInclIn = testInclInput();
            testInclIn.clear();
            testInclIn.sendKeys(testIncl);
            // testInclIn.sendKeys(Keys.TAB);
            logger.info("Entered Test Inclusions: " + testIncl);
        }
    }

    private void setTestTypExclDropDown(String testTypExcl)
    {
        WebElement testTypExclInputDropDown = testTypExclDropDown();
        if (!testTypExcl.equals(""))
        {
            Select select = new Select(testTypExclInputDropDown);
            select.selectByVisibleText(testTypExcl);
            logger.info("Selected Test Type Exclusions: " + testTypExcl + " from Dropdown List.");
        } else
        {
            testTypExclInputDropDown.sendKeys(Keys.TAB);
        }
    }

    private void setTestTypInclDropDown(String testTypIncl)
    {
        WebElement testTypInclInputDropDown = testTypInclDropDown();
        if (!testTypIncl.equals(""))
        {
            Select select = new Select(testTypInclInputDropDown);
            select.selectByVisibleText(testTypIncl);
            logger.info("Selected Test Type Inclusions: " + testTypIncl + " from Dropdown List.");
        } else
        {
            testTypInclInputDropDown.sendKeys(Keys.TAB);
        }
    }

    private void setTaxonomyCdExclInput(String taxonomyCdExcl)
    {
        if (!taxonomyCdExcl.equals(""))
        {
            WebElement taxomonyExclIn = taxonomyCdExclInput();
            taxomonyExclIn.clear();
            taxomonyExclIn.sendKeys(taxonomyCdExcl);
            //taxomonyExclIn.sendKeys(Keys.TAB);
            logger.info("Entered Taxomony Code Exclusions: " + taxonomyCdExcl);
        }
    }

    private void setTaxonomyCdInclInput(String taxonomyCdIncl)
    {
        if (!taxonomyCdIncl.equals(""))
        {
            WebElement taxomonyInclIn = taxonomyCdInclInput();
            taxomonyInclIn.clear();
            taxomonyInclIn.sendKeys(taxonomyCdIncl);
            // taxomonyInclIn.sendKeys(Keys.TAB);
            logger.info("Entered Taxomony Code Inclusions: " + taxonomyCdIncl);
        }
    }

    private void setClnExclInput(String clnExcl)
    {
        if (!clnExcl.equals(""))
        {
            WebElement clnExclIn = clnExclInput();
            clnExclIn.clear();
            clnExclIn.sendKeys(clnExcl);
            // clnExclIn.sendKeys(Keys.TAB);
            logger.info("Entered Client Exclusions: " + clnExcl);
        }
    }

    private void setClnInclInput(String clnIncl)
    {
        if (!clnIncl.equals(""))
        {
            WebElement clnInclIn = clnInclInput();
            clnInclIn.clear();
            clnInclIn.sendKeys(clnIncl);
            //  clnInclIn.sendKeys(Keys.TAB);
            logger.info("Entered Client Inclusions: " + clnIncl);
        }
    }

    private void setClnAcctTypExclDropDown(String clnAcctTypExcl)
    {
        WebElement clnAcctTypExclDropDown = clnAcctTypExclDropDown();
        if (!clnAcctTypExcl.equals(""))
        {
            Select select = new Select(clnAcctTypExclDropDown);
            select.selectByVisibleText(clnAcctTypExcl);
            logger.info("Selected Client Account Type Exclusions: " + clnAcctTypExcl + " from Dropdown List.");
        } else
        {
            clnAcctTypExclDropDown.sendKeys(Keys.TAB);
        }
    }

    private void setClnAcctTypInclDropDown(String clnAcctTypIncl)
    {
        WebElement clnAcctTypInclDropDown = clnAcctTypInclDropDown();
        if (!clnAcctTypIncl.equals(""))
        {
            Select select = new Select(clnAcctTypInclDropDown);
            select.selectByVisibleText(clnAcctTypIncl);
            logger.info("Selected Client Account Type Inclusions: " + clnAcctTypIncl + " from Dropdown List.");
        } else
        {
            clnAcctTypInclDropDown.sendKeys(Keys.TAB);
        }
    }

    private void setPyrExclInput(String pyrExcl)
    {
        WebElement pyrExclIn = pyrExclInput();
        if (!pyrExcl.equals(""))
        {
            pyrExclIn.clear();
            pyrExclIn.sendKeys(pyrExcl);
            // pyrExclIn.sendKeys(Keys.TAB);
            logger.info("Entered Payor Exclusions: " + pyrExcl);
        } else
        {
            //  pyrExclIn.sendKeys(Keys.TAB);
        }
    }

    private void setPyrInclInput(String pyrIncl)
    {
        WebElement pyrInclIn = pyrInclInput();
        if (!pyrIncl.equals(""))
        {
            pyrInclIn.clear();
            pyrInclIn.sendKeys(pyrIncl);
            //   pyrInclIn.sendKeys(Keys.TAB);
            logger.info("Entered Payor Inclusions: " + pyrIncl);
        } else
        {
            //  pyrInclIn.sendKeys(Keys.TAB);
        }
    }

    private void setPyrGrpExclDropDown(String pyrGrpExcl)
    {
        WebElement pyrGrpExclDropDown = pyrGrpExclDropDown();
        if (!pyrGrpExcl.equals(""))
        {
            Select select = new Select(pyrGrpExclDropDown);
            select.selectByVisibleText(pyrGrpExcl);
            logger.info("Selected Payor Group Exclusions: " + pyrGrpExcl + " from Dropdown List.");
        } else
        {
            pyrGrpExclDropDown.sendKeys(Keys.TAB);
        }
    }

    private void setPyrGrpInclDropDown(String pyrGrpIncl)
    {
        WebElement pyrGrpInclInputDropDown = pyrGrpInclDropDown();
        if (!pyrGrpIncl.equals(""))
        {
            Select select = new Select(pyrGrpInclInputDropDown);
            select.selectByVisibleText(pyrGrpIncl);
            logger.info("Selected Payor Group Inclusions: " + pyrGrpIncl + " from Dropdown List.");
        } else
        {
            pyrGrpInclInputDropDown.sendKeys(Keys.TAB);
        }
    }

    public WebElement ptNotificationConfigPageTitle()
    {
        return driver.findElement(By.cssSelector(".platormPageTitle"));
    }

    public WebElement ptNotifConfigAddBtn()
    {
        return driver.findElement(By.id("add_tbl_ptNotificationLetterConfig"));
    }

    public WebElement ptNotifConfigEditBtn()
    {
        return driver.findElement(By.id("edit_tbl_ptNotificationLetterConfig"));
    }

    public WebElement pyrGrpInclInput()
    {
        return driver.findElement(By.id("s2id_pyrGrpIncl"));
    }

    public WebElement pyrGrpInclDropDown()
    {
        return driver.findElement(By.id("pyrGrpIncl"));
    }

    public WebElement pyrGrpExclInput()
    {
//        return driver.findElement(By.xpath(".//*[@id='s2id_pyrGrpExcl']"));
        return driver.findElement(By.id("s2id_pyrGrpExcl"));
    }

    public WebElement pyrGrpExclDropDown()
    {
        return driver.findElement(By.id("pyrGrpExcl"));
    }

    public WebElement pyrInclInput()
    {
        return driver.findElement(By.id("pyrIncl"));
    }

    public WebElement pyrExclInput()
    {
        return driver.findElement(By.id("pyrExcl"));
    }

    public WebElement clnAcctTypInclInput()
    {
        return driver.findElement(By.id("s2id_clnAccntTypIncl"));
    }

    public WebElement clnAcctTypInclDropDown()
    {
        return driver.findElement(By.id("clnAccntTypIncl"));
    }

    public WebElement clnAcctTypExclInput()
    {
        return driver.findElement(By.id("s2id_clnAccntTypExcl"));
    }

    public WebElement clnAcctTypExclDropDown()
    {
        return driver.findElement(By.id("clnAccntTypExcl"));
    }

    public WebElement clnInclInput()
    {
        return driver.findElement(By.id("clnIncl"));
    }

    public WebElement clnExclInput()
    {
        return driver.findElement(By.id("clnExcl"));
    }

    public WebElement taxonomyCdInclInput()
    {
        return driver.findElement(By.id("taxonomyCdIncl"));
    }

    public WebElement taxonomyCdExclInput()
    {
        return driver.findElement(By.id("taxonomyCdExcl"));
    }

    public WebElement testTypInclInput()
    {
        return driver.findElement(By.id("s2id_testTypIncl"));
    }

    public WebElement testTypInclDropDown()
    {
        return driver.findElement(By.id("testTypIncl"));
    }

    public WebElement testTypExclInput()
    {
        return driver.findElement(By.id("s2id_testTypExcl"));
    }

    public WebElement testTypExclDropDown()
    {
        return driver.findElement(By.id("testTypExcl"));
    }

    public WebElement testInclInput()
    {
        return driver.findElement(By.id("testIncl"));
    }

    public WebElement testExclInput()
    {
        return driver.findElement(By.id("testExcl"));
    }

    public WebElement clnFacInclInput()
    {
        return driver.findElement(By.id("s2id_primaryFacIdIncl"));
    }

    public WebElement clnFacInclDropDown()
    {
        return driver.findElement(By.id("primaryFacIdIncl"));
    }

    public WebElement clnFacExclInput()
    {
        return driver.findElement(By.id("s2id_primaryFacIdExcl"));
    }

    public WebElement clnFacExclDropDown()
    {
        return driver.findElement(By.id("primaryFacIdExcl"));
    }

    public WebElement letterIdInput()
    {
        return driver.findElement(By.id("letterId"));
    }
    public WebElement titleTextInHelp()
    {
        return driver.findElement(By.xpath("/html/body/h1"));
    }
}
