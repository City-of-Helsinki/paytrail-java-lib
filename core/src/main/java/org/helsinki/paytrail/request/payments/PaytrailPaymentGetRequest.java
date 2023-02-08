package org.helsinki.paytrail.request.payments;

import lombok.NonNull;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.request.common.PaytrailGetRequest;
import org.helsinki.paytrail.request.contracts.paytrail.PaytrailPayload;
import org.helsinki.paytrail.response.payments.PaytrailPaymentGetResponse;

public class PaytrailPaymentGetRequest extends PaytrailGetRequest<PaytrailPaymentGetResponse> {
    private final String transactionId;

    public PaytrailPaymentGetRequest(@NonNull String transactionId) {
        this.transactionId = transactionId;
        this.setCheckoutTransactionId(transactionId);
    }

    @Override
    public String path() { return "payments/" + this.transactionId; }

    @Override
    protected PaytrailPayload<?> getPayload(PaytrailClient client) {
        return null;
    }

    @Override
    public Class<PaytrailPaymentGetResponse> getResponseType() {
        return PaytrailPaymentGetResponse.class;
    }
}
