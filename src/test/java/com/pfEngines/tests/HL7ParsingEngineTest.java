package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnClnQ.AccnClnQ;
import com.mbasys.mars.ejb.entity.accnCntct.AccnCntct;
import com.mbasys.mars.ejb.entity.accnDiag.AccnDiag;
import com.mbasys.mars.ejb.entity.accnPhys.AccnPhys;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.ejb.entity.hospitalAdmitCheck.HospitalAdmitCheck;
import com.mbasys.mars.ejb.entity.phys.Phys;
import com.mbasys.mars.ejb.entity.qOe.QOe;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.utils.HL7ParsingEngineUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import domain.engines.hl7parsing.mgl.*;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HL7ParsingEngineTest extends SeleniumBaseTest {
	private RandomCharacter randomCharacter;
	//private EngineUtils engineUtils;
	//private ImportEngineUtils importEngineUtils;
	private TimeStamp timeStamp;
	private FileManipulation fileManipulation;
	private XifinAdminUtils xifinAdminUtils;	
	private HL7ParsingEngineUtils hl7ParsingEngineUtils;
    private File upOne = new File(System.getProperty("user.dir")).getParentFile();
    private static final SimpleDateFormat DATE_FORMAT_MMDDYYYY = new SimpleDateFormat("MMddyyyy");
    private static final SimpleDateFormat DATE_FORMAT_MMDDYYYY_WITH_SLASH = new SimpleDateFormat("MM/dd/yyyy");
    protected static long QUEUE_POLL_TIME = TimeUnit.SECONDS.toMillis(4);
    protected static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(1);

    private final String testDb = null;

	@Test(priority = 1, description="GenericV231-Add accn with all HL7 fields")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_482(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_482 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; Update TASK_TYP.PK_TASK_TYP_ID = 1116, Update TASK_TYP.PK_TASK_TYP_ID = 1103,Update TASK_TYP.PK_TASK_TYP_ID = 1165 Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);

		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 3 Actions: - Create a GenericV231 HL7 file with all fields");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "genericv231-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId = "QAAUTOHL7" + currDtTime;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        List<String>pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId);         
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        List<String>in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M"); //isInvalidPyr = false;
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1GenericV231("6197774568", 40, "M", "C", "987456789", "8586217459");
        String dos = timeStamp.getCurrentDate("YYYYMMdd");
        List<String>ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "1", "Y", "Y", "1", "71010", dos); //isProfTest = false; "D" - Debit; "1" - unit
        List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        List<String>zxa = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false); //pyr_abbrev, isFreeText = false;
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        String content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), 
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE", "CLIENT", "FACILITY"), Arrays.asList(zxa), finalReportDt, true);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
		
		logger.info("*** Step 4 Actions: - Insert test data in TEST_Q in DB");				
		String testId = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(ft1.get(4), testDb).get(0);		
		daoManagerPlatform.insertTestQ(testId, "1", "3", testDb);		
		
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");
		
        logger.info("*** Step 5 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		File fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");  
        		
        logger.info("*** Step 5 Expected Results: - Verify that data are saved to the corresponding tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");

        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN properly");       
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");     
        
        String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn.get(8),pid.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn.get(3),pid.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(16),"        Pt SSN is saved into ACCN.");
        
        String clnAbbrev = pv1.get(3);
        String clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        String clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");
        
        //String ptLocation = pv1.get(0);        
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created        
        String pscId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1.get(5), testDb).get(4);
        assertEquals(accn.get(49), pscId, "        PSC should be saved into ACCN."); 
        assertEquals(accn.get(56), pv1.get(6), "        Admission Source should be saved into ACCN."); 
        assertEquals(accn.get(32), pv1.get(7), "        Patient Type should be saved into ACCN.");
        
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date actDate = df1.parse(accn.get(18));       
        DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate), ft1.get(13), "        Receipt Date should be saved into ACCN."); 
        
        assertEquals(accn.get(34), ft1.get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn.get(33), ft1.get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn.get(36), ft1.get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn.get(35), "1", "        roundTrip should be saved into ACCN.");
        
        assertEquals(accn.get(48), zxa.get(5), "        Phlebotomist ID should be saved into ACCN.");
        
        String onSetDtStrInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn.get(61));
        assertEquals(onSetDtStrInDB, zxa.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn.get(62), zxa.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn.get(59), zxa.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn.get(19), zxa.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn.get(42), "1", "        MSP Received Flag should be saved into ACCN.");
        assertEquals(accn.get(63), zxa.get(15), "        Accident State should be saved into ACCN.");        
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_V2 properly");
        String epi = pid.get(0);
        System.out.println("epi = " + epi);//debug info
        List<String> ptV2List = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi, testDb);
        assertTrue(ptV2List.size()>0,"        Patient EPI " + epi + " should be saved into PT_V2.");  
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_PYR properly"); 
        //accn_pyr
        List<List<String>> accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr.get(0).get(7),in1.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(13),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr.get(0).get(18),in1.get(14),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr.get(0).get(16),in1.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(25),in1.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(20), "1","        INS_SEX is saved into Db"); //1 = M (Male)
        assertEquals(accnPyr.get(0).get(26),in1.get(23),"        SUBS_ID is saved into Db");
        //assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created
        
        //String eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        //assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created
        
        String insuredDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(19));
        assertEquals(insuredDOBInDB, in1.get(12),"        INS_DOB is saved into Db");
        
        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr.get(1).get(7).toUpperCase(),gt1.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");       
        assertEquals(accnPyr.get(1).get(8).toUpperCase(),gt1.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(9).toUpperCase(),gt1.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(18).toUpperCase(),gt1.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");
        
        String guarantorDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(1).get(19));
        assertEquals(guarantorDOBInDB, gt1.get(8),"        Guarantor's DOB is saved into Db");
        
        assertEquals(accnPyr.get(1).get(20), "1","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr.get(1).get(22), gt1.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr.get(1).get(27), gt1.get(7),"        Guarantor's Home Phone is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(11), gt1.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr.get(1).get(12), gt1.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(23), gt1.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr.get(1).get(24), gt1.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr.get(1).get(28), gt1.get(19),"        Employer's Phone number is saved into Db");        
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST properly"); 
        //accn_test
        List<String> accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");
        assertEquals(accnTest.get(0),ft1.get(4),"        Test is saved into Db");
        
        ArrayList<String> accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1.get(4), testDb);        
        String accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(6),ft1.get(19),"        Mod1 is saved into Db");
        
        assertEquals(accnTestList.get(3),ft1.get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1.get(5),"        ALT_TEST_NAME is saved into Db");        
        assertEquals(accnTestList.get(13),ft1.get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),ft1.get(0),"        LIS Trace Id is saved into Db");
        //assertEquals(accnTestList.get(4), "1", "        Performing Facility is saved into Db"); //It works only for the tests that has only one test facility
        
        assertEquals(accnTestList.get(7),zxt.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(8), "1","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList.get(9), "1","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList.get(10), zxt.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList.get(11), zxt.get(5),"        B_RENAL is saved into Db");               
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_DAIG properly"); 
        //accn_daig
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "1" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
                
        //test level diag
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "2" ,testDb);
        assertEquals(accnDiag.get(2), ft1.get(11),"        Test level Diag Code " + ft1.get(11) + " should be saved into ACCN_DIAG.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CLN_Q properly"); 
        //accn_cln_q        
        List<String> accnClnQ = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId,testDb); 
        assertTrue(accnClnQ.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ.get(0), zxa.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa.get(0) + " should be saved.");
        assertEquals(accnClnQ.get(1), zxa.get(4),"        ACCN_CLN_Q.RESPNS " + zxa.get(4) + " should be saved.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CNTCT properly"); 
        //accn_cntct
        List<String> accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is null", testDb);
        assertTrue(accnCntct.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct.get(0), zxa.get(3),"        CNTCT_INFO " + zxa.get(3) + " should be saved into ACCN_CNTCT.");
       
        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is not null", testDb);
        assertEquals(accnCntct.get(3), zxa.get(11),"       " + zxa.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CLINTRL properly"); 
        //accn_clintrl
        List<String> accnClintrl = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId,testDb);
        assertTrue(accnClintrl.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl.get(0),zxa.get(4),"        CLINTRL_ENCNTR " + zxa.get(4) + " should be saved into ACCN_CLINTRL.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_DAIL properly"); 
        //accn_dail
        List<String> accnDial = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId,testDb);
        assertTrue(accnDial.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial.get(1), zxa.get(6),"        FK_DIAL_TYP_ID " + zxa.get(6) + " should be saved into ACCN_DIAL.");        
        
        String firstDailDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial.get(3));
        assertEquals(firstDailDtInDB, zxa.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL.");       
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST_Q properly"); 
        //accn_test_q
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_PYR_V2 properly"); 
        //pt_pyr_v2
        String ptSeqId = accn.get(43);
        String ptPyrEffDt = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId, "1", testDb).get(28);
        String ptPyrEffDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt);        
        assertEquals(ptPyrEffDtInDB, in1.get(9), "        EFF_DT should be saved into PT_PYR_V2.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_CLN_LNK properly");
        //pt_cln_lnk
        String ptClnIdInDB = daoManagerPlatform.getPtClnLnkInfoFromByPtSeqIdClnId(ptSeqId, clnIdInDB, testDb).get(1);
        assertEquals(ptClnIdInDB, pid.get(0), "        SPECIFIC_PT_ID should be saved into PT_CLN_LNK.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_FAC_LNK properly");     
        //pt_fac_lnk
        String ptFacIdInDB = daoManagerXifinRpm.getPtFacLnkInfoFromPTFACLNKByPtSeqId(ptSeqId, testDb).get(3);
        assertEquals(ptFacIdInDB, pid.get(0), "        SPECIFIC_PT_ID should be saved into PT_FAC_LNK.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_PHYS properly");        
        //accn_phys
        String renderingPhysSeqIdInDB = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId, accnTestSeqId, testDb).get(0);        
        String renderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1.get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB, renderingPhysSeqId, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");
        
        String referingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(4), testDb).get(0);
        String referingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB, referingPhysSeqId, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");       
        
        String orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(2), testDb).get(0);
        String orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS."); 
        		
        logger.info("*** Step 7 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.deleteAccnTestQByAccnId(accnId, testDb);
        daoManagerPlatform.deleteTestQByTestId(testId, testDb);
        
		logger.info("*** Step 8 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
	}

	/*
	@Test(priority = 1, description="GenericV231-Add accn with invalid payor, SS#85 = 2, SS#1822 = 1")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_489(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_489 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 2, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1, 1822 = 1; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 2","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1822", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
		
		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache(this, email, password, xapEnv, false);
		
		logger.info("*** Step 3 Actions: - Create a GenericV231 HL7 file with all fields and Invalid Payor ID");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "genericv231-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);		
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId = "QAAUTOHL7" + currDtTime;       
        List<String>pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId); 
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1GenericV231();
        List<String>in1 = hl7ParsingEngineUtils.getListIN1GenericV231(true); //isInvalidPyr = true;
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1GenericV231();
        List<String>ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "1"); //isProfTest = false; "D" - Debit; "1" - unit
        List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231();
        List<String>zxa = hl7ParsingEngineUtils.getListZXAGenericV231();
        String pyrAbbrev = daoManagerPlatform.getThirdPartyPyrFromPYR(testDb);
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(pyrAbbrev, false); //pyr_abbrev, isFreeText = false;
        //List<String>dg1 = Arrays.asList("");
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        String content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), 
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE"), zxa, finalReportDt);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
		
		logger.info("*** Step 4 Actions: - Insert test data in TEST_Q in DB");				
		String testId = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(ft1.get(4), testDb).get(0);		
		daoManagerPlatform.insertTestQ(testId, "1", "3", testDb);		
		
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");
		
        logger.info("*** Step 5 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		File fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");  
        		
        logger.info("*** Step 5 Expected Results: - Verify that data are saved to the corresponding tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");       
        
        logger.info("*** Step 6 Actions: - Run PF-OE Posting Engine");
		xifinAdminUtils.runPFEngine(this, email, password, xapEnv, eType2, engConfigDB, true);
		Thread.sleep(5000);	
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN properly");       
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");     
        
        String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn.get(8),pid.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn.get(3),pid.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(16),"        Pt SSN is saved into ACCN.");
        
        String clnAbbrev = pv1.get(3);
        String clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        String clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");
        
        //String ptLocation = pv1.get(0);        
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created        
        String pscId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1.get(5), testDb).get(4);
        assertEquals(accn.get(49), pscId, "        PSC should be saved into ACCN."); 
        assertEquals(accn.get(56), pv1.get(6), "        Admission Source should be saved into ACCN."); 
        assertEquals(accn.get(32), pv1.get(7), "        Patient Type should be saved into ACCN.");
        
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date actDate = df1.parse(accn.get(18));       
        DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate), ft1.get(13), "        Receipt Date should be saved into ACCN."); 
        
        assertEquals(accn.get(34), ft1.get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn.get(33), ft1.get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn.get(36), ft1.get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn.get(35), "1", "        roundTrip should be saved into ACCN.");
        
        assertEquals(accn.get(48), zxa.get(5), "        Phlebotomist ID should be saved into ACCN.");
        
        String onSetDtStrInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn.get(61));
        assertEquals(onSetDtStrInDB, zxa.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn.get(62), zxa.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn.get(59), zxa.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn.get(19), zxa.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn.get(42), "1", "        MSP Received Flag should be saved into ACCN.");
        assertEquals(accn.get(63), zxa.get(15), "        Accident State should be saved into ACCN.");        
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_V2 properly");
        String epi = pid.get(0);
        System.out.println("epi = " + epi);//debug info
        List<String> ptV2List = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi, testDb);
        assertTrue(ptV2List.size()>0,"        Patient EPI " + epi + " should be saved into PT_V2.");  
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_PYR properly"); 
        //accn_pyr
        List<List<String>> accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr.get(0).get(7),in1.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(13),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr.get(0).get(18),in1.get(14),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr.get(0).get(16),in1.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(25),in1.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(20), "1","        INS_SEX is saved into Db"); //1 = M (Male)
        assertEquals(accnPyr.get(0).get(26),in1.get(23),"        SUBS_ID is saved into Db");
        //assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created
        
        //String eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        //assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created
        
        String insuredDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(19));
        assertEquals(insuredDOBInDB, in1.get(12),"        INS_DOB is saved into Db");
        
        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr.get(1).get(7).toUpperCase(),gt1.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");       
        assertEquals(accnPyr.get(1).get(8).toUpperCase(),gt1.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(9).toUpperCase(),gt1.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(18).toUpperCase(),gt1.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");
        
        String guarantorDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(1).get(19));
        assertEquals(guarantorDOBInDB, gt1.get(8),"        Guarantor's DOB is saved into Db");
        
        assertEquals(accnPyr.get(1).get(20), "1","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr.get(1).get(22), gt1.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr.get(1).get(27), gt1.get(7),"        Guarantor's Home Phone is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(11), gt1.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr.get(1).get(12), gt1.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(23), gt1.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr.get(1).get(24), gt1.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr.get(1).get(28), gt1.get(19),"        Employer's Phone number is saved into Db");        
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST properly"); 
        //accn_test
        List<String> accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");
        assertEquals(accnTest.get(0),ft1.get(4),"        Test is saved into Db");
        
        ArrayList<String> accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1.get(4), testDb);        
        String accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(6),ft1.get(19),"        Mod1 is saved into Db");
        
        assertEquals(accnTestList.get(3),ft1.get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1.get(5),"        ALT_TEST_NAME is saved into Db");        
        assertEquals(accnTestList.get(13),ft1.get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),ft1.get(0),"        LIS Trace Id is saved into Db");
        assertEquals(accnTestList.get(4),"1","        Performing Facility is saved into Db");
        
        assertEquals(accnTestList.get(7),zxt.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(8), "1","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList.get(9), "1","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList.get(10), zxt.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList.get(11), zxt.get(5),"        B_RENAL is saved into Db");               
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_DAIG properly"); 
        //accn_daig
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "1" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
                
        //test level diag
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "2" ,testDb);
        assertEquals(accnDiag.get(2), ft1.get(11),"        Test level Diag Code " + ft1.get(11) + " should be saved into ACCN_DIAG.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CLN_Q properly"); 
        //accn_cln_q        
        List<String> accnClnQ = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId,testDb); 
        assertTrue(accnClnQ.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ.get(0), zxa.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa.get(0) + " should be saved.");
        assertEquals(accnClnQ.get(1), zxa.get(4),"        ACCN_CLN_Q.RESPNS " + zxa.get(4) + " should be saved.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CNTCT properly"); 
        //accn_cntct
        List<String> accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is null", testDb);
        assertTrue(accnCntct.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct.get(0), zxa.get(3),"        CNTCT_INFO " + zxa.get(3) + " should be saved into ACCN_CNTCT.");
       
        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is not null", testDb);
        assertEquals(accnCntct.get(3), zxa.get(11),"       " + zxa.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CLINTRL properly"); 
        //accn_clintrl
        List<String> accnClintrl = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId,testDb);
        assertTrue(accnClintrl.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl.get(0),zxa.get(4),"        CLINTRL_ENCNTR " + zxa.get(4) + " should be saved into ACCN_CLINTRL.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_DAIL properly"); 
        //accn_dail
        List<String> accnDial = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId,testDb);
        assertTrue(accnDial.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial.get(1), zxa.get(6),"        FK_DIAL_TYP_ID " + zxa.get(6) + " should be saved into ACCN_DIAL.");        
        
        String firstDailDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial.get(3));
        assertEquals(firstDailDtInDB, zxa.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL.");       
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST_Q properly");      
        //accn_test_q
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_PYR_V2 properly"); 
        //pt_pyr_v2
        String ptSeqId = accn.get(43);
        String ptPyrEffDt = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId, "1", testDb).get(28);
        String ptPyrEffDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt);        
        assertEquals(ptPyrEffDtInDB, in1.get(9), "        EFF_DT should be saved into PT_PYR_V2.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_CLN_LNK properly");
        //pt_cln_lnk
        String ptClnIdInDB = daoManagerPlatform.getPtClnLnkInfoFromByPtSeqIdClnId(ptSeqId, clnIdInDB, testDb).get(1);
        assertEquals(ptClnIdInDB, pid.get(0), "        SPECIFIC_PT_ID should be saved into PT_CLN_LNK.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_FAC_LNK properly");     
        //pt_fac_lnk
        String ptFacIdInDB = daoManagerXifinRpm.getPtFacLnkInfoFromPTFACLNKByPtSeqId(ptSeqId, testDb).get(3);
        assertEquals(ptFacIdInDB, pid.get(0), "        SPECIFIC_PT_ID should be saved into PT_FAC_LNK.");       
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_PHYS properly");        
        //accn_phys
        String renderingPhysSeqIdInDB = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId, accnTestSeqId, testDb).get(0);        
        String renderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1.get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB, renderingPhysSeqId, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");
        
        String referingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(4), testDb).get(0);
        String referingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB, referingPhysSeqId, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");       
        
        String orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(2), testDb).get(0);
        String orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS.");      
        		
        logger.info("*** Step 7 Actions: - Clear test data");
        //fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1822", testDb);
        daoManagerPlatform.deleteAccnTestQByAccnId(accnId, testDb);
        daoManagerPlatform.deleteTestQByTestId(testId, testDb);
        
		logger.info("*** Step 8 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache(this, email, password, xapEnv, true);
	}	
	*/
	
	@Test(priority = 1, description="GenericV231-Add and Update accn with all HL7 fields")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_483(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_483 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);

		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 3 Actions: - Create a GenericV231 HL7 file with all fields");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "genericv231-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId = "QAAUTOHL7" + currDtTime;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        //accnId, ptSex, years, ptHomePhone, ptWorkPhone, ptMaritalStatus, String ptSSN, String ptId
        List<String>pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId);       
        //phleFacId, admSrcId
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        //isInvalidPyr, insRelTyp, eligStatus, insSex
        List<String>in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M"); 
        //guarantorHomePhone, years, guarantorSex, guarantorRelshpTyp, guarantorSSN, employerPhone
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1GenericV231("6197774568", 40, "M", "C", "987456789", "8586217459");
        //isProfTest, transTyp, unit, abnReceivedFlag, isRoundTrip, testFacId, specifiedTestAbbrev
        String dos = timeStamp.getCurrentDate("YYYYMMdd");
        List<String>ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "1", "Y", "Y", "1", "71010", dos); 
        //testSpecificQuestion, abnReceivedFlag, renalFlag, payorToBill
        List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        //mspReceivedFlag
        List<String>zxa = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        //pyr_abbrev, isFreeText
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false);
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        String content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), 
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE"), Arrays.asList(zxa), finalReportDt, true);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
		
		logger.info("*** Step 4 Actions: - Insert test data in TEST_Q in DB");				
		String testId = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(ft1.get(4), testDb).get(0);		
		daoManagerPlatform.insertTestQ(testId, "1", "3", testDb);		
		
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");	
		
        logger.info("*** Step 5 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		File fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");  
        		
        logger.info("*** Step 5 Expected Results: - Verify that data are saved to the corresponding tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN properly");       
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");     
        
        String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn.get(8),pid.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn.get(3),pid.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(16),"        Pt SSN is saved into ACCN.");
        
        String clnAbbrev = pv1.get(3);
        String clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        String clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");
        
        //String ptLocation = pv1.get(0);        
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created        
        String pscId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1.get(5), testDb).get(4);
        assertEquals(accn.get(49), pscId, "        PSC should be saved into ACCN."); 
        assertEquals(accn.get(56), pv1.get(6), "        Admission Source should be saved into ACCN."); 
        assertEquals(accn.get(32), pv1.get(7), "        Patient Type should be saved into ACCN.");
        
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date actDate = df1.parse(accn.get(18));       
        DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate), ft1.get(13), "        Receipt Date should be saved into ACCN."); 
        
        assertEquals(accn.get(34), ft1.get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn.get(33), ft1.get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn.get(36), ft1.get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn.get(35), "1", "        roundTrip should be saved into ACCN.");
        
        assertEquals(accn.get(48), zxa.get(5), "        Phlebotomist ID should be saved into ACCN.");
        
        String onSetDtStrInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn.get(61));
        assertEquals(onSetDtStrInDB, zxa.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn.get(62), zxa.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn.get(59), zxa.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn.get(19), zxa.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn.get(42), "1", "        MSP Received Flag should be saved into ACCN.");
        assertEquals(accn.get(63), zxa.get(15), "        Accident State should be saved into ACCN.");        
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_V2 properly");
        String epi = pid.get(0);
        System.out.println("epi = " + epi);//debug info
        List<String> ptV2List = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi, testDb);
        assertTrue(ptV2List.size()>0,"        Patient EPI " + epi + " should be saved into PT_V2.");  
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_PYR properly"); 
        //accn_pyr
        List<List<String>> accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr.get(0).get(7),in1.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(13),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr.get(0).get(18),in1.get(14),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr.get(0).get(16),in1.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(25),in1.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(20), "1","        INS_SEX is saved into Db"); //1 = M (Male)
        assertEquals(accnPyr.get(0).get(26),in1.get(23),"        SUBS_ID is saved into Db");
        //assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created
        
        //String eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        //assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created
        
        String insuredDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(19));
        assertEquals(insuredDOBInDB, in1.get(12),"        INS_DOB is saved into Db");
        
        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr.get(1).get(7).toUpperCase(),gt1.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");       
        assertEquals(accnPyr.get(1).get(8).toUpperCase(),gt1.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(9).toUpperCase(),gt1.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(18).toUpperCase(),gt1.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");
        
        String guarantorDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(1).get(19));
        assertEquals(guarantorDOBInDB, gt1.get(8),"        Guarantor's DOB is saved into Db");
        
        assertEquals(accnPyr.get(1).get(20), "1","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr.get(1).get(22), gt1.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr.get(1).get(27), gt1.get(7),"        Guarantor's Home Phone is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(11), gt1.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr.get(1).get(12), gt1.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(23), gt1.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr.get(1).get(24), gt1.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr.get(1).get(28), gt1.get(19),"        Employer's Phone number is saved into Db");        
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST properly"); 
        //accn_test
        List<String> accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");
        assertEquals(accnTest.get(0),ft1.get(4),"        Test is saved into Db");
        
        ArrayList<String> accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1.get(4), testDb);        
        String accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(6),ft1.get(19),"        Mod1 is saved into Db");
        
        assertEquals(accnTestList.get(3),ft1.get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1.get(5),"        ALT_TEST_NAME is saved into Db");        
        assertEquals(accnTestList.get(13),ft1.get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),ft1.get(0),"        LIS Trace Id is saved into Db");
        //assertEquals(accnTestList.get(4),"1","        Performing Facility is saved into Db");        
        
        assertEquals(accnTestList.get(7),zxt.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(8), "1","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList.get(9), "1","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList.get(10), zxt.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList.get(11), zxt.get(5),"        B_RENAL is saved into Db");               
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_DAIG properly"); 
        //accn_daig
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "1" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");

        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CLN_Q properly"); 
        //accn_cln_q        
        List<String> accnClnQ = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId,testDb); 
        assertTrue(accnClnQ.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ.get(0), zxa.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa.get(0) + " should be saved.");
        assertEquals(accnClnQ.get(1), zxa.get(4),"        ACCN_CLN_Q.RESPNS " + zxa.get(4) + " should be saved.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CNTCT properly"); 
        //accn_cntct
        List<String> accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is null", testDb);
        assertTrue(accnCntct.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct.get(0), zxa.get(3),"        CNTCT_INFO " + zxa.get(3) + " should be saved into ACCN_CNTCT.");
       
        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is not null", testDb);
        assertEquals(accnCntct.get(3), zxa.get(11),"       " + zxa.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CLINTRL properly"); 
        //accn_clintrl
        List<String> accnClintrl = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId,testDb);
        assertTrue(accnClintrl.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl.get(0),zxa.get(4),"        CLINTRL_ENCNTR " + zxa.get(4) + " should be saved into ACCN_CLINTRL.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_DAIL properly"); 
        //accn_dail
        List<String> accnDial = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId,testDb);
        assertTrue(accnDial.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial.get(1), zxa.get(6),"        FK_DIAL_TYP_ID " + zxa.get(6) + " should be saved into ACCN_DIAL.");        
        
        String firstDailDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial.get(3));
        assertEquals(firstDailDtInDB, zxa.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL.");       
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST_Q properly"); 
        //accn_test_q
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_PYR_V2 properly"); 
        //pt_pyr_v2
        String ptSeqId = accn.get(43);
        String ptPyrEffDt = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId, "1", testDb).get(28);
        String ptPyrEffDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt);        
        assertEquals(ptPyrEffDtInDB, in1.get(9), "        EFF_DT should be saved into PT_PYR_V2.");

        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_PHYS properly");        
        //accn_phys
        String renderingPhysSeqIdInDB = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId, accnTestSeqId, testDb).get(0);        
        String renderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1.get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB, renderingPhysSeqId, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");
        
        String referingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(4), testDb).get(0);
        String referingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB, referingPhysSeqId, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");       
        
        String orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(2), testDb).get(0);
        String orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS."); 
               
        logger.info("*** Step 7 Actions: - Create a new GenericV231 HL7 file with the same Accession ID and Patient ID and updated values in all fields");
		currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = "genericv231-" + currDtTime +"-update.hl7";
        
        //accnId, ptSex, years, ptHomePhone, ptWorkPhone, ptMaritalStatus, String ptSSN, String ptId
        pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "F", 16, "9516578854", "7607353125", "S", "987654321", ptId);     
        //phleFacId, admSrcId
        pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("WWW", "3");
        //isInvalidPyr, insRelTyp, eligStatus, insSex
        in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "N", "F");
        //guarantorHomePhone, years, guarantorSex, guarantorRelshpTyp, guarantorSSN, employerPhone
        gt1 = hl7ParsingEngineUtils.getListGT1GenericV231("9517774568", 38, "F", "C", "127456789", "6196217459");
        //isProfTest, transTyp, unit, abnReceivedFlag, isRoundTrip, testFacId, specifiedTestAbbrev
        ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "2", "N", "N", "1", "BS", dos); //"D" - Debit
        //testSpecificQuestion, abnReceivedFlag, renalFlag, payorToBill
        zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "N", "N", "");
        //mspReceivedFlag
        zxa = hl7ParsingEngineUtils.getListZXAGenericV231("N");
        //pyr_abbrev, isFreeText = false
        dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false); 
        finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), 
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE"), Arrays.asList(zxa), finalReportDt, true);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 7 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
		
		logger.info("*** Step 8 Actions: - Insert test data in TEST_Q in DB");				
		testId = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(ft1.get(4), testDb).get(0);		
		daoManagerPlatform.insertTestQ(testId, "1", "3", testDb);		
		
	    logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");
		
        logger.info("*** Step 9 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");  
        		
        logger.info("*** Step 9 Expected Results: - Verify that data are saved to the corresponding tables properly");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN properly");       
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");     
        
        ptDOB = hl7ParsingEngineUtils.geNewDate(16, "yyyy-MM-dd");
        //accn
        accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn.get(8),pid.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn.get(3),pid.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"2","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(16),"        Pt SSN is saved into ACCN.");
        
        clnAbbrev = pv1.get(3);
        clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");
        
        //String ptLocation = pv1.get(0);        
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created        
        pscId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1.get(5), testDb).get(4);
        assertEquals(accn.get(49), pscId, "        PSC should be saved into ACCN."); 
        //////////////assertEquals(accn.get(56), pv1.get(6), "        Admission Source should be saved into ACCN."); //Mars also not updating the accn.FK_ADMISSION_SRC_ID to '3'!
        assertEquals(accn.get(32), pv1.get(7), "        Patient Type should be saved into ACCN.");
        
        df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        actDate = df1.parse(accn.get(18));       
        df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate), ft1.get(13), "        Receipt Date should be saved into ACCN."); 
        
        assertEquals(accn.get(34), ft1.get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn.get(33), ft1.get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn.get(36), ft1.get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn.get(35), "1", "        roundTrip should be saved into ACCN."); //Mars also not updating the accn.B_ROUND_TRIP to '0'!
        
        assertEquals(accn.get(48), zxa.get(5), "        Phlebotomist ID should be saved into ACCN.");
        
        onSetDtStrInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn.get(61));
        assertEquals(onSetDtStrInDB, zxa.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn.get(62), zxa.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn.get(59), zxa.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn.get(19), zxa.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn.get(42), "1", "        MSP Received Flag should be saved into ACCN."); //Mars also not updating the accn.B_MSP_FORM to '0'!
        assertEquals(accn.get(63), zxa.get(15), "        Accident State should be saved into ACCN.");        
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to PT_V2 properly");
        epi = pid.get(0);
        System.out.println("epi = " + epi);//debug info
        ptV2List = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi, testDb);
        assertTrue(ptV2List.size()>0,"        Patient EPI " + epi + " should be saved into PT_V2.");  
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_PYR properly"); 
        //accn_pyr
        accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr.get(0).get(7),in1.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(13),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr.get(0).get(18),in1.get(14),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr.get(0).get(16),in1.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(25),in1.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(20), "2","        INS_SEX is saved into Db"); //2 = F (Female)
        assertEquals(accnPyr.get(0).get(26),in1.get(23),"        SUBS_ID is saved into Db");
        //assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created
        
        //String eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        //assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created
        
        insuredDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(19));
        assertEquals(insuredDOBInDB, in1.get(12),"        INS_DOB is saved into Db");
        
        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr.get(1).get(7).toUpperCase(),gt1.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");       
        assertEquals(accnPyr.get(1).get(8).toUpperCase(),gt1.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(9).toUpperCase(),gt1.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(18).toUpperCase(),gt1.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");
        
        guarantorDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(1).get(19));
        assertEquals(guarantorDOBInDB, gt1.get(8),"        Guarantor's DOB is saved into Db");
        
        assertEquals(accnPyr.get(1).get(20), "2","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr.get(1).get(22), gt1.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr.get(1).get(27), gt1.get(7),"        Guarantor's Home Phone is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(11), gt1.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr.get(1).get(12), gt1.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(23), gt1.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr.get(1).get(24), gt1.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr.get(1).get(28), gt1.get(19),"        Employer's Phone number is saved into Db");        
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_TEST properly"); 
        //accn_test
        accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");
        assertEquals(accnTest.get(0),ft1.get(4),"        Test is saved into Db");
        
        accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1.get(4), testDb);        
        accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(6),ft1.get(19),"        Mod1 is saved into Db");
        
        assertEquals(accnTestList.get(3),ft1.get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1.get(5),"        ALT_TEST_NAME is saved into Db");        
        assertEquals(accnTestList.get(13),ft1.get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),ft1.get(0),"        LIS Trace Id is saved into Db");       
        //assertEquals(accnTestList.get(4),"1","        Performing Facility is saved into Db");
        
        assertEquals(accnTestList.get(7),zxt.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(8), "0","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList.get(9), "0","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList.get(10), zxt.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList.get(11), zxt.get(5),"        B_RENAL is saved into Db");               
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_DAIG properly"); 
        //accn_daig
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "3" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
                
        //test level diag
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "4" ,testDb);
        assertEquals(accnDiag.get(2), ft1.get(11),"        Test level Diag Code " + ft1.get(11) + " should be saved into ACCN_DIAG.");
                
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_CLN_Q properly"); 
        //accn_cln_q        
        accnClnQ = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId,testDb); 
        assertTrue(accnClnQ.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ.get(0), zxa.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa.get(0) + " should be saved.");
        assertEquals(accnClnQ.get(1), zxa.get(4),"        ACCN_CLN_Q.RESPNS " + zxa.get(4) + " should be saved.");
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_CNTCT properly"); 
        //accn_cntct
        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is null order by pk_cntct_seq desc", testDb);
        assertTrue(accnCntct.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct.get(0), zxa.get(3),"        CNTCT_INFO " + zxa.get(3) + " should be saved into ACCN_CNTCT.");
       
        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is not null order by pk_cntct_seq desc", testDb);
        assertEquals(accnCntct.get(3), zxa.get(11),"       " + zxa.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");
                
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_CLINTRL properly"); 
        //accn_clintrl
        accnClintrl = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId,testDb);
        assertTrue(accnClintrl.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl.get(0),zxa.get(4),"        CLINTRL_ENCNTR " + zxa.get(4) + " should be saved into ACCN_CLINTRL.");
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_DAIL properly"); 
        //accn_dail
        accnDial = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId,testDb);
        assertTrue(accnDial.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial.get(1), zxa.get(6),"        FK_DIAL_TYP_ID " + zxa.get(6) + " should be saved into ACCN_DIAL.");        
        
        firstDailDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial.get(3));
        assertEquals(firstDailDtInDB, zxa.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL.");       
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_TEST_Q properly"); 
        //accn_test_q
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to PT_PYR_V2 properly"); 
        //pt_pyr_v2
        ptSeqId = accn.get(43);
        ptPyrEffDt = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId, "1", testDb).get(28);
        ptPyrEffDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt);        
        assertEquals(ptPyrEffDtInDB, in1.get(9), "        EFF_DT should be saved into PT_PYR_V2.");
 
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_PHYS properly");        
        //accn_phys
        renderingPhysSeqIdInDB = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId, accnTestSeqId, testDb).get(0);        
        renderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1.get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB, renderingPhysSeqId, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");
        
        referingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(4), testDb).get(0);
        referingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB, referingPhysSeqId, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");       
        
        orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(2), testDb).get(0);
        orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS.");        
                
        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.deleteAccnTestQByAccnId(accnId, testDb);
        daoManagerPlatform.deleteTestQByTestId(testId, testDb);
        
		logger.info("*** Step 12 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
	}

	@Test(priority = 1, description="GenericV231-LIS_INTERFACE_HIST_ERR_MSG_TYP=3, SS#1821 = 0")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_484(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_484 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1, 1821 = 0; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1821", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
		
		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 3 Actions: - Create a GenericV231 HL7 file with all fields and with a Priced Accession");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "genericv231-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId = daoManagerPlatform.getPricedAccnNotInAccnRmk(testDb).get(0);       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        List<String>pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId);         
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        List<String>in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M"); 
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1GenericV231("6197774568", 40, "M", "C", "987456789", "8586217459"); 
        String dos = timeStamp.getCurrentDate("YYYYMMdd");
        List<String>ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "1", "Y", "Y", "1", "71010", dos); 
        List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        List<String>zxa = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false); //pyr_abbrev, isFreeText = false;
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        String content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), 
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE", "CLIENT", "FACILITY"), Arrays.asList(zxa), finalReportDt, true);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
				
		//logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        //Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");
		
        //logger.info("*** Step 4 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		File fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		//assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");
        
		logger.info("*** Step 4 Expected Results: - Verify that the no updates should be applied to the Priced Accession");
		ArrayList<String> accnList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
		assertEquals(accnList.get(2), "21", "        Accession " + accnId + " status should be 21 (Priced).");		
		
		logger.info("*** Step 4 Expected Results: - Verify that LIS_INTERFACE_HIST_ERR.FK_MESSAGE_TYPE_ID = 3");
		Thread.sleep(QUEUE_WAIT_TIME_MS);
		List<List<String>> lisInterfaceHistErrList = daoManagerXifinRpm.getInterfaceErrInfonFromLISINTERFACEHISTERRByAccnId(accnId, testDb);
		String crrDt = timeStamp.getCurrentDate();
		
		assertEquals(lisInterfaceHistErrList.get(0).get(0), "3","        LIS_INTERFACE_HIST_ERR.FK_MESSAGE_TYPE_ID should be 3 (No updates allowed to Priced accession)."); 
		assertEquals(lisInterfaceHistErrList.get(0).get(2), crrDt,"        LIS_INTERFACE_HIST_ERR.ERR_DT should be " + crrDt); 		
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains("Accession already priced no updates allowed"), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain 'Accession already priced no updates allowed' information.");
		
		String ptName = pid.get(3) + " " + pid.get(2);
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains(ptName), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain Patient Name " + ptName);
		
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains(pv1.get(3)), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain Client Abbrev " + pv1.get(3));
		
		crrDt = timeStamp.getCurrentDate("yyyy-MM-dd");
		String dosStrInMsg = "Dos : " + crrDt;
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains(dosStrInMsg), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain " + dosStrInMsg);
				
        logger.info("*** Step 5 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);      
        
		logger.info("*** Step 6 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
	}	

	@Test(priority = 1, description="GenericV231-LIS_INTERFACE_HIST_ERR_MSG_TYP = 1")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_485(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_485 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1, 1821 = 0; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1821", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
		updateSystemSetting(SystemSettingMap.SS_RELEASE_Q_OE_ACCN, "False", "0");//Updating 1116 to false
		updateSystemSetting(SystemSettingMap.SS_Q_OE_MNL_LAB_MSGS, "False", "0");//Updating 1103 to false
		updateSystemSetting(SystemSettingMap.SS_OEPOSTING_CLIENT_ID_BASED_ON_ENCOUNTER_FACILITY, "False", "0");//Updating 1165 to false

		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 3 Actions: - Create a GenericV231 HL7 file with all fields but no DOS");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "genericv231-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId = "QAAUTOHL7" + currDtTime;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        List<String>pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId);         
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        List<String>in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M"); 
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1GenericV231("6197774568", 40, "M", "C", "987456789", "8586217459");   
        String dos = "";
        List<String>ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "1", "Y", "Y", "1", "71010", dos); 
        List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        List<String>zxa = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false); //pyr_abbrev, isFreeText = false;
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        String content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), 
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE", "CLIENT", "FACILITY"), Arrays.asList(zxa), finalReportDt, true);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
				
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
		//Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");
		
        logger.info("*** Step 4 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		File fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		//assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");
        		
		logger.info("*** Step 4 Expected Results: - Verify that LIS_INTERFACE_HIST_ERR.FK_MESSAGE_TYPE_ID = 1");
		Thread.sleep(QUEUE_WAIT_TIME_MS);
		List<List<String>> lisInterfaceHistErrList = daoManagerXifinRpm.getInterfaceErrInfonFromLISINTERFACEHISTERRByAccnId(accnId, testDb);
		String crrDt = timeStamp.getCurrentDate();
		
		assertEquals(lisInterfaceHistErrList.get(0).get(0), "1","        LIS_INTERFACE_HIST_ERR.FK_MESSAGE_TYPE_ID should be 1 (Dos not specified)."); 
		assertEquals(lisInterfaceHistErrList.get(0).get(2), crrDt,"        LIS_INTERFACE_HIST_ERR.ERR_DT should be " + crrDt); 		
			
		String ptName = pid.get(3) + " " + pid.get(2);
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains(ptName), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain Patient Name " + ptName);
		
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains(pv1.get(3)), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain Client Abbrev " + pv1.get(3));

        logger.info("*** Step 5 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);      
        
		logger.info("*** Step 6 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
	}

	@Test(priority = 1, description="GenericV231-LIS_INTERFACE_HIST_ERR_MSG_TYP =12")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_486(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_486 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1, 1821 = 0; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1821", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
		
		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 3 Actions: - Create a GenericV231 HL7 file with all fields but no FT1 and ZXT segments");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "genericv231-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId = "QAAUTOHL7" + currDtTime;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        List<String>pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId);         
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        List<String>in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M"); 
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1GenericV231("6197774568", 40, "M", "C", "987456789", "8586217459");   
        String dos = "";
        List<String>ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "1", "Y", "Y", "1", "71010", dos); 
        List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        List<String>zxa = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false); //pyr_abbrev, isFreeText = false;
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        String content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), 
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE", "CLIENT", "FACILITY"), Arrays.asList(zxa), finalReportDt, false);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
				
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
       // Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");
		
        logger.info("*** Step 4 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		File fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		//assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");
        		
		logger.info("*** Step 4 Expected Results: - Verify that LIS_INTERFACE_HIST_ERR.FK_MESSAGE_TYPE_ID = 12");
		Thread.sleep(QUEUE_WAIT_TIME_MS*2);
		List<List<String>> lisInterfaceHistErrList = daoManagerXifinRpm.getInterfaceErrInfonFromLISINTERFACEHISTERRByAccnId(accnId, testDb);
		String crrDt = timeStamp.getCurrentDate();
		
		assertEquals(lisInterfaceHistErrList.get(0).get(0), "12","        LIS_INTERFACE_HIST_ERR.FK_MESSAGE_TYPE_ID should be 12 (No tests, hl7 message ignored)."); 
		assertEquals(lisInterfaceHistErrList.get(0).get(2), crrDt,"        LIS_INTERFACE_HIST_ERR.ERR_DT should be " + crrDt); 		
			
		String ptName = pid.get(3) + " " + pid.get(2);
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains(ptName), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain Patient Name " + ptName);
		
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains(pv1.get(3)), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain Client Abbrev " + pv1.get(3));

        logger.info("*** Step 5 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);      
        
		logger.info("*** Step 6 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
	}	

	@Test(priority = 1, description="GenericV231-Add multiple accns")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_487(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_487 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
		
		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 3 Actions: - Create a GenericV231 HL7 file with all fields that has multiple Accessions");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "genericv231-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId1 = "QAAUTOHL7" + currDtTime;       
        String ptId1 = randomCharacter.getNonZeroRandomNumericString(8);
        List<String>pid_1 = hl7ParsingEngineUtils.getListPIDGenericV231(accnId1, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId1);         
        List<String>pv1_1 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        List<String>in1_1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M"); //isInvalidPyr = false;
        List<String>gt1_1 = hl7ParsingEngineUtils.getListGT1GenericV231("6197774568", 40, "M", "C", "987456789", "8586217459");
        String dos = timeStamp.getCurrentDate("YYYYMMdd");
        List<String>ft1_1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "1", "Y", "Y", "1", "71010", dos); //isProfTest = false; "D" - Debit; "1" - unit
        List<String>zxt_1 = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        List<String>zxa_1 = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1_1.get(0), false); //pyr_abbrev, isFreeText = false;
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");        
        
        String accnId2 = "QAAUTOHL7" + currDtTime + "_2";       
        String ptId2 = randomCharacter.getNonZeroRandomNumericString(8);
        List<String> pid_2 = hl7ParsingEngineUtils.getListPIDGenericV231(accnId2, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId2);         
        List<String> pv1_2 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        List<String> in1_2 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M"); //isInvalidPyr = false;
        List<String> gt1_2 = hl7ParsingEngineUtils.getListGT1GenericV231("6197774568", 40, "M", "C", "987456789", "8586217459");
        //dos = timeStamp.getCurrentDate("YYYYMMdd");
        List<String> ft1_2 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "1", "Y", "Y", "1", "71010", dos); //isProfTest = false; "D" - Debit; "1" - unit
        List<String> zxt_2 = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        List<String> zxa_2 = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        //dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false); //pyr_abbrev, isFreeText = false;       
 
        String content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccn(currDtTime, Arrays.asList(pid_1, pid_2), Arrays.asList(pv1_1, pv1_2), dg1, Arrays.asList(in1_1, in1_2), 
                Arrays.asList(gt1_1, gt1_2), Arrays.asList(ft1_1, ft1_2), Arrays.asList(zxt_1, zxt_2), Arrays.asList("ENTERPRISE", "CLIENT", "FACILITY"), Arrays.asList(zxa_1, zxa_2), finalReportDt, true);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
		
		logger.info("*** Step 4 Actions: - Insert test data in TEST_Q in DB");				
		String testId_1 = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(ft1_1.get(4), testDb).get(0);		
		daoManagerPlatform.insertTestQ(testId_1, "1", "3", testDb);		
		
		String testId_2 = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(ft1_2.get(4), testDb).get(0);		
		daoManagerPlatform.insertTestQ(testId_2, "1", "3", testDb);	
		
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId1);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId1, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");

        logger.info("*** Step 5 Expected Results: - Verify that data are saved to the corresponding tables properly");
        List<String> qOe1 = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId1, testDb);
        List<String> qOe2 = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId2, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe1.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe1.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");         

        count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe2.get(0)), testDb);
        assertTrue(qOe2.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");

        logger.info("*** Step 5 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
        File fArchive = new File(filePathArchive + fileName);
        System.out.println("File Path = " + filePathArchive + fileName);//Debug Info
        assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");


        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId1);
        Assert.assertTrue(isOutOfQOEQueue(accnId1, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");
		      
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN properly for all accessions");     
        //accn1
        qOe1 = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId1, testDb);       
        assertTrue(qOe1.size()==0,"        The accession should be moved out of Q_OE.");     
        
        String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn1 = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId1, testDb);
        assertTrue(accn1.size()>0,"        Accn " + accnId1 + " should be saved into ACCN.");
        assertEquals(accn1.get(5),pid_1.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn1.get(6),pid_1.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn1.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn1.get(9),pid_1.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn1.get(10),pid_1.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn1.get(11),pid_1.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn1.get(12),pid_1.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn1.get(8),pid_1.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn1.get(3),pid_1.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn1.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn1.get(15),pid_1.get(16),"        Pt SSN is saved into ACCN.");
        
        String clnAbbrev1 = pv1_1.get(3);
        String clnId1 = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev1, testDb);
        String clnIdInDB1 = accn1.get(1);
        assertEquals(clnIdInDB1, clnId1,"        Client ID is saved into accn.fk_cln_id.");
        
        //String ptLocation = pv1.get(0);        
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created        
        String pscId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1_1.get(5), testDb).get(4);
        assertEquals(accn1.get(49), pscId, "        PSC should be saved into ACCN."); 
        assertEquals(accn1.get(56), pv1_1.get(6), "        Admission Source should be saved into ACCN."); 
        assertEquals(accn1.get(32), pv1_1.get(7), "        Patient Type should be saved into ACCN.");
        
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date actDate = df1.parse(accn1.get(18));       
        DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate), ft1_1.get(13), "        Receipt Date should be saved into ACCN."); 
        
        assertEquals(accn1.get(34), ft1_1.get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn1.get(33), ft1_1.get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn1.get(36), ft1_1.get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn1.get(35), "1", "        roundTrip should be saved into ACCN.");
        
        assertEquals(accn1.get(48), zxa_1.get(5), "        Phlebotomist ID should be saved into ACCN.");
        
        String onSetDtStrInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn1.get(61));
        assertEquals(onSetDtStrInDB, zxa_1.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn1.get(62), zxa_1.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn1.get(59), zxa_1.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn1.get(19), zxa_1.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn1.get(42), "1", "        MSP Received Flag should be saved into ACCN.");
        assertEquals(accn1.get(63), zxa_1.get(15), "        Accident State should be saved into ACCN.");     
               
        //accn2
        qOe2 = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId2, testDb);       
        assertTrue(qOe2.size()==0,"        The accession should be moved out of Q_OE.");     
        
        //String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn2 = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId2, testDb);
        assertTrue(accn2.size()>0,"        Accn " + accnId2 + " should be saved into ACCN.");
        assertEquals(accn2.get(5),pid_2.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn2.get(6),pid_2.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn2.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn2.get(9),pid_2.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn2.get(10),pid_2.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn2.get(11),pid_2.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn2.get(12),pid_2.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn2.get(8),pid_2.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn2.get(3),pid_2.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn2.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn2.get(15),pid_2.get(16),"        Pt SSN is saved into ACCN.");
        
        String clnAbbrev2 = pv1_2.get(3);
        String clnId2 = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev2, testDb);
        String clnIdInDB2 = accn2.get(1);
        assertEquals(clnIdInDB2, clnId2,"        Client ID is saved into accn.fk_cln_id.");
        
        //String ptLocation = pv1.get(0);        
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created        
        String pscId2 = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1_2.get(5), testDb).get(4);
        assertEquals(accn2.get(49), pscId2, "        PSC should be saved into ACCN."); 
        assertEquals(accn2.get(56), pv1_2.get(6), "        Admission Source should be saved into ACCN."); 
        assertEquals(accn2.get(32), pv1_2.get(7), "        Patient Type should be saved into ACCN.");
        
        //DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date actDate2 = df1.parse(accn2.get(18));       
        //DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate2), ft1_2.get(13), "        Receipt Date should be saved into ACCN."); 
        
        assertEquals(accn2.get(34), ft1_2.get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn2.get(33), ft1_2.get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn2.get(36), ft1_2.get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn2.get(35), "1", "        roundTrip should be saved into ACCN.");
        
        assertEquals(accn2.get(48), zxa_2.get(5), "        Phlebotomist ID should be saved into ACCN.");
        
        String onSetDtStrInDB2 = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn2.get(61));
        assertEquals(onSetDtStrInDB2, zxa_2.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn2.get(62), zxa_2.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn2.get(59), zxa_2.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn2.get(19), zxa_2.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn2.get(42), "1", "        MSP Received Flag should be saved into ACCN.");
        assertEquals(accn2.get(63), zxa_2.get(15), "        Accident State should be saved into ACCN.");       
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_V2 properly for all accessions");
        String epi1 = pid_1.get(0);
        System.out.println("epi1 = " + epi1);//debug info
        List<String> ptV2List1 = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi1, testDb);
        assertTrue(ptV2List1.size()>0,"        Patient EPI " + epi1 + " should be saved into PT_V2.");  
        
        String epi2 = pid_2.get(0);
        System.out.println("epi2 = " + epi2);//debug info
        List<String> ptV2List2 = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi2, testDb);
        assertTrue(ptV2List2.size()>0,"        Patient EPI " + epi2 + " should be saved into PT_V2."); 
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_PYR properly for all accessions"); 
        //accn_pyr
        //accn1
        List<List<String>> accnPyr1 = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId1, testDb);
        assertTrue(accnPyr1.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr1.get(0).get(0),testDb),in1_1.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr1.get(0).get(7),in1_1.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr1.get(0).get(8),in1_1.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr1.get(0).get(9),in1_1.get(13),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr1.get(0).get(18),in1_1.get(14),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr1.get(0).get(16),in1_1.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr1.get(0).get(25),in1_1.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr1.get(0).get(20), "1","        INS_SEX is saved into Db"); //1 = M (Male)
        assertEquals(accnPyr1.get(0).get(26),in1_1.get(23),"        SUBS_ID is saved into Db");
        //assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created
        
        //String eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        //assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created
        
        String insuredDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr1.get(0).get(19));
        assertEquals(insuredDOBInDB, in1_1.get(12),"        INS_DOB is saved into Db");
        
        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr1.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr1.get(1).get(7).toUpperCase(),gt1_1.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");       
        assertEquals(accnPyr1.get(1).get(8).toUpperCase(),gt1_1.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");        
        
        assertEquals(accnPyr1.get(1).get(9).toUpperCase(),gt1_1.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr1.get(1).get(18).toUpperCase(),gt1_1.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");
        
        String guarantorDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr1.get(1).get(19));
        assertEquals(guarantorDOBInDB, gt1_1.get(8),"        Guarantor's DOB is saved into Db");
        
        assertEquals(accnPyr1.get(1).get(20), "1","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr1.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr1.get(1).get(22), gt1_1.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr1.get(1).get(27), gt1_1.get(7),"        Guarantor's Home Phone is saved into Db");        
        
        assertEquals(accnPyr1.get(1).get(11), gt1_1.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr1.get(1).get(12), gt1_1.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr1.get(1).get(23), gt1_1.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr1.get(1).get(24), gt1_1.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr1.get(1).get(28), gt1_1.get(19),"        Employer's Phone number is saved into Db");     
                
        //accn_pyr
        //accn2
        List<List<String>> accnPyr2 = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId2, testDb);
        assertTrue(accnPyr2.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr2.get(0).get(0),testDb),in1_2.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr2.get(0).get(7),in1_2.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr2.get(0).get(8),in1_2.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr2.get(0).get(9),in1_2.get(13),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr2.get(0).get(18),in1_2.get(14),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr2.get(0).get(16),in1_2.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr2.get(0).get(25),in1_2.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr2.get(0).get(20), "1","        INS_SEX is saved into Db"); //1 = M (Male)
        assertEquals(accnPyr2.get(0).get(26),in1_2.get(23),"        SUBS_ID is saved into Db");
        //assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created
        
        //String eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        //assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created
        
        String insuredDOBInDB2 = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr2.get(0).get(19));
        assertEquals(insuredDOBInDB2, in1_2.get(12),"        INS_DOB is saved into Db");
        
        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr2.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr2.get(1).get(7).toUpperCase(),gt1_2.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");       
        assertEquals(accnPyr2.get(1).get(8).toUpperCase(),gt1_2.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");        
        
        assertEquals(accnPyr2.get(1).get(9).toUpperCase(),gt1_2.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr2.get(1).get(18).toUpperCase(),gt1_2.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");
        
        String guarantorDOBInDB2 = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr2.get(1).get(19));
        assertEquals(guarantorDOBInDB2, gt1_2.get(8),"        Guarantor's DOB is saved into Db");
        
        assertEquals(accnPyr2.get(1).get(20), "1","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr2.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr2.get(1).get(22), gt1_2.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr2.get(1).get(27), gt1_2.get(7),"        Guarantor's Home Phone is saved into Db");        
        
        assertEquals(accnPyr2.get(1).get(11), gt1_2.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr2.get(1).get(12), gt1_2.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr2.get(1).get(23), gt1_2.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr2.get(1).get(24), gt1_2.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr2.get(1).get(28), gt1_2.get(19),"        Employer's Phone number is saved into Db");                
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST properly for all accessions"); 
        //accn_test
        //accn1
        List<String> accnTest1 = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId1, testDb);
        assertTrue(accnTest1.size() > 0,"        Data is saved into Db");
        assertEquals(accnTest1.get(0),ft1_1.get(4),"        Test is saved into Db");
        
        ArrayList<String> accnTestList1 = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId1, ft1_1.get(4), testDb);        
        String accnTestSeqId1 = accnTestList1.get(0);
        assertEquals(accnTestList1.get(6),ft1_1.get(19),"        Mod1 is saved into Db");
        
        assertEquals(accnTestList1.get(3),ft1_1.get(6),"        Units is saved into Db");
        assertEquals(accnTestList1.get(12),ft1_1.get(5),"        ALT_TEST_NAME is saved into Db");        
        assertEquals(accnTestList1.get(13),ft1_1.get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList1.get(14),ft1_1.get(0),"        LIS Trace Id is saved into Db");
        //assertEquals(accnTestList1.get(4), "1", "        Performing Facility is saved into Db");
        
        assertEquals(accnTestList1.get(7),zxt_1.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList1.get(8), "1","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList1.get(9), "1","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList1.get(10), zxt_1.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList1.get(11), zxt_1.get(5),"        B_RENAL is saved into Db");               
               
        //accn_test
        //accn2
        List<String> accnTest2 = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId2, testDb);
        assertTrue(accnTest2.size() > 0,"        Data is saved into Db");
        assertEquals(accnTest2.get(0),ft1_2.get(4),"        Test is saved into Db");
        
        ArrayList<String> accnTestList2 = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId2, ft1_2.get(4), testDb);        
        String accnTestSeqId2 = accnTestList2.get(0);
        assertEquals(accnTestList2.get(6),ft1_2.get(19),"        Mod1 is saved into Db");
        
        assertEquals(accnTestList2.get(3),ft1_2.get(6),"        Units is saved into Db");
        assertEquals(accnTestList2.get(12),ft1_2.get(5),"        ALT_TEST_NAME is saved into Db");        
        assertEquals(accnTestList2.get(13),ft1_2.get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList2.get(14),ft1_2.get(0),"        LIS Trace Id is saved into Db");
        //assertEquals(accnTestList2.get(4), "1", "        Performing Facility is saved into Db");
        
        assertEquals(accnTestList2.get(7),zxt_2.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList2.get(8), "1","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList2.get(9), "1","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList2.get(10), zxt_2.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList2.get(11), zxt_2.get(5),"        B_RENAL is saved into Db");
               
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_DAIG properly for all accessions"); 
        //accn_daig
        //accn1
        List<String> accnDiag1 = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId1, "1" ,testDb);
        assertTrue(accnDiag1.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag1.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
                
        //test level diag
        //accn1
        accnDiag1 = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId1, "2" ,testDb);
        assertEquals(accnDiag1.get(2), ft1_1.get(11),"        Test level Diag Code " + ft1_1.get(11) + " should be saved into ACCN_DIAG.");
               
        //accn_daig
        //accn2
        List<String> accnDiag2 = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId2, "1" ,testDb);
        assertTrue(accnDiag2.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag2.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
                
        //test level diag
        //accn2
        accnDiag2 = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId2, "2" ,testDb);
        assertEquals(accnDiag2.get(2), ft1_2.get(11),"        Test level Diag Code " + ft1_2.get(11) + " should be saved into ACCN_DIAG.");        
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CLN_Q properly for all accessions"); 
        //accn_cln_q     
        //accn1
        List<String> accnClnQ1 = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId1,testDb); 
        assertTrue(accnClnQ1.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ1.get(0), zxa_1.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa_1.get(0) + " should be saved.");
        assertEquals(accnClnQ1.get(1), zxa_1.get(4),"        ACCN_CLN_Q.RESPNS " + zxa_1.get(4) + " should be saved.");
               
        //accn_cln_q     
        //accn2
        List<String> accnClnQ2 = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId2,testDb); 
        assertTrue(accnClnQ2.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ2.get(0), zxa_2.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa_2.get(0) + " should be saved.");
        assertEquals(accnClnQ2.get(1), zxa_2.get(4),"        ACCN_CLN_Q.RESPNS " + zxa_2.get(4) + " should be saved.");
               
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CNTCT properly for all accessions"); 
        //accn_cntct
        //accn1
        List<String> accnCntct1 = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId1, "NOTE is null", testDb);
        assertTrue(accnCntct1.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct1.get(0), zxa_1.get(3),"        CNTCT_INFO " + zxa_1.get(3) + " should be saved into ACCN_CNTCT.");
       
        accnCntct1 = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId1, "NOTE is not null", testDb);
        assertEquals(accnCntct1.get(3), zxa_1.get(11),"       " + zxa_1.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct1.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");
               
        //accn_cntct
        //accn2
        List<String> accnCntct2 = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId2, "NOTE is null", testDb);
        assertTrue(accnCntct2.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct2.get(0), zxa_2.get(3),"        CNTCT_INFO " + zxa_2.get(3) + " should be saved into ACCN_CNTCT.");
       
        accnCntct2 = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId2, "NOTE is not null", testDb);
        assertEquals(accnCntct2.get(3), zxa_2.get(11),"       " + zxa_2.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct2.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");
                        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CLINTRL properly for all accessions"); 
        //accn_clintrl
        //accn1
        List<String> accnClintrl1 = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId1,testDb);
        assertTrue(accnClintrl1.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl1.get(0),zxa_1.get(4),"        CLINTRL_ENCNTR " + zxa_1.get(4) + " should be saved into ACCN_CLINTRL.");
        
        //accn_clintrl
        //accn2
        List<String> accnClintrl2 = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId2,testDb);
        assertTrue(accnClintrl2.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl2.get(0),zxa_2.get(4),"        CLINTRL_ENCNTR " + zxa_2.get(4) + " should be saved into ACCN_CLINTRL.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_DAIL properly for all accessions"); 
        //accn_dail
        //accn1
        List<String> accnDial1 = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId1,testDb);
        assertTrue(accnDial1.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial1.get(1), zxa_1.get(6),"        FK_DIAL_TYP_ID " + zxa_1.get(6) + " should be saved into ACCN_DIAL.");        
        
        String firstDailDtInDB1 = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial1.get(3));
        assertEquals(firstDailDtInDB1, zxa_1.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL.");       
               
        //accn_dail
        //accn2
        List<String> accnDial2 = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId2,testDb);
        assertTrue(accnDial2.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial2.get(1), zxa_2.get(6),"        FK_DIAL_TYP_ID " + zxa_2.get(6) + " should be saved into ACCN_DIAL.");        
        
        String firstDailDtInDB2 = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial2.get(3));
        assertEquals(firstDailDtInDB2, zxa_2.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL."); 
               
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST_Q properly"); 
        //accn_test_q
        //accn1
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId1, accnTestSeqId1).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId1, accnTestSeqId1).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");
        
        //accn_test_q
        //accn2
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId2, accnTestSeqId2).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId2, accnTestSeqId2).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_PYR_V2 properly for all accessions"); 
        //pt_pyr_v2
        //accn1
        String ptSeqId1 = accn1.get(43);
        String ptPyrEffDt1 = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId1, "1", testDb).get(28);
        String ptPyrEffDtInDB1 = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt1);        
        assertEquals(ptPyrEffDtInDB1, in1_1.get(9), "        EFF_DT should be saved into PT_PYR_V2.");
        
        //pt_pyr_v2
        //accn2
        String ptSeqId2 = accn2.get(43);
        String ptPyrEffDt2 = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId2, "1", testDb).get(28);
        String ptPyrEffDtInDB2 = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt2);        
        assertEquals(ptPyrEffDtInDB2, in1_2.get(9), "        EFF_DT should be saved into PT_PYR_V2.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_CLN_LNK properly for all accessions");
        //pt_cln_lnk
        //accn1
        String ptClnIdInDB1 = daoManagerPlatform.getPtClnLnkInfoFromByPtSeqIdClnId(ptSeqId1, clnIdInDB1, testDb).get(1);
        assertEquals(ptClnIdInDB1, pid_1.get(0), "        SPECIFIC_PT_ID should be saved into PT_CLN_LNK.");       
        
        //pt_cln_lnk
        String ptClnIdInDB2 = daoManagerPlatform.getPtClnLnkInfoFromByPtSeqIdClnId(ptSeqId2, clnIdInDB2, testDb).get(1);
        assertEquals(ptClnIdInDB2, pid_2.get(0), "        SPECIFIC_PT_ID should be saved into PT_CLN_LNK.");        
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_FAC_LNK properly for all accessions");     
        //pt_fac_lnk
        //accn1
        String ptFacIdInDB1 = daoManagerXifinRpm.getPtFacLnkInfoFromPTFACLNKByPtSeqId(ptSeqId1, testDb).get(3);
        assertEquals(ptFacIdInDB1, pid_1.get(0), "        SPECIFIC_PT_ID should be saved into PT_FAC_LNK.");
        
        //pt_fac_lnk
        //accn2
        String ptFacIdInDB2 = daoManagerXifinRpm.getPtFacLnkInfoFromPTFACLNKByPtSeqId(ptSeqId2, testDb).get(3);
        assertEquals(ptFacIdInDB2, pid_2.get(0), "        SPECIFIC_PT_ID should be saved into PT_FAC_LNK.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_PHYS properly for all accessions");        
        //accn_phys
        //accn1
        String renderingPhysSeqIdInDB1 = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId1, accnTestSeqId1, testDb).get(0);        
        String renderingPhysSeqId1 = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1_1.get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB1, renderingPhysSeqId1, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");
        
        String referingPhysSeqId1 = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1_1.get(4), testDb).get(0);
        String referingPhysSeqIdInDB1 = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId1, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB1, referingPhysSeqId1, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");       
        
        String orderingPhysSeqId1 = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1_1.get(2), testDb).get(0);
        String orderingPhysTypIdInDB1 = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId1, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB1, orderingPhysSeqId1, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS."); 
                		
        //accn_phys
        //accn2
        String renderingPhysSeqIdInDB2 = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId2, accnTestSeqId2, testDb).get(0);        
        String renderingPhysSeqId2 = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1_2.get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB2, renderingPhysSeqId2, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");
        
        String referingPhysSeqId2 = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1_2.get(4), testDb).get(0);
        String referingPhysSeqIdInDB2 = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId2, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB2, referingPhysSeqId2, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");       
        
        String orderingPhysSeqId2 = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1_2.get(2), testDb).get(0);
        String orderingPhysTypIdInDB2 = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId2, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB2, orderingPhysSeqId2, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS.");		
        		
        		
        logger.info("*** Step 7 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.deleteAccnTestQByAccnId(accnId1, testDb);
        daoManagerPlatform.deleteAccnTestQByAccnId(accnId2, testDb);
        daoManagerPlatform.deleteTestQByTestId(testId_1, testDb);
        daoManagerPlatform.deleteTestQByTestId(testId_2, testDb);
        
		logger.info("*** Step 8 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
	}
	
	@Test(priority = 1, description="GenericV231-LIS_INTERFACE_HIST_ERR_MSG_TYP = 5")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_499(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_499 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
		
		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 3 Actions: - Create a GenericV231 HL7 file with a Profile test that only has components");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "genericv231-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId = "QAAUTOHL7" + currDtTime;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        String dos = timeStamp.getCurrentDate("YYYYMMdd"); 
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
        
        List<String>pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId);         
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        List<String>in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M"); 
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1GenericV231("6197774568", 40, "M", "C", "987456789", "8586217459");
        //Profile test that only has components      
        //List<String>ft1_P = hl7ParsingEngineUtils.getListFT1WithProfileTestsGenericV231("DW1", "", "D", "1", "Y", "Y", "1", dos, true);
        List<String>ft1_C1 = hl7ParsingEngineUtils.getListFT1WithProfileTestsGenericV231("CBC", "DW1", "D", "1", "Y", "Y", "1", dos, false);
        List<String>ft1_C2 = hl7ParsingEngineUtils.getListFT1WithProfileTestsGenericV231("PSA", "DW1", "D", "1", "Y", "Y", "1", dos, false);
        List<List<String>> ft1 = new ArrayList<List<String>>();
        //ft1.add(ft1_P);
        ft1.add(ft1_C1);
        ft1.add(ft1_C2);      
        
        List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        List<String>zxa = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false);         
        
        //needs updates
        String content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccnProfTest(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), 
               Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE", "CLIENT", "FACILITY"), Arrays.asList(zxa), finalReportDt, true);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
				
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");
		
        logger.info("*** Step 4 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		File fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");  
        		
        logger.info("*** Step 4 Expected Results: - Verify that data are saved to the corresponding tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");       
        
        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");

		logger.info("*** Step 5 Expected Results: - Verify that the accession should be moved out of Q_OE");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession " + accnId + " should be moved out of Q_OE.");        
        
		logger.info("*** Step 5 Expected Results: - Verify that LIS_INTERFACE_HIST_ERR.FK_MESSAGE_TYPE_ID = 5 (Components received but no prof in hl7)");
		List<List<String>> lisInterfaceHistErrList = daoManagerXifinRpm.getInterfaceErrInfonFromLISINTERFACEHISTERRByAccnId(accnId, testDb);
		String crrDt = timeStamp.getCurrentDate();
		
		assertEquals(lisInterfaceHistErrList.get(0).get(0), "5","        LIS_INTERFACE_HIST_ERR.FK_MESSAGE_TYPE_ID should be 5 (Components received but no prof in hl7)."); 
		assertEquals(lisInterfaceHistErrList.get(0).get(2), crrDt,"        LIS_INTERFACE_HIST_ERR.ERR_DT should be " + crrDt); 		

		String ptName = pid.get(3) + " " + pid.get(2);
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains(ptName), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain Patient Name " + ptName);
		
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains(pv1.get(3)), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain Client Abbrev " + pv1.get(3));
		
		crrDt = timeStamp.getCurrentDate("yyyy-MM-dd");
		String dosStrInMsg = "Dos : " + crrDt;
		assertTrue(lisInterfaceHistErrList.get(0).get(3).contains(dosStrInMsg), "        LIS_INTERFACE_HIST_ERR.MESSAGE should contain " + dosStrInMsg);
        
        logger.info("*** Step 6 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
 
		logger.info("*** Step 7 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
	}	
	
	@Test(priority = 1, description="GenericV231-Add and Update accn with Profile test")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_488(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_488 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
        updateSystemSetting(SystemSettingMap.SS_RELEASE_Q_OE_ACCN, "False", "0");//Updating 1116 to false
        updateSystemSetting(SystemSettingMap.SS_Q_OE_MNL_LAB_MSGS, "False", "0");//Updating 1103 to false
        updateSystemSetting(SystemSettingMap.SS_OEPOSTING_CLIENT_ID_BASED_ON_ENCOUNTER_FACILITY, "False", "0");//Updating 1165 to false
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
		
		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 3 Actions: - Create a GenericV231 HL7 file with all fields and a Profile test");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "genericv231-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId = "QAAUTOHL7" + currDtTime;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        String dos = timeStamp.getCurrentDate("YYYYMMdd"); 
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");        
        //MSH 1
        List<String>pid_1 = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "F", 17, "8586578854", "6197353125", "F", "987654321", ptId);         
        List<String>pv1_1 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        List<String>in1_1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "N", "F"); 
        List<String>gt1_1 = hl7ParsingEngineUtils.getListGT1GenericV231("8587774568", 42, "F", "S", "987456777", "6196217459");
        //Profile test       
        List<String>ft1_P_1 = hl7ParsingEngineUtils.getListFT1WithProfileTestsGenericV231("DW1", "", "D", "1", "Y", "Y", "1", dos, true);
        List<String>ft1_C1_1 = hl7ParsingEngineUtils.getListFT1WithProfileTestsGenericV231("CBC", "DW1", "D", "1", "Y", "Y", "1", dos, false);
        List<String>ft1_C2_1 = hl7ParsingEngineUtils.getListFT1WithProfileTestsGenericV231("PSA", "DW1", "D", "1", "Y", "Y", "1", dos, false);
        List<List<String>> ft1_1 = new ArrayList<List<String>>();
        ft1_1.add(ft1_P_1);
        ft1_1.add(ft1_C1_1);
        ft1_1.add(ft1_C2_1);             
        
        //List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        List<String>zxa_1 = hl7ParsingEngineUtils.getListZXAGenericV231("N");
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1_1.get(0), false);                
        
        //MSH 2
        List<String>pid_2 = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId);         
        List<String>pv1_2 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        List<String>in1_2 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M"); 
        List<String>gt1_2 = hl7ParsingEngineUtils.getListGT1GenericV231("6197774568", 40, "M", "C", "987456789", "8586217459");
        //Profile test       
        List<String>ft1_P_2 = hl7ParsingEngineUtils.getListFT1WithProfileTestsGenericV231("DW1", "", "D", "1", "Y", "Y", "1", dos, true);
        List<String>ft1_C1_2 = hl7ParsingEngineUtils.getListFT1WithProfileTestsGenericV231("CBC", "DW1", "D", "1", "Y", "Y", "1", dos, false);
        List<String>ft1_C2_2 = hl7ParsingEngineUtils.getListFT1WithProfileTestsGenericV231("PSA", "DW1", "D", "1", "Y", "Y", "1", dos, false);
        List<List<String>> ft1_2 = new ArrayList<List<String>>();
        ft1_2.add(ft1_P_2);
        ft1_2.add(ft1_C1_2);
        ft1_2.add(ft1_C2_2);              
        
        List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        List<String>zxa_2 = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1_2.get(0), false);         
 
        String content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccnProfTest(currDtTime, Arrays.asList(pid_1, pid_2), Arrays.asList(pv1_1, pv1_2), dg1, Arrays.asList(in1_1, in1_2), 
                Arrays.asList(gt1_1, gt1_2), Arrays.asList(ft1_1, ft1_2), Arrays.asList(zxt), Arrays.asList("ENTERPRISE", "CLIENT", "FACILITY"), Arrays.asList(zxa_1, zxa_2), finalReportDt, true);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");

        logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");

        logger.info("*** Step 4 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		File fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");  
        		
        logger.info("*** Step 4 Expected Results: - Verify that data are saved to the corresponding tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");       
        
        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");	
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession should be moved out of Q_OE");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession " + accnId + " should be moved out of Q_OE."); 
                
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to ACCN properly");              
        String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid_2.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid_2.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid_2.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid_2.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid_2.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid_2.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn.get(8),pid_2.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn.get(3),pid_2.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid_2.get(16),"        Pt SSN is saved into ACCN.");
        
        String clnAbbrev = pv1_2.get(3);
        String clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        String clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");
        
        //String ptLocation = pv1.get(0);        
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created        
        String pscId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1_2.get(5), testDb).get(4);
        assertEquals(accn.get(49), pscId, "        PSC should be saved into ACCN."); 
        assertEquals(accn.get(56), pv1_2.get(6), "        Admission Source should be saved into ACCN."); 
        assertEquals(accn.get(32), pv1_2.get(7), "        Patient Type should be saved into ACCN.");
        
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date actDate = df1.parse(accn.get(18));       
        DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate), ft1_2.get(0).get(13), "        Receipt Date should be saved into ACCN."); 
        
        assertEquals(accn.get(34), ft1_2.get(0).get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn.get(33), ft1_2.get(0).get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn.get(36), ft1_2.get(0).get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn.get(35), "1", "        roundTrip should be saved into ACCN.");
        
        assertEquals(accn.get(48), zxa_2.get(5), "        Phlebotomist ID should be saved into ACCN.");
        
        String onSetDtStrInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn.get(61));
        assertEquals(onSetDtStrInDB, zxa_2.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn.get(62), zxa_2.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn.get(59), zxa_2.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn.get(19), zxa_2.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn.get(42), "1", "        MSP Received Flag should be saved into ACCN.");
        assertEquals(accn.get(63), zxa_2.get(15), "        Accident State should be saved into ACCN.");        
        
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to PT_V2 properly");
        String epi = pid_2.get(0);
        System.out.println("epi = " + epi);//debug info
        List<String> ptV2List = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi, testDb);
        assertTrue(ptV2List.size()>0,"        Patient EPI " + epi + " should be saved into PT_V2.");  
        
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to ACCN_PYR properly"); 
        //accn_pyr
        List<List<String>> accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1_2.get(0),"        PyrId is saved into Db");
        assertEquals(accnPyr.get(0).get(7),in1_2.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1_2.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1_2.get(13),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr.get(0).get(18),in1_2.get(14),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr.get(0).get(16),in1_2.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(25),in1_2.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(20), "1","        INS_SEX is saved into Db"); //1 = M (Male)
        assertEquals(accnPyr.get(0).get(26),in1_2.get(23),"        SUBS_ID is saved into Db");
        //assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created
        
        //String eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        //assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created
        
        String insuredDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(19));
        assertEquals(insuredDOBInDB, in1_2.get(12),"        INS_DOB is saved into Db");
        
        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr.get(1).get(7).toUpperCase(),gt1_2.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");       
        assertEquals(accnPyr.get(1).get(8).toUpperCase(),gt1_2.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(9).toUpperCase(),gt1_2.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(18).toUpperCase(),gt1_2.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");
        
        String guarantorDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(1).get(19));
        assertEquals(guarantorDOBInDB, gt1_2.get(8),"        Guarantor's DOB is saved into Db");
        
        assertEquals(accnPyr.get(1).get(20), "1","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr.get(1).get(22), gt1_2.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr.get(1).get(27), gt1_2.get(7),"        Guarantor's Home Phone is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(11), gt1_2.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr.get(1).get(12), gt1_2.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(23), gt1_2.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr.get(1).get(24), gt1_2.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr.get(1).get(28), gt1_2.get(19),"        Employer's Phone number is saved into Db");        
        
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to ACCN_TEST properly"); 
        //accn_test
        List<String> accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");
        int index = accnTest.size()-1;/////////////////
        ///assertEquals(accnTest.get(0),ft1_2.get(0).get(4),"        Profile Test is saved into Db");
        assertEquals(accnTest.get(index),ft1_2.get(0).get(4),"        Profile Test is saved into Db");///////////////////////
        
        //Profile 
        ArrayList<String> accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1_2.get(0).get(4), testDb);        
        String accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(15),ft1_2.get(0).get(19),"        Mod2 is saved into Db");
        
        assertEquals(accnTestList.get(3),ft1_2.get(0).get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1_2.get(0).get(5),"        ALT_TEST_NAME is saved into Db");        
        //assertEquals(accnTestList.get(13),ft1_2.get(0).get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),"1","        LIS Trace Id is saved into Db");
        //assertEquals(accnTestList.get(4), "1", "        Performing Facility is saved into Db");
        
        //Component1
        accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1_2.get(1).get(4), testDb);        
        accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(15),ft1_2.get(1).get(19),"        Mod2 is saved into Db");
        
        assertEquals(accnTestList.get(3),ft1_2.get(1).get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1_2.get(1).get(5),"        ALT_TEST_NAME is saved into Db");        
        assertEquals(accnTestList.get(13),ft1_2.get(1).get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),"2","        LIS Trace Id is saved into Db");
        //assertEquals(accnTestList.get(4), "1", "        Performing Facility is saved into Db");
        
        //Component2
        accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1_2.get(2).get(4), testDb);        
        accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(15),ft1_2.get(2).get(19),"        Mod2 is saved into Db");
        
        assertEquals(accnTestList.get(3),ft1_2.get(2).get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1_2.get(2).get(5),"        ALT_TEST_NAME is saved into Db");        
        assertEquals(accnTestList.get(13),ft1_2.get(2).get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),"3","        LIS Trace Id is saved into Db");
        //assertEquals(accnTestList.get(4), "1", "        Performing Facility is saved into Db");

        /*
        assertEquals(accnTestList.get(7),zxt.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(8), "1","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList.get(9), "1","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList.get(10), zxt.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList.get(11), zxt.get(5),"        B_RENAL is saved into Db");               
        */
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to ACCN_DAIG properly"); 
        //accn_daig
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "1" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
                
        //test level diag - Component 1
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "4" ,testDb);
        assertEquals(accnDiag.get(2), ft1_2.get(1).get(11),"        Test level Diag Code " + ft1_2.get(1).get(11) + " should be saved into ACCN_DIAG.");
        
        //test level diag - Component 2
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "5" ,testDb);
        assertEquals(accnDiag.get(2), ft1_2.get(2).get(11),"        Test level Diag Code " + ft1_2.get(2).get(11) + " should be saved into ACCN_DIAG.");
                
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to ACCN_CLN_Q properly"); 
        //accn_cln_q        
        List<String> accnClnQ = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId,testDb); 
        assertTrue(accnClnQ.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ.get(0), zxa_2.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa_2.get(0) + " should be saved.");
        assertEquals(accnClnQ.get(1), zxa_2.get(4),"        ACCN_CLN_Q.RESPNS " + zxa_2.get(4) + " should be saved.");
        
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to ACCN_CNTCT properly"); 
        //accn_cntct
        List<String> accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is null and PK_CNTCT_SEQ > 2", testDb);
        assertTrue(accnCntct.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct.get(0), zxa_2.get(3),"        CNTCT_INFO " + zxa_2.get(3) + " should be saved into ACCN_CNTCT.");
       
        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is not null and PK_CNTCT_SEQ > 2", testDb);
        assertEquals(accnCntct.get(3), zxa_2.get(11),"       " + zxa_2.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");
                
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to ACCN_CLINTRL properly"); 
        //accn_clintrl
        List<String> accnClintrl = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId,testDb);
        assertTrue(accnClintrl.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl.get(0),zxa_2.get(4),"        CLINTRL_ENCNTR " + zxa_2.get(4) + " should be saved into ACCN_CLINTRL.");
        
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to ACCN_DAIL properly"); 
        //accn_dail
        List<String> accnDial = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId,testDb);
        assertTrue(accnDial.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial.get(1), zxa_2.get(6),"        FK_DIAL_TYP_ID " + zxa_2.get(6) + " should be saved into ACCN_DIAL.");        
        
        String firstDailDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial.get(3));
        assertEquals(firstDailDtInDB, zxa_2.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL.");       
        /*
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST_Q properly"); 
        //accn_test_q
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");
        */
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to PT_PYR_V2 properly"); 
        //pt_pyr_v2
        String ptSeqId = accn.get(43);
        String ptPyrEffDt = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId, "1", testDb).get(28);
        String ptPyrEffDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt);        
        assertEquals(ptPyrEffDtInDB, in1_2.get(9), "        EFF_DT should be saved into PT_PYR_V2.");
        
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to PT_CLN_LNK properly");
        //pt_cln_lnk
        String ptClnIdInDB = daoManagerPlatform.getPtClnLnkInfoFromByPtSeqIdClnId(ptSeqId, clnIdInDB, testDb).get(1);
        assertEquals(ptClnIdInDB, pid_2.get(0), "        SPECIFIC_PT_ID should be saved into PT_CLN_LNK.");
        
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to PT_FAC_LNK properly");     
        //pt_fac_lnk
        String ptFacIdInDB = daoManagerXifinRpm.getPtFacLnkInfoFromPTFACLNKByPtSeqId(ptSeqId, testDb).get(3);
        assertEquals(ptFacIdInDB, pid_2.get(0), "        SPECIFIC_PT_ID should be saved into PT_FAC_LNK.");
        
        logger.info("*** Step 5 Expected Results: - Verify that the updated data are saved to ACCN_PHYS properly");        
        //accn_phys
        //Rendering Physician for Component1
        accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1_2.get(1).get(4), testDb);        
        accnTestSeqId = accnTestList.get(0);
        String renderingPhysSeqIdInDB = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId, accnTestSeqId, testDb).get(0);         
        String renderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1_2.get(1).get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB, renderingPhysSeqId, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");
               
        //Rendering Physician for Component2
        accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1_2.get(2).get(4), testDb);        
        accnTestSeqId = accnTestList.get(0);
        renderingPhysSeqIdInDB = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId, accnTestSeqId, testDb).get(0);
        renderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1_2.get(2).get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB, renderingPhysSeqId, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");
        
        String referingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1_2.get(4), testDb).get(0);
        String referingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB, referingPhysSeqId, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");       
        
        String orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1_2.get(2), testDb).get(0);
        String orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS."); 
        		
        logger.info("*** Step 6 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        //daoManagerPlatform.deleteAccnTestQByAccnId(accnId, testDb);
        //daoManagerPlatform.deleteTestQByTestId(testId, testDb);
        
		logger.info("*** Step 7 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
	}	
	
	
	@Test(priority = 1, description="Nslij-Add and Update accn with all HL7 fields")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_490(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_490 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);


        logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 1, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; Update TASK_TYP.PK_TASK_TYP_ID = 18, Update TASK_TYP.PK_TASK_TYP_ID = 1116, Update TASK_TYP.PK_TASK_TYP_ID = 1103,Update TASK_TYP.PK_TASK_TYP_ID = 1165 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.nslij.NslijHl7Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.nslij.NslijHl7Parser'","PK_TASK_TYP_ID", "18", testDb);
		
		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 3 Actions: - Create a Nslij HL7 file with all fields");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "nslij-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId = "QAAUTOHL7" + currDtTime;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(9);
        //String accnId, String ptId, int years, String ptSex, String ptHomePhone, String ptSSN
        List<String>pid = hl7ParsingEngineUtils.getListPIDNslij(accnId, ptId, 25, "M", "6196578854", "123456789");       
        //String ptTyp, String linkAccn, String clnAbbrev
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1Nslij("I", "", "10002");        
        //isInvalidPyr, insRelTyp
        List<String>in1 = hl7ParsingEngineUtils.getListIN1Nslij(false, "SELF");
        //boolean isProfTest, String transTyp, String testFacId, String specifiedTestAbbrev, String dos
        //Can add multiple FT1 segments
        String dos = timeStamp.getCurrentDate("YYYYMMdd");
        List<String>ft1 = hl7ParsingEngineUtils.getListFT1Nslij(false, "+", "1", "71010", dos); 
        List<List<String>> ft1List = new ArrayList<List<String>>();
        ft1List.add(ft1);        
        //pyr_abbrev, isFreeText



        List<String>dg1 = hl7ParsingEngineUtils.getListDG1Nslij(in1.get(0), false);//

        //String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        String fileContent = hl7ParsingEngineUtils.createHL7FileNslijMultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), Arrays.asList(ft1List), true);
         
        fileManipulation.writeFileToFolder(fileContent, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        Nslij HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
				
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");
		
        logger.info("*** Step 4 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		File fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        Nslij HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");  
        	
        logger.info("*** Step 4 Expected Results: - Verify that data are saved to the corresponding tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HIST.");       
        
        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");	
        
        logger.info("*** Step 5 Expected Results: - Verify that the accession is moved out of Q_OE");       
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");           
        
        logger.info("*** Step 5 Expected Results: - Verify that data are saved to ACCN properly");
        String ptDOB = hl7ParsingEngineUtils.geNewDate(25, "yyyy-MM-dd");
        //accn
        List<String> accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accession " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(12),"        Pt Home phone is saved into ACCN.");       
        assertEquals(accn.get(3),pid.get(13),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(14),"        Pt SSN is saved into ACCN.");        
        assertTrue(Integer.valueOf(accn.get(43)) > 0, "        EPI should be saved into ACCN.");// EPI is auto generated if SS#85 = 1
        //assertEquals(accn.get(32), "1", "        Patient Type should be saved into ACCN."); //Patient Type is not read/parse by Nalij parser
        
        String clnAbbrev = pv1.get(4);
        String clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        String clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID " + clnId + " should be saved into accn.fk_cln_id.");
        
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dosDt = df1.parse(accn.get(4));       
        DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(dosDt), ft1.get(0), "        DOS should be saved into ACCN."); 
        assertEquals(df2.format(dosDt), ft1.get(1), "        Final Report Date should be saved into ACCN.");        
        
        logger.info("*** Step 5 Expected Results: - Verify that data are saved to PT_CLN_LNK properly");
        //pt_cln_lnk
        String ptSeqId = accn.get(43);
        String ptClnIdInDB = daoManagerPlatform.getPtClnLnkInfoFromByPtSeqIdClnId(ptSeqId, clnIdInDB, testDb).get(1);
        assertEquals(ptClnIdInDB, pid.get(0), "        SPECIFIC_PT_ID should be saved into PT_CLN_LNK.");
        
        logger.info("*** Step 5 Expected Results: - Verify that data are saved to ACCN_PYR properly"); 
        //accn_pyr
        List<List<String>> accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 0,"        Payor should be saved into ACCN_PYR.");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        Payor " + in1.get(0) + " should be saved into ACCN_PYR.");
        assertEquals(accnPyr.get(0).get(7),in1.get(2),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(3),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(5),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr.get(0).get(18),in1.get(6),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr.get(0).get(16),in1.get(9),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(26),in1.get(11),"        SUBS_ID is saved into Db");

        logger.info("*** Step 5 Expected Results: - Verify that data are saved to ACCN_TEST properly"); 
        //accn_test
        List<String> accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into ACCN_TEST.");
        assertEquals(accnTest.get(0),ft1.get(3),"        Test " + ft1.get(3) + " should be saved into ACCN_TEST.");
        
        ArrayList<String> accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1.get(3), testDb);        
        //String accnTestSeqId = accnTestList.get(0);       
        assertEquals(accnTestList.get(3),"1","        Units is saved into Db"); //There is no unit speicify in HL7 file.
        String sysDt = timeStamp.getCurrentDate();
        assertEquals(accnTestList.get(16), sysDt,"        Final Report date " + sysDt + "should be saved into ACCN_TEST."); 
        assertEquals(accnTestList.get(4),"1","        Performing Facility is saved into Db");  
        
        logger.info("*** Step 5 Expected Results: - Verify that data are saved to ACCN_DAIG properly"); 
        //accn_daig
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "1" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
       
        logger.info("*** Step 5 Expected Results: - Verify that data are saved to ACCN_PHYS properly");        
        //accn_phys        
        String orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(3), testDb).get(0);
        String orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS.");         
       
        logger.info("*** Step 6 Actions: - Create a new Nslij HL7 file with the same Accession ID and Patient ID and updated values in all fields");
		currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = "nslij-" + currDtTime +"-update.hl7";        
        
        //String accnId, String ptId, int years, String ptSex, String ptHomePhone, String ptSSN
        pid = hl7ParsingEngineUtils.getListPIDNslij(accnId, ptId, 27, "F", "9516578854", "987654321");       
        //String ptTyp, String linkAccn, String clnAbbrev
        pv1 = hl7ParsingEngineUtils.getListPV1Nslij("O", "", "DW100");        
        //isInvalidPyr, insRelTyp
        in1 = hl7ParsingEngineUtils.getListIN1Nslij(false, "SPOUSE");         
        //boolean isProfTest, String transTyp, String testFacId, String specifiedTestAbbrev, String dos
        //Can add multiple FT1 segments
        dos = timeStamp.getCurrentDate("YYYYMMdd");
        ft1 = hl7ParsingEngineUtils.getListFT1Nslij(false, "+", "1", "BS", dos); 
        ft1List = new ArrayList<List<String>>();
        ft1List.add(ft1);        
        //pyr_abbrev, isFreeText
        dg1 = hl7ParsingEngineUtils.getListDG1Nslij(in1.get(0), false);
        //String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        fileContent = hl7ParsingEngineUtils.createHL7FileNslijMultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), Arrays.asList(ft1List), true);
         
        fileManipulation.writeFileToFolder(fileContent, filePathIn, fileName);
		
		logger.info("*** Step 6 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        Nslij HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
				
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");		
		
        logger.info("*** Step 7 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        Nslij HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");  
        	
        logger.info("*** Step 7 Expected Results: - Verify that data are saved to the corresponding tables properly");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HIST.");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");
        
        logger.info("*** Step 8 Expected Results: - Verify that the accession is moved out of Q_OE");       
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");           
        
        logger.info("*** Step 8 Expected Results: - Verify that the updated data are saved to ACCN properly");
        ptDOB = hl7ParsingEngineUtils.geNewDate(27, "yyyy-MM-dd");
        //accn
        accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accession " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(12),"        Pt Home phone is saved into ACCN.");       
        assertEquals(accn.get(3),pid.get(13),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"2","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(14),"        Pt SSN is saved into ACCN.");        
        assertTrue(Integer.valueOf(accn.get(43)) > 0, "        EPI should be saved into ACCN.");// EPI is auto generated if SS#85 = 1
        //assertEquals(accn.get(32), "1", "        Patient Type should be saved into ACCN."); //Patient Type is not read/parse by Nalij parser
        
        clnAbbrev = pv1.get(4);
        clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID " + clnId + "should be saved into accn.fk_cln_id.");
        
        df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dosDt = df1.parse(accn.get(4));       
        df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(dosDt), ft1.get(0), "        DOS should be saved into ACCN."); 
        assertEquals(df2.format(dosDt), ft1.get(1), "        Final Report Date should be saved into ACCN.");        
        
        logger.info("*** Step 8 Expected Results: - Verify that the updated data are saved to PT_CLN_LNK properly");
        //pt_cln_lnk
        ptSeqId = accn.get(43);
        ptClnIdInDB = daoManagerPlatform.getPtClnLnkInfoFromByPtSeqIdClnId(ptSeqId, clnIdInDB, testDb).get(1);
        assertEquals(ptClnIdInDB, pid.get(0), "        SPECIFIC_PT_ID should be saved into PT_CLN_LNK.");
        
        logger.info("*** Step 8 Expected Results: - Verify that the updated data are saved to ACCN_PYR properly"); 
        //accn_pyr
        accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 0,"        Payor should be saved into ACCN_PYR.");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        Payor " + in1.get(0) + " should be saved into ACCN_PYR.");
        assertEquals(accnPyr.get(0).get(7),in1.get(2),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(3),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(5),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr.get(0).get(18),in1.get(6),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr.get(0).get(16),in1.get(9),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(26),in1.get(11),"        SUBS_ID is saved into Db");

        logger.info("*** Step 8 Expected Results: - Verify that the updated data are saved to ACCN_TEST properly"); 
        //accn_test
        accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into ACCN_TEST.");
        assertEquals(accnTest.get(0),ft1.get(3),"        Test " + ft1.get(3) + " should be saved into ACCN_TEST.");
        
        accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1.get(3), testDb);        
        //String accnTestSeqId = accnTestList.get(0);       
        assertEquals(accnTestList.get(3),"1","        Units is saved into Db"); //There is no unit speicify in HL7 file.
        sysDt = timeStamp.getCurrentDate();
        assertEquals(accnTestList.get(16), sysDt,"        Final Report date " + sysDt + "should be saved into ACCN_TEST."); 
        assertEquals(accnTestList.get(4),"1","        Performing Facility is saved into Db"); 
        
        logger.info("*** Step 8 Expected Results: - Verify that the updated data are saved to ACCN_DAIG properly"); 
        //accn_daig
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "2" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag Code " + dg1.get(0) + " should be saved into ACCN_DAIG."); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
       
        logger.info("*** Step 8 Expected Results: - Verify that the updated data are saved to ACCN_PHYS properly");        
        //accn_phys        
        orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(3), testDb).get(0);
        orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS."); 
                      
        logger.info("*** Step 9 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
        
		logger.info("*** Step 10 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
	}
	
	@Test(priority = 1, description="Arx-Add and Update accn with all HL7 fields")
	@Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_493(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_493 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Step 1 Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1, 1165 = 1; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.arx.ArxHl7V231Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1165", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.arx.ArxHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
		
		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 3 Actions: - Create a ArxHl7V231 HL7 file with all fields");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "arxhl7v231-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

        String accnId = "QAAUTOHL7" + currDtTime;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        //accnId, ptSex, years, ptHomePhone, ptWorkPhone, ptMaritalStatus, String ptSSN, String ptId
        List<String>pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId);       
        //phleFacId, admSrcId
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        //isInvalidPyr, insRelTyp, eligStatus, insSex
        List<String>in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M"); 
        //guarantorHomePhone, years, guarantorSex, guarantorRelshpTyp, guarantorSSN, employerPhone, guarantorWorkPhone
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1ArxHl7V231("6197774568", 40, "M", "C", "987456789", "8586217459", "9512657113");
        //isProfTest, transTyp, unit, abnReceivedFlag, isRoundTrip, testFacId, specifiedTestAbbrev
        String dos = timeStamp.getCurrentDate("YYYYMMdd");
        List<String>ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "1", "Y", "Y", "1", "71010", dos); 
        //testSpecificQuestion, abnReceivedFlag, renalFlag, payorToBill
        List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        //mspReceivedFlag
        List<String>zxa = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        //pyr_abbrev, isFreeText
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false);
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        String content = hl7ParsingEngineUtils.createHL7FileArxHl7V231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), 
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE"), Arrays.asList(zxa), finalReportDt, true);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		File fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        GenericV231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
		
		logger.info("*** Step 4 Actions: - Insert test data in TEST_Q in DB");				
		String testId = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(ft1.get(4), testDb).get(0);		
		daoManagerPlatform.insertTestQ(testId, "1", "3", testDb);		
		
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");
		//Thread.sleep(5000);
		
        logger.info("*** Step 5 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		File fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        ArxHl7V231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");  
        		
        logger.info("*** Step 5 Expected Results: - Verify that data are saved to the corresponding tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN properly");       
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");     
        
        String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn.get(8),pid.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn.get(3),pid.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(16),"        Pt SSN is saved into ACCN.");
        
        String clnAbbrev = pv1.get(3);
        String clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        String clnIdInDB = accn.get(1);
        //assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");
        assertEquals(clnIdInDB, "0","        Client ID should be 0 in accn.fk_cln_id.");

        String currDt = timeStamp.getCurrentDate();
        ArrayList<String> accnErrList = daoManagerPlatform.getAccnErrInfoFromACCNERRByAccnIdErrCdErrDt(accnId, "1128", currDt, testDb);
        assertTrue(accnErrList.size()>0, "       Accn " + accnId + " should have INVCL Invalid Client error.");
        
        //String ptLocation = pv1.get(0);        
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created        
        String pscId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1.get(5), testDb).get(4);
        assertEquals(accn.get(49), pscId, "        PSC should be saved into ACCN."); 
        assertEquals(accn.get(56), pv1.get(6), "        Admission Source should be saved into ACCN."); 
        assertEquals(accn.get(32), pv1.get(7), "        Patient Type should be saved into ACCN.");
        
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date actDate = df1.parse(accn.get(18));       
        DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate), ft1.get(13), "        Receipt Date should be saved into ACCN."); 
        
        assertEquals(accn.get(34), ft1.get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn.get(33), ft1.get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn.get(36), ft1.get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn.get(35), "1", "        roundTrip should be saved into ACCN.");
        
        assertEquals(accn.get(48), zxa.get(5), "        Phlebotomist ID should be saved into ACCN.");
        
        String onSetDtStrInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn.get(61));
        assertEquals(onSetDtStrInDB, zxa.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn.get(62), zxa.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn.get(59), zxa.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn.get(19), zxa.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn.get(42), "1", "        MSP Received Flag should be saved into ACCN.");
        assertEquals(accn.get(63), zxa.get(15), "        Accident State should be saved into ACCN.");

        Accn accnInfo = accessionDao.getAccn(accnId);
        String accnBillingFacId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr("DS", testDb).get(4);
        //assertEquals(String.valueOf(accnInfo.getAccnBillingFacId()),  accnBillingFacId);
        assertEquals(String.valueOf(accnInfo.getAccnBillingFacId()),  "0");  //DS is Draw Station and can't be billing fac
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_V2 properly");
        String epi = pid.get(0);
        System.out.println("epi = " + epi);//debug info
        List<String> ptV2List = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi, testDb);
        assertTrue(ptV2List.size()>0,"        Patient EPI " + epi + " should be saved into PT_V2.");  
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_PYR properly"); 
        //accn_pyr
        List<List<String>> accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr.get(0).get(7),in1.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(13),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr.get(0).get(18),in1.get(14),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr.get(0).get(16),in1.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(25),in1.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(20), "1","        INS_SEX is saved into Db"); //1 = M (Male)
        assertEquals(accnPyr.get(0).get(26),in1.get(23),"        SUBS_ID is saved into Db");        
        //assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created
        
        //String eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        //assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created
        
        String insuredDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(19));
        assertEquals(insuredDOBInDB, in1.get(12),"        INS_DOB is saved into Db");
        
        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr.get(1).get(7).toUpperCase(),gt1.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");       
        assertEquals(accnPyr.get(1).get(8).toUpperCase(),gt1.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(9).toUpperCase(),gt1.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(18).toUpperCase(),gt1.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");

        String guarantorDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(1).get(19));
        assertEquals(guarantorDOBInDB, gt1.get(8),"        Guarantor's DOB is saved into Db");
        
        assertEquals(accnPyr.get(1).get(20), "1","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr.get(1).get(22), gt1.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr.get(1).get(27), gt1.get(7),"        Guarantor's Home Phone is saved into Db");  
        ///assertEquals(accnPyr.get(1).get(29), gt1.get(21),"        Guarantor's Work Phone is saved into Db");//not working
        
        assertEquals(accnPyr.get(1).get(11), gt1.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr.get(1).get(12), gt1.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(23), gt1.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr.get(1).get(24), gt1.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr.get(1).get(28), gt1.get(19),"        Employer's Phone number is saved into Db");        
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST properly"); 
        //accn_test
        List<String> accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");
        assertEquals(accnTest.get(0),ft1.get(4),"        Test is saved into Db");
        
        ArrayList<String> accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1.get(4), testDb);        
        String accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(6),ft1.get(19),"        Mod1 is saved into Db");
        
        assertEquals(accnTestList.get(3),ft1.get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1.get(5),"        ALT_TEST_NAME is saved into Db");        
        assertEquals(accnTestList.get(13),ft1.get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),ft1.get(0),"        LIS Trace Id is saved into Db");
        //assertEquals(accnTestList.get(4),"1","        Performing Facility is saved into Db");        
        
        assertEquals(accnTestList.get(7),zxt.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(8), "1","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList.get(9), "1","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList.get(10), zxt.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList.get(11), zxt.get(5),"        B_RENAL is saved into Db");               
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_DAIG properly"); 
        //accn_daig
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "1" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
                
        //test level diag
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "2" ,testDb);
        assertEquals(accnDiag.get(2), ft1.get(11),"        Test level Diag Code " + ft1.get(11) + " should be saved into ACCN_DIAG.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CLN_Q properly"); 
        //accn_cln_q        
        List<String> accnClnQ = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId,testDb); 
        assertTrue(accnClnQ.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ.get(0), zxa.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa.get(0) + " should be saved.");
        assertEquals(accnClnQ.get(1), zxa.get(4),"        ACCN_CLN_Q.RESPNS " + zxa.get(4) + " should be saved.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CNTCT properly"); 
        //accn_cntct
        List<String> accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is null", testDb);
        assertTrue(accnCntct.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct.get(0), zxa.get(3),"        CNTCT_INFO " + zxa.get(3) + " should be saved into ACCN_CNTCT.");
       
        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is not null", testDb);
        assertEquals(accnCntct.get(3), zxa.get(11),"       " + zxa.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");
                
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_CLINTRL properly"); 
        //accn_clintrl
        List<String> accnClintrl = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId,testDb);
        assertTrue(accnClintrl.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl.get(0),zxa.get(4),"        CLINTRL_ENCNTR " + zxa.get(4) + " should be saved into ACCN_CLINTRL.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_DAIL properly"); 
        //accn_dail
        List<String> accnDial = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId,testDb);
        assertTrue(accnDial.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial.get(1), zxa.get(6),"        FK_DIAL_TYP_ID " + zxa.get(6) + " should be saved into ACCN_DIAL.");        
        
        String firstDailDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial.get(3));
        assertEquals(firstDailDtInDB, zxa.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL.");       
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_TEST_Q properly"); 
        //accn_test_q
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");
        
        logger.info("*** Step 6 Expected Results: - Verify that data are saved to PT_PYR_V2 properly"); 
        //pt_pyr_v2
        String ptSeqId = accn.get(43);
        String ptPyrEffDt = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId, "1", testDb).get(28);
        String ptPyrEffDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt);        
        assertEquals(ptPyrEffDtInDB, in1.get(9), "        EFF_DT should be saved into PT_PYR_V2.");

        logger.info("*** Step 6 Expected Results: - Verify that data are saved to ACCN_PHYS properly");        
        //accn_phys
        String renderingPhysSeqIdInDB = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId, accnTestSeqId, testDb).get(0);        
        String renderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1.get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB, renderingPhysSeqId, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");
        
        String referingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(4), testDb).get(0);
        String referingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB, referingPhysSeqId, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");       
        
        String orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(2), testDb).get(0);
        String orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS."); 
               
        logger.info("*** Step 7 Actions: - Create a new ArxHl7V231 HL7 file with the same Accession ID and Patient ID and updated values in all fields");
		currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = "arxhl7v231-" + currDtTime +"-update.hl7";
        
        //accnId, ptSex, years, ptHomePhone, ptWorkPhone, ptMaritalStatus, String ptSSN, String ptId
        pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "F", 16, "9516578854", "7607353125", "S", "987654321", ptId);     
        //phleFacId, admSrcId
        pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("WWW", "3");
        //isInvalidPyr, insRelTyp, eligStatus, insSex
        in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "N", "F");
        //guarantorHomePhone, years, guarantorSex, guarantorRelshpTyp, guarantorSSN, employerPhone
        gt1 = hl7ParsingEngineUtils.getListGT1ArxHl7V231("9517774568", 38, "F", "C", "127456789", "6196217459", "9514665138");
        //isProfTest, transTyp, unit, abnReceivedFlag, isRoundTrip, testFacId, specifiedTestAbbrev
        ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "2", "N", "N", "1", "BS", dos); //"D" - Debit
        //testSpecificQuestion, abnReceivedFlag, renalFlag, payorToBill
        zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "N", "N", "");
        //mspReceivedFlag
        zxa = hl7ParsingEngineUtils.getListZXAGenericV231("N");
        //pyr_abbrev, isFreeText = false
        dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false); 
        finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
 
        content = hl7ParsingEngineUtils.createHL7FileArxHl7V231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1), 
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE"), Arrays.asList(zxa), finalReportDt, true);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName);
		
		logger.info("*** Step 7 Expected Results: - Verify that the new HL7 file is generated in the in folder");
		fIncoming = new File(filePathIn + fileName);
		System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        ArxHl7V231 HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");
		
		logger.info("*** Step 8 Actions: - Insert test data in TEST_Q in DB");				
		testId = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(ft1.get(4), testDb).get(0);		
		daoManagerPlatform.insertTestQ(testId, "1", "3", testDb);		
		
		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");	
		
		//Thread.sleep(5000);
		
        logger.info("*** Step 9 Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		fArchive = new File(filePathArchive + fileName);
		System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        ArxHl7V231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");  
        		
        logger.info("*** Step 9 Expected Results: - Verify that data are saved to the corresponding tables properly");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN properly");       
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");     
        
        ptDOB = hl7ParsingEngineUtils.geNewDate(16, "yyyy-MM-dd");
        //accn
        accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(6),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn.get(8),pid.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn.get(3),pid.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"2","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(16),"        Pt SSN is saved into ACCN.");
        
        clnAbbrev = pv1.get(3);
        clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        clnIdInDB = accn.get(1);
        //assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");
        assertEquals(clnIdInDB, "0","        Client ID should be 0 in accn.fk_cln_id.");
        
        //String ptLocation = pv1.get(0);        
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created        
        pscId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1.get(5), testDb).get(4);
        assertEquals(accn.get(49), pscId, "        PSC should be saved into ACCN."); 
        //////////////assertEquals(accn.get(56), pv1.get(6), "        Admission Source should be saved into ACCN."); //Mars also not updating the accn.FK_ADMISSION_SRC_ID to '3'!
        assertEquals(accn.get(32), pv1.get(7), "        Patient Type should be saved into ACCN.");
        
        df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        actDate = df1.parse(accn.get(18));       
        df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate), ft1.get(13), "        Receipt Date should be saved into ACCN."); 
        
        assertEquals(accn.get(34), ft1.get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn.get(33), ft1.get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn.get(36), ft1.get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn.get(35), "1", "        roundTrip should be saved into ACCN."); //Mars also not updating the accn.B_ROUND_TRIP to '0'!
        
        assertEquals(accn.get(48), zxa.get(5), "        Phlebotomist ID should be saved into ACCN.");
        
        onSetDtStrInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn.get(61));
        assertEquals(onSetDtStrInDB, zxa.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn.get(62), zxa.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn.get(59), zxa.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn.get(19), zxa.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn.get(42), "1", "        MSP Received Flag should be saved into ACCN."); //Mars also not updating the accn.B_MSP_FORM to '0'!
        assertEquals(accn.get(63), zxa.get(15), "        Accident State should be saved into ACCN.");        
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to PT_V2 properly");
        epi = pid.get(0);
        System.out.println("epi = " + epi);//debug info
        ptV2List = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi, testDb);
        assertTrue(ptV2List.size()>0,"        Patient EPI " + epi + " should be saved into PT_V2.");  
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_PYR properly"); 
        //accn_pyr
        accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr.get(0).get(7),in1.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(13),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr.get(0).get(18),in1.get(14),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr.get(0).get(16),in1.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(25),in1.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(20), "2","        INS_SEX is saved into Db"); //2 = F (Female)
        assertEquals(accnPyr.get(0).get(26),in1.get(23),"        SUBS_ID is saved into Db");
        //assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created
        
        //String eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        //assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created
        
        insuredDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(19));
        assertEquals(insuredDOBInDB, in1.get(12),"        INS_DOB is saved into Db");
        
        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr.get(1).get(7).toUpperCase(),gt1.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");       
        assertEquals(accnPyr.get(1).get(8).toUpperCase(),gt1.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");        
        
        assertEquals(accnPyr.get(1).get(9).toUpperCase(),gt1.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(18).toUpperCase(),gt1.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");
        
        guarantorDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(1).get(19));
        assertEquals(guarantorDOBInDB, gt1.get(8),"        Guarantor's DOB is saved into Db");
        
        assertEquals(accnPyr.get(1).get(20), "2","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr.get(1).get(22), gt1.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr.get(1).get(27), gt1.get(7),"        Guarantor's Home Phone is saved into Db");    
        ////assertEquals(accnPyr.get(1).get(29), gt1.get(21),"        Guarantor's Work Phone is saved into Db");//not working
        
        assertEquals(accnPyr.get(1).get(11), gt1.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr.get(1).get(12), gt1.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(23), gt1.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr.get(1).get(24), gt1.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr.get(1).get(28), gt1.get(19),"        Employer's Phone number is saved into Db");        
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_TEST properly"); 
        //accn_test
        accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");
        assertEquals(accnTest.get(0),ft1.get(4),"        Test is saved into Db");
        
        accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1.get(4), testDb);        
        accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(6),ft1.get(19),"        Mod1 is saved into Db");
        
        assertEquals(accnTestList.get(3),ft1.get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1.get(5),"        ALT_TEST_NAME is saved into Db");        
        assertEquals(accnTestList.get(13),ft1.get(8),"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),ft1.get(0),"        LIS Trace Id is saved into Db");       
        //assertEquals(accnTestList.get(4),"1","        Performing Facility is saved into Db");
        
        assertEquals(accnTestList.get(7),zxt.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(8), "0","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList.get(9), "0","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList.get(10), zxt.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList.get(11), zxt.get(5),"        B_RENAL is saved into Db");               
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_DAIG properly"); 
        //accn_daig
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "3" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");
                
        //test level diag
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "4" ,testDb);
        assertEquals(accnDiag.get(2), ft1.get(11),"        Test level Diag Code " + ft1.get(11) + " should be saved into ACCN_DIAG.");
                
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_CLN_Q properly"); 
        //accn_cln_q        
        accnClnQ = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId,testDb); 
        assertTrue(accnClnQ.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ.get(0), zxa.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa.get(0) + " should be saved.");
        assertEquals(accnClnQ.get(1), zxa.get(4),"        ACCN_CLN_Q.RESPNS " + zxa.get(4) + " should be saved.");
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_CNTCT properly"); 
        //accn_cntct
        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is null order by pk_cntct_seq desc", testDb);
        assertTrue(accnCntct.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct.get(0), zxa.get(3),"        CNTCT_INFO " + zxa.get(3) + " should be saved into ACCN_CNTCT.");
       
        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is not null order by pk_cntct_seq desc", testDb);
        assertEquals(accnCntct.get(3), zxa.get(11),"       " + zxa.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");
                
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_CLINTRL properly"); 
        //accn_clintrl
        accnClintrl = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId,testDb);
        assertTrue(accnClintrl.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl.get(0),zxa.get(4),"        CLINTRL_ENCNTR " + zxa.get(4) + " should be saved into ACCN_CLINTRL.");
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_DAIL properly"); 
        //accn_dail
        accnDial = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId,testDb);
        assertTrue(accnDial.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial.get(1), zxa.get(6),"        FK_DIAL_TYP_ID " + zxa.get(6) + " should be saved into ACCN_DIAL.");        
        
        firstDailDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial.get(3));
        assertEquals(firstDailDtInDB, zxa.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL.");       
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_TEST_Q properly"); 
        //accn_test_q
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");
        
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to PT_PYR_V2 properly"); 
        //pt_pyr_v2
        ptSeqId = accn.get(43);
        ptPyrEffDt = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId, "1", testDb).get(28);
        ptPyrEffDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt);        
        assertEquals(ptPyrEffDtInDB, in1.get(9), "        EFF_DT should be saved into PT_PYR_V2.");
 
        logger.info("*** Step 10 Expected Results: - Verify that updated data are saved to ACCN_PHYS properly");        
        //accn_phys
        renderingPhysSeqIdInDB = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId, accnTestSeqId, testDb).get(0);        
        renderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1.get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB, renderingPhysSeqId, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");
        
        referingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(4), testDb).get(0);
        referingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB, referingPhysSeqId, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");       
        
        orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(2), testDb).get(0);
        orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS.");        
                
        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1165", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
        daoManagerPlatform.deleteAccnTestQByAccnId(accnId, testDb);
        daoManagerPlatform.deleteTestQByTestId(testId, testDb);
        
		logger.info("*** Step 12 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
	}

	@Test(priority = 1, description="Allina-Add and Update accn with all HL7 fields by processing ORM, ADT, XTEND files")
	@Parameters({"email", "password","eType1", "eType2", "eType3", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_491(String email, String password, String eType1, String eType2, String eType3, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_491 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; 1141 = 1; 1162 = 1 Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.allina.AllinaHl7Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1141", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1162", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.allina.AllinaHl7Parser'","PK_TASK_TYP_ID", "18", testDb);

		logger.info("*** Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Actions: - Generate an Allina ORM HL7 file with all fields and place it to in folder");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName1 = "allina-orm-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

		String identifier = randomCharacter.getNonZeroRandomNumericString(9);
        String accnId = "X" + identifier;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        
        List<String>pid = hl7ParsingEngineUtils.getListPIDAllinaORM("M", 15, "6196578854", "123456789", ptId);           
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1AllinaORM("VC", identifier); //If PtTyp is 'VC' , sets Cln Pyr ID from SS #74 (Client Payor)       
        List<String>orc = hl7ParsingEngineUtils.getListORCAllinaORM(identifier);//identifier=xRefId
        List<String>obr = hl7ParsingEngineUtils.getListOBRAllinaORM();

        String pyrAbbrev = payorDao.getThirdPartyPyrFromPYR().pyrAbbrv;
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(pyrAbbrev, false);

        String finalReportDtTime = timeStamp.getCurrentDate("YYYYMMddHHmmss");
        String finalReportDt = timeStamp.getCurrentDate("MM/dd/yyyy");
 
        String content = hl7ParsingEngineUtils.createHL7FileAllinaORMMultiAccn(finalReportDtTime, Arrays.asList(pid), Arrays.asList(pv1), Arrays.asList(orc), 
                Arrays.asList(obr), Arrays.asList(dg1), finalReportDtTime, ptId);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName1);
		
		logger.info("*** Expected Results: - Verify that the new HL7 file is in the in folder");
		File fIncoming = new File(filePathIn + fileName1);
		//System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        Allina ORM HL7 file: " + fileName1 + " should be generated under " + filePathIn + " folder.");

		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");			
		
        logger.info("*** Expected Results: - Verify that the HL7 ORM file is processed and moved to the archive folder");
		File fArchive = new File(filePathArchive + fileName1);
		//System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        Allina ORM HL7 file: " + fileName1 + " should be processed and moved to " + filePathArchive + " folder.");  
      	
        logger.info("*** Expected Results: - Verify that data are saved to the corresponding Q_OE tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName1, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");

		logger.info("*** Expected Results: - Verify that the accession is processed and being moved out of Q_OE");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");     
        		
        logger.info("*** Expected Results: - Verify that data are saved to ACCN properly");   
        String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(0),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(1),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(5),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(6),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(9),"        Pt zip is saved into ACCN.");                    
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(12),"        Pt SSN is saved into ACCN.");
        
        assertEquals(accn.get(38),pv1.get(12),"        MRO is saved into ACCN.");
        assertEquals(accn.get(3),pv1.get(12),"        Requisition ID is saved into ACCN.");
        
        String clnAbbrev = orc.get(16);
        String clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        String clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");        

        logger.info("*** Expected Results: - Verify that data (Client Payor 'C') are saved to ACCN_PYR properly"); 
        //accn_pyr
        List<List<String>> accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 0,"        Data is saved into Db");
        
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb), "C","        Client Payor - 'C' is saved into Db.");        
        
        logger.info("*** Expected Results: - Verify that data (INVTEST) are saved to ACCN_TEST properly"); 
        //accn_test
        List<String> accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");       
        
        ArrayList<String> accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, accnTest.get(0), testDb);  
        assertEquals(accnTestList.get(7),obr.get(20).toUpperCase().trim(),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(16), finalReportDt,"        Final Report Date is saved into Db.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_DAIG properly"); 
        //accn_daig
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "1" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_PHYS properly");        
        //accn_phys
        String orderingPhysSeqId = daoManagerPlatform.getPhysSeqIdFromPHYSByAccnId(accnId, testDb);
        String orderingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysSeqIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS."); 

        logger.info("*** Actions: - Generate an HL7 ADT file (update) in all fields and place it in the in folder");
		currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName2 = "allina-adt-" + currDtTime +".hl7";
        String subsIdIN1 = randomCharacter.getNonZeroRandomNumericString(8);        
        
        List<String>evn = hl7ParsingEngineUtils.getListEVNsAllinaADT();
        pid = hl7ParsingEngineUtils.getListPIDAllinaORM("F", 16, "9516578854", "987654321", ptId);        
        pv1 = hl7ParsingEngineUtils.getListPV1AllinaADT(identifier, "3", "OP");
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1AllinaADT("9517774568", 38, "S", "605410547");
        List<String>in1 = hl7ParsingEngineUtils.getListIN1AllinaADT(false, "C", "Eligible", "1", subsIdIN1);
        List<String>in2 = hl7ParsingEngineUtils.getListIN2AllinaADT();
        List<String>acc = hl7ParsingEngineUtils.getListACCAllinaADT(); 
        finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");        
       
        //createHL7FileAllinaADTMultiAccn
        content = hl7ParsingEngineUtils.createHL7FileAllinaADTMultiAccn(currDtTime, Arrays.asList(evn), Arrays.asList(pid), Arrays.asList(pv1), Arrays.asList(gt1),Arrays.asList(in1), Arrays.asList(in2), Arrays.asList(acc), "A08");
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName2);
		
		logger.info("*** Expected Results: - Verify that the new Allina ADT file is generated in the in folder");
		fIncoming = new File(filePathIn + fileName2);
		//System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        Allina ADT HL7 file: " + fileName2 + " should be generated under " + filePathIn + " folder.");

		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for xRefId="+identifier);
        Assert.assertTrue(waitForHL7EngineToCreateQoebyXref(identifier, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");


        logger.info("*** Expected Results: - Verify that the Allina ADT file is processed and moved to the archive folder");
		fArchive = new File(filePathArchive + fileName2);
		//System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        Allina ADT HL7 file: " + fileName2 + " should be processed and moved to " + filePathArchive + " folder.");  
        		
        logger.info("*** Expected Results: - Verify that data are saved to the corresponding Q_OE tables properly");
        
        List<QOe> qOeList = rpmDao.getQOeByXrefId(testDb, identifier);
        assertEquals(qOeList.get(0).xrefId, identifier, "        Q_OE.XREF_ID = " + identifier);
        
        count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(qOeList.get(0).seq, testDb);        
        lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName2, testDb);       
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HIST");      
        assertEquals(qOeList.get(0).xrefId, identifier, "        Q_OE.XREF_ID = " + identifier);
        
        logger.info("*** Actions: - Run PF-OE Posting ADT Engine");		
		
        Assert.assertTrue(accessionDao.waitForRecordToBeProcessedByADTMatchEngine(identifier, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");
        logger.info("*** Expected Results: - Verify that the Accession is processed and moved out of Q_OE");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");         
        
        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN properly");           
        ptDOB = hl7ParsingEngineUtils.geNewDate(16, "yyyy-MM-dd");
        //accn
        accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(0),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(1) + " " + pid.get(2),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(5),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(6),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(9),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(11),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn.get(0),"2","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(12),"        Pt SSN is saved into ACCN.");
         
        assertEquals(accn.get(56), pv1.get(6), "        Admission Source should be saved into ACCN.");
        assertEquals(accn.get(37), pv1.get(11), "        Patient Status should be saved into ACCN.");
       
        DateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date admitDtInPV1 = df1.parse(pv1.get(9));       
        DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");   
        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date admitDtInDB = df3.parse(accn.get(40));
        assertEquals(df2.format(admitDtInDB), df2.format(admitDtInPV1), "        ADMISSION_DT should be saved into ACCN.");
                      
        Date dischargeDtInPV1 = df1.parse(pv1.get(10));   
        Date dischargeDtInDB = df3.parse(accn.get(41));
        assertEquals(df2.format(dischargeDtInDB), df2.format(dischargeDtInPV1), "        DISCHARGE_DT should be saved into ACCN.");
         
        assertEquals(accn.get(63), acc.get(0), "        Accident State should be saved into ACCN.");        
        
        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_PYR properly"); 
        //accn_pyr
        accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 0,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr.get(0).get(7),in1.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(11),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(13),"        INS_ADDR1 is saved into Db");     
        assertEquals(accnPyr.get(0).get(18),in1.get(14),"        INS_ADDR2 is saved into Db"); 
        assertEquals(accnPyr.get(0).get(16),in1.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(25),in1.get(8),"        GRP_ID is saved into Db");        
        assertEquals(accnPyr.get(0).get(26),in1.get(20),"        SUBS_ID is saved into Db");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_PHYS properly");        
        //accn_phys
        orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(12), testDb).get(0);
        String orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS.");      

        logger.info("*** Actions: - Generate an Allina XTEND HL7 file with all fields and place it in the in folder");
        currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileName3 = "allina-xtend-" + currDtTime +".hl7";
        String dos = finalReportDt;

        pid = hl7ParsingEngineUtils.getListPIDAllinaXTEND("M", 18, "112233445", identifier, ptId);
        pv1 = hl7ParsingEngineUtils.getListPV1AllinaXTEND(identifier);
        List<String>ft1 = hl7ParsingEngineUtils.getListFT1AllinaXTEND(false,"D","ACME","71010",dos,"","GA");
        
        //createHL7FileAllinaXTENDMultiAccn
        content = hl7ParsingEngineUtils.createHL7FileAllinaXTENDMultiAccn(currDtTime,Arrays.asList(pid),Arrays.asList(pv1),Arrays.asList(ft1),"P05");        
        
        fileManipulation.writeFileToFolder(content, filePathIn, fileName3);
		
		logger.info("*** Expected Results: - Verify that the new HL7 file is generated in the in folder");
		fIncoming = new File(filePathIn + fileName3);
		//System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        Allina XTEND HL7 file: " + fileName3 + " should be generated under " + filePathIn + " folder.");

		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");				
		
        logger.info("*** Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
		fArchive = new File(filePathArchive + fileName3);
		//System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        Allina XTEND HL7 file: " + fileName3 + " should be processed and moved to " + filePathArchive + " folder.");  
      	
        logger.info("*** Expected Results: - Verify that data are saved to the corresponding Q_OE tables properly");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName3, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");   
        
        qOeList = rpmDao.getQOeByXrefId(testDb, identifier);
        assertEquals(qOeList.get(0).xrefId, identifier, "        Q_OE.XREF_ID = " + identifier);
        assertEquals(qOeList.get(0).source, "HL7BIL", "        Q_OE.SOURCE = " + "HL7BIL");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");
		logger.info("*** Expected Results: - Verify that the accession is processed and being moved out of Q_OE");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");  
        
        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN properly");      
        
        ptDOB = hl7ParsingEngineUtils.geNewDate(18, "yyyy-MM-dd");
        //accn
        accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3) + " " + pid.get(4),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(7),"        Pt SSN is saved into ACCN.");
        
        clnAbbrev = ft1.get(6);
        clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");
        
        logger.info("*** Expected Results: - Verify that data are saved to PT_V2 properly");
        String epi = pid.get(1);
        //System.out.println("epi = " + epi);//debug info
        List<String> ptV2List = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi, testDb);
        assertTrue(ptV2List.size()>0,"        Patient EPI " + epi + " should be saved into PT_V2.");
        
        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_PHYS properly");        
        //accn_phys
        orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(6), testDb).get(0);
        orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS."); 
        
        logger.info("*** Expected Results: - Verify that data are saved to ACCN_TEST properly"); 
        //accn_test
        accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");        
        
        accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, accnTest.get(0), testDb);         
        assertEquals(accnTestList.get(6),ft1.get(8),"        Mod1 is saved into Db");
        assertEquals(accnTestList.get(4),"1","        Performing Facility is saved into Db");       
        //assertEquals(accnTestList.get(16), finalReportDt,"        Final Report Date is saved into Db.");
        assertEquals(accnTestList.get(9), "1","        B_ABN_REC is saved into Db");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_DAIG properly");                 
        //test level diag
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "2" ,testDb);
        assertEquals(accnDiag.get(2), ft1.get(7),"        Test level Diag Code " + ft1.get(7) + " should be saved into ACCN_DIAG.");
             
        logger.info("*** Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName1);
        fileManipulation.deleteFile(filePathArchive, fileName2);
        fileManipulation.deleteFile(filePathArchive, fileName3);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
         
		logger.info("*** Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
	}	

	@Test(priority = 1, description="Allina-ADT PV1.2 (Patient Type) = 'IP'")
	@Parameters({"email", "password","eType1", "eType2", "eType3", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_555(String email, String password, String eType1, String eType2, String eType3, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_555 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; 1141 = 1; 1162 = 1 Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.allina.AllinaHl7Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1141", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1162", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.allina.AllinaHl7Parser'","PK_TASK_TYP_ID", "18", testDb);

		logger.info("*** Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Actions: - Generate an Allina ORM HL7 file with all fields and place it to in folder");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName1 = "allina-orm-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

		String identifier = randomCharacter.getNonZeroRandomNumericString(9);
        String accnId = "X" + identifier;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        
        List<String>pid = hl7ParsingEngineUtils.getListPIDAllinaORM("M", 15, "6196578854", "123456789", ptId);           
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1AllinaORM("VC", identifier); //If PtTyp is 'VC' , sets Cln Pyr ID from SS #74 (Client Payor)       
        List<String>orc = hl7ParsingEngineUtils.getListORCAllinaORM(identifier);//identifier=xRefId
        List<String>obr = hl7ParsingEngineUtils.getListOBRAllinaORM();

        String pyrAbbrev = payorDao.getThirdPartyPyrFromPYR().pyrAbbrv;
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(pyrAbbrev, false);
        String finalReportDtTime = timeStamp.getCurrentDate("YYYYMMddHHmmss");
        String finalReportDt = timeStamp.getCurrentDate("MM/dd/yyyy");
 
        String content = hl7ParsingEngineUtils.createHL7FileAllinaORMMultiAccn(finalReportDtTime, Arrays.asList(pid), Arrays.asList(pv1), Arrays.asList(orc), 
                Arrays.asList(obr), Arrays.asList(dg1), finalReportDtTime, ptId);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName1);
		
		logger.info("*** Expected Results: - Verify that the new HL7 file is in the in folder");
		File fIncoming = new File(filePathIn + fileName1);
		Assert.assertTrue(isFileExists(fIncoming, 5), "        Allina ORM HL7 file: " + fileName1 + " should be generated under " + filePathIn + " folder.");

		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");			
		
        logger.info("*** Expected Results: - Verify that the HL7 ORM file is processed and moved to the archive folder");
		File fArchive = new File(filePathArchive + fileName1);
		assertTrue(isFileExists(fArchive, 5), "        Allina ORM HL7 file: " + fileName1 + " should be processed and moved to " + filePathArchive + " folder.");  
      	
        logger.info("*** Expected Results: - Verify that data are saved to the corresponding Q_OE tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName1, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");
		
		logger.info("*** Expected Results: - Verify that the accession is processed and being moved out of Q_OE");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");     
        		
        logger.info("*** Expected Results: - Verify that data are saved to ACCN properly");   
        String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(0),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(1),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(5),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(6),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(9),"        Pt zip is saved into ACCN.");                    
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(12),"        Pt SSN is saved into ACCN.");
        
        assertEquals(accn.get(38),pv1.get(12),"        MRO is saved into ACCN.");
        assertEquals(accn.get(3),pv1.get(12),"        Requisition ID is saved into ACCN.");
        
        String clnAbbrev = orc.get(16);
        String clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        String clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");        

        logger.info("*** Expected Results: - Verify that data (Client Payor 'C') are saved to ACCN_PYR properly"); 
        //accn_pyr
        List<List<String>> accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 0,"        Data is saved into Db");
        
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb), "C","        Client Payor - 'C' is saved into Db.");        
        
        logger.info("*** Expected Results: - Verify that data (INVTEST) are saved to ACCN_TEST properly"); 
        //accn_test
        List<String> accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");       
        
        ArrayList<String> accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, accnTest.get(0), testDb);  
        assertEquals(accnTestList.get(7),obr.get(20).toUpperCase().trim(),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(16), finalReportDt,"        Final Report Date is saved into Db.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_DAIG properly"); 
        //accn_daig
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "1" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_PHYS properly");        
        //accn_phys
        String orderingPhysSeqId = daoManagerPlatform.getPhysSeqIdFromPHYSByAccnId(accnId, testDb);
        String orderingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysSeqIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS."); 

        logger.info("*** Actions: - Generate an HL7 ADT file (update) with all fields and PV1.2 = 'IP' and place it in the in folder");
		currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName2 = "allina-adt-" + currDtTime +".hl7";
        String subsIdIN1 = randomCharacter.getNonZeroRandomNumericString(8);        
        
        List<String>evn = hl7ParsingEngineUtils.getListEVNsAllinaADT();
        pid = hl7ParsingEngineUtils.getListPIDAllinaORM("F", 16, "9516578854", "987654321", ptId);        
        pv1 = hl7ParsingEngineUtils.getListPV1AllinaADT(identifier, "3", "IP");
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1AllinaADT("9517774568", 38, "S", "605410547");
        List<String>in1 = hl7ParsingEngineUtils.getListIN1AllinaADT(false, "C", "Eligible", "1", subsIdIN1);
        List<String>in2 = hl7ParsingEngineUtils.getListIN2AllinaADT();
        List<String>acc = hl7ParsingEngineUtils.getListACCAllinaADT(); 
        finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");        
       
        //createHL7FileAllinaADTMultiAccn
        content = hl7ParsingEngineUtils.createHL7FileAllinaADTMultiAccn(currDtTime, Arrays.asList(evn), Arrays.asList(pid), Arrays.asList(pv1), Arrays.asList(gt1),Arrays.asList(in1), Arrays.asList(in2), Arrays.asList(acc), "A08");
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName2);
		
		logger.info("*** Expected Results: - Verify that the new Allina ADT file is generated in the in folder");
		fIncoming = new File(filePathIn + fileName2);
		Assert.assertTrue(isFileExists(fIncoming, 5), "        Allina ADT HL7 file: " + fileName2 + " should be generated under " + filePathIn + " folder.");

		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the HospitalAdmit record for subsId="+subsIdIN1);

        long maxTime = (QUEUE_WAIT_TIME_MS*2) + System.currentTimeMillis();
        while (((rpmDao.getHospitalAdmitCheckBySubsId(testDb, subsIdIN1)).size()<1) && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for PF-HL7 Engine to create HospitalAdmit record for subsId=" + subsIdIN1 + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(maxTime - System.currentTimeMillis()) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
        }
        logger.info("*** Expected Results: - Verify that the Allina ADT file is processed and moved to the archive folder");
		fArchive = new File(filePathArchive + fileName2);
		assertTrue(isFileExists(fArchive, 5), "        Allina ADT HL7 file: " + fileName2 + " should be processed and moved to " + filePathArchive + " folder.");         

		logger.info("*** Expected Results: - Verify that data are saved to HOSPITAL_ADMIT_CHECK table properly");
        
        List<HospitalAdmitCheck> hospitalAdmitChkList = rpmDao.getHospitalAdmitCheckBySubsId(testDb, subsIdIN1);
        
        assertEquals(String.valueOf(hospitalAdmitChkList.get(0).ssn), "987654321", "        HOSPITAL_ADMIT_CHECK.SSN = '987654321'");	        
        ptDOB = hl7ParsingEngineUtils.geNewDate(16, "yyyy-MM-dd");
        assertEquals(hospitalAdmitChkList.get(0).ptLNm,pid.get(0),"        Pt lName is saved into HOSPITAL_ADMIT_CHECK.");
        assertEquals(hospitalAdmitChkList.get(0).ptFNm,pid.get(1) + " " + pid.get(2),"        Pt fName is saved into HOSPITAL_ADMIT_CHECK.");
        
        DateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Date admitDtInPV1 = df1.parse(pv1.get(9));
        assertEquals(hospitalAdmitChkList.get(0).admitDt.toString(), df2.format(admitDtInPV1).toString(),"        ADMIT_DT is saved into HOSPITAL_ADMIT_CHECK."); 
                      
        Date dischargeDtInPV1 = df1.parse(pv1.get(10));           
        assertEquals(hospitalAdmitChkList.get(0).dischargeDt.toString(), df2.format(dischargeDtInPV1).toString(), "        DISCHARGE_DT should be saved into HOSPITAL_ADMIT_CHECK.");
               
        Date ptDobInFile = df2.parse(ptDOB);
        assertEquals(hospitalAdmitChkList.get(0).dob.toString(), df2.format(ptDobInFile).toString(), "        DOB should be saved into HOSPITAL_ADMIT_CHECK.");
  
        logger.info("*** Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName1);
        fileManipulation.deleteFile(filePathArchive, fileName2);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
        rpmDao.deleteHospitalAdmitCheckBySubsId(testDb, subsIdIN1);
         
		logger.info("*** Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
	}		
	
	@Test(priority = 1, description="Allina-ADT PV1.2 (Patient Type) = 'HO'")
	@Parameters({"email", "password","eType1", "eType2", "eType3", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_556(String email, String password, String eType1, String eType2, String eType3, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - testPFER_556 ======");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);	
		hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
		
		
		logger.info("*** Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; 1141 = 1; 1162 = 1 Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.allina.AllinaHl7Parser'");
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1141", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1162", testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.allina.AllinaHl7Parser'","PK_TASK_TYP_ID", "18", testDb);

		logger.info("*** Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Actions: - Generate an Allina ORM HL7 file with all fields and place it to in folder");
		//File name
		String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName1 = "allina-orm-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		String filePathIn = dirBase + "in" + File.separator;
		String filePathArchive = dirBase + "archive" + File.separator;        

		String identifier = randomCharacter.getNonZeroRandomNumericString(9);
        String accnId = "X" + identifier;       
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        
        List<String>pid = hl7ParsingEngineUtils.getListPIDAllinaORM("M", 15, "6196578854", "123456789", ptId);           
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1AllinaORM("VC", identifier); //If PtTyp is 'VC' , sets Cln Pyr ID from SS #74 (Client Payor)       
        List<String>orc = hl7ParsingEngineUtils.getListORCAllinaORM(identifier);//identifier=xRefId
        List<String>obr = hl7ParsingEngineUtils.getListOBRAllinaORM();

        String pyrAbbrev = payorDao.getThirdPartyPyrFromPYR().pyrAbbrv;
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(pyrAbbrev, false);
        String finalReportDtTime = timeStamp.getCurrentDate("YYYYMMddHHmmss");
        String finalReportDt = timeStamp.getCurrentDate("MM/dd/yyyy");
 
        String content = hl7ParsingEngineUtils.createHL7FileAllinaORMMultiAccn(finalReportDtTime, Arrays.asList(pid), Arrays.asList(pv1), Arrays.asList(orc), 
                Arrays.asList(obr), Arrays.asList(dg1), finalReportDtTime, ptId);
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName1);
		
		logger.info("*** Expected Results: - Verify that the new HL7 file is in the in folder");
		File fIncoming = new File(filePathIn + fileName1);
		//System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        Allina ORM HL7 file: " + fileName1 + " should be generated under " + filePathIn + " folder.");

		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");			
		
        logger.info("*** Expected Results: - Verify that the HL7 ORM file is processed and moved to the archive folder");
		File fArchive = new File(filePathArchive + fileName1);
		//System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        Allina ORM HL7 file: " + fileName1 + " should be processed and moved to " + filePathArchive + " folder.");  
      	
        logger.info("*** Expected Results: - Verify that data are saved to the corresponding Q_OE tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName1, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");

		logger.info("*** Expected Results: - Verify that the accession is processed and being moved out of Q_OE");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);       
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");     
        		
        logger.info("*** Expected Results: - Verify that data are saved to ACCN properly");   
        String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(0),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(1),"        Pt fName is saved into ACCN.");        
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(5),"        Pt Addr1 is saved into ACCN.");     
        assertEquals(accn.get(10),pid.get(6),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(9),"        Pt zip is saved into ACCN.");                    
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(12),"        Pt SSN is saved into ACCN.");
        
        assertEquals(accn.get(38),pv1.get(12),"        MRO is saved into ACCN.");
        assertEquals(accn.get(3),pv1.get(12),"        Requisition ID is saved into ACCN.");
        
        String clnAbbrev = orc.get(16);
        String clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        String clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");        

        logger.info("*** Expected Results: - Verify that data (Client Payor 'C') are saved to ACCN_PYR properly"); 
        //accn_pyr
        List<List<String>> accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 0,"        Data is saved into Db");
        
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb), "C","        Client Payor - 'C' is saved into Db.");        
        
        logger.info("*** Expected Results: - Verify that data (INVTEST) are saved to ACCN_TEST properly"); 
        //accn_test
        List<String> accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");       
        
        ArrayList<String> accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, accnTest.get(0), testDb);  
        assertEquals(accnTestList.get(7),obr.get(20).toUpperCase().trim(),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(16), finalReportDt,"        Final Report Date is saved into Db.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_DAIG properly"); 
        //accn_daig
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "1" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db"); 
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_PHYS properly");        
        //accn_phys
        String orderingPhysSeqId = daoManagerPlatform.getPhysSeqIdFromPHYSByAccnId(accnId, testDb);
        String orderingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysSeqIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS."); 

        logger.info("*** Actions: - Generate an HL7 ADT file (update) with all fields and PV1.2 = 'IP' and place it in the in folder");
		currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName2 = "allina-adt-" + currDtTime +".hl7";
        String subsIdIN1 = randomCharacter.getNonZeroRandomNumericString(8);        
        
        List<String>evn = hl7ParsingEngineUtils.getListEVNsAllinaADT();
        pid = hl7ParsingEngineUtils.getListPIDAllinaORM("F", 16, "9516578854", "987654321", ptId);        
        pv1 = hl7ParsingEngineUtils.getListPV1AllinaADT(identifier, "3", "HO");
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1AllinaADT("9517774568", 38, "S", "605410547");
        List<String>in1 = hl7ParsingEngineUtils.getListIN1AllinaADT(false, "C", "Eligible", "1", subsIdIN1);
        List<String>in2 = hl7ParsingEngineUtils.getListIN2AllinaADT();
        List<String>acc = hl7ParsingEngineUtils.getListACCAllinaADT(); 
        finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");        
       
        //createHL7FileAllinaADTMultiAccn
        content = hl7ParsingEngineUtils.createHL7FileAllinaADTMultiAccn(currDtTime, Arrays.asList(evn), Arrays.asList(pid), Arrays.asList(pv1), Arrays.asList(gt1),Arrays.asList(in1), Arrays.asList(in2), Arrays.asList(acc), "A01");
         
        fileManipulation.writeFileToFolder(content, filePathIn, fileName2);
		
		logger.info("*** Expected Results: - Verify that the new Allina ADT file is generated in the in folder");
		fIncoming = new File(filePathIn + fileName2);
		//System.out.println("File Path = " + filePathIn + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fIncoming, 5), "        Allina ADT HL7 file: " + fileName2 + " should be generated under " + filePathIn + " folder.");

		logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the HospitalAdmit record for subsId="+subsIdIN1);
		long maxTime = (QUEUE_WAIT_TIME_MS*2) + System.currentTimeMillis();
		while (((rpmDao.getHospitalAdmitCheckBySubsId(testDb, subsIdIN1)).size()<1) && System.currentTimeMillis() < maxTime)
		{
			logger.info("Waiting for PF-HL7 Engine to create HospitalAdmit record for subsId=" + subsIdIN1 + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(maxTime - System.currentTimeMillis()) + "s");
			Thread.sleep(QUEUE_POLL_TIME_MS);
		}
        Assert.assertTrue(((rpmDao.getHospitalAdmitCheckBySubsId(testDb, subsIdIN1)).size()>0), "Accession is not in Q_OE");

        logger.info("*** Expected Results: - Verify that the Allina ADT file is processed and moved to the archive folder");
		fArchive = new File(filePathArchive + fileName2);
		//System.out.println("File Path = " + filePathArchive + fileName);//Debug Info	
		assertTrue(isFileExists(fArchive, 5), "        Allina ADT HL7 file: " + fileName2 + " should be processed and moved to " + filePathArchive + " folder.");         

		logger.info("*** Expected Results: - Verify that data are saved to HOSPITAL_ADMIT_CHECK table properly");
        
        List<HospitalAdmitCheck> hospitalAdmitChkList = rpmDao.getHospitalAdmitCheckBySubsId(testDb, subsIdIN1);
        
        assertEquals(String.valueOf(hospitalAdmitChkList.get(0).ssn), "987654321", "        HOSPITAL_ADMIT_CHECK.SSN = '987654321'");	        
        ptDOB = hl7ParsingEngineUtils.geNewDate(16, "yyyy-MM-dd");
        assertEquals(hospitalAdmitChkList.get(0).ptLNm,pid.get(0),"        Pt lName is saved into HOSPITAL_ADMIT_CHECK.");
        assertEquals(hospitalAdmitChkList.get(0).ptFNm,pid.get(1) + " " + pid.get(2),"        Pt fName is saved into HOSPITAL_ADMIT_CHECK.");
        
        DateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Date admitDtInPV1 = df1.parse(pv1.get(9));
        assertEquals(hospitalAdmitChkList.get(0).admitDt.toString(), df2.format(admitDtInPV1).toString(),"        ADMIT_DT is saved into HOSPITAL_ADMIT_CHECK."); 
                      
        Date dischargeDtInPV1 = df1.parse(pv1.get(10));           
        assertEquals(hospitalAdmitChkList.get(0).dischargeDt.toString(), df2.format(dischargeDtInPV1).toString(), "        DISCHARGE_DT should be saved into HOSPITAL_ADMIT_CHECK.");
               
        Date ptDobInFile = df2.parse(ptDOB);
        assertEquals(hospitalAdmitChkList.get(0).dob.toString(), df2.format(ptDobInFile).toString(), "        DOB should be saved into HOSPITAL_ADMIT_CHECK.");
  
        logger.info("*** Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName1);
        fileManipulation.deleteFile(filePathArchive, fileName2);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);
        rpmDao.deleteHospitalAdmitCheckBySubsId(testDb, subsIdIN1);
         
		logger.info("*** Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
	}


    @Test(priority = 1, description="Mgl-Add accn with all fields")
    @Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType", "file"})
    public void testPFER_619(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType, String file) throws Exception {
        logger.info("====== Testing - testPFER_619 ======");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
        

        logger.info("*** Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.mgl.MglHl7Parser'");
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.mgl.MglHl7Parser'","PK_TASK_TYP_ID", "18", testDb);

        logger.info("*** Actions: - Clear the System Data Cache");
        xifinAdminUtils.clearDataCache();

        logger.info("*** Actions: - Generate MGL flat file with all fields and place the file to /files/hl7/in folder");
        String accnId = "QAA" + randomCharacter.getRandomNumericString(11);
        String content = FileUtils.readFileToString(new File(upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file));
        //File name
        String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "mgl-" + currDtTime +".txt";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String filePathIn = dirBase + "in" + File.separator;
        String filePathArchive = dirBase + "archive" + File.separator;

        fileManipulation.writeFileToFolder(content.replaceAll("ACCESSION 001>", accnId), filePathIn, fileName);

        logger.info("*** Expected Results: - Verify that the new MGL flat file is generated in the in folder");
        File fIncoming = new File(filePathIn + fileName);
        System.out.println("File Path = " + filePathIn + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        MGL flat file: " + fileName + " should be generated under " + filePathIn + " folder.");

        logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");

        logger.info("*** Expected Results: - Verify that the MGL flat file is processed and moved to archive folder");
        File fArchive = new File(filePathArchive + fileName);
        System.out.println("File Path = " + filePathArchive + fileName);//Debug Info
        assertTrue(isFileExists(fArchive, 5), "        MGL flat file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");

        logger.info("*** Expected Results: - Verify that data are saved to the corresponding tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");

        //accn_cln_q
        logger.info("*** Expected Results: - Verify that data are saved to ACCN_CLN_Q table properly");
        List<AccnClnQ> accnClnQList = rpmDao.getAccnClnQByAccnId(testDb, accnId);
        int questionId = accnClnQList.get(0).getQuestnId();
        String response = accnClnQList.get(0).getRespns().trim();
        assertEquals(questionId, 53, "       The ACCN_CLN_Q.PK_QUESTN_ID = 53. for accn id = " + accnId);
        assertEquals(response, "CLNQ007353287MYRIADMAXPRMYRIADMAXPRMYRIADMAXPRMYRIADMAXPR999", "       The ACCN_CLN_Q.RESPNS = 'CLNQ007353287MYRIADMAXPRMYRIADMAXPRMYRIADMAXPRMYRIADMAXPR999'. for accn id = " + accnId);

        //accn
        logger.info("*** Expected Results: - Verify that data are saved to ACCN table properly");
        AccnRecord expectedAccnRecord = setValuesInAccnRecord();
        Accn accn = accessionDao.getAccn(accnId);
        verifyAccnDataSaved(accn, expectedAccnRecord);

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_TEST table properly");
        //accn_test
        AccnTestRecord expectedAccnTestRecord = setValuesInAccnTestRecord();
        List<AccnTest> accnTestList = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, "219");
        verifyAccnTestDataSaved(accnTestList.get(0), expectedAccnTestRecord);

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_DIAG table properly");
        //accn_diag
        AccnDiagRecord expectedAccnDiagRecord = setValuesInAccnDiagRecord();
        List<AccnDiag> accnDiagList = rpmDao.getAccnDiagByAccnId(testDb, accnId);
        verifyAccnDiagDataSaved(accnDiagList.get(0), expectedAccnDiagRecord);

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_PHYS table properly");
        //accn_phys
        Phys phys = rpmDao.getPhysByUpin(testDb, "A28186");
        AccnPhysRecord expectedAccnPhysRecord = setValuesInAccnPhysRecord(phys);
        List<AccnPhys> accnPhysList = rpmDao.getAccnPhysByAccnId(testDb, accnId);
        verifyAccnPhysDataSaved(accnPhysList.get(0), expectedAccnPhysRecord);

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_CNTCT table properly");
        //accn_cntct
        List<String> noteList = new ArrayList<>();
        noteList.add("Self,,No Cancer_Fam,48,Thyroid_Fam,31,Ovary_Fam,34,Breast, Invasive");
        noteList.add("*170103,KPIEP,rcvd fax \"_IN5864D56CDA18\" Approval letter for 81211 Auth# 161207306 Valid 12/202/2016 - 01/20/2017");
        List<AccnCntctRecord> expectedAccnCntctRecordList = setValuesInAccnCntctRecord(fileName, noteList);
        List<AccnCntct> accnCntctList = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        verifyAccnCntctDataSaved(accnCntctList, expectedAccnCntctRecordList);

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_PYR table properly");
        //accn_pyr
        List<Integer> pyrIdList = new ArrayList<>();
        pyrIdList.add(rpmDao.getPyrByPyrAbbrv(testDb, "BSCA").getPyrId());
        pyrIdList.add(rpmDao.getPyrByPyrAbbrv(testDb, "P").getPyrId());
        List<String> grpIdList = new ArrayList<>();
        grpIdList.add("7770");
        grpIdList.add("123456");
        List<String> subsIdList = new ArrayList<>();
        subsIdList.add("212267700782985");
        subsIdList.add("123456789A");
        List<Integer> relshpIdList = new ArrayList<>();
        relshpIdList.add(1);
        relshpIdList.add(2);
        List<String> cmntList = new ArrayList<>();
        cmntList.add("PYR1CMN");
        cmntList.add("PYR2CMN");
        List<AccnPyrRecord> expectedAccnPyrRecordList = setValuesInAccnPyrRecord(pyrIdList, grpIdList, subsIdList, relshpIdList, cmntList);
        List<AccnPyr> accnPyrList = accessionDao.getAccnPyrs(accnId);
        verifyAccnPyrDataSaved(accnPyrList, expectedAccnPyrRecordList);

        logger.info("*** Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);

        logger.info("*** Actions: - Clear the System Data Cache");
        xifinAdminUtils.clearDataCache();

    }
    @Test(priority = 1, description="Mayo-Add and Update accn with all HL7 fields")
    @Parameters({"email", "password","eType1", "eType2", "xapEnv", "engConfigDB", "formatType"})
    public void testPFER_495(String email, String password, String eType1, String eType2, String xapEnv, String engConfigDB, String formatType) throws Exception {
        logger.info("====== Testing - testPFER_495 ======");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
        

        logger.info("*** Actions: - Update system_setting and set 85 = 0, 1111 = 0, 1119 = 0, 1147 = 1, 1828 = 0, 102 = 1, 1842 = 1, 1835 = 1, 1827 = 1; Update TASK_TYP.PK_TASK_TYP_ID = 18 set CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.MayoHl7Parser'");
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "85", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1111", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1119", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1147", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1828", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1842", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 1","PK_SETTING_ID", "1827", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.MayoHl7Parser'","PK_TASK_TYP_ID", "18", testDb);

        logger.info("*** Actions: - Create a Mayo HL7 file with all fields");
        //File name
        String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "mayo-" + currDtTime +".hl7";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String filePathIn = dirBase + "in" + File.separator;
        String filePathArchive = dirBase + "archive" + File.separator;

        String accnId = "QAAUTOHL7" + currDtTime;
        logger.info("       Accession ID = " + accnId);
        String ptId = randomCharacter.getNonZeroRandomNumericString(8);
        //accnId, ptSex, years, ptHomePhone, ptWorkPhone, ptMaritalStatus, String ptSSN, String ptId
        List<String>pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "M", 15, "6196578854", "8587353125", "M", "123456789", ptId);
        //phleFacId, admSrcId
        List<String>pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("DS", "1");
        //isInvalidPyr, insRelTyp, eligStatus, insSex
        List<String>in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "Y", "M");
        //guarantorHomePhone, years, guarantorSex, guarantorRelshpTyp, guarantorSSN, employerPhone
        List<String>gt1 = hl7ParsingEngineUtils.getListGT1GenericV231("6197774568", 40, "M", "C", "987456789", "8586217459");
        //isProfTest, transTyp, unit, abnReceivedFlag, isRoundTrip, testFacId, specifiedTestAbbrev
        String dos = timeStamp.getCurrentDate("YYYYMMdd");
        List<String>ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "1", "Y", "Y", "1", "71010", dos);
        //testSpecificQuestion, abnReceivedFlag, renalFlag, payorToBill
        List<String>zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "Y", "Y", "C");
        //mspReceivedFlag
        List<String>zxa = hl7ParsingEngineUtils.getListZXAGenericV231("Y");
        //pyr_abbrev, isFreeText
        List<String>dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false);
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");

        String content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1),
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE"), Arrays.asList(zxa), finalReportDt, true);

        fileManipulation.writeFileToFolder(content, filePathIn, fileName);

        logger.info("*** Expected Results: - Verify that the new HL7 file is generated in the in folder");
        File fIncoming = new File(filePathIn + fileName);
        System.out.println("File Path = " + filePathIn + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Mayo HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");

        logger.info("*** Actions: - Insert test data in TEST_Q in DB");
        String testId = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(ft1.get(4), testDb).get(0);
        daoManagerPlatform.insertTestQ(testId, "1", "3", testDb);

      logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");

        logger.info("*** Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
        File fArchive = new File(filePathArchive + fileName);
        System.out.println("File Path = " + filePathArchive + fileName);//Debug Info
        assertTrue(isFileExists(fArchive, 5), "        Mayo HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");

        logger.info("*** Expected Results: - Verify that data are saved to the corresponding tables properly");
        List<String> qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        int count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        List<String> lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN table properly");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");

        String ptDOB = hl7ParsingEngineUtils.geNewDate(15, "yyyy-MM-dd");
        //accn
        List<String> accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3),"        Pt fName is saved into ACCN.");
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(6),"        Pt Addr1 is saved into ACCN.");
        assertEquals(accn.get(10),pid.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn.get(8),pid.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn.get(3),pid.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"1","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(16),"        Pt SSN is saved into ACCN.");

        String clnAbbrev = pv1.get(3);
        String clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        String clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");

        //String ptLocation = pv1.get(0);
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created
        String pscId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1.get(5), testDb).get(4);
        assertEquals(accn.get(49), pscId, "        PSC should be saved into ACCN.");
        assertEquals(accn.get(56), pv1.get(6), "        Admission Source should be saved into ACCN.");
        assertEquals(accn.get(32), pv1.get(7), "        Patient Type should be saved into ACCN.");

        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date actDate = df1.parse(accn.get(18));
        DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate), ft1.get(13), "        Receipt Date should be saved into ACCN.");

        assertEquals(accn.get(34), ft1.get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn.get(33), ft1.get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn.get(36), ft1.get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn.get(35), "1", "        roundTrip should be saved into ACCN.");

        assertEquals(accn.get(48), zxa.get(5), "        Phlebotomist ID should be saved into ACCN.");

        String onSetDtStrInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn.get(61));
        assertEquals(onSetDtStrInDB, zxa.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn.get(62), zxa.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn.get(59), zxa.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn.get(19), zxa.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn.get(42), "1", "        MSP Received Flag should be saved into ACCN.");
        assertEquals(accn.get(63), zxa.get(15), "        Accident State should be saved into ACCN.");

        logger.info("*** Expected Results: - Verify that data are saved to PT_V2 properly");
        String epi = pid.get(0);
        System.out.println("epi = " + epi);//debug info
        List<String> ptV2List = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi, testDb);
        assertTrue(ptV2List.size()>0,"        Patient EPI " + epi + " should be saved into PT_V2.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_PYR properly");
        //accn_pyr
        List<List<String>> accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr.get(0).get(7),in1.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(13),"        INS_ADDR1 is saved into Db");
        assertEquals(accnPyr.get(0).get(18),in1.get(14),"        INS_ADDR2 is saved into Db");
        assertEquals(accnPyr.get(0).get(16),in1.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(25),in1.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(20), "1","        INS_SEX is saved into Db"); //1 = M (Male)
        assertEquals(accnPyr.get(0).get(26),in1.get(23),"        SUBS_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created

        String eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created

        String insuredDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(19));
        assertEquals(insuredDOBInDB, in1.get(12),"        INS_DOB is saved into Db");

        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr.get(1).get(7).toUpperCase(),gt1.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");
        assertEquals(accnPyr.get(1).get(8).toUpperCase(),gt1.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");

        assertEquals(accnPyr.get(1).get(9).toUpperCase(),gt1.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(18).toUpperCase(),gt1.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");

        String guarantorDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(1).get(19));
        assertEquals(guarantorDOBInDB, gt1.get(8),"        Guarantor's DOB is saved into Db");

        assertEquals(accnPyr.get(1).get(20), "1","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr.get(1).get(22), gt1.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr.get(1).get(27), gt1.get(7),"        Guarantor's Home Phone is saved into Db");

        assertEquals(accnPyr.get(1).get(11), gt1.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr.get(1).get(12), gt1.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(23), gt1.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr.get(1).get(24), gt1.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr.get(1).get(28), gt1.get(19),"        Employer's Phone number is saved into Db");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_TEST table properly");
        //accn_test
        List<String> accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");
        assertEquals(accnTest.get(0),ft1.get(4),"        Test is saved into Db");

        ArrayList<String> accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1.get(4), testDb);
        String accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(6),ft1.get(19),"        Mod1 is saved into Db");

        assertEquals(accnTestList.get(3),ft1.get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1.get(5),"        ALT_TEST_NAME is saved into Db");
        //Mayo: Price shall be multiplied by number of units in FT-10 to calculate total price for the test.
        int units = Integer.valueOf(ft1.get(6));
        int manualPrc = Integer.valueOf(ft1.get(8));
        String expectedManualPrc = String.valueOf(manualPrc * units);
        String actualManualPrcAccnTestList = String.valueOf(Integer.valueOf(accnTestList.get(13))*Integer.valueOf(accnTestList.get(3)));
        assertEquals(actualManualPrcAccnTestList, expectedManualPrc,"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),ft1.get(0),"        LIS Trace Id is saved into Db");
        //assertEquals(accnTestList.get(4),"1","        Performing Facility is saved into Db");

        assertEquals(accnTestList.get(7),zxt.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(8), "1","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList.get(9), "1","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList.get(10), zxt.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList.get(11), zxt.get(5),"        B_RENAL is saved into Db");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_DAIG table properly");
        //accn_daig
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "1" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db");
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");

        //test level diag
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "2" ,testDb);
        assertEquals(accnDiag.get(2), ft1.get(11),"        Test level Diag Code " + ft1.get(11) + " should be saved into ACCN_DIAG.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_CLN_Q table properly");
        //accn_cln_q
        List<String> accnClnQ = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId,testDb);
        assertTrue(accnClnQ.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ.get(0), zxa.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa.get(0) + " should be saved.");
        assertEquals(accnClnQ.get(1), zxa.get(4),"        ACCN_CLN_Q.RESPNS " + zxa.get(4) + " should be saved.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_CNTCT table properly");
        //accn_cntct
        List<String> accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is null", testDb);
        assertTrue(accnCntct.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct.get(0), zxa.get(3),"        CNTCT_INFO " + zxa.get(3) + " should be saved into ACCN_CNTCT.");

        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is not null", testDb);
        assertEquals(accnCntct.get(3), zxa.get(11),"       " + zxa.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_CLINTRL table properly");
        //accn_clintrl
        List<String> accnClintrl = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId,testDb);
        assertTrue(accnClintrl.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl.get(0),zxa.get(4),"        CLINTRL_ENCNTR " + zxa.get(4) + " should be saved into ACCN_CLINTRL.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_DAIL table properly");
        //accn_dail
        List<String> accnDial = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId,testDb);
        assertTrue(accnDial.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial.get(1), zxa.get(6),"        FK_DIAL_TYP_ID " + zxa.get(6) + " should be saved into ACCN_DIAL.");

        String firstDailDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial.get(3));
        assertEquals(firstDailDtInDB, zxa.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_TEST_Q table properly");
        //accn_test_q
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");

        logger.info("*** Expected Results: - Verify that data are saved to PT_PYR_V2 properly");
        //pt_pyr_v2
        String ptSeqId = accn.get(43);
        String ptPyrEffDt = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId, "1", testDb).get(28);
        String ptPyrEffDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt);
        assertEquals(ptPyrEffDtInDB, in1.get(9), "        EFF_DT should be saved into PT_PYR_V2.");

        logger.info("*** Expected Results: - Verify that data are saved to ACCN_PHYS table properly");
        //accn_phys
        //String renderingPhysSeqIdInDB = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId, accnTestSeqId, testDb).get(0);
        //String renderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1.get(20), testDb).get(0);
        //assertEquals(renderingPhysSeqIdInDB, renderingPhysSeqId, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");

        String referingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(4), testDb).get(0);
        String referingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB, referingPhysSeqId, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");

        String orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(2), testDb).get(0);
        String orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS.");

        logger.info("*** Actions: - Create a new Mayo HL7 file with the same Accession ID and Patient ID and updated values in all fields");
        currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = "mayo-" + currDtTime +"-update.hl7";

        //accnId, ptSex, years, ptHomePhone, ptWorkPhone, ptMaritalStatus, String ptSSN, String ptId
        pid = hl7ParsingEngineUtils.getListPIDGenericV231(accnId, "F", 16, "9516578854", "7607353125", "S", "987654321", ptId);
        //phleFacId, admSrcId
        pv1 = hl7ParsingEngineUtils.getListPV1GenericV231("WWW", "3");
        //isInvalidPyr, insRelTyp, eligStatus, insSex
        in1 = hl7ParsingEngineUtils.getListIN1GenericV231(false, "C", "N", "F");
        //guarantorHomePhone, years, guarantorSex, guarantorRelshpTyp, guarantorSSN, employerPhone
        gt1 = hl7ParsingEngineUtils.getListGT1GenericV231("9517774568", 38, "F", "C", "127456789", "6196217459");
        //isProfTest, transTyp, unit, abnReceivedFlag, isRoundTrip, testFacId, specifiedTestAbbrev
        ft1 = hl7ParsingEngineUtils.getListFT1WithTestsGenericV231(false, "D", "2", "N", "N", "1", "BS", dos); //"D" - Debit
        //testSpecificQuestion, abnReceivedFlag, renalFlag, payorToBill
        zxt = hl7ParsingEngineUtils.getListZXTGenericV231("1", "N", "N", "");
        //mspReceivedFlag
        zxa = hl7ParsingEngineUtils.getListZXAGenericV231("N");
        //pyr_abbrev, isFreeText = false
        dg1 = hl7ParsingEngineUtils.getListDG1GenericV231(in1.get(0), false);
        finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");

        content = hl7ParsingEngineUtils.createHL7FileGenericV231MultiAccn(currDtTime, Arrays.asList(pid), Arrays.asList(pv1), dg1, Arrays.asList(in1),
                Arrays.asList(gt1), Arrays.asList(ft1), Arrays.asList(zxt), Arrays.asList("ENTERPRISE"), Arrays.asList(zxa), finalReportDt, true);

        fileManipulation.writeFileToFolder(content, filePathIn, fileName);

        logger.info("*** Expected Results: - Verify that the new HL7 file is generated in the in folder");
        fIncoming = new File(filePathIn + fileName);
        System.out.println("File Path = " + filePathIn + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Mayo HL7 file: " + fileName + " should be generated under " + filePathIn + " folder.");

        logger.info("*** Actions: - Insert test data in TEST_Q table in DB");
        testId = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(ft1.get(4), testDb).get(0);
        daoManagerPlatform.insertTestQ(testId, "1", "3", testDb);

        logger.info("*** Step 4 Actions: - Wait for PF-HL7 Parsing Engine to create the q_oe record for accnId="+accnId);
        Assert.assertTrue(waitForHL7EngineToCreateQoe(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not in Q_OE");

        logger.info("*** Expected Results: - Verify that the HL7 file is processed and moved to archive folder");
        fArchive = new File(filePathArchive + fileName);
        System.out.println("File Path = " + filePathArchive + fileName);//Debug Info
        assertTrue(isFileExists(fArchive, 5), "        GenericV231 HL7 file: " + fileName + " should be processed and moved to " + filePathArchive + " folder.");

        logger.info("*** Expected Results: - Verify that data are saved to the corresponding tables properly");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        count = daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(Integer.parseInt(qOe.get(0)), testDb);
        lisInterfaceHistList = daoManagerPlatform.getLisInterfaceHistByFileName(fileName, testDb);
        assertTrue(qOe.size()>0,"        Data is saved into Q_OE");
        assertTrue(count>0,"        Data is saved into Q_OE_DATA");
        assertTrue(lisInterfaceHistList.size()>0,"        Data is saved into LIS_INTERFACE_HISTER");

        logger.info("*** Step 5 Actions: - Wait for PF-OE Posting Engine to process the q_oe record for accnId="+accnId);
        Assert.assertTrue(isOutOfQOEQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of Q_OE Queue");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN table properly");
        qOe = daoManagerPlatform.getQOEInfoFromQOEByAccnId(accnId, testDb);
        assertTrue(qOe.size()==0,"        The accession should be moved out of Q_OE.");

        ptDOB = hl7ParsingEngineUtils.geNewDate(16, "yyyy-MM-dd");
        //accn
        accn = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        assertTrue(accn.size()>0,"        Accn " + accnId + " should be saved into ACCN.");
        assertEquals(accn.get(5),pid.get(2),"        Pt lName is saved into ACCN.");
        assertEquals(accn.get(6),pid.get(3),"        Pt fName is saved into ACCN.");
        assertTrue(accn.get(7).contains(ptDOB),"        DOB is saved into ACCN.");
        assertEquals(accn.get(9),pid.get(6),"        Pt Addr1 is saved into ACCN.");
        assertEquals(accn.get(10),pid.get(7),"        Pt Addr2 is saved into ACCN.");
        assertEquals(accn.get(11),pid.get(10),"        Pt zip is saved into ACCN.");
        assertEquals(accn.get(12),pid.get(12),"        Pt Home phone is saved into ACCN.");
        assertEquals(accn.get(8),pid.get(13),"        Pt Work phone is saved into ACCN.");
        assertEquals(accn.get(3),pid.get(15),"        Requisition ID is saved into ACCN.");
        assertEquals(accn.get(0),"2","        Pt Sex is saved into ACCN.");
        assertEquals(accn.get(15),pid.get(16),"        Pt SSN is saved into ACCN.");

        clnAbbrev = pv1.get(3);
        clnId = daoManagerClientWS.getCLnIDFromIDbyAbbrev(clnAbbrev, testDb);
        clnIdInDB = accn.get(1);
        assertEquals(clnIdInDB, clnId,"        Client ID is saved into accn.fk_cln_id.");

        //String ptLocation = pv1.get(0);
        //assertEquals(accn.get(47), pv1.get(0), "        Pt Location should be saved into ACCN."); // PV1.3 - Patient Location; PF OE Posting Engine CR has been created
        pscId = daoManagerXifinRpm.getFacInfoFromFACByFacAbbr(pv1.get(5), testDb).get(4);
        assertEquals(accn.get(49), pscId, "        PSC should be saved into ACCN.");
        //////////////assertEquals(accn.get(56), pv1.get(6), "        Admission Source should be saved into ACCN."); //Mars also not updating the accn.FK_ADMISSION_SRC_ID to '3'!
        assertEquals(accn.get(32), pv1.get(7), "        Patient Type should be saved into ACCN.");

        df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        actDate = df1.parse(accn.get(18));
        df2 = new SimpleDateFormat("yyyyMMdd");
        assertEquals(df2.format(actDate), ft1.get(13), "        Receipt Date should be saved into ACCN.");

        assertEquals(accn.get(34), ft1.get(14), "        tripMiles should be saved into ACCN.");
        assertEquals(accn.get(33), ft1.get(15), "        tripStops should be saved into ACCN.");
        assertEquals(accn.get(36), ft1.get(16), "        tripPtCount should be saved into ACCN.");
        assertEquals(accn.get(35), "1", "        roundTrip should be saved into ACCN."); //Mars also not updating the accn.B_ROUND_TRIP to '0'!

        assertEquals(accn.get(48), zxa.get(5), "        Phlebotomist ID should be saved into ACCN.");

        onSetDtStrInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accn.get(61));
        assertEquals(onSetDtStrInDB, zxa.get(7), "        Onset Date should be saved into ACCN.");
        assertEquals(accn.get(62), zxa.get(16), "        Onset Type should be saved into ACCN.");
        assertEquals(accn.get(59), zxa.get(17), "        Accident Cause should be saved into ACCN.");
        assertEquals(accn.get(19), zxa.get(10), "        Indigent Percent should be saved into ACCN.");
        assertEquals(accn.get(42), "1", "        MSP Received Flag should be saved into ACCN."); //Mars also not updating the accn.B_MSP_FORM to '0'!
        assertEquals(accn.get(63), zxa.get(15), "        Accident State should be saved into ACCN.");

        logger.info("*** Expected Results: - Verify that updated data are saved to PT_V2 properly");
        epi = pid.get(0);
        System.out.println("epi = " + epi);//debug info
        ptV2List = daoManagerPlatform.getPtInfoFromPTV2ByEpi(epi, testDb);
        assertTrue(ptV2List.size()>0,"        Patient EPI " + epi + " should be saved into PT_V2.");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_PYR table properly");
        //accn_pyr
        accnPyr = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
        assertTrue(accnPyr.size() > 1,"        Data is saved into Db");
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(0).get(0),testDb),in1.get(0),"        PyrId in1 data is saved into Db");
        assertEquals(accnPyr.get(0).get(7),in1.get(10),"        INS_L_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(8),in1.get(10),"        INS_F_NM is saved into Db");
        assertEquals(accnPyr.get(0).get(9),in1.get(13),"        INS_ADDR1 is saved into Db");
        assertEquals(accnPyr.get(0).get(18),in1.get(14),"        INS_ADDR2 is saved into Db");
        assertEquals(accnPyr.get(0).get(16),in1.get(17),"        FK_INS_ZIP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(25),in1.get(8),"        GRP_ID is saved into Db");
        assertEquals(accnPyr.get(0).get(20), "2","        INS_SEX is saved into Db"); //2 = F (Female)
        assertEquals(accnPyr.get(0).get(26),in1.get(23),"        SUBS_ID is saved into Db");
        //assertEquals(accnPyr.get(0).get(3), "HL7interface","        ELIG_SVC_NAME is saved into Db"); //IN1.25 (Eligibility Status); PF OE Posting Engine CR 54949 has been created

        //eligchkDtInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(1));
        //assertEquals(eligchkDtInDB, in1.get(21),"        ELIG_CHK_DT is saved into Db"); //IN1.26 (Eligibility Check Date); PF OE Posting Engine CR 54949 has been created

        insuredDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(0).get(19));
        assertEquals(insuredDOBInDB, in1.get(12),"        INS_DOB is saved into Db");

        //2nd payor = Guarantor
        assertEquals(daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyr.get(1).get(0),testDb),"G","        PyrId gt1 data id is saved into Db");
        assertEquals(accnPyr.get(1).get(7).toUpperCase(),gt1.get(0).toUpperCase(),"        Guarantor L_NM is saved into Db");
        assertEquals(accnPyr.get(1).get(8).toUpperCase(),gt1.get(0).toUpperCase(),"        guarantor F_NM is saved into Db");

        assertEquals(accnPyr.get(1).get(9).toUpperCase(),gt1.get(1).toUpperCase(),"        Guarantor Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(18).toUpperCase(),gt1.get(2).toUpperCase(),"        Guarantor Addr2 is saved into Db");

        guarantorDOBInDB = hl7ParsingEngineUtils.convertDateFormat("yyyy-MM-dd HH:mm:ss", "yyyyMMdd", accnPyr.get(1).get(19));
        assertEquals(guarantorDOBInDB, gt1.get(8),"        Guarantor's DOB is saved into Db");

        assertEquals(accnPyr.get(1).get(20), "2","        Guarantor's SEX is saved into Db");
        assertEquals(accnPyr.get(1).get(21), "3","        Guarantor's Relationship is saved into Db"); //3 = C (Child)
        assertEquals(accnPyr.get(1).get(22), gt1.get(11),"        Guarantor's SSN is saved into Db");
        assertEquals(accnPyr.get(1).get(27), gt1.get(7),"        Guarantor's Home Phone is saved into Db");

        assertEquals(accnPyr.get(1).get(11), gt1.get(12),"        Employer Name is saved into Db");
        assertEquals(accnPyr.get(1).get(12), gt1.get(13),"        Employer's Addr1 is saved into Db");
        assertEquals(accnPyr.get(1).get(23), gt1.get(14),"        Employer's Addr2 is saved into Db");
        assertEquals(accnPyr.get(1).get(24), gt1.get(17),"        Employer's Zip is saved into Db");
        assertEquals(accnPyr.get(1).get(28), gt1.get(19),"        Employer's Phone number is saved into Db");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_TEST table properly");
        //accn_test
        accnTest = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, testDb);
        assertTrue(accnTest.size() > 0,"        Data is saved into Db");
        assertEquals(accnTest.get(0),ft1.get(4),"        Test is saved into Db");

        accnTestList = daoManagerPlatform.getAccnTestInfoFromACCNTESTByAccnIdTestAbbrev(accnId, ft1.get(4), testDb);
        accnTestSeqId = accnTestList.get(0);
        assertEquals(accnTestList.get(6),ft1.get(19),"        Mod1 is saved into Db");

        assertEquals(accnTestList.get(3),ft1.get(6),"        Units is saved into Db");
        assertEquals(accnTestList.get(12),ft1.get(5),"        ALT_TEST_NAME is saved into Db");
        //Mayo: Price shall be multiplied by number of units in FT-10 to calculate total price for the test.
        units = Integer.valueOf(ft1.get(6));
        manualPrc = Integer.valueOf(ft1.get(8));
        expectedManualPrc = String.valueOf(manualPrc * units);
        actualManualPrcAccnTestList = String.valueOf(Integer.valueOf(accnTestList.get(13))*Integer.valueOf(accnTestList.get(3)));
        assertEquals(actualManualPrcAccnTestList, expectedManualPrc,"        MANUAL_PRC is saved into Db");
        assertEquals(accnTestList.get(14),ft1.get(0),"        LIS Trace Id is saved into Db");
        //assertEquals(accnTestList.get(4),"1","        Performing Facility is saved into Db");

        assertEquals(accnTestList.get(7),zxt.get(0),"        NOTE is saved into Db");
        assertEquals(accnTestList.get(8), "0","        B_BILL_CLN is saved into Db"); //when value ‘C’ received in ZXT.3 (Payor to Bill), set accn_test.b_bill_cln to true
        assertEquals(accnTestList.get(9), "0","        B_ABN_REC is saved into Db");
        assertEquals(accnTestList.get(10), zxt.get(4),"        PYR_SRV_AUTH_NUM is saved into Db");
        assertEquals(accnTestList.get(11), zxt.get(5),"        B_RENAL is saved into Db");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_DAIG table properly");
        //accn_daig
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "3" ,testDb);
        assertTrue(accnDiag.size() > 0,"        Diag is saved into Db");
        assertEquals(accnDiag.get(2), dg1.get(0),"        Accession level Diag Code " + dg1.get(0) + " should be saved into ACCN_DIAG.");

        //test level diag
        accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnId, "4" ,testDb);
        assertEquals(accnDiag.get(2), ft1.get(11),"        Test level Diag Code " + ft1.get(11) + " should be saved into ACCN_DIAG.");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_CLN_Q table properly");
        //accn_cln_q
        accnClnQ = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId,testDb);
        assertTrue(accnClnQ.size() > 0,"        Accession Client Question should be saved into DB.");
        assertEquals(accnClnQ.get(0), zxa.get(0),"        ACCN_CLN_Q.PK_QUESTN_ID " + zxa.get(0) + " should be saved.");
        assertEquals(accnClnQ.get(1), zxa.get(4),"        ACCN_CLN_Q.RESPNS " + zxa.get(4) + " should be saved.");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_CNTCT table properly");
        //accn_cntct
        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is null order by pk_cntct_seq desc", testDb);
        assertTrue(accnCntct.size() > 0,"        Accession Contact should be saved into DB.");
        assertEquals(accnCntct.get(0), zxa.get(3),"        CNTCT_INFO " + zxa.get(3) + " should be saved into ACCN_CNTCT.");

        accnCntct = daoManagerPlatform.getAccnCntctByAccnIdAndCondition(accnId, "NOTE is not null order by pk_cntct_seq desc", testDb);
        assertEquals(accnCntct.get(3), zxa.get(11),"       " + zxa.get(11) + " should be saved into ACCN_CNTCT.NOTE.");
        assertEquals(accnCntct.get(4), "1","       PRNT_STMNT should be saved into ACCN_CNTCT.");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_CLINTRL table properly");
        //accn_clintrl
        accnClintrl = daoManagerAccnWS.getAccnFromACCN_CLINTRL(accnId,testDb);
        assertTrue(accnClintrl.size() > 0,"        Clinical Trial Data should be saved into DB.");
        assertEquals(accnClintrl.get(0),zxa.get(4),"        CLINTRL_ENCNTR " + zxa.get(4) + " should be saved into ACCN_CLINTRL.");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_DAIL table properly");
        //accn_dail
        accnDial = daoManagerXifinRpm.getAccnDialInfoFromACCNDIALByAccnId(accnId,testDb);
        assertTrue(accnDial.size() > 0,"        Accession Dialysis should be saved into DB.");
        assertEquals(accnDial.get(1), zxa.get(6),"        FK_DIAL_TYP_ID " + zxa.get(6) + " should be saved into ACCN_DIAL.");

        firstDailDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", accnDial.get(3));
        assertEquals(firstDailDtInDB, zxa.get(13), "        PT_FIRST_DIALYSIS_DT should be saved into ACCN_DAIL.");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_TEST_Q table properly");
        //accn_test_q
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getQuestnId(), 1, "       FK_QUESTN_ID should be saved into ACCN_TEST_Q.");
        assertEquals(accessionDao.getAccnTestQByAccnIdAccnTestSeqId( accnId, accnTestSeqId).get(0).getRespns(), "answer for 1", "       RESPNS should be saved into ACCN_TEST_Q.");

        logger.info("*** Expected Results: - Verify that updated data are saved to PT_PYR_V2 properly");
        //pt_pyr_v2
        ptSeqId = accn.get(43);
        ptPyrEffDt = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId, "1", testDb).get(28);
        ptPyrEffDtInDB = hl7ParsingEngineUtils.convertDateFormat("MM/dd/yyyy", "yyyyMMdd", ptPyrEffDt);
        assertEquals(ptPyrEffDtInDB, in1.get(9), "        EFF_DT should be saved into PT_PYR_V2.");

        logger.info("*** Expected Results: - Verify that updated data are saved to ACCN_PHYS table properly");
        //accn_phys
        /*renderingPhysSeqIdInDB = daoManagerPlatform.getAccnPhysInfoFromACCNPHYSByAccnIdAccnTestSeqId(accnId, accnTestSeqId, testDb).get(0);
        renderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(ft1.get(20), testDb).get(0);
        assertEquals(renderingPhysSeqIdInDB, renderingPhysSeqId, "        Rendering Physician (accn_phys_typ = 4) should be saved into ACCN_PHYS.");*/

        referingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(4), testDb).get(0);
        referingPhysSeqIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "2").get(0).getPhysSeqId());
        assertEquals(referingPhysSeqIdInDB, referingPhysSeqId, "        Refering Physician (accn_phys_typ = 2) should be saved into ACCN_PHYS.");

        orderingPhysSeqId = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(pv1.get(2), testDb).get(0);
        orderingPhysTypIdInDB = String.valueOf(accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, "1").get(0).getPhysSeqId());
        assertEquals(orderingPhysTypIdInDB, orderingPhysSeqId, "        Ordering Physician (accn_phys_typ = 1) should be saved into ACCN_PHYS.");

        logger.info("*** Actions: - Clear test data");
        fileManipulation.deleteFile(filePathArchive, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "102", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = 0","PK_SETTING_ID", "1835", testDb);
        daoManagerPlatform.deleteAccnTestQByAccnId(accnId, testDb);
        daoManagerPlatform.deleteTestQByTestId(testId, testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("TASK_TYP","CLASSNAME = 'com.mbasys.mars.externalInterfaces.hl7.GenericHl7V231Parser'","PK_TASK_TYP_ID", "18", testDb);

        logger.info("*** Actions: - Clear the System Data Cache");
        xifinAdminUtils.clearDataCache();

    }










    //************************************************************************************************************************************

    private AccnRecord setValuesInAccnRecord() throws ParseException, XifinDataAccessException {
	    AccnRecord accnRecord = new AccnRecord();

        accnRecord.setDos(new java.sql.Date(DATE_FORMAT_MMDDYYYY.parse("12202017").getTime()));
        accnRecord.setPtLName("COLLINS");
        accnRecord.setPtFName("CHRISTIE");
        accnRecord.setPtDob(new java.sql.Date(DATE_FORMAT_MMDDYYYY.parse("02011977").getTime()));
        accnRecord.setPtSex(2);//F = 2        ;
        accnRecord.setClnId(rpmDao.getClnByClnAbbrev(testDb, "100").getClnId());
        accnRecord.setPtAddr1("9414 BALLMAN LN");
        accnRecord.setPtAddr2("PT ADDR2");
        accnRecord.setPtZip("92111");
        accnRecord.setPtHomePhone("6196775730");
        accnRecord.setPtSsn(123456789);
        accnRecord.setReqId("11223344");
        accnRecord.setPtCity("GRAFTON");
        accnRecord.setPtCountry(0);//US = 0

	    return accnRecord;
    }

    private void verifyAccnDataSaved(Accn accn, AccnRecord accnRecord){
	    assertEquals(accn.getDos(), accnRecord.getDos());
        assertEquals(accn.getPtLNm(), accnRecord.getPtLName());
        assertEquals(accn.getPtFNm(), accnRecord.getPtFName());
        assertEquals(accn.getPtDob(), accnRecord.getPtDob());
        assertEquals(accn.getPtSex(), accnRecord.getPtSex());
        assertEquals(accn.getClnId(), accnRecord.getClnId());
        assertEquals(accn.getPtAddr1(), accnRecord.getPtAddr1());
        assertEquals(accn.getPtAddr2(), accnRecord.getPtAddr2());
        assertEquals(accn.getPtZipId(), accnRecord.getPtZip());
        assertEquals(accn.getPtHmPhn(), accnRecord.getPtHomePhone());
        assertEquals(accn.getPtSsn(), accnRecord.getPtSsn());
        assertEquals(accn.getReqId(), accnRecord.getReqId());
        assertEquals(accn.getPtCity(), accnRecord.getPtCity());
        assertEquals(accn.getPtCntryId(), accnRecord.getPtCountry());
    }

    private AccnTestRecord setValuesInAccnTestRecord() {
        AccnTestRecord accnTestRecord = new AccnTestRecord();

        accnTestRecord.setMod1Id("33");
        accnTestRecord.setFacId(1);
        accnTestRecord.setProcTypId(38);
        accnTestRecord.setProcId("8100");
        accnTestRecord.setAbnRec(true);
        accnTestRecord.setUnits(1);
        accnTestRecord.setPyrSvcAuthNum("AUTHID001");

        return accnTestRecord;
    }

    private void verifyAccnTestDataSaved(AccnTest accnTest, AccnTestRecord accnTestRecord){
        assertEquals(accnTest.getMod1Id(), accnTestRecord.getMod1Id());
        assertEquals(accnTest.getFacId(), accnTestRecord.getFacId());
        assertEquals(accnTest.getProcTypId(), accnTestRecord.getProcTypId());
        assertEquals(accnTest.getIsAbnRec(), accnTestRecord.isAbnRec());
        assertEquals(accnTest.getUnits(), accnTestRecord.getUnits());
        assertEquals(accnTest.getPyrSrvAuthNum(), accnTestRecord.getPyrSvcAuthNum());
    }

    private AccnDiagRecord setValuesInAccnDiagRecord() {
        AccnDiagRecord accnDiagRecord = new AccnDiagRecord();

        accnDiagRecord.setDiagCdId("S82.042D");
        accnDiagRecord.setDiagTypId(102018);

        return accnDiagRecord;
    }

    private void verifyAccnDiagDataSaved(AccnDiag accnDiag, AccnDiagRecord accnDiagRecord){
        assertEquals(accnDiag.getDiagCdId(), accnDiagRecord.getDiagCdId());
        assertEquals(accnDiag.getDiagTypId(), accnDiagRecord.getDiagTypId());
    }

    private AccnPhysRecord setValuesInAccnPhysRecord(Phys phys) {
        AccnPhysRecord accnPhysRecord = new AccnPhysRecord();

        accnPhysRecord.setPhysSeqId(phys.getSeqId());
        accnPhysRecord.setAccnPhysTypId(1);

        return accnPhysRecord;
    }

    private void verifyAccnPhysDataSaved(AccnPhys accnPhys, AccnPhysRecord accnPhysRecord){
        assertEquals(accnPhys.getPhysSeqId(), accnPhysRecord.getPhysSeqId());
        assertEquals(accnPhys.getAccnPhysTypId(), accnPhysRecord.getAccnPhysTypId());
    }

    private List<AccnCntctRecord> setValuesInAccnCntctRecord(String fileName, List<String> noteList) throws Exception {
        List<AccnCntctRecord> accnCntctRecordList = new ArrayList<>();

        for(int i=0; i<noteList.size(); i++) {
            AccnCntctRecord accnCntctRecord = new AccnCntctRecord();
            accnCntctRecord.setCntctDt(new java.sql.Date(DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(timeStamp.getCurrentDate()).getTime()));
            accnCntctRecord.setUserId(fileName);
            accnCntctRecord.setNote(noteList.get(i));

            accnCntctRecordList.add(accnCntctRecord);
        }

        return accnCntctRecordList;
    }

    private void verifyAccnCntctDataSaved(List<AccnCntct> accnCntctList, List<AccnCntctRecord> expectedAccnCntctRecordList){
	    for (int i=0; i<accnCntctList.size(); i++){
            assertEquals(accnCntctList.get(i).getUserId().trim(), expectedAccnCntctRecordList.get(i).getUserId());
            assertEquals(accnCntctList.get(i).getCntctDt(), expectedAccnCntctRecordList.get(i).getCntctDt());
            assertEquals(accnCntctList.get(i).getNote().trim(), expectedAccnCntctRecordList.get(i).getNote());
        }
    }

    private List<AccnPyrRecord> setValuesInAccnPyrRecord(List<Integer> pyrIdList, List<String> grpIdList, List<String> subsIdList, List<Integer> relshpIdList, List<String> cmntList) throws ParseException {
        List<AccnPyrRecord> accnPyrRecordList = new ArrayList<>();

        for(int i=0; i<pyrIdList.size(); i++) {
            AccnPyrRecord accnPyrRecord = new AccnPyrRecord();
            accnPyrRecord.setPyrId(pyrIdList.get(i));
            accnPyrRecord.setGrpId(grpIdList.get(i));
            accnPyrRecord.setSubsId(subsIdList.get(i));
            accnPyrRecord.setRelshpId(relshpIdList.get(i));
            accnPyrRecord.setCmnt(cmntList.get(i));

            if (relshpIdList.get(i) == 2){ //2: Spouse
                accnPyrRecord.setInsLNm("COLLINS");
                accnPyrRecord.setInsFNm("GREGORY");
                accnPyrRecord.setInsAddr1("9414 BALLMAN LN");
                accnPyrRecord.setInsAddr2("PT ADDR2");
                accnPyrRecord.setInsZipId("92111");
                accnPyrRecord.setInsDob(new java.sql.Date(DATE_FORMAT_MMDDYYYY.parse("09121980").getTime()));
            }

            accnPyrRecordList.add(accnPyrRecord);
        }

        return accnPyrRecordList;
    }

    private void verifyAccnPyrDataSaved(List<AccnPyr> accnPyrList, List<AccnPyrRecord> expectedAccnPyrRecordList){
        for (int i=0; i<accnPyrList.size(); i++){
            assertEquals(accnPyrList.get(i).getPyrId(), expectedAccnPyrRecordList.get(i).getPyrId());
            assertEquals(accnPyrList.get(i).getGrpId(), expectedAccnPyrRecordList.get(i).getGrpId());
            assertEquals(accnPyrList.get(i).getSubsId(), expectedAccnPyrRecordList.get(i).getSubsId());
            assertEquals(accnPyrList.get(i).getRelshpId(), expectedAccnPyrRecordList.get(i).getRelshpId());
            assertEquals(accnPyrList.get(i).getCmnt(), expectedAccnPyrRecordList.get(i).getCmnt());
            assertEquals(accnPyrList.get(i).getPyrId(), expectedAccnPyrRecordList.get(i).getPyrId());

            if (expectedAccnPyrRecordList.get(i).getRelshpId() == 2) { //2: Spouse
                assertEquals(accnPyrList.get(i).getInsLNm(), expectedAccnPyrRecordList.get(i).getInsLNm());
                assertEquals(accnPyrList.get(i).getInsFNm(), expectedAccnPyrRecordList.get(i).getInsFNm());
                assertEquals(accnPyrList.get(i).getInsAddr1(), expectedAccnPyrRecordList.get(i).getInsAddr1());
                assertEquals(accnPyrList.get(i).getInsAddr2(), expectedAccnPyrRecordList.get(i).getInsAddr2());
                assertEquals(accnPyrList.get(i).getInsZipId(), expectedAccnPyrRecordList.get(i).getInsZipId());
                assertEquals(accnPyrList.get(i).getInsDob(), expectedAccnPyrRecordList.get(i).getInsDob());
            }
        }
    }
    protected boolean waitForHL7EngineToCreateQoe(String accnId, long maxTime)
            throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isQOECreatedForAccnId =  accessionDao.isInQOEQueue(accnId);
        while (!isQOECreatedForAccnId && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for HL7 Engine to create q_oe record for accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isQOECreatedForAccnId =  accessionDao.isInQOEQueue(accnId);
        }
        return isQOECreatedForAccnId;
    }

    protected boolean waitForHL7EngineToCreateQoebyXref(String xRefId, long maxTime)
            throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isQOECreatedForXRefId =  accessionDao.isQOeRecordByXrefIdExists(xRefId);
        while (!isQOECreatedForXRefId && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for HL7 Engine to create q_oe record for xref_id=" + xRefId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isQOECreatedForXRefId =  accessionDao.isQOeRecordByXrefIdExists(xRefId);
        }
        return isQOECreatedForXRefId;
    }

    protected boolean isOutOfQOEQueue(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = accessionDao.isInQOEQueue(accnId);
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit q_oe queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInQueue = accessionDao.isInQOEQueue(accnId);
        }
        return !isInQueue;
    }
}
