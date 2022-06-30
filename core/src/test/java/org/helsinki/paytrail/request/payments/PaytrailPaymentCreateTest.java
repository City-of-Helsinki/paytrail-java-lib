package org.helsinki.paytrail.request.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.PaytrailCommonTest;
import org.helsinki.paytrail.mapper.PaytrailPaymentMethodsResponseMapper;
import org.helsinki.paytrail.model.payments.PaymentCallbackUrls;
import org.helsinki.paytrail.model.payments.PaymentCustomer;
import org.helsinki.paytrail.model.payments.PaymentItem;
import org.helsinki.paytrail.response.PaytrailResponse;
import org.helsinki.paytrail.response.paymentmethods.PaytrailPaymentMethodsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
class PaytrailPaymentCreateTest extends PaytrailCommonTest {
    PaytrailPaymentMethodsResponseMapper mapper;

    public PaytrailPaymentCreateTest() {
        this.mapper = new PaytrailPaymentMethodsResponseMapper(new ObjectMapper());
    }

    @Test
    void createTestPayment() throws ExecutionException, InterruptedException {

        TreeMap<String, String> headers = new TreeMap<>();

        headers.put("checkout-account", merchantId);
        headers.put("checkout-algorithm", "sha256");
        headers.put("checkout-method", "POST");
        headers.put("checkout-nonce", "564635208570151");
        headers.put("checkout-timestamp", "2018-07-06T10:01:31.904Z");
        //headers.put("content-type", "application/json; charset=utf-8");

        PaytrailClient client = new PaytrailClient(merchantId, secretKey);

        Gson gson = new Gson();

        LinkedHashMap<String, Object> object = new LinkedHashMap<>();
        ArrayList<Object> items = new ArrayList<>();
        LinkedHashMap<String, Object> item = new LinkedHashMap<>();
        LinkedHashMap<String, Object> customerEmail = new LinkedHashMap<>();
        LinkedHashMap<String, Object> redirectUrls = new LinkedHashMap<>();

        PaytrailPaymentCreateRequest.CreatePaymentPayload payload = new PaytrailPaymentCreateRequest.CreatePaymentPayload();

        payload.setStamp("d2568f2a-e4c6-40ba-a7cd-d573382ce548");

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
        customer.setEmail("severi.kupari@ambientia.fi");
        payload.setCustomer(customer);

        PaymentCallbackUrls callbackUrls = new PaymentCallbackUrls();

        callbackUrls.setSuccess("https://ecom.example.com/cart/success");
        callbackUrls.setCancel("https://ecom.example.com/cart/cancel");

        payload.setCallbackUrls(callbackUrls);
        payload.setRedirectUrls(callbackUrls);

//        PaytrailPaymentCreateRequest request = new PaytrailPaymentCreateRequest(payload);
//
//        //  TODO Change to createResponse
//        CompletableFuture<PaytrailResponse> response = client.sendRequest(request);
//
//        PaytrailResponse methodsResponse = mapper.to(response.get());
//
//
//        try {
//            String json = gson.toJson(response.get());
//            System.out.println(json);
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }

    }
}