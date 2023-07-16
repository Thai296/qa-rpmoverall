package com.overall.utils;

import static org.testng.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.mbasys.common.utility.Money;
import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnErr.AccnErr;
import com.mbasys.mars.ejb.entity.accnLnk.AccnLnk;
import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.accnProcErr.AccnProcErr;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.accnRmk.AccnRmk;
import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.ejb.entity.accnTestErr.AccnTestErr;
import com.mbasys.mars.ejb.entity.accnTestMsg.AccnTestMsg;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnDt.ClnDt;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.prc.Prc;
import com.mbasys.mars.ejb.entity.prcTest.PrcTest;
import com.mbasys.mars.ejb.entity.procCd.ProcCd;
import com.mbasys.mars.ejb.entity.procCdTyp.ProcCdTyp;
import com.mbasys.mars.ejb.entity.pyrModSplitPyr.PyrModSplitPyr;
import com.mbasys.mars.ejb.entity.qFrPending.QFrPending;
import com.mbasys.mars.ejb.entity.rmkCd.RmkCd;
import com.mbasys.mars.ejb.entity.systemSetting.SystemSetting;
import com.mbasys.mars.ejb.entity.systemStSettings.SystemStSettings;
import com.mbasys.mars.ejb.entity.test.Test;
import com.mbasys.mars.ejb.entity.testDt.TestDt;
import com.mbasys.mars.ejb.entity.testFac.TestFac;
import com.mbasys.mars.ejb.entity.testMsg.TestMsg;
import com.mbasys.mars.ejb.entity.testProc.TestProc;
import com.mbasys.mars.ejb.entity.testProf.TestProf;
import com.mbasys.mars.ejb.entity.testPyr.TestPyr;
import com.mbasys.mars.ejb.entity.testPyrExcl.TestPyrExcl;
import com.mbasys.mars.ejb.entity.testPyrGrpExcl.TestPyrGrpExcl;
import com.mbasys.mars.ejb.entity.testPyrGrpMod.TestPyrGrpMod;
import com.mbasys.mars.ejb.entity.testPyrMod.TestPyrMod;
import com.mbasys.mars.ejb.entity.testXref.TestXref;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.mbasys.mars.persistance.SystemStateSettingMap;
import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.AccessionDao;
import com.xifin.qa.dao.rpm.AccessionDaoImpl;
import com.xifin.qa.dao.rpm.ClientDao;
import com.xifin.qa.dao.rpm.ClientDaoImpl;
import com.xifin.qa.dao.rpm.ErrorDao;
import com.xifin.qa.dao.rpm.ErrorDaoImpl;
import com.xifin.qa.dao.rpm.FacilityDao;
import com.xifin.qa.dao.rpm.FacilityDaoImpl;
import com.xifin.qa.dao.rpm.PayorDao;
import com.xifin.qa.dao.rpm.PayorDaoImpl;
import com.xifin.qa.dao.rpm.PrcDao;
import com.xifin.qa.dao.rpm.PrcDaoImpl;
import com.xifin.qa.dao.rpm.ProcedureCodeDao;
import com.xifin.qa.dao.rpm.ProcedureCodeDaoImpl;
import com.xifin.qa.dao.rpm.RpmDao;
import com.xifin.qa.dao.rpm.RpmDaoImpl;
import com.xifin.qa.dao.rpm.SystemDao;
import com.xifin.qa.dao.rpm.SystemDaoImpl;
import com.xifin.qa.dao.rpm.TestDao;
import com.xifin.qa.dao.rpm.TestDaoImpl;
import com.xifin.qaautomation.webservices.accession.utils.AccessionWsUtils;
import com.xifin.qaautomation.webservices.accession.utils.TearDownUtils;
import com.xifin.utils.SeleniumBaseTest;

import flex.messaging.log.Log;

public class SplitAccessionUtils {
	private AccessionDao accessionDao;
	private TestDao testDao;
	private PrcDao prcDao;
	private PayorDao payorDao;
	private ErrorDao errorDao;
	private SystemDao systemDao;
	private RpmDao rpmDao;
	private ClientDao clientDao;
	private TearDownUtils tearDownUtils;
	private DaoManagerXifinRpm daoManagerXifinRpm;
	private AccessionWsUtils accessionWsUtils;
	private ProcedureCodeDao procedureCodeDao;
	private FacilityDao facilityDao;
	
	private static final int FIRST_RECORD = 0;
	private static final String CLN_BIL_REF = "Client Bill Referrals Split Accession";
	private static final String FLOW_SPLIT_ACCN = "Combination of flow and molecular Split Accession";
	private static final String CLN_BILL_C = "C";
	private static final String CLN_BILL_XDB = "Direct Billing Split Accession";
	private static final String NON_FDA_SPLIT_ACCN = "NonFDA Split Accession";
	private static final String TEST_MSG_DB = "DB";
	private static final String RMK_CD = "DBACCNS";
	private static final String ERROR_CD_DIRECTBILLING = "DIRECTBILLING";
	private static final String ADHOC_MAN = "AD HOC/Manual Split Accession";
	private static final String HMO = "California HMO Split Accession";
	private static final String ESRD = "ESRD Dialysis Split Accession";
	private static final String XG = "Technical Component of Physician Pathology Services Split Accession";
	
