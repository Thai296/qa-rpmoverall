package com.overall.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPmt.AccnPmt;
import com.mbasys.mars.ejb.entity.dlyRcpt.DlyRcpt;
import com.mbasys.mars.patient.PatientDetail;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.PmtMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.AccessionDao;
import com.xifin.qa.dao.rpm.PatientDao;
import com.xifin.qa.dao.rpm.SystemDao;
import com.xifin.qa.dao.rpm.domain.ReceiptLog;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class DailyReceiptUtils {
	
	private static final String EMPTY = "";
	private static final String CREATE_NEW = "create new";
	
	protected Logger logger;
	private SystemDao systemDao;
	private PatientDao patientDao;
	private RemoteWebDriver driver;	
	private AccessionDao accessionDao;

	public DailyReceiptUtils(RemoteWebDriver driver, SystemDao systemDao, AccessionDao accessionDao, PatientDao patientDao) {
		this.driver = driver;
		this.systemDao = systemDao;
		this.patientDao = patientDao;
		this.accessionDao = accessionDao;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	private String concatPatientName(final String firstName, final String lastName) {
	    if (firstName != null) {
	        return lastName + ", " + firstName;
	    } else if (lastName != null) {
	        return lastName;
	    } else {
	        return EMPTY;
	    }
	}

	public List<ReceiptLog> mapToReceiptLog(String dailyReceiptId, String facAbbrv, String userId, String type) throws Exception {
		List<ReceiptLog> receiptLogs = new ArrayList<ReceiptLog>();

		List<AccnPmt> accnPmts  = new ArrayList<>();
		if (type.equals(CREATE_NEW)) {
			String sysSS40 = systemDao.getSystemSetting(SystemSettingMap.SS_SECURITY_BY_FACILITY).getDataValue();
			if (sysSS40.equals("1")) {
				accnPmts = accessionDao.getAccnPmtByUserIdAndFacAbbrevAndAccnIdInUserFacAccess(facAbbrv, userId);
			} else {
				accnPmts = accessionDao.getAccnPmtByUserIdAndFacAbbrev(facAbbrv, userId);
			}
		} else {
			DlyRcpt dlyRcpt = accessionDao.getDlyRcptByAbbrv(dailyReceiptId);
			accnPmts = accessionDao.getAccnPmtByDailyReceiptId(dlyRcpt.getDlyRcptId());
		}

		if (accnPmts != null && accnPmts.size() > 0) {
			for (AccnPmt accnPmt : accnPmts) {
				Accn accn = accessionDao.getAccn(accnPmt.getAccnId());
				final PatientDetail ptDetailObj;
				/* Check if we can get a patient from the accn **/
				if (0 != accn.getPtSeqId()) {
					// get a patient detail obj to get the correct ptId
	                try {
	                    ptDetailObj = new PatientDetail(patientDao.getPtBySeqId(accn.getPtSeqId()));
	                    ptDetailObj.setPtIdTyp(MiscMap.PT_ID_TYP_EPI);
	                } catch (Exception e) {
	                    throw new XifinDataNotFoundException("Failed to get patient EPI detail, ptSeqId=" + accn.getPtSeqId() + ", clnId=" + accn.getClnId(), e);
	                }
				} else {
					ptDetailObj = new PatientDetail();
				}

				ReceiptLog receiptLog = new ReceiptLog();
				receiptLog.setPmtSeq(accnPmt.getPmtSeq());
				receiptLog.setAccnId(accn.getAccnId());
				receiptLog.setEpi(defaultIfNull(ptDetailObj.getPtId(), EMPTY));
				receiptLog.setPatientFullName(concatPatientName(accn.getPtFNm(), accn.getPtLNm()).trim());

				if (accnPmt.getPmtTypId() == PmtMap.PMT_TYP_CASH || accnPmt.getPmtTypId() == PmtMap.PMT_TYP_CASH_DISCOUNT) {
					receiptLog.setCashAmount(accnPmt.getPaidAmtAsMoney().toDisplayString());
					receiptLog.setCheckAmount(EMPTY);
					receiptLog.setCheckNumber(EMPTY);

				} else if (accnPmt.getPmtTypId() == PmtMap.PMT_TYP_CHECK) {
					receiptLog.setCashAmount(EMPTY);
					receiptLog.setCheckAmount(accnPmt.getPaidAmtAsMoney().toDisplayString());
					receiptLog.setCheckNumber(defaultIfNull(accnPmt.getChkNum(), EMPTY));
				}
				receiptLogs.add(receiptLog);
			}
		}

		return receiptLogs;
	}
}
