package org.helsinki.paytrail.model.payments;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaytrailPaymentMitChargeFailureResponse {
    private String message;
    private String status;
    private String acquirerResponseCode;
    private String acquirerResponseCodeDescription;
}
