package com.overall.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ImportEngineUtils {

	private final RemoteWebDriver driver;
	private final Logger logger;
	private EngineUtils engineUtils;
	
	public ImportEngineUtils(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	//Add new data in ELIG_PYR_ROSTER for case Update (UpdateIndicator = U)
	public boolean createNewElig(String textInput, String location, String fileName,String sourceLocation, String batEngineRun, int waitTime) throws Exception{
		engineUtils = new EngineUtils(driver);
		engineUtils.creatFile(textInput, location, fileName);
		engineUtils.runEngine(sourceLocation, batEngineRun, waitTime);
		return true;
	}
	
	public String createDataForGenericFile(String division, String subsId, String effDt, String expDt, String FName, String LName, String MName, String DOB, String payorId){
		String content ="DIVISION | MEMBID | EFFDATE | EXPDATE | FIRSTNAME | LASTNAME | MIDDLENAME || DOB ||||||||||| PYRID\r\n" 
				+ division+"|"+subsId+"|"+effDt+"|"+expDt+"|"+FName+"|"+LName+"|"+MName+"||"+DOB+"|||||||||||"+payorId ;
		return content;
	}
	
	//Create hospitalAdmit file contents
	public String createDataForHospitalAdmitFile(String type, String subID, String lastName, String firstName, String midName, String dob, String admitDate, String dischargeDate, String SSN, String facAbbrv, String ptTyp)
	{
		List<String> fields = new ArrayList<>();
		fields.add(StringUtils.defaultString(type));
		fields.add(StringUtils.defaultString(subID));
		fields.add(StringUtils.defaultString(lastName));
		fields.add(StringUtils.defaultString(firstName));
		fields.add(StringUtils.defaultString(midName));
		fields.add(StringUtils.defaultString(dob));
		fields.add(StringUtils.defaultString(admitDate));
		fields.add(StringUtils.defaultString(dischargeDate));
		fields.add(StringUtils.defaultString(SSN));
		fields.add(StringUtils.defaultString(null)); // unused 1
		fields.add(StringUtils.defaultString(null)); // unused 2
		fields.add(StringUtils.defaultString(facAbbrv));
		fields.add(StringUtils.defaultString(ptTyp));
		return StringUtils.stripEnd(StringUtils.join(fields, "|"), "|");
	}
	
	public String createDataForUSPSfile(String accnID,String oldPatientAddress, String oldPatientZipID, String newPatientAddr1, String newPatienttAddr2,String newPatientCity, String newPatientStatusId,String newPatientZipId, String NcoaCdType,String NcoaCd){
		String content = "accnID|oldPatientAddress||||oldPatientZipID|newPatientAddr1|newPatienttAddr2|newPatientCity|newPatientStatusId|newPatientZipId|NcoaCdType|NcoaCd\r\n"
				+accnID+"|"+oldPatientAddress+"||||"+oldPatientZipID+"|"+newPatientAddr1+"|"+newPatienttAddr2+"|"+newPatientCity+"|"+newPatientStatusId+"|"+newPatientZipId+"|"+NcoaCdType+"|"+NcoaCd;
		return content;
	}
	
	public String createInvalidDataForUSPSfile(){
		String content = "Header\r\n"
				+"total";
		return content;
	}
	
	public String createAllinaRefundFile(String accnId, String chkNum, String dateSent, String amt, String refundSeqId, String refundType ){
		String content = "1,"+accnId + refundType + refundSeqId + "\r\n"
				+ "2,Check number: #       "+chkNum+" Date sent: "+dateSent+" Amount:,"+amt+"";
		return content;
	}
	
	//Create Default Refund file
	public String createDefaultRefundFile(String accnId, String refundAmt, String refundDt, String chkNum, String chkClearDt, String refundNote){
		String content = accnId + "|" + refundAmt + "|" + refundDt + "|" + chkNum + "|" + chkClearDt + "|" + refundNote;
		logger.info (content);
		return content;
	}

	public String convertList(List<String> listData){
		StringBuilder data = new StringBuilder();
		for(int i = 0 ; i< listData.size();i++){
			data.append(listData.get(i));
			if(i < listData.size()-1){
				data.append(",");
			}
		}
		return data.toString();
	}
	
	public String CreateNewTransactionDetail(String number,List<String> typeCode, List<String> amountStr, List<String> fundsType, String bankRef, String customerRef, String text ){
		StringBuilder content = new StringBuilder();
		for(int i = 0 ; i< typeCode.size();i++){
			content.append(number).append(",").append(typeCode.get(i)).append(",").append(amountStr.get(i)).append(",").append(fundsType.get(i)).append(",").append(bankRef).append(",").append(customerRef).append(",").append(text);
			if(i < typeCode.size()-1){
				content.append("\r\n");
			}
		}
		return content.toString();
	}

	public String createValidFileBAI2File(List<String> fileHeader,List<String> groupHeader,List<String> accountIdAndSummary ,String transactionDetail,List<String> AccountTrailer,List<String> GroupTrailer,List<String>FileTrailer){
		String data = "";
		if(fileHeader!= null && !fileHeader.isEmpty())
		{
			data += convertList(fileHeader)+"\r\n";
		}
		if(groupHeader!= null && !groupHeader.isEmpty())
		{
			data += convertList(groupHeader)+"\r\n";
		}
		if(accountIdAndSummary!= null && !accountIdAndSummary.isEmpty())
		{
			data += convertList(accountIdAndSummary)+"\r\n";
		}
		if(!transactionDetail.equals("") &&transactionDetail!=null)
		{
			data += transactionDetail+"\r\n";
		}
		if(AccountTrailer!= null && !AccountTrailer.isEmpty())
		{
			data += convertList(AccountTrailer)+"\r\n";
		}
		if(GroupTrailer!= null && !GroupTrailer.isEmpty())
		{
			data += convertList(GroupTrailer)+"\r\n";
		}
		if(FileTrailer!= null && !FileTrailer.isEmpty())
		{
			data += convertList(FileTrailer);
		}
		
		logger.info(data);		
		return data;
	}
	
	public String getRandomFundTyp(){
		List<String> fundsTyp = Arrays.asList("0", "1", "2", "Z", "");
		Random randomizer = new Random();
		String fundType = fundsTyp.get(randomizer.nextInt(fundsTyp.size()));
		return fundType;
	}
	
	public String createDataForTevixFile(String accnId, String oldPTAdd1, String oldPTZIpID, String newPTAdd1, String newPTAdd2, String newPtCity, String newPtStId, String newPTZipId, String newPTPhone){
		String content = "Accession ID|SSN|Patient Last name|Patient First Name| Patient Address 1| Patient Address 2|Patient City|Patient State|Patient zip code| Patient Phone Number| Patient DOB| Patient Date of Service|Updated Patient Address 1|Updated Patient Address 2| Updated Patient City|Updated Patient State| Updated Patient Zip Code| Updated Patient Phone|\r\n"		
				+ accnId+"|"+"|"+"|"+"|"+oldPTAdd1+"|"+"|"+"|"+"|"+oldPTZIpID+"|"+"|"+"|"+"|"+newPTAdd1+"|"+newPTAdd2+"|"+newPtCity+"|"+newPtStId+"|"+newPTZipId+"|"+newPTPhone ;
		
		logger.info(content);
		return content;
	}
	
	public String createValidDiagnosisFile(String FHS , String fileCreateDateTime,String fileNameAgncyName_fileNameCustomerName_fileNameCreateDateTime , String fileId , String customerName,
			String ACCN , String accnID , String accnDOS,String accnPTDOB,String accnPtGender,String DIAG , String diagDescr , String engineAssignedDiagCodes,String engineConfidences,String FTS,String segmentCnt){
		String result;
		result = ""+FHS+"|"+fileCreateDateTime+"|"+fileNameAgncyName_fileNameCustomerName_fileNameCreateDateTime+"|"+fileId+"|"+customerName+"\r\n"
					+ ""+ACCN+"|"+accnID+"|"+accnDOS+"|"+accnPTDOB+"|"+accnPtGender+"\r\n"
					+ ""+DIAG+"|"+diagDescr+"|"+engineAssignedDiagCodes+"|"+engineConfidences+"\r\n"
					+ ""+FTS+"|"+segmentCnt+"";
		
		logger.info(result);
		return result;
	}

	public String checkFundTyp(String fundTyp){
		String data;
		if(!fundTyp.equals("") &&fundTyp!=null)
		{
			data = fundTyp;
		}
		else{
			data = "Z";
		}
		return data;
	}
	
	public String checkGroupCNTandTransCNT(List<List<String>> data){
		String count ="";
		if(data.size() == 0){
			count = "0";
		}
		if(data.size() > 1){
			count = String.valueOf(data.size());
		}
		else if(data.size() == 1){count = "1";}
		return count;
	}
	
	public String createDLSAccnRefundFile(String accnID,String refundAmount,String refundDate,String checkNumber,String clearDate,String refundNote,String randomNumber){
		String result;
		result = "AccnID|RefundAmount|RefundDate|CheckNumber|ClearData|RefundNote\r\n"
					+ ""+accnID+"|"+refundAmount+"|"+refundDate+"|"+checkNumber+"|"+clearDate+"|"+refundNote+"\r\n"
					+ "TRL|"+randomNumber;
		
		logger.info(result);
		return result;
	}
	
	//If the last 8 characters in the identifier are a parseable MMDDCCYY date, it’s a Client refund.  The client abbrev is the first part of the identifier up to the accounting date.
	public String createDLSClnRefundFile(String clnAbbrev,String refundAmount,String refundDate,String checkNumber,String clearDate,String refundNote,String randomNumber, String acctingDt){
		String result;
		acctingDt = acctingDt.replaceAll("/", "");
		
		result = "AccnID|RefundAmount|RefundDate|CheckNumber|ClearData|RefundNote\r\n"
					+ ""+clnAbbrev + acctingDt + "|"+refundAmount+"|"+refundDate+"|"+checkNumber+"|"+clearDate+"|"+refundNote+"\r\n"
					+ "TRL|"+randomNumber;
		
		logger.info(result);
		return result;
	}	
	
	public String createValidDataForDefaultRefundFile(String identifier, String amt, String dtSent, String checkNum, String chkClearDt, String note){
		String content = identifier+"|"+amt+"|"+dtSent+"|"+checkNum+"|"+chkClearDt+"|"+note;
		
		logger.info(content);
		return content;
	}
	
	public double getSummaryAmount(List<List<String>> data,int sumIndex,String accnID,int accnIDIndex) {
		double sum = 0;
		for (List<String> subData : data) {

			if (subData.get(accnIDIndex).equals(accnID)) {
				sum += Double.parseDouble(subData.get(sumIndex));
			} else {
				logger.info("        The list does not contain " + accnID);
			}

		}
		return sum;
	}
	
}
