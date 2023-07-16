package com.overall.utils;

import com.xifin.utils.SeleniumBaseTest;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class TestCodeUtils {
	private RemoteWebDriver driver;	
	protected Logger logger;

	public TestCodeUtils(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	

	public boolean selectLoadOption(List<WebElement> elm,String optionName,boolean selectOption){
		boolean flag = false;
		List<WebElement> chkBx_loadOption = elm;
		try {
			// This will tell you the number of checkboxes are present
			int iSize = chkBx_loadOption.size();

			// Start the loop from first checkbox to last checkboxe
			for(int i=0; i < iSize ; i++ ){
				// Store the checkbox name to the string variable, using 'Value' attribute
				String sValue = chkBx_loadOption.get(i).getAttribute("value");
			
				// Select the checkbox it the value of the checkbox is same what you are looking for
				if (sValue.equalsIgnoreCase(optionName)){
					
					if(selectOption){
						
						chkBx_loadOption.get(i).click();
					}
					flag=true;
					break;

				}

			}
		} catch (Exception e) {

			logger.info(e.toString());
		}
		return flag;
	}

	// This function return text selected in dropdown
	public String getTextSelectedInDropdown(WebElement dropDown){
		Select select = new Select(dropDown);
		WebElement tmp = select.getFirstSelectedOption();
		return tmp.getText();  
	}

	// This function return value selected in dropdown
	public String getValueSelectedInDropdown(WebElement dropDown){
		Select select = new Select(dropDown);
		WebElement tmp = select.getFirstSelectedOption();
		return tmp.getAttribute("value");  
	}

	//	verify window had been closed
	public boolean windowIsClosed(RemoteWebDriver driver){

		boolean isClosed=false;

		try{
			driver.getWindowHandle();
		}catch (NoSuchWindowException e){
			isClosed=true;
		}
		return isClosed;
	}
	
	public int selectItemByIndex(WebElement element, int value) throws Exception {
		if (value >=0) {
			new Select(element).selectByIndex(value);
			logger.info("        Selected Dropdown Item: " + value);	
		} else {
			logger.error("        Item does not exist in Dropdown: " + value);
		}
		Thread.sleep(2000);
		return value;
	}

	public String getTotalResultSearch(WebElement pager){
		String total = pager.getText();
		StringTokenizer strToken = new StringTokenizer(total," ");
		String numberOfResult = "";
		while(strToken.hasMoreTokens()){
			numberOfResult = strToken.nextToken();
			if (numberOfResult.equalsIgnoreCase("records") || numberOfResult.isEmpty()) {
				return "0";
			}
		}
		
		logger.info("        Total Number of Search Results = " + numberOfResult);	
		return numberOfResult;
	}

	// This function return text in last item of dropdown
	public String getLastItemDropdown(WebElement dropDown){
		List<WebElement> listOptions = dropDown.findElements(By.tagName("option"));
		String lastItem = "";
		for (WebElement option : listOptions) {
			lastItem = option.getText();
		}
		return lastItem;
	}

	// This function return previous year of date passing
	public String getPreviousYear(String date) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date myDate = dateFormat.parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDate);
		calendar.add(Calendar.YEAR, -1);

		// Use the date formatter to produce a formatted date string
		Date previousDate = calendar.getTime();
		String result = dateFormat.format(previousDate);
		return result;
	}

	public String getNextYear(String date) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date myDate = dateFormat.parse(date);		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDate);
		calendar.add(Calendar.YEAR, 1);

		// Use the date formatter to produce a formatted date string
		Date previousDate = calendar.getTime();
		String result = dateFormat.format(previousDate);		
		return result;
	}


	public List<String> getAllColumnValue(WebElement element) {
		List<String> list = new ArrayList<String>();
		// Get row count
		List<WebElement> rows = element.findElements(By.tagName("tr"));
		
		// loop through each row
		for (WebElement row : rows) {
			// get column count
			List<WebElement> cols = row.findElements(By.xpath("td"));
			// loop through each column
			for (WebElement col : cols) {
				list.add(col.getText());
			}
		}

		return list;
	}
	
 	public List<String> Handle_Dynamic_Webtable(WebElement element, int index) {
        
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
			System.out.println("Number of cells In Row " + row + " are "+ columns_count);
			
			// Loop will execute till the last cell of that specific row.
			for (int column = 0; column < columns_count; column++) {
				// To retrieve text from that specific cell.
				if(column == index){
					String str = Columns_row.get(index).getText().toString();
					if(str != null && !str.isEmpty())
						list.add(str);
				}
				
			}
			
		}
		
		return list;
	}

	public String getPreviousDay(String date) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date myDate = dateFormat.parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		// Use the date formatter to produce a formatted date string
		Date previousDate = calendar.getTime();
		String result = dateFormat.format(previousDate);
		return result;
	}

	public String getNextDay(String date) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date myDate = dateFormat.parse(date);	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_MONTH, +1);

		// Use the date formatter to produce a formatted date string
		Date previousDate = calendar.getTime();
		String result = dateFormat.format(previousDate);		
		return result;
	}

	public  boolean checkTextInDropdown(WebElement dropDown, String text, Boolean ignoreCase){
		// Check text if it's available in Dropdown return true, else return false!
		boolean flag = false;

		//Get list of options from 
		Select select = new Select(dropDown);
		List<WebElement> listOptions = select.getOptions();

		for(WebElement element : listOptions){
			//Ignore case
			if (ignoreCase){
				if (element.getText().equalsIgnoreCase(text)){
					flag=true;
					break;
				}
			} 
			//Non-ignore case
			else {
				if (element.getText().equals(text)){
					flag=true;
					break;
				}
			}
		}

		return flag;
	}

	public String convertDecimalFormat(int number){
		DecimalFormat Formatter = new DecimalFormat("###,###,###");
		return Formatter.format(number);

	}
	
	public String formatDecimalPoint(int number){
		String str = "0";
		DecimalFormat Formatter = new DecimalFormat("0.00");
		str = Formatter.format(number);
		logger.info("        Returned Number format: " + str);
		
		return str;
		//return Formatter.format(number);
	}
	
	//This method checks if the accn was loaded properly within 10 seconds time of period
	public String getTextValue(WebElement element,int time) throws Exception{
		String str = "";
		int count = 0 ;
		do {
			if (count == time) {
				break;
			}else {
				str = element.getText();
				count ++;
				Thread.sleep(1000);
			}

		} while (str.isEmpty());

		return str;
	}
	//This method checks if the accn was loaded properly within 10 seconds time of period
	public String getAttributeValue(WebElement element,int time) throws Exception{
		String str = "";
		int count = 0 ;
		do {
			if (count == time) {
				break;
			}else {
				str = element.getAttribute("value");
				count ++;
				Thread.sleep(1000);
			}

		} while (str.isEmpty());

		return str;
	}
	
	public <T> boolean isListEmpty(List<T> list){
		return list.isEmpty()?true:false;
	}
	
	public String remove0Number(String number){
		return number.equals("0")?"1":number;
	}
	
	public void scrollIntoView(WebElement element){
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}
	
	public boolean hasClass( WebElement el, String className){
	    String[] listClass = el.getAttribute("class").split(" ");
	    for (String classText : listClass) {
			if (classText.equalsIgnoreCase(className)){
				return true;
			}
		}
	    return false;
	}

	public String getNonDuplicateEffDate(SeleniumBaseTest b, WebElement element ,String effDate) throws ParseException{
		boolean flag = false;
		String newEffDate = "";
		String tempEffDate = effDate;
		do {
			newEffDate = getNextDay(tempEffDate);
			tempEffDate = newEffDate;
			flag = b.getColumnValue(element, tempEffDate);
			if(!flag){break;}
		} while (flag);
		return newEffDate;
	}

	//input text to text field and tab out (if tabOutAfter = true) it
	public void inputText(WebElement wb, String textInput, boolean tabOutAfter) throws Exception{
		wb.clear();
		wb.sendKeys(textInput);
		logger.info("        Input "+ textInput+" to "+ wb.getTagName());
		if (tabOutAfter)
			wb.sendKeys(Keys.TAB);
	}

	public void doubleClickOnRowByIndex(WebElement element, int index) throws Exception {
		Actions actions = new Actions(driver);
		List<WebElement> rows = element.findElements(By.tagName("tr"));
		int i = 0;
		for (WebElement row : rows) {
			if (i== index){
				actions.moveToElement(row).doubleClick().perform();
				logger.info("        Clicked on row "+i);
				Thread.sleep(1000);
				break;
			}
			i++;
		}
	}
	
	public Map<String, String> getMapByGivenList(List<String> list) {
		Map<String, String> map = new HashMap<>();
		map.put("testId", list.get(0));
		map.put("testAbbrev", list.get(1));
		map.put("name", list.get(2));
		map.put("testTypeAbbrev", list.get(3));
		map.put("labTestCodeId", list.get(4));
		return map;
	}
}
