package org.helsinki.vismapay.response.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.model.payment.Payment;
import org.helsinki.vismapay.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentDetailsResponse extends VismaPayResponse {
	private Payment payment;
	private String[] errors;
}
