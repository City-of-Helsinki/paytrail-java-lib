package org.helsinki.paytrail.request.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.PaytrailCommonTest;
import org.helsinki.paytrail.mapper.ConfiguredObjectMapper;
import org.helsinki.paytrail.mapper.PaytrailPaymentCreateResponseMapper;
import org.helsinki.paytrail.model.payments.PaymentCallbackUrls;
import org.helsinki.paytrail.model.payments.PaymentCustomer;
import org.helsinki.paytrail.model.payments.PaymentItem;
import org.helsinki.paytrail.model.payments.PaytrailPaymentResponse;
import org.helsinki.paytrail.response.payments.PaytrailPaymentCreateResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        customer.setEmail("test.customer@example.com");
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
            assertTrue(paymentResponseDto.getGroups().size() > 1);
            assertTrue(paymentResponseDto.getProviders().size() > 1);
            assertEquals("https://pay.paytrail.com/pay/" + paymentResponseDto.getTransactionId(), paymentResponseDto.getHref());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void createTestPaymentSignature() throws ExecutionException, InterruptedException, JsonProcessingException {
        PaytrailClient client = new PaytrailClient(merchantId, secretKey);

        ObjectMapper mapper1 = ConfiguredObjectMapper.getMapper();

        PaytrailPaymentCreateRequest.CreatePaymentPayload payload = mapper1.readValue("{\"stamp\":\"b149f16e-7880-3423-871e-4f067b25e863_at_20230314-140651\",\"reference\":\"b149f16e-7880-3423-871e-4f067b25e863\",\"amount\":12400,\"currency\":\"EUR\",\"language\":\"FI\",\"items\":[{\"reference\":\"a33e3feb-7ab9-47a4-9128-5b1b91cea077\",\"unitPrice\":12400,\"units\":1,\"vatPercentage\":24,\"productCode\":\"b56382ff-02b2-32f1-848f-981f12faf8cd\",\"description\":\"J?rjest?tila\"}],\"customer\":{\"email\":\"severi.kupari@ambientia.fi\",\"firstName\":\"Severi\",\"lastName\":\"Kupari\"},\"redirectUrls\":{\"success\":\"https://checkout-test-api.test.hel.ninja/v1/payment/paytrailOnlinePayment/return/success\",\"cancel\":\"https://checkout-test-api.test.hel.ninja/v1/payment/paytrailOnlinePayment/return/cancel\"},\"callbackUrls\":{\"success\":\"https://checkout-test-api.test.hel.ninja/v1/payment/paytrailOnlinePayment/notify/success\",\"cancel\":\"https://checkout-test-api.test.hel.ninja/v1/payment/paytrailOnlinePayment/notify/cancel\"}}", PaytrailPaymentCreateRequest.CreatePaymentPayload.class);
        PaytrailPaymentCreateRequest request = new PaytrailPaymentCreateRequest(payload);
        CompletableFuture<PaytrailPaymentCreateResponse> response = client.sendRequest(request);

        PaytrailPaymentCreateResponse paymentCreateResponse = mapper.to(response.get());

        try {
            Gson gson = new Gson();
            String json = gson.toJson(response.get());
            System.out.println(json);
            System.out.println(paymentCreateResponse.toString());

            PaytrailPaymentResponse paymentResponseDto = paymentCreateResponse.getPaymentResponse();
            assertTrue(paymentResponseDto.getGroups().size() > 1);
            assertTrue(paymentResponseDto.getProviders().size() > 1);
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
        customer.setEmail("test.customer@example.com");
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
            assertTrue(paymentResponseDto.getGroups().size() > 1);
            assertTrue(paymentResponseDto.getProviders().size() > 1);
            assertEquals("https://pay.paytrail.com/pay/" + paymentResponseDto.getTransactionId(), paymentResponseDto.getHref());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}