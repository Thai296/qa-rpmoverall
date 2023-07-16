package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.eobClaim.EobClaim;
import com.mbasys.mars.ejb.entity.eobClaimAdj.EobClaimAdj;
import com.mbasys.mars.ejb.entity.eobClaimRenderingProvId.EobClaimRenderingProvId;
import com.mbasys.mars.ejb.entity.eobClaimSupplementalAmt.EobClaimSupplementalAmt;
import com.mbasys.mars.ejb.entity.eobClaimSupplementalQty.EobClaimSupplementalQty;
import com.mbasys.mars.ejb.entity.eobFinancialTrans.EobFinancialTrans;
import com.mbasys.mars.ejb.entity.eobOtherClaimIdentInfo.EobOtherClaimIdentInfo;
import com.mbasys.mars.ejb.entity.eobPayee.EobPayee;
import com.mbasys.mars.ejb.entity.eobPhi.EobPhi;
import com.mbasys.mars.ejb.entity.eobProvSummary.EobProvSummary;
import com.mbasys.mars.ejb.entity.eobPyr.EobPyr;
import com.mbasys.mars.ejb.entity.eobPyrContact.EobPyrContact;
import com.mbasys.mars.ejb.entity.eobSvcAdj.EobSvcAdj;
import com.mbasys.mars.ejb.entity.eobSvcId.EobSvcId;
import com.mbasys.mars.ejb.entity.eobSvcPmt.EobSvcPmt;
import com.mbasys.mars.ejb.entity.eobSvcRemarkCd.EobSvcRemarkCd;
import com.mbasys.mars.ejb.entity.eobSvcRenderingProvId.EobSvcRenderingProvId;
import com.mbasys.mars.ejb.entity.eobSvcSupplementalAmt.EobSvcSupplementalAmt;
import com.mbasys.mars.ejb.entity.eobTechContactInformation.EobTechContactInformation;
import com.mbasys.mars.ejb.entity.x12Interchange.X12Interchange;
import com.mbasys.mars.ejb.entity.x12Sender.X12Sender;
import com.mbasys.mars.ejb.entity.x12TransSet.X12TransSet;
import com.overall.utils.HL7ParsingEngineUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import domain.engines.x12interface.EobClaimAdjRecord;
import domain.engines.x12interface.EobClaimRecord;
import domain.engines.x12interface.EobClaimRenderingProvIdRecord;
import domain.engines.x12interface.EobClaimSupplementalAmtRecord;
import domain.engines.x12interface.EobClaimSupplementalQtyRecord;
import domain.engines.x12interface.EobFinancialTransRecord;
import domain.engines.x12interface.EobOtherClaimIdentInfoRecord;
import domain.engines.x12interface.EobPayeeRecord;
import domain.engines.x12interface.EobPhiRecord;
import domain.engines.x12interface.EobProvSummaryRecord;
import domain.engines.x12interface.EobPyrContactRecord;
import domain.engines.x12interface.EobPyrRecord;
import domain.engines.x12interface.EobSvcAdjRecord;
import domain.engines.x12interface.EobSvcIdRecord;
import domain.engines.x12interface.EobSvcPmtRecord;
import domain.engines.x12interface.EobSvcRemarkCdRecord;
import domain.engines.x12interface.EobSvcRenderingProvIdRecord;
import domain.engines.x12interface.EobSvcSupplementalAmtRecord;
import domain.engines.x12interface.EobTechContactInformationRecord;
import domain.engines.x12interface.X12InterchangeRecord;
import domain.engines.x12interface.X12TransSetRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;




