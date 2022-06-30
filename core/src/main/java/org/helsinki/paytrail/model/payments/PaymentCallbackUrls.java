package org.helsinki.paytrail.model.payments;

import lombok.Data;

@Data
public class PaymentCallbackUrls {
    public String success;
    public String cancel;
}
