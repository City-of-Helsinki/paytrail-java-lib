package org.helsinki.paytrail.request.paymentmethods;

import com.google.gson.Gson;
import okhttp3.RequestBody;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.PaytrailCommonTest;
import org.helsinki.paytrail.response.paymentmethods.PaytrailPaymentMethodsResponse;
import org.helsinki.paytrail.service.PaytrailSignatureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class PaytrailPaymentMethodsRequestTest extends PaytrailCommonTest {

    @Test
    void getMerchantsPaymentProviders() throws NoSuchAlgorithmException, InvalidKeyException {

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


        RequestBody requestBody = RequestBody.create(null, "");
        Assertions.assertEquals(
                "a21294097934b5ea37794c882cdc9cda5954d8aeae774c3cf629ead8915c6439",
                PaytrailSignatureService.calculateSignature(headers, requestBody, secretKey)
        );

        PaytrailPaymentMethodsRequest.PaymentMethodsPayload payload = new PaytrailPaymentMethodsRequest.PaymentMethodsPayload();

        PaytrailPaymentMethodsRequest request = new PaytrailPaymentMethodsRequest(payload);

        CompletableFuture<PaytrailPaymentMethodsResponse> response = client.sendRequest(request);


        try {
            System.out.println(gson.toJson(response.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}