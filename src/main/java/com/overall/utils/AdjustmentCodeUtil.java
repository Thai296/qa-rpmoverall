package com.overall.utils;

import java.util.ArrayList;
import java.util.List;

import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.rpm.AdjustmentCodeDao;
import com.xifin.qa.dao.rpm.AdjustmentCodeDaoImpl;
import com.xifin.qa.dao.rpm.GeneralLedgerCodeDao;
import com.xifin.qa.dao.rpm.GeneralLedgerCodeDaoImpl;
import com.xifin.qa.dao.rpm.CrossReferenceDao;
import com.xifin.qa.dao.rpm.CrossReferenceDaoImpl;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mbasys.mars.ejb.entity.adjCd.AdjCd;
import com.mbasys.mars.ejb.entity.adjCdTyp.AdjCdTyp;
import com.mbasys.mars.ejb.entity.adjXref.AdjXref;
import com.mbasys.mars.ejb.entity.glCd.GlCd;
import domain.fileMaintenance.adjustmentCode.AdjCode;
import domain.fileMaintenance.adjustmentCode.AdjCodeCrossRef;

public class AdjustmentCodeUtil{
	protected Logger logger;
	private CrossReferenceDao crossReferenceDao;
	private AdjustmentCodeDao adjustmentCodeDao;
	private GeneralLedgerCodeDao generalLedgerCodeDao;

	public AdjustmentCodeUtil(RemoteWebDriver driver, Configuration config) {
		this.adjustmentCodeDao = new AdjustmentCodeDaoImpl(config.getRpmDatabase());
		this.generalLedgerCodeDao = new GeneralLedgerCodeDaoImpl(config.getRpmDatabase());
		this.crossReferenceDao = new CrossReferenceDaoImpl(config.getRpmDatabase());
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver );
	}

	public AdjCode mapToAdjCode(String adjAbbrv) throws Exception {
		AdjCode adjCode = new AdjCode();
		AdjCd adjCd = adjustmentCodeDao.getAdjCdByAdjAbbrv(adjAbbrv);
		if (adjCd == null) return null;
		GlCd glCd = generalLedgerCodeDao.getGlCdByGlCdId(adjCd.getGlCdId());
		String glCode = glCd.getGlCdId() + " : " + glCd.getDescr();
		AdjCdTyp adjCdTyp = adjustmentCodeDao.getAdjCdTypByAdjCdTypId(adjCd.getAdjCdTypId());

		adjCode.setAdjCode(adjCd.getAdjAbbrv());
		adjCode.setAdjCodeType(adjCdTyp.getDescr());
		adjCode.setDesc(adjCd.getDescr());
		adjCode.setNote(adjCd.getCmnt());
		adjCode.setGlCdId(adjCd.getGlCdId());
		adjCode.setGlCode(glCode);
		adjCode.setInActiveAdjCode(adjCd.getIsDeleted());

		return adjCode;
	}

	public List<AdjCodeCrossRef> mapToAdjCodeCrossRef(String adjCd) throws Exception {
		List<AdjCodeCrossRef> lstAdjCodeCrossRef = new ArrayList<AdjCodeCrossRef>(); 

		List<AdjXref> checkAdjXref = crossReferenceDao.getAdjXrefByAdjCdAbbrv(adjCd);
		for (AdjXref adjXref : checkAdjXref) {
			String crossRefDesc = crossReferenceDao.getCrossReferenceDescriptionByXrefId(adjXref.getXrefId()).getDescr();
			AdjCodeCrossRef adjCodeCrossRef = new AdjCodeCrossRef();
			adjCodeCrossRef.setEffDt(adjXref.getEffDt());
			adjCodeCrossRef.setExpDt(adjXref.getExpDt());
			adjCodeCrossRef.setXrefId(adjXref.getXrefId());
			adjCodeCrossRef.setCrossRefDesc(crossRefDesc);
			lstAdjCodeCrossRef.add(adjCodeCrossRef);			
		}

		return lstAdjCodeCrossRef;
	}
}

