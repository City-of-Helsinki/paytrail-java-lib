package org.helsinki.vismapay.request.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.request.VismaPayPostRequest;
import org.helsinki.vismapay.request.payload.trait.impl.BaseRefundIdentifiablePayload;
import org.helsinki.vismapay.response.payment.CancelRefundResponse;
import org.helsinki.vismapay.util.AuthCodeCalculator;

@RequiredArgsConstructor
public class CancelRefundRequest
		extends VismaPayPostRequest<CancelRefundResponse, CancelRefundRequest.CancelRefundPayload> {

	@NonNull
	private final CancelRefundPayload payload;

	@Override
	protected CancelRefundPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() + "|" + payload.getRefundId();
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));
		return payload;
	}

	@Override
	public String path() {
		return "cancel_refund";
	}

	@Override
	public Class<CancelRefundResponse> getResponseType() {
		return CancelRefundResponse.class;
	}

	/**
	 * Convenience class
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class CancelRefundPayload extends BaseRefundIdentifiablePayload<CancelRefundPayload> {
	}
}
