package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.hospitalAdmitCheck.HospitalAdmitCheck;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.MiscMap;
import com.overall.utils.EngineUtils;
import com.overall.utils.ImportEngineUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ImportEngineTest extends SeleniumBaseTest
{
    private static final SimpleDateFormat HOSPITAL_ADMIT_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private RandomCharacter randomCharacter;
    private EngineUtils engineUtils;
    private ImportEngineUtils importEngineUtils;
    private TimeStamp timeStamp;
    private FileManipulation fileManipulation;

    private static final String IMPORT_DIAG_DXQC = "DXQC";
    private static final String IMPORT_DIAG_DXCODE = "DXCODE";
    private static final String IMPORT_DIAG_DXREVIEW = "DXREVIEW";
    private static final String IMPORT_DIAG_DXINSUFDOC = "DXINSUFDOC";
    private static final String ENGINE_CONFIDENCE_QC = "QC";
    private static final String ENGINE_CONFIDENCE_CODE = "Code";
    private static final String ENGINE_CONFIDENCE_REVIEW = "Review";
    private static final String ENGINE_CONFIDENCE_CONFIDENT = "Confident";
    private static final String ENGINE_CONFIDENCE_INSUFFICIENT_DOC = "Insufficient Doc";

    @Test(priority = 1, description = "Hospital Admit - Run import engine with valid document format and code type A")
    @Parameters({"formatType"})
    public void testPFER_95(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_95 ======");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        logger.info("*** Step 1 Action: Create document valid format with type A");
        String type = "A";
        String subsID = randomCharacter.getRandomNumericString(8);
        String lName = randomCharacter.getRandomAlphaString(5);
        String fName = randomCharacter.getRandomAlphaString(5);
        String mName = randomCharacter.getRandomAlphaString(1);
        String DOB = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String admitDate = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String dischargeDate = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String SSN = "123456789";//randomCharacter.getNonZeroRandomNumericString(5);
        String textInput = importEngineUtils.createDataForHospitalAdmitFile(type, subsID, lName, fName, mName, DOB, admitDate, dischargeDate, SSN, null, null);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDate = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDate + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;

        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that a new row is added into hospital_admit_check table in database");
        List<HospitalAdmitCheck> hospitalAdmitChecks = rpmDao.getHospitalAdmitCheckBySubsId(null, subsID);
        Assert.assertEquals(hospitalAdmitChecks.size(), 1, "Expected one hospital admit check record, subId="+subsID);
        Assert.assertEquals(hospitalAdmitChecks.get(0).getSubsId(), subsID, "Subscriber ID does not match");
        Assert.assertEquals(hospitalAdmitChecks.get(0).getSsn(), Integer.parseInt(SSN), "SSN does not match");
        Assert.assertEquals(HOSPITAL_ADMIT_DATE_FORMAT.format(hospitalAdmitChecks.get(0).getDob()), DOB, "DOB does not match");
        Assert.assertEquals(HOSPITAL_ADMIT_DATE_FORMAT.format(hospitalAdmitChecks.get(0).getAdmitDt()), admitDate, "Admit Date does not match");
        Assert.assertEquals(HOSPITAL_ADMIT_DATE_FORMAT.format(hospitalAdmitChecks.get(0).getDischargeDt()), dischargeDate, "Discharge Date does not match");
        Assert.assertEquals(hospitalAdmitChecks.get(0).getPtLNm(), lName, "Last Name does not match");
        String expectedFirstName = StringUtils.isNotBlank(mName) ? fName+","+mName : fName;
        Assert.assertEquals(hospitalAdmitChecks.get(0).getPtFNm(), expectedFirstName, "First Name does not match");
        Assert.assertNull(hospitalAdmitChecks.get(0).getXrefId(), "XREF ID should be null");
        Assert.assertNull(hospitalAdmitChecks.get(0).getAccnId(), "Accession ID should be null");
        Assert.assertNull(hospitalAdmitChecks.get(0).getFacId(), "Facility ID should be null");
        Assert.assertNull(hospitalAdmitChecks.get(0).getPtTypId(), "Patient Type ID should be null");

        logger.info("*** Step 3 Action: Clear test data");
        daoManagerXifinRpm.delHospitalAdmitCheckFromHOSPITAL_ADMIT_CHECK(subsID, DOB, admitDate, testDb);
        fileManipulation.deleteFile(filePathProcessed, fileName);
    }

    @Test(priority = 1, description = "Hospital Admit - Run import engine with code type I")
    @Parameters({"formatType"})
    public void testPFER_99(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_99 ======");

        logger.info("*** Step 1 Action: Create document valid format with type I");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        String type = "I";
        String subsID = randomCharacter.getRandomNumericString(9);
        String lName = randomCharacter.getRandomAlphaString(6);
        String fName = randomCharacter.getRandomAlphaString(8);
        String mName = randomCharacter.getRandomAlphaString(1);
        String DOB = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String admitDate = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String dischargeDate = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String SSN = "123456789";//randomCharacter.getNonZeroRandomNumericString(5);
        String textInput = importEngineUtils.createDataForHospitalAdmitFile(type, subsID, lName, fName, mName, DOB, admitDate, dischargeDate, SSN, null, null);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDate = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDate + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that a new row is added into hospital_admit_check table in database");
        List<String> dataInDB = daoManagerXifinRpm.getHospitalAdmitCheckFromHOSPITAL_ADMIT_CHECK(subsID, DOB, admitDate, testDb);
        Assert.assertEquals(dischargeDate, dataInDB.get(0), "       Discharge Date should be saved properly.");
        Assert.assertEquals(fName + "," + mName, dataInDB.get(2), "       First name and middle name should be saved properly.");
        Assert.assertEquals(lName, dataInDB.get(3), "       Last name should be saved properly.");

        logger.info("*** Step 3 Action: Clear test data");
        daoManagerXifinRpm.delHospitalAdmitCheckFromHOSPITAL_ADMIT_CHECK(subsID, DOB, admitDate, testDb);
        fileManipulation.deleteFile(filePathProcessed, fileName);
    }

    @Test(priority = 1, description = "Hospital Admit - Run import engine with valid document format and code type X")
    @Parameters({"formatType"})
    public void testPFER_101(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_101 ======");

        logger.info("*** Step 1 Action: Create document valid format with type A");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        String type = "A";
        String subsID = randomCharacter.getRandomNumericString(9);
        String lName = randomCharacter.getRandomAlphaString(5);
        String fName = randomCharacter.getRandomAlphaString(10);
        String mName = randomCharacter.getRandomAlphaString(1);
        String DOB = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String admitDate = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String dischargeDate = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String SSN = "123456789";

        String textInput = importEngineUtils.createDataForHospitalAdmitFile(type, subsID, lName, fName, mName, DOB, admitDate, dischargeDate, SSN, null, null);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDate = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDate + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that a new row is added into hospital_admit_check table in database");
        List<String> dataInDB = daoManagerXifinRpm.getHospitalAdmitCheckFromHOSPITAL_ADMIT_CHECK(subsID, DOB, admitDate, testDb);
        Assert.assertEquals(dischargeDate, dataInDB.get(0), "       Discharge Date should be saved properly.");
        Assert.assertEquals(fName + "," + mName, dataInDB.get(2), "       First name and middle name should be saved properly.");
        Assert.assertEquals(lName, dataInDB.get(3), "       Last name should be saved properly.");

        fileManipulation.deleteFile(filePathProcessed, fileName);

        logger.info("*** Step 3 Action: Create document valid format with type X , dob, admitDate, subID the same with step 1");
        type = "X";
        textInput = importEngineUtils.createDataForHospitalAdmitFile(type, subsID, lName, fName, mName, DOB, admitDate, dischargeDate, SSN, null, null);

        currentDate = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = formatType + "_" + currentDate + ".txt";
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 3 Expected Result: Verify that the new file is generated in incoming folder");
        fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 4 Action: Run Platform Import Engine");
        waitForImportEngine(fIncoming, isFileExists(fIncoming, 5), QUEUE_WAIT_TIME_MS);
        Thread.sleep(5000);

        logger.info("*** Step 4 Expected Result: Verify that the matching record is deleted from hospital_admit_check table in database");
        dataInDB = daoManagerXifinRpm.getHospitalAdmitCheckFromHOSPITAL_ADMIT_CHECK(subsID, DOB, admitDate, testDb);
        Assert.assertEquals(dataInDB.size(), 0, "       Record with SUBS_ID " + subsID + " should be deleted from HOSPITAL_ADMIT_CHECK table in DB.");

        logger.info("*** Step 5 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
    }

    @Test(priority = 1, description = "HOSPITALADMIT-File format with doc type D and update Discharge Date to existing record")
    @Parameters({"formatType"})
    public void testPFER_97(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_97 ======");

        logger.info("*** Step 1 Action: Create document valid format with type A");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        String type = "A";
        String subsID = randomCharacter.getRandomNumericString(9);
        String lName = randomCharacter.getRandomAlphaString(5);
        String fName = randomCharacter.getRandomAlphaString(10);
        String mName = randomCharacter.getRandomAlphaString(1);
        String DOB = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String admitDate = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String dischargeDate = "";//timeStamp.getNextDay("yyyy/MM/dd", new Date());
        String SSN = "123456789";

        String textInput = importEngineUtils.createDataForHospitalAdmitFile(type, subsID, lName, fName, mName, DOB, admitDate, dischargeDate, SSN, null, null);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDate = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDate + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that a new row is added into hospital_admit_check table in database");
        List<String> dataInDB = daoManagerXifinRpm.getHospitalAdmitCheckFromHOSPITAL_ADMIT_CHECK(subsID, DOB, admitDate, testDb);
        Assert.assertEquals(dischargeDate, dataInDB.get(0), "       Discharge Date should be saved properly.");
        Assert.assertEquals(fName + "," + mName, dataInDB.get(2), "       First name and middle name should be saved properly.");
        Assert.assertEquals(lName, dataInDB.get(3), "       Last name should be saved properly.");

        fileManipulation.deleteFile(filePathProcessed, fileName);

        logger.info("*** Step 3 Action: Create a new document with type D and new Discharge Date. Other data for DOB, admitDate, subsID are the same as step 1.");
        type = "D";
        dischargeDate = timeStamp.getNextDay("yyyy/MM/dd", new Date());
        textInput = importEngineUtils.createDataForHospitalAdmitFile(type, subsID, lName, fName, mName, DOB, admitDate, dischargeDate, SSN, null, null);

        currentDate = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = formatType + "_" + currentDate + ".txt";
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 3 Expected Result: Verify that the new file is generated in incoming folder");
        fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 4 Action: Run Platform Import Engine");
        waitForImportEngine(fIncoming, isFileExists(fIncoming, 5), QUEUE_WAIT_TIME_MS);
        Thread.sleep(5000);

        logger.info("*** Step 4 Expected Result: Verify that the matching row is updated with the new Discharge Date in hospital_admit_check table in database");
        dataInDB = daoManagerXifinRpm.getHospitalAdmitCheckFromHOSPITAL_ADMIT_CHECK(subsID, DOB, admitDate, testDb);
        Assert.assertEquals(dischargeDate, dataInDB.get(0), "       Discharge Date should be updated properly.");
        Assert.assertEquals(fName + "," + mName, dataInDB.get(2), "       First name and middle name should be saved properly.");
        Assert.assertEquals(lName, dataInDB.get(3), "       Last name should be saved properly.");

        logger.info("*** Step 5 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        daoManagerXifinRpm.delHospitalAdmitCheckFromHOSPITAL_ADMIT_CHECK(subsID, DOB, admitDate, testDb);
    }

    /*USPS*/
    @Test(priority = 1, description = "USPS-newPtAd1='NEW ADDRESS INFORMATION UNKNOWN', Status!=[1,11,51,1010], Pt exists")
    @Parameters({"formatType"})
    public void testPFER_136(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_136 =====");

        logger.info("*** Step 1 Action: Create USPS document with Accession has Patient records and Accession Status is not {1,11,51,1010} and Ncoa Code is not 'M' or 'B'");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
        //The accession status can't be Obsolete (62)
        List<String> accnInfoList = daoManagerXifinRpm.getAccnHasPtAddrZipFromACCNByAccnStatus(new int[]{1, 11, 51, 1010, 62, 31}, false, testDb);
        String accnId = accnInfoList.get(0);
        String oldPatientAddress = accnInfoList.get(1);
        String oldPatientZipID = accnInfoList.get(2);
        String newPatientAddr1 = "NEW ADDRESS INFORMATION UNKNOWN";
        String newPatientAddr2 = "NEWADDR2" + randomCharacter.getRandomAlphaString(5);
        String newPatientCity = "San Diego";
        String newPatientStateId = "CA";
        String newPatientZipId = "92129";
        String NcoaCdType = randomCharacter.getRandomAlphaString(4);
        //Get a Ncoa code that is NOT M or B
        String NcoaCd = randomCharacter.getRandomAlphaString(1).replace("M", "A").replace("B", "C");
        String textInput = importEngineUtils.createDataForUSPSfile(accnId, oldPatientAddress, oldPatientZipID, newPatientAddr1, newPatientAddr2, newPatientCity, newPatientStateId, newPatientZipId, NcoaCdType, NcoaCd);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDate = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDate + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        String ptSeqId = accnInfoList.get(4);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fIncoming, isFileExists(fIncoming, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fIncoming, 5), "        Import file: " + fileName + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected: Verify that the Accession was being pushed into Q_VALIDATE_ACCN and PT_DEMO_CHK. And the PT_PYR got suspended.");
        String sysDt = timeStamp.getCurrentDate();
        String qValidateAccnId = daoManagerXifinRpm.getAccnIdFromQVALIDATEACCNDELByAccnId(accnId, sysDt, testDb);
        Assert.assertNotNull(qValidateAccnId, "       Accession ID " + accnId + " should be in Q_VALIDATE_ACCN.");

        ArrayList<String> ptDemoChkInfoList = daoManagerPlatform.getPtDemoChkInfoFromPTDEMOCHKByPtSeqId(ptSeqId, testDb);
        String createDt = ptDemoChkInfoList.get(2);
        Assert.assertEquals(createDt, sysDt, "        PT_DEMO_CHK.CREATE_DT should be " + sysDt + " for pt_seq_id = " + ptSeqId);
//		String audUser = ptDemoChkInfoList.get(5);
//		Assert.assertTrue(audUser.contains("engine~ImportEngine"),"       The PT_DEMO_CHK.AUD_USER should be  engine~ImportEngine.");

        ArrayList<String> ptPyrSuspInfoList = daoManagerPlatform.getPtPyrSuspInfoFromPTPYRSUSPENDREASONDTByPtSeqId(ptSeqId, testDb);
        String reasonDt = ptPyrSuspInfoList.get(3);
        Assert.assertEquals(reasonDt, sysDt, "        PT_PYR_SUSPEND_REASON_DT.REASON_DT should be " + sysDt + " for pt_seq_id = " + ptSeqId);
        Assert.assertTrue(ptPyrSuspInfoList.get(4).isEmpty(), "        PT_PYR_SUSPEND_REASON_DT.FIX_DT should be empty for pt_seq_id = " + ptSeqId);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
    }

    /* USPS - SMOKE TEST */
    @Test(priority = 1, description = "USPS-NCOACD !=[M,B] and accn status = 1 and no EPI on the accn")
    @Parameters({"formatType","accnId"})
    public void testPFER_130(String formatType, String accnId) throws Exception {
        logger.info("====== Testing - testPFER_130 =====");

        logger.info("*** Step 1 Action: Create USPS document with Accession without Patient records and Accession Status is {1} and Ncoa Code is not 'M' or 'B'");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
//        List<String> accnInfoList = daoManagerXifinRpm.getAccnHasAddrZipNoEPIFromACCNByAccnStatus(new int[]{1}, true, testDb);
//        String accnId = accnInfoList.get(0);
        Accn accn = accessionDao.getAccn(accnId);
        String oldPatientAddress = accn.getPtAddr1();
        String oldPatientZipID = accn.getPtZipId();
        String newPatientAddr1 = "NEWADDR1" + randomCharacter.getRandomAlphaString(5);
        String newPatientAddr2 = "NEWADDR2" + randomCharacter.getRandomAlphaString(5);
        String newPatientCity = "San Diego";
        String newPatientStateId = "CA";
        String newPatientZipId = "92128";
        String NcoaCdType = randomCharacter.getRandomAlphaString(4);
        //Get a Ncoa code that is NOT M or B
        String NcoaCd = randomCharacter.getRandomAlphaString(1).replace("M", "A").replace("B", "C");
        String textInput = importEngineUtils.createDataForUSPSfile(accnId, oldPatientAddress, oldPatientZipID, newPatientAddr1, newPatientAddr2, newPatientCity, newPatientStateId, newPatientZipId, NcoaCdType, NcoaCd);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected: Verify that Bad Address error is added to the ACCN_PYR_ERR table");
        String currDt = timeStamp.getCurrentDate("MM/dd/YYYY");
        //errCd = '2000' is Bad Address error code
        ArrayList<String> accnPyrErrInfoList = daoManagerPlatform.getAccnPyrErrInfoFromACCNPYRERRByAccnIdErrCdErrDt(accnId, "2000", currDt, testDb);
        int pyrPrio = Integer.parseInt(accnPyrErrInfoList.get(0));
        String ptPyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, pyrPrio, testDb);
        String pyrGrpName = daoManagerAccnWS.getPayorGroupNameByPayorId(ptPyrAbbrev, testDb);
        Assert.assertEquals(pyrGrpName, "Patient", "       Bad Address error should be added to the Patient Payor on the accession " + accnId);
        String fixDt = accnPyrErrInfoList.get(1);
        Assert.assertTrue(fixDt.isEmpty(), "       Bad Address error should NOT be fixed.");

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);

        logger.info("*** Step 4 Action: Create USPS document with Accession without Patient records and Accession Status is {1} and Ncoa Code is 'M'");
        NcoaCd = "M";
        textInput = importEngineUtils.createDataForUSPSfile(accnId, oldPatientAddress, oldPatientZipID, newPatientAddr1, newPatientAddr2, newPatientCity, newPatientStateId, newPatientZipId, NcoaCdType, NcoaCd);

        currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = formatType + "_" + currentDateTime + ".txt";
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 4 Expected Result: Verify that the new file is generated in incoming folder");
        fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 5 Action: Run Platform Import Engine");
        fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 5 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 5 Expected: Verify that Bad Address error is Fixed");
        accnPyrErrInfoList = daoManagerPlatform.getAccnPyrErrInfoFromACCNPYRERRByAccnIdErrCdErrDt(accnId, "2000", currDt, testDb);
        fixDt = accnPyrErrInfoList.get(1);
        Assert.assertEquals(fixDt, currDt, "       Bad Address error should be fixed.");

        logger.info("*** Step 5 Expected: Verify that the Patient Addresses in ACCN table got updated");
        accn = accessionDao.getAccn(accnId);
        String updatedPtAddr1 = accn.getPtAddr1();
        String updatedPtAddr2 = accn.getPtAddr2();
        String updatedPtZip = accn.getPtZipId();
        Assert.assertEquals(updatedPtAddr1, newPatientAddr1, "       The Patient Address1 in ACCN table should be updated to " + newPatientAddr1);
        Assert.assertEquals(updatedPtAddr2, newPatientAddr2, "       The Patient Address2 in ACCN table should be updated to " + newPatientAddr2);
        Assert.assertEquals(updatedPtZip, newPatientZipId, "       The Patient Zip in ACCN table should be updated to " + newPatientZipId);

        logger.info("*** Step 6 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);

    }

    @Test(priority = 1, description = "USPS-NCOACD!=[M,B] and FK_PT_Sta_id!=[1,11,51,1010] and no EPI on the accession")
    @Parameters({"formatType"})
    public void testPFER_134(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_134 =====");

        logger.info("*** Step 1 Action: Create USPS document with Accession without Patient records and Accession Status is {21} and Ncoa Code is not 'M' or 'B'");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
        List<String> accnInfoList = daoManagerXifinRpm.getAccnHasAddrZipNoEPIFromACCNByAccnStatus(new int[]{21}, true, testDb);
        String accnId = accnInfoList.get(0);
        String oldPatientAddress = accnInfoList.get(1);
        String oldPatientZipID = accnInfoList.get(2);
        String newPatientAddr1 = "NEWADDR1" + randomCharacter.getRandomAlphaString(5);
        String newPatientAddr2 = "NEWADDR2" + randomCharacter.getRandomAlphaString(5);
        String newPatientCity = "San Diego";
        String newPatientStateId = "CA";
        String newPatientZipId = "92130";
        String NcoaCdType = randomCharacter.getRandomAlphaString(4);
        //Get a Ncoa code that is NOT M or B
        String NcoaCd = randomCharacter.getRandomAlphaString(1).replace("M", "A").replace("B", "C");
        String textInput = importEngineUtils.createDataForUSPSfile(accnId, oldPatientAddress, oldPatientZipID, newPatientAddr1, newPatientAddr2, newPatientCity, newPatientStateId, newPatientZipId, NcoaCdType, NcoaCd);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected: Verify that Bad Address error is added to the ACCN_PYR_ERR table");
        String currDt = timeStamp.getCurrentDate("MM/dd/YYYY");
        //errCd = '2000' is Bad Address error code
        ArrayList<String> accnPyrErrInfoList = daoManagerPlatform.getAccnPyrErrInfoFromACCNPYRERRByAccnIdErrCdErrDt(accnId, "2000", currDt, testDb);
        int pyrPrio = Integer.parseInt(accnPyrErrInfoList.get(0));
        String ptPyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, pyrPrio, testDb);
        String pyrGrpName = daoManagerAccnWS.getPayorGroupNameByPayorId(ptPyrAbbrev, testDb);
        Assert.assertEquals(pyrGrpName, "Patient", "       Bad Address error should be added to the Patient Payor on the accession " + accnId);
        String fixDt = accnPyrErrInfoList.get(1);
        Assert.assertTrue(fixDt.isEmpty(), "       Bad Address error should NOT be fixed.");

        logger.info("*** Step 2 Expected: Verify that the accession was pushed into Q_EP_UNBILLABLE");
        String errDt = daoManagerPlatform.getInDtFromQEPUNBILLABLEByAccnId(accnId, testDb).get(0);
        Assert.assertEquals(errDt, currDt, "       The Accession ID: " + accnId + " should be in Q_EP_UNBILLABLE.");

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);

        logger.info("*** Step 4 Action: Create USPS document with Accession without Patient records and Accession Status is {21} and Ncoa Code is 'B'");
        NcoaCd = "B";
        textInput = importEngineUtils.createDataForUSPSfile(accnId, oldPatientAddress, oldPatientZipID, newPatientAddr1, newPatientAddr2, newPatientCity, newPatientStateId, newPatientZipId, NcoaCdType, NcoaCd);

        currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = formatType + "_" + currentDateTime + ".txt";
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 4 Expected Result: Verify that the new file is generated in incoming folder");
        fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 5 Action: Run Platform Import Engine");
        fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 5 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 5 Expected: Verify that Bad Address error is Fixed");
        accnPyrErrInfoList = daoManagerPlatform.getAccnPyrErrInfoFromACCNPYRERRByAccnIdErrCdErrDt(accnId, "2000", currDt, testDb);
        fixDt = accnPyrErrInfoList.get(1);
        Assert.assertEquals(fixDt, currDt, "       Bad Address error should be fixed.");

        logger.info("*** Step 5 Expected: Verify that the Patient Addresses in ACCN table got updated");
        accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);
        String updatedPtAddr1 = accnInfoList.get(9);
        String updatedPtAddr2 = accnInfoList.get(10);
        String updatedPtZip = accnInfoList.get(11);
        Assert.assertEquals(updatedPtAddr1, newPatientAddr1, "       The Patient Address1 in ACCN table should be updated to " + newPatientAddr1);
        Assert.assertEquals(updatedPtAddr2, newPatientAddr2, "       The Patient Address2 in ACCN table should be updated to " + newPatientAddr2);
        Assert.assertEquals(updatedPtZip, newPatientZipId, "       The Patient Zip in ACCN table should be updated to " + newPatientZipId);

        logger.info("*** Step 6 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);

    }

    @Test(priority = 1, description = "DEFAULTREFUND-AccnRefund-Run import engine with identifier is not accnId")
    @Parameters({"formatType"})
    public void testPFER_253(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_253 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Generic Refund file with Accession ID does not exist in the ACCN_REFUND table");
        String accnId = "NONEXISTINGACCNID" + randomCharacter.getNonZeroRandomNumericString(5);
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getFutureDate("MM/dd/yyyy", 2);
        String refundAmt = "10";
        String chkClearDt = timeStamp.getCurrentDate();
        String refundNote = "AUTOTEST " + randomCharacter.getRandomAlphaNumericString(8);
        String textInput = importEngineUtils.createDefaultRefundFile(accnId, refundAmt, refundDt, chkNum, chkClearDt, refundNote);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that error file will be generated");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertTrue(isFileExists(fErrored, 5), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that proper error message was generated in the error file");
        String errFileStr = engineUtils.readFile(filePathErrored, fileName + ".error");
        Assert.assertTrue(errFileStr.contains("Errors: Refunds could not be identified for record Identifier: " + accnId), "        The error file should contains: Errors: Refunds could not be identified for record Identifier: " + accnId);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        fileManipulation.deleteFile(filePathErrored, fileName);
    }

    @Test(priority = 1, description = "USPS-newPtAd1=null, NcoaCd=B,AccnStatus=21, P with Self relationship ")
    @Parameters({"formatType","accnId"})
    public void testPFER_143(String formatType, String accnId) throws Exception {
        logger.info("====== Testing - testPFER_143 =====");

        logger.info("*** Step 1 Action: Create USPS document with Accession has Patient records and Accession Status is not {1,11,51,1010} and Ncoa Code is 'B'");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Get an Accession which status is 21
//        List<String> accnInfoList = daoManagerXifinRpm.getAccnHasAddrZipNoEPIFromACCNByAccnStatus(new int[]{21}, true, testDb);

//        String accnId = accnInfoList.get(0);
        Accn accn =accessionDao.getAccn(accnId);
        String oldPatientAddress = accn.getPtAddr1();
        String oldPatientZipID = accn.getPtZipId();
        String newPatientAddr1 = "";
        String newPatientAddr2 = "NEWADDR2" + randomCharacter.getRandomAlphaString(5);

        ArrayList<String> zipInfoList = daoManagerPayorWS.getZipInfo(testDb);
        String newPatientCity = zipInfoList.get(2);
        String newPatientStateId = zipInfoList.get(1);
        String newPatientZipId = zipInfoList.get(0);
        String NcoaCdType = randomCharacter.getRandomAlphaString(4);
        //Get a Ncoa code that is NOT M or B
        String NcoaCd = "B";
        String textInput = importEngineUtils.createDataForUSPSfile(accnId, oldPatientAddress, oldPatientZipID, newPatientAddr1, newPatientAddr2, newPatientCity, newPatientStateId, newPatientZipId, NcoaCdType, NcoaCd);

        //Need to determine if the ins Addr1 and Zip in ACCN_PYR are matching the old pt Addr1 and Zip, if matches True;
        List<String> accnAndPyrInfoList = daoManagerXifinRpm.getAddrAndZipFromAccnAndAccn_pyrByAccnID(accnId, testDb);
        boolean isInsAddrZipMatch = false;
        if (accnAndPyrInfoList.get(0).equalsIgnoreCase(oldPatientAddress) && accnAndPyrInfoList.get(4).equals(oldPatientZipID)) {
            isInsAddrZipMatch = true;
        }

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDate = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDate + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + ".error" + " in " + filePathErrored + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that Patient Addr1 is not updated, Addr2 and Zip are updated in ACCN/ACCN_PYR tables.");
        accnAndPyrInfoList = daoManagerXifinRpm.getAddrAndZipFromAccnAndAccn_pyrByAccnID(accnId, testDb);
        if (isInsAddrZipMatch) {
            Assert.assertEquals(accnAndPyrInfoList.get(0), oldPatientAddress, "       INS_ADDR1 should be :" + oldPatientAddress);
            Assert.assertEquals(accnAndPyrInfoList.get(1), newPatientAddr2, "       INS_ADDR2 should be :" + newPatientAddr2);
            Assert.assertEquals(accnAndPyrInfoList.get(4), newPatientZipId, "       FK_INS_ZIP_ID should be :" + newPatientZipId);
        }
        Assert.assertEquals(accnAndPyrInfoList.get(5), oldPatientAddress, "       pt_ADDR1 should be :" + oldPatientAddress);
        Assert.assertEquals(accnAndPyrInfoList.get(6), newPatientAddr2, "       PT_ADDR2 should be :" + newPatientAddr2);
        Assert.assertEquals(accnAndPyrInfoList.get(9), newPatientZipId, "       FK_PT_ZIP_ID should be :" + newPatientZipId);

    }

    @Test(priority = 1, description = "Generic-Run Engine with valid document format")
    @Parameters({"formatType", "eligSvcId"})
    public void testPFER_71(String formatType, String eligSvcId) throws Exception {
        logger.info("====== Testing - testPFER_71 =====");

        logger.info("*** Step 1 Actions: - Create valid Generic document file");

        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

//		List<String> eligSvcInfoList = daoManagerPlatform.getEligSvcInfoFromELIGSVCByClassName("GenericPyrRosterEligibilityProvider", testDb);
        String div = rpmDao.getEligSvc(null, eligSvcId).getEligSvcName();
//		String eligSvcId = eligSvcInfoList.get(0);
        String subsId = "SUBSID" + randomCharacter.getRandomNumericString(8);
        String effDt = timeStamp.getCurrentDate("ddMMyyyy");
        String expDt = timeStamp.getFutureDate("ddMMyyyy", 365);
        String fName = "FirstName" + randomCharacter.getRandomAlphaString(4);
        String lName = "LastName" + randomCharacter.getRandomAlphaString(4);
        String mName = randomCharacter.getRandomAlphaString(1);
        String DOB = timeStamp.getPreviousDate("ddMMyyyy", 10000);
        String pyrId = daoManagerXifinRpm.getPayorID(testDb);
        String textInput = importEngineUtils.createDataForGenericFile(div, subsId, effDt, expDt, fName, lName, mName, DOB, pyrId);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDate = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDate + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 1), "        Import file: " + fileName + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that new data is added into ELIG_PYR_ROSTER table");
        List<String> eligInfoList = daoManagerXifinRpm.getEligInfoFromELIGPYRROSTERByPKSUBSID(eligSvcId, subsId, effDt, "ddMMyyyy", testDb);
        Assert.assertTrue(eligInfoList.size() > 0, "        New record is added into Elig_Pyr_Roster table");
        String convertedExpDt = timeStamp.getConvertedDate("ddMMyyyy", "MM/dd/yyyy", expDt);
        String convertedDOB = timeStamp.getConvertedDate("ddMMyyyy", "MM/dd/yyyy", DOB);
        Assert.assertEquals(eligInfoList.get(3), convertedExpDt, "        ELIG_PYR_ROSTER.EXP_DT should be " + convertedExpDt);
        Assert.assertEquals(eligInfoList.get(7), pyrId, "        ELIG_PYR_ROSTER.DATA1 (Payor Abbrev) should be " + pyrId);
        Assert.assertEquals(eligInfoList.get(8), div, "        ELIG_PYR_ROSTER.DATA2 should be " + div);
        Assert.assertEquals(eligInfoList.get(4), fName + "," + mName, "        ELIG_PYR_ROSTER.INS_F_NM should be " + fName + "," + mName);
        Assert.assertEquals(eligInfoList.get(5), lName, "        ELIG_PYR_ROSTER.INS_L_NM should be " + lName);
        Assert.assertEquals(eligInfoList.get(6), convertedDOB, "        ELIG_PYR_ROSTER.INS_DOB should be " + convertedDOB);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);

        daoManagerXifinRpm.deleteEligInfoFromELIGPYRROSTERByPKSUBSID(eligSvcId, subsId, effDt, "ddMMyyyy", testDb);
        eligInfoList = daoManagerXifinRpm.getEligInfoFromELIGPYRROSTERByPKSUBSID(eligSvcId, subsId, effDt, "ddMMyyyy", testDb);
        Assert.assertEquals(eligInfoList.size(), 0, "        Cleared data.");

    }

    @Test(priority = 1, description = "Generic-Update existing record with updated data; Duplicate error file generated")
    @Parameters({"formatType", "eligSvcId"})
    public void testPFER_121(String formatType, String eligSvcId) throws Exception {
        logger.info("====== Testing - testPFER_121 =====");

        logger.info("*** Step 1 Action: Create valid Generic document");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

//        List<String> eligSvcInfoList = daoManagerPlatform.getEligSvcInfoFromELIGSVCByClassName("GenericPyrRosterEligibilityProvider", testDb);
        String div = rpmDao.getEligSvc(null, eligSvcId).getEligSvcName();
//        String eligSvcId = eligSvcInfoList.get(0);
        String subsId = "SUBSID" + randomCharacter.getRandomNumericString(8);
        String effDt = timeStamp.getCurrentDate("ddMMyyyy");
        String expDt = timeStamp.getFutureDate("ddMMyyyy", 365);
        String fName = "FirstName" + randomCharacter.getRandomAlphaString(4);
        String lName = "LastName" + randomCharacter.getRandomAlphaString(4);
        String mName = randomCharacter.getRandomAlphaString(1);
        String DOB = timeStamp.getPreviousDate("ddMMyyyy", 10000);
        String pyrId = daoManagerXifinRpm.getPayorID(testDb);
        String textInput = importEngineUtils.createDataForGenericFile(div, subsId, effDt, expDt, fName, lName, mName, DOB, pyrId);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder; And error file was generated for duplicate record");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 2), "        Import file: " + fileName + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that new data is added into ELIG_PYR_ROSTER table");
        List<String> eligInfoList = daoManagerXifinRpm.getEligInfoFromELIGPYRROSTERByPKSUBSID(eligSvcId, subsId, effDt, "ddMMyyyy", testDb);
        Assert.assertTrue(eligInfoList.size() > 0, "        New record is added into Elig_Pyr_Roster table");
        String convertedExpDt = timeStamp.getConvertedDate("ddMMyyyy", "MM/dd/yyyy", expDt);
        String convertedDOB = timeStamp.getConvertedDate("ddMMyyyy", "MM/dd/yyyy", DOB);
        Assert.assertEquals(eligInfoList.get(3), convertedExpDt, "        ELIG_PYR_ROSTER.EXP_DT should be " + convertedExpDt);
        Assert.assertEquals(eligInfoList.get(7), pyrId, "        ELIG_PYR_ROSTER.DATA1 (Payor Abbrev) should be " + pyrId);
        Assert.assertEquals(eligInfoList.get(8), div, "        ELIG_PYR_ROSTER.DATA2 (Elig Svc Name) should be " + div);
        Assert.assertEquals(eligInfoList.get(4), fName + "," + mName, "        ELIG_PYR_ROSTER.INS_F_NM should be " + fName + "," + mName);
        Assert.assertEquals(eligInfoList.get(5), lName, "        ELIG_PYR_ROSTER.INS_L_NM should be " + lName);
        Assert.assertEquals(eligInfoList.get(6), convertedDOB, "        ELIG_PYR_ROSTER.INS_DOB should be " + convertedDOB);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);

        logger.info("*** Step 4 Action: Create a new Generic document with updated Name, DOB,Exp Date and Payor; Other info are the same");
        expDt = timeStamp.getFutureDate("ddMMyyyy", 200);
        fName = "NewFirstName" + randomCharacter.getRandomAlphaString(4);
        lName = "NewLastName" + randomCharacter.getRandomAlphaString(4);
        mName = randomCharacter.getRandomAlphaString(1);
        DOB = timeStamp.getPreviousDate("ddMMyyyy", 15000);
        pyrId = daoManagerXifinRpm.getPayorID(testDb);
        textInput = importEngineUtils.createDataForGenericFile(div, subsId, effDt, expDt, fName, lName, mName, DOB, pyrId);

        currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = formatType + "_" + currentDateTime + ".txt";
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 4 Expected Result: Verify that the new file is generated in incoming folder");
        fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 5 Action: Run Platform Import Engine");
        fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 5 Expected Result: Verify that the import file got processed and moved to processed folder");
        fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertTrue(isFileExists(fErrored, 2), "        Import file: " + fileName + " in " + filePathIncoming + " folder.");
        Assert.assertTrue(engineUtils.readFile(filePathErrored, fileName + ".error").trim().contains("Record already exists with same SubsId but with insured name"), "        The error document is generate with message 'Record already exists with same SubsId but with insured name'");

        logger.info("*** Step 5 Expected result: the existing record is updated");
        eligInfoList = daoManagerXifinRpm.getEligInfoFromELIGPYRROSTERByPKSUBSID(eligSvcId, subsId, effDt, "ddMMyyyy", testDb);
        convertedExpDt = timeStamp.getConvertedDate("ddMMyyyy", "MM/dd/yyyy", expDt);
        convertedDOB = timeStamp.getConvertedDate("ddMMyyyy", "MM/dd/yyyy", DOB);
        Assert.assertEquals(eligInfoList.get(3), convertedExpDt, "        ELIG_PYR_ROSTER.EXP_DT should be updated to " + convertedExpDt);
        Assert.assertEquals(eligInfoList.get(4), fName + "," + mName, "        ELIG_PYR_ROSTER.INS_F_NM should be updated to " + fName + "," + mName);
        Assert.assertEquals(eligInfoList.get(5), lName, "        ELIG_PYR_ROSTER.INS_L_NM should be updated to " + lName);
        Assert.assertEquals(eligInfoList.get(6), convertedDOB, "        ELIG_PYR_ROSTER.INS_DOB should be updated to " + convertedDOB);
        Assert.assertEquals(eligInfoList.get(7), pyrId, "        ELIG_PYR_ROSTER.DATA1 (Payor Abbrev) should be updated to " + pyrId);

        logger.info("*** Step 6 Action: Clear test data");
        daoManagerXifinRpm.deleteEligInfoFromELIGPYRROSTERByPKSUBSID(eligSvcId, subsId, effDt, "ddMMyyyy", testDb);
        fileManipulation.deleteFile(filePathProcessed, fileName);
        fileManipulation.deleteFile(filePathErrored, fileName + ".error");
        eligInfoList = daoManagerXifinRpm.getEligInfoFromELIGPYRROSTERByPKSUBSID(eligSvcId, subsId, effDt, "ddMMyyyy", testDb);
        Assert.assertEquals(eligInfoList.size(), 0, "        Cleared data.");

    }

    /*REFUND SMOKE-TEST*/
    @Test(priority = 1, description = "DEFAULTREFUND-AccnRefund-Run import engine when input data for require fields only")
    @Parameters({"formatType", "refundSeqId"})
    public void testPFER_252(String formatType, String refundSeqId) throws Exception {
        logger.info("====== Testing - testPFER_252 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        String accnId = accessionDao.getAccnRefundBySeqId(Integer.parseInt(refundSeqId)).getAccnId();
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getFutureDate("MM/dd/yyyy", 2);
        String refundAmt = "1";
        String chkClearDt = "";
        String refundNote = "AUTOTEST " + randomCharacter.getRandomAlphaNumericString(8);
        String textInput = importEngineUtils.createDefaultRefundFile(accnId, refundAmt, refundDt, chkNum, chkClearDt, refundNote);

        List<String> accnRefundInfoList = daoManagerXifinRpm.getAccnRefundInfofromACCNREFUNDByAccnIDSeqId(accnId, refundSeqId, testDb);
        int amtSent_Original = Integer.parseInt(accnRefundInfoList.get(3));
        int amtSent_New = amtSent_Original + Integer.parseInt(refundAmt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        System.out.println("File Path = " + filePathProcessed + fileName);
        File fErrored = new File(filePathErrored + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertFalse(isFileExists(fErrored, 2), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the data in ACCN_REFUND table got updated properly ");
        accnRefundInfoList = daoManagerXifinRpm.getAccnRefundInfofromACCNREFUNDByAccnIDSeqId(accnId, refundSeqId, testDb);
        Assert.assertTrue(accnRefundInfoList.get(1).contains(chkNum), "        ACCN_REFUND.CHK_NUM should be updated to " + chkNum);
        Assert.assertEquals(accnRefundInfoList.get(2), refundDt, "        ACCN_REFUND.CHECK_DT should be " + refundDt);
        Assert.assertEquals(Integer.parseInt(accnRefundInfoList.get(3)), amtSent_New, "        ACCN_REFUND.AMT_SENT should be " + amtSent_New);
        Assert.assertTrue(accnRefundInfoList.get(4).contains(refundNote), "        ACCN_REFUND.NOTE should be updated to " + refundNote);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        daoManagerXifinRpm.updateValuesFromACCNREFUNDByAccnIdSeqId(accnId, refundSeqId, testDb);
    }

    @Test(priority = 1, description = "ALLINAREFUND-AccnRefund-run import engine with AccnId in Accn_refund & AMT > AMT_SENT")
    @Parameters({"formatType", "refundSeqId"})
    public void testPFER_243(String formatType, String refundSeqId) throws Exception {
        logger.info("====== Testing - testPFER_243 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Allina Refund file with valid data");
        String accnId = accessionDao.getAccnRefundBySeqId(Integer.parseInt(refundSeqId)).getAccnId();
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getFutureDate("MM/dd/yyyy", 2);
        String refundAmt = "1";
        String refundType = "A"; // A = Accession
        String textInput = importEngineUtils.createAllinaRefundFile(accnId, chkNum, refundDt, refundAmt, refundSeqId, refundType);

        List<String> accnRefundInfoList = daoManagerXifinRpm.getAccnRefundInfofromACCNREFUNDByAccnIDSeqId(accnId, refundSeqId, testDb);
        int amtSent_Original = Integer.parseInt(accnRefundInfoList.get(3));
        int amtSent_New = amtSent_Original + Integer.parseInt(refundAmt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fIncoming, isFileExists(fIncoming, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the data in ACCN_REFUND table got updated properly ");
        accnRefundInfoList = daoManagerXifinRpm.getAccnRefundInfofromACCNREFUNDByAccnIDSeqId(accnId, refundSeqId, testDb);
        Assert.assertTrue(accnRefundInfoList.get(1).contains(chkNum), "        ACCN_REFUND.CHK_NUM should be updated to " + chkNum);
        Assert.assertEquals(accnRefundInfoList.get(2), refundDt, "        ACCN_REFUND.CHECK_DT should be " + refundDt);
        Assert.assertEquals(Integer.parseInt(accnRefundInfoList.get(3)), amtSent_New, "        ACCN_REFUND.AMT_SENT should be " + amtSent_New);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        daoManagerXifinRpm.updateValuesFromACCNREFUNDByAccnIdSeqId(accnId, refundSeqId, testDb);
    }

    @Test(priority = 1, description = "ALLINAREFUND-AccnRefund-run import engine with invalid AccnID")
    @Parameters({"formatType"})
    public void testPFER_283(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_283 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Generic Refund file with Accession ID does not exist in the ACCN_REFUND table");
        String accnId = "NONEXISTINGACCNID" + randomCharacter.getNonZeroRandomNumericString(5);
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getFutureDate("MM/dd/yyyy", 2);
        String refundAmt = "10.00";
        String refundType = "A"; // A = Accession
        String refundSeqId = randomCharacter.getNonZeroRandomNumericString(4);
        String textInput = importEngineUtils.createAllinaRefundFile(accnId, chkNum, refundDt, refundAmt, refundSeqId, refundType);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that error file will be generated");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertTrue(isFileExists(fErrored, 5), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that proper error message was generated in the error file");
        String errFileStr = engineUtils.readFile(filePathErrored, fileName + ".error");
        Assert.assertTrue(errFileStr.contains("Unable to find refund for record: Identifier: " + accnId), "        The error file should contains: Unable to find refund for record: Identifier: " + accnId);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        fileManipulation.deleteFile(filePathErrored, fileName);
    }

    @Test(priority = 1, description = "BAI2-Run import engine with 2 transaction detail(16) & 1st row Transaction Detail is 88")
    @Parameters({"formatType", "depId"})
    public void testPFER_187(String formatType, String depId) throws Exception {
        logger.info("====== Testing - testPFER_187 =====");

        logger.info("*** Step 1 Action: Create BAI2 file with valid data");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
        String senderId, receiverId, fileIdNum, physicalRecordLength, blockSize, fileControlTotal, numberOfGroup, numberOfRecord;
        senderId = randomCharacter.getNonZeroRandomNumericString(9);
        receiverId = randomCharacter.getNonZeroRandomNumericString(9);
        fileIdNum = randomCharacter.getNonZeroRandomNumericString(2);
        physicalRecordLength = randomCharacter.getNonZeroRandomNumericString(2);
        blockSize = randomCharacter.getRandomNumericString(2);
        fileControlTotal = randomCharacter.getRandomNumericString(2);
        numberOfGroup = randomCharacter.getNonZeroRandomNumericString(2);
        numberOfRecord = randomCharacter.getNonZeroRandomNumericString(2);
        String fileCreationDateStr = timeStamp.getCurrentDate("yyMMdd");
        String fileCreationTimeStr = randomCharacter.getRandomNumericString(4);
        String versionNum = "2";
        List<String> fileHeader = Arrays.asList("01", senderId, receiverId, fileCreationDateStr, fileCreationTimeStr, fileIdNum, physicalRecordLength, blockSize, versionNum);
        List<String> groupHeader = Arrays.asList("02", senderId, senderId, senderId, fileCreationDateStr, fileCreationTimeStr, senderId, fileCreationDateStr);
        List<String> accountIdAndSummary = Arrays.asList("03", senderId, senderId, senderId, randomCharacter.getRandomNumericString(2), randomCharacter.getRandomNumericString(2), senderId);

        List<String> typeCode = Collections.singletonList(daoManagerXifinRpm.getTypeCodeFromBANKBAITYP(testDb));
        String depAmt = paymentDao.getDepByDepId(Integer.parseInt(depId)).getDepAmtAsMoney().toString().split("\\.")[0];
        //The amount 1500000 in BAI2 file is actually $15,000.00
        if ((Double.valueOf(depAmt) % 1 > 0)) {
            depAmt = depAmt.replace(".", "");
            System.out.println("depAmt has decimal =" + depAmt);
        } else {
            depAmt = depAmt + "00";
            System.out.println("depAmt=" + depAmt);
        }
        List<String> amt = Collections.singletonList(depAmt);
        List<String> fundTyp = Collections.singletonList(importEngineUtils.getRandomFundTyp());

        String currDt = timeStamp.getCurrentDate();
        String values = "remit_dt = to_date ('" + currDt + "', 'MM/dd/yyyy')";
        daoManagerXifinRpm.updateDEPBySeqId(depId, values, testDb);

        String transactionDetail = importEngineUtils.CreateNewTransactionDetail("88,16", typeCode, amt, fundTyp, senderId, senderId, senderId);
        transactionDetail += "\r\n" + importEngineUtils.CreateNewTransactionDetail("16", typeCode, amt, fundTyp, senderId, senderId, senderId);
        List<String> AccountTrailer = Arrays.asList("49", fileControlTotal, numberOfGroup);
        List<String> GroupTrailer = Arrays.asList("98", fileControlTotal, numberOfGroup, numberOfRecord);
        List<String> FileTrailer = Arrays.asList("99", fileControlTotal, numberOfGroup, numberOfRecord);
        String textInput = importEngineUtils.createValidFileBAI2File(fileHeader, groupHeader, accountIdAndSummary, transactionDetail, AccountTrailer, GroupTrailer, FileTrailer);

        daoManagerXifinRpm.deleteBaiInfo(testDb);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected: Verify that data are saved into BANK_FILE_HIST and BANK TRANS properly");
        List<String> baiInfo = daoManagerXifinRpm.getBankInfoFromBANKFILEHISTByfilename(fileName, testDb);
        String seqId = baiInfo.get(0);
        List<List<String>> bankTransInfo = daoManagerXifinRpm.getBankTransFromBANKTRANSBySeqId(seqId, testDb);
        Assert.assertEquals(baiInfo.get(2), senderId, "        senderId is saved");
        Assert.assertEquals(baiInfo.get(3), receiverId, "        receiverId is saved");
        Assert.assertEquals(baiInfo.get(5), fileIdNum, "        fileIdNum is saved");
        Assert.assertEquals(baiInfo.get(6), versionNum, "        versionNum is saved");
        Assert.assertEquals(baiInfo.get(7), "1", "        Group Id is 1");
        Assert.assertEquals(baiInfo.get(8), numberOfRecord, "        RecordId is saved");
        Assert.assertEquals(baiInfo.get(9), importEngineUtils.checkGroupCNTandTransCNT(bankTransInfo), "        Transaction Id is saved");

        Assert.assertEquals(bankTransInfo.get(0).get(4), typeCode.get(0), "        Bank bai Type is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(1), seqId, "        Bank Bai File Id is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(2).substring(5), senderId.substring(5), "        AccnNum is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(6), importEngineUtils.checkFundTyp(fundTyp.get(0)), "        FundTyp is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(7), senderId, "        BankRef is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(8), senderId, "        CustRef is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(9), senderId, "        Text is saved");

//        logger.info("*** Step 2 Expected: Verify that Bank BAI Recondilation process has found the match and saved data into DEP_BANK_TRANS table");
//        ArrayList<String> depBankTransInfoList = daoManagerXifinRpm.getDepBankTransInfoFromDEPBANKTRANSByDepId(depId, testDb);
//        String depIdInDB = depBankTransInfoList.get(1);
//        String bankTransSeqIdInDB = depBankTransInfoList.get(0);
//        Assert.assertEquals(depIdInDB, depId, "        DEP_BANK_TRANS.FK_DEP_ID should be " + depId);
//        Assert.assertEquals(bankTransSeqIdInDB, bankTransInfo.get(0).get(0), "        DEP_BANK_TRANS.FK_BANK_TRANS_SEQ_ID should be " + seqId);
//        Assert.assertEquals(depBankTransInfoList.get(2), "A", "        DEP_BANK_TRANS.FK_BANK_TRANS_MATCH_TYP_ID should be A (Automatic match).");

        logger.info("*** Step 3 Action: Clear test data");
        daoManagerXifinRpm.deleteBaiInfo(testDb);
        baiInfo = daoManagerXifinRpm.getBankInfoFromBANKFILEHISTByfilename(fileName, testDb);
        Assert.assertEquals(baiInfo.size(), 0, "       Cleared test data.");
        fileManipulation.deleteFile(filePathProcessed, fileName);

    }

    @Test(priority = 1, description = "BAI2-Run import engine with FileHeader is lack of one required fields")
    @Parameters({"formatType"})
    public void testPFER_195(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_195 =====");

        logger.info("*** Step 1 Action: Create BAI2 file with valid format but has no required data");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
        String senderId, receiverId, fileIdNum, physicalRecordLength, blockSize;
        senderId = randomCharacter.getNonZeroRandomNumericString(9);
        receiverId = randomCharacter.getNonZeroRandomNumericString(9);
        fileIdNum = randomCharacter.getNonZeroRandomNumericString(2);
        physicalRecordLength = randomCharacter.getNonZeroRandomNumericString(2);
        blockSize = randomCharacter.getRandomNumericString(2);
        String fileCreationDateStr = timeStamp.getCurrentDate("yyMMdd");
        String fileCreationTimeStr = randomCharacter.getRandomNumericString(4);
        String versionNum = "2";
        List<String> fileHeader = Arrays.asList("01", senderId, receiverId, fileCreationDateStr, fileCreationTimeStr, fileIdNum, physicalRecordLength, blockSize, versionNum);
        List<String> groupHeader = Arrays.asList("02", senderId, senderId, senderId, fileCreationDateStr, fileCreationTimeStr, senderId, fileCreationDateStr);
        String textInput = importEngineUtils.createValidFileBAI2File(fileHeader, groupHeader, null, null, null, null, null);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fErrored = new File(filePathErrored + fileName);
        waitForImportEngine(fErrored, isFileExists(fErrored, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file was moved to errored folder");
        Assert.assertTrue(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathErrored + " folder.");

        logger.info("*** Step 2 Expected: Verify that a record was saved into BANK_FILE_HIST table with Failed status");
        List<String> baiInfo = daoManagerXifinRpm.getBankInfoFromBANKFILEHISTByfilename(fileName, testDb);
        Assert.assertEquals(baiInfo.get(11), "F", "       BANK_FILE_HIST.BANK_FILE_RESULT_TYP_ID = F (Failed).");
        Assert.assertEquals(baiInfo.get(12), "1", "       BANK_FILE_HIST.ERR_CNT = 1.");

        logger.info("*** Step 3 Action: Clear test data");
        daoManagerXifinRpm.deleteBaiInfo(testDb);
        fileManipulation.deleteFile(filePathErrored, fileName);

    }

    @Test(priority = 1, description = "BAI2-Run import engine with duplicate TransactionDetail")
    @Parameters({"formatType"})
    public void testPFER_208(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_208 =====");
        logger.info("*** Step 1 Action: Create BAI2 file with valid data");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
        String senderId, receiverId, fileIdNum, physicalRecordLength, blockSize, fileControlTotal, numberOfGroup, numberOfRecord;
        senderId = randomCharacter.getNonZeroRandomNumericString(9);
        receiverId = randomCharacter.getNonZeroRandomNumericString(9);
        fileIdNum = randomCharacter.getNonZeroRandomNumericString(2);
        physicalRecordLength = randomCharacter.getNonZeroRandomNumericString(2);
        blockSize = randomCharacter.getRandomNumericString(2);
        fileControlTotal = randomCharacter.getRandomNumericString(2);
        numberOfGroup = randomCharacter.getNonZeroRandomNumericString(2);
        numberOfRecord = randomCharacter.getNonZeroRandomNumericString(2);
        String fileCreationDateStr = timeStamp.getCurrentDate("yyMMdd");
        String fileCreationTimeStr = randomCharacter.getRandomNumericString(4);
        String versionNum = "2";
        List<String> fileHeader = Arrays.asList("01", senderId, receiverId, fileCreationDateStr, fileCreationTimeStr, fileIdNum, physicalRecordLength, blockSize, versionNum);
        List<String> groupHeader = Arrays.asList("02", senderId, senderId, senderId, fileCreationDateStr, fileCreationTimeStr, senderId, fileCreationDateStr);
        List<String> accountIdAndSummary = Arrays.asList("03", senderId, senderId, senderId, randomCharacter.getRandomNumericString(2), randomCharacter.getRandomNumericString(2), senderId);

        List<String> typeCode = Collections.singletonList(daoManagerXifinRpm.getTypeCodeFromBANKBAITYP(testDb));
        String depId = "32005";
        String depAmt = paymentDao.getDepByDepId(Integer.parseInt(depId)).getDepAmtAsMoney().toString().split("\\.")[0];
        //The amount 1500000 in BAI2 file is actually $15,000.00
        if ((Double.valueOf(depAmt) % 1 > 0)) {
            depAmt = depAmt.replace(".", "");
            System.out.println("depAmt has decimal =" + depAmt);
        } else {
            depAmt = depAmt + "00";
            System.out.println("depAmt=" + depAmt);
        }
        List<String> amt = Collections.singletonList(depAmt);
        List<String> fundTyp = Collections.singletonList(importEngineUtils.getRandomFundTyp());

        String currDt = timeStamp.getCurrentDate();
        String values = "remit_dt = to_date ('" + currDt + "', 'MM/dd/yyyy')";
        daoManagerXifinRpm.updateDEPBySeqId(depId, values, testDb);

        String transactionDetail = importEngineUtils.CreateNewTransactionDetail("16", typeCode, amt, fundTyp, senderId, senderId, senderId);
        transactionDetail += "\r\n" + importEngineUtils.CreateNewTransactionDetail("16", typeCode, amt, fundTyp, senderId, senderId, senderId);
        List<String> AccountTrailer = Arrays.asList("49", fileControlTotal, numberOfGroup);
        List<String> GroupTrailer = Arrays.asList("98", fileControlTotal, numberOfGroup, numberOfRecord);
        List<String> FileTrailer = Arrays.asList("99", fileControlTotal, numberOfGroup, numberOfRecord);
        String textInput = importEngineUtils.createValidFileBAI2File(fileHeader, groupHeader, accountIdAndSummary, transactionDetail, AccountTrailer, GroupTrailer, FileTrailer);

        daoManagerXifinRpm.deleteBaiInfo(testDb);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        waitForImportEngine(fIncoming, isFileExists(fIncoming, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected: Verify that data are saved into BANK_FILE_HIST, BANK TRANS, BANK_FILE_DUP_TRANS properly");
        List<String> baiInfo = daoManagerXifinRpm.getBankInfoFromBANKFILEHISTByfilename(fileName, testDb);
        String seqId = baiInfo.get(0);
        List<List<String>> bankTransInfo = daoManagerXifinRpm.getBankTransFromBANKTRANSBySeqId(seqId, testDb);
        Assert.assertEquals(baiInfo.get(2), senderId, "        senderId is saved");
        Assert.assertEquals(baiInfo.get(3), receiverId, "        receiverId is saved");
        Assert.assertEquals(baiInfo.get(5), fileIdNum, "        fileIdNum is saved");
        Assert.assertEquals(baiInfo.get(6), versionNum, "        versionNum is saved");
        Assert.assertEquals(baiInfo.get(7), "1", "        Group Id is 1");
        Assert.assertEquals(baiInfo.get(8), numberOfRecord, "        RecordId is saved");
        //A record should be saved into BANK_FILE_DUP_TRANS
        String transCnt = String.valueOf(daoManagerXifinRpm.getBankDubFromBANKFILEDUPTRANSBybankTransId(bankTransInfo.get(0).get(0), testDb).size());
        Assert.assertEquals(baiInfo.get(9), transCnt, "        BANK_FILE_HIST.TRANS_CNT = " + transCnt);
        Assert.assertEquals(baiInfo.get(10), "1", "        BANK_FILE_HIST.DUP_CNT = 1.");

        Assert.assertEquals(bankTransInfo.get(0).get(4), typeCode.get(0), "        Bank bai Type is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(1), seqId, "        Bank Bai File Id is saved");
        //BANK_TRANS.ACCT_NUM is saved with mask characters like XXXXX9689;
        Assert.assertEquals(bankTransInfo.get(0).get(2).substring(5), senderId.substring(5), "        AccnNum is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(6), importEngineUtils.checkFundTyp(fundTyp.get(0)), "        FundTyp is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(7), senderId, "        BankRef is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(8), senderId, "        CustRef is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(9), senderId, "        Text is saved");

        logger.info("*** Step 2 Expected: Verify that Bank BAI Recondilation process has found the match and saved data into DEP_BANK_TRANS table");
//        ArrayList<String> depBankTransInfoList = daoManagerXifinRpm.getDepBankTransInfoFromDEPBANKTRANSByDepId(depId, testDb);
//        String depIdInDB = depBankTransInfoList.get(1);
//        String bankTransSeqIdInDB = depBankTransInfoList.get(0);
//        Assert.assertEquals(depIdInDB, depId, "        DEP_BANK_TRANS.FK_DEP_ID should be " + depId);
//        Assert.assertEquals(bankTransSeqIdInDB, bankTransInfo.get(0).get(0), "        DEP_BANK_TRANS.FK_BANK_TRANS_SEQ_ID should be " + seqId);
//        Assert.assertEquals(depBankTransInfoList.get(2), "A", "        DEP_BANK_TRANS.FK_BANK_TRANS_MATCH_TYP_ID should be A (Automatic match).");

        logger.info("*** Step 3 Action: Clear test data");
        daoManagerXifinRpm.deleteBaiInfo(testDb);
        baiInfo = daoManagerXifinRpm.getBankInfoFromBANKFILEHISTByfilename(fileName, testDb);
        Assert.assertEquals(baiInfo.size(), 0, "       Cleared test data.");
        fileManipulation.deleteFile(filePathProcessed, fileName);

    }

    @Test(priority = 1, description = "BAI2-Run import engine with fundType is S")
    @Parameters({"formatType", "depId"})
    public void testPFER_230(String formatType, String depId) throws Exception {
        logger.info("====== Testing - testPFER_230 =====");
        logger.info("*** Step 1 Action: Create BAI2 file with valid format and data with Fund Type 'S'");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
        String senderId, receiverId, fileIdNum, physicalRecordLength, blockSize, fileControlTotal, numberOfGroup, numberOfRecord;
        senderId = randomCharacter.getNonZeroRandomNumericString(9);
        receiverId = randomCharacter.getNonZeroRandomNumericString(9);
        fileIdNum = randomCharacter.getNonZeroRandomNumericString(2);
        physicalRecordLength = randomCharacter.getNonZeroRandomNumericString(2);
        blockSize = randomCharacter.getRandomNumericString(2);
        fileControlTotal = randomCharacter.getRandomNumericString(2);
        numberOfGroup = randomCharacter.getNonZeroRandomNumericString(2);
        numberOfRecord = randomCharacter.getNonZeroRandomNumericString(2);
        String fileCreationDateStr = timeStamp.getCurrentDate("yyMMdd");
        String fileCreationTimeStr = randomCharacter.getRandomNumericString(4);
        String versionNum = "2";
        List<String> fileHeader = Arrays.asList("01", senderId, receiverId, fileCreationDateStr, fileCreationTimeStr, fileIdNum, physicalRecordLength, blockSize, versionNum);
        List<String> groupHeader = Arrays.asList("02", senderId, senderId, senderId, fileCreationDateStr, fileCreationTimeStr, senderId, fileCreationDateStr);
        List<String> accountIdAndSummary = Arrays.asList("03", senderId, senderId, senderId, randomCharacter.getRandomNumericString(2), randomCharacter.getRandomNumericString(2), senderId);

        List<String> typeCode = Collections.singletonList(daoManagerXifinRpm.getTypeCodeFromBANKBAITYP(testDb));
        String depAmtInDb = paymentDao.getDepByDepId(Integer.parseInt(depId)).getDepAmtAsMoney().toString().split("\\.")[0];
        //The amount 1500000 in BAI2 file is actually $15,000.00
        String depAmt = depAmtInDb;
        //The amount 1500000 in BAI2 file is actually $15,000.00
        if ((Double.valueOf(depAmt) % 1 > 0)) {
            depAmt = depAmt.replace(".", "");
            System.out.println("depAmt has decimal =" + depAmt);
        } else {
            depAmt = depAmt + "00";
            System.out.println("depAmt=" + depAmt);
        }
        List<String> amt = Collections.singletonList(depAmt);
        List<String> fundTyp = Collections.singletonList("S");

        String currDt = timeStamp.getCurrentDate();
        String values = "remit_dt = to_date ('" + currDt + "', 'MM/dd/yyyy')";
        daoManagerXifinRpm.updateDEPBySeqId(depId, values, testDb);

        String transactionDetail = importEngineUtils.CreateNewTransactionDetail("16", typeCode, amt, fundTyp, senderId, senderId, senderId);
        List<String> AccountTrailer = Arrays.asList("49", fileControlTotal, numberOfGroup);
        List<String> GroupTrailer = Arrays.asList("98", fileControlTotal, numberOfGroup, numberOfRecord);
        List<String> FileTrailer = Arrays.asList("99", fileControlTotal, numberOfGroup, numberOfRecord);
        String textInput = importEngineUtils.createValidFileBAI2File(fileHeader, groupHeader, accountIdAndSummary, transactionDetail, AccountTrailer, GroupTrailer, FileTrailer);

        daoManagerXifinRpm.deleteBaiInfo(testDb);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fErrored = new File(filePathErrored + fileName);
        waitForImportEngine(fErrored, isFileExists(fErrored, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected: Verify that data are saved into BANK_FILE_HIST, BANK TRANS, BANK_FILE_DUP_TRANS properly");
        List<String> baiInfo = daoManagerXifinRpm.getBankInfoFromBANKFILEHISTByfilename(fileName, testDb);
        String seqId = baiInfo.get(0);
        List<List<String>> bankTransInfo = daoManagerXifinRpm.getBankTransFromBANKTRANSBySeqId(seqId, testDb);
        Assert.assertEquals(baiInfo.get(2), senderId, "        senderId is saved");
        Assert.assertEquals(baiInfo.get(3), receiverId, "        receiverId is saved");
        Assert.assertEquals(baiInfo.get(5), fileIdNum, "        fileIdNum is saved");
        Assert.assertEquals(baiInfo.get(6), versionNum, "        versionNum is saved");
        Assert.assertEquals(baiInfo.get(7), "1", "        BANK_FILE_HIST.GRP_CNT = 1.");
        Assert.assertEquals(baiInfo.get(8), numberOfRecord, "        RecordId is saved");
        Assert.assertEquals(baiInfo.get(9), "1", "        BANK_FILE_HIST.TRANS_CNT = 1.");
        Assert.assertEquals(baiInfo.get(10), "0", "        BANK_FILE_HIST.DUP_CNT = 0.");
        Assert.assertEquals(baiInfo.get(11), "S", "        BANK_FILE_HIST.FK_BANK_FILE_RESULT_TYP_ID = S.");

        Assert.assertEquals(bankTransInfo.get(0).get(4), typeCode.get(0), "        Bank bai Type is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(1), seqId, "        Bank Bai File Id is saved");
        //BANK_TRANS.ACCT_NUM is saved with mask characters like XXXXX9689;
        Assert.assertEquals(bankTransInfo.get(0).get(2).substring(5), senderId.substring(5), "        AccnNum is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(6), importEngineUtils.checkFundTyp(fundTyp.get(0)), "        FundTyp is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(7), "", "        BankRef is NOT saved.");
        Assert.assertEquals(bankTransInfo.get(0).get(8), "", "        CustRef is NOT saved.");
        Assert.assertEquals(bankTransInfo.get(0).get(9), "", "        Text is NOT saved.");
        Assert.assertEquals(bankTransInfo.get(0).get(5), depAmtInDb, "        BANK_TRANS.AMT = " + depAmt);

//        logger.info("*** Step 2 Expected: Verify that Bank BAI Recondilation process has found the match and saved data into DEP_BANK_TRANS table");
//        ArrayList<String> depBankTransInfoList = daoManagerXifinRpm.getDepBankTransInfoFromDEPBANKTRANSByDepId(depId, testDb);
//        String depIdInDB = depBankTransInfoList.get(1);
//        String bankTransSeqIdInDB = depBankTransInfoList.get(0);
//        Assert.assertEquals(depIdInDB, depId, "        DEP_BANK_TRANS.FK_DEP_ID should be " + depId);
//        Assert.assertEquals(bankTransSeqIdInDB, bankTransInfo.get(0).get(0), "        DEP_BANK_TRANS.FK_BANK_TRANS_SEQ_ID should be " + seqId);
//        Assert.assertEquals(depBankTransInfoList.get(2), "A", "        DEP_BANK_TRANS.FK_BANK_TRANS_MATCH_TYP_ID should be A (Automatic match).");

        logger.info("*** Step 3 Action: Clear test data");
        daoManagerXifinRpm.deleteBaiInfo(testDb);
        baiInfo = daoManagerXifinRpm.getBankInfoFromBANKFILEHISTByfilename(fileName, testDb);
        Assert.assertEquals(baiInfo.size(), 0, "       Cleared test data.");
        fileManipulation.deleteFile(filePathProcessed, fileName);

    }

    @Test(priority = 1, description = "BAI2-Run import engine with fundType is V")
    @Parameters({"formatType", "depId"})
    public void testPFER_231(String formatType, String depId) throws Exception {
        logger.info("====== Testing - testPFER_231 =====");
        logger.info("*** Step 1 Action: Create BAI2 file with valid format and data with Fund Type 'V'");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
        String senderId, receiverId, fileIdNum, physicalRecordLength, blockSize, fileControlTotal, numberOfGroup, numberOfRecord;
        senderId = randomCharacter.getNonZeroRandomNumericString(9);
        receiverId = randomCharacter.getNonZeroRandomNumericString(9);
        fileIdNum = randomCharacter.getNonZeroRandomNumericString(2);
        physicalRecordLength = randomCharacter.getNonZeroRandomNumericString(2);
        blockSize = randomCharacter.getRandomNumericString(2);
        fileControlTotal = randomCharacter.getRandomNumericString(2);
        numberOfGroup = randomCharacter.getNonZeroRandomNumericString(2);
        numberOfRecord = randomCharacter.getNonZeroRandomNumericString(2);
        String fileCreationDateStr = timeStamp.getCurrentDate("yyMMdd");
        String fileCreationTimeStr = randomCharacter.getRandomNumericString(4);
        String versionNum = "2";
        List<String> fileHeader = Arrays.asList("01", senderId, receiverId, fileCreationDateStr, fileCreationTimeStr, fileIdNum, physicalRecordLength, blockSize, versionNum);
        List<String> groupHeader = Arrays.asList("02", senderId, senderId, senderId, fileCreationDateStr, fileCreationTimeStr, senderId, fileCreationDateStr);
        List<String> accountIdAndSummary = Arrays.asList("03", senderId, senderId, senderId, randomCharacter.getRandomNumericString(2), randomCharacter.getRandomNumericString(2), senderId);

        List<String> typeCode = Collections.singletonList(daoManagerXifinRpm.getTypeCodeFromBANKBAITYP(testDb));
        String depAmtInDb = paymentDao.getDepByDepId(Integer.parseInt(depId)).getDepAmtAsMoney().toString().split("\\.")[0];
        //The amount 1500000 in BAI2 file is actually $15,000.00
        String depAmt = depAmtInDb;
        if ((Double.valueOf(depAmt) % 1 > 0)) {
            depAmt = depAmt.replace(".", "");
            System.out.println("depAmt has decimal =" + depAmt);
        } else {
            depAmt = depAmt + "00";
            System.out.println("depAmt=" + depAmt);
        }
        List<String> amt = Collections.singletonList(depAmt);
        List<String> fundTyp = Collections.singletonList("V");

        String currDt = timeStamp.getCurrentDate();
        String values = "remit_dt = to_date ('" + currDt + "', 'MM/dd/yyyy')";
        daoManagerXifinRpm.updateDEPBySeqId(depId, values, testDb);

        String transactionDetail = importEngineUtils.CreateNewTransactionDetail("16", typeCode, amt, fundTyp, senderId, senderId, senderId);
        List<String> AccountTrailer = Arrays.asList("49", fileControlTotal, numberOfGroup);
        List<String> GroupTrailer = Arrays.asList("98", fileControlTotal, numberOfGroup, numberOfRecord);
        List<String> FileTrailer = Arrays.asList("99", fileControlTotal, numberOfGroup, numberOfRecord);
        String textInput = importEngineUtils.createValidFileBAI2File(fileHeader, groupHeader, accountIdAndSummary, transactionDetail, AccountTrailer, GroupTrailer, FileTrailer);

        daoManagerXifinRpm.deleteBaiInfo(testDb);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected: Verify that data are saved into BANK_FILE_HIST, BANK TRANS, BANK_FILE_DUP_TRANS properly");
        List<String> baiInfo = daoManagerXifinRpm.getBankInfoFromBANKFILEHISTByfilename(fileName, testDb);
        String seqId = baiInfo.get(0);
        List<List<String>> bankTransInfo = daoManagerXifinRpm.getBankTransFromBANKTRANSBySeqId(seqId, testDb);
        Assert.assertEquals(baiInfo.get(2), senderId, "        senderId is saved");
        Assert.assertEquals(baiInfo.get(3), receiverId, "        receiverId is saved");
        Assert.assertEquals(baiInfo.get(5), fileIdNum, "        fileIdNum is saved");
        Assert.assertEquals(baiInfo.get(6), versionNum, "        versionNum is saved");
        Assert.assertEquals(baiInfo.get(7), "1", "        BANK_FILE_HIST.GRP_CNT = 1.");
        Assert.assertEquals(baiInfo.get(8), numberOfRecord, "        RecordId is saved");
        Assert.assertEquals(baiInfo.get(9), "1", "        BANK_FILE_HIST.TRANS_CNT = 1.");
        Assert.assertEquals(baiInfo.get(10), "0", "        BANK_FILE_HIST.DUP_CNT = 0.");
        Assert.assertEquals(baiInfo.get(11), "S", "        BANK_FILE_HIST.FK_BANK_FILE_RESULT_TYP_ID = S.");

        Assert.assertEquals(bankTransInfo.get(0).get(4), typeCode.get(0), "        Bank bai Type is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(1), seqId, "        Bank Bai File Id is saved");
        //BANK_TRANS.ACCT_NUM is saved with mask characters like XXXXX9689;
        Assert.assertEquals(bankTransInfo.get(0).get(2).substring(5), senderId.substring(5), "        AccnNum is saved");
        Assert.assertEquals(bankTransInfo.get(0).get(6), importEngineUtils.checkFundTyp(fundTyp.get(0)), "        BANK_TRANS.FUNDS_TYP = " + fundTyp.get(0));
        Assert.assertEquals(bankTransInfo.get(0).get(7), senderId, "        BANK_TRANS.BANK_REF = " + senderId);
        Assert.assertEquals(bankTransInfo.get(0).get(8), "", "        CustRef is NOT saved.");
        Assert.assertEquals(bankTransInfo.get(0).get(9), "", "        Text is NOT saved.");
        Assert.assertEquals(bankTransInfo.get(0).get(5), depAmtInDb, "        BANK_TRANS.AMT = " + depAmt);

        logger.info("*** Step 2 Expected: Verify that Bank BAI Recondilation process has found the match and saved data into DEP_BANK_TRANS table");
//        ArrayList<String> depBankTransInfoList = daoManagerXifinRpm.getDepBankTransInfoFromDEPBANKTRANSByDepId(depId, testDb);
//        String depIdInDB = depBankTransInfoList.get(1);
//        String bankTransSeqIdInDB = depBankTransInfoList.get(0);
//        Assert.assertEquals(depIdInDB, depId, "        DEP_BANK_TRANS.FK_DEP_ID should be " + depId);
//        Assert.assertEquals(bankTransSeqIdInDB, bankTransInfo.get(0).get(0), "        DEP_BANK_TRANS.FK_BANK_TRANS_SEQ_ID should be " + seqId);
//        Assert.assertEquals(depBankTransInfoList.get(2), "A", "        DEP_BANK_TRANS.FK_BANK_TRANS_MATCH_TYP_ID should be A (Automatic match).");

        logger.info("*** Step 3 Action: Clear test data");
        daoManagerXifinRpm.deleteBaiInfo(testDb);
        baiInfo = daoManagerXifinRpm.getBankInfoFromBANKFILEHISTByfilename(fileName, testDb);
        Assert.assertEquals(baiInfo.size(), 0, "       Cleared test data.");
        fileManipulation.deleteFile(filePathProcessed, fileName);

    }

    @Test(priority = 1, description = "BAI2-Run import engine with document name is duplicate")
    @Parameters({"formatType", "depId"})
    public void testPFER_242(String formatType, String depId) throws Exception {
        logger.info("====== Testing - testPFER_242 =====");
        logger.info("*** Step 1 Action: Create BAI2 file with valid format and data with Fund Type 'V'");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
        String senderId, receiverId, fileIdNum, physicalRecordLength, blockSize, fileControlTotal, numberOfGroup, numberOfRecord;
        senderId = randomCharacter.getNonZeroRandomNumericString(9);
        receiverId = randomCharacter.getNonZeroRandomNumericString(9);
        fileIdNum = randomCharacter.getNonZeroRandomNumericString(2);
        physicalRecordLength = randomCharacter.getNonZeroRandomNumericString(2);
        blockSize = randomCharacter.getRandomNumericString(2);
        fileControlTotal = randomCharacter.getRandomNumericString(2);
        numberOfGroup = randomCharacter.getNonZeroRandomNumericString(2);
        numberOfRecord = randomCharacter.getNonZeroRandomNumericString(2);
        String fileCreationDateStr = timeStamp.getCurrentDate("yyMMdd");
        String fileCreationTimeStr = randomCharacter.getRandomNumericString(4);
        String versionNum = "2";
        List<String> fileHeader = Arrays.asList("01", senderId, receiverId, fileCreationDateStr, fileCreationTimeStr, fileIdNum, physicalRecordLength, blockSize, versionNum);
        List<String> groupHeader = Arrays.asList("02", senderId, senderId, senderId, fileCreationDateStr, fileCreationTimeStr, senderId, fileCreationDateStr);
        List<String> accountIdAndSummary = Arrays.asList("03", senderId, senderId, senderId, randomCharacter.getRandomNumericString(2), randomCharacter.getRandomNumericString(2), senderId);

        List<String> typeCode = Collections.singletonList(daoManagerXifinRpm.getTypeCodeFromBANKBAITYP(testDb));
        String depAmt = paymentDao.getDepByDepId(Integer.parseInt(depId)).getDepAmtAsMoney().toString().split("\\.")[0];
        //The amount 1500000 in BAI2 file is actually $15,000.00
        if ((Double.valueOf(depAmt) % 1 > 0)) {
            depAmt = depAmt.replace(".", "");
            System.out.println("depAmt has decimal =" + depAmt);
        } else {
            depAmt = depAmt + "00";
            System.out.println("depAmt=" + depAmt);
        }
        List<String> amt = Collections.singletonList(depAmt);
        List<String> fundTyp = Collections.singletonList("V");

        String currDt = timeStamp.getCurrentDate();
        String values = "remit_dt = to_date ('" + currDt + "', 'MM/dd/yyyy')";
        daoManagerXifinRpm.updateDEPBySeqId(depId, values, testDb);

        String transactionDetail = importEngineUtils.CreateNewTransactionDetail("16", typeCode, amt, fundTyp, senderId, senderId, senderId);
        List<String> AccountTrailer = Arrays.asList("49", fileControlTotal, numberOfGroup);
        List<String> GroupTrailer = Arrays.asList("98", fileControlTotal, numberOfGroup, numberOfRecord);
        List<String> FileTrailer = Arrays.asList("99", fileControlTotal, numberOfGroup, numberOfRecord);
        String textInput = importEngineUtils.createValidFileBAI2File(fileHeader, groupHeader, accountIdAndSummary, transactionDetail, AccountTrailer, GroupTrailer, FileTrailer);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        String filePathDup = dirBase + "dup" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);

        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 3 Action: Create a file has a duplicate filename");
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 3 Expected Result: Verify that the duplicate file is generated in incoming folder");
        fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 4 Action: Run Platform Import Engine");
        File fDup = new File(filePathDup + fileName);
        waitForImportEngine(fDup, isFileExists(fDup, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 4 Expected: Verify that the file will be moved to dup folder");
        Assert.assertTrue(isFileExists(fDup, 5), "        Import file: " + fileName + " should be moved to " + fDup + " folder.");

        logger.info("*** Step 5 Action: Clear test data");
        daoManagerXifinRpm.deleteBaiInfo(testDb);
        List<String> baiInfo = daoManagerXifinRpm.getBankInfoFromBANKFILEHISTByfilename(fileName, testDb);
        Assert.assertEquals(baiInfo.size(), 0, "       Cleared test data.");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        fileManipulation.deleteFile(filePathDup, fileName);

    }

    @Test(priority = 1, description = "TEVIX-Patient Payor, Relshp is Self, has PT and PT_PYR")
    @Parameters({"formatType", "accnId"})
    public void testPFER_172(String formatType, String accnId) throws Exception {
        logger.info("====== Testing - testPFER_172 =====");

        logger.info("*** Step 1 Actions: Create Tevix document with valid format");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

//        List<String> oldDataInDB = daoManagerXifinRpm.getPtAccnHasPtPyr(testDb);
//        String accnId = oldDataInDB.get(0);
        Accn accn = accessionDao.getAccn(accnId);
        String oldPTAdd1 = accn.getPtAddr1();
        String oldPTZIpID = accn.getPtZipId();
        List<AccnPyr> accnPyr = accessionDao.getAccnPyrByAccnId(accnId);
        String pyrId = String.valueOf(accnPyr.get(0).getPyrId());

        String newPTAdd1 = randomCharacter.getRandomAlphaString(6);
        String newPTAdd2 = randomCharacter.getRandomAlphaString(6);
        ArrayList<String> zipInfoList = daoManagerXifinRpm.getZipCodeFromZIP(testDb);
        String newPtCity = zipInfoList.get(1);
        String newPtStId = zipInfoList.get(2);
        String newPTZipId = zipInfoList.get(0);
        String newPTPhone = "858" + randomCharacter.getNonZeroRandomNumericString(7);
        String textInput = importEngineUtils.createDataForTevixFile(accnId, oldPTAdd1, oldPTZIpID, newPTAdd1, newPTAdd2, newPtCity, newPtStId, newPTZipId, newPTPhone);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathErrored + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are updated in ACCN table");
        List<String> updatedAccnInfoList = daoManagerXifinRpm.getAddrFromACCNByAccnID(accnId, testDb);
        Assert.assertEquals(updatedAccnInfoList.get(0), newPTAdd1, "        ACCN.PT_ADDR1 should be updated to " + newPTAdd1);
        Assert.assertEquals(updatedAccnInfoList.get(1), newPTAdd2, "        ACCN.PT_ADDR2 should be updated to " + newPTAdd2);
        Assert.assertEquals(updatedAccnInfoList.get(4), newPTZipId, "        ACCN.FK_PT_ZIP_ID should be updated to " + newPTZipId);
        Assert.assertEquals(updatedAccnInfoList.get(5), newPTPhone, "        ACCN.PT_HM_PHN should be updated to " + newPTPhone);

//        logger.info("*** Step 2 Expected Result: Verify that a new record is added into Q_VALIDATE_ACCN table");
//        String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");
//        Assert.assertEquals(daoManagerXifinRpm.getAccnIdFromQVALIDATEACCNDELByAccnId(accnId, currDt, testDb), accnId, "        Accession ID = " + accnId + "should be added in Q_Validate_ACCN table.");

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are updated in PT_PYR");
        String ptSeqId = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb).get(43);
        String dos = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb).get(4);
        dos = timeStamp.getConvertedDate("yyyy-MM-dd HH:mm:SS", "MM/dd/yyyy", dos);
        List<String> ptPyrInfoList = daoManagerXifinRpm.getPtPyrInfoFromPTPYRByPtSeqIdPyrIdDOS(ptSeqId, pyrId, dos, testDb);
        Assert.assertEquals(ptPyrInfoList.get(0), newPTAdd1, "        PT_PYR.INS_ADDR1 should be updated to " + newPTAdd1);
        Assert.assertEquals(ptPyrInfoList.get(1), newPTAdd2, "        PT_PYR.INS_ADDR2 should be updated to " + newPTAdd2);
        Assert.assertEquals(ptPyrInfoList.get(3), newPTZipId, "        PT_PYR.FK_INS_ZIP_ID should be updated to " + newPTZipId);
        Assert.assertEquals(ptPyrInfoList.get(4), newPTPhone, "        PT_PYR.INS_HM_PHN should be updated to " + newPTPhone);

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are updated in PT");
        List<String> ptInfoList = daoManagerXifinRpm.getPtInfoFromPTBySeqId(ptSeqId, testDb);
        Assert.assertEquals(ptInfoList.get(0), newPTAdd1, "        PT.PT_ADDR1 should be updated to " + newPTAdd1);
        Assert.assertEquals(ptInfoList.get(1), newPTAdd2, "        PT.PT_ADDR2 should be updated to " + newPTAdd2);
        Assert.assertEquals(ptInfoList.get(3), newPTZipId, "        PT.FK_PT_ZIP_ID should be updated to " + newPTZipId);
        Assert.assertEquals(ptInfoList.get(4), newPTPhone, "        PT.PT_HM_PHN should be updated to " + newPTPhone);

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are updated in ACCN_PYR");
        List<String> accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, pyrId, "1", testDb);
        Assert.assertEquals(accnPyrInfoList.get(0), newPTAdd1, "        ACCN_PYR.INS_ADDR1 should be updated to " + newPTAdd1);
        Assert.assertEquals(accnPyrInfoList.get(1), newPTAdd2, "        ACCN_PYR.INS_ADDR2 should be updated to " + newPTAdd2);
        Assert.assertEquals(accnPyrInfoList.get(2), newPTZipId, "        ACCN_PYR.FK_INS_ZIP_ID should be updated to " + newPTZipId);
        Assert.assertEquals(accnPyrInfoList.get(3), newPTPhone, "        ACCN_PYR.INS_HM_PHN should be updated to " + newPTPhone);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
//		daoManagerXifinRpm.delAccnIdFromQVALIDATEACCNDELByAccnId(accnId, currDt,testDb);
    }

    @Test(priority = 1, description = "TEVIX-Non-Patient Pyr, Relshp is Non-Self, has PT and PT_PYR")
    @Parameters({"formatType"})
    public void testPFER_175(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_175 =====");

        logger.info("*** Step 1 Actions: Create Tevix document with valid format");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);

        fileManipulation = new FileManipulation(driver);

        List<String> oldDataInDB = daoManagerXifinRpm.getNonPtNonSelfAccnHasPtPyr(testDb);
        String accnId = oldDataInDB.get(0);
        String oldPTAdd1 = oldDataInDB.get(1);
        String oldPTZIpID = oldDataInDB.get(3);
        String pyrId = oldDataInDB.get(9);

        String newPTAdd1 = randomCharacter.getRandomAlphaString(6);
        String newPTAdd2 = randomCharacter.getRandomAlphaString(6);
        ArrayList<String> zipInfoList = daoManagerXifinRpm.getZipCodeFromZIP(testDb);
        String newPtCity = zipInfoList.get(1);
        String newPtStId = zipInfoList.get(2);
        String newPTZipId = zipInfoList.get(0);
        String newPTPhone = "619" + randomCharacter.getNonZeroRandomNumericString(7);
        String textInput = importEngineUtils.createDataForTevixFile(accnId, oldPTAdd1, oldPTZIpID, newPTAdd1, newPTAdd2, newPtCity, newPtStId, newPTZipId, newPTPhone);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathErrored + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are updated in ACCN table");
        List<String> updatedAccnInfoList = daoManagerXifinRpm.getAddrFromACCNByAccnID(accnId, testDb);
        Assert.assertEquals(updatedAccnInfoList.get(0), newPTAdd1, "        ACCN.PT_ADDR1 should be updated to " + newPTAdd1);
        Assert.assertEquals(updatedAccnInfoList.get(1), newPTAdd2, "        ACCN.PT_ADDR2 should be updated to " + newPTAdd2);
        Assert.assertEquals(updatedAccnInfoList.get(4), newPTZipId, "        ACCN.FK_PT_ZIP_ID should be updated to " + newPTZipId);
        Assert.assertEquals(updatedAccnInfoList.get(5), newPTPhone, "        ACCN.PT_HM_PHN should be updated to " + newPTPhone);

        logger.info("*** Step 2 Expected Result: Verify that a new record is added into Q_VALIDATE_ACCN table");
        String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        Assert.assertEquals(daoManagerXifinRpm.getAccnIdFromQVALIDATEACCNDELByAccnId(accnId, currDt, testDb), accnId, "        Accession ID = " + accnId + "should be added in Q_Validate_ACCN table.");

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are updated in PT");
        String ptSeqId = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb).get(43);
        List<String> ptInfoList = daoManagerXifinRpm.getPtInfoFromPTBySeqId(ptSeqId, testDb);
        Assert.assertEquals(ptInfoList.get(0), newPTAdd1, "        PT.PT_ADDR1 should be updated to " + newPTAdd1);
        Assert.assertEquals(ptInfoList.get(1), newPTAdd2, "        PT.PT_ADDR2 should be updated to " + newPTAdd2);
        Assert.assertEquals(ptInfoList.get(3), newPTZipId, "        PT.FK_PT_ZIP_ID should be updated to " + newPTZipId);
        Assert.assertEquals(ptInfoList.get(4), newPTPhone, "        PT.PT_HM_PHN should be updated to " + newPTPhone);

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are NOT updated in PT_PYR");
        String dos = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb).get(4);
        dos = timeStamp.getConvertedDate("yyyy-MM-dd HH:mm:SS", "MM/dd/yyyy", dos);
        List<String> ptPyrInfoList = daoManagerXifinRpm.getPtPyrInfoFromPTPYRByPtSeqIdPyrIdDOS(ptSeqId, pyrId, dos, testDb);
        Assert.assertNotEquals(ptPyrInfoList.get(0), newPTAdd1, "        PT_PYR.INS_ADDR1 should not be updated to " + newPTAdd1);
        Assert.assertNotEquals(ptPyrInfoList.get(1), newPTAdd2, "        PT_PYR.INS_ADDR2 should not be updated to " + newPTAdd2);
        Assert.assertNotEquals(ptPyrInfoList.get(3), newPTZipId, "        PT_PYR.FK_INS_ZIP_ID should not be updated to " + newPTZipId);
        Assert.assertNotEquals(ptPyrInfoList.get(4), newPTPhone, "        PT_PYR.INS_HM_PHN should not be updated to " + newPTPhone);

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are NOT updated in ACCN_PYR");
        List<String> accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, pyrId, "1", testDb);
        Assert.assertNotEquals(accnPyrInfoList.get(0), newPTAdd1, "        ACCN_PYR.INS_ADDR1 should not be updated to " + newPTAdd1);
        Assert.assertNotEquals(accnPyrInfoList.get(1), newPTAdd2, "        ACCN_PYR.INS_ADDR2 should not be updated to " + newPTAdd2);
        Assert.assertNotEquals(accnPyrInfoList.get(2), newPTZipId, "        ACCN_PYR.FK_INS_ZIP_ID should not be updated to " + newPTZipId);
        Assert.assertNotEquals(accnPyrInfoList.get(3), newPTPhone, "        ACCN_PYR.INS_HM_PHN should not be updated to " + newPTPhone);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
//		daoManagerXifinRpm.delAccnIdFromQVALIDATEACCNDELByAccnId(accnId,currDt, testDb);

        daoManagerPlatform.setValuesFromTableByColNameValue("ACCN", "PT_ADDR1 = '" + oldPTAdd1 + "', FK_PT_ZIP_ID = '" + oldPTZIpID + "'", "pk_accn_id ", accnId, testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("PT", "PT_ADDR1 = '" + oldPTAdd1 + "', FK_PT_ZIP_ID = '" + oldPTZIpID + "'", "pk_seq_id ", ptSeqId, testDb);
    }

    @Test(priority = 1, description = "TEVIX-Non-Patient Pyr, Relshp is Self, has PT and PT_PYR")
    @Parameters({"formatType","accnId"})
    public void testPFER_239(String formatType, String accnId) throws Exception {
        logger.info("====== Testing - testPFER_239 =====");

        logger.info("*** Step 1 Actions: Create Tevix document with valid format");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);

        fileManipulation = new FileManipulation(driver);

//        List<String> oldDataInDB = daoManagerXifinRpm.getNonPtSelfAccnHasPtPyr(testDb);
//        String accnId = oldDataInDB.get(0);
        Accn accn =accessionDao.getAccn(accnId);
        List<AccnPyr> accnPyr=accessionDao.getAccnPyrByAccnId(accnId);
        String oldPTAdd1 = accn.getPtAddr1();
        String oldPTZIpID = accn.getPtZipId();
        String pyrId = String.valueOf(accnPyr.get(0).getPyrId());

        String newPTAdd1 = randomCharacter.getRandomAlphaString(6);
        String newPTAdd2 = randomCharacter.getRandomAlphaString(6);
        ArrayList<String> zipInfoList = daoManagerXifinRpm.getZipCodeFromZIP(testDb);
        String newPtCity = zipInfoList.get(1);
        String newPtStId = zipInfoList.get(2);
        String newPTZipId = zipInfoList.get(0);
        String newPTPhone = "619" + randomCharacter.getNonZeroRandomNumericString(7);
        String textInput = importEngineUtils.createDataForTevixFile(accnId, oldPTAdd1, oldPTZIpID, newPTAdd1, newPTAdd2, newPtCity, newPtStId, newPTZipId, newPTPhone);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathErrored + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are updated in ACCN table");
        List<String> updatedAccnInfoList = daoManagerXifinRpm.getAddrFromACCNByAccnID(accnId, testDb);
        Assert.assertEquals(updatedAccnInfoList.get(0), newPTAdd1, "        ACCN.PT_ADDR1 should be updated to " + newPTAdd1);
        Assert.assertEquals(updatedAccnInfoList.get(1), newPTAdd2, "        ACCN.PT_ADDR2 should be updated to " + newPTAdd2);
        Assert.assertEquals(updatedAccnInfoList.get(4), newPTZipId, "        ACCN.FK_PT_ZIP_ID should be updated to " + newPTZipId);
        Assert.assertEquals(updatedAccnInfoList.get(5), newPTPhone, "        ACCN.PT_HM_PHN should be updated to " + newPTPhone);

//        logger.info("*** Step 2 Expected Result: Verify that a new record is added into Q_VALIDATE_ACCN table");
//        String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");
//        Assert.assertEquals(daoManagerXifinRpm.getAccnIdFromQVALIDATEACCNDELByAccnId(accnId, currDt, testDb), accnId, "        Accession ID = " + accnId + "should be added in Q_Validate_ACCN table.");

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are updated in PT");
        String ptSeqId = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb).get(43);
        List<String> ptInfoList = daoManagerXifinRpm.getPtInfoFromPTBySeqId(ptSeqId, testDb);
        Assert.assertEquals(ptInfoList.get(0), newPTAdd1, "        PT.PT_ADDR1 should be updated to " + newPTAdd1);
        Assert.assertEquals(ptInfoList.get(1), newPTAdd2, "        PT.PT_ADDR2 should be updated to " + newPTAdd2);
        Assert.assertEquals(ptInfoList.get(3), newPTZipId, "        PT.FK_PT_ZIP_ID should be updated to " + newPTZipId);
        Assert.assertEquals(ptInfoList.get(4), newPTPhone, "        PT.PT_HM_PHN should be updated to " + newPTPhone);

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are NOT updated in PT_PYR");
        String dos = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb).get(4);
        dos = timeStamp.getConvertedDate("yyyy-MM-dd HH:mm:SS", "MM/dd/yyyy", dos);
        List<String> ptPyrInfoList = daoManagerXifinRpm.getPtPyrInfoFromPTPYRByPtSeqIdPyrIdDOS(ptSeqId, pyrId, dos, testDb);
        Assert.assertNotEquals(ptPyrInfoList.get(0), newPTAdd1, "        PT_PYR.INS_ADDR1 should not be updated to " + newPTAdd1);
        Assert.assertNotEquals(ptPyrInfoList.get(1), newPTAdd2, "        PT_PYR.INS_ADDR2 should not be updated to " + newPTAdd2);
        Assert.assertNotEquals(ptPyrInfoList.get(3), newPTZipId, "        PT_PYR.FK_INS_ZIP_ID should not be updated to " + newPTZipId);
        Assert.assertNotEquals(ptPyrInfoList.get(4), newPTPhone, "        PT_PYR.INS_HM_PHN should not be updated to " + newPTPhone);

        logger.info("*** Step 2 Expected Result: Verify that Patient Address and phone info are NOT updated in ACCN_PYR");
        List<String> accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, pyrId, "1", testDb);
        Assert.assertNotEquals(accnPyrInfoList.get(0), newPTAdd1, "        ACCN_PYR.INS_ADDR1 should not be updated to " + newPTAdd1);
        Assert.assertNotEquals(accnPyrInfoList.get(1), newPTAdd2, "        ACCN_PYR.INS_ADDR2 should not be updated to " + newPTAdd2);
        Assert.assertNotEquals(accnPyrInfoList.get(2), newPTZipId, "        ACCN_PYR.FK_INS_ZIP_ID should not be updated to " + newPTZipId);
        Assert.assertNotEquals(accnPyrInfoList.get(3), newPTPhone, "        ACCN_PYR.INS_HM_PHN should not be updated to " + newPTPhone);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
//		daoManagerXifinRpm.delAccnIdFromQVALIDATEACCNDELByAccnId(accnId,currDt, testDb);
    }

    @Test(priority = 1, description = "TEVIX-Non-existing AccnId")
    @Parameters({"formatType"})
    public void testPFER_181(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_181 =====");

        logger.info("*** Step 1 Actions: Create Tevix document with valid format and non-existing Accession ID");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);

        fileManipulation = new FileManipulation(driver);

        List<String> oldDataInDB = daoManagerXifinRpm.getNonPtSelfAccnHasPtPyr(testDb);
        String accnId = "NONEXISTINGACCNID";
        String oldPTAdd1 = oldDataInDB.get(1);
        String oldPTZIpID = oldDataInDB.get(3);

        String newPTAdd1 = randomCharacter.getRandomAlphaString(6);
        String newPTAdd2 = randomCharacter.getRandomAlphaString(6);
        ArrayList<String> zipInfoList = daoManagerXifinRpm.getZipCodeFromZIP(testDb);
        String newPtCity = zipInfoList.get(1);
        String newPtStId = zipInfoList.get(2);
        String newPTZipId = zipInfoList.get(0);
        String newPTPhone = "619" + randomCharacter.getNonZeroRandomNumericString(7);
        String textInput = importEngineUtils.createDataForTevixFile(accnId, oldPTAdd1, oldPTZIpID, newPTAdd1, newPTAdd2, newPtCity, newPtStId, newPTZipId, newPTPhone);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder and also generate an error file");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertTrue(isFileExists(fErrored, 5), "        Import file: " + fileName + ".error" + " should be generated in " + filePathErrored + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that proper error message was generated in the error file");
        String errFileStr = engineUtils.readFile(filePathErrored, fileName + ".error");
        Assert.assertTrue(errFileStr.contains(accnId), "        The error file should contains: " + accnId);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        fileManipulation.deleteFile(filePathErrored, fileName + ".error");
    }

    @Test(priority = 1, description = "TEVIX-Run import Engine with duplicate fileName")
    @Parameters({"formatType"})
    public void testPFER_241(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_241 =====");

        logger.info("*** Step 1 Actions: Create Tevix document with valid format and non-existing Accession ID");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);

        fileManipulation = new FileManipulation(driver);

        List<String> oldDataInDB = daoManagerXifinRpm.getNonPtSelfAccnHasPtPyr(testDb);
        String accnId = "NONEXISTINGACCNID";
        String oldPTAdd1 = oldDataInDB.get(1);
        String oldPTZIpID = oldDataInDB.get(3);

        String newPTAdd1 = randomCharacter.getRandomAlphaString(6);
        String newPTAdd2 = randomCharacter.getRandomAlphaString(6);
        ArrayList<String> zipInfoList = daoManagerXifinRpm.getZipCodeFromZIP(testDb);
        String newPtCity = zipInfoList.get(1);
        String newPtStId = zipInfoList.get(2);
        String newPTZipId = zipInfoList.get(0);
        String newPTPhone = "619" + randomCharacter.getNonZeroRandomNumericString(7);
        String textInput = importEngineUtils.createDataForTevixFile(accnId, oldPTAdd1, oldPTZIpID, newPTAdd1, newPTAdd2, newPtCity, newPtStId, newPTZipId, newPTPhone);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder and also generate an error file");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertTrue(isFileExists(fErrored, 5), "        Import file: " + fileName + ".error" + " should be generated in " + filePathErrored + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that proper error message was generated in the error file");
        String errFileStr = engineUtils.readFile(filePathErrored, fileName + ".error");
        Assert.assertTrue(errFileStr.contains(accnId), "        The error file should contains: " + accnId);

        logger.info("*** Step 3 Action: Push the duplicate file into the incoming folder");
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 3 Expected Result: Verify that the same file is generated in incoming folder");
        fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 4 Action: Run Platform Import Engine again");
        String filePathDup = dirBase + "dup" + File.separator;
        File fDup = new File(filePathDup + fileName);
        waitForImportEngine(fDup, isFileExists(fDup, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 4 Expected Result: Verify that the import file is moved to dup folder");
        Assert.assertTrue(isFileExists(fDup, 5), "        Import file: " + fileName + " should be generated in " + filePathDup + " folder.");

        logger.info("*** Step 5 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        fileManipulation.deleteFile(filePathErrored, fileName + ".error");
        fileManipulation.deleteFile(filePathDup, fileName);
    }

    @Test(priority = 1, description = "TEVIX-Fix Bad Address error")
    @Parameters({"formatType1", "formatType2"})
    public void testPFER_237(String formatType1, String formatType2) throws Exception {
        logger.info("====== Testing - testPFER_237 =====");

        logger.info("*** Step 1 Action: Create USPS document with Accession without Patient records and Accession Status is {21} and Ncoa Code is not 'M' or 'B'");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
        List<String> accnInfoList = daoManagerXifinRpm.getAccnHasAddrZipNoEPIFromACCNByAccnStatus(new int[]{21}, true, testDb);
        String accnId = accnInfoList.get(0);
        String oldPatientAddress = accnInfoList.get(1);
        String oldPatientZipID = accnInfoList.get(2);
        String newPatientAddr1 = "NEWADDR1" + randomCharacter.getRandomAlphaString(5);
        String newPatientAddr2 = "NEWADDR2" + randomCharacter.getRandomAlphaString(5);
        String newPatientCity = "San Diego";
        String newPatientStateId = "CA";
        String newPatientZipId = "92130";
        String NcoaCdType = randomCharacter.getRandomAlphaString(4);
        //Get a Ncoa code that is NOT M or B
        String NcoaCd = randomCharacter.getRandomAlphaString(1).replace("M", "A").replace("B", "C");
        String textInput = importEngineUtils.createDataForUSPSfile(accnId, oldPatientAddress, oldPatientZipID, newPatientAddr1, newPatientAddr2, newPatientCity, newPatientStateId, newPatientZipId, NcoaCdType, NcoaCd);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType1);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType1 + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected: Verify that Bad Address error is added to the ACCN_PYR_ERR table");
        String currDt = timeStamp.getCurrentDate("MM/dd/YYYY");
        //errCd = '2000' is Bad Address error code
        ArrayList<String> accnPyrErrInfoList = daoManagerPlatform.getAccnPyrErrInfoFromACCNPYRERRByAccnIdErrCdErrDt(accnId, "2000", currDt, testDb);
        int pyrPrio = Integer.parseInt(accnPyrErrInfoList.get(0));
        String ptPyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, pyrPrio, testDb);
        String pyrGrpName = daoManagerAccnWS.getPayorGroupNameByPayorId(ptPyrAbbrev, testDb);
        Assert.assertEquals(pyrGrpName, "Patient", "       Bad Address error should be added to the Patient Payor on the accession " + accnId);
        String fixDt = accnPyrErrInfoList.get(1);
        Assert.assertTrue(fixDt.isEmpty(), "       Bad Address error should NOT be fixed.");

        logger.info("*** Step 2 Expected: Verify that the accession was pushed into Q_EP_UNBILLABLE");
        String errDt = daoManagerPlatform.getInDtFromQEPUNBILLABLEByAccnId(accnId, testDb).get(0);
        Assert.assertEquals(errDt, currDt, "       The Accession ID: " + accnId + " should be in Q_EP_UNBILLABLE.");

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);

        logger.info("*** Step 4 Actions: Create Tevix document with valid format and the same Accession ID");
        String newPTAdd1 = randomCharacter.getRandomAlphaString(6);
        String newPTAdd2 = randomCharacter.getRandomAlphaString(6);
        ArrayList<String> zipInfoList = daoManagerXifinRpm.getZipCodeFromZIP(testDb);
        String newPtCity = zipInfoList.get(1);
        String newPtStId = zipInfoList.get(2);
        String newPTZipId = zipInfoList.get(0);
        String newPTPhone = "760" + randomCharacter.getNonZeroRandomNumericString(7);
        textInput = importEngineUtils.createDataForTevixFile(accnId, oldPatientAddress, oldPatientZipID, newPTAdd1, newPTAdd2, newPtCity, newPtStId, newPTZipId, newPTPhone);

        dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType2);
        currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = formatType2 + "_" + currentDateTime + ".txt";
        filePathIncoming = dirBase + "incoming" + File.separator;
        filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 4 Expected Result: Verify that the new file is generated in incoming folder");
        fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 5 Action: Run Platform Import Engine");
        fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 5 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathErrored + " folder.");

        logger.info("*** Step 5 Expected Result: Verify that Patient Address and phone info are updated in ACCN table");
        List<String> updatedAccnInfoList = daoManagerXifinRpm.getAddrFromACCNByAccnID(accnId, testDb);
        Assert.assertEquals(updatedAccnInfoList.get(0), newPTAdd1, "        ACCN.PT_ADDR1 should be updated to " + newPTAdd1);
        Assert.assertEquals(updatedAccnInfoList.get(1), newPTAdd2, "        ACCN.PT_ADDR2 should be updated to " + newPTAdd2);
        Assert.assertEquals(updatedAccnInfoList.get(4), newPTZipId, "        ACCN.FK_PT_ZIP_ID should be updated to " + newPTZipId);
        Assert.assertEquals(updatedAccnInfoList.get(5), newPTPhone, "        ACCN.PT_HM_PHN should be updated to " + newPTPhone);

        logger.info("*** Step 5 Expected Result:  Verify that the Bad Address error in ACCN_PYR_ERR table is fixed");
        String currentDate = timeStamp.getCurrentDate("MM/dd/yyyy");
        Assert.assertTrue(daoManagerXifinRpm.checkFixDtFromACCNPYRERRByAccnId(accnId, currentDate, "MM/dd/yyyy", testDb), "        Bad Address error on Accession ID = " + accnId + " should be fixed.");

        logger.info("*** Step 5 Expected Result: Verify that a new record is added into Q_VALIDATE_ACCN table");
