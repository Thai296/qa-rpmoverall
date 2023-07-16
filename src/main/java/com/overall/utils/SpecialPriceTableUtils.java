package com.overall.utils;

import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnDt.ClnDt;
import com.mbasys.mars.ejb.entity.prc.Prc;
import com.mbasys.mars.ejb.entity.prcTest.PrcTest;
import com.mbasys.mars.ejb.entity.qRetro.QRetro;
import com.mbasys.mars.ejb.entity.qRetroAccn.QRetroAccn;
import com.mbasys.mars.ejb.entity.test.Test;
import com.mbasys.mars.ejb.entity.testMsg.TestMsg;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.MiscMap;
import com.overall.fileMaintenance.pricing.SpecialPriceTable;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.rpm.AccessionDao;
import com.xifin.qa.dao.rpm.AccessionDaoImpl;
import com.xifin.qa.dao.rpm.ClientDao;
import com.xifin.qa.dao.rpm.ClientDaoImpl;
import com.xifin.qa.dao.rpm.ErrorProcessingDao;
import com.xifin.qa.dao.rpm.ErrorProcessingDaoImpl;
import com.xifin.qa.dao.rpm.FacilityDao;
import com.xifin.qa.dao.rpm.FacilityDaoImpl;
import com.xifin.qa.dao.rpm.PrcDao;
import com.xifin.qa.dao.rpm.PrcDaoImpl;
import com.xifin.qa.dao.rpm.QRetroDao;
import com.xifin.qa.dao.rpm.QRetroDaoImpl;
import com.xifin.qa.dao.rpm.TestDao;
import com.xifin.qa.dao.rpm.TestDaoImpl;
import com.xifin.qa.dao.rpm.domain.ClientRetroPriceImpact;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import domain.client.pricingconfiguration.RetroactivePriceImpact;
import domain.fileMaintenance.specialpricetable.SpecialPriceTableInformation;
import domain.filemaint.specialPriceTable.ClientPricingTable;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.testng.Assert.assertEquals;

public class SpecialPriceTableUtils {
	protected Logger logger;
	private ClientDao clientDao;
	private TestDao testDao;
	private ErrorProcessingDao errorProcessingDao;
	private FacilityDao facilityDao;
	private PrcDao prcDao;
	private QRetroDao qRetroDao;
	private RandomCharacter randomCharacter;
	private SpecialPriceTable specialPriceTable;
	private static final String ACCOUNT_TYP_CLIENT = "Client";
	private static final String ACCOUNT_TYP_NON_CLIENT = "Non-Client";
	private static final String RETRO = "RETRO";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final int CLIENT_RETAIL_PRC = 950002;
	private TimeStamp timeStamp;
	
