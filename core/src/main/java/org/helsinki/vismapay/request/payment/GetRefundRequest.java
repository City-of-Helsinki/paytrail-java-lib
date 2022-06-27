package org.helsinki.vismapay.request.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.request.VismaPayPostRequest;
import org.helsinki.vismapay.request.payload.trait.impl.BaseRefundIdentifiablePayload;
import org.helsinki.vismapay.response.payment.RefundDetailsResponse;
import org.helsinki.vismapay.util.AuthCodeCalculator;

import java.io.Serializable;

@RequiredArgsConstructor
public class GetRefundRequest extends
		VismaPayPostRequest<RefundDetailsResponse, GetRefundRequest.GetRefundPayload> {

	@NonNull
	private final GetRefundRequest.GetRefundPayload payload;

	@Override
	protected GetRefundRequest.GetRefundPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() + "|" + payload.getRefundId();
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));

		return payload;
	}

	@Override
	public String path() {
		return "get_refund";
	}

	@Override
	public Class<RefundDetailsResponse> getResponseType() {
		return RefundDetailsResponse.class;
	}

	/**
	 * Convenience class
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class GetRefundPayload
			extends BaseRefundIdentifiablePayload<GetRefundPayload>
			implements Serializable {
	}
}
