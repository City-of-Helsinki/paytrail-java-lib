package org.helsinki.paytrail.request.payments;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.model.payments.PaymentCallbackUrls;
import org.helsinki.paytrail.model.payments.PaymentCustomer;
import org.helsinki.paytrail.model.payments.PaymentItem;
import org.helsinki.paytrail.request.common.PaytrailPostRequest;
import org.helsinki.paytrail.request.contracts.paytrail.PaytrailPayload;
import org.helsinki.paytrail.response.PaytrailResponse;
import org.helsinki.paytrail.response.paymentmethods.PaytrailPaymentMethodsResponse;
import org.helsinki.paytrail.response.payments.PaytrailPaymentCreateResponse;

import java.util.ArrayList;

@RequiredArgsConstructor
public class PaytrailPaymentCreateRequest extends PaytrailPostRequest<PaytrailPaymentCreateResponse, PaytrailPaymentCreateRequest.CreatePaymentPayload> {

    @NonNull
    private final PaytrailPaymentCreateRequest.CreatePaymentPayload payload;

    @Override
    protected CreatePaymentPayload getPayload(PaytrailClient client) {
        return payload;
    }

    @Override
    public String path() {
        return "payments";
    }

    @Override
    public Class<PaytrailPaymentCreateResponse> getResponseType() {
        return PaytrailPaymentCreateResponse.class;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    public static class CreatePaymentPayload extends PaytrailPayload<CreatePaymentPayload> {
        private String stamp;
        private String reference;
        private int amount;
        private String currency;
        private String language;
        private ArrayList<PaymentItem> items;
        private PaymentCustomer customer;
        private PaymentCallbackUrls redirectUrls;
        private PaymentCallbackUrls callbackUrls;
    }
}
