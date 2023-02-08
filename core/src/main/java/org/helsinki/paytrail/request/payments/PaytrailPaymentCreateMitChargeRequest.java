package org.helsinki.paytrail.request.payments;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.request.common.PaytrailPostRequest;
import org.helsinki.paytrail.response.payments.PaytrailPaymentCreateMitChargeResponse;

import java.util.TreeMap;

public class PaytrailPaymentCreateMitChargeRequest extends PaytrailPostRequest<PaytrailPaymentCreateMitChargeResponse> {
    private final CreateMitChargePayload payload;

    public PaytrailPaymentCreateMitChargeRequest(
            @NonNull CreateMitChargePayload payload
    ) {
        this.setCheckoutTransactionId(payload.getToken());
        this.payload = payload;
    }

    @Override
    protected CreateMitChargePayload getPayload(PaytrailClient client) {
        return payload;
    }

    @Override
    protected TreeMap<String, String> getRequestSpecificHeaders() {
        return null;
    }

    @Override
    public String path() {
        return "payments/token/mit/charge";
    }

    @Override
    public Class<PaytrailPaymentCreateMitChargeResponse> getResponseType() {
        return PaytrailPaymentCreateMitChargeResponse.class;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    public static class CreateMitChargePayload extends PaytrailPaymentCreateRequest.CreatePaymentPayload {
        private String token;
    }
}
