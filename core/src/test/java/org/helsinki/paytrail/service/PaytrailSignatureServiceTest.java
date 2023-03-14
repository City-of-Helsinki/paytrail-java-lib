package org.helsinki.paytrail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.helsinki.paytrail.PaytrailCommonTest;
import org.helsinki.paytrail.request.payments.PaytrailPaymentCreateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

@Slf4j
class PaytrailSignatureServiceTest extends PaytrailCommonTest {


    @Test
    void calculateHeadersOnlySignature() throws NoSuchAlgorithmException, InvalidKeyException {
        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("checkout-account", merchantId);
        headers.put("checkout-algorithm", "sha256");
        headers.put("checkout-method", "POST");
        headers.put("checkout-nonce", "564635208570151");
        headers.put("checkout-timestamp", "2018-07-06T10:01:31.904Z");
        RequestBody requestBody = RequestBody.create(null, "");
        Assertions.assertEquals(
                "a21294097934b5ea37794c882cdc9cda5954d8aeae774c3cf629ead8915c6439",
                PaytrailSignatureService.calculateSignature(headers, requestBody, secretKey)
        );
    }

    @Test
    void calculateSignatureWithBody() throws NoSuchAlgorithmException, InvalidKeyException {
        TreeMap<String, String> headers = new TreeMap<>();

        headers.put("checkout-account", merchantId);
        headers.put("checkout-algorithm", "sha256");
        headers.put("checkout-method", "POST");
        headers.put("checkout-nonce", "564635208570151");
        headers.put("checkout-timestamp", "2018-07-06T10:01:31.904Z");
        Gson gson = new Gson();

        LinkedHashMap<String, Object> object = new LinkedHashMap<>();
        ArrayList<Object> items = new ArrayList<>();
        LinkedHashMap<String, Object> item = new LinkedHashMap<>();
        LinkedHashMap<String, Object> customerEmail = new LinkedHashMap<>();
        LinkedHashMap<String, Object> redirectUrls = new LinkedHashMap<>();

        object.put("stamp", "unique-identifier-for-merchant");
        object.put("reference", "3759170");
        object.put("amount", 1525);
        object.put("currency", "EUR");
        object.put("language", "FI");

        item.put("unitPrice", 1525);
        item.put("units", 1);
        item.put("vatPercentage", 24);
        item.put("productCode", "#1234");
        item.put("deliveryDate", "2018-09-01");
        items.add(item);

        object.put("items", items);

        customerEmail.put("email", "test.customer@example.com");

        redirectUrls.put("success", "https://ecom.example.com/cart/success");
        redirectUrls.put("cancel", "https://ecom.example.com/cart/cancel");

        object.put("items", items);
        object.put("customer", customerEmail);
        object.put("redirectUrls", redirectUrls);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=" + StandardCharsets.UTF_8), gson.toJson(object));
        Assertions.assertEquals(
                "3708f6497ae7cc55a2e6009fc90aa10c3ad0ef125260ee91b19168750f6d74f6",
                PaytrailSignatureService.calculateSignature(headers, requestBody, secretKey)
        );
    }

    @Test
    void filterRequestAuthenticationHeaders() {
        TreeMap<String, String> headers = new TreeMap<>();
        TreeMap<String, String> filteredHeaders = new TreeMap<>();

        String shouldBeFiltered = "Should-not-be-found";
        headers.put(shouldBeFiltered, merchantId);
        headers.put("checkout-account", merchantId);
        headers.put("checkout-algorithm", "sha256");
        headers.put("checkout-method", "POST");
        headers.put("checkout-nonce", "564635208570151");
        headers.put("checkout-timestamp", "2018-07-06T10:01:31.904Z");
        Request.Builder request = new Request.Builder();
        request.url("https://www.google.com/");
        headers.forEach(request::addHeader);
        PaytrailSignatureService.filterRequestAuthenticationHeaders(request.build(), filteredHeaders);
        Assertions.assertNotEquals(headers, filteredHeaders);
        Assertions.assertNotNull(filteredHeaders);
        Assertions.assertEquals(filteredHeaders.size(), 5);
        Assertions.assertEquals(headers.size(), 6);
    }

