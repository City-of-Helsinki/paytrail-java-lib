package org.helsinki.paytrail.response.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.payment.Refund;
import org.helsinki.paytrail.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class RefundDetailsResponse extends VismaPayResponse {
	private Refund refund;
	private String[] errors;
}
