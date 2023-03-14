package org.helsinki.paytrail.response.payments;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.payments.PaytrailPaymentMitChargeFailureResponse;
import org.helsinki.paytrail.model.payments.PaytrailPaymentMitChargeSuccessResponse;
import org.helsinki.paytrail.response.PaytrailResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaytrailPaymentCreateMitChargeResponse extends PaytrailResponse {
    private PaytrailPaymentMitChargeSuccessResponse success;
    private PaytrailPaymentMitChargeFailureResponse failure;
    private String[] errors;
}
