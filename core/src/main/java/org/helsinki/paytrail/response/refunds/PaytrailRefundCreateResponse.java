package org.helsinki.paytrail.response.refunds;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.refunds.PaytrailRefundResponse;
import org.helsinki.paytrail.response.PaytrailResponse;


@EqualsAndHashCode(callSuper = true)
@Data
public class PaytrailRefundCreateResponse extends PaytrailResponse {
	private PaytrailRefundResponse refundResponse;
	private String[] errors;
}
