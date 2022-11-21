package org.helsinki.paytrail.request.refunds;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.model.payments.PaymentCallbackUrls;
import org.helsinki.paytrail.model.refunds.RefundItem;
import org.helsinki.paytrail.request.common.PaytrailPostRequest;
import org.helsinki.paytrail.request.contracts.paytrail.PaytrailPayload;
import org.helsinki.paytrail.response.refunds.PaytrailRefundCreateResponse;

import java.util.List;

public class PaytrailRefundCreateRequest extends PaytrailPostRequest<PaytrailRefundCreateResponse, PaytrailRefundCreateRequest.CreateRefundPayload> {

    private final String paymentTransactionId;
    private final PaytrailRefundCreateRequest.CreateRefundPayload payload;

    public PaytrailRefundCreateRequest(
            @NonNull String paymentTransactionId,
            @NonNull PaytrailRefundCreateRequest.CreateRefundPayload payload
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