	private static final Logger LOGGER = Logger.getLogger(SplitAccessionUtils.class.getSimpleName());
	
	public SplitAccessionUtils(SeleniumBaseTest b, RemoteWebDriver driver, WebDriverWait wait, Configuration config) {
		accessionDao = new AccessionDaoImpl(config.getRpmDatabase());
		testDao = new TestDaoImpl(config.getRpmDatabase());
		prcDao = new PrcDaoImpl(config.getRpmDatabase());
		payorDao = new PayorDaoImpl(config.getRpmDatabase());
		errorDao = new ErrorDaoImpl(config.getRpmDatabase());
		systemDao = new SystemDaoImpl(config.getRpmDatabase());
		rpmDao = new RpmDaoImpl(config.getRpmDatabase());
		clientDao = new ClientDaoImpl(config.getRpmDatabase());
		tearDownUtils = new TearDownUtils(config);
		daoManagerXifinRpm = new DaoManagerXifinRpm(config.getRpmDatabase());
		accessionWsUtils = new AccessionWsUtils(config);
		procedureCodeDao = new ProcedureCodeDaoImpl(config.getRpmDatabase());
		facilityDao = new FacilityDaoImpl(config.getRpmDatabase());
	}
	
	/**
	 * Verify AccnLnk is created successful
	 * @param accnId
	 * @param splitAccnId
	 * @return splitAccnId
	 * @throws XifinDataAccessException
	 */
	public String verifyAccnLnkIsCreatedSuccessful(String accnId, String splitTyp) throws XifinDataAccessException {
		List<AccnLnk> accnLnks = accessionDao.getAccnLnkByAccnId(accnId);
		String expSplitAccn = accnId + splitTyp;
		String actSplitAccn = (accnLnks.size() > 0) ? accnLnks.get(FIRST_RECORD).getLnkAccnId() : null;
		assertEquals(actSplitAccn, expSplitAccn, "AccnLnk isn't exist in system, accnId=" + accnId + ", splitAccnId=" + expSplitAccn);
		
		return actSplitAccn;
	}
	
	/**
	 * Verify AccnTestMsg is work properly by AccnTest and split type
	 * @param accnTest
	 * @param splitTyp
	 * @throws XifinDataAccessException
	 */
	public AccnTestMsg verifyAccnTestMsgIsWorkProperlyByAccnTestAndSplitTyp(String accnId, String splitTyp) throws XifinDataAccessException {
		AccnTestMsg accnTestMsg = accessionDao.getAccnTestMsgByAccnId(accnId);
		
		switch(splitTyp) {
		case MiscMap.ACCN_SPLIT_TYPE_CLN_BILL_REF_SUFFIX:
			assertEquals(accnTestMsg.getNote(), CLN_BIL_REF, "Cln Bill Ref type isn't update correctly");
			break;
		case MiscMap.ACCN_SPLIT_TYPE_FLOW_SUFFIX:
			assertEquals(accnTestMsg.getNote(), FLOW_SPLIT_ACCN, "Combination of flow and molecular type isn't update correctly");
			break;
		case MiscMap.ACCN_SPLIT_TYPE_DIRECT_BILLING_SUFFIX:
			assertEquals(accnTestMsg.getNote(), CLN_BILL_XDB,"Direct Billing type isn't update correctly");
			break;
		case MiscMap.ACCN_SPLIT_TYPE_NONFDA_SUFFIX:
			assertEquals(accnTestMsg.getNote(), NON_FDA_SPLIT_ACCN,"Non-FDA Tests to Client type isn't update correctly");
			break;
		case MiscMap.ACCN_SPLIT_TYPE_MAN_SUFFIX:
			assertEquals(accnTestMsg.getNote(), ADHOC_MAN, "AD HOC/Manual type isn't update correctly");
			break;
		case MiscMap.ACCN_SPLIT_TYPE_HMO_SUFFIX:
			assertEquals(accnTestMsg.getNote(), HMO, "California HMO type isn't update correctly");
			break;
		case MiscMap.ACCN_SPLIT_TYPE_ESRD_SUFFIX:
			assertEquals(accnTestMsg.getNote(), ESRD, "Dialysis / ESRD type isn't update correctly");
			break;
		case MiscMap.ACCN_SPLIT_TYPE_PATHOLOGY_SUFFIX:
			assertEquals(accnTestMsg.getNote(), XG, "Technical Component of Physician Pathology Services Split Accession type isn't update correctly");
			break;
		default:
			Assert.fail("Split Typ is invalid, splitTyp=" + splitTyp);
		}
		
		return accnTestMsg;
	}
	
