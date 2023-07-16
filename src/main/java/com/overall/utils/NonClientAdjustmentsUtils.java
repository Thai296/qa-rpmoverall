package com.overall.utils;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.adjCd.AdjCd;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.overall.payment.adjustments.nonClientAdjustments.NonClientAdjustments;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.rpm.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NonClientAdjustmentsUtils
{
	private static final Logger LOG = Logger.getLogger(NonClientAdjustmentsUtils.class);
	private final RemoteWebDriver driver;
	private final AccessionDao accessionDao;
	private final PayorDao payorDao;
	private final ClientDao clientDao;
	private final AdjustmentCodeDao adjustmentCodeDao;

	public NonClientAdjustmentsUtils(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.accessionDao=new AccessionDaoImpl(config.getRpmDatabase());
		this.payorDao=new PayorDaoImpl(config.getRpmDatabase());
		this.clientDao=new ClientDaoImpl(config.getRpmDatabase());
		this.adjustmentCodeDao=new AdjustmentCodeDaoImpl(config.getRpmDatabase());
	}

	//Enter text to text input field and tab out (if tabOutAfter = true) it
	public void inputText(WebElement wb, String textInput, boolean tabOutAfter) throws Exception{
		wb.clear();
		wb.sendKeys(textInput);

		if (tabOutAfter){
			wb.sendKeys(Keys.TAB);
		}
		
		Thread.sleep(1000);
		
		LOG.info("        Entered "+ textInput+" to "+ wb.getTagName() + " field.");
	}
	
	public void creatUploadNonClnAdjFile(List<String> accnID,float adjCost, String pathCreateFile) throws Exception{
		String headerInput = "Accn Level Compare for Submitted Payor:  PTCONTRACTED     from 10/01/2000 to 12/10/2017 based on Date of Service     Compare Fee Schedule:  KIN     Fields to Compare to FS$:   Copay + Deductible + Coinsurance     Client Lab State:       Exclude Accns w/ Adj Code:             Adj Code:  PTPROV     Adj Comment:   TEST KIN Adj       Print:  N,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\r\n"+
							"Client ID,Client Lab State,Accn ID,DOS,Final Rpt Dt,Adj Payor ID,Primary Payor Name,Primary Payor Group,Proc Cd,Mod1,Units Billed,Billed Amt,Paid Amt,Ded Amt,Copay Amt,Pt Resp Amt,Last Pmt Dt,Due Amt w-Bulk,FS ID,FS Amt,Compare Amt, Compare $ > FS $ Flag,Pt Paid Amt,Last Pt Pmt Dt,Adj Amt,Adj CD,Adj Cmnt,Print Flag,New Balance Amt,Process Adj Flag\r\n";
							
		List<String> lines = new ArrayList<>();
		lines.add(headerInput);
		for (String accn : accnID) {
			AdjCd accAdj = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo();
			Accn accnList = accessionDao.getAccn(accn);
			float balance = Float.valueOf(String.valueOf(accnList.getDueAmtAsMoney()))+adjCost;
			Pyr pyrs = payorDao.getPyrFromPyrDtAndPyr();
			String temp = clientDao.getClnWithSubmSvcFromCln().getClnAbbrev()+",SC,"+accn+",10/01/2015,10/08/2015,"+pyrs.getPyrAbbrv()+",CIGNA HEALTHCARE,Contracted,Accn,,1,68512.08,442.09,0.00,189.50,0.00,12/07/2015,235.81,KIN,0.00,189.50,Y,0.00,,"+ adjCost +","+accAdj.getAdjAbbrv()+",,N,"+ balance +",y";
			lines.add(temp);	
		}
		Path file = Paths.get(pathCreateFile);
		Files.write(file, lines, Charset.forName("UTF-8"));
		Thread.sleep(2000);
	}
	
	public void createEmptyFile(String pathCreateFile) throws Exception{
		List<String> lines = new ArrayList<>();
		Path file = Paths.get(pathCreateFile);
		Files.write(file, lines, Charset.forName("UTF-8"));
	}
	
	public void creatUploadNewBalanceAmtIsNotEqualNew$(List<String> accnID,float adjCost, String pathCreateFile) throws Exception{
  String headerInput = "Accn Level Compare for Submitted Payor:  PTCONTRACTED     from 10/01/2000 to 12/10/2017 based on Date of Service     Compare Fee Schedule:  KIN     Fields to Compare to FS$:   Copay + Deductible + Coinsurance     Client Lab State:       Exclude Accns w/ Adj Code:             Adj Code:  PTPROV     Adj Comment:   TEST KIN Adj       Print:  N,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\r\n"+
       "Client ID,Client Lab State,Accn ID,DOS,Final Rpt Dt,Adj Payor ID,Primary Payor Name,Primary Payor Group,Proc Cd,Mod1,Units Billed,Billed Amt,Paid Amt,Ded Amt,Copay Amt,Pt Resp Amt,Last Pmt Dt,Due Amt w-Bulk,FS ID,FS Amt,Compare Amt, Compare $ > FS $ Flag,Pt Paid Amt,Last Pt Pmt Dt,Adj Amt,Adj CD,Adj Cmnt,Print Flag,New Balance Amt,Process Adj Flag\r\n";
       
  List<String> lines = new ArrayList<>();
  lines.add(headerInput);
  for (String accn : accnID) {
   AdjCd accAdj = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo();
   Accn accnList = accessionDao.getAccn(accn);
   float balance = Float.valueOf(String.valueOf(accnList.getDueAmtAsMoney()));
   Pyr pyrs = payorDao.getPyrFromPyrDtAndPyr();
   String temp = clientDao.getClnWithSubmSvcFromCln().getClnAbbrev()+",SC,"+accn+",10/01/2015,10/08/2015,"+pyrs.getPyrAbbrv()+",CIGNA HEALTHCARE,Contracted,Accn,mod123,10,35,238.81,0.00,0.00,0.00,12/07/2015,0.00,KIN,0.00,102.54,N,0.00,,"+ adjCost +","+accAdj.getAdjAbbrv()+",,N,"+ balance +",Y";
   lines.add(temp); 
  }
  Path file = Paths.get(pathCreateFile);
  Files.write(file, lines, Charset.forName("UTF-8"));
 }

	public void creatUploadWithNotSpecialAccn(List<String> accnID,float adjCost, String pathCreateFile,String accnTitle) throws Exception{
  String headerInput = "Accn Level Compare for Submitted Payor:  PTCONTRACTED     from 10/01/2000 to 12/10/2017 based on Date of Service     Compare Fee Schedule:  KIN     Fields to Compare to FS$:   Copay + Deductible + Coinsurance     Client Lab State:       Exclude Accns w/ Adj Code:             Adj Code:  PTPROV     Adj Comment:   TEST KIN Adj       Print:  N,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\r\n"+
       "Client ID,Client Lab State,Accn ID,DOS,Final Rpt Dt,Adj Payor ID,Primary Payor Name,Primary Payor Group,Proc Cd,Mod1,Units Billed,Billed Amt,Paid Amt,Ded Amt,Copay Amt,Pt Resp Amt,Last Pmt Dt,Due Amt w-Bulk,FS ID,FS Amt,Compare Amt, Compare $ > FS $ Flag,Pt Paid Amt,Last Pt Pmt Dt,Adj Amt,Adj CD,Adj Cmnt,Print Flag,New Balance Amt,Process Adj Flag\r\n";
       
  List<String> lines = new ArrayList<>();
  lines.add(headerInput);
  for (String accn : accnID) {
   AdjCd accAdj = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo();
   Accn accnList = accessionDao.getAccn(accn);
   float balance = Float.valueOf(String.valueOf(accnList.getDueAmtAsMoney()))+adjCost;
   Pyr pyrs = payorDao.getPyrFromPyrDtAndPyr();
   String temp = clientDao.getClnWithSubmSvcFromCln().getClnAbbrev()+",SC,"+accn+",10/01/2015,10/08/2015,"+pyrs.getPyrAbbrv()+",CIGNA HEALTHCARE,Contracted,"+accnTitle+",,1,68512.08,442.09,0.00,189.50,0.00,12/07/2015,235.81,KIN,0.00,189.50,Y,0.00,,"+ adjCost +","+accAdj.getAdjAbbrv()+",,N,"+ balance +",y";
   lines.add(temp); 
  }
  Path file = Paths.get(pathCreateFile);
  Files.write(file, lines, Charset.forName("UTF-8"));
 }

	public void creatUploadNonClnAdjInvalidAccnIDFile(List<String> accnInvalidID,List<Float> adjCost,List<Float> blances, String pathCreateFile) throws Exception{
		String headerInput = "Accn Level Compare for Submitted Payor:  PTCONTRACTED     from 10/01/2000 to 12/10/2017 based on Date of Service     Compare Fee Schedule:  KIN     Fields to Compare to FS$:   Copay + Deductible + Coinsurance     Client Lab State:       Exclude Accns w/ Adj Code:             Adj Code:  PTPROV     Adj Comment:   TEST KIN Adj       Print:  N,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\r\n"+
							"Client ID,Client Lab State,Accn ID,DOS,Final Rpt Dt,Adj Payor ID,Primary Payor Name,Primary Payor Group,Proc Cd,Mod1,Units Billed,Billed Amt,Paid Amt,Ded Amt,Copay Amt,Pt Resp Amt,Last Pmt Dt,Due Amt w-Bulk,FS ID,FS Amt,Compare Amt, Compare $ > FS $ Flag,Pt Paid Amt,Last Pt Pmt Dt,Adj Amt,Adj CD,Adj Cmnt,Print Flag,New Balance Amt,Process Adj Flag\r\n";
							
		List<String> lines = new ArrayList<>();
		lines.add(headerInput);
		int i=0;
		for (String accn : accnInvalidID) {
			AdjCd accAdj = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo();
			Pyr pyrs = payorDao.getPyrFromPyrDtAndPyr();
			String temp = clientDao.getClnWithSubmSvcFromCln().getClnAbbrev()+",SC,"+accn+",10/01/2015,10/08/2015,"+pyrs.getPyrAbbrv()+",CIGNA HEALTHCARE,Contracted,Accn,,1,68512.08,442.09,0.00,189.50,0.00,12/07/2015,235.81,KIN,0.00,189.50,Y,0.00,,"+adjCost.get(i)+","+accAdj.getAdjAbbrv()+",,N,"+blances.get(i)+",y";
			lines.add(temp);
			i++;
		}
		Path file = Paths.get(pathCreateFile);
		Files.write(file, lines, Charset.forName("UTF-8"));
	}
	
	public void creatUploadNonClnAdjMultiInfoFile(List<String> accnID,List<String> adjPyrID, List<String> adjCD, List<String> procCD, List<String> mod1, List<Integer> unit, List<Float> bill,List<Float> adjCost, List<String> adjComment,List<Float> anticipatedNew, String pathCreateFile) throws Exception{
		String headerInput = "Accn Level Compare for Submitted Payor:  PTCONTRACTED     from 10/01/2000 to 12/10/2017 based on Date of Service     Compare Fee Schedule:  KIN     Fields to Compare to FS$:   Copay + Deductible + Coinsurance     Client Lab State:       Exclude Accns w/ Adj Code:             Adj Code:  PTPROV     Adj Comment:   TEST KIN Adj       Print:  N,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\r\n"+
							"Client ID,Client Lab State,Accn ID,DOS,Final Rpt Dt,Adj Payor ID,Primary Payor Name,Primary Payor Group,Proc Cd,Mod1,Units Billed,Billed Amt,Paid Amt,Ded Amt,Copay Amt,Pt Resp Amt,Last Pmt Dt,Due Amt w-Bulk,FS ID,FS Amt,Compare Amt, Compare $ > FS $ Flag,Pt Paid Amt,Last Pt Pmt Dt,Adj Amt,Adj CD,Adj Cmnt,Print Flag,New Balance Amt,Process Adj Flag\r\n";
							
		List<String> lines = new ArrayList<>();
		lines.add(headerInput);
		int i=0;
		
		for (String accn : accnID) {
			String temp = clientDao.getClnWithSubmSvcFromCln().getClnAbbrev()+",SC,"+accn+",10/01/2015,10/08/2015,"+adjPyrID.get(i)+",CIGNA HEALTHCARE,Contracted,"+procCD.get(i)+","+mod1.get(i)+","+unit.get(i)+","+bill.get(i)+",442.09,0.00,189.50,0.00,12/07/2015,235.81,KIN,0.00,189.50,Y,0.00,,"+adjCost.get(i)+","+adjCD.get(i)+","+adjComment.get(i)+",N,"+anticipatedNew.get(i)+",y";
			lines.add(temp);	
			i++;
		}
		Path file = Paths.get(pathCreateFile);
		Files.write(file, lines, Charset.forName("UTF-8"));
	}
	
	public void creatUploadNonClnAdjInvalidPayorFile(List<String> accnID,float adjCost, String pathCreateFile,String pyrs) throws Exception{
		String headerInput = "Accn Level Compare for Submitted Payor:  PTCONTRACTED     from 10/01/2000 to 12/10/2017 based on Date of Service     Compare Fee Schedule:  KIN     Fields to Compare to FS$:   Copay + Deductible + Coinsurance     Client Lab State:       Exclude Accns w/ Adj Code:             Adj Code:  PTPROV     Adj Comment:   TEST KIN Adj       Print:  N,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\r\n"+
				"Client ID,Client Lab State,Accn ID,DOS,Final Rpt Dt,Adj Payor ID,Primary Payor Name,Primary Payor Group,Proc Cd,Mod1,Units Billed,Billed Amt,Paid Amt,Ded Amt,Copay Amt,Pt Resp Amt,Last Pmt Dt,Due Amt w-Bulk,FS ID,FS Amt,Compare Amt, Compare $ > FS $ Flag,Pt Paid Amt,Last Pt Pmt Dt,Adj Amt,Adj CD,Adj Cmnt,Print Flag,New Balance Amt,Process Adj Flag\r\n";
		List<String> lines = new ArrayList<>();
		lines.add(headerInput);
		
		for (String accn : accnID) {
		AdjCd accAdj = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo();
		Accn accnList = accessionDao.getAccn(accn);
		float balance = Float.valueOf(String.valueOf(accnList.getDueAmtAsMoney()))+adjCost;
		String temp = clientDao.getClnWithSubmSvcFromCln().getClnAbbrev()+",SC,"+accn+",10/01/2015,10/08/2015,"+pyrs+",CIGNA HEALTHCARE,Contracted,Accn,,1,68512.08,442.09,0.00,189.50,0.00,12/07/2015,235.81,KIN,0.00,189.50,Y,0.00,,"+ adjCost +","+accAdj.getAdjAbbrv()+",,N,"+ balance +",y";
		lines.add(temp);
		}
		Path file = Paths.get(pathCreateFile);
		Files.write(file, lines, Charset.forName("UTF-8"));
	}

	public boolean verifyAllRowIsDisplayValidData(NonClientAdjustments nonClientAdjustments) throws Exception {
		int totalRows = driver.findElements(By.xpath("//*[@id='tbl_nonClientAdjustment']/tbody/tr")).size() -1;
		System.out.println("totalRows = " +totalRows);
		if (totalRows < 1) return false; // No data to verify

		for (int i = 1; i <= totalRows; i++) {
			String accnID = nonClientAdjustments.getTextCellTableNonClientAdj(2, i+1);
			Accn accInfoList = accessionDao.getAccn(accnID);
			float currentDollar = getNumberFromText(nonClientAdjustments.getTextCellTableNonClientAdj(13, i+1));
			
			if(currentDollar != getNumberFromText(String.valueOf(accInfoList.getDueAmtAsMoney()))) {
				System.out.println("currentDollar " + i + " = " + currentDollar);
				System.out.println("accInfoList.get(25) " + i + " = " + getNumberFromText(String.valueOf(accInfoList.getDueAmtAsMoney())));
				return false; // Compare Current $ == Due_Amount in database or not
			}
			
			float newDollar = getNumberFromText(nonClientAdjustments.getTextCellTableNonClientAdj(14, i+1));
			float adjDollar = getNumberFromText(nonClientAdjustments.getTextCellTableNonClientAdj(9, i+1));		
			
			String sumAmt = String.format("%.2f", (adjDollar+currentDollar));
			
			if(newDollar != Float.valueOf(sumAmt)) {
				System.out.println("newDollar " + i + " = " + newDollar);
				System.out.println("sumAmt " + i + " = " + sumAmt);
				return false;
			}
		}
		
		return true;
	}

	public boolean verifyAllRowIsSavedCorrect(List<String> listAccnIDInTable, List<String> listNewDollars) throws Exception {
		
		int i=0;
		for (String accnID : listAccnIDInTable) {
			Accn accInfoList = accessionDao.getAccn(accnID);
			if(getNumberFromText(listNewDollars.get(i)) != getNumberFromText(String.valueOf(accInfoList.getDueAmtAsMoney()))) return false; // Compare New $ == Due_Amount in database or not
			i++;
		}
		return true;
	}
	public String formatDecimalPoint(Double number){
		if (number == 0) {
			return "0.00";
		} else{
			DecimalFormat Formatter = new DecimalFormat("###,###,###.00");
			return Formatter.format(number);
		}
	}

	public void deleteFile(String filePath){
		File file = new File(filePath);
		file.delete();
		LOG.info("      " + filePath + " was deleted.");
	}
	
	public float getNumberFromText(String textNumber){
		return Float.parseFloat(textNumber.replace(",", ""));
	}
	
	public void creatNonClnAdjUploadMiniReqFile(List<String> accnID, List<String> adjPyrID, List<String> adjCD, List<String> billAmt, List<Float> adjAmt, List<Float> anticipatedNewAmt, String pathCreateFile) throws Exception{
		String headerStr = "Header,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\r\n"+
							"Accn ID,Adj Payor ID,Adj CD,Billed Amt,Adj Amt,New Balance Amt\r\n";
		String footerStr = "Footer,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
							
		List<String> lines = new ArrayList<>();
		lines.add(headerStr);
		int i=0;
		
		for (String accn : accnID) {
			String temp = accn+","+adjPyrID.get(i)+","+adjCD.get(i)+","+billAmt.get(i)+","+adjAmt.get(i)+","+anticipatedNewAmt.get(i);
			lines.add(temp);	
			i++;
		}
		lines.add(footerStr);
		
		Path file = Paths.get(pathCreateFile);
		Files.write(file, lines, Charset.forName("UTF-8"));
	}	
	
	public void creatNonClnAdjUploadFileWithProcCode(List<String> accnID, List<String> adjPyrID, List<String> procCd, int units, List<String> adjCD, List<String> billAmt, List<Float> adjAmt, List<Float> anticipatedNewAmt, String pathCreateFile) throws Exception{
		String headerStr = "Header,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\r\n"+
							"Accn ID,Adj Payor ID,Proc Cd,Units Billed,Adj CD,Billed Amt,Adj Amt,New Balance Amt\r\n";
		String footerStr = "Footer,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
							
		List<String> lines = new ArrayList<>();
		lines.add(headerStr);
		int i=0;
		
		for (String accn : accnID) {
			String temp = accn+","+adjPyrID.get(i)+","+procCd.get(i)+","+units + "," + adjCD.get(i)+","+billAmt.get(i)+","+adjAmt.get(i)+","+anticipatedNewAmt.get(i);
			lines.add(temp);	
			i++;
		}
		lines.add(footerStr);
		
		Path file = Paths.get(pathCreateFile);
		Files.write(file, lines, Charset.forName("UTF-8"));
	}
	
	public boolean isFileExist(String filePath){
		boolean flag = false;
		LocalFileDetector detector = new LocalFileDetector();		
		File file = detector.getLocalFile(filePath);
		
		if (file.exists()){
			flag = true;
			LOG.info("      " + filePath + " exists.");
		}
		else{
			LOG.info("      " + filePath + " does not exist.");
		}
		
		return flag;
	}
	
	public void waitForLoginSuccess(){
		
		try {
			WebElement element = driver.findElement(By.xpath("//div[@class='step_non_cln_adj widePage']//span[@class='platormPageTitle']"));
			if(element.isDisplayed() && element.isEnabled()) return;
		} catch (Exception e){
			waitForLoginSuccess();
		}
	}
	
}
