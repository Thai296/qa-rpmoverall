package com.overall.utils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.rpm.CountryDaoImpl;
import com.xifin.qa.dao.rpm.EmploymentDao;
import com.xifin.qa.dao.rpm.EmploymentDaoImpl;
import com.xifin.qa.dao.rpm.GenderDaoImpl;
import com.xifin.qa.dao.rpm.MaritalStatusDao;
import com.xifin.qa.dao.rpm.GenderDao;
import com.xifin.qa.dao.rpm.MaritalStatusDaoImpl;
import com.xifin.qa.dao.rpm.PatientDao;
import com.xifin.qa.dao.rpm.PatientDaoImpl;
import com.xifin.qa.dao.rpm.PayorDao;
import com.xifin.qa.dao.rpm.PayorDaoImpl;
import com.xifin.qa.dao.rpm.ProcedureCodeDao;
import com.xifin.qa.dao.rpm.ProcedureCodeDaoImpl;
import com.xifin.qa.dao.rpm.RelationshipDao;
import com.xifin.qa.dao.rpm.RelationshipDaoImpl;
import com.xifin.qa.dao.rpm.StateDao;
import com.xifin.qa.dao.rpm.CountryDao;
import com.xifin.qa.dao.rpm.StateDaoImpl;
import com.xifin.qa.dao.rpm.ZipDao;
import com.xifin.qa.dao.rpm.ZipDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mbasys.mars.ejb.entity.country.Country;
import com.mbasys.mars.ejb.entity.employmentStaTyp.EmploymentStaTyp;
import com.mbasys.mars.ejb.entity.genderTyp.GenderTyp;
import com.mbasys.mars.ejb.entity.maritalStatusTyp.MaritalStatusTyp;
import com.mbasys.mars.ejb.entity.pt.Pt;
import com.mbasys.mars.ejb.entity.ptDialysisHistory.PtDialysisHistory;
import com.mbasys.mars.ejb.entity.ptPyr.PtPyr;
import com.mbasys.mars.ejb.entity.ptPyrPhi.PtPyrPhi;
import com.mbasys.mars.ejb.entity.ptPyrSuspendReasonDt.PtPyrSuspendReasonDt;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.state.State;
import com.mbasys.mars.ejb.entity.zip.Zip;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.domain.AllAccnPt;
import com.xifin.qa.dao.rpm.domain.AssociatePatientIdsPatientDemo;
import com.xifin.qa.dao.rpm.domain.PtPyrV2;
import com.xifin.qa.dao.rpm.domain.RpmDailysisPatientHistory;
import com.xifin.utils.ConvertUtil;

import domain.accession.patientDemographics.AllAccessionsForThisPatientTable;
import domain.accession.patientDemographics.AssociatedPatientIDsTable;
import domain.accession.patientDemographics.EmployerInfo;
import domain.accession.patientDemographics.Header;
import domain.accession.patientDemographics.InsuredInfo;
import domain.accession.patientDemographics.NonRPMDialysisPatientHistoryTable;
import domain.accession.patientDemographics.PatientDemographicsRequireFields;
import domain.accession.patientDemographics.PatientInformation;
import domain.accession.patientDemographics.PayorInfo;
import domain.accession.patientDemographics.PayorNotes;
import domain.accession.patientDemographics.RPMDialysisPatientHistory;
import domain.accession.patientDemographics.SuspendedReasonTable;

public class PatientDemographicsUtil {
    private PatientDao patientDao;
	private ZipDao zipDao;
    private PayorDao payorDao;
    private CountryDao countryDao;
	private MaritalStatusDao maritalStatusDao;
	private GenderDao genderDao;
    protected Logger logger;
	private ProcedureCodeDao procedureCodeDao;
	private StateDao stateDao;
	private RelationshipDao relationshipDao;
	private EmploymentDao employmentDao;

	public PatientDemographicsUtil(Configuration config) {
        this.patientDao = new PatientDaoImpl(config.getRpmDatabase());
        this.payorDao = new PayorDaoImpl(config.getRpmDatabase());
		this.zipDao = new ZipDaoImpl(config.getRpmDatabase());
        this.countryDao = new CountryDaoImpl(config.getRpmDatabase());
		this.procedureCodeDao = new ProcedureCodeDaoImpl(config.getRpmDatabase());
		this.stateDao = new StateDaoImpl(config.getRpmDatabase());
		this.maritalStatusDao = new MaritalStatusDaoImpl(config.getRpmDatabase());
		this.genderDao = new GenderDaoImpl(config.getRpmDatabase());
		this.relationshipDao = new RelationshipDaoImpl(config.getRpmDatabase());
		this.employmentDao = new EmploymentDaoImpl(config.getRpmDatabase());
    }

