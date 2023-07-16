package com.EndToEndTests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.qFrPending.QFrPending;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.util.StringUtils;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.TestDataSetup;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;


public class EndToEndBaseTest extends SeleniumBaseTest {
    XifinAdminUtils xifinAdminUtils;
    FileManipulation fileManipulation;
    public int maxWaitTimeinSec = 600;

    public void fileContainsString(String filePath, Hashtable<String, String> lookupDetails) throws FileNotFoundException {
        List<String> assertList = new ArrayList<>();
        try {
            File file = new File(filePath);
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            lookupDetails.forEach((lookupString, validationString) -> {
                logger.info("looking for \"" + validationString + ":" + lookupString + "\" in " + file.getName());
                if (!StringUtils.containsIgnoreCase(fileContent,lookupString)) {
                    assertList.add(lookupString);
                }
            });
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (Exception e) {
            Assert.fail();
        }
        if (!assertList.isEmpty()) {
            logger.info(assertList.toString() + " not found in " + filePath);
            Assert.fail();
        }
    }

    public String createAccn(String email, String password, String project, String testSuite, String testCase) throws Exception {
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);

        logger.info("*** Step 1 Action - Create Accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("accnId" + accnId);
        String clnAbbrev = testProperties.getProperty("ClnAbbrev");
        logger.debug(clnAbbrev);

        logger.info("Step 1: Look up for client id. If fk_prim_cln_id and fk_cln_id are 0 in accn table, update it to correct client id by cln_bbrev being used");
        Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
        Accn accn = accessionDao.getAccn(accnId);
        if((accn.getClnId()==0) || (accn.getPrimClnId()==0))
        {
            accessionDao.updateAccnClnIdByClnAbbrev(cln.clnAbbrev, accnId);
        }

        logger.info("*** Step 1 Expected Results - Verify Accession is created");
        Assert.assertNotNull(accnId, " Accession is not generated");
        logger.info(" accession Id = " + accnId);

        logger.info("*** Step 1 Expected Result - Verify Accession is in QFRPending");
        Assert.assertTrue(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_FR_PENDING), " Accession is not in Q_FR_PENDING");

        logger.info("*** Step 2 Action - Set B_OE_PERFORMED to true for given accnId");
        QFrPending qFrPending = accessionDao.getQFrPendingByAccnId(accnId);
        if (!qFrPending.isOePerformed) {
            qFrPending.setIsOePerformed(true);
            accessionDao.setQFrPending(qFrPending);
        }

        logger.info("*** Step 3 Action - Run OE Posting Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "OePostingEngine", "SSO_APP_STAGING", false);

        logger.info("*** Step 3 Expected Result - Ensure AccnId is processed by OePosting Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_FR_PENDING), " Accession is still in Q_FR_PENDING");
        return (accnId);
    }
}