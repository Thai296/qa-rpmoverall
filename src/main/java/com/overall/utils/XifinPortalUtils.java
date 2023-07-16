package com.overall.utils;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.rpm.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;

import jodd.typeconverter.Convert;

import com.xifin.qa.dao.rpm.domain.GrpAcl;
import com.overall.mar.menu.TabLinks;
import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPmt.AccnPmt;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnDt.ClnDt;
import com.mbasys.mars.ejb.entity.clnQ.ClnQ;
import com.mbasys.mars.ejb.entity.clnXref.ClnXref;
import com.mbasys.mars.ejb.entity.dep.Dep;
import com.mbasys.mars.ejb.entity.depBatch.DepBatch;
import com.mbasys.mars.ejb.entity.depBatchSeq.DepBatchSeq;
import com.mbasys.mars.ejb.entity.dlyRcpt.DlyRcpt;
import com.mbasys.mars.ejb.entity.npi.Npi;
import com.mbasys.mars.ejb.entity.phys.Phys;
import com.mbasys.mars.ejb.entity.physCredTyp.PhysCredTyp;
import com.mbasys.mars.ejb.entity.physPyrClnLic.PhysPyrClnLic;
import com.mbasys.mars.ejb.entity.physPyrLic.PhysPyrLic;
import com.mbasys.mars.ejb.entity.physXref.PhysXref;
import com.mbasys.mars.ejb.entity.prc.Prc;
import com.mbasys.mars.ejb.entity.prcProc.PrcProc;
import com.mbasys.mars.ejb.entity.prcTest.PrcTest;
import com.mbasys.mars.ejb.entity.grpMember.GrpMember;
import com.mbasys.mars.ejb.entity.grps.Grps;
import com.mbasys.mars.ejb.entity.pt.Pt;
import com.mbasys.mars.ejb.entity.pyrClnExcl.PyrClnExcl;
import com.mbasys.mars.ejb.entity.pyrCntct.PyrCntct;
import com.mbasys.mars.ejb.entity.pyrGrpClnExcl.PyrGrpClnExcl;
import com.mbasys.mars.ejb.entity.pyrGrpDun.PyrGrpDun;
import com.mbasys.mars.ejb.entity.pyrGrpPhysExcl.PyrGrpPhysExcl;
import com.mbasys.mars.ejb.entity.pyrPhysExcl.PyrPhysExcl;
import com.mbasys.mars.ejb.entity.pyrSvc.PyrSvc;
import com.mbasys.mars.ejb.entity.spcmnQ.SpcmnQ;
import com.mbasys.mars.ejb.entity.taxonomy.Taxonomy;
import com.mbasys.mars.ejb.entity.upin.Upin;
import com.mbasys.mars.ejb.entity.userFacAccess.UserFacAccess;
import com.mbasys.mars.persistance.DatabaseMap;
import com.mbasys.mars.persistance.ErrorCodeMap;

public class XifinPortalUtils extends SeleniumBaseTest {
	private static final int MAX_LOOP = 5;
	private static final int MAX_LENGTH_NPI = 10;

	private static final String EMPTY = "";
	private static final String GRP_NAME = "xifin_auto_readonly";
	private static final String GRP_DESCRIPTION = "Xifin Automation Read Only";

	private RemoteWebDriver driver;
	protected Logger logger;