public class X12InterfaceEngineTest extends SeleniumBaseTest {
	private RandomCharacter randomCharacter;
	private TimeStamp timeStamp;
	private FileManipulation fileManipulation;
	private XifinAdminUtils xifinAdminUtils;	
	private HL7ParsingEngineUtils hl7ParsingEngineUtils;
    private File upOne = new File(System.getProperty("user.dir")).getParentFile();
    private static final SimpleDateFormat DATE_FORMAT_MMDDYYYY_WITH_SLASH = new SimpleDateFormat("MM/dd/yyyy");
    protected static long QUEUE_POLL_TIME_MS = TimeUnit.SECONDS.toMillis(5);
    protected static long QUEUE_WAIT_TIME_MS = TimeUnit.MINUTES.toMillis(10);
    protected static long QUEUE_ENGINE_WAIT_TIME = TimeUnit.SECONDS.toMillis(120);

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            setUpTestCondition();
        }
        catch (Exception e)
        {
            Assert.fail("Error running BeforeSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running AfterSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            setUpTestCondition();
        }
        catch (Exception e)
        {
            Assert.fail("Error running AfterSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }


    @Test(priority = 1, description="Verify 835 file with all loops and segments are processed and saved properly")
    @Parameters({"formatType", "file"})
    public void testPFER_608(String formatType, String file) throws Exception {
        logger.info("====== Testing - testPFER_608 ======");

        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);

        String content = FileUtils.readFileToString(new File(upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file));
        //File name
        String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "X12-835-" + currDtTime +".txt";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator + "ngs" + File.separator + "835" + File.separator;
        logger.info("Generate X12 835 file with all loops and segments and place the file to "+filePathIncoming);

        fileManipulation.writeFileToFolder(content, filePathIncoming, fileName);

        logger.info("Verify that the new X12 835 file is in the /incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        Assert.assertTrue(isFileExists(fIncoming, 5), "X12 835 file: " + fileName + " should be under " + filePathIncoming + " folder.");

        Assert.assertTrue(waitForX12EngineToCreateX12Interchange(fileName, QUEUE_WAIT_TIME_MS*2));

        logger.info("Verify that the X12 835 file is processed and moved to the /processed folder");
        File fProcessed = new File(filePathProcessed + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "X12 835 file: " + fileName + " should be processed and moved to " + filePathProcessed + " folder.");

        logger.info("Verify that data are saved to the corresponding tables properly");
        Date currentDt = DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(timeStamp.getCurrentDate());
        //X12_INTERCHANGE
        X12Interchange x12Interchange = rpmDao.getX12InterchangeByFileName(testDb, fileName);
        int x12InterchangeId = x12Interchange.getSeqId();
        //X12_TRANS_SET
        X12TransSet x12TransSet = rpmDao.getX12TransSetByX12InterchangeId(testDb, x12InterchangeId);
        int x12TransSetId = x12TransSet.getSeqId();
        //EOB_FINANCIAL_TRANS
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTransByX12TransSetId(testDb, x12TransSetId);
        int eobFinancialTransId = eobFinancialTrans.getSeqId();
        //EOB_CLAIM
        List<EobClaim> eobClaims = rpmDao.getEobClaimsByEobFinancialTransId(eobFinancialTransId);
        int eobClaimSeqId = eobClaims.get(0).getSeqId();
        //EOB_PROV_SUMMARY
        EobProvSummary eobProvSummary = rpmDao.getEobProvSummaryByEobFinanTransId(testDb, eobFinancialTransId);
        int eobProvSummarySeqId = eobProvSummary.getSeqId();
        //EOB_CLAIM_ADJ
        EobClaimAdj eobClaimAdj = rpmDao.getEobClaimAdjByEobClaimId(testDb, eobClaimSeqId);
        //EOB_SVC_PMT
        EobSvcPmt eobSvcPmt = rpmDao.getEobSvcPmtByEobClaimIdSubmittedCharge(testDb, eobClaimSeqId, "80051");
        int eobSvcPmtSeqId = eobSvcPmt.getSeqId();
        //EOB_SVC_ADJ
        List<EobSvcAdj> eobSvcAdjTableList = rpmDao.getEobSvcAdjByEobSvcPmtId(testDb, eobSvcPmtSeqId);
        //EOB_SVC_ID
        List<EobSvcId> eobSvcIdList = rpmDao.getEobSvcIdByEobSvcPmtSeqId(testDb, eobSvcPmtSeqId);
        //EOB_CLAIM_SUPPLEMENTAL_AMT
        EobClaimSupplementalAmt eobClaimSupplementalAmt = rpmDao.getEobClaimSupplementalAmtByEobClaimId(testDb, eobClaimSeqId);
        //EOB_SVC_REMARK_CD
        List<EobSvcRemarkCd> eobSvcRemarkCdList = rpmDao.getEobSvcRemarkCdByEobSvcPmtId(testDb, eobSvcPmtSeqId);
        //EOB_PYR
        EobPyr eobPyr = rpmDao.getEobPyrByEobFinanTransId(testDb, eobFinancialTransId);
        int eobPyrSeqId = eobPyr.getSeqId();
        //EOB_PYR_CONTACT
        List<EobPyrContact> eobPyrContactList =  rpmDao.getEobPyrContactByEobPyrSeqId(testDb, eobPyrSeqId);
        //EOB_TECH_CONTACT_INFORMATION
        List<EobTechContactInformation> eobTechContactInformationList = rpmDao.getEobTechContactInformationByContactName(testDb, "EDI HELPDESK");
        //EOB_PAYEE
        List<EobPayee> eobPayeeList = rpmDao.getEobPayeeByEobFinancialTransId(testDb, eobFinancialTransId);
        //EOB_PHI
        EobPhi eobPhi = rpmDao.getEobPhi(testDb, eobClaimSeqId);
        //EOB_OTHER_CLAIM_IDENT_INFO
        EobOtherClaimIdentInfo eobOtherClaimIdentInfo = rpmDao.getEobOtherClaimIdentInfoByEobClaimId(testDb, eobClaimSeqId);
        //EOB_CLAIM_RENDERING_PROV_ID
        EobClaimRenderingProvId eobClaimRenderingProvId = rpmDao.getEobClaimRenderingProvIdByEobClaimSeqId(testDb, eobClaimSeqId);
        //EOB_SVC_SUPPLEMENTAL_AMT
        EobSvcSupplementalAmt eobSvcSupplementalAmt = rpmDao.getEobSvcSupplementalAmtByEobSvcPmtId(testDb, eobSvcPmtSeqId);
        //EOB_CLAIM_SUPPLEMENTAL_QTY
        EobClaimSupplementalQty eobClaimSupplementalQty = rpmDao.getEobClaimSupplementalQtyByEobClaimId(testDb, eobClaimSeqId);
        //EOB_SVC_RENDERING_PROV_ID
        EobSvcRenderingProvId eobSvcRenderingProvId = rpmDao.getEobSvcRenderingProvIdByEobSvcPmtSeqId(testDb, eobSvcPmtSeqId);

        logger.info("Verify that data is saved to the X12_INTERCHANGE table properly");
        X12Sender x12Sender = rpmDao.getX12SenderByNameExternalId(testDb, "NGS", "04412");
        int x12SenderId = x12Sender.getSeqId();
        X12InterchangeRecord expectedX12InterchangeRecord = setValuesInX12InterchangeRecord(x12InterchangeId, fileName, 1, "722000095", "170808", currentDt, x12SenderId, 2359);
        X12InterchangeRecord actualX12InterchangeRecord = getValuesInX12InterchangeTable(x12Interchange);
        Assert.assertEquals(actualX12InterchangeRecord, expectedX12InterchangeRecord);

        logger.info("*** Expected Results: - Verify that data are saved to the X12_TRANS_SET table properly");
        X12TransSetRecord expectedX12TransSetRecord = setValuesInX12TransSetRecord(1, 1, "000000023", currentDt, "87");
        X12TransSetRecord actualX12TransSetRecord = getValuesInX12TransSetTable(x12TransSet);
        Assert.assertEquals(actualX12TransSetRecord, expectedX12TransSetRecord);

        logger.info("*** Expected Results: - Verify that data are saved to the EOB_FINANCIAL_TRANS table properly");
        EobFinancialTransRecord expectedEobFinancialTransRecord = setValuesInEobFinancialTransRecord("H", "0", "NON", "20170809", false, "C", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "463051216AFA5", "E14645", "1964772447", "20170808", 4, 0, 0);
        EobFinancialTransRecord actualEobFinancialTransRecord = getValuesInEobFinancialTransTable(eobFinancialTrans);
        Assert.assertEquals(actualEobFinancialTransRecord, expectedEobFinancialTransRecord);

        logger.info("*** Expected Results: - Verify that data are saved to the EOB_CLAIM table properly");
        List eobClaimList = Arrays.asList("11223344556699", "1", "211399.99", "138099.9", "0", "12", "A2011060211484718", "3", eobProvSummarySeqId, StringUtils.EMPTY, "NCFLUWKIVB", "XX", "Centers for Medicare and Medicaid Services", "ACME INSURANCE", "XV", "Centers for Medicare and Medicaid Services PlanID", "20180715", StringUtils.EMPTY, "20180713", StringUtils.EMPTY, StringUtils.EMPTY, "8005551212", "ACME INSURANCE", "XV", "Centers for Medicare and Medicaid Services PlanID", false, StringUtils.EMPTY, StringUtils.EMPTY, 0, "A2011060211484718", false, StringUtils.EMPTY, false, 0, 0, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "20180714");
        EobClaimRecord expectedEobClaimRecord = setValuesInEobClaimRecord(eobClaimList);
        EobClaimRecord actualEobClaimRecord = getValuesInEobClaimTable(eobClaims.get(0));
        Assert.assertEquals(actualEobClaimRecord, expectedEobClaimRecord);

        logger.info("*** Expected Results: - Verify that data are saved to the EOB_CLAIM_ADJ table properly");
        List eobClaimAdjList = Arrays.asList("CO", "131", "550", StringUtils.EMPTY);
        EobClaimAdjRecord expectedEobClaimAdjRecord = setValuesInEobClaimAdjRecord(eobClaimAdjList);
        EobClaimAdjRecord actualEobClaimAdjRecord = getValuesInEobClaimAdjTable(eobClaimAdj);
        Assert.assertEquals(actualEobClaimAdjRecord, expectedEobClaimAdjRecord);

        logger.info("Verify that data are saved to the EOB_SVC_PMT table properly");
        List eobSvcPmtList = Arrays.asList("HC", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "80051", "100", "80",  StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,  StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "20180716", StringUtils.EMPTY, StringUtils.EMPTY, "A78910", StringUtils.EMPTY, 0);
        EobSvcPmtRecord expectedEobSvcPmtRecord = setValuesInEobSvcPmtRecord(eobSvcPmtList);
        EobSvcPmtRecord actualEobSvcPmtRecord = getValuesInEobSvcPmtTable(eobSvcPmt);
        Assert.assertEquals(actualEobSvcPmtRecord, expectedEobSvcPmtRecord);

        logger.info("Verify that data are saved to the EOB_SVC_ADJ table properly");
        List<List> eobSvcAdjList = new ArrayList<>();
        List list = Arrays.asList("PR", "1", "793", StringUtils.EMPTY);
        eobSvcAdjList.add(list);
        list = Arrays.asList("PR", "3", "25", StringUtils.EMPTY);
        eobSvcAdjList.add(list);
        list = Arrays.asList("CO", "131", "250", StringUtils.EMPTY);
        eobSvcAdjList.add(list);
        List<EobSvcAdjRecord> expectedEobSvcAdjRecord = setValuesInEobSvcAdjRecord(eobSvcAdjList);
        List<EobSvcAdjRecord> actualEobSvcAdjRecord = getValuesInEobSvcAdjTable(eobSvcAdjTableList);
        assertEquals(actualEobSvcAdjRecord,expectedEobSvcAdjRecord);

        logger.info("Verify that data are saved to the EOB_SVC_ID table properly");
        List<List> eobSvcIdListList = new ArrayList<>();
        list = Arrays.asList(8, "100");
        eobSvcIdListList.add(list);
        list = Arrays.asList(9, "L12345678910");
        eobSvcIdListList.add(list);
        List<EobSvcIdRecord> expectedEobSvcIdRecord = setValuesInEobSvcIdRecord(eobSvcIdListList);
        List<EobSvcIdRecord> actualEobSvcIdRecord = getValuesInEobSvcIdTable(eobSvcIdList);
        assertEquals(actualEobSvcIdRecord, expectedEobSvcIdRecord);

        logger.info("Verify that data are saved to the EOB_CLAIM_SUPPLEMENTAL_AMT table properly");
        List eobClaimSupplementalAmtList = Arrays.asList("ZO", "49");
        EobClaimSupplementalAmtRecord expectedEobClaimSupplementalAmtRecord = setValuesInEobClaimSupplementalAmtRecord(eobClaimSupplementalAmtList);
        EobClaimSupplementalAmtRecord actualEobClaimSupplementalAmtRecord = getValuesInEobClaimSupplementalAmtTable(eobClaimSupplementalAmt);
        Assert.assertEquals(actualEobClaimSupplementalAmtRecord, expectedEobClaimSupplementalAmtRecord);

        logger.info("Verify that data are saved to the EOB_SVC_REMARK_CD table properly");
        List eobSvcRemarkCdValueList = Arrays.asList("12345");
        List<EobSvcRemarkCdRecord> expectedEobSvcRemarkCdRecord = setValuesInEobSvcRemarkCdRecord(eobSvcRemarkCdValueList);
        List<EobSvcRemarkCdRecord> actualEobSvcRemarkCdRecord = getValuesInEobSvcRemarkCdTable(eobSvcRemarkCdList);
        assertEquals(actualEobSvcRemarkCdRecord, expectedEobSvcRemarkCdRecord);

        logger.info("Verify that data are saved to the EOB_PYR_CONTACT table properly");
        List<EobPyrContactRecord> expectedEobPyrContactRecord = setValuesInEobPyrContactRecord(eobTechContactInformationList);
        List<EobPyrContactRecord> actualEobPyrContactRecord = getValuesInEobPyrContactTable(eobPyrContactList);
        assertEquals(actualEobPyrContactRecord, expectedEobPyrContactRecord);

        logger.info("Verify that data are saved to the EOB_PYR table properly");
        List eobPyrList = Arrays.asList("NOVITAS SOLUTIONS, INC.", "1900750689", "PO BOX 3110", StringUtils.EMPTY, "MECHANICSBURG", "PA", "170551826", "04412", "JOHN WAYNE", "8582528782", StringUtils.EMPTY, StringUtils.EMPTY, "2U", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "1900750689");
        EobPyrRecord expectedEobPyrRecord = setValuesInEobPyrRecord(eobPyrList);
        EobPyrRecord actualEobPyrRecord = getValuesInEobPyrTable(eobPyr);
        Assert.assertEquals(actualEobPyrRecord, expectedEobPyrRecord);

        logger.info("Verify that data are saved to the EOB_TECH_CONTACT_INFORMATION table properly");
        List<List> eobTechContactInformationValueList = new ArrayList<>();
        list = Arrays.asList(3, "8582528782");
        eobTechContactInformationValueList.add(list);
        list = Arrays.asList(4, "1234");
        eobTechContactInformationValueList.add(list);
        list = Arrays.asList(1, "WEBSITEEDI@EDI-HELPDESK.COM");
        eobTechContactInformationValueList.add(list);
        List<EobTechContactInformationRecord> expectedEobTechContactInformationRecord = setValuesInEobTechContactInformationRecord(eobTechContactInformationValueList);
        List<EobTechContactInformationRecord> actualEobTechContactInformationRecord = getValuesInEobTechContactInformationTable(eobTechContactInformationList);
        Assert.assertEquals(actualEobTechContactInformationRecord, expectedEobTechContactInformationRecord);

        logger.info("Verify that data are saved to the EOB_PAYEE table properly");
        List eobPayeeValueList = Arrays.asList("AMARILLO PATHOLOGY GROUP LLP", "1538105366", "P O BOX 51525", StringUtils.EMPTY, "AMARILLO", "TX", "791591525", "TJ", "752656007", "XX", 2, "TXKIHEFDTD", "5637661");
        List<EobPayeeRecord> expectedEobPayeeRecord = setValuesInEobPayeeRecord(eobPayeeValueList);
        List<EobPayeeRecord> actualEobPayeeRecord = getValuesInEobPayeeTable(eobPayeeList);
        Assert.assertEquals(actualEobPayeeRecord, expectedEobPayeeRecord);

        logger.info("Verify that data are saved to the EOB_PROV_SUMMARY table properly");
        List eobProvSummaryValueList = Arrays.asList("3", "20180716", "12.2", "1239.9", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "3123584", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);

        EobProvSummaryRecord expectedEobProvSummaryRecord = setValuesInEobProvSummaryRecord(eobProvSummaryValueList);
        EobProvSummaryRecord actualEobProvSummaryRecord = getValuesInEobProvSummaryTable(eobProvSummary);
        Assert.assertEquals(actualEobProvSummaryRecord, expectedEobProvSummaryRecord);

        logger.info("Verify that data are saved to the EOB_PHI table properly");
        List eobPhiValueList = Arrays.asList("HARRIET", "RUBIN", "M", "MR", "Medicaid Recipient Identification Number", "JESSICA", "SHEPARD", StringUtils.EMPTY, "II", "Standard Unique Health Identifier for each Individual", "I3X9XNY0O2", "NCFLUWKIVB", "NHOU9DNFAW", "Insured Changed Unique Identification Number", "NCFLUWKIVB", "I3X9XNY0O2", "NHOU9DNFAW", "Member Identification Number", "MI");
        EobPhiRecord expectedEobPhiRecord = setValuesInEobPhiRecord(eobPhiValueList);
        EobPhiRecord actualEobPhiRecord = getValuesInEobPhiTable(eobPhi);
        Assert.assertEquals(actualEobPhiRecord, expectedEobPhiRecord);

        logger.info("Verify that data are saved to the EOB_OTHER_CLAIM_IDENT_INFO table properly");
        List eobOtherClaimIdentInfoValueList = Arrays.asList("1L", "Group or Policy Number");
        EobOtherClaimIdentInfoRecord expectedEobOtherClaimIdentInfoRecord = setValuesInEobOtherClaimIdentInfoRecord(eobOtherClaimIdentInfoValueList);
        EobOtherClaimIdentInfoRecord actualEobOtherClaimIdentInfoRecord = getValuesInEobOtherClaimIdentInfoTable(eobOtherClaimIdentInfo);
        Assert.assertEquals(actualEobOtherClaimIdentInfoRecord, expectedEobOtherClaimIdentInfoRecord);

        logger.info("Verify that data are saved to the EOB_CLAIM_RENDERING_PROV_ID table properly");
        List eobClaimRenderingProvIdValueList = Arrays.asList(1, "Medical Record Identification Number");
        EobClaimRenderingProvIdRecord expectedEobClaimRenderingProvIdRecord = setValuesInEobClaimRenderingProvIdRecord(eobClaimRenderingProvIdValueList);
        EobClaimRenderingProvIdRecord actualEobClaimRenderingProvIdRecord = getValuesInEobClaimRenderingProvIdTable(eobClaimRenderingProvId);
        Assert.assertEquals(actualEobClaimRenderingProvIdRecord, expectedEobClaimRenderingProvIdRecord);

        logger.info("Verify that data are saved to the EOB_CLAIM_SUPPLEMENTAL_QTY table properly");
        List eobClaimSupplementalQtyValueList = Arrays.asList("ZK", "333222");
        EobClaimSupplementalQtyRecord expectedEobClaimSupplementalQtyRecord = setValuesInEobClaimSupplementalQtyRecord(eobClaimSupplementalQtyValueList);
        EobClaimSupplementalQtyRecord actualEobClaimSupplementalQtyRecord = getValuesInEobClaimSupplementalQtyTable(eobClaimSupplementalQty);
        Assert.assertEquals(actualEobClaimSupplementalQtyRecord, expectedEobClaimSupplementalQtyRecord);

        logger.info("Verify that data are saved to the EOB_SVC_SUPPLEMENTAL_AMT table properly");
        List eobSvcSupplementalAmtValueList = Arrays.asList("B6", "425");
        EobSvcSupplementalAmtRecord expectedEobSvcSupplementalAmtRecord = setValuesInEobSvcSupplementalAmtRecord(eobSvcSupplementalAmtValueList);
        EobSvcSupplementalAmtRecord actualEobSvcSupplementalAmtRecord = getValuesInEobSvcSupplementalAmtTable(eobSvcSupplementalAmt);
        Assert.assertEquals(actualEobSvcSupplementalAmtRecord, expectedEobSvcSupplementalAmtRecord);

        logger.info("Verify that data are saved to the EOB_SVC_RENDERING_PROV_ID table properly");
        List eobSvcRenderingProvIdValueList = Arrays.asList(12, "1234567891");
        EobSvcRenderingProvIdRecord expectedEobSvcRenderingProvIdRecord = setValuesInEobSvcRenderingProvIdRecord(eobSvcRenderingProvIdValueList);
        EobSvcRenderingProvIdRecord actualEobSvcRenderingProvIdRecord = getValuesInEobSvcRenderingProvIdTable(eobSvcRenderingProvId);
        Assert.assertEquals(actualEobSvcRenderingProvIdRecord, expectedEobSvcRenderingProvIdRecord);

    }

    @Test(priority = 1, description="Verify 4010 835 file with all loops and segments are processed properly")
    @Parameters({"formatType","file"})
    public void testPFER_636(String formatType, String file) throws Exception {
        logger.info("====== Testing - testPFER_636 ======");

        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);
        hl7ParsingEngineUtils = new HL7ParsingEngineUtils(driver, config);
        setUpTestCondition();

        String content = FileUtils.readFileToString(new File(upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file));
        //File name
        String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "X12-835-4010-" + currDtTime +".txt";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator + "ngs" + File.separator + "835" + File.separator;
        logger.info("Generate X12 835 file with all loops and segments and place the file to "+filePathIncoming);

        fileManipulation.writeFileToFolder(content, filePathIncoming, fileName);

        logger.info("Verify that the new X12 835 4010 file is in the /incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        Assert.assertTrue(isFileExists(fIncoming, 5), "X12 835 4010 file: " + fileName + " should be under " + filePathIncoming + " folder.");

        logger.info("Wait for PF-X12 Interface Engine");
        Assert.assertTrue(waitForX12EngineToCreateX12Interchange(fileName, QUEUE_WAIT_TIME_MS*2));

        logger.info("Verify that the X12 835 4010 file is processed and moved to the /processed folder");
        File fProcessed = new File(filePathProcessed + fileName);
        Assert.assertTrue(isFileExists(fProcessed, 5), "X12 835 4010 file: " + fileName + " should be processed and moved to " + filePathProcessed + " folder.");

        logger.info("Verify that data are saved to the corresponding tables properly");
        Date currentDt = DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(timeStamp.getCurrentDate());
        //X12_INTERCHANGE
        X12Interchange x12Interchange = rpmDao.getX12InterchangeByFileName(testDb, fileName);
        int x12InterchangeId = x12Interchange.getSeqId();
        //X12_TRANS_SET
        X12TransSet x12TransSet = rpmDao.getX12TransSetByX12InterchangeId(testDb, x12InterchangeId);
        int x12TransSetId = x12TransSet.getSeqId();
        //EOB_FINANCIAL_TRANS
        EobFinancialTrans eobFinancialTrans = rpmDao.getEobFinancialTransByX12TransSetId(testDb, x12TransSetId);
        int eobFinancialTransId = eobFinancialTrans.getSeqId();
        //EOB_CLAIM
        List<EobClaim> eobClaims = rpmDao.getEobClaimsByEobFinancialTransId(eobFinancialTransId);
        int eobClaimSeqId = eobClaims.get(0).getSeqId();
        //EOB_PROV_SUMMARY
        EobProvSummary eobProvSummary = rpmDao.getEobProvSummaryByEobFinanTransId(testDb, eobFinancialTransId);
        int eobProvSummarySeqId = eobProvSummary.getSeqId();
        //EOB_CLAIM_ADJ
        EobClaimAdj eobClaimAdj = rpmDao.getEobClaimAdjByEobClaimId(testDb, eobClaimSeqId);
        //EOB_SVC_PMT
        EobSvcPmt eobSvcPmt = rpmDao.getEobSvcPmtByEobClaimIdSubmittedCharge(testDb, eobClaimSeqId, "80051");
        int eobSvcPmtSeqId = eobSvcPmt.getSeqId();
        //EOB_SVC_ADJ
        List<EobSvcAdj> eobSvcAdjTableList = rpmDao.getEobSvcAdjByEobSvcPmtId(testDb, eobSvcPmtSeqId);
        //EOB_SVC_ID
        List<EobSvcId> eobSvcIdList = rpmDao.getEobSvcIdByEobSvcPmtSeqId(testDb, eobSvcPmtSeqId);
        //EOB_CLAIM_SUPPLEMENTAL_AMT
        EobClaimSupplementalAmt eobClaimSupplementalAmt = rpmDao.getEobClaimSupplementalAmtByEobClaimId(testDb, eobClaimSeqId);
        //EOB_SVC_REMARK_CD
        List<EobSvcRemarkCd> eobSvcRemarkCdList = rpmDao.getEobSvcRemarkCdByEobSvcPmtId(testDb, eobSvcPmtSeqId);
        //EOB_PYR
        EobPyr eobPyr = rpmDao.getEobPyrByEobFinanTransId(testDb, eobFinancialTransId);
        int eobPyrSeqId = eobPyr.getSeqId();
        //EOB_PYR_CONTACT
        List<EobPyrContact> eobPyrContactList =  rpmDao.getEobPyrContactByEobPyrSeqId(testDb, eobPyrSeqId);
        //EOB_TECH_CONTACT_INFORMATION
        List<EobTechContactInformation> eobTechContactInformationList = rpmDao.getEobTechContactInformationByContactName(testDb, "EDI HELPDESK");
        //EOB_PAYEE
        List<EobPayee> eobPayeeList = rpmDao.getEobPayeeByEobFinancialTransId(testDb, eobFinancialTransId);
        //EOB_PHI
        EobPhi eobPhi = rpmDao.getEobPhi(testDb, eobClaimSeqId);
        //EOB_OTHER_CLAIM_IDENT_INFO
        EobOtherClaimIdentInfo eobOtherClaimIdentInfo = rpmDao.getEobOtherClaimIdentInfoByEobClaimId(testDb, eobClaimSeqId);
        //EOB_SVC_SUPPLEMENTAL_AMT
        EobSvcSupplementalAmt eobSvcSupplementalAmt = rpmDao.getEobSvcSupplementalAmtByEobSvcPmtId(testDb, eobSvcPmtSeqId);
        //EOB_CLAIM_SUPPLEMENTAL_QTY
        EobClaimSupplementalQty eobClaimSupplementalQty = rpmDao.getEobClaimSupplementalQtyByEobClaimId(testDb, eobClaimSeqId);

        logger.info("Verify that data are saved to the X12_INTERCHANGE table properly");
        X12Sender x12Sender = rpmDao.getX12SenderByNameExternalId(testDb, "NGS", "04412");
        int x12SenderId = x12Sender.getSeqId();
        X12InterchangeRecord expectedX12InterchangeRecord = setValuesInX12InterchangeRecord(x12InterchangeId, fileName, 1, "722000095", "170808", currentDt, x12SenderId, 2359);
        X12InterchangeRecord actualX12InterchangeRecord = getValuesInX12InterchangeTable(x12Interchange);
        Assert.assertEquals(actualX12InterchangeRecord, expectedX12InterchangeRecord);

        logger.info("Verify that data are saved to the X12_TRANS_SET table properly");
        X12TransSetRecord expectedX12TransSetRecord = setValuesInX12TransSetRecord(1, 1, "000000023", currentDt, "87");
        X12TransSetRecord actualX12TransSetRecord = getValuesInX12TransSetTable(x12TransSet);
        Assert.assertEquals(actualX12TransSetRecord, expectedX12TransSetRecord);

        logger.info("Verify that data are saved to the EOB_FINANCIAL_TRANS table properly");
        EobFinancialTransRecord expectedEobFinancialTransRecord = setValuesInEobFinancialTransRecord("H", "0", "NON", "20180809", false, "C", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "463051216AFA5", "E14645", "1964772447", "20180808", 1, 0, 0);
        EobFinancialTransRecord actualEobFinancialTransRecord = getValuesInEobFinancialTransTable(eobFinancialTrans);
        Assert.assertEquals(actualEobFinancialTransRecord, expectedEobFinancialTransRecord);

        logger.info("Verify that data are saved to the EOB_CLAIM table properly");
        List eobClaimList = Arrays.asList("11223344556699", "1", "211399.99", "138099.9", "0", "12", "A2011060211484718", "3", eobProvSummarySeqId, StringUtils.EMPTY, "NCFLUWKIVB", "XX", "Centers for Medicare and Medicaid Services", "ACME INSURANCE", "XV", "Centers for Medicare and Medicaid Services PlanID", "20180715", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "8005551212", "ACME INSURANCE", "XV", "Centers for Medicare and Medicaid Services PlanID", false, StringUtils.EMPTY, StringUtils.EMPTY, 0, "A2011060211484718", false, StringUtils.EMPTY, false, 0, 0, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
        EobClaimRecord expectedEobClaimRecord = setValuesInEobClaimRecord(eobClaimList);
        EobClaimRecord actualEobClaimRecord = getValuesInEobClaimTable(eobClaims.get(0));
        Assert.assertEquals(actualEobClaimRecord, expectedEobClaimRecord);

        logger.info("Verify that data are saved to the EOB_CLAIM_ADJ table properly");
        List eobClaimAdjList = Arrays.asList("CO", "131", "550", StringUtils.EMPTY);
        EobClaimAdjRecord expectedEobClaimAdjRecord = setValuesInEobClaimAdjRecord(eobClaimAdjList);
        EobClaimAdjRecord actualEobClaimAdjRecord = getValuesInEobClaimAdjTable(eobClaimAdj);
        Assert.assertEquals(actualEobClaimAdjRecord, expectedEobClaimAdjRecord);

        logger.info("Verify that data are saved to the EOB_SVC_PMT table properly");
        List eobSvcPmtList = Arrays.asList("HC", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "80051", "100", "80",  StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,  StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "20180716", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, 0);
        EobSvcPmtRecord expectedEobSvcPmtRecord = setValuesInEobSvcPmtRecord(eobSvcPmtList);
        EobSvcPmtRecord actualEobSvcPmtRecord = getValuesInEobSvcPmtTable(eobSvcPmt);
        Assert.assertEquals(actualEobSvcPmtRecord, expectedEobSvcPmtRecord);

        logger.info("Verify that data are saved to the EOB_SVC_ADJ table properly");
        List<List> eobSvcAdjList = new ArrayList<>();
        List list = Arrays.asList("PR", "1", "793", StringUtils.EMPTY);
        eobSvcAdjList.add(list);
        list = Arrays.asList("PR", "3", "25", StringUtils.EMPTY);
        eobSvcAdjList.add(list);
        list = Arrays.asList("CO", "131", "250", StringUtils.EMPTY);
        eobSvcAdjList.add(list);
        List<EobSvcAdjRecord> expectedEobSvcAdjRecord = setValuesInEobSvcAdjRecord(eobSvcAdjList);
        List<EobSvcAdjRecord> actualEobSvcAdjRecord = getValuesInEobSvcAdjTable(eobSvcAdjTableList);
        Assert.assertEquals(actualEobSvcAdjRecord,expectedEobSvcAdjRecord);

        logger.info("Verify that data are saved to the EOB_SVC_ID table properly");
        List<List> eobSvcIdListList = new ArrayList<>();
        list = Arrays.asList(8, "100");
        eobSvcIdListList.add(list);
        List<EobSvcIdRecord> expectedEobSvcIdRecord = setValuesInEobSvcIdRecord(eobSvcIdListList);
        List<EobSvcIdRecord> actualEobSvcIdRecord = getValuesInEobSvcIdTable(eobSvcIdList);
        Assert.assertEquals(actualEobSvcIdRecord, expectedEobSvcIdRecord);

        logger.info("Verify that data are saved to the EOB_CLAIM_SUPPLEMENTAL_AMT table properly");
        List eobClaimSupplementalAmtList = Arrays.asList("ZO", "49");
        EobClaimSupplementalAmtRecord expectedEobClaimSupplementalAmtRecord = setValuesInEobClaimSupplementalAmtRecord(eobClaimSupplementalAmtList);
        EobClaimSupplementalAmtRecord actualEobClaimSupplementalAmtRecord = getValuesInEobClaimSupplementalAmtTable(eobClaimSupplementalAmt);
        assertEquals(actualEobClaimSupplementalAmtRecord, expectedEobClaimSupplementalAmtRecord);

        logger.info("Verify that data are saved to the EOB_SVC_REMARK_CD table properly");
        List eobSvcRemarkCdValueList = Arrays.asList("12345");
        List<EobSvcRemarkCdRecord> expectedEobSvcRemarkCdRecord = setValuesInEobSvcRemarkCdRecord(eobSvcRemarkCdValueList);
        List<EobSvcRemarkCdRecord> actualEobSvcRemarkCdRecord = getValuesInEobSvcRemarkCdTable(eobSvcRemarkCdList);
        Assert.assertEquals(actualEobSvcRemarkCdRecord, expectedEobSvcRemarkCdRecord);

        logger.info("Verify that data are saved to the EOB_PYR_CONTACT table properly");
        List<EobPyrContactRecord> expectedEobPyrContactRecord = setValuesInEobPyrContactRecord(eobTechContactInformationList);
        List<EobPyrContactRecord> actualEobPyrContactRecord = getValuesInEobPyrContactTable(eobPyrContactList);
        Assert.assertEquals(actualEobPyrContactRecord, expectedEobPyrContactRecord);

        logger.info("Verify that data are saved to the EOB_PYR table properly");
        List eobPyrList = Arrays.asList("NOVITAS SOLUTIONS, INC.", "1900750689", "PO BOX 3110", StringUtils.EMPTY, "MECHANICSBURG", "PA", "170551826", "04412", "JOHN WAYNE", "8582528782", StringUtils.EMPTY, StringUtils.EMPTY, "2U", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "1900750689");
        EobPyrRecord expectedEobPyrRecord = setValuesInEobPyrRecord(eobPyrList);
        EobPyrRecord actualEobPyrRecord = getValuesInEobPyrTable(eobPyr);
        Assert.assertEquals(actualEobPyrRecord, expectedEobPyrRecord);

        logger.info("Verify that data are saved to the EOB_PAYEE table properly");
        List eobPayeeValueList = Arrays.asList("AMARILLO PATHOLOGY GROUP LLP", "1538105366", "P O BOX 51525", StringUtils.EMPTY, "AMARILLO", "TX", "791591525", "TJ", "752656007", "XX", 0, StringUtils.EMPTY, StringUtils.EMPTY);
        List<EobPayeeRecord> expectedEobPayeeRecord = setValuesInEobPayeeRecord(eobPayeeValueList);
        List<EobPayeeRecord> actualEobPayeeRecord = getValuesInEobPayeeTable(eobPayeeList);
        Assert.assertEquals(actualEobPayeeRecord, expectedEobPayeeRecord);

        logger.info("Verify that data are saved to the EOB_PROV_SUMMARY table properly");
        List eobProvSummaryValueList = Arrays.asList("3", "20180716", "12.2", "1239.9", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, "3123584", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);

        EobProvSummaryRecord expectedEobProvSummaryRecord = setValuesInEobProvSummaryRecord(eobProvSummaryValueList);
        EobProvSummaryRecord actualEobProvSummaryRecord = getValuesInEobProvSummaryTable(eobProvSummary);
        Assert.assertEquals(actualEobProvSummaryRecord, expectedEobProvSummaryRecord);

        logger.info("Verify that data are saved to the EOB_PHI table properly");
        List eobPhiValueList = Arrays.asList("HARRIET", "RUBIN", "M", "MR", "Medicaid Recipient Identification Number", "JESSICA", "SHEPARD", StringUtils.EMPTY, "II", "Standard Unique Health Identifier for each Individual", "I3X9XNY0O2", "NCFLUWKIVB", "NHOU9DNFAW", "Insured Changed Unique Identification Number", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
        EobPhiRecord expectedEobPhiRecord = setValuesInEobPhiRecord(eobPhiValueList);
        EobPhiRecord actualEobPhiRecord = getValuesInEobPhiTable(eobPhi);
        Assert.assertEquals(actualEobPhiRecord, expectedEobPhiRecord);

        logger.info("Verify that data are saved to the EOB_OTHER_CLAIM_IDENT_INFO table properly");
        List eobOtherClaimIdentInfoValueList = Arrays.asList("1L", "Group or Policy Number");
        EobOtherClaimIdentInfoRecord expectedEobOtherClaimIdentInfoRecord = setValuesInEobOtherClaimIdentInfoRecord(eobOtherClaimIdentInfoValueList);
        EobOtherClaimIdentInfoRecord actualEobOtherClaimIdentInfoRecord = getValuesInEobOtherClaimIdentInfoTable(eobOtherClaimIdentInfo);
        Assert.assertEquals(actualEobOtherClaimIdentInfoRecord, expectedEobOtherClaimIdentInfoRecord);

        logger.info("Verify that data are saved to the EOB_CLAIM_SUPPLEMENTAL_QTY table properly");
        List eobClaimSupplementalQtyValueList = Arrays.asList("ZK", "333222");
        EobClaimSupplementalQtyRecord expectedEobClaimSupplementalQtyRecord = setValuesInEobClaimSupplementalQtyRecord(eobClaimSupplementalQtyValueList);
        EobClaimSupplementalQtyRecord actualEobClaimSupplementalQtyRecord = getValuesInEobClaimSupplementalQtyTable(eobClaimSupplementalQty);
        Assert.assertEquals(actualEobClaimSupplementalQtyRecord, expectedEobClaimSupplementalQtyRecord);

        logger.info("Verify that data are saved to the EOB_SVC_SUPPLEMENTAL_AMT table properly");
        List eobSvcSupplementalAmtValueList = Arrays.asList("B6", "425");
        EobSvcSupplementalAmtRecord expectedEobSvcSupplementalAmtRecord = setValuesInEobSvcSupplementalAmtRecord(eobSvcSupplementalAmtValueList);
        EobSvcSupplementalAmtRecord actualEobSvcSupplementalAmtRecord = getValuesInEobSvcSupplementalAmtTable(eobSvcSupplementalAmt);
        Assert.assertEquals(actualEobSvcSupplementalAmtRecord, expectedEobSvcSupplementalAmtRecord);
    }

    @Test(priority = 1, description="Verify 835 file with all loops and segments failed and moved to errored directory ")
    @Parameters({"formatType", "file"})
    public void testPFER_640(String formatType, String file) throws Exception
    {
        logger.info("====== Testing - testPFER_640 ======");

        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);

        String content = FileUtils.readFileToString(new File(upOne + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + file));
        //File name
        String currDtTime = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
        String fileName = "X12-835-" + currDtTime + ".txt";
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathErrored = dirBase + "errored" + File.separator;
        logger.info("Generate X12 835 file with all loops and segments and place the file to " + filePathIncoming);

        fileManipulation.writeFileToFolder(content, filePathIncoming, fileName);

        logger.info("Verify that the new X12 835 file is in the /incoming folder");
        File fIncoming = new File(filePathIncoming + fileName);
        Assert.assertTrue(isFileExists(fIncoming, 5), "X12 835 file: " + fileName + " should be under " + filePathIncoming + " folder.");

        logger.info("Waiting for X12 Engine to run");
        Thread.sleep(QUEUE_ENGINE_WAIT_TIME);

        logger.info("Verify that the X12 835 file failed to process and moved to the /errored folder");
        File fErrored = new File(filePathErrored + fileName);
        Assert.assertTrue(isFileExists(fErrored, 10), "X12 835 file: " + fileName + " should be failed and moved to " + filePathErrored + " folder.");
    }

    @Test(priority = 1, description="Verify 835 file with all loops and segments are processed and saved properly for already existed interchange record ")
    @Parameters({"formatType", "file"})
    public void testPFER_642(String formatType, String file) throws Exception
    {
        logger.info("====== Testing - testPFER_642 ======");

        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);

        String content = FileUtils.readFileToString(new File(upOne + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + file));

        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        String filePathIncoming = dirBase + "incoming" + File.separator;
        String filePathProcessed = dirBase + "processed" + File.separator + "ngs" + File.separator + "835" + File.separator;

        logger.info("Check for already existed files in processed directory ");
        File[] listOfFiles = fileManipulation.findAllFilesStartingWithName(filePathProcessed, file);
        for (File fileName: listOfFiles){
            Assert.assertTrue(fileManipulation.deleteFile(filePathProcessed, fileName.getName()), "File should have been deleted: " + fileName.getName());
        }

        logger.info("Generate X12 835 file with all loops and segments and place the file to "+filePathIncoming);
        fileManipulation.writeFileToFolder(content, filePathIncoming, file);

        logger.info("Verify that the new X12 835 file is in the /incoming folder");
        File fIncoming = new File(filePathIncoming + file);
        Assert.assertTrue(isFileExists(fIncoming, 5), "X12 835 file: " + file + " should be under " + filePathIncoming + " folder.");

        Assert.assertTrue(rpmDao.isX12InterchangeExistByFileName(file), "File Name Exists: " + file);

        logger.info("Waiting for X12 Engine to run");
        Thread.sleep(QUEUE_ENGINE_WAIT_TIME);

        logger.info("Verify that the X12 835 file is processed and moved to the /processed folder");
        int fileSize = fileManipulation.findAllFilesStartingWithName(filePathProcessed, file).length;
        Assert.assertEquals(fileSize, 1, "X12 835 file: " + file + " should be processed and moved to " + filePathProcessed + " folder.");
    }


    /*========== Utils Methods ==========*/

    private void setUpTestCondition() throws XifinDataNotFoundException, XifinDataAccessException {
        List<EobTechContactInformation> eobTechContactInformationList = rpmDao.getEobTechContactInformationByContactName(testDb, "EDI HELPDESK");
        try {
            List<EobPyrContact> eobPyrContactList = rpmDao.getEobPyrContactByEobCntctInfoSeqId(testDb, eobTechContactInformationList.get(0).getSeqId());
            int eobPyrseqId = eobPyrContactList.get(0).getEobPyrSeqId();

            logger.info("*** Actions: - Set up EOB_PYR_CONTACT");
            //Delete EOB_PYR_CONTACT
            rpmDao.deleteEobPyrContactByEobPyrSeqId(testDb, eobPyrseqId);
            logger.info("*** Actions: - Set up EOB_PYR");
            //Delete EOB_PYR
            rpmDao.deleteEobPyrBySeqId(testDb, eobPyrseqId);
        }catch (Exception e){
            //Do nothing;
        }
        logger.info("*** Actions: - Set up EOB_TECH_CONTACT_INFORMATION");
        //Delete EOB_TECH_CONTACT_INFORMATION
        for (EobTechContactInformation eobTechContactInformation:eobTechContactInformationList){
            rpmDao.deleteEobTechContactInformationBySeqId(testDb, eobTechContactInformation.seqId);
        }

        logger.info("*** Actions: - Update EOB_CLAIM.FK_RECONCILE_STAGE_ID = 999 in DB");
        rpmDao.updateEobClaimReconcileStageId(testDb);

        logger.info("*** Actions: - Update EOB_FINANCIAL_TRANS.FK_RECONCILE_STAGE_ID = 999 in DB");
        rpmDao.updateEobFinancialTransReconcileStageId(testDb);
    }

    private X12InterchangeRecord setValuesInX12InterchangeRecord(int seqId , String fileName, int totTransSet, String controlId, String senderControlId, Date currentDate, int senderId, int senderTime) throws ParseException {
        X12InterchangeRecord x12InterchangeRecord = new X12InterchangeRecord();

        x12InterchangeRecord.setSeqId(seqId);
        x12InterchangeRecord.setFileName(fileName);
        x12InterchangeRecord.setTotTransSets(totTransSet);
        x12InterchangeRecord.setSenderControlId(controlId);
        x12InterchangeRecord.setSenderInterchangeDate(senderControlId);
        x12InterchangeRecord.setCreationDt(new java.sql.Date(DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(DATE_FORMAT_MMDDYYYY_WITH_SLASH.format(currentDate)).getTime()));
        x12InterchangeRecord.setX12SenderId(senderId);
        x12InterchangeRecord.setSenderTime(String.valueOf(senderTime));

        return x12InterchangeRecord;
    }

    private X12InterchangeRecord getValuesInX12InterchangeTable(X12Interchange x12Interchange) throws ParseException {
        X12InterchangeRecord x12InterchangeRecord = new X12InterchangeRecord();
        Date creationDt = DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(DATE_FORMAT_MMDDYYYY_WITH_SLASH.format(x12Interchange.getCreationDt().getTime()));

        x12InterchangeRecord.setSeqId(x12Interchange.getSeqId());
        x12InterchangeRecord.setFileName(x12Interchange.getFileName());
        x12InterchangeRecord.setTotTransSets(x12Interchange.getTotTransSets());
        x12InterchangeRecord.setSenderControlId(x12Interchange.getSenderControlId());
        x12InterchangeRecord.setSenderInterchangeDate(x12Interchange.getSenderInterchangeDate());
        x12InterchangeRecord.setCreationDt(new java.sql.Date(creationDt.getTime()));
        x12InterchangeRecord.setX12SenderId(x12Interchange.getX12SenderId());
        x12InterchangeRecord.setSenderTime(x12Interchange.senderTime);

        return x12InterchangeRecord;
    }

    private X12TransSetRecord setValuesInX12TransSetRecord(int x12TransSetTyp, int statusId, String externalCtrlId, Date currentDate, String grpExtCtrlId) throws ParseException {
        X12TransSetRecord x12TransSetRecord = new X12TransSetRecord();

        x12TransSetRecord.setX12TransSetTyp(x12TransSetTyp);
        x12TransSetRecord.setProcessStatusId(statusId);
        x12TransSetRecord.setExternalCtrlId(externalCtrlId);
        x12TransSetRecord.setCreationDt(new java.sql.Date(DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(DATE_FORMAT_MMDDYYYY_WITH_SLASH.format(currentDate)).getTime()));
        x12TransSetRecord.setLastProcessAttemptDt(new java.sql.Date(DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(DATE_FORMAT_MMDDYYYY_WITH_SLASH.format(currentDate)).getTime()));
        x12TransSetRecord.setFunctionalGrpExtCtrlId(grpExtCtrlId);

        return x12TransSetRecord;
    }

    private X12TransSetRecord getValuesInX12TransSetTable(X12TransSet x12TransSet) throws ParseException {
        X12TransSetRecord x12TransSetRecord = new X12TransSetRecord();
        Date lastProcessAttemptDt = DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(DATE_FORMAT_MMDDYYYY_WITH_SLASH.format(x12TransSet.getLastProcessAttemptDt().getTime()));

        x12TransSetRecord.setX12TransSetTyp(x12TransSet.getX12TransSetTyp());
        x12TransSetRecord.setProcessStatusId(x12TransSet.getProcessStatusId());
        x12TransSetRecord.setExternalCtrlId(x12TransSet.getExternalCtrlId());
        x12TransSetRecord.setCreationDt(x12TransSet.getCreationDt());
        x12TransSetRecord.setLastProcessAttemptDt(new java.sql.Date(lastProcessAttemptDt.getTime()));
        x12TransSetRecord.setFunctionalGrpExtCtrlId(x12TransSet.getFunctionalGrpExtCtrlId());

        return x12TransSetRecord;
    }
     private EobFinancialTransRecord setValuesInEobFinancialTransRecord(String eobCode, String transAmt, String eobCdPayment, String effectiveDt, boolean released,
                                                                      String creditCard, String bank, String acctNum, String bankId, String bankAccntNum,
                                                                       String eftTranceNum, String receiverId, String versionIdCd, String productionDtStr, int eobFinancialTransSrcTyp,
                                                                       int reconcileStageId, int dupId)  {
        EobFinancialTransRecord  eobFinancialTransRecord  = new EobFinancialTransRecord ();
        eobFinancialTransRecord.setEobCodeTransHandling(eobCode);
        eobFinancialTransRecord.setTransAmtStr(transAmt);
        eobFinancialTransRecord.setEobCodePmtMethod(eobCdPayment);
        eobFinancialTransRecord.setEffectiveDtStr(effectiveDt);
        eobFinancialTransRecord.setReleased(released);
        eobFinancialTransRecord.setCreditOrDebit(creditCard);
        eobFinancialTransRecord.setPayorsBankId(bank);
        eobFinancialTransRecord.setPayorsBankAcctNum(acctNum);
        eobFinancialTransRecord.setProvidersBankId(bankId);
        eobFinancialTransRecord.setProvidersBankAcctId(bankAccntNum);
        eobFinancialTransRecord.setEftTranceNum(eftTranceNum);
        eobFinancialTransRecord.setReceiverId(receiverId);
        eobFinancialTransRecord.setVersionIdCd(versionIdCd);
        eobFinancialTransRecord.setProductionDtStr(productionDtStr);
        eobFinancialTransRecord.setEobFinancialTransSrcTyp(eobFinancialTransSrcTyp);
        eobFinancialTransRecord.setReconcileStageId(reconcileStageId);
        eobFinancialTransRecord.setDupId(dupId);

        return eobFinancialTransRecord;
    }

    private EobFinancialTransRecord getValuesInEobFinancialTransTable(EobFinancialTrans eobFinancialTrans) {
        EobFinancialTransRecord eobFinancialTransRecord = new EobFinancialTransRecord();

        eobFinancialTransRecord.setEobCodeTransHandling(eobFinancialTrans.getEobCodeTransHandling());
        eobFinancialTransRecord.setTransAmtStr(eobFinancialTrans.getTransAmtStr());
        eobFinancialTransRecord.setEobCodePmtMethod(eobFinancialTrans.getEobCodePmtMethod());
        eobFinancialTransRecord.setEffectiveDtStr(eobFinancialTrans.getEffectiveDtStr());
        eobFinancialTransRecord.setReleased(eobFinancialTrans.getIsReleased());
        eobFinancialTransRecord.setCreditOrDebit(eobFinancialTrans.getCreditOrDebit());
        eobFinancialTransRecord.setPayorsBankId(eobFinancialTrans.getPayorsBankId() == null ? StringUtils.EMPTY : String.valueOf(eobFinancialTrans.getPayorsBankId().trim()));
        eobFinancialTransRecord.setPayorsBankAcctNum(eobFinancialTrans.getPayorsBankAcctNum() == null ? StringUtils.EMPTY : String.valueOf(eobFinancialTrans.getPayorsBankAcctNum().trim())) ;
        eobFinancialTransRecord.setProvidersBankId(eobFinancialTrans.getProvidersBankId() == null ? StringUtils.EMPTY : String.valueOf(eobFinancialTrans.getProvidersBankId().trim()));
        eobFinancialTransRecord.setProvidersBankAcctId(eobFinancialTrans.getProvidersBankAcctId() == null ? StringUtils.EMPTY : String.valueOf(eobFinancialTrans.getProvidersBankAcctId().trim()));
        eobFinancialTransRecord.setEftTranceNum(eobFinancialTrans.getEftTraceNum());
        eobFinancialTransRecord.setReceiverId(eobFinancialTrans.getReceiverId());
        eobFinancialTransRecord.setVersionIdCd(eobFinancialTrans.getVersionIdCd());
        eobFinancialTransRecord.setProductionDtStr(eobFinancialTrans.getProductionDtStr());
        eobFinancialTransRecord.setEobFinancialTransSrcTyp(eobFinancialTrans.getEobFinancialTransSrcTyp());
        eobFinancialTransRecord.setReconcileStageId(eobFinancialTrans.getReconcileStageId());
        eobFinancialTransRecord.setDupId(eobFinancialTrans.getDupId());

        return eobFinancialTransRecord;
    }

    private EobClaimRecord setValuesInEobClaimRecord (List eobClaimList)  {
        EobClaimRecord  eobClaimRecord  = new EobClaimRecord ();

        eobClaimRecord.setExternalCtrlId(String.valueOf(eobClaimList.get(0)));
        eobClaimRecord.setEobCodeClaimStatus(String.valueOf(eobClaimList.get(1)));
        eobClaimRecord.setTotChargeAmtStr(String.valueOf(eobClaimList.get(2)));
        eobClaimRecord.setTotPaidAmtStr(String.valueOf(eobClaimList.get(3)));
        eobClaimRecord.setPtRespAmtStr(String.valueOf(eobClaimList.get(4)));
        eobClaimRecord.setEobCodeClaimFiling(String.valueOf(eobClaimList.get(5)));
        eobClaimRecord.setInternalCtrlId(String.valueOf(eobClaimList.get(6)));
        eobClaimRecord.setEobCodeFac(String.valueOf(eobClaimList.get(7)));
        eobClaimRecord.setEobProvSummaryId(Integer.valueOf(eobClaimList.get(8).toString()));
        eobClaimRecord.setEobCodeClaimFrequency(String.valueOf(eobClaimList.get(9)));
        eobClaimRecord.setServiceProvName(String.valueOf(eobClaimList.get(10)));
        eobClaimRecord.setEobQualifierSvcProvId(String.valueOf(eobClaimList.get(11)));
        eobClaimRecord.setServiceProvExternalId(String.valueOf(eobClaimList.get(12)));
        eobClaimRecord.setCrossoverCarrierName(String.valueOf(eobClaimList.get(13)));
        eobClaimRecord.setEobQualifierCrossoverId(String.valueOf(eobClaimList.get(14)));
        eobClaimRecord.setCrossoverCarrierExternalId(String.valueOf(eobClaimList.get(15)));
        eobClaimRecord.setReceivedByPyrDtStr(String.valueOf(eobClaimList.get(16)));
        eobClaimRecord.setStmtPeriodStartDtStr(String.valueOf(eobClaimList.get(17)));
        eobClaimRecord.setStmtPeriodEndDtStr(String.valueOf(eobClaimList.get(18)));
        eobClaimRecord.setContactName(String.valueOf(eobClaimList.get(19)));
        eobClaimRecord.setEobQualifierClmCntctNum(String.valueOf(eobClaimList.get(20)));
        eobClaimRecord.setContactNumber(String.valueOf(eobClaimList.get(21)));
        eobClaimRecord.setCorrectedPyrPrioName(String.valueOf(eobClaimList.get(22)));
        eobClaimRecord.setEobQualCorrectPyrPrio(String.valueOf(eobClaimList.get(23)));
        eobClaimRecord.setCorrectedPyrPrioId(String.valueOf(eobClaimList.get(24)));
        eobClaimRecord.setExcluded(Boolean.valueOf(eobClaimList.get(25).toString()));
        eobClaimRecord.setReimbursementRate(String.valueOf(eobClaimList.get(26)));
        eobClaimRecord.setHcpcsPayableAmtStr(String.valueOf(eobClaimList.get(27)));
        eobClaimRecord.setPyrId(Integer.valueOf(eobClaimList.get(28).toString()));
        eobClaimRecord.setAccnId(String.valueOf(eobClaimList.get(29)));
        eobClaimRecord.setProcessed(Boolean.valueOf(eobClaimList.get(30).toString()));
        eobClaimRecord.setErrorNote(String.valueOf(eobClaimList.get(31)));
        eobClaimRecord.setDiscrepancy(Boolean.valueOf(eobClaimList.get(32).toString()));
        eobClaimRecord.setPyrPrio(Integer.valueOf(eobClaimList.get(33).toString()));
        eobClaimRecord.setReconcileStageId(Integer.valueOf(eobClaimList.get(34).toString()));
        eobClaimRecord.setDrgCode(String.valueOf(eobClaimList.get(35)));
        eobClaimRecord.setDrgWeight(String.valueOf(eobClaimList.get(36)));
        eobClaimRecord.setDischargeFraction(String.valueOf(eobClaimList.get(37)));
        eobClaimRecord.setCoverageExpDt(String.valueOf(eobClaimList.get(38)));

        return eobClaimRecord;
    }

    private EobClaimRecord getValuesInEobClaimTable(EobClaim eobClaim) {
        EobClaimRecord eobClaimRecord = new EobClaimRecord();

        eobClaimRecord.setExternalCtrlId(eobClaim.getExternalCtrlId());
        eobClaimRecord.setEobCodeClaimStatus(eobClaim.getEobCodeClaimStatus());
        eobClaimRecord.setTotChargeAmtStr(eobClaim.getTotChargeAmtStr());
        eobClaimRecord.setTotPaidAmtStr(eobClaim.getTotPaidAmtStr());
        eobClaimRecord.setPtRespAmtStr(eobClaim.getPtRespAmtStr());
        eobClaimRecord.setEobCodeClaimFiling(eobClaim.getEobCodeClaimFiling());
        eobClaimRecord.setInternalCtrlId(eobClaim.getInternalCtrlId());
        eobClaimRecord.setEobCodeFac(eobClaim.getEobCodeFac());
        eobClaimRecord.setEobProvSummaryId(eobClaim.getEobProvSummaryId());
        eobClaimRecord.setEobCodeClaimFrequency(eobClaim.getEobCodeClaimFrequency()== null ? StringUtils.EMPTY : String.valueOf(eobClaim.getEobCodeClaimFrequency()));
        eobClaimRecord.setServiceProvName(eobClaim.getServiceProvName());
        eobClaimRecord.setEobQualifierSvcProvId(eobClaim.getEobQualifierSvcProvId());
        eobClaimRecord.setServiceProvExternalId(eobClaim.getServiceProvExternalId());
        eobClaimRecord.setCrossoverCarrierName(eobClaim.getCrossoverCarrierName());
        eobClaimRecord.setEobQualifierCrossoverId(eobClaim.getEobQualifierCrossoverId());
        eobClaimRecord.setCrossoverCarrierExternalId(eobClaim.getCrossoverCarrierExternalId());
        eobClaimRecord.setReceivedByPyrDtStr(eobClaim.getReceivedByPyrDtStr());
        eobClaimRecord.setStmtPeriodStartDtStr(eobClaim.getStmtPeriodStartDtStr()== null ? StringUtils.EMPTY : String.valueOf(eobClaim.getStmtPeriodStartDtStr()));
        eobClaimRecord.setStmtPeriodEndDtStr(eobClaim.getStmtPeriodEndDtStr()== null ? StringUtils.EMPTY : eobClaim.getStmtPeriodEndDtStr());
        eobClaimRecord.setContactName(eobClaim.getContactName()== null ? StringUtils.EMPTY : String.valueOf(eobClaim.getContactName()));
        eobClaimRecord.setEobQualifierClmCntctNum(eobClaim.getEobQualifierClmCntctNum()== null ? StringUtils.EMPTY : String.valueOf(eobClaim.getEobQualifierClmCntctNum()));
        eobClaimRecord.setContactNumber(eobClaim.getContactNumber());
        eobClaimRecord.setCorrectedPyrPrioName(eobClaim.getCorrectedPyrPrioName());
        eobClaimRecord.setEobQualCorrectPyrPrio(eobClaim.getEobQualCorrectPyrPrio());
        eobClaimRecord.setCorrectedPyrPrioId(eobClaim.getCorrectedPyrPrioId());
        eobClaimRecord.setExcluded(eobClaim.getIsExcluded());
        eobClaimRecord.setReimbursementRate(eobClaim.getReimbursementRate()== null ? StringUtils.EMPTY : String.valueOf(eobClaim.getReimbursementRate()));
        eobClaimRecord.setHcpcsPayableAmtStr(eobClaim.getHcpcsPayableAmtStr()== null ? StringUtils.EMPTY : String.valueOf(eobClaim.getHcpcsPayableAmtStr()));
        eobClaimRecord.setPyrId(eobClaim.getPyrId());
        eobClaimRecord.setAccnId(eobClaim.getAccnId());
        eobClaimRecord.setProcessed(eobClaim.getIsProcessed());
        eobClaimRecord.setErrorNote(eobClaim.getErrorNote()== null ? StringUtils.EMPTY : String.valueOf(eobClaim.getErrorNote()));
        eobClaimRecord.setDiscrepancy(eobClaim.getIsDiscrepancy());
        eobClaimRecord.setPyrPrio(eobClaim.getPyrPrio());
        eobClaimRecord.setReconcileStageId(eobClaim.getReconcileStageId());
        eobClaimRecord.setDrgCode(eobClaim.getDrgCode()== null ? StringUtils.EMPTY : String.valueOf(eobClaim.getDrgCode()));
        eobClaimRecord.setDrgWeight(eobClaim.getDrgWeight() == null ? StringUtils.EMPTY : String.valueOf(eobClaim.getDrgWeight()));
        eobClaimRecord.setDischargeFraction(eobClaim.getDischargeFraction() == null ? StringUtils.EMPTY : String.valueOf((eobClaim.getDischargeFraction())));
        eobClaimRecord.setCoverageExpDt(eobClaim.getCoverageExpDt()== null ? StringUtils.EMPTY : eobClaim.getCoverageExpDt());

        return eobClaimRecord;
    }

    private EobClaimAdjRecord setValuesInEobClaimAdjRecord(List eobClaimAdjList) {
        EobClaimAdjRecord eobClaimAdjRecord = new EobClaimAdjRecord();

        eobClaimAdjRecord.setEobCodeAdjGrp(String.valueOf(eobClaimAdjList.get(0)));
        eobClaimAdjRecord.setReasonCd(String.valueOf(eobClaimAdjList.get(1)));
        eobClaimAdjRecord.setAmtStr(String.valueOf(eobClaimAdjList.get(2)));
        eobClaimAdjRecord.setQuantity(String.valueOf(eobClaimAdjList.get(3)));

        return eobClaimAdjRecord;
    }

    private EobClaimAdjRecord getValuesInEobClaimAdjTable(EobClaimAdj eobClaimAdj) {
        EobClaimAdjRecord eobClaimAdjRecord = new EobClaimAdjRecord();

        eobClaimAdjRecord.setEobCodeAdjGrp(eobClaimAdj.getEobCodeAdjGrp());
        eobClaimAdjRecord.setReasonCd(eobClaimAdj.getReasonCd());
        eobClaimAdjRecord.setAmtStr(eobClaimAdj.getAmtStr());
        eobClaimAdjRecord.setQuantity(eobClaimAdj.getQuantity() == null ? StringUtils.EMPTY : String.valueOf(eobClaimAdj.getQuantity()));

        return eobClaimAdjRecord;
    }

    private EobSvcPmtRecord setValuesInEobSvcPmtRecord (List eobSvcPmtList)  {
        EobSvcPmtRecord  eobSvcPmtRecord  = new EobSvcPmtRecord ();

        eobSvcPmtRecord.setProcTypQualifier(String.valueOf(eobSvcPmtList.get(0)));
        eobSvcPmtRecord.setProcCd(String.valueOf(eobSvcPmtList.get(1)));
        eobSvcPmtRecord.setProCdMod1(String.valueOf(eobSvcPmtList.get(2)));
        eobSvcPmtRecord.setProCdMod2(String.valueOf(eobSvcPmtList.get(3)));
        eobSvcPmtRecord.setProCdMod3(String.valueOf(eobSvcPmtList.get(4)));
        eobSvcPmtRecord.setProCdMod4(String.valueOf(eobSvcPmtList.get(5)));
        eobSvcPmtRecord.setProcCdDescr(String.valueOf(eobSvcPmtList.get(6)));
        eobSvcPmtRecord.setSubmittedCharge(String.valueOf(eobSvcPmtList.get(7)));
        eobSvcPmtRecord.setPaidAmtStr(String.valueOf(eobSvcPmtList.get(8)));
        eobSvcPmtRecord.setRevenueCd(String.valueOf(eobSvcPmtList.get(9)));
        eobSvcPmtRecord.setUnits(String.valueOf(eobSvcPmtList.get(10)));
        eobSvcPmtRecord.setAltProcTypQualifier(String.valueOf(eobSvcPmtList.get(11)));
        eobSvcPmtRecord.setAltProcCd(String.valueOf(eobSvcPmtList.get(12)));
        eobSvcPmtRecord.setAltProcCdMod1(String.valueOf(eobSvcPmtList.get(13)));
        eobSvcPmtRecord.setAltProcCdMod2(String.valueOf(eobSvcPmtList.get(14)));
        eobSvcPmtRecord.setAltProcCdMod3(String.valueOf(eobSvcPmtList.get(15)));
        eobSvcPmtRecord.setAltProcCdMod4(String.valueOf(eobSvcPmtList.get(16)));
        eobSvcPmtRecord.setAltUnits(String.valueOf(eobSvcPmtList.get(17)));
        eobSvcPmtRecord.setSvcStartDtStr(String.valueOf(eobSvcPmtList.get(18)));
        eobSvcPmtRecord.setSvcEndDtStr(String.valueOf(eobSvcPmtList.get(19)));
        eobSvcPmtRecord.setSingleDaySvcDtStr(String.valueOf(eobSvcPmtList.get(20)));
        eobSvcPmtRecord.setProviderControlId(String.valueOf(eobSvcPmtList.get(21)));
        eobSvcPmtRecord.setLocationNumber(String.valueOf(eobSvcPmtList.get(22)));
        eobSvcPmtRecord.setAccnProcSeqId(Integer.valueOf(eobSvcPmtList.get(23).toString()));

        return eobSvcPmtRecord;
    }

    private EobSvcPmtRecord getValuesInEobSvcPmtTable (EobSvcPmt eobSvcPmt)  {
        EobSvcPmtRecord  eobSvcPmtRecord  = new EobSvcPmtRecord ();

        eobSvcPmtRecord.setProcTypQualifier(eobSvcPmt.getProcTypQualifier());
        eobSvcPmtRecord.setProcCd(eobSvcPmt.getProcCd()== null ? StringUtils.EMPTY : eobSvcPmt.getProcCd());
        eobSvcPmtRecord.setProCdMod1(eobSvcPmt.getProcCdMod1() == null ? StringUtils.EMPTY : eobSvcPmt.getProcCdMod1());
        eobSvcPmtRecord.setProCdMod2(eobSvcPmt.getProcCdMod2() == null ? StringUtils.EMPTY : eobSvcPmt.getProcCdMod2());
        eobSvcPmtRecord.setProCdMod3(eobSvcPmt.getProcCdMod3()== null ? StringUtils.EMPTY : eobSvcPmt.getProcCdMod3());
        eobSvcPmtRecord.setProCdMod4(eobSvcPmt.getProcCdMod4()== null ? StringUtils.EMPTY : eobSvcPmt.getProcCdMod4());
        eobSvcPmtRecord.setProcCdDescr(eobSvcPmt.getProcCdDescr()== null ? StringUtils.EMPTY : eobSvcPmt.getProcCdDescr());
        eobSvcPmtRecord.setSubmittedCharge(eobSvcPmt.getSubmittedCharge());
        eobSvcPmtRecord.setPaidAmtStr(eobSvcPmt.getPaidAmtStr());
        eobSvcPmtRecord.setRevenueCd(eobSvcPmt.getRevenueCd());
        eobSvcPmtRecord.setUnits(eobSvcPmt.getUnits()== null ? StringUtils.EMPTY : eobSvcPmt.getUnits());
        eobSvcPmtRecord.setAltProcTypQualifier(eobSvcPmt.getAltProcTypQualifier()== null ? StringUtils.EMPTY : eobSvcPmt.getAltProcTypQualifier());
        eobSvcPmtRecord.setAltProcCd(eobSvcPmt.getAltProcCd()== null ? StringUtils.EMPTY : eobSvcPmt.getAltProcCd());
        eobSvcPmtRecord.setAltProcCdMod1(eobSvcPmt.getAltProcCdMod1()== null ? StringUtils.EMPTY : eobSvcPmt.getAltProcCdMod1());
        eobSvcPmtRecord.setAltProcCdMod2(eobSvcPmt.getAltProcCdMod2()== null ? StringUtils.EMPTY : eobSvcPmt.getAltProcCdMod2());
        eobSvcPmtRecord.setAltProcCdMod3(eobSvcPmt.getAltProcCdMod3()== null ? StringUtils.EMPTY : eobSvcPmt.getAltProcCdMod3());
        eobSvcPmtRecord.setAltProcCdMod4(eobSvcPmt.getAltProcCdMod4()== null ? StringUtils.EMPTY : eobSvcPmt.getAltProcCdMod4());
        eobSvcPmtRecord.setAltUnits(eobSvcPmt.getAltUnits()== null ? StringUtils.EMPTY : eobSvcPmt.getAltUnits());
        eobSvcPmtRecord.setSvcStartDtStr(eobSvcPmt.getSvcStartDtStr());
        eobSvcPmtRecord.setSvcEndDtStr(eobSvcPmt.getSvcEndDtStr()== null ? StringUtils.EMPTY : eobSvcPmt.getSvcEndDtStr());
        eobSvcPmtRecord.setSingleDaySvcDtStr(eobSvcPmt.getSingleDaySvcDtStr()== null ? StringUtils.EMPTY : eobSvcPmt.getSingleDaySvcDtStr());
        eobSvcPmtRecord.setProviderControlId(eobSvcPmt.getProviderControlId()== null ? StringUtils.EMPTY : eobSvcPmt.getProviderControlId());
        eobSvcPmtRecord.setLocationNumber(eobSvcPmt.getLocationNumber() == null ? StringUtils.EMPTY : eobSvcPmt.getLocationNumber());
        eobSvcPmtRecord.setAccnProcSeqId(eobSvcPmt.getAccnProcSeqId());

        return eobSvcPmtRecord;
    }

    private  List<EobSvcAdjRecord> setValuesInEobSvcAdjRecord (List<List> eobSvcAdjList) {
        List<EobSvcAdjRecord> returnEobSvcAdjRecordList = new ArrayList<>();

        for (List list: eobSvcAdjList){
            EobSvcAdjRecord eobSvcAdjRecord = new EobSvcAdjRecord();
            eobSvcAdjRecord.setEobCodeAdjGrp(String.valueOf(list.get(0)));
            eobSvcAdjRecord.setReasonCd(String.valueOf(list.get(1)));
            eobSvcAdjRecord.setAmtStr(String.valueOf(list.get(2)));
            eobSvcAdjRecord.setQuantity(String.valueOf(list.get(3)));
            returnEobSvcAdjRecordList.add(eobSvcAdjRecord);
        }

        //System.out.println("returnEobSvcAdjRecordList=" + returnEobSvcAdjRecordList);
        return returnEobSvcAdjRecordList;
    }

    private List<EobSvcAdjRecord> getValuesInEobSvcAdjTable(List<EobSvcAdj> eobSvcAdjTableList){
        List<EobSvcAdjRecord> returnEobSvcAdjRecordList = new ArrayList<>();

        for (EobSvcAdj eobSvcAdj: eobSvcAdjTableList){
            EobSvcAdjRecord record = new EobSvcAdjRecord();

            record.setEobCodeAdjGrp(eobSvcAdj.getEobCodeAdjGrp());
            record.setReasonCd(eobSvcAdj.getReasonCd());
            record.setAmtStr(eobSvcAdj.getAmtStr());
            record.setQuantity(eobSvcAdj.getQuantity()== null ? StringUtils.EMPTY : eobSvcAdj.getQuantity());

            returnEobSvcAdjRecordList.add(record);
        }

        return returnEobSvcAdjRecordList;
    }

    private  List<EobSvcIdRecord> setValuesInEobSvcIdRecord (List<List> eobSvcIdListList) {
        List<EobSvcIdRecord> returnEobSvcIdRecordList = new ArrayList<>();

        for (List list: eobSvcIdListList){
            EobSvcIdRecord eobSvcIdRecord = new EobSvcIdRecord();
            eobSvcIdRecord.setEobSvcIdTyp(Integer.valueOf(list.get(0).toString()));
            eobSvcIdRecord.setReferenceId(String.valueOf(list.get(1)));
            returnEobSvcIdRecordList.add(eobSvcIdRecord);
        }

        return returnEobSvcIdRecordList;
    }

    private List<EobSvcIdRecord> getValuesInEobSvcIdTable(List<EobSvcId> eobSvcIdTableList){
        List<EobSvcIdRecord> returnEobSvcIdRecordList = new ArrayList<>();

        for (EobSvcId eobSvcId: eobSvcIdTableList){
            EobSvcIdRecord record = new EobSvcIdRecord();
            record.setEobSvcIdTyp(eobSvcId.getEobSvcIdTyp());
            record.setReferenceId(eobSvcId.getReferenceId());

            returnEobSvcIdRecordList.add(record);
        }

        return returnEobSvcIdRecordList;
    }

    private EobClaimSupplementalAmtRecord setValuesInEobClaimSupplementalAmtRecord(List eobClaimSupplementalAmtList) {
        EobClaimSupplementalAmtRecord returnEobClaimSupplementalAmtRecord = new EobClaimSupplementalAmtRecord();

        returnEobClaimSupplementalAmtRecord.setEobQualifierSuppAmt(String.valueOf(eobClaimSupplementalAmtList.get(0)));
        returnEobClaimSupplementalAmtRecord.setAmtStr(String.valueOf(eobClaimSupplementalAmtList.get(1)));

        return returnEobClaimSupplementalAmtRecord;
    }

    private EobClaimSupplementalAmtRecord getValuesInEobClaimSupplementalAmtTable(EobClaimSupplementalAmt eobClaimSupplementalAmt) {
        EobClaimSupplementalAmtRecord returnEobClaimSupplementalAmtRecord = new EobClaimSupplementalAmtRecord();

        returnEobClaimSupplementalAmtRecord.setEobQualifierSuppAmt(eobClaimSupplementalAmt.getEobQualifierSuppAmt());
        returnEobClaimSupplementalAmtRecord.setAmtStr(eobClaimSupplementalAmt.getAmtStr());

        return returnEobClaimSupplementalAmtRecord;
    }

    private  List<EobSvcRemarkCdRecord> setValuesInEobSvcRemarkCdRecord(List eobSvcRemarkCdValueList) {
        List<EobSvcRemarkCdRecord> returnEobSvcRemarkCdRecordList = new ArrayList<>();

        for (Object o: eobSvcRemarkCdValueList){
            EobSvcRemarkCdRecord eobSvcRemarkCdRecord = new EobSvcRemarkCdRecord();
            eobSvcRemarkCdRecord.setRemarkCd(String.valueOf(o));
            returnEobSvcRemarkCdRecordList.add(eobSvcRemarkCdRecord);
        }

        return returnEobSvcRemarkCdRecordList;
    }

    private List<EobSvcRemarkCdRecord> getValuesInEobSvcRemarkCdTable(List<EobSvcRemarkCd> eobSvcRemarkCdList){
        List<EobSvcRemarkCdRecord> returnEobSvcRemarkCdRecordList = new ArrayList<>();

        for (EobSvcRemarkCd eobSvcRemarkCd: eobSvcRemarkCdList){
            EobSvcRemarkCdRecord record = new EobSvcRemarkCdRecord();
            record.setRemarkCd(eobSvcRemarkCd.getRemarkCd());
            returnEobSvcRemarkCdRecordList.add(record);
        }

        return returnEobSvcRemarkCdRecordList;
    }


    private  List<EobPyrContactRecord> setValuesInEobPyrContactRecord(List<EobTechContactInformation> eobTechContactInformationList) {
        List<EobPyrContactRecord> returnEobPyrContactRecordList = new ArrayList<>();

        for (EobTechContactInformation eobTechContactInformation: eobTechContactInformationList){
            EobPyrContactRecord eobPyrContactRecord = new EobPyrContactRecord();
            eobPyrContactRecord.setEobCntctInfoSeqId(eobTechContactInformation.getSeqId());
            returnEobPyrContactRecordList.add(eobPyrContactRecord);
        }

        return returnEobPyrContactRecordList;
    }

    private List<EobPyrContactRecord> getValuesInEobPyrContactTable(List<EobPyrContact> eobPyrContactList){
        List<EobPyrContactRecord> returnEobPyrContactRecordList = new ArrayList<>();

        for (EobPyrContact eobPyrContact: eobPyrContactList){
            EobPyrContactRecord record = new EobPyrContactRecord();
            record.setEobCntctInfoSeqId(eobPyrContact.getEobCntctInfoSeqId());
            returnEobPyrContactRecordList.add(record);
        }

        return returnEobPyrContactRecordList;
    }

    private EobPyrRecord setValuesInEobPyrRecord (List eobPyrList)  {
        EobPyrRecord  eobPyrRecord  = new EobPyrRecord();

        eobPyrRecord.setName(String.valueOf(eobPyrList.get(0)));
        eobPyrRecord.setExternalIdCd(String.valueOf(eobPyrList.get(1)));
        eobPyrRecord.setAddress1(String.valueOf(eobPyrList.get(2)));
        eobPyrRecord.setAddress2(String.valueOf(eobPyrList.get(3)));
        eobPyrRecord.setCity(String.valueOf(eobPyrList.get(4)));
        eobPyrRecord.setSt(String.valueOf(eobPyrList.get(5)));
        eobPyrRecord.setZipCd(String.valueOf(eobPyrList.get(6)));
        eobPyrRecord.setAdditionalExternalIdCd(String.valueOf(eobPyrList.get(7)));
        eobPyrRecord.setContactName(String.valueOf(eobPyrList.get(8)));
        eobPyrRecord.setContactCommunicationsNumber1(String.valueOf(eobPyrList.get(9)));
        eobPyrRecord.setContactCommunicationsNumber2(String.valueOf(eobPyrList.get(10)));
        eobPyrRecord.setContactCommunicationsNumber3(String.valueOf(eobPyrList.get(11)));
        eobPyrRecord.setEobQualifierAdditionalId(String.valueOf(eobPyrList.get(12)));
        eobPyrRecord.setTaxId(String.valueOf(eobPyrList.get(13)));
        eobPyrRecord.setWebSiteUrl(String.valueOf(eobPyrList.get(14)));
        eobPyrRecord.setContactCommunicationsQual1(String.valueOf(eobPyrList.get(15)));
        eobPyrRecord.setContactCommunicationsQual2(String.valueOf(eobPyrList.get(16)));
        eobPyrRecord.setContactCommunicationsQual3(String.valueOf(eobPyrList.get(17)));
        eobPyrRecord.setOrigCompanyId(String.valueOf(eobPyrList.get(18)));

        return eobPyrRecord;
    }

    private EobPyrRecord getValuesInEobPyrTable (EobPyr eobPyr)  {
        EobPyrRecord  eobPyrRecord  = new EobPyrRecord();

        eobPyrRecord.setName(eobPyr.getName());
        eobPyrRecord.setExternalIdCd(eobPyr.getExternalIdCd());
        eobPyrRecord.setAddress1(eobPyr.getAddress1());
        eobPyrRecord.setAddress2(eobPyr.getAddress2()== null ? StringUtils.EMPTY : eobPyr.getAddress2());
        eobPyrRecord.setCity(eobPyr.getCity()== null ? StringUtils.EMPTY : eobPyr.getCity());
        eobPyrRecord.setSt(eobPyr.getSt()== null ? StringUtils.EMPTY : eobPyr.getSt());
        eobPyrRecord.setZipCd(eobPyr.getZipCd()== null ? StringUtils.EMPTY : eobPyr.getZipCd());
        eobPyrRecord.setAdditionalExternalIdCd(eobPyr.getAdditionalExternalIdCd()== null ? StringUtils.EMPTY : eobPyr.getAdditionalExternalIdCd());
        eobPyrRecord.setContactName(eobPyr.getContactName()== null ? StringUtils.EMPTY : eobPyr.getContactName());
        eobPyrRecord.setContactCommunicationsNumber1(eobPyr.getContactCommunicationsNumber1()== null ? StringUtils.EMPTY : eobPyr.getContactCommunicationsNumber1());
        eobPyrRecord.setContactCommunicationsNumber2(eobPyr.getContactCommunicationsNumber2()== null ? StringUtils.EMPTY : eobPyr.getContactCommunicationsNumber2());
        eobPyrRecord.setContactCommunicationsNumber3(eobPyr.getContactCommunicationsNumber3()== null ? StringUtils.EMPTY : eobPyr.getContactCommunicationsNumber3());
        eobPyrRecord.setEobQualifierAdditionalId(eobPyr.getEobQualifierAdditionalId()== null ? StringUtils.EMPTY : eobPyr.getEobQualifierAdditionalId());
        eobPyrRecord.setTaxId(eobPyr.getTaxId()== null ? StringUtils.EMPTY : eobPyr.getTaxId());
        eobPyrRecord.setWebSiteUrl(eobPyr.getWebSiteUrl()== null ? StringUtils.EMPTY : eobPyr.getWebSiteUrl());
        eobPyrRecord.setContactCommunicationsQual1(eobPyr.getContactCommunicationsQual1()== null ? StringUtils.EMPTY : eobPyr.getContactCommunicationsQual1());
        eobPyrRecord.setContactCommunicationsQual2(eobPyr.getContactCommunicationsQual2()== null ? StringUtils.EMPTY : eobPyr.getContactCommunicationsQual2());
        eobPyrRecord.setContactCommunicationsQual3(eobPyr.getContactCommunicationsQual3()== null ? StringUtils.EMPTY : eobPyr.getContactCommunicationsQual3());
        eobPyrRecord.setOrigCompanyId(eobPyr.getOrigCompanyId()== null ? StringUtils.EMPTY : eobPyr.getOrigCompanyId());

        return eobPyrRecord;
    }

    private  List<EobTechContactInformationRecord> setValuesInEobTechContactInformationRecord (List<List> eobTechContactInformationValueList) {
        List<EobTechContactInformationRecord> returnEobTechContactInformationRecordList = new ArrayList<>();

        for (List list: eobTechContactInformationValueList){
            EobTechContactInformationRecord eobTechContactInformationRecord = new EobTechContactInformationRecord();
            eobTechContactInformationRecord.setEobCommTypSeqId(Integer.valueOf(list.get(0).toString()));
            eobTechContactInformationRecord.setContactNumber(String.valueOf(list.get(1)));
            returnEobTechContactInformationRecordList.add(eobTechContactInformationRecord);
        }

        return returnEobTechContactInformationRecordList;
    }

    private List<EobTechContactInformationRecord> getValuesInEobTechContactInformationTable(List<EobTechContactInformation> eobTechContactInformationList){
        List<EobTechContactInformationRecord> returnEobTechContactInformationRecordList = new ArrayList<>();

        for (EobTechContactInformation eobTechContactInformation: eobTechContactInformationList){
            EobTechContactInformationRecord record = new EobTechContactInformationRecord();
            record.setEobCommTypSeqId(eobTechContactInformation.getEobCommTypSeqId());
            record.setContactNumber(eobTechContactInformation.getContactNumber());

            returnEobTechContactInformationRecordList.add(record);
        }

        return returnEobTechContactInformationRecordList;
    }

    private List<EobPayeeRecord> setValuesInEobPayeeRecord (List eobPayeeValueList)  {
        List<EobPayeeRecord>  returnEobPayeeRecordList  = new ArrayList<>();
        EobPayeeRecord eobPayeeRecord = new EobPayeeRecord();
        for (Object o : eobPayeeValueList) {
            eobPayeeRecord.setName(String.valueOf(eobPayeeValueList.get(0)));
            eobPayeeRecord.setExternalId(String.valueOf(eobPayeeValueList.get(1)));
            eobPayeeRecord.setAddress1(String.valueOf(eobPayeeValueList.get(2)));
            eobPayeeRecord.setAddress2(String.valueOf(eobPayeeValueList.get(3)));
            eobPayeeRecord.setCity(String.valueOf(eobPayeeValueList.get(4)));
            eobPayeeRecord.setState(String.valueOf(eobPayeeValueList.get(5)));
            eobPayeeRecord.setZip(String.valueOf(eobPayeeValueList.get(6)));
            eobPayeeRecord.setEobQualifierAdditionalId(String.valueOf(eobPayeeValueList.get(7)));
            eobPayeeRecord.setAddionalId(String.valueOf(eobPayeeValueList.get(8)));
            eobPayeeRecord.setEobQualifierExtIdQual(String.valueOf(eobPayeeValueList.get(9)));
            eobPayeeRecord.setEobDeliverMthdTyp(Integer.valueOf(eobPayeeValueList.get(10).toString()));
            eobPayeeRecord.setRemitRecipientName(String.valueOf(eobPayeeValueList.get(11)));
            eobPayeeRecord.setRemitCommNumber(String.valueOf(eobPayeeValueList.get(12)));


        }
        returnEobPayeeRecordList.add(eobPayeeRecord);

        return returnEobPayeeRecordList;
    }

    private List<EobPayeeRecord> getValuesInEobPayeeTable(List<EobPayee> eobPayeeList){
        List<EobPayeeRecord> returnEobPayeeRecordList = new ArrayList<>();

        for (EobPayee eobPayee: eobPayeeList){
            EobPayeeRecord record = new EobPayeeRecord();
            record.setName(eobPayee.getName());
            record.setExternalId(eobPayee.getExternalId());
            record.setAddress1(eobPayee.getAddress1());
            record.setAddress2(eobPayee.getAddress2()== null ? StringUtils.EMPTY : eobPayee.getAddress2());
            record.setCity(eobPayee.getCity());
            record.setState(eobPayee.getState());
            record.setZip(eobPayee.getZip());
            record.setEobQualifierAdditionalId(eobPayee.getEobQualifierAdditionalId());
            record.setAddionalId(eobPayee.getAdditionalId());
            record.setEobQualifierExtIdQual(eobPayee.getEobQualifierExtIdQual());
            record.setEobDeliverMthdTyp(eobPayee.getEobDeliveryMthdTyp());
            record.setRemitRecipientName(eobPayee.getRemitRecipientName()== null ? StringUtils.EMPTY : eobPayee.getRemitRecipientName());
            record.setRemitCommNumber(eobPayee.getRemitCommNumber() == null ? StringUtils.EMPTY : eobPayee.getRemitCommNumber());

            returnEobPayeeRecordList.add(record);
        }

        return returnEobPayeeRecordList;
    }

    private EobProvSummaryRecord setValuesInEobProvSummaryRecord (List eobProvSummaryValueList)  {
        EobProvSummaryRecord  eobProvSummaryRecord  = new EobProvSummaryRecord ();

        eobProvSummaryRecord.setFacCd(String.valueOf(eobProvSummaryValueList.get(0)));
        eobProvSummaryRecord.setFiscalPeriodDtStr(String.valueOf(eobProvSummaryValueList.get(1)));
        eobProvSummaryRecord.setClaimCount(String.valueOf(eobProvSummaryValueList.get(2)));
        eobProvSummaryRecord.setTotClaimChargeAmtStr(String.valueOf(eobProvSummaryValueList.get(3)));
        eobProvSummaryRecord.setTotCoveredChargeAmtStr(String.valueOf(eobProvSummaryValueList.get(4)));
        eobProvSummaryRecord.setTotNonCoveredChargeAmtStr(String.valueOf(eobProvSummaryValueList.get(5)));
        eobProvSummaryRecord.setTotDeniedChargeAmtStr(String.valueOf(eobProvSummaryValueList.get(6)));
        eobProvSummaryRecord.setTotProvPmtAmtStr(String.valueOf(eobProvSummaryValueList.get(7)));
        eobProvSummaryRecord.setTotInterestAmtStr(String.valueOf(eobProvSummaryValueList.get(8)));
        eobProvSummaryRecord.setTotContractualAdjAmtStr(String.valueOf(eobProvSummaryValueList.get(9)));
        eobProvSummaryRecord.setTotMcSecondaryPyrAmtStr(String.valueOf(eobProvSummaryValueList.get(10)));
        eobProvSummaryRecord.setTotCoInsAmtStr(String.valueOf(eobProvSummaryValueList.get(11)));
        eobProvSummaryRecord.setTotDeductAmtStr(String.valueOf(eobProvSummaryValueList.get(12)));
        eobProvSummaryRecord.setExternalProviderId(String.valueOf(eobProvSummaryValueList.get(13)));
        eobProvSummaryRecord.setTotPatReimbursementAmtStr(String.valueOf(eobProvSummaryValueList.get(14)));
        eobProvSummaryRecord.setTotNonLabChargeAmtStr(String.valueOf(eobProvSummaryValueList.get(15)));
        eobProvSummaryRecord.setTotHcpcsPayableAmtStr(String.valueOf(eobProvSummaryValueList.get(16)));
        eobProvSummaryRecord.setTotHcpcsReportChrgAmtStr(String.valueOf(eobProvSummaryValueList.get(17)));
        eobProvSummaryRecord.setTotProfessionalCompAmtStr(String.valueOf(eobProvSummaryValueList.get(18)));
        eobProvSummaryRecord.setTotMspPtLiabltyMetAmtStr(String.valueOf(eobProvSummaryValueList.get(19)));
        eobProvSummaryRecord.setTotPipClaimCountStr(String.valueOf(eobProvSummaryValueList.get(20)));
        eobProvSummaryRecord.setTotPipAdjAmtStr(String.valueOf(eobProvSummaryValueList.get(21)));

        return eobProvSummaryRecord;
    }

    private EobProvSummaryRecord getValuesInEobProvSummaryTable (EobProvSummary eobProvSummary)  {
        EobProvSummaryRecord  eobProvSummaryRecord  = new EobProvSummaryRecord ();

        eobProvSummaryRecord.setFacCd(eobProvSummary.getFacCd()== null ? StringUtils.EMPTY : eobProvSummary.getFacCd());
        eobProvSummaryRecord.setFiscalPeriodDtStr(eobProvSummary.getFiscalPeriodDtStr() == null ? StringUtils.EMPTY : eobProvSummary.getFiscalPeriodDtStr());
        eobProvSummaryRecord.setClaimCount(eobProvSummary.getClaimCount()== null ? StringUtils.EMPTY : eobProvSummary.getClaimCount());
        eobProvSummaryRecord.setTotClaimChargeAmtStr(eobProvSummary.getTotClaimChargeAmtStr()== null ? StringUtils.EMPTY : eobProvSummary.getTotClaimChargeAmtStr());
        eobProvSummaryRecord.setTotCoveredChargeAmtStr(eobProvSummary.getTotCoveredChargeAmtStr()== null ? StringUtils.EMPTY : eobProvSummary.getTotCoveredChargeAmtStr());
        eobProvSummaryRecord.setTotNonCoveredChargeAmtStr(eobProvSummary.getTotNonCoveredChargeAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotNonCoveredChargeAmtStr());
        eobProvSummaryRecord.setTotDeniedChargeAmtStr(eobProvSummary.getTotDeniedChargeAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotDeniedChargeAmtStr());
        eobProvSummaryRecord.setTotProvPmtAmtStr(eobProvSummary.getTotProvPmtAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotProvPmtAmtStr());
        eobProvSummaryRecord.setTotInterestAmtStr(eobProvSummary.getTotInterestAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotInterestAmtStr());
        eobProvSummaryRecord.setTotContractualAdjAmtStr(eobProvSummary.getTotContractualAdjAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotContractualAdjAmtStr());
        eobProvSummaryRecord.setTotMcSecondaryPyrAmtStr(eobProvSummary.getTotMcSecondaryPyrAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotMcSecondaryPyrAmtStr());
        eobProvSummaryRecord.setTotCoInsAmtStr(eobProvSummary.getTotCoInsAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotCoInsAmtStr());
        eobProvSummaryRecord.setTotDeductAmtStr(eobProvSummary.getTotDeductAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotDeductAmtStr());
        eobProvSummaryRecord.setExternalProviderId(eobProvSummary.getExternalProviderId()== null ? StringUtils.EMPTY :eobProvSummary.getExternalProviderId());
        eobProvSummaryRecord.setTotPatReimbursementAmtStr(eobProvSummary.getTotPatReimbursementAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotPatReimbursementAmtStr());
        eobProvSummaryRecord.setTotNonLabChargeAmtStr(eobProvSummary.getTotNonLabChargeAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotNonLabChargeAmtStr());
        eobProvSummaryRecord.setTotHcpcsPayableAmtStr(eobProvSummary.getTotHcpcsPayableAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotHcpcsPayableAmtStr());
        eobProvSummaryRecord.setTotHcpcsReportChrgAmtStr(eobProvSummary.getTotHcpcsReportChrgAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotHcpcsReportChrgAmtStr());
        eobProvSummaryRecord.setTotProfessionalCompAmtStr(eobProvSummary.getTotProfessionalCompAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotProfessionalCompAmtStr());
        eobProvSummaryRecord.setTotMspPtLiabltyMetAmtStr(eobProvSummary.getTotMspPtLiabltyMetAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotMspPtLiabltyMetAmtStr());
        eobProvSummaryRecord.setTotPipClaimCountStr(eobProvSummary.getTotPipClaimCountStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotPipClaimCountStr());
        eobProvSummaryRecord.setTotPipAdjAmtStr(eobProvSummary.getTotPipAdjAmtStr()== null ? StringUtils.EMPTY :eobProvSummary.getTotPipAdjAmtStr());

        return eobProvSummaryRecord;
    }

    private EobPhiRecord setValuesInEobPhiRecord (List eobPhiValueList)  {
        EobPhiRecord  eobPhiRecord  = new EobPhiRecord ();

        eobPhiRecord.setPtFNm(String.valueOf(eobPhiValueList.get(0)));
        eobPhiRecord.setPtLNm(String.valueOf(eobPhiValueList.get(1)));
        eobPhiRecord.setPtMNm(String.valueOf(eobPhiValueList.get(2)));
        eobPhiRecord.setEobQualifierPtId(String.valueOf(eobPhiValueList.get(3)));
        eobPhiRecord.setExternalPtId(String.valueOf(eobPhiValueList.get(4)));
        eobPhiRecord.setSubsFNm(String.valueOf(eobPhiValueList.get(5)));
        eobPhiRecord.setSubsLNm(String.valueOf(eobPhiValueList.get(6)));
        eobPhiRecord.setSubsMNm(String.valueOf(eobPhiValueList.get(7)));
        eobPhiRecord.setEobQualifierSubs(String.valueOf(eobPhiValueList.get(8)));
        eobPhiRecord.setExternalSubsId(String.valueOf(eobPhiValueList.get(9)));
        eobPhiRecord.setCorrectedSubsFNm(String.valueOf(eobPhiValueList.get(10)));
        eobPhiRecord.setCorrectedSubsLNm(String.valueOf(eobPhiValueList.get(11)));
        eobPhiRecord.setCorrectedSubsMNm(String.valueOf(eobPhiValueList.get(12)));
        eobPhiRecord.setCorrectedExternalSubsId(String.valueOf(eobPhiValueList.get(13)));
        eobPhiRecord.setOtherSubsLName(String.valueOf(eobPhiValueList.get(14)));
        eobPhiRecord.setOtherSubsFName(String.valueOf(eobPhiValueList.get(15)));
        eobPhiRecord.setOtherSubsMName(String.valueOf(eobPhiValueList.get(16)));
        eobPhiRecord.setExternalOtherSubsId(String.valueOf(eobPhiValueList.get(17)));
        eobPhiRecord.setEobQualifierOtherSubsId(String.valueOf(eobPhiValueList.get(18)));

        return eobPhiRecord;
    }

    private EobPhiRecord getValuesInEobPhiTable (EobPhi eobPhi)  {
        EobPhiRecord  eobPhiRecord  = new EobPhiRecord ();

        eobPhiRecord.setPtFNm(eobPhi.getPtFNm()== null ? StringUtils.EMPTY : eobPhi.getPtFNm());
        eobPhiRecord.setPtLNm(eobPhi.getPtLNm() == null ? StringUtils.EMPTY : eobPhi.getPtLNm());
        eobPhiRecord.setPtMNm(eobPhi.getPtMNm()== null ? StringUtils.EMPTY : eobPhi.getPtMNm());
        eobPhiRecord.setEobQualifierPtId(eobPhi.getEobQualifierPtId()== null ? StringUtils.EMPTY : eobPhi.getEobQualifierPtId());
        eobPhiRecord.setExternalPtId(eobPhi.getExternalPtId()== null ? StringUtils.EMPTY : eobPhi.getExternalPtId());
        eobPhiRecord.setSubsFNm(eobPhi.getSubsFNm()== null ? StringUtils.EMPTY :eobPhi.getSubsFNm());
        eobPhiRecord.setSubsLNm(eobPhi.getSubsLNm()== null ? StringUtils.EMPTY :eobPhi.getSubsLNm());
        eobPhiRecord.setSubsMNm(eobPhi.getSubsMNm()== null ? StringUtils.EMPTY :eobPhi.getSubsMNm());
        eobPhiRecord.setEobQualifierSubs(eobPhi.getEobQualifierSubs()== null ? StringUtils.EMPTY :eobPhi.getEobQualifierSubs());
        eobPhiRecord.setExternalSubsId(eobPhi.getExternalSubsId()== null ? StringUtils.EMPTY :eobPhi.getExternalSubsId());
        eobPhiRecord.setCorrectedSubsFNm(eobPhi.getCorrectedSubsFNm()== null ? StringUtils.EMPTY :eobPhi.getCorrectedSubsFNm());
        eobPhiRecord.setCorrectedSubsLNm(eobPhi.getCorrectedSubsLNm()== null ? StringUtils.EMPTY :eobPhi.getCorrectedSubsLNm());
        eobPhiRecord.setCorrectedSubsMNm(eobPhi.getCorrectedSubsMNm()== null ? StringUtils.EMPTY :eobPhi.getCorrectedSubsMNm());
        eobPhiRecord.setCorrectedExternalSubsId(eobPhi.getCorrectedExternalSubsId()== null ? StringUtils.EMPTY :eobPhi.getCorrectedExternalSubsId());
        eobPhiRecord.setOtherSubsLName(eobPhi.getOtherSubsLName()== null ? StringUtils.EMPTY :eobPhi.getOtherSubsLName());
        eobPhiRecord.setOtherSubsFName(eobPhi.getOtherSubsFName()== null ? StringUtils.EMPTY :eobPhi.getOtherSubsFName());
        eobPhiRecord.setOtherSubsMName(eobPhi.getOtherSubsMName()== null ? StringUtils.EMPTY :eobPhi.getOtherSubsMName());
        eobPhiRecord.setExternalOtherSubsId(eobPhi.getExternalOtherSubsId()== null ? StringUtils.EMPTY :eobPhi.getExternalOtherSubsId());
        eobPhiRecord.setEobQualifierOtherSubsId(eobPhi.getEobQualifierOtherSubsId()== null ? StringUtils.EMPTY :eobPhi.getEobQualifierOtherSubsId());

        return eobPhiRecord;
    }

    private EobOtherClaimIdentInfoRecord setValuesInEobOtherClaimIdentInfoRecord (List eobOtherClaimIdentInfoValueList)  {
        EobOtherClaimIdentInfoRecord  eobOtherClaimIdentInfoRecord  = new EobOtherClaimIdentInfoRecord ();

        eobOtherClaimIdentInfoRecord.setEobQualifierIdentInfo(String.valueOf(eobOtherClaimIdentInfoValueList.get(0)));
        eobOtherClaimIdentInfoRecord.setIdentityInfo(String.valueOf(eobOtherClaimIdentInfoValueList.get(1)));

        return eobOtherClaimIdentInfoRecord;
    }

    private EobOtherClaimIdentInfoRecord getValuesInEobOtherClaimIdentInfoTable (EobOtherClaimIdentInfo eobOtherClaimIdentInfo)  {
        EobOtherClaimIdentInfoRecord  eobOtherClaimIdentInfoRecord  = new EobOtherClaimIdentInfoRecord ();

        eobOtherClaimIdentInfoRecord.setEobQualifierIdentInfo(eobOtherClaimIdentInfo.getEobQualifierIdentInfo()== null ? StringUtils.EMPTY : eobOtherClaimIdentInfo.getEobQualifierIdentInfo());
        eobOtherClaimIdentInfoRecord.setIdentityInfo(eobOtherClaimIdentInfo.getIdentityInfo() == null ? StringUtils.EMPTY : eobOtherClaimIdentInfo.getIdentityInfo());

        return eobOtherClaimIdentInfoRecord;
    }

    private EobClaimRenderingProvIdRecord setValuesInEobClaimRenderingProvIdRecord (List eobClaimRenderingProvIdValueList)  {
        EobClaimRenderingProvIdRecord  eobClaimRenderingProvIdRecord  = new EobClaimRenderingProvIdRecord ();

        eobClaimRenderingProvIdRecord.setEobRenderingProvIdTyp(Integer.valueOf(eobClaimRenderingProvIdValueList.get(0).toString()));
        eobClaimRenderingProvIdRecord.setReferenceId(String.valueOf(eobClaimRenderingProvIdValueList.get(1)));

        return eobClaimRenderingProvIdRecord;
    }

    private EobClaimRenderingProvIdRecord getValuesInEobClaimRenderingProvIdTable (EobClaimRenderingProvId eobClaimRenderingProvId)  {
        EobClaimRenderingProvIdRecord  eobClaimRenderingProvIdRecord  = new EobClaimRenderingProvIdRecord ();

        eobClaimRenderingProvIdRecord.setEobRenderingProvIdTyp(eobClaimRenderingProvId.getEobRenderingProvIdTyp());
        eobClaimRenderingProvIdRecord.setReferenceId(eobClaimRenderingProvId.getReferenceId() == null ? StringUtils.EMPTY : eobClaimRenderingProvId.getReferenceId());

        return eobClaimRenderingProvIdRecord;
    }


    private EobClaimSupplementalQtyRecord setValuesInEobClaimSupplementalQtyRecord (List eobClaimSupplementalQtyValueList)  {
        EobClaimSupplementalQtyRecord  eobClaimSupplementalQtyRecord  = new EobClaimSupplementalQtyRecord ();

        eobClaimSupplementalQtyRecord.setEobQualifierClmSuppQty(String.valueOf(eobClaimSupplementalQtyValueList.get(0)));
        eobClaimSupplementalQtyRecord.setQty(String.valueOf(eobClaimSupplementalQtyValueList.get(1)));

        return eobClaimSupplementalQtyRecord;
    }

    private EobClaimSupplementalQtyRecord getValuesInEobClaimSupplementalQtyTable (EobClaimSupplementalQty eobClaimSupplementalQty)  {
        EobClaimSupplementalQtyRecord  eobClaimSupplementalQtyRecord  = new EobClaimSupplementalQtyRecord ();

        eobClaimSupplementalQtyRecord.setEobQualifierClmSuppQty(eobClaimSupplementalQty.getEobQualifierClmSuppQty());
        eobClaimSupplementalQtyRecord.setQty(eobClaimSupplementalQty.getQty()== null ? StringUtils.EMPTY : eobClaimSupplementalQty.getQty());

        return eobClaimSupplementalQtyRecord;
    }

    private EobSvcSupplementalAmtRecord setValuesInEobSvcSupplementalAmtRecord (List eobSvcSupplementalAmtValueList)  {
        EobSvcSupplementalAmtRecord  eobSvcSupplementalAmtRecord  = new EobSvcSupplementalAmtRecord ();

        eobSvcSupplementalAmtRecord.setEobQualifier(String.valueOf(eobSvcSupplementalAmtValueList.get(0)));
        eobSvcSupplementalAmtRecord.setAmtStr(String.valueOf(eobSvcSupplementalAmtValueList.get(1)));

        return eobSvcSupplementalAmtRecord;
    }

    private EobSvcSupplementalAmtRecord getValuesInEobSvcSupplementalAmtTable (EobSvcSupplementalAmt eobSvcSupplementalAmt)  {
        EobSvcSupplementalAmtRecord  eobSvcSupplementalAmtRecord  = new EobSvcSupplementalAmtRecord ();

        eobSvcSupplementalAmtRecord.setEobQualifier(eobSvcSupplementalAmt.getEobQualifier());
        eobSvcSupplementalAmtRecord.setAmtStr(eobSvcSupplementalAmt.getAmtStr()== null ? StringUtils.EMPTY : eobSvcSupplementalAmt.getAmtStr());

        return eobSvcSupplementalAmtRecord;
    }

    private EobSvcRenderingProvIdRecord setValuesInEobSvcRenderingProvIdRecord (List eobSvcSupplementalAmtValueList)  {
        EobSvcRenderingProvIdRecord  eobSvcRenderingProvIdRecord  = new EobSvcRenderingProvIdRecord ();

        eobSvcRenderingProvIdRecord.setEobRenderingProvIdTyp(Integer.valueOf(eobSvcSupplementalAmtValueList.get(0).toString()));
        eobSvcRenderingProvIdRecord.setReferenceId(String.valueOf(eobSvcSupplementalAmtValueList.get(1)));

        return eobSvcRenderingProvIdRecord;
    }

    private EobSvcRenderingProvIdRecord getValuesInEobSvcRenderingProvIdTable (EobSvcRenderingProvId eobSvcRenderingProvId)  {
        EobSvcRenderingProvIdRecord  eobSvcRenderingProvIdRecord  = new EobSvcRenderingProvIdRecord ();

        eobSvcRenderingProvIdRecord.setEobRenderingProvIdTyp(eobSvcRenderingProvId.getEobRenderingProvIdTyp());
        eobSvcRenderingProvIdRecord.setReferenceId(eobSvcRenderingProvId.getReferenceId()== null ? StringUtils.EMPTY : eobSvcRenderingProvId.getReferenceId());

        return eobSvcRenderingProvIdRecord;
    }
    protected boolean waitForX12EngineToCreateX12Interchange(String filename, long maxTime) throws InterruptedException, XifinDataAccessException, XifinDataNotFoundException {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        logger.info("Waiting for X12 Engine");
        boolean isFileInX12Interchange = rpmDao.isX12InterchangeExistByFileName(filename);
        while (!isFileInX12Interchange && System.currentTimeMillis() < maxTime)
        {
            Thread.sleep(QUEUE_POLL_TIME_MS);
            logger.info("Waiting for X12 Engine to create x12_interchange record for filename=" + filename + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            isFileInX12Interchange = rpmDao.isX12InterchangeExistByFileName(filename);
        }
        return isFileInX12Interchange;
    }
}