    public List<AssociatedPatientIDsTable> mapToAssociatedPatientId(int ptSeqId) throws Exception {
        List<AssociatedPatientIDsTable> associatedPatientIDsTables = new ArrayList<>();
        List<AssociatePatientIdsPatientDemo> associatePatientIds = patientDao.getAssocPatientsFromPtClnLnkBySeqId(ptSeqId);
        for (AssociatePatientIdsPatientDemo associatePatientIdDB : associatePatientIds) {
            AssociatedPatientIDsTable patientIDsTable = new AssociatedPatientIDsTable();
            patientIDsTable.setSourceType(associatePatientIdDB.getSourceType());
            patientIDsTable.setPatientId(associatePatientIdDB.getPatientId());
            patientIDsTable.setSourceName(associatePatientIdDB.getSourceName());
            patientIDsTable.setLongTempDiagnosis(associatePatientIdDB.getLongTermDiag());
            patientIDsTable.setOrderingPhysicianNPI(associatePatientIdDB.getOrderingPhysNPI() == 0 ? "": String.valueOf(associatePatientIdDB.getOrderingPhysNPI()));
            patientIDsTable.setOrderingPhysicianName(associatePatientIdDB.getOrderingPhysName());
        }
        return associatedPatientIDsTables;
    }

    public Header mapToHeader(String epi, int ssn) throws Exception {
        Header header = new Header();
        Pt patient = null;
        if (epi == null || epi.trim().equals("")) {
            patient = patientDao.getPtBySsn(ssn).get(0);
        } else {
            patient = patientDao.getPtByEpi(epi);
        }
        if (patient == null) {
            header.setEpi(epi);
            header.setPatientSSN(ssn);
            return header;
        }

        header.setEpi(patient.getEpi());
        header.setPatientSSN(patient.getPtSsn());
        return header;
    }

    public PatientDemographicsRequireFields mapPatientDemographicsAtRequireFieldsFromDB(Header header, PatientInformation patientInformation, PayorInfo payorInfo) throws Exception {
    	PatientDemographicsRequireFields ptDemoRequireFields = new PatientDemographicsRequireFields();
		Pt pt = patientDao.getPtByEpi(String.valueOf(header.getEpi()));
		List<PtPyr> ptPyrs = patientDao.getPtPyrByPtSeqIdAndPyrAbbrv(pt.getSeqId(), payorInfo.getPayorId());
		Pyr pyr = payorDao.getPyrByPyrAbbrv(payorInfo.getPayorId());
		ptDemoRequireFields.setEpi(pt.getEpi());
		ptDemoRequireFields.setPatientSSN(pt.getPtSsn());
		ptDemoRequireFields.setPostalCode(pt.getPtZipId());
		ptDemoRequireFields.setCity(pt.getPtCity());
		ptDemoRequireFields.setState(stateDao.getStateByStateId(pt.getPtStId()).get(0).getName());
		ptDemoRequireFields.setCountry(countryDao.getCountryByCntryId(pt.getPtCntryId()).getName());
		ptDemoRequireFields.setPayorPriority(ptPyrs.get(0).getPyrPrio());
		ptDemoRequireFields.setPayorId(payorInfo.getPayorId());
		ptDemoRequireFields.setPayorName(pyr.getName());
		ptDemoRequireFields.setGroupName(ptPyrs.get(0).getGrpName());
		return ptDemoRequireFields;
    }
    
