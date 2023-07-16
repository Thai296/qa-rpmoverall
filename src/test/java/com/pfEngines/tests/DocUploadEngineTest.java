package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.docStore.DocStore;
import com.mbasys.mars.ejb.entity.docUploadEngineErr.DocUploadEngineErr;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.utility.cache.CacheMap;
import com.overall.utils.DocStoreUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.sso.orgconfigs.constants.OrganizationConfigs;
import com.xifin.util.StringUtils;
import com.xifin.utils.ClearCacheUtil;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TimeStamp;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

public class DocUploadEngineTest extends SeleniumBaseTest
{
	private static final String UPLOAD_BY = "DocumentUploadEngine";
	private static final String AUD_USER = "engine~DocUploadEngine";

	private static final String DOC_STORE_TYP_ABBREV_CLNSTMT = "clnstmt";
	private static final String DOC_STORE_TYP_ABBREV_XIFIN_PTSTMT = "xifinptstmt";
	private static final String DOC_STORE_TYP_3RD_PARTY_NON_PTSTMT = "thirdpartynonptstmt";
	private static final String DOC_STORE_TYP_ABBREV_ACK_FILE = "ackfile";
	private static final String DOC_STORE_TYP_835_FILE = "835";
	private static final String DOC_STORE_TYP_DISCREPANCY = "discrepancyreport";
	private static final String DOC_STORE_TYP_EOB_REPORT = "eobreport";

	private DocStoreUtils docStoreUtils;

	@BeforeSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
	public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
	{
		try
		{
			logger.info("Running BeforeSuite");
			// Disable excess Selenium logging
			java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(java.util.logging.Level.OFF);
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			logIntoSso(ssoUsername, ssoPassword);
			ssoDao.updateOrganizationConfigurationDataValue(config.getProperty(PropertyMap.ORGALIAS), String.valueOf(OrganizationConfigs.DOC_STORE.UPLOAD_TYPE.getId()), "D,F,M");
			ClearCacheUtil clearCacheUtil = new ClearCacheUtil(orgAlias, config.getProperty(PropertyMap.XIFINADMINPORTAL_URL));
			clearCacheUtil.clearCache(CacheMap.SYSTEM_SETTINGS);
			logger.info("Cleared cache");
			driver.close();
		}
		catch (SkipException e)
		{
			logger.warn("Skipped exception thrown during BeforeSuite action", e);
		}
		catch (Exception e)
		{
			throw new SkipException("Error running BeforeSuite", e);
		}
		finally
		{
			quitWebDriver();
		}
	}

