package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.clnCorrespHist.ClnCorrespHist;
import com.mbasys.mars.ejb.entity.correspFile.CorrespFile;
import com.mbasys.mars.ejb.entity.prc.Prc;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.accnws.dao.DaoManagerAccnWS;
import com.xifin.accnws.dao.IGenericDaoAccnWS;
import com.xifin.clientws.dao.DaoManagerClientWS;
import com.xifin.clientws.dao.IGenericDaoClientWS;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.platform.dao.IGenericDaoPlatform;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class ClientCorrespondenceEngineTest extends SeleniumBaseTest {
	
	private TimeStamp timeStamp;
	private XifinAdminUtils xifinAdminUtils;
	private FileManipulation fileManipulation;
	private ConvertUtil convertUtil;
	private IGenericDaoPlatform iGenericDaoPlatform;
	private IGenericDaoAccnWS iGenericDaoAccnWS;
	private IGenericDaoClientWS iGenericDaoClientWS;
	private SimpleDateFormat format;
	protected static long QUEUE_POLL_TIME_MS = TimeUnit.SECONDS.toMillis(5);
	private static final long QUEUE_WAIT_TIME_MS = TimeUnit.MINUTES.toMillis(10);
	
	@Test(priority = 1, description = "File_Typ_Id=4 and Creat_Dt is null and Pal_Cln_Test_Prc.size()>0")
	@Parameters({"formatType"})
	public void testPFER_476(String formatType) throws Exception {
		logger.info("====== Testing - PFER_476 ======");		
		
		timeStamp=new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);
		convertUtil = new ConvertUtil();
		iGenericDaoPlatform = new DaoManagerPlatform(config.getRpmDatabase());
		iGenericDaoAccnWS = new DaoManagerAccnWS(config.getRpmDatabase());
		iGenericDaoClientWS = new DaoManagerClientWS(config.getRpmDatabase());
		format = new SimpleDateFormat("MM/dd/yyyy");

		logger.info("*** Step 1 Actions: - Update records in CORRESP_FILE so they can't be processed by the Engine");
		List<CorrespFile> oldCorrespFile = clientDao.getCorrespFileByCreateDtIsNullFileSeqNotNull();
		String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");

		for (CorrespFile strings : oldCorrespFile) {
			iGenericDaoPlatform.setValuesFromTableByColNameValue("Corresp_File", "CREAT_DT = to_date('" + currDt + "','MM/dd/yyyy')", "PK_FILE_SEQ", String.valueOf(strings.getFileSeq())
					, testDb);
		}
		
		logger.info("*** Step 2 Actions: - Create a new record in CORRESP_FILE with Fk_file_typ_id = 4 and Creat_dt = null");
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_FILE_SEQ");
		insertFields.add("FK_SUBM_SVC_SEQ_ID");
		insertFields.add("FK_ACK_FILE_ID");
		insertFields.add("FK_DOC_ID");
		insertFields.add("FILE_REL_PATH");
		insertFields.add("B_PROCESSED");
		insertFields.add("B_PULL_OK");
		insertFields.add("FILE_NAME");
		insertFields.add("FK_FILE_TYP_ID");
		
		List <Object> insertValues= new ArrayList<>();
		String fileSeq = iGenericDaoPlatform.getNextSeqIdFromDUALBySequenceId("CORRESP_FILE_SEQ",testDb);
		String fileName = "ClientPal_" + timeStamp.getTimeStamp() + ".pdf.gz";
		//SS# = 1701 (Client Letters outbound extension to the root directory)
		String filePath = systemDao.getSystemSetting(1701).getDataValue();
		insertValues.add(Integer.parseInt(fileSeq));		
		insertValues.add(0);
		insertValues.add(0);		
		insertValues.add(0);
		insertValues.add(filePath);
		insertValues.add(0);
		insertValues.add(0);
		insertValues.add(fileName);
		insertValues.add(4);				
		int num = iGenericDaoPlatform.addRecordIntoTable("CORRESP_FILE", insertFields, insertValues, testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that a new record is added into CORRESP_FILE table");
		assertTrue(num > 0, "        A new record should be added into CORRESP_FILE table.");
		
		logger.info("*** Step 3 ActionS: - Create a new record in CLN_CORRESP_HIST table with: pk_file_seq_id=Corresp_File.Pk_file_Seq.");
		String clnAbbrev = "AUTOTEST100";
		int clnId = clientDao.getClnByClnAbbrev(clnAbbrev).getClnId();
		
		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_FILE_SEQ_ID");
		insertFields.add("B_ACK_REQ");
		insertFields.add("B_ACK_REC");
		
		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(Integer.parseInt(fileSeq));
		insertValues.add(0);
		insertValues.add(0);
		num = iGenericDaoPlatform.addRecordIntoTable("CLN_CORRESP_HIST", insertFields, insertValues, testDb);
		
		logger.info("*** Step 3 Expected Results: - Verify that a new record is added into CLN_CORRESP_HIST table");
		assertTrue(num > 0, "        A new record should be added into CLN_CORRESP_HIST table.");
		
		logger.info("*** Step 4 Action: Add new record in Cln_adl_stage with: Pk_cln_id= cln_corresp_hist.pk_cln_id. Pk_test_id is profile test.");
		List<String> profTestId = daoManagerXifinRpm.getProfileTestFromTESTByHavingMaxTwoComponents(testDb);		
		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_TEST_ID");		
		
		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(profTestId.get(0));
		num = iGenericDaoPlatform.getRecordFromTable("CLN_ADL_STAGE", insertFields, insertValues, testDb)==0?iGenericDaoPlatform.addRecordIntoTable("CLN_ADL_STAGE", insertFields, insertValues, testDb):1;

		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into CLN_ADL_STAGE table");
		assertTrue(num > 0, "        A new record should be added into CLN_ADL_STAGE table.");		
		
		logger.info("*** Step 5 Actions:  - Create a new record in pal_cln_test_prc with: Pk_cln_id = cln_corresp_hist.Pk_Cln_id. Pk_test_id=cln_adl_stage.pk_test_id.");
		//PRC_ABBREV = 'AETNA'				
		List<Prc> prcs = prcDao.getPrcByPrcAbbrev("AETNA");
		
		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_PRC_ID");
		insertFields.add("PK_TEST_ID");

		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(prcs.get(0).prcId);
		insertValues.add(profTestId.get(0));
		num=iGenericDaoPlatform.getRecordFromTable("PAL_CLN_TEST_PRC", insertFields, insertValues, testDb);
		insertFields.add("FK_FILE_SEQ_ID");
		insertValues.add(Integer.parseInt(fileSeq));
		num = num==0?iGenericDaoPlatform.addRecordIntoTable("PAL_CLN_TEST_PRC", insertFields, insertValues, testDb):num;

		logger.info("*** Step 5 Expected Results: - Verify that a new record is added into PAL_CLN_TEST_PRC table");
		assertTrue(num > 0, "        A new record should be added into PAL_CLN_TEST_PRC table.");

		logger.info("*** Step 6 Actions: - Run PF-Client Correspondence Engine");
		clientDao.waitForClientCorrespEng(Integer.parseInt(fileSeq),QUEUE_WAIT_TIME_MS);

		logger.info("*** Step 6 Expected Results: - Verify that a Client Physician Acknowledgement Letter (PAL) is generated");
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		File fOut = new File(dirBase + fileName);		
		Assert.assertTrue(isFileExists(fOut, 5), "        Client Physician Acknowledgement Letter (PAL): " + fileName + " should be generated under " + dirBase + " folder.");

		logger.info("*** Step 6 Expected Results: - Verify that the Corresp_File.Creat_Dt is updated to Sysdate");		
		CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(Integer.parseInt(fileSeq));
		assertNotNull(correspFile.getCreatDt(), "        Corresp_File.Creat_Dt should not be null ");
		
		String[] fList = fileName.split("[.]");
		String pdfFileName = fList[0] + ".pdf";		

		logger.info("*** Step 7 Actions: - Decompress the generated GZ file");
		convertUtil.decompressGzipFile(fileName, dirBase);
		Thread.sleep(3000);
		File unZippedFile = new File(dirBase + pdfFileName);		
		Assert.assertTrue(isFileExists(unZippedFile, 5), "        Unzipped PAL file: " + pdfFileName + " should be generated under " + dirBase + " folder.");

		logger.info("*** Step 7 Expected Results: - Verify that the PAL letter header and the profile data are printed in the PDF");
		String profTestAbbev = profTestId.get(1);		
		List<com.mbasys.mars.ejb.entity.test.Test> comps = testDao.getProfTestCompFromTestByTestAbbrev(profTestAbbev);
		
		String pdf = convertUtil.getTextFromPdf(1, 1, dirBase + pdfFileName);
		String header = "Physician Acknowledgment";
		assertTrue(pdf.contains(header),"        The PAL letter header " + header + " is printed in the PDF.");
		
		assertTrue(pdf.contains(profTestAbbev),"        The Profile test " + profTestAbbev + " is printed in the PDF.");
		
		for(com.mbasys.mars.ejb.entity.test.Test comp:comps){
			String compName = comp.getName();
			assertTrue(pdf.contains(compName),"        The profile test " + profTestAbbev + " component name " + compName + " is printed in the PDF.");
		}		
						
		logger.info("*** Step 8 Actions: - Clear test data");		
		fileManipulation.deleteFile(dirBase, fileName);		
		fileManipulation.deleteFile(dirBase, pdfFileName);	
		
		//delete data
		for (CorrespFile strings : oldCorrespFile) {
			iGenericDaoPlatform.setValuesFromTableByColNameValue("Corresp_File", "CREAT_DT = ''", "PK_FILE_SEQ", String.valueOf(strings.getFileSeq()), testDb);
		}
		
		iGenericDaoPlatform.deleteDataFromTableByCondition("Cln_Corresp_Hist","PK_FILE_SEQ_ID="+fileSeq,testDb);
		iGenericDaoPlatform.deleteDataFromTableByCondition("Pal_Cln_Test_Prc","FK_FILE_SEQ_ID="+fileSeq,testDb);
		iGenericDaoPlatform.deleteDataFromTableByCondition("Cln_Adl_Stage","Pk_Cln_Id="+clnId,testDb);
		iGenericDaoPlatform.deleteDataFromTableByCondition("corresp_file","PK_FILE_SEQ="+fileSeq,testDb);
	}
	
	@Test(priority = 1, description = "File_Typ_Id=4 and Creat_Dt is null and Pal_Cln_Test_Prc.size()=0")
	@Parameters({"formatType"})
	public void testPFER_477(String formatType) throws Exception {
		logger.info("====== Testing - PFER_477 ======");		
		
		timeStamp=new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);

		logger.info("*** Step 1 Actions: - Update records in CORRESP_FILE so they can't be processed by the Engine");
		List<CorrespFile> oldCorrespFile = clientDao.getCorrespFileByCreateDtIsNullFileSeqNotNull();
		String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");

		for (CorrespFile strings : oldCorrespFile) {
			iGenericDaoPlatform.setValuesFromTableByColNameValue("Corresp_File", "CREAT_DT = to_date('" + currDt + "','MM/dd/yyyy')", "PK_FILE_SEQ", String.valueOf(strings.getFileSeq()), testDb);
		}
		
		logger.info("*** Step 2 Actions: - Create a new record in CORRESP_FILE with Fk_file_typ_id = 4 and Creat_dt = null");
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_FILE_SEQ");
		insertFields.add("FK_SUBM_SVC_SEQ_ID");
		insertFields.add("FK_ACK_FILE_ID");
		insertFields.add("FK_DOC_ID");
		insertFields.add("FILE_REL_PATH");
		insertFields.add("B_PROCESSED");
		insertFields.add("B_PULL_OK");
		insertFields.add("FILE_NAME");
		insertFields.add("FK_FILE_TYP_ID");
		
		List <Object> insertValues= new ArrayList<>();
		String fileSeq = iGenericDaoPlatform.getNextSeqIdFromDUALBySequenceId("CORRESP_FILE_SEQ",testDb);
		String fileName = "ClientPal_" + timeStamp.getTimeStamp() + ".pdf.gz";
		//SS# = 1701 (Client Letters outbound extension to the root directory)
		String filePath = systemDao.getSystemSetting(1701).getDataValue();
		insertValues.add(Integer.parseInt(fileSeq));		
		insertValues.add(0);
		insertValues.add(0);		
		insertValues.add(0);
		insertValues.add(filePath);
		insertValues.add(0);
		insertValues.add(0);
		insertValues.add(fileName);
		insertValues.add(4);				
		int num = iGenericDaoPlatform.addRecordIntoTable("CORRESP_FILE", insertFields, insertValues, testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that a new record is added into CORRESP_FILE table");
		assertTrue(num > 0, "        A new record should be added into CORRESP_FILE table.");
		
		logger.info("*** Step 3 ActionS: - Create a new record in CLN_CORRESP_HIST table with: pk_file_seq_id=Corresp_File.Pk_file_Seq, Pk_cln_id not in Pal_cln_test_prc");
		int clnId = clientDao.getClnNotInPalClnTestProc().getClnId();

		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_FILE_SEQ_ID");
		insertFields.add("B_ACK_REQ");
		insertFields.add("B_ACK_REC");
		
		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(Integer.parseInt(fileSeq));
		insertValues.add(0);
		insertValues.add(0);
		num = iGenericDaoPlatform.addRecordIntoTable("CLN_CORRESP_HIST", insertFields, insertValues, testDb);
		
		logger.info("*** Step 3 Expected Results: - Verify that a new record is added into CLN_CORRESP_HIST table");
		assertTrue(num > 0, "        A new record should be added into CLN_CORRESP_HIST table.");

		logger.info("*** Step 6 Actions: - Run PF-Client Correspondence Engine");
		clientDao.isInCorrespFile(Integer.parseInt(fileSeq));
		
		logger.info("*** Step 6 Expected Results: - Verify that a Client Physician Acknowledgement Letter (PAL) is generated");
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		File fOut = new File(dirBase + fileName);		
		Assert.assertTrue(isFileExists(fOut, 5), "        Client Physician Acknowledgement Letter (PAL): " + fileName + " should be generated under " + dirBase + " folder.");

		logger.info("*** Step 6 Expected Results: - Verify that the Corresp_File.Creat_Dt is not updated");		
		CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(Integer.parseInt(fileSeq));
		assertNotNull(correspFile.getCreatDt(), "        Corresp_File.Creat_Dt should not be null ");

	}	
	
	@Test(priority = 1, description = "File_Typ_Id=10 and Creat_Dt is null and Pal_Cln_Test_Prc.size()>0")
	@Parameters({"formatType"})
	public void testPFER_478(String formatType) throws Exception {
		logger.info("====== Testing - PFER_478 ======");		
		
		timeStamp=new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);
		convertUtil = new ConvertUtil();
		iGenericDaoPlatform = new DaoManagerPlatform(config.getRpmDatabase());
		iGenericDaoAccnWS = new DaoManagerAccnWS(config.getRpmDatabase());
		iGenericDaoClientWS = new DaoManagerClientWS(config.getRpmDatabase());

		logger.info("*** Step 1 Actions: - Update records in CORRESP_FILE so they can't be processed by the Engine");
		List<CorrespFile> oldCorrespFile = clientDao.getCorrespFileByCreateDtIsNullFileSeqNotNull();
		String currDt = timeStamp.getCurrentDate("yyyy-MM-dd");

		for (CorrespFile strings : oldCorrespFile) {
			iGenericDaoPlatform.setValuesFromTableByColNameValue("Corresp_File", "CREAT_DT = to_date('" + currDt + "','MM/dd/yyyy')", "PK_FILE_SEQ", String.valueOf(strings.getFileSeq()), testDb);
		}
		
		logger.info("*** Step 2 Actions: - Create a new record in CORRESP_FILE with Fk_file_typ_id = 10 and Creat_dt = null");
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_FILE_SEQ");
		insertFields.add("FK_SUBM_SVC_SEQ_ID");
		insertFields.add("FK_ACK_FILE_ID");
		insertFields.add("FK_DOC_ID");
		insertFields.add("FILE_REL_PATH");
		insertFields.add("B_PROCESSED");
		insertFields.add("B_PULL_OK");
		insertFields.add("FILE_NAME");
		insertFields.add("FK_FILE_TYP_ID");
		
		String clnAbbrev = "AUTOTEST100";
		
		List <Object> insertValues= new ArrayList<>();
		String fileSeq = iGenericDaoPlatform.getNextSeqIdFromDUALBySequenceId("CORRESP_FILE_SEQ",testDb);
		String fileName = "ANNUAL_DISCLOSURE_" + clnAbbrev + "_" + timeStamp.getTimeStamp() + ".pdf.gz";
		//SS# = 1720 (Client Annual Disclosure Letters outbound extension to the root directory)
		String filePath = systemDao.getSystemSetting(1720).getDataValue();
		insertValues.add(Integer.parseInt(fileSeq));		
		insertValues.add(0);
		insertValues.add(0);		
		insertValues.add(0);
		insertValues.add(filePath);
		insertValues.add(0);
		insertValues.add(0);
		insertValues.add(fileName);
		insertValues.add(10);				
		int num = iGenericDaoPlatform.addRecordIntoTable("CORRESP_FILE", insertFields, insertValues, testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that a new record is added into CORRESP_FILE table");
		assertTrue(num > 0, "        A new record should be added into CORRESP_FILE table.");
		
		logger.info("*** Step 3 ActionS: - Create a new record in CLN_CORRESP_HIST table with: pk_file_seq_id=Corresp_File.Pk_file_Seq.");
		int clnId = clientDao.getClnByClnAbbrev(clnAbbrev).getClnId();
		
		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_FILE_SEQ_ID");
		insertFields.add("B_ACK_REQ");
		insertFields.add("B_ACK_REC");
		
		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(Integer.parseInt(fileSeq));
		insertValues.add(0);
		insertValues.add(0);
		num = iGenericDaoPlatform.addRecordIntoTable("CLN_CORRESP_HIST", insertFields, insertValues, testDb);
		
		logger.info("*** Step 3 Expected Results: - Verify that a new record is added into CLN_CORRESP_HIST table");
		assertTrue(num > 0, "        A new record should be added into CLN_CORRESP_HIST table.");
		
		logger.info("*** Step 4 Action: Add new record in Cln_adl_stage with: Pk_cln_id= cln_corresp_hist.pk_cln_id. Pk_test_id is profile test.");
		List<String> profTestId = daoManagerXifinRpm.getProfileTestFromTESTByHavingMaxTwoComponents(testDb);		
		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_TEST_ID");		
		
		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(profTestId.get(0));
		num = iGenericDaoPlatform.getRecordFromTable("CLN_ADL_STAGE", insertFields, insertValues, testDb)==0?iGenericDaoPlatform.addRecordIntoTable("CLN_ADL_STAGE", insertFields, insertValues, testDb):1;

		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into CLN_ADL_STAGE table");
		assertTrue(num > 0, "        A new record should be added into CLN_ADL_STAGE table.");		
		
		logger.info("*** Step 5 Actions:  - Create a new record in pal_cln_test_prc with: Pk_cln_id = cln_corresp_hist.Pk_Cln_id. Pk_test_id=cln_adl_stage.pk_test_id.");
		//PRC_ABBREV = 'AETNA'				
		List<Prc> prcs = prcDao.getPrcByPrcAbbrev("AETNA");

		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_PRC_ID");
		insertFields.add("PK_TEST_ID");

		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(prcs.get(0).prcId);
		insertValues.add(profTestId.get(0));
		num=iGenericDaoPlatform.getRecordFromTable("PAL_CLN_TEST_PRC", insertFields, insertValues, testDb);
		insertFields.add("FK_FILE_SEQ_ID");
		insertValues.add(Integer.parseInt(fileSeq));
		num = num==0?iGenericDaoPlatform.addRecordIntoTable("PAL_CLN_TEST_PRC", insertFields, insertValues, testDb):num;

		logger.info("*** Step 5 Expected Results: - Verify that a new record is added into PAL_CLN_TEST_PRC table");
		assertTrue(num > 0, "        A new record should be added into PAL_CLN_TEST_PRC table.");	
		
		logger.info("*** Step 6 Actions: - Run PF-Client Correspondence Engine");
		clientDao.waitForClientCorrespEng(Integer.parseInt(fileSeq),QUEUE_WAIT_TIME_MS);
		
		logger.info("*** Step 6 Expected Results: - Verify that a Annual Disclosure Letter (ADL) is generated");		
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		File fOut = new File(dirBase + fileName);		
		Assert.assertTrue(isFileExists(fOut, 5), "        Annual Disclosure Letter (ADL): " + fileName + " should be generated under " + dirBase + " folder.");

		logger.info("*** Step 6 Expected Results: - Verify that the Corresp_File.Creat_Dt is updated to Sysdate");		
		CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(Integer.parseInt(fileSeq));
		assertNotNull(correspFile.getCreatDt(), "        Corresp_File.Creat_Dt should not be null ");
		String[] fList = fileName.split("[.]");
		String pdfFileName = fList[0] + ".pdf";		

		logger.info("*** Step 7 Actions: - Decompress the generated GZ file");
		convertUtil.decompressGzipFile(fileName, dirBase);
		Thread.sleep(3000);
		File unZippedFile = new File(dirBase + pdfFileName);		
		Assert.assertTrue(isFileExists(unZippedFile, 5), "        Unzipped ADL file: " + pdfFileName + " should be generated under " + dirBase + " folder.");

		logger.info("*** Step 7 Expected Results: - Verify that the ADL letter header and the profile data are printed in the PDF");
		
		String profTestAbbev = profTestId.get(1);		
		List<com.mbasys.mars.ejb.entity.test.Test> comps = testDao.getProfTestCompFromTestByTestAbbrev(profTestAbbev);
		
		String pdfTextPg1 = convertUtil.getTextFromPdf(1, 1, dirBase + pdfFileName);
		String header = "Dear Doctor";
		assertTrue(pdfTextPg1.contains(header),"        The ADL letter header " + header + " is printed in the PDF.");
		String content = "annual notification";
		assertTrue(pdfTextPg1.contains(content),"        The ADL letter part of contents " + content + " is printed in the PDF.");
		
		String pdfTextPg2 = convertUtil.getTextFromPdf(2, 2, dirBase + pdfFileName);
		assertTrue(pdfTextPg2.contains(profTestAbbev),"        The Profile test " + profTestAbbev + " is printed in the PDF.");
		
		for(com.mbasys.mars.ejb.entity.test.Test comp:comps){
			String compName = comp.getName();
			assertTrue(pdfTextPg2.contains(compName),"        The profile test " + profTestAbbev + " component name " + compName + " is printed in the PDF.");
		}				
						
		logger.info("*** Step 8 Actions: - Clear test data");		
		fileManipulation.deleteFile(dirBase, fileName);		
		fileManipulation.deleteFile(dirBase, pdfFileName);	
		
		//delete data
		for (CorrespFile strings : oldCorrespFile) {
			iGenericDaoPlatform.setValuesFromTableByColNameValue("Corresp_File", "CREAT_DT = ''", "PK_FILE_SEQ", String.valueOf(strings.getFileSeq()), testDb);
		}
		
		iGenericDaoPlatform.deleteDataFromTableByCondition("Cln_Corresp_Hist","PK_FILE_SEQ_ID="+fileSeq,testDb);
		iGenericDaoPlatform.deleteDataFromTableByCondition("Pal_Cln_Test_Prc","FK_FILE_SEQ_ID="+fileSeq,testDb);
		iGenericDaoPlatform.deleteDataFromTableByCondition("Cln_Adl_Stage","Pk_Cln_Id="+clnId,testDb);
		iGenericDaoPlatform.deleteDataFromTableByCondition("corresp_file","PK_FILE_SEQ="+fileSeq,testDb);

	}
	
	@Test(priority = 1, description = "File_Typ_Id=10 and Creat_Dt is null and Pal_Cln_Test_Prc.size()=0")
	@Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_479(String email, String password, String eType, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - PFER_479 ======");		
		
		timeStamp=new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);
		convertUtil = new ConvertUtil();
		iGenericDaoPlatform = new DaoManagerPlatform(config.getRpmDatabase());
		iGenericDaoAccnWS = new DaoManagerAccnWS(config.getRpmDatabase());
		iGenericDaoClientWS = new DaoManagerClientWS(config.getRpmDatabase());

		logger.info("*** Step 1 Actions: - Update records in CORRESP_FILE so they can't be processed by the Engine");
		List<CorrespFile> oldCorrespFile = clientDao.getCorrespFileByCreateDtIsNullFileSeqNotNull();
		String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");

		for (CorrespFile strings : oldCorrespFile) {
			iGenericDaoPlatform.setValuesFromTableByColNameValue("Corresp_File", "CREAT_DT = to_date('" + currDt + "','MM/dd/yyyy')", "PK_FILE_SEQ", String.valueOf(strings.getFileSeq()), testDb);
		}
		
		logger.info("*** Step 2 Actions: - Create a new record in CORRESP_FILE with Fk_file_typ_id = 10 and Creat_dt = null");
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_FILE_SEQ");
		insertFields.add("FK_SUBM_SVC_SEQ_ID");
		insertFields.add("FK_ACK_FILE_ID");
		insertFields.add("FK_DOC_ID");
		insertFields.add("FILE_REL_PATH");
		insertFields.add("B_PROCESSED");
		insertFields.add("B_PULL_OK");
		insertFields.add("FILE_NAME");
		insertFields.add("FK_FILE_TYP_ID");
		
		String clnAbbrev = "AUTOTEST100";
		
		List <Object> insertValues= new ArrayList<>();
		int fileSeq = Integer.parseInt(iGenericDaoPlatform.getNextSeqIdFromDUALBySequenceId("CORRESP_FILE_SEQ",testDb));
		String fileName = "ANNUAL_DISCLOSURE_" + clnAbbrev + "_" + timeStamp.getTimeStamp() + ".pdf.gz";
		//SS# = 1720 (Client Annual Disclosure Letters outbound extension to the root directory)
		String filePath = systemDao.getSystemSetting(1720).getDataValue();
		insertValues.add(fileSeq);
		insertValues.add(0);
		insertValues.add(0);		
		insertValues.add(0);
		insertValues.add(filePath);
		insertValues.add(0);
		insertValues.add(0);
		insertValues.add(fileName);
		insertValues.add(10);				
		int num = iGenericDaoPlatform.addRecordIntoTable("CORRESP_FILE", insertFields, insertValues, testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that a new record is added into CORRESP_FILE table");
		assertTrue(num > 0, "        A new record should be added into CORRESP_FILE table.");
		
		logger.info("*** Step 3 Actions: - Create a new record in CLN_CORRESP_HIST table with: pk_file_seq_id=Corresp_File.Pk_file_Seq.");		
		int clnId = clientDao.getClnByClnAbbrev(clnAbbrev).getClnId();
		
		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_FILE_SEQ_ID");
		insertFields.add("B_ACK_REQ");
		insertFields.add("B_ACK_REC");
		
		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(fileSeq);
		insertValues.add(0);
		insertValues.add(0);
		num = iGenericDaoPlatform.addRecordIntoTable("CLN_CORRESP_HIST", insertFields, insertValues, testDb);
		
		logger.info("*** Step 3 Expected Results: - Verify that a new record is added into CLN_CORRESP_HIST table");
		assertTrue(num > 0, "        A new record should be added into CLN_CORRESP_HIST table.");

		logger.info("*** Step 4 Actions: - Run PF-Client Correspondence Engine");
		xifinAdminUtils.runPFEngine(this, email, password, xapEnv, eType, engConfigDB, false);
		
		logger.info("*** Step 4 Expected Results: - Verify that a Annual Disclosure Letter (ADL) is generated");		
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		File fOut = new File(dirBase + fileName);		
		Assert.assertTrue(isFileExists(fOut, 5), "        Annual Disclosure Letter (ADL): " + fileName + " should be generated under " + dirBase + " folder.");

		logger.info("*** Step 4 Expected Results: - Verify that the records in CORRESP_FILE and CLN_CORRESP_HIST tables are removed");		
		CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(fileSeq);
		Assert.assertNull(correspFile, "        The record for CORRESP_FILE.PK_FILE_SEQ = " + fileSeq + " should be removed.");
		
		List<ClnCorrespHist> clnCorrespHist = clientDao.getClnCorrespHistByFileSeq(fileSeq);
		assertEquals(clnCorrespHist.size(), 0, "        The record for CLN_CORRESP_HIST.PK_FILE_SEQ_id = " + fileSeq + " should be removed.");
						
		logger.info("*** Step 5 Actions: - Clear test data");		
		fileManipulation.deleteFile(dirBase, fileName);		

	}
	
	@Test(priority = 1, description = "File_Typ_Id=11 and Creat_Dt is null and Pal_Cln_Test_Prc.size()>0")
	@Parameters({"formatType"})
	public void testPFER_480(String formatType) throws Exception {
		logger.info("====== Testing - PFER_480 ======");		
		
		timeStamp=new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);
		convertUtil = new ConvertUtil();
		iGenericDaoPlatform = new DaoManagerPlatform(config.getRpmDatabase());
		iGenericDaoAccnWS = new DaoManagerAccnWS(config.getRpmDatabase());
		iGenericDaoClientWS = new DaoManagerClientWS(config.getRpmDatabase());

		logger.info("*** Step 1 Actions: - Update records in CORRESP_FILE so they can't be processed by the Engine");
		List<CorrespFile> oldCorrespFile = clientDao.getCorrespFileByCreateDtIsNullFileSeqNotNull();
		String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");

		for (CorrespFile strings : oldCorrespFile) {
			iGenericDaoPlatform.setValuesFromTableByColNameValue("Corresp_File", "CREAT_DT = to_date('" + currDt + "','MM/dd/yyyy')", "PK_FILE_SEQ", String.valueOf(strings.getFileSeq()), testDb);
		}
		
		logger.info("*** Step 2 Actions: - Create a new record in CORRESP_FILE with Fk_file_typ_id = 11 and Creat_dt = null");
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_FILE_SEQ");
		insertFields.add("FK_SUBM_SVC_SEQ_ID");
		insertFields.add("FK_ACK_FILE_ID");
		insertFields.add("FK_DOC_ID");
		insertFields.add("FILE_REL_PATH");
		insertFields.add("B_PROCESSED");
		insertFields.add("B_PULL_OK");
		insertFields.add("FILE_NAME");
		insertFields.add("FK_FILE_TYP_ID");
		
		String clnAbbrev = "AUTOTEST100";
		
		List <Object> insertValues= new ArrayList<>();
		String fileSeq = iGenericDaoPlatform.getNextSeqIdFromDUALBySequenceId("CORRESP_FILE_SEQ",testDb);
		String fileName = "COMBINED_CLIENT_ANNUAL_DISCLOSURE_" + timeStamp.getTimeStamp() + ".pdf.gz";
		//SS# = 1720 (Client Annual Disclosure Letters outbound extension to the root directory)
		String filePath = systemDao.getSystemSetting(1720).getDataValue();
		insertValues.add(Integer.parseInt(fileSeq));		
		insertValues.add(0);
		insertValues.add(0);		
		insertValues.add(0);
		insertValues.add(filePath);
		insertValues.add(0);
		insertValues.add(0);
		insertValues.add(fileName);
		insertValues.add(11);				
		int num = iGenericDaoPlatform.addRecordIntoTable("CORRESP_FILE", insertFields, insertValues, testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that a new record is added into CORRESP_FILE table");
		assertTrue(num > 0, "        A new record should be added into CORRESP_FILE table.");
		
		logger.info("*** Step 3 ActionS: - Create a new record in CLN_CORRESP_HIST table with: pk_file_seq_id=Corresp_File.Pk_file_Seq.");
		int clnId = clientDao.getClnByClnAbbrev(clnAbbrev).getClnId();
		
		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_FILE_SEQ_ID");
		insertFields.add("B_ACK_REQ");
		insertFields.add("B_ACK_REC");
		
		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(Integer.parseInt(fileSeq));
		insertValues.add(0);
		insertValues.add(0);
		num = iGenericDaoPlatform.addRecordIntoTable("CLN_CORRESP_HIST", insertFields, insertValues, testDb);

		logger.info("*** Step 3 Expected Results: - Verify that a new record is added into CLN_CORRESP_HIST table");
		assertTrue(num > 0, "        A new record should be added into CLN_CORRESP_HIST table.");
		
		logger.info("*** Step 4 Action: Add new record in Cln_adl_stage with: Pk_cln_id= cln_corresp_hist.pk_cln_id. Pk_test_id is profile test.");
		List<String> profTestId = daoManagerXifinRpm.getProfileTestFromTESTByHavingMaxTwoComponents(testDb);		
		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_TEST_ID");		
		
		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(profTestId.get(0));
		num = iGenericDaoPlatform.getRecordFromTable("CLN_ADL_STAGE", insertFields, insertValues, testDb)==0?iGenericDaoPlatform.addRecordIntoTable("CLN_ADL_STAGE", insertFields, insertValues, testDb):1;


		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into CLN_ADL_STAGE table");
		assertTrue(num > 0, "        A new record should be added into CLN_ADL_STAGE table.");		
		
		logger.info("*** Step 5 Actions:  - Create a new record in pal_cln_test_prc with: Pk_cln_id = cln_corresp_hist.Pk_Cln_id. Pk_test_id=cln_adl_stage.pk_test_id.");
		//PRC_ABBREV = 'AETNA'				
		List<Prc> prcs = prcDao.getPrcByPrcAbbrev("AETNA");

		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_PRC_ID");
		insertFields.add("PK_TEST_ID");

		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(prcs.get(0).prcId);
		insertValues.add(profTestId.get(0));
		num=iGenericDaoPlatform.getRecordFromTable("PAL_CLN_TEST_PRC", insertFields, insertValues, testDb);
		insertFields.add("FK_FILE_SEQ_ID");
		insertValues.add(Integer.parseInt(fileSeq));
		num = num==0?iGenericDaoPlatform.addRecordIntoTable("PAL_CLN_TEST_PRC", insertFields, insertValues, testDb):num;

		logger.info("*** Step 5 Expected Results: - Verify that a new record is added into PAL_CLN_TEST_PRC table");
		assertTrue(num > 0, "        A new record should be added into PAL_CLN_TEST_PRC table.");	
		
		logger.info("*** Step 6 Actions: - Run PF-Client Correspondence Engine");
		clientDao.waitForClientCorrespEng(Integer.parseInt(fileSeq),QUEUE_WAIT_TIME_MS);
		
		logger.info("*** Step 6 Expected Results: - Verify that a Combined Annual Disclosure Letter is generated");		
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		File fOut = new File(dirBase + fileName);		
		Assert.assertTrue(isFileExists(fOut, 5), "        Combined Annual Disclosure Letter: " + fileName + " should be generated under " + dirBase + " folder.");

		logger.info("*** Step 6 Expected Results: - Verify that the Corresp_File.Creat_Dt is updated to Sysdate");
		CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(Integer.parseInt(fileSeq));
		assertNotNull(correspFile.getCreatDt(), "        Corresp_File.Creat_Dt should not be null ");
		String[] fList = fileName.split("[.]");
		String pdfFileName = fList[0] + ".pdf";		

		logger.info("*** Step 7 Actions: - Decompress the generated GZ file");
		convertUtil.decompressGzipFile(fileName, dirBase);
		Thread.sleep(3000);
		File unZippedFile = new File(dirBase + pdfFileName);		
		Assert.assertTrue(isFileExists(unZippedFile, 5), "        Unzipped Combined ADL file: " + pdfFileName + " should be generated under " + dirBase + " folder.");

		logger.info("*** Step 7 Expected Results: - Verify that the Combined ADL letter header and the profile data are printed in the PDF");
		
		String profTestAbbev = profTestId.get(1);		
		List<com.mbasys.mars.ejb.entity.test.Test> comps = testDao.getProfTestCompFromTestByTestAbbrev(profTestAbbev);
		
		String pdfTextPg1 = convertUtil.getTextFromPdf(1, 1, dirBase + pdfFileName);
		String header = "Dear Doctor";
		assertTrue(pdfTextPg1.contains(header),"        The Combined ADL letter header " + header + " is printed in the PDF.");
		String content = "annual notification";
		assertTrue(pdfTextPg1.contains(content),"        The Combined ADL letter part of contents " + content + " is printed in the PDF.");
		
		String pdfTextPg2 = convertUtil.getTextFromPdf(2, 2, dirBase + pdfFileName);
		assertTrue(pdfTextPg2.contains(profTestAbbev),"        The Profile test " + profTestAbbev + " is printed in the PDF.");
		
		for( com.mbasys.mars.ejb.entity.test.Test comp:comps){
			String compName = comp.getName();
			assertTrue(pdfTextPg2.contains(compName),"        The profile test " + profTestAbbev + " component name " + compName + " is printed in the PDF.");
		}				
						
		logger.info("*** Step 8 Actions: - Clear test data");		
		fileManipulation.deleteFile(dirBase, fileName);		
		fileManipulation.deleteFile(dirBase, pdfFileName);	
		
		//delete data
		for (CorrespFile strings : oldCorrespFile) {
			iGenericDaoPlatform.setValuesFromTableByColNameValue("Corresp_File", "CREAT_DT = ''", "PK_FILE_SEQ", String.valueOf(strings.getFileSeq()), testDb);
		}
		
		iGenericDaoPlatform.deleteDataFromTableByCondition("Cln_Corresp_Hist","PK_FILE_SEQ_ID="+fileSeq,testDb);
		iGenericDaoPlatform.deleteDataFromTableByCondition("Pal_Cln_Test_Prc","FK_FILE_SEQ_ID="+fileSeq,testDb);
		iGenericDaoPlatform.deleteDataFromTableByCondition("Cln_Adl_Stage","Pk_Cln_Id="+clnId,testDb);
		iGenericDaoPlatform.deleteDataFromTableByCondition("corresp_file","PK_FILE_SEQ="+fileSeq,testDb);
	}
	
	@Test(priority = 1, description = "File_Typ_Id=11 and Creat_Dt is null and Pal_Cln_Test_Prc.size()=0")
	@Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType"})
	public void testPFER_481(String email, String password, String eType, String xapEnv, String engConfigDB, String formatType) throws Exception {
		logger.info("====== Testing - PFER_481 ======");		
		
		timeStamp=new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		fileManipulation = new FileManipulation(driver);
		convertUtil = new ConvertUtil();
		iGenericDaoPlatform = new DaoManagerPlatform(config.getRpmDatabase());
		logger.info("*** Step 1 Actions: - Update records in CORRESP_FILE so they can't be processed by the Engine");
		List<CorrespFile> oldCorrespFile = clientDao.getCorrespFileByCreateDtIsNullFileSeqNotNull();
		String currDt = timeStamp.getCurrentDate("MM/dd/yyyy");

		for (CorrespFile strings : oldCorrespFile) {
			iGenericDaoPlatform.setValuesFromTableByColNameValue("Corresp_File", "CREAT_DT = to_date('" + currDt + "','MM/dd/yyyy')", "PK_FILE_SEQ", String.valueOf(strings.getFileSeq()), testDb);
		}
		
		logger.info("*** Step 2 Actions: - Create a new record in CORRESP_FILE with Fk_file_typ_id = 11 and Creat_dt = null");
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_FILE_SEQ");
		insertFields.add("FK_SUBM_SVC_SEQ_ID");
		insertFields.add("FK_ACK_FILE_ID");
		insertFields.add("FK_DOC_ID");
		insertFields.add("FILE_REL_PATH");
		insertFields.add("B_PROCESSED");
		insertFields.add("B_PULL_OK");
		insertFields.add("FILE_NAME");
		insertFields.add("FK_FILE_TYP_ID");
		
		String clnAbbrev = "AUTOTEST100";
		
		List <Object> insertValues= new ArrayList<>();
		int fileSeq = Integer.parseInt(iGenericDaoPlatform.getNextSeqIdFromDUALBySequenceId("CORRESP_FILE_SEQ",testDb));
		String fileName = "COMBINED_CLIENT_ANNUAL_DISCLOSURE_" + timeStamp.getTimeStamp() + ".pdf.gz";
		//SS# = 1720 (Client Annual Disclosure Letters outbound extension to the root directory)
		String filePath = systemDao.getSystemSetting(1720).getDataValue();
		insertValues.add(fileSeq);
		insertValues.add(0);
		insertValues.add(0);		
		insertValues.add(0);
		insertValues.add(filePath);
		insertValues.add(0);
		insertValues.add(0);
		insertValues.add(fileName);
		insertValues.add(11);				
		int num = iGenericDaoPlatform.addRecordIntoTable("CORRESP_FILE", insertFields, insertValues, testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that a new record is added into CORRESP_FILE table");
		assertTrue(num > 0, "        A new record should be added into CORRESP_FILE table.");
		
		logger.info("*** Step 3 Actions: - Create a new record in CLN_CORRESP_HIST table with: pk_file_seq_id=Corresp_File.Pk_file_Seq.");		
		int clnId = clientDao.getClnByClnAbbrev(clnAbbrev).getClnId();
		
		insertFields.clear();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_FILE_SEQ_ID");
		insertFields.add("B_ACK_REQ");
		insertFields.add("B_ACK_REC");
		
		insertValues.clear();
		insertValues.add(clnId);
		insertValues.add(fileSeq);
		insertValues.add(0);
		insertValues.add(0);
		num = iGenericDaoPlatform.addRecordIntoTable("CLN_CORRESP_HIST", insertFields, insertValues, testDb);
		
		logger.info("*** Step 3 Expected Results: - Verify that a new record is added into CLN_CORRESP_HIST table");
		assertTrue(num > 0, "        A new record should be added into CLN_CORRESP_HIST table.");

		logger.info("*** Step 4 Actions: - Run PF-Client Correspondence Engine");
		xifinAdminUtils.runPFEngine(this, email, password, xapEnv, eType, engConfigDB, false);
		
		logger.info("*** Step 4 Expected Results: - Verify that a Combined Annual Disclosure Letter is generated");		
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		File fOut = new File(dirBase + fileName);		
		Assert.assertTrue(isFileExists(fOut, 5), "        Combined Annual Disclosure Letter: " + fileName + " should be generated under " + dirBase + " folder.");

		logger.info("*** Step 4 Expected Results: - Verify that the records in CORRESP_FILE and CLN_CORRESP_HIST tables are removed");
		CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(fileSeq);
		Assert.assertNull(correspFile, "        The record for CORRESP_FILE.PK_FILE_SEQ = " + fileSeq + " should be removed.");
		
		List<ClnCorrespHist> clnCorrespHist = clientDao.getClnCorrespHistByFileSeq(fileSeq);
		assertEquals(clnCorrespHist.size(), 0, "        The record for CLN_CORRESP_HIST.PK_FILE_SEQ_id = " + fileSeq + " should be removed.");
						
		logger.info("*** Step 5 Actions: - Clear test data");		
		fileManipulation.deleteFile(dirBase, fileName);		

	}
	
}
