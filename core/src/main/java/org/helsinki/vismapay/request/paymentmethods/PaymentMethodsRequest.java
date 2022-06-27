package org.helsinki.vismapay.request.paymentmethods;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.request.VismaPayPostRequest;
import org.helsinki.vismapay.request.payload.trait.impl.VismaPayPayload;
import org.helsinki.vismapay.response.paymentmethods.PaymentMethodsResponse;
import org.helsinki.vismapay.util.AuthCodeCalculator;

@RequiredArgsConstructor
public class PaymentMethodsRequest extends VismaPayPostRequest<PaymentMethodsResponse, PaymentMethodsRequest.PaymentMethodsPayload> {

	@NonNull
	private final PaymentMethodsRequest.PaymentMethodsPayload payload;

	@Override
	protected PaymentMethodsRequest.PaymentMethodsPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey();
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));
		return payload;
	}

	@Override
	public String path() {
		return "merchant_payment_methods";
	}

	@Override
	public Class<PaymentMethodsResponse> getResponseType() {
		return PaymentMethodsResponse.class;
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class PaymentMethodsPayload extends VismaPayPayload<PaymentMethodsRequest.PaymentMethodsPayload> {
		private String currency;
	}
}