	@BeforeTest(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins", "fileName"})
	public void beforeTest(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword,
						   String disableBrowserPlugins, @Optional String fileName)
	{
		try
		{
			logger.info("Running BeforeTest");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			docStoreUtils = new DocStoreUtils(docStoreDao);
			logger.info("Running BeforeTest - Clearing previous files, fileName=" + fileName);
			List<String> fileNames = Arrays.asList(StringUtils.split(fileName, "|"));
			for (String fileNameStr : fileNames)
			{
				cleanUpDocStore(fileNameStr);
			}
		}
		catch (Exception e)
		{
			Assert.fail("Error running BeforeTest", e);
		}
		finally
		{
			quitWebDriver();
		}
	}

	@Test(priority = 1, description = "DOCUMENT-ContentType=text/plain, doc_category=Accession")
	@Parameters({"fileName", "contentType", "docCategory", "docCategoryId", "docStoreTyp", "isInProcess"})
	public void testPFER_145(String fileName, String contentType, String docCategory, String docCategoryId, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_145 ======");

		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Create a text upload document and place to the inbound folder");
		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();
		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator;
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator;

		// Write file to the Inbound folder
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);

		logger.info("*** Expected Results: - Verify that the new file is generated in Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		Assert.assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be generated under " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database");
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "DOCUMENT-Invalid Doc Type")
	@Parameters({"fileName"})
	public void testPFER_146(String fileName) throws Exception
	{
		logger.info("====== Testing - testPFER_146 ======");

		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Update the Document Upload Indexing Type to D (all files) in SSO DB");
		logger.info("*** Action: - Create an invalid doc type (.zip) upload document");
		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();

		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator;
		String filePathError = dirBaseInbound + "from_xifin" + File.separator + "image_load_errors" + File.separator;

		//Write file to the Inbound folder
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);

		logger.info("*** Expected Results: - Verify that the new file is generated in Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		Assert.assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be generated under " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that the invalid file is moved to the error folder");
		File fError = new File(filePathError + fileName);
		assertTrue(isFileExists(fError, 5), "        Upload file: " + fileName + " should be moved to " + filePathError + " folder.");

		logger.info("*** Expected Results: - Verify that a new record is added in DOC_UPLOAD_ENGINE_ERR table");
		List<DocUploadEngineErr> docUploadEngineErrList = rpmDao.getDocUploadEngineErrByFileName(testDb, fileName);
		Assert.assertFalse(docUploadEngineErrList.isEmpty(), "DocUploadEngineErr was not created");
		Assert.assertEquals(docUploadEngineErrList.get(0).getErrReason().trim(), "The file extension is not in the white list", "       'The file extension is not in the white list' should be saved into DOC_UPLOAD_ENGINE_ERR.ERR_REASON.");
		Assert.assertEquals(docUploadEngineErrList.get(0).getAudUser(), AUD_USER, "DocUploadErr AudUser is not correct");

		String errDt = docUploadEngineErrList.get(0).getErrDt().toString();
		String sysDt = timeStamp.getCurrentDate("yyyy-MM-dd");
		Assert.assertEquals(errDt, sysDt, "       " + sysDt + " should be saved into DOC_UPLOAD_ENGINE_ERR.ERR_DT.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathError, fileName);
	}

	@Test(priority = 1, description = "DOCUMENT-File contains duplicate contents")
	@Parameters({"fileName", "fileName2", "contentType", "docCategory", "docCategoryId", "docStoreTyp", "isInProcess"})
	public void testPFER_147( String fileName, String fileName2, String contentType, String docCategory, String docCategoryId, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_147 ======");
		cleanUpDocStore(fileName2);
		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Update the Document Upload Indexing Type to D (all files) in SSO DB");
		logger.info("*** Action: - Create a text upload document");
		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();

		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator;
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator;

		//Write file to the Inbound folder
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);

		logger.info("*** Expected Results: - Verify that the new file is generated in Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		Assert.assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be generated under " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database");
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		Assert.assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		Assert.assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Create a new text upload document with the duplicate contents");
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName2);

		logger.info("*** Expected Results: - Verify that the dup content file is generated in Inbound folder");
		File fInbound2 = new File(filePathInbound + fileName2);
		Assert.assertTrue(isFileExists(fInbound2, 5), "        Upload file: " + fileName2 + " should be generated under " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the dup content file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound2);
		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database");
		List<DocStore> docStoreInfoList2 = rpmDao.getDocStoreRecordsByFileName(testDb, fileName2);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName2);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName2 = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName2);

