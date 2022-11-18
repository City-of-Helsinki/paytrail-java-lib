package org.helsinki.paytrail.request.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.AccessLevel;
import lombok.Setter;
import org.helsinki.paytrail.request.auth.constants.PaytrailAuthHeaders;
import org.helsinki.paytrail.util.DateTimeUtil;
import org.helsinki.paytrail.util.Pair;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.helsinki.paytrail.PaytrailClient;
import org.helsinki.paytrail.exception.PaytrailResponseException;
import org.helsinki.paytrail.response.PaytrailResponse;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class PaytrailRequest<T extends PaytrailResponse> {

	protected Gson gson;

	// Used to store the transaction id of the payment.
	@Setter(AccessLevel.PROTECTED)
	private String checkoutTransactionId;

	public abstract String path();

	public abstract Request formRequest(PaytrailClient client);

	public abstract Class<T> getResponseType();

	public String baseApiUrl(PaytrailClient client) {
		return client.getBaseUrl();
	}

	public String formUrl(PaytrailClient client) {
		return baseApiUrl(client) + "/" + path();
	}

	public CompletableFuture<T> execute(PaytrailClient client) {
		return client.sendRequest(this);
	}

	@SneakyThrows
	public T parseResponse(Pair<Response, String> response) {
		T paytrailResponse = null;
		String responseValue = response.getValue();
		try {
			paytrailResponse = parseResponse(responseValue);
		} catch (JsonSyntaxException exception) {
			log.info("Paytrail request parsing ignored error was . {}", exception.getMessage());
		}

		if (!response.getKey().isSuccessful()) {
			throw new PaytrailResponseException(paytrailResponse, "Response from Paytrail API wasn't a success response");
		}

		// If data cant be parsed here, add it to resultJson field for further process.
		if (responseValue != null) {
			T responseWithJson = getResponseType().getDeclaredConstructor().newInstance();
			responseWithJson.setResultJson(responseValue);
			return responseWithJson;
		}
		return paytrailResponse;
	}

	public T parseResponse(String json) throws JsonSyntaxException {
		return parseResponse(json, getResponseType());
	}

	public <U> U parseResponse(String json, Class<U> type) throws JsonSyntaxException {
		log.debug("{} parsing response : {}", path(), json);
		return getGson().fromJson(json, type);
	}

	protected Gson getGson() {
		return getGson(false);
	}

	protected Gson getGson(boolean createNew) {
		if (gson == null || createNew) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			configureGsonBuilder(gsonBuilder);
			gson = gsonBuilder.create();
		}
		return gson;
	}

	protected void configureGsonBuilder(GsonBuilder gsonBuilder) {
		// nothing by default
	}

	protected Request.Builder applyHeaders(PaytrailClient client, Request.Builder request) {
		//request.addHeader("Connection", "close");
		request.addHeader("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8);
		return request;
	}

	protected Request.Builder applyDefaultAuthHeaders(PaytrailClient client, Request.Builder request, String checkoutMethod) {
		request.addHeader(String.valueOf(PaytrailAuthHeaders.CHECKOUT_ACCOUNT), client.getInternalMerchantId());
		request.addHeader(String.valueOf(PaytrailAuthHeaders.CHECKOUT_ALGORITHM), PaytrailClient.CHECKOUT_ALGORITHM);
		request.addHeader(String.valueOf(PaytrailAuthHeaders.CHECKOUT_METHOD), checkoutMethod);
		request.addHeader(String.valueOf(PaytrailAuthHeaders.CHECKOUT_NONCE), UUID.randomUUID().toString());
		request.addHeader(String.valueOf(PaytrailAuthHeaders.CHECKOUT_TIMESTAMP), DateTimeUtil.getDateTime());
		if (this.checkoutTransactionId != null) {
			request.addHeader(String.valueOf(PaytrailAuthHeaders.CHECKOUT_TRANSACTION_ID), this.checkoutTransactionId);
		}
		return request;
	}

}
