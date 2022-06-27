package org.helsinki.paytrail.request.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.VismaPayClient;
import org.helsinki.paytrail.request.VismaPayPostRequest;
import org.helsinki.paytrail.request.payload.trait.impl.BaseOrderIdentifiablePayload;
import org.helsinki.paytrail.response.payment.PaymentDetailsResponse;
import org.helsinki.paytrail.util.AuthCodeCalculator;

import java.io.Serializable;

@RequiredArgsConstructor
public class GetPaymentRequest extends
		VismaPayPostRequest<PaymentDetailsResponse, GetPaymentRequest.GetPaymentPayload> {

	@NonNull
	private final GetPaymentRequest.GetPaymentPayload payload;

	@Override
	protected GetPaymentRequest.GetPaymentPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() + "|" + payload.getOrderNumber();
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));

		return payload;
	}

	@Override
	public String path() {
		return "get_payment";
	}

	@Override
	public Class<PaymentDetailsResponse> getResponseType() {
		return PaymentDetailsResponse.class;
	}

	/**
	 * Convenience class
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class GetPaymentPayload
			extends BaseOrderIdentifiablePayload<GetPaymentPayload>
			implements Serializable {
	}
}
