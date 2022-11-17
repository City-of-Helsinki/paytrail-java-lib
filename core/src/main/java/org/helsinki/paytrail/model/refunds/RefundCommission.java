package org.helsinki.paytrail.model.refunds;

import lombok.Data;

@Data
public class RefundCommission {
    private String merchant;
    private int amount;
}