		logger.info("*** Expected Results: - Verify that the dup content file is uploaded and moved to Uploaded folder");
		File fUploaded2 = new File(filePathUploaded + encryptedFileName2);
		Assert.assertTrue(isFileExists(fUploaded2, 5), "        Upload file: " + encryptedFileName2 + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the dup content file is moved to Completed folder");
		File fCompleted2 = new File(filePathCompleted + fileName2);
		Assert.assertTrue(isFileExists(fCompleted2, 5), "        Upload file: " + fileName2 + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "FOLDER-DocTyp=doc, DocCategory=Accession")
	@Parameters({"fileName", "contentType", "docCategory", "docCategoryId", "docStoreTyp", "isInProcess"})
	public void testPFER_163(String fileName, String contentType, String docCategory, String docCategoryId, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_163 ======");
		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Create a text upload document (.doc)");
		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();

		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator + docStoreTyp + File.separator;
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator + docStoreTyp + File.separator;

		// Write file to the Inbound folder
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);

		logger.info("*** Expected Results: - Verify that the new file is generated in Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be generated under " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database");
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "DOCUMENT-ContentType=pdf, doc_category=Client")
	@Parameters({"fileName", "contentType", "docCategory", "docCategoryId", "docStoreTyp", "isInProcess"})
	public void testPFER_550(String fileName, String contentType, String docCategory, String docCategoryId, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_550 ======");

		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Copy a PDF file to the upload folder");
		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator;

		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);

		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator;

		logger.info("*** Expected Results: - Verify that the file is copied to the Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be copied to " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database");
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "DOCUMENT-ContentType=jpg, doc_category=Payor")
	@Parameters({"fileName", "contentType", "docStoreTyp", "isInProcess"})
	public void testPFER_551(String fileName, String contentType, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_551 ======");

		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Copy a JPG file to the upload folder");
		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator;

		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);

		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator;

		logger.info("*** Expected Results: - Verify that the file is copied to the Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be copied to " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database");
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), null, null, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "DOCUMENT-ContentType=xlsx, doc_category=Accession")
	@Parameters({"fileName", "contentType", "docCategory", "docCategoryId", "docStoreTyp", "isInProcess"})
	public void testPFER_552(String fileName, String contentType, String docCategory, String docCategoryId, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_552 ======");

		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Update the Document Upload Indexing Type to D (all files) in SSO DB");
		logger.info("*** Action: - Copy a XLSX file to the upload folder");
		String fileNameCopy = "DocUploadEngineTest" + ".xlsx";

		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator;

		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);

		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator;

		logger.info("*** Expected Results: - Verify that the file is copied to the Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be copied to " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database");
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "DOCUMENT-ContentType=docx, doc_category=Accession")
	@Parameters({"fileName", "contentType", "docCategory", "docCategoryId", "docStoreTyp", "isInProcess"})
	public void testPFER_553(String fileName, String contentType, String docCategory, String docCategoryId, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_553 ======");

		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Copy a DOCX file to the upload folder");
		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator;

		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);

		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator;

		logger.info("*** Expected Results: - Verify that the file is copied to the Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be copied to " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database");
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "DOCUMENT-ContentType=gif, doc_category=Accession")
	@Parameters({"fileName", "contentType", "docCategory", "docCategoryId", "docStoreTyp", "isInProcess"})
	public void testPFER_554(String fileName, String contentType, String docCategory, String docCategoryId, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_554 ======");

		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Copy a GIF file to the upload folder");
		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator;

		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);

		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator;

