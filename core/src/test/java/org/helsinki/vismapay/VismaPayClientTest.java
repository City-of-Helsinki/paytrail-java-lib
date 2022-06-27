package org.helsinki.vismapay;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.helsinki.vismapay.model.payment.*;
import org.helsinki.vismapay.request.payment.*;
import org.helsinki.vismapay.request.paymentmethods.PaymentMethodsRequest;
import org.helsinki.vismapay.response.*;
import org.helsinki.vismapay.response.payment.*;
import org.helsinki.vismapay.response.paymentmethods.PaymentMethodsResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import static junit.framework.TestCase.*;
import static junit.framework.TestCase.assertEquals;

public class VismaPayClientTest {

	private final static String MOCK_WEB_SERVER_BASE_URL = "/some-url";
	private final static String DEFAULT_CONTENT_TYPE_HEADER = "application/json; charset=" + StandardCharsets.UTF_8;

	private MockWebServer mockWebServer;
	private VismaPayClient client;
	private HttpUrl baseUrl;

	@Before
	public void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		baseUrl = mockWebServer.url(MOCK_WEB_SERVER_BASE_URL);
		client = new VismaPayClient(
				"TESTAPIKEY",
				"private_key",
				"w3.1",
				baseUrl.toString()
		);
	}

	@After
	public void teardown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	public void testGetTokenEPayment() throws Exception {
		String expectedResponseBody = read("get_token_e-payment_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<ChargeResponse> responseCF =
				client.sendRequest(getChargeRequestForGetTokenEPayment());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/auth_payment", request.getPath());
		assertEquals(
				DEFAULT_CONTENT_TYPE_HEADER,
				request.getHeader("Content-Type")
		);
		JSONAssert.assertEquals(
				read("get_token_e-payment_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		ChargeResponse response = responseCF.get();
		assertEquals("test_token", response.getToken());
		assertEquals(PaymentMethod.TYPE_EPAYMENT, response.getType());
	}

	@Test
	public void testGetTokenCard() throws Exception {
		String expectedResponseBody = read("get_token_card_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<ChargeResponse> responseCF =
				client.sendRequest(getChargeRequestForGetTokenCard());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/auth_payment", request.getPath());
		JSONAssert.assertEquals(
				read("get_token_card_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		ChargeResponse response = responseCF.get();
		assertEquals("test_token", response.getToken());
		assertEquals(Source.TYPE_CARD, response.getType());
	}

	@Test
	public void testGetStatusWithToken() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<PaymentStatusResponse> responseCF =
				client.sendRequest(getPaymentStatusRequest(true));

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/check_payment_status", request.getPath());
		JSONAssert.assertEquals(
				read("check_payment_status_with_token_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		PaymentStatusResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	@Test
	public void testGetStatusWithOrderNumber() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<PaymentStatusResponse> responseCF =
				client.sendRequest(getPaymentStatusRequest(false));

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/check_payment_status", request.getPath());
		JSONAssert.assertEquals(
				read("check_payment_status_with_order_number_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		PaymentStatusResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	@Test
	public void testGetTokenReturnsFailedResponse() throws Exception {
		arrangeMockServerResponse("kkk", 500);

		ChargeRequest.PaymentTokenPayload payload = new ChargeRequest.PaymentTokenPayload();
		payload.setAmount(BigInteger.valueOf(100))
				.setOrderNumber("a")
				.setCurrency("EUR");

		CompletableFuture<ChargeResponse> responseCF = client.sendRequest(new ChargeRequest(payload));

		ChargeResponse response = responseCF.get();
		assertNull(response.getResult());
	}

	@Test
	public void testChargeCardToken() throws Exception {
		String expectedResponseBody = read("charge_card_token_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<ChargeCardTokenResponse> responseCF =
				client.sendRequest(getChargeCardTokenRequest(false));

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/charge_card_token", request.getPath());
		JSONAssert.assertEquals(
				read("charge_card_token_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		ChargeCardTokenResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	@Test
	public void testChargeCardTokenCIT() throws Exception {
		String expectedResponseBody = read("charge_card_token_cit_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<ChargeCardTokenResponse> responseCF =
				client.sendRequest(getChargeCardTokenRequest(true));

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/charge_card_token", request.getPath());
		JSONAssert.assertEquals(
				read("charge_card_token_cit_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		ChargeCardTokenResponse response = responseCF.get();
		assertSame(30, response.getResult());
	}

	@Test
	public void testCapturePayment() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<VismaPayResponse> responseCF =
				client.sendRequest(getCapturePaymentRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/capture", request.getPath());
		JSONAssert.assertEquals(
				read("capture_payment_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		VismaPayResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	@Test
	public void testCancelPayment() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<VismaPayResponse> responseCF =
				client.sendRequest(getCancelPaymentRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/cancel", request.getPath());
		JSONAssert.assertEquals(
				read("cancel_payment_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		VismaPayResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	@Test
	public void testGetCardToken() throws Exception {
		String expectedResponseBody = read("get_card_token_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<CardTokenResponse> responseCF =
				client.sendRequest(getCardTokenRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/get_card_token", request.getPath());
		JSONAssert.assertEquals(
				read("get_card_token_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		assertCardTokenResponseValues(responseCF.get());
	}

	@Test
	public void testDeleteCardToken() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<VismaPayResponse> responseCF =
				client.sendRequest(getDeleteCardTokenRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/delete_card_token", request.getPath());
		JSONAssert.assertEquals(
				read("get_card_token_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		VismaPayResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}
	
	@Test
	public void testGetMerchantPaymentMethods() throws Exception {
		client = new VismaPayClient(
				"TESTAPIKEY",
				"private_key",
				"2",
				baseUrl.toString()
		);

		String expectedResponseBody = read("get_merchant_payment_methods_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<PaymentMethodsResponse> responseCF =
				client.sendRequest(getMerchantPaymentMethodsRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/merchant_payment_methods", request.getPath());
		JSONAssert.assertEquals(
				read("get_merchant_payment_methods_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		assertPaymentMethodsResponseValues(responseCF.get());
	}

	@Test
	public void testGetPayment() throws Exception {
		String expectedResponseBody = read("get_payment_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<PaymentDetailsResponse> responseCF =
				client.sendRequest(getGetPaymentRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/get_payment", request.getPath());
		JSONAssert.assertEquals(
				read("get_payment_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		assertPaymentDetailsResponseValues(responseCF.get());
	}

	@Test
	public void testGetRefund() throws Exception {
		String expectedResponseBody = read("get_refund_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<RefundDetailsResponse> responseCF =
				client.sendRequest(getGetRefundRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/get_refund", request.getPath());
		JSONAssert.assertEquals(
				read("get_refund_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		RefundDetailsResponse response = responseCF.get();
		assertSame(0, response.getResult());
		assertNotNull(response.getRefund());
		assertSame(1, response.getRefund().getPaymentProducts().length);
	}

	@Test
	public void testCreateRefund() throws Exception {
		String expectedResponseBody = read("create_refund_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<CreateRefundResponse> responseCF =
				client.sendRequest(getCreateRefundRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/create_refund", request.getPath());
		JSONAssert.assertEquals(
				read("create_refund_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		CreateRefundResponse response = responseCF.get();
		assertSame(0, response.getResult());
		assertEquals("instant", response.getType());
		assertEquals(Long.valueOf(2587411L), response.getRefundId());
	}

	@Test
	public void testCancelRefund() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<CancelRefundResponse> responseCF =
				client.sendRequest(getCancelRefundRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/cancel_refund", request.getPath());
		JSONAssert.assertEquals(
				read("cancel_refund_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		CancelRefundResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	private ChargeRequest getChargeRequestForGetTokenEPayment() {
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setType(PaymentMethod.TYPE_EPAYMENT)
				.setReturnUrl("https://localhost/return")
				.setNotifyUrl("https://localhost/return")
				.setSkipReceipt(false);

		Product product = new Product();
		product.setId("as123")
				.setType(ProductType.TYPE_PRODUCT)
				.setTitle("Product 1")
				.setCount(1)
				.setPretaxPrice(BigDecimal.valueOf(300))
				.setTax(24)
				.setPrice(BigDecimal.valueOf(372));

		ChargeRequest.PaymentTokenPayload payload = new ChargeRequest.PaymentTokenPayload();
		payload.setAmount(BigInteger.valueOf(100))
				.setOrderNumber("a")
				.setCurrency("EUR")
				.setPaymentMethod(paymentMethod)
				.addProduct(product);

		return new ChargeRequest(payload);
	}

	private ChargeRequest getChargeRequestForGetTokenCard() {
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setType(PaymentMethod.TYPE_CARD);

		ChargeRequest.PaymentTokenPayload payload = new ChargeRequest.PaymentTokenPayload();
		payload.setAmount(BigInteger.valueOf(100))
				.setOrderNumber("a")
				.setCurrency("EUR")
				.setPaymentMethod(paymentMethod);

		return new ChargeRequest(payload);
	}

	private CheckPaymentStatusRequest getPaymentStatusRequest(boolean useToken) {
		CheckPaymentStatusRequest.PaymentStatusPayload payload = new CheckPaymentStatusRequest.PaymentStatusPayload();

		if (useToken) {
			payload.setToken("test_token");
		} else {
			payload.setOrderNumber("test_token");
		}
		return new CheckPaymentStatusRequest(payload);
	}

	private ChargeCardTokenRequest getChargeCardTokenRequest(boolean citVersion) {
		ChargeCardTokenRequest.CardTokenPayload payload = new ChargeCardTokenRequest.CardTokenPayload();
		payload.setAmount(BigInteger.valueOf(100))
				.setOrderNumber("a")
				.setCurrency("EUR")
				.setCardToken("card_token");

		if(citVersion) {
			Initiator initiator = new Initiator().setType(Initiator.TYPE_CUSTOMER_INITIATED)
					.setReturnUrl("https://localhost/return")
					.setNotifyUrl("https://localhost/return");

			payload.setInitiator(initiator);
		}
		return new ChargeCardTokenRequest(payload);
	}

	private CapturePaymentRequest getCapturePaymentRequest() {
		CapturePaymentRequest.CapturePaymentPayload payload = new CapturePaymentRequest.CapturePaymentPayload();
		payload.setOrderNumber("a");
		return new CapturePaymentRequest(payload);
	}

	private CancelPaymentRequest getCancelPaymentRequest() {
		CancelPaymentRequest.CancelPaymentPayload payload = new CancelPaymentRequest.CancelPaymentPayload();
		payload.setOrderNumber("a");
		return new CancelPaymentRequest(payload);
	}

	private CardTokenRequest getCardTokenRequest() {
		CardTokenRequest.CardTokenPayload payload = new CardTokenRequest.CardTokenPayload();
		payload.setCardToken("card_token");
		return new CardTokenRequest(payload);
	}

	private DeleteCardTokenRequest getDeleteCardTokenRequest() {
		DeleteCardTokenRequest.DeleteCardTokenPayload payload = new DeleteCardTokenRequest.DeleteCardTokenPayload();
		payload.setCardToken("card_token");
		return new DeleteCardTokenRequest(payload);
	}

	private PaymentMethodsRequest getMerchantPaymentMethodsRequest() {
		PaymentMethodsRequest.PaymentMethodsPayload payload
				= new PaymentMethodsRequest.PaymentMethodsPayload();
		payload.setCurrency("");
		return new PaymentMethodsRequest(payload);
	}

	private GetPaymentRequest getGetPaymentRequest() {
		GetPaymentRequest.GetPaymentPayload payload = new GetPaymentRequest.GetPaymentPayload();
		payload.setOrderNumber("test_token");
		return new GetPaymentRequest(payload);
	}

	private GetRefundRequest getGetRefundRequest() {
		GetRefundRequest.GetRefundPayload payload = new GetRefundRequest.GetRefundPayload();
		payload.setRefundId((long)123);
		return new GetRefundRequest(payload);
	}

	private CreateRefundRequest getCreateRefundRequest() {
		Product product = new Product();
		product.setCount(1)
				.setProductId(123L);

		CreateRefundRequest.CreateRefundPayload payload = new CreateRefundRequest.CreateRefundPayload();
		payload.setOrderNumber("a")
				.addProduct(product);
		return new CreateRefundRequest(payload);
	}

	private CancelRefundRequest getCancelRefundRequest() {
		CancelRefundRequest.CancelRefundPayload payload = new CancelRefundRequest.CancelRefundPayload();
		payload.setRefundId(123L);
		return new CancelRefundRequest(payload);
	}

	private void assertCardTokenResponseValues(CardTokenResponse response) {
		Source source = response.getSource();

		assertSame(0, response.getResult());
		assertEquals(Source.TYPE_CARD, source.getObject());
		assertEquals("1234", source.getLast4());
		assertEquals(Short.valueOf((short)2015), source.getExpYear());
		assertSame((byte)5, source.getExpMonth());
		assertEquals("Visa", source.getBrand());
		assertEquals("card_token", source.getCardToken());
	}

	private void assertPaymentMethodsResponseValues(PaymentMethodsResponse response) {
		assertSame(0, response.getResult());
		assertSame(2, response.getPaymentMethods().length);

		org.helsinki.vismapay.model.paymentmethods.PaymentMethod method
				= response.getPaymentMethods()[0];
		assertEquals("Mobilepay", method.getName());
		assertEquals("mobilepay", method.getSelectedValue());
		assertEquals("wallets", method.getGroup());
		assertEquals("https://www.vismapay.com", method.getImg());
		assertEquals(BigInteger.valueOf(0), method.getMinAmount());
		assertEquals(BigInteger.valueOf(100000), method.getMaxAmount());
		assertEquals("1479131257", method.getImgTimestamp());
		assertEquals("Mobilepay", method.getName());

		assertSame(2, method.getCurrency().length);
		assertEquals("EUR", method.getCurrency()[0]);
	}

	private void assertPaymentDetailsResponseValues(PaymentDetailsResponse response) {
		assertSame(0, response.getResult());

		Payment payment = response.getPayment();
		assertSame(3, payment.getPaymentProducts().length);
		assertSame(1, payment.getRefunds().length);
		assertNotNull(payment.getCustomer());
		assertNotNull(payment.getSource());

		assertEquals(Long.valueOf((long)123), payment.getId());
		assertEquals(BigDecimal.valueOf((long)562), payment.getAmount());
		assertEquals("EUR", payment.getCurrency());
		assertEquals("test_order_1", payment.getOrderNumber());
		assertEquals(Short.valueOf((short)4), payment.getStatus());
		assertEquals("email", payment.getRefundType());
	}

	private void arrangeMockServerResponse(String body) {
		arrangeMockServerResponse(body, 200);
	}

	private void arrangeMockServerResponse(String body, int code) {
		mockWebServer.enqueue(new MockResponse()
				.addHeader("Content-Type", DEFAULT_CONTENT_TYPE_HEADER)
				.setBody(body)
				.setResponseCode(code));
	}

	private String read(String fileName) throws Exception {
		Path filePath = Paths.get("src","test","resources", "org.helsinki.vismapay", fileName);

		byte[] bytes = Files.readAllBytes(filePath);
		return new String(bytes, StandardCharsets.UTF_8);
	}
}