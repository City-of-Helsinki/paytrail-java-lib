package org.helsinki.vismapay.response.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.model.payment.Source;
import org.helsinki.vismapay.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentStatusResponse extends VismaPayResponse {
	private Byte settled;
	private String type;
	private Source source;
}