//        Assert.assertEquals(daoManagerXifinRpm.getAccnIdFromQVALIDATEACCNDELByAccnId(accnId, currentDate, testDb), accnId, "        Accession ID = " + accnId + "should be added in Q_Validate_ACCN table.");

        logger.info("*** Step 6 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
    }

    @Test(priority = 1, description = "TEVIX-Fix PT_PYR_SUSPEND_REASON_DT")
    @Parameters({"formatType1", "formatType2"})
    public void testPFER_300(String formatType1, String formatType2) throws Exception {
        logger.info("====== Testing - testPFER_300 =====");

        logger.info("*** Step 1 Action: Create USPS document with Accession has Patient records and Accession Status is not {1,11,51,1010} and Ncoa Code is not 'M' or 'B'");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);

        //Setup Data
        //The accession status can't be Obsolete (62)
        List<String> accnInfoList = daoManagerXifinRpm.getAccnHasPtAddrZipFromACCNByAccnStatus(new int[]{1, 11, 51, 1010, 62, 31}, false, testDb);
        String accnId = accnInfoList.get(0);
        String oldPatientAddress = accnInfoList.get(1);
        String oldPatientZipID = accnInfoList.get(2);
        String newPatientAddr1 = "NEW ADDRESS INFORMATION UNKNOWN";
        String newPatientAddr2 = "NEWADDR2" + randomCharacter.getRandomAlphaString(5);
        String newPatientCity = "San Diego";
        String newPatientStateId = "CA";
        String newPatientZipId = "92129";
        String NcoaCdType = randomCharacter.getRandomAlphaString(4);
        //Get a Ncoa code that is NOT M or B
        String NcoaCd = randomCharacter.getRandomAlphaString(1).replace("M", "A").replace("B", "C");
        String textInput = importEngineUtils.createDataForUSPSfile(accnId, oldPatientAddress, oldPatientZipID, newPatientAddr1, newPatientAddr2, newPatientCity, newPatientStateId, newPatientZipId, NcoaCdType, NcoaCd);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType1);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType1 + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        String ptSeqId = accnInfoList.get(4);
