package com.overall.help;

import com.overall.headerNavigation.HeaderNavigation;
import com.xifin.qa.config.Configuration;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Help
{
	private static final Logger LOG = Logger.getLogger(Help.class);

	private final RemoteWebDriver driver;
	private final Configuration config;

	public Help(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.config = config;
	}	
	
	public String helpTitle()
	{
		return driver.getTitle();
	}
	
	public void navigateToHelp()
	{
		HeaderNavigation headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.marsHelpLink();
	}
}