		logger.info("*** Expected Results: - Verify that the file is copied to the Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be copied to " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database");
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "METADATA-Upload file CONTENT_TYPE = text/plain and DOC_CATEGORY = Client")
	@Parameters({"fileName", "contentType", "docCategory", "docCategoryId", "docStoreTyp", "comment", "isInProcess"})
	public void testPFER_152(String fileName, String contentType, String docCategory, String docCategoryId, String docStoreTyp, String comment, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_152 ======");

		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		Assert.assertTrue(fileName.startsWith("metadata"), "FileName must start with 'metadata', fileName="+fileName);

		logger.info("*** Action: - Generate and write the Metadata (.csv) and Text (.txt) files into the upload folder");
		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator;

		comment = comment + "-" + timeStamp.getTimeStamp();
		String metadataFileContent = docStoreUtils.generateMetadataFile(fileName, docStoreTyp, docCategory, docCategoryId, comment);

		// Write Metadata file to the Inbound folder
		fileManipulation.writeFileToFolder(metadataFileContent, filePathInbound, fileName);

		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator;

		logger.info("*** Expected Results: - Verify that the metadata file is in Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		// Verify that Metadata file exists
		assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be copied to " + filePathInbound + " folder.");


		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database");
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyp, contentType, comment, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "FOLDER-DocTyp=doc, No Category and Category ID, still process file")
	@Parameters({"fileName", "contentType", "docStoreTyp", "isInProcess"})
	public void testPFER_749(String fileName, String contentType, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_749 ======");

		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Create a text upload document (.doc)");
		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();

		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator + docStoreTyp + File.separator;
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator + docStoreTyp + File.separator;

		// Write file to the Inbound folder
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);

		logger.info("*** Expected Results: - Verify that the new file is generated in Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be generated under " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database, filename="+fileName);
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), null, null, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "FOLDER-DocTyp=doc-ACCN Category and Category ID - AccnId does not exist, still process file ")
	@Parameters({"fileName", "contentType", "docCategory", "docCategoryId", "docStoreTyp", "isInProcess"})
	public void testPFER_750(String fileName, String contentType, String docCategory, String docCategoryId, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testPFER_750 ======");

		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Create a text upload document (.doc)");
		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();

		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploadInbound");
		String dirBaseUploaded = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "docUploaded");
		String filePathInbound = dirBaseInbound + "to_xifin" + File.separator + docStoreTyp + File.separator;
		String filePathUploaded = dirBaseUploaded + File.separator + "docstore" + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + timeStamp.getCurrentDate("yyyyMM") + File.separator;
		String filePathCompleted = dirBaseUploaded + File.separator + "docstoreprocessed" + File.separator + docStoreTyp + File.separator;

