package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnDiag.AccnDiag;
import com.mbasys.mars.ejb.entity.accnPhys.AccnPhys;
import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.ejb.entity.accnTestCons.AccnTestCons;
import com.mbasys.mars.ejb.entity.cons.Cons;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.phys.Phys;
import com.mbasys.mars.ejb.entity.procCd.ProcCd;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrSubmSvc.PyrSubmSvc;
import com.mbasys.mars.ejb.entity.pyrXref.PyrXref;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.qAccnSubmStage.QAccnSubmStage;
import com.mbasys.mars.ejb.entity.submClaimAudit.SubmClaimAudit;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submFileAudit.SubmFileAudit;
import com.mbasys.mars.ejb.entity.submSvc.SubmSvc;
import com.mbasys.mars.ejb.entity.xref.Xref;
import com.mbasys.mars.ejb.entity.xrefTyp.XrefTyp;
import com.mbasys.mars.persistance.DatabaseMap;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.utils.EngineUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.rpm.domain.StmtConfig;
import com.xifin.util.DateConversion;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.TestDataSetup;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class NonClientStatementEngineCMSTest extends NonClientStatementEngineTest
{
    private EngineUtils engineUtils;

    @Test(priority = 1, description = "Cms1500 Box 33B displays from statement config")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "tpiStmtConfig", "stmtConfig", "facAbbrv"})
    public void testPFER_566(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String tpiStmtConfig, int stmtConfig, String facAbbrv) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-566 - Non-Client Subm and Statement eng - Cms1500 Box 33B displays from statement config");
        logger.info("Make sure there is a Statement Config for given Pyr and Subm_svc");
        cleanUpAccn(accnId);

        String stmtConfigStmtData = rpmDao.getStatementConfigStmtDataByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId(), stmtConfig);
        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrv);
        Assert.assertTrue(stmtConfigStmtData.contains(fac.getNpi() + "|" + tpiStmtConfig));

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to process the statement");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);

        logger.info("Find New Statement File in the nonclientsubm/out dir");
        String submFileName = submFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        logger.info("Verify File content, submFileName " + submFileName);
        String textStatement = convertPdfToText(file);

        logger.info("Make sure New Statement File contains accnId, " + accnId);
        QAccnSubm qAccnSubm = rpmDao.getQAccnSubm(testDb, accnId).get(0);
        Assert.assertTrue(textStatement.contains(qAccnSubm.getAccnId() + "Z" + qAccnSubm.getClaimIdSuffix()));

        logger.info("Make sure New Statement File contains TPI from Statement Config, " + tpiStmtConfig);
        Assert.assertTrue(textStatement.contains(tpiStmtConfig));

        logger.info("Make sure New Statement File contains NPI from Statement Config, " + fac.getNpi());
        Assert.assertTrue(textStatement.contains(String.valueOf(fac.getNpi())));

        logger.info("Make sure subm_claim_audit record is created for 837");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(testDb, qAccnSubm.getDocSeqId());
        logger.info("SubmClaimAudit ValueObject [" + submClaimAudit.toString() + "]");
        logger.info("Expected values, facId=" + fac.getFacId() + ", npi=" + fac.getNpi() + ", facName=" + fac.getName());
        Assert.assertEquals(submClaimAudit.getBillingFacId(), fac.getFacId());
        Assert.assertEquals(submClaimAudit.getSubmittedNpi(), fac.getNpi());
        Assert.assertEquals(submClaimAudit.getSubmittedFacName(), fac.getName());

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qAccnSubm.getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Cms1500 Box 33B - pyr_subm_svc.ext_recvr_id")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "npiBillingOverride", "tpiExtReciveId", "facAbbrv"})
    public void testPFER_565(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String npiBillingOverride, String tpiExtReciveId, String facAbbrv) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-565");
        cleanUpAccn(accnId);

        logger.info("Make sure there is a record in pyr_subm_svc for given Pyr and Subm_svc and Fac");
        SubmSvc submSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrv);

        PyrSubmSvc pyrSubmSvc = rpmDao.getPyrSubmSvc(testDb, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId(), fac.getFacId(), submSvc.getSubmSvcSeqId());
        Assert.assertEquals(pyrSubmSvc.getExtRecvrId(), tpiExtReciveId);

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvc.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to process the statement");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);

        logger.info("Find New Statement File in the nonclientsubm/out dir");
        String submFileName = submFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        String textStatement = convertPdfToText(file);

        logger.info("Make sure New Statement File contains accnId, " + accnId);
        QAccnSubm qAccnSubm = rpmDao.getQAccnSubm(testDb, accnId).get(0);
        Assert.assertTrue(textStatement.contains(qAccnSubm.getAccnId() + "Z" + qAccnSubm.getClaimIdSuffix()));

        logger.info("Make sure New Statement File contains TPI from pyr_subm_svc.ext_recvr_id, " + tpiExtReciveId);
        Assert.assertTrue(textStatement.contains(tpiExtReciveId));

        logger.info("Make sure New Statement File contains NPI from Billing Override, " + npiBillingOverride);
        Assert.assertTrue(textStatement.contains(npiBillingOverride));

        logger.info("Make sure subm_claim_audit record is created for 837");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(testDb, qAccnSubm.getDocSeqId());
        logger.info("SubmClaimAudit ValueObject [" + submClaimAudit.toString() + "]");
        logger.info("Expected values, facId=" + fac.getFacId() + ", npi=" + npiBillingOverride + ", facName=" + fac.getName());
        Assert.assertEquals(submClaimAudit.getBillingFacId(), fac.getFacId());
        Assert.assertEquals(String.valueOf(submClaimAudit.getSubmittedNpi()), npiBillingOverride);
        Assert.assertEquals(submClaimAudit.getSubmittedFacName(), fac.getName());

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qAccnSubm.getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Cms1500 Box 31 is displaying signature from SS 4003 when PYR.HCFA_SIGNATURE is empty")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "facAbbrv"})
    public void testPFER_737(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String facAbbrv) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-737");
        cleanUpAccn(accnId);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        SubmSvc submSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrv);

        logger.info("Make sure there is no statement config 110025");
        Assert.assertNull(rpmDao.getStatementConfigStmtDataByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId(), 110025));

        logger.info("Make sure PYR has NO signature saved in PYR.HCFA_SIGNATURE, primPyrAbbrv=" + pyrAbbrv + " hcfaSignature=" + pyr.getHcfaSignature());
        Assert.assertNull(pyr.getHcfaSignature());

        String systemSetting4003DataValue = systemDao.getSystemSetting(4003).getDataValue();
        logger.info("Make sure SS 4003 has default signature to populate Box 31=" + systemSetting4003DataValue);
        Assert.assertNotNull(systemSetting4003DataValue);

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvc.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to process the statement");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);

        logger.info("Find New Statement File in the nonclientsubm/out dir");
        String submFileName = submFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        String textStatement = convertPdfToText(file);

        logger.info("Make sure New Statement File contains accnId, " + accnId);
        QAccnSubm qAccnSubm = rpmDao.getQAccnSubm(testDb, accnId).get(0);
        Assert.assertTrue(textStatement.contains(qAccnSubm.getAccnId() + "Z" + qAccnSubm.getClaimIdSuffix()));

        logger.info("Make sure New Statement File contains signature from SS4003, " + systemSetting4003DataValue);
        Assert.assertTrue(textStatement.contains(StringUtils.upperCase(systemSetting4003DataValue)));

        logger.info("Make sure New Statement File contains facility name, " + fac.getName());
        Assert.assertTrue(textStatement.contains(StringUtils.upperCase(fac.getName())));

        logger.info("Make sure subm_claim_audit record is created for 837");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(testDb, qAccnSubm.getDocSeqId());

        logger.info("Expected values, facId=" + fac.getFacId() + ", facName=" + fac.getName());
        Assert.assertEquals(submClaimAudit.getBillingFacId(), fac.getFacId());
        Assert.assertEquals(submClaimAudit.getSubmittedFacName(), fac.getName());

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qAccnSubm.getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }
    private String createAccn(String project, String testSuite, String testCase, String propLevel, String propName) throws Exception {
        TestDataSetup testDataSetup = new TestDataSetup(driver);

        String orgAlias = config.getProperty(PropertyMap.ORGALIAS);
        String endpoint = config.getProperty(PropertyMap.ACCNWS_URL);
        String wsUsername = config.getProperty(PropertyMap.ACCNWS_USER);
        String wsPassword = config.getProperty(PropertyMap.ACCNWS_PASSWORD);
        String testingDb = config.getProperty(PropertyMap.RPM_DB_USER);
        String accnID = testDataSetup.sendWSReqestTestCaseNoParentFile(project, testSuite, testCase, propLevel,
                propName, orgAlias, wsUsername, wsPassword, testingDb, endpoint, null);
        Assert.assertTrue(accnID != null, "        The new Accession is created: " + accnID);

        return accnID;
    }

    private void updateAccn(String accnId, PyrXref pyrXref, boolean isCreateXrefTyp) throws Exception {
        Accn accn = accessionDao.getAccn(accnId);
        accn.setStaId(21);
        accn.setOnsetTypId(1);
        accn.setPrcDt(DateConversion.getNowAsSqlDate());
        accn.setAcctngDt(DateConversion.getNowAsSqlDate());
        accn.setOnsetDt(DateConversion.getNowAsSqlDate());
        accn.setAccidentCauseId(1);
        accn.setResultCode(ErrorCodeMap.RECORD_FOUND);
        if(isCreateXrefTyp) {
            accn.setDos(pyrXref.getEffDt());
        }
        accessionDao.setAccn(accn);
    }

    private void updateAccnPyr(AccnPyr accnPyr, String accnId) throws Exception {
        accnPyr.setAccnId(accnId);
        accnPyr.setPyrPrio(1);
        accnPyr.setInsCntryId(81);
        accnPyr.setPyrId(149577);
        accnPyr.setGrpId("ZK");
        accnPyr.setSubsId("AI");
        accnPyr.setInsFNm("REBECCA");
        accnPyr.setInsLNm("HEATH");
        accnPyr.setInsAddr1("T19G5X7ON3");
        accnPyr.setInsAddr2("0DJ9BZTI5U");
        accessionDao.setAccnPyr(accnPyr);
    }

    private void createXrefTyp() throws Exception {
        XrefTyp xrefTyp = null;
        try {
            xrefTyp = rpmDao.getXrefTypByAbbrev(testDb, "ORDPHYSNAME");
        } catch (Exception e) {
            if (xrefTyp == null) {
                xrefTyp = new XrefTyp();
                xrefTyp.setDescr("Ordering Physician Name Override");
                xrefTyp.setAbbrev("ORDPHYSNAME");
                xrefTyp.setXrefTypId(512);
                xrefTyp.setXrefCatId(5);
                rpmDao.setXrefTyp(testDb, xrefTyp);
            }
        }
    }

    private void createXref(Xref xref) throws Exception {
        if(xref == null) {
            xref = new Xref();
            xref.setDescr("UHCSR");
            xref.setAbbrev("UHCSR");
            xref.setXrefTypId(512);
            xref.setXrefId(rpmDao.getNextValueInSequence(testDb, DatabaseMap.SEQ_XREF_ID_SEQ));
            rpmDao.setXref(testDb, xref);
        }
    }

    private void createPyr() throws Exception {
        Pyr pyr = null;
        try {
            // create data pyr
            pyr = rpmDao.getPyrByPyrAbbrv(testDb, "6628");
        } catch (Exception e) {
            if (pyr == null) {
                pyr = new Pyr();
                pyr.setPyrAbbrv("6628");
                pyr.setName("Sonora");
                pyr.setPyrId(149577);
                pyr.setManSubmSvcId(3);
                pyr.setCntctStId("RI");
                pyr.setCntctCntryId(0);
                pyr.setDiagSelMthdTypId(2);
                pyr.setDnlTblId(1);
                pyr.setDefPhysIdTypId(9);
                pyr.setPyrGrpId(2);
                pyr.setApplDocId(0);
                rpmDao.setPyr(testDb, pyr);
            }
        }
    }

    private void createPyrXref(Xref xref, PyrXref pyrXref) throws Exception {
        try {
            pyrXref = rpmDao.getPyrXrefByPyrIdAndXref(testDb, 149577, xref.getXrefId());
        } catch (Exception e) {
            if(pyrXref == null) {
                pyrXref = new PyrXref();
                pyrXref.setXrefId(xref.getXrefId());
                pyrXref.setPyrId(149577);
                pyrXref.setEffDt(DateConversion.getNowAsSqlDate());
                rpmDao.setPyrXref(testDb, pyrXref);
            }
        }
    }

    private void createProcCd(ProcCd procCd, String procId, String descr) throws Exception {
        if(procCd == null) {
            procCd = new ProcCd();
            procCd.setProcId(procId);
            procCd.setDescr(descr);
            if(systemDao.getSystemSetting(SystemSettingMap.SS_DEFAULT_PROC_TYP_ID).getDataValue() != null) {
                procCd.setProcTypId(Integer.parseInt(systemDao.getSystemSetting(SystemSettingMap.SS_DEFAULT_PROC_TYP_ID).getDataValue()));
            } else {
                procCd.setProcTypId(43);
            }
            procCd.setSvcTypId(5);
            procCd.setShortDescr(descr);
            rpmDao.setProCd(testDb, procCd);
        }
    }

    private void createSubmsvc(SubmSvc submSvc, int dataFmtTypeId, String abbrev, String descr) throws Exception {
        if(submSvc == null) {
            submSvc = new SubmSvc();
            submSvc.setSubmSvcSeqId(databaseSequenceDao.getNextSeqIdFromTBLByColName(DatabaseMap.FLD_SUBM_SVC_SUBM_SVC_SEQ_ID, DatabaseMap.TBL_SUBM_SVC));
            submSvc.setSubmCntryId(1);
            submSvc.setDataFmtTypId(dataFmtTypeId);
            submSvc.setDelvryMthdTypId(4);
            submSvc.setAbbrev(abbrev);
            submSvc.setDescr(descr);
            submSvc.setDocTypId(5);
            submSvc.setSubmtrId("ABC");
            submSvc.setRecvrId("ABC");
            submSvc.setCombinedSubmSvcId(0);
            submSvc.setSubmtrIntrchngQualTypId(9);
            submSvc.setRecvrIntrchngQualTypId(9);;
            rpmDao.setSubmSvc(testDb, submSvc);
        }
    }

    private void createPyrSubmsvc(SubmSvc submSvc) throws Exception {
        PyrSubmSvc pyrSubmSvc = null;
        if(submSvc != null) {
            try {
                pyrSubmSvc = rpmDao.getPyrSubmSvc(testDb, 149577, 1, submSvc.getSubmSvcSeqId());
            } catch (Exception e) {
                if(pyrSubmSvc == null) {
                    pyrSubmSvc = new PyrSubmSvc();
                    pyrSubmSvc.setPyrId(149577);
                    pyrSubmSvc.setSubmSvcSeqId(submSvc.getSubmSvcSeqId());
                    pyrSubmSvc.setExtPyrId("123456");
                    pyrSubmSvc.setExtRecvrId("TRNAET");
                    pyrSubmSvc.setFacId(1);
                    rpmDao.setPyrSubmSvc(testDb, pyrSubmSvc);
                }
            }
        }
    }

    private void createSubmFile(SubmSvc submSvc, SubmFile submFile, int dataFmtTypId, String fileName) throws Exception {
        if(submFile == null && submSvc != null) {
            submFile = new SubmFile();
            submFile.setSubmFileSeqId(databaseSequenceDao.getNextSeqIdFromTBLByColName(DatabaseMap.FLD_SUBM_FILE_SUBM_FILE_SEQ_ID, DatabaseMap.TBL_SUBM_FILE));
            submFile.setSubmSvcSeqId(submSvc.getSubmSvcSeqId());
            submFile.setDataFmtTypId(dataFmtTypId);
            submFile.setDir("/nonclientsubm/out/");
            submFile.setFilename(fileName);
            rpmDao.setSubmFile(testDb, submFile);
        }
    }

    private void createAccnPyr(String accnId) throws Exception {
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        if(accnPyrs.isEmpty()) {
            AccnPyr accnPyr = new AccnPyr();
            updateAccnPyr(accnPyr, accnId);
        } else {
            for (AccnPyr accnPyr : accnPyrs) {
                updateAccnPyr(accnPyr, accnId);
            }
        }
    }

    private void createTest(com.mbasys.mars.ejb.entity.test.Test test, String abbrev, String name) throws Exception {
        if(test == null) {
            test = new com.mbasys.mars.ejb.entity.test.Test();
            test.setTestAbbrev(abbrev);
            test.setTestId(rpmDao.getNextValueInSequence(testDb, DatabaseMap.SEQ_TEST_PK_TEST_ID_SEQ));
            test.setName(name);
            test.setTestTypId(2);
            rpmDao.setTest(testDb, test);
        }
    }

    private void createAccnPhys(String accnId, Phys phys, List<AccnPhys> accnPhys) throws Exception {
        if(accnPhys.isEmpty() && phys != null) {
            AccnPhys acPhys= new AccnPhys();
            acPhys.setSeqId(rpmDao.getNextValueInSequence(testDb, DatabaseMap.SEQ_ACCN_PHYS));
            updateAccnPhys(acPhys, accnId, phys, accnPhys);
        } else {
            for (AccnPhys acPhys : accnPhys) {
                updateAccnPhys(acPhys, accnId, phys, accnPhys);
            }
        }
    }

    private void updateAccnPhys(AccnPhys acPhys, String accnId, Phys phys, List<AccnPhys> accnPhys) throws Exception {
        acPhys.setAccnId(accnId);
        acPhys.setPhysSeqId(phys.getSeqId());
        acPhys.setAccnPhysTypId(1);
        rpmDao.setAccnPhys(testDb, acPhys);
    }

    private void createAccnTest(String accnId, ProcCd procCd, com.mbasys.mars.ejb.entity.test.Test test, List<AccnTest> accnTests) throws Exception {
        if(accnTests.isEmpty() && test != null && procCd != null) {
            AccnTest accnTest = new AccnTest();
            accnTest.setAccnTestSeqId(rpmDao.getNextValueInSequence(testDb, DatabaseMap.SEQ_ACCN_TEST_SEQ));
            updateAccnTest(accnTest ,accnId ,test , procCd, accnTests);
            accnTests.add(accnTest);
        } else {
            for (AccnTest accnTest : accnTests) {
                updateAccnTest(accnTest ,accnId ,test, procCd, accnTests);
            }
        }
    }

    private void updateAccnTest(AccnTest accnTest, String accnId, com.mbasys.mars.ejb.entity.test.Test test, ProcCd procCd, List<AccnTest> accnTests) throws Exception {
        accnTest.setAccnId(accnId);
        accnTest.setTestId(test.getTestId());
        accnTest.setProcTypId(43);
        accnTest.setProcId(procCd.getProcId());
        accnTest.setAbncmnt("MMSFFULIDI");
        accnTest.setNote("test");
        accnTest.setResult("JHVEBPAUQJ");
        accessionDao.setAccnTest(accnTest);
    }

    private void createAccnDiag(String accnId, ProcCd procCd, List<AccnTest> accnTests) throws Exception {
        List<AccnDiag>  accnDiags = rpmDao.getAccnDiagByAccnId(testDb, accnId);

        if(accnDiags.isEmpty() && !accnTests.isEmpty()) {
            AccnDiag accnDiag = new AccnDiag();
            accnDiag.setAccnId(accnId);
            accnDiag.setDiagCdId("V57.21");
            accnDiag.setDiagSeq(1);
            accnDiag.setDiagTypId(25);
            accnDiag.setUserDiagOrder(1);
            accnDiag.setAccnTestSeqId(accnTests.get(0).getAccnTestSeqId());
            rpmDao.setAccnDiag(testDb, accnDiag);
        }
    }

    private void createCons(Cons cons, String consAbbrev, String descr) throws Exception {
        if(cons == null) {
            cons = new Cons();
            cons.setConsId(rpmDao.getNextValueInSequence(testDb, DatabaseMap.SEQ_CONS_CONS_ID_SEQ));
            cons.setConsAbbrev(consAbbrev);
            cons.setDescr(descr);
            cons.setPrio(5);
            cons.setIsInUse(true);
            cons.setLev1MinProcMatch(2);
            rpmDao.setCons(testDb, cons);
        }
    }

    private void createAccnProc(String accnId, SubmFile submFile, ProcCd procCd) throws Exception {
        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(testDb, accnId);

        if(accnProcs.isEmpty() && submFile != null && procCd != null) {
            AccnProc accnProc = new AccnProc();
            accnProc.setProcId(procCd.getProcId());
            accnProc.setAccnProcSeqId(rpmDao.getNextValueInSequence(testDb, DatabaseMap.SEQ_ACCN_PROC_SEQ));
            accnProc.setAccnId(accnId);
            accnProc.setFacId(1);
            accnProc.setBillFacId(1);
            accnProc.setPrcPyrPrio(1);
            accnProc.setSubmDiagCd1("V57.21");
            accnProc.setSubmPyrPrio(1);
            accnProc.setStaId(21);
            accnProc.setExpPrc(75F);
            accnProc.setBilPrc(75F);
            accnProc.setGrossPrc(75F);
            accnProc.setDueAmt(75F);
            accnProc.setPrcOrder(1);
            accnProc.setSubmFileSeqId(submFile.getSubmFileSeqId());
            accnProc.setUnits(1);
            accnProc.setDueAmtWithBulk(75F);
            accnProc.setExpDueAmt(75F);
            accnProc.setExpDueAmtWithoutBulk(75F);
            accnProc.setPrcMthdTypId(56);
            rpmDao.setAccnProc(testDb, accnProc);
        }
    }

    private void createAccnTestCons(String accnId, List<AccnTest> accnTests, Cons cons, ProcCd procCd) throws Exception {
        List<AccnTestCons> accnTestConsList = rpmDao.getAccnTestCons(testDb, accnId, accnTests.get(0).getAccnTestSeqId(), cons.getConsId());

        if(accnTestConsList.isEmpty() && !accnTests.isEmpty() && cons != null && procCd != null) {
            AccnTestCons accnTestCons= new AccnTestCons();
            accnTestCons.setAccnId(accnId);
            accnTestCons.setAccnTestSeqId(accnTests.get(0).getAccnTestSeqId());
            accnTestCons.setConsId(cons.getConsId());
            accnTestCons.setProcId(procCd.getProcId());
            accnTestCons.setSeqId(rpmDao.getNextValueInSequence(testDb, DatabaseMap.SEQ_ACCN_TEST_CONS_SEQ));
            accessionDao.setAccnTestCons(accnTestCons);
        }
    }

    private void createQAccnSubm(String accnId, SubmFile submFile, SubmSvc submSvc, List<QAccnSubm> qAccnSubms) throws Exception {
        if(qAccnSubms.isEmpty() && submFile != null && submSvc != null) {
            QAccnSubm qAccnSubm = new QAccnSubm();
            qAccnSubm.setDocSeqId(databaseSequenceDao.getNextSeqIdFromTBLByColName(DatabaseMap.FLD_Q_ACCN_SUBM_DOC_SEQ_ID, DatabaseMap.TBL_Q_ACCN_SUBM));
            qAccnSubm.setPyrGrpId(1);
            qAccnSubm.setPyrPrio(1);
            qAccnSubm.setAccnId(accnId);
            qAccnSubm.setPyrId(149577);
            qAccnSubm.setSubmCnt(1);
            qAccnSubm.setSubmFileSeqId(submFile.getSubmFileSeqId());
            qAccnSubm.setSubmSvcSeqId(submSvc.getSubmSvcSeqId());
            qAccnSubm.setClaimIdSuffix("01");
            qAccnSubms.add(qAccnSubm);
            rpmDao.setQAccnSubm(testDb, qAccnSubm);
        }
    }

    private void createQAccnSubmStage(String accnId, SubmFile submFile, SubmSvc submSvc, List<QAccnSubm> qAccnSubms) throws Exception {
        List<QAccnSubmStage> qAccnSubmStages = rpmDao.getQAccnSubmStage(testDb, accnId);

        if(qAccnSubmStages.isEmpty() && submSvc != null && submFile != null && !qAccnSubms.isEmpty()) {
            QAccnSubmStage qAccnSubmStage = new QAccnSubmStage();
            qAccnSubmStage.setPyrGrpId(1);
            qAccnSubmStage.setPyrPrio(1);
            qAccnSubmStage.setDocSeqId(qAccnSubms.get(0).getDocSeqId());
            qAccnSubmStage.setAccnId(accnId);
            qAccnSubmStage.setPyrId(149577);
            qAccnSubmStage.setSubmCnt(1);
            qAccnSubmStage.setSubmFileSeqId(submFile.getSubmFileSeqId());
            qAccnSubmStage.setSubmSvcSeqId(submSvc.getSubmSvcSeqId());
            qAccnSubmStage.setClaimIdSuffix("01");
            rpmDao.setQAccnSubmStage(testDb, qAccnSubmStage);
        }
    }

    private boolean createPhys(Phys phys) throws Exception {
        boolean isExisted = true;

        if(phys == null) {
            isExisted = false;
            phys = new Phys();
            phys.setSeqId(rpmDao.getNextValueInSequence(testDb, DatabaseMap.SEQ_PHYS));
            phys.setPhysLname("KATZ");
            phys.setPhysFname("RICHARD");
            phys.setAddr1("5555 RESERVOIR DRIVE");
            phys.setAddr2("SUITE 112");
            phys.setCity("SAN DIEGO");
            phys.setStId("CA");
            phys.setZipId("921205195");
            phys.setNpiId(1548332539L);
            phys.setUpinId("A41737");
            rpmDao.setPhys(testDb, phys);
        }

        return isExisted;
    }

    private boolean createStmtConfig(int generalStmtTypId, String stmtData) throws Exception {
        boolean isExisted = true;
        StmtConfig stmtConfig = rpmDao.getStmtConfigByPyrIdAndStmtConfigAndGeneralTypeId(testDb, 149577, 240017, generalStmtTypId);
        if(stmtConfig == null) {
            isExisted = false;
            stmtConfig = new StmtConfig();
            stmtConfig.setSeqId(databaseSequenceDao.getNextSeqIdFromTBLByColName(DatabaseMap.FLD_STMT_CONFIG_SEQ_ID, DatabaseMap.TBL_STMT_CONFIG));
            stmtConfig.setPyrId(149577);
            stmtConfig.setGeneralStmtTypId(generalStmtTypId);
            stmtConfig.setStmtConfigElementTypId(240017);
            stmtConfig.setStmtData(stmtData);
            stmtConfig.setFacId(0);
            stmtConfig.setPyrGrpId(0);
            stmtConfig.setSubmSvcSeqId(0);
        } else {
            stmtConfig.setStmtData(stmtData);
        }

        rpmDao.setStmtConfig(testDb, stmtConfig);

        return isExisted;
    }

    private void prepareDataFor5010Stament(boolean isCreateXrefTyp, String accnId) throws Exception {
        Xref xref = null;
        Phys phys = null;
        SubmSvc submSvc = null;
        SubmFile submFile = null;
        PyrXref pyrXref = null;
        Cons cons = null;
        List<QAccnSubm> qAccnSubms = new ArrayList<QAccnSubm>();
        List<AccnTest> accnTests = new ArrayList<AccnTest>();
        ProcCd procCd = null;
        com.mbasys.mars.ejb.entity.test.Test test = null;

        if(isCreateXrefTyp) {

            createXrefTyp();
            try {
                xref = rpmDao.getXrefByDescr(testDb, "UHCSR");
            } catch (Exception e) {
                createXref(xref);
            }

            if(xref != null) {
                createPyrXref(xref, pyrXref);
            }

        }

        try {
            pyrXref = rpmDao.getPyrXrefByPyrIdAndXref(testDb, 149577, xref.getXrefId());
        } catch (Exception e) {
        }

        updateAccn(accnId, pyrXref, isCreateXrefTyp);
        createPyr();
        createAccnPyr(accnId);

        procCd = rpmDao.getProcCdByProcId(testDb, "Ver5010A1837");
        createProcCd(procCd, "Ver5010A1837", "AUTOMATTION TEST 5010A1837");

        test = rpmDao.getTestByTestAbbrev(testDb, "STD5010");
        createTest(test, "STD5010" , "STD5010");

        try {
            phys = rpmDao.getPhysByUpin(testDb, "A41737");
        } catch (Exception e) {
            createPhys(phys);
        }

        List<AccnPhys> accnPhys = rpmDao.getAccnPhysByAccnId(testDb, accnId);
        createAccnPhys(accnId, phys, accnPhys);

        try {
            cons = rpmDao.getConsByAbbrev(testDb, "STD5010");
        } catch (Exception e) {
            createCons(cons, "STD5010" , "STD5010");
        }

        test = rpmDao.getTestByTestAbbrev(testDb, "STD5010");
        procCd = rpmDao.getProcCdByProcId(testDb, "STDUB04");
        accnTests = accessionDao.getAccnTestsByAccnId(accnId);
        createAccnTest(accnId, procCd, test, accnTests);

        cons = rpmDao.getConsByAbbrev(testDb, "STD5010");
        createAccnTestCons(accnId, accnTests, cons, procCd);
        createAccnDiag(accnId, procCd, accnTests);

        submSvc = rpmDao.getSubmSvcByAbbrev(testDb, "STD5010");
        createSubmsvc(submSvc, 600, "STD5010", "Standard 5010 Statement");

        submSvc = rpmDao.getSubmSvcByAbbrev(testDb, "STD5010");
        createPyrSubmsvc(submSvc);

        try {
            submFile = rpmDao.getSubmFileByFilename(testDb, "STD5010Test.txt");
        } catch (Exception e) {
            createSubmFile(submSvc, submFile, 600, "STD5010Test.txt");
        }

        submFile = rpmDao.getSubmFileByFilename(testDb, "STD5010Test.txt");
        createAccnProc(accnId, submFile, procCd);
        qAccnSubms = rpmDao.getQAccnSubm(testDb, accnId);
        createQAccnSubm(accnId, submFile, submSvc, qAccnSubms);
        createQAccnSubmStage(accnId, submFile, submSvc, qAccnSubms);
    }

    private void prepareDataForCMS1500(String project, String testSuite, String testCase, String propLevel, String propName, String accnId ,boolean isCreateXrefTyp) throws Exception {
        Xref xref = null;
        Phys phys = null;
        SubmSvc submSvc = null;
        SubmFile submFile = null;
        PyrXref pyrXref = null;
        Cons cons = null;
        List<QAccnSubm> qAccnSubms = new ArrayList<QAccnSubm>();
        List<AccnTest> accnTests = new ArrayList<AccnTest>();
        ProcCd procCd = null;
        com.mbasys.mars.ejb.entity.test.Test test = null;

        if(isCreateXrefTyp) {

            createXrefTyp();
            try {
                xref = rpmDao.getXrefByDescr(testDb, "UHCSR");
            } catch (Exception e) {
                createXref(xref);
            }

            if(xref != null) {
                createPyrXref(xref, pyrXref);
            }
        }

        try {
            pyrXref = rpmDao.getPyrXrefByPyrIdAndXref(testDb, 149577, xref.getXrefId());
        } catch (Exception e) {
        }

        updateAccn(accnId, pyrXref, isCreateXrefTyp);

        createPyr();
        createAccnPyr(accnId);

        procCd = rpmDao.getProcCdByProcId(testDb, "CMS1500");
        createProcCd(procCd, "CMS1500", "AUTOMATTION TEST CMS1500");

        test = rpmDao.getTestByTestAbbrev(testDb, "CMS1500");
        createTest(test, "CMS1500", "CMS1500");

        try {
            phys = rpmDao.getPhysByUpin(testDb, "A41737");
        } catch (Exception e) {
            createPhys(phys);
        }

        List<AccnPhys> accnPhys = rpmDao.getAccnPhysByAccnId(testDb, accnId);
        createAccnPhys(accnId, phys, accnPhys);

        try {
            cons = rpmDao.getConsByAbbrev(testDb, "CMS1500");
        } catch (Exception e) {
            createCons(cons, "CMS1500", "CMS1500");
        }

        test = rpmDao.getTestByTestAbbrev(testDb, "CMS1500");
        procCd = rpmDao.getProcCdByProcId(testDb, "CMS1500");
        accnTests = accessionDao.getAccnTestsByAccnId(accnId);
        createAccnTest(accnId, procCd, test, accnTests);

        cons = rpmDao.getConsByAbbrev(testDb, "CMS1500");
        createAccnTestCons(accnId, accnTests, cons, procCd);
        createAccnDiag(accnId, procCd, accnTests);

        submSvc = rpmDao.getSubmSvcByAbbrev(testDb, "CMS1500");
        createSubmsvc(submSvc, 503, "CMS1500", "CMS1500 V0212");

        submSvc = rpmDao.getSubmSvcByAbbrev(testDb, "CMS1500");
        createPyrSubmsvc(submSvc);

        try {
            submFile = rpmDao.getSubmFileByFilename(testDb, "CMS1500Test.pdf");
        } catch (Exception e) {
            createSubmFile(submSvc, submFile, 503, "CMS1500Test.pdf");
        }

        submFile = rpmDao.getSubmFileByFilename(testDb, "CMS1500Test.pdf");
        createAccnProc(accnId, submFile, procCd);
        qAccnSubms = rpmDao.getQAccnSubm(testDb, accnId);
        createQAccnSubm(accnId, submFile, submSvc, qAccnSubms);
        createQAccnSubmStage(accnId, submFile, submSvc, qAccnSubms);
    }

    private void deleteCreateData(String accnId, String fileName, String submSvcAbbrev, String procCdId, String testAbbrev, String consAbbrev, boolean isStmtConfigExisted, int generalTypeId) throws Exception {
        StmtConfig stmtConfig = rpmDao.getStmtConfigByPyrIdAndStmtConfigAndGeneralTypeId(testDb, 149577, 240017, generalTypeId);
        if(!isStmtConfigExisted && stmtConfig != null) {
            stmtConfig.setResultCode(ErrorCodeMap.DELETED_RECORD);
            rpmDao.setStmtConfig(testDb, stmtConfig);
        }
        rpmDao.enableTriggerStmtConfig();

        Xref xref = rpmDao.getXrefByDescr(testDb, "UHCSR");
        if (xref != null) {
            try {
                PyrXref pyrXref = rpmDao.getPyrXrefByPyrIdAndXref(testDb, 149577, xref.getXrefId());
                if(pyrXref != null) {
                    pyrXref.setResultCode(ErrorCodeMap.DELETED_RECORD);
                    rpmDao.setPyrXref(testDb, pyrXref);
                }
            } catch (Exception e) {
            }
        }


        List<QAccnSubmStage> qAccnSubmStages = rpmDao.getQAccnSubmStage(testDb, accnId);
        for (QAccnSubmStage qAccnSubmStage : qAccnSubmStages) {
            qAccnSubmStage.setResultCode(ErrorCodeMap.DELETED_RECORD);
            rpmDao.setQAccnSubmStage(testDb, qAccnSubmStage);
        }

        SubmSvc submSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        if(submSvc != null) {
            try {
                PyrSubmSvc pyrSubmSvc = rpmDao.getPyrSubmSvc(testDb, 149577, 1, submSvc.getSubmSvcSeqId());
                if(pyrSubmSvc != null ) {
                    pyrSubmSvc.setResultCode(ErrorCodeMap.DELETED_RECORD);
                    rpmDao.setPyrSubmSvc(testDb, pyrSubmSvc);
                }
            } catch (Exception e) {

            }
        }

        SubmFile submFile = rpmDao.getSubmFileByFilename(testDb, fileName);
        if(submFile != null) {
            List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAuditByAccnIdAndSubmFileSeqId(testDb, accnId, submFile.getSubmFileSeqId());
            if (!submFileAudits.isEmpty())
            {
                submFileAudits.get(0).setResultCode(ErrorCodeMap.DELETED_RECORD);
                rpmDao.setSubmFileAudit(testDb, submFileAudits.get(0));
            }
        }

        List<QAccnSubm> qAccnSubms = rpmDao.getQAccnSubm(testDb, accnId);
        for (QAccnSubm qAccnSubm : qAccnSubms) {
            if(qAccnSubm != null) {
                SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(testDb, qAccnSubm.getDocSeqId());
                if(submClaimAudit != null) {
                    submClaimAudit.setResultCode(ErrorCodeMap.DELETED_RECORD);
                    rpmDao.setSubmClaimAudit(testDb, submClaimAudit);
                }
            }
        }

        accessionDao.deleteAccn(accnId);

        ProcCd procCd = rpmDao.getProcCdByProcId(testDb, procCdId);
        if(procCd != null) {
            procCd.setResultCode(ErrorCodeMap.DELETED_RECORD);
            rpmDao.setProCd(testDb, procCd);
        }

        try {
            Cons cons = rpmDao.getConsByAbbrev(testDb, consAbbrev);
            if(cons != null) {
                cons.setResultCode(ErrorCodeMap.DELETED_RECORD);
                rpmDao.setCons(testDb, cons);
            }
        } catch (Exception e) {

        }

        com.mbasys.mars.ejb.entity.test.Test test = rpmDao.getTestByTestAbbrev(testDb, testAbbrev);
        if(test != null) {
            test.setResultCode(ErrorCodeMap.DELETED_RECORD);
            rpmDao.setTest(testDb, test);
        }

        if(submFile != null) {
            submFile.setResultCode(ErrorCodeMap.DELETED_RECORD);
            rpmDao.setSubmFile(testDb, submFile);
        }

        if(submSvc != null) {
            submSvc.setResultCode(ErrorCodeMap.DELETED_RECORD);
            rpmDao.setSubmSvc(testDb, submSvc);
        }
    }

    @Test(priority = 1, description = "Generate 5010 for accn without orderPhysOverride xref ")
    @Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "engineSource", "runEngineBatFile", "timeWaitEngine"})
    public void testRPM_New_17487(String project, String testSuite, String testCase, String propLevel, String propName, String engineSource, String runEngineBatFile,int timeWaitEngine) throws Exception {
        engineUtils = new EngineUtils(driver);

        boolean isExisted = true;
        logger.info("*** Step 1 : prepare data.");
        rpmDao.disableTriggerStmtConfig();
        isExisted = createStmtConfig(5, "true");
        String accnId = createAccn(project, testSuite, testCase, propLevel, propName);

        prepareDataFor5010Stament(false, accnId);

        logger.info("*** Step 2 Action: Run PF-NONCLNSTMTENGINE.");
        engineUtils.runEngine(engineSource, runEngineBatFile, timeWaitEngine);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, "STD5010");
        Assert.assertTrue(submFileSeqId > 0);

        List<AccnPhys> accnPhys = rpmDao.getAccnPhysByAccnId(testDb, accnId);
        Phys phys = rpmDao.getPhys(testDb, accnPhys.get(0).getPhysSeqId());

        logger.info("Find New 5010 Statement File in the nonclientsubm/out dir");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        String submFileName = submFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        logger.info("Make sure in the file 5010 Statement are displayed");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = null;
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            data = data + line;
        }
        Assert.assertTrue(data.contains(phys.getPhysLname() + '*' + phys.getPhysFname()));
        Assert.assertTrue(data.contains(String.valueOf(phys.getNpiId())));
        deleteCreateData(accnId, "STD5010Test.txt", "STD5010", "Ver5010A1837", "STD5010", "STD5010", isExisted, 5);
        FileManipulation.forceDeleteFile(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "nonclientsubm" + File.separator + "out" + File.separator, submFileName);
    }

    @Test(priority = 1, description = "Generate 5010 for accn with orderPhysOverride xref not null")
    @Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "engineSource", "runEngineBatFile", "timeWaitEngine"})
    public void testRPM_New_17489(String project, String testSuite, String testCase, String propLevel, String propName, String engineSource, String runEngineBatFile,int timeWaitEngine) throws Exception {
        engineUtils = new EngineUtils(driver);
        boolean isExisted = true;

        logger.info("*** Step 1 : prepare data.");
        rpmDao.disableTriggerStmtConfig();
        isExisted = createStmtConfig(5, "true");
        String accnId = createAccn(project, testSuite, testCase, propLevel, propName);
        prepareDataFor5010Stament(true, accnId);

        logger.info("*** Step 2 Action: Run PF-NONCLNSTMTENGINE.");
        engineUtils.runEngine(engineSource, runEngineBatFile, timeWaitEngine);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, "STD5010");
        Assert.assertTrue(submFileSeqId > 0);

        List<AccnPhys> accnPhys = rpmDao.getAccnPhysByAccnId(testDb, accnId);
        Phys phys = rpmDao.getPhys(testDb, accnPhys.get(0).getPhysSeqId());
        Xref xref = rpmDao.getXrefByDescr(testDb, "UHCSR");

        logger.info("Find New 5010 Statement File in the nonclientsubm/out dir");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        String submFileName = submFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        logger.info("Verify File content, submFileName " + submFileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = null;
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            data = data + line;
        }
        Assert.assertTrue(data.contains(xref.getAbbrev()));
        Assert.assertTrue(data.contains(String.valueOf(phys.getNpiId())));
        deleteCreateData(accnId, "STD5010Test.txt", "STD5010", "Ver5010A1837", "STD5010", "STD5010", isExisted, 5);
        FileManipulation.forceDeleteFile(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "nonclientsubm" + File.separator + "out" + File.separator, submFileName);
    }

    @Test(priority = 1, description = "Generate 5010 for accn with orderPhysOverride xref not null")
    @Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "engineSource", "runEngineBatFile", "timeWaitEngine"})
    public void testRPM_New_17491(String project, String testSuite, String testCase, String propLevel, String propName, String engineSource, String runEngineBatFile,int timeWaitEngine) throws Exception {
        engineUtils = new EngineUtils(driver);
        boolean isExisted = true;

        logger.info("*** Step 1 : prepare data.");
        rpmDao.disableTriggerStmtConfig();
        isExisted = createStmtConfig(5, "false");
        String accnId = createAccn(project, testSuite, testCase, propLevel, propName);
        prepareDataFor5010Stament(true, accnId);

        logger.info("*** Step 2 Action: Run PF-NONCLNSTMTENGINE.");
        engineUtils.runEngine(engineSource, runEngineBatFile, timeWaitEngine);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, "STD5010");
        Assert.assertTrue(submFileSeqId > 0);

        List<AccnPhys> accnPhys = rpmDao.getAccnPhysByAccnId(testDb, accnId);
        Phys phys = rpmDao.getPhys(testDb, accnPhys.get(0).getPhysSeqId());
        Xref xref = rpmDao.getXrefByDescr(testDb, "UHCSR");

        logger.info("Find New 5010 Statement File in the nonclientsubm/out dir");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        String submFileName = submFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        logger.info("Verify File content, submFileName " + submFileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = null;
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            data = data + line;
        }
        Assert.assertTrue(data.contains(xref.getAbbrev()));
        Assert.assertTrue(data.contains(String.valueOf(phys.getNpiId())));
        deleteCreateData(accnId, "STD5010Test.txt", "STD5010", "Ver5010A1837", "STD5010", "STD5010", isExisted, 5);
        FileManipulation.forceDeleteFile(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "nonclientsubm" + File.separator + "out" + File.separator, submFileName);
    }

    @Test(priority = 1, description = "Generate CMS1500 for accn without orderPhysOverride xref")
    @Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "engineSource", "runEngineBatFile", "timeWaitEngine"})
    public void testRPM_New_17488(String project, String testSuite, String testCase, String propLevel, String propName, String engineSource, String runEngineBatFile,int timeWaitEngine) throws Exception {
        engineUtils = new EngineUtils(driver);
        // prepare data
        logger.info("*** Step 1 Action: create data");
        boolean isExisted = true;
        rpmDao.disableTriggerStmtConfig();
        isExisted = createStmtConfig(6, "true");
        String accnId = createAccn(project, testSuite, testCase, propLevel, propName);
        prepareDataForCMS1500(project, testSuite, testCase, propLevel, propName, accnId, true);

        logger.info("*** Step 2 Action: Run PF-NONCLNSTMTENGINE.");
        engineUtils.runEngine(engineSource, runEngineBatFile, timeWaitEngine);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, "CMS1500");
        Assert.assertTrue(submFileSeqId > 0);

        List<AccnPhys> accnPhys = rpmDao.getAccnPhysByAccnId(testDb, accnId);
        Phys phys = rpmDao.getPhys(testDb, accnPhys.get(0).getPhysSeqId());
        Xref xref = rpmDao.getXrefByDescr(testDb, "UHCSR");

        logger.info("Find New CMS1500 File in the nonclientsubm/out dir");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        String submFileName = submFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        logger.info("Verify File content, submFileName " + submFileName);
        String textStatement = convertPdfToText(file);

        Assert.assertTrue(textStatement.contains(xref.getAbbrev()));
        Assert.assertTrue(textStatement.contains(String.valueOf(phys.getNpiId())));

        deleteCreateData(accnId, "CMS1500Test.pdf", "CMS1500", "CMS1500", "CMS1500", "CMS1500", isExisted, 6);
        FileManipulation.forceDeleteFile(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "nonclientsubm" + File.separator + "out" + File.separator, submFileName);
    }

    @Test(priority = 1, description = "Generate CMS1500 for accn with orderPhysOverride xref not null")
    @Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "engineSource", "runEngineBatFile", "timeWaitEngine"})
    public void testRPM_New_17490(String project, String testSuite, String testCase, String propLevel, String propName, String engineSource, String runEngineBatFile,int timeWaitEngine) throws Exception {
        engineUtils = new EngineUtils(driver);

        logger.info("*** Step 1 Action: create data");
        boolean isExisted = true;
        rpmDao.disableTriggerStmtConfig();
        isExisted = createStmtConfig(6, "true");
        String accnId = createAccn(project, testSuite, testCase, propLevel, propName);
        prepareDataForCMS1500(project, testSuite, testCase, propLevel, propName, accnId, false);

        logger.info("*** Step 2 Action: Run PF-NONCLNSTMTENGINE.");
        engineUtils.runEngine(engineSource, runEngineBatFile, timeWaitEngine);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, "CMS1500");
        Assert.assertTrue(submFileSeqId > 0);

        List<AccnPhys> accnPhys = rpmDao.getAccnPhysByAccnId(testDb, accnId);
        Phys phys = rpmDao.getPhys(testDb, accnPhys.get(0).getPhysSeqId());

        logger.info("Find New CMS1500 in the nonclientsubm/out dir");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        String submFileName = submFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        logger.info("Verify File content, submFileName " + submFileName);
        String textStatement = convertPdfToText(file);

        Assert.assertTrue(textStatement.contains(phys.getPhysFname() + " " + phys.getPhysLname()));
        Assert.assertTrue(textStatement.contains(String.valueOf(phys.getNpiId())));

        deleteCreateData(accnId, "CMS1500Test.pdf", "CMS1500", "CMS1500", "CMS1500", "CMS1500", isExisted, 6);
        FileManipulation.forceDeleteFile(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator, submFileName);
    }

    @Test(priority = 1, description = "Generate CMS1500 for accn with orderPhysOverride xref not null")
    @Parameters({"project", "testSuite", "testCase", "propLevel", "propName", "engineSource", "runEngineBatFile", "timeWaitEngine"})
    public void testRPM_New_17492(String project, String testSuite, String testCase, String propLevel, String propName, String engineSource, String runEngineBatFile,int timeWaitEngine) throws Exception {
        engineUtils = new EngineUtils(driver);

        logger.info("*** Step 1 Action: create data");
        boolean isExisted = true;
        rpmDao.disableTriggerStmtConfig();
        isExisted = createStmtConfig(6, "false");
        String accnId = createAccn(project, testSuite, testCase, propLevel, propName);
        prepareDataForCMS1500(project, testSuite, testCase, propLevel, propName, accnId, false);

        logger.info("*** Step 2 Action: Run PF-NONCLNSTMTENGINE.");
        engineUtils.runEngine(engineSource, runEngineBatFile, timeWaitEngine);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, "CMS1500");
        Assert.assertTrue(submFileSeqId > 0);

        List<AccnPhys> accnPhys = rpmDao.getAccnPhysByAccnId(testDb, accnId);
        Phys phys = rpmDao.getPhys(testDb, accnPhys.get(0).getPhysSeqId());
        Xref xref = rpmDao.getXrefByDescr(testDb, "UHCSR");

        logger.info("Find New CMS1500 in the nonclientsubm/out dir");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        String submFileName = submFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        logger.info("Verify File content, submFileName " + submFileName);
        String textStatement = convertPdfToText(file);

        Assert.assertTrue(textStatement.contains(xref.getAbbrev()));
        Assert.assertTrue(textStatement.contains(String.valueOf(phys.getNpiId())));

        deleteCreateData(accnId, "CMS1500Test.pdf", "CMS1500", "CMS1500", "CMS1500", "CMS1500", isExisted, 6);
        FileManipulation.forceDeleteFile(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator, submFileName);
    }
}
