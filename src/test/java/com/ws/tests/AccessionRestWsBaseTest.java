package com.ws.tests;

import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.overall.accessionws.AccessionWsShareContext;
import com.xifin.qaautomation.webservices.accession.utils.AccessionWsConstant;
import com.xifin.qaautomation.webservices.accession.utils.AccessionWsUtils;
import com.xifin.qaautomation.webservices.accession.utils.FactoryHelperManager;
import com.xifin.qaautomation.webservices.test.base.RestAssuredBaseTest;
import com.xifin.webservices.accession.schema.accession.AddBadAddressErrorRequest;
import com.xifin.webservices.accession.schema.accession.AddBadAddressErrorResponse;
import com.xifin.webservices.accession.schema.accession.CreateAccessionRequest;
import com.xifin.webservices.accession.schema.accession.CreateAccessionResponse;
import com.xifin.webservices.accession.schema.accession.GetAccessionRequest;
import com.xifin.webservices.accession.schema.accession.GetAccessionResponse;
import com.xifin.webservices.accession.schema.accession.ObjectFactory;
import com.xifin.webservices.accession.schema.msgheader.MessageHeader;

public class AccessionRestWsBaseTest extends RestAssuredBaseTest {
	private ObjectFactory objectFactory;
	private MessageHeader messageHeader;
	private AccessionWsUtils accessionWsUtils;
	private String userId;
	private String accnId;

	@BeforeMethod(alwaysRun = true)
	public void preCondition() throws Exception {
		accessionWsUtils = new AccessionWsUtils(config);
		objectFactory = new ObjectFactory();
		messageHeader = accessionWsUtils.getMessageHeader(getMessageHeader());
		userId = messageHeader.getUserId();
	}

	@Test(priority = 1, description = "Create Accession With Bad Address Error")
	public void createAccessionWithBadAddressError(ITestContext context) throws Exception {
		logger.info("*** Starting test case addBadAddressError ***");

		logger.info("*** Step 1 Action: Setup data to create new accession");
		CreateAccessionRequest createAccessionRequest = objectFactory.createCreateAccessionRequest();
		createAccessionRequest.setMessageHeader(messageHeader);
		createAccessionRequest.setPayload(FactoryHelperManager.getCreateAccessionRequestHelper(config).getCreateAccessionValidPayload(userId, 1, 1, 1, 1));
		accnId = createAccessionRequest.getPayload().getCreateAccession().getAccessionId();

		logger.info("*** Step 2 Action: Send create new accn request ***");
		CreateAccessionResponse createAccessionResponse = postRequest(createAccessionRequest, AccessionWsConstant.ACCESSION_WEBSERVICE, AccessionWsConstant.CREATE_ACCESSION, CreateAccessionResponse.class);

		logger.info("*** Step 2 Expected result: Verify create new accn status response ***");
		accessionWsUtils.verifySuccessStatusType(createAccessionResponse.getPayload().getStatus());

		logger.info("*** Step 3 Action: Send GetAccession GET request ***");
		GetAccessionRequest.Payload getAccessionRequest = objectFactory.createGetAccessionRequestPayload();
		getAccessionRequest.setAccessionId(accnId);
		GetAccessionResponse getAccessionResponse = getRequest(getAccessionRequest, AccessionWsConstant.ACCESSION_WEBSERVICE, AccessionWsConstant.ACCESSION, GetAccessionResponse.class);

		logger.info("*** Step 3 Expected: Verify create accession successful by get accession request ***");
		FactoryHelperManager.getCreateAccessionResponseHelper(config).verifyAccessionCreateOrUpdateSuccessfulByGetAccession(createAccessionRequest.getPayload().getCreateAccession(), getAccessionResponse.getPayload().getGetAccessionResponseMessage());

		logger.info("*** Step 4 Action: Prepare data for Add Bad Address Error request ***");
		AddBadAddressErrorRequest addBadAddressErrorRequest = new AddBadAddressErrorRequest();
		addBadAddressErrorRequest.setMessageHeader(messageHeader);
		addBadAddressErrorRequest.setPayload(FactoryHelperManager.getAddBadAddressErrorRequestHelper().getAddBadAddressErrorRequest(accnId));

		logger.info("*** Step 5 Action: Send Add Bad Address Error request ***");
		AddBadAddressErrorResponse addBadAddressErrorResponse = postRequest(addBadAddressErrorRequest, AccessionWsConstant.ACCESSION, AccessionWsConstant.ADD_BAD_ADDRESS_ERROR, AddBadAddressErrorResponse.class);

		logger.info("*** Step 5 Expected result: Verify Add Bad Address Error response ***");
		accessionWsUtils.verifySuccessStatusType(addBadAddressErrorResponse.getPayload().getStatus());
		FactoryHelperManager.getAddBadAddressErrorResponseHelper(config).verifyAddBadAddressErrorResponse(accnId);

		logger.info("*** Step 6 Action: Sharing accession Id to another class test ***");
		context.setAttribute(AccessionWsShareContext.ACCN_ID, accnId);
	}

}
