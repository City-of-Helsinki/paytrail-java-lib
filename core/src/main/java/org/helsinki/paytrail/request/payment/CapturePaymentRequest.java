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
public class CapturePaymentRequest extends
		VismaPayPostRequest<VismaPayResponse, CapturePaymentRequest.CapturePaymentPayload> {

	@NonNull
	private final CapturePaymentPayload payload;

	@Override
	protected CapturePaymentPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() + "|" + payload.getOrderNumber();
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));

		return payload;
	}

	@Override
	public String path() {
		return "capture";
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
	public static class CapturePaymentPayload
			extends BaseOrderIdentifiablePayload<CapturePaymentPayload>
			implements Serializable {
	}
}
