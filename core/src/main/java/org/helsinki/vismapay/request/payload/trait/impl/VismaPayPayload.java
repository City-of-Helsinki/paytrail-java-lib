package org.helsinki.vismapay.request.payload.trait.impl;

import com.google.gson.annotations.SerializedName;
import org.helsinki.vismapay.request.payload.trait.ApiKeyResource;
import org.helsinki.vismapay.request.payload.trait.Authenticable;
import org.helsinki.vismapay.request.payload.trait.Versionable;

public abstract class VismaPayPayload<T extends Versionable<T> & Authenticable<T> & ApiKeyResource<T>>
		implements Versionable<T>, Authenticable<T>, ApiKeyResource<T> {

	@SerializedName("api_key")
	private String apiKey;

	@SerializedName("authcode")
	private String authCode;

	private String version;

	public String getApiKey() {
		return apiKey;
	}

	public T setApiKey(String apiKey) {
		this.apiKey = apiKey;
		return (T) this;
	}

	public String getAuthCode() {
		return authCode;
	}

	@SuppressWarnings("UnusedReturnValue")
	public T setAuthCode(String authCode) {
		this.authCode = authCode;
		return (T) this;
	}

	public String getVersion() {
		return version;
	}

	public T setVersion(String version) {
		this.version = version;
		return (T) this;
	}
}
