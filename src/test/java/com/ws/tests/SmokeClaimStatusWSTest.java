package com.ws.tests;



import com.overall.claimstatusWS.ClaimStatusWSIndexPage;
import com.overall.menu.MenuNavigation;
import com.xifin.utils.SeleniumBaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;



public class SmokeClaimStatusWSTest extends SeleniumBaseTest {


	private MenuNavigation navigation;
	private ClaimStatusWSIndexPage claimStatusWSIndexPage;
	

	@Test(priority = 1, description = "Index Page")
	@Parameters({"email", "password"})
	public void testCSWS_06(String email, String password) throws Exception {		
		logger.info("===== Testing - testClaimStatusWS_06 =====");		
		navigation = new MenuNavigation(driver, config);
		claimStatusWSIndexPage =  new ClaimStatusWSIndexPage(driver);
		logger.info("*** Step 1 Actions: - Navigate to ClaimStatus Webservice Index page");				
		navigation.navigateToClaimStatusWSPage();
		Thread.sleep(1000);
		
		logger.info("*** Step 2 Expected Results: - Verify that the ClaimStatus WS Index page displays properly");
		assertTrue(isElementPresent(claimStatusWSIndexPage.claimStatusWSLink(), 5));		
		assertEquals(claimStatusWSIndexPage.claimStatusWSLink().getText().trim(),"Claim Status Webservice WSDL");
		
		logger.info("*** Step 3 Actions: - Click ClaimStatus Webservice WSDL link");
		claimStatusWSIndexPage.clickClaimStatusWSLink();
		
		logger.info("*** Step 4 Expected Results: - Verify that the ClaimStatus Webservice WSDL page displays properly");
		assertTrue(driver.getCurrentUrl().contains("claimstatus.wsdl"));
		
		// Parse the URL
		RequestSpecification httpRequest = RestAssured.given();
		Response response =httpRequest.relaxedHTTPSValidation().when().get(driver.getCurrentUrl());
		String respon = response.asString();
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(respon.getBytes("utf-8"))));
		// Get the element from response using element tag name
		String sch0 = doc.getDocumentElement().getAttribute("xmlns:sch0");
		// Verify the response content using Assert
		Assert.assertEquals("http://www.xifin.com/schema/claimstatus",sch0);
		
	}
	@Test(priority = 1, description = "Health Check Page Status - OK")
	@Parameters({"email", "password"})
	public void testCSWS_04(String email, String password) throws Exception {		

		logger.info("===== Testing - testClaimStatusWS_04 =====");		
			
		logger.info("*** Step 1 Actions: - Navigate to ClaimStatus Webservice Health Check Page");	
				
		RequestSpecification httpRequest = RestAssured.given();
		String baseUrl="https://www.xifinqa.com/prod/claimstatusv";
		// todo afa Response response =httpRequest.relaxedHTTPSValidation().when().get(baseUrl+xpVersion+"/m0n1tor");
		// todo afa driver.get(baseUrl+xpVersion+"/m0n1tor");
		logger.info("*** Step 2 Make Sure Status is - Success");	
		// todo afaint statusCode = response.getStatusCode();
		//Assert.assertEquals(200, statusCode);
		
		// todo afa String responseBody = response.getBody().asString();
		
//		JsonPath jp = new JsonPath(responseBody);
//		String appVersion= jp.get("appp");
//		String appStatus= jp.get("status");
//		logger.info("Make sure WS version and status are valid " + appVersion + appStatus);
		
		// todo afa assertEquals("prod/claimstatusv"+xpVersion, appVersion);
		//assertEquals("OK", appStatus);
		
		driver.close();	
		
		
	}
	@Test(priority = 1, description = "Health Check Page Status - 404 Not Found, response is empty")
	@Parameters({"email", "password", "oldXpVersion"})
	public void testCSWS_05(String email, String password, String oldXpVersion) throws Exception {		

		logger.info("===== Testing - testClaimStatusWS_05 =====");		
			
		logger.info("*** Step 1 Actions: - Navigate to ClaimStatus Webservice Health Check Page");	
				
		RequestSpecification httpRequest = RestAssured.given();
		String baseUrl="https://www.xifinqa.com/prod/claimstatusv";
		
		Response response =httpRequest.relaxedHTTPSValidation().when().get(baseUrl+oldXpVersion+"/m0n1tor");
		
		logger.info("*** Step 2 Make Sure Status is - 404 Not Found");	
		int statusCode = response.getStatusCode();
		Assert.assertEquals(404, statusCode);
		
		String responseBody = response.getBody().asString();
		logger.info("Make sure WS response is empty " + responseBody);
		assertTrue(responseBody.isEmpty());
		
		driver.close();	
		
		
	}
	

	
}	
