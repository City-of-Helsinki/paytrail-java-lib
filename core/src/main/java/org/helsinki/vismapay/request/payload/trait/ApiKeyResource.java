package org.helsinki.vismapay.request.payload.trait;

public interface ApiKeyResource<T extends ApiKeyResource<T>> {

	String getApiKey();

	T setApiKey(String apiKey);
}
