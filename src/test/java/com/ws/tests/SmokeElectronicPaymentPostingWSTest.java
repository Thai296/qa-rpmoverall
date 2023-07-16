package com.ws.tests;



import com.overall.eppws.EppWSIndexPage;
import com.overall.menu.MenuNavigation;
import com.xifin.utils.SeleniumBaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class SmokeElectronicPaymentPostingWSTest extends SeleniumBaseTest {


	private MenuNavigation navigation;
	private EppWSIndexPage eppWSIndexPage;
	

	@Test(priority = 1, description = "IndexPage")
	@Parameters({"email", "password"})
	public void testEPWS_10(String email, String password) throws Exception {		
		logger.info("===== Testing - testEPWS_10 =====");		
		navigation = new MenuNavigation(driver, config);
		eppWSIndexPage =  new EppWSIndexPage(driver);
		
		logger.info("*** Actions: - Navigate to ElectronicPaymentPosting Webservice Index page ");				
		navigation.navigateToElectronicPaymentPostingWSPage();
		Thread.sleep(1000);
		
		logger.info("*** Expected Results: - Verify that the ElectronicPaymentPosting Webservice Index page displays properly");
		assertTrue(isElementPresent(eppWSIndexPage.eppWSLink(), 5),"        ElectronicPaymentPosting Webservice Index page is displayed.");		
		assertEquals(eppWSIndexPage.eppWSLink().getText().trim(),"Electronic Payment Posting Webservice WSDL", "        ElectronicPaymentPosting Webservice WSDL link is displayed.");
		
		logger.info("*** Actions: - Click ElectronicPaymentPosting Webservice WSDL link ");
		eppWSIndexPage.clickEPPWSLink();
		
		logger.info("*** Expected Results: - Verify that the ElectronicPaymentPosting Webservice WSDL page displays properly");		
		// Parse the URL
		RequestSpecification httpRequest = RestAssured.given();
		Response response =httpRequest.relaxedHTTPSValidation().when().get(driver.getCurrentUrl());
		String respon = response.asString();
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(respon.getBytes("utf-8"))));
		// Get the element from response using element tag name
		String sch0 = doc.getDocumentElement().getAttribute("xmlns:sch4");
		// Verify the response content using Assert
		assertEquals("http://www.xifin.com/schema/electronicpaymentposting",sch0);

		driver.close();	
	}

	@Test(priority = 1, description = "HealthCheckPage-Status OK")
	@Parameters({"email", "password"})
	public void testEPWS_12(String email, String password) throws Exception {
		logger.info("===== Testing - testEPWS_12 =====");		
//
//		logger.info("*** Actions: - Navigate to Electronic Payment Posting Webservice Health Check Page ");
//		RequestSpecification httpRequest = RestAssured.given();
//		String baseUrl="https://www.xifinqa.com/prod/electronicpaymentpostingv";
////		Response response =httpRequest.relaxedHTTPSValidation().when().get(baseUrl+xpVersion+"/m0n1tor");
////		driver.get(baseUrl+xpVersion+"/m0n1tor");
//
//		logger.info("*** Expected Results: - Make Sure Status is - Success");
//		int statusCode = response.getStatusCode();
//		assertEquals(statusCode,200);
//
//		String responseBody = response.getBody().asString();
//
//		JsonPath jp = new JsonPath(responseBody);
//		String appVersion= jp.get("appp");
//		String appStatus= jp.get("status");
//		logger.info("Make sure WS version and status are valid " + appVersion + appStatus);
//
//		assertEquals(appVersion,"prod/electronicpaymentpostingv");
//		assertEquals(appStatus,"OK");
		
		driver.close();			
	}
	
}	
