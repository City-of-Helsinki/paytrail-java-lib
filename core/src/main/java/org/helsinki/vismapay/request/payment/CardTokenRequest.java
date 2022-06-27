package org.helsinki.vismapay.request.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.request.VismaPayPostRequest;
import org.helsinki.vismapay.request.payload.trait.impl.BaseCardTokenIdentifiablePayload;
import org.helsinki.vismapay.request.payload.trait.impl.VismaPayPayload;
import org.helsinki.vismapay.response.payment.CardTokenResponse;
import org.helsinki.vismapay.util.AuthCodeCalculator;

@RequiredArgsConstructor
public class CardTokenRequest extends VismaPayPostRequest<CardTokenResponse, CardTokenRequest.CardTokenPayload> {

	@NonNull
	private final CardTokenPayload payload;

	@Override
	protected CardTokenPayload getPayload(VismaPayClient client) {
		String authCodeStr = client.getApiKey() + "|" + payload.getCardToken();
		payload.setAuthCode(AuthCodeCalculator.calcAuthCode(client.getPrivateKey(), authCodeStr));

		return payload;
	}

	@Override
	public String path() {
		return "get_card_token";
	}

	@Override
	public Class<CardTokenResponse> getResponseType() {
		return CardTokenResponse.class;
	}

	/**
	 * Convenience class
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	@Accessors(chain = true)
	public static class CardTokenPayload extends BaseCardTokenIdentifiablePayload<CardTokenPayload> {
	}
}
