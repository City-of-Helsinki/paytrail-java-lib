package org.helsinki.paytrail.request.payload.trait;

public interface CardTokenIdentifiable<T extends CardTokenIdentifiable<T>> extends CardTokenIdentified {

	T setCardToken(String cardToken);
}