	/**
	 * Verify Split accn is work properly for clnBilRef type
	 * @param splitAccnId
	 * @throws Exception
	 */
	public void verifySplitAccnIsWorkProperlyForClnBilRef(String splitAccnId, String testAbbrev) throws Exception {
		AccnTest splitAccnTest = accessionDao.getAccnTestByAccnIdTestAbbrev(splitAccnId, testAbbrev).get(FIRST_RECORD);
		AccnPyr accnPyr = accessionDao.getAccnPyrFromAccnByAccnId(splitAccnId);
		
		assertEquals(payorDao.getPyrByPyrId(accnPyr.getPyrId()).getPyrAbbrv(), CLN_BILL_C, "Split accession isn't update Cln Pyr billed");
		verifyPrcForSplitAccession(splitAccnId, splitAccnTest);
	}
	
	/**
	 * Verify Prc for split accession
	 * @param accnId
	 * @param actAccnTest
	 * @throws Exception
	 */
	private void verifyPrcForSplitAccession(String accnId, AccnTest actAccnTest) throws Exception {
		Accn accn = accessionDao.getAccn(accnId);
		TestFac testFac = testDao.getTestFacByTestIdFacIdAndMaxEffDt(actAccnTest.getTestId(), actAccnTest.getFacId(), accn.getDos());
		PrcTest prcTest = prcDao.getPrcTestByPrcIdTestIdAndMaxEffDt(actAccnTest.getBilPrcId(), actAccnTest.getTestId(), accn.getDos());
		
		Float actBilPrc = actAccnTest.getBilPrcAsMoney().asDollars();
		Float expBilPrc = getPrcForAccnTestByPrcTestAndTestFac(prcTest, testFac);
		
		assertEquals(actBilPrc, expBilPrc, "BilPrc don't update as expected");
	}
	
	/**
	 * Get Prc for AccnTest by PrcTest and TestFAc
	 * @param prcTest
	 * @param testFac
	 * @return Float
	 */
	private Float getPrcForAccnTestByPrcTestAndTestFac(PrcTest prcTest, TestFac testFac) {
		Float dollar = 0.0f;
		
		if (isPrcTestBelowLabCost(prcTest, testFac)) {
			dollar = testFac.getLabCostAsMoney().asDollars();
		} else {
			dollar = prcTest.getPriceAsMoney().asDollars();
		}
		
		return dollar;
	}
	
	/**
	 * Check is PrcTest below lab cost
	 * @param prcTest
	 * @param testFac
	 * @return boolean
	 */
	private boolean isPrcTestBelowLabCost(PrcTest prcTest, TestFac testFac) {
		boolean flag = false;
		
		Float prcTestDollar = (prcTest != null) ? prcTest.getPriceAsMoney().asDollars() : 0.0f;
		Float labCostDollar = (testFac != null) ? testFac.getLabCostAsMoney().asDollars() : 0.0f;
		
		if (prcTestDollar < labCostDollar) {
			flag = true;
		}
		
		return flag;
	}
	
	/**
	 * Delete accn and accn_lnk by accnId, splitAccnId and testAbbrev
	 * @param accnId
	 * @param splitAccnId
	 * @param testAbbrev
	 * @throws Exception
	 */
	public void deleteAccnAndAccnLnkByAccnIdSplitAccnIdAndTestAbbrev(String accnId, String splitAccnId) throws Exception {
		List<AccnTest> splitAccnTests = accessionDao.getAccnTestsByAccnId(splitAccnId);
		
		for (AccnTest splitAccnTest : splitAccnTests) {
			splitAccnTest.setSourceAccnId(null);
			splitAccnTest.setResultCode(ErrorCodeMap.RECORD_FOUND);
			accessionDao.setAccnTest(splitAccnTest);
		}
		
		if (accnId != null) {
			
			tearDownUtils.deleteAccession(accnId);
		}
		
		if (splitAccnId != null) {
			tearDownUtils.deleteAccession(splitAccnId);
		}
	}
	
	/**
	 * Verify Test Msg
	 * @param accnTest
	 * @throws Exception
	 * */
	public void  verifyTestMsgIsWorkProperly(AccnTestMsg accnTestMsg) throws Exception {
		TestMsg testMsg = testDao.getTestMsg(accnTestMsg.getTestMsgId());

		assertEquals(testMsg.getAbbrev(), TEST_MSG_DB, "Accession isn't update Test Msg correctly");
	}

	/**
	 * Verify RmkCd
	 * @param splitAccnId
	 * @throws Exception
	 * */
	public  void verifyRmkCdIsWorkProperly(String splitAccnId) throws Exception {
		AccnRmk accnRmk = accessionDao.getAccnRmkByAccnId(splitAccnId);
		RmkCd rmkCd = errorDao.getRmkCd(accnRmk.getRmkCdId());

		assertEquals(rmkCd.getAbbrv(), RMK_CD, "Split Accession isn't update Rmk Cd correctly");
	}

