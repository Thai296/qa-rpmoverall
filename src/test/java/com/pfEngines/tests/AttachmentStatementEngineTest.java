package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.attachFile.AttachFile;
import com.mbasys.mars.ejb.entity.attachReq.AttachReq;
import com.mbasys.mars.ejb.entity.docStore.DocStore;
import com.mbasys.mars.ejb.entity.docStoreIndex.DocStoreIndex;
import com.mbasys.mars.ejb.entity.docStoreTyp.DocStoreTyp;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.qAccnSubmAttach.QAccnSubmAttach;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submSvc.SubmSvc;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.fileMaintenance.sysMgt.DocumentUploadAndStorage;
import com.overall.menu.MenuNavigation;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.TestDataSetup;
import com.xifin.xap.utils.XifinAdminUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.overall.fileMaintenance.sysMgt.DocumentUploadAndStorage.PAGE_TITLE;

public class AttachmentStatementEngineTest extends SeleniumBaseTest
{
	private static final String OS_NAME = System.getProperty("os.name");
	private static final String WINDOWS = "windows";

	private static final String MIME_TYPE_PDF = "application/pdf";
	private static final int DOC_STORE_INDEX_TYPE_SYSTEM = 5;
	private static final String DOC_CATEGORY_ID_PREFIX_ATTACH = "ATTACH";
	private static final int DOC_STORE_TYP_ATTACH = 85;
	private static final int DOC_STORE_SCAN_STATUS_INTERNAL_DOCUMENT = 1;
	private static final int DOC_STORE_LOCATION_S3 = 2;
	private static final String DOC_STORE_URI_PREFIX_S3 = "S3://";

	public static long QUEUE_POLL_TIME = TimeUnit.SECONDS.toMillis(5);
	public static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(10);

