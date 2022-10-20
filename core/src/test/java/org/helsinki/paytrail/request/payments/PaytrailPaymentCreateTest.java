package org.helsinki.paytrail.request.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.PaytrailCommonTest;
import org.helsinki.paytrail.mapper.PaytrailPaymentCreateResponseMapper;
import org.helsinki.paytrail.model.payments.PaymentCallbackUrls;
import org.helsinki.paytrail.model.payments.PaymentCustomer;
import org.helsinki.paytrail.model.payments.PaymentItem;
import org.helsinki.paytrail.response.payments.PaytrailPaymentCreateResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
class PaytrailPaymentCreateTest extends PaytrailCommonTest {

    private PaytrailPaymentCreateResponseMapper mapper;

    public PaytrailPaymentCreateTest() {
        this.mapper = new PaytrailPaymentCreateResponseMapper(new ObjectMapper());
    }

    @Test
    public void createTestPayment() throws ExecutionException, InterruptedException {
        PaytrailClient client = new PaytrailClient(merchantId, secretKey);

        PaytrailPaymentCreateRequest.CreatePaymentPayload payload = new PaytrailPaymentCreateRequest.CreatePaymentPayload();

        payload.setStamp("d2568f2a-e4c6-40ba-a7cd-d573382ce549");

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

        PaytrailPaymentCreateRequest request = new PaytrailPaymentCreateRequest(payload);
        CompletableFuture<PaytrailPaymentCreateResponse> response = client.sendRequest(request);

        PaytrailPaymentCreateResponse paymentCreateResponse = mapper.to(response.get());

        try {
            Gson gson = new Gson();
            String json = gson.toJson(response.get());
            System.out.println(json);
            System.out.println(paymentCreateResponse.toString());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void createTestShopInShopPayment() throws ExecutionException, InterruptedException {
        PaytrailClient client = new PaytrailClient(aggregateMerchantId, shopInShopSecretKey);

        PaytrailPaymentCreateRequest.CreatePaymentPayload payload = new PaytrailPaymentCreateRequest.CreatePaymentPayload();

        payload.setStamp("16502c48-8126-4dd1-9bd5-0666328de984");
        payload.setReference("3759170");
        payload.setAmount(1525);
        payload.setCurrency("EUR");
        payload.setLanguage("FI");
        PaymentItem paymentItem = new PaymentItem();

        paymentItem.setStamp("67641606-1c74-4994-bd6c-ee40436d398h");
        paymentItem.setReference("fur-suits-5");
        paymentItem.setUnitPrice(1525);
        paymentItem.setUnits(1);
        paymentItem.setVatPercentage(24);
        paymentItem.setProductCode("#1234");
        paymentItem.setMerchant(shopInShopMerchantId);
        ArrayList<PaymentItem> items1 = new ArrayList<>();
        items1.add(paymentItem);
        payload.setItems(items1);

        PaymentCustomer customer = new PaymentCustomer();
        customer.setEmail("martin.lehtomaa@ambientia.fi");
        payload.setCustomer(customer);

        PaymentCallbackUrls callbackUrls = new PaymentCallbackUrls();

        callbackUrls.setSuccess("https://ecom.example.com/cart/success");
        callbackUrls.setCancel("https://ecom.example.com/cart/cancel");

        payload.setCallbackUrls(callbackUrls);
        payload.setRedirectUrls(callbackUrls);

        PaytrailPaymentCreateRequest request = new PaytrailPaymentCreateRequest(payload);
        CompletableFuture<PaytrailPaymentCreateResponse> response = client.sendRequest(request);

        PaytrailPaymentCreateResponse paymentCreateResponse = mapper.to(response.get());

        try {
            Gson gson = new Gson();
            String json = gson.toJson(response.get());
            System.out.println(json);
            System.out.println(paymentCreateResponse.toString());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}