package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.elcPmtDetail.ElcPmtDetail;
import com.mbasys.mars.ejb.entity.pmtSusp.PmtSusp;
import com.overall.accession.PatientServiceCenter.PSCPrepayConfig;
import com.overall.menu.MenuNavigation;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ElcPmtProcessingEngineTest extends SeleniumBaseTest {
    @Test(priority = 1, description = "Create Pre-Payment")
    @Parameters({"ssoUsername", "ssoPassword","orderId","pmtAmnt","cardNumber","expMonth","expYr"})
    public void PrePayment(String ssoUsername, String ssoPassword, String orderId, String pmtAmnt, String cardNumber, String expMonth,String expYr) throws Exception {
        MenuNavigation menuNavigation = new MenuNavigation(driver, config);
        PSCPrepayConfig pscPrepayConfig = new PSCPrepayConfig(driver, wait);
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 1 Action - Create Pre-Payment using Patient Service Center and get Transaction Id");
        logIntoSso(ssoUsername, ssoPassword);
        menuNavigation.navigateToPSCPrepayPage();
        String transId = pscPrepayConfig.getTransId(this,orderId,pmtAmnt,cardNumber,expMonth,expYr);
        logger.info("Transaction Id is = "+transId);

        logger.info("*** Step 1 Expected Results - Verify Pre-Payment is created in DB");
        ElcPmtDetail elcPmtDetail = paymentDao.getElcPmtDetailByTransId(transId);
        Assert.assertNotNull(elcPmtDetail, "Pre-payment is not created");

        logger.info("*** Step 2 Action - Run Electronic Payment Processing Engine");
        //wait at least 2 mins, otherwise engine is not going to process Pre-Payment
        Thread.sleep(130000);
        xifinAdminUtils.runPFEngine(this,ssoUsername,ssoPassword,null,"ElcPmtProcessingEngine", "SSO_APP_STAGING",true);
        Thread.sleep(5000);

        logger.info("*** Step 2 Expected Results - Verify record in Pmt_susp is created");
        PmtSusp pmtSusp = paymentDao.getPmtSuspByElcPmtDetailTransId(transId);
        Assert.assertNotNull(pmtSusp, "Pmt_susp record is not created");
    }
}




