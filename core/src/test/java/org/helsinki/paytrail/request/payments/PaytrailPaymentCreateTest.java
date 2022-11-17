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
import org.helsinki.paytrail.model.payments.PaytrailPaymentResponse;
import org.helsinki.paytrail.response.payments.PaytrailPaymentCreateResponse;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;
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

            PaytrailPaymentResponse paymentResponseDto = paymentCreateResponse.getPaymentResponse();
            assertEquals(4, paymentResponseDto.getGroups().size());
            assertEquals(17, paymentResponseDto.getProviders().size());
            assertEquals("https://pay.paytrail.com/pay/" + paymentResponseDto.getTransactionId(), paymentResponseDto.getHref());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void createTestShopInShopPayment() throws ExecutionException, InterruptedException {
        PaytrailClient client = new PaytrailClient(aggregateMerchantId, aggregateSecretKey);

        PaytrailPaymentCreateRequest.CreatePaymentPayload payload = new PaytrailPaymentCreateRequest.CreatePaymentPayload();

        payload.setStamp(UUID.randomUUID().toString());
        payload.setReference("3759170");
        payload.setAmount(1525);
        payload.setCurrency("EUR");
        payload.setLanguage("FI");
        PaymentItem paymentItem = new PaymentItem();

        paymentItem.setStamp(UUID.randomUUID().toString());
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

            PaytrailPaymentResponse paymentResponseDto = paymentCreateResponse.getPaymentResponse();
            assertEquals(4, paymentResponseDto.getGroups().size());
            assertEquals(17, paymentResponseDto.getProviders().size());
            assertEquals("https://pay.paytrail.com/pay/" + paymentResponseDto.getTransactionId(), paymentResponseDto.getHref());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}