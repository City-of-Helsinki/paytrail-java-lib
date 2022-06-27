package org.helsinki.paytrail.response.paymentmethods;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.paymentmethods.PaymentMethod;
import org.helsinki.paytrail.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentMethodsResponse extends VismaPayResponse {

	@SerializedName("payment_methods")
	private PaymentMethod[] paymentMethods;

	private String[] errors;
}
