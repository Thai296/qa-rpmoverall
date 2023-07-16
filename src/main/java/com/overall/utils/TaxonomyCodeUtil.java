package com.overall.utils;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.overall.fileMaintenance.taxonomyCode.TaxonomyCode;
import com.xifin.qa.dao.rpm.domain.PyrGrpTaxonomyExclPyrGrp;
import com.xifin.qa.dao.rpm.domain.PyrTaxonomyExclPyr;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ConvertUtil;

public class TaxonomyCodeUtil{
	protected Logger logger;
	private RemoteWebDriver driver;
    private static final String DATE_FORMATTED_DEFAULT = "MM/dd/yyyy";
    
	public TaxonomyCodeUtil(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public List<List<String>> allValidDataAllPagesTbl(SeleniumBaseTest b, int pageNumbers, String tblId, List<Integer> cols) throws Exception {
		TaxonomyCode taxonomyCode = new TaxonomyCode(driver);
		List<List<String>> data = new ArrayList<>();
		List<String> values = new ArrayList<>();
		int pageload = 0;
		int oldPageNumbers = pageNumbers;
		while (pageNumbers > 0) {
			int currentRows = driver.findElements(By.xpath("//*[@id='" + tblId + "']/tbody/tr")).size() - 1;
			for (int j = 1; j <= currentRows; j++) {
				values = new ArrayList<>();
				for (int col : cols) {
					values.add(driver.findElement(By.xpath(".//*[@id='" + tblId + "']/tbody/tr[" + (j + 1) + "]/td[" + col + "]")).getText());
				}
				data.add(values);
			}
			pageNumbers -= currentRows;
			pageload += currentRows;
			if ((oldPageNumbers - pageload) >= 1) {
				b.clickHiddenPageObject(taxonomyCode.nextIconInPayorGroupExclutions(), 0);
			}
			assertTrue(b.isElementPresent(taxonomyCode.taxonomyCodePageTitle(), 5), "        Taxonomy Code Page Title is displayed");
		}
		return data;
	}
	
	public List<PyrGrpTaxonomyExclPyrGrp> mapToPyrGrpTaxonomyExclPyrGrpFromUI(SeleniumBaseTest b, int pageNumbers, String tblId,List<Integer>cols) throws Exception{
		List<PyrGrpTaxonomyExclPyrGrp> list = new ArrayList<PyrGrpTaxonomyExclPyrGrp>();
		List<List<String>> pyrGroupsInUI = allValidDataAllPagesTbl(b, pageNumbers, "tbl_payorGroupExclusions", cols);
		for (int i = 0; i < pyrGroupsInUI.size(); i++) {
			PyrGrpTaxonomyExclPyrGrp pyrGrpTaxonomyExclPyrGrp = new PyrGrpTaxonomyExclPyrGrp();
			pyrGrpTaxonomyExclPyrGrp.setGrpNm(pyrGroupsInUI.get(i).get(0));
			pyrGrpTaxonomyExclPyrGrp.setEffDt(ConvertUtil.convertStringToSQLDate(pyrGroupsInUI.get(i).get(1),DATE_FORMATTED_DEFAULT));
			pyrGrpTaxonomyExclPyrGrp.setExpDt(ConvertUtil.convertStringToSQLDate(pyrGroupsInUI.get(i).get(2),DATE_FORMATTED_DEFAULT));
			list.add(pyrGrpTaxonomyExclPyrGrp);
		}
		return list;
	}
	
	public List<PyrGrpTaxonomyExclPyrGrp> mapToPyrGrpTaxonomyExclPyrGrpFromDB(List<PyrGrpTaxonomyExclPyrGrp> pyrGrpTaxonomyExclPyrGrps) throws Exception{
		List<PyrGrpTaxonomyExclPyrGrp> list = new ArrayList<PyrGrpTaxonomyExclPyrGrp>();
		for (PyrGrpTaxonomyExclPyrGrp pyrGrpTaxonomyExclPyrGrp : pyrGrpTaxonomyExclPyrGrps) {
			PyrGrpTaxonomyExclPyrGrp pyrGrpTaxExclPyrGrp = new PyrGrpTaxonomyExclPyrGrp();
			pyrGrpTaxExclPyrGrp.setGrpNm(pyrGrpTaxonomyExclPyrGrp.getGrpNm());
			pyrGrpTaxExclPyrGrp.setEffDt(pyrGrpTaxonomyExclPyrGrp.getEffDt());
			pyrGrpTaxExclPyrGrp.setExpDt(pyrGrpTaxonomyExclPyrGrp.getExpDt());
			list.add(pyrGrpTaxonomyExclPyrGrp);
		}
		return list;
	}
	
	public List<PyrTaxonomyExclPyr> mapToPyrTaxonomyExclPyrFromUI(SeleniumBaseTest b, int pageNumbers, String tblId,List<Integer>cols) throws Exception{
		List<PyrTaxonomyExclPyr> list = new ArrayList<PyrTaxonomyExclPyr>();
		List<List<String>> pyrInUI = allValidDataAllPagesTbl(b, pageNumbers, "tbl_payorGroupExclusions", cols);
		for (int i = 0; i < pyrInUI.size(); i++) {
			PyrTaxonomyExclPyr pyrTaxonomyExclPyr = new PyrTaxonomyExclPyr();
			pyrTaxonomyExclPyr.setPyrId(Integer.parseInt(pyrInUI.get(i).get(0)));
			pyrTaxonomyExclPyr.setName(pyrInUI.get(i).get(1));
			pyrTaxonomyExclPyr.setEffDt(ConvertUtil.convertStringToSQLDate(pyrInUI.get(i).get(2),DATE_FORMATTED_DEFAULT));
			pyrTaxonomyExclPyr.setExpDt(ConvertUtil.convertStringToSQLDate(pyrInUI.get(i).get(3),DATE_FORMATTED_DEFAULT));
			list.add(pyrTaxonomyExclPyr);
		}
		return list;
	}
	
	public List<PyrTaxonomyExclPyr> mapToPyrTaxonomyExclPyrFromDB(List<PyrTaxonomyExclPyr> pyrTaxonomyExclPyrs) throws Exception{
		List<PyrTaxonomyExclPyr> list = new ArrayList<PyrTaxonomyExclPyr>();
		for (PyrTaxonomyExclPyr pyrTaxExclPyr : pyrTaxonomyExclPyrs) {
			PyrTaxonomyExclPyr pyrTaxonomyExclPyr = new PyrTaxonomyExclPyr();
			pyrTaxonomyExclPyr.setPyrId(pyrTaxExclPyr.getPyrId());
			pyrTaxonomyExclPyr.setName(pyrTaxExclPyr.getName());
			pyrTaxonomyExclPyr.setEffDt(pyrTaxExclPyr.getEffDt());
			pyrTaxonomyExclPyr.setExpDt(pyrTaxExclPyr.getExpDt());
			list.add(pyrTaxonomyExclPyr);
		}
		return list;
	}
}
