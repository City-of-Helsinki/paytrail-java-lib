package org.helsinki.paytrail.model.payments;

import lombok.Data;

@Data
public class PaymentItem {
    private String stamp;
    private String reference;
    private String merchant;
    private int unitPrice;
    private int units;
    private int vatPercentage;
    private String productCode;
    private String description;
    private String orderId;
}
