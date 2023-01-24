package org.helsinki.paytrail.request.refunds;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.model.payments.PaymentCallbackUrls;
import org.helsinki.paytrail.model.refunds.RefundItem;
import org.helsinki.paytrail.request.common.PaytrailPostRequest;
import org.helsinki.paytrail.request.contracts.paytrail.PaytrailPayload;
import org.helsinki.paytrail.response.refunds.PaytrailRefundCreateResponse;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PaytrailRefundCreateRequest extends PaytrailPostRequest<PaytrailRefundCreateResponse> {

    private final String paymentTransactionId;
    private final CreateRefundPayload payload;

    public PaytrailRefundCreateRequest(
            @NonNull String paymentTransactionId,
            @NonNull CreateRefundPayload payload
    ) {
        this.setCheckoutTransactionId(paymentTransactionId);
        this.paymentTransactionId = paymentTransactionId;
        this.payload = payload;
    }

    @Override
    protected CreateRefundPayload getPayload(PaytrailClient client) {
        return payload;
    }

    @Override
    protected TreeMap<String, String> getRequestSpecificHeaders() {
        return null;
    }

    @Override
    public String path() {
        return "payments/" + paymentTransactionId + "/refund";
    }

    @Override
    public Class<PaytrailRefundCreateResponse> getResponseType() {
        return PaytrailRefundCreateResponse.class;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    public static class CreateRefundPayload extends PaytrailPayload<CreateRefundPayload> {
        private int amount;
        private String email;
        private String refundStamp;
        private String refundReference;
        private List<RefundItem> items;
        private PaymentCallbackUrls callbackUrls;
    }
}