	public XifinPortalUtils(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public XifinPortalUtils(RemoteWebDriver driver, Configuration config){
		this.driver = driver;
		this.clientDao = new ClientDaoImpl(config.getRpmDatabase());
		this.payorDao = new PayorDaoImpl(config.getRpmDatabase());
		this.accessionDao = new AccessionDaoImpl(config.getRpmDatabase());
		this.submissionDao = new SubmissionDaoImpl(config.getRpmDatabase());
		this.zipDao = new ZipDaoImpl(config.getRpmDatabase());
		this.usersDao = new UsersDaoImpl(config.getRpmDatabase());
		this.rpmDao = new RpmDaoImpl(config.getRpmDatabase());
		this.patientDao = new PatientDaoImpl(config.getRpmDatabase());
		this.paymentDao = new PaymentDaoImpl(config.getRpmDatabase());
		this.physicianDao = new PhysicianDaoImpl(config.getRpmDatabase());
		this.prcDao = new PrcDaoImpl(config.getRpmDatabase());
		this.npiDao = new NpiDaoImpl(config.getRpmDatabase());
		this.specimenDao = new SpecimenDaoImpl(config.getRpmDatabase());
		this.databaseSequenceDao = new DataBaseSequenceDaoImpl(config.getRpmDatabase());
		this.upinDao = new UPINDaoImpl(config.getRpmDatabase());
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	/**
	 * Get total Row of Table in Result page and format number if its size > 3
	 * @param pager
	 * @return
	 */
	public String getTotalResultSearch(WebElement pager) {
		String total = pager.getText();
		StringTokenizer strToken = new StringTokenizer(total, " ");
		String numberOfResult = "";
		while (strToken.hasMoreTokens()) {
			numberOfResult = strToken.nextToken();
			if (numberOfResult.equalsIgnoreCase("records")
					|| numberOfResult.isEmpty()) {
				return "0";
			}
		}
		return numberOfResult;
	}

	/**
	 * Get List Column values in tbl
	 * @param tblId
	 * @param col
	 * @return
	 * @throws Exception
	 */
	public List<WebElement> getListColValuesFromTbl(String tblId, int col) throws Exception {
		return driver.findElements(By.xpath(".//*[@id='" + tblId + "']/tbody/tr/td[" + col + "]"));
	}

	public List<String> getValuesInColTable(SeleniumBaseTest b, String tblId, int col, int totalRecords, int maxRecordsOnePg,WebElement buttonTbl) throws Exception {
		List<String> values = new ArrayList<String>();
		List<WebElement> rows = new ArrayList<WebElement>();
		if (totalRecords == 0) {
			return values;
		}

		if (totalRecords <= maxRecordsOnePg) {
			rows = getListColValuesFromTbl(tblId, col);
			for (int i = 1; i < rows.size(); i++) {
				values.add(rows.get(i).getText());
			}
		} else {
			while (totalRecords > maxRecordsOnePg) {
				rows = getListColValuesFromTbl(tblId, col);
				for (int i = 1; i < rows.size(); i++) {
					values.add(rows.get(i).getText());
					totalRecords--;
				}
				b.clickHiddenPageObject(buttonTbl, 0);
			}
			while (totalRecords > 0) {
				rows = getListColValuesFromTbl(tblId, col);
				for (int i = 1; i < rows.size(); i++) {
					values.add(rows.get(i).getText());
					totalRecords--;
				}
			}
		}

		return values;
	}

	public void selectFirstRowOnTable(WebElement table,int number) throws Exception{
		Actions actions = new Actions(driver);
		WebElement e = table.findElement(By.xpath("//tbody/tr["+(number+1)+"]/td[2]//a"));
		if (e != null) {
			actions.click(e).build().perform();
		} else {
			logger.error("        Element " + e + " does not exist.");
		}

	}

	public void switchToFrame(SeleniumBaseTest b) throws Exception {
		TabLinks tabLinks = new TabLinks(driver);
		b.switchToDefaultWinFromFrame();
		b.switchToFrame(tabLinks.contentFrame());
		b.switchToFrame(tabLinks.platformFrame());
	}

	/**
	 * wait until the next page is loaded
	 */
	public void waitForPageLoaded(WebDriverWait wait) {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
			}
		};
		try {
			Thread.sleep(1000);
			wait.until(expectation);
		} catch (Throwable error) {
			Assert.fail("Timeout waiting for Page Load Request to complete.");
		}
	}

	/**
	 * Create a Pt_V2
	 * @return
	 * @throws Exception
	 */
	public Pt createPtV2() throws Exception {
		Pt pt = new Pt();
		int nextSeqId = databaseSequenceDao.getNextSeqIdFromTBLByColName(DatabaseMap.FLD_PT_SEQ_ID, DatabaseMap.TBL_PT_V2);
		pt.setSeqId(nextSeqId);
		pt.setPtStId("CA");
		pt.setResultCode(ErrorCodeMap.NEW_RECORD);
		patientDao.setPt(pt);

		return pt;
	}

