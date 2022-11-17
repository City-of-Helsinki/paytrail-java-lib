package org.helsinki.paytrail.model.refunds;

import lombok.Data;

@Data
public class RefundItem {
    private int amount;
    private String stamp;
    private String refundStamp;
    private String refundReference;
    private RefundCommission commission;
}
