package com.mars.tests;

import com.mbasys.mars.ejb.entity.docStore.DocStore;
import com.mbasys.mars.ejb.entity.docStoreAudit.DocStoreAudit;
import com.mbasys.mars.ejb.entity.docStoreTyp.DocStoreTyp;
import com.overall.fileMaintenance.sysMgt.DSAccessionSearch;
import com.overall.fileMaintenance.sysMgt.DocumentUploadAndStorage;
import com.overall.menu.MenuNavigation;
import com.overall.utils.DocStoreUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.overall.fileMaintenance.sysMgt.DocumentUploadAndStorage.DUPLICATE_DOCUMENT_ERROR;
import static com.overall.fileMaintenance.sysMgt.DocumentUploadAndStorage.EMPTY_DOCUMENT_ERROR;
import static com.overall.fileMaintenance.sysMgt.DocumentUploadAndStorage.MATCHING_CONTENT_ERROR;
import static com.overall.fileMaintenance.sysMgt.DocumentUploadAndStorage.PAGE_TITLE;
import static com.overall.fileMaintenance.sysMgt.DocumentUploadAndStorage.SYSTEM_CATEGORY;


public class FileMaintenanceDocUploadStorage extends SeleniumBaseTest
{
    private static final String AUD_USER_PREFIX = "xifinportal~";

    private static final SimpleDateFormat DATE_FORMAT_MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");

    @BeforeMethod(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void beforeMethod(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running Before Method");
            logIntoSso(ssoUsername, ssoPassword);
            MenuNavigation navigation = new MenuNavigation(driver, config);
            navigation.navigateToDocUploadStoragePage();
        }
        catch (Exception e)
        {
            Assert.fail("Error running Before Method", e);
        }
    }

    @Test(priority = 1, description = "Cannot Upload Duplicate File")
    @Parameters({"filename", "category", "categoryId", "documentType", "contentType", "uploadBy", "filename2"})
    public void testRPM_768(String filename, String category, String categoryId, String documentType,
                            String contentType, String uploadBy, String filename2) throws Exception
    {
        logger.info("message=Clean DocStore records, filename=" + filename);
        deleteDocStoreItem(filename);

        logger.info("message=Verify FM Document Upload And Storage page is displayed");
        DocumentUploadAndStorage documentUploadAndStorage = new DocumentUploadAndStorage(driver, wait);
        verifyPageTitle(documentUploadAndStorage);

        logger.info("message=Click on Upload Document button");
        documentUploadAndStorage.uploadDocumentButton().click();
        logger.info("message=Upload document, filename=" + filename);
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename);
        logger.info("message=Set Category dropdown, value=" + category);
        selectItem(documentUploadAndStorage.categoryDropdown(), category);
        logger.info("message=Set Category ID, value=" + categoryId);
        documentUploadAndStorage.setCategoryId(categoryId);
        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.clickUploadButton();
        logger.info("message=Verify new document is in the uploads grid");
        documentUploadAndStorage.document(filename);
        logger.info("message=Click Save button");
        documentUploadAndStorage.clickSaveDocumentsButton();

