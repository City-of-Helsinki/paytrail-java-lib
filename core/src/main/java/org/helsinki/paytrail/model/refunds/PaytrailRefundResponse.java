package org.helsinki.paytrail.model.refunds;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.constants.RefundStatus;
import org.helsinki.paytrail.response.PaytrailResponse;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaytrailRefundResponse extends PaytrailResponse implements Serializable {
    private String transactionId;
    private String provider;
    private RefundStatus status;
}
