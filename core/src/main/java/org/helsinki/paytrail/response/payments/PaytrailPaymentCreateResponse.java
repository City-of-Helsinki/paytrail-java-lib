package org.helsinki.paytrail.response.payments;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.paymentmethods.PaytrailPaymentMethod;
import org.helsinki.paytrail.model.payments.PaytrailPayment;
import org.helsinki.paytrail.response.PaytrailResponse;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaytrailPaymentCreateResponse extends PaytrailResponse {

	private PaytrailPayment payment;

	private String[] errors;
}
