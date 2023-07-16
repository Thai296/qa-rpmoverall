package com.ws.tests;



import com.xifin.utils.SeleniumBaseTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class SmokeElectronicPaymentWSTest extends SeleniumBaseTest {

	@Test(priority = 1, description = "HealthCheckPage-Status OK")
	public void testEPWS_13(){
		logger.info("===== Testing - testEPWS_13 =====");

		logger.info("*** Actions: - Navigate to Electronic Payment Webservice Health Check Page ");
		RequestSpecification httpRequest = RestAssured.given();
		String baseUrl="https://xifinpay.xifinqa.com/prod/electronicpayment/";
		Response response =httpRequest.relaxedHTTPSValidation().when().get(baseUrl+"m0n1tor");
		driver.get(baseUrl+"/m0n1tor");

		logger.info("*** Expected Results: - Make Sure Status is - Success");
		int statusCode = response.getStatusCode();
		assertEquals(statusCode,200);

		String responseBody = response.getBody().asString();

		JsonPath jp = new JsonPath(responseBody);
		String appVersion= jp.get("appp");
		String appStatus= jp.get("status");
		logger.info("Make sure WS version and status are valid " + appVersion + appStatus);

		assertEquals(appVersion,"prod/electronicpayment");
		assertEquals(appStatus,"OK");

	}

}	
