package org.helsinki.paytrail.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.helsinki.paytrail.PaytrailCommonTest;
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

}