package org.helsinki.vismapay.request.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.request.VismaPayPostRequest;
import org.helsinki.vismapay.request.payload.trait.impl.BaseOrderIdentifiablePayload;
import org.helsinki.vismapay.response.payment.PaymentStatusResponse;
import org.helsinki.vismapay.util.AuthCodeCalculator;

import java.io.Serializable;

@RequiredArgsConstructor
public class CheckPaymentStatusRequest extends
		VismaPayPostRequest<PaymentStatusResponse, CheckPaymentStatusRequest.PaymentStatusPayload> {

	@NonNull
	private final PaymentStatusPayload payload;

	@Override
	protected PaymentStatusPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() +
				"|" + (payload.getToken() != null ? payload.getToken() : payload.getOrderNumber());
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));

		return payload;
	}

	@Override
	public String path() {
		return "check_payment_status";
	}

	@Override
	public Class<PaymentStatusResponse> getResponseType() {
		return PaymentStatusResponse.class;
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class PaymentStatusPayload
			extends BaseOrderIdentifiablePayload<PaymentStatusPayload>
			implements Serializable {
		private String token;
	}
}
