package org.helsinki.paytrail.model.payments;

import lombok.Data;

@Data
public class PaymentItem {

    public String stamp;
    public String regerence;
    public String merchant;
    public int unitPrice;
    public int units;
    public int vatPercentage;
    public String productCode;
    public String description;
    public String orderId;
}
