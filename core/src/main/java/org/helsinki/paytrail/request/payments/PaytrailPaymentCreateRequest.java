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

import java.util.ArrayList;

@RequiredArgsConstructor
public class PaytrailPaymentCreateRequest extends PaytrailPostRequest<PaytrailResponse, PaytrailPaymentCreateRequest.CreatePaymentPayload> {

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
    public Class<PaytrailResponse> getResponseType() {
        return PaytrailResponse.class;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    public static class CreatePaymentPayload extends PaytrailPayload<CreatePaymentPayload> {
        public String stamp;
        public String reference;
        public int amount;
        public String currency;
        public String language;
        public ArrayList<PaymentItem> items;
        public PaymentCustomer customer;
        public PaymentCallbackUrls redirectUrls;
        public PaymentCallbackUrls callbackUrls;
    }
}