        Timestamp uploadDate = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1));

        logger.info("message=Verify document is stored in DB");
        List<DocStore> docStoreList = docStoreDao.getDocStoreList(filename, uploadDate);
        Assert.assertEquals(docStoreList.size(), 1);

        new DocStoreUtils(docStoreDao).verifyDocStore(docStoreList.get(0), category, categoryId, documentType, contentType, null, uploadBy, AUD_USER_PREFIX+uploadBy, false, false, true);

        logger.info("message=Click Upload Documents button again");
        documentUploadAndStorage.uploadDocumentButton().click();
        logger.info("message=Upload the same document with same category");
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename);
        logger.info("message=Set Category dropdown, value=" + category);
        selectItem(documentUploadAndStorage.categoryDropdown(), category);
        logger.info("message=Set Category ID, value=" + categoryId);
        documentUploadAndStorage.setCategoryId(categoryId);
        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.uploadButton().click();
        logger.info("message=Verify that an error message is displayed");
        String errorMsg = documentUploadAndStorage.dialogUploadConfirm().getText();
        Assert.assertTrue(StringUtils.containsIgnoreCase(errorMsg, DUPLICATE_DOCUMENT_ERROR), "Error message not found, expectedError="+DUPLICATE_DOCUMENT_ERROR+", actualMsg="+errorMsg);
        logger.info("message=Close error popup");
        documentUploadAndStorage.clickDialogUploadErrorCancelButton();
        documentUploadAndStorage.cancelUploadButton().click();

        logger.info("message=Click Upload Documents button again");
        documentUploadAndStorage.uploadDocumentButton().click();
        String newCategory = "System";
        Assert.assertNotEquals(category, newCategory);
        logger.info("message=Upload the same document with a different category, newCategory=" + newCategory);
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename);
        logger.info("message=Set Category dropdown, value=" + newCategory);
        selectItem(documentUploadAndStorage.categoryDropdown(), newCategory);
        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.uploadButton().click();
        logger.info("message=Verify that an error message is displayed");
        Assert.assertTrue(StringUtils.containsIgnoreCase(documentUploadAndStorage.dialogUploadConfirm().getText(), DUPLICATE_DOCUMENT_ERROR));
        logger.info("message=Close error popup");
        documentUploadAndStorage.clickDialogUploadErrorCancelButton();
        documentUploadAndStorage.cancelUploadButton().click();

        logger.info("message=Click Upload Documents button again");
        documentUploadAndStorage.uploadDocumentButton().click();
        logger.info("message=Upload a different file with the same content, filename2=" + filename2);
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename2);
        logger.info("message=Set Category dropdown, value=" + category);
        selectItem(documentUploadAndStorage.categoryDropdown(), category);
        logger.info("message=Set Category ID, value=" + categoryId);
        documentUploadAndStorage.setCategoryId(categoryId);
        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.uploadButton().click();
        logger.info("message=Verify that an error message is displayed");
        Assert.assertTrue(StringUtils.containsIgnoreCase(documentUploadAndStorage.dialogUploadConfirm().getText(), MATCHING_CONTENT_ERROR));
        logger.info("message=Close error popup");
        documentUploadAndStorage.clickDialogUploadErrorCancelButton();
        documentUploadAndStorage.cancelUploadButton().click();
    }

    @Test(priority = 1, description = "Cannot Upload Empty File")
    @Parameters({"filename", "documentType"})
    public void testRPM_770(String filename, String documentType) throws Exception
    {
        logger.info("message=Verify FM Document Upload And Storage page is displayed");
        DocumentUploadAndStorage documentUploadAndStorage = new DocumentUploadAndStorage(driver, wait);
        verifyPageTitle(documentUploadAndStorage);

        logger.info("message=Click on Upload Document button");
        documentUploadAndStorage.uploadDocumentButton().click();
        logger.info("message=Upload document, filename=" + filename);
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename);
        logger.info("message=Set Category dropdown, value=" + SYSTEM_CATEGORY);
        selectItem(documentUploadAndStorage.categoryDropdown(), SYSTEM_CATEGORY);
        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.uploadButton().click();
        logger.info("message=Verify that an error message is displayed");
        Assert.assertTrue(StringUtils.containsIgnoreCase(documentUploadAndStorage.dialogUploadConfirm().getText(), EMPTY_DOCUMENT_ERROR));
        logger.info("message=Close error popup");
        documentUploadAndStorage.clickDialogUploadErrorCancelButton();
        documentUploadAndStorage.cancelUploadButton().click();
    }

    @Test(priority = 1, description = "File With Non-Existent Category ID Is Pending")
    @Parameters({"filename", "category", "categoryId", "documentType", "contentType", "uploadBy"})
    public void testRPM_759(String filename, String category, String categoryId, String documentType, String contentType, String uploadBy) throws Exception
    {
        logger.info("message=Clean DocStore records, filename=" + filename);
        deleteDocStoreItem(filename);

        logger.info("message=Verify FM Document Upload And Storage page is displayed");
        DocumentUploadAndStorage documentUploadAndStorage = new DocumentUploadAndStorage(driver, wait);
        verifyPageTitle(documentUploadAndStorage);

        logger.info("message=Click on Upload Document button");
        documentUploadAndStorage.uploadDocumentButton().click();
        logger.info("message=Upload document, filename=" + filename);
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename);
        logger.info("message=Set Category dropdown, value=" + category);
        selectItem(documentUploadAndStorage.categoryDropdown(), category);
        logger.info("message=Set Category ID, value=" + categoryId);
        documentUploadAndStorage.setCategoryId(categoryId);
        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.clickUploadButton();
        logger.info("message=Verify new document is in the uploads grid");
        documentUploadAndStorage.document(filename);
        logger.info("message=Click Save button");
        documentUploadAndStorage.clickSaveDocumentsButton();

        Timestamp uploadDate = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1));

        logger.info("message=Verify document is stored in DB");
        List<DocStore> docStoreList = docStoreDao.getDocStoreList(filename, uploadDate);
        Assert.assertEquals(docStoreList.size(), 1);

        new DocStoreUtils(docStoreDao).verifyDocStore(docStoreList.get(0), category, categoryId, documentType, contentType, null,  uploadBy, AUD_USER_PREFIX+uploadBy, true, false, true);
    }

    @Test(priority = 1, description = "Cannot Upload Duplicate Pending File")
    @Parameters({"filename", "category", "categoryId", "documentType", "contentType", "uploadBy", "filename2", "categoryId2"})
    public void testRPM_761(String filename, String category, String categoryId, String documentType, String contentType, String uploadBy, String filename2, String categoryId2) throws Exception
    {
        logger.info("message=Clean DocStore records, filename=" + filename);
        deleteDocStoreItem(filename);

        logger.info("message=Verify FM Document Upload And Storage page is displayed");
        DocumentUploadAndStorage documentUploadAndStorage = new DocumentUploadAndStorage(driver, wait);
        verifyPageTitle(documentUploadAndStorage);

        logger.info("message=Click on Upload Document button");
        documentUploadAndStorage.uploadDocumentButton().click();
        logger.info("message=Upload document, filename=" + filename);
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename);
        logger.info("message=Set Category dropdown, value=" + category);
        selectItem(documentUploadAndStorage.categoryDropdown(), category);
        logger.info("message=Set Category ID, value=" + categoryId);
        documentUploadAndStorage.setCategoryId(categoryId);
        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.clickUploadButton();
        logger.info("message=Verify new document is in the uploads grid");
        documentUploadAndStorage.document(filename);
        logger.info("message=Click Save button");
        documentUploadAndStorage.clickSaveDocumentsButton();

        Timestamp uploadDate = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1));

        logger.info("message=Verify document is stored in DB");
        List<DocStore> docStoreList = docStoreDao.getDocStoreList(filename, uploadDate);
        Assert.assertEquals(docStoreList.size(), 1);

        new DocStoreUtils(docStoreDao).verifyDocStore(docStoreList.get(0), category, categoryId, documentType, contentType, null,  uploadBy, AUD_USER_PREFIX+uploadBy, true, false, true);

        logger.info("message=Click Upload Documents button again");
        documentUploadAndStorage.uploadDocumentButton().click();
        logger.info("message=Upload a different file with the same content, filename2=" + filename2);
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename2);
        logger.info("message=Set Category dropdown, value=" + category);
        selectItem(documentUploadAndStorage.categoryDropdown(), category);
        logger.info("message=Set Category ID, value=" + categoryId2);
        documentUploadAndStorage.setCategoryId(categoryId2);
        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.uploadButton().click();
        logger.info("message=Verify that an error message is displayed");
        String errorMsg = documentUploadAndStorage.dialogUploadConfirm().getText();
        Assert.assertTrue(StringUtils.containsIgnoreCase(errorMsg, MATCHING_CONTENT_ERROR), "Error message not found, expectedError="+MATCHING_CONTENT_ERROR+", actualMsg="+errorMsg);
        logger.info("message=Close error popup");
        documentUploadAndStorage.clickDialogUploadErrorCancelButton();
        documentUploadAndStorage.cancelUploadButton().click();
    }

    @Test(priority = 1, description = "Uploaded Pending File Can Be Deleted")
    @Parameters({"filename", "category", "categoryId", "documentType", "contentType", "uploadBy"})
    public void testRPM_760(String filename, String category, String categoryId, String documentType, String contentType, String uploadBy) throws Exception
    {
        logger.info("message=Clean DocStore records, filename=" + filename);
        deleteDocStoreItem(filename);

        logger.info("message=Verify FM Document Upload And Storage page is displayed");
        DocumentUploadAndStorage documentUploadAndStorage = new DocumentUploadAndStorage(driver, wait);
        verifyPageTitle(documentUploadAndStorage);

        logger.info("message=Click on Upload Document button");
        documentUploadAndStorage.uploadDocumentButton().click();
        logger.info("message=Upload document, filename=" + filename);
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename);
        logger.info("message=Set Category dropdown, value=" + category);
        selectItem(documentUploadAndStorage.categoryDropdown(), category);
        logger.info("message=Set Category ID, value=" + categoryId);
        documentUploadAndStorage.setCategoryId(categoryId);
        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.clickUploadButton();
        logger.info("message=Verify new document is in the uploads grid");
        documentUploadAndStorage.document(filename);
        logger.info("message=Click Save button");
        documentUploadAndStorage.clickSaveDocumentsButton();

        Timestamp uploadDate = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1));

        logger.info("message=Verify document is stored in DB");
        List<DocStore> docStoreList = docStoreDao.getDocStoreList(filename, uploadDate);
        Assert.assertEquals(docStoreList.size(), 1);

        new DocStoreUtils(docStoreDao).verifyDocStore(docStoreList.get(0), category, categoryId, documentType, contentType, null,  uploadBy, AUD_USER_PREFIX+uploadBy, true, false, true);

        logger.info("message=Select Pending documents");
        selectItemByVal(documentUploadAndStorage.viewByDropdownList(), "Pending");
        clickHiddenPageObject(documentUploadAndStorage.loadDocumentButton(), 0);

        logger.info("message=Verify that newly uploaded documents is displayed");
        Assert.assertEquals(documentUploadAndStorage.documentLinks(filename).getText(), filename);

        logger.info("message=Select Delete checkbox");
        clickHiddenPageObject(documentUploadAndStorage.checkboxOnDocument(docStoreList.get(0).getSeqId()), 0);
        clickHiddenPageObject(documentUploadAndStorage.deleteSelectedRowButton(), 0);

        logger.info("message=Click Yes in Confirmation popup window");
        clickHiddenPageObject(documentUploadAndStorage.confirmDeleteButton(), 0);

        logger.info("message=Verify deleted document is removed from the Current Documents grid");
        Assert.assertNull(documentUploadAndStorage.documentNotPresent(filename));

        logger.info("message=Verify deleted document is marked as Deleted in the DB");
        docStoreList = docStoreDao.getDocStoreList(filename, uploadDate);
        Assert.assertEquals(docStoreList.size(), 1);
        new DocStoreUtils(docStoreDao).verifyDocStore(docStoreList.get(0), category, categoryId, documentType, contentType, null,  uploadBy, AUD_USER_PREFIX+uploadBy, true, true, true);
    }

    @Test(priority = 1, description = "Upload Accession Document To Local Storage")
    @Parameters({"filename", "categoryId", "documentType", "contentType", "uploadBy", "comment"})
    public void testRPM_763(String filename, String categoryId, String documentType, String contentType, String uploadBy, String comment) throws Exception
    {
        String category = "Accession";

        logger.info("message=Clean DocStore records, filename=" + filename);
        deleteDocStoreItem(filename);

        logger.info("message=Verify FM Document Upload And Storage page is displayed");
        DocumentUploadAndStorage documentUploadAndStorage = new DocumentUploadAndStorage(driver, wait);
        verifyPageTitle(documentUploadAndStorage);

        logger.info("message=Click on Upload Document button");
        documentUploadAndStorage.uploadDocumentButton().click();
        logger.info("message=Upload document, filename=" + filename);
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename);
        logger.info("message=Set Category dropdown, value=" + category);
        selectItem(documentUploadAndStorage.categoryDropdown(), category);
        logger.info("message=Search for Accession ID, value=" + categoryId);
        documentUploadAndStorage.categoryIdSearchByAccessionButton().click();
        String parent = switchToPopupWin();
        DSAccessionSearch accessionSearch = new DSAccessionSearch(driver);
        accessionSearch.inputAccnID(categoryId);
        clickHiddenPageObject(accessionSearch.searchButton(), 0);
        String accnId = accessionSearch.searchGrid(2, 2).getText();
        logger.info("message=Verify Accession ID is found, value=" + categoryId);
        Assert.assertEquals(accnId, categoryId);
        clickHiddenPageObject(accessionSearch.searchGrid(2, 2), 0);
        switchToWin(parent);

        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Set Comment, value=" + comment);
        documentUploadAndStorage.setCommentInput(comment);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.clickUploadButton();
        logger.info("message=Verify new document is in the uploads grid");
        documentUploadAndStorage.document(filename);
        logger.info("message=Click Save button");
        documentUploadAndStorage.clickSaveDocumentsButton();

        Timestamp uploadDate = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1));

        logger.info("message=Verify document is stored in DB");
        List<DocStore> docStoreList = docStoreDao.getDocStoreList(filename, uploadDate);
        Assert.assertEquals(docStoreList.size(), 1);

        DocStoreUtils docStoreUtils = new DocStoreUtils(docStoreDao);
        docStoreUtils.verifyDocStore(docStoreList.get(0), category, categoryId, documentType, contentType, comment, uploadBy, AUD_USER_PREFIX+uploadBy, false, false, true);
        Assert.assertEquals(docStoreList.get(0).getDocStoreLocationId(), 1);

        logger.info("message=Load Accession Documents");
        selectItemByVal(documentUploadAndStorage.viewByDropdownList(), "Accession");
        documentUploadAndStorage.setLoadDocumentInput(categoryId);
        documentUploadAndStorage.loadDocumentButton().click();

        logger.info("message=Verify document is displayed in grid");
        DocStoreTyp docStoreTyp = docStoreDao.getDocStoreTyp(docStoreList.get(0).getDocStoreTyp());
        Assert.assertEquals(documentUploadAndStorage.documentLinks(filename).getText(), filename);
        Assert.assertEquals(documentUploadAndStorage.uploadDateOnDocument(docStoreList.get(0).getSeqId()).getText(), DATE_FORMAT_MMDDYYYY.format(uploadDate));
        Assert.assertEquals(documentUploadAndStorage.uploadByOnDocument(docStoreList.get(0).getSeqId()).getText(), uploadBy);
        Assert.assertEquals(documentUploadAndStorage.documentTypeOnDocument(docStoreList.get(0).getSeqId()).getText(), docStoreTyp.getAbbrev()+" - "+docStoreTyp.getDescr());

        logger.info("message=Verify document can be opened");
        documentUploadAndStorage.documentLinks(filename).click();
        String expectedFileContent = FileUtils.readFileToString(new File(getClass().getResource("/"+filename).getFile()));
        expectedFileContent = StringUtils.normalizeSpace(expectedFileContent);
        parent = switchToPopupWin();
        String pageContent = StringUtils.normalizeSpace(driver.getPageSource());
        Assert.assertTrue(StringUtils.contains(pageContent, expectedFileContent));
        switchToWin(parent);

        logger.info("message=Verify DocStoreAudit is created");
        List<DocStoreAudit> docStoreAudits = docStoreDao.getDocStoreAudits(docStoreList.get(0).getSeqId());
        Assert.assertEquals(docStoreAudits.size(), 1, "DocStoreAudit record count is incorrect");
        docStoreUtils.verifyDocStoreAudit(docStoreAudits.get(0), docStoreList.get(0).getSeqId(), 1, AUD_USER_PREFIX+uploadBy);
    }

    @Test(priority = 1, description = "Upload Accession Document To S3")
    @Parameters({"filename", "categoryId", "documentType", "contentType", "uploadBy", "comment"})
    public void testUploadAccessionDocumentToS3(String filename, String categoryId, String documentType, String contentType, String uploadBy, String comment) throws Exception
    {
        String category = "Accession";

        logger.info("message=Clean DocStore records, filename=" + filename);
        deleteDocStoreItem(filename);

        logger.info("message=Verify FM Document Upload And Storage page is displayed");
        DocumentUploadAndStorage documentUploadAndStorage = new DocumentUploadAndStorage(driver, wait);
        verifyPageTitle(documentUploadAndStorage);

        logger.info("message=Click on Upload Document button");
        documentUploadAndStorage.uploadDocumentButton().click();
        logger.info("message=Upload document, filename=" + filename);
        uploadFile(documentUploadAndStorage.xfnUploaderFile(), filename);
        logger.info("message=Set Category dropdown, value=" + category);
        selectItem(documentUploadAndStorage.categoryDropdown(), category);
        logger.info("message=Search for Accession ID, value=" + categoryId);
        documentUploadAndStorage.categoryIdSearchByAccessionButton().click();
        String parent = switchToPopupWin();
        DSAccessionSearch accessionSearch = new DSAccessionSearch(driver);
        accessionSearch.inputAccnID(categoryId);
        clickHiddenPageObject(accessionSearch.searchButton(), 0);
        String accnId = accessionSearch.searchGrid(2, 2).getText();
        logger.info("message=Verify Accession ID is found, value=" + categoryId);
        Assert.assertEquals(accnId, categoryId);
        clickHiddenPageObject(accessionSearch.searchGrid(2, 2), 0);
        switchToWin(parent);

        logger.info("message=Set Document Type, value=" + documentType);
        documentUploadAndStorage.setDocumentType(documentType);
        logger.info("message=Set Comment, value=" + comment);
        documentUploadAndStorage.setCommentInput(comment);
        logger.info("message=Click Upload button");
        documentUploadAndStorage.clickUploadButton();
        logger.info("message=Verify new document is in the uploads grid");
        documentUploadAndStorage.document(filename);
        logger.info("message=Click Save button");
        documentUploadAndStorage.clickSaveDocumentsButton();

        Timestamp uploadDate = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1));

        logger.info("message=Verify document is stored in DB");
        List<DocStore> docStoreList = docStoreDao.getDocStoreList(filename, uploadDate);
        Assert.assertEquals(docStoreList.size(), 1);

        DocStoreUtils docStoreUtils = new DocStoreUtils(docStoreDao);
        docStoreUtils.verifyDocStore(docStoreList.get(0), category, categoryId, documentType, contentType, comment, uploadBy, AUD_USER_PREFIX+uploadBy, false, false, true);
        Assert.assertEquals(docStoreList.get(0).getDocStoreLocationId(), 2);

        logger.info("message=Load Accession Documents");
        selectItemByVal(documentUploadAndStorage.viewByDropdownList(), "Accession");
        documentUploadAndStorage.setLoadDocumentInput(categoryId);
        documentUploadAndStorage.loadDocumentButton().click();

        logger.info("message=Verify document is displayed in grid");
        DocStoreTyp docStoreTyp = docStoreDao.getDocStoreTyp(docStoreList.get(0).getDocStoreTyp());
        Assert.assertEquals(documentUploadAndStorage.documentLinks(filename).getText(), filename);
        Assert.assertEquals(documentUploadAndStorage.uploadDateOnDocument(docStoreList.get(0).getSeqId()).getText(), DATE_FORMAT_MMDDYYYY.format(uploadDate));
        Assert.assertEquals(documentUploadAndStorage.uploadByOnDocument(docStoreList.get(0).getSeqId()).getText(), uploadBy);
        Assert.assertEquals(documentUploadAndStorage.documentTypeOnDocument(docStoreList.get(0).getSeqId()).getText(),docStoreTyp.getAbbrev()+" - "+docStoreTyp.getDescr());

        logger.info("message=Verify document can be opened");
        documentUploadAndStorage.documentLinks(filename).click();
        String expectedFileContent = FileUtils.readFileToString(new File(getClass().getResource("/"+filename).getFile()));
        expectedFileContent = StringUtils.normalizeSpace(expectedFileContent);
        parent = switchToPopupWin();
        String pageContent = StringUtils.normalizeSpace(driver.getPageSource());
        Assert.assertTrue(StringUtils.contains(pageContent, expectedFileContent));
        switchToWin(parent);

        logger.info("message=Verify DocStoreAudit is created");
        List<DocStoreAudit> docStoreAudits = docStoreDao.getDocStoreAudits(docStoreList.get(0).getSeqId());
        Assert.assertEquals(docStoreAudits.size(), 1, "DocStoreAudit record count is incorrect");
        docStoreUtils.verifyDocStoreAudit(docStoreAudits.get(0), docStoreList.get(0).getSeqId(), 2, AUD_USER_PREFIX+uploadBy);
    }

