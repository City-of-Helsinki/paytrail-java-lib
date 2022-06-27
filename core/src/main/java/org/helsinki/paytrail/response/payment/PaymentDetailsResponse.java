package org.helsinki.paytrail.response.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.payment.Payment;
import org.helsinki.paytrail.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentDetailsResponse extends VismaPayResponse {
	private Payment payment;
	private String[] errors;
}