	// Cannot create PtPhi because not found PtPhi into ValueObject(Latest version)
	/*public PtPhi createPtPhiByPtSeqId(int ptSeqId) throws Exception {
		
	}*/

	/**
	 * Create an Accession
	 * @param clnId
	 * @return
	 * @throws Exception
	 */
	public Accn createAccn(int clnId) throws Exception {
		Accn accn = null;

		RandomCharacter randomCharacter = new RandomCharacter();
		String accnId = "Auto" + randomCharacter.getRandomNumericString(8);
		Pt pt = patientDao.getRandomPt();

		accn = new Accn();
		accn.setAccnId(accnId);
		accn.setClnId(clnId);
		accn.setPrimClnId(clnId);
		accn.setPtSeqId(pt.getSeqId());
		accn.setPtStaAbbrv(pt.getPtStId());
		accn.setPtFNm(pt.getPtFNm());
		accn.setPtLNm(pt.getPtLNm());
		accn.setPtDob(pt.getPtDob());
		accn.setResultCode(ErrorCodeMap.NEW_RECORD);

		accessionDao.setAccn(accn);

		return accn;
	}

	/**
	 * Create an AccnPmt
	 * @param accnId
	 * @param pmtTypeId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public AccnPmt createAccnPmt(String accnId, int pmtTypeId, int pmtFacId, String userId) throws Exception {
		AccnPmt accnPmt = null;
		int nextPmtSeq = databaseSequenceDao.getNextSeqIdFromTBLByColName(DatabaseMap.FLD_ACCN_PMT_PMT_SEQ, DatabaseMap.TBL_ACCN_PMT);
		if (userId == null || userId == "") {
			userId = usersDao.getRandomUsers().getUserId();
		}
		int pyrId = payorDao.getRandomPyr().getPyrId();
		TimeStamp timeStamp = new TimeStamp();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date pmtDt = new Date(formatter.parse(timeStamp.getCurrentDate("MM/dd/yyyy")).getTime());

		accnPmt = new AccnPmt();
		accnPmt.setPmtSeq(nextPmtSeq);
		accnPmt.setAccnId(accnId);
		accnPmt.setDepId(0);
		accnPmt.setDepBatchId(0);
		accnPmt.setDepBatchSeqId(0);
		accnPmt.setPyrId(pyrId);
		accnPmt.setUserId(userId);
		accnPmt.setPmtTypId(pmtTypeId);
		accnPmt.setPmtDt(pmtDt);
		accnPmt.setBillAmtFromEob(0);
		accnPmt.setPmtFacId(pmtFacId);
		accnPmt.setResultCode(ErrorCodeMap.NEW_RECORD);

		accessionDao.setAccnPmt(accnPmt);

		return accnPmt;
	}

	/**
	 * Create an user_fac_access
	 * @param facId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public UserFacAccess createUserFacAccess(int facId, String userId) throws Exception {
		UserFacAccess userFacAccess = usersDao.getUserFacAccessByUserIdAndFacId(userId, facId);
		if (userFacAccess != null) return userFacAccess;

		int nextSeqId = databaseSequenceDao.getNextSeqIdFromTBLByColName(DatabaseMap.FLD_USER_FAC_ACCESS_SEQ_ID, DatabaseMap.TBL_USER_FAC_ACCESS);

		userFacAccess = new UserFacAccess();
		userFacAccess.setSeqId(nextSeqId);
		userFacAccess.setFacId(facId);
		userFacAccess.setUserId(userId);
		userFacAccess.setResultCode(ErrorCodeMap.NEW_RECORD);
		usersDao.setUserFacAccess(userFacAccess);

		return userFacAccess;
	}

	/**
	 * Create an user_fac_access from an user_fac_access
	 * @param userFacAccess
	 * @throws Exception
	 */
	public void createUserFacAccess(UserFacAccess userFacAccess) throws Exception {
		if (userFacAccess == null) return;
		userFacAccess.setResultCode(ErrorCodeMap.NEW_RECORD);
		usersDao.setUserFacAccess(userFacAccess);
	}

