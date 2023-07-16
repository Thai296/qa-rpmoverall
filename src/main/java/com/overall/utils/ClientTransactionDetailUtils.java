package com.overall.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientTransactionDetailUtils {
	private RemoteWebDriver driver;	
	protected Logger logger;

	public ClientTransactionDetailUtils(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	

	public String formatDollarAmount(Object amount){
		if(amount.toString().isEmpty() || amount == null ){amount = 0;};
		//Infonam//NumberFormat formatter = NumberFormat.getCurrencyInstance();
		NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
		return formatter.format(amount).toString().trim();
	}

	public int getLeftSide(WebElement element){
		//Used points class to get x coordinates of element.
		Point classname = element.getLocation();
		int xcordi = classname.getX();
		//Debug //System.out.println("Element's Position from left side"+xcordi +" pixels.");

		return xcordi;
	}
	public int getTopSide(WebElement element){
		//Used points class to get y coordinates of element.
		Point classname = element.getLocation();
		int ycordi = classname.getY();
		//Debug //System.out.println("Element's Position from top"+ycordi +" pixels.");

		return ycordi;
	}


	public boolean getColumnvalueWithIndex(WebElement element,String columnValue ,int index) {
		boolean flag = true;
		boolean found = false;
		List<String> list = new ArrayList<String>();
		// To locate table.
		WebElement mytable = element;
		// To locate rows of table.
		List<WebElement> rows_table = mytable.findElements(By.tagName("tr"));
		// To calculate no of rows In table.
		int rows_count = rows_table.size();
		//"dd-MMM-yyyy"
		// Loop will execute till the last row of table.
		for (int row = 0; row < rows_count; row++) {
			// To locate columns(cells) of that specific row.
			List<WebElement> Columns_row = rows_table.get(row).findElements(By.tagName("td"));
			// To calculate no of columns(cells) In that specific row.
			int columns_count = Columns_row.size();
			//Debug//System.out.println("Number of cells In Row " + row + " are "+ columns_count);//Debug info

			// Loop will execute till the last cell of that specific row.
			for (int column = 0; column < columns_count; column++) {
				// To retrieve text from that specific cell.
				if(column == index){
					String str = Columns_row.get(index).getText().toString();
					//Debug//System.out.println("Str=" + str);
					if (str.isEmpty()) {
						flag = false;
						found = true;
						logger.info("        Value is empty in table");
						break;
					}
					// Compare column value with the string passed
					if (str.contains(columnValue)) {
						flag = false;
						found = true;
						logger.info("        Matching value found in webtable for: " + columnValue.replaceAll(",",""));
						break;
					}
				}
				if (!flag) {
					break;
				}

			}

		}
		return found;
	}

	public String formatDate(String dateString, String patter) throws ParseException{

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date date=df.parse(dateString);
		df = new SimpleDateFormat(patter);

		return df.format(date);
	}

	public boolean verfiyFilterOfTable(String filterString, int indexOfColumFilter, WebElement tableFilter, boolean haveLinkOnColumn){
		// indexOfColumnFilter start with 1 for the first column

		List<WebElement> rows = tableFilter.findElements(By.tagName("tr"));
		String idTable = tableFilter.getAttribute("id");
		String xpathString = haveLinkOnColumn ? "//*[@id='"+idTable+"']//tr/td["+indexOfColumFilter+"]/a" : "//*[@id='"+idTable+"']//tr/td["+indexOfColumFilter+"]";
		for (WebElement row : rows) {
			if (!tableFilter.findElement(By.xpath(xpathString)).getText().equalsIgnoreCase(filterString))
				return false;
		}
		return true;
	}

	public String priceWithoutDecimal (Double price) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		DecimalFormat df = (DecimalFormat)nf;
		df.applyPattern("###,###.00");
		String output = df.format(price);
		return output.equalsIgnoreCase(".00")==true?"0.00":output;
	}

	public boolean verfiyNavigatePageOfTable(WebElement footerNavigate, String navigateType){
		/*

		 * footerNavigate is Element of div in footer table. Ex : //*[@id="pg_tbl_clnBilledAccessions_pagernav"]
		 * navigateType select one to verify :
			+ 'next' : verify next page
			+ 'previous' : verify previous page
			+ 'first' : verify first page
			+ 'last' : verify last page

		 */

		String idFooter = footerNavigate.getAttribute("id");
		WebElement currentPageInput = driver.findElement(By.xpath("//*[@id='"+idFooter+"']/table/tbody/tr/td[2]/table/tbody/tr/td[4]/input"));
		WebElement totalPageLabel = driver.findElement(By.xpath("//*[@id='"+idFooter+"']/table/tbody/tr/td[2]/table/tbody/tr/td[4]/span"));
		int currentPage = Integer.parseInt(currentPageInput.getAttribute("value").trim());
		int totalPage = Integer.parseInt(totalPageLabel.getText().trim());
		if (navigateType.equalsIgnoreCase("next"))					if (currentPage == 1) return false;
		else if (navigateType.equalsIgnoreCase("previous"))			if (currentPage == totalPage) return false;
		else if (navigateType.equalsIgnoreCase("first"))			if (currentPage != 1) return false;
		else if (navigateType.equalsIgnoreCase("last"))				if (currentPage != totalPage) return false;
		return true;
	}

	public boolean verfiyWindowIsClosed(){ // Window closed return true
		
		try {
			if (!(null == ((RemoteWebDriver)driver).getSessionId())) {
			    // check in chrome browser
				driver.close();
			} 
			driver.getWindowHandle();
			return false;
		} catch (Exception e){
			return true;
		}
	}
	
	public void selectColumnValueWithIndexColumn(WebElement element ,int index) throws Exception {
		Actions actions = new Actions(driver);
		boolean flag = false;
		String str ="";
		List<String> list = new ArrayList<String>();
		// To locate table.
		WebElement mytable = element;
		// To locate rows of table.
		List<WebElement> rows_table = mytable.findElements(By.tagName("tr"));
		// To calculate no of rows In table.
		int rows_count = rows_table.size();
		// Loop will execute till the last row of table.
		for (int row = 0; row < rows_count; row++) {
			// To locate columns(cells) of that specific row.
			List<WebElement> Columns_row = rows_table.get(row).findElements(By.tagName("td"));
			// To calculate no of columns(cells) In that specific row.
			int columns_count = Columns_row.size();
			// Loop will execute till the last cell of that specific row.
			for (int column = 0; column < columns_count; column++) {
				// To retrieve text from that specific cell.
				if(column == index){
					str = Columns_row.get(index).getText().toString();
					if(!str.isEmpty()){
						doubleclickHiddenPageObject(Columns_row.get(index),0);
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				break;
			}

		}

	}
	
	public void doubleclickHiddenPageObject(WebElement element, int elementArg) throws Exception{

		if(element != null) {
			((JavascriptExecutor) driver).executeScript("var evt = document.createEvent('MouseEvents'); evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null); arguments[" + elementArg  +"].dispatchEvent(evt);", element);
			logger.info("        Double Click on element " + element);
			Thread.sleep(1000);
		} else {
			logger.error("        Element " + element + " does not exist");
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method checks if the Processing is done 
	public boolean isProgressingNotPresent(WebElement element, int time) throws Exception{
		boolean flag = false;
		int i = 0;

		while (i < time) {
			if(!(element.getAttribute("style").contains("none"))){	
				logger.info("        Progressing is visible.");					
			}else{			
				flag = true;
				logger.info("        Progressing is not visible.");	
				break;
			}
			Thread.sleep(1000);
			i++;		
		}
		return flag;	
	}	
	
	public String formatNumberValue(Object value){
		if(value.toString().isEmpty() || value == null ){value = 0;};
		NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("en", "US"));
		return formatter.format(value).toString().trim();
	}
	// Switch to popup window and returns parent window handler
	public String switchToPopupWin() throws InterruptedException
	{
		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> it = handles.iterator();
		String switchWin = null;

		while (it.hasNext()){
			switchWin = it.next();
			logger.info("        Switching to Pop Up Window: " + driver.switchTo().window(switchWin).getTitle());
			driver.switchTo().window(switchWin);
		}
		Thread.sleep(2000);
		return parentWindow;
	}
}
