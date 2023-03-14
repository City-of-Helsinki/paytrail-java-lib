package org.helsinki.paytrail.model.payments;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class PaytrailPaymentMitChargeSuccessResponse implements Serializable {
    private String transactionId;
}