	public void deleteAccnPmt(AccnPmt accnPmt) throws Exception {
		if (accnPmt == null) return;
		accnPmt.setResultCode(ErrorCodeMap.DELETED_RECORD);
		accessionDao.setAccnPmt(accnPmt);
	}

	public void deleteAccn(Accn accn) throws Exception {
		if (accn == null) return;
		accn.setResultCode(ErrorCodeMap.DELETED_RECORD);
		accessionDao.setAccn(accn);
	}

	public void deleteDep(Dep dep) throws Exception {
		if (dep == null) return;
		dep.setResultCode(ErrorCodeMap.DELETED_RECORD);
		paymentDao.setDep(dep);
	}

	public void deleteDepBatch(DepBatch depBatch) throws Exception {
		if (depBatch == null) return;
		depBatch.setResultCode(ErrorCodeMap.DELETED_RECORD);
		paymentDao.setDepBatch(depBatch);
	}

	public void deleteDepBatchSeq(DepBatchSeq depBatchSeq) throws Exception {
		if (depBatchSeq == null) return;
		depBatchSeq.setResultCode(ErrorCodeMap.DELETED_RECORD);
		paymentDao.setDepBatchSeq(depBatchSeq);
	}

	public void deleteDlyReceipt(DlyRcpt dlyRcpt) throws Exception {
		if (dlyRcpt == null) return;
		dlyRcpt.setResultCode(ErrorCodeMap.DELETED_RECORD);
		accessionDao.setDlyRcpt(dlyRcpt);
	}

	public void deleteUserFacAccess(UserFacAccess userFacAccess) throws Exception {
		if (userFacAccess == null) return;
		userFacAccess.setResultCode(ErrorCodeMap.DELETED_RECORD);
		usersDao.setUserFacAccess(userFacAccess);
	}

	public void deletePhysPyrLic(PhysPyrLic physPyrLic) throws Exception {
		if (physPyrLic == null) return;
		physPyrLic.setResultCode(ErrorCodeMap.DELETED_RECORD);
		physicianDao.setPhysPyrLic(physPyrLic);
	}

	public void deletePhysPyrClnLic(PhysPyrClnLic physPyrClnLic) throws Exception {
		if (physPyrClnLic == null) return;
		physPyrClnLic.setResultCode(ErrorCodeMap.DELETED_RECORD);
		physicianDao.setPhysPyrClnLic(physPyrClnLic);
	}

	public void deletePyrGrpPhysExcl(PyrGrpPhysExcl pyrGrpPhysExcl) throws Exception {
		if (pyrGrpPhysExcl == null) return;
		pyrGrpPhysExcl.setResultCode(ErrorCodeMap.DELETED_RECORD);
		payorDao.setPyrGrpPhysExcl(pyrGrpPhysExcl);
	}

	public void deletePyrPhysExcl(PyrPhysExcl pyrPhysExcl) throws Exception {
		if (pyrPhysExcl == null) return;
		pyrPhysExcl.setResultCode(ErrorCodeMap.DELETED_RECORD);
		payorDao.setPyrPhysExcl(pyrPhysExcl);
	}

	public void deletePhysXref(PhysXref physXref) throws Exception {
		if (physXref == null) return;
		physXref.setResultCode(ErrorCodeMap.DELETED_RECORD);
		physicianDao.setPhysXref(physXref);
	}

	/**
	 * verify the help page is displayed correctly
	 * @param url : the main keyword of the URL link
	 * @param notifyMsg: The message to notify
	 * @throws Exception
	 */
	public void verifyHelpPageIsDisplayed(SeleniumBaseTest b, String url, String notifyMsg) throws Exception {
		String mainPage = b.switchToPopupWin();
		String currentUrl = driver.getCurrentUrl();
		if (notifyMsg == null || notifyMsg == "") notifyMsg = "        The help page is displayed correctly.";
		assertTrue(currentUrl.contains(url), notifyMsg);
		driver.close();
		b.switchToParentWin(mainPage);
	}
	
