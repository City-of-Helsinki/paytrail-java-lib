package org.helsinki.vismapay.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.helsinki.vismapay.util.Pair;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.helsinki.vismapay.VismaPayClient;
import org.helsinki.vismapay.exception.VismaPayResponseException;
import org.helsinki.vismapay.response.VismaPayResponse;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class VismaPayRequest<T extends VismaPayResponse> {

	protected Gson gson;

	public abstract String path();

	public abstract Request formRequest(VismaPayClient client);

	public abstract Class<T> getResponseType();

	public String baseApiUrl(VismaPayClient client) {
		return client.getBaseUrl();
	}

	public String formUrl(VismaPayClient client) {
		return baseApiUrl(client) + "/" + path();
	}

	public CompletableFuture<T> execute(VismaPayClient client) {
		return client.sendRequest(this);
	}

	@SneakyThrows
	public T parseResponse(Pair<Response, String> response) {
		T vismaPayResponse = parseResponse(response.getValue());

		if (!response.getKey().isSuccessful() || vismaPayResponse.getResult() == null) {
			throw new VismaPayResponseException(vismaPayResponse, "Response from Visma Pay API wasn't a success response");
		}
		return vismaPayResponse;
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

	protected void applyHeaders(VismaPayClient client, Request.Builder requst) {
		requst.addHeader("Connection", "close");
		requst.addHeader("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8);
	}
}
