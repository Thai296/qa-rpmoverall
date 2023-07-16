package com.ws.tests;

import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SmokeCPMTest extends SeleniumBaseTest
{
	@Test(priority = 1, description = "Health Check Page Status with no version - OK")
	public void testITRS_01() {
	
		logger.info("===== Testing - testITRS-1: =====");		
			
		logger.info("*** Step 1 Actions: - Navigate to CPM Health Check Page ");	
				
		RequestSpecification httpRequest = RestAssured.given();
		String baseUrl=config.getProperty(PropertyMap.CPM_URL);
		Response response =httpRequest.relaxedHTTPSValidation().when().get(baseUrl+"/m0n1tor");
		driver.get(baseUrl+"/m0n1tor");
		logger.info("*** Step 2 Make Sure Status is - Success");	
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		
		String responseBody = response.getBody().asString();
		
		JsonPath jp = new JsonPath(responseBody);
		String appVersion= jp.get("appp");
		String appStatus= jp.get("status");
		logger.info("Make sure CPM app and status are valid " + appStatus);
		
		assertEquals("cpm", appVersion);
		assertEquals("OK", appStatus);
	}
}
	

	
