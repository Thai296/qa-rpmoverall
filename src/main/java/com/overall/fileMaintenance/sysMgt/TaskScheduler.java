package com.overall.fileMaintenance.sysMgt;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

public class TaskScheduler {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public TaskScheduler(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	

	public By contentFrameLocator()
	{
		return By.id("content");
	}

	public WebElement contentFrame()
	{
		return driver.findElement(contentFrameLocator());
	}
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement taskSchedulerEngineCountText() throws Exception {
		return driver.findElement(By.id("oRwCnt"));
	}
	
	public WebElement taskSchedulerTable() throws Exception {
		return driver.findElement(By.id("oTable"));
	}
	
	public WebElement taskTypeDropdown(String taskType) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#oTable > tbody > tr > td:nth-child(2) > select')).val(" + taskType  + ")[0]");
	}
	
	public WebElement startTimeInput(String startTime) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#oTable > tbody > tr > td:nth-child(3) > input')).val(\"" + startTime + "\")[0]");
	}

	public WebElement taskSchedulerConcurrencyInput(int row, int concurrencyNum) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#oTable > tbody > tr:nth-child(" + row + ") > td:nth-child(5) > :input[name=maxConcurrency][class=nbrdr]')).val(\"" + concurrencyNum + "\")).trigger('onchange')[0]");
	}
	
	public WebElement taskSchedulerConcurrencyText(int row) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#oTable > tbody > tr:nth-child(" + row + ") > td:nth-child(5) > :input[name=maxConcurrency][class=nbrdr]')[0]"); 
	}
	
	public WebElement engineCheckbox(int concurrencyNum) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#oTable > tbody > tr:nth-child(" + concurrencyNum + ") > td:nth-child(6) > input.nbrdr')).click()[0]"); 
	}

	/**
	 * Clicks the delete checkbox for the given row.
	 * @param row the row number to delete
	 * @throws Exception
	 */
	private void deleteTask(int row) throws Exception
	{
		((JavascriptExecutor)driver).executeScript("$($('#oTable > tbody > tr:nth-child("+row+") > td:nth-child(6) > input.nbrdr')).click()[0]");
	}

	/**
	 * Gets the task type Id value for the given row.
	 * @param row the row
	 * @return the task type Id
	 */
	public String getTaskType(int row)
	{
		return ((WebElement)((JavascriptExecutor) driver).executeScript("return $('#oTable > tbody > tr:nth-child("+row+") > td:nth-child(2) > select')[0]")).getAttribute("value");
	}

	/**
	 * Gets the current selected row value from the grid.
	 * @return the selected row value
	 */
	public String getRowNumber()
	{
		return ((WebElement)((JavascriptExecutor) driver).executeScript("return $('#rowNumber')[0]")).getAttribute("value");
	}

	/**
	 * Updates the task type Id, start time, interval and concurrency values for the given row.
	 * @param row the row to update
	 * @param taskTypId the new task type Id
	 * @param startTime the new start time
	 * @param interval the new interval
	 * @param concurrency the new concurrency
	 */
	public void setTask(int row, int taskTypId, String startTime, String interval, int concurrency)
	{
		((JavascriptExecutor)driver).executeScript("$($('#oTable > tbody > tr:nth-child("+row+") > td:nth-child(2) > select')).val("+taskTypId+")[0]");
		((JavascriptExecutor)driver).executeScript("$($('#oTable > tbody > tr:nth-child("+row+") > td:nth-child(3) > input')).val(\""+startTime+"\")[0]");
		((JavascriptExecutor)driver).executeScript("$($('#oTable > tbody > tr:nth-child("+row+") > td:nth-child(4) > input')).val(\""+interval+"\")[0]");
		((JavascriptExecutor)driver).executeScript("$($($('#oTable > tbody > tr:nth-child("+row+") > td:nth-child(5) > :input[name=maxConcurrency][class=nbrdr]')).val(\""+concurrency+"\")).trigger('onchange')[0]");
	}

	public void addTask(int taskTypId, String startTime, String interval, int concurrency) throws Exception
	{
		clickAddRow();
		
		int totalRows = NumberUtils.toInt(taskSchedulerEngineCountText().getText(), 1);
		int rowNumber = NumberUtils.toInt(getRowNumber(), 0)+1;
		if (rowNumber == 1)
		{
			rowNumber = totalRows;
		}
		((JavascriptExecutor)driver).executeScript("$($('#oTable > tbody > tr:nth-child("+rowNumber+") > td:nth-child(2) > select')).val("+taskTypId+")[0]");
		((JavascriptExecutor)driver).executeScript("$($('#oTable > tbody > tr:nth-child("+rowNumber+") > td:nth-child(3) > input')).val(\""+startTime+"\")[0]");
		((JavascriptExecutor)driver).executeScript("$($('#oTable > tbody > tr:nth-child("+rowNumber+") > td:nth-child(4) > input')).val(\""+interval+"\")[0]");
		((JavascriptExecutor)driver).executeScript("$($($('#oTable > tbody > tr:nth-child("+rowNumber+") > td:nth-child(5) > :input[name=maxConcurrency]')).val(\""+concurrency+"\")).trigger('onchange')[0]");
	}

	public void clickAddRow()
	{
		driver.findElement(By.id("btnAddRw1")).click();
	}

	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit")); 
	}
		
	public WebElement taskSchedulerIntervalInput(String interval) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($('#oTable > tbody > tr:nth-child(" + interval + ") > td:nth-child(4) >:input[name=maxConcurrency][class=nbrdr]')[0]");
	}
	
	public void resetAllTaskSchedulerConcurrency() throws Exception{
		int count = Integer.parseInt(taskSchedulerEngineCountText().getText());
		
		for (int i=1; i<count+1; i++){
			taskSchedulerConcurrencyInput(i,0);
		}

		submitBtn().click();
		logger.info("       Reset All Task Scheduler Concurrency to 0");
	}
	
	public boolean isAllTaskSchedulerConcurrencyReset() throws Exception{
		int count = Integer.parseInt(taskSchedulerEngineCountText().getText());
		
		for (int i=1; i<count+1; i++){
			if(!(taskSchedulerConcurrencyText(i).getAttribute("value").equals("0"))){
				return false;
			}
		}
		logger.info("        All Task Scheduler Concurrency is reset");
		return true;
	}
	
	public void setTaskScheduler(String taskType, String startTime, int concurrencyNum) throws Exception{
		taskTypeDropdown(taskType);
		startTimeInput(startTime);
		taskSchedulerConcurrencyInput(1,concurrencyNum);
		submitBtn().click();
		logger.info("        Set Task Scheduler: " + taskType + " Concurrency to " + concurrencyNum);
	}
	
	public void setTaskScheduler(String taskType, String startTime, int concurrencyNum, String interval) throws Exception{
		taskTypeDropdown(taskType);
		startTimeInput(startTime);
		taskSchedulerConcurrencyInput(1,concurrencyNum);
		taskSchedulerIntervalInput(interval);
		submitBtn().click();
		logger.info("        Set Task Scheduler: " + taskType + " Concurrency to " + concurrencyNum + " Interval is "+interval);
	}

	/**
	 * Updates the task schedules for the given list of task types. Will delete extraneous entries for the given task list. Will
	 * add rows for tasks that do not exist in the scheduler.
	 * @param taskTypIds list of task type Ids
	 * @param startTime new start time value
	 * @param interval new internal value
	 * @param concurrency new concurrency value
	 * @throws Exception
	 */
	public void setTaskSchedules(List<Integer> taskTypIds, String startTime, String interval, int concurrency) throws Exception
	{
		for (Integer taskTypId : taskTypIds)
		{
			int taskCnt = 0;
			for (int i = 1; i <= Integer.valueOf(taskSchedulerEngineCountText().getText()); i++)
			{
				if (StringUtils.equals(getTaskType(i), String.valueOf(taskTypId)))
				{
					// If this is the first entry for the task, update the start time, interval and concurrency values.
					if (taskCnt++ == 0)
					{
						setTask(i, taskTypId, startTime, interval, concurrency);
					}
					else
					{
						// If this is a duplicate entry for the task, delete the row.
						deleteTask(i);
					}
				}
			}
			// If we haven't found the task in the grid, add an entry for it.
			if (taskCnt == 0)
			{
				addTask(taskTypId, startTime, interval, concurrency);
			}
		}
		submitBtn().click();
	}
}