//	@Test(priority = 1, description = "Upload single valid file with category is Payor")
//	@Parameters({"email", "password","fileName"})
//	public void testRPM_762(String email, String password, String fileName) throws Exception {
//    	logger.info("*** Testing - testRPM_762 ***");
//		payorSearch = new DSPayorSearch(driver);
//		PayorSearchResult = new DSPayorSearchResult(driver);
//
////		logger.info("*** Step 1 - Log into Mars with username and password");
////		ssoLogin = new SsoLogin(driver);
////		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Upload document with a valid Single file with category is Payor");
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//		selectItem(fileMaintenceDocumentUploadAndStorage.categoryDropdownList(),"Payor");
//
//		logger.info("*** Step 4 Action: Search for a Payor ID");
//		Assert.assertTrue(isElementPresent(payorSearch.payorIdSearchIcon(), 5),"        The Payor Id Search icon should be displayed");
//		payorSearch.clickPayorIdSearchIcon();
//		String parent = switchToPopupWin();
//
//		Assert.assertTrue(isElementPresent(payorSearch.payorIdTextbox(), 5),"        The Payor Id Search textbox should be displayed");
//		payorSearch.enterPayorId("*");
//		Assert.assertTrue(isElementPresent(payorSearch.searchBtn(), 5),"        The Search button should be displayed");
//		clickHiddenPageObject(payorSearch.searchBtn(),0);
//		switchToPopupWin();
//
//		logger.info("*** Step 5 Action: Select a Payor ID from the Payor Search Results window");
//		String payorId = PayorSearchResult.payorSearchResultGrid(4, 2).getText();
//		Assert.assertTrue(isElementPresent(PayorSearchResult.payorSearchResultTbl(), 5),"        The Search Result table should be displayed");
//		clickHiddenPageObject(PayorSearchResult.payorSearchResultGrid(4, 2),0);
//		switchToParentWin(parent);
//
//		logger.info("*** Step 6 Action: Upload the document");
//		ArrayList<String>docTypList = daoManagerPlatform.getRandomDocStoreTypFromDOCSTORETYP(testDb);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.documentTypeUploadDocDropdownList(), 5),"        The button in Upload Document popup should be displayed");
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.documentTypeUploadDocDropdownList(),docTypList.get(0));
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5),"        The Upload button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.UploadBtn(),0);
//
//		waitUntilElementIsNotVisible(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 10);
//
//		logger.info("*** Step 6 Expected result: Verify that new document is displayed in Current Documents grid");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(fileName),5),"        Document file name " +  fileName + " should show.");
//
//		logger.info("*** Step 7 Action: click Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5), "        Save button should show.");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 7 Expected Results: - Verify that the Document is saved into DB with correct data");
//		List<String>list = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		timeStamp = new TimeStamp();
//		String date = timeStamp.getCurrentDate();
//		assertEquals(list.get(2),fileName ,"        Document name should be " + fileName);
//		assertEquals(list.get(6),"Payor" ,"        Document Category should be Payor");
//		assertEquals(list.get(3),date,"        Upload Date should be " + date);
//
//		logger.info("*** Step 8 Actions: Select Payor in View By dropdown  - Enter Payor Id - Click on Load Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(), 5),"        The View By dropdown should be displayed");
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(),"Payor");
//
//		logger.info("*** Step 9 Actions: Enter Payor Id");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentInput(), 5),"        The Load Document should be displayed");
//		fileMaintenceDocumentUploadAndStorage.inputLoadDocument(payorId);
//
//		logger.info("*** Step 10 Actions: Click Load Documents button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(), 5),"        The Load button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(),0);
//
//		logger.info("*** Step 10 Expected Results: - New Document is displayed with correct infomation");
//		Assert.assertEquals(isElementPresent(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName),5),true,"        New Document: " + fileName + " should be displayed.");
//
//		logger.info("*** Step 11 Action: Clear test data in DB");
//		int seqId = Integer.parseInt(list.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqId, testDb);
//    }
//
//	@Test(priority = 1, description = "Verify user can view Engine upload file")
//	@Parameters({"email", "password","engineLocationJar","engineBatFile","timeOutWaitForEngine", "inboundLocation","completedLocation"})
//	public void testRPM_779(String email, String password,String engineLocationJar, String engineBatFile,String timeOutWaitForEngine,String inboundLocation, String completedLocation) throws Exception {
//    	logger.info("***** Testing - testRPM_779 *****");
//
//		logger.info("*** Step 1 Actions: - Prepare document file and run engine document upload");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		List<String> accnFileNameList = fileMaintenceDocumentUploadAndStorage.createAccnDocumentFileName("",this,testDb);
//		String accnId = accnFileNameList.get(0);
//		String documentFileName = accnFileNameList.get(2);
//		EngineUtils engineUtils = new EngineUtils(driver);
//		engineUtils.creatFileInbound(documentFileName,inboundLocation);
//		engineUtils.runEngine(engineLocationJar, engineBatFile, Integer.parseInt(timeOutWaitForEngine));
//
//		logger.info("*** Step 1 Expected Results: - Verify that document uploaded");
//		File fileUploaded = new File(completedLocation+"\\"+documentFileName);
//		List<String> listDoc= daoManagerPlatform.getDocumentFromDOCSTOREByFileName(documentFileName,testDb);
//		Assert.assertTrue(this.isFileExists(fileUploaded, 5),"       File must be uploaded");
//		Assert.assertTrue(listDoc.size()>0,"       Document must added to datbase");
//
//		/*
//		logger.info("*** Step 2 Actions: - Log into RPM and navigate to Document Upload and Storage");
//		ssoLogin = new SsoLogin(driver);
//		ssoLogin.login(email, password);
//
//		logger.info("*** Step 2 Expected Results: - Document upload and Storage page displayed");
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(),5),"        Document upload button must display");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(),5),"        Load document button must display");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(),5),"        View by drop down must display");
//
//		logger.info("*** Step 3 Actions: - Load document by accession ID");
//		fileMaintenceDocumentUploadAndStorage.selectviewByDropdownList("Accession");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentInput(),5),"        Load document input must display");
//		fileMaintenceDocumentUploadAndStorage.loadDocument(accID);
//
//		logger.info("*** Step 3 Expected Results: - Document uploaded by engine display");
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.documentLinks(documentFileName).getText().equalsIgnoreCase(documentFileName),"        Document uploaded must display");
//
//		logger.info("*** Step 4 Actions: - Click to document file link to open");
//		fileMaintenceDocumentUploadAndStorage.clickToDocumentLinkOnGrid(documentFileName);
//		switchToPopupWin();
//
//		logger.info("*** Step 4 Expected Results: - Document is display");
//		Assert.assertTrue(driver.getWindowHandle().length()>0,"        Document uploaded must display");
//
//		int seqDoc = Integer.parseInt(listDoc.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqDoc,testDb);
//		*/
//		driver.close();
//    }
//
//	@Test(priority = 1, description = "Edit existing document with new category")
//	@Parameters({"email", "password","fileName"})
//	public void testRPM_771(String email, String password, String fileName) throws Exception {
//    	logger.info("*** Testing - testRPM_771 ***");
//
//		logger.info("*** Step 1 - Log into Mars with username and password");
//		ssoLogin = new SsoLogin(driver);
//		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Upload document with a valid Single file");
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5), "        Upload button should show.");
//		fileMaintenceDocumentUploadAndStorage.clickUploadBtn();
//
//		waitUntilElementIsNotVisible(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 10);
//
//		logger.info("*** Step 3 Expected result: Verify that new document is displayed in Current Documents grid");
//		timeStamp = new TimeStamp(driver);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(fileName),5),"        Document file name " +  fileName + " should show.");
//
//		logger.info("*** Step 4 Action: click on Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5), "        Save button should show.");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 4 Expected result: Verify that the new document is saved into DB" );
//		List<String>list = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		assertEquals(list.get(2),fileName,"        New Document " + fileName + " is saved into database.");
//		assertEquals(list.get(6),"","        New Document " + fileName + " is displayed under UNLINK");
//
//		logger.info("*** Step 5 Actions: - Load the uploaded document and edit information");
//		fileMaintenceDocumentUploadAndStorage.selectviewByDropdownList("Unlinked");
//		fileMaintenceDocumentUploadAndStorage.clickLoadDocumentBtn();
//		fileMaintenceDocumentUploadAndStorage.editDocumentLinkOnGrid(fileName);
//
//		logger.info("*** Step 5 Expected Results: - Verify edit popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.editCategoryDropdownList(), 5),"        Edit Category dropdown should show.");
//
//		logger.info("*** Step 6 Actions: - Select Client in Category and enter a Client ID in Category ID Input filed and click save button");
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.editCategoryDropdownList(),"Client");
//		String clientID = daoManagerPlatform.getRandomClnWithSubmSvcInfoFromCLN(testDb).get(0);
//		fileMaintenceDocumentUploadAndStorage.enterCategoryIdEditAccessionInput(clientID);
//		fileMaintenceDocumentUploadAndStorage.clickSaveEditBtn();
//
//		logger.info("*** Step 6 Expected Results: - The updated information is saved into DB");
//		Thread.sleep(5000);
//		List<String> docAfterUpdate = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		Assert.assertTrue(docAfterUpdate.get(1).equalsIgnoreCase(clientID),"        The value in client ID field in DB should be updated.");
//
//		logger.info("*** Step 7 Actions: - Load the updated document again and verify that it has been updated with new client ID");
//		fileMaintenceDocumentUploadAndStorage.selectviewByDropdownList("Client");
//		fileMaintenceDocumentUploadAndStorage.loadDocument(clientID);
//
//		logger.info("*** Step 7 Expected Results: - Verify that  the document is displayed");
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName).getText().equalsIgnoreCase(fileName),"        The uploaded document: " + fileName + " should be displayed.");
//
//		logger.info("*** Step 8 Action: Clear test data in DB");
//		int seqId = Integer.parseInt(list.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqId, testDb);
//    }
//
//	@Test(priority = 1, description = "Edit existing document with new category and invalid categoryId")
//	@Parameters({"email", "password","fileName"})
//	public void testRPM_772(String email, String password, String fileName) throws Exception {
//    	logger.info("*** Testing - testRPM_772 ***");
//
//		logger.info("*** Step 1 - Log into Mars with username and password");
//		ssoLogin = new SsoLogin(driver);
//		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Upload document with a valid Single file");
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//		selectItem(fileMaintenceDocumentUploadAndStorage.categoryDropdownList(),"Client");
//
//		String clientId = daoManagerXifinRpm.getClientId(testDb);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.categoryIdInput(), 5),"        Category ID Input field should show.");
//		fileMaintenceDocumentUploadAndStorage.enterCategoryIdInput(clientId);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5), "        Upload button should show.");
//		fileMaintenceDocumentUploadAndStorage.clickUploadBtn();
//
//		waitUntilElementIsNotVisible(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 10);
//
//		logger.info("*** Step 3 Expected result: Verify that new document is displayed in Current Documents grid");
//		timeStamp = new TimeStamp(driver);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(fileName),5),"        Document file name " +  fileName + " should show.");
//
//		logger.info("*** Step 4 Action: click on Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5), "        Save button should show.");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 4 Expected result: Verify that the new document is saved into DB" );
//		Assert.assertTrue(daoManagerClientPortal.isUploadedFileHasSaved(fileName, clientId),"        The new document " + fileName + " should be saved into DOC_STORE table.");
//
//		logger.info("*** Step 5 Actions: - Load the updated document and edit information");
//		fileMaintenceDocumentUploadAndStorage.selectviewByDropdownList("Client");
//		fileMaintenceDocumentUploadAndStorage.loadDocument(clientId);
//
//		logger.info("*** Step 5 Expected Results: - Verify that document is displayed");
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName).getText().equalsIgnoreCase(fileName),"        Document uploaded must display");
//
//		logger.info("*** Step 6 Actions: - Edit document");
//		fileMaintenceDocumentUploadAndStorage.editDocumentLinkOnGrid(fileName);
//
//		logger.info("*** Step 6 Expected Results: - Verify edit popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.editCategoryDropdownList(), 5),"        Edit Category drop down must display");
//
//		logger.info("*** Step 7 Actions: - Select Payor in Category and enter an invalid Payor Id in Category ID input field and click save");
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.editCategoryDropdownList(),"Payor");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.categoryIdEditAccessionInput(), 5),"        Category ID input field should display");
//		randomCharacter = new RandomCharacter(driver);
//		String invalidPayor = "INVALIDPYR"; // + randomCharacter.getRandomAlphaString(5);
//		fileMaintenceDocumentUploadAndStorage.enterCategoryIdEditAccessionInput(invalidPayor);
//
//		logger.info("*** Step 7 Expected Results: - Verify that error message shows");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.errorMessageContent(), 5),"        Error message should show.");
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.errorMessageContent().getText().equalsIgnoreCase("Payor "+invalidPayor+" does not exist in the system."),"        Error message " +  "Payor "+invalidPayor+" does not exist in the system. should show.");
//
//		fileMaintenceDocumentUploadAndStorage.clickCloseErrorMessage();
//		fileMaintenceDocumentUploadAndStorage.clickSaveEditBtn();
//
//		logger.info("*** Step 8 Actions: - Load the updated document and select Pending in View By dropdown");
//		fileMaintenceDocumentUploadAndStorage.selectviewByDropdownList("Pending");
//		fileMaintenceDocumentUploadAndStorage.clickLoadDocumentBtn();
//
//		logger.info("*** Step 8 Expected Results: - Verify that the document is display in the Pending category");
//		//Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName), 5),"        Document: " + fileName + " should show.");
//		Assert.assertTrue(getColumnValue(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), invalidPayor),"        Document : " + fileName + " should show with Pending category.");
//
//		logger.info("*** Step 9 Action: Clear test data in DB");
//		daoManagerClientPortal.deleteUploadedfileRecordFromDb(fileName, clientId);
//    }
//
//	@Test(priority = 1, description = " Edit Existing document with new document type")
//	@Parameters({"email", "password","fileName"})
//	public void testRPM_773(String email, String password, String fileName) throws Exception {
//    	logger.info("*** Testing - testRPM_773 ***");
//    	randomCharacter = new RandomCharacter(driver);
//
//		logger.info("*** Step 1 - Log into Mars with username and password");
//		ssoLogin = new SsoLogin(driver);
//		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Upload document with a valid Single file");
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//		selectItem(fileMaintenceDocumentUploadAndStorage.categoryDropdownList(),"Client");
//
//		String clientId = daoManagerXifinRpm.getClientId(testDb);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.categoryIdInput(), 5),"        Category ID Input field should show.");
//		fileMaintenceDocumentUploadAndStorage.enterCategoryIdInput(clientId);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5), "        Upload button should show.");
//		fileMaintenceDocumentUploadAndStorage.clickUploadBtn();
//
//		waitUntilElementIsNotVisible(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 10);
//
//		logger.info("*** Step 3 Expected result: Verify that new document is displayed in Current Documents grid");
//		timeStamp = new TimeStamp(driver);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(fileName),5),"        Document file name " +  fileName + " should show.");
//
//		logger.info("*** Step 4 Action: click on Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5), "        Save button should show.");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 4 Expected result: Verify that the new document is saved into DB" );
//		Assert.assertTrue(daoManagerClientPortal.isUploadedFileHasSaved(fileName, clientId),"        The new document " + fileName + " should be saved into DOC_STORE table.");
//
//		logger.info("*** Step 5 Actions: - Load the updated document and edit information");
//		fileMaintenceDocumentUploadAndStorage.selectviewByDropdownList("Client");
//		fileMaintenceDocumentUploadAndStorage.loadDocument(clientId);
//
//		logger.info("*** Step 5 Expected Results: - Verify that document is displayed");
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName).getText().equalsIgnoreCase(fileName),"        Document uploaded must display");
//
//		logger.info("*** Step 6 Actions: - Edit document");
//		fileMaintenceDocumentUploadAndStorage.editDocumentLinkOnGrid(fileName);
//
//		logger.info("*** Step 6 Expected Results: - Verify Edit popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.documentTypeDropdownList(), 5),"        Document type drop down must display");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.editDocumentCommentInput(), 5),"        Comment input must display");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveEditBtn(), 5),"        Save button must display");
//
//		logger.info("*** Step 7 Actions: - Select document type and enter new comments - Click save");
//		List<String> docTyp = daoManagerPlatform.getRandomDocStoreTypFromDOCSTORETYP(testDb);
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.documentTypeDropdownList(),docTyp.get(0));
//		String comment = randomCharacter.getRandomAlphaNumericString(16);
//		fileMaintenceDocumentUploadAndStorage.enterEditDocumentCommentInput(comment);
//		fileMaintenceDocumentUploadAndStorage.clickSaveEditBtn();
//		Thread.sleep(2000);
//
//		logger.info("*** Step 7 Expected Results: - Verify document type and comment display in the table");
//		String docTypeDisplay = docTyp.get(1)+ " - " + docTyp.get(2);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.documentCol(fileName,3).getText().equalsIgnoreCase(docTypeDisplay),"        Document type must display");
//		String cmt = fileMaintenceDocumentUploadAndStorage.documentColComment(fileName).getText();
//		Assert.assertTrue(cmt.equalsIgnoreCase(comment),"        Document comment must display");
//
//		logger.info("*** Step 8 Action: Clear test data in DB");
//		List<String> listDoc= daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName,testDb);
//		int seqDoc = Integer.parseInt(listDoc.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqDoc,testDb);
//    }
//
//
//	@Test(priority = 1, description = "Upload Single valid file with Category is System")
//	@Parameters({"email", "password","fileName"})
//	public void testRPM_764(String email, String password, String fileName) throws Exception {
//    	logger.info("*** Testing - testRPM_764 ***");
//    	dsAccessionSearch = new DSAccessionSearch(driver);
//
//		logger.info("*** Step 1 - Log into Mars with username and password");
//		ssoLogin = new SsoLogin(driver);
//		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Upload document with a valid Single file with category is System");
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//		selectItem(fileMaintenceDocumentUploadAndStorage.categoryDropdownList(),"System");
//
//		logger.info("*** Step 4 Action: - Select a Document Type from the dropdown, enter comments and click Upload button");
//		ArrayList<String>docTypList = daoManagerPlatform.getRandomDocStoreTypFromDOCSTORETYP(testDb);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.documentTypeUploadDocDropdownList(), 5),"        The button in Upload Document popup should be displayed");
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.documentTypeUploadDocDropdownList(),docTypList.get(0));
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.commentUploadDocDropdownList(), 5),"        The Comment area should be displayed");
//		fileMaintenceDocumentUploadAndStorage.enterUploadDocumentCommentInput("Test Comments");
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5),"        The Upload button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.UploadBtn(),0);
//
//		logger.info("*** Step 4 Expected result: Verify that the new document is displayed in Current Documents grid");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(fileName),5),"        Document file name " +  fileName + " should show.");
//
//		logger.info("*** Step 5 Actions: Click on Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5),"        The Save button should be displayed");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 5 Expected Results: - Verify that the Document is saved into DB with correct data");
//		List<String>list = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		timeStamp = new TimeStamp();
//		String date = timeStamp.getCurrentDate();
//		assertEquals(list.get(2),fileName ,"        Document name should be " + fileName);
//		assertEquals(list.get(6),"System" ,"        Document Category should be Accession");
//		assertEquals(list.get(3),date,"        Upload Date should be " + date);
//
//		logger.info("*** Step 7 Actions: Select View By is System - Click on Load Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(), 5),"        The View By dropdown should be displayed");
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(),"System");
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(), 5),"        The Load button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(),0);
//
//		List<String> listResult = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		String seqID = listResult.get(0);
//		String fileNameResult = listResult.get(2);
//		String updatedDate = listResult.get(3);
//		String updatedBy = listResult.get(4);
//
//		logger.info("*** Step 7 Expected Results: - New Document is displayed with correct infomation such as Document, upload by, upload date");
//		Assert.assertEquals(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName).getText(), fileNameResult,"        The Document Name should be displayed correctly");
//		Assert.assertEquals(fileMaintenceDocumentUploadAndStorage.uploadDateOnCurrentDocument(seqID).getText(), updatedDate,"        The Updated Date should be displayed correctly");
//		Assert.assertEquals(fileMaintenceDocumentUploadAndStorage.uploadByOnCurrentDocument(seqID).getText(), updatedBy,"        The Updated By should be displayed correctly");
//
//		logger.info("*** Step 8 Action: Clear test data in DB");
//		int seqId = Integer.parseInt(list.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqId, testDb);
//    }
//
//	@Test(priority = 1, description = "Upload single valid file with Category is Client")
//	@Parameters({"email", "password","fileName"})
//	public void testRPM_765(String email, String password, String fileName) throws Exception {
//    	logger.info("*** Testing - testRPM_765 ***");
//    	dsClientSearch = new DSClientSearch(driver);
//
//		logger.info("*** Step 1 - Log into Mars with username and password");
//		ssoLogin = new SsoLogin(driver);
//		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Upload document with a valid Single file with category is Accession");
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//		selectItem(fileMaintenceDocumentUploadAndStorage.categoryDropdownList(),"Client");
//
//		logger.info("*** Step 4 Action: Search for a Client ID");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.btnCategoryIDSearchByClient(), 5),"        The Category Id Search btn in Upload Document popup should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.btnCategoryIDSearchByClient(), 0);
//
//		String parent = switchToPopupWin();
//		Assert.assertTrue(isElementPresent(dsClientSearch.clientID(), 5),"        The Client ID input should be displayed");
//		dsClientSearch.inputClientID("*");
//
//		Assert.assertTrue(isElementPresent(dsClientSearch.searchBtnNew(), 5),"        The Search button should be displayed");
//		clickHiddenPageObject(dsClientSearch.searchBtnNew(), 0);
//
//		String clientID = dsClientSearch.clientSearchGrid(1, 1).getText();
//		Assert.assertTrue(isElementPresent(dsClientSearch.clientSearchGrid(1, 1), 5),"        The first row of Client Search should be displayed");
//		clickHiddenPageObject(dsClientSearch.clientSearchGrid(1, 1), 0);
//		switchToWin(parent);
//
//		logger.info("*** Step 5 Action: - Select a Document Type from the dropdown, enter comments and click Upload button");
//		ArrayList<String>docTypList = daoManagerPlatform.getRandomDocStoreTypFromDOCSTORETYP(testDb);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.documentTypeUploadDocDropdownList(), 5),"        The button in Upload Document popup should be displayed");
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.documentTypeUploadDocDropdownList(),docTypList.get(0));
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.commentUploadDocDropdownList(), 5),"        The Comment area should be displayed");
//		fileMaintenceDocumentUploadAndStorage.enterUploadDocumentCommentInput("Test Comments");
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5),"        The Upload button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.UploadBtn(),0);
//
//		logger.info("*** Step 5 Expected result: Verify that the new document is displayed in Current Documents grid");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(fileName),5),"        Document file name " +  fileName + " should show.");
//
//		logger.info("*** Step 6 Actions: Click on Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5),"        The Save button should be displayed");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 6 Expected Results: - Verify that the Document is saved into DB with correct data");
//		List<String>list = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		timeStamp = new TimeStamp();
//		String date = timeStamp.getCurrentDate();
//		assertEquals(list.get(2),fileName ,"        Document name should be " + fileName);
//		assertEquals(list.get(6),"Client" ,"        Document Category should be Client.");
//		assertEquals(list.get(3),date,"        Upload Date should be " + date);
//
//		logger.info("*** Step 7 Actions: Select View By is Client - Enter Client Id - Click on Load Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(), 5),"        The View By dropdown should be displayed");
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(),"Client");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentInput(), 5),"        The Load Document Input should be displayed");
//		fileMaintenceDocumentUploadAndStorage.inputLoadDocument(clientID);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(), 5),"        The Load button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(),0);
//
//		List<String> listResult = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		String seqID = listResult.get(0);
//		String fileNameResult = listResult.get(2);
//		String updatedDate = listResult.get(3);
//		String updatedBy = listResult.get(4);
//
//		logger.info("*** Step 7 Expected Results: - New Document is displayed with correct infomation such as Document, upload by, upload date");
//		Assert.assertEquals(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName).getText(), fileNameResult,"        The Document Name should be displayed correctly");
//		Assert.assertEquals(fileMaintenceDocumentUploadAndStorage.uploadDateOnCurrentDocument(seqID).getText(), updatedDate,"        The Updated Date should be displayed correctly");
//		Assert.assertEquals(fileMaintenceDocumentUploadAndStorage.uploadByOnCurrentDocument(seqID).getText(), updatedBy,"        The Updated By should be displayed correctly");
//
//		logger.info("*** Step 8 Action: Clear test data in DB");
//		int seqId = Integer.parseInt(list.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqId, testDb);
//    }
//
//	@Test(priority = 1, description = "Upload the same file name that was deleted before")
//	@Parameters({"email", "password","fileName"})
//	public void testRPM_766(String email, String password, String fileName) throws Exception {
//    	logger.info("*** Testing - testRPM_766 ***");
//
////		logger.info("*** Step 1 - Log into Mars with username and password");
////		ssoLogin = new SsoLogin(driver);
////		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Upload document with a valid Single file");
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5), "        Upload button should show.");
//		fileMaintenceDocumentUploadAndStorage.clickUploadBtn();
//
//		waitUntilElementIsNotVisible(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 10);
//
//		logger.info("*** Step 3 Expected result: Verify that new document is displayed in Current Documents grid");
//		timeStamp = new TimeStamp(driver);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(fileName),5),"        Document file name " +  fileName + " should show.");
//
//		logger.info("*** Step 4 Action: click on Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5), "        Save button should show.");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 4 Expected result: Verify that the new document is saved into DB" );
//		List<String>list = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		assertEquals(list.get(2),fileName,"        New Document " + fileName + " is saved into database.");
//		assertEquals(list.get(6),"","        New Document " + fileName + " is displayed under UNLINK");
//
//		logger.info("*** Step 5 Actions: Select UNLINK in View By dropdown and click Load Documents button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(), 5),"        The View By dropdown should be displayed");
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(),"Unlinked");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(), 5),"        The Load Document button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(),0);
//
//		logger.info("*** Step 5 Expected Results: - Verify that newly uploaded documents is displayed");
//		Assert.assertEquals(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName).getText(), fileName,"        Document: " + fileName + " should be displayed as unlinked document.");
//
//		logger.info("*** Step 6 Actions: Select the newly uploaded document and check Delete checkbox");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.checkboxOnCurrentDocument(list.get(0)),0);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.deleteSelctRowBtn(), 5),"        The Delete Selected button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.deleteSelctRowBtn(),0);
//
//		logger.info("*** Step 7 Actions: Click Yes in Confirmation popup window");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveDeleteBtn(), 5),"        The Yes button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.saveDeleteBtn(),0);
//
//		logger.info("*** Step 7 Expected Results: - Selected document is removed the unlinked document list and is marked as deleted in DB");
//		list = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		Assert.assertNull(fileMaintenceDocumentUploadAndStorage.documentNotPresent(fileName));
//		Assert.assertEquals(list.get(10),"1","        Document is marked as deleted in DB");
//
//		logger.info("*** Step 8 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 8 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 9 Action: Upload the same document");
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5), "        Upload button should show.");
//		fileMaintenceDocumentUploadAndStorage.clickUploadBtn();
//
//		waitUntilElementIsNotVisible(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 10);
//
//		logger.info("*** Step 9 Expected result: Verify that the document is displayed in Current Documents grid");
//		timeStamp = new TimeStamp(driver);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(fileName),5),"        Document file name " +  fileName + " should show.");
//
//		logger.info("*** Step 10 Action: click on Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5), "        Save button should show.");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 10 Expected result: Verify that the document is saved into DB" );
//		list = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		assertEquals(list.get(2),fileName,"        Document " + fileName + " is saved into database.");
//		assertEquals(list.get(6),"","        Document " + fileName + " is displayed under UNLINK");
//
//		logger.info("*** Step 11 Actions: Select UNLINK in View By dropdown and click Load Documents button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(), 5),"        The View By dropdown should be displayed");
//		selectItemByVal(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(),"Unlinked");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(), 5),"        The Load Document button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn(),0);
//
//		logger.info("*** Step 11 Expected Results: - Verify that the uploaded documents is displayed");
//		Assert.assertEquals(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName).getText(), fileName,"        Document: " + fileName + " should be displayed as unlinked document.");
//
//		logger.info("*** Step 12 Action: Clear test data in DB");
//		int seqId = Integer.parseInt(list.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqId, testDb);
//
//		list = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		seqId = Integer.parseInt(list.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqId, testDb);
//    }
//
//	@Test(priority = 1, description = "Upload with non-white list document")
//	@Parameters({"email", "password","fileName"})
//	public void testRPM_767(String email, String password, String fileName) throws Exception {
//    	logger.info("*** Testing - testRPM_767 ***");
//
////		logger.info("*** Step 1 - Log into Mars with username and password");
////		ssoLogin = new SsoLogin(driver);
////		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Try to upload a non-white list document");
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5), "        Upload button should show.");
//		fileMaintenceDocumentUploadAndStorage.clickUploadBtn();
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.continueUploadBtn(), 5),"        The Continue Upload button should be displayed");
//		clickHiddenPageObject(fileMaintenceDocumentUploadAndStorage.continueUploadBtn(),0);
//
//		logger.info("*** Step 3 Expected Results: - Verify that the invalid document error message is displayed and upload is not allowed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.warningMessageText(), 5),"        The Warning message should show.");
//		Assert.assertEquals(fileMaintenceDocumentUploadAndStorage.warningMessageText().getText(), "No valid document is selected. Please choose at least one valid document.","        The invalid document error message should show.");
//    }
//
//	@Test(priority = 1, description = "Verify parsing functionality for file name convention as 'AccnAccnIDDocumentType'")
//	@Parameters({"email", "password","accnId", "dsType"})
//	public void testRPM_775(String email, String password, String accnId, String dsType) throws Exception {
//    	logger.info("*** Testing - testRPM_775 ***");
//
////		logger.info("*** Step 1 - Log into Mars with username and password");
////		ssoLogin = new SsoLogin(driver);
////		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Upload document with a valid Single file with name convention as 'AccnAccnIDDocumentType'");
//		String fileName = "Accn" + accnId + dsType + ".txt";
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5), "        Upload button should show.");
//		fileMaintenceDocumentUploadAndStorage.clickUploadBtn();
//
//		waitUntilElementIsNotVisible(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 10);
//
//		logger.info("*** Step 3 Expected result: Verify that the new document is displayed in Current Documents grid");
//		timeStamp = new TimeStamp(driver);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(fileName),5),"        Document file name " +  fileName + " should show.");
//
//		logger.info("*** Step 4 Action: click on Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5), "        Save button should show.");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 4 Expected Results: Verify that the new document is save in DB with category is 'Assession' and category ID is AccessionID");
//
//		List<String> listDoc = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		String category = listDoc.get(6);
//		String docCatID = listDoc.get(2);
//		String catID    = listDoc.get(7);
//
//		Assert.assertEquals(category, "Accession", "        The Category should be Accession.");
//		Assert.assertEquals(catID, accnId, "        The Category ID should be Accession ID: " + accnId);
//		assertEquals(docCatID,fileName,"        New Document " + fileName + " is saved into database.");
//
//		logger.info("*** Step 5 Actions: Select view by Accession , Category ID is AccnID as file name and Click on Load Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(), 5),"        Dropdown should be displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentInput() , 5),"        Load Document input should be displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn()   , 5),"        Load Document button should be displayed");
//		fileMaintenceDocumentUploadAndStorage.selectviewByDropdownList("Accession");
//		fileMaintenceDocumentUploadAndStorage.inputLoadDocument(accnId);
//		fileMaintenceDocumentUploadAndStorage.clickLoadDocumentBtn();
//
//		logger.info("*** Step 5 Expected Results: New document is displayed with correct document type as file name");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName) ,5),"        New document is displayed with correct document type as file name");
//
//		logger.info("*** Step 6 Action: Clear test data in DB");
//		int seqId = Integer.parseInt(listDoc.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqId, testDb);
//    }
//
//	@Test(priority = 1, description = "Verify parsing functionality for file name convention as 'ClnClientIDDocumentType'")
//	@Parameters({"email", "password","clnId", "dsType"})
//	public void testRPM_777(String email, String password, String clnId, String dsType) throws Exception {
//    	logger.info("*** Testing - testRPM_777 ***");
//
////		logger.info("*** Step 1 - Log into Mars with username and password");
////		ssoLogin = new SsoLogin(driver);
////		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Upload document with a valid Single file with name convention as 'ClnClientIDDocumentType'");
//		String fileName = "Cln" + clnId + dsType + ".txt";
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, fileName);
//		Thread.sleep(3000);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5), "        Upload button should show.");
//		fileMaintenceDocumentUploadAndStorage.clickUploadBtn();
//
//		waitUntilElementIsNotVisible(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 10);
//
//		logger.info("*** Step 3 Expected result: Verify that the new document is displayed in Current Documents grid");
//		timeStamp = new TimeStamp(driver);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(fileName),5),"        Document file name " +  fileName + " should show.");
//
//		logger.info("*** Step 4 Action: click on Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5), "        Save button should show.");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 4 Expected Results: Verify that the new document is save in DB with category is 'Client' and category ID is ClientID");
//		List<String> listDoc = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(fileName, testDb);
//		String category = listDoc.get(6);
//		String docCatID = listDoc.get(2);
//		String catID    = listDoc.get(7);
//
//		Assert.assertEquals(category, "Client", "        The Category should be Client.");
//		Assert.assertEquals(catID, clnId, "        The Category ID should be Client ID: " + clnId);
//		assertEquals(docCatID,fileName,"        New Document " + fileName + " is saved into database.");
//
//		logger.info("*** Step 5 Actions: Select view by Client , Category ID is ClientID as file name and Click on Load Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(), 5),"        Dropdown should be displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentInput() , 5),"        Load Document input should be displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn()   , 5),"        Load Document button should be displayed");
//		fileMaintenceDocumentUploadAndStorage.selectviewByDropdownList("Client");
//		fileMaintenceDocumentUploadAndStorage.inputLoadDocument(clnId);
//		fileMaintenceDocumentUploadAndStorage.clickLoadDocumentBtn();
//
//		logger.info("*** Step 5 Expected Results: New document is displayed with correct document type as file name");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.documentLinks(fileName) ,5),"        New document is displayed with correct document type as file name");
//
//		logger.info("*** Step 6 Action: Clear test data in DB");
//		int seqId = Integer.parseInt(listDoc.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqId, testDb);
//    }
//
//	@Test(priority = 1, description = "Verify parsing functionality for multiple files that name convention as 'AccnAccnIDDocumentType' and 'ClnClientIDDocumentType'")
//	@Parameters({"email", "password","accnId", "clnId", "accnDSType", "clnDSType"})
//	public void testRPM_778(String email, String password, String accnId, String clnId, String accnDSType, String clnDSType) throws Exception {
//    	logger.info("*** Testing - testRPM_778 ***");
//
////		logger.info("*** Step 1 - Log into Mars with username and password");
////		ssoLogin = new SsoLogin(driver);
////		ssoLogin.login(email, password);
//
//		logger.info("*** Step 1 Expected result: Document Upload & Storage page is displayed");
//		fileMaintenceDocumentUploadAndStorage = new DocumentUploadAndStorage(driver);
//		Assert.assertTrue(fileMaintenceDocumentUploadAndStorage.docStorageTitle().getText().trim().contains("Document Upload And Storage"),"        Document Upload & Storage is displayed");
//
//		logger.info("*** Step 2 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 2 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 3 Action: Upload document with a valid Single file with name convention as 'AccnAccnIDDocumentType'");
//		String accnFileName = "Accn" + accnId + accnDSType + ".txt";
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, accnFileName);
//		Thread.sleep(3000);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5), "        Upload button should show.");
//		fileMaintenceDocumentUploadAndStorage.clickUploadBtn();
//
//		waitUntilElementIsNotVisible(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 10);
//
//		logger.info("*** Step 3 Expected result: Verify that the new document is displayed in Current Documents grid");
//		timeStamp = new TimeStamp(driver);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(accnFileName),5),"        Document file name " +  accnFileName + " should show.");
//
//		logger.info("*** Step 4 Action - Click on Upload Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.uploadDocumentBtn(), 5), "        Upload Documents button should show.");
//		fileMaintenceDocumentUploadAndStorage.launchUploadDocumentPopup();
//
//		logger.info("*** Step 4 Expected result: Upload Document popup is displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.xfnUploaderFile(), 5));
//
//		logger.info("*** Step 5 Action: Upload document with a valid Single file with name convention as 'ClnClientIDDocumentType'");
//		String clnFileName = "Cln" + clnId + clnDSType + ".txt";
//		fileMaintenceDocumentUploadAndStorage.chooseFileUpload(this, clnFileName);
//		Thread.sleep(3000);
//
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 5), "        Upload button should show.");
//		fileMaintenceDocumentUploadAndStorage.clickUploadBtn();
//
//		waitUntilElementIsNotVisible(fileMaintenceDocumentUploadAndStorage.UploadBtn(), 10);
//
//		logger.info("*** Step 5 Expected result: Verify that the new document is displayed in Current Documents grid");
//		timeStamp = new TimeStamp(driver);
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.curDocumentTbl(), 5), "        Current Documents grid should show.");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.document(clnFileName),5),"        Document file name " +  clnFileName + " should show.");
//
//		logger.info("*** Step 6 Action: click on Save button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.saveBtn(), 5), "        Save button should show.");
//		fileMaintenceDocumentUploadAndStorage.submitDocument();
//		Thread.sleep(5000);
//
//		logger.info("*** Step 6 Expected Results: Verify that the new Accession document is saved in DB with category is 'Assession' and category ID is AccessionID ");
//
//		List<String> listDoc = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(accnFileName, testDb);
//		String category = listDoc.get(6);
//		String docCatID = listDoc.get(2);
//		String catID    = listDoc.get(7);
//
//		Assert.assertEquals(category, "Accession", "        The Category should be Accession.");
//		Assert.assertEquals(catID, accnId, "        The Category ID should be Accession ID: " + accnId);
//		assertEquals(docCatID,accnFileName,"        New Document " + accnFileName + " is saved into database.");
//
//		logger.info("*** Step 7 Actions: Select view by Accession , Category ID is AccnID as file name and Click on Load Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(), 5),"        Dropdown should be displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentInput() , 5),"        Load Document input should be displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn()   , 5),"        Load Document button should be displayed");
//		fileMaintenceDocumentUploadAndStorage.selectviewByDropdownList("Accession");
//		fileMaintenceDocumentUploadAndStorage.inputLoadDocument(accnId);
//		fileMaintenceDocumentUploadAndStorage.clickLoadDocumentBtn();
//
//		logger.info("*** Step 7 Expected Results: New document is displayed with correct document type as file name");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.documentLinks(accnFileName) ,5),"        New document is displayed with correct document type as file name");
//
//		logger.info("*** Step 8 Action: Clear test data for Accession document in DB");
//		int seqId = Integer.parseInt(listDoc.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqId, testDb);
//
//		logger.info("*** Step 9 Actions: Verify that the new Client document is save in DB with category is 'Client' and category ID is ClientID");
//		listDoc = daoManagerPlatform.getDocumentFromDOCSTOREByFileName(clnFileName, testDb);
//		category = listDoc.get(6);
//		docCatID = listDoc.get(2);
//		catID    = listDoc.get(7);
//
//		Assert.assertEquals(category, "Client", "        The Category should be Client.");
//		Assert.assertEquals(catID, clnId, "        The Category ID should be Client ID: " + clnId);
//		assertEquals(docCatID,clnFileName,"        New Document " + clnFileName + " is saved into database.");
//
//		logger.info("*** Step 10 Actions: Select view by Client , Category ID is ClientID as file name and Click on Load Document button");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.viewByDropdownList(), 5),"        Dropdown should be displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentInput() , 5),"        Load Document input should be displayed");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.loadDocumentBtn()   , 5),"        Load Document button should be displayed");
//		fileMaintenceDocumentUploadAndStorage.selectviewByDropdownList("Client");
//		fileMaintenceDocumentUploadAndStorage.inputLoadDocument(clnId);
//		fileMaintenceDocumentUploadAndStorage.clickLoadDocumentBtn();
//
//		logger.info("*** Step 10 Expected Results: New Client document is displayed with correct document type as file name");
//		Assert.assertTrue(isElementPresent(fileMaintenceDocumentUploadAndStorage.documentLinks(clnFileName) ,5),"        New document is displayed with correct document type as file name");
//
//		logger.info("*** Step 11 Action: Clear test data in DB");
//		seqId = Integer.parseInt(listDoc.get(0));
//		daoManagerPlatform.deleteDoc_StoreBySeqID(seqId, testDb);
//    }

    private static void verifyPageTitle(DocumentUploadAndStorage documentUploadAndStorage)
    {
        Assert.assertTrue(StringUtils.containsIgnoreCase(documentUploadAndStorage.pageTitle().getText(), PAGE_TITLE));
    }

    private void deleteDocStoreItem(String filename) throws XifinDataAccessException
    {
        docStoreDao.deleteDocStoreAudit(filename);
        docStoreDao.deleteDocStore(filename);
    }
}
