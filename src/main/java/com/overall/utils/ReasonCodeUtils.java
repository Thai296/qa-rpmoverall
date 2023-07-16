package com.overall.utils;

import static org.testng.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import com.xifin.qa.dao.rpm.ErrorDao;
import com.xifin.qa.dao.rpm.ErrorDaoImpl;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TimeStamp;
import com.mbasys.mars.ejb.entity.errCdXref.ErrCdXref;
import com.mbasys.mars.ejb.entity.xref.Xref;
import com.overall.fileMaintenance.reasonCode.ReasonCode;

public class ReasonCodeUtils extends SeleniumBaseTest {
	protected Logger logger;
	private ErrorDao errorDao;
	private Actions actions;
	private TimeStamp timeStamp;
	private ReasonCode reasonCode;
	
	public ReasonCodeUtils(RemoteWebDriver driver){
		this.driver = driver;
		errorDao = new ErrorDaoImpl(config.getRpmDatabase());
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public List<List<String>>  addNewRecordAtCrossReferencesWithXrefCatIdEquals9() throws Exception {
		timeStamp = new TimeStamp();
		actions = new Actions(driver);
		reasonCode = new ReasonCode(driver, wait);
		
		List<List<String>> results = new ArrayList<List<String>>();
		String toDate = timeStamp.getCurrentDate("MM/dd/yyyy");
		Xref xref = crossReferenceDao.getRandomXrefByXrefCatId(9);
		
		scrollToElement(reasonCode.crossReferenceTblAddBtn());
		clickHiddenPageObject(reasonCode.crossReferenceTblAddBtn(),0);
		enterValues(reasonCode.crossReferenceTblEffDtInput(), toDate);
		enterValues(reasonCode.crossReferenceTblExpDtInput(), toDate);
		selectDropDown(reasonCode.crossReferenceTblDerscDdl(), xref.getDescr());
		actions.sendKeys(Keys.TAB).perform();
		
		List<String> crossRefDes = new ArrayList<String>();
		crossRefDes.add(String.valueOf(xref.getXrefId()));
		crossRefDes.add(xref.getDescr());
		crossRefDes.add(toDate);
		crossRefDes.add(toDate);
		
		results.add(crossRefDes);
		
		return results; 
	}
	
	public void checkAddNewInErrCdXref(List<String> crossRefDescrList, String date) throws Exception {
		for (int i = 0; i < crossRefDescrList.size(); i++) {
			int xrefId = Integer.parseInt(crossRefDescrList.get(i));
			ErrCdXref errCdXref = errorDao.getErrCdXrefByXrefId(xrefId).get(0);
			assertEquals(errCdXref.getEffDt().toString(), date, "        The EffDt is added correctly.");
			assertEquals(errCdXref.getExpDt().toString(), date, "        The ExpDt is added correctly.");
		}
	}
	
	public int deleteDataInErrCdXrefByList(List<String> list) throws Exception {
		int flag = 0;
		for (int i = 0; i < list.size(); i++) {
			flag = daoManagerPlatform.deleteDataFromTableByCondition(" Err_cd_xref ","PK_XREF_ID = '"+list.get(i)+"' " , testDb);
		}
		return flag;
	}
	
}
