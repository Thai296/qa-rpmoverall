package com.ws.tests;

import com.overall.eligibilityws.EligibilityWSIndexPage;
import com.overall.menu.MenuNavigation;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SmokeEligibilityWSTest extends SeleniumBaseTest
{
	private MenuNavigation navigation;
	private EligibilityWSIndexPage eligibilityWSIndexPage;

	@Test(priority = 1, description = "Index Page")
	@Parameters({})
	public void testEligWS_05() throws Exception {
		logger.info("Testing - testEligWS_05");
		navigation = new MenuNavigation(driver,config);
		eligibilityWSIndexPage =  new EligibilityWSIndexPage(driver);
		navigation.navigateToEligibilityWsIndexPage();

// todo: afa fix		logger.info("Verify that the Eligibility WS Index page displays Eligibility Webservice WSDL Link and Header version="+xpVersion);
//		wait.until(ExpectedConditions.visibilityOf(eligibilityWSIndexPage.eligibilityWSLink()));
//		Assert.assertEquals(eligibilityWSIndexPage.eligibilityWSLink().getText().trim(),"Eligibility Webservice WSDL");
//		Assert.assertTrue(eligibilityWSIndexPage.eligibilityWSHeading().getText().replace(".", "").contains(xpVersion));
//
//		logger.info("Click Eligibility Webservice WSDL link");
//		eligibilityWSIndexPage.clickEligibilityWSLink();
//		wait.until(ExpectedConditions.urlContains("eligibility.wsdl"));
//		logger.info("Verify that the Eligibility Webservice WSDL page displays properly");
//		Assert.assertTrue(driver.getCurrentUrl().contains(xpVersion));
//		// Parse the URL
//		RequestSpecification httpRequest = RestAssured.given();
//		Response response = httpRequest.relaxedHTTPSValidation().when().get(driver.getCurrentUrl());
//		String respon = response.asString();
//		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(respon.getBytes("utf-8"))));
//		// Get the element from response using element tag name
//		String sch0 = doc.getDocumentElement().getAttribute("xmlns:sch0");
//		// Verify the response content using Assert
//		Assert.assertEquals(sch0, "http://www.xifin.com/schema/eligibility");
//		driver.close();

	}

	@Test(priority = 1, description = "Health Check Page Status - OK")
	@Parameters({})
	public void testEligWS_06()  {

// todo: afa fix		logger.info("Testing - testEligWS_06");
//
//		logger.info("Navigate to Eligibility Webservice Health Check Page");
//		RequestSpecification httpRequest = RestAssured.given();
//		String heathCheckUrl = StringUtils.replace(config.getProperty(PropertyMap.ELIGWS_URL), "/services?", "/m0n1tor?");
//		Response response = httpRequest.relaxedHTTPSValidation().when().get(heathCheckUrl);
//		driver.get(heathCheckUrl);
//		logger.info("Make Sure Status is - Success, Url="+baseUrl+xpVersion+"/m0n1tor");
//		int statusCode = response.getStatusCode();
//		Assert.assertEquals(statusCode, 200);
//		String responseBody = response.getBody().asString();
//		JsonPath jp = new JsonPath(responseBody);
//		String appVersion= jp.get("appp");
//		String appStatus= jp.get("status");
//		logger.info("Make sure WS version and status are valid " + appVersion + appStatus);
//		Assert.assertEquals("prod/eligibilityv"+xpVersion, appVersion);
//		Assert.assertEquals("OK", appStatus);
//		driver.close();
}

	@Test(priority = 1, description = "Health Check Page Status - 404 Not Found, response is empty")
	@Parameters({"oldXpVersion"})
	public void testEligWS_07(String oldXpVersion)  {

		logger.info("Testing - testEligWS_07");
		logger.info("Navigate to Eligibility Webservice Health Check Page");
				
		RequestSpecification httpRequest = RestAssured.given();
		String baseUrl = config.getProperty(PropertyMap.ELIGWS_URL);
		logger.info("Loaded eligibility WS base URL, url="+baseUrl);
		baseUrl = StringUtils.replacePattern(baseUrl, "v\\d+/services", "v0000");
		logger.info("Constructed undeployed eligibility WS base URL, url="+baseUrl);
		Response response = httpRequest.relaxedHTTPSValidation().when().get(baseUrl + "/m0n1tor");

		logger.info("Make Sure Status is - 404 Not Found, Url="+baseUrl+oldXpVersion+"/m0n1tor");
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 404);
		String responseBody = response.getBody().asString();
		logger.info("Make sure WS response is empty " + responseBody);
		Assert.assertTrue(responseBody.isEmpty());
		driver.close();

	}
}