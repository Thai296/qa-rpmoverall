package com.overall.utils;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnAccntTyp.ClnAccntTyp;
import com.mbasys.mars.ejb.entity.clnDt.ClnDt;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.overall.client.clientProcessing.ClientPricingConfiguration;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.rpm.ClientDao;
import com.xifin.qa.dao.rpm.ClientDaoImpl;
import com.xifin.qa.dao.rpm.FacilityDao;
import com.xifin.qa.dao.rpm.FacilityDaoImpl;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.SeleniumBaseTest;

import domain.client.pricingconfiguration.Header;

public class ClientPricingConfigurationUtils {
	private ClientPricingConfiguration clientPricingConfiguration;
	private ClientDao clientDao;
	private FacilityDao facilityDao;
	private static final String PRICING_CONFIGURATION_TITLE = "Pricing Configuration";
	private Logger logger;
	private WebDriverWait wait;
	
	public ClientPricingConfigurationUtils(RemoteWebDriver driver, Configuration config, WebDriverWait wait) {
		clientPricingConfiguration = new ClientPricingConfiguration(driver, wait);
		clientDao = new ClientDaoImpl(config.getRpmDatabase());
		facilityDao = new FacilityDaoImpl(config.getRpmDatabase());
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
        this.wait = wait;
	}
	
	public void verifyClientPricingConfigurationPageIsDisplayed(SeleniumBaseTest b) throws Exception {
		assertTrue(b.isElementPresent(clientPricingConfiguration.pricingConfigLoadPageTitleTxt(), 5),"        Client Pricing Configuration load page title is displayed");
		assertEquals(PRICING_CONFIGURATION_TITLE, clientPricingConfiguration.pricingConfigLoadPageTitleTxt().getText(), "        Client Pricing Configuration title equals 'Pricing Configuration'");
	}
	
	public Header getPricingConfigurationHeaderFromDB(String clnAbbrv) throws Exception {
		Header header = new Header();

		Cln cln = clientDao.getClnByClnAbbrev(clnAbbrv);
		ClnAccntTyp clnAccntTyp = clientDao.getClnAccnTypByAccnTypId(cln.getAcctTypId());
		Fac fac = facilityDao.getFacByFacId(cln.getOrderingFacId());

		header.setClientId(clnAbbrv);
		header.setClientName(cln.getBilAcctNm().toUpperCase());
		header.setAccountType(clnAccntTyp.getDescr());
		header.setFacAbbr(fac.getAbbrv());
		header.setFacNm(fac.getName());

		return header;
	}

	public Header getPricingConfigurationHeaderFromUI() throws XifinDataAccessException {
		Header header = new Header();

		header.setClientId(clientPricingConfiguration.headerClientIdTxt().getText());
		header.setClientName(clientPricingConfiguration.headerClientNameTxt().getText());
		header.setAccountType(clientPricingConfiguration.headerAccountTypeTxt().getText());
		header.setFacAbbr(clientPricingConfiguration.headerFacAbbrevTxt().getText());
		header.setFacNm(clientPricingConfiguration.headerFacNameTxt().getText());

		return header;
	}
	
	public void updateClnDtByClnAbbrev(ClnDt clnDt, int oldSpcPrcId) throws XifinDataAccessException {
		if (clnDt != null) {
			clnDt.setClnSpcPrcId(oldSpcPrcId);
			clnDt.setResultCode(ErrorCodeMap.RECORD_FOUND);
			clientDao.setClnDt(clnDt);
		}
	}
	
    /**
     * Clicks the Save and Clear button, then waits until save is complete.
     * And return total estimated impact price
     *
     * @param wait the wait handler
     */
    public Float saveAndClearAndReturnTotalEstimatedImpact() {
        WebElement saveAndClearBtn = clientPricingConfiguration.footerSaveAndClearBtn();
        logger.info("Waiting for Save and Clear button to be clickable");
        wait.until(ExpectedConditions.elementToBeClickable(saveAndClearBtn));
        saveAndClearBtn.sendKeys(Keys.ALT + "S");
        Float totalEstimatedImpact = 0.00f;
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));

        } catch (Exception e) {
        	logger.info("Save and Clear button is still visible");
        	if (clientPricingConfiguration.retroactivePrcImpactTbl().isDisplayed()) {
            	logger.info("Retroactive Pricing Impact page is displayed");
        		totalEstimatedImpact += ConvertUtil.convertStringToFloat(clientPricingConfiguration.retroactivePrcImpactTotalEstimatedImpactTxt().getText().split("\\$", 2)[1]);
        	}
            saveAndClearBtn.sendKeys(Keys.ALT + "S");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
        }
        
        return totalEstimatedImpact;
    }
}