    public PatientDemographicsRequireFields mapPatientDemographicsAtRequireFields(Header header, PatientInformation patientInformation, PayorInfo payorInfo) throws Exception {
    	PatientDemographicsRequireFields ptDemoRequireFields = new PatientDemographicsRequireFields();
		ptDemoRequireFields.setEpi(header.getEpi());
		ptDemoRequireFields.setPatientSSN(header.getPatientSSN());
		ptDemoRequireFields.setPostalCode(patientInformation.getPostalCode());
		ptDemoRequireFields.setCity(patientInformation.getCity());
		ptDemoRequireFields.setState(patientInformation.getState());
		ptDemoRequireFields.setCountry(patientInformation.getCountry());
		ptDemoRequireFields.setPayorPriority(payorInfo.getPayorPriority());
		ptDemoRequireFields.setPayorId(payorInfo.getPayorId());
		ptDemoRequireFields.setPayorName(payorInfo.getPayorName());
		ptDemoRequireFields.setGroupName(payorInfo.getGroupName());
		return ptDemoRequireFields;
    }
    
    public NonRPMDialysisPatientHistoryTable mapNonRPMDialysisPatientHistoryTable(Date dos, String procId) throws Exception{
    	NonRPMDialysisPatientHistoryTable monRPMDialysisPatientHistoryTable = new NonRPMDialysisPatientHistoryTable();
    	
    	PtDialysisHistory ptDialysisHistory = patientDao.getPtDialysisHistoryByDosProcId(dos, procId);
		if (ptDialysisHistory != null) {
    		String descr =  procedureCodeDao.getListProcCdByProcId(ptDialysisHistory.getProcId()).get(0).getDescr();
    		
    		monRPMDialysisPatientHistoryTable.setDos(ptDialysisHistory.getDos());
    		monRPMDialysisPatientHistoryTable.setCompositeRoutine(ptDialysisHistory.getProcId());
    		monRPMDialysisPatientHistoryTable.setDescription(descr.toUpperCase());
    	}
    	
    	return monRPMDialysisPatientHistoryTable;
    }
    
    public List<AllAccessionsForThisPatientTable> mapAllAccessionsForThisPatientTable(List<AllAccnPt> allAccnPts) throws Exception{
    	AllAccessionsForThisPatientTable allAccessionsForThisPatientTable = new AllAccessionsForThisPatientTable();
    	List<AllAccessionsForThisPatientTable> allAccessionsForThisPatientTables = new ArrayList<>();
    	
    	for (AllAccnPt allAccnPt : allAccnPts) {
    		allAccessionsForThisPatientTable.setDos(allAccnPt.getDos());
    		allAccessionsForThisPatientTable.setAccessionId(allAccnPt.getAccnId());
    		allAccessionsForThisPatientTable.setAccessionStatus(allAccnPt.getAccnStatus());
    		allAccessionsForThisPatientTable.setOrderingPhysician(StringUtils.defaultString(allAccnPt.getOtherPhys()));
    		allAccessionsForThisPatientTable.setClientId(allAccnPt.getClnAbbrv());
    		allAccessionsForThisPatientTable.setPrimaryPayor(StringUtils.defaultString(allAccnPt.getPyrAbbrv()));
    		allAccessionsForThisPatientTable.setStatementStatus(StringUtils.defaultString(allAccnPt.getStatementStatus()));
    		allAccessionsForThisPatientTable.setPaid(allAccnPt.getPaid());
    		allAccessionsForThisPatientTable.setAdj(allAccnPt.getAdj());
    		allAccessionsForThisPatientTable.setBalanceDue(allAccnPt.getBalanceDue());
    		allAccessionsForThisPatientTables.add(allAccessionsForThisPatientTable);
		}
    	
		return allAccessionsForThisPatientTables;
    }
	
    public PayorInfo mapPayorInfo(String epi, String pyrAbbrev, Date effDt, boolean mutiRecord,int pyrPrio) throws Exception{
    	PayorInfo payorInfo = new PayorInfo();
    	
    	Pt pt = patientDao.getPtByEpi(String.valueOf(epi));
    	PtPyr ptPyr = patientDao.getPtPyrByPyrAbbrv(pyrAbbrev);
		Pyr pyr = payorDao.getPyrByPyrAbbrv(pyrAbbrev);
		PtPyrV2 ptPyrV2 = new PtPyrV2();
		if (!mutiRecord) {
			ptPyrV2 = patientDao.getPatientFromPtPyrV2BySeqIdAndEffDt(pt.getSeqId(), effDt);
		} else {
			ptPyrV2 = patientDao.getPatientFromPtPyrV2BySeqIdAndEffDtPyrPrio(pt.getSeqId(), effDt,pyrPrio);
		}
		PtPyrPhi ptPyrPhi = patientDao.getPatientFromPtPyrPhiBySeqId(ptPyrV2.getSeqId());
    	
		payorInfo.setPayorPriority(ptPyr.getPyrPrio());
		payorInfo.setPayorId(pyr.getPyrAbbrv());
		payorInfo.setPayorName(pyr.getName());
		payorInfo.setSubscriberID(ptPyrPhi.getSubsId());
		payorInfo.setGroupName(ptPyr.getGrpName());
		payorInfo.setGroupId(ptPyrV2.getGrpId());
		payorInfo.setPlanId(ptPyrV2.getPlanId());
		payorInfo.setCaseId(ptPyrV2.getCaseId());
    	
    	return payorInfo;
    }
    
