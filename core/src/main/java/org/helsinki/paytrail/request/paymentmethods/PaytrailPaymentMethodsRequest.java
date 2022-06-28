package org.helsinki.paytrail.request.paymentmethods;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.request.PaytrailGetRequest;
import org.helsinki.paytrail.request.contracts.paytrail.PaytrailPayload;
import org.helsinki.paytrail.response.paymentmethods.PaytrailPaymentMethodsResponse;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PaytrailPaymentMethodsRequest extends PaytrailGetRequest<PaytrailPaymentMethodsResponse, PaytrailPaymentMethodsRequest.PaymentMethodsPayload> {
	@Override
	protected void setDefaultQueryParameters() {
		Map<String,String> params = new HashMap<>();
		if (this.payload.amount != null) {
			params.put("amount", String.valueOf(this.payload.amount));
		}
		if (this.payload.groups != null) {
			params.put("groups", this.payload.groups);
		}
		this.setGetParameters(params);
	}

	@NonNull
	private final PaytrailPaymentMethodsRequest.PaymentMethodsPayload payload;

	@Override
	protected PaytrailPaymentMethodsRequest.PaymentMethodsPayload getPayload(PaytrailClient client) {
		return payload;
	}

	@Override
	public String path() {
		return "merchants/payment-providers";
	}

	@Override
	public Class<PaytrailPaymentMethodsResponse> getResponseType() {
		return PaytrailPaymentMethodsResponse.class;
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class PaymentMethodsPayload extends PaytrailPayload<PaymentMethodsPayload> {
		private Integer amount;
		private String groups;
	}
}
