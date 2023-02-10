package org.helsinki.paytrail.request.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.PaytrailCommonTest;
import org.helsinki.paytrail.mapper.PaytrailPaymentCreateResponseMapper;
import org.helsinki.paytrail.mapper.PaytrailPaymentGetResponseMapper;
import org.helsinki.paytrail.model.payments.PaymentCallbackUrls;
import org.helsinki.paytrail.model.payments.PaymentCustomer;
import org.helsinki.paytrail.model.payments.PaymentItem;
import org.helsinki.paytrail.model.payments.PaytrailPayment;
import org.helsinki.paytrail.response.payments.PaytrailPaymentCreateResponse;
import org.helsinki.paytrail.response.payments.PaytrailPaymentGetResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class PaytrailPaymentGetTest extends PaytrailCommonTest {
    private PaytrailPaymentGetResponseMapper getPaymentMapper;
    private PaytrailPaymentCreateResponseMapper createPaymentMapper;

    public PaytrailPaymentGetTest() {
        this.getPaymentMapper = new PaytrailPaymentGetResponseMapper(new ObjectMapper());
        this.createPaymentMapper = new PaytrailPaymentCreateResponseMapper(new ObjectMapper());
    }

    private PaytrailPaymentCreateResponse createPayment(PaytrailClient client) throws ExecutionException, InterruptedException {
        PaytrailPaymentCreateRequest.CreatePaymentPayload payload = new PaytrailPaymentCreateRequest.CreatePaymentPayload();

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

        PaytrailPaymentCreateRequest request = new PaytrailPaymentCreateRequest(payload);
        CompletableFuture<PaytrailPaymentCreateResponse> response = client.sendRequest(request);

        return createPaymentMapper.to(response.get());
    }

    @Test
    public void getTestPayment() throws ExecutionException, InterruptedException {
        PaytrailClient client = new PaytrailClient(merchantId, secretKey);
        PaytrailPaymentCreateResponse createdPayment = this.createPayment(client);
        PaytrailPaymentGetRequest request = new PaytrailPaymentGetRequest(createdPayment.getPaymentResponse().getTransactionId());
        CompletableFuture<PaytrailPaymentGetResponse> response = client.sendRequest(request);
        PaytrailPaymentGetResponse paymentGetResponse = getPaymentMapper.to(response.get());
        try {
            Gson gson = new Gson();
            String json = gson.toJson(response.get());
            System.out.println(json);
            System.out.println(paymentGetResponse.toString());

            PaytrailPayment paytrailPayment = paymentGetResponse.getPaytrailPayment();
            assertNull(paymentGetResponse.getErrors());
            assertNotNull(paytrailPayment.getTransactionId());
            assertNotNull(paytrailPayment.getStamp());
            assertNotNull(paytrailPayment.getAmount());
            assertNotNull(paytrailPayment.getStatus());
            assertNotNull(paytrailPayment.getCurrency());
            assertNotNull(paytrailPayment.getReference());
            assertNotNull(paytrailPayment.getCreatedAt());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
