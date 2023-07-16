package com.overall.utils;

import com.mbasys.mars.ejb.entity.docStore.DocStore;
import com.mbasys.mars.ejb.entity.docStoreAudit.DocStoreAudit;
import com.mbasys.mars.ejb.entity.docStoreIndex.DocStoreIndex;
import com.mbasys.mars.ejb.entity.docStoreIndexTyp.DocStoreIndexTyp;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.DocStoreDao;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class DocStoreUtils
{
	public static final String DOC_CATEGORY_ACCN = "Accession";
	public static final String DOC_CATEGORY_CLN = "Client";

	private static final Logger LOG = Logger.getLogger(DocStoreUtils.class);

	private static final Date TODAYS_DT = new Date (DateUtils.truncate(new java.util.Date(), Calendar.DATE).getTime());

	private final DocStoreDao docStoreDao;

	public DocStoreUtils(DocStoreDao docStoreDao)
	{
		this.docStoreDao = docStoreDao;
	}

	public String generateMetadataFile(String docFileName, String docType, String category, String id, String comment)
	{
		String content ="documentfilename,documenttype,category,categoryid,comment\r\n" 
				+ docFileName+","+docType+","+getMetadataDocCategory(category)+","+id+","+comment;
		LOG.info("Generated Metadata file, docFileName="+docFileName+", content="+content);
		return content;
	}

	public void verifyDocStore(DocStore docStore, String docCategory, String docCategoryId, String docStoreTyp, String contentType, String comment, String uploadBy, String audUser, boolean isInProcess, boolean isDeleted, boolean isUserLoaded) throws XifinDataNotFoundException, XifinDataAccessException {
		List<DocStoreIndex> docStoreIndexInfoList = docStoreDao.getDocStoreIndexRecordsByDocStoreSeqId(docStore.getSeqId());
		Map<String, DocStoreIndexTyp> docStoreIndexTypMap = docStoreDao.getDocStoreIndexTypMap();
		verifyDocStore(docStore, docStoreIndexInfoList, docStoreIndexTypMap, docCategory, docCategoryId, docStoreTyp, contentType, comment, uploadBy, audUser, isInProcess, isDeleted, isUserLoaded);
	}

	private void verifyDocStore(DocStore docStore, List<DocStoreIndex> docStoreIndexList, Map<String, DocStoreIndexTyp> docStoreIndexTypMap, String docCategory, String docCategoryId, String docStoreTyp, String contentType, String comment, String uploadBy, String audUser, boolean isInProcess, boolean isDeleted, boolean isUserLoaded) throws XifinDataNotFoundException, XifinDataAccessException {
		LOG.info("Verifying DocStore fields, seqId="+docStore.getSeqId());
		Assert.assertFalse(docStore.getPreEncryptedFileHashValue().isEmpty(), "DocStore Pre-encrypted file hash is empty");
		Assert.assertEquals(new Date(DateUtils.truncate(docStore.getLocationStorageDate(), Calendar.DATE).getTime()), TODAYS_DT, "DocStore Location Storage Date is incorrect");
		Assert.assertEquals(new Date (DateUtils.truncate(docStore.getLastAccessDate(), Calendar.DATE).getTime()), TODAYS_DT, "DocStore Last Access Date is incorrect");
		Assert.assertEquals(new Date (DateUtils.truncate(docStore.getUploadDate(), Calendar.DATE).getTime()), TODAYS_DT, "DocStore Upload Date is incorrect");
		Assert.assertNull(docStore.getDocCategory(), "DocStore Doc Category should be null");
		Assert.assertNull(docStore.getDocCategoryId(), "DocStore Doc Category ID should be null");
		if (StringUtils.isBlank(docCategory) && StringUtils.isBlank(docCategoryId))
		{
			Assert.assertEquals(docStoreIndexList.size(), 0, "Doc Store Index List should be empty");
		}
		else
		{
			Assert.assertEquals(docStoreIndexList.size(), 1, "Doc Store Index List size is not correct");
			Assert.assertEquals(docStoreIndexList.get(0).getIndexTypId(), docStoreIndexTypMap.get(docCategory).getIndexTypId(), "Doc Store Index Typ is incorrect");
			Assert.assertEquals(docStoreIndexList.get(0).getDataValue(), docCategoryId, "Doc Store Index Value is incorrect");
		}
		Assert.assertEquals(getDocStoreTypAbbrev(docStore.getDocStoreTyp()), docStoreTyp, "DocStore Type is not correct");
		Assert.assertEquals(docStore.getDocComment(), comment, "DocStore Doc Comment is not correct");
		Assert.assertEquals(docStore.getContentType(), contentType, "DocStore Content Type is not correct");
		Assert.assertEquals(docStore.getUploadBy(), uploadBy, "DocStore UploadBy user not correct");
		Assert.assertEquals(docStore.getAudUser(), audUser, "DocStore AudUser not correct");
		Assert.assertEquals(docStore.getIsProcess(), isInProcess, "DocStore In Process flag is incorrect, isInProcess="+isInProcess);
		Assert.assertEquals(docStore.getIsDeleted(), isDeleted, "DocStore Deleted flag is incorrect, isDeleted="+isDeleted);
		Assert.assertEquals(docStore.getIsUserLoaded(), isUserLoaded, "DocStore User Loaded flag is not True");
		Assert.assertTrue(docStore.getDocSize() > 0, "DocStore Size is 0");
	}

	public void verifyDocStoreAudit(DocStoreAudit docStoreAudit, int docStoreSeqId, int docStoreLocationId, String audUser)
	{
		LOG.info("Verifying DocStoreAudit fields, docStoreAuditSeqId="+ docStoreAudit.getSeqId()+", docStoreSeqId="+docStoreSeqId);
		Assert.assertEquals(docStoreAudit.getDocStoreId(), docStoreSeqId, "DocStoreAudit Doc Store Id is not correct");
		Assert.assertEquals(docStoreAudit.getDocStoreLocationId(), docStoreLocationId, "DocStoreAudit Doc Store Location Id is not correct");
		Assert.assertEquals(docStoreAudit.getAudUser(), audUser, "DocStoreAudit Aud User is not correct");
		Assert.assertTrue(docStoreAudit.getDownloadTime() > 0, "DocStoreAudit Download Time is 0");
	}

	private String getDocStoreTypAbbrev(int docStoreTypId)
	{
		String abbrev = null;

		try
		{
			abbrev = docStoreDao.getDocStoreTyp(docStoreTypId).getAbbrev();
		}
		catch (Exception e)
		{
			LOG.info("Unable to find DocStoreTyp, docStoreTypId="+docStoreTypId, e);
		}
		return abbrev;
	}

	private String getMetadataDocCategory(String docCategory)
	{
		String metadataDocCategory = null;

		switch (docCategory)
		{
			case DOC_CATEGORY_ACCN:
				metadataDocCategory = "Accn";
				break;
			case DOC_CATEGORY_CLN:
				metadataDocCategory = "Cln";
				break;
		}
		return metadataDocCategory;
	}
}
