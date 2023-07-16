package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accnAdj.AccnAdj;
import com.mbasys.mars.ejb.entity.accnPmt.AccnPmt;
import com.mbasys.mars.ejb.entity.dep.Dep;
import com.mbasys.mars.ejb.entity.depBatch.DepBatch;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.sql.Date;
import java.util.List;

import static com.mbasys.mars.persistance.PmtMap.DEP_STA_TYP_COMPLETE;
import static com.mbasys.mars.persistance.PmtMap.DEP_STA_TYP_POSTING;
import static com.xifin.util.DateConversion.stringToSqlDate;

public class AccnPaymentPostingEngineTest extends SeleniumBaseTest {
    @Test(priority = 1, description = "Post Deposit")
    @Parameters({"ssoUsername", "ssoPassword", "depId"})
    public void postDeposit(String ssoUsername, String ssoPassword, int depId) throws Exception {
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);

        DepBatch depBatch = paymentDao.getDepBatchByDepIdAndBatchId(depId,1);
        Dep dep = paymentDao.getDepByDepId(depId);
        double depAmt = Double.parseDouble(String.valueOf(dep.depAmt));
        String dB = config.getProperty(PropertyMap.RPM_DB_USER);

        logger.info("*** Reopen deposit if it was posted");
        if (depBatch.isPosted) {
            reopenDepBatch(dB, depId,"test", depAmt);
            depBatch = paymentDao.getDepBatchByDepIdAndBatchId(depId,1);
        }

        logger.info("*** Step 1 Action - Release Discrepancies");
        depBatch.setIsDiscrpRelease(true);
        paymentDao.setDepBatch(depBatch);

        logger.info("*** Step 2 Action - Run Accn Payment Posting Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "AccnPaymentPostingEngine", "SSO_APP_STAGING", false);
        Thread.sleep(5000);

        logger.info("*** If Deposit status is posting, rerun the engine");
        depBatch = paymentDao.getDepBatchByDepIdAndBatchId(depId,1);
        if (depBatch.staId == DEP_STA_TYP_POSTING) {
            xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "AccnPaymentPostingEngine", "SSO_APP_STAGING", true);
            Thread.sleep(5000);
            depBatch = paymentDao.getDepBatchByDepIdAndBatchId(depId,1);
        }

        logger.info("*** Step 2 Expected Results - Deposit is Posted");
        Assert.assertTrue(depBatch.isPosted);
        Assert.assertEquals(DEP_STA_TYP_COMPLETE, depBatch.staId,"Deposit is not Completed");
        Assert.assertEquals(depBatch.applAmt, depBatch.postAmt,"Applied amount and Posted amount should be same");

        logger.info("*** Step 3 Action - Reopen the Deposit");
        reopenDepBatch(dB, depId,"test", depAmt);

    }

    private void reopenDepBatch(String dB, int depId, String caseNum, double depAmt) throws Exception {
        try {
            paymentDao.reopenDepBatch(dB, depId,"test", depAmt);
            List<DepBatch> depBatches = paymentDao.getDepBatchByDepId(depId);
            for(DepBatch depBatch : depBatches){
                if (depBatch.batchAmt.isZero()){
                    depBatch.setResultCode(ErrorCodeMap.DELETED_RECORD);
                    paymentDao.setDepBatch(depBatch);
                }
            }

        }
        catch (XifinDataAccessException e) {
            if (e.getCause().toString().contains("payment(s) must be in current period.")) {
                String ssDate = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(71, testDb);
                Date newDate = stringToSqlDate(ssDate);

                List <AccnAdj> accnAdjList = rpmDao.getAccnAdjByDepId(testDb,depId);
                List <AccnPmt> accnPmtList = rpmDao.getAccnPmtByDepId(testDb,depId);

                for (AccnAdj accnAdj : accnAdjList) {
                    accnAdj.setAdjDt(newDate);
                    accessionDao.setAccnAdj(accnAdj);
                }

                for (AccnPmt accnPmt : accnPmtList) {
                    accnPmt.setPmtDt(newDate);
                    accessionDao.setAccnPmt(accnPmt);
                }
                logger.info("*** AdjDt and PmtDt is set to " + ssDate);

                paymentDao.reopenDepBatch(dB, depId,"test", depAmt);
                List<DepBatch> depBatches = paymentDao.getDepBatchByDepId(depId);
                for(DepBatch depBatch : depBatches){
                    if (depBatch.batchAmt.isZero()){
                        depBatch.setResultCode(ErrorCodeMap.DELETED_RECORD);
                        paymentDao.setDepBatch(depBatch);
                    }
                }
                logger.info("*** Deposit reopened");
            }
            else {
                throw new Exception();
            }
        }
    }

}