		// Write file to the Inbound folder
		fileManipulation.writeFileToFolder(textInput, filePathInbound, fileName);
		String accnId = fileName.replaceAll("\\D+","");
		logger.info("*** Expected Results: - Verify that the new file is generated in Inbound folder");
		File fInbound = new File(filePathInbound + fileName);
		assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileName + " should be generated under " + filePathInbound + " folder.");

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);

		logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database, filename="+fileName);
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);

		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, isInProcess, false, true);

		String encryptedFileName = docStoreInfoList.get(0).getRepositoryUri().split("/")[2];
		logger.info("        The Encrypted File name is " + encryptedFileName);

		logger.info("*** Expected Results: - Verify that the file is uploaded and moved to Uploaded folder");
		File fUploaded = new File(filePathUploaded + encryptedFileName);
		assertTrue(isFileExists(fUploaded, 5), "        Upload file: " + encryptedFileName + " should be moved to " + filePathUploaded + " folder.");

		logger.info("*** Expected Results: - Verify that the file is moved to Completed folder");
		File fCompleted = new File(filePathCompleted + fileName);
		assertTrue(isFileExists(fCompleted, 5), "        Upload file: " + fileName + " should be moved to " + filePathCompleted + " folder.");

		logger.info("*** Action: - Clear test data");
		fileManipulation.deleteFile(filePathUploaded, encryptedFileName);
		fileManipulation.deleteFile(filePathCompleted, fileName);
	}

	@Test(priority = 1, description = "Migrate 835 and EOB Files to Doc Store")
	@Parameters({"fileName", "contentType", "docCategory", "docCategoryId", "docStoreTyp", "isInProcess"})
	public void testMigrate835sAndDepositPdfs(String fileName, String contentType, String docCategory, String docCategoryId, String docStoreTyp, boolean isInProcess) throws Exception
	{
		logger.info("====== Testing - testMigrate835sAndDepositPdfs ======");

		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("*** Action: - Create a text upload document (.txt)");
		String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();

		String dirBaseInbound = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "X12");
		String filePathInbound = dirBaseInbound + "processed" + File.separator + "ngs" + File.separator + "835" + File.separator;

		List<String> fileNames = Arrays.asList(StringUtils.split(fileName, "|"));
		List<String> contentTypes = Arrays.asList(StringUtils.split(contentType, "|"));

		File fInbound = null;

		for (String fileNameStr : fileNames)
		{
			// Write file to the X12/[payor]/835 folder
			fileManipulation.writeFileToFolder(textInput, filePathInbound, fileNameStr);

			logger.info("*** Expected Results: - Verify that the new file is generated in Inbound folder");
			fInbound = new File(filePathInbound + fileNameStr);
			assertTrue(isFileExists(fInbound, 5), "        Upload file: " + fileNameStr + " should be generated under " + filePathInbound + " folder.");
		}

		logger.info("Wait until PF Document Upload Engine processes the file");
		waitUntilDocUploadEngMovesFileFromIncoming(fInbound);
		List<String> docStoreTyps = Arrays.asList(StringUtils.split(docStoreTyp, "|"));
		int i = 0;
		for (String fileNameStr : fileNames)
		{
			logger.info("*** Expected Results: - Verify that a new row is added into DOC_STORE table in database, filename=" + fileNameStr);
			List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileNameStr);
			Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileNameStr);
			docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, docCategoryId, docStoreTyps.get(i), contentTypes.get(i), null, UPLOAD_BY, AUD_USER, isInProcess, false, false);
			i++;
		}
	}

	@Test(priority = 1, description = "Migrate Non-Client Statement File to Doc Store")
	@Parameters({"fileName", "contentType", "docCategory", "docStoreTyp"})
	public void testMigrateNonClientStatementFile(String fileName, String contentType, String docCategory, String docStoreTyp) throws Exception
	{
		RandomCharacter randomCharacter = new RandomCharacter(driver);
		TimeStamp timeStamp = new TimeStamp(driver);
		FileManipulation fileManipulation = new FileManipulation(driver);

		logger.info("message=Verifying SubmFile record, filename="+fileName);
		SubmFile submFile = submissionDao.getSubmFileByFilename(fileName);
		Assert.assertTrue(submFile.getIsEgateProcessed(), "Non-Client Statement file should be e-gate processed, submFileSeqId="+submFile.getSubmFileSeqId()+", filename="+submFile.getFilename());

		File localStatementFile = new File(FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + submFile.getDir() + File.separator + submFile.getFilename());
		logger.info("message=Verifying local statement file exists, filename="+fileName+", localStatementFile="+localStatementFile);
		if (!localStatementFile.exists())
		{
			logger.info("message=Creating new local statement file, filename="+fileName+", localStatementFile="+localStatementFile);
			String textInput = randomCharacter.getRandomNumericString(8) + timeStamp.getTimeStamp();
			fileManipulation.writeFileToFolder(textInput, localStatementFile.getParent()+File.separator, fileName);
		}
		Assert.assertTrue(localStatementFile.exists(), "Local statement file should exist, localStatementFile="+localStatementFile);

		logger.info("message=Waiting for DocUploadEngine to process the file, filename="+fileName+", localStatementFile="+localStatementFile);
		waitUntilDocUploadEngMovesFileFromIncoming(localStatementFile);
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);
		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, String.valueOf(submFile.getSubmFileSeqId()), docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, false, false, false);
		Assert.assertFalse(localStatementFile.exists(), "Local statement file should no longer exist, localStatementFile="+localStatementFile);
	}

	@Test(priority = 1, description = "Migrate Patient Statement File to Doc Store")
	@Parameters({"fileName", "contentType", "docCategory", "docStoreTyp"})
	public void testMigratePatientStatementFile(String fileName, String contentType, String docCategory, String docStoreTyp) throws Exception
	{
		logger.info("message=Verifying SubmFile record, filename="+fileName);
		SubmFile submFile = submissionDao.getSubmFileByFilename(fileName);
		Assert.assertTrue(submFile.getIsEgateProcessed(), "Patient Statement file should be e-gate processed, submFileSeqId="+submFile.getSubmFileSeqId()+", filename="+submFile.getFilename());

		File localStatementFile = new File(FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + submFile.getDir() + File.separator + submFile.getFilename());
		logger.info("message=Verifying local statement file exists, filename="+fileName+", localStatementFile="+localStatementFile);
		if (!localStatementFile.exists())
		{
			logger.info("message=Copying test file to patient statement directory, filename="+fileName+", localStatementFile="+localStatementFile);
			String testFilePath = "/testData/docUploadEngine/" + fileName;
			URL testFileURL = getClass().getResource(testFilePath);
			Assert.assertNotNull(testFileURL, "Patient statement test file URL should not be null, testFilePath="+testFilePath);
			File testFile = new File(testFileURL.getFile());
			Assert.assertTrue(testFile.exists(), "Patient statement test file should exist, testFile="+testFile.getAbsolutePath());
			FileUtils.copyFile(testFile, localStatementFile);
		}
		Assert.assertTrue(localStatementFile.exists(), "Local statement file should exist, localStatementFile="+localStatementFile);

		logger.info("message=Waiting for DocUploadEngine to process the file, filename="+fileName+", localStatementFile="+localStatementFile);
		waitUntilDocUploadEngMovesFileFromIncoming(localStatementFile);
		List<DocStore> docStoreInfoList = rpmDao.getDocStoreRecordsByFileName(testDb, fileName);
		Assert.assertEquals(docStoreInfoList.size(), 1, "Expected 1 document, filename=" + fileName);
		docStoreUtils.verifyDocStore(docStoreInfoList.get(0), docCategory, String.valueOf(submFile.getSubmFileSeqId()), docStoreTyp, contentType, null, UPLOAD_BY, AUD_USER, false, false, false);
		Assert.assertFalse(localStatementFile.exists(), "Local statement file should no longer exist, localStatementFile="+localStatementFile);
	}

	private void waitUntilDocUploadEngMovesFileFromIncoming(File incomingFile) throws InterruptedException
	{
		long startTime = System.currentTimeMillis();
		long maxTime = startTime + QUEUE_WAIT_TIME_MS;
		while (incomingFile.exists() && System.currentTimeMillis() < maxTime)
		{
			logger.info("Waiting for DocUpload to process file, file=" + incomingFile.getAbsolutePath() + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME_MS);
		}
		Thread.sleep(QUEUE_POLL_TIME_MS);
	}

	private void cleanUpDocStore(String fileName) throws Exception
	{
		for (DocStore docStore : rpmDao.getDocStoreRecordsByFileName(testDb, fileName))
		{
			logger.info("Clearing DocStoreAudit, docStore.getSeqId()=" + docStore.getSeqId());
			rpmDao.deleteDocStoreAuditByDocStoreId(docStore.getSeqId());
			if (docStore.getDocStoreTyp() == docStoreDao.getDocStoreTypByAbbrev(DOC_STORE_TYP_835_FILE).getSeqId())
			{
				paymentDao.clearX12InterchangeAndDepBy835DocStoreId(docStore.getSeqId());
			}
			else if (docStore.getDocStoreTyp() == docStoreDao.getDocStoreTypByAbbrev(DOC_STORE_TYP_3RD_PARTY_NON_PTSTMT).getSeqId()
					|| docStore.getDocStoreTyp() == docStoreDao.getDocStoreTypByAbbrev(DOC_STORE_TYP_ABBREV_XIFIN_PTSTMT).getSeqId())
			{
				SubmFile submFile = submissionDao.getSubmFileByFilename(fileName);
				submFile.setDocStoreSeqId(null);
				databaseSequenceDao.setValueObject(submFile);
			}
		}
		logger.info("Clearing DocStore, fileName=" + fileName);
		rpmDao.deleteDocUploadEngineErrByFileName(null, fileName);
		rpmDao.deleteDocStoreIndexByFileName(fileName);
		rpmDao.deleteDocStoreByFileName(fileName);
	}
}