    public InsuredInfo mapInsuredInfo(String epi, String countryAbbrv, Date effDt, boolean mutiRecord,int pyrPrio) throws Exception{
    	InsuredInfo insuredInfo = new InsuredInfo();
    	
    	Pt pt = patientDao.getPtByEpi(String.valueOf(epi));
    	PtPyrV2 ptPyrV2 = new PtPyrV2();
		if (!mutiRecord) {
			ptPyrV2 = patientDao.getPatientFromPtPyrV2BySeqIdAndEffDt(pt.getSeqId(), effDt);
		} else {
			ptPyrV2 = patientDao.getPatientFromPtPyrV2BySeqIdAndEffDtPyrPrio(pt.getSeqId(), effDt,pyrPrio);
		}
		PtPyrPhi ptPyrPhi = patientDao.getPatientFromPtPyrPhiBySeqId(ptPyrV2.getSeqId());
		String relationship =  relationshipDao.getDataFromRelshpTypByRelshpTypId(ptPyrV2.getRelshpTypId()).getDescr();
		Zip zip = zipDao.getZipById(ptPyrPhi.getInsZipId());
		State state = stateDao.getStateByStateId(zip.getStId()).get(0);
		Country country = countryDao.getCountryByAbbrv(countryAbbrv);
		
		insuredInfo.setRelationship(relationship);
		insuredInfo.setFirstName(ptPyrPhi.getInsFNm());
		insuredInfo.setLastName(ptPyrPhi.getInsLNm());
		insuredInfo.setDateOfBirth(ptPyrPhi.getInsDob());
		insuredInfo.setGender(genderDao.getGenderTypByGenderId(ptPyrV2.getInsSex()).getDescr());
		insuredInfo.setSsn(String.valueOf(ptPyrPhi.getInsSsn()));
		insuredInfo.setAddress1(ptPyrPhi.getInsAddr1());
		insuredInfo.setAddress2(ptPyrPhi.getInsAddr2());
		insuredInfo.setPostalCode(ptPyrPhi.getInsZipId());
		insuredInfo.setCity(ptPyrPhi.getInsCity());
		insuredInfo.setState(state.getName());
		insuredInfo.setCountry(country.getName());
		insuredInfo.setHomePhone(StringUtils.replaceChars(ptPyrPhi.getInsHmPhn(), "()- ", null));
		insuredInfo.setWorkPhone(StringUtils.replaceChars(ptPyrPhi.getInsWrkPhn(), "()- ", null));
		return insuredInfo;
    }
    
    public PayorNotes mapPayorNotes(String epi, Date effDt, boolean mutiRecord,int pyrPrio) throws Exception{
    	PayorNotes payorNotes = new PayorNotes();
    	
    	Pt pt = patientDao.getPtByEpi(String.valueOf(epi));
    	PtPyrV2 ptPyrV2 = new PtPyrV2();
		if (!mutiRecord) {
			ptPyrV2 = patientDao.getPatientFromPtPyrV2BySeqIdAndEffDt(pt.getSeqId(), effDt);
		} else {
			ptPyrV2 = patientDao.getPatientFromPtPyrV2BySeqIdAndEffDtPyrPrio(pt.getSeqId(), effDt,pyrPrio);
		}
		
		payorNotes.setClaimNotes(ptPyrV2.getOtherInfo());
		payorNotes.setInteralNotes(ptPyrV2.getCmnt());
		payorNotes.setOrtherInfo1(ptPyrV2.getOthrInfo1());
		payorNotes.setOrtherInfo2(ptPyrV2.getOthrInfo2());
		payorNotes.setOrtherInfo3(ptPyrV2.getOthrInfo3());
		
    	return payorNotes;
    }
    
