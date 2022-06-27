package org.helsinki.vismapay.response.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.model.payment.Refund;
import org.helsinki.vismapay.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class RefundDetailsResponse extends VismaPayResponse {
	private Refund refund;
	private String[] errors;
}
