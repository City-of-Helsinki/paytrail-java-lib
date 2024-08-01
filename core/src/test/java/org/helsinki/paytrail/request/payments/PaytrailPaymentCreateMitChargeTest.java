package org.helsinki.paytrail.request.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.PaytrailCommonTest;
import org.helsinki.paytrail.mapper.PaytrailPaymentCreateMitChargeResponseMapper;
import org.helsinki.paytrail.model.payments.*;
import org.helsinki.paytrail.response.payments.PaytrailPaymentCreateMitChargeResponse;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

@Slf4j
class PaytrailPaymentCreateMitChargeTest extends PaytrailCommonTest {
    private PaytrailPaymentCreateMitChargeResponseMapper mapper;
    public PaytrailPaymentCreateMitChargeTest() { this.mapper = new PaytrailPaymentCreateMitChargeResponseMapper(new ObjectMapper()); }

    private PaytrailPaymentCreateMitChargeRequest.CreateMitChargePayload createPayload(String token) {
        PaytrailPaymentCreateMitChargeRequest.CreateMitChargePayload payload = new PaytrailPaymentCreateMitChargeRequest.CreateMitChargePayload();
        payload.setStamp(UUID.randomUUID().toString());
        payload.setReference("3759170");
        payload.setAmount(1525);
        payload.setCurrency("EUR");
        payload.setLanguage("FI");

        PaymentItem paymentItem = new PaymentItem();
        paymentItem.setUnitPrice(1525);
        paymentItem.setUnits(1);
        paymentItem.setVatPercentage(24);
        paymentItem.setProductCode("#1234");
        ArrayList<PaymentItem> items1 = new ArrayList<>();
        items1.add(paymentItem);
        payload.setItems(items1);

        PaymentCustomer customer = new PaymentCustomer();
        customer.setEmail("test.customer@example.com");
        payload.setCustomer(customer);

        PaymentCallbackUrls callbackUrls = new PaymentCallbackUrls();

        callbackUrls.setSuccess("https://ecom.example.com/cart/success");
        callbackUrls.setCancel("https://ecom.example.com/cart/cancel");

        payload.setCallbackUrls(callbackUrls);
        payload.setRedirectUrls(callbackUrls);

        payload.setToken(token);

        return payload;
    }

    // TODO: Fix this test. uses token for expired card
//    @Test
//    public void createTestPayment() throws ExecutionException, InterruptedException {
//        PaytrailClient client = new PaytrailClient(merchantId, secretKey);
//        PaytrailPaymentCreateMitChargeRequest.CreateMitChargePayload payload = this.createPayload(this.cardToken1);
//
//        PaytrailPaymentCreateMitChargeRequest request = new PaytrailPaymentCreateMitChargeRequest(payload);
//        CompletableFuture<PaytrailPaymentCreateMitChargeResponse> response = client.sendRequest(request);
//
//        PaytrailPaymentCreateMitChargeResponse paymentCreateResponse = mapper.to(response.get());
//
//        try {
//            Gson gson = new Gson();
//            String json = gson.toJson(response.get());
//            System.out.println(json);
//            System.out.println(paymentCreateResponse.toString());
//
//            assertNull(paymentCreateResponse.getFailure());
//            assertNull(paymentCreateResponse.getErrors());
//            assertNotNull(paymentCreateResponse.getSuccess().getTransactionId());
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Test
    public void createTestPaymentFailureWithBogusToken() throws ExecutionException, InterruptedException {
        PaytrailClient client = new PaytrailClient(merchantId, secretKey);
        PaytrailPaymentCreateMitChargeRequest.CreateMitChargePayload payload = this.createPayload("token");

        PaytrailPaymentCreateMitChargeRequest request = new PaytrailPaymentCreateMitChargeRequest(payload);
        CompletableFuture<PaytrailPaymentCreateMitChargeResponse> response = client.sendRequest(request);

        PaytrailPaymentCreateMitChargeResponse paymentCreateResponse = mapper.to(response.get());

        try {
            Gson gson = new Gson();
            String json = gson.toJson(response.get());
            System.out.println(json);
            System.out.println(paymentCreateResponse.toString());

            assertNull(paymentCreateResponse.getSuccess());
            assertNull(paymentCreateResponse.getErrors());
            assertEquals(paymentCreateResponse.getFailure().getMessage(), "Failed to create token payment.");
            assertEquals(paymentCreateResponse.getFailure().getStatus(), "error");
            assertNull(paymentCreateResponse.getFailure().getAcquirerResponseCodeDescription());
            assertNull(paymentCreateResponse.getFailure().getAcquirerResponseCode());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createTestPaymentFailureWithInsufficientFunds() throws ExecutionException, InterruptedException {
        PaytrailClient client = new PaytrailClient(merchantId, secretKey);
        PaytrailPaymentCreateMitChargeRequest.CreateMitChargePayload payload = this.createPayload(this.cardToken2);

        PaytrailPaymentCreateMitChargeRequest request = new PaytrailPaymentCreateMitChargeRequest(payload);
        CompletableFuture<PaytrailPaymentCreateMitChargeResponse> response = client.sendRequest(request);

        PaytrailPaymentCreateMitChargeResponse paymentCreateResponse = mapper.to(response.get());

        try {
            Gson gson = new Gson();
            String json = gson.toJson(response.get());
            System.out.println(json);
            System.out.println(paymentCreateResponse.toString());

            assertNull(paymentCreateResponse.getSuccess());
            assertNull(paymentCreateResponse.getErrors());
            assertEquals(paymentCreateResponse.getFailure().getMessage(), "Failed to create token payment.");
            assertEquals(paymentCreateResponse.getFailure().getStatus(), "error");
            assertNotNull(paymentCreateResponse.getFailure().getAcquirerResponseCodeDescription());
            assertNotNull(paymentCreateResponse.getFailure().getAcquirerResponseCode());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
