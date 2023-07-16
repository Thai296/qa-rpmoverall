package com.ws.tests;



import com.overall.accessionws.AccessionWSIndexPage;
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



public class SmokeAccessionWSTest extends SeleniumBaseTest {


	private MenuNavigation navigation;
	private AccessionWSIndexPage accessionWSIndexPage;
	

	@Test(priority = 1, description = "Health Check Page Status - OK")
	@Parameters({"email", "password"})
	public void testAWS_175(String email, String password) throws Exception {		
 
		logger.info("===== Testing - testAWS-175 =====");		
		navigation = new MenuNavigation(driver, config);
		accessionWSIndexPage =  new AccessionWSIndexPage(driver);
	
		logger.info("*** Step 1 Actions: - Navigate to Accession Webservice Index page ");				
		navigation.navigateToAccessionWSPage();
		Thread.sleep(1000);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Accession WS Index page displays properly");
		assertTrue(isElementPresent(accessionWSIndexPage.accessionWSLink(), 5));		
		assertEquals(accessionWSIndexPage.accessionWSLink().getText().trim(),"Accession Webservice WSDL");
		
		logger.info("*** Step 3 Actions: - Click Accession Webservice WSDL link ");
		accessionWSIndexPage.clickAccessionWSLink();
		
		logger.info("*** Step 4 Expected Results: - Verify that the Accession Webservice WSDL page displays properly");
		assertTrue(driver.getCurrentUrl().contains("accession.wsdl"));
	
		// Parse the URL
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.relaxedHTTPSValidation().when().get(driver.getCurrentUrl());
		String respon = response.asString();
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(respon.getBytes("utf-8"))));
		// Get the element from response using element tag name
		String sch0 = doc.getDocumentElement().getAttribute("xmlns:sch0");
		// Verify the response content using Assert
		Assert.assertEquals(sch0, "http://www.xifin.com/schema/accession");
		
		driver.close();
		
		
		
	}
	@Test(priority = 1, description = "Health Check Page Status - OK - Success")
	@Parameters({"email", "password"})
	public void testAWS_176(String email, String password) throws Exception {		

		logger.info("===== Testing - testAWS_176 =====");		
			
		logger.info("*** Step 1 Actions: - Navigate to Accession Webservice Health Check Page ");	
//	todo afa
//		RequestSpecification httpRequest = RestAssured.given();
//		String baseUrl="https://www.xifinqa.com/prod/accessionv";
//		Response response =httpRequest.relaxedHTTPSValidation().when().get(baseUrl+xpVersion+"/m0n1tor");
//		driver.get(baseUrl+xpVersion+"/m0n1tor");
//		logger.info("*** Step 2 Make Sure Status is - Success");
//		int statusCode = response.getStatusCode();
//		Assert.assertEquals(statusCode, 200);
//
//		String responseBody = response.getBody().asString();
//
//		JsonPath jp = new JsonPath(responseBody);
//		String appVersion= jp.get("appp");
//		String appStatus= jp.get("status");
//		logger.info("Make sure WS version and status are valid " + appVersion + appStatus);
//
//		assertEquals("prod/accessionv"+xpVersion, appVersion);
//		assertEquals("OK", appStatus);
		
		driver.close();	
		
		
	}
	@Test(priority = 1, description = "Health Check Page Status - OK")
	@Parameters({"email", "password", "oldXpVersion"})
	public void testAWS_177(String email, String password, String oldXpVersion) throws Exception {		

		logger.info("===== Testing - testAWS-175  =====");		
			
		logger.info("*** Step 1 Actions: - Navigate to Accession Webservice Health Check Page ");	
				
		RequestSpecification httpRequest = RestAssured.given();
		String baseUrl="https://www.xifinqa.com/prod/accessionv";
		
		Response response =httpRequest.relaxedHTTPSValidation().when().get(baseUrl+oldXpVersion+"/m0n1tor");
		
		logger.info("*** Step 2 Make Sure Status is - 404 Not Found");	
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 404);
		
		String responseBody = response.getBody().asString();
		logger.info("Make sure WS response is empty " + responseBody);
		assertTrue(responseBody.isEmpty());
		
		driver.close();	
		
		
	}
	

	
}	
