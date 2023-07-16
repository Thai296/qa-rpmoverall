package com.overall.utils;

import com.overall.accession.orderProcessing.AccnTestUpdate;
import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.patientportal.dao.IGenericDaoPatientPortal;
import com.xifin.qa.config.Configuration;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccnTestUpdateUtils
{
	private static final Logger LOG = Logger.getLogger(AccnTestUpdateUtils.class);

	private final RemoteWebDriver driver;
	private final Configuration config;
	private final WebDriverWait wait;

	private IGenericDaoPatientPortal patientPortalDao;

	public AccnTestUpdateUtils(RemoteWebDriver driver, Configuration config, WebDriverWait wait)
	{
		this.wait=wait;
		this.driver = driver;
		this.config = config;
		this.patientPortalDao = new DaoManagerPatientPortal(config.getRpmDatabase());
	}

	public boolean isAccnLoaded(String accnId) throws Exception
	{
		boolean flag = false;	
		int i = 0;
		int time = 10;
		AccnTestUpdate accnTestUpdate = new AccnTestUpdate(driver, wait);
				
		String ptFullNameInDb = patientPortalDao.getLnameByAccnId(accnId, null).replaceAll(" ", "") + patientPortalDao.getFnameByAccnId(accnId, null).replaceAll(" ", "");
		String dos = patientPortalDao.getDosByAccnId(accnId, null);

		while (i < time) {	
			if ((accnTestUpdate.accnIdInput().getAttribute("value").trim().equals(accnId)) 					 
					&& (accnTestUpdate.dosInput().getText().trim().equals(dos))
					&& (accnTestUpdate.ptNameInput().getAttribute("title").replaceAll(",","").replaceAll(" ", "").trim().equals(ptFullNameInDb)) )
			{
				flag = true;
				break;
			}else {
				Thread.sleep(1000);
				i++;
			}	
		}

		LOG.info("Checked for loaded accn, accnId="+accnId+", isLoaded="+flag);
		return flag;		
	}	

}
