package com.overall.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.utils.SeleniumBaseTest;

public class QuestionAssignmentUtils extends SeleniumBaseTest {
	protected Logger logger;
	public QuestionAssignmentUtils(RemoteWebDriver driver){
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	public List<String> getRequiredropdownListByRequireValue(String require){
		Random rand = new Random();
		List<String> result = new ArrayList<String>();
		List<String> requireds = Arrays.asList("optional", "recommended", "required");
		List<String> requiredsId = Arrays.asList("1", "2", "3");
		
		for (int i = 0; i < requireds.size(); i++) {
	        int randomIndex = rand.nextInt(requireds.size());
	        String randomElement = requireds.get(randomIndex);
	        String randomElementId = requiredsId.get(randomIndex);
	        if(!randomElement.equals(require)){
	        	result.add(randomElement);//0
	        	result.add(randomElementId);//1
	        	break;
	        }
	    }
		
		return result;
	}
	
}
