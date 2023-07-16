package com.overall.accession.accessionProcessing;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class AccessionTransactionDetail {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public AccessionTransactionDetail(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement pageTitleText() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"blue\"]')[0]"); 
	}
	
	public WebElement accnIdInput(String accnId) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#accnId')).val(\"" + accnId + "\")).trigger('onblur')[0]"); 
	}
	
	public WebElement accnSearchIconBtn(){
		return driver.findElement(By.id("accnSrch"));
	}
	
	//Overload
	public WebElement accnIdInput() throws Exception {
		return driver.findElement(By.id("accnId"));
	}
	
	public WebElement ptFNameInput() throws Exception {
		return driver.findElement(By.id("ptFNm"));
	}
	
	public WebElement accnStatusInput() throws Exception {
		return driver.findElement(By.id("accnStatus"));
	}	
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	public WebElement submitBtnInFrame() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#contentarea').contents()).contents().find('#btn_submit').trigger('onclick')[0]");		
	}
	
	public WebElement resetBtnInFrame() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#contentarea').contents()).contents().find('#btn_reset').trigger('onclick')[0]");		
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}	
	
	//Ordered Test Details
	public WebElement orderTestDetailsMod1Dropdown() throws Exception {
		return driver.findElement(By.id("testMod1"));
	}
	
	public WebElement orderTestDetailsMod1Text() throws Exception {
		return driver.findElement(By.id("testMod1"));
	}
	
	public String incrPriceMethodText(String index) throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[title=\"INCREMENTAL\"]')[" + index + "]).val()"); 
	}
	
	//Diagnosis Detail
	public WebElement addDxBtn() throws Exception {
		return driver.findElement(By.id("insrtDiagBtn"));
	}
	
	public WebElement dXCodeInput() throws Exception {
		return driver.findElement(By.id("diagCdId"));
	}
	
	public WebElement dXCodeText(String dxCode) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($(':input[title=\"" + dxCode + "\"]')).get(0)"); 
	}
	
	public WebElement clientContactInput() throws Exception {
		return driver.findElement(By.id("contact"));
	}
	
	public WebElement clientContactInputText() throws Exception {
		return driver.findElement(By.name("contact"));
	}
	
	public WebElement clientContactInputRow2Text() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($(':input[name=contact]')).get(1)"); 
	}
	
	public WebElement docRecvInput() throws Exception {
		return driver.findElement(By.id("diagDoc"));
	}
	
	public WebElement docRecvInputText() throws Exception {
		return driver.findElement(By.name("diagDoc"));
	}
	
	public WebElement docRecvInputRow2Text() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($(':input[name=diagDoc]')).get(1)"); 
	}
	
	public WebElement commentInputText() throws Exception {
		return driver.findElement(By.name("diagDoc"));
	}
	
	public WebElement commentInputRow2Text() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($(':input[name=diagCmnt]')).get(1)"); 
	}
	
	public WebElement contactDateInput() throws Exception {
		return driver.findElement(By.id("contactDt"));
	}
	
	public WebElement statementTypeDropdown() throws Exception {
		return driver.findElement(By.id("submissionSvcID"));
	}
	
	//Billable Procedure Code Details
	public WebElement adjCodeDropdown() throws Exception {
		return driver.findElement(By.id("addAdj"));
	}
	
	public WebElement billableProcCodeDetailsText(int row, int col) throws Exception {	
		return driver.findElement(By.cssSelector("#bpcTable > tbody > tr:nth-child(" + row + ") > td:nth-child(" + col + ")"));	
	}
	
	public WebElement addOverrideBtn() throws Exception {
		//return driver.findElement(By.id("addErrBtn"));
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#contentarea').contents()).contents().find('#addErrBtn')[0].click()");
	}
	
	public WebElement billableProcCodeDetailsTable() throws Exception {
		return driver.findElement(By.id("bpcTable"));
	}
	
	public WebElement selectProcCodeInFrame() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#contentarea').contents()).contents().find('#bpcTable')).find('#procId')[0].click()"); 
	}
	
	public String procCodeTextInFrame() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($($($('#contentarea').contents()).contents().find('#bpcTable')).find('#procId')).attr(\"value\")"); 
	}
	
	public WebElement accnIdInputInFrame(String accnId) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#contentarea').contents()).contents().find('#accnId').val(\"" + accnId + "\").trigger('onblur')[0]"); 
	}
	
	//Bulk Payment / Denials
	public WebElement adjCodeBulkDropdown() throws Exception {
		return driver.findElement(By.id("addAdjBulk"));
	}
	
	public void selectStatementType(SeleniumBaseTest b, String value) throws Exception{
		b.selectItem(statementTypeDropdown(), value);
		submitBtn().click();
	}
	
	public void searchAccnId(String accnId) throws Exception{
		accnIdInput(accnId);
		logger.info("Entered AccnID: " + accnId);
	}
	
	public void addDiagnosisCode(String dxCode, String clientContact) throws Exception{
		addDxBtn().click();
		dXCodeInput().sendKeys(dxCode);
		clientContactInput().sendKeys(clientContact);
		contactDateInput().sendKeys("t");
		submitBtn().click();
		logger.info("Submitted Diagnosis Code");
	}
	
	public void selectOrderTestDetailMod(SeleniumBaseTest b, String value) throws Exception{
		orderTestDetailsMod1Dropdown().click();
		b.selectItem(orderTestDetailsMod1Dropdown(), value);
		submitBtn().click();
		logger.info("Submitted Modifier: " + value);
	}
	
	public void clickAccnSearchIconBtn() throws Exception{		
		accnSearchIconBtn().click();
		logger.info("        Clicked Accession Search button.");
	}
	
	//public boolean compareDropdownItems(List<String> dbList, WebElement e) throws Exception{
		
		/*Boolean flag = true;
		Select select = new Select(e);
		List<WebElement> eList = select.getOptions();
		
		if(dbList.size() == eList.size()){
			
			for (int i=0; i<dbList.size(); i++){
				System.out.println("Item " + i + " : ---" + dbList.get(i).trim()+ "---");
			}
			
			for (int j=0; j<eList.size(); j++){
				System.out.println("Element " + j + " : ---" + eList.get(j).getText().trim()+ "---");
			}
			
			
			
		} else {
			logger.error("dbList Size " + dbList.size() + " != " + "eList Size " + eList.size());
		}*/
		
		
		//Check size
		/*if(dbList.size() == eList.size()){
			System.out.println("dbList Size " + dbList.size() + " = " + "eList Size " + eList.size() );
			//Compare items in the dropdown list
			 for(WebElement option : eList)  {  
	        	for (int i=0; i<dbList.size(); i++){
	        		if (!option.getText().trim().equals(dbList.get(i).trim())){
	        			
	        			logger.error("<UI Dropdown List:> " + option.getText() + ", <DB List>: " + dbList.get(i));
	        			flag = false;
	        			break;
	        		}   
	        	}
	        	if(!flag)
	        		break;
			 }
		} else {
			logger.error("dbList Size " + dbList.size() + " != " + "eList Size " + eList.size() );
			return false;
		}*/
		//return compareLists();
	//}
	

}
