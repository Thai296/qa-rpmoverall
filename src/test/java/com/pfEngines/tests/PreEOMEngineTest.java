package com.pfEngines.tests;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.xifin.utils.SeleniumBaseTest;
import com.mbasys.mars.ejb.entity.clnAdj.ClnAdj;
import java.sql.Date;
import java.text.SimpleDateFormat;


public class PreEOMEngineTest extends SeleniumBaseTest {

    XifinAdminUtils xifinAdminUtils;

    @Test(priority = 1, description = " PreEOMEngine - Post Retro Client adjustments")
    @Parameters({"email", "password"})
    public void postRetroClnAdj(String email, String password) throws Exception {
        logger.info("===== Testing - postRetroClnAdj =====");

        xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 1 Actions: - Add a new record in Cln_Adj table with  b_posted = 0 ");
        ClnAdj clnAdj = new ClnAdj();
        clnAdj.setClnId(34343);
        clnAdj.setAdjSeq(2128);
        clnAdj.setSubmDt(new Date((new SimpleDateFormat("MM/dd/yyyy")).parse("02/28/2023").getTime()));
        clnAdj.setDepId(0);
        clnAdj.setDepBatchId(0);
        clnAdj.setAdjCdId(4);
        clnAdj.setUserId("autoqatester");
        clnAdj.setPmtSeq(0);
        clnAdj.setAdjDt(new Date((System.currentTimeMillis())));
        clnAdj.setAdjAmt(-100);
        clnAdj.setIsPrintable(false);
        clnAdj.setIsPosted(false);
        clnAdj.setRevAccnId("A2011110102030218");
        clientDao.setClnAdj(clnAdj);


        logger.info("*** Step 1 Expected Results: - Verify that a new record is added into Cln_Adj table");
        Assert.assertEquals(clnAdj.getUserId(), "autoqatester");

        logger.info("*** Step 2 Actions: - wait for a record to be processed by Pre-EOM  Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "PreEOMEngine", "SSO_APP_STAGING", false);

        Thread.sleep(5000);

        logger.info("*** Step 2 Expected Results: - Verify that the data in Cln_Adjtable are updated properly");

        clnAdj = clientDao.getClnAdjByUserId("autoqatester");
        Assert.assertTrue(clnAdj.getIsPosted());

        logger.info("*** Step 3 Actions: - Delete the cln-adj record entered in Step 1");
        clnAdj.setResultCode(ErrorCodeMap.DELETED_RECORD);
        clientDao.setClnAdj(clnAdj);


        logger.info("*** Step 3 Expected results : - Verify that the recprd is deleted ");
        clnAdj = clientDao.getClnAdjByUserId("autoqatester");
         Assert.assertEquals(clnAdj,null);

    }


}

