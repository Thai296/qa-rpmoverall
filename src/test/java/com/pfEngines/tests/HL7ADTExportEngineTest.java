package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.pt.Pt;
import com.overall.utils.EngineUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.ListUtil;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HL7ADTExportEngineTest extends SeleniumBaseTest {
	
	private EngineUtils engineUtils;
	private FileManipulation fileManipulation;
	private ListUtil listUtil;
	XifinAdminUtils xifinAdminUtils;
	
	@Test(priority = 1, description="PT.B_SENT_ADT = 0 and the record can be processed")
	@Parameters({ "formatType", "ptSeqId", "email", "password"})
	public void testPFER_469(String formatType, String ptSeqId, String email, String password) throws Exception {
		logger.info("====== Testing - testPFER_469 ======");
		fileManipulation = new FileManipulation(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		listUtil = new ListUtil();
		engineUtils = new EngineUtils(driver);
		
		logger.info("*** Step 1 Action: - Delete all the previously generated export files in out folder");
		String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		System.out.println("filePathOut = " + filePathOut);//Debug Info		
		File f = new File(filePathOut);	
		
		logger.info("*** Step 1 Expected Results: - Verify that the all the files are deleted in the out folder");			
		Assert.assertTrue(fileManipulation.deleteAllFilesInDirectory(filePathOut), "        All files under " + filePathOut + " should be deleted.");
		
		logger.info("*** Step 2 Action: - Update a record in PT and set B_SENT_ADT = 0");
		daoManagerPlatform.setValuesFromTable("PT", "B_SENT_ADT = 1", "pk_seq_id != " + ptSeqId + " and B_SENT_ADT = 0", testDb);
		daoManagerPlatform.setValuesFromTable("PT", "B_SENT_ADT = 0", "pk_seq_id = " + ptSeqId, testDb);		
		
		logger.info("*** Step 3 Action: - Run Platform HL7 ADT Export Engine");
		xifinAdminUtils.runPFEngine(this, email, password, null, "Hl7AdtExportEngine", "SSO_APP_STAGING", false);

		logger.info("*** Step 3 Expected Results: - Verify that PT.B_SENT_ADT = 1");
		String epi = patientDao.getPtBySeqId(Integer.parseInt(ptSeqId)).getEpi();
		Pt ptInfoList = patientDao.getPtByEpi(epi);
        Assert.assertTrue(ptInfoList.getIsSentAdt(), "        PT.B_SENT_ADT = 1 after running the engine.");
		
		logger.info("*** Step 3 Expected Results: - Verify that a HL7 export file is generated in the out folder");
        Assert.assertEquals(String.valueOf(Objects.requireNonNull(f.listFiles()).length), "1", "        A file should be generated after running the engine.");
		System.out.println("f.listFiles()[0] = " + Objects.requireNonNull(f.listFiles())[0]);//Debug Info
		
		logger.info("*** Step 3 Expected Results: - Verify that the HL7 export file size > 0");
		String newFilePath = String.valueOf(Objects.requireNonNull(f.listFiles())[0]);
		Assert.assertTrue(((File)new File(newFilePath)).length() > 0, "        An non-empty file should be generated after running the engine.");
		
		logger.info("*** Step 3 Expected Results: - Verify that the HL7 export file contains correct data");
		List<String> origPIDList = new ArrayList<>();
		origPIDList.add("PID");
		origPIDList.add("1");
		origPIDList.add("9436441");
		origPIDList.add("WU^HAZEL^");
		origPIDList.add("19410331");
		origPIDList.add("F");
		origPIDList.add("VPALRH^OFIXXF^WILLIS^MI^48191^US");
		origPIDList.add("6198811959");
		origPIDList.add("000001234");
		logger.info("       origPIDList: " + origPIDList);
		
		List<String> origIN1List = new ArrayList<>();
		origIN1List.add("IN1");
		origIN1List.add("1");
		origIN1List.add("BCBSND");
		origIN1List.add("BCBS ND");
		origIN1List.add("3BD8IA^^EAU CLAIRE^ND^58756^US");
		origIN1List.add("WESTERN SERVICE CENTER");
		origIN1List.add("8008483308");
		origIN1List.add("WU^HAZEL^");
		origIN1List.add("S");
		origIN1List.add("19410331");
		origIN1List.add("5343 PENN AVENUE N^^SAINT PAUL^MN^55118^US");
		origIN1List.add("F");
		logger.info("       origIN1List: " + origIN1List);
		
		List<String> origBTSList = new ArrayList<>();
		origBTSList.add("BTS");
		origBTSList.add("1");
		origBTSList.add("1");
		logger.info("       origBTSList: " + origBTSList);

		List<String> newPIDList = engineUtils.parseHL7ADTExportFile(newFilePath, "PID");
		List<String> newIN1List = engineUtils.parseHL7ADTExportFile(newFilePath, "IN1");
		List<String> newBTSList = engineUtils.parseHL7ADTExportFile(newFilePath, "BTS");

		Assert.assertTrue(listUtil.compareLists(origPIDList, newPIDList), "        PID segment in the newly generated file should match the expected results.");
        Assert.assertTrue(listUtil.compareLists(origIN1List, newIN1List), "        IN1 segment in the newly generated file should match the expected results.");
        Assert.assertTrue(listUtil.compareLists(origBTSList, newBTSList), "        BTS segment in the newly generated file should match the expected results.");
		
	}	

	@Test(priority = 1, description="T.B_SENT_ADT = 1 and the record won't be processed")
	@Parameters({"formatType","ptSeqId", "email", "password"})
	public void testPFER_470(String formatType, String ptSeqId, String email, String password) throws Exception {
		logger.info("====== Testing - testPFER_470 ======");
		fileManipulation = new FileManipulation(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		listUtil = new ListUtil();
		engineUtils = new EngineUtils(driver);

		logger.info("*** Step 1 Action: - Delete all the previously generated export files in out folder");
		String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		System.out.println("filePathOut = " + filePathOut);//Debug Info		
		File f = new File(filePathOut);	
		
		logger.info("*** Step 1 Expected Results: - Verify that the all the files are deleted in the out folder");			
		Assert.assertTrue(fileManipulation.deleteAllFilesInDirectory(filePathOut), "        All files under " + filePathOut + " should be deleted.");
		
		logger.info("*** Step 2 Action: - Update all the records in PT and set B_SENT_ADT = 1");
		daoManagerPlatform.setValuesFromTable("PT", "B_SENT_ADT = 1", "pk_seq_id != " + ptSeqId + " and B_SENT_ADT = 0", testDb);

		logger.info("*** Step 3 Action: - Run Platform HL7 ADT Export Engine");
		xifinAdminUtils.runPFEngine(this, email, password, null, "Hl7AdtExportEngine", "SSO_APP_STAGING", false);

		logger.info("*** Step 3 Expected Results: - Verify that no file is generated in the out folder");
        Assert.assertEquals(String.valueOf(Objects.requireNonNull(f.listFiles()).length), "0", "        No file should be generated after running the engine.");

	}	
	
	@Test(priority = 1, description="PT.B_SENT_ADT = 0 and multiple records can be processed")
	@Parameters({"formatType", "ptSeqId1", "ptSeqId2", "email", "password"})
	public void testPFER_471(String formatType, String ptSeqId1, String ptSeqId2,String email, String password) throws Exception {
		logger.info("====== Testing - testPFER_471 ======");
		fileManipulation = new FileManipulation(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		listUtil = new ListUtil();
		engineUtils = new EngineUtils(driver);
		
		logger.info("*** Step 1 Action: - Delete all the previously generated export files in out folder");
		String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		System.out.println("filePathOut = " + filePathOut);//Debug Info		
		File f = new File(filePathOut);	
		
		logger.info("*** Step 1 Expected Results: - Verify that the all the files are deleted in the out folder");			
		Assert.assertTrue(fileManipulation.deleteAllFilesInDirectory(filePathOut), "        All files under " + filePathOut + " should be deleted.");
		
		logger.info("*** Step 2 Action: - Update multiple records in PT and set B_SENT_ADT = 0");
		daoManagerPlatform.setValuesFromTable("PT", "B_SENT_ADT = 1", "B_SENT_ADT = 0", testDb);
		daoManagerPlatform.setValuesFromTable("PT", "B_SENT_ADT = 0", "pk_seq_id in (" + ptSeqId1 + ", " + ptSeqId2 + ")", testDb);		
		
		logger.info("*** Step 3 Action: - Run Platform HL7 ADT Export Engine");
		xifinAdminUtils.runPFEngine(this, email, password, null, "Hl7AdtExportEngine", "SSO_APP_STAGING", false);
		
		logger.info("*** Step 3 Expected Results: - Verify that the records have PT.B_SENT_ADT = 1");
		String epi = patientDao.getPtBySeqId(Integer.parseInt(ptSeqId1)).getEpi();
		Pt ptInfoList = patientDao.getPtByEpi(epi);
		Assert.assertTrue(ptInfoList.getIsSentAdt(), "        PT.B_SENT_ADT = 1 after running the engine for pt.pk_seq_id = " + ptSeqId1);

		epi = patientDao.getPtBySeqId(Integer.parseInt(ptSeqId2)).getEpi();
		ptInfoList = patientDao.getPtByEpi(epi);
		Assert.assertTrue(ptInfoList.getIsSentAdt(), "        PT.B_SENT_ADT = 1 after running the engine for pt.pk_seq_id = " + ptSeqId2);

		logger.info("*** Step 3 Expected Results: - Verify that a HL7 export file is generated in the out folder");
        Assert.assertEquals(String.valueOf(Objects.requireNonNull(f.listFiles()).length), "1", "        A file should be generated after running the engine.");

        logger.info("*** Step 3 Expected Results: - Verify that the HL7 export file size > 0");
		String newFilePath = String.valueOf(Objects.requireNonNull(f.listFiles())[0]);
		Assert.assertTrue(((File)(new File(newFilePath))).length() > 0, "        An non-empty file should be generated after running the engine.");
		System.out.println("f.listFiles()[0] = " + Objects.requireNonNull(f.listFiles())[0]+" length"+ ((File)(new File(newFilePath))).length());//Debug Info

		logger.info("*** Step 3 Expected Results: - Verify that the HL7 export file contains correct data for multiple PT records");
		List<String> origPIDList = new ArrayList<>();
		origPIDList.add("PID");
		origPIDList.add("1");
		origPIDList.add("1946903");
		origPIDList.add("HOROWITZ^JORDAN^");
		origPIDList.add("19630508");
		origPIDList.add("F");
		origPIDList.add("4475 REINDEER LANE^^HASTINGS^MN^55033^US");
		origPIDList.add("000001234");

		origPIDList.add("PID");
		origPIDList.add("1");
		origPIDList.add("9436441");
		origPIDList.add("WU^HAZEL^");
		origPIDList.add("19410331");
		origPIDList.add("F");
		origPIDList.add("VPALRH^OFIXXF^WILLIS^MI^48191^US");
		origPIDList.add("6198811959");
		origPIDList.add("000001234");
		logger.info("       origPIDList: " + origPIDList);
		
		List<String> origIN1List = new ArrayList<>();
		origIN1List.add("IN1");
		origIN1List.add("1");
		origIN1List.add("MCCT");
		origIN1List.add("MEDICARE - CONNECTICUT");
		origIN1List.add("G3E7QR^CT029-05AA^HARTFORD^CT^06504^US");
		origIN1List.add("UNITED HEALTH");
		origIN1List.add("8607026668");
		origIN1List.add("HOROWITZ^JORDAN^");
		origIN1List.add("S");
		origIN1List.add("19630508");
		origIN1List.add("4475 REINDEER LANE^^HASTINGS^MN^55033^US");
		origIN1List.add("F");
		origIN1List.add("456456");

		origIN1List.add("IN1");
		origIN1List.add("1");
		origIN1List.add("BCBSND");
		origIN1List.add("BCBS ND");
		origIN1List.add("3BD8IA^^EAU CLAIRE^ND^58756^US");
		origIN1List.add("WESTERN SERVICE CENTER");
		origIN1List.add("8008483308");
		origIN1List.add("WU^HAZEL^");
		origIN1List.add("S");
		origIN1List.add("19410331");
		origIN1List.add("5343 PENN AVENUE N^^SAINT PAUL^MN^55118^US");
		origIN1List.add("F");
		logger.info("       origIN1List: " + origIN1List);
		
		List<String> origBTSList = new ArrayList<>();
		origBTSList.add("BTS");
		origBTSList.add("2");
		origBTSList.add("2");
		logger.info("       origBTSList: " + origBTSList);
		
		List<String> newPIDList;
		List<String> newIN1List;
		List<String> newBTSList;
		
		newPIDList = engineUtils.parseHL7ADTExportFile(newFilePath, "PID");
		newIN1List = engineUtils.parseHL7ADTExportFile(newFilePath, "IN1");
		newBTSList = engineUtils.parseHL7ADTExportFile(newFilePath, "BTS");

		logger.info("origPIDList.length: " + origPIDList.size() + " newPIDList.length: "+ newPIDList.size());
		logger.info("origPIDList: "+ origPIDList);
		logger.info("newPIDList: "+ newPIDList);
		
		Assert.assertTrue(listUtil.compareLists(origPIDList, newPIDList), "        PID segment in the newly generated file should match the expected results.");
        Assert.assertTrue(listUtil.compareLists(newIN1List, origIN1List), "        IN1 segment in the newly generated file should match the expected results.");
        Assert.assertTrue(listUtil.compareLists(origBTSList, newBTSList), "        BTS segment in the newly generated file should match the expected results.");

	}
    private void waitForHl7AdtExportEngine(File f, long maxTime) throws Exception {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        int isInQueue = Objects.requireNonNull(f.listFiles()).length;
        while(isInQueue!=1 && System.currentTimeMillis() < maxTime){
            logger.info("Waiting for HL7 ADT Export engine, elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInQueue = Objects.requireNonNull(f.listFiles()).length;
        }

    }
}
