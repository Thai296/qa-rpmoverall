package com.overall.utils;

import com.xifin.mars.dao.IGenericDaoXifinRpm;
import com.xifin.utils.RandomCharacter;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

public class IncrementalPricingUtils {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private RandomCharacter randomCharacter;

	public IncrementalPricingUtils(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public void selectDropDownJQGird(WebElement dropDown,String textSelect) throws InterruptedException{
		dropDown.findElement(By.tagName("a")).click();
		WebElement list = driver.findElement(By.xpath(".//*[@id='select2-drop']/ul"));

		List<WebElement> allRows = list.findElements(By.tagName("li")); 
		// And iterate over them, getting the cells 
		for (WebElement row : allRows) {
			if (row.getText().equalsIgnoreCase(textSelect)) {
				row.click();
				logger.info("        Clicked on dropdown table : " + textSelect);
				break;
			}
		}
	}
	
	//return true : first radio button is checked --- return false : second radiobutton is checked
	public boolean checkRadioBtnInTable(WebElement element ,String value) throws Exception {
		boolean flag = false;
		String str ="";
		String found ="";
		WebElement mytable = element;
		List<WebElement> rows_table = mytable.findElements(By.tagName("tr"));
		int rows_count = rows_table.size();
		for (int row = 0; row < rows_count; row++) {
			List<WebElement> Columns_row = rows_table.get(row).findElements(By.tagName("td"));
			int columns_count = Columns_row.size();
			for (int column = 0; column < columns_count; column++) {
				str = Columns_row.get(column).getText().toString();
				if(str.contains(value)){
					flag = Columns_row.get(column).findElement(By.tagName("input")).isSelected();
					found ="found";
					break;
				}
			}
			if(!found.isEmpty()) break;

		}
		return flag;
	}

	//return true : first radio button is checked --- return false : second radiobutton is checked
	public boolean checkCheckBoxInTable(WebElement element ,String compStr, String value) throws Exception {
		boolean flag = false;
		String str ="";
		String found ="";
		WebElement mytable = element;
		List<WebElement> rows_table = mytable.findElements(By.tagName("tr"));
		int rows_count = rows_table.size();
		for (int row = 0; row < rows_count; row++) {
			List<WebElement> Columns_row = rows_table.get(row).findElements(By.tagName("td"));
			List<WebElement> listcheckBox = Columns_row.get(row).findElements(By.xpath("//input[@type='checkbox']"));
			int listcheckBox_count = listcheckBox.size();
			for (int column = 0; column < listcheckBox_count; column++) {
				str = listcheckBox.get(column).getAttribute(compStr);
				if (str.equalsIgnoreCase(value)) {
					flag = listcheckBox.get(column).isSelected();
					found = "found";
					break;
				}
			}
			if(!found.isEmpty()) break;

		}
		return flag;
	}

	public String getCurrentSelectTextInJQGridDropdown(WebElement dropDown) throws InterruptedException{
		String idDropdown = dropDown.getAttribute("id");
		return driver.findElement(By.xpath(".//*[@id='"+idDropdown+"']/a/span[1]")).getText();
	}

	public List<String> getTestHaveTestType(IGenericDaoXifinRpm xifinRpmDao) throws Exception{
		List<String> testCodeInfo = null;
		do{
			List<String> testCode = xifinRpmDao.getSingleTest(null);
			String testID = testCode.get(1);
			testCodeInfo = xifinRpmDao.getTestCodeInfo(testID,null);
		} while (testCodeInfo.get(3).trim().length() < 1);
		return testCodeInfo;
	}
	
	public Integer getNonZeroRandomNumber(int length) throws Exception{  
		  randomCharacter = new RandomCharacter(driver);
		  String str = randomCharacter.getRandomNumericString(length);
		  
		  /* Infonam
		  while (str.equals("0")){
			  str = randomCharacter.getRandomNumericString(length);
		  }*/
		  while (str.substring(0,1).equals("0")){
			  str = randomCharacter.getRandomNumericString(length);
		  }

		  return Integer.parseInt(str);
		 }
	
	public boolean checkDeleteRowInTable(WebElement element, String compStr) {
		boolean deleted = false;
		String disabledClass = element.getAttribute("class");
		if (disabledClass.contains(compStr)) {
			deleted = true;
		}

		return deleted;
	}
}
