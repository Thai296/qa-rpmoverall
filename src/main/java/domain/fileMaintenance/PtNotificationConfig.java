package domain.fileMaintenance;

import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnAccntTyp.ClnAccntTyp;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.ptNotifLtrConfig.PtNotifLtrConfig;
import com.mbasys.mars.ejb.entity.ptNotifLtrConfigLnk.PtNotifLtrConfigLnk;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrGrp.PyrGrp;
import com.mbasys.mars.ejb.entity.taxonomy.Taxonomy;
import com.mbasys.mars.ejb.entity.test.Test;
import com.mbasys.mars.ejb.entity.testTyp.TestTyp;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.MiscMap;
import java.util.ArrayList;
import java.util.List;

public class PtNotificationConfig
{
    private Pyr pyr;
    private PyrGrp pyrGrp;
    private String letterId;
    private ClnAccntTyp clnAccntTyp;
    private Cln cln;
    private Taxonomy taxonomy;
    private TestTyp testTyp;
    private Test test;
    private Fac clnFac;

    public ClnAccntTyp getClnAccntTyp()
    {
        return clnAccntTyp;
    }
    public void setClnAccntTyp(ClnAccntTyp clnAccntTyp)
    {
        this.clnAccntTyp = clnAccntTyp;
    }
    public Cln getCln()
    {
        return cln;
    }
    public void setCln(Cln cln)
    {
        this.cln = cln;
    }
    public Taxonomy getTaxonomy()
    {
        return taxonomy;
    }
    public void setTaxonomy(Taxonomy taxonomy)
    {
        this.taxonomy = taxonomy;
    }
    public TestTyp getTestTyp()
    {
        return testTyp;
    }
    public void setTestTyp(TestTyp testtTyp)
    {
        this.testTyp = testtTyp;
    }
    public Test getTest()
    {
        return test;
    }
    public void setTest(Test test)
    {
        this.test = test;
    }
    public Fac getClnFac()
    {
        return clnFac;
    }
    public void setClnFac(Fac clnFac)
    {
        this.clnFac = clnFac;
    }
    public Pyr getPyr()
    {
        return pyr;
    }
    public void setPyr(Pyr pyr)
    {
        this.pyr = pyr;
    }
    public PyrGrp getPyrGrp()
    {
        return pyrGrp;
    }
    public void setPyrGrp(PyrGrp pyrGrp)
    {
        this.pyrGrp = pyrGrp;
    }
    public String getLetterId()
    {
        return letterId;
    }
    public void setLetterId(String letterId)
    {
        this.letterId = letterId;
    }
    public PtNotifLtrConfig getPtNotifLtrConfig()
    {
        PtNotifLtrConfig ptNotifLtrConfig = new PtNotifLtrConfig();
        ptNotifLtrConfig.setLetterId(getLetterId());
        ptNotifLtrConfig.setResultCode(ErrorCodeMap.RECORD_FOUND);
        return ptNotifLtrConfig;
    }