//		daoManagerXifinRpm.deletePtDemoChkInfoFromPTDEMOCHKByPtSeqId(ptSeqId, testDb);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected: Verify that the Accession was being pushed into Q_VALIDATE_ACCN and PT_DEMO_CHK. And the PT_PYR got suspended.");
        String sysDt = timeStamp.getCurrentDate();
//        QValidateAccn qValidateAccnInfoList = accessionDao.getQValidateAccn(accnId);
//        Assert.assertNotNull(qValidateAccnInfoList, "       Accession ID " + accnId + " should be in Q_VALIDATE_ACCN.");

        ArrayList<String> ptDemoChkInfoList = daoManagerPlatform.getPtDemoChkInfoFromPTDEMOCHKByPtSeqId(ptSeqId, testDb);
        String createDt = ptDemoChkInfoList.get(2);
        Assert.assertEquals(createDt, sysDt, "        PT_DEMO_CHK.CREATE_DT should be " + sysDt + " for pt_seq_id = " + ptSeqId);
//		String audUser = ptDemoChkInfoList.get(5);
//		Assert.assertTrue(audUser.contains("engine~ImportEngine"),"       The PT_DEMO_CHK.AUD_USER should be  engine~ImportEngine.");

        ArrayList<String> ptPyrSuspInfoList = daoManagerPlatform.getPtPyrSuspInfoFromPTPYRSUSPENDREASONDTByPtSeqId(ptSeqId, testDb);
        String reasonDt = ptPyrSuspInfoList.get(3);
        Assert.assertEquals(reasonDt, sysDt, "        PT_PYR_SUSPEND_REASON_DT.REASON_DT should be " + sysDt + " for pt_seq_id = " + ptSeqId);
        Assert.assertTrue(ptPyrSuspInfoList.get(4).isEmpty(), "        PT_PYR_SUSPEND_REASON_DT.FIX_DT should be empty for pt_seq_id = " + ptSeqId);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);

        logger.info("*** Step 4 Actions: Create Tevix document with valid format and the same Accession ID");
        String newPTAdd1 = randomCharacter.getRandomAlphaString(6);
        String newPTAdd2 = randomCharacter.getRandomAlphaString(6);
        ArrayList<String> zipInfoList = daoManagerXifinRpm.getZipCodeFromZIP(testDb);
        String newPtCity = zipInfoList.get(1);
        String newPtStId = zipInfoList.get(2);
        String newPTZipId = zipInfoList.get(0);
        String newPTPhone = "760" + randomCharacter.getNonZeroRandomNumericString(7);
        textInput = importEngineUtils.createDataForTevixFile(accnId, oldPatientAddress, oldPatientZipID, newPTAdd1, newPTAdd2, newPtCity, newPtStId, newPTZipId, newPTPhone);

        dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType2);
        currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        fileName = formatType2 + "_" + currentDateTime + ".txt";
        filePathIncoming = dirBase + "incoming" + File.separator;
        filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 4 Expected Result: Verify that the new file is generated in incoming folder");
        fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 5 Action: Run Platform Import Engine");
        fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 5 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + " in " + filePathErrored + " folder.");

        logger.info("*** Step 5 Expected Result: Verify that Patient Address and phone info are updated in ACCN table");
        List<String> updatedAccnInfoList = daoManagerXifinRpm.getAddrFromACCNByAccnID(accnId, testDb);
        Assert.assertEquals(updatedAccnInfoList.get(0), newPTAdd1, "        ACCN.PT_ADDR1 should be updated to " + newPTAdd1);
        Assert.assertEquals(updatedAccnInfoList.get(1), newPTAdd2, "        ACCN.PT_ADDR2 should be updated to " + newPTAdd2);
        Assert.assertEquals(updatedAccnInfoList.get(4), newPTZipId, "        ACCN.FK_PT_ZIP_ID should be updated to " + newPTZipId);
        Assert.assertEquals(updatedAccnInfoList.get(5), newPTPhone, "        ACCN.PT_HM_PHN should be updated to " + newPTPhone);

        logger.info("*** Step 5 Expected Result: Verify that Patient Payor Suspend error should be fixed");
        ptPyrSuspInfoList = daoManagerPlatform.getPtPyrSuspInfoFromPTPYRSUSPENDREASONDTByPtSeqId(ptSeqId, testDb);
        Assert.assertEquals(ptPyrSuspInfoList.get(4), sysDt, "        PT_PYR_SUSPEND_REASON_DT.FIX_DT should be " + sysDt + " for pt_seq_id = " + ptSeqId);

        logger.info("*** Step 6 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
    }

    @Test(priority = 1, description = "DEFAULTREFUND-ClnRefund-Run import engine with valid document format")
    @Parameters({"formatType"})
    public void testPFER_255(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_255 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Generic Refund file with valid data");
        List<String> clnRefundInfoList = daoManagerXifinRpm.getApprovedPostedClnRefundFromCLNREFUND(testDb);
        String clnAbbrev = clnRefundInfoList.get(6);
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundAmt = "1";
        String chkClearDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundNote = "AUTOTEST " + randomCharacter.getRandomAlphaNumericString(8);
        String refundSeqId = clnRefundInfoList.get(0);
        String textInput = importEngineUtils.createDefaultRefundFile(clnAbbrev, refundAmt, refundDt, chkNum, chkClearDt, refundNote);

        clnRefundInfoList = daoManagerXifinRpm.getClnRefundInfoFromCLNREFUNDBySeqId(refundSeqId, testDb);
        int amtSent_Original = Integer.parseInt(clnRefundInfoList.get(0));
        int amtSent_New = amtSent_Original + Integer.parseInt(refundAmt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 2), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the data in CLN_REFUND table got updated properly ");
        clnRefundInfoList = daoManagerXifinRpm.getClnRefundInfoFromCLNREFUNDBySeqId(refundSeqId, testDb);
        Assert.assertTrue(clnRefundInfoList.get(1).contains(chkNum), "        CLN_REFUND.CHK_NUM should be updated to " + chkNum);
        Assert.assertEquals(clnRefundInfoList.get(2), refundDt, "        ACCN_REFUND.CHECK_DT should be " + refundDt);
        Assert.assertEquals(Integer.parseInt(clnRefundInfoList.get(0)), amtSent_New, "        CLN_REFUND.AMT_SENT should be " + amtSent_New);
        Assert.assertTrue(clnRefundInfoList.get(4).contains(refundNote), "        CLN_REFUND.NOTE should be updated to " + refundNote);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
//		daoManagerXifinRpm.updateValuesFromCLNREFUNDBySeqId(refundSeqId, testDb);
    }

    @Test(priority = 1, description = "DEFAULTREFUND-ClnRefund-Run import engine with identifier is not ClnId")
    @Parameters({"formatType"})
    public void testPFER_257(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_257 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Generic Refund file with Non-existing Client Abbrev");
        String clnAbbrev = "NONEXISTINGCLNABBREV";
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundAmt = "1";
        String chkClearDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundNote = "AUTOTEST " + randomCharacter.getRandomAlphaNumericString(8);
        String textInput = importEngineUtils.createDefaultRefundFile(clnAbbrev, refundAmt, refundDt, chkNum, chkClearDt, refundNote);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertTrue(isFileExists(fErrored, 2), "        Import file: " + fileName + ".error" + " in " + filePathErrored + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that proper error message was generated in the error file");
        String errFileStr = engineUtils.readFile(filePathErrored, fileName + ".error");
        Assert.assertTrue(errFileStr.contains("Errors: Refunds could not be identified for record Identifier: " + clnAbbrev), "        The error file should contains: Errors: Refunds could not be identified for record Identifier: " + clnAbbrev);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        fileManipulation.deleteFile(filePathErrored, fileName + ".error");
    }

    @Test(priority = 1, description = "DEFAULTREFUND-ClnRefund-Run import engine with B_posted = 0")
    @Parameters({"formatType"})
    public void testPFER_258(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_258 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Generic Refund file with Client Id in CLN_REFUND.B_POSTED = 0");
        List<String> clnRefundInfoList = daoManagerXifinRpm.getClnRefundNotPostedFromCLNREFUND(testDb);
        String clnAbbrev = clnRefundInfoList.get(6);
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundAmt = "1";
        String chkClearDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundNote = "AUTOTEST " + randomCharacter.getRandomAlphaNumericString(8);
        String textInput = importEngineUtils.createDefaultRefundFile(clnAbbrev, refundAmt, refundDt, chkNum, chkClearDt, refundNote);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertTrue(isFileExists(fErrored, 5), "        Import file: " + fileName + ".error" + " in " + filePathErrored + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that proper error message was generated in the error file");
        String errFileStr = engineUtils.readFile(filePathErrored, fileName + ".error");
        Assert.assertTrue(errFileStr.contains("Errors: Refunds could not be identified for record Identifier: " + clnAbbrev), "        The error file should contains: Errors: Refunds could not be identified for record Identifier: " + clnAbbrev);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        fileManipulation.deleteFile(filePathErrored, fileName + ".error");
    }

    @Test(priority = 1, description = "DEFAULTREFUND-ClnRefund-Leftover refund amount after applying to multiple cln refunds")
    @Parameters({"formatType"})
    public void testPFER_294(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_294 ======");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Generic Refund file with valid data");
        ArrayList<String> clnInfoList = daoManagerXifinRpm.getClnWApprovedPostedMultiRefundsFromCLNREFUND(testDb);
        String clnId = clnInfoList.get(0);
        String clnAbbrev = clnInfoList.get(1);

        List<List<String>> clnRefundInfoList = daoManagerXifinRpm.getClnRefundInfoFromCLNREFUNDByClnId(clnId, testDb);
        double amt = 0;
        for (List<String> strings : clnRefundInfoList) {
            amt = amt + Double.parseDouble(strings.get(1));
        }
        String refundAmt = String.valueOf(amt + Double.parseDouble("20"));
        String chkNum = randomCharacter.getNonZeroRandomNumericString(4);
        String refundDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String chkClearDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundNote = "AUTOTEST " + randomCharacter.getRandomAlphaNumericString(8);
        String textInput = importEngineUtils.createDefaultRefundFile(clnAbbrev, refundAmt, refundDt, chkNum, chkClearDt, refundNote);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder. Error file is also generated");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertTrue(isFileExists(fErrored, 2), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that data in CLN_REFUND are updated properly");
        clnRefundInfoList = daoManagerXifinRpm.getClnRefundInfoFromCLNREFUNDByClnId(clnId, testDb);

        for (List<String> strings : clnRefundInfoList) {
            Assert.assertEquals(strings.get(2), strings.get(1), "        CLN_REFUND.AMT_SENT should equal to CLN_REFUND.AMT_SENT.AMT for Client ID = " + clnId);
            Assert.assertEquals(strings.get(3), chkNum, "        CLN_REFUND.chkNum should be " + chkNum);
            Assert.assertEquals(strings.get(4), chkClearDt, "        CLN_REFUND.chkNum should be " + chkClearDt);
            Assert.assertEquals(strings.get(5), refundNote, "        CLN_REFUND.note should be " + refundNote);
        }

        logger.info("*** Step 2 Expected Result: Verify that proper error message was generated in the error file");
        String errFileStr = engineUtils.readFile(filePathErrored, fileName + ".error");
        String leftOverAmt = String.format("%.2f", (Double.parseDouble(refundAmt) - amt));
        Assert.assertTrue(errFileStr.contains("After processing all qualifying refunds, $" + leftOverAmt), "         Money leftover on Error file should be " + leftOverAmt + " for Client" + clnAbbrev);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
//		for (List<String> strings : clnRefundInfoList) {
//			daoManagerXifinRpm.updateValuesFromCLNREFUNDBySeqId(strings.get(0), testDb);
//		}
    }

    @Test(priority = 1, description = "DEFAULTREFUND-PmtSusp-Run import engine with valid document format")
    @Parameters({"formatType"})
    public void testPFER_259(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_259 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Generic Refund file with valid data");
        List<String> pmtSuspInfoList = daoManagerXifinRpm.getPmtSuspApprovedPostedFromPMTSUSP(testDb);
        String accnId = pmtSuspInfoList.get(0);
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundAmt = "1";
        String chkClearDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundNote = "AUTOTEST " + randomCharacter.getRandomAlphaNumericString(8);
        String pmtSuspRefundSeqId = pmtSuspInfoList.get(7);
        String textInput = importEngineUtils.createDefaultRefundFile(accnId, refundAmt, refundDt, chkNum, chkClearDt, refundNote);

        List<String> pmtSuspRefundInfoList = daoManagerXifinRpm.getPmtSuspRefundInfoFromPMTSUSPREFUNDBySeqId(pmtSuspRefundSeqId, testDb);
        int amtSent_Original = Integer.parseInt(pmtSuspRefundInfoList.get(0));
        int amtSent_New = amtSent_Original + Integer.parseInt(refundAmt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 2), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the data in PMT_SUSP_REFUND table got updated properly ");
        pmtSuspRefundInfoList = daoManagerXifinRpm.getPmtSuspRefundInfoFromPMTSUSPREFUNDBySeqId(pmtSuspRefundSeqId, testDb);
        Assert.assertTrue(pmtSuspRefundInfoList.get(1).contains(chkNum), "        PMT_SUSP_REFUND.CHK_NUM should be updated to " + chkNum);
        Assert.assertEquals(pmtSuspRefundInfoList.get(2), refundDt, "        PMT_SUSP_REFUND.CHECK_DT should be " + refundDt);
        Assert.assertEquals(Integer.parseInt(pmtSuspRefundInfoList.get(0)), amtSent_New, "        PMT_SUSP_REFUND.AMT_SENT should be " + amtSent_New);
        Assert.assertTrue(pmtSuspRefundInfoList.get(5).contains(refundNote), "        PMT_SUSP_REFUND.NOTE should be updated to " + refundNote);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
//		daoManagerXifinRpm.updateValuesFromPMTSUSPREFUNDBySeqId(pmtSuspRefundSeqId, testDb);
    }

    @Test(priority = 1, description = "ALLINAREFUND-PmtSusp-Run import engine with valid document")
    @Parameters({"formatType"})
    public void testPFER_284(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_284 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Allina Refund file with valid dataa");
        List<String> pmtSuspInfoList = daoManagerXifinRpm.getPmtSuspApprovedPostedFromPMTSUSP(testDb);
        String accnId = pmtSuspInfoList.get(0);
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundAmt = "1";
        String pmtSuspRefundSeqId = pmtSuspInfoList.get(7);
        String refundType = "S"; // S = Suspense Payment
        String textInput = importEngineUtils.createAllinaRefundFile(accnId, chkNum, refundDt, refundAmt, pmtSuspRefundSeqId, refundType);

        List<String> pmtSuspRefundInfoList = daoManagerXifinRpm.getPmtSuspRefundInfoFromPMTSUSPREFUNDBySeqId(pmtSuspRefundSeqId, testDb);
        int amtSent_Original = Integer.parseInt(pmtSuspRefundInfoList.get(0));
        int amtSent_New = amtSent_Original + Integer.parseInt(refundAmt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 2), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the data in PMT_SUSP_REFUND table got updated properly ");
        pmtSuspRefundInfoList = daoManagerXifinRpm.getPmtSuspRefundInfoFromPMTSUSPREFUNDBySeqId(pmtSuspRefundSeqId, testDb);
        Assert.assertTrue(pmtSuspRefundInfoList.get(1).contains(chkNum), "        PMT_SUSP_REFUND.CHK_NUM should be updated to " + chkNum);
        Assert.assertEquals(pmtSuspRefundInfoList.get(2), refundDt, "        PMT_SUSP_REFUND.CHECK_DT should be " + refundDt);
        Assert.assertEquals(Integer.parseInt(pmtSuspRefundInfoList.get(0)), amtSent_New, "        PMT_SUSP_REFUND.AMT_SENT should be " + amtSent_New);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
//		daoManagerXifinRpm.updateValuesFromPMTSUSPREFUNDBySeqId(pmtSuspRefundSeqId, testDb);
    }

    @Test(priority = 1, description = "ALLINAREFUND-ClnRefund-run import engine with valid document")
    @Parameters({"formatType"})
    public void testPFER_247(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_247 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Allina Refund file with valid data");
        List<String> clnRefundInfoList = daoManagerXifinRpm.getApprovedPostedClnRefundFromCLNREFUND(testDb);
        String clnAbbrev = clnRefundInfoList.get(6);
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundAmt = "1";
        String refundSeqId = clnRefundInfoList.get(0);
        String refundType = "C"; // C = Client
        String textInput = importEngineUtils.createAllinaRefundFile(clnAbbrev, chkNum, refundDt, refundAmt, refundSeqId, refundType);

        clnRefundInfoList = daoManagerXifinRpm.getClnRefundInfoFromCLNREFUNDBySeqId(refundSeqId, testDb);
        int amtSent_Original = Integer.parseInt(clnRefundInfoList.get(0));
        int amtSent_New = amtSent_Original + Integer.parseInt(refundAmt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 2), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the data in CLN_REFUND table got updated properly ");
        clnRefundInfoList = daoManagerXifinRpm.getClnRefundInfoFromCLNREFUNDBySeqId(refundSeqId, testDb);
        Assert.assertTrue(clnRefundInfoList.get(1).contains(chkNum), "        CLN_REFUND.CHK_NUM should be updated to " + chkNum);
        Assert.assertEquals(clnRefundInfoList.get(2), refundDt, "        ACCN_REFUND.CHECK_DT should be " + refundDt);
        Assert.assertEquals(Integer.parseInt(clnRefundInfoList.get(0)), amtSent_New, "        CLN_REFUND.AMT_SENT should be " + amtSent_New);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
//		daoManagerXifinRpm.updateValuesFromCLNREFUNDBySeqId(refundSeqId, testDb);
    }

    @Test(priority = 1, description = "DLSTREFUND-AccnRefund-Run import engine with valid document format")
    @Parameters({"formatType", "refundSeqId"})
    public void testPFER_263(String formatType, String refundSeqId) throws Exception {
        logger.info("====== Testing - testPFER_263 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create DLS Accn Refund file with valid data");
        String accnId = accessionDao.getAccnRefundBySeqId(Integer.parseInt(refundSeqId)).getAccnId();
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getFutureDate("MM/dd/yyyy", 2);
        String refundAmt = "1";
        String chkClearDt = "";
        String refundNote = "AUTOTEST " + randomCharacter.getRandomAlphaNumericString(8);
        String randomNumber = randomCharacter.getRandomNumericString(3);
        String textInput = importEngineUtils.createDLSAccnRefundFile(accnId, refundAmt, refundDt, chkNum, chkClearDt, refundNote, randomNumber);

        List<String> accnRefundInfoList = daoManagerXifinRpm.getAccnRefundInfofromACCNREFUNDByAccnIDSeqId(accnId, refundSeqId, testDb);
        int amtSent_Original = Integer.parseInt(accnRefundInfoList.get(3));
        int amtSent_New = amtSent_Original + Integer.parseInt(refundAmt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 2), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the data in ACCN_REFUND table got updated properly ");
        accnRefundInfoList = daoManagerXifinRpm.getAccnRefundInfofromACCNREFUNDByAccnIDSeqId(accnId, refundSeqId, testDb);
        Assert.assertTrue(accnRefundInfoList.get(1).contains(chkNum), "        ACCN_REFUND.CHK_NUM should be updated to " + chkNum);
        Assert.assertEquals(accnRefundInfoList.get(2), refundDt, "        ACCN_REFUND.CHECK_DT should be " + refundDt);
        Assert.assertEquals(Integer.parseInt(accnRefundInfoList.get(3)), amtSent_New, "        ACCN_REFUND.AMT_SENT should be " + amtSent_New);
        Assert.assertTrue(accnRefundInfoList.get(4).contains(refundNote), "        ACCN_REFUND.NOTE should be updated to " + refundNote);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        daoManagerXifinRpm.updateValuesFromACCNREFUNDByAccnIdSeqId(accnId, refundSeqId, testDb);
    }

    @Test(priority = 1, description = "DLSREFUND-ClnRefund-Run import engine with valid document format")
    @Parameters({"formatType"})
    public void testPFER_267(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_267 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create DLS Client Refund file with valid data");
        List<String> clnRefundInfoList = daoManagerXifinRpm.getApprovedPostedClnRefundFromCLNREFUND(testDb);
        String clnAbbrev = clnRefundInfoList.get(6);
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundAmt = "1";
        String chkClearDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundNote = "AUTOTEST " + randomCharacter.getRandomAlphaNumericString(8);
        String refundSeqId = clnRefundInfoList.get(0);
        String randomNumber = randomCharacter.getRandomNumericString(3);
        String acctingDt = clnRefundInfoList.get(8);
        String textInput = importEngineUtils.createDLSClnRefundFile(clnAbbrev, refundAmt, refundDt, chkNum, chkClearDt, refundNote, randomNumber, acctingDt);

        clnRefundInfoList = daoManagerXifinRpm.getClnRefundInfoFromCLNREFUNDBySeqId(refundSeqId, testDb);
        int amtSent_Original = Integer.parseInt(clnRefundInfoList.get(0));
        int amtSent_New = amtSent_Original + Integer.parseInt(refundAmt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertFalse(isFileExists(fErrored, 5), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the data in CLN_REFUND table got updated properly ");
        clnRefundInfoList = daoManagerXifinRpm.getClnRefundInfoFromCLNREFUNDBySeqId(refundSeqId, testDb);
        Assert.assertTrue(clnRefundInfoList.get(1).contains(chkNum), "        CLN_REFUND.CHK_NUM should be updated to " + chkNum);
        Assert.assertEquals(clnRefundInfoList.get(2), refundDt, "        ACCN_REFUND.CHECK_DT should be " + refundDt);
        Assert.assertEquals(Integer.parseInt(clnRefundInfoList.get(0)), amtSent_New, "        CLN_REFUND.AMT_SENT should be " + amtSent_New);
        Assert.assertTrue(clnRefundInfoList.get(4).contains(refundNote), "        CLN_REFUND.NOTE should be updated to " + refundNote);

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
//		daoManagerXifinRpm.updateValuesFromCLNREFUNDBySeqId(refundSeqId, testDb);
    }

    @Test(priority = 1, description = "DLSREFUND-ClnRefund-Run import engine with invalid ClnAbbrev")
    @Parameters({"formatType"})
    public void testPFER_269(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_269 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create DLS Client Refund file with invalid Client Abbrev");
        List<String> clnRefundInfoList = daoManagerXifinRpm.getApprovedPostedClnRefundFromCLNREFUND(testDb);
        String clnAbbrev = "NONEXISTINGCLN";//clnRefundInfoList.get(6);
        String chkNum = randomCharacter.getNonZeroRandomNumericString(5);
        String refundDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundAmt = "1";
        String chkClearDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        String refundNote = "AUTOTEST " + randomCharacter.getRandomAlphaNumericString(8);
        String randomNumber = randomCharacter.getRandomNumericString(3);
        String acctingDt = clnRefundInfoList.get(8);
        String textInput = importEngineUtils.createDLSClnRefundFile(clnAbbrev, refundAmt, refundDt, chkNum, chkClearDt, refundNote, randomNumber, acctingDt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = formatType + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, false, QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder and also generated an error file");
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertTrue(isFileExists(fErrored, 5), "        Import file: " + fileName + ".error" + " in " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that proper error message was generated in the error file");
        String errFileStr = engineUtils.readFile(filePathErrored, fileName + ".error");
        Assert.assertTrue(errFileStr.contains("Unable to find refund for record: Identifier: " + clnAbbrev + acctingDt.replaceAll("/", "")), "        The error file should contains: Unable to find refund for record: Identifier: " + clnAbbrev + acctingDt.replaceAll("/", ""));

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        fileManipulation.deleteFile(filePathErrored, fileName);
    }

    @Test(priority = 1, description = "DIAGNOSIS-Run import engine with engineConfidences is Confident")
    @Parameters({"formatType"})
    public void testPFER_209(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_209 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Diagnosis CodeRyte txt document with engineConfidences is Confident");
        String fileCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileNameAgncyName = randomCharacter.getRandomAlphaString(4);
        String fileNameCustomerName = randomCharacter.getRandomAlphaString(4);
        String fileNameCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileInfo = fileNameAgncyName + "_" + fileNameCustomerName + "_" + fileNameCreateDateTime;
        String fileID = randomCharacter.getRandomNumericString(5);
        String customerName = randomCharacter.getRandomAlphaNumericString(5);

        List<List<String>> accnInfo = daoManagerXifinRpm.getAccnWithNarrativeDiagFromACCNDIAG(testDb);
        String accnID = accnInfo.get(0).get(0);
        String accnDOS = timeStamp.getConvertedDate("MM/dd/yyyy", "yyyyMMdd", accnInfo.get(0).get(1));
        String accnPTDOB = timeStamp.getCurrentDate("yyyyMMdd");
        String accnPTGender = "Female";
        String diagDescr = accnInfo.get(0).get(2);
        String diagTypId = accnInfo.get(0).get(3);
        String assignedDiagCd = daoManagerXifinRpm.getDiagIdFromDIAGCDByDiagTyp(diagTypId, testDb).get(0);
        String segmentCnt = randomCharacter.getRandomNumericString(2);
        String textInput = importEngineUtils.createValidDiagnosisFile("FHS", fileCreateDateTime, fileInfo, fileID, customerName, "ACCN", accnID, accnDOS, accnPTDOB, accnPTGender, "DIAG", diagDescr, assignedDiagCd, ENGINE_CONFIDENCE_CONFIDENT, "FTS", segmentCnt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "CodeRyte" + "_" + fileNameCustomerName + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the narrative diagnosis code is voided and the new diagnosis code from the import file got added to the accn_diag");
        String cmnt = "Org Value: " + diagDescr;
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDCmnt(accnID, cmnt, testDb);
        List<String> accnDiagDel = daoManagerXifinRpm.getAccnFromACCNDIAGDELByAccnID(accnID, diagDescr, testDb);

        Assert.assertTrue(accnDiag.size() > 0, "        Diagnosis Code: " + assignedDiagCd + " should be added to Accn_Diag table.");
        Assert.assertTrue(accnDiagDel.size() > 0, "        Narrative Diagnosis Code: " + diagDescr + " should be moved to Accn_Diag_Del table.");

        logger.info("*** Step 2 Expected Result: Verify that the Accession should be in the q_validate_accn queue");
        Assert.assertEquals(daoManagerXifinRpm.getAccnIdFromQVALIDATEACCNByAccnId(accnID, testDb), accnID, "        Accession: " + accnID + " should be in q_validate_accn.");

        logger.info("*** Step 3 Action: Clear test data");
//		daoManagerXifinRpm.updateACCNDIAGByAccnIdCmnt(accnID, diagDescr, cmnt, testDb);
//		daoManagerXifinRpm.delAccnIdFromQVALIDATEACCNDELByAccnId(accnID, currDt,testDb);
//		daoManagerXifinRpm.deleteFromACCNDIAGDELByAccnIdDescr(accnID, diagDescr, testDb);
        fileManipulation.deleteFile(filePathProcessed, fileName);
    }

    @Test(priority = 1, description = "DIAGNOSIS-Run import engine with engineConfidences is QC")
    @Parameters({"formatType"})
    public void testPFER_210(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_210 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Diagnosis CodeRyte txt document with engineConfidences is Confident");
        String fileCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileNameAgncyName = randomCharacter.getRandomAlphaString(4);
        String fileNameCustomerName = randomCharacter.getRandomAlphaString(4);
        String fileNameCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileInfo = fileNameAgncyName + "_" + fileNameCustomerName + "_" + fileNameCreateDateTime;
        String fileID = randomCharacter.getRandomNumericString(5);
        String customerName = randomCharacter.getRandomAlphaNumericString(5);

        List<List<String>> accnInfo = daoManagerXifinRpm.getAccnWithNarrativeDiagFromACCNDIAG(testDb);
        String accnID = accnInfo.get(0).get(0);
        String accnDOS = timeStamp.getConvertedDate("MM/dd/yyyy", "yyyyMMdd", accnInfo.get(0).get(1));
        String accnPTDOB = timeStamp.getCurrentDate("yyyyMMdd");
        String accnPTGender = "Female";
        String diagDescr = accnInfo.get(0).get(2);
        String diagTypId = accnInfo.get(0).get(3);
        String assignedDiagCd = daoManagerXifinRpm.getDiagIdFromDIAGCDByDiagTyp(diagTypId, testDb).get(0);
        String segmentCnt = randomCharacter.getRandomNumericString(2);
        String diagSeqId = accnInfo.get(0).get(4);
        String textInput = importEngineUtils.createValidDiagnosisFile("FHS", fileCreateDateTime, fileInfo, fileID, customerName, "ACCN", accnID, accnDOS, accnPTDOB, accnPTGender, "DIAG", diagDescr, assignedDiagCd, ENGINE_CONFIDENCE_QC, "FTS", segmentCnt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "CodeRyte" + "_" + fileNameCustomerName + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the narrative diagnosis code is kept in accn_diag and the new diagnosis code from the import file got added to the accn_diag");
        String cmnt = "Org Value: " + diagDescr;
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDCmnt(accnID, cmnt, testDb);
        List<String> accnDiagInfoList = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnID, diagSeqId, testDb);

        Assert.assertTrue(accnDiag.size() > 0, "        Diagnosis Code: " + assignedDiagCd + " should be added to Accn_Diag table.");
        Assert.assertTrue(accnDiagInfoList.size() > 0, "        Narrative Diagnosis Code: " + diagDescr + " should stay in Accn_Diag table.");

        logger.info("*** Step 2 Expected Result: Verify that DXQC error is added to the accn_pyr_err table");
        //dnl table id = '-2' Unbillable error
        String errCD = daoManagerXifinRpm.getErrCdFromERRCDByAbbrvDateDnlTableId(IMPORT_DIAG_DXQC, timeStamp.getConvertedDate("yyyyMMdd", "MM/dd/yyyy", accnDOS), "-2", testDb);
        String accnPyrErr = daoManagerXifinRpm.getAccnPyrErrByAccnIdAndErrCd(accnID, errCD, testDb);
        Assert.assertTrue(accnPyrErr.length() > 0, "        " + IMPORT_DIAG_DXQC + " unbillable error should be added to Accn_Pyr_Err table for Accession " + accnID);

        logger.info("*** Step 2 Expected Result: Verify that the Accession should be in the q_validate_accn queue");
        String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        Assert.assertEquals(daoManagerXifinRpm.getAccnIdFromQVALIDATEACCNDELByAccnId(accnID, currDt, testDb), accnID, "        Accession: " + accnID + " should be in q_validate_accn.");

        logger.info("*** Step 3 Action: Clear test data");
        //daoManagerXifinRpm.updateACCNDIAGByAccnIdCmnt(accnID, diagDescr, cmnt, testDb);
//		daoManagerXifinRpm.delAccnIdFromQVALIDATEACCNDELByAccnId(accnID,currDt, testDb);
//		daoManagerXifinRpm.deleteFromACCNDIAGByAccnIdCmnt(accnID, cmnt, testDb);
//		daoManagerXifinRpm.deleteDataFromAccnPyrErrByAccnIDAndErrCD(accnID,errCD,testDb);
        fileManipulation.deleteFile(filePathProcessed, fileName);

    }

    @Test(priority = 1, description = "DIAGNOSIS-Run import engine with engineConfidences is Code")
    @Parameters({"formatType"})
    public void testPFER_211(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_211 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Diagnosis CodeRyte txt document with engineConfidences is Code");
        String fileCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileNameAgncyName = randomCharacter.getRandomAlphaString(4);
        String fileNameCustomerName = randomCharacter.getRandomAlphaString(4);
        String fileNameCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileInfo = fileNameAgncyName + "_" + fileNameCustomerName + "_" + fileNameCreateDateTime;
        String fileID = randomCharacter.getRandomNumericString(5);
        String customerName = randomCharacter.getRandomAlphaNumericString(5);

        List<List<String>> accnInfo = daoManagerXifinRpm.getAccnWithNarrativeDiagFromACCNDIAG(testDb);
        String accnID = accnInfo.get(0).get(0);
        String accnDOS = timeStamp.getConvertedDate("MM/dd/yyyy", "yyyyMMdd", accnInfo.get(0).get(1));
        String accnPTDOB = timeStamp.getCurrentDate("yyyyMMdd");
        String accnPTGender = "Female";
        String diagDescr = accnInfo.get(0).get(2);
        String diagTypId = accnInfo.get(0).get(3);
        String assignedDiagCd = daoManagerXifinRpm.getDiagIdFromDIAGCDByDiagTyp(diagTypId, testDb).get(0);
        String segmentCnt = randomCharacter.getRandomNumericString(2);
        String diagSeqId = accnInfo.get(0).get(4);
        String textInput = importEngineUtils.createValidDiagnosisFile("FHS", fileCreateDateTime, fileInfo, fileID, customerName, "ACCN", accnID, accnDOS, accnPTDOB, accnPTGender, "DIAG", diagDescr, assignedDiagCd, ENGINE_CONFIDENCE_CODE, "FTS", segmentCnt);

        //dnl table id = '-2' Unbillable error
        String errCD = daoManagerXifinRpm.getErrCdFromERRCDByAbbrvDateDnlTableId(IMPORT_DIAG_DXCODE, timeStamp.getConvertedDate("yyyyMMdd", "MM/dd/yyyy", accnDOS), "-2", testDb);
//		daoManagerXifinRpm.deleteDataFromAccnPyrErrByAccnIDAndErrCD(accnID,errCD,testDb);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "CodeRyte" + "_" + fileNameCustomerName + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the narrative diagnosis code is kept in accn_diag and the new diagnosis code from the import file got added to the accn_diag");
        String cmnt = "Org Value: " + diagDescr;
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDCmnt(accnID, cmnt, testDb);
        List<String> accnDiagInfoList = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnID, diagSeqId, testDb);

        Assert.assertTrue(accnDiag.size() > 0, "        Diagnosis Code: " + assignedDiagCd + " should be added to Accn_Diag table.");
        Assert.assertTrue(accnDiagInfoList.size() > 0, "        Narrative Diagnosis Code: " + diagDescr + " should stay in Accn_Diag table.");

        logger.info("*** Step 2 Expected Result: Verify that DXCODE error is added to the accn_pyr_err table");
        //dnl table id = '-2' Unbillable error
        String accnPyrErr = daoManagerXifinRpm.getAccnPyrErrByAccnIdAndErrCd(accnID, errCD, testDb);
        Assert.assertTrue(accnPyrErr.length() > 0, "        " + IMPORT_DIAG_DXCODE + " unbillable error should be added to Accn_Pyr_Err table for Accession " + accnID);

        logger.info("*** Step 2 Expected Result: Verify that the Accession should be in the q_validate_accn queue");
        String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        Assert.assertEquals(daoManagerXifinRpm.getAccnIdFromQVALIDATEACCNDELByAccnId(accnID, currDt, testDb), accnID, "        Accession: " + accnID + " should be in q_validate_accn.");

        logger.info("*** Step 3 Action: Clear test data");
        //daoManagerXifinRpm.updateACCNDIAGByAccnIdCmnt(accnID, diagDescr, cmnt, testDb);
//		daoManagerXifinRpm.delAccnIdFromQVALIDATEACCNDELByAccnId(accnID,currDt, testDb);
//		daoManagerXifinRpm.deleteFromACCNDIAGByAccnIdCmnt(accnID, cmnt, testDb);
//		daoManagerXifinRpm.deleteDataFromAccnPyrErrByAccnIDAndErrCD(accnID,errCD,testDb);
        fileManipulation.deleteFile(filePathProcessed, fileName);
    }

    @Test(priority = 1, description = "DIAGNOSIS-Run import engine with engineConfidences is Review")
    @Parameters({"formatType"})
    public void testPFER_212(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_212 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Diagnosis CodeRyte txt document with engineConfidences is Review");
        String fileCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileNameAgncyName = randomCharacter.getRandomAlphaString(4);
        String fileNameCustomerName = randomCharacter.getRandomAlphaString(4);
        String fileNameCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileInfo = fileNameAgncyName + "_" + fileNameCustomerName + "_" + fileNameCreateDateTime;
        String fileID = randomCharacter.getRandomNumericString(5);
        String customerName = randomCharacter.getRandomAlphaNumericString(5);

        List<List<String>> accnInfo = daoManagerXifinRpm.getAccnWithNarrativeDiagFromACCNDIAG(testDb);
        String accnID = accnInfo.get(0).get(0);
        String accnDOS = timeStamp.getConvertedDate("MM/dd/yyyy", "yyyyMMdd", accnInfo.get(0).get(1));
        String accnPTDOB = timeStamp.getCurrentDate("yyyyMMdd");
        String accnPTGender = "Female";
        String diagDescr = accnInfo.get(0).get(2);
        String diagTypId = accnInfo.get(0).get(3);
        String assignedDiagCd = daoManagerXifinRpm.getDiagIdFromDIAGCDByDiagTyp(diagTypId, testDb).get(0);
        String segmentCnt = randomCharacter.getRandomNumericString(2);
        String diagSeqId = accnInfo.get(0).get(4);
        String textInput = importEngineUtils.createValidDiagnosisFile("FHS", fileCreateDateTime, fileInfo, fileID, customerName, "ACCN", accnID, accnDOS, accnPTDOB, accnPTGender, "DIAG", diagDescr, assignedDiagCd, ENGINE_CONFIDENCE_REVIEW, "FTS", segmentCnt);

        //dnl table id = '-2' Unbillable error
        String errCD = daoManagerXifinRpm.getErrCdFromERRCDByAbbrvDateDnlTableId(IMPORT_DIAG_DXREVIEW, timeStamp.getConvertedDate("yyyyMMdd", "MM/dd/yyyy", accnDOS), "-2", testDb);
//		daoManagerXifinRpm.deleteDataFromAccnPyrErrByAccnIDAndErrCD(accnID,errCD,testDb);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "CodeRyte" + "_" + fileNameCustomerName + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that the narrative diagnosis code is kept in accn_diag and the new diagnosis code from the import file got added to the accn_diag");
        String cmnt = "Org Value: " + diagDescr;
        List<String> accnDiag = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDCmnt(accnID, cmnt, testDb);
        List<String> accnDiagInfoList = daoManagerXifinRpm.getAccnDiagInfoFromACCNDIAGByAccnIDDiagSeq(accnID, diagSeqId, testDb);

        Assert.assertTrue(accnDiag.size() > 0, "        Diagnosis Code: " + assignedDiagCd + " should be added to Accn_Diag table.");
        Assert.assertTrue(accnDiagInfoList.size() > 0, "        Narrative Diagnosis Code: " + diagDescr + " should stay in Accn_Diag table.");

        logger.info("*** Step 2 Expected Result: Verify that DXREVIEW error is added to the accn_pyr_err table");
        //dnl table id = '-2' Unbillable error
        String accnPyrErr = daoManagerXifinRpm.getAccnPyrErrByAccnIdAndErrCd(accnID, errCD, testDb);
        Assert.assertTrue(accnPyrErr.length() > 0, "        " + IMPORT_DIAG_DXREVIEW + " unbillable error should be added to Accn_Pyr_Err table for Accession " + accnID);

        logger.info("*** Step 2 Expected Result: Verify that the Accession should be in the q_validate_accn queue");
        String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        Assert.assertEquals(daoManagerXifinRpm.getAccnIdFromQVALIDATEACCNDELByAccnId(accnID, currDt, testDb), accnID, "        Accession: " + accnID + " should be in q_validate_accn.");

        logger.info("*** Step 3 Action: Clear test data");
        //daoManagerXifinRpm.updateACCNDIAGByAccnIdCmnt(accnID, diagDescr, cmnt, testDb);
//		daoManagerXifinRpm.delAccnIdFromQVALIDATEACCNDELByAccnId(accnID,currDt, testDb);
//		daoManagerXifinRpm.deleteFromACCNDIAGByAccnIdCmnt(accnID, cmnt, testDb);
//		daoManagerXifinRpm.deleteDataFromAccnPyrErrByAccnIDAndErrCD(accnID,errCD,testDb);
        fileManipulation.deleteFile(filePathProcessed, fileName);
    }

    @Test(priority = 1, description = "DIAGNOSIS-EngineConfidence is Insufficient Doc, Engine Assigned DiagCd is blank")
    @Parameters({"formatType"})
    public void testPFER_214(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_214 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Diagnosis CodeRyte txt document with engineConfidences is Insufficient Doc and Engine Assigned Diagnosis Code is blank");
        String fileCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileNameAgncyName = randomCharacter.getRandomAlphaString(4);
        String fileNameCustomerName = randomCharacter.getRandomAlphaString(4);
        String fileNameCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileInfo = fileNameAgncyName + "_" + fileNameCustomerName + "_" + fileNameCreateDateTime;
        String fileID = randomCharacter.getRandomNumericString(5);
        String customerName = randomCharacter.getRandomAlphaNumericString(5);

        List<List<String>> accnInfo = daoManagerXifinRpm.getAccnWithNarrativeDiagFromACCNDIAG(testDb);
        String accnID = accnInfo.get(0).get(0);
        String accnDOS = timeStamp.getConvertedDate("MM/dd/yyyy", "yyyyMMdd", accnInfo.get(0).get(1));
        String accnPTDOB = timeStamp.getCurrentDate("yyyyMMdd");
        String accnPTGender = "Female";
        String diagDescr = accnInfo.get(0).get(2);
        String assignedDiagCd = "";
        String segmentCnt = randomCharacter.getRandomNumericString(2);
        String textInput = importEngineUtils.createValidDiagnosisFile("FHS", fileCreateDateTime, fileInfo, fileID, customerName, "ACCN", accnID, accnDOS, accnPTDOB, accnPTGender, "DIAG", diagDescr, assignedDiagCd, ENGINE_CONFIDENCE_INSUFFICIENT_DOC, "FTS", segmentCnt);

        String errCD = daoManagerXifinRpm.getErrCdFromERRCDByAbbrvDateDnlTableId(IMPORT_DIAG_DXINSUFDOC, timeStamp.getConvertedDate("yyyyMMdd", "MM/dd/yyyy", accnDOS), "-2", testDb);
//		daoManagerXifinRpm.deleteDataFromAccnPyrErrByAccnIDAndErrCD(accnID,errCD,testDb);

        //Remove Accn Diag (Non-narrative) from accn_diag table so the DXINSUFDOC accn pyr error can be generated later
//		daoManagerXifinRpm.deleteAccnDiagFromACCNDIAGByAccnIdDiagCd(accnID, "is not null", testDb);
//		daoManagerXifinRpm.deleteAccnDiagFromACCNDIAGDELByAccnId(accnID, testDb);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "CodeRyte" + "_" + fileNameCustomerName + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and moved to processed folder");
//		File fProcessed = new File(filePathProcessed + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");

        logger.info("*** Step 2 Expected Result: Verify that DXINSUFDOC error is added to the accn_pyr_err table");
        //dnl table id = '-2' Unbillable error
        String accnPyrErr = daoManagerXifinRpm.getAccnPyrErrByAccnIdAndErrCd(accnID, errCD, testDb);
        Assert.assertTrue(accnPyrErr.length() > 0, "        " + IMPORT_DIAG_DXINSUFDOC + " unbillable error should be added to Accn_Pyr_Err table for Accession " + accnID);

        logger.info("*** Step 2 Expected Result: Verify that the Accession should be in the q_validate_accn queue");
        String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        Assert.assertEquals(daoManagerXifinRpm.getAccnIdFromQVALIDATEACCNDELByAccnId(accnID, currDt, testDb), accnID, "        Accession: " + accnID + " should be in q_validate_accn.");

        logger.info("*** Step 3 Action: Clear test data");
//		daoManagerXifinRpm.delAccnIdFromQVALIDATEACCNDELByAccnId(accnID,currDt, testDb);
//		daoManagerXifinRpm.deleteDataFromAccnPyrErrByAccnIDAndErrCD(accnID,errCD,testDb);
        fileManipulation.deleteFile(filePathProcessed, fileName);
    }

    @Test(priority = 1, description = "DIAGNOSIS-Run import engine with Date of Service is empty")
    @Parameters({"formatType"})
    public void testPFER_222(String formatType) throws Exception {
        logger.info("====== Testing - testPFER_222 =====");
        randomCharacter = new RandomCharacter(driver);
        engineUtils = new EngineUtils(driver);
        timeStamp = new TimeStamp(driver);
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);


        logger.info("*** Step 1 Action: Create Diagnosis CodeRyte txt document with engineConfidences is Insufficient Doc and Engine Assigned Diagnosis Code is blank and DOS is empty");
        String fileCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileNameAgncyName = randomCharacter.getRandomAlphaString(4);
        String fileNameCustomerName = randomCharacter.getRandomAlphaString(4);
        String fileNameCreateDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmss");
        String fileInfo = fileNameAgncyName + "_" + fileNameCustomerName + "_" + fileNameCreateDateTime;
        String fileID = randomCharacter.getRandomNumericString(5);
        String customerName = randomCharacter.getRandomAlphaNumericString(5);

        List<List<String>> accnInfo = daoManagerXifinRpm.getAccnWithNarrativeDiagFromACCNDIAG(testDb);
        String accnID = accnInfo.get(0).get(0);
        String accnDOS = "";//accnInfo.get(0).get(1);
        String accnPTDOB = timeStamp.getCurrentDate("yyyyMMdd");
        String accnPTGender = "Female";
        String diagDescr = accnInfo.get(0).get(2);
        String assignedDiagCd = "";
        String segmentCnt = randomCharacter.getRandomNumericString(2);
        String textInput = importEngineUtils.createValidDiagnosisFile("FHS", fileCreateDateTime, fileInfo, fileID, customerName, "ACCN", accnID, accnDOS, accnPTDOB, accnPTGender, "DIAG", diagDescr, assignedDiagCd, ENGINE_CONFIDENCE_INSUFFICIENT_DOC, "FTS", segmentCnt);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String currentDateTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "CodeRyte" + "_" + fileNameCustomerName + "_" + currentDateTime + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        fileManipulation.writeFileToFolder(textInput, filePathIncoming, fileName);

        logger.info("*** Step 1 Expected Result: Verify that the new file is generated in incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        System.out.println("File Path = " + filePathIncoming + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fIncoming, 5), "        Import file: " + fileName + " should be generated under " + filePathIncoming + " folder.");

        logger.info("*** Step 2 Action: Run Platform Import Engine");
        File fProcessed = new File(filePathProcessed + fileName);
        waitForImportEngine(fProcessed, isFileExists(fProcessed, 5), QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 2 Expected Result: Verify that the import file got processed and an error file was generated in the errored folder");
//		File fProcessed = new File(filePathProcessed + fileName);
        File fErrored = new File(filePathErrored + fileName + ".error");
        Assert.assertTrue(isFileExists(fProcessed, 5), "        Import file: " + fileName + " should be moved to " + filePathProcessed + " folder.");
        Assert.assertTrue(isFileExists(fErrored, 5), "        Import file: " + fileName + ".error" + " in " + filePathErrored + " folder.");

        logger.info("*** Step 3 Action: Clear test data");
        fileManipulation.deleteFile(filePathProcessed, fileName);
        fileManipulation.deleteFile(filePathErrored, fileName + ".error");
    }

    @Test(priority = 1, description = "Hospital Admit - Patient Type Inpatient")
    @Parameters({"subId", "ssn", "dob", "admitDt", "dischargeDt", "ptLastName", "ptFirstName", "ptMi"})
    public void testHospitalAdmitPatientTypeInpatient(String subId, String ssn, String dob, String admitDt, @Optional String dischargeDt, String ptLastName, String ptFirstName, @Optional String ptMi) throws Exception
    {
        testHospitalAdmitPatientType(subId, ssn, dob, admitDt, dischargeDt, ptLastName, ptFirstName, ptMi, "I");
    }

    @Test(priority = 1, description = "Hospital Admit - Patient Type Outpatient")
    @Parameters({"subId", "ssn", "dob", "admitDt", "dischargeDt", "ptLastName", "ptFirstName", "ptMi"})
    public void testHospitalAdmitPatientTypeOutpatient(String subId, String ssn, String dob, String admitDt, @Optional String dischargeDt, String ptLastName, String ptFirstName, @Optional String ptMi) throws Exception
    {
        testHospitalAdmitPatientType(subId, ssn, dob, admitDt, dischargeDt, ptLastName, ptFirstName, ptMi, "O");
    }

    @Test(priority = 1, description = "Hospital Admit - Patient Type Non-patient")
    @Parameters({"subId", "ssn", "dob", "admitDt", "dischargeDt", "ptLastName", "ptFirstName", "ptMi"})
    public void testHospitalAdmitPatientTypeNonPatient(String subId, String ssn, String dob, String admitDt, @Optional String dischargeDt, String ptLastName, String ptFirstName, @Optional String ptMi) throws Exception
    {
        testHospitalAdmitPatientType(subId, ssn, dob, admitDt, dischargeDt, ptLastName, ptFirstName, ptMi, "N");
    }

    @Test(priority = 1, description = "Hospital Admit - Patient Type Invalid")
    @Parameters({"subId", "ssn", "dob", "admitDt", "dischargeDt", "ptLastName", "ptFirstName", "ptMi"})
    public void testHospitalAdmitPatientTypeInvalid(String subId, String ssn, String dob, String admitDt, @Optional String dischargeDt, String ptLastName, String ptFirstName, @Optional String ptMi) throws Exception
    {
        testHospitalAdmitPatientType(subId, ssn, dob, admitDt, dischargeDt, ptLastName, ptFirstName, ptMi, "Z");
    }

    private void testHospitalAdmitPatientType(String subId, String ssn, String dob, String admitDt, @Optional String dischargeDt, String ptLastName, String ptFirstName, @Optional String ptMi, String ptTyp) throws Exception
    {
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);
        timeStamp = new TimeStamp(driver);

        cleanUpHospitalAdmitData(subId);

        String formatType = "hospitalAdmit";
        String recordType = "A";
        logger.info("message=Creating hospital admit check content, recordType="+recordType+", subId="+subId+", ssn="+ssn+", dob="+dob+", admitDt="+admitDt+", dischargeDt="+dischargeDt+
                ", ptLastName="+ptLastName+", ptFirstName="+ptFirstName+", ptTyp="+ptTyp);
        String content = importEngineUtils.createDataForHospitalAdmitFile(recordType, subId, ptLastName, ptFirstName, ptMi, dob, admitDt, dischargeDt, ssn, null, ptTyp);
        logger.info("message=Created hospital admit check content, content="+content);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String fileName = formatType + "_" + timeStamp.getCurrentDate("yyyyMMddHHmmssSS") + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        logger.info("message=Creating hospital admit check file, incomingDir="+filePathIncoming+", filename="+fileName);
        fileManipulation.writeFileToFolder(content, filePathIncoming, fileName);

        File stagedFile = new File(filePathIncoming + fileName);
        Assert.assertTrue(isFileExists(stagedFile, 5), "Staged hospital admit check file does not exist, stagedFile="+stagedFile.getAbsolutePath());

        File processedFile = new File(filePathProcessed + fileName);
        waitForImportEngine(processedFile, isFileExists(processedFile, 5), QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(isFileExists(processedFile, 5), "Processed hospital admit check file does not exist, processedFile="+processedFile.getAbsolutePath());

        logger.info("message=Verifying hospital admit data, subId="+subId);
        List<HospitalAdmitCheck> hospitalAdmitChecks = rpmDao.getHospitalAdmitCheckBySubsId(null, subId);
        Assert.assertEquals(hospitalAdmitChecks.size(), 1, "Expected one hospital admit check record, subId="+subId);
        Assert.assertEquals(hospitalAdmitChecks.get(0).getSubsId(), subId, "Subscriber ID does not match");
        Assert.assertEquals(hospitalAdmitChecks.get(0).getSsn(), Integer.parseInt(ssn), "SSN does not match");
        Assert.assertEquals(HOSPITAL_ADMIT_DATE_FORMAT.format(hospitalAdmitChecks.get(0).getDob()), dob, "DOB does not match");
        Assert.assertEquals(HOSPITAL_ADMIT_DATE_FORMAT.format(hospitalAdmitChecks.get(0).getAdmitDt()), admitDt, "Admit Date does not match");
        if (StringUtils.isNotBlank(dischargeDt))
        {
            Assert.assertEquals(HOSPITAL_ADMIT_DATE_FORMAT.format(hospitalAdmitChecks.get(0).getDischargeDt()), dischargeDt, "Discharge Date does not match");
        }
        else
        {
            Assert.assertNull(hospitalAdmitChecks.get(0).getDischargeDt(), "Discharge Date does not match");
        }
        Assert.assertEquals(hospitalAdmitChecks.get(0).getPtLNm(), ptLastName, "Last Name does not match");
        String expectedFirstName = StringUtils.isNotBlank(ptMi) ? ptFirstName+","+ptMi : ptFirstName;
        Assert.assertEquals(hospitalAdmitChecks.get(0).getPtFNm(), expectedFirstName, "First Name does not match");
        Assert.assertNull(hospitalAdmitChecks.get(0).getXrefId(), "XREF ID should be null");
        Assert.assertNull(hospitalAdmitChecks.get(0).getAccnId(), "Accession ID should be null");
        Assert.assertNull(hospitalAdmitChecks.get(0).getFacId(), "Facility ID should be null");
        Integer expectedPtTypId;
        switch (ptTyp)
        {
            case "I":
                expectedPtTypId = MiscMap.PT_TYP_IN;
                break;
            case "O":
                expectedPtTypId = MiscMap.PT_TYP_OUT;
                break;
            case "N":
                expectedPtTypId = MiscMap.PT_TYP_NON;
                break;
            default:
                expectedPtTypId = null;
        }
        Assert.assertEquals(hospitalAdmitChecks.get(0).getPtTypId(), expectedPtTypId, "Patient Type ID does not match");
    }

    @Test(priority = 1, description = "Hospital Admit - Valid Facility ID")
    @Parameters({"subId", "ssn", "dob", "admitDt", "dischargeDt", "ptLastName", "ptFirstName", "ptMi", "facAbbrv"})
    public void testHospitalAdmitValidFacId(String subId, String ssn, String dob, String admitDt, @Optional String dischargeDt, String ptLastName, String ptFirstName, @Optional String ptMi, String facAbbrv) throws Exception
    {
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);
        timeStamp = new TimeStamp(driver);

        cleanUpHospitalAdmitData(subId);

        String formatType = "hospitalAdmit";
        String recordType = "A";
        logger.info("message=Creating hospital admit check content, recordType="+recordType+", subId="+subId+", ssn="+ssn+", dob="+dob+", admitDt="+admitDt+", dischargeDt="+dischargeDt+
                ", ptLastName="+ptLastName+", ptFirstName="+ptFirstName+", facAbbrv="+facAbbrv);
        String content = importEngineUtils.createDataForHospitalAdmitFile(recordType, subId, ptLastName, ptFirstName, ptMi, dob, admitDt, dischargeDt, ssn, facAbbrv, null);
        logger.info("message=Created hospital admit check content, content="+content);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String fileName = formatType + "_" + timeStamp.getCurrentDate("yyyyMMddHHmmssSS") + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        logger.info("message=Creating hospital admit check file, incomingDir="+filePathIncoming+", filename="+fileName);
        fileManipulation.writeFileToFolder(content, filePathIncoming, fileName);

        File stagedFile = new File(filePathIncoming + fileName);
        Assert.assertTrue(isFileExists(stagedFile, 5), "Staged hospital admit check file does not exist, stagedFile="+stagedFile.getAbsolutePath());

        File processedFile = new File(filePathProcessed + fileName);
        waitForImportEngine(processedFile, isFileExists(processedFile, 5), QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(isFileExists(processedFile, 5), "Processed hospital admit check file does not exist, processedFile="+processedFile.getAbsolutePath());

        logger.info("message=Verifying hospital admit data, subId="+subId);
        List<HospitalAdmitCheck> hospitalAdmitChecks = rpmDao.getHospitalAdmitCheckBySubsId(null, subId);
        Assert.assertEquals(hospitalAdmitChecks.size(), 1, "Expected one hospital admit check record, subId="+subId);
        Assert.assertEquals(hospitalAdmitChecks.get(0).getSubsId(), subId, "Subscriber ID does not match");
        Assert.assertEquals(hospitalAdmitChecks.get(0).getSsn(), Integer.parseInt(ssn), "SSN does not match");
        Assert.assertEquals(HOSPITAL_ADMIT_DATE_FORMAT.format(hospitalAdmitChecks.get(0).getDob()), dob, "DOB does not match");
        Assert.assertEquals(HOSPITAL_ADMIT_DATE_FORMAT.format(hospitalAdmitChecks.get(0).getAdmitDt()), admitDt, "Admit Date does not match");
        if (StringUtils.isNotBlank(dischargeDt))
        {
            Assert.assertEquals(HOSPITAL_ADMIT_DATE_FORMAT.format(hospitalAdmitChecks.get(0).getDischargeDt()), dischargeDt, "Discharge Date does not match");
        }
        else
        {
            Assert.assertNull(hospitalAdmitChecks.get(0).getDischargeDt(), "Discharge Date does not match");
        }
        Assert.assertEquals(hospitalAdmitChecks.get(0).getPtLNm(), ptLastName, "Last Name does not match");
        String expectedFirstName = StringUtils.isNotBlank(ptMi) ? ptFirstName+","+ptMi : ptFirstName;
        Assert.assertEquals(hospitalAdmitChecks.get(0).getPtFNm(), expectedFirstName, "First Name does not match");
        Assert.assertNull(hospitalAdmitChecks.get(0).getXrefId(), "XREF ID should be null");
        Assert.assertNull(hospitalAdmitChecks.get(0).getAccnId(), "Accession ID should be null");
        Fac expectedFac = facilityDao.getFacByAbbrv(facAbbrv);
        Assert.assertEquals(hospitalAdmitChecks.get(0).getFacId(), Integer.valueOf(expectedFac.getFacId()), "Facility ID does not match");
        Assert.assertNull(hospitalAdmitChecks.get(0).getPtTypId(), "Patient Type ID should be null");
    }

    @Test(priority = 1, description = "Hospital Admit - Invalid Facility ID")
    @Parameters({"subId", "ssn", "dob", "admitDt", "dischargeDt", "ptLastName", "ptFirstName", "ptMi", "facAbbrv"})
    public void testHospitalAdmitInvalidFacId(String subId, String ssn, String dob, String admitDt, @Optional String dischargeDt, String ptLastName, String ptFirstName, @Optional String ptMi, String facAbbrv) throws Exception
    {
        importEngineUtils = new ImportEngineUtils(driver);
        fileManipulation = new FileManipulation(driver);
        timeStamp = new TimeStamp(driver);
        engineUtils = new EngineUtils(driver);

        cleanUpHospitalAdmitData(subId);

        String formatType = "hospitalAdmit";
        String recordType = "A";
        logger.info("message=Creating hospital admit check content, recordType="+recordType+", subId="+subId+", ssn="+ssn+", dob="+dob+", admitDt="+admitDt+", dischargeDt="+dischargeDt+
                ", ptLastName="+ptLastName+", ptFirstName="+ptFirstName+", facAbbrv="+facAbbrv);
        String content = importEngineUtils.createDataForHospitalAdmitFile(recordType, subId, ptLastName, ptFirstName, ptMi, dob, admitDt, dischargeDt, ssn, facAbbrv, null);
        logger.info("message=Created hospital admit check content, content="+content);

        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String fileName = formatType + "_" + timeStamp.getCurrentDate("yyyyMMddHHmmssSS") + ".txt";
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        logger.info("message=Creating hospital admit check file, incomingDir="+filePathIncoming+", filename="+fileName);
        fileManipulation.writeFileToFolder(content, filePathIncoming, fileName);

        File stagedFile = new File(filePathIncoming + fileName);
        Assert.assertTrue(isFileExists(stagedFile, 5), "Staged hospital admit check file does not exist, stagedFile="+stagedFile.getAbsolutePath());

        File processedFile = new File(filePathProcessed + fileName);
        waitForImportEngine(processedFile, isFileExists(processedFile, 5), QUEUE_WAIT_TIME_MS);
        Assert.assertTrue(isFileExists(processedFile, 5), "Processed hospital admit check file does not exist, processedFile="+processedFile.getAbsolutePath());

        File erroredFile = new File(filePathErrored + fileName + ".error");
        logger.info("message=Verifying error file, subId="+subId+", erroredFile="+erroredFile.getAbsolutePath());
        Assert.assertTrue(isFileExists(erroredFile, 5), "Errored hospital admit check file does not exist, erroredFile="+erroredFile.getAbsolutePath());
        String expectedErrorContent = "Facility could not be determined from abbrev, facAbbrv="+facAbbrv;
        String actualErrorContent = StringUtils.trim(engineUtils.readFile(filePathErrored, erroredFile.getName()));
        Assert.assertTrue(StringUtils.endsWith(actualErrorContent, expectedErrorContent));

        logger.info("message=Verifying hospital admit data, subId="+subId);
        List<HospitalAdmitCheck> hospitalAdmitChecks = rpmDao.getHospitalAdmitCheckBySubsId(null, subId);
        Assert.assertTrue(hospitalAdmitChecks.isEmpty(), "Expected no hospital admit check records, subId=");
    }

    private void cleanUpHospitalAdmitData(String subId) throws XifinDataAccessException
    {
        logger.info("message=Deleting hospital admit check data, subId="+subId);
        for (HospitalAdmitCheck hospitalAdmitCheck : rpmDao.getHospitalAdmitCheckBySubsId(testDb, subId))
        {
            hospitalAdmitCheck.setResultCode(ErrorCodeMap.DELETED_RECORD);
            rpmDao.set(hospitalAdmitCheck);
            logger.info("message=Deleting hospital admit check record, subId="+subId+", hospitalAdmitCheckId="+hospitalAdmitCheck.getHospitalAdmitCheckId());
        }
    }

    protected void waitForImportEngine(File f, boolean status, long maxTime) throws Exception {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = status;
        while (isInQueue == status && System.currentTimeMillis() < maxTime) {
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInQueue = isFileExists(f, 5);
        }
    }

}
