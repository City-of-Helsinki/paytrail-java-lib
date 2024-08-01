package org.helsinki.paytrail.model.payments;

import lombok.Data;

@Data
public class PaymentItem {
    private String stamp;
    private String reference;
    private String merchant;
    private int unitPrice;
    private int units;
    // for decimal handling (KYV-1064)
//    private double vatPercentage;
    private int vatPercentage;
    private String productCode;
    private String description;
    private String orderId;
}