    public List<PtNotifLtrConfigLnk> getPtNotifLtrConfigLnks(boolean isInclusion)
    {
        List<PtNotifLtrConfigLnk> ptNotifLtrConfigLnks = new ArrayList<>();
        PtNotifLtrConfigLnk ptNotifLtrConfigLnkPyrLnk = new PtNotifLtrConfigLnk();
        ptNotifLtrConfigLnkPyrLnk.setPtNotifLtrConfigLnkTyp(isInclusion ? MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_PYR_INCL : MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_PYR_EXCL);
        ptNotifLtrConfigLnkPyrLnk.setConfigId(String.valueOf(getPyr().getPyrId()));
        ptNotifLtrConfigLnkPyrLnk.setResultCode(ErrorCodeMap.RECORD_FOUND);

        PtNotifLtrConfigLnk ptNotifLtrConfigLnkPyrGrpLnk = new PtNotifLtrConfigLnk();
        ptNotifLtrConfigLnkPyrGrpLnk.setPtNotifLtrConfigLnkTyp(isInclusion ? MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_PYR_GRP_INCL : MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_PYR_GRP_EXCL);
        ptNotifLtrConfigLnkPyrGrpLnk.setConfigId(String.valueOf(getPyrGrp().getPyrGrpId()));
        ptNotifLtrConfigLnkPyrGrpLnk.setResultCode(ErrorCodeMap.RECORD_FOUND);

        PtNotifLtrConfigLnk ptNotifLtrConfigLnkClnAccntTyp = new PtNotifLtrConfigLnk();
        ptNotifLtrConfigLnkClnAccntTyp.setPtNotifLtrConfigLnkTyp(isInclusion ? MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_CLN_ACCNT_TYP_INCL : MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_CLN_ACCNT_TYP_EXCL);
        ptNotifLtrConfigLnkClnAccntTyp.setConfigId(String.valueOf(getClnAccntTyp().getAccntTypId()));
        ptNotifLtrConfigLnkClnAccntTyp.setResultCode(ErrorCodeMap.RECORD_FOUND);

        PtNotifLtrConfigLnk ptNotifLtrConfigLnkCln = new PtNotifLtrConfigLnk();
        ptNotifLtrConfigLnkCln.setPtNotifLtrConfigLnkTyp(isInclusion ? MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_CLN_INCL : MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_CLN_EXCL);
        ptNotifLtrConfigLnkCln.setConfigId(String.valueOf(getCln().getClnId()));
        ptNotifLtrConfigLnkCln.setResultCode(ErrorCodeMap.RECORD_FOUND);

        PtNotifLtrConfigLnk ptNotifLtrConfigLnkTaxonCd = new PtNotifLtrConfigLnk();
        ptNotifLtrConfigLnkTaxonCd.setPtNotifLtrConfigLnkTyp(isInclusion ? MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_TAXONOMY_CD_INCL : MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_TAXONOMY_CD_EXCL);
        ptNotifLtrConfigLnkTaxonCd.setConfigId(String.valueOf(getTaxonomy().getTaxonomyCd()));
        ptNotifLtrConfigLnkTaxonCd.setResultCode(ErrorCodeMap.RECORD_FOUND);

        PtNotifLtrConfigLnk ptNotifLtrConfigLnkTestTyp = new PtNotifLtrConfigLnk();
        ptNotifLtrConfigLnkTestTyp.setPtNotifLtrConfigLnkTyp(isInclusion ? MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_TEST_TYP_INCL : MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_TEST_TYP_EXCL);
        ptNotifLtrConfigLnkTestTyp.setConfigId(String.valueOf(getTestTyp().getTestTypId()));
        ptNotifLtrConfigLnkTestTyp.setResultCode(ErrorCodeMap.RECORD_FOUND);

        PtNotifLtrConfigLnk ptNotifLtrConfigLnkTest = new PtNotifLtrConfigLnk();
        ptNotifLtrConfigLnkTest.setPtNotifLtrConfigLnkTyp(isInclusion ? MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_TEST_INCL : MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_TEST_EXCL);
        ptNotifLtrConfigLnkTest.setConfigId(String.valueOf(getTest().getTestId()));
        ptNotifLtrConfigLnkTest.setResultCode(ErrorCodeMap.RECORD_FOUND);

        PtNotifLtrConfigLnk ptNotifLtrConfigLnkClnFac = new PtNotifLtrConfigLnk();
        ptNotifLtrConfigLnkClnFac.setPtNotifLtrConfigLnkTyp(isInclusion ? MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_CLN_PRIM_FAC_INCL : MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_CLN_PRIM_FAC_EXCL);
        ptNotifLtrConfigLnkClnFac.setConfigId(String.valueOf(getClnFac().getFacId()));
        ptNotifLtrConfigLnkClnFac.setResultCode(ErrorCodeMap.RECORD_FOUND);

        ptNotifLtrConfigLnks.add(ptNotifLtrConfigLnkPyrLnk);
        ptNotifLtrConfigLnks.add(ptNotifLtrConfigLnkPyrGrpLnk);
        ptNotifLtrConfigLnks.add(ptNotifLtrConfigLnkClnAccntTyp);
        ptNotifLtrConfigLnks.add(ptNotifLtrConfigLnkCln);
        ptNotifLtrConfigLnks.add(ptNotifLtrConfigLnkTaxonCd);
        ptNotifLtrConfigLnks.add(ptNotifLtrConfigLnkTestTyp);
        ptNotifLtrConfigLnks.add(ptNotifLtrConfigLnkTest);
        ptNotifLtrConfigLnks.add(ptNotifLtrConfigLnkClnFac);

        return ptNotifLtrConfigLnks;
    }
}