	@BeforeSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
	public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
	{
		try
		{
			logger.info("Running BeforeSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			cleanUpAttachmentFiles(6025);
			logIntoSso(ssoUsername, ssoPassword);
			//Clear Cache
			XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
			xifinAdminUtils.clearDataCache();
		}
		catch (Exception e)
		{
			Assert.fail("Error running BeforeSuite", e);
		}
		finally
		{
			quitWebDriver();
		}
	}

	@BeforeMethod(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
	public void beforeMethod(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String disableBrowserPlugins)
	{
		try
		{
			logger.info("Running Before Method");
			logIntoSso(ssoUsername, ssoPassword);
		}
		catch (Exception e)
		{
			Assert.fail("Error running Before Method", e);
		}
	}

	@Test(priority = 1, description = "Create Claim attachment for 5010 Statement using S3 document")
	@Parameters({"ssoUsername", "ssoPassword", "project", "testSuite", "testCase", "filename", "claimSubmSvc", "attachSubmSvc"})
	public void testPFER_454(String ssoUsername, String ssoPassword, String project, String testSuite, String testCase, String filename, String claimSubmSvc, String attachSubmSvc) throws Exception
	{
		logger.info("message=Getting DocStore records, filename=" + filename);
		List<DocStore> docStoreList = docStoreDao.getDocStoreList(filename, null);
		Assert.assertEquals(docStoreList.size(), 1);
		logger.info("message=Verify document DB fields");
		Assert.assertEquals(docStoreList.get(0).getDocCategory(), "Accession");
		Assert.assertTrue(docStoreList.get(0).getDocSize() > 0);
		Assert.assertFalse(docStoreList.get(0).getIsDeleted());
		Assert.assertEquals(docStoreList.get(0).getDocStoreLocationId(), 2);
		DocStoreTyp docStoreTyp = docStoreDao.getDocStoreTyp(docStoreList.get(0).getDocStoreTyp());

		logger.info("Sending WS request to create accession");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		Assert.assertTrue(StringUtils.isNotBlank(accnId), "Accession ID not found in WS response");
		Assert.assertTrue(isOutOfQFrPendingQueue(accnId, QUEUE_WAIT_TIME_MS*2), "Accession is not out of FR Pending Queue");
		Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME_MS), "Accession is not out of Eligibility Queue");
		Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");
		Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");

		List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
		Assert.assertFalse(accnPyrs.isEmpty());
		Pyr pyr = payorDao.getPyrByPyrId(accnPyrs.get(0).getPyrId());
		logger.info("message=Verify payor is configured for 275 claim attachments, pyrAbbrv="+pyr.getPyrAbbrv()+", attachSubmSvc="+attachSubmSvc);
		Assert.assertTrue(pyr.getAttachSubmSvcSeqId() > 0);
		SubmSvc submSvc = submissionDao.getSubmSvc(pyr.getAttachSubmSvcSeqId());
		Assert.assertEquals(submSvc.getAbbrev(), attachSubmSvc);

		logger.info("message=Linking DocStore item to accession, accnId="+accnId+", docStoreTypAbbrev="+docStoreTyp.getAbbrev()+", docStoreSeqId="+docStoreList.get(0).getSeqId());
		docStoreList.get(0).setDocCategoryId(accnId);
		docStoreDao.setDocStore(docStoreList.get(0));

		logger.info("message=Enabling attachment requirement for accn, accnId="+accnId+", docStoreTypAbbrev="+docStoreTyp.getAbbrev());
		MenuNavigation navigation = new MenuNavigation(driver, config);
		navigation.navigateToAccnDetailPage();
		AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
		accessionDetail.loadAccnOnAccnDetail(wait, accnId);
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
		// Clear EPI so we don't need to wait for Patient Demo Sweeper
		accessionDetail.enterEpiInput("");
		accessionDetail.setAttachmentType(docStoreTyp.getDescr());

		logger.info("message=Submitting claim, accnId="+accnId+", pyrAbbrev="+pyr.getPyrAbbrv()+", claimSubmSvc="+claimSubmSvc);
		accessionDetail.submitClaimsOnAccnDetail(accnId, pyr.getPyrAbbrv(), accnPyrs.get(0).getSubsId(), claimSubmSvc, wait, false, true);

		List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
		Assert.assertEquals(qasList.size(), 1);
		logger.info("message=Clear last submission date, submSvcAbbrev=" + claimSubmSvc);
		rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, claimSubmSvc).submSvcSeqId);
		logger.info("message=Wait for Non-Client Submission Engine to process the claim, accnId="+accnId+", docSeqId="+qasList.get(0).getDocSeqId());
		int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, claimSubmSvc);
		Assert.assertTrue(submFileSeqId > 0);
		logger.info("message=Wait for Non-Client Statement Engine to process the file, submFileSeqId="+submFileSeqId);
		boolean isFileProcessed = waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME);
		Assert.assertTrue(isFileProcessed);
		logger.info("message=Make sure no errors added to the accession, accnId="+accnId);
		Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);
		QAccnSubm claimQas = rpmDao.getQAccnSubm(null, qasList.get(0).getDocSeqId());
		SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
		logger.info("message=Make sure 1 attachment submission was created, accnId="+accnId);
		List<QAccnSubmAttach> qAccnSubmAttachList = submissionDao.getQAccnSubmAttachListByDocSeqId(claimQas.getDocSeqId());
		Assert.assertEquals(qAccnSubmAttachList.size(), 1);
		Assert.assertEquals(qAccnSubmAttachList.get(0).getAccnId(), claimQas.getAccnId());
		Assert.assertEquals(qAccnSubmAttachList.get(0).getOrigSubmFileSeqId(), claimQas.getSubmFileSeqId());
		Assert.assertTrue(qAccnSubmAttachList.get(0).getAttachControlSuffixId() > 0);
		Assert.assertEquals(qAccnSubmAttachList.get(0).getAttachSubmSvcSeqId(), pyr.getAttachSubmSvcSeqId());
		Assert.assertTrue(qAccnSubmAttachList.get(0).getAttachFileSeqId() > 0);
		logger.info("message=Verifying AttachFile record, seqId="+qAccnSubmAttachList.get(0).getAttachFileSeqId());
		AttachFile attachFile = submissionDao.getAttachFile(qAccnSubmAttachList.get(0).getAttachFileSeqId());
		Assert.assertEquals(attachFile.getAccnId(), accnId);
		Assert.assertEquals(attachFile.getDocStoreSeqId(), Integer.valueOf(docStoreList.get(0).getSeqId()));
		Assert.assertEquals(attachFile.getFilename(), filename);
		Assert.assertEquals(attachFile.getPyrId(), pyr.getPyrId());

		logger.info("message=Make sure 837 file is saved to the dir, dir="+submFile.getDir()+", filename="+submFile.getFilename());
		File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFile.getFilename());
		Assert.assertTrue(file.exists(), "Expected file, filename=" + submFile.getFilename());
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String data = bufferedReader.readLine();
		String attachmentControlId = claimQas.getAccnId()+"Z"+claimQas.getClaimIdSuffix()+"Y"+qAccnSubmAttachList.get(0).getAttachControlSuffixId();
		String pwkSegment = "PWK*I5*EL***AC*"+attachmentControlId+"~";
		logger.info("message=Make sure 837 contains the PWK segment, data="+pwkSegment);
		Assert.assertTrue(data.contains(pwkSegment));

		SubmFile attachSubmFile = waitForAttachmentStatementEngine(qAccnSubmAttachList.get(0), QUEUE_WAIT_TIME);
		logger.info("message=Verifying attachment SubmFile record");
		Assert.assertNotNull(attachSubmFile);
		Assert.assertTrue(attachSubmFile.getIsEgateProcessed());
		Assert.assertTrue(DateUtils.isSameDay(attachSubmFile.getFileCreatDt(), new Date(System.currentTimeMillis())));
		Assert.assertNotNull(attachSubmFile.getFilename());
		Assert.assertNotNull(attachSubmFile.getDocStoreSeqId());
		Assert.assertTrue(attachSubmFile.getDocStoreSeqId() > 0);

		DocStore attachDocStore = docStoreDao.getDocStore(attachSubmFile.getDocStoreSeqId());
		Assert.assertTrue(attachDocStore.getDocSize() > 0);
		Assert.assertTrue(DateUtils.isSameDay(attachDocStore.getUploadDate(), new Date(System.currentTimeMillis())));
		Assert.assertEquals(attachDocStore.getUploadBy(), AccnStatusMap.ATTACH_STATEMENT_ENGINE);
		verifyDocStoreIndex(attachDocStore.getSeqId(), DOC_STORE_INDEX_TYPE_SYSTEM, DOC_CATEGORY_ID_PREFIX_ATTACH + attachSubmFile.getSubmFileSeqId());
		Assert.assertEquals(docStoreDao.getDocStoreTyp(attachDocStore.getDocStoreTyp()).getAbbrev(), "xifinattach");
		Assert.assertEquals(attachDocStore.getDocStoreLocationId(), 2);

		String trnSegment = "TRN*1*"+attachmentControlId+"~";
		logger.info("message=Verifying 275 content, data="+trnSegment);
		navigation.navigateToDocUploadStorageGetDocPage(attachDocStore.getSeqId());
		String pageContent = StringUtils.normalizeSpace(driver.getPageSource());
		Assert.assertTrue(StringUtils.contains(pageContent, trnSegment));
	}

	@Test(priority = 1, description = "Create combined PDF attachment")
	@Parameters({"ssoUsername", "ssoPassword", "accnId", "claimSubmSvc", "attachSubmSvc"})
	public void testCreateCombinedPdfAttachment(String ssoUsername, String ssoPassword, String accnId, String claimSubmSvc, String attachSubmSvc) throws Exception
	{
		logger.info("message=Starting test case, methodName=testCreateCombinedPdfAttachment, accnId=" + accnId + ", attachSubmSvc="+attachSubmSvc);
		cleanUpAccn(accnId);
		List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
		Assert.assertFalse(accnPyrs.isEmpty(), "Expected at least one payor, accnId="+accnId);
		Pyr pyr = payorDao.getPyrByPyrId(accnPyrs.get(0).getPyrId());
		List<AttachReq> attachReqs = submissionDao.getAttachReqsByAccnIdPyrId(accnId, pyr.getPyrId());
		Assert.assertEquals(attachReqs.size(), 2, "Expected 2 required attachments, accnId="+accnId+", pyrId=" + pyr.getPyrId());
		List<QAccnSubm> qasList = rpmDao.getQAccnSubm(null, accnId);
		Assert.assertTrue(qasList.isEmpty(), "Expected 0 QAS records, accnId="+accnId);
		logger.info("message=Submitting claim, accnId="+accnId+", pyrAbbrev="+pyr.getPyrAbbrv()+", claimSubmSvc="+claimSubmSvc);
		MenuNavigation navigation = new MenuNavigation(driver, config);
		navigation.navigateToAccnDetailPage();
		AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
		accessionDetail.submitClaimsOnAccnDetail(accnId, pyr.getPyrAbbrv(), accnPyrs.get(0).getSubsId(), claimSubmSvc, wait, false);
		qasList = rpmDao.getQAccnSubm(null, accnId);
		Assert.assertEquals(qasList.size(), 1, "Expected 1 QAS record, accnId="+accnId);
		logger.info("message=Clear last submission date, submSvcAbbrev=" + claimSubmSvc);
		rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, rpmDao.getSubmSvcByAbbrev(null, claimSubmSvc).getSubmSvcSeqId());
		logger.info("message=Wait for Non-Client Submission Engine to process the claim, accnId="+accnId+", docSeqId="+qasList.get(0).getDocSeqId());
		int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, claimSubmSvc);
		Assert.assertTrue(submFileSeqId > 0, "Expected submFileSeqId > 0");
		logger.info("message=Wait for Non-Client Statement Engine to process the file, submFileSeqId="+submFileSeqId);
		boolean isFileProcessed = waitForStatementEngine(rpmDao.getSubmFile(null, submFileSeqId), QUEUE_WAIT_TIME);
		Assert.assertTrue(isFileProcessed, "Expected E_GATE_PROCESSED=1, submFileSeqId="+submFileSeqId);
		SubmFile sf = rpmDao.getSubmFile(null, submFileSeqId);
		Assert.assertTrue(DateUtils.isSameDay(sf.getFileCreatDt(), new java.util.Date()), "Expected fileCreatDt=today, submFileSeqId="+submFileSeqId+", fileCreatDt="+sf.getFileCreatDt());

		SubmSvc submSvc = submissionDao.getSubmSvcByAbbrev(attachSubmSvc);
		List<QAccnSubmAttach> qAccnSubmAttachList = submissionDao.getQAccnSubmAttachListByDocSeqId(qasList.get(0).getDocSeqId());
		Assert.assertEquals(qAccnSubmAttachList.size(), 2, "Expected 2 QAccnSubmAttach records, accnId="+accnId);
		Set<Integer> attachSubmFileSeqIds = new HashSet<>();
		for (QAccnSubmAttach qAccnSubmAttach : qAccnSubmAttachList)
		{
			logger.info("message=Verifying QAccnSubmAttach record, accnId="+accnId+", qAccnSubmAttachSeqId="+qAccnSubmAttach.getSeqId());
			Assert.assertEquals(qAccnSubmAttach.getAttachSubmSvcSeqId(), submSvc.getSubmSvcSeqId(), "Attachment submission service does not match, accnId="+accnId+", qAccnSubmAttachSeqId="+qAccnSubmAttach.getSeqId());
			Assert.assertTrue(qAccnSubmAttach.getAttachFileSeqId() > 0, "AttachFileSeqId should be > 0, accnId="+accnId+", qAccnSubmAttachSeqId="+qAccnSubmAttach.getSeqId());
			Assert.assertTrue(qAccnSubmAttach.getAttachTypId() > 0, "AttachTyp should be > 0, accnId="+accnId+", qAccnSubmAttachSeqId="+qAccnSubmAttach.getSeqId());
			Assert.assertEquals(String.valueOf(qAccnSubmAttach.getAttachControlSuffixId()), qAccnSubmAttach.getAttachTypId()+"00", "AttachControlSuffixId is incorrect, accnId="+accnId+", qAccnSubmAttachSeqId="+qAccnSubmAttach.getSeqId());
			Assert.assertEquals(qAccnSubmAttach.getOrigSubmFileSeqId(), sf.getSubmFileSeqId(), "Original SubmFileSeqId is incorrect, accnId="+accnId+", qAccnSubmAttachSeqId="+qAccnSubmAttach.getSeqId());
			AttachFile attachFile = submissionDao.getAttachFile(qAccnSubmAttach.getAttachFileSeqId());
			logger.info("message=Verifying AttachFile record, accnId="+accnId+", qAccnSubmAttachSeqId="+qAccnSubmAttach.getSeqId()+", attachFileSeqId="+qAccnSubmAttach.getAttachFileSeqId());
			Assert.assertEquals(attachFile.getAccnId(), accnId, "AttachFile accnId is incorrect, accnId="+accnId+", attachFileSeqId="+attachFile.getSeqId());
			Assert.assertEquals(attachFile.getPyrId(), pyr.getPyrId(), "AttachFile pyrId is incorrect, accnId="+accnId+", attachFileSeqId="+attachFile.getSeqId());
			Assert.assertFalse(attachFile.getIsDeleted(), "AttachFile is deleted, accnId="+accnId+", attachFileSeqId="+attachFile.getSeqId());
			Assert.assertTrue(DateUtils.isSameDay(attachFile.getInDt(), new java.util.Date()), "Expected inDt=today, accnId="+accnId+", attachFileSeqId="+attachFile.getSeqId());
			Assert.assertNotNull(attachFile.getDocStoreSeqId(), "AttachFile docStoreSeqId should not be null, accnId="+accnId+", attachFileSeqId="+attachFile.getSeqId());
			DocStore docStore = rpmDao.getDocStore(attachFile.getDocStoreSeqId());
			Assert.assertEquals(attachFile.getFilename(), docStore.getFileName(), "AttachFile filename is incorrect, accnId="+accnId+", attachFileSeqId="+attachFile.getSeqId());
			Assert.assertEquals(attachFile.getDir(), docStore.getRepositoryUri(), "AttachFile dir is incorrect, accnId="+accnId+", attachFileSeqId="+attachFile.getSeqId());
			SubmFile attachSubmFile = waitForAttachmentStatementEngine(qAccnSubmAttach, QUEUE_WAIT_TIME);
			attachSubmFileSeqIds.add(attachSubmFile.getSubmFileSeqId());
		}
		Assert.assertEquals(attachSubmFileSeqIds.size(), 1, "Expected 1 attachment SubmFile records, accnId="+accnId);
		SubmFile attachSubmFile = submissionDao.getSubmFileBySubmFileSeqId(attachSubmFileSeqIds.iterator().next());
		logger.info("message=Verifying attachment SubmFile record, accnId="+accnId+", attachSubmFileSeqId="+attachSubmFile.getSubmFileSeqId());
		Assert.assertEquals(attachSubmFile.getSubmSvcSeqId(), submSvc.getSubmSvcSeqId(), "Attachment SubmFile submission service does not match, accnId="+accnId+", attachSubmFileSeqId="+attachSubmFile.getSubmFileSeqId());
		Assert.assertEquals(attachSubmFile.getDataFmtTypId(), submSvc.getDataFmtTypId(), "Attachment SubmFile data format type does not match, accnId="+accnId+", attachSubmFileSeqId="+attachSubmFile.getSubmFileSeqId());
		Assert.assertTrue(DateUtils.isSameDay(attachSubmFile.getFileCreatDt(), new java.util.Date()), "Attachment SubmFile create date should be today, accnId="+accnId+", attachSubmFileSeqId="+attachSubmFile.getSubmFileSeqId());
		Assert.assertTrue(attachSubmFile.getIsEgateProcessed(), "Attachment SubmFile should be marked as e-gate processed, accnId="+accnId+", attachSubmFileSeqId="+attachSubmFile.getSubmFileSeqId());
		Assert.assertTrue(StringUtils.endsWithIgnoreCase(attachSubmFile.getFilename(), ".pdf"), "Attachment SubmFile filename should contain .pdf suffix, accnId="+accnId+", attachSubmFileSeqId="+attachSubmFile.getSubmFileSeqId());
		Assert.assertTrue(attachSubmFile.getDocStoreSeqId() != null && attachSubmFile.getDocStoreSeqId() > 0, "Attachment SubmFile docStoreSeqId should be > 0, accnId="+accnId+", attachSubmFileSeqId="+attachSubmFile.getSubmFileSeqId());
		logger.info("message=Verifying attachment DocStore record, accnId="+accnId+", docStoreSeqId="+attachSubmFile.getDocStoreSeqId());
		DocStore docStore = rpmDao.getDocStore(attachSubmFile.getDocStoreSeqId());
		Assert.assertEquals(docStore.getFileName(), attachSubmFile.getFilename(), "Attachment DocStore filename does not match, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertEquals(docStore.getContentType(), MIME_TYPE_PDF, "Attachment DocStore content type does not match, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertTrue(docStore.getDocSize() > 0, "Attachment DocStore file size should be > 0, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertTrue(DateUtils.isSameDay(docStore.getUploadDate(), new java.util.Date()), "Attachment DocStore upload date should be today, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertEquals(docStore.getUploadBy(), AccnStatusMap.ATTACH_STATEMENT_ENGINE, "Attachment DocStore upload by type does not match, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertEquals(docStore.getDocStoreTyp(), DOC_STORE_TYP_ATTACH, "Attachment DocStore type does not match, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		verifyDocStoreIndex(docStore.getSeqId(), DOC_STORE_INDEX_TYPE_SYSTEM, DOC_CATEGORY_ID_PREFIX_ATTACH + attachSubmFile.getSubmFileSeqId());
		Assert.assertNotNull(docStore.getRepositoryUri(), "Attachment DocStore repository URI should not be null, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertFalse(docStore.getIsProcess(), "Attachment DocStore record should not be marked in-process, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertFalse(docStore.getIsDeleted(), "Attachment DocStore record should not be marked deleted, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertEquals(docStore.getScanStatusTypId(), DOC_STORE_SCAN_STATUS_INTERNAL_DOCUMENT, "Attachment DocStore scan status should be 1 (Internal Document), accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertTrue(docStore.getDocStoreLocationId() > 0, "Attachment DocStore location ID should be > 0, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertTrue(DateUtils.isSameDay(docStore.getLocationStorageDate(), new java.util.Date()), "Attachment DocStore location storage date should be today, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertTrue(DateUtils.isSameDay(docStore.getLastAccessDate(), new java.util.Date()), "Attachment DocStore last access date should be today, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertFalse(docStore.getIsUserLoaded(), "Attachment DocStore record should not be marked user-loaded, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());

//		logger.info("message=Verifying attachment PDF content, accnId="+accnId+", docStoreSeqId="+attachSubmFile.getDocStoreSeqId());
//		File pdfDownload = FileManipulation.getPdfDownload(docStore.getFileName());
//		if (pdfDownload.exists())
//		{
//			logger.info("message=Deleting previously-downloaded pdf, file="+pdfDownload.getAbsolutePath());
//			FileUtils.deleteQuietly(pdfDownload);
//		}
//		Assert.assertFalse(pdfDownload.exists(), "PDF file should not already exist, accnId="+accnId+", filename="+pdfDownload.getAbsolutePath());
//		navigation.navigateToDocUploadStorageGetDocPage(docStore.getSeqId());
//		waitForFileToDownload(pdfDownload);
//		Assert.assertTrue(pdfDownload.exists(), "Downloaded PDF file should exist, accnId="+accnId+", filename="+pdfDownload.getAbsolutePath());
//		String actualPdfContent = readPdfContent(pdfDownload);
//		String expectedPdfFilename = "/testData/attachmentStatementEngine/testCreateCombinedPdfAttachment.pdf";
//		URL expectedPdfUrl = getClass().getResource(expectedPdfFilename);
//		Assert.assertNotNull(expectedPdfUrl, "Cannot open PDF expected file, accnId="+accnId+", filename="+expectedPdfFilename);
//		File expectedPdfFile = new File(expectedPdfUrl.getFile());
//		Assert.assertTrue(expectedPdfFile.exists(), "Expected PDF file does not exist, accnId="+accnId+", file="+expectedPdfFile.getAbsolutePath());
//		String expectedPdfContent = readPdfContent(expectedPdfFile);
//		Assert.assertEquals(actualPdfContent, expectedPdfContent, "Actual PDF content does not match expected, actualFile="+pdfDownload);
	}

	private void verifyDocStoreIndex(int seqId, int docStoreIndexType, String dataValue) throws XifinDataAccessException
	{
		boolean foundDocStoreIndex = false;
		for (DocStoreIndex dsi : docStoreDao.getDocStoreIndexRecordsByDocStoreSeqId(seqId))
		{
			if (dsi.getIndexTypId() == docStoreIndexType && StringUtils.equals(dsi.getDataValue(), dataValue))
			{
				foundDocStoreIndex = true;
				break;
			}
		}
		Assert.assertTrue(foundDocStoreIndex, "Expected doc store index, docStoreSeqId="+seqId+", docStoreIndexType="+docStoreIndexType+", dataValue="+dataValue);
	}

	@Test(priority = 1, description = "Test S3 upload and download")
	@Parameters({"ssoUsername", "ssoPassword", "accnId", "claimSubmSvc", "attachSubmSvc"})
	public void testS3UploadAndDownload(String ssoUsername, String ssoPassword, String accnId, String claimSubmSvc, String attachSubmSvc) throws Exception
	{
		cleanUpAccn(accnId);
		List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
		Assert.assertFalse(accnPyrs.isEmpty(), "Expected at least one payor, accnId="+accnId);
		Pyr pyr = payorDao.getPyrByPyrId(accnPyrs.get(0).getPyrId());
		List<AttachReq> attachReqs = submissionDao.getAttachReqsByAccnIdPyrId(accnId, pyr.getPyrId());
		Assert.assertEquals(attachReqs.size(), 1, "Expected 1 required attachment, accnId="+accnId+", pyrId=" + pyr.getPyrId());
		List<QAccnSubm> qasList = rpmDao.getQAccnSubm(null, accnId);
		Assert.assertTrue(qasList.isEmpty(), "Expected 0 QAS records, accnId="+accnId);
		logger.info("message=Submitting claim, accnId="+accnId+", pyrAbbrev="+pyr.getPyrAbbrv()+", claimSubmSvc="+claimSubmSvc);
		MenuNavigation navigation = new MenuNavigation(driver, config);
		navigation.navigateToAccnDetailPage();
		AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
		accessionDetail.submitClaimsOnAccnDetail(accnId, pyr.getPyrAbbrv(), accnPyrs.get(0).getSubsId(), claimSubmSvc, wait, false);
		qasList = rpmDao.getQAccnSubm(null, accnId);
		Assert.assertEquals(qasList.size(), 1, "Expected 1 QAS record, accnId="+accnId);
		logger.info("message=Clear last submission date, submSvcAbbrev=" + claimSubmSvc);
		rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, rpmDao.getSubmSvcByAbbrev(null, claimSubmSvc).getSubmSvcSeqId());
		logger.info("message=Wait for Non-Client Submission Engine to process the claim, accnId="+accnId+", docSeqId="+qasList.get(0).getDocSeqId());
		int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, claimSubmSvc);
		Assert.assertTrue(submFileSeqId > 0, "Expected submFileSeqId > 0");
		logger.info("message=Wait for Non-Client Statement Engine to process the file, submFileSeqId="+submFileSeqId);
		boolean isFileProcessed = waitForStatementEngine(rpmDao.getSubmFile(null, submFileSeqId), QUEUE_WAIT_TIME);
		Assert.assertTrue(isFileProcessed, "Expected E_GATE_PROCESSED=1, submFileSeqId="+submFileSeqId);
		SubmFile sf = rpmDao.getSubmFile(null, submFileSeqId);
		Assert.assertTrue(DateUtils.isSameDay(sf.getFileCreatDt(), new java.util.Date()), "Expected fileCreatDt=today, submFileSeqId="+submFileSeqId+", fileCreatDt="+sf.getFileCreatDt());
		SubmSvc submSvc = submissionDao.getSubmSvcByAbbrev(attachSubmSvc);
		List<QAccnSubmAttach> qAccnSubmAttachList = submissionDao.getQAccnSubmAttachListByDocSeqId(qasList.get(0).getDocSeqId());
		Assert.assertEquals(qAccnSubmAttachList.size(), 1, "Expected 1 QAccnSubmAttach record, accnId="+accnId);
		AttachFile attachFile = submissionDao.getAttachFile(qAccnSubmAttachList.get(0).getAttachFileSeqId());
		DocStore docStore = rpmDao.getDocStore(attachFile.getDocStoreSeqId());
		logger.info("message=Verifying attachment DocStore location is S3, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertEquals(docStore.getDocStoreLocationId(), DOC_STORE_LOCATION_S3, "Expected attachment doc store location S3 (2), accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		Assert.assertTrue(StringUtils.startsWithIgnoreCase(docStore.getRepositoryUri(), DOC_STORE_URI_PREFIX_S3), "Expected attachment doc store URI to begin with S3, accnId="+accnId+", docStoreSeqId="+docStore.getSeqId());
		SubmFile attachSubmFile = waitForAttachmentStatementEngine(qAccnSubmAttachList.get(0), QUEUE_WAIT_TIME);
		DocStore combinedDocStore = rpmDao.getDocStore(attachSubmFile.getDocStoreSeqId());
		logger.info("message=Verifying combined PDF DocStore location is S3, accnId="+accnId+", docStoreSeqId="+combinedDocStore.getSeqId());
		Assert.assertEquals(combinedDocStore.getDocStoreLocationId(), DOC_STORE_LOCATION_S3, "Expected combined PDF doc store location S3 (2), accnId="+accnId+", docStoreSeqId="+combinedDocStore.getSeqId());
		Assert.assertTrue(StringUtils.startsWithIgnoreCase(combinedDocStore.getRepositoryUri(), DOC_STORE_URI_PREFIX_S3), "Expected combined PDF doc store URI to begin with S3, accnId="+accnId+", docStoreSeqId="+combinedDocStore.getSeqId());

//		logger.info("message=Verifying combined PDF content, accnId="+accnId+", docStoreSeqId="+combinedDocStore.getSeqId());
//		File pdfDownload = FileManipulation.getPdfDownload(combinedDocStore.getFileName());
//		if (pdfDownload.exists())
//		{
//			logger.info("message=Deleting previously-downloaded pdf, file="+pdfDownload.getAbsolutePath());
//			FileUtils.deleteQuietly(pdfDownload);
//		}
//		Assert.assertFalse(pdfDownload.exists(), "PDF file should not already exist, accnId="+accnId+", filename="+pdfDownload.getAbsolutePath());
//		navigation.navigateToDocUploadStorageGetDocPage(combinedDocStore.getSeqId());
//		waitForFileToDownload(pdfDownload);
//		Assert.assertTrue(pdfDownload.exists(), "Downloaded PDF file should exist, accnId="+accnId+", filename="+pdfDownload.getAbsolutePath());
//		String actualPdfContent = readPdfContent(pdfDownload);
//		String expectedPdfFilename = "/testData/attachmentStatementEngine/testS3UploadAndDownload.pdf";
//		URL expectedPdfUrl = getClass().getResource(expectedPdfFilename);
//		Assert.assertNotNull(expectedPdfUrl, "Cannot open PDF expected file, accnId="+accnId+", filename="+expectedPdfFilename);
//		File expectedPdfFile = new File(expectedPdfUrl.getFile());
//		Assert.assertTrue(expectedPdfFile.exists(), "Expected PDF file does not exist, accnId="+accnId+", file="+expectedPdfFile.getAbsolutePath());
//		String expectedPdfContent = readPdfContent(expectedPdfFile);
//		Assert.assertEquals(actualPdfContent, expectedPdfContent, "Actual combined PDF content does not match expected, actualFile="+pdfDownload);
	}

	@Test(priority = 1, description = "Generate missing attachment errors")
	@Parameters({"ssoUsername", "ssoPassword", "accnId"})
	public void testGenerateMissingAttachmentErrors(String ssoUsername, String ssoPassword, String accnId) throws Exception
	{
		logger.info("message=Starting test case, methodName=testGenerateMissingAttachmentErrors, accnId=" + accnId);
		cleanUpAccn(accnId);
		File pdfDownload = FileManipulation.getPdfDownload("testCreateCombinedPdfAttachment.pdf");
		if (pdfDownload.exists())
		{
			logger.info("message=Deleting previously-downloaded pdf, file="+pdfDownload.getAbsolutePath());
			FileUtils.deleteQuietly(pdfDownload);
		}
		Assert.assertFalse(pdfDownload.exists(), "PDF file should not already exist, accnId="+accnId+", filename="+pdfDownload.getAbsolutePath());
		MenuNavigation navigation = new MenuNavigation(driver, config);
		navigation.navigateToDocUploadStorageGetDocPage(82949);
		waitForFileToDownload(pdfDownload);
		Assert.assertTrue(pdfDownload.exists(), "Downloaded PDF file should exist, accnId="+accnId+", filename="+pdfDownload.getAbsolutePath());
		String actualPdfContent = readPdfContent(pdfDownload);
	}

	public String readPdfContent(File file) throws IOException
	{
		String content = null;
		try (FileInputStream fis = new FileInputStream(file); BufferedInputStream fileToParse = new BufferedInputStream(fis);
			 PDDocument document = PDDocument.load(fileToParse))
		{
			content = new PDFTextStripper().getText(document);
		}
		return content;
	}

	private void cleanUpAccn(String accnId) throws XifinDataAccessException
	{
		logger.info("message=Cleaning up all claim statements, accnId=" + accnId);
		// delete all submissions
		submissionDao.deleteSubmissions(rpmDao.getQAccnSubm(null, accnId));
	}

	protected boolean isOutOfPricingQueue(String accnId, long maxTime) throws Exception
	{
		long startTime = System.currentTimeMillis();
		maxTime += startTime;
		boolean isInQueue = accessionDao.isInPricingQueue(accnId);
		while (isInQueue && System.currentTimeMillis() < maxTime)
		{
			logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME_MS);
			isInQueue = accessionDao.isInPricingQueue(accnId);
		}
		return !isInQueue;
	}

	protected boolean isOutOfEligibilityQueue(String accnId, long maxTime) throws Exception
	{
		long startTime = System.currentTimeMillis();
		maxTime += startTime;
		boolean isInQueue = accessionDao.isInEligibilityQueue(accnId);
		while (isInQueue && System.currentTimeMillis() < maxTime)
		{
			logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME_MS);
			isInQueue = accessionDao.isInEligibilityQueue(accnId);
		}
		return !isInQueue;
	}

	protected boolean isOutOfQFrPendingQueue(String accnId, long maxTime) throws Exception
	{
		long startTime = System.currentTimeMillis();
		maxTime += startTime;
		boolean isInQueue = accessionDao.isInQFrPendingQueue(accnId);
		while (isInQueue && System.currentTimeMillis() < maxTime)
		{
			logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME_MS);
			isInQueue = accessionDao.isInQFrPendingQueue(accnId);
		}
		return !isInQueue;
	}

	private static void verifyPageTitle(DocumentUploadAndStorage documentUploadAndStorage)
	{
		Assert.assertTrue(org.apache.commons.lang3.StringUtils.containsIgnoreCase(documentUploadAndStorage.pageTitle().getText(), PAGE_TITLE));
	}

	protected int waitForSubmissionEngine(QAccnSubm qAccnSubm, long maxTime, String submSvcAbbrev) throws InterruptedException, XifinDataAccessException
	{
		long startTime = System.currentTimeMillis();
		maxTime += startTime;
		int submFileSeqId = qAccnSubm.getSubmFileSeqId();
		while (submFileSeqId == 0 && System.currentTimeMillis() < maxTime)
		{
			logger.info("Waiting for Submission Engine to process claim file, accnId=" + qAccnSubm.getAccnId() + ",docSeqId=" + qAccnSubm.getDocSeqId() + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME);
			try
			{
				long currentTime = System.currentTimeMillis();
				if (currentTime == (startTime + maxTime) / 2)
				{
					logger.info("Waiting for maxTime/2=" + maxTime / 2);
					rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);
				}
				qAccnSubm = rpmDao.getQAccnSubm(testDb, qAccnSubm.getDocSeqId());
			}
			catch (XifinDataNotFoundException e)
			{
				submFileSeqId = -1;
			}
			submFileSeqId = qAccnSubm.getSubmFileSeqId();
		}
		return submFileSeqId;
	}

	protected boolean waitForStatementEngine(SubmFile submFile, long maxTime)
			throws InterruptedException, XifinDataAccessException
	{
		long startTime = System.currentTimeMillis();
		maxTime += startTime;
		boolean submFileBEgateProcessed = submFile.getIsEgateProcessed();
		while (!submFileBEgateProcessed && System.currentTimeMillis() < maxTime)
		{
			logger.info("Waiting for Statement Engine to process subm file, submFileSeqId=" + submFile.getSubmFileSeqId() + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME);
			SubmFile submFile1 = rpmDao.getSubmFile(testDb, submFile.getSubmFileSeqId());
			if (submFile1 == null)
			{
				logger.info("Submission file was not found, submFileSeqId=" + submFile.getSubmFileSeqId());
				break;
			}
			submFileBEgateProcessed = submFile1.getIsEgateProcessed();
		}
		return submFileBEgateProcessed;
	}

	protected SubmFile waitForAttachmentStatementEngine(QAccnSubmAttach qAccnSubmAttach, long maxTime)
			throws InterruptedException, XifinDataAccessException, XifinDataNotFoundException
	{
		long startTime = System.currentTimeMillis();
		maxTime += startTime;
		SubmFile attachSubmFile = null;
		if (qAccnSubmAttach.getAttachSubmFileSeqId() > 0)
		{
			attachSubmFile = rpmDao.getSubmFile(null, qAccnSubmAttach.getAttachSubmFileSeqId());
		}
		while (System.currentTimeMillis() < maxTime && (attachSubmFile == null || !attachSubmFile.getIsEgateProcessed()))
		{
			logger.info("Waiting for Attachment Statement Engine, accnId="+qAccnSubmAttach.getAccnId()+", qAccnSubmAttachSeqId=" + qAccnSubmAttach.getSeqId() + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME);
			qAccnSubmAttach = submissionDao.getQAccnSubmAttach(qAccnSubmAttach.getSeqId());
			if (qAccnSubmAttach.getAttachSubmFileSeqId() > 0)
			{
				attachSubmFile = rpmDao.getSubmFile(null, qAccnSubmAttach.getAttachSubmFileSeqId());
			}
		}
		return attachSubmFile;
	}

	protected void waitForFileToDownload(File file) throws InterruptedException, XifinDataAccessException
	{
		long startTime = System.currentTimeMillis();
		long maxTime = startTime + QUEUE_WAIT_TIME;
		while ((!file.exists() || file.lastModified() >= System.currentTimeMillis()-QUEUE_POLL_TIME) && System.currentTimeMillis() < maxTime)
		{
			logger.info("Waiting for file to download, file=" + file.getAbsolutePath() + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME);
		}
	}

	public static String getBaseDir()
	{
		String baseDir;

		if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(OS_NAME, WINDOWS))
		{
			baseDir = File.separator + File.separator + "a3unity01-mp" + File.separator + "cnfs01";
		}
		else
		{
			baseDir = File.separator + "home";
		}
		return baseDir;
	}

	private void cleanUpAttachmentFiles(int submSvcSeqId) throws XifinDataAccessException
	{
		submissionDao.deleteQAccnSubmAttachMissingQAS(submSvcSeqId);
		submissionDao.setSubmFilesProcessedForQASA();
	}
}
