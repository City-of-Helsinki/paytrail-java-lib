package org.helsinki.paytrail.request.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.paytrail.VismaPayClient;
import org.helsinki.paytrail.request.VismaPayPostRequest;
import org.helsinki.paytrail.request.payload.trait.impl.BaseOrderIdentifiablePayload;
import org.helsinki.paytrail.response.VismaPayResponse;
import org.helsinki.paytrail.util.AuthCodeCalculator;

import java.io.Serializable;

@RequiredArgsConstructor
public class CancelPaymentRequest extends
		VismaPayPostRequest<VismaPayResponse, CancelPaymentRequest.CancelPaymentPayload> {

	@NonNull
	private final CancelPaymentPayload payload;

	@Override
	protected CancelPaymentPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() + "|" + payload.getOrderNumber();
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));

		return payload;
	}

	@Override
	public String path() {
		return "cancel";
	}

	@Override
	public Class<VismaPayResponse> getResponseType() {
		return VismaPayResponse.class;
	}

	/**
	 * Convenience class
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class CancelPaymentPayload
			extends BaseOrderIdentifiablePayload<CancelPaymentPayload>
			implements Serializable {
	}
}
