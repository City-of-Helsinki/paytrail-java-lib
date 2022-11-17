package org.helsinki.paytrail.model.refunds;

import lombok.Data;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.constants.RefundStatus;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class PaytrailRefundResponse implements Serializable {
    private String transactionId;
    private String provider;
    private RefundStatus status;
}
