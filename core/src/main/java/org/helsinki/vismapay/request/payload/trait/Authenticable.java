package org.helsinki.vismapay.request.payload.trait;

public interface Authenticable<T extends Authenticable<T>> extends Authenticated {

	@SuppressWarnings("UnusedReturnValue")
	T setApiKey(String apiKey);
}
