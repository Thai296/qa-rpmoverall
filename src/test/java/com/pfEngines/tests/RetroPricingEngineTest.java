package com.pfEngines.tests;


import com.mbasys.mars.ejb.entity.prcTest.PrcTest;
import com.mbasys.mars.ejb.entity.qRetro.QRetro;
import com.mbasys.mars.ejb.entity.qRetroAccn.QRetroAccn;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ConvertUtil;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertTrue;


public class RetroPricingEngineTest extends SeleniumBaseTest {
	@Test(priority = 1, description = "Process the record(s) in Q_RETRO and Q_RETRO_ACCN")
	@Parameters({"date", "testId", "retroBatchId", "prcId", "ssoUsername", "ssoPassword", "eType"})
	public void processRecordsInQRetroAndQRetroAccn(String date, int testId, int retroBatchId, int prcId, String ssoUsername, String ssoPassword, String eType) throws Exception {
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		ConvertUtil convertUtil = new ConvertUtil();

		logger.info("*** Actions: - Setup test data in Q_RETRO table and delete record in SUBM_FILE in DB");
		Date effDt = convertUtil.convertStringToUtilDate(date, "MM/dd/yyyy");
		PrcTest prcTest = rpmDao.getPrcTestByTestIdEffDtPrcId(testDb, testId, new java.sql.Date(effDt.getTime()), prcId);
		prcTest.setPrice((float) (prcTest.getPrice() + 0.5));
		rpmDao.setPrcTest(testDb, prcTest);
		QRetro qRetro = rpmDao.getQRetroByRetroBatchId(testDb, retroBatchId);

		qRetro.setIsProcessed(false);
		qRetro.setSubmFileSeqId(0);
		rpmDao.setQRetro(testDb, qRetro);		

		logger.info("*** Actions: - Setup test data in Q_RETRO_ACCN table in DB");
		List<QRetroAccn> qRetroAccnList = rpmDao.getQRetroAccnByRetroBatchId(testDb, retroBatchId);
		for (QRetroAccn qRetroAccn : qRetroAccnList) {
			qRetroAccn.setIsRetroProcessed(false);
			rpmDao.setQRetroAccn(testDb, qRetroAccn);
		}

		logger.info("*** Actions: - Run Retro Pricing Engine");
		xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, eType, "SSO_APP_STAGING", false);
		Thread.sleep(5000);
		
    	logger.info("*** Expected Results: - Verify that the batch in Q_RETRO is processed properly in DB");
    	qRetro = rpmDao.getQRetroByRetroBatchId(testDb, retroBatchId);
		assertTrue(qRetro.getIsProcessed(), "        q_retro.b_processed = 1 for PK_RETRO_BATCH_ID = " + retroBatchId + ".");
    	assertTrue(qRetro.getSubmFileSeqId() > 0, "       q_retro.FK_SUBM_FILE_SEQ_ID > 0 for PK_RETRO_BATCH_ID = " + retroBatchId + ".");
		
    	logger.info("*** Expected Results: - Verify that the Accessions in the batch in Q_RETRO_ACCN are processed properly in DB");
    	qRetroAccnList = rpmDao.getQRetroAccnByRetroBatchId(testDb, retroBatchId);
		for (QRetroAccn qRetroAccn : qRetroAccnList) {
			assertTrue(qRetroAccn.getIsRetroProcessed(), "        q_retroaccn.b_retro_processed = 1 for fK_RETRO_BATCH_ID = " + retroBatchId + ".");
		}
	}

}
