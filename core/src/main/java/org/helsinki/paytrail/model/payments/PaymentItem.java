package org.helsinki.paytrail.model.payments;

import lombok.Data;

@Data
public class PaymentItem {
    public int unitPrice;
    public int units;
    public int vatPercentage;
    public String productCode;
}
