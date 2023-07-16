package com.overall.utils;

import com.mbasys.mars.ejb.entity.eligResp.EligResp;
import com.overall.utils.EligibilityConstants.ServiceTypeCode;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.RpmDao;

import domain.fileMaintenance.EligibilityConfiguration.EligibilityTranslation;

public class EligibilityUtils
{
	private final RpmDao rpmDao;
	private final String dbEnv; 

	public EligibilityUtils(RpmDao rpmDao, String dbEnv)
	{
	    this.rpmDao = rpmDao;
	    this.dbEnv = dbEnv;
	   
	}

	public EligResp mapToEligResp(EligibilityTranslation eligTranslation)
	        throws XifinDataNotFoundException, XifinDataAccessException
	{
		
	    EligResp eligResp = new EligResp();
	    if (eligTranslation.getPyrId() != null)
	    {
	    	eligResp.setPyrId(rpmDao.getPyrByPyrAbbrv(dbEnv, eligTranslation.getPyrId()).getPyrId());
	    }
	    if (eligTranslation.getNewPyrId() !=null)
	    {
	    	eligResp.setNewPyrId(rpmDao.getPyrByPyrAbbrv(dbEnv, eligTranslation.getNewPyrId()).getPyrId());
	    }
	    if (eligTranslation.getMatchType() != null)
	    {
	    	eligResp.setEligRespMatchTypId(rpmDao.getEligRespMatchTypByDescr(dbEnv, eligTranslation.getMatchType()).getTypId());	
	    }
	    if (eligTranslation.getBenefitType() != null)
	    {
	    eligResp.setEligRespBenefitTypId(rpmDao.getEligRespBenefitTypByDescr(dbEnv, eligTranslation.getBenefitType()).getTypId());
	    }
	    if (eligTranslation.getResponse() != null)
	    {
	    eligResp.setResp(eligTranslation.getResponse());
	    }
	    eligResp.setPrio(eligTranslation.getPrio());
	    if (eligTranslation.getServiceTypeCode() != null)
	    {
	    eligResp.setSvcTypCd(ServiceTypeCode.getByDescription(eligTranslation.getServiceTypeCode()).getCode());
	    }
		if (eligTranslation.getIsBypassUnknownResponseErrors() != null)
		{
	    eligResp.setIsAllowUnmapped(eligTranslation.getIsBypassUnknownResponseErrors());
		}
		if (eligTranslation.getIsCheckElig() != null)
		{
			eligResp.setIsCheckElig(eligTranslation.getIsCheckElig());
		}
		if (eligTranslation.getEligSvcName() != null)
		{
		   eligResp.setEligSvcId(rpmDao.getEligSvcByEligSvcDesrc(dbEnv, eligTranslation.getEligSvcName()).getEligSvcId());
		}
		if (eligTranslation.getIsAllowSecondaryTranslation() != null)
		{
			eligResp.setIsAllowSecondary(eligTranslation.getIsAllowSecondaryTranslation());
		}
		if (eligTranslation.getSubIdType() != null)
		{
			eligResp.setEligRespSubsRefTypId(rpmDao.getEligRespSubsRefTyById(dbEnv, eligTranslation.getSubIdType()).getTypId());
		}
	    
	    // eligResp.set
	    // You can do it!
	    
	    return eligResp;
	}

}
