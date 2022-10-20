package org.helsinki.paytrail.model.payments;

import lombok.Data;

@Data
public class PaymentCallbackUrls {
    private String success;
    private String cancel;
}
