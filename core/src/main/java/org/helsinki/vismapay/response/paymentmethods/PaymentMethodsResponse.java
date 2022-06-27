package org.helsinki.vismapay.response.paymentmethods;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.model.paymentmethods.PaymentMethod;
import org.helsinki.vismapay.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentMethodsResponse extends VismaPayResponse {

	@SerializedName("payment_methods")
	private PaymentMethod[] paymentMethods;

	private String[] errors;
}
