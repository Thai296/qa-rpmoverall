package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;



public class OePostingADTMatchEngineTest extends SeleniumBaseTest {
    XifinAdminUtils xifinAdminUtils;

    @Test(priority = 1, description = "Process from Q_OE by matching the XREF_ID")
    @Parameters({"accnId", "qOeSeqId", "xRefId", "ptSSN","email", "password"})
    public void testPFER_17(String accnId, String qOeSeqId, String xRefId, int ptSSN, String email, String password) throws Exception {
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        logger.info("***** Testing - testPFER_17 *****");

        logger.info("*** Step 1 Actions: - Clear the value in SSN field in ACCN table in DB");

        daoManagerXifinRpm.setValuesFromACCNByAccnId(accnId, "PT_SSN = '0'", testDb);

        logger.info("*** Step 2 Actions: - Delete the record in Q_OE_ARCH table in DB");

        daoManagerPlatform.deleteFromQOEARCHByseqId(qOeSeqId, testDb);

        logger.info("*** Step 3 Actions: - Insert a record in Q_OE table in DB");
        daoManagerPlatform.deleteFromQOEByseqId(qOeSeqId, testDb);
        daoManagerPlatform.setFromQOEBySeqIdXrefId(qOeSeqId, "10/09/2014", xRefId, testDb);

        logger.info("*** Step 3 Actions: - wait for record to be processed by OePosting ADT Match Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "OePostingAdtMatchEngine", "SSO_APP_STAGING", false);
        Assert.assertTrue(accessionDao.waitForRecordToBeProcessedByADTMatchEngine(xRefId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 3 Expected Results: - The Patient SSN should be updated");
        Accn accnInfoList = accessionDao.getAccn(accnId);
        int ssn = accnInfoList.getPtSsn();
        Assert.assertEquals(ptSSN, ssn, "        The Patient SSN in ACCN table should be updated to " + ptSSN);

    }
}

