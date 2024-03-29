package org.helsinki.paytrail.request.refunds;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.PaytrailCommonTest;
import org.helsinki.paytrail.mapper.PaytrailPaymentCreateResponseMapper;
import org.helsinki.paytrail.mapper.PaytrailRefundCreateResponseMapper;
import org.helsinki.paytrail.model.payments.PaymentCallbackUrls;
import org.helsinki.paytrail.model.payments.PaymentCustomer;
import org.helsinki.paytrail.model.payments.PaymentItem;
import org.helsinki.paytrail.model.payments.PaytrailPaymentResponse;
import org.helsinki.paytrail.model.refunds.RefundItem;
import org.helsinki.paytrail.request.payments.PaytrailPaymentCreateRequest;
import org.helsinki.paytrail.response.payments.PaytrailPaymentCreateResponse;
import org.helsinki.paytrail.response.refunds.PaytrailRefundCreateResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class PaytrailRefundCreateTest extends PaytrailCommonTest {

    private PaytrailRefundCreateResponseMapper refundMapper;
    private PaytrailPaymentCreateResponseMapper paymentMapper;

    public PaytrailRefundCreateTest() {
        this.refundMapper = new PaytrailRefundCreateResponseMapper(new ObjectMapper());
        this.paymentMapper = new PaytrailPaymentCreateResponseMapper(new ObjectMapper());
    }

    @Test
    public void createTestRefundNormalMerchant() throws ExecutionException, InterruptedException {
        PaytrailClient client = new PaytrailClient(merchantId, secretKey);

        /* Creating a test payment */
        PaytrailPaymentResponse paymentResponse = createTestNormalMerchantPayment(client);

        PaytrailRefundCreateRequest.CreateRefundPayload payload = new PaytrailRefundCreateRequest.CreateRefundPayload();

        String refundStamp = UUID.randomUUID().toString();
        String refundReference = "3759170";

        payload.setRefundStamp(refundStamp);
        payload.setRefundReference(refundReference);
        payload.setAmount(10);
        payload.setEmail("test.customer@example.com");

        PaymentCallbackUrls callbackUrls = new PaymentCallbackUrls();

        callbackUrls.setSuccess("https://ecom.example.com/cart/success");
        callbackUrls.setCancel("https://ecom.example.com/cart/cancel");
        payload.setCallbackUrls(callbackUrls);

        PaytrailRefundCreateRequest request = new PaytrailRefundCreateRequest(paymentResponse.getTransactionId(), payload);
        CompletableFuture<PaytrailRefundCreateResponse> response = client.sendRequest(request);

        PaytrailRefundCreateResponse refundCreateResponse = response.get();
        PaytrailRefundCreateResponse refundResponse = refundMapper.to(refundCreateResponse);
        assertEquals(refundCreateResponse.getResultJson(),"{\"status\":\"error\",\"message\":\"Transaction not paid\"}");

    }

    @Test
    public void createTestRefundShopInShop() throws ExecutionException, InterruptedException {
        PaytrailClient client = new PaytrailClient(aggregateMerchantId, aggregateSecretKey);
        client.setCustomerMerchantId(shopInShopMerchantId);

        /* Creating a test payment */
        PaytrailPaymentResponse paymentResponse = createTestShopInShopPayment(client);

        PaytrailRefundCreateRequest.CreateRefundPayload payload = new PaytrailRefundCreateRequest.CreateRefundPayload();

        String refundStamp = UUID.randomUUID().toString();
        String refundReference = "3759170";

        payload.setRefundStamp(refundStamp);
        payload.setRefundReference(refundReference);
        payload.setAmount(10);
        payload.setEmail("test.customer@example.com");

        RefundItem refundItem = new RefundItem();
        refundItem.setStamp(UUID.randomUUID().toString());
        refundItem.setRefundStamp(refundStamp);
        refundItem.setRefundReference(refundReference);
        refundItem.setAmount(10);
        List<RefundItem> items = new ArrayList<>();
        items.add(refundItem);
        payload.setItems(items);

        PaymentCallbackUrls callbackUrls = new PaymentCallbackUrls();

        callbackUrls.setSuccess("https://ecom.example.com/cart/success");
        callbackUrls.setCancel("https://ecom.example.com/cart/cancel");
        payload.setCallbackUrls(callbackUrls);

        PaytrailRefundCreateRequest request = new PaytrailRefundCreateRequest(paymentResponse.getTransactionId(), payload);
        CompletableFuture<PaytrailRefundCreateResponse> response = client.sendRequest(request);

        PaytrailRefundCreateResponse refundCreateResponse = response.get();
        PaytrailRefundCreateResponse refundResponse = refundMapper.to(refundCreateResponse);
        assertEquals(refundCreateResponse.getResultJson(),"{\"status\":\"error\",\"message\":\"Transaction not paid\"}");
    }

    private PaytrailPaymentResponse createTestNormalMerchantPayment(PaytrailClient client) throws ExecutionException, InterruptedException {
        PaytrailPaymentCreateRequest.CreatePaymentPayload payload = new PaytrailPaymentCreateRequest.CreatePaymentPayload();

        payload.setStamp(UUID.randomUUID().toString());
        payload.setReference("3759170");
        payload.setAmount(10);
        payload.setCurrency("EUR");
        payload.setLanguage("FI");

        PaymentItem paymentItem = new PaymentItem();
        paymentItem.setUnitPrice(10);
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

        PaytrailPaymentCreateResponse paymentCreateResponse = paymentMapper.to(response.get());

        return paymentCreateResponse.getPaymentResponse();
    }

    private PaytrailPaymentResponse createTestShopInShopPayment(PaytrailClient client) throws ExecutionException, InterruptedException {
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

        PaytrailPaymentCreateResponse paymentCreateResponse = paymentMapper.to(response.get());
        return paymentCreateResponse.getPaymentResponse();
    }
}