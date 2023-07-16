package com.overall.utils;

import com.mbasys.mars.ejb.entity.diagCd.DiagCd;
import com.xifin.accnws.dao.DaoManagerAccnWS;
import com.xifin.accnws.dao.IGenericDaoAccnWS;
import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.mars.dao.IGenericDaoXifinRpm;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.platform.dao.IGenericDaoPlatform;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.rpm.DiagnosisCodeDao;
import com.xifin.qa.dao.rpm.DiagnosisCodeDaoImpl;
import com.xifin.qa.dao.rpm.PatientDaoImpl;
import com.xifin.qa.dao.rpm.PayorDaoImpl;
import com.xifin.qa.dao.rpm.RpmDao;
import com.xifin.qa.dao.rpm.RpmDaoImpl;
import com.xifin.qa.dao.rpm.*;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HL7ParsingEngineUtils
{
    private static final Logger LOG = Logger.getLogger(HL7ParsingEngineUtils.class);
	
    private final RemoteWebDriver driver;
    private final Configuration config;

	private RpmDao rpmDao;
	private IGenericDaoXifinRpm xifinRpmDao;
    private IGenericDaoAccnWS accnWsDao;
    private IGenericDaoPlatform platformDao;
    private PatientDaoImpl patientDaoImpl;
    private PayorDaoImpl payorDaoImpl;
    private DiagnosisCodeDao diagnosisCodeDao;

    public HL7ParsingEngineUtils(RemoteWebDriver driver, Configuration config)
    {
		this.driver = driver;
		this.config = config;
		this.rpmDao = new RpmDaoImpl(config.getRpmDatabase());
		this.xifinRpmDao = new DaoManagerXifinRpm(config.getRpmDatabase());
		this.accnWsDao = new DaoManagerAccnWS(config.getRpmDatabase());
		this.platformDao = new DaoManagerPlatform(config.getRpmDatabase());
		this.patientDaoImpl = new PatientDaoImpl(config.getRpmDatabase());
		this.payorDaoImpl = new PayorDaoImpl(config.getRpmDatabase());
		this.diagnosisCodeDao = new DiagnosisCodeDaoImpl(config.getRpmDatabase());
		diagnosisCodeDao = new DiagnosisCodeDaoImpl(config.getRpmDatabase());
	}

	
    public List<String> getListPIDGenericV231(String accnId, String ptSex, int years, String ptHomePhone, String ptWorkPhone, String ptMaritalStatus, String ptSSN, String ptId) throws Exception{ 
        RandomCharacter randomCharacter = new RandomCharacter();
        List<String> list = new ArrayList<String>();
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);
        
        //String ptId = randomCharacter.getNonZeroRandomNumericString(8);       
        String pLName = "PTLNM" + randomCharacter.getRandomAlphaString(4);
        String pFName = "PTFNM" +randomCharacter.getRandomAlphaString(3);		
        String ptDOB = geNewDate(years, "yyyyMMdd");
        //String ptSex = "M";
        String ptAdd1 = "ADD1 " + randomCharacter.getRandomAlphaString(5);
        String ptAdd2 = "ADD2 " + randomCharacter.getRandomAlphaString(5);
        String ptCity = zips.get(1);
        String ptState = zips.get(3);
        String ptZip = zips.get(0);
        String ptCountry = "US";//platformDao.getCountryFromCOUNTRY(null).get(2);
        //String ptHomePhone = "6196578854";
        //String ptWorkPhone = "8587353125";
        //String ptMaritalStatus = "M";
        String reqId = randomCharacter.getRandomAlphaString(6);
        //String ptSSN = "123456789";
        
        list.add(ptId); // 0
        list.add(accnId); // 1       
        list.add(pLName); // 2
        list.add(pFName); // 3
        list.add(ptDOB); // 4
        list.add(ptSex); // 5
        list.add(ptAdd1); // 6
        list.add(ptAdd2); // 7
        list.add(ptCity); // 8
        list.add(ptState); // 9
        list.add(ptZip); // 10
        list.add(ptCountry); // 11
        list.add(ptHomePhone); // 12
        list.add(ptWorkPhone); // 13
        list.add(ptMaritalStatus); // 14
        list.add(reqId); // 15
        list.add(ptSSN); // 16
        
        return list;
    }	
    
	public List<String> getListPIDNslij(String accnId, String ptId, int years, String ptSex, String ptHomePhone, String ptSSN) throws Exception{ 
	    List<String> list = new ArrayList<String>();
        RandomCharacter randomCharacter = new RandomCharacter();
        TimeStamp timeStamp = new TimeStamp();
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);
        
        String pLName = "PTLNM" + randomCharacter.getRandomAlphaString(4);
        String pFName = "PTFNM" +randomCharacter.getRandomAlphaString(3);	
        String ptDOB = geNewDate(years, "yyyyMMdd");
        String ptAdd1 = "ADD1 " + randomCharacter.getRandomAlphaString(5);
        String ptAdd2 = "ADD2 " + randomCharacter.getRandomAlphaString(5);
        String ptCity = zips.get(1);
        String ptState = zips.get(3);
        String ptZip = zips.get(0);
        String ptCountry = "US";
        String reqId = randomCharacter.getRandomAlphaString(6);
        
        list.add(ptId);//0 Patient ID
        list.add(accnId);//1 Accession ID
        list.add(pLName); // 2 Patient lName
        list.add(pFName); // 3 Patient fName
        list.add(ptDOB);//4 DOB
        list.add(ptSex);//5 Sex
        list.add(ptAdd1);//6 Patient Addr1
        list.add(ptAdd2);//7 Patient Addr2
        list.add(ptCity);//8 City
        list.add(ptState);//9 State
        list.add(ptZip);//10 Zip
        list.add(ptCountry);//11 Country
        list.add(ptHomePhone);//12 Patient Home Phone        
        list.add(reqId);//13  Requisition ID
        list.add(ptSSN);//14  Patient SSN
        
        return list;
    }
	
    public List<String> getListPIDAllinaORM(String ptSex, int years, String ptHomePhone, String ptSSN, String ptId) throws Exception{ 
        RandomCharacter randomCharacter = new RandomCharacter();
        
        List<String> list = new ArrayList<String>();
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);        
               
        String pLName = "PTLNM" + randomCharacter.getRandomAlphaString(4);
        String pFName = "PTFNM" +randomCharacter.getRandomAlphaString(3);	
        String pMName = randomCharacter.getRandomAlphaString(1);
        String ptDOB = geNewDate(years, "yyyyMMdd");        
        String ptAdd1 = "ADD1 " + randomCharacter.getRandomAlphaString(5);
        String ptAdd2 = "ADD2 " + randomCharacter.getRandomAlphaString(5);
        String ptCity = zips.get(1);
        String ptState = zips.get(3);
        String ptZip = zips.get(0);
        String ptCountry = "US";
     
        list.add(pLName); // 0
        list.add(pFName); // 1
        list.add(pMName); // 2
        list.add(ptDOB); // 3
        list.add(ptSex); // 4
        list.add(ptAdd1); // 5
        list.add(ptAdd2); // 6
        list.add(ptCity); // 7
        list.add(ptState); // 8
        list.add(ptZip); // 9
        list.add(ptCountry); // 10
        list.add(ptHomePhone); // 11 
        list.add(ptSSN); // 12
        list.add(ptId); // 13
        
        return list;
    }
    
    public List<String> getListPIDAllinaADT(String ptSex, int years, String ptHomePhone, String ptSSN) throws Exception{ 
        RandomCharacter randomCharacter = new RandomCharacter();
        
        List<String> list = new ArrayList<String>();
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);        
               
        String pLName = "PTLNM" + randomCharacter.getRandomAlphaString(4);
        String pFName = "PTFNM" +randomCharacter.getRandomAlphaString(3);	
        String pMName = randomCharacter.getRandomAlphaString(1);
        String ptDOB = geNewDate(years, "yyyyMMdd");        
        String ptAdd1 = "ADD1 " + randomCharacter.getRandomAlphaString(5);
        String ptAdd2 = "ADD2 " + randomCharacter.getRandomAlphaString(5);
        String ptCity = zips.get(1);
        String ptState = zips.get(3);
        String ptZip = zips.get(0);
        String ptCountry = "US";
     
        list.add(pLName); // 0
        list.add(pFName); // 1
        list.add(pMName); // 2
        list.add(ptDOB); // 3
        list.add(ptSex); // 4
        list.add(ptAdd1); // 5
        list.add(ptAdd2); // 6
        list.add(ptCity); // 7
        list.add(ptState); // 8
        list.add(ptZip); // 9
        list.add(ptCountry); // 10
        list.add(ptHomePhone); // 11 
        list.add(ptSSN); // 12
        
        return list;
    }
    
    public List<String> getListPIDAllinaXTEND(String ptSex, int years, String ptSSN, String accnId, String ptId) throws Exception{ 
        RandomCharacter randomCharacter = new RandomCharacter();
        
        List<String> list = new ArrayList<String>();              
               
        String pLName = "PTLNM" + randomCharacter.getRandomAlphaString(4);
        String pFName = "PTFNM" +randomCharacter.getRandomAlphaString(3);	
        String pMName = randomCharacter.getRandomAlphaString(1);
        String ptDOB = geNewDate(years, "yyyyMMdd");    
     
        list.add(accnId); // 0
        list.add(ptId); // 1
        list.add(pLName); // 2
        list.add(pFName); // 3
        list.add(pMName); // 4
        list.add(ptDOB); // 5
        list.add(ptSex); // 6      
        list.add(ptSSN); // 7
        
        return list;
    }
	
	
    public List<String> getListPV1GenericV231(String phleFacId, String admSrcId) throws Exception{
        TimeStamp timeStamp = new TimeStamp();
        List<String> list = new ArrayList<String>();
        
        String ptLocation = xifinRpmDao.getPatientLocationFromPT_LOC_TYP(null);
        String admissionTyp = accnWsDao.getRandomAdmissionTypeFromADMISSION_TYPE_TYP(null).get(1);
        String ordringPhys = accnWsDao.getPhysicianNPI(null);
        String clnAbbr = xifinRpmDao.getClientId(null);
        String referringPhys = accnWsDao.getPhysicianNPI(null);
        //String phleFacId = "DS";
        //String admissionSource = accnWsDao.getAdmissionSrcTypeFromADMISSION_SRC_TYP("1", null).get(1);
        String admissionSource = accnWsDao.getAdmissionSrcTypeFromADMISSION_SRC_TYP(admSrcId, null).get(1);
        String ptTyp = accnWsDao.getRandomPatientTypeFromPT_TYP(null).get(1);
        String ptStatusId = accnWsDao.getRandomPatientStatusTypeFromPT_STA_TYP(null).get(1);
        String clnProductId = rpmDao.getClnProductTyp(null).get(0).getAbbrev();
        String hospitalAdmitDt = timeStamp.getCurrentDate("YYYYMMdd");
        String hospitalDischargeDt = timeStamp.getCurrentDate("YYYYMMdd");

        list.add(ptLocation); // 0
        list.add(admissionTyp); // 1
        list.add(ordringPhys); // 2
        list.add(clnAbbr); // 3
        list.add(referringPhys); // 4
        list.add(phleFacId); // 5
        list.add(admissionSource); // 6
        list.add(ptTyp); // 7
        list.add(ptStatusId); // 8
        list.add(clnProductId); // 9
        list.add(hospitalAdmitDt); // 10
        list.add(hospitalDischargeDt); // 11
        list.add(phleFacId); // 12
        
        return list;
    }
    
	public List<String> getListPV1Nslij(String ptTyp, String linkAccn, String clnAbbrev) throws Exception{ 
        List<String> list = new ArrayList<String>();
        TimeStamp timeStamp = new TimeStamp();
        RandomCharacter randomCharacter = new RandomCharacter();
        
        
        //        
        //String today = timeStamp.getCurrentDate("yyyyMMdd");
        String ordringPhysNPI = accnWsDao.getPhysicianNPI(null);
        String orderingPhysUPIN = xifinRpmDao.getPhysInfoFromPHYSByNPI(ordringPhysNPI, null).get(13);
        String referringPhysNPI = accnWsDao.getPhysicianNPI(null);
        
        list.add(ptTyp);//0 Patient Type
        list.add(linkAccn);// 1 Link Accession        
        list.add(orderingPhysUPIN);//2 Ordering Physician UPIN
        list.add(ordringPhysNPI);//3 Ordering Physician NPI
        //list.add(platformDao.getRandomFromCLNByCondition(" where FK_DEFAULT_ACCN_PYR_ID = 0 and LENGTH(CLN_ABBREV) > 5 ", null).get(0));//4 Client ID
        list.add(clnAbbrev); //4 Client Abbrev; LENGTH(CLN_ABBREV) > 5
        list.add(referringPhysNPI);//5 Referring Physician NPI 
        
        return list;
	}
	
    public List<String> getListPV1AllinaORM(String ptType, String identifier) throws Exception{       	
       	RandomCharacter randomCharacter = new RandomCharacter();
        TimeStamp timeStamp = new TimeStamp();
        List<String> list = new ArrayList<String>();
        String ptClass = "ED";
        String assignedPtLocation1 = randomCharacter.getRandomAlphaString(6); 
        String assignedPtLocation2 = randomCharacter.getNonZeroRandomNumericString(2);
        String assignedPtLocation3 = randomCharacter.getNonZeroRandomNumericString(2);
        String ordPhys1 = randomCharacter.getRandomAlphaNumericString(6); 
        String ordPhys2 = randomCharacter.getRandomAlphaString(6);
        String ordPhys3 = randomCharacter.getRandomAlphaString(3);
        String ordPhys4 = randomCharacter.getRandomAlphaString(8); 
        String referUPIN1 = randomCharacter.getRandomAlphaNumericString(7); 
        String referUPIN2 = randomCharacter.getRandomAlphaString(10);
        String referUPIN3 = randomCharacter.getRandomAlphaString(6);
        String AdmitDateTime = timeStamp.getCurrentDate("YYYYMMdd");
        
        list.add(ptClass); // 0
        list.add(assignedPtLocation1); // 1
        list.add(assignedPtLocation2); // 2
        list.add(assignedPtLocation3); // 3
        list.add(ordPhys1); // 4
        list.add(ordPhys2); // 5
        list.add(ordPhys3); // 6
        list.add(ordPhys4); // 7
        list.add(referUPIN1); // 8
        list.add(referUPIN2); // 9
        list.add(referUPIN3); // 10
        list.add(ptType); // 11 ptType
        list.add(identifier); // 12 visitNumber
        list.add(AdmitDateTime); // 13
        
        return list;
    }
    
    public List<String> getListPV1AllinaADT(String xRefId, String admSrcId, String ptTyp) throws Exception{ 
        
        TimeStamp timeStamp = new TimeStamp();
        List<String> list = new ArrayList<String>();
        
        String ordringPhysNPI = accnWsDao.getPhysicianNPI(null);
        String admissionTyp = accnWsDao.getRandomAdmissionTypeFromADMISSION_TYPE_TYP(null).get(1);        
        ArrayList<String> physInfoList = xifinRpmDao.getPhysInfoFromPHYSByNPI(ordringPhysNPI, null);
        String orderingPhysUPIN = physInfoList.get(13);
        String ordringPhysrLNm = physInfoList.get(1); 
        String ordringPhysrFNm = physInfoList.get(2); 
        String ordringPhysrMNm = "";
        String admissionSource = accnWsDao.getAdmissionSrcTypeFromADMISSION_SRC_TYP(admSrcId, null).get(1);        
        ArrayList<String> ptStatusList = accnWsDao.getRandomPatientStatusTypeFromPT_STA_TYP(null);
        String ptStatusId = ptStatusList.get(1);      
        String hospitalAdmitDt = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String hospitalDischargeDt = hospitalAdmitDt;
        String ptStaTypId = ptStatusList.get(2);
       
        list.add(ptTyp); // 0 Pt typ
        list.add(admissionTyp); // 1 AdmissionTyp
        list.add(orderingPhysUPIN); //2 Ordering Phys NPI
        list.add(ordringPhysrLNm); //3 Ordering Phys Last Name
        list.add(ordringPhysrFNm); //4 Ordering Phys First Name
        list.add(ordringPhysrMNm); //5 Ordering Phys Middle Name
        list.add(admissionSource); //6 AdmissionSrc
        list.add(xRefId); //7 Xref ID
        list.add(ptStatusId); // 8 PtStaId        
        list.add(hospitalAdmitDt); // 9 Admit Dt
        list.add(hospitalDischargeDt); // 10 Discharge Dt    
        list.add(ptStaTypId); //11
        list.add(ordringPhysNPI); //12
        
        return list;
    }
    
    public List<String> getListPV1AllinaXTEND(String xRefId) throws Exception{ 
                
        List<String> list = new ArrayList<String>();
        
        String ordringPhysNPI = accnWsDao.getPhysicianNPI(null);       
        ArrayList<String> physInfoList = xifinRpmDao.getPhysInfoFromPHYSByNPI(ordringPhysNPI, null);
        String orderingPhysUPIN = physInfoList.get(13);
        String ordringPhysrLNm = physInfoList.get(1); 
        String ordringPhysrFNm = physInfoList.get(2); 
        String ordringPhysrMNm = "";

        String clnAbbrev = xifinRpmDao.getClientId(null);
       
        list.add(orderingPhysUPIN); //0 Ordering Phys UPIN
        list.add(ordringPhysrLNm); //1 Ordering Phys Last Name
        list.add(ordringPhysrFNm); //2 Ordering Phys First Name
        list.add(ordringPhysrMNm); //3 Ordering Phys Middle Name
        list.add(clnAbbrev); //4 clnAbbrev 
        list.add(xRefId); //5 XrefId
        list.add(ordringPhysNPI); //6
        
        return list;
    }
	
	
  /*
	public String getDianosigCdDoNotDublicate(String dianosigCd) throws Exception{ 
		
		
		String dianosigCdTypId = xifinRpmDao.getDataValueFromSYSTEMSETTING(57, null);
		String dianosigCdTyp = xifinRpmDao.getDianogisTypFromDIAGCDTYPByTypId(dianosigCdTypId,null);
 		String newDianosigCd = accnWsDao.getDiagnosisCode(dianosigCdTyp,null);
		while(newDianosigCd.equals(dianosigCd)){
			newDianosigCd = accnWsDao.getDiagnosisCode(dianosigCdTyp,null);
		}
		return newDianosigCd;
	}
*/
	
	public boolean deleteFile(String filePath, String fileName){
		boolean flag = false;
		
		String file = filePath + fileName;
		File f = new File(file);
		
		if(f.delete()){
			flag = true;
			LOG.info("        Deleted File: " + file );
		}else{
			flag = false;
		}
		
		return flag;
	}
	
	//Can add single test or self-explode test profile
    public List<String> getListFT1WithTestsGenericV231(boolean isProfTest, String transTyp, String unit, String abnReceivedFlag, String isRoundTrip, String testFacId, String specifiedTestAbbrev, String dos) throws Exception{ 
        TimeStamp timeStamp = new TimeStamp();
        RandomCharacter randomCharacter = new RandomCharacter();
        
        
        
        List<String> list = new ArrayList<String>();
        String testAbbrev = "";
        String profileId = "";
        //String effDt = geNewDate(1, "MM-dd-yyyy");   
        String effDt = geNewDate(1, "MM/dd/yyyy"); 
        String lisTraceId = randomCharacter.getNonZeroRandomNumericString(5);
        //String dos = timeStamp.getCurrentDate("YYYYMMdd");
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
       
        if (isProfTest == true) {
            List<String> testProfileList = platformDao.getSelfExplodeTestProf(null);
            testAbbrev = testProfileList.get(1);
        } else {
            if (testAbbrev.equals("")){
        		List<String> testList =  xifinRpmDao.getSingleTestByTestFacIdTestEffDt(testFacId, effDt, null);
        		testAbbrev = testList.get(1);
        	}
            else{
            	testAbbrev = specifiedTestAbbrev;
            }
        }
        String altTestName = randomCharacter.getRandomAlphaString(5);        
        //String abnReceivedFlag = "Y";
        String manualPrice = randomCharacter.getNonZeroRandomNumericString(2);
        String performingFacId = accnWsDao.getFacAbbrevFromFACByFacId("1",  null);//xifinRpmDao.getFacInfoFromFAC(null).get(0);
        String testNote = randomCharacter.getRandomAlphaString(5);;
        String testLevelDiagCd = accnWsDao.getDiagnosisCode(null);
        //String profileId = platformDao.getTestprofFromTEST(null).get(1);
        String receiptDt = timeStamp.getCurrentDate("YYYYMMdd");               
        String mileage = randomCharacter.getNonZeroRandomNumericString(2);
        String numOfStops = randomCharacter.getNonZeroRandomNumericString(1);
        String numOfPt = numOfStops;   
        //String isRoundTrip = "Y";
        String phleName = "POOH BEAR";         
        String mod = "2E";//xifinRpmDao.getRandomModId(null);
        String renderingPhysNPI = accnWsDao.getPhysicianNPI(null);
        
        list.add(lisTraceId); // 0
        list.add(dos); // 1
        list.add(finalReportDt); // 2
        list.add(transTyp); // 3
        list.add(testAbbrev); // 4
        list.add(altTestName); // 5
        list.add(unit); // 6
        list.add(abnReceivedFlag); // 7
        list.add(manualPrice); // 8
        list.add(performingFacId); // 9
        list.add(testNote); // 10
        list.add(testLevelDiagCd); // 11
        list.add(profileId); // 12
        list.add(receiptDt); // 13
        //list.add(travelFeeData); // 14
        list.add(mileage); // 14
        list.add(numOfStops); // 15
        list.add(numOfPt); // 16
        list.add(isRoundTrip); // 17
        list.add(phleName); // 18
        list.add(mod); // 19
        list.add(renderingPhysNPI); // 20
        
        return list;
    }
    
	//Can add multiple single tests or self-explode test profiles
    public List<String> getListFT1Nslij(boolean isProfTest, String transTyp, String testFacId, String specifiedTestAbbrev, String dos) throws Exception{ 
        TimeStamp timeStamp = new TimeStamp();
        //RandomCharacter randomCharacter = new RandomCharacter();
        
        
        
        List<String> list = new ArrayList<String>();
        String testAbbrev = "";
        String testName = "";
        String effDt = geNewDate(1, "MM/dd/yyyy"); 
        String finalReportDt = timeStamp.getCurrentDate("yyyyMMdd");
       
        if (isProfTest == true) {
            List<String> testProfileList = platformDao.getSelfExplodeTestProf(null);
            testAbbrev = testProfileList.get(1);
            testName = testProfileList.get(2);
        } else {            
        	if (specifiedTestAbbrev.equals("")){
        		List<String> testList =  xifinRpmDao.getSingleTestByTestFacIdTestEffDt(testFacId, effDt, null);
        		testAbbrev = testList.get(1);
        		testName = testList.get(2);
        	}
            else{
            	testAbbrev = specifiedTestAbbrev;
            	testName = xifinRpmDao.getTestInfoFromTESTByTestAbbrev(specifiedTestAbbrev, null).get(1);
            }
        } 
        String performingFacId = accnWsDao.getFacAbbrevFromFACByFacId("1",  null);//xifinRpmDao.getFacInfoFromFAC(null).get(0);
        
        list.add(dos); // 0
        list.add(finalReportDt); // 1
        list.add(transTyp); // 2       
        list.add(testAbbrev); // 3
        list.add(testName); // 4
        list.add(performingFacId); // 5 
        
        return list;
    }
    
	
    public List<String> getListZXTGenericV231(String testSpecificQuestion, String abnReceivedFlag, String renalFlag, String payorToBill) throws Exception{ 
        RandomCharacter randomCharacter = new RandomCharacter();
        //
        List<String> list = new ArrayList<String>();
        String testNote = randomCharacter.getRandomAlphaNumericString(5);
        //String payorToBill = "C";
        //String testSpecificQuestion = "1";//xifinRpmDao.getQuestionFromQUESTIONByCondition("PK_QUESTN_ID != 0", null).get(1);
        //String abnReceivedFlag = "Y";
        String pyrSvcAuthNumber = randomCharacter.getNonZeroRandomNumericString(10);
        //String renalFlag = "Y";
        
        list.add(testNote); // 0
        list.add(payorToBill); // 1
        list.add(testSpecificQuestion); // 2
        list.add(abnReceivedFlag); // 3
        list.add(pyrSvcAuthNumber); //4
        list.add(renalFlag); // 5
        
        return list;
    }	
   
    public List<String> getListIN1GenericV231(boolean isInvalidPyr, String insRelTyp, String eligStatus, String insSex) throws Exception{
        
        
        //
        RandomCharacter randomCharacter = new RandomCharacter();
        TimeStamp timeStamp = new TimeStamp();
        List<String> list = new ArrayList<String>();        
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);
        String pyrAbbrev = payorDaoImpl.getThirdPartyPyrFromPYR().pyrAbbrv;


        List<String> pyrInfoList = platformDao.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, null);
        String pyrId = "";
        
        if(isInvalidPyr){
        	pyrId = "INVALIDPYR";
        }
        else{
        	pyrId = pyrAbbrev;
        }
        //String pyrId = pyrAbbrev;
        String pyrName = pyrInfoList.get(2);
        String ptAddr1 = "ADDR1 " + randomCharacter.getRandomAlphaString(5);
        String ptAddr2 = "ADDR2 " + randomCharacter.getRandomAlphaString(5);
        String ptCity = zips.get(1);
        String ptState = zips.get(2);
        String ptZip = zips.get(0);
        String ptCountry = "US";//platformDao.getCountryFromCOUNTRY(null).get(2);
        String grpId = randomCharacter.getNonZeroRandomNumericString(5);
        String insEffDt = timeStamp.getCurrentDate("YYYYMMdd");
        String insName = randomCharacter.getRandomAlphaString(5);
        //String insRelTyp = "C";// C = Child
        String insDOB = timeStamp.getCurrentDate("YYYYMMdd");
        String insAddr1 = "ADDR1 " + randomCharacter.getRandomAlphaString(5);
        String insAddr2 = "ADDR2 " + randomCharacter.getRandomAlphaString(5);
        String insCity = zips.get(1);
        String insState = zips.get(2);
        String insZip = zips.get(0);
        String insCountry = "US";//platformDao.getCountryFromCOUNTRY(null).get(2);
        String cobPyrPrio = "1";
        //String eligStatus = "Y";
        String elibChkDt = timeStamp.getCurrentDate("YYYYMMdd");
        //String insSex = "M";
        String subsId = randomCharacter.getNonZeroRandomNumericString(8);
        
        list.add(pyrId); // 0
        list.add(pyrName); // 1
        list.add(ptAddr1); // 2
        list.add(ptAddr2); // 3
        list.add(ptCity); // 4
        list.add(ptState); // 5
        list.add(ptZip); // 6
        list.add(ptCountry); // 7
        list.add(grpId); // 8
        list.add(insEffDt); // 9
        list.add(insName); // 10
        list.add(insRelTyp); // 11
        list.add(insDOB); // 12
        list.add(insAddr1); // 13
        list.add(insAddr2); // 14
        list.add(insCity); // 15
        list.add(insState); // 16
        list.add(insZip); // 17
        list.add(insCountry); // 18
        list.add(cobPyrPrio); // 19
        list.add(eligStatus); // 20
        list.add(elibChkDt); // 21
        list.add(insSex); // 22
        list.add(subsId); // 23
        
        return list;
    }
    
    public List<String> getListIN1Nslij(boolean isInvalidPyr, String insRelTyp) throws Exception{
        
               
        RandomCharacter randomCharacter = new RandomCharacter();
        TimeStamp timeStamp = new TimeStamp();
        List<String> list = new ArrayList<String>();        
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);
        String pyrAbbrev = payorDaoImpl.getThirdPartyPyrFromPYR().pyrAbbrv;

        String pyrId = "";
        
        if(isInvalidPyr){
        	pyrId = "INVALIDPYR";
        }
        else{
        	pyrId = pyrAbbrev;
        }       
        	
        //Note: When a second HL7 message is sent for an accession, and the accession is available for updating, the accession is only updated when a date is sent in IN1-12 or PV1-3.
        String dtToUpdate = timeStamp.getCurrentDate("yyyyMMdd");
        String insLName = "INSLNM" + randomCharacter.getRandomAlphaString(4);
        String insFName = "INSFNM" +randomCharacter.getRandomAlphaString(3);
        String insAddr1 = "ADDR1 " + randomCharacter.getRandomAlphaString(5);
        String insAddr2 = "ADDR2 " + randomCharacter.getRandomAlphaString(5);
        String insCity = zips.get(1);
        String insState = zips.get(2);
        String insZip = zips.get(0);
        String insCountry = "US";
        String subsId = randomCharacter.getNonZeroRandomNumericString(8);

        list.add(pyrId); // 0     
        list.add(dtToUpdate); // 1
        list.add(insLName); // 2
        list.add(insFName); // 3
        list.add(insRelTyp); // 4
        list.add(insAddr1); // 5
        list.add(insAddr2); // 6
        list.add(insCity); // 7
        list.add(insState); // 8
        list.add(insZip); // 9
        list.add(insCountry); // 10 
        list.add(subsId); // 11 
        
        return list;
    }
    
    public List<String> getListIN1AllinaADT(boolean isInvalidPyr, String insRelTyp, String eligStatus, String pyrPrio, String subsId) throws Exception{
        
               
        RandomCharacter randomCharacter = new RandomCharacter();
        
        List<String> list = new ArrayList<String>();        
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);
        String pyrAbbrev = platformDao.getThirdPartyPyrFromPYR(null);
        List<String> pyrInfoList = platformDao.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, null);
        String pyrId = "";
        
        if(isInvalidPyr){
        	pyrId = "INVALIDPYR";
        }
        else{
        	pyrId = pyrAbbrev;
        }
       
        String pyrName = pyrInfoList.get(2);
        String pyrAddr1 = "ADDR1 " + randomCharacter.getRandomAlphaString(5);
        String pyrAddr2 = "ADDR2 " + randomCharacter.getRandomAlphaString(5);
        String pyrCity = zips.get(1);
        String pyrState = zips.get(2);
        String pyrZip = zips.get(0);
        String pyrCountry = "US";
        String grpId = randomCharacter.getNonZeroRandomNumericString(5);        
        String insLNm = randomCharacter.getRandomAlphaString(5);
        String insFNm = randomCharacter.getRandomAlphaString(6);
    
        String insAddr1 = "ADDR1 " + randomCharacter.getRandomAlphaString(5);
        String insAddr2 = "ADDR2 " + randomCharacter.getRandomAlphaString(5);
        String insCity = zips.get(1);
        String insState = zips.get(2);
        String insZip = zips.get(0);
        String insCountry = "US";
        
        list.add(pyrId); // 0 Payor Abbrev
        list.add(pyrName); // 1 Payor Name
        list.add(pyrAddr1); // 2 Payor Address
        list.add(pyrAddr2); // 3 Payor Address
        list.add(pyrCity); // 4 Payor Address
        list.add(pyrState); // 5 Payor Address
        list.add(pyrZip); // 6 Payor Address
        list.add(pyrCountry); // 7 Payor Address
        list.add(grpId); // 8 
        list.add(eligStatus); // 9 Elig Status
        list.add(insLNm); // 10 Ins FN/LN
        list.add(insFNm); // 11 Ins FN/LN
        list.add(insRelTyp); // 12 Ins RelationShip
        list.add(insAddr1); // 13 Ins Address
        list.add(insAddr2); // 14 Ins Address
        list.add(insCity); // 15 Ins Address
        list.add(insState); // 16 Ins Address
        list.add(insZip); // 17 Ins Address
        list.add(insCountry); // 18 Ins Address
        list.add(pyrPrio); // 19 Payor prio            
        list.add(subsId); // 20 Sub ID
        
        return list;
    }
    
    public List<String> getListIN2AllinaADT() throws Exception{
    	RandomCharacter randomCharacter = new RandomCharacter();
    	List<String> list = new ArrayList<String>();
    	
        String subsId = randomCharacter.getNonZeroRandomNumericString(8);        
         
        list.add(subsId); //0 Pyr Sub ID
        
        return list;
    }

	public List<String> getListDG1GenericV231(String pyrAbbrev, boolean isFreeTxt) throws Exception{ 
	    
	    
	    List<String> diagCdList =   new ArrayList<String>();
	    
	    if (!isFreeTxt){
            String diagCdTyp = platformDao.getPyrDtByPyrAbbrev(pyrAbbrev, null).get(3);
            DiagCd diagCd  = diagnosisCodeDao.getDiagCdByDiagTypId( Integer.parseInt(diagCdTyp));
            diagCdList.add(diagCd.diagCdId);//0
	    }
	    else{
	    	diagCdList.add("Free Text" + new RandomCharacter().getRandomAlphaString(5));//0
	    }	    
	
        return diagCdList;
    }
	
	public List<String> getListDG1Nslij(String pyrAbbrev, boolean isFreeTxt) throws Exception{ 
	    
	    
	    List<String> diagCdList =   new ArrayList<String>();
	    
	    if (!isFreeTxt){

	    	String diagCdTyp = platformDao.getPyrDtByPyrAbbrev(pyrAbbrev, null).get(3);
	    	DiagCd diagCd  = diagnosisCodeDao.getDiagCdByDiagTypId( Integer.parseInt(diagCdTyp));

	    	diagCdList.add(diagCd.diagCdId);//0
	    }
	    else{
	    	diagCdList.add("Free Text" + new RandomCharacter().getRandomAlphaString(5));//0
	    }	    
	
        return diagCdList;
    }	
	
	
	/*//////////////////
	public List<String> getListIN1SInvalidPayor() throws Exception{ 
	    
	    
	    RandomCharacter randomCharacter = new RandomCharacter();
	    TimeStamp timeStamp = new TimeStamp();
		List<String> list = new ArrayList<String>();
        List<String> pyrInfo = platformDao.getPyrInfoFromPyr(null);
        List<String> reltsh = platformDao.getPyrInfoFromPyr(null);
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);
        list.add(randomCharacter.getRandomAlphaString(4)); // 0 PyrId
        list.add(pyrInfo.get(2)+"SSS"); // 1 PyrName
        list.add(timeStamp.getCurrentDate("YYYYMMdd")); //2
        list.add(randomCharacter.getRandomAlphaNumericString(4)); //3
        list.add(randomCharacter.getRandomAlphaNumericString(3)); //4
        list.add(reltsh.get(1)); //5
        list.add(randomCharacter.getRandomAlphaNumericString(3)); //6
        list.add(randomCharacter.getRandomAlphaNumericString(3)); //7
        list.add(zips.get(2)); //8
        list.add(zips.get(0)); //9
        list.add(timeStamp.getCurrentDate("YYYYMMdd")); //10
        return list;
    }
    */	
	
    public List<String> getListGT1GenericV231(String guarantorHomePhone, int years, String guarantorSex, String guarantorRelshpTyp, String guarantorSSN, String employerPhone) throws Exception{ 
        //TimeStamp timeStamp = new TimeStamp();
        RandomCharacter randomCharacter = new RandomCharacter();
        
        //
        List<String> list = new ArrayList<String>();
        //List<String> pyrInfo = platformDao.getPyrInfoFromPYRByPyrAbbrev("G", null);//xifinRpmDao.getPyrFromPyrByPyrAbbrev("G", null);        
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);
        
        String guarantorName = randomCharacter.getRandomAlphaString(5);
        String guarantorAdd1 = "ADD1 " + randomCharacter.getRandomAlphaString(5);
        String guarantorAdd2 = "ADD2 " + randomCharacter.getRandomAlphaString(5);
        String guarantorCity =  zips.get(1);
        String guarantorState = zips.get(2);
        String guarantorZipId = zips.get(0);
        String guarantorCountry = "US";
        //String guarantorHomePhone = "6197774568";
        //String guarantorDOB = geNewDate(40, "yyyyMMdd");
        String guarantorDOB = geNewDate(years, "yyyyMMdd");
        //String guarantorSex = "M";
        //String guarantorRelshpTyp = "C"; //C = Child
        //String guarantorSSN = "987456789";
        String employerName = randomCharacter.getRandomAlphaString(10);
        String employerAdd1 = "ADD1 " + randomCharacter.getRandomAlphaString(5);
        String employerAdd2 = "ADD2 " + randomCharacter.getRandomAlphaString(5);
        String employerCity = zips.get(1);
        String employerState = zips.get(2);
        String employerZip = zips.get(0);
        String employerCountry = "US";
        //String employerPhone = "8586217459";
        
        list.add(guarantorName); // 0
        list.add(guarantorAdd1); // 1
        list.add(guarantorAdd2); // 2
        list.add(guarantorCity); // 3
        list.add(guarantorState); // 4
        list.add(guarantorZipId); // 5
        list.add(guarantorCountry); // 6
        list.add(guarantorHomePhone); // 7
        list.add(guarantorDOB); // 8
        list.add(guarantorSex); // 9
        list.add(guarantorRelshpTyp); // 10
        list.add(guarantorSSN); // 11
        list.add(employerName); // 12
        list.add(employerAdd1); // 13
        list.add(employerAdd2); // 14
        list.add(employerCity); // 15
        list.add(employerState); // 16
        list.add(employerZip); // 17
        list.add(employerCountry); // 18
        list.add(employerPhone); // 19
        
        return list;
    }
    
    public List<String> getListGT1ArxHl7V231(String guarantorHomePhone, int years, String guarantorSex, String guarantorRelshpTyp, String guarantorSSN, String employerPhone, String guarantorWorkPhone) throws Exception{ 
        
        RandomCharacter randomCharacter = new RandomCharacter();
                
        List<String> list = new ArrayList<String>();               
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);
        
        String guarantorName = randomCharacter.getRandomAlphaString(5);
        String guarantorAdd1 = "ADD1 " + randomCharacter.getRandomAlphaString(5);
        String guarantorAdd2 = "ADD2 " + randomCharacter.getRandomAlphaString(5);
        String guarantorCity =  zips.get(1);
        String guarantorState = zips.get(2);
        String guarantorZipId = zips.get(0);
        String guarantorCountry = "US";       
        String guarantorDOB = geNewDate(years, "yyyyMMdd");
        String employerName = randomCharacter.getRandomAlphaString(10);
        String employerAdd1 = "ADD1 " + randomCharacter.getRandomAlphaString(5);
        String employerAdd2 = "ADD2 " + randomCharacter.getRandomAlphaString(5);
        String employerCity = zips.get(1);
        String employerState = zips.get(2);
        String employerZip = zips.get(0);
        String employerCountry = "US";      
        //Arx specific
        String guarantorSpouseNm = randomCharacter.getRandomAlphaString(6);
        
        list.add(guarantorName); // 0
        list.add(guarantorAdd1); // 1
        list.add(guarantorAdd2); // 2
        list.add(guarantorCity); // 3
        list.add(guarantorState); // 4
        list.add(guarantorZipId); // 5
        list.add(guarantorCountry); // 6
        list.add(guarantorHomePhone); // 7
        list.add(guarantorDOB); // 8
        list.add(guarantorSex); // 9
        list.add(guarantorRelshpTyp); // 10
        list.add(guarantorSSN); // 11
        list.add(employerName); // 12
        list.add(employerAdd1); // 13
        list.add(employerAdd2); // 14
        list.add(employerCity); // 15
        list.add(employerState); // 16
        list.add(employerZip); // 17
        list.add(employerCountry); // 18
        list.add(employerPhone); // 19
        //Arx specific
        list.add(guarantorSpouseNm); // 20
        list.add(guarantorWorkPhone); // 21
        
        return list;
    }
    
   public List<String> getListGT1AllinaADT(String gtHomePhone, int years, String gtRelshpTyp, String gtSSN) throws Exception{ 
        
        RandomCharacter randomCharacter = new RandomCharacter();
                
        List<String> list = new ArrayList<String>();               
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);
        
        String gtName = randomCharacter.getRandomAlphaString(5);
        String gtAdd1 = "ADD1 " + randomCharacter.getRandomAlphaString(5);
        String gtAdd2 = "ADD2 " + randomCharacter.getRandomAlphaString(5);
        String gtCity =  zips.get(1);
        String gtState = zips.get(2);
        String gtZipId = zips.get(0);
        String gtCountry = "US";          
        
        list.add(gtName); // 0
        list.add(gtAdd1); // 1 GT address
        list.add(gtAdd2); // 2 GT address
        list.add(gtCity); // 3 GT address
        list.add(gtState); // 4 GT address
        list.add(gtZipId); // 5 GT address
        list.add(gtCountry); // 6 GT address
        list.add(gtHomePhone); // 7 GT Homephone       
        list.add(gtRelshpTyp); // 8 Relationship 
        list.add(gtSSN); // 9 GT SSN
        
        return list;
    }
   
   public List<String> getListACCAllinaADT() throws Exception{        
               
       List<String> list = new ArrayList<String>();               
       ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);       

       String gtState = zips.get(2);      
 
       list.add(gtState); // 0 GT address
       
       return list;
   }
		
    public List<String> getListZXAGenericV231(String mspReceivedFlag) throws Exception{ 
        RandomCharacter randomCharacter = new RandomCharacter();
        
        
        
        TimeStamp timeStamp = new TimeStamp();
        List<String> list = new ArrayList<String>();
        ArrayList<String> zips = xifinRpmDao.getZipCodeFromZIP(null);

        String clnSpecQuestionId = xifinRpmDao.getQuestionFromQUESTIONByCondition("PK_QUESTN_ID != 0", null).get(1);
        String ptGravida = randomCharacter.getNonZeroRandomNumericString(1);
        String physSignOnFileDt = timeStamp.getCurrentDate("YYYYMMdd");
        String accnNote = "CNTCT_INFO "+randomCharacter.getRandomAlphaString(15);
        String clinicalTrialData = randomCharacter.getRandomAlphaString(10);
        String phlebotomistId = platformDao.getPhlebotomist(null).get(0);
        String dialysisType = accnWsDao.getRandomDialTypFromDIAL_TYP(null).get(1);
        String onsetDate = timeStamp.getCurrentDate("YYYYMMdd");
        ArrayList<String> onSetTypList = accnWsDao.getRandomOnsetTypeFromONSET_TYP(null);
        String onsetTypAbbrev = onSetTypList.get(1);
        ArrayList<String> accidentCauseTypList = accnWsDao.getRandomAccidentCauseTypeFromACCIDENT_CAUSE_TYP(null);
        String accidentCauseAbbrev = accidentCauseTypList.get(1);
        String indigentPercent = randomCharacter.getNonZeroRandomNumericString(2);
        String printableNote = randomCharacter.getRandomAlphaString(15);
        //String mspReceivedFlag = "Y";
        String firstDialysisDt = timeStamp.getCurrentDate("YYYYMMdd");
        String clnBillingCategory = xifinRpmDao.getCBCInfoFromCLNBILLINGCATEGORY(null).get(0); // Will read by generic HL7 when ss#102 (Use Client Billing Categories) is true
        String accidentState = zips.get(2);
        
        list.add(clnSpecQuestionId); // 0
        list.add(ptGravida); // 1
        list.add(physSignOnFileDt); // 2
        list.add(accnNote); // 3
        list.add(clinicalTrialData); // 4
        list.add(phlebotomistId); // 5 
        list.add(dialysisType); // 6
        list.add(onsetDate); // 7
        list.add(onsetTypAbbrev); // 8
        list.add(accidentCauseAbbrev); // 9
        list.add(indigentPercent); // 10
        list.add(printableNote); // 11
        list.add(mspReceivedFlag); // 12
        list.add(firstDialysisDt); // 13
        list.add(clnBillingCategory); // 14
        list.add(accidentState); // 15
        
        //For validation only
        String onSetTypId = onSetTypList.get(2);
        list.add(onSetTypId); // 16
        String accidentCauseId = accidentCauseTypList.get(2);
        list.add(accidentCauseId); // 17
        
        return list;
    }
	
	public String geNewDate(int years, String dtFormat){
        //generate a new DOB
        Calendar cal = Calendar.getInstance();        
        int y  = cal.get(Calendar.YEAR) - years;
        int m  = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DATE);
        cal.set(y,m,d);
        Date date = cal.getTime();             
        SimpleDateFormat format = new SimpleDateFormat(dtFormat);
        String dob = format.format(date); 
        
        return dob;
	}
	
	
    //Create HL7 file for GenericV231 parser
    public String createHL7FileGenericV231MultiAccn(String time,List<List<String>> pids,List<List<String>> pv1s,List<String> dg1s, List<List<String>> in1s, List<List<String>> gt1s, List<List<String>> ft1s, 
    		List<List<String>> zxts,List<String> ptTypeDescs,List<List<String>> zxas, String finalReportDt, boolean includeFT1ZXT) throws Exception {
    	
        String fileContents = "",contents = "", ptId = "";

        String header = "FHS|^~\\&|||||" + time + "||||||\r\n"
                      + "BHS|^~\\&|||||" + time + "||||||\r\n";

        if(pids.size() == pv1s.size() && pv1s.size() == in1s.size() && in1s.size() == gt1s.size() && gt1s.size()== ft1s.size()) {
            for(int i = 0; i<pids.size();i++) {
                String msh = "MSH|^~\\&|BIOTECH XRAY||||201307110711||DFT^P03||P|2.4\r\n";
                ptId = "";
                for(String ptTypeDesc:ptTypeDescs) {
                    switch (ptTypeDesc) {
                    case "ENTERPRISE":
                    	ptId += pids.get(i).get(0)+"&ENTERPRISE~";
                        break;
                    case "CLIENT":
                    	ptId += pids.get(i).get(0)+"&CLIENT~";
                        break;
                    case "FACILITY":
                    	ptId += pids.get(i).get(0)+"&FACILITY~";
                        break;
                    case "DEFAULT":
                    	ptId += pids.get(i);
                    default:
                        break;
                    }
                    
                }                
 
                String pid = "PID|1|"+ptId+"|"+pids.get(i).get(1)+"||"+pids.get(i).get(2)+ "^" + pids.get(i).get(3) + "||"+pids.get(i).get(4)+"|"+pids.get(i).get(5)+"|||"+pids.get(i).get(6)+"^"+pids.get(i).get(7)+"^"+pids.get(i).get(8)+"^"+pids.get(i).get(9)+"^"+pids.get(i).get(10)+"^"+pids.get(i).get(11)+""
                        + "||"+pids.get(i).get(12)+"|"+pids.get(i).get(13)+"||"+pids.get(i).get(14)+"||"+pids.get(i).get(15)+"|"+pids.get(i).get(16)+"\r\n";

                String pv1 = "PV1|1||"+pv1s.get(i).get(0)+"|"+pv1s.get(i).get(1)+"|||"+pv1s.get(i).get(2)+"^^^^^^^^^^^^NPI|"+pv1s.get(i).get(3)+"|"+pv1s.get(i).get(4)+"^^^^^^^^^^^^NPI||"+pv1s.get(i).get(5)+"|||"+pv1s.get(i).get(6)+"||||"+pv1s.get(i).get(7)+"|||||||||||||||||"
                        + "|"+pv1s.get(i).get(8)+"|||"+pv1s.get(i).get(9)+"|||||"+pv1s.get(i).get(10)+"|"+pv1s.get(i).get(11)+"||\r\n";
                
                String dg1 = "DG1|1||" + dg1s.get(0) + "\r\n";

                String in1 = "IN1|1|"+in1s.get(i).get(0)+"||"+in1s.get(i).get(1)+"|"+in1s.get(i).get(2)+"^"+in1s.get(i).get(3)+"^"+in1s.get(i).get(4)+"^"+in1s.get(i).get(5)+"^"+in1s.get(i).get(6)+"^"+in1s.get(i).get(7)+"|||"+in1s.get(i).get(8)+"|||"
                        + "|"+in1s.get(i).get(9)+"||||"+in1s.get(i).get(10)+"^" + in1s.get(i).get(10) + "|"+in1s.get(i).get(11)+"|"+in1s.get(i).get(12)+"|"+in1s.get(i).get(13)+"^"+in1s.get(i).get(14)+"^"+in1s.get(i).get(15)+"^"+in1s.get(i).get(16)+"^"+in1s.get(i).get(17)+"^"+in1s.get(i).get(18)+"||"
                                + "|"+in1s.get(i).get(19)+"|||"+in1s.get(i).get(20)+"|"+in1s.get(i).get(21)+"|||||||||||||||||"+in1s.get(i).get(22)+"||||||"+in1s.get(i).get(23)+"\r\n";

                String gt1 = "GT1|1||"+gt1s.get(i).get(0) +"^" +gt1s.get(i).get(0) +"||"+gt1s.get(i).get(1)+"^"+gt1s.get(i).get(2)+"^"+gt1s.get(i).get(3)+"^"+gt1s.get(i).get(4)+"^"+gt1s.get(i).get(5)+"^"+gt1s.get(i).get(6)+"|"+gt1s.get(i).get(7)+"||"+gt1s.get(i).get(8)+"|"+gt1s.get(i).get(9)+"||"
                        + ""+gt1s.get(i).get(10)+"|"+gt1s.get(i).get(11)+"||||"+gt1s.get(i).get(12)+"|"+gt1s.get(i).get(13)+"^"+gt1s.get(i).get(14)+"^"+gt1s.get(i).get(15)+"^"+gt1s.get(i).get(16)+"^"+gt1s.get(i).get(17)+"^"+gt1s.get(i).get(18)+"|"+gt1s.get(i).get(19)+"\r\n";
                
                String ft1 = "FT1|"+ft1s.get(i).get(0)+"|||"+ft1s.get(i).get(1)+"|"+ft1s.get(i).get(2)+"|"+ft1s.get(i).get(3)+"|"+ft1s.get(i).get(4)+"||"+ft1s.get(i).get(5)+"|"+ft1s.get(i).get(6)+"|"+ft1s.get(i).get(7)+"|"+ft1s.get(i).get(8)+"|" +ft1s.get(i).get(9)+"||||"                		
                		  + ""+ft1s.get(i).get(10)+"||"+ft1s.get(i).get(11)+"|"+ft1s.get(i).get(20)+"^^^^^^^^^^^^NPI|" +ft1s.get(i).get(12)+"||"+ft1s.get(i).get(13)+"|"+ft1s.get(i).get(14)+"^"+ft1s.get(i).get(15) + "^"+ft1s.get(i).get(16) + "^"+ft1s.get(i).get(17) + "^"+ft1s.get(i).get(18)+ "||"+ft1s.get(i).get(19)+"|||\r\n";
                
                String zxt = "ZXT|1|"+zxts.get(i).get(0)+"|"+zxts.get(i).get(1)+"||||"+zxts.get(i).get(2)+"^"+ "answer for " + zxts.get(i).get(2) + "|"+zxts.get(i).get(3)+"|"+zxts.get(i).get(4)+"|"+zxts.get(i).get(5)+"\r\n";
                
                String zxa = "ZXA|1|"+zxas.get(i).get(0)+"^"+zxas.get(i).get(4)+"|"+zxas.get(i).get(1)+"||"+zxas.get(i).get(2)+"||"+zxas.get(i).get(3)+"|"+zxas.get(i).get(4)+"||"+zxas.get(i).get(5)+"|"+zxas.get(i).get(6)+"|"+zxas.get(i).get(7)+"|"+zxas.get(i).get(8)+"|"+zxas.get(i).get(9)+"|"+zxas.get(i).get(10)+"|"+zxas.get(i).get(11)+"|"+zxas.get(i).get(12)+"|"+zxas.get(i).get(13)+""
                        + "|"+zxas.get(i).get(14)+"|"+zxas.get(i).get(15)+"\r\n";
                
                if (includeFT1ZXT){
                	contents += msh + pid + pv1 + dg1 + in1 + gt1 + ft1 + zxt + zxa;
                }
                else{
                	contents += msh + pid + pv1 + dg1 + in1 + gt1;
                }
                

            }
        }
        
        //String zxt = "ZXT|1|"+zxts.get(0)+"|"+zxts.get(1)+"||||"+zxts.get(2)+"|"+zxts.get(3)+"|"+zxts.get(4)+"|"+zxts.get(5)+"\r\n";

        //String zxa = "ZXA|1|"+zxas.get(0)+"^"+zxas.get(4)+"|"+zxas.get(1)+"||"+zxas.get(2)+"||"+zxas.get(3)+"|"+zxas.get(4)+"||"+zxas.get(5)+"|"+zxas.get(6)+"|"+zxas.get(7)+"|"+zxas.get(8)+"|"+zxas.get(9)+"|"+zxas.get(10)+"|"+zxas.get(11)+"|"+zxas.get(12)+"|"+zxas.get(13)+""
                //+ "|"+zxas.get(14)+"|"+zxas.get(15)+"\r\n";
        
        String blg = "BLG|"+finalReportDt+"\r\n";
        
        //contents+= zxt + zxa + blg ;
        //contents+= zxa + blg ;
        contents+= blg ;
        
        String numOfMSHSegments = String.valueOf(pids.size());
        String footer = "BTS|" + numOfMSHSegments + "\r\n" + 
                        "FTS|1"; 
        
        fileContents = header += contents += footer;
        
        return fileContents;
    }
	
    public String convertDateFormat(String fromFormat, String toFormat, String date) throws Exception{
        DateFormat df1 = new SimpleDateFormat(fromFormat);
        Date parsedDt = df1.parse(date);       
        DateFormat df2 = new SimpleDateFormat(toFormat);       
        
        return df2.format(parsedDt);
	}	
    
    
    
    
    
    
    
    
    
    
    
    
	//Can non-Self-Explode test profile
    public List<String> getListFT1WithProfileTestsGenericV231(String testComponent, String testProfile, String transTyp, String unit, String abnReceivedFlag, 
    		String isRoundTrip, String testFacId, String dos, boolean isProfile) throws Exception{ 
        TimeStamp timeStamp = new TimeStamp();
        RandomCharacter randomCharacter = new RandomCharacter();
        //
        
        
        List<String> list = new ArrayList<String>();
        //String testAbbrev = "";
        //String profileId = "";
        //String effDt = geNewDate(1, "MM-dd-yyyy");   
        //String effDt = geNewDate(1, "MM/dd/yyyy"); 
        String lisTraceId = randomCharacter.getNonZeroRandomNumericString(5);
        //String dos = timeStamp.getCurrentDate("YYYYMMdd");
        String finalReportDt = timeStamp.getCurrentDate("YYYYMMdd");
       /*
        if (isProfTest == true) {
            List<String> testProfileList = platformDao.getSelfExplodeTestProf(null);
            testAbbrev = testProfileList.get(1);
        } else {            
        	if (testAbbrev.equals("")){
        		List<String> testList =  xifinRpmDao.getSingleTestByTestFacIdTestEffDt(testFacId, effDt, null);
        		testAbbrev = testList.get(1);
        	}
            else{
            	testAbbrev = specifiedTestAbbrev;
            }
        }
        */
        String altTestName = randomCharacter.getRandomAlphaString(5);        
       
        String manualPrice = "";//randomCharacter.getNonZeroRandomNumericString(2);
        String performingFacId = accnWsDao.getFacAbbrevFromFACByFacId("1",  null);//xifinRpmDao.getFacInfoFromFAC(null).get(0);
        String testNote = randomCharacter.getRandomAlphaString(5);;
        //String testLevelDiagCd = accnWsDao.getDiagnosisCode(null);   
        String testLevelDiagCd = "";
        String receiptDt = timeStamp.getCurrentDate("YYYYMMdd");               
        String mileage = randomCharacter.getNonZeroRandomNumericString(2);
        String numOfStops = randomCharacter.getNonZeroRandomNumericString(1);
        String numOfPt = numOfStops;           
        String phleName = "POOH BEAR";         
        String mod = xifinRpmDao.getRandomModId(null);
        //String renderingPhysNPI = accnWsDao.getPhysicianNPI(null);
        String renderingPhysNPI = "";
        
        if (!(isProfile == true)){        
        	testLevelDiagCd = accnWsDao.getDiagnosisCode(null);
        	renderingPhysNPI = accnWsDao.getPhysicianNPI(null);    
        	manualPrice = randomCharacter.getNonZeroRandomNumericString(2);
        }
        
        list.add(lisTraceId); // 0
        list.add(dos); // 1
        list.add(finalReportDt); // 2
        list.add(transTyp); // 3
        list.add(testComponent); // 4
        list.add(altTestName); // 5
        list.add(unit); // 6
        list.add(abnReceivedFlag); // 7
        list.add(manualPrice); // 8
        list.add(performingFacId); // 9
        list.add(testNote); // 10
        list.add(testLevelDiagCd); // 11
        list.add(testProfile); // 12
        list.add(receiptDt); // 13       
        list.add(mileage); // 14
        list.add(numOfStops); // 15
        list.add(numOfPt); // 16
        list.add(isRoundTrip); // 17
        list.add(phleName); // 18
        list.add(mod); // 19
        list.add(renderingPhysNPI); // 20
        
        return list;
    }   
    
	
    //Create HL7 file with profile tests for GenericV231 parser 
    public String createHL7FileGenericV231MultiAccnProfTest(String time,List<List<String>> pids,List<List<String>> pv1s,List<String> dg1s, List<List<String>> in1s, List<List<String>> gt1s,  
    		List<List<List<String>>> ft1s, List<List<String>> zxts,List<String> ptTypeDescs,List<List<String>> zxas, String finalReportDt, 
    														boolean includeFT1ZXT) throws Exception {
    	
        String fileContents = "",contents = "", ptId = ""; 
        //List<String> ft1List = new ArrayList<String>(); 
        String seqId = "";

        String header = "FHS|^~\\&|||||" + time + "||||||\r\n"
                      + "BHS|^~\\&|||||" + time + "||||||\r\n";

        if(pids.size() == pv1s.size() && pv1s.size() == in1s.size() && in1s.size() == gt1s.size() && gt1s.size()== ft1s.size()) {
            for(int i = 0; i<pids.size();i++) {
                String msh = "MSH|^~\\&|BIOTECH XRAY||||201307110711||DFT^P03||P|2.4\r\n";
                ptId = "";
                List<String> ft1List = new ArrayList<String>();                
                
                for(String ptTypeDesc:ptTypeDescs) {
                    switch (ptTypeDesc) {
                    case "ENTERPRISE":
                    	ptId += pids.get(i).get(0)+"&ENTERPRISE~";
                        break;
                    case "CLIENT":
                    	ptId += pids.get(i).get(0)+"&CLIENT~";
                        break;
                    case "FACILITY":
                    	ptId += pids.get(i).get(0)+"&FACILITY~";
                        break;
                    case "DEFAULT":
                    	ptId += pids.get(i);
                    default:
                        break;
                    }
                    
                }                
 
                String pid = "PID|1|"+ptId+"|"+pids.get(i).get(1)+"||"+pids.get(i).get(2)+ "^" + pids.get(i).get(3) + "||"+pids.get(i).get(4)+"|"+pids.get(i).get(5)+"|||"+pids.get(i).get(6)+"^"+pids.get(i).get(7)+"^"+pids.get(i).get(8)+"^"+pids.get(i).get(9)+"^"+pids.get(i).get(10)+"^"+pids.get(i).get(11)+""
                        + "||"+pids.get(i).get(12)+"|"+pids.get(i).get(13)+"||"+pids.get(i).get(14)+"||"+pids.get(i).get(15)+"|"+pids.get(i).get(16)+"\r\n";

                String pv1 = "PV1|1||"+pv1s.get(i).get(0)+"|"+pv1s.get(i).get(1)+"|||"+pv1s.get(i).get(2)+"^^^^^^^^^^^^NPI|"+pv1s.get(i).get(3)+"|"+pv1s.get(i).get(4)+"^^^^^^^^^^^^NPI||"+pv1s.get(i).get(5)+"|||"+pv1s.get(i).get(6)+"||||"+pv1s.get(i).get(7)+"|||||||||||||||||"
                        + "|"+pv1s.get(i).get(8)+"|||"+pv1s.get(i).get(9)+"|||||"+pv1s.get(i).get(10)+"|"+pv1s.get(i).get(11)+"||\r\n";
                
                String dg1 = "DG1|1||" + dg1s.get(0) + "\r\n";

                String in1 = "IN1|1|"+in1s.get(i).get(0)+"||"+in1s.get(i).get(1)+"|"+in1s.get(i).get(2)+"^"+in1s.get(i).get(3)+"^"+in1s.get(i).get(4)+"^"+in1s.get(i).get(5)+"^"+in1s.get(i).get(6)+"^"+in1s.get(i).get(7)+"|||"+in1s.get(i).get(8)+"|||"
                        + "|"+in1s.get(i).get(9)+"||||"+in1s.get(i).get(10)+"^" + in1s.get(i).get(10) + "|"+in1s.get(i).get(11)+"|"+in1s.get(i).get(12)+"|"+in1s.get(i).get(13)+"^"+in1s.get(i).get(14)+"^"+in1s.get(i).get(15)+"^"+in1s.get(i).get(16)+"^"+in1s.get(i).get(17)+"^"+in1s.get(i).get(18)+"||"
                                + "|"+in1s.get(i).get(19)+"|||"+in1s.get(i).get(20)+"|"+in1s.get(i).get(21)+"|||||||||||||||||"+in1s.get(i).get(22)+"||||||"+in1s.get(i).get(23)+"\r\n";

                String gt1 = "GT1|1||"+gt1s.get(i).get(0) +"^" +gt1s.get(i).get(0) +"||"+gt1s.get(i).get(1)+"^"+gt1s.get(i).get(2)+"^"+gt1s.get(i).get(3)+"^"+gt1s.get(i).get(4)+"^"+gt1s.get(i).get(5)+"^"+gt1s.get(i).get(6)+"|"+gt1s.get(i).get(7)+"||"+gt1s.get(i).get(8)+"|"+gt1s.get(i).get(9)+"||"
                        + ""+gt1s.get(i).get(10)+"|"+gt1s.get(i).get(11)+"||||"+gt1s.get(i).get(12)+"|"+gt1s.get(i).get(13)+"^"+gt1s.get(i).get(14)+"^"+gt1s.get(i).get(15)+"^"+gt1s.get(i).get(16)+"^"+gt1s.get(i).get(17)+"^"+gt1s.get(i).get(18)+"|"+gt1s.get(i).get(19)+"\r\n";
                
                
                for (int j = 0; j < ft1s.get(i).size(); j++) {
                	seqId = String.valueOf(j+1);
                	String ft1 = "FT1|"+ seqId +"|||"+ft1s.get(i).get(j).get(1)+"|"+ft1s.get(i).get(j).get(2)+"|"+ft1s.get(i).get(j).get(3)+"|"+ft1s.get(i).get(j).get(4)+"||"+ft1s.get(i).get(j).get(5)+"|"+ft1s.get(i).get(j).get(6)+"|"+ft1s.get(i).get(j).get(7)+"|"+ft1s.get(i).get(j).get(8)+"|" +ft1s.get(i).get(j).get(9)+"||||"                		
                		  + ""+ft1s.get(i).get(j).get(10)+"||"+ft1s.get(i).get(j).get(11)+"|"+ft1s.get(i).get(j).get(20)+"^^^^^^^^^^^^NPI|" +ft1s.get(i).get(j).get(12)+"||"+ft1s.get(i).get(j).get(13)+"|"+ft1s.get(i).get(j).get(14)+"^"+ft1s.get(i).get(j).get(15) + "^"+ft1s.get(i).get(j).get(16) + "^"+ft1s.get(i).get(j).get(17) + "^"+ft1s.get(i).get(j).get(18)+ "||"+ft1s.get(i).get(j).get(19)+"|||\r\n";
                	ft1List.add(ft1);
                }
                
                //String zxt = "ZXT|1|"+zxts.get(i).get(0)+"|"+zxts.get(i).get(1)+"||||"+zxts.get(i).get(2)+"^"+ "answer for " + zxts.get(i).get(2) + "|"+zxts.get(i).get(3)+"|"+zxts.get(i).get(4)+"|"+zxts.get(i).get(5)+"\r\n";
                
                String zxa = "ZXA|1|"+zxas.get(i).get(0)+"^"+zxas.get(i).get(4)+"|"+zxas.get(i).get(1)+"||"+zxas.get(i).get(2)+"||"+zxas.get(i).get(3)+"|"+zxas.get(i).get(4)+"||"+zxas.get(i).get(5)+"|"+zxas.get(i).get(6)+"|"+zxas.get(i).get(7)+"|"+zxas.get(i).get(8)+"|"+zxas.get(i).get(9)+"|"+zxas.get(i).get(10)+"|"+zxas.get(i).get(11)+"|"+zxas.get(i).get(12)+"|"+zxas.get(i).get(13)+""
                        	  + "|"+zxas.get(i).get(14)+"|"+zxas.get(i).get(15)+"\r\n";
                
                if (includeFT1ZXT){
                	//contents += msh + pid + pv1 + dg1 + in1 + gt1 + ft1 + zxt + zxa;
                	contents += msh + pid + pv1 + dg1 + in1 + gt1;
                	for (int k = 0; k < ft1List.size(); k++){
                		contents += ft1List.get(k);
                	}
                	//contents += zxt + zxa;
                	contents += zxa;
                }
                else{
                	contents += msh + pid + pv1 + dg1 + in1 + gt1;
                }
            }
        }
        
        String blg = "BLG|"+finalReportDt+"\r\n";        

        contents+= blg ;
        
        String numOfMSHSegments = String.valueOf(pids.size());
        String footer = "BTS|" + numOfMSHSegments + "\r\n" + 
                        "FTS|1"; 
        
        fileContents = header += contents += footer;
        
        return fileContents;
    }
    
    
	public String createHL7FileNslijMultiAccn(String time, List<List<String>> pids,List<List<String>> pv1s,List<String> dg1s, List<List<String>> in1s, List<List<List<String>>> ft1s, 
			boolean includeFT1) throws Exception {
        String fileContents = "";

        String header = "FHS|^~\\&|||" + time + "\r\n"
                + "BHS|^~\\&\r\n";

        String contents = ""; //, ptId = "";
        if(pids.size() == pv1s.size() && pv1s.size() == in1s.size() && in1s.size() == ft1s.size()) {
            for(int i = 0; i<pids.size();i++) {
                String msh = "MSH|^~\\&|||||20150803161600|Q1962813501T3521379343|ORM^O01|P:30608504-E:106032384-O:1656918961-Q:1962813501-MSG:ORM\r\n";
                //ptId = "";
                List<String> ft1List = new ArrayList<String>();
                /*
                for(String ptTypeDesc:ptTypeDescs) {
                    switch (ptTypeDesc) {
                    case "ENTERPRISE":
                    	ptId += pids.get(i).get(0)+"&ENTERPRISE~";
                        break;
                    case "CLIENT":
                    	ptId += pids.get(i).get(0)+"&CLIENT~";
                        break;
                    case "FACILITY":
                    	ptId += pids.get(i).get(0)+"&FACILITY~";
                        break;
                    default:
                        break;
                    }
                }*/
                
                String pid = "PID|1|" + pids.get(i).get(0) +"|" + pids.get(i).get(1) + "||" + pids.get(i).get(2)+ "^"+ pids.get(i).get(3)+"^||"
                        + pids.get(i).get(4) + "|" + pids.get(i).get(5) + "|||" + pids.get(i).get(6) + "^" + pids.get(i).get(7) + "^"+ pids.get(i).get(8)+ "^" + pids.get(i).get(9)+ "^" + pids.get(i).get(10) +"^" + pids.get(i).get(11) + "||"
                        + pids.get(i).get(12) + "|" + pids.get(i).get(12) + "||||" + pids.get(i).get(13) + "|"
                        + pids.get(i).get(14) + "\r\n";
                               
                String pv1 = "PV1|1|"+pv1s.get(i).get(0)+"|"+pv1s.get(i).get(1)+"||||" + pv1s.get(i).get(2)+"^^^^^^^^^^^^UPIN~"+pv1s.get(i).get(3) + "^^^^^^^^^^0^NPI^NPI|"
                //+ pv1s.get(i).get(4) + "|"+pv1s.get(i).get(5)+"|||||||||||||||||||||||||||"+pv1s.get(i).get(6)+"|||"+pv1s.get(i).get(7)+"|||||"+pv1s.get(i).get(8)+"|"+pv1s.get(i).get(9)+"||\r\n";
                		+ pv1s.get(i).get(4) + "\r\n";                     

                String dg1 = "DG1|1||" + dg1s.get(0) + "\r\n";
                
                String in1 = "IN1|1||" + in1s.get(i).get(0) + "|||||||||" + in1s.get(i).get(1) + "||||"
                        + in1s.get(i).get(2) + "^"+ in1s.get(i).get(3)+"^|"+ in1s.get(i).get(4)+"||" + in1s.get(i).get(5) + "^" + in1s.get(i).get(6) + "^"+ in1s.get(i).get(7)+ "^" + in1s.get(i).get(8)+ "^" + in1s.get(i).get(9) +"^" + in1s.get(i).get(10) + "||||||||||||||||||||||||||||||"+in1s.get(i).get(11)+"|\r\n";
                
                for (int j = 0; j < ft1s.get(i).size(); j++) {
                	String ft1 = "FT1|1|||" + ft1s.get(i).get(j).get(0) + "|" + ft1s.get(i).get(j).get(1) + "|"
                				+ ft1s.get(i).get(j).get(2)+"||" + ft1s.get(i).get(j).get(4) + "|" + ft1s.get(i).get(j).get(3)+ "||||" 
                				+ ft1s.get(i).get(j).get(5) + "|||" + ft1s.get(i).get(j).get(5) + "\r\n";
                	ft1List.add(ft1);
                }
                
                if (includeFT1){
                	contents += msh + pid + pv1 + dg1 + in1;
                	for (int k = 0; k < ft1List.size(); k++){
                		contents += ft1List.get(k);
                	}
                }
                else{
                	contents += msh + pid + pv1 + dg1 + in1;
                }
            }
        }
        
        String numOfMSHSegments = String.valueOf(pids.size());
        String footer = "BTS|" + numOfMSHSegments + "\r\n" + 
                		"FTS|1"; 
        
        fileContents = header += contents += footer;
        
        return fileContents;
    }
	
    //Create HL7 file for ArxHl7V231 parser
    public String createHL7FileArxHl7V231MultiAccn(String time,List<List<String>> pids,List<List<String>> pv1s,List<String> dg1s, List<List<String>> in1s, List<List<String>> gt1s, List<List<String>> ft1s, 
    		List<List<String>> zxts,List<String> ptTypeDescs,List<List<String>> zxas, String finalReportDt, boolean includeFT1ZXT) throws Exception {
    	
        String fileContents = "",contents = "", ptId = "";

        String header = "FHS|^~\\&|||||" + time + "||||||\r\n"
                      + "BHS|^~\\&|||||" + time + "||||||\r\n";

        if(pids.size() == pv1s.size() && pv1s.size() == in1s.size() && in1s.size() == gt1s.size() && gt1s.size()== ft1s.size()) {
            for(int i = 0; i<pids.size();i++) {
                String msh = "MSH|^~\\&|BIOTECH XRAY||||201307110711||DFT^P03||P|2.4\r\n";
                ptId = "";
                for(String ptTypeDesc:ptTypeDescs) {
                    switch (ptTypeDesc) {
                    case "ENTERPRISE":
                    	ptId += pids.get(i).get(0)+"&ENTERPRISE~";
                        break;
                    case "CLIENT":
                    	ptId += pids.get(i).get(0)+"&CLIENT~";
                        break;
                    case "FACILITY":
                    	ptId += pids.get(i).get(0)+"&FACILITY~";
                        break;
                    case "DEFAULT":
                    	ptId += pids.get(i);
                    default:
                        break;
                    }
                    
                }                
 
                String pid = "PID|1|"+ptId+"|"+pids.get(i).get(1)+"||"+pids.get(i).get(2)+ "^" + pids.get(i).get(3) + "||"+pids.get(i).get(4)+"|"+pids.get(i).get(5)+"|||"+pids.get(i).get(6)+"^"+pids.get(i).get(7)+"^"+pids.get(i).get(8)+"^"+pids.get(i).get(9)+"^"+pids.get(i).get(10)+"^"+pids.get(i).get(11)+""
                        + "||"+pids.get(i).get(12)+"|"+pids.get(i).get(13)+"||"+pids.get(i).get(14)+"||"+pids.get(i).get(15)+"|"+pids.get(i).get(16)+"\r\n";

                String pv1 = "PV1|1||"+pv1s.get(i).get(0)+"|"+pv1s.get(i).get(1)+"|||"+pv1s.get(i).get(2)+"^^^^^^^^^^^^NPI|"+pv1s.get(i).get(3)+"|"+pv1s.get(i).get(4)+"^^^^^^^^^^^^NPI||"+pv1s.get(i).get(5)+"|||"+pv1s.get(i).get(6)+"||||"+pv1s.get(i).get(7)+"|||||||||||||||||"
                        + "|"+pv1s.get(i).get(8)+"|||"+pv1s.get(i).get(12)+"|||||"+pv1s.get(i).get(10)+"|"+pv1s.get(i).get(11)+"||\r\n";
                
                String dg1 = "DG1|1||" + dg1s.get(0) + "\r\n";

                String in1 = "IN1|1|"+in1s.get(i).get(0)+"||"+in1s.get(i).get(1)+"|"+in1s.get(i).get(2)+"^"+in1s.get(i).get(3)+"^"+in1s.get(i).get(4)+"^"+in1s.get(i).get(5)+"^"+in1s.get(i).get(6)+"^"+in1s.get(i).get(7)+"|||"+in1s.get(i).get(8)+"|||"
                        + "|"+in1s.get(i).get(9)+"||||"+in1s.get(i).get(10)+"^" + in1s.get(i).get(10) + "|"+in1s.get(i).get(11)+"|"+in1s.get(i).get(12)+"|"+in1s.get(i).get(13)+"^"+in1s.get(i).get(14)+"^"+in1s.get(i).get(15)+"^"+in1s.get(i).get(16)+"^"+in1s.get(i).get(17)+"^"+in1s.get(i).get(18)+"||"
                                + "|"+in1s.get(i).get(19)+"|||"+in1s.get(i).get(20)+"|"+in1s.get(i).get(21)+"|||||||||||||||||"+in1s.get(i).get(22)+"||||||"+in1s.get(i).get(23)+"\r\n";

                String gt1 = "GT1|1||"+gt1s.get(i).get(0) +"^" +gt1s.get(i).get(0) +"|" +gt1s.get(i).get(20) +"^" +gt1s.get(i).get(20) +"|"+gt1s.get(i).get(1)+"^"+gt1s.get(i).get(2)+"^"+gt1s.get(i).get(3)+"^"+gt1s.get(i).get(4)+"^"+gt1s.get(i).get(5)+"^"+gt1s.get(i).get(6)+"|"+gt1s.get(i).get(7)+"|"+gt1s.get(i).get(21)+"|"+gt1s.get(i).get(8)+"|"+gt1s.get(i).get(9)+"||"
                        + ""+gt1s.get(i).get(10)+"|"+gt1s.get(i).get(11)+"||||"+gt1s.get(i).get(12)+"|"+gt1s.get(i).get(13)+"^"+gt1s.get(i).get(14)+"^"+gt1s.get(i).get(15)+"^"+gt1s.get(i).get(16)+"^"+gt1s.get(i).get(17)+"^"+gt1s.get(i).get(18)+"|"+gt1s.get(i).get(19)+"\r\n";
                
                String ft1 = "FT1|"+ft1s.get(i).get(0)+"|||"+ft1s.get(i).get(1)+"|"+ft1s.get(i).get(2)+"|"+ft1s.get(i).get(3)+"|"+ft1s.get(i).get(4)+"||"+ft1s.get(i).get(5)+"|"+ft1s.get(i).get(6)+"|"+ft1s.get(i).get(7)+"|"+ft1s.get(i).get(8)+"|" +ft1s.get(i).get(9)+"||||"                		
                		  + ""+ft1s.get(i).get(10)+"||"+ft1s.get(i).get(11)+"|"+ft1s.get(i).get(20)+"^^^^^^^^^^^^NPI|" +ft1s.get(i).get(12)+"||"+ft1s.get(i).get(13)+"|"+ft1s.get(i).get(14)+"^"+ft1s.get(i).get(15) + "^"+ft1s.get(i).get(16) + "^"+ft1s.get(i).get(17) + "^"+ft1s.get(i).get(18)+ "||"+ft1s.get(i).get(19)+"|||\r\n";
                
                String zxt = "ZXT|1|"+zxts.get(i).get(0)+"|"+zxts.get(i).get(1)+"||||"+zxts.get(i).get(2)+"^"+ "answer for " + zxts.get(i).get(2) + "|"+zxts.get(i).get(3)+"|"+zxts.get(i).get(4)+"|"+zxts.get(i).get(5)+"\r\n";
                
                String zxa = "ZXA|1|"+zxas.get(i).get(0)+"^"+zxas.get(i).get(4)+"|"+zxas.get(i).get(1)+"||"+zxas.get(i).get(2)+"||"+zxas.get(i).get(3)+"|"+zxas.get(i).get(4)+"||"+zxas.get(i).get(5)+"|"+zxas.get(i).get(6)+"|"+zxas.get(i).get(7)+"|"+zxas.get(i).get(8)+"|"+zxas.get(i).get(9)+"|"+zxas.get(i).get(10)+"|"+zxas.get(i).get(11)+"|"+zxas.get(i).get(12)+"|"+zxas.get(i).get(13)+""
                        + "|"+zxas.get(i).get(14)+"|"+zxas.get(i).get(15)+"\r\n";
                
                if (includeFT1ZXT){
                	contents += msh + pid + pv1 + dg1 + in1 + gt1 + ft1 + zxt + zxa;
                }
                else{
                	contents += msh + pid + pv1 + dg1 + in1 + gt1;
                }
                

            }
        }
        
        //String zxt = "ZXT|1|"+zxts.get(0)+"|"+zxts.get(1)+"||||"+zxts.get(2)+"|"+zxts.get(3)+"|"+zxts.get(4)+"|"+zxts.get(5)+"\r\n";

        //String zxa = "ZXA|1|"+zxas.get(0)+"^"+zxas.get(4)+"|"+zxas.get(1)+"||"+zxas.get(2)+"||"+zxas.get(3)+"|"+zxas.get(4)+"||"+zxas.get(5)+"|"+zxas.get(6)+"|"+zxas.get(7)+"|"+zxas.get(8)+"|"+zxas.get(9)+"|"+zxas.get(10)+"|"+zxas.get(11)+"|"+zxas.get(12)+"|"+zxas.get(13)+""
                //+ "|"+zxas.get(14)+"|"+zxas.get(15)+"\r\n";
        
        String blg = "BLG|"+finalReportDt+"\r\n";
        
        //contents+= zxt + zxa + blg ;
        //contents+= zxa + blg ;
        contents+= blg ;
        
        String numOfMSHSegments = String.valueOf(pids.size());
        String footer = "BTS|" + numOfMSHSegments + "\r\n" + 
                        "FTS|1"; 
        
        fileContents = header += contents += footer;
        
        return fileContents;
    }
    
    
    public List<String> getListORCAllinaORM(String identifier) throws Exception{
    	List<String> list = new ArrayList<String>();
    	        
        RandomCharacter randomCharacter = new RandomCharacter();
        TimeStamp timeStamp = new TimeStamp();
        
        String testCode = randomCharacter.getNonZeroRandomNumericString(5);
        String quantity = "O"; 
        String interval = "ONE TIME"; 
        String startDt = timeStamp.getPreviousDate("YYYYMMdd", 5);
        String endDt = timeStamp.getCurrentDate("YYYYMMdd");
        String priority = "S"; 
        String condition = "Standing"; 
        String DtOfTransaction =  timeStamp.getCurrentDate("YYYYMMdd");  
        String xcnID = randomCharacter.getRandomAlphaNumericString(6); 
        String xcnFamilyName = randomCharacter.getRandomAlphaString(10); 
        String xcnGivenName = randomCharacter.getRandomAlphaString(10);
        String ordProviderST = randomCharacter.getNonZeroRandomNumericString(5); 
        String ordProviderLastN = randomCharacter.getRandomAlphaString(10); 
        String ordProviderFirstN = randomCharacter.getRandomAlphaString(10); 
        String ordProviderMidN = randomCharacter.getRandomAlphaString(5); 
        String callBackNumber = randomCharacter.getNonZeroRandomNumericString(9); 
        String enterDeviceId = randomCharacter.getRandomAlphaNumericString(5); 
        String enterDeviceTxt = randomCharacter.getRandomAlphaNumericString(10); 
        String orcField23 = randomCharacter.getNonZeroRandomNumericString(10); 
           
        String clnAbbrev = xifinRpmDao.getClientId(null);
 
        list.add(identifier);// 0 accn id = X + identifier
        list.add(testCode);//1 Test Code
        list.add(quantity);//2
        list.add(interval);//3
        list.add(startDt);//4
        list.add(endDt);//5
        list.add(priority);//6
        list.add(condition);//7
        list.add(DtOfTransaction);//8
        list.add(xcnID);//9
        list.add(xcnFamilyName);//10
        list.add(xcnGivenName);//11
        list.add(ordProviderST);//12    
        list.add(ordProviderLastN);//13
        list.add(ordProviderFirstN);//14
        list.add(ordProviderMidN);//15
        //list.add(testAbbrev); //0
        list.add(clnAbbrev); //16 Client Abbrev
        list.add(callBackNumber);//17
        list.add(enterDeviceId);//18
        list.add(enterDeviceTxt);//19
        list.add(orcField23);//20
        
    	return list;
    }
    
    public List<String> getListEVNsAllinaADT() throws Exception{
    	RandomCharacter randomCharacter = new RandomCharacter();
        List<String> list = new ArrayList<String>();
        
        String evnField4 = randomCharacter.getRandomAlphaNumericString(10); 
        String referUPIN1 = randomCharacter.getRandomAlphaString(7); 
        String referUPIN2 = randomCharacter.getRandomAlphaString(10);
        String referUPIN3 = randomCharacter.getRandomAlphaString(8);
        
        list.add(evnField4); //0
        list.add(referUPIN1); //1
        list.add(referUPIN2);//2
        list.add(referUPIN3); //3
        
        return list;
    }
    
    
    public List<String> getListOBRAllinaORM() throws Exception{
    	List<String> list = new ArrayList<String>();
    	       
        RandomCharacter randomCharacter = new RandomCharacter();
        
        TimeStamp timeStamp = new TimeStamp();
        
        String fillerOrderNumber = randomCharacter.getRandomAlphaNumericString(4);         
    	String effDt = geNewDate(1, "MM/dd/yyyy");
    	List<String> testList =  xifinRpmDao.getSingleTestByTestFacIdTestEffDt("1", effDt, null);
    	String testAbbrev = testList.get(1);
        String universalSrvTxt = randomCharacter.getRandomAlphaNumericString(15); 
        String universalSrvCdSystem = randomCharacter.getRandomAlphaNumericString(5); 
        String universalSrvAltTxt = randomCharacter.getRandomAlphaNumericString(15); 
        
        String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
         
        String collectorID = randomCharacter.getRandomAlphaNumericString(10); 
        String collectorLastName = randomCharacter.getRandomAlphaNumericString(10); 
        String collectorFirstName = randomCharacter.getRandomAlphaNumericString(10); 
        String collectorMidName = randomCharacter.getRandomAlphaNumericString(10); 
        String specimenActionCode = "NP";          
        String specimenSource = randomCharacter.getRandomAlphaNumericString(15);
        
        String ordringPhysNPI = accnWsDao.getPhysicianNPI(null);
        ArrayList<String> physInfoList = xifinRpmDao.getPhysInfoFromPHYSByNPI(ordringPhysNPI, null);
        String orderingPhysUPIN = physInfoList.get(13);
        String ordringPhysrLNm = physInfoList.get(1); 
        String ordringPhysrFNm = physInfoList.get(2); 
        String ordringPhysrMNm = ""; 
        String ordProviderCode = randomCharacter.getNonZeroRandomNumericString(5); 
        String ordCallBackNumber = randomCharacter.getNonZeroRandomNumericString(9);
        String testNote = "Test Note" + randomCharacter.getNonZeroRandomNumericString(10);  
        String departmentCode = "Lab"; 
        String testResult = "B"; 
        String resultCopiesTo = randomCharacter.getRandomAlphaNumericString(5); 
        String numberOfSampleContainers  = randomCharacter.getNonZeroRandomNumericString(2); 
        String ordFacility1 = randomCharacter.getNonZeroRandomNumericString(4);
        String ordFacility2 = randomCharacter.getRandomAlphaString(15); 
        String ordFacility3 = randomCharacter.getRandomAlphaString(5); 
        String ordFacility4 = randomCharacter.getRandomAlphaString(15);
        
        list.add(fillerOrderNumber); //0        
        list.add(testAbbrev); //1 Test Code
        list.add(universalSrvTxt); //2
        list.add(universalSrvCdSystem); //3
        list.add(universalSrvAltTxt); //4
        list.add(currDtTime); //5
        list.add(currDtTime); //6 DOS
        list.add(collectorID); //7
        list.add(collectorLastName); //8
        list.add(collectorFirstName); //9
        list.add(collectorMidName);//10
        list.add(specimenActionCode); //11
        list.add(currDtTime); //12 DOS
        list.add(specimenSource); //13
        list.add(orderingPhysUPIN); //14 Ordering Phys UPIN
        list.add(ordringPhysrLNm); //15 Ordering Phys Last Name
        list.add(ordringPhysrFNm); //16 Ordering Phys First Name
        list.add(ordringPhysrMNm); //17 Ordering Phys Middle Name
        list.add(ordProviderCode); //18
        list.add(ordCallBackNumber); //19
        list.add(testNote); //20 Test Note
        list.add(departmentCode); //21 Department Code
        list.add(testResult); //22 Test Result
        list.add(resultCopiesTo); //23
        list.add(numberOfSampleContainers); //24
        list.add(ordFacility1); //25
        list.add(ordFacility2); //26
        list.add(ordFacility3); //27
        list.add(ordFacility4); //28
        
        return list;
    }
    
	//Can add multiple single tests or self-explode test profiles
    public List<String> getListFT1AllinaXTEND(boolean isProfTest, String transTyp, String testFacId, String specifiedTestAbbrev, String dos, String chbk, String mod) throws Exception{         
        RandomCharacter randomCharacter = new RandomCharacter();
        
        List<String> list = new ArrayList<String>();
        String testAbbrev = "";        
        String effDt = geNewDate(1, "MM/dd/yyyy");         
       
        if (isProfTest == true) {
            List<String> testProfileList = platformDao.getSelfExplodeTestProf(null);
            testAbbrev = testProfileList.get(1);            
        } else {            
        	if (specifiedTestAbbrev.equals("")){
        		List<String> testList =  xifinRpmDao.getSingleTestByTestFacIdTestEffDt(testFacId, effDt, null);
        		testAbbrev = testList.get(1);        		
        	}
            else{
            	testAbbrev = specifiedTestAbbrev;            	
            }
        } 
        String performingFacId = accnWsDao.getFacAbbrevFromFACByFacId("1",  null);
        String testLevelDiagCd = accnWsDao.getDiagnosisCode(null);
        String depId = randomCharacter.getNonZeroRandomNumericString(6);
        String clnAbbrev = xifinRpmDao.getClientId(null);
        //String mod = "GA"; //if it is GA sets Abn Rec to Y
        
        list.add(chbk);//0
        list.add(dos);//1
        list.add(transTyp);//2   
        list.add(testAbbrev);//3
        list.add(performingFacId);//4 
        list.add(depId);//5 
        list.add(clnAbbrev);//6 
        list.add(testLevelDiagCd);//7        
        list.add(mod);//8        
        
        return list;
    }
    
    public String createHL7FileAllinaORMMultiAccn(String time,List<List<String>> pid, List<List<String>> pv1, List<List<String>> orc, List<List<String>> obr, List<List<String>> dg1, String finalReportDt, String ptId) throws Exception {
    	String fileContents  = "";    	
    	
    	for (int i=0; i<pid.size(); i++){   	
	    	String msh = "MSH|^~\\&|BEAKER|ALLINA|XIFIN|ALLINA|" + time + "|A056776|ORM^O01|65031502|P|2.3\r\n";
	    	String pids = "PID|1|^^^^EPIC|" + ptId + "^^^^EPI||" + pid.get(i).get(0) + "^" + pid.get(i).get(1)+"^^" +pid.get(i).get(2)+"||" 
	        		+ pid.get(i).get(3) + "|" + pid.get(i).get(4)+"|||" + pid.get(i).get(5) + "^" + pid.get(i).get(6) + "^" + pid.get(i).get(7) + "^" + pid.get(i).get(8) + "^" 
	        	    + pid.get(i).get(9) +"^" + pid.get(i).get(10) + "||" + pid.get(i).get(11) + "||||||"+pid.get(i).get(12)+"\r\n";
	        String pv1s = "PV1||" + pv1.get(i).get(0) + "|" + pv1.get(i).get(1) + "^" + pv1.get(i).get(2) + "^" + pv1.get(i).get(3) + "^EPP"+ "^^^EPW||||"
	        		+ pv1.get(i).get(4) + "^" + pv1.get(i).get(5) + "^" + pv1.get(i).get(6) + "^" + pv1.get(i).get(7) + "||" + pv1.get(i).get(8) + "^" + pv1.get(i).get(9) + "^" + pv1.get(i).get(10) + "^L"
	        		+ "|||||||||" + pv1.get(i).get(11) + "|" + pv1.get(i).get(12) + "|||||||||||||||||||||||||" + pv1.get(i).get(13) +"\r\n"; 
	    	
	    	String orcs= "ORC|SC||-" + orc.get(i).get(0) + "-^" + orc.get(i).get(1) + "||||" + orc.get(i).get(2) + "^" + orc.get(i).get(3) + "^^" + orc.get(i).get(4) + "^" + orc.get(i).get(5) + "^" + orc.get(i).get(6) + "^^" + orc.get(i).get(7) + "^^^1||"
	        		+ orc.get(i).get(8) + "|"+ orc.get(i).get(9) + "^" + orc.get(i).get(10) + "^" + orc.get(i).get(11) + "||"+ orc.get(i).get(12) + "^" + orc.get(i).get(13)+"^" + orc.get(i).get(14) + "^" + orc.get(i).get(15)+ "|"
	        		+ orc.get(i).get(16) + "|" + orc.get(i).get(17) + "||||" + orc.get(i).get(18) +"^" + orc.get(i).get(19) + "||||||" + orc.get(i).get(20) +"\r\n";       
	    	String obrs = "OBR|1||" + obr.get(i).get(0) + "|" + obr.get(i).get(1) + "^" + obr.get(i).get(1) + "^" + obr.get(i).get(1) + "^^" + obr.get(i).get(1)+"||" + obr.get(i).get(5) + "|" + obr.get(i).get(6) + "|||" + obr.get(i).get(7) + "^" + obr.get(i).get(8)+ "^" + obr.get(i).get(9)+ "-" + obr.get(i).get(10) 
	    			+ "|" + obr.get(i).get(11) + "|||" + obr.get(i).get(12) + "|" + obr.get(i).get(13) + "|" + obr.get(i).get(14) + "^" + obr.get(i).get(15) +"^" + obr.get(i).get(16) +"^^^^^" + obr.get(i).get(17) + "|"
	    			+ obr.get(i).get(19) + "||" + obr.get(i).get(20) + "|||"+finalReportDt + "||"+ obr.get(i).get(21)+ "|" + obr.get(i).get(22) + "|||" + obr.get(i).get(23)  
	    			+ "|||||||||" + obr.get(i).get(24) + "||||||||" + obr.get(i).get(25)+"^" + obr.get(i).get(26) + "^" + obr.get(i).get(27) + "^^" + obr.get(i).get(28)+"\r\n";
	    	String dgs = "DG1|1|||" + dg1.get(i).get(0) + "\r\n";    
	    	
	    	fileContents  = msh + pids + pv1s + orcs + obrs + dgs;
    	}
    	
        return fileContents ;
    }
    
    
    public String createHL7FileAllinaADTMultiAccn(String time,List<List<String>> evn, List<List<String>> pid,List<List<String>> pv1, List<List<String>> gt1, List<List<String>> in1, List<List<String>> in2, List<List<String>> acc, String msh8Value) throws Exception {
    	String fileContents = "";
    	
    	for (int i=0; i<pid.size(); i++){ 
	    	String msh = "MSH|^-\\&|EPIC|ALLINA|||" + time + "|A110013|ADT^" + msh8Value + "|381513166|P|2.3\r\n";
	    	String evns = "EVN|A08|" + time + "||" + evn.get(i).get(0) + "|" + evn.get(i).get(1) + "^" + evn.get(i).get(2) + "^" + evn.get(i).get(3) + "^L^^^^^EPP^^^^^EPW\r\n";
	    	String pids = "PID|1||||" + pid.get(i).get(0) + "^" + pid.get(i).get(1) + "^" + pid.get(i).get(2) + "||" + pid.get(i).get(3) + "|" 
	    			+ pid.get(i).get(4) +"|||" + pid.get(i).get(5) + "^" + pid.get(i).get(6) + "^" + pid.get(i).get(7) + "^" + pid.get(i).get(8) + "^" + pid.get(i).get(9) + "^" + pid.get(i).get(10) + "||" + pid.get(i).get(11) + "||||||" + pid.get(i).get(12) + "\r\n";
	    	String pv1s = "PV1||"+ pv1.get(i).get(0)+ "||" + pv1.get(i).get(1) + "|||"+ pv1.get(i).get(2) + "^" + pv1.get(i).get(3) + "^" + pv1.get(i).get(4) + "^" + pv1.get(i).get(5) + "^^^^^^^^^PRST|||||||" + pv1.get(i).get(6) + "|||||" + pv1.get(i).get(7) + "|||||||||||||||||" + pv1.get(i).get(8) + "||||||||" + pv1.get(i).get(9) + "|" 
	    			+ pv1.get(i).get(10) + "\r\n";
	    	String gt1s = "GT1|||||" + gt1.get(i).get(1) + "^" + gt1.get(i).get(2) + "^" + gt1.get(i).get(3) + "^" + gt1.get(i).get(4) + "^" + gt1.get(i).get(5) + "^" + gt1.get(i).get(6) + "^^^BENTON|" + gt1.get(i).get(7) + "|||||" + gt1.get(i).get(8) + "|" + gt1.get(i).get(9) + "\r\n";
	    	String in1s = "IN1||" + in1.get(i).get(0) + "||" + in1.get(i).get(1) + "|" + in1.get(i).get(2) + "^" + in1.get(i).get(3) + "^" + in1.get(i).get(4) + "^" + in1.get(i).get(5) + "^" + in1.get(i).get(6) + "^" + in1.get(i).get(7) + "|||" 
	    			+ in1.get(i).get(8) + "||||||||" + in1.get(i).get(10) + "^" + in1.get(i).get(11) + "|" + in1.get(i).get(12) + "||" + in1.get(i).get(13) + "^" + in1.get(i).get(14) + "^" + in1.get(i).get(15) + "^" + in1.get(i).get(16) + "^" + in1.get(i).get(17) + "^" + in1.get(i).get(18) + "||" + in1.get(i).get(19) + "|" + in1.get(i).get(9) + "||||||||||||||" + in1.get(i).get(20) + "\r\n";
	    	String in2s = "IN2||||||||||||||||||||||||||" + in2.get(i).get(0) + "\r\n";
	    	String accs = "ACC||||" + acc.get(i).get(0) + "\r\n";
	    	
	    	fileContents = msh + evns + pids + pv1s + gt1s + in1s + in2s + accs;
    	}
    	
        return fileContents;
    }
   
    public String createHL7FileAllinaXTENDMultiAccn(String time,List<List<String>> pid,List<List<String>> pv1, List<List<String>> ft1, String msh8Value) throws Exception {
    	String fileContents = "";
    	
    	for (int i=0; i<pid.size(); i++){ 
	    	String msh="MSH|^~\\&|||||||FTS^"+msh8Value+"|"+time+"|P|2.3\r\n";	    	
	    	String pids="PID||-"+pid.get(i).get(0)+"-|"+pid.get(i).get(1)+"||"+pid.get(i).get(2)+"^"+pid.get(i).get(3)+"^"+pid.get(i).get(4)+"||"+pid.get(i).get(5)+"|"+pid.get(i).get(6)+"|||||||||||" + pid.get(i).get(7) + "\r\n";
	    	String pv1s="PV1|||||||"+pv1.get(i).get(0)+"^"+pv1.get(i).get(1)+"^"+pv1.get(i).get(2)+"^"+pv1.get(i).get(3)+"|"+pv1.get(i).get(4)+"|||||||||||"+pv1.get(i).get(5)+"\r\n";
	    	String ft1s="FT1|1||"+ft1.get(i).get(0)+"|"+ft1.get(i).get(1)+"||"+ft1.get(i).get(2)+"|"+ft1.get(i).get(3)+"||||||"+ft1.get(i).get(4)+"|"+ft1.get(i).get(5)+"||"+ft1.get(i).get(6)+"|||"+ft1.get(i).get(7)+"|||||||"+ft1.get(i).get(8)+"\r\n";
	    	
	    	fileContents=msh+pids+pv1s+ft1s;
    	}
    	
        return fileContents;
    }   
    
}
