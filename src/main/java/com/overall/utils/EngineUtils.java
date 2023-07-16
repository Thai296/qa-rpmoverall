package com.overall.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.utils.RandomCharacter;



public class EngineUtils {

	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public EngineUtils(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public void runEngine(String sourceLocation, String batEngineRun, int waitTime) throws IOException, InterruptedException{
//		sourceLocation 	: location of batEngine file 
//		batEngineRun 	: name of bat file to run engine (ex : run-engine.bat)
//		waitTime		: time for waiting completed running engine
		
		String cmd = "cmd /c start "+batEngineRun;
		Runtime.getRuntime().exec(cmd,null,new File(sourceLocation));
		Thread.sleep(waitTime*1000);
		Runtime.getRuntime().exec("taskkill /f /im cmd.exe") ;
	}

	public String creatFileInbound(String documentFileName, String inboundLocation) throws Exception { // create a documentFileName in inboundLocation with random content
//		documentFileName	: file will be uploaded to server
//		inboundLocation		: inbound folder
		PrintWriter writer = new PrintWriter(new FileOutputStream(inboundLocation+"\\"+documentFileName,true));
		RandomCharacter random = new RandomCharacter(driver);
		String stringWrited = random.getRandomAlphaString(32);
		writer.println(stringWrited);
		writer.close();
		return stringWrited;
	}

	public boolean creatFile(String textInput, String location, String fileName) throws Exception {
//		This function will create a file with content = textInput. 
//		File will be created in : location + fileName;
		PrintWriter writer = new PrintWriter(new FileOutputStream(location+"\\"+fileName,true));
		writer.println(textInput);
		writer.close();
		return this.fileExist(location, fileName);
	}
	
	public boolean fileExist(String location, String filename){
		boolean isExist = false;
		File f = new File(location+filename);
		if(f.exists()){
			isExist = true;
		}else{
			isExist = false;
		}
		return isExist;
		
	}
	
	public static boolean deleteFile(String location, String filename){
		boolean isdelete = false;
		File f = new File(location+filename);
		if(f.delete()){
			isdelete = true;
		}else{
			isdelete = false;
		}
		return isdelete;
	}
	
	public String lastToken(String token, String delim){
		String result = "";
		StringTokenizer tokennize = new StringTokenizer(token, delim);
		while (tokennize.hasMoreElements()) 
		{
			result = tokennize.nextToken();
		}
		return result;
	}
	
	public int checkAmount(int amt , int amt_sent, int money){
		int accnRefund = 0;
		int amtToAsign = Math.min((money), (amt + amt_sent));
		int amtSave = amtToAsign + amt_sent;
		accnRefund = amtSave;
		return accnRefund;
	}
	
	public String readFile(String location, String filename) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(location+"/"+filename));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
	public List<String> parseHL7ADTExportFile(String filePathName, String segmentHeaderName) throws IOException {
        BufferedReader br = null;
        String strLine = "";
        List<String> elementList = new ArrayList<String>();
        
        try {
            br = new BufferedReader( new FileReader(filePathName));
            while( (strLine = br.readLine()) != null){
            	if (strLine.contains(segmentHeaderName)){
            		//System.out.println(strLine);//Debug Info            		
            		String[] strList = strLine.split("[|]");
            		for (String str : strList){
            			if (!(str.equals(""))){
            				elementList.add(str);            				           				
            			}
            		}
            		logger.info("       elementList: " + elementList);
            	}
            }           
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find the file: " + filePathName);
        } catch (IOException e) {
            System.err.println("Unable to read the file: " + filePathName);
        }
        finally {
	        br.close();
	    }
        
        return elementList;        
	}	
	
}