    public EmployerInfo mapEmployerInfo(String epi, String countryAbbrv, Date effDt, boolean mutiRecord,int pyrPrio) throws Exception{
    	EmployerInfo employerInfo = new EmployerInfo();
    	
    	Pt pt = patientDao.getPtByEpi(String.valueOf(epi));
    	PtPyrV2 ptPyrV2 = new PtPyrV2();
		if (!mutiRecord) {
			ptPyrV2 = patientDao.getPatientFromPtPyrV2BySeqIdAndEffDt(pt.getSeqId(), effDt);
		} else {
			ptPyrV2 = patientDao.getPatientFromPtPyrV2BySeqIdAndEffDtPyrPrio(pt.getSeqId(), effDt,pyrPrio);
		}
		PtPyrPhi ptPyrPhi = patientDao.getPatientFromPtPyrPhiBySeqId(ptPyrV2.getSeqId());
		Zip zip = zipDao.getZipById(ptPyrPhi.getEmployerZipId());
		State state = stateDao.getStateByStateId(zip.getStId()).get(0);
		EmploymentStaTyp employmentStaTyp  = employmentDao.getEmploymentStaTypByEmploymentStaTypId(ptPyrV2.getEmploymentStaTypId());
		String employmentStatus = employmentStaTyp.getAbbrev() + " - " + employmentStaTyp.getDescr();
		Country country = countryDao.getCountryByAbbrv(countryAbbrv);
		
		employerInfo.setEmployerName(ConvertUtil.standardizeString(ptPyrPhi.getEmployerName()));
		employerInfo.setEmployerStatus(employmentStatus);
		employerInfo.setAddress1(ConvertUtil.standardizeString(ptPyrPhi.getEmployerAddr1()));
		employerInfo.setAddress2(ConvertUtil.standardizeString(ptPyrPhi.getEmployerAddr2()));
		employerInfo.setPostalCode(ptPyrPhi.getEmployerZipId());
		employerInfo.setCity(ptPyrPhi.getEmployerCity());
		employerInfo.setState(ConvertUtil.standardizeString(state.getName()));
		employerInfo.setCountry(country.getName());
		employerInfo.setWorkPhone(ptPyrPhi.getEmployerPhn());
		employerInfo.setFax(StringUtils.replaceChars(ptPyrPhi.getEmployerFax(), "()- " , null));

		return employerInfo;
    }

	public PatientInformation mapPatientInformationSection(Pt pt, String countryAbrrv) throws XifinDataAccessException, XifinDataNotFoundException, Exception {
		PatientInformation patientInformation = new PatientInformation();
		
		Zip zip = zipDao.getZipById(pt.getPtZipId());
		State state = stateDao.getStateByStateId(zip.getStId()).get(0);
		Country country = countryDao.getCountryByAbbrv(countryAbrrv);
		MaritalStatusTyp maritalStatusTyp = maritalStatusDao.getMaritalStatusTypByMaritalStatusTypId(pt.getPtMaritalStaId());
		GenderTyp gender = genderDao.getGenderTypByGenderId(pt.getPtSex());
		
		patientInformation.setLastName(pt.getPtLNm());
		patientInformation.setFirstName(pt.getPtFNm());
		patientInformation.setDateOfBirth(pt.getPtDob());
		patientInformation.setGender(gender.getDescr());
		patientInformation.setMaritalStatus(maritalStatusTyp.getAbbrev() + " - " + maritalStatusTyp.getDescr());
		patientInformation.setNotes(pt.getComments());
		patientInformation.setDosOfMostMSPFrom(pt.getMspFormDos());
		patientInformation.setAddress1(pt.getPtAddr1());
		patientInformation.setAddress2(pt.getPtAddr2());
		patientInformation.setHomePhone(StringUtils.replaceChars(pt.getPtHmPhn(), "()- ", null));
		patientInformation.setWorkPhone(StringUtils.replaceChars(pt.getPtWrkPhn(), "()- ", null));
		patientInformation.setEmail(pt.getPtEmail());
		patientInformation.setPostalCode(zip.getZipId());
		patientInformation.setCity(zip.getCtyNm());
		patientInformation.setCountry(country.getName());
		patientInformation.setState(state.getName());
		
		return patientInformation;
	}
	
