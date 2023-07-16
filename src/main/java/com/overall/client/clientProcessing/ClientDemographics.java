package com.overall.client.clientProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClientDemographics {
	
	private final RemoteWebDriver driver;
	protected final Logger logger;
	private final WebDriverWait wait;
	
	public ClientDemographics(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		this.wait=wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	//Page
	public String pageTitleText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('.blue')[0]).text()"); 
	}
	
	public WebElement clnIdInput() throws Exception {
		return driver.findElement(By.id("clnAbbrev"));
	}
	
	public WebElement nameInput() throws Exception {
		return driver.findElement(By.id("bilAcctNm"));	
	}
	
	public WebElement accountTypeDropDown() throws Exception {
		return driver.findElement(By.id("accntTypId"));	
	}
	
	public WebElement startDateInput() throws Exception {
		return driver.findElement(By.id("startDate"));	
	}
	
	public WebElement adlDropDown() throws Exception {
		return driver.findElement(By.id("annDisclrLtr"));	
	}
	
	public WebElement eavInput() throws Exception {
		return driver.findElement(By.id("estAcctVol"));	
	}
	
	public WebElement taxIdInput() throws Exception {
		return driver.findElement(By.id("clTaxId"));	
	}
	
	public WebElement clnFacNPIInput() throws Exception {
		return driver.findElement(By.id("clFacNPI"));	
	}
	
	public WebElement primaryFacilityDropDown() throws Exception {
		return driver.findElement(By.id("prmryFacId"));	
	}
	
	public WebElement performBilAssgnCheckBox() throws Exception {
		return driver.findElement(By.id("newBillingAssgn"));	
	}
	
	public WebElement performEligCensusCheckBox() throws Exception {
		return driver.findElement(By.id("eligCheckBox"));	
	}
	
	public WebElement doNotReqOECheckBox() throws Exception {
		return driver.findElement(By.name("noOrdEntryCheckBox"));	
	}
	
	public WebElement generalCommentsInput() throws Exception {
		return driver.findElement(By.id("lbCmnts"));	
	}
	
	public WebElement internalCommentsInput() throws Exception {
		return driver.findElement(By.id("interCmnts"));	
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));	
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));	
	}
	
    public WebElement helpBtnInFrame() throws Exception {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#contentarea').contents()).contents().find('#helpbtn')[0]).click()");              
    }
    
	public WebElement copyToShippingAddrCheckBox() throws Exception {
		return driver.findElement(By.id("cpytoShipAddr"));	
	}
	
	public WebElement copyToCorrespAddrCheckBox() throws Exception {
		return driver.findElement(By.id("cpyAddr1"));	
	}
	
	public WebElement copyToStreetAddrCheckBox() throws Exception {
		return driver.findElement(By.id("cpyAddr"));	
	}
	
	//Account Demographics
	public WebElement addXrefBtn() throws Exception {
		return driver.findElement(By.id("btnAddRw1"));	
	}
	
	public WebElement effDateInput() throws Exception {
		return driver.findElement(By.id("effDt"));	
	}
	
	public WebElement xRefTypeDropDown() throws Exception {
		return driver.findElement(By.id("xrefTypeSearch"));	
	}
	
	public WebElement xRefTypeInput() throws Exception {
		return driver.findElement(By.id("xrefType"));	
	}
	
	public WebElement xRefMemberAbbrevInput() throws Exception {
		return driver.findElement(By.id("xrefAbbr"));	
	}
	
	public WebElement xRefMemberDropDown() throws Exception {
		return driver.findElement(By.id("xrefSearch"));	
	}
	
	public WebElement xRefMemberDescrInput() throws Exception {
		return driver.findElement(By.id("xrefDescr"));	
	}
	
	public WebElement xRefInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"select2-search__field\"]')[0]"); 
	}
	
	public WebElement expDateInput() throws Exception {
		return driver.findElement(By.id("expDt"));	
	}
	
	public WebElement deleteCheckBox() throws Exception {
		return driver.findElement(By.id("isDelete"));	
	}
	
	//Billing Address and Contact Information
	public WebElement billingContact1Input() throws Exception {
		return driver.findElement(By.id("bilCntct"));	
	}
	
	public WebElement billingContctMethodDropDown() throws Exception {
		return driver.findElement(By.id("bilCntctMthId"));	
	}
	
	public WebElement billingAddr1Input() throws Exception {
		return driver.findElement(By.id("bilAddr1"));	
	}
	
	public WebElement billingAddr2Input() throws Exception {
		return driver.findElement(By.id("bilAddr2"));	
	}
	
	public WebElement billingZipInput() throws Exception {
		return driver.findElement(By.id("bilZip"));	
	}	
	
	public WebElement billingCityInput() throws Exception {
		return driver.findElement(By.id("bilCity"));	
	}
	
	public WebElement billingPhone1Input() throws Exception {
		return driver.findElement(By.id("bilPhone1"));	
	}
	
	public WebElement billingStateDropDown() throws Exception {
		return driver.findElement(By.id("bilState"));	
	}
	
	public WebElement billingFax1Input() throws Exception {
		return driver.findElement(By.id("bilPhone2"));	
	}
	
	public WebElement billingEmail1Input() throws Exception {
		return driver.findElement(By.id("bilEmail"));	
	}
	
	public WebElement billingContact2Input() throws Exception {
		return driver.findElement(By.id("bil2Cntct"));	
	}
	
	public WebElement billingPhone2Input() throws Exception {
		return driver.findElement(By.id("bil2Phn"));	
	}
	
	public WebElement billingFax2Input() throws Exception {
		return driver.findElement(By.id("bil2Fax"));	
	}
	
	public WebElement billingEmail2Input() throws Exception {
		return driver.findElement(By.id("bil2Email"));	
	}
	
	//Street Address and Contact Information
	public WebElement streetContact1Input() throws Exception {
		return driver.findElement(By.id("lbCntct"));	
	}	
	
	public WebElement streetContctMethodDropDown() throws Exception {
		return driver.findElement(By.id("lbCntctMthId"));	
	}
	
	public WebElement streetAddr1Input() throws Exception {
		return driver.findElement(By.id("lbAddr1"));	
	}
	
	public WebElement streetAddr2Input() throws Exception {
		return driver.findElement(By.id("lbAddr2"));	
	}
	
	public WebElement streetZipInput() throws Exception {
		return driver.findElement(By.id("lbZip"));	
	}	
	
	public WebElement streetPhone1Input() throws Exception {
		return driver.findElement(By.id("lbPhone1"));	
	}
	
	public WebElement streetFax1Input() throws Exception {
		return driver.findElement(By.id("lbPhone2"));	
	}
	
	public WebElement streetEmail1Input() throws Exception {
		return driver.findElement(By.id("lbEmail"));	
	}
	
	public WebElement streetContact2Input() throws Exception {
		return driver.findElement(By.id("lb2Cntct"));	
	}
	
	public WebElement streetPhone2Input() throws Exception {
		return driver.findElement(By.id("lb2Phn"));	
	}
	
	public WebElement streetFax2Input() throws Exception {
		return driver.findElement(By.id("lb2Fax"));	
	}
	
	public WebElement streetEmail2Input() throws Exception {
		return driver.findElement(By.id("lb2Email"));	
	}
	
	public WebElement streetCityInput() throws Exception {
		return driver.findElement(By.id("lbCity"));	
	}
	
	public WebElement streetStateDropDown() throws Exception {
		return driver.findElement(By.id("lbState"));	
	}
	
	//Correspondence Address and Contact Information
	public WebElement correspAddr1Input() throws Exception {
		return driver.findElement(By.id("corAddr1"));	
	}
	
	public WebElement correspAddr2Input() throws Exception {
		return driver.findElement(By.id("corAddr2"));	
	}
	
	public WebElement correspZipInput() throws Exception {
		return driver.findElement(By.id("corZip"));	
	}	
	
	public WebElement correspPhone1Input() throws Exception {
		return driver.findElement(By.id("corPhone1"));	
	}
	
	public WebElement correspFax1Input() throws Exception {
		return driver.findElement(By.id("corPhone2"));	
	}
	
	public WebElement correspEmail1Input() throws Exception {
		return driver.findElement(By.id("corEmail"));	
	}
	
	public WebElement correspContact1Input() throws Exception {
		return driver.findElement(By.id("corCntct"));	
	}
	
	public WebElement correspCityInput() throws Exception {
		return driver.findElement(By.id("corCity"));	
	}
	
	public WebElement correspStateDropDown() throws Exception {
		return driver.findElement(By.id("corState"));	
	}
	
	//Client Refund Address Information
	public WebElement refundAddr1Input() throws Exception {
		return driver.findElement(By.name("refundAddr1"));	
	}
	
	public WebElement refundAddr2Input() throws Exception {
		return driver.findElement(By.name("refundAddr2"));	
	}
	
	public WebElement refundZipInput() throws Exception {
		return driver.findElement(By.name("refundZip"));	
	}
	
	public WebElement refundCityInput() throws Exception {
		return driver.findElement(By.name("refundCity"));	
	}
	
	public WebElement refundStateDropDown() throws Exception {
		return driver.findElement(By.id("refundState"));	
	}
	
	//Shipping Address and Contact Information
	public WebElement shippingAddr1Input() throws Exception {
		return driver.findElement(By.id("shipAddr1"));	
	}
	
	public WebElement shippingZipInput() throws Exception {
		return driver.findElement(By.id("shipZip"));	
	}	
	
	public WebElement shippingPhone1Input() throws Exception {
		return driver.findElement(By.id("shipPhone1"));	
	}
	
	public WebElement shippingContact1Input() throws Exception {
		return driver.findElement(By.id("shipCntct"));	
	}
	
	public WebElement shippingAddr2Input() throws Exception {
		return driver.findElement(By.id("shipAddr2"));	
	}
	
	public WebElement shippingCityInput() throws Exception {
		return driver.findElement(By.id("shipCity"));	
	}
	
	public WebElement shippingStateDropDown() throws Exception {
		return driver.findElement(By.id("shipState"));	
	}
	
	public WebElement shippingFax1Input() throws Exception {
		return driver.findElement(By.id("shipPhone2"));	
	}

	public WebElement shippingEmail1Input() throws Exception {
		return driver.findElement(By.id("shipEmail"));	
	}
	public WebElement clientDemographicsLoadClientIdInput(){
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("lookupClientId")));
	}

	//-------------------------------------------------------------------------------------
	public void setShippingEmail1(String str) throws Exception{		
		shippingEmail1Input().clear();
		shippingEmail1Input().sendKeys(str);
		shippingEmail1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Shipping Email1: " + str);
	}		

	//-------------------------------------------------------------------------------------
	public void setShippingFax1(String str) throws Exception{		
		shippingFax1Input().clear();
		shippingFax1Input().sendKeys(str);
		shippingFax1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Shipping Fax1: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setShippingContact1(String str) throws Exception{		
		shippingContact1Input().clear();
		shippingContact1Input().sendKeys(str);
		shippingContact1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Shipping Contact1: " + str);
	}

	//-------------------------------------------------------------------------------------
	public void setShippingAddr2(String str) throws Exception{		
		shippingAddr2Input().clear();
		shippingAddr2Input().sendKeys(str);
		shippingAddr2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Shipping Addr2: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setRefundZip(String str) throws Exception{		
		refundZipInput().clear();
		refundZipInput().sendKeys(str);
		refundZipInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Client Refund Zip: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setRefundAddr1(String str) throws Exception{		
		refundAddr1Input().clear();
		refundAddr1Input().sendKeys(str);
		refundAddr1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Client Refund Addr1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setRefundAddr2(String str) throws Exception{		
		refundAddr2Input().clear();
		refundAddr2Input().sendKeys(str);
		refundAddr2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Client Refund Addr2: " + str);
	}

	//-------------------------------------------------------------------------------------
	public void setInternalComments(String str) throws Exception{		
		internalCommentsInput().clear();
		internalCommentsInput().sendKeys(str);
		internalCommentsInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Interal Comments: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setGeneralComments(String str) throws Exception{		
		generalCommentsInput().clear();
		generalCommentsInput().sendKeys(str);
		generalCommentsInput().sendKeys(Keys.TAB);	
		logger.info("        Entered General Comments: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setCorrespContact1(String str) throws Exception{		
		correspContact1Input().clear();
		correspContact1Input().sendKeys(str);
		correspContact1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Correspondence Contact1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setCorrespEmail1(String str) throws Exception{		
		correspEmail1Input().clear();
		correspEmail1Input().sendKeys(str);
		correspEmail1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Correspondence Email1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setCorrespFax1(String str) throws Exception{		
		correspFax1Input().clear();
		correspFax1Input().sendKeys(str);
		correspFax1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Correspondence Fax1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setCorrespAddr2(String str) throws Exception{		
		correspAddr2Input().clear();
		correspAddr2Input().sendKeys(str);
		correspAddr2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Correspondence Addr2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setStreetEmail2(String str) throws Exception{		
		streetEmail2Input().clear();
		streetEmail2Input().sendKeys(str);
		streetEmail2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Email2: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setStreetFax2(String str) throws Exception{		
		streetFax2Input().clear();
		streetFax2Input().sendKeys(str);
		streetFax2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Fax2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setStreetPhone2(String str) throws Exception{		
		streetPhone2Input().clear();
		streetPhone2Input().sendKeys(str);
		streetPhone2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Phone2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setStreetContact2(String str) throws Exception{		
		streetContact2Input().clear();
		streetContact2Input().sendKeys(str);
		streetContact2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Contact2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setStreetEmail1(String str) throws Exception{		
		streetEmail1Input().clear();
		streetEmail1Input().sendKeys(str);
		streetEmail1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Email1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setStreetFax1(String str) throws Exception{		
		streetFax1Input().clear();
		streetFax1Input().sendKeys(str);
		streetFax1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Fax1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setStreetAddr2(String str) throws Exception{		
		streetAddr2Input().clear();
		streetAddr2Input().sendKeys(str);
		streetAddr2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Addr2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setStreetContact1(String str) throws Exception{		
		streetContact1Input().clear();
		streetContact1Input().sendKeys(str);
		streetContact1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Contact1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setBillingContact2(String str) throws Exception{		
		billingContact2Input().clear();
		billingContact2Input().sendKeys(str);
		billingContact2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Contact2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setBillingPhone2(String str) throws Exception{		
		billingPhone2Input().clear();
		billingPhone2Input().sendKeys(str);
		billingPhone2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Phone2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setBillingEmail2(String str) throws Exception{		
		billingEmail2Input().clear();
		billingEmail2Input().sendKeys(str);
		billingEmail2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Email2: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setBillingFax2(String str) throws Exception{		
		billingFax2Input().clear();
		billingFax2Input().sendKeys(str);
		billingFax2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Fax2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setBillingEmail1(String str) throws Exception{		
		billingEmail1Input().clear();
		billingEmail1Input().sendKeys(str);
		billingEmail1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Email1: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setBillingFax1(String str) throws Exception{		
		billingFax1Input().clear();
		billingFax1Input().sendKeys(str);
		billingFax1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Fax1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setBillingAddr2(String str) throws Exception{		
		billingAddr2Input().clear();
		billingAddr2Input().sendKeys(str);
		billingAddr2Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Addr2: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setBillingContact1(String str) throws Exception{		
		billingContact1Input().clear();
		billingContact1Input().sendKeys(str);
		billingContact1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Contact1: " + str);
	}

	//-------------------------------------------------------------------------------------
	public void setExpDate(String str) throws Exception{			
		expDateInput().sendKeys(str);
		expDateInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Exp Date: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setXref(String str) throws Exception{			
		xRefInput().sendKeys(str);
		xRefInput().sendKeys(Keys.RETURN);	
		logger.info("        Entered Xref Value: " + str);
	}		
	
	//-------------------------------------------------------------------------------------
	public void setEffDate(String str) throws Exception{			
		effDateInput().sendKeys(str);
		effDateInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Eff Date: " + str);
	}		
	
	//-------------------------------------------------------------------------------------
	public void setEAV(String str) throws Exception{		
		eavInput().clear();
		eavInput().sendKeys(str);
		eavInput().sendKeys(Keys.TAB);	
		logger.info("        Entered EAV: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setTaxId(String str) throws Exception{		
		taxIdInput().clear();
		taxIdInput().sendKeys(str);
		taxIdInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Tax ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setClnFacNPI(String str) throws Exception{		
		clnFacNPIInput().clear();
		clnFacNPIInput().sendKeys(str);
		clnFacNPIInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Client Facility NPI: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setClnId(String str) throws Exception{			
		clnIdInput().sendKeys(str);
		clnIdInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Client ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setName(String str) throws Exception{		
		nameInput().clear();
		nameInput().sendKeys(str);
		nameInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Name: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setStartDate(String str) throws Exception{		
		startDateInput().clear();
		startDateInput().sendKeys(str);
		startDateInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Start Date: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setBillingAddr1(String str) throws Exception{		
		billingAddr1Input().clear();
		billingAddr1Input().sendKeys(str);
		billingAddr1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Addr1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setBillingZip(String str) throws Exception{		
		billingZipInput().clear();
		billingZipInput().sendKeys(str);
		billingZipInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Zip: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setBillingPhone1(String str) throws Exception{		
		billingPhone1Input().clear();
		billingPhone1Input().sendKeys(str);
		billingPhone1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Billing Phone1: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setStreetAddr1(String str) throws Exception{		
		streetAddr1Input().clear();
		streetAddr1Input().sendKeys(str);
		streetAddr1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Addr1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setStreetZip(String str) throws Exception{		
		streetZipInput().clear();
		streetZipInput().sendKeys(str);
		streetZipInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Zip: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setStreetPhone1(String str) throws Exception{		
		streetPhone1Input().clear();
		streetPhone1Input().sendKeys(str);
		streetPhone1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Street Phone1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setCorrespAddr1(String str) throws Exception{		
		correspAddr1Input().clear();
		correspAddr1Input().sendKeys(str);
		correspAddr1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Correspondence Addr1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setCorrespZip(String str) throws Exception{		
		correspZipInput().clear();
		correspZipInput().sendKeys(str);
		correspZipInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Correspondence Zip: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setCorrespPhone1(String str) throws Exception{		
		correspPhone1Input().clear();
		correspPhone1Input().sendKeys(str);
		correspPhone1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Correspondence Phone1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setShippingAddr1(String str) throws Exception{		
		shippingAddr1Input().clear();
		shippingAddr1Input().sendKeys(str);
		shippingAddr1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Shipping Addr1: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setShippingZip(String str) throws Exception{		
		shippingZipInput().clear();
		shippingZipInput().sendKeys(str);
		shippingZipInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Shipping Zip: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setShippingPhone1(String str) throws Exception{		
		shippingPhone1Input().clear();
		shippingPhone1Input().sendKeys(str);
		shippingPhone1Input().sendKeys(Keys.TAB);	
		logger.info("        Entered Shipping Phone1: " + str);
	}
	
	public String convertStringToMonth (String month) throws Exception{
		String letterMonth = "";
		
		switch (month){
			case "01":
				letterMonth = "January";
				break;
			case "02":
				letterMonth = "February";
				break;
			case "03":
				letterMonth = "March";
				break;
			case "04":
				letterMonth = "April";
				break;	
			case "05":
				letterMonth = "May";
				break;
			case "06":
				letterMonth = "June";
				break;
			case "07":
				letterMonth = "July";
				break;
			case "08":
				letterMonth = "August";
				break;
			case "09":
				letterMonth = "September";
				break;
			case "10":
				letterMonth = "October";
				break;
			case "11":
				letterMonth = "November";
				break;
			case "12":
				letterMonth = "December";
				break;
	        default: 
	            logger.error("        String " + month + " has no matching Month.");
	            break;					
		}
		
		logger.info("        Converted String " + month + " to Month: " + letterMonth);
		return letterMonth;
	}
	
}