    @Test
    void filterRequestQueryParameters() {
        TreeMap<String, String> queryParams = new TreeMap<>();
        TreeMap<String, String> filteredQueryParameters = new TreeMap<>();

        String shouldBeFiltered = "Should-not-be-found";
        queryParams.put(shouldBeFiltered, merchantId);
        queryParams.put("checkout-account", merchantId);
        queryParams.put("checkout-algorithm", "sha256");
        queryParams.put("checkout-method", "POST");
        queryParams.put("checkout-nonce", "564635208570151");
        queryParams.put("checkout-timestamp", "2018-07-06T10:01:31.904Z");
        Request.Builder request = new Request.Builder();
        request.url("https://www.google.com/");
        HttpUrl.Builder builder = request.build().url().newBuilder();
        queryParams.forEach(builder::addQueryParameter);

        request.url(builder.build());

        PaytrailSignatureService.filterRequestQueryParameters(request.build(), filteredQueryParameters);
        Assertions.assertNotEquals(queryParams, filteredQueryParameters);
        Assertions.assertNotNull(filteredQueryParameters);
        Assertions.assertEquals(filteredQueryParameters.size(), 5);
        Assertions.assertEquals(queryParams.size(), 6);
    }


    @Test
    void calculateSignatureWithBodyRealCase() throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        TreeMap<String, String> headers = new TreeMap<>();


        ObjectMapper objectMapper = new ObjectMapper();

        headers.put("checkout-account", "1071053");
        headers.put("checkout-algorithm", "sha256");
        headers.put("checkout-method", "POST");
        headers.put("checkout-nonce", "88e166e2-2ee5-4617-a209-8ba1855d91ce");
        headers.put("checkout-timestamp", "2023-03-14T07:44:03.305611");

        PaytrailPaymentCreateRequest.CreatePaymentPayload object = objectMapper.readValue("{\"version\":null,\"stamp\":\"760ab91a-8dfe-3fde-8e10-7587ee2a40f7_at_20230314-074402\",\"reference\":\"760ab91a-8dfe-3fde-8e10-7587ee2a40f7\",\"amount\":12400,\"currency\":\"EUR\",\"language\":\"FI\",\"items\":[{\"stamp\":null,\"reference\":\"8d940cc9-4fb2-4846-a718-59a8c5ea7e60\",\"merchant\":null,\"unitPrice\":12400,\"units\":1,\"vatPercentage\":24,\"productCode\":\"b56382ff-02b2-32f1-848f-981f12faf8cd\",\"description\":\"J?rjest?tila\",\"orderId\":null}],\"customer\":{\"email\":\"severi.kupari@ambientia.fi\",\"firstName\":\"Severi\",\"lastName\":\"Kupari\",\"phone\":null},\"redirectUrls\":{\"success\":\"https://checkout-test-api.test.hel.ninja/v1/payment/paytrailOnlinePayment/return/success\",\"cancel\":\"https://checkout-test-api.test.hel.ninja/v1/payment/paytrailOnlinePayment/return/cancel\"},\"callbackUrls\":{\"success\":\"https://checkout-test-api.test.hel.ninja/v1/payment/paytrailOnlinePayment/notify/success\",\"cancel\":\"https://checkout-test-api.test.hel.ninja/v1/payment/paytrailOnlinePayment/notify/cancel\"}}",PaytrailPaymentCreateRequest.CreatePaymentPayload.class);

        log.info(objectMapper.writeValueAsString(object));

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=" + StandardCharsets.UTF_8), objectMapper.writeValueAsString(object));
        Assertions.assertEquals(
                "938149a93c7cd63394234fb6d9e24eeb4f26ecfda21d8d289df02207b894198f",
                PaytrailSignatureService.calculateSignature(headers, requestBody, "226a735c07fc99bb463fc1a77d0fb7fb01a006449262ffa579841bc2539dc3ba094a183936467965")
        );
    }

}