	public SpecialPriceTableUtils(RemoteWebDriver driver, Configuration config, WebDriverWait wait) {
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		clientDao = new ClientDaoImpl(config.getRpmDatabase());
		errorProcessingDao = new ErrorProcessingDaoImpl(config.getRpmDatabase());
		testDao = new TestDaoImpl(config.getRpmDatabase());
		facilityDao = new FacilityDaoImpl(config.getRpmDatabase());
		prcDao = new PrcDaoImpl(config.getRpmDatabase());
		qRetroDao = new QRetroDaoImpl(config.getRpmDatabase());
		specialPriceTable = new SpecialPriceTable(driver, wait);
		randomCharacter = new RandomCharacter();
		timeStamp = new TimeStamp();
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
	
	public SpecialPriceTableInformation getSpecialPriceTblInforInDB(Prc prc, String displayOption) throws XifinDataAccessException {
		SpecialPriceTableInformation specialPriceTblInfor = new SpecialPriceTableInformation();
		String facAbbrv = facilityDao.getFacByFacId(prc.getFacId()).getAbbrv() != null ? facilityDao.getFacByFacId(prc.getFacId()).getAbbrv() : facilityDao.getFacByFacId(1).getAbbrv();
		specialPriceTblInfor.setSpecialPriceTableId(prc.getPrcAbbrev());
		specialPriceTblInfor.setName(prc.getDescr());
		specialPriceTblInfor.setAccountType(getAccountType(prc));
		specialPriceTblInfor.setFacility(facAbbrv);
		specialPriceTblInfor.setDisplayOption(displayOption);
		
		return specialPriceTblInfor;
	}
	
	public String getNewPrcAbbrvIsNotExistInSystem() throws Exception {
		String prcAbbrv;
		
		do {
			prcAbbrv = randomCharacter.getRandomAlphaNumericString(8);
		} while (prcDao.getPrcByPrcAbbrev(prcAbbrv).size() > 0);
		
		return prcAbbrv;
	}
	
	public void updateSpecialPriceTableIdWithOldValue(String newPrcAbbrv, String oldPrcAbbrv) throws XifinDataAccessException {
		Prc prc = prcDao.getPrcByPrcAbbrev(newPrcAbbrv).get(0);
		if (prc != null) {
			prc.setPrcAbbrev(oldPrcAbbrv);
			prc.setResultCode(ErrorCodeMap.RECORD_FOUND);
			prcDao.setPrc(prc);
			assertEquals(oldPrcAbbrv, prc.getPrcAbbrev(), "Data should be revert correctly");
		}
	}
	
	public Prc getDifferentPrcWithAnotherOne(Prc prc, int specialPriceTable) throws XifinDataAccessException {
		Prc newPrc;
		do {
			newPrc = prcDao.getRandomPrcByFeeSchedule(specialPriceTable);
		} while (newPrc.equals(prc));
		
		return newPrc;
	}
	
	public List<ClientPricingTable> getClientPricingTable() {
		int totalRowNumber = specialPriceTable.clientPayorPricingTableRow().size();
		List<ClientPricingTable> clientPricingTables = new ArrayList<>();
		
		for (int i = 2; i < totalRowNumber + 1; i++) {
			ClientPricingTable clientPricingTable = new ClientPricingTable();
			clientPricingTable.setClientId(specialPriceTable.componentClientPricingTableByRowAndAttribute(i, "abbrev").getText());;
			clientPricingTable.setName(specialPriceTable.componentClientPricingTableByRowAndAttribute(i, "name").getText());;
			clientPricingTable.setEffectiveDate(specialPriceTable.componentClientPricingTableByRowAndAttribute(i, "effDt").getText());
			clientPricingTables.add(clientPricingTable);;
		}
		return clientPricingTables;
	}
	
	public List<ClientPricingTable> getClientPricingTableFromDB(Prc prc) throws Exception {
		List<ClnDt> clnDts = clientDao.getClnDtBySpecialPrcIdWithMaxEffDt(prc.getPrcId());
		List<ClientPricingTable> clientPricingTables = new ArrayList<>();
		
		for (ClnDt clnDt : clnDts) {
			Cln cln = clientDao.getClnByClnId(clnDt.getClnId());
			ClientPricingTable clientPricingTable = new ClientPricingTable();
			clientPricingTable.setClientId(cln.getClnAbbrev());
			clientPricingTable.setName(cln.getBilAcctNm());
			clientPricingTable.setEffectiveDate(timeStamp.convertDateToString(clnDt.getClnEffDt(), "MM/dd/yyyy"));
			clientPricingTables.add(clientPricingTable);
		}
		return clientPricingTables;
	}
	
	/**
	 * Get record have to satisfy the conditions:
	 * <ul><b>
	 * <li>Test have at least 1 record</li>
	 * </b></ul>
	 * 
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public Map<String, Object> getClnSpcTestRetroImpactBySpcPrcIdAndClnId(int spcPrcId, int clnId) throws Exception {
		List<String> clnSpcTestRetroImpact;
		Test test;
		Date dos;
		String clnAbbrev;
		String clnName;
		float originalRetroPrice;
		List<PrcTest> retailPrcTests = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		
		do {
			clnSpcTestRetroImpact = prcDao.getClientSpecialTestRetroImpactBySpcPrcIdAndClnId(spcPrcId, clnId);
			test = testDao.getTestByTestId(Integer.valueOf(clnSpcTestRetroImpact.get(0)));
			dos = ConvertUtil.convertStringToSQLDate(clnSpcTestRetroImpact.get(1), DATE_FORMAT);
			clnAbbrev = clnSpcTestRetroImpact.get(2);
			clnName = clnSpcTestRetroImpact.get(3);
			clnName = clnSpcTestRetroImpact.get(3);
			originalRetroPrice = Float.parseFloat(clnSpcTestRetroImpact.get(4));
			retailPrcTests = prcDao.getListPrcTestByPrcIdAndTestId(CLIENT_RETAIL_PRC, test.getTestId());
			logger.info("Size of retail Prc Test = [" + retailPrcTests.size() + "]");
			map.put("Test", test);
			map.put("Dos", dos);
			map.put("ClnAbbrev", clnAbbrev);
			map.put("ClnName", clnName);
			map.put("OriginalRetroPrice", originalRetroPrice);
		} while (test == null && retailPrcTests.size() != 1);
		
		logger.info("clnSpcTestRetroImpact = [" + clnSpcTestRetroImpact + "]");
		return map;
	}
	
	public List<RetroactivePriceImpact> getRetroactivePriceImpactFromUI() throws Exception {
		int totalRow = specialPriceTable.retroactivePriceImpactTblRow().size();
		List<RetroactivePriceImpact> retroactivePriceImpacts = new ArrayList<>();
		
		for (int i = 2; i <= totalRow; i++) {
			RetroactivePriceImpact retroactivePriceImpact = new RetroactivePriceImpact();
			retroactivePriceImpact.setBegAmt(Double.valueOf(specialPriceTable.retroactivePriceImpactTblRecordByRowAndAttribute(i, "origPrc").getText().replace(",", "")));
			retroactivePriceImpact.setRetroAdj(Double.valueOf(specialPriceTable.retroactivePriceImpactTblRecordByRowAndAttribute(i, "retroAdjPrc").getText().replace(",", "")));
			retroactivePriceImpact.setEndAmt(Double.valueOf(specialPriceTable.retroactivePriceImpactTblRecordByRowAndAttribute(i, "newPrc").getText().replace(",", "")));
			retroactivePriceImpacts.add(retroactivePriceImpact);
		}
		
		logger.info("RetroactivePriceImpact in UI: " + retroactivePriceImpacts);
		return retroactivePriceImpacts;
	}
	
	public List<RetroactivePriceImpact> getRetroactivePriceImpactFromDB(List<ClientRetroPriceImpact> clientRetroPriceImpacts) {
		List<RetroactivePriceImpact> retroactivePriceImpacts = new ArrayList<>();
		
		Map<Date, List<ClientRetroPriceImpact>> map = new HashMap<>();
		
		for (ClientRetroPriceImpact clientRetroPriceImpact : clientRetroPriceImpacts) {
			Date key = clientRetroPriceImpact.getInvoiceDt();
			if (map.containsKey(key)) {
				List<ClientRetroPriceImpact> list = map.get(key);
				list.add(clientRetroPriceImpact);
			} else {
				List<ClientRetroPriceImpact> list = new ArrayList<>();
				list.add(clientRetroPriceImpact);
				map.put(key, list);
			}
		}
		
		SortedSet<Date> keys = new TreeSet<Date>(map.keySet());
		
		for (Date key : keys) {
			RetroactivePriceImpact retroactivePriceImpact = new RetroactivePriceImpact();
			List<ClientRetroPriceImpact> clnRetroPriceImpacts = map.get(key);
			Double retroNewPrice = 0.0;
			Double retroOriginalPrice = 0.0;
			Double retroAdjPrice = 0.0;
			for (ClientRetroPriceImpact clientRetroPriceImpact : clnRetroPriceImpacts) {
				retroNewPrice += clientRetroPriceImpact.getEndAmt();
				retroOriginalPrice += clientRetroPriceImpact.getBegAmt();
				retroAdjPrice += clientRetroPriceImpact.getRetroAdj();
			}
			
			retroactivePriceImpact.setBegAmt(roundADoubleNumberTo2Digits(retroOriginalPrice));
			retroactivePriceImpact.setRetroAdj(roundADoubleNumberTo2Digits(retroAdjPrice));
			retroactivePriceImpact.setEndAmt(roundADoubleNumberTo2Digits(retroNewPrice));
			retroactivePriceImpacts.add(retroactivePriceImpact);
			if(retroactivePriceImpacts.size() == 2) break;
		}
		
		logger.info("RetroactivePriceImpact in DB: " + retroactivePriceImpacts);
		return retroactivePriceImpacts;
	}
	
	public boolean isQRetroProcessed(List<QRetro> qRetros) {
		boolean flag = false;
		for (QRetro qRetro : qRetros) {
			if (qRetro.getIsProcessed()) {
				flag = true;
			}
		}
		return flag;
	}
	
	public boolean isQRetroAccnProcessed(List<QRetroAccn> qRetroAccns) {
		boolean flag = false;
		for (QRetroAccn qRetroAccn : qRetroAccns) {
			if (qRetroAccn.getIsRetroProcessed()) {
				flag = true;
			}
		}
		return flag;
	}
	
	public void verifyRetroPricingDisplayedCorrectly(List<RetroactivePriceImpact> actualList, List<RetroactivePriceImpact> expectedList) {
		int i = 0;
		for(RetroactivePriceImpact actual : actualList) {
			RetroactivePriceImpact expected = expectedList.get(i);
			if(actual.getBegAmt() == expected.getBegAmt() && actual.getRetroAdj() == expected.getRetroAdj()) {
				assertEquals(actual.getClientId(), expected.getClientId(), "Client Id should display correctly");
				assertEquals(actual.getClientName(), expected.getClientName(), "Client Name should display correctly");
				assertEquals(actual.getInvoiceDt(), expected.getInvoiceDt(), "Invoice Dt should display correctly");
				assertEquals(actual.getEndAmt(), expected.getEndAmt(), "End Amt should display correctly");
				assertEquals(actual.getTotalAdj(), expected.getTotalAdj(), "Total Adj should display correctly");
				assertEquals(actual.getTotalEstImp(), expected.getTotalEstImp(), "Total Est Imp should display correctly");
				assertEquals(actual.getAccnId(), expected.getAccnId(), "Accn Id should display correctly");
				assertEquals(actual.getTestId(), expected.getTestId(), "Test Id should display correctly");
				assertEquals(actual.getAccnTestSeqId(), expected.getAccnTestSeqId(), "Accn Test SeqId should display correctly");
			}
		}
	}
	
	public void cleanAllOfRetroBatchesNotCompletedByClnId(int clnId) throws Exception {
		List<QRetro> qRetros = qRetroDao.getQRetroByClnId(clnId);
		if (qRetros.size() != 0) {
			for (QRetro qRetro: qRetros) {
				String customerNote = (qRetro.getCustomerNote() == null) ? "" : qRetro.getCustomerNote();
				String interalNote = (qRetro.getInternalNote() == null) ? "" : qRetro.getInternalNote();
				if (customerNote.contains("Previous Retro Batch is not completed") || interalNote.contains("NORETRO") || interalNote.contains("RETRO") || interalNote.contains("BPRC") || qRetro.getIsProcessed() == false) {
					errorProcessingDao.deleteQRetroAccnByRetroBatchId(qRetro.getRetroBatchId());
					qRetroDao.deleteQRetroByRetroBatchId(qRetro.getRetroBatchId());
				}
			}
			logger.info("*** Previous Retro Batch is not completed cleaned ***");
		}
	}
	
	public void deletePrcTest(PrcTest prcTest) throws XifinDataAccessException {
		if (prcTest != null) {
			prcTest.setResultCode(ErrorCodeMap.DELETED_RECORD);
			prcDao.setPrcTest(prcTest);
			logger.info("*** PrcTest is revert successfully ***");
		}
	}
	
	public void deletePrc(Prc prc) throws XifinDataAccessException {
		if (prc != null) {
			List<PrcTest> prcTests = prcDao.getPrcTestByPrcId(String.valueOf(prc.getPrcId()));
			for (PrcTest prcTest : prcTests) {
				deletePrcTest(prcTest);
			}
			prc.setResultCode(ErrorCodeMap.DELETED_RECORD);
			prcDao.setPrc(prc);
			logger.info("*** Prc is revert successfully ***");
		}
	}
	
	public Float convertStringToFloat(String value) {
		Float number = Float.parseFloat(value.replaceAll("[a-z A-Z:$,]", ""));
		logger.info("         Convert Result: " + number);
		return number;
	}
	
	/**
	 * This method is used to checking TestMsg record with abbrev equals "SPDISCFLAG" either has in Database or not.
	 * <br>If this record isn't in Database, Retro Pricing Engine will run unsuccessfully.
	 * @throws XifinDataAccessException
	 */
	public void checkingSpcDiscFlagRecordInTestMsgTable() throws XifinDataAccessException {
		TestMsg testMsg = testDao.getTestMsgByAbbrev(MiscMap.TEST_MSG_SPECIAL_DISCOUNT_FLAG_ABBREV);
		
		if (testMsg == null) {
			List<Integer> pkTestMsgList = new ArrayList<>(); 
			for (TestMsg testMsgTemp : testDao.getTestMsgs()) {
				pkTestMsgList.add(testMsgTemp.getTestMsg());
			}
			
			int pk_test_msg = Collections.max(pkTestMsgList) + 1;
			
			int testMsgRecord = testDao.insertTestMsg(pk_test_msg, MiscMap.TEST_MSG_SPECIAL_DISCOUNT_FLAG_ABBREV, "SPECIAL DISCOUNT FLAG");
			assertEquals(testMsgRecord, 1, "TestMsg record should create successfully");
		}
	}
	
	private Double roundADoubleNumberTo2Digits(Double value) {
		DecimalFormat df = new DecimalFormat("#.00");
		Double number = Double.valueOf(df.format(value));
		logger.info("*** Round a number to 2 digits: " + number);
		return number;
	}
	
	public List<QRetroAccn> getQRetroAccnByRetroBatchId(List<QRetro> qRetros) throws XifinDataAccessException {
		List<QRetroAccn> qRetroAccns = new ArrayList<>();
		for(QRetro qRetro : qRetros) {
			qRetroAccns.add(errorProcessingDao.getQRetroAccnByRetroBatchId(qRetro.getRetroBatchId()));
		}
		return qRetroAccns;
	}
	
	public float getNewRetroPrice(float originalRetroPrice) {
		float newRetroPrice = RandomCharacter.getRandomIntInRange(1, 1000);
		do {
			newRetroPrice = RandomCharacter.getRandomIntInRange(1, 1000);
		} while(newRetroPrice == originalRetroPrice);
			return newRetroPrice;
	}
}
