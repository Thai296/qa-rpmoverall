package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.qValidateAccn.QValidateAccn;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.validation.ValidateQueueMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

public class AccnValidationEngineTest extends SeleniumBaseTest {

    private TimeStamp timeStamp;
    XifinAdminUtils xifinAdminUtils;
    private File upOne = new File(System.getProperty("user.dir")).getParentFile();
    public static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(10);
    private static final String OS_NAME = System.getProperty("os.name");
    private static final String WINDOWS = "windows";

    @Test(priority = 1, description = "Re-Validate Type")
    @Parameters({"project", "testSuite", "testCase", "email", "password"})
    public void testPFER_1(String project, String testSuite, String testCase, String email, String password) throws Exception {
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        logger.info("***** Testing - testPFER_1 for Validation Type Re-validate *****");

        logger.info("*** Actions: - Send WS request to create a Final Reported 3rd Party Accession with SUBID error");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Expected Results: - Verify that a new accession was generated");
        Assert.assertNotNull(accnId, "        A new Accession was generated.");

        ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        if (!clnBillingAssignInfoList.isEmpty()) {
            daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
        }
        logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Actions: - Wait for PF EP Workflow Engine");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession has SUBID accn_pyr_err");
        timeStamp = new TimeStamp(driver);
        String currDtStr = timeStamp.getCurrentDate();
        List<String> accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDtStr, "2136", testDb); //2136: SUBID ErrCd
        Assert.assertTrue(accnPyrErrInfoList.size() > 0, "        Accession ID " + accnId + " should have SUBID accn_pyr_err.");

        logger.info("*** Actions: - Add Subscriber id manually in DB");
        List<AccnPyr> accnPyrList = accessionDao.getAccnPyrByAccnId(accnId);
        for (AccnPyr accnPyr : accnPyrList) {
            if (StringUtils.isEmpty(accnPyr.getSubsId())) {
                accnPyr.setSubsId("A914022193");
                accessionDao.setAccnPyr(accnPyr);
            }
        }

        logger.info("*** Actions: - Manually add a new record in Q_VALIDATE_ACCN table");
        try {
            accessionDao.getQValidateAccn(accnId);
            int validateTypId = ValidateQueueMap.RE_VALIDATE;
            int priority = 1;
            Boolean isErr = false;
            int pyrId = 0;
            accessionDao.updateQValidateAccnByAccnId(accnId, pyrId, validateTypId, priority, isErr);
        } catch (Exception e) {
            logger.info("in catch");
            QValidateAccn qValidateAccn = new QValidateAccn();
            qValidateAccn.setAccnId(accnId);
            qValidateAccn.setValidateTypId(ValidateQueueMap.RE_VALIDATE);
            qValidateAccn.setPriority(1);
            qValidateAccn.setIsErr(false);
            qValidateAccn.setNewPyrId(0);
            accessionDao.setQValidateAccn(qValidateAccn);
        }
        logger.info("Waiting for accession to exit validation queue, accnId=" + accnId);
        xifinAdminUtils.runPFEngine(this, email, password, null, "AccnValidationEngine", "SSO_APP_STAGING", false);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfValidationQueue(accnId, ValidateQueueMap.RE_VALIDATE, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Expected Results: - Verify that the accn payor error is fixed");
        List<AccnPyrErr> accnPyrErrList = accessionDao.getAccnPyrErrsByAccnId(accnId);

        for (AccnPyrErr accnPyrErr : accnPyrErrList) {
            if (accnPyrErr.getErrCd() == 2136) {
                assertNotNull(accnPyrErr.getFixDt());
                assertNotNull(accnPyrErr.getFixUserId());
            }
        }

        logger.info("*** Expected Results: - Verify that the accn moved out of EP queue and is in submissions");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        assertEquals(AccnStatusMap.Q_ACCN_SUBM, accnQue.getQTyp());
    }

    @Test(priority = 1, description = "RePrice Accn")
    @Parameters({"email", "password"})
    public void testPFER_2(String email, String password) throws Exception {
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        logger.info("***** Testing - testPFER_2 for Validation Type RePrice Accn *****");

        logger.info("*** Actions: - Find a priced accn");
        String accnId = getPricedAccessionInSubmQueue();

        logger.info("*** Actions: - Manually add a new record in Q_VALIDATE_ACCN table");
        QValidateAccn qValidateAccn = new QValidateAccn();
        qValidateAccn.setAccnId(accnId);
        qValidateAccn.setValidateTypId(ValidateQueueMap.RE_VALIDATE_RE_PRICE);
        qValidateAccn.setPriority(1);
        qValidateAccn.setIsErr(false);
        qValidateAccn.setNewPyrId(0);
        accessionDao.setQValidateAccn(qValidateAccn);

        logger.info("Waiting for accession to exit validation queue, accnId=" + accnId);
        xifinAdminUtils.runPFEngine(this, email, password, null, "AccnValidationEngine", "SSO_APP_STAGING", false);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfValidationQueue(accnId, ValidateQueueMap.RE_VALIDATE_RE_PRICE, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Expected Results: - Verify that the accn is in Pricing queue");
        AccnQue accnQue2 = accessionDao.getAccnQueByAccnId(accnId);
        assertEquals(AccnStatusMap.Q_PRICE, accnQue2.getQTyp());

    }

    @Test(priority = 1, description = "Add primary Payor")
    @Parameters({"email", "password"})
    public void testPFER_3(String email, String password) throws Exception {
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        logger.info("***** Testing - testPFER_3 for Validation Type Add primary Payor *****");

        logger.info("*** Actions: - Find a priced accn");
        Accn accn = accessionDao.getPricedAccession();
        String accnId = accn.getAccnId();
        logger.info("        AccnID: " + accnId);

        logger.info("*** Expected: - Verify that the accn is in submissions queue");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        while (AccnStatusMap.Q_ACCN_SUBM != accnQue.getQTyp()) {
            accn = accessionDao.getPricedAccession();
            accnId = accn.getAccnId();
            accnQue = accessionDao.getAccnQueByAccnId(accnId);
        }
        assertEquals(AccnStatusMap.Q_ACCN_SUBM, accnQue.getQTyp());

        logger.info("*** Actions: - Manually add a new record in Q_VALIDATE_ACCN table");
        QValidateAccn qValidateAccn = new QValidateAccn();
        qValidateAccn.setAccnId(accnId);
        qValidateAccn.setValidateTypId(ValidateQueueMap.ADD_PRIMARY_PAYOR);
        qValidateAccn.setNewPyrId(payorDao.getPyrByPyrAbbrv("MCCA").getPyrId());
        qValidateAccn.setPriority(1);
        qValidateAccn.setIsErr(false);
        accessionDao.setQValidateAccn(qValidateAccn);

        logger.info("Waiting for accession to exit validation queue, accnId=" + accnId);
        xifinAdminUtils.runPFEngine(this, email, password, null, "AccnValidationEngine", "SSO_APP_STAGING", false);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfValidationQueue(accnId, ValidateQueueMap.ADD_PRIMARY_PAYOR, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Expected Results: - Verify that the accn has new primary Payor");
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        for (AccnPyr accnPyr : accnPyrs) {
            if (accnPyr.getPyrPrio() == 1) {
                assertEquals(accnPyr.getPyrId(), 1);
            }
        }

        logger.info("*** Expected Results: - Verify that the accn is in Pricing queue");
        AccnQue accnQue2 = accessionDao.getAccnQueByAccnId(accnId);
        assertEquals(AccnStatusMap.Q_PRICE, accnQue2.getQTyp());

    }

    private String getPricedAccessionInSubmQueue() throws XifinDataAccessException, XifinDataNotFoundException {
        Accn accn = accessionDao.getPricedAccession();
        String accnId = accn.getAccnId();
        logger.info("        AccnID: " + accnId);
        while (accessionDao.getAccnQueByAccnId(accnId).getQTyp() != AccnStatusMap.Q_ACCN_SUBM) {
            accn = accessionDao.getPricedAccession();
            accnId = accn.getAccnId();
            logger.info("        AccnID: " + accnId);

        }
        return accnId;

    }
}

