package org.helsinki.paytrail.response.paymentmethods;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.paymentmethods.PaytrailPaymentMethod;
import org.helsinki.paytrail.response.PaytrailResponse;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaytrailPaymentMethodsResponse extends PaytrailResponse {

	private List<PaytrailPaymentMethod> paymentMethods;
	private String[] errors;
}
