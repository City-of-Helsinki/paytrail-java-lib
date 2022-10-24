package org.helsinki.paytrail.response.payments;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.payments.PaytrailPaymentTransaction;
import org.helsinki.paytrail.response.PaytrailResponse;


@EqualsAndHashCode(callSuper = true)
@Data
public class PaytrailPaymentCreateResponse extends PaytrailResponse {
	private PaytrailPaymentTransaction paymentTransaction;
	private String[] errors;
}
