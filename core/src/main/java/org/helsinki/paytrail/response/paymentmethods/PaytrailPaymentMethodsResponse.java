package org.helsinki.paytrail.response.paymentmethods;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.paymentmethods.PaytrailPaymentMethod;
import org.helsinki.paytrail.response.PaytrailResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaytrailPaymentMethodsResponse extends PaytrailResponse {

	@SerializedName("payment_methods")
	private PaytrailPaymentMethod[] paytrailPaymentMethods;

	private String[] errors;
}