	/**
	 * Analyze the last digit of NPI String
	 * @param npi: max length equals 9 characters
	 * @return
	 * @throws Exception
	 */
	private String analyzeLastDigitNpi(String npi) throws Exception {
		int pos9 = 2 * Convert.toInteger(npi.charAt(8));
		int pos7 = 2 * Convert.toInteger(npi.charAt(6));
		int pos5 = 2 * Convert.toInteger(npi.charAt(4));
		int pos3 = 2 * Convert.toInteger(npi.charAt(2));
		int pos1 = 2 * Convert.toInteger(npi.charAt(0));
		int checkSum = 0;
		checkSum = checkSum + 24;
		int checkSumLen;
		int checkDigit;
		int multipleTen;

		if (String.valueOf(pos1).length() > 1) {
			for (int i = 0; i < String.valueOf(pos1).length(); i++) {
				checkSum = checkSum + Convert.toInteger(String.valueOf(pos1).charAt(i));
			}
		} else {
			checkSum = checkSum + pos1;
		}
		checkSum = checkSum + Convert.toInteger(npi.charAt(1));

		if (String.valueOf(pos3).length() > 1) {
			for (int i = 0; i < String.valueOf(pos3).length(); i++) {
				checkSum = checkSum + Convert.toInteger(String.valueOf(pos3).charAt(i));
			}
		} else {
			checkSum = checkSum + pos3;
		}
		checkSum = checkSum + Convert.toInteger(npi.charAt(3));

		if (String.valueOf(pos5).length() > 1) {
			for (int i = 0; i < String.valueOf(pos5).length(); i++) {
				checkSum = checkSum + Convert.toInteger(String.valueOf(pos5).charAt(i));
			}
		} else {
			checkSum = checkSum + pos5;
		}
		checkSum = checkSum + Convert.toInteger(npi.charAt(5));

		if (String.valueOf(pos7).length() > 1) {
			for (int i = 0; i < String.valueOf(pos7).length(); i++) {
				checkSum = checkSum + Convert.toInteger(String.valueOf(pos7).charAt(i));
			}
		} else {
			checkSum = checkSum + pos7;
		}
		checkSum = checkSum + Convert.toInteger(npi.charAt(7));

		if (String.valueOf(pos9).length() > 1) {
			for (int i = 0; i < String.valueOf(pos9).length(); i++) {
				checkSum = checkSum + Convert.toInteger(String.valueOf(pos9).charAt(i));
			}
		} else {
			checkSum = checkSum + pos9;
		}

		checkSumLen = String.valueOf(checkSum).length();
		if (String.valueOf(checkSum).charAt(checkSumLen - 1) == '0') {
			checkDigit = 0;
		} else {
			multipleTen = checkSum - checkSum % 10 + 10;
			checkDigit = multipleTen - checkSum;
		}

		return String.valueOf(checkDigit);
	}
	/**
	 * Create a NPI
	 * @return
	 * @throws Exception
	 */
	public Npi createNpi() throws Exception {
		Npi npi = new Npi();
		RandomCharacter randomCharacter = new RandomCharacter();
		String randomNumber = EMPTY;
		long npiId;
		int loop = 0;
		do {
			randomNumber = randomCharacter.getRandomNumericString(9);
			randomNumber += this.analyzeLastDigitNpi(randomNumber);
			npiId = Long.parseLong(randomNumber);
			loop++;
		} while (String.valueOf(npiId).length() != MAX_LENGTH_NPI || loop >= MAX_LOOP);

		if (String.valueOf(npiId).length() != MAX_LENGTH_NPI) return null;

		npi.setNpi(npiId);
		npi.setNpiTypId(1);
		npi.setResultCode(ErrorCodeMap.NEW_RECORD);
		npiDao.setNpi(npi);

		return npi;
	}