	/**
	 * Verify ErrCd
	 * @param splitAccnId
	 * @throws Exception
	 * */
	public void verifyErrCdIsWorkProperly(String splitAccnId) throws Exception {
		AccnErr accnErr = accessionDao.getAccnErrsByAccnId(splitAccnId).get(FIRST_RECORD);
		ErrCd errCd = errorDao.getErrCdByErrCd(accnErr.getErrCd());

		assertEquals(errCd.getAbbrev(), ERROR_CD_DIRECTBILLING, "Split Accession isn't update Err Cd correctly");
	}

	/**
	 * verify Status Price
	 * @param splitAccnId
	 * @param accnStatusEp
	 * @throws Exception
	 * */
	public void verifyStatusPriceIsWorkProperlyBySplitAccn(String splitAccnId, int accnStatusEp) throws Exception {
		Accn accn = accessionDao.getAccn(splitAccnId);
		assertEquals(accn.getStaId(), accnStatusEp, "Split Accession isn't update sta_id correctly");
	}

	public void deleteTestByTestCodeId(String testCodeId) throws XifinDataAccessException {
		LOGGER.info("*** Starting delete Test ***");
		Test test = testDao.getTestByTestAbbrev(testCodeId);
		if (test != null) {
			deletePrcTestByTest(test);
			deleteTestProfByTest(test);
			testDao.deleteTestFac(test.getTestId());
			testDao.deleteTestProc(test.getTestId());
			deleteTestPyrExclByTest(test);
			deleteTestPyrGrpExclByTest(test);
			deleteTestPyrGrpModByTest(test);
			deleteTestPyrModByTest(test);
			deleteTestDtByTest(test);
			deleteTestXrefByTest(test);
			deleteTestPyrByTest(test);
			test.setResultCode(ErrorCodeMap.DELETED_RECORD);
			testDao.setTest(test);
			LOGGER.info("*** Record Deleted ***");
		}
	}
	
	private void deletePrcTestByTest(Test test) throws XifinDataAccessException {
		LOGGER.info("*** Starting delete PrcTest ***");
		for (PrcTest prcTest : prcDao.getPrcTestByTestId(String.valueOf(test.getTestId()))) {
			if (prcTest != null) {
				prcTest.setResultCode(ErrorCodeMap.DELETED_RECORD);
				prcDao.setPrcTest(prcTest);
			}

		}
		LOGGER.info("*** Delete PrcTest successful ***");
	}
	
	private void deleteTestProfByTest(Test test) throws XifinDataAccessException {
		LOGGER.info("*** Starting delete TestProf ***");
		for (TestProf testProf : testDao.getTestProfById(test.getTestId())) {
			testProf.setResultCode(ErrorCodeMap.DELETED_RECORD);
			testDao.setTestProf(testProf);
		}
	}
	
	private void deleteTestPyrExclByTest(Test test) throws XifinDataAccessException {
		LOGGER.info("*** Starting delete TestPyrExcl ***");
		List<TestPyrExcl> testPyrExcls = testDao.getTestPyrExclByTestAbbrv(test.getTestAbbrev());
		for (TestPyrExcl testPyrExcl : testPyrExcls) {
			testPyrExcl.setResultCode(ErrorCodeMap.DELETED_RECORD);
			testDao.setTestPyrExcl(testPyrExcl);
		}
	}
	
	private void deleteTestPyrGrpExclByTest(Test test) throws XifinDataAccessException {
		LOGGER.info("*** Starting delete TestPyrGrpExcl ***");
		for (TestPyrGrpExcl testPyrGrpExcl : testDao.getTestPyrGrpExclByTestAbbrv(test.getTestAbbrev())) {
			testPyrGrpExcl.setResultCode(ErrorCodeMap.DELETED_RECORD);
			testDao.setTestPyrGrpExcl(testPyrGrpExcl);
		}
	}
	
	private void deleteTestPyrGrpModByTest(Test test) throws XifinDataAccessException {
		LOGGER.info("*** Starting delete TestPyrGrpMod ***");
		for (TestPyrGrpMod testPyrGrpMod : testDao.getTestPyrGrpModByTestAbbrev(test.getTestAbbrev())) {
			testPyrGrpMod.setResultCode(ErrorCodeMap.DELETED_RECORD);
			testDao.setTestPyrGrpMod(testPyrGrpMod);
		}
	}
	
	private void deleteTestPyrModByTest(Test test) throws XifinDataAccessException {
		LOGGER.info("*** Starting delete TestPyrMod ***");
		for (TestPyrMod testPyrMod : testDao.getTestPyrModByTestAbbrev(test.getTestAbbrev())) {
			testPyrMod.setResultCode(ErrorCodeMap.DELETED_RECORD);
			testDao.setTestPyrMod(testPyrMod);
		}
	}
	
