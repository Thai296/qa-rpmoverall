package com.overall.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mbasys.mars.ejb.entity.clnBillingAssignmentTyp.ClnBillingAssignmentTyp;
import com.mbasys.mars.ejb.entity.clnBillingCategory.ClnBillingCategory;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.overall.fileMaintenance.clientBillingCategory.ClientBillingCategory;
import com.overall.menu.MenuNavigation;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.rpm.ClientDao;
import com.xifin.qa.dao.rpm.ClientDaoImpl;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.xap.utils.XifinAdminUtils;

public class ClientBillingCategoryUtils {
	private Configuration config;
	private RemoteWebDriver driver;
	private WebDriverWait wait;
	private ClientBillingCategory clientBillingCategory;
	private ClientDao clientDao;
	private Logger logger;
	private RandomCharacter randomCharacter;
	private MenuNavigation menuNavigation;
	private static final String DATA_LOCKED = "Data locked";
	private static final String CLIENT_BILLING_CATEGORY_TABLE = "CLN_BILLING_CATEGORY";
	
	public ClientBillingCategoryUtils(RemoteWebDriver driver, Configuration config, WebDriverWait wait) {
		logger = Logger.getLogger(getClass().getSimpleName());
		this.config = config;
		this.driver = driver;
		this.wait = wait;
		clientBillingCategory = new ClientBillingCategory(wait);
		clientDao = new ClientDaoImpl(config.getRpmDatabase());
		randomCharacter = new RandomCharacter();
		menuNavigation = new MenuNavigation(driver, config);
	}
	
	public void unlockUserDataIfAny(String ssoXpUsername, String ssoXpPassword, SeleniumBaseTest b, XifinPortalUtils xifinPortalUtils) throws Exception {
		String errorLockData = "";
		int count = 0;
		do {
			xifinPortalUtils.waitForPageLoaded(wait);
			try {
				if (b.isElementPresent(clientBillingCategory.lockMsgTxtLocator())) {
					logger.error("        Element found: " + clientBillingCategory.lockMsgTxt().getText());
					errorLockData = clientBillingCategory.lockMsgTxt().getText();
				}
			} catch (Exception e) {
				logger.error("        Element not found: " + e.getMessage());
			}
			if (errorLockData.contains(DATA_LOCKED)) {
				XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
				//String org = ssoXpUsername.substring(0, ssoXpUsername.indexOf("_")); //using for offsite tester
				//String userId = ssoXpUsername.substring(ssoXpUsername.indexOf("_") + 1, ssoXpUsername.length());//using for offsite tester
				String org = config.getProperty(PropertyMap.ORGALIAS);
				String userId = "x" + ssoXpUsername.substring(0, ssoXpUsername.indexOf("@"));
				xifinAdminUtils.unlocksOrganize(ssoXpUsername, ssoXpPassword, userId, org, CLIENT_BILLING_CATEGORY_TABLE, true, b);
				menuNavigation.navigateToClientBillingCatagoryPage();
				xifinPortalUtils.waitForPageLoaded(wait);
			}
			count++;
		} while (b.isElementPresent(clientBillingCategory.lockMsgTxtLocator()) && count < 5);
	}

	public void clickOnSaveAndClearBtn(SeleniumBaseTest b) throws Exception {
		b.clickHiddenPageObject(clientBillingCategory.saveAndClearBtn(), 0);
		try {
			if (b.isElementPresent(clientBillingCategory.savingLoadingMessage())) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(clientBillingCategory.savingLoadingMessage()));
			}
		} catch (Exception e){
			logger.info("There's no need to waiting element present", e);
		}
		
	}
	
	public String getRandomClnBillingCatIdUpdateHaveNotExisted(String oldId) throws Exception {
		String updateId;
		
		do {
			updateId = randomCharacter.getRandomAlphaNumericString(5);
		} while (updateId.equals(oldId) || clientDao.getDataFromClnBillingCategoryByAbbrev(updateId) != null);
		
		return updateId;
	}
	
	public String getRandomClnBillingCatIdNotExisted() throws Exception {
		String id;
		
		do {
			id = randomCharacter.getRandomAlphaNumericString(15);
		} while (clientDao.getDataFromClnBillingCategoryByAbbrev(id) != null);
		
		return id;
	}
	
	public String getRandomValueDifferentToOldValue(String oldValue) throws Exception {
		String newValue;
		
		do {
			newValue = randomCharacter.getRandomAlphaNumericString(15);
		} while (newValue.equals(oldValue));
		
		return newValue;
	}
	
	public ClnBillingAssignmentTyp getClnBillingAssignmentTypDifferentGivenOne(ClnBillingAssignmentTyp givenClnBillAssignTyp) throws XifinDataAccessException {
		ClnBillingAssignmentTyp newClnBillingAssignmentTyp;
		
		do {
			newClnBillingAssignmentTyp = clientDao.getRandomClnBillingAssignmentTyp();
		} while (newClnBillingAssignmentTyp.equals(givenClnBillAssignTyp));
		
		return newClnBillingAssignmentTyp;
	}
	
	public boolean verifyHelpPageIsDisplayedAndSwitchingToParentWindow(SeleniumBaseTest b, String url) throws Exception {
		String parentWindow = b.switchToPopupWin();
		boolean matched = false;

		if (b.verifyUrlDisplayed(url)) {
			matched = true;
		}

		b.getDriver().close();
		b.switchToParentWin(parentWindow);
		return matched;
	}
	
	public void deleteClnBillingCategoryById(String clnBillingCatId) throws XifinDataAccessException {
		ClnBillingCategory clnBillingCategoty = clientDao.getDataFromClnBillingCategoryByAbbrev(clnBillingCatId);
		if (clnBillingCategoty != null) {
			clnBillingCategoty.setResultCode(ErrorCodeMap.DELETED_RECORD);
			clientDao.setClnBillingCategory(clnBillingCategoty);
			logger.info("*** Deleted ClnBillingCategory Object is successfully ***");
		}
	}
}