	/**
	 * Create a Phys
	 * @param npi is NPI object
	 * @return
	 * @throws Exception
	 */
	public Phys createPhys(Npi npi) throws Exception {
		Phys phys = new Phys();
		
		int nextSeqId = databaseSequenceDao.getNextSeqIdFromTBLByColName(DatabaseMap.FLD_PHYS_SEQ_ID, DatabaseMap.TBL_PHYS);
		RandomCharacter randomCharacter = new RandomCharacter();
		String lName = randomCharacter.getRandomAlphaString(5);
		String fName = randomCharacter.getRandomAlphaString(10);
		String add1 = randomCharacter.getRandomAlphaString(10);
		String add2 = randomCharacter.getRandomAlphaString(10);
		String zipCd = zipDao.getRandomZipCodeByConditions(EMPTY).get(0);
		PhysCredTyp physCredTyp = physicianDao.getRandomPhysCredTyp();
		if (npi == null) {
			npi = this.createNpi();
		}
		Upin upin = upinDao.getRandomUpinByRow(10);
		Taxonomy taxonomy = rpmDao.getTaxonomy(null).get(0);
		
		phys.setSeqId(nextSeqId);
		phys.setPhysLname(lName);
		phys.setPhysFname(fName);
		phys.setAddr1(add1);
		phys.setAddr2(add2);
		phys.setCity("LOS ANGELES");
		phys.setStId("CA");
		phys.setZipId(zipCd);
		phys.setCntryId(0);
		phys.setPhysCredId(physCredTyp.getPhysCredTypId());
		phys.setPhysSpecId(0);
		phys.setNpiId(npi.getNpi());
		phys.setUpinId(upin.getUpinId());
		phys.setTaxonomyCd(taxonomy.getTaxonomyCd());
		phys.setIsDisplayAsRenderingPhy(false);
		phys.setOverrideSvcFacId(0);
		phys.setResultCode(ErrorCodeMap.NEW_RECORD);

		physicianDao.setPhys(phys);

		return phys;
	}

	public void deleteNpi(Npi npi) throws Exception {
		if (npi == null) return;
		npi.setResultCode(ErrorCodeMap.DELETED_RECORD);
		npiDao.setNpi(npi);
	}

	public void deletePhys(Phys phys) throws Exception {
		if (phys == null) return;
		phys.setResultCode(ErrorCodeMap.DELETED_RECORD);
		physicianDao.setPhys(phys);
	}

	public boolean checkElementIsAvailable(WebElement element) throws Exception {
		try {
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			logger.error("        Element " + element + " is unavailable. Exception: " + e);
			return false;
		}
		return true;
	}

	private void createGrp() throws Exception {
		Grps grps = new Grps();
		grps.setGrpName(GRP_NAME);
		grps.setDescr(GRP_DESCRIPTION);
		grps.setIsSystem(true);	
		grps.setResultCode(ErrorCodeMap.NEW_RECORD);
		groupDao.setGrps(grps);
	}

	/*private void createGrpAcl(int aclId, String grpName) throws Exception {
		GrpAcl grpAcl = new GrpAcl();
		grpAcl.setAclId(aclId);
		grpAcl.setGrpName(grpName);
		grpAcl.setResultCode(ErrorCodeMap.NEW_RECORD);
		utilDao.setGrpAcl(grpAcl);
	}*/

	/**
	 * set permission for user if any
	 * @param userName
	 * @param resourceName get from AclMap into Persistence
	 * @return
	 * @throws Exception
	 */
	public void setPermissionIfAny(String userName, String resourceName) throws Exception {
		List<Integer> lstAclId = aclsDao.getAclIdByResourceName(resourceName);
		for (Integer aclId : lstAclId) {
			List<GrpMember> lstGrpMember = groupDao.getGrpMemberByUserIdAndAclId(userName, aclId);
			if (lstGrpMember.isEmpty()) {

				// Create new GRP
				Grps grps = groupDao.getGrpsByGrpName(GRP_NAME);
				if (grps == null) {
					try {
						createGrp();
					}catch (Exception e){
						logger.error("Error during create new Grp message:" +e.getMessage());
					}
					grps = groupDao.getGrpsByGrpName(GRP_NAME);
				}
				if (grps == null){
					fail("Unable to create new Grp record to grant permission and continue testing");
				}

				// add new GRP_ACLS
				GrpAcl grpAcl = groupDao.getGrpAclByAclIdAndGrpName(aclId, grps.getGrpName());
				if (grpAcl == null) {
					int row = groupDao.insertGrpAcl(aclId, grps.getGrpName());
					logger.info("Total create GrpAcl record:" +row);
				}

				// Add current user to GRP
				GrpMember grpMember = groupDao.getGrpMemberByUserIdAndGrpName(userName, grps.getGrpName());
				if (grpMember == null) {
					addUserToGrp(userName, grps.getGrpName());
				}
			}
		}
	}

