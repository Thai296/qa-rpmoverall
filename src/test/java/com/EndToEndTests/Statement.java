package com.EndToEndTests;


import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnSubmFile.ClnSubmFile;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.phys.Phys;
import com.mbasys.mars.ejb.entity.pt.Pt;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submFileAudit.SubmFileAudit;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.FileManipulation;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Statement extends EndToEndBaseTest {


    @Test(priority = 1, description = "Generate statement for Third Party Payor Accession")
    @Parameters({"email", "password", "project", "testSuite", "testCase"})
    public void generateStmtFor3rdPartyPyrAccn(String email, String password, String project, String testSuite, String testCase) throws Exception {
        String accnId = createAccn(email, password, project, testSuite, testCase);

        logger.info("*** Step 4 Action - Check if accn is in QElig and then run Elig engine");
        boolean qElig = accessionDao.isInEligibilityQueue(accnId);
        if(qElig) {
            xifinAdminUtils.runPFEngine(this, email, password, null, "EligEngine", "SSO_APP_STAGING", true);
        }

        logger.info("*** Step 4 Expected Result - Ensure AccnId is processed by Elig Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_ELIG), " Accession is still in Q_ELIG");

        logger.info("*** Step 6 Action - Run Pricing Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "PricingEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 6 Expected Result - Ensure AccnId is processed by Pricing Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_PRICE), " Accession is not in Q_PRICE");

        logger.info("*** Step 6 Expected Result - Ensure AccnId is Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");

        logger.info("*** Step 6 Expected Result  Ensure AccnId is in QAccnSubm");
        QAccnSubm qAccnSubm = rpmDao.getQAccnSubm(null, accnId).get(0);

        Assert.assertNotNull(qAccnSubm, " Accession is not Q_ACCN_SUBM");

        logger.info("*** Step 7 Action - Update Last Date for Subm cvs to be null for given submSvcAbbrev");
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, qAccnSubm.getSubmSvcSeqId());

        logger.info("*** Step 8 Action - Run NonClnSubm Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "NonClnSubmEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 8 Expected Result - Ensure AccnId is processed by NonClnSubm Engine");
        qAccnSubm = rpmDao.getQAccnSubm(null, accnId).get(0);
        Assert.assertTrue(qAccnSubm.getSubmFileSeqId() > 0);

        logger.info("*** Step 8 Action - Run NonClnStm Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "NonClnStmtEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 8 Expected Result - Ensure AccnId is processed by NonClnStmt Engine");
        SubmFile submFile = submissionDao.getSubmFileBySubmFileSeqId(qAccnSubm.getSubmFileSeqId());
        Assert.assertNotNull(submFile.getFilename(), " No file generated");
        StringBuilder submFilePath = new StringBuilder(FileManipulation.getBaseDir());
        submFilePath.append(File.separator).append(config.getProperty(PropertyMap.ORGALIAS)).append(File.separator).append("files").append(File.separator).append("nonclientsubm").append(File.separator).append("out").append(File.separator).append(submFile.getFilename());
        File file = new File(submFilePath.toString());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFile.getFilename());

        logger.info("*** Step 9 Verify - Details are present in submFile");
        Hashtable<String, String> lookUpDetails = new Hashtable<>();
        SubmFileAudit submfileaudit = accessionDao.getSubmFileAuditbyAccnId(accnId);
        lookUpDetails.put(submfileaudit.procId, "submfileaudit.procId");
        lookUpDetails.put(Integer.toString(submfileaudit.submFileSeqId), "submfileaudit.submFileSeqId");
        lookUpDetails.put((new SimpleDateFormat(ConvertUtil.DateFormatNoSeperator).format(submfileaudit.audDt)), "submfileaudit.audDt");

        Accn accn = accessionDao.getAccn(accnId);

        lookUpDetails.put(accn.ptLNm, "accn.ptLNm");
        lookUpDetails.put((new SimpleDateFormat(ConvertUtil.DateFormatNoSeperator).format(accn.ptDob)), "accn.ptDob");
        lookUpDetails.put(accn.ptAddr1, "accn.ptAddr1");
        lookUpDetails.put(accn.ptAddr2, "accn.ptAddr2");
        lookUpDetails.put(submissionDao.getQAccnSubmsByAccnId(accnId).get(0).getSubmittedSubsId(), "QAccnSubm submittedSubsId");
        lookUpDetails.put((accessionDao.getAccnDiagByAccnId(accnId).diagCdId).replace(".", ""), "AccnDiag diagCdId");
        lookUpDetails.put(accessionDao.getAccnProcByAccnId(accnId).procId, "procId");

        Cln cln = clientDao.getClnByAccnId(accnId);
        lookUpDetails.put(cln.clnAbbrev, "cln.clnAbbrev");
        lookUpDetails.put(cln.bilZipId, "cln.bilZipId");
        lookUpDetails.put(payorDao.getPyrFromAccnPyrByAccnId(accnId).name, "Pyr name");

        Phys phys = physicianDao.getPhysByAccnID(accnId);
        lookUpDetails.put(phys.physFname,"phys.physFname");
        lookUpDetails.put(phys.physLname,"phys.physLname");
        lookUpDetails.put(String.valueOf(phys.npiId),"phys.npiId");

        fileContainsString(submFilePath.toString(), lookUpDetails);
    }

    @Test(priority = 1, description = "Generate statement for patient billed accession")
    @Parameters({"email", "password", "project", "testSuite", "testCase"})
    public void generateStmtForPatientBilledAccn(String email, String password, String project, String testSuite, String testCase) throws Exception {
        String accnId = createAccn(email, password, project, testSuite, testCase);

        logger.info("*** Step 4 Action - Check if accn is in QElig and then run Elig engine");
        boolean qElig = accessionDao.isInEligibilityQueue(accnId);
        if(qElig) {
            xifinAdminUtils.runPFEngine(this, email, password, null, "EligEngine", "SSO_APP_STAGING", true);
        }

        logger.info("*** Step 4 Expected Result - Ensure AccnId is processed by Elig Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_ELIG), " Accession is still in Q_ELIG");

        logger.info("*** Step 5 Action - Run Pricing Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "PricingEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 5 Expected Result - Ensure AccnId is processed by Pricing Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_PRICE), " Accession is not in Q_PRICE");

        logger.info("*** Step 6 Expected Result  Ensure AccnId is in QAccnSubm");
        QAccnSubm qAccnSubm = rpmDao.getQAccnSubm(null, accnId).get(0);

        Assert.assertNotNull(qAccnSubm, " Accession is not Q_ACCN_SUBM");

        logger.info("*** Step 7 Action - Update Last Date for Subm cvs to be null for given submSvcAbbrev");
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, qAccnSubm.getSubmSvcSeqId());

        logger.info("*** Step 8 Action - Run NonClnSubm Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "NonClnSubmEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 8 Expected Result - Ensure AccnId is processed by NonClnSubm Engine");
        qAccnSubm = rpmDao.getQAccnSubm(null, accnId).get(0);
        Assert.assertTrue(qAccnSubm.getSubmFileSeqId() > 0);

        logger.info("*** Step 8 Action - Run NonClnStm Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "NonClnStmtEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 8 Expected Result - Ensure AccnId is processed by NonClnStmt Engine");
        SubmFile submFile = submissionDao.getSubmFileBySubmFileSeqId(qAccnSubm.getSubmFileSeqId());
        Assert.assertNotNull(submFile.getFilename(), " No file generated");
        StringBuilder submFilePath = new StringBuilder(FileManipulation.getBaseDir());
        submFilePath.append(File.separator).append(config.getProperty(PropertyMap.ORGALIAS)).append(File.separator).append("files").append(File.separator).append("nonclientsubm").append(File.separator).append("out").append(File.separator).append(submFile.getFilename());
        File file = new File(submFilePath.toString());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFile.getFilename());

        Hashtable<String, String> lookUpDetails = new Hashtable<String, String>();
        lookUpDetails.put(accnId, "accnId");
        lookUpDetails.put(accessionDao.getAccnPyrs(accnId).get(0).subsId, "AccnPyr subsId");
        lookUpDetails.put(accessionDao.getAccnTestsByAccnId(accnId).get(0).procId, "AccnPyr procId");

        Accn accn = accessionDao.getAccn(accnId);
        lookUpDetails.put(accn.ptLNm, "accn.ptLNm");
        lookUpDetails.put(accn.ptLNm, "accn.ptLNm");
        lookUpDetails.put(accn.ptZipId, "accn.ptZipId");
        lookUpDetails.put(new SimpleDateFormat("yyyyMMdd").format(accn.ptDob), "accn.ptDob");
        lookUpDetails.put((new SimpleDateFormat("yyyyMMdd").format(accn.dos)), "accn.dos");

        SubmFileAudit submfileaudit = accessionDao.getSubmFileAuditbyAccnId(accnId);
        lookUpDetails.put(submfileaudit.procId, "submfileaudit.procId");
        lookUpDetails.put(Integer.toString(submfileaudit.submFileSeqId), "submfileaudit.submFileSeqId");
        lookUpDetails.put((new SimpleDateFormat("yyyyMMdd").format(submfileaudit.audDt)), "submfileaudit.audDt");

        Cln cln = clientDao.getClnByAccnId(accnId);
        lookUpDetails.put(cln.bilAcctNm, "cln.bilAcctNm");
        lookUpDetails.put(cln.clnAbbrev, "cln.clnAbbrev");

        Fac fac = facilityDao.getFacFromFacByAccnTestAccnID(accnId);
        lookUpDetails.put(fac.name, "fac.name");
        lookUpDetails.put(fac.name, "fac.name");
        lookUpDetails.put(fac.addr2, "fac.addr2");
        lookUpDetails.put(fac.city, "fac.city");
        lookUpDetails.put(fac.stId, "fac.stId");
        lookUpDetails.put(fac.zipId, "fac.zipId");

        Pt patient = patientDao.getPtFromPtByAccnID(accnId);
        lookUpDetails.put(patient.ptAddr1, "v");
        lookUpDetails.put(patient.ptAddr2, "patient.ptAddr2)");
        lookUpDetails.put(patient.ptStId, "patient.ptStId");
        lookUpDetails.put(patient.ptZipId, "patient.ptZipId");
        lookUpDetails.put(patient.ptCity, "patient.ptCity");


        Phys phys = physicianDao.getPhysByAccnID(accnId);
        lookUpDetails.put(phys.physFname, "phys.physFname");
        lookUpDetails.put(phys.physLname, "phys.physLname");
        lookUpDetails.put(String.valueOf(phys.npiId), "phys.npiId");

        Pyr pyr = payorDao.getPyrFromAccnPyrByAccnId(accnId);
        lookUpDetails.put(pyr.name, "pyr.name");
        lookUpDetails.put(pyr.cntctCity, "pyr.cntctCity");

        QAccnSubm qAccnSubm1 = rpmDao.getQAccnSubm(null, accnId).get(0);
        lookUpDetails.put(qAccnSubm1.submittedSubsId, "qAccnSubm1.submittedSubsId");;

        lookUpDetails.put(submissionDao.getQAccnSubmsByAccnId(accnId).get(0).getSubmittedSubsId(), "accnId.submittedSubsId");

        fileContainsString(submFilePath.toString(), lookUpDetails);
    }
    @Test(priority = 1, description = "Generate client statement")
    @Parameters({"email", "password", "project", "testSuite", "testCase"})
    public void generateClientStatement(String email, String password, String project, String testSuite, String testCase) throws Exception {
        logger.info("***Step 1, 2 & 3 Action- Create accession with payor C");
        String accnId = createAccn(email, password, project, testSuite, testCase);

        logger.info("*** Step 4 Action - Run Pricing Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "PricingEngine", "SSO_APP_STAGING", true);

        logger.info("***Step 5 Action - Ensure accn id has same CLN_SUBM_DT field populated as display value of SYSTEM_SETTING.PK_SETTING ID = 71");
        clientDao.updateCLNSubmDtForAccnID(accnId,"30/JUN/2015");

        logger.info("*** Step 5 Expected Result - Ensure AccnId is processed by Pricing Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_PRICE), " Accession is not in Q_PRICE");

        logger.info("*** Step 6 Action- Delete the CLN_SUBM_FILE records for the client used to create accession");
        Accn accn = accessionDao.getAccn(accnId);
        clientDao.deleteClnSubmFileByClnId(accn.clnId);

        logger.info("*** Step 7 Action - Run ClnSubm Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "ClnSubmEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 7 Expected Result - Verify that new CLN_SUBM_FILE and SUBM_FILE records are created");
        List<ClnSubmFile> clnSubmFile = clientDao.getClnSubmFileByClnId(accn.clnId);
        clnSubmFile = clientDao.getClnSubmFileByClnId(accn.clnId);
        long currentTime = System.currentTimeMillis();
        while (clnSubmFile.size() < 2) {
            clnSubmFile = clientDao.getClnSubmFileByClnId(accn.clnId);
            logger.info(" waiting for the pdf file to be generated");
            if(((System.currentTimeMillis()- currentTime)/1000) > maxWaitTimeinSec){
                Assert.assertFalse(clnSubmFile.size() < 2);
            }
        }
        Assert.assertNotNull(clnSubmFile);
        SubmFile submFile = submissionDao.getSubmFileBySubmFileSeqId(clnSubmFile.get(1).submFileSeqId);
        Assert.assertNotNull(submFile);

        logger.info("*** Step 8 Action - Delete the existing pdf file from the qa07 directory");
        fileManipulation.deleteFile(submFile.getDir().trim().replaceAll("[^0-9]", ""), submFile.getFilename().trim());

        logger.info("*** Step 8 Expected Result - Verify the file is deleted");
        String fileLocation = submFile.getDir() + File.separator;
        String submFileFileName = submFile.getFilename();
        File fGenerated = new File(fileLocation + submFileFileName);
        Assert.assertFalse(isFileExists(fGenerated, 5), "        Client Statement file: " + submFileFileName + " should be generated in " + fileLocation + " folder.");

        logger.info("*** Step 9 Action - Run ClnStmt Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "ClnStmtEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 9 expected Result - Verify that the B_EGATE_Processed = 1 in SUBM_FILE table obtained by seq_id ifron cln_subm_file");
        submFile = submissionDao.getSubmFileBySubmFileSeqId(clnSubmFile.get(1).submFileSeqId);
        if (submFile.getFilename().contains(".xlsx")) {
            submFile = submissionDao.getSubmFileBySubmFileSeqId(clnSubmFile.get(0).submFileSeqId);
        }
        Assert.assertTrue(submFile.getIsEgateProcessed());

        logger.info("*** Step 9 expected Result - Verify the file is created");
        StringBuilder submFilePath = new StringBuilder(FileManipulation.getBaseDir());
        submFilePath.append(File.separator).append(config.getProperty(PropertyMap.ORGALIAS)).
                append(File.separator).append("files").
                append(File.separator).append(submFile.getDir()).
                append(File.separator).append(submFile.getFilename());
        logger.info("submFilePath: " + submFilePath.toString());

        fGenerated = new File(submFilePath.toString());
        Assert.assertTrue(isFileExists(fGenerated, 5), "        Client Statement file: " + submFileFileName + " should be generated in " + fileLocation + " folder.");

        ConvertUtil convertUtil= new ConvertUtil();
        String pdfContentsStr = convertUtil.getTextFromPdf(1, -1, submFilePath.toString());

        Cln cln = clientDao.getClnByAccnId(accnId);
        Assert.assertTrue(pdfContentsStr.contains(cln.bilAcctNm),"        PDF file  should contain " + cln.bilAcctNm);
        Assert.assertTrue(pdfContentsStr.contains(cln.clnAbbrev),"        PDF file  should contain " + cln.clnAbbrev);
        if(cln.bilAcctNm != null) {
            Assert.assertTrue(pdfContentsStr.contains(cln.bilAcctNm), "        PDF file  should contain " + cln.bilAddr1);
        }
        if(cln.bilAddr1 != null) {
            Assert.assertTrue(pdfContentsStr.contains(cln.bilAddr1), "        PDF file  should contain " + cln.bilAddr1);
        }
        if(cln.bilAddr2 != null) {
            Assert.assertTrue(pdfContentsStr.contains(cln.bilAddr2), "        PDF file  should contain " + cln.bilAddr2);
        }
        if(cln.bilCity != null) {
            Assert.assertTrue(pdfContentsStr.contains(cln.bilCity), "        PDF file  should contain " + cln.bilCity);
        }
        if(cln.bilStId != null) {
            Assert.assertTrue(pdfContentsStr.contains(cln.bilStId),"        PDF file  should contain " + cln.bilStId);
        }
        if(cln.bilZipId != null) {
            Assert.assertTrue(pdfContentsStr.contains(cln.bilZipId),"        PDF file  should contain " + cln.bilStId);
        }
        accn = accessionDao.getAccn(accnId);
        logger.info("accnId: " + accnId + " accn.reqId:"+accn.reqId+" accn.ptLNm:"+ accn.ptLNm+ " accn.ptFNm:"+accn.ptFNm);
        Assert.assertTrue(pdfContentsStr.contains(accn.ptLNm),"        PDF file  should contain " + accn.ptLNm);
        if(accn.ptFNm != null) {
            Assert.assertTrue(pdfContentsStr.contains(accn.ptFNm), "        PDF file  should contain " + accn.ptLNm);
        }
        Assert.assertTrue(pdfContentsStr.contains(accn.ptLNm),"        PDF file  should contain " + accn.ptLNm);
        if(accn.ptLNm != null) {
            Assert.assertTrue(pdfContentsStr.contains(accn.ptLNm), "        PDF file  should contain " + accn.ptLNm);
        }
        if(accn.ptDob != null) {
            String dateInFormat = (new SimpleDateFormat("MM/dd/yyyy").format(accn.ptDob)).toString();
            Assert.assertTrue(pdfContentsStr.contains(dateInFormat), "        PDF file  should contain " + dateInFormat);
        }
        ArrayList<String> testInfoList = daoManagerAccnWS.getTestIdFacIdFromAccnTestByAccnId(accnId, testDb);
        String testAbbrevinAccn = daoManagerAccnWS.getTestAbbrevFromTESTByTestId(testInfoList.get(0), testDb).trim();
        Assert.assertTrue(pdfContentsStr.contains(testAbbrevinAccn), "        PDF file  should contain " + testAbbrevinAccn);
    }
}