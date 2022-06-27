package org.helsinki.vismapay.request.payload.trait.impl;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.request.payload.trait.CardTokenIdentifiable;

@EqualsAndHashCode(callSuper = true)
public class BaseCardTokenIdentifiablePayload<T extends VismaPayPayload<T> & CardTokenIdentifiable<T>>
		extends VismaPayPayload<T>
		implements CardTokenIdentifiable<T> {

	@SerializedName("card_token")
	private String cardToken;

	public String getCardToken() {
		return cardToken;
	}

	public T setCardToken(String cardToken) {
		this.cardToken = cardToken;
		return (T) this;
	}
}