	private void addUserToGrp(String userId, String grpName) throws XifinDataAccessException {
		GrpMember grpMember = new GrpMember();
		grpMember.setGrpName(grpName);
		grpMember.setUserId(userId);
		grpMember.setModified(true);
		grpMember.setResultCode(ErrorCodeMap.NEW_RECORD);
		groupDao.setGrpMember(grpMember);
	}

	public void deletePyrCntct(PyrCntct pyrCntct) throws Exception {
		if (pyrCntct == null) return;
		pyrCntct.setResultCode(ErrorCodeMap.DELETED_RECORD);
		payorDao.setPyrCntct(pyrCntct);
	}

	public void deletePyrGrpDun(PyrGrpDun pyrGrpDun) throws Exception {
		if (pyrGrpDun == null) return;
		pyrGrpDun.setResultCode(ErrorCodeMap.DELETED_RECORD);
		payorDao.setPyrGrpDun(pyrGrpDun);
	}

	public void deleteCln(Cln cln) throws Exception {
		if (cln == null) return;
		cln.setResultCode(ErrorCodeMap.DELETED_RECORD);
		clientDao.setCln(cln);
	}

	public void deleteClnDt(ClnDt clnDt) throws Exception {
		if (clnDt == null) return;
		clnDt.setResultCode(ErrorCodeMap.DELETED_RECORD);
		clientDao.setClnDt(clnDt);
	}

	public void deleteClnXref(ClnXref clnXref) throws Exception {
		if (clnXref == null) return;
		clnXref.setResultCode(ErrorCodeMap.DELETED_RECORD);
		clientDao.setClnXref(clnXref);
	}

	public void deletePyrClnExcl(PyrClnExcl pyrClnExcl) throws Exception {
		if (pyrClnExcl == null) return;
		pyrClnExcl.setResultCode(ErrorCodeMap.DELETED_RECORD);
		payorDao.setPyrClnExcl(pyrClnExcl);
	}

	public void deletePyrGrpClnExcl(PyrGrpClnExcl pyrGrpClnExcl) throws Exception {
		if (pyrGrpClnExcl == null) return;
		pyrGrpClnExcl.setResultCode(ErrorCodeMap.DELETED_RECORD);
		payorDao.setPyrGrpClnExcl(pyrGrpClnExcl);
	}

	public void deletePrcTest(PrcTest prcTest) throws Exception {
		if (prcTest == null) return;
		prcTest.setResultCode(ErrorCodeMap.DELETED_RECORD);
		prcDao.setPrcTest(prcTest);
	}

	public void deletePrc(Prc prc) throws Exception {
		if (prc == null) return;
		prc.setResultCode(ErrorCodeMap.DELETED_RECORD);
		prcDao.setPrc(prc);
	}

	public void deletePrcProc(PrcProc prcProc) throws Exception {
		if (prcProc == null) return;
		prcProc.setResultCode(ErrorCodeMap.DELETED_RECORD);
		prcDao.setPrcProc(prcProc);
	}

	public void deletePyrSvc(PyrSvc pyrSvc) throws XifinDataAccessException {
		if (pyrSvc == null) return;
		pyrSvc.setResultCode(ErrorCodeMap.DELETED_RECORD);
		payorDao.setPyrSvc(pyrSvc);
	}

	public void deleteClnQ(ClnQ clnQ) throws Exception {
		if (clnQ == null) return;
		clnQ.setResultCode(ErrorCodeMap.DELETED_RECORD);
		clientDao.setClnQ(clnQ);
	}

	public void deleteSpcmnQ(SpcmnQ spcmnQ) throws Exception {
		if (spcmnQ == null) return;
		spcmnQ.setResultCode(ErrorCodeMap.DELETED_RECORD);
		specimenDao.setSpcmnQ(spcmnQ);
	}

	public String convertDecimalFormat(int number){
		DecimalFormat Formatter = new DecimalFormat("###,###,###");
		return Formatter.format(number);
	}
}