	private void deleteTestDtByTest(Test test) throws XifinDataAccessException {
		LOGGER.info("*** Starting delete TestDt ***");
		for (TestDt testDt : testDao.getTestDtByTestAbbrv(test.getTestAbbrev())) {
			testDt.setResultCode(ErrorCodeMap.DELETED_RECORD);
			testDao.setTestDt(testDt);
		}
	}
	
	private void deleteTestXrefByTest(Test test) throws XifinDataAccessException {
		LOGGER.info("*** Starting delete TestXref ***");
		for (TestXref testXref : testDao.getTestXrefByTestAbbrv(test.getTestAbbrev())) {
			testXref.setResultCode(ErrorCodeMap.DELETED_RECORD);
			testDao.setTestXref(testXref);
		}
	}

	private void deleteTestPyrByTest(Test test) throws XifinDataAccessException {
		LOGGER.info("*** Starting delete TestPyr ***");
		for (TestPyr testPyr : testDao.getTestPyrByTestAbbrev(test.getTestAbbrev())) {
			testPyr.setResultCode(ErrorCodeMap.DELETED_RECORD);
			testDao.setTestPyr(testPyr);
		}
	}
	
	/**
	 * Get list of procedure codes of system state settings 3000
	 * @param SystemSettingId
	 * @return procCds
	 * @throws XifinDataAccessException
	 */
	public String getProcCdsInSystemStSettings(String SystemSettingId) throws XifinDataAccessException {
		SystemStSettings ss = systemDao.getSystemSateSetting(SystemStateSettingMap.SSS_PAP_TANNER_PROC_CDS);
		String procCds = "'" + ss.getDataValue().replace(",", "','") + "'";
		
		return procCds;
	}
	
	/**
	 * verify Accession Link Is Created Correctly With Accession Link Type
	 * @param accnId
	 * @param accnLnkTyp
	 * @throws XifinDataAccessException
	 */
	public void verifyAccnLnkIsCreatedCorrectlyWithAccnLnkTyp(String accnId, int accnLnkTyp) throws XifinDataAccessException {
		assertEquals(accessionDao.getAccnLnkByAccnId(accnId).get(FIRST_RECORD).getAccnLnkTypId(), accnLnkTyp, "The accnLnkTypId should match to " + accnLnkTyp);
	}
	
	 public int getNextAvailableSplitAccnSuffixId(String accessionId, String accessionSuffix) throws XifinDataAccessException 
	{
		boolean exists = true; // this seems inefficient but it looks simpler.
		int count = 1;
		String accessionIdString;
		while (exists)
		{
			try
			{
				accessionIdString = accessionId + accessionSuffix + count;
				LOGGER.info("getting accnId = " + accessionId);
				accessionDao.getAccn(accessionIdString);
				count++;
			}
			catch (XifinDataNotFoundException onfe)
			{
				exists = false;
		}
	}
	return (count-1);
	}
	 
	 
	public void getErrsWhenCreateAccn(String accnId) throws Exception {
		
		LOGGER.info("					Log errrs when create new Accn");
		List<AccnErr> accnErrlst = accessionDao.getAccnErrsByAccnId(accnId);
		List<AccnTestErr> accnTestErrlst = accessionDao.getAccnTestErrsByAccnId(accnId);
		List<AccnProcErr> accnProcErrlst = accessionDao.getAccnProcErrsByAccnId(accnId);
		List<AccnPyrErr> accnPyrErrlst = accessionDao.getAccnPyrErrsByAccnId(accnId);
		List<AccnTest> accnTestlst = accessionDao.getAccnTestsByAccnId(accnId);
		
		if(!accnErrlst.isEmpty()) {
			for (AccnErr accnErr : accnErrlst) {
				LOGGER.info("accnErr :" + accnErr);
				LOGGER.info("Error code of accnErr: " + errorDao.getErrCdByErrCd(accnErr.getErrCd()));
			}
		}

		if(!accnTestErrlst.isEmpty()) {
			for (AccnTestErr accnTestErr : accnTestErrlst) {
				LOGGER.info("accnTestErr :" + accnTestErr);
				LOGGER.info("Error code of accnTestErr: " + errorDao.getErrCdByErrCd(accnTestErr.getErrCd()));
			}
		}

		if(!accnProcErrlst.isEmpty()) {
			for (AccnProcErr accnProcErr : accnProcErrlst) {
				LOGGER.info("accnProcErr :" + accnProcErr);
				LOGGER.info("Error code of accnProcErr: " + errorDao.getErrCdByErrCd(accnProcErr.getErrCd()));
			}
		}
		
		if(!accnPyrErrlst.isEmpty()) {
			for (AccnPyrErr accnPyrErr : accnPyrErrlst) {
				LOGGER.info("accnPyrErr :" + accnPyrErr);
				LOGGER.info("Error code of accn_pyr_err: " + errorDao.getErrCdByErrCd(accnPyrErr.getErrCd()));
			}
		}
		
		if(!accnTestlst.isEmpty()) {
			for (AccnTest accnTest : accnTestlst) {
				LOGGER.info("			accnTest :" + accnTest);
				Test test = testDao.getTestByTestId(accnTest.getTestId());
				LOGGER.info("			testAbbrev :" + test.getTestAbbrev());
				
			}
		}
		
	}
	
