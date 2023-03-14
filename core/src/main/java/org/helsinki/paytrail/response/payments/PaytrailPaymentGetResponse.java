package org.helsinki.paytrail.response.payments;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.payments.PaytrailPayment;
import org.helsinki.paytrail.response.PaytrailResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaytrailPaymentGetResponse extends PaytrailResponse {
    private PaytrailPayment paytrailPayment;
    private String[] errors;
}
