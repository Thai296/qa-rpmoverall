package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.claimStatus.ClaimStatus;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrClaimStatusCheckConfig.PyrClaimStatusCheckConfig;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.qClaimStatus.QClaimStatus;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.util.DateConversion;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClaimStatusCheckEngineTest extends SeleniumBaseTest {
    protected static long QUEUE_POLL_TIME = TimeUnit.SECONDS.toMillis(4);
    protected static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(4);

    @Test(alwaysRun = true, description = "First Check Claim created outside of wait time is queued")
    @Parameters({"accnId", "pyrAbbrv", "docSeqId"})
    public void testPFER_770(String accnId, String pyrAbbrv, int docSeqId) throws Exception {
        logger.info("Starting Test Case: PFER-770, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv + ", docSeqId=" + docSeqId);
        int pyrId = payorDao.getPyrByPyrAbbrv(pyrAbbrv).getPyrId();

        logger.info("Clearing all QClaimStatus records for Accn");
        clearQClaimStatusByAccnId(accnId);

        logger.info("Clearing all QAccnSubm records except the one that matches the docSeqId for Accn");
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, pyrId, Collections.singletonList(String.valueOf(docSeqId)));

        int submFileSeqId = rpmDao.getQAccnSubm(null, docSeqId).getSubmFileSeqId();
        logger.info("Updating the fileCreatDt of the SubmFile record");
        rpmDao.updateSubmFileSetFileCreatDt(submFileSeqId, DateConversion.getNowAsSqlDate(-4));

        logger.info("Wait for new QAccnSubm record to be added");
        boolean IsAccnInQClaimStatus = waitForNewQASRecordToBeAdded(accnId, QUEUE_WAIT_TIME*2);
        Assert.assertTrue(IsAccnInQClaimStatus, "Claim Status Check Engine failed to add Accession to q_claim_status table:");
    }

    @Test(alwaysRun = true, description = "User ID Is Correct in Real Time Claim Status Request")
    @Parameters({"accnId", "pyrAbbrv", "docSeqId"})
    public void testRealTimeClaimStatusCheck(String accnId, String pyrAbbrv, int docSeqId) throws Exception {
        logger.info("Starting Test Case: testUserIdIsCorrectInRealTimeClaimStatusCheck, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv + ", docSeqId=" + docSeqId);
        int pyrId = payorDao.getPyrByPyrAbbrv(pyrAbbrv).getPyrId();

        logger.info("Clearing all QClaimStatus records for Accn");
        clearQClaimStatusByAccnId(accnId);

        logger.info("Clearing all QAccnSubm records except the one that matches the docSeqId for Accn");
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, pyrId, Collections.singletonList(String.valueOf(docSeqId)));

        logger.info("Clearing all ClaimStatus records, accnId="+accnId);
        cleanClaimStatusTablesAndClaimStatHistoryByAccnId(accnId);

        List<ClaimStatus> origClaimStatusList = rpmDao.getClaimStatusList(accnId, 0);

        int submFileSeqId = rpmDao.getQAccnSubm(null, docSeqId).getSubmFileSeqId();
        logger.info("Updating the fileCreatDt of the SubmFile record");
        rpmDao.updateSubmFileSetFileCreatDt(submFileSeqId, DateConversion.getNowAsSqlDate(-4));

        logger.info("Wait for new claim status record to be added, accnId="+accnId);
        List<ClaimStatus> newClaimStatusList = waitForNewClaimStatus(accnId,Arrays.asList("01"), origClaimStatusList, QUEUE_WAIT_TIME*2);
        Assert.assertEquals(newClaimStatusList.size(), 1,"Failed to find new ClaimStatus record, accnId="+accnId);
        Assert.assertEquals(newClaimStatusList.get(0).getAck277Typ(), "Real-Time");
        Assert.assertNull(newClaimStatusList.get(0).getUserId());
    }

    @Test(alwaysRun = true, description = "Test Claim Status Check Engine Configuration Options")
    @Parameters({"accnId", "pyrAbbrv", "claimSuffixIds"})
    public void testConfigOptions(String accnId, String pyrAbbrv, String claimSuffixIds) throws Exception
    {
        logger.info("Starting Test Case: testConfigOptions, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv + ", claimSuffixIds=" + claimSuffixIds);

        Pyr pyr = payorDao.getPyrByPyrAbbrv(pyrAbbrv);
        logger.info("Verifying payor's claim status check configuration, accnId="+accnId+", pyrId="+pyr.getPyrId());
        List<PyrClaimStatusCheckConfig> pyrClaimStatusCheckConfigs = payorDao.getPyrClaimStatusCheckConfigsByPyrId(pyr.getPyrId());
        Assert.assertEquals(pyrClaimStatusCheckConfigs.size(), 1, "Expected only 1 PyrClaimStatusCheckConfig record");
        Assert.assertTrue(pyrClaimStatusCheckConfigs.get(0).getFirstRequestWait() > 0, "Expected First Request Wait Period > 0, firstRequestWait="+pyrClaimStatusCheckConfigs.get(0).getFirstRequestWait());
        Assert.assertTrue(pyrClaimStatusCheckConfigs.get(0).getRequestInterval() > 0, "Expected Request Interval > 0, requestInterval="+pyrClaimStatusCheckConfigs.get(0).getRequestInterval());
        Assert.assertTrue(pyrClaimStatusCheckConfigs.get(0).getMaxRequest() > 0, "Expected Max Request = 1, maxRequest="+pyrClaimStatusCheckConfigs.get(0).getMaxRequest());

        List<String> claimSuffixIdsToCheck = Arrays.asList(StringUtils.split(claimSuffixIds, ","));
        Assert.assertTrue(claimSuffixIdsToCheck.size() > 1, "Expected more than 1 claim ID suffix, size="+claimSuffixIdsToCheck.size());

        List<SubmFile> submFilesToCheck = new ArrayList<>();
        for (QAccnSubm qas : rpmDao.getQAccnSubm(null, accnId))
        {
            if (claimSuffixIdsToCheck.contains(qas.getClaimIdSuffix()) && qas.getSubmFileSeqId() > 0)
            {
                submFilesToCheck.add(submissionDao.getSubmFileBySubmFileSeqId(qas.getSubmFileSeqId()));
            }
        }

        Date todayDt = new Date(DateUtils.truncate(new java.util.Date(), Calendar.DATE).getTime());
        for (SubmFile sf : submFilesToCheck)
        {
            logger.info("Updating file create date to today, submFileSeqId="+sf.getSubmFileSeqId()+", fileCreatDt="+todayDt);
            sf.setFileCreatDt(todayDt);
            rpmDao.setSubmFile(null, sf);
        }

        logger.info("Clearing all QClaimStatus records for Accn");
        cleanClaimStatusTablesAndClaimStatHistoryByAccnId(accnId);

        List<QAccnSubm> submissionsToDelete = new ArrayList<>();
        for (QAccnSubm qas : rpmDao.getQAccnSubm(null, accnId))
        {
            if (!claimSuffixIdsToCheck.contains(qas.getClaimIdSuffix()))
            {
                submissionsToDelete.add(qas);
            }
        }
        logger.info("Deleting submissions, accnId="+accnId+", submissionsToDelete="+submissionsToDelete);
        submissionDao.deleteSubmissions(submissionsToDelete);

        logger.info("Verifying claim status is not triggered when File Create Date is within First Request Wait, accnId="+accnId);
        List<ClaimStatus> claimStatusList = waitForNewClaimStatus(accnId, claimSuffixIdsToCheck, rpmDao.getClaimStatusList(accnId, 0), QUEUE_WAIT_TIME*2);
        Assert.assertTrue(claimStatusList.isEmpty(), "Expected no new Claim Status for accession");

        Date newFileCreatDt = Date.valueOf(LocalDate.now().minusDays(pyrClaimStatusCheckConfigs.get(0).getFirstRequestWait()+1));
        for (SubmFile sf : submFilesToCheck)
        {
            logger.info("Update subm file create date, accnId="+accnId+" submFileSeqId="+sf.getSubmFileSeqId()+", fileCreatDt="+newFileCreatDt);
            sf.setFileCreatDt(newFileCreatDt);
            rpmDao.setSubmFile(null, sf);
        }

        claimStatusList = waitForNewClaimStatus(accnId, claimSuffixIdsToCheck, rpmDao.getClaimStatusList(accnId, 0), QUEUE_WAIT_TIME*2);
        Assert.assertEquals(claimStatusList.size(), claimSuffixIdsToCheck.size(), "Expected new Claim Status for each claim ID suffix");

        logger.info("Verifying claim status is not triggered when Max Requests is met, accnId="+accnId);
        claimStatusList = waitForNewClaimStatus(accnId, claimSuffixIdsToCheck, rpmDao.getClaimStatusList(accnId, 0), QUEUE_WAIT_TIME*2);
        Assert.assertTrue(claimStatusList.isEmpty(), "Expected no new Claim Status for accession");

        logger.info("Verifying claim status is only triggered for specific claim, accnId="+accnId);
        List<String> newClaimSuffixIdsToCheck = Arrays.asList(claimSuffixIdsToCheck.get(claimSuffixIdsToCheck.size()-1));
        cleanClaimStatusTableByAccnIdClaimIdSuffix(accnId, newClaimSuffixIdsToCheck);
        claimStatusList = waitForNewClaimStatus(accnId, newClaimSuffixIdsToCheck, rpmDao.getClaimStatusList(accnId, 0), QUEUE_WAIT_TIME*2);
        Assert.assertEquals(claimStatusList.size(), newClaimSuffixIdsToCheck.size(), "Expected new Claim Status for only 1 claim ID suffix");
    }

    private boolean waitForNewQASRecordToBeAdded(String accnId, long maxTime) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isNewQASRecordAdded = isNewQASRecordAdded(accnId);

        while (!isNewQASRecordAdded && System.currentTimeMillis() < maxTime) {
            logger.info("Waiting for new QAccnSubm record to be added, accnId=" + accnId + ", elapsedTime=" + calcElapsedTime(startTime));
            Thread.sleep(QUEUE_POLL_TIME);
            isNewQASRecordAdded = isNewQASRecordAdded(accnId);
        }

        return isNewQASRecordAdded;
    }

    private void cleanClaimStatusTablesAndClaimStatHistoryByAccnId(@Optional String accnId) throws XifinDataAccessException
    {
        logger.info("Deleting all QClaimStatus records for accession, accnId=" + accnId);
        rpmDao.deleteQClaimStatus(testDb, accnId);
        logger.info("Deleting all AccnClmStatHist records for accession, accnId=" + accnId);
        rpmDao.deleteAccnClaimStatHistByAccnId(testDb, accnId);

        logger.info("Deleting all claim status transaction data for accession, accnId=" + accnId);
        List<ClaimStatus> claimStatuses = rpmDao.getClaimStatusList(accnId, 0);
        for (ClaimStatus claimStatus : claimStatuses)
        {
            rpmDao.deleteClaimStatusStatusRecordsByClaimId(claimStatus.getSeqId());
            rpmDao.deleteClaimStatusProcRecordsByClaimId(claimStatus.getSeqId());
            rpmDao.deleteClaimStatusRecordBySeqId(claimStatus.getSeqId());
        }
    }

    private void cleanClaimStatusTableByAccnIdClaimIdSuffix(String accnId, List<String> claimIdSuffixList) throws XifinDataAccessException
    {
        logger.info("Deleting all QClaimStatus records for claims, accnId=" + accnId+", claimIdSuffixList="+claimIdSuffixList);
        for (QAccnSubm qas : rpmDao.getQAccnSubm(null, accnId))
        {
            if (claimIdSuffixList.contains(qas.getClaimIdSuffix()))
            {
                for (QClaimStatus qcs : rpmDao.getQClaimStatusByAccnIdDocSeqId(accnId, qas.getDocSeqId()))
                {
                    qcs.setResultCode(ErrorCodeMap.DELETED_RECORD);
                    databaseSequenceDao.setValueObject(qcs);
                }
            }
        }

        logger.info("Deleting all claim status transaction data for accession, accnId=" + accnId+", claimIdSuffixList="+claimIdSuffixList);
        for (ClaimStatus claimStatus : rpmDao.getClaimStatusList(accnId, 0))
        {
            if (claimIdSuffixList.contains(claimStatus.getClaimIdSuffix()))
            {
                rpmDao.deleteClaimStatusStatusRecordsByClaimId(claimStatus.getSeqId());
                rpmDao.deleteClaimStatusProcRecordsByClaimId(claimStatus.getSeqId());
                rpmDao.deleteClaimStatusRecordBySeqId(claimStatus.getSeqId());
            }
        }
    }


    private boolean isNewQASRecordAdded(String accnId) {
        try {
            logger.info("Retrieving list of QAccnSubm records, accnId=" + accnId);
            List<QAccnSubm> qAccnSubms = rpmDao.getQAccnSubm(testDb, accnId);
            if (qAccnSubms.size() < 2) {
                return false;
            }
            if (qAccnSubms.get(qAccnSubms.size()-1).getSubmUserId().equalsIgnoreCase(AccnStatusMap.CLAIM_STATUS_ENGINE_USER)) {
                return true;
            }

        } catch (XifinDataAccessException e) {
            return false;
        }

        return false;
    }

    private void clearQClaimStatusByAccnId(@Optional String accnId) throws XifinDataAccessException {
        logger.info("Clearing QClaimStatus records for accnId=" + accnId);
        rpmDao.deleteQClaimStatus(testDb, accnId);
    }

    /**
     * For a particular accession and payor, delete all the QAS entries except those that match the docSeqId list.
     *
     * @param accnId              Accession ID
     * @param pyrId               Payor ID
     * @param docSeqIdsStringList Doc Seq ID list
     * @throws Exception Exception
     */
    protected void cleanQASForPyrOnAccnExceptDocSeqIds(String accnId, int pyrId, List<String> docSeqIdsStringList) throws Exception {
        logger.info("Clearing QAS for Pyr on accn, accnId=" + accnId + ", pyrId=" + pyrId);
        List<QAccnSubm> qasListToDelete = new ArrayList<>();
        for (QAccnSubm accnSubm : rpmDao.getQAccnSubm(null, accnId))
        {
            if (accnSubm.getPyrId() == pyrId && !docSeqIdsStringList.contains(String.valueOf(accnSubm.getDocSeqId()))) {
                qasListToDelete.add(accnSubm);
            }
        }
        submissionDao.deleteSubmissions(qasListToDelete);
    }

    private String calcElapsedTime(long startTime) {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s";
    }

    public List<ClaimStatus>  waitForNewClaimStatus(String accnId, List<String> claimIdSuffixList, List<ClaimStatus> origClaimStatusList, long maxWaitTime)
            throws XifinDataAccessException, InterruptedException
    {
        List<ClaimStatus> claimStatusList = new ArrayList<>();
        long curTime, startTime = System.currentTimeMillis();
        long endTime = startTime + maxWaitTime;
        boolean isDone = false;
        List<ClaimStatus> csList = new ArrayList<>();
        while (!isDone && (curTime = System.currentTimeMillis()) < endTime)
        {
            List<String> claimIdSuffixListCopy = new ArrayList<>(claimIdSuffixList);
            logger.info("Waiting for ClaimStatus check, accnId=" + accnId + ", claimIdSuffixList="+claimIdSuffixListCopy+", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(curTime-startTime) + "s, maxTime=" + TimeUnit.MILLISECONDS.toSeconds(QUEUE_WAIT_TIME) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            List<ClaimStatus> newClaimStatusList = rpmDao.getClaimStatusList(accnId, 0);
            if (!newClaimStatusList.isEmpty())
            {
                newClaimStatusList = newClaimStatusList.subList(origClaimStatusList.size(), newClaimStatusList.size());
                for (ClaimStatus cs : newClaimStatusList)
                {
                    claimIdSuffixListCopy.remove(cs.getClaimIdSuffix());
                }
                if (claimIdSuffixListCopy.isEmpty())
                {
                    csList = newClaimStatusList;
                    isDone = true;
                }
            }
        }
        return csList;
    }
}