	public void deleteEligHistFromACCNELIGHISTByAccnIdPyrAbbrev(String accnId, String pyrAbbrev) {
		try {
			daoManagerXifinRpm.deleteEligHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void  getAccnQueByAccnIdAndTyp(String accnId, int typ) throws XifinDataAccessException {
		List<AccnQue> accnQuelst = null;
		AccnQue accnQue = null;
		accnQuelst = accessionDao.getAccnQueByAccnIdAndQTyp(accnId, typ);
		accnQue = accessionDao.getAccnQueByAccnId(accnId);
		
		accnQuelst = accessionDao.getAccnQueByAccnIdAndQTyp(accnId, typ);
		
		if(!accnQuelst.isEmpty()) {
			for (AccnQue accnque : accnQuelst) {
				LOGGER.info("accnQue :" + accnque.getAccnId());
				LOGGER.info("accnQue :" + accnque.getQTyp());
			}
		}
		
		LOGGER.info("accnQue1 :" + accnQue.getAccnId());
		LOGGER.info("accnQue2 :" + accnQue.getQTyp());
	}
	
	public void getAccnInAccnQueByAccnIdAndOutDtIsNull(String accnId) {
		try {
			
			LOGGER.info("Accn In Accn_Que By AccnId And OutDt Is Null: " + accessionDao.getCurrentAccnQue(accnId));
		
		} catch (XifinDataAccessException e) {
			LOGGER.info("Get accn failed by XifinDataAccessException");
		} catch (XifinDataNotFoundException e) {
			LOGGER.info("Get accn failed by XifinDataNotFoundException");
		}
	}
	
	public void getAccnInQEligByAccnId(String accnId) {
		try {
			
			LOGGER.info("Accn In QElig By AccnId: " + accessionDao.getQEligByAccnId(accnId));
		
		} catch (XifinDataAccessException e) {
			LOGGER.info("Get QElig failed by XifinDataAccessException");
		} catch (XifinDataNotFoundException e) {
			LOGGER.info("Get QElig failed by XifinDataNotFoundException");
		}
	}
	
	public void getAccnInAccnQueByAccnIdAndOutDtIsNullByTimeWait(String accnId, long maxTime) {
		long startTime = System.currentTimeMillis();
        maxTime += startTime;
		while (System.currentTimeMillis() < maxTime) {
			try {
				
				LOGGER.info("Accn In Accn_Que By AccnId And OutDt Is Null: " + accessionDao.getCurrentAccnQue(accnId));
			
			} catch (XifinDataAccessException e) {
				LOGGER.info("Get accn failed by XifinDataAccessException");
			} catch (XifinDataNotFoundException e) {
				LOGGER.info("Get accn failed by XifinDataNotFoundException");
			}
		}
	}
	
	public void updateAccnAndAccnProc(String accnId) throws XifinDataAccessException, XifinDataNotFoundException {
		
		try {
			Accn accn = accessionDao.getAccn(accnId);
			if (accn != null && accn.getDueAmt() != 0) {
				accn.setDueAmt(0);
				accn.setModified(true);
				accn.setResultCode(ErrorCodeMap.RECORD_FOUND);
				accessionDao.setAccn(accn);
			}
			
			try {
				AccnProc accnProc = accessionDao.getAccnProcByAccnId(accnId);
				if (accnProc != null && accnProc.getDueAmt() != 0) {
					accnProc.setDueAmt(0);
					accnProc.setDueAmtAsMoney(Money.ZERO);
					accnProc.setDueAmtWithBulk(0);
					accnProc.setDueAmtWithBulkAsMoney(Money.ZERO);
					accnProc.setModified(true);
					accnProc.setResultCode(ErrorCodeMap.RECORD_FOUND);
					rpmDao.setAccnProc(null, accnProc);
				}
			} catch (Exception e) {
				LOGGER.info("Error updating accn_proc");
			}
			
		} catch (Exception e) {
			LOGGER.info("Error updating accn");
		}
	}
	
	public void getSystemSettingBySystemSettingId(int id) {
		try {
			
			systemDao.getSystemSetting(id);
			
		}catch(Exception e) {
			LOGGER.info("Error getting SystemSetting");
		}
	}
	
	public void logAllClnHasHospitalClnTyp(List<Cln> clns) {
		try {

			for(Cln cln : clns) {
				
				LOGGER.info("ClnClnHasHospitalClnTyp :" + cln);
				
				List<ClnDt> clnDts = clientDao.getClnDtByClnId(cln.getClnId());
				
				for (ClnDt clnDt : clnDts) {
				
					LOGGER.info("		ClnDt :" + clnDt);
				
				}
				
			}

		} catch (Exception e) {
			LOGGER.info("Error getting cln");
		}
	}
	
	public void waitingAccessionIdProcess(String accnId, int qType, long timeWait) {
		try {
			accessionDao.waitForAccnToBeInTheQueue(accnId, qType, timeWait);
		} catch (Exception e) {
			LOGGER.info("Error waiting Accession is process by engine");
		}
	}
	
	public void logAllTestHasDiagnosticLabAndNotProfessionalProcCd() {
		try {
			List<Test> tests = testDao.getTestHasDiagnosticLabAndNotProfessionalProcCd();
			
			for (Test test : tests) {
				LOGGER.info("Test : " + test);
				List<TestDt> testDts = testDao.getTestDtByTestId(test.getTestId());
				
				for (TestDt testDt : testDts) {
					
					LOGGER.info("	TestDt : " + testDt);
				}
			}
			
		} catch (Exception e) {
			LOGGER.info("Error logAllTestHasDiagnosticLabAndNotProfessionalProcCd");
		}
	}
	
	public void updateReleaseDtInQFrPending(String accnId) throws Exception {

		try {
			QFrPending qFrPending = accessionDao.getQFrPendingByAccnId(accnId);
			LOGGER.info("QFrPending: " + qFrPending);
			
			if (qFrPending != null) {
				LOGGER.info("Start updadting qFrPending");
				accessionWsUtils.updateReleaseDtInQFrPending(accnId);
				LOGGER.info("Update qFrPending successful");
			}

		} catch (NullPointerException e) {
			LOGGER.info("Error update QFrPending by NullPointerException");
		}

	}
	
	public void logAllPyrModSplitPyr() {
		try {
			List<PyrModSplitPyr> pyrModSplitPyrs = payorDao.getPyrModSplitPyrForCaliforniaHMO();
			for (PyrModSplitPyr pyrModSplitPyr : pyrModSplitPyrs) {
				LOGGER.info("pyrModSplitPyr: " + pyrModSplitPyr);
				LOGGER.info("PyrId: " + payorDao.getPyrByPyrId(pyrModSplitPyr.getPyrId()));
				LOGGER.info("SplitPyrId: " + payorDao.getPyrByPyrId(pyrModSplitPyr.getSplitPyrId()));
			}
		} catch (Exception e) {
			LOGGER.info("Error getting the pyrModSplitPyr");
		}
	}
	
	public void logAllClnHasHospitalClnTypAndArrOnFileSvcNo() {
		try {
			List<Cln> clns = clientDao.getClnHasHospitalClnTypAndArrOnFileSvcNo();
			for (Cln cln : clns) {
				LOGGER.info("cln: " + cln);
				
				List<ClnDt> clnDts = clientDao.getClnDtByClnId(cln.getClnId());
				
				for (ClnDt clnDt : clnDts) {
					LOGGER.info("cln_dt: " + clnDt);
				}
				
			}
		} catch (Exception e) {
			LOGGER.info("Error getting all the cln");
		}
	}
	
	public void updateClnDtByCln() {
        try {
            Cln cln = clientDao.getClnByClnAbbrev("DIALYSIS1");
            if (cln != null) {
                LOGGER.info("Start updadting cln");
                cln.setIsClnBillRefLab(true);
                cln.setModified(true);
                cln.setResultCode(ErrorCodeMap.RECORD_FOUND);
                clientDao.setCln(cln);
            }
            
        } catch (Exception e) {
           
        	LOGGER.info("Error updating cln");
       
        }
        
    }
	
	public void logAllPrc(String testAbbrev) {
		try {
			
			LOGGER.info("Start logging Test");
			Test test = testDao.getTestByTestAbbrev(testAbbrev);
			List<PrcTest> prcTests = prcDao.getPrcTestByTestId(Integer.toString(test.getTestId()));
			for (PrcTest prcTest : prcTests) {
				LOGGER.info("PrcTest: " + prcTest);
				Prc prc = prcDao.getPrcByPrcId(prcTest.getPrcId());
				LOGGER.info("Prc: " + prc);
			}
			
		} catch (Exception e) {
			LOGGER.info("Error logging test");
		}
	}
	
	public void updateTestFac(String testAbbrev, int facId) {
		try {
			
			LOGGER.info("					facId: " + facId);
            Test test = testDao.getTestByTestAbbrev(testAbbrev);
            TestFac testFac = testDao.getTestFacByTestId(test.getTestId());
            LOGGER.info("TestFac before update testFac: " + testFac.getFacId());
            if (testFac != null) {
            	
                LOGGER.info("Start updadting testFac");
                testFac.setFacId(facId);
                testFac.setModified(true);
                testFac.setResultCode(ErrorCodeMap.RECORD_FOUND);
                testDao.setTestFac(testFac);
            }
            
            TestFac testFac1 = testDao.getTestFacByTestId(test.getTestId());
           
            LOGGER.info("TestFac after update testFac1: " + testFac1.getFacId());
            
        } catch (Exception e) {
           
        	LOGGER.info("Error updating Test Fac");
       
        }
	}
	
	public void logAllTest(String testId) {
		try {
			Test testKRAS = testDao.getTestByTestAbbrev(testId);
			
			List<TestProf> testProfs = testDao.getTestProfById(testKRAS.getTestId());
			for (TestProf testProf : testProfs) {
				LOGGER.info("		TestProf for: " + testProf);
				
//				if (testProf.getCompId() == 8133) {
//					testProf.setUnits(1);
//					testProf.setModified(true);
//					testProf.setResultCode(ErrorCodeMap.RECORD_FOUND);
//					testDao.setTestProf(testProf);
//					LOGGER.info("		Update testProf success");
//				}
				
				testDao.getTestByTestId(testProf.getCompId());
				TestProc testProc = testDao.getTestProcByTestIdProcTypId(testProf.getCompId(), 38);
				
				if (testProc.getProcId().equals("84132") || (testProc.getTestId() == 10483)) {
					testProc.setProcId("88188");
					testProc.setModified(true);
					testProc.setResultCode(ErrorCodeMap.RECORD_FOUND);
					testDao.setTestProc(testProc);
					LOGGER.info("		Update testProc success:" + testDao.getTestProcByTestIdProcTypId(testProf.getCompId(), 38));
				}
			}
			
		} catch (Exception e) {
			LOGGER.info("Error logging all the Test");
		}
		
	}
	
	public void logTestFac() {
		try {
		
			List<TestFac> testFacs = testDao.getListTestIdHasReferLabAndSingleTest();
			for (TestFac testFac : testFacs) {
				LOGGER.info("              TestFac: "+ testFac);
				facilityDao.getFacByFacId(testFac.getFacId());
				testDao.getTestFacByTestId(testFac.getTestId());
			
				List<TestDt> testDts = testDao.getTestDtByTestId(testFac.getTestId());
				
				for (TestDt testDt : testDts) {
					LOGGER.info("              TestDt: "+ testDt);
				}
			}
			
		} catch (Exception e) {
			LOGGER.info("Error logging all the TestFac");
		}
	}
	
	public void logClnDt() {
		try {
			Cln cln = clientDao.getClnHasClnBillRefLab().get(FIRST_RECORD);
			List<ClnDt> clnDts = clientDao.getClnDtByClnId(cln.getClnId());
			
			for(ClnDt clnDt : clnDts) {
				LOGGER.info("              ClnDt: "+ clnDt);
				if (clnDt.getClnPrimaryFacId() == 0 || clnDt.getClnPrimaryFacId() == 989) {
					LOGGER.info("Start updating ClnDt");
					clnDt.setClnPrimaryFacId(1);
					clnDt.setModified(true);
					clnDt.setResultCode(ErrorCodeMap.RECORD_FOUND);
					clientDao.setClnDt(clnDt);
					LOGGER.info("Update ClnDt success");
				}
			}
		} catch (Exception e) {
			LOGGER.info("Error logging all the ClnDt");
		}
	}
	
	public void logAllFacByFacTypIsFive() {
		try {
			List<Fac> facs = facilityDao.getFacByFacTypId(5);
			
			for (Fac fac: facs) {
				LOGGER.info("				Fac: " + fac);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public List<List<String>> logAllDataByFacTypIsFive() {
		try {
			List<List<String>> datas = testDao.getDataTestHasFacTypIsFive();
			
			for (List<String> daString: datas) {
				
				LOGGER.info("				Fac: " + daString);
			}
			return datas;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void updateOrderingFacId() throws XifinDataAccessException {
		try {
			
			List<Cln> clns = clientDao.getClnHasClnBillRefLab();
			for (Cln cln : clns) {
				LOGGER.info("				Cln: " + cln);
			}
			Cln cln = clns.get(FIRST_RECORD);
			
			if (cln != null && cln.getOrderingFacId() == 989) {
				LOGGER.info("	Starting update ordering fac by cln");
				cln.setOrderingFacId(1);
				cln.setModified(true);
				cln.setResultCode(ErrorCodeMap.RECORD_FOUND);
				clientDao.setCln(cln);
				LOGGER.info("	Updating success ordering fac by cln");
			}
		}catch (Exception e) {
			
		}
	}
	
	public void logAllTestByTestAbbrev(String testAbrrev) {
		try {
			LOGGER.info("------------------------------Get Test By TestAbbrev");
			Test test = testDao.getTestByTestAbbrev(testAbrrev);
			LOGGER.info("------------------------------Get Test_Fac");
			TestFac testFac = testDao.getTestFacByTestId(test.getTestId());
			LOGGER.info("------------------------------Get Test_Dts");
			List<TestDt> testDts = testDao.getTestDtByTestAbbrv(testAbrrev);
			for (TestDt testDt : testDts) {
				LOGGER.info("------------------------------Get Test_Dt" + testDt);
			}
			
		} catch (Exception e) {
			LOGGER.info("------------------------------Error getting Test By TestAbbrev");
		}
	}
}
