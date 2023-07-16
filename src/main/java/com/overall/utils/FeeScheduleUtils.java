package com.overall.utils;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.xifin.qa.dao.rpm.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mbasys.mars.ejb.entity.prc.Prc;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;

import domain.fileMaintenance.feeschedule.FeeScheduleInformation;

public class FeeScheduleUtils {
	protected Logger logger;
	private RemoteWebDriver driver;
	private TestDao testDao;
	private FacilityDao facilityDao;
	private PrcDao prcDao;
	private ModDao modDao;
	private RandomCharacter randomCharacter;
	private static final String ACCOUNT_TYP_CLIENT = "Client";
	private static final String BASIC_TYP_TEST_CD = "Test Code";
	private static final String BASIC_TYP_PROC_CD = "Procedure Code";
	private static final String ACCOUNT_TYP_NON_CLIENT = "Non-Client";
	private static final String PRICE_TYP_NO_RETAIL_AND_NO_EXPECT = "";
	private static final String PRICE_TYP_NO_RETAIL_AND_EXPECT = "Expect";
	private static final String PRICE_TYP_RETAIL_AND_NO_EXPECT = "Retail";
	private static final String PRICE_TYP_NORMAL = "Normal";

	
	public FeeScheduleUtils(RemoteWebDriver driver, Configuration config){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		testDao = new TestDaoImpl(config.getRpmDatabase());
		facilityDao = new FacilityDaoImpl(config.getRpmDatabase());
		prcDao = new PrcDaoImpl(config.getRpmDatabase());
		modDao = new ModDaoImpl(config.getRpmDatabase());
		randomCharacter = new RandomCharacter();
	}

	public String getAccountType(Prc prc) {
		String value = "";
		if (prc.isClientBased) {
			value = ACCOUNT_TYP_CLIENT;
		} else {
			value = ACCOUNT_TYP_NON_CLIENT;
		}
		return value;
	}
	
	public String getBasicType(Prc prc) {
		String value = "";
		if (prc.isTestBased) {
			value = BASIC_TYP_TEST_CD;
		} else {
			value = BASIC_TYP_PROC_CD;
		}
		return value;
	}
	
	public List<String> getContentFileAppend(String curTestAbbrv) throws Exception{
		TimeStamp timeStamp = new TimeStamp();
		List<String> data = new ArrayList<String>();
		RandomCharacter randomCharacter = new RandomCharacter();
		
		int testId = prcDao.getRandomPrcTestByTestIdNotExpiredInTestDtAndNotInTestProf().getTestId();
		String testAbbrv = testDao.getTestByTestId(testId).getTestAbbrev();
		
		while(testAbbrv.equals(curTestAbbrv)){
			 testId = prcDao.getRandomPrcTestByTestIdNotExpiredInTestDtAndNotInTestProf().getTestId();
			 testAbbrv = testDao.getTestByTestId(testId).getTestAbbrev();
		}
		
		String effDt = timeStamp.getCurrentDate("MM/dd/yyyy");
		String expDt = timeStamp.getCurrentDate("MM/dd/yyyy");
		String price = randomCharacter.getNonZeroRandomNumericString(5);
		String mod = modDao.getMod().getModId();
		String content = testAbbrv + "," + effDt + "," + expDt + "," + price + "," + mod + ",,";
		data = new ArrayList<String>();
		data.add(String.valueOf(testId));
		data.add(testAbbrv);
		data.add(effDt);
		data.add(expDt);
		data.add(price);
		data.add(mod);
		data.add("");
		data.add(content);
		
		return data;
	}
	
	public String getPriceType(Prc prc){
		String value = "";
		if (!prc.isRetail && !prc.isExpect) {
			value = PRICE_TYP_NORMAL;
		} else if (!prc.isRetail && prc.isExpect) {
			value = PRICE_TYP_NO_RETAIL_AND_EXPECT;
		} else if (prc.isRetail) {
			value = PRICE_TYP_RETAIL_AND_NO_EXPECT;
		} else {
			value = PRICE_TYP_NO_RETAIL_AND_NO_EXPECT;
		}
		return value;
	}
	
	public FeeScheduleInformation getFeeScheduleInformationInDB(Prc prc, String displayOption) throws Exception {
		FeeScheduleInformation feeScheduleInformation = new FeeScheduleInformation();
		String facAbbrv = facilityDao.getFacByFacId(prc.getFacId()).getAbbrv() != null ? facilityDao.getFacByFacId(prc.getFacId()).getAbbrv() : facilityDao.getFacByFacId(1).getAbbrv();
		feeScheduleInformation.setFeeScheduleId(prc.getPrcAbbrev());
		feeScheduleInformation.setName(prc.getDescr());
		feeScheduleInformation.setAccountType(getAccountType(prc));
		feeScheduleInformation.setFacility(facAbbrv);
		feeScheduleInformation.setBasisType(getBasicType(prc));
		feeScheduleInformation.setPriceType(getPriceType(prc));
		feeScheduleInformation.setDisplayOption(displayOption);
		feeScheduleInformation.setDiscountable(!prc.getIsNoDisc());
		
		return feeScheduleInformation;
	}
	
	public String getNewPrcAbbrvIsNotExistInSystem(int feeSchedule) throws Exception {
		String prcAbbrv = randomCharacter.getRandomAlphaNumericString(8);
		List<Prc> prcExisted = prcDao.getPrcExistedByFeeSchedule(feeSchedule);
		List<String> prcAbbrvExisted = new ArrayList<String>();
		for (Prc prc : prcExisted) {
			prcAbbrvExisted.add(prc.getPrcAbbrev());
		}
		
		while (prcAbbrvExisted.contains(prcAbbrv)) {
			prcAbbrv = randomCharacter.getRandomAlphaNumericString(8);
		}
		
		return prcAbbrv;
	}
	
	public void updateFeeScheduleIdWithOldValue(String newPrcAbbrv, String oldPrcAbbrv) throws XifinDataAccessException {
		Prc prc = prcDao.getPrcByPrcAbbrev(newPrcAbbrv).get(0);
		if (prc != null) {
			prc.setPrcAbbrev(oldPrcAbbrv);
			prc.setResultCode(ErrorCodeMap.RECORD_FOUND);
			prcDao.setPrc(prc);
			assertEquals(oldPrcAbbrv, prc.getPrcAbbrev(), "Data should be revert correctly");
		}
	}
	
	public Prc getDifferentPrcWithAnotherOne(Prc prc, int feeSchedule) throws XifinDataAccessException {
		Prc newPrc;
		do {
			newPrc = prcDao.getRandomPrcByFeeSchedule(feeSchedule);
		} while (newPrc.equals(prc));
		
		return newPrc;
	}
 
}