    public SuspendedReasonTable mapSuspendedReasonTable(String epi) throws Exception {
        SuspendedReasonTable suspendedReasonTable = new SuspendedReasonTable();

        Pt pt = patientDao.getPtByEpi(String.valueOf(epi));
        PtPyrSuspendReasonDt ptPyrSuspendReasonDt = patientDao.getPtPyrSuspendReasonDtByPtSeqId(pt.getSeqId()).get(0);
        String reason = patientDao.getPtSuspendReasonTypBySeqId(ptPyrSuspendReasonDt.getPtSuspendReasonTypId()).getDescr();

        suspendedReasonTable.setDate(ptPyrSuspendReasonDt.getReasonDt());
        suspendedReasonTable.setReason(reason);
        suspendedReasonTable.setNotes(ptPyrSuspendReasonDt.getNote());
        suspendedReasonTable.setUser(ptPyrSuspendReasonDt.getCreateUserId());
        suspendedReasonTable.setFix(ptPyrSuspendReasonDt.getFixDt() != null);
        suspendedReasonTable.setDeleted(false);

        return suspendedReasonTable;
    }
	
	public List<RPMDialysisPatientHistory> mapToRpmDialysisPatientHistory(Date fromDate, Date throughDate, String epi) throws XifinDataAccessException, XifinDataNotFoundException, Exception {
	    Pt pt = patientDao.getPtByEpi(epi);
	    List<RPMDialysisPatientHistory> dialysisPatientHistories = new ArrayList<RPMDialysisPatientHistory>();
	    List<RpmDailysisPatientHistory> dialysisHistoriesDb = null;
	    try {
	        dialysisHistoriesDb = patientDao.getPtDialysisHistoryByDosAndPtSeqId(fromDate, throughDate, pt.getSeqId());
		} catch (XifinDataNotFoundException e) {
	        dialysisHistoriesDb = new ArrayList<>();
        }
	    for (RpmDailysisPatientHistory ptDialysisHistory : dialysisHistoriesDb) {
	        RPMDialysisPatientHistory dialysisPatientHistory = new RPMDialysisPatientHistory();
	        dialysisPatientHistory.setAccessionId(ptDialysisHistory.getAccnId());
	        dialysisPatientHistory.setDos(ptDialysisHistory.getDos());
	        dialysisPatientHistory.setCompositeRoutine(ptDialysisHistory.getCompositeRoutine());
	        dialysisPatientHistory.setDescription(ptDialysisHistory.getDescription());
            dialysisPatientHistory.setModifiers(ptDialysisHistory.getModifiers() == null ? "" : ptDialysisHistory.getModifiers());	     
	        dialysisPatientHistories.add(dialysisPatientHistory);
        }
	    
	    return dialysisPatientHistories;
	}

    public List<NonRPMDialysisPatientHistoryTable> mapToNonRpmDialysisPatientHistory(String epi) throws Exception {
        Pt pt = patientDao.getPtByEpi(epi);
        List<NonRPMDialysisPatientHistoryTable> nonRPMDialysisPatientHistoryTables = new ArrayList<NonRPMDialysisPatientHistoryTable>();
        List<PtDialysisHistory> ptDialysisHistoriesDb = null;
        try {
            ptDialysisHistoriesDb = patientDao.getPtDialysisHistoryByPtSeqId(pt.getSeqId());
        } catch (XifinDataNotFoundException e) {
            ptDialysisHistoriesDb = new ArrayList<>();
        }
        for (PtDialysisHistory ptDialysisHistory : ptDialysisHistoriesDb) {
            NonRPMDialysisPatientHistoryTable dialysisPatientHistory = new NonRPMDialysisPatientHistoryTable();
            dialysisPatientHistory.setDos(ptDialysisHistory.getDos());
            dialysisPatientHistory.setCompositeRoutine(ptDialysisHistory.getProcId());
            dialysisPatientHistory.setDescription(procedureCodeDao.getListProcCdByProcId(ptDialysisHistory.getProcId()).get(0).getDescr());
            nonRPMDialysisPatientHistoryTables.add(dialysisPatientHistory);
        }        
        return nonRPMDialysisPatientHistoryTables;
    }